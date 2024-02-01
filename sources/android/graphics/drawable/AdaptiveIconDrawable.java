package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.PathParser;
import com.android.internal.R;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class AdaptiveIconDrawable extends Drawable implements Drawable.Callback {
    private static final int BACKGROUND_ID = 0;
    private static final float DEFAULT_VIEW_PORT_SCALE = 0.6666667f;
    private static final float EXTRA_INSET_PERCENTAGE = 0.25f;
    private static final int FOREGROUND_ID = 1;
    public static final float MASK_SIZE = 100.0f;
    private static final float SAFEZONE_SCALE = 0.9166667f;
    private static Path sMask;
    private final Canvas mCanvas;
    private boolean mChildRequestedInvalidation;
    private Rect mHotspotBounds;
    LayerState mLayerState;
    private Bitmap mLayersBitmap;
    private Shader mLayersShader;
    private final Path mMask;
    private Bitmap mMaskBitmap;
    private final Matrix mMaskMatrix;
    private boolean mMutated;
    private Paint mPaint;
    private boolean mSuspendChildInvalidation;
    private final Rect mTmpOutRect;
    private final Region mTransparentRegion;

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized AdaptiveIconDrawable() {
        this((LayerState) null, (Resources) null);
    }

    synchronized AdaptiveIconDrawable(LayerState state, Resources res) {
        this.mTmpOutRect = new Rect();
        this.mPaint = new Paint(7);
        this.mLayerState = createConstantState(state, res);
        if (sMask == null) {
            sMask = PathParser.createPathFromPathData(Resources.getSystem().getString(R.string.config_icon_mask));
        }
        this.mMask = PathParser.createPathFromPathData(Resources.getSystem().getString(R.string.config_icon_mask));
        this.mMaskMatrix = new Matrix();
        this.mCanvas = new Canvas();
        this.mTransparentRegion = new Region();
    }

    private synchronized ChildDrawable createChildDrawable(Drawable drawable) {
        ChildDrawable layer = new ChildDrawable(this.mLayerState.mDensity);
        layer.mDrawable = drawable;
        layer.mDrawable.setCallback(this);
        this.mLayerState.mChildrenChangingConfigurations |= layer.mDrawable.getChangingConfigurations();
        return layer;
    }

    synchronized LayerState createConstantState(LayerState state, Resources res) {
        return new LayerState(state, this, res);
    }

    public AdaptiveIconDrawable(Drawable backgroundDrawable, Drawable foregroundDrawable) {
        this((LayerState) null, (Resources) null);
        if (backgroundDrawable != null) {
            addLayer(0, createChildDrawable(backgroundDrawable));
        }
        if (foregroundDrawable != null) {
            addLayer(1, createChildDrawable(foregroundDrawable));
        }
    }

    private synchronized void addLayer(int index, ChildDrawable layer) {
        this.mLayerState.mChildren[index] = layer;
        this.mLayerState.invalidateCache();
    }

    @Override // android.graphics.drawable.Drawable
    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        super.inflate(r, parser, attrs, theme);
        LayerState state = this.mLayerState;
        if (state == null) {
            return;
        }
        int deviceDensity = Drawable.resolveDensity(r, 0);
        state.setDensity(deviceDensity);
        state.mSrcDensityOverride = this.mSrcDensityOverride;
        ChildDrawable[] array = state.mChildren;
        for (int i = 0; i < state.mChildren.length; i++) {
            ChildDrawable layer = array[i];
            layer.setDensity(deviceDensity);
        }
        inflateLayers(r, parser, attrs, theme);
    }

    public static float getExtraInsetFraction() {
        return 0.25f;
    }

    public static synchronized float getExtraInsetPercentage() {
        return 0.25f;
    }

    public Path getIconMask() {
        return this.mMask;
    }

    public Drawable getForeground() {
        return this.mLayerState.mChildren[1].mDrawable;
    }

    public Drawable getBackground() {
        return this.mLayerState.mChildren[0].mDrawable;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.drawable.Drawable
    public void onBoundsChange(Rect bounds) {
        if (bounds.isEmpty()) {
            return;
        }
        updateLayerBounds(bounds);
    }

    private synchronized void updateLayerBounds(Rect bounds) {
        if (bounds.isEmpty()) {
            return;
        }
        try {
            suspendChildInvalidation();
            updateLayerBoundsInternal(bounds);
            updateMaskBoundsInternal(bounds);
        } finally {
            resumeChildInvalidation();
        }
    }

    private synchronized void updateLayerBoundsInternal(Rect bounds) {
        Drawable d;
        int cX = bounds.width() / 2;
        int cY = bounds.height() / 2;
        LayerState layerState = this.mLayerState;
        for (int i = 0; i < 2; i++) {
            ChildDrawable r = this.mLayerState.mChildren[i];
            if (r != null && (d = r.mDrawable) != null) {
                int insetWidth = (int) (bounds.width() / 1.3333334f);
                int insetHeight = (int) (bounds.height() / 1.3333334f);
                Rect outRect = this.mTmpOutRect;
                outRect.set(cX - insetWidth, cY - insetHeight, cX + insetWidth, cY + insetHeight);
                d.setBounds(outRect);
            }
        }
    }

    private synchronized void updateMaskBoundsInternal(Rect b) {
        this.mMaskMatrix.setScale(b.width() / 100.0f, b.height() / 100.0f);
        sMask.transform(this.mMaskMatrix, this.mMask);
        if (this.mMaskBitmap == null || this.mMaskBitmap.getWidth() != b.width() || this.mMaskBitmap.getHeight() != b.height()) {
            this.mMaskBitmap = Bitmap.createBitmap(b.width(), b.height(), Bitmap.Config.ALPHA_8);
            this.mLayersBitmap = Bitmap.createBitmap(b.width(), b.height(), Bitmap.Config.ARGB_8888);
        }
        this.mCanvas.setBitmap(this.mMaskBitmap);
        this.mPaint.setShader(null);
        this.mCanvas.drawPath(this.mMask, this.mPaint);
        this.mMaskMatrix.postTranslate(b.left, b.top);
        this.mMask.reset();
        sMask.transform(this.mMaskMatrix, this.mMask);
        this.mTransparentRegion.setEmpty();
        this.mLayersShader = null;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Drawable dr;
        if (this.mLayersBitmap == null) {
            return;
        }
        if (this.mLayersShader == null) {
            this.mCanvas.setBitmap(this.mLayersBitmap);
            this.mCanvas.drawColor(-16777216);
            int i = 0;
            while (true) {
                LayerState layerState = this.mLayerState;
                if (i >= 2) {
                    break;
                }
                if (this.mLayerState.mChildren[i] != null && (dr = this.mLayerState.mChildren[i].mDrawable) != null) {
                    dr.draw(this.mCanvas);
                }
                i++;
            }
            this.mLayersShader = new BitmapShader(this.mLayersBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            this.mPaint.setShader(this.mLayersShader);
        }
        if (this.mMaskBitmap != null) {
            Rect bounds = getBounds();
            canvas.drawBitmap(this.mMaskBitmap, bounds.left, bounds.top, this.mPaint);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void invalidateSelf() {
        this.mLayersShader = null;
        super.invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void getOutline(Outline outline) {
        outline.setConvexPath(this.mMask);
    }

    public Region getSafeZone() {
        this.mMaskMatrix.reset();
        this.mMaskMatrix.setScale(SAFEZONE_SCALE, SAFEZONE_SCALE, getBounds().centerX(), getBounds().centerY());
        Path p = new Path();
        this.mMask.transform(this.mMaskMatrix, p);
        Region safezoneRegion = new Region(getBounds());
        safezoneRegion.setPath(p, safezoneRegion);
        return safezoneRegion;
    }

    @Override // android.graphics.drawable.Drawable
    public Region getTransparentRegion() {
        if (this.mTransparentRegion.isEmpty()) {
            this.mMask.toggleInverseFillType();
            this.mTransparentRegion.set(getBounds());
            this.mTransparentRegion.setPath(this.mMask, this.mTransparentRegion);
            this.mMask.toggleInverseFillType();
        }
        return this.mTransparentRegion;
    }

    @Override // android.graphics.drawable.Drawable
    public void applyTheme(Resources.Theme t) {
        super.applyTheme(t);
        LayerState state = this.mLayerState;
        if (state == null) {
            return;
        }
        int density = Drawable.resolveDensity(t.getResources(), 0);
        state.setDensity(density);
        ChildDrawable[] array = state.mChildren;
        for (int i = 0; i < 2; i++) {
            ChildDrawable layer = array[i];
            layer.setDensity(density);
            if (layer.mThemeAttrs != null) {
                TypedArray a = t.resolveAttributes(layer.mThemeAttrs, R.styleable.AdaptiveIconDrawableLayer);
                updateLayerFromTypedArray(layer, a);
                a.recycle();
            }
            Drawable d = layer.mDrawable;
            if (d != null && d.canApplyTheme()) {
                d.applyTheme(t);
                state.mChildrenChangingConfigurations |= d.getChangingConfigurations();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x00ac, code lost:
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized void inflateLayers(android.content.res.Resources r17, org.xmlpull.v1.XmlPullParser r18, android.util.AttributeSet r19, android.content.res.Resources.Theme r20) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            r16 = this;
            r0 = r16
            r1 = r17
            r2 = r19
            r3 = r20
            android.graphics.drawable.AdaptiveIconDrawable$LayerState r4 = r0.mLayerState
            int r5 = r18.getDepth()
            r6 = 1
            int r5 = r5 + r6
            r7 = 0
        L11:
            int r8 = r18.next()
            r9 = r8
            if (r8 == r6) goto Laa
            int r8 = r18.getDepth()
            r10 = r8
            if (r8 >= r5) goto L22
            r8 = 3
            if (r9 == r8) goto Laa
        L22:
            r8 = 2
            if (r9 == r8) goto L29
        L26:
            r14 = r18
            goto L11
        L29:
            if (r10 <= r5) goto L2c
            goto L26
        L2c:
            java.lang.String r11 = r18.getName()
            java.lang.String r12 = "background"
            boolean r12 = r11.equals(r12)
            if (r12 == 0) goto L3a
            r7 = 0
            goto L43
        L3a:
            java.lang.String r12 = "foreground"
            boolean r12 = r11.equals(r12)
            if (r12 == 0) goto L26
            r7 = 1
        L43:
            android.graphics.drawable.AdaptiveIconDrawable$ChildDrawable r12 = new android.graphics.drawable.AdaptiveIconDrawable$ChildDrawable
            int r13 = r4.mDensity
            r12.<init>(r13)
            int[] r13 = com.android.internal.R.styleable.AdaptiveIconDrawableLayer
            android.content.res.TypedArray r13 = obtainAttributes(r1, r3, r2, r13)
            r0.updateLayerFromTypedArray(r12, r13)
            r13.recycle()
            android.graphics.drawable.Drawable r14 = r12.mDrawable
            if (r14 != 0) goto La3
            int[] r14 = r12.mThemeAttrs
            if (r14 != 0) goto La3
        L5e:
            int r14 = r18.next()
            r9 = r14
            r15 = 4
            if (r14 != r15) goto L67
            goto L5e
        L67:
            if (r9 != r8) goto L86
            android.graphics.drawable.AdaptiveIconDrawable$LayerState r8 = r0.mLayerState
            int r8 = r8.mSrcDensityOverride
            r14 = r18
            android.graphics.drawable.Drawable r8 = android.graphics.drawable.Drawable.createFromXmlInnerForDensity(r1, r14, r2, r8, r3)
            r12.mDrawable = r8
            android.graphics.drawable.Drawable r8 = r12.mDrawable
            r8.setCallback(r0)
            int r8 = r4.mChildrenChangingConfigurations
            android.graphics.drawable.Drawable r15 = r12.mDrawable
            int r15 = r15.getChangingConfigurations()
            r8 = r8 | r15
            r4.mChildrenChangingConfigurations = r8
            goto La5
        L86:
            r14 = r18
            org.xmlpull.v1.XmlPullParserException r6 = new org.xmlpull.v1.XmlPullParserException
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r15 = r18.getPositionDescription()
            r8.append(r15)
            java.lang.String r15 = ": <foreground> or <background> tag requires a 'drawable'attribute or child tag defining a drawable"
            r8.append(r15)
            java.lang.String r8 = r8.toString()
            r6.<init>(r8)
            throw r6
        La3:
            r14 = r18
        La5:
            r0.addLayer(r7, r12)
            goto L11
        Laa:
            r14 = r18
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.drawable.AdaptiveIconDrawable.inflateLayers(android.content.res.Resources, org.xmlpull.v1.XmlPullParser, android.util.AttributeSet, android.content.res.Resources$Theme):void");
    }

    private synchronized void updateLayerFromTypedArray(ChildDrawable layer, TypedArray a) {
        LayerState state = this.mLayerState;
        state.mChildrenChangingConfigurations |= a.getChangingConfigurations();
        layer.mThemeAttrs = a.extractThemeAttrs();
        Drawable dr = a.getDrawableForDensity(0, state.mSrcDensityOverride);
        if (dr != null) {
            if (layer.mDrawable != null) {
                layer.mDrawable.setCallback(null);
            }
            layer.mDrawable = dr;
            layer.mDrawable.setCallback(this);
            state.mChildrenChangingConfigurations |= layer.mDrawable.getChangingConfigurations();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean canApplyTheme() {
        return (this.mLayerState != null && this.mLayerState.canApplyTheme()) || super.canApplyTheme();
    }

    public synchronized boolean isProjected() {
        if (super.isProjected()) {
            return true;
        }
        ChildDrawable[] layers = this.mLayerState.mChildren;
        int i = 0;
        while (true) {
            LayerState layerState = this.mLayerState;
            if (i >= 2) {
                return false;
            }
            if (layers[i].mDrawable.isProjected()) {
                return true;
            }
            i++;
        }
    }

    private synchronized void suspendChildInvalidation() {
        this.mSuspendChildInvalidation = true;
    }

    private synchronized void resumeChildInvalidation() {
        this.mSuspendChildInvalidation = false;
        if (this.mChildRequestedInvalidation) {
            this.mChildRequestedInvalidation = false;
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable who) {
        if (this.mSuspendChildInvalidation) {
            this.mChildRequestedInvalidation = true;
        } else {
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        scheduleSelf(what, when);
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void unscheduleDrawable(Drawable who, Runnable what) {
        unscheduleSelf(what);
    }

    @Override // android.graphics.drawable.Drawable
    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mLayerState.getChangingConfigurations();
    }

    @Override // android.graphics.drawable.Drawable
    public void setHotspot(float x, float y) {
        ChildDrawable[] array = this.mLayerState.mChildren;
        int i = 0;
        while (true) {
            LayerState layerState = this.mLayerState;
            if (i < 2) {
                Drawable dr = array[i].mDrawable;
                if (dr != null) {
                    dr.setHotspot(x, y);
                }
                i++;
            } else {
                return;
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setHotspotBounds(int left, int top, int right, int bottom) {
        ChildDrawable[] array = this.mLayerState.mChildren;
        int i = 0;
        while (true) {
            LayerState layerState = this.mLayerState;
            if (i >= 2) {
                break;
            }
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setHotspotBounds(left, top, right, bottom);
            }
            i++;
        }
        if (this.mHotspotBounds == null) {
            this.mHotspotBounds = new Rect(left, top, right, bottom);
        } else {
            this.mHotspotBounds.set(left, top, right, bottom);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void getHotspotBounds(Rect outRect) {
        if (this.mHotspotBounds != null) {
            outRect.set(this.mHotspotBounds);
        } else {
            super.getHotspotBounds(outRect);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setVisible(boolean visible, boolean restart) {
        boolean changed = super.setVisible(visible, restart);
        ChildDrawable[] array = this.mLayerState.mChildren;
        int i = 0;
        while (true) {
            LayerState layerState = this.mLayerState;
            if (i < 2) {
                Drawable dr = array[i].mDrawable;
                if (dr != null) {
                    dr.setVisible(visible, restart);
                }
                i++;
            } else {
                return changed;
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setDither(boolean dither) {
        ChildDrawable[] array = this.mLayerState.mChildren;
        int i = 0;
        while (true) {
            LayerState layerState = this.mLayerState;
            if (i < 2) {
                Drawable dr = array[i].mDrawable;
                if (dr != null) {
                    dr.setDither(dither);
                }
                i++;
            } else {
                return;
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        this.mPaint.setAlpha(alpha);
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        ChildDrawable[] array = this.mLayerState.mChildren;
        int i = 0;
        while (true) {
            LayerState layerState = this.mLayerState;
            if (i < 2) {
                Drawable dr = array[i].mDrawable;
                if (dr != null) {
                    dr.setColorFilter(colorFilter);
                }
                i++;
            } else {
                return;
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintList(ColorStateList tint) {
        ChildDrawable[] array = this.mLayerState.mChildren;
        LayerState layerState = this.mLayerState;
        for (int i = 0; i < 2; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setTintList(tint);
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintMode(PorterDuff.Mode tintMode) {
        ChildDrawable[] array = this.mLayerState.mChildren;
        LayerState layerState = this.mLayerState;
        for (int i = 0; i < 2; i++) {
            Drawable dr = array[i].mDrawable;
            if (dr != null) {
                dr.setTintMode(tintMode);
            }
        }
    }

    public void setOpacity(int opacity) {
        this.mLayerState.mOpacityOverride = opacity;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        if (this.mLayerState.mOpacityOverride != 0) {
            return this.mLayerState.mOpacityOverride;
        }
        return this.mLayerState.getOpacity();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAutoMirrored(boolean mirrored) {
        this.mLayerState.mAutoMirrored = mirrored;
        ChildDrawable[] array = this.mLayerState.mChildren;
        int i = 0;
        while (true) {
            LayerState layerState = this.mLayerState;
            if (i < 2) {
                Drawable dr = array[i].mDrawable;
                if (dr != null) {
                    dr.setAutoMirrored(mirrored);
                }
                i++;
            } else {
                return;
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isAutoMirrored() {
        return this.mLayerState.mAutoMirrored;
    }

    @Override // android.graphics.drawable.Drawable
    public void jumpToCurrentState() {
        ChildDrawable[] array = this.mLayerState.mChildren;
        int i = 0;
        while (true) {
            LayerState layerState = this.mLayerState;
            if (i < 2) {
                Drawable dr = array[i].mDrawable;
                if (dr != null) {
                    dr.jumpToCurrentState();
                }
                i++;
            } else {
                return;
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        return this.mLayerState.isStateful();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean hasFocusStateSpecified() {
        return this.mLayerState.hasFocusStateSpecified();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.drawable.Drawable
    public boolean onStateChange(int[] state) {
        boolean changed = false;
        ChildDrawable[] array = this.mLayerState.mChildren;
        int i = 0;
        while (true) {
            LayerState layerState = this.mLayerState;
            if (i >= 2) {
                break;
            }
            Drawable dr = array[i].mDrawable;
            if (dr != null && dr.isStateful() && dr.setState(state)) {
                changed = true;
            }
            i++;
        }
        if (changed) {
            updateLayerBounds(getBounds());
        }
        return changed;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.drawable.Drawable
    public boolean onLevelChange(int level) {
        boolean changed = false;
        ChildDrawable[] array = this.mLayerState.mChildren;
        int i = 0;
        while (true) {
            LayerState layerState = this.mLayerState;
            if (i >= 2) {
                break;
            }
            Drawable dr = array[i].mDrawable;
            if (dr != null && dr.setLevel(level)) {
                changed = true;
            }
            i++;
        }
        if (changed) {
            updateLayerBounds(getBounds());
        }
        return changed;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return (int) (getMaxIntrinsicWidth() * DEFAULT_VIEW_PORT_SCALE);
    }

    private synchronized int getMaxIntrinsicWidth() {
        int w;
        int width = -1;
        int i = 0;
        while (true) {
            LayerState layerState = this.mLayerState;
            if (i < 2) {
                ChildDrawable r = this.mLayerState.mChildren[i];
                if (r.mDrawable != null && (w = r.mDrawable.getIntrinsicWidth()) > width) {
                    width = w;
                }
                i++;
            } else {
                return width;
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return (int) (getMaxIntrinsicHeight() * DEFAULT_VIEW_PORT_SCALE);
    }

    private synchronized int getMaxIntrinsicHeight() {
        int h;
        int height = -1;
        int i = 0;
        while (true) {
            LayerState layerState = this.mLayerState;
            if (i < 2) {
                ChildDrawable r = this.mLayerState.mChildren[i];
                if (r.mDrawable != null && (h = r.mDrawable.getIntrinsicHeight()) > height) {
                    height = h;
                }
                i++;
            } else {
                return height;
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        if (this.mLayerState.canConstantState()) {
            this.mLayerState.mChangingConfigurations = getChangingConfigurations();
            return this.mLayerState;
        }
        return null;
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mLayerState = createConstantState(this.mLayerState, null);
            int i = 0;
            while (true) {
                LayerState layerState = this.mLayerState;
                if (i >= 2) {
                    break;
                }
                Drawable dr = this.mLayerState.mChildren[i].mDrawable;
                if (dr != null) {
                    dr.mutate();
                }
                i++;
            }
            this.mMutated = true;
        }
        return this;
    }

    @Override // android.graphics.drawable.Drawable
    public synchronized void clearMutated() {
        super.clearMutated();
        ChildDrawable[] array = this.mLayerState.mChildren;
        int i = 0;
        while (true) {
            LayerState layerState = this.mLayerState;
            if (i < 2) {
                Drawable dr = array[i].mDrawable;
                if (dr != null) {
                    dr.clearMutated();
                }
                i++;
            } else {
                this.mMutated = false;
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ChildDrawable {
        public int mDensity;
        public Drawable mDrawable;
        public int[] mThemeAttrs;

        synchronized ChildDrawable(int density) {
            this.mDensity = 160;
            this.mDensity = density;
        }

        synchronized ChildDrawable(ChildDrawable orig, AdaptiveIconDrawable owner, Resources res) {
            Drawable clone;
            this.mDensity = 160;
            Drawable dr = orig.mDrawable;
            if (dr != null) {
                Drawable.ConstantState cs = dr.getConstantState();
                if (cs == null) {
                    clone = dr;
                } else if (res != null) {
                    clone = cs.newDrawable(res);
                } else {
                    clone = cs.newDrawable();
                }
                clone.setCallback(owner);
                clone.setBounds(dr.getBounds());
                clone.setLevel(dr.getLevel());
            } else {
                clone = null;
            }
            this.mDrawable = clone;
            this.mThemeAttrs = orig.mThemeAttrs;
            this.mDensity = Drawable.resolveDensity(res, orig.mDensity);
        }

        public synchronized boolean canApplyTheme() {
            return this.mThemeAttrs != null || (this.mDrawable != null && this.mDrawable.canApplyTheme());
        }

        public final synchronized void setDensity(int targetDensity) {
            if (this.mDensity != targetDensity) {
                this.mDensity = targetDensity;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class LayerState extends Drawable.ConstantState {
        static final int N_CHILDREN = 2;
        private boolean mAutoMirrored;
        int mChangingConfigurations;
        private boolean mCheckedOpacity;
        private boolean mCheckedStateful;
        ChildDrawable[] mChildren;
        int mChildrenChangingConfigurations;
        int mDensity;
        private boolean mIsStateful;
        private int mOpacity;
        int mOpacityOverride;
        int mSrcDensityOverride;
        private int[] mThemeAttrs;

        synchronized LayerState(LayerState orig, AdaptiveIconDrawable owner, Resources res) {
            int i = 0;
            this.mSrcDensityOverride = 0;
            this.mOpacityOverride = 0;
            this.mAutoMirrored = false;
            this.mDensity = Drawable.resolveDensity(res, orig != null ? orig.mDensity : 0);
            this.mChildren = new ChildDrawable[2];
            if (orig == null) {
                while (i < 2) {
                    this.mChildren[i] = new ChildDrawable(this.mDensity);
                    i++;
                }
                return;
            }
            ChildDrawable[] origChildDrawable = orig.mChildren;
            this.mChangingConfigurations = orig.mChangingConfigurations;
            this.mChildrenChangingConfigurations = orig.mChildrenChangingConfigurations;
            while (i < 2) {
                ChildDrawable or = origChildDrawable[i];
                this.mChildren[i] = new ChildDrawable(or, owner, res);
                i++;
            }
            this.mCheckedOpacity = orig.mCheckedOpacity;
            this.mOpacity = orig.mOpacity;
            this.mCheckedStateful = orig.mCheckedStateful;
            this.mIsStateful = orig.mIsStateful;
            this.mAutoMirrored = orig.mAutoMirrored;
            this.mThemeAttrs = orig.mThemeAttrs;
            this.mOpacityOverride = orig.mOpacityOverride;
            this.mSrcDensityOverride = orig.mSrcDensityOverride;
        }

        public final synchronized void setDensity(int targetDensity) {
            if (this.mDensity != targetDensity) {
                this.mDensity = targetDensity;
            }
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public boolean canApplyTheme() {
            if (this.mThemeAttrs != null || super.canApplyTheme()) {
                return true;
            }
            ChildDrawable[] array = this.mChildren;
            for (int i = 0; i < 2; i++) {
                ChildDrawable layer = array[i];
                if (layer.canApplyTheme()) {
                    return true;
                }
            }
            return false;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            return new AdaptiveIconDrawable(this, (Resources) null);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources res) {
            return new AdaptiveIconDrawable(this, res);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return this.mChangingConfigurations | this.mChildrenChangingConfigurations;
        }

        public final synchronized int getOpacity() {
            int op;
            if (this.mCheckedOpacity) {
                return this.mOpacity;
            }
            ChildDrawable[] array = this.mChildren;
            int firstIndex = -1;
            int i = 0;
            while (true) {
                if (i < 2) {
                    if (array[i].mDrawable == null) {
                        i++;
                    } else {
                        firstIndex = i;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (firstIndex >= 0) {
                op = array[firstIndex].mDrawable.getOpacity();
            } else {
                op = -2;
            }
            for (int i2 = firstIndex + 1; i2 < 2; i2++) {
                Drawable dr = array[i2].mDrawable;
                if (dr != null) {
                    op = Drawable.resolveOpacity(op, dr.getOpacity());
                }
            }
            this.mOpacity = op;
            this.mCheckedOpacity = true;
            return op;
        }

        public final synchronized boolean isStateful() {
            if (this.mCheckedStateful) {
                return this.mIsStateful;
            }
            ChildDrawable[] array = this.mChildren;
            boolean isStateful = false;
            int i = 0;
            while (true) {
                if (i < 2) {
                    Drawable dr = array[i].mDrawable;
                    if (dr == null || !dr.isStateful()) {
                        i++;
                    } else {
                        isStateful = true;
                        break;
                    }
                } else {
                    break;
                }
            }
            this.mIsStateful = isStateful;
            this.mCheckedStateful = true;
            return isStateful;
        }

        public final synchronized boolean hasFocusStateSpecified() {
            ChildDrawable[] array = this.mChildren;
            for (int i = 0; i < 2; i++) {
                Drawable dr = array[i].mDrawable;
                if (dr != null && dr.hasFocusStateSpecified()) {
                    return true;
                }
            }
            return false;
        }

        public final synchronized boolean canConstantState() {
            ChildDrawable[] array = this.mChildren;
            for (int i = 0; i < 2; i++) {
                Drawable dr = array[i].mDrawable;
                if (dr != null && dr.getConstantState() == null) {
                    return false;
                }
            }
            return true;
        }

        public synchronized void invalidateCache() {
            this.mCheckedOpacity = false;
            this.mCheckedStateful = false;
        }
    }
}
