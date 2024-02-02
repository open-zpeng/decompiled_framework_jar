package android.content.res;

import android.text.TextUtils;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ResourcesKey {
    public final CompatibilityInfo mCompatInfo;
    public final int mDisplayId;
    private final int mHash;
    public final String[] mLibDirs;
    public final String[] mOverlayDirs;
    public final Configuration mOverrideConfiguration;
    private protected final String mResDir;
    private protected final String[] mSplitResDirs;

    /* JADX INFO: Access modifiers changed from: private */
    public ResourcesKey(String resDir, String[] splitResDirs, String[] overlayDirs, String[] libDirs, int displayId, Configuration overrideConfig, CompatibilityInfo compatInfo) {
        this.mResDir = resDir;
        this.mSplitResDirs = splitResDirs;
        this.mOverlayDirs = overlayDirs;
        this.mLibDirs = libDirs;
        this.mDisplayId = displayId;
        this.mOverrideConfiguration = new Configuration(overrideConfig != null ? overrideConfig : Configuration.EMPTY);
        if (this.mOverrideConfiguration.uiMode < 0) {
            this.mOverrideConfiguration.uiMode = 0;
        }
        this.mCompatInfo = compatInfo != null ? compatInfo : CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO;
        int hash = (31 * 17) + Objects.hashCode(this.mResDir);
        this.mHash = (31 * ((31 * ((31 * ((31 * ((31 * ((31 * hash) + Arrays.hashCode(this.mSplitResDirs))) + Arrays.hashCode(this.mOverlayDirs))) + Arrays.hashCode(this.mLibDirs))) + this.mDisplayId)) + Objects.hashCode(this.mOverrideConfiguration))) + Objects.hashCode(this.mCompatInfo);
    }

    public synchronized boolean hasOverrideConfiguration() {
        return !Configuration.EMPTY.equals(this.mOverrideConfiguration);
    }

    public synchronized boolean isPathReferenced(String path) {
        return (this.mResDir != null && this.mResDir.startsWith(path)) || anyStartsWith(this.mSplitResDirs, path) || anyStartsWith(this.mOverlayDirs, path) || anyStartsWith(this.mLibDirs, path);
    }

    private static synchronized boolean anyStartsWith(String[] list, String prefix) {
        if (list != null) {
            for (String s : list) {
                if (s != null && s.startsWith(prefix)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int hashCode() {
        return this.mHash;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ResourcesKey) {
            ResourcesKey peer = (ResourcesKey) obj;
            return this.mHash == peer.mHash && Objects.equals(this.mResDir, peer.mResDir) && Arrays.equals(this.mSplitResDirs, peer.mSplitResDirs) && Arrays.equals(this.mOverlayDirs, peer.mOverlayDirs) && Arrays.equals(this.mLibDirs, peer.mLibDirs) && this.mDisplayId == peer.mDisplayId && Objects.equals(this.mOverrideConfiguration, peer.mOverrideConfiguration) && Objects.equals(this.mCompatInfo, peer.mCompatInfo);
        }
        return false;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder().append("ResourcesKey{");
        builder.append(" mHash=");
        builder.append(Integer.toHexString(this.mHash));
        builder.append(" mResDir=");
        builder.append(this.mResDir);
        builder.append(" mSplitDirs=[");
        if (this.mSplitResDirs != null) {
            builder.append(TextUtils.join(",", this.mSplitResDirs));
        }
        builder.append("]");
        builder.append(" mOverlayDirs=[");
        if (this.mOverlayDirs != null) {
            builder.append(TextUtils.join(",", this.mOverlayDirs));
        }
        builder.append("]");
        builder.append(" mLibDirs=[");
        if (this.mLibDirs != null) {
            builder.append(TextUtils.join(",", this.mLibDirs));
        }
        builder.append("]");
        builder.append(" mDisplayId=");
        builder.append(this.mDisplayId);
        builder.append(" mOverrideConfig=");
        builder.append(Configuration.resourceQualifierString(this.mOverrideConfiguration));
        builder.append(" mCompatInfo=");
        builder.append(this.mCompatInfo);
        builder.append("}");
        return builder.toString();
    }
}
