package com.android.internal.os;

import android.mtp.MtpConstants;
import android.os.Process;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.KernelWakelockStats;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
/* loaded from: classes3.dex */
public class KernelWakelockReader {
    private static final String TAG = "KernelWakelockReader";
    private static final String sWakelockFile = "/proc/wakelocks";
    private static final String sWakeupSourceFile = "/d/wakeup_sources";
    private static int sKernelWakelockUpdateVersion = 0;
    private static final int[] PROC_WAKELOCKS_FORMAT = {5129, MtpConstants.RESPONSE_INVALID_OBJECT_HANDLE, 9, 9, 9, MtpConstants.RESPONSE_INVALID_OBJECT_HANDLE};
    private static final int[] WAKEUP_SOURCES_FORMAT = {4105, 8457, 265, 265, 265, 265, 8457};
    private final String[] mProcWakelocksName = new String[3];
    private final long[] mProcWakelocksData = new long[3];

    public final synchronized KernelWakelockStats readKernelWakelockStats(KernelWakelockStats staleStats) {
        FileInputStream is;
        boolean wakeup_sources;
        byte[] buffer = new byte[32768];
        long startTime = SystemClock.uptimeMillis();
        int oldMask = StrictMode.allowThreadDiskReadsMask();
        try {
            try {
                is = new FileInputStream(sWakelockFile);
                wakeup_sources = false;
            } catch (FileNotFoundException e) {
                try {
                    FileInputStream is2 = new FileInputStream(sWakeupSourceFile);
                    is = is2;
                    wakeup_sources = true;
                } catch (FileNotFoundException e2) {
                    Slog.wtf(TAG, "neither /proc/wakelocks nor /d/wakeup_sources exists");
                    return null;
                }
            }
            int len = is.read(buffer);
            is.close();
            StrictMode.setThreadPolicyMask(oldMask);
            long readTime = SystemClock.uptimeMillis() - startTime;
            if (readTime > 100) {
                Slog.w(TAG, "Reading wakelock stats took " + readTime + "ms");
            }
            if (len > 0) {
                if (len >= buffer.length) {
                    Slog.wtf(TAG, "Kernel wake locks exceeded buffer size " + buffer.length);
                }
                int i = 0;
                while (true) {
                    if (i >= len) {
                        break;
                    } else if (buffer[i] == 0) {
                        len = i;
                        break;
                    } else {
                        i++;
                    }
                }
            }
            return parseProcWakelocks(buffer, len, wakeup_sources, staleStats);
        } catch (IOException e3) {
            Slog.wtf(TAG, "failed to read kernel wakelocks", e3);
            return null;
        } finally {
            StrictMode.setThreadPolicyMask(oldMask);
        }
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:78:0x012e -> B:79:0x012f). Please submit an issue!!! */
    @VisibleForTesting
    public synchronized KernelWakelockStats parseProcWakelocks(byte[] wlBuffer, int len, boolean wakeup_sources, KernelWakelockStats staleStats) {
        int i;
        byte b;
        int startIndex;
        long totalTime;
        char c = 0;
        int i2 = 0;
        while (true) {
            i = i2;
            b = 10;
            if (i >= len || wlBuffer[i] == 10 || wlBuffer[i] == 0) {
                break;
            }
            i2 = i + 1;
        }
        int i3 = i + 1;
        int endIndex = i3;
        synchronized (this) {
            try {
                sKernelWakelockUpdateVersion++;
                int startIndex2 = i3;
                while (true) {
                    if (endIndex < len) {
                        int endIndex2 = startIndex2;
                        int endIndex3 = endIndex2;
                        while (endIndex3 < len) {
                            try {
                                if (wlBuffer[endIndex3] == b || wlBuffer[endIndex3] == 0) {
                                    break;
                                }
                                endIndex3++;
                            } catch (Throwable th) {
                                th = th;
                                throw th;
                            }
                        }
                        if (endIndex3 > len - 1) {
                            startIndex = startIndex2;
                            break;
                        }
                        try {
                            String[] nameStringArray = this.mProcWakelocksName;
                            long[] wlData = this.mProcWakelocksData;
                            for (int j = startIndex2; j < endIndex3; j++) {
                                if ((wlBuffer[j] & 128) != 0) {
                                    wlBuffer[j] = 63;
                                }
                            }
                            int endIndex4 = endIndex3;
                            int startIndex3 = startIndex2;
                            try {
                                boolean parsed = Process.parseProcLine(wlBuffer, startIndex2, endIndex3, wakeup_sources ? WAKEUP_SOURCES_FORMAT : PROC_WAKELOCKS_FORMAT, nameStringArray, wlData, null);
                                String name = nameStringArray[c];
                                int count = (int) wlData[1];
                                if (wakeup_sources) {
                                    totalTime = wlData[2] * 1000;
                                } else {
                                    long totalTime2 = wlData[2];
                                    totalTime = (totalTime2 + 500) / 1000;
                                }
                                long totalTime3 = totalTime;
                                if (parsed && name.length() > 0) {
                                    if (!staleStats.containsKey(name)) {
                                        staleStats.put(name, new KernelWakelockStats.Entry(count, totalTime3, sKernelWakelockUpdateVersion));
                                    } else {
                                        KernelWakelockStats.Entry kwlStats = (KernelWakelockStats.Entry) staleStats.get(name);
                                        if (kwlStats.mVersion == sKernelWakelockUpdateVersion) {
                                            kwlStats.mCount += count;
                                            kwlStats.mTotalTime += totalTime3;
                                        } else {
                                            kwlStats.mCount = count;
                                            kwlStats.mTotalTime = totalTime3;
                                            kwlStats.mVersion = sKernelWakelockUpdateVersion;
                                        }
                                    }
                                } else if (!parsed) {
                                    try {
                                        Slog.wtf(TAG, "Failed to parse proc line: " + new String(wlBuffer, startIndex3, endIndex4 - startIndex3));
                                    } catch (Exception e) {
                                        Slog.wtf(TAG, "Failed to parse proc line!");
                                    }
                                }
                                startIndex2 = endIndex4 + 1;
                                endIndex = endIndex4;
                                c = 0;
                                b = 10;
                            } catch (Throwable th2) {
                                th = th2;
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                        }
                    } else {
                        startIndex = startIndex2;
                        break;
                    }
                }
                try {
                    Iterator<KernelWakelockStats.Entry> itr = staleStats.values().iterator();
                    while (itr.hasNext()) {
                        if (itr.next().mVersion != sKernelWakelockUpdateVersion) {
                            itr.remove();
                        }
                    }
                    staleStats.kernelWakelockVersion = sKernelWakelockUpdateVersion;
                    return staleStats;
                } catch (Throwable th4) {
                    th = th4;
                    throw th;
                }
            } catch (Throwable th5) {
                th = th5;
            }
        }
    }
}
