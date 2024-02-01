package com.android.server;

import android.content.ComponentName;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.os.SystemProperties;
import android.permission.PermissionManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.XmlUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes3.dex */
public class SystemConfig {
    private static final int ALLOW_ALL = -1;
    private static final int ALLOW_APP_CONFIGS = 8;
    private static final int ALLOW_ASSOCIATIONS = 128;
    private static final int ALLOW_FEATURES = 1;
    private static final int ALLOW_HIDDENAPI_WHITELISTING = 64;
    private static final int ALLOW_LIBS = 2;
    private static final int ALLOW_OEM_PERMISSIONS = 32;
    private static final int ALLOW_PERMISSIONS = 4;
    private static final int ALLOW_PRIVAPP_PERMISSIONS = 16;
    private static final String SKU_PROPERTY = "ro.boot.product.hardware.sku";
    static final String TAG = "SystemConfig";
    static SystemConfig sInstance;
    int[] mGlobalGids;
    final SparseArray<ArraySet<String>> mSystemPermissions = new SparseArray<>();
    final ArrayList<PermissionManager.SplitPermissionInfo> mSplitPermissions = new ArrayList<>();
    final ArrayMap<String, SharedLibraryEntry> mSharedLibraries = new ArrayMap<>();
    final ArrayMap<String, FeatureInfo> mAvailableFeatures = new ArrayMap<>();
    final ArraySet<String> mUnavailableFeatures = new ArraySet<>();
    final ArrayMap<String, PermissionEntry> mPermissions = new ArrayMap<>();
    final ArraySet<String> mAllowInPowerSaveExceptIdle = new ArraySet<>();
    final ArraySet<String> mAllowInPowerSave = new ArraySet<>();
    final ArraySet<String> mAllowInDataUsageSave = new ArraySet<>();
    final ArraySet<String> mAllowUnthrottledLocation = new ArraySet<>();
    final ArraySet<String> mAllowIgnoreLocationSettings = new ArraySet<>();
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
    final ArrayMap<String, ArraySet<String>> mProductServicesPrivAppPermissions = new ArrayMap<>();
    final ArrayMap<String, ArraySet<String>> mProductServicesPrivAppDenyPermissions = new ArrayMap<>();
    final ArrayMap<String, ArrayMap<String, Boolean>> mOemPermissions = new ArrayMap<>();
    final ArrayMap<String, ArraySet<String>> mAllowedAssociations = new ArrayMap<>();
    private final ArraySet<String> mBugreportWhitelistedPackages = new ArraySet<>();

    /* loaded from: classes3.dex */
    public static final class SharedLibraryEntry {
        public final String[] dependencies;
        public final String filename;
        public final String name;

        SharedLibraryEntry(String name, String filename, String[] dependencies) {
            this.name = name;
            this.filename = filename;
            this.dependencies = dependencies;
        }
    }

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
        if (!isSystemProcess()) {
            Slog.wtf(TAG, "SystemConfig is being accessed by a process other than system_server.");
        }
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

    public ArrayList<PermissionManager.SplitPermissionInfo> getSplitPermissions() {
        return this.mSplitPermissions;
    }

    public ArrayMap<String, SharedLibraryEntry> getSharedLibraries() {
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

    public ArraySet<String> getAllowIgnoreLocationSettings() {
        return this.mAllowIgnoreLocationSettings;
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

    public ArraySet<String> getProductServicesPrivAppPermissions(String packageName) {
        return this.mProductServicesPrivAppPermissions.get(packageName);
    }

    public ArraySet<String> getProductServicesPrivAppDenyPermissions(String packageName) {
        return this.mProductServicesPrivAppDenyPermissions.get(packageName);
    }

    public Map<String, Boolean> getOemPermissions(String packageName) {
        Map<String, Boolean> oemPermissions = this.mOemPermissions.get(packageName);
        if (oemPermissions != null) {
            return oemPermissions;
        }
        return Collections.emptyMap();
    }

    public ArrayMap<String, ArraySet<String>> getAllowedAssociations() {
        return this.mAllowedAssociations;
    }

    public ArraySet<String> getBugreportWhitelistedPackages() {
        return this.mBugreportWhitelistedPackages;
    }

    SystemConfig() {
        readPermissions(Environment.buildPath(Environment.getRootDirectory(), "etc", "sysconfig"), -1);
        readPermissions(Environment.buildPath(Environment.getRootDirectory(), "etc", "permissions"), -1);
        int vendorPermissionFlag = Build.VERSION.FIRST_SDK_INT <= 27 ? 147 | 12 : 147;
        readPermissions(Environment.buildPath(Environment.getVendorDirectory(), "etc", "sysconfig"), vendorPermissionFlag);
        readPermissions(Environment.buildPath(Environment.getVendorDirectory(), "etc", "permissions"), vendorPermissionFlag);
        int odmPermissionFlag = vendorPermissionFlag;
        readPermissions(Environment.buildPath(Environment.getOdmDirectory(), "etc", "sysconfig"), odmPermissionFlag);
        readPermissions(Environment.buildPath(Environment.getOdmDirectory(), "etc", "permissions"), odmPermissionFlag);
        String skuProperty = SystemProperties.get(SKU_PROPERTY, "");
        if (!skuProperty.isEmpty()) {
            String skuDir = "sku_" + skuProperty;
            readPermissions(Environment.buildPath(Environment.getOdmDirectory(), "etc", "sysconfig", skuDir), odmPermissionFlag);
            readPermissions(Environment.buildPath(Environment.getOdmDirectory(), "etc", "permissions", skuDir), odmPermissionFlag);
        }
        readPermissions(Environment.buildPath(Environment.getOemDirectory(), "etc", "sysconfig"), 161);
        readPermissions(Environment.buildPath(Environment.getOemDirectory(), "etc", "permissions"), 161);
        readPermissions(Environment.buildPath(Environment.getProductDirectory(), "etc", "sysconfig"), -1);
        readPermissions(Environment.buildPath(Environment.getProductDirectory(), "etc", "permissions"), -1);
        readPermissions(Environment.buildPath(Environment.getProductServicesDirectory(), "etc", "sysconfig"), -1);
        readPermissions(Environment.buildPath(Environment.getProductServicesDirectory(), "etc", "permissions"), -1);
    }

    void readPermissions(File libraryDir, int permissionFlag) {
        File[] listFiles;
        if (!libraryDir.exists() || !libraryDir.isDirectory()) {
            if (permissionFlag == -1) {
                Slog.w(TAG, "No directory " + libraryDir + ", skipping");
            }
        } else if (libraryDir.canRead()) {
            File platformFile = null;
            for (File f : libraryDir.listFiles()) {
                if (f.isFile()) {
                    if (f.getPath().endsWith("etc/permissions/platform.xml")) {
                        platformFile = f;
                    } else if (!f.getPath().endsWith(".xml")) {
                        Slog.i(TAG, "Non-xml file " + f + " in " + libraryDir + " directory, ignoring");
                    } else if (f.canRead()) {
                        readPermissionsFromXml(f, permissionFlag);
                    } else {
                        Slog.w(TAG, "Permissions library file " + f + " cannot be read");
                    }
                }
            }
            if (platformFile != null) {
                readPermissionsFromXml(platformFile, permissionFlag);
            }
        } else {
            Slog.w(TAG, "Directory " + libraryDir + " cannot be read");
        }
    }

    private void logNotAllowedInPartition(String name, File permFile, XmlPullParser parser) {
        Slog.w(TAG, "<" + name + "> not allowed in partition of " + permFile + " at " + parser.getPositionDescription());
    }

    /* JADX WARN: Code restructure failed: missing block: B:101:0x017b, code lost:
        if (r13.equals("system-user-whitelisted-app") == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x017d, code lost:
        r14 = 14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x0187, code lost:
        if (r13.equals("backup-transport-whitelisted-service") == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x0189, code lost:
        r14 = 17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x0193, code lost:
        if (r13.equals("hidden-api-whitelisted-app") == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x0195, code lost:
        r14 = 22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:10:0x002b, code lost:
        if (r12 != 2) goto L422;
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x019f, code lost:
        if (r13.equals("library") == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x01a1, code lost:
        r14 = 4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x01aa, code lost:
        if (r13.equals(android.net.wifi.WifiConfiguration.GroupCipher.varName) == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x01ac, code lost:
        r14 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x01b6, code lost:
        if (r13.equals("permission") == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x01b8, code lost:
        r14 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x01c0, code lost:
        if (r13.equals("allow-ignore-location-settings") == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x01c2, code lost:
        r14 = 11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x01cb, code lost:
        if (r13.equals("allow-in-power-save-except-idle") == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x01cd, code lost:
        r14 = 7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:125:0x01d6, code lost:
        if (r13.equals("unavailable-feature") == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:126:0x01d8, code lost:
        r14 = 6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:128:0x01e1, code lost:
        if (r13.equals("system-user-blacklisted-app") == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:129:0x01e3, code lost:
        r14 = 15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0038, code lost:
        if (r10.getName().equals("permissions") != false) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x01ec, code lost:
        if (r13.equals("feature") == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:132:0x01ee, code lost:
        r14 = 5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x01f6, code lost:
        if (r13.equals("allow-association") == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:135:0x01f8, code lost:
        r14 = 23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x0201, code lost:
        if (r13.equals("disabled-until-used-preinstalled-carrier-app") == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:138:0x0203, code lost:
        r14 = 19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x020c, code lost:
        if (r13.equals("allow-in-power-save") == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:141:0x020e, code lost:
        r14 = '\b';
     */
    /* JADX WARN: Code restructure failed: missing block: B:143:0x0217, code lost:
        if (r13.equals("allow-unthrottled-location") == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x0219, code lost:
        r14 = '\n';
     */
    /* JADX WARN: Code restructure failed: missing block: B:145:0x021c, code lost:
        r14 = 65535;
     */
    /* JADX WARN: Code restructure failed: missing block: B:146:0x021d, code lost:
        r24 = r12;
        r25 = r7;
        r26 = r4;
        r27 = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:147:0x0235, code lost:
        switch(r14) {
            case 0: goto L285;
            case 1: goto L278;
            case 2: goto L261;
            case 3: goto L257;
            case 4: goto L242;
            case 5: goto L229;
            case 6: goto L221;
            case 7: goto L213;
            case 8: goto L205;
            case 9: goto L197;
            case 10: goto L189;
            case 11: goto L181;
            case 12: goto L173;
            case 13: goto L165;
            case 14: goto L157;
            case 15: goto L149;
            case 16: goto L139;
            case 17: goto L128;
            case 18: goto L115;
            case 19: goto L107;
            case 20: goto L89;
            case 21: goto L86;
            case 22: goto L78;
            case 23: goto L65;
            case 24: goto L60;
            default: goto L55;
        };
     */
    /* JADX WARN: Code restructure failed: missing block: B:148:0x0238, code lost:
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:149:0x023c, code lost:
        r3 = r10.getAttributeValue(null, "package");
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0044, code lost:
        if (r10.getName().equals("config") == false) goto L408;
     */
    /* JADX WARN: Code restructure failed: missing block: B:150:0x0240, code lost:
        if (r3 != null) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:151:0x0242, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without package in " + r30 + " at " + r10.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:152:0x0265, code lost:
        r29.mBugreportWhitelistedPackages.add(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:153:0x026a, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:154:0x0271, code lost:
        if (r11 == false) goto L77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:155:0x0273, code lost:
        r3 = r10.getAttributeValue(null, "target");
     */
    /* JADX WARN: Code restructure failed: missing block: B:156:0x027a, code lost:
        if (r3 != null) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:157:0x027c, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without target in " + r30 + " at " + r10.getPositionDescription());
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:158:0x02a7, code lost:
        r7 = r10.getAttributeValue(null, "allowed");
     */
    /* JADX WARN: Code restructure failed: missing block: B:159:0x02ad, code lost:
        if (r7 != null) goto L72;
     */
    /* JADX WARN: Code restructure failed: missing block: B:160:0x02af, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without allowed in " + r30 + " at " + r10.getPositionDescription());
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:161:0x02da, code lost:
        r4 = r3.intern();
        r4 = r7.intern();
        r7 = r29.mAllowedAssociations.get(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:162:0x02eb, code lost:
        if (r7 != null) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:163:0x02ed, code lost:
        r7 = new android.util.ArraySet<>();
        r29.mAllowedAssociations.put(r4, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x02f8, code lost:
        android.util.Slog.i(com.android.server.SystemConfig.TAG, "Adding association: " + r4 + " <- " + r4);
        r7.add(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:165:0x0319, code lost:
        logNotAllowedInPartition(r13, r30, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x031c, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x0323, code lost:
        if (r22 == false) goto L85;
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x0325, code lost:
        r3 = r10.getAttributeValue(null, "package");
     */
    /* JADX WARN: Code restructure failed: missing block: B:169:0x0329, code lost:
        if (r3 != null) goto L84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x032b, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without package in " + r30 + " at " + r10.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:171:0x034e, code lost:
        r29.mHiddenApiPackageWhitelist.add(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:173:0x0354, code lost:
        logNotAllowedInPartition(r13, r30, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:174:0x0357, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:175:0x035e, code lost:
        if (r21 == false) goto L88;
     */
    /* JADX WARN: Code restructure failed: missing block: B:176:0x0360, code lost:
        readOemPermissions(r10);
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:177:0x0367, code lost:
        logNotAllowedInPartition(r13, r30, r10);
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:178:0x0371, code lost:
        if (r20 == false) goto L106;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x006e, code lost:
        throw new org.xmlpull.v1.XmlPullParserException("Unexpected start tag in " + r30 + ": found " + r10.getName() + ", expected 'permissions' or 'config'");
     */
    /* JADX WARN: Code restructure failed: missing block: B:180:0x0392, code lost:
        if (r30.toPath().startsWith(android.os.Environment.getVendorDirectory().toPath() + "/") != false) goto L105;
     */
    /* JADX WARN: Code restructure failed: missing block: B:182:0x03b3, code lost:
        if (r30.toPath().startsWith(android.os.Environment.getOdmDirectory().toPath() + "/") == false) goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:184:0x03b6, code lost:
        r3 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:185:0x03b8, code lost:
        r3 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:186:0x03b9, code lost:
        r4 = r30.toPath().startsWith(android.os.Environment.getProductDirectory().toPath() + "/");
        r7 = r30.toPath().startsWith(android.os.Environment.getProductServicesDirectory().toPath() + "/");
     */
    /* JADX WARN: Code restructure failed: missing block: B:187:0x03f7, code lost:
        if (r3 == false) goto L100;
     */
    /* JADX WARN: Code restructure failed: missing block: B:188:0x03f9, code lost:
        readPrivAppPermissions(r10, r29.mVendorPrivAppPermissions, r29.mVendorPrivAppDenyPermissions);
     */
    /* JADX WARN: Code restructure failed: missing block: B:189:0x0401, code lost:
        if (r4 == false) goto L102;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x006f, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:190:0x0403, code lost:
        readPrivAppPermissions(r10, r29.mProductPrivAppPermissions, r29.mProductPrivAppDenyPermissions);
     */
    /* JADX WARN: Code restructure failed: missing block: B:191:0x040b, code lost:
        if (r7 == false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:192:0x040d, code lost:
        readPrivAppPermissions(r10, r29.mProductServicesPrivAppPermissions, r29.mProductServicesPrivAppDenyPermissions);
     */
    /* JADX WARN: Code restructure failed: missing block: B:193:0x0415, code lost:
        readPrivAppPermissions(r10, r29.mPrivAppPermissions, r29.mPrivAppDenyPermissions);
     */
    /* JADX WARN: Code restructure failed: missing block: B:194:0x041c, code lost:
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:195:0x0420, code lost:
        logNotAllowedInPartition(r13, r30, r10);
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:196:0x042a, code lost:
        if (r19 == false) goto L114;
     */
    /* JADX WARN: Code restructure failed: missing block: B:197:0x042c, code lost:
        r3 = r10.getAttributeValue(null, "package");
     */
    /* JADX WARN: Code restructure failed: missing block: B:198:0x0430, code lost:
        if (r3 != null) goto L113;
     */
    /* JADX WARN: Code restructure failed: missing block: B:199:0x0432, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without package in " + r30 + " at " + r10.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0070, code lost:
        r3 = r0;
        r25 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x0455, code lost:
        r29.mDisabledUntilUsedPreinstalledCarrierApps.add(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:202:0x045b, code lost:
        logNotAllowedInPartition(r13, r30, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:203:0x045e, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:204:0x0465, code lost:
        if (r19 == false) goto L127;
     */
    /* JADX WARN: Code restructure failed: missing block: B:205:0x0467, code lost:
        r3 = r10.getAttributeValue(null, "package");
        r7 = r10.getAttributeValue(null, "carrierAppPackage");
     */
    /* JADX WARN: Code restructure failed: missing block: B:206:0x0471, code lost:
        if (r3 == null) goto L126;
     */
    /* JADX WARN: Code restructure failed: missing block: B:207:0x0473, code lost:
        if (r7 != null) goto L120;
     */
    /* JADX WARN: Code restructure failed: missing block: B:209:0x0476, code lost:
        r4 = r29.mDisabledUntilUsedPreinstalledCarrierAssociatedApps.get(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0077, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:210:0x047e, code lost:
        if (r4 != null) goto L123;
     */
    /* JADX WARN: Code restructure failed: missing block: B:211:0x0480, code lost:
        r4 = new java.util.ArrayList();
        r29.mDisabledUntilUsedPreinstalledCarrierAssociatedApps.put(r7, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:212:0x048b, code lost:
        r4.add(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:213:0x048f, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without package or carrierAppPackage in " + r30 + " at " + r10.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:215:0x04b4, code lost:
        logNotAllowedInPartition(r13, r30, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:216:0x04b7, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:217:0x04be, code lost:
        if (r17 == false) goto L138;
     */
    /* JADX WARN: Code restructure failed: missing block: B:218:0x04c0, code lost:
        r3 = r10.getAttributeValue(null, "service");
     */
    /* JADX WARN: Code restructure failed: missing block: B:219:0x04c7, code lost:
        if (r3 != null) goto L134;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0078, code lost:
        r3 = r0;
        r26 = "Got exception parsing permissions.";
        r25 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:220:0x04c9, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without service in " + r30 + " at " + r10.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:221:0x04ee, code lost:
        r7 = android.content.ComponentName.unflattenFromString(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:222:0x04f2, code lost:
        if (r7 != null) goto L137;
     */
    /* JADX WARN: Code restructure failed: missing block: B:223:0x04f4, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> with invalid service name " + r3 + " in " + r30 + " at " + r10.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:224:0x0521, code lost:
        r29.mBackupTransportWhitelist.add(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:226:0x0527, code lost:
        logNotAllowedInPartition(r13, r30, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:227:0x052a, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:228:0x0531, code lost:
        if (r19 == false) goto L148;
     */
    /* JADX WARN: Code restructure failed: missing block: B:229:0x0533, code lost:
        r3 = r10.getAttributeValue(null, "package");
        r7 = r10.getAttributeValue(null, "class");
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0081, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:230:0x053d, code lost:
        if (r3 != null) goto L145;
     */
    /* JADX WARN: Code restructure failed: missing block: B:231:0x053f, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without package in " + r30 + " at " + r10.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:232:0x0562, code lost:
        if (r7 != null) goto L147;
     */
    /* JADX WARN: Code restructure failed: missing block: B:233:0x0564, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without class in " + r30 + " at " + r10.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:234:0x0589, code lost:
        r29.mDefaultVrComponents.add(new android.content.ComponentName(r3, r7));
     */
    /* JADX WARN: Code restructure failed: missing block: B:236:0x0594, code lost:
        logNotAllowedInPartition(r13, r30, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:237:0x0597, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:238:0x059e, code lost:
        if (r19 == false) goto L156;
     */
    /* JADX WARN: Code restructure failed: missing block: B:239:0x05a0, code lost:
        r3 = r10.getAttributeValue(null, "package");
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0082, code lost:
        r3 = r0;
        r25 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:240:0x05a4, code lost:
        if (r3 != null) goto L155;
     */
    /* JADX WARN: Code restructure failed: missing block: B:241:0x05a6, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without package in " + r30 + " at " + r10.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:242:0x05c9, code lost:
        r29.mSystemUserBlacklistedApps.add(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:244:0x05cf, code lost:
        logNotAllowedInPartition(r13, r30, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:245:0x05d2, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:246:0x05d9, code lost:
        if (r19 == false) goto L164;
     */
    /* JADX WARN: Code restructure failed: missing block: B:247:0x05db, code lost:
        r3 = r10.getAttributeValue(null, "package");
     */
    /* JADX WARN: Code restructure failed: missing block: B:248:0x05df, code lost:
        if (r3 != null) goto L163;
     */
    /* JADX WARN: Code restructure failed: missing block: B:249:0x05e1, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without package in " + r30 + " at " + r10.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:250:0x0604, code lost:
        r29.mSystemUserWhitelistedApps.add(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:252:0x060a, code lost:
        logNotAllowedInPartition(r13, r30, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:253:0x060d, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:254:0x0614, code lost:
        if (r19 == false) goto L172;
     */
    /* JADX WARN: Code restructure failed: missing block: B:255:0x0616, code lost:
        r3 = r10.getAttributeValue(null, "package");
     */
    /* JADX WARN: Code restructure failed: missing block: B:256:0x061a, code lost:
        if (r3 != null) goto L171;
     */
    /* JADX WARN: Code restructure failed: missing block: B:257:0x061c, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without package in " + r30 + " at " + r10.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:258:0x063f, code lost:
        r29.mLinkedApps.add(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x008a, code lost:
        if (r31 != (-1)) goto L403;
     */
    /* JADX WARN: Code restructure failed: missing block: B:260:0x0645, code lost:
        logNotAllowedInPartition(r13, r30, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:261:0x0648, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:262:0x064f, code lost:
        if (r15 == false) goto L180;
     */
    /* JADX WARN: Code restructure failed: missing block: B:263:0x0651, code lost:
        r3 = r10.getAttributeValue(null, "action");
     */
    /* JADX WARN: Code restructure failed: missing block: B:264:0x0657, code lost:
        if (r3 != null) goto L179;
     */
    /* JADX WARN: Code restructure failed: missing block: B:265:0x0659, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without action in " + r30 + " at " + r10.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:266:0x067e, code lost:
        r29.mAllowImplicitBroadcasts.add(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:268:0x0684, code lost:
        logNotAllowedInPartition(r13, r30, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:269:0x0687, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x008c, code lost:
        r15 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:270:0x068e, code lost:
        if (r15 == false) goto L188;
     */
    /* JADX WARN: Code restructure failed: missing block: B:271:0x0690, code lost:
        r3 = r10.getAttributeValue(null, "package");
     */
    /* JADX WARN: Code restructure failed: missing block: B:272:0x0694, code lost:
        if (r3 != null) goto L187;
     */
    /* JADX WARN: Code restructure failed: missing block: B:273:0x0696, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without package in " + r30 + " at " + r10.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:274:0x06b9, code lost:
        r29.mAllowIgnoreLocationSettings.add(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:276:0x06bf, code lost:
        logNotAllowedInPartition(r13, r30, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:277:0x06c2, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:278:0x06c9, code lost:
        if (r15 == false) goto L196;
     */
    /* JADX WARN: Code restructure failed: missing block: B:279:0x06cb, code lost:
        r3 = r10.getAttributeValue(null, "package");
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x008e, code lost:
        r15 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:280:0x06cf, code lost:
        if (r3 != null) goto L195;
     */
    /* JADX WARN: Code restructure failed: missing block: B:281:0x06d1, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without package in " + r30 + " at " + r10.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:282:0x06f4, code lost:
        r29.mAllowUnthrottledLocation.add(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:284:0x06fa, code lost:
        logNotAllowedInPartition(r13, r30, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:285:0x06fd, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:286:0x0704, code lost:
        if (r15 == false) goto L204;
     */
    /* JADX WARN: Code restructure failed: missing block: B:287:0x0706, code lost:
        r3 = r10.getAttributeValue(null, "package");
     */
    /* JADX WARN: Code restructure failed: missing block: B:288:0x070a, code lost:
        if (r3 != null) goto L203;
     */
    /* JADX WARN: Code restructure failed: missing block: B:289:0x070c, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without package in " + r30 + " at " + r10.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:290:0x072f, code lost:
        r29.mAllowInDataUsageSave.add(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x0735, code lost:
        logNotAllowedInPartition(r13, r30, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:293:0x0738, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x073f, code lost:
        if (r15 == false) goto L212;
     */
    /* JADX WARN: Code restructure failed: missing block: B:295:0x0741, code lost:
        r3 = r10.getAttributeValue(null, "package");
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x0745, code lost:
        if (r3 != null) goto L211;
     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x0747, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without package in " + r30 + " at " + r10.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x076a, code lost:
        r29.mAllowInPowerSave.add(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0091, code lost:
        if ((r31 & 2) == 0) goto L402;
     */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x0770, code lost:
        logNotAllowedInPartition(r13, r30, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x0773, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:302:0x077a, code lost:
        if (r15 == false) goto L220;
     */
    /* JADX WARN: Code restructure failed: missing block: B:303:0x077c, code lost:
        r3 = r10.getAttributeValue(null, "package");
     */
    /* JADX WARN: Code restructure failed: missing block: B:304:0x0780, code lost:
        if (r3 != null) goto L219;
     */
    /* JADX WARN: Code restructure failed: missing block: B:305:0x0782, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without package in " + r30 + " at " + r10.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:306:0x07a5, code lost:
        r29.mAllowInPowerSaveExceptIdle.add(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:308:0x07ab, code lost:
        logNotAllowedInPartition(r13, r30, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x07ae, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0093, code lost:
        r16 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x07b5, code lost:
        if (r17 == false) goto L228;
     */
    /* JADX WARN: Code restructure failed: missing block: B:311:0x07b7, code lost:
        r3 = r10.getAttributeValue(null, "name");
     */
    /* JADX WARN: Code restructure failed: missing block: B:312:0x07bb, code lost:
        if (r3 != null) goto L227;
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x07bd, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without name in " + r30 + " at " + r10.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:314:0x07e2, code lost:
        r29.mUnavailableFeatures.add(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:316:0x07e8, code lost:
        logNotAllowedInPartition(r13, r30, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x07eb, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:319:0x07f4, code lost:
        if (r17 == false) goto L241;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0096, code lost:
        r16 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:320:0x07f6, code lost:
        r3 = r10.getAttributeValue(null, "name");
        r7 = com.android.internal.util.XmlUtils.readIntAttribute(r10, "version", 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:321:0x0802, code lost:
        if (r27 != false) goto L240;
     */
    /* JADX WARN: Code restructure failed: missing block: B:322:0x0804, code lost:
        r9 = true;
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x0808, code lost:
        r9 = r10.getAttributeValue(null, "notLowRam");
        r23 = 1;
        r9 = !"true".equals(r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:324:0x081a, code lost:
        if (r3 != null) goto L238;
     */
    /* JADX WARN: Code restructure failed: missing block: B:325:0x081c, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without name in " + r30 + " at " + r10.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:326:0x083f, code lost:
        if (r9 == false) goto L236;
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x0841, code lost:
        addFeature(r3, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:329:0x0845, code lost:
        r23 = 1;
        logNotAllowedInPartition(r13, r30, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:330:0x084a, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x084f, code lost:
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:332:0x0853, code lost:
        if (r16 == false) goto L256;
     */
    /* JADX WARN: Code restructure failed: missing block: B:333:0x0855, code lost:
        r3 = r10.getAttributeValue(null, "name");
        r7 = r10.getAttributeValue(null, android.content.ContentResolver.SCHEME_FILE);
        r9 = r10.getAttributeValue(null, "dependency");
     */
    /* JADX WARN: Code restructure failed: missing block: B:334:0x0865, code lost:
        if (r3 != null) goto L249;
     */
    /* JADX WARN: Code restructure failed: missing block: B:335:0x0867, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without name in " + r30 + " at " + r10.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:336:0x088a, code lost:
        if (r7 != null) goto L251;
     */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x088c, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without file in " + r30 + " at " + r10.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:339:0x08b3, code lost:
        if (r9 != null) goto L255;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x009a, code lost:
        if ((r31 & 1) == 0) goto L401;
     */
    /* JADX WARN: Code restructure failed: missing block: B:340:0x08b5, code lost:
        r12 = new java.lang.String[0];
     */
    /* JADX WARN: Code restructure failed: missing block: B:341:0x08b9, code lost:
        r12 = r9.split(android.provider.SettingsStringUtil.DELIMITER);
     */
    /* JADX WARN: Code restructure failed: missing block: B:342:0x08bf, code lost:
        r4 = new com.android.server.SystemConfig.SharedLibraryEntry(r3, r7, r12);
        r29.mSharedLibraries.put(r3, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:344:0x08c8, code lost:
        logNotAllowedInPartition(r13, r30, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x08cb, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:346:0x08d0, code lost:
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:347:0x08d2, code lost:
        if (r18 == false) goto L260;
     */
    /* JADX WARN: Code restructure failed: missing block: B:348:0x08d4, code lost:
        readSplitPermission(r10, r30);
     */
    /* JADX WARN: Code restructure failed: missing block: B:349:0x08d9, code lost:
        logNotAllowedInPartition(r13, r30, r10);
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x009c, code lost:
        r17 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:350:0x08e1, code lost:
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:351:0x08e5, code lost:
        if (r18 == false) goto L277;
     */
    /* JADX WARN: Code restructure failed: missing block: B:352:0x08e7, code lost:
        r3 = r10.getAttributeValue(null, "name");
     */
    /* JADX WARN: Code restructure failed: missing block: B:353:0x08eb, code lost:
        if (r3 != null) goto L266;
     */
    /* JADX WARN: Code restructure failed: missing block: B:354:0x08ed, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without name in " + r30 + " at " + r10.getPositionDescription());
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x0914, code lost:
        r7 = r10.getAttributeValue(null, "uid");
     */
    /* JADX WARN: Code restructure failed: missing block: B:356:0x091b, code lost:
        if (r7 != null) goto L269;
     */
    /* JADX WARN: Code restructure failed: missing block: B:357:0x091d, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without uid in " + r30 + " at " + r10.getPositionDescription());
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:358:0x0946, code lost:
        r9 = android.os.Process.getUidForName(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:359:0x094a, code lost:
        if (r9 >= 0) goto L272;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x009f, code lost:
        r17 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:360:0x094c, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> with unknown uid \"" + r7 + "  in " + r30 + " at " + r10.getPositionDescription());
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x097d, code lost:
        r4 = r3.intern();
        r4 = r29.mSystemPermissions.get(r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:362:0x098a, code lost:
        if (r4 != null) goto L275;
     */
    /* JADX WARN: Code restructure failed: missing block: B:363:0x098c, code lost:
        r4 = new android.util.ArraySet<>();
        r29.mSystemPermissions.put(r9, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:364:0x0997, code lost:
        r4.add(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:365:0x099c, code lost:
        logNotAllowedInPartition(r13, r30, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:366:0x099f, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:367:0x09a4, code lost:
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:368:0x09a8, code lost:
        if (r18 == false) goto L284;
     */
    /* JADX WARN: Code restructure failed: missing block: B:369:0x09aa, code lost:
        r3 = r10.getAttributeValue(null, "name");
     */
    /* JADX WARN: Code restructure failed: missing block: B:370:0x09ae, code lost:
        if (r3 != null) goto L283;
     */
    /* JADX WARN: Code restructure failed: missing block: B:371:0x09b0, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without name in " + r30 + " at " + r10.getPositionDescription());
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:372:0x09d7, code lost:
        readPermission(r10, r3.intern());
     */
    /* JADX WARN: Code restructure failed: missing block: B:373:0x09e1, code lost:
        logNotAllowedInPartition(r13, r30, r10);
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:374:0x09e8, code lost:
        r23 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:375:0x09ea, code lost:
        if (r15 == false) goto L293;
     */
    /* JADX WARN: Code restructure failed: missing block: B:376:0x09ec, code lost:
        r3 = r10.getAttributeValue(null, "gid");
     */
    /* JADX WARN: Code restructure failed: missing block: B:377:0x09f2, code lost:
        if (r3 == null) goto L292;
     */
    /* JADX WARN: Code restructure failed: missing block: B:378:0x09f4, code lost:
        r4 = android.os.Process.getGidForName(r3);
        r29.mGlobalGids = com.android.internal.util.ArrayUtils.appendInt(r29.mGlobalGids, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:379:0x0a01, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "<" + r13 + "> without gid in " + r30 + " at " + r10.getPositionDescription());
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00a3, code lost:
        if ((r31 & 4) == 0) goto L400;
     */
    /* JADX WARN: Code restructure failed: missing block: B:381:0x0a26, code lost:
        logNotAllowedInPartition(r13, r30, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:382:0x0a29, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:383:0x0a2d, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, "Tag " + r13 + " is unknown in " + r30 + " at " + r10.getPositionDescription());
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:384:0x0a56, code lost:
        r14 = r23;
        r12 = r24;
        r7 = r25;
        r4 = r26;
        r8 = r27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:386:0x0a74, code lost:
        throw new org.xmlpull.v1.XmlPullParserException("No start tag found");
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00a5, code lost:
        r18 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:396:0x0a8f, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, r26, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:399:0x0a9a, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, r4, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00a8, code lost:
        r18 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:400:0x0a9e, code lost:
        libcore.io.IoUtils.closeQuietly(r25);
     */
    /* JADX WARN: Code restructure failed: missing block: B:402:0x0aaf, code lost:
        if (android.os.SystemProperties.get("ro.baseband").equals("apq") != false) goto L375;
     */
    /* JADX WARN: Code restructure failed: missing block: B:403:0x0ab1, code lost:
        r29.mUnavailableFeatures.add(android.content.pm.PackageManager.FEATURE_SENSOR_ACCELEROMETER);
        r29.mUnavailableFeatures.add(android.content.pm.PackageManager.FEATURE_SENSOR_COMPASS);
        r29.mUnavailableFeatures.add(android.content.pm.PackageManager.FEATURE_SENSOR_LIGHT);
        r29.mUnavailableFeatures.add(android.content.pm.PackageManager.FEATURE_SENSOR_PROXIMITY);
        r29.mUnavailableFeatures.add(android.content.pm.PackageManager.FEATURE_LOCATION_NETWORK);
        r29.mUnavailableFeatures.add(android.content.pm.PackageManager.FEATURE_SCREEN_PORTRAIT);
        r29.mUnavailableFeatures.add(android.content.pm.PackageManager.FEATURE_PICTURE_IN_PICTURE);
     */
    /* JADX WARN: Code restructure failed: missing block: B:405:0x0ae6, code lost:
        if (android.os.storage.StorageManager.isFileEncryptedNativeOnly() == false) goto L393;
     */
    /* JADX WARN: Code restructure failed: missing block: B:406:0x0ae8, code lost:
        r4 = 0;
        addFeature(android.content.pm.PackageManager.FEATURE_FILE_BASED_ENCRYPTION, 0);
        addFeature(android.content.pm.PackageManager.FEATURE_SECURELY_REMOVES_USERS, 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:407:0x0af4, code lost:
        r4 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:409:0x0af9, code lost:
        if (android.os.storage.StorageManager.hasAdoptable() != false) goto L381;
     */
    /* JADX WARN: Code restructure failed: missing block: B:410:0x0afb, code lost:
        addFeature(android.content.pm.PackageManager.FEATURE_ADOPTABLE_STORAGE, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:412:0x0b04, code lost:
        if (android.app.ActivityManager.isLowRamDeviceStatic() == false) goto L392;
     */
    /* JADX WARN: Code restructure failed: missing block: B:413:0x0b06, code lost:
        addFeature(android.content.pm.PackageManager.FEATURE_RAM_LOW, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:414:0x0b0c, code lost:
        addFeature(android.content.pm.PackageManager.FEATURE_RAM_NORMAL, r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:415:0x0b11, code lost:
        r3 = r29.mUnavailableFeatures.iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:417:0x0b1b, code lost:
        if (r3.hasNext() != false) goto L388;
     */
    /* JADX WARN: Code restructure failed: missing block: B:418:0x0b1d, code lost:
        r4 = r3.next();
        removeFeature(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:419:0x0b27, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00ac, code lost:
        if ((r31 & 8) == 0) goto L399;
     */
    /* JADX WARN: Code restructure failed: missing block: B:422:0x0b2a, code lost:
        libcore.io.IoUtils.closeQuietly(r25);
     */
    /* JADX WARN: Code restructure failed: missing block: B:423:0x0b2d, code lost:
        throw r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00ae, code lost:
        r19 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00b1, code lost:
        r19 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00b5, code lost:
        if ((r31 & 16) == 0) goto L398;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00b7, code lost:
        r20 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00ba, code lost:
        r20 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00be, code lost:
        if ((r31 & 32) == 0) goto L397;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00c0, code lost:
        r21 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00c3, code lost:
        r21 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00c7, code lost:
        if ((r31 & 64) == 0) goto L396;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00c9, code lost:
        r22 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00cc, code lost:
        r22 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00d0, code lost:
        if ((r31 & 128) == 0) goto L395;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00d2, code lost:
        r11 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00d4, code lost:
        r11 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00d5, code lost:
        com.android.internal.util.XmlUtils.nextElement(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00dc, code lost:
        if (r10.getEventType() != r14) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00df, code lost:
        libcore.io.IoUtils.closeQuietly(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x00e8, code lost:
        r13 = r10.getName();
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00ec, code lost:
        if (r13 != null) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00ee, code lost:
        com.android.internal.util.XmlUtils.skipCurrentTag(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x00f7, code lost:
        switch(r13.hashCode()) {
            case -2040330235: goto L363;
            case -1882490007: goto L360;
            case -1005864890: goto L357;
            case -980620291: goto L354;
            case -979207434: goto L351;
            case -851582420: goto L348;
            case -828905863: goto L345;
            case -642819164: goto L342;
            case -560717308: goto L339;
            case -517618225: goto L336;
            case 98629247: goto L333;
            case 166208699: goto L330;
            case 180165796: goto L327;
            case 347247519: goto L324;
            case 508457430: goto L321;
            case 802332808: goto L318;
            case 953292141: goto L315;
            case 1044015374: goto L312;
            case 1121420326: goto L309;
            case 1269564002: goto L306;
            case 1567330472: goto L303;
            case 1633270165: goto L300;
            case 1723146313: goto L297;
            case 1723586945: goto L294;
            case 1954925533: goto L50;
            default: goto L366;
        };
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0102, code lost:
        if (r13.equals("allow-implicit-broadcast") == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x0104, code lost:
        r14 = '\f';
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x010e, code lost:
        if (r13.equals("bugreport-whitelisted") == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x0110, code lost:
        r14 = 24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x011b, code lost:
        if (r13.equals("privapp-permissions") == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x011d, code lost:
        r14 = 20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x0127, code lost:
        if (r13.equals("disabled-until-used-preinstalled-carrier-associated-app") == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x0129, code lost:
        r14 = 18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x0133, code lost:
        if (r13.equals("default-enabled-vr-app") == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x0135, code lost:
        r14 = 16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x0140, code lost:
        if (r13.equals("split-permission") == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x0142, code lost:
        r14 = 3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x014b, code lost:
        if (r13.equals("app-link") == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:90:0x014d, code lost:
        r14 = '\r';
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x0157, code lost:
        if (r13.equals("oem-permissions") == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:93:0x0159, code lost:
        r14 = 21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x0163, code lost:
        if (r13.equals("assign-permission") == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x0165, code lost:
        r14 = 2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x016e, code lost:
        if (r13.equals("allow-in-data-usage-save") == false) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x0170, code lost:
        r14 = '\t';
     */
    /* JADX WARN: Not initialized variable reg: 26, insn: 0x0a7a: MOVE  (r4 I:??[OBJECT, ARRAY]) = (r26 I:??[OBJECT, ARRAY]), block:B:390:0x0a79 */
    /* JADX WARN: Removed duplicated region for block: B:403:0x0ab1  */
    /* JADX WARN: Removed duplicated region for block: B:406:0x0ae8  */
    /* JADX WARN: Removed duplicated region for block: B:407:0x0af4  */
    /* JADX WARN: Removed duplicated region for block: B:410:0x0afb  */
    /* JADX WARN: Removed duplicated region for block: B:413:0x0b06  */
    /* JADX WARN: Removed duplicated region for block: B:414:0x0b0c  */
    /* JADX WARN: Removed duplicated region for block: B:418:0x0b1d A[LOOP:2: B:416:0x0b17->B:418:0x0b1d, LOOP_END] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void readPermissionsFromXml(java.io.File r30, int r31) {
        /*
            Method dump skipped, instructions count: 3042
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.SystemConfig.readPermissionsFromXml(java.io.File, int):void");
    }

    private void addFeature(String name, int version) {
        if (PackageManager.FEATURE_WIFI_AWARE.equals(name) && !SystemProperties.getBoolean("ro.vendor.wlan.aware", true)) {
            Slog.w(TAG, "<" + name + "> not supported due to ro.vendor.wlan.aware is false");
            return;
        }
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
            if ("permission".equals(name)) {
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
            if ("permission".equals(name)) {
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

    private void readSplitPermission(XmlPullParser parser, File permFile) throws IOException, XmlPullParserException {
        String splitPerm = parser.getAttributeValue(null, "name");
        if (splitPerm == null) {
            Slog.w(TAG, "<split-permission> without name in " + permFile + " at " + parser.getPositionDescription());
            XmlUtils.skipCurrentTag(parser);
            return;
        }
        String targetSdkStr = parser.getAttributeValue(null, "targetSdk");
        int targetSdk = 10001;
        if (!TextUtils.isEmpty(targetSdkStr)) {
            try {
                targetSdk = Integer.parseInt(targetSdkStr);
            } catch (NumberFormatException e) {
                Slog.w(TAG, "<split-permission> targetSdk not an integer in " + permFile + " at " + parser.getPositionDescription());
                XmlUtils.skipCurrentTag(parser);
                return;
            }
        }
        int depth = parser.getDepth();
        List<String> newPermissions = new ArrayList<>();
        while (XmlUtils.nextElementWithin(parser, depth)) {
            String name = parser.getName();
            if ("new-permission".equals(name)) {
                String newName = parser.getAttributeValue(null, "name");
                if (TextUtils.isEmpty(newName)) {
                    Slog.w(TAG, "name is required for <new-permission> in " + parser.getPositionDescription());
                } else {
                    newPermissions.add(newName);
                }
            } else {
                XmlUtils.skipCurrentTag(parser);
            }
        }
        if (!newPermissions.isEmpty()) {
            this.mSplitPermissions.add(new PermissionManager.SplitPermissionInfo(splitPerm, newPermissions, targetSdk));
        }
    }

    private static boolean isSystemProcess() {
        return Process.myUid() == 1000;
    }
}
