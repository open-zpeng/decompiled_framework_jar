package com.android.internal.util;

import android.annotation.UnsupportedAppUsage;
import android.os.Message;

/* loaded from: classes3.dex */
public interface IState {
    public static final boolean HANDLED = true;
    public static final boolean NOT_HANDLED = false;

    void enter();

    void exit();

    @UnsupportedAppUsage
    String getName();

    boolean processMessage(Message message);
}
