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

import com.scaleoutsoftware.digitaltwin.abstractions.DigitalTwinBase;
import com.scaleoutsoftware.digitaltwin.abstractions.TimerMetadata;

import java.util.HashMap;

class TwinProxy {
    private DigitalTwinBase                                             _instance;
    private ProxyState                                                  _state;
    private long                                                        _nextSimulationTime;
    private HashMap<String, TimerMetadata<? extends DigitalTwinBase>>   _timerHandlers;
    public TwinProxy(DigitalTwinBase instance, HashMap<String, TimerMetadata<? extends DigitalTwinBase>> timerHandlers) {
        _instance           = instance;
        _timerHandlers      = timerHandlers;
        _state              = ProxyState.Unspecified;
        _nextSimulationTime = 0L;
    }

    void setProxyState(ProxyState state) {
        _state = state;
    }

    ProxyState getProxyState() {
        return _state;
    }

    DigitalTwinBase getInstance() {
        return _instance;
    }

    public void setInstance(DigitalTwinBase instance) {
        _instance = instance;
    }

    HashMap<String, TimerMetadata<? extends DigitalTwinBase>> getTimerHandlers() {
        return _timerHandlers;
    }

    @Override
    public String toString() {
        return "TwinProxy{" +
                "_instance=" + _instance +
                ", _state=" + _state +
                '}';
    }
}

