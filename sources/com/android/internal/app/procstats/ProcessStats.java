package com.android.internal.app.procstats;

import android.os.Debug;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.SettingsStringUtil;
import android.text.format.DateFormat;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.DebugUtils;
import android.util.LongSparseArray;
import android.util.Slog;
import android.util.SparseArray;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import com.android.internal.app.ProcessMap;
import com.android.internal.content.NativeLibraryHelper;
import com.android.internal.telephony.PhoneConstants;
import com.xiaopeng.util.FeatureOption;
import dalvik.system.VMRuntime;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* loaded from: classes3.dex */
public final class ProcessStats implements Parcelable {
    public static final int ADD_PSS_EXTERNAL = 3;
    public static final int ADD_PSS_EXTERNAL_SLOW = 4;
    public static final int ADD_PSS_INTERNAL_ALL_MEM = 1;
    public static final int ADD_PSS_INTERNAL_ALL_POLL = 2;
    public static final int ADD_PSS_INTERNAL_SINGLE = 0;
    public static final int ADJ_COUNT = 8;
    public static final int ADJ_MEM_FACTOR_COUNT = 4;
    public static final int ADJ_MEM_FACTOR_CRITICAL = 3;
    public static final int ADJ_MEM_FACTOR_LOW = 2;
    public static final int ADJ_MEM_FACTOR_MODERATE = 1;
    public static final int ADJ_MEM_FACTOR_NORMAL = 0;
    public static final int ADJ_NOTHING = -1;
    public static final int ADJ_SCREEN_MOD = 4;
    public static final int ADJ_SCREEN_OFF = 0;
    public static final int ADJ_SCREEN_ON = 4;
    static final boolean DEBUG = false;
    static final boolean DEBUG_PARCEL = false;
    public static final int FLAG_COMPLETE = 1;
    public static final int FLAG_SHUTDOWN = 2;
    public static final int FLAG_SYSPROPS = 4;
    private static final int MAGIC = 1347638356;
    private static final int PARCEL_VERSION = 27;
    public static final int PSS_AVERAGE = 2;
    public static final int PSS_COUNT = 10;
    public static final int PSS_MAXIMUM = 3;
    public static final int PSS_MINIMUM = 1;
    public static final int PSS_RSS_AVERAGE = 8;
    public static final int PSS_RSS_MAXIMUM = 9;
    public static final int PSS_RSS_MINIMUM = 7;
    public static final int PSS_SAMPLE_COUNT = 0;
    public static final int PSS_USS_AVERAGE = 5;
    public static final int PSS_USS_MAXIMUM = 6;
    public static final int PSS_USS_MINIMUM = 4;
    public static final String SERVICE_NAME = "procstats";
    public static final int STATE_BACKUP = 4;
    public static final int STATE_CACHED_ACTIVITY = 11;
    public static final int STATE_CACHED_ACTIVITY_CLIENT = 12;
    public static final int STATE_CACHED_EMPTY = 13;
    public static final int STATE_COUNT = 14;
    public static final int STATE_HEAVY_WEIGHT = 8;
    public static final int STATE_HOME = 9;
    public static final int STATE_IMPORTANT_BACKGROUND = 3;
    public static final int STATE_IMPORTANT_FOREGROUND = 2;
    public static final int STATE_LAST_ACTIVITY = 10;
    public static final int STATE_NOTHING = -1;
    public static final int STATE_PERSISTENT = 0;
    public static final int STATE_RECEIVER = 7;
    public static final int STATE_SERVICE = 5;
    public static final int STATE_SERVICE_RESTARTING = 6;
    public static final int STATE_TOP = 1;
    public static final int SYS_MEM_USAGE_CACHED_AVERAGE = 2;
    public static final int SYS_MEM_USAGE_CACHED_MAXIMUM = 3;
    public static final int SYS_MEM_USAGE_CACHED_MINIMUM = 1;
    public static final int SYS_MEM_USAGE_COUNT = 16;
    public static final int SYS_MEM_USAGE_FREE_AVERAGE = 5;
    public static final int SYS_MEM_USAGE_FREE_MAXIMUM = 6;
    public static final int SYS_MEM_USAGE_FREE_MINIMUM = 4;
    public static final int SYS_MEM_USAGE_KERNEL_AVERAGE = 11;
    public static final int SYS_MEM_USAGE_KERNEL_MAXIMUM = 12;
    public static final int SYS_MEM_USAGE_KERNEL_MINIMUM = 10;
    public static final int SYS_MEM_USAGE_NATIVE_AVERAGE = 14;
    public static final int SYS_MEM_USAGE_NATIVE_MAXIMUM = 15;
    public static final int SYS_MEM_USAGE_NATIVE_MINIMUM = 13;
    public static final int SYS_MEM_USAGE_SAMPLE_COUNT = 0;
    public static final int SYS_MEM_USAGE_ZRAM_AVERAGE = 8;
    public static final int SYS_MEM_USAGE_ZRAM_MAXIMUM = 9;
    public static final int SYS_MEM_USAGE_ZRAM_MINIMUM = 7;
    public static final String TAG = "ProcessStats";
    ArrayMap<String, Integer> mCommonStringToIndex;
    public long mExternalPssCount;
    public long mExternalPssTime;
    public long mExternalSlowPssCount;
    public long mExternalSlowPssTime;
    public int mFlags;
    boolean mHasSwappedOutPss;
    ArrayList<String> mIndexToCommonString;
    public long mInternalAllMemPssCount;
    public long mInternalAllMemPssTime;
    public long mInternalAllPollPssCount;
    public long mInternalAllPollPssTime;
    public long mInternalSinglePssCount;
    public long mInternalSinglePssTime;
    public int mMemFactor;
    public final long[] mMemFactorDurations;
    public final ProcessMap<LongSparseArray<PackageState>> mPackages;
    private final ArrayList<String> mPageTypeLabels;
    private final ArrayList<int[]> mPageTypeSizes;
    private final ArrayList<Integer> mPageTypeZones;
    public final ProcessMap<ProcessState> mProcesses;
    public String mReadError;
    boolean mRunning;
    String mRuntime;
    public long mStartTime;
    public final SysMemUsageTable mSysMemUsage;
    public final long[] mSysMemUsageArgs;
    public final SparseMappingTable mTableData;
    public long mTimePeriodEndRealtime;
    public long mTimePeriodEndUptime;
    public long mTimePeriodStartClock;
    public String mTimePeriodStartClockStr;
    public long mTimePeriodStartRealtime;
    public long mTimePeriodStartUptime;
    public static long COMMIT_PERIOD = 10800000;
    public static long COMMIT_UPTIME_PERIOD = 3600000;
    public static final int[] ALL_MEM_ADJ = {0, 1, 2, 3};
    public static final int[] ALL_SCREEN_ADJ = {0, 4};
    public static final int[] NON_CACHED_PROC_STATES = {0, 1, 2, 3, 4, 5, 6, 7, 8};
    public static final int[] BACKGROUND_PROC_STATES = {2, 3, 4, 8, 5, 6, 7};
    public static final int[] ALL_PROC_STATES = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
    private static final Pattern sPageTypeRegex = Pattern.compile("^Node\\s+(\\d+),.*. type\\s+(\\w+)\\s+([\\s\\d]+?)\\s*$");
    public static final Parcelable.Creator<ProcessStats> CREATOR = new Parcelable.Creator<ProcessStats>() { // from class: com.android.internal.app.procstats.ProcessStats.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ProcessStats createFromParcel(Parcel in) {
            return new ProcessStats(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ProcessStats[] newArray(int size) {
            return new ProcessStats[size];
        }
    };
    static final int[] BAD_TABLE = new int[0];

    public ProcessStats(boolean running) {
        this.mPackages = new ProcessMap<>();
        this.mProcesses = new ProcessMap<>();
        this.mMemFactorDurations = new long[8];
        this.mMemFactor = -1;
        this.mTableData = new SparseMappingTable();
        this.mSysMemUsageArgs = new long[16];
        this.mSysMemUsage = new SysMemUsageTable(this.mTableData);
        this.mPageTypeZones = new ArrayList<>();
        this.mPageTypeLabels = new ArrayList<>();
        this.mPageTypeSizes = new ArrayList<>();
        this.mRunning = running;
        reset();
        if (running) {
            Debug.MemoryInfo info = new Debug.MemoryInfo();
            Debug.getMemoryInfo(Process.myPid(), info);
            this.mHasSwappedOutPss = info.hasSwappedOutPss();
        }
    }

    public ProcessStats(Parcel in) {
        this.mPackages = new ProcessMap<>();
        this.mProcesses = new ProcessMap<>();
        this.mMemFactorDurations = new long[8];
        this.mMemFactor = -1;
        this.mTableData = new SparseMappingTable();
        this.mSysMemUsageArgs = new long[16];
        this.mSysMemUsage = new SysMemUsageTable(this.mTableData);
        this.mPageTypeZones = new ArrayList<>();
        this.mPageTypeLabels = new ArrayList<>();
        this.mPageTypeSizes = new ArrayList<>();
        reset();
        readFromParcel(in);
    }

    public void add(ProcessStats other) {
        ArrayMap<String, SparseArray<ProcessState>> procMap;
        ProcessState thisProc;
        LongSparseArray<PackageState> versions;
        ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap;
        SparseArray<LongSparseArray<PackageState>> uids;
        int NSRVS;
        int NPROCS;
        PackageState otherState;
        int NSRVS2;
        ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap2 = other.mPackages.getMap();
        int ip = 0;
        while (true) {
            int ip2 = ip;
            int ip3 = pkgMap2.size();
            if (ip2 >= ip3) {
                break;
            }
            String pkgName = pkgMap2.keyAt(ip2);
            SparseArray<LongSparseArray<PackageState>> uids2 = pkgMap2.valueAt(ip2);
            int iu = 0;
            while (true) {
                int iu2 = iu;
                int iu3 = uids2.size();
                if (iu2 < iu3) {
                    int uid = uids2.keyAt(iu2);
                    LongSparseArray<PackageState> versions2 = uids2.valueAt(iu2);
                    int iv = 0;
                    while (true) {
                        int iv2 = iv;
                        int iv3 = versions2.size();
                        if (iv2 < iv3) {
                            long vers = versions2.keyAt(iv2);
                            PackageState otherState2 = versions2.valueAt(iv2);
                            int NPROCS2 = otherState2.mProcesses.size();
                            int NSRVS3 = otherState2.mServices.size();
                            int iproc = 0;
                            while (true) {
                                int iproc2 = iproc;
                                if (iproc2 >= NPROCS2) {
                                    break;
                                }
                                int NSRVS4 = NSRVS3;
                                ProcessState otherProc = otherState2.mProcesses.valueAt(iproc2);
                                int NPROCS3 = NPROCS2;
                                if (otherProc.getCommonProcess() == otherProc) {
                                    versions = versions2;
                                    pkgMap = pkgMap2;
                                    uids = uids2;
                                    NSRVS = NSRVS4;
                                    NPROCS = NPROCS3;
                                    otherState = otherState2;
                                    NSRVS2 = iv2;
                                } else {
                                    versions = versions2;
                                    pkgMap = pkgMap2;
                                    NPROCS = NPROCS3;
                                    NSRVS = NSRVS4;
                                    uids = uids2;
                                    otherState = otherState2;
                                    long vers2 = vers;
                                    NSRVS2 = iv2;
                                    ProcessState thisProc2 = getProcessStateLocked(pkgName, uid, vers, otherProc.getName());
                                    if (thisProc2.getCommonProcess() == thisProc2) {
                                        thisProc2.setMultiPackage(true);
                                        long now = SystemClock.uptimeMillis();
                                        vers = vers2;
                                        PackageState pkgState = getPackageStateLocked(pkgName, uid, vers);
                                        thisProc2 = thisProc2.clone(now);
                                        pkgState.mProcesses.put(thisProc2.getName(), thisProc2);
                                    } else {
                                        vers = vers2;
                                    }
                                    thisProc2.add(otherProc);
                                }
                                iproc = iproc2 + 1;
                                NSRVS3 = NSRVS;
                                otherState2 = otherState;
                                NPROCS2 = NPROCS;
                                iv2 = NSRVS2;
                                versions2 = versions;
                                pkgMap2 = pkgMap;
                                uids2 = uids;
                            }
                            int iv4 = iv2;
                            LongSparseArray<PackageState> versions3 = versions2;
                            ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap3 = pkgMap2;
                            SparseArray<LongSparseArray<PackageState>> uids3 = uids2;
                            int NSRVS5 = NSRVS3;
                            PackageState otherState3 = otherState2;
                            int isvc = 0;
                            while (true) {
                                int isvc2 = isvc;
                                if (isvc2 < NSRVS5) {
                                    ServiceState otherSvc = otherState3.mServices.valueAt(isvc2);
                                    ServiceState thisSvc = getServiceStateLocked(pkgName, uid, vers, otherSvc.getProcessName(), otherSvc.getName());
                                    thisSvc.add(otherSvc);
                                    isvc = isvc2 + 1;
                                    NSRVS5 = NSRVS5;
                                }
                            }
                            iv = iv4 + 1;
                            versions2 = versions3;
                            pkgMap2 = pkgMap3;
                            uids2 = uids3;
                        }
                    }
                    iu = iu2 + 1;
                }
            }
            ip = ip2 + 1;
        }
        ArrayMap<String, SparseArray<ProcessState>> procMap2 = other.mProcesses.getMap();
        int ip4 = 0;
        while (true) {
            int ip5 = ip4;
            int ip6 = procMap2.size();
            if (ip5 >= ip6) {
                break;
            }
            SparseArray<ProcessState> uids4 = procMap2.valueAt(ip5);
            int iu4 = 0;
            while (true) {
                int iu5 = iu4;
                int iu6 = uids4.size();
                if (iu5 < iu6) {
                    int uid2 = uids4.keyAt(iu5);
                    ProcessState otherProc2 = uids4.valueAt(iu5);
                    String name = otherProc2.getName();
                    String pkg = otherProc2.getPackage();
                    long vers3 = otherProc2.getVersion();
                    ProcessState thisProc3 = this.mProcesses.get(name, uid2);
                    if (thisProc3 == null) {
                        procMap = procMap2;
                        thisProc = new ProcessState(this, pkg, uid2, vers3, name);
                        this.mProcesses.put(name, uid2, thisProc);
                        PackageState thisState = getPackageStateLocked(pkg, uid2, vers3);
                        if (!thisState.mProcesses.containsKey(name)) {
                            thisState.mProcesses.put(name, thisProc);
                        }
                    } else {
                        procMap = procMap2;
                        thisProc = thisProc3;
                    }
                    thisProc.add(otherProc2);
                    iu4 = iu5 + 1;
                    procMap2 = procMap;
                }
            }
            ip4 = ip5 + 1;
        }
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= 8) {
                break;
            }
            long[] jArr = this.mMemFactorDurations;
            jArr[i2] = jArr[i2] + other.mMemFactorDurations[i2];
            i = i2 + 1;
        }
        this.mSysMemUsage.mergeStats(other.mSysMemUsage);
        if (other.mTimePeriodStartClock < this.mTimePeriodStartClock) {
            this.mTimePeriodStartClock = other.mTimePeriodStartClock;
            this.mTimePeriodStartClockStr = other.mTimePeriodStartClockStr;
        }
        this.mTimePeriodEndRealtime += other.mTimePeriodEndRealtime - other.mTimePeriodStartRealtime;
        this.mTimePeriodEndUptime += other.mTimePeriodEndUptime - other.mTimePeriodStartUptime;
        this.mInternalSinglePssCount += other.mInternalSinglePssCount;
        this.mInternalSinglePssTime += other.mInternalSinglePssTime;
        this.mInternalAllMemPssCount += other.mInternalAllMemPssCount;
        this.mInternalAllMemPssTime += other.mInternalAllMemPssTime;
        this.mInternalAllPollPssCount += other.mInternalAllPollPssCount;
        this.mInternalAllPollPssTime += other.mInternalAllPollPssTime;
        this.mExternalPssCount += other.mExternalPssCount;
        this.mExternalPssTime += other.mExternalPssTime;
        this.mExternalSlowPssCount += other.mExternalSlowPssCount;
        this.mExternalSlowPssTime += other.mExternalSlowPssTime;
        this.mHasSwappedOutPss |= other.mHasSwappedOutPss;
    }

    public void addSysMemUsage(long cachedMem, long freeMem, long zramMem, long kernelMem, long nativeMem) {
        if (this.mMemFactor != -1) {
            int state = this.mMemFactor * 14;
            this.mSysMemUsageArgs[0] = 1;
            for (int i = 0; i < 3; i++) {
                this.mSysMemUsageArgs[1 + i] = cachedMem;
                this.mSysMemUsageArgs[4 + i] = freeMem;
                this.mSysMemUsageArgs[7 + i] = zramMem;
                this.mSysMemUsageArgs[10 + i] = kernelMem;
                this.mSysMemUsageArgs[13 + i] = nativeMem;
            }
            this.mSysMemUsage.mergeStats(state, this.mSysMemUsageArgs, 0);
        }
    }

    public void computeTotalMemoryUse(TotalMemoryUseCollection data, long now) {
        long[] totalMemUsage;
        long j = now;
        data.totalTime = 0L;
        int i = 0;
        for (int i2 = 0; i2 < 14; i2++) {
            data.processStateWeight[i2] = 0.0d;
            data.processStatePss[i2] = 0;
            data.processStateTime[i2] = 0;
            data.processStateSamples[i2] = 0;
        }
        for (int i3 = 0; i3 < 16; i3++) {
            data.sysMemUsage[i3] = 0;
        }
        data.sysMemCachedWeight = FeatureOption.FO_BOOT_POLICY_CPU;
        data.sysMemFreeWeight = FeatureOption.FO_BOOT_POLICY_CPU;
        data.sysMemZRamWeight = FeatureOption.FO_BOOT_POLICY_CPU;
        data.sysMemKernelWeight = FeatureOption.FO_BOOT_POLICY_CPU;
        data.sysMemNativeWeight = FeatureOption.FO_BOOT_POLICY_CPU;
        data.sysMemSamples = 0;
        long[] totalMemUsage2 = this.mSysMemUsage.getTotalMemUsage();
        int is = 0;
        while (is < data.screenStates.length) {
            int im = i;
            while (im < data.memStates.length) {
                int memBucket = data.screenStates[is] + data.memStates[im];
                int stateBucket = memBucket * 14;
                long memTime = this.mMemFactorDurations[memBucket];
                if (this.mMemFactor == memBucket) {
                    memTime += j - this.mStartTime;
                }
                data.totalTime += memTime;
                int sysKey = this.mSysMemUsage.getKey((byte) stateBucket);
                long[] longs = totalMemUsage2;
                int idx = 0;
                if (sysKey != -1) {
                    long[] tmpLongs = this.mSysMemUsage.getArrayForKey(sysKey);
                    int tmpIndex = SparseMappingTable.getIndexFromKey(sysKey);
                    if (tmpLongs[tmpIndex + 0] >= 3) {
                        totalMemUsage = totalMemUsage2;
                        long[] totalMemUsage3 = data.sysMemUsage;
                        SysMemUsageTable.mergeSysMemUsage(totalMemUsage3, i, longs, 0);
                        longs = tmpLongs;
                        idx = tmpIndex;
                        data.sysMemCachedWeight += longs[idx + 2] * memTime;
                        data.sysMemFreeWeight += longs[idx + 5] * memTime;
                        data.sysMemZRamWeight += longs[idx + 8] * memTime;
                        data.sysMemKernelWeight += longs[idx + 11] * memTime;
                        data.sysMemNativeWeight += longs[idx + 14] * memTime;
                        data.sysMemSamples = (int) (data.sysMemSamples + longs[idx + 0]);
                        im++;
                        totalMemUsage2 = totalMemUsage;
                        j = now;
                        i = 0;
                    }
                }
                totalMemUsage = totalMemUsage2;
                data.sysMemCachedWeight += longs[idx + 2] * memTime;
                data.sysMemFreeWeight += longs[idx + 5] * memTime;
                data.sysMemZRamWeight += longs[idx + 8] * memTime;
                data.sysMemKernelWeight += longs[idx + 11] * memTime;
                data.sysMemNativeWeight += longs[idx + 14] * memTime;
                data.sysMemSamples = (int) (data.sysMemSamples + longs[idx + 0]);
                im++;
                totalMemUsage2 = totalMemUsage;
                j = now;
                i = 0;
            }
            is++;
            j = now;
            i = 0;
        }
        data.hasSwappedOutPss = this.mHasSwappedOutPss;
        ArrayMap<String, SparseArray<ProcessState>> procMap = this.mProcesses.getMap();
        for (int iproc = 0; iproc < procMap.size(); iproc++) {
            SparseArray<ProcessState> uids = procMap.valueAt(iproc);
            for (int iu = 0; iu < uids.size(); iu++) {
                ProcessState proc = uids.valueAt(iu);
                proc.aggregatePss(data, now);
            }
        }
    }

    public void reset() {
        resetCommon();
        this.mPackages.getMap().clear();
        this.mProcesses.getMap().clear();
        this.mMemFactor = -1;
        this.mStartTime = 0L;
    }

    public void resetSafely() {
        resetCommon();
        long now = SystemClock.uptimeMillis();
        ArrayMap<String, SparseArray<ProcessState>> procMap = this.mProcesses.getMap();
        for (int ip = procMap.size() - 1; ip >= 0; ip--) {
            SparseArray<ProcessState> uids = procMap.valueAt(ip);
            for (int iu = uids.size() - 1; iu >= 0; iu--) {
                uids.valueAt(iu).tmpNumInUse = 0;
            }
        }
        ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap = this.mPackages.getMap();
        for (int ip2 = pkgMap.size() - 1; ip2 >= 0; ip2--) {
            SparseArray<LongSparseArray<PackageState>> uids2 = pkgMap.valueAt(ip2);
            for (int iu2 = uids2.size() - 1; iu2 >= 0; iu2--) {
                LongSparseArray<PackageState> vpkgs = uids2.valueAt(iu2);
                for (int iv = vpkgs.size() - 1; iv >= 0; iv--) {
                    PackageState pkgState = vpkgs.valueAt(iv);
                    for (int iproc = pkgState.mProcesses.size() - 1; iproc >= 0; iproc--) {
                        ProcessState ps = pkgState.mProcesses.valueAt(iproc);
                        if (ps.isInUse()) {
                            ps.resetSafely(now);
                            ps.getCommonProcess().tmpNumInUse++;
                            ps.getCommonProcess().tmpFoundSubProc = ps;
                        } else {
                            pkgState.mProcesses.valueAt(iproc).makeDead();
                            pkgState.mProcesses.removeAt(iproc);
                        }
                    }
                    for (int isvc = pkgState.mServices.size() - 1; isvc >= 0; isvc--) {
                        ServiceState ss = pkgState.mServices.valueAt(isvc);
                        if (ss.isInUse()) {
                            ss.resetSafely(now);
                        } else {
                            pkgState.mServices.removeAt(isvc);
                        }
                    }
                    if (pkgState.mProcesses.size() <= 0 && pkgState.mServices.size() <= 0) {
                        vpkgs.removeAt(iv);
                    }
                }
                int iv2 = vpkgs.size();
                if (iv2 <= 0) {
                    uids2.removeAt(iu2);
                }
            }
            int iu3 = uids2.size();
            if (iu3 <= 0) {
                pkgMap.removeAt(ip2);
            }
        }
        int ip3 = procMap.size();
        for (int ip4 = ip3 - 1; ip4 >= 0; ip4--) {
            SparseArray<ProcessState> uids3 = procMap.valueAt(ip4);
            for (int iu4 = uids3.size() - 1; iu4 >= 0; iu4--) {
                ProcessState ps2 = uids3.valueAt(iu4);
                if (ps2.isInUse() || ps2.tmpNumInUse > 0) {
                    if (!ps2.isActive() && ps2.isMultiPackage() && ps2.tmpNumInUse == 1) {
                        ProcessState ps3 = ps2.tmpFoundSubProc;
                        ps3.makeStandalone();
                        uids3.setValueAt(iu4, ps3);
                    } else {
                        ps2.resetSafely(now);
                    }
                } else {
                    ps2.makeDead();
                    uids3.removeAt(iu4);
                }
            }
            int iu5 = uids3.size();
            if (iu5 <= 0) {
                procMap.removeAt(ip4);
            }
        }
        this.mStartTime = now;
    }

    private void resetCommon() {
        this.mTimePeriodStartClock = System.currentTimeMillis();
        buildTimePeriodStartClockStr();
        long elapsedRealtime = SystemClock.elapsedRealtime();
        this.mTimePeriodEndRealtime = elapsedRealtime;
        this.mTimePeriodStartRealtime = elapsedRealtime;
        long uptimeMillis = SystemClock.uptimeMillis();
        this.mTimePeriodEndUptime = uptimeMillis;
        this.mTimePeriodStartUptime = uptimeMillis;
        this.mInternalSinglePssCount = 0L;
        this.mInternalSinglePssTime = 0L;
        this.mInternalAllMemPssCount = 0L;
        this.mInternalAllMemPssTime = 0L;
        this.mInternalAllPollPssCount = 0L;
        this.mInternalAllPollPssTime = 0L;
        this.mExternalPssCount = 0L;
        this.mExternalPssTime = 0L;
        this.mExternalSlowPssCount = 0L;
        this.mExternalSlowPssTime = 0L;
        this.mTableData.reset();
        Arrays.fill(this.mMemFactorDurations, 0L);
        this.mSysMemUsage.resetTable();
        this.mStartTime = 0L;
        this.mReadError = null;
        this.mFlags = 0;
        evaluateSystemProperties(true);
        updateFragmentation();
    }

    public boolean evaluateSystemProperties(boolean update) {
        boolean changed = false;
        String runtime = SystemProperties.get("persist.sys.dalvik.vm.lib.2", VMRuntime.getRuntime().vmLibrary());
        if (!Objects.equals(runtime, this.mRuntime)) {
            changed = true;
            if (update) {
                this.mRuntime = runtime;
            }
        }
        return changed;
    }

    private void buildTimePeriodStartClockStr() {
        this.mTimePeriodStartClockStr = DateFormat.format("yyyy-MM-dd-HH-mm-ss", this.mTimePeriodStartClock).toString();
    }

    public void updateFragmentation() {
        Integer zone;
        BufferedReader reader = null;
        try {
            try {
                reader = new BufferedReader(new FileReader("/proc/pagetypeinfo"));
                Matcher matcher = sPageTypeRegex.matcher("");
                this.mPageTypeZones.clear();
                this.mPageTypeLabels.clear();
                this.mPageTypeSizes.clear();
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        try {
                            reader.close();
                            return;
                        } catch (IOException e) {
                            return;
                        }
                    }
                    matcher.reset(line);
                    if (matcher.matches() && (zone = Integer.valueOf(matcher.group(1), 10)) != null) {
                        this.mPageTypeZones.add(zone);
                        this.mPageTypeLabels.add(matcher.group(2));
                        this.mPageTypeSizes.add(splitAndParseNumbers(matcher.group(3)));
                    }
                }
            } catch (IOException e2) {
                this.mPageTypeZones.clear();
                this.mPageTypeLabels.clear();
                this.mPageTypeSizes.clear();
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e3) {
                    }
                }
            }
        } catch (Throwable th) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e4) {
                }
            }
            throw th;
        }
    }

    private static int[] splitAndParseNumbers(String s) {
        int count = 0;
        int N = s.length();
        boolean digit = false;
        for (int i = 0; i < N; i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                if (!digit) {
                    digit = true;
                    count++;
                }
            } else {
                digit = false;
            }
        }
        int[] result = new int[count];
        int p = 0;
        int val = 0;
        for (int i2 = 0; i2 < N; i2++) {
            char c2 = s.charAt(i2);
            if (c2 >= '0' && c2 <= '9') {
                if (!digit) {
                    digit = true;
                    val = c2 - '0';
                } else {
                    val = (val * 10) + (c2 - '0');
                }
            } else if (digit) {
                digit = false;
                result[p] = val;
                p++;
            }
        }
        if (count > 0) {
            result[count - 1] = val;
        }
        return result;
    }

    private void writeCompactedLongArray(Parcel out, long[] array, int num) {
        for (int i = 0; i < num; i++) {
            long val = array[i];
            if (val < 0) {
                Slog.w(TAG, "Time val negative: " + val);
                val = 0;
            }
            if (val > 2147483647L) {
                int top = ~((int) (2147483647L & (val >> 32)));
                int bottom = (int) (4294967295L & val);
                out.writeInt(top);
                out.writeInt(bottom);
            } else {
                out.writeInt((int) val);
            }
        }
    }

    private void readCompactedLongArray(Parcel in, int version, long[] array, int num) {
        if (version <= 10) {
            in.readLongArray(array);
            return;
        }
        int alen = array.length;
        if (num > alen) {
            throw new RuntimeException("bad array lengths: got " + num + " array is " + alen);
        }
        int i = 0;
        while (i < num) {
            int val = in.readInt();
            if (val >= 0) {
                array[i] = val;
            } else {
                int bottom = in.readInt();
                array[i] = ((~val) << 32) | bottom;
            }
            i++;
        }
        while (i < alen) {
            array[i] = 0;
            i++;
        }
    }

    private void writeCommonString(Parcel out, String name) {
        Integer index = this.mCommonStringToIndex.get(name);
        if (index != null) {
            out.writeInt(index.intValue());
            return;
        }
        Integer index2 = Integer.valueOf(this.mCommonStringToIndex.size());
        this.mCommonStringToIndex.put(name, index2);
        out.writeInt(~index2.intValue());
        out.writeString(name);
    }

    private String readCommonString(Parcel in, int version) {
        if (version <= 9) {
            return in.readString();
        }
        int index = in.readInt();
        if (index >= 0) {
            return this.mIndexToCommonString.get(index);
        }
        int index2 = ~index;
        String name = in.readString();
        while (this.mIndexToCommonString.size() <= index2) {
            this.mIndexToCommonString.add(null);
        }
        this.mIndexToCommonString.set(index2, name);
        return name;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        writeToParcel(out, SystemClock.uptimeMillis(), flags);
    }

    public void writeToParcel(Parcel out, long now, int flags) {
        int NUID;
        out.writeInt(MAGIC);
        out.writeInt(27);
        out.writeInt(14);
        out.writeInt(8);
        out.writeInt(10);
        out.writeInt(16);
        out.writeInt(4096);
        this.mCommonStringToIndex = new ArrayMap<>(this.mProcesses.size());
        ArrayMap<String, SparseArray<ProcessState>> procMap = this.mProcesses.getMap();
        int NPROC = procMap.size();
        for (int ip = 0; ip < NPROC; ip++) {
            SparseArray<ProcessState> uids = procMap.valueAt(ip);
            int NUID2 = uids.size();
            for (int iu = 0; iu < NUID2; iu++) {
                uids.valueAt(iu).commitStateTime(now);
            }
        }
        ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap = this.mPackages.getMap();
        int NPKG = pkgMap.size();
        for (int ip2 = 0; ip2 < NPKG; ip2++) {
            SparseArray<LongSparseArray<PackageState>> uids2 = pkgMap.valueAt(ip2);
            int iproc = uids2.size();
            for (int iu2 = 0; iu2 < iproc; iu2++) {
                LongSparseArray<PackageState> vpkgs = uids2.valueAt(iu2);
                int NVERS = vpkgs.size();
                int iv = 0;
                while (iv < NVERS) {
                    PackageState pkgState = vpkgs.valueAt(iv);
                    SparseArray<LongSparseArray<PackageState>> uids3 = uids2;
                    int NPROCS = pkgState.mProcesses.size();
                    int iproc2 = 0;
                    while (true) {
                        int iproc3 = iproc2;
                        NUID = iproc;
                        if (iproc3 >= NPROCS) {
                            break;
                        }
                        int NPROCS2 = NPROCS;
                        ProcessState proc = pkgState.mProcesses.valueAt(iproc3);
                        LongSparseArray<PackageState> vpkgs2 = vpkgs;
                        if (proc.getCommonProcess() != proc) {
                            proc.commitStateTime(now);
                        }
                        iproc2 = iproc3 + 1;
                        iproc = NUID;
                        NPROCS = NPROCS2;
                        vpkgs = vpkgs2;
                    }
                    LongSparseArray<PackageState> vpkgs3 = vpkgs;
                    int NSRVS = pkgState.mServices.size();
                    for (int isvc = 0; isvc < NSRVS; isvc++) {
                        pkgState.mServices.valueAt(isvc).commitStateTime(now);
                    }
                    iv++;
                    uids2 = uids3;
                    iproc = NUID;
                    vpkgs = vpkgs3;
                }
            }
        }
        out.writeLong(this.mTimePeriodStartClock);
        out.writeLong(this.mTimePeriodStartRealtime);
        out.writeLong(this.mTimePeriodEndRealtime);
        out.writeLong(this.mTimePeriodStartUptime);
        out.writeLong(this.mTimePeriodEndUptime);
        out.writeLong(this.mInternalSinglePssCount);
        out.writeLong(this.mInternalSinglePssTime);
        out.writeLong(this.mInternalAllMemPssCount);
        out.writeLong(this.mInternalAllMemPssTime);
        out.writeLong(this.mInternalAllPollPssCount);
        out.writeLong(this.mInternalAllPollPssTime);
        out.writeLong(this.mExternalPssCount);
        out.writeLong(this.mExternalPssTime);
        out.writeLong(this.mExternalSlowPssCount);
        out.writeLong(this.mExternalSlowPssTime);
        out.writeString(this.mRuntime);
        out.writeInt(this.mHasSwappedOutPss ? 1 : 0);
        out.writeInt(this.mFlags);
        this.mTableData.writeToParcel(out);
        if (this.mMemFactor != -1) {
            long[] jArr = this.mMemFactorDurations;
            int i = this.mMemFactor;
            jArr[i] = jArr[i] + (now - this.mStartTime);
            this.mStartTime = now;
        }
        writeCompactedLongArray(out, this.mMemFactorDurations, this.mMemFactorDurations.length);
        this.mSysMemUsage.writeToParcel(out);
        out.writeInt(NPROC);
        for (int ip3 = 0; ip3 < NPROC; ip3++) {
            writeCommonString(out, procMap.keyAt(ip3));
            SparseArray<ProcessState> uids4 = procMap.valueAt(ip3);
            int NUID3 = uids4.size();
            out.writeInt(NUID3);
            for (int iu3 = 0; iu3 < NUID3; iu3++) {
                out.writeInt(uids4.keyAt(iu3));
                ProcessState proc2 = uids4.valueAt(iu3);
                writeCommonString(out, proc2.getPackage());
                out.writeLong(proc2.getVersion());
                proc2.writeToParcel(out, now);
            }
        }
        out.writeInt(NPKG);
        for (int ip4 = 0; ip4 < NPKG; ip4++) {
            writeCommonString(out, pkgMap.keyAt(ip4));
            SparseArray<LongSparseArray<PackageState>> uids5 = pkgMap.valueAt(ip4);
            int NUID4 = uids5.size();
            out.writeInt(NUID4);
            for (int iu4 = 0; iu4 < NUID4; iu4++) {
                out.writeInt(uids5.keyAt(iu4));
                LongSparseArray<PackageState> vpkgs4 = uids5.valueAt(iu4);
                int NVERS2 = vpkgs4.size();
                out.writeInt(NVERS2);
                int iv2 = 0;
                while (iv2 < NVERS2) {
                    ArrayMap<String, SparseArray<ProcessState>> procMap2 = procMap;
                    int NPROC2 = NPROC;
                    out.writeLong(vpkgs4.keyAt(iv2));
                    PackageState pkgState2 = vpkgs4.valueAt(iv2);
                    int NPROCS3 = pkgState2.mProcesses.size();
                    out.writeInt(NPROCS3);
                    int iproc4 = 0;
                    while (iproc4 < NPROCS3) {
                        int NPROCS4 = NPROCS3;
                        writeCommonString(out, pkgState2.mProcesses.keyAt(iproc4));
                        ProcessState proc3 = pkgState2.mProcesses.valueAt(iproc4);
                        ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap2 = pkgMap;
                        if (proc3.getCommonProcess() == proc3) {
                            out.writeInt(0);
                        } else {
                            out.writeInt(1);
                            proc3.writeToParcel(out, now);
                        }
                        iproc4++;
                        NPROCS3 = NPROCS4;
                        pkgMap = pkgMap2;
                    }
                    ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap3 = pkgMap;
                    int NSRVS2 = pkgState2.mServices.size();
                    out.writeInt(NSRVS2);
                    int isvc2 = 0;
                    while (isvc2 < NSRVS2) {
                        out.writeString(pkgState2.mServices.keyAt(isvc2));
                        ServiceState svc = pkgState2.mServices.valueAt(isvc2);
                        writeCommonString(out, svc.getProcessName());
                        svc.writeToParcel(out, now);
                        isvc2++;
                        pkgState2 = pkgState2;
                    }
                    iv2++;
                    procMap = procMap2;
                    NPROC = NPROC2;
                    pkgMap = pkgMap3;
                }
            }
        }
        int NPAGETYPES = this.mPageTypeLabels.size();
        out.writeInt(NPAGETYPES);
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 < NPAGETYPES) {
                out.writeInt(this.mPageTypeZones.get(i3).intValue());
                out.writeString(this.mPageTypeLabels.get(i3));
                out.writeIntArray(this.mPageTypeSizes.get(i3));
                i2 = i3 + 1;
            } else {
                this.mCommonStringToIndex = null;
                return;
            }
        }
    }

    private boolean readCheckedInt(Parcel in, int val, String what) {
        int got = in.readInt();
        if (got != val) {
            this.mReadError = "bad " + what + ": " + got;
            return false;
        }
        return true;
    }

    static byte[] readFully(InputStream stream, int[] outLen) throws IOException {
        int pos = 0;
        int initialAvail = stream.available();
        byte[] data = new byte[initialAvail > 0 ? initialAvail + 1 : 16384];
        while (true) {
            int amt = stream.read(data, pos, data.length - pos);
            if (amt < 0) {
                outLen[0] = pos;
                return data;
            }
            pos += amt;
            if (pos >= data.length) {
                byte[] newData = new byte[pos + 16384];
                System.arraycopy(data, 0, newData, 0, pos);
                data = newData;
            }
        }
    }

    public void read(InputStream stream) {
        try {
            int[] len = new int[1];
            byte[] raw = readFully(stream, len);
            Parcel in = Parcel.obtain();
            in.unmarshall(raw, 0, len[0]);
            in.setDataPosition(0);
            stream.close();
            readFromParcel(in);
        } catch (IOException e) {
            this.mReadError = "caught exception: " + e;
        }
    }

    public void readFromParcel(Parcel in) {
        PackageState pkgState;
        long vers;
        int uid;
        LongSparseArray<PackageState> vpkg;
        ServiceState serv;
        int uid2;
        boolean z = false;
        boolean hadData = this.mPackages.getMap().size() > 0 || this.mProcesses.getMap().size() > 0;
        if (hadData) {
            resetSafely();
        }
        if (!readCheckedInt(in, MAGIC, "magic number")) {
            return;
        }
        int version = in.readInt();
        if (version == 27) {
            if (!readCheckedInt(in, 14, "state count") || !readCheckedInt(in, 8, "adj count") || !readCheckedInt(in, 10, "pss count") || !readCheckedInt(in, 16, "sys mem usage count") || !readCheckedInt(in, 4096, "longs size")) {
                return;
            }
            this.mIndexToCommonString = new ArrayList<>();
            this.mTimePeriodStartClock = in.readLong();
            buildTimePeriodStartClockStr();
            this.mTimePeriodStartRealtime = in.readLong();
            this.mTimePeriodEndRealtime = in.readLong();
            this.mTimePeriodStartUptime = in.readLong();
            this.mTimePeriodEndUptime = in.readLong();
            this.mInternalSinglePssCount = in.readLong();
            this.mInternalSinglePssTime = in.readLong();
            this.mInternalAllMemPssCount = in.readLong();
            this.mInternalAllMemPssTime = in.readLong();
            this.mInternalAllPollPssCount = in.readLong();
            this.mInternalAllPollPssTime = in.readLong();
            this.mExternalPssCount = in.readLong();
            this.mExternalPssTime = in.readLong();
            this.mExternalSlowPssCount = in.readLong();
            this.mExternalSlowPssTime = in.readLong();
            this.mRuntime = in.readString();
            this.mHasSwappedOutPss = in.readInt() != 0;
            this.mFlags = in.readInt();
            this.mTableData.readFromParcel(in);
            readCompactedLongArray(in, version, this.mMemFactorDurations, this.mMemFactorDurations.length);
            if (!this.mSysMemUsage.readFromParcel(in)) {
                return;
            }
            int NPROC = in.readInt();
            if (NPROC < 0) {
                this.mReadError = "bad process count: " + NPROC;
                return;
            }
            int NPROC2 = NPROC;
            while (NPROC2 > 0) {
                int NPROC3 = NPROC2 - 1;
                String procName = readCommonString(in, version);
                if (procName == null) {
                    this.mReadError = "bad process name";
                    return;
                }
                int NUID = in.readInt();
                if (NUID < 0) {
                    this.mReadError = "bad uid count: " + NUID;
                    return;
                }
                while (NUID > 0) {
                    int NUID2 = NUID - 1;
                    int uid3 = in.readInt();
                    if (uid3 < 0) {
                        this.mReadError = "bad uid: " + uid3;
                        return;
                    }
                    String pkgName = readCommonString(in, version);
                    if (pkgName == null) {
                        this.mReadError = "bad process package name";
                        return;
                    }
                    long vers2 = in.readLong();
                    ProcessState proc = hadData ? this.mProcesses.get(procName, uid3) : null;
                    if (proc != null) {
                        if (!proc.readFromParcel(in, false)) {
                            return;
                        }
                        uid2 = uid3;
                    } else {
                        uid2 = uid3;
                        proc = new ProcessState(this, pkgName, uid3, vers2, procName);
                        if (!proc.readFromParcel(in, true)) {
                            return;
                        }
                    }
                    this.mProcesses.put(procName, uid2, proc);
                    NUID = NUID2;
                }
                NPROC2 = NPROC3;
            }
            int NPKG = in.readInt();
            if (NPKG < 0) {
                this.mReadError = "bad package count: " + NPKG;
                return;
            }
            while (NPKG > 0) {
                int NPKG2 = NPKG - 1;
                String pkgName2 = readCommonString(in, version);
                if (pkgName2 == null) {
                    this.mReadError = "bad package name";
                    return;
                }
                int NUID3 = in.readInt();
                if (NUID3 < 0) {
                    this.mReadError = "bad uid count: " + NUID3;
                    return;
                }
                while (NUID3 > 0) {
                    int NUID4 = NUID3 - 1;
                    int uid4 = in.readInt();
                    if (uid4 < 0) {
                        this.mReadError = "bad uid: " + uid4;
                        return;
                    }
                    int NVERS = in.readInt();
                    if (NVERS < 0) {
                        this.mReadError = "bad versions count: " + NVERS;
                        return;
                    }
                    while (NVERS > 0) {
                        int NVERS2 = NVERS - 1;
                        long vers3 = in.readLong();
                        PackageState pkgState2 = new PackageState(pkgName2, uid4);
                        LongSparseArray<PackageState> vpkg2 = this.mPackages.get(pkgName2, uid4);
                        if (vpkg2 == null) {
                            vpkg2 = new LongSparseArray<>();
                            this.mPackages.put(pkgName2, uid4, vpkg2);
                        }
                        vpkg2.put(vers3, pkgState2);
                        int NPROCS = in.readInt();
                        if (NPROCS < 0) {
                            this.mReadError = "bad package process count: " + NPROCS;
                            return;
                        }
                        int NPROCS2 = NPROCS;
                        while (NPROCS2 > 0) {
                            NPROCS2--;
                            String procName2 = readCommonString(in, version);
                            if (procName2 == null) {
                                this.mReadError = "bad package process name";
                                return;
                            }
                            int hasProc = in.readInt();
                            ProcessState commonProc = this.mProcesses.get(procName2, uid4);
                            if (commonProc == null) {
                                this.mReadError = "no common proc: " + procName2;
                                return;
                            }
                            LongSparseArray<PackageState> vpkg3 = vpkg2;
                            if (hasProc != 0) {
                                ProcessState proc2 = hadData ? pkgState2.mProcesses.get(procName2) : null;
                                if (proc2 != null) {
                                    if (!proc2.readFromParcel(in, z)) {
                                        return;
                                    }
                                } else {
                                    proc2 = new ProcessState(commonProc, pkgName2, uid4, vers3, procName2, 0L);
                                    if (!proc2.readFromParcel(in, true)) {
                                        return;
                                    }
                                }
                                pkgState2.mProcesses.put(procName2, proc2);
                            } else {
                                pkgState2.mProcesses.put(procName2, commonProc);
                            }
                            vpkg2 = vpkg3;
                            z = false;
                        }
                        LongSparseArray<PackageState> vpkg4 = vpkg2;
                        int NSRVS = in.readInt();
                        if (NSRVS < 0) {
                            this.mReadError = "bad package service count: " + NSRVS;
                            return;
                        }
                        while (NSRVS > 0) {
                            int NSRVS2 = NSRVS - 1;
                            String serviceName = in.readString();
                            if (serviceName == null) {
                                this.mReadError = "bad package service name";
                                return;
                            }
                            String processName = version > 9 ? readCommonString(in, version) : null;
                            ServiceState serv2 = hadData ? pkgState2.mServices.get(serviceName) : null;
                            if (serv2 == null) {
                                vpkg = vpkg4;
                                pkgState = pkgState2;
                                vers = vers3;
                                uid = uid4;
                                serv = new ServiceState(this, pkgName2, serviceName, processName, null);
                            } else {
                                pkgState = pkgState2;
                                vers = vers3;
                                uid = uid4;
                                vpkg = vpkg4;
                                serv = serv2;
                            }
                            if (!serv.readFromParcel(in)) {
                                return;
                            }
                            pkgState2 = pkgState;
                            pkgState2.mServices.put(serviceName, serv);
                            NSRVS = NSRVS2;
                            vpkg4 = vpkg;
                            vers3 = vers;
                            uid4 = uid;
                        }
                        NVERS = NVERS2;
                        z = false;
                    }
                    NUID3 = NUID4;
                    z = false;
                }
                NPKG = NPKG2;
                z = false;
            }
            int NPAGETYPES = in.readInt();
            this.mPageTypeZones.clear();
            this.mPageTypeZones.ensureCapacity(NPAGETYPES);
            this.mPageTypeLabels.clear();
            this.mPageTypeLabels.ensureCapacity(NPAGETYPES);
            this.mPageTypeSizes.clear();
            this.mPageTypeSizes.ensureCapacity(NPAGETYPES);
            int i = 0;
            while (true) {
                int i2 = i;
                if (i2 < NPAGETYPES) {
                    this.mPageTypeZones.add(Integer.valueOf(in.readInt()));
                    this.mPageTypeLabels.add(in.readString());
                    this.mPageTypeSizes.add(in.createIntArray());
                    i = i2 + 1;
                } else {
                    this.mIndexToCommonString = null;
                    return;
                }
            }
        } else {
            this.mReadError = "bad version: " + version;
        }
    }

    public PackageState getPackageStateLocked(String packageName, int uid, long vers) {
        LongSparseArray<PackageState> vpkg = this.mPackages.get(packageName, uid);
        if (vpkg == null) {
            vpkg = new LongSparseArray<>();
            this.mPackages.put(packageName, uid, vpkg);
        }
        PackageState as = vpkg.get(vers);
        if (as != null) {
            return as;
        }
        PackageState as2 = new PackageState(packageName, uid);
        vpkg.put(vers, as2);
        return as2;
    }

    public ProcessState getProcessStateLocked(String packageName, int uid, long vers, String processName) {
        ProcessState commonProc;
        PackageState pkgState;
        ProcessState ps;
        PackageState pkgState2 = getPackageStateLocked(packageName, uid, vers);
        ProcessState ps2 = pkgState2.mProcesses.get(processName);
        if (ps2 == null) {
            ProcessState commonProc2 = this.mProcesses.get(processName, uid);
            if (commonProc2 == null) {
                commonProc = new ProcessState(this, packageName, uid, vers, processName);
                this.mProcesses.put(processName, uid, commonProc);
            } else {
                commonProc = commonProc2;
            }
            if (!commonProc.isMultiPackage()) {
                if (packageName.equals(commonProc.getPackage()) && vers == commonProc.getVersion()) {
                    ps = commonProc;
                    pkgState = pkgState2;
                } else {
                    commonProc.setMultiPackage(true);
                    long now = SystemClock.uptimeMillis();
                    PackageState commonPkgState = getPackageStateLocked(commonProc.getPackage(), uid, commonProc.getVersion());
                    if (commonPkgState != null) {
                        ProcessState cloned = commonProc.clone(now);
                        commonPkgState.mProcesses.put(commonProc.getName(), cloned);
                        int i = commonPkgState.mServices.size() - 1;
                        while (true) {
                            int i2 = i;
                            if (i2 < 0) {
                                break;
                            }
                            ServiceState ss = commonPkgState.mServices.valueAt(i2);
                            if (ss.getProcess() == commonProc) {
                                ss.setProcess(cloned);
                            }
                            i = i2 - 1;
                        }
                    } else {
                        Slog.w(TAG, "Cloning proc state: no package state " + commonProc.getPackage() + "/" + uid + " for proc " + commonProc.getName());
                    }
                    pkgState = pkgState2;
                    ps = new ProcessState(commonProc, packageName, uid, vers, processName, now);
                }
            } else {
                pkgState = pkgState2;
                ps = new ProcessState(commonProc, packageName, uid, vers, processName, SystemClock.uptimeMillis());
            }
            pkgState.mProcesses.put(processName, ps);
            return ps;
        }
        return ps2;
    }

    public ServiceState getServiceStateLocked(String packageName, int uid, long vers, String processName, String className) {
        PackageState as = getPackageStateLocked(packageName, uid, vers);
        ServiceState ss = as.mServices.get(className);
        if (ss != null) {
            return ss;
        }
        ProcessState ps = processName != null ? getProcessStateLocked(packageName, uid, vers, processName) : null;
        ServiceState ss2 = new ServiceState(this, packageName, className, processName, ps);
        as.mServices.put(className, ss2);
        return ss2;
    }

    /* JADX WARN: Removed duplicated region for block: B:82:0x023f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void dumpLocked(java.io.PrintWriter r38, java.lang.String r39, long r40, boolean r42, boolean r43, boolean r44) {
        /*
            Method dump skipped, instructions count: 1131
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.app.procstats.ProcessStats.dumpLocked(java.io.PrintWriter, java.lang.String, long, boolean, boolean, boolean):void");
    }

    public void dumpSummaryLocked(PrintWriter pw, String reqPackage, long now, boolean activeOnly) {
        long totalTime = DumpUtils.dumpSingleTime(null, null, this.mMemFactorDurations, this.mMemFactor, this.mStartTime, now);
        dumpFilteredSummaryLocked(pw, null, "  ", ALL_SCREEN_ADJ, ALL_MEM_ADJ, ALL_PROC_STATES, NON_CACHED_PROC_STATES, now, totalTime, reqPackage, activeOnly);
        pw.println();
        dumpTotalsLocked(pw, now);
    }

    private void dumpFragmentationLocked(PrintWriter pw) {
        pw.println();
        pw.println("Available pages by page size:");
        int NPAGETYPES = this.mPageTypeLabels.size();
        for (int i = 0; i < NPAGETYPES; i++) {
            pw.format("Zone %3d  %14s ", this.mPageTypeZones.get(i), this.mPageTypeLabels.get(i));
            int[] sizes = this.mPageTypeSizes.get(i);
            int N = sizes == null ? 0 : sizes.length;
            for (int j = 0; j < N; j++) {
                pw.format("%6d", Integer.valueOf(sizes[j]));
            }
            pw.println();
        }
    }

    long printMemoryCategory(PrintWriter pw, String prefix, String label, double memWeight, long totalTime, long curTotalMem, int samples) {
        if (memWeight != FeatureOption.FO_BOOT_POLICY_CPU) {
            long mem = (long) ((1024.0d * memWeight) / totalTime);
            pw.print(prefix);
            pw.print(label);
            pw.print(": ");
            DebugUtils.printSizeValue(pw, mem);
            pw.print(" (");
            pw.print(samples);
            pw.print(" samples)");
            pw.println();
            return curTotalMem + mem;
        }
        return curTotalMem;
    }

    void dumpTotalsLocked(PrintWriter pw, long now) {
        int i;
        pw.println("Run time Stats:");
        DumpUtils.dumpSingleTime(pw, "  ", this.mMemFactorDurations, this.mMemFactor, this.mStartTime, now);
        pw.println();
        pw.println("Memory usage:");
        TotalMemoryUseCollection totalMem = new TotalMemoryUseCollection(ALL_SCREEN_ADJ, ALL_MEM_ADJ);
        computeTotalMemoryUse(totalMem, now);
        long totalPss = printMemoryCategory(pw, "  ", "Kernel ", totalMem.sysMemKernelWeight, totalMem.totalTime, 0L, totalMem.sysMemSamples);
        int i2 = 0;
        long totalPss2 = printMemoryCategory(pw, "  ", "Native ", totalMem.sysMemNativeWeight, totalMem.totalTime, totalPss, totalMem.sysMemSamples);
        while (true) {
            int i3 = i2;
            if (i3 >= 14) {
                break;
            }
            if (i3 == 6) {
                i = i3;
            } else {
                i = i3;
                totalPss2 = printMemoryCategory(pw, "  ", DumpUtils.STATE_NAMES[i3], totalMem.processStateWeight[i3], totalMem.totalTime, totalPss2, totalMem.processStateSamples[i3]);
            }
            i2 = i + 1;
        }
        long totalPss3 = printMemoryCategory(pw, "  ", "Z-Ram  ", totalMem.sysMemZRamWeight, totalMem.totalTime, printMemoryCategory(pw, "  ", "Free   ", totalMem.sysMemFreeWeight, totalMem.totalTime, printMemoryCategory(pw, "  ", "Cached ", totalMem.sysMemCachedWeight, totalMem.totalTime, totalPss2, totalMem.sysMemSamples), totalMem.sysMemSamples), totalMem.sysMemSamples);
        pw.print("  TOTAL  : ");
        DebugUtils.printSizeValue(pw, totalPss3);
        pw.println();
        printMemoryCategory(pw, "  ", DumpUtils.STATE_NAMES[6], totalMem.processStateWeight[6], totalMem.totalTime, totalPss3, totalMem.processStateSamples[6]);
        pw.println();
        pw.println("PSS collection stats:");
        pw.print("  Internal Single: ");
        pw.print(this.mInternalSinglePssCount);
        pw.print("x over ");
        TimeUtils.formatDuration(this.mInternalSinglePssTime, pw);
        pw.println();
        pw.print("  Internal All Procs (Memory Change): ");
        pw.print(this.mInternalAllMemPssCount);
        pw.print("x over ");
        TimeUtils.formatDuration(this.mInternalAllMemPssTime, pw);
        pw.println();
        pw.print("  Internal All Procs (Polling): ");
        pw.print(this.mInternalAllPollPssCount);
        pw.print("x over ");
        TimeUtils.formatDuration(this.mInternalAllPollPssTime, pw);
        pw.println();
        pw.print("  External: ");
        pw.print(this.mExternalPssCount);
        pw.print("x over ");
        TimeUtils.formatDuration(this.mExternalPssTime, pw);
        pw.println();
        pw.print("  External Slow: ");
        pw.print(this.mExternalSlowPssCount);
        pw.print("x over ");
        TimeUtils.formatDuration(this.mExternalSlowPssTime, pw);
        pw.println();
        pw.println();
        pw.print("          Start time: ");
        pw.print(DateFormat.format("yyyy-MM-dd HH:mm:ss", this.mTimePeriodStartClock));
        pw.println();
        pw.print("        Total uptime: ");
        TimeUtils.formatDuration((this.mRunning ? SystemClock.uptimeMillis() : this.mTimePeriodEndUptime) - this.mTimePeriodStartUptime, pw);
        pw.println();
        pw.print("  Total elapsed time: ");
        TimeUtils.formatDuration((this.mRunning ? SystemClock.elapsedRealtime() : this.mTimePeriodEndRealtime) - this.mTimePeriodStartRealtime, pw);
        boolean partial = true;
        if ((this.mFlags & 2) != 0) {
            pw.print(" (shutdown)");
            partial = false;
        }
        if ((this.mFlags & 4) != 0) {
            pw.print(" (sysprops)");
            partial = false;
        }
        if ((this.mFlags & 1) != 0) {
            pw.print(" (complete)");
            partial = false;
        }
        if (partial) {
            pw.print(" (partial)");
        }
        if (this.mHasSwappedOutPss) {
            pw.print(" (swapped-out-pss)");
        }
        pw.print(' ');
        pw.print(this.mRuntime);
        pw.println();
    }

    void dumpFilteredSummaryLocked(PrintWriter pw, String header, String prefix, int[] screenStates, int[] memStates, int[] procStates, int[] sortProcStates, long now, long totalTime, String reqPackage, boolean activeOnly) {
        ArrayList<ProcessState> procs = collectProcessesLocked(screenStates, memStates, procStates, sortProcStates, now, reqPackage, activeOnly);
        if (procs.size() > 0) {
            if (header != null) {
                pw.println();
                pw.println(header);
            }
            DumpUtils.dumpProcessSummaryLocked(pw, prefix, procs, screenStates, memStates, sortProcStates, now, totalTime);
        }
    }

    public ArrayList<ProcessState> collectProcessesLocked(int[] screenStates, int[] memStates, int[] procStates, int[] sortProcStates, long now, String reqPackage, boolean activeOnly) {
        String str = reqPackage;
        ArraySet<ProcessState> foundProcs = new ArraySet<>();
        ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap = this.mPackages.getMap();
        int ip = 0;
        while (ip < pkgMap.size()) {
            String pkgName = pkgMap.keyAt(ip);
            SparseArray<LongSparseArray<PackageState>> procs = pkgMap.valueAt(ip);
            int iu = 0;
            while (iu < procs.size()) {
                LongSparseArray<PackageState> vpkgs = procs.valueAt(iu);
                int NVERS = vpkgs.size();
                int iv = 0;
                while (iv < NVERS) {
                    PackageState state = vpkgs.valueAt(iv);
                    int NPROCS = state.mProcesses.size();
                    boolean pkgMatch = str == null || str.equals(pkgName);
                    int iproc = 0;
                    while (iproc < NPROCS) {
                        ProcessState proc = state.mProcesses.valueAt(iproc);
                        if ((pkgMatch || str.equals(proc.getName())) && (!activeOnly || proc.isInUse())) {
                            foundProcs.add(proc.getCommonProcess());
                        }
                        iproc++;
                        str = reqPackage;
                    }
                    iv++;
                    str = reqPackage;
                }
                iu++;
                str = reqPackage;
            }
            ip++;
            str = reqPackage;
        }
        ArrayList<ProcessState> outProcs = new ArrayList<>(foundProcs.size());
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 < foundProcs.size()) {
                ProcessState proc2 = foundProcs.valueAt(i2);
                if (proc2.computeProcessTimeLocked(screenStates, memStates, procStates, now) > 0) {
                    outProcs.add(proc2);
                    if (procStates != sortProcStates) {
                        proc2.computeProcessTimeLocked(screenStates, memStates, sortProcStates, now);
                    }
                }
                i = i2 + 1;
            } else {
                Collections.sort(outProcs, ProcessState.COMPARATOR);
                return outProcs;
            }
        }
    }

    public void dumpCheckinLocked(PrintWriter pw, String reqPackage) {
        String str = reqPackage;
        long now = SystemClock.uptimeMillis();
        ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap = this.mPackages.getMap();
        pw.println("vers,5");
        pw.print("period,");
        pw.print(this.mTimePeriodStartClockStr);
        pw.print(",");
        pw.print(this.mTimePeriodStartRealtime);
        pw.print(",");
        pw.print(this.mRunning ? SystemClock.elapsedRealtime() : this.mTimePeriodEndRealtime);
        boolean partial = true;
        if ((this.mFlags & 2) != 0) {
            pw.print(",shutdown");
            partial = false;
        }
        if ((this.mFlags & 4) != 0) {
            pw.print(",sysprops");
            partial = false;
        }
        if ((this.mFlags & 1) != 0) {
            pw.print(",complete");
            partial = false;
        }
        if (partial) {
            pw.print(",partial");
        }
        if (this.mHasSwappedOutPss) {
            pw.print(",swapped-out-pss");
        }
        pw.println();
        pw.print("config,");
        pw.println(this.mRuntime);
        int ip = 0;
        while (true) {
            int ip2 = ip;
            int ip3 = pkgMap.size();
            if (ip2 >= ip3) {
                break;
            }
            String pkgName = pkgMap.keyAt(ip2);
            if (str == null || str.equals(pkgName)) {
                SparseArray<LongSparseArray<PackageState>> uids = pkgMap.valueAt(ip2);
                int iu = 0;
                while (true) {
                    int iu2 = iu;
                    int iu3 = uids.size();
                    if (iu2 < iu3) {
                        int uid = uids.keyAt(iu2);
                        LongSparseArray<PackageState> vpkgs = uids.valueAt(iu2);
                        int isvc = 0;
                        while (true) {
                            int iv = isvc;
                            if (iv < vpkgs.size()) {
                                long vers = vpkgs.keyAt(iv);
                                PackageState pkgState = vpkgs.valueAt(iv);
                                int NPROCS = pkgState.mProcesses.size();
                                int NSRVS = pkgState.mServices.size();
                                int iproc = 0;
                                while (true) {
                                    int iproc2 = iproc;
                                    if (iproc2 >= NPROCS) {
                                        break;
                                    }
                                    ProcessState proc = pkgState.mProcesses.valueAt(iproc2);
                                    int NSRVS2 = NPROCS;
                                    proc.dumpPackageProcCheckin(pw, pkgName, uid, vers, pkgState.mProcesses.keyAt(iproc2), now);
                                    iproc = iproc2 + 1;
                                    pkgName = pkgName;
                                    NSRVS = NSRVS;
                                    pkgState = pkgState;
                                    ip2 = ip2;
                                    NPROCS = NSRVS2;
                                    pkgMap = pkgMap;
                                    iv = iv;
                                    iu2 = iu2;
                                    vpkgs = vpkgs;
                                    uids = uids;
                                }
                                int NSRVS3 = NSRVS;
                                int iv2 = iv;
                                int iu4 = iu2;
                                LongSparseArray<PackageState> vpkgs2 = vpkgs;
                                SparseArray<LongSparseArray<PackageState>> uids2 = uids;
                                int ip4 = ip2;
                                String pkgName2 = pkgName;
                                ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap2 = pkgMap;
                                PackageState pkgState2 = pkgState;
                                int isvc2 = 0;
                                while (true) {
                                    int isvc3 = isvc2;
                                    if (isvc3 < NSRVS3) {
                                        String serviceName = DumpUtils.collapseString(pkgName2, pkgState2.mServices.keyAt(isvc3));
                                        ServiceState svc = pkgState2.mServices.valueAt(isvc3);
                                        svc.dumpTimesCheckin(pw, pkgName2, uid, vers, serviceName, now);
                                        isvc2 = isvc3 + 1;
                                    }
                                }
                                isvc = iv2 + 1;
                                pkgName = pkgName2;
                                ip2 = ip4;
                                pkgMap = pkgMap2;
                                iu2 = iu4;
                                vpkgs = vpkgs2;
                                uids = uids2;
                            }
                        }
                        iu = iu2 + 1;
                    }
                }
            }
            ip = ip2 + 1;
            pkgMap = pkgMap;
            str = reqPackage;
        }
        ArrayMap<String, SparseArray<ProcessState>> procMap = this.mProcesses.getMap();
        int iu5 = 0;
        while (true) {
            int ip5 = iu5;
            int ip6 = procMap.size();
            if (ip5 >= ip6) {
                break;
            }
            String procName = procMap.keyAt(ip5);
            SparseArray<ProcessState> uids3 = procMap.valueAt(ip5);
            int iu6 = 0;
            while (true) {
                int iu7 = iu6;
                int iu8 = uids3.size();
                if (iu7 < iu8) {
                    int uid2 = uids3.keyAt(iu7);
                    ProcessState procState = uids3.valueAt(iu7);
                    procState.dumpProcCheckin(pw, procName, uid2, now);
                    iu6 = iu7 + 1;
                }
            }
            iu5 = ip5 + 1;
        }
        pw.print("total");
        DumpUtils.dumpAdjTimesCheckin(pw, ",", this.mMemFactorDurations, this.mMemFactor, this.mStartTime, now);
        pw.println();
        int sysMemUsageCount = this.mSysMemUsage.getKeyCount();
        if (sysMemUsageCount > 0) {
            pw.print("sysmemusage");
            for (int i = 0; i < sysMemUsageCount; i++) {
                int key = this.mSysMemUsage.getKeyAt(i);
                int type = SparseMappingTable.getIdFromKey(key);
                pw.print(",");
                DumpUtils.printProcStateTag(pw, type);
                for (int j = 0; j < 16; j++) {
                    if (j > 1) {
                        pw.print(SettingsStringUtil.DELIMITER);
                    }
                    pw.print(this.mSysMemUsage.getValue(key, j));
                }
            }
        }
        pw.println();
        TotalMemoryUseCollection totalMem = new TotalMemoryUseCollection(ALL_SCREEN_ADJ, ALL_MEM_ADJ);
        computeTotalMemoryUse(totalMem, now);
        pw.print("weights,");
        pw.print(totalMem.totalTime);
        pw.print(",");
        pw.print(totalMem.sysMemCachedWeight);
        pw.print(SettingsStringUtil.DELIMITER);
        pw.print(totalMem.sysMemSamples);
        pw.print(",");
        pw.print(totalMem.sysMemFreeWeight);
        pw.print(SettingsStringUtil.DELIMITER);
        pw.print(totalMem.sysMemSamples);
        pw.print(",");
        pw.print(totalMem.sysMemZRamWeight);
        pw.print(SettingsStringUtil.DELIMITER);
        pw.print(totalMem.sysMemSamples);
        pw.print(",");
        pw.print(totalMem.sysMemKernelWeight);
        pw.print(SettingsStringUtil.DELIMITER);
        pw.print(totalMem.sysMemSamples);
        pw.print(",");
        pw.print(totalMem.sysMemNativeWeight);
        pw.print(SettingsStringUtil.DELIMITER);
        pw.print(totalMem.sysMemSamples);
        for (int i2 = 0; i2 < 14; i2++) {
            pw.print(",");
            pw.print(totalMem.processStateWeight[i2]);
            pw.print(SettingsStringUtil.DELIMITER);
            pw.print(totalMem.processStateSamples[i2]);
        }
        pw.println();
        int NPAGETYPES = this.mPageTypeLabels.size();
        for (int i3 = 0; i3 < NPAGETYPES; i3++) {
            pw.print("availablepages,");
            pw.print(this.mPageTypeLabels.get(i3));
            pw.print(",");
            pw.print(this.mPageTypeZones.get(i3));
            pw.print(",");
            int[] sizes = this.mPageTypeSizes.get(i3);
            int N = sizes == null ? 0 : sizes.length;
            for (int j2 = 0; j2 < N; j2++) {
                if (j2 != 0) {
                    pw.print(",");
                }
                pw.print(sizes[j2]);
            }
            pw.println();
        }
    }

    public void writeToProto(ProtoOutputStream proto, long fieldId, long now) {
        this.mPackages.getMap();
        long token = proto.start(fieldId);
        proto.write(1112396529665L, this.mTimePeriodStartRealtime);
        proto.write(1112396529666L, this.mRunning ? SystemClock.elapsedRealtime() : this.mTimePeriodEndRealtime);
        proto.write(1112396529667L, this.mTimePeriodStartUptime);
        proto.write(1112396529668L, this.mTimePeriodEndUptime);
        proto.write(1138166333445L, this.mRuntime);
        proto.write(1133871366150L, this.mHasSwappedOutPss);
        boolean partial = true;
        if ((this.mFlags & 2) != 0) {
            proto.write(2259152797703L, 3);
            partial = false;
        }
        if ((this.mFlags & 4) != 0) {
            proto.write(2259152797703L, 4);
            partial = false;
        }
        if ((this.mFlags & 1) != 0) {
            proto.write(2259152797703L, 1);
            partial = false;
        }
        if (partial) {
            proto.write(2259152797703L, 2);
        }
        ArrayMap<String, SparseArray<ProcessState>> procMap = this.mProcesses.getMap();
        int ip = 0;
        while (true) {
            int ip2 = ip;
            int ip3 = procMap.size();
            if (ip2 < ip3) {
                String procName = procMap.keyAt(ip2);
                SparseArray<ProcessState> uids = procMap.valueAt(ip2);
                int iu = 0;
                while (true) {
                    int iu2 = iu;
                    int iu3 = uids.size();
                    if (iu2 < iu3) {
                        int uid = uids.keyAt(iu2);
                        ProcessState procState = uids.valueAt(iu2);
                        procState.writeToProto(proto, 2246267895816L, procName, uid, now);
                        iu = iu2 + 1;
                        ip2 = ip2;
                        uids = uids;
                    }
                }
                ip = ip2 + 1;
            } else {
                proto.end(token);
                return;
            }
        }
    }

    /* loaded from: classes3.dex */
    public static final class ProcessStateHolder {
        public final long appVersion;
        public ProcessState state;

        public ProcessStateHolder(long _appVersion) {
            this.appVersion = _appVersion;
        }
    }

    /* loaded from: classes3.dex */
    public static final class PackageState {
        public final String mPackageName;
        public final ArrayMap<String, ProcessState> mProcesses = new ArrayMap<>();
        public final ArrayMap<String, ServiceState> mServices = new ArrayMap<>();
        public final int mUid;

        public PackageState(String packageName, int uid) {
            this.mUid = uid;
            this.mPackageName = packageName;
        }
    }

    /* loaded from: classes3.dex */
    public static final class ProcessDataCollection {
        public long avgPss;
        public long avgRss;
        public long avgUss;
        public long maxPss;
        public long maxRss;
        public long maxUss;
        final int[] memStates;
        public long minPss;
        public long minRss;
        public long minUss;
        public long numPss;
        final int[] procStates;
        final int[] screenStates;
        public long totalTime;

        public ProcessDataCollection(int[] _screenStates, int[] _memStates, int[] _procStates) {
            this.screenStates = _screenStates;
            this.memStates = _memStates;
            this.procStates = _procStates;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void print(PrintWriter pw, long overallTime, boolean full) {
            if (this.totalTime > overallTime) {
                pw.print(PhoneConstants.APN_TYPE_ALL);
            }
            DumpUtils.printPercent(pw, this.totalTime / overallTime);
            if (this.numPss > 0) {
                pw.print(" (");
                DebugUtils.printSizeValue(pw, this.minPss * 1024);
                pw.print(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
                DebugUtils.printSizeValue(pw, this.avgPss * 1024);
                pw.print(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
                DebugUtils.printSizeValue(pw, this.maxPss * 1024);
                pw.print("/");
                DebugUtils.printSizeValue(pw, this.minUss * 1024);
                pw.print(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
                DebugUtils.printSizeValue(pw, this.avgUss * 1024);
                pw.print(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
                DebugUtils.printSizeValue(pw, this.maxUss * 1024);
                pw.print("/");
                DebugUtils.printSizeValue(pw, this.minRss * 1024);
                pw.print(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
                DebugUtils.printSizeValue(pw, this.avgRss * 1024);
                pw.print(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
                DebugUtils.printSizeValue(pw, this.maxRss * 1024);
                if (full) {
                    pw.print(" over ");
                    pw.print(this.numPss);
                }
                pw.print(")");
            }
        }
    }

    /* loaded from: classes3.dex */
    public static class TotalMemoryUseCollection {
        public boolean hasSwappedOutPss;
        final int[] memStates;
        final int[] screenStates;
        public double sysMemCachedWeight;
        public double sysMemFreeWeight;
        public double sysMemKernelWeight;
        public double sysMemNativeWeight;
        public int sysMemSamples;
        public double sysMemZRamWeight;
        public long totalTime;
        public long[] processStatePss = new long[14];
        public double[] processStateWeight = new double[14];
        public long[] processStateTime = new long[14];
        public int[] processStateSamples = new int[14];
        public long[] sysMemUsage = new long[16];

        public TotalMemoryUseCollection(int[] _screenStates, int[] _memStates) {
            this.screenStates = _screenStates;
            this.memStates = _memStates;
        }
    }
}
