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

public class AlertProviderConfiguration implements Serializable {
    private final String URL;
    private final String IntegrationKey;
    private final String RoutingKey;
    private final String Name;
    private final String EntityId;

    private AlertProviderConfiguration() {URL = IntegrationKey = RoutingKey = Name = EntityId = null;}

    public AlertProviderConfiguration(String url, String integrationKey, String routingKey, String name, String entityId) {
        URL             = url;
        IntegrationKey  = integrationKey;
        RoutingKey      = routingKey;
        Name            = name;
        EntityId        = entityId;
    }

    public String getURL() {
        return URL;
    }

    public String getIntegrationKey() {
        return IntegrationKey;
    }

    public String getRoutingKey() {
        return RoutingKey;
    }

    public String getName() {
        return Name;
    }

    public String getEntityId() {
        return EntityId;
    }

    @Override
    public String toString() {
        return "AlertProviderConfiguration{" +
                "URL='" + URL + '\'' +
                ", IntegrationKey='" + IntegrationKey + '\'' +
                ", RoutingKey='" + RoutingKey + '\'' +
                ", Name='" + Name + '\'' +
                ", EntityId='" + EntityId + '\'' +
                '}';
    }
}
