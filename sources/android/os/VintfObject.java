package android.os;

import java.util.Map;
/* loaded from: classes2.dex */
public class VintfObject {
    private protected static native String[] getHalNamesAndVersions();

    private protected static native String getSepolicyVersion();

    private protected static native Long getTargetFrameworkCompatibilityMatrixVersion();

    private protected static native Map<String, String[]> getVndkSnapshots();

    private protected static native String[] report();

    public static native int verify(String[] strArr);

    public static native int verifyWithoutAvb();
}
