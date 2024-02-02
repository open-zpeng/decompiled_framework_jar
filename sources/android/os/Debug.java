package android.os;

import android.app.AppGlobals;
import android.content.Context;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcelable;
import android.provider.SettingsStringUtil;
import android.util.Log;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.util.Preconditions;
import com.android.internal.util.TypedProperties;
import dalvik.system.VMDebug;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.apache.harmony.dalvik.ddmc.Chunk;
import org.apache.harmony.dalvik.ddmc.ChunkHandler;
import org.apache.harmony.dalvik.ddmc.DdmServer;
/* loaded from: classes2.dex */
public final class Debug {
    private static final String DEFAULT_TRACE_BODY = "dmtrace";
    private static final String DEFAULT_TRACE_EXTENSION = ".trace";
    public static final int MEMINFO_BUFFERS = 2;
    public static final int MEMINFO_CACHED = 3;
    public static final int MEMINFO_COUNT = 15;
    public static final int MEMINFO_FREE = 1;
    public static final int MEMINFO_KERNEL_STACK = 14;
    public static final int MEMINFO_MAPPED = 11;
    public static final int MEMINFO_PAGE_TABLES = 13;
    public static final int MEMINFO_SHMEM = 4;
    public static final int MEMINFO_SLAB = 5;
    public static final int MEMINFO_SLAB_RECLAIMABLE = 6;
    public static final int MEMINFO_SLAB_UNRECLAIMABLE = 7;
    public static final int MEMINFO_SWAP_FREE = 9;
    public static final int MEMINFO_SWAP_TOTAL = 8;
    public static final int MEMINFO_TOTAL = 0;
    public static final int MEMINFO_VM_ALLOC_USED = 12;
    public static final int MEMINFO_ZRAM_TOTAL = 10;
    private static final int MIN_DEBUGGER_IDLE = 1300;
    public static final int SHOW_CLASSLOADER = 2;
    public static final int SHOW_FULL_DETAIL = 1;
    public static final int SHOW_INITIALIZED = 4;
    private static final int SPIN_DELAY = 200;
    private static final String SYSFS_QEMU_TRACE_STATE = "/sys/qemu_trace/state";
    private static final String TAG = "Debug";
    @Deprecated
    public static final int TRACE_COUNT_ALLOCS = 1;
    private static volatile boolean mWaiting = false;
    private static final TypedProperties debugProperties = null;

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: classes2.dex */
    public @interface DebugProperty {
    }

    public static native boolean dumpJavaBacktraceToFileTimeout(int i, String str, int i2);

    public static native boolean dumpNativeBacktraceToFileTimeout(int i, String str, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dumpNativeHeap(FileDescriptor fileDescriptor);

    public static native void dumpNativeMallocInfo(FileDescriptor fileDescriptor);

    public static final native int getBinderDeathObjectCount();

    public static final native int getBinderLocalObjectCount();

    public static final native int getBinderProxyObjectCount();

    public static native int getBinderReceivedTransactions();

    public static native int getBinderSentTransactions();

    /* JADX INFO: Access modifiers changed from: private */
    public static native void getMemInfo(long[] jArr);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void getMemoryInfo(int i, MemoryInfo memoryInfo);

    public static native void getMemoryInfo(MemoryInfo memoryInfo);

    public static native long getNativeHeapAllocatedSize();

    public static native long getNativeHeapFreeSize();

    public static native long getNativeHeapSize();

    public static native long getPss();

    public static native long getPss(int i, long[] jArr, long[] jArr2);

    public static native String getUnreachableMemory(int i, boolean z);

    /* loaded from: classes2.dex */
    public static class MemoryInfo implements Parcelable {
        public static final Parcelable.Creator<MemoryInfo> CREATOR = new Parcelable.Creator<MemoryInfo>() { // from class: android.os.Debug.MemoryInfo.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public MemoryInfo createFromParcel(Parcel source) {
                return new MemoryInfo(source);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public MemoryInfo[] newArray(int size) {
                return new MemoryInfo[size];
            }
        };
        public static final int HEAP_DALVIK = 1;
        public static final int HEAP_NATIVE = 2;
        public static final int HEAP_UNKNOWN = 0;
        public static final int NUM_CATEGORIES = 9;
        private protected static final int NUM_DVK_STATS = 14;
        private protected static final int NUM_OTHER_STATS = 17;
        public static final int OFFSET_PRIVATE_CLEAN = 5;
        public static final int OFFSET_PRIVATE_DIRTY = 3;
        public static final int OFFSET_PSS = 0;
        public static final int OFFSET_RSS = 2;
        public static final int OFFSET_SHARED_CLEAN = 6;
        public static final int OFFSET_SHARED_DIRTY = 4;
        public static final int OFFSET_SWAPPABLE_PSS = 1;
        public static final int OFFSET_SWAPPED_OUT = 7;
        public static final int OFFSET_SWAPPED_OUT_PSS = 8;
        public static final int OTHER_APK = 8;
        public static final int OTHER_ART = 12;
        public static final int OTHER_ART_APP = 29;
        public static final int OTHER_ART_BOOT = 30;
        public static final int OTHER_ASHMEM = 3;
        public static final int OTHER_CURSOR = 2;
        public static final int OTHER_DALVIK_LARGE = 18;
        public static final int OTHER_DALVIK_NON_MOVING = 20;
        public static final int OTHER_DALVIK_NORMAL = 17;
        public static final int OTHER_DALVIK_OTHER = 0;
        public static final int OTHER_DALVIK_OTHER_ACCOUNTING = 22;
        public static final int OTHER_DALVIK_OTHER_CODE_CACHE = 23;
        public static final int OTHER_DALVIK_OTHER_COMPILER_METADATA = 24;
        public static final int OTHER_DALVIK_OTHER_INDIRECT_REFERENCE_TABLE = 25;
        public static final int OTHER_DALVIK_OTHER_LINEARALLOC = 21;
        public static final int OTHER_DALVIK_ZYGOTE = 19;
        public static final int OTHER_DEX = 10;
        public static final int OTHER_DEX_APP_DEX = 27;
        public static final int OTHER_DEX_APP_VDEX = 28;
        public static final int OTHER_DEX_BOOT_VDEX = 26;
        public static final int OTHER_DVK_STAT_ART_END = 13;
        public static final int OTHER_DVK_STAT_ART_START = 12;
        public static final int OTHER_DVK_STAT_DALVIK_END = 3;
        public static final int OTHER_DVK_STAT_DALVIK_OTHER_END = 8;
        public static final int OTHER_DVK_STAT_DALVIK_OTHER_START = 4;
        public static final int OTHER_DVK_STAT_DALVIK_START = 0;
        public static final int OTHER_DVK_STAT_DEX_END = 11;
        public static final int OTHER_DVK_STAT_DEX_START = 9;
        public static final int OTHER_GL = 15;
        public static final int OTHER_GL_DEV = 4;
        public static final int OTHER_GRAPHICS = 14;
        public static final int OTHER_JAR = 7;
        public static final int OTHER_OAT = 11;
        public static final int OTHER_OTHER_MEMTRACK = 16;
        public static final int OTHER_SO = 6;
        public static final int OTHER_STACK = 1;
        public static final int OTHER_TTF = 9;
        public static final int OTHER_UNKNOWN_DEV = 5;
        public static final int OTHER_UNKNOWN_MAP = 13;
        private protected int dalvikPrivateClean;
        public int dalvikPrivateDirty;
        public int dalvikPss;
        private protected int dalvikRss;
        private protected int dalvikSharedClean;
        public int dalvikSharedDirty;
        private protected int dalvikSwappablePss;
        private protected int dalvikSwappedOut;
        private protected int dalvikSwappedOutPss;
        private protected boolean hasSwappedOutPss;
        private protected int nativePrivateClean;
        public int nativePrivateDirty;
        public int nativePss;
        private protected int nativeRss;
        private protected int nativeSharedClean;
        public int nativeSharedDirty;
        private protected int nativeSwappablePss;
        private protected int nativeSwappedOut;
        private protected int nativeSwappedOutPss;
        private protected int otherPrivateClean;
        public int otherPrivateDirty;
        public int otherPss;
        private protected int otherRss;
        private protected int otherSharedClean;
        public int otherSharedDirty;
        public protected int[] otherStats;
        private protected int otherSwappablePss;
        private protected int otherSwappedOut;
        private protected int otherSwappedOutPss;

        public MemoryInfo() {
            this.otherStats = new int[279];
        }

        public int getTotalPss() {
            return this.dalvikPss + this.nativePss + this.otherPss + getTotalSwappedOutPss();
        }

        private protected int getTotalUss() {
            return this.dalvikPrivateClean + this.dalvikPrivateDirty + this.nativePrivateClean + this.nativePrivateDirty + this.otherPrivateClean + this.otherPrivateDirty;
        }

        public int getTotalSwappablePss() {
            return this.dalvikSwappablePss + this.nativeSwappablePss + this.otherSwappablePss;
        }

        public synchronized int getTotalRss() {
            return this.dalvikRss + this.nativeRss + this.otherRss;
        }

        public int getTotalPrivateDirty() {
            return this.dalvikPrivateDirty + this.nativePrivateDirty + this.otherPrivateDirty;
        }

        public int getTotalSharedDirty() {
            return this.dalvikSharedDirty + this.nativeSharedDirty + this.otherSharedDirty;
        }

        public int getTotalPrivateClean() {
            return this.dalvikPrivateClean + this.nativePrivateClean + this.otherPrivateClean;
        }

        public int getTotalSharedClean() {
            return this.dalvikSharedClean + this.nativeSharedClean + this.otherSharedClean;
        }

        public synchronized int getTotalSwappedOut() {
            return this.dalvikSwappedOut + this.nativeSwappedOut + this.otherSwappedOut;
        }

        public synchronized int getTotalSwappedOutPss() {
            return this.dalvikSwappedOutPss + this.nativeSwappedOutPss + this.otherSwappedOutPss;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getOtherPss(int which) {
            return this.otherStats[(which * 9) + 0];
        }

        public synchronized int getOtherSwappablePss(int which) {
            return this.otherStats[(which * 9) + 1];
        }

        public synchronized int getOtherRss(int which) {
            return this.otherStats[(which * 9) + 2];
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getOtherPrivateDirty(int which) {
            return this.otherStats[(which * 9) + 3];
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getOtherSharedDirty(int which) {
            return this.otherStats[(which * 9) + 4];
        }

        public synchronized int getOtherPrivateClean(int which) {
            return this.otherStats[(which * 9) + 5];
        }

        private protected int getOtherPrivate(int which) {
            return getOtherPrivateClean(which) + getOtherPrivateDirty(which);
        }

        public synchronized int getOtherSharedClean(int which) {
            return this.otherStats[(which * 9) + 6];
        }

        public synchronized int getOtherSwappedOut(int which) {
            return this.otherStats[(which * 9) + 7];
        }

        public synchronized int getOtherSwappedOutPss(int which) {
            return this.otherStats[(which * 9) + 8];
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static String getOtherLabel(int which) {
            switch (which) {
                case 0:
                    return "Dalvik Other";
                case 1:
                    return "Stack";
                case 2:
                    return "Cursor";
                case 3:
                    return "Ashmem";
                case 4:
                    return "Gfx dev";
                case 5:
                    return "Other dev";
                case 6:
                    return ".so mmap";
                case 7:
                    return ".jar mmap";
                case 8:
                    return ".apk mmap";
                case 9:
                    return ".ttf mmap";
                case 10:
                    return ".dex mmap";
                case 11:
                    return ".oat mmap";
                case 12:
                    return ".art mmap";
                case 13:
                    return "Other mmap";
                case 14:
                    return "EGL mtrack";
                case 15:
                    return "GL mtrack";
                case 16:
                    return "Other mtrack";
                case 17:
                    return ".Heap";
                case 18:
                    return ".LOS";
                case 19:
                    return ".Zygote";
                case 20:
                    return ".NonMoving";
                case 21:
                    return ".LinearAlloc";
                case 22:
                    return ".GC";
                case 23:
                    return ".JITCache";
                case 24:
                    return ".CompilerMetadata";
                case 25:
                    return ".IndirectRef";
                case 26:
                    return ".Boot vdex";
                case 27:
                    return ".App dex";
                case 28:
                    return ".App vdex";
                case 29:
                    return ".App art";
                case 30:
                    return ".Boot art";
                default:
                    return "????";
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        public String getMemoryStat(String statName) {
            char c;
            switch (statName.hashCode()) {
                case -1629983121:
                    if (statName.equals("summary.java-heap")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case -1318722433:
                    if (statName.equals("summary.total-pss")) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case -1086991874:
                    if (statName.equals("summary.private-other")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case -1040176230:
                    if (statName.equals("summary.native-heap")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case -675184064:
                    if (statName.equals("summary.stack")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 549300599:
                    if (statName.equals("summary.system")) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case 1640306485:
                    if (statName.equals("summary.code")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 2016489427:
                    if (statName.equals("summary.graphics")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 2069370308:
                    if (statName.equals("summary.total-swap")) {
                        c = '\b';
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                    return Integer.toString(getSummaryJavaHeap());
                case 1:
                    return Integer.toString(getSummaryNativeHeap());
                case 2:
                    return Integer.toString(getSummaryCode());
                case 3:
                    return Integer.toString(getSummaryStack());
                case 4:
                    return Integer.toString(getSummaryGraphics());
                case 5:
                    return Integer.toString(getSummaryPrivateOther());
                case 6:
                    return Integer.toString(getSummarySystem());
                case 7:
                    return Integer.toString(getSummaryTotalPss());
                case '\b':
                    return Integer.toString(getSummaryTotalSwap());
                default:
                    return null;
            }
        }

        public Map<String, String> getMemoryStats() {
            Map<String, String> stats = new HashMap<>();
            stats.put("summary.java-heap", Integer.toString(getSummaryJavaHeap()));
            stats.put("summary.native-heap", Integer.toString(getSummaryNativeHeap()));
            stats.put("summary.code", Integer.toString(getSummaryCode()));
            stats.put("summary.stack", Integer.toString(getSummaryStack()));
            stats.put("summary.graphics", Integer.toString(getSummaryGraphics()));
            stats.put("summary.private-other", Integer.toString(getSummaryPrivateOther()));
            stats.put("summary.system", Integer.toString(getSummarySystem()));
            stats.put("summary.total-pss", Integer.toString(getSummaryTotalPss()));
            stats.put("summary.total-swap", Integer.toString(getSummaryTotalSwap()));
            return stats;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getSummaryJavaHeap() {
            return this.dalvikPrivateDirty + getOtherPrivate(12);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getSummaryNativeHeap() {
            return this.nativePrivateDirty;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getSummaryCode() {
            return getOtherPrivate(6) + getOtherPrivate(7) + getOtherPrivate(8) + getOtherPrivate(9) + getOtherPrivate(10) + getOtherPrivate(11);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getSummaryStack() {
            return getOtherPrivateDirty(1);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getSummaryGraphics() {
            return getOtherPrivate(4) + getOtherPrivate(14) + getOtherPrivate(15);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getSummaryPrivateOther() {
            return (((((getTotalPrivateClean() + getTotalPrivateDirty()) - getSummaryJavaHeap()) - getSummaryNativeHeap()) - getSummaryCode()) - getSummaryStack()) - getSummaryGraphics();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getSummarySystem() {
            return (getTotalPss() - getTotalPrivateClean()) - getTotalPrivateDirty();
        }

        public synchronized int getSummaryTotalPss() {
            return getTotalPss();
        }

        public synchronized int getSummaryTotalSwap() {
            return getTotalSwappedOut();
        }

        public synchronized int getSummaryTotalSwapPss() {
            return getTotalSwappedOutPss();
        }

        public synchronized boolean hasSwappedOutPss() {
            return this.hasSwappedOutPss;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.dalvikPss);
            dest.writeInt(this.dalvikSwappablePss);
            dest.writeInt(this.dalvikRss);
            dest.writeInt(this.dalvikPrivateDirty);
            dest.writeInt(this.dalvikSharedDirty);
            dest.writeInt(this.dalvikPrivateClean);
            dest.writeInt(this.dalvikSharedClean);
            dest.writeInt(this.dalvikSwappedOut);
            dest.writeInt(this.dalvikSwappedOutPss);
            dest.writeInt(this.nativePss);
            dest.writeInt(this.nativeSwappablePss);
            dest.writeInt(this.nativeRss);
            dest.writeInt(this.nativePrivateDirty);
            dest.writeInt(this.nativeSharedDirty);
            dest.writeInt(this.nativePrivateClean);
            dest.writeInt(this.nativeSharedClean);
            dest.writeInt(this.nativeSwappedOut);
            dest.writeInt(this.nativeSwappedOutPss);
            dest.writeInt(this.otherPss);
            dest.writeInt(this.otherSwappablePss);
            dest.writeInt(this.otherRss);
            dest.writeInt(this.otherPrivateDirty);
            dest.writeInt(this.otherSharedDirty);
            dest.writeInt(this.otherPrivateClean);
            dest.writeInt(this.otherSharedClean);
            dest.writeInt(this.otherSwappedOut);
            dest.writeInt(this.hasSwappedOutPss ? 1 : 0);
            dest.writeInt(this.otherSwappedOutPss);
            dest.writeIntArray(this.otherStats);
        }

        public void readFromParcel(Parcel source) {
            this.dalvikPss = source.readInt();
            this.dalvikSwappablePss = source.readInt();
            this.dalvikRss = source.readInt();
            this.dalvikPrivateDirty = source.readInt();
            this.dalvikSharedDirty = source.readInt();
            this.dalvikPrivateClean = source.readInt();
            this.dalvikSharedClean = source.readInt();
            this.dalvikSwappedOut = source.readInt();
            this.dalvikSwappedOutPss = source.readInt();
            this.nativePss = source.readInt();
            this.nativeSwappablePss = source.readInt();
            this.nativeRss = source.readInt();
            this.nativePrivateDirty = source.readInt();
            this.nativeSharedDirty = source.readInt();
            this.nativePrivateClean = source.readInt();
            this.nativeSharedClean = source.readInt();
            this.nativeSwappedOut = source.readInt();
            this.nativeSwappedOutPss = source.readInt();
            this.otherPss = source.readInt();
            this.otherSwappablePss = source.readInt();
            this.otherRss = source.readInt();
            this.otherPrivateDirty = source.readInt();
            this.otherSharedDirty = source.readInt();
            this.otherPrivateClean = source.readInt();
            this.otherSharedClean = source.readInt();
            this.otherSwappedOut = source.readInt();
            this.hasSwappedOutPss = source.readInt() != 0;
            this.otherSwappedOutPss = source.readInt();
            this.otherStats = source.createIntArray();
        }

        private synchronized MemoryInfo(Parcel source) {
            this.otherStats = new int[279];
            readFromParcel(source);
        }
    }

    public static void waitForDebugger() {
        if (!VMDebug.isDebuggingEnabled() || isDebuggerConnected()) {
            return;
        }
        System.out.println("Sending WAIT chunk");
        byte[] data = {0};
        Chunk waitChunk = new Chunk(ChunkHandler.type("WAIT"), data, 0, 1);
        DdmServer.sendChunk(waitChunk);
        mWaiting = true;
        while (!isDebuggerConnected()) {
            try {
                Thread.sleep(200L);
            } catch (InterruptedException e) {
            }
        }
        mWaiting = false;
        System.out.println("Debugger has connected");
        while (true) {
            long delta = VMDebug.lastDebuggerActivity();
            if (delta < 0) {
                System.out.println("debugger detached?");
                return;
            } else if (delta < 1300) {
                System.out.println("waiting for debugger to settle...");
                try {
                    Thread.sleep(200L);
                } catch (InterruptedException e2) {
                }
            } else {
                PrintStream printStream = System.out;
                printStream.println("debugger has settled (" + delta + ")");
                return;
            }
        }
    }

    public static boolean waitingForDebugger() {
        return mWaiting;
    }

    public static boolean isDebuggerConnected() {
        return VMDebug.isDebuggerConnected();
    }

    public static synchronized String[] getVmFeatureList() {
        return VMDebug.getVmFeatureList();
    }

    @Deprecated
    public static void changeDebugPort(int port) {
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0020, code lost:
        if (r0 == null) goto L5;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void startNativeTracing() {
        /*
            r0 = 0
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L18 java.lang.Exception -> L1f
            java.lang.String r2 = "/sys/qemu_trace/state"
            r1.<init>(r2)     // Catch: java.lang.Throwable -> L18 java.lang.Exception -> L1f
            com.android.internal.util.FastPrintWriter r2 = new com.android.internal.util.FastPrintWriter     // Catch: java.lang.Throwable -> L18 java.lang.Exception -> L1f
            r2.<init>(r1)     // Catch: java.lang.Throwable -> L18 java.lang.Exception -> L1f
            r0 = r2
            java.lang.String r2 = "1"
            r0.println(r2)     // Catch: java.lang.Throwable -> L18 java.lang.Exception -> L1f
        L14:
            r0.close()
            goto L23
        L18:
            r1 = move-exception
            if (r0 == 0) goto L1e
            r0.close()
        L1e:
            throw r1
        L1f:
            r1 = move-exception
            if (r0 == 0) goto L23
            goto L14
        L23:
            dalvik.system.VMDebug.startEmulatorTracing()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.Debug.startNativeTracing():void");
    }

    public static void stopNativeTracing() {
        VMDebug.stopEmulatorTracing();
        PrintWriter outStream = null;
        try {
            FileOutputStream fos = new FileOutputStream(SYSFS_QEMU_TRACE_STATE);
            outStream = new FastPrintWriter(fos);
            outStream.println("0");
        } catch (Exception e) {
            if (outStream == null) {
                return;
            }
        } catch (Throwable th) {
            if (outStream != null) {
                outStream.close();
            }
            throw th;
        }
        outStream.close();
    }

    public static void enableEmulatorTraceOutput() {
        VMDebug.startEmulatorTracing();
    }

    public static void startMethodTracing() {
        VMDebug.startMethodTracing(fixTracePath(null), 0, 0, false, 0);
    }

    public static void startMethodTracing(String tracePath) {
        startMethodTracing(tracePath, 0, 0);
    }

    public static void startMethodTracing(String tracePath, int bufferSize) {
        startMethodTracing(tracePath, bufferSize, 0);
    }

    public static void startMethodTracing(String tracePath, int bufferSize, int flags) {
        VMDebug.startMethodTracing(fixTracePath(tracePath), bufferSize, flags, false, 0);
    }

    public static void startMethodTracingSampling(String tracePath, int bufferSize, int intervalUs) {
        VMDebug.startMethodTracing(fixTracePath(tracePath), bufferSize, 0, true, intervalUs);
    }

    private static synchronized String fixTracePath(String tracePath) {
        File dir;
        if (tracePath == null || tracePath.charAt(0) != '/') {
            Context context = AppGlobals.getInitialApplication();
            if (context != null) {
                dir = context.getExternalFilesDir(null);
            } else {
                dir = Environment.getExternalStorageDirectory();
            }
            if (tracePath == null) {
                tracePath = new File(dir, DEFAULT_TRACE_BODY).getAbsolutePath();
            } else {
                tracePath = new File(dir, tracePath).getAbsolutePath();
            }
        }
        if (!tracePath.endsWith(DEFAULT_TRACE_EXTENSION)) {
            return tracePath + DEFAULT_TRACE_EXTENSION;
        }
        return tracePath;
    }

    public static synchronized void startMethodTracing(String traceName, FileDescriptor fd, int bufferSize, int flags, boolean streamOutput) {
        VMDebug.startMethodTracing(traceName, fd, bufferSize, flags, false, 0, streamOutput);
    }

    public static synchronized void startMethodTracingDdms(int bufferSize, int flags, boolean samplingEnabled, int intervalUs) {
        VMDebug.startMethodTracingDdms(bufferSize, flags, samplingEnabled, intervalUs);
    }

    public static synchronized int getMethodTracingMode() {
        return VMDebug.getMethodTracingMode();
    }

    public static void stopMethodTracing() {
        VMDebug.stopMethodTracing();
    }

    public static long threadCpuTimeNanos() {
        return VMDebug.threadCpuTimeNanos();
    }

    @Deprecated
    public static void startAllocCounting() {
        VMDebug.startAllocCounting();
    }

    @Deprecated
    public static void stopAllocCounting() {
        VMDebug.stopAllocCounting();
    }

    @Deprecated
    public static int getGlobalAllocCount() {
        return VMDebug.getAllocCount(1);
    }

    @Deprecated
    public static void resetGlobalAllocCount() {
        VMDebug.resetAllocCount(1);
    }

    @Deprecated
    public static int getGlobalAllocSize() {
        return VMDebug.getAllocCount(2);
    }

    @Deprecated
    public static void resetGlobalAllocSize() {
        VMDebug.resetAllocCount(2);
    }

    @Deprecated
    public static int getGlobalFreedCount() {
        return VMDebug.getAllocCount(4);
    }

    @Deprecated
    public static void resetGlobalFreedCount() {
        VMDebug.resetAllocCount(4);
    }

    @Deprecated
    public static int getGlobalFreedSize() {
        return VMDebug.getAllocCount(8);
    }

    @Deprecated
    public static void resetGlobalFreedSize() {
        VMDebug.resetAllocCount(8);
    }

    @Deprecated
    public static int getGlobalGcInvocationCount() {
        return VMDebug.getAllocCount(16);
    }

    @Deprecated
    public static void resetGlobalGcInvocationCount() {
        VMDebug.resetAllocCount(16);
    }

    @Deprecated
    public static int getGlobalClassInitCount() {
        return VMDebug.getAllocCount(32);
    }

    @Deprecated
    public static void resetGlobalClassInitCount() {
        VMDebug.resetAllocCount(32);
    }

    @Deprecated
    public static int getGlobalClassInitTime() {
        return VMDebug.getAllocCount(64);
    }

    @Deprecated
    public static void resetGlobalClassInitTime() {
        VMDebug.resetAllocCount(64);
    }

    @Deprecated
    public static int getGlobalExternalAllocCount() {
        return 0;
    }

    @Deprecated
    public static void resetGlobalExternalAllocSize() {
    }

    @Deprecated
    public static void resetGlobalExternalAllocCount() {
    }

    @Deprecated
    public static int getGlobalExternalAllocSize() {
        return 0;
    }

    @Deprecated
    public static int getGlobalExternalFreedCount() {
        return 0;
    }

    @Deprecated
    public static void resetGlobalExternalFreedCount() {
    }

    @Deprecated
    public static int getGlobalExternalFreedSize() {
        return 0;
    }

    @Deprecated
    public static void resetGlobalExternalFreedSize() {
    }

    @Deprecated
    public static int getThreadAllocCount() {
        return VMDebug.getAllocCount(65536);
    }

    @Deprecated
    public static void resetThreadAllocCount() {
        VMDebug.resetAllocCount(65536);
    }

    @Deprecated
    public static int getThreadAllocSize() {
        return VMDebug.getAllocCount(131072);
    }

    @Deprecated
    public static void resetThreadAllocSize() {
        VMDebug.resetAllocCount(131072);
    }

    @Deprecated
    public static int getThreadExternalAllocCount() {
        return 0;
    }

    @Deprecated
    public static void resetThreadExternalAllocCount() {
    }

    @Deprecated
    public static int getThreadExternalAllocSize() {
        return 0;
    }

    @Deprecated
    public static void resetThreadExternalAllocSize() {
    }

    @Deprecated
    public static int getThreadGcInvocationCount() {
        return VMDebug.getAllocCount(1048576);
    }

    @Deprecated
    public static void resetThreadGcInvocationCount() {
        VMDebug.resetAllocCount(1048576);
    }

    @Deprecated
    public static void resetAllCounts() {
        VMDebug.resetAllocCount(-1);
    }

    public static String getRuntimeStat(String statName) {
        return VMDebug.getRuntimeStat(statName);
    }

    public static Map<String, String> getRuntimeStats() {
        return VMDebug.getRuntimeStats();
    }

    @Deprecated
    public static int setAllocationLimit(int limit) {
        return -1;
    }

    @Deprecated
    public static int setGlobalAllocationLimit(int limit) {
        return -1;
    }

    public static void printLoadedClasses(int flags) {
        VMDebug.printLoadedClasses(flags);
    }

    public static int getLoadedClassCount() {
        return VMDebug.getLoadedClassCount();
    }

    public static void dumpHprofData(String fileName) throws IOException {
        VMDebug.dumpHprofData(fileName);
    }

    public static synchronized void dumpHprofData(String fileName, FileDescriptor fd) throws IOException {
        VMDebug.dumpHprofData(fileName, fd);
    }

    public static synchronized void dumpHprofDataDdms() {
        VMDebug.dumpHprofDataDdms();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long countInstancesOfClass(Class cls) {
        return VMDebug.countInstancesOfClass(cls, true);
    }

    public static final synchronized boolean cacheRegisterMap(String classAndMethodDesc) {
        return VMDebug.cacheRegisterMap(classAndMethodDesc);
    }

    private protected static final void dumpReferenceTables() {
        VMDebug.dumpReferenceTables();
    }

    @Deprecated
    /* loaded from: classes2.dex */
    public static class InstructionCount {
        public boolean resetAndStart() {
            return false;
        }

        public boolean collect() {
            return false;
        }

        public int globalTotal() {
            return 0;
        }

        public int globalMethodInvocations() {
            return 0;
        }
    }

    private static synchronized boolean fieldTypeMatches(Field field, Class<?> cl) {
        Class<?> fieldClass = field.getType();
        if (fieldClass == cl) {
            return true;
        }
        try {
            Field primitiveTypeField = cl.getField("TYPE");
            try {
                if (fieldClass == ((Class) primitiveTypeField.get(null))) {
                    return true;
                }
                return false;
            } catch (IllegalAccessException e) {
                return false;
            }
        } catch (NoSuchFieldException e2) {
            return false;
        }
    }

    private static synchronized void modifyFieldIfSet(Field field, TypedProperties properties, String propertyName) {
        if (field.getType() == String.class) {
            int stringInfo = properties.getStringInfo(propertyName);
            switch (stringInfo) {
                case -2:
                    throw new IllegalArgumentException("Type of " + propertyName + "  does not match field type (" + field.getType() + ")");
                case -1:
                    return;
                case 0:
                    try {
                        field.set(null, null);
                        return;
                    } catch (IllegalAccessException ex) {
                        throw new IllegalArgumentException("Cannot set field for " + propertyName, ex);
                    }
                case 1:
                    break;
                default:
                    throw new IllegalStateException("Unexpected getStringInfo(" + propertyName + ") return value " + stringInfo);
            }
        }
        Object value = properties.get(propertyName);
        if (value != null) {
            if (!fieldTypeMatches(field, value.getClass())) {
                throw new IllegalArgumentException("Type of " + propertyName + " (" + value.getClass() + ")  does not match field type (" + field.getType() + ")");
            }
            try {
                field.set(null, value);
            } catch (IllegalAccessException ex2) {
                throw new IllegalArgumentException("Cannot set field for " + propertyName, ex2);
            }
        }
    }

    public static synchronized void setFieldsOn(Class<?> cl) {
        setFieldsOn(cl, false);
    }

    public static synchronized void setFieldsOn(Class<?> cl, boolean partial) {
        StringBuilder sb = new StringBuilder();
        sb.append("setFieldsOn(");
        sb.append(cl == null ? "null" : cl.getName());
        sb.append(") called in non-DEBUG build");
        Log.wtf(TAG, sb.toString());
    }

    public static boolean dumpService(String name, FileDescriptor fd, String[] args) {
        IBinder service = ServiceManager.getService(name);
        if (service == null) {
            Log.e(TAG, "Can't find service to dump: " + name);
            return false;
        }
        try {
            service.dump(fd, args);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "Can't dump service: " + name, e);
            return false;
        }
    }

    private static synchronized String getCaller(StackTraceElement[] callStack, int depth) {
        if (4 + depth >= callStack.length) {
            return "<bottom of call stack>";
        }
        StackTraceElement caller = callStack[4 + depth];
        return caller.getClassName() + "." + caller.getMethodName() + SettingsStringUtil.DELIMITER + caller.getLineNumber();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getCallers(int depth) {
        StackTraceElement[] callStack = Thread.currentThread().getStackTrace();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < depth; i++) {
            sb.append(getCaller(callStack, i));
            sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        }
        return sb.toString();
    }

    public static synchronized String getCallers(int start, int depth) {
        StackTraceElement[] callStack = Thread.currentThread().getStackTrace();
        StringBuffer sb = new StringBuffer();
        int depth2 = depth + start;
        for (int i = start; i < depth2; i++) {
            sb.append(getCaller(callStack, i));
            sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        }
        return sb.toString();
    }

    public static synchronized String getCallers(int depth, String linePrefix) {
        StackTraceElement[] callStack = Thread.currentThread().getStackTrace();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < depth; i++) {
            sb.append(linePrefix);
            sb.append(getCaller(callStack, i));
            sb.append("\n");
        }
        return sb.toString();
    }

    private protected static String getCaller() {
        return getCaller(Thread.currentThread().getStackTrace(), 0);
    }

    public static void attachJvmtiAgent(String library, String options, ClassLoader classLoader) throws IOException {
        Preconditions.checkNotNull(library);
        Preconditions.checkArgument(!library.contains("="));
        if (options == null) {
            VMDebug.attachAgent(library, classLoader);
            return;
        }
        VMDebug.attachAgent(library + "=" + options, classLoader);
    }
}
