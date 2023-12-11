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

import com.scaleoutsoftware.digitaltwin.core.DigitalTwinBase;
import com.scaleoutsoftware.digitaltwin.core.ProcessingContext;
import com.scaleoutsoftware.digitaltwin.core.TimerHandler;

import java.util.Date;

class SimulationEventTimerImpl extends SimulationEvent {
    TwinProxy _proxy;
    TimerHandler _handler;
    String _timerName;

    SimulationEventTimerImpl(String model, String id, long priority, String name, TwinProxy proxy, TimerHandler handler) {
        super(model, id,priority);
        _timerName  = name;
        _proxy      = proxy;
        _handler    = handler;

    }

    @Override
    SimulationEventResult processSimulationEvent(ProcessingContext context, Date currentTime) {
        DigitalTwinBase base = _proxy.getInstance();
        _handler.onTimedMessage(_timerName, base, context);
        _proxy.setInstance(base);
        return new SimulationEventResult(){};
    }

    @Override
    ProxyState getProxyState() {
        return _proxy.getProxyState();
    }

    @Override
    void setProxyState(ProxyState newState) {
        _proxy.setProxyState(newState);
    }

    @Override
    void handleResetNextSimulationTime() {
    }
}
