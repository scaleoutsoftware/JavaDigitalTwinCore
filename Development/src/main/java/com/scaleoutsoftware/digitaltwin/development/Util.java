package com.scaleoutsoftware.digitaltwin.development;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Util {
    public static <T> List<T> copyOf(Collection<? extends T> source) {
        if (source == null) {
            throw new NullPointerException("source");
        }

        List<T> copy = new ArrayList<>(source.size());

        for (T item : source) {
            if (item == null) {
                throw new NullPointerException("List contains null");
            }
            copy.add(item);
        }

        return Collections.unmodifiableList(copy);
    }
}
