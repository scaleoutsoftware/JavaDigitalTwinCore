package com.scaleoutsoftware.digitaltwin;

import java.util.List;

public interface MessageList<V> {
    void addMessage(FieldSelector<V> selector, V message);
    List<V> getMessageList();
    void setListSize(int listSize);
    <W extends Iterable<V>, I extends Iterable<W>> I toSessionWindows(FieldSelector<V> selector, long start, long timeout);
    <W extends Iterable<V>, I extends Iterable<W>> I toTumblingWindows(FieldSelector<V> selector, long start, long duration);
    <W extends Iterable<V>, I extends Iterable<W>> I toSlidingWindows(FieldSelector<V> selector, long start, long duration, long every);
}
