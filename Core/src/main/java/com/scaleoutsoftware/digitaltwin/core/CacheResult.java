package com.scaleoutsoftware.digitaltwin.core;

/**
 * Represents a response from a {@link SharedData} operation.
 */
public interface CacheResult {
    /**
     * Gets the key or null to the object associated with the result.
     * @return the key or null.
     */
    public String getKey();

    /**
     * Get the object returned from a Get operation.
     * @return the object or null.
     */
    public byte[] getValue();

    /**
     * Gets the status of the cache operation.
     * @return the operation status.
     */
    CacheOperationStatus getStatus();
}
