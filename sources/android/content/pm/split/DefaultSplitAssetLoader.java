package android.content.pm.split;

import android.content.pm.PackageParser;
import android.content.res.ApkAssets;
import android.content.res.AssetManager;
import android.os.Build;
import com.android.internal.util.ArrayUtils;
import java.io.IOException;
import libcore.io.IoUtils;
/* loaded from: classes.dex */
public class DefaultSplitAssetLoader implements SplitAssetLoader {
    public protected final String mBaseCodePath;
    public protected AssetManager mCachedAssetManager;
    public protected final int mFlags;
    public protected final String[] mSplitCodePaths;

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized DefaultSplitAssetLoader(PackageParser.PackageLite pkg, int flags) {
        this.mBaseCodePath = pkg.baseCodePath;
        this.mSplitCodePaths = pkg.splitCodePaths;
        this.mFlags = flags;
    }

    public protected static synchronized ApkAssets loadApkAssets(String path, int flags) throws PackageParser.PackageParserException {
        if ((flags & 1) != 0 && !PackageParser.isApkPath(path)) {
            throw new PackageParser.PackageParserException(-100, "Invalid package file: " + path);
        }
        try {
            return ApkAssets.loadFromPath(path);
        } catch (IOException e) {
            throw new PackageParser.PackageParserException(-2, "Failed to load APK at path " + path, e);
        }
    }

    private protected synchronized AssetManager getBaseAssetManager() throws PackageParser.PackageParserException {
        if (this.mCachedAssetManager != null) {
            return this.mCachedAssetManager;
        }
        ApkAssets[] apkAssets = new ApkAssets[(this.mSplitCodePaths != null ? this.mSplitCodePaths.length : 0) + 1];
        int splitIdx = 0 + 1;
        apkAssets[0] = loadApkAssets(this.mBaseCodePath, this.mFlags);
        if (!ArrayUtils.isEmpty(this.mSplitCodePaths)) {
            String[] strArr = this.mSplitCodePaths;
            int length = strArr.length;
            int splitIdx2 = splitIdx;
            int splitIdx3 = 0;
            while (splitIdx3 < length) {
                String apkPath = strArr[splitIdx3];
                apkAssets[splitIdx2] = loadApkAssets(apkPath, this.mFlags);
                splitIdx3++;
                splitIdx2++;
            }
        }
        AssetManager assets = new AssetManager();
        assets.setConfiguration(0, 0, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, Build.VERSION.RESOURCES_SDK_INT);
        assets.setApkAssets(apkAssets, false);
        this.mCachedAssetManager = assets;
        return this.mCachedAssetManager;
    }

    private protected synchronized AssetManager getSplitAssetManager(int splitIdx) throws PackageParser.PackageParserException {
        return getBaseAssetManager();
    }

    @Override // java.lang.AutoCloseable
    public void close() throws Exception {
        IoUtils.closeQuietly(this.mCachedAssetManager);
    }
}
