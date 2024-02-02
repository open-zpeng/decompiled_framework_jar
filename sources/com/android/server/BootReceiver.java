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
import android.text.TextUtils;
import android.util.AtomicFile;
import android.util.EventLog;
import android.util.Slog;
import android.util.StatsLog;
import com.android.internal.annotations.VisibleForTesting;
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
                    } catch (RemoteException e2) {
                    }
                    if (!onlyCore) {
                        BootReceiver.this.removeOldUpdatePackages(context);
                    }
                } catch (Exception e3) {
                    Slog.e(BootReceiver.TAG, "Can't remove old update packages", e3);
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
        final DropBoxManager db = (DropBoxManager) ctx.getSystemService(Context.DROPBOX_SERVICE);
        final String headers = getBootHeadersToLogAndUpdate();
        String bootReason = SystemProperties.get("ro.boot.bootreason", null);
        String recovery = RecoverySystem.handleAftermath(ctx);
        if (recovery != null && db != null) {
            db.addText("SYSTEM_RECOVERY_LOG", headers + recovery);
        }
        String lastKmsgFooter = "";
        if (bootReason != null) {
            StringBuilder sb = new StringBuilder(512);
            sb.append("\n");
            sb.append("Boot info:\n");
            sb.append("Last boot reason: ");
            sb.append(bootReason);
            sb.append("\n");
            lastKmsgFooter = sb.toString();
        }
        String lastKmsgFooter2 = lastKmsgFooter;
        HashMap<String, Long> timestamps = readTimestamps();
        if (SystemProperties.getLong("ro.runtime.firstboot", 0L) == 0) {
            if (!StorageManager.inCryptKeeperBounce()) {
                String now = Long.toString(System.currentTimeMillis());
                SystemProperties.set("ro.runtime.firstboot", now);
            }
            if (db != null) {
                db.addText("SYSTEM_BOOT", headers);
            }
            addFileWithFootersToDropBox(db, timestamps, headers, lastKmsgFooter2, "/proc/last_kmsg", -LOG_SIZE, "SYSTEM_LAST_KMSG");
            addFileWithFootersToDropBox(db, timestamps, headers, lastKmsgFooter2, "/sys/fs/pstore/console-ramoops", -LOG_SIZE, "SYSTEM_LAST_KMSG");
            addFileWithFootersToDropBox(db, timestamps, headers, lastKmsgFooter2, "/sys/fs/pstore/console-ramoops-0", -LOG_SIZE, "SYSTEM_LAST_KMSG");
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
        int i = 0;
        while (true) {
            int i2 = i;
            if (tombstoneFiles == null || i2 >= tombstoneFiles.length) {
                break;
            }
            if (tombstoneFiles[i2].isFile()) {
                addFileToDropBox(db, timestamps, headers, tombstoneFiles[i2].getPath(), LOG_SIZE, TAG_TOMBSTONE);
            }
            i = i2 + 1;
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
    }

    private static void addTextToDropBox(DropBoxManager db, String tag, String text, String filename, int maxSize) {
        Slog.i(TAG, "Copying " + filename + " to DropBox (" + tag + ")");
        db.addText(tag, text);
        EventLog.writeEvent(81002, filename, Integer.valueOf(maxSize), tag);
    }

    private static void addAuditErrorsToDropBox(DropBoxManager db, HashMap<String, Long> timestamps, String headers, int maxSize, String tag) throws IOException {
        String[] split;
        if (db == null || !db.isTagEnabled(tag)) {
            return;
        }
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

    /* JADX WARN: Code restructure failed: missing block: B:5:0x000b, code lost:
        if (r21.isTagEnabled(r7) == false) goto L35;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
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
            goto Le
        L14:
            r0 = 0
            java.lang.String r1 = "BootReceiver"
            java.lang.String r2 = "Checking for fsck errors"
            android.util.Slog.i(r1, r2)
            java.io.File r1 = new java.io.File
            java.lang.String r2 = "/dev/fscklogs/log"
            r1.<init>(r2)
            r9 = r1
            long r10 = r9.lastModified()
            r1 = 0
            int r1 = (r10 > r1 ? 1 : (r10 == r1 ? 0 : -1))
            if (r1 > 0) goto L2f
            return
        L2f:
            java.lang.String r1 = "[[TRUNCATED]]\n"
            r12 = r24
            java.lang.String r13 = android.os.FileUtils.readTextFile(r9, r12, r1)
        */
        //  java.lang.String r1 = "fs_stat,[^,]*/([^/,]+),(0x[0-9a-fA-F]+)"
        /*
            java.util.regex.Pattern r14 = java.util.regex.Pattern.compile(r1)
            java.lang.String r1 = "\n"
            java.lang.String[] r15 = r13.split(r1)
            r1 = 0
            r2 = 0
            int r3 = r15.length
            r4 = 0
            r16 = r0
            r5 = r1
        L4a:
            if (r4 >= r3) goto L9d
            r0 = r15[r4]
            java.lang.String r1 = "FILE SYSTEM WAS MODIFIED"
            boolean r1 = r0.contains(r1)
            if (r1 == 0) goto L5c
            r1 = 1
            r16 = r1
        L59:
            r20 = r3
            goto L96
        L5c:
            java.lang.String r1 = "fs_stat"
            boolean r1 = r0.contains(r1)
            if (r1 == 0) goto L90
            java.util.regex.Matcher r1 = r14.matcher(r0)
            boolean r17 = r1.find()
            if (r17 == 0) goto L73
            handleFsckFsStat(r1, r15, r2, r5)
            r2 = r5
            goto L59
        L73:
            r18 = r1
            java.lang.String r1 = "BootReceiver"
            r19 = r2
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r20 = r3
            java.lang.String r3 = "cannot parse fs_stat:"
            r2.append(r3)
            r2.append(r0)
            java.lang.String r2 = r2.toString()
            android.util.Slog.w(r1, r2)
            goto L94
        L90:
            r19 = r2
            r20 = r3
        L94:
            r2 = r19
        L96:
            int r5 = r5 + 1
            int r4 = r4 + 1
            r3 = r20
            goto L4a
        L9d:
            r19 = r2
            if (r8 == 0) goto Lb4
            if (r16 == 0) goto Lb4
            java.lang.String r3 = "/dev/fscklogs/log"
            r0 = r6
            r1 = r22
            r17 = r19
            r2 = r23
            r4 = r12
            r18 = r5
            r5 = r7
            addFileToDropBox(r0, r1, r2, r3, r4, r5)
            goto Lb8
        Lb4:
            r18 = r5
            r17 = r19
        Lb8:
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
            String[] array = metricsStr.split(",");
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

    private static void logStatsdShutdownAtom(String rebootStr, String reasonStr, String startStr, String durationStr) {
        boolean reboot = false;
        String reason = "<EMPTY>";
        long start = 0;
        long duration = 0;
        if (rebootStr != null) {
            if (rebootStr.equals("y")) {
                reboot = true;
            } else if (!rebootStr.equals("n")) {
                Slog.e(TAG, "Unexpected value for reboot : " + rebootStr);
            }
        } else {
            Slog.e(TAG, "No value received for reboot");
        }
        boolean reboot2 = reboot;
        if (reasonStr != null) {
            reason = reasonStr;
        } else {
            Slog.e(TAG, "No value received for shutdown reason");
        }
        if (startStr != null) {
            try {
                start = Long.parseLong(startStr);
            } catch (NumberFormatException e) {
                Slog.e(TAG, "Cannot parse shutdown start time: " + startStr);
            }
        } else {
            Slog.e(TAG, "No value received for shutdown start time");
        }
        if (durationStr != null) {
            try {
                duration = Long.parseLong(durationStr);
            } catch (NumberFormatException e2) {
                Slog.e(TAG, "Cannot parse shutdown duration: " + startStr);
            }
        } else {
            Slog.e(TAG, "No value received for shutdown duration");
        }
        StatsLog.write(56, reboot2, reason, start, duration);
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
                Slog.i(TAG, "boot_fs_shutdown," + matcher.group(1) + "," + matcher.group(2));
                return;
            }
            MetricsLogger.histogram(null, "boot_fs_shutdown_umount_stat", 4);
            Slog.w(TAG, "boot_fs_shutdown, string not found");
        } catch (IOException e) {
            Slog.w(TAG, "cannot read last msg", e);
        }
    }

    @VisibleForTesting
    public static int fixFsckFsStat(String partition, int statOrg, String[] lines, int startLineNumber, int endLineNumber) {
        String line;
        Pattern passPattern;
        Pattern treeOptPattern;
        if ((statOrg & 1024) != 0) {
            Pattern passPattern2 = Pattern.compile(FSCK_PASS_PATTERN);
            Pattern treeOptPattern2 = Pattern.compile(FSCK_TREE_OPTIMIZATION_PATTERN);
            boolean foundOtherFix = false;
            String otherFixLine = null;
            boolean foundTimestampAdjustment = false;
            boolean foundTimestampAdjustment2 = false;
            boolean foundQuotaFix = false;
            String currentPass = "";
            int i = startLineNumber;
            while (true) {
                if (i >= endLineNumber) {
                    break;
                }
                line = lines[i];
                if (line.contains(FSCK_FS_MODIFIED)) {
                    break;
                }
                if (line.startsWith("Pass ")) {
                    Matcher matcher = passPattern2.matcher(line);
                    if (matcher.find()) {
                        currentPass = matcher.group(1);
                    }
                    passPattern = passPattern2;
                    treeOptPattern = treeOptPattern2;
                } else if (!line.startsWith("Inode ")) {
                    passPattern = passPattern2;
                    treeOptPattern = treeOptPattern2;
                    if (line.startsWith("[QUOTA WARNING]") && currentPass.equals("5")) {
                        Slog.i(TAG, "fs_stat, partition:" + partition + " found quota warning:" + line);
                        foundTimestampAdjustment2 = true;
                        if (!foundQuotaFix) {
                            otherFixLine = line;
                            break;
                        }
                    } else if (!line.startsWith("Update quota info") || !currentPass.equals("5")) {
                        if (!line.startsWith("Timestamp(s) on inode") || !line.contains("beyond 2310-04-04 are likely pre-1970") || !currentPass.equals("1")) {
                            String line2 = line.trim();
                            if (!line2.isEmpty() && !currentPass.isEmpty()) {
                                foundOtherFix = true;
                                otherFixLine = line2;
                                break;
                            }
                        } else {
                            Slog.i(TAG, "fs_stat, partition:" + partition + " found timestamp adjustment:" + line);
                            if (lines[i + 1].contains("Fix? yes")) {
                                i++;
                            }
                            foundTimestampAdjustment = true;
                        }
                    }
                } else if (!treeOptPattern2.matcher(line).find() || !currentPass.equals("1")) {
                    break;
                } else {
                    foundQuotaFix = true;
                    passPattern = passPattern2;
                    StringBuilder sb = new StringBuilder();
                    treeOptPattern = treeOptPattern2;
                    sb.append("fs_stat, partition:");
                    sb.append(partition);
                    sb.append(" found tree optimization:");
                    sb.append(line);
                    Slog.i(TAG, sb.toString());
                }
                i++;
                passPattern2 = passPattern;
                treeOptPattern2 = treeOptPattern;
            }
            foundOtherFix = true;
            otherFixLine = line;
            if (foundOtherFix) {
                if (otherFixLine != null) {
                    Slog.i(TAG, "fs_stat, partition:" + partition + " fix:" + otherFixLine);
                    return statOrg;
                }
                return statOrg;
            } else if (foundTimestampAdjustment2 && !foundQuotaFix) {
                Slog.i(TAG, "fs_stat, got quota fix without tree optimization, partition:" + partition);
                return statOrg;
            } else if ((foundQuotaFix && foundTimestampAdjustment2) || foundTimestampAdjustment) {
                Slog.i(TAG, "fs_stat, partition:" + partition + " fix ignored");
                int stat = statOrg & (-1025);
                return stat;
            } else {
                return statOrg;
            }
        }
        return statOrg;
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

    /* JADX WARN: Code restructure failed: missing block: B:33:0x0091, code lost:
        if (1 == 0) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0143, code lost:
        if (0 != 0) goto L30;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.util.HashMap<java.lang.String, java.lang.Long> readTimestamps() {
        /*
            Method dump skipped, instructions count: 338
            To view this dump add '--comments-level debug' option
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
