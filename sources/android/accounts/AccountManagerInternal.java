package android.accounts;

import android.os.RemoteCallback;
/* loaded from: classes.dex */
public abstract class AccountManagerInternal {

    /* loaded from: classes.dex */
    public interface OnAppPermissionChangeListener {
        synchronized void onAppPermissionChanged(Account account, int i);
    }

    public abstract synchronized void addOnAppPermissionChangeListener(OnAppPermissionChangeListener onAppPermissionChangeListener);

    public abstract synchronized byte[] backupAccountAccessPermissions(int i);

    public abstract synchronized boolean hasAccountAccess(Account account, int i);

    public abstract synchronized void requestAccountAccess(Account account, String str, int i, RemoteCallback remoteCallback);

    public abstract synchronized void restoreAccountAccessPermissions(byte[] bArr, int i);
}
