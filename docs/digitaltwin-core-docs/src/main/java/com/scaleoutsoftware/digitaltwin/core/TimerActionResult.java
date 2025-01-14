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
 * The result of a timer action.
 */
public enum TimerActionResult {
    /**
     * The operation completed successfully.
     */
    Success(0),

    /**
     * Failed to start a new timer due to reaching the limit for a number of active timers.
     */
    FailedTooManyTimers(1),

    /**
     * Failed to stop the existing timer, the timer is no longer active.
     */
    FailedNoSuchTimer(2),

    /**
     * Failed to start the timer, the timer with the specified name already exists.
     */
    FailedTimerAlreadyExists(3),

    /**
     * Failed to start/stop timer due to an internal error.
     */
    FailedInternalError(4);

    private final int _value;
    private TimerActionResult(int val) {
        _value = val;
    }

    /**
     * Convert an ordinal into a {@link TimerActionResult}.
     *
     * 0 = {@link TimerActionResult#Success}, 1 = {@link TimerActionResult#FailedTooManyTimers},
     * 2 = {@link TimerActionResult#FailedNoSuchTimer}, 3 = {@link TimerActionResult#FailedTimerAlreadyExists},
     * 4 = {@link TimerActionResult#FailedInternalError}
     * @param val the ordinal value.
     * @return the associated {@link TimerActionResult} or throws an IllegalArgumentException for an unexpected
     * ordinal value.
     */
    public static TimerActionResult fromOrdinal(int val) {
        switch (val) {
            case 0:
                return Success;
            case 1:
                return FailedTooManyTimers;
            case 2:
                return FailedNoSuchTimer;
            case 3:
                return FailedTimerAlreadyExists;
            case 4:
                return FailedInternalError;
            default:
                throw new IllegalArgumentException("No known TimerActionResult from value: " + val);

        }
    }
}
