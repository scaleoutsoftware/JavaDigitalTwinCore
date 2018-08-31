package com.scaleoutsoftware.digitaltwin.model;

import java.util.ArrayList;
import java.util.List;

public abstract class DigitalTwin {
    protected ArrayList messageList;
    protected String id;
    protected String digitalTwinType;

    public DigitalTwin() {
    }

    public abstract void init(String id, String digitalTwinType);
}
