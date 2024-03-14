package com.scaleoutsoftware.digitaltwin.core;

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
