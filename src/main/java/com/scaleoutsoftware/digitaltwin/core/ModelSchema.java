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
    private final List<AlertProviderConfiguration>  alertProviders;

    private ModelSchema() {
        modelType = messageProcessorType = messageType = assemblyName = "default constructor";
        alertProviders = null;
    }

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
        modelType               = dtClass;
        messageProcessorType    = mpClass;
        messageType             = msgClass;
        assemblyName            = "NOT_USED_BY_JAVA_MODELS";
        alertProviders          = null;
    }

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
        modelType               = dtClass;
        messageProcessorType    = mpClass;
        messageType             = msgClass;
        assemblyName            = "NOT_USED_BY_JAVA_MODELS";
        alertProviders          = alertingProviders;
    }

    public String getModelType() {
        return modelType;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getMessageProcessorType() {
        return messageProcessorType;
    }

    public String getAssemblyName() {
        return assemblyName;
    }

    public List<AlertProviderConfiguration> getAlertProviders() {return alertProviders; }

}
