package android.content.pm;

import android.Manifest;
import android.annotation.UnsupportedAppUsage;
import android.apex.ApexInfo;
import android.app.ActivityTaskManager;
import android.app.ActivityThread;
import android.app.ResourcesManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageParserCacheHelper;
import android.content.pm.permission.SplitPermissionInfoParcelable;
import android.content.pm.split.DefaultSplitAssetLoader;
import android.content.pm.split.SplitAssetDependencyLoader;
import android.content.pm.split.SplitAssetLoader;
import android.content.pm.split.SplitDependencyLoader;
import android.content.res.ApkAssets;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.media.TtmlUtils;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.IncidentManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PatternMatcher;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.permission.PermissionManager;
import android.provider.SettingsStringUtil;
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
import android.util.Log;
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
import com.android.server.SystemConfig;
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
    private static final int DEFAULT_MIN_SDK_VERSION = 1;
    private static final float DEFAULT_PRE_O_MAX_ASPECT_RATIO = 1.86f;
    private static final float DEFAULT_PRE_Q_MIN_ASPECT_RATIO = 1.333f;
    private static final float DEFAULT_PRE_Q_MIN_ASPECT_RATIO_WATCH = 1.0f;
    private static final int DEFAULT_TARGET_SDK_VERSION = 0;
    private static final boolean LOG_PARSE_TIMINGS = Build.IS_DEBUGGABLE;
    private static final int LOG_PARSE_TIMINGS_THRESHOLD_MS = 100;
    private static final boolean LOG_UNSAFE_BROADCASTS = false;
    private static final String METADATA_MAX_ASPECT_RATIO = "android.max_aspect";
    private static final String MNT_EXPAND = "/mnt/expand/";
    private static final boolean MULTI_PACKAGE_APK_ENABLED;
    @UnsupportedAppUsage
    public static final NewPermissionInfo[] NEW_PERMISSIONS;
    public static final int PARSE_CHATTY = Integer.MIN_VALUE;
    public static final int PARSE_COLLECT_CERTIFICATES = 32;
    private static final int PARSE_DEFAULT_INSTALL_LOCATION = -1;
    private static final int PARSE_DEFAULT_TARGET_SANDBOX = 1;
    public static final int PARSE_ENFORCE_CODE = 64;
    public static final int PARSE_EXTERNAL_STORAGE = 8;
    public static final int PARSE_IGNORE_PROCESSES = 2;
    public static final int PARSE_IS_SYSTEM_DIR = 16;
    public static final int PARSE_MUST_BE_APK = 1;
    private static final String PROPERTY_CHILD_PACKAGES_ENABLED = "persist.sys.child_packages_enabled";
    private static final int RECREATE_ON_CONFIG_CHANGES_MASK = 3;
    private static final boolean RIGID_PARSER = false;
    private static final Set<String> SAFE_BROADCASTS;
    private static final String[] SDK_CODENAMES;
    private static final int SDK_VERSION;
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
    private static boolean sUseRoundIcon;
    @Deprecated
    private String mArchiveSourcePath;
    private File mCacheDir;
    @UnsupportedAppUsage
    private Callback mCallback;
    private boolean mOnlyCoreApps;
    private ParsePackageItemArgs mParseInstrumentationArgs;
    private String[] mSeparateProcesses;
    private int mParseError = 1;
    private DisplayMetrics mMetrics = new DisplayMetrics();

    /* loaded from: classes.dex */
    public interface Callback {
        String[] getOverlayApks(String str);

        String[] getOverlayPaths(String str, String str2);

        boolean hasFeature(String str);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface ParseFlags {
    }

    static {
        MULTI_PACKAGE_APK_ENABLED = Build.IS_DEBUGGABLE && SystemProperties.getBoolean(PROPERTY_CHILD_PACKAGES_ENABLED, false);
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
        SDK_VERSION = Build.VERSION.SDK_INT;
        SDK_CODENAMES = Build.VERSION.ACTIVE_CODENAMES;
        sCompatibilityModeEnabled = true;
        sUseRoundIcon = false;
        sSplitNameComparator = new SplitNameComparator();
    }

    /* loaded from: classes.dex */
    public static class NewPermissionInfo {
        public final int fileVersion;
        @UnsupportedAppUsage
        public final String name;
        @UnsupportedAppUsage
        public final int sdkVersion;

        public NewPermissionInfo(String name, int sdkVersion, int fileVersion) {
            this.name = name;
            this.sdkVersion = sdkVersion;
            this.fileVersion = fileVersion;
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

        ParsePackageItemArgs(Package _owner, String[] _outError, int _nameRes, int _labelRes, int _iconRes, int _roundIconRes, int _logoRes, int _bannerRes) {
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

        public ParseComponentArgs(Package _owner, String[] _outError, int _nameRes, int _labelRes, int _iconRes, int _roundIconRes, int _logoRes, int _bannerRes, String[] _sepProcesses, int _processRes, int _descriptionRes, int _enabledRes) {
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
        @UnsupportedAppUsage
        public final int installLocation;
        public final boolean[] isFeatureSplits;
        public final boolean isolatedSplits;
        public final boolean multiArch;
        @UnsupportedAppUsage
        public final String packageName;
        public final String[] splitCodePaths;
        public final String[] splitNames;
        public final int[] splitRevisionCodes;
        public final boolean use32bitAbi;
        public final String[] usesSplitNames;
        public final VerifierInfo[] verifiers;
        public final int versionCode;
        public final int versionCodeMajor;

        public PackageLite(String codePath, ApkLite baseApk, String[] splitNames, boolean[] isFeatureSplits, String[] usesSplitNames, String[] configForSplit, String[] splitCodePaths, int[] splitRevisionCodes) {
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

        public List<String> getAllCodePaths() {
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
        public final boolean isSplitRequired;
        public final boolean isolatedSplits;
        public final int minSdkVersion;
        public final boolean multiArch;
        public final String packageName;
        public final int revisionCode;
        public final SigningDetails signingDetails;
        public final String splitName;
        public final int targetSdkVersion;
        public final boolean use32bitAbi;
        public final boolean useEmbeddedDex;
        public final String usesSplitName;
        public final VerifierInfo[] verifiers;
        public final int versionCode;
        public final int versionCodeMajor;

        public ApkLite(String codePath, String packageName, String splitName, boolean isFeatureSplit, String configForSplit, String usesSplitName, boolean isSplitRequired, int versionCode, int versionCodeMajor, int revisionCode, int installLocation, List<VerifierInfo> verifiers, SigningDetails signingDetails, boolean coreApp, boolean debuggable, boolean multiArch, boolean use32bitAbi, boolean useEmbeddedDex, boolean extractNativeLibs, boolean isolatedSplits, int minSdkVersion, int targetSdkVersion) {
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
            this.useEmbeddedDex = useEmbeddedDex;
            this.extractNativeLibs = extractNativeLibs;
            this.isolatedSplits = isolatedSplits;
            this.isSplitRequired = isSplitRequired;
            this.minSdkVersion = minSdkVersion;
            this.targetSdkVersion = targetSdkVersion;
        }

        public long getLongVersionCode() {
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

        private CachedComponentArgs() {
        }
    }

    @UnsupportedAppUsage
    public PackageParser() {
        this.mMetrics.setToDefaults();
    }

    @UnsupportedAppUsage
    public void setSeparateProcesses(String[] procs) {
        this.mSeparateProcesses = procs;
    }

    public void setOnlyCoreApps(boolean onlyCoreApps) {
        this.mOnlyCoreApps = onlyCoreApps;
    }

    public void setDisplayMetrics(DisplayMetrics metrics) {
        this.mMetrics = metrics;
    }

    public void setCacheDir(File cacheDir) {
        this.mCacheDir = cacheDir;
    }

    /* loaded from: classes.dex */
    public static final class CallbackImpl implements Callback {
        private final PackageManager mPm;

        public CallbackImpl(PackageManager pm) {
            this.mPm = pm;
        }

        @Override // android.content.pm.PackageParser.Callback
        public boolean hasFeature(String feature) {
            return this.mPm.hasSystemFeature(feature);
        }

        @Override // android.content.pm.PackageParser.Callback
        public String[] getOverlayPaths(String targetPackageName, String targetPath) {
            return null;
        }

        @Override // android.content.pm.PackageParser.Callback
        public String[] getOverlayApks(String targetPackageName) {
            return null;
        }
    }

    public void setCallback(Callback cb) {
        this.mCallback = cb;
    }

    public static final boolean isApkFile(File file) {
        return isApkPath(file.getName());
    }

    public static boolean isApkPath(String path) {
        return path.endsWith(APK_FILE_EXTENSION);
    }

    @UnsupportedAppUsage
    public static PackageInfo generatePackageInfo(Package p, int[] gids, int flags, long firstInstallTime, long lastUpdateTime, Set<String> grantedPermissions, PackageUserState state) {
        return generatePackageInfo(p, gids, flags, firstInstallTime, lastUpdateTime, grantedPermissions, state, UserHandle.getCallingUserId());
    }

    private static boolean checkUseInstalledOrHidden(int flags, PackageUserState state, ApplicationInfo appInfo) {
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

    public static boolean isAvailable(PackageUserState state) {
        return checkUseInstalledOrHidden(0, state, null);
    }

    @UnsupportedAppUsage
    public static PackageInfo generatePackageInfo(Package p, int[] gids, int flags, long firstInstallTime, long lastUpdateTime, Set<String> grantedPermissions, PackageUserState state, int userId) {
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
            pi.targetOverlayableName = p.mOverlayTargetName;
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
                    if (state.isMatch(a.info, flags) && !PackageManager.APP_DETAILS_ACTIVITY_CLASS_NAME.equals(a.className)) {
                        res[num] = generateActivityInfo(a, flags, state, userId);
                        num++;
                    }
                    num2++;
                    N5 = N10;
                }
                pi.activities = (ActivityInfo[]) ArrayUtils.trimToSize(res, num);
            }
            int N11 = flags & 2;
            if (N11 != 0 && (N4 = p.receivers.size()) > 0) {
                int num3 = 0;
                ActivityInfo[] res2 = new ActivityInfo[N4];
                for (int i = 0; i < N4; i++) {
                    Activity a2 = p.receivers.get(i);
                    if (state.isMatch(a2.info, flags)) {
                        res2[num3] = generateActivityInfo(a2, flags, state, userId);
                        num3++;
                    }
                }
                pi.receivers = (ActivityInfo[]) ArrayUtils.trimToSize(res2, num3);
            }
            int N12 = flags & 4;
            if (N12 != 0 && (N3 = p.services.size()) > 0) {
                int num4 = 0;
                ServiceInfo[] res3 = new ServiceInfo[N3];
                for (int i2 = 0; i2 < N3; i2++) {
                    Service s = p.services.get(i2);
                    if (state.isMatch(s.info, flags)) {
                        res3[num4] = generateServiceInfo(s, flags, state, userId);
                        num4++;
                    }
                }
                pi.services = (ServiceInfo[]) ArrayUtils.trimToSize(res3, num4);
            }
            int N13 = flags & 8;
            if (N13 != 0 && (N2 = p.providers.size()) > 0) {
                int num5 = 0;
                ProviderInfo[] res4 = new ProviderInfo[N2];
                for (int i3 = 0; i3 < N2; i3++) {
                    Provider pr = p.providers.get(i3);
                    if (state.isMatch(pr.info, flags)) {
                        res4[num5] = generateProviderInfo(pr, flags, state, userId);
                        num5++;
                    }
                }
                pi.providers = (ProviderInfo[]) ArrayUtils.trimToSize(res4, num5);
            }
            int N14 = flags & 16;
            if (N14 != 0 && (N = p.instrumentation.size()) > 0) {
                pi.instrumentation = new InstrumentationInfo[N];
                for (int i4 = 0; i4 < N; i4++) {
                    pi.instrumentation[i4] = generateInstrumentationInfo(p.instrumentation.get(i4), flags);
                }
            }
            int N15 = flags & 4096;
            if (N15 != 0) {
                int N16 = p.permissions.size();
                if (N16 > 0) {
                    pi.permissions = new PermissionInfo[N16];
                    for (int i5 = 0; i5 < N16; i5++) {
                        pi.permissions[i5] = generatePermissionInfo(p.permissions.get(i5), flags);
                    }
                }
                int N17 = p.requestedPermissions.size();
                if (N17 > 0) {
                    pi.requestedPermissions = new String[N17];
                    pi.requestedPermissionsFlags = new int[N17];
                    for (int i6 = 0; i6 < N17; i6++) {
                        String perm = p.requestedPermissions.get(i6);
                        pi.requestedPermissions[i6] = perm;
                        int[] iArr = pi.requestedPermissionsFlags;
                        iArr[i6] = iArr[i6] | 1;
                        if (grantedPermissions != null && grantedPermissions.contains(perm)) {
                            int[] iArr2 = pi.requestedPermissionsFlags;
                            iArr2[i6] = iArr2[i6] | 2;
                        }
                    }
                }
            }
            int N18 = flags & 64;
            if (N18 != 0) {
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
        private SplitNameComparator() {
        }

        @Override // java.util.Comparator
        public int compare(String lhs, String rhs) {
            if (lhs == null) {
                return -1;
            }
            if (rhs == null) {
                return 1;
            }
            return lhs.compareTo(rhs);
        }
    }

    @UnsupportedAppUsage
    public static PackageLite parsePackageLite(File packageFile, int flags) throws PackageParserException {
        if (packageFile.isDirectory()) {
            return parseClusterPackageLite(packageFile, flags);
        }
        return parseMonolithicPackageLite(packageFile, flags);
    }

    private static PackageLite parseMonolithicPackageLite(File packageFile, int flags) throws PackageParserException {
        Trace.traceBegin(262144L, "parseApkLite");
        ApkLite baseApk = parseApkLite(packageFile, flags);
        String packagePath = packageFile.getAbsolutePath();
        Trace.traceEnd(262144L);
        return new PackageLite(packagePath, baseApk, null, null, null, null, null, null);
    }

    static PackageLite parseClusterPackageLite(File packageDir, int flags) throws PackageParserException {
        String[] splitCodePaths;
        int[] splitRevisionCodes;
        File[] files = packageDir.listFiles();
        if (ArrayUtils.isEmpty(files)) {
            throw new PackageParserException(-100, "No packages found in split");
        }
        String packageName = null;
        int versionCode = 0;
        Trace.traceBegin(262144L, "parseApkLite");
        ArrayMap<String, ApkLite> apks = new ArrayMap<>();
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
        if (size <= 0) {
            splitCodePaths = null;
            splitRevisionCodes = null;
        } else {
            String[] splitNames2 = new String[size];
            isFeatureSplits = new boolean[size];
            usesSplitNames = new String[size];
            configForSplits = new String[size];
            String[] splitCodePaths2 = new String[size];
            int[] splitRevisionCodes2 = new int[size];
            splitNames = (String[]) apks.keySet().toArray(splitNames2);
            Arrays.sort(splitNames, sSplitNameComparator);
            for (int i = 0; i < size; i++) {
                ApkLite apk = apks.get(splitNames[i]);
                usesSplitNames[i] = apk.usesSplitName;
                isFeatureSplits[i] = apk.isFeatureSplit;
                configForSplits[i] = apk.configForSplit;
                splitCodePaths2[i] = apk.codePath;
                splitRevisionCodes2[i] = apk.revisionCode;
            }
            splitCodePaths = splitCodePaths2;
            splitRevisionCodes = splitRevisionCodes2;
        }
        String codePath = packageDir.getAbsolutePath();
        return new PackageLite(codePath, baseApk, splitNames, isFeatureSplits, usesSplitNames, configForSplits, splitCodePaths, splitRevisionCodes);
    }

    @UnsupportedAppUsage
    public Package parsePackage(File packageFile, int flags, boolean useCaches) throws PackageParserException {
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

    @UnsupportedAppUsage
    public Package parsePackage(File packageFile, int flags) throws PackageParserException {
        return parsePackage(packageFile, flags, false);
    }

    private String getCacheKey(File packageFile, int flags) {
        return packageFile.getName() + '-' + flags;
    }

    @VisibleForTesting
    protected Package fromCacheEntry(byte[] bytes) {
        return fromCacheEntryStatic(bytes);
    }

    @VisibleForTesting
    public static Package fromCacheEntryStatic(byte[] bytes) {
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
    protected byte[] toCacheEntry(Package pkg) {
        return toCacheEntryStatic(pkg);
    }

    @VisibleForTesting
    public static byte[] toCacheEntryStatic(Package pkg) {
        Parcel p = Parcel.obtain();
        PackageParserCacheHelper.WriteHelper helper = new PackageParserCacheHelper.WriteHelper(p);
        pkg.writeToParcel(p, 0);
        helper.finishAndUninstall();
        byte[] serialized = p.marshall();
        p.recycle();
        return serialized;
    }

    private static boolean isCacheUpToDate(File packageFile, File cacheFile) {
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

    private Package getCachedResult(File packageFile, int flags) {
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

    private void cacheResult(File packageFile, int flags, Package parsed) {
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
                        try {
                            fos.close();
                        } catch (Throwable th3) {
                            th.addSuppressed(th3);
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

    private Package parseClusterPackage(File packageDir, int flags) throws PackageParserException {
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

    @UnsupportedAppUsage
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

    private Package parseBaseApk(File apkFile, AssetManager assets, int flags) throws PackageParserException {
        String volumeUuid;
        XmlResourceParser parser;
        String apkPath = apkFile.getAbsolutePath();
        if (apkPath.startsWith(MNT_EXPAND)) {
            int end = apkPath.indexOf(47, MNT_EXPAND.length());
            String volumeUuid2 = apkPath.substring(MNT_EXPAND.length(), end);
            volumeUuid = volumeUuid2;
        } else {
            volumeUuid = null;
        }
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
                        pkg.setVolumeUuid(volumeUuid);
                        pkg.setApplicationVolumeUuid(volumeUuid);
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
            } catch (PackageParserException e3) {
                throw e3;
            } catch (Exception e4) {
                e = e4;
            }
        } catch (Throwable th2) {
            e = th2;
            parser = null;
        }
    }

    private void parseSplitApk(Package pkg, int splitIndex, AssetManager assets, int flags) throws PackageParserException {
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
                        int i = this.mParseError;
                        throw new PackageParserException(i, apkPath + " (at " + parser2.getPositionDescription() + "): " + outError[0]);
                    } catch (PackageParserException e) {
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
                } catch (Exception e4) {
                    e = e4;
                } catch (Throwable th2) {
                    e = th2;
                    parser = parser2;
                }
            } catch (PackageParserException e5) {
                throw e5;
            } catch (Exception e6) {
                e = e6;
            } catch (Throwable th3) {
                e = th3;
            }
        } catch (Throwable th4) {
            e = th4;
        }
    }

    private Package parseSplitApk(Package pkg, Resources res, XmlResourceParser parser, int flags, int splitIndex, String[] outError) throws XmlPullParserException, IOException, PackageParserException {
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

    public static ArraySet<PublicKey> toSigningKeys(Signature[] signatures) throws CertificateException {
        ArraySet<PublicKey> keys = new ArraySet<>(signatures.length);
        for (Signature signature : signatures) {
            keys.add(signature.getPublicKey());
        }
        return keys;
    }

    @UnsupportedAppUsage
    public static void collectCertificates(Package pkg, boolean skipVerify) throws PackageParserException {
        collectCertificatesInternal(pkg, skipVerify);
        int childCount = pkg.childPackages != null ? pkg.childPackages.size() : 0;
        for (int i = 0; i < childCount; i++) {
            Package childPkg = pkg.childPackages.get(i);
            childPkg.mSigningDetails = pkg.mSigningDetails;
        }
    }

    private static void collectCertificatesInternal(Package pkg, boolean skipVerify) throws PackageParserException {
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

    @UnsupportedAppUsage
    private static void collectCertificates(Package pkg, File apkFile, boolean skipVerify) throws PackageParserException {
        SigningDetails verified;
        String apkPath = apkFile.getAbsolutePath();
        int minSignatureScheme = 1;
        if (pkg.applicationInfo.isStaticSharedLibrary()) {
            minSignatureScheme = 2;
        }
        if (skipVerify) {
            verified = ApkSignatureVerifier.unsafeGetCertsWithoutVerification(apkPath, minSignatureScheme);
        } else {
            verified = ApkSignatureVerifier.verify(apkPath, minSignatureScheme);
        }
        if (pkg.mSigningDetails == SigningDetails.UNKNOWN) {
            pkg.mSigningDetails = verified;
        } else if (!Signature.areExactMatch(pkg.mSigningDetails.signatures, verified.signatures)) {
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES, apkPath + " has mismatched certificates");
        }
    }

    private static AssetManager newConfiguredAssetManager() {
        AssetManager assetManager = new AssetManager();
        assetManager.setConfiguration(0, 0, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, Build.VERSION.RESOURCES_SDK_INT);
        return assetManager;
    }

    public static ApkLite parseApkLite(File apkFile, int flags) throws PackageParserException {
        return parseApkLiteInner(apkFile, null, null, flags);
    }

    public static ApkLite parseApkLite(FileDescriptor fd, String debugPathName, int flags) throws PackageParserException {
        return parseApkLiteInner(null, fd, debugPathName, flags);
    }

    private static ApkLite parseApkLiteInner(File apkFile, FileDescriptor fd, String debugPathName, int flags) throws PackageParserException {
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
                    try {
                        apkAssets2.close();
                    } catch (Throwable th2) {
                    }
                    return parseApkLite;
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
            } catch (IOException e) {
                throw new PackageParserException(-100, "Failed to parse " + apkPath);
            }
        } catch (IOException | RuntimeException | XmlPullParserException e2) {
            Slog.w(TAG, "Failed to parse " + apkPath, e2);
            throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION, "Failed to parse " + apkPath, e2);
        }
    }

    private static String validateName(String name, boolean requireSeparator, boolean requireFilename) {
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

    private static Pair<String, String> parsePackageSplitNames(XmlPullParser parser, AttributeSet attrs) throws IOException, XmlPullParserException, PackageParserException {
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
        if ("android".equals(packageName) || (error = validateName(packageName, true, true)) == null) {
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
        throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME, "Invalid manifest package: " + error);
    }

    private static ApkLite parseApkLite(String codePath, XmlPullParser parser, AttributeSet attrs, SigningDetails signingDetails) throws IOException, XmlPullParserException, PackageParserException {
        int targetSdkVersion;
        int minSdkVersion;
        int targetSdkVersion2;
        int type;
        int searchDepth;
        Pair<String, String> packageSplit = parsePackageSplitNames(parser, attrs);
        int targetSdkVersion3 = 0;
        int minSdkVersion2 = 1;
        String configForSplit = null;
        String usesSplitName = null;
        boolean isSplitRequired = false;
        boolean isSplitRequired2 = false;
        boolean isFeatureSplit = false;
        boolean isolatedSplits = false;
        int revisionCode = 0;
        int revisionCode2 = 0;
        int versionCodeMajor = 0;
        int installLocation = -1;
        int installLocation2 = 0;
        while (true) {
            targetSdkVersion = targetSdkVersion3;
            int targetSdkVersion4 = attrs.getAttributeCount();
            minSdkVersion = minSdkVersion2;
            if (installLocation2 >= targetSdkVersion4) {
                break;
            }
            String attr = attrs.getAttributeName(installLocation2);
            if (attr.equals("installLocation")) {
                installLocation = attrs.getAttributeIntValue(installLocation2, -1);
            } else if (attr.equals("versionCode")) {
                versionCodeMajor = attrs.getAttributeIntValue(installLocation2, 0);
            } else if (attr.equals("versionCodeMajor")) {
                revisionCode2 = attrs.getAttributeIntValue(installLocation2, 0);
            } else if (attr.equals("revisionCode")) {
                revisionCode = attrs.getAttributeIntValue(installLocation2, 0);
            } else if (attr.equals("coreApp")) {
                isolatedSplits = attrs.getAttributeBooleanValue(installLocation2, false);
            } else if (attr.equals("isolatedSplits")) {
                isFeatureSplit = attrs.getAttributeBooleanValue(installLocation2, false);
            } else if (attr.equals("configForSplit")) {
                configForSplit = attrs.getAttributeValue(installLocation2);
            } else if (attr.equals("isFeatureSplit")) {
                isSplitRequired2 = attrs.getAttributeBooleanValue(installLocation2, false);
            } else if (attr.equals("isSplitRequired")) {
                isSplitRequired = attrs.getAttributeBooleanValue(installLocation2, false);
            }
            installLocation2++;
            targetSdkVersion3 = targetSdkVersion;
            minSdkVersion2 = minSdkVersion;
        }
        int i = parser.getDepth();
        int type2 = 1;
        int i2 = i + 1;
        List<VerifierInfo> verifiers = new ArrayList<>();
        boolean extractNativeLibs = true;
        boolean useEmbeddedDex = false;
        boolean multiArch = false;
        boolean use32bitAbi = false;
        int minSdkVersion3 = minSdkVersion;
        boolean debuggable = false;
        int targetSdkVersion5 = targetSdkVersion;
        while (true) {
            targetSdkVersion2 = targetSdkVersion5;
            int type3 = parser.next();
            if (type3 != type2 && ((type = type3) != 3 || parser.getDepth() >= i2)) {
                if (type == 3) {
                    searchDepth = i2;
                } else if (type == 4) {
                    searchDepth = i2;
                } else if (parser.getDepth() != i2) {
                    searchDepth = i2;
                } else {
                    searchDepth = i2;
                    if (TAG_PACKAGE_VERIFIER.equals(parser.getName())) {
                        VerifierInfo verifier = parseVerifier(attrs);
                        if (verifier != null) {
                            verifiers.add(verifier);
                        }
                    } else if (TAG_APPLICATION.equals(parser.getName())) {
                        int i3 = 0;
                        while (i3 < attrs.getAttributeCount()) {
                            String attr2 = attrs.getAttributeName(i3);
                            int type4 = type;
                            if ("debuggable".equals(attr2)) {
                                debuggable = attrs.getAttributeBooleanValue(i3, false);
                            }
                            if ("multiArch".equals(attr2)) {
                                multiArch = attrs.getAttributeBooleanValue(i3, false);
                            }
                            if ("use32bitAbi".equals(attr2)) {
                                use32bitAbi = attrs.getAttributeBooleanValue(i3, false);
                            }
                            if ("extractNativeLibs".equals(attr2)) {
                                extractNativeLibs = attrs.getAttributeBooleanValue(i3, true);
                            }
                            if ("useEmbeddedDex".equals(attr2)) {
                                useEmbeddedDex = attrs.getAttributeBooleanValue(i3, false);
                            }
                            i3++;
                            type = type4;
                        }
                        targetSdkVersion5 = targetSdkVersion2;
                        i2 = searchDepth;
                        type2 = 1;
                    } else if (TAG_USES_SPLIT.equals(parser.getName())) {
                        if (usesSplitName == null) {
                            usesSplitName = attrs.getAttributeValue(ANDROID_RESOURCES, "name");
                            if (usesSplitName == null) {
                                throw new PackageParserException(PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "<uses-split> tag requires 'android:name' attribute");
                            }
                            targetSdkVersion5 = targetSdkVersion2;
                            i2 = searchDepth;
                            type2 = 1;
                        } else {
                            Slog.w(TAG, "Only one <uses-split> permitted. Ignoring others.");
                        }
                    } else if (TAG_USES_SDK.equals(parser.getName())) {
                        int i4 = 0;
                        int targetSdkVersion6 = targetSdkVersion2;
                        while (i4 < attrs.getAttributeCount()) {
                            String attr3 = attrs.getAttributeName(i4);
                            int targetSdkVersion7 = targetSdkVersion6;
                            if ("targetSdkVersion".equals(attr3)) {
                                targetSdkVersion7 = attrs.getAttributeIntValue(i4, 0);
                            }
                            if ("minSdkVersion".equals(attr3)) {
                                minSdkVersion3 = attrs.getAttributeIntValue(i4, 1);
                            }
                            i4++;
                            targetSdkVersion6 = targetSdkVersion7;
                        }
                        type2 = 1;
                        targetSdkVersion5 = targetSdkVersion6;
                        i2 = searchDepth;
                    }
                }
                type2 = 1;
                targetSdkVersion5 = targetSdkVersion2;
                i2 = searchDepth;
            }
        }
        return new ApkLite(codePath, packageSplit.first, packageSplit.second, isSplitRequired2, configForSplit, usesSplitName, isSplitRequired, versionCodeMajor, revisionCode2, revisionCode, installLocation, verifiers, signingDetails, isolatedSplits, debuggable, multiArch, use32bitAbi, useEmbeddedDex, extractNativeLibs, isFeatureSplit, minSdkVersion3, targetSdkVersion2);
    }

    private boolean parseBaseApkChild(Package parentPkg, Resources res, XmlResourceParser parser, int flags, String[] outError) throws XmlPullParserException, IOException {
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

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private Package parseBaseApk(String apkPath, Resources res, XmlResourceParser parser, int flags, String[] outError) throws XmlPullParserException, IOException {
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
            Callback callback = this.mCallback;
            if (callback != null && (overlayPaths = callback.getOverlayPaths(pkgName, apkPath)) != null && overlayPaths.length > 0) {
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

    /* JADX WARN: Code restructure failed: missing block: B:262:0x0857, code lost:
        if (r32 != false) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:264:0x085f, code lost:
        if (r35.instrumentation.size() != 0) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:265:0x0861, code lost:
        r40[0] = "<manifest> does not contain an <application> or <instrumentation>";
        r34.mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_EMPTY;
     */
    /* JADX WARN: Code restructure failed: missing block: B:266:0x086a, code lost:
        r0 = android.content.pm.PackageParser.NEW_PERMISSIONS.length;
        r1 = null;
        r2 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:267:0x086f, code lost:
        if (r2 >= r0) goto L100;
     */
    /* JADX WARN: Code restructure failed: missing block: B:268:0x0871, code lost:
        r3 = android.content.pm.PackageParser.NEW_PERMISSIONS[r2];
     */
    /* JADX WARN: Code restructure failed: missing block: B:269:0x087b, code lost:
        if (r35.applicationInfo.targetSdkVersion < r3.sdkVersion) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:272:0x0886, code lost:
        if (r35.requestedPermissions.contains(r3.name) != false) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:273:0x0888, code lost:
        if (r1 != null) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:274:0x088a, code lost:
        r1 = new java.lang.StringBuilder(128);
        r1.append(r35.packageName);
        r1.append(": compat added ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:275:0x089d, code lost:
        r1.append(' ');
     */
    /* JADX WARN: Code restructure failed: missing block: B:276:0x08a2, code lost:
        r1.append(r3.name);
        r35.requestedPermissions.add(r3.name);
        r35.implicitPermissions.add(r3.name);
     */
    /* JADX WARN: Code restructure failed: missing block: B:277:0x08b5, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:278:0x08b8, code lost:
        if (r1 == null) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:279:0x08ba, code lost:
        android.util.Slog.i(android.content.pm.PackageParser.TAG, r1.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:280:0x08c1, code lost:
        r2 = getSplitPermissions();
        r3 = r2.size();
        r4 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:281:0x08ca, code lost:
        if (r4 >= r3) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:282:0x08cc, code lost:
        r5 = r2.get(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:283:0x08da, code lost:
        if (r35.applicationInfo.targetSdkVersion >= r5.getTargetSdk()) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:285:0x08e6, code lost:
        if (r35.requestedPermissions.contains(r5.getSplitPermission()) != false) goto L54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:287:0x08eb, code lost:
        r12 = r5.getNewPermissions();
        r13 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:289:0x08f4, code lost:
        if (r13 >= r12.size()) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:290:0x08f6, code lost:
        r15 = r12.get(r13);
        r17 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:291:0x0904, code lost:
        if (r35.requestedPermissions.contains(r15) != false) goto L62;
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x0906, code lost:
        r35.requestedPermissions.add(r15);
        r35.implicitPermissions.add(r15);
     */
    /* JADX WARN: Code restructure failed: missing block: B:293:0x0910, code lost:
        r13 = r13 + 1;
        r0 = r17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x091a, code lost:
        r4 = r4 + 1;
        r0 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x0921, code lost:
        if (r8 < 0) goto L99;
     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x0923, code lost:
        if (r8 <= 0) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x092a, code lost:
        if (r35.applicationInfo.targetSdkVersion < 4) goto L73;
     */
    /* JADX WARN: Code restructure failed: missing block: B:302:0x092c, code lost:
        r35.applicationInfo.flags |= 512;
     */
    /* JADX WARN: Code restructure failed: missing block: B:303:0x0934, code lost:
        if (r18 == 0) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:304:0x0936, code lost:
        r35.applicationInfo.flags |= 1024;
     */
    /* JADX WARN: Code restructure failed: missing block: B:305:0x093e, code lost:
        if (r14 < 0) goto L98;
     */
    /* JADX WARN: Code restructure failed: missing block: B:306:0x0940, code lost:
        if (r14 <= 0) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:308:0x0947, code lost:
        if (r35.applicationInfo.targetSdkVersion < 4) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x0949, code lost:
        r35.applicationInfo.flags |= 2048;
     */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x0951, code lost:
        if (r22 < 0) goto L97;
     */
    /* JADX WARN: Code restructure failed: missing block: B:311:0x0953, code lost:
        if (r22 <= 0) goto L83;
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x095b, code lost:
        if (r35.applicationInfo.targetSdkVersion < 9) goto L83;
     */
    /* JADX WARN: Code restructure failed: missing block: B:314:0x095d, code lost:
        r35.applicationInfo.flags |= 524288;
     */
    /* JADX WARN: Code restructure failed: missing block: B:315:0x0966, code lost:
        if (r23 < 0) goto L96;
     */
    /* JADX WARN: Code restructure failed: missing block: B:316:0x0968, code lost:
        if (r23 <= 0) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:318:0x096f, code lost:
        if (r35.applicationInfo.targetSdkVersion < 4) goto L87;
     */
    /* JADX WARN: Code restructure failed: missing block: B:319:0x0971, code lost:
        r35.applicationInfo.flags |= 4096;
     */
    /* JADX WARN: Code restructure failed: missing block: B:320:0x0979, code lost:
        if (r28 < 0) goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:321:0x097b, code lost:
        if (r28 <= 0) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x0982, code lost:
        if (r35.applicationInfo.targetSdkVersion < 4) goto L91;
     */
    /* JADX WARN: Code restructure failed: missing block: B:324:0x0984, code lost:
        r35.applicationInfo.flags |= 8192;
     */
    /* JADX WARN: Code restructure failed: missing block: B:326:0x0992, code lost:
        if (r35.applicationInfo.usesCompatibilityMode() == false) goto L94;
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x0994, code lost:
        adjustPackageToBeUnresizeableAndUnpipable(r35);
     */
    /* JADX WARN: Code restructure failed: missing block: B:328:0x0997, code lost:
        return r35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x027d, code lost:
        r40[0] = "<overlay> priority must be between 0 and 9999";
        r34.mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0285, code lost:
        return null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private android.content.pm.PackageParser.Package parseBaseApkCommon(android.content.pm.PackageParser.Package r35, java.util.Set<java.lang.String> r36, android.content.res.Resources r37, android.content.res.XmlResourceParser r38, int r39, java.lang.String[] r40) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 2456
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.parseBaseApkCommon(android.content.pm.PackageParser$Package, java.util.Set, android.content.res.Resources, android.content.res.XmlResourceParser, int, java.lang.String[]):android.content.pm.PackageParser$Package");
    }

    private List<SplitPermissionInfoParcelable> getSplitPermissions() {
        if (ActivityThread.isSystem()) {
            return PermissionManager.splitPermissionInfoListToParcelableList(SystemConfig.getInstance().getSplitPermissions());
        }
        try {
            return ActivityThread.getPackageManager().getSplitPermissions();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private boolean checkOverlayRequiredSystemProperty(String propName, String propValue) {
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

    private void adjustPackageToBeUnresizeableAndUnpipable(Package pkg) {
        Iterator<Activity> it = pkg.activities.iterator();
        while (it.hasNext()) {
            Activity a = it.next();
            a.info.resizeMode = 0;
            a.info.flags &= -4194305;
        }
    }

    private static boolean matchTargetCode(String[] codeNames, String targetCode) {
        String targetCodeName;
        int targetCodeIdx = targetCode.indexOf(46);
        if (targetCodeIdx == -1) {
            targetCodeName = targetCode;
        } else {
            targetCodeName = targetCode.substring(0, targetCodeIdx);
        }
        return ArrayUtils.contains(codeNames, targetCodeName);
    }

    public static int computeTargetSdkVersion(int targetVers, String targetCode, String[] platformSdkCodenames, String[] outError) {
        if (targetCode == null) {
            return targetVers;
        }
        if (matchTargetCode(platformSdkCodenames, targetCode)) {
            return 10000;
        }
        if (platformSdkCodenames.length > 0) {
            outError[0] = "Requires development platform " + targetCode + " (current platform is any of " + Arrays.toString(platformSdkCodenames) + ")";
            return -1;
        }
        outError[0] = "Requires development platform " + targetCode + " but this is a release platform.";
        return -1;
    }

    public static int computeMinSdkVersion(int minVers, String minCode, int platformSdkVersion, String[] platformSdkCodenames, String[] outError) {
        if (minCode == null) {
            if (minVers <= platformSdkVersion) {
                return minVers;
            }
            outError[0] = "Requires newer sdk version #" + minVers + " (current version is #" + platformSdkVersion + ")";
            return -1;
        } else if (matchTargetCode(platformSdkCodenames, minCode)) {
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

    private FeatureInfo parseUsesFeature(Resources res, AttributeSet attrs) {
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

    private boolean parseUsesStaticLibrary(Package pkg, Resources res, XmlResourceParser parser, String[] outError) throws XmlPullParserException, IOException {
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
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String[] parseAdditionalCertificates(android.content.res.Resources r10, android.content.res.XmlResourceParser r11, java.lang.String[] r12) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
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

    private boolean parseUsesPermission(Package pkg, Resources res, XmlResourceParser parser) throws XmlPullParserException, IOException {
        Callback callback;
        Callback callback2;
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
        if (requiredFeature != null && (callback2 = this.mCallback) != null && !callback2.hasFeature(requiredFeature)) {
            return true;
        }
        if (requiredNotfeature != null && (callback = this.mCallback) != null && callback.hasFeature(requiredNotfeature)) {
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

    private static String buildClassName(String pkg, CharSequence clsSeq, String[] outError) {
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

    private static String buildCompoundName(String pkg, CharSequence procSeq, String type, String[] outError) {
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
        if (nameError2 != null && !"system".equals(proc)) {
            outError[0] = "Invalid " + type + " name " + proc + " in package " + pkg + ": " + nameError2;
            return null;
        }
        return proc;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String buildProcessName(String pkg, String defProc, CharSequence procSeq, int flags, String[] separateProcesses, String[] outError) {
        if ((flags & 2) != 0 && !"system".equals(procSeq)) {
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

    private static String buildTaskAffinityName(String pkg, String defProc, CharSequence procSeq, String[] outError) {
        if (procSeq == null) {
            return defProc;
        }
        if (procSeq.length() <= 0) {
            return null;
        }
        return buildCompoundName(pkg, procSeq, "taskAffinity", outError);
    }

    /* JADX WARN: Code restructure failed: missing block: B:56:0x0201, code lost:
        r4 = r7.keySet();
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x020f, code lost:
        if (r4.removeAll(r9.keySet()) == false) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0211, code lost:
        r26[0] = "Package" + r23.packageName + " AndroidManifext.xml 'key-set' and 'public-key' names must be distinct.";
        r22.mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x022e, code lost:
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x022f, code lost:
        r23.mKeySetMapping = new android.util.ArrayMap<>();
        r5 = r9.entrySet().iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x0242, code lost:
        if (r5.hasNext() == false) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0244, code lost:
        r12 = r5.next();
        r13 = r12.getKey();
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x025c, code lost:
        if (r12.getValue().size() != 0) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x025e, code lost:
        android.util.Slog.w(android.content.pm.PackageParser.TAG, "Package" + r23.packageName + " AndroidManifext.xml 'key-set' " + r13 + " has no valid associated 'public-key'. Not including in package's defined key-sets.");
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0286, code lost:
        if (r10.contains(r13) == false) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0288, code lost:
        android.util.Slog.w(android.content.pm.PackageParser.TAG, "Package" + r23.packageName + " AndroidManifext.xml 'key-set' " + r13 + " contained improper 'public-key' tags. Not including in package's defined key-sets.");
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x02ac, code lost:
        r23.mKeySetMapping.put(r13, new android.util.ArraySet<>());
        r2 = r12.getValue().iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x02c4, code lost:
        if (r2.hasNext() == false) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x02c6, code lost:
        r3 = r2.next();
        r23.mKeySetMapping.get(r13).add(r7.get(r3));
        r2 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x02f4, code lost:
        if (r23.mKeySetMapping.keySet().containsAll(r8) == false) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x02f6, code lost:
        r23.mUpgradeKeySets = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x02f9, code lost:
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x02fa, code lost:
        r26[0] = "Package" + r23.packageName + " AndroidManifext.xml does not define all 'upgrade-key-set's .";
        r22.mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x0317, code lost:
        return false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean parseKeySets(android.content.pm.PackageParser.Package r23, android.content.res.Resources r24, android.content.res.XmlResourceParser r25, java.lang.String[] r26) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 792
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.parseKeySets(android.content.pm.PackageParser$Package, android.content.res.Resources, android.content.res.XmlResourceParser, java.lang.String[]):boolean");
    }

    private boolean parsePermissionGroup(Package owner, int flags, Resources res, XmlResourceParser parser, String[] outError) throws XmlPullParserException, IOException {
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestPermissionGroup);
        int requestDetailResourceId = sa.getResourceId(12, 0);
        int backgroundRequestResourceId = sa.getResourceId(9, 0);
        int backgroundRequestDetailResourceId = sa.getResourceId(10, 0);
        PermissionGroup perm = new PermissionGroup(owner, requestDetailResourceId, backgroundRequestResourceId, backgroundRequestDetailResourceId);
        if (!parsePackageItemInfo(owner, perm.info, outError, "<permission-group>", sa, true, 2, 0, 1, 8, 5, 7)) {
            sa.recycle();
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        perm.info.descriptionRes = sa.getResourceId(4, 0);
        perm.info.requestRes = sa.getResourceId(11, 0);
        perm.info.flags = sa.getInt(6, 0);
        perm.info.priority = sa.getInt(3, 0);
        sa.recycle();
        if (parseAllMetaData(res, parser, "<permission-group>", perm, outError)) {
            owner.permissionGroups.add(perm);
            return true;
        }
        this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x006c  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0072  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean parsePermission(android.content.pm.PackageParser.Package r22, android.content.res.Resources r23, android.content.res.XmlResourceParser r24, java.lang.String[] r25) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 359
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.parsePermission(android.content.pm.PackageParser$Package, android.content.res.Resources, android.content.res.XmlResourceParser, java.lang.String[]):boolean");
    }

    private boolean parsePermissionTree(Package owner, Resources res, XmlResourceParser parser, String[] outError) throws XmlPullParserException, IOException {
        int index;
        Permission perm = new Permission(owner, (String) null);
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestPermissionTree);
        if (!parsePackageItemInfo(owner, perm.info, outError, "<permission-tree>", sa, true, 2, 0, 1, 5, 3, 4)) {
            sa.recycle();
            this.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
        sa.recycle();
        int index2 = perm.info.name.indexOf(46);
        if (index2 <= 0) {
            index = index2;
        } else {
            index = perm.info.name.indexOf(46, index2 + 1);
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

    private Instrumentation parseInstrumentation(Package owner, Resources res, XmlResourceParser parser, String[] outError) throws XmlPullParserException, IOException {
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestInstrumentation);
        if (this.mParseInstrumentationArgs == null) {
            this.mParseInstrumentationArgs = new ParsePackageItemArgs(owner, outError, 2, 0, 1, 8, 6, 7);
            this.mParseInstrumentationArgs.tag = "<instrumentation>";
        }
        ParsePackageItemArgs parsePackageItemArgs = this.mParseInstrumentationArgs;
        parsePackageItemArgs.sa = sa;
        Instrumentation a = new Instrumentation(parsePackageItemArgs, new InstrumentationInfo());
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

    /* JADX WARN: Code restructure failed: missing block: B:278:0x0666, code lost:
        r9[0] = "Bad static-library declaration name: " + r10 + " version: " + r13;
        r0.mParseError = android.content.pm.PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
        com.android.internal.util.XmlUtils.skipCurrentTag(r40);
     */
    /* JADX WARN: Code restructure failed: missing block: B:279:0x0688, code lost:
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x07a6, code lost:
        if (android.text.TextUtils.isEmpty(r14.staticSharedLibName) == false) goto L195;
     */
    /* JADX WARN: Code restructure failed: missing block: B:318:0x07a8, code lost:
        r14.activities.add(r0.generateAppDetailsHiddenActivity(r14, r41, r9, r14.baseHardwareAccelerated));
     */
    /* JADX WARN: Code restructure failed: missing block: B:320:0x07b8, code lost:
        if (r26 == 0) goto L185;
     */
    /* JADX WARN: Code restructure failed: missing block: B:321:0x07ba, code lost:
        java.util.Collections.sort(r14.activities, android.content.pm.$$Lambda$PackageParser$0aobsT7Zf7WVZCqMZ5z2clAuQf4.INSTANCE);
     */
    /* JADX WARN: Code restructure failed: missing block: B:322:0x07c1, code lost:
        if (r27 == false) goto L187;
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x07c3, code lost:
        java.util.Collections.sort(r14.receivers, android.content.pm.$$Lambda$PackageParser$0DZRgzfgaIMpCOhJqjw6PUiU5vw.INSTANCE);
     */
    /* JADX WARN: Code restructure failed: missing block: B:324:0x07ca, code lost:
        if (r28 == false) goto L189;
     */
    /* JADX WARN: Code restructure failed: missing block: B:325:0x07cc, code lost:
        java.util.Collections.sort(r14.services, android.content.pm.$$Lambda$PackageParser$M9fHqS_eEp1oYkuKJhRHOGUxf8.INSTANCE);
     */
    /* JADX WARN: Code restructure failed: missing block: B:326:0x07d3, code lost:
        setMaxAspectRatio(r38);
        setMinAspectRatio(r38);
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x07dd, code lost:
        if (hasDomainURLs(r38) == false) goto L193;
     */
    /* JADX WARN: Code restructure failed: missing block: B:328:0x07df, code lost:
        r14.applicationInfo.privateFlags |= 16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:329:0x07e8, code lost:
        r14.applicationInfo.privateFlags &= -17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:330:0x07f0, code lost:
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:363:?, code lost:
        return true;
     */
    @android.annotation.UnsupportedAppUsage
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean parseBaseApplication(android.content.pm.PackageParser.Package r38, android.content.res.Resources r39, android.content.res.XmlResourceParser r40, int r41, java.lang.String[] r42) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 2034
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.parseBaseApplication(android.content.pm.PackageParser$Package, android.content.res.Resources, android.content.res.XmlResourceParser, int, java.lang.String[]):boolean");
    }

    private static boolean hasDomainURLs(Package pkg) {
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

    private boolean parseSplitApplication(Package owner, Resources res, XmlResourceParser parser, int flags, int splitIndex, String[] outError) throws XmlPullParserException, IOException {
        int innerDepth;
        String classLoaderName;
        XmlResourceParser xmlResourceParser;
        Package r1;
        PackageParser packageParser;
        boolean z;
        int i;
        String[] strArr;
        Resources resources;
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
        if (classLoaderName2 == null || ClassLoaderFactory.isValidClassLoaderName(classLoaderName2)) {
            r14.applicationInfo.splitClassLoaderNames[splitIndex] = classLoaderName2;
            int innerDepth2 = parser.getDepth();
            while (true) {
                int type = parser.next();
                if (type != i2) {
                    if (type != 3 || parser.getDepth() > innerDepth2) {
                        if (type == 3) {
                            innerDepth = innerDepth2;
                            classLoaderName = classLoaderName2;
                            xmlResourceParser = xmlResourceParser2;
                            r1 = r14;
                            packageParser = packageParser2;
                            z = z2;
                            i = i4;
                            strArr = strArr2;
                            resources = resources2;
                        } else if (type == i3) {
                            innerDepth = innerDepth2;
                            classLoaderName = classLoaderName2;
                            xmlResourceParser = xmlResourceParser2;
                            r1 = r14;
                            packageParser = packageParser2;
                            z = z2;
                            i = i4;
                            strArr = strArr2;
                            resources = resources2;
                        } else {
                            CachedComponentArgs cachedArgs = new CachedComponentArgs();
                            String tagName = parser.getName();
                            if (tagName.equals(Context.ACTIVITY_SERVICE)) {
                                innerDepth = innerDepth2;
                                int i5 = i4;
                                classLoaderName = classLoaderName2;
                                Activity a = parseActivity(owner, res, parser, flags, outError, cachedArgs, false, r14.baseHardwareAccelerated);
                                if (a == null) {
                                    packageParser2.mParseError = i5;
                                    return false;
                                }
                                r14.activities.add(a);
                                ComponentInfo parsedComponent2 = a.info;
                                resources = res;
                                parsedComponent = parsedComponent2;
                                strArr = strArr2;
                                xmlResourceParser = xmlResourceParser2;
                                packageParser = packageParser2;
                                z = false;
                                r1 = r14;
                                i = i5;
                            } else {
                                innerDepth = innerDepth2;
                                boolean z3 = z2;
                                int i6 = i4;
                                classLoaderName = classLoaderName2;
                                if (tagName.equals(IncidentManager.URI_PARAM_RECEIVER_CLASS)) {
                                    xmlResourceParser = xmlResourceParser2;
                                    resources = res;
                                    r1 = r14;
                                    packageParser = packageParser2;
                                    Activity a2 = parseActivity(owner, res, parser, flags, outError, cachedArgs, true, false);
                                    if (a2 == null) {
                                        packageParser.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                                        return false;
                                    }
                                    i = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
                                    z = false;
                                    r1.receivers.add(a2);
                                    parsedComponent = a2.info;
                                    strArr = outError;
                                } else {
                                    resources = res;
                                    xmlResourceParser = xmlResourceParser2;
                                    packageParser = packageParser2;
                                    z = z3;
                                    r1 = r14;
                                    i = i6;
                                    if (tagName.equals("service")) {
                                        Service s = parseService(owner, res, parser, flags, outError, cachedArgs);
                                        if (s == null) {
                                            packageParser.mParseError = i;
                                            return z;
                                        }
                                        r1.services.add(s);
                                        parsedComponent = s.info;
                                        strArr = outError;
                                    } else if (tagName.equals("provider")) {
                                        Provider p = parseProvider(owner, res, parser, flags, outError, cachedArgs);
                                        if (p == null) {
                                            packageParser.mParseError = i;
                                            return z;
                                        }
                                        r1.providers.add(p);
                                        parsedComponent = p.info;
                                        strArr = outError;
                                    } else if (tagName.equals("activity-alias")) {
                                        Activity a3 = parseActivityAlias(owner, res, parser, flags, outError, cachedArgs);
                                        if (a3 == null) {
                                            packageParser.mParseError = i;
                                            return z;
                                        }
                                        r1.activities.add(a3);
                                        parsedComponent = a3.info;
                                        strArr = outError;
                                    } else {
                                        if (parser.getName().equals("meta-data")) {
                                            strArr = outError;
                                            Bundle parseMetaData = packageParser.parseMetaData(resources, xmlResourceParser, r1.mAppMetaData, strArr);
                                            r1.mAppMetaData = parseMetaData;
                                            if (parseMetaData == null) {
                                                packageParser.mParseError = i;
                                                return z;
                                            }
                                        } else {
                                            strArr = outError;
                                            if (tagName.equals("uses-static-library")) {
                                                if (!packageParser.parseUsesStaticLibrary(r1, resources, xmlResourceParser, strArr)) {
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
                                                        r1.usesLibraries = ArrayUtils.add(r1.usesLibraries, lname2);
                                                        r1.usesOptionalLibraries = ArrayUtils.remove(r1.usesOptionalLibraries, lname2);
                                                    } else if (!ArrayUtils.contains(r1.usesLibraries, lname2)) {
                                                        r1.usesOptionalLibraries = ArrayUtils.add(r1.usesOptionalLibraries, lname2);
                                                    }
                                                }
                                                XmlUtils.skipCurrentTag(parser);
                                                parsedComponent = null;
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
                            }
                            if (parsedComponent != null && parsedComponent.splitName == null) {
                                parsedComponent.splitName = r1.splitNames[splitIndex];
                            }
                            xmlResourceParser2 = xmlResourceParser;
                            resources2 = resources;
                            strArr2 = strArr;
                            i4 = i;
                            z2 = z;
                            classLoaderName2 = classLoaderName;
                            innerDepth2 = innerDepth;
                            i3 = 4;
                            i2 = 1;
                            packageParser2 = packageParser;
                            r14 = r1;
                        }
                        xmlResourceParser2 = xmlResourceParser;
                        resources2 = resources;
                        strArr2 = strArr;
                        i4 = i;
                        z2 = z;
                        classLoaderName2 = classLoaderName;
                        innerDepth2 = innerDepth;
                        i3 = 4;
                        i2 = 1;
                        packageParser2 = packageParser;
                        r14 = r1;
                    } else {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        } else {
            strArr2[0] = "Invalid class loader name: " + classLoaderName2;
            packageParser2.mParseError = PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED;
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean parsePackageItemInfo(Package owner, PackageItemInfo outInfo, String[] outError, String tag, TypedArray sa, boolean nameRequired, int nameRes, int labelRes, int iconRes, int roundIconRes, int logoRes, int bannerRes) {
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
            String outInfoName = buildClassName(owner.applicationInfo.packageName, name, outError);
            if (PackageManager.APP_DETAILS_ACTIVITY_CLASS_NAME.equals(outInfoName)) {
                outError[0] = tag + " invalid android:name";
                return false;
            }
            outInfo.name = outInfoName;
            if (outInfoName == null) {
                return false;
            }
        }
        int roundIconVal = sUseRoundIcon ? sa.getResourceId(roundIconRes, 0) : 0;
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

    private Activity generateAppDetailsHiddenActivity(Package owner, int flags, String[] outError, boolean hardwareAccelerated) {
        Activity a = new Activity(owner, PackageManager.APP_DETAILS_ACTIVITY_CLASS_NAME, new ActivityInfo());
        a.owner = owner;
        a.setPackageName(owner.packageName);
        a.info.theme = 16973909;
        a.info.exported = true;
        a.info.name = PackageManager.APP_DETAILS_ACTIVITY_CLASS_NAME;
        a.info.processName = owner.applicationInfo.processName;
        a.info.uiOptions = a.info.applicationInfo.uiOptions;
        a.info.taskAffinity = buildTaskAffinityName(owner.packageName, owner.packageName, ":app_details", outError);
        a.info.enabled = true;
        a.info.launchMode = 0;
        a.info.documentLaunchMode = 0;
        a.info.maxRecents = ActivityTaskManager.getDefaultAppRecentsLimitStatic();
        a.info.configChanges = getActivityConfigChanges(0, 0);
        a.info.softInputMode = 0;
        a.info.persistableMode = 1;
        a.info.screenOrientation = -1;
        a.info.resizeMode = 4;
        a.info.lockTaskLaunchMode = 0;
        ActivityInfo activityInfo = a.info;
        a.info.directBootAware = false;
        activityInfo.encryptionAware = false;
        a.info.rotationAnimation = -1;
        a.info.colorMode = 0;
        if (hardwareAccelerated) {
            a.info.flags |= 512;
        }
        return a;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Activity parseActivity(Package owner, Resources res, XmlResourceParser parser, int flags, String[] outError, CachedComponentArgs cachedArgs, boolean receiver, boolean hardwareAccelerated) throws XmlPullParserException, IOException {
        int i;
        int i2;
        ActivityIntentInfo intent;
        int visibility;
        ActivityIntentInfo intent2;
        int visibility2;
        char c;
        Package r6 = owner;
        Resources resources = res;
        XmlResourceParser xmlResourceParser = parser;
        String[] strArr = outError;
        TypedArray sa = resources.obtainAttributes(xmlResourceParser, R.styleable.AndroidManifestActivity);
        if (cachedArgs.mActivityArgs == null) {
            cachedArgs.mActivityArgs = new ParseComponentArgs(owner, outError, 3, 1, 2, 44, 23, 30, this.mSeparateProcesses, 7, 17, 5);
        }
        cachedArgs.mActivityArgs.tag = receiver ? "<receiver>" : "<activity>";
        cachedArgs.mActivityArgs.sa = sa;
        cachedArgs.mActivityArgs.flags = flags;
        Activity a = new Activity(cachedArgs.mActivityArgs, new ActivityInfo());
        int i3 = 0;
        if (strArr[0] != null) {
            sa.recycle();
            return null;
        }
        boolean setExported = sa.hasValue(6);
        if (setExported) {
            a.info.exported = sa.getBoolean(6, false);
        }
        a.info.theme = sa.getResourceId(0, 0);
        a.info.uiOptions = sa.getInt(26, a.info.applicationInfo.uiOptions);
        String parentName = sa.getNonConfigurationString(27, 1024);
        if (parentName != null) {
            String parentClassName = buildClassName(a.info.packageName, parentName, strArr);
            if (strArr[0] == null) {
                a.info.parentActivityName = parentClassName;
            } else {
                Log.e(TAG, "Activity " + a.info.name + " specified invalid parentActivityName " + parentName);
                i3 = 0;
                strArr[0] = null;
            }
        }
        String str = sa.getNonConfigurationString(4, i3);
        if (str == null) {
            a.info.permission = r6.applicationInfo.permission;
        } else {
            a.info.permission = str.length() > 0 ? str.toString().intern() : null;
        }
        a.info.taskAffinity = buildTaskAffinityName(r6.applicationInfo.packageName, r6.applicationInfo.taskAffinity, sa.getNonConfigurationString(8, 1024), strArr);
        a.info.splitName = sa.getNonConfigurationString(48, 0);
        a.info.flags = 0;
        if (sa.getBoolean(9, false)) {
            a.info.flags |= 1;
        }
        if (sa.getBoolean(10, false)) {
            a.info.flags |= 2;
        }
        if (sa.getBoolean(11, false)) {
            a.info.flags |= 4;
        }
        if (sa.getBoolean(21, false)) {
            a.info.flags |= 128;
        }
        if (sa.getBoolean(18, false)) {
            a.info.flags |= 8;
        }
        if (sa.getBoolean(12, false)) {
            a.info.flags |= 16;
        }
        if (sa.getBoolean(13, false)) {
            a.info.flags |= 32;
        }
        if (sa.getBoolean(19, (r6.applicationInfo.flags & 32) != 0)) {
            a.info.flags |= 64;
        }
        if (sa.getBoolean(22, false)) {
            a.info.flags |= 256;
        }
        if (sa.getBoolean(29, false) || sa.getBoolean(39, false)) {
            a.info.flags |= 1024;
        }
        if (sa.getBoolean(24, false)) {
            a.info.flags |= 2048;
        }
        if (sa.getBoolean(56, false)) {
            a.info.flags |= 536870912;
        }
        if (!receiver) {
            if (sa.getBoolean(25, hardwareAccelerated)) {
                a.info.flags |= 512;
            }
            a.info.launchMode = sa.getInt(14, 0);
            a.info.documentLaunchMode = sa.getInt(33, 0);
            a.info.maxRecents = sa.getInt(34, ActivityTaskManager.getDefaultAppRecentsLimitStatic());
            a.info.configChanges = getActivityConfigChanges(sa.getInt(16, 0), sa.getInt(47, 0));
            a.info.softInputMode = sa.getInt(20, 0);
            a.info.persistableMode = sa.getInteger(32, 0);
            if (sa.getBoolean(31, false)) {
                a.info.flags |= Integer.MIN_VALUE;
            }
            if (sa.getBoolean(35, false)) {
                a.info.flags |= 8192;
            }
            if (sa.getBoolean(36, false)) {
                a.info.flags |= 4096;
            }
            if (sa.getBoolean(37, false)) {
                a.info.flags |= 16384;
            }
            a.info.screenOrientation = sa.getInt(15, -1);
            setActivityResizeMode(a.info, sa, r6);
            if (sa.getBoolean(41, false)) {
                a.info.flags |= 4194304;
            }
            if (sa.getBoolean(55, false)) {
                a.info.flags |= 262144;
            }
            if (sa.hasValue(50) && sa.getType(50) == 4) {
                a.setMaxAspectRatio(sa.getFloat(50, 0.0f));
            }
            if (sa.hasValue(53) && sa.getType(53) == 4) {
                a.setMinAspectRatio(sa.getFloat(53, 0.0f));
            }
            a.info.lockTaskLaunchMode = sa.getInt(38, 0);
            ActivityInfo activityInfo = a.info;
            ActivityInfo activityInfo2 = a.info;
            boolean z = sa.getBoolean(42, false);
            activityInfo2.directBootAware = z;
            activityInfo.encryptionAware = z;
            a.info.requestedVrComponent = sa.getString(43);
            a.info.rotationAnimation = sa.getInt(46, -1);
            a.info.colorMode = sa.getInt(49, 0);
            if (sa.getBoolean(51, false)) {
                a.info.flags |= 8388608;
            }
            if (sa.getBoolean(52, false)) {
                a.info.flags |= 16777216;
            }
            if (sa.getBoolean(54, false)) {
                a.info.privateFlags |= 1;
            }
        } else {
            a.info.launchMode = 0;
            a.info.configChanges = 0;
            if (sa.getBoolean(28, false)) {
                a.info.flags |= 1073741824;
            }
            ActivityInfo activityInfo3 = a.info;
            ActivityInfo activityInfo4 = a.info;
            boolean z2 = sa.getBoolean(42, false);
            activityInfo4.directBootAware = z2;
            activityInfo3.encryptionAware = z2;
        }
        if (a.info.directBootAware) {
            r6.applicationInfo.privateFlags |= 256;
        }
        boolean visibleToEphemeral = sa.getBoolean(45, false);
        if (visibleToEphemeral) {
            a.info.flags |= 1048576;
            r6.visibleToInstantApps = true;
        }
        sa.recycle();
        if (receiver) {
            i2 = 2;
            if ((r6.applicationInfo.privateFlags & 2) == 0) {
                i = 0;
            } else if (a.info.processName != r6.packageName) {
                i = 0;
            } else {
                i = 0;
                strArr[0] = "Heavy-weight applications can not have receivers in main process";
            }
        } else {
            i = 0;
            i2 = 2;
        }
        if (strArr[i] != null) {
            return null;
        }
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1 && (type != 3 || parser.getDepth() > outerDepth)) {
                if (type == 3) {
                    sa = sa;
                    xmlResourceParser = xmlResourceParser;
                    outerDepth = outerDepth;
                } else if (type != 4) {
                    TypedArray sa2 = sa;
                    if (parser.getName().equals("intent-filter")) {
                        ActivityIntentInfo intent3 = new ActivityIntentInfo(a);
                        int outerDepth2 = outerDepth;
                        String[] strArr2 = strArr;
                        Package r11 = r6;
                        if (!parseIntent(res, parser, true, true, intent3, outError)) {
                            return null;
                        }
                        if (intent3.countActions() == 0) {
                            Slog.w(TAG, "No actions in intent filter at " + this.mArchiveSourcePath + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + parser.getPositionDescription());
                            intent = intent3;
                        } else {
                            a.order = Math.max(intent3.getOrder(), a.order);
                            intent = intent3;
                            a.intents.add(intent);
                        }
                        if (visibleToEphemeral) {
                            visibility = 1;
                        } else if (!receiver && isImplicitlyExposedIntent(intent)) {
                            visibility = i2;
                        } else {
                            visibility = i;
                        }
                        intent.setVisibilityToInstantApp(visibility);
                        if (intent.isVisibleToInstantApp()) {
                            a.info.flags |= 1048576;
                        }
                        if (intent.isImplicitlyVisibleToInstantApp()) {
                            a.info.flags |= 2097152;
                        }
                        resources = res;
                        xmlResourceParser = parser;
                        strArr = strArr2;
                        r6 = r11;
                        sa = sa2;
                        outerDepth = outerDepth2;
                    } else {
                        int outerDepth3 = outerDepth;
                        String[] strArr3 = strArr;
                        Package r112 = r6;
                        if (!receiver && parser.getName().equals("preferred")) {
                            ActivityIntentInfo intent4 = new ActivityIntentInfo(a);
                            if (!parseIntent(res, parser, false, false, intent4, outError)) {
                                return null;
                            }
                            if (intent4.countActions() == 0) {
                                Slog.w(TAG, "No actions in preferred at " + this.mArchiveSourcePath + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + parser.getPositionDescription());
                                intent2 = intent4;
                            } else {
                                if (r112.preferredActivityFilters == null) {
                                    r112.preferredActivityFilters = new ArrayList<>();
                                }
                                intent2 = intent4;
                                r112.preferredActivityFilters.add(intent2);
                            }
                            if (visibleToEphemeral) {
                                visibility2 = 1;
                            } else if (!receiver && isImplicitlyExposedIntent(intent2)) {
                                visibility2 = i2;
                            } else {
                                visibility2 = i;
                            }
                            intent2.setVisibilityToInstantApp(visibility2);
                            if (!intent2.isVisibleToInstantApp()) {
                                c = 0;
                            } else {
                                c = 0;
                                a.info.flags |= 1048576;
                            }
                            if (intent2.isImplicitlyVisibleToInstantApp()) {
                                a.info.flags |= 2097152;
                            }
                            resources = res;
                            strArr = strArr3;
                            r6 = r112;
                            sa = sa2;
                            outerDepth = outerDepth3;
                            xmlResourceParser = parser;
                        } else if (parser.getName().equals("meta-data")) {
                            Bundle parseMetaData = parseMetaData(res, parser, a.metaData, strArr3);
                            a.metaData = parseMetaData;
                            if (parseMetaData == null) {
                                return null;
                            }
                            resources = res;
                            strArr = strArr3;
                            r6 = r112;
                            sa = sa2;
                            xmlResourceParser = parser;
                            outerDepth = outerDepth3;
                        } else if (!receiver && parser.getName().equals(TtmlUtils.TAG_LAYOUT)) {
                            parseLayout(res, parser, a);
                            resources = res;
                            strArr = strArr3;
                            r6 = r112;
                            sa = sa2;
                            xmlResourceParser = parser;
                            outerDepth = outerDepth3;
                        } else {
                            Slog.w(TAG, "Problem in package " + this.mArchiveSourcePath + SettingsStringUtil.DELIMITER);
                            if (receiver) {
                                Slog.w(TAG, "Unknown element under <receiver>: " + parser.getName() + " at " + this.mArchiveSourcePath + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + parser.getPositionDescription());
                            } else {
                                Slog.w(TAG, "Unknown element under <activity>: " + parser.getName() + " at " + this.mArchiveSourcePath + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + parser.getPositionDescription());
                            }
                            XmlUtils.skipCurrentTag(parser);
                            resources = res;
                            strArr = strArr3;
                            r6 = r112;
                            sa = sa2;
                            xmlResourceParser = parser;
                            outerDepth = outerDepth3;
                        }
                    }
                }
            }
        }
        if (!setExported) {
            a.info.exported = a.intents.size() > 0 ? 1 : i;
        }
        return a;
    }

    private void setActivityResizeMode(ActivityInfo aInfo, TypedArray sa, Package owner) {
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

    private void setMaxAspectRatio(Package owner) {
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

    private void setMinAspectRatio(Package owner) {
        float minAspectRatio;
        float f = 0.0f;
        if (owner.applicationInfo.minAspectRatio != 0.0f) {
            minAspectRatio = owner.applicationInfo.minAspectRatio;
        } else {
            if (owner.applicationInfo.targetSdkVersion < 29) {
                Callback callback = this.mCallback;
                if (callback != null && callback.hasFeature(PackageManager.FEATURE_WATCH)) {
                    f = 1.0f;
                } else {
                    f = DEFAULT_PRE_Q_MIN_ASPECT_RATIO;
                }
            }
            minAspectRatio = f;
        }
        Iterator<Activity> it = owner.activities.iterator();
        while (it.hasNext()) {
            Activity activity = it.next();
            if (!activity.hasMinAspectRatio()) {
                activity.setMinAspectRatio(minAspectRatio);
            }
        }
    }

    public static int getActivityConfigChanges(int configChanges, int recreateOnConfigChanges) {
        return ((~recreateOnConfigChanges) & 3) | configChanges;
    }

    private void parseLayout(Resources res, AttributeSet attrs, Activity a) {
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

    /* JADX WARN: Code restructure failed: missing block: B:101:0x03aa, code lost:
        if (r14 != false) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x03ac, code lost:
        r0 = r0.info;
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x03b4, code lost:
        if (r0.intents.size() <= 0) goto L65;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x03b6, code lost:
        r1 = r23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x03b9, code lost:
        r1 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x03bb, code lost:
        r0.exported = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x03bd, code lost:
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private android.content.pm.PackageParser.Activity parseActivityAlias(android.content.pm.PackageParser.Package r32, android.content.res.Resources r33, android.content.res.XmlResourceParser r34, int r35, java.lang.String[] r36, android.content.pm.PackageParser.CachedComponentArgs r37) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 958
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.parseActivityAlias(android.content.pm.PackageParser$Package, android.content.res.Resources, android.content.res.XmlResourceParser, int, java.lang.String[], android.content.pm.PackageParser$CachedComponentArgs):android.content.pm.PackageParser$Activity");
    }

    private Provider parseProvider(Package owner, Resources res, XmlResourceParser parser, int flags, String[] outError, CachedComponentArgs cachedArgs) throws XmlPullParserException, IOException {
        TypedArray sa;
        boolean providerExportedDefault;
        String str;
        TypedArray sa2 = res.obtainAttributes(parser, R.styleable.AndroidManifestProvider);
        if (cachedArgs.mProviderArgs != null) {
            sa = sa2;
        } else {
            sa = sa2;
            cachedArgs.mProviderArgs = new ParseComponentArgs(owner, outError, 2, 0, 1, 19, 15, 17, this.mSeparateProcesses, 8, 14, 6);
            cachedArgs.mProviderArgs.tag = "<provider>";
        }
        TypedArray sa3 = sa;
        cachedArgs.mProviderArgs.sa = sa3;
        cachedArgs.mProviderArgs.flags = flags;
        Provider p = new Provider(cachedArgs.mProviderArgs, new ProviderInfo());
        if (outError[0] != null) {
            sa3.recycle();
            return null;
        }
        if (owner.applicationInfo.targetSdkVersion >= 17) {
            providerExportedDefault = false;
        } else {
            providerExportedDefault = true;
        }
        p.info.exported = sa3.getBoolean(7, providerExportedDefault);
        String cpname = sa3.getNonConfigurationString(10, 0);
        p.info.isSyncable = sa3.getBoolean(11, false);
        String permission = sa3.getNonConfigurationString(3, 0);
        String str2 = sa3.getNonConfigurationString(4, 0);
        if (str2 == null) {
            str2 = permission;
        }
        if (str2 == null) {
            p.info.readPermission = owner.applicationInfo.permission;
        } else {
            p.info.readPermission = str2.length() > 0 ? str2.toString().intern() : null;
        }
        String str3 = sa3.getNonConfigurationString(5, 0);
        if (str3 != null) {
            str = str3;
        } else {
            str = permission;
        }
        if (str == null) {
            p.info.writePermission = owner.applicationInfo.permission;
        } else {
            p.info.writePermission = str.length() > 0 ? str.toString().intern() : null;
        }
        p.info.grantUriPermissions = sa3.getBoolean(13, false);
        p.info.forceUriPermissions = sa3.getBoolean(22, false);
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

    private boolean parseProviderTags(Resources res, XmlResourceParser parser, boolean visibleToEphemeral, Provider outInfo, String[] outError) throws XmlPullParserException, IOException {
        String readPermission;
        String readPermission2;
        String writePermission;
        PathPermission pa;
        PathPermission pa2;
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        if (parser.getName().equals("intent-filter")) {
                            ProviderIntentInfo intent = new ProviderIntentInfo(outInfo);
                            if (!parseIntent(res, parser, true, false, intent, outError)) {
                                return false;
                            }
                            if (visibleToEphemeral) {
                                intent.setVisibilityToInstantApp(1);
                                outInfo.info.flags |= 1048576;
                            }
                            outInfo.order = Math.max(intent.getOrder(), outInfo.order);
                            outInfo.intents.add(intent);
                        } else if (parser.getName().equals("meta-data")) {
                            Bundle parseMetaData = parseMetaData(res, parser, outInfo.metaData, outError);
                            outInfo.metaData = parseMetaData;
                            if (parseMetaData == null) {
                                return false;
                            }
                        } else if (parser.getName().equals("grant-uri-permission")) {
                            TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestGrantUriPermission);
                            PatternMatcher pa3 = null;
                            String str = sa.getNonConfigurationString(0, 0);
                            if (str != null) {
                                pa3 = new PatternMatcher(str, 0);
                            }
                            String str2 = sa.getNonConfigurationString(1, 0);
                            if (str2 != null) {
                                pa3 = new PatternMatcher(str2, 1);
                            }
                            String str3 = sa.getNonConfigurationString(2, 0);
                            if (str3 != null) {
                                pa3 = new PatternMatcher(str3, 2);
                            }
                            sa.recycle();
                            if (pa3 != null) {
                                if (outInfo.info.uriPermissionPatterns == null) {
                                    outInfo.info.uriPermissionPatterns = new PatternMatcher[1];
                                    outInfo.info.uriPermissionPatterns[0] = pa3;
                                } else {
                                    int N = outInfo.info.uriPermissionPatterns.length;
                                    PatternMatcher[] newp = new PatternMatcher[N + 1];
                                    System.arraycopy(outInfo.info.uriPermissionPatterns, 0, newp, 0, N);
                                    newp[N] = pa3;
                                    outInfo.info.uriPermissionPatterns = newp;
                                }
                                outInfo.info.grantUriPermissions = true;
                                XmlUtils.skipCurrentTag(parser);
                            } else {
                                Slog.w(TAG, "Unknown element under <path-permission>: " + parser.getName() + " at " + this.mArchiveSourcePath + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + parser.getPositionDescription());
                                XmlUtils.skipCurrentTag(parser);
                            }
                        } else if (parser.getName().equals("path-permission")) {
                            TypedArray sa2 = res.obtainAttributes(parser, R.styleable.AndroidManifestPathPermission);
                            String permission = sa2.getNonConfigurationString(0, 0);
                            String readPermission3 = sa2.getNonConfigurationString(1, 0);
                            if (readPermission3 != null) {
                                readPermission = readPermission3;
                            } else {
                                readPermission = permission;
                            }
                            String writePermission2 = sa2.getNonConfigurationString(2, 0);
                            if (writePermission2 == null) {
                                writePermission2 = permission;
                            }
                            boolean havePerm = false;
                            if (readPermission == null) {
                                readPermission2 = readPermission;
                            } else {
                                havePerm = true;
                                readPermission2 = readPermission.intern();
                            }
                            if (writePermission2 == null) {
                                writePermission = writePermission2;
                            } else {
                                havePerm = true;
                                writePermission = writePermission2.intern();
                            }
                            if (!havePerm) {
                                Slog.w(TAG, "No readPermission or writePermssion for <path-permission>: " + parser.getName() + " at " + this.mArchiveSourcePath + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + parser.getPositionDescription());
                                XmlUtils.skipCurrentTag(parser);
                            } else {
                                String path = sa2.getNonConfigurationString(3, 0);
                                if (path == null) {
                                    pa = null;
                                } else {
                                    pa = new PathPermission(path, 0, readPermission2, writePermission);
                                }
                                PathPermission pa4 = pa;
                                String path2 = sa2.getNonConfigurationString(4, 0);
                                if (path2 == null) {
                                    pa2 = pa4;
                                } else {
                                    pa2 = new PathPermission(path2, 1, readPermission2, writePermission);
                                }
                                String path3 = sa2.getNonConfigurationString(5, 0);
                                if (path3 != null) {
                                    pa2 = new PathPermission(path3, 2, readPermission2, writePermission);
                                }
                                PathPermission pa5 = pa2;
                                String path4 = sa2.getNonConfigurationString(6, 0);
                                if (path4 != null) {
                                    pa5 = new PathPermission(path4, 3, readPermission2, writePermission);
                                }
                                sa2.recycle();
                                if (pa5 != null) {
                                    if (outInfo.info.pathPermissions == null) {
                                        outInfo.info.pathPermissions = new PathPermission[1];
                                        outInfo.info.pathPermissions[0] = pa5;
                                    } else {
                                        int N2 = outInfo.info.pathPermissions.length;
                                        PathPermission[] newp2 = new PathPermission[N2 + 1];
                                        System.arraycopy(outInfo.info.pathPermissions, 0, newp2, 0, N2);
                                        newp2[N2] = pa5;
                                        outInfo.info.pathPermissions = newp2;
                                    }
                                    XmlUtils.skipCurrentTag(parser);
                                } else {
                                    Slog.w(TAG, "No path, pathPrefix, or pathPattern for <path-permission>: " + parser.getName() + " at " + this.mArchiveSourcePath + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + parser.getPositionDescription());
                                    XmlUtils.skipCurrentTag(parser);
                                }
                            }
                        } else {
                            Slog.w(TAG, "Unknown element under <provider>: " + parser.getName() + " at " + this.mArchiveSourcePath + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + parser.getPositionDescription());
                            XmlUtils.skipCurrentTag(parser);
                        }
                    }
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:82:0x0279, code lost:
        if (r12 != false) goto L57;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x027b, code lost:
        r1 = r0.info;
     */
    /* JADX WARN: Code restructure failed: missing block: B:84:0x0283, code lost:
        if (r0.intents.size() <= 0) goto L55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x0286, code lost:
        r0 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:87:0x0287, code lost:
        r1.exported = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x0289, code lost:
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private android.content.pm.PackageParser.Service parseService(android.content.pm.PackageParser.Package r25, android.content.res.Resources r26, android.content.res.XmlResourceParser r27, int r28, java.lang.String[] r29, android.content.pm.PackageParser.CachedComponentArgs r30) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 650
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.parseService(android.content.pm.PackageParser$Package, android.content.res.Resources, android.content.res.XmlResourceParser, int, java.lang.String[], android.content.pm.PackageParser$CachedComponentArgs):android.content.pm.PackageParser$Service");
    }

    private boolean isImplicitlyExposedIntent(IntentInfo intent) {
        return intent.hasCategory(Intent.CATEGORY_BROWSABLE) || intent.hasAction(Intent.ACTION_SEND) || intent.hasAction(Intent.ACTION_SENDTO) || intent.hasAction(Intent.ACTION_SEND_MULTIPLE);
    }

    private boolean parseAllMetaData(Resources res, XmlResourceParser parser, String tag, Component<?> outInfo, String[] outError) throws XmlPullParserException, IOException {
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                break;
            } else if (type != 3 && type != 4) {
                if (parser.getName().equals("meta-data")) {
                    Bundle parseMetaData = parseMetaData(res, parser, outInfo.metaData, outError);
                    outInfo.metaData = parseMetaData;
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

    private Bundle parseMetaData(Resources res, XmlResourceParser parser, Bundle data, String[] outError) throws XmlPullParserException, IOException {
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

    private static VerifierInfo parseVerifier(AttributeSet attrs) {
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

    public static final PublicKey parsePublicKey(String encodedPublicKey) {
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

    /* JADX WARN: Code restructure failed: missing block: B:37:0x00ae, code lost:
        r24[0] = "No value supplied for <android:name>";
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00b0, code lost:
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00cd, code lost:
        r24[0] = "No value supplied for <android:name>";
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00cf, code lost:
        return false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean parseIntent(android.content.res.Resources r19, android.content.res.XmlResourceParser r20, boolean r21, boolean r22, android.content.pm.PackageParser.IntentInfo r23, java.lang.String[] r24) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 472
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.PackageParser.parseIntent(android.content.res.Resources, android.content.res.XmlResourceParser, boolean, boolean, android.content.pm.PackageParser$IntentInfo, java.lang.String[]):boolean");
    }

    /* loaded from: classes.dex */
    public static final class SigningDetails implements Parcelable {
        private static final int PAST_CERT_EXISTS = 0;
        public final Signature[] pastSigningCertificates;
        public final ArraySet<PublicKey> publicKeys;
        @SignatureSchemeVersion
        public final int signatureSchemeVersion;
        @UnsupportedAppUsage
        public final Signature[] signatures;
        public static final SigningDetails UNKNOWN = new SigningDetails(null, 0, null, null);
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
        public SigningDetails(Signature[] signatures, @SignatureSchemeVersion int signatureSchemeVersion, ArraySet<PublicKey> keys, Signature[] pastSigningCertificates) {
            this.signatures = signatures;
            this.signatureSchemeVersion = signatureSchemeVersion;
            this.publicKeys = keys;
            this.pastSigningCertificates = pastSigningCertificates;
        }

        public SigningDetails(Signature[] signatures, @SignatureSchemeVersion int signatureSchemeVersion, Signature[] pastSigningCertificates) throws CertificateException {
            this(signatures, signatureSchemeVersion, PackageParser.toSigningKeys(signatures), pastSigningCertificates);
        }

        public SigningDetails(Signature[] signatures, @SignatureSchemeVersion int signatureSchemeVersion) throws CertificateException {
            this(signatures, signatureSchemeVersion, null);
        }

        public SigningDetails(SigningDetails orig) {
            if (orig != null) {
                Signature[] signatureArr = orig.signatures;
                if (signatureArr != null) {
                    this.signatures = (Signature[]) signatureArr.clone();
                } else {
                    this.signatures = null;
                }
                this.signatureSchemeVersion = orig.signatureSchemeVersion;
                this.publicKeys = new ArraySet<>(orig.publicKeys);
                Signature[] signatureArr2 = orig.pastSigningCertificates;
                if (signatureArr2 != null) {
                    this.pastSigningCertificates = (Signature[]) signatureArr2.clone();
                    return;
                } else {
                    this.pastSigningCertificates = null;
                    return;
                }
            }
            this.signatures = null;
            this.signatureSchemeVersion = 0;
            this.publicKeys = null;
            this.pastSigningCertificates = null;
        }

        public boolean hasSignatures() {
            Signature[] signatureArr = this.signatures;
            return signatureArr != null && signatureArr.length > 0;
        }

        public boolean hasPastSigningCertificates() {
            Signature[] signatureArr = this.pastSigningCertificates;
            return signatureArr != null && signatureArr.length > 0;
        }

        public boolean hasAncestorOrSelf(SigningDetails oldDetails) {
            SigningDetails signingDetails = UNKNOWN;
            if (this == signingDetails || oldDetails == signingDetails) {
                return false;
            }
            Signature[] signatureArr = oldDetails.signatures;
            if (signatureArr.length > 1) {
                return signaturesMatchExactly(oldDetails);
            }
            return hasCertificate(signatureArr[0]);
        }

        public boolean hasAncestor(SigningDetails oldDetails) {
            SigningDetails signingDetails = UNKNOWN;
            if (this != signingDetails && oldDetails != signingDetails && hasPastSigningCertificates() && oldDetails.signatures.length == 1) {
                int i = 0;
                while (true) {
                    Signature[] signatureArr = this.pastSigningCertificates;
                    if (i >= signatureArr.length - 1) {
                        break;
                    } else if (signatureArr[i].equals(oldDetails.signatures[i])) {
                        return true;
                    } else {
                        i++;
                    }
                }
            }
            return false;
        }

        public boolean checkCapability(SigningDetails oldDetails, @CertCapabilities int flags) {
            SigningDetails signingDetails = UNKNOWN;
            if (this == signingDetails || oldDetails == signingDetails) {
                return false;
            }
            Signature[] signatureArr = oldDetails.signatures;
            if (signatureArr.length > 1) {
                return signaturesMatchExactly(oldDetails);
            }
            return hasCertificate(signatureArr[0], flags);
        }

        public boolean checkCapabilityRecover(SigningDetails oldDetails, @CertCapabilities int flags) throws CertificateException {
            SigningDetails signingDetails = UNKNOWN;
            if (oldDetails == signingDetails || this == signingDetails) {
                return false;
            }
            if (hasPastSigningCertificates() && oldDetails.signatures.length == 1) {
                int i = 0;
                while (true) {
                    Signature[] signatureArr = this.pastSigningCertificates;
                    if (i >= signatureArr.length) {
                        return false;
                    }
                    if (Signature.areEffectiveMatch(oldDetails.signatures[0], signatureArr[i]) && this.pastSigningCertificates[i].getFlags() == flags) {
                        return true;
                    }
                    i++;
                }
            } else {
                return Signature.areEffectiveMatch(oldDetails.signatures, this.signatures);
            }
        }

        public boolean hasCertificate(Signature signature) {
            return hasCertificateInternal(signature, 0);
        }

        public boolean hasCertificate(Signature signature, @CertCapabilities int flags) {
            return hasCertificateInternal(signature, flags);
        }

        public boolean hasCertificate(byte[] certificate) {
            Signature signature = new Signature(certificate);
            return hasCertificate(signature);
        }

        private boolean hasCertificateInternal(Signature signature, int flags) {
            int i;
            if (this == UNKNOWN) {
                return false;
            }
            if (hasPastSigningCertificates()) {
                while (true) {
                    Signature[] signatureArr = this.pastSigningCertificates;
                    if (i >= signatureArr.length - 1) {
                        break;
                    }
                    i = (signatureArr[i].equals(signature) && (flags == 0 || (this.pastSigningCertificates[i].getFlags() & flags) == flags)) ? 0 : i + 1;
                }
                return true;
            }
            Signature[] signatureArr2 = this.signatures;
            return signatureArr2.length == 1 && signatureArr2[0].equals(signature);
        }

        public boolean checkCapability(String sha256String, @CertCapabilities int flags) {
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

        public boolean hasSha256Certificate(byte[] sha256Certificate) {
            return hasSha256CertificateInternal(sha256Certificate, 0);
        }

        public boolean hasSha256Certificate(byte[] sha256Certificate, @CertCapabilities int flags) {
            return hasSha256CertificateInternal(sha256Certificate, flags);
        }

        private boolean hasSha256CertificateInternal(byte[] sha256Certificate, int flags) {
            int i;
            if (this == UNKNOWN) {
                return false;
            }
            if (hasPastSigningCertificates()) {
                while (true) {
                    Signature[] signatureArr = this.pastSigningCertificates;
                    if (i >= signatureArr.length - 1) {
                        break;
                    }
                    byte[] digest = PackageUtils.computeSha256DigestBytes(signatureArr[i].toByteArray());
                    i = (Arrays.equals(sha256Certificate, digest) && (flags == 0 || (this.pastSigningCertificates[i].getFlags() & flags) == flags)) ? 0 : i + 1;
                }
                return true;
            }
            Signature[] signatureArr2 = this.signatures;
            if (signatureArr2.length == 1) {
                byte[] digest2 = PackageUtils.computeSha256DigestBytes(signatureArr2[0].toByteArray());
                return Arrays.equals(sha256Certificate, digest2);
            }
            return false;
        }

        public boolean signaturesMatchExactly(SigningDetails other) {
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
        }

        protected SigningDetails(Parcel in) {
            ClassLoader boot = Object.class.getClassLoader();
            this.signatures = (Signature[]) in.createTypedArray(Signature.CREATOR);
            this.signatureSchemeVersion = in.readInt();
            this.publicKeys = in.readArraySet(boot);
            this.pastSigningCertificates = (Signature[]) in.createTypedArray(Signature.CREATOR);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o instanceof SigningDetails) {
                SigningDetails that = (SigningDetails) o;
                if (this.signatureSchemeVersion == that.signatureSchemeVersion && Signature.areExactMatch(this.signatures, that.signatures)) {
                    ArraySet<PublicKey> arraySet = this.publicKeys;
                    if (arraySet != null) {
                        if (!arraySet.equals(that.publicKeys)) {
                            return false;
                        }
                    } else if (that.publicKeys != null) {
                        return false;
                    }
                    return Arrays.equals(this.pastSigningCertificates, that.pastSigningCertificates);
                }
                return false;
            }
            return false;
        }

        public int hashCode() {
            int result = Arrays.hashCode(this.signatures);
            int result2 = ((result * 31) + this.signatureSchemeVersion) * 31;
            ArraySet<PublicKey> arraySet = this.publicKeys;
            return ((result2 + (arraySet != null ? arraySet.hashCode() : 0)) * 31) + Arrays.hashCode(this.pastSigningCertificates);
        }

        /* loaded from: classes.dex */
        public static class Builder {
            private Signature[] mPastSigningCertificates;
            private int mSignatureSchemeVersion = 0;
            private Signature[] mSignatures;

            @UnsupportedAppUsage
            public Builder setSignatures(Signature[] signatures) {
                this.mSignatures = signatures;
                return this;
            }

            @UnsupportedAppUsage
            public Builder setSignatureSchemeVersion(int signatureSchemeVersion) {
                this.mSignatureSchemeVersion = signatureSchemeVersion;
                return this;
            }

            @UnsupportedAppUsage
            public Builder setPastSigningCertificates(Signature[] pastSigningCertificates) {
                this.mPastSigningCertificates = pastSigningCertificates;
                return this;
            }

            private void checkInvariants() {
                if (this.mSignatures == null) {
                    throw new IllegalStateException("SigningDetails requires the current signing certificates.");
                }
            }

            @UnsupportedAppUsage
            public SigningDetails build() throws CertificateException {
                checkInvariants();
                return new SigningDetails(this.mSignatures, this.mSignatureSchemeVersion, this.mPastSigningCertificates);
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
        @UnsupportedAppUsage
        public final ArrayList<Activity> activities;
        @UnsupportedAppUsage
        public ApplicationInfo applicationInfo;
        public String baseCodePath;
        public boolean baseHardwareAccelerated;
        public int baseRevisionCode;
        public ArrayList<Package> childPackages;
        public String codePath;
        @UnsupportedAppUsage
        public ArrayList<ConfigurationInfo> configPreferences;
        public boolean coreApp;
        public String cpuAbiOverride;
        public ArrayList<FeatureGroupInfo> featureGroups;
        public final ArrayList<String> implicitPermissions;
        @UnsupportedAppUsage
        public int installLocation;
        @UnsupportedAppUsage
        public final ArrayList<Instrumentation> instrumentation;
        public boolean isStub;
        public ArrayList<String> libraryNames;
        public ArrayList<String> mAdoptPermissions;
        @UnsupportedAppUsage
        public Bundle mAppMetaData;
        public int mCompileSdkVersion;
        public String mCompileSdkVersionCodename;
        @UnsupportedAppUsage
        public Object mExtras;
        @UnsupportedAppUsage
        public ArrayMap<String, ArraySet<PublicKey>> mKeySetMapping;
        public long[] mLastPackageUsageTimeInMills;
        public ArrayList<String> mOriginalPackages;
        public String mOverlayCategory;
        public boolean mOverlayIsStatic;
        public int mOverlayPriority;
        public String mOverlayTarget;
        public String mOverlayTargetName;
        @UnsupportedAppUsage
        public int mPreferredOrder;
        public String mRealPackage;
        public String mRequiredAccountType;
        public boolean mRequiredForAllUsers;
        public String mRestrictedAccountType;
        @UnsupportedAppUsage
        public String mSharedUserId;
        @UnsupportedAppUsage
        public int mSharedUserLabel;
        @UnsupportedAppUsage
        public SigningDetails mSigningDetails;
        @UnsupportedAppUsage
        public ArraySet<String> mUpgradeKeySets;
        @UnsupportedAppUsage
        public int mVersionCode;
        public int mVersionCodeMajor;
        @UnsupportedAppUsage
        public String mVersionName;
        public String manifestPackageName;
        @UnsupportedAppUsage
        public String packageName;
        public Package parentPackage;
        @UnsupportedAppUsage
        public final ArrayList<PermissionGroup> permissionGroups;
        @UnsupportedAppUsage
        public final ArrayList<Permission> permissions;
        public ArrayList<ActivityIntentInfo> preferredActivityFilters;
        @UnsupportedAppUsage
        public ArrayList<String> protectedBroadcasts;
        @UnsupportedAppUsage
        public final ArrayList<Provider> providers;
        @UnsupportedAppUsage
        public final ArrayList<Activity> receivers;
        @UnsupportedAppUsage
        public ArrayList<FeatureInfo> reqFeatures;
        @UnsupportedAppUsage
        public final ArrayList<String> requestedPermissions;
        public byte[] restrictUpdateHash;
        @UnsupportedAppUsage
        public final ArrayList<Service> services;
        public String[] splitCodePaths;
        public int[] splitFlags;
        public String[] splitNames;
        public int[] splitPrivateFlags;
        public int[] splitRevisionCodes;
        public String staticSharedLibName;
        public long staticSharedLibVersion;
        public boolean use32bitAbi;
        @UnsupportedAppUsage
        public ArrayList<String> usesLibraries;
        @UnsupportedAppUsage
        public String[] usesLibraryFiles;
        public ArrayList<SharedLibraryInfo> usesLibraryInfos;
        @UnsupportedAppUsage
        public ArrayList<String> usesOptionalLibraries;
        public ArrayList<String> usesStaticLibraries;
        public String[][] usesStaticLibrariesCertDigests;
        public long[] usesStaticLibrariesVersions;
        public boolean visibleToInstantApps;
        public String volumeUuid;

        public long getLongVersionCode() {
            return PackageInfo.composeLongVersionCode(this.mVersionCodeMajor, this.mVersionCode);
        }

        @UnsupportedAppUsage
        public Package(String packageName) {
            this.applicationInfo = new ApplicationInfo();
            this.permissions = new ArrayList<>(0);
            this.permissionGroups = new ArrayList<>(0);
            this.activities = new ArrayList<>(0);
            this.receivers = new ArrayList<>(0);
            this.providers = new ArrayList<>(0);
            this.services = new ArrayList<>(0);
            this.instrumentation = new ArrayList<>(0);
            this.requestedPermissions = new ArrayList<>();
            this.implicitPermissions = new ArrayList<>();
            this.staticSharedLibName = null;
            this.staticSharedLibVersion = 0L;
            this.libraryNames = null;
            this.usesLibraries = null;
            this.usesStaticLibraries = null;
            this.usesStaticLibrariesVersions = null;
            this.usesStaticLibrariesCertDigests = null;
            this.usesOptionalLibraries = null;
            this.usesLibraryFiles = null;
            this.usesLibraryInfos = null;
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
            ApplicationInfo applicationInfo = this.applicationInfo;
            applicationInfo.packageName = packageName;
            applicationInfo.uid = -1;
        }

        public void setApplicationVolumeUuid(String volumeUuid) {
            UUID storageUuid = StorageManager.convert(volumeUuid);
            ApplicationInfo applicationInfo = this.applicationInfo;
            applicationInfo.volumeUuid = volumeUuid;
            applicationInfo.storageUuid = storageUuid;
            ArrayList<Package> arrayList = this.childPackages;
            if (arrayList != null) {
                int packageCount = arrayList.size();
                for (int i = 0; i < packageCount; i++) {
                    this.childPackages.get(i).applicationInfo.volumeUuid = volumeUuid;
                    this.childPackages.get(i).applicationInfo.storageUuid = storageUuid;
                }
            }
        }

        public void setApplicationInfoCodePath(String codePath) {
            this.applicationInfo.setCodePath(codePath);
            ArrayList<Package> arrayList = this.childPackages;
            if (arrayList != null) {
                int packageCount = arrayList.size();
                for (int i = 0; i < packageCount; i++) {
                    this.childPackages.get(i).applicationInfo.setCodePath(codePath);
                }
            }
        }

        @Deprecated
        public void setApplicationInfoResourcePath(String resourcePath) {
            this.applicationInfo.setResourcePath(resourcePath);
            ArrayList<Package> arrayList = this.childPackages;
            if (arrayList != null) {
                int packageCount = arrayList.size();
                for (int i = 0; i < packageCount; i++) {
                    this.childPackages.get(i).applicationInfo.setResourcePath(resourcePath);
                }
            }
        }

        @Deprecated
        public void setApplicationInfoBaseResourcePath(String resourcePath) {
            this.applicationInfo.setBaseResourcePath(resourcePath);
            ArrayList<Package> arrayList = this.childPackages;
            if (arrayList != null) {
                int packageCount = arrayList.size();
                for (int i = 0; i < packageCount; i++) {
                    this.childPackages.get(i).applicationInfo.setBaseResourcePath(resourcePath);
                }
            }
        }

        public void setApplicationInfoBaseCodePath(String baseCodePath) {
            this.applicationInfo.setBaseCodePath(baseCodePath);
            ArrayList<Package> arrayList = this.childPackages;
            if (arrayList != null) {
                int packageCount = arrayList.size();
                for (int i = 0; i < packageCount; i++) {
                    this.childPackages.get(i).applicationInfo.setBaseCodePath(baseCodePath);
                }
            }
        }

        public List<String> getChildPackageNames() {
            ArrayList<Package> arrayList = this.childPackages;
            if (arrayList == null) {
                return null;
            }
            int childCount = arrayList.size();
            List<String> childPackageNames = new ArrayList<>(childCount);
            for (int i = 0; i < childCount; i++) {
                String childPackageName = this.childPackages.get(i).packageName;
                childPackageNames.add(childPackageName);
            }
            return childPackageNames;
        }

        public boolean hasChildPackage(String packageName) {
            ArrayList<Package> arrayList = this.childPackages;
            int childCount = arrayList != null ? arrayList.size() : 0;
            for (int i = 0; i < childCount; i++) {
                if (this.childPackages.get(i).packageName.equals(packageName)) {
                    return true;
                }
            }
            return false;
        }

        public void setApplicationInfoSplitCodePaths(String[] splitCodePaths) {
            this.applicationInfo.setSplitCodePaths(splitCodePaths);
        }

        @Deprecated
        public void setApplicationInfoSplitResourcePaths(String[] resroucePaths) {
            this.applicationInfo.setSplitResourcePaths(resroucePaths);
        }

        public void setSplitCodePaths(String[] codePaths) {
            this.splitCodePaths = codePaths;
        }

        public void setCodePath(String codePath) {
            this.codePath = codePath;
            ArrayList<Package> arrayList = this.childPackages;
            if (arrayList != null) {
                int packageCount = arrayList.size();
                for (int i = 0; i < packageCount; i++) {
                    this.childPackages.get(i).codePath = codePath;
                }
            }
        }

        public void setBaseCodePath(String baseCodePath) {
            this.baseCodePath = baseCodePath;
            ArrayList<Package> arrayList = this.childPackages;
            if (arrayList != null) {
                int packageCount = arrayList.size();
                for (int i = 0; i < packageCount; i++) {
                    this.childPackages.get(i).baseCodePath = baseCodePath;
                }
            }
        }

        public void setSigningDetails(SigningDetails signingDetails) {
            this.mSigningDetails = signingDetails;
            ArrayList<Package> arrayList = this.childPackages;
            if (arrayList != null) {
                int packageCount = arrayList.size();
                for (int i = 0; i < packageCount; i++) {
                    this.childPackages.get(i).mSigningDetails = signingDetails;
                }
            }
        }

        public void setVolumeUuid(String volumeUuid) {
            this.volumeUuid = volumeUuid;
            ArrayList<Package> arrayList = this.childPackages;
            if (arrayList != null) {
                int packageCount = arrayList.size();
                for (int i = 0; i < packageCount; i++) {
                    this.childPackages.get(i).volumeUuid = volumeUuid;
                }
            }
        }

        public void setApplicationInfoFlags(int mask, int flags) {
            ApplicationInfo applicationInfo = this.applicationInfo;
            applicationInfo.flags = (applicationInfo.flags & (~mask)) | (mask & flags);
            ArrayList<Package> arrayList = this.childPackages;
            if (arrayList != null) {
                int packageCount = arrayList.size();
                for (int i = 0; i < packageCount; i++) {
                    this.childPackages.get(i).applicationInfo.flags = (this.applicationInfo.flags & (~mask)) | (mask & flags);
                }
            }
        }

        public void setUse32bitAbi(boolean use32bitAbi) {
            this.use32bitAbi = use32bitAbi;
            ArrayList<Package> arrayList = this.childPackages;
            if (arrayList != null) {
                int packageCount = arrayList.size();
                for (int i = 0; i < packageCount; i++) {
                    this.childPackages.get(i).use32bitAbi = use32bitAbi;
                }
            }
        }

        public boolean isLibrary() {
            return (this.staticSharedLibName == null && ArrayUtils.isEmpty(this.libraryNames)) ? false : true;
        }

        public List<String> getAllCodePaths() {
            ArrayList<String> paths = new ArrayList<>();
            paths.add(this.baseCodePath);
            if (!ArrayUtils.isEmpty(this.splitCodePaths)) {
                Collections.addAll(paths, this.splitCodePaths);
            }
            return paths;
        }

        public List<String> getAllCodePathsExcludingResourceOnly() {
            ArrayList<String> paths = new ArrayList<>();
            if ((this.applicationInfo.flags & 4) != 0) {
                paths.add(this.baseCodePath);
            }
            if (!ArrayUtils.isEmpty(this.splitCodePaths)) {
                int i = 0;
                while (true) {
                    String[] strArr = this.splitCodePaths;
                    if (i >= strArr.length) {
                        break;
                    }
                    if ((this.splitFlags[i] & 4) != 0) {
                        paths.add(strArr[i]);
                    }
                    i++;
                }
            }
            return paths;
        }

        @UnsupportedAppUsage
        public void setPackageName(String newName) {
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

        public boolean hasComponentClassName(String name) {
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

        public boolean isExternal() {
            return this.applicationInfo.isExternal();
        }

        public boolean isForwardLocked() {
            return false;
        }

        public boolean isOem() {
            return this.applicationInfo.isOem();
        }

        public boolean isVendor() {
            return this.applicationInfo.isVendor();
        }

        public boolean isProduct() {
            return this.applicationInfo.isProduct();
        }

        public boolean isProductServices() {
            return this.applicationInfo.isProductServices();
        }

        public boolean isOdm() {
            return this.applicationInfo.isOdm();
        }

        public boolean isPrivileged() {
            return this.applicationInfo.isPrivilegedApp();
        }

        public boolean isSystem() {
            return this.applicationInfo.isSystemApp();
        }

        public boolean isUpdatedSystemApp() {
            return this.applicationInfo.isUpdatedSystemApp();
        }

        public boolean canHaveOatDir() {
            return !isSystem() || isUpdatedSystemApp();
        }

        public boolean isMatch(int flags) {
            if ((1048576 & flags) != 0) {
                return isSystem();
            }
            return true;
        }

        public long getLatestPackageUseTimeInMills() {
            long[] jArr;
            long latestUse = 0;
            for (long use : this.mLastPackageUsageTimeInMills) {
                latestUse = Math.max(latestUse, use);
            }
            return latestUse;
        }

        public long getLatestForegroundPackageUseTimeInMills() {
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

        public Package(Parcel dest) {
            this.applicationInfo = new ApplicationInfo();
            this.permissions = new ArrayList<>(0);
            this.permissionGroups = new ArrayList<>(0);
            this.activities = new ArrayList<>(0);
            this.receivers = new ArrayList<>(0);
            this.providers = new ArrayList<>(0);
            this.services = new ArrayList<>(0);
            this.instrumentation = new ArrayList<>(0);
            this.requestedPermissions = new ArrayList<>();
            this.implicitPermissions = new ArrayList<>();
            this.staticSharedLibName = null;
            this.staticSharedLibVersion = 0L;
            this.libraryNames = null;
            this.usesLibraries = null;
            this.usesStaticLibraries = null;
            this.usesStaticLibrariesVersions = null;
            this.usesStaticLibrariesCertDigests = null;
            this.usesOptionalLibraries = null;
            this.usesLibraryFiles = null;
            this.usesLibraryInfos = null;
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
                ApplicationInfo applicationInfo = this.applicationInfo;
                applicationInfo.permission = applicationInfo.permission.intern();
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
            dest.readStringList(this.implicitPermissions);
            internStringArrayList(this.implicitPermissions);
            this.protectedBroadcasts = dest.createStringArrayList();
            internStringArrayList(this.protectedBroadcasts);
            this.parentPackage = (Package) dest.readParcelable(boot);
            this.childPackages = new ArrayList<>();
            dest.readParcelableList(this.childPackages, boot);
            if (this.childPackages.size() == 0) {
                this.childPackages = null;
            }
            this.staticSharedLibName = dest.readString();
            String str = this.staticSharedLibName;
            if (str != null) {
                this.staticSharedLibName = str.intern();
            }
            this.staticSharedLibVersion = dest.readLong();
            this.libraryNames = dest.createStringArrayList();
            internStringArrayList(this.libraryNames);
            this.usesLibraries = dest.createStringArrayList();
            internStringArrayList(this.usesLibraries);
            this.usesOptionalLibraries = dest.createStringArrayList();
            internStringArrayList(this.usesOptionalLibraries);
            this.usesLibraryFiles = dest.readStringArray();
            this.usesLibraryInfos = dest.createTypedArrayList(SharedLibraryInfo.CREATOR);
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
            String str2 = this.mVersionName;
            if (str2 != null) {
                this.mVersionName = str2.intern();
            }
            this.mSharedUserId = dest.readString();
            String str3 = this.mSharedUserId;
            if (str3 != null) {
                this.mSharedUserId = str3.intern();
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
            this.mOverlayTargetName = dest.readString();
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

        private static void internStringArrayList(List<String> list) {
            if (list != null) {
                int N = list.size();
                for (int i = 0; i < N; i++) {
                    list.set(i, list.get(i).intern());
                }
            }
        }

        private void fixupOwner(List<? extends Component<?>> list) {
            if (list != null) {
                for (Component<?> c : list) {
                    c.owner = this;
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
            dest.writeStringList(this.implicitPermissions);
            dest.writeStringList(this.protectedBroadcasts);
            dest.writeParcelable(this.parentPackage, flags);
            dest.writeParcelableList(this.childPackages, flags);
            dest.writeString(this.staticSharedLibName);
            dest.writeLong(this.staticSharedLibVersion);
            dest.writeStringList(this.libraryNames);
            dest.writeStringList(this.usesLibraries);
            dest.writeStringList(this.usesOptionalLibraries);
            dest.writeStringArray(this.usesLibraryFiles);
            dest.writeTypedList(this.usesLibraryInfos);
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
            dest.writeString(this.mOverlayTargetName);
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

        private static void writeKeySetMapping(Parcel dest, ArrayMap<String, ArraySet<PublicKey>> keySetMapping) {
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

        private static ArrayMap<String, ArraySet<PublicKey>> readKeySetMapping(Parcel in) {
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
        @UnsupportedAppUsage
        public final String className;
        ComponentName componentName;
        String componentShortName;
        @UnsupportedAppUsage
        public final ArrayList<II> intents;
        @UnsupportedAppUsage
        public Bundle metaData;
        public int order;
        @UnsupportedAppUsage
        public Package owner;

        public Component(Package owner, ArrayList<II> intents, String className) {
            this.owner = owner;
            this.intents = intents;
            this.className = className;
        }

        public Component(Package owner) {
            this.owner = owner;
            this.intents = null;
            this.className = null;
        }

        public Component(ParsePackageItemArgs args, PackageItemInfo outInfo) {
            this.owner = args.owner;
            this.intents = new ArrayList<>(0);
            if (PackageParser.parsePackageItemInfo(args.owner, outInfo, args.outError, args.tag, args.sa, true, args.nameRes, args.labelRes, args.iconRes, args.roundIconRes, args.logoRes, args.bannerRes)) {
                this.className = outInfo.name;
            } else {
                this.className = null;
            }
        }

        public Component(ParseComponentArgs args, ComponentInfo outInfo) {
            this((ParsePackageItemArgs) args, (PackageItemInfo) outInfo);
            String nonResourceString;
            if (args.outError[0] != null) {
                return;
            }
            if (args.processRes != 0) {
                if (this.owner.applicationInfo.targetSdkVersion >= 8) {
                    nonResourceString = args.sa.getNonConfigurationString(args.processRes, 1024);
                } else {
                    nonResourceString = args.sa.getNonResourceString(args.processRes);
                }
                outInfo.processName = PackageParser.buildProcessName(this.owner.applicationInfo.packageName, this.owner.applicationInfo.processName, nonResourceString, args.flags, args.sepProcesses, args.outError);
            }
            if (args.descriptionRes != 0) {
                outInfo.descriptionRes = args.sa.getResourceId(args.descriptionRes, 0);
            }
            outInfo.enabled = args.sa.getBoolean(args.enabledRes, true);
        }

        public Component(Component<II> clone) {
            this.owner = clone.owner;
            this.intents = clone.intents;
            this.className = clone.className;
            this.componentName = clone.componentName;
            this.componentShortName = clone.componentShortName;
        }

        @UnsupportedAppUsage
        public ComponentName getComponentName() {
            ComponentName componentName = this.componentName;
            if (componentName != null) {
                return componentName;
            }
            if (this.className != null) {
                this.componentName = new ComponentName(this.owner.applicationInfo.packageName, this.className);
            }
            return this.componentName;
        }

        protected Component(Parcel in) {
            this.className = in.readString();
            this.metaData = in.readBundle();
            this.intents = createIntentsList(in);
            this.owner = null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.className);
            dest.writeBundle(this.metaData);
            writeIntentsList(this.intents, dest, flags);
        }

        private static void writeIntentsList(ArrayList<? extends IntentInfo> list, Parcel out, int flags) {
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
        private static <T extends IntentInfo> ArrayList<T> createIntentsList(Parcel in) {
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

        public void appendComponentShortName(StringBuilder sb) {
            ComponentName.appendShortString(sb, this.owner.applicationInfo.packageName, this.className);
        }

        public void printComponentShortName(PrintWriter pw) {
            ComponentName.printShortString(pw, this.owner.applicationInfo.packageName, this.className);
        }

        public void setPackageName(String packageName) {
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
        @UnsupportedAppUsage
        public PermissionGroup group;
        @UnsupportedAppUsage
        public final PermissionInfo info;
        @UnsupportedAppUsage
        public boolean tree;

        public Permission(Package owner, String backgroundPermission) {
            super(owner);
            this.info = new PermissionInfo(backgroundPermission);
        }

        @UnsupportedAppUsage
        public Permission(Package _owner, PermissionInfo _info) {
            super(_owner);
            this.info = _info;
        }

        @Override // android.content.pm.PackageParser.Component
        public void setPackageName(String packageName) {
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

        public boolean isAppOp() {
            return this.info.isAppOp();
        }

        private Permission(Parcel in) {
            super(in);
            ClassLoader boot = Object.class.getClassLoader();
            this.info = (PermissionInfo) in.readParcelable(boot);
            if (this.info.group != null) {
                PermissionInfo permissionInfo = this.info;
                permissionInfo.group = permissionInfo.group.intern();
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
        @UnsupportedAppUsage
        public final PermissionGroupInfo info;

        public PermissionGroup(Package owner, int requestDetailResourceId, int backgroundRequestResourceId, int backgroundRequestDetailResourceId) {
            super(owner);
            this.info = new PermissionGroupInfo(requestDetailResourceId, backgroundRequestResourceId, backgroundRequestDetailResourceId);
        }

        public PermissionGroup(Package _owner, PermissionGroupInfo _info) {
            super(_owner);
            this.info = _info;
        }

        @Override // android.content.pm.PackageParser.Component
        public void setPackageName(String packageName) {
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

        private PermissionGroup(Parcel in) {
            super(in);
            this.info = (PermissionGroupInfo) in.readParcelable(Object.class.getClassLoader());
        }
    }

    private static boolean copyNeeded(int flags, Package p, PackageUserState state, Bundle metaData, int userId) {
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
        if ((flags & 128) != 0 && (metaData != null || p.mAppMetaData != null)) {
            return true;
        }
        if ((flags & 1024) == 0 || p.usesLibraryFiles == null) {
            return (((flags & 1024) == 0 || p.usesLibraryInfos == null) && p.staticSharedLibName == null) ? false : true;
        }
        return true;
    }

    @UnsupportedAppUsage
    public static ApplicationInfo generateApplicationInfo(Package p, int flags, PackageUserState state) {
        return generateApplicationInfo(p, flags, state, UserHandle.getCallingUserId());
    }

    private static void updateApplicationInfo(ApplicationInfo ai, int flags, PackageUserState state) {
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
        ai.icon = (!sUseRoundIcon || ai.roundIconRes == 0) ? ai.iconRes : ai.roundIconRes;
    }

    @UnsupportedAppUsage
    public static ApplicationInfo generateApplicationInfo(Package p, int flags, PackageUserState state, int userId) {
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
            ai.sharedLibraryInfos = p.usesLibraryInfos;
        }
        if (state.stopped) {
            ai.flags |= 2097152;
        } else {
            ai.flags &= -2097153;
        }
        updateApplicationInfo(ai, flags, state);
        return ai;
    }

    public static ApplicationInfo generateApplicationInfo(ApplicationInfo ai, int flags, PackageUserState state, int userId) {
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

    @UnsupportedAppUsage
    public static final PermissionInfo generatePermissionInfo(Permission p, int flags) {
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

    @UnsupportedAppUsage
    public static final PermissionGroupInfo generatePermissionGroupInfo(PermissionGroup pg, int flags) {
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
        @UnsupportedAppUsage
        public final ActivityInfo info;
        private boolean mHasMaxAspectRatio;
        private boolean mHasMinAspectRatio;

        /* JADX INFO: Access modifiers changed from: private */
        public boolean hasMaxAspectRatio() {
            return this.mHasMaxAspectRatio;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean hasMinAspectRatio() {
            return this.mHasMinAspectRatio;
        }

        Activity(Package owner, String className, ActivityInfo info) {
            super(owner, new ArrayList(0), className);
            this.info = info;
            this.info.applicationInfo = owner.applicationInfo;
        }

        public Activity(ParseComponentArgs args, ActivityInfo _info) {
            super(args, (ComponentInfo) _info);
            this.info = _info;
            this.info.applicationInfo = args.owner.applicationInfo;
        }

        @Override // android.content.pm.PackageParser.Component
        public void setPackageName(String packageName) {
            super.setPackageName(packageName);
            this.info.packageName = packageName;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setMaxAspectRatio(float maxAspectRatio) {
            if (this.info.resizeMode == 2 || this.info.resizeMode == 1) {
                return;
            }
            if (maxAspectRatio < 1.0f && maxAspectRatio != 0.0f) {
                return;
            }
            this.info.maxAspectRatio = maxAspectRatio;
            this.mHasMaxAspectRatio = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setMinAspectRatio(float minAspectRatio) {
            if (this.info.resizeMode == 2 || this.info.resizeMode == 1) {
                return;
            }
            if (minAspectRatio < 1.0f && minAspectRatio != 0.0f) {
                return;
            }
            this.info.minAspectRatio = minAspectRatio;
            this.mHasMinAspectRatio = true;
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
            dest.writeBoolean(this.mHasMinAspectRatio);
        }

        private Activity(Parcel in) {
            super(in);
            this.info = (ActivityInfo) in.readParcelable(Object.class.getClassLoader());
            this.mHasMaxAspectRatio = in.readBoolean();
            this.mHasMinAspectRatio = in.readBoolean();
            Iterator it = this.intents.iterator();
            while (it.hasNext()) {
                ActivityIntentInfo aii = (ActivityIntentInfo) it.next();
                aii.activity = this;
                this.order = Math.max(aii.getOrder(), this.order);
            }
            if (this.info.permission != null) {
                ActivityInfo activityInfo = this.info;
                activityInfo.permission = activityInfo.permission.intern();
            }
        }
    }

    @UnsupportedAppUsage
    public static final ActivityInfo generateActivityInfo(Activity a, int flags, PackageUserState state, int userId) {
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
        return ai;
    }

    public static final ActivityInfo generateActivityInfo(ActivityInfo ai, int flags, PackageUserState state, int userId) {
        if (ai == null || !checkUseInstalledOrHidden(flags, state, ai.applicationInfo)) {
            return null;
        }
        ActivityInfo ai2 = new ActivityInfo(ai);
        ai2.applicationInfo = generateApplicationInfo(ai2.applicationInfo, flags, state, userId);
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
        @UnsupportedAppUsage
        public final ServiceInfo info;

        public Service(ParseComponentArgs args, ServiceInfo _info) {
            super(args, (ComponentInfo) _info);
            this.info = _info;
            this.info.applicationInfo = args.owner.applicationInfo;
        }

        @Override // android.content.pm.PackageParser.Component
        public void setPackageName(String packageName) {
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

        private Service(Parcel in) {
            super(in);
            this.info = (ServiceInfo) in.readParcelable(Object.class.getClassLoader());
            Iterator it = this.intents.iterator();
            while (it.hasNext()) {
                ServiceIntentInfo aii = (ServiceIntentInfo) it.next();
                aii.service = this;
                this.order = Math.max(aii.getOrder(), this.order);
            }
            if (this.info.permission != null) {
                ServiceInfo serviceInfo = this.info;
                serviceInfo.permission = serviceInfo.permission.intern();
            }
        }
    }

    @UnsupportedAppUsage
    public static final ServiceInfo generateServiceInfo(Service s, int flags, PackageUserState state, int userId) {
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
        @UnsupportedAppUsage
        public final ProviderInfo info;
        @UnsupportedAppUsage
        public boolean syncable;

        public Provider(ParseComponentArgs args, ProviderInfo _info) {
            super(args, (ComponentInfo) _info);
            this.info = _info;
            this.info.applicationInfo = args.owner.applicationInfo;
            this.syncable = false;
        }

        @UnsupportedAppUsage
        public Provider(Provider existingProvider) {
            super(existingProvider);
            this.info = existingProvider.info;
            this.syncable = existingProvider.syncable;
        }

        @Override // android.content.pm.PackageParser.Component
        public void setPackageName(String packageName) {
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

        private Provider(Parcel in) {
            super(in);
            this.info = (ProviderInfo) in.readParcelable(Object.class.getClassLoader());
            this.syncable = in.readInt() == 1;
            Iterator it = this.intents.iterator();
            while (it.hasNext()) {
                ProviderIntentInfo aii = (ProviderIntentInfo) it.next();
                aii.provider = this;
            }
            if (this.info.readPermission != null) {
                ProviderInfo providerInfo = this.info;
                providerInfo.readPermission = providerInfo.readPermission.intern();
            }
            if (this.info.writePermission != null) {
                ProviderInfo providerInfo2 = this.info;
                providerInfo2.writePermission = providerInfo2.writePermission.intern();
            }
            if (this.info.authority != null) {
                ProviderInfo providerInfo3 = this.info;
                providerInfo3.authority = providerInfo3.authority.intern();
            }
        }
    }

    @UnsupportedAppUsage
    public static final ProviderInfo generateProviderInfo(Provider p, int flags, PackageUserState state, int userId) {
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
        @UnsupportedAppUsage
        public final InstrumentationInfo info;

        public Instrumentation(ParsePackageItemArgs args, InstrumentationInfo _info) {
            super(args, _info);
            this.info = _info;
        }

        @Override // android.content.pm.PackageParser.Component
        public void setPackageName(String packageName) {
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

        private Instrumentation(Parcel in) {
            super(in);
            this.info = (InstrumentationInfo) in.readParcelable(Object.class.getClassLoader());
            if (this.info.targetPackage != null) {
                InstrumentationInfo instrumentationInfo = this.info;
                instrumentationInfo.targetPackage = instrumentationInfo.targetPackage.intern();
            }
            if (this.info.targetProcesses != null) {
                InstrumentationInfo instrumentationInfo2 = this.info;
                instrumentationInfo2.targetProcesses = instrumentationInfo2.targetProcesses.intern();
            }
        }
    }

    @UnsupportedAppUsage
    public static final InstrumentationInfo generateInstrumentationInfo(Instrumentation i, int flags) {
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
        @UnsupportedAppUsage
        public int banner;
        @UnsupportedAppUsage
        public boolean hasDefault;
        @UnsupportedAppUsage
        public int icon;
        @UnsupportedAppUsage
        public int labelRes;
        @UnsupportedAppUsage
        public int logo;
        @UnsupportedAppUsage
        public CharSequence nonLocalizedLabel;
        public int preferred;

        @UnsupportedAppUsage
        protected IntentInfo() {
        }

        protected IntentInfo(Parcel dest) {
            super(dest);
            this.hasDefault = dest.readInt() == 1;
            this.labelRes = dest.readInt();
            this.nonLocalizedLabel = dest.readCharSequence();
            this.icon = dest.readInt();
            this.logo = dest.readInt();
            this.banner = dest.readInt();
            this.preferred = dest.readInt();
        }

        public void writeIntentInfoToParcel(Parcel dest, int flags) {
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
        @UnsupportedAppUsage
        public Activity activity;

        public ActivityIntentInfo(Activity _activity) {
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

        public ActivityIntentInfo(Parcel in) {
            super(in);
        }
    }

    /* loaded from: classes.dex */
    public static final class ServiceIntentInfo extends IntentInfo {
        @UnsupportedAppUsage
        public Service service;

        public ServiceIntentInfo(Service _service) {
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

        public ServiceIntentInfo(Parcel in) {
            super(in);
        }
    }

    /* loaded from: classes.dex */
    public static final class ProviderIntentInfo extends IntentInfo {
        @UnsupportedAppUsage
        public Provider provider;

        public ProviderIntentInfo(Provider provider) {
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

        public ProviderIntentInfo(Parcel in) {
            super(in);
        }
    }

    @UnsupportedAppUsage
    public static void setCompatibilityModeEnabled(boolean compatibilityModeEnabled) {
        sCompatibilityModeEnabled = compatibilityModeEnabled;
    }

    public static void readConfigUseRoundIcon(Resources r) {
        if (r != null) {
            sUseRoundIcon = r.getBoolean(R.bool.config_useRoundIcon);
            return;
        }
        try {
            ApplicationInfo androidAppInfo = ActivityThread.getPackageManager().getApplicationInfo("android", 0, UserHandle.myUserId());
            Resources systemResources = Resources.getSystem();
            Resources overlayableRes = ResourcesManager.getInstance().getResources(null, null, null, androidAppInfo.resourceDirs, androidAppInfo.sharedLibraryFiles, 0, null, systemResources.getCompatibilityInfo(), systemResources.getClassLoader());
            sUseRoundIcon = overlayableRes.getBoolean(R.bool.config_useRoundIcon);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* loaded from: classes.dex */
    public static class PackageParserException extends Exception {
        public final int error;

        public PackageParserException(int error, String detailMessage) {
            super(detailMessage);
            this.error = error;
        }

        public PackageParserException(int error, String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
            this.error = error;
        }
    }

    public static PackageInfo generatePackageInfoFromApex(ApexInfo apexInfo, int flags) throws PackageParserException {
        PackageParser pp = new PackageParser();
        File apexFile = new File(apexInfo.packagePath);
        Package p = pp.parsePackage(apexFile, flags, false);
        PackageUserState state = new PackageUserState();
        PackageInfo pi = generatePackageInfo(p, EmptyArray.INT, flags, 0L, 0L, Collections.emptySet(), state);
        pi.applicationInfo.sourceDir = apexFile.getPath();
        pi.applicationInfo.publicSourceDir = apexFile.getPath();
        if (apexInfo.isFactory) {
            pi.applicationInfo.flags |= 1;
        } else {
            pi.applicationInfo.flags &= -2;
        }
        if (apexInfo.isActive) {
            pi.applicationInfo.flags |= 8388608;
        } else {
            pi.applicationInfo.flags &= -8388609;
        }
        pi.isApex = true;
        if ((134217728 & flags) != 0) {
            collectCertificates(p, apexFile, false);
            if (p.mSigningDetails.hasPastSigningCertificates()) {
                pi.signatures = new Signature[1];
                pi.signatures[0] = p.mSigningDetails.pastSigningCertificates[0];
            } else if (p.mSigningDetails.hasSignatures()) {
                int numberOfSigs = p.mSigningDetails.signatures.length;
                pi.signatures = new Signature[numberOfSigs];
                System.arraycopy(p.mSigningDetails.signatures, 0, pi.signatures, 0, numberOfSigs);
            }
            if (p.mSigningDetails != SigningDetails.UNKNOWN) {
                pi.signingInfo = new SigningInfo(p.mSigningDetails);
            } else {
                pi.signingInfo = null;
            }
        }
        return pi;
    }
}
