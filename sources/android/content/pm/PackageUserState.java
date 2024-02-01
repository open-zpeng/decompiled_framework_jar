package android.content.pm;

import android.os.BaseBundle;
import android.os.PersistableBundle;
import android.util.ArraySet;
import com.android.internal.util.ArrayUtils;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public class PackageUserState {
    public int appLinkGeneration;
    public int categoryHint;
    public long ceDataInode;
    public String dialogMessage;
    public ArraySet<String> disabledComponents;
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

    /* JADX INFO: Access modifiers changed from: private */
    public PackageUserState() {
        this.categoryHint = -1;
        this.installed = true;
        this.hidden = false;
        this.suspended = false;
        this.enabled = 0;
        this.domainVerificationStatus = 0;
        this.installReason = 0;
    }

    public synchronized PackageUserState(PackageUserState o) {
        this.categoryHint = -1;
        this.ceDataInode = o.ceDataInode;
        this.installed = o.installed;
        this.stopped = o.stopped;
        this.notLaunched = o.notLaunched;
        this.hidden = o.hidden;
        this.suspended = o.suspended;
        this.suspendingPackage = o.suspendingPackage;
        this.dialogMessage = o.dialogMessage;
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
        this.overlayPaths = o.overlayPaths == null ? null : (String[]) Arrays.copyOf(o.overlayPaths, o.overlayPaths.length);
        this.harmfulAppWarning = o.harmfulAppWarning;
    }

    public synchronized boolean isAvailable(int flags) {
        boolean matchAnyUser = (4194304 & flags) != 0;
        boolean matchUninstalled = (flags & 8192) != 0;
        if (!matchAnyUser) {
            if (!this.installed) {
                return false;
            }
            if (this.hidden && !matchUninstalled) {
                return false;
            }
        }
        return true;
    }

    public synchronized boolean isMatch(ComponentInfo componentInfo, int flags) {
        boolean isSystemApp = componentInfo.applicationInfo.isSystemApp();
        boolean matchUninstalled = (4202496 & flags) != 0;
        if ((isAvailable(flags) || (isSystemApp && matchUninstalled)) && isEnabled(componentInfo, flags)) {
            if ((1048576 & flags) == 0 || isSystemApp) {
                boolean matchesUnaware = ((262144 & flags) == 0 || componentInfo.directBootAware) ? false : true;
                boolean matchesAware = (524288 & flags) != 0 && componentInfo.directBootAware;
                return matchesUnaware || matchesAware;
            }
            return false;
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0028 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0029  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized boolean isEnabled(android.content.pm.ComponentInfo r5, int r6) {
        /*
            r4 = this;
            r0 = r6 & 512(0x200, float:7.17E-43)
            r1 = 1
            if (r0 == 0) goto L6
            return r1
        L6:
            int r0 = r4.enabled
            r2 = 0
            if (r0 == 0) goto L17
            switch(r0) {
                case 2: goto L16;
                case 3: goto L16;
                case 4: goto Lf;
                default: goto Le;
            }
        Le:
            goto L1e
        Lf:
            r0 = 32768(0x8000, float:4.5918E-41)
            r0 = r0 & r6
            if (r0 != 0) goto L17
            return r2
        L16:
            return r2
        L17:
            android.content.pm.ApplicationInfo r0 = r5.applicationInfo
            boolean r0 = r0.enabled
            if (r0 != 0) goto L1e
            return r2
        L1e:
            android.util.ArraySet<java.lang.String> r0 = r4.enabledComponents
            java.lang.String r3 = r5.name
            boolean r0 = com.android.internal.util.ArrayUtils.contains(r0, r3)
            if (r0 == 0) goto L29
            return r1
        L29:
            android.util.ArraySet<java.lang.String> r0 = r4.disabledComponents
            java.lang.String r1 = r5.name
            boolean r0 = com.android.internal.util.ArrayUtils.contains(r0, r1)
            if (r0 == 0) goto L34
            return r2
        L34:
            boolean r0 = r5.enabled
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageUserState.isEnabled(android.content.pm.ComponentInfo, int):boolean");
    }

    public final boolean equals(Object obj) {
        if (obj instanceof PackageUserState) {
            PackageUserState oldState = (PackageUserState) obj;
            if (this.ceDataInode == oldState.ceDataInode && this.installed == oldState.installed && this.stopped == oldState.stopped && this.notLaunched == oldState.notLaunched && this.hidden == oldState.hidden && this.suspended == oldState.suspended) {
                if ((!this.suspended || (this.suspendingPackage != null && this.suspendingPackage.equals(oldState.suspendingPackage) && Objects.equals(this.dialogMessage, oldState.dialogMessage) && BaseBundle.kindofEquals(this.suspendedAppExtras, oldState.suspendedAppExtras) && BaseBundle.kindofEquals(this.suspendedLauncherExtras, oldState.suspendedLauncherExtras))) && this.instantApp == oldState.instantApp && this.virtualPreload == oldState.virtualPreload && this.enabled == oldState.enabled) {
                    if ((this.lastDisableAppCaller != null || oldState.lastDisableAppCaller == null) && ((this.lastDisableAppCaller == null || this.lastDisableAppCaller.equals(oldState.lastDisableAppCaller)) && this.domainVerificationStatus == oldState.domainVerificationStatus && this.appLinkGeneration == oldState.appLinkGeneration && this.categoryHint == oldState.categoryHint && this.installReason == oldState.installReason)) {
                        if ((this.disabledComponents != null || oldState.disabledComponents == null) && (this.disabledComponents == null || oldState.disabledComponents != null)) {
                            if (this.disabledComponents != null) {
                                if (this.disabledComponents.size() != oldState.disabledComponents.size()) {
                                    return false;
                                }
                                for (int i = this.disabledComponents.size() - 1; i >= 0; i--) {
                                    if (!oldState.disabledComponents.contains(this.disabledComponents.valueAt(i))) {
                                        return false;
                                    }
                                }
                            }
                            if ((this.enabledComponents != null || oldState.enabledComponents == null) && (this.enabledComponents == null || oldState.enabledComponents != null)) {
                                if (this.enabledComponents != null) {
                                    if (this.enabledComponents.size() != oldState.enabledComponents.size()) {
                                        return false;
                                    }
                                    for (int i2 = this.enabledComponents.size() - 1; i2 >= 0; i2--) {
                                        if (!oldState.enabledComponents.contains(this.enabledComponents.valueAt(i2))) {
                                            return false;
                                        }
                                    }
                                }
                                return (this.harmfulAppWarning != null || oldState.harmfulAppWarning == null) && (this.harmfulAppWarning == null || this.harmfulAppWarning.equals(oldState.harmfulAppWarning));
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
