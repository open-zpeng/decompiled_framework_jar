package android.os;

import android.annotation.UnsupportedAppUsage;
import android.util.Printer;
import android.util.SparseArray;
import android.util.proto.ProtoOutputStream;
import java.io.FileDescriptor;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public final class MessageQueue {
    private static final boolean DEBUG = false;
    private static final int RETRY_TIME = 200;
    private static final String TAG = "MessageQueue";
    private boolean mBlocked;
    private SparseArray<FileDescriptorRecord> mFileDescriptorRecords;
    @UnsupportedAppUsage
    Message mMessages;
    @UnsupportedAppUsage
    private int mNextBarrierToken;
    private IdleHandler[] mPendingIdleHandlers;
    @UnsupportedAppUsage
    private final boolean mQuitAllowed;
    private boolean mQuitting;
    private int retryCount = 0;
    private boolean isAPPLooper = false;
    @UnsupportedAppUsage
    private final ArrayList<IdleHandler> mIdleHandlers = new ArrayList<>();
    @UnsupportedAppUsage
    private long mPtr = nativeInit();

    /* loaded from: classes2.dex */
    public interface IdleHandler {
        boolean queueIdle();
    }

    /* loaded from: classes2.dex */
    public interface OnFileDescriptorEventListener {
        public static final int EVENT_ERROR = 4;
        public static final int EVENT_INPUT = 1;
        public static final int EVENT_OUTPUT = 2;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes2.dex */
        public @interface Events {
        }

        int onFileDescriptorEvents(FileDescriptor fileDescriptor, int i);
    }

    private static native void nativeDestroy(long j);

    private static native long nativeInit();

    private static native boolean nativeIsPolling(long j);

    @UnsupportedAppUsage
    private native void nativePollOnce(long j, int i);

    private static native void nativeSetFileDescriptorEvents(long j, int i, int i2);

    private static native void nativeWake(long j);

    /* JADX INFO: Access modifiers changed from: package-private */
    public MessageQueue(boolean quitAllowed) {
        this.mQuitAllowed = quitAllowed;
    }

    protected void finalize() throws Throwable {
        try {
            dispose();
        } finally {
            super.finalize();
        }
    }

    private void dispose() {
        long j = this.mPtr;
        if (j != 0) {
            nativeDestroy(j);
            this.mPtr = 0L;
        }
    }

    public boolean isIdle() {
        boolean z;
        synchronized (this) {
            long now = SystemClock.uptimeMillis();
            z = this.mMessages == null || now < this.mMessages.when;
        }
        return z;
    }

    public void addIdleHandler(IdleHandler handler) {
        if (handler == null) {
            throw new NullPointerException("Can't add a null IdleHandler");
        }
        synchronized (this) {
            this.mIdleHandlers.add(handler);
        }
    }

    public void removeIdleHandler(IdleHandler handler) {
        synchronized (this) {
            this.mIdleHandlers.remove(handler);
        }
    }

    public boolean isPolling() {
        boolean isPollingLocked;
        synchronized (this) {
            isPollingLocked = isPollingLocked();
        }
        return isPollingLocked;
    }

    private boolean isPollingLocked() {
        return !this.mQuitting && nativeIsPolling(this.mPtr);
    }

    public void addOnFileDescriptorEventListener(FileDescriptor fd, int events, OnFileDescriptorEventListener listener) {
        if (fd == null) {
            throw new IllegalArgumentException("fd must not be null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }
        synchronized (this) {
            updateOnFileDescriptorEventListenerLocked(fd, events, listener);
        }
    }

    public void removeOnFileDescriptorEventListener(FileDescriptor fd) {
        if (fd == null) {
            throw new IllegalArgumentException("fd must not be null");
        }
        synchronized (this) {
            updateOnFileDescriptorEventListenerLocked(fd, 0, null);
        }
    }

    private void updateOnFileDescriptorEventListenerLocked(FileDescriptor fd, int events, OnFileDescriptorEventListener listener) {
        int fdNum = fd.getInt$();
        int index = -1;
        FileDescriptorRecord record = null;
        SparseArray<FileDescriptorRecord> sparseArray = this.mFileDescriptorRecords;
        if (sparseArray != null && (index = sparseArray.indexOfKey(fdNum)) >= 0 && (record = this.mFileDescriptorRecords.valueAt(index)) != null && record.mEvents == events) {
            return;
        }
        if (events != 0) {
            int events2 = events | 4;
            if (record == null) {
                if (this.mFileDescriptorRecords == null) {
                    this.mFileDescriptorRecords = new SparseArray<>();
                }
                this.mFileDescriptorRecords.put(fdNum, new FileDescriptorRecord(fd, events2, listener));
            } else {
                record.mListener = listener;
                record.mEvents = events2;
                record.mSeq++;
            }
            nativeSetFileDescriptorEvents(this.mPtr, fdNum, events2);
        } else if (record != null) {
            record.mEvents = 0;
            this.mFileDescriptorRecords.removeAt(index);
            nativeSetFileDescriptorEvents(this.mPtr, fdNum, 0);
        }
    }

    @UnsupportedAppUsage
    private int dispatchEvents(int fd, int events) {
        synchronized (this) {
            FileDescriptorRecord record = this.mFileDescriptorRecords.get(fd);
            if (record == null) {
                return 0;
            }
            int oldWatchedEvents = record.mEvents;
            int events2 = events & oldWatchedEvents;
            if (events2 == 0) {
                return oldWatchedEvents;
            }
            OnFileDescriptorEventListener listener = record.mListener;
            int seq = record.mSeq;
            int newWatchedEvents = listener.onFileDescriptorEvents(record.mDescriptor, events2);
            if (newWatchedEvents != 0) {
                newWatchedEvents |= 4;
            }
            if (newWatchedEvents != oldWatchedEvents) {
                synchronized (this) {
                    int index = this.mFileDescriptorRecords.indexOfKey(fd);
                    if (index >= 0 && this.mFileDescriptorRecords.valueAt(index) == record && record.mSeq == seq) {
                        record.mEvents = newWatchedEvents;
                        if (newWatchedEvents == 0) {
                            this.mFileDescriptorRecords.removeAt(index);
                        }
                    }
                }
            }
            return newWatchedEvents;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00ed, code lost:
        r6 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x00ee, code lost:
        if (r6 >= r0) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00f0, code lost:
        r7 = r14.mPendingIdleHandlers;
        r8 = r7[r6];
        r7[r6] = null;
        r7 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x00fb, code lost:
        r7 = r8.queueIdle();
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x00fd, code lost:
        r9 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x00fe, code lost:
        android.util.Log.wtf(android.os.MessageQueue.TAG, "IdleHandler threw exception", r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x0115, code lost:
        r0 = 0;
        r5 = 0;
     */
    @android.annotation.UnsupportedAppUsage
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public android.os.Message next() {
        /*
            Method dump skipped, instructions count: 284
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.MessageQueue.next():android.os.Message");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void quit(boolean safe) {
        if (!this.mQuitAllowed) {
            throw new IllegalStateException("Main thread not allowed to quit.");
        }
        synchronized (this) {
            if (this.mQuitting) {
                return;
            }
            this.mQuitting = true;
            if (safe) {
                removeAllFutureMessagesLocked();
            } else {
                removeAllMessagesLocked();
            }
            nativeWake(this.mPtr);
        }
    }

    public int postSyncBarrier() {
        return postSyncBarrier(SystemClock.uptimeMillis());
    }

    private int postSyncBarrier(long when) {
        int token;
        synchronized (this) {
            token = this.mNextBarrierToken;
            this.mNextBarrierToken = token + 1;
            Message msg = Message.obtain();
            msg.markInUse();
            msg.when = when;
            msg.arg1 = token;
            Message prev = null;
            Message p = this.mMessages;
            if (when != 0) {
                while (p != null && p.when <= when) {
                    prev = p;
                    p = p.next;
                }
            }
            if (prev != null) {
                msg.next = p;
                prev.next = msg;
            } else {
                msg.next = p;
                this.mMessages = msg;
            }
        }
        return token;
    }

    public void removeSyncBarrier(int token) {
        boolean needWake;
        synchronized (this) {
            Message prev = null;
            Message p = this.mMessages;
            while (p != null && (p.target != null || p.arg1 != token)) {
                prev = p;
                p = p.next;
            }
            if (p == null) {
                throw new IllegalStateException("The specified message queue synchronization  barrier token has not been posted or has already been removed.");
            }
            if (prev != null) {
                prev.next = p.next;
                needWake = false;
            } else {
                this.mMessages = p.next;
                if (this.mMessages != null && this.mMessages.target == null) {
                    needWake = false;
                }
                needWake = true;
            }
            p.recycleUnchecked();
            if (needWake && !this.mQuitting) {
                nativeWake(this.mPtr);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0080 A[Catch: all -> 0x0087, TryCatch #0 {, blocks: (B:7:0x000b, B:9:0x0010, B:10:0x0034, B:12:0x0036, B:16:0x0046, B:19:0x004d, B:21:0x0051, B:23:0x0055, B:26:0x005c, B:28:0x0062, B:32:0x006b, B:35:0x0073, B:38:0x0080, B:39:0x0085, B:36:0x0078), top: B:48:0x000b }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean enqueueMessage(android.os.Message r7, long r8) {
        /*
            r6 = this;
            android.os.Handler r0 = r7.target
            if (r0 == 0) goto La1
            boolean r0 = r7.isInUse()
            if (r0 != 0) goto L8a
            monitor-enter(r6)
            boolean r0 = r6.mQuitting     // Catch: java.lang.Throwable -> L87
            r1 = 0
            if (r0 == 0) goto L36
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException     // Catch: java.lang.Throwable -> L87
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L87
            r2.<init>()     // Catch: java.lang.Throwable -> L87
            android.os.Handler r3 = r7.target     // Catch: java.lang.Throwable -> L87
            r2.append(r3)     // Catch: java.lang.Throwable -> L87
            java.lang.String r3 = " sending message to a Handler on a dead thread"
            r2.append(r3)     // Catch: java.lang.Throwable -> L87
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Throwable -> L87
            r0.<init>(r2)     // Catch: java.lang.Throwable -> L87
            java.lang.String r2 = "MessageQueue"
            java.lang.String r3 = r0.getMessage()     // Catch: java.lang.Throwable -> L87
            android.util.Log.w(r2, r3, r0)     // Catch: java.lang.Throwable -> L87
            r7.recycle()     // Catch: java.lang.Throwable -> L87
            monitor-exit(r6)     // Catch: java.lang.Throwable -> L87
            return r1
        L36:
            r7.markInUse()     // Catch: java.lang.Throwable -> L87
            r7.when = r8     // Catch: java.lang.Throwable -> L87
            android.os.Message r0 = r6.mMessages     // Catch: java.lang.Throwable -> L87
            r2 = 1
            if (r0 == 0) goto L78
            r3 = 0
            int r3 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1))
            if (r3 == 0) goto L78
            long r3 = r0.when     // Catch: java.lang.Throwable -> L87
            int r3 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1))
            if (r3 >= 0) goto L4d
            goto L78
        L4d:
            boolean r3 = r6.mBlocked     // Catch: java.lang.Throwable -> L87
            if (r3 == 0) goto L5c
            android.os.Handler r3 = r0.target     // Catch: java.lang.Throwable -> L87
            if (r3 != 0) goto L5c
            boolean r3 = r7.isAsynchronous()     // Catch: java.lang.Throwable -> L87
            if (r3 == 0) goto L5c
            r1 = r2
        L5c:
            r3 = r0
            android.os.Message r4 = r0.next     // Catch: java.lang.Throwable -> L87
            r0 = r4
            if (r0 == 0) goto L73
            long r4 = r0.when     // Catch: java.lang.Throwable -> L87
            int r4 = (r8 > r4 ? 1 : (r8 == r4 ? 0 : -1))
            if (r4 >= 0) goto L69
            goto L73
        L69:
            if (r1 == 0) goto L5c
            boolean r4 = r0.isAsynchronous()     // Catch: java.lang.Throwable -> L87
            if (r4 == 0) goto L5c
            r1 = 0
            goto L5c
        L73:
            r7.next = r0     // Catch: java.lang.Throwable -> L87
            r3.next = r7     // Catch: java.lang.Throwable -> L87
            goto L7e
        L78:
            r7.next = r0     // Catch: java.lang.Throwable -> L87
            r6.mMessages = r7     // Catch: java.lang.Throwable -> L87
            boolean r1 = r6.mBlocked     // Catch: java.lang.Throwable -> L87
        L7e:
            if (r1 == 0) goto L85
            long r3 = r6.mPtr     // Catch: java.lang.Throwable -> L87
            nativeWake(r3)     // Catch: java.lang.Throwable -> L87
        L85:
            monitor-exit(r6)     // Catch: java.lang.Throwable -> L87
            return r2
        L87:
            r0 = move-exception
            monitor-exit(r6)     // Catch: java.lang.Throwable -> L87
            throw r0
        L8a:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r7)
            java.lang.String r2 = " This message is already in use."
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        La1:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "Message must have a target."
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.MessageQueue.enqueueMessage(android.os.Message, long):boolean");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasMessages(Handler h, int what, Object object) {
        if (h == null) {
            return false;
        }
        synchronized (this) {
            for (Message p = this.mMessages; p != null; p = p.next) {
                if (p.target == h && p.what == what && (object == null || p.obj == object)) {
                    return true;
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public boolean hasMessages(Handler h, Runnable r, Object object) {
        if (h == null) {
            return false;
        }
        synchronized (this) {
            for (Message p = this.mMessages; p != null; p = p.next) {
                if (p.target == h && p.callback == r && (object == null || p.obj == object)) {
                    return true;
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasMessages(Handler h) {
        if (h == null) {
            return false;
        }
        synchronized (this) {
            for (Message p = this.mMessages; p != null; p = p.next) {
                if (p.target == h) {
                    return true;
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeMessages(Handler h, int what, Object object) {
        if (h == null) {
            return;
        }
        synchronized (this) {
            Message p = this.mMessages;
            while (p != null && p.target == h && p.what == what && (object == null || p.obj == object)) {
                Message n = p.next;
                this.mMessages = n;
                p.recycleUnchecked();
                p = n;
            }
            while (p != null) {
                Message n2 = p.next;
                if (n2 != null && n2.target == h && n2.what == what && (object == null || n2.obj == object)) {
                    Message nn = n2.next;
                    n2.recycleUnchecked();
                    p.next = nn;
                } else {
                    p = n2;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeMessages(Handler h, Runnable r, Object object) {
        if (h == null || r == null) {
            return;
        }
        synchronized (this) {
            Message p = this.mMessages;
            while (p != null && p.target == h && p.callback == r && (object == null || p.obj == object)) {
                Message n = p.next;
                this.mMessages = n;
                p.recycleUnchecked();
                p = n;
            }
            while (p != null) {
                Message n2 = p.next;
                if (n2 != null && n2.target == h && n2.callback == r && (object == null || n2.obj == object)) {
                    Message nn = n2.next;
                    n2.recycleUnchecked();
                    p.next = nn;
                } else {
                    p = n2;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeCallbacksAndMessages(Handler h, Object object) {
        if (h == null) {
            return;
        }
        synchronized (this) {
            Message p = this.mMessages;
            while (p != null && p.target == h && (object == null || p.obj == object)) {
                Message n = p.next;
                this.mMessages = n;
                p.recycleUnchecked();
                p = n;
            }
            while (p != null) {
                Message n2 = p.next;
                if (n2 != null && n2.target == h && (object == null || n2.obj == object)) {
                    Message nn = n2.next;
                    n2.recycleUnchecked();
                    p.next = nn;
                } else {
                    p = n2;
                }
            }
        }
    }

    private void removeAllMessagesLocked() {
        Message p = this.mMessages;
        while (p != null) {
            Message n = p.next;
            p.recycleUnchecked();
            p = n;
        }
        this.mMessages = null;
    }

    private void removeAllFutureMessagesLocked() {
        long now = SystemClock.uptimeMillis();
        Message p = this.mMessages;
        if (p != null) {
            if (p.when > now) {
                removeAllMessagesLocked();
                return;
            }
            while (true) {
                Message n = p.next;
                if (n == null) {
                    return;
                }
                if (n.when <= now) {
                    p = n;
                } else {
                    p.next = null;
                    do {
                        Message p2 = n;
                        n = p2.next;
                        p2.recycleUnchecked();
                    } while (n != null);
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dump(Printer pw, String prefix, Handler h) {
        synchronized (this) {
            long now = SystemClock.uptimeMillis();
            int n = 0;
            for (Message msg = this.mMessages; msg != null; msg = msg.next) {
                if (h == null || h == msg.target) {
                    pw.println(prefix + "Message " + n + ": " + msg.toString(now));
                }
                n++;
            }
            pw.println(prefix + "(Total messages: " + n + ", polling=" + isPollingLocked() + ", quitting=" + this.mQuitting + ")");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void writeToProto(ProtoOutputStream proto, long fieldId) {
        long messageQueueToken = proto.start(fieldId);
        synchronized (this) {
            for (Message msg = this.mMessages; msg != null; msg = msg.next) {
                msg.writeToProto(proto, 2246267895809L);
            }
            proto.write(1133871366146L, isPollingLocked());
            proto.write(1133871366147L, this.mQuitting);
        }
        proto.end(messageQueueToken);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class FileDescriptorRecord {
        public final FileDescriptor mDescriptor;
        public int mEvents;
        public OnFileDescriptorEventListener mListener;
        public int mSeq;

        public FileDescriptorRecord(FileDescriptor descriptor, int events, OnFileDescriptorEventListener listener) {
            this.mDescriptor = descriptor;
            this.mEvents = events;
            this.mListener = listener;
        }
    }
}
