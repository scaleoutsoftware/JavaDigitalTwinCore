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
    AzureDigitalTwinsService("AzureDigitalTwinsService", 1),
    /**
     * Enum for DynamoDB
     */
    DynamoDb("DynamoDB", 5),
    /**
     * Enum for SQLite
     */
    SQLite("SQLite", 4),
    /**
     * Enum for SQLServer
     */
    SQLServer("SQLServer", 3),

    /**
     * Enum for an unconfigured PersistenceProvider
     */
    Unconfigured("", 0);

    private final String _name;
    private final int _value;
    PersistenceProviderType(String name, int ordinal) {
        _name = name;
        _value = ordinal;
    }


    public String getName() {
        return _name;
    }

    public int getServiceOrdinalValue() {
        return _value;
    }

    /**
     * Return the PersistenceProviderType from a string value. We do not rely on Java's naming
     * because the string values need to be cross-platform and the names may be different in each language.
     * @param name the enums name.
     * @return the associated PersistenceProviderType, or null if no association exists.
     */
    public static PersistenceProviderType fromString(String name) {
        if(name != null && !name.isEmpty() && !name.isBlank()) {
            switch(name) {
                case "AzureDigitalTwinsService":
                    return AzureDigitalTwinsService;
                case "SQLite":
                    return SQLite;
                case "SQLServer":
                    return SQLServer;
                case "DynamoDB":
                    return DynamoDb;
                default:
                    return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Return the PersistenceProviderType from an ordinal value. We do not rely on Java's ordering
     * because the ordinal values need to be cross-platform, and the values may be ordered differently.
     * @param ordinal the enums ordinal value.
     * @return the associated PersistenceProviderType, or null if no association exists.
     */
    public static PersistenceProviderType fromOrdinal(int ordinal) {
        switch(ordinal) {
            case 1:
                return AzureDigitalTwinsService;
            case 3:
                return SQLServer;
            case 4:
                return SQLite;
            case 5:
                return DynamoDb;
            default:
                return null;
        }
    }
}
