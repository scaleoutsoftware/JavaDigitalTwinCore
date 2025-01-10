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
 * Status of a cache operation.
 */
public enum CacheOperationStatus {

    /**
     * The object was successfully retrieved.
     */

    ObjectRetrieved,

    /**
     * The object was successfully added/updated.
     */
    ObjectPut,

    /**
     * The object could not be retrieved because it was not found.
     */
    ObjectDoesNotExist,

    /**
     * The object was removed successfully.
     */
    ObjectRemoved,

    /**
     * The cache was cleared successfully.
     */
    CacheCleared
}
