package com.android.server.backup;

import android.app.backup.BlobBackupHelper;
import android.content.pm.IShortcutService;
import android.os.ServiceManager;
import android.util.Slog;

/* loaded from: classes3.dex */
public class ShortcutBackupHelper extends BlobBackupHelper {
    private static final int BLOB_VERSION = 1;
    private static final String KEY_USER_FILE = "shortcutuser.xml";
    private static final String TAG = "ShortcutBackupAgent";

    public ShortcutBackupHelper() {
        super(1, KEY_USER_FILE);
    }

    private IShortcutService getShortcutService() {
        return IShortcutService.Stub.asInterface(ServiceManager.getService("shortcut"));
    }

    @Override // android.app.backup.BlobBackupHelper
    protected byte[] getBackupPayload(String key) {
        if (((key.hashCode() == -792920646 && key.equals(KEY_USER_FILE)) ? (char) 0 : (char) 65535) == 0) {
            try {
                return getShortcutService().getBackupPayload(0);
            } catch (Exception e) {
                Slog.wtf(TAG, "Backup failed", e);
                return null;
            }
        }
        Slog.w(TAG, "Unknown key: " + key);
        return null;
    }

    @Override // android.app.backup.BlobBackupHelper
    protected void applyRestoredPayload(String key, byte[] payload) {
        if (((key.hashCode() == -792920646 && key.equals(KEY_USER_FILE)) ? (char) 0 : (char) 65535) == 0) {
            try {
                getShortcutService().applyRestore(payload, 0);
                return;
            } catch (Exception e) {
                Slog.wtf(TAG, "Restore failed", e);
                return;
            }
        }
        Slog.w(TAG, "Unknown key: " + key);
    }
}
