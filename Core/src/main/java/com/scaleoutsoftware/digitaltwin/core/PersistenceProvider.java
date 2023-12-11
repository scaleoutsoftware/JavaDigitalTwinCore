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

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * An interface that can be used for persisting/retrieving the state of real-time digital twins.
 */
public interface PersistenceProvider {

    /**
     * Returns true if this PersistenceProvider is active, false otherwise.
     * @return true if this PersistenceProvider is active, false otherwise.
     */
    public abstract boolean isActive();

    /**
     * Retrieves this persistence providers type. Currently supported provider types: AzureDigitalTwins.
     * @return the persistence provider type.
     */
    public PersistenceProviderType getProviderType();

    /**
     * Retrieves a future that when complete will return the instance IDs stored in a container, or an empty list if no instances exist.
     * @param containerName the container name.
     * @return a future that will return a list of instances.
     */
    public CompletableFuture<List<String>> getInstanceIdsAsync(String containerName);

    /**
     * Retrieves the instance IDs stored in a container, or an empty list if no instances exist.
     * @param containerName the container name.
     * @return a list of instances.
     */
    public List<String> getInstanceIds(String containerName);

    /**
     * Retrieves a future that when complete will return an instance or null if it doesn't exist.
     * @param containerName the container name.
     * @param instanceId the instance identifier.
     * @return a future that will return an instance or null.
     */
    public CompletableFuture<String> getInstanceAsync(String containerName, String instanceId);

    /**
     * Retrieves an instance or null if it doesn't exist.
     * @param containerName the container name
     * @param instanceId the instance identifier
     * @return the instance or null.
     */
    public String getInstance(String containerName, String instanceId);

    /**
     * Retrieves a future that will return a map of property names to property, or an empty map.
     * @param containerName the container name.
     * @return a future that will return a map of property names to property types.
     */
    public CompletableFuture<Map<String,String>> getPropertyMapAsync(String containerName);

    /**
     * Retrieves a map of property names to property types, or an empty map.
     * @param containerName the container name.
     * @return a map of property names to property types.
     */
    public Map<String,String> getPropertyMap(String containerName);

    /**
     * Retrieves a future that will complete exceptionally or return void if the instance's property was successfully updated.
     * Updates a property for the provided instance id in the provided container specified by the property name and property value.
     * @param containerName the container name.
     * @param instanceId the instance id.
     * @param propertyName the property name.
     * @param propertyValue the property value.
     * @return a future that will complete exceptionally or return void.
     */
    public CompletableFuture<Void> updatePropertyAsync(String containerName, String instanceId, String propertyName, Object propertyValue);

    /**
     * Updates a property for the provided instance id in the provided container specified by the property name and property value.
     * @param containerName the container name.
     * @param instanceId the instance id.
     * @param propertyName the property name.
     * @param propertyValue the property value.
     */
    public void updateProperty(String containerName, String instanceId, String propertyName, Object propertyValue);

    /**
     * Retrieves a future that will return a property or null if the property does not exist.
     * @param containerName the container name.
     * @param instanceId the instance id.
     * @param propertyName the property name.
     * @param clazz the class representing the property.
     * @param <T> the type of the property to return
     * @return the property or null if the property does not exist.
     */
    public <T> CompletableFuture<T> getPropertyAsync(String containerName, String instanceId, String propertyName, Class<T> clazz);

    /**
     * Retrieves a property or null if the property does not exist.
     * @param containerName the container name.
     * @param instanceId the instance id.
     * @param propertyName the property name
     * @param clazz the class representing the property.
     * @param <T> the type of the property to return.
     * @return the property or null if the property does not exist.
     */
    public <T> T getProperty(String containerName, String instanceId, String propertyName, Class<T> clazz);

    /**
     * Retrieves a future that will complete exceptionally or return void if the RTDT's property was successfully updated.
     * Updates a RTDT property for the provided instance id specified by the property name and property value.
     * @param instanceId the instance id.
     * @param propertyName the property name.
     * @param propertyValue the property value.
     * @return a future that will complete exceptionally or return void.
     */
    public CompletableFuture<Void> updateRtdtPropertyAsync(String instanceId, String propertyName, Object propertyValue);

    /**
     * Updates a RTDT property for the provided instance id specified by the property name and property value.
     * @param instanceId the instance id.
     * @param propertyName the property name.
     * @param propertyValue the property value.
     */
    public void updateRtdtProperty(String instanceId, String propertyName, Object propertyValue);

    /**
     * Retrieves a future that will return a property value for a RTDT instance or null if the property doesn't exist.
     * @param instanceId the instance id.
     * @param propertyName the property name.
     * @param clazz the class of the property type
     * @param <T> the type of the property to return.
     * @return a future that will return a property value for a RTDT instance.
     */
    public <T> CompletableFuture<T> getRtdtPropertyAsync(String instanceId, String propertyName, Class<T> clazz);

    /**
     * Retrieves a property for a RTDT instance or null if the property does not exist.
     * @param instanceId the instance id.
     * @param propertyName the property name.
     * @param clazz the class of the property type.
     * @param <T> the type of the property to return.
     * @return the property value.
     */
    public <T> T getRtdtProperty(String instanceId, String propertyName, Class<T> clazz);
}
