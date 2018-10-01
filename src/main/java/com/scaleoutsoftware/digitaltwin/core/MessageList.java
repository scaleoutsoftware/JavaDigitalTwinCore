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

import java.util.List;

public interface MessageList<V> {
    void addMessage(FieldSelector<V> selector, V message);
    List<V> getMessageList();
    void setListSize(int listSize);
    <W extends Iterable<V>, I extends Iterable<W>> I toSessionWindows(FieldSelector<V> selector, long start, long timeout);
    <W extends Iterable<V>, I extends Iterable<W>> I toTumblingWindows(FieldSelector<V> selector, long start, long duration);
    <W extends Iterable<V>, I extends Iterable<W>> I toSlidingWindows(FieldSelector<V> selector, long start, long duration, long every);
}
