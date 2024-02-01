package com.android.internal.os;

import android.os.Binder;
import android.os.Process;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.ArrayMap;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.os.BinderCallsStats;
import com.android.internal.os.BinderInternal;
import com.android.internal.os.CachedDeviceState;
import com.xiaopeng.util.FeatureOption;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.ToDoubleFunction;

/* loaded from: classes3.dex */
public class BinderCallsStats implements BinderInternal.Observer {
    private static final int CALL_SESSIONS_POOL_SIZE = 100;
    private static final String DEBUG_ENTRY_PREFIX = "__DEBUG_";
    public static final boolean DEFAULT_TRACK_DIRECT_CALLING_UID = true;
    public static final boolean DEFAULT_TRACK_SCREEN_INTERACTIVE = false;
    public static final boolean DETAILED_TRACKING_DEFAULT = true;
    public static final boolean ENABLED_DEFAULT = true;
    private static final String EXCEPTION_COUNT_OVERFLOW_NAME = "overflow";
    public static final int MAX_BINDER_CALL_STATS_COUNT_DEFAULT = 1500;
    private static final int MAX_EXCEPTION_COUNT_SIZE = 50;
    private static final Class<? extends Binder> OVERFLOW_BINDER = OverflowBinder.class;
    private static final int OVERFLOW_DIRECT_CALLING_UID = -1;
    private static final boolean OVERFLOW_SCREEN_INTERACTIVE = false;
    private static final int OVERFLOW_TRANSACTION_CODE = -1;
    public static final int PERIODIC_SAMPLING_INTERVAL_DEFAULT = 1000;
    private static final String TAG = "BinderCallsStats";
    private CachedDeviceState.TimeInStateStopwatch mBatteryStopwatch;
    private CachedDeviceState.Readonly mDeviceState;
    private final Random mRandom;
    private boolean mDetailedTracking = true;
    private int mPeriodicSamplingInterval = 1000;
    private int mMaxBinderCallStatsCount = 1500;
    @GuardedBy({"mLock"})
    private final SparseArray<UidEntry> mUidEntries = new SparseArray<>();
    @GuardedBy({"mLock"})
    private final ArrayMap<String, Integer> mExceptionCounts = new ArrayMap<>();
    private final Queue<BinderInternal.CallSession> mCallSessionsPool = new ConcurrentLinkedQueue();
    private final Object mLock = new Object();
    private long mStartCurrentTime = System.currentTimeMillis();
    private long mStartElapsedTime = SystemClock.elapsedRealtime();
    private long mCallStatsCount = 0;
    private boolean mAddDebugEntries = false;
    private boolean mTrackDirectCallingUid = true;
    private boolean mTrackScreenInteractive = false;

    /* loaded from: classes3.dex */
    public static class ExportedCallStat {
        Class<? extends Binder> binderClass;
        public long callCount;
        public int callingUid;
        public String className;
        public long cpuTimeMicros;
        public long exceptionCount;
        public long latencyMicros;
        public long maxCpuTimeMicros;
        public long maxLatencyMicros;
        public long maxReplySizeBytes;
        public long maxRequestSizeBytes;
        public String methodName;
        public long recordedCallCount;
        public boolean screenInteractive;
        int transactionCode;
        public int workSourceUid;
    }

    /* loaded from: classes3.dex */
    private static class OverflowBinder extends Binder {
        private OverflowBinder() {
        }
    }

    /* loaded from: classes3.dex */
    public static class Injector {
        public Random getRandomGenerator() {
            return new Random();
        }
    }

    public BinderCallsStats(Injector injector) {
        this.mRandom = injector.getRandomGenerator();
    }

    public void setDeviceState(CachedDeviceState.Readonly deviceState) {
        CachedDeviceState.TimeInStateStopwatch timeInStateStopwatch = this.mBatteryStopwatch;
        if (timeInStateStopwatch != null) {
            timeInStateStopwatch.close();
        }
        this.mDeviceState = deviceState;
        this.mBatteryStopwatch = deviceState.createTimeOnBatteryStopwatch();
    }

    @Override // com.android.internal.os.BinderInternal.Observer
    public BinderInternal.CallSession callStarted(Binder binder, int code, int workSourceUid) {
        CachedDeviceState.Readonly readonly = this.mDeviceState;
        if (readonly == null || readonly.isCharging()) {
            return null;
        }
        BinderInternal.CallSession s = obtainCallSession();
        s.binderClass = binder.getClass();
        s.transactionCode = code;
        s.exceptionThrown = false;
        s.cpuTimeStarted = -1L;
        s.timeStarted = -1L;
        if (shouldRecordDetailedData()) {
            s.cpuTimeStarted = getThreadTimeMicro();
            s.timeStarted = getElapsedRealtimeMicro();
        }
        return s;
    }

    private BinderInternal.CallSession obtainCallSession() {
        BinderInternal.CallSession s = this.mCallSessionsPool.poll();
        return s == null ? new BinderInternal.CallSession() : s;
    }

    @Override // com.android.internal.os.BinderInternal.Observer
    public void callEnded(BinderInternal.CallSession s, int parcelRequestSize, int parcelReplySize, int workSourceUid) {
        if (s == null) {
            return;
        }
        processCallEnded(s, parcelRequestSize, parcelReplySize, workSourceUid);
        if (this.mCallSessionsPool.size() < 100) {
            this.mCallSessionsPool.add(s);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v0 */
    /* JADX WARN: Type inference failed for: r5v1 */
    /* JADX WARN: Type inference failed for: r5v10 */
    /* JADX WARN: Type inference failed for: r5v2 */
    /* JADX WARN: Type inference failed for: r5v8 */
    private void processCallEnded(BinderInternal.CallSession s, int parcelRequestSize, int parcelReplySize, int workSourceUid) {
        long duration;
        long latencyDuration;
        Object obj;
        Object obj2;
        ?? r5 = 0;
        boolean recordCall = s.cpuTimeStarted >= 0;
        if (recordCall) {
            duration = getThreadTimeMicro() - s.cpuTimeStarted;
            latencyDuration = getElapsedRealtimeMicro() - s.timeStarted;
        } else {
            duration = 0;
            latencyDuration = 0;
        }
        boolean screenInteractive = this.mTrackScreenInteractive ? this.mDeviceState.isScreenInteractive() : false;
        int callingUid = this.mTrackDirectCallingUid ? getCallingUid() : -1;
        Object obj3 = this.mLock;
        synchronized (obj3) {
            try {
                try {
                    if (this.mDeviceState == null) {
                        obj = obj3;
                    } else if (!this.mDeviceState.isCharging()) {
                        UidEntry uidEntry = getUidEntry(workSourceUid);
                        long j = 1;
                        uidEntry.callCount++;
                        if (recordCall) {
                            uidEntry.cpuTimeMicros += duration;
                            uidEntry.recordedCallCount++;
                            try {
                                r5 = obj3;
                                try {
                                    CallStat callStat = uidEntry.getOrCreate(callingUid, s.binderClass, s.transactionCode, screenInteractive, this.mCallStatsCount >= ((long) this.mMaxBinderCallStatsCount));
                                    boolean isNewCallStat = callStat.callCount == 0;
                                    if (isNewCallStat) {
                                        try {
                                            this.mCallStatsCount++;
                                        } catch (Throwable th) {
                                            th = th;
                                            throw th;
                                        }
                                    }
                                    callStat.callCount++;
                                    callStat.recordedCallCount++;
                                    callStat.cpuTimeMicros += duration;
                                    callStat.maxCpuTimeMicros = Math.max(callStat.maxCpuTimeMicros, duration);
                                    callStat.latencyMicros += latencyDuration;
                                    callStat.maxLatencyMicros = Math.max(callStat.maxLatencyMicros, latencyDuration);
                                    if (this.mDetailedTracking) {
                                        long j2 = callStat.exceptionCount;
                                        if (!s.exceptionThrown) {
                                            j = 0;
                                        }
                                        callStat.exceptionCount = j2 + j;
                                        try {
                                            callStat.maxRequestSizeBytes = Math.max(callStat.maxRequestSizeBytes, parcelRequestSize);
                                            callStat.maxReplySizeBytes = Math.max(callStat.maxReplySizeBytes, parcelReplySize);
                                        } catch (Throwable th2) {
                                            th = th2;
                                            throw th;
                                        }
                                    }
                                    obj2 = r5;
                                } catch (Throwable th3) {
                                    th = th3;
                                }
                            } catch (Throwable th4) {
                                th = th4;
                                r5 = obj3;
                            }
                        } else {
                            Object obj4 = obj3;
                            CallStat callStat2 = uidEntry.get(callingUid, s.binderClass, s.transactionCode, screenInteractive);
                            obj2 = obj4;
                            if (callStat2 != null) {
                                callStat2.callCount++;
                                obj2 = obj4;
                            }
                        }
                        return;
                    } else {
                        obj = obj3;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    r5 = obj3;
                }
            } catch (Throwable th6) {
                th = th6;
            }
        }
    }

    private UidEntry getUidEntry(int uid) {
        UidEntry uidEntry = this.mUidEntries.get(uid);
        if (uidEntry == null) {
            UidEntry uidEntry2 = new UidEntry(uid);
            this.mUidEntries.put(uid, uidEntry2);
            return uidEntry2;
        }
        return uidEntry;
    }

    @Override // com.android.internal.os.BinderInternal.Observer
    public void callThrewException(BinderInternal.CallSession s, Exception exception) {
        if (s == null) {
            return;
        }
        int i = 1;
        s.exceptionThrown = true;
        try {
            String className = exception.getClass().getName();
            synchronized (this.mLock) {
                if (this.mExceptionCounts.size() >= 50) {
                    className = EXCEPTION_COUNT_OVERFLOW_NAME;
                }
                Integer count = this.mExceptionCounts.get(className);
                ArrayMap<String, Integer> arrayMap = this.mExceptionCounts;
                if (count != null) {
                    i = 1 + count.intValue();
                }
                arrayMap.put(className, Integer.valueOf(i));
            }
        } catch (RuntimeException e) {
            Slog.wtf(TAG, "Unexpected exception while updating mExceptionCounts");
        }
    }

    private Method getDefaultTransactionNameMethod(Class<? extends Binder> binder) {
        try {
            return binder.getMethod("getDefaultTransactionName", Integer.TYPE);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private String resolveTransactionCode(Method getDefaultTransactionName, int transactionCode) {
        if (getDefaultTransactionName == null) {
            return null;
        }
        try {
            return (String) getDefaultTransactionName.invoke(null, Integer.valueOf(transactionCode));
        } catch (ClassCastException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<ExportedCallStat> getExportedCallStats() {
        String str;
        String methodName;
        if (!this.mDetailedTracking) {
            return new ArrayList<>();
        }
        ArrayList<ExportedCallStat> resultCallStats = new ArrayList<>();
        synchronized (this.mLock) {
            int uidEntriesSize = this.mUidEntries.size();
            for (int entryIdx = 0; entryIdx < uidEntriesSize; entryIdx++) {
                UidEntry entry = this.mUidEntries.valueAt(entryIdx);
                for (CallStat stat : entry.getCallStatsList()) {
                    ExportedCallStat exported = new ExportedCallStat();
                    exported.workSourceUid = entry.workSourceUid;
                    exported.callingUid = stat.callingUid;
                    exported.className = stat.binderClass.getName();
                    exported.binderClass = stat.binderClass;
                    exported.transactionCode = stat.transactionCode;
                    exported.screenInteractive = stat.screenInteractive;
                    exported.cpuTimeMicros = stat.cpuTimeMicros;
                    exported.maxCpuTimeMicros = stat.maxCpuTimeMicros;
                    exported.latencyMicros = stat.latencyMicros;
                    exported.maxLatencyMicros = stat.maxLatencyMicros;
                    exported.recordedCallCount = stat.recordedCallCount;
                    exported.callCount = stat.callCount;
                    exported.maxRequestSizeBytes = stat.maxRequestSizeBytes;
                    exported.maxReplySizeBytes = stat.maxReplySizeBytes;
                    exported.exceptionCount = stat.exceptionCount;
                    resultCallStats.add(exported);
                }
            }
        }
        ExportedCallStat previous = null;
        Method getDefaultTransactionName = null;
        String previousMethodName = null;
        resultCallStats.sort(new Comparator() { // from class: com.android.internal.os.-$$Lambda$BinderCallsStats$sqXweH5BoxhmZvI188ctqYiACRk
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int compareByBinderClassAndCode;
                compareByBinderClassAndCode = BinderCallsStats.compareByBinderClassAndCode((BinderCallsStats.ExportedCallStat) obj, (BinderCallsStats.ExportedCallStat) obj2);
                return compareByBinderClassAndCode;
            }
        });
        Iterator<ExportedCallStat> it = resultCallStats.iterator();
        while (it.hasNext()) {
            ExportedCallStat exported2 = it.next();
            boolean isCodeDifferent = false;
            boolean isClassDifferent = 0 == 0 || !previous.className.equals(exported2.className);
            if (isClassDifferent) {
                getDefaultTransactionName = getDefaultTransactionNameMethod(exported2.binderClass);
            }
            isCodeDifferent = (0 == 0 || previous.transactionCode != exported2.transactionCode) ? true : true;
            if (isClassDifferent || isCodeDifferent) {
                String resolvedCode = resolveTransactionCode(getDefaultTransactionName, exported2.transactionCode);
                if (resolvedCode == null) {
                    str = String.valueOf(exported2.transactionCode);
                } else {
                    str = resolvedCode;
                }
                methodName = str;
            } else {
                methodName = previousMethodName;
            }
            previousMethodName = methodName;
            exported2.methodName = methodName;
        }
        if (this.mAddDebugEntries && this.mBatteryStopwatch != null) {
            resultCallStats.add(createDebugEntry("start_time_millis", this.mStartElapsedTime));
            resultCallStats.add(createDebugEntry("end_time_millis", SystemClock.elapsedRealtime()));
            resultCallStats.add(createDebugEntry("battery_time_millis", this.mBatteryStopwatch.getMillis()));
            resultCallStats.add(createDebugEntry("sampling_interval", this.mPeriodicSamplingInterval));
        }
        return resultCallStats;
    }

    private ExportedCallStat createDebugEntry(String variableName, long value) {
        int uid = Process.myUid();
        ExportedCallStat callStat = new ExportedCallStat();
        callStat.className = "";
        callStat.workSourceUid = uid;
        callStat.callingUid = uid;
        callStat.recordedCallCount = 1L;
        callStat.callCount = 1L;
        callStat.methodName = "__DEBUG_" + variableName;
        callStat.latencyMicros = value;
        return callStat;
    }

    public ArrayMap<String, Integer> getExportedExceptionStats() {
        ArrayMap<String, Integer> arrayMap;
        synchronized (this.mLock) {
            arrayMap = new ArrayMap<>(this.mExceptionCounts);
        }
        return arrayMap;
    }

    public void dump(PrintWriter pw, AppIdToPackageMap packageMap, boolean verbose) {
        synchronized (this.mLock) {
            dumpLocked(pw, packageMap, verbose);
        }
    }

    private void dumpLocked(PrintWriter pw, AppIdToPackageMap packageMap, boolean verbose) {
        AppIdToPackageMap appIdToPackageMap = packageMap;
        long totalCallsCount = 0;
        long totalRecordedCallsCount = 0;
        long totalCpuTime = 0;
        pw.print("Start time: ");
        pw.println(DateFormat.format("yyyy-MM-dd HH:mm:ss", this.mStartCurrentTime));
        pw.print("On battery time (ms): ");
        CachedDeviceState.TimeInStateStopwatch timeInStateStopwatch = this.mBatteryStopwatch;
        pw.println(timeInStateStopwatch != null ? timeInStateStopwatch.getMillis() : 0L);
        pw.println("Sampling interval period: " + this.mPeriodicSamplingInterval);
        List<UidEntry> entries = new ArrayList<>();
        int uidEntriesSize = this.mUidEntries.size();
        for (int i = 0; i < uidEntriesSize; i++) {
            UidEntry e = this.mUidEntries.valueAt(i);
            entries.add(e);
            totalCpuTime += e.cpuTimeMicros;
            totalRecordedCallsCount += e.recordedCallCount;
            totalCallsCount += e.callCount;
        }
        entries.sort(Comparator.comparingDouble(new ToDoubleFunction() { // from class: com.android.internal.os.-$$Lambda$BinderCallsStats$iPOmTqbqUiHzgsAugINuZgf9tls
            @Override // java.util.function.ToDoubleFunction
            public final double applyAsDouble(Object obj) {
                double d;
                BinderCallsStats.UidEntry uidEntry = (BinderCallsStats.UidEntry) obj;
                return d;
            }
        }).reversed());
        String str = "";
        String datasetSizeDesc = verbose ? "" : "(top 90% by cpu time) ";
        StringBuilder sb = new StringBuilder();
        pw.println("Per-UID raw data " + datasetSizeDesc + "(package/uid, worksource, call_desc, screen_interactive, cpu_time_micros, max_cpu_time_micros, latency_time_micros, max_latency_time_micros, exception_count, max_request_size_bytes, max_reply_size_bytes, recorded_call_count, call_count):");
        List<ExportedCallStat> exportedCallStats = getExportedCallStats();
        exportedCallStats.sort(new Comparator() { // from class: com.android.internal.os.-$$Lambda$BinderCallsStats$233x_Qux4c_AiqShYaWwvFplEXs
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int compareByCpuDesc;
                compareByCpuDesc = BinderCallsStats.compareByCpuDesc((BinderCallsStats.ExportedCallStat) obj, (BinderCallsStats.ExportedCallStat) obj2);
                return compareByCpuDesc;
            }
        });
        Iterator<ExportedCallStat> it = exportedCallStats.iterator();
        while (true) {
            int uidEntriesSize2 = uidEntriesSize;
            if (!it.hasNext()) {
                break;
            }
            ExportedCallStat e2 = it.next();
            List<ExportedCallStat> exportedCallStats2 = exportedCallStats;
            Iterator<ExportedCallStat> it2 = it;
            if (e2.methodName.startsWith("__DEBUG_")) {
                exportedCallStats = exportedCallStats2;
                uidEntriesSize = uidEntriesSize2;
                it = it2;
            } else {
                sb.setLength(0);
                sb.append("    ");
                sb.append(appIdToPackageMap.mapUid(e2.callingUid));
                sb.append(',');
                sb.append(appIdToPackageMap.mapUid(e2.workSourceUid));
                sb.append(',');
                sb.append(e2.className);
                sb.append('#');
                sb.append(e2.methodName);
                sb.append(',');
                sb.append(e2.screenInteractive);
                sb.append(',');
                sb.append(e2.cpuTimeMicros);
                sb.append(',');
                sb.append(e2.maxCpuTimeMicros);
                sb.append(',');
                sb.append(e2.latencyMicros);
                sb.append(',');
                sb.append(e2.maxLatencyMicros);
                sb.append(',');
                sb.append(this.mDetailedTracking ? e2.exceptionCount : 95L);
                sb.append(',');
                sb.append(this.mDetailedTracking ? e2.maxRequestSizeBytes : 95L);
                sb.append(',');
                sb.append(this.mDetailedTracking ? e2.maxReplySizeBytes : 95L);
                sb.append(',');
                sb.append(e2.recordedCallCount);
                sb.append(',');
                sb.append(e2.callCount);
                pw.println(sb);
                exportedCallStats = exportedCallStats2;
                uidEntriesSize = uidEntriesSize2;
                it = it2;
            }
        }
        pw.println();
        pw.println("Per-UID Summary " + datasetSizeDesc + "(cpu_time, % of total cpu_time, recorded_call_count, call_count, package/uid):");
        List<UidEntry> summaryEntries = verbose ? entries : getHighestValues(entries, new ToDoubleFunction() { // from class: com.android.internal.os.-$$Lambda$BinderCallsStats$xI0E0RpviGYsokEB7ojNx8LEbUc
            @Override // java.util.function.ToDoubleFunction
            public final double applyAsDouble(Object obj) {
                double d;
                BinderCallsStats.UidEntry uidEntry = (BinderCallsStats.UidEntry) obj;
                return d;
            }
        }, 0.9d);
        Iterator<UidEntry> it3 = summaryEntries.iterator();
        while (true) {
            List<UidEntry> entries2 = entries;
            if (!it3.hasNext()) {
                break;
            }
            UidEntry entry = it3.next();
            String uidStr = appIdToPackageMap.mapUid(entry.workSourceUid);
            pw.println(String.format("  %10d %3.0f%% %8d %8d %s", Long.valueOf(entry.cpuTimeMicros), Double.valueOf((entry.cpuTimeMicros * 100.0d) / totalCpuTime), Long.valueOf(entry.recordedCallCount), Long.valueOf(entry.callCount), uidStr));
            appIdToPackageMap = packageMap;
            entries = entries2;
            datasetSizeDesc = datasetSizeDesc;
            sb = sb;
            str = str;
            summaryEntries = summaryEntries;
        }
        String str2 = str;
        pw.println();
        pw.println(String.format("  Summary: total_cpu_time=%d, calls_count=%d, avg_call_cpu_time=%.0f", Long.valueOf(totalCpuTime), Long.valueOf(totalCallsCount), Double.valueOf(totalCpuTime / totalRecordedCallsCount)));
        pw.println();
        pw.println("Exceptions thrown (exception_count, class_name):");
        final List<Pair<String, Integer>> exceptionEntries = new ArrayList<>();
        this.mExceptionCounts.entrySet().iterator().forEachRemaining(new Consumer() { // from class: com.android.internal.os.-$$Lambda$BinderCallsStats$Vota0PqfoPWckjXH35wE48myGdk
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                exceptionEntries.add(Pair.create((String) r2.getKey(), (Integer) ((Map.Entry) obj).getValue()));
            }
        });
        exceptionEntries.sort(new Comparator() { // from class: com.android.internal.os.-$$Lambda$BinderCallsStats$-YP-7pwoNn8TN0iTmo5Q1r2lQz0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int compare;
                compare = Integer.compare(((Integer) ((Pair) obj2).second).intValue(), ((Integer) ((Pair) obj).second).intValue());
                return compare;
            }
        });
        for (Pair<String, Integer> entry2 : exceptionEntries) {
            pw.println(String.format("  %6d %s", entry2.second, entry2.first));
        }
        if (this.mPeriodicSamplingInterval != 1) {
            pw.println(str2);
            pw.println("/!\\ Displayed data is sampled. See sampling interval at the top.");
        }
    }

    protected long getThreadTimeMicro() {
        return SystemClock.currentThreadTimeMicro();
    }

    protected int getCallingUid() {
        return Binder.getCallingUid();
    }

    protected long getElapsedRealtimeMicro() {
        return SystemClock.elapsedRealtimeNanos() / 1000;
    }

    protected boolean shouldRecordDetailedData() {
        return this.mRandom.nextInt() % this.mPeriodicSamplingInterval == 0;
    }

    public void setDetailedTracking(boolean enabled) {
        synchronized (this.mLock) {
            if (enabled != this.mDetailedTracking) {
                this.mDetailedTracking = enabled;
                reset();
            }
        }
    }

    public void setTrackScreenInteractive(boolean enabled) {
        synchronized (this.mLock) {
            if (enabled != this.mTrackScreenInteractive) {
                this.mTrackScreenInteractive = enabled;
                reset();
            }
        }
    }

    public void setTrackDirectCallerUid(boolean enabled) {
        synchronized (this.mLock) {
            if (enabled != this.mTrackDirectCallingUid) {
                this.mTrackDirectCallingUid = enabled;
                reset();
            }
        }
    }

    public void setAddDebugEntries(boolean addDebugEntries) {
        this.mAddDebugEntries = addDebugEntries;
    }

    public void setMaxBinderCallStats(int maxKeys) {
        if (maxKeys <= 0) {
            Slog.w(TAG, "Ignored invalid max value (value must be positive): " + maxKeys);
            return;
        }
        synchronized (this.mLock) {
            if (maxKeys != this.mMaxBinderCallStatsCount) {
                this.mMaxBinderCallStatsCount = maxKeys;
                reset();
            }
        }
    }

    public void setSamplingInterval(int samplingInterval) {
        if (samplingInterval <= 0) {
            Slog.w(TAG, "Ignored invalid sampling interval (value must be positive): " + samplingInterval);
            return;
        }
        synchronized (this.mLock) {
            if (samplingInterval != this.mPeriodicSamplingInterval) {
                this.mPeriodicSamplingInterval = samplingInterval;
                reset();
            }
        }
    }

    public void reset() {
        synchronized (this.mLock) {
            this.mCallStatsCount = 0L;
            this.mUidEntries.clear();
            this.mExceptionCounts.clear();
            this.mStartCurrentTime = System.currentTimeMillis();
            this.mStartElapsedTime = SystemClock.elapsedRealtime();
            if (this.mBatteryStopwatch != null) {
                this.mBatteryStopwatch.reset();
            }
        }
    }

    @VisibleForTesting
    /* loaded from: classes3.dex */
    public static class CallStat {
        public final Class<? extends Binder> binderClass;
        public long callCount;
        public final int callingUid;
        public long cpuTimeMicros;
        public long exceptionCount;
        public long latencyMicros;
        public long maxCpuTimeMicros;
        public long maxLatencyMicros;
        public long maxReplySizeBytes;
        public long maxRequestSizeBytes;
        public long recordedCallCount;
        public final boolean screenInteractive;
        public final int transactionCode;

        CallStat(int callingUid, Class<? extends Binder> binderClass, int transactionCode, boolean screenInteractive) {
            this.callingUid = callingUid;
            this.binderClass = binderClass;
            this.transactionCode = transactionCode;
            this.screenInteractive = screenInteractive;
        }
    }

    /* loaded from: classes3.dex */
    public static class CallStatKey {
        public Class<? extends Binder> binderClass;
        public int callingUid;
        private boolean screenInteractive;
        public int transactionCode;

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            CallStatKey key = (CallStatKey) o;
            return this.callingUid == key.callingUid && this.transactionCode == key.transactionCode && this.screenInteractive == key.screenInteractive && this.binderClass.equals(key.binderClass);
        }

        public int hashCode() {
            int result = this.binderClass.hashCode();
            return (((((result * 31) + this.transactionCode) * 31) + this.callingUid) * 31) + (this.screenInteractive ? MetricsProto.MetricsEvent.AUTOFILL_SERVICE_DISABLED_APP : MetricsProto.MetricsEvent.ANOMALY_TYPE_UNOPTIMIZED_BT);
        }
    }

    @VisibleForTesting
    /* loaded from: classes3.dex */
    public static class UidEntry {
        public long callCount;
        public long cpuTimeMicros;
        private Map<CallStatKey, CallStat> mCallStats = new ArrayMap();
        private CallStatKey mTempKey = new CallStatKey();
        public long recordedCallCount;
        public int workSourceUid;

        UidEntry(int uid) {
            this.workSourceUid = uid;
        }

        CallStat get(int callingUid, Class<? extends Binder> binderClass, int transactionCode, boolean screenInteractive) {
            CallStatKey callStatKey = this.mTempKey;
            callStatKey.callingUid = callingUid;
            callStatKey.binderClass = binderClass;
            callStatKey.transactionCode = transactionCode;
            callStatKey.screenInteractive = screenInteractive;
            return this.mCallStats.get(this.mTempKey);
        }

        CallStat getOrCreate(int callingUid, Class<? extends Binder> binderClass, int transactionCode, boolean screenInteractive, boolean maxCallStatsReached) {
            CallStat mapCallStat = get(callingUid, binderClass, transactionCode, screenInteractive);
            if (mapCallStat == null) {
                if (maxCallStatsReached) {
                    CallStat mapCallStat2 = get(-1, BinderCallsStats.OVERFLOW_BINDER, -1, false);
                    if (mapCallStat2 != null) {
                        return mapCallStat2;
                    }
                    callingUid = -1;
                    binderClass = BinderCallsStats.OVERFLOW_BINDER;
                    transactionCode = -1;
                    screenInteractive = false;
                }
                CallStat mapCallStat3 = new CallStat(callingUid, binderClass, transactionCode, screenInteractive);
                CallStatKey key = new CallStatKey();
                key.callingUid = callingUid;
                key.binderClass = binderClass;
                key.transactionCode = transactionCode;
                key.screenInteractive = screenInteractive;
                this.mCallStats.put(key, mapCallStat3);
                return mapCallStat3;
            }
            return mapCallStat;
        }

        public Collection<CallStat> getCallStatsList() {
            return this.mCallStats.values();
        }

        public String toString() {
            return "UidEntry{cpuTimeMicros=" + this.cpuTimeMicros + ", callCount=" + this.callCount + ", mCallStats=" + this.mCallStats + '}';
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            UidEntry uidEntry = (UidEntry) o;
            return this.workSourceUid == uidEntry.workSourceUid;
        }

        public int hashCode() {
            return this.workSourceUid;
        }
    }

    @VisibleForTesting
    public SparseArray<UidEntry> getUidEntries() {
        return this.mUidEntries;
    }

    @VisibleForTesting
    public ArrayMap<String, Integer> getExceptionCounts() {
        return this.mExceptionCounts;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @VisibleForTesting
    public static <T> List<T> getHighestValues(List<T> list, ToDoubleFunction<T> toDouble, double percentile) {
        List<T> sortedList = new ArrayList<>(list);
        sortedList.sort(Comparator.comparingDouble(toDouble).reversed());
        double total = FeatureOption.FO_BOOT_POLICY_CPU;
        for (T item : list) {
            total += toDouble.applyAsDouble(item);
        }
        List<T> result = new ArrayList<>();
        double runningSum = FeatureOption.FO_BOOT_POLICY_CPU;
        for (T item2 : sortedList) {
            if (runningSum > percentile * total) {
                break;
            }
            result.add(item2);
            runningSum += toDouble.applyAsDouble(item2);
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int compareByCpuDesc(ExportedCallStat a, ExportedCallStat b) {
        return Long.compare(b.cpuTimeMicros, a.cpuTimeMicros);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int compareByBinderClassAndCode(ExportedCallStat a, ExportedCallStat b) {
        int result = a.className.compareTo(b.className);
        if (result != 0) {
            return result;
        }
        return Integer.compare(a.transactionCode, b.transactionCode);
    }
}
