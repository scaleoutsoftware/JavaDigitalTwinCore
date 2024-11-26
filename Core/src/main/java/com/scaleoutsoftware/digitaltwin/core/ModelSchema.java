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
    private final String                            simulationProcessorType;
    private final String                            messageType;
    private final String                            assemblyName;
    private final String                            entryPoint;
    private final String                            azureDigitalTwinModelName;
    private final String                            persistenceProvider;
    private final boolean                           enablePersistence;
    private final boolean                           enableSimulationSupport;
    private final boolean                           enableMessageRecording;
    private final List<AlertProviderConfiguration>  alertProviders;

    private ModelSchema() {
        modelType = messageProcessorType = simulationProcessorType = messageType = assemblyName = entryPoint = azureDigitalTwinModelName = persistenceProvider = null;
        enablePersistence       = false;
        enableSimulationSupport = false;
        enableMessageRecording  = false;
        alertProviders          = null;
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
        simulationProcessorType     = null;
        enableSimulationSupport     = false;
        messageType                 = msgClass;
        assemblyName                = "NOT_USED_BY_JAVA_MODELS";
        entryPoint                  = null;
        alertProviders              = null;
        azureDigitalTwinModelName   = null;
        enablePersistence           = false;
        enableMessageRecording      = false;
        persistenceProvider         = null;
    }

    public ModelSchema(
            String dtClass,
            String mpClass,
            String msgClass,
            String ep) {
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
        simulationProcessorType     = null;
        enableSimulationSupport     = false;
        messageType                 = msgClass;
        assemblyName                = "NOT_USED_BY_JAVA_MODELS";
        entryPoint                  = ep;
        alertProviders              = null;
        azureDigitalTwinModelName   = null;
        enablePersistence           = false;
        enableMessageRecording      = false;
        persistenceProvider         = null;
    }

    /**
     * Creates a model schema from a digital twin class, a message processor class, and a message class.
     * @param dtClass the digital twin class implementation.
     * @param mpClass the message processor class implementation.
     * @param msgClass a JSON serializable message class.
     * @param emr enable message recording for this model.
     */
    public ModelSchema(
            String dtClass,
            String mpClass,
            String msgClass,
            boolean emr) {
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
        simulationProcessorType     = null;
        enableSimulationSupport     = false;
        messageType                 = msgClass;
        assemblyName                = "NOT_USED_BY_JAVA_MODELS";
        entryPoint                  = null;
        alertProviders              = null;
        azureDigitalTwinModelName   = null;
        enablePersistence           = false;
        enableMessageRecording      = emr;
        persistenceProvider         = null;
    }

    public ModelSchema(
            String dtClass,
            String mpClass,
            String msgClass,
            String ep,
            boolean emr) {
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
        simulationProcessorType     = null;
        enableSimulationSupport     = false;
        messageType                 = msgClass;
        assemblyName                = "NOT_USED_BY_JAVA_MODELS";
        entryPoint                  = ep;
        alertProviders              = null;
        azureDigitalTwinModelName   = null;
        enablePersistence           = false;
        enableMessageRecording      = emr;
        persistenceProvider         = null;
    }

    // TODO

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
        simulationProcessorType     = null;
        enableSimulationSupport     = false;
        messageType                 = msgClass;
        assemblyName                = "NOT_USED_BY_JAVA_MODELS";
        entryPoint                  = null;
        azureDigitalTwinModelName   = null;
        enablePersistence           = false;
        enableMessageRecording      = false;
        persistenceProvider         = null;
        alertProviders              = alertingProviders;
    }

    /**
     * Creates a model schema from a digital twin class, a message processor class, a message class, and
     * alert provider configurations.
     * @param dtClass the digital twin class implementation.
     * @param mpClass the message processor class implementation.
     * @param msgClass a JSON serializable message class.
     * @param spClass the simulation processor class implementation.
     * @param alertingProviders the alerting provider configurations.
     */
    public ModelSchema(
            String dtClass,
            String mpClass,
            String msgClass,
            String spClass,
            List<AlertProviderConfiguration> alertingProviders) {
        if( (dtClass    == null || dtClass.isEmpty()) ||
                (mpClass    == null || mpClass.isEmpty()) ||
                (msgClass   == null || msgClass.isEmpty()) ||
                (spClass    == null || spClass.isEmpty())
        ) {
            throw new IllegalArgumentException(String.format("Expected value for dtClass, mpClass, and msgClass; actual values: %s, %s, %s",
                    (dtClass == null ? "null dtClass" : dtClass),
                    (mpClass == null ? "null mpClass" : mpClass),
                    (msgClass == null ? "null mpClass" : msgClass),
                    (spClass == null ? "null mpClass" : msgClass)
            ));
        }
        modelType                   = dtClass;
        messageProcessorType        = mpClass;
        simulationProcessorType     = spClass;
        enableSimulationSupport     = true;
        messageType                 = msgClass;
        assemblyName                = "NOT_USED_BY_JAVA_MODELS";
        entryPoint                  = null;
        azureDigitalTwinModelName   = null;
        enablePersistence           = false;
        persistenceProvider         = null;
        enableMessageRecording      = false;
        alertProviders              = alertingProviders;
    }

    public ModelSchema(
            String dtClass,
            String mpClass,
            String msgClass,
            String spClass,
            String ep,
            List<AlertProviderConfiguration> alertingProviders) {
        if( (dtClass    == null || dtClass.isEmpty()) ||
                (mpClass    == null || mpClass.isEmpty()) ||
                (msgClass   == null || msgClass.isEmpty()) ||
                (spClass    == null || spClass.isEmpty())
        ) {
            throw new IllegalArgumentException(String.format("Expected value for dtClass, mpClass, and msgClass; actual values: %s, %s, %s",
                    (dtClass == null ? "null dtClass" : dtClass),
                    (mpClass == null ? "null mpClass" : mpClass),
                    (msgClass == null ? "null mpClass" : msgClass),
                    (spClass == null ? "null mpClass" : msgClass)
            ));
        }
        modelType                   = dtClass;
        messageProcessorType        = mpClass;
        simulationProcessorType     = spClass;
        enableSimulationSupport     = true;
        messageType                 = msgClass;
        assemblyName                = "NOT_USED_BY_JAVA_MODELS";
        entryPoint                  = ep;
        azureDigitalTwinModelName   = null;
        enablePersistence           = false;
        persistenceProvider         = null;
        enableMessageRecording      = false;
        alertProviders              = alertingProviders;
    }

    /**
     * Creates a model schema from a digital twin class, a message processor class, a message class, and
     * alert provider configurations.
     * @param dtClass the digital twin class implementation.
     * @param mpClass the message processor class implementation.
     * @param msgClass a JSON serializable message class.
     * @param spClass the simulation processor class implementation.
     * @param alertingProviders the alerting provider configurations.
     * @param emr enable message recording for this model.
     */
    public ModelSchema(
            String dtClass,
            String mpClass,
            String msgClass,
            String spClass,
            List<AlertProviderConfiguration> alertingProviders,
            boolean emr) {
        if( (dtClass    == null || dtClass.isEmpty()) ||
                (mpClass    == null || mpClass.isEmpty()) ||
                (msgClass   == null || msgClass.isEmpty()) ||
                (spClass    == null || spClass.isEmpty())
        ) {
            throw new IllegalArgumentException(String.format("Expected value for dtClass, mpClass, and msgClass; actual values: %s, %s, %s",
                    (dtClass == null ? "null dtClass" : dtClass),
                    (mpClass == null ? "null mpClass" : mpClass),
                    (msgClass == null ? "null mpClass" : msgClass),
                    (spClass == null ? "null mpClass" : msgClass)
            ));
        }
        modelType                   = dtClass;
        messageProcessorType        = mpClass;
        simulationProcessorType     = spClass;
        enableSimulationSupport     = true;
        messageType                 = msgClass;
        assemblyName                = "NOT_USED_BY_JAVA_MODELS";
        entryPoint                  = null;
        azureDigitalTwinModelName   = null;
        enablePersistence           = false;
        persistenceProvider         = null;
        enableMessageRecording      = emr;
        alertProviders              = alertingProviders;
    }

    public ModelSchema(
            String dtClass,
            String mpClass,
            String msgClass,
            String spClass,
            String ep,
            List<AlertProviderConfiguration> alertingProviders,
            boolean emr) {
        if( (dtClass    == null || dtClass.isEmpty()) ||
                (mpClass    == null || mpClass.isEmpty()) ||
                (msgClass   == null || msgClass.isEmpty()) ||
                (spClass    == null || spClass.isEmpty())
        ) {
            throw new IllegalArgumentException(String.format("Expected value for dtClass, mpClass, and msgClass; actual values: %s, %s, %s",
                    (dtClass == null ? "null dtClass" : dtClass),
                    (mpClass == null ? "null mpClass" : mpClass),
                    (msgClass == null ? "null mpClass" : msgClass),
                    (spClass == null ? "null mpClass" : msgClass)
            ));
        }
        modelType                   = dtClass;
        messageProcessorType        = mpClass;
        simulationProcessorType     = spClass;
        enableSimulationSupport     = true;
        messageType                 = msgClass;
        assemblyName                = "NOT_USED_BY_JAVA_MODELS";
        entryPoint                  = ep;
        azureDigitalTwinModelName   = null;
        enablePersistence           = false;
        persistenceProvider         = null;
        enableMessageRecording      = emr;
        alertProviders              = alertingProviders;
    }

    /**
     * Creates a model schema from a digital twin class, a message processor class, a message class, and
     * alert provider configurations.
     * @param dtClass the digital twin class implementation.
     * @param mpClass the message processor class implementation.
     * @param msgClass a JSON serializable message class.
     * @param adtName the Azure Digital Twin model name.
     * @param persistenceType the persistence provider type.
     * @param alertingProviders the alerting provider configurations.
     */
    public ModelSchema(
            String dtClass,
            String mpClass,
            String msgClass,
            String adtName,
            PersistenceProviderType persistenceType,
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
        simulationProcessorType     = null;
        enableSimulationSupport     = false;
        messageType                 = msgClass;
        enableMessageRecording      = false;
        assemblyName                = "NOT_USED_BY_JAVA_MODELS";
        entryPoint                  = null;
        persistenceProvider         = persistenceType.name();
        switch (persistenceType) {
            case AzureDigitalTwinsService:
                azureDigitalTwinModelName   = adtName;
                enablePersistence           = true;
                break;
            case SQLite:
            case SQLServer:
            case DynamoDb:
                enablePersistence           = true;
                azureDigitalTwinModelName   = null;
                break;
            default:
                azureDigitalTwinModelName   = null;
                enablePersistence           = false;
                break;
        }
        alertProviders              = alertingProviders;
    }

    /**
     * Creates a model schema from a digital twin class, a message processor class, a message class, and
     * alert provider configurations.
     * @param dtClass the digital twin class implementation.
     * @param mpClass the message processor class implementation.
     * @param msgClass a JSON serializable message class.
     * @param adtName the Azure Digital Twin model name.
     * @param persistenceType the persistence provider type.
     * @param alertingProviders the alerting provider configurations.
     * @param emr enable message recording for this model.
     */
    public ModelSchema(
            String dtClass,
            String mpClass,
            String msgClass,
            String adtName,
            PersistenceProviderType persistenceType,
            List<AlertProviderConfiguration> alertingProviders,
            boolean emr) {
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
        simulationProcessorType     = null;
        enableSimulationSupport     = false;
        messageType                 = msgClass;
        enableMessageRecording      = emr;
        assemblyName                = "NOT_USED_BY_JAVA_MODELS";
        entryPoint                  = null;
        persistenceProvider         = persistenceType.name();
        switch (persistenceType) {
            case AzureDigitalTwinsService:
                azureDigitalTwinModelName   = adtName;
                enablePersistence           = true;
                break;
            case SQLite:
            case SQLServer:
            case DynamoDb:
                enablePersistence           = true;
                azureDigitalTwinModelName   = null;
                break;
            default:
                azureDigitalTwinModelName   = null;
                enablePersistence           = false;
                break;
        }
        alertProviders              = alertingProviders;
    }

    /**
     * Creates a model schema from a digital twin class, a message processor class, a message class,
     * a simulation processor class, an Azure Digital Twin Model name class, a persistence provider type,
     * and an alert provider configuration.
     *
     * @param dtClass the digital twin class implementation.
     * @param mpClass the message processor class implementation.
     * @param msgClass a JSON serializable message class.
     * @param simulationProcessorClass the simulation processor class implementation.
     * @param adtName the Azure Digital Twin model name.
     * @param persistenceType the persistence provider type.
     * @param alertingProviders the alerting provider configurations.
     */
    public ModelSchema(
            String dtClass,
            String mpClass,
            String msgClass,
            String simulationProcessorClass,
            String adtName,
            PersistenceProviderType persistenceType,
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
        simulationProcessorType     = simulationProcessorClass;
        enableSimulationSupport     = true;
        enableMessageRecording      = false;
        messageType                 = msgClass;
        assemblyName                = "NOT_USED_BY_JAVA_MODELS";
        entryPoint                  = null;
        persistenceProvider         = persistenceType.name();
        switch (persistenceType) {
            case AzureDigitalTwinsService:
                azureDigitalTwinModelName   = adtName;
                enablePersistence           = true;
                break;
            case SQLite:
            case SQLServer:
            case DynamoDb:
                enablePersistence           = true;
                azureDigitalTwinModelName   = null;
                break;
            default:
                azureDigitalTwinModelName   = null;
                enablePersistence           = false;
                break;
        }
        alertProviders              = alertingProviders;
    }

    /**
     * Creates a model schema from a digital twin class, a message processor class, a message class,
     * a simulation processor class, an Azure Digital Twin Model name class, a persistence provider type,
     * and an alert provider configuration.
     *
     * @param dtClass the digital twin class implementation.
     * @param mpClass the message processor class implementation.
     * @param msgClass a JSON serializable message class.
     * @param simulationProcessorClass the simulation processor class implementation.
     * @param adtName the Azure Digital Twin model name.
     * @param persistenceType the persistence provider type.
     * @param alertingProviders the alerting provider configurations.
     * @param emr enable message recording for this model.
     */
    public ModelSchema(
            String dtClass,
            String mpClass,
            String msgClass,
            String simulationProcessorClass,
            String adtName,
            PersistenceProviderType persistenceType,
            List<AlertProviderConfiguration> alertingProviders,
            boolean emr) {
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
        simulationProcessorType     = simulationProcessorClass;
        enableSimulationSupport     = true;
        enableMessageRecording      = emr;
        messageType                 = msgClass;
        assemblyName                = "NOT_USED_BY_JAVA_MODELS";
        entryPoint                  = null;
        persistenceProvider         = persistenceType.name();
        switch (persistenceType) {
            case AzureDigitalTwinsService:
                azureDigitalTwinModelName   = adtName;
                enablePersistence           = true;
                break;
            case SQLite:
            case SQLServer:
            case DynamoDb:
            case CosmosDb:
                enablePersistence           = true;
                azureDigitalTwinModelName   = null;
                break;
            default:
                azureDigitalTwinModelName   = null;
                enablePersistence           = false;
                break;
        }
        alertProviders              = alertingProviders;
    }

    public ModelSchema(
            String dtClass,
            String mpClass,
            String msgClass,
            String simulationProcessorClass,
            String adtName,
            String ep,
            PersistenceProviderType persistenceType,
            List<AlertProviderConfiguration> alertingProviders,
            boolean emr) {
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
        simulationProcessorType     = simulationProcessorClass;
        enableSimulationSupport     = true;
        enableMessageRecording      = emr;
        messageType                 = msgClass;
        assemblyName                = "NOT_USED_BY_JAVA_MODELS";
        entryPoint                  = ep;
        persistenceProvider         = persistenceType.name();
        switch (persistenceType) {
            case AzureDigitalTwinsService:
                azureDigitalTwinModelName   = adtName;
                enablePersistence           = true;
                break;
            case SQLite:
            case SQLServer:
            case DynamoDb:
                enablePersistence           = true;
                azureDigitalTwinModelName   = null;
                break;
            default:
                azureDigitalTwinModelName   = null;
                enablePersistence           = false;
                break;
        }
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
     * Retrieve the simulation processor type (a {@link SimulationProcessor} implementation).
     * @return the simulation processor type.
     */
    public String getSimulationProcessorType() {
        return simulationProcessorType;
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

    /**
     * Retrieve persistence status. True if persistence is enabled, false otherwise.
     * @return True if persistence is enabled, false otherwise.
     */
    public boolean persistenceEnabled() { return enablePersistence; }

    /**
     * Retrieve simulation support enabled status. True if simulation support is enabled, false otherwise.
     * @return True if simulation support is enabled, false otherwise.
     */
    public boolean simulationSupportEnabled() {return enableSimulationSupport;}

    /**
     * Retrieve the persistence provider type.
     * @return the persistence provider type.
     */
    public PersistenceProviderType getPersistenceProvider() { return PersistenceProviderType.fromString(persistenceProvider); }

    /**
     * Retrieves the message recording enabled status. True if this model should persist messages when message recording is active,
     * false otherwise.
     * @return True if message recording is enabled, false otherwise.
     */
    public boolean messageRecordingEnabled() {
        return enableMessageRecording;
    }

    /**
     * Retrieves the packaged model's entry point (fully-qualified class name -- FQCN -- of a Java main) for launching.
     * @return the entry point for launching.
     */
    public String getEntryPoint() {
        return entryPoint;
    }
}
