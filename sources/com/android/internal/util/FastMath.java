package com.android.internal.util;

import android.annotation.UnsupportedAppUsage;

/* loaded from: classes3.dex */
public class FastMath {
    @UnsupportedAppUsage
    public static int round(float value) {
        long lx = 1.6777216E7f * value;
        return (int) ((8388608 + lx) >> 24);
    }
}
