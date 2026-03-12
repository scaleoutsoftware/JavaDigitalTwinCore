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

import com.scaleoutsoftware.digitaltwin.abstractions.*;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

class WorkbenchProcessingContext<T extends DigitalTwinBase<T>> extends ProcessingContext<T> {
    final TwinExecutionEngine   _twinExecutionEngine;
    String                      _model;
    String                      _id;
    String                      _source;
    TwinProxy                   _proxy;
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

    void reset(String model, String id, String source, TwinProxy proxy) {
        _model          = model;
        _id             = id;
        _proxy          = proxy;
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

    void resetProxy(TwinProxy proxy) {
        _proxy = proxy;
    }

    boolean forceSave() {
        return _forceSave;
    }

    @Override
    public CompletableFuture<SendingResult> sendToDataSource(byte[] message) {
        try {
            return CompletableFuture.completedFuture(_twinExecutionEngine.sendToSource(_source, _model, _id, message));
        } catch (WorkbenchException e) {
            return CompletableFuture.completedFuture(SendingResult.NotHandled);
        }
    }

    @Override
    public CompletableFuture<SendingResult> sendToDigitalTwin(String model, String id, byte[] message) {
        try {
            return CompletableFuture.completedFuture(_twinExecutionEngine.run(model, id,null, message) != null ? SendingResult.Handled : SendingResult.NotHandled);
        } catch (WorkbenchException e) {
            return CompletableFuture.completedFuture(SendingResult.NotHandled);
        }
    }

    @Override
    public CompletableFuture<SendingResult> sendAlert(String alertProviderName, AlertMessage alertMessage) {
        if(alertProviderName.isEmpty() || alertMessage == null) return CompletableFuture.completedFuture(SendingResult.NotHandled);
        else if (!_twinExecutionEngine.hasAlertProviderConfiguration(_model, alertProviderName)) return CompletableFuture.completedFuture(SendingResult.NotHandled);
        else {
            if(_twinExecutionEngine.hasAlertProviderConfiguration(_model, alertProviderName)) {
                _twinExecutionEngine.recordAlertMessage(_model, alertProviderName, alertMessage);
                return CompletableFuture.completedFuture(SendingResult.Handled);
            } else {
                return CompletableFuture.completedFuture(SendingResult.NotHandled);
            }
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
    public CompletableFuture<Void> logMessage(Level level, String msg) {
        _twinExecutionEngine.logMessage(_model, new LogMessage(level, msg));
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public TimerActionResult startTimer(String timerName, Duration interval, TimerType timerType, TimerHandler<T> timerHandler) {
        TimerActionResult ret = WorkbenchTimerService.startTimer(_twinExecutionEngine, _proxy, _model, _id, timerName, interval, timerType, timerHandler);
        if(ret != TimerActionResult.Success) {
            _forceSave = false;
        } else {
            _forceSave = true;
        }
        return ret;
    }

    @Override
    public TimerActionResult stopTimer(String timerName) {
        TimerActionResult ret = WorkbenchTimerService.stopTimer(_twinExecutionEngine, _proxy, _model, _id, timerName);
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
