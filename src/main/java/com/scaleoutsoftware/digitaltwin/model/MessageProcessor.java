package com.scaleoutsoftware.digitaltwin.model;

import java.io.Serializable;

public interface MessageProcessor<V extends DigitalTwin> extends Serializable {
    ProcessingResult processMessage(V stateObject, Message toProcess) throws Exception;
}
