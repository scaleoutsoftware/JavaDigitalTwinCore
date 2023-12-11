/*
 Copyright (c) 2018 by ScaleOut Software, Inc.

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

import java.util.Date;
import java.util.HashMap;

/**
 * A real-time digital twin of a data source. The implementation of the real-time DigitalTwin should have a parameterless constructor for
 * basic initialization.
 */
public abstract class DigitalTwinBase {

    /* capitalized to match .NET serialization */
    /**
     * The identifier for this twin instance
     */
    public String Id = "";

    /**
     * The model this twin instance belongs to.
     */
    public String Model = "";

    /**
     * The timer handlers for this twin instance.
     */
    public HashMap<String,TimerMetadata> TimerHandlers = new HashMap<>();

    /**
     * Note: Simulation only. The next time in milliseconds that this Digital Twin instance will be passed to {@link SimulationProcessor#processModel(ProcessingContext, DigitalTwinBase, Date)}.
     */
    public long NextSimulationTime = 0L;

    /**
     * Retrieve the next simulation time in milliseconds.
     * @return the next simulation time in milliseconds.
     */
    public long getNextSimulationTimeMs() {
        return NextSimulationTime;
    }

    /**
     * Set the next simulation time in milliseconds.
     * @param nextSimulationTime set the next simulation time.
     */
    public void setNextSimulationTime(long nextSimulationTime) {
        NextSimulationTime = nextSimulationTime;
    }

    /**
     * The identifier of this DigitalTwin.
     * @return the identifier of this digital twin
     */
    public String getId() {
        return Id;
    }

    /**
     * The model for this DigitalTwin.
     * @return the model for this DigitalTwin
     */
    public String getModel() {
        return Model;
    }

    /**
     * Initialization method to set the identifier and model for a DigitalTwin instance. Optionally use the
     * {@link InitContext} to start a timer.
     * @param context the initialization context.
     * @throws IllegalStateException if init is called after initialization.
     */
    public void init(InitContext context) throws IllegalStateException {
        this.Id     = context.getId();
        this.Model  = context.getModel();
    }
}
