package android.os;
/* loaded from: classes2.dex */
public class VintfRuntimeInfo {
    public static native String getBootAvbVersion();

    public static native String getBootVbmetaAvbVersion();

    private protected static native String getCpuInfo();

    private protected static native String getHardwareId();

    public static native long getKernelSepolicyVersion();

    private protected static native String getKernelVersion();

    private protected static native String getNodeName();

    private protected static native String getOsName();

    private protected static native String getOsRelease();

    private protected static native String getOsVersion();

    private synchronized VintfRuntimeInfo() {
    }
}
