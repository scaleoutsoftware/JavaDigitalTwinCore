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
 * Retrieves a long (typically a timestamp) for ordering in the MessageList.
 * @param <V> the value of the object stored in the MessageList.
 */
public interface FieldSelector<V> {
    /**
     * Selects a long from the instance of the parameter value.
     * @param value the long to return for ordering
     * @return the long value for ordering
     */
    long select(V value);
}
