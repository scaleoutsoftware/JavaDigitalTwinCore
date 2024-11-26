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
import java.util.function.Predicate;

class SimulationWorker implements Callable<SimulationStep> {
    private final Logger                                        _logger = LogManager.getLogger(SimulationWorker.class);
    private final PriorityQueue<SimulationEvent>                _timeOrderedQueue = new PriorityQueue<>();
    private final ConcurrentHashMap<String, SimulationEvent>    _timers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, SimulationEvent>    _events = new ConcurrentHashMap<>();
    private final int                                           _slotId;
    private final String                                        _modelName;
    private final SimulationProcessor                           _simulationProcessor;
    private final TwinExecutionEngine                           _twinExecutionEngine;
    private final SimulationScheduler                           _simulationScheduler;
    private long                                                _curSimulationTime;
    private long                                                _simulationInterval;
    private long                                                _nextSimulationTime;
    private boolean                                             _running;

    public SimulationWorker(int slotId,
                            String model,
                            SimulationProcessor modelProcessor,
                            Class<? extends DigitalTwinBase> digitalTwinClass,
                            TwinExecutionEngine engine,
                            SimulationScheduler scheduler) {
        _slotId                 = slotId;
        _modelName              = model;
        _simulationProcessor    = modelProcessor;
        _twinExecutionEngine    = engine;
        _simulationScheduler    = scheduler;
    }

    public void reset(SimulationStepArgs runSimulationEventArgs) {
        _curSimulationTime  = runSimulationEventArgs.getCurSimulationTime();
        _simulationInterval = runSimulationEventArgs.getIterationSize();
        _logger.info(String.format("Worker reset... cur: %s interval: %s", _curSimulationTime, _simulationInterval));
    }

    public void shutdown() {
        _timeOrderedQueue.clear();
        _events.clear();
        _timers.clear();
    }

    public void addTwinToQueue(TwinProxy proxy) {
        SimulationEvent event = new SimulationEventTwinImpl(_curSimulationTime, proxy, _simulationProcessor);
        _timeOrderedQueue.add(event);
        _events.put(String.format("%s%s",event.getModel(),event.getId()), event);
    }

    public void addTwinToQueue(SimulationEvent event) {
        _timeOrderedQueue.add(event);
        _events.put(String.format("%s%s",event.getModel(),event.getId()), event);
    }

    public void addTimerToQueue(TwinProxy proxy, String modelName, String id, String timerName, TimerType type, Duration interval, TimerHandler handler) {
        SimulationEvent event = new SimulationEventTimerImpl(modelName, id, interval.toMillis(), timerName, proxy, handler);
        _timers.put(timerName, event);
        _timeOrderedQueue.add(event);
        _events.put(String.format("%s%s",event.getModel(),event.getId()), event);
    }

    public void stopTimer(String model, String id, String timerName) {
        SimulationEvent event = _timers.remove(String.format("%s%s%s",model, id,timerName));
        event.setProxyState(ProxyState.Removed);
        _events.remove(String.format("%s%s",event.getModel(),event.getId()));
    }

    public void runThisInstance(String model, String id) throws WorkbenchException {
        SimulationEvent event = _events.remove(String.format("%s%s",model,id));
        if(event == null) {
            TwinProxy proxy = _twinExecutionEngine.getTwinProxy(model, id);
            event = new SimulationEventTwinImpl(_curSimulationTime, proxy, _simulationProcessor);
        } else {
            _timeOrderedQueue.remove(event);
        }
        WorkbenchSimulationController simulationController = new WorkbenchSimulationController(_twinExecutionEngine, _simulationScheduler);
        WorkbenchProcessingContext processingContext = new WorkbenchProcessingContext(_twinExecutionEngine, simulationController);
        processingContext.reset(model, id, null);
        Date date = new Date();
        date.setTime(_curSimulationTime);
        event.processSimulationEvent(processingContext, date);
        if(simulationController.delayRequested()) {
            long delay = simulationController.getRequestedDelay();
            if(delay == 0x0000e677d21fdbffL) {
                event.setPriority(simulationController.getRequestedDelay());
                event.setNextSimulationTime(simulationController.getRequestedDelay());
            } else if (delay == 0L) {
                event.setPriority(_curSimulationTime);
                event.setNextSimulationTime(_curSimulationTime);
            } else {
                event.setPriority(_curSimulationTime + simulationController.getRequestedDelay());
                event.setNextSimulationTime(_curSimulationTime + simulationController.getRequestedDelay());
            }
        } else {
            event.setPriority(_curSimulationTime + _simulationInterval);
            event.setNextSimulationTime(_curSimulationTime + _simulationInterval);
        }
        _events.put(String.format("%s%s",model,id), event);
        _timeOrderedQueue.add(event);
    }

    @Override
    public SimulationStep call() throws Exception {
        synchronized (this) {
            _running = true;
        }
        SimulationTime simulationTime = new SimulationTime(_curSimulationTime, _simulationInterval);
        long lowestNextSimulationTime = Long.MAX_VALUE;
        long nextQueueTm = Long.MAX_VALUE;
        boolean keepProcessing = true;
        boolean delayed = false;
        boolean addToBuffer = true;
        List<SimulationEvent> buffer = new LinkedList<>();
        WorkbenchSimulationController simulationController = new WorkbenchSimulationController(_twinExecutionEngine, _simulationScheduler);
        WorkbenchProcessingContext processingContext = new WorkbenchProcessingContext(_twinExecutionEngine, simulationController);
        Date currentTime = new Date();
        currentTime.setTime(_curSimulationTime);
        int processed = 0;
        do {
            addToBuffer = true;
            SimulationEvent next = _timeOrderedQueue.poll();
            if(next != null) {
                if(next.getProxyState() == ProxyState.Active) {
                    processed++;
                    nextQueueTm = next.getPriority();
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
                        if(simulationController.delayRequested()) {
                            delayed = true;
                            long delay = simulationController.getRequestedDelay();
                            if(delay == 0x0000e677d21fdbffL) {
                                next.setPriority(simulationController.getRequestedDelay());
                                next.setNextSimulationTime(simulationController.getRequestedDelay());
                            } else if (delay == 0L) {
                                next.setPriority(_curSimulationTime);
                                next.setNextSimulationTime(_curSimulationTime);
                                addToBuffer = false;
                            } else {
                                next.setPriority(simulationTime.getCurrentSimulationTime() + simulationController.getRequestedDelay());
                                next.setNextSimulationTime(simulationTime.getCurrentSimulationTime() + simulationController.getRequestedDelay());
                            }
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
                        if(!simulationController.enqueue()) {
                            // the user called "runThisInstance" -- the work item has already been re-enqueued for the
                            // current time slice.
                            addToBuffer = false;
                        }
                    } else {
                        synchronized (this) {
                            _running = false;
                        }
                        keepProcessing = false;
                    }
                    if(!simulationController.deleted() && addToBuffer) {
                        buffer.add(next);
                    }
                }
            } else {
                synchronized (this) {
                    _running = false;
                }
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
