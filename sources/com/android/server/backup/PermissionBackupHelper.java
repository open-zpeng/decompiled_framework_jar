package com.android.server.backup;

import android.app.backup.BlobBackupHelper;
import android.os.UserHandle;
import android.permission.PermissionManagerInternal;
import android.util.Slog;
import com.android.server.LocalServices;

/* loaded from: classes3.dex */
public class PermissionBackupHelper extends BlobBackupHelper {
    private static final boolean DEBUG = false;
    private static final String KEY_PERMISSIONS = "permissions";
    private static final int STATE_VERSION = 1;
    private static final String TAG = "PermissionBackup";
    private final PermissionManagerInternal mPermissionManager;
    private final UserHandle mUser;

    public PermissionBackupHelper(int userId) {
        super(1, KEY_PERMISSIONS);
        this.mUser = UserHandle.of(userId);
        this.mPermissionManager = (PermissionManagerInternal) LocalServices.getService(PermissionManagerInternal.class);
    }

    @Override // android.app.backup.BlobBackupHelper
    protected byte[] getBackupPayload(String key) {
        char c = 65535;
        try {
            if (key.hashCode() == 1133704324 && key.equals(KEY_PERMISSIONS)) {
                c = 0;
            }
            if (c == 0) {
                return this.mPermissionManager.backupRuntimePermissions(this.mUser);
            }
            Slog.w(TAG, "Unexpected backup key " + key);
            return null;
        } catch (Exception e) {
            Slog.e(TAG, "Unable to store payload " + key);
            return null;
        }
    }

    @Override // android.app.backup.BlobBackupHelper
    protected void applyRestoredPayload(String key, byte[] payload) {
        char c = 65535;
        try {
            if (key.hashCode() == 1133704324 && key.equals(KEY_PERMISSIONS)) {
                c = 0;
            }
            if (c == 0) {
                this.mPermissionManager.restoreRuntimePermissions(payload, this.mUser);
                return;
            }
            Slog.w(TAG, "Unexpected restore key " + key);
        } catch (Exception e) {
            Slog.w(TAG, "Unable to restore key " + key);
        }
    }
}
