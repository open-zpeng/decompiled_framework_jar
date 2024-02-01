package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ImageDecoder;
import android.graphics.Insets;
import android.graphics.NinePatch;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import com.android.internal.R;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class NinePatchDrawable extends Drawable {
    private static final boolean DEFAULT_DITHER = false;
    private int mBitmapHeight;
    private int mBitmapWidth;
    private boolean mMutated;
    public protected NinePatchState mNinePatchState;
    private Insets mOpticalInsets;
    private Rect mOutlineInsets;
    private float mOutlineRadius;
    private Rect mPadding;
    private Paint mPaint;
    private int mTargetDensity;
    private Rect mTempRect;
    private PorterDuffColorFilter mTintFilter;

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized NinePatchDrawable() {
        this.mOpticalInsets = Insets.NONE;
        this.mTargetDensity = 160;
        this.mBitmapWidth = -1;
        this.mBitmapHeight = -1;
        this.mNinePatchState = new NinePatchState();
    }

    @Deprecated
    public NinePatchDrawable(Bitmap bitmap, byte[] chunk, Rect padding, String srcName) {
        this(new NinePatchState(new NinePatch(bitmap, chunk, srcName), padding), (Resources) null);
    }

    public NinePatchDrawable(Resources res, Bitmap bitmap, byte[] chunk, Rect padding, String srcName) {
        this(new NinePatchState(new NinePatch(bitmap, chunk, srcName), padding), res);
    }

    public synchronized NinePatchDrawable(Resources res, Bitmap bitmap, byte[] chunk, Rect padding, Rect opticalInsets, String srcName) {
        this(new NinePatchState(new NinePatch(bitmap, chunk, srcName), padding, opticalInsets), res);
    }

    @Deprecated
    public NinePatchDrawable(NinePatch patch) {
        this(new NinePatchState(patch, new Rect()), (Resources) null);
    }

    public NinePatchDrawable(Resources res, NinePatch patch) {
        this(new NinePatchState(patch, new Rect()), res);
    }

    public void setTargetDensity(Canvas canvas) {
        setTargetDensity(canvas.getDensity());
    }

    public void setTargetDensity(DisplayMetrics metrics) {
        setTargetDensity(metrics.densityDpi);
    }

    public void setTargetDensity(int density) {
        if (density == 0) {
            density = 160;
        }
        if (this.mTargetDensity != density) {
            this.mTargetDensity = density;
            computeBitmapSize();
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        boolean clearColorFilter;
        int restoreAlpha;
        NinePatchState state = this.mNinePatchState;
        Rect bounds = getBounds();
        int restoreToCount = -1;
        boolean needsDensityScaling = false;
        if (this.mTintFilter != null && getPaint().getColorFilter() == null) {
            this.mPaint.setColorFilter(this.mTintFilter);
            clearColorFilter = true;
        } else {
            clearColorFilter = false;
        }
        if (state.mBaseAlpha != 1.0f) {
            restoreAlpha = getPaint().getAlpha();
            this.mPaint.setAlpha((int) ((restoreAlpha * state.mBaseAlpha) + 0.5f));
        } else {
            restoreAlpha = -1;
        }
        if (canvas.getDensity() == 0 && state.mNinePatch.getDensity() != 0) {
            needsDensityScaling = true;
        }
        if (needsDensityScaling) {
            restoreToCount = -1 >= 0 ? -1 : canvas.save();
            float scale = this.mTargetDensity / state.mNinePatch.getDensity();
            float px = bounds.left;
            float py = bounds.top;
            canvas.scale(scale, scale, px, py);
            if (this.mTempRect == null) {
                this.mTempRect = new Rect();
            }
            Rect scaledBounds = this.mTempRect;
            scaledBounds.left = bounds.left;
            scaledBounds.top = bounds.top;
            scaledBounds.right = bounds.left + Math.round(bounds.width() / scale);
            scaledBounds.bottom = bounds.top + Math.round(bounds.height() / scale);
            bounds = scaledBounds;
        }
        boolean needsMirroring = needsMirroring();
        if (needsMirroring) {
            restoreToCount = restoreToCount >= 0 ? restoreToCount : canvas.save();
            float cx = (bounds.left + bounds.right) / 2.0f;
            float cy = (bounds.top + bounds.bottom) / 2.0f;
            canvas.scale(-1.0f, 1.0f, cx, cy);
        }
        state.mNinePatch.draw(canvas, bounds, this.mPaint);
        if (restoreToCount >= 0) {
            canvas.restoreToCount(restoreToCount);
        }
        if (clearColorFilter) {
            this.mPaint.setColorFilter(null);
        }
        if (restoreAlpha >= 0) {
            this.mPaint.setAlpha(restoreAlpha);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mNinePatchState.getChangingConfigurations();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean getPadding(Rect padding) {
        if (this.mPadding != null) {
            padding.set(this.mPadding);
            return (((padding.left | padding.top) | padding.right) | padding.bottom) != 0;
        }
        return super.getPadding(padding);
    }

    @Override // android.graphics.drawable.Drawable
    public void getOutline(Outline outline) {
        NinePatch.InsetStruct insets;
        Rect bounds = getBounds();
        if (bounds.isEmpty()) {
            return;
        }
        if (this.mNinePatchState != null && this.mOutlineInsets != null && (insets = this.mNinePatchState.mNinePatch.getBitmap().getNinePatchInsets()) != null) {
            outline.setRoundRect(bounds.left + this.mOutlineInsets.left, bounds.top + this.mOutlineInsets.top, bounds.right - this.mOutlineInsets.right, bounds.bottom - this.mOutlineInsets.bottom, this.mOutlineRadius);
            outline.setAlpha(insets.outlineAlpha * (getAlpha() / 255.0f));
            return;
        }
        super.getOutline(outline);
    }

    private protected Insets getOpticalInsets() {
        Insets opticalInsets = this.mOpticalInsets;
        if (needsMirroring()) {
            return Insets.of(opticalInsets.right, opticalInsets.top, opticalInsets.left, opticalInsets.bottom);
        }
        return opticalInsets;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        if (this.mPaint == null && alpha == 255) {
            return;
        }
        getPaint().setAlpha(alpha);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        if (this.mPaint == null) {
            return 255;
        }
        return getPaint().getAlpha();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        if (this.mPaint == null && colorFilter == null) {
            return;
        }
        getPaint().setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintList(ColorStateList tint) {
        this.mNinePatchState.mTint = tint;
        this.mTintFilter = updateTintFilter(this.mTintFilter, tint, this.mNinePatchState.mTintMode);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintMode(PorterDuff.Mode tintMode) {
        this.mNinePatchState.mTintMode = tintMode;
        this.mTintFilter = updateTintFilter(this.mTintFilter, this.mNinePatchState.mTint, tintMode);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setDither(boolean dither) {
        if (this.mPaint == null && !dither) {
            return;
        }
        getPaint().setDither(dither);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAutoMirrored(boolean mirrored) {
        this.mNinePatchState.mAutoMirrored = mirrored;
    }

    private synchronized boolean needsMirroring() {
        return isAutoMirrored() && getLayoutDirection() == 1;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isAutoMirrored() {
        return this.mNinePatchState.mAutoMirrored;
    }

    @Override // android.graphics.drawable.Drawable
    public void setFilterBitmap(boolean filter) {
        getPaint().setFilterBitmap(filter);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isFilterBitmap() {
        return this.mPaint != null && getPaint().isFilterBitmap();
    }

    @Override // android.graphics.drawable.Drawable
    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        super.inflate(r, parser, attrs, theme);
        TypedArray a = obtainAttributes(r, theme, attrs, R.styleable.NinePatchDrawable);
        updateStateFromTypedArray(a);
        a.recycle();
        updateLocalState(r);
    }

    private synchronized void updateStateFromTypedArray(TypedArray a) throws XmlPullParserException {
        Resources r = a.getResources();
        NinePatchState state = this.mNinePatchState;
        state.mChangingConfigurations |= a.getChangingConfigurations();
        state.mThemeAttrs = a.extractThemeAttrs();
        state.mDither = a.getBoolean(1, state.mDither);
        int srcResId = a.getResourceId(0, 0);
        if (srcResId != 0) {
            final Rect padding = new Rect();
            Rect opticalInsets = new Rect();
            Bitmap bitmap = null;
            try {
                TypedValue value = new TypedValue();
                InputStream is = r.openRawResource(srcResId, value);
                int density = 0;
                if (value.density == 0) {
                    density = 160;
                } else if (value.density != 65535) {
                    density = value.density;
                }
                ImageDecoder.Source source = ImageDecoder.createSource(r, is, density);
                bitmap = ImageDecoder.decodeBitmap(source, new ImageDecoder.OnHeaderDecodedListener() { // from class: android.graphics.drawable.-$$Lambda$NinePatchDrawable$yQvfm7FAkslD5wdGFysjgwt8cLE
                    @Override // android.graphics.ImageDecoder.OnHeaderDecodedListener
                    public final void onHeaderDecoded(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source2) {
                        NinePatchDrawable.lambda$updateStateFromTypedArray$0(Rect.this, imageDecoder, imageInfo, source2);
                    }
                });
                is.close();
            } catch (IOException e) {
            }
            if (bitmap == null) {
                throw new XmlPullParserException(a.getPositionDescription() + ": <nine-patch> requires a valid src attribute");
            } else if (bitmap.getNinePatchChunk() == null) {
                throw new XmlPullParserException(a.getPositionDescription() + ": <nine-patch> requires a valid 9-patch source image");
            } else {
                bitmap.getOpticalInsets(opticalInsets);
                state.mNinePatch = new NinePatch(bitmap, bitmap.getNinePatchChunk());
                state.mPadding = padding;
                state.mOpticalInsets = Insets.of(opticalInsets);
            }
        }
        state.mAutoMirrored = a.getBoolean(4, state.mAutoMirrored);
        state.mBaseAlpha = a.getFloat(3, state.mBaseAlpha);
        int tintMode = a.getInt(5, -1);
        if (tintMode != -1) {
            state.mTintMode = Drawable.parseTintMode(tintMode, PorterDuff.Mode.SRC_IN);
        }
        ColorStateList tint = a.getColorStateList(2);
        if (tint != null) {
            state.mTint = tint;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$updateStateFromTypedArray$0(Rect padding, ImageDecoder decoder, ImageDecoder.ImageInfo info, ImageDecoder.Source src) {
        decoder.setOutPaddingRect(padding);
        decoder.setAllocator(1);
    }

    @Override // android.graphics.drawable.Drawable
    public void applyTheme(Resources.Theme t) {
        super.applyTheme(t);
        NinePatchState state = this.mNinePatchState;
        if (state == null) {
            return;
        }
        if (state.mThemeAttrs != null) {
            TypedArray a = t.resolveAttributes(state.mThemeAttrs, R.styleable.NinePatchDrawable);
            try {
                try {
                    updateStateFromTypedArray(a);
                } catch (XmlPullParserException e) {
                    rethrowAsRuntimeException(e);
                }
            } finally {
                a.recycle();
            }
        }
        if (state.mTint != null && state.mTint.canApplyTheme()) {
            state.mTint = state.mTint.mo19obtainForTheme(t);
        }
        updateLocalState(t.getResources());
    }

    @Override // android.graphics.drawable.Drawable
    public boolean canApplyTheme() {
        return this.mNinePatchState != null && this.mNinePatchState.canApplyTheme();
    }

    public Paint getPaint() {
        if (this.mPaint == null) {
            this.mPaint = new Paint();
            this.mPaint.setDither(false);
        }
        return this.mPaint;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.mBitmapWidth;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.mBitmapHeight;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return (this.mNinePatchState.mNinePatch.hasAlpha() || (this.mPaint != null && this.mPaint.getAlpha() < 255)) ? -3 : -1;
    }

    @Override // android.graphics.drawable.Drawable
    public Region getTransparentRegion() {
        return this.mNinePatchState.mNinePatch.getTransparentRegion(getBounds());
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        this.mNinePatchState.mChangingConfigurations = getChangingConfigurations();
        return this.mNinePatchState;
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mNinePatchState = new NinePatchState(this.mNinePatchState);
            this.mMutated = true;
        }
        return this;
    }

    @Override // android.graphics.drawable.Drawable
    public synchronized void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.drawable.Drawable
    public boolean onStateChange(int[] stateSet) {
        NinePatchState state = this.mNinePatchState;
        if (state.mTint != null && state.mTintMode != null) {
            this.mTintFilter = updateTintFilter(this.mTintFilter, state.mTint, state.mTintMode);
            return true;
        }
        return false;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        NinePatchState s = this.mNinePatchState;
        return super.isStateful() || (s.mTint != null && s.mTint.isStateful());
    }

    @Override // android.graphics.drawable.Drawable
    public boolean hasFocusStateSpecified() {
        return this.mNinePatchState.mTint != null && this.mNinePatchState.mTint.hasFocusStateSpecified();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class NinePatchState extends Drawable.ConstantState {
        boolean mAutoMirrored;
        float mBaseAlpha;
        int mChangingConfigurations;
        boolean mDither;
        public private protected NinePatch mNinePatch;
        Insets mOpticalInsets;
        Rect mPadding;
        int[] mThemeAttrs;
        ColorStateList mTint;
        PorterDuff.Mode mTintMode;

        synchronized NinePatchState() {
            this.mNinePatch = null;
            this.mTint = null;
            this.mTintMode = Drawable.DEFAULT_TINT_MODE;
            this.mPadding = null;
            this.mOpticalInsets = Insets.NONE;
            this.mBaseAlpha = 1.0f;
            this.mDither = false;
            this.mAutoMirrored = false;
        }

        synchronized NinePatchState(NinePatch ninePatch, Rect padding) {
            this(ninePatch, padding, null, false, false);
        }

        synchronized NinePatchState(NinePatch ninePatch, Rect padding, Rect opticalInsets) {
            this(ninePatch, padding, opticalInsets, false, false);
        }

        synchronized NinePatchState(NinePatch ninePatch, Rect padding, Rect opticalInsets, boolean dither, boolean autoMirror) {
            this.mNinePatch = null;
            this.mTint = null;
            this.mTintMode = Drawable.DEFAULT_TINT_MODE;
            this.mPadding = null;
            this.mOpticalInsets = Insets.NONE;
            this.mBaseAlpha = 1.0f;
            this.mDither = false;
            this.mAutoMirrored = false;
            this.mNinePatch = ninePatch;
            this.mPadding = padding;
            this.mOpticalInsets = Insets.of(opticalInsets);
            this.mDither = dither;
            this.mAutoMirrored = autoMirror;
        }

        synchronized NinePatchState(NinePatchState orig) {
            this.mNinePatch = null;
            this.mTint = null;
            this.mTintMode = Drawable.DEFAULT_TINT_MODE;
            this.mPadding = null;
            this.mOpticalInsets = Insets.NONE;
            this.mBaseAlpha = 1.0f;
            this.mDither = false;
            this.mAutoMirrored = false;
            this.mChangingConfigurations = orig.mChangingConfigurations;
            this.mNinePatch = orig.mNinePatch;
            this.mTint = orig.mTint;
            this.mTintMode = orig.mTintMode;
            this.mPadding = orig.mPadding;
            this.mOpticalInsets = orig.mOpticalInsets;
            this.mBaseAlpha = orig.mBaseAlpha;
            this.mDither = orig.mDither;
            this.mAutoMirrored = orig.mAutoMirrored;
            this.mThemeAttrs = orig.mThemeAttrs;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public boolean canApplyTheme() {
            return this.mThemeAttrs != null || (this.mTint != null && this.mTint.canApplyTheme()) || super.canApplyTheme();
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            return new NinePatchDrawable(this, null);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources res) {
            return new NinePatchDrawable(this, res);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return this.mChangingConfigurations | (this.mTint != null ? this.mTint.getChangingConfigurations() : 0);
        }
    }

    private synchronized void computeBitmapSize() {
        NinePatch ninePatch = this.mNinePatchState.mNinePatch;
        if (ninePatch == null) {
            return;
        }
        int targetDensity = this.mTargetDensity;
        int sourceDensity = ninePatch.getDensity() == 0 ? targetDensity : ninePatch.getDensity();
        Insets sourceOpticalInsets = this.mNinePatchState.mOpticalInsets;
        if (sourceOpticalInsets != Insets.NONE) {
            int left = Drawable.scaleFromDensity(sourceOpticalInsets.left, sourceDensity, targetDensity, true);
            int top = Drawable.scaleFromDensity(sourceOpticalInsets.top, sourceDensity, targetDensity, true);
            int right = Drawable.scaleFromDensity(sourceOpticalInsets.right, sourceDensity, targetDensity, true);
            int bottom = Drawable.scaleFromDensity(sourceOpticalInsets.bottom, sourceDensity, targetDensity, true);
            this.mOpticalInsets = Insets.of(left, top, right, bottom);
        } else {
            this.mOpticalInsets = Insets.NONE;
        }
        Rect sourcePadding = this.mNinePatchState.mPadding;
        if (sourcePadding != null) {
            if (this.mPadding == null) {
                this.mPadding = new Rect();
            }
            this.mPadding.left = Drawable.scaleFromDensity(sourcePadding.left, sourceDensity, targetDensity, true);
            this.mPadding.top = Drawable.scaleFromDensity(sourcePadding.top, sourceDensity, targetDensity, true);
            this.mPadding.right = Drawable.scaleFromDensity(sourcePadding.right, sourceDensity, targetDensity, true);
            this.mPadding.bottom = Drawable.scaleFromDensity(sourcePadding.bottom, sourceDensity, targetDensity, true);
        } else {
            this.mPadding = null;
        }
        this.mBitmapHeight = Drawable.scaleFromDensity(ninePatch.getHeight(), sourceDensity, targetDensity, true);
        this.mBitmapWidth = Drawable.scaleFromDensity(ninePatch.getWidth(), sourceDensity, targetDensity, true);
        NinePatch.InsetStruct insets = ninePatch.getBitmap().getNinePatchInsets();
        if (insets != null) {
            Rect outlineRect = insets.outlineRect;
            this.mOutlineInsets = NinePatch.InsetStruct.scaleInsets(outlineRect.left, outlineRect.top, outlineRect.right, outlineRect.bottom, targetDensity / sourceDensity);
            this.mOutlineRadius = Drawable.scaleFromDensity(insets.outlineRadius, sourceDensity, targetDensity);
            return;
        }
        this.mOutlineInsets = null;
    }

    private synchronized NinePatchDrawable(NinePatchState state, Resources res) {
        this.mOpticalInsets = Insets.NONE;
        this.mTargetDensity = 160;
        this.mBitmapWidth = -1;
        this.mBitmapHeight = -1;
        this.mNinePatchState = state;
        updateLocalState(res);
    }

    private synchronized void updateLocalState(Resources res) {
        NinePatchState state = this.mNinePatchState;
        if (state.mDither) {
            setDither(state.mDither);
        }
        if (res == null && state.mNinePatch != null) {
            this.mTargetDensity = state.mNinePatch.getDensity();
        } else {
            this.mTargetDensity = Drawable.resolveDensity(res, this.mTargetDensity);
        }
        this.mTintFilter = updateTintFilter(this.mTintFilter, state.mTint, state.mTintMode);
        computeBitmapSize();
    }
}
