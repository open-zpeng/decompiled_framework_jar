package android.graphics.drawable;

import android.graphics.Rect;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class RippleComponent {
    protected final Rect mBounds;
    protected float mDensityScale;
    private boolean mHasMaxRadius;
    protected final RippleDrawable mOwner;
    protected float mTargetRadius;

    public synchronized RippleComponent(RippleDrawable owner, Rect bounds) {
        this.mOwner = owner;
        this.mBounds = bounds;
    }

    public synchronized void onBoundsChange() {
        if (!this.mHasMaxRadius) {
            this.mTargetRadius = getTargetRadius(this.mBounds);
            onTargetRadiusChanged(this.mTargetRadius);
        }
    }

    public final synchronized void setup(float maxRadius, int densityDpi) {
        if (maxRadius >= 0.0f) {
            this.mHasMaxRadius = true;
            this.mTargetRadius = maxRadius;
        } else {
            this.mTargetRadius = getTargetRadius(this.mBounds);
        }
        this.mDensityScale = densityDpi * 0.00625f;
        onTargetRadiusChanged(this.mTargetRadius);
    }

    private static synchronized float getTargetRadius(Rect bounds) {
        float halfWidth = bounds.width() / 2.0f;
        float halfHeight = bounds.height() / 2.0f;
        return (float) Math.sqrt((halfWidth * halfWidth) + (halfHeight * halfHeight));
    }

    public synchronized void getBounds(Rect bounds) {
        int r = (int) Math.ceil(this.mTargetRadius);
        bounds.set(-r, -r, r, r);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final synchronized void invalidateSelf() {
        this.mOwner.invalidateSelf(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final synchronized void onHotspotBoundsChanged() {
        if (!this.mHasMaxRadius) {
            this.mTargetRadius = getTargetRadius(this.mBounds);
            onTargetRadiusChanged(this.mTargetRadius);
        }
    }

    protected synchronized void onTargetRadiusChanged(float targetRadius) {
    }
}
