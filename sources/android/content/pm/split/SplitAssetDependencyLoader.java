package android.content.pm.split;

import android.content.pm.PackageParser;
import android.content.res.ApkAssets;
import android.content.res.AssetManager;
import android.os.Build;
import android.util.SparseArray;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import libcore.io.IoUtils;
/* loaded from: classes.dex */
public class SplitAssetDependencyLoader extends SplitDependencyLoader<PackageParser.PackageParserException> implements SplitAssetLoader {
    public protected final AssetManager[] mCachedAssetManagers;
    public protected final ApkAssets[][] mCachedSplitApks;
    public protected final int mFlags;
    public protected final String[] mSplitPaths;

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized SplitAssetDependencyLoader(PackageParser.PackageLite pkg, SparseArray<int[]> dependencies, int flags) {
        super(dependencies);
        this.mSplitPaths = new String[pkg.splitCodePaths.length + 1];
        this.mSplitPaths[0] = pkg.baseCodePath;
        System.arraycopy(pkg.splitCodePaths, 0, this.mSplitPaths, 1, pkg.splitCodePaths.length);
        this.mFlags = flags;
        this.mCachedSplitApks = new ApkAssets[this.mSplitPaths.length];
        this.mCachedAssetManagers = new AssetManager[this.mSplitPaths.length];
    }

    public private synchronized boolean isSplitCached(int splitIdx) {
        return this.mCachedAssetManagers[splitIdx] != null;
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

    public protected static synchronized AssetManager createAssetManagerWithAssets(ApkAssets[] apkAssets) {
        AssetManager assets = new AssetManager();
        assets.setConfiguration(0, 0, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, Build.VERSION.RESOURCES_SDK_INT);
        assets.setApkAssets(apkAssets, false);
        return assets;
    }

    public private synchronized void constructSplit(int splitIdx, int[] configSplitIndices, int parentSplitIdx) throws PackageParser.PackageParserException {
        ArrayList<ApkAssets> assets = new ArrayList<>();
        if (parentSplitIdx >= 0) {
            Collections.addAll(assets, this.mCachedSplitApks[parentSplitIdx]);
        }
        assets.add(loadApkAssets(this.mSplitPaths[splitIdx], this.mFlags));
        for (int configSplitIdx : configSplitIndices) {
            assets.add(loadApkAssets(this.mSplitPaths[configSplitIdx], this.mFlags));
        }
        this.mCachedSplitApks[splitIdx] = (ApkAssets[]) assets.toArray(new ApkAssets[assets.size()]);
        this.mCachedAssetManagers[splitIdx] = createAssetManagerWithAssets(this.mCachedSplitApks[splitIdx]);
    }

    private protected synchronized AssetManager getBaseAssetManager() throws PackageParser.PackageParserException {
        loadDependenciesForSplit(0);
        return this.mCachedAssetManagers[0];
    }

    private protected synchronized AssetManager getSplitAssetManager(int idx) throws PackageParser.PackageParserException {
        loadDependenciesForSplit(idx + 1);
        return this.mCachedAssetManagers[idx + 1];
    }

    @Override // java.lang.AutoCloseable
    public void close() throws Exception {
        AssetManager[] assetManagerArr;
        for (AssetManager assets : this.mCachedAssetManagers) {
            IoUtils.closeQuietly(assets);
        }
    }
}
