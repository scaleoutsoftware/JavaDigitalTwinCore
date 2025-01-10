/*
 Copyright (c) 2025 by ScaleOut Software, Inc.

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
