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

/**
 * The ModelSimulation interface is used to interact with the current DigitalTwin simulation.
 */
public interface ModelSimulation {

    /**
     * Retrieves the current simulation time increment.
     * @return the simulation time increment.
     */
    Duration getSimulationTimeIncrement();

    /**
     * Delay simulation processing for this DigitalTwin instance for a duration of time.
     *
     * Simulation processing will be delayed for the duration specified relative to the current simulation time.
     * @param duration the duration to delay.
     * @return {@link SendingResult#Handled} if the delay was processed or {@link SendingResult#NotHandled}
     * if the delay was not processed.
     */
    SendingResult delay(Duration duration);

    /**
     * Asynchronously send a JSON serialized message to a DigitalTwin instance that will be processed by the DigitalTwin
     * models {@link MessageProcessor#processMessages(ProcessingContext, DigitalTwinBase, Iterable)} method.
     * @param modelName the model to send the messages too.
     * @param telemetryMessage a blob representing a JSON serialized messages.
     * @return {@link SendingResult#Handled} if the messages were processed, {@link SendingResult#Enqueued} if
     * the messages are in process of being handled, or {@link SendingResult#NotHandled} if the delay was not processed.
     */
    SendingResult emitTelemetry(String modelName, byte[] telemetryMessage);

    /**
     * Create a new instance for simulation processing.
     * @param modelName the model name.
     * @param instanceId the instance id.
     * @return {@link SendingResult#Handled} if the instance was created, {@link SendingResult#Enqueued} if the instance
     * is in process of being created, or {@link SendingResult#NotHandled} if the instance could not be created.
     */
    SendingResult createInstance(String modelName, String instanceId);

    /**
     * Delete a digital twin instance from simulation processing.
     * @param modelName the model name.
     * @param instanceId the instance id.
     * @return {@link SendingResult#Handled} if the instance was deleted, {@link SendingResult#Enqueued} if the instance
     * is in process of being deleted, or {@link SendingResult#NotHandled} if the instance could not be deleted.
     */
    SendingResult deleteInstance(String modelName, String instanceId);

    /**
     * Deletes this digital twin instance.
     * @return this local request will always return {@link SendingResult#Handled}.
     */
    SendingResult deleteThisInstance();
}
