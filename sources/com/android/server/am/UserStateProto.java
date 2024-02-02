package com.android.server.am;
/* loaded from: classes3.dex */
public final class UserStateProto {
    private protected static final long STATE = 1159641169921L;
    private protected static final int STATE_BOOTING = 0;
    private protected static final int STATE_RUNNING_LOCKED = 1;
    private protected static final int STATE_RUNNING_UNLOCKED = 3;
    private protected static final int STATE_RUNNING_UNLOCKING = 2;
    private protected static final int STATE_SHUTDOWN = 5;
    private protected static final int STATE_STOPPING = 4;
    private protected static final long SWITCHING = 1133871366146L;

    private protected synchronized UserStateProto() {
    }
}
