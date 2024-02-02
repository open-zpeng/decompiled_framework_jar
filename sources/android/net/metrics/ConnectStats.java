package android.net.metrics;

import android.net.NetworkCapabilities;
import android.provider.SettingsStringUtil;
import android.system.OsConstants;
import android.util.IntArray;
import android.util.SparseIntArray;
import com.android.internal.util.BitUtils;
import com.android.internal.util.TokenBucket;
/* loaded from: classes2.dex */
public class ConnectStats {
    private static final int EALREADY = OsConstants.EALREADY;
    private static final int EINPROGRESS = OsConstants.EINPROGRESS;
    public final TokenBucket mLatencyTb;
    public final int mMaxLatencyRecords;
    public final int netId;
    public final long transports;
    public final SparseIntArray errnos = new SparseIntArray();
    public final IntArray latencies = new IntArray();
    public int eventCount = 0;
    public int connectCount = 0;
    public int connectBlockingCount = 0;
    public int ipv6ConnectCount = 0;

    public synchronized ConnectStats(int netId, long transports, TokenBucket tb, int maxLatencyRecords) {
        this.netId = netId;
        this.transports = transports;
        this.mLatencyTb = tb;
        this.mMaxLatencyRecords = maxLatencyRecords;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean addEvent(int errno, int latencyMs, String ipAddr) {
        this.eventCount++;
        if (isSuccess(errno)) {
            countConnect(errno, ipAddr);
            countLatency(errno, latencyMs);
            return true;
        }
        countError(errno);
        return false;
    }

    private synchronized void countConnect(int errno, String ipAddr) {
        this.connectCount++;
        if (!isNonBlocking(errno)) {
            this.connectBlockingCount++;
        }
        if (isIPv6(ipAddr)) {
            this.ipv6ConnectCount++;
        }
    }

    private synchronized void countLatency(int errno, int ms) {
        if (isNonBlocking(errno) || !this.mLatencyTb.get() || this.latencies.size() >= this.mMaxLatencyRecords) {
            return;
        }
        this.latencies.add(ms);
    }

    private synchronized void countError(int errno) {
        int newcount = this.errnos.get(errno, 0) + 1;
        this.errnos.put(errno, newcount);
    }

    private static synchronized boolean isSuccess(int errno) {
        return errno == 0 || isNonBlocking(errno);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized boolean isNonBlocking(int errno) {
        return errno == EINPROGRESS || errno == EALREADY;
    }

    private static synchronized boolean isIPv6(String ipAddr) {
        return ipAddr.contains(SettingsStringUtil.DELIMITER);
    }

    public String toString() {
        int[] unpackBits;
        StringBuilder sb = new StringBuilder("ConnectStats(");
        sb.append("netId=");
        sb.append(this.netId);
        StringBuilder builder = sb.append(", ");
        for (int t : BitUtils.unpackBits(this.transports)) {
            builder.append(NetworkCapabilities.transportNameOf(t));
            builder.append(", ");
        }
        builder.append(String.format("%d events, ", Integer.valueOf(this.eventCount)));
        builder.append(String.format("%d success, ", Integer.valueOf(this.connectCount)));
        builder.append(String.format("%d blocking, ", Integer.valueOf(this.connectBlockingCount)));
        builder.append(String.format("%d IPv6 dst", Integer.valueOf(this.ipv6ConnectCount)));
        for (int i = 0; i < this.errnos.size(); i++) {
            String errno = OsConstants.errnoName(this.errnos.keyAt(i));
            int count = this.errnos.valueAt(i);
            builder.append(String.format(", %s: %d", errno, Integer.valueOf(count)));
        }
        builder.append(")");
        return builder.toString();
    }
}
