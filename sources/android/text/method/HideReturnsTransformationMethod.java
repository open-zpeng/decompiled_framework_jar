package android.text.method;

import android.annotation.UnsupportedAppUsage;

/* loaded from: classes2.dex */
public class HideReturnsTransformationMethod extends ReplacementTransformationMethod {
    private static char[] ORIGINAL = {'\r'};
    private static char[] REPLACEMENT = {65279};
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private static HideReturnsTransformationMethod sInstance;

    @Override // android.text.method.ReplacementTransformationMethod
    protected char[] getOriginal() {
        return ORIGINAL;
    }

    @Override // android.text.method.ReplacementTransformationMethod
    protected char[] getReplacement() {
        return REPLACEMENT;
    }

    public static HideReturnsTransformationMethod getInstance() {
        HideReturnsTransformationMethod hideReturnsTransformationMethod = sInstance;
        if (hideReturnsTransformationMethod != null) {
            return hideReturnsTransformationMethod;
        }
        sInstance = new HideReturnsTransformationMethod();
        return sInstance;
    }
}
