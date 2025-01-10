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

import java.util.HashMap;

/**
 * A message that should be sent to a configured alert provider.
 */
public class AlertMessage {
    private final String                    _title;
    private final String                    _severity;
    private final String                    _message;
    private final HashMap<String,String>    _optionalTwinInstanceProperties;

    private AlertMessage() {_title = _severity = _message = null; _optionalTwinInstanceProperties = null;}

    /**
     * Construct an alert message with a title, severity, and custom message.
     * @param title the title for the alert message.
     * @param severity the severity for this alert.
     * @param message the custom message for this alert.
     */
    public AlertMessage(String title, String severity, String message) {
        _title                              = title;
        _severity                           = severity;
        _message                            = message;
        _optionalTwinInstanceProperties     = null;
    }

    /**
     * Construct an alert message with a title, severity, and custom message.
     * @param title the title for the alert message.
     * @param severity the severity for this alert.
     * @param message the custom message for this alert.
     * @param optProps the optional properties that should be sent to the alerting provider.
     */
    public AlertMessage(String title, String severity, String message, HashMap<String,String> optProps) {
        _title                              = title;
        _severity                           = severity;
        _message                            = message;
        _optionalTwinInstanceProperties     = optProps;
    }

    /**
     * Retrieve the title for this alert message.
     * @return the title of this alert message.
     */
    public String getTitle() {
        return _title;
    }

    /**
     * Retrieve the severity for this alert message.
     * @return the severity for this alert message.
     */
    public String getSeverity() {
        return _severity;
    }

    /**
     * Retrieve the message for this alert message.
     * @return the message for this alert message.
     */
    public String getMessage() {
        return _message;
    }

    /**
     * Retrieve the optional twin instance properties for this alert message.
     * @return the optional twin instance properties for this alert message.
     */
    public HashMap<String, String> getOptionalTwinInstanceProperties() {
        return _optionalTwinInstanceProperties;
    }

    @Override
    public String toString() {
        return "AlertMessage{" +
                "_title='" + _title + '\'' +
                ", _severity='" + _severity + '\'' +
                ", _message='" + _message + '\'' +
                ", _optionalTwinInstanceProperties=" + _optionalTwinInstanceProperties +
                '}';
    }
}
