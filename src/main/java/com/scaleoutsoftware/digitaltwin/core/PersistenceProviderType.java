/*
 Copyright (c) 2021 by ScaleOut Software, Inc.

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

/**
 * Available {@link PersistenceProvider} types.
 */
public enum PersistenceProviderType implements Serializable {
    /**
     * Enum for the Azure Digital Twin service.
     */
    AzureDigitalTwinsService("AzureDigitalTwinsService"),
    SQLite("SQLite"),
    SQLServer("SQLServer");
    private String _value;
    PersistenceProviderType(String name) {
        _value = name;
    }

    public static PersistenceProviderType fromString(String name) {
        if(name != null && !name.isEmpty() && !name.isBlank()) {
            switch(name) {
                case "AzureDigitalTwinsService":
                    return AzureDigitalTwinsService;
                case "SQLite":
                    return SQLite;
                case "SQLServer":
                    return SQLServer;
                default:
                    return null;

            }
        } else {
            return null;
        }
    }
}
