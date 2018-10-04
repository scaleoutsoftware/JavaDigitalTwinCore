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

/**
 * The MessageList stores incoming messages that have been processed in the ExecutionEnvironment.
 * @param <V> the message value to store in this message list
 */
public interface MessageList<V> {
    /**
     * Adds a message to this MessageList
     * @param selector the field selector used for ordering.
     * @param message the typed message to add
     */
    void addMessage(FieldSelector<V> selector, V message);

    /**
     * Sets the maximum size of the list
     * @param listSize the size of the MessageList.
     */
    void setMaxSize(int listSize);

    /**
     * Returns a time ordered collection of session windows (where a session window contains elements beginning from the parameter
     * start time and continuing until the time between elements exceeds the timeout) from this message list.
     * @param selector the field selector used for ordering
     * @param start the start time
     * @param timeout the timeout between elements in the collection before a new window is created
     * @param <W> an iterable window of V
     * @param <I> an iterable of windows
     * @return an iterable of iterables, where each inner iterable is a session window bounded by the parameter start and timeout
     */
    <W extends Iterable<V>, I extends Iterable<W>> I toSessionWindows(FieldSelector<V> selector, long start, long timeout);

    /**
     * Returns a time ordered collection of tumbling windows (where a tumbling window contains elements beginning from
     * the paremeter start time and continuing until the parameter duration time is reached) from this message list.
     * @param selector the field selector used for ordering
     * @param start the start time
     * @param duration the duration of a window
     * @param <W> an iterable window of V
     * @param <I> an iterable of windows
     * @return an iterable of iterables, where each inner iterable is a session window bounded by the parameter start and duration
     */
    <W extends Iterable<V>, I extends Iterable<W>> I toTumblingWindows(FieldSelector<V> selector, long start, long duration);

    /**
     * Returns a time ordered collection of sliding windows (where a sliding window contains elements beginning from the
     * parameter start time and continuing until the parameter duration time is reached) where each window can overlap
     * if the parameter value every is smaller than the parameter value for duration.
     * @param selector the field selector used for ordering
     * @param start the start time
     * @param duration the duration of the window
     * @param every new windows occur after the following elapsed amount of time
     * @param <W> an iterable window of V
     * @param <I> an iterable of windows
     * @return an iterable of iterables, where each inner iterable is a sliding window bounded by the paramter start, duration, and occuring every.
     */
    <W extends Iterable<V>, I extends Iterable<W>> I toSlidingWindows(FieldSelector<V> selector, long start, long duration, long every);
}
