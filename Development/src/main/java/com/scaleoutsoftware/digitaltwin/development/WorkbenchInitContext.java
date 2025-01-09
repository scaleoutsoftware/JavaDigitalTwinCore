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

import com.scaleoutsoftware.digitaltwin.core.*;

import java.time.Duration;

class WorkbenchInitContext extends InitContext {
    TwinExecutionEngine     _twinExecutionEngine;
    DigitalTwinBase         _instance;
    String                  _model;
    String                  _id;

    WorkbenchInitContext(TwinExecutionEngine twinExecutionEngine, DigitalTwinBase instance, String model, String id) {
        _twinExecutionEngine    = twinExecutionEngine;
        _instance               = instance;
        _model                  = model;
        _id                     = id;
    }

    @Override
    public <T extends DigitalTwinBase> TimerActionResult startTimer(String timerName, Duration duration, TimerType timerType, TimerHandler<T> timerHandler) {
        return WorkbenchTimerService.startTimer(_twinExecutionEngine, (T)_instance, _model, _id, timerName, duration, timerType, timerHandler);
    }

    @Override
    public SharedData getSharedModelData() {
        return new WorkbenchSharedData(_twinExecutionEngine.getModelData(_model));
    }

    @Override
    public SharedData getSharedGlobalData() {
        return new WorkbenchSharedData(_twinExecutionEngine.getGlobalSharedData());
    }

    @Override
    public String getId() {
        return _id;
    }

    @Override
    public String getModel() {
        return _model;
    }
}
