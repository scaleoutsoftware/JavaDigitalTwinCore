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
    private final String alertProviderType;
    private final String url;
    private final String integrationKey;
    private final String routingKey;
    private final String name;
    private final String entityId;

    private AlertProviderConfiguration() {alertProviderType = url = integrationKey = routingKey = name = entityId = null;}

    public AlertProviderConfiguration(String alertProviderType, String url, String integrationKey, String routingKey, String name, String entityId) {
        this.alertProviderType  = alertProviderType;
        this.url                = url;
        this.integrationKey     = integrationKey;
        this.routingKey         = routingKey;
        this.name               = name;
        this.entityId           = entityId;
    }

    public String getAlertProviderType() {
        return alertProviderType;
    }

    public String getURL() {
        return url;
    }

    public String getIntegrationKey() {
        return integrationKey;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public String getName() {
        return name;
    }

    public String getEntityId() {
        return entityId;
    }

    @Override
    public String toString() {
        return "AlertProviderConfiguration{" +
                "alertProviderType=" + alertProviderType + '\'' +
                ", URL='" + url + '\'' +
                ", IntegrationKey='" + integrationKey + '\'' +
                ", RoutingKey='" + routingKey + '\'' +
                ", Name='" + name + '\'' +
                ", EntityId='" + entityId + '\'' +
                '}';
    }
}
