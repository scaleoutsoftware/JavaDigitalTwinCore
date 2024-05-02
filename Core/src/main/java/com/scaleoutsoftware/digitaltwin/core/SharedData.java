package com.scaleoutsoftware.digitaltwin.core;

public interface SharedData {
    /**
     * Retrieves an existing object from the cache.
     * @param key the key mapping to a value.
     * @return A cache result.
     */
    public CacheResult get(String key);

    /**
     * Put a new key/value mapping into the cache.
     * @param key the key mapping to a value.
     * @param value the value.
     * @return a cache result.
     */
    public CacheResult put(String key, byte[] value);

    /**
     * Remove a key/value mapping from the cache.
     * @param key the key mapping to a value.
     * @return a cache result.
     */
    public CacheResult remove(String key);

    /**
     * Clear the shared data cache.
     * @return a cache result.
     */
    public CacheResult clear();
}
