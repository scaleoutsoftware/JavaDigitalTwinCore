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
package com.scaleoutsoftware.digitaltwin.core;

import java.io.Serializable;
import java.util.Date;

/**
 * Processes simulation events for a digital twin.
 * @param <T> the type of the digital twin.
 */
public abstract class SimulationProcessor<T extends DigitalTwinBase> implements Serializable {

    /**
     * Default constructor.
     */
    public SimulationProcessor() {}

    /**
     * Processes simulation events for a real-time digital twin.
     * @param context the processing context.
     * @param instance the digital twin instance.
     * @param epoch the current time of the simulation.
     * @return {@link ProcessingResult#UpdateDigitalTwin} to update the digital twin, or
     * {@link ProcessingResult#NoUpdate} to ignore the changes.
     */
    public abstract ProcessingResult processModel(ProcessingContext context, T instance, Date epoch);

    /**
     * <p>
     *     Optional method that is called per-instance when a simulation is started. Default behavior is a no-op.
     * </p>
     *
     * <p>
     *     onInitSimulation can be used when internal digital twin starting state is set outside the context of a digital twins init method and may be changed
     *     between simulation runs.
     *     <p>
     *         <ul>
     *             <li>Set variables in global or shared data.</li>
     *             <li>Run a simulation.</li>
     *             <li>onInitSimulation is called (per-instance) and digital twin instances set internal state based on the values in shared data.</li>
     *             <li>Complete simulation and evaluate the result.</li>
     *         </ul>
     *
     *     </p>
     * </p>
     * @param context The simulation init context.
     * @param instance The digital twin instance.
     * @param epoch the simulation start time.
     * @return {@link ProcessingResult#UpdateDigitalTwin} or {@link ProcessingResult#NoUpdate}. Default behavior: {@link ProcessingResult#NoUpdate}.
     */
    public ProcessingResult onInitSimulation(InitSimulationContext context, T instance, Date epoch) {
        return ProcessingResult.NoUpdate;
    }
}
