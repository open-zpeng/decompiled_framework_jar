package android.app.role;

import android.annotation.SystemApi;
import android.os.UserHandle;

@SystemApi
/* loaded from: classes.dex */
public interface OnRoleHoldersChangedListener {
    void onRoleHoldersChanged(String str, UserHandle userHandle);
}
