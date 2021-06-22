/*
 Copyright (c) 2021 by ScaleOut Software, Inc.

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

public class AlertMessage {
    private final String                    _title;
    private final String                    _severity;
    private final String                    _message;
    private final HashMap<String,String>    _optionalTwinInstanceProperties;

    private AlertMessage() {_title = _severity = _message = null; _optionalTwinInstanceProperties = null;}

    public AlertMessage(String title, String severity, String message) {
        _title                              = title;
        _severity                           = severity;
        _message                            = message;
        _optionalTwinInstanceProperties     = null;
    }

    public AlertMessage(String title, String severity, String message, HashMap<String,String> optProps) {
        _title                              = title;
        _severity                           = severity;
        _message                            = message;
        _optionalTwinInstanceProperties     = optProps;
    }

    public String getTitle() {
        return _title;
    }

    public String getSeverity() {
        return _severity;
    }

    public String getMessage() {
        return _message;
    }

    public HashMap<String, String> getOptionalTwinInstanceProperties() {
        return _optionalTwinInstanceProperties;
    }
}
