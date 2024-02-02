package android.os;

import android.util.Slog;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
/* loaded from: classes2.dex */
public class SELinux {
    private static final int SELINUX_ANDROID_RESTORECON_DATADATA = 16;
    private static final int SELINUX_ANDROID_RESTORECON_FORCE = 8;
    private static final int SELINUX_ANDROID_RESTORECON_NOCHANGE = 1;
    private static final int SELINUX_ANDROID_RESTORECON_RECURSE = 4;
    private static final int SELINUX_ANDROID_RESTORECON_VERBOSE = 2;
    private static final String TAG = "SELinux";

    private protected static final native boolean checkSELinuxAccess(String str, String str2, String str3, String str4);

    private protected static final native String getContext();

    private protected static final native String getFileContext(String str);

    public static final native String getPeerContext(FileDescriptor fileDescriptor);

    private protected static final native String getPidContext(int i);

    private protected static final native boolean isSELinuxEnabled();

    private protected static final native boolean isSELinuxEnforced();

    private static native boolean native_restorecon(String str, int i);

    public static final native boolean setFSCreateContext(String str);

    public static final native boolean setFileContext(String str, String str2);

    public static synchronized boolean restorecon(String pathname) throws NullPointerException {
        if (pathname == null) {
            throw new NullPointerException();
        }
        return native_restorecon(pathname, 0);
    }

    public static synchronized boolean restorecon(File file) throws NullPointerException {
        try {
            return native_restorecon(file.getCanonicalPath(), 0);
        } catch (IOException e) {
            Slog.e(TAG, "Error getting canonical path. Restorecon failed for " + file.getPath(), e);
            return false;
        }
    }

    private protected static boolean restoreconRecursive(File file) {
        try {
            return native_restorecon(file.getCanonicalPath(), 4);
        } catch (IOException e) {
            Slog.e(TAG, "Error getting canonical path. Restorecon failed for " + file.getPath(), e);
            return false;
        }
    }
}
