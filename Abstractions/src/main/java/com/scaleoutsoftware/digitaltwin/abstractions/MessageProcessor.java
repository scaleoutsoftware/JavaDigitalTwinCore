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
package com.scaleoutsoftware.digitaltwin.abstractions;

import java.io.Serializable;

/**
 * Processes messages for a real-time digital twin.
 * @param <T> the real type of the DigitalTwinBase
 */
public abstract class MessageProcessor<T extends DigitalTwinBase<T>> {

    /**
     * Default constructor.
     */
    public MessageProcessor() {}

    /**
     * Processes a set of incoming messages and determines whether to update the real-time digital twin.
     * @param context optional context for processing.
     * @param stateObject the state object.
     * @param incomingMessage the incoming message.
     * @return processing results for updating the state object.
     * @throws Exception if an exception occurs during processing
     */
    public abstract ProcessingResult processMessage(ProcessingContext<T> context, T stateObject, byte[] incomingMessage) throws Exception;
}

