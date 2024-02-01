package android.graphics;

import android.annotation.UnsupportedAppUsage;
import android.graphics.PorterDuff;

/* loaded from: classes.dex */
public class Xfermode {
    static final int DEFAULT = PorterDuff.Mode.SRC_OVER.nativeInt;
    @UnsupportedAppUsage
    int porterDuffMode = DEFAULT;
}
