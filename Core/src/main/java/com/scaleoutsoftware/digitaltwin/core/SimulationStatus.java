package com.scaleoutsoftware.digitaltwin.core;

/**
 * The status of a simulation.
 */
public enum SimulationStatus {
    /**
     * The simulation is running.
     */
    Running,
    /**
     * The simulation status is not set.
     */
    NotSet,
    /**
     * The user requested a stop.
     */
    UserRequested,
    /**
     * The simulation end time has been reached.
     */
    EndTimeReached,
    /**
     * There is no remaining work for the simulation.
     */
    NoRemainingWork,
    /**
     * A digital twin instance has requested the simulation to stop by calling {@link SimulationController#stopSimulation()}
     */
    InstanceRequestedStop,
    /**
     * There was a runtime-change of simulation configuration.
     */
    UnexpectedChangeInConfiguration;
}
