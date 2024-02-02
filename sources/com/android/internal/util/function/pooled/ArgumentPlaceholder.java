package com.android.internal.util.function.pooled;
/* loaded from: classes3.dex */
public final class ArgumentPlaceholder<R> {
    static final ArgumentPlaceholder<?> INSTANCE = new ArgumentPlaceholder<>();

    private ArgumentPlaceholder() {
    }

    public String toString() {
        return "_";
    }
}
