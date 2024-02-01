package com.android.internal.os;
/* loaded from: classes3.dex */
public class FuseUnavailableMountException extends Exception {
    public synchronized FuseUnavailableMountException(int mountId) {
        super("AppFuse mount point " + mountId + " is unavailable");
    }
}
