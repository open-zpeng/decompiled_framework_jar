package com.xiaopeng.util;

import android.app.IActivityManager;
import android.content.Context;
import android.os.IBinder;
import android.os.ServiceManager;
import android.os.StrictMode;
import android.text.TextUtils;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
/* loaded from: classes3.dex */
public class FeatureFactory {
    private static final boolean DEBUG = false;
    private static final String TAG = "FeatureFactory";
    private static Object sLock = new Object();
    private static volatile boolean sDefaultLoadCompleted = false;
    private static volatile boolean sProjectLoadCompleted = false;
    private static volatile Properties defaultProperties = new Properties();
    private static volatile Properties projectProperties = new Properties();

    /* loaded from: classes3.dex */
    public interface IFeatureInterface {
        boolean getBoolean(String str, boolean z);

        double getDouble(String str, double d);

        int getInt(String str, int i);

        long getLong(String str, long j);

        String getString(String str, String str2);
    }

    static {
        loadProperties();
    }

    private static void loadProperties() {
        int oldMask = StrictMode.allowThreadDiskReadsMask();
        synchronized (sLock) {
            if (!sDefaultLoadCompleted) {
                try {
                    InputStream is = new FileInputStream("/system/etc/xui_default.properties");
                    defaultProperties.load(is);
                    is.close();
                    sDefaultLoadCompleted = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    sDefaultLoadCompleted = false;
                }
            }
            if (!sProjectLoadCompleted) {
                try {
                    InputStream is2 = new FileInputStream("/system/etc/xui_project.properties");
                    projectProperties.load(is2);
                    is2.close();
                    sProjectLoadCompleted = true;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    sProjectLoadCompleted = false;
                }
            }
        }
        StrictMode.setThreadPolicyMask(oldMask);
    }

    public static String get(String key, String defaultValue) {
        synchronized (sLock) {
            if (!sDefaultLoadCompleted || !sProjectLoadCompleted) {
                loadProperties();
            }
            if (!TextUtils.isEmpty(key) && projectProperties != null && projectProperties.containsKey(key)) {
                return projectProperties.getProperty(key);
            } else if (!TextUtils.isEmpty(key) && defaultProperties != null && defaultProperties.containsKey(key)) {
                return defaultProperties.getProperty(key);
            } else {
                return defaultValue;
            }
        }
    }

    /* loaded from: classes3.dex */
    public static class DefaultFeatureInterface implements IFeatureInterface {
        private static final int OPTION_ERROR = 3;
        private static final int OPTION_LOCAL = 2;
        private static final int OPTION_NONE = 0;
        private static final int OPTION_SERVER = 1;

        private String getOption(String key, String defaultValue) {
            try {
                IBinder b = ServiceManager.getService(Context.ACTIVITY_SERVICE);
                IActivityManager am = IActivityManager.Stub.asInterface(b);
                String value = am.getOption(key, defaultValue);
                return value;
            } catch (Exception e) {
                try {
                    String value2 = FeatureFactory.get(key, defaultValue);
                    return value2;
                } catch (Exception e2) {
                    return defaultValue;
                }
            }
        }

        @Override // com.xiaopeng.util.FeatureFactory.IFeatureInterface
        public int getInt(String key, int defaultValue) {
            String value = getOption(key, String.valueOf(defaultValue));
            return xpTextUtils.toInteger(value, Integer.valueOf(defaultValue)).intValue();
        }

        @Override // com.xiaopeng.util.FeatureFactory.IFeatureInterface
        public long getLong(String key, long defaultValue) {
            String value = getOption(key, String.valueOf(defaultValue));
            return xpTextUtils.toLong(value, Long.valueOf(defaultValue)).longValue();
        }

        @Override // com.xiaopeng.util.FeatureFactory.IFeatureInterface
        public double getDouble(String key, double defaultValue) {
            String value = getOption(key, String.valueOf(defaultValue));
            return xpTextUtils.toDouble(value, Double.valueOf(defaultValue)).doubleValue();
        }

        @Override // com.xiaopeng.util.FeatureFactory.IFeatureInterface
        public String getString(String key, String defaultValue) {
            String value = getOption(key, String.valueOf(defaultValue));
            return xpTextUtils.toString(value);
        }

        @Override // com.xiaopeng.util.FeatureFactory.IFeatureInterface
        public boolean getBoolean(String key, boolean defaultValue) {
            String value = getOption(key, String.valueOf(defaultValue));
            return xpTextUtils.toBoolean(value, Boolean.valueOf(defaultValue)).booleanValue();
        }
    }
}
