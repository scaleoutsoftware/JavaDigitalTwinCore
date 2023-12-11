/*
 Copyright (c) 2023 by ScaleOut Software, Inc.

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

import com.scaleoutsoftware.digitaltwin.core.*;

import java.time.Duration;

class WorkbenchTimerService {

    static <T extends DigitalTwinBase> TimerActionResult startTimer(TwinExecutionEngine twinExecutionEngine, T instance, String model, String id, String timerName, Duration interval, TimerType timerType, TimerHandler<T> timerHandler) {
        if(timerName == null || timerName.isBlank() || timerName.isEmpty() || interval == null ||
                interval.isZero() || interval.isNegative() || timerType == null || timerHandler == null) {
            String msg = String.format("Empty, blank, zero, or null parameter provided: timerName %s interval %s timerType %s timerHandler %s",
                    timerName, interval, timerType, timerHandler);
            throw new IllegalArgumentException(msg);

        }
        if (instance.TimerHandlers.size() >= Constants.MAX_TIMER_COUNT) // all timer slots are occupied
            return TimerActionResult.FailedTooManyTimers;

        if(instance.TimerHandlers.containsKey(timerName)) return TimerActionResult.FailedTimerAlreadyExists;

        int timerId = -1;

        boolean[] taken = new boolean[Constants.MAX_TIMER_COUNT];
        // List of all timer Ids
        for (TimerMetadata md : instance.TimerHandlers.values()) {
            taken[md.getTimerId()] = true;
        }

        for(int i = 0; i < taken.length; i++) {
            if(!taken[i]) {
                timerId = i;
                break;
            }
        }

        twinExecutionEngine.addTimer(model, id, timerName, timerType, interval, timerHandler);

        TimerMetadata<T> metadata = new TimerMetadata<>(timerHandler, timerType, interval, timerId);
        instance.TimerHandlers.put(timerName, metadata);
        return TimerActionResult.Success;
    }

    static <T extends DigitalTwinBase> TimerActionResult stopTimer(TwinExecutionEngine twinExecutionEngine, T instance, String model, String id, String timerName) {
        twinExecutionEngine.stopTimer(model, id, timerName);
        instance.TimerHandlers.remove(timerName);
        return TimerActionResult.Success;
    }

}
