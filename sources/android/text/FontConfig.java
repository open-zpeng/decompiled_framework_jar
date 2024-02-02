package android.text;

import android.graphics.fonts.FontVariationAxis;
import android.net.Uri;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes2.dex */
public final class FontConfig {
    private final Alias[] mAliases;
    private final Family[] mFamilies;

    public synchronized FontConfig(Family[] families, Alias[] aliases) {
        this.mFamilies = families;
        this.mAliases = aliases;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Family[] getFamilies() {
        return this.mFamilies;
    }

    public synchronized Alias[] getAliases() {
        return this.mAliases;
    }

    /* loaded from: classes2.dex */
    public static final class Font {
        private final FontVariationAxis[] mAxes;
        private final String mFallbackFor;
        private final String mFontName;
        private final boolean mIsItalic;
        private final int mTtcIndex;
        private Uri mUri;
        private final int mWeight;

        public synchronized Font(String fontName, int ttcIndex, FontVariationAxis[] axes, int weight, boolean isItalic, String fallbackFor) {
            this.mFontName = fontName;
            this.mTtcIndex = ttcIndex;
            this.mAxes = axes;
            this.mWeight = weight;
            this.mIsItalic = isItalic;
            this.mFallbackFor = fallbackFor;
        }

        public synchronized String getFontName() {
            return this.mFontName;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getTtcIndex() {
            return this.mTtcIndex;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public FontVariationAxis[] getAxes() {
            return this.mAxes;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getWeight() {
            return this.mWeight;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isItalic() {
            return this.mIsItalic;
        }

        public synchronized Uri getUri() {
            return this.mUri;
        }

        public synchronized void setUri(Uri uri) {
            this.mUri = uri;
        }

        public synchronized String getFallbackFor() {
            return this.mFallbackFor;
        }
    }

    /* loaded from: classes2.dex */
    public static final class Alias {
        private final String mName;
        private final String mToName;
        private final int mWeight;

        public synchronized Alias(String name, String toName, int weight) {
            this.mName = name;
            this.mToName = toName;
            this.mWeight = weight;
        }

        public synchronized String getName() {
            return this.mName;
        }

        public synchronized String getToName() {
            return this.mToName;
        }

        public synchronized int getWeight() {
            return this.mWeight;
        }
    }

    /* loaded from: classes2.dex */
    public static final class Family {
        public static final int VARIANT_COMPACT = 1;
        public static final int VARIANT_DEFAULT = 0;
        public static final int VARIANT_ELEGANT = 2;
        private final Font[] mFonts;
        private final String[] mLanguages;
        private final String mName;
        private final int mVariant;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes2.dex */
        public @interface Variant {
        }

        public synchronized Family(String name, Font[] fonts, String[] languages, int variant) {
            this.mName = name;
            this.mFonts = fonts;
            this.mLanguages = languages;
            this.mVariant = variant;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getName() {
            return this.mName;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Font[] getFonts() {
            return this.mFonts;
        }

        public synchronized String[] getLanguages() {
            return this.mLanguages;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getVariant() {
            return this.mVariant;
        }
    }
}
