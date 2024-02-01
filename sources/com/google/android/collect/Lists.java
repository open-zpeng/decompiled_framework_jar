package com.google.android.collect;

import android.annotation.UnsupportedAppUsage;
import java.util.ArrayList;
import java.util.Collections;

/* loaded from: classes3.dex */
public class Lists {
    @UnsupportedAppUsage
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<>();
    }

    @UnsupportedAppUsage
    public static <E> ArrayList<E> newArrayList(E... elements) {
        int capacity = ((elements.length * 110) / 100) + 5;
        ArrayList<E> list = new ArrayList<>(capacity);
        Collections.addAll(list, elements);
        return list;
    }
}
