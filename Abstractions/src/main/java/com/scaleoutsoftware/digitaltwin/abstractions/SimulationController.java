/*
 Copyright (c) 2026 by ScaleOut Software, Inc.

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
package com.scaleoutsoftware.digitaltwin.abstractions;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

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
     *
     */

    /**
     * <p>
     * Retrieves the simulation start time.
     * </p>
     * @return the simulation start time.
     */
    Date getSimulationStartTime();

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
     * if the delay was not processed.
     */
    void delay(Duration duration);

    /**
     * <p>
     * Delay simulation processing for this DigitalTwin instance, indefinitely.
     * </p>
     *
     * <p>
     * Simulation processing will be delayed until this instance is run with {@link SimulationController#runThisInstance()}.
     * </p>
     *
     * if the delay was not processed.
     */
    void delayIndefinitely();

    /**
     * <p>
     * Asynchronously send a JSON serialized message to a DigitalTwin instance that will be processed by the DigitalTwin
     * models {@link MessageProcessor#processMessage(ProcessingContext, DigitalTwinBase, byte[])} method.
     * </p>
     * @param modelName the model to send the messages too.
     * @param telemetryMessage a blob representing a JSON serialized messages.
     * @return {@link SendingResult#Handled} if the messages were processed, {@link SendingResult#Enqueued} if
     * the messages are in process of being handled, or {@link SendingResult#NotHandled} if the delay was not processed.
     */
    CompletableFuture<SendingResult> emitTelemetry(String modelName, byte[] telemetryMessage);

    /**
     * Create a new digital twin instance for simulation processing.
     * @param modelName the model name.
     * @param instanceId the instance id.
     * @param base the instance to create.
     * @return {@link SendingResult#Handled} if the instance was created, {@link SendingResult#Enqueued} if the instance
     * is in process of being created, or {@link SendingResult#NotHandled} if the instance could not be created.
     * @param <T> the type of the digital twin to create.
     */
    <T extends DigitalTwinBase<T>> CompletableFuture<CreateResult> createInstance(String modelName, String instanceId, T base);

    /**
     * Delete and remove a digital twin instance from simulation processing.
     * @param modelName the model name.
     * @param instanceId the instance id.
     * @return {@link SendingResult#Handled} if the instance was deleted, {@link SendingResult#Enqueued} if the instance
     * is in process of being deleted, or {@link SendingResult#NotHandled} if the instance could not be deleted.
     */
    CompletableFuture<DeleteResult> deleteInstance(String modelName, String instanceId);

    /**
     * Delete and remove this digital twin instance from simulation processing.
     * @return this local request will always return {@link SendingResult#Handled}.
     */
    CompletableFuture<DeleteResult> deleteThisInstance();

    /**
     * Run this instance during this simulation step. The instance will be run using the models {@link SimulationProcessor#processModel(ProcessingContext, DigitalTwinBase, Date)}
     * implementation.
     *
     * This will cause the simulation sub-system to run this instance during the current simulation step.
     */
    void runThisInstance();

    /**
     * Stop the simulation.
     * @return a {@link SimulationStatus#InstanceRequestedStop}.
     */
    SimulationStatus stopSimulation();

}
