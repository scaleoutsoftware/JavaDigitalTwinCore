package com.scaleoutsoftware.digitaltwin;

public abstract class MessageProcessorBase<T extends DigitalTwin> {
    public abstract ProcessingResult processMessages(T twin, MessageListFactory messageListFactory) throws Exception;
}
