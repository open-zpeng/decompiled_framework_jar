package com.android.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageManager;
import android.os.Build;
import android.os.DropBoxManager;
import android.os.Environment;
import android.os.FileObserver;
import android.os.FileUtils;
import android.os.RecoverySystem;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.storage.StorageManager;
import android.provider.Downloads;
import android.provider.SettingsStringUtil;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.AtomicFile;
import android.util.EventLog;
import android.util.Slog;
import android.util.StatsLog;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.FastXmlSerializer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlSerializer;

/* loaded from: classes3.dex */
public class BootReceiver extends BroadcastReceiver {
    private static final String FSCK_FS_MODIFIED = "FILE SYSTEM WAS MODIFIED";
    private static final String FSCK_PASS_PATTERN = "Pass ([1-9]E?):";
    private static final String FSCK_TREE_OPTIMIZATION_PATTERN = "Inode [0-9]+ extent tree.*could be shorter";
    private static final int FS_STAT_FS_FIXED = 1024;
    private static final String FS_STAT_PATTERN = "fs_stat,[^,]*/([^/,]+),(0x[0-9a-fA-F]+)";
    private static final String LAST_HEADER_FILE = "last-header.txt";
    private static final String[] LAST_KMSG_FILES;
    private static final String LAST_SHUTDOWN_TIME_PATTERN = "powerctl_shutdown_time_ms:([0-9]+):([0-9]+)";
    private static final String LOG_FILES_FILE = "log-files.xml";
    private static final int LOG_SIZE;
    private static final String METRIC_SHUTDOWN_TIME_START = "begin_shutdown";
    private static final String METRIC_SYSTEM_SERVER = "shutdown_system_server";
    private static final String[] MOUNT_DURATION_PROPS_POSTFIX;
    private static final String OLD_UPDATER_CLASS = "com.google.android.systemupdater.SystemUpdateReceiver";
    private static final String OLD_UPDATER_PACKAGE = "com.google.android.systemupdater";
    private static final String SHUTDOWN_METRICS_FILE = "/data/system/shutdown-metrics.txt";
    private static final String SHUTDOWN_TRON_METRICS_PREFIX = "shutdown_";
    private static final String TAG = "BootReceiver";
    private static final String TAG_TOMBSTONE = "SYSTEM_TOMBSTONE";
    private static final File TOMBSTONE_DIR;
    private static final int UMOUNT_STATUS_NOT_AVAILABLE = 4;
    private static final File lastHeaderFile;
    private static final AtomicFile sFile;
    private static FileObserver sTombstoneObserver;

    static /* synthetic */ HashMap access$200() {
        return readTimestamps();
    }

    static {
        LOG_SIZE = SystemProperties.getInt("ro.debuggable", 0) == 1 ? 98304 : 65536;
        TOMBSTONE_DIR = new File("/data/tombstones");
        sTombstoneObserver = null;
        sFile = new AtomicFile(new File(Environment.getDataSystemDirectory(), LOG_FILES_FILE), "log-files");
        lastHeaderFile = new File(Environment.getDataSystemDirectory(), LAST_HEADER_FILE);
        MOUNT_DURATION_PROPS_POSTFIX = new String[]{"early", "default", "late"};
        LAST_KMSG_FILES = new String[]{"/sys/fs/pstore/console-ramoops", "/proc/last_kmsg"};
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.server.BootReceiver$1] */
    @Override // android.content.BroadcastReceiver
    public void onReceive(final Context context, Intent intent) {
        new Thread() { // from class: com.android.server.BootReceiver.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    BootReceiver.this.logBootEvents(context);
                } catch (Exception e) {
                    Slog.e(BootReceiver.TAG, "Can't log boot events", e);
                }
                boolean onlyCore = false;
                try {
                    try {
                        onlyCore = IPackageManager.Stub.asInterface(ServiceManager.getService("package")).isOnlyCoreApps();
                    } catch (Exception e2) {
                        Slog.e(BootReceiver.TAG, "Can't remove old update packages", e2);
                        return;
                    }
                } catch (RemoteException e3) {
                }
                if (!onlyCore) {
                    BootReceiver.this.removeOldUpdatePackages(context);
                }
            }
        }.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeOldUpdatePackages(Context context) {
        Downloads.removeAllDownloadsByPackage(context, OLD_UPDATER_PACKAGE, OLD_UPDATER_CLASS);
    }

    private String getPreviousBootHeaders() {
        try {
            return FileUtils.readTextFile(lastHeaderFile, 0, null);
        } catch (IOException e) {
            return null;
        }
    }

    private String getCurrentBootHeaders() throws IOException {
        StringBuilder sb = new StringBuilder(512);
        sb.append("Build: ");
        sb.append(Build.FINGERPRINT);
        sb.append("\n");
        sb.append("Hardware: ");
        sb.append(Build.BOARD);
        sb.append("\n");
        sb.append("Revision: ");
        sb.append(SystemProperties.get("ro.revision", ""));
        sb.append("\n");
        sb.append("Bootloader: ");
        sb.append(Build.BOOTLOADER);
        sb.append("\n");
        sb.append("Radio: ");
        sb.append(Build.getRadioVersion());
        sb.append("\n");
        sb.append("Kernel: ");
        sb.append(FileUtils.readTextFile(new File("/proc/version"), 1024, "...\n"));
        sb.append("\n");
        return sb.toString();
    }

    private String getBootHeadersToLogAndUpdate() throws IOException {
        String oldHeaders = getPreviousBootHeaders();
        String newHeaders = getCurrentBootHeaders();
        try {
            FileUtils.stringToFile(lastHeaderFile, newHeaders);
        } catch (IOException e) {
            Slog.e(TAG, "Error writing " + lastHeaderFile, e);
        }
        if (oldHeaders == null) {
            return "isPrevious: false\n" + newHeaders;
        }
        return "isPrevious: true\n" + oldHeaders;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logBootEvents(Context ctx) throws IOException {
        String lastKmsgFooter;
        final DropBoxManager db = (DropBoxManager) ctx.getSystemService(Context.DROPBOX_SERVICE);
        final String headers = getBootHeadersToLogAndUpdate();
        String bootReason = SystemProperties.get("ro.boot.bootreason", null);
        String recovery = RecoverySystem.handleAftermath(ctx);
        if (recovery != null && db != null) {
            db.addText("SYSTEM_RECOVERY_LOG", headers + recovery);
        }
        if (bootReason == null) {
            lastKmsgFooter = "";
        } else {
            StringBuilder sb = new StringBuilder(512);
            sb.append("\n");
            sb.append("Boot info:\n");
            sb.append("Last boot reason: ");
            sb.append(bootReason);
            sb.append("\n");
            String lastKmsgFooter2 = sb.toString();
            lastKmsgFooter = lastKmsgFooter2;
        }
        HashMap<String, Long> timestamps = readTimestamps();
        if (SystemProperties.getLong("ro.runtime.firstboot", 0L) == 0) {
            if (!StorageManager.inCryptKeeperBounce()) {
                String now = Long.toString(System.currentTimeMillis());
                SystemProperties.set("ro.runtime.firstboot", now);
            }
            if (db != null) {
                db.addText("SYSTEM_BOOT", headers);
            }
            String str = lastKmsgFooter;
            addFileWithFootersToDropBox(db, timestamps, headers, str, "/proc/last_kmsg", -LOG_SIZE, "SYSTEM_LAST_KMSG");
            addFileWithFootersToDropBox(db, timestamps, headers, str, "/sys/fs/pstore/console-ramoops", -LOG_SIZE, "SYSTEM_LAST_KMSG");
            addFileWithFootersToDropBox(db, timestamps, headers, str, "/sys/fs/pstore/console-ramoops-0", -LOG_SIZE, "SYSTEM_LAST_KMSG");
            addFileToDropBox(db, timestamps, headers, "/cache/recovery/log", -LOG_SIZE, "SYSTEM_RECOVERY_LOG");
            addFileToDropBox(db, timestamps, headers, "/cache/recovery/last_kmsg", -LOG_SIZE, "SYSTEM_RECOVERY_KMSG");
            addAuditErrorsToDropBox(db, timestamps, headers, -LOG_SIZE, "SYSTEM_AUDIT");
        } else if (db != null) {
            db.addText("SYSTEM_RESTART", headers);
        }
        logFsShutdownTime();
        logFsMountTime();
        addFsckErrorsToDropBoxAndLogFsStat(db, timestamps, headers, -LOG_SIZE, "SYSTEM_FSCK");
        logSystemServerShutdownTimeMetrics();
        File[] tombstoneFiles = TOMBSTONE_DIR.listFiles();
        for (int i = 0; tombstoneFiles != null && i < tombstoneFiles.length; i++) {
            if (tombstoneFiles[i].isFile()) {
                addFileToDropBox(db, timestamps, headers, tombstoneFiles[i].getPath(), LOG_SIZE, TAG_TOMBSTONE);
            }
        }
        writeTimestamps(timestamps);
        sTombstoneObserver = new FileObserver(TOMBSTONE_DIR.getPath(), 256) { // from class: com.android.server.BootReceiver.2
            @Override // android.os.FileObserver
            public void onEvent(int event, String path) {
                HashMap<String, Long> timestamps2 = BootReceiver.access$200();
                try {
                    File file = new File(BootReceiver.TOMBSTONE_DIR, path);
                    if (file.isFile() && file.getName().startsWith("tombstone_")) {
                        BootReceiver.addFileToDropBox(db, timestamps2, headers, file.getPath(), BootReceiver.LOG_SIZE, BootReceiver.TAG_TOMBSTONE);
                    }
                } catch (IOException e) {
                    Slog.e(BootReceiver.TAG, "Can't log tombstone", e);
                }
                BootReceiver.this.writeTimestamps(timestamps2);
            }
        };
        sTombstoneObserver.startWatching();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addFileToDropBox(DropBoxManager db, HashMap<String, Long> timestamps, String headers, String filename, int maxSize, String tag) throws IOException {
        addFileWithFootersToDropBox(db, timestamps, headers, "", filename, maxSize, tag);
    }

    private static void addFileWithFootersToDropBox(DropBoxManager db, HashMap<String, Long> timestamps, String headers, String footers, String filename, int maxSize, String tag) throws IOException {
        if (db == null || !db.isTagEnabled(tag)) {
            return;
        }
        File file = new File(filename);
        long fileTime = file.lastModified();
        if (fileTime <= 0) {
            return;
        }
        if (timestamps.containsKey(filename) && timestamps.get(filename).longValue() == fileTime) {
            return;
        }
        timestamps.put(filename, Long.valueOf(fileTime));
        String fileContents = FileUtils.readTextFile(file, maxSize, "[[TRUNCATED]]\n");
        String text = headers + fileContents + footers;
        if (tag.equals(TAG_TOMBSTONE) && fileContents.contains(">>> system_server <<<")) {
            addTextToDropBox(db, "system_server_native_crash", text, filename, maxSize);
        } else {
            addTextToDropBox(db, tag, text, filename, maxSize);
        }
        if (tag.equals(TAG_TOMBSTONE)) {
            StatsLog.write(186);
        }
    }

    private static void addTextToDropBox(DropBoxManager db, String tag, String text, String filename, int maxSize) {
        Slog.i(TAG, "Copying " + filename + " to DropBox (" + tag + ")");
        db.addText(tag, text);
        EventLog.writeEvent((int) DropboxLogTags.DROPBOX_FILE_COPY, filename, Integer.valueOf(maxSize), tag);
    }

    private static void addAuditErrorsToDropBox(DropBoxManager db, HashMap<String, Long> timestamps, String headers, int maxSize, String tag) throws IOException {
        String[] split;
        if (db != null && db.isTagEnabled(tag)) {
            Slog.i(TAG, "Copying audit failures to DropBox");
            File file = new File("/proc/last_kmsg");
            long fileTime = file.lastModified();
            if (fileTime <= 0) {
                file = new File("/sys/fs/pstore/console-ramoops");
                fileTime = file.lastModified();
                if (fileTime <= 0) {
                    file = new File("/sys/fs/pstore/console-ramoops-0");
                    fileTime = file.lastModified();
                }
            }
            if (fileTime <= 0) {
                return;
            }
            if (timestamps.containsKey(tag) && timestamps.get(tag).longValue() == fileTime) {
                return;
            }
            timestamps.put(tag, Long.valueOf(fileTime));
            String log = FileUtils.readTextFile(file, maxSize, "[[TRUNCATED]]\n");
            StringBuilder sb = new StringBuilder();
            for (String line : log.split("\n")) {
                if (line.contains("audit")) {
                    sb.append(line + "\n");
                }
            }
            Slog.i(TAG, "Copied " + sb.toString().length() + " worth of audits to DropBox");
            StringBuilder sb2 = new StringBuilder();
            sb2.append(headers);
            sb2.append(sb.toString());
            db.addText(tag, sb2.toString());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x002e A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:13:0x002f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void addFsckErrorsToDropBoxAndLogFsStat(android.os.DropBoxManager r21, java.util.HashMap<java.lang.String, java.lang.Long> r22, java.lang.String r23, int r24, java.lang.String r25) throws java.io.IOException {
        /*
            r6 = r21
            r0 = 1
            if (r6 == 0) goto L10
            r7 = r25
            boolean r1 = r6.isTagEnabled(r7)
            if (r1 != 0) goto Le
            goto L12
        Le:
            r8 = r0
            goto L14
        L10:
            r7 = r25
        L12:
            r0 = 0
            r8 = r0
        L14:
            r0 = 0
            java.lang.String r1 = "BootReceiver"
            java.lang.String r2 = "Checking for fsck errors"
            android.util.Slog.i(r1, r2)
            java.io.File r2 = new java.io.File
            java.lang.String r3 = "/dev/fscklogs/log"
            r2.<init>(r3)
            r9 = r2
            long r10 = r9.lastModified()
            r2 = 0
            int r2 = (r10 > r2 ? 1 : (r10 == r2 ? 0 : -1))
            if (r2 > 0) goto L2f
            return
        L2f:
            java.lang.String r2 = "[[TRUNCATED]]\n"
            r12 = r24
            java.lang.String r13 = android.os.FileUtils.readTextFile(r9, r12, r2)
        */
        //  java.lang.String r2 = "fs_stat,[^,]*/([^/,]+),(0x[0-9a-fA-F]+)"
        /*
            java.util.regex.Pattern r14 = java.util.regex.Pattern.compile(r2)
            java.lang.String r2 = "\n"
            java.lang.String[] r15 = r13.split(r2)
            r2 = 0
            r3 = 0
            int r4 = r15.length
            r5 = 0
            r16 = r0
            r20 = r3
            r3 = r2
            r2 = r20
        L4e:
            if (r5 >= r4) goto L9c
            r0 = r15[r5]
            r17 = r4
            java.lang.String r4 = "FILE SYSTEM WAS MODIFIED"
            boolean r4 = r0.contains(r4)
            if (r4 == 0) goto L60
            r4 = 1
            r16 = r4
            goto L94
        L60:
            java.lang.String r4 = "fs_stat"
            boolean r4 = r0.contains(r4)
            if (r4 == 0) goto L90
            java.util.regex.Matcher r4 = r14.matcher(r0)
            boolean r18 = r4.find()
            if (r18 == 0) goto L77
            handleFsckFsStat(r4, r15, r2, r3)
            r2 = r3
            goto L94
        L77:
            r18 = r2
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r19 = r4
            java.lang.String r4 = "cannot parse fs_stat:"
            r2.append(r4)
            r2.append(r0)
            java.lang.String r2 = r2.toString()
            android.util.Slog.w(r1, r2)
            goto L92
        L90:
            r18 = r2
        L92:
            r2 = r18
        L94:
            int r3 = r3 + 1
            int r5 = r5 + 1
            r4 = r17
            goto L4e
        L9c:
            r18 = r2
            if (r8 == 0) goto Lb7
            if (r16 == 0) goto Lb7
            java.lang.String r4 = "/dev/fscklogs/log"
            r0 = r21
            r1 = r22
            r17 = r18
            r2 = r23
            r18 = r3
            r3 = r4
            r4 = r24
            r5 = r25
            addFileToDropBox(r0, r1, r2, r3, r4, r5)
            goto Lbb
        Lb7:
            r17 = r18
            r18 = r3
        Lbb:
            r9.delete()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.BootReceiver.addFsckErrorsToDropBoxAndLogFsStat(android.os.DropBoxManager, java.util.HashMap, java.lang.String, int, java.lang.String):void");
    }

    private static void logFsMountTime() {
        String[] strArr;
        for (String propPostfix : MOUNT_DURATION_PROPS_POSTFIX) {
            int duration = SystemProperties.getInt("ro.boottime.init.mount_all." + propPostfix, 0);
            if (duration != 0) {
                MetricsLogger.histogram(null, "boot_mount_all_duration_" + propPostfix, duration);
            }
        }
    }

    private static void logSystemServerShutdownTimeMetrics() {
        File metricsFile = new File(SHUTDOWN_METRICS_FILE);
        String metricsStr = null;
        if (metricsFile.exists()) {
            try {
                metricsStr = FileUtils.readTextFile(metricsFile, 0, null);
            } catch (IOException e) {
                Slog.e(TAG, "Problem reading " + metricsFile, e);
            }
        }
        if (!TextUtils.isEmpty(metricsStr)) {
            String[] array = metricsStr.split(SmsManager.REGEX_PREFIX_DELIMITER);
            String duration = null;
            String duration2 = null;
            String start_time = null;
            String reason = null;
            for (String keyValueStr : array) {
                String[] keyValue = keyValueStr.split(SettingsStringUtil.DELIMITER);
                if (keyValue.length != 2) {
                    Slog.e(TAG, "Wrong format of shutdown metrics - " + metricsStr);
                } else {
                    if (keyValue[0].startsWith(SHUTDOWN_TRON_METRICS_PREFIX)) {
                        logTronShutdownMetric(keyValue[0], keyValue[1]);
                        if (keyValue[0].equals(METRIC_SYSTEM_SERVER)) {
                            duration = keyValue[1];
                        }
                    }
                    if (keyValue[0].equals("reboot")) {
                        reason = keyValue[1];
                    } else if (keyValue[0].equals("reason")) {
                        start_time = keyValue[1];
                    } else if (keyValue[0].equals(METRIC_SHUTDOWN_TIME_START)) {
                        duration2 = keyValue[1];
                    }
                }
            }
            logStatsdShutdownAtom(reason, start_time, duration2, duration);
        }
        metricsFile.delete();
    }

    private static void logTronShutdownMetric(String metricName, String valueStr) {
        try {
            int value = Integer.parseInt(valueStr);
            if (value >= 0) {
                MetricsLogger.histogram(null, metricName, value);
            }
        } catch (NumberFormatException e) {
            Slog.e(TAG, "Cannot parse metric " + metricName + " int value - " + valueStr);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0043  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0068  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x008d  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x006f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x004a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void logStatsdShutdownAtom(java.lang.String r17, java.lang.String r18, java.lang.String r19, java.lang.String r20) {
        /*
            r1 = r17
            r2 = r19
            r0 = 0
            java.lang.String r3 = "<EMPTY>"
            r4 = 0
            r6 = 0
            java.lang.String r8 = "BootReceiver"
            if (r1 == 0) goto L38
            java.lang.String r9 = "y"
            boolean r9 = r1.equals(r9)
            if (r9 == 0) goto L1b
            r0 = 1
            r9 = r0
            goto L3e
        L1b:
            java.lang.String r9 = "n"
            boolean r9 = r1.equals(r9)
            if (r9 != 0) goto L3d
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "Unexpected value for reboot : "
            r9.append(r10)
            r9.append(r1)
            java.lang.String r9 = r9.toString()
            android.util.Slog.e(r8, r9)
            goto L3d
        L38:
            java.lang.String r9 = "No value received for reboot"
            android.util.Slog.e(r8, r9)
        L3d:
            r9 = r0
        L3e:
            if (r18 == 0) goto L43
            r3 = r18
            goto L48
        L43:
            java.lang.String r0 = "No value received for shutdown reason"
            android.util.Slog.e(r8, r0)
        L48:
            if (r2 == 0) goto L68
            long r10 = java.lang.Long.parseLong(r19)     // Catch: java.lang.NumberFormatException -> L50
            r4 = r10
        L4f:
            goto L6d
        L50:
            r0 = move-exception
            r10 = r0
            r0 = r10
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "Cannot parse shutdown start time: "
            r10.append(r11)
            r10.append(r2)
            java.lang.String r10 = r10.toString()
            android.util.Slog.e(r8, r10)
            goto L4f
        L68:
            java.lang.String r0 = "No value received for shutdown start time"
            android.util.Slog.e(r8, r0)
        L6d:
            if (r20 == 0) goto L8d
            long r10 = java.lang.Long.parseLong(r20)     // Catch: java.lang.NumberFormatException -> L75
            r6 = r10
        L74:
            goto L92
        L75:
            r0 = move-exception
            r10 = r0
            r0 = r10
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            r10.<init>()
            java.lang.String r11 = "Cannot parse shutdown duration: "
            r10.append(r11)
            r10.append(r2)
            java.lang.String r10 = r10.toString()
            android.util.Slog.e(r8, r10)
            goto L74
        L8d:
            java.lang.String r0 = "No value received for shutdown duration"
            android.util.Slog.e(r8, r0)
        L92:
            r10 = 56
            r11 = r9
            r12 = r3
            r13 = r4
            r15 = r6
            android.util.StatsLog.write(r10, r11, r12, r13, r15)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.BootReceiver.logStatsdShutdownAtom(java.lang.String, java.lang.String, java.lang.String, java.lang.String):void");
    }

    private static void logFsShutdownTime() {
        File f = null;
        String[] strArr = LAST_KMSG_FILES;
        int length = strArr.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            String fileName = strArr[i];
            File file = new File(fileName);
            if (!file.exists()) {
                i++;
            } else {
                f = file;
                break;
            }
        }
        if (f == null) {
            return;
        }
        try {
            String lines = FileUtils.readTextFile(f, -16384, null);
            Pattern pattern = Pattern.compile(LAST_SHUTDOWN_TIME_PATTERN, 8);
            Matcher matcher = pattern.matcher(lines);
            if (matcher.find()) {
                MetricsLogger.histogram(null, "boot_fs_shutdown_duration", Integer.parseInt(matcher.group(1)));
                MetricsLogger.histogram(null, "boot_fs_shutdown_umount_stat", Integer.parseInt(matcher.group(2)));
                Slog.i(TAG, "boot_fs_shutdown," + matcher.group(1) + SmsManager.REGEX_PREFIX_DELIMITER + matcher.group(2));
                return;
            }
            MetricsLogger.histogram(null, "boot_fs_shutdown_umount_stat", 4);
            Slog.w(TAG, "boot_fs_shutdown, string not found");
        } catch (IOException e) {
            Slog.w(TAG, "cannot read last msg", e);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x008f, code lost:
        r8 = true;
        r9 = r14;
     */
    @com.android.internal.annotations.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int fixFsckFsStat(java.lang.String r18, int r19, java.lang.String[] r20, int r21, int r22) {
        /*
            Method dump skipped, instructions count: 405
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.BootReceiver.fixFsckFsStat(java.lang.String, int, java.lang.String[], int, int):int");
    }

    private static void handleFsckFsStat(Matcher match, String[] lines, int startLineNumber, int endLineNumber) {
        String partition = match.group(1);
        try {
            int stat = fixFsckFsStat(partition, Integer.decode(match.group(2)).intValue(), lines, startLineNumber, endLineNumber);
            MetricsLogger.histogram(null, "boot_fs_stat_" + partition, stat);
            Slog.i(TAG, "fs_stat, partition:" + partition + " stat:0x" + Integer.toHexString(stat));
        } catch (NumberFormatException e) {
            Slog.w(TAG, "cannot parse fs_stat: partition:" + partition + " stat:" + match.group(2));
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:32:0x0093, code lost:
        if (1 == 0) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0143, code lost:
        if (0 != 0) goto L29;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.util.HashMap<java.lang.String, java.lang.Long> readTimestamps() {
        /*
            Method dump skipped, instructions count: 338
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.BootReceiver.readTimestamps():java.util.HashMap");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeTimestamps(HashMap<String, Long> timestamps) {
        synchronized (sFile) {
            try {
                try {
                    FileOutputStream stream = sFile.startWrite();
                    try {
                        XmlSerializer out = new FastXmlSerializer();
                        out.setOutput(stream, StandardCharsets.UTF_8.name());
                        out.startDocument(null, true);
                        out.startTag(null, "log-files");
                        for (String filename : timestamps.keySet()) {
                            out.startTag(null, "log");
                            out.attribute(null, "filename", filename);
                            out.attribute(null, "timestamp", timestamps.get(filename).toString());
                            out.endTag(null, "log");
                        }
                        out.endTag(null, "log-files");
                        out.endDocument();
                        sFile.finishWrite(stream);
                    } catch (IOException e) {
                        Slog.w(TAG, "Failed to write timestamp file, using the backup: " + e);
                        sFile.failWrite(stream);
                    }
                } catch (IOException e2) {
                    Slog.w(TAG, "Failed to write timestamp file: " + e2);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
