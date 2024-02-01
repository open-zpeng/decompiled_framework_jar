package android.mtp;

import android.media.MediaFile;
import android.mtp.MtpStorageManager;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.FileObserver;
import android.os.storage.StorageVolume;
import android.provider.Telephony;
import android.util.Log;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
/* loaded from: classes2.dex */
public class MtpStorageManager {
    private static final int IN_IGNORED = 32768;
    private static final int IN_ISDIR = 1073741824;
    private static final int IN_ONLYDIR = 16777216;
    private static final int IN_Q_OVERFLOW = 16384;
    private static final String TAG = MtpStorageManager.class.getSimpleName();
    public static boolean sDebug = false;
    private MtpNotifier mMtpNotifier;
    private Set<String> mSubdirectories;
    private HashMap<Integer, MtpObject> mObjects = new HashMap<>();
    private HashMap<Integer, MtpObject> mRoots = new HashMap<>();
    private int mNextObjectId = 1;
    private int mNextStorageId = 1;
    private volatile boolean mCheckConsistency = false;
    private Thread mConsistencyThread = new Thread(new Runnable() { // from class: android.mtp.-$$Lambda$MtpStorageManager$HocvlaKIXOtuA3p8uOP9PA7UJtw
        @Override // java.lang.Runnable
        public final void run() {
            MtpStorageManager.lambda$new$0(MtpStorageManager.this);
        }
    });

    /* loaded from: classes2.dex */
    public static abstract class MtpNotifier {
        public abstract synchronized void sendObjectAdded(int i);

        public abstract synchronized void sendObjectRemoved(int i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public enum MtpObjectState {
        NORMAL,
        FROZEN,
        FROZEN_ADDED,
        FROZEN_REMOVED,
        FROZEN_ONESHOT_ADD,
        FROZEN_ONESHOT_DEL
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public enum MtpOperation {
        NONE,
        ADD,
        RENAME,
        COPY,
        DELETE
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class MtpObjectObserver extends FileObserver {
        MtpObject mObject;

        MtpObjectObserver(MtpObject object) {
            super(object.getPath().toString(), 16778176);
            this.mObject = object;
        }

        /* JADX WARN: Code restructure failed: missing block: B:30:0x0097, code lost:
            r2 = android.mtp.MtpStorageManager.TAG;
            android.util.Log.w(r2, "Object was null in event " + r7);
         */
        /* JADX WARN: Code restructure failed: missing block: B:32:0x00b0, code lost:
            return;
         */
        @Override // android.os.FileObserver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onEvent(int r6, java.lang.String r7) {
            /*
                Method dump skipped, instructions count: 274
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpStorageManager.MtpObjectObserver.onEvent(int, java.lang.String):void");
        }

        @Override // android.os.FileObserver
        public void finalize() {
        }
    }

    /* loaded from: classes2.dex */
    public static class MtpObject {
        private HashMap<String, MtpObject> mChildren;
        private int mId;
        private boolean mIsDir;
        private String mName;
        private MtpObject mParent;
        private FileObserver mObserver = null;
        private boolean mVisited = false;
        private MtpObjectState mState = MtpObjectState.NORMAL;
        private MtpOperation mOp = MtpOperation.NONE;

        synchronized MtpObject(String name, int id, MtpObject parent, boolean isDir) {
            this.mId = id;
            this.mName = name;
            this.mParent = parent;
            this.mIsDir = isDir;
            this.mChildren = this.mIsDir ? new HashMap<>() : null;
        }

        public synchronized String getName() {
            return this.mName;
        }

        public synchronized int getId() {
            return this.mId;
        }

        public synchronized boolean isDir() {
            return this.mIsDir;
        }

        public synchronized int getFormat() {
            if (this.mIsDir) {
                return 12289;
            }
            return MediaFile.getFormatCode(this.mName, null);
        }

        public synchronized int getStorageId() {
            return getRoot().getId();
        }

        public synchronized long getModifiedTime() {
            return getPath().toFile().lastModified() / 1000;
        }

        public synchronized MtpObject getParent() {
            return this.mParent;
        }

        public synchronized MtpObject getRoot() {
            return isRoot() ? this : this.mParent.getRoot();
        }

        public synchronized long getSize() {
            if (this.mIsDir) {
                return 0L;
            }
            return getPath().toFile().length();
        }

        public synchronized Path getPath() {
            return isRoot() ? Paths.get(this.mName, new String[0]) : this.mParent.getPath().resolve(this.mName);
        }

        public synchronized boolean isRoot() {
            return this.mParent == null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void setName(String name) {
            this.mName = name;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void setId(int id) {
            this.mId = id;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized boolean isVisited() {
            return this.mVisited;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void setParent(MtpObject parent) {
            this.mParent = parent;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void setDir(boolean dir) {
            if (dir != this.mIsDir) {
                this.mIsDir = dir;
                this.mChildren = this.mIsDir ? new HashMap<>() : null;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void setVisited(boolean visited) {
            this.mVisited = visited;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized MtpObjectState getState() {
            return this.mState;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void setState(MtpObjectState state) {
            this.mState = state;
            if (this.mState == MtpObjectState.NORMAL) {
                this.mOp = MtpOperation.NONE;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized MtpOperation getOperation() {
            return this.mOp;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void setOperation(MtpOperation op) {
            this.mOp = op;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized FileObserver getObserver() {
            return this.mObserver;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void setObserver(FileObserver observer) {
            this.mObserver = observer;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void addChild(MtpObject child) {
            this.mChildren.put(child.getName(), child);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized MtpObject getChild(String name) {
            return this.mChildren.get(name);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized Collection<MtpObject> getChildren() {
            return this.mChildren.values();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized boolean exists() {
            return getPath().toFile().exists();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized MtpObject copy(boolean recursive) {
            MtpObject copy = new MtpObject(this.mName, this.mId, this.mParent, this.mIsDir);
            copy.mIsDir = this.mIsDir;
            copy.mVisited = this.mVisited;
            copy.mState = this.mState;
            copy.mChildren = this.mIsDir ? new HashMap<>() : null;
            if (recursive && this.mIsDir) {
                for (MtpObject child : this.mChildren.values()) {
                    MtpObject childCopy = child.copy(true);
                    childCopy.setParent(copy);
                    copy.addChild(childCopy);
                }
            }
            return copy;
        }
    }

    public synchronized MtpStorageManager(MtpNotifier notifier, Set<String> subdirectories) {
        this.mMtpNotifier = notifier;
        this.mSubdirectories = subdirectories;
        if (this.mCheckConsistency) {
            this.mConsistencyThread.start();
        }
    }

    public static /* synthetic */ void lambda$new$0(MtpStorageManager mtpStorageManager) {
        while (mtpStorageManager.mCheckConsistency) {
            try {
                Thread.sleep(15000L);
                if (mtpStorageManager.checkConsistency()) {
                    Log.v(TAG, "Cache is consistent");
                } else {
                    Log.w(TAG, "Cache is not consistent");
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public synchronized void close() {
        Stream<MtpObject> objs = Stream.concat(this.mRoots.values().stream(), this.mObjects.values().stream());
        for (MtpObject obj : objs) {
            if (obj.getObserver() != null) {
                obj.getObserver().stopWatching();
                obj.setObserver(null);
            }
        }
        if (this.mCheckConsistency) {
            this.mCheckConsistency = false;
            this.mConsistencyThread.interrupt();
            try {
                this.mConsistencyThread.join();
            } catch (InterruptedException e) {
            }
        }
    }

    public synchronized void setSubdirectories(Set<String> subDirs) {
        this.mSubdirectories = subDirs;
    }

    public synchronized MtpStorage addMtpStorage(StorageVolume volume) {
        MtpStorage storage;
        int storageId = ((getNextStorageId() & 65535) << 16) + 1;
        storage = new MtpStorage(volume, storageId);
        MtpObject root = new MtpObject(storage.getPath(), storageId, null, true);
        this.mRoots.put(Integer.valueOf(storageId), root);
        return storage;
    }

    public synchronized void removeMtpStorage(MtpStorage storage) {
        removeObjectFromCache(getStorageRoot(storage.getStorageId()), true, true);
    }

    private synchronized boolean isSpecialSubDir(MtpObject obj) {
        boolean z;
        if (obj.getParent().isRoot() && this.mSubdirectories != null) {
            z = this.mSubdirectories.contains(obj.getName()) ? false : true;
        }
        return z;
    }

    public synchronized MtpObject getByPath(String path) {
        String[] split;
        MtpObject obj = null;
        for (MtpObject root : this.mRoots.values()) {
            if (path.startsWith(root.getName())) {
                obj = root;
                path = path.substring(root.getName().length());
            }
        }
        for (String name : path.split("/")) {
            if (obj != null && obj.isDir()) {
                if (!"".equals(name)) {
                    if (!obj.isVisited()) {
                        getChildren(obj);
                    }
                    obj = obj.getChild(name);
                }
            }
            return null;
        }
        return obj;
    }

    public synchronized MtpObject getObject(int id) {
        if (id == 0 || id == -1) {
            Log.w(TAG, "Can't get root storages with getObject()");
            return null;
        } else if (!this.mObjects.containsKey(Integer.valueOf(id))) {
            String str = TAG;
            Log.w(str, "Id " + id + " doesn't exist");
            return null;
        } else {
            return this.mObjects.get(Integer.valueOf(id));
        }
    }

    public synchronized MtpObject getStorageRoot(int id) {
        if (!this.mRoots.containsKey(Integer.valueOf(id))) {
            String str = TAG;
            Log.w(str, "StorageId " + id + " doesn't exist");
            return null;
        }
        return this.mRoots.get(Integer.valueOf(id));
    }

    private synchronized int getNextObjectId() {
        int ret = this.mNextObjectId;
        this.mNextObjectId = (int) (this.mNextObjectId + 1);
        return ret;
    }

    private synchronized int getNextStorageId() {
        int i = this.mNextStorageId;
        this.mNextStorageId = i + 1;
        return i;
    }

    public synchronized Stream<MtpObject> getObjects(int parent, int format, int storageId) {
        boolean recursive = parent == 0;
        if (parent == -1) {
            parent = 0;
        }
        if (storageId == -1 && parent == 0) {
            ArrayList<Stream<MtpObject>> streamList = new ArrayList<>();
            for (MtpObject root : this.mRoots.values()) {
                streamList.add(getObjects(root, format, recursive));
            }
            return (Stream) Stream.of(streamList).flatMap($$Lambda$JdUL9ZP9AzcttUlxZCHq6pfTzU.INSTANCE).reduce($$Lambda$MtpStorageManager$QdR1YPNkK9RX4bISfNvQAOnGxGE.INSTANCE).orElseGet($$Lambda$MtpStorageManager$TsWypJRYDhxg01Bfs_tm2d_H9zU.INSTANCE);
        }
        MtpObject obj = parent == 0 ? getStorageRoot(storageId) : getObject(parent);
        if (obj == null) {
            return null;
        }
        return getObjects(obj, format, recursive);
    }

    private synchronized Stream<MtpObject> getObjects(MtpObject parent, final int format, boolean rec) {
        Collection<MtpObject> children = getChildren(parent);
        if (children == null) {
            return null;
        }
        Stream<MtpObject> ret = Stream.of(children).flatMap(new Function() { // from class: android.mtp.-$$Lambda$72U6ffwsZ0Sm2BXYilXSg7hTsO8
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return ((Collection) obj).stream();
            }
        });
        if (format != 0) {
            ret = ret.filter(new Predicate() { // from class: android.mtp.-$$Lambda$MtpStorageManager$DVPwWM5hkC_B_cgO9AF8IKzObmQ
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return MtpStorageManager.lambda$getObjects$1(format, (MtpStorageManager.MtpObject) obj);
                }
            });
        }
        if (rec) {
            ArrayList<Stream<MtpObject>> streamList = new ArrayList<>();
            streamList.add(ret);
            for (MtpObject o : children) {
                if (o.isDir()) {
                    streamList.add(getObjects(o, format, true));
                }
            }
            ret = (Stream) Stream.of(streamList).filter(new Predicate() { // from class: android.mtp.-$$Lambda$MtpStorageManager$ZX5EBcSdO0MZYnMFDwTJpRFAOd0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean nonNull;
                    nonNull = Objects.nonNull((ArrayList) obj);
                    return nonNull;
                }
            }).flatMap($$Lambda$JdUL9ZP9AzcttUlxZCHq6pfTzU.INSTANCE).reduce($$Lambda$MtpStorageManager$QdR1YPNkK9RX4bISfNvQAOnGxGE.INSTANCE).orElseGet($$Lambda$MtpStorageManager$TsWypJRYDhxg01Bfs_tm2d_H9zU.INSTANCE);
        }
        return ret;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$getObjects$1(int format, MtpObject o) {
        return o.getFormat() == format;
    }

    private synchronized Collection<MtpObject> getChildren(MtpObject object) {
        if (object != null) {
            if (object.isDir()) {
                if (!object.isVisited()) {
                    Path dir = object.getPath();
                    if (object.getObserver() != null) {
                        Log.e(TAG, "Observer is not null!");
                    }
                    object.setObserver(new MtpObjectObserver(object));
                    object.getObserver().startWatching();
                    try {
                        DirectoryStream<Path> stream = Files.newDirectoryStream(dir);
                        try {
                            for (Path file : stream) {
                                addObjectToCache(object, file.getFileName().toString(), file.toFile().isDirectory());
                            }
                            if (stream != null) {
                                $closeResource(null, stream);
                            }
                            object.setVisited(true);
                        } finally {
                        }
                    } catch (IOException | DirectoryIteratorException e) {
                        Log.e(TAG, e.toString());
                        object.getObserver().stopWatching();
                        object.setObserver(null);
                        return null;
                    }
                }
                return object.getChildren();
            }
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Can't find children of ");
        sb.append(object == null ? "null" : Integer.valueOf(object.getId()));
        Log.w(str, sb.toString());
        return null;
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 == null) {
            x1.close();
            return;
        }
        try {
            x1.close();
        } catch (Throwable th) {
            x0.addSuppressed(th);
        }
    }

    private synchronized MtpObject addObjectToCache(MtpObject parent, String newName, boolean isDir) {
        if (parent.isRoot() || getObject(parent.getId()) == parent) {
            if (parent.getChild(newName) != null) {
                return null;
            }
            if (this.mSubdirectories == null || !parent.isRoot() || this.mSubdirectories.contains(newName)) {
                MtpObject obj = new MtpObject(newName, getNextObjectId(), parent, isDir);
                this.mObjects.put(Integer.valueOf(obj.getId()), obj);
                parent.addChild(obj);
                return obj;
            }
            return null;
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0045 A[Catch: all -> 0x00d5, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0009, B:11:0x0021, B:13:0x0025, B:14:0x003f, B:16:0x0045, B:30:0x0076, B:32:0x007a, B:33:0x0094, B:35:0x009a, B:36:0x00a5, B:39:0x00ad, B:40:0x00ba, B:42:0x00c0, B:23:0x005e), top: B:53:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:22:0x005c  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x009a A[Catch: all -> 0x00d5, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0009, B:11:0x0021, B:13:0x0025, B:14:0x003f, B:16:0x0045, B:30:0x0076, B:32:0x007a, B:33:0x0094, B:35:0x009a, B:36:0x00a5, B:39:0x00ad, B:40:0x00ba, B:42:0x00c0, B:23:0x005e), top: B:53:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00c0 A[Catch: all -> 0x00d5, TRY_LEAVE, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0009, B:11:0x0021, B:13:0x0025, B:14:0x003f, B:16:0x0045, B:30:0x0076, B:32:0x007a, B:33:0x0094, B:35:0x009a, B:36:0x00a5, B:39:0x00ad, B:40:0x00ba, B:42:0x00c0, B:23:0x005e), top: B:53:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized boolean removeObjectFromCache(android.mtp.MtpStorageManager.MtpObject r8, boolean r9, boolean r10) {
        /*
            r7 = this;
            monitor-enter(r7)
            boolean r0 = r8.isRoot()     // Catch: java.lang.Throwable -> Ld5
            r1 = 0
            r2 = 1
            if (r0 != 0) goto L1e
            android.mtp.MtpStorageManager$MtpObject r0 = r8.getParent()     // Catch: java.lang.Throwable -> Ld5
            java.util.HashMap r0 = android.mtp.MtpStorageManager.MtpObject.access$1100(r0)     // Catch: java.lang.Throwable -> Ld5
            java.lang.String r3 = r8.getName()     // Catch: java.lang.Throwable -> Ld5
            boolean r0 = r0.remove(r3, r8)     // Catch: java.lang.Throwable -> Ld5
            if (r0 == 0) goto L1c
            goto L1e
        L1c:
            r0 = r1
            goto L1f
        L1e:
            r0 = r2
        L1f:
            if (r0 != 0) goto L3f
            boolean r3 = android.mtp.MtpStorageManager.sDebug     // Catch: java.lang.Throwable -> Ld5
            if (r3 == 0) goto L3f
            java.lang.String r3 = android.mtp.MtpStorageManager.TAG     // Catch: java.lang.Throwable -> Ld5
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Ld5
            r4.<init>()     // Catch: java.lang.Throwable -> Ld5
            java.lang.String r5 = "Failed to remove from parent "
            r4.append(r5)     // Catch: java.lang.Throwable -> Ld5
            java.nio.file.Path r5 = r8.getPath()     // Catch: java.lang.Throwable -> Ld5
            r4.append(r5)     // Catch: java.lang.Throwable -> Ld5
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Throwable -> Ld5
            android.util.Log.w(r3, r4)     // Catch: java.lang.Throwable -> Ld5
        L3f:
            boolean r3 = r8.isRoot()     // Catch: java.lang.Throwable -> Ld5
            if (r3 == 0) goto L5c
            java.util.HashMap<java.lang.Integer, android.mtp.MtpStorageManager$MtpObject> r3 = r7.mRoots     // Catch: java.lang.Throwable -> Ld5
            int r4 = r8.getId()     // Catch: java.lang.Throwable -> Ld5
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch: java.lang.Throwable -> Ld5
            boolean r3 = r3.remove(r4, r8)     // Catch: java.lang.Throwable -> Ld5
            if (r3 == 0) goto L59
            if (r0 == 0) goto L59
            r3 = r2
            goto L5a
        L59:
            r3 = r1
        L5a:
            r0 = r3
            goto L74
        L5c:
            if (r9 == 0) goto L74
            java.util.HashMap<java.lang.Integer, android.mtp.MtpStorageManager$MtpObject> r3 = r7.mObjects     // Catch: java.lang.Throwable -> Ld5
            int r4 = r8.getId()     // Catch: java.lang.Throwable -> Ld5
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch: java.lang.Throwable -> Ld5
            boolean r3 = r3.remove(r4, r8)     // Catch: java.lang.Throwable -> Ld5
            if (r3 == 0) goto L72
            if (r0 == 0) goto L72
            r3 = r2
            goto L73
        L72:
            r3 = r1
        L73:
            r0 = r3
        L74:
            if (r0 != 0) goto L94
            boolean r3 = android.mtp.MtpStorageManager.sDebug     // Catch: java.lang.Throwable -> Ld5
            if (r3 == 0) goto L94
            java.lang.String r3 = android.mtp.MtpStorageManager.TAG     // Catch: java.lang.Throwable -> Ld5
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Ld5
            r4.<init>()     // Catch: java.lang.Throwable -> Ld5
            java.lang.String r5 = "Failed to remove from global cache "
            r4.append(r5)     // Catch: java.lang.Throwable -> Ld5
            java.nio.file.Path r5 = r8.getPath()     // Catch: java.lang.Throwable -> Ld5
            r4.append(r5)     // Catch: java.lang.Throwable -> Ld5
            java.lang.String r4 = r4.toString()     // Catch: java.lang.Throwable -> Ld5
            android.util.Log.w(r3, r4)     // Catch: java.lang.Throwable -> Ld5
        L94:
            android.os.FileObserver r3 = android.mtp.MtpStorageManager.MtpObject.access$500(r8)     // Catch: java.lang.Throwable -> Ld5
            if (r3 == 0) goto La5
            android.os.FileObserver r3 = android.mtp.MtpStorageManager.MtpObject.access$500(r8)     // Catch: java.lang.Throwable -> Ld5
            r3.stopWatching()     // Catch: java.lang.Throwable -> Ld5
            r3 = 0
            android.mtp.MtpStorageManager.MtpObject.access$600(r8, r3)     // Catch: java.lang.Throwable -> Ld5
        La5:
            boolean r3 = r8.isDir()     // Catch: java.lang.Throwable -> Ld5
            if (r3 == 0) goto Ld3
            if (r10 == 0) goto Ld3
            java.util.ArrayList r3 = new java.util.ArrayList     // Catch: java.lang.Throwable -> Ld5
            java.util.Collection r4 = android.mtp.MtpStorageManager.MtpObject.access$900(r8)     // Catch: java.lang.Throwable -> Ld5
            r3.<init>(r4)     // Catch: java.lang.Throwable -> Ld5
            java.util.Iterator r4 = r3.iterator()     // Catch: java.lang.Throwable -> Ld5
        Lba:
            boolean r5 = r4.hasNext()     // Catch: java.lang.Throwable -> Ld5
            if (r5 == 0) goto Ld3
            java.lang.Object r5 = r4.next()     // Catch: java.lang.Throwable -> Ld5
            android.mtp.MtpStorageManager$MtpObject r5 = (android.mtp.MtpStorageManager.MtpObject) r5     // Catch: java.lang.Throwable -> Ld5
            boolean r6 = r7.removeObjectFromCache(r5, r9, r2)     // Catch: java.lang.Throwable -> Ld5
            if (r6 == 0) goto Ld0
            if (r0 == 0) goto Ld0
            r6 = r2
            goto Ld1
        Ld0:
            r6 = r1
        Ld1:
            r0 = r6
            goto Lba
        Ld3:
            monitor-exit(r7)
            return r0
        Ld5:
            r8 = move-exception
            monitor-exit(r7)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpStorageManager.removeObjectFromCache(android.mtp.MtpStorageManager$MtpObject, boolean, boolean):boolean");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handleAddedObject(MtpObject parent, String path, boolean isDir) {
        MtpOperation op = MtpOperation.NONE;
        MtpObject obj = parent.getChild(path);
        if (obj != null) {
            MtpObjectState state = obj.getState();
            op = obj.getOperation();
            if (obj.isDir() != isDir && state != MtpObjectState.FROZEN_REMOVED) {
                String str = TAG;
                Log.d(str, "Inconsistent directory info! " + obj.getPath());
            }
            obj.setDir(isDir);
            switch (state) {
                case FROZEN:
                case FROZEN_REMOVED:
                    obj.setState(MtpObjectState.FROZEN_ADDED);
                    break;
                case FROZEN_ONESHOT_ADD:
                    obj.setState(MtpObjectState.NORMAL);
                    break;
                case NORMAL:
                case FROZEN_ADDED:
                    return;
                default:
                    String str2 = TAG;
                    Log.w(str2, "Unexpected state in add " + path + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + state);
                    break;
            }
            if (sDebug) {
                String str3 = TAG;
                Log.i(str3, state + " transitioned to " + obj.getState() + " in op " + op);
            }
        } else {
            obj = addObjectToCache(parent, path, isDir);
            if (obj == null) {
                if (sDebug) {
                    String str4 = TAG;
                    Log.w(str4, "object " + path + " already exists");
                }
                return;
            }
            this.mMtpNotifier.sendObjectAdded(obj.getId());
        }
        if (isDir) {
            if (op == MtpOperation.RENAME) {
                return;
            }
            if (op == MtpOperation.COPY && !obj.isVisited()) {
                return;
            }
            if (obj.getObserver() != null) {
                Log.e(TAG, "Observer is not null!");
                return;
            }
            obj.setObserver(new MtpObjectObserver(obj));
            obj.getObserver().startWatching();
            obj.setVisited(true);
            try {
                DirectoryStream<Path> stream = Files.newDirectoryStream(obj.getPath());
                try {
                    for (Path file : stream) {
                        if (sDebug) {
                            String str5 = TAG;
                            Log.i(str5, "Manually handling event for " + file.getFileName().toString());
                        }
                        handleAddedObject(obj, file.getFileName().toString(), file.toFile().isDirectory());
                    }
                    if (stream != null) {
                        $closeResource(null, stream);
                    }
                } finally {
                }
            } catch (IOException | DirectoryIteratorException e) {
                Log.e(TAG, e.toString());
                obj.getObserver().stopWatching();
                obj.setObserver(null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handleRemovedObject(MtpObject obj) {
        MtpObjectState state = obj.getState();
        MtpOperation op = obj.getOperation();
        int i = AnonymousClass1.$SwitchMap$android$mtp$MtpStorageManager$MtpObjectState[state.ordinal()];
        boolean z = true;
        if (i == 1) {
            obj.setState(MtpObjectState.FROZEN_REMOVED);
        } else {
            switch (i) {
                case 4:
                    if (removeObjectFromCache(obj, true, true)) {
                        this.mMtpNotifier.sendObjectRemoved(obj.getId());
                        break;
                    }
                    break;
                case 5:
                    obj.setState(MtpObjectState.FROZEN_REMOVED);
                    break;
                case 6:
                    if (op == MtpOperation.RENAME) {
                        z = false;
                    }
                    removeObjectFromCache(obj, z, false);
                    break;
                default:
                    Log.e(TAG, "Got unexpected object remove for " + obj.getName());
                    break;
            }
        }
        if (sDebug) {
            Log.i(TAG, state + " transitioned to " + obj.getState() + " in op " + op);
        }
    }

    public synchronized void flushEvents() {
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
        }
    }

    public synchronized void dump() {
        for (Integer num : this.mObjects.keySet()) {
            int key = num.intValue();
            MtpObject obj = this.mObjects.get(Integer.valueOf(key));
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(key);
            sb.append(" | ");
            sb.append(obj.getParent() == null ? Integer.valueOf(obj.getParent().getId()) : "null");
            sb.append(" | ");
            sb.append(obj.getName());
            sb.append(" | ");
            sb.append(obj.isDir() ? "dir" : "obj");
            sb.append(" | ");
            sb.append(obj.isVisited() ? Telephony.BaseMmsColumns.MMS_VERSION : "nv");
            sb.append(" | ");
            sb.append(obj.getState());
            Log.i(str, sb.toString());
        }
    }

    public synchronized boolean checkConsistency() {
        boolean ret;
        Stream<MtpObject> objs = Stream.concat(this.mRoots.values().stream(), this.mObjects.values().stream());
        ret = true;
        for (MtpObject obj : objs) {
            if (!obj.exists()) {
                Log.w(TAG, "Object doesn't exist " + obj.getPath() + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + obj.getId());
                ret = false;
            }
            if (obj.getState() != MtpObjectState.NORMAL) {
                Log.w(TAG, "Object " + obj.getPath() + " in state " + obj.getState());
                ret = false;
            }
            if (obj.getOperation() != MtpOperation.NONE) {
                Log.w(TAG, "Object " + obj.getPath() + " in operation " + obj.getOperation());
                ret = false;
            }
            if (!obj.isRoot() && this.mObjects.get(Integer.valueOf(obj.getId())) != obj) {
                Log.w(TAG, "Object " + obj.getPath() + " is not in map correctly");
                ret = false;
            }
            if (obj.getParent() != null) {
                if (obj.getParent().isRoot() && obj.getParent() != this.mRoots.get(Integer.valueOf(obj.getParent().getId()))) {
                    Log.w(TAG, "Root parent is not in root mapping " + obj.getPath());
                    ret = false;
                }
                if (!obj.getParent().isRoot() && obj.getParent() != this.mObjects.get(Integer.valueOf(obj.getParent().getId()))) {
                    Log.w(TAG, "Parent is not in object mapping " + obj.getPath());
                    ret = false;
                }
                if (obj.getParent().getChild(obj.getName()) != obj) {
                    Log.w(TAG, "Child does not exist in parent " + obj.getPath());
                    ret = false;
                }
            }
            if (obj.isDir()) {
                if (obj.isVisited() == (obj.getObserver() == null)) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append(obj.getPath());
                    sb.append(" is ");
                    sb.append(obj.isVisited() ? "" : "not ");
                    sb.append(" visited but observer is ");
                    sb.append(obj.getObserver());
                    Log.w(str, sb.toString());
                    ret = false;
                }
                if (!obj.isVisited() && obj.getChildren().size() > 0) {
                    Log.w(TAG, obj.getPath() + " is not visited but has children");
                    ret = false;
                }
                try {
                    DirectoryStream<Path> stream = Files.newDirectoryStream(obj.getPath());
                    try {
                        Set<String> files = new HashSet<>();
                        for (Path file : stream) {
                            if (obj.isVisited() && obj.getChild(file.getFileName().toString()) == null && (this.mSubdirectories == null || !obj.isRoot() || this.mSubdirectories.contains(file.getFileName().toString()))) {
                                Log.w(TAG, "File exists in fs but not in children " + file);
                                ret = false;
                            }
                            files.add(file.toString());
                        }
                        for (MtpObject child : obj.getChildren()) {
                            if (!files.contains(child.getPath().toString())) {
                                Log.w(TAG, "File in children doesn't exist in fs " + child.getPath());
                                ret = false;
                            }
                            if (child != this.mObjects.get(Integer.valueOf(child.getId()))) {
                                Log.w(TAG, "Child is not in object map " + child.getPath());
                                ret = false;
                            }
                        }
                        if (stream != null) {
                            $closeResource(null, stream);
                        }
                    } catch (Throwable th) {
                        try {
                            throw th;
                            break;
                        } catch (Throwable th2) {
                            if (stream != null) {
                                $closeResource(th, stream);
                            }
                            throw th2;
                            break;
                        }
                    }
                } catch (IOException | DirectoryIteratorException e) {
                    Log.w(TAG, e.toString());
                    ret = false;
                }
            }
        }
        return ret;
    }

    public synchronized int beginSendObject(MtpObject parent, String name, int format) {
        if (sDebug) {
            String str = TAG;
            Log.v(str, "beginSendObject " + name);
        }
        if (parent.isDir()) {
            if (!parent.isRoot() || this.mSubdirectories == null || this.mSubdirectories.contains(name)) {
                getChildren(parent);
                MtpObject obj = addObjectToCache(parent, name, format == 12289);
                if (obj == null) {
                    return -1;
                }
                obj.setState(MtpObjectState.FROZEN);
                obj.setOperation(MtpOperation.ADD);
                return obj.getId();
            }
            return -1;
        }
        return -1;
    }

    public synchronized boolean endSendObject(MtpObject obj, boolean succeeded) {
        if (sDebug) {
            String str = TAG;
            Log.v(str, "endSendObject " + succeeded);
        }
        return generalEndAddObject(obj, succeeded, true);
    }

    public synchronized boolean beginRenameObject(MtpObject obj, String newName) {
        if (sDebug) {
            String str = TAG;
            Log.v(str, "beginRenameObject " + obj.getName() + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + newName);
        }
        if (obj.isRoot()) {
            return false;
        }
        if (isSpecialSubDir(obj)) {
            return false;
        }
        if (obj.getParent().getChild(newName) != null) {
            return false;
        }
        MtpObject oldObj = obj.copy(false);
        obj.setName(newName);
        obj.getParent().addChild(obj);
        oldObj.getParent().addChild(oldObj);
        return generalBeginRenameObject(oldObj, obj);
    }

    public synchronized boolean endRenameObject(MtpObject obj, String oldName, boolean success) {
        MtpObject oldObj;
        if (sDebug) {
            String str = TAG;
            Log.v(str, "endRenameObject " + success);
        }
        MtpObject parent = obj.getParent();
        oldObj = parent.getChild(oldName);
        if (!success) {
            MtpObjectState oldState = oldObj.getState();
            oldObj.setName(obj.getName());
            oldObj.setState(obj.getState());
            oldObj = obj;
            oldObj.setName(oldName);
            oldObj.setState(oldState);
            obj = oldObj;
            parent.addChild(obj);
            parent.addChild(oldObj);
        }
        return generalEndRenameObject(oldObj, obj, success);
    }

    public synchronized boolean beginRemoveObject(MtpObject obj) {
        boolean z;
        if (sDebug) {
            String str = TAG;
            Log.v(str, "beginRemoveObject " + obj.getName());
        }
        if (!obj.isRoot() && !isSpecialSubDir(obj)) {
            z = generalBeginRemoveObject(obj, MtpOperation.DELETE);
        }
        return z;
    }

    public synchronized boolean endRemoveObject(MtpObject obj, boolean success) {
        boolean z;
        if (sDebug) {
            Log.v(TAG, "endRemoveObject " + success);
        }
        boolean ret = true;
        z = false;
        if (obj.isDir()) {
            Iterator it = new ArrayList(obj.getChildren()).iterator();
            while (it.hasNext()) {
                MtpObject child = (MtpObject) it.next();
                if (child.getOperation() == MtpOperation.DELETE) {
                    ret = endRemoveObject(child, success) && ret;
                }
            }
        }
        if (generalEndRemoveObject(obj, success, true) && ret) {
            z = true;
        }
        return z;
    }

    public synchronized boolean beginMoveObject(MtpObject obj, MtpObject newParent) {
        if (sDebug) {
            Log.v(TAG, "beginMoveObject " + newParent.getPath());
        }
        if (obj.isRoot()) {
            return false;
        }
        if (isSpecialSubDir(obj)) {
            return false;
        }
        getChildren(newParent);
        if (newParent.getChild(obj.getName()) != null) {
            return false;
        }
        if (obj.getStorageId() == newParent.getStorageId()) {
            MtpObject oldObj = obj.copy(false);
            obj.setParent(newParent);
            oldObj.getParent().addChild(oldObj);
            obj.getParent().addChild(obj);
            return generalBeginRenameObject(oldObj, obj);
        }
        boolean z = true;
        MtpObject newObj = obj.copy(true);
        newObj.setParent(newParent);
        newParent.addChild(newObj);
        if (!generalBeginRemoveObject(obj, MtpOperation.RENAME) || !generalBeginCopyObject(newObj, false)) {
            z = false;
        }
        return z;
    }

    public synchronized boolean endMoveObject(MtpObject oldParent, MtpObject newParent, String name, boolean success) {
        if (sDebug) {
            Log.v(TAG, "endMoveObject " + success);
        }
        MtpObject oldObj = oldParent.getChild(name);
        MtpObject newObj = newParent.getChild(name);
        boolean z = false;
        if (oldObj != null && newObj != null) {
            if (oldParent.getStorageId() != newObj.getStorageId()) {
                boolean ret = endRemoveObject(oldObj, success);
                if (generalEndCopyObject(newObj, success, true) && ret) {
                    z = true;
                }
                return z;
            }
            if (!success) {
                MtpObjectState oldState = oldObj.getState();
                oldObj.setParent(newObj.getParent());
                oldObj.setState(newObj.getState());
                oldObj = newObj;
                oldObj.setParent(oldParent);
                oldObj.setState(oldState);
                newObj = oldObj;
                newObj.getParent().addChild(newObj);
                oldParent.addChild(oldObj);
            }
            return generalEndRenameObject(oldObj, newObj, success);
        }
        return false;
    }

    public synchronized int beginCopyObject(MtpObject object, MtpObject newParent) {
        if (sDebug) {
            String str = TAG;
            Log.v(str, "beginCopyObject " + object.getName() + " to " + newParent.getPath());
        }
        String name = object.getName();
        if (newParent.isDir()) {
            if (!newParent.isRoot() || this.mSubdirectories == null || this.mSubdirectories.contains(name)) {
                getChildren(newParent);
                if (newParent.getChild(name) != null) {
                    return -1;
                }
                MtpObject newObj = object.copy(object.isDir());
                newParent.addChild(newObj);
                newObj.setParent(newParent);
                if (generalBeginCopyObject(newObj, true)) {
                    return newObj.getId();
                }
                return -1;
            }
            return -1;
        }
        return -1;
    }

    public synchronized boolean endCopyObject(MtpObject object, boolean success) {
        if (sDebug) {
            String str = TAG;
            Log.v(str, "endCopyObject " + object.getName() + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + success);
        }
        return generalEndCopyObject(object, success, false);
    }

    private synchronized boolean generalEndAddObject(MtpObject obj, boolean succeeded, boolean removeGlobal) {
        int i = AnonymousClass1.$SwitchMap$android$mtp$MtpStorageManager$MtpObjectState[obj.getState().ordinal()];
        if (i == 5) {
            obj.setState(MtpObjectState.NORMAL);
            if (!succeeded) {
                MtpObject parent = obj.getParent();
                if (!removeObjectFromCache(obj, removeGlobal, false)) {
                    return false;
                }
                handleAddedObject(parent, obj.getName(), obj.isDir());
            }
        } else {
            switch (i) {
                case 1:
                    if (succeeded) {
                        obj.setState(MtpObjectState.FROZEN_ONESHOT_ADD);
                        break;
                    } else if (!removeObjectFromCache(obj, removeGlobal, false)) {
                        return false;
                    }
                    break;
                case 2:
                    if (!removeObjectFromCache(obj, removeGlobal, false)) {
                        return false;
                    }
                    if (succeeded) {
                        this.mMtpNotifier.sendObjectRemoved(obj.getId());
                        break;
                    }
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    private synchronized boolean generalEndRemoveObject(MtpObject obj, boolean success, boolean removeGlobal) {
        int i = AnonymousClass1.$SwitchMap$android$mtp$MtpStorageManager$MtpObjectState[obj.getState().ordinal()];
        if (i == 5) {
            obj.setState(MtpObjectState.NORMAL);
            if (success) {
                MtpObject parent = obj.getParent();
                if (!removeObjectFromCache(obj, removeGlobal, false)) {
                    return false;
                }
                handleAddedObject(parent, obj.getName(), obj.isDir());
            }
        } else {
            switch (i) {
                case 1:
                    if (success) {
                        obj.setState(MtpObjectState.FROZEN_ONESHOT_DEL);
                        break;
                    } else {
                        obj.setState(MtpObjectState.NORMAL);
                        break;
                    }
                case 2:
                    if (!removeObjectFromCache(obj, removeGlobal, false)) {
                        return false;
                    }
                    if (!success) {
                        this.mMtpNotifier.sendObjectRemoved(obj.getId());
                        break;
                    }
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    private synchronized boolean generalBeginRenameObject(MtpObject fromObj, MtpObject toObj) {
        fromObj.setState(MtpObjectState.FROZEN);
        toObj.setState(MtpObjectState.FROZEN);
        fromObj.setOperation(MtpOperation.RENAME);
        toObj.setOperation(MtpOperation.RENAME);
        return true;
    }

    private synchronized boolean generalEndRenameObject(MtpObject fromObj, MtpObject toObj, boolean success) {
        boolean ret;
        ret = generalEndRemoveObject(fromObj, success, !success);
        return generalEndAddObject(toObj, success, success) && ret;
    }

    private synchronized boolean generalBeginRemoveObject(MtpObject obj, MtpOperation op) {
        obj.setState(MtpObjectState.FROZEN);
        obj.setOperation(op);
        if (obj.isDir()) {
            for (MtpObject child : obj.getChildren()) {
                generalBeginRemoveObject(child, op);
            }
        }
        return true;
    }

    private synchronized boolean generalBeginCopyObject(MtpObject obj, boolean newId) {
        obj.setState(MtpObjectState.FROZEN);
        obj.setOperation(MtpOperation.COPY);
        if (newId) {
            obj.setId(getNextObjectId());
            this.mObjects.put(Integer.valueOf(obj.getId()), obj);
        }
        if (obj.isDir()) {
            for (MtpObject child : obj.getChildren()) {
                if (!generalBeginCopyObject(child, newId)) {
                    return false;
                }
            }
        }
        return true;
    }

    private synchronized boolean generalEndCopyObject(MtpObject obj, boolean success, boolean addGlobal) {
        boolean ret;
        boolean z;
        if (success && addGlobal) {
            this.mObjects.put(Integer.valueOf(obj.getId()), obj);
        }
        boolean ret2 = true;
        ret = false;
        if (obj.isDir()) {
            Iterator it = new ArrayList(obj.getChildren()).iterator();
            while (it.hasNext()) {
                MtpObject child = (MtpObject) it.next();
                if (child.getOperation() == MtpOperation.COPY) {
                    ret2 = generalEndCopyObject(child, success, addGlobal) && ret2;
                }
            }
        }
        if (!success && addGlobal) {
            z = false;
            if (generalEndAddObject(obj, success, z) && ret2) {
                ret = true;
            }
        }
        z = true;
        if (generalEndAddObject(obj, success, z)) {
            ret = true;
        }
        return ret;
    }
}
