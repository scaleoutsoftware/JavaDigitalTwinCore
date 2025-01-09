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

import java.util.logging.Level;

/**
 * A messaged that was logged by a digital twin.
 */
public class LogMessage {
    private String  _message;
    private Level   _severity;
    private long    _timestamp;
    LogMessage(Level severity, String message) {
        _message    = message;
        _severity   = severity;
        _timestamp  = System.currentTimeMillis();
    }

    /**
     * Retrieve the string message associated with this log message.
     * @return the message.
     */
    public String getMessage() {
        return _message;
    }

    /**
     * Retrieve the severity of this log message.
     * @return the severity.
     */
    public Level getSeverity() {
        return _severity;
    }

    /**
     * Retrieve the timestamp from when this message was generated.
     * @return the timestamp.
     */
    public long getTimestamp() {
        return _timestamp;
    }
}
