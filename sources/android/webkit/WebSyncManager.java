package android.webkit;

import android.content.Context;
import android.os.Handler;
@Deprecated
/* loaded from: classes2.dex */
abstract class WebSyncManager implements Runnable {
    protected static final String LOGTAG = "websync";
    protected WebViewDatabase mDataBase;
    public private Handler mHandler;

    public private protected abstract void syncFromRamToFlash();

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized WebSyncManager(Context context, String name) {
    }

    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("doesn't implement Cloneable");
    }

    @Override // java.lang.Runnable
    public void run() {
    }

    public synchronized void sync() {
    }

    public synchronized void resetSync() {
    }

    public synchronized void startSync() {
    }

    public synchronized void stopSync() {
    }

    protected synchronized void onSyncInit() {
    }
}
