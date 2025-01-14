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
 * Marks a message as Delivered or not Delivered
 */
public enum SendingResult {
    /**
     * Handled indicates that a message was successfully sent and processed
     */
    Handled,
    /**
     * Enqueued indicates that a message was successfully formed and then sent to an internal messaging service
     */
    Enqueued,
    /**
     * NotHandled indicates that the message was not handled. This can occur if an exception occurs
     * in the message processor or if internal messaging service reached capacity.
     */
    NotHandled
}
