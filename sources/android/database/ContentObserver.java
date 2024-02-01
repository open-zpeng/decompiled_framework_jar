package android.database;

import android.database.IContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.UserHandle;
/* loaded from: classes.dex */
public abstract class ContentObserver {
    Handler mHandler;
    private final Object mLock = new Object();
    private Transport mTransport;

    public ContentObserver(Handler handler) {
        this.mHandler = handler;
    }

    public synchronized IContentObserver getContentObserver() {
        Transport transport;
        synchronized (this.mLock) {
            if (this.mTransport == null) {
                this.mTransport = new Transport(this);
            }
            transport = this.mTransport;
        }
        return transport;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IContentObserver releaseContentObserver() {
        Transport oldTransport;
        synchronized (this.mLock) {
            oldTransport = this.mTransport;
            if (oldTransport != null) {
                oldTransport.releaseContentObserver();
                this.mTransport = null;
            }
        }
        return oldTransport;
    }

    public boolean deliverSelfNotifications() {
        return false;
    }

    public void onChange(boolean selfChange) {
    }

    public void onChange(boolean selfChange, Uri uri) {
        onChange(selfChange);
    }

    public synchronized void onChange(boolean selfChange, Uri uri, int userId) {
        onChange(selfChange, uri);
    }

    @Deprecated
    public final void dispatchChange(boolean selfChange) {
        dispatchChange(selfChange, null);
    }

    public final void dispatchChange(boolean selfChange, Uri uri) {
        dispatchChange(selfChange, uri, UserHandle.getCallingUserId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dispatchChange(boolean selfChange, Uri uri, int userId) {
        if (this.mHandler == null) {
            onChange(selfChange, uri, userId);
        } else {
            this.mHandler.post(new NotificationRunnable(selfChange, uri, userId));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class NotificationRunnable implements Runnable {
        private final boolean mSelfChange;
        private final Uri mUri;
        private final int mUserId;

        public NotificationRunnable(boolean selfChange, Uri uri, int userId) {
            this.mSelfChange = selfChange;
            this.mUri = uri;
            this.mUserId = userId;
        }

        @Override // java.lang.Runnable
        public void run() {
            ContentObserver.this.onChange(this.mSelfChange, this.mUri, this.mUserId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class Transport extends IContentObserver.Stub {
        private ContentObserver mContentObserver;

        public synchronized Transport(ContentObserver contentObserver) {
            this.mContentObserver = contentObserver;
        }

        public synchronized void onChange(boolean selfChange, Uri uri, int userId) {
            ContentObserver contentObserver = this.mContentObserver;
            if (contentObserver != null) {
                contentObserver.dispatchChange(selfChange, uri, userId);
            }
        }

        public synchronized void releaseContentObserver() {
            this.mContentObserver = null;
        }
    }
}
