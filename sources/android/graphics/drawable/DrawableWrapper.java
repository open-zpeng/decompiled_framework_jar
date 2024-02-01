package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Insets;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import com.android.internal.R;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public abstract class DrawableWrapper extends Drawable implements Drawable.Callback {
    private Drawable mDrawable;
    private boolean mMutated;
    public protected DrawableWrapperState mState;

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized DrawableWrapper(DrawableWrapperState state, Resources res) {
        this.mState = state;
        updateLocalState(res);
    }

    public DrawableWrapper(Drawable dr) {
        this.mState = null;
        this.mDrawable = dr;
    }

    private synchronized void updateLocalState(Resources res) {
        if (this.mState != null && this.mState.mDrawableState != null) {
            Drawable dr = this.mState.mDrawableState.newDrawable(res);
            setDrawable(dr);
        }
    }

    public void setDrawable(Drawable dr) {
        if (this.mDrawable != null) {
            this.mDrawable.setCallback(null);
        }
        this.mDrawable = dr;
        if (dr != null) {
            dr.setCallback(this);
            dr.setVisible(isVisible(), true);
            dr.setState(getState());
            dr.setLevel(getLevel());
            dr.setBounds(getBounds());
            dr.setLayoutDirection(getLayoutDirection());
            if (this.mState != null) {
                this.mState.mDrawableState = dr.getConstantState();
            }
        }
        invalidateSelf();
    }

    public Drawable getDrawable() {
        return this.mDrawable;
    }

    @Override // android.graphics.drawable.Drawable
    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        super.inflate(r, parser, attrs, theme);
        DrawableWrapperState state = this.mState;
        if (state == null) {
            return;
        }
        int densityDpi = r.getDisplayMetrics().densityDpi;
        int targetDensity = densityDpi == 0 ? 160 : densityDpi;
        state.setDensity(targetDensity);
        state.mSrcDensityOverride = this.mSrcDensityOverride;
        TypedArray a = obtainAttributes(r, theme, attrs, R.styleable.DrawableWrapper);
        updateStateFromTypedArray(a);
        a.recycle();
        inflateChildDrawable(r, parser, attrs, theme);
    }

    @Override // android.graphics.drawable.Drawable
    public void applyTheme(Resources.Theme t) {
        super.applyTheme(t);
        if (this.mDrawable != null && this.mDrawable.canApplyTheme()) {
            this.mDrawable.applyTheme(t);
        }
        DrawableWrapperState state = this.mState;
        if (state == null) {
            return;
        }
        int densityDpi = t.getResources().getDisplayMetrics().densityDpi;
        int density = densityDpi == 0 ? 160 : densityDpi;
        state.setDensity(density);
        if (state.mThemeAttrs != null) {
            TypedArray a = t.resolveAttributes(state.mThemeAttrs, R.styleable.DrawableWrapper);
            updateStateFromTypedArray(a);
            a.recycle();
        }
    }

    private synchronized void updateStateFromTypedArray(TypedArray a) {
        DrawableWrapperState state = this.mState;
        if (state == null) {
            return;
        }
        state.mChangingConfigurations |= a.getChangingConfigurations();
        state.mThemeAttrs = a.extractThemeAttrs();
        if (a.hasValueOrEmpty(0)) {
            setDrawable(a.getDrawable(0));
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean canApplyTheme() {
        return (this.mState != null && this.mState.canApplyTheme()) || super.canApplyTheme();
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable who) {
        Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, what, when);
        }
    }

    @Override // android.graphics.drawable.Drawable.Callback
    public void unscheduleDrawable(Drawable who, Runnable what) {
        Drawable.Callback callback = getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, what);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        if (this.mDrawable != null) {
            this.mDrawable.draw(canvas);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | (this.mState != null ? this.mState.getChangingConfigurations() : 0) | this.mDrawable.getChangingConfigurations();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean getPadding(Rect padding) {
        return this.mDrawable != null && this.mDrawable.getPadding(padding);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Insets getOpticalInsets() {
        return this.mDrawable != null ? this.mDrawable.getOpticalInsets() : Insets.NONE;
    }

    @Override // android.graphics.drawable.Drawable
    public void setHotspot(float x, float y) {
        if (this.mDrawable != null) {
            this.mDrawable.setHotspot(x, y);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setHotspotBounds(int left, int top, int right, int bottom) {
        if (this.mDrawable != null) {
            this.mDrawable.setHotspotBounds(left, top, right, bottom);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void getHotspotBounds(Rect outRect) {
        if (this.mDrawable != null) {
            this.mDrawable.getHotspotBounds(outRect);
        } else {
            outRect.set(getBounds());
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean setVisible(boolean visible, boolean restart) {
        boolean superChanged = super.setVisible(visible, restart);
        boolean changed = this.mDrawable != null && this.mDrawable.setVisible(visible, restart);
        return superChanged | changed;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        if (this.mDrawable != null) {
            this.mDrawable.setAlpha(alpha);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        if (this.mDrawable != null) {
            return this.mDrawable.getAlpha();
        }
        return 255;
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        if (this.mDrawable != null) {
            this.mDrawable.setColorFilter(colorFilter);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public ColorFilter getColorFilter() {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            return drawable.getColorFilter();
        }
        return super.getColorFilter();
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintList(ColorStateList tint) {
        if (this.mDrawable != null) {
            this.mDrawable.setTintList(tint);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintMode(PorterDuff.Mode tintMode) {
        if (this.mDrawable != null) {
            this.mDrawable.setTintMode(tintMode);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean onLayoutDirectionChanged(int layoutDirection) {
        return this.mDrawable != null && this.mDrawable.setLayoutDirection(layoutDirection);
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        if (this.mDrawable != null) {
            return this.mDrawable.getOpacity();
        }
        return -2;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        return this.mDrawable != null && this.mDrawable.isStateful();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean hasFocusStateSpecified() {
        return this.mDrawable != null && this.mDrawable.hasFocusStateSpecified();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.drawable.Drawable
    public boolean onStateChange(int[] state) {
        if (this.mDrawable != null && this.mDrawable.isStateful()) {
            boolean changed = this.mDrawable.setState(state);
            if (changed) {
                onBoundsChange(getBounds());
            }
            return changed;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.drawable.Drawable
    public boolean onLevelChange(int level) {
        return this.mDrawable != null && this.mDrawable.setLevel(level);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.drawable.Drawable
    public void onBoundsChange(Rect bounds) {
        if (this.mDrawable != null) {
            this.mDrawable.setBounds(bounds);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        if (this.mDrawable != null) {
            return this.mDrawable.getIntrinsicWidth();
        }
        return -1;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        if (this.mDrawable != null) {
            return this.mDrawable.getIntrinsicHeight();
        }
        return -1;
    }

    @Override // android.graphics.drawable.Drawable
    public void getOutline(Outline outline) {
        if (this.mDrawable != null) {
            this.mDrawable.getOutline(outline);
        } else {
            super.getOutline(outline);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        if (this.mState != null && this.mState.canConstantState()) {
            this.mState.mChangingConfigurations = getChangingConfigurations();
            return this.mState;
        }
        return null;
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mState = mutateConstantState();
            if (this.mDrawable != null) {
                this.mDrawable.mutate();
            }
            if (this.mState != null) {
                this.mState.mDrawableState = this.mDrawable != null ? this.mDrawable.getConstantState() : null;
            }
            this.mMutated = true;
        }
        return this;
    }

    synchronized DrawableWrapperState mutateConstantState() {
        return this.mState;
    }

    @Override // android.graphics.drawable.Drawable
    public synchronized void clearMutated() {
        super.clearMutated();
        if (this.mDrawable != null) {
            this.mDrawable.clearMutated();
        }
        this.mMutated = false;
    }

    private synchronized void inflateChildDrawable(Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        Drawable dr = null;
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                break;
            } else if (type == 2) {
                dr = Drawable.createFromXmlInnerForDensity(r, parser, attrs, this.mState.mSrcDensityOverride, theme);
            }
        }
        if (dr != null) {
            setDrawable(dr);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static abstract class DrawableWrapperState extends Drawable.ConstantState {
        int mChangingConfigurations;
        int mDensity;
        Drawable.ConstantState mDrawableState;
        int mSrcDensityOverride;
        private int[] mThemeAttrs;

        @Override // android.graphics.drawable.Drawable.ConstantState
        public abstract Drawable newDrawable(Resources resources);

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized DrawableWrapperState(DrawableWrapperState orig, Resources res) {
            this.mDensity = 160;
            int density = 0;
            this.mSrcDensityOverride = 0;
            if (orig != null) {
                this.mThemeAttrs = orig.mThemeAttrs;
                this.mChangingConfigurations = orig.mChangingConfigurations;
                this.mDrawableState = orig.mDrawableState;
                this.mSrcDensityOverride = orig.mSrcDensityOverride;
            }
            if (res != null) {
                density = res.getDisplayMetrics().densityDpi;
            } else if (orig != null) {
                density = orig.mDensity;
            }
            this.mDensity = density != 0 ? density : 160;
        }

        public final synchronized void setDensity(int targetDensity) {
            if (this.mDensity != targetDensity) {
                int sourceDensity = this.mDensity;
                this.mDensity = targetDensity;
                onDensityChanged(sourceDensity, targetDensity);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized void onDensityChanged(int sourceDensity, int targetDensity) {
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public boolean canApplyTheme() {
            return this.mThemeAttrs != null || (this.mDrawableState != null && this.mDrawableState.canApplyTheme()) || super.canApplyTheme();
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            return newDrawable(null);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return this.mChangingConfigurations | (this.mDrawableState != null ? this.mDrawableState.getChangingConfigurations() : 0);
        }

        public synchronized boolean canConstantState() {
            return this.mDrawableState != null;
        }
    }
}
