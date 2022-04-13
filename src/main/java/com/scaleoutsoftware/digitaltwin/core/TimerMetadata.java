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

import java.time.Duration;

public class TimerMetadata<T extends DigitalTwinBase> {
    private TimerHandler<T>     _timerHandler;
    private TimerType           _timerType;
    private Duration            _timerInterval;
    private int                 _timerId;

    public TimerMetadata(TimerHandler<T> handler, TimerType timerType, Duration timerInterval, int timerIdx) {
        _timerHandler   = handler;
        _timerType      = timerType;
        _timerInterval  = timerInterval;
        _timerId        = timerIdx;
    }

    public TimerHandler<T> getTimerHandler() {
        return _timerHandler;
    }

    public TimerType getTimerType() {
        return _timerType;
    }

    public Duration getTimerInterval() {
        return _timerInterval;
    }

    public int getTimerId() {
        return _timerId;
    }
}
