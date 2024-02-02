package android.content.res;

import android.content.pm.ActivityInfo;
import android.content.res.XmlBlock;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import libcore.io.IoUtils;
/* loaded from: classes.dex */
public final class AssetManager implements AutoCloseable {
    public static final int ACCESS_BUFFER = 3;
    public static final int ACCESS_RANDOM = 1;
    public static final int ACCESS_STREAMING = 2;
    public static final int ACCESS_UNKNOWN = 0;
    private static final boolean DEBUG_REFS = false;
    private static final String FRAMEWORK_APK_PATH = "/system/framework/framework-res.apk";
    private static final String TAG = "AssetManager";
    @GuardedBy("sSync")
    private static ArraySet<ApkAssets> sSystemApkAssetsSet;
    @GuardedBy("this")
    private ApkAssets[] mApkAssets;
    @GuardedBy("this")
    private int mNumRefs;
    @GuardedBy("this")
    public protected long mObject;
    @GuardedBy("this")
    private final long[] mOffsets;
    @GuardedBy("this")
    private boolean mOpen;
    @GuardedBy("this")
    private HashMap<Long, RuntimeException> mRefStacks;
    @GuardedBy("this")
    private final TypedValue mValue;
    private static final Object sSync = new Object();
    private static final ApkAssets[] sEmptyApkAssets = new ApkAssets[0];
    @GuardedBy("sSync")
    public private protected static AssetManager sSystem = null;
    @GuardedBy("sSync")
    private static ApkAssets[] sSystemApkAssets = new ApkAssets[0];

    public static native String getAssetAllocations();

    /* JADX INFO: Access modifiers changed from: private */
    public static native int getGlobalAssetCount();

    /* JADX INFO: Access modifiers changed from: private */
    public static native int getGlobalAssetManagerCount();

    private static native void nativeApplyStyle(long j, long j2, int i, int i2, long j3, int[] iArr, long j4, long j5);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeAssetDestroy(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long nativeAssetGetLength(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long nativeAssetGetRemainingLength(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int nativeAssetRead(long j, byte[] bArr, int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int nativeAssetReadChar(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long nativeAssetSeek(long j, long j2, int i);

    private static native long nativeCreate();

    private static native void nativeDestroy(long j);

    private static native SparseArray<String> nativeGetAssignedPackageIdentifiers(long j);

    private static native String[] nativeGetLocales(long j, boolean z);

    private static native int nativeGetResourceArray(long j, int i, int[] iArr);

    private static native int nativeGetResourceArraySize(long j, int i);

    private static native int nativeGetResourceBagValue(long j, int i, int i2, TypedValue typedValue);

    private static native String nativeGetResourceEntryName(long j, int i);

    private static native int nativeGetResourceIdentifier(long j, String str, String str2, String str3);

    private static native int[] nativeGetResourceIntArray(long j, int i);

    private static native String nativeGetResourceName(long j, int i);

    private static native String nativeGetResourcePackageName(long j, int i);

    private static native String[] nativeGetResourceStringArray(long j, int i);

    private static native int[] nativeGetResourceStringArrayInfo(long j, int i);

    private static native String nativeGetResourceTypeName(long j, int i);

    private static native int nativeGetResourceValue(long j, int i, short s, TypedValue typedValue, boolean z);

    private static native Configuration[] nativeGetSizeConfigurations(long j);

    private static native int[] nativeGetStyleAttributes(long j, int i);

    private static native String[] nativeList(long j, String str) throws IOException;

    private static native long nativeOpenAsset(long j, String str, int i);

    private static native ParcelFileDescriptor nativeOpenAssetFd(long j, String str, long[] jArr) throws IOException;

    private static native long nativeOpenNonAsset(long j, int i, String str, int i2);

    private static native ParcelFileDescriptor nativeOpenNonAssetFd(long j, int i, String str, long[] jArr) throws IOException;

    private static native long nativeOpenXmlAsset(long j, int i, String str);

    private static native boolean nativeResolveAttrs(long j, long j2, int i, int i2, int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4);

    private static native boolean nativeRetrieveAttributes(long j, long j2, int[] iArr, int[] iArr2, int[] iArr3);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nativeSetApkAssets(long j, ApkAssets[] apkAssetsArr, boolean z);

    private static native void nativeSetConfiguration(long j, int i, int i2, String str, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15, int i16, int i17);

    private static native void nativeThemeApplyStyle(long j, long j2, int i, boolean z);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native void nativeThemeClear(long j);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native void nativeThemeCopy(long j, long j2);

    private static native long nativeThemeCreate(long j);

    private static native void nativeThemeDestroy(long j);

    private static native void nativeThemeDump(long j, long j2, int i, String str, String str2);

    private static native int nativeThemeGetAttributeValue(long j, long j2, int i, TypedValue typedValue, boolean z);

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native int nativeThemeGetChangingConfigurations(long j);

    private static native void nativeVerifySystemIdmaps();

    /* loaded from: classes.dex */
    public static class Builder {
        private ArrayList<ApkAssets> mUserApkAssets = new ArrayList<>();

        public synchronized Builder addApkAssets(ApkAssets apkAssets) {
            this.mUserApkAssets.add(apkAssets);
            return this;
        }

        public synchronized AssetManager build() {
            ApkAssets[] systemApkAssets = AssetManager.getSystem().getApkAssets();
            int totalApkAssetCount = systemApkAssets.length + this.mUserApkAssets.size();
            ApkAssets[] apkAssets = new ApkAssets[totalApkAssetCount];
            System.arraycopy(systemApkAssets, 0, apkAssets, 0, systemApkAssets.length);
            int userApkAssetCount = this.mUserApkAssets.size();
            for (int i = 0; i < userApkAssetCount; i++) {
                apkAssets[systemApkAssets.length + i] = this.mUserApkAssets.get(i);
            }
            AssetManager assetManager = new AssetManager(false);
            assetManager.mApkAssets = apkAssets;
            AssetManager.nativeSetApkAssets(assetManager.mObject, apkAssets, false);
            return assetManager;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public AssetManager() {
        ApkAssets[] assets;
        this.mValue = new TypedValue();
        this.mOffsets = new long[2];
        this.mOpen = true;
        this.mNumRefs = 1;
        synchronized (sSync) {
            createSystemAssetsInZygoteLocked();
            assets = sSystemApkAssets;
        }
        this.mObject = nativeCreate();
        setApkAssets(assets, false);
    }

    private synchronized AssetManager(boolean sentinel) {
        this.mValue = new TypedValue();
        this.mOffsets = new long[2];
        this.mOpen = true;
        this.mNumRefs = 1;
        this.mObject = nativeCreate();
    }

    @GuardedBy("sSync")
    private static synchronized void createSystemAssetsInZygoteLocked() {
        if (sSystem != null) {
            return;
        }
        nativeVerifySystemIdmaps();
        try {
            ArrayList<ApkAssets> apkAssets = new ArrayList<>();
            apkAssets.add(ApkAssets.loadFromPath(FRAMEWORK_APK_PATH, true));
            loadStaticRuntimeOverlays(apkAssets);
            sSystemApkAssetsSet = new ArraySet<>(apkAssets);
            sSystemApkAssets = (ApkAssets[]) apkAssets.toArray(new ApkAssets[apkAssets.size()]);
            sSystem = new AssetManager(true);
            sSystem.setApkAssets(sSystemApkAssets, false);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create system AssetManager", e);
        }
    }

    private static synchronized void loadStaticRuntimeOverlays(ArrayList<ApkAssets> outApkAssets) throws IOException {
        try {
            FileInputStream fis = new FileInputStream("/data/resource-cache/overlays.list");
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                FileLock flock = fis.getChannel().lock(0L, Long.MAX_VALUE, true);
                while (true) {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    String idmapPath = line.split(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER)[1];
                    outApkAssets.add(ApkAssets.loadOverlayFromPath(idmapPath, true));
                }
                if (flock != null) {
                    $closeResource(null, flock);
                }
                $closeResource(null, br);
            } finally {
                IoUtils.closeQuietly(fis);
            }
        } catch (FileNotFoundException e) {
            Log.i(TAG, "no overlays.list file found");
        }
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 == null) {
            x1.close();
            return;
        }
        try {
            x1.close();
        } catch (Throwable th) {
            x0.addSuppressed(th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static AssetManager getSystem() {
        AssetManager assetManager;
        synchronized (sSync) {
            createSystemAssetsInZygoteLocked();
            assetManager = sSystem;
        }
        return assetManager;
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        synchronized (this) {
            if (this.mOpen) {
                this.mOpen = false;
                decRefsLocked(hashCode());
            }
        }
    }

    public synchronized void setApkAssets(ApkAssets[] apkAssets, boolean invalidateCaches) {
        Preconditions.checkNotNull(apkAssets, "apkAssets");
        ApkAssets[] newApkAssets = new ApkAssets[sSystemApkAssets.length + apkAssets.length];
        System.arraycopy(sSystemApkAssets, 0, newApkAssets, 0, sSystemApkAssets.length);
        int newLength = sSystemApkAssets.length;
        for (ApkAssets apkAsset : apkAssets) {
            if (!sSystemApkAssetsSet.contains(apkAsset)) {
                newApkAssets[newLength] = apkAsset;
                newLength++;
            }
        }
        if (newLength != newApkAssets.length) {
            newApkAssets = (ApkAssets[]) Arrays.copyOf(newApkAssets, newLength);
        }
        synchronized (this) {
            ensureOpenLocked();
            this.mApkAssets = newApkAssets;
            nativeSetApkAssets(this.mObject, this.mApkAssets, invalidateCaches);
            if (invalidateCaches) {
                invalidateCachesLocked(-1);
            }
        }
    }

    private synchronized void invalidateCachesLocked(int diff) {
    }

    private protected ApkAssets[] getApkAssets() {
        synchronized (this) {
            if (this.mOpen) {
                return this.mApkAssets;
            }
            return sEmptyApkAssets;
        }
    }

    public synchronized int findCookieForPath(String path) {
        Preconditions.checkNotNull(path, "path");
        synchronized (this) {
            ensureValidLocked();
            int count = this.mApkAssets.length;
            for (int i = 0; i < count; i++) {
                if (path.equals(this.mApkAssets[i].getAssetPath())) {
                    return i + 1;
                }
            }
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Deprecated
    public int addAssetPath(String path) {
        return addAssetPathInternal(path, false, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Deprecated
    public int addAssetPathAsSharedLibrary(String path) {
        return addAssetPathInternal(path, false, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Deprecated
    public int addOverlayPath(String path) {
        return addAssetPathInternal(path, true, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0 */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v4 */
    private synchronized int addAssetPathInternal(String path, boolean overlay, boolean appAsLib) {
        Preconditions.checkNotNull(path, "path");
        synchronized (this) {
            ensureOpenLocked();
            int count = this.mApkAssets.length;
            int assets = 0;
            for (int i = 0; i < count; i++) {
                if (this.mApkAssets[i].getAssetPath().equals(path)) {
                    return i + 1;
                }
            }
            try {
                if (overlay) {
                    String idmapPath = "/data/resource-cache/" + path.substring(1).replace('/', '@') + "@idmap";
                    assets = ApkAssets.loadOverlayFromPath(idmapPath, false);
                } else {
                    assets = ApkAssets.loadFromPath(path, false, appAsLib);
                }
                this.mApkAssets = (ApkAssets[]) Arrays.copyOf(this.mApkAssets, count + 1);
                this.mApkAssets[count] = assets;
                nativeSetApkAssets(this.mObject, this.mApkAssets, true);
                invalidateCachesLocked(-1);
                return count + 1;
            } catch (IOException e) {
                return assets;
            }
        }
    }

    @GuardedBy("this")
    private synchronized void ensureValidLocked() {
        if (this.mObject == 0) {
            throw new RuntimeException("AssetManager has been destroyed");
        }
    }

    @GuardedBy("this")
    private synchronized void ensureOpenLocked() {
        if (!this.mOpen) {
            throw new RuntimeException("AssetManager has been closed");
        }
    }

    public private protected boolean getResourceValue(int resId, int densityDpi, TypedValue outValue, boolean resolveRefs) {
        Preconditions.checkNotNull(outValue, "outValue");
        synchronized (this) {
            ensureValidLocked();
            int cookie = nativeGetResourceValue(this.mObject, resId, (short) densityDpi, outValue, resolveRefs);
            if (cookie <= 0) {
                return false;
            }
            outValue.changingConfigurations = ActivityInfo.activityInfoConfigNativeToJava(outValue.changingConfigurations);
            if (outValue.type == 3) {
                outValue.string = this.mApkAssets[cookie - 1].getStringFromPool(outValue.data);
            }
            return true;
        }
    }

    public private protected CharSequence getResourceText(int resId) {
        synchronized (this) {
            TypedValue outValue = this.mValue;
            if (getResourceValue(resId, 0, outValue, true)) {
                return outValue.coerceToString();
            }
            return null;
        }
    }

    public private protected CharSequence getResourceBagText(int resId, int bagEntryId) {
        synchronized (this) {
            ensureValidLocked();
            TypedValue outValue = this.mValue;
            int cookie = nativeGetResourceBagValue(this.mObject, resId, bagEntryId, outValue);
            if (cookie <= 0) {
                return null;
            }
            outValue.changingConfigurations = ActivityInfo.activityInfoConfigNativeToJava(outValue.changingConfigurations);
            if (outValue.type == 3) {
                return this.mApkAssets[cookie - 1].getStringFromPool(outValue.data);
            }
            return outValue.coerceToString();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int getResourceArraySize(int resId) {
        ensureValidLocked();
        return nativeGetResourceArraySize(this.mObject, resId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int getResourceArray(int resId, int[] outData) {
        int nativeGetResourceArray;
        Preconditions.checkNotNull(outData, "outData");
        synchronized (this) {
            ensureValidLocked();
            nativeGetResourceArray = nativeGetResourceArray(this.mObject, resId, outData);
        }
        return nativeGetResourceArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized String[] getResourceStringArray(int resId) {
        ensureValidLocked();
        return nativeGetResourceStringArray(this.mObject, resId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized CharSequence[] getResourceTextArray(int resId) {
        CharSequence charSequence;
        ensureValidLocked();
        int[] rawInfoArray = nativeGetResourceStringArrayInfo(this.mObject, resId);
        if (rawInfoArray == null) {
            return null;
        }
        int rawInfoArrayLen = rawInfoArray.length;
        int infoArrayLen = rawInfoArrayLen / 2;
        CharSequence[] retArray = new CharSequence[infoArrayLen];
        int i = 0;
        int j = 0;
        while (i < rawInfoArrayLen) {
            int cookie = rawInfoArray[i];
            int index = rawInfoArray[i + 1];
            if (index < 0 || cookie <= 0) {
                charSequence = null;
            } else {
                charSequence = this.mApkAssets[cookie - 1].getStringFromPool(index);
            }
            retArray[j] = charSequence;
            i += 2;
            j++;
        }
        return retArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int[] getResourceIntArray(int resId) {
        ensureValidLocked();
        return nativeGetResourceIntArray(this.mObject, resId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int[] getStyleAttributes(int resId) {
        ensureValidLocked();
        return nativeGetStyleAttributes(this.mObject, resId);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean getThemeValue(long theme, int resId, TypedValue outValue, boolean resolveRefs) {
        Preconditions.checkNotNull(outValue, "outValue");
        synchronized (this) {
            ensureValidLocked();
            int cookie = nativeThemeGetAttributeValue(this.mObject, theme, resId, outValue, resolveRefs);
            if (cookie <= 0) {
                return false;
            }
            outValue.changingConfigurations = ActivityInfo.activityInfoConfigNativeToJava(outValue.changingConfigurations);
            if (outValue.type == 3) {
                outValue.string = this.mApkAssets[cookie - 1].getStringFromPool(outValue.data);
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void dumpTheme(long theme, int priority, String tag, String prefix) {
        ensureValidLocked();
        nativeThemeDump(this.mObject, theme, priority, tag, prefix);
    }

    public private protected String getResourceName(int resId) {
        String nativeGetResourceName;
        synchronized (this) {
            ensureValidLocked();
            nativeGetResourceName = nativeGetResourceName(this.mObject, resId);
        }
        return nativeGetResourceName;
    }

    public private protected String getResourcePackageName(int resId) {
        String nativeGetResourcePackageName;
        synchronized (this) {
            ensureValidLocked();
            nativeGetResourcePackageName = nativeGetResourcePackageName(this.mObject, resId);
        }
        return nativeGetResourcePackageName;
    }

    public private protected String getResourceTypeName(int resId) {
        String nativeGetResourceTypeName;
        synchronized (this) {
            ensureValidLocked();
            nativeGetResourceTypeName = nativeGetResourceTypeName(this.mObject, resId);
        }
        return nativeGetResourceTypeName;
    }

    public private protected String getResourceEntryName(int resId) {
        String nativeGetResourceEntryName;
        synchronized (this) {
            ensureValidLocked();
            nativeGetResourceEntryName = nativeGetResourceEntryName(this.mObject, resId);
        }
        return nativeGetResourceEntryName;
    }

    public private protected int getResourceIdentifier(String name, String defType, String defPackage) {
        int nativeGetResourceIdentifier;
        synchronized (this) {
            ensureValidLocked();
            nativeGetResourceIdentifier = nativeGetResourceIdentifier(this.mObject, name, defType, defPackage);
        }
        return nativeGetResourceIdentifier;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized CharSequence getPooledStringForCookie(int cookie, int id) {
        return getApkAssets()[cookie - 1].getStringFromPool(id);
    }

    public InputStream open(String fileName) throws IOException {
        return open(fileName, 2);
    }

    public InputStream open(String fileName, int accessMode) throws IOException {
        AssetInputStream assetInputStream;
        Preconditions.checkNotNull(fileName, "fileName");
        synchronized (this) {
            ensureOpenLocked();
            long asset = nativeOpenAsset(this.mObject, fileName, accessMode);
            if (asset == 0) {
                throw new FileNotFoundException("Asset file: " + fileName);
            }
            assetInputStream = new AssetInputStream(asset);
            incRefsLocked(assetInputStream.hashCode());
        }
        return assetInputStream;
    }

    public AssetFileDescriptor openFd(String fileName) throws IOException {
        AssetFileDescriptor assetFileDescriptor;
        Preconditions.checkNotNull(fileName, "fileName");
        synchronized (this) {
            ensureOpenLocked();
            ParcelFileDescriptor pfd = nativeOpenAssetFd(this.mObject, fileName, this.mOffsets);
            if (pfd == null) {
                throw new FileNotFoundException("Asset file: " + fileName);
            }
            assetFileDescriptor = new AssetFileDescriptor(pfd, this.mOffsets[0], this.mOffsets[1]);
        }
        return assetFileDescriptor;
    }

    public String[] list(String path) throws IOException {
        String[] nativeList;
        Preconditions.checkNotNull(path, "path");
        synchronized (this) {
            ensureValidLocked();
            nativeList = nativeList(this.mObject, path);
        }
        return nativeList;
    }

    private protected InputStream openNonAsset(String fileName) throws IOException {
        return openNonAsset(0, fileName, 2);
    }

    private protected InputStream openNonAsset(String fileName, int accessMode) throws IOException {
        return openNonAsset(0, fileName, accessMode);
    }

    private protected InputStream openNonAsset(int cookie, String fileName) throws IOException {
        return openNonAsset(cookie, fileName, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public InputStream openNonAsset(int cookie, String fileName, int accessMode) throws IOException {
        AssetInputStream assetInputStream;
        Preconditions.checkNotNull(fileName, "fileName");
        synchronized (this) {
            ensureOpenLocked();
            long asset = nativeOpenNonAsset(this.mObject, cookie, fileName, accessMode);
            if (asset == 0) {
                throw new FileNotFoundException("Asset absolute file: " + fileName);
            }
            assetInputStream = new AssetInputStream(asset);
            incRefsLocked(assetInputStream.hashCode());
        }
        return assetInputStream;
    }

    public AssetFileDescriptor openNonAssetFd(String fileName) throws IOException {
        return openNonAssetFd(0, fileName);
    }

    public AssetFileDescriptor openNonAssetFd(int cookie, String fileName) throws IOException {
        AssetFileDescriptor assetFileDescriptor;
        Preconditions.checkNotNull(fileName, "fileName");
        synchronized (this) {
            ensureOpenLocked();
            ParcelFileDescriptor pfd = nativeOpenNonAssetFd(this.mObject, cookie, fileName, this.mOffsets);
            if (pfd == null) {
                throw new FileNotFoundException("Asset absolute file: " + fileName);
            }
            assetFileDescriptor = new AssetFileDescriptor(pfd, this.mOffsets[0], this.mOffsets[1]);
        }
        return assetFileDescriptor;
    }

    public XmlResourceParser openXmlResourceParser(String fileName) throws IOException {
        return openXmlResourceParser(0, fileName);
    }

    public XmlResourceParser openXmlResourceParser(int cookie, String fileName) throws IOException {
        XmlBlock block = openXmlBlockAsset(cookie, fileName);
        try {
            XmlResourceParser parser = block.newParser();
            if (parser == null) {
                throw new AssertionError("block.newParser() returned a null parser");
            }
            if (block != null) {
                $closeResource(null, block);
            }
            return parser;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                if (block != null) {
                    $closeResource(th, block);
                }
                throw th2;
            }
        }
    }

    synchronized XmlBlock openXmlBlockAsset(String fileName) throws IOException {
        return openXmlBlockAsset(0, fileName);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized XmlBlock openXmlBlockAsset(int cookie, String fileName) throws IOException {
        XmlBlock block;
        Preconditions.checkNotNull(fileName, "fileName");
        synchronized (this) {
            ensureOpenLocked();
            long xmlBlock = nativeOpenXmlAsset(this.mObject, cookie, fileName);
            if (xmlBlock == 0) {
                throw new FileNotFoundException("Asset XML file: " + fileName);
            }
            block = new XmlBlock(this, xmlBlock);
            incRefsLocked(block.hashCode());
        }
        return block;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void xmlBlockGone(int id) {
        decRefsLocked(id);
    }

    public private protected void applyStyle(long themePtr, int defStyleAttr, int defStyleRes, XmlBlock.Parser parser, int[] inAttrs, long outValuesAddress, long outIndicesAddress) {
        Preconditions.checkNotNull(inAttrs, "inAttrs");
        synchronized (this) {
            ensureValidLocked();
            nativeApplyStyle(this.mObject, themePtr, defStyleAttr, defStyleRes, parser != null ? parser.mParseState : 0L, inAttrs, outValuesAddress, outIndicesAddress);
        }
    }

    public private protected boolean resolveAttrs(long themePtr, int defStyleAttr, int defStyleRes, int[] inValues, int[] inAttrs, int[] outValues, int[] outIndices) {
        boolean nativeResolveAttrs;
        Preconditions.checkNotNull(inAttrs, "inAttrs");
        Preconditions.checkNotNull(outValues, "outValues");
        Preconditions.checkNotNull(outIndices, "outIndices");
        synchronized (this) {
            ensureValidLocked();
            nativeResolveAttrs = nativeResolveAttrs(this.mObject, themePtr, defStyleAttr, defStyleRes, inValues, inAttrs, outValues, outIndices);
        }
        return nativeResolveAttrs;
    }

    public private protected boolean retrieveAttributes(XmlBlock.Parser parser, int[] inAttrs, int[] outValues, int[] outIndices) {
        boolean nativeRetrieveAttributes;
        Preconditions.checkNotNull(parser, "parser");
        Preconditions.checkNotNull(inAttrs, "inAttrs");
        Preconditions.checkNotNull(outValues, "outValues");
        Preconditions.checkNotNull(outIndices, "outIndices");
        synchronized (this) {
            ensureValidLocked();
            nativeRetrieveAttributes = nativeRetrieveAttributes(this.mObject, parser.mParseState, inAttrs, outValues, outIndices);
        }
        return nativeRetrieveAttributes;
    }

    public private protected long createTheme() {
        long themePtr;
        synchronized (this) {
            ensureValidLocked();
            themePtr = nativeThemeCreate(this.mObject);
            incRefsLocked(themePtr);
        }
        return themePtr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void releaseTheme(long themePtr) {
        nativeThemeDestroy(themePtr);
        decRefsLocked(themePtr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void applyStyleToTheme(long themePtr, int resId, boolean force) {
        ensureValidLocked();
        nativeThemeApplyStyle(this.mObject, themePtr, resId, force);
    }

    protected void finalize() throws Throwable {
        if (this.mObject != 0) {
            nativeDestroy(this.mObject);
        }
    }

    /* loaded from: classes.dex */
    public final class AssetInputStream extends InputStream {
        private long mAssetNativePtr;
        private long mLength;
        private long mMarkPos;

        private protected final int getAssetInt() {
            throw new UnsupportedOperationException();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final long getNativeAsset() {
            return this.mAssetNativePtr;
        }

        private AssetInputStream(long assetNativePtr) {
            this.mAssetNativePtr = assetNativePtr;
            this.mLength = AssetManager.nativeAssetGetLength(assetNativePtr);
        }

        @Override // java.io.InputStream
        public final int read() throws IOException {
            ensureOpen();
            return AssetManager.nativeAssetReadChar(this.mAssetNativePtr);
        }

        @Override // java.io.InputStream
        public final int read(byte[] b) throws IOException {
            ensureOpen();
            Preconditions.checkNotNull(b, "b");
            return AssetManager.nativeAssetRead(this.mAssetNativePtr, b, 0, b.length);
        }

        @Override // java.io.InputStream
        public final int read(byte[] b, int off, int len) throws IOException {
            ensureOpen();
            Preconditions.checkNotNull(b, "b");
            return AssetManager.nativeAssetRead(this.mAssetNativePtr, b, off, len);
        }

        @Override // java.io.InputStream
        public final long skip(long n) throws IOException {
            ensureOpen();
            long pos = AssetManager.nativeAssetSeek(this.mAssetNativePtr, 0L, 0);
            if (pos + n > this.mLength) {
                n = this.mLength - pos;
            }
            if (n > 0) {
                AssetManager.nativeAssetSeek(this.mAssetNativePtr, n, 0);
            }
            return n;
        }

        @Override // java.io.InputStream
        public final int available() throws IOException {
            ensureOpen();
            long len = AssetManager.nativeAssetGetRemainingLength(this.mAssetNativePtr);
            if (len > 2147483647L) {
                return Integer.MAX_VALUE;
            }
            return (int) len;
        }

        @Override // java.io.InputStream
        public final boolean markSupported() {
            return true;
        }

        @Override // java.io.InputStream
        public final void mark(int readlimit) {
            ensureOpen();
            this.mMarkPos = AssetManager.nativeAssetSeek(this.mAssetNativePtr, 0L, 0);
        }

        @Override // java.io.InputStream
        public final void reset() throws IOException {
            ensureOpen();
            AssetManager.nativeAssetSeek(this.mAssetNativePtr, this.mMarkPos, -1);
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public final void close() throws IOException {
            if (this.mAssetNativePtr != 0) {
                AssetManager.nativeAssetDestroy(this.mAssetNativePtr);
                this.mAssetNativePtr = 0L;
                synchronized (AssetManager.this) {
                    AssetManager.this.decRefsLocked(hashCode());
                }
            }
        }

        protected void finalize() throws Throwable {
            close();
        }

        private synchronized void ensureOpen() {
            if (this.mAssetNativePtr == 0) {
                throw new IllegalStateException("AssetInputStream is closed");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isUpToDate() {
        ApkAssets[] apkAssets;
        for (ApkAssets apkAssets2 : getApkAssets()) {
            if (!apkAssets2.isUpToDate()) {
                return false;
            }
        }
        return true;
    }

    public String[] getLocales() {
        String[] nativeGetLocales;
        synchronized (this) {
            ensureValidLocked();
            nativeGetLocales = nativeGetLocales(this.mObject, false);
        }
        return nativeGetLocales;
    }

    public synchronized String[] getNonSystemLocales() {
        ensureValidLocked();
        return nativeGetLocales(this.mObject, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Configuration[] getSizeConfigurations() {
        ensureValidLocked();
        return nativeGetSizeConfigurations(this.mObject);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setConfiguration(int mcc, int mnc, String locale, int orientation, int touchscreen, int density, int keyboard, int keyboardHidden, int navigation, int screenWidth, int screenHeight, int smallestScreenWidthDp, int screenWidthDp, int screenHeightDp, int screenLayout, int uiMode, int colorMode, int majorVersion) {
        synchronized (this) {
            ensureValidLocked();
            nativeSetConfiguration(this.mObject, mcc, mnc, locale, orientation, touchscreen, density, keyboard, keyboardHidden, navigation, screenWidth, screenHeight, smallestScreenWidthDp, screenWidthDp, screenHeightDp, screenLayout, uiMode, colorMode, majorVersion);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SparseArray<String> getAssignedPackageIdentifiers() {
        SparseArray<String> nativeGetAssignedPackageIdentifiers;
        synchronized (this) {
            ensureValidLocked();
            nativeGetAssignedPackageIdentifiers = nativeGetAssignedPackageIdentifiers(this.mObject);
        }
        return nativeGetAssignedPackageIdentifiers;
    }

    @GuardedBy("this")
    private synchronized void incRefsLocked(long id) {
        this.mNumRefs++;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @GuardedBy("this")
    public synchronized void decRefsLocked(long id) {
        this.mNumRefs--;
        if (this.mNumRefs == 0 && this.mObject != 0) {
            nativeDestroy(this.mObject);
            this.mObject = 0L;
            if (this.mApkAssets != null) {
                int size = this.mApkAssets.length;
                for (int i = sSystemApkAssets.length; i < size; i++) {
                    String path = this.mApkAssets[i].getAssetPath();
                    Log.i(TAG, "mApkAssets[" + i + "]: " + path);
                    if (!TextUtils.isEmpty(path) && path.startsWith("/storage/")) {
                        try {
                            this.mApkAssets[i].close();
                        } catch (Throwable throwable) {
                            Log.w(TAG, "throwable: " + throwable.getMessage());
                        }
                        this.mApkAssets[i] = null;
                    }
                }
            }
            this.mApkAssets = sEmptyApkAssets;
        }
    }
}
