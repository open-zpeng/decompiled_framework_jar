package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ImageDecoder;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import com.android.internal.R;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class BitmapDrawable extends Drawable {
    private static final int DEFAULT_PAINT_FLAGS = 6;
    private static final int TILE_MODE_CLAMP = 0;
    private static final int TILE_MODE_DISABLED = -1;
    private static final int TILE_MODE_MIRROR = 2;
    private static final int TILE_MODE_REPEAT = 1;
    private static final int TILE_MODE_UNDEFINED = -2;
    private int mBitmapHeight;
    public protected BitmapState mBitmapState;
    private int mBitmapWidth;
    private final Rect mDstRect;
    private boolean mDstRectAndInsetsDirty;
    private Matrix mMirrorMatrix;
    private boolean mMutated;
    private Insets mOpticalInsets;
    public protected int mTargetDensity;
    private PorterDuffColorFilter mTintFilter;

    @Deprecated
    public BitmapDrawable() {
        this.mDstRect = new Rect();
        this.mTargetDensity = 160;
        this.mDstRectAndInsetsDirty = true;
        this.mOpticalInsets = Insets.NONE;
        init(new BitmapState((Bitmap) null), null);
    }

    @Deprecated
    public BitmapDrawable(Resources res) {
        this.mDstRect = new Rect();
        this.mTargetDensity = 160;
        this.mDstRectAndInsetsDirty = true;
        this.mOpticalInsets = Insets.NONE;
        init(new BitmapState((Bitmap) null), res);
    }

    @Deprecated
    public BitmapDrawable(Bitmap bitmap) {
        this.mDstRect = new Rect();
        this.mTargetDensity = 160;
        this.mDstRectAndInsetsDirty = true;
        this.mOpticalInsets = Insets.NONE;
        init(new BitmapState(bitmap), null);
    }

    public BitmapDrawable(Resources res, Bitmap bitmap) {
        this.mDstRect = new Rect();
        this.mTargetDensity = 160;
        this.mDstRectAndInsetsDirty = true;
        this.mOpticalInsets = Insets.NONE;
        init(new BitmapState(bitmap), res);
    }

    @Deprecated
    public BitmapDrawable(String filepath) {
        this((Resources) null, filepath);
    }

    public BitmapDrawable(Resources res, String filepath) {
        String str;
        StringBuilder sb;
        this.mDstRect = new Rect();
        this.mTargetDensity = 160;
        this.mDstRectAndInsetsDirty = true;
        this.mOpticalInsets = Insets.NONE;
        try {
            FileInputStream stream = new FileInputStream(filepath);
            try {
                Bitmap bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(res, stream), new ImageDecoder.OnHeaderDecodedListener() { // from class: android.graphics.drawable.-$$Lambda$BitmapDrawable$23eAuhdkgEf5MIRJC-rMNbn4Pyg
                    @Override // android.graphics.ImageDecoder.OnHeaderDecodedListener
                    public final void onHeaderDecoded(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
                        imageDecoder.setAllocator(1);
                    }
                });
                $closeResource(null, stream);
                init(new BitmapState(bitmap), res);
            } catch (Throwable th) {
                try {
                    throw th;
                } catch (Throwable th2) {
                    $closeResource(th, stream);
                    throw th2;
                }
            }
        } catch (Exception e) {
            init(new BitmapState((Bitmap) null), res);
            if (this.mBitmapState.mBitmap != null) {
                return;
            }
            str = "BitmapDrawable";
            sb = new StringBuilder();
        } catch (Throwable th3) {
            init(new BitmapState((Bitmap) null), res);
            if (this.mBitmapState.mBitmap == null) {
                Log.w("BitmapDrawable", "BitmapDrawable cannot decode " + filepath);
            }
            throw th3;
        }
        if (this.mBitmapState.mBitmap == null) {
            str = "BitmapDrawable";
            sb = new StringBuilder();
            sb.append("BitmapDrawable cannot decode ");
            sb.append(filepath);
            Log.w(str, sb.toString());
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

    @Deprecated
    public BitmapDrawable(InputStream is) {
        this((Resources) null, is);
    }

    public BitmapDrawable(Resources res, InputStream is) {
        String str;
        StringBuilder sb;
        this.mDstRect = new Rect();
        this.mTargetDensity = 160;
        this.mDstRectAndInsetsDirty = true;
        this.mOpticalInsets = Insets.NONE;
        try {
            Bitmap bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(res, is), new ImageDecoder.OnHeaderDecodedListener() { // from class: android.graphics.drawable.-$$Lambda$BitmapDrawable$T1BUUqQwU4Z6Ve8DJHFuQvYohkY
                @Override // android.graphics.ImageDecoder.OnHeaderDecodedListener
                public final void onHeaderDecoded(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
                    imageDecoder.setAllocator(1);
                }
            });
            init(new BitmapState(bitmap), res);
        } catch (Exception e) {
            init(new BitmapState((Bitmap) null), res);
            if (this.mBitmapState.mBitmap == null) {
                str = "BitmapDrawable";
                sb = new StringBuilder();
            } else {
                return;
            }
        } catch (Throwable th) {
            init(new BitmapState((Bitmap) null), res);
            if (this.mBitmapState.mBitmap == null) {
                Log.w("BitmapDrawable", "BitmapDrawable cannot decode " + is);
            }
            throw th;
        }
        if (this.mBitmapState.mBitmap == null) {
            str = "BitmapDrawable";
            sb = new StringBuilder();
            sb.append("BitmapDrawable cannot decode ");
            sb.append(is);
            Log.w(str, sb.toString());
        }
    }

    public final Paint getPaint() {
        return this.mBitmapState.mPaint;
    }

    public final Bitmap getBitmap() {
        return this.mBitmapState.mBitmap;
    }

    private synchronized void computeBitmapSize() {
        Bitmap bitmap = this.mBitmapState.mBitmap;
        if (bitmap != null) {
            this.mBitmapWidth = bitmap.getScaledWidth(this.mTargetDensity);
            this.mBitmapHeight = bitmap.getScaledHeight(this.mTargetDensity);
            return;
        }
        this.mBitmapHeight = -1;
        this.mBitmapWidth = -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setBitmap(Bitmap bitmap) {
        if (this.mBitmapState.mBitmap != bitmap) {
            this.mBitmapState.mBitmap = bitmap;
            computeBitmapSize();
            invalidateSelf();
        }
    }

    public void setTargetDensity(Canvas canvas) {
        setTargetDensity(canvas.getDensity());
    }

    public void setTargetDensity(DisplayMetrics metrics) {
        setTargetDensity(metrics.densityDpi);
    }

    public void setTargetDensity(int density) {
        if (this.mTargetDensity != density) {
            this.mTargetDensity = density == 0 ? 160 : density;
            if (this.mBitmapState.mBitmap != null) {
                computeBitmapSize();
            }
            invalidateSelf();
        }
    }

    public int getGravity() {
        return this.mBitmapState.mGravity;
    }

    public void setGravity(int gravity) {
        if (this.mBitmapState.mGravity != gravity) {
            this.mBitmapState.mGravity = gravity;
            this.mDstRectAndInsetsDirty = true;
            invalidateSelf();
        }
    }

    public void setMipMap(boolean mipMap) {
        if (this.mBitmapState.mBitmap != null) {
            this.mBitmapState.mBitmap.setHasMipMap(mipMap);
            invalidateSelf();
        }
    }

    public boolean hasMipMap() {
        return this.mBitmapState.mBitmap != null && this.mBitmapState.mBitmap.hasMipMap();
    }

    public void setAntiAlias(boolean aa) {
        this.mBitmapState.mPaint.setAntiAlias(aa);
        invalidateSelf();
    }

    public boolean hasAntiAlias() {
        return this.mBitmapState.mPaint.isAntiAlias();
    }

    @Override // android.graphics.drawable.Drawable
    public void setFilterBitmap(boolean filter) {
        this.mBitmapState.mPaint.setFilterBitmap(filter);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isFilterBitmap() {
        return this.mBitmapState.mPaint.isFilterBitmap();
    }

    @Override // android.graphics.drawable.Drawable
    public void setDither(boolean dither) {
        this.mBitmapState.mPaint.setDither(dither);
        invalidateSelf();
    }

    public Shader.TileMode getTileModeX() {
        return this.mBitmapState.mTileModeX;
    }

    public Shader.TileMode getTileModeY() {
        return this.mBitmapState.mTileModeY;
    }

    public void setTileModeX(Shader.TileMode mode) {
        setTileModeXY(mode, this.mBitmapState.mTileModeY);
    }

    public final void setTileModeY(Shader.TileMode mode) {
        setTileModeXY(this.mBitmapState.mTileModeX, mode);
    }

    public void setTileModeXY(Shader.TileMode xmode, Shader.TileMode ymode) {
        BitmapState state = this.mBitmapState;
        if (state.mTileModeX != xmode || state.mTileModeY != ymode) {
            state.mTileModeX = xmode;
            state.mTileModeY = ymode;
            state.mRebuildShader = true;
            this.mDstRectAndInsetsDirty = true;
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAutoMirrored(boolean mirrored) {
        if (this.mBitmapState.mAutoMirrored != mirrored) {
            this.mBitmapState.mAutoMirrored = mirrored;
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public final boolean isAutoMirrored() {
        return this.mBitmapState.mAutoMirrored;
    }

    @Override // android.graphics.drawable.Drawable
    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mBitmapState.getChangingConfigurations();
    }

    private synchronized boolean needMirroring() {
        return isAutoMirrored() && getLayoutDirection() == 1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.drawable.Drawable
    public void onBoundsChange(Rect bounds) {
        this.mDstRectAndInsetsDirty = true;
        Bitmap bitmap = this.mBitmapState.mBitmap;
        Shader shader = this.mBitmapState.mPaint.getShader();
        if (bitmap != null && shader != null) {
            updateShaderMatrix(bitmap, this.mBitmapState.mPaint, shader, needMirroring());
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        int restoreAlpha;
        Bitmap bitmap = this.mBitmapState.mBitmap;
        if (bitmap == null) {
            return;
        }
        BitmapState state = this.mBitmapState;
        Paint paint = state.mPaint;
        boolean clearColorFilter = false;
        if (state.mRebuildShader) {
            Shader.TileMode tmx = state.mTileModeX;
            Shader.TileMode tmy = state.mTileModeY;
            if (tmx == null && tmy == null) {
                paint.setShader(null);
            } else {
                paint.setShader(new BitmapShader(bitmap, tmx == null ? Shader.TileMode.CLAMP : tmx, tmy == null ? Shader.TileMode.CLAMP : tmy));
            }
            state.mRebuildShader = false;
        }
        if (state.mBaseAlpha != 1.0f) {
            Paint p = getPaint();
            restoreAlpha = p.getAlpha();
            p.setAlpha((int) ((restoreAlpha * state.mBaseAlpha) + 0.5f));
        } else {
            restoreAlpha = -1;
        }
        int restoreAlpha2 = restoreAlpha;
        if (this.mTintFilter != null && paint.getColorFilter() == null) {
            paint.setColorFilter(this.mTintFilter);
            clearColorFilter = true;
        }
        updateDstRectAndInsetsIfDirty();
        Shader shader = paint.getShader();
        boolean needMirroring = needMirroring();
        if (shader == null) {
            if (needMirroring) {
                canvas.save();
                canvas.translate(this.mDstRect.right - this.mDstRect.left, 0.0f);
                canvas.scale(-1.0f, 1.0f);
            }
            canvas.drawBitmap(bitmap, (Rect) null, this.mDstRect, paint);
            if (needMirroring) {
                canvas.restore();
            }
        } else {
            updateShaderMatrix(bitmap, paint, shader, needMirroring);
            canvas.drawRect(this.mDstRect, paint);
        }
        if (clearColorFilter) {
            paint.setColorFilter(null);
        }
        if (restoreAlpha2 >= 0) {
            paint.setAlpha(restoreAlpha2);
        }
    }

    private synchronized void updateShaderMatrix(Bitmap bitmap, Paint paint, Shader shader, boolean needMirroring) {
        int sourceDensity = bitmap.getDensity();
        int targetDensity = this.mTargetDensity;
        boolean needScaling = (sourceDensity == 0 || sourceDensity == targetDensity) ? false : true;
        if (needScaling || needMirroring) {
            Matrix matrix = getOrCreateMirrorMatrix();
            matrix.reset();
            if (needMirroring) {
                int dx = this.mDstRect.right - this.mDstRect.left;
                matrix.setTranslate(dx, 0.0f);
                matrix.setScale(-1.0f, 1.0f);
            }
            if (needScaling) {
                float densityScale = targetDensity / sourceDensity;
                matrix.postScale(densityScale, densityScale);
            }
            shader.setLocalMatrix(matrix);
        } else {
            this.mMirrorMatrix = null;
            shader.setLocalMatrix(Matrix.IDENTITY_MATRIX);
        }
        paint.setShader(shader);
    }

    private synchronized Matrix getOrCreateMirrorMatrix() {
        if (this.mMirrorMatrix == null) {
            this.mMirrorMatrix = new Matrix();
        }
        return this.mMirrorMatrix;
    }

    private synchronized void updateDstRectAndInsetsIfDirty() {
        if (this.mDstRectAndInsetsDirty) {
            if (this.mBitmapState.mTileModeX == null && this.mBitmapState.mTileModeY == null) {
                Rect bounds = getBounds();
                int layoutDirection = getLayoutDirection();
                Gravity.apply(this.mBitmapState.mGravity, this.mBitmapWidth, this.mBitmapHeight, bounds, this.mDstRect, layoutDirection);
                int left = this.mDstRect.left - bounds.left;
                int top = this.mDstRect.top - bounds.top;
                int right = bounds.right - this.mDstRect.right;
                int bottom = bounds.bottom - this.mDstRect.bottom;
                this.mOpticalInsets = Insets.of(left, top, right, bottom);
            } else {
                copyBounds(this.mDstRect);
                this.mOpticalInsets = Insets.NONE;
            }
        }
        this.mDstRectAndInsetsDirty = false;
    }

    private protected Insets getOpticalInsets() {
        updateDstRectAndInsetsIfDirty();
        return this.mOpticalInsets;
    }

    @Override // android.graphics.drawable.Drawable
    public void getOutline(Outline outline) {
        updateDstRectAndInsetsIfDirty();
        outline.setRect(this.mDstRect);
        boolean opaqueOverShape = (this.mBitmapState.mBitmap == null || this.mBitmapState.mBitmap.hasAlpha()) ? false : true;
        outline.setAlpha(opaqueOverShape ? getAlpha() / 255.0f : 0.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        int oldAlpha = this.mBitmapState.mPaint.getAlpha();
        if (alpha != oldAlpha) {
            this.mBitmapState.mPaint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.mBitmapState.mPaint.getAlpha();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.mBitmapState.mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public ColorFilter getColorFilter() {
        return this.mBitmapState.mPaint.getColorFilter();
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintList(ColorStateList tint) {
        BitmapState state = this.mBitmapState;
        if (state.mTint != tint) {
            state.mTint = tint;
            this.mTintFilter = updateTintFilter(this.mTintFilter, tint, this.mBitmapState.mTintMode);
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintMode(PorterDuff.Mode tintMode) {
        BitmapState state = this.mBitmapState;
        if (state.mTintMode != tintMode) {
            state.mTintMode = tintMode;
            this.mTintFilter = updateTintFilter(this.mTintFilter, this.mBitmapState.mTint, tintMode);
            invalidateSelf();
        }
    }

    private protected ColorStateList getTint() {
        return this.mBitmapState.mTint;
    }

    private protected PorterDuff.Mode getTintMode() {
        return this.mBitmapState.mTintMode;
    }

    @Override // android.graphics.drawable.Drawable
    public synchronized void setXfermode(Xfermode xfermode) {
        this.mBitmapState.mPaint.setXfermode(xfermode);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mBitmapState = new BitmapState(this.mBitmapState);
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
        BitmapState state = this.mBitmapState;
        if (state.mTint != null && state.mTintMode != null) {
            this.mTintFilter = updateTintFilter(this.mTintFilter, state.mTint, state.mTintMode);
            return true;
        }
        return false;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        return (this.mBitmapState.mTint != null && this.mBitmapState.mTint.isStateful()) || super.isStateful();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean hasFocusStateSpecified() {
        return this.mBitmapState.mTint != null && this.mBitmapState.mTint.hasFocusStateSpecified();
    }

    @Override // android.graphics.drawable.Drawable
    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        super.inflate(r, parser, attrs, theme);
        TypedArray a = obtainAttributes(r, theme, attrs, R.styleable.BitmapDrawable);
        updateStateFromTypedArray(a, this.mSrcDensityOverride);
        verifyRequiredAttributes(a);
        a.recycle();
        updateLocalState(r);
    }

    private synchronized void verifyRequiredAttributes(TypedArray a) throws XmlPullParserException {
        BitmapState state = this.mBitmapState;
        if (state.mBitmap == null) {
            if (state.mThemeAttrs == null || state.mThemeAttrs[1] == 0) {
                throw new XmlPullParserException(a.getPositionDescription() + ": <bitmap> requires a valid 'src' attribute");
            }
        }
    }

    private synchronized void updateStateFromTypedArray(TypedArray a, int srcDensityOverride) throws XmlPullParserException {
        Resources r = a.getResources();
        BitmapState state = this.mBitmapState;
        state.mChangingConfigurations |= a.getChangingConfigurations();
        state.mThemeAttrs = a.extractThemeAttrs();
        state.mSrcDensityOverride = srcDensityOverride;
        state.mTargetDensity = Drawable.resolveDensity(r, 0);
        int srcResId = a.getResourceId(1, 0);
        if (srcResId != 0) {
            TypedValue value = new TypedValue();
            r.getValueForDensity(srcResId, srcDensityOverride, value, true);
            if (srcDensityOverride > 0 && value.density > 0 && value.density != 65535) {
                if (value.density == srcDensityOverride) {
                    value.density = r.getDisplayMetrics().densityDpi;
                } else {
                    value.density = (value.density * r.getDisplayMetrics().densityDpi) / srcDensityOverride;
                }
            }
            int density = 0;
            if (value.density != 0) {
                if (value.density != 65535) {
                    density = value.density;
                }
            } else {
                density = 160;
            }
            Bitmap bitmap = null;
            try {
                InputStream is = r.openRawResource(srcResId, value);
                ImageDecoder.Source source = ImageDecoder.createSource(r, is, density);
                bitmap = ImageDecoder.decodeBitmap(source, new ImageDecoder.OnHeaderDecodedListener() { // from class: android.graphics.drawable.-$$Lambda$BitmapDrawable$LMqt8JvxZ4giSOIRAtlCKDg39Jw
                    @Override // android.graphics.ImageDecoder.OnHeaderDecodedListener
                    public final void onHeaderDecoded(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source2) {
                        imageDecoder.setAllocator(1);
                    }
                });
                if (is != null) {
                    $closeResource(null, is);
                }
            } catch (Exception e) {
            }
            if (bitmap == null) {
                throw new XmlPullParserException(a.getPositionDescription() + ": <bitmap> requires a valid 'src' attribute");
            }
            state.mBitmap = bitmap;
        }
        boolean defMipMap = state.mBitmap != null ? state.mBitmap.hasMipMap() : false;
        setMipMap(a.getBoolean(8, defMipMap));
        state.mAutoMirrored = a.getBoolean(9, state.mAutoMirrored);
        state.mBaseAlpha = a.getFloat(7, state.mBaseAlpha);
        int tintMode = a.getInt(10, -1);
        if (tintMode != -1) {
            state.mTintMode = Drawable.parseTintMode(tintMode, PorterDuff.Mode.SRC_IN);
        }
        ColorStateList tint = a.getColorStateList(5);
        if (tint != null) {
            state.mTint = tint;
        }
        Paint paint = this.mBitmapState.mPaint;
        paint.setAntiAlias(a.getBoolean(2, paint.isAntiAlias()));
        paint.setFilterBitmap(a.getBoolean(3, paint.isFilterBitmap()));
        paint.setDither(a.getBoolean(4, paint.isDither()));
        setGravity(a.getInt(0, state.mGravity));
        int tileMode = a.getInt(6, -2);
        if (tileMode != -2) {
            Shader.TileMode mode = parseTileMode(tileMode);
            setTileModeXY(mode, mode);
        }
        int tileModeX = a.getInt(11, -2);
        if (tileModeX != -2) {
            setTileModeX(parseTileMode(tileModeX));
        }
        int tileModeY = a.getInt(12, -2);
        if (tileModeY != -2) {
            setTileModeY(parseTileMode(tileModeY));
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void applyTheme(Resources.Theme t) {
        super.applyTheme(t);
        BitmapState state = this.mBitmapState;
        if (state == null) {
            return;
        }
        if (state.mThemeAttrs != null) {
            TypedArray a = t.resolveAttributes(state.mThemeAttrs, R.styleable.BitmapDrawable);
            try {
                try {
                    updateStateFromTypedArray(a, state.mSrcDensityOverride);
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

    private static synchronized Shader.TileMode parseTileMode(int tileMode) {
        switch (tileMode) {
            case 0:
                return Shader.TileMode.CLAMP;
            case 1:
                return Shader.TileMode.REPEAT;
            case 2:
                return Shader.TileMode.MIRROR;
            default:
                return null;
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean canApplyTheme() {
        return this.mBitmapState != null && this.mBitmapState.canApplyTheme();
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
        Bitmap bitmap;
        return (this.mBitmapState.mGravity == 119 && (bitmap = this.mBitmapState.mBitmap) != null && !bitmap.hasAlpha() && this.mBitmapState.mPaint.getAlpha() >= 255) ? -1 : -3;
    }

    @Override // android.graphics.drawable.Drawable
    public final Drawable.ConstantState getConstantState() {
        this.mBitmapState.mChangingConfigurations |= getChangingConfigurations();
        return this.mBitmapState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class BitmapState extends Drawable.ConstantState {
        boolean mAutoMirrored;
        float mBaseAlpha;
        Bitmap mBitmap;
        int mChangingConfigurations;
        int mGravity;
        final Paint mPaint;
        boolean mRebuildShader;
        int mSrcDensityOverride;
        int mTargetDensity;
        int[] mThemeAttrs;
        Shader.TileMode mTileModeX;
        Shader.TileMode mTileModeY;
        ColorStateList mTint;
        PorterDuff.Mode mTintMode;

        synchronized BitmapState(Bitmap bitmap) {
            this.mThemeAttrs = null;
            this.mBitmap = null;
            this.mTint = null;
            this.mTintMode = Drawable.DEFAULT_TINT_MODE;
            this.mGravity = 119;
            this.mBaseAlpha = 1.0f;
            this.mTileModeX = null;
            this.mTileModeY = null;
            this.mSrcDensityOverride = 0;
            this.mTargetDensity = 160;
            this.mAutoMirrored = false;
            this.mBitmap = bitmap;
            this.mPaint = new Paint(6);
        }

        synchronized BitmapState(BitmapState bitmapState) {
            this.mThemeAttrs = null;
            this.mBitmap = null;
            this.mTint = null;
            this.mTintMode = Drawable.DEFAULT_TINT_MODE;
            this.mGravity = 119;
            this.mBaseAlpha = 1.0f;
            this.mTileModeX = null;
            this.mTileModeY = null;
            this.mSrcDensityOverride = 0;
            this.mTargetDensity = 160;
            this.mAutoMirrored = false;
            this.mBitmap = bitmapState.mBitmap;
            this.mTint = bitmapState.mTint;
            this.mTintMode = bitmapState.mTintMode;
            this.mThemeAttrs = bitmapState.mThemeAttrs;
            this.mChangingConfigurations = bitmapState.mChangingConfigurations;
            this.mGravity = bitmapState.mGravity;
            this.mTileModeX = bitmapState.mTileModeX;
            this.mTileModeY = bitmapState.mTileModeY;
            this.mSrcDensityOverride = bitmapState.mSrcDensityOverride;
            this.mTargetDensity = bitmapState.mTargetDensity;
            this.mBaseAlpha = bitmapState.mBaseAlpha;
            this.mPaint = new Paint(bitmapState.mPaint);
            this.mRebuildShader = bitmapState.mRebuildShader;
            this.mAutoMirrored = bitmapState.mAutoMirrored;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public boolean canApplyTheme() {
            return this.mThemeAttrs != null || (this.mTint != null && this.mTint.canApplyTheme());
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            return new BitmapDrawable(this, null);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources res) {
            return new BitmapDrawable(this, res);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return this.mChangingConfigurations | (this.mTint != null ? this.mTint.getChangingConfigurations() : 0);
        }
    }

    private synchronized BitmapDrawable(BitmapState state, Resources res) {
        this.mDstRect = new Rect();
        this.mTargetDensity = 160;
        this.mDstRectAndInsetsDirty = true;
        this.mOpticalInsets = Insets.NONE;
        init(state, res);
    }

    private synchronized void init(BitmapState state, Resources res) {
        this.mBitmapState = state;
        updateLocalState(res);
        if (this.mBitmapState != null && res != null) {
            this.mBitmapState.mTargetDensity = this.mTargetDensity;
        }
    }

    private synchronized void updateLocalState(Resources res) {
        this.mTargetDensity = resolveDensity(res, this.mBitmapState.mTargetDensity);
        this.mTintFilter = updateTintFilter(this.mTintFilter, this.mBitmapState.mTint, this.mBitmapState.mTintMode);
        computeBitmapSize();
    }
}
