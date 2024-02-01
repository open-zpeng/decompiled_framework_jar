package android.os;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.net.wifi.WifiEnterpriseConfig;
import android.provider.Settings;
import android.provider.SettingsStringUtil;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import dalvik.system.VMRuntime;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class GraphicsEnvironment {
    private static final String ACTION_ANGLE_FOR_ANDROID = "android.app.action.ANGLE_FOR_ANDROID";
    private static final String ACTION_ANGLE_FOR_ANDROID_TOAST_MESSAGE = "android.app.action.ANGLE_FOR_ANDROID_TOAST_MESSAGE";
    private static final String ANGLE_RULES_FILE = "a4a_rules.json";
    private static final String ANGLE_TEMP_RULES = "debug.angle.rules";
    private static final boolean DEBUG = false;
    private static final int GAME_DRIVER_GLOBAL_OPT_IN_DEFAULT = 0;
    private static final int GAME_DRIVER_GLOBAL_OPT_IN_GAME_DRIVER = 1;
    private static final int GAME_DRIVER_GLOBAL_OPT_IN_OFF = 3;
    private static final int GAME_DRIVER_GLOBAL_OPT_IN_PRERELEASE_DRIVER = 2;
    private static final String GAME_DRIVER_SPHAL_LIBRARIES_FILENAME = "sphal_libraries.txt";
    private static final String GAME_DRIVER_WHITELIST_ALL = "*";
    private static final String INTENT_KEY_A4A_TOAST_MESSAGE = "A4A Toast Message";
    private static final String METADATA_DEVELOPER_DRIVER_ENABLE = "com.android.graphics.developerdriver.enable";
    private static final String METADATA_DRIVER_BUILD_TIME = "com.android.gamedriver.build_time";
    private static final String PROPERTY_GFX_DRIVER = "ro.gfx.driver.0";
    private static final String PROPERTY_GFX_DRIVER_BUILD_TIME = "ro.gfx.driver_build_time";
    private static final String PROPERTY_GFX_DRIVER_PRERELEASE = "ro.gfx.driver.1";
    private static final String SYSTEM_DRIVER_NAME = "system";
    private static final long SYSTEM_DRIVER_VERSION_CODE = 0;
    private static final String SYSTEM_DRIVER_VERSION_NAME = "";
    private static final String TAG = "GraphicsEnvironment";
    private static final int VULKAN_1_0 = 4194304;
    private static final int VULKAN_1_1 = 4198400;
    private ClassLoader mClassLoader;
    private String mDebugLayerPath;
    private String mLayerPath;
    private static final GraphicsEnvironment sInstance = new GraphicsEnvironment();
    private static final Map<OpenGlDriverChoice, String> sDriverMap = buildMap();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public enum OpenGlDriverChoice {
        DEFAULT,
        NATIVE,
        ANGLE
    }

    private static native boolean getShouldUseAngle(String str);

    public static native void hintActivityLaunch();

    private static native boolean isDebuggable();

    private static native void setAngleInfo(String str, String str2, String str3, FileDescriptor fileDescriptor, long j, long j2);

    private static native void setDebugLayers(String str);

    private static native void setDebugLayersGLES(String str);

    private static native void setDriverPathAndSphalLibraries(String str, String str2);

    private static native void setGpuStats(String str, String str2, long j, long j2, String str3, int i);

    private static native void setLayerPaths(ClassLoader classLoader, String str);

    public static GraphicsEnvironment getInstance() {
        return sInstance;
    }

    public void setup(Context context, Bundle coreSettings) {
        PackageManager pm = context.getPackageManager();
        String packageName = context.getPackageName();
        Trace.traceBegin(2L, "setupGpuLayers");
        setupGpuLayers(context, coreSettings, pm, packageName);
        Trace.traceEnd(2L);
        Trace.traceBegin(2L, "setupAngle");
        setupAngle(context, coreSettings, pm, packageName);
        Trace.traceEnd(2L);
        Trace.traceBegin(2L, "chooseDriver");
        if (!chooseDriver(context, coreSettings, pm, packageName)) {
            setGpuStats("system", "", 0L, SystemProperties.getLong(PROPERTY_GFX_DRIVER_BUILD_TIME, 0L), packageName, getVulkanVersion(pm));
        }
        Trace.traceEnd(2L);
    }

    public static boolean shouldUseAngle(Context context, Bundle coreSettings, String packageName) {
        if (packageName.isEmpty()) {
            Log.v(TAG, "No package name available yet, ANGLE should not be used");
            return false;
        }
        String devOptIn = getDriverForPkg(context, coreSettings, packageName);
        boolean whitelisted = checkAngleWhitelist(context, coreSettings, packageName);
        boolean requested = devOptIn.equals(sDriverMap.get(OpenGlDriverChoice.ANGLE));
        boolean useAngle = whitelisted || requested;
        if (useAngle) {
            if (whitelisted) {
                Log.v(TAG, "ANGLE whitelist includes " + packageName);
            }
            if (requested) {
                Log.v(TAG, "ANGLE developer option for " + packageName + ": " + devOptIn);
            }
            return true;
        }
        return false;
    }

    private static int getVulkanVersion(PackageManager pm) {
        if (pm.hasSystemFeature(PackageManager.FEATURE_VULKAN_HARDWARE_VERSION, VULKAN_1_1)) {
            return VULKAN_1_1;
        }
        if (pm.hasSystemFeature(PackageManager.FEATURE_VULKAN_HARDWARE_VERSION, 4194304)) {
            return 4194304;
        }
        return 0;
    }

    public void setLayerPaths(ClassLoader classLoader, String layerPath, String debugLayerPath) {
        this.mClassLoader = classLoader;
        this.mLayerPath = layerPath;
        this.mDebugLayerPath = debugLayerPath;
    }

    private static String getDebugLayerAppPaths(PackageManager pm, String app) {
        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(app, 131072);
            String abi = chooseAbi(appInfo);
            String paths = appInfo.nativeLibraryDir + File.pathSeparator + appInfo.sourceDir + "!/lib/" + abi;
            return paths;
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "Debug layer app '" + app + "' not installed");
            return null;
        }
    }

    private void setupGpuLayers(Context context, Bundle coreSettings, PackageManager pm, String packageName) {
        String gpuDebugApp;
        String layerPaths = "";
        if (isDebuggable()) {
            int enable = coreSettings.getInt(Settings.Global.ENABLE_GPU_DEBUG_LAYERS, 0);
            if (enable != 0 && (gpuDebugApp = coreSettings.getString(Settings.Global.GPU_DEBUG_APP)) != null && packageName != null && !gpuDebugApp.isEmpty() && !packageName.isEmpty() && gpuDebugApp.equals(packageName)) {
                Log.i(TAG, "GPU debug layers enabled for " + packageName);
                layerPaths = this.mDebugLayerPath + SettingsStringUtil.DELIMITER;
                String gpuDebugLayerApp = coreSettings.getString(Settings.Global.GPU_DEBUG_LAYER_APP);
                if (gpuDebugLayerApp != null && !gpuDebugLayerApp.isEmpty()) {
                    Log.i(TAG, "GPU debug layer app: " + gpuDebugLayerApp);
                    String[] layerApps = gpuDebugLayerApp.split(SettingsStringUtil.DELIMITER);
                    for (String str : layerApps) {
                        String paths = getDebugLayerAppPaths(pm, str);
                        if (paths != null) {
                            layerPaths = layerPaths + paths + SettingsStringUtil.DELIMITER;
                        }
                    }
                }
                String layers = coreSettings.getString(Settings.Global.GPU_DEBUG_LAYERS);
                Log.i(TAG, "Vulkan debug layer list: " + layers);
                if (layers != null && !layers.isEmpty()) {
                    setDebugLayers(layers);
                }
                String layersGLES = coreSettings.getString(Settings.Global.GPU_DEBUG_LAYERS_GLES);
                Log.i(TAG, "GLES debug layer list: " + layersGLES);
                if (layersGLES != null && !layersGLES.isEmpty()) {
                    setDebugLayersGLES(layersGLES);
                }
            }
        }
        setLayerPaths(this.mClassLoader, layerPaths + this.mLayerPath);
    }

    private static Map<OpenGlDriverChoice, String> buildMap() {
        Map<OpenGlDriverChoice, String> map = new HashMap<>();
        map.put(OpenGlDriverChoice.DEFAULT, "default");
        map.put(OpenGlDriverChoice.ANGLE, "angle");
        map.put(OpenGlDriverChoice.NATIVE, "native");
        return map;
    }

    private static List<String> getGlobalSettingsString(ContentResolver contentResolver, Bundle bundle, String globalSetting) {
        String settingsValue;
        if (bundle != null) {
            settingsValue = bundle.getString(globalSetting);
        } else {
            settingsValue = Settings.Global.getString(contentResolver, globalSetting);
        }
        if (settingsValue != null) {
            List<String> valueList = new ArrayList<>(Arrays.asList(settingsValue.split(SmsManager.REGEX_PREFIX_DELIMITER)));
            return valueList;
        }
        List<String> valueList2 = new ArrayList<>();
        return valueList2;
    }

    private static int getGlobalSettingsPkgIndex(String pkgName, List<String> globalSettingsDriverPkgs) {
        for (int pkgIndex = 0; pkgIndex < globalSettingsDriverPkgs.size(); pkgIndex++) {
            if (globalSettingsDriverPkgs.get(pkgIndex).equals(pkgName)) {
                return pkgIndex;
            }
        }
        return -1;
    }

    private static String getDriverForPkg(Context context, Bundle bundle, String packageName) {
        String allUseAngle;
        if (bundle != null) {
            allUseAngle = bundle.getString(Settings.Global.GLOBAL_SETTINGS_ANGLE_GL_DRIVER_ALL_ANGLE);
        } else {
            allUseAngle = Settings.Global.getString(context.getContentResolver(), Settings.Global.GLOBAL_SETTINGS_ANGLE_GL_DRIVER_ALL_ANGLE);
        }
        if (allUseAngle != null && allUseAngle.equals(WifiEnterpriseConfig.ENGINE_ENABLE)) {
            return sDriverMap.get(OpenGlDriverChoice.ANGLE);
        }
        ContentResolver contentResolver = context.getContentResolver();
        List<String> globalSettingsDriverPkgs = getGlobalSettingsString(contentResolver, bundle, Settings.Global.GLOBAL_SETTINGS_ANGLE_GL_DRIVER_SELECTION_PKGS);
        List<String> globalSettingsDriverValues = getGlobalSettingsString(contentResolver, bundle, Settings.Global.GLOBAL_SETTINGS_ANGLE_GL_DRIVER_SELECTION_VALUES);
        if (packageName == null || packageName.isEmpty()) {
            return sDriverMap.get(OpenGlDriverChoice.DEFAULT);
        }
        if (globalSettingsDriverPkgs.size() != globalSettingsDriverValues.size()) {
            Log.w(TAG, "Global.Settings values are invalid: globalSettingsDriverPkgs.size = " + globalSettingsDriverPkgs.size() + ", globalSettingsDriverValues.size = " + globalSettingsDriverValues.size());
            return sDriverMap.get(OpenGlDriverChoice.DEFAULT);
        }
        int pkgIndex = getGlobalSettingsPkgIndex(packageName, globalSettingsDriverPkgs);
        if (pkgIndex < 0) {
            return sDriverMap.get(OpenGlDriverChoice.DEFAULT);
        }
        return globalSettingsDriverValues.get(pkgIndex);
    }

    private String getAnglePackageName(PackageManager pm) {
        Intent intent = new Intent(ACTION_ANGLE_FOR_ANDROID);
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 1048576);
        if (resolveInfos.size() != 1) {
            Log.e(TAG, "Invalid number of ANGLE packages. Required: 1, Found: " + resolveInfos.size());
            for (ResolveInfo resolveInfo : resolveInfos) {
                Log.e(TAG, "Found ANGLE package: " + resolveInfo.activityInfo.packageName);
            }
            return "";
        }
        return resolveInfos.get(0).activityInfo.packageName;
    }

    private String getAngleDebugPackage(Context context, Bundle coreSettings) {
        String debugPackage;
        if (isDebuggable()) {
            if (coreSettings != null) {
                debugPackage = coreSettings.getString(Settings.Global.GLOBAL_SETTINGS_ANGLE_DEBUG_PACKAGE);
            } else {
                ContentResolver contentResolver = context.getContentResolver();
                debugPackage = Settings.Global.getString(contentResolver, Settings.Global.GLOBAL_SETTINGS_ANGLE_DEBUG_PACKAGE);
            }
            if (debugPackage != null && !debugPackage.isEmpty()) {
                return debugPackage;
            }
            return "";
        }
        return "";
    }

    private static boolean setupAngleWithTempRulesFile(Context context, String packageName, String paths, String devOptIn) {
        if (!isDebuggable()) {
            Log.v(TAG, "Skipping loading temporary rules file");
            return false;
        }
        String angleTempRules = SystemProperties.get(ANGLE_TEMP_RULES);
        if (angleTempRules == null || angleTempRules.isEmpty()) {
            Log.v(TAG, "System property 'debug.angle.rules' is not set or is empty");
            return false;
        }
        Log.i(TAG, "Detected system property debug.angle.rules: " + angleTempRules);
        File tempRulesFile = new File(angleTempRules);
        if (tempRulesFile.exists()) {
            Log.i(TAG, angleTempRules + " exists, loading file.");
            try {
                try {
                    FileInputStream stream = new FileInputStream(angleTempRules);
                    try {
                        FileDescriptor rulesFd = stream.getFD();
                        long rulesLength = stream.getChannel().size();
                        Log.i(TAG, "Loaded temporary ANGLE rules from " + angleTempRules);
                        setAngleInfo(paths, packageName, devOptIn, rulesFd, 0L, rulesLength);
                        stream.close();
                        return true;
                    } catch (IOException e) {
                        Log.w(TAG, "Hit IOException thrown by FileInputStream: " + e);
                    }
                } catch (FileNotFoundException e2) {
                    Log.w(TAG, "Temp ANGLE rules file not found: " + e2);
                }
            } catch (SecurityException e3) {
                Log.w(TAG, "Temp ANGLE rules file not accessible: " + e3);
            }
        }
        return false;
    }

    private static boolean setupAngleRulesApk(String anglePkgName, ApplicationInfo angleInfo, PackageManager pm, String packageName, String paths, String devOptIn) {
        try {
            AssetManager angleAssets = pm.getResourcesForApplication(angleInfo).getAssets();
            try {
                AssetFileDescriptor assetsFd = angleAssets.openFd(ANGLE_RULES_FILE);
                setAngleInfo(paths, packageName, devOptIn, assetsFd.getFileDescriptor(), assetsFd.getStartOffset(), assetsFd.getLength());
                assetsFd.close();
                return true;
            } catch (IOException e) {
                Log.w(TAG, "Failed to get AssetFileDescriptor for a4a_rules.json from '" + anglePkgName + "': " + e);
                return false;
            }
        } catch (PackageManager.NameNotFoundException e2) {
            Log.w(TAG, "Failed to get AssetManager for '" + anglePkgName + "': " + e2);
            return false;
        }
    }

    private static boolean checkAngleWhitelist(Context context, Bundle bundle, String packageName) {
        ContentResolver contentResolver = context.getContentResolver();
        List<String> angleWhitelist = getGlobalSettingsString(contentResolver, bundle, Settings.Global.GLOBAL_SETTINGS_ANGLE_WHITELIST);
        return angleWhitelist.contains(packageName);
    }

    public boolean setupAngle(Context context, Bundle bundle, PackageManager pm, String packageName) {
        ApplicationInfo angleInfo;
        String anglePkgName;
        if (shouldUseAngle(context, bundle, packageName)) {
            ApplicationInfo angleInfo2 = null;
            String anglePkgName2 = getAngleDebugPackage(context, bundle);
            if (!anglePkgName2.isEmpty()) {
                Log.i(TAG, "ANGLE debug package enabled: " + anglePkgName2);
                try {
                    angleInfo2 = pm.getApplicationInfo(anglePkgName2, 0);
                } catch (PackageManager.NameNotFoundException e) {
                    Log.w(TAG, "ANGLE debug package '" + anglePkgName2 + "' not installed");
                    return false;
                }
            }
            if (angleInfo2 == null) {
                String anglePkgName3 = getAnglePackageName(pm);
                if (!anglePkgName3.isEmpty()) {
                    Log.i(TAG, "ANGLE package enabled: " + anglePkgName3);
                    try {
                        angleInfo = pm.getApplicationInfo(anglePkgName3, 1048576);
                        anglePkgName = anglePkgName3;
                    } catch (PackageManager.NameNotFoundException e2) {
                        Log.w(TAG, "ANGLE package '" + anglePkgName3 + "' not installed");
                        return false;
                    }
                } else {
                    Log.e(TAG, "Failed to find ANGLE package.");
                    return false;
                }
            } else {
                angleInfo = angleInfo2;
                anglePkgName = anglePkgName2;
            }
            String abi = chooseAbi(angleInfo);
            String paths = angleInfo.nativeLibraryDir + File.pathSeparator + angleInfo.sourceDir + "!/lib/" + abi;
            String devOptIn = getDriverForPkg(context, bundle, packageName);
            return setupAngleWithTempRulesFile(context, packageName, paths, devOptIn) || setupAngleRulesApk(anglePkgName, angleInfo, pm, packageName, paths, devOptIn);
        }
        return false;
    }

    private boolean shouldShowAngleInUseDialogBox(Context context) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            int showDialogBox = Settings.Global.getInt(contentResolver, Settings.Global.GLOBAL_SETTINGS_SHOW_ANGLE_IN_USE_DIALOG_BOX);
            if (showDialogBox != 1) {
                return false;
            }
            return true;
        } catch (Settings.SettingNotFoundException | SecurityException e) {
            return false;
        }
    }

    private boolean setupAndUseAngle(Context context, String packageName) {
        if (!setupAngle(context, null, context.getPackageManager(), packageName)) {
            Log.v(TAG, "Package '" + packageName + "' should not use ANGLE");
            return false;
        }
        boolean useAngle = getShouldUseAngle(packageName);
        Log.v(TAG, "Package '" + packageName + "' should use ANGLE = '" + useAngle + "'");
        return useAngle;
    }

    public void showAngleInUseDialogBox(Context context) {
        String packageName = context.getPackageName();
        if (shouldShowAngleInUseDialogBox(context) && setupAndUseAngle(context, packageName)) {
            Intent intent = new Intent(ACTION_ANGLE_FOR_ANDROID_TOAST_MESSAGE);
            String anglePkg = getAnglePackageName(context.getPackageManager());
            intent.setPackage(anglePkg);
            context.sendOrderedBroadcast(intent, null, new BroadcastReceiver() { // from class: android.os.GraphicsEnvironment.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context2, Intent intent2) {
                    Bundle results = getResultExtras(true);
                    String toastMsg = results.getString(GraphicsEnvironment.INTENT_KEY_A4A_TOAST_MESSAGE);
                    Toast toast = Toast.makeText(context2, toastMsg, 1);
                    toast.show();
                }
            }, null, -1, null, null);
        }
    }

    private static String chooseDriverInternal(Context context, Bundle coreSettings, PackageManager pm, String packageName) {
        ApplicationInfo ai;
        String gameDriver = SystemProperties.get(PROPERTY_GFX_DRIVER);
        boolean hasGameDriver = (gameDriver == null || gameDriver.isEmpty()) ? false : true;
        String prereleaseDriver = SystemProperties.get(PROPERTY_GFX_DRIVER_PRERELEASE);
        boolean hasPrereleaseDriver = (prereleaseDriver == null || prereleaseDriver.isEmpty()) ? false : true;
        if (!hasGameDriver && !hasPrereleaseDriver) {
            return null;
        }
        try {
            ai = pm.getApplicationInfo(packageName, 128);
        } catch (PackageManager.NameNotFoundException e) {
            ai = context.getApplicationInfo();
        }
        if (ai.isPrivilegedApp() || (ai.isSystemApp() && !ai.isUpdatedSystemApp())) {
            return null;
        }
        boolean enablePrereleaseDriver = (ai.metaData != null && ai.metaData.getBoolean(METADATA_DEVELOPER_DRIVER_ENABLE)) || isDebuggable();
        int i = coreSettings.getInt(Settings.Global.GAME_DRIVER_ALL_APPS, 0);
        if (i == 1) {
            if (hasGameDriver) {
                return gameDriver;
            }
            return null;
        } else if (i == 2) {
            if (hasPrereleaseDriver && enablePrereleaseDriver) {
                return prereleaseDriver;
            }
            return null;
        } else if (i == 3) {
            return null;
        } else {
            String appPackageName = ai.packageName;
            if (getGlobalSettingsString(null, coreSettings, Settings.Global.GAME_DRIVER_OPT_OUT_APPS).contains(appPackageName)) {
                return null;
            }
            if (getGlobalSettingsString(null, coreSettings, Settings.Global.GAME_DRIVER_PRERELEASE_OPT_IN_APPS).contains(appPackageName)) {
                if (hasPrereleaseDriver && enablePrereleaseDriver) {
                    return prereleaseDriver;
                }
                return null;
            } else if (!hasGameDriver) {
                return null;
            } else {
                boolean isOptIn = getGlobalSettingsString(null, coreSettings, Settings.Global.GAME_DRIVER_OPT_IN_APPS).contains(appPackageName);
                List<String> whitelist = getGlobalSettingsString(null, coreSettings, Settings.Global.GAME_DRIVER_WHITELIST);
                if (!isOptIn && whitelist.indexOf("*") != 0 && !whitelist.contains(appPackageName)) {
                    return null;
                }
                if (!isOptIn && getGlobalSettingsString(null, coreSettings, Settings.Global.GAME_DRIVER_BLACKLIST).contains(appPackageName)) {
                    return null;
                }
                return gameDriver;
            }
        }
    }

    private static boolean chooseDriver(Context context, Bundle coreSettings, PackageManager pm, String packageName) {
        String abi;
        String driverPackageName = chooseDriverInternal(context, coreSettings, pm, packageName);
        if (driverPackageName == null) {
            return false;
        }
        try {
            PackageInfo driverPackageInfo = pm.getPackageInfo(driverPackageName, 1048704);
            ApplicationInfo driverAppInfo = driverPackageInfo.applicationInfo;
            if (driverAppInfo.targetSdkVersion < 26 || (abi = chooseAbi(driverAppInfo)) == null) {
                return false;
            }
            String paths = driverAppInfo.nativeLibraryDir + File.pathSeparator + driverAppInfo.sourceDir + "!/lib/" + abi;
            String sphalLibraries = getSphalLibraries(context, driverPackageName);
            setDriverPathAndSphalLibraries(paths, sphalLibraries);
            if (driverAppInfo.metaData == null) {
                throw new NullPointerException("apk's meta-data cannot be null");
            }
            String driverBuildTime = driverAppInfo.metaData.getString(METADATA_DRIVER_BUILD_TIME);
            if (driverBuildTime == null || driverBuildTime.isEmpty()) {
                throw new IllegalArgumentException("com.android.gamedriver.build_time is not set");
            }
            setGpuStats(driverPackageName, driverPackageInfo.versionName, driverAppInfo.longVersionCode, Long.parseLong(driverBuildTime.substring(1)), packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "driver package '" + driverPackageName + "' not installed");
            return false;
        }
    }

    private static String chooseAbi(ApplicationInfo ai) {
        String isa = VMRuntime.getCurrentInstructionSet();
        if (ai.primaryCpuAbi != null && isa.equals(VMRuntime.getInstructionSet(ai.primaryCpuAbi))) {
            return ai.primaryCpuAbi;
        }
        if (ai.secondaryCpuAbi != null && isa.equals(VMRuntime.getInstructionSet(ai.secondaryCpuAbi))) {
            return ai.secondaryCpuAbi;
        }
        return null;
    }

    private static String getSphalLibraries(Context context, String driverPackageName) {
        try {
            Context driverContext = context.createPackageContext(driverPackageName, 4);
            BufferedReader reader = new BufferedReader(new InputStreamReader(driverContext.getAssets().open(GAME_DRIVER_SPHAL_LIBRARIES_FILENAME)));
            ArrayList<String> assetStrings = new ArrayList<>();
            while (true) {
                String assetString = reader.readLine();
                if (assetString != null) {
                    assetStrings.add(assetString);
                } else {
                    return String.join(SettingsStringUtil.DELIMITER, assetStrings);
                }
            }
        } catch (PackageManager.NameNotFoundException | IOException e) {
            return "";
        }
    }
}
