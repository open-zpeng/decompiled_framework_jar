package com.android.internal.util;
/* loaded from: classes3.dex */
public final class VirtualRefBasePtr {
    private long mNativePtr;

    private static native void nDecStrong(long j);

    private static native void nIncStrong(long j);

    public synchronized VirtualRefBasePtr(long ptr) {
        this.mNativePtr = ptr;
        nIncStrong(this.mNativePtr);
    }

    public synchronized long get() {
        return this.mNativePtr;
    }

    public synchronized void release() {
        if (this.mNativePtr != 0) {
            nDecStrong(this.mNativePtr);
            this.mNativePtr = 0L;
        }
    }

    protected void finalize() throws Throwable {
        try {
            release();
        } finally {
            super.finalize();
        }
    }
}
