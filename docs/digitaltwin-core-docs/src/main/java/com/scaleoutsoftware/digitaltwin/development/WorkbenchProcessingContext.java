/*
 Copyright (c) 2025 by ScaleOut Software, Inc.

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

import com.scaleoutsoftware.digitaltwin.core.*;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.logging.Level;

class WorkbenchProcessingContext extends ProcessingContext {
    final TwinExecutionEngine   _twinExecutionEngine;
    String                      _model;
    String                      _id;
    String                      _source;
    DigitalTwinBase             _twinInstance;
    SimulationController        _controller;
    HashMap<String, byte[]>     _modelData;
    HashMap<String, byte[]>     _globalData;
    boolean                     _forceSave;

    WorkbenchProcessingContext(TwinExecutionEngine twinExecutionEngine, HashMap<String, byte[]> modelSharedData, HashMap<String, byte[]> globalSharedData, SimulationController simulationController) {
        _twinExecutionEngine = twinExecutionEngine;
        _controller          = simulationController;
        _modelData           = modelSharedData;
        _globalData          = globalSharedData;
    }

    WorkbenchProcessingContext(TwinExecutionEngine twinExecutionEngine, SimulationController controller) {
        _twinExecutionEngine    = twinExecutionEngine;
        _controller             = controller;
    }

    void reset(String model, String id, String source, DigitalTwinBase instance) {
        _model          = model;
        _id             = id;
        _twinInstance   = instance;
        _forceSave      = false;
        _source         = source;
        _modelData      = _twinExecutionEngine.getModelData(model);
        _globalData     = _twinExecutionEngine.getGlobalSharedData();
    }

    void reset(String model, String id, String source) {
        _model          = model;
        _id             = id;
        _forceSave      = false;
        _source         = source;
        _modelData      = _twinExecutionEngine.getModelData(model);
        _globalData     = _twinExecutionEngine.getGlobalSharedData();
    }

    void resetInstance(DigitalTwinBase instance) {
        _twinInstance = instance;
    }

    boolean forceSave() {
        return _forceSave;
    }

    @Override
    public SendingResult sendToDataSource(byte[] bytes) {
        try {
            return _twinExecutionEngine.sendToSource(_source, _model, _id, new String(bytes, StandardCharsets.UTF_8));
        } catch (WorkbenchException e) {
            return SendingResult.NotHandled;
        }
    }

    @Override
    public SendingResult sendToDataSource(Object jsonSerializableMessage) {
        try {
            List<Object> jsonSerializableMessages = new LinkedList<>();
            jsonSerializableMessages.add(jsonSerializableMessage);
            return _twinExecutionEngine.sendToSource(_source, _model, _id, jsonSerializableMessages);
        } catch (WorkbenchException e) {
            return SendingResult.NotHandled;
        }
    }

    @Override
    public SendingResult sendToDataSource(List<Object> list) {
        try {
            return _twinExecutionEngine.sendToSource(_source, _model, _id, list);
        } catch (WorkbenchException e) {
            return SendingResult.NotHandled;
        }
    }

    @Override
    public SendingResult sendToDigitalTwin(String model, String id, byte[] bytes) {
        List<byte[]> msgs = new LinkedList<>();
        msgs.add(bytes);
        return sendToDigitalTwin(model, id, msgs);
    }

    @Override
    public SendingResult sendToDigitalTwin(String model, String id, Object jsonSerializableMessage) {
        try {
            if(_twinExecutionEngine.getTwinInstance(model, id) != null) {
                List<Object> jsonSerializableMessages;
                if(jsonSerializableMessage instanceof List) {
                    jsonSerializableMessages = (List<Object>)jsonSerializableMessage;
                } else {
                    jsonSerializableMessages = new LinkedList<>();
                    jsonSerializableMessages.add(jsonSerializableMessage);
                }

                return _twinExecutionEngine.run(model, id, null, jsonSerializableMessages) != null ? SendingResult.Handled : SendingResult.NotHandled;
            } else {
                return SendingResult.NotHandled;
            }
        } catch (WorkbenchException e) {
            return SendingResult.NotHandled;
        }
    }

    @Override
    public SendingResult sendToDigitalTwin(String model, String id, String msg) {
        return sendToDigitalTwin(model, id, msg.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public SendingResult sendToDigitalTwin(String model, String id, List<byte[]> list) {
        if( (model == null || model.isEmpty()) ||
            (id == null || id.isEmpty()) ||
            (list == null || list.isEmpty())) {
            return SendingResult.NotHandled;
        }
        Gson gson = new Gson();
        List<String> msgs = new LinkedList<>();
        for(byte[] serialMsg : list) {
            msgs.add(new String(serialMsg, StandardCharsets.UTF_8));
        }
        String json = gson.toJson(msgs);
        try {
            if(_twinExecutionEngine.getTwinInstance(model, id) != null) {
                return _twinExecutionEngine.run(model, id, null, json) != null ? SendingResult.Handled : SendingResult.NotHandled;
            } else {
                return SendingResult.NotHandled;
            }

        } catch (WorkbenchException e) {
            return SendingResult.NotHandled;
        }
    }

    @Override
    public SendingResult sendAlert(String alertProviderName, AlertMessage alertMessage) {
        if(alertProviderName.isBlank() || alertProviderName.isEmpty() || alertMessage == null) return SendingResult.NotHandled;
        else if (!_twinExecutionEngine.hasAlertProviderConfiguration(_model, alertProviderName)) return SendingResult.NotHandled;
        else {
            if(_twinExecutionEngine.hasAlertProviderConfiguration(_model, alertProviderName)) {
                _twinExecutionEngine.recordAlertMessage(_model, alertProviderName, alertMessage);
            }
            return SendingResult.Handled;
        }
    }

    @Override
    public PersistenceProvider getPersistenceProvider() {
        return null;
    }

    @Override
    public String getDataSourceId() {
        return _id;
    }

    @Override
    public String getDigitalTwinModel() {
        return _model;
    }

    @Override
    public void logMessage(Level level, String msg) {
        _twinExecutionEngine.logMessage(_model, new LogMessage(level, msg));
    }

    @Override
    public <T extends DigitalTwinBase> TimerActionResult startTimer(String timerName, Duration interval, TimerType timerType, TimerHandler<T> timerHandler) {
        TimerActionResult ret = WorkbenchTimerService.startTimer(_twinExecutionEngine, (T)_twinInstance, _model, _id, timerName, interval, timerType, timerHandler);
        if(ret != TimerActionResult.Success) {
            _forceSave = false;
        } else {
            _forceSave = true;
        }
        return ret;
    }

    @Override
    public TimerActionResult stopTimer(String timerName) {
        TimerActionResult ret = WorkbenchTimerService.stopTimer(_twinExecutionEngine, _twinInstance, _model, _id, timerName);
        if(ret != TimerActionResult.Success) {
            _forceSave = false;
        } else {
            _forceSave = true;
        }
        return ret;
    }

    @Override
    public Date getCurrentTime() {
        return _twinExecutionEngine.getTime(_model);
    }

    @Override
    public SimulationController getSimulationController() {
        return _controller;
    }

    @Override
    public SharedData getSharedModelData() {
        return new WorkbenchSharedData(_modelData);
    }

    @Override
    public SharedData getSharedGlobalData() {
        return new WorkbenchSharedData(_globalData);
    }
}
