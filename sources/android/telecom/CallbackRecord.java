package android.telecom;

import android.os.Handler;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class CallbackRecord<T> {
    private final T mCallback;
    private final Handler mHandler;

    public synchronized CallbackRecord(T callback, Handler handler) {
        this.mCallback = callback;
        this.mHandler = handler;
    }

    public synchronized T getCallback() {
        return this.mCallback;
    }

    public synchronized Handler getHandler() {
        return this.mHandler;
    }
}
