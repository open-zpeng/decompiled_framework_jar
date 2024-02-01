package android.app;

import android.annotation.SystemApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Size;
import com.android.internal.graphics.ColorUtils;
import com.android.internal.graphics.palette.Palette;
import com.android.internal.graphics.palette.VariationalKMeansQuantizer;
import com.android.internal.util.ContrastColorUtil;
import com.xiaopeng.util.FeatureOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/* loaded from: classes.dex */
public final class WallpaperColors implements Parcelable {
    private static final float BRIGHT_IMAGE_MEAN_LUMINANCE = 0.75f;
    public static final Parcelable.Creator<WallpaperColors> CREATOR = new Parcelable.Creator<WallpaperColors>() { // from class: android.app.WallpaperColors.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WallpaperColors createFromParcel(Parcel in) {
            return new WallpaperColors(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WallpaperColors[] newArray(int size) {
            return new WallpaperColors[size];
        }
    };
    private static final float DARK_PIXEL_CONTRAST = 6.0f;
    private static final float DARK_THEME_MEAN_LUMINANCE = 0.25f;
    private static final boolean DEBUG_DARK_PIXELS = false;
    public static final int HINT_FROM_BITMAP = 4;
    @SystemApi
    public static final int HINT_SUPPORTS_DARK_TEXT = 1;
    @SystemApi
    public static final int HINT_SUPPORTS_DARK_THEME = 2;
    private static final int MAX_BITMAP_SIZE = 112;
    private static final float MAX_DARK_AREA = 0.025f;
    private static final int MAX_WALLPAPER_EXTRACTION_AREA = 12544;
    private static final float MIN_COLOR_OCCURRENCE = 0.05f;
    private int mColorHints;
    private final ArrayList<Color> mMainColors;

    public WallpaperColors(Parcel parcel) {
        this.mMainColors = new ArrayList<>();
        int count = parcel.readInt();
        for (int i = 0; i < count; i++) {
            int colorInt = parcel.readInt();
            Color color = Color.valueOf(colorInt);
            this.mMainColors.add(color);
        }
        int i2 = parcel.readInt();
        this.mColorHints = i2;
    }

    public static WallpaperColors fromDrawable(Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Drawable cannot be null");
        }
        Rect initialBounds = drawable.copyBounds();
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        if (width <= 0 || height <= 0) {
            width = 112;
            height = 112;
        }
        Size optimalSize = calculateOptimalSize(width, height);
        Bitmap bitmap = Bitmap.createBitmap(optimalSize.getWidth(), optimalSize.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas bmpCanvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        drawable.draw(bmpCanvas);
        WallpaperColors colors = fromBitmap(bitmap);
        bitmap.recycle();
        drawable.setBounds(initialBounds);
        return colors;
    }

    public static WallpaperColors fromBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            throw new IllegalArgumentException("Bitmap can't be null");
        }
        int bitmapArea = bitmap.getWidth() * bitmap.getHeight();
        boolean shouldRecycle = false;
        if (bitmapArea > MAX_WALLPAPER_EXTRACTION_AREA) {
            shouldRecycle = true;
            Size optimalSize = calculateOptimalSize(bitmap.getWidth(), bitmap.getHeight());
            bitmap = Bitmap.createScaledBitmap(bitmap, optimalSize.getWidth(), optimalSize.getHeight(), true);
        }
        Palette palette = Palette.from(bitmap).setQuantizer(new VariationalKMeansQuantizer()).maximumColorCount(5).clearFilters().resizeBitmapArea(MAX_WALLPAPER_EXTRACTION_AREA).generate();
        ArrayList<Palette.Swatch> swatches = new ArrayList<>(palette.getSwatches());
        final float minColorArea = bitmap.getWidth() * bitmap.getHeight() * MIN_COLOR_OCCURRENCE;
        swatches.removeIf(new Predicate() { // from class: android.app.-$$Lambda$WallpaperColors$8R5kfKKLfHjpw_QXmD1mWOKwJxc
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return WallpaperColors.lambda$fromBitmap$0(minColorArea, (Palette.Swatch) obj);
            }
        });
        swatches.sort(new Comparator() { // from class: android.app.-$$Lambda$WallpaperColors$MQFGJ9EZ9CDeGbIhMufJKqru3IE
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return WallpaperColors.lambda$fromBitmap$1((Palette.Swatch) obj, (Palette.Swatch) obj2);
            }
        });
        int swatchesSize = swatches.size();
        Color primary = null;
        Color secondary = null;
        Color tertiary = null;
        for (int i = 0; i < swatchesSize; i++) {
            Color color = Color.valueOf(swatches.get(i).getRgb());
            if (i == 0) {
                primary = color;
            } else if (i == 1) {
                secondary = color;
            } else if (i != 2) {
                break;
            } else {
                tertiary = color;
            }
        }
        int hints = calculateDarkHints(bitmap);
        if (shouldRecycle) {
            bitmap.recycle();
        }
        return new WallpaperColors(primary, secondary, tertiary, hints | 4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$fromBitmap$0(float minColorArea, Palette.Swatch s) {
        return ((float) s.getPopulation()) < minColorArea;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$fromBitmap$1(Palette.Swatch a, Palette.Swatch b) {
        return b.getPopulation() - a.getPopulation();
    }

    public WallpaperColors(Color primaryColor, Color secondaryColor, Color tertiaryColor) {
        this(primaryColor, secondaryColor, tertiaryColor, 0);
    }

    @SystemApi
    public WallpaperColors(Color primaryColor, Color secondaryColor, Color tertiaryColor, int colorHints) {
        if (primaryColor == null) {
            throw new IllegalArgumentException("Primary color should never be null.");
        }
        this.mMainColors = new ArrayList<>(3);
        this.mMainColors.add(primaryColor);
        if (secondaryColor != null) {
            this.mMainColors.add(secondaryColor);
        }
        if (tertiaryColor != null) {
            if (secondaryColor == null) {
                throw new IllegalArgumentException("tertiaryColor can't be specified when secondaryColor is null");
            }
            this.mMainColors.add(tertiaryColor);
        }
        this.mColorHints = colorHints;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        List<Color> mainColors = getMainColors();
        int count = mainColors.size();
        dest.writeInt(count);
        for (int i = 0; i < count; i++) {
            Color color = mainColors.get(i);
            dest.writeInt(color.toArgb());
        }
        int i2 = this.mColorHints;
        dest.writeInt(i2);
    }

    public Color getPrimaryColor() {
        return this.mMainColors.get(0);
    }

    public Color getSecondaryColor() {
        if (this.mMainColors.size() < 2) {
            return null;
        }
        return this.mMainColors.get(1);
    }

    public Color getTertiaryColor() {
        if (this.mMainColors.size() < 3) {
            return null;
        }
        return this.mMainColors.get(2);
    }

    public List<Color> getMainColors() {
        return Collections.unmodifiableList(this.mMainColors);
    }

    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WallpaperColors other = (WallpaperColors) o;
        return this.mMainColors.equals(other.mMainColors) && this.mColorHints == other.mColorHints;
    }

    public int hashCode() {
        return (this.mMainColors.hashCode() * 31) + this.mColorHints;
    }

    @SystemApi
    public int getColorHints() {
        return this.mColorHints;
    }

    public void setColorHints(int colorHints) {
        this.mColorHints = colorHints;
    }

    private static int calculateDarkHints(Bitmap source) {
        if (source == null) {
            return 0;
        }
        int[] pixels = new int[source.getWidth() * source.getHeight()];
        double totalLuminance = FeatureOption.FO_BOOT_POLICY_CPU;
        int maxDarkPixels = (int) (pixels.length * MAX_DARK_AREA);
        int darkPixels = 0;
        source.getPixels(pixels, 0, source.getWidth(), 0, 0, source.getWidth(), source.getHeight());
        float[] tmpHsl = new float[3];
        for (int i = 0; i < pixels.length; i++) {
            ColorUtils.colorToHSL(pixels[i], tmpHsl);
            float luminance = tmpHsl[2];
            int alpha = Color.alpha(pixels[i]);
            boolean satisfiesTextContrast = ContrastColorUtil.calculateContrast(pixels[i], -16777216) > 6.0d;
            if (!satisfiesTextContrast && alpha != 0) {
                darkPixels++;
            }
            totalLuminance += luminance;
        }
        int hints = 0;
        double meanLuminance = totalLuminance / pixels.length;
        if (meanLuminance > 0.75d && darkPixels < maxDarkPixels) {
            hints = 0 | 1;
        }
        if (meanLuminance < 0.25d) {
            return hints | 2;
        }
        return hints;
    }

    private static Size calculateOptimalSize(int width, int height) {
        int requestedArea = width * height;
        double scale = 1.0d;
        if (requestedArea > MAX_WALLPAPER_EXTRACTION_AREA) {
            scale = Math.sqrt(12544.0d / requestedArea);
        }
        int newWidth = (int) (width * scale);
        int newHeight = (int) (height * scale);
        if (newWidth == 0) {
            newWidth = 1;
        }
        if (newHeight == 0) {
            newHeight = 1;
        }
        return new Size(newWidth, newHeight);
    }

    public String toString() {
        StringBuilder colors = new StringBuilder();
        for (int i = 0; i < this.mMainColors.size(); i++) {
            colors.append(Integer.toHexString(this.mMainColors.get(i).toArgb()));
            colors.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        }
        return "[WallpaperColors: " + colors.toString() + "h: " + this.mColorHints + "]";
    }
}
