package com.android.internal.os;

import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.ProxyFileDescriptorCallback;
import android.system.ErrnoException;
import android.system.OsConstants;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
/* loaded from: classes3.dex */
public class FuseAppLoop implements Handler.Callback {
    private static final int ARGS_POOL_SIZE = 50;
    private static final int FUSE_FSYNC = 20;
    private static final int FUSE_GETATTR = 3;
    private static final int FUSE_LOOKUP = 1;
    private static final int FUSE_MAX_WRITE = 131072;
    private static final int FUSE_OK = 0;
    private static final int FUSE_OPEN = 14;
    private static final int FUSE_READ = 15;
    private static final int FUSE_RELEASE = 18;
    private static final int FUSE_WRITE = 16;
    private static final int MIN_INODE = 2;
    public static final int ROOT_INODE = 1;
    @GuardedBy("mLock")
    private long mInstance;
    private final int mMountPointId;
    private final Thread mThread;
    private static final String TAG = "FuseAppLoop";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final ThreadFactory sDefaultThreadFactory = new ThreadFactory() { // from class: com.android.internal.os.FuseAppLoop.1
        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable r) {
            return new Thread(r, FuseAppLoop.TAG);
        }
    };
    private final Object mLock = new Object();
    @GuardedBy("mLock")
    private final SparseArray<CallbackEntry> mCallbackMap = new SparseArray<>();
    @GuardedBy("mLock")
    private final BytesMap mBytesMap = new BytesMap();
    @GuardedBy("mLock")
    private final LinkedList<Args> mArgsPool = new LinkedList<>();
    @GuardedBy("mLock")
    private int mNextInode = 2;

    /* loaded from: classes3.dex */
    public static class UnmountedException extends Exception {
    }

    native void native_delete(long j);

    native long native_new(int i);

    native void native_replyGetAttr(long j, long j2, long j3, long j4);

    native void native_replyLookup(long j, long j2, long j3, long j4);

    native void native_replyOpen(long j, long j2, long j3);

    native void native_replyRead(long j, long j2, int i, byte[] bArr);

    native void native_replySimple(long j, long j2, int i);

    native void native_replyWrite(long j, long j2, int i);

    native void native_start(long j);

    public synchronized FuseAppLoop(int mountPointId, ParcelFileDescriptor fd, ThreadFactory factory) {
        this.mMountPointId = mountPointId;
        factory = factory == null ? sDefaultThreadFactory : factory;
        this.mInstance = native_new(fd.detachFd());
        this.mThread = factory.newThread(new Runnable() { // from class: com.android.internal.os.-$$Lambda$FuseAppLoop$e9Yru2f_btesWlxIgerkPnHibpg
            @Override // java.lang.Runnable
            public final void run() {
                FuseAppLoop.lambda$new$0(FuseAppLoop.this);
            }
        });
        this.mThread.start();
    }

    public static /* synthetic */ void lambda$new$0(FuseAppLoop fuseAppLoop) {
        fuseAppLoop.native_start(fuseAppLoop.mInstance);
        synchronized (fuseAppLoop.mLock) {
            fuseAppLoop.native_delete(fuseAppLoop.mInstance);
            fuseAppLoop.mInstance = 0L;
            fuseAppLoop.mBytesMap.clear();
        }
    }

    public synchronized int registerCallback(ProxyFileDescriptorCallback callback, Handler handler) throws FuseUnavailableMountException {
        int id;
        synchronized (this.mLock) {
            Preconditions.checkNotNull(callback);
            Preconditions.checkNotNull(handler);
            Preconditions.checkState(this.mCallbackMap.size() < 2147483645, "Too many opened files.");
            Preconditions.checkArgument(Thread.currentThread().getId() != handler.getLooper().getThread().getId(), "Handler must be different from the current thread");
            if (this.mInstance == 0) {
                throw new FuseUnavailableMountException(this.mMountPointId);
            }
            do {
                id = this.mNextInode;
                this.mNextInode++;
                if (this.mNextInode < 0) {
                    this.mNextInode = 2;
                }
            } while (this.mCallbackMap.get(id) != null);
            this.mCallbackMap.put(id, new CallbackEntry(callback, new Handler(handler.getLooper(), this)));
        }
        return id;
    }

    public synchronized void unregisterCallback(int id) {
        synchronized (this.mLock) {
            this.mCallbackMap.remove(id);
        }
    }

    public synchronized int getMountPointId() {
        return this.mMountPointId;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:169:0x01c9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r16v0 */
    /* JADX WARN: Type inference failed for: r16v1 */
    /* JADX WARN: Type inference failed for: r16v11 */
    /* JADX WARN: Type inference failed for: r16v12 */
    /* JADX WARN: Type inference failed for: r16v16 */
    /* JADX WARN: Type inference failed for: r16v17 */
    /* JADX WARN: Type inference failed for: r16v2 */
    /* JADX WARN: Type inference failed for: r16v21 */
    /* JADX WARN: Type inference failed for: r16v22 */
    /* JADX WARN: Type inference failed for: r16v23 */
    /* JADX WARN: Type inference failed for: r16v6 */
    /* JADX WARN: Type inference failed for: r16v7 */
    @Override // android.os.Handler.Callback
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean handleMessage(android.os.Message r26) {
        /*
            Method dump skipped, instructions count: 494
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.FuseAppLoop.handleMessage(android.os.Message):boolean");
    }

    public protected void onCommand(int command, long unique, long inode, long offset, int size, byte[] data) {
        Args args;
        synchronized (this.mLock) {
            try {
                if (this.mArgsPool.size() == 0) {
                    args = new Args();
                } else {
                    args = this.mArgsPool.pop();
                }
                args.unique = unique;
                args.inode = inode;
                args.offset = offset;
                args.size = size;
                args.data = data;
                args.entry = getCallbackEntryOrThrowLocked(inode);
            } catch (Exception error) {
                replySimpleLocked(unique, getError(error));
            }
            if (!args.entry.handler.sendMessage(Message.obtain(args.entry.handler, command, 0, 0, args))) {
                throw new ErrnoException("onCommand", OsConstants.EBADF);
            }
        }
    }

    public protected byte[] onOpen(long unique, long inode) {
        CallbackEntry entry;
        synchronized (this.mLock) {
            try {
                entry = getCallbackEntryOrThrowLocked(inode);
            } catch (ErrnoException error) {
                replySimpleLocked(unique, getError(error));
            }
            if (entry.opened) {
                throw new ErrnoException("onOpen", OsConstants.EMFILE);
            }
            if (this.mInstance != 0) {
                native_replyOpen(this.mInstance, unique, inode);
                entry.opened = true;
                return this.mBytesMap.startUsing(entry.getThreadId());
            }
            return null;
        }
    }

    private static synchronized int getError(Exception error) {
        int errno;
        if ((error instanceof ErrnoException) && (errno = ((ErrnoException) error).errno) != OsConstants.ENOSYS) {
            return -errno;
        }
        return -OsConstants.EBADF;
    }

    @GuardedBy("mLock")
    private synchronized CallbackEntry getCallbackEntryOrThrowLocked(long inode) throws ErrnoException {
        CallbackEntry entry = this.mCallbackMap.get(checkInode(inode));
        if (entry == null) {
            throw new ErrnoException("getCallbackEntryOrThrowLocked", OsConstants.ENOENT);
        }
        return entry;
    }

    @GuardedBy("mLock")
    private synchronized void recycleLocked(Args args) {
        if (this.mArgsPool.size() < 50) {
            this.mArgsPool.add(args);
        }
    }

    @GuardedBy("mLock")
    private synchronized void replySimpleLocked(long unique, int result) {
        if (this.mInstance != 0) {
            native_replySimple(this.mInstance, unique, result);
        }
    }

    private static synchronized int checkInode(long inode) {
        Preconditions.checkArgumentInRange(inode, 2L, 2147483647L, "checkInode");
        return (int) inode;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class CallbackEntry {
        final ProxyFileDescriptorCallback callback;
        final Handler handler;
        boolean opened;

        synchronized CallbackEntry(ProxyFileDescriptorCallback callback, Handler handler) {
            this.callback = (ProxyFileDescriptorCallback) Preconditions.checkNotNull(callback);
            this.handler = (Handler) Preconditions.checkNotNull(handler);
        }

        synchronized long getThreadId() {
            return this.handler.getLooper().getThread().getId();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class BytesMapEntry {
        byte[] bytes;
        int counter;

        private synchronized BytesMapEntry() {
            this.counter = 0;
            this.bytes = new byte[131072];
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class BytesMap {
        final Map<Long, BytesMapEntry> mEntries;

        private synchronized BytesMap() {
            this.mEntries = new HashMap();
        }

        synchronized byte[] startUsing(long threadId) {
            BytesMapEntry entry = this.mEntries.get(Long.valueOf(threadId));
            if (entry == null) {
                entry = new BytesMapEntry();
                this.mEntries.put(Long.valueOf(threadId), entry);
            }
            entry.counter++;
            return entry.bytes;
        }

        synchronized void stopUsing(long threadId) {
            BytesMapEntry entry = this.mEntries.get(Long.valueOf(threadId));
            Preconditions.checkNotNull(entry);
            entry.counter--;
            if (entry.counter <= 0) {
                this.mEntries.remove(Long.valueOf(threadId));
            }
        }

        synchronized void clear() {
            this.mEntries.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class Args {
        byte[] data;
        CallbackEntry entry;
        long inode;
        long offset;
        int size;
        long unique;

        private synchronized Args() {
        }
    }
}
