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

public enum TimerActionResult {
    /// <summary>The operation completed successfully.</summary>
    Success(0),

    /// <summary>Failed to start a new timer due to reaching the limit for a number of active timers.</summary>
    FailedTooManyTimers(1),

    /// <summary>Failed to stop the existing timer, the timer is no longer active.</summary>
    FailedNoSuchTimer(2),

    /// <summary>Failed to start the timer, the timer with the specified name already exists.</summary>
    FailedTimerAlreadyExists(3),

    /// <summary>Failed to start/stop timer due to an internal error.</summary>
    FailedInternalError(4);

    private final int _value;
    private TimerActionResult(int val) {
        _value = val;
    }

    public static TimerActionResult fromOrdinal(int val) {
        switch (val) {
            case 0:
                return Success;
            case 1:
                return FailedTooManyTimers;
            case 2:
                return FailedNoSuchTimer;
            case 3:
                return FailedInternalError;
            default:
                throw new IllegalArgumentException("No known TimerActionResult from value: " + val);

        }
    }
}
