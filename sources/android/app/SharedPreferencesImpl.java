package android.app;

import android.app.SharedPreferencesImpl;
import android.content.SharedPreferences;
import android.os.FileUtils;
import android.os.Looper;
import android.system.ErrnoException;
import android.system.Os;
import android.system.StructStat;
import android.system.StructTimespec;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.ExponentiallyBucketedHistogram;
import com.android.internal.util.XmlUtils;
import dalvik.system.BlockGuard;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CountDownLatch;
import libcore.io.IoUtils;
import org.xmlpull.v1.XmlPullParserException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class SharedPreferencesImpl implements SharedPreferences {
    private static final Object CONTENT = new Object();
    private static final boolean DEBUG = false;
    private static final long MAX_FSYNC_DURATION_MILLIS = 256;
    private static final String TAG = "SharedPreferencesImpl";
    private final File mBackupFile;
    @GuardedBy("this")
    private long mCurrentMemoryStateGeneration;
    @GuardedBy("mWritingToDiskLock")
    private long mDiskStateGeneration;
    public protected final File mFile;
    @GuardedBy("mLock")
    private boolean mLoaded;
    private final int mMode;
    @GuardedBy("mLock")
    private long mStatSize;
    @GuardedBy("mLock")
    private StructTimespec mStatTimestamp;
    private final Object mLock = new Object();
    private final Object mWritingToDiskLock = new Object();
    @GuardedBy("mLock")
    private int mDiskWritesInFlight = 0;
    @GuardedBy("mLock")
    private final WeakHashMap<SharedPreferences.OnSharedPreferenceChangeListener, Object> mListeners = new WeakHashMap<>();
    @GuardedBy("mWritingToDiskLock")
    private final ExponentiallyBucketedHistogram mSyncTimes = new ExponentiallyBucketedHistogram(16);
    private int mNumSync = 0;
    @GuardedBy("mLock")
    private Map<String, Object> mMap = null;
    @GuardedBy("mLock")
    private Throwable mThrowable = null;

    static /* synthetic */ int access$308(SharedPreferencesImpl x0) {
        int i = x0.mDiskWritesInFlight;
        x0.mDiskWritesInFlight = i + 1;
        return i;
    }

    static /* synthetic */ int access$310(SharedPreferencesImpl x0) {
        int i = x0.mDiskWritesInFlight;
        x0.mDiskWritesInFlight = i - 1;
        return i;
    }

    static /* synthetic */ long access$608(SharedPreferencesImpl x0) {
        long j = x0.mCurrentMemoryStateGeneration;
        x0.mCurrentMemoryStateGeneration = 1 + j;
        return j;
    }

    public private protected SharedPreferencesImpl(File file, int mode) {
        this.mLoaded = false;
        this.mFile = file;
        this.mBackupFile = makeBackupFile(file);
        this.mMode = mode;
        this.mLoaded = false;
        startLoadFromDisk();
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [android.app.SharedPreferencesImpl$1] */
    public protected void startLoadFromDisk() {
        synchronized (this.mLock) {
            this.mLoaded = false;
        }
        new Thread("SharedPreferencesImpl-load") { // from class: android.app.SharedPreferencesImpl.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                SharedPreferencesImpl.this.loadFromDisk();
            }
        }.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void loadFromDisk() {
        synchronized (this.mLock) {
            if (this.mLoaded) {
                return;
            }
            if (this.mBackupFile.exists()) {
                this.mFile.delete();
                this.mBackupFile.renameTo(this.mFile);
            }
            if (this.mFile.exists() && !this.mFile.canRead()) {
                Log.w(TAG, "Attempt to read preferences file " + this.mFile + " without permission");
            }
            Map<String, Object> map = null;
            StructStat stat = null;
            BufferedInputStream str = null;
            Throwable thrown = null;
            try {
                stat = Os.stat(this.mFile.getPath());
                try {
                    if (this.mFile.canRead()) {
                        try {
                            str = new BufferedInputStream(new FileInputStream(this.mFile), 16384);
                            map = XmlUtils.readMapXml(str);
                        } catch (Exception e) {
                            Log.w(TAG, "Cannot read " + this.mFile.getAbsolutePath(), e);
                            IoUtils.closeQuietly(str);
                        }
                    }
                } finally {
                }
            } catch (ErrnoException e2) {
            } catch (Throwable t) {
                thrown = t;
            }
            StructStat stat2 = stat;
            Map<String, Object> map2 = map;
            synchronized (this.mLock) {
                this.mLoaded = true;
                this.mThrowable = thrown;
                if (thrown == null) {
                    if (map2 != null) {
                        this.mMap = map2;
                        this.mStatTimestamp = stat2.st_mtim;
                        this.mStatSize = stat2.st_size;
                    } else {
                        this.mMap = new HashMap();
                    }
                }
                this.mLock.notifyAll();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized File makeBackupFile(File prefsFile) {
        return new File(prefsFile.getPath() + ".bak");
    }

    public private protected void startReloadIfChangedUnexpectedly() {
        synchronized (this.mLock) {
            if (hasFileChangedUnexpectedly()) {
                startLoadFromDisk();
            }
        }
    }

    private synchronized boolean hasFileChangedUnexpectedly() {
        synchronized (this.mLock) {
            if (this.mDiskWritesInFlight > 0) {
                return false;
            }
            boolean z = true;
            try {
                BlockGuard.getThreadPolicy().onReadFromDisk();
                StructStat stat = Os.stat(this.mFile.getPath());
                synchronized (this.mLock) {
                    if (stat.st_mtim.equals(this.mStatTimestamp) && this.mStatSize == stat.st_size) {
                        z = false;
                    }
                }
                return z;
            } catch (ErrnoException e) {
                return true;
            }
        }
    }

    @Override // android.content.SharedPreferences
    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        synchronized (this.mLock) {
            this.mListeners.put(listener, CONTENT);
        }
    }

    @Override // android.content.SharedPreferences
    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        synchronized (this.mLock) {
            this.mListeners.remove(listener);
        }
    }

    @GuardedBy("mLock")
    private synchronized void awaitLoadedLocked() {
        if (!this.mLoaded) {
            BlockGuard.getThreadPolicy().onReadFromDisk();
        }
        while (!this.mLoaded) {
            try {
                this.mLock.wait();
            } catch (InterruptedException e) {
            }
        }
        if (this.mThrowable != null) {
            throw new IllegalStateException(this.mThrowable);
        }
    }

    @Override // android.content.SharedPreferences
    public Map<String, ?> getAll() {
        HashMap hashMap;
        synchronized (this.mLock) {
            awaitLoadedLocked();
            hashMap = new HashMap(this.mMap);
        }
        return hashMap;
    }

    @Override // android.content.SharedPreferences
    public String getString(String key, String defValue) {
        String str;
        synchronized (this.mLock) {
            awaitLoadedLocked();
            String v = (String) this.mMap.get(key);
            str = v != null ? v : defValue;
        }
        return str;
    }

    @Override // android.content.SharedPreferences
    public Set<String> getStringSet(String key, Set<String> defValues) {
        Set<String> set;
        synchronized (this.mLock) {
            awaitLoadedLocked();
            Set<String> v = (Set) this.mMap.get(key);
            set = v != null ? v : defValues;
        }
        return set;
    }

    @Override // android.content.SharedPreferences
    public int getInt(String key, int defValue) {
        int intValue;
        synchronized (this.mLock) {
            awaitLoadedLocked();
            Integer v = (Integer) this.mMap.get(key);
            intValue = v != null ? v.intValue() : defValue;
        }
        return intValue;
    }

    @Override // android.content.SharedPreferences
    public long getLong(String key, long defValue) {
        long longValue;
        synchronized (this.mLock) {
            awaitLoadedLocked();
            Long v = (Long) this.mMap.get(key);
            longValue = v != null ? v.longValue() : defValue;
        }
        return longValue;
    }

    @Override // android.content.SharedPreferences
    public float getFloat(String key, float defValue) {
        float floatValue;
        synchronized (this.mLock) {
            awaitLoadedLocked();
            Float v = (Float) this.mMap.get(key);
            floatValue = v != null ? v.floatValue() : defValue;
        }
        return floatValue;
    }

    @Override // android.content.SharedPreferences
    public boolean getBoolean(String key, boolean defValue) {
        boolean booleanValue;
        synchronized (this.mLock) {
            awaitLoadedLocked();
            Boolean v = (Boolean) this.mMap.get(key);
            booleanValue = v != null ? v.booleanValue() : defValue;
        }
        return booleanValue;
    }

    @Override // android.content.SharedPreferences
    public boolean contains(String key) {
        boolean containsKey;
        synchronized (this.mLock) {
            awaitLoadedLocked();
            containsKey = this.mMap.containsKey(key);
        }
        return containsKey;
    }

    @Override // android.content.SharedPreferences
    public SharedPreferences.Editor edit() {
        synchronized (this.mLock) {
            awaitLoadedLocked();
        }
        return new EditorImpl();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class MemoryCommitResult {
        final List<String> keysModified;
        final Set<SharedPreferences.OnSharedPreferenceChangeListener> listeners;
        final Map<String, Object> mapToWriteToDisk;
        final long memoryStateGeneration;
        boolean wasWritten;
        @GuardedBy("mWritingToDiskLock")
        volatile boolean writeToDiskResult;
        final CountDownLatch writtenToDiskLatch;

        private synchronized MemoryCommitResult(long memoryStateGeneration, List<String> keysModified, Set<SharedPreferences.OnSharedPreferenceChangeListener> listeners, Map<String, Object> mapToWriteToDisk) {
            this.writtenToDiskLatch = new CountDownLatch(1);
            this.writeToDiskResult = false;
            this.wasWritten = false;
            this.memoryStateGeneration = memoryStateGeneration;
            this.keysModified = keysModified;
            this.listeners = listeners;
            this.mapToWriteToDisk = mapToWriteToDisk;
        }

        synchronized void setDiskWriteResult(boolean wasWritten, boolean result) {
            this.wasWritten = wasWritten;
            this.writeToDiskResult = result;
            this.writtenToDiskLatch.countDown();
        }
    }

    /* loaded from: classes.dex */
    public final class EditorImpl implements SharedPreferences.Editor {
        private final Object mEditorLock = new Object();
        @GuardedBy("mEditorLock")
        private final Map<String, Object> mModified = new HashMap();
        @GuardedBy("mEditorLock")
        private boolean mClear = false;

        public EditorImpl() {
        }

        @Override // android.content.SharedPreferences.Editor
        public SharedPreferences.Editor putString(String key, String value) {
            synchronized (this.mEditorLock) {
                this.mModified.put(key, value);
            }
            return this;
        }

        @Override // android.content.SharedPreferences.Editor
        public SharedPreferences.Editor putStringSet(String key, Set<String> values) {
            synchronized (this.mEditorLock) {
                this.mModified.put(key, values == null ? null : new HashSet(values));
            }
            return this;
        }

        @Override // android.content.SharedPreferences.Editor
        public SharedPreferences.Editor putInt(String key, int value) {
            synchronized (this.mEditorLock) {
                this.mModified.put(key, Integer.valueOf(value));
            }
            return this;
        }

        @Override // android.content.SharedPreferences.Editor
        public SharedPreferences.Editor putLong(String key, long value) {
            synchronized (this.mEditorLock) {
                this.mModified.put(key, Long.valueOf(value));
            }
            return this;
        }

        @Override // android.content.SharedPreferences.Editor
        public SharedPreferences.Editor putFloat(String key, float value) {
            synchronized (this.mEditorLock) {
                this.mModified.put(key, Float.valueOf(value));
            }
            return this;
        }

        @Override // android.content.SharedPreferences.Editor
        public SharedPreferences.Editor putBoolean(String key, boolean value) {
            synchronized (this.mEditorLock) {
                this.mModified.put(key, Boolean.valueOf(value));
            }
            return this;
        }

        @Override // android.content.SharedPreferences.Editor
        public SharedPreferences.Editor remove(String key) {
            synchronized (this.mEditorLock) {
                this.mModified.put(key, this);
            }
            return this;
        }

        @Override // android.content.SharedPreferences.Editor
        public SharedPreferences.Editor clear() {
            synchronized (this.mEditorLock) {
                this.mClear = true;
            }
            return this;
        }

        @Override // android.content.SharedPreferences.Editor
        public void apply() {
            final long startTime = System.currentTimeMillis();
            final MemoryCommitResult mcr = commitToMemory();
            final Runnable awaitCommit = new Runnable() { // from class: android.app.SharedPreferencesImpl.EditorImpl.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        mcr.writtenToDiskLatch.await();
                    } catch (InterruptedException e) {
                    }
                }
            };
            QueuedWork.addFinisher(awaitCommit);
            Runnable postWriteRunnable = new Runnable() { // from class: android.app.SharedPreferencesImpl.EditorImpl.2
                @Override // java.lang.Runnable
                public void run() {
                    awaitCommit.run();
                    QueuedWork.removeFinisher(awaitCommit);
                }
            };
            SharedPreferencesImpl.this.enqueueDiskWrite(mcr, postWriteRunnable);
            notifyListeners(mcr);
        }

        /* JADX WARN: Removed duplicated region for block: B:43:0x00b2 A[Catch: all -> 0x00d5, TryCatch #1 {, blocks: (B:4:0x0009, B:6:0x0011, B:7:0x0021, B:12:0x003f, B:13:0x0055, B:14:0x0057, B:50:0x00ca, B:16:0x0059, B:18:0x005d, B:20:0x0063, B:21:0x0067, B:22:0x0069, B:23:0x0073, B:25:0x0079, B:29:0x008e, B:31:0x0094, B:33:0x009a, B:36:0x00a1, B:43:0x00b2, B:37:0x00a5, B:40:0x00ac, B:45:0x00b6, B:47:0x00bd, B:48:0x00c2, B:49:0x00c9), top: B:59:0x0009 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private synchronized android.app.SharedPreferencesImpl.MemoryCommitResult commitToMemory() {
            /*
                r13 = this;
                r0 = 0
                r1 = 0
                android.app.SharedPreferencesImpl r2 = android.app.SharedPreferencesImpl.this
                java.lang.Object r2 = android.app.SharedPreferencesImpl.access$200(r2)
                monitor-enter(r2)
                android.app.SharedPreferencesImpl r3 = android.app.SharedPreferencesImpl.this     // Catch: java.lang.Throwable -> Ld8
                int r3 = android.app.SharedPreferencesImpl.access$300(r3)     // Catch: java.lang.Throwable -> Ld8
                if (r3 <= 0) goto L21
                android.app.SharedPreferencesImpl r3 = android.app.SharedPreferencesImpl.this     // Catch: java.lang.Throwable -> Ld8
                java.util.HashMap r4 = new java.util.HashMap     // Catch: java.lang.Throwable -> Ld8
                android.app.SharedPreferencesImpl r5 = android.app.SharedPreferencesImpl.this     // Catch: java.lang.Throwable -> Ld8
                java.util.Map r5 = android.app.SharedPreferencesImpl.access$400(r5)     // Catch: java.lang.Throwable -> Ld8
                r4.<init>(r5)     // Catch: java.lang.Throwable -> Ld8
                android.app.SharedPreferencesImpl.access$402(r3, r4)     // Catch: java.lang.Throwable -> Ld8
            L21:
                android.app.SharedPreferencesImpl r3 = android.app.SharedPreferencesImpl.this     // Catch: java.lang.Throwable -> Ld8
                java.util.Map r3 = android.app.SharedPreferencesImpl.access$400(r3)     // Catch: java.lang.Throwable -> Ld8
                r9 = r3
                android.app.SharedPreferencesImpl r3 = android.app.SharedPreferencesImpl.this     // Catch: java.lang.Throwable -> Ld8
                android.app.SharedPreferencesImpl.access$308(r3)     // Catch: java.lang.Throwable -> Ld8
                android.app.SharedPreferencesImpl r3 = android.app.SharedPreferencesImpl.this     // Catch: java.lang.Throwable -> Ld8
                java.util.WeakHashMap r3 = android.app.SharedPreferencesImpl.access$500(r3)     // Catch: java.lang.Throwable -> Ld8
                int r3 = r3.size()     // Catch: java.lang.Throwable -> Ld8
                r4 = 0
                if (r3 <= 0) goto L3c
                r3 = 1
                goto L3d
            L3c:
                r3 = r4
            L3d:
                if (r3 == 0) goto L55
                java.util.ArrayList r5 = new java.util.ArrayList     // Catch: java.lang.Throwable -> Ld8
                r5.<init>()     // Catch: java.lang.Throwable -> Ld8
                r0 = r5
                java.util.HashSet r5 = new java.util.HashSet     // Catch: java.lang.Throwable -> Ld8
                android.app.SharedPreferencesImpl r6 = android.app.SharedPreferencesImpl.this     // Catch: java.lang.Throwable -> Ld8
                java.util.WeakHashMap r6 = android.app.SharedPreferencesImpl.access$500(r6)     // Catch: java.lang.Throwable -> Ld8
                java.util.Set r6 = r6.keySet()     // Catch: java.lang.Throwable -> Ld8
                r5.<init>(r6)     // Catch: java.lang.Throwable -> Ld8
                r1 = r5
            L55:
                java.lang.Object r7 = r13.mEditorLock     // Catch: java.lang.Throwable -> Ld8
                monitor-enter(r7)     // Catch: java.lang.Throwable -> Ld8
                r5 = 0
                boolean r6 = r13.mClear     // Catch: java.lang.Throwable -> Ld5
                if (r6 == 0) goto L69
                boolean r6 = r9.isEmpty()     // Catch: java.lang.Throwable -> Ld5
                if (r6 != 0) goto L67
                r5 = 1
                r9.clear()     // Catch: java.lang.Throwable -> Ld5
            L67:
                r13.mClear = r4     // Catch: java.lang.Throwable -> Ld5
            L69:
                java.util.Map<java.lang.String, java.lang.Object> r4 = r13.mModified     // Catch: java.lang.Throwable -> Ld5
                java.util.Set r4 = r4.entrySet()     // Catch: java.lang.Throwable -> Ld5
                java.util.Iterator r4 = r4.iterator()     // Catch: java.lang.Throwable -> Ld5
            L73:
                boolean r6 = r4.hasNext()     // Catch: java.lang.Throwable -> Ld5
                if (r6 == 0) goto Lb6
                java.lang.Object r6 = r4.next()     // Catch: java.lang.Throwable -> Ld5
                java.util.Map$Entry r6 = (java.util.Map.Entry) r6     // Catch: java.lang.Throwable -> Ld5
                java.lang.Object r8 = r6.getKey()     // Catch: java.lang.Throwable -> Ld5
                java.lang.String r8 = (java.lang.String) r8     // Catch: java.lang.Throwable -> Ld5
                java.lang.Object r10 = r6.getValue()     // Catch: java.lang.Throwable -> Ld5
                if (r10 == r13) goto La5
                if (r10 != 0) goto L8e
                goto La5
            L8e:
                boolean r11 = r9.containsKey(r8)     // Catch: java.lang.Throwable -> Ld5
                if (r11 == 0) goto La1
                java.lang.Object r11 = r9.get(r8)     // Catch: java.lang.Throwable -> Ld5
                if (r11 == 0) goto La1
                boolean r12 = r11.equals(r10)     // Catch: java.lang.Throwable -> Ld5
                if (r12 == 0) goto La1
                goto L73
            La1:
                r9.put(r8, r10)     // Catch: java.lang.Throwable -> Ld5
                goto Laf
            La5:
                boolean r11 = r9.containsKey(r8)     // Catch: java.lang.Throwable -> Ld5
                if (r11 != 0) goto Lac
                goto L73
            Lac:
                r9.remove(r8)     // Catch: java.lang.Throwable -> Ld5
            Laf:
                r5 = 1
                if (r3 == 0) goto Lb5
                r0.add(r8)     // Catch: java.lang.Throwable -> Ld5
            Lb5:
                goto L73
            Lb6:
                java.util.Map<java.lang.String, java.lang.Object> r4 = r13.mModified     // Catch: java.lang.Throwable -> Ld5
                r4.clear()     // Catch: java.lang.Throwable -> Ld5
                if (r5 == 0) goto Lc2
                android.app.SharedPreferencesImpl r4 = android.app.SharedPreferencesImpl.this     // Catch: java.lang.Throwable -> Ld5
                android.app.SharedPreferencesImpl.access$608(r4)     // Catch: java.lang.Throwable -> Ld5
            Lc2:
                android.app.SharedPreferencesImpl r4 = android.app.SharedPreferencesImpl.this     // Catch: java.lang.Throwable -> Ld5
                long r10 = android.app.SharedPreferencesImpl.access$600(r4)     // Catch: java.lang.Throwable -> Ld5
                r5 = r10
                monitor-exit(r7)     // Catch: java.lang.Throwable -> Ld5
                monitor-exit(r2)     // Catch: java.lang.Throwable -> Ld8
                android.app.SharedPreferencesImpl$MemoryCommitResult r2 = new android.app.SharedPreferencesImpl$MemoryCommitResult
                r10 = 0
                r4 = r2
                r7 = r0
                r8 = r1
                r4.<init>(r5, r7, r8, r9)
                return r2
            Ld5:
                r4 = move-exception
                monitor-exit(r7)     // Catch: java.lang.Throwable -> Ld5
                throw r4     // Catch: java.lang.Throwable -> Ld8
            Ld8:
                r3 = move-exception
                monitor-exit(r2)     // Catch: java.lang.Throwable -> Ld8
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: android.app.SharedPreferencesImpl.EditorImpl.commitToMemory():android.app.SharedPreferencesImpl$MemoryCommitResult");
        }

        @Override // android.content.SharedPreferences.Editor
        public boolean commit() {
            MemoryCommitResult mcr = commitToMemory();
            SharedPreferencesImpl.this.enqueueDiskWrite(mcr, null);
            try {
                mcr.writtenToDiskLatch.await();
                notifyListeners(mcr);
                return mcr.writeToDiskResult;
            } catch (InterruptedException e) {
                return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void notifyListeners(final MemoryCommitResult mcr) {
            if (mcr.listeners == null || mcr.keysModified == null || mcr.keysModified.size() == 0) {
                return;
            }
            if (Looper.myLooper() == Looper.getMainLooper()) {
                for (int i = mcr.keysModified.size() - 1; i >= 0; i--) {
                    String key = mcr.keysModified.get(i);
                    for (SharedPreferences.OnSharedPreferenceChangeListener listener : mcr.listeners) {
                        if (listener != null) {
                            listener.onSharedPreferenceChanged(SharedPreferencesImpl.this, key);
                        }
                    }
                }
                return;
            }
            ActivityThread.sMainThreadHandler.post(new Runnable() { // from class: android.app.-$$Lambda$SharedPreferencesImpl$EditorImpl$3CAjkhzA131V3V-sLfP2uy0FWZ0
                @Override // java.lang.Runnable
                public final void run() {
                    SharedPreferencesImpl.EditorImpl.this.notifyListeners(mcr);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void enqueueDiskWrite(final MemoryCommitResult mcr, final Runnable postWriteRunnable) {
        boolean wasEmpty;
        final boolean isFromSyncCommit = postWriteRunnable == null;
        Runnable writeToDiskRunnable = new Runnable() { // from class: android.app.SharedPreferencesImpl.2
            @Override // java.lang.Runnable
            public void run() {
                synchronized (SharedPreferencesImpl.this.mWritingToDiskLock) {
                    SharedPreferencesImpl.this.writeToFile(mcr, isFromSyncCommit);
                }
                synchronized (SharedPreferencesImpl.this.mLock) {
                    SharedPreferencesImpl.access$310(SharedPreferencesImpl.this);
                }
                if (postWriteRunnable != null) {
                    postWriteRunnable.run();
                }
            }
        };
        if (isFromSyncCommit) {
            synchronized (this.mLock) {
                wasEmpty = this.mDiskWritesInFlight == 1;
            }
            if (wasEmpty) {
                writeToDiskRunnable.run();
                return;
            }
        }
        QueuedWork.queue(writeToDiskRunnable, isFromSyncCommit ? false : true);
    }

    private static synchronized FileOutputStream createFileOutputStream(File file) {
        try {
            FileOutputStream str = new FileOutputStream(file);
            return str;
        } catch (FileNotFoundException e) {
            File parent = file.getParentFile();
            if (!parent.mkdir()) {
                Log.e(TAG, "Couldn't create directory for SharedPreferences file " + file);
                return null;
            }
            FileUtils.setPermissions(parent.getPath(), 505, -1, -1);
            try {
                FileOutputStream str2 = new FileOutputStream(file);
                return str2;
            } catch (FileNotFoundException e2) {
                Log.e(TAG, "Couldn't create SharedPreferences file " + file, e2);
                return null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy("mWritingToDiskLock")
    public synchronized void writeToFile(MemoryCommitResult mcr, boolean isFromSyncCommit) {
        boolean fileExists = this.mFile.exists();
        if (fileExists) {
            long j = this.mDiskStateGeneration;
            long existsTime = mcr.memoryStateGeneration;
            if (j < existsTime) {
                if (isFromSyncCommit) {
                    needsWrite = true;
                } else {
                    synchronized (this.mLock) {
                        try {
                            try {
                                long j2 = this.mCurrentMemoryStateGeneration;
                                long backupExistsTime = mcr.memoryStateGeneration;
                                needsWrite = j2 == backupExistsTime;
                            } catch (Throwable th) {
                                th = th;
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            throw th;
                        }
                    }
                }
            }
            if (!needsWrite) {
                mcr.setDiskWriteResult(false, true);
                return;
            }
            boolean backupFileExists = this.mBackupFile.exists();
            if (backupFileExists) {
                this.mFile.delete();
            } else if (!this.mFile.renameTo(this.mBackupFile)) {
                Log.e(TAG, "Couldn't rename file " + this.mFile + " to backup file " + this.mBackupFile);
                mcr.setDiskWriteResult(false, false);
                return;
            }
        }
        try {
            FileOutputStream str = createFileOutputStream(this.mFile);
            if (str == null) {
                mcr.setDiskWriteResult(false, false);
                return;
            }
            XmlUtils.writeMapXml(mcr.mapToWriteToDisk, str);
            long writeTime = System.currentTimeMillis();
            FileUtils.sync(str);
            long fsyncTime = System.currentTimeMillis();
            str.close();
            ContextImpl.setFilePermissionsFromMode(this.mFile.getPath(), this.mMode, 0);
            try {
                StructStat stat = Os.stat(this.mFile.getPath());
                synchronized (this.mLock) {
                    this.mStatTimestamp = stat.st_mtim;
                    this.mStatSize = stat.st_size;
                }
            } catch (ErrnoException e) {
            }
            this.mBackupFile.delete();
            this.mDiskStateGeneration = mcr.memoryStateGeneration;
            mcr.setDiskWriteResult(true, true);
            long fsyncDuration = fsyncTime - writeTime;
            this.mSyncTimes.add((int) fsyncDuration);
            this.mNumSync++;
            if (this.mNumSync % 1024 == 0 || fsyncDuration > 256) {
                this.mSyncTimes.log(TAG, "Time required to fsync " + this.mFile + ": ");
            }
        } catch (IOException e2) {
            Log.w(TAG, "writeToFile: Got exception:", e2);
            if (this.mFile.exists() && !this.mFile.delete()) {
                Log.e(TAG, "Couldn't clean up partially-written file " + this.mFile);
            }
            mcr.setDiskWriteResult(false, false);
        } catch (XmlPullParserException e3) {
            Log.w(TAG, "writeToFile: Got exception:", e3);
            if (this.mFile.exists()) {
                Log.e(TAG, "Couldn't clean up partially-written file " + this.mFile);
            }
            mcr.setDiskWriteResult(false, false);
        }
    }
}
