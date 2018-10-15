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
 * A "DigitalTwin" of a device. The implementation of the DigitalTwin should have a parameterless constructor for
 * basic initialization.
 */
public abstract class DigitalTwinBase {

    private String Id;
    private String Model;

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
     * Initialization method to set the identifier and model for a DigitalTwin instance.
     * @param id the id to use for this digital twin
     * @param model the model to use for this digital twin
     */
    public void init(String id, String model) {
        this.Id = id;
        this.Model = model;
    }
}
