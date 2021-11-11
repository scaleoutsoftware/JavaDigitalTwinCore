package com.scaleoutsoftware.digitaltwin.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface PersistenceProvider {
    /**
     * Retrieves this persistence providers type. Currently supported provider types: AzureDigitalTwins.
     * @return the persistence provider type.
     */
    public PersistenceProviderType getProviderType();

    /**
     * Retrieves a future that when complete will return a list of available containers, or an empty list if no containers
     * are available.
     * @return a future that will return a list of container names.
     */
    public CompletableFuture<List<String>> getContainerListAsync();

    /**
     * Retrieves a list of available containers, or an empty list if no containers are available.
     * @return a list of available containers.
     */
    public List<String> getContainerList();

    /**
     * Retrieves a future that when complete will return a containers schema, or null if the container does not exist.
     * @param containerName the container name.
     * @return the schema (as a string) for this container.
     */
    public CompletableFuture<String> getContainerSchemaAsync(String containerName);

    /**
     * Retrieves a containers schema, or null if the container does not exist.
     * @param containerName the containers name.
     * @return the schema (as a string) for this container.
     */
    public String getContainerSchema(String containerName);

    /**
     * Retrieves a future that when complete will return the instances of a container, or an empty list if no instances exist.
     * @param containerName the container name.
     * @return a future that will return a list of instances.
     */
    public CompletableFuture<List<String>> getInstanceListAsync(String containerName);

    /**
     * Retrieves the instances of a container, or an empty list if no instances exist.
     * @param containerName the container name.
     * @return a list of instances.
     */
    public List<String> getInstanceList(String containerName);

    /**
     * Retrieves a future that will return a map of property names to property, or an empty map.
     * @param containerName the container name.
     * @return a future that will return a map of property names to property types.
     */
    public CompletableFuture<Map<String,String>> getPropertyListAsync(String containerName);

    /**
     * Retrieves a map of property names to property types, or an empty map.
     * @param containerName the container name.
     * @return a map of property names to property types.
     */
    public Map<String,String> getPropertyList(String containerName);

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
     * @param containerName
     * @param instanceId
     * @param propertyName
     * @param propertyValue
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
