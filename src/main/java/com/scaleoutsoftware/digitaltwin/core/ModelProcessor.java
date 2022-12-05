package com.scaleoutsoftware.digitaltwin.core;

import java.io.Serializable;

/**
 * Processes simulation events for a real-time digital twin.
 * @param <T> the type of the digital twin.
 */
public abstract class ModelProcessor<T extends DigitalTwinBase> implements Serializable {

    /**
     * Processes simulation events for a real-time digital twin.
     * @param context the processing context.
     * @param instance the digital twin instance.
     * @param epoch the current time interval of the simulation.
     * @return {@link ProcessingResult#UpdateDigitalTwin} to update the digital twin, or
     * {@link ProcessingResult#NoUpdate} to ignore the changes.
     */
    public abstract ProcessingResult processModel(ProcessingContext context, T instance, long epoch);
}
