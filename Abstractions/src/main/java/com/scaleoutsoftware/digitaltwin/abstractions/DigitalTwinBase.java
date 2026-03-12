/*
 Copyright (c) 2026 by ScaleOut Software, Inc.

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

import java.util.Date;
import java.util.HashMap;

/**
 * A real-time digital twin of a data source. The implementation of the real-time DigitalTwin should have a parameterless constructor for
 * basic initialization.
 */
public abstract class DigitalTwinBase<T extends DigitalTwinBase<T>> {

    /* capitalized to match .NET serialization */
    /**
     * The identifier for this twin instance
     */
    public String Id = "";

    /**
     * The model this twin instance belongs to.
     */
    public String Model = "";

    /**
     * Default constructor.
     */
    public DigitalTwinBase() {}

    /**
     * The identifier of this DigitalTwin.
     * @return the identifier of this digital twin
     */
    public String getId() {
        return Id;
    }

    /**
     * The model for this DigitalTwin.
     * @return the model for this DigitalTwin
     */
    public String getModel() {
        return Model;
    }

    /**
     * Initialization method to set the identifier and model for a DigitalTwin instance. Optionally use the
     * {@link InitContext} to start a timer.
     * @param context the initialization context.
     * @throws IllegalStateException if init is called after initialization.
     */
    public void init(InitContext<T> context) throws IllegalStateException {
        this.Id     = context.getId();
        this.Model  = context.getModel();
    }
}
