package com.android.internal.os;

import android.os.StrictMode;
import android.system.Os;
import android.system.OsConstants;
import android.text.TextUtils;
import android.util.Slog;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
/* loaded from: classes3.dex */
public class KernelCpuSpeedReader {
    private static final String TAG = "KernelCpuSpeedReader";
    private final long[] mDeltaSpeedTimesMs;
    private final long mJiffyMillis;
    private final long[] mLastSpeedTimesMs;
    private final int mNumSpeedSteps;
    private final String mProcFile;

    public synchronized KernelCpuSpeedReader(int cpuNumber, int numSpeedSteps) {
        this.mProcFile = String.format("/sys/devices/system/cpu/cpu%d/cpufreq/stats/time_in_state", Integer.valueOf(cpuNumber));
        this.mNumSpeedSteps = numSpeedSteps;
        this.mLastSpeedTimesMs = new long[numSpeedSteps];
        this.mDeltaSpeedTimesMs = new long[numSpeedSteps];
        long jiffyHz = Os.sysconf(OsConstants._SC_CLK_TCK);
        this.mJiffyMillis = 1000 / jiffyHz;
    }

    public synchronized long[] readDelta() {
        String line;
        StrictMode.ThreadPolicy policy = StrictMode.allowThreadDiskReads();
        try {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(this.mProcFile));
                try {
                    TextUtils.SimpleStringSplitter splitter = new TextUtils.SimpleStringSplitter(' ');
                    for (int speedIndex = 0; speedIndex < this.mLastSpeedTimesMs.length && (line = reader.readLine()) != null; speedIndex++) {
                        splitter.setString(line);
                        splitter.next();
                        long time = Long.parseLong(splitter.next()) * this.mJiffyMillis;
                        if (time < this.mLastSpeedTimesMs[speedIndex]) {
                            this.mDeltaSpeedTimesMs[speedIndex] = time;
                        } else {
                            this.mDeltaSpeedTimesMs[speedIndex] = time - this.mLastSpeedTimesMs[speedIndex];
                        }
                        this.mLastSpeedTimesMs[speedIndex] = time;
                    }
                    $closeResource(null, reader);
                } finally {
                }
            } catch (Throwable th) {
                StrictMode.setThreadPolicy(policy);
                throw th;
            }
        } catch (IOException e) {
            Slog.e(TAG, "Failed to read cpu-freq: " + e.getMessage());
            Arrays.fill(this.mDeltaSpeedTimesMs, 0L);
        }
        StrictMode.setThreadPolicy(policy);
        return this.mDeltaSpeedTimesMs;
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

    public synchronized long[] readAbsolute() {
        String line;
        StrictMode.ThreadPolicy policy = StrictMode.allowThreadDiskReads();
        long[] speedTimeMs = new long[this.mNumSpeedSteps];
        try {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(this.mProcFile));
                try {
                    TextUtils.SimpleStringSplitter splitter = new TextUtils.SimpleStringSplitter(' ');
                    for (int speedIndex = 0; speedIndex < this.mNumSpeedSteps && (line = reader.readLine()) != null; speedIndex++) {
                        splitter.setString(line);
                        splitter.next();
                        long time = Long.parseLong(splitter.next()) * this.mJiffyMillis;
                        speedTimeMs[speedIndex] = time;
                    }
                    $closeResource(null, reader);
                } finally {
                }
            } finally {
                StrictMode.setThreadPolicy(policy);
            }
        } catch (IOException e) {
            Slog.e(TAG, "Failed to read cpu-freq: " + e.getMessage());
            Arrays.fill(speedTimeMs, 0L);
        }
        return speedTimeMs;
    }
}
