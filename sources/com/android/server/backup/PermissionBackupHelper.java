package com.android.server.backup;

import android.app.backup.BlobBackupHelper;
/* loaded from: classes3.dex */
public class PermissionBackupHelper extends BlobBackupHelper {
    private static final boolean DEBUG = false;
    private static final String KEY_PERMISSIONS = "permissions";
    private static final int STATE_VERSION = 1;
    private static final String TAG = "PermissionBackup";

    public PermissionBackupHelper() {
        super(1, "permissions");
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x001b, code lost:
        android.util.Slog.w(com.android.server.backup.PermissionBackupHelper.TAG, "Unexpected backup key " + r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:?, code lost:
        return null;
     */
    @Override // android.app.backup.BlobBackupHelper
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected byte[] getBackupPayload(java.lang.String r6) {
        /*
            r5 = this;
            android.content.pm.IPackageManager r0 = android.app.AppGlobals.getPackageManager()
            r1 = -1
            int r2 = r6.hashCode()     // Catch: java.lang.Exception -> L37
            r3 = 1133704324(0x4392f484, float:293.91028)
            r4 = 0
            if (r2 == r3) goto L10
            goto L19
        L10:
            java.lang.String r2 = "permissions"
            boolean r2 = r6.equals(r2)     // Catch: java.lang.Exception -> L37
            if (r2 == 0) goto L19
            r1 = r4
        L19:
            if (r1 == 0) goto L32
            java.lang.String r1 = "PermissionBackup"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L37
            r2.<init>()     // Catch: java.lang.Exception -> L37
            java.lang.String r3 = "Unexpected backup key "
            r2.append(r3)     // Catch: java.lang.Exception -> L37
            r2.append(r6)     // Catch: java.lang.Exception -> L37
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Exception -> L37
            android.util.Slog.w(r1, r2)     // Catch: java.lang.Exception -> L37
            goto L4e
        L32:
            byte[] r1 = r0.getPermissionGrantBackup(r4)     // Catch: java.lang.Exception -> L37
            return r1
        L37:
            r1 = move-exception
            java.lang.String r2 = "PermissionBackup"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Unable to store payload "
            r3.append(r4)
            r3.append(r6)
            java.lang.String r3 = r3.toString()
            android.util.Slog.e(r2, r3)
        L4e:
            r1 = 0
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.PermissionBackupHelper.getBackupPayload(java.lang.String):byte[]");
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x001b, code lost:
        android.util.Slog.w(com.android.server.backup.PermissionBackupHelper.TAG, "Unexpected restore key " + r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:?, code lost:
        return;
     */
    @Override // android.app.backup.BlobBackupHelper
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void applyRestoredPayload(java.lang.String r6, byte[] r7) {
        /*
            r5 = this;
            android.content.pm.IPackageManager r0 = android.app.AppGlobals.getPackageManager()
            r1 = -1
            int r2 = r6.hashCode()     // Catch: java.lang.Exception -> L37
            r3 = 1133704324(0x4392f484, float:293.91028)
            r4 = 0
            if (r2 == r3) goto L10
            goto L19
        L10:
            java.lang.String r2 = "permissions"
            boolean r2 = r6.equals(r2)     // Catch: java.lang.Exception -> L37
            if (r2 == 0) goto L19
            r1 = r4
        L19:
            if (r1 == 0) goto L32
            java.lang.String r1 = "PermissionBackup"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L37
            r2.<init>()     // Catch: java.lang.Exception -> L37
            java.lang.String r3 = "Unexpected restore key "
            r2.append(r3)     // Catch: java.lang.Exception -> L37
            r2.append(r6)     // Catch: java.lang.Exception -> L37
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Exception -> L37
            android.util.Slog.w(r1, r2)     // Catch: java.lang.Exception -> L37
            goto L36
        L32:
            r0.restorePermissionGrants(r7, r4)     // Catch: java.lang.Exception -> L37
        L36:
            goto L4e
        L37:
            r1 = move-exception
            java.lang.String r2 = "PermissionBackup"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Unable to restore key "
            r3.append(r4)
            r3.append(r6)
            java.lang.String r3 = r3.toString()
            android.util.Slog.w(r2, r3)
        L4e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.PermissionBackupHelper.applyRestoredPayload(java.lang.String, byte[]):void");
    }
}
