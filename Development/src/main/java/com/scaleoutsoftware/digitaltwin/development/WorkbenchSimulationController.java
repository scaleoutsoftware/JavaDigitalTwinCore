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

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

class WorkbenchSimulationController implements SimulationController {
    TwinExecutionEngine     _engine;
    SimulationScheduler     _scheduler;
    private long            _requestedDelay;
    private boolean         _delayRequested;
    private boolean         _deleted;
    private boolean         _enqueue;
    private String          _modelName;
    private String          _id;
    private SimulationStatus _simulationStatus = SimulationStatus.Running;

    public WorkbenchSimulationController(TwinExecutionEngine engine, SimulationScheduler scheduler) {
        _engine     = engine;
        _scheduler  = scheduler;
    }

    @Override
    public Duration getSimulationTimeIncrement() {
        return null;
    }

    @Override
    public Date getSimulationStartTime() {
        return _scheduler.getSimulationStartTime();
    }

    @Override
    public SendingResult delay(Duration duration) {
        _requestedDelay = duration.toMillis();
        _delayRequested = true;
        return SendingResult.Handled;
    }

    @Override
    public SendingResult delayIndefinitely() {
        _requestedDelay = 0x0000e677d21fdbffL;
        _delayRequested = true;
        return SendingResult.Handled;
    }

    @Override
    public SendingResult emitTelemetry(String modelName, byte[] bytes) {
        try {
            _engine.run(modelName, _id, _modelName, String.format("[%s]", new String(bytes, StandardCharsets.UTF_8)));
            return SendingResult.Handled;
        } catch (WorkbenchException e) {
            e.printStackTrace();
            return SendingResult.NotHandled;
        }
    }

    @Override
    public SendingResult emitTelemetry(String modelName, Object jsonSerializableMessage) {
        try {
            if(_engine.hasModel(modelName)) {
                List<Object> jsonSerializableMessages = new LinkedList<>();
                jsonSerializableMessages.add(jsonSerializableMessage);
                _engine.run(modelName, _id, _modelName, jsonSerializableMessages);
                return SendingResult.Handled;
            } else {
                return SendingResult.NotHandled;
            }

        } catch (WorkbenchException e) {
            e.printStackTrace();
            return SendingResult.NotHandled;
        }
    }

    @Override
    public <T extends DigitalTwinBase> SendingResult createInstance(String modelName, String id, T instance) {
        try {
            _engine.createInstance(modelName, id, instance);
            return SendingResult.Handled;
        } catch (Exception e) {
            e.printStackTrace();
            return SendingResult.NotHandled;
        }
    }

    @Override
    public SendingResult createInstanceFromPersistenceStore(String modelName, String id) {
        try {
            throw new NoSuchMethodException("Not available on the workbench.");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends DigitalTwinBase> SendingResult createInstanceFromPersistenceStore(String modelName, String id, T defaultInstance) {
        try {
            throw new NoSuchMethodException("Not available on the workbench.");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SendingResult deleteInstance(String modelName, String id) {
        _engine.deleteSimulationInstance(modelName, id);
        return SendingResult.Handled;
    }

    @Override
    public SendingResult deleteThisInstance() {
        _engine.deleteSimulationInstance(_modelName, _id);
        _deleted = true;
        return SendingResult.Handled;
    }

    @Override
    public void runThisInstance() {
        try {
            _scheduler.runThisInstance(_modelName, _id);
            _enqueue = false;
        } catch (WorkbenchException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public SimulationStatus stopSimulation() {
        _simulationStatus = SimulationStatus.InstanceRequestedStop;
        return _simulationStatus;
    }

    public boolean delayRequested() {
        return _delayRequested;
    }

    public void reset(String modelName, String id) {
        _modelName      = modelName;
        _id             = id;
        _requestedDelay = Long.MIN_VALUE;
        _delayRequested = false;
        _deleted        = false;
        _enqueue        = true;
    }

    public long getRequestedDelay() {
        return _requestedDelay;
    }

    public boolean deleted() {
        return _deleted;
    }

    public boolean enqueue() {
        return _enqueue;
    }

    public SimulationStatus getSimulationStatus() {
        return _simulationStatus;
    }
}
