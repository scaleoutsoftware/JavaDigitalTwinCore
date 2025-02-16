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

import com.scaleoutsoftware.digitaltwin.core.ProcessingContext;

import java.util.Date;

abstract class SimulationEvent implements Comparable<SimulationEvent> {
    protected String    _model;
    protected String    _id;
    protected long      _priority;
    protected long      _nextSimulationTime;

    SimulationEvent(String model, String id, long priority) {
        _model      = model;
        _id         = id;
        _priority   = priority;
    }

    abstract SimulationEventResult processSimulationEvent(ProcessingContext context, Date currentTime) throws WorkbenchException;

    abstract ProxyState getProxyState();

    abstract void setProxyState(ProxyState newState);

    abstract void handleResetNextSimulationTime();

    abstract void simulationInit(Date simulationStartTime);

    long getPriority() {
        return _priority;
    }

    void setPriority(long priority) {
        _priority = priority;
    }

    String getId() {
        return _id;
    }

    String getModel() {return _model;}

    void setNextSimulationTime(long nextSimulationTime) {
        _nextSimulationTime = nextSimulationTime;
        handleResetNextSimulationTime();
    }



    @Override
    public int compareTo(SimulationEvent other) {
        return Long.compare(this._priority, other._priority);
    }
}
