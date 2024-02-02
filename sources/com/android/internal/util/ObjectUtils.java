package com.android.internal.util;
/* loaded from: classes3.dex */
public class ObjectUtils {
    private ObjectUtils() {
    }

    public static <T> T firstNotNull(T a, T b) {
        return a != null ? a : (T) Preconditions.checkNotNull(b);
    }

    public static <T extends Comparable> int compare(T a, T b) {
        if (a == null) {
            return b != null ? -1 : 0;
        } else if (b != null) {
            return a.compareTo(b);
        } else {
            return 1;
        }
    }
}
