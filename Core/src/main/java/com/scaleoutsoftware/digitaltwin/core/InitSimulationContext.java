package com.scaleoutsoftware.digitaltwin.core;

public interface InitSimulationContext {
    /**
     * Retrieve a {@link SharedData} accessor for this model's shared data.
     * @return a {@link SharedData} instance.
     */
    public abstract SharedData getSharedModelData();

    /**
     * Retrieve a {@link SharedData} accessor for globally shared data.
     * @return a {@link SharedData} instance.
     */
    public abstract SharedData getSharedGlobalData();
}
