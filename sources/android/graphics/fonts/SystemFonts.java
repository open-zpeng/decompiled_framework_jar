package android.graphics.fonts;

import android.graphics.FontListParser;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontCustomizationParser;
import android.graphics.fonts.FontFamily;
import android.text.FontConfig;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes.dex */
public final class SystemFonts {
    private static final String DEFAULT_FAMILY = "sans-serif";
    private static final String TAG = "SystemFonts";
    private static final FontConfig.Alias[] sAliases;
    private static final List<Font> sAvailableFonts;
    private static final Map<String, FontFamily[]> sSystemFallbackMap;

    private SystemFonts() {
    }

    public static Set<Font> getAvailableFonts() {
        HashSet<Font> set = new HashSet<>();
        set.addAll(sAvailableFonts);
        return set;
    }

    public static FontFamily[] getSystemFallback(String familyName) {
        FontFamily[] families = sSystemFallbackMap.get(familyName);
        return families == null ? sSystemFallbackMap.get(DEFAULT_FAMILY) : families;
    }

    public static Map<String, FontFamily[]> getRawSystemFallbackMap() {
        return sSystemFallbackMap;
    }

    public static FontConfig.Alias[] getAliases() {
        return sAliases;
    }

    private static ByteBuffer mmap(String fullPath) {
        try {
            FileInputStream file = new FileInputStream(fullPath);
            FileChannel fileChannel = file.getChannel();
            long fontSize = fileChannel.size();
            MappedByteBuffer map = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0L, fontSize);
            $closeResource(null, file);
            return map;
        } catch (IOException e) {
            Log.e(TAG, "Error mapping font file " + fullPath);
            return null;
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

    private static void pushFamilyToFallback(FontConfig.Family xmlFamily, ArrayMap<String, ArrayList<FontFamily>> fallbackMap, Map<String, ByteBuffer> cache, ArrayList<Font> availableFonts) {
        FontConfig.Font[] fonts;
        String languageTags = xmlFamily.getLanguages();
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
        FontFamily defaultFamily = defaultFonts.isEmpty() ? null : createFontFamily(xmlFamily.getName(), defaultFonts, languageTags, variant, cache, availableFonts);
        for (int i = 0; i < fallbackMap.size(); i++) {
            ArrayList<FontConfig.Font> fallback2 = specificFallbackFonts.get(fallbackMap.keyAt(i));
            if (fallback2 == null) {
                if (defaultFamily != null) {
                    fallbackMap.valueAt(i).add(defaultFamily);
                }
            } else {
                FontFamily family = createFontFamily(xmlFamily.getName(), fallback2, languageTags, variant, cache, availableFonts);
                if (family != null) {
                    fallbackMap.valueAt(i).add(family);
                } else if (defaultFamily != null) {
                    fallbackMap.valueAt(i).add(defaultFamily);
                }
            }
        }
    }

    private static FontFamily createFontFamily(String familyName, List<FontConfig.Font> fonts, String languageTags, int variant, Map<String, ByteBuffer> cache, ArrayList<Font> availableFonts) {
        if (fonts.size() == 0) {
            return null;
        }
        FontFamily.Builder b = null;
        int i = 0;
        while (true) {
            if (i >= fonts.size()) {
                break;
            }
            FontConfig.Font fontConfig = fonts.get(i);
            String fullPath = fontConfig.getFontName();
            ByteBuffer buffer = cache.get(fullPath);
            try {
                if (buffer == null) {
                    if (cache.containsKey(fullPath)) {
                        continue;
                    } else {
                        buffer = mmap(fullPath);
                        cache.put(fullPath, buffer);
                        if (buffer == null) {
                            continue;
                        }
                    }
                    i++;
                }
                Font font = new Font.Builder(buffer, new File(fullPath), languageTags).setWeight(fontConfig.getWeight()).setSlant(fontConfig.isItalic() ? 1 : 0).setTtcIndex(fontConfig.getTtcIndex()).setFontVariationSettings(fontConfig.getAxes()).build();
                availableFonts.add(font);
                if (b == null) {
                    b = new FontFamily.Builder(font);
                } else {
                    b.addFont(font);
                }
                i++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (b == null) {
            return null;
        }
        return b.build(languageTags, variant, false);
    }

    private static void appendNamedFamily(FontConfig.Family xmlFamily, HashMap<String, ByteBuffer> bufferCache, ArrayMap<String, ArrayList<FontFamily>> fallbackListMap, ArrayList<Font> availableFonts) {
        String familyName = xmlFamily.getName();
        FontFamily family = createFontFamily(familyName, Arrays.asList(xmlFamily.getFonts()), xmlFamily.getLanguages(), xmlFamily.getVariant(), bufferCache, availableFonts);
        if (family == null) {
            return;
        }
        ArrayList<FontFamily> fallback = new ArrayList<>();
        fallback.add(family);
        fallbackListMap.put(familyName, fallback);
    }

    @VisibleForTesting
    public static FontConfig.Alias[] buildSystemFallback(String xmlPath, String fontDir, FontCustomizationParser.Result oemCustomization, ArrayMap<String, FontFamily[]> fallbackMap, ArrayList<Font> availableFonts) {
        try {
            FileInputStream fontsIn = new FileInputStream(xmlPath);
            FontConfig fontConfig = FontListParser.parse(fontsIn, fontDir);
            HashMap<String, ByteBuffer> bufferCache = new HashMap<>();
            FontConfig.Family[] xmlFamilies = fontConfig.getFamilies();
            ArrayMap<String, ArrayList<FontFamily>> fallbackListMap = new ArrayMap<>();
            for (FontConfig.Family xmlFamily : xmlFamilies) {
                String familyName = xmlFamily.getName();
                if (familyName != null) {
                    appendNamedFamily(xmlFamily, bufferCache, fallbackListMap, availableFonts);
                }
            }
            for (int i = 0; i < oemCustomization.mAdditionalNamedFamilies.size(); i++) {
                appendNamedFamily(oemCustomization.mAdditionalNamedFamilies.get(i), bufferCache, fallbackListMap, availableFonts);
            }
            for (int i2 = 0; i2 < xmlFamilies.length; i2++) {
                FontConfig.Family xmlFamily2 = xmlFamilies[i2];
                if (i2 == 0 || xmlFamily2.getName() == null) {
                    pushFamilyToFallback(xmlFamily2, fallbackListMap, bufferCache, availableFonts);
                }
            }
            for (int i3 = 0; i3 < fallbackListMap.size(); i3++) {
                String fallbackName = fallbackListMap.keyAt(i3);
                List<FontFamily> familyList = fallbackListMap.valueAt(i3);
                FontFamily[] families = (FontFamily[]) familyList.toArray(new FontFamily[familyList.size()]);
                fallbackMap.put(fallbackName, families);
            }
            ArrayList<FontConfig.Alias> list = new ArrayList<>();
            list.addAll(Arrays.asList(fontConfig.getAliases()));
            list.addAll(oemCustomization.mAdditionalAliases);
            return (FontConfig.Alias[]) list.toArray(new FontConfig.Alias[list.size()]);
        } catch (IOException | XmlPullParserException e) {
            Log.e(TAG, "Failed initialize system fallbacks.", e);
            return (FontConfig.Alias[]) ArrayUtils.emptyArray(FontConfig.Alias.class);
        }
    }

    private static FontCustomizationParser.Result readFontCustomization(String customizeXml, String customFontsDir) {
        try {
            FileInputStream f = new FileInputStream(customizeXml);
            try {
                FontCustomizationParser.Result parse = FontCustomizationParser.parse(f, customFontsDir);
                $closeResource(null, f);
                return parse;
            } finally {
            }
        } catch (IOException e) {
            return new FontCustomizationParser.Result();
        } catch (XmlPullParserException e2) {
            Log.e(TAG, "Failed to parse font customization XML", e2);
            return new FontCustomizationParser.Result();
        }
    }

    static {
        ArrayMap<String, FontFamily[]> systemFallbackMap = new ArrayMap<>();
        ArrayList<Font> availableFonts = new ArrayList<>();
        FontCustomizationParser.Result oemCustomization = readFontCustomization("/product/etc/fonts_customization.xml", "/product/fonts/");
        sAliases = buildSystemFallback("/system/etc/fonts.xml", "/system/fonts/", oemCustomization, systemFallbackMap, availableFonts);
        sSystemFallbackMap = Collections.unmodifiableMap(systemFallbackMap);
        sAvailableFonts = Collections.unmodifiableList(availableFonts);
    }
}
