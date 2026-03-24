/*
 Copyright (c) 2026 by ScaleOut Software, Inc.

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

import com.google.gson.Gson;
import com.scaleoutsoftware.digitaltwin.abstractions.*;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

class TwinExecutionEngine implements Closeable {
    private List<String>                                                                    _modelNames;
    private ConcurrentHashMap<String, Class<? extends DigitalTwinBase<?>>>                  _digitalTwins;
    private ConcurrentHashMap<String, MessageProcessor<?>>                                  _messageProcessors;
    private ConcurrentHashMap<String, SimulationProcessor<?>>                               _simulationProcessors;
    private ConcurrentHashMap<String, ConcurrentHashMap<String, TwinProxy>>                 _modelInstances;
    private ConcurrentHashMap<String,String>                                                _alertProviders;
    private ConcurrentHashMap<String, HashMap<String,byte[]>>                               _modelsSharedData;
    private HashMap<String,byte[]>                                                          _globalSharedData;
    private Workbench                                                                       _workbench;
    private ConcurrentHashMap<String, SimulationScheduler>                                  _simulationSchedulers;
    private ConcurrentHashMap<String, WorkbenchTimerTask>                                   _realTimeTimers;
    private Gson                                                                            _gson;


    TwinExecutionEngine(Workbench workbench) {
        _workbench = workbench;
        init();
    }

    void init(   ) {
        _modelNames             = new LinkedList<>();
        _digitalTwins           = new ConcurrentHashMap<>();
        _messageProcessors      = new ConcurrentHashMap<>();
        _simulationProcessors   = new ConcurrentHashMap<>();
        _modelInstances         = new ConcurrentHashMap<>();
        _modelsSharedData       = new ConcurrentHashMap<>();
        _globalSharedData       = new HashMap<>();
        _alertProviders         = new ConcurrentHashMap<>();
        _simulationSchedulers   = new ConcurrentHashMap<>();
        _realTimeTimers         = new ConcurrentHashMap<>();
        _gson                   = new Gson();
    }

    void addDigitalTwin(String digitalTwinModelName, MessageProcessor digitalTwinMessageProcessor, Class dtType) {
        _modelNames.add(digitalTwinModelName);
        _digitalTwins.put(digitalTwinModelName, dtType);
        _messageProcessors.put(digitalTwinModelName, digitalTwinMessageProcessor);
    }

    void addDigitalTwin(String digitalTwinModelName, MessageProcessor digitalTwinMessageProcessor, SimulationProcessor simulationProcessor, Class dtType, int numWorkers) {
        _modelNames.add(digitalTwinModelName);
        _digitalTwins.put(digitalTwinModelName, dtType);
        _messageProcessors.put(digitalTwinModelName, digitalTwinMessageProcessor);
        _simulationProcessors.put(digitalTwinModelName, simulationProcessor);
        _simulationSchedulers.put(digitalTwinModelName, new SimulationScheduler(digitalTwinModelName, dtType, simulationProcessor, this, numWorkers));
    }

    void addTimer(String modelName, String id, String timerName, TimerType type, Duration interval, TimerHandler handler) {
        ConcurrentHashMap<String,TwinProxy> modelInstances = _modelInstances.get(modelName);
        TwinProxy proxy = modelInstances.get(id);
        SimulationScheduler scheduler = _simulationSchedulers.get(modelName);
        if (scheduler != null) {
            scheduler.addTimer(proxy, modelName, id, timerName, type, interval, handler);
        } else {
            Timer timer = new Timer();
            WorkbenchTimerTask task = new WorkbenchTimerTask(this, modelName, id, timerName, proxy, type, interval, handler);
            _realTimeTimers.put(String.format("%s%s%s", modelName, id, timerName), task);
            switch(type) {
                case OneTime:
                    timer.schedule(task, interval.toMillis());
                    break;
                case Recurring:
                    timer.scheduleAtFixedRate(task, interval.toMillis(), interval.toMillis());
                    break;
            }
        }

    }

    void stopTimer(String modelName, String id, String timerName) {
        SimulationScheduler scheduler = _simulationSchedulers.get(modelName);
        if (scheduler != null) {
            scheduler.stopTimer(modelName, id, timerName);
        } else {
            WorkbenchTimerTask task = _realTimeTimers.get(String.format("%s%s%s",modelName, id,timerName));
            task.cancel();
        }
    }

    void addAlertProvider(String modelName, String configuration) {
        _alertProviders.put(modelName, configuration);
    }

    void updateTwin(String model, String id, TwinProxy proxy) {
        ConcurrentHashMap<String,TwinProxy> modelInstances = _modelInstances.get(model);
        modelInstances.put(id, proxy);
        _modelInstances.put(model, modelInstances);
    }

    Date getTime(String model) {
        if(_simulationSchedulers.containsKey(model)) {
            SimulationScheduler scheduler = _simulationSchedulers.get(model);
            return new Date(scheduler.getCurrentTime());
        }
        return new Date(System.currentTimeMillis());
    }

    void setSimulationStatus(boolean status) {
        for(SimulationScheduler scheduler : _simulationSchedulers.values()) {
            scheduler.setStatus(status);
        }
    }

    List<String> runningModels() {
        return _modelNames;
    }

    HashMap<String, DigitalTwinBase> getTwinInstances(String model) {
        HashMap<String,DigitalTwinBase> ret = new HashMap<>();
        ConcurrentHashMap<String,TwinProxy> instances = _modelInstances.get(model);
        if(instances!= null) {
            for(Map.Entry<String,TwinProxy> entry : instances.entrySet()) {
                ret.put(entry.getKey(), entry.getValue().getInstance());
            }
        }
        return ret;
    }

    DigitalTwinBase getTwinInstance(String model, String id) {
        DigitalTwinBase ret = null;
        ConcurrentHashMap<String,TwinProxy> instances = _modelInstances.get(model);
        if(instances != null) {
            TwinProxy proxy = instances.get(id);
            if(proxy != null) {
                ret = proxy.getInstance();
            }
        }
        return ret;
    }

    TwinProxy getTwinProxy(String model, String id) {
        TwinProxy proxy = null;
        ConcurrentHashMap<String,TwinProxy> instances = _modelInstances.get(model);
        if(instances != null) {
            proxy = instances.get(id);
            return proxy;
        }
        return proxy;
    }

    boolean hasAlertProviderConfiguration(String model) {
        if(hasModel(model)) {
            return _alertProviders.containsKey(model);
        } else {
            return false;
        }

    }

    boolean hasModel(String modelName) {
        return _modelNames.contains(modelName);
    }

    SendingResult sendToSource(String source, String model, String id, byte[] msg) throws WorkbenchException {
        if(_modelNames.contains(source)) {
            run(source, id, null, msg);
            return SendingResult.Handled;
        } else {
            ConcurrentHashMap<String, List<String>> messagesByModel = _workbench.SOURCE_MESSAGES.getOrDefault(model, new ConcurrentHashMap<>());
            List<String> messages = messagesByModel.getOrDefault(id, new LinkedList<>());
            messages.add(new String(msg, StandardCharsets.UTF_8));
            messagesByModel.put(id, messages);
            _workbench.SOURCE_MESSAGES.put(model, messagesByModel);
            return SendingResult.Handled;
        }
    }

    SimulationStep runSimulationStep(SimulationStepArgs args) {
        SimulationStep status = null;
        for(Map.Entry<String,SimulationScheduler> entry : _simulationSchedulers.entrySet()) {
            SimulationStep next = entry.getValue().runSimulation(args);
            if(status == null) {
                status = next;
            } else {
                status.merge(next);
            }
        }
        return status;
    }

    HashMap<String, byte[]> getModelData(String model) {
        HashMap<String, byte[]> sharedData = _modelsSharedData.get(model);
        if(sharedData == null) sharedData = new HashMap<>();
        _modelsSharedData.put(model, sharedData);
        return sharedData;
    }

    HashMap<String, byte[]> getGlobalSharedData() {
        return _globalSharedData;
    }


    public void logMessage(String model, LogMessage message) {
        ConcurrentLinkedQueue<LogMessage> prev = _workbench.LOGGED_MESSAGES.get(model);
        if(prev == null) {
            synchronized (_workbench.LOGGED_MESSAGES) {
                prev = _workbench.LOGGED_MESSAGES.get(model);
                if(prev == null) {
                    prev = new ConcurrentLinkedQueue<>();
                    _workbench.LOGGED_MESSAGES.put(model, prev);
                }
            }
        }
        prev.add(message);
        _workbench.LOGGED_MESSAGES.put(model, prev);
    }

    public void recordAlertMessage(String model, AlertMessage message) {
        ConcurrentHashMap<String, ConcurrentLinkedQueue<AlertMessage>> perModelMessages = _workbench.ALERT_MESSAGES.getOrDefault(model, new ConcurrentHashMap<>());
        ConcurrentLinkedQueue<AlertMessage> perApMessages = perModelMessages.getOrDefault(_alertProviders.get(model), new ConcurrentLinkedQueue<>());
        perApMessages.add(message);
        perModelMessages.put(_alertProviders.get(model), perApMessages);
        _workbench.ALERT_MESSAGES.put(model, perModelMessages);
    }

    public void createInstance(String modelName, String id, DigitalTwinBase instance) {
        TwinProxy proxy = new TwinProxy(instance, new HashMap<>());
        ConcurrentHashMap<String,TwinProxy> modelInstances = _modelInstances.get(modelName);
        if(modelInstances == null) {
            modelInstances = new ConcurrentHashMap<>();
        }
        modelInstances.put(id, proxy);
        InitContext initContext = new WorkbenchInitContext<>(this, proxy, modelName, id);
        instance.init(initContext);
        SimulationScheduler scheduler = _simulationSchedulers.get(modelName);
        if(scheduler != null) {
            proxy.setProxyState(ProxyState.Active);
            scheduler.addInstance(proxy);
        }
        modelInstances.put(id, proxy);
        _modelInstances.put(modelName, modelInstances);
    }

    public void deleteSimulationInstance(String modelName, String id) {
        ConcurrentHashMap<String, TwinProxy> modelInstances = _modelInstances.get(modelName);
        TwinProxy proxy = modelInstances.remove(id);
        proxy.setProxyState(ProxyState.Removed);
        _modelInstances.put(modelName, modelInstances);
    }

    ProcessingResult run(String model, String id, String source, byte[] message) throws WorkbenchException {
        try {
            ConcurrentHashMap<String,TwinProxy> twinInstances = _modelInstances.get(model);
            if(twinInstances == null) {
                twinInstances = new ConcurrentHashMap<>();
            }
            TwinProxy proxy = twinInstances.get(id);
            DigitalTwinBase instance = null;
            if(proxy == null) {
                Class<? extends DigitalTwinBase> dtClazz = _digitalTwins.get(model);
                if(dtClazz == null) {
                    throw new WorkbenchException(String.format("DigitalTwin model \"%s\" does not exist on this workbench.", model));
                }
                instance = dtClazz.getConstructor().newInstance();
                InitContext initContext = new WorkbenchInitContext(this, proxy, model, id);
                instance.init(initContext);
                proxy = new TwinProxy(instance, new HashMap<>());
                SimulationScheduler scheduler = _simulationSchedulers.get(model);
                if(scheduler != null) {
                    proxy.setProxyState(ProxyState.Active);
                    scheduler.addInstance(proxy);
                }
            } else {
                instance = proxy.getInstance();
            }
            MessageProcessor mp = _messageProcessors.get(model);
            HashMap<String, byte[]> sharedData = _modelsSharedData.get(model);
            if(sharedData == null) sharedData = new HashMap<>();
            _modelsSharedData.put(model, sharedData);
            WorkbenchSimulationController simulationController = null;
            SimulationScheduler scheduler = _simulationSchedulers.get(model);
            if(scheduler != null) {
                simulationController = new WorkbenchSimulationController(this, scheduler);
            }
            WorkbenchProcessingContext context = new WorkbenchProcessingContext(_workbench._twinExecutionEngine, sharedData, _globalSharedData, simulationController);
            context.reset(model, id, source, proxy);
            if(simulationController != null) {
                simulationController.reset(model, id);
            }

            ProcessingResult res = mp.processMessage(context, instance, message);
            if(context.forceSave()) res = ProcessingResult.UpdateDigitalTwin;
            switch(res) {
                case UpdateDigitalTwin:
                    proxy.setInstance(instance);
                    twinInstances.put(id, proxy);
                    _modelInstances.put(model, twinInstances);
                    break;
                case NoUpdate:
                    break;
                default:
                    break;
            }
            return res;
        } catch (Exception e) {
            throw new WorkbenchException("Exception thrown while running message processor.", e);
        }
    }

    @Override
    public void close() throws IOException {
        if(_realTimeTimers != null && _realTimeTimers.size() > 0) {
            for(Map.Entry<String, WorkbenchTimerTask> entry : _realTimeTimers.entrySet()) {
                WorkbenchTimerTask task = _realTimeTimers.remove(entry.getKey());
                task.cancel();
            }
            _realTimeTimers = null;
        }
    }
}
