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
package com.scaleoutsoftware.digitaltwin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public abstract class MessageProcessor<T extends DigitalTwin, V> extends MessageProcessorBase<T> implements Serializable {
    Logger logger = LoggerFactory.getLogger(MessageProcessor.class);
    public abstract ProcessingResult processMessages(T stateObject, MessageList<V> messageList, Iterable<V> incomingMessages) throws Exception;

    @Override
    protected ProcessingResult processMessages(T twin, MessageListFactory factory) throws Exception {
        logger.info("in process message base");
        MessageList<V> ml = factory.getMessageList();
        logger.info("ml recieved");
        Iterable<V> incoming = factory.getIncomingMessages();
        logger.info("iterable recieved");
        return this.processMessages(twin, ml, incoming);
    }
}

