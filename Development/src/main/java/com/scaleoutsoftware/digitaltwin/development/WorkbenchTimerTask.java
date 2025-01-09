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
import java.util.TimerTask;

class WorkbenchTimerTask extends TimerTask {
    TwinExecutionEngine     _engine;
    String                  _modelName;
    String                  _id;
    String                  _timerName;
    TwinProxy               _proxy;
    TimerType               _type;
    Duration                _interval;
    TimerHandler            _handler;

    WorkbenchTimerTask(TwinExecutionEngine engine, String modelName, String id, String timerName, TwinProxy proxy, TimerType type, Duration interval, TimerHandler handler) {
        _engine     = engine;
        _modelName  = modelName;
        _id         = id;
        _timerName  = timerName;
        _proxy      = proxy;
        _type       = type;
        _interval   = interval;
        _handler    = handler;
    }

    @Override
    public void run() {
        DigitalTwinBase instance = _proxy.getInstance();
        WorkbenchProcessingContext context = new WorkbenchProcessingContext(_engine, null);
        context.reset(_modelName, _id, null, instance);
        ProcessingResult result;
        synchronized (instance) {
             result = _handler.onTimedMessage(_timerName, instance, context);
        }
        switch (result) {
            case UpdateDigitalTwin:
                _engine.updateTwin(_modelName, _id, _proxy);
                break;
            case NoUpdate:
                break;
            default:
                throw new RuntimeException(new WorkbenchException("Unknown return type from timer handler."));
        }
    }
}
