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
            if (type == 1) {
                int value = Settings.System.getInt(resolver, key, defaultValue);
                return value;
            } else if (type == 2) {
                int value2 = Settings.Secure.getInt(resolver, key, defaultValue);
                return value2;
            } else if (type != 3) {
                return defaultValue;
            } else {
                int value3 = Settings.Global.getInt(resolver, key, defaultValue);
                return value3;
            }
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static boolean putInt(Context context, int type, String key, int value) {
        try {
            ContentResolver resolver = context.getContentResolver();
            if (type == 1) {
                boolean ret = Settings.System.putInt(resolver, key, value);
                return ret;
            } else if (type == 2) {
                boolean ret2 = Settings.Secure.putInt(resolver, key, value);
                return ret2;
            } else if (type != 3) {
                return false;
            } else {
                boolean ret3 = Settings.Global.putInt(resolver, key, value);
                return ret3;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
