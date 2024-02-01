package android.view;

import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.util.PathParser;
import android.util.proto.ProtoOutputStream;
import com.android.internal.R;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public final class DisplayCutout {
    private static final String BOTTOM_MARKER = "@bottom";
    private static final String DP_MARKER = "@dp";
    public static final String EMULATION_OVERLAY_CATEGORY = "com.android.internal.display_cutout_emulation";
    private static final String RIGHT_MARKER = "@right";
    private static final String TAG = "DisplayCutout";
    @GuardedBy("CACHE_LOCK")
    private static float sCachedDensity;
    @GuardedBy("CACHE_LOCK")
    private static int sCachedDisplayHeight;
    @GuardedBy("CACHE_LOCK")
    private static int sCachedDisplayWidth;
    @GuardedBy("CACHE_LOCK")
    private static String sCachedSpec;
    private final Region mBounds;
    private final Rect mSafeInsets;
    private static final Rect ZERO_RECT = new Rect();
    private static final Region EMPTY_REGION = new Region();
    public static final DisplayCutout NO_CUTOUT = new DisplayCutout(ZERO_RECT, EMPTY_REGION, false);
    private static final Pair<Path, DisplayCutout> NULL_PAIR = new Pair<>(null, null);
    private static final Object CACHE_LOCK = new Object();
    @GuardedBy("CACHE_LOCK")
    private static Pair<Path, DisplayCutout> sCachedCutout = NULL_PAIR;

    public DisplayCutout(Rect safeInsets, List<Rect> boundingRects) {
        this(safeInsets != null ? new Rect(safeInsets) : ZERO_RECT, boundingRectsToRegion(boundingRects), true);
    }

    private synchronized DisplayCutout(Rect safeInsets, Region bounds, boolean copyArguments) {
        Rect rect;
        Region obtain;
        if (safeInsets == null) {
            rect = ZERO_RECT;
        } else {
            rect = copyArguments ? new Rect(safeInsets) : safeInsets;
        }
        this.mSafeInsets = rect;
        if (bounds == null) {
            obtain = Region.obtain();
        } else {
            obtain = copyArguments ? Region.obtain(bounds) : bounds;
        }
        this.mBounds = obtain;
    }

    public synchronized boolean isEmpty() {
        return this.mSafeInsets.equals(ZERO_RECT);
    }

    public synchronized boolean isBoundsEmpty() {
        return this.mBounds.isEmpty();
    }

    public int getSafeInsetTop() {
        return this.mSafeInsets.top;
    }

    public int getSafeInsetBottom() {
        return this.mSafeInsets.bottom;
    }

    public int getSafeInsetLeft() {
        return this.mSafeInsets.left;
    }

    public int getSafeInsetRight() {
        return this.mSafeInsets.right;
    }

    public synchronized Rect getSafeInsets() {
        return new Rect(this.mSafeInsets);
    }

    public synchronized Region getBounds() {
        return Region.obtain(this.mBounds);
    }

    public List<Rect> getBoundingRects() {
        List<Rect> result = new ArrayList<>();
        Region bounds = Region.obtain();
        bounds.set(this.mBounds);
        bounds.op(0, 0, Integer.MAX_VALUE, getSafeInsetTop(), Region.Op.INTERSECT);
        if (!bounds.isEmpty()) {
            result.add(bounds.getBounds());
        }
        bounds.set(this.mBounds);
        bounds.op(0, 0, getSafeInsetLeft(), Integer.MAX_VALUE, Region.Op.INTERSECT);
        if (!bounds.isEmpty()) {
            result.add(bounds.getBounds());
        }
        bounds.set(this.mBounds);
        bounds.op(getSafeInsetLeft() + 1, getSafeInsetTop() + 1, Integer.MAX_VALUE, Integer.MAX_VALUE, Region.Op.INTERSECT);
        if (!bounds.isEmpty()) {
            result.add(bounds.getBounds());
        }
        bounds.recycle();
        return result;
    }

    public int hashCode() {
        int result = this.mSafeInsets.hashCode();
        return (result * 31) + this.mBounds.getBounds().hashCode();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof DisplayCutout) {
            DisplayCutout c = (DisplayCutout) o;
            return this.mSafeInsets.equals(c.mSafeInsets) && this.mBounds.equals(c.mBounds);
        }
        return false;
    }

    public String toString() {
        return "DisplayCutout{insets=" + this.mSafeInsets + " boundingRect=" + this.mBounds.getBounds() + "}";
    }

    public synchronized void writeToProto(ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        this.mSafeInsets.writeToProto(proto, 1146756268033L);
        this.mBounds.getBounds().writeToProto(proto, 1146756268034L);
        proto.end(token);
    }

    public synchronized DisplayCutout inset(int insetLeft, int insetTop, int insetRight, int insetBottom) {
        if (this.mBounds.isEmpty() || (insetLeft == 0 && insetTop == 0 && insetRight == 0 && insetBottom == 0)) {
            return this;
        }
        Rect safeInsets = new Rect(this.mSafeInsets);
        Region bounds = Region.obtain(this.mBounds);
        if (insetTop > 0 || safeInsets.top > 0) {
            safeInsets.top = atLeastZero(safeInsets.top - insetTop);
        }
        if (insetBottom > 0 || safeInsets.bottom > 0) {
            safeInsets.bottom = atLeastZero(safeInsets.bottom - insetBottom);
        }
        if (insetLeft > 0 || safeInsets.left > 0) {
            safeInsets.left = atLeastZero(safeInsets.left - insetLeft);
        }
        if (insetRight > 0 || safeInsets.right > 0) {
            safeInsets.right = atLeastZero(safeInsets.right - insetRight);
        }
        bounds.translate(-insetLeft, -insetTop);
        return new DisplayCutout(safeInsets, bounds, false);
    }

    public synchronized DisplayCutout replaceSafeInsets(Rect safeInsets) {
        return new DisplayCutout(new Rect(safeInsets), this.mBounds, false);
    }

    private static synchronized int atLeastZero(int value) {
        if (value < 0) {
            return 0;
        }
        return value;
    }

    @VisibleForTesting
    public static synchronized DisplayCutout fromBoundingRect(int left, int top, int right, int bottom) {
        Region r = Region.obtain();
        r.set(left, top, right, bottom);
        return fromBounds(r);
    }

    public static DisplayCutout fromBounds(Region region) {
        return new DisplayCutout(ZERO_RECT, region, false);
    }

    public static DisplayCutout fromResourcesRectApproximation(Resources res, int displayWidth, int displayHeight) {
        return fromSpec(res.getString(R.string.config_mainBuiltInDisplayCutoutRectApproximation), displayWidth, displayHeight, DisplayMetrics.DENSITY_DEVICE_STABLE / 160.0f);
    }

    public static synchronized Path pathFromResources(Resources res, int displayWidth, int displayHeight) {
        return pathAndDisplayCutoutFromSpec(res.getString(R.string.config_mainBuiltInDisplayCutout), displayWidth, displayHeight, DisplayMetrics.DENSITY_DEVICE_STABLE / 160.0f).first;
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public static synchronized DisplayCutout fromSpec(String spec, int displayWidth, int displayHeight, float density) {
        return pathAndDisplayCutoutFromSpec(spec, displayWidth, displayHeight, density).second;
    }

    private static synchronized Pair<Path, DisplayCutout> pathAndDisplayCutoutFromSpec(String spec, int displayWidth, int displayHeight, float density) {
        float offsetX;
        int bottomInset;
        if (TextUtils.isEmpty(spec)) {
            return NULL_PAIR;
        }
        synchronized (CACHE_LOCK) {
            try {
                try {
                    if (spec.equals(sCachedSpec) && sCachedDisplayWidth == displayWidth && sCachedDisplayHeight == displayHeight && sCachedDensity == density) {
                        return sCachedCutout;
                    }
                    String spec2 = spec.trim();
                    if (spec2.endsWith(RIGHT_MARKER)) {
                        offsetX = displayWidth;
                        spec2 = spec2.substring(0, spec2.length() - RIGHT_MARKER.length()).trim();
                    } else {
                        float offsetX2 = displayWidth;
                        offsetX = offsetX2 / 2.0f;
                    }
                    boolean inDp = spec2.endsWith(DP_MARKER);
                    if (inDp) {
                        spec2 = spec2.substring(0, spec2.length() - DP_MARKER.length());
                    }
                    String bottomSpec = null;
                    if (spec2.contains(BOTTOM_MARKER)) {
                        String[] splits = spec2.split(BOTTOM_MARKER, 2);
                        spec2 = splits[0].trim();
                        bottomSpec = splits[1].trim();
                    }
                    String bottomSpec2 = bottomSpec;
                    String spec3 = spec2;
                    Region r = Region.obtain();
                    try {
                        Path p = PathParser.createPathFromPathData(spec3);
                        Matrix m = new Matrix();
                        if (inDp) {
                            m.postScale(density, density);
                        }
                        m.postTranslate(offsetX, 0.0f);
                        p.transform(m);
                        Rect tmpRect = new Rect();
                        toRectAndAddToRegion(p, r, tmpRect);
                        int topInset = tmpRect.bottom;
                        if (bottomSpec2 != null) {
                            try {
                                Path bottomPath = PathParser.createPathFromPathData(bottomSpec2);
                                m.postTranslate(0.0f, displayHeight);
                                bottomPath.transform(m);
                                p.addPath(bottomPath);
                                toRectAndAddToRegion(bottomPath, r, tmpRect);
                                bottomInset = displayHeight - tmpRect.top;
                            } catch (Throwable e) {
                                Log.wtf(TAG, "Could not inflate bottom cutout: ", e);
                                return NULL_PAIR;
                            }
                        } else {
                            bottomInset = 0;
                        }
                        tmpRect.set(0, topInset, 0, bottomInset);
                        DisplayCutout cutout = new DisplayCutout(tmpRect, r, false);
                        Pair<Path, DisplayCutout> result = new Pair<>(p, cutout);
                        synchronized (CACHE_LOCK) {
                            sCachedSpec = spec3;
                            sCachedDisplayWidth = displayWidth;
                            sCachedDisplayHeight = displayHeight;
                            sCachedDensity = density;
                            sCachedCutout = result;
                        }
                        return result;
                    } catch (Throwable e2) {
                        Log.wtf(TAG, "Could not inflate cutout: ", e2);
                        return NULL_PAIR;
                    }
                } catch (Throwable th) {
                    e = th;
                    throw e;
                }
            } catch (Throwable th2) {
                e = th2;
                throw e;
            }
        }
    }

    private static void toRectAndAddToRegion(Path p, Region inoutRegion, Rect inoutRect) {
        RectF rectF = new RectF();
        p.computeBounds(rectF, false);
        rectF.round(inoutRect);
        inoutRegion.op(inoutRect, Region.Op.UNION);
    }

    private static synchronized Region boundingRectsToRegion(List<Rect> rects) {
        Region result = Region.obtain();
        if (rects != null) {
            for (Rect r : rects) {
                result.op(r, Region.Op.UNION);
            }
        }
        return result;
    }

    /* loaded from: classes2.dex */
    public static final class ParcelableWrapper implements Parcelable {
        public static final Parcelable.Creator<ParcelableWrapper> CREATOR = new Parcelable.Creator<ParcelableWrapper>() { // from class: android.view.DisplayCutout.ParcelableWrapper.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ParcelableWrapper createFromParcel(Parcel in) {
                return new ParcelableWrapper(ParcelableWrapper.readCutoutFromParcel(in));
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public ParcelableWrapper[] newArray(int size) {
                return new ParcelableWrapper[size];
            }
        };
        private DisplayCutout mInner;

        public synchronized ParcelableWrapper() {
            this(DisplayCutout.NO_CUTOUT);
        }

        public synchronized ParcelableWrapper(DisplayCutout cutout) {
            this.mInner = cutout;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            writeCutoutToParcel(this.mInner, out, flags);
        }

        public static synchronized void writeCutoutToParcel(DisplayCutout cutout, Parcel out, int flags) {
            if (cutout == null) {
                out.writeInt(-1);
            } else if (cutout == DisplayCutout.NO_CUTOUT) {
                out.writeInt(0);
            } else {
                out.writeInt(1);
                out.writeTypedObject(cutout.mSafeInsets, flags);
                out.writeTypedObject(cutout.mBounds, flags);
            }
        }

        public synchronized void readFromParcel(Parcel in) {
            this.mInner = readCutoutFromParcel(in);
        }

        public static synchronized DisplayCutout readCutoutFromParcel(Parcel in) {
            int variant = in.readInt();
            if (variant == -1) {
                return null;
            }
            if (variant == 0) {
                return DisplayCutout.NO_CUTOUT;
            }
            Rect safeInsets = (Rect) in.readTypedObject(Rect.CREATOR);
            Region bounds = (Region) in.readTypedObject(Region.CREATOR);
            return new DisplayCutout(safeInsets, bounds, false);
        }

        public synchronized DisplayCutout get() {
            return this.mInner;
        }

        public synchronized void set(ParcelableWrapper cutout) {
            this.mInner = cutout.get();
        }

        public synchronized void set(DisplayCutout cutout) {
            this.mInner = cutout;
        }

        public int hashCode() {
            return this.mInner.hashCode();
        }

        public boolean equals(Object o) {
            return (o instanceof ParcelableWrapper) && this.mInner.equals(((ParcelableWrapper) o).mInner);
        }

        public String toString() {
            return String.valueOf(this.mInner);
        }
    }
}
