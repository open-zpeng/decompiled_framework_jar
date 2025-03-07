package com.android.internal.os;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/* loaded from: classes3.dex */
public class StoragedUidIoStatsReader {
    private static final String TAG = StoragedUidIoStatsReader.class.getSimpleName();
    private static String sUidIoFile = "/proc/uid_io/stats";

    /* loaded from: classes3.dex */
    public interface Callback {
        void onUidStorageStats(int i, long j, long j2, long j3, long j4, long j5, long j6, long j7, long j8, long j9, long j10);
    }

    public StoragedUidIoStatsReader() {
    }

    @VisibleForTesting
    public StoragedUidIoStatsReader(String file) {
        sUidIoFile = file;
    }

    public void readAbsolute(Callback callback) {
        int oldMask = StrictMode.allowThreadDiskReadsMask();
        File file = new File(sUidIoFile);
        try {
            try {
                BufferedReader reader = Files.newBufferedReader(file.toPath());
                while (true) {
                    try {
                        String line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        String[] fields = TextUtils.split(line, WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                        if (fields.length != 11) {
                            String str = TAG;
                            Slog.e(str, "Malformed entry in " + sUidIoFile + ": " + line);
                        } else {
                            try {
                                String str2 = fields[0];
                                int uid = Integer.parseInt(fields[0], 10);
                                long fgCharsRead = Long.parseLong(fields[1], 10);
                                long fgCharsWrite = Long.parseLong(fields[2], 10);
                                long fgBytesRead = Long.parseLong(fields[3], 10);
                                long fgBytesWrite = Long.parseLong(fields[4], 10);
                                long bgCharsRead = Long.parseLong(fields[5], 10);
                                long bgCharsWrite = Long.parseLong(fields[6], 10);
                                long bgBytesRead = Long.parseLong(fields[7], 10);
                                long bgBytesWrite = Long.parseLong(fields[8], 10);
                                long fgFsync = Long.parseLong(fields[9], 10);
                                long bgFsync = Long.parseLong(fields[10], 10);
                                callback.onUidStorageStats(uid, fgCharsRead, fgCharsWrite, fgBytesRead, fgBytesWrite, bgCharsRead, bgCharsWrite, bgBytesRead, bgBytesWrite, fgFsync, bgFsync);
                            } catch (NumberFormatException e) {
                                String str3 = TAG;
                                Slog.e(str3, "Could not parse entry in " + sUidIoFile + ": " + e.getMessage());
                            }
                        }
                    } finally {
                    }
                }
                reader.close();
            } catch (IOException e2) {
                String str4 = TAG;
                Slog.e(str4, "Failed to read " + sUidIoFile + ": " + e2.getMessage());
            }
        } finally {
            StrictMode.setThreadPolicyMask(oldMask);
        }
    }
}
