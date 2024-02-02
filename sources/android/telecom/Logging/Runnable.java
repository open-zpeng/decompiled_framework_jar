package android.telecom.Logging;

import android.telecom.Log;
/* loaded from: classes2.dex */
public abstract class Runnable {
    public protected final Object mLock;
    public protected final java.lang.Runnable mRunnable = new java.lang.Runnable() { // from class: android.telecom.Logging.Runnable.1
        @Override // java.lang.Runnable
        public void run() {
            synchronized (Runnable.this.mLock) {
                Log.continueSession(Runnable.this.mSubsession, Runnable.this.mSubsessionName);
                Runnable.this.loggedRun();
                if (Runnable.this.mSubsession != null) {
                    Log.endSession();
                    Runnable.this.mSubsession = null;
                }
            }
        }
    };
    public protected Session mSubsession;
    public protected final String mSubsessionName;

    private protected abstract synchronized void loggedRun();

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized Runnable(String subsessionName, Object lock) {
        if (lock == null) {
            this.mLock = new Object();
        } else {
            this.mLock = lock;
        }
        this.mSubsessionName = subsessionName;
    }

    private protected final synchronized java.lang.Runnable getRunnableToCancel() {
        return this.mRunnable;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized java.lang.Runnable prepare() {
        cancel();
        this.mSubsession = Log.createSubsession();
        return this.mRunnable;
    }

    private protected synchronized void cancel() {
        synchronized (this.mLock) {
            Log.cancelSubsession(this.mSubsession);
            this.mSubsession = null;
        }
    }
}
