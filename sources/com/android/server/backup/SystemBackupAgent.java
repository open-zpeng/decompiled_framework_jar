package com.android.server.backup;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupHelper;
import android.app.backup.FullBackupDataOutput;
import android.app.backup.WallpaperBackupHelper;
import android.content.Context;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.UserHandle;
import com.google.android.collect.Sets;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/* loaded from: classes3.dex */
public class SystemBackupAgent extends BackupAgentHelper {
    private static final String ACCOUNT_MANAGER_HELPER = "account_manager";
    private static final String PREFERRED_HELPER = "preferred_activities";
    private static final String SHORTCUT_MANAGER_HELPER = "shortcut_manager";
    private static final String SLICES_HELPER = "slices";
    private static final String TAG = "SystemBackupAgent";
    private static final String USAGE_STATS_HELPER = "usage_stats";
    private static final String WALLPAPER_HELPER = "wallpaper";
    private static final String WALLPAPER_IMAGE_FILENAME = "wallpaper";
    private static final String WALLPAPER_IMAGE_KEY = "/data/data/com.android.settings/files/wallpaper";
    private int mUserId = 0;
    private static final String WALLPAPER_IMAGE_DIR = Environment.getUserSystemDirectory(0).getAbsolutePath();
    public static final String WALLPAPER_IMAGE = new File(Environment.getUserSystemDirectory(0), Context.WALLPAPER_SERVICE).getAbsolutePath();
    private static final String WALLPAPER_INFO_DIR = Environment.getUserSystemDirectory(0).getAbsolutePath();
    private static final String WALLPAPER_INFO_FILENAME = "wallpaper_info.xml";
    public static final String WALLPAPER_INFO = new File(Environment.getUserSystemDirectory(0), WALLPAPER_INFO_FILENAME).getAbsolutePath();
    private static final String PERMISSION_HELPER = "permissions";
    private static final String NOTIFICATION_HELPER = "notifications";
    private static final String SYNC_SETTINGS_HELPER = "account_sync_settings";
    private static final Set<String> sEligibleForMultiUser = Sets.newArraySet(PERMISSION_HELPER, NOTIFICATION_HELPER, SYNC_SETTINGS_HELPER);

    @Override // android.app.backup.BackupAgent
    public void onCreate(UserHandle user) {
        super.onCreate(user);
        this.mUserId = user.getIdentifier();
        addHelper(SYNC_SETTINGS_HELPER, new AccountSyncSettingsBackupHelper(this, this.mUserId));
        addHelper(PREFERRED_HELPER, new PreferredActivityBackupHelper());
        addHelper(NOTIFICATION_HELPER, new NotificationBackupHelper(this.mUserId));
        addHelper(PERMISSION_HELPER, new PermissionBackupHelper(this.mUserId));
        addHelper(USAGE_STATS_HELPER, new UsageStatsBackupHelper(this));
        addHelper(SHORTCUT_MANAGER_HELPER, new ShortcutBackupHelper());
        addHelper(ACCOUNT_MANAGER_HELPER, new AccountManagerBackupHelper());
        addHelper(SLICES_HELPER, new SliceBackupHelper(this));
    }

    @Override // android.app.backup.BackupAgent
    public void onFullBackup(FullBackupDataOutput data) throws IOException {
    }

    @Override // android.app.backup.BackupAgentHelper, android.app.backup.BackupAgent
    public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState) throws IOException {
        addHelper(Context.WALLPAPER_SERVICE, new WallpaperBackupHelper(this, new String[]{"/data/data/com.android.settings/files/wallpaper"}));
        addHelper("system_files", new WallpaperBackupHelper(this, new String[]{"/data/data/com.android.settings/files/wallpaper"}));
        super.onRestore(data, appVersionCode, newState);
    }

    @Override // android.app.backup.BackupAgentHelper
    public void addHelper(String keyPrefix, BackupHelper helper) {
        if (this.mUserId != 0 && !sEligibleForMultiUser.contains(keyPrefix)) {
            return;
        }
        super.addHelper(keyPrefix, helper);
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x008a A[Catch: IOException -> 0x00af, TRY_LEAVE, TryCatch #0 {IOException -> 0x00af, blocks: (B:12:0x0058, B:13:0x0079, B:15:0x008a, B:18:0x0093, B:22:0x009a), top: B:28:0x0058, inners: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0058 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:33:? A[RETURN, SYNTHETIC] */
    @Override // android.app.backup.BackupAgent
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void onRestoreFile(android.os.ParcelFileDescriptor r17, long r18, int r20, java.lang.String r21, java.lang.String r22, long r23, long r25) throws java.io.IOException {
        /*
            r16 = this;
            r1 = r21
            r2 = r22
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r3 = "Restoring file domain="
            r0.append(r3)
            r0.append(r1)
            java.lang.String r3 = " path="
            r0.append(r3)
            r0.append(r2)
            java.lang.String r0 = r0.toString()
            java.lang.String r3 = "SystemBackupAgent"
            android.util.Slog.i(r3, r0)
            r0 = 0
            r4 = 0
            java.lang.String r5 = "r"
            boolean r5 = r1.equals(r5)
            java.lang.String r6 = "wallpaper"
            if (r5 == 0) goto L55
            java.lang.String r5 = "wallpaper_info.xml"
            boolean r5 = r2.equals(r5)
            if (r5 == 0) goto L44
            java.io.File r5 = new java.io.File
            java.lang.String r7 = com.android.server.backup.SystemBackupAgent.WALLPAPER_INFO
            r5.<init>(r7)
            r4 = r5
            r0 = 1
            r5 = r0
            goto L56
        L44:
            boolean r5 = r2.equals(r6)
            if (r5 == 0) goto L55
            java.io.File r5 = new java.io.File
            java.lang.String r7 = com.android.server.backup.SystemBackupAgent.WALLPAPER_IMAGE
            r5.<init>(r7)
            r4 = r5
            r0 = 1
            r5 = r0
            goto L56
        L55:
            r5 = r0
        L56:
            if (r4 != 0) goto L79
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.io.IOException -> Laf
            r0.<init>()     // Catch: java.io.IOException -> Laf
            java.lang.String r7 = "Skipping unrecognized system file: [ "
            r0.append(r7)     // Catch: java.io.IOException -> Laf
            r0.append(r1)     // Catch: java.io.IOException -> Laf
            java.lang.String r7 = " : "
            r0.append(r7)     // Catch: java.io.IOException -> Laf
            r0.append(r2)     // Catch: java.io.IOException -> Laf
            java.lang.String r7 = " ]"
            r0.append(r7)     // Catch: java.io.IOException -> Laf
            java.lang.String r0 = r0.toString()     // Catch: java.io.IOException -> Laf
            android.util.Slog.w(r3, r0)     // Catch: java.io.IOException -> Laf
        L79:
            r7 = r17
            r8 = r18
            r10 = r20
            r11 = r23
            r13 = r25
            r15 = r4
            android.app.backup.FullBackup.restoreFile(r7, r8, r10, r11, r13, r15)     // Catch: java.io.IOException -> Laf
            if (r5 == 0) goto Lae
        L8a:
            android.os.IBinder r0 = android.os.ServiceManager.getService(r6)     // Catch: java.io.IOException -> Laf
            android.app.IWallpaperManager r0 = (android.app.IWallpaperManager) r0     // Catch: java.io.IOException -> Laf
            r6 = r0
            if (r6 == 0) goto Lae
            r6.settingsRestored()     // Catch: android.os.RemoteException -> L97 java.io.IOException -> Laf
            goto Lae
        L97:
            r0 = move-exception
            r7 = r0
            r0 = r7
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch: java.io.IOException -> Laf
            r7.<init>()     // Catch: java.io.IOException -> Laf
            java.lang.String r8 = "Couldn't restore settings\n"
            r7.append(r8)     // Catch: java.io.IOException -> Laf
            r7.append(r0)     // Catch: java.io.IOException -> Laf
            java.lang.String r7 = r7.toString()     // Catch: java.io.IOException -> Laf
            android.util.Slog.e(r3, r7)     // Catch: java.io.IOException -> Laf
        Lae:
            goto Lc6
        Laf:
            r0 = move-exception
            if (r5 == 0) goto Lc6
            java.io.File r3 = new java.io.File
            java.lang.String r6 = com.android.server.backup.SystemBackupAgent.WALLPAPER_IMAGE
            r3.<init>(r6)
            r3.delete()
            java.io.File r3 = new java.io.File
            java.lang.String r6 = com.android.server.backup.SystemBackupAgent.WALLPAPER_INFO
            r3.<init>(r6)
            r3.delete()
        Lc6:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.SystemBackupAgent.onRestoreFile(android.os.ParcelFileDescriptor, long, int, java.lang.String, java.lang.String, long, long):void");
    }
}
