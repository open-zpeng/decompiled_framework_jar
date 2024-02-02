package com.android.internal.os;

import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
/* loaded from: classes3.dex */
public class KernelCpuProcReader {
    private static final long DEFAULT_THROTTLE_INTERVAL = 3000;
    private static final int ERROR_THRESHOLD = 5;
    private static final int MAX_BUFFER_SIZE = 1048576;
    private static final String TAG = "KernelCpuProcReader";
    private int mContentSize;
    private int mErrors;
    private final Path mProc;
    private static final String PROC_UID_FREQ_TIME = "/proc/uid_cpupower/time_in_state";
    private static final KernelCpuProcReader mFreqTimeReader = new KernelCpuProcReader(PROC_UID_FREQ_TIME);
    private static final String PROC_UID_ACTIVE_TIME = "/proc/uid_cpupower/concurrent_active_time";
    private static final KernelCpuProcReader mActiveTimeReader = new KernelCpuProcReader(PROC_UID_ACTIVE_TIME);
    private static final String PROC_UID_CLUSTER_TIME = "/proc/uid_cpupower/concurrent_policy_time";
    private static final KernelCpuProcReader mClusterTimeReader = new KernelCpuProcReader(PROC_UID_CLUSTER_TIME);
    private long mThrottleInterval = 3000;
    private long mLastReadTime = Long.MIN_VALUE;
    private byte[] mBuffer = new byte[8192];

    public static synchronized KernelCpuProcReader getFreqTimeReaderInstance() {
        return mFreqTimeReader;
    }

    public static synchronized KernelCpuProcReader getActiveTimeReaderInstance() {
        return mActiveTimeReader;
    }

    public static synchronized KernelCpuProcReader getClusterTimeReaderInstance() {
        return mClusterTimeReader;
    }

    @VisibleForTesting
    public synchronized KernelCpuProcReader(String procFile) {
        this.mProc = Paths.get(procFile, new String[0]);
    }

    public synchronized ByteBuffer readBytes() {
        if (this.mErrors >= 5) {
            return null;
        }
        if (SystemClock.elapsedRealtime() < this.mLastReadTime + this.mThrottleInterval) {
            if (this.mContentSize > 0) {
                return ByteBuffer.wrap(this.mBuffer, 0, this.mContentSize).asReadOnlyBuffer().order(ByteOrder.nativeOrder());
            }
            return null;
        }
        this.mLastReadTime = SystemClock.elapsedRealtime();
        this.mContentSize = 0;
        int oldMask = StrictMode.allowThreadDiskReadsMask();
        try {
            try {
                try {
                    InputStream in = Files.newInputStream(this.mProc, new OpenOption[0]);
                    int numBytes = 0;
                    while (true) {
                        try {
                            int curr = in.read(this.mBuffer, numBytes, this.mBuffer.length - numBytes);
                            if (curr < 0) {
                                this.mContentSize = numBytes;
                                ByteBuffer order = ByteBuffer.wrap(this.mBuffer, 0, this.mContentSize).asReadOnlyBuffer().order(ByteOrder.nativeOrder());
                                if (in != null) {
                                    in.close();
                                }
                                return order;
                            }
                            numBytes += curr;
                            if (numBytes == this.mBuffer.length) {
                                if (this.mBuffer.length == 1048576) {
                                    this.mErrors++;
                                    Slog.e(TAG, "Proc file is too large: " + this.mProc);
                                    if (in != null) {
                                        in.close();
                                    }
                                    return null;
                                }
                                this.mBuffer = Arrays.copyOf(this.mBuffer, Math.min(this.mBuffer.length << 1, 1048576));
                            }
                        } finally {
                        }
                    }
                } catch (IOException e) {
                    this.mErrors++;
                    Slog.e(TAG, "Error reading: " + this.mProc, e);
                    return null;
                }
            } catch (FileNotFoundException | NoSuchFileException e2) {
                this.mErrors++;
                Slog.w(TAG, "File not exist: " + this.mProc);
                return null;
            }
        } finally {
            StrictMode.setThreadPolicyMask(oldMask);
        }
    }

    public synchronized void setThrottleInterval(long throttleInterval) {
        if (throttleInterval >= 0) {
            this.mThrottleInterval = throttleInterval;
        }
    }
}
