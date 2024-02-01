package android.renderscript;

import java.io.File;
/* loaded from: classes2.dex */
public class RenderScriptCacheDir {
    public private protected static File mCacheDir;

    /* JADX INFO: Access modifiers changed from: private */
    public static void setupDiskCache(File cacheDir) {
        mCacheDir = cacheDir;
    }
}
