package android.content.pm;

import android.Manifest;
import android.app.AppGlobals;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageParserCacheHelper;
import android.content.pm.split.DefaultSplitAssetLoader;
import android.content.pm.split.SplitAssetDependencyLoader;
import android.content.pm.split.SplitAssetLoader;
import android.content.pm.split.SplitDependencyLoader;
import android.content.res.ApkAssets;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.provider.SettingsStringUtil;
import android.service.notification.ZenModeConfig;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructStat;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.ByteStringUtils;
import android.util.DisplayMetrics;
import android.util.PackageUtils;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.TypedValue;
import android.util.apk.ApkSignatureVerifier;
import com.android.internal.R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.DumpHeapActivity;
import com.android.internal.os.ClassLoaderFactory;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.XmlUtils;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import libcore.io.IoUtils;
import libcore.util.EmptyArray;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class PackageParser {
    public static final String ANDROID_MANIFEST_FILENAME = "AndroidManifest.xml";
    private static final String ANDROID_RESOURCES = "http://schemas.android.com/apk/res/android";
    public static final String APK_FILE_EXTENSION = ".apk";
    private static final Set<String> CHILD_PACKAGE_TAGS;
    private static final boolean DEBUG_BACKUP = false;
    private static final boolean DEBUG_JAR = false;
    private static final boolean DEBUG_PARSER = false;
    private static float DEFAULT_PRE_O_MAX_ASPECT_RATIO = 0.0f;
    private static final boolean LOG_PARSE_TIMINGS = Build.IS_DEBUGGABLE;
    private static final int LOG_PARSE_TIMINGS_THRESHOLD_MS = 100;
    private static final boolean LOG_UNSAFE_BROADCASTS = false;
    private static final String METADATA_MAX_ASPECT_RATIO = "android.max_aspect";
    private static final String MNT_EXPAND = "/mnt/expand/";
    private static final boolean MULTI_PACKAGE_APK_ENABLED;
    private protected static final NewPermissionInfo[] NEW_PERMISSIONS;
    public static final int PARSE_CHATTY = Integer.MIN_VALUE;
    public static final int PARSE_COLLECT_CERTIFICATES = 32;
    private static final int PARSE_DEFAULT_INSTALL_LOCATION = -1;
    private static final int PARSE_DEFAULT_TARGET_SANDBOX = 1;
    public static final int PARSE_ENFORCE_CODE = 64;
    public static final int PARSE_EXTERNAL_STORAGE = 8;
    public static final int PARSE_FORCE_SDK = 128;
    @Deprecated
    public static final int PARSE_FORWARD_LOCK = 4;
    public static final int PARSE_IGNORE_PROCESSES = 2;
    public static final int PARSE_IS_SYSTEM_DIR = 16;
    public static final int PARSE_MUST_BE_APK = 1;
    private static final String PROPERTY_CHILD_PACKAGES_ENABLED = "persist.sys.child_packages_enabled";
    private static final int RECREATE_ON_CONFIG_CHANGES_MASK = 3;
    private static final boolean RIGID_PARSER = false;
    private static final Set<String> SAFE_BROADCASTS;
    private static final String[] SDK_CODENAMES;
    private static final int SDK_VERSION;
    public static final SplitPermissionInfo[] SPLIT_PERMISSIONS;
    private static final String TAG = "PackageParser";
    private static final String TAG_ADOPT_PERMISSIONS = "adopt-permissions";
    private static final String TAG_APPLICATION = "application";
    private static final String TAG_COMPATIBLE_SCREENS = "compatible-screens";
    private static final String TAG_EAT_COMMENT = "eat-comment";
    private static final String TAG_FEATURE_GROUP = "feature-group";
    private static final String TAG_INSTRUMENTATION = "instrumentation";
    private static final String TAG_KEY_SETS = "key-sets";
    private static final String TAG_MANIFEST = "manifest";
    private static final String TAG_ORIGINAL_PACKAGE = "original-package";
    private static final String TAG_OVERLAY = "overlay";
    private static final String TAG_PACKAGE = "package";
    private static final String TAG_PACKAGE_VERIFIER = "package-verifier";
    private static final String TAG_PERMISSION = "permission";
    private static final String TAG_PERMISSION_GROUP = "permission-group";
    private static final String TAG_PERMISSION_TREE = "permission-tree";
    private static final String TAG_PROTECTED_BROADCAST = "protected-broadcast";
    private static final String TAG_RESTRICT_UPDATE = "restrict-update";
    private static final String TAG_SUPPORTS_INPUT = "supports-input";
    private static final String TAG_SUPPORT_SCREENS = "supports-screens";
    private static final String TAG_USES_CONFIGURATION = "uses-configuration";
    private static final String TAG_USES_FEATURE = "uses-feature";
    private static final String TAG_USES_GL_TEXTURE = "uses-gl-texture";
    private static final String TAG_USES_PERMISSION = "uses-permission";
    private static final String TAG_USES_PERMISSION_SDK_23 = "uses-permission-sdk-23";
    private static final String TAG_USES_PERMISSION_SDK_M = "uses-permission-sdk-m";
    private static final String TAG_USES_SDK = "uses-sdk";
    private static final String TAG_USES_SPLIT = "uses-split";
    public static final AtomicInteger sCachedPackageReadCount;
    private static boolean sCompatibilityModeEnabled;
    private static final Comparator<String> sSplitNameComparator;
    @Deprecated
    private String mArchiveSourcePath;
    private File mCacheDir;
    public protected Callback mCallback;
    private boolean mOnlyCoreApps;
    private ParsePackageItemArgs mParseInstrumentationArgs;
    private String[] mSeparateProcesses;
    private int mParseError = 1;
    private DisplayMetrics mMetrics = new DisplayMetrics();

    /* loaded from: classes.dex */
    public interface Callback {
        synchronized String[] getOverlayApks(String str);

        synchronized String[] getOverlayPaths(String str, String str2);

        synchronized boolean hasFeature(String str);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface ParseFlags {
    }

    static {
        MULTI_PACKAGE_APK_ENABLED = Build.IS_DEBUGGABLE && SystemProperties.getBoolean(PROPERTY_CHILD_PACKAGES_ENABLED, false);
        DEFAULT_PRE_O_MAX_ASPECT_RATIO = 1.86f;
        CHILD_PACKAGE_TAGS = new ArraySet();
        CHILD_PACKAGE_TAGS.add(TAG_APPLICATION);
        CHILD_PACKAGE_TAGS.add(TAG_USES_PERMISSION);
        CHILD_PACKAGE_TAGS.add(TAG_USES_PERMISSION_SDK_M);
        CHILD_PACKAGE_TAGS.add(TAG_USES_PERMISSION_SDK_23);
        CHILD_PACKAGE_TAGS.add(TAG_USES_CONFIGURATION);
        CHILD_PACKAGE_TAGS.add(TAG_USES_FEATURE);
        CHILD_PACKAGE_TAGS.add(TAG_FEATURE_GROUP);
        CHILD_PACKAGE_TAGS.add(TAG_USES_SDK);
        CHILD_PACKAGE_TAGS.add(TAG_SUPPORT_SCREENS);
        CHILD_PACKAGE_TAGS.add(TAG_INSTRUMENTATION);
        CHILD_PACKAGE_TAGS.add(TAG_USES_GL_TEXTURE);
        CHILD_PACKAGE_TAGS.add(TAG_COMPATIBLE_SCREENS);
        CHILD_PACKAGE_TAGS.add(TAG_SUPPORTS_INPUT);
        CHILD_PACKAGE_TAGS.add(TAG_EAT_COMMENT);
        sCachedPackageReadCount = new AtomicInteger();
        SAFE_BROADCASTS = new ArraySet();
        SAFE_BROADCASTS.add(Intent.ACTION_BOOT_COMPLETED);
        NEW_PERMISSIONS = new NewPermissionInfo[]{new NewPermissionInfo(Manifest.permission.WRITE_EXTERNAL_STORAGE, 4, 0), new NewPermissionInfo(Manifest.permission.READ_PHONE_STATE, 4, 0)};
        SPLIT_PERMISSIONS = new SplitPermissionInfo[]{new SplitPermissionInfo(Manifest.permission.WRITE_EXTERNAL_STORAGE, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10001), new SplitPermissionInfo(Manifest.permission.READ_CONTACTS, new String[]{Manifest.permission.READ_CALL_LOG}, 16), new SplitPermissionInfo(Manifest.permission.WRITE_CONTACTS, new String[]{Manifest.permission.WRITE_CALL_LOG}, 16)};
        SDK_VERSION = Build.VERSION.SDK_INT;
        SDK_CODENAMES = Build.VERSION.ACTIVE_CODENAMES;
        sCompatibilityModeEnabled = true;
        sSplitNameComparator = new SplitNameComparator();
    }

    /* loaded from: classes.dex */
    public static class NewPermissionInfo {
        public final int fileVersion;
        private protected final String name;
        private protected final int sdkVersion;

        public synchronized NewPermissionInfo(String name, int sdkVersion, int fileVersion) {
            this.name = name;
            this.sdkVersion = sdkVersion;
            this.fileVersion = fileVersion;
        }
    }

    /* loaded from: classes.dex */
    public static class SplitPermissionInfo {
        public final String[] newPerms;
        public final String rootPerm;
        public final int targetSdk;

        public synchronized SplitPermissionInfo(String rootPerm, String[] newPerms, int targetSdk) {
            this.rootPerm = rootPerm;
            this.newPerms = newPerms;
            this.targetSdk = targetSdk;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ParsePackageItemArgs {
        final int bannerRes;
        final int iconRes;
        final int labelRes;
        final int logoRes;
        final int nameRes;
        final String[] outError;
        final Package owner;
        final int roundIconRes;
        TypedArray sa;
        String tag;

        synchronized ParsePackageItemArgs(Package _owner, String[] _outError, int _nameRes, int _labelRes, int _iconRes, int _roundIconRes, int _logoRes, int _bannerRes) {
            this.owner = _owner;
            this.outError = _outError;
            this.nameRes = _nameRes;
            this.labelRes = _labelRes;
            this.iconRes = _iconRes;
            this.logoRes = _logoRes;
            this.bannerRes = _bannerRes;
            this.roundIconRes = _roundIconRes;
        }
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public static class ParseComponentArgs extends ParsePackageItemArgs {
        final int descriptionRes;
        final int enabledRes;
        int flags;
        final int processRes;
        final String[] sepProcesses;

        public synchronized ParseComponentArgs(Package _owner, String[] _outError, int _nameRes, int _labelRes, int _iconRes, int _roundIconRes, int _logoRes, int _bannerRes, String[] _sepProcesses, int _processRes, int _descriptionRes, int _enabledRes) {
            super(_owner, _outError, _nameRes, _labelRes, _iconRes, _roundIconRes, _logoRes, _bannerRes);
            this.sepProcesses = _sepProcesses;
            this.processRes = _processRes;
            this.descriptionRes = _descriptionRes;
            this.enabledRes = _enabledRes;
        }
    }

    /* loaded from: classes.dex */
    public static class PackageLite {
        public final String baseCodePath;
        public final int baseRevisionCode;
        public final String codePath;
        public final String[] configForSplit;
        public final boolean coreApp;
        public final boolean debuggable;
        public final boolean extractNativeLibs;
        private protected final int installLocation;
        public final boolean[] isFeatureSplits;
        public final boolean isolatedSplits;
        public final boolean multiArch;
        private protected final String packageName;
        public final String[] splitCodePaths;
        public final String[] splitNames;
        public final int[] splitRevisionCodes;
        public final boolean use32bitAbi;
        public final String[] usesSplitNames;
        public final VerifierInfo[] verifiers;
        public final int versionCode;
        public final int versionCodeMajor;

        public synchronized PackageLite(String codePath, ApkLite baseApk, String[] splitNames, boolean[] isFeatureSplits, String[] usesSplitNames, String[] configForSplit, String[] splitCodePaths, int[] splitRevisionCodes) {
            this.packageName = baseApk.packageName;
            this.versionCode = baseApk.versionCode;
            this.versionCodeMajor = baseApk.versionCodeMajor;
            this.installLocation = baseApk.installLocation;
            this.verifiers = baseApk.verifiers;
            this.splitNames = splitNames;
            this.isFeatureSplits = isFeatureSplits;
            this.usesSplitNames = usesSplitNames;
            this.configForSplit = configForSplit;
            this.codePath = codePath;
            this.baseCodePath = baseApk.codePath;
            this.splitCodePaths = splitCodePaths;
            this.baseRevisionCode = baseApk.revisionCode;
            this.splitRevisionCodes = splitRevisionCodes;
            this.coreApp = baseApk.coreApp;
            this.debuggable = baseApk.debuggable;
            this.multiArch = baseApk.multiArch;
            this.use32bitAbi = baseApk.use32bitAbi;
            this.extractNativeLibs = baseApk.extractNativeLibs;
            this.isolatedSplits = baseApk.isolatedSplits;
        }

        public synchronized List<String> getAllCodePaths() {
            ArrayList<String> paths = new ArrayList<>();
            paths.add(this.baseCodePath);
            if (!ArrayUtils.isEmpty(this.splitCodePaths)) {
                Collections.addAll(paths, this.splitCodePaths);
            }
            return paths;
        }
    }

    /* loaded from: classes.dex */
    public static class ApkLite {
        public final String codePath;
        public final String configForSplit;
        public final boolean coreApp;
        public final boolean debuggable;
        public final boolean extractNativeLibs;
        public final int installLocation;
        public boolean isFeatureSplit;
        public final boolean isolatedSplits;
        public final boolean multiArch;
        public final String packageName;
        public final int revisionCode;
        public final SigningDetails signingDetails;
        public final String splitName;
        public final boolean use32bitAbi;
        public final String usesSplitName;
        public final VerifierInfo[] verifiers;
        public final int versionCode;
        public final int versionCodeMajor;

        public synchronized ApkLite(String codePath, String packageName, String splitName, boolean isFeatureSplit, String configForSplit, String usesSplitName, int versionCode, int versionCodeMajor, int revisionCode, int installLocation, List<VerifierInfo> verifiers, SigningDetails signingDetails, boolean coreApp, boolean debuggable, boolean multiArch, boolean use32bitAbi, boolean extractNativeLibs, boolean isolatedSplits) {
            this.codePath = codePath;
            this.packageName = packageName;
            this.splitName = splitName;
            this.isFeatureSplit = isFeatureSplit;
            this.configForSplit = configForSplit;
            this.usesSplitName = usesSplitName;
            this.versionCode = versionCode;
            this.versionCodeMajor = versionCodeMajor;
            this.revisionCode = revisionCode;
            this.installLocation = installLocation;
            this.signingDetails = signingDetails;
            this.verifiers = (VerifierInfo[]) verifiers.toArray(new VerifierInfo[verifiers.size()]);
            this.coreApp = coreApp;
            this.debuggable = debuggable;
            this.multiArch = multiArch;
            this.use32bitAbi = use32bitAbi;
            this.extractNativeLibs = extractNativeLibs;
            this.isolatedSplits = isolatedSplits;
        }

        public synchronized long getLongVersionCode() {
            return PackageInfo.composeLongVersionCode(this.versionCodeMajor, this.versionCode);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class CachedComponentArgs {
        ParseComponentArgs mActivityAliasArgs;
        ParseComponentArgs mActivityArgs;
        ParseComponentArgs mProviderArgs;
        ParseComponentArgs mServiceArgs;

        private synchronized CachedComponentArgs() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public PackageParser() {
        this.mMetrics.setToDefaults();
    }

    private protected void setSeparateProcesses(String[] procs) {
        this.mSeparateProcesses = procs;
    }

    public synchronized void setOnlyCoreApps(boolean onlyCoreApps) {
        this.mOnlyCoreApps = onlyCoreApps;
    }

    public synchronized void setDisplayMetrics(DisplayMetrics metrics) {
        this.mMetrics = metrics;
    }

    public synchronized void setCacheDir(File cacheDir) {
        this.mCacheDir = cacheDir;
    }

    /* loaded from: classes.dex */
    public static final class CallbackImpl implements Callback {
        private final PackageManager mPm;

        public synchronized CallbackImpl(PackageManager pm) {
            this.mPm = pm;
        }

        @Override // android.content.pm.PackageParser.Callback
        public synchronized boolean hasFeature(String feature) {
            return this.mPm.hasSystemFeature(feature);
        }

        @Override // android.content.pm.PackageParser.Callback
        public synchronized String[] getOverlayPaths(String targetPackageName, String targetPath) {
            return null;
        }

        @Override // android.content.pm.PackageParser.Callback
        public synchronized String[] getOverlayApks(String targetPackageName) {
            return null;
        }
    }

    public synchronized void setCallback(Callback cb) {
        this.mCallback = cb;
    }

    public static final synchronized boolean isApkFile(File file) {
        return isApkPath(file.getName());
    }

    public static synchronized boolean isApkPath(String path) {
        return path.endsWith(APK_FILE_EXTENSION);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static PackageInfo generatePackageInfo(Package p, int[] gids, int flags, long firstInstallTime, long lastUpdateTime, Set<String> grantedPermissions, PackageUserState state) {
        return generatePackageInfo(p, gids, flags, firstInstallTime, lastUpdateTime, grantedPermissions, state, UserHandle.getCallingUserId());
    }

    private static synchronized boolean checkUseInstalledOrHidden(int flags, PackageUserState state, ApplicationInfo appInfo) {
        if ((flags & 536870912) != 0 || state.installed || appInfo == null || !appInfo.hiddenUntilInstalled) {
            if (!state.isAvailable(flags)) {
                if (appInfo == null || !appInfo.isSystemApp()) {
                    return false;
                }
                if ((4202496 & flags) == 0 && (536870912 & flags) == 0) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static synchronized boolean isAvailable(PackageUserState state) {
        return checkUseInstalledOrHidden(0, state, null);
    }

    private protected static PackageInfo generatePackageInfo(Package p, int[] gids, int flags, long firstInstallTime, long lastUpdateTime, Set<String> grantedPermissions, PackageUserState state, int userId) {
        int N;
        int N2;
        int N3;
        int N4;
        int N5;
        if (checkUseInstalledOrHidden(flags, state, p.applicationInfo) && p.isMatch(flags)) {
            PackageInfo pi = new PackageInfo();
            pi.packageName = p.packageName;
            pi.splitNames = p.splitNames;
            pi.versionCode = p.mVersionCode;
            pi.versionCodeMajor = p.mVersionCodeMajor;
            pi.baseRevisionCode = p.baseRevisionCode;
            pi.splitRevisionCodes = p.splitRevisionCodes;
            pi.versionName = p.mVersionName;
            pi.sharedUserId = p.mSharedUserId;
            pi.sharedUserLabel = p.mSharedUserLabel;
            pi.applicationInfo = generateApplicationInfo(p, flags, state, userId);
            pi.installLocation = p.installLocation;
            pi.isStub = p.isStub;
            pi.coreApp = p.coreApp;
            if ((pi.applicationInfo.flags & 1) != 0 || (pi.applicationInfo.flags & 128) != 0) {
                pi.requiredForAllUsers = p.mRequiredForAllUsers;
            }
            pi.restrictedAccountType = p.mRestrictedAccountType;
            pi.requiredAccountType = p.mRequiredAccountType;
            pi.overlayTarget = p.mOverlayTarget;
            pi.overlayCategory = p.mOverlayCategory;
            pi.overlayPriority = p.mOverlayPriority;
            pi.mOverlayIsStatic = p.mOverlayIsStatic;
            pi.compileSdkVersion = p.mCompileSdkVersion;
            pi.compileSdkVersionCodename = p.mCompileSdkVersionCodename;
            pi.firstInstallTime = firstInstallTime;
            pi.lastUpdateTime = lastUpdateTime;
            if ((flags & 256) != 0) {
                pi.gids = gids;
            }
            if ((flags & 16384) != 0) {
                int N6 = p.configPreferences != null ? p.configPreferences.size() : 0;
                if (N6 > 0) {
                    pi.configPreferences = new ConfigurationInfo[N6];
                    p.configPreferences.toArray(pi.configPreferences);
                }
                int N7 = p.reqFeatures != null ? p.reqFeatures.size() : 0;
                if (N7 > 0) {
                    pi.reqFeatures = new FeatureInfo[N7];
                    p.reqFeatures.toArray(pi.reqFeatures);
                }
                int N8 = p.featureGroups != null ? p.featureGroups.size() : 0;
                if (N8 > 0) {
                    pi.featureGroups = new FeatureGroupInfo[N8];
                    p.featureGroups.toArray(pi.featureGroups);
                }
            }
            int N9 = flags & 1;
            if (N9 != 0 && (N5 = p.activities.size()) > 0) {
                ActivityInfo[] res = new ActivityInfo[N5];
                int num = 0;
                int num2 = 0;
                while (num2 < N5) {
                    Activity a = p.activities.get(num2);
                    int N10 = N5;
                    if (state.isMatch(a.info, flags)) {
                        res[num] = generateActivityInfo(a, flags, state, userId);
                        num++;
                    }
                    num2++;
                    N5 = N10;
                }
                pi.activities = (ActivityInfo[]) ArrayUtils.trimToSize(res, num);
            }
            if ((flags & 2) != 0 && (N4 = p.receivers.size()) > 0) {
                ActivityInfo[] res2 = new ActivityInfo[N4];
                int num3 = 0;
                int num4 = 0;
                while (num4 < N4) {
                    Activity a2 = p.receivers.get(num4);
                    int N11 = N4;
                    if (state.isMatch(a2.info, flags)) {
                        res2[num3] = generateActivityInfo(a2, flags, state, userId);
                        num3++;
                    }
                    num4++;
                    N4 = N11;
                }
                pi.receivers = (ActivityInfo[]) ArrayUtils.trimToSize(res2, num3);
            }
            if ((flags & 4) != 0 && (N3 = p.services.size()) > 0) {
                ServiceInfo[] res3 = new ServiceInfo[N3];
                int num5 = 0;
                int num6 = 0;
                while (num6 < N3) {
                    Service s = p.services.get(num6);
                    int N12 = N3;
                    if (state.isMatch(s.info, flags)) {
                        res3[num5] = generateServiceInfo(s, flags, state, userId);
                        num5++;
                    }
                    num6++;
                    N3 = N12;
                }
                pi.services = (ServiceInfo[]) ArrayUtils.trimToSize(res3, num5);
            }
            if ((flags & 8) != 0 && (N2 = p.providers.size()) > 0) {
                ProviderInfo[] res4 = new ProviderInfo[N2];
                int num7 = 0;
                int num8 = 0;
                while (num8 < N2) {
                    Provider pr = p.providers.get(num8);
                    int N13 = N2;
                    if (state.isMatch(pr.info, flags)) {
                        res4[num7] = generateProviderInfo(pr, flags, state, userId);
                        num7++;
                    }
                    num8++;
                    N2 = N13;
                }
                pi.providers = (ProviderInfo[]) ArrayUtils.trimToSize(res4, num7);
            }
            if ((flags & 16) != 0 && (N = p.instrumentation.size()) > 0) {
                pi.instrumentation = new InstrumentationInfo[N];
                for (int i = 0; i < N; i++) {
                    pi.instrumentation[i] = generateInstrumentationInfo(p.instrumentation.get(i), flags);
                }
            }
            int N14 = flags & 4096;
            if (N14 != 0) {
                int N15 = p.permissions.size();
                if (N15 > 0) {
                    pi.permissions = new PermissionInfo[N15];
                    for (int i2 = 0; i2 < N15; i2++) {
                        pi.permissions[i2] = generatePermissionInfo(p.permissions.get(i2), flags);
                    }
                }
                int N16 = p.requestedPermissions.size();
                if (N16 > 0) {
                    pi.requestedPermissions = new String[N16];
                    pi.requestedPermissionsFlags = new int[N16];
                    for (int i3 = 0; i3 < N16; i3++) {
                        String perm = p.requestedPermissions.get(i3);
                        pi.requestedPermissions[i3] = perm;
                        int[] iArr = pi.requestedPermissionsFlags;
                        iArr[i3] = iArr[i3] | 1;
                        if (grantedPermissions != null && grantedPermissions.contains(perm)) {
                            int[] iArr2 = pi.requestedPermissionsFlags;
                            iArr2[i3] = iArr2[i3] | 2;
                        }
                    }
                }
            }
            int N17 = flags & 64;
            if (N17 != 0) {
                if (p.mSigningDetails.hasPastSigningCertificates()) {
                    pi.signatures = new Signature[1];
                    pi.signatures[0] = p.mSigningDetails.pastSigningCertificates[0];
                } else if (p.mSigningDetails.hasSignatures()) {
                    int numberOfSigs = p.mSigningDetails.signatures.length;
                    pi.signatures = new Signature[numberOfSigs];
                    System.arraycopy(p.mSigningDetails.signatures, 0, pi.signatures, 0, numberOfSigs);
                }
            }
            if ((134217728 & flags) != 0) {
                if (p.mSigningDetails != SigningDetails.UNKNOWN) {
                    pi.signingInfo = new SigningInfo(p.mSigningDetails);
                } else {
                    pi.signingInfo = null;
                }
            }
            return pi;
        }
        return null;
    }

    /* loaded from: classes.dex */
    private static class SplitNameComparator implements Comparator<String> {
        private synchronized SplitNameComparator() {
        }

        @Override // java.util.Comparator
        public synchronized int compare(String lhs, String rhs) {
            if (lhs == null) {
                return -1;
            }
            if (rhs == null) {
                return 1;
            }
            return lhs.compareTo(rhs);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static PackageLite parsePackageLite(File packageFile, int flags) throws PackageParserException {
        if (packageFile.isDirectory()) {
            return parseClusterPackageLite(packageFile, flags);
        }
        return parseMonolithicPackageLite(packageFile, flags);
    }

    private static synchronized PackageLite parseMonolithicPackageLite(File packageFile, int flags) throws PackageParserException {
        Trace.traceBegin(262144L, "parseApkLite");
        ApkLite baseApk = parseApkLite(packageFile, flags);
        String packagePath = packageFile.getAbsolutePath();
        Trace.traceEnd(262144L);
        return new PackageLite(packagePath, baseApk, null, null, null, null, null, null);
    }

    static synchronized PackageLite parseClusterPackageLite(File packageDir, int flags) throws PackageParserException {
        File[] files = packageDir.listFiles();
        if (ArrayUtils.isEmpty(files)) {
            throw new PackageParserException(-100, "No packages found in split");
        }
        Trace.traceBegin(262144L, "parseApkLite");
        ArrayMap<String, ApkLite> apks = new ArrayMap<>();
        int versionCode = 0;
        String packageName = null;
        for (File file : files) {
            if (isApkFile(file)) {
                ApkLite lite = parseApkLite(file, flags);
                if (packageName == null) {
                    packageName = lite.packageName;
                    versionCode = lite.versionCode;
                } else if (!packageName.equals(lite.packageName)) {
                    throw new PackageParserException(-101, "Inconsistent package " + lite.packageName + " in " + file + "; expected " + packageName);
                } else if (versionCode != lite.versionCode) {
                    throw new PackageParserException(-101, "Inconsistent version " + lite.versionCode + " in " + file + "; expected " + versionCode);
                }
                if (apks.put(lite.splitName, lite) != null) {
                    throw new PackageParserException(-101, "Split name " + lite.splitName + " defined more than once; most recent was " + file);
                }
            }
        }
        Trace.traceEnd(262144L);
        ApkLite baseApk = apks.remove(null);
        if (baseApk == null) {
            throw new PackageParserException(-101, "Missing base APK in " + packageDir);
        }
        int size = apks.size();
        String[] splitNames = null;
        boolean[] isFeatureSplits = null;
        String[] usesSplitNames = null;
        String[] configForSplits = null;
        String[] splitCodePaths = null;
        int[] splitRevisionCodes = null;
        if (size > 0) {
            String[] splitNames2 = new String[size];
            isFeatureSplits = new boolean[size];
            usesSplitNames = new String[size];
            configForSplits = new String[size];
            splitCodePaths = new String[size];
            splitRevisionCodes = new int[size];
            splitNames = (String[]) apks.keySet().toArray(splitNames2);
            Arrays.sort(splitNames, sSplitNameComparator);
            for (int i = 0; i < size; i++) {
                ApkLite apk = apks.get(splitNames[i]);
                usesSplitNames[i] = apk.usesSplitName;
                isFeatureSplits[i] = apk.isFeatureSplit;
                configForSplits[i] = apk.configForSplit;
                splitCodePaths[i] = apk.codePath;
                splitRevisionCodes[i] = apk.revisionCode;
            }
        }
        String codePath = packageDir.getAbsolutePath();
        return new PackageLite(codePath, baseApk, splitNames, isFeatureSplits, usesSplitNames, configForSplits, splitCodePaths, splitRevisionCodes);
    }

    private protected Package parsePackage(File packageFile, int flags, boolean useCaches) throws PackageParserException {
        Package parsed;
        Package parsed2 = useCaches ? getCachedResult(packageFile, flags) : null;
        if (parsed2 != null) {
            return parsed2;
        }
        long parseTime = LOG_PARSE_TIMINGS ? SystemClock.uptimeMillis() : 0L;
        if (packageFile.isDirectory()) {
            parsed = parseClusterPackage(packageFile, flags);
        } else {
            parsed = parseMonolithicPackage(packageFile, flags);
        }
        long cacheTime = LOG_PARSE_TIMINGS ? SystemClock.uptimeMillis() : 0L;
        cacheResult(packageFile, flags, parsed);
        if (LOG_PARSE_TIMINGS) {
            long parseTime2 = cacheTime - parseTime;
            long cacheTime2 = SystemClock.uptimeMillis() - cacheTime;
            if (parseTime2 + cacheTime2 > 100) {
                Slog.i(TAG, "Parse times for '" + packageFile + "': parse=" + parseTime2 + "ms, update_cache=" + cacheTime2 + " ms");
            }
        }
        return parsed;
    }

    private protected Package parsePackage(File packageFile, int flags) throws PackageParserException {
        return parsePackage(packageFile, flags, false);
    }

    private synchronized String getCacheKey(File packageFile, int flags) {
        return packageFile.getName() + '-' + flags;
    }

    @VisibleForTesting
    protected synchronized Package fromCacheEntry(byte[] bytes) {
        return fromCacheEntryStatic(bytes);
    }

    @VisibleForTesting
    public static synchronized Package fromCacheEntryStatic(byte[] bytes) {
        Parcel p = Parcel.obtain();
        p.unmarshall(bytes, 0, bytes.length);
        p.setDataPosition(0);
        PackageParserCacheHelper.ReadHelper helper = new PackageParserCacheHelper.ReadHelper(p);
        helper.startAndInstall();
        Package pkg = new Package(p);
        p.recycle();
        sCachedPackageReadCount.incrementAndGet();
        return pkg;
    }

    @VisibleForTesting
    protected synchronized byte[] toCacheEntry(Package pkg) {
        return toCacheEntryStatic(pkg);
    }

    @VisibleForTesting
    public static synchronized byte[] toCacheEntryStatic(Package pkg) {
        Parcel p = Parcel.obtain();
        PackageParserCacheHelper.WriteHelper helper = new PackageParserCacheHelper.WriteHelper(p);
        pkg.writeToParcel(p, 0);
        helper.finishAndUninstall();
        byte[] serialized = p.marshall();
        p.recycle();
        return serialized;
    }

    private static synchronized boolean isCacheUpToDate(File packageFile, File cacheFile) {
        try {
            StructStat pkg = Os.stat(packageFile.getAbsolutePath());
            StructStat cache = Os.stat(cacheFile.getAbsolutePath());
            return pkg.st_mtime < cache.st_mtime;
        } catch (ErrnoException ee) {
            if (ee.errno != OsConstants.ENOENT) {
                Slog.w("Error while stating package cache : ", ee);
            }
            return false;
        }
    }

    private synchronized Package getCachedResult(File packageFile, int flags) {
        String[] overlayApks;
        if (this.mCacheDir == null) {
            return null;
        }
        String cacheKey = getCacheKey(packageFile, flags);
        File cacheFile = new File(this.mCacheDir, cacheKey);
        try {
            if (isCacheUpToDate(packageFile, cacheFile)) {
                byte[] bytes = IoUtils.readFileAsByteArray(cacheFile.getAbsolutePath());
                Package p = fromCacheEntry(bytes);
                if (this.mCallback != null && (overlayApks = this.mCallback.getOverlayApks(p.packageName)) != null && overlayApks.length > 0) {
                    for (String overlayApk : overlayApks) {
                        if (!isCacheUpToDate(new File(overlayApk), cacheFile)) {
                            return null;
                        }
                    }
                }
                return p;
            }
            return null;
        } catch (Throwable e) {
            Slog.w(TAG, "Error reading package cache: ", e);
            cacheFile.delete();
            return null;
        }
    }

    private synchronized void cacheResult(File packageFile, int flags, Package parsed) {
        if (this.mCacheDir == null) {
            return;
        }
        try {
            String cacheKey = getCacheKey(packageFile, flags);
            File cacheFile = new File(this.mCacheDir, cacheKey);
            if (cacheFile.exists() && !cacheFile.delete()) {
                Slog.e(TAG, "Unable to delete cache file: " + cacheFile);
            }
            byte[] cacheEntry = toCacheEntry(parsed);
            if (cacheEntry == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(cacheFile);
                try {
                    fos.write(cacheEntry);
                    fos.close();
                } catch (Throwable th) {
                    try {
                        throw th;
                    } catch (Throwable th2) {
                        if (th != null) {
                            try {
                                fos.close();
                            } catch (Throwable th3) {
                                th.addSuppressed(th3);
                            }
                        } else {
                            fos.close();
                        }
                        throw th2;
                    }
                }
            } catch (IOException ioe) {
                Slog.w(TAG, "Error writing cache entry.", ioe);
                cacheFile.delete();
            }
        } catch (Throwable e) {
            Slog.w(TAG, "Error saving package cache.", e);
        }
    }

    private synchronized Package parseClusterPackage(File packageDir, int flags) throws PackageParserException {
        SplitAssetLoader assetLoader;
        PackageLite lite = parseClusterPackageLite(packageDir, 0);
        if (this.mOnlyCoreApps && !lite.coreApp) {
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "Not a coreApp: " + packageDir);
        }
        SparseArray<int[]> splitDependencies = null;
        if (lite.isolatedSplits && !ArrayUtils.isEmpty(lite.splitNames)) {
            try {
                splitDependencies = SplitAssetDependencyLoader.createDependenciesFromPackage(lite);
                assetLoader = new SplitAssetDependencyLoader(lite, splitDependencies, flags);
            } catch (SplitDependencyLoader.IllegalDependencyException e) {
                throw new PackageParserException(-101, e.getMessage());
            }
        } else {
            assetLoader = new DefaultSplitAssetLoader(lite, flags);
        }
        try {
            try {
                AssetManager assets = assetLoader.getBaseAssetManager();
                File baseApk = new File(lite.baseCodePath);
                Package pkg = parseBaseApk(baseApk, assets, flags);
                if (pkg == null) {
                    throw new PackageParserException(-100, "Failed to parse base APK: " + baseApk);
                }
                if (!ArrayUtils.isEmpty(lite.splitNames)) {
                    int num = lite.splitNames.length;
                    pkg.splitNames = lite.splitNames;
                    pkg.splitCodePaths = lite.splitCodePaths;
                    pkg.splitRevisionCodes = lite.splitRevisionCodes;
                    pkg.splitFlags = new int[num];
                    pkg.splitPrivateFlags = new int[num];
                    pkg.applicationInfo.splitNames = pkg.splitNames;
                    pkg.applicationInfo.splitDependencies = splitDependencies;
                    pkg.applicationInfo.splitClassLoaderNames = new String[num];
                    for (int i = 0; i < num; i++) {
                        AssetManager splitAssets = assetLoader.getSplitAssetManager(i);
                        parseSplitApk(pkg, i, splitAssets, flags);
                    }
                }
                pkg.setCodePath(packageDir.getCanonicalPath());
                pkg.setUse32bitAbi(lite.use32bitAbi);
                return pkg;
            } catch (IOException e2) {
                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to get path: " + lite.baseCodePath, e2);
            }
        } finally {
            IoUtils.closeQuietly(assetLoader);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Deprecated
    public Package parseMonolithicPackage(File apkFile, int flags) throws PackageParserException {
        PackageLite lite = parseMonolithicPackageLite(apkFile, flags);
        if (this.mOnlyCoreApps && !lite.coreApp) {
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "Not a coreApp: " + apkFile);
        }
        SplitAssetLoader assetLoader = new DefaultSplitAssetLoader(lite, flags);
        try {
            try {
                Package pkg = parseBaseApk(apkFile, assetLoader.getBaseAssetManager(), flags);
                pkg.setCodePath(apkFile.getCanonicalPath());
                pkg.setUse32bitAbi(lite.use32bitAbi);
                return pkg;
            } catch (IOException e) {
                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to get path: " + apkFile, e);
            }
        } finally {
            IoUtils.closeQuietly(assetLoader);
        }
    }

    private synchronized Package parseBaseApk(File apkFile, AssetManager assets, int flags) throws PackageParserException {
        XmlResourceParser parser;
        String apkPath = apkFile.getAbsolutePath();
        String volumeUuid = null;
        if (apkPath.startsWith(MNT_EXPAND)) {
            int end = apkPath.indexOf(47, MNT_EXPAND.length());
            volumeUuid = apkPath.substring(MNT_EXPAND.length(), end);
        }
        String volumeUuid2 = volumeUuid;
        this.mParseError = 1;
        this.mArchiveSourcePath = apkFile.getAbsolutePath();
        try {
            try {
                int cookie = assets.findCookieForPath(apkPath);
                if (cookie == 0) {
                    throw new PackageParserException(-101, "Failed adding asset path: " + apkPath);
                }
                parser = assets.openXmlResourceParser(cookie, ANDROID_MANIFEST_FILENAME);
                try {
                    Resources res = new Resources(assets, this.mMetrics, null);
                    String[] outError = new String[1];
                    Package pkg = parseBaseApk(apkPath, res, parser, flags, outError);
                    if (pkg != null) {
                        pkg.setVolumeUuid(volumeUuid2);
                        pkg.setApplicationVolumeUuid(volumeUuid2);
                        pkg.setBaseCodePath(apkPath);
                        pkg.setSigningDetails(SigningDetails.UNKNOWN);
                        IoUtils.closeQuietly(parser);
                        return pkg;
                    }
                    throw new PackageParserException(this.mParseError, apkPath + " (at " + parser.getPositionDescription() + "): " + outError[0]);
                } catch (PackageParserException e) {
                    throw e;
                } catch (Exception e2) {
                    e = e2;
                    throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to read manifest from " + apkPath, e);
                } catch (Throwable th) {
                    e = th;
                    IoUtils.closeQuietly(parser);
                    throw e;
                }
            } catch (Throwable th2) {
                e = th2;
                parser = null;
            }
        } catch (PackageParserException e3) {
            throw e3;
        } catch (Exception e4) {
            e = e4;
        }
    }

    private synchronized void parseSplitApk(Package pkg, int splitIndex, AssetManager assets, int flags) throws PackageParserException {
        String apkPath = pkg.splitCodePaths[splitIndex];
        this.mParseError = 1;
        this.mArchiveSourcePath = apkPath;
        XmlResourceParser parser = null;
        try {
            try {
                int cookie = assets.findCookieForPath(apkPath);
                if (cookie == 0) {
                    throw new PackageParserException(-101, "Failed adding asset path: " + apkPath);
                }
                XmlResourceParser parser2 = assets.openXmlResourceParser(cookie, ANDROID_MANIFEST_FILENAME);
                try {
                    Resources res = new Resources(assets, this.mMetrics, null);
                    String[] outError = new String[1];
                    if (parseSplitApk(pkg, res, parser2, flags, splitIndex, outError) != null) {
                        IoUtils.closeQuietly(parser2);
                        return;
                    }
                    try {
                        throw new PackageParserException(this.mParseError, apkPath + " (at " + parser2.getPositionDescription() + "): " + outError[0]);
                    } catch (PackageParserException e) {
                        e = e;
                    } catch (Exception e2) {
                        e = e2;
                        throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to read manifest from " + apkPath, e);
                    } catch (Throwable th) {
                        e = th;
                        parser = parser2;
                        IoUtils.closeQuietly(parser);
                        throw e;
                    }
                } catch (PackageParserException e3) {
                    e = e3;
                } catch (Exception e4) {
                    e = e4;
                } catch (Throwable th2) {
                    e = th2;
                }
            } catch (Throwable th3) {
                e = th3;
            }
        } catch (PackageParserException e5) {
            throw e5;
        } catch (Exception e6) {
            e = e6;
        }
    }

    private synchronized Package parseSplitApk(Package pkg, Resources res, XmlResourceParser parser, int flags, int splitIndex, String[] outError) throws XmlPullParserException, IOException, PackageParserException {
        parsePackageSplitNames(parser, parser);
        this.mParseInstrumentationArgs = null;
        boolean foundApp = false;
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                break;
            } else if (type != 3 && type != 4) {
                String tagName = parser.getName();
                if (tagName.equals(TAG_APPLICATION)) {
                    if (foundApp) {
                        Slog.w(TAG, "<manifest> has more than one <application>");
                        XmlUtils.skipCurrentTag(parser);
                    } else {
                        foundApp = true;
                        if (!parseSplitApplication(pkg, res, parser, flags, splitIndex, outError)) {
                            return null;
                        }
                    }
                } else {
                    Slog.w(TAG, "Unknown element under <manifest>: " + parser.getName() + " at " + this.mArchiveSourcePath + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + parser.getPositionDescription());
                    XmlUtils.skipCurrentTag(parser);
                }
            }
        }
        if (!foundApp) {
            outError[0] = "<manifest> does not contain an <application>";
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_EMPTY;
        }
        return pkg;
    }

    public static synchronized ArraySet<PublicKey> toSigningKeys(Signature[] signatures) throws CertificateException {
        ArraySet<PublicKey> keys = new ArraySet<>(signatures.length);
        for (Signature signature : signatures) {
            keys.add(signature.getPublicKey());
        }
        return keys;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void collectCertificates(Package pkg, boolean skipVerify) throws PackageParserException {
        collectCertificatesInternal(pkg, skipVerify);
        int childCount = pkg.childPackages != null ? pkg.childPackages.size() : 0;
        for (int i = 0; i < childCount; i++) {
            Package childPkg = pkg.childPackages.get(i);
            childPkg.mSigningDetails = pkg.mSigningDetails;
        }
    }

    private static synchronized void collectCertificatesInternal(Package pkg, boolean skipVerify) throws PackageParserException {
        pkg.mSigningDetails = SigningDetails.UNKNOWN;
        Trace.traceBegin(262144L, "collectCertificates");
        try {
            collectCertificates(pkg, new File(pkg.baseCodePath), skipVerify);
            if (!ArrayUtils.isEmpty(pkg.splitCodePaths)) {
                for (int i = 0; i < pkg.splitCodePaths.length; i++) {
                    collectCertificates(pkg, new File(pkg.splitCodePaths[i]), skipVerify);
                }
            }
        } finally {
            Trace.traceEnd(262144L);
        }
    }

    public protected static void collectCertificates(Package pkg, File apkFile, boolean skipVerify) throws PackageParserException {
        SigningDetails verified;
        String apkPath = apkFile.getAbsolutePath();
        int minSignatureScheme = 1;
        if (pkg.applicationInfo.isStaticSharedLibrary()) {
            minSignatureScheme = 2;
        }
        if (skipVerify) {
            verified = ApkSignatureVerifier.plsCertsNoVerifyOnlyCerts(apkPath, minSignatureScheme);
        } else {
            verified = ApkSignatureVerifier.verify(apkPath, minSignatureScheme);
        }
        if (pkg.mSigningDetails == SigningDetails.UNKNOWN) {
            pkg.mSigningDetails = verified;
        } else if (!Signature.areExactMatch(pkg.mSigningDetails.signatures, verified.signatures)) {
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES, apkPath + " has mismatched certificates");
        }
    }

    private static synchronized AssetManager newConfiguredAssetManager() {
        AssetManager assetManager = new AssetManager();
        assetManager.setConfiguration(0, 0, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, Build.VERSION.RESOURCES_SDK_INT);
        return assetManager;
    }

    public static synchronized ApkLite parseApkLite(File apkFile, int flags) throws PackageParserException {
        return parseApkLiteInner(apkFile, null, null, flags);
    }

    public static synchronized ApkLite parseApkLite(FileDescriptor fd, String debugPathName, int flags) throws PackageParserException {
        return parseApkLiteInner(null, fd, debugPathName, flags);
    }

    private static synchronized ApkLite parseApkLiteInner(File apkFile, FileDescriptor fd, String debugPathName, int flags) throws PackageParserException {
        ApkAssets loadFromPath;
        SigningDetails signingDetails;
        String apkPath = fd != null ? debugPathName : apkFile.getAbsolutePath();
        ApkAssets apkAssets = null;
        try {
            try {
                try {
                    if (fd != null) {
                        loadFromPath = ApkAssets.loadFromFd(fd, debugPathName, false, false);
                    } else {
                        loadFromPath = ApkAssets.loadFromPath(apkPath);
                    }
                    ApkAssets apkAssets2 = loadFromPath;
                    XmlResourceParser parser = apkAssets2.openXml(ANDROID_MANIFEST_FILENAME);
                    if ((flags & 32) != 0) {
                        Package tempPkg = new Package((String) null);
                        boolean skipVerify = (flags & 16) != 0;
                        Trace.traceBegin(262144L, "collectCertificates");
                        try {
                            collectCertificates(tempPkg, apkFile, skipVerify);
                            Trace.traceEnd(262144L);
                            signingDetails = tempPkg.mSigningDetails;
                        } catch (Throwable th) {
                            Trace.traceEnd(262144L);
                            throw th;
                        }
                    } else {
                        signingDetails = SigningDetails.UNKNOWN;
                    }
                    ApkLite parseApkLite = parseApkLite(apkPath, parser, parser, signingDetails);
                    IoUtils.closeQuietly(parser);
                    if (apkAssets2 != null) {
                        try {
                            apkAssets2.close();
                        } catch (Throwable th2) {
                        }
                    }
                    return parseApkLite;
                } catch (IOException e) {
                    throw new PackageParserException(-100, "Failed to parse " + apkPath);
                }
            } catch (Throwable th3) {
                IoUtils.closeQuietly((AutoCloseable) null);
                if (0 != 0) {
                    try {
                        apkAssets.close();
                    } catch (Throwable th4) {
                    }
                }
                throw th3;
            }
        } catch (IOException | RuntimeException | XmlPullParserException e2) {
            Slog.w(TAG, "Failed to parse " + apkPath, e2);
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to parse " + apkPath, e2);
        }
    }

    private static synchronized String validateName(String name, boolean requireSeparator, boolean requireFilename) {
        int N = name.length();
        boolean hasSep = false;
        boolean front = true;
        for (int i = 0; i < N; i++) {
            char c = name.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                front = false;
            } else if (front || ((c < '0' || c > '9') && c != '_')) {
                if (c != '.') {
                    return "bad character '" + c + "'";
                }
                hasSep = true;
                front = true;
            }
        }
        if (requireFilename && !FileUtils.isValidExtFilename(name)) {
            return "Invalid filename";
        }
        if (hasSep || !requireSeparator) {
            return null;
        }
        return "must have at least one '.' separator";
    }

    private static synchronized Pair<String, String> parsePackageSplitNames(XmlPullParser parser, AttributeSet attrs) throws IOException, XmlPullParserException, PackageParserException {
        int type;
        String error;
        do {
            type = parser.next();
            if (type == 2) {
                break;
            }
        } while (type != 1);
        if (type != 2) {
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "No start tag found");
        }
        if (!parser.getName().equals(TAG_MANIFEST)) {
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "No <manifest> tag");
        }
        String packageName = attrs.getAttributeValue(null, "package");
        if (!ZenModeConfig.SYSTEM_AUTHORITY.equals(packageName) && (error = validateName(packageName, true, true)) != null) {
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME, "Invalid manifest package: " + error);
        }
        String splitName = attrs.getAttributeValue(null, "split");
        if (splitName != null) {
            if (splitName.length() == 0) {
                splitName = null;
            } else {
                String error2 = validateName(splitName, false, false);
                if (error2 != null) {
                    throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME, "Invalid manifest split: " + error2);
                }
            }
        }
        return Pair.create(packageName.intern(), splitName != null ? splitName.intern() : splitName);
    }

    private static synchronized ApkLite parseApkLite(String codePath, XmlPullParser parser, AttributeSet attrs, SigningDetails signingDetails) throws IOException, XmlPullParserException, PackageParserException {
        boolean isolatedSplits;
        int type;
        int searchDepth;
        Pair<String, String> packageSplit = parsePackageSplitNames(parser, attrs);
        boolean debuggable = false;
        boolean multiArch = false;
        boolean use32bitAbi = false;
        boolean extractNativeLibs = true;
        String configForSplit = null;
        String usesSplitName = null;
        boolean isFeatureSplit = false;
        boolean isFeatureSplit2 = false;
        boolean isolatedSplits2 = false;
        int revisionCode = 0;
        int revisionCode2 = 0;
        int versionCodeMajor = 0;
        int installLocation = -1;
        int installLocation2 = 0;
        while (installLocation2 < attrs.getAttributeCount()) {
            String attr = attrs.getAttributeName(installLocation2);
            boolean debuggable2 = debuggable;
            boolean debuggable3 = attr.equals("installLocation");
            if (debuggable3) {
                installLocation = attrs.getAttributeIntValue(installLocation2, -1);
            } else if (attr.equals("versionCode")) {
                versionCodeMajor = attrs.getAttributeIntValue(installLocation2, 0);
            } else if (attr.equals("versionCodeMajor")) {
                revisionCode2 = attrs.getAttributeIntValue(installLocation2, 0);
            } else if (attr.equals("revisionCode")) {
                revisionCode = attrs.getAttributeIntValue(installLocation2, 0);
            } else if (attr.equals("coreApp")) {
                isolatedSplits2 = attrs.getAttributeBooleanValue(installLocation2, false);
            } else if (attr.equals("isolatedSplits")) {
                isFeatureSplit2 = attrs.getAttributeBooleanValue(installLocation2, false);
            } else if (attr.equals("configForSplit")) {
                String configForSplit2 = attrs.getAttributeValue(installLocation2);
                configForSplit = configForSplit2;
            } else if (attr.equals("isFeatureSplit")) {
                boolean isFeatureSplit3 = attrs.getAttributeBooleanValue(installLocation2, false);
                isFeatureSplit = isFeatureSplit3;
            }
            installLocation2++;
            debuggable = debuggable2;
        }
        boolean debuggable4 = debuggable;
        int type2 = 1;
        int searchDepth2 = parser.getDepth() + 1;
        List<VerifierInfo> verifiers = new ArrayList<>();
        while (true) {
            isolatedSplits = isFeatureSplit2;
            int next = parser.next();
            if (next != type2 && ((type = next) != 3 || parser.getDepth() >= searchDepth2)) {
                if (type != 3) {
                    if (type != 4 && parser.getDepth() == searchDepth2) {
                        searchDepth = searchDepth2;
                        if (TAG_PACKAGE_VERIFIER.equals(parser.getName())) {
                            VerifierInfo verifier = parseVerifier(attrs);
                            if (verifier != null) {
                                verifiers.add(verifier);
                            }
                        } else if (TAG_APPLICATION.equals(parser.getName())) {
                            int i = 0;
                            while (i < attrs.getAttributeCount()) {
                                String attr2 = attrs.getAttributeName(i);
                                int type3 = type;
                                if ("debuggable".equals(attr2)) {
                                    debuggable4 = attrs.getAttributeBooleanValue(i, false);
                                }
                                if ("multiArch".equals(attr2)) {
                                    multiArch = attrs.getAttributeBooleanValue(i, false);
                                }
                                if ("use32bitAbi".equals(attr2)) {
                                    use32bitAbi = attrs.getAttributeBooleanValue(i, false);
                                }
                                if ("extractNativeLibs".equals(attr2)) {
                                    extractNativeLibs = attrs.getAttributeBooleanValue(i, true);
                                }
                                i++;
                                type = type3;
                            }
                            isFeatureSplit2 = isolatedSplits;
                            searchDepth2 = searchDepth;
                            type2 = 1;
                        } else {
                            type2 = 1;
                            if (TAG_USES_SPLIT.equals(parser.getName())) {
                                if (usesSplitName != null) {
                                    Slog.w(TAG, "Only one <uses-split> permitted. Ignoring others.");
                                } else {
                                    usesSplitName = attrs.getAttributeValue(ANDROID_RESOURCES, "name");
                                    if (usesSplitName == null) {
                                        throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "<uses-split> tag requires 'android:name' attribute");
                                    }
                                }
                            }
                        }
                    } else {
                        searchDepth = searchDepth2;
                    }
                    type2 = 1;
                } else {
                    searchDepth = searchDepth2;
                    type2 = 1;
                }
                isFeatureSplit2 = isolatedSplits;
                searchDepth2 = searchDepth;
            }
        }
        return new ApkLite(codePath, packageSplit.first, packageSplit.second, isFeatureSplit, configForSplit, usesSplitName, versionCodeMajor, revisionCode2, revisionCode, installLocation, verifiers, signingDetails, isolatedSplits2, debuggable4, multiArch, use32bitAbi, extractNativeLibs, isolatedSplits);
    }

    private synchronized boolean parseBaseApkChild(Package parentPkg, Resources res, XmlResourceParser parser, int flags, String[] outError) throws XmlPullParserException, IOException {
        String childPackageName = parser.getAttributeValue(null, "package");
        if (validateName(childPackageName, true, false) != null) {
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME;
            return false;
        } else if (childPackageName.equals(parentPkg.packageName)) {
            String message = "Child package name cannot be equal to parent package name: " + parentPkg.packageName;
            Slog.w(TAG, message);
            outError[0] = message;
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        } else if (parentPkg.hasChildPackage(childPackageName)) {
            String message2 = "Duplicate child package:" + childPackageName;
            Slog.w(TAG, message2);
            outError[0] = message2;
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        } else {
            Package childPkg = new Package(childPackageName);
            childPkg.mVersionCode = parentPkg.mVersionCode;
            childPkg.baseRevisionCode = parentPkg.baseRevisionCode;
            childPkg.mVersionName = parentPkg.mVersionName;
            childPkg.applicationInfo.targetSdkVersion = parentPkg.applicationInfo.targetSdkVersion;
            childPkg.applicationInfo.minSdkVersion = parentPkg.applicationInfo.minSdkVersion;
            Package childPkg2 = parseBaseApkCommon(childPkg, CHILD_PACKAGE_TAGS, res, parser, flags, outError);
            if (childPkg2 == null) {
                return false;
            }
            if (parentPkg.childPackages == null) {
                parentPkg.childPackages = new ArrayList<>();
            }
            parentPkg.childPackages.add(childPkg2);
            childPkg2.parentPackage = parentPkg;
            return true;
        }
    }

    public protected Package parseBaseApk(String apkPath, Resources res, XmlResourceParser parser, int flags, String[] outError) throws XmlPullParserException, IOException {
        String[] overlayPaths;
        try {
            Pair<String, String> packageSplit = parsePackageSplitNames(parser, parser);
            String pkgName = packageSplit.first;
            String splitName = packageSplit.second;
            if (!TextUtils.isEmpty(splitName)) {
                outError[0] = "Expected base APK, but found split " + splitName;
                this.mParseError = PackageManager.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME;
                return null;
            }
            if (this.mCallback != null && (overlayPaths = this.mCallback.getOverlayPaths(pkgName, apkPath)) != null && overlayPaths.length > 0) {
                for (String overlayPath : overlayPaths) {
                    res.getAssets().addOverlayPath(overlayPath);
                }
            }
            Package pkg = new Package(pkgName);
            TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifest);
            pkg.mVersionCode = sa.getInteger(1, 0);
            pkg.mVersionCodeMajor = sa.getInteger(11, 0);
            pkg.applicationInfo.setVersionCode(pkg.getLongVersionCode());
            pkg.baseRevisionCode = sa.getInteger(5, 0);
            pkg.mVersionName = sa.getNonConfigurationString(2, 0);
            if (pkg.mVersionName != null) {
                pkg.mVersionName = pkg.mVersionName.intern();
            }
            pkg.coreApp = parser.getAttributeBooleanValue(null, "coreApp", false);
            pkg.mCompileSdkVersion = sa.getInteger(9, 0);
            pkg.applicationInfo.compileSdkVersion = pkg.mCompileSdkVersion;
            pkg.mCompileSdkVersionCodename = sa.getNonConfigurationString(10, 0);
            if (pkg.mCompileSdkVersionCodename != null) {
                pkg.mCompileSdkVersionCodename = pkg.mCompileSdkVersionCodename.intern();
            }
            pkg.applicationInfo.compileSdkVersionCodename = pkg.mCompileSdkVersionCodename;
            sa.recycle();
            return parseBaseApkCommon(pkg, null, res, parser, flags, outError);
        } catch (PackageParserException e) {
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME;
            return null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:281:0x0789, code lost:
        if (r18 != false) goto L367;
     */
    /* JADX WARN: Code restructure failed: missing block: B:283:0x0791, code lost:
        if (r48.instrumentation.size() != 0) goto L367;
     */
    /* JADX WARN: Code restructure failed: missing block: B:284:0x0793, code lost:
        r2 = 0;
        r53[0] = "<manifest> does not contain an <application> or <instrumentation>";
        r47.mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_EMPTY;
     */
    /* JADX WARN: Code restructure failed: missing block: B:285:0x079d, code lost:
        r2 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:286:0x079e, code lost:
        r0 = android.content.pm.PackageParser.NEW_PERMISSIONS.length;
        r3 = null;
        r1 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:287:0x07a4, code lost:
        if (r1 >= r0) goto L366;
     */
    /* JADX WARN: Code restructure failed: missing block: B:288:0x07a6, code lost:
        r4 = android.content.pm.PackageParser.NEW_PERMISSIONS[r1];
     */
    /* JADX WARN: Code restructure failed: missing block: B:289:0x07b0, code lost:
        if (r48.applicationInfo.targetSdkVersion < r4.sdkVersion) goto L303;
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x07bb, code lost:
        if (r48.requestedPermissions.contains(r4.name) != false) goto L311;
     */
    /* JADX WARN: Code restructure failed: missing block: B:293:0x07bd, code lost:
        if (r3 != null) goto L310;
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x07bf, code lost:
        r3 = new java.lang.StringBuilder(128);
        r3.append(r48.packageName);
        r3.append(": compat added ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:295:0x07d2, code lost:
        r3.append(' ');
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x07d7, code lost:
        r3.append(r4.name);
        r48.requestedPermissions.add(r4.name);
     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x07e3, code lost:
        r1 = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x07e6, code lost:
        if (r3 == null) goto L315;
     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x07e8, code lost:
        android.util.Slog.i(android.content.pm.PackageParser.TAG, r3.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x07f1, code lost:
        r1 = android.content.pm.PackageParser.SPLIT_PERMISSIONS.length;
        r4 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x07f5, code lost:
        if (r4 >= r1) goto L334;
     */
    /* JADX WARN: Code restructure failed: missing block: B:302:0x07f7, code lost:
        r5 = android.content.pm.PackageParser.SPLIT_PERMISSIONS[r4];
     */
    /* JADX WARN: Code restructure failed: missing block: B:303:0x0801, code lost:
        if (r48.applicationInfo.targetSdkVersion >= r5.targetSdk) goto L333;
     */
    /* JADX WARN: Code restructure failed: missing block: B:305:0x080b, code lost:
        if (r48.requestedPermissions.contains(r5.rootPerm) != false) goto L321;
     */
    /* JADX WARN: Code restructure failed: missing block: B:307:0x080e, code lost:
        r8 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x0812, code lost:
        if (r8 >= r5.newPerms.length) goto L330;
     */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x0814, code lost:
        r13 = r5.newPerms[r8];
     */
    /* JADX WARN: Code restructure failed: missing block: B:311:0x081e, code lost:
        if (r48.requestedPermissions.contains(r13) != false) goto L329;
     */
    /* JADX WARN: Code restructure failed: missing block: B:312:0x0820, code lost:
        r48.requestedPermissions.add(r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x0825, code lost:
        r8 = r8 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:314:0x0829, code lost:
        r4 = r4 + 1;
        r2 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:315:0x082d, code lost:
        if (r15 < 0) goto L365;
     */
    /* JADX WARN: Code restructure failed: missing block: B:316:0x082f, code lost:
        if (r15 <= 0) goto L339;
     */
    /* JADX WARN: Code restructure failed: missing block: B:318:0x0836, code lost:
        if (r48.applicationInfo.targetSdkVersion < 4) goto L339;
     */
    /* JADX WARN: Code restructure failed: missing block: B:319:0x0838, code lost:
        r48.applicationInfo.flags |= 512;
     */
    /* JADX WARN: Code restructure failed: missing block: B:320:0x0840, code lost:
        if (r14 == 0) goto L341;
     */
    /* JADX WARN: Code restructure failed: missing block: B:321:0x0842, code lost:
        r48.applicationInfo.flags |= 1024;
     */
    /* JADX WARN: Code restructure failed: missing block: B:322:0x084a, code lost:
        if (r19 < 0) goto L364;
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x084c, code lost:
        if (r19 <= 0) goto L345;
     */
    /* JADX WARN: Code restructure failed: missing block: B:325:0x0853, code lost:
        if (r48.applicationInfo.targetSdkVersion < 4) goto L345;
     */
    /* JADX WARN: Code restructure failed: missing block: B:326:0x0855, code lost:
        r48.applicationInfo.flags |= 2048;
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x085d, code lost:
        if (r16 < 0) goto L363;
     */
    /* JADX WARN: Code restructure failed: missing block: B:328:0x085f, code lost:
        if (r16 <= 0) goto L349;
     */
    /* JADX WARN: Code restructure failed: missing block: B:330:0x0867, code lost:
        if (r48.applicationInfo.targetSdkVersion < 9) goto L349;
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x0869, code lost:
        r48.applicationInfo.flags |= 524288;
     */
    /* JADX WARN: Code restructure failed: missing block: B:332:0x0872, code lost:
        if (r21 < 0) goto L362;
     */
    /* JADX WARN: Code restructure failed: missing block: B:333:0x0874, code lost:
        if (r21 <= 0) goto L353;
     */
    /* JADX WARN: Code restructure failed: missing block: B:335:0x087b, code lost:
        if (r48.applicationInfo.targetSdkVersion < 4) goto L353;
     */
    /* JADX WARN: Code restructure failed: missing block: B:336:0x087d, code lost:
        r48.applicationInfo.flags |= 4096;
     */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x0885, code lost:
        if (r42 < 0) goto L361;
     */
    /* JADX WARN: Code restructure failed: missing block: B:338:0x0887, code lost:
        if (r42 <= 0) goto L357;
     */
    /* JADX WARN: Code restructure failed: missing block: B:340:0x088e, code lost:
        if (r48.applicationInfo.targetSdkVersion < 4) goto L357;
     */
    /* JADX WARN: Code restructure failed: missing block: B:341:0x0890, code lost:
        r48.applicationInfo.flags |= 8192;
     */
    /* JADX WARN: Code restructure failed: missing block: B:343:0x089e, code lost:
        if (r48.applicationInfo.usesCompatibilityMode() == false) goto L360;
     */
    /* JADX WARN: Code restructure failed: missing block: B:344:0x08a0, code lost:
        adjustPackageToBeUnresizeableAndUnpipable(r48);
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x08a3, code lost:
        return r48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x024f, code lost:
        r53[0] = "<overlay> priority must be between 0 and 9999";
        r47.mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x0257, code lost:
        return null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized android.content.pm.PackageParser.Package parseBaseApkCommon(android.content.pm.PackageParser.Package r48, java.util.Set<java.lang.String> r49, android.content.res.Resources r50, android.content.res.XmlResourceParser r51, int r52, java.lang.String[] r53) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 2212
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.parseBaseApkCommon(android.content.pm.PackageParser$Package, java.util.Set, android.content.res.Resources, android.content.res.XmlResourceParser, int, java.lang.String[]):android.content.pm.PackageParser$Package");
    }

    private synchronized boolean checkOverlayRequiredSystemProperty(String propName, String propValue) {
        if (TextUtils.isEmpty(propName) || TextUtils.isEmpty(propValue)) {
            if (TextUtils.isEmpty(propName) && TextUtils.isEmpty(propValue)) {
                return true;
            }
            Slog.w(TAG, "Disabling overlay - incomplete property :'" + propName + "=" + propValue + "' - require both requiredSystemPropertyName AND requiredSystemPropertyValue to be specified.");
            return false;
        }
        String currValue = SystemProperties.get(propName);
        return currValue != null && currValue.equals(propValue);
    }

    private synchronized void adjustPackageToBeUnresizeableAndUnpipable(Package pkg) {
        Iterator<Activity> it = pkg.activities.iterator();
        while (it.hasNext()) {
            Activity a = it.next();
            a.info.resizeMode = 0;
            a.info.flags &= -4194305;
        }
    }

    public static synchronized int computeTargetSdkVersion(int targetVers, String targetCode, String[] platformSdkCodenames, String[] outError, boolean forceCurrentDev) {
        if (targetCode == null) {
            return targetVers;
        }
        if (ArrayUtils.contains(platformSdkCodenames, targetCode) || forceCurrentDev) {
            return 10000;
        }
        if (platformSdkCodenames.length > 0) {
            outError[0] = "Requires development platform " + targetCode + " (current platform is any of " + Arrays.toString(platformSdkCodenames) + ")";
            return -1;
        }
        outError[0] = "Requires development platform " + targetCode + " but this is a release platform.";
        return -1;
    }

    public static synchronized int computeMinSdkVersion(int minVers, String minCode, int platformSdkVersion, String[] platformSdkCodenames, String[] outError) {
        if (minCode == null) {
            if (minVers <= platformSdkVersion) {
                return minVers;
            }
            outError[0] = "Requires newer sdk version #" + minVers + " (current version is #" + platformSdkVersion + ")";
            return -1;
        } else if (ArrayUtils.contains(platformSdkCodenames, minCode)) {
            return 10000;
        } else {
            if (platformSdkCodenames.length > 0) {
                outError[0] = "Requires development platform " + minCode + " (current platform is any of " + Arrays.toString(platformSdkCodenames) + ")";
            } else {
                outError[0] = "Requires development platform " + minCode + " but this is a release platform.";
            }
            return -1;
        }
    }

    private synchronized FeatureInfo parseUsesFeature(Resources res, AttributeSet attrs) {
        FeatureInfo fi = new FeatureInfo();
        TypedArray sa = res.obtainAttributes(attrs, R.styleable.AndroidManifestUsesFeature);
        fi.name = sa.getNonResourceString(0);
        fi.version = sa.getInt(3, 0);
        if (fi.name == null) {
            fi.reqGlEsVersion = sa.getInt(1, 0);
        }
        if (sa.getBoolean(2, true)) {
            fi.flags |= 1;
        }
        sa.recycle();
        return fi;
    }

    private synchronized boolean parseUsesStaticLibrary(Package pkg, Resources res, XmlResourceParser parser, String[] outError) throws XmlPullParserException, IOException {
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestUsesStaticLibrary);
        String lname = sa.getNonResourceString(0);
        int version = sa.getInt(1, -1);
        String certSha256Digest = sa.getNonResourceString(2);
        sa.recycle();
        if (lname == null || version < 0 || certSha256Digest == null) {
            outError[0] = "Bad uses-static-library declaration name: " + lname + " version: " + version + " certDigest" + certSha256Digest;
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            XmlUtils.skipCurrentTag(parser);
            return false;
        } else if (pkg.usesStaticLibraries != null && pkg.usesStaticLibraries.contains(lname)) {
            outError[0] = "Depending on multiple versions of static library " + lname;
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            XmlUtils.skipCurrentTag(parser);
            return false;
        } else {
            String lname2 = lname.intern();
            String certSha256Digest2 = certSha256Digest.replace(SettingsStringUtil.DELIMITER, "").toLowerCase();
            String[] additionalCertSha256Digests = EmptyArray.STRING;
            if (pkg.applicationInfo.targetSdkVersion >= 27) {
                additionalCertSha256Digests = parseAdditionalCertificates(res, parser, outError);
                if (additionalCertSha256Digests == null) {
                    return false;
                }
            } else {
                XmlUtils.skipCurrentTag(parser);
            }
            String[] certSha256Digests = new String[additionalCertSha256Digests.length + 1];
            certSha256Digests[0] = certSha256Digest2;
            System.arraycopy(additionalCertSha256Digests, 0, certSha256Digests, 1, additionalCertSha256Digests.length);
            pkg.usesStaticLibraries = ArrayUtils.add(pkg.usesStaticLibraries, lname2);
            pkg.usesStaticLibrariesVersions = ArrayUtils.appendLong(pkg.usesStaticLibrariesVersions, version, true);
            pkg.usesStaticLibrariesCertDigests = (String[][]) ArrayUtils.appendElement(String[].class, pkg.usesStaticLibrariesCertDigests, certSha256Digests, true);
            return true;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x0076, code lost:
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized java.lang.String[] parseAdditionalCertificates(android.content.res.Resources r10, android.content.res.XmlResourceParser r11, java.lang.String[] r12) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            r9 = this;
            java.lang.String[] r0 = libcore.util.EmptyArray.STRING
            int r1 = r11.getDepth()
        L6:
            int r2 = r11.next()
            r3 = r2
            r4 = 1
            if (r2 == r4) goto L76
            r2 = 3
            if (r3 != r2) goto L17
            int r4 = r11.getDepth()
            if (r4 <= r1) goto L76
        L17:
            if (r3 == r2) goto L6
            r2 = 4
            if (r3 != r2) goto L1d
            goto L6
        L1d:
            java.lang.String r2 = r11.getName()
            java.lang.String r4 = "additional-certificate"
            boolean r4 = r2.equals(r4)
            if (r4 == 0) goto L72
            int[] r4 = com.android.internal.R.styleable.AndroidManifestAdditionalCertificate
            android.content.res.TypedArray r4 = r10.obtainAttributes(r11, r4)
            r5 = 0
            java.lang.String r6 = r4.getNonResourceString(r5)
            r4.recycle()
            boolean r7 = android.text.TextUtils.isEmpty(r6)
            if (r7 == 0) goto L5c
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "Bad additional-certificate declaration with empty certDigest:"
            r7.append(r8)
            r7.append(r6)
            java.lang.String r7 = r7.toString()
            r12[r5] = r7
            r5 = -108(0xffffffffffffff94, float:NaN)
            r9.mParseError = r5
            com.android.internal.util.XmlUtils.skipCurrentTag(r11)
            r4.recycle()
            r5 = 0
            return r5
        L5c:
            java.lang.String r5 = ":"
            java.lang.String r7 = ""
            java.lang.String r5 = r6.replace(r5, r7)
            java.lang.String r5 = r5.toLowerCase()
            java.lang.Class<java.lang.String> r6 = java.lang.String.class
            java.lang.Object[] r6 = com.android.internal.util.ArrayUtils.appendElement(r6, r0, r5)
            r0 = r6
            java.lang.String[] r0 = (java.lang.String[]) r0
            goto L75
        L72:
            com.android.internal.util.XmlUtils.skipCurrentTag(r11)
        L75:
            goto L6
        L76:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.parseAdditionalCertificates(android.content.res.Resources, android.content.res.XmlResourceParser, java.lang.String[]):java.lang.String[]");
    }

    private synchronized boolean parseUsesPermission(Package pkg, Resources res, XmlResourceParser parser) throws XmlPullParserException, IOException {
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestUsesPermission);
        String name = sa.getNonResourceString(0);
        int maxSdkVersion = 0;
        TypedValue val = sa.peekValue(1);
        if (val != null && val.type >= 16 && val.type <= 31) {
            maxSdkVersion = val.data;
        }
        String requiredFeature = sa.getNonConfigurationString(2, 0);
        String requiredNotfeature = sa.getNonConfigurationString(3, 0);
        sa.recycle();
        XmlUtils.skipCurrentTag(parser);
        if (name == null) {
            return true;
        }
        if (maxSdkVersion != 0 && maxSdkVersion < Build.VERSION.RESOURCES_SDK_INT) {
            return true;
        }
        if (requiredFeature != null && this.mCallback != null && !this.mCallback.hasFeature(requiredFeature)) {
            return true;
        }
        if (requiredNotfeature != null && this.mCallback != null && this.mCallback.hasFeature(requiredNotfeature)) {
            return true;
        }
        int index = pkg.requestedPermissions.indexOf(name);
        if (index == -1) {
            pkg.requestedPermissions.add(name.intern());
        } else {
            Slog.w(TAG, "Ignoring duplicate uses-permissions/uses-permissions-sdk-m: " + name + " in package: " + pkg.packageName + " at: " + parser.getPositionDescription());
        }
        return true;
    }

    private static synchronized String buildClassName(String pkg, CharSequence clsSeq, String[] outError) {
        if (clsSeq == null || clsSeq.length() <= 0) {
            outError[0] = "Empty class name in package " + pkg;
            return null;
        }
        String cls = clsSeq.toString();
        char c = cls.charAt(0);
        if (c == '.') {
            return pkg + cls;
        } else if (cls.indexOf(46) < 0) {
            return pkg + '.' + cls;
        } else {
            return cls;
        }
    }

    private static synchronized String buildCompoundName(String pkg, CharSequence procSeq, String type, String[] outError) {
        String proc = procSeq.toString();
        char c = proc.charAt(0);
        if (pkg != null && c == ':') {
            if (proc.length() < 2) {
                outError[0] = "Bad " + type + " name " + proc + " in package " + pkg + ": must be at least two characters";
                return null;
            }
            String subName = proc.substring(1);
            String nameError = validateName(subName, false, false);
            if (nameError != null) {
                outError[0] = "Invalid " + type + " name " + proc + " in package " + pkg + ": " + nameError;
                return null;
            }
            return pkg + proc;
        }
        String nameError2 = validateName(proc, true, false);
        if (nameError2 != null && !StorageManager.UUID_SYSTEM.equals(proc)) {
            outError[0] = "Invalid " + type + " name " + proc + " in package " + pkg + ": " + nameError2;
            return null;
        }
        return proc;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized String buildProcessName(String pkg, String defProc, CharSequence procSeq, int flags, String[] separateProcesses, String[] outError) {
        if ((flags & 2) != 0 && !StorageManager.UUID_SYSTEM.equals(procSeq)) {
            return defProc != null ? defProc : pkg;
        }
        if (separateProcesses != null) {
            for (int i = separateProcesses.length - 1; i >= 0; i--) {
                String sp = separateProcesses[i];
                if (sp.equals(pkg) || sp.equals(defProc) || sp.equals(procSeq)) {
                    return pkg;
                }
            }
        }
        if (procSeq == null || procSeq.length() <= 0) {
            return defProc;
        }
        return TextUtils.safeIntern(buildCompoundName(pkg, procSeq, DumpHeapActivity.KEY_PROCESS, outError));
    }

    private static synchronized String buildTaskAffinityName(String pkg, String defProc, CharSequence procSeq, String[] outError) {
        if (procSeq == null) {
            return defProc;
        }
        if (procSeq.length() <= 0) {
            return null;
        }
        return buildCompoundName(pkg, procSeq, "taskAffinity", outError);
    }

    /* JADX WARN: Code restructure failed: missing block: B:56:0x0208, code lost:
        r1 = r7.keySet();
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0214, code lost:
        if (r1.removeAll(r9.keySet()) == false) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0216, code lost:
        r26[0] = "Package" + r23.packageName + " AndroidManifext.xml 'key-set' and 'public-key' names must be distinct.";
        r22.mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x0237, code lost:
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0238, code lost:
        r23.mKeySetMapping = new android.util.ArrayMap<>();
        r4 = r9.entrySet().iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x024d, code lost:
        if (r4.hasNext() == false) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x024f, code lost:
        r11 = r4.next();
        r12 = r11.getKey();
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0265, code lost:
        if (r11.getValue().size() != 0) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0267, code lost:
        r14 = new java.lang.StringBuilder();
        r20 = r1;
        r14.append("Package");
        r14.append(r23.packageName);
        r14.append(" AndroidManifext.xml 'key-set' ");
        r14.append(r12);
        r14.append(" has no valid associated 'public-key'. Not including in package's defined key-sets.");
        android.util.Slog.w(android.content.pm.PackageParser.TAG, r14.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x028f, code lost:
        r1 = r20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0292, code lost:
        r20 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0298, code lost:
        if (r10.contains(r12) == false) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x029a, code lost:
        android.util.Slog.w(android.content.pm.PackageParser.TAG, "Package" + r23.packageName + " AndroidManifext.xml 'key-set' " + r12 + " contained improper 'public-key' tags. Not including in package's defined key-sets.");
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x02c0, code lost:
        r23.mKeySetMapping.put(r12, new android.util.ArraySet<>());
        r1 = r11.getValue().iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x02d8, code lost:
        if (r1.hasNext() == false) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x02da, code lost:
        r13 = r1.next();
        r23.mKeySetMapping.get(r12).add(r7.get(r13));
        r1 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0306, code lost:
        if (r23.mKeySetMapping.keySet().containsAll(r8) == false) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x0308, code lost:
        r23.mUpgradeKeySets = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x030b, code lost:
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x030c, code lost:
        r26[0] = "Package" + r23.packageName + " AndroidManifext.xml does not define all 'upgrade-key-set's .";
        r22.mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x032b, code lost:
        return false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized boolean parseKeySets(android.content.pm.PackageParser.Package r23, android.content.res.Resources r24, android.content.res.XmlResourceParser r25, java.lang.String[] r26) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 812
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.parseKeySets(android.content.pm.PackageParser$Package, android.content.res.Resources, android.content.res.XmlResourceParser, java.lang.String[]):boolean");
    }

    private synchronized boolean parsePermissionGroup(Package owner, int flags, Resources res, XmlResourceParser parser, String[] outError) throws XmlPullParserException, IOException {
        PermissionGroup perm = new PermissionGroup(owner);
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestPermissionGroup);
        if (!parsePackageItemInfo(owner, perm.info, outError, "<permission-group>", sa, true, 2, 0, 1, 8, 5, 7)) {
            sa.recycle();
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        perm.info.descriptionRes = sa.getResourceId(4, 0);
        perm.info.requestRes = sa.getResourceId(9, 0);
        perm.info.flags = sa.getInt(6, 0);
        perm.info.priority = sa.getInt(3, 0);
        sa.recycle();
        if (!parseAllMetaData(res, parser, "<permission-group>", perm, outError)) {
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        owner.permissionGroups.add(perm);
        return true;
    }

    private synchronized boolean parsePermission(Package owner, Resources res, XmlResourceParser parser, String[] outError) throws XmlPullParserException, IOException {
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestPermission);
        Permission perm = new Permission(owner);
        if (!parsePackageItemInfo(owner, perm.info, outError, "<permission>", sa, true, 2, 0, 1, 9, 6, 8)) {
            sa.recycle();
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        perm.info.group = sa.getNonResourceString(4);
        if (perm.info.group != null) {
            perm.info.group = perm.info.group.intern();
        }
        perm.info.descriptionRes = sa.getResourceId(5, 0);
        perm.info.requestRes = sa.getResourceId(10, 0);
        perm.info.protectionLevel = sa.getInt(3, 0);
        perm.info.flags = sa.getInt(7, 0);
        sa.recycle();
        if (perm.info.protectionLevel == -1) {
            outError[0] = "<permission> does not specify protectionLevel";
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        perm.info.protectionLevel = PermissionInfo.fixProtectionLevel(perm.info.protectionLevel);
        if (perm.info.getProtectionFlags() != 0 && (perm.info.protectionLevel & 4096) == 0 && (perm.info.protectionLevel & 8192) == 0 && (perm.info.protectionLevel & 15) != 2) {
            outError[0] = "<permission>  protectionLevel specifies a non-instant flag but is not based on signature type";
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        } else if (!parseAllMetaData(res, parser, "<permission>", perm, outError)) {
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        } else {
            owner.permissions.add(perm);
            return true;
        }
    }

    private synchronized boolean parsePermissionTree(Package owner, Resources res, XmlResourceParser parser, String[] outError) throws XmlPullParserException, IOException {
        Permission perm = new Permission(owner);
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestPermissionTree);
        if (!parsePackageItemInfo(owner, perm.info, outError, "<permission-tree>", sa, true, 2, 0, 1, 5, 3, 4)) {
            sa.recycle();
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        sa.recycle();
        int index = perm.info.name.indexOf(46);
        if (index > 0) {
            index = perm.info.name.indexOf(46, index + 1);
        }
        if (index < 0) {
            outError[0] = "<permission-tree> name has less than three segments: " + perm.info.name;
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        perm.info.descriptionRes = 0;
        perm.info.requestRes = 0;
        perm.info.protectionLevel = 0;
        perm.tree = true;
        if (!parseAllMetaData(res, parser, "<permission-tree>", perm, outError)) {
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        owner.permissions.add(perm);
        return true;
    }

    private synchronized Instrumentation parseInstrumentation(Package owner, Resources res, XmlResourceParser parser, String[] outError) throws XmlPullParserException, IOException {
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestInstrumentation);
        if (this.mParseInstrumentationArgs == null) {
            this.mParseInstrumentationArgs = new ParsePackageItemArgs(owner, outError, 2, 0, 1, 8, 6, 7);
            this.mParseInstrumentationArgs.tag = "<instrumentation>";
        }
        this.mParseInstrumentationArgs.sa = sa;
        Instrumentation a = new Instrumentation(this.mParseInstrumentationArgs, new InstrumentationInfo());
        if (outError[0] != null) {
            sa.recycle();
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return null;
        }
        String str = sa.getNonResourceString(3);
        a.info.targetPackage = str != null ? str.intern() : null;
        String str2 = sa.getNonResourceString(9);
        a.info.targetProcesses = str2 != null ? str2.intern() : null;
        a.info.handleProfiling = sa.getBoolean(4, false);
        a.info.functionalTest = sa.getBoolean(5, false);
        sa.recycle();
        if (a.info.targetPackage == null) {
            outError[0] = "<instrumentation> does not specify targetPackage";
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return null;
        } else if (!parseAllMetaData(res, parser, "<instrumentation>", a, outError)) {
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return null;
        } else {
            owner.instrumentation.add(a);
            return a;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:256:0x0599, code lost:
        r9[0] = "Bad static-library declaration name: " + r10 + " version: " + r13;
        r0.mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
        com.android.internal.util.XmlUtils.skipCurrentTag(r37);
     */
    /* JADX WARN: Code restructure failed: missing block: B:257:0x05bc, code lost:
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:290:0x06b1, code lost:
        if (r21 == 0) goto L160;
     */
    /* JADX WARN: Code restructure failed: missing block: B:291:0x06b3, code lost:
        java.util.Collections.sort(r14.activities, android.content.pm.$$Lambda$PackageParser$0aobsT7Zf7WVZCqMZ5z2clAuQf4.INSTANCE);
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x06ba, code lost:
        if (r22 == false) goto L162;
     */
    /* JADX WARN: Code restructure failed: missing block: B:293:0x06bc, code lost:
        java.util.Collections.sort(r14.receivers, android.content.pm.$$Lambda$PackageParser$0DZRgzfgaIMpCOhJqjw6PUiU5vw.INSTANCE);
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x06c3, code lost:
        if (r26 == false) goto L164;
     */
    /* JADX WARN: Code restructure failed: missing block: B:295:0x06c5, code lost:
        java.util.Collections.sort(r14.services, android.content.pm.$$Lambda$PackageParser$M9fHqS_eEp1oYkuKJhRHOGUxf8.INSTANCE);
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x06cc, code lost:
        android.content.pm.PackageParser.DEFAULT_PRE_O_MAX_ASPECT_RATIO = r7.getFloat(com.android.internal.R.dimen.config_defaultPreOMaxAspectRatio);
        setMaxAspectRatio(r35);
        android.content.pm.PackageBackwardCompatibility.modifySharedLibraries(r35);
     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x06df, code lost:
        if (hasDomainURLs(r35) == false) goto L168;
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x06e1, code lost:
        r14.applicationInfo.privateFlags |= 16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x06ea, code lost:
        r14.applicationInfo.privateFlags &= -17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x06f2, code lost:
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:?, code lost:
        return true;
     */
    /* JADX WARN: Type inference failed for: r1v34 */
    /* JADX WARN: Type inference failed for: r1v35, types: [boolean] */
    /* JADX WARN: Type inference failed for: r1v69 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public protected boolean parseBaseApplication(android.content.pm.PackageParser.Package r35, android.content.res.Resources r36, android.content.res.XmlResourceParser r37, int r38, java.lang.String[] r39) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 1780
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.parseBaseApplication(android.content.pm.PackageParser$Package, android.content.res.Resources, android.content.res.XmlResourceParser, int, java.lang.String[]):boolean");
    }

    private static synchronized boolean hasDomainURLs(Package pkg) {
        if (pkg == null || pkg.activities == null) {
            return false;
        }
        ArrayList<Activity> activities = pkg.activities;
        int countActivities = activities.size();
        for (int n = 0; n < countActivities; n++) {
            Activity activity = activities.get(n);
            ArrayList<II> arrayList = activity.intents;
            if (arrayList != 0) {
                int countFilters = arrayList.size();
                for (int m = 0; m < countFilters; m++) {
                    ActivityIntentInfo aii = (ActivityIntentInfo) arrayList.get(m);
                    if (aii.hasAction("android.intent.action.VIEW") && aii.hasAction("android.intent.action.VIEW") && (aii.hasDataScheme(IntentFilter.SCHEME_HTTP) || aii.hasDataScheme(IntentFilter.SCHEME_HTTPS))) {
                        return true;
                    }
                }
                continue;
            }
        }
        return false;
    }

    private synchronized boolean parseSplitApplication(Package owner, Resources res, XmlResourceParser parser, int flags, int splitIndex, String[] outError) throws XmlPullParserException, IOException {
        int innerDepth;
        boolean z;
        int i;
        String classLoaderName;
        String[] strArr;
        XmlResourceParser xmlResourceParser;
        Resources resources;
        Package r4;
        PackageParser packageParser;
        ComponentInfo parsedComponent;
        PackageParser packageParser2 = this;
        Package r14 = owner;
        Resources resources2 = res;
        XmlResourceParser xmlResourceParser2 = parser;
        String[] strArr2 = outError;
        TypedArray sa = resources2.obtainAttributes(xmlResourceParser2, R.styleable.AndroidManifestApplication);
        int i2 = 1;
        int i3 = 4;
        if (sa.getBoolean(7, true)) {
            int[] iArr = r14.splitFlags;
            iArr[splitIndex] = iArr[splitIndex] | 4;
        }
        String classLoaderName2 = sa.getString(46);
        int i4 = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
        boolean z2 = false;
        if (classLoaderName2 != null && !ClassLoaderFactory.isValidClassLoaderName(classLoaderName2)) {
            strArr2[0] = "Invalid class loader name: " + classLoaderName2;
            packageParser2.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        r14.applicationInfo.splitClassLoaderNames[splitIndex] = classLoaderName2;
        int innerDepth2 = parser.getDepth();
        while (true) {
            int innerDepth3 = innerDepth2;
            int type = parser.next();
            if (type == i2) {
                return true;
            }
            if (type == 3 && parser.getDepth() <= innerDepth3) {
                return true;
            }
            if (type == 3) {
                innerDepth = innerDepth3;
                z = z2;
                i = i4;
                classLoaderName = classLoaderName2;
                strArr = strArr2;
                xmlResourceParser = xmlResourceParser2;
                resources = resources2;
                r4 = r14;
                packageParser = packageParser2;
            } else if (type == i3) {
                innerDepth = innerDepth3;
                z = z2;
                i = i4;
                classLoaderName = classLoaderName2;
                strArr = strArr2;
                xmlResourceParser = xmlResourceParser2;
                resources = resources2;
                r4 = r14;
                packageParser = packageParser2;
            } else {
                CachedComponentArgs cachedArgs = new CachedComponentArgs();
                String tagName = parser.getName();
                if (tagName.equals(Context.ACTIVITY_SERVICE)) {
                    innerDepth = innerDepth3;
                    int i5 = i4;
                    classLoaderName = classLoaderName2;
                    Activity a = packageParser2.parseActivity(r14, resources2, xmlResourceParser2, flags, strArr2, cachedArgs, false, r14.baseHardwareAccelerated);
                    if (a == null) {
                        packageParser2.mParseError = i5;
                        return false;
                    }
                    z = false;
                    r14.activities.add(a);
                    ComponentInfo parsedComponent2 = a.info;
                    parsedComponent = parsedComponent2;
                    i = i5;
                    strArr = strArr2;
                    xmlResourceParser = xmlResourceParser2;
                    resources = resources2;
                    r4 = r14;
                    packageParser = packageParser2;
                } else {
                    innerDepth = innerDepth3;
                    z = z2;
                    int i6 = i4;
                    classLoaderName = classLoaderName2;
                    if (tagName.equals("receiver")) {
                        i = i6;
                        r4 = r14;
                        packageParser = packageParser2;
                        Activity a2 = packageParser2.parseActivity(r14, resources2, xmlResourceParser2, flags, outError, cachedArgs, true, false);
                        if (a2 == null) {
                            packageParser.mParseError = i;
                            return z;
                        }
                        r4.receivers.add(a2);
                        parsedComponent = a2.info;
                    } else {
                        i = i6;
                        r4 = r14;
                        packageParser = packageParser2;
                        if (tagName.equals("service")) {
                            Service s = packageParser.parseService(r4, res, parser, flags, outError, cachedArgs);
                            if (s == null) {
                                packageParser.mParseError = i;
                                return z;
                            }
                            r4.services.add(s);
                            parsedComponent = s.info;
                        } else if (tagName.equals("provider")) {
                            Provider p = packageParser.parseProvider(r4, res, parser, flags, outError, cachedArgs);
                            if (p == null) {
                                packageParser.mParseError = i;
                                return z;
                            }
                            r4.providers.add(p);
                            parsedComponent = p.info;
                        } else if (tagName.equals("activity-alias")) {
                            Activity a3 = packageParser.parseActivityAlias(r4, res, parser, flags, outError, cachedArgs);
                            if (a3 == null) {
                                packageParser.mParseError = i;
                                return z;
                            }
                            r4.activities.add(a3);
                            parsedComponent = a3.info;
                        } else {
                            if (parser.getName().equals("meta-data")) {
                                resources = res;
                                xmlResourceParser = parser;
                                strArr = outError;
                                Bundle parseMetaData = packageParser.parseMetaData(resources, xmlResourceParser, r4.mAppMetaData, strArr);
                                r4.mAppMetaData = parseMetaData;
                                if (parseMetaData == null) {
                                    packageParser.mParseError = i;
                                    return z;
                                }
                            } else {
                                resources = res;
                                xmlResourceParser = parser;
                                strArr = outError;
                                if (tagName.equals("uses-static-library")) {
                                    if (!packageParser.parseUsesStaticLibrary(r4, resources, xmlResourceParser, strArr)) {
                                        return z;
                                    }
                                } else if (tagName.equals("uses-library")) {
                                    TypedArray sa2 = resources.obtainAttributes(xmlResourceParser, R.styleable.AndroidManifestUsesLibrary);
                                    String lname = sa2.getNonResourceString(z ? 1 : 0);
                                    boolean req = sa2.getBoolean(1, true);
                                    sa2.recycle();
                                    if (lname != null) {
                                        String lname2 = lname.intern();
                                        if (req) {
                                            r4.usesLibraries = ArrayUtils.add(r4.usesLibraries, lname2);
                                            r4.usesOptionalLibraries = ArrayUtils.remove(r4.usesOptionalLibraries, lname2);
                                        } else if (!ArrayUtils.contains(r4.usesLibraries, lname2)) {
                                            r4.usesOptionalLibraries = ArrayUtils.add(r4.usesOptionalLibraries, lname2);
                                        }
                                    }
                                    XmlUtils.skipCurrentTag(parser);
                                } else if (tagName.equals("uses-package")) {
                                    XmlUtils.skipCurrentTag(parser);
                                } else {
                                    Slog.w(TAG, "Unknown element under <application>: " + tagName + " at " + packageParser.mArchiveSourcePath + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + parser.getPositionDescription());
                                    XmlUtils.skipCurrentTag(parser);
                                }
                            }
                            parsedComponent = null;
                        }
                    }
                    resources = res;
                    xmlResourceParser = parser;
                    strArr = outError;
                }
                if (parsedComponent != null && parsedComponent.splitName == null) {
                    parsedComponent.splitName = r4.splitNames[splitIndex];
                }
            }
            packageParser2 = packageParser;
            r14 = r4;
            resources2 = resources;
            xmlResourceParser2 = xmlResourceParser;
            strArr2 = strArr;
            classLoaderName2 = classLoaderName;
            i3 = 4;
            i2 = 1;
            i4 = i;
            z2 = z;
            innerDepth2 = innerDepth;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized boolean parsePackageItemInfo(Package owner, PackageItemInfo outInfo, String[] outError, String tag, TypedArray sa, boolean nameRequired, int nameRes, int labelRes, int iconRes, int roundIconRes, int logoRes, int bannerRes) {
        if (sa == null) {
            outError[0] = tag + " does not contain any attributes";
            return false;
        }
        String name = sa.getNonConfigurationString(nameRes, 0);
        if (name == null) {
            if (nameRequired) {
                outError[0] = tag + " does not specify android:name";
                return false;
            }
        } else {
            outInfo.name = buildClassName(owner.applicationInfo.packageName, name, outError);
            if (outInfo.name == null) {
                return false;
            }
        }
        boolean useRoundIcon = Resources.getSystem().getBoolean(R.bool.config_useRoundIcon);
        int roundIconVal = useRoundIcon ? sa.getResourceId(roundIconRes, 0) : 0;
        if (roundIconVal != 0) {
            outInfo.icon = roundIconVal;
            outInfo.nonLocalizedLabel = null;
        } else {
            int iconVal = sa.getResourceId(iconRes, 0);
            if (iconVal != 0) {
                outInfo.icon = iconVal;
                outInfo.nonLocalizedLabel = null;
            }
        }
        int logoVal = sa.getResourceId(logoRes, 0);
        if (logoVal != 0) {
            outInfo.logo = logoVal;
        }
        int bannerVal = sa.getResourceId(bannerRes, 0);
        if (bannerVal != 0) {
            outInfo.banner = bannerVal;
        }
        TypedValue v = sa.peekValue(labelRes);
        if (v != null) {
            int i = v.resourceId;
            outInfo.labelRes = i;
            if (i == 0) {
                outInfo.nonLocalizedLabel = v.coerceToString();
            }
        }
        outInfo.packageName = owner.packageName;
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:216:0x0626, code lost:
        if (r12 != false) goto L128;
     */
    /* JADX WARN: Code restructure failed: missing block: B:217:0x0628, code lost:
        r0 = r0.info;
     */
    /* JADX WARN: Code restructure failed: missing block: B:218:0x0630, code lost:
        if (r0.intents.size() <= 0) goto L127;
     */
    /* JADX WARN: Code restructure failed: missing block: B:219:0x0632, code lost:
        r3 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:220:0x0634, code lost:
        r3 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:221:0x0635, code lost:
        r0.exported = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:222:0x0637, code lost:
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized android.content.pm.PackageParser.Activity parseActivity(android.content.pm.PackageParser.Package r26, android.content.res.Resources r27, android.content.res.XmlResourceParser r28, int r29, java.lang.String[] r30, android.content.pm.PackageParser.CachedComponentArgs r31, boolean r32, boolean r33) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 1592
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.parseActivity(android.content.pm.PackageParser$Package, android.content.res.Resources, android.content.res.XmlResourceParser, int, java.lang.String[], android.content.pm.PackageParser$CachedComponentArgs, boolean, boolean):android.content.pm.PackageParser$Activity");
    }

    private synchronized void setActivityResizeMode(ActivityInfo aInfo, TypedArray sa, Package owner) {
        boolean appExplicitDefault = (owner.applicationInfo.privateFlags & 3072) != 0;
        if (sa.hasValue(40) || appExplicitDefault) {
            boolean appResizeable = (owner.applicationInfo.privateFlags & 1024) != 0;
            if (sa.getBoolean(40, appResizeable)) {
                aInfo.resizeMode = 2;
            } else {
                aInfo.resizeMode = 0;
            }
        } else if ((owner.applicationInfo.privateFlags & 4096) != 0) {
            aInfo.resizeMode = 1;
        } else if (aInfo.isFixedOrientationPortrait()) {
            aInfo.resizeMode = 6;
        } else if (aInfo.isFixedOrientationLandscape()) {
            aInfo.resizeMode = 5;
        } else if (aInfo.isFixedOrientation()) {
            aInfo.resizeMode = 7;
        } else {
            aInfo.resizeMode = 4;
        }
    }

    private synchronized void setMaxAspectRatio(Package owner) {
        float activityAspectRatio;
        float maxAspectRatio = owner.applicationInfo.targetSdkVersion < 26 ? DEFAULT_PRE_O_MAX_ASPECT_RATIO : 0.0f;
        if (owner.applicationInfo.maxAspectRatio != 0.0f) {
            maxAspectRatio = owner.applicationInfo.maxAspectRatio;
        } else if (owner.mAppMetaData != null && owner.mAppMetaData.containsKey(METADATA_MAX_ASPECT_RATIO)) {
            maxAspectRatio = owner.mAppMetaData.getFloat(METADATA_MAX_ASPECT_RATIO, maxAspectRatio);
        }
        Iterator<Activity> it = owner.activities.iterator();
        while (it.hasNext()) {
            Activity activity = it.next();
            if (!activity.hasMaxAspectRatio()) {
                if (activity.metaData != null) {
                    activityAspectRatio = activity.metaData.getFloat(METADATA_MAX_ASPECT_RATIO, maxAspectRatio);
                } else {
                    activityAspectRatio = maxAspectRatio;
                }
                activity.setMaxAspectRatio(activityAspectRatio);
            }
        }
    }

    public static synchronized int getActivityConfigChanges(int configChanges, int recreateOnConfigChanges) {
        return ((~recreateOnConfigChanges) & 3) | configChanges;
    }

    private synchronized void parseLayout(Resources res, AttributeSet attrs, Activity a) {
        TypedArray sw = res.obtainAttributes(attrs, R.styleable.AndroidManifestLayout);
        int width = -1;
        float widthFraction = -1.0f;
        int height = -1;
        float heightFraction = -1.0f;
        int widthType = sw.getType(3);
        if (widthType == 6) {
            widthFraction = sw.getFraction(3, 1, 1, -1.0f);
        } else if (widthType == 5) {
            width = sw.getDimensionPixelSize(3, -1);
        }
        int heightType = sw.getType(4);
        if (heightType == 6) {
            heightFraction = sw.getFraction(4, 1, 1, -1.0f);
        } else if (heightType == 5) {
            height = sw.getDimensionPixelSize(4, -1);
        }
        int gravity = sw.getInt(0, 17);
        int minWidth = sw.getDimensionPixelSize(1, -1);
        int minHeight = sw.getDimensionPixelSize(2, -1);
        sw.recycle();
        a.info.windowLayout = new ActivityInfo.WindowLayout(width, widthFraction, height, heightFraction, gravity, minWidth, minHeight);
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x036e, code lost:
        if (r15 != false) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:101:0x0370, code lost:
        r0 = r0.info;
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x0378, code lost:
        if (r0.intents.size() <= 0) goto L65;
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x037a, code lost:
        r4 = r19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x037d, code lost:
        r4 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x037f, code lost:
        r0.exported = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x0381, code lost:
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized android.content.pm.PackageParser.Activity parseActivityAlias(android.content.pm.PackageParser.Package r33, android.content.res.Resources r34, android.content.res.XmlResourceParser r35, int r36, java.lang.String[] r37, android.content.pm.PackageParser.CachedComponentArgs r38) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 898
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.parseActivityAlias(android.content.pm.PackageParser$Package, android.content.res.Resources, android.content.res.XmlResourceParser, int, java.lang.String[], android.content.pm.PackageParser$CachedComponentArgs):android.content.pm.PackageParser$Activity");
    }

    private synchronized Provider parseProvider(Package owner, Resources res, XmlResourceParser parser, int flags, String[] outError, CachedComponentArgs cachedArgs) throws XmlPullParserException, IOException {
        TypedArray sa;
        TypedArray sa2 = res.obtainAttributes(parser, R.styleable.AndroidManifestProvider);
        if (cachedArgs.mProviderArgs == null) {
            sa = sa2;
            cachedArgs.mProviderArgs = new ParseComponentArgs(owner, outError, 2, 0, 1, 19, 15, 17, this.mSeparateProcesses, 8, 14, 6);
            cachedArgs.mProviderArgs.tag = "<provider>";
        } else {
            sa = sa2;
        }
        TypedArray sa3 = sa;
        cachedArgs.mProviderArgs.sa = sa3;
        cachedArgs.mProviderArgs.flags = flags;
        Provider p = new Provider(cachedArgs.mProviderArgs, new ProviderInfo());
        if (outError[0] != null) {
            sa3.recycle();
            return null;
        }
        boolean providerExportedDefault = false;
        if (owner.applicationInfo.targetSdkVersion < 17) {
            providerExportedDefault = true;
        }
        p.info.exported = sa3.getBoolean(7, providerExportedDefault);
        String cpname = sa3.getNonConfigurationString(10, 0);
        p.info.isSyncable = sa3.getBoolean(11, false);
        String permission = sa3.getNonConfigurationString(3, 0);
        String str = sa3.getNonConfigurationString(4, 0);
        if (str == null) {
            str = permission;
        }
        if (str == null) {
            p.info.readPermission = owner.applicationInfo.permission;
        } else {
            p.info.readPermission = str.length() > 0 ? str.toString().intern() : null;
        }
        String str2 = sa3.getNonConfigurationString(5, 0);
        if (str2 == null) {
            str2 = permission;
        }
        String str3 = str2;
        if (str3 == null) {
            p.info.writePermission = owner.applicationInfo.permission;
        } else {
            p.info.writePermission = str3.length() > 0 ? str3.toString().intern() : null;
        }
        p.info.grantUriPermissions = sa3.getBoolean(13, false);
        p.info.multiprocess = sa3.getBoolean(9, false);
        p.info.initOrder = sa3.getInt(12, 0);
        p.info.splitName = sa3.getNonConfigurationString(21, 0);
        p.info.flags = 0;
        if (sa3.getBoolean(16, false)) {
            p.info.flags |= 1073741824;
        }
        ProviderInfo providerInfo = p.info;
        ProviderInfo providerInfo2 = p.info;
        boolean z = sa3.getBoolean(18, false);
        providerInfo2.directBootAware = z;
        providerInfo.encryptionAware = z;
        if (p.info.directBootAware) {
            owner.applicationInfo.privateFlags |= 256;
        }
        boolean visibleToEphemeral = sa3.getBoolean(20, false);
        if (visibleToEphemeral) {
            p.info.flags |= 1048576;
            owner.visibleToInstantApps = true;
        }
        sa3.recycle();
        if ((owner.applicationInfo.privateFlags & 2) != 0 && p.info.processName == owner.packageName) {
            outError[0] = "Heavy-weight applications can not have providers in main process";
            return null;
        } else if (cpname == null) {
            outError[0] = "<provider> does not include authorities attribute";
            return null;
        } else if (cpname.length() <= 0) {
            outError[0] = "<provider> has empty authorities attribute";
            return null;
        } else {
            p.info.authority = cpname.intern();
            if (parseProviderTags(res, parser, visibleToEphemeral, p, outError)) {
                return p;
            }
            return null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:84:0x0280, code lost:
        return true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized boolean parseProviderTags(android.content.res.Resources r20, android.content.res.XmlResourceParser r21, boolean r22, android.content.pm.PackageParser.Provider r23, java.lang.String[] r24) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 641
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.parseProviderTags(android.content.res.Resources, android.content.res.XmlResourceParser, boolean, android.content.pm.PackageParser$Provider, java.lang.String[]):boolean");
    }

    private synchronized Service parseService(Package owner, Resources res, XmlResourceParser parser, int flags, String[] outError, CachedComponentArgs cachedArgs) throws XmlPullParserException, IOException {
        boolean z;
        int outerDepth;
        TypedArray sa;
        XmlResourceParser xmlResourceParser;
        Resources resources;
        boolean z2;
        String[] strArr;
        char c;
        Resources resources2 = res;
        XmlResourceParser xmlResourceParser2 = parser;
        String[] strArr2 = outError;
        TypedArray sa2 = resources2.obtainAttributes(xmlResourceParser2, R.styleable.AndroidManifestService);
        if (cachedArgs.mServiceArgs == null) {
            cachedArgs.mServiceArgs = new ParseComponentArgs(owner, strArr2, 2, 0, 1, 15, 8, 12, this.mSeparateProcesses, 6, 7, 4);
            cachedArgs.mServiceArgs.tag = "<service>";
        }
        cachedArgs.mServiceArgs.sa = sa2;
        cachedArgs.mServiceArgs.flags = flags;
        Service s = new Service(cachedArgs.mServiceArgs, new ServiceInfo());
        if (strArr2[0] != null) {
            sa2.recycle();
            return null;
        }
        boolean setExported = sa2.hasValue(5);
        if (setExported) {
            s.info.exported = sa2.getBoolean(5, false);
        }
        String str = sa2.getNonConfigurationString(3, 0);
        if (str == null) {
            s.info.permission = owner.applicationInfo.permission;
        } else {
            s.info.permission = str.length() > 0 ? str.toString().intern() : null;
        }
        s.info.splitName = sa2.getNonConfigurationString(17, 0);
        s.info.flags = 0;
        boolean z3 = true;
        if (sa2.getBoolean(9, false)) {
            s.info.flags |= 1;
        }
        if (sa2.getBoolean(10, false)) {
            s.info.flags |= 2;
        }
        if (sa2.getBoolean(14, false)) {
            s.info.flags |= 4;
        }
        if (sa2.getBoolean(11, false)) {
            s.info.flags |= 1073741824;
        }
        ServiceInfo serviceInfo = s.info;
        ServiceInfo serviceInfo2 = s.info;
        boolean z4 = sa2.getBoolean(13, false);
        serviceInfo2.directBootAware = z4;
        serviceInfo.encryptionAware = z4;
        if (s.info.directBootAware) {
            owner.applicationInfo.privateFlags |= 256;
        }
        boolean visibleToEphemeral = sa2.getBoolean(16, false);
        if (visibleToEphemeral) {
            s.info.flags |= 1048576;
            owner.visibleToInstantApps = true;
        }
        sa2.recycle();
        if ((owner.applicationInfo.privateFlags & 2) != 0 && s.info.processName == owner.packageName) {
            strArr2[0] = "Heavy-weight applications can not have services in main process";
            return null;
        }
        int outerDepth2 = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == z3) {
                z = z3;
                break;
            } else if (type == 3 && parser.getDepth() <= outerDepth2) {
                z = true;
                break;
            } else {
                if (type == 3) {
                    outerDepth = outerDepth2;
                    sa = sa2;
                    xmlResourceParser = xmlResourceParser2;
                    resources = resources2;
                    z2 = true;
                    strArr = strArr2;
                    c = 0;
                } else if (type == 4) {
                    outerDepth = outerDepth2;
                    sa = sa2;
                    xmlResourceParser = xmlResourceParser2;
                    resources = resources2;
                    z2 = true;
                    strArr = strArr2;
                    c = 0;
                } else if (parser.getName().equals("intent-filter")) {
                    ServiceIntentInfo intent = new ServiceIntentInfo(s);
                    outerDepth = outerDepth2;
                    sa = sa2;
                    xmlResourceParser = xmlResourceParser2;
                    if (!parseIntent(resources2, xmlResourceParser2, true, false, intent, outError)) {
                        return null;
                    }
                    if (visibleToEphemeral) {
                        z2 = true;
                        intent.setVisibilityToInstantApp(1);
                        c = 0;
                        s.info.flags |= 1048576;
                    } else {
                        z2 = true;
                        c = 0;
                    }
                    s.order = Math.max(intent.getOrder(), s.order);
                    s.intents.add(intent);
                    strArr = outError;
                    resources = res;
                } else {
                    outerDepth = outerDepth2;
                    sa = sa2;
                    xmlResourceParser = xmlResourceParser2;
                    z2 = true;
                    c = 0;
                    if (parser.getName().equals("meta-data")) {
                        strArr = outError;
                        resources = res;
                        Bundle parseMetaData = parseMetaData(resources, xmlResourceParser, s.metaData, strArr);
                        s.metaData = parseMetaData;
                        if (parseMetaData == null) {
                            return null;
                        }
                    } else {
                        strArr = outError;
                        resources = res;
                        Slog.w(TAG, "Unknown element under <service>: " + parser.getName() + " at " + this.mArchiveSourcePath + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + parser.getPositionDescription());
                        XmlUtils.skipCurrentTag(parser);
                    }
                }
                resources2 = resources;
                strArr2 = strArr;
                xmlResourceParser2 = xmlResourceParser;
                sa2 = sa;
                z3 = z2;
                outerDepth2 = outerDepth;
            }
        }
        if (!setExported) {
            ServiceInfo serviceInfo3 = s.info;
            if (s.intents.size() <= 0) {
                z = false;
            }
            serviceInfo3.exported = z;
        }
        return s;
    }

    private synchronized boolean isImplicitlyExposedIntent(IntentInfo intent) {
        return intent.hasCategory(Intent.CATEGORY_BROWSABLE) || intent.hasAction(Intent.ACTION_SEND) || intent.hasAction(Intent.ACTION_SENDTO) || intent.hasAction(Intent.ACTION_SEND_MULTIPLE);
    }

    private synchronized boolean parseAllMetaData(Resources res, XmlResourceParser parser, String tag, Component<?> outInfo, String[] outError) throws XmlPullParserException, IOException {
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                break;
            } else if (type != 3 && type != 4) {
                if (parser.getName().equals("meta-data")) {
                    Bundle parseMetaData = parseMetaData(res, parser, ((Component) outInfo).metaData, outError);
                    ((Component) outInfo).metaData = parseMetaData;
                    if (parseMetaData == null) {
                        return false;
                    }
                } else {
                    Slog.w(TAG, "Unknown element under " + tag + ": " + parser.getName() + " at " + this.mArchiveSourcePath + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + parser.getPositionDescription());
                    XmlUtils.skipCurrentTag(parser);
                }
            }
        }
        return true;
    }

    private synchronized Bundle parseMetaData(Resources res, XmlResourceParser parser, Bundle data, String[] outError) throws XmlPullParserException, IOException {
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestMetaData);
        if (data == null) {
            data = new Bundle();
        }
        boolean z = false;
        String name = sa.getNonConfigurationString(0, 0);
        if (name == null) {
            outError[0] = "<meta-data> requires an android:name attribute";
            sa.recycle();
            return null;
        }
        String name2 = name.intern();
        TypedValue v = sa.peekValue(2);
        if (v != null && v.resourceId != 0) {
            data.putInt(name2, v.resourceId);
        } else {
            TypedValue v2 = sa.peekValue(1);
            if (v2 == null) {
                outError[0] = "<meta-data> requires an android:value or android:resource attribute";
                data = null;
            } else if (v2.type == 3) {
                CharSequence cs = v2.coerceToString();
                data.putString(name2, cs != null ? cs.toString() : null);
            } else if (v2.type == 18) {
                if (v2.data != 0) {
                    z = true;
                }
                data.putBoolean(name2, z);
            } else if (v2.type >= 16 && v2.type <= 31) {
                data.putInt(name2, v2.data);
            } else if (v2.type == 4) {
                data.putFloat(name2, v2.getFloat());
            } else {
                Slog.w(TAG, "<meta-data> only supports string, integer, float, color, boolean, and resource reference types: " + parser.getName() + " at " + this.mArchiveSourcePath + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + parser.getPositionDescription());
            }
        }
        sa.recycle();
        XmlUtils.skipCurrentTag(parser);
        return data;
    }

    private static synchronized VerifierInfo parseVerifier(AttributeSet attrs) {
        String packageName = null;
        String encodedPublicKey = null;
        int attrCount = attrs.getAttributeCount();
        for (int i = 0; i < attrCount; i++) {
            int attrResId = attrs.getAttributeNameResource(i);
            if (attrResId == 16842755) {
                packageName = attrs.getAttributeValue(i);
            } else if (attrResId == 16843686) {
                encodedPublicKey = attrs.getAttributeValue(i);
            }
        }
        if (packageName == null || packageName.length() == 0) {
            Slog.i(TAG, "verifier package name was null; skipping");
            return null;
        }
        PublicKey publicKey = parsePublicKey(encodedPublicKey);
        if (publicKey == null) {
            Slog.i(TAG, "Unable to parse verifier public key for " + packageName);
            return null;
        }
        return new VerifierInfo(packageName, publicKey);
    }

    public static final synchronized PublicKey parsePublicKey(String encodedPublicKey) {
        if (encodedPublicKey == null) {
            Slog.w(TAG, "Could not parse null public key");
            return null;
        }
        try {
            byte[] encoded = Base64.decode(encodedPublicKey, 0);
            EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            try {
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                return keyFactory.generatePublic(keySpec);
            } catch (NoSuchAlgorithmException e) {
                Slog.wtf(TAG, "Could not parse public key: RSA KeyFactory not included in build");
                try {
                    KeyFactory keyFactory2 = KeyFactory.getInstance("EC");
                    return keyFactory2.generatePublic(keySpec);
                } catch (NoSuchAlgorithmException e2) {
                    Slog.wtf(TAG, "Could not parse public key: EC KeyFactory not included in build");
                    try {
                        KeyFactory keyFactory3 = KeyFactory.getInstance("DSA");
                        return keyFactory3.generatePublic(keySpec);
                    } catch (NoSuchAlgorithmException e3) {
                        Slog.wtf(TAG, "Could not parse public key: DSA KeyFactory not included in build");
                        return null;
                    } catch (InvalidKeySpecException e4) {
                        return null;
                    }
                } catch (InvalidKeySpecException e5) {
                    KeyFactory keyFactory32 = KeyFactory.getInstance("DSA");
                    return keyFactory32.generatePublic(keySpec);
                }
            } catch (InvalidKeySpecException e6) {
                KeyFactory keyFactory22 = KeyFactory.getInstance("EC");
                return keyFactory22.generatePublic(keySpec);
            }
        } catch (IllegalArgumentException e7) {
            Slog.w(TAG, "Could not parse verifier public key; invalid Base64");
            return null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:40:0x00b5, code lost:
        r26[0] = "No value supplied for <android:name>";
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00ba, code lost:
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00da, code lost:
        r26[0] = "No value supplied for <android:name>";
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00e1, code lost:
        return false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized boolean parseIntent(android.content.res.Resources r21, android.content.res.XmlResourceParser r22, boolean r23, boolean r24, android.content.pm.PackageParser.IntentInfo r25, java.lang.String[] r26) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 494
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.parseIntent(android.content.res.Resources, android.content.res.XmlResourceParser, boolean, boolean, android.content.pm.PackageParser$IntentInfo, java.lang.String[]):boolean");
    }

    /* loaded from: classes.dex */
    public static final class SigningDetails implements Parcelable {
        private static final int PAST_CERT_EXISTS = 0;
        public final Signature[] pastSigningCertificates;
        public final int[] pastSigningCertificatesFlags;
        public final ArraySet<PublicKey> publicKeys;
        @SignatureSchemeVersion
        public final int signatureSchemeVersion;
        private protected final Signature[] signatures;
        public static final SigningDetails UNKNOWN = new SigningDetails(null, 0, null, null, null);
        public static final Parcelable.Creator<SigningDetails> CREATOR = new Parcelable.Creator<SigningDetails>() { // from class: android.content.pm.PackageParser.SigningDetails.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SigningDetails createFromParcel(Parcel source) {
                if (source.readBoolean()) {
                    return SigningDetails.UNKNOWN;
                }
                return new SigningDetails(source);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SigningDetails[] newArray(int size) {
                return new SigningDetails[size];
            }
        };

        /* loaded from: classes.dex */
        public @interface CertCapabilities {
            public static final int AUTH = 16;
            public static final int INSTALLED_DATA = 1;
            public static final int PERMISSION = 4;
            public static final int ROLLBACK = 8;
            public static final int SHARED_USER_ID = 2;
        }

        /* loaded from: classes.dex */
        public @interface SignatureSchemeVersion {
            public static final int JAR = 1;
            public static final int SIGNING_BLOCK_V2 = 2;
            public static final int SIGNING_BLOCK_V3 = 3;
            public static final int UNKNOWN = 0;
        }

        @VisibleForTesting
        public synchronized SigningDetails(Signature[] signatures, @SignatureSchemeVersion int signatureSchemeVersion, ArraySet<PublicKey> keys, Signature[] pastSigningCertificates, int[] pastSigningCertificatesFlags) {
            this.signatures = signatures;
            this.signatureSchemeVersion = signatureSchemeVersion;
            this.publicKeys = keys;
            this.pastSigningCertificates = pastSigningCertificates;
            this.pastSigningCertificatesFlags = pastSigningCertificatesFlags;
        }

        public synchronized SigningDetails(Signature[] signatures, @SignatureSchemeVersion int signatureSchemeVersion, Signature[] pastSigningCertificates, int[] pastSigningCertificatesFlags) throws CertificateException {
            this(signatures, signatureSchemeVersion, PackageParser.toSigningKeys(signatures), pastSigningCertificates, pastSigningCertificatesFlags);
        }

        public synchronized SigningDetails(Signature[] signatures, @SignatureSchemeVersion int signatureSchemeVersion) throws CertificateException {
            this(signatures, signatureSchemeVersion, null, null);
        }

        public synchronized SigningDetails(SigningDetails orig) {
            if (orig != null) {
                if (orig.signatures != null) {
                    this.signatures = (Signature[]) orig.signatures.clone();
                } else {
                    this.signatures = null;
                }
                this.signatureSchemeVersion = orig.signatureSchemeVersion;
                this.publicKeys = new ArraySet<>(orig.publicKeys);
                if (orig.pastSigningCertificates != null) {
                    this.pastSigningCertificates = (Signature[]) orig.pastSigningCertificates.clone();
                    this.pastSigningCertificatesFlags = (int[]) orig.pastSigningCertificatesFlags.clone();
                    return;
                }
                this.pastSigningCertificates = null;
                this.pastSigningCertificatesFlags = null;
                return;
            }
            this.signatures = null;
            this.signatureSchemeVersion = 0;
            this.publicKeys = null;
            this.pastSigningCertificates = null;
            this.pastSigningCertificatesFlags = null;
        }

        public synchronized boolean hasSignatures() {
            return this.signatures != null && this.signatures.length > 0;
        }

        public synchronized boolean hasPastSigningCertificates() {
            return this.pastSigningCertificates != null && this.pastSigningCertificates.length > 0;
        }

        public synchronized boolean hasAncestorOrSelf(SigningDetails oldDetails) {
            if (this == UNKNOWN || oldDetails == UNKNOWN) {
                return false;
            }
            if (oldDetails.signatures.length > 1) {
                return signaturesMatchExactly(oldDetails);
            }
            return hasCertificate(oldDetails.signatures[0]);
        }

        public synchronized boolean hasAncestor(SigningDetails oldDetails) {
            if (this != UNKNOWN && oldDetails != UNKNOWN && hasPastSigningCertificates() && oldDetails.signatures.length == 1) {
                for (int i = 0; i < this.pastSigningCertificates.length - 1; i++) {
                    if (this.pastSigningCertificates[i].equals(oldDetails.signatures[i])) {
                        return true;
                    }
                }
            }
            return false;
        }

        public synchronized boolean checkCapability(SigningDetails oldDetails, @CertCapabilities int flags) {
            if (this == UNKNOWN || oldDetails == UNKNOWN) {
                return false;
            }
            if (oldDetails.signatures.length > 1) {
                return signaturesMatchExactly(oldDetails);
            }
            return hasCertificate(oldDetails.signatures[0], flags);
        }

        public synchronized boolean checkCapabilityRecover(SigningDetails oldDetails, @CertCapabilities int flags) throws CertificateException {
            if (oldDetails == UNKNOWN || this == UNKNOWN) {
                return false;
            }
            if (hasPastSigningCertificates() && oldDetails.signatures.length == 1) {
                for (int i = 0; i < this.pastSigningCertificates.length; i++) {
                    if (Signature.areEffectiveMatch(oldDetails.signatures[0], this.pastSigningCertificates[i]) && this.pastSigningCertificatesFlags[i] == flags) {
                        return true;
                    }
                }
                return false;
            }
            return Signature.areEffectiveMatch(oldDetails.signatures, this.signatures);
        }

        public synchronized boolean hasCertificate(Signature signature) {
            return hasCertificateInternal(signature, 0);
        }

        public synchronized boolean hasCertificate(Signature signature, @CertCapabilities int flags) {
            return hasCertificateInternal(signature, flags);
        }

        public synchronized boolean hasCertificate(byte[] certificate) {
            Signature signature = new Signature(certificate);
            return hasCertificate(signature);
        }

        private synchronized boolean hasCertificateInternal(Signature signature, int flags) {
            if (this == UNKNOWN) {
                return false;
            }
            if (hasPastSigningCertificates()) {
                for (int i = 0; i < this.pastSigningCertificates.length - 1; i++) {
                    if (this.pastSigningCertificates[i].equals(signature) && (flags == 0 || (this.pastSigningCertificatesFlags[i] & flags) == flags)) {
                        return true;
                    }
                }
            }
            return this.signatures.length == 1 && this.signatures[0].equals(signature);
        }

        public synchronized boolean checkCapability(String sha256String, @CertCapabilities int flags) {
            if (this == UNKNOWN) {
                return false;
            }
            byte[] sha256Bytes = ByteStringUtils.fromHexToByteArray(sha256String);
            if (hasSha256Certificate(sha256Bytes, flags)) {
                return true;
            }
            String[] mSignaturesSha256Digests = PackageUtils.computeSignaturesSha256Digests(this.signatures);
            String mSignaturesSha256Digest = PackageUtils.computeSignaturesSha256Digest(mSignaturesSha256Digests);
            return mSignaturesSha256Digest.equals(sha256String);
        }

        public synchronized boolean hasSha256Certificate(byte[] sha256Certificate) {
            return hasSha256CertificateInternal(sha256Certificate, 0);
        }

        public synchronized boolean hasSha256Certificate(byte[] sha256Certificate, @CertCapabilities int flags) {
            return hasSha256CertificateInternal(sha256Certificate, flags);
        }

        private synchronized boolean hasSha256CertificateInternal(byte[] sha256Certificate, int flags) {
            if (this == UNKNOWN) {
                return false;
            }
            if (hasPastSigningCertificates()) {
                for (int i = 0; i < this.pastSigningCertificates.length - 1; i++) {
                    byte[] digest = PackageUtils.computeSha256DigestBytes(this.pastSigningCertificates[i].toByteArray());
                    if (Arrays.equals(sha256Certificate, digest) && (flags == 0 || (this.pastSigningCertificatesFlags[i] & flags) == flags)) {
                        return true;
                    }
                }
            }
            if (this.signatures.length == 1) {
                byte[] digest2 = PackageUtils.computeSha256DigestBytes(this.signatures[0].toByteArray());
                return Arrays.equals(sha256Certificate, digest2);
            }
            return false;
        }

        public synchronized boolean signaturesMatchExactly(SigningDetails other) {
            return Signature.areExactMatch(this.signatures, other.signatures);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            boolean isUnknown = UNKNOWN == this;
            dest.writeBoolean(isUnknown);
            if (isUnknown) {
                return;
            }
            dest.writeTypedArray(this.signatures, flags);
            dest.writeInt(this.signatureSchemeVersion);
            dest.writeArraySet(this.publicKeys);
            dest.writeTypedArray(this.pastSigningCertificates, flags);
            dest.writeIntArray(this.pastSigningCertificatesFlags);
        }

        protected synchronized SigningDetails(Parcel in) {
            ClassLoader boot = Object.class.getClassLoader();
            this.signatures = (Signature[]) in.createTypedArray(Signature.CREATOR);
            this.signatureSchemeVersion = in.readInt();
            this.publicKeys = in.readArraySet(boot);
            this.pastSigningCertificates = (Signature[]) in.createTypedArray(Signature.CREATOR);
            this.pastSigningCertificatesFlags = in.createIntArray();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o instanceof SigningDetails) {
                SigningDetails that = (SigningDetails) o;
                if (this.signatureSchemeVersion == that.signatureSchemeVersion && Signature.areExactMatch(this.signatures, that.signatures)) {
                    if (this.publicKeys != null) {
                        if (!this.publicKeys.equals(that.publicKeys)) {
                            return false;
                        }
                    } else if (that.publicKeys != null) {
                        return false;
                    }
                    return Arrays.equals(this.pastSigningCertificates, that.pastSigningCertificates) && Arrays.equals(this.pastSigningCertificatesFlags, that.pastSigningCertificatesFlags);
                }
                return false;
            }
            return false;
        }

        public int hashCode() {
            int result = Arrays.hashCode(this.signatures);
            return (31 * ((31 * ((31 * ((31 * result) + this.signatureSchemeVersion)) + (this.publicKeys != null ? this.publicKeys.hashCode() : 0))) + Arrays.hashCode(this.pastSigningCertificates))) + Arrays.hashCode(this.pastSigningCertificatesFlags);
        }

        /* loaded from: classes.dex */
        public static class Builder {
            private Signature[] mPastSigningCertificates;
            private int[] mPastSigningCertificatesFlags;
            private int mSignatureSchemeVersion = 0;
            private Signature[] mSignatures;

            private protected Builder() {
            }

            private protected Builder setSignatures(Signature[] signatures) {
                this.mSignatures = signatures;
                return this;
            }

            private protected Builder setSignatureSchemeVersion(int signatureSchemeVersion) {
                this.mSignatureSchemeVersion = signatureSchemeVersion;
                return this;
            }

            private protected Builder setPastSigningCertificates(Signature[] pastSigningCertificates) {
                this.mPastSigningCertificates = pastSigningCertificates;
                return this;
            }

            private protected Builder setPastSigningCertificatesFlags(int[] pastSigningCertificatesFlags) {
                this.mPastSigningCertificatesFlags = pastSigningCertificatesFlags;
                return this;
            }

            private synchronized void checkInvariants() {
                if (this.mSignatures == null) {
                    throw new IllegalStateException("SigningDetails requires the current signing certificates.");
                }
                boolean pastMismatch = false;
                if (this.mPastSigningCertificates != null && this.mPastSigningCertificatesFlags != null) {
                    if (this.mPastSigningCertificates.length != this.mPastSigningCertificatesFlags.length) {
                        pastMismatch = true;
                    }
                } else if (this.mPastSigningCertificates != null || this.mPastSigningCertificatesFlags != null) {
                    pastMismatch = true;
                }
                if (pastMismatch) {
                    throw new IllegalStateException("SigningDetails must have a one to one mapping between pastSigningCertificates and pastSigningCertificatesFlags");
                }
            }

            private protected SigningDetails build() throws CertificateException {
                checkInvariants();
                return new SigningDetails(this.mSignatures, this.mSignatureSchemeVersion, this.mPastSigningCertificates, this.mPastSigningCertificatesFlags);
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class Package implements Parcelable {
        public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Package>() { // from class: android.content.pm.PackageParser.Package.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Package createFromParcel(Parcel in) {
                return new Package(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Package[] newArray(int size) {
                return new Package[size];
            }
        };
        private protected final ArrayList<Activity> activities;
        private protected ApplicationInfo applicationInfo;
        public String baseCodePath;
        public boolean baseHardwareAccelerated;
        public int baseRevisionCode;
        public ArrayList<Package> childPackages;
        public String codePath;
        private protected ArrayList<ConfigurationInfo> configPreferences;
        public boolean coreApp;
        public String cpuAbiOverride;
        public ArrayList<FeatureGroupInfo> featureGroups;
        private protected int installLocation;
        private protected final ArrayList<Instrumentation> instrumentation;
        public boolean isStub;
        public ArrayList<String> libraryNames;
        public ArrayList<String> mAdoptPermissions;
        private protected Bundle mAppMetaData;
        public int mCompileSdkVersion;
        public String mCompileSdkVersionCodename;
        private protected Object mExtras;
        private protected ArrayMap<String, ArraySet<PublicKey>> mKeySetMapping;
        public long[] mLastPackageUsageTimeInMills;
        public ArrayList<String> mOriginalPackages;
        public String mOverlayCategory;
        public boolean mOverlayIsStatic;
        public int mOverlayPriority;
        public String mOverlayTarget;
        private protected int mPreferredOrder;
        public String mRealPackage;
        public String mRequiredAccountType;
        public boolean mRequiredForAllUsers;
        public String mRestrictedAccountType;
        private protected String mSharedUserId;
        private protected int mSharedUserLabel;
        private protected SigningDetails mSigningDetails;
        private protected ArraySet<String> mUpgradeKeySets;
        private protected int mVersionCode;
        public int mVersionCodeMajor;
        private protected String mVersionName;
        public String manifestPackageName;
        private protected String packageName;
        public Package parentPackage;
        private protected final ArrayList<PermissionGroup> permissionGroups;
        private protected final ArrayList<Permission> permissions;
        public ArrayList<ActivityIntentInfo> preferredActivityFilters;
        private protected ArrayList<String> protectedBroadcasts;
        private protected final ArrayList<Provider> providers;
        private protected final ArrayList<Activity> receivers;
        private protected ArrayList<FeatureInfo> reqFeatures;
        private protected final ArrayList<String> requestedPermissions;
        public byte[] restrictUpdateHash;
        private protected final ArrayList<Service> services;
        public String[] splitCodePaths;
        public int[] splitFlags;
        public String[] splitNames;
        public int[] splitPrivateFlags;
        public int[] splitRevisionCodes;
        public String staticSharedLibName;
        public long staticSharedLibVersion;
        public boolean use32bitAbi;
        private protected ArrayList<String> usesLibraries;
        private protected String[] usesLibraryFiles;
        private protected ArrayList<String> usesOptionalLibraries;
        public ArrayList<String> usesStaticLibraries;
        public String[][] usesStaticLibrariesCertDigests;
        public long[] usesStaticLibrariesVersions;
        public boolean visibleToInstantApps;
        public String volumeUuid;

        public synchronized long getLongVersionCode() {
            return PackageInfo.composeLongVersionCode(this.mVersionCodeMajor, this.mVersionCode);
        }

        private protected Package(String packageName) {
            this.applicationInfo = new ApplicationInfo();
            this.permissions = new ArrayList<>(0);
            this.permissionGroups = new ArrayList<>(0);
            this.activities = new ArrayList<>(0);
            this.receivers = new ArrayList<>(0);
            this.providers = new ArrayList<>(0);
            this.services = new ArrayList<>(0);
            this.instrumentation = new ArrayList<>(0);
            this.requestedPermissions = new ArrayList<>();
            this.staticSharedLibName = null;
            this.staticSharedLibVersion = 0L;
            this.libraryNames = null;
            this.usesLibraries = null;
            this.usesStaticLibraries = null;
            this.usesStaticLibrariesVersions = null;
            this.usesStaticLibrariesCertDigests = null;
            this.usesOptionalLibraries = null;
            this.usesLibraryFiles = null;
            this.preferredActivityFilters = null;
            this.mOriginalPackages = null;
            this.mRealPackage = null;
            this.mAdoptPermissions = null;
            this.mAppMetaData = null;
            this.mSigningDetails = SigningDetails.UNKNOWN;
            this.mPreferredOrder = 0;
            this.mLastPackageUsageTimeInMills = new long[8];
            this.configPreferences = null;
            this.reqFeatures = null;
            this.featureGroups = null;
            this.packageName = packageName;
            this.manifestPackageName = packageName;
            this.applicationInfo.packageName = packageName;
            this.applicationInfo.uid = -1;
        }

        public synchronized void setApplicationVolumeUuid(String volumeUuid) {
            UUID storageUuid = StorageManager.convert(volumeUuid);
            this.applicationInfo.volumeUuid = volumeUuid;
            this.applicationInfo.storageUuid = storageUuid;
            if (this.childPackages != null) {
                int packageCount = this.childPackages.size();
                for (int i = 0; i < packageCount; i++) {
                    this.childPackages.get(i).applicationInfo.volumeUuid = volumeUuid;
                    this.childPackages.get(i).applicationInfo.storageUuid = storageUuid;
                }
            }
        }

        public synchronized void setApplicationInfoCodePath(String codePath) {
            this.applicationInfo.setCodePath(codePath);
            if (this.childPackages != null) {
                int packageCount = this.childPackages.size();
                for (int i = 0; i < packageCount; i++) {
                    this.childPackages.get(i).applicationInfo.setCodePath(codePath);
                }
            }
        }

        @Deprecated
        public synchronized void setApplicationInfoResourcePath(String resourcePath) {
            this.applicationInfo.setResourcePath(resourcePath);
            if (this.childPackages != null) {
                int packageCount = this.childPackages.size();
                for (int i = 0; i < packageCount; i++) {
                    this.childPackages.get(i).applicationInfo.setResourcePath(resourcePath);
                }
            }
        }

        @Deprecated
        public synchronized void setApplicationInfoBaseResourcePath(String resourcePath) {
            this.applicationInfo.setBaseResourcePath(resourcePath);
            if (this.childPackages != null) {
                int packageCount = this.childPackages.size();
                for (int i = 0; i < packageCount; i++) {
                    this.childPackages.get(i).applicationInfo.setBaseResourcePath(resourcePath);
                }
            }
        }

        public synchronized void setApplicationInfoBaseCodePath(String baseCodePath) {
            this.applicationInfo.setBaseCodePath(baseCodePath);
            if (this.childPackages != null) {
                int packageCount = this.childPackages.size();
                for (int i = 0; i < packageCount; i++) {
                    this.childPackages.get(i).applicationInfo.setBaseCodePath(baseCodePath);
                }
            }
        }

        public synchronized List<String> getChildPackageNames() {
            if (this.childPackages == null) {
                return null;
            }
            int childCount = this.childPackages.size();
            List<String> childPackageNames = new ArrayList<>(childCount);
            for (int i = 0; i < childCount; i++) {
                String childPackageName = this.childPackages.get(i).packageName;
                childPackageNames.add(childPackageName);
            }
            return childPackageNames;
        }

        public synchronized boolean hasChildPackage(String packageName) {
            int childCount = this.childPackages != null ? this.childPackages.size() : 0;
            for (int i = 0; i < childCount; i++) {
                if (this.childPackages.get(i).packageName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }

        public synchronized void setApplicationInfoSplitCodePaths(String[] splitCodePaths) {
            this.applicationInfo.setSplitCodePaths(splitCodePaths);
        }

        @Deprecated
        public synchronized void setApplicationInfoSplitResourcePaths(String[] resroucePaths) {
            this.applicationInfo.setSplitResourcePaths(resroucePaths);
        }

        public synchronized void setSplitCodePaths(String[] codePaths) {
            this.splitCodePaths = codePaths;
        }

        public synchronized void setCodePath(String codePath) {
            this.codePath = codePath;
            if (this.childPackages != null) {
                int packageCount = this.childPackages.size();
                for (int i = 0; i < packageCount; i++) {
                    this.childPackages.get(i).codePath = codePath;
                }
            }
        }

        public synchronized void setBaseCodePath(String baseCodePath) {
            this.baseCodePath = baseCodePath;
            if (this.childPackages != null) {
                int packageCount = this.childPackages.size();
                for (int i = 0; i < packageCount; i++) {
                    this.childPackages.get(i).baseCodePath = baseCodePath;
                }
            }
        }

        public synchronized void setSigningDetails(SigningDetails signingDetails) {
            this.mSigningDetails = signingDetails;
            if (this.childPackages != null) {
                int packageCount = this.childPackages.size();
                for (int i = 0; i < packageCount; i++) {
                    this.childPackages.get(i).mSigningDetails = signingDetails;
                }
            }
        }

        public synchronized void setVolumeUuid(String volumeUuid) {
            this.volumeUuid = volumeUuid;
            if (this.childPackages != null) {
                int packageCount = this.childPackages.size();
                for (int i = 0; i < packageCount; i++) {
                    this.childPackages.get(i).volumeUuid = volumeUuid;
                }
            }
        }

        public synchronized void setApplicationInfoFlags(int mask, int flags) {
            this.applicationInfo.flags = (this.applicationInfo.flags & (~mask)) | (mask & flags);
            if (this.childPackages != null) {
                int packageCount = this.childPackages.size();
                for (int i = 0; i < packageCount; i++) {
                    this.childPackages.get(i).applicationInfo.flags = (this.applicationInfo.flags & (~mask)) | (mask & flags);
                }
            }
        }

        public synchronized void setUse32bitAbi(boolean use32bitAbi) {
            this.use32bitAbi = use32bitAbi;
            if (this.childPackages != null) {
                int packageCount = this.childPackages.size();
                for (int i = 0; i < packageCount; i++) {
                    this.childPackages.get(i).use32bitAbi = use32bitAbi;
                }
            }
        }

        public synchronized boolean isLibrary() {
            return (this.staticSharedLibName == null && ArrayUtils.isEmpty(this.libraryNames)) ? false : true;
        }

        public synchronized List<String> getAllCodePaths() {
            ArrayList<String> paths = new ArrayList<>();
            paths.add(this.baseCodePath);
            if (!ArrayUtils.isEmpty(this.splitCodePaths)) {
                Collections.addAll(paths, this.splitCodePaths);
            }
            return paths;
        }

        public synchronized List<String> getAllCodePathsExcludingResourceOnly() {
            ArrayList<String> paths = new ArrayList<>();
            if ((this.applicationInfo.flags & 4) != 0) {
                paths.add(this.baseCodePath);
            }
            if (!ArrayUtils.isEmpty(this.splitCodePaths)) {
                for (int i = 0; i < this.splitCodePaths.length; i++) {
                    if ((this.splitFlags[i] & 4) != 0) {
                        paths.add(this.splitCodePaths[i]);
                    }
                }
            }
            return paths;
        }

        private protected void setPackageName(String newName) {
            this.packageName = newName;
            this.applicationInfo.packageName = newName;
            for (int i = this.permissions.size() - 1; i >= 0; i--) {
                this.permissions.get(i).setPackageName(newName);
            }
            for (int i2 = this.permissionGroups.size() - 1; i2 >= 0; i2--) {
                this.permissionGroups.get(i2).setPackageName(newName);
            }
            for (int i3 = this.activities.size() - 1; i3 >= 0; i3--) {
                this.activities.get(i3).setPackageName(newName);
            }
            for (int i4 = this.receivers.size() - 1; i4 >= 0; i4--) {
                this.receivers.get(i4).setPackageName(newName);
            }
            for (int i5 = this.providers.size() - 1; i5 >= 0; i5--) {
                this.providers.get(i5).setPackageName(newName);
            }
            for (int i6 = this.services.size() - 1; i6 >= 0; i6--) {
                this.services.get(i6).setPackageName(newName);
            }
            for (int i7 = this.instrumentation.size() - 1; i7 >= 0; i7--) {
                this.instrumentation.get(i7).setPackageName(newName);
            }
        }

        public synchronized boolean hasComponentClassName(String name) {
            for (int i = this.activities.size() - 1; i >= 0; i--) {
                if (name.equals(this.activities.get(i).className)) {
                    return true;
                }
            }
            for (int i2 = this.receivers.size() - 1; i2 >= 0; i2--) {
                if (name.equals(this.receivers.get(i2).className)) {
                    return true;
                }
            }
            for (int i3 = this.providers.size() - 1; i3 >= 0; i3--) {
                if (name.equals(this.providers.get(i3).className)) {
                    return true;
                }
            }
            for (int i4 = this.services.size() - 1; i4 >= 0; i4--) {
                if (name.equals(this.services.get(i4).className)) {
                    return true;
                }
            }
            for (int i5 = this.instrumentation.size() - 1; i5 >= 0; i5--) {
                if (name.equals(this.instrumentation.get(i5).className)) {
                    return true;
                }
            }
            return false;
        }

        public synchronized boolean isExternal() {
            return this.applicationInfo.isExternal();
        }

        public synchronized boolean isForwardLocked() {
            return this.applicationInfo.isForwardLocked();
        }

        public synchronized boolean isOem() {
            return this.applicationInfo.isOem();
        }

        public synchronized boolean isVendor() {
            return this.applicationInfo.isVendor();
        }

        public synchronized boolean isProduct() {
            return this.applicationInfo.isProduct();
        }

        public synchronized boolean isPrivileged() {
            return this.applicationInfo.isPrivilegedApp();
        }

        public synchronized boolean isSystem() {
            return this.applicationInfo.isSystemApp();
        }

        public synchronized boolean isUpdatedSystemApp() {
            return this.applicationInfo.isUpdatedSystemApp();
        }

        public synchronized boolean canHaveOatDir() {
            return ((isSystem() && !isUpdatedSystemApp()) || isForwardLocked() || this.applicationInfo.isExternalAsec()) ? false : true;
        }

        public synchronized boolean isMatch(int flags) {
            if ((1048576 & flags) != 0) {
                return isSystem();
            }
            return true;
        }

        public synchronized long getLatestPackageUseTimeInMills() {
            long[] jArr;
            long latestUse = 0;
            for (long use : this.mLastPackageUsageTimeInMills) {
                latestUse = Math.max(latestUse, use);
            }
            return latestUse;
        }

        public synchronized long getLatestForegroundPackageUseTimeInMills() {
            int[] foregroundReasons = {0, 2};
            long latestUse = 0;
            for (int reason : foregroundReasons) {
                latestUse = Math.max(latestUse, this.mLastPackageUsageTimeInMills[reason]);
            }
            return latestUse;
        }

        public String toString() {
            return "Package{" + Integer.toHexString(System.identityHashCode(this)) + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + this.packageName + "}";
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public synchronized Package(Parcel dest) {
            this.applicationInfo = new ApplicationInfo();
            this.permissions = new ArrayList<>(0);
            this.permissionGroups = new ArrayList<>(0);
            this.activities = new ArrayList<>(0);
            this.receivers = new ArrayList<>(0);
            this.providers = new ArrayList<>(0);
            this.services = new ArrayList<>(0);
            this.instrumentation = new ArrayList<>(0);
            this.requestedPermissions = new ArrayList<>();
            this.staticSharedLibName = null;
            this.staticSharedLibVersion = 0L;
            this.libraryNames = null;
            this.usesLibraries = null;
            this.usesStaticLibraries = null;
            this.usesStaticLibrariesVersions = null;
            this.usesStaticLibrariesCertDigests = null;
            this.usesOptionalLibraries = null;
            this.usesLibraryFiles = null;
            this.preferredActivityFilters = null;
            this.mOriginalPackages = null;
            this.mRealPackage = null;
            this.mAdoptPermissions = null;
            this.mAppMetaData = null;
            this.mSigningDetails = SigningDetails.UNKNOWN;
            this.mPreferredOrder = 0;
            this.mLastPackageUsageTimeInMills = new long[8];
            this.configPreferences = null;
            this.reqFeatures = null;
            this.featureGroups = null;
            ClassLoader boot = Object.class.getClassLoader();
            this.packageName = dest.readString().intern();
            this.manifestPackageName = dest.readString();
            this.splitNames = dest.readStringArray();
            this.volumeUuid = dest.readString();
            this.codePath = dest.readString();
            this.baseCodePath = dest.readString();
            this.splitCodePaths = dest.readStringArray();
            this.baseRevisionCode = dest.readInt();
            this.splitRevisionCodes = dest.createIntArray();
            this.splitFlags = dest.createIntArray();
            this.splitPrivateFlags = dest.createIntArray();
            this.baseHardwareAccelerated = dest.readInt() == 1;
            this.applicationInfo = (ApplicationInfo) dest.readParcelable(boot);
            if (this.applicationInfo.permission != null) {
                this.applicationInfo.permission = this.applicationInfo.permission.intern();
            }
            dest.readParcelableList(this.permissions, boot);
            fixupOwner(this.permissions);
            dest.readParcelableList(this.permissionGroups, boot);
            fixupOwner(this.permissionGroups);
            dest.readParcelableList(this.activities, boot);
            fixupOwner(this.activities);
            dest.readParcelableList(this.receivers, boot);
            fixupOwner(this.receivers);
            dest.readParcelableList(this.providers, boot);
            fixupOwner(this.providers);
            dest.readParcelableList(this.services, boot);
            fixupOwner(this.services);
            dest.readParcelableList(this.instrumentation, boot);
            fixupOwner(this.instrumentation);
            dest.readStringList(this.requestedPermissions);
            internStringArrayList(this.requestedPermissions);
            this.protectedBroadcasts = dest.createStringArrayList();
            internStringArrayList(this.protectedBroadcasts);
            this.parentPackage = (Package) dest.readParcelable(boot);
            this.childPackages = new ArrayList<>();
            dest.readParcelableList(this.childPackages, boot);
            if (this.childPackages.size() == 0) {
                this.childPackages = null;
            }
            this.staticSharedLibName = dest.readString();
            if (this.staticSharedLibName != null) {
                this.staticSharedLibName = this.staticSharedLibName.intern();
            }
            this.staticSharedLibVersion = dest.readLong();
            this.libraryNames = dest.createStringArrayList();
            internStringArrayList(this.libraryNames);
            this.usesLibraries = dest.createStringArrayList();
            internStringArrayList(this.usesLibraries);
            this.usesOptionalLibraries = dest.createStringArrayList();
            internStringArrayList(this.usesOptionalLibraries);
            this.usesLibraryFiles = dest.readStringArray();
            int libCount = dest.readInt();
            if (libCount > 0) {
                this.usesStaticLibraries = new ArrayList<>(libCount);
                dest.readStringList(this.usesStaticLibraries);
                internStringArrayList(this.usesStaticLibraries);
                this.usesStaticLibrariesVersions = new long[libCount];
                dest.readLongArray(this.usesStaticLibrariesVersions);
                this.usesStaticLibrariesCertDigests = new String[libCount];
                for (int i = 0; i < libCount; i++) {
                    this.usesStaticLibrariesCertDigests[i] = dest.createStringArray();
                }
            }
            this.preferredActivityFilters = new ArrayList<>();
            dest.readParcelableList(this.preferredActivityFilters, boot);
            if (this.preferredActivityFilters.size() == 0) {
                this.preferredActivityFilters = null;
            }
            this.mOriginalPackages = dest.createStringArrayList();
            this.mRealPackage = dest.readString();
            this.mAdoptPermissions = dest.createStringArrayList();
            this.mAppMetaData = dest.readBundle();
            this.mVersionCode = dest.readInt();
            this.mVersionCodeMajor = dest.readInt();
            this.mVersionName = dest.readString();
            if (this.mVersionName != null) {
                this.mVersionName = this.mVersionName.intern();
            }
            this.mSharedUserId = dest.readString();
            if (this.mSharedUserId != null) {
                this.mSharedUserId = this.mSharedUserId.intern();
            }
            this.mSharedUserLabel = dest.readInt();
            this.mSigningDetails = (SigningDetails) dest.readParcelable(boot);
            this.mPreferredOrder = dest.readInt();
            this.configPreferences = new ArrayList<>();
            dest.readParcelableList(this.configPreferences, boot);
            if (this.configPreferences.size() == 0) {
                this.configPreferences = null;
            }
            this.reqFeatures = new ArrayList<>();
            dest.readParcelableList(this.reqFeatures, boot);
            if (this.reqFeatures.size() == 0) {
                this.reqFeatures = null;
            }
            this.featureGroups = new ArrayList<>();
            dest.readParcelableList(this.featureGroups, boot);
            if (this.featureGroups.size() == 0) {
                this.featureGroups = null;
            }
            this.installLocation = dest.readInt();
            this.coreApp = dest.readInt() == 1;
            this.mRequiredForAllUsers = dest.readInt() == 1;
            this.mRestrictedAccountType = dest.readString();
            this.mRequiredAccountType = dest.readString();
            this.mOverlayTarget = dest.readString();
            this.mOverlayCategory = dest.readString();
            this.mOverlayPriority = dest.readInt();
            this.mOverlayIsStatic = dest.readInt() == 1;
            this.mCompileSdkVersion = dest.readInt();
            this.mCompileSdkVersionCodename = dest.readString();
            this.mUpgradeKeySets = dest.readArraySet(boot);
            this.mKeySetMapping = readKeySetMapping(dest);
            this.cpuAbiOverride = dest.readString();
            this.use32bitAbi = dest.readInt() == 1;
            this.restrictUpdateHash = dest.createByteArray();
            this.visibleToInstantApps = dest.readInt() == 1;
        }

        private static synchronized void internStringArrayList(List<String> list) {
            if (list != null) {
                int N = list.size();
                for (int i = 0; i < N; i++) {
                    list.set(i, list.get(i).intern());
                }
            }
        }

        private synchronized void fixupOwner(List<? extends Component<?>> list) {
            if (list != null) {
                for (Component<?> c : list) {
                    ((Component) c).owner = this;
                    if (c instanceof Activity) {
                        ((Activity) c).info.applicationInfo = this.applicationInfo;
                    } else if (c instanceof Service) {
                        ((Service) c).info.applicationInfo = this.applicationInfo;
                    } else if (c instanceof Provider) {
                        ((Provider) c).info.applicationInfo = this.applicationInfo;
                    }
                }
            }
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            String[][] strArr;
            dest.writeString(this.packageName);
            dest.writeString(this.manifestPackageName);
            dest.writeStringArray(this.splitNames);
            dest.writeString(this.volumeUuid);
            dest.writeString(this.codePath);
            dest.writeString(this.baseCodePath);
            dest.writeStringArray(this.splitCodePaths);
            dest.writeInt(this.baseRevisionCode);
            dest.writeIntArray(this.splitRevisionCodes);
            dest.writeIntArray(this.splitFlags);
            dest.writeIntArray(this.splitPrivateFlags);
            dest.writeInt(this.baseHardwareAccelerated ? 1 : 0);
            dest.writeParcelable(this.applicationInfo, flags);
            dest.writeParcelableList(this.permissions, flags);
            dest.writeParcelableList(this.permissionGroups, flags);
            dest.writeParcelableList(this.activities, flags);
            dest.writeParcelableList(this.receivers, flags);
            dest.writeParcelableList(this.providers, flags);
            dest.writeParcelableList(this.services, flags);
            dest.writeParcelableList(this.instrumentation, flags);
            dest.writeStringList(this.requestedPermissions);
            dest.writeStringList(this.protectedBroadcasts);
            dest.writeParcelable(this.parentPackage, flags);
            dest.writeParcelableList(this.childPackages, flags);
            dest.writeString(this.staticSharedLibName);
            dest.writeLong(this.staticSharedLibVersion);
            dest.writeStringList(this.libraryNames);
            dest.writeStringList(this.usesLibraries);
            dest.writeStringList(this.usesOptionalLibraries);
            dest.writeStringArray(this.usesLibraryFiles);
            if (ArrayUtils.isEmpty(this.usesStaticLibraries)) {
                dest.writeInt(-1);
            } else {
                dest.writeInt(this.usesStaticLibraries.size());
                dest.writeStringList(this.usesStaticLibraries);
                dest.writeLongArray(this.usesStaticLibrariesVersions);
                for (String[] usesStaticLibrariesCertDigest : this.usesStaticLibrariesCertDigests) {
                    dest.writeStringArray(usesStaticLibrariesCertDigest);
                }
            }
            dest.writeParcelableList(this.preferredActivityFilters, flags);
            dest.writeStringList(this.mOriginalPackages);
            dest.writeString(this.mRealPackage);
            dest.writeStringList(this.mAdoptPermissions);
            dest.writeBundle(this.mAppMetaData);
            dest.writeInt(this.mVersionCode);
            dest.writeInt(this.mVersionCodeMajor);
            dest.writeString(this.mVersionName);
            dest.writeString(this.mSharedUserId);
            dest.writeInt(this.mSharedUserLabel);
            dest.writeParcelable(this.mSigningDetails, flags);
            dest.writeInt(this.mPreferredOrder);
            dest.writeParcelableList(this.configPreferences, flags);
            dest.writeParcelableList(this.reqFeatures, flags);
            dest.writeParcelableList(this.featureGroups, flags);
            dest.writeInt(this.installLocation);
            dest.writeInt(this.coreApp ? 1 : 0);
            dest.writeInt(this.mRequiredForAllUsers ? 1 : 0);
            dest.writeString(this.mRestrictedAccountType);
            dest.writeString(this.mRequiredAccountType);
            dest.writeString(this.mOverlayTarget);
            dest.writeString(this.mOverlayCategory);
            dest.writeInt(this.mOverlayPriority);
            dest.writeInt(this.mOverlayIsStatic ? 1 : 0);
            dest.writeInt(this.mCompileSdkVersion);
            dest.writeString(this.mCompileSdkVersionCodename);
            dest.writeArraySet(this.mUpgradeKeySets);
            writeKeySetMapping(dest, this.mKeySetMapping);
            dest.writeString(this.cpuAbiOverride);
            dest.writeInt(this.use32bitAbi ? 1 : 0);
            dest.writeByteArray(this.restrictUpdateHash);
            dest.writeInt(this.visibleToInstantApps ? 1 : 0);
        }

        private static synchronized void writeKeySetMapping(Parcel dest, ArrayMap<String, ArraySet<PublicKey>> keySetMapping) {
            if (keySetMapping == null) {
                dest.writeInt(-1);
                return;
            }
            int N = keySetMapping.size();
            dest.writeInt(N);
            for (int i = 0; i < N; i++) {
                dest.writeString(keySetMapping.keyAt(i));
                ArraySet<PublicKey> keys = keySetMapping.valueAt(i);
                if (keys == null) {
                    dest.writeInt(-1);
                } else {
                    int M = keys.size();
                    dest.writeInt(M);
                    for (int j = 0; j < M; j++) {
                        dest.writeSerializable(keys.valueAt(j));
                    }
                }
            }
        }

        private static synchronized ArrayMap<String, ArraySet<PublicKey>> readKeySetMapping(Parcel in) {
            int N = in.readInt();
            if (N == -1) {
                return null;
            }
            ArrayMap<String, ArraySet<PublicKey>> keySetMapping = new ArrayMap<>();
            for (int i = 0; i < N; i++) {
                String key = in.readString();
                int M = in.readInt();
                if (M == -1) {
                    keySetMapping.put(key, null);
                } else {
                    ArraySet<PublicKey> keys = new ArraySet<>(M);
                    for (int j = 0; j < M; j++) {
                        PublicKey pk = (PublicKey) in.readSerializable();
                        keys.add(pk);
                    }
                    keySetMapping.put(key, keys);
                }
            }
            return keySetMapping;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Component<II extends IntentInfo> {
        private protected final String className;
        ComponentName componentName;
        String componentShortName;
        private protected final ArrayList<II> intents;
        private protected Bundle metaData;
        public int order;
        private protected Package owner;

        public synchronized Component(Package _owner) {
            this.owner = _owner;
            this.intents = null;
            this.className = null;
        }

        public synchronized Component(ParsePackageItemArgs args, PackageItemInfo outInfo) {
            this.owner = args.owner;
            this.intents = new ArrayList<>(0);
            if (PackageParser.parsePackageItemInfo(args.owner, outInfo, args.outError, args.tag, args.sa, true, args.nameRes, args.labelRes, args.iconRes, args.roundIconRes, args.logoRes, args.bannerRes)) {
                this.className = outInfo.name;
            } else {
                this.className = null;
            }
        }

        public synchronized Component(ParseComponentArgs args, ComponentInfo outInfo) {
            this((ParsePackageItemArgs) args, (PackageItemInfo) outInfo);
            if (args.outError[0] != null) {
                return;
            }
            if (args.processRes != 0) {
                CharSequence pname = this.owner.applicationInfo.targetSdkVersion >= 8 ? args.sa.getNonConfigurationString(args.processRes, 1024) : args.sa.getNonResourceString(args.processRes);
                outInfo.processName = PackageParser.buildProcessName(this.owner.applicationInfo.packageName, this.owner.applicationInfo.processName, pname, args.flags, args.sepProcesses, args.outError);
            }
            if (args.descriptionRes != 0) {
                outInfo.descriptionRes = args.sa.getResourceId(args.descriptionRes, 0);
            }
            outInfo.enabled = args.sa.getBoolean(args.enabledRes, true);
        }

        public synchronized Component(Component<II> clone) {
            this.owner = clone.owner;
            this.intents = clone.intents;
            this.className = clone.className;
            this.componentName = clone.componentName;
            this.componentShortName = clone.componentShortName;
        }

        private protected ComponentName getComponentName() {
            if (this.componentName != null) {
                return this.componentName;
            }
            if (this.className != null) {
                this.componentName = new ComponentName(this.owner.applicationInfo.packageName, this.className);
            }
            return this.componentName;
        }

        protected synchronized Component(Parcel in) {
            this.className = in.readString();
            this.metaData = in.readBundle();
            this.intents = createIntentsList(in);
            this.owner = null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public synchronized void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.className);
            dest.writeBundle(this.metaData);
            writeIntentsList(this.intents, dest, flags);
        }

        private static synchronized void writeIntentsList(ArrayList<? extends IntentInfo> list, Parcel out, int flags) {
            if (list == null) {
                out.writeInt(-1);
                return;
            }
            int N = list.size();
            out.writeInt(N);
            if (N > 0) {
                IntentInfo info = list.get(0);
                out.writeString(info.getClass().getName());
                for (int i = 0; i < N; i++) {
                    list.get(i).writeIntentInfoToParcel(out, flags);
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        private static synchronized <T extends IntentInfo> ArrayList<T> createIntentsList(Parcel in) {
            int N = in.readInt();
            if (N == -1) {
                return null;
            }
            if (N == 0) {
                return new ArrayList<>(0);
            }
            String componentName = in.readString();
            try {
                Constructor<?> constructor = Class.forName(componentName).getConstructor(Parcel.class);
                ArrayList<T> intentsList = (ArrayList<T>) new ArrayList(N);
                for (int i = 0; i < N; i++) {
                    intentsList.add((IntentInfo) constructor.newInstance(in));
                }
                return intentsList;
            } catch (ReflectiveOperationException e) {
                throw new AssertionError("Unable to construct intent list for: " + componentName);
            }
        }

        public synchronized void appendComponentShortName(StringBuilder sb) {
            ComponentName.appendShortString(sb, this.owner.applicationInfo.packageName, this.className);
        }

        public synchronized void printComponentShortName(PrintWriter pw) {
            ComponentName.printShortString(pw, this.owner.applicationInfo.packageName, this.className);
        }

        public synchronized void setPackageName(String packageName) {
            this.componentName = null;
            this.componentShortName = null;
        }
    }

    /* loaded from: classes.dex */
    public static final class Permission extends Component<IntentInfo> implements Parcelable {
        public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Permission>() { // from class: android.content.pm.PackageParser.Permission.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Permission createFromParcel(Parcel in) {
                return new Permission(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Permission[] newArray(int size) {
                return new Permission[size];
            }
        };
        private protected PermissionGroup group;
        private protected final PermissionInfo info;
        private protected boolean tree;

        public synchronized Permission(Package _owner) {
            super(_owner);
            this.info = new PermissionInfo();
        }

        private protected Permission(Package _owner, PermissionInfo _info) {
            super(_owner);
            this.info = _info;
        }

        @Override // android.content.pm.PackageParser.Component
        public synchronized void setPackageName(String packageName) {
            super.setPackageName(packageName);
            this.info.packageName = packageName;
        }

        public String toString() {
            return "Permission{" + Integer.toHexString(System.identityHashCode(this)) + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + this.info.name + "}";
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.content.pm.PackageParser.Component, android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(this.info, flags);
            dest.writeInt(this.tree ? 1 : 0);
            dest.writeParcelable(this.group, flags);
        }

        public synchronized boolean isAppOp() {
            return this.info.isAppOp();
        }

        private synchronized Permission(Parcel in) {
            super(in);
            ClassLoader boot = Object.class.getClassLoader();
            this.info = (PermissionInfo) in.readParcelable(boot);
            if (this.info.group != null) {
                this.info.group = this.info.group.intern();
            }
            this.tree = in.readInt() == 1;
            this.group = (PermissionGroup) in.readParcelable(boot);
        }
    }

    /* loaded from: classes.dex */
    public static final class PermissionGroup extends Component<IntentInfo> implements Parcelable {
        public static final Parcelable.Creator CREATOR = new Parcelable.Creator<PermissionGroup>() { // from class: android.content.pm.PackageParser.PermissionGroup.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public PermissionGroup createFromParcel(Parcel in) {
                return new PermissionGroup(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public PermissionGroup[] newArray(int size) {
                return new PermissionGroup[size];
            }
        };
        private protected final PermissionGroupInfo info;

        public synchronized PermissionGroup(Package _owner) {
            super(_owner);
            this.info = new PermissionGroupInfo();
        }

        public synchronized PermissionGroup(Package _owner, PermissionGroupInfo _info) {
            super(_owner);
            this.info = _info;
        }

        @Override // android.content.pm.PackageParser.Component
        public synchronized void setPackageName(String packageName) {
            super.setPackageName(packageName);
            this.info.packageName = packageName;
        }

        public String toString() {
            return "PermissionGroup{" + Integer.toHexString(System.identityHashCode(this)) + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + this.info.name + "}";
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.content.pm.PackageParser.Component, android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(this.info, flags);
        }

        private synchronized PermissionGroup(Parcel in) {
            super(in);
            this.info = (PermissionGroupInfo) in.readParcelable(Object.class.getClassLoader());
        }
    }

    private static synchronized boolean copyNeeded(int flags, Package p, PackageUserState state, Bundle metaData, int userId) {
        if (userId != 0) {
            return true;
        }
        if (state.enabled != 0) {
            boolean enabled = state.enabled == 1;
            if (p.applicationInfo.enabled != enabled) {
                return true;
            }
        }
        boolean suspended = (p.applicationInfo.flags & 1073741824) != 0;
        if (state.suspended != suspended || !state.installed || state.hidden || state.stopped || state.instantApp != p.applicationInfo.isInstantApp()) {
            return true;
        }
        if ((flags & 128) == 0 || (metaData == null && p.mAppMetaData == null)) {
            return (((flags & 1024) == 0 || p.usesLibraryFiles == null) && p.staticSharedLibName == null) ? false : true;
        }
        return true;
    }

    private protected static ApplicationInfo generateApplicationInfo(Package p, int flags, PackageUserState state) {
        return generateApplicationInfo(p, flags, state, UserHandle.getCallingUserId());
    }

    private static synchronized void updateApplicationInfo(ApplicationInfo ai, int flags, PackageUserState state) {
        if (!sCompatibilityModeEnabled) {
            ai.disableCompatibilityMode();
        }
        if (state.installed) {
            ai.flags |= 8388608;
        } else {
            ai.flags &= -8388609;
        }
        if (state.suspended) {
            ai.flags |= 1073741824;
        } else {
            ai.flags &= -1073741825;
        }
        if (state.instantApp) {
            ai.privateFlags |= 128;
        } else {
            ai.privateFlags &= -129;
        }
        if (state.virtualPreload) {
            ai.privateFlags |= 65536;
        } else {
            ai.privateFlags &= -65537;
        }
        if (state.hidden) {
            ai.privateFlags |= 1;
        } else {
            ai.privateFlags &= -2;
        }
        if (state.enabled == 1) {
            ai.enabled = true;
        } else if (state.enabled == 4) {
            ai.enabled = (32768 & flags) != 0;
        } else if (state.enabled == 2 || state.enabled == 3) {
            ai.enabled = false;
        }
        ai.enabledSetting = state.enabled;
        if (ai.category == -1) {
            ai.category = state.categoryHint;
        }
        if (ai.category == -1) {
            ai.category = FallbackCategoryProvider.getFallbackCategory(ai.packageName);
        }
        ai.seInfoUser = SELinuxUtil.assignSeinfoUser(state);
        ai.resourceDirs = state.overlayPaths;
        if (AppGlobals.getPackageManager() != null) {
            try {
                AppGlobals.getPackageManager().getOverlayApplicationInfo(ai);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private protected static ApplicationInfo generateApplicationInfo(Package p, int flags, PackageUserState state, int userId) {
        if (p == null || !checkUseInstalledOrHidden(flags, state, p.applicationInfo) || !p.isMatch(flags)) {
            return null;
        }
        if (!copyNeeded(flags, p, state, null, userId) && ((32768 & flags) == 0 || state.enabled != 4)) {
            updateApplicationInfo(p.applicationInfo, flags, state);
            return p.applicationInfo;
        }
        ApplicationInfo ai = new ApplicationInfo(p.applicationInfo);
        ai.initForUser(userId);
        if ((flags & 128) != 0) {
            ai.metaData = p.mAppMetaData;
        }
        if ((flags & 1024) != 0) {
            ai.sharedLibraryFiles = p.usesLibraryFiles;
        }
        if (state.stopped) {
            ai.flags |= 2097152;
        } else {
            ai.flags &= -2097153;
        }
        updateApplicationInfo(ai, flags, state);
        return ai;
    }

    public static synchronized ApplicationInfo generateApplicationInfo(ApplicationInfo ai, int flags, PackageUserState state, int userId) {
        if (ai == null || !checkUseInstalledOrHidden(flags, state, ai)) {
            return null;
        }
        ApplicationInfo ai2 = new ApplicationInfo(ai);
        ai2.initForUser(userId);
        if (state.stopped) {
            ai2.flags |= 2097152;
        } else {
            ai2.flags &= -2097153;
        }
        updateApplicationInfo(ai2, flags, state);
        return ai2;
    }

    private protected static final PermissionInfo generatePermissionInfo(Permission p, int flags) {
        if (p == null) {
            return null;
        }
        if ((flags & 128) == 0) {
            return p.info;
        }
        PermissionInfo pi = new PermissionInfo(p.info);
        pi.metaData = p.metaData;
        return pi;
    }

    private protected static final PermissionGroupInfo generatePermissionGroupInfo(PermissionGroup pg, int flags) {
        if (pg == null) {
            return null;
        }
        if ((flags & 128) == 0) {
            return pg.info;
        }
        PermissionGroupInfo pgi = new PermissionGroupInfo(pg.info);
        pgi.metaData = pg.metaData;
        return pgi;
    }

    /* loaded from: classes.dex */
    public static final class Activity extends Component<ActivityIntentInfo> implements Parcelable {
        public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Activity>() { // from class: android.content.pm.PackageParser.Activity.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Activity createFromParcel(Parcel in) {
                return new Activity(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Activity[] newArray(int size) {
                return new Activity[size];
            }
        };
        private protected final ActivityInfo info;
        private boolean mHasMaxAspectRatio;

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized boolean hasMaxAspectRatio() {
            return this.mHasMaxAspectRatio;
        }

        public synchronized Activity(ParseComponentArgs args, ActivityInfo _info) {
            super(args, (ComponentInfo) _info);
            this.info = _info;
            this.info.applicationInfo = args.owner.applicationInfo;
        }

        @Override // android.content.pm.PackageParser.Component
        public synchronized void setPackageName(String packageName) {
            super.setPackageName(packageName);
            this.info.packageName = packageName;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void setMaxAspectRatio(float maxAspectRatio) {
            if (this.info.resizeMode == 2 || this.info.resizeMode == 1) {
                return;
            }
            if (maxAspectRatio < 1.0f && maxAspectRatio != 0.0f) {
                return;
            }
            this.info.maxAspectRatio = maxAspectRatio;
            this.mHasMaxAspectRatio = true;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append("Activity{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(' ');
            appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.content.pm.PackageParser.Component, android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(this.info, flags | 2);
            dest.writeBoolean(this.mHasMaxAspectRatio);
        }

        private synchronized Activity(Parcel in) {
            super(in);
            this.info = (ActivityInfo) in.readParcelable(Object.class.getClassLoader());
            this.mHasMaxAspectRatio = in.readBoolean();
            Iterator it = this.intents.iterator();
            while (it.hasNext()) {
                ActivityIntentInfo aii = (ActivityIntentInfo) it.next();
                aii.activity = this;
                this.order = Math.max(aii.getOrder(), this.order);
            }
            if (this.info.permission != null) {
                this.info.permission = this.info.permission.intern();
            }
        }
    }

    private protected static final ActivityInfo generateActivityInfo(Activity a, int flags, PackageUserState state, int userId) {
        if (a == null || !checkUseInstalledOrHidden(flags, state, a.owner.applicationInfo)) {
            return null;
        }
        if (!copyNeeded(flags, a.owner, state, a.metaData, userId)) {
            updateApplicationInfo(a.info.applicationInfo, flags, state);
            return a.info;
        }
        ActivityInfo ai = new ActivityInfo(a.info);
        ai.metaData = a.metaData;
        ai.applicationInfo = generateApplicationInfo(a.owner, flags, state, userId);
        if (AppGlobals.getPackageManager() != null) {
            try {
                return AppGlobals.getPackageManager().getOverlayActivityInfo(ai);
            } catch (Exception e) {
                e.printStackTrace();
                return ai;
            }
        }
        return ai;
    }

    public static final synchronized ActivityInfo generateActivityInfo(ActivityInfo ai, int flags, PackageUserState state, int userId) {
        if (ai == null || !checkUseInstalledOrHidden(flags, state, ai.applicationInfo)) {
            return null;
        }
        ActivityInfo ai2 = new ActivityInfo(ai);
        ai2.applicationInfo = generateApplicationInfo(ai2.applicationInfo, flags, state, userId);
        if (AppGlobals.getPackageManager() != null) {
            try {
                return AppGlobals.getPackageManager().getOverlayActivityInfo(ai2);
            } catch (Exception e) {
                e.printStackTrace();
                return ai2;
            }
        }
        return ai2;
    }

    /* loaded from: classes.dex */
    public static final class Service extends Component<ServiceIntentInfo> implements Parcelable {
        public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Service>() { // from class: android.content.pm.PackageParser.Service.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Service createFromParcel(Parcel in) {
                return new Service(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Service[] newArray(int size) {
                return new Service[size];
            }
        };
        private protected final ServiceInfo info;

        public synchronized Service(ParseComponentArgs args, ServiceInfo _info) {
            super(args, (ComponentInfo) _info);
            this.info = _info;
            this.info.applicationInfo = args.owner.applicationInfo;
        }

        @Override // android.content.pm.PackageParser.Component
        public synchronized void setPackageName(String packageName) {
            super.setPackageName(packageName);
            this.info.packageName = packageName;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append("Service{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(' ');
            appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.content.pm.PackageParser.Component, android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(this.info, flags | 2);
        }

        private synchronized Service(Parcel in) {
            super(in);
            this.info = (ServiceInfo) in.readParcelable(Object.class.getClassLoader());
            Iterator it = this.intents.iterator();
            while (it.hasNext()) {
                ServiceIntentInfo aii = (ServiceIntentInfo) it.next();
                aii.service = this;
                this.order = Math.max(aii.getOrder(), this.order);
            }
            if (this.info.permission != null) {
                this.info.permission = this.info.permission.intern();
            }
        }
    }

    private protected static final ServiceInfo generateServiceInfo(Service s, int flags, PackageUserState state, int userId) {
        if (s == null || !checkUseInstalledOrHidden(flags, state, s.owner.applicationInfo)) {
            return null;
        }
        if (!copyNeeded(flags, s.owner, state, s.metaData, userId)) {
            updateApplicationInfo(s.info.applicationInfo, flags, state);
            return s.info;
        }
        ServiceInfo si = new ServiceInfo(s.info);
        si.metaData = s.metaData;
        si.applicationInfo = generateApplicationInfo(s.owner, flags, state, userId);
        return si;
    }

    /* loaded from: classes.dex */
    public static final class Provider extends Component<ProviderIntentInfo> implements Parcelable {
        public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Provider>() { // from class: android.content.pm.PackageParser.Provider.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Provider createFromParcel(Parcel in) {
                return new Provider(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Provider[] newArray(int size) {
                return new Provider[size];
            }
        };
        private protected final ProviderInfo info;
        private protected boolean syncable;

        public synchronized Provider(ParseComponentArgs args, ProviderInfo _info) {
            super(args, (ComponentInfo) _info);
            this.info = _info;
            this.info.applicationInfo = args.owner.applicationInfo;
            this.syncable = false;
        }

        private protected Provider(Provider existingProvider) {
            super(existingProvider);
            this.info = existingProvider.info;
            this.syncable = existingProvider.syncable;
        }

        @Override // android.content.pm.PackageParser.Component
        public synchronized void setPackageName(String packageName) {
            super.setPackageName(packageName);
            this.info.packageName = packageName;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append("Provider{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(' ');
            appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.content.pm.PackageParser.Component, android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(this.info, flags | 2);
            dest.writeInt(this.syncable ? 1 : 0);
        }

        private synchronized Provider(Parcel in) {
            super(in);
            this.info = (ProviderInfo) in.readParcelable(Object.class.getClassLoader());
            this.syncable = in.readInt() == 1;
            Iterator it = this.intents.iterator();
            while (it.hasNext()) {
                ProviderIntentInfo aii = (ProviderIntentInfo) it.next();
                aii.provider = this;
            }
            if (this.info.readPermission != null) {
                this.info.readPermission = this.info.readPermission.intern();
            }
            if (this.info.writePermission != null) {
                this.info.writePermission = this.info.writePermission.intern();
            }
            if (this.info.authority != null) {
                this.info.authority = this.info.authority.intern();
            }
        }
    }

    private protected static final ProviderInfo generateProviderInfo(Provider p, int flags, PackageUserState state, int userId) {
        if (p == null || !checkUseInstalledOrHidden(flags, state, p.owner.applicationInfo)) {
            return null;
        }
        if (!copyNeeded(flags, p.owner, state, p.metaData, userId) && ((flags & 2048) != 0 || p.info.uriPermissionPatterns == null)) {
            updateApplicationInfo(p.info.applicationInfo, flags, state);
            return p.info;
        }
        ProviderInfo pi = new ProviderInfo(p.info);
        pi.metaData = p.metaData;
        if ((flags & 2048) == 0) {
            pi.uriPermissionPatterns = null;
        }
        pi.applicationInfo = generateApplicationInfo(p.owner, flags, state, userId);
        return pi;
    }

    /* loaded from: classes.dex */
    public static final class Instrumentation extends Component<IntentInfo> implements Parcelable {
        public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Instrumentation>() { // from class: android.content.pm.PackageParser.Instrumentation.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Instrumentation createFromParcel(Parcel in) {
                return new Instrumentation(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Instrumentation[] newArray(int size) {
                return new Instrumentation[size];
            }
        };
        private protected final InstrumentationInfo info;

        public synchronized Instrumentation(ParsePackageItemArgs args, InstrumentationInfo _info) {
            super(args, _info);
            this.info = _info;
        }

        @Override // android.content.pm.PackageParser.Component
        public synchronized void setPackageName(String packageName) {
            super.setPackageName(packageName);
            this.info.packageName = packageName;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append("Instrumentation{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(' ');
            appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.content.pm.PackageParser.Component, android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeParcelable(this.info, flags);
        }

        private synchronized Instrumentation(Parcel in) {
            super(in);
            this.info = (InstrumentationInfo) in.readParcelable(Object.class.getClassLoader());
            if (this.info.targetPackage != null) {
                this.info.targetPackage = this.info.targetPackage.intern();
            }
            if (this.info.targetProcesses != null) {
                this.info.targetProcesses = this.info.targetProcesses.intern();
            }
        }
    }

    private protected static final InstrumentationInfo generateInstrumentationInfo(Instrumentation i, int flags) {
        if (i == null) {
            return null;
        }
        if ((flags & 128) == 0) {
            return i.info;
        }
        InstrumentationInfo ii = new InstrumentationInfo(i.info);
        ii.metaData = i.metaData;
        return ii;
    }

    /* loaded from: classes.dex */
    public static abstract class IntentInfo extends IntentFilter {
        private protected int banner;
        private protected boolean hasDefault;
        private protected int icon;
        private protected int labelRes;
        private protected int logo;
        private protected CharSequence nonLocalizedLabel;
        public int preferred;

        public private IntentInfo() {
        }

        protected synchronized IntentInfo(Parcel dest) {
            super(dest);
            this.hasDefault = dest.readInt() == 1;
            this.labelRes = dest.readInt();
            this.nonLocalizedLabel = dest.readCharSequence();
            this.icon = dest.readInt();
            this.logo = dest.readInt();
            this.banner = dest.readInt();
            this.preferred = dest.readInt();
        }

        public synchronized void writeIntentInfoToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.hasDefault ? 1 : 0);
            dest.writeInt(this.labelRes);
            dest.writeCharSequence(this.nonLocalizedLabel);
            dest.writeInt(this.icon);
            dest.writeInt(this.logo);
            dest.writeInt(this.banner);
            dest.writeInt(this.preferred);
        }
    }

    /* loaded from: classes.dex */
    public static final class ActivityIntentInfo extends IntentInfo {
        private protected Activity activity;

        public synchronized ActivityIntentInfo(Activity _activity) {
            this.activity = _activity;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append("ActivityIntentInfo{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(' ');
            this.activity.appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }

        public synchronized ActivityIntentInfo(Parcel in) {
            super(in);
        }
    }

    /* loaded from: classes.dex */
    public static final class ServiceIntentInfo extends IntentInfo {
        private protected Service service;

        public synchronized ServiceIntentInfo(Service _service) {
            this.service = _service;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append("ServiceIntentInfo{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(' ');
            this.service.appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }

        public synchronized ServiceIntentInfo(Parcel in) {
            super(in);
        }
    }

    /* loaded from: classes.dex */
    public static final class ProviderIntentInfo extends IntentInfo {
        private protected Provider provider;

        public synchronized ProviderIntentInfo(Provider provider) {
            this.provider = provider;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append("ProviderIntentInfo{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(' ');
            this.provider.appendComponentShortName(sb);
            sb.append('}');
            return sb.toString();
        }

        public synchronized ProviderIntentInfo(Parcel in) {
            super(in);
        }
    }

    private protected static void setCompatibilityModeEnabled(boolean compatibilityModeEnabled) {
        sCompatibilityModeEnabled = compatibilityModeEnabled;
    }

    /* loaded from: classes.dex */
    public static class PackageParserException extends Exception {
        public final int error;

        public synchronized PackageParserException(int error, String detailMessage) {
            super(detailMessage);
            this.error = error;
        }

        public synchronized PackageParserException(int error, String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
            this.error = error;
        }
    }
}
