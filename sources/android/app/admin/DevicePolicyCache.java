package android.app.admin;

import com.android.server.LocalServices;
/* loaded from: classes.dex */
public abstract class DevicePolicyCache {
    public abstract synchronized boolean getScreenCaptureDisabled(int i);

    protected synchronized DevicePolicyCache() {
    }

    public static synchronized DevicePolicyCache getInstance() {
        DevicePolicyManagerInternal dpmi = (DevicePolicyManagerInternal) LocalServices.getService(DevicePolicyManagerInternal.class);
        return dpmi != null ? dpmi.getDevicePolicyCache() : EmptyDevicePolicyCache.INSTANCE;
    }

    /* loaded from: classes.dex */
    private static class EmptyDevicePolicyCache extends DevicePolicyCache {
        private static final EmptyDevicePolicyCache INSTANCE = new EmptyDevicePolicyCache();

        private synchronized EmptyDevicePolicyCache() {
        }

        @Override // android.app.admin.DevicePolicyCache
        public synchronized boolean getScreenCaptureDisabled(int userHandle) {
            return false;
        }
    }
}
