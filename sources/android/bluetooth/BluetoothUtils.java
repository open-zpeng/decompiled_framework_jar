package android.bluetooth;

/* loaded from: classes.dex */
public class BluetoothUtils {
    private static final String TAG = "BluetoothUtils";

    public static native int getDownloadProgress();

    public static native String getModuleVersion();

    public static native int getUploadProgress();

    public static native boolean startDownload(String str);

    public static native boolean startUpload();
}
