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

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class SimulationScheduler {
    static AtomicInteger PROCESSED   = new AtomicInteger(0);
    static AtomicInteger QUEUED      = new AtomicInteger(0);
    static AtomicInteger SENT        = new AtomicInteger(0);
    private final List<SimulationWorker>                    _workers;
    private final ExecutorService                           _simulationService;
    private final String                                    _modelName;
    private final SimulationProcessor                       _simulationProcessor;
    private final Logger                                    _logger     = LogManager.getLogger(SimulationScheduler.class);
    private long                                            _curSimulationTime;
    private Date                                            _simulationStartTime;
    private boolean                                         _isActive;


    public SimulationScheduler(String modelName,
                     Class digitalTwinClass,
                     SimulationProcessor<? extends DigitalTwinBase> modelProcessor,
                     TwinExecutionEngine executor,
                     int numWorkers) {
        _modelName              = modelName;
        _simulationProcessor    = modelProcessor;
        _workers                = new ArrayList<>(numWorkers);
        _simulationService      = Executors.newFixedThreadPool(numWorkers, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "SimulationWorker");
                t.setName(t.getName()+"-"+t.getId());
                t.setDaemon(true);
                return t;
            }
        });
        for(int i = 0; i < numWorkers; i++) {
            _workers.add(new SimulationWorker(i, _modelName, _simulationProcessor, digitalTwinClass, executor, this));
        }
    }

    // -------------- package private methods ----------------
    SimulationStep runSimulation(SimulationStepArgs runSimulationEventArgs) {
        _logger.info("Received run simulation event with args: " + runSimulationEventArgs.getCurSimulationTime() + " iterationSize: " + runSimulationEventArgs.getIterationSize() +  " flags: " + runSimulationEventArgs.getSimulationFlags() );
        long current = System.currentTimeMillis();
        SimulationStep ret;
        _curSimulationTime = runSimulationEventArgs.getCurSimulationTime();

        if (runSimulationEventArgs.getSimulationFlags() == WorkbenchSimulationFlags.Stop) {
            _logger.info("Stopping simulation; shutting down workers.");
            for(SimulationWorker worker : _workers) {
                worker.shutdown();
            }
            return new SimulationStep(SimulationStatus.UserRequested,_curSimulationTime);
        } if(runSimulationEventArgs.getSimulationFlags() == WorkbenchSimulationFlags.Start) {
            _logger.info("Starting simulation; initializing instances.");
            for(SimulationWorker worker : _workers) {
                worker.initSimulation(new Date(runSimulationEventArgs.getCurSimulationTime()));
            }
            return new SimulationStep(SimulationStatus.Running,_curSimulationTime);
        } else {
            ret = runSimulationStep(runSimulationEventArgs);
        }

        _logger.info(String.format("runSim complete in %s ms... returning next: %s", (System.currentTimeMillis()-current), ret));
        return ret;
    }

    long getCurrentTime() {
        if(_isActive) {
            return _curSimulationTime;
        }
        return System.currentTimeMillis();
    }

    void setStatus(boolean active) {
        _isActive = active;
    }

    Date getSimulationStartTime() {
        return _simulationStartTime;
    }

    void setSimulationStartTime(Date simulationStartTime) {
        _simulationStartTime = simulationStartTime;
    }



    // -------------- private methods ----------------
    private SimulationStep runSimulationStep(SimulationStepArgs args) {
        long currentTimeMs = System.currentTimeMillis();
        List<Future<SimulationStep>> futures = new LinkedList<>();
        for(SimulationWorker worker : _workers) {
            worker.reset(args);
            futures.add(_simulationService.submit(worker));
        }

        SimulationStatus status = SimulationStatus.Running;
        boolean workFound = false;
        long next = -1;
        long cur = Long.MAX_VALUE;
        for(Future<SimulationStep> f : futures) {
            try {
                SimulationStep result = f.get();
                if(result.getStatus() == SimulationStatus.Running) {
                    workFound = true;
                }
                if(result.getStatus() != SimulationStatus.Running) {
                    status = result.getStatus();
                }
                cur = result.getTime();
                if (cur != -1 && cur < next) {
                    _logger.info(String.format("future return is lower than next... cur: %s next: %s", cur, next));
                    next = cur;
                } else if (next == -1 && cur > -1) {
                    next = cur;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        _logger.info(String.format("Simulation step complete in %s ms... returning next: %s", (System.currentTimeMillis()-currentTimeMs), next));

        if(workFound && status == SimulationStatus.NoRemainingWork) status = SimulationStatus.Running;
        return new SimulationStep(status, next);
    }

    void addInstance(TwinProxy proxy) {
        SimulationWorker worker = _workers.get(findSlotId(proxy.getInstance().getId()));
        worker.addTwinToQueue(proxy);
    }

    void addTimer(TwinProxy proxy, String modelName, String id, String timerName, TimerType type, Duration interval, TimerHandler handler) {
        SimulationWorker worker = _workers.get(findSlotId(proxy.getInstance().getId()));
        worker.addTimerToQueue(proxy, modelName, id, timerName, type, interval, handler);
    }
    void stopTimer(String modelName, String id, String timerName) {
        SimulationWorker worker = _workers.get(findSlotId(id));
        worker.stopTimer(modelName, id, timerName);
    }

    void runThisInstance(String model, String id) throws WorkbenchException {
        SimulationWorker worker = _workers.get(findSlotId(id));
        worker.runThisInstance(model, id);
    }

    private int findSlotId(String id) {
        return (int)((Constants.getHash(id.getBytes(StandardCharsets.UTF_8))) % (long)_workers.size());
    }


}
