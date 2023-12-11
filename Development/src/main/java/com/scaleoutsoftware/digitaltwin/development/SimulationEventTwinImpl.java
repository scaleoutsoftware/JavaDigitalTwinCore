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
import com.scaleoutsoftware.digitaltwin.core.SimulationProcessor;

import java.util.Date;

class SimulationEventTwinImpl extends SimulationEvent {
    SimulationProcessor _processor;
    TwinProxy           _proxy;

    SimulationEventTwinImpl(long priority, TwinProxy proxy, SimulationProcessor processor) {
        super(proxy.getInstance().Model, proxy.getInstance().Id, priority);
        _proxy      = proxy;
        _processor  = processor;
    }

    @Override
    SimulationEventResult processSimulationEvent(ProcessingContext context, Date currentTime) throws WorkbenchException {
        try {
            DigitalTwinBase base = _proxy.getInstance();
            synchronized (_proxy) {
                WorkbenchProcessingContext wpc = (WorkbenchProcessingContext)context;
                wpc.resetInstance(base);
                _processor.processModel(wpc, base, currentTime);
                _proxy.setInstance(base);
            }
        } catch (Exception e) {
            throw new WorkbenchException(e);
        }
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
        DigitalTwinBase base = _proxy.getInstance();
        base.NextSimulationTime = _nextSimulationTime;
        _proxy.setInstance(base);
    }
}
