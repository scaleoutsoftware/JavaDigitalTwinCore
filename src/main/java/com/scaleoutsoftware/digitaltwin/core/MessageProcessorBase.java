package com.scaleoutsoftware.digitaltwin.core;

public abstract class MessageProcessorBase<T extends DigitalTwin> {
    public abstract ProcessingResult processMessages(ProcessingContext context, T twin, MessageListFactory messageListFactory) throws Exception;
}
