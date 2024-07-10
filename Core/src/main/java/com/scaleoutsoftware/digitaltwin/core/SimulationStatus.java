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
package com.scaleoutsoftware.digitaltwin.core;

/**
 * The status of a simulation.
 */
public enum SimulationStatus {
    /**
     * The simulation is running.
     */
    Running,
    /**
     * The simulation status is not set.
     */
    NotSet,
    /**
     * The user requested a stop.
     */
    UserRequested,
    /**
     * The simulation end time has been reached.
     */
    EndTimeReached,
    /**
     * There is no remaining work for the simulation.
     */
    NoRemainingWork,
    /**
     * A digital twin instance has requested the simulation to stop by calling {@link SimulationController#stopSimulation()}
     */
    InstanceRequestedStop,
    /**
     * There was a runtime-change of simulation configuration.
     */
    UnexpectedChangeInConfiguration;
}
