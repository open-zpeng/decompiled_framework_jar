package com.android.server.backup;

import android.app.backup.BlobBackupHelper;

/* loaded from: classes3.dex */
public class PreferredActivityBackupHelper extends BlobBackupHelper {
    private static final boolean DEBUG = false;
    private static final String KEY_DEFAULT_APPS = "default-apps";
    private static final String KEY_INTENT_VERIFICATION = "intent-verification";
    private static final String KEY_PREFERRED = "preferred-activity";
    private static final int STATE_VERSION = 3;
    private static final String TAG = "PreferredBackup";

    public PreferredActivityBackupHelper() {
        super(3, KEY_PREFERRED, KEY_DEFAULT_APPS, KEY_INTENT_VERIFICATION);
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x003e  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0061 A[Catch: Exception -> 0x0066, TRY_LEAVE, TryCatch #0 {Exception -> 0x0066, blocks: (B:3:0x0007, B:22:0x0042, B:23:0x0057, B:25:0x005c, B:27:0x0061, B:10:0x001e, B:13:0x0029, B:16:0x0033), top: B:33:0x0007 }] */
    @Override // android.app.backup.BlobBackupHelper
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected byte[] getBackupPayload(java.lang.String r9) {
        /*
            r8 = this;
            java.lang.String r0 = "PreferredBackup"
            android.content.pm.IPackageManager r1 = android.app.AppGlobals.getPackageManager()
            r2 = -1
            int r3 = r9.hashCode()     // Catch: java.lang.Exception -> L66
            r4 = -696985986(0xffffffffd674d67e, float:-6.730052E13)
            r5 = 2
            r6 = 1
            r7 = 0
            if (r3 == r4) goto L33
            r4 = -429170260(0xffffffffe66b61ac, float:-2.7788946E23)
            if (r3 == r4) goto L29
            r4 = 1336142555(0x4fa3eadb, float:5.5001554E9)
            if (r3 == r4) goto L1e
        L1d:
            goto L3c
        L1e:
            java.lang.String r3 = "preferred-activity"
            boolean r3 = r9.equals(r3)     // Catch: java.lang.Exception -> L66
            if (r3 == 0) goto L1d
            r2 = r7
            goto L3c
        L29:
            java.lang.String r3 = "intent-verification"
            boolean r3 = r9.equals(r3)     // Catch: java.lang.Exception -> L66
            if (r3 == 0) goto L1d
            r2 = r5
            goto L3c
        L33:
            java.lang.String r3 = "default-apps"
            boolean r3 = r9.equals(r3)     // Catch: java.lang.Exception -> L66
            if (r3 == 0) goto L1d
            r2 = r6
        L3c:
            if (r2 == 0) goto L61
            if (r2 == r6) goto L5c
            if (r2 == r5) goto L57
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L66
            r2.<init>()     // Catch: java.lang.Exception -> L66
            java.lang.String r3 = "Unexpected backup key "
            r2.append(r3)     // Catch: java.lang.Exception -> L66
            r2.append(r9)     // Catch: java.lang.Exception -> L66
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Exception -> L66
            android.util.Slog.w(r0, r2)     // Catch: java.lang.Exception -> L66
            goto L7b
        L57:
            byte[] r0 = r1.getIntentFilterVerificationBackup(r7)     // Catch: java.lang.Exception -> L66
            return r0
        L5c:
            byte[] r0 = r1.getDefaultAppsBackup(r7)     // Catch: java.lang.Exception -> L66
            return r0
        L61:
            byte[] r0 = r1.getPreferredActivityBackup(r7)     // Catch: java.lang.Exception -> L66
            return r0
        L66:
            r2 = move-exception
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Unable to store payload "
            r3.append(r4)
            r3.append(r9)
            java.lang.String r3 = r3.toString()
            android.util.Slog.e(r0, r3)
        L7b:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.PreferredActivityBackupHelper.getBackupPayload(java.lang.String):byte[]");
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x003e  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x005f A[Catch: Exception -> 0x0064, TRY_LEAVE, TryCatch #0 {Exception -> 0x0064, blocks: (B:3:0x0007, B:22:0x0042, B:23:0x0057, B:24:0x005b, B:25:0x005f, B:10:0x001e, B:13:0x0029, B:16:0x0033), top: B:30:0x0007 }] */
    @Override // android.app.backup.BlobBackupHelper
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void applyRestoredPayload(java.lang.String r9, byte[] r10) {
        /*
            r8 = this;
            java.lang.String r0 = "PreferredBackup"
            android.content.pm.IPackageManager r1 = android.app.AppGlobals.getPackageManager()
            r2 = -1
            int r3 = r9.hashCode()     // Catch: java.lang.Exception -> L64
            r4 = -696985986(0xffffffffd674d67e, float:-6.730052E13)
            r5 = 2
            r6 = 1
            r7 = 0
            if (r3 == r4) goto L33
            r4 = -429170260(0xffffffffe66b61ac, float:-2.7788946E23)
            if (r3 == r4) goto L29
            r4 = 1336142555(0x4fa3eadb, float:5.5001554E9)
            if (r3 == r4) goto L1e
        L1d:
            goto L3c
        L1e:
            java.lang.String r3 = "preferred-activity"
            boolean r3 = r9.equals(r3)     // Catch: java.lang.Exception -> L64
            if (r3 == 0) goto L1d
            r2 = r7
            goto L3c
        L29:
            java.lang.String r3 = "intent-verification"
            boolean r3 = r9.equals(r3)     // Catch: java.lang.Exception -> L64
            if (r3 == 0) goto L1d
            r2 = r5
            goto L3c
        L33:
            java.lang.String r3 = "default-apps"
            boolean r3 = r9.equals(r3)     // Catch: java.lang.Exception -> L64
            if (r3 == 0) goto L1d
            r2 = r6
        L3c:
            if (r2 == 0) goto L5f
            if (r2 == r6) goto L5b
            if (r2 == r5) goto L57
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L64
            r2.<init>()     // Catch: java.lang.Exception -> L64
            java.lang.String r3 = "Unexpected restore key "
            r2.append(r3)     // Catch: java.lang.Exception -> L64
            r2.append(r9)     // Catch: java.lang.Exception -> L64
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Exception -> L64
            android.util.Slog.w(r0, r2)     // Catch: java.lang.Exception -> L64
            goto L63
        L57:
            r1.restoreIntentFilterVerification(r10, r7)     // Catch: java.lang.Exception -> L64
            goto L63
        L5b:
            r1.restoreDefaultApps(r10, r7)     // Catch: java.lang.Exception -> L64
            goto L63
        L5f:
            r1.restorePreferredActivities(r10, r7)     // Catch: java.lang.Exception -> L64
        L63:
            goto L79
        L64:
            r2 = move-exception
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Unable to restore key "
            r3.append(r4)
            r3.append(r9)
            java.lang.String r3 = r3.toString()
            android.util.Slog.w(r0, r3)
        L79:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.PreferredActivityBackupHelper.applyRestoredPayload(java.lang.String, byte[]):void");
    }
}
