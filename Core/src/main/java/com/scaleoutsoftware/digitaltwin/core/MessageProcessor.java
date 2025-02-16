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

import java.io.Serializable;

/**
 * Processes messages for a real-time digital twin.
 * @param <T> the real type of the DigitalTwinBase
 * @param <V> the type of messages processed by the real-time digital twin
 */
public abstract class MessageProcessor<T extends DigitalTwinBase, V> extends MessageProcessorBase<T> implements Serializable {

    /**
     * Default constructor.
     */
    public MessageProcessor() {}

    /**
     * Processes a set of incoming messages and determines whether to update the real-time digital twin.
     * @param context optional context for processing.
     * @param stateObject the state object.
     * @param incomingMessages the incoming messages.
     * @return processing results for updating the state object.
     * @throws Exception if an exception occurs during processing
     */
    public abstract ProcessingResult processMessages(ProcessingContext context, T stateObject, Iterable<V> incomingMessages) throws Exception;

    /**
     * Helper method to ensure proper typing for user methods.
     * @param context the processing context.
     * @param twin the digital twin object.
     * @param factory the message list factory.
     * @return the implementing class's processing result.
     * @throws Exception if an exception occurs during processing.
     */
    @Override
    public ProcessingResult processMessages(ProcessingContext context, T twin, MessageFactory factory) throws Exception {
        Iterable<V> incoming = factory.getIncomingMessages();
        return this.processMessages(context, twin, incoming);
    }
}

