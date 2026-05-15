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

import java.io.Serializable;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * Context object that allows the user to send a message to a DataSource.
 * @param <T> the type of the digital twin
 */
public abstract class ProcessingContext<T extends DigitalTwinBase<T>> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public ProcessingContext() {}

    /**
     * <p>
     * Sends a message to a data source. This will route messages through the connector back to the data source.
     * </p>
     *
     * <p>
     * if the datasource is simulation instance, then the message will be sent to the simulation model's implementation
     * of the {@link MessageProcessor}.
     * </p>
     *
     * @param payload the message (as a serialized JSON string)
     * @return the sending result
     */
    public abstract CompletableFuture<SendingResult> sendToDataSource(byte[] payload);

    /**
     * <p>
     *     This method sends a serialized JSON message to a real-time digital twin
     * </p>
     *
     * <p>
     *     Note, the message contents must be serialized so that the registered message type
     *     of the digital twin model will be sufficient to deserialize the message.
     * </p>
     * @param model the model of the digital twin
     * @param id the id of the digital twin
     * @param payload the serialized JSON message
     * @return the sending result
     */
    public abstract CompletableFuture<SendingResult> sendToDigitalTwin(String model, String id, byte[] payload);

    /**
     * <p>
     *     This method sends an alert message to configured systems.
     * </p>
     *
     * <p>
     *     If the message cannot be sent then the returned CompletableFuture will complete exceptionally.
     * </p>
     * @param alert the alert message.
     * @return the sending result.
     */
    public abstract CompletableFuture<Void> sendAlert(AlertMessage alert);

    /**
     * Returns an {@link AzureDigitalTwinsProvider} or null if no AzureDigitalTwinsProvider configuration can be found.
     * @return a {@link AzureDigitalTwinsProvider} or null.
     */
    public abstract AzureDigitalTwinsProvider getAzureDigitalTwinsProvider();

    /**
     * Retrieve the unique Identifier for a DataSource (matches the Device/Datasource/Real-time twin ID)
     * @return the digital twin id
     */
    public abstract String getDataSourceId();

    /**
     * Retrieve the model for a DigitalTwin (matches the model of a Device/Datasource/real-time twin)
     * @return the digital twin model
     */
    public abstract String getDigitalTwinModel();

    /**
     * Log a message to the real-time digital twin UI.
     *
     * Note: the only supported severity levels are: INFO, WARN, and SEVERE
     *
     * @param severity the severity of the log message
     * @param message the message to log
     * @return A future that will complete successfully with no result or exceptionally.
     */
    public abstract CompletableFuture<Void> logMessage(Level severity, String message);

    /**
     * Delete the target real-time twin instance.
     * @param targetTwinModel the model of the real-time twin instance
     * @param targetTwinId the id of the real-time twin instance
     * @return a completable future that will complete with a {@link DeleteResult} indicating the status of the operation, or
     * exceptionally indicating that an error occurred.
     */
    public abstract CompletableFuture<DeleteResult> removeRealTimeTwin(String targetTwinModel, String targetTwinId);

    /**
     * Starts a new timer for the digital twin
     * @param timerName the timer name
     * @param interval the timer interval
     * @param timerType the timer type
     * @param timerHandler the time handler callback
     * @param timerHandlerClass the timer handler callback class
     * @return returns {@link TimerActionResult#Success} if the timer was started, {@link TimerActionResult#FailedTooManyTimers}
     * if too many timers exist, or {@link TimerActionResult#FailedInternalError} if an unexpected error occurs.
     */
    public abstract TimerActionResult startTimer(String timerName, Duration interval, TimerType timerType, TimerHandler<T> timerHandler, Class<? extends TimerHandler<T>> timerHandlerClass);

    /**
     * Stops the specified timer.
     * @param timerName the timer name.
     * @return returns {@link TimerActionResult#Success} if the timer was stopped, {@link TimerActionResult#FailedNoSuchTimer}
     * if no timer exists with that name, or {@link TimerActionResult#FailedInternalError} if an unexpected error occurs.
     */
    public abstract TimerActionResult stopTimer(String timerName);

    /**
     * Retrieves the current time. If the model (simulation or real-time) is running inside of a simulation then the
     * simulation time will be returned.
     *
     * @return The current time (real time, or simulation if running under simulation).
     */
    public abstract Date getCurrentTime();

    /**
     * Retrieve the  running  {@link SimulationController} or null if no simulation is running.
     * @return the {@link SimulationController} or null if no simulation is running.
     */
    public abstract SimulationController getSimulationController();

    /**
     * Retrieve a {@link SharedData} accessor for this model's shared data.
     * @return a {@link SharedData} instance.
     */
    public abstract SharedData getSharedModelData();

    /**
     * Retrieve a {@link SharedData} accessor for globally shared data.
     * @return a {@link SharedData} instance.
     */
    public abstract SharedData getSharedGlobalData();

}
