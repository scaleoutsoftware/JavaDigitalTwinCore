/*
 Copyright (c) 2018 by ScaleOut Software, Inc.

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
 * Base class for the MessageProcessor to help with typing.
 * @param <T> the type of the DigitalTwin
 */
public abstract class MessageProcessorBase<T extends DigitalTwinBase> {

    /**
     * Default constructor.
     */
    public MessageProcessorBase() {}

    /**
     * Helper method to ensure proper typing for the user methods.
     * @param context the processing context
     * @param twin the real-time digital twin instance
     * @param messageListFactory the message list factory
     * @return the implementing class's processing result
     * @throws Exception if an exception occurs during processing
     */
    public abstract ProcessingResult processMessages(ProcessingContext context, T twin, MessageFactory messageListFactory) throws Exception;
}
