package com.android.internal.os;

import android.os.Binder;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.ArrayMap;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.BinderCallsStats;
import com.android.internal.util.Preconditions;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
/* loaded from: classes3.dex */
public class BinderCallsStats {
    private static final int CALL_SESSIONS_POOL_SIZE = 100;
    private static final BinderCallsStats sInstance = new BinderCallsStats();
    private final Queue<CallSession> mCallSessionsPool;
    private volatile boolean mDetailedTracking;
    private final Object mLock;
    private long mStartTime;
    @GuardedBy("mLock")
    private final SparseArray<UidEntry> mUidEntries;

    /* loaded from: classes3.dex */
    public static class CallSession {
        CallStat mCallStat = new CallStat();
        int mCallingUId;
        long mStarted;
    }

    private BinderCallsStats() {
        this.mDetailedTracking = false;
        this.mUidEntries = new SparseArray<>();
        this.mCallSessionsPool = new ConcurrentLinkedQueue();
        this.mLock = new Object();
        this.mStartTime = System.currentTimeMillis();
    }

    @VisibleForTesting
    public BinderCallsStats(boolean detailedTracking) {
        this.mDetailedTracking = false;
        this.mUidEntries = new SparseArray<>();
        this.mCallSessionsPool = new ConcurrentLinkedQueue();
        this.mLock = new Object();
        this.mStartTime = System.currentTimeMillis();
        this.mDetailedTracking = detailedTracking;
    }

    public CallSession callStarted(Binder binder, int code) {
        return callStarted(binder.getClass().getName(), code);
    }

    private CallSession callStarted(String className, int code) {
        CallSession s = this.mCallSessionsPool.poll();
        if (s == null) {
            s = new CallSession();
        }
        s.mCallStat.className = className;
        s.mCallStat.msg = code;
        s.mStarted = getThreadTimeMicro();
        return s;
    }

    public void callEnded(CallSession s) {
        Preconditions.checkNotNull(s);
        long duration = this.mDetailedTracking ? getThreadTimeMicro() - s.mStarted : 1L;
        s.mCallingUId = Binder.getCallingUid();
        synchronized (this.mLock) {
            UidEntry uidEntry = this.mUidEntries.get(s.mCallingUId);
            if (uidEntry == null) {
                uidEntry = new UidEntry(s.mCallingUId);
                this.mUidEntries.put(s.mCallingUId, uidEntry);
            }
            if (this.mDetailedTracking) {
                CallStat callStat = uidEntry.mCallStats.get(s.mCallStat);
                if (callStat == null) {
                    callStat = new CallStat(s.mCallStat.className, s.mCallStat.msg);
                    uidEntry.mCallStats.put(callStat, callStat);
                }
                callStat.callCount++;
                callStat.time += duration;
            }
            uidEntry.time += duration;
            uidEntry.callCount++;
        }
        if (this.mCallSessionsPool.size() < 100) {
            this.mCallSessionsPool.add(s);
        }
    }

    public void dump(PrintWriter pw) {
        long totalCallsTime;
        long j;
        long j2;
        BinderCallsStats binderCallsStats = this;
        Map<Integer, Long> uidTimeMap = new HashMap<>();
        Map<Integer, Long> uidCallCountMap = new HashMap<>();
        long totalCallsTime2 = 0;
        pw.print("Start time: ");
        pw.println(DateFormat.format("yyyy-MM-dd HH:mm:ss", binderCallsStats.mStartTime));
        int uidEntriesSize = binderCallsStats.mUidEntries.size();
        List<UidEntry> entries = new ArrayList<>();
        synchronized (binderCallsStats.mLock) {
            long totalCallsCount = 0;
            int i = 0;
            while (i < uidEntriesSize) {
                try {
                    UidEntry e = binderCallsStats.mUidEntries.valueAt(i);
                    entries.add(e);
                    long totalCallsTime3 = totalCallsTime2 + e.time;
                    try {
                        Long totalTimePerUid = (Long) uidTimeMap.get(Integer.valueOf(e.uid));
                        Integer valueOf = Integer.valueOf(e.uid);
                        if (totalTimePerUid == null) {
                            j = e.time;
                            totalCallsTime = totalCallsTime3;
                        } else {
                            long longValue = totalTimePerUid.longValue();
                            totalCallsTime = totalCallsTime3;
                            long totalCallsTime4 = e.time;
                            j = longValue + totalCallsTime4;
                        }
                        uidTimeMap.put(valueOf, Long.valueOf(j));
                        Long totalCallsPerUid = (Long) uidCallCountMap.get(Integer.valueOf(e.uid));
                        Integer valueOf2 = Integer.valueOf(e.uid);
                        if (totalCallsPerUid != null) {
                            j2 = totalCallsPerUid.longValue() + e.callCount;
                        } else {
                            try {
                                j2 = e.callCount;
                            } catch (Throwable th) {
                                th = th;
                                while (true) {
                                    try {
                                        break;
                                    } catch (Throwable th2) {
                                        th = th2;
                                    }
                                }
                                throw th;
                            }
                        }
                        uidCallCountMap.put(valueOf2, Long.valueOf(j2));
                        totalCallsCount += e.callCount;
                        i++;
                        totalCallsTime2 = totalCallsTime;
                        binderCallsStats = this;
                    } catch (Throwable th3) {
                        th = th3;
                    }
                } catch (Throwable th4) {
                    th = th4;
                }
            }
            try {
                if (this.mDetailedTracking) {
                    pw.println("Raw data (uid,call_desc,time):");
                    entries.sort(new Comparator() { // from class: com.android.internal.os.-$$Lambda$BinderCallsStats$JdIS98lVGLAIfkEkC976rVyBc_U
                        @Override // java.util.Comparator
                        public final int compare(Object obj, Object obj2) {
                            return BinderCallsStats.lambda$dump$0((BinderCallsStats.UidEntry) obj, (BinderCallsStats.UidEntry) obj2);
                        }
                    });
                    StringBuilder sb = new StringBuilder();
                    for (UidEntry uidEntry : entries) {
                        List<CallStat> callStats = new ArrayList<>(uidEntry.mCallStats.keySet());
                        callStats.sort(new Comparator() { // from class: com.android.internal.os.-$$Lambda$BinderCallsStats$8JB19VSNkNr7RqU7ZTJ6NGkFXVU
                            @Override // java.util.Comparator
                            public final int compare(Object obj, Object obj2) {
                                return BinderCallsStats.lambda$dump$1((BinderCallsStats.CallStat) obj, (BinderCallsStats.CallStat) obj2);
                            }
                        });
                        for (Iterator<CallStat> it = callStats.iterator(); it.hasNext(); it = it) {
                            CallStat e2 = it.next();
                            sb.setLength(0);
                            sb.append("    ");
                            sb.append(uidEntry.uid);
                            sb.append(",");
                            sb.append(e2);
                            sb.append(',');
                            sb.append(e2.time);
                            pw.println(sb);
                            callStats = callStats;
                        }
                    }
                    pw.println();
                    pw.println("Per UID Summary(UID: time, % of total_time, calls_count):");
                    List<Map.Entry<Integer, Long>> uidTotals = new ArrayList<>(uidTimeMap.entrySet());
                    uidTotals.sort(new Comparator() { // from class: com.android.internal.os.-$$Lambda$BinderCallsStats$BeSOWJ8AoyB7S9CtX-6IPAXHyNQ
                        @Override // java.util.Comparator
                        public final int compare(Object obj, Object obj2) {
                            int compareTo;
                            compareTo = ((Long) ((Map.Entry) obj2).getValue()).compareTo((Long) ((Map.Entry) obj).getValue());
                            return compareTo;
                        }
                    });
                    for (Iterator<Map.Entry<Integer, Long>> it2 = uidTotals.iterator(); it2.hasNext(); it2 = it2) {
                        Map.Entry<Integer, Long> uidTotal = it2.next();
                        Long callCount = (Long) uidCallCountMap.get(uidTotal.getKey());
                        pw.println(String.format("  %7d: %11d %3.0f%% %8d", uidTotal.getKey(), uidTotal.getValue(), Double.valueOf((uidTotal.getValue().longValue() * 100.0d) / totalCallsTime2), callCount));
                        sb = sb;
                        uidTotals = uidTotals;
                    }
                    pw.println();
                    pw.println(String.format("  Summary: total_time=%d, calls_count=%d, avg_call_time=%.0f", Long.valueOf(totalCallsTime2), Long.valueOf(totalCallsCount), Double.valueOf(totalCallsTime2 / totalCallsCount)));
                    return;
                }
                pw.println("Per UID Summary(UID: calls_count, % of total calls_count):");
                List<Map.Entry<Integer, Long>> uidTotals2 = new ArrayList<>(uidTimeMap.entrySet());
                uidTotals2.sort(new Comparator() { // from class: com.android.internal.os.-$$Lambda$BinderCallsStats$jhdszMKzG9FSuIQ4Vz9B0exXKPk
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int compareTo;
                        compareTo = ((Long) ((Map.Entry) obj2).getValue()).compareTo((Long) ((Map.Entry) obj).getValue());
                        return compareTo;
                    }
                });
                Iterator<Map.Entry<Integer, Long>> it3 = uidTotals2.iterator();
                while (it3.hasNext()) {
                    Map.Entry<Integer, Long> uidTotal2 = it3.next();
                    Long callCount2 = uidCallCountMap.get(uidTotal2.getKey());
                    pw.println(String.format("    %7d: %8d %3.0f%%", uidTotal2.getKey(), callCount2, Double.valueOf((uidTotal2.getValue().longValue() * 100.0d) / totalCallsTime2)));
                    uidTotals2 = uidTotals2;
                    it3 = it3;
                    uidTimeMap = uidTimeMap;
                    uidCallCountMap = uidCallCountMap;
                }
            } catch (Throwable th5) {
                th = th5;
                while (true) {
                    break;
                    break;
                }
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$dump$0(UidEntry o1, UidEntry o2) {
        if (o1.time < o2.time) {
            return 1;
        }
        if (o1.time > o2.time) {
            return -1;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$dump$1(CallStat o1, CallStat o2) {
        if (o1.time < o2.time) {
            return 1;
        }
        if (o1.time > o2.time) {
            return -1;
        }
        return 0;
    }

    private long getThreadTimeMicro() {
        if (this.mDetailedTracking) {
            return SystemClock.currentThreadTimeMicro();
        }
        return 0L;
    }

    public static BinderCallsStats getInstance() {
        return sInstance;
    }

    public void setDetailedTracking(boolean enabled) {
        if (enabled != this.mDetailedTracking) {
            reset();
            this.mDetailedTracking = enabled;
        }
    }

    public void reset() {
        synchronized (this.mLock) {
            this.mUidEntries.clear();
            this.mStartTime = System.currentTimeMillis();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class CallStat {
        long callCount;
        String className;
        int msg;
        long time;

        CallStat() {
        }

        CallStat(String className, int msg) {
            this.className = className;
            this.msg = msg;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            CallStat callStat = (CallStat) o;
            return this.msg == callStat.msg && this.className.equals(callStat.className);
        }

        public int hashCode() {
            int result = this.className.hashCode();
            return (31 * result) + this.msg;
        }

        public String toString() {
            return this.className + "/" + this.msg;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class UidEntry {
        long callCount;
        Map<CallStat, CallStat> mCallStats = new ArrayMap();
        long time;
        int uid;

        UidEntry(int uid) {
            this.uid = uid;
        }

        public String toString() {
            return "UidEntry{time=" + this.time + ", callCount=" + this.callCount + ", mCallStats=" + this.mCallStats + '}';
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            UidEntry uidEntry = (UidEntry) o;
            return this.uid == uidEntry.uid;
        }

        public int hashCode() {
            return this.uid;
        }
    }
}
