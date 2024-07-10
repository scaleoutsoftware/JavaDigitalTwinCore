/*
 Copyright (c) 2024 by ScaleOut Software, Inc.

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

/**
 * SharedData is used to access a model's, or globally, shared cache.
 */
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
