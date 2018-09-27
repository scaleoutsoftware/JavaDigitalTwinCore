package com.scaleoutsoftware.digitaltwin.core;

import java.io.Serializable;

public abstract class ProcessingContext implements Serializable {
    public abstract SendingResult sendToDataSource(byte[] payload);
}
