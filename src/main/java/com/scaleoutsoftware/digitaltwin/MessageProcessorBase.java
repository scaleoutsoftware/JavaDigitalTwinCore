package com.scaleoutsoftware.digitaltwin;

public abstract class MessageProcessorBase<T extends DigitalTwin> {
    abstract ProcessingResult processMessages(T twin, MessageListFactory messageListFactory) throws Exception;
}
