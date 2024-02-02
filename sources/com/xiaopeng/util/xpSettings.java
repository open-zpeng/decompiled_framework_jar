package com.xiaopeng.util;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
/* loaded from: classes3.dex */
public class xpSettings {
    public static final int TYPE_GLOBAL = 3;
    public static final int TYPE_SECURE = 2;
    public static final int TYPE_SYSTEM = 1;

    /* loaded from: classes3.dex */
    public static final class Global {
    }

    /* loaded from: classes3.dex */
    public static final class Secure {
        public static final String KEY_INPUT_METHOD_SHOWN = "key_input_method_shown";
    }

    /* loaded from: classes3.dex */
    public static final class System {
    }

    public static int getInt(Context context, int type, String key, int defaultValue) {
        try {
            ContentResolver resolver = context.getContentResolver();
            switch (type) {
                case 1:
                    int value = Settings.System.getInt(resolver, key, defaultValue);
                    return value;
                case 2:
                    int value2 = Settings.Secure.getInt(resolver, key, defaultValue);
                    return value2;
                case 3:
                    int value3 = Settings.Global.getInt(resolver, key, defaultValue);
                    return value3;
                default:
                    return defaultValue;
            }
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static boolean putInt(Context context, int type, String key, int value) {
        try {
            ContentResolver resolver = context.getContentResolver();
            switch (type) {
                case 1:
                    boolean ret = Settings.System.putInt(resolver, key, value);
                    return ret;
                case 2:
                    boolean ret2 = Settings.Secure.putInt(resolver, key, value);
                    return ret2;
                case 3:
                    boolean ret3 = Settings.Global.putInt(resolver, key, value);
                    return ret3;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
