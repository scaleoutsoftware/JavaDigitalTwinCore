/*
 Copyright (c) 2022 by ScaleOut Software, Inc.

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
package com.scaleoutsoftware.digitaltwin.core;

public class DigitalTwinTimerMessage {

    private String      _modelName;
    private String      _twinId;
    private int         _timerId;
    private String      _timerName;
    private TimerType   _timerType;

    public DigitalTwinTimerMessage(String modelName, String twinId, int timerId, String timerName, TimerType timerType) {
        _modelName  = modelName;
        _twinId     = twinId;
        _timerId    = timerId;
        _timerName  = timerName;
        _timerType  = timerType;
    }

    public String getModelName() {
        return _modelName;
    }

    public String getTwinId() {
        return _twinId;
    }

    public int getTimerId() {
        return _timerId;
    }

    public String getTimerName() {
        return _timerName;
    }

    public TimerType getTimerType() {
        return _timerType;
    }
}
