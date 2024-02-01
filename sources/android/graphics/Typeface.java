package android.graphics;

import android.content.res.AssetManager;
import android.content.res.FontResourcesParser;
import android.graphics.fonts.FontVariationAxis;
import android.media.tv.TvContract;
import android.net.Uri;
import android.provider.FontRequest;
import android.provider.FontsContract;
import android.text.FontConfig;
import android.util.ArrayMap;
import android.util.Base64;
import android.util.Log;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import libcore.util.NativeAllocationRegistry;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class Typeface {
    public static final int BOLD = 1;
    public static final int BOLD_ITALIC = 3;
    public static final Typeface DEFAULT;
    public static final Typeface DEFAULT_BOLD;
    private static final String DEFAULT_FAMILY = "sans-serif";
    public static final int ITALIC = 2;
    public static final int MAX_WEIGHT = 1000;
    public static final Typeface MONOSPACE;
    public static final int NORMAL = 0;
    public static final int RESOLVE_BY_FONT_TABLE = -1;
    public static final Typeface SANS_SERIF;
    public static final Typeface SERIF;
    private static final int STYLE_ITALIC = 1;
    public static final int STYLE_MASK = 3;
    private static final int STYLE_NORMAL = 0;
    static Typeface sDefaultTypeface;
    public private protected static Typeface[] sDefaults;
    public private protected static final Map<String, FontFamily[]> sSystemFallbackMap;
    public private protected static final Map<String, Typeface> sSystemFontMap;
    public protected int mStyle;
    private int[] mSupportedAxes;
    private int mWeight;
    private protected long native_instance;
    private static String TAG = "Typeface";
    private static final NativeAllocationRegistry sRegistry = new NativeAllocationRegistry(Typeface.class.getClassLoader(), nativeGetReleaseFunc(), 64);
    @GuardedBy("sStyledCacheLock")
    private static final LongSparseArray<SparseArray<Typeface>> sStyledTypefaceCache = new LongSparseArray<>(3);
    private static final Object sStyledCacheLock = new Object();
    @GuardedBy("sWeightCacheLock")
    private static final LongSparseArray<SparseArray<Typeface>> sWeightTypefaceCache = new LongSparseArray<>(3);
    private static final Object sWeightCacheLock = new Object();
    @GuardedBy("sDynamicCacheLock")
    private static final LruCache<String, Typeface> sDynamicTypefaceCache = new LruCache<>(16);
    private static final Object sDynamicCacheLock = new Object();
    private static final int[] EMPTY_AXES = new int[0];

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Style {
    }

    public protected static native long nativeCreateFromArray(long[] jArr, int i, int i2);

    private static native long nativeCreateFromTypeface(long j, int i);

    private static native long nativeCreateFromTypefaceWithExactStyle(long j, int i, boolean z);

    private static native long nativeCreateFromTypefaceWithVariation(long j, List<FontVariationAxis> list);

    public protected static native long nativeCreateWeightAlias(long j, int i);

    @CriticalNative
    private static native long nativeGetReleaseFunc();

    @CriticalNative
    private static native int nativeGetStyle(long j);

    private static native int[] nativeGetSupportedAxes(long j);

    @CriticalNative
    private static native int nativeGetWeight(long j);

    @CriticalNative
    private static native void nativeSetDefault(long j);

    static {
        ArrayMap<String, Typeface> systemFontMap = new ArrayMap<>();
        ArrayMap<String, FontFamily[]> systemFallbackMap = new ArrayMap<>();
        buildSystemFallback("/system/etc/fonts.xml", "/system/fonts/", systemFontMap, systemFallbackMap);
        sSystemFontMap = Collections.unmodifiableMap(systemFontMap);
        sSystemFallbackMap = Collections.unmodifiableMap(systemFallbackMap);
        setDefault(sSystemFontMap.get(DEFAULT_FAMILY));
        String str = null;
        DEFAULT = create(str, 0);
        DEFAULT_BOLD = create(str, 1);
        SANS_SERIF = create(DEFAULT_FAMILY, 0);
        SERIF = create("serif", 0);
        MONOSPACE = create("monospace", 0);
        sDefaults = new Typeface[]{DEFAULT, DEFAULT_BOLD, create(str, 2), create(str, 3)};
    }

    public protected static void setDefault(Typeface t) {
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

    public static synchronized Typeface createFromResources(AssetManager mgr, String path, int cookie) {
        synchronized (sDynamicCacheLock) {
            String key = Builder.createAssetUid(mgr, path, 0, null, -1, -1, DEFAULT_FAMILY);
            Typeface typeface = sDynamicTypefaceCache.get(key);
            if (typeface != null) {
                return typeface;
            }
            FontFamily fontFamily = new FontFamily();
            if (fontFamily.addFontFromAssetManager(mgr, path, cookie, false, 0, -1, -1, null)) {
                if (fontFamily.freeze()) {
                    FontFamily[] families = {fontFamily};
                    Typeface typeface2 = createFromFamiliesWithDefault(families, DEFAULT_FAMILY, -1, -1);
                    sDynamicTypefaceCache.put(key, typeface2);
                    return typeface2;
                }
                return null;
            }
            return null;
        }
    }

    public static synchronized Typeface createFromResources(FontResourcesParser.FamilyResourceEntry entry, AssetManager mgr, String path) {
        if (entry instanceof FontResourcesParser.ProviderResourceEntry) {
            FontResourcesParser.ProviderResourceEntry providerEntry = (FontResourcesParser.ProviderResourceEntry) entry;
            List<List<String>> givenCerts = providerEntry.getCerts();
            List<List<byte[]>> certs = new ArrayList<>();
            if (givenCerts != null) {
                for (int i = 0; i < givenCerts.size(); i++) {
                    List<String> certSet = givenCerts.get(i);
                    List<byte[]> byteArraySet = new ArrayList<>();
                    for (int j = 0; j < certSet.size(); j++) {
                        byteArraySet.add(Base64.decode(certSet.get(j), 0));
                    }
                    certs.add(byteArraySet);
                }
            }
            FontRequest request = new FontRequest(providerEntry.getAuthority(), providerEntry.getPackage(), providerEntry.getQuery(), certs);
            Typeface typeface = FontsContract.getFontSync(request);
            return typeface == null ? DEFAULT : typeface;
        }
        Typeface typeface2 = findFromCache(mgr, path);
        if (typeface2 != null) {
            return typeface2;
        }
        FontResourcesParser.FontFamilyFilesResourceEntry filesEntry = (FontResourcesParser.FontFamilyFilesResourceEntry) entry;
        FontFamily fontFamily = new FontFamily();
        FontResourcesParser.FontFileResourceEntry[] entries = filesEntry.getEntries();
        int length = entries.length;
        int i2 = 0;
        while (i2 < length) {
            FontResourcesParser.FontFileResourceEntry fontFile = entries[i2];
            int i3 = i2;
            if (!fontFamily.addFontFromAssetManager(mgr, fontFile.getFileName(), 0, false, fontFile.getTtcIndex(), fontFile.getWeight(), fontFile.getItalic(), FontVariationAxis.fromFontVariationSettings(fontFile.getVariationSettings()))) {
                return null;
            }
            i2 = i3 + 1;
        }
        if (fontFamily.freeze()) {
            FontFamily[] familyChain = {fontFamily};
            Typeface typeface3 = createFromFamiliesWithDefault(familyChain, DEFAULT_FAMILY, -1, -1);
            synchronized (sDynamicCacheLock) {
                String key = Builder.createAssetUid(mgr, path, 0, null, -1, -1, DEFAULT_FAMILY);
                sDynamicTypefaceCache.put(key, typeface3);
            }
            return typeface3;
        }
        return null;
    }

    public static synchronized Typeface findFromCache(AssetManager mgr, String path) {
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
        private AssetManager mAssetManager;
        private FontVariationAxis[] mAxes;
        private String mFallbackFamilyName;
        private FileDescriptor mFd;
        private Map<Uri, ByteBuffer> mFontBuffers;
        private FontsContract.FontInfo[] mFonts;
        private String mPath;
        private int mTtcIndex;
        private int mWeight = -1;
        private int mItalic = -1;

        public Builder(File path) {
            this.mPath = path.getAbsolutePath();
        }

        public Builder(FileDescriptor fd) {
            this.mFd = fd;
        }

        public Builder(String path) {
            this.mPath = path;
        }

        public Builder(AssetManager assetManager, String path) {
            this.mAssetManager = (AssetManager) Preconditions.checkNotNull(assetManager);
            this.mPath = (String) Preconditions.checkStringNotEmpty(path);
        }

        public synchronized Builder(FontsContract.FontInfo[] fonts, Map<Uri, ByteBuffer> buffers) {
            this.mFonts = fonts;
            this.mFontBuffers = buffers;
        }

        public Builder setWeight(int weight) {
            this.mWeight = weight;
            return this;
        }

        public Builder setItalic(boolean italic) {
            this.mItalic = italic ? 1 : 0;
            return this;
        }

        public Builder setTtcIndex(int ttcIndex) {
            if (this.mFonts != null) {
                throw new IllegalArgumentException("TTC index can not be specified for FontResult source.");
            }
            this.mTtcIndex = ttcIndex;
            return this;
        }

        public Builder setFontVariationSettings(String variationSettings) {
            if (this.mFonts != null) {
                throw new IllegalArgumentException("Font variation settings can not be specified for FontResult source.");
            }
            if (this.mAxes != null) {
                throw new IllegalStateException("Font variation settings are already set.");
            }
            this.mAxes = FontVariationAxis.fromFontVariationSettings(variationSettings);
            return this;
        }

        public Builder setFontVariationSettings(FontVariationAxis[] axes) {
            if (this.mFonts != null) {
                throw new IllegalArgumentException("Font variation settings can not be specified for FontResult source.");
            }
            if (this.mAxes != null) {
                throw new IllegalStateException("Font variation settings are already set.");
            }
            this.mAxes = axes;
            return this;
        }

        public Builder setFallback(String familyName) {
            this.mFallbackFamilyName = familyName;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized String createAssetUid(AssetManager mgr, String path, int ttcIndex, FontVariationAxis[] axes, int weight, int italic, String fallback) {
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

        private synchronized Typeface resolveFallbackTypeface() {
            if (this.mFallbackFamilyName == null) {
                return null;
            }
            Typeface base = Typeface.sSystemFontMap.get(this.mFallbackFamilyName);
            if (base == null) {
                base = Typeface.sDefaultTypeface;
            }
            if (this.mWeight == -1 && this.mItalic == -1) {
                return base;
            }
            int weight = this.mWeight == -1 ? base.mWeight : this.mWeight;
            boolean z = false;
            if (this.mItalic != -1 ? this.mItalic == 1 : (base.mStyle & 2) != 0) {
                z = true;
            }
            boolean italic = z;
            return Typeface.createWeightStyle(base, weight, italic);
        }

        public Typeface build() {
            if (this.mFd != null) {
                try {
                    FileInputStream fis = new FileInputStream(this.mFd);
                    FileChannel channel = fis.getChannel();
                    long size = channel.size();
                    ByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0L, size);
                    FontFamily fontFamily = new FontFamily();
                    if (!fontFamily.addFontFromBuffer(buffer, this.mTtcIndex, this.mAxes, this.mWeight, this.mItalic)) {
                        fontFamily.abortCreation();
                        Typeface resolveFallbackTypeface = resolveFallbackTypeface();
                        fis.close();
                        return resolveFallbackTypeface;
                    } else if (!fontFamily.freeze()) {
                        Typeface resolveFallbackTypeface2 = resolveFallbackTypeface();
                        fis.close();
                        return resolveFallbackTypeface2;
                    } else {
                        FontFamily[] families = {fontFamily};
                        Typeface createFromFamiliesWithDefault = Typeface.createFromFamiliesWithDefault(families, this.mFallbackFamilyName, this.mWeight, this.mItalic);
                        fis.close();
                        return createFromFamiliesWithDefault;
                    }
                } catch (IOException e) {
                    return resolveFallbackTypeface();
                }
            } else if (this.mAssetManager != null) {
                String key = createAssetUid(this.mAssetManager, this.mPath, this.mTtcIndex, this.mAxes, this.mWeight, this.mItalic, this.mFallbackFamilyName);
                synchronized (Typeface.sDynamicCacheLock) {
                    Typeface typeface = (Typeface) Typeface.sDynamicTypefaceCache.get(key);
                    if (typeface != null) {
                        return typeface;
                    }
                    FontFamily fontFamily2 = new FontFamily();
                    if (!fontFamily2.addFontFromAssetManager(this.mAssetManager, this.mPath, this.mTtcIndex, true, this.mTtcIndex, this.mWeight, this.mItalic, this.mAxes)) {
                        fontFamily2.abortCreation();
                        return resolveFallbackTypeface();
                    } else if (!fontFamily2.freeze()) {
                        return resolveFallbackTypeface();
                    } else {
                        FontFamily[] families2 = {fontFamily2};
                        Typeface typeface2 = Typeface.createFromFamiliesWithDefault(families2, this.mFallbackFamilyName, this.mWeight, this.mItalic);
                        Typeface.sDynamicTypefaceCache.put(key, typeface2);
                        return typeface2;
                    }
                }
            } else if (this.mPath != null) {
                FontFamily fontFamily3 = new FontFamily();
                if (!fontFamily3.addFont(this.mPath, this.mTtcIndex, this.mAxes, this.mWeight, this.mItalic)) {
                    fontFamily3.abortCreation();
                    return resolveFallbackTypeface();
                } else if (fontFamily3.freeze()) {
                    FontFamily[] families3 = {fontFamily3};
                    return Typeface.createFromFamiliesWithDefault(families3, this.mFallbackFamilyName, this.mWeight, this.mItalic);
                } else {
                    return resolveFallbackTypeface();
                }
            } else if (this.mFonts != null) {
                FontFamily fontFamily4 = new FontFamily();
                FontsContract.FontInfo[] fontInfoArr = this.mFonts;
                int length = fontInfoArr.length;
                int i = 0;
                boolean atLeastOneFont = false;
                while (i < length) {
                    FontsContract.FontInfo font = fontInfoArr[i];
                    ByteBuffer fontBuffer = this.mFontBuffers.get(font.getUri());
                    if (fontBuffer != null) {
                        boolean success = fontFamily4.addFontFromBuffer(fontBuffer, font.getTtcIndex(), font.getAxes(), font.getWeight(), font.isItalic() ? 1 : 0);
                        if (!success) {
                            fontFamily4.abortCreation();
                            return null;
                        }
                        atLeastOneFont = true;
                    }
                    i++;
                    atLeastOneFont = atLeastOneFont;
                }
                if (!atLeastOneFont) {
                    fontFamily4.abortCreation();
                    return null;
                }
                fontFamily4.freeze();
                FontFamily[] families4 = {fontFamily4};
                return Typeface.createFromFamiliesWithDefault(families4, this.mFallbackFamilyName, this.mWeight, this.mItalic);
            } else {
                throw new IllegalArgumentException("No source was set.");
            }
        }
    }

    public static Typeface create(String familyName, int style) {
        return create(sSystemFontMap.get(familyName), style);
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
    public static synchronized Typeface createWeightStyle(Typeface base, int weight, boolean italic) {
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

    public static synchronized Typeface createFromTypefaceWithVariation(Typeface family, List<FontVariationAxis> axes) {
        long ni = family == null ? 0L : family.native_instance;
        return new Typeface(nativeCreateFromTypefaceWithVariation(ni, axes));
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

    private static synchronized String createProviderUid(String authority, String query) {
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

    public protected static Typeface createFromFamilies(FontFamily[] families) {
        long[] ptrArray = new long[families.length];
        for (int i = 0; i < families.length; i++) {
            ptrArray[i] = families[i].mNativePtr;
        }
        return new Typeface(nativeCreateFromArray(ptrArray, -1, -1));
    }

    public protected static Typeface createFromFamiliesWithDefault(FontFamily[] families, int weight, int italic) {
        return createFromFamiliesWithDefault(families, DEFAULT_FAMILY, weight, italic);
    }

    /* JADX INFO: Access modifiers changed from: public */
    public static Typeface createFromFamiliesWithDefault(FontFamily[] families, String fallbackName, int weight, int italic) {
        FontFamily[] fallback = sSystemFallbackMap.get(fallbackName);
        if (fallback == null) {
            fallback = sSystemFallbackMap.get(DEFAULT_FAMILY);
        }
        long[] ptrArray = new long[families.length + fallback.length];
        for (int i = 0; i < families.length; i++) {
            ptrArray[i] = families[i].mNativePtr;
        }
        for (int i2 = 0; i2 < fallback.length; i2++) {
            ptrArray[families.length + i2] = fallback[i2].mNativePtr;
        }
        return new Typeface(nativeCreateFromArray(ptrArray, weight, italic));
    }

    public protected Typeface(long ni) {
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

    private static synchronized ByteBuffer mmap(String fullPath) {
        try {
            FileInputStream file = new FileInputStream(fullPath);
            FileChannel fileChannel = file.getChannel();
            long fontSize = fileChannel.size();
            MappedByteBuffer map = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0L, fontSize);
            file.close();
            return map;
        } catch (IOException e) {
            String str = TAG;
            Log.e(str, "Error mapping font file " + fullPath);
            return null;
        }
    }

    private static synchronized FontFamily createFontFamily(String familyName, List<FontConfig.Font> fonts, String[] languageTags, int variant, Map<String, ByteBuffer> cache, String fontDir) {
        FontFamily family = new FontFamily(languageTags, variant);
        int i = 0;
        while (true) {
            int i2 = i;
            int i3 = fonts.size();
            if (i2 >= i3) {
                break;
            }
            FontConfig.Font font = fonts.get(i2);
            String fullPath = fontDir + font.getFontName();
            ByteBuffer buffer = cache.get(fullPath);
            if (buffer == null) {
                if (!cache.containsKey(fullPath)) {
                    buffer = mmap(fullPath);
                    cache.put(fullPath, buffer);
                    if (buffer == null) {
                    }
                }
                i = i2 + 1;
            }
            if (!family.addFontFromBuffer(buffer, font.getTtcIndex(), font.getAxes(), font.getWeight(), font.isItalic() ? 1 : 0)) {
                Log.e(TAG, "Error creating font " + fullPath + "#" + font.getTtcIndex());
            }
            i = i2 + 1;
        }
        if (family.freeze()) {
            return family;
        }
        Log.e(TAG, "Unable to load Family: " + familyName + " : " + Arrays.toString(languageTags));
        return null;
    }

    private static synchronized void pushFamilyToFallback(FontConfig.Family xmlFamily, ArrayMap<String, ArrayList<FontFamily>> fallbackMap, Map<String, ByteBuffer> cache, String fontDir) {
        FontConfig.Font[] fonts;
        String[] languageTags = xmlFamily.getLanguages();
        int variant = xmlFamily.getVariant();
        ArrayList<FontConfig.Font> defaultFonts = new ArrayList<>();
        ArrayMap<String, ArrayList<FontConfig.Font>> specificFallbackFonts = new ArrayMap<>();
        for (FontConfig.Font font : xmlFamily.getFonts()) {
            String fallbackName = font.getFallbackFor();
            if (fallbackName == null) {
                defaultFonts.add(font);
            } else {
                ArrayList<FontConfig.Font> fallback = specificFallbackFonts.get(fallbackName);
                if (fallback == null) {
                    fallback = new ArrayList<>();
                    specificFallbackFonts.put(fallbackName, fallback);
                }
                fallback.add(font);
            }
        }
        FontFamily defaultFamily = defaultFonts.isEmpty() ? null : createFontFamily(xmlFamily.getName(), defaultFonts, languageTags, variant, cache, fontDir);
        for (int i = 0; i < fallbackMap.size(); i++) {
            ArrayList<FontConfig.Font> fallback2 = specificFallbackFonts.get(fallbackMap.keyAt(i));
            if (fallback2 == null) {
                if (defaultFamily != null) {
                    fallbackMap.valueAt(i).add(defaultFamily);
                }
            } else {
                FontFamily family = createFontFamily(xmlFamily.getName(), fallback2, languageTags, variant, cache, fontDir);
                if (family != null) {
                    fallbackMap.valueAt(i).add(family);
                } else if (defaultFamily != null) {
                    fallbackMap.valueAt(i).add(defaultFamily);
                }
            }
        }
    }

    @VisibleForTesting
    public static synchronized void buildSystemFallback(String xmlPath, String fontDir, ArrayMap<String, Typeface> fontMap, ArrayMap<String, FontFamily[]> fallbackMap) {
        int i;
        FontConfig fontConfig;
        HashMap<String, ByteBuffer> bufferCache;
        FontFamily family;
        try {
            FileInputStream fontsIn = new FileInputStream(xmlPath);
            FontConfig fontConfig2 = FontListParser.parse(fontsIn);
            HashMap<String, ByteBuffer> bufferCache2 = new HashMap<>();
            FontConfig.Family[] xmlFamilies = fontConfig2.getFamilies();
            ArrayMap<String, ArrayList<FontFamily>> fallbackListMap = new ArrayMap<>();
            for (FontConfig.Family xmlFamily : xmlFamilies) {
                String familyName = xmlFamily.getName();
                if (familyName != null && (family = createFontFamily(xmlFamily.getName(), Arrays.asList(xmlFamily.getFonts()), xmlFamily.getLanguages(), xmlFamily.getVariant(), bufferCache2, fontDir)) != null) {
                    ArrayList<FontFamily> fallback = new ArrayList<>();
                    fallback.add(family);
                    fallbackListMap.put(familyName, fallback);
                }
            }
            while (i < xmlFamilies.length) {
                FontConfig.Family xmlFamily2 = xmlFamilies[i];
                i = (i == 0 || xmlFamily2.getName() == null) ? 0 : i + 1;
                pushFamilyToFallback(xmlFamily2, fallbackListMap, bufferCache2, fontDir);
            }
            for (int i2 = 0; i2 < fallbackListMap.size(); i2++) {
                String fallbackName = fallbackListMap.keyAt(i2);
                List<FontFamily> familyList = fallbackListMap.valueAt(i2);
                FontFamily[] families = (FontFamily[]) familyList.toArray(new FontFamily[familyList.size()]);
                try {
                    fallbackMap.put(fallbackName, families);
                    long[] ptrArray = new long[families.length];
                    int j = 0;
                    while (j < families.length) {
                        ptrArray[j] = families[j].mNativePtr;
                        j++;
                        familyList = familyList;
                    }
                    fontMap.put(fallbackName, new Typeface(nativeCreateFromArray(ptrArray, -1, -1)));
                } catch (FileNotFoundException e) {
                    e = e;
                    Log.e(TAG, "Error opening " + xmlPath, e);
                    return;
                } catch (IOException e2) {
                    e = e2;
                    Log.e(TAG, "Error reading " + xmlPath, e);
                    return;
                } catch (RuntimeException e3) {
                    e = e3;
                    Log.w(TAG, "Didn't create default family (most likely, non-Minikin build)", e);
                    return;
                } catch (XmlPullParserException e4) {
                    e = e4;
                    Log.e(TAG, "XML parse exception for " + xmlPath, e);
                    return;
                }
            }
            FontConfig.Alias[] aliases = fontConfig2.getAliases();
            int length = aliases.length;
            int i3 = 0;
            while (i3 < length) {
                FontConfig.Alias alias = aliases[i3];
                Typeface base = fontMap.get(alias.getToName());
                Typeface newFace = base;
                int weight = alias.getWeight();
                if (weight != 400) {
                    fontConfig = fontConfig2;
                    bufferCache = bufferCache2;
                    newFace = new Typeface(nativeCreateWeightAlias(base.native_instance, weight));
                } else {
                    fontConfig = fontConfig2;
                    bufferCache = bufferCache2;
                }
                fontMap.put(alias.getName(), newFace);
                i3++;
                fontConfig2 = fontConfig;
                bufferCache2 = bufferCache;
            }
        } catch (FileNotFoundException e5) {
            e = e5;
        } catch (IOException e6) {
            e = e6;
        } catch (RuntimeException e7) {
            e = e7;
        } catch (XmlPullParserException e8) {
            e = e8;
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
        int result = (31 * 17) + ((int) (this.native_instance ^ (this.native_instance >>> 32)));
        return (31 * result) + this.mStyle;
    }

    public synchronized boolean isSupportedAxes(int axis) {
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
