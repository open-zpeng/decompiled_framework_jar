package android.content.res;

import android.animation.Animator;
import android.animation.StateListAnimator;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.FontResourcesParser;
import android.content.res.Resources;
import android.content.res.XmlBlock;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.icu.text.PluralRules;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Build;
import android.os.LocaleList;
import android.os.Process;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.TypedValue;
import android.util.Xml;
import android.view.DisplayAdjustments;
import com.android.internal.util.GrowingArrayUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Supplier;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class ResourcesImpl {
    private static final boolean DEBUG_CONFIG = false;
    private static final boolean DEBUG_LOAD = false;
    private static final int ID_OTHER = 16777220;
    static final String TAG = "Resources";
    static final String TAG_PRELOAD = "Resources.preload";
    public protected static final boolean TRACE_FOR_MISS_PRELOAD = false;
    public protected static final boolean TRACE_FOR_PRELOAD = false;
    private static final int XML_BLOCK_CACHE_SIZE = 4;
    private static int sPreloadTracingNumLoadedDrawables;
    private static boolean sPreloaded;
    public private protected final AssetManager mAssets;
    private final DisplayAdjustments mDisplayAdjustments;
    private PluralRules mPluralRule;
    private long mPreloadTracingPreloadStartTime;
    private long mPreloadTracingStartBitmapCount;
    private long mPreloadTracingStartBitmapSize;
    public protected boolean mPreloading;
    public static final boolean TRACE_FOR_DETAILED_PRELOAD = SystemProperties.getBoolean("debug.trace_resource_preload", false);
    private static final Object sSync = new Object();
    public protected static final LongSparseArray<Drawable.ConstantState> sPreloadedColorDrawables = new LongSparseArray<>();
    public protected static final LongSparseArray<ConstantState<ComplexColor>> sPreloadedComplexColors = new LongSparseArray<>();
    public protected static final LongSparseArray<Drawable.ConstantState>[] sPreloadedDrawables = new LongSparseArray[2];
    public protected final Object mAccessLock = new Object();
    private final Configuration mTmpConfig = new Configuration();
    public protected final DrawableCache mDrawableCache = new DrawableCache();
    public protected final DrawableCache mColorDrawableCache = new DrawableCache();
    private final ConfigurationBoundResourceCache<ComplexColor> mComplexColorCache = new ConfigurationBoundResourceCache<>();
    public protected final ConfigurationBoundResourceCache<Animator> mAnimatorCache = new ConfigurationBoundResourceCache<>();
    public protected final ConfigurationBoundResourceCache<StateListAnimator> mStateListAnimatorCache = new ConfigurationBoundResourceCache<>();
    private final ThreadLocal<LookupStack> mLookupStack = ThreadLocal.withInitial(new Supplier() { // from class: android.content.res.-$$Lambda$ResourcesImpl$h3PTRX185BeQl8SVC2_w9arp5Og
        @Override // java.util.function.Supplier
        public final Object get() {
            return ResourcesImpl.lambda$new$0();
        }
    });
    private int mLastCachedXmlBlockIndex = -1;
    private final int[] mCachedXmlBlockCookies = new int[4];
    private final String[] mCachedXmlBlockFiles = new String[4];
    private final XmlBlock[] mCachedXmlBlocks = new XmlBlock[4];
    private final DisplayMetrics mMetrics = new DisplayMetrics();
    private boolean mColorCacheEnabled = false;
    private boolean mDrawableCacheEnabled = false;
    public protected final Configuration mConfiguration = new Configuration();

    static {
        sPreloadedDrawables[0] = new LongSparseArray<>();
        sPreloadedDrawables[1] = new LongSparseArray<>();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ LookupStack lambda$new$0() {
        return new LookupStack();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ResourcesImpl(AssetManager assets, DisplayMetrics metrics, Configuration config, DisplayAdjustments displayAdjustments) {
        this.mAssets = assets;
        this.mMetrics.setToDefaults();
        this.mDisplayAdjustments = displayAdjustments;
        this.mConfiguration.setToDefaults();
        updateConfiguration(config, metrics, displayAdjustments.getCompatibilityInfo());
    }

    public synchronized DisplayAdjustments getDisplayAdjustments() {
        return this.mDisplayAdjustments;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public AssetManager getAssets() {
        return this.mAssets;
    }

    public private protected DisplayMetrics getDisplayMetrics() {
        return this.mMetrics;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Configuration getConfiguration() {
        return this.mConfiguration;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Configuration[] getSizeConfigurations() {
        return this.mAssets.getSizeConfigurations();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized CompatibilityInfo getCompatibilityInfo() {
        return this.mDisplayAdjustments.getCompatibilityInfo();
    }

    private synchronized PluralRules getPluralRule() {
        PluralRules pluralRules;
        synchronized (sSync) {
            if (this.mPluralRule == null) {
                this.mPluralRule = PluralRules.forLocale(this.mConfiguration.getLocales().get(0));
            }
            pluralRules = this.mPluralRule;
        }
        return pluralRules;
    }

    public private protected void getValue(int id, TypedValue outValue, boolean resolveRefs) throws Resources.NotFoundException {
        boolean found = this.mAssets.getResourceValue(id, 0, outValue, resolveRefs);
        if (found) {
            return;
        }
        throw new Resources.NotFoundException("Resource ID #0x" + Integer.toHexString(id));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void getValueForDensity(int id, int density, TypedValue outValue, boolean resolveRefs) throws Resources.NotFoundException {
        boolean found = this.mAssets.getResourceValue(id, density, outValue, resolveRefs);
        if (found) {
            return;
        }
        throw new Resources.NotFoundException("Resource ID #0x" + Integer.toHexString(id));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void getValue(String name, TypedValue outValue, boolean resolveRefs) throws Resources.NotFoundException {
        int id = getIdentifier(name, "string", null);
        if (id != 0) {
            getValue(id, outValue, resolveRefs);
            return;
        }
        throw new Resources.NotFoundException("String resource name " + name);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int getIdentifier(String name, String defType, String defPackage) {
        if (name == null) {
            throw new NullPointerException("name is null");
        }
        try {
            return Integer.parseInt(name);
        } catch (Exception e) {
            return this.mAssets.getResourceIdentifier(name, defType, defPackage);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized String getResourceName(int resid) throws Resources.NotFoundException {
        String str = this.mAssets.getResourceName(resid);
        if (str != null) {
            return str;
        }
        throw new Resources.NotFoundException("Unable to find resource ID #0x" + Integer.toHexString(resid));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized String getResourcePackageName(int resid) throws Resources.NotFoundException {
        String str = this.mAssets.getResourcePackageName(resid);
        if (str != null) {
            return str;
        }
        throw new Resources.NotFoundException("Unable to find resource ID #0x" + Integer.toHexString(resid));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized String getResourceTypeName(int resid) throws Resources.NotFoundException {
        String str = this.mAssets.getResourceTypeName(resid);
        if (str != null) {
            return str;
        }
        throw new Resources.NotFoundException("Unable to find resource ID #0x" + Integer.toHexString(resid));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized String getResourceEntryName(int resid) throws Resources.NotFoundException {
        String str = this.mAssets.getResourceEntryName(resid);
        if (str != null) {
            return str;
        }
        throw new Resources.NotFoundException("Unable to find resource ID #0x" + Integer.toHexString(resid));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized CharSequence getQuantityText(int id, int quantity) throws Resources.NotFoundException {
        PluralRules rule = getPluralRule();
        CharSequence res = this.mAssets.getResourceBagText(id, attrForQuantityCode(rule.select(quantity)));
        if (res != null) {
            return res;
        }
        CharSequence res2 = this.mAssets.getResourceBagText(id, ID_OTHER);
        if (res2 != null) {
            return res2;
        }
        throw new Resources.NotFoundException("Plural resource ID #0x" + Integer.toHexString(id) + " quantity=" + quantity + " item=" + rule.select(quantity));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private static synchronized int attrForQuantityCode(String quantityCode) {
        char c;
        switch (quantityCode.hashCode()) {
            case 101272:
                if (quantityCode.equals("few")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 110182:
                if (quantityCode.equals("one")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 115276:
                if (quantityCode.equals("two")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 3343967:
                if (quantityCode.equals("many")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 3735208:
                if (quantityCode.equals("zero")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return 16777221;
            case 1:
                return 16777222;
            case 2:
                return 16777223;
            case 3:
                return 16777224;
            case 4:
                return 16777225;
            default:
                return ID_OTHER;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized AssetFileDescriptor openRawResourceFd(int id, TypedValue tempValue) throws Resources.NotFoundException {
        getValue(id, tempValue, true);
        try {
            return this.mAssets.openNonAssetFd(tempValue.assetCookie, tempValue.string.toString());
        } catch (Exception e) {
            throw new Resources.NotFoundException("File " + tempValue.string.toString() + " from drawable resource ID #0x" + Integer.toHexString(id), e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized InputStream openRawResource(int id, TypedValue value) throws Resources.NotFoundException {
        getValue(id, value, true);
        try {
            return this.mAssets.openNonAsset(value.assetCookie, value.string.toString(), 2);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("File ");
            sb.append(value.string == null ? "(null)" : value.string.toString());
            sb.append(" from drawable resource ID #0x");
            sb.append(Integer.toHexString(id));
            Resources.NotFoundException rnf = new Resources.NotFoundException(sb.toString());
            rnf.initCause(e);
            throw rnf;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ConfigurationBoundResourceCache<Animator> getAnimatorCache() {
        return this.mAnimatorCache;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ConfigurationBoundResourceCache<StateListAnimator> getStateListAnimatorCache() {
        return this.mStateListAnimatorCache;
    }

    public synchronized void updateConfiguration(Configuration config, DisplayMetrics metrics, CompatibilityInfo compat) {
        int width;
        int i;
        int i2;
        Locale bestLocale;
        Trace.traceBegin(8192L, "ResourcesImpl#updateConfiguration");
        try {
            synchronized (this.mAccessLock) {
                if (compat != null) {
                    this.mDisplayAdjustments.setCompatibilityInfo(compat);
                }
                if (metrics != null) {
                    this.mMetrics.setTo(metrics);
                }
                this.mDisplayAdjustments.getCompatibilityInfo().applyToDisplayMetrics(this.mMetrics);
                int configChanges = calcConfigChanges(config);
                LocaleList locales = this.mConfiguration.getLocales();
                if (locales.isEmpty()) {
                    locales = LocaleList.getDefault();
                    this.mConfiguration.setLocales(locales);
                }
                if ((configChanges & 4) != 0 && locales.size() > 1) {
                    String[] availableLocales = this.mAssets.getNonSystemLocales();
                    if (LocaleList.isPseudoLocalesOnly(availableLocales)) {
                        availableLocales = this.mAssets.getLocales();
                        if (LocaleList.isPseudoLocalesOnly(availableLocales)) {
                            availableLocales = null;
                        }
                    }
                    if (availableLocales != null && (bestLocale = locales.getFirstMatchWithEnglishSupported(availableLocales)) != null && bestLocale != locales.get(0)) {
                        this.mConfiguration.setLocales(new LocaleList(bestLocale, locales));
                    }
                }
                if (this.mConfiguration.densityDpi != 0) {
                    this.mMetrics.densityDpi = this.mConfiguration.densityDpi;
                    this.mMetrics.density = this.mConfiguration.densityDpi * 0.00625f;
                }
                this.mMetrics.scaledDensity = this.mMetrics.density * (this.mConfiguration.fontScale != 0.0f ? this.mConfiguration.fontScale : 1.0f);
                if (this.mMetrics.widthPixels >= this.mMetrics.heightPixels) {
                    width = this.mMetrics.widthPixels;
                    i = this.mMetrics.heightPixels;
                } else {
                    width = this.mMetrics.heightPixels;
                    i = this.mMetrics.widthPixels;
                }
                int height = i;
                if (this.mConfiguration.keyboardHidden == 1 && this.mConfiguration.hardKeyboardHidden == 2) {
                    i2 = 3;
                } else {
                    i2 = this.mConfiguration.keyboardHidden;
                }
                int keyboardHidden = i2;
                this.mAssets.setConfiguration(this.mConfiguration.mcc, this.mConfiguration.mnc, adjustLanguageTag(this.mConfiguration.getLocales().get(0).toLanguageTag()), this.mConfiguration.orientation, this.mConfiguration.touchscreen, this.mConfiguration.densityDpi, this.mConfiguration.keyboard, keyboardHidden, this.mConfiguration.navigation, width, height, this.mConfiguration.smallestScreenWidthDp, this.mConfiguration.screenWidthDp, this.mConfiguration.screenHeightDp, this.mConfiguration.screenLayout, this.mConfiguration.uiMode, this.mConfiguration.colorMode, Build.VERSION.RESOURCES_SDK_INT);
                this.mDrawableCache.onConfigurationChange(configChanges);
                this.mColorDrawableCache.onConfigurationChange(configChanges);
                this.mComplexColorCache.onConfigurationChange(configChanges);
                this.mAnimatorCache.onConfigurationChange(configChanges);
                this.mStateListAnimatorCache.onConfigurationChange(configChanges);
                flushLayoutCache();
            }
            synchronized (sSync) {
                if (this.mPluralRule != null) {
                    this.mPluralRule = PluralRules.forLocale(this.mConfiguration.getLocales().get(0));
                }
            }
        } finally {
            Trace.traceEnd(8192L);
        }
    }

    public synchronized int calcConfigChanges(Configuration config) {
        if (config == null) {
            return -1;
        }
        this.mTmpConfig.setTo(config);
        int density = config.densityDpi;
        if (density == 0) {
            density = this.mMetrics.noncompatDensityDpi;
        }
        this.mDisplayAdjustments.getCompatibilityInfo().applyToConfiguration(density, this.mTmpConfig);
        if (this.mTmpConfig.getLocales().isEmpty()) {
            this.mTmpConfig.setLocales(LocaleList.getDefault());
        }
        return this.mConfiguration.updateFrom(this.mTmpConfig);
    }

    private static synchronized String adjustLanguageTag(String languageTag) {
        String language;
        String remainder;
        int separator = languageTag.indexOf(45);
        if (separator == -1) {
            language = languageTag;
            remainder = "";
        } else {
            language = languageTag.substring(0, separator);
            remainder = languageTag.substring(separator);
        }
        return Locale.adjustLanguageCode(language) + remainder;
    }

    public synchronized void flushLayoutCache() {
        synchronized (this.mCachedXmlBlocks) {
            Arrays.fill(this.mCachedXmlBlockCookies, 0);
            Arrays.fill(this.mCachedXmlBlockFiles, (Object) null);
            XmlBlock[] cachedXmlBlocks = this.mCachedXmlBlocks;
            for (int i = 0; i < 4; i++) {
                XmlBlock oldBlock = cachedXmlBlocks[i];
                if (oldBlock != null) {
                    oldBlock.close();
                }
            }
            Arrays.fill(cachedXmlBlocks, (Object) null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00e5  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0106 A[Catch: Exception -> 0x0139, TRY_LEAVE, TryCatch #1 {Exception -> 0x0139, blocks: (B:20:0x0043, B:22:0x0049, B:24:0x004f, B:26:0x0063, B:29:0x006d, B:31:0x0073, B:34:0x007b, B:38:0x0098, B:40:0x009c, B:42:0x00a0, B:44:0x00a6, B:46:0x00ac, B:47:0x00ce, B:52:0x00e1, B:57:0x00ea, B:63:0x00f9, B:65:0x0106, B:50:0x00d5, B:51:0x00dd, B:35:0x0084, B:25:0x0056), top: B:89:0x0043 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized android.graphics.drawable.Drawable loadDrawable(android.content.res.Resources r26, android.util.TypedValue r27, int r28, int r29, android.content.res.Resources.Theme r30) throws android.content.res.Resources.NotFoundException {
        /*
            Method dump skipped, instructions count: 365
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.res.ResourcesImpl.loadDrawable(android.content.res.Resources, android.util.TypedValue, int, int, android.content.res.Resources$Theme):android.graphics.drawable.Drawable");
    }

    private synchronized void cacheDrawable(TypedValue value, boolean isColorDrawable, DrawableCache caches, Resources.Theme theme, boolean usesTheme, long key, Drawable dr) {
        Drawable.ConstantState cs = dr.getConstantState();
        if (cs == null) {
            return;
        }
        if (this.mPreloading) {
            int changingConfigs = cs.getChangingConfigurations();
            if (isColorDrawable) {
                if (verifyPreloadConfig(changingConfigs, 0, value.resourceId, "drawable")) {
                    sPreloadedColorDrawables.put(key, cs);
                    return;
                }
                return;
            } else if (verifyPreloadConfig(changingConfigs, 8192, value.resourceId, "drawable")) {
                if ((changingConfigs & 8192) == 0) {
                    sPreloadedDrawables[0].put(key, cs);
                    sPreloadedDrawables[1].put(key, cs);
                    return;
                }
                sPreloadedDrawables[this.mConfiguration.getLayoutDirection()].put(key, cs);
                return;
            } else {
                return;
            }
        }
        synchronized (this.mAccessLock) {
            caches.put(key, theme, cs, usesTheme);
        }
    }

    private synchronized boolean verifyPreloadConfig(int changingConfigurations, int allowVarying, int resourceId, String name) {
        String resName;
        if (((-1073745921) & changingConfigurations & (~allowVarying)) != 0) {
            try {
                resName = getResourceName(resourceId);
            } catch (Resources.NotFoundException e) {
                resName = "?";
            }
            Log.w(TAG, "Preloaded " + name + " resource #0x" + Integer.toHexString(resourceId) + " (" + resName + ") that varies with configuration!!");
            return false;
        }
        return true;
    }

    private synchronized Drawable decodeImageDrawable(AssetManager.AssetInputStream ais, Resources wrapper, TypedValue value) {
        ImageDecoder.Source src = new ImageDecoder.AssetInputStreamSource(ais, wrapper, value);
        try {
            return ImageDecoder.decodeDrawable(src, new ImageDecoder.OnHeaderDecodedListener() { // from class: android.content.res.-$$Lambda$ResourcesImpl$99dm2ENnzo9b0SIUjUj2Kl3pi90
                @Override // android.graphics.ImageDecoder.OnHeaderDecodedListener
                public final void onHeaderDecoded(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
                    imageDecoder.setAllocator(1);
                }
            });
        } catch (IOException e) {
            return null;
        }
    }

    private synchronized Drawable loadDrawableForCookie(Resources wrapper, TypedValue value, int id, int density) {
        Drawable dr;
        String name;
        if (value.string == null) {
            throw new Resources.NotFoundException("Resource \"" + getResourceName(id) + "\" (" + Integer.toHexString(id) + ") is not a Drawable (color or path): " + value);
        }
        String file = value.string.toString();
        long startTime = 0;
        int startBitmapCount = 0;
        long startBitmapSize = 0;
        int startDrawableCount = 0;
        if (TRACE_FOR_DETAILED_PRELOAD) {
            startTime = System.nanoTime();
            startBitmapCount = Bitmap.sPreloadTracingNumInstantiatedBitmaps;
            startBitmapSize = Bitmap.sPreloadTracingTotalBitmapsSize;
            startDrawableCount = sPreloadTracingNumLoadedDrawables;
        }
        int startBitmapCount2 = startBitmapCount;
        Trace.traceBegin(8192L, file);
        LookupStack stack = this.mLookupStack.get();
        try {
        } catch (Exception | StackOverflowError e) {
            e = e;
        }
        try {
            if (stack.contains(id)) {
                throw new Exception("Recursive reference in drawable");
            }
            stack.push(id);
            try {
                if (file.endsWith(".xml")) {
                    try {
                        XmlResourceParser rp = loadXmlResourceParser(file, id, value.assetCookie, "drawable");
                        dr = Drawable.createFromXmlForDensity(wrapper, rp, density, null);
                        rp.close();
                    } catch (Throwable th) {
                        th = th;
                        stack.pop();
                        throw th;
                    }
                } else {
                    InputStream is = this.mAssets.openNonAsset(value.assetCookie, file, 2);
                    AssetManager.AssetInputStream ais = (AssetManager.AssetInputStream) is;
                    dr = decodeImageDrawable(ais, wrapper, value);
                }
                Drawable dr2 = dr;
                stack.pop();
                Trace.traceEnd(8192L);
                if (TRACE_FOR_DETAILED_PRELOAD && (id >>> 24) == 1 && (name = getResourceName(id)) != null) {
                    long time = System.nanoTime() - startTime;
                    int loadedBitmapCount = Bitmap.sPreloadTracingNumInstantiatedBitmaps - startBitmapCount2;
                    long startTime2 = Bitmap.sPreloadTracingTotalBitmapsSize - startBitmapSize;
                    int loadedDrawables = sPreloadTracingNumLoadedDrawables - startDrawableCount;
                    sPreloadTracingNumLoadedDrawables++;
                    boolean isRoot = Process.myUid() == 0;
                    StringBuilder sb = new StringBuilder();
                    sb.append(isRoot ? "Preloaded FW drawable #" : "Loaded non-preloaded FW drawable #");
                    sb.append(Integer.toHexString(id));
                    sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                    sb.append(name);
                    sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                    sb.append(file);
                    sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                    sb.append(dr2.getClass().getCanonicalName());
                    sb.append(" #nested_drawables= ");
                    sb.append(loadedDrawables);
                    sb.append(" #bitmaps= ");
                    sb.append(loadedBitmapCount);
                    sb.append(" total_bitmap_size= ");
                    sb.append(startTime2);
                    sb.append(" in[us] ");
                    long loadedBitmapSize = time / 1000;
                    sb.append(loadedBitmapSize);
                    Log.d(TAG_PRELOAD, sb.toString());
                }
                return dr2;
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception | StackOverflowError e2) {
            e = e2;
            Trace.traceEnd(8192L);
            Resources.NotFoundException rnf = new Resources.NotFoundException("File " + file + " from drawable resource ID #0x" + Integer.toHexString(id));
            rnf.initCause(e);
            throw rnf;
        }
    }

    public synchronized Typeface loadFont(Resources wrapper, TypedValue value, int id) {
        if (value.string == null) {
            throw new Resources.NotFoundException("Resource \"" + getResourceName(id) + "\" (" + Integer.toHexString(id) + ") is not a Font: " + value);
        }
        String file = value.string.toString();
        if (file.startsWith("res/")) {
            Typeface cached = Typeface.findFromCache(this.mAssets, file);
            if (cached != null) {
                return cached;
            }
            Trace.traceBegin(8192L, file);
            try {
                try {
                    if (file.endsWith("xml")) {
                        XmlResourceParser rp = loadXmlResourceParser(file, id, value.assetCookie, "font");
                        FontResourcesParser.FamilyResourceEntry familyEntry = FontResourcesParser.parse(rp, wrapper);
                        if (familyEntry == null) {
                            return null;
                        }
                        return Typeface.createFromResources(familyEntry, this.mAssets, file);
                    }
                    return Typeface.createFromResources(this.mAssets, file, value.assetCookie);
                } catch (IOException e) {
                    Log.e(TAG, "Failed to read xml resource " + file, e);
                    return null;
                } catch (XmlPullParserException e2) {
                    Log.e(TAG, "Failed to parse xml resource " + file, e2);
                    return null;
                }
            } finally {
                Trace.traceEnd(8192L);
            }
        }
        return null;
    }

    private synchronized ComplexColor loadComplexColorFromName(Resources wrapper, Resources.Theme theme, TypedValue value, int id) {
        long key = (value.assetCookie << 32) | value.data;
        ConfigurationBoundResourceCache<ComplexColor> cache = this.mComplexColorCache;
        ComplexColor complexColor = null;
        if (this.mColorCacheEnabled) {
            ComplexColor complexColor2 = cache.getInstance(key, wrapper, theme);
            complexColor = complexColor2;
            if (complexColor != null) {
                return complexColor;
            }
        }
        ConstantState<ComplexColor> factory = sPreloadedComplexColors.get(key);
        if (factory != null) {
            ComplexColor complexColor3 = factory.newInstance(wrapper, theme);
            complexColor = complexColor3;
        }
        if (complexColor == null) {
            complexColor = loadComplexColorForCookie(wrapper, value, id, theme);
        }
        if (complexColor != null) {
            complexColor.setBaseChangingConfigurations(value.changingConfigurations);
            if (this.mPreloading) {
                if (verifyPreloadConfig(complexColor.getChangingConfigurations(), 0, value.resourceId, "color")) {
                    sPreloadedComplexColors.put(key, complexColor.getConstantState());
                }
            } else {
                cache.put(key, theme, complexColor.getConstantState());
            }
        }
        return complexColor;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ComplexColor loadComplexColor(Resources wrapper, TypedValue value, int id, Resources.Theme theme) {
        long key = (value.assetCookie << 32) | value.data;
        if (value.type >= 28 && value.type <= 31) {
            return getColorStateListFromInt(value, key);
        }
        String file = value.string.toString();
        if (file.endsWith(".xml")) {
            try {
                ComplexColor complexColor = loadComplexColorFromName(wrapper, theme, value, id);
                return complexColor;
            } catch (Exception e) {
                Resources.NotFoundException rnf = new Resources.NotFoundException("File " + file + " from complex color resource ID #0x" + Integer.toHexString(id));
                rnf.initCause(e);
                throw rnf;
            }
        }
        throw new Resources.NotFoundException("File " + file + " from drawable resource ID #0x" + Integer.toHexString(id) + ": .xml extension required");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ColorStateList loadColorStateList(Resources wrapper, TypedValue value, int id, Resources.Theme theme) throws Resources.NotFoundException {
        long key = (value.assetCookie << 32) | value.data;
        if (value.type >= 28 && value.type <= 31) {
            return getColorStateListFromInt(value, key);
        }
        ComplexColor complexColor = loadComplexColorFromName(wrapper, theme, value, id);
        if (complexColor != null && (complexColor instanceof ColorStateList)) {
            return (ColorStateList) complexColor;
        }
        throw new Resources.NotFoundException("Can't find ColorStateList from drawable resource ID #0x" + Integer.toHexString(id));
    }

    private synchronized ColorStateList getColorStateListFromInt(TypedValue value, long key) {
        ConstantState<ComplexColor> factory = sPreloadedComplexColors.get(key);
        if (factory != null) {
            return (ColorStateList) factory.newInstance();
        }
        ColorStateList csl = ColorStateList.valueOf(value.data);
        if (this.mPreloading && verifyPreloadConfig(value.changingConfigurations, 0, value.resourceId, "color")) {
            sPreloadedComplexColors.put(key, csl.getConstantState());
        }
        return csl;
    }

    private synchronized ComplexColor loadComplexColorForCookie(Resources wrapper, TypedValue value, int id, Resources.Theme theme) {
        int type;
        if (value.string == null) {
            throw new UnsupportedOperationException("Can't convert to ComplexColor: type=0x" + value.type);
        }
        String file = value.string.toString();
        ComplexColor complexColor = null;
        Trace.traceBegin(8192L, file);
        if (file.endsWith(".xml")) {
            try {
                XmlResourceParser parser = loadXmlResourceParser(file, id, value.assetCookie, "ComplexColor");
                AttributeSet attrs = Xml.asAttributeSet(parser);
                while (true) {
                    type = parser.next();
                    if (type == 2 || type == 1) {
                        break;
                    }
                }
                if (type != 2) {
                    throw new XmlPullParserException("No start tag found");
                }
                String name = parser.getName();
                if (name.equals("gradient")) {
                    complexColor = GradientColor.createFromXmlInner(wrapper, parser, attrs, theme);
                } else if (name.equals("selector")) {
                    complexColor = ColorStateList.createFromXmlInner(wrapper, parser, attrs, theme);
                }
                parser.close();
                Trace.traceEnd(8192L);
                return complexColor;
            } catch (Exception e) {
                Trace.traceEnd(8192L);
                Resources.NotFoundException rnf = new Resources.NotFoundException("File " + file + " from ComplexColor resource ID #0x" + Integer.toHexString(id));
                rnf.initCause(e);
                throw rnf;
            }
        }
        Trace.traceEnd(8192L);
        throw new Resources.NotFoundException("File " + file + " from drawable resource ID #0x" + Integer.toHexString(id) + ": .xml extension required");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized XmlResourceParser loadXmlResourceParser(String file, int id, int assetCookie, String type) throws Resources.NotFoundException {
        if (id != 0) {
            try {
                synchronized (this.mCachedXmlBlocks) {
                    int[] cachedXmlBlockCookies = this.mCachedXmlBlockCookies;
                    String[] cachedXmlBlockFiles = this.mCachedXmlBlockFiles;
                    XmlBlock[] cachedXmlBlocks = this.mCachedXmlBlocks;
                    int num = cachedXmlBlockFiles.length;
                    for (int i = 0; i < num; i++) {
                        if (cachedXmlBlockCookies[i] == assetCookie && cachedXmlBlockFiles[i] != null && cachedXmlBlockFiles[i].equals(file)) {
                            return cachedXmlBlocks[i].newParser();
                        }
                    }
                    XmlBlock block = this.mAssets.openXmlBlockAsset(assetCookie, file);
                    if (block != null) {
                        int pos = (this.mLastCachedXmlBlockIndex + 1) % num;
                        this.mLastCachedXmlBlockIndex = pos;
                        XmlBlock oldBlock = cachedXmlBlocks[pos];
                        if (oldBlock != null) {
                            oldBlock.close();
                        }
                        cachedXmlBlockCookies[pos] = assetCookie;
                        cachedXmlBlockFiles[pos] = file;
                        cachedXmlBlocks[pos] = block;
                        return block.newParser();
                    }
                }
            } catch (Exception e) {
                Resources.NotFoundException rnf = new Resources.NotFoundException("File " + file + " from xml type " + type + " resource ID #0x" + Integer.toHexString(id));
                rnf.initCause(e);
                throw rnf;
            }
        }
        throw new Resources.NotFoundException("File " + file + " from xml type " + type + " resource ID #0x" + Integer.toHexString(id));
    }

    public final synchronized void startPreloading() {
        synchronized (sSync) {
            if (sPreloaded) {
                throw new IllegalStateException("Resources already preloaded");
            }
            sPreloaded = true;
            this.mPreloading = true;
            this.mConfiguration.densityDpi = DisplayMetrics.DENSITY_DEVICE;
            updateConfiguration(null, null, null);
            if (TRACE_FOR_DETAILED_PRELOAD) {
                this.mPreloadTracingPreloadStartTime = SystemClock.uptimeMillis();
                this.mPreloadTracingStartBitmapSize = Bitmap.sPreloadTracingTotalBitmapsSize;
                this.mPreloadTracingStartBitmapCount = Bitmap.sPreloadTracingNumInstantiatedBitmaps;
                Log.d(TAG_PRELOAD, "Preload starting");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void finishPreloading() {
        if (this.mPreloading) {
            if (TRACE_FOR_DETAILED_PRELOAD) {
                long time = SystemClock.uptimeMillis() - this.mPreloadTracingPreloadStartTime;
                long size = Bitmap.sPreloadTracingTotalBitmapsSize - this.mPreloadTracingStartBitmapSize;
                long count = Bitmap.sPreloadTracingNumInstantiatedBitmaps - this.mPreloadTracingStartBitmapCount;
                Log.d(TAG_PRELOAD, "Preload finished, " + count + " bitmaps of " + size + " bytes in " + time + " ms");
            }
            this.mPreloading = false;
            flushLayoutCache();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized LongSparseArray<Drawable.ConstantState> getPreloadedDrawables() {
        return sPreloadedDrawables[0];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ThemeImpl newThemeImpl() {
        return new ThemeImpl();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ThemeImpl newThemeImpl(Resources.ThemeKey key) {
        ThemeImpl impl = new ThemeImpl();
        impl.mKey.setTo(key);
        impl.rebase();
        return impl;
    }

    /* loaded from: classes.dex */
    public class ThemeImpl {
        private final AssetManager mAssets;
        private final long mTheme;
        private final Resources.ThemeKey mKey = new Resources.ThemeKey();
        private int mThemeResId = 0;

        ThemeImpl() {
            this.mAssets = ResourcesImpl.this.mAssets;
            this.mTheme = this.mAssets.createTheme();
        }

        protected void finalize() throws Throwable {
            super.finalize();
            this.mAssets.releaseTheme(this.mTheme);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized Resources.ThemeKey getKey() {
            return this.mKey;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized long getNativeTheme() {
            return this.mTheme;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized int getAppliedStyleResId() {
            return this.mThemeResId;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized void applyStyle(int resId, boolean force) {
            synchronized (this.mKey) {
                this.mAssets.applyStyleToTheme(this.mTheme, resId, force);
                this.mThemeResId = resId;
                this.mKey.append(resId, force);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized void setTo(ThemeImpl other) {
            synchronized (this.mKey) {
                synchronized (other.mKey) {
                    AssetManager.nativeThemeCopy(this.mTheme, other.mTheme);
                    this.mThemeResId = other.mThemeResId;
                    this.mKey.setTo(other.getKey());
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized TypedArray obtainStyledAttributes(Resources.Theme wrapper, AttributeSet set, int[] attrs, int defStyleAttr, int defStyleRes) {
            synchronized (this.mKey) {
                try {
                    try {
                        int len = attrs.length;
                        TypedArray array = TypedArray.obtain(wrapper.getResources(), len);
                        XmlBlock.Parser parser = (XmlBlock.Parser) set;
                        this.mAssets.applyStyle(this.mTheme, defStyleAttr, defStyleRes, parser, attrs, array.mDataAddress, array.mIndicesAddress);
                        array.mTheme = wrapper;
                        array.mXml = parser;
                        return array;
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized TypedArray resolveAttributes(Resources.Theme wrapper, int[] values, int[] attrs) {
            TypedArray array;
            synchronized (this.mKey) {
                int len = attrs.length;
                if (values == null || len != values.length) {
                    throw new IllegalArgumentException("Base attribute values must the same length as attrs");
                }
                array = TypedArray.obtain(wrapper.getResources(), len);
                this.mAssets.resolveAttrs(this.mTheme, 0, 0, values, attrs, array.mData, array.mIndices);
                array.mTheme = wrapper;
                array.mXml = null;
            }
            return array;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized boolean resolveAttribute(int resid, TypedValue outValue, boolean resolveRefs) {
            boolean themeValue;
            synchronized (this.mKey) {
                themeValue = this.mAssets.getThemeValue(this.mTheme, resid, outValue, resolveRefs);
            }
            return themeValue;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized int[] getAllAttributes() {
            return this.mAssets.getStyleAttributes(getAppliedStyleResId());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized int getChangingConfigurations() {
            int activityInfoConfigNativeToJava;
            synchronized (this.mKey) {
                int nativeChangingConfig = AssetManager.nativeThemeGetChangingConfigurations(this.mTheme);
                activityInfoConfigNativeToJava = ActivityInfo.activityInfoConfigNativeToJava(nativeChangingConfig);
            }
            return activityInfoConfigNativeToJava;
        }

        public synchronized void dump(int priority, String tag, String prefix) {
            synchronized (this.mKey) {
                this.mAssets.dumpTheme(this.mTheme, priority, tag, prefix);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized String[] getTheme() {
            String[] themes;
            synchronized (this.mKey) {
                int N = this.mKey.mCount;
                themes = new String[N * 2];
                int i = 0;
                int j = N - 1;
                while (i < themes.length) {
                    int resId = this.mKey.mResId[j];
                    boolean forced = this.mKey.mForce[j];
                    try {
                        themes[i] = ResourcesImpl.this.getResourceName(resId);
                    } catch (Resources.NotFoundException e) {
                        themes[i] = Integer.toHexString(i);
                    }
                    themes[i + 1] = forced ? "forced" : "not forced";
                    i += 2;
                    j--;
                }
            }
            return themes;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized void rebase() {
            synchronized (this.mKey) {
                AssetManager.nativeThemeClear(this.mTheme);
                for (int i = 0; i < this.mKey.mCount; i++) {
                    int resId = this.mKey.mResId[i];
                    boolean force = this.mKey.mForce[i];
                    this.mAssets.applyStyleToTheme(this.mTheme, resId, force);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class LookupStack {
        private int[] mIds;
        private int mSize;

        private synchronized LookupStack() {
            this.mIds = new int[4];
            this.mSize = 0;
        }

        public synchronized void push(int id) {
            this.mIds = GrowingArrayUtils.append(this.mIds, this.mSize, id);
            this.mSize++;
        }

        public synchronized boolean contains(int id) {
            for (int i = 0; i < this.mSize; i++) {
                if (this.mIds[i] == id) {
                    return true;
                }
            }
            return false;
        }

        public synchronized void pop() {
            this.mSize--;
        }
    }

    public void setColorCacheEnabled(boolean enabled) {
        this.mColorCacheEnabled = enabled;
    }

    public void setDrawableCacheEnabled(boolean enabled) {
        this.mDrawableCacheEnabled = enabled;
    }
}
