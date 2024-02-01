package com.android.internal.net;

import android.net.NetworkStats;
import android.os.StrictMode;
import android.os.SystemClock;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.ProcFileReader;
import com.android.server.NetworkManagementSocketTagger;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import libcore.io.IoUtils;
/* loaded from: classes3.dex */
public class NetworkStatsFactory {
    private static final boolean SANITY_CHECK_NATIVE = false;
    private static final String TAG = "NetworkStatsFactory";
    private static final boolean USE_NATIVE_PARSING = true;
    private static final ConcurrentHashMap<String, String> sStackedIfaces = new ConcurrentHashMap<>();
    private final File mStatsXtIfaceAll;
    private final File mStatsXtIfaceFmt;
    private final File mStatsXtUid;
    private boolean mUseBpfStats;

    @VisibleForTesting
    public static native int nativeReadNetworkStatsDetail(NetworkStats networkStats, String str, int i, String[] strArr, int i2, boolean z);

    @VisibleForTesting
    public static native int nativeReadNetworkStatsDev(NetworkStats networkStats);

    public static synchronized void noteStackedIface(String stackedIface, String baseIface) {
        if (stackedIface != null && baseIface != null) {
            sStackedIfaces.put(stackedIface, baseIface);
        }
    }

    public static synchronized String[] augmentWithStackedInterfaces(String[] requiredIfaces) {
        if (requiredIfaces == NetworkStats.INTERFACES_ALL) {
            return null;
        }
        HashSet<String> relatedIfaces = new HashSet<>(Arrays.asList(requiredIfaces));
        for (Map.Entry<String, String> entry : sStackedIfaces.entrySet()) {
            if (relatedIfaces.contains(entry.getKey())) {
                relatedIfaces.add(entry.getValue());
            } else if (relatedIfaces.contains(entry.getValue())) {
                relatedIfaces.add(entry.getKey());
            }
        }
        String[] outArray = new String[relatedIfaces.size()];
        return (String[]) relatedIfaces.toArray(outArray);
    }

    public static void apply464xlatAdjustments(NetworkStats baseTraffic, NetworkStats stackedTraffic, boolean useBpfStats) {
        NetworkStats.apply464xlatAdjustments(baseTraffic, stackedTraffic, sStackedIfaces, useBpfStats);
    }

    @VisibleForTesting
    public static synchronized void clearStackedIfaces() {
        sStackedIfaces.clear();
    }

    public synchronized NetworkStatsFactory() {
        this(new File("/proc/"), new File("/sys/fs/bpf/traffic_uid_stats_map").exists());
    }

    @VisibleForTesting
    public synchronized NetworkStatsFactory(File procRoot, boolean useBpfStats) {
        this.mStatsXtIfaceAll = new File(procRoot, "net/xt_qtaguid/iface_stat_all");
        this.mStatsXtIfaceFmt = new File(procRoot, "net/xt_qtaguid/iface_stat_fmt");
        this.mStatsXtUid = new File(procRoot, "net/xt_qtaguid/stats");
        this.mUseBpfStats = useBpfStats;
    }

    public synchronized NetworkStats readBpfNetworkStatsDev() throws IOException {
        NetworkStats stats = new NetworkStats(SystemClock.elapsedRealtime(), 6);
        if (nativeReadNetworkStatsDev(stats) != 0) {
            throw new IOException("Failed to parse bpf iface stats");
        }
        return stats;
    }

    public synchronized NetworkStats readNetworkStatsSummaryDev() throws IOException {
        if (this.mUseBpfStats) {
            return readBpfNetworkStatsDev();
        }
        StrictMode.ThreadPolicy savedPolicy = StrictMode.allowThreadDiskReads();
        NetworkStats stats = new NetworkStats(SystemClock.elapsedRealtime(), 6);
        NetworkStats.Entry entry = new NetworkStats.Entry();
        ProcFileReader reader = null;
        try {
            try {
                reader = new ProcFileReader(new FileInputStream(this.mStatsXtIfaceAll));
                while (reader.hasMoreData()) {
                    entry.iface = reader.nextString();
                    entry.uid = -1;
                    entry.set = -1;
                    entry.tag = 0;
                    boolean active = reader.nextInt() != 0;
                    entry.rxBytes = reader.nextLong();
                    entry.rxPackets = reader.nextLong();
                    entry.txBytes = reader.nextLong();
                    entry.txPackets = reader.nextLong();
                    if (active) {
                        entry.rxBytes += reader.nextLong();
                        entry.rxPackets += reader.nextLong();
                        entry.txBytes += reader.nextLong();
                        entry.txPackets += reader.nextLong();
                    }
                    stats.addValues(entry);
                    reader.finishLine();
                }
                return stats;
            } catch (NullPointerException | NumberFormatException e) {
                throw new ProtocolException("problem parsing stats", e);
            }
        } finally {
            IoUtils.closeQuietly(reader);
            StrictMode.setThreadPolicy(savedPolicy);
        }
    }

    public synchronized NetworkStats readNetworkStatsSummaryXt() throws IOException {
        if (this.mUseBpfStats) {
            return readBpfNetworkStatsDev();
        }
        StrictMode.ThreadPolicy savedPolicy = StrictMode.allowThreadDiskReads();
        ProcFileReader reader = null;
        if (this.mStatsXtIfaceFmt.exists()) {
            NetworkStats stats = new NetworkStats(SystemClock.elapsedRealtime(), 6);
            NetworkStats.Entry entry = new NetworkStats.Entry();
            try {
                try {
                    reader = new ProcFileReader(new FileInputStream(this.mStatsXtIfaceFmt));
                    reader.finishLine();
                    while (reader.hasMoreData()) {
                        entry.iface = reader.nextString();
                        entry.uid = -1;
                        entry.set = -1;
                        entry.tag = 0;
                        entry.rxBytes = reader.nextLong();
                        entry.rxPackets = reader.nextLong();
                        entry.txBytes = reader.nextLong();
                        entry.txPackets = reader.nextLong();
                        stats.addValues(entry);
                        reader.finishLine();
                    }
                    return stats;
                } catch (NullPointerException | NumberFormatException e) {
                    throw new ProtocolException("problem parsing stats", e);
                }
            } finally {
                IoUtils.closeQuietly(reader);
                StrictMode.setThreadPolicy(savedPolicy);
            }
        }
        return null;
    }

    public synchronized NetworkStats readNetworkStatsDetail() throws IOException {
        return readNetworkStatsDetail(-1, null, -1, null);
    }

    public synchronized NetworkStats readNetworkStatsDetail(int limitUid, String[] limitIfaces, int limitTag, NetworkStats lastStats) throws IOException {
        NetworkStats stats = readNetworkStatsDetailInternal(limitUid, limitIfaces, limitTag, lastStats);
        stats.apply464xlatAdjustments(sStackedIfaces, this.mUseBpfStats);
        return stats;
    }

    private synchronized NetworkStats readNetworkStatsDetailInternal(int limitUid, String[] limitIfaces, int limitTag, NetworkStats lastStats) throws IOException {
        NetworkStats stats;
        if (lastStats != null) {
            stats = lastStats;
            stats.setElapsedRealtime(SystemClock.elapsedRealtime());
        } else {
            stats = new NetworkStats(SystemClock.elapsedRealtime(), -1);
        }
        if (nativeReadNetworkStatsDetail(stats, this.mStatsXtUid.getAbsolutePath(), limitUid, limitIfaces, limitTag, this.mUseBpfStats) != 0) {
            throw new IOException("Failed to parse network stats");
        }
        return stats;
    }

    @VisibleForTesting
    public static synchronized NetworkStats javaReadNetworkStatsDetail(File detailPath, int limitUid, String[] limitIfaces, int limitTag) throws IOException {
        StrictMode.ThreadPolicy savedPolicy = StrictMode.allowThreadDiskReads();
        NetworkStats stats = new NetworkStats(SystemClock.elapsedRealtime(), 24);
        NetworkStats.Entry entry = new NetworkStats.Entry();
        int idx = 1;
        int lastIdx = 1;
        ProcFileReader reader = null;
        try {
            try {
                reader = new ProcFileReader(new FileInputStream(detailPath));
                reader.finishLine();
                while (reader.hasMoreData()) {
                    idx = reader.nextInt();
                    if (idx != lastIdx + 1) {
                        throw new ProtocolException("inconsistent idx=" + idx + " after lastIdx=" + lastIdx);
                    }
                    lastIdx = idx;
                    entry.iface = reader.nextString();
                    entry.tag = NetworkManagementSocketTagger.kernelToTag(reader.nextString());
                    entry.uid = reader.nextInt();
                    entry.set = reader.nextInt();
                    entry.rxBytes = reader.nextLong();
                    entry.rxPackets = reader.nextLong();
                    entry.txBytes = reader.nextLong();
                    entry.txPackets = reader.nextLong();
                    if ((limitIfaces == null || ArrayUtils.contains(limitIfaces, entry.iface)) && ((limitUid == -1 || limitUid == entry.uid) && (limitTag == -1 || limitTag == entry.tag))) {
                        stats.addValues(entry);
                    }
                    reader.finishLine();
                }
                return stats;
            } catch (NullPointerException | NumberFormatException e) {
                throw new ProtocolException("problem parsing idx " + idx, e);
            }
        } finally {
            IoUtils.closeQuietly(reader);
            StrictMode.setThreadPolicy(savedPolicy);
        }
    }

    public synchronized void assertEquals(NetworkStats expected, NetworkStats actual) {
        if (expected.size() != actual.size()) {
            throw new AssertionError("Expected size " + expected.size() + ", actual size " + actual.size());
        }
        NetworkStats.Entry expectedRow = null;
        NetworkStats.Entry actualRow = null;
        for (int i = 0; i < expected.size(); i++) {
            expectedRow = expected.getValues(i, expectedRow);
            actualRow = actual.getValues(i, actualRow);
            if (!expectedRow.equals(actualRow)) {
                throw new AssertionError("Expected row " + i + ": " + expectedRow + ", actual row " + actualRow);
            }
        }
    }
}
