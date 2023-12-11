package com.scaleoutsoftware.digitaltwin.core;

import java.time.Duration;

/**
 * The InitContext is passed as a parameter to the {@link DigitalTwinBase#init(InitContext)} method of an initializing
 * digital twin.
 */
public abstract class InitContext {
    /**
     * Starts a new timer for the digital twin
     * @param timerName the timer name
     * @param interval the timer interval
     * @param timerType the timer type
     * @param timerHandler the time handler callback
     * @param <T> the type of the digital twin
     * @return returns {@link TimerActionResult#Success} if the timer was started, {@link TimerActionResult#FailedTooManyTimers}
     * if too many timers exist, or {@link TimerActionResult#FailedInternalError} if an unexpected error occurs.
     */
    public abstract <T extends DigitalTwinBase> TimerActionResult startTimer(String timerName, Duration interval, TimerType timerType, TimerHandler<T> timerHandler);

    /**
     * Get the model-unique Id identifier of the initializing digital twin instance.
     * @return the id identifier.
     */
    public abstract String getId();

    /**
     * Get the Model identifier of the initializing digital twin instance.
     * @return the model identifier.
     */
    public abstract String getModel();

}
