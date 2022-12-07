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
     * @return {@link SendingResult#Handled} if the delay was process or {@link SendingResult#NotHandled}
     * if the delay was not processed.
     */
    SendingResult delay(Duration duration);

    /**
     * Send a list of JSON serialized messages to a DigitalTwin instance that will be processed by the DigitalTwin
     * models {@link MessageProcessor#processMessages(ProcessingContext, DigitalTwinBase, Iterable)} method.
     * @param telemetryMessage the list of JSON serialized messages
     * @return {@link SendingResult#Handled} if the messages were processed, {@link SendingResult#Enqueued} if
     * the messages are in process of being handled, or {@link SendingResult#NotHandled} if the delay was not processed.
     */
    SendingResult emitTelemetry(byte[] telemetryMessage);

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
