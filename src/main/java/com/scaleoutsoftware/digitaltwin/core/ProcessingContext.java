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

import java.io.Serializable;

/**
 * Context object that allows the user to send a message to a DataSource.
 */
public abstract class ProcessingContext implements Serializable {

    /**
     * Sends a message to a data source
     * @param payload the message (as a serialized JSON string)
     * @return the sending result
     */
    public abstract SendingResult sendToDataSource(byte[] payload);

    /**
     * Retrieve the unique Identier for a DigitalTwin (matches the Device/DigitalTwin ID)
     * @return the digital twin id
     */
    public abstract String getDataSourceId();

    /**
     * Retrieve the model for a DigitalTwin (matches the model of a Device/DigitalTwin)
     * @return the digital twin model
     */
    public abstract String getDataSourceModel();
}
