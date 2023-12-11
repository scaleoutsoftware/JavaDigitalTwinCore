/*
 Copyright (c) 2023 by ScaleOut Software, Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/
package com.scaleoutsoftware.digitaltwin.development;

import com.scaleoutsoftware.digitaltwin.core.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

class SimulationWorker implements Callable<SimulationStep> {
    private final Logger                                        _logger = LogManager.getLogger(SimulationWorker.class);
    private final PriorityQueue<SimulationEvent>                _timeOrderedQueue = new PriorityQueue<>();
    private final ConcurrentHashMap<String, SimulationEvent>    _timers = new ConcurrentHashMap<>();
    private final int                                           _slotId;
    private final String                                        _modelName;
    private final SimulationProcessor                           _simulationProcessor;
    private final TwinExecutionEngine                           _twinExecutionEngine;
    private long                                                _curSimulationTime;
    private long                                                _simulationInterval;
    private long                                                _nextSimulationTime;

    public SimulationWorker(int slotId,
                            String model,
                            SimulationProcessor modelProcessor,
                            Class<? extends DigitalTwinBase> digitalTwinClass,
                            TwinExecutionEngine engine) {
        _slotId                 = slotId;
        _modelName              = model;
        _simulationProcessor    = modelProcessor;
        _twinExecutionEngine    = engine;
    }

    public void reset(SimulationStepArgs runSimulationEventArgs) {
        _curSimulationTime  = runSimulationEventArgs.getCurSimulationTime();
        _simulationInterval = runSimulationEventArgs.getIterationSize();
        _logger.info(String.format("Worker reset... cur: %s interval: %s", _curSimulationTime, _simulationInterval));
    }

    public void shutdown() {
        _timeOrderedQueue.clear();
    }

    public void addTwinToQueue(TwinProxy proxy) {
        SimulationEvent event = new SimulationEventTwinImpl(0, proxy, _simulationProcessor);
        _timeOrderedQueue.add(event);
    }

    public void addTimerToQueue(TwinProxy proxy, String modelName, String id, String timerName, TimerType type, Duration interval, TimerHandler handler) {
        SimulationEvent event = new SimulationEventTimerImpl(modelName, id, interval.toMillis(), timerName, proxy, handler);
        _timers.put(timerName, event);
        _timeOrderedQueue.add(event);
    }

    public void stopTimer(String model, String id, String timerName) {
        SimulationEvent event = _timers.remove(String.format("%s%s%s",model, id,timerName));
        event.setProxyState(ProxyState.Removed);
    }

    @Override
    public SimulationStep call() throws Exception {
        SimulationTime simulationTime = new SimulationTime(_curSimulationTime, _simulationInterval);
        long lowestNextSimulationTime = Long.MAX_VALUE;
        long nextQueueTm = Long.MAX_VALUE;
        boolean keepProcessing = true;
        boolean delayed = false;
        List<SimulationEvent> buffer = new LinkedList<>();
        WorkbenchSimulationController simulationController = new WorkbenchSimulationController(_twinExecutionEngine);
        WorkbenchProcessingContext processingContext = new WorkbenchProcessingContext(_twinExecutionEngine, simulationController);
        Date currentTime = new Date();
        currentTime.setTime(_curSimulationTime);
        int processed = 0;
        do {
            SimulationEvent next = _timeOrderedQueue.poll();
            if(next != null) {
                if(next.getProxyState() == ProxyState.Active) {
                    processed++;
                    nextQueueTm                 = next.getPriority();
                    if(next.getPriority() <= simulationTime.getCurrentSimulationTime()) {
                        simulationController.reset(_modelName, next.getId());
                        processingContext.reset(_modelName, next.getId(), null);
                        ProcessingResult result = ProcessingResult.NoUpdate;
                        try {
                            next.processSimulationEvent(processingContext, currentTime);
                        } catch (Exception e) {
                            _logger.error("simulation processor threw an exception.", e);
                            result = ProcessingResult.NoUpdate;
                        }
                        if(simulationController.getRequestedDelay() != Long.MIN_VALUE && simulationController.getRequestedDelay() != 0) {
                            delayed = true;
                            next.setPriority(simulationTime.getCurrentSimulationTime() + simulationController.getRequestedDelay());
                            next.setNextSimulationTime(simulationTime.getCurrentSimulationTime() + simulationController.getRequestedDelay());
                        } else {
                            next.setPriority(simulationTime.getNextSimulationTime());
                            next.setNextSimulationTime(simulationTime.getNextSimulationTime());
                        }
                        if(lowestNextSimulationTime > next.getPriority()) {
                            lowestNextSimulationTime = next.getPriority();
                        }
                        if(simulationController.deleted()) {
                            result = ProcessingResult.NoUpdate;
                        }

                    } else {
                        keepProcessing = false;
                    }
                    if(!simulationController.deleted()) {
                        buffer.add(next);
                    }
                }
            } else {
                keepProcessing  = false;
                nextQueueTm     = Long.MAX_VALUE;
            }
        } while (keepProcessing);

        _timeOrderedQueue.addAll(buffer);
        _nextSimulationTime = Math.min(lowestNextSimulationTime, nextQueueTm);

        if(_nextSimulationTime == Long.MAX_VALUE && !delayed) { // check to make sure the user didn't set delay to Long.MAX_VALUE
            _nextSimulationTime = simulationTime.getNextSimulationTime();
        }

        SimulationScheduler.PROCESSED.addAndGet(processed);
        SimulationScheduler.QUEUED.addAndGet(_timeOrderedQueue.size());
        if(processed > 0) {
            return new SimulationStep(simulationController.getSimulationStatus(), _nextSimulationTime);
        } else {
            return new SimulationStep(SimulationStatus.NoRemainingWork, _nextSimulationTime);
        }
    }
}
