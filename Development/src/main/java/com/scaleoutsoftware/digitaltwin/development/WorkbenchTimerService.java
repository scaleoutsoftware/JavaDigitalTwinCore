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
package com.scaleoutsoftware.digitaltwin.development;

import com.scaleoutsoftware.digitaltwin.abstractions.*;

import java.time.Duration;

class WorkbenchTimerService {

    static <T extends DigitalTwinBase> TimerActionResult startTimer(TwinExecutionEngine twinExecutionEngine, TwinProxy proxy, String model, String id, String timerName, Duration interval, TimerType timerType, TimerHandler<T> timerHandler) {
        if(timerName == null || timerName.isEmpty() || interval == null ||
                interval.isZero() || interval.isNegative() || timerType == null || timerHandler == null) {
            String msg = String.format("Empty, blank, zero, or null parameter provided: timerName %s interval %s timerType %s timerHandler %s",
                    timerName, interval, timerType, timerHandler);
            throw new IllegalArgumentException(msg);

        }
        if (proxy.getTimerHandlers().size() >= Constants.MAX_TIMER_COUNT) // all timer slots are occupied
            return TimerActionResult.FailedTooManyTimers;

        if(proxy.getTimerHandlers().containsKey(timerName)) return TimerActionResult.FailedTimerAlreadyExists;

        int timerId = -1;

        boolean[] taken = new boolean[Constants.MAX_TIMER_COUNT];
        // List of all timer Ids
        for (TimerMetadata md : proxy.getTimerHandlers().values()) {
            taken[md.getTimerId()] = true;
        }

        for(int i = 0; i < taken.length; i++) {
            if(!taken[i]) {
                timerId = i;
                break;
            }
        }

        twinExecutionEngine.addTimer(model, id, timerName, timerType, interval, timerHandler);

        TimerMetadata<T> metadata = new TimerMetadata<>(timerHandler, timerType, interval.toMillis(), timerId);
        proxy.getTimerHandlers().put(timerName, metadata);
        return TimerActionResult.Success;
    }

    static <T extends DigitalTwinBase> TimerActionResult stopTimer(TwinExecutionEngine twinExecutionEngine, TwinProxy proxy, String model, String id, String timerName) {
        twinExecutionEngine.stopTimer(model, id, timerName);
        proxy.getTimerHandlers().remove(timerName);
        return TimerActionResult.Success;
    }

}
