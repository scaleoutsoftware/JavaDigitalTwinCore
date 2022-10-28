/*
 Copyright (c) 2022 by ScaleOut Software, Inc.

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

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;

/**
 * Metadata class for a timer.
 * @param <T> the type of the {@link DigitalTwinBase} implementation.
 */
public class TimerMetadata<T extends DigitalTwinBase> {
    public String      timerHandler;
    public TimerType   timerType;
    public Duration    timerInterval;
    public int         timerId;

    /**
     * Constructs a timer metadata.
     * @param handler the timer handler.
     * @param timerType the timer type.
     * @param timerInterval the timer interval.
     * @param timerIdx the timer index.
     */
    public TimerMetadata(TimerHandler<T> handler, TimerType timerType, Duration timerInterval, int timerIdx) {
        this.timerHandler   = handler.getClass().getName();
        this.timerType      = timerType;
        this.timerInterval  = timerInterval;
        this.timerId        = timerIdx;
    }

    /**
     * Retrieves the timer handler class name.
     * @return the timer handler class name.
     */
    public String getTimerHandlerClass() {
        return timerHandler;
    }

    /**
     * Retrieves the timer type.
     * @return the timer type.
     */
    public TimerType getTimerType() {
        return timerType;
    }

    /**
     * Retrieves the timer interval.
     * @return the timer interval.
     */
    public Duration getTimerInterval() {
        return timerInterval;
    }

    /**
     * Retrieves the timer ID.
     * @return the timer ID.
     */
    public int getTimerId() {
        return timerId;
    }
}
