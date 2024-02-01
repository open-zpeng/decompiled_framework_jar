package android.view;

import android.content.res.Resources;
import android.graphics.Insets;
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
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes3.dex */
public final class DisplayCutout {
    private static final String BOTTOM_MARKER = "@bottom";
    public static final int BOUNDS_POSITION_BOTTOM = 3;
    public static final int BOUNDS_POSITION_LEFT = 0;
    public static final int BOUNDS_POSITION_LENGTH = 4;
    public static final int BOUNDS_POSITION_RIGHT = 2;
    public static final int BOUNDS_POSITION_TOP = 1;
    private static final Object CACHE_LOCK;
    private static final String DP_MARKER = "@dp";
    public static final String EMULATION_OVERLAY_CATEGORY = "com.android.internal.display_cutout_emulation";
    private static final String LEFT_MARKER = "@left";
    public static final DisplayCutout NO_CUTOUT;
    private static final Pair<Path, DisplayCutout> NULL_PAIR;
    private static final String RIGHT_MARKER = "@right";
    private static final String TAG = "DisplayCutout";
    private static final Rect ZERO_RECT = new Rect();
    @GuardedBy({"CACHE_LOCK"})
    private static Pair<Path, DisplayCutout> sCachedCutout;
    @GuardedBy({"CACHE_LOCK"})
    private static float sCachedDensity;
    @GuardedBy({"CACHE_LOCK"})
    private static int sCachedDisplayHeight;
    @GuardedBy({"CACHE_LOCK"})
    private static int sCachedDisplayWidth;
    @GuardedBy({"CACHE_LOCK"})
    private static String sCachedSpec;
    private final Bounds mBounds;
    private final Rect mSafeInsets;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes3.dex */
    public @interface BoundsPosition {
    }

    static {
        Rect rect = ZERO_RECT;
        NO_CUTOUT = new DisplayCutout(rect, rect, rect, rect, rect, false);
        NULL_PAIR = new Pair<>(null, null);
        CACHE_LOCK = new Object();
        sCachedCutout = NULL_PAIR;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class Bounds {
        private final Rect[] mRects;

        private Bounds(Rect left, Rect top, Rect right, Rect bottom, boolean copyArguments) {
            this.mRects = new Rect[4];
            this.mRects[0] = DisplayCutout.getCopyOrRef(left, copyArguments);
            this.mRects[1] = DisplayCutout.getCopyOrRef(top, copyArguments);
            this.mRects[2] = DisplayCutout.getCopyOrRef(right, copyArguments);
            this.mRects[3] = DisplayCutout.getCopyOrRef(bottom, copyArguments);
        }

        private Bounds(Rect[] rects, boolean copyArguments) {
            if (rects.length != 4) {
                throw new IllegalArgumentException("rects must have exactly 4 elements: rects=" + Arrays.toString(rects));
            } else if (copyArguments) {
                this.mRects = new Rect[4];
                for (int i = 0; i < 4; i++) {
                    this.mRects[i] = new Rect(rects[i]);
                }
            } else {
                for (Rect rect : rects) {
                    if (rect == null) {
                        throw new IllegalArgumentException("rects must have non-null elements: rects=" + Arrays.toString(rects));
                    }
                }
                this.mRects = rects;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isEmpty() {
            Rect[] rectArr;
            for (Rect rect : this.mRects) {
                if (!rect.isEmpty()) {
                    return false;
                }
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Rect getRect(int pos) {
            return new Rect(this.mRects[pos]);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Rect[] getRects() {
            Rect[] rects = new Rect[4];
            for (int i = 0; i < 4; i++) {
                rects[i] = new Rect(this.mRects[i]);
            }
            return rects;
        }

        public int hashCode() {
            Rect[] rectArr;
            int result = 0;
            for (Rect rect : this.mRects) {
                result = (48271 * result) + rect.hashCode();
            }
            return result;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof Bounds) {
                Bounds b = (Bounds) o;
                return Arrays.deepEquals(this.mRects, b.mRects);
            }
            return false;
        }

        public String toString() {
            return "Bounds=" + Arrays.toString(this.mRects);
        }
    }

    public DisplayCutout(Insets safeInsets, Rect boundLeft, Rect boundTop, Rect boundRight, Rect boundBottom) {
        this(safeInsets.toRect(), boundLeft, boundTop, boundRight, boundBottom, true);
    }

    @Deprecated
    public DisplayCutout(Rect safeInsets, List<Rect> boundingRects) {
        this(safeInsets, extractBoundsFromList(safeInsets, boundingRects), true);
    }

    private DisplayCutout(Rect safeInsets, Rect boundLeft, Rect boundTop, Rect boundRight, Rect boundBottom, boolean copyArguments) {
        this.mSafeInsets = getCopyOrRef(safeInsets, copyArguments);
        this.mBounds = new Bounds(boundLeft, boundTop, boundRight, boundBottom, copyArguments);
    }

    private DisplayCutout(Rect safeInsets, Rect[] bounds, boolean copyArguments) {
        this.mSafeInsets = getCopyOrRef(safeInsets, copyArguments);
        this.mBounds = new Bounds(bounds, copyArguments);
    }

    private DisplayCutout(Rect safeInsets, Bounds bounds) {
        this.mSafeInsets = safeInsets;
        this.mBounds = bounds;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Rect getCopyOrRef(Rect r, boolean copyArguments) {
        if (r == null) {
            return ZERO_RECT;
        }
        if (copyArguments) {
            return new Rect(r);
        }
        return r;
    }

    public static Rect[] extractBoundsFromList(Rect safeInsets, List<Rect> boundingRects) {
        Rect[] sortedBounds = new Rect[4];
        for (int i = 0; i < sortedBounds.length; i++) {
            sortedBounds[i] = ZERO_RECT;
        }
        if (safeInsets != null && boundingRects != null) {
            for (Rect bound : boundingRects) {
                if (bound.left == 0) {
                    sortedBounds[0] = bound;
                } else if (bound.top == 0) {
                    sortedBounds[1] = bound;
                } else if (safeInsets.right > 0) {
                    sortedBounds[2] = bound;
                } else if (safeInsets.bottom > 0) {
                    sortedBounds[3] = bound;
                }
            }
        }
        return sortedBounds;
    }

    public boolean isBoundsEmpty() {
        return this.mBounds.isEmpty();
    }

    public boolean isEmpty() {
        return this.mSafeInsets.equals(ZERO_RECT);
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

    public Rect getSafeInsets() {
        return new Rect(this.mSafeInsets);
    }

    public List<Rect> getBoundingRects() {
        Rect[] boundingRectsAll;
        List<Rect> result = new ArrayList<>();
        for (Rect bound : getBoundingRectsAll()) {
            if (!bound.isEmpty()) {
                result.add(new Rect(bound));
            }
        }
        return result;
    }

    public Rect[] getBoundingRectsAll() {
        return this.mBounds.getRects();
    }

    public Rect getBoundingRectLeft() {
        return this.mBounds.getRect(0);
    }

    public Rect getBoundingRectTop() {
        return this.mBounds.getRect(1);
    }

    public Rect getBoundingRectRight() {
        return this.mBounds.getRect(2);
    }

    public Rect getBoundingRectBottom() {
        return this.mBounds.getRect(3);
    }

    public int hashCode() {
        return (this.mSafeInsets.hashCode() * 48271) + this.mBounds.hashCode();
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
        return "DisplayCutout{insets=" + this.mSafeInsets + " boundingRect={" + this.mBounds + "}}";
    }

    public void writeToProto(ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        this.mSafeInsets.writeToProto(proto, 1146756268033L);
        this.mBounds.getRect(0).writeToProto(proto, 1146756268035L);
        this.mBounds.getRect(1).writeToProto(proto, 1146756268036L);
        this.mBounds.getRect(2).writeToProto(proto, 1146756268037L);
        this.mBounds.getRect(3).writeToProto(proto, 1146756268038L);
        proto.end(token);
    }

    public DisplayCutout inset(int insetLeft, int insetTop, int insetRight, int insetBottom) {
        if ((insetLeft == 0 && insetTop == 0 && insetRight == 0 && insetBottom == 0) || isBoundsEmpty()) {
            return this;
        }
        Rect safeInsets = new Rect(this.mSafeInsets);
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
        if (insetLeft != 0 || insetTop != 0 || !this.mSafeInsets.equals(safeInsets)) {
            Rect[] bounds = this.mBounds.getRects();
            for (int i = 0; i < bounds.length; i++) {
                if (!bounds[i].equals(ZERO_RECT)) {
                    bounds[i].offset(-insetLeft, -insetTop);
                }
            }
            return new DisplayCutout(safeInsets, bounds, false);
        }
        return this;
    }

    public DisplayCutout replaceSafeInsets(Rect safeInsets) {
        return new DisplayCutout(new Rect(safeInsets), this.mBounds);
    }

    private static int atLeastZero(int value) {
        if (value < 0) {
            return 0;
        }
        return value;
    }

    @VisibleForTesting
    public static DisplayCutout fromBoundingRect(int left, int top, int right, int bottom, int pos) {
        Rect[] bounds = new Rect[4];
        int i = 0;
        while (i < 4) {
            bounds[i] = pos == i ? new Rect(left, top, right, bottom) : new Rect();
            i++;
        }
        return new DisplayCutout(ZERO_RECT, bounds, false);
    }

    public static DisplayCutout fromBounds(Rect[] bounds) {
        return new DisplayCutout(ZERO_RECT, bounds, false);
    }

    public static DisplayCutout fromResourcesRectApproximation(Resources res, int displayWidth, int displayHeight) {
        return fromSpec(res.getString(R.string.config_mainBuiltInDisplayCutoutRectApproximation), displayWidth, displayHeight, DisplayMetrics.DENSITY_DEVICE_STABLE / 160.0f);
    }

    public static Path pathFromResources(Resources res, int displayWidth, int displayHeight) {
        return pathAndDisplayCutoutFromSpec(res.getString(R.string.config_mainBuiltInDisplayCutout), displayWidth, displayHeight, DisplayMetrics.DENSITY_DEVICE_STABLE / 160.0f).first;
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PRIVATE)
    public static DisplayCutout fromSpec(String spec, int displayWidth, int displayHeight, float density) {
        return pathAndDisplayCutoutFromSpec(spec, displayWidth, displayHeight, density).second;
    }

    private static Pair<Path, DisplayCutout> pathAndDisplayCutoutFromSpec(String spec, int displayWidth, int displayHeight, float density) {
        float offsetX;
        String bottomSpec;
        String bottomSpec2;
        int bottomInset;
        Rect boundBottom;
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
                    } else if (spec2.endsWith(LEFT_MARKER)) {
                        offsetX = 0.0f;
                        spec2 = spec2.substring(0, spec2.length() - LEFT_MARKER.length()).trim();
                    } else {
                        float offsetX2 = displayWidth;
                        offsetX = offsetX2 / 2.0f;
                    }
                    boolean inDp = spec2.endsWith(DP_MARKER);
                    if (inDp) {
                        spec2 = spec2.substring(0, spec2.length() - DP_MARKER.length());
                    }
                    if (!spec2.contains(BOTTOM_MARKER)) {
                        bottomSpec = null;
                        bottomSpec2 = spec2;
                    } else {
                        String[] splits = spec2.split(BOTTOM_MARKER, 2);
                        String spec3 = splits[0].trim();
                        String bottomSpec3 = splits[1].trim();
                        bottomSpec = bottomSpec3;
                        bottomSpec2 = spec3;
                    }
                    Region r = Region.obtain();
                    try {
                        Path p = PathParser.createPathFromPathData(bottomSpec2);
                        Matrix m = new Matrix();
                        if (inDp) {
                            m.postScale(density, density);
                        }
                        m.postTranslate(offsetX, 0.0f);
                        p.transform(m);
                        Rect boundTop = new Rect();
                        toRectAndAddToRegion(p, r, boundTop);
                        int topInset = boundTop.bottom;
                        if (bottomSpec != null) {
                            try {
                                Path bottomPath = PathParser.createPathFromPathData(bottomSpec);
                                m.postTranslate(0.0f, displayHeight);
                                bottomPath.transform(m);
                                p.addPath(bottomPath);
                                Rect boundBottom2 = new Rect();
                                toRectAndAddToRegion(bottomPath, r, boundBottom2);
                                bottomInset = displayHeight - boundBottom2.top;
                                boundBottom = boundBottom2;
                            } catch (Throwable e) {
                                Log.wtf(TAG, "Could not inflate bottom cutout: ", e);
                                return NULL_PAIR;
                            }
                        } else {
                            bottomInset = 0;
                            boundBottom = null;
                        }
                        Rect safeInset = new Rect(0, topInset, 0, bottomInset);
                        DisplayCutout cutout = new DisplayCutout(safeInset, null, boundTop, null, boundBottom, false);
                        Pair<Path, DisplayCutout> result = new Pair<>(p, cutout);
                        synchronized (CACHE_LOCK) {
                            sCachedSpec = bottomSpec2;
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

    /* loaded from: classes3.dex */
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

        public ParcelableWrapper() {
            this(DisplayCutout.NO_CUTOUT);
        }

        public ParcelableWrapper(DisplayCutout cutout) {
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

        public static void writeCutoutToParcel(DisplayCutout cutout, Parcel out, int flags) {
            if (cutout == null) {
                out.writeInt(-1);
            } else if (cutout == DisplayCutout.NO_CUTOUT) {
                out.writeInt(0);
            } else {
                out.writeInt(1);
                out.writeTypedObject(cutout.mSafeInsets, flags);
                out.writeTypedArray(cutout.mBounds.getRects(), flags);
            }
        }

        public void readFromParcel(Parcel in) {
            this.mInner = readCutoutFromParcel(in);
        }

        public static DisplayCutout readCutoutFromParcel(Parcel in) {
            int variant = in.readInt();
            if (variant == -1) {
                return null;
            }
            if (variant == 0) {
                return DisplayCutout.NO_CUTOUT;
            }
            Rect safeInsets = (Rect) in.readTypedObject(Rect.CREATOR);
            Rect[] bounds = new Rect[4];
            in.readTypedArray(bounds, Rect.CREATOR);
            return new DisplayCutout(safeInsets, bounds, false);
        }

        public DisplayCutout get() {
            return this.mInner;
        }

        public void set(ParcelableWrapper cutout) {
            this.mInner = cutout.get();
        }

        public void set(DisplayCutout cutout) {
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
