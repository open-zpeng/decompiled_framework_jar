package com.google.android.collect;

import android.annotation.UnsupportedAppUsage;
import android.util.ArraySet;
import java.util.Collections;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

/* loaded from: classes3.dex */
public class Sets {
    @UnsupportedAppUsage
    public static <K> HashSet<K> newHashSet() {
        return new HashSet<>();
    }

    @UnsupportedAppUsage
    public static <E> HashSet<E> newHashSet(E... elements) {
        int capacity = ((elements.length * 4) / 3) + 1;
        HashSet<E> set = new HashSet<>(capacity);
        Collections.addAll(set, elements);
        return set;
    }

    @UnsupportedAppUsage
    public static <E> SortedSet<E> newSortedSet() {
        return new TreeSet();
    }

    public static <E> SortedSet<E> newSortedSet(E... elements) {
        SortedSet<E> set = new TreeSet<>();
        Collections.addAll(set, elements);
        return set;
    }

    @UnsupportedAppUsage
    public static <E> ArraySet<E> newArraySet() {
        return new ArraySet<>();
    }

    @UnsupportedAppUsage
    public static <E> ArraySet<E> newArraySet(E... elements) {
        int capacity = ((elements.length * 4) / 3) + 1;
        ArraySet<E> set = new ArraySet<>(capacity);
        Collections.addAll(set, elements);
        return set;
    }
}
