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

import java.time.Duration;

/**
 * The InitContext is passed as a parameter to the {@link DigitalTwinBase#init(InitContext)} method of an initializing
 * digital twin.
 */
public abstract class InitContext {

    /**
     * Default constructor.
     */
    public InitContext() {}

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
