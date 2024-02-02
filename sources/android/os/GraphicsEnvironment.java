package android.os;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.opengl.EGL14;
import android.provider.Settings;
import android.provider.SettingsStringUtil;
import android.util.Log;
import dalvik.system.VMRuntime;
import java.io.File;
/* loaded from: classes2.dex */
public class GraphicsEnvironment {
    private static final boolean DEBUG = false;
    private static final String PROPERTY_GFX_DRIVER = "ro.gfx.driver.0";
    private static final String TAG = "GraphicsEnvironment";
    private static final GraphicsEnvironment sInstance = new GraphicsEnvironment();
    private ClassLoader mClassLoader;
    private String mDebugLayerPath;
    private String mLayerPath;

    private static native void setDebugLayers(String str);

    private static native void setDriverPath(String str);

    private static native void setLayerPaths(ClassLoader classLoader, String str);

    public static synchronized GraphicsEnvironment getInstance() {
        return sInstance;
    }

    public synchronized void setup(Context context) {
        setupGpuLayers(context);
        chooseDriver(context);
    }

    private static synchronized boolean isDebuggable(Context context) {
        return (context.getApplicationInfo().flags & 2) > 0;
    }

    public synchronized void setLayerPaths(ClassLoader classLoader, String layerPath, String debugLayerPath) {
        this.mClassLoader = classLoader;
        this.mLayerPath = layerPath;
        this.mDebugLayerPath = debugLayerPath;
    }

    private synchronized void setupGpuLayers(Context context) {
        String layerPaths = "";
        if (isDebuggable(context)) {
            int enable = Settings.Global.getInt(context.getContentResolver(), Settings.Global.ENABLE_GPU_DEBUG_LAYERS, 0);
            if (enable != 0) {
                String gpuDebugApp = Settings.Global.getString(context.getContentResolver(), Settings.Global.GPU_DEBUG_APP);
                String packageName = context.getPackageName();
                if (gpuDebugApp != null && packageName != null && !gpuDebugApp.isEmpty() && !packageName.isEmpty() && gpuDebugApp.equals(packageName)) {
                    Log.i(TAG, "GPU debug layers enabled for " + packageName);
                    layerPaths = this.mDebugLayerPath + SettingsStringUtil.DELIMITER;
                    String layers = Settings.Global.getString(context.getContentResolver(), Settings.Global.GPU_DEBUG_LAYERS);
                    Log.i(TAG, "Debug layer list: " + layers);
                    if (layers != null && !layers.isEmpty()) {
                        setDebugLayers(layers);
                    }
                }
            }
        }
        setLayerPaths(this.mClassLoader, layerPaths + this.mLayerPath);
    }

    private static synchronized void chooseDriver(Context context) {
        String driverPackageName = SystemProperties.get(PROPERTY_GFX_DRIVER);
        if (driverPackageName == null || driverPackageName.isEmpty()) {
            return;
        }
        ApplicationInfo ai = context.getApplicationInfo();
        if (ai.isPrivilegedApp()) {
            return;
        }
        if (ai.isSystemApp() && !ai.isUpdatedSystemApp()) {
            return;
        }
        try {
            ApplicationInfo driverInfo = context.getPackageManager().getApplicationInfo(driverPackageName, 1048576);
            String abi = chooseAbi(driverInfo);
            if (abi == null) {
                return;
            }
            if (driverInfo.targetSdkVersion < 26) {
                Log.w(TAG, "updated driver package is not known to be compatible with O");
                return;
            }
            String paths = driverInfo.nativeLibraryDir + File.pathSeparator + driverInfo.sourceDir + "!/lib/" + abi;
            setDriverPath(paths);
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "driver package '" + driverPackageName + "' not installed");
        }
    }

    public static synchronized void earlyInitEGL() {
        Thread eglInitThread = new Thread(new Runnable() { // from class: android.os.-$$Lambda$GraphicsEnvironment$U4RqBlx5-Js31-71IFOgvpvoAFg
            @Override // java.lang.Runnable
            public final void run() {
                EGL14.eglGetDisplay(0);
            }
        }, "EGL Init");
        eglInitThread.start();
    }

    private static synchronized String chooseAbi(ApplicationInfo ai) {
        String isa = VMRuntime.getCurrentInstructionSet();
        if (ai.primaryCpuAbi != null && isa.equals(VMRuntime.getInstructionSet(ai.primaryCpuAbi))) {
            return ai.primaryCpuAbi;
        }
        if (ai.secondaryCpuAbi != null && isa.equals(VMRuntime.getInstructionSet(ai.secondaryCpuAbi))) {
            return ai.secondaryCpuAbi;
        }
        return null;
    }
}
