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

/**
 * A Workbench exception indicates that a real-time or simulated twin caused an exception.
 */
public class WorkbenchException extends Exception {

    /**
     * The string message for this workbench exception.
     */
    String      _message;
    /**
     * The inner cause of the workbench exception.
     */
    Exception   _innerException;

    /**
     * Instantiates a WorkbenchException with the parameter message and inner exception
     *
     * @param message the message of this exception
     * @param e       the inner exception
     */
    public WorkbenchException(String message, Exception e) {
        _message        = message;
        _innerException = e;
    }

    /**
     * Instantiates a WorkbenchException with the parameter inner exception
     *
     * @param e the inner exception
     */
    public WorkbenchException(Exception e) {
        _innerException = e;
        _message        = e.getMessage();
    }

    /**
     * Instantiates a WorkbenchException with the parameter message
     *
     * @param message the message of this exception
     */
    public WorkbenchException(String message) {
        _message = message;
    }

    @Override
    public String getMessage() {
        return _message;
    }
}
