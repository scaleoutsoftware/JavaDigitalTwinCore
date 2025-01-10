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
 * A message sent to a digital twin instance's message processor.
 */
public class DigitalTwinTimerMessage {

    private String      _modelName;
    private String      _twinId;
    private int         _timerId;
    private String      _timerName;
    private TimerType   _timerType;

    /**
     * Construct a digital twin timer message.
     * @param modelName the digital twin model name.
     * @param twinId the digital twin instance ID
     * @param timerId the timer ID
     * @param timerName the timer name
     * @param timerType the timer type
     */
    public DigitalTwinTimerMessage(String modelName, String twinId, int timerId, String timerName, TimerType timerType) {
        _modelName  = modelName;
        _twinId     = twinId;
        _timerId    = timerId;
        _timerName  = timerName;
        _timerType  = timerType;
    }

    /**
     * Retrieve the digital twin model name.
     * @return the digital twin model name.
     */
    public String getModelName() {
        return _modelName;
    }

    /**
     * Retrieve the digital twin ID.
     * @return the digital twin ID.
     */
    public String getTwinId() {
        return _twinId;
    }

    /**
     * Retrieve the timer ID.
     * @return the timer ID.
     */
    public int getTimerId() {
        return _timerId;
    }

    /**
     * Retrieve the timer name.
     * @return the timer name.
     */
    public String getTimerName() {
        return _timerName;
    }

    /**
     * Retrieve the {@link TimerType}.
     * @return the {@link TimerType}
     */
    public TimerType getTimerType() {
        return _timerType;
    }
}
