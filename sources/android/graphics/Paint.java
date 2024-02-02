package android.graphics;

import android.graphics.fonts.FontVariationAxis;
import android.os.LocaleList;
import android.text.GraphicsOperations;
import android.text.SpannableString;
import android.text.SpannedString;
import android.text.TextUtils;
import com.android.internal.annotations.GuardedBy;
import dalvik.annotation.optimization.CriticalNative;
import dalvik.annotation.optimization.FastNative;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import libcore.util.NativeAllocationRegistry;
/* loaded from: classes.dex */
public class Paint {
    public static final int ANTI_ALIAS_FLAG = 1;
    public static final int AUTO_HINTING_TEXT_FLAG = 2048;
    public static final int BIDI_DEFAULT_LTR = 2;
    public static final int BIDI_DEFAULT_RTL = 3;
    private static final int BIDI_FLAG_MASK = 7;
    public static final int BIDI_FORCE_LTR = 4;
    public static final int BIDI_FORCE_RTL = 5;
    public static final int BIDI_LTR = 0;
    private static final int BIDI_MAX_FLAG_VALUE = 5;
    public static final int BIDI_RTL = 1;
    public static final int CURSOR_AFTER = 0;
    public static final int CURSOR_AT = 4;
    public static final int CURSOR_AT_OR_AFTER = 1;
    public static final int CURSOR_AT_OR_BEFORE = 3;
    public static final int CURSOR_BEFORE = 2;
    private static final int CURSOR_OPT_MAX_VALUE = 4;
    public static final int DEV_KERN_TEXT_FLAG = 256;
    public static final int DIRECTION_LTR = 0;
    public static final int DIRECTION_RTL = 1;
    public static final int DITHER_FLAG = 4;
    public static final int EMBEDDED_BITMAP_TEXT_FLAG = 1024;
    public static final int FAKE_BOLD_TEXT_FLAG = 32;
    public static final int FILTER_BITMAP_FLAG = 2;
    static final int HIDDEN_DEFAULT_PAINT_FLAGS = 1280;
    public static final int HINTING_OFF = 0;
    public static final int HINTING_ON = 1;
    public static final int HYPHENEDIT_MASK_END_OF_LINE = 7;
    public static final int HYPHENEDIT_MASK_START_OF_LINE = 24;
    public static final int LCD_RENDER_TEXT_FLAG = 512;
    public static final int LINEAR_TEXT_FLAG = 64;
    private static final long NATIVE_PAINT_SIZE = 98;
    public static final int STRIKE_THRU_TEXT_FLAG = 16;
    public static final int SUBPIXEL_TEXT_FLAG = 128;
    public static final int UNDERLINE_TEXT_FLAG = 8;
    public static final int VERTICAL_TEXT_FLAG = 4096;
    public int mBidiFlags;
    private ColorFilter mColorFilter;
    private float mCompatScaling;
    private String mFontFeatureSettings;
    private String mFontVariationSettings;
    private boolean mHasCompatScaling;
    private float mInvCompatScaling;
    private LocaleList mLocales;
    private MaskFilter mMaskFilter;
    private long mNativeColorFilter;
    public protected long mNativePaint;
    private long mNativeShader;
    private PathEffect mPathEffect;
    private Shader mShader;
    private int mShadowLayerColor;
    private float mShadowLayerDx;
    private float mShadowLayerDy;
    private float mShadowLayerRadius;
    public protected Typeface mTypeface;
    private Xfermode mXfermode;
    private static final Object sCacheLock = new Object();
    @GuardedBy("sCacheLock")
    private static final HashMap<String, Integer> sMinikinLocaleListIdCache = new HashMap<>();
    static final Style[] sStyleArray = {Style.FILL, Style.STROKE, Style.FILL_AND_STROKE};
    static final Cap[] sCapArray = {Cap.BUTT, Cap.ROUND, Cap.SQUARE};
    static final Join[] sJoinArray = {Join.MITER, Join.ROUND, Join.BEVEL};
    static final Align[] sAlignArray = {Align.LEFT, Align.CENTER, Align.RIGHT};

    /* loaded from: classes.dex */
    public static class FontMetrics {
        public float ascent;
        public float bottom;
        public float descent;
        public float leading;
        public float top;
    }

    @CriticalNative
    private static native float nAscent(long j);

    private static native int nBreakText(long j, String str, boolean z, float f, int i, float[] fArr);

    private static native int nBreakText(long j, char[] cArr, int i, int i2, float f, int i3, float[] fArr);

    @CriticalNative
    private static native float nDescent(long j);

    @CriticalNative
    private static native boolean nEqualsForTextMeasurement(long j, long j2);

    @CriticalNative
    private static native int nGetAlpha(long j);

    private static native void nGetCharArrayBounds(long j, char[] cArr, int i, int i2, int i3, Rect rect);

    @CriticalNative
    private static native int nGetColor(long j);

    @CriticalNative
    private static native boolean nGetFillPath(long j, long j2, long j3);

    @CriticalNative
    private static native int nGetFlags(long j);

    @FastNative
    private static native float nGetFontMetrics(long j, FontMetrics fontMetrics);

    @FastNative
    private static native int nGetFontMetricsInt(long j, FontMetricsInt fontMetricsInt);

    @CriticalNative
    private static native int nGetHinting(long j);

    @CriticalNative
    private static native int nGetHyphenEdit(long j);

    @CriticalNative
    private static native float nGetLetterSpacing(long j);

    private static native long nGetNativeFinalizer();

    private static native int nGetOffsetForAdvance(long j, char[] cArr, int i, int i2, int i3, int i4, boolean z, float f);

    private static native float nGetRunAdvance(long j, char[] cArr, int i, int i2, int i3, int i4, boolean z, int i5);

    @CriticalNative
    private static native float nGetStrikeThruPosition(long j);

    @CriticalNative
    private static native float nGetStrikeThruThickness(long j);

    private static native void nGetStringBounds(long j, String str, int i, int i2, int i3, Rect rect);

    @CriticalNative
    private static native int nGetStrokeCap(long j);

    @CriticalNative
    private static native int nGetStrokeJoin(long j);

    @CriticalNative
    private static native float nGetStrokeMiter(long j);

    @CriticalNative
    private static native float nGetStrokeWidth(long j);

    @CriticalNative
    private static native int nGetStyle(long j);

    private static native float nGetTextAdvances(long j, String str, int i, int i2, int i3, int i4, int i5, float[] fArr, int i6);

    private static native float nGetTextAdvances(long j, char[] cArr, int i, int i2, int i3, int i4, int i5, float[] fArr, int i6);

    @CriticalNative
    private static native int nGetTextAlign(long j);

    private static native void nGetTextPath(long j, int i, String str, int i2, int i3, float f, float f2, long j2);

    private static native void nGetTextPath(long j, int i, char[] cArr, int i2, int i3, float f, float f2, long j2);

    private native int nGetTextRunCursor(long j, String str, int i, int i2, int i3, int i4, int i5);

    private native int nGetTextRunCursor(long j, char[] cArr, int i, int i2, int i3, int i4, int i5);

    @CriticalNative
    private static native float nGetTextScaleX(long j);

    @CriticalNative
    private static native float nGetTextSize(long j);

    @CriticalNative
    private static native float nGetTextSkewX(long j);

    @CriticalNative
    private static native float nGetUnderlinePosition(long j);

    @CriticalNative
    private static native float nGetUnderlineThickness(long j);

    @CriticalNative
    private static native float nGetWordSpacing(long j);

    private static native boolean nHasGlyph(long j, int i, String str);

    @CriticalNative
    private static native boolean nHasShadowLayer(long j);

    private static native long nInit();

    private static native long nInitWithPaint(long j);

    @CriticalNative
    private static native boolean nIsElegantTextHeight(long j);

    @CriticalNative
    private static native void nReset(long j);

    @CriticalNative
    private static native void nSet(long j, long j2);

    @CriticalNative
    private static native void nSetAlpha(long j, int i);

    @CriticalNative
    private static native void nSetAntiAlias(long j, boolean z);

    @CriticalNative
    private static native void nSetColor(long j, int i);

    @CriticalNative
    private static native long nSetColorFilter(long j, long j2);

    @CriticalNative
    private static native void nSetDither(long j, boolean z);

    @CriticalNative
    private static native void nSetElegantTextHeight(long j, boolean z);

    @CriticalNative
    private static native void nSetFakeBoldText(long j, boolean z);

    @CriticalNative
    private static native void nSetFilterBitmap(long j, boolean z);

    @CriticalNative
    private static native void nSetFlags(long j, int i);

    @FastNative
    private static native void nSetFontFeatureSettings(long j, String str);

    @CriticalNative
    private static native void nSetHinting(long j, int i);

    @CriticalNative
    private static native void nSetHyphenEdit(long j, int i);

    @CriticalNative
    private static native void nSetLetterSpacing(long j, float f);

    @CriticalNative
    private static native void nSetLinearText(long j, boolean z);

    @CriticalNative
    private static native long nSetMaskFilter(long j, long j2);

    @CriticalNative
    private static native long nSetPathEffect(long j, long j2);

    @CriticalNative
    private static native long nSetShader(long j, long j2);

    @CriticalNative
    private static native void nSetShadowLayer(long j, float f, float f2, float f3, int i);

    @CriticalNative
    private static native void nSetStrikeThruText(long j, boolean z);

    @CriticalNative
    private static native void nSetStrokeCap(long j, int i);

    @CriticalNative
    private static native void nSetStrokeJoin(long j, int i);

    @CriticalNative
    private static native void nSetStrokeMiter(long j, float f);

    @CriticalNative
    private static native void nSetStrokeWidth(long j, float f);

    @CriticalNative
    private static native void nSetStyle(long j, int i);

    @CriticalNative
    private static native void nSetSubpixelText(long j, boolean z);

    @CriticalNative
    private static native void nSetTextAlign(long j, int i);

    @FastNative
    private static native int nSetTextLocales(long j, String str);

    @CriticalNative
    private static native void nSetTextLocalesByMinikinLocaleListId(long j, int i);

    @CriticalNative
    private static native void nSetTextScaleX(long j, float f);

    @CriticalNative
    private static native void nSetTextSize(long j, float f);

    @CriticalNative
    private static native void nSetTextSkewX(long j, float f);

    @CriticalNative
    private static native void nSetTypeface(long j, long j2);

    @CriticalNative
    private static native void nSetUnderlineText(long j, boolean z);

    @CriticalNative
    private static native void nSetWordSpacing(long j, float f);

    @CriticalNative
    private static native void nSetXfermode(long j, int i);

    static /* synthetic */ long access$000() {
        return nGetNativeFinalizer();
    }

    /* loaded from: classes.dex */
    private static class NoImagePreloadHolder {
        public static final NativeAllocationRegistry sRegistry = new NativeAllocationRegistry(Paint.class.getClassLoader(), Paint.access$000(), (long) Paint.NATIVE_PAINT_SIZE);

        private synchronized NoImagePreloadHolder() {
        }
    }

    /* loaded from: classes.dex */
    public enum Style {
        FILL(0),
        STROKE(1),
        FILL_AND_STROKE(2);
        
        final int nativeInt;

        Style(int nativeInt) {
            this.nativeInt = nativeInt;
        }
    }

    /* loaded from: classes.dex */
    public enum Cap {
        BUTT(0),
        ROUND(1),
        SQUARE(2);
        
        final int nativeInt;

        Cap(int nativeInt) {
            this.nativeInt = nativeInt;
        }
    }

    /* loaded from: classes.dex */
    public enum Join {
        MITER(0),
        ROUND(1),
        BEVEL(2);
        
        final int nativeInt;

        Join(int nativeInt) {
            this.nativeInt = nativeInt;
        }
    }

    /* loaded from: classes.dex */
    public enum Align {
        LEFT(0),
        CENTER(1),
        RIGHT(2);
        
        final int nativeInt;

        Align(int nativeInt) {
            this.nativeInt = nativeInt;
        }
    }

    public Paint() {
        this(0);
    }

    public Paint(int flags) {
        this.mBidiFlags = 2;
        this.mNativePaint = nInit();
        NoImagePreloadHolder.sRegistry.registerNativeAllocation(this, this.mNativePaint);
        setFlags(flags | 1280);
        this.mInvCompatScaling = 1.0f;
        this.mCompatScaling = 1.0f;
        setTextLocales(LocaleList.getAdjustedDefault());
    }

    public Paint(Paint paint) {
        this.mBidiFlags = 2;
        this.mNativePaint = nInitWithPaint(paint.getNativeInstance());
        NoImagePreloadHolder.sRegistry.registerNativeAllocation(this, this.mNativePaint);
        setClassVariablesFrom(paint);
    }

    public void reset() {
        nReset(this.mNativePaint);
        setFlags(1280);
        this.mColorFilter = null;
        this.mMaskFilter = null;
        this.mPathEffect = null;
        this.mShader = null;
        this.mNativeShader = 0L;
        this.mTypeface = null;
        this.mXfermode = null;
        this.mHasCompatScaling = false;
        this.mCompatScaling = 1.0f;
        this.mInvCompatScaling = 1.0f;
        this.mBidiFlags = 2;
        setTextLocales(LocaleList.getAdjustedDefault());
        setElegantTextHeight(false);
        this.mFontFeatureSettings = null;
        this.mFontVariationSettings = null;
        this.mShadowLayerRadius = 0.0f;
        this.mShadowLayerDx = 0.0f;
        this.mShadowLayerDy = 0.0f;
        this.mShadowLayerColor = 0;
    }

    public void set(Paint src) {
        if (this != src) {
            nSet(this.mNativePaint, src.mNativePaint);
            setClassVariablesFrom(src);
        }
    }

    private synchronized void setClassVariablesFrom(Paint paint) {
        this.mColorFilter = paint.mColorFilter;
        this.mMaskFilter = paint.mMaskFilter;
        this.mPathEffect = paint.mPathEffect;
        this.mShader = paint.mShader;
        this.mNativeShader = paint.mNativeShader;
        this.mTypeface = paint.mTypeface;
        this.mXfermode = paint.mXfermode;
        this.mHasCompatScaling = paint.mHasCompatScaling;
        this.mCompatScaling = paint.mCompatScaling;
        this.mInvCompatScaling = paint.mInvCompatScaling;
        this.mBidiFlags = paint.mBidiFlags;
        this.mLocales = paint.mLocales;
        this.mFontFeatureSettings = paint.mFontFeatureSettings;
        this.mFontVariationSettings = paint.mFontVariationSettings;
        this.mShadowLayerRadius = paint.mShadowLayerRadius;
        this.mShadowLayerDx = paint.mShadowLayerDx;
        this.mShadowLayerDy = paint.mShadowLayerDy;
        this.mShadowLayerColor = paint.mShadowLayerColor;
    }

    public synchronized boolean hasEqualAttributes(Paint other) {
        return this.mColorFilter == other.mColorFilter && this.mMaskFilter == other.mMaskFilter && this.mPathEffect == other.mPathEffect && this.mShader == other.mShader && this.mTypeface == other.mTypeface && this.mXfermode == other.mXfermode && this.mHasCompatScaling == other.mHasCompatScaling && this.mCompatScaling == other.mCompatScaling && this.mInvCompatScaling == other.mInvCompatScaling && this.mBidiFlags == other.mBidiFlags && this.mLocales.equals(other.mLocales) && TextUtils.equals(this.mFontFeatureSettings, other.mFontFeatureSettings) && TextUtils.equals(this.mFontVariationSettings, other.mFontVariationSettings) && this.mShadowLayerRadius == other.mShadowLayerRadius && this.mShadowLayerDx == other.mShadowLayerDx && this.mShadowLayerDy == other.mShadowLayerDy && this.mShadowLayerColor == other.mShadowLayerColor && getFlags() == other.getFlags() && getHinting() == other.getHinting() && getStyle() == other.getStyle() && getColor() == other.getColor() && getStrokeWidth() == other.getStrokeWidth() && getStrokeMiter() == other.getStrokeMiter() && getStrokeCap() == other.getStrokeCap() && getStrokeJoin() == other.getStrokeJoin() && getTextAlign() == other.getTextAlign() && isElegantTextHeight() == other.isElegantTextHeight() && getTextSize() == other.getTextSize() && getTextScaleX() == other.getTextScaleX() && getTextSkewX() == other.getTextSkewX() && getLetterSpacing() == other.getLetterSpacing() && getWordSpacing() == other.getWordSpacing() && getHyphenEdit() == other.getHyphenEdit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCompatibilityScaling(float factor) {
        if (factor == 1.0d) {
            this.mHasCompatScaling = false;
            this.mInvCompatScaling = 1.0f;
            this.mCompatScaling = 1.0f;
            return;
        }
        this.mHasCompatScaling = true;
        this.mCompatScaling = factor;
        this.mInvCompatScaling = 1.0f / factor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getNativeInstance() {
        long newNativeShader = this.mShader == null ? 0L : this.mShader.getNativeInstance();
        if (newNativeShader != this.mNativeShader) {
            this.mNativeShader = newNativeShader;
            nSetShader(this.mNativePaint, this.mNativeShader);
        }
        long newNativeColorFilter = this.mColorFilter != null ? this.mColorFilter.getNativeInstance() : 0L;
        if (newNativeColorFilter != this.mNativeColorFilter) {
            this.mNativeColorFilter = newNativeColorFilter;
            nSetColorFilter(this.mNativePaint, this.mNativeColorFilter);
        }
        return this.mNativePaint;
    }

    public synchronized int getBidiFlags() {
        return this.mBidiFlags;
    }

    public synchronized void setBidiFlags(int flags) {
        int flags2 = flags & 7;
        if (flags2 > 5) {
            throw new IllegalArgumentException("unknown bidi flag: " + flags2);
        }
        this.mBidiFlags = flags2;
    }

    public int getFlags() {
        return nGetFlags(this.mNativePaint);
    }

    public void setFlags(int flags) {
        nSetFlags(this.mNativePaint, flags);
    }

    public int getHinting() {
        return nGetHinting(this.mNativePaint);
    }

    public void setHinting(int mode) {
        nSetHinting(this.mNativePaint, mode);
    }

    public final boolean isAntiAlias() {
        return (getFlags() & 1) != 0;
    }

    public void setAntiAlias(boolean aa) {
        nSetAntiAlias(this.mNativePaint, aa);
    }

    public final boolean isDither() {
        return (getFlags() & 4) != 0;
    }

    public void setDither(boolean dither) {
        nSetDither(this.mNativePaint, dither);
    }

    public final boolean isLinearText() {
        return (getFlags() & 64) != 0;
    }

    public void setLinearText(boolean linearText) {
        nSetLinearText(this.mNativePaint, linearText);
    }

    public final boolean isSubpixelText() {
        return (getFlags() & 128) != 0;
    }

    public void setSubpixelText(boolean subpixelText) {
        nSetSubpixelText(this.mNativePaint, subpixelText);
    }

    public final boolean isUnderlineText() {
        return (getFlags() & 8) != 0;
    }

    public synchronized float getUnderlinePosition() {
        return nGetUnderlinePosition(this.mNativePaint);
    }

    public synchronized float getUnderlineThickness() {
        return nGetUnderlineThickness(this.mNativePaint);
    }

    public void setUnderlineText(boolean underlineText) {
        nSetUnderlineText(this.mNativePaint, underlineText);
    }

    public final boolean isStrikeThruText() {
        return (getFlags() & 16) != 0;
    }

    public synchronized float getStrikeThruPosition() {
        return nGetStrikeThruPosition(this.mNativePaint);
    }

    public synchronized float getStrikeThruThickness() {
        return nGetStrikeThruThickness(this.mNativePaint);
    }

    public void setStrikeThruText(boolean strikeThruText) {
        nSetStrikeThruText(this.mNativePaint, strikeThruText);
    }

    public final boolean isFakeBoldText() {
        return (getFlags() & 32) != 0;
    }

    public void setFakeBoldText(boolean fakeBoldText) {
        nSetFakeBoldText(this.mNativePaint, fakeBoldText);
    }

    public final boolean isFilterBitmap() {
        return (getFlags() & 2) != 0;
    }

    public void setFilterBitmap(boolean filter) {
        nSetFilterBitmap(this.mNativePaint, filter);
    }

    public Style getStyle() {
        return sStyleArray[nGetStyle(this.mNativePaint)];
    }

    public void setStyle(Style style) {
        nSetStyle(this.mNativePaint, style.nativeInt);
    }

    public int getColor() {
        return nGetColor(this.mNativePaint);
    }

    public void setColor(int color) {
        nSetColor(this.mNativePaint, color);
    }

    public int getAlpha() {
        return nGetAlpha(this.mNativePaint);
    }

    public void setAlpha(int a) {
        nSetAlpha(this.mNativePaint, a);
    }

    public void setARGB(int a, int r, int g, int b) {
        setColor((a << 24) | (r << 16) | (g << 8) | b);
    }

    public float getStrokeWidth() {
        return nGetStrokeWidth(this.mNativePaint);
    }

    public void setStrokeWidth(float width) {
        nSetStrokeWidth(this.mNativePaint, width);
    }

    public float getStrokeMiter() {
        return nGetStrokeMiter(this.mNativePaint);
    }

    public void setStrokeMiter(float miter) {
        nSetStrokeMiter(this.mNativePaint, miter);
    }

    public Cap getStrokeCap() {
        return sCapArray[nGetStrokeCap(this.mNativePaint)];
    }

    public void setStrokeCap(Cap cap) {
        nSetStrokeCap(this.mNativePaint, cap.nativeInt);
    }

    public Join getStrokeJoin() {
        return sJoinArray[nGetStrokeJoin(this.mNativePaint)];
    }

    public void setStrokeJoin(Join join) {
        nSetStrokeJoin(this.mNativePaint, join.nativeInt);
    }

    public boolean getFillPath(Path src, Path dst) {
        return nGetFillPath(this.mNativePaint, src.readOnlyNI(), dst.mutateNI());
    }

    public Shader getShader() {
        return this.mShader;
    }

    public Shader setShader(Shader shader) {
        if (this.mShader != shader) {
            this.mNativeShader = -1L;
            nSetShader(this.mNativePaint, 0L);
        }
        this.mShader = shader;
        return shader;
    }

    public ColorFilter getColorFilter() {
        return this.mColorFilter;
    }

    public ColorFilter setColorFilter(ColorFilter filter) {
        if (this.mColorFilter != filter) {
            this.mNativeColorFilter = -1L;
        }
        this.mColorFilter = filter;
        return filter;
    }

    public Xfermode getXfermode() {
        return this.mXfermode;
    }

    public Xfermode setXfermode(Xfermode xfermode) {
        int newMode = xfermode != null ? xfermode.porterDuffMode : Xfermode.DEFAULT;
        int curMode = this.mXfermode != null ? this.mXfermode.porterDuffMode : Xfermode.DEFAULT;
        if (newMode != curMode) {
            nSetXfermode(this.mNativePaint, newMode);
        }
        this.mXfermode = xfermode;
        return xfermode;
    }

    public PathEffect getPathEffect() {
        return this.mPathEffect;
    }

    public PathEffect setPathEffect(PathEffect effect) {
        long effectNative = 0;
        if (effect != null) {
            effectNative = effect.native_instance;
        }
        nSetPathEffect(this.mNativePaint, effectNative);
        this.mPathEffect = effect;
        return effect;
    }

    public MaskFilter getMaskFilter() {
        return this.mMaskFilter;
    }

    public MaskFilter setMaskFilter(MaskFilter maskfilter) {
        long maskfilterNative = 0;
        if (maskfilter != null) {
            maskfilterNative = maskfilter.native_instance;
        }
        nSetMaskFilter(this.mNativePaint, maskfilterNative);
        this.mMaskFilter = maskfilter;
        return maskfilter;
    }

    public Typeface getTypeface() {
        return this.mTypeface;
    }

    public Typeface setTypeface(Typeface typeface) {
        long typefaceNative = typeface == null ? 0L : typeface.native_instance;
        nSetTypeface(this.mNativePaint, typefaceNative);
        this.mTypeface = typeface;
        return typeface;
    }

    @Deprecated
    private protected Rasterizer getRasterizer() {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @Deprecated
    public Rasterizer setRasterizer(Rasterizer rasterizer) {
        return rasterizer;
    }

    public void setShadowLayer(float radius, float dx, float dy, int shadowColor) {
        this.mShadowLayerRadius = radius;
        this.mShadowLayerDx = dx;
        this.mShadowLayerDy = dy;
        this.mShadowLayerColor = shadowColor;
        nSetShadowLayer(this.mNativePaint, radius, dx, dy, shadowColor);
    }

    public void clearShadowLayer() {
        setShadowLayer(0.0f, 0.0f, 0.0f, 0);
    }

    public synchronized boolean hasShadowLayer() {
        return nHasShadowLayer(this.mNativePaint);
    }

    public Align getTextAlign() {
        return sAlignArray[nGetTextAlign(this.mNativePaint)];
    }

    public void setTextAlign(Align align) {
        nSetTextAlign(this.mNativePaint, align.nativeInt);
    }

    public Locale getTextLocale() {
        return this.mLocales.get(0);
    }

    public LocaleList getTextLocales() {
        return this.mLocales;
    }

    public void setTextLocale(Locale locale) {
        if (locale == null) {
            throw new IllegalArgumentException("locale cannot be null");
        }
        if (this.mLocales != null && this.mLocales.size() == 1 && locale.equals(this.mLocales.get(0))) {
            return;
        }
        this.mLocales = new LocaleList(locale);
        syncTextLocalesWithMinikin();
    }

    public void setTextLocales(LocaleList locales) {
        if (locales == null || locales.isEmpty()) {
            throw new IllegalArgumentException("locales cannot be null or empty");
        }
        if (locales.equals(this.mLocales)) {
            return;
        }
        this.mLocales = locales;
        syncTextLocalesWithMinikin();
    }

    private synchronized void syncTextLocalesWithMinikin() {
        String languageTags = this.mLocales.toLanguageTags();
        synchronized (sCacheLock) {
            Integer minikinLocaleListId = sMinikinLocaleListIdCache.get(languageTags);
            if (minikinLocaleListId == null) {
                int newID = nSetTextLocales(this.mNativePaint, languageTags);
                sMinikinLocaleListIdCache.put(languageTags, Integer.valueOf(newID));
                return;
            }
            nSetTextLocalesByMinikinLocaleListId(this.mNativePaint, minikinLocaleListId.intValue());
        }
    }

    public boolean isElegantTextHeight() {
        return nIsElegantTextHeight(this.mNativePaint);
    }

    public void setElegantTextHeight(boolean elegant) {
        nSetElegantTextHeight(this.mNativePaint, elegant);
    }

    public float getTextSize() {
        return nGetTextSize(this.mNativePaint);
    }

    public void setTextSize(float textSize) {
        nSetTextSize(this.mNativePaint, textSize);
    }

    public float getTextScaleX() {
        return nGetTextScaleX(this.mNativePaint);
    }

    public void setTextScaleX(float scaleX) {
        nSetTextScaleX(this.mNativePaint, scaleX);
    }

    public float getTextSkewX() {
        return nGetTextSkewX(this.mNativePaint);
    }

    public void setTextSkewX(float skewX) {
        nSetTextSkewX(this.mNativePaint, skewX);
    }

    public float getLetterSpacing() {
        return nGetLetterSpacing(this.mNativePaint);
    }

    public void setLetterSpacing(float letterSpacing) {
        nSetLetterSpacing(this.mNativePaint, letterSpacing);
    }

    public synchronized float getWordSpacing() {
        return nGetWordSpacing(this.mNativePaint);
    }

    public synchronized void setWordSpacing(float wordSpacing) {
        nSetWordSpacing(this.mNativePaint, wordSpacing);
    }

    public String getFontFeatureSettings() {
        return this.mFontFeatureSettings;
    }

    public void setFontFeatureSettings(String settings) {
        if (settings != null && settings.equals("")) {
            settings = null;
        }
        if (settings != null || this.mFontFeatureSettings != null) {
            if (settings != null && settings.equals(this.mFontFeatureSettings)) {
                return;
            }
            this.mFontFeatureSettings = settings;
            nSetFontFeatureSettings(this.mNativePaint, settings);
        }
    }

    public String getFontVariationSettings() {
        return this.mFontVariationSettings;
    }

    public boolean setFontVariationSettings(String fontVariationSettings) {
        String settings = TextUtils.nullIfEmpty(fontVariationSettings);
        if (settings == this.mFontVariationSettings || (settings != null && settings.equals(this.mFontVariationSettings))) {
            return true;
        }
        if (settings == null || settings.length() == 0) {
            this.mFontVariationSettings = null;
            setTypeface(Typeface.createFromTypefaceWithVariation(this.mTypeface, Collections.emptyList()));
            return true;
        }
        Typeface targetTypeface = this.mTypeface == null ? Typeface.DEFAULT : this.mTypeface;
        FontVariationAxis[] axes = FontVariationAxis.fromFontVariationSettings(settings);
        ArrayList<FontVariationAxis> filteredAxes = new ArrayList<>();
        for (FontVariationAxis axis : axes) {
            if (targetTypeface.isSupportedAxes(axis.getOpenTypeTagValue())) {
                filteredAxes.add(axis);
            }
        }
        if (filteredAxes.isEmpty()) {
            return false;
        }
        this.mFontVariationSettings = settings;
        setTypeface(Typeface.createFromTypefaceWithVariation(targetTypeface, filteredAxes));
        return true;
    }

    public synchronized int getHyphenEdit() {
        return nGetHyphenEdit(this.mNativePaint);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setHyphenEdit(int hyphen) {
        nSetHyphenEdit(this.mNativePaint, hyphen);
    }

    public float ascent() {
        return nAscent(this.mNativePaint);
    }

    public float descent() {
        return nDescent(this.mNativePaint);
    }

    public float getFontMetrics(FontMetrics metrics) {
        return nGetFontMetrics(this.mNativePaint, metrics);
    }

    public FontMetrics getFontMetrics() {
        FontMetrics fm = new FontMetrics();
        getFontMetrics(fm);
        return fm;
    }

    /* loaded from: classes.dex */
    public static class FontMetricsInt {
        public int ascent;
        public int bottom;
        public int descent;
        public int leading;
        public int top;

        public String toString() {
            return "FontMetricsInt: top=" + this.top + " ascent=" + this.ascent + " descent=" + this.descent + " bottom=" + this.bottom + " leading=" + this.leading;
        }
    }

    public int getFontMetricsInt(FontMetricsInt fmi) {
        return nGetFontMetricsInt(this.mNativePaint, fmi);
    }

    public FontMetricsInt getFontMetricsInt() {
        FontMetricsInt fm = new FontMetricsInt();
        getFontMetricsInt(fm);
        return fm;
    }

    public float getFontSpacing() {
        return getFontMetrics(null);
    }

    public float measureText(char[] text, int index, int count) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null");
        }
        if ((index | count) < 0 || index + count > text.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (text.length == 0 || count == 0) {
            return 0.0f;
        }
        if (!this.mHasCompatScaling) {
            return (float) Math.ceil(nGetTextAdvances(this.mNativePaint, text, index, count, index, count, this.mBidiFlags, (float[]) null, 0));
        }
        float oldSize = getTextSize();
        setTextSize(this.mCompatScaling * oldSize);
        float w = nGetTextAdvances(this.mNativePaint, text, index, count, index, count, this.mBidiFlags, (float[]) null, 0);
        setTextSize(oldSize);
        return (float) Math.ceil(this.mInvCompatScaling * w);
    }

    public float measureText(String text, int start, int end) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null");
        }
        if ((start | end | (end - start) | (text.length() - end)) < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (text.length() == 0 || start == end) {
            return 0.0f;
        }
        if (!this.mHasCompatScaling) {
            return (float) Math.ceil(nGetTextAdvances(this.mNativePaint, text, start, end, start, end, this.mBidiFlags, (float[]) null, 0));
        }
        float oldSize = getTextSize();
        setTextSize(this.mCompatScaling * oldSize);
        float w = nGetTextAdvances(this.mNativePaint, text, start, end, start, end, this.mBidiFlags, (float[]) null, 0);
        setTextSize(oldSize);
        return (float) Math.ceil(this.mInvCompatScaling * w);
    }

    public float measureText(String text) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null");
        }
        return measureText(text, 0, text.length());
    }

    public float measureText(CharSequence text, int start, int end) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null");
        }
        if ((start | end | (end - start) | (text.length() - end)) < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (text.length() == 0 || start == end) {
            return 0.0f;
        }
        if (text instanceof String) {
            return measureText((String) text, start, end);
        }
        if ((text instanceof SpannedString) || (text instanceof SpannableString)) {
            return measureText(text.toString(), start, end);
        }
        if (text instanceof GraphicsOperations) {
            return ((GraphicsOperations) text).measureText(start, end, this);
        }
        char[] buf = TemporaryBuffer.obtain(end - start);
        TextUtils.getChars(text, start, end, buf, 0);
        float result = measureText(buf, 0, end - start);
        TemporaryBuffer.recycle(buf);
        return result;
    }

    public int breakText(char[] text, int index, int count, float maxWidth, float[] measuredWidth) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null");
        }
        if (index < 0 || text.length - index < Math.abs(count)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (text.length == 0 || count == 0) {
            return 0;
        }
        if (!this.mHasCompatScaling) {
            return nBreakText(this.mNativePaint, text, index, count, maxWidth, this.mBidiFlags, measuredWidth);
        }
        float oldSize = getTextSize();
        setTextSize(this.mCompatScaling * oldSize);
        int res = nBreakText(this.mNativePaint, text, index, count, maxWidth * this.mCompatScaling, this.mBidiFlags, measuredWidth);
        setTextSize(oldSize);
        if (measuredWidth != null) {
            measuredWidth[0] = measuredWidth[0] * this.mInvCompatScaling;
        }
        return res;
    }

    public int breakText(CharSequence text, int start, int end, boolean measureForwards, float maxWidth, float[] measuredWidth) {
        int result;
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null");
        }
        if ((start | end | (end - start) | (text.length() - end)) < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (text.length() == 0 || start == end) {
            return 0;
        }
        if (start == 0 && (text instanceof String) && end == text.length()) {
            return breakText((String) text, measureForwards, maxWidth, measuredWidth);
        }
        char[] buf = TemporaryBuffer.obtain(end - start);
        TextUtils.getChars(text, start, end, buf, 0);
        if (measureForwards) {
            result = breakText(buf, 0, end - start, maxWidth, measuredWidth);
        } else {
            result = breakText(buf, 0, -(end - start), maxWidth, measuredWidth);
        }
        TemporaryBuffer.recycle(buf);
        return result;
    }

    public int breakText(String text, boolean measureForwards, float maxWidth, float[] measuredWidth) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null");
        }
        if (text.length() == 0) {
            return 0;
        }
        if (!this.mHasCompatScaling) {
            return nBreakText(this.mNativePaint, text, measureForwards, maxWidth, this.mBidiFlags, measuredWidth);
        }
        float oldSize = getTextSize();
        setTextSize(this.mCompatScaling * oldSize);
        int res = nBreakText(this.mNativePaint, text, measureForwards, maxWidth * this.mCompatScaling, this.mBidiFlags, measuredWidth);
        setTextSize(oldSize);
        if (measuredWidth != null) {
            measuredWidth[0] = measuredWidth[0] * this.mInvCompatScaling;
        }
        return res;
    }

    public int getTextWidths(char[] text, int index, int count, float[] widths) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null");
        }
        if ((index | count) < 0 || index + count > text.length || count > widths.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (text.length == 0 || count == 0) {
            return 0;
        }
        if (!this.mHasCompatScaling) {
            nGetTextAdvances(this.mNativePaint, text, index, count, index, count, this.mBidiFlags, widths, 0);
            return count;
        }
        float oldSize = getTextSize();
        setTextSize(this.mCompatScaling * oldSize);
        nGetTextAdvances(this.mNativePaint, text, index, count, index, count, this.mBidiFlags, widths, 0);
        setTextSize(oldSize);
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= count) {
                return count;
            }
            widths[i2] = widths[i2] * this.mInvCompatScaling;
            i = i2 + 1;
        }
    }

    public int getTextWidths(CharSequence text, int start, int end, float[] widths) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null");
        }
        if ((start | end | (end - start) | (text.length() - end)) < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (end - start > widths.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (text.length() == 0 || start == end) {
            return 0;
        }
        if (text instanceof String) {
            return getTextWidths((String) text, start, end, widths);
        }
        if ((text instanceof SpannedString) || (text instanceof SpannableString)) {
            return getTextWidths(text.toString(), start, end, widths);
        }
        if (text instanceof GraphicsOperations) {
            return ((GraphicsOperations) text).getTextWidths(start, end, widths, this);
        }
        char[] buf = TemporaryBuffer.obtain(end - start);
        TextUtils.getChars(text, start, end, buf, 0);
        int result = getTextWidths(buf, 0, end - start, widths);
        TemporaryBuffer.recycle(buf);
        return result;
    }

    public int getTextWidths(String text, int start, int end, float[] widths) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null");
        }
        if ((start | end | (end - start) | (text.length() - end)) < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (end - start > widths.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (text.length() == 0 || start == end) {
            return 0;
        }
        if (!this.mHasCompatScaling) {
            nGetTextAdvances(this.mNativePaint, text, start, end, start, end, this.mBidiFlags, widths, 0);
            return end - start;
        }
        float oldSize = getTextSize();
        setTextSize(this.mCompatScaling * oldSize);
        nGetTextAdvances(this.mNativePaint, text, start, end, start, end, this.mBidiFlags, widths, 0);
        setTextSize(oldSize);
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= end - start) {
                return end - start;
            }
            widths[i2] = widths[i2] * this.mInvCompatScaling;
            i = i2 + 1;
        }
    }

    public int getTextWidths(String text, float[] widths) {
        return getTextWidths(text, 0, text.length(), widths);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getTextRunAdvances(char[] chars, int index, int count, int contextIndex, int contextCount, boolean isRtl, float[] advances, int advancesIndex) {
        if (chars != null) {
            if ((index | count | contextIndex | contextCount | advancesIndex | (index - contextIndex) | (contextCount - count) | ((contextIndex + contextCount) - (index + count)) | (chars.length - (contextIndex + contextCount)) | (advances == null ? 0 : advances.length - (advancesIndex + count))) >= 0) {
                if (chars.length == 0 || count == 0) {
                    return 0.0f;
                }
                if (!this.mHasCompatScaling) {
                    return nGetTextAdvances(this.mNativePaint, chars, index, count, contextIndex, contextCount, isRtl ? 5 : 4, advances, advancesIndex);
                }
                float oldSize = getTextSize();
                setTextSize(this.mCompatScaling * oldSize);
                float res = nGetTextAdvances(this.mNativePaint, chars, index, count, contextIndex, contextCount, isRtl ? 5 : 4, advances, advancesIndex);
                setTextSize(oldSize);
                if (advances != null) {
                    int i = advancesIndex;
                    int e = i + count;
                    while (i < e) {
                        advances[i] = advances[i] * this.mInvCompatScaling;
                        i++;
                    }
                }
                return this.mInvCompatScaling * res;
            }
            throw new IndexOutOfBoundsException();
        }
        throw new IllegalArgumentException("text cannot be null");
    }

    public synchronized float getTextRunAdvances(CharSequence text, int start, int end, int contextStart, int contextEnd, boolean isRtl, float[] advances, int advancesIndex) {
        if (text != null) {
            if ((start | end | contextStart | contextEnd | advancesIndex | (end - start) | (start - contextStart) | (contextEnd - end) | (text.length() - contextEnd) | (advances == null ? 0 : (advances.length - advancesIndex) - (end - start))) < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (text instanceof String) {
                return getTextRunAdvances((String) text, start, end, contextStart, contextEnd, isRtl, advances, advancesIndex);
            }
            if ((text instanceof SpannedString) || (text instanceof SpannableString)) {
                return getTextRunAdvances(text.toString(), start, end, contextStart, contextEnd, isRtl, advances, advancesIndex);
            }
            if (text instanceof GraphicsOperations) {
                return ((GraphicsOperations) text).getTextRunAdvances(start, end, contextStart, contextEnd, isRtl, advances, advancesIndex, this);
            }
            if (text.length() == 0 || end == start) {
                return 0.0f;
            }
            int contextLen = contextEnd - contextStart;
            int len = end - start;
            char[] buf = TemporaryBuffer.obtain(contextLen);
            TextUtils.getChars(text, contextStart, contextEnd, buf, 0);
            float result = getTextRunAdvances(buf, start - contextStart, len, 0, contextLen, isRtl, advances, advancesIndex);
            TemporaryBuffer.recycle(buf);
            return result;
        }
        throw new IllegalArgumentException("text cannot be null");
    }

    public synchronized float getTextRunAdvances(String text, int start, int end, int contextStart, int contextEnd, boolean isRtl, float[] advances, int advancesIndex) {
        if (text != null) {
            if ((start | end | contextStart | contextEnd | advancesIndex | (end - start) | (start - contextStart) | (contextEnd - end) | (text.length() - contextEnd) | (advances == null ? 0 : (advances.length - advancesIndex) - (end - start))) >= 0) {
                if (text.length() == 0 || start == end) {
                    return 0.0f;
                }
                if (!this.mHasCompatScaling) {
                    return nGetTextAdvances(this.mNativePaint, text, start, end, contextStart, contextEnd, isRtl ? 5 : 4, advances, advancesIndex);
                }
                float oldSize = getTextSize();
                setTextSize(this.mCompatScaling * oldSize);
                float totalAdvance = nGetTextAdvances(this.mNativePaint, text, start, end, contextStart, contextEnd, isRtl ? 5 : 4, advances, advancesIndex);
                setTextSize(oldSize);
                if (advances != null) {
                    int i = advancesIndex;
                    int e = (end - start) + i;
                    while (i < e) {
                        advances[i] = advances[i] * this.mInvCompatScaling;
                        i++;
                    }
                }
                return this.mInvCompatScaling * totalAdvance;
            }
            throw new IndexOutOfBoundsException();
        }
        throw new IllegalArgumentException("text cannot be null");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getTextRunCursor(char[] text, int contextStart, int contextLength, int dir, int offset, int cursorOpt) {
        int contextEnd = contextStart + contextLength;
        if ((contextStart | contextEnd | offset | (contextEnd - contextStart) | (offset - contextStart) | (contextEnd - offset) | (text.length - contextEnd) | cursorOpt) < 0 || cursorOpt > 4) {
            throw new IndexOutOfBoundsException();
        }
        return nGetTextRunCursor(this.mNativePaint, text, contextStart, contextLength, dir, offset, cursorOpt);
    }

    public synchronized int getTextRunCursor(CharSequence text, int contextStart, int contextEnd, int dir, int offset, int cursorOpt) {
        if ((text instanceof String) || (text instanceof SpannedString) || (text instanceof SpannableString)) {
            return getTextRunCursor(text.toString(), contextStart, contextEnd, dir, offset, cursorOpt);
        }
        if (text instanceof GraphicsOperations) {
            return ((GraphicsOperations) text).getTextRunCursor(contextStart, contextEnd, dir, offset, cursorOpt, this);
        }
        int contextLen = contextEnd - contextStart;
        char[] buf = TemporaryBuffer.obtain(contextLen);
        TextUtils.getChars(text, contextStart, contextEnd, buf, 0);
        int relPos = getTextRunCursor(buf, 0, contextLen, dir, offset - contextStart, cursorOpt);
        TemporaryBuffer.recycle(buf);
        if (relPos == -1) {
            return -1;
        }
        return relPos + contextStart;
    }

    public synchronized int getTextRunCursor(String text, int contextStart, int contextEnd, int dir, int offset, int cursorOpt) {
        if ((contextStart | contextEnd | offset | (contextEnd - contextStart) | (offset - contextStart) | (contextEnd - offset) | (text.length() - contextEnd) | cursorOpt) < 0 || cursorOpt > 4) {
            throw new IndexOutOfBoundsException();
        }
        return nGetTextRunCursor(this.mNativePaint, text, contextStart, contextEnd, dir, offset, cursorOpt);
    }

    public void getTextPath(char[] text, int index, int count, float x, float y, Path path) {
        if ((index | count) >= 0 && index + count <= text.length) {
            nGetTextPath(this.mNativePaint, this.mBidiFlags, text, index, count, x, y, path.mutateNI());
            return;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public void getTextPath(String text, int start, int end, float x, float y, Path path) {
        if ((start | end | (end - start) | (text.length() - end)) < 0) {
            throw new IndexOutOfBoundsException();
        }
        nGetTextPath(this.mNativePaint, this.mBidiFlags, text, start, end, x, y, path.mutateNI());
    }

    public void getTextBounds(String text, int start, int end, Rect bounds) {
        if ((start | end | (end - start) | (text.length() - end)) < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (bounds == null) {
            throw new NullPointerException("need bounds Rect");
        }
        nGetStringBounds(this.mNativePaint, text, start, end, this.mBidiFlags, bounds);
    }

    public synchronized void getTextBounds(CharSequence text, int start, int end, Rect bounds) {
        if ((start | end | (end - start) | (text.length() - end)) < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (bounds == null) {
            throw new NullPointerException("need bounds Rect");
        }
        char[] buf = TemporaryBuffer.obtain(end - start);
        TextUtils.getChars(text, start, end, buf, 0);
        getTextBounds(buf, 0, end - start, bounds);
        TemporaryBuffer.recycle(buf);
    }

    public void getTextBounds(char[] text, int index, int count, Rect bounds) {
        if ((index | count) < 0 || index + count > text.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (bounds == null) {
            throw new NullPointerException("need bounds Rect");
        }
        nGetCharArrayBounds(this.mNativePaint, text, index, count, this.mBidiFlags, bounds);
    }

    public boolean hasGlyph(String string) {
        return nHasGlyph(this.mNativePaint, this.mBidiFlags, string);
    }

    public float getRunAdvance(char[] text, int start, int end, int contextStart, int contextEnd, boolean isRtl, int offset) {
        if (text != null) {
            if ((contextStart | start | offset | end | contextEnd | (start - contextStart) | (offset - start) | (end - offset) | (contextEnd - end) | (text.length - contextEnd)) >= 0) {
                if (end == start) {
                    return 0.0f;
                }
                return nGetRunAdvance(this.mNativePaint, text, start, end, contextStart, contextEnd, isRtl, offset);
            }
            throw new IndexOutOfBoundsException();
        }
        throw new IllegalArgumentException("text cannot be null");
    }

    public float getRunAdvance(CharSequence text, int start, int end, int contextStart, int contextEnd, boolean isRtl, int offset) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null");
        }
        if ((contextStart | start | offset | end | contextEnd | (start - contextStart) | (offset - start) | (end - offset) | (contextEnd - end) | (text.length() - contextEnd)) < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (end == start) {
            return 0.0f;
        }
        char[] buf = TemporaryBuffer.obtain(contextEnd - contextStart);
        TextUtils.getChars(text, contextStart, contextEnd, buf, 0);
        float result = getRunAdvance(buf, start - contextStart, end - contextStart, 0, contextEnd - contextStart, isRtl, offset - contextStart);
        TemporaryBuffer.recycle(buf);
        return result;
    }

    public int getOffsetForAdvance(char[] text, int start, int end, int contextStart, int contextEnd, boolean isRtl, float advance) {
        if (text != null) {
            if ((contextStart | start | end | contextEnd | (start - contextStart) | (end - start) | (contextEnd - end) | (text.length - contextEnd)) >= 0) {
                return nGetOffsetForAdvance(this.mNativePaint, text, start, end, contextStart, contextEnd, isRtl, advance);
            }
            throw new IndexOutOfBoundsException();
        }
        throw new IllegalArgumentException("text cannot be null");
    }

    public int getOffsetForAdvance(CharSequence text, int start, int end, int contextStart, int contextEnd, boolean isRtl, float advance) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null");
        }
        if ((contextStart | start | end | contextEnd | (start - contextStart) | (end - start) | (contextEnd - end) | (text.length() - contextEnd)) < 0) {
            throw new IndexOutOfBoundsException();
        }
        char[] buf = TemporaryBuffer.obtain(contextEnd - contextStart);
        TextUtils.getChars(text, contextStart, contextEnd, buf, 0);
        int result = getOffsetForAdvance(buf, start - contextStart, end - contextStart, 0, contextEnd - contextStart, isRtl, advance) + contextStart;
        TemporaryBuffer.recycle(buf);
        return result;
    }

    public boolean equalsForTextMeasurement(Paint other) {
        return nEqualsForTextMeasurement(this.mNativePaint, other.mNativePaint);
    }
}
