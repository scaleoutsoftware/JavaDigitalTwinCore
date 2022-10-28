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

import java.util.List;

/**
 * The ModelSchema class is used as a Java object representation of the model.json schema file used for deploying a
 * digital twin model to the real-time digital twin cloud service.
 */
public class ModelSchema {
    private final String                            modelType;
    private final String                            messageProcessorType;
    private final String                            messageType;
    private final String                            assemblyName;
    private final String                            azureDigitalTwinModelName;
    private final List<AlertProviderConfiguration>  alertProviders;

    private ModelSchema() {
        modelType = messageProcessorType = messageType = assemblyName = azureDigitalTwinModelName = null;
        alertProviders = null;
    }

    /**
     * Creates a model schema from a digital twin class, a message processor class, and a message class.
     * @param dtClass the digital twin class implementation.
     * @param mpClass the message processor class implementation.
     * @param msgClass a JSON serializable message class.
     */
    public ModelSchema(
            String dtClass,
            String mpClass,
            String msgClass) {
        if( (dtClass    == null || dtClass.isEmpty()) ||
            (mpClass    == null || mpClass.isEmpty()) ||
            (msgClass   == null || msgClass.isEmpty())
        ) {
            throw new IllegalArgumentException(String.format("Expected value for dtClass, mpClass, and msgClass; actual values: %s, %s, %s",
                    (dtClass == null ? "null dtClass" : dtClass),
                    (mpClass == null ? "null mpClass" : mpClass),
                    (msgClass == null ? "null mpClass" : msgClass)
                    ));
        }
        modelType                   = dtClass;
        messageProcessorType        = mpClass;
        messageType                 = msgClass;
        assemblyName                = "NOT_USED_BY_JAVA_MODELS";
        alertProviders              = null;
        azureDigitalTwinModelName   = null;
    }

    /**
     * Creates a model schema from a digital twin class, a message processor class, a message class, and
     * alert provider configurations.
     * @param dtClass the digital twin class implementation.
     * @param mpClass the message processor class implementation.
     * @param msgClass a JSON serializable message class.
     * @param alertingProviders the alerting provider configurations.
     */
    public ModelSchema(
            String dtClass,
            String mpClass,
            String msgClass,
            List<AlertProviderConfiguration> alertingProviders) {
        if( (dtClass    == null || dtClass.isEmpty()) ||
                (mpClass    == null || mpClass.isEmpty()) ||
                (msgClass   == null || msgClass.isEmpty())
        ) {
            throw new IllegalArgumentException(String.format("Expected value for dtClass, mpClass, and msgClass; actual values: %s, %s, %s",
                    (dtClass == null ? "null dtClass" : dtClass),
                    (mpClass == null ? "null mpClass" : mpClass),
                    (msgClass == null ? "null mpClass" : msgClass)
            ));
        }
        modelType                   = dtClass;
        messageProcessorType        = mpClass;
        messageType                 = msgClass;
        assemblyName                = "NOT_USED_BY_JAVA_MODELS";
        azureDigitalTwinModelName   = null;
        alertProviders              = alertingProviders;
    }
    /**
     * Creates a model schema from a digital twin class, a message processor class, a message class, and
     * alert provider configurations.
     * @param dtClass the digital twin class implementation.
     * @param mpClass the message processor class implementation.
     * @param msgClass a JSON serializable message class.
     * @param adtModelName the Azure Digital Twin model name.
     * @param alertingProviders the alerting provider configurations.
     */
    public ModelSchema(
            String dtClass,
            String mpClass,
            String msgClass,
            String adtModelName,
            List<AlertProviderConfiguration> alertingProviders) {
        if( (dtClass    == null || dtClass.isEmpty()) ||
                (mpClass    == null || mpClass.isEmpty()) ||
                (msgClass   == null || msgClass.isEmpty())
        ) {
            throw new IllegalArgumentException(String.format("Expected value for dtClass, mpClass, and msgClass; actual values: %s, %s, %s",
                    (dtClass == null ? "null dtClass" : dtClass),
                    (mpClass == null ? "null mpClass" : mpClass),
                    (msgClass == null ? "null mpClass" : msgClass)
            ));
        }
        modelType                   = dtClass;
        messageProcessorType        = mpClass;
        messageType                 = msgClass;
        assemblyName                = "NOT_USED_BY_JAVA_MODELS";
        azureDigitalTwinModelName   = adtModelName;
        alertProviders              = alertingProviders;
    }

    /**
     * Retrieve the digital twin model type (a {@link DigitalTwinBase} implementation).
     * @return the model type.
     */
    public String getModelType() {
        return modelType;
    }

    /**
     * Retrieve the message type (JSON serializable message implementation).
     * @return the message type.
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * Retrieve the message processor type (a {@link MessageProcessor} implementation).
     * @return the message processor type.
     */
    public String getMessageProcessorType() {
        return messageProcessorType;
    }

    /**
     * NOT USED BY JAVA MODEL SCHEMA
     * @return NOT USED BY JAVA MODEL SCHEMA
     */
    public String getAssemblyName() {
        return assemblyName;
    }

    /**
     * Retrieve the alert provider configurations.
     * @return the alert provider configurations.
     */
    public List<AlertProviderConfiguration> getAlertProviders() {return alertProviders; }

    /**
     * Retrieve the Azure Digital Twin model name.
     * @return the Azure Digital Twin model name.
     */
    public String getAzureDigitalTwinModelName() {
        return azureDigitalTwinModelName;
    }

}
