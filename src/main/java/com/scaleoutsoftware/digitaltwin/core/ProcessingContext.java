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

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;

/**
 * Context object that allows the user to send a message to a DataSource.
 */
public abstract class ProcessingContext implements Serializable {

    /**
     * Sends a message to a data source
     * @param payload the message (as a serialized JSON string)
     * @return the sending result
     */
    public abstract SendingResult sendToDataSource(byte[] payload);

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
    public abstract SendingResult sendToDigitalTwin(String model, String id, byte[] payload);

    /**
     * <p>
     *     This method sends a JSON message to a real-time digital twin
     * </p>
     *
     * <p>
     *     Note, the message contents must be serialized so that the registered message type
     *     of the digital twin model will be sufficient to deserialize the message.
     * </p>
     *
     * @param model the model of the digital twin
     * @param id the id of the digital twin
     * @param payload the JSON message
     * @return the sending result
     */
    public abstract SendingResult sendToDigitalTwin(String model, String id, String payload);

    /**
     * <p>
     *     This method sends a list of serialized JSON message to a real-time digital twin
     * </p>
     *
     * <p>
     *     Note, the message contents must be serialized so that the registered message type
     *     of the digital twin model will be sufficient to deserialize the message.
     * </p>
     *
     * @param model the model of the digital twin
     * @param id the id of the digital twin
     * @param payload the JSON message
     * @return the sending result
     */
    public abstract SendingResult sendToDigitalTwin(String model, String id, List<byte[]> payload);


    /**
     * <p>
     *     This method sends an alert message to supported systems. See "TODO: Link to docs" for more details on supported systems.
     * </p>
     *
     * <p>
     *     When a model is deployed, an optional alerting provider configuration can be supplied. The provider name corresponds
     *     to the name of the configured alerting provider. For example, if an alerting provider configuration is called
     *     "SREPod1", then the processing context will send an alert message to the alerting provider configured with the name
     *     "SREPod1".
     * </p>
     *
     * <p>
     *     If the message cannot be sent then {@link SendingResult#NotHandled} will be returned. If the message is sent
     *     and in process of sending then {@link SendingResult#Enqueued} will be returned. Once the message is successfully sent
     *     then the returned enum will be changed to {@link SendingResult#Handled}.
     * </p>
     * @param alertingProviderName the alerting provider name. Note, must match a valid configuration.
     * @param alert the alert message.
     * @return the sending result.
     */
    public abstract SendingResult sendAlert(String alertingProviderName, AlertMessage alert);

    /**
     * Returns a list of registered and active persistence providers.
     * @return a list of registered and active persistence providers.
     */
    public abstract List<PersistenceProvider> getPersistenceProviders();


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
     * Logs a message to the real-time digital twin cloud service.
     *
     * Note: the only supported severity levels are: INFO, WARN, and SEVERE
     *
     * @param severity the severity of the log message
     * @param message the message to log
     */
    public abstract void logMessage(Level severity, String message);
}
