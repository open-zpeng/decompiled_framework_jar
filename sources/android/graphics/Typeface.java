package android.graphics;

import android.annotation.UnsupportedAppUsage;
import android.content.res.AssetManager;
import android.content.res.FontResourcesParser;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.graphics.fonts.FontStyle;
import android.graphics.fonts.FontVariationAxis;
import android.graphics.fonts.SystemFonts;
import android.media.tv.TvContract;
import android.os.ParcelFileDescriptor;
import android.provider.FontRequest;
import android.provider.FontsContract;
import android.text.FontConfig;
import android.util.Base64;
import android.util.LongSparseArray;
import android.util.LruCache;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.content.NativeLibraryHelper;
import com.android.internal.util.Preconditions;
import dalvik.annotation.optimization.CriticalNative;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import libcore.util.NativeAllocationRegistry;

/* loaded from: classes.dex */
public class Typeface {
    public static final int BOLD = 1;
    public static final int BOLD_ITALIC = 3;
    public static final Typeface DEFAULT;
    public static final Typeface DEFAULT_BOLD;
    private static final String DEFAULT_FAMILY = "sans-serif";
    public static final int ITALIC = 2;
    public static final Typeface MONOSPACE;
    public static final int NORMAL = 0;
    public static final int RESOLVE_BY_FONT_TABLE = -1;
    public static final Typeface SANS_SERIF;
    public static final Typeface SERIF;
    private static final int STYLE_ITALIC = 1;
    public static final int STYLE_MASK = 3;
    private static final int STYLE_NORMAL = 0;
    static Typeface sDefaultTypeface;
    @UnsupportedAppUsage(trackingBug = 123769446)
    static Typeface[] sDefaults;
    @UnsupportedAppUsage(trackingBug = 123769347)
    static final Map<String, Typeface> sSystemFontMap;
    @UnsupportedAppUsage
    private int mStyle;
    private int[] mSupportedAxes;
    private int mWeight;
    @UnsupportedAppUsage
    public long native_instance;
    private static String TAG = "Typeface";
    private static final NativeAllocationRegistry sRegistry = NativeAllocationRegistry.createMalloced(Typeface.class.getClassLoader(), nativeGetReleaseFunc());
    @GuardedBy({"sStyledCacheLock"})
    private static final LongSparseArray<SparseArray<Typeface>> sStyledTypefaceCache = new LongSparseArray<>(3);
    private static final Object sStyledCacheLock = new Object();
    @GuardedBy({"sWeightCacheLock"})
    private static final LongSparseArray<SparseArray<Typeface>> sWeightTypefaceCache = new LongSparseArray<>(3);
    private static final Object sWeightCacheLock = new Object();
    @GuardedBy({"sDynamicCacheLock"})
    private static final LruCache<String, Typeface> sDynamicTypefaceCache = new LruCache<>(16);
    private static final Object sDynamicCacheLock = new Object();
    @UnsupportedAppUsage(trackingBug = 123768928)
    @Deprecated
    static final Map<String, FontFamily[]> sSystemFallbackMap = Collections.emptyMap();
    private static final int[] EMPTY_AXES = new int[0];

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Style {
    }

    /* JADX INFO: Access modifiers changed from: private */
    @UnsupportedAppUsage
    public static native long nativeCreateFromArray(long[] jArr, int i, int i2);

    private static native long nativeCreateFromTypeface(long j, int i);

    private static native long nativeCreateFromTypefaceWithExactStyle(long j, int i, boolean z);

    private static native long nativeCreateFromTypefaceWithVariation(long j, List<FontVariationAxis> list);

    @UnsupportedAppUsage
    private static native long nativeCreateWeightAlias(long j, int i);

    @CriticalNative
    private static native long nativeGetReleaseFunc();

    @CriticalNative
    private static native int nativeGetStyle(long j);

    private static native int[] nativeGetSupportedAxes(long j);

    @CriticalNative
    private static native int nativeGetWeight(long j);

    private static native void nativeRegisterGenericFamily(String str, long j);

    @CriticalNative
    private static native void nativeSetDefault(long j);

    static {
        HashMap<String, Typeface> systemFontMap = new HashMap<>();
        initSystemDefaultTypefaces(systemFontMap, SystemFonts.getRawSystemFallbackMap(), SystemFonts.getAliases());
        sSystemFontMap = Collections.unmodifiableMap(systemFontMap);
        if (sSystemFontMap.containsKey(DEFAULT_FAMILY)) {
            setDefault(sSystemFontMap.get(DEFAULT_FAMILY));
        }
        String str = null;
        DEFAULT = create(str, 0);
        DEFAULT_BOLD = create(str, 1);
        SANS_SERIF = create(DEFAULT_FAMILY, 0);
        SERIF = create("serif", 0);
        MONOSPACE = create("monospace", 0);
        sDefaults = new Typeface[]{DEFAULT, DEFAULT_BOLD, create(str, 2), create(str, 3)};
        String[] genericFamilies = {"serif", DEFAULT_FAMILY, "cursive", "fantasy", "monospace", "system-ui"};
        for (String genericFamily : genericFamilies) {
            registerGenericFamilyNative(genericFamily, systemFontMap.get(genericFamily));
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    private static void setDefault(Typeface t) {
        sDefaultTypeface = t;
        nativeSetDefault(t.native_instance);
    }

    public int getWeight() {
        return this.mWeight;
    }

    public int getStyle() {
        return this.mStyle;
    }

    public final boolean isBold() {
        return (this.mStyle & 1) != 0;
    }

    public final boolean isItalic() {
        return (this.mStyle & 2) != 0;
    }

    public static Typeface createFromResources(FontResourcesParser.FamilyResourceEntry entry, AssetManager mgr, String path) {
        Typeface typeface;
        FontFamily.Builder familyBuilder;
        int i;
        if (entry instanceof FontResourcesParser.ProviderResourceEntry) {
            FontResourcesParser.ProviderResourceEntry providerEntry = (FontResourcesParser.ProviderResourceEntry) entry;
            List<List<String>> givenCerts = providerEntry.getCerts();
            List<List<byte[]>> certs = new ArrayList<>();
            if (givenCerts != null) {
                for (int i2 = 0; i2 < givenCerts.size(); i2++) {
                    List<String> certSet = givenCerts.get(i2);
                    List<byte[]> byteArraySet = new ArrayList<>();
                    for (int j = 0; j < certSet.size(); j++) {
                        byteArraySet.add(Base64.decode(certSet.get(j), 0));
                    }
                    certs.add(byteArraySet);
                }
            }
            FontRequest request = new FontRequest(providerEntry.getAuthority(), providerEntry.getPackage(), providerEntry.getQuery(), certs);
            Typeface typeface2 = FontsContract.getFontSync(request);
            return typeface2 == null ? DEFAULT : typeface2;
        }
        Typeface typeface3 = findFromCache(mgr, path);
        if (typeface3 != null) {
            return typeface3;
        }
        FontResourcesParser.FontFamilyFilesResourceEntry filesEntry = (FontResourcesParser.FontFamilyFilesResourceEntry) entry;
        try {
            FontResourcesParser.FontFileResourceEntry[] entries = filesEntry.getEntries();
            int length = entries.length;
            familyBuilder = null;
            int i3 = 0;
            while (true) {
                i = 1;
                if (i3 >= length) {
                    break;
                }
                FontResourcesParser.FontFileResourceEntry fontFile = entries[i3];
                Font.Builder fontBuilder = new Font.Builder(mgr, fontFile.getFileName(), false, 0).setTtcIndex(fontFile.getTtcIndex()).setFontVariationSettings(fontFile.getVariationSettings());
                if (fontFile.getWeight() != -1) {
                    fontBuilder.setWeight(fontFile.getWeight());
                }
                if (fontFile.getItalic() != -1) {
                    if (fontFile.getItalic() != 1) {
                        i = 0;
                    }
                    fontBuilder.setSlant(i);
                }
                if (familyBuilder == null) {
                    familyBuilder = new FontFamily.Builder(fontBuilder.build());
                } else {
                    familyBuilder.addFont(fontBuilder.build());
                }
                i3++;
            }
        } catch (IOException e) {
            typeface = DEFAULT;
        } catch (IllegalArgumentException e2) {
            return null;
        }
        if (familyBuilder == null) {
            return DEFAULT;
        }
        android.graphics.fonts.FontFamily family = familyBuilder.build();
        FontStyle normal = new FontStyle(400, 0);
        Font bestFont = family.getFont(0);
        int bestScore = normal.getMatchScore(bestFont.getStyle());
        while (i < family.getSize()) {
            Font candidate = family.getFont(i);
            int score = normal.getMatchScore(candidate.getStyle());
            if (score < bestScore) {
                bestFont = candidate;
                bestScore = score;
            }
            i++;
        }
        typeface = new CustomFallbackBuilder(family).setStyle(bestFont.getStyle()).build();
        synchronized (sDynamicCacheLock) {
            String key = Builder.createAssetUid(mgr, path, 0, null, -1, -1, DEFAULT_FAMILY);
            sDynamicTypefaceCache.put(key, typeface);
        }
        return typeface;
    }

    public static Typeface findFromCache(AssetManager mgr, String path) {
        synchronized (sDynamicCacheLock) {
            String key = Builder.createAssetUid(mgr, path, 0, null, -1, -1, DEFAULT_FAMILY);
            Typeface typeface = sDynamicTypefaceCache.get(key);
            if (typeface != null) {
                return typeface;
            }
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        public static final int BOLD_WEIGHT = 700;
        public static final int NORMAL_WEIGHT = 400;
        private final AssetManager mAssetManager;
        private String mFallbackFamilyName;
        private final Font.Builder mFontBuilder;
        private int mItalic;
        private final String mPath;
        private int mWeight;

        public Builder(File path) {
            this.mWeight = -1;
            this.mItalic = -1;
            this.mFontBuilder = new Font.Builder(path);
            this.mAssetManager = null;
            this.mPath = null;
        }

        public Builder(FileDescriptor fd) {
            Font.Builder builder;
            this.mWeight = -1;
            this.mItalic = -1;
            try {
                builder = new Font.Builder(ParcelFileDescriptor.dup(fd));
            } catch (IOException e) {
                builder = null;
            }
            this.mFontBuilder = builder;
            this.mAssetManager = null;
            this.mPath = null;
        }

        public Builder(String path) {
            this.mWeight = -1;
            this.mItalic = -1;
            this.mFontBuilder = new Font.Builder(new File(path));
            this.mAssetManager = null;
            this.mPath = null;
        }

        public Builder(AssetManager assetManager, String path) {
            this(assetManager, path, true, 0);
        }

        public Builder(AssetManager assetManager, String path, boolean isAsset, int cookie) {
            this.mWeight = -1;
            this.mItalic = -1;
            this.mFontBuilder = new Font.Builder(assetManager, path, isAsset, cookie);
            this.mAssetManager = assetManager;
            this.mPath = path;
        }

        public Builder setWeight(int weight) {
            this.mWeight = weight;
            this.mFontBuilder.setWeight(weight);
            return this;
        }

        public Builder setItalic(boolean italic) {
            this.mItalic = italic ? 1 : 0;
            this.mFontBuilder.setSlant(this.mItalic);
            return this;
        }

        public Builder setTtcIndex(int ttcIndex) {
            this.mFontBuilder.setTtcIndex(ttcIndex);
            return this;
        }

        public Builder setFontVariationSettings(String variationSettings) {
            this.mFontBuilder.setFontVariationSettings(variationSettings);
            return this;
        }

        public Builder setFontVariationSettings(FontVariationAxis[] axes) {
            this.mFontBuilder.setFontVariationSettings(axes);
            return this;
        }

        public Builder setFallback(String familyName) {
            this.mFallbackFamilyName = familyName;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static String createAssetUid(AssetManager mgr, String path, int ttcIndex, FontVariationAxis[] axes, int weight, int italic, String fallback) {
            SparseArray<String> pkgs = mgr.getAssignedPackageIdentifiers();
            StringBuilder builder = new StringBuilder();
            int size = pkgs.size();
            for (int i = 0; i < size; i++) {
                builder.append(pkgs.valueAt(i));
                builder.append(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
            }
            builder.append(path);
            builder.append(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
            builder.append(Integer.toString(ttcIndex));
            builder.append(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
            builder.append(Integer.toString(weight));
            builder.append(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
            builder.append(Integer.toString(italic));
            builder.append("--");
            builder.append(fallback);
            builder.append("--");
            if (axes != null) {
                for (FontVariationAxis axis : axes) {
                    builder.append(axis.getTag());
                    builder.append(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
                    builder.append(Float.toString(axis.getStyleValue()));
                }
            }
            return builder.toString();
        }

        private Typeface resolveFallbackTypeface() {
            String str = this.mFallbackFamilyName;
            if (str != null) {
                Typeface base = Typeface.getSystemDefaultTypeface(str);
                if (this.mWeight == -1 && this.mItalic == -1) {
                    return base;
                }
                int weight = this.mWeight;
                if (weight == -1) {
                    weight = base.mWeight;
                }
                int i = this.mItalic;
                boolean z = false;
                if (i != -1 ? i == 1 : (base.mStyle & 2) != 0) {
                    z = true;
                }
                boolean italic = z;
                return Typeface.createWeightStyle(base, weight, italic);
            }
            return null;
        }

        public Typeface build() {
            String key;
            Font.Builder builder = this.mFontBuilder;
            if (builder == null) {
                return resolveFallbackTypeface();
            }
            try {
                Font font = builder.build();
                if (this.mAssetManager == null) {
                    key = null;
                } else {
                    key = createAssetUid(this.mAssetManager, this.mPath, font.getTtcIndex(), font.getAxes(), this.mWeight, this.mItalic, this.mFallbackFamilyName == null ? Typeface.DEFAULT_FAMILY : this.mFallbackFamilyName);
                }
                if (key != null) {
                    synchronized (Typeface.sDynamicCacheLock) {
                        Typeface typeface = (Typeface) Typeface.sDynamicTypefaceCache.get(key);
                        if (typeface != null) {
                            return typeface;
                        }
                    }
                }
                android.graphics.fonts.FontFamily family = new FontFamily.Builder(font).build();
                int weight = this.mWeight == -1 ? font.getStyle().getWeight() : this.mWeight;
                int slant = this.mItalic == -1 ? font.getStyle().getSlant() : this.mItalic;
                CustomFallbackBuilder builder2 = new CustomFallbackBuilder(family).setStyle(new FontStyle(weight, slant));
                if (this.mFallbackFamilyName != null) {
                    builder2.setSystemFallback(this.mFallbackFamilyName);
                }
                Typeface typeface2 = builder2.build();
                if (key != null) {
                    synchronized (Typeface.sDynamicCacheLock) {
                        Typeface.sDynamicTypefaceCache.put(key, typeface2);
                    }
                }
                return typeface2;
            } catch (IOException | IllegalArgumentException e) {
                return resolveFallbackTypeface();
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class CustomFallbackBuilder {
        private static final int MAX_CUSTOM_FALLBACK = 64;
        private FontStyle mStyle;
        private final ArrayList<android.graphics.fonts.FontFamily> mFamilies = new ArrayList<>();
        private String mFallbackName = null;

        public static int getMaxCustomFallbackCount() {
            return 64;
        }

        public CustomFallbackBuilder(android.graphics.fonts.FontFamily family) {
            Preconditions.checkNotNull(family);
            this.mFamilies.add(family);
        }

        public CustomFallbackBuilder setSystemFallback(String familyName) {
            Preconditions.checkNotNull(familyName);
            this.mFallbackName = familyName;
            return this;
        }

        public CustomFallbackBuilder setStyle(FontStyle style) {
            this.mStyle = style;
            return this;
        }

        public CustomFallbackBuilder addCustomFallback(android.graphics.fonts.FontFamily family) {
            Preconditions.checkNotNull(family);
            boolean z = this.mFamilies.size() < getMaxCustomFallbackCount();
            Preconditions.checkArgument(z, "Custom fallback limit exceeded(" + getMaxCustomFallbackCount() + ")");
            this.mFamilies.add(family);
            return this;
        }

        public Typeface build() {
            int userFallbackSize = this.mFamilies.size();
            android.graphics.fonts.FontFamily[] fallback = SystemFonts.getSystemFallback(this.mFallbackName);
            long[] ptrArray = new long[fallback.length + userFallbackSize];
            for (int i = 0; i < userFallbackSize; i++) {
                ptrArray[i] = this.mFamilies.get(i).getNativePtr();
            }
            for (int i2 = 0; i2 < fallback.length; i2++) {
                ptrArray[i2 + userFallbackSize] = fallback[i2].getNativePtr();
            }
            FontStyle fontStyle = this.mStyle;
            int weight = fontStyle == null ? 400 : fontStyle.getWeight();
            FontStyle fontStyle2 = this.mStyle;
            int italic = (fontStyle2 == null || fontStyle2.getSlant() == 0) ? 0 : 1;
            return new Typeface(Typeface.nativeCreateFromArray(ptrArray, weight, italic));
        }
    }

    public static Typeface create(String familyName, int style) {
        return create(getSystemDefaultTypeface(familyName), style);
    }

    public static Typeface create(Typeface family, int style) {
        if ((style & (-4)) != 0) {
            style = 0;
        }
        if (family == null) {
            family = sDefaultTypeface;
        }
        if (family.mStyle == style) {
            return family;
        }
        long ni = family.native_instance;
        synchronized (sStyledCacheLock) {
            SparseArray<Typeface> styles = sStyledTypefaceCache.get(ni);
            if (styles == null) {
                styles = new SparseArray<>(4);
                sStyledTypefaceCache.put(ni, styles);
            } else {
                Typeface typeface = styles.get(style);
                if (typeface != null) {
                    return typeface;
                }
            }
            Typeface typeface2 = new Typeface(nativeCreateFromTypeface(ni, style));
            styles.put(style, typeface2);
            return typeface2;
        }
    }

    public static Typeface create(Typeface family, int weight, boolean italic) {
        Preconditions.checkArgumentInRange(weight, 0, 1000, TvContract.PreviewPrograms.COLUMN_WEIGHT);
        if (family == null) {
            family = sDefaultTypeface;
        }
        return createWeightStyle(family, weight, italic);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Typeface createWeightStyle(Typeface base, int weight, boolean italic) {
        int key = (weight << 1) | (italic ? 1 : 0);
        synchronized (sWeightCacheLock) {
            SparseArray<Typeface> innerCache = sWeightTypefaceCache.get(base.native_instance);
            if (innerCache == null) {
                innerCache = new SparseArray<>(4);
                sWeightTypefaceCache.put(base.native_instance, innerCache);
            } else {
                Typeface typeface = innerCache.get(key);
                if (typeface != null) {
                    return typeface;
                }
            }
            Typeface typeface2 = new Typeface(nativeCreateFromTypefaceWithExactStyle(base.native_instance, weight, italic));
            innerCache.put(key, typeface2);
            return typeface2;
        }
    }

    public static Typeface createFromTypefaceWithVariation(Typeface family, List<FontVariationAxis> axes) {
        Typeface base = family == null ? DEFAULT : family;
        return new Typeface(nativeCreateFromTypefaceWithVariation(base.native_instance, axes));
    }

    public static Typeface defaultFromStyle(int style) {
        return sDefaults[style];
    }

    public static Typeface createFromAsset(AssetManager mgr, String path) {
        Preconditions.checkNotNull(path);
        Preconditions.checkNotNull(mgr);
        Typeface typeface = new Builder(mgr, path).build();
        if (typeface != null) {
            return typeface;
        }
        try {
            InputStream inputStream = mgr.open(path);
            if (inputStream != null) {
                inputStream.close();
            }
            return DEFAULT;
        } catch (IOException e) {
            throw new RuntimeException("Font asset not found " + path);
        }
    }

    private static String createProviderUid(String authority, String query) {
        return "provider:" + authority + NativeLibraryHelper.CLEAR_ABI_OVERRIDE + query;
    }

    public static Typeface createFromFile(File file) {
        Typeface typeface = new Builder(file).build();
        if (typeface != null) {
            return typeface;
        }
        if (!file.exists()) {
            throw new RuntimeException("Font asset not found " + file.getAbsolutePath());
        }
        return DEFAULT;
    }

    public static Typeface createFromFile(String path) {
        Preconditions.checkNotNull(path);
        return createFromFile(new File(path));
    }

    @UnsupportedAppUsage(trackingBug = 123768928)
    @Deprecated
    private static Typeface createFromFamilies(FontFamily[] families) {
        long[] ptrArray = new long[families.length];
        for (int i = 0; i < families.length; i++) {
            ptrArray[i] = families[i].mNativePtr;
        }
        return new Typeface(nativeCreateFromArray(ptrArray, -1, -1));
    }

    private static Typeface createFromFamilies(android.graphics.fonts.FontFamily[] families) {
        long[] ptrArray = new long[families.length];
        for (int i = 0; i < families.length; i++) {
            ptrArray[i] = families[i].getNativePtr();
        }
        return new Typeface(nativeCreateFromArray(ptrArray, -1, -1));
    }

    @UnsupportedAppUsage(trackingBug = 123768395)
    @Deprecated
    private static Typeface createFromFamiliesWithDefault(FontFamily[] families, int weight, int italic) {
        return createFromFamiliesWithDefault(families, DEFAULT_FAMILY, weight, italic);
    }

    @UnsupportedAppUsage(trackingBug = 123768928)
    @Deprecated
    private static Typeface createFromFamiliesWithDefault(FontFamily[] families, String fallbackName, int weight, int italic) {
        android.graphics.fonts.FontFamily[] fallback = SystemFonts.getSystemFallback(fallbackName);
        long[] ptrArray = new long[families.length + fallback.length];
        for (int i = 0; i < families.length; i++) {
            ptrArray[i] = families[i].mNativePtr;
        }
        for (int i2 = 0; i2 < fallback.length; i2++) {
            ptrArray[families.length + i2] = fallback[i2].getNativePtr();
        }
        return new Typeface(nativeCreateFromArray(ptrArray, weight, italic));
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private Typeface(long ni) {
        this.mStyle = 0;
        this.mWeight = 0;
        if (ni == 0) {
            throw new RuntimeException("native typeface cannot be made");
        }
        this.native_instance = ni;
        sRegistry.registerNativeAllocation(this, this.native_instance);
        this.mStyle = nativeGetStyle(ni);
        this.mWeight = nativeGetWeight(ni);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Typeface getSystemDefaultTypeface(String familyName) {
        Typeface tf = sSystemFontMap.get(familyName);
        return tf == null ? DEFAULT : tf;
    }

    @VisibleForTesting
    public static void initSystemDefaultTypefaces(Map<String, Typeface> systemFontMap, Map<String, android.graphics.fonts.FontFamily[]> fallbacks, FontConfig.Alias[] aliases) {
        Typeface base;
        for (Map.Entry<String, android.graphics.fonts.FontFamily[]> entry : fallbacks.entrySet()) {
            systemFontMap.put(entry.getKey(), createFromFamilies(entry.getValue()));
        }
        for (FontConfig.Alias alias : aliases) {
            if (!systemFontMap.containsKey(alias.getName()) && (base = systemFontMap.get(alias.getToName())) != null) {
                int weight = alias.getWeight();
                Typeface newFace = weight == 400 ? base : new Typeface(nativeCreateWeightAlias(base.native_instance, weight));
                systemFontMap.put(alias.getName(), newFace);
            }
        }
    }

    private static void registerGenericFamilyNative(String familyName, Typeface typeface) {
        if (typeface != null) {
            nativeRegisterGenericFamily(familyName, typeface.native_instance);
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Typeface typeface = (Typeface) o;
        if (this.mStyle == typeface.mStyle && this.native_instance == typeface.native_instance) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        long j = this.native_instance;
        int result = (17 * 31) + ((int) (j ^ (j >>> 32)));
        return (result * 31) + this.mStyle;
    }

    public boolean isSupportedAxes(int axis) {
        if (this.mSupportedAxes == null) {
            synchronized (this) {
                if (this.mSupportedAxes == null) {
                    this.mSupportedAxes = nativeGetSupportedAxes(this.native_instance);
                    if (this.mSupportedAxes == null) {
                        this.mSupportedAxes = EMPTY_AXES;
                    }
                }
            }
        }
        return Arrays.binarySearch(this.mSupportedAxes, axis) >= 0;
    }
}
