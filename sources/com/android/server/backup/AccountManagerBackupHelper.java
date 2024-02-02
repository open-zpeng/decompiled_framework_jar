package com.android.server.backup;

import android.app.backup.BlobBackupHelper;
/* loaded from: classes3.dex */
public class AccountManagerBackupHelper extends BlobBackupHelper {
    private static final boolean DEBUG = false;
    private static final String KEY_ACCOUNT_ACCESS_GRANTS = "account_access_grants";
    private static final int STATE_VERSION = 1;
    private static final String TAG = "AccountsBackup";

    public AccountManagerBackupHelper() {
        super(1, KEY_ACCOUNT_ACCESS_GRANTS);
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x001f, code lost:
        android.util.Slog.w(com.android.server.backup.AccountManagerBackupHelper.TAG, "Unexpected backup key " + r7);
     */
    @Override // android.app.backup.BlobBackupHelper
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected byte[] getBackupPayload(java.lang.String r7) {
        /*
            r6 = this;
            java.lang.Class<android.accounts.AccountManagerInternal> r0 = android.accounts.AccountManagerInternal.class
            java.lang.Object r0 = com.android.server.LocalServices.getService(r0)
            android.accounts.AccountManagerInternal r0 = (android.accounts.AccountManagerInternal) r0
            r1 = -1
            r2 = 0
            int r3 = r7.hashCode()     // Catch: java.lang.Exception -> L3b
            r4 = 1544100736(0x5c091b80, float:1.5436923E17)
            if (r3 == r4) goto L14
            goto L1d
        L14:
            java.lang.String r3 = "account_access_grants"
            boolean r3 = r7.equals(r3)     // Catch: java.lang.Exception -> L3b
            if (r3 == 0) goto L1d
            r1 = r2
        L1d:
            if (r1 == 0) goto L36
            java.lang.String r1 = "AccountsBackup"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L3b
            r3.<init>()     // Catch: java.lang.Exception -> L3b
            java.lang.String r4 = "Unexpected backup key "
            r3.append(r4)     // Catch: java.lang.Exception -> L3b
            r3.append(r7)     // Catch: java.lang.Exception -> L3b
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Exception -> L3b
            android.util.Slog.w(r1, r3)     // Catch: java.lang.Exception -> L3b
            goto L52
        L36:
            byte[] r1 = r0.backupAccountAccessPermissions(r2)     // Catch: java.lang.Exception -> L3b
            return r1
        L3b:
            r1 = move-exception
            java.lang.String r3 = "AccountsBackup"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "Unable to store payload "
            r4.append(r5)
            r4.append(r7)
            java.lang.String r4 = r4.toString()
            android.util.Slog.e(r3, r4)
        L52:
            byte[] r1 = new byte[r2]
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.AccountManagerBackupHelper.getBackupPayload(java.lang.String):byte[]");
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x001f, code lost:
        android.util.Slog.w(com.android.server.backup.AccountManagerBackupHelper.TAG, "Unexpected restore key " + r6);
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
            java.lang.Class<android.accounts.AccountManagerInternal> r0 = android.accounts.AccountManagerInternal.class
            java.lang.Object r0 = com.android.server.LocalServices.getService(r0)
            android.accounts.AccountManagerInternal r0 = (android.accounts.AccountManagerInternal) r0
            r1 = -1
            int r2 = r6.hashCode()     // Catch: java.lang.Exception -> L3b
            r3 = 1544100736(0x5c091b80, float:1.5436923E17)
            r4 = 0
            if (r2 == r3) goto L14
            goto L1d
        L14:
            java.lang.String r2 = "account_access_grants"
            boolean r2 = r6.equals(r2)     // Catch: java.lang.Exception -> L3b
            if (r2 == 0) goto L1d
            r1 = r4
        L1d:
            if (r1 == 0) goto L36
            java.lang.String r1 = "AccountsBackup"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L3b
            r2.<init>()     // Catch: java.lang.Exception -> L3b
            java.lang.String r3 = "Unexpected restore key "
            r2.append(r3)     // Catch: java.lang.Exception -> L3b
            r2.append(r6)     // Catch: java.lang.Exception -> L3b
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Exception -> L3b
            android.util.Slog.w(r1, r2)     // Catch: java.lang.Exception -> L3b
            goto L3a
        L36:
            r0.restoreAccountAccessPermissions(r7, r4)     // Catch: java.lang.Exception -> L3b
        L3a:
            goto L52
        L3b:
            r1 = move-exception
            java.lang.String r2 = "AccountsBackup"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Unable to restore key "
            r3.append(r4)
            r3.append(r6)
            java.lang.String r3 = r3.toString()
            android.util.Slog.w(r2, r3)
        L52:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.AccountManagerBackupHelper.applyRestoredPayload(java.lang.String, byte[]):void");
    }
}
