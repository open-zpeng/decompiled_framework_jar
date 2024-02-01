package com.android.internal.util;

import android.annotation.UnsupportedAppUsage;
import android.os.Message;

/* loaded from: classes3.dex */
public class State implements IState {
    @Override // com.android.internal.util.IState
    @UnsupportedAppUsage
    public void enter() {
    }

    @Override // com.android.internal.util.IState
    @UnsupportedAppUsage
    public void exit() {
    }

    @Override // com.android.internal.util.IState
    @UnsupportedAppUsage
    public boolean processMessage(Message msg) {
        return false;
    }

    @Override // com.android.internal.util.IState
    @UnsupportedAppUsage
    public String getName() {
        String name = getClass().getName();
        int lastDollar = name.lastIndexOf(36);
        return name.substring(lastDollar + 1);
    }
}
