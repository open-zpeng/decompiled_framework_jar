package com.android.internal.colorextraction.types;

import android.app.WallpaperColors;
import android.app.slice.Slice;
import android.content.Context;
import android.graphics.Color;
import android.net.wifi.WifiEnterpriseConfig;
import android.util.Log;
import android.util.MathUtils;
import android.util.Range;
import com.android.internal.R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.internal.graphics.ColorUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes3.dex */
public class Tonal implements ExtractionType {
    private static final boolean DEBUG = true;
    private static final float FIT_WEIGHT_H = 1.0f;
    private static final float FIT_WEIGHT_L = 10.0f;
    private static final float FIT_WEIGHT_S = 1.0f;
    public static final int MAIN_COLOR_DARK = -16777216;
    public static final int MAIN_COLOR_LIGHT = -2039584;
    private static final String TAG = "Tonal";
    public static final int THRESHOLD_COLOR_DARK = -14606047;
    public static final int THRESHOLD_COLOR_LIGHT = -2039584;
    private final ArrayList<ColorRange> mBlacklistedColors;
    private final TonalPalette mGreyPalette;
    private float[] mTmpHSL = new float[3];
    private final ArrayList<TonalPalette> mTonalPalettes;

    public Tonal(Context context) {
        ConfigParser parser = new ConfigParser(context);
        this.mTonalPalettes = parser.getTonalPalettes();
        this.mBlacklistedColors = parser.getBlacklistedColors();
        this.mGreyPalette = this.mTonalPalettes.get(0);
        this.mTonalPalettes.remove(0);
    }

    @Override // com.android.internal.colorextraction.types.ExtractionType
    public void extractInto(WallpaperColors inWallpaperColors, ColorExtractor.GradientColors outColorsNormal, ColorExtractor.GradientColors outColorsDark, ColorExtractor.GradientColors outColorsExtraDark) {
        boolean success = runTonalExtraction(inWallpaperColors, outColorsNormal, outColorsDark, outColorsExtraDark);
        if (!success) {
            applyFallback(inWallpaperColors, outColorsNormal, outColorsDark, outColorsExtraDark);
        }
    }

    private boolean runTonalExtraction(WallpaperColors inWallpaperColors, ColorExtractor.GradientColors outColorsNormal, ColorExtractor.GradientColors outColorsDark, ColorExtractor.GradientColors outColorsExtraDark) {
        int primaryIndex;
        int primaryIndex2;
        if (inWallpaperColors == null) {
            return false;
        }
        List<Color> mainColors = inWallpaperColors.getMainColors();
        int mainColorsSize = mainColors.size();
        int hints = inWallpaperColors.getColorHints();
        boolean supportsDarkText = (hints & 1) != 0;
        boolean generatedFromBitmap = (hints & 4) != 0;
        if (mainColorsSize == 0) {
            return false;
        }
        Color bestColor = null;
        float[] hsl = new float[3];
        int i = 0;
        while (i < mainColorsSize) {
            Color color = mainColors.get(i);
            int colorValue = color.toArgb();
            List<Color> mainColors2 = mainColors;
            ColorUtils.RGBToHSL(Color.red(colorValue), Color.green(colorValue), Color.blue(colorValue), hsl);
            if (generatedFromBitmap && isBlacklisted(hsl)) {
                i++;
                mainColors = mainColors2;
            } else {
                bestColor = color;
                break;
            }
        }
        if (bestColor == null) {
            return false;
        }
        int colorValue2 = bestColor.toArgb();
        ColorUtils.RGBToHSL(Color.red(colorValue2), Color.green(colorValue2), Color.blue(colorValue2), hsl);
        hsl[0] = hsl[0] / 360.0f;
        TonalPalette palette = findTonalPalette(hsl[0], hsl[1]);
        if (palette == null) {
            Log.w(TAG, "Could not find a tonal palette!");
            return false;
        }
        int fitIndex = bestFit(palette, hsl[0], hsl[1], hsl[2]);
        if (fitIndex == -1) {
            Log.w(TAG, "Could not find best fit!");
            return false;
        }
        float[] h = fit(palette.h, hsl[0], fitIndex, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
        float[] s = fit(palette.s, hsl[1], fitIndex, 0.0f, 1.0f);
        float[] l = fit(palette.l, hsl[2], fitIndex, 0.0f, 1.0f);
        StringBuilder builder = new StringBuilder("Tonal Palette - index: " + fitIndex + ". Main color: " + Integer.toHexString(getColorInt(fitIndex, h, s, l)) + "\nColors: ");
        for (int i2 = 0; i2 < h.length; i2++) {
            builder.append(Integer.toHexString(getColorInt(i2, h, s, l)));
            if (i2 < h.length - 1) {
                builder.append(", ");
            }
        }
        Log.d(TAG, builder.toString());
        int mainColor = getColorInt(fitIndex, h, s, l);
        ColorUtils.colorToHSL(mainColor, this.mTmpHSL);
        float mainLuminosity = this.mTmpHSL[2];
        ColorUtils.colorToHSL(-2039584, this.mTmpHSL);
        float lightLuminosity = this.mTmpHSL[2];
        if (mainLuminosity > lightLuminosity) {
            return false;
        }
        ColorUtils.colorToHSL(THRESHOLD_COLOR_DARK, this.mTmpHSL);
        float darkLuminosity = this.mTmpHSL[2];
        if (mainLuminosity < darkLuminosity) {
            return false;
        }
        outColorsNormal.setMainColor(mainColor);
        outColorsNormal.setSecondaryColor(mainColor);
        if (supportsDarkText) {
            primaryIndex = h.length - 1;
        } else if (fitIndex < 2) {
            primaryIndex = 0;
        } else {
            primaryIndex = Math.min(fitIndex, 3);
        }
        int mainColor2 = getColorInt(primaryIndex, h, s, l);
        outColorsDark.setMainColor(mainColor2);
        outColorsDark.setSecondaryColor(mainColor2);
        if (supportsDarkText) {
            primaryIndex2 = h.length - 1;
        } else if (fitIndex < 2) {
            primaryIndex2 = 0;
        } else {
            primaryIndex2 = 2;
        }
        int mainColor3 = getColorInt(primaryIndex2, h, s, l);
        outColorsExtraDark.setMainColor(mainColor3);
        outColorsExtraDark.setSecondaryColor(mainColor3);
        outColorsNormal.setSupportsDarkText(supportsDarkText);
        outColorsDark.setSupportsDarkText(supportsDarkText);
        outColorsExtraDark.setSupportsDarkText(supportsDarkText);
        Log.d(TAG, "Gradients: \n\tNormal " + outColorsNormal + "\n\tDark " + outColorsDark + "\n\tExtra dark: " + outColorsExtraDark);
        return true;
    }

    private void applyFallback(WallpaperColors inWallpaperColors, ColorExtractor.GradientColors outColorsNormal, ColorExtractor.GradientColors outColorsDark, ColorExtractor.GradientColors outColorsExtraDark) {
        applyFallback(inWallpaperColors, outColorsNormal);
        applyFallback(inWallpaperColors, outColorsDark);
        applyFallback(inWallpaperColors, outColorsExtraDark);
    }

    public static void applyFallback(WallpaperColors inWallpaperColors, ColorExtractor.GradientColors outGradientColors) {
        boolean light = true;
        light = (inWallpaperColors == null || (inWallpaperColors.getColorHints() & 1) == 0) ? false : false;
        int color = light ? -2039584 : -16777216;
        outGradientColors.setMainColor(color);
        outGradientColors.setSecondaryColor(color);
        outGradientColors.setSupportsDarkText(light);
    }

    private int getColorInt(int fitIndex, float[] h, float[] s, float[] l) {
        this.mTmpHSL[0] = fract(h[fitIndex]) * 360.0f;
        this.mTmpHSL[1] = s[fitIndex];
        this.mTmpHSL[2] = l[fitIndex];
        return ColorUtils.HSLToColor(this.mTmpHSL);
    }

    private boolean isBlacklisted(float[] hsl) {
        for (int i = this.mBlacklistedColors.size() - 1; i >= 0; i--) {
            ColorRange badRange = this.mBlacklistedColors.get(i);
            if (badRange.containsColor(hsl[0], hsl[1], hsl[2])) {
                return true;
            }
        }
        return false;
    }

    private static float[] fit(float[] data, float v, int index, float min, float max) {
        float[] fitData = new float[data.length];
        float delta = v - data[index];
        for (int i = 0; i < data.length; i++) {
            fitData[i] = MathUtils.constrain(data[i] + delta, min, max);
        }
        return fitData;
    }

    private static int bestFit(TonalPalette palette, float h, float s, float l) {
        int minErrorIndex = -1;
        float minError = Float.POSITIVE_INFINITY;
        for (int i = 0; i < palette.h.length; i++) {
            float error = (Math.abs(h - palette.h[i]) * 1.0f) + (1.0f * Math.abs(s - palette.s[i])) + (10.0f * Math.abs(l - palette.l[i]));
            if (error < minError) {
                minError = error;
                minErrorIndex = i;
            }
        }
        return minErrorIndex;
    }

    @VisibleForTesting
    public List<ColorRange> getBlacklistedColors() {
        return this.mBlacklistedColors;
    }

    private TonalPalette findTonalPalette(float h, float s) {
        float fract;
        if (s < 0.05f) {
            return this.mGreyPalette;
        }
        TonalPalette best = null;
        float error = Float.POSITIVE_INFINITY;
        int tonalPalettesCount = this.mTonalPalettes.size();
        for (int i = 0; i < tonalPalettesCount; i++) {
            TonalPalette candidate = this.mTonalPalettes.get(i);
            if (h >= candidate.minHue && h <= candidate.maxHue) {
                return candidate;
            }
            if (candidate.maxHue > 1.0f && h >= 0.0f && h <= fract(candidate.maxHue)) {
                return candidate;
            }
            if (candidate.minHue < 0.0f && h >= fract(candidate.minHue) && h <= 1.0f) {
                return candidate;
            }
            if (h <= candidate.minHue && candidate.minHue - h < error) {
                best = candidate;
                fract = candidate.minHue - h;
            } else {
                if (h >= candidate.maxHue && h - candidate.maxHue < error) {
                    best = candidate;
                    error = h - candidate.maxHue;
                } else if (candidate.maxHue > 1.0f && h >= fract(candidate.maxHue) && h - fract(candidate.maxHue) < error) {
                    best = candidate;
                    error = h - fract(candidate.maxHue);
                } else if (candidate.minHue < 0.0f && h <= fract(candidate.minHue) && fract(candidate.minHue) - h < error) {
                    best = candidate;
                    fract = fract(candidate.minHue) - h;
                }
            }
            error = fract;
        }
        return best;
    }

    private static float fract(float v) {
        return v - ((float) Math.floor(v));
    }

    @VisibleForTesting
    /* loaded from: classes3.dex */
    public static class TonalPalette {
        public final float[] h;
        public final float[] l;
        public final float maxHue;
        public final float minHue;
        public final float[] s;

        TonalPalette(float[] h, float[] s, float[] l) {
            if (h.length != s.length || s.length != l.length) {
                throw new IllegalArgumentException("All arrays should have the same size. h: " + Arrays.toString(h) + " s: " + Arrays.toString(s) + " l: " + Arrays.toString(l));
            }
            this.h = h;
            this.s = s;
            this.l = l;
            float minHue = Float.POSITIVE_INFINITY;
            float maxHue = Float.NEGATIVE_INFINITY;
            for (float v : h) {
                minHue = Math.min(v, minHue);
                maxHue = Math.max(v, maxHue);
            }
            this.minHue = minHue;
            this.maxHue = maxHue;
        }
    }

    @VisibleForTesting
    /* loaded from: classes3.dex */
    public static class ColorRange {
        private Range<Float> mHue;
        private Range<Float> mLightness;
        private Range<Float> mSaturation;

        public ColorRange(Range<Float> hue, Range<Float> saturation, Range<Float> lightness) {
            this.mHue = hue;
            this.mSaturation = saturation;
            this.mLightness = lightness;
        }

        public boolean containsColor(float h, float s, float l) {
            return this.mHue.contains((Range<Float>) Float.valueOf(h)) && this.mSaturation.contains((Range<Float>) Float.valueOf(s)) && this.mLightness.contains((Range<Float>) Float.valueOf(l));
        }

        public float[] getCenter() {
            return new float[]{this.mHue.getLower().floatValue() + ((this.mHue.getUpper().floatValue() - this.mHue.getLower().floatValue()) / 2.0f), this.mSaturation.getLower().floatValue() + ((this.mSaturation.getUpper().floatValue() - this.mSaturation.getLower().floatValue()) / 2.0f), this.mLightness.getLower().floatValue() + ((this.mLightness.getUpper().floatValue() - this.mLightness.getLower().floatValue()) / 2.0f)};
        }

        public String toString() {
            return String.format("H: %s, S: %s, L %s", this.mHue, this.mSaturation, this.mLightness);
        }
    }

    @VisibleForTesting
    /* loaded from: classes3.dex */
    public static class ConfigParser {
        private final ArrayList<TonalPalette> mTonalPalettes = new ArrayList<>();
        private final ArrayList<ColorRange> mBlacklistedColors = new ArrayList<>();

        public ConfigParser(Context context) {
            try {
                XmlPullParser parser = context.getResources().getXml(R.xml.color_extraction);
                for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
                    if (eventType != 0 && eventType != 3) {
                        if (eventType == 2) {
                            String tagName = parser.getName();
                            if (tagName.equals("palettes")) {
                                parsePalettes(parser);
                            } else if (tagName.equals("blacklist")) {
                                parseBlacklist(parser);
                            }
                        } else {
                            throw new XmlPullParserException("Invalid XML event " + eventType + " - " + parser.getName(), parser, null);
                        }
                    }
                }
            } catch (IOException | XmlPullParserException e) {
                throw new RuntimeException(e);
            }
        }

        public ArrayList<TonalPalette> getTonalPalettes() {
            return this.mTonalPalettes;
        }

        public ArrayList<ColorRange> getBlacklistedColors() {
            return this.mBlacklistedColors;
        }

        private void parseBlacklist(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(2, null, "blacklist");
            while (parser.next() != 3) {
                if (parser.getEventType() == 2) {
                    String name = parser.getName();
                    if (name.equals(Slice.SUBTYPE_RANGE)) {
                        this.mBlacklistedColors.add(readRange(parser));
                        parser.next();
                    } else {
                        throw new XmlPullParserException("Invalid tag: " + name, parser, null);
                    }
                }
            }
        }

        private ColorRange readRange(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(2, null, Slice.SUBTYPE_RANGE);
            float[] h = readFloatArray(parser.getAttributeValue(null, "h"));
            float[] s = readFloatArray(parser.getAttributeValue(null, "s"));
            float[] l = readFloatArray(parser.getAttributeValue(null, "l"));
            if (h == null || s == null || l == null) {
                throw new XmlPullParserException("Incomplete range tag.", parser, null);
            }
            return new ColorRange(new Range(Float.valueOf(h[0]), Float.valueOf(h[1])), new Range(Float.valueOf(s[0]), Float.valueOf(s[1])), new Range(Float.valueOf(l[0]), Float.valueOf(l[1])));
        }

        private void parsePalettes(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(2, null, "palettes");
            while (parser.next() != 3) {
                if (parser.getEventType() == 2) {
                    String name = parser.getName();
                    if (name.equals("palette")) {
                        this.mTonalPalettes.add(readPalette(parser));
                        parser.next();
                    } else {
                        throw new XmlPullParserException("Invalid tag: " + name);
                    }
                }
            }
        }

        private TonalPalette readPalette(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(2, null, "palette");
            float[] h = readFloatArray(parser.getAttributeValue(null, "h"));
            float[] s = readFloatArray(parser.getAttributeValue(null, "s"));
            float[] l = readFloatArray(parser.getAttributeValue(null, "l"));
            if (h == null || s == null || l == null) {
                throw new XmlPullParserException("Incomplete range tag.", parser, null);
            }
            return new TonalPalette(h, s, l);
        }

        private float[] readFloatArray(String attributeValue) throws IOException, XmlPullParserException {
            String[] tokens = attributeValue.replaceAll(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER, "").replaceAll("\n", "").split(",");
            float[] numbers = new float[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                numbers[i] = Float.parseFloat(tokens[i]);
            }
            return numbers;
        }
    }
}
