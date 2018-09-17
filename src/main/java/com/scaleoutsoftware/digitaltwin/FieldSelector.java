package com.scaleoutsoftware.digitaltwin;

public interface FieldSelector<V> {
    long select(V value);
}
