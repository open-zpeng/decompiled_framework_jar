package com.android.server;

import android.content.ComponentName;
import android.content.pm.FeatureInfo;
import android.hardware.usb.UsbManager;
import android.net.wifi.WifiConfiguration;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.os.storage.StorageVolume;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.XmlUtils;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes3.dex */
public class SystemConfig {
    private static final int ALLOW_ALL = -1;
    private static final int ALLOW_APP_CONFIGS = 8;
    private static final int ALLOW_FEATURES = 1;
    private static final int ALLOW_HIDDENAPI_WHITELISTING = 64;
    private static final int ALLOW_LIBS = 2;
    private static final int ALLOW_OEM_PERMISSIONS = 32;
    private static final int ALLOW_PERMISSIONS = 4;
    private static final int ALLOW_PRIVAPP_PERMISSIONS = 16;
    static final String TAG = "SystemConfig";
    static SystemConfig sInstance;
    int[] mGlobalGids;
    final SparseArray<ArraySet<String>> mSystemPermissions = new SparseArray<>();
    final ArrayMap<String, String> mSharedLibraries = new ArrayMap<>();
    final ArrayMap<String, FeatureInfo> mAvailableFeatures = new ArrayMap<>();
    final ArraySet<String> mUnavailableFeatures = new ArraySet<>();
    final ArraySet<String> unsupportFeatures = new ArraySet<>();
    final ArrayMap<String, PermissionEntry> mPermissions = new ArrayMap<>();
    final ArraySet<String> mAllowInPowerSaveExceptIdle = new ArraySet<>();
    final ArraySet<String> mAllowInPowerSave = new ArraySet<>();
    final ArraySet<String> mAllowInDataUsageSave = new ArraySet<>();
    final ArraySet<String> mAllowUnthrottledLocation = new ArraySet<>();
    final ArraySet<String> mAllowImplicitBroadcasts = new ArraySet<>();
    final ArraySet<String> mLinkedApps = new ArraySet<>();
    final ArraySet<String> mSystemUserWhitelistedApps = new ArraySet<>();
    final ArraySet<String> mSystemUserBlacklistedApps = new ArraySet<>();
    final ArraySet<ComponentName> mDefaultVrComponents = new ArraySet<>();
    final ArraySet<ComponentName> mBackupTransportWhitelist = new ArraySet<>();
    final ArraySet<String> mHiddenApiPackageWhitelist = new ArraySet<>();
    final ArraySet<String> mDisabledUntilUsedPreinstalledCarrierApps = new ArraySet<>();
    final ArrayMap<String, List<String>> mDisabledUntilUsedPreinstalledCarrierAssociatedApps = new ArrayMap<>();
    final ArrayMap<String, ArraySet<String>> mPrivAppPermissions = new ArrayMap<>();
    final ArrayMap<String, ArraySet<String>> mPrivAppDenyPermissions = new ArrayMap<>();
    final ArrayMap<String, ArraySet<String>> mVendorPrivAppPermissions = new ArrayMap<>();
    final ArrayMap<String, ArraySet<String>> mVendorPrivAppDenyPermissions = new ArrayMap<>();
    final ArrayMap<String, ArraySet<String>> mProductPrivAppPermissions = new ArrayMap<>();
    final ArrayMap<String, ArraySet<String>> mProductPrivAppDenyPermissions = new ArrayMap<>();
    final ArrayMap<String, ArrayMap<String, Boolean>> mOemPermissions = new ArrayMap<>();

    /* loaded from: classes3.dex */
    public static final class PermissionEntry {
        public int[] gids;
        public final String name;
        public boolean perUser;

        PermissionEntry(String name, boolean perUser) {
            this.name = name;
            this.perUser = perUser;
        }
    }

    public static SystemConfig getInstance() {
        SystemConfig systemConfig;
        synchronized (SystemConfig.class) {
            if (sInstance == null) {
                sInstance = new SystemConfig();
            }
            systemConfig = sInstance;
        }
        return systemConfig;
    }

    public int[] getGlobalGids() {
        return this.mGlobalGids;
    }

    public SparseArray<ArraySet<String>> getSystemPermissions() {
        return this.mSystemPermissions;
    }

    public ArrayMap<String, String> getSharedLibraries() {
        return this.mSharedLibraries;
    }

    public ArrayMap<String, FeatureInfo> getAvailableFeatures() {
        return this.mAvailableFeatures;
    }

    public ArrayMap<String, PermissionEntry> getPermissions() {
        return this.mPermissions;
    }

    public ArraySet<String> getAllowImplicitBroadcasts() {
        return this.mAllowImplicitBroadcasts;
    }

    public ArraySet<String> getAllowInPowerSaveExceptIdle() {
        return this.mAllowInPowerSaveExceptIdle;
    }

    public ArraySet<String> getAllowInPowerSave() {
        return this.mAllowInPowerSave;
    }

    public ArraySet<String> getAllowInDataUsageSave() {
        return this.mAllowInDataUsageSave;
    }

    public ArraySet<String> getAllowUnthrottledLocation() {
        return this.mAllowUnthrottledLocation;
    }

    public ArraySet<String> getLinkedApps() {
        return this.mLinkedApps;
    }

    public ArraySet<String> getSystemUserWhitelistedApps() {
        return this.mSystemUserWhitelistedApps;
    }

    public ArraySet<String> getSystemUserBlacklistedApps() {
        return this.mSystemUserBlacklistedApps;
    }

    public ArraySet<String> getHiddenApiWhitelistedApps() {
        return this.mHiddenApiPackageWhitelist;
    }

    public ArraySet<ComponentName> getDefaultVrComponents() {
        return this.mDefaultVrComponents;
    }

    public ArraySet<ComponentName> getBackupTransportWhitelist() {
        return this.mBackupTransportWhitelist;
    }

    public ArraySet<String> getDisabledUntilUsedPreinstalledCarrierApps() {
        return this.mDisabledUntilUsedPreinstalledCarrierApps;
    }

    public ArrayMap<String, List<String>> getDisabledUntilUsedPreinstalledCarrierAssociatedApps() {
        return this.mDisabledUntilUsedPreinstalledCarrierAssociatedApps;
    }

    public ArraySet<String> getPrivAppPermissions(String packageName) {
        return this.mPrivAppPermissions.get(packageName);
    }

    public ArraySet<String> getPrivAppDenyPermissions(String packageName) {
        return this.mPrivAppDenyPermissions.get(packageName);
    }

    public ArraySet<String> getVendorPrivAppPermissions(String packageName) {
        return this.mVendorPrivAppPermissions.get(packageName);
    }

    public ArraySet<String> getVendorPrivAppDenyPermissions(String packageName) {
        return this.mVendorPrivAppDenyPermissions.get(packageName);
    }

    public ArraySet<String> getProductPrivAppPermissions(String packageName) {
        return this.mProductPrivAppPermissions.get(packageName);
    }

    public ArraySet<String> getProductPrivAppDenyPermissions(String packageName) {
        return this.mProductPrivAppDenyPermissions.get(packageName);
    }

    public Map<String, Boolean> getOemPermissions(String packageName) {
        Map<String, Boolean> oemPermissions = this.mOemPermissions.get(packageName);
        if (oemPermissions != null) {
            return oemPermissions;
        }
        return Collections.emptyMap();
    }

    SystemConfig() {
        readPermissions(Environment.buildPath(Environment.getRootDirectory(), "etc", "sysconfig"), -1);
        readPermissions(Environment.buildPath(Environment.getRootDirectory(), "etc", StorageVolume.ScopedAccessProviderContract.TABLE_PERMISSIONS), -1);
        int vendorPermissionFlag = Build.VERSION.FIRST_SDK_INT <= 27 ? 19 | 12 : 19;
        readPermissions(Environment.buildPath(Environment.getVendorDirectory(), "etc", "sysconfig"), vendorPermissionFlag);
        readPermissions(Environment.buildPath(Environment.getVendorDirectory(), "etc", StorageVolume.ScopedAccessProviderContract.TABLE_PERMISSIONS), vendorPermissionFlag);
        int odmPermissionFlag = vendorPermissionFlag;
        readPermissions(Environment.buildPath(Environment.getOdmDirectory(), "etc", "sysconfig"), odmPermissionFlag);
        readPermissions(Environment.buildPath(Environment.getOdmDirectory(), "etc", StorageVolume.ScopedAccessProviderContract.TABLE_PERMISSIONS), odmPermissionFlag);
        readPermissions(Environment.buildPath(Environment.getOemDirectory(), "etc", "sysconfig"), 33);
        readPermissions(Environment.buildPath(Environment.getOemDirectory(), "etc", StorageVolume.ScopedAccessProviderContract.TABLE_PERMISSIONS), 33);
        readPermissions(Environment.buildPath(Environment.getProductDirectory(), "etc", "sysconfig"), 31);
        readPermissions(Environment.buildPath(Environment.getProductDirectory(), "etc", StorageVolume.ScopedAccessProviderContract.TABLE_PERMISSIONS), 31);
    }

    void readPermissions(File libraryDir, int permissionFlag) {
        File[] listFiles;
        if (!libraryDir.exists() || !libraryDir.isDirectory()) {
            if (permissionFlag == -1) {
                Slog.w(TAG, "No directory " + libraryDir + ", skipping");
            }
        } else if (!libraryDir.canRead()) {
            Slog.w(TAG, "Directory " + libraryDir + " cannot be read");
        } else {
            File platformFile = null;
            for (File f : libraryDir.listFiles()) {
                if (f.getPath().endsWith("etc/permissions/platform.xml")) {
                    platformFile = f;
                } else if (!f.getPath().endsWith(".xml")) {
                    Slog.i(TAG, "Non-xml file " + f + " in " + libraryDir + " directory, ignoring");
                } else if (!f.canRead()) {
                    Slog.w(TAG, "Permissions library file " + f + " cannot be read");
                } else {
                    readPermissionsFromXml(f, permissionFlag);
                }
            }
            if (platformFile != null) {
                readPermissionsFromXml(platformFile, permissionFlag);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x022a, code lost:
        r4.add(r4);
        com.android.internal.util.XmlUtils.skipCurrentTag(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:101:0x0232, code lost:
        r22 = r4;
        r21 = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x023c, code lost:
        if ("library".equals(r6) == false) goto L99;
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x023e, code lost:
        if (r10 == false) goto L99;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x0240, code lost:
        r0 = r7.getAttributeValue(null, "name");
        r4 = r7.getAttributeValue(null, android.content.ContentResolver.SCHEME_FILE);
     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x024e, code lost:
        if (r0 != null) goto L96;
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x0250, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<library> without name in " + r25 + " at " + r7.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x0273, code lost:
        if (r4 != null) goto L98;
     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x0275, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<library> without file in " + r25 + " at " + r7.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x0298, code lost:
        r24.mSharedLibraries.put(r0, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x029d, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x02a8, code lost:
        if ("feature".equals(r6) == false) goto L111;
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x02aa, code lost:
        if (r12 == false) goto L111;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x02ac, code lost:
        r0 = r7.getAttributeValue(null, "name");
        r3 = com.android.internal.util.XmlUtils.readIntAttribute(r7, "version", 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x02ba, code lost:
        if (r5 != false) goto L110;
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x02bc, code lost:
        r4 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x02bf, code lost:
        r4 = r7.getAttributeValue(null, "notLowRam");
        r4 = !"true".equals(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x02cf, code lost:
        if (r0 != null) goto L108;
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x02d1, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<feature> without name in " + r25 + " at " + r7.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0026, code lost:
        if (r9 != 2) goto L327;
     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x02f4, code lost:
        if (r4 == false) goto L107;
     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x02f6, code lost:
        addFeature(r0, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x02f9, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:124:0x0304, code lost:
        if ("unavailable-feature".equals(r6) == false) goto L119;
     */
    /* JADX WARN: Code restructure failed: missing block: B:125:0x0306, code lost:
        if (r12 == false) goto L119;
     */
    /* JADX WARN: Code restructure failed: missing block: B:126:0x0308, code lost:
        r0 = r7.getAttributeValue(null, "name");
     */
    /* JADX WARN: Code restructure failed: missing block: B:127:0x030f, code lost:
        if (r0 != null) goto L118;
     */
    /* JADX WARN: Code restructure failed: missing block: B:128:0x0311, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<unavailable-feature> without name in " + r25 + " at " + r7.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:129:0x0334, code lost:
        r24.mUnavailableFeatures.add(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:130:0x0339, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:132:0x0344, code lost:
        if ("allow-in-power-save-except-idle".equals(r6) == false) goto L127;
     */
    /* JADX WARN: Code restructure failed: missing block: B:133:0x0346, code lost:
        if (r8 == false) goto L127;
     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x0348, code lost:
        r0 = r7.getAttributeValue(null, "package");
     */
    /* JADX WARN: Code restructure failed: missing block: B:135:0x034f, code lost:
        if (r0 != null) goto L126;
     */
    /* JADX WARN: Code restructure failed: missing block: B:136:0x0351, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<allow-in-power-save-except-idle> without package in " + r25 + " at " + r7.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x0374, code lost:
        r24.mAllowInPowerSaveExceptIdle.add(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:138:0x0379, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0032, code lost:
        if (r7.getName().equals(android.os.storage.StorageVolume.ScopedAccessProviderContract.TABLE_PERMISSIONS) != false) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x0384, code lost:
        if ("allow-in-power-save".equals(r6) == false) goto L135;
     */
    /* JADX WARN: Code restructure failed: missing block: B:141:0x0386, code lost:
        if (r8 == false) goto L135;
     */
    /* JADX WARN: Code restructure failed: missing block: B:142:0x0388, code lost:
        r0 = r7.getAttributeValue(null, "package");
     */
    /* JADX WARN: Code restructure failed: missing block: B:143:0x038f, code lost:
        if (r0 != null) goto L134;
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x0391, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<allow-in-power-save> without package in " + r25 + " at " + r7.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:145:0x03b4, code lost:
        r24.mAllowInPowerSave.add(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:146:0x03b9, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:148:0x03c4, code lost:
        if ("allow-in-data-usage-save".equals(r6) == false) goto L143;
     */
    /* JADX WARN: Code restructure failed: missing block: B:149:0x03c6, code lost:
        if (r8 == false) goto L143;
     */
    /* JADX WARN: Code restructure failed: missing block: B:150:0x03c8, code lost:
        r0 = r7.getAttributeValue(null, "package");
     */
    /* JADX WARN: Code restructure failed: missing block: B:151:0x03cf, code lost:
        if (r0 != null) goto L142;
     */
    /* JADX WARN: Code restructure failed: missing block: B:152:0x03d1, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<allow-in-data-usage-save> without package in " + r25 + " at " + r7.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:153:0x03f4, code lost:
        r24.mAllowInDataUsageSave.add(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:154:0x03f9, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:156:0x0404, code lost:
        if ("allow-unthrottled-location".equals(r6) == false) goto L151;
     */
    /* JADX WARN: Code restructure failed: missing block: B:157:0x0406, code lost:
        if (r8 == false) goto L151;
     */
    /* JADX WARN: Code restructure failed: missing block: B:158:0x0408, code lost:
        r0 = r7.getAttributeValue(null, "package");
     */
    /* JADX WARN: Code restructure failed: missing block: B:159:0x040f, code lost:
        if (r0 != null) goto L150;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x003e, code lost:
        if (r7.getName().equals("config") == false) goto L306;
     */
    /* JADX WARN: Code restructure failed: missing block: B:160:0x0411, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<allow-unthrottled-location> without package in " + r25 + " at " + r7.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:161:0x0434, code lost:
        r24.mAllowUnthrottledLocation.add(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:162:0x0439, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x0444, code lost:
        if ("allow-implicit-broadcast".equals(r6) == false) goto L159;
     */
    /* JADX WARN: Code restructure failed: missing block: B:165:0x0446, code lost:
        if (r8 == false) goto L159;
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x0448, code lost:
        r0 = r7.getAttributeValue(null, "action");
     */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x044f, code lost:
        if (r0 != null) goto L158;
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x0451, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<allow-implicit-broadcast> without action in " + r25 + " at " + r7.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:169:0x0474, code lost:
        r24.mAllowImplicitBroadcasts.add(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x0479, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:172:0x0484, code lost:
        if ("app-link".equals(r6) == false) goto L167;
     */
    /* JADX WARN: Code restructure failed: missing block: B:173:0x0486, code lost:
        if (r14 == false) goto L167;
     */
    /* JADX WARN: Code restructure failed: missing block: B:174:0x0488, code lost:
        r0 = r7.getAttributeValue(null, "package");
     */
    /* JADX WARN: Code restructure failed: missing block: B:175:0x048f, code lost:
        if (r0 != null) goto L166;
     */
    /* JADX WARN: Code restructure failed: missing block: B:176:0x0491, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<app-link> without package in " + r25 + " at " + r7.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:177:0x04b4, code lost:
        r24.mLinkedApps.add(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:178:0x04b9, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:180:0x04c4, code lost:
        if ("system-user-whitelisted-app".equals(r6) == false) goto L175;
     */
    /* JADX WARN: Code restructure failed: missing block: B:181:0x04c6, code lost:
        if (r14 == false) goto L175;
     */
    /* JADX WARN: Code restructure failed: missing block: B:182:0x04c8, code lost:
        r0 = r7.getAttributeValue(null, "package");
     */
    /* JADX WARN: Code restructure failed: missing block: B:183:0x04cf, code lost:
        if (r0 != null) goto L174;
     */
    /* JADX WARN: Code restructure failed: missing block: B:184:0x04d1, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<system-user-whitelisted-app> without package in " + r25 + " at " + r7.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:185:0x04f4, code lost:
        r24.mSystemUserWhitelistedApps.add(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:186:0x04f9, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:188:0x0504, code lost:
        if ("system-user-blacklisted-app".equals(r6) == false) goto L183;
     */
    /* JADX WARN: Code restructure failed: missing block: B:189:0x0506, code lost:
        if (r14 == false) goto L183;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0068, code lost:
        throw new org.xmlpull.v1.XmlPullParserException("Unexpected start tag in " + r25 + ": found " + r7.getName() + ", expected 'permissions' or 'config'");
     */
    /* JADX WARN: Code restructure failed: missing block: B:190:0x0508, code lost:
        r0 = r7.getAttributeValue(null, "package");
     */
    /* JADX WARN: Code restructure failed: missing block: B:191:0x050f, code lost:
        if (r0 != null) goto L182;
     */
    /* JADX WARN: Code restructure failed: missing block: B:192:0x0511, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<system-user-blacklisted-app without package in " + r25 + " at " + r7.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:193:0x0534, code lost:
        r24.mSystemUserBlacklistedApps.add(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:194:0x0539, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:196:0x0544, code lost:
        if ("default-enabled-vr-app".equals(r6) == false) goto L193;
     */
    /* JADX WARN: Code restructure failed: missing block: B:197:0x0546, code lost:
        if (r14 == false) goto L193;
     */
    /* JADX WARN: Code restructure failed: missing block: B:198:0x0548, code lost:
        r0 = r7.getAttributeValue(null, "package");
        r4 = r7.getAttributeValue(null, "class");
     */
    /* JADX WARN: Code restructure failed: missing block: B:199:0x0556, code lost:
        if (r0 != null) goto L190;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0069, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x0558, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<default-enabled-vr-app without package in " + r25 + " at " + r7.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:201:0x057b, code lost:
        if (r4 != null) goto L192;
     */
    /* JADX WARN: Code restructure failed: missing block: B:202:0x057d, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<default-enabled-vr-app without class in " + r25 + " at " + r7.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:203:0x05a0, code lost:
        r24.mDefaultVrComponents.add(new android.content.ComponentName(r0, r4));
     */
    /* JADX WARN: Code restructure failed: missing block: B:204:0x05aa, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:206:0x05b5, code lost:
        if ("backup-transport-whitelisted-service".equals(r6) == false) goto L204;
     */
    /* JADX WARN: Code restructure failed: missing block: B:207:0x05b7, code lost:
        if (r12 == false) goto L204;
     */
    /* JADX WARN: Code restructure failed: missing block: B:208:0x05b9, code lost:
        r0 = r7.getAttributeValue(null, "service");
     */
    /* JADX WARN: Code restructure failed: missing block: B:209:0x05c0, code lost:
        if (r0 != null) goto L200;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x006a, code lost:
        r3 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:210:0x05c2, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<backup-transport-whitelisted-service> without service in " + r25 + " at " + r7.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:211:0x05e5, code lost:
        r3 = android.content.ComponentName.unflattenFromString(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:212:0x05e9, code lost:
        if (r3 != null) goto L203;
     */
    /* JADX WARN: Code restructure failed: missing block: B:213:0x05eb, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<backup-transport-whitelisted-service> with invalid service name " + r0 + " in " + r25 + " at " + r7.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:214:0x0616, code lost:
        r24.mBackupTransportWhitelist.add(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:215:0x061b, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:217:0x0626, code lost:
        if ("disabled-until-used-preinstalled-carrier-associated-app".equals(r6) == false) goto L217;
     */
    /* JADX WARN: Code restructure failed: missing block: B:218:0x0628, code lost:
        if (r14 == false) goto L217;
     */
    /* JADX WARN: Code restructure failed: missing block: B:219:0x062a, code lost:
        r0 = r7.getAttributeValue(null, "package");
        r4 = r7.getAttributeValue(null, "carrierAppPackage");
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x006d, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:220:0x0638, code lost:
        if (r0 == null) goto L216;
     */
    /* JADX WARN: Code restructure failed: missing block: B:221:0x063a, code lost:
        if (r4 != null) goto L211;
     */
    /* JADX WARN: Code restructure failed: missing block: B:223:0x063d, code lost:
        r4 = r24.mDisabledUntilUsedPreinstalledCarrierAssociatedApps.get(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:224:0x0645, code lost:
        if (r4 != null) goto L214;
     */
    /* JADX WARN: Code restructure failed: missing block: B:225:0x0647, code lost:
        r4 = new java.util.ArrayList();
        r24.mDisabledUntilUsedPreinstalledCarrierAssociatedApps.put(r4, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:226:0x0652, code lost:
        r4.add(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:227:0x0656, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<disabled-until-used-preinstalled-carrier-associated-app without package or carrierAppPackage in " + r25 + " at " + r7.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:228:0x0678, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x006e, code lost:
        r22 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:230:0x0683, code lost:
        if ("disabled-until-used-preinstalled-carrier-app".equals(r6) == false) goto L225;
     */
    /* JADX WARN: Code restructure failed: missing block: B:231:0x0685, code lost:
        if (r14 == false) goto L225;
     */
    /* JADX WARN: Code restructure failed: missing block: B:232:0x0687, code lost:
        r0 = r7.getAttributeValue(null, "package");
     */
    /* JADX WARN: Code restructure failed: missing block: B:233:0x068e, code lost:
        if (r0 != null) goto L224;
     */
    /* JADX WARN: Code restructure failed: missing block: B:234:0x0690, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<disabled-until-used-preinstalled-carrier-app> without package in " + r25 + " at " + r7.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:235:0x06b3, code lost:
        r24.mDisabledUntilUsedPreinstalledCarrierApps.add(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:236:0x06b8, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:238:0x06c3, code lost:
        if ("privapp-permissions".equals(r6) == false) goto L242;
     */
    /* JADX WARN: Code restructure failed: missing block: B:239:0x06c5, code lost:
        if (r15 == false) goto L242;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0072, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:241:0x06d7, code lost:
        if (r25.toPath().startsWith(android.os.Environment.getVendorDirectory().toPath()) != false) goto L241;
     */
    /* JADX WARN: Code restructure failed: missing block: B:243:0x06e9, code lost:
        if (r25.toPath().startsWith(android.os.Environment.getOdmDirectory().toPath()) == false) goto L233;
     */
    /* JADX WARN: Code restructure failed: missing block: B:245:0x06ec, code lost:
        r0 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:246:0x06ee, code lost:
        r0 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:247:0x06ef, code lost:
        r3 = r25.toPath().startsWith(android.os.Environment.getProductDirectory().toPath());
     */
    /* JADX WARN: Code restructure failed: missing block: B:248:0x06ff, code lost:
        if (r0 == false) goto L238;
     */
    /* JADX WARN: Code restructure failed: missing block: B:249:0x0701, code lost:
        readPrivAppPermissions(r7, r24.mVendorPrivAppPermissions, r24.mVendorPrivAppDenyPermissions);
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0073, code lost:
        r3 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:250:0x0709, code lost:
        if (r3 == false) goto L240;
     */
    /* JADX WARN: Code restructure failed: missing block: B:251:0x070b, code lost:
        readPrivAppPermissions(r7, r24.mProductPrivAppPermissions, r24.mProductPrivAppDenyPermissions);
     */
    /* JADX WARN: Code restructure failed: missing block: B:252:0x0713, code lost:
        readPrivAppPermissions(r7, r24.mPrivAppPermissions, r24.mPrivAppDenyPermissions);
     */
    /* JADX WARN: Code restructure failed: missing block: B:255:0x0722, code lost:
        if ("oem-permissions".equals(r6) == false) goto L246;
     */
    /* JADX WARN: Code restructure failed: missing block: B:256:0x0724, code lost:
        if (r16 == false) goto L246;
     */
    /* JADX WARN: Code restructure failed: missing block: B:257:0x0726, code lost:
        readOemPermissions(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:259:0x0731, code lost:
        if ("hidden-api-whitelisted-app".equals(r6) == false) goto L254;
     */
    /* JADX WARN: Code restructure failed: missing block: B:260:0x0733, code lost:
        if (r17 == false) goto L254;
     */
    /* JADX WARN: Code restructure failed: missing block: B:261:0x0735, code lost:
        r3 = null;
        r0 = r7.getAttributeValue(null, "package");
     */
    /* JADX WARN: Code restructure failed: missing block: B:262:0x073c, code lost:
        if (r0 != null) goto L253;
     */
    /* JADX WARN: Code restructure failed: missing block: B:263:0x073e, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<hidden-api-whitelisted-app> without package in " + r25 + " at " + r7.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:264:0x0761, code lost:
        r24.mHiddenApiPackageWhitelist.add(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:265:0x0766, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:267:0x076b, code lost:
        r3 = null;
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "Tag " + r6 + " is unknown or not allowed in " + r25.getParent());
        com.android.internal.util.XmlUtils.skipCurrentTag(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:268:0x0792, code lost:
        r0 = r3;
        r9 = r20;
        r13 = r21;
        r4 = r22;
        r11 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0077, code lost:
        if (r26 != (-1)) goto L301;
     */
    /* JADX WARN: Code restructure failed: missing block: B:270:0x07a9, code lost:
        throw new org.xmlpull.v1.XmlPullParserException("No start tag found");
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0079, code lost:
        r8 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:283:0x07bf, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "Got exception parsing permissions.", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:284:0x07c6, code lost:
        libcore.io.IoUtils.closeQuietly(r22);
     */
    /* JADX WARN: Code restructure failed: missing block: B:285:0x07cc, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:286:0x07cd, code lost:
        r3 = r22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:289:0x07d3, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "Got exception parsing permissions.", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x007b, code lost:
        r8 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:290:0x07da, code lost:
        libcore.io.IoUtils.closeQuietly(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:293:0x07e5, code lost:
        if (android.os.SystemProperties.getBoolean("persist.graphics.vulkan.disable", false) != false) goto L260;
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x07e7, code lost:
        removeFeature(android.content.pm.PackageManager.FEATURE_VULKAN_HARDWARE_LEVEL);
        removeFeature(android.content.pm.PackageManager.FEATURE_VULKAN_HARDWARE_VERSION);
        removeFeature(android.content.pm.PackageManager.FEATURE_VULKAN_HARDWARE_COMPUTE);
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x0802, code lost:
        if (android.os.SystemProperties.get("ro.baseband").equals("apq") != false) goto L263;
     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x0804, code lost:
        r24.unsupportFeatures.add(android.content.pm.PackageManager.FEATURE_TELEPHONY);
        r24.unsupportFeatures.add(android.content.pm.PackageManager.FEATURE_TELEPHONY_GSM);
        r24.unsupportFeatures.add(android.content.pm.PackageManager.FEATURE_TELEPHONY_CDMA);
        r24.unsupportFeatures.add(android.content.pm.PackageManager.FEATURE_HIFI_SENSORS);
        r24.unsupportFeatures.add(android.content.pm.PackageManager.FEATURE_SENSOR_ACCELEROMETER);
        r24.unsupportFeatures.add(android.content.pm.PackageManager.FEATURE_SENSOR_BAROMETER);
        r24.unsupportFeatures.add(android.content.pm.PackageManager.FEATURE_SENSOR_COMPASS);
        r24.unsupportFeatures.add(android.content.pm.PackageManager.FEATURE_SENSOR_GYROSCOPE);
        r24.unsupportFeatures.add(android.content.pm.PackageManager.FEATURE_SENSOR_LIGHT);
        r24.unsupportFeatures.add(android.content.pm.PackageManager.FEATURE_SENSOR_PROXIMITY);
        r24.unsupportFeatures.add(android.content.pm.PackageManager.FEATURE_SENSOR_STEP_COUNTER);
        r24.unsupportFeatures.add(android.content.pm.PackageManager.FEATURE_SENSOR_STEP_DETECTOR);
        r24.unsupportFeatures.add(android.content.pm.PackageManager.FEATURE_SENSOR_AMBIENT_TEMPERATURE);
        r24.unsupportFeatures.add(android.content.pm.PackageManager.FEATURE_SENSOR_RELATIVE_HUMIDITY);
        r24.unsupportFeatures.add(android.content.pm.PackageManager.FEATURE_CAMERA);
        r24.unsupportFeatures.add(android.content.pm.PackageManager.FEATURE_CAMERA_AUTOFOCUS);
        r24.unsupportFeatures.add(android.content.pm.PackageManager.FEATURE_CAMERA_FLASH);
        r24.unsupportFeatures.add(android.content.pm.PackageManager.FEATURE_CAMERA_FRONT);
        r24.unsupportFeatures.add(android.content.pm.PackageManager.FEATURE_CAMERA_ANY);
        r24.unsupportFeatures.add(android.content.pm.PackageManager.FEATURE_CAMERA_LEVEL_FULL);
        r24.unsupportFeatures.add(android.content.pm.PackageManager.FEATURE_CAMERA_CAPABILITY_MANUAL_SENSOR);
        r24.unsupportFeatures.add(android.content.pm.PackageManager.FEATURE_CAMERA_CAPABILITY_MANUAL_POST_PROCESSING);
        r24.unsupportFeatures.add(android.content.pm.PackageManager.FEATURE_CAMERA_CAPABILITY_RAW);
        r24.unsupportFeatures.add(android.content.pm.PackageManager.FEATURE_CAMERA_EXTERNAL);
        android.util.Slog.i(com.android.server.SystemConfig.TAG, "Removing unsupported features");
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x08b3, code lost:
        r0 = r24.unsupportFeatures.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x08bd, code lost:
        if (r0.hasNext() != false) goto L267;
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x08bf, code lost:
        r4 = r0.next();
     */
    /* JADX WARN: Code restructure failed: missing block: B:302:0x08cb, code lost:
        if (r24.mAvailableFeatures.remove(r4) != null) goto L269;
     */
    /* JADX WARN: Code restructure failed: missing block: B:303:0x08cd, code lost:
        android.util.Slog.d(com.android.server.SystemConfig.TAG, "Removed unsupport feature " + r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:306:0x08e8, code lost:
        if (android.os.storage.StorageManager.isFileEncryptedNativeOnly() == false) goto L292;
     */
    /* JADX WARN: Code restructure failed: missing block: B:307:0x08ea, code lost:
        r4 = 0;
        addFeature(android.content.pm.PackageManager.FEATURE_FILE_BASED_ENCRYPTION, 0);
        addFeature(android.content.pm.PackageManager.FEATURE_SECURELY_REMOVES_USERS, 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:308:0x08f6, code lost:
        r4 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x007e, code lost:
        if ((r26 & 2) == 0) goto L300;
     */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x08fb, code lost:
        if (android.os.storage.StorageManager.hasAdoptable() != false) goto L280;
     */
    /* JADX WARN: Code restructure failed: missing block: B:311:0x08fd, code lost:
        addFeature(android.content.pm.PackageManager.FEATURE_ADOPTABLE_STORAGE, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x0906, code lost:
        if (android.app.ActivityManager.isLowRamDeviceStatic() == false) goto L291;
     */
    /* JADX WARN: Code restructure failed: missing block: B:314:0x0908, code lost:
        addFeature(android.content.pm.PackageManager.FEATURE_RAM_LOW, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:315:0x090e, code lost:
        addFeature(android.content.pm.PackageManager.FEATURE_RAM_NORMAL, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:316:0x0913, code lost:
        r0 = r24.mUnavailableFeatures.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:318:0x091d, code lost:
        if (r0.hasNext() != false) goto L287;
     */
    /* JADX WARN: Code restructure failed: missing block: B:319:0x091f, code lost:
        r4 = r0.next();
        removeFeature(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0080, code lost:
        r10 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:320:0x0929, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:321:0x092a, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:322:0x092b, code lost:
        libcore.io.IoUtils.closeQuietly(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x092e, code lost:
        throw r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0082, code lost:
        r10 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0085, code lost:
        if ((r26 & 1) == 0) goto L299;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0087, code lost:
        r12 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0089, code lost:
        r12 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x008c, code lost:
        if ((r26 & 4) == 0) goto L298;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x008e, code lost:
        r13 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0090, code lost:
        r13 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0093, code lost:
        if ((r26 & 8) == 0) goto L297;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0095, code lost:
        r14 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0097, code lost:
        r14 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x009a, code lost:
        if ((r26 & 16) == 0) goto L296;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x009c, code lost:
        r15 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x009e, code lost:
        r15 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00a1, code lost:
        if ((r26 & 32) == 0) goto L295;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00a3, code lost:
        r16 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00a6, code lost:
        r16 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00aa, code lost:
        if ((r26 & 64) == 0) goto L294;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00ac, code lost:
        r17 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00af, code lost:
        r17 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00b1, code lost:
        com.android.internal.util.XmlUtils.nextElement(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00b8, code lost:
        if (r7.getEventType() != r11) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00bb, code lost:
        libcore.io.IoUtils.closeQuietly(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00c2, code lost:
        r6 = r7.getName();
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00cc, code lost:
        if (android.net.wifi.WifiConfiguration.GroupCipher.varName.equals(r6) == false) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00ce, code lost:
        if (r8 == false) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x00d0, code lost:
        r11 = r7.getAttributeValue(r0, "gid");
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00d6, code lost:
        if (r11 == null) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00d8, code lost:
        r18 = android.os.Process.getGidForName(r11);
        r24.mGlobalGids = com.android.internal.util.ArrayUtils.appendInt(r24.mGlobalGids, r18);
        r20 = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x00ec, code lost:
        r3 = new java.lang.StringBuilder();
        r20 = r9;
        r3.append("<group> without gid in ");
        r3.append(r25);
        r3.append(" at ");
        r3.append(r7.getPositionDescription());
        android.util.Slog.w(com.android.server.SystemConfig.TAG, r3.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0110, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0114, code lost:
        r22 = r4;
        r21 = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x0118, code lost:
        r3 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x011b, code lost:
        r20 = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x0123, code lost:
        if (android.hardware.usb.UsbManager.EXTRA_PERMISSION_GRANTED.equals(r6) == false) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0125, code lost:
        if (r13 == false) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0127, code lost:
        r0 = r7.getAttributeValue(null, "name");
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x012e, code lost:
        if (r0 != null) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0130, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<permission> without name in " + r25 + " at " + r7.getPositionDescription());
        com.android.internal.util.XmlUtils.skipCurrentTag(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x0156, code lost:
        readPermission(r7, r0.intern());
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x015f, code lost:
        r22 = r4;
        r21 = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0163, code lost:
        r3 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x016c, code lost:
        if ("assign-permission".equals(r6) == false) goto L89;
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x016e, code lost:
        if (r13 == false) goto L89;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x0170, code lost:
        r0 = r7.getAttributeValue(null, "name");
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x0177, code lost:
        if (r0 != null) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:85:0x0179, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<assign-permission> without name in " + r25 + " at " + r7.getPositionDescription());
        com.android.internal.util.XmlUtils.skipCurrentTag(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x01a0, code lost:
        r3 = r7.getAttributeValue(null, "uid");
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x01a7, code lost:
        if (r3 != null) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x01a9, code lost:
        r11 = new java.lang.StringBuilder();
        r21 = r13;
        r11.append("<assign-permission> without uid in ");
        r11.append(r25);
        r11.append(" at ");
        r11.append(r7.getPositionDescription());
        android.util.Slog.w(com.android.server.SystemConfig.TAG, r11.toString());
        com.android.internal.util.XmlUtils.skipCurrentTag(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x01d1, code lost:
        r22 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x01d5, code lost:
        r21 = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x01d7, code lost:
        r9 = android.os.Process.getUidForName(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x01db, code lost:
        if (r9 >= 0) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x01dd, code lost:
        r13 = new java.lang.StringBuilder();
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x01e4, code lost:
        r22 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x01e6, code lost:
        r13.append("<assign-permission> with unknown uid \"");
        r13.append(r3);
        r13.append("  in ");
        r13.append(r25);
        r13.append(" at ");
        r13.append(r7.getPositionDescription());
        android.util.Slog.w(com.android.server.SystemConfig.TAG, r13.toString());
        com.android.internal.util.XmlUtils.skipCurrentTag(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x020e, code lost:
        r22 = r4;
        r4 = r0.intern();
        r4 = r24.mSystemPermissions.get(r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x021d, code lost:
        if (r4 != null) goto L88;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x021f, code lost:
        r4 = new android.util.ArraySet<>();
        r24.mSystemPermissions.put(r9, r4);
     */
    /* JADX WARN: Not initialized variable reg: 22, insn: 0x07ab: MOVE  (r3 I:??[OBJECT, ARRAY]) = (r22 I:??[OBJECT, ARRAY] A[D('permReader' java.io.FileReader)]), block:B:272:0x07ab */
    /* JADX WARN: Not initialized variable reg: 22, insn: 0x07b2: MOVE  (r3 I:??[OBJECT, ARRAY]) = (r22 I:??[OBJECT, ARRAY] A[D('permReader' java.io.FileReader)]), block:B:276:0x07b2 */
    /* JADX WARN: Removed duplicated region for block: B:294:0x07e7  */
    /* JADX WARN: Removed duplicated region for block: B:297:0x0804  */
    /* JADX WARN: Removed duplicated region for block: B:301:0x08bf  */
    /* JADX WARN: Removed duplicated region for block: B:307:0x08ea  */
    /* JADX WARN: Removed duplicated region for block: B:308:0x08f6  */
    /* JADX WARN: Removed duplicated region for block: B:311:0x08fd  */
    /* JADX WARN: Removed duplicated region for block: B:314:0x0908  */
    /* JADX WARN: Removed duplicated region for block: B:315:0x090e  */
    /* JADX WARN: Removed duplicated region for block: B:319:0x091f A[LOOP:3: B:317:0x0919->B:319:0x091f, LOOP_END] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void readPermissionsFromXml(java.io.File r25, int r26) {
        /*
            Method dump skipped, instructions count: 2375
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.SystemConfig.readPermissionsFromXml(java.io.File, int):void");
    }

    private void addFeature(String name, int version) {
        FeatureInfo fi = this.mAvailableFeatures.get(name);
        if (fi == null) {
            FeatureInfo fi2 = new FeatureInfo();
            fi2.name = name;
            fi2.version = version;
            this.mAvailableFeatures.put(name, fi2);
            return;
        }
        fi.version = Math.max(fi.version, version);
    }

    private void removeFeature(String name) {
        if (this.mAvailableFeatures.remove(name) != null) {
            Slog.d(TAG, "Removed unavailable feature " + name);
        }
    }

    void readPermission(XmlPullParser parser, String name) throws IOException, XmlPullParserException {
        if (this.mPermissions.containsKey(name)) {
            throw new IllegalStateException("Duplicate permission definition for " + name);
        }
        boolean perUser = XmlUtils.readBooleanAttribute(parser, "perUser", false);
        PermissionEntry perm = new PermissionEntry(name, perUser);
        this.mPermissions.put(name, perm);
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        String tagName = parser.getName();
                        if (WifiConfiguration.GroupCipher.varName.equals(tagName)) {
                            String gidStr = parser.getAttributeValue(null, "gid");
                            if (gidStr != null) {
                                int gid = Process.getGidForName(gidStr);
                                perm.gids = ArrayUtils.appendInt(perm.gids, gid);
                            } else {
                                Slog.w(TAG, "<group> without gid at " + parser.getPositionDescription());
                            }
                        }
                        XmlUtils.skipCurrentTag(parser);
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private void readPrivAppPermissions(XmlPullParser parser, ArrayMap<String, ArraySet<String>> grantMap, ArrayMap<String, ArraySet<String>> denyMap) throws IOException, XmlPullParserException {
        String packageName = parser.getAttributeValue(null, "package");
        if (TextUtils.isEmpty(packageName)) {
            Slog.w(TAG, "package is required for <privapp-permissions> in " + parser.getPositionDescription());
            return;
        }
        ArraySet<String> permissions = grantMap.get(packageName);
        if (permissions == null) {
            permissions = new ArraySet<>();
        }
        ArraySet<String> denyPermissions = denyMap.get(packageName);
        int depth = parser.getDepth();
        while (XmlUtils.nextElementWithin(parser, depth)) {
            String name = parser.getName();
            if (UsbManager.EXTRA_PERMISSION_GRANTED.equals(name)) {
                String permName = parser.getAttributeValue(null, "name");
                if (TextUtils.isEmpty(permName)) {
                    Slog.w(TAG, "name is required for <permission> in " + parser.getPositionDescription());
                } else {
                    permissions.add(permName);
                }
            } else if ("deny-permission".equals(name)) {
                String permName2 = parser.getAttributeValue(null, "name");
                if (TextUtils.isEmpty(permName2)) {
                    Slog.w(TAG, "name is required for <deny-permission> in " + parser.getPositionDescription());
                } else {
                    if (denyPermissions == null) {
                        denyPermissions = new ArraySet<>();
                    }
                    denyPermissions.add(permName2);
                }
            }
        }
        grantMap.put(packageName, permissions);
        if (denyPermissions != null) {
            denyMap.put(packageName, denyPermissions);
        }
    }

    void readOemPermissions(XmlPullParser parser) throws IOException, XmlPullParserException {
        String packageName = parser.getAttributeValue(null, "package");
        if (TextUtils.isEmpty(packageName)) {
            Slog.w(TAG, "package is required for <oem-permissions> in " + parser.getPositionDescription());
            return;
        }
        ArrayMap<String, Boolean> permissions = this.mOemPermissions.get(packageName);
        if (permissions == null) {
            permissions = new ArrayMap<>();
        }
        int depth = parser.getDepth();
        while (XmlUtils.nextElementWithin(parser, depth)) {
            String name = parser.getName();
            if (UsbManager.EXTRA_PERMISSION_GRANTED.equals(name)) {
                String permName = parser.getAttributeValue(null, "name");
                if (TextUtils.isEmpty(permName)) {
                    Slog.w(TAG, "name is required for <permission> in " + parser.getPositionDescription());
                } else {
                    permissions.put(permName, Boolean.TRUE);
                }
            } else if ("deny-permission".equals(name)) {
                String permName2 = parser.getAttributeValue(null, "name");
                if (TextUtils.isEmpty(permName2)) {
                    Slog.w(TAG, "name is required for <deny-permission> in " + parser.getPositionDescription());
                } else {
                    permissions.put(permName2, Boolean.FALSE);
                }
            }
        }
        this.mOemPermissions.put(packageName, permissions);
    }
}
