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

import com.scaleoutsoftware.digitaltwin.core.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;

class SimulationEventTwinImpl extends SimulationEvent {
    SimulationProcessor _processor;
    TwinProxy           _proxy;
    SharedData          _modelSharedData;
    SharedData          _globalSharedData;

    SimulationEventTwinImpl(long priority, TwinProxy proxy, SimulationProcessor processor, SharedData modelSharedData, SharedData globalSharedData) {
        super(proxy.getInstance().Model, proxy.getInstance().Id, priority);
        _proxy              = proxy;
        _processor          = processor;
        _modelSharedData    = modelSharedData;
        _globalSharedData   = globalSharedData;
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

    @Override
    void simulationInit(Date simulationStartTime) {
        InitSimulationContext context = new WorkbenchInitSimulationContext(_globalSharedData, _modelSharedData);
        synchronized (_proxy) {
            DigitalTwinBase base = _proxy.getInstance();
            _processor.onInitSimulation(context, base, simulationStartTime);
            _proxy.setInstance(base);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimulationEventTwinImpl that = (SimulationEventTwinImpl) o;
        return this._proxy.getInstance().getId().compareTo(that._id) == 0 && this._proxy.getInstance().getModel().compareTo(that._model) == 0;
    }

    @Override
    public int hashCode() {
        return (int)Constants.getHash(_id.getBytes(StandardCharsets.UTF_8));
    }
}
