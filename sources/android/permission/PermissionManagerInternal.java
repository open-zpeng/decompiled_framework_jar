package android.permission;

import android.os.UserHandle;

/* loaded from: classes2.dex */
public abstract class PermissionManagerInternal {

    /* loaded from: classes2.dex */
    public interface OnRuntimePermissionStateChangedListener {
        void onRuntimePermissionStateChanged(String str, int i);
    }

    public abstract void addOnRuntimePermissionStateChangedListener(OnRuntimePermissionStateChangedListener onRuntimePermissionStateChangedListener);

    public abstract byte[] backupRuntimePermissions(UserHandle userHandle);

    public abstract void removeOnRuntimePermissionStateChangedListener(OnRuntimePermissionStateChangedListener onRuntimePermissionStateChangedListener);

    public abstract void restoreDelayedRuntimePermissions(String str, UserHandle userHandle);

    public abstract void restoreRuntimePermissions(byte[] bArr, UserHandle userHandle);
}
