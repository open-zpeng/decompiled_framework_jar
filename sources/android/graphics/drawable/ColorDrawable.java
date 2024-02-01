package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewDebug;
import com.android.internal.R;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class ColorDrawable extends Drawable {
    @ViewDebug.ExportedProperty(deepExport = true, prefix = "state_")
    private ColorState mColorState;
    private boolean mMutated;
    public protected final Paint mPaint;
    private PorterDuffColorFilter mTintFilter;

    public ColorDrawable() {
        this.mPaint = new Paint(1);
        this.mColorState = new ColorState();
    }

    public ColorDrawable(int color) {
        this.mPaint = new Paint(1);
        this.mColorState = new ColorState();
        setColor(color);
    }

    @Override // android.graphics.drawable.Drawable
    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mColorState.getChangingConfigurations();
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mColorState = new ColorState(this.mColorState);
            this.mMutated = true;
        }
        return this;
    }

    @Override // android.graphics.drawable.Drawable
    public synchronized void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        ColorFilter colorFilter = this.mPaint.getColorFilter();
        if ((this.mColorState.mUseColor >>> 24) != 0 || colorFilter != null || this.mTintFilter != null) {
            if (colorFilter == null) {
                this.mPaint.setColorFilter(this.mTintFilter);
            }
            this.mPaint.setColor(this.mColorState.mUseColor);
            canvas.drawRect(getBounds(), this.mPaint);
            this.mPaint.setColorFilter(colorFilter);
        }
    }

    public int getColor() {
        return this.mColorState.mUseColor;
    }

    public void setColor(int color) {
        if (this.mColorState.mBaseColor != color || this.mColorState.mUseColor != color) {
            ColorState colorState = this.mColorState;
            this.mColorState.mUseColor = color;
            colorState.mBaseColor = color;
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.mColorState.mUseColor >>> 24;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        int baseAlpha = this.mColorState.mBaseColor >>> 24;
        int useAlpha = (baseAlpha * (alpha + (alpha >> 7))) >> 8;
        int useColor = ((this.mColorState.mBaseColor << 8) >>> 8) | (useAlpha << 24);
        if (this.mColorState.mUseColor != useColor) {
            this.mColorState.mUseColor = useColor;
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintList(ColorStateList tint) {
        this.mColorState.mTint = tint;
        this.mTintFilter = updateTintFilter(this.mTintFilter, tint, this.mColorState.mTintMode);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintMode(PorterDuff.Mode tintMode) {
        this.mColorState.mTintMode = tintMode;
        this.mTintFilter = updateTintFilter(this.mTintFilter, this.mColorState.mTint, tintMode);
        invalidateSelf();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.drawable.Drawable
    public boolean onStateChange(int[] stateSet) {
        ColorState state = this.mColorState;
        if (state.mTint != null && state.mTintMode != null) {
            this.mTintFilter = updateTintFilter(this.mTintFilter, state.mTint, state.mTintMode);
            return true;
        }
        return false;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        return this.mColorState.mTint != null && this.mColorState.mTint.isStateful();
    }

    @Override // android.graphics.drawable.Drawable
    public boolean hasFocusStateSpecified() {
        return this.mColorState.mTint != null && this.mColorState.mTint.hasFocusStateSpecified();
    }

    @Override // android.graphics.drawable.Drawable
    public synchronized void setXfermode(Xfermode mode) {
        this.mPaint.setXfermode(mode);
        invalidateSelf();
    }

    public Xfermode getXfermode() {
        return this.mPaint.getXfermode();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        if (this.mTintFilter == null && this.mPaint.getColorFilter() == null) {
            int i = this.mColorState.mUseColor >>> 24;
            if (i != 0) {
                return i != 255 ? -3 : -1;
            }
            return -2;
        }
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public void getOutline(Outline outline) {
        outline.setRect(getBounds());
        outline.setAlpha(getAlpha() / 255.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        super.inflate(r, parser, attrs, theme);
        TypedArray a = obtainAttributes(r, theme, attrs, R.styleable.ColorDrawable);
        updateStateFromTypedArray(a);
        a.recycle();
        updateLocalState(r);
    }

    private synchronized void updateStateFromTypedArray(TypedArray a) {
        ColorState state = this.mColorState;
        state.mChangingConfigurations |= a.getChangingConfigurations();
        state.mThemeAttrs = a.extractThemeAttrs();
        state.mBaseColor = a.getColor(0, state.mBaseColor);
        state.mUseColor = state.mBaseColor;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean canApplyTheme() {
        return this.mColorState.canApplyTheme() || super.canApplyTheme();
    }

    @Override // android.graphics.drawable.Drawable
    public void applyTheme(Resources.Theme t) {
        super.applyTheme(t);
        ColorState state = this.mColorState;
        if (state == null) {
            return;
        }
        if (state.mThemeAttrs != null) {
            TypedArray a = t.resolveAttributes(state.mThemeAttrs, R.styleable.ColorDrawable);
            updateStateFromTypedArray(a);
            a.recycle();
        }
        if (state.mTint != null && state.mTint.canApplyTheme()) {
            state.mTint = state.mTint.mo19obtainForTheme(t);
        }
        updateLocalState(t.getResources());
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        return this.mColorState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class ColorState extends Drawable.ConstantState {
        int mBaseColor;
        int mChangingConfigurations;
        int[] mThemeAttrs;
        ColorStateList mTint;
        PorterDuff.Mode mTintMode;
        @ViewDebug.ExportedProperty
        public private protected int mUseColor;

        synchronized ColorState() {
            this.mTint = null;
            this.mTintMode = Drawable.DEFAULT_TINT_MODE;
        }

        synchronized ColorState(ColorState state) {
            this.mTint = null;
            this.mTintMode = Drawable.DEFAULT_TINT_MODE;
            this.mThemeAttrs = state.mThemeAttrs;
            this.mBaseColor = state.mBaseColor;
            this.mUseColor = state.mUseColor;
            this.mChangingConfigurations = state.mChangingConfigurations;
            this.mTint = state.mTint;
            this.mTintMode = state.mTintMode;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public boolean canApplyTheme() {
            return this.mThemeAttrs != null || (this.mTint != null && this.mTint.canApplyTheme());
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            return new ColorDrawable(this, null);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources res) {
            return new ColorDrawable(this, res);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return this.mChangingConfigurations | (this.mTint != null ? this.mTint.getChangingConfigurations() : 0);
        }
    }

    private synchronized ColorDrawable(ColorState state, Resources res) {
        this.mPaint = new Paint(1);
        this.mColorState = state;
        updateLocalState(res);
    }

    private synchronized void updateLocalState(Resources r) {
        this.mTintFilter = updateTintFilter(this.mTintFilter, this.mColorState.mTint, this.mColorState.mTintMode);
    }
}
