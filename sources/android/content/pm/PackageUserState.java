package android.content.pm;

import android.annotation.UnsupportedAppUsage;
import android.os.BaseBundle;
import android.os.PersistableBundle;
import android.util.ArraySet;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import java.util.Arrays;
import java.util.Objects;

/* loaded from: classes.dex */
public class PackageUserState {
    private static final boolean DEBUG = false;
    private static final String LOG_TAG = "PackageUserState";
    public int appLinkGeneration;
    public int categoryHint;
    public long ceDataInode;
    public SuspendDialogInfo dialogInfo;
    public ArraySet<String> disabledComponents;
    public int distractionFlags;
    public int domainVerificationStatus;
    public int enabled;
    public ArraySet<String> enabledComponents;
    public String harmfulAppWarning;
    public boolean hidden;
    public int installReason;
    public boolean installed;
    public boolean instantApp;
    public String lastDisableAppCaller;
    public boolean notLaunched;
    public String[] overlayPaths;
    public boolean stopped;
    public boolean suspended;
    public PersistableBundle suspendedAppExtras;
    public PersistableBundle suspendedLauncherExtras;
    public String suspendingPackage;
    public boolean virtualPreload;

    @UnsupportedAppUsage
    public PackageUserState() {
        this.categoryHint = -1;
        this.installed = true;
        this.hidden = false;
        this.suspended = false;
        this.enabled = 0;
        this.domainVerificationStatus = 0;
        this.installReason = 0;
    }

    @VisibleForTesting
    public PackageUserState(PackageUserState o) {
        this.categoryHint = -1;
        this.ceDataInode = o.ceDataInode;
        this.installed = o.installed;
        this.stopped = o.stopped;
        this.notLaunched = o.notLaunched;
        this.hidden = o.hidden;
        this.distractionFlags = o.distractionFlags;
        this.suspended = o.suspended;
        this.suspendingPackage = o.suspendingPackage;
        this.dialogInfo = o.dialogInfo;
        this.suspendedAppExtras = o.suspendedAppExtras;
        this.suspendedLauncherExtras = o.suspendedLauncherExtras;
        this.instantApp = o.instantApp;
        this.virtualPreload = o.virtualPreload;
        this.enabled = o.enabled;
        this.lastDisableAppCaller = o.lastDisableAppCaller;
        this.domainVerificationStatus = o.domainVerificationStatus;
        this.appLinkGeneration = o.appLinkGeneration;
        this.categoryHint = o.categoryHint;
        this.installReason = o.installReason;
        this.disabledComponents = ArrayUtils.cloneOrNull(o.disabledComponents);
        this.enabledComponents = ArrayUtils.cloneOrNull(o.enabledComponents);
        String[] strArr = o.overlayPaths;
        this.overlayPaths = strArr == null ? null : (String[]) Arrays.copyOf(strArr, strArr.length);
        this.harmfulAppWarning = o.harmfulAppWarning;
    }

    public boolean isAvailable(int flags) {
        boolean matchAnyUser = (4194304 & flags) != 0;
        boolean matchUninstalled = (flags & 8192) != 0;
        if (matchAnyUser) {
            return true;
        }
        return this.installed && (!this.hidden || matchUninstalled);
    }

    public boolean isMatch(ComponentInfo componentInfo, int flags) {
        boolean isSystemApp = componentInfo.applicationInfo.isSystemApp();
        boolean z = true;
        boolean matchUninstalled = (4202496 & flags) != 0;
        if (!isAvailable(flags) && (!isSystemApp || !matchUninstalled)) {
            return reportIfDebug(false, flags);
        }
        if (isEnabled(componentInfo, flags)) {
            if ((1048576 & flags) != 0 && !isSystemApp) {
                return reportIfDebug(false, flags);
            }
            boolean matchesUnaware = ((262144 & flags) == 0 || componentInfo.directBootAware) ? false : true;
            boolean matchesAware = (524288 & flags) != 0 && componentInfo.directBootAware;
            if (!matchesUnaware && !matchesAware) {
                z = false;
            }
            return reportIfDebug(z, flags);
        }
        return reportIfDebug(false, flags);
    }

    private boolean reportIfDebug(boolean result, int flags) {
        return result;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x002e A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x002f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean isEnabled(android.content.pm.ComponentInfo r5, int r6) {
        /*
            r4 = this;
            r0 = r6 & 512(0x200, float:7.17E-43)
            r1 = 1
            if (r0 == 0) goto L6
            return r1
        L6:
            int r0 = r4.enabled
            r2 = 0
            if (r0 == 0) goto L1d
            r3 = 2
            if (r0 == r3) goto L1c
            r3 = 3
            if (r0 == r3) goto L1c
            r3 = 4
            if (r0 == r3) goto L15
            goto L24
        L15:
            r0 = 32768(0x8000, float:4.5918E-41)
            r0 = r0 & r6
            if (r0 != 0) goto L1d
            return r2
        L1c:
            return r2
        L1d:
            android.content.pm.ApplicationInfo r0 = r5.applicationInfo
            boolean r0 = r0.enabled
            if (r0 != 0) goto L24
            return r2
        L24:
            android.util.ArraySet<java.lang.String> r0 = r4.enabledComponents
            java.lang.String r3 = r5.name
            boolean r0 = com.android.internal.util.ArrayUtils.contains(r0, r3)
            if (r0 == 0) goto L2f
            return r1
        L2f:
            android.util.ArraySet<java.lang.String> r0 = r4.disabledComponents
            java.lang.String r1 = r5.name
            boolean r0 = com.android.internal.util.ArrayUtils.contains(r0, r1)
            if (r0 == 0) goto L3a
            return r2
        L3a:
            boolean r0 = r5.enabled
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageUserState.isEnabled(android.content.pm.ComponentInfo, int):boolean");
    }

    public final boolean equals(Object obj) {
        boolean z;
        String str;
        String str2;
        String str3;
        if (obj instanceof PackageUserState) {
            PackageUserState oldState = (PackageUserState) obj;
            if (this.ceDataInode == oldState.ceDataInode && this.installed == oldState.installed && this.stopped == oldState.stopped && this.notLaunched == oldState.notLaunched && this.hidden == oldState.hidden && this.distractionFlags == oldState.distractionFlags && (z = this.suspended) == oldState.suspended) {
                if ((!z || ((str3 = this.suspendingPackage) != null && str3.equals(oldState.suspendingPackage) && Objects.equals(this.dialogInfo, oldState.dialogInfo) && BaseBundle.kindofEquals(this.suspendedAppExtras, oldState.suspendedAppExtras) && BaseBundle.kindofEquals(this.suspendedLauncherExtras, oldState.suspendedLauncherExtras))) && this.instantApp == oldState.instantApp && this.virtualPreload == oldState.virtualPreload && this.enabled == oldState.enabled) {
                    if ((this.lastDisableAppCaller != null || oldState.lastDisableAppCaller == null) && (((str = this.lastDisableAppCaller) == null || str.equals(oldState.lastDisableAppCaller)) && this.domainVerificationStatus == oldState.domainVerificationStatus && this.appLinkGeneration == oldState.appLinkGeneration && this.categoryHint == oldState.categoryHint && this.installReason == oldState.installReason)) {
                        if ((this.disabledComponents != null || oldState.disabledComponents == null) && (this.disabledComponents == null || oldState.disabledComponents != null)) {
                            ArraySet<String> arraySet = this.disabledComponents;
                            if (arraySet != null) {
                                if (arraySet.size() != oldState.disabledComponents.size()) {
                                    return false;
                                }
                                for (int i = this.disabledComponents.size() - 1; i >= 0; i--) {
                                    if (!oldState.disabledComponents.contains(this.disabledComponents.valueAt(i))) {
                                        return false;
                                    }
                                }
                            }
                            if ((this.enabledComponents != null || oldState.enabledComponents == null) && (this.enabledComponents == null || oldState.enabledComponents != null)) {
                                ArraySet<String> arraySet2 = this.enabledComponents;
                                if (arraySet2 != null) {
                                    if (arraySet2.size() != oldState.enabledComponents.size()) {
                                        return false;
                                    }
                                    for (int i2 = this.enabledComponents.size() - 1; i2 >= 0; i2--) {
                                        if (!oldState.enabledComponents.contains(this.enabledComponents.valueAt(i2))) {
                                            return false;
                                        }
                                    }
                                }
                                return (this.harmfulAppWarning != null || oldState.harmfulAppWarning == null) && ((str2 = this.harmfulAppWarning) == null || str2.equals(oldState.harmfulAppWarning));
                            }
                            return false;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }
}
