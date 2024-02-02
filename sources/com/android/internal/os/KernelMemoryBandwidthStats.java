package com.android.internal.os;

import android.os.StrictMode;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.LongSparseLongArray;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
/* loaded from: classes3.dex */
public class KernelMemoryBandwidthStats {
    private static final boolean DEBUG = false;
    private static final String TAG = "KernelMemoryBandwidthStats";
    private static final String mSysfsFile = "/sys/kernel/memory_state_time/show_stat";
    protected final LongSparseLongArray mBandwidthEntries = new LongSparseLongArray();
    private boolean mStatsDoNotExist = false;

    public synchronized void updateStats() {
        BufferedReader reader;
        if (this.mStatsDoNotExist) {
            return;
        }
        long startTime = SystemClock.uptimeMillis();
        StrictMode.ThreadPolicy policy = StrictMode.allowThreadDiskReads();
        try {
            try {
                try {
                    reader = new BufferedReader(new FileReader(mSysfsFile));
                } catch (IOException e) {
                    Slog.e(TAG, "Failed to read memory bandwidth: " + e.getMessage());
                    this.mBandwidthEntries.clear();
                }
            } catch (FileNotFoundException e2) {
                Slog.w(TAG, "No kernel memory bandwidth stats available");
                this.mBandwidthEntries.clear();
                this.mStatsDoNotExist = true;
            }
            try {
                parseStats(reader);
                reader.close();
                StrictMode.setThreadPolicy(policy);
                long readTime = SystemClock.uptimeMillis() - startTime;
                if (readTime > 100) {
                    Slog.w(TAG, "Reading memory bandwidth file took " + readTime + "ms");
                }
            } catch (Throwable th) {
                try {
                    throw th;
                } catch (Throwable th2) {
                    if (th != null) {
                        try {
                            reader.close();
                        } catch (Throwable th3) {
                            th.addSuppressed(th3);
                        }
                    } else {
                        reader.close();
                    }
                    throw th2;
                }
            }
        } catch (Throwable th4) {
            StrictMode.setThreadPolicy(policy);
            throw th4;
        }
    }

    @VisibleForTesting
    public synchronized void parseStats(BufferedReader reader) throws IOException {
        TextUtils.SimpleStringSplitter splitter = new TextUtils.SimpleStringSplitter(' ');
        this.mBandwidthEntries.clear();
        while (true) {
            String line = reader.readLine();
            if (line != null) {
                splitter.setString(line);
                splitter.next();
                int bandwidth = 0;
                do {
                    int index = this.mBandwidthEntries.indexOfKey(bandwidth);
                    if (index >= 0) {
                        this.mBandwidthEntries.put(bandwidth, this.mBandwidthEntries.valueAt(index) + (Long.parseLong(splitter.next()) / 1000000));
                    } else {
                        this.mBandwidthEntries.put(bandwidth, Long.parseLong(splitter.next()) / 1000000);
                    }
                    bandwidth++;
                } while (splitter.hasNext());
            } else {
                return;
            }
        }
    }

    public synchronized LongSparseLongArray getBandwidthEntries() {
        return this.mBandwidthEntries;
    }
}
