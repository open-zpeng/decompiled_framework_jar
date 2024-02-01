package android.os;

import android.annotation.SystemApi;
import android.util.MutableInt;
import com.android.internal.annotations.GuardedBy;
import java.util.ArrayList;
import java.util.HashMap;
@SystemApi
/* loaded from: classes2.dex */
public class SystemProperties {
    private protected static final int PROP_NAME_MAX = Integer.MAX_VALUE;
    public static final int PROP_VALUE_MAX = 91;
    private static final String TAG = "SystemProperties";
    private static final boolean TRACK_KEY_ACCESS = false;
    @GuardedBy("sChangeCallbacks")
    public protected static final ArrayList<Runnable> sChangeCallbacks = new ArrayList<>();
    @GuardedBy("sRoReads")
    private static final HashMap<String, MutableInt> sRoReads = null;

    public protected static native void native_add_change_callback();

    public protected static native String native_get(String str);

    public protected static native String native_get(String str, String str2);

    public protected static native boolean native_get_boolean(String str, boolean z);

    public protected static native int native_get_int(String str, int i);

    public protected static native long native_get_long(String str, long j);

    private static native void native_report_sysprop_change();

    public protected static native void native_set(String str, String str2);

    private static synchronized void onKeyAccess(String key) {
    }

    @SystemApi
    public static String get(String key) {
        return native_get(key);
    }

    @SystemApi
    public static String get(String key, String def) {
        return native_get(key, def);
    }

    @SystemApi
    public static int getInt(String key, int def) {
        return native_get_int(key, def);
    }

    @SystemApi
    public static long getLong(String key, long def) {
        return native_get_long(key, def);
    }

    @SystemApi
    public static boolean getBoolean(String key, boolean def) {
        return native_get_boolean(key, def);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void set(String key, String val) {
        if (val != null && !val.startsWith("ro.") && val.length() > 91) {
            throw new IllegalArgumentException("value of system property '" + key + "' is longer than 91 characters: " + val);
        }
        native_set(key, val);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addChangeCallback(Runnable callback) {
        synchronized (sChangeCallbacks) {
            if (sChangeCallbacks.size() == 0) {
                native_add_change_callback();
            }
            sChangeCallbacks.add(callback);
        }
    }

    private static synchronized void callChangeCallbacks() {
        synchronized (sChangeCallbacks) {
            if (sChangeCallbacks.size() == 0) {
                return;
            }
            ArrayList<Runnable> callbacks = new ArrayList<>(sChangeCallbacks);
            for (int i = 0; i < callbacks.size(); i++) {
                callbacks.get(i).run();
            }
        }
    }

    private protected static void reportSyspropChanged() {
        native_report_sysprop_change();
    }
}
