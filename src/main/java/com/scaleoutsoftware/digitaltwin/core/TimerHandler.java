/*
 Copyright (c) 2022 by ScaleOut Software, Inc.

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

public interface TimerHandler<T extends DigitalTwinBase> {
    /**
     * Callback to handle a timer message.
     * @param instance the digital twin instance.
     * @param ctx the processing context.
     * @return {@link ProcessingResult#UpdateDigitalTwin} to update the digital twin instance or
     * {@link ProcessingResult#NoUpdate} to leave the instance state as is.
     */
    public ProcessingResult onTimedMessage(String timerName, T instance, ProcessingContext ctx);
}
