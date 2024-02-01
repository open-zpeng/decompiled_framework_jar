package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import com.android.internal.R;
import java.io.IOException;
import java.util.Arrays;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class RippleDrawable extends LayerDrawable {
    private static final int MASK_CONTENT = 1;
    private static final int MASK_EXPLICIT = 2;
    private static final int MASK_NONE = 0;
    private static final int MASK_UNKNOWN = -1;
    private static final int MAX_RIPPLES = 10;
    public static final int RADIUS_AUTO = -1;
    private RippleBackground mBackground;
    public protected int mDensity;
    private final Rect mDirtyBounds;
    private final Rect mDrawingBounds;
    private RippleForeground[] mExitingRipples;
    private int mExitingRipplesCount;
    private boolean mForceSoftware;
    private boolean mHasPending;
    private boolean mHasValidMask;
    private final Rect mHotspotBounds;
    private Drawable mMask;
    private Bitmap mMaskBuffer;
    private Canvas mMaskCanvas;
    private PorterDuffColorFilter mMaskColorFilter;
    private Matrix mMaskMatrix;
    private BitmapShader mMaskShader;
    private boolean mOverrideBounds;
    private float mPendingX;
    private float mPendingY;
    private RippleForeground mRipple;
    private boolean mRippleActive;
    private Paint mRipplePaint;
    public protected RippleState mState;
    private final Rect mTempRect;

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized RippleDrawable() {
        this(new RippleState(null, null, null), null);
    }

    public RippleDrawable(ColorStateList color, Drawable content, Drawable mask) {
        this(new RippleState(null, null, null), null);
        if (color == null) {
            throw new IllegalArgumentException("RippleDrawable requires a non-null color");
        }
        if (content != null) {
            addLayer(content, null, 0, 0, 0, 0, 0);
        }
        if (mask != null) {
            addLayer(mask, null, 16908334, 0, 0, 0, 0);
        }
        setColor(color);
        ensurePadding();
        refreshPadding();
        updateLocalState();
    }

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public void jumpToCurrentState() {
        super.jumpToCurrentState();
        if (this.mRipple != null) {
            this.mRipple.end();
        }
        if (this.mBackground != null) {
            this.mBackground.jumpToFinal();
        }
        cancelExitingRipples();
    }

    private synchronized void cancelExitingRipples() {
        int count = this.mExitingRipplesCount;
        RippleForeground[] ripples = this.mExitingRipples;
        for (int i = 0; i < count; i++) {
            ripples[i].end();
        }
        if (ripples != null) {
            Arrays.fill(ripples, 0, count, (Object) null);
        }
        this.mExitingRipplesCount = 0;
        invalidateSelf(false);
    }

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public boolean onStateChange(int[] stateSet) {
        boolean changed = super.onStateChange(stateSet);
        boolean z = false;
        boolean hovered = false;
        boolean hovered2 = false;
        boolean focused = false;
        boolean enabled = false;
        for (int state : stateSet) {
            if (state == 16842910) {
                enabled = true;
            } else if (state == 16842908) {
                hovered2 = true;
            } else if (state == 16842919) {
                focused = true;
            } else if (state == 16843623) {
                hovered = true;
            }
        }
        if (enabled && focused) {
            z = true;
        }
        setRippleActive(z);
        setBackgroundActive(hovered, hovered2, focused);
        return changed;
    }

    private synchronized void setRippleActive(boolean active) {
        if (this.mRippleActive != active) {
            this.mRippleActive = active;
            if (active) {
                tryRippleEnter();
            } else {
                tryRippleExit();
            }
        }
    }

    private synchronized void setBackgroundActive(boolean hovered, boolean focused, boolean pressed) {
        if (this.mBackground == null && (hovered || focused)) {
            this.mBackground = new RippleBackground(this, this.mHotspotBounds, isBounded());
            this.mBackground.setup(this.mState.mMaxRadius, this.mDensity);
        }
        if (this.mBackground != null) {
            this.mBackground.setState(focused, hovered, pressed);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        if (!this.mOverrideBounds) {
            this.mHotspotBounds.set(bounds);
            onHotspotBoundsChanged();
        }
        int count = this.mExitingRipplesCount;
        RippleForeground[] ripples = this.mExitingRipples;
        for (int i = 0; i < count; i++) {
            ripples[i].onBoundsChange();
        }
        if (this.mBackground != null) {
            this.mBackground.onBoundsChange();
        }
        if (this.mRipple != null) {
            this.mRipple.onBoundsChange();
        }
        invalidateSelf();
    }

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public boolean setVisible(boolean visible, boolean restart) {
        boolean changed = super.setVisible(visible, restart);
        if (!visible) {
            clearHotspots();
        } else if (changed) {
            if (this.mRippleActive) {
                tryRippleEnter();
            }
            jumpToCurrentState();
        }
        return changed;
    }

    @Override // android.graphics.drawable.LayerDrawable
    public synchronized boolean isProjected() {
        if (isBounded()) {
            return false;
        }
        int radius = this.mState.mMaxRadius;
        Rect drawableBounds = getBounds();
        Rect hotspotBounds = this.mHotspotBounds;
        if (radius == -1 || radius > hotspotBounds.width() / 2 || radius > hotspotBounds.height() / 2) {
            return true;
        }
        return (drawableBounds.equals(hotspotBounds) || drawableBounds.contains(hotspotBounds)) ? false : true;
    }

    private synchronized boolean isBounded() {
        return getNumberOfLayers() > 0;
    }

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public boolean isStateful() {
        return true;
    }

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public boolean hasFocusStateSpecified() {
        return true;
    }

    public void setColor(ColorStateList color) {
        this.mState.mColor = color;
        invalidateSelf(false);
    }

    public void setRadius(int radius) {
        this.mState.mMaxRadius = radius;
        invalidateSelf(false);
    }

    public int getRadius() {
        return this.mState.mMaxRadius;
    }

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        TypedArray a = obtainAttributes(r, theme, attrs, R.styleable.RippleDrawable);
        setPaddingMode(1);
        super.inflate(r, parser, attrs, theme);
        updateStateFromTypedArray(a);
        verifyRequiredAttributes(a);
        a.recycle();
        updateLocalState();
    }

    @Override // android.graphics.drawable.LayerDrawable
    public boolean setDrawableByLayerId(int id, Drawable drawable) {
        if (super.setDrawableByLayerId(id, drawable)) {
            if (id == 16908334) {
                this.mMask = drawable;
                this.mHasValidMask = false;
                return true;
            }
            return true;
        }
        return false;
    }

    @Override // android.graphics.drawable.LayerDrawable
    public void setPaddingMode(int mode) {
        super.setPaddingMode(mode);
    }

    private synchronized void updateStateFromTypedArray(TypedArray a) throws XmlPullParserException {
        RippleState state = this.mState;
        state.mChangingConfigurations |= a.getChangingConfigurations();
        state.mTouchThemeAttrs = a.extractThemeAttrs();
        ColorStateList color = a.getColorStateList(0);
        if (color != null) {
            this.mState.mColor = color;
        }
        this.mState.mMaxRadius = a.getDimensionPixelSize(1, this.mState.mMaxRadius);
    }

    private synchronized void verifyRequiredAttributes(TypedArray a) throws XmlPullParserException {
        if (this.mState.mColor == null) {
            if (this.mState.mTouchThemeAttrs == null || this.mState.mTouchThemeAttrs[0] == 0) {
                throw new XmlPullParserException(a.getPositionDescription() + ": <ripple> requires a valid color attribute");
            }
        }
    }

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public void applyTheme(Resources.Theme t) {
        super.applyTheme(t);
        RippleState state = this.mState;
        if (state == null) {
            return;
        }
        if (state.mTouchThemeAttrs != null) {
            TypedArray a = t.resolveAttributes(state.mTouchThemeAttrs, R.styleable.RippleDrawable);
            try {
                try {
                    updateStateFromTypedArray(a);
                    verifyRequiredAttributes(a);
                } catch (XmlPullParserException e) {
                    rethrowAsRuntimeException(e);
                }
            } finally {
                a.recycle();
            }
        }
        if (state.mColor != null && state.mColor.canApplyTheme()) {
            state.mColor = state.mColor.mo19obtainForTheme(t);
        }
        updateLocalState();
    }

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public boolean canApplyTheme() {
        return (this.mState != null && this.mState.canApplyTheme()) || super.canApplyTheme();
    }

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public void setHotspot(float x, float y) {
        if (this.mRipple == null || this.mBackground == null) {
            this.mPendingX = x;
            this.mPendingY = y;
            this.mHasPending = true;
        }
        if (this.mRipple != null) {
            this.mRipple.move(x, y);
        }
    }

    private synchronized void tryRippleEnter() {
        float x;
        float exactCenterY;
        if (this.mExitingRipplesCount >= 10) {
            return;
        }
        if (this.mRipple == null) {
            if (this.mHasPending) {
                this.mHasPending = false;
                x = this.mPendingX;
                exactCenterY = this.mPendingY;
            } else {
                x = this.mHotspotBounds.exactCenterX();
                exactCenterY = this.mHotspotBounds.exactCenterY();
            }
            float y = exactCenterY;
            this.mRipple = new RippleForeground(this, this.mHotspotBounds, x, y, this.mForceSoftware);
        }
        this.mRipple.setup(this.mState.mMaxRadius, this.mDensity);
        this.mRipple.enter();
    }

    private synchronized void tryRippleExit() {
        if (this.mRipple != null) {
            if (this.mExitingRipples == null) {
                this.mExitingRipples = new RippleForeground[10];
            }
            RippleForeground[] rippleForegroundArr = this.mExitingRipples;
            int i = this.mExitingRipplesCount;
            this.mExitingRipplesCount = i + 1;
            rippleForegroundArr[i] = this.mRipple;
            this.mRipple.exit();
            this.mRipple = null;
        }
    }

    private synchronized void clearHotspots() {
        if (this.mRipple != null) {
            this.mRipple.end();
            this.mRipple = null;
            this.mRippleActive = false;
        }
        if (this.mBackground != null) {
            this.mBackground.setState(false, false, false);
        }
        cancelExitingRipples();
    }

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public void setHotspotBounds(int left, int top, int right, int bottom) {
        this.mOverrideBounds = true;
        this.mHotspotBounds.set(left, top, right, bottom);
        onHotspotBoundsChanged();
    }

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public void getHotspotBounds(Rect outRect) {
        outRect.set(this.mHotspotBounds);
    }

    private synchronized void onHotspotBoundsChanged() {
        int count = this.mExitingRipplesCount;
        RippleForeground[] ripples = this.mExitingRipples;
        for (int i = 0; i < count; i++) {
            ripples[i].onHotspotBoundsChanged();
        }
        if (this.mRipple != null) {
            this.mRipple.onHotspotBoundsChanged();
        }
        if (this.mBackground != null) {
            this.mBackground.onHotspotBoundsChanged();
        }
    }

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public void getOutline(Outline outline) {
        LayerDrawable.LayerState state = this.mLayerState;
        LayerDrawable.ChildDrawable[] children = state.mChildren;
        int N = state.mNumChildren;
        for (int i = 0; i < N; i++) {
            if (children[i].mId != 16908334) {
                children[i].mDrawable.getOutline(outline);
                if (!outline.isEmpty()) {
                    return;
                }
            }
        }
    }

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        pruneRipples();
        Rect bounds = getDirtyBounds();
        int saveCount = canvas.save(2);
        if (isBounded()) {
            canvas.clipRect(bounds);
        }
        drawContent(canvas);
        drawBackgroundAndRipples(canvas);
        canvas.restoreToCount(saveCount);
    }

    @Override // android.graphics.drawable.Drawable
    public void invalidateSelf() {
        invalidateSelf(true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void invalidateSelf(boolean invalidateMask) {
        super.invalidateSelf();
        if (invalidateMask) {
            this.mHasValidMask = false;
        }
    }

    private synchronized void pruneRipples() {
        int remaining = 0;
        RippleForeground[] ripples = this.mExitingRipples;
        int count = this.mExitingRipplesCount;
        for (int i = 0; i < count; i++) {
            if (!ripples[i].hasFinishedExit()) {
                ripples[remaining] = ripples[i];
                remaining++;
            }
        }
        for (int i2 = remaining; i2 < count; i2++) {
            ripples[i2] = null;
        }
        this.mExitingRipplesCount = remaining;
    }

    private synchronized void updateMaskShaderIfNeeded() {
        int maskType;
        if (this.mHasValidMask || (maskType = getMaskType()) == -1) {
            return;
        }
        this.mHasValidMask = true;
        Rect bounds = getBounds();
        if (maskType == 0 || bounds.isEmpty()) {
            if (this.mMaskBuffer != null) {
                this.mMaskBuffer.recycle();
                this.mMaskBuffer = null;
                this.mMaskShader = null;
                this.mMaskCanvas = null;
            }
            this.mMaskMatrix = null;
            this.mMaskColorFilter = null;
            return;
        }
        if (this.mMaskBuffer == null || this.mMaskBuffer.getWidth() != bounds.width() || this.mMaskBuffer.getHeight() != bounds.height()) {
            if (this.mMaskBuffer != null) {
                this.mMaskBuffer.recycle();
            }
            this.mMaskBuffer = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ALPHA_8);
            this.mMaskShader = new BitmapShader(this.mMaskBuffer, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            this.mMaskCanvas = new Canvas(this.mMaskBuffer);
        } else {
            this.mMaskBuffer.eraseColor(0);
        }
        if (this.mMaskMatrix == null) {
            this.mMaskMatrix = new Matrix();
        } else {
            this.mMaskMatrix.reset();
        }
        if (this.mMaskColorFilter == null) {
            this.mMaskColorFilter = new PorterDuffColorFilter(0, PorterDuff.Mode.SRC_IN);
        }
        int left = bounds.left;
        int top = bounds.top;
        this.mMaskCanvas.translate(-left, -top);
        if (maskType == 2) {
            drawMask(this.mMaskCanvas);
        } else if (maskType == 1) {
            drawContent(this.mMaskCanvas);
        }
        this.mMaskCanvas.translate(left, top);
    }

    private synchronized int getMaskType() {
        if (this.mRipple != null || this.mExitingRipplesCount > 0 || (this.mBackground != null && this.mBackground.isVisible())) {
            if (this.mMask != null) {
                return this.mMask.getOpacity() == -1 ? 0 : 2;
            }
            LayerDrawable.ChildDrawable[] array = this.mLayerState.mChildren;
            int count = this.mLayerState.mNumChildren;
            for (int i = 0; i < count; i++) {
                if (array[i].mDrawable.getOpacity() != -1) {
                    return 1;
                }
            }
            return 0;
        }
        return -1;
    }

    private synchronized void drawContent(Canvas canvas) {
        LayerDrawable.ChildDrawable[] array = this.mLayerState.mChildren;
        int count = this.mLayerState.mNumChildren;
        for (int i = 0; i < count; i++) {
            if (array[i].mId != 16908334) {
                array[i].mDrawable.draw(canvas);
            }
        }
    }

    private synchronized void drawBackgroundAndRipples(Canvas canvas) {
        RippleForeground active = this.mRipple;
        RippleBackground background = this.mBackground;
        int count = this.mExitingRipplesCount;
        if (active == null && count <= 0 && (background == null || !background.isVisible())) {
            return;
        }
        float x = this.mHotspotBounds.exactCenterX();
        float y = this.mHotspotBounds.exactCenterY();
        canvas.translate(x, y);
        Paint p = getRipplePaint();
        if (background != null && background.isVisible()) {
            background.draw(canvas, p);
        }
        if (count > 0) {
            RippleForeground[] ripples = this.mExitingRipples;
            for (int i = 0; i < count; i++) {
                ripples[i].draw(canvas, p);
            }
        }
        if (active != null) {
            active.draw(canvas, p);
        }
        canvas.translate(-x, -y);
    }

    private synchronized void drawMask(Canvas canvas) {
        this.mMask.draw(canvas);
    }

    public private protected Paint getRipplePaint() {
        if (this.mRipplePaint == null) {
            this.mRipplePaint = new Paint();
            this.mRipplePaint.setAntiAlias(true);
            this.mRipplePaint.setStyle(Paint.Style.FILL);
        }
        float x = this.mHotspotBounds.exactCenterX();
        float y = this.mHotspotBounds.exactCenterY();
        updateMaskShaderIfNeeded();
        if (this.mMaskShader != null) {
            Rect bounds = getBounds();
            this.mMaskMatrix.setTranslate(bounds.left - x, bounds.top - y);
            this.mMaskShader.setLocalMatrix(this.mMaskMatrix);
        }
        int color = this.mState.mColor.getColorForState(getState(), -16777216);
        if (Color.alpha(color) > 128) {
            color = (16777215 & color) | Integer.MIN_VALUE;
        }
        Paint p = this.mRipplePaint;
        if (this.mMaskColorFilter != null) {
            this.mMaskColorFilter.setColor(color | (-16777216));
            p.setColor((-16777216) & color);
            p.setColorFilter(this.mMaskColorFilter);
            p.setShader(this.mMaskShader);
        } else {
            p.setColor(color);
            p.setColorFilter(null);
            p.setShader(null);
        }
        return p;
    }

    @Override // android.graphics.drawable.Drawable
    public Rect getDirtyBounds() {
        if (!isBounded()) {
            Rect drawingBounds = this.mDrawingBounds;
            Rect dirtyBounds = this.mDirtyBounds;
            dirtyBounds.set(drawingBounds);
            drawingBounds.setEmpty();
            int cX = (int) this.mHotspotBounds.exactCenterX();
            int cY = (int) this.mHotspotBounds.exactCenterY();
            Rect rippleBounds = this.mTempRect;
            RippleForeground[] activeRipples = this.mExitingRipples;
            int N = this.mExitingRipplesCount;
            for (int i = 0; i < N; i++) {
                activeRipples[i].getBounds(rippleBounds);
                rippleBounds.offset(cX, cY);
                drawingBounds.union(rippleBounds);
            }
            RippleBackground background = this.mBackground;
            if (background != null) {
                background.getBounds(rippleBounds);
                rippleBounds.offset(cX, cY);
                drawingBounds.union(rippleBounds);
            }
            dirtyBounds.union(drawingBounds);
            dirtyBounds.union(super.getDirtyBounds());
            return dirtyBounds;
        }
        return getBounds();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setForceSoftware(boolean forceSoftware) {
        this.mForceSoftware = forceSoftware;
    }

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        return this.mState;
    }

    @Override // android.graphics.drawable.LayerDrawable, android.graphics.drawable.Drawable
    public Drawable mutate() {
        super.mutate();
        this.mState = (RippleState) this.mLayerState;
        this.mMask = findDrawableByLayerId(16908334);
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.graphics.drawable.LayerDrawable
    public synchronized RippleState createConstantState(LayerDrawable.LayerState state, Resources res) {
        return new RippleState(state, this, res);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class RippleState extends LayerDrawable.LayerState {
        public private protected ColorStateList mColor;
        int mMaxRadius;
        int[] mTouchThemeAttrs;

        public synchronized RippleState(LayerDrawable.LayerState orig, RippleDrawable owner, Resources res) {
            super(orig, owner, res);
            this.mColor = ColorStateList.valueOf(Color.MAGENTA);
            this.mMaxRadius = -1;
            if (orig != null && (orig instanceof RippleState)) {
                RippleState origs = (RippleState) orig;
                this.mTouchThemeAttrs = origs.mTouchThemeAttrs;
                this.mColor = origs.mColor;
                this.mMaxRadius = origs.mMaxRadius;
                if (origs.mDensity != this.mDensity) {
                    applyDensityScaling(orig.mDensity, this.mDensity);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.graphics.drawable.LayerDrawable.LayerState
        public synchronized void onDensityChanged(int sourceDensity, int targetDensity) {
            super.onDensityChanged(sourceDensity, targetDensity);
            applyDensityScaling(sourceDensity, targetDensity);
        }

        private synchronized void applyDensityScaling(int sourceDensity, int targetDensity) {
            if (this.mMaxRadius != -1) {
                this.mMaxRadius = Drawable.scaleFromDensity(this.mMaxRadius, sourceDensity, targetDensity, true);
            }
        }

        @Override // android.graphics.drawable.LayerDrawable.LayerState, android.graphics.drawable.Drawable.ConstantState
        public boolean canApplyTheme() {
            return this.mTouchThemeAttrs != null || (this.mColor != null && this.mColor.canApplyTheme()) || super.canApplyTheme();
        }

        @Override // android.graphics.drawable.LayerDrawable.LayerState, android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            return new RippleDrawable(this, (Resources) null);
        }

        @Override // android.graphics.drawable.LayerDrawable.LayerState, android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources res) {
            return new RippleDrawable(this, res);
        }

        @Override // android.graphics.drawable.LayerDrawable.LayerState, android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return super.getChangingConfigurations() | (this.mColor != null ? this.mColor.getChangingConfigurations() : 0);
        }
    }

    private synchronized RippleDrawable(RippleState state, Resources res) {
        this.mTempRect = new Rect();
        this.mHotspotBounds = new Rect();
        this.mDrawingBounds = new Rect();
        this.mDirtyBounds = new Rect();
        this.mExitingRipplesCount = 0;
        this.mState = new RippleState(state, this, res);
        this.mLayerState = this.mState;
        this.mDensity = Drawable.resolveDensity(res, this.mState.mDensity);
        if (this.mState.mNumChildren > 0) {
            ensurePadding();
            refreshPadding();
        }
        updateLocalState();
    }

    private synchronized void updateLocalState() {
        this.mMask = findDrawableByLayerId(16908334);
    }
}
