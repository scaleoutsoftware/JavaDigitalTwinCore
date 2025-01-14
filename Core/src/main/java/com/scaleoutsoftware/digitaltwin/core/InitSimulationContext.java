package com.scaleoutsoftware.digitaltwin.core;

import java.util.Date;

/**
 * The InitSimulationContext is passed as a parameter to the {@link SimulationProcessor#onInitSimulation(InitSimulationContext, DigitalTwinBase, Date)} method of
 * digital twin instance when a simulation is initializing.
 */
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
