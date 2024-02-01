package com.android.internal.widget;

import android.annotation.UnsupportedAppUsage;

/* loaded from: classes3.dex */
public class ScrollBarUtils {
    @UnsupportedAppUsage
    public static int getThumbLength(int size, int thickness, int extent, int range) {
        int minLength = thickness * 2;
        int length = Math.round((size * extent) / range);
        if (length < minLength) {
            return minLength;
        }
        return length;
    }

    public static int getThumbOffset(int size, int thumbLength, int extent, int range, int offset) {
        int thumbOffset = Math.round(((size - thumbLength) * offset) / (range - extent));
        if (thumbOffset > size - thumbLength) {
            return size - thumbLength;
        }
        return thumbOffset;
    }
}
