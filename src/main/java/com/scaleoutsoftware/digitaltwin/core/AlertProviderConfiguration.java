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
 * Configuration for an alert provider.
 */
public class AlertProviderConfiguration implements Serializable {
    private final String alertProviderType;
    private final String url;
    private final String integrationKey;
    private final String routingKey;
    private final String name;
    private final String entityId;

    private AlertProviderConfiguration() {alertProviderType = url = integrationKey = routingKey = name = entityId = null;}

    /**
     * Construct an alert provider configuration.
     * @param alertProviderType the alert provider type.
     * @param url the alert provider URL where alerts should be posted.
     * @param integrationKey the integration key.
     * @param routingKey the routing key.
     * @param name the name of the alert provider.
     * @param entityId the entity Id.
     */
    public AlertProviderConfiguration(String alertProviderType, String url, String integrationKey, String routingKey, String name, String entityId) {
        this.alertProviderType  = alertProviderType;
        this.url                = url;
        this.integrationKey     = integrationKey;
        this.routingKey         = routingKey;
        this.name               = name;
        this.entityId           = entityId;
    }

    /**
     * Retrieve the alert provider type for this configuration.
     * @return the alert provider type.
     */
    public String getAlertProviderType() {
        return alertProviderType;
    }

    /**
     * Retrieve the URL for this alert provider configuration.
     * @return the URL for this alert provider configuration.
     */
    public String getURL() {
        return url;
    }

    /**
     * Retrieve the integration key for this alert provider configuration.
     * @return the integration key for this alert provider configuration.
     */
    public String getIntegrationKey() {
        return integrationKey;
    }

    /**
     * Retrieve the routing key for this alert provider configuration.
     * @return the routing key for this alert provider configuration.
     */
    public String getRoutingKey() {
        return routingKey;
    }

    /**
     * Retrieve the name of this alert provider configuration.
     * @return the name of this alert provider configuration.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieve the entity ID for this alert provider configuration.
     * @return the entity ID for this alert provider configuration.
     */
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
