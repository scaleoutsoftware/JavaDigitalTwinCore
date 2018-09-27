package com.scaleoutsoftware.digitaltwin.core;

public interface FieldSelector<V> {
    long select(V value);
}
