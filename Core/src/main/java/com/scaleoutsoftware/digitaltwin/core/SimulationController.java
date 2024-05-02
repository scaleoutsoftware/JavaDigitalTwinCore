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
import java.util.Date;

/**
 * The SimulationController interface is used to interact with the  running DigitalTwin simulation.
 */
public interface SimulationController {

    /**
     * <p>
     * Retrieves the current simulation time increment.
     * </p>
     * @return the simulation time increment.
     */
    Duration getSimulationTimeIncrement();

    /**
     * <p>
     * Delay simulation processing for this DigitalTwin instance for a duration of time.
     * </p>
     *
     * <p>
     * Simulation processing will be delayed for the duration specified relative to the current simulation time.
     * </p>
     *
     * <p>
     * Examples:
     * </p>
     *
     * <p>
     * at a current simulation time of 10, an interval of 20, and a delay of 40 -- the instance would
     * skip one cycle of processing and be processed at simulation time 50.
     * </p>
     *
     * <p>
     * at a current simulation time of 10, an interval of 20, and a delay of 30 -- the instance would
     * skip one cycle of processing and be processed at simulation time 50.
     * </p>
     *
     * <p>
     * at a current simulation time of 10, an interval of 20, and a delay of 50 -- the instance would
     * skip two cycles of processing and be processed at simulation time 70.
     * </p>
     *
     * @param duration the duration to delay.
     * @return {@link SendingResult#Handled} if the delay was processed or {@link SendingResult#NotHandled}
     * if the delay was not processed.
     */
    SendingResult delay(Duration duration);

    /**
     * <p>
     * Delay simulation processing for this DigitalTwin instance, indefinitely.
     * </p>
     *
     * <p>
     * Simulation processing will be delayed until this instance is run with {@link SimulationController#runThisTwin()}.
     * </p>
     *
     * @return {@link SendingResult#Handled} if the delay was processed or {@link SendingResult#NotHandled}
     * if the delay was not processed.
     */
    SendingResult delayIndefinitely();

    /**
     * <p>
     * Asynchronously send a JSON serialized message to a DigitalTwin instance that will be processed by the DigitalTwin
     * models {@link MessageProcessor#processMessages(ProcessingContext, DigitalTwinBase, Iterable)} method.
     * </p>
     * @param modelName the model to send the messages too.
     * @param telemetryMessage a blob representing a JSON serialized messages.
     * @return {@link SendingResult#Handled} if the messages were processed, {@link SendingResult#Enqueued} if
     * the messages are in process of being handled, or {@link SendingResult#NotHandled} if the delay was not processed.
     */
    SendingResult emitTelemetry(String modelName, byte[] telemetryMessage);

    /**
     * <p>
     * Asynchronously send a JSON serializable message to a DigitalTwin instance that will be processed by the DigitalTwin
     * models {@link MessageProcessor#processMessages(ProcessingContext, DigitalTwinBase, Iterable)} method.
     * </p>
     * @param modelName the model to send the messages too.
     * @param jsonSerializableMessage an object message that is JSON serializable.
     * @return {@link SendingResult#Handled} if the messages were processed, {@link SendingResult#Enqueued} if
     * the messages are in process of being handled, or {@link SendingResult#NotHandled} if the delay was not processed.
     */
    SendingResult emitTelemetry(String modelName, Object jsonSerializableMessage);

    /**
     * Create a new digital twin instance for simulation processing.
     * @param modelName the model name.
     * @param instanceId the instance id.
     * @param base the instance to create.
     * @return {@link SendingResult#Handled} if the instance was created, {@link SendingResult#Enqueued} if the instance
     * is in process of being created, or {@link SendingResult#NotHandled} if the instance could not be created.
     * @param <T> the type of the digital twin to create.
     */
    <T extends DigitalTwinBase> SendingResult  createInstance(String modelName, String instanceId, T base);

    /**
     * Create a new digital twin instance for simulation processing from a persistence store.
     *
     * The twin instance will be loaded via model name and id from a persistence store.
     *
     * If no instance can be found, then an exception will be thrown and no instance will be created.
     *
     * @param model The model name.
     * @param id the instance id.
     * @return {@link SendingResult#Handled} if the instance was created, {@link SendingResult#Enqueued} if the instance
     * is in process of being created, or {@link SendingResult#NotHandled} if the instance could not be created.
     */
    SendingResult createInstanceFromPersistenceStore(String model, String id);

    /**
     * The twin instance will be loaded via model name and id from a persistence store.
     *
     * The twin instance will be loaded via model name and id from a persistence store.
     *
     * If no instance can be found, then the default parameter instance will be used.
     *
     * @param model the model name.
     * @param id the instance id.
     * @param def the default instance to create.
     * @return {@link SendingResult#Handled} if the instance was created, {@link SendingResult#Enqueued} if the instance
     *      * is in process of being created, or {@link SendingResult#NotHandled} if the instance could not be created.
     * @param <T> the type of the digital twin to create.
     */
    <T extends DigitalTwinBase> SendingResult createInstanceFromPersistenceStore(String model, String id, T def);

    /**
     * Delete and remove a digital twin instance from simulation processing.
     * @param modelName the model name.
     * @param instanceId the instance id.
     * @return {@link SendingResult#Handled} if the instance was deleted, {@link SendingResult#Enqueued} if the instance
     * is in process of being deleted, or {@link SendingResult#NotHandled} if the instance could not be deleted.
     */
    SendingResult deleteInstance(String modelName, String instanceId);

    /**
     * Delete and remove this digital twin instance from simulation processing.
     * @return this local request will always return {@link SendingResult#Handled}.
     */
    SendingResult deleteThisInstance();

    /**
     * Run this instance during this simulation step. The instance will be run using the models {@link SimulationProcessor#processModel(ProcessingContext, DigitalTwinBase, Date)}
     * implementation.
     *
     * This will cause the simulation sub-system to run this instance regardless of the instances current
     * {@link DigitalTwinBase#NextSimulationTime}.
     */
    void runThisInstance();

    /**
     * Stop the simulation.
     * @return a {@link SimulationStatus#InstanceRequestedStop}.
     */
    SimulationStatus stopSimulation();


}
