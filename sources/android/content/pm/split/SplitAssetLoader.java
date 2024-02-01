package android.content.pm.split;

import android.content.pm.PackageParser;
import android.content.res.AssetManager;
/* loaded from: classes.dex */
public interface SplitAssetLoader extends AutoCloseable {
    /* JADX INFO: Access modifiers changed from: private */
    synchronized AssetManager getBaseAssetManager() throws PackageParser.PackageParserException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized AssetManager getSplitAssetManager(int i) throws PackageParser.PackageParserException;
}
