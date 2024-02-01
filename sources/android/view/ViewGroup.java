package android.view;

import android.animation.LayoutTransition;
import android.annotation.UnsupportedAppUsage;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.media.TtmlUtils;
import android.net.wifi.WifiScanner;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pools;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.WindowInsetsAnimationListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.Transformation;
import android.view.autofill.Helper;
import android.view.inspector.InspectionCompanion;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import com.android.internal.R;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/* loaded from: classes3.dex */
public abstract class ViewGroup extends View implements ViewParent, ViewManager {
    private static final int ARRAY_CAPACITY_INCREMENT = 12;
    private static final int ARRAY_INITIAL_CAPACITY = 12;
    private static final int CHILD_LEFT_INDEX = 0;
    private static final int CHILD_TOP_INDEX = 1;
    protected static final int CLIP_TO_PADDING_MASK = 34;
    @UnsupportedAppUsage
    private static final boolean DBG = false;
    private static final int FLAG_ADD_STATES_FROM_CHILDREN = 8192;
    @Deprecated
    private static final int FLAG_ALWAYS_DRAWN_WITH_CACHE = 16384;
    @Deprecated
    private static final int FLAG_ANIMATION_CACHE = 64;
    static final int FLAG_ANIMATION_DONE = 16;
    @Deprecated
    private static final int FLAG_CHILDREN_DRAWN_WITH_CACHE = 32768;
    static final int FLAG_CLEAR_TRANSFORMATION = 256;
    static final int FLAG_CLIP_CHILDREN = 1;
    private static final int FLAG_CLIP_TO_PADDING = 2;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123983692)
    protected static final int FLAG_DISALLOW_INTERCEPT = 524288;
    static final int FLAG_INVALIDATE_REQUIRED = 4;
    static final int FLAG_IS_TRANSITION_GROUP = 16777216;
    static final int FLAG_IS_TRANSITION_GROUP_SET = 33554432;
    private static final int FLAG_LAYOUT_MODE_WAS_EXPLICITLY_SET = 8388608;
    private static final int FLAG_MASK_FOCUSABILITY = 393216;
    private static final int FLAG_NOTIFY_ANIMATION_LISTENER = 512;
    private static final int FLAG_NOTIFY_CHILDREN_ON_DRAWABLE_STATE_CHANGE = 65536;
    static final int FLAG_OPTIMIZE_INVALIDATE = 128;
    private static final int FLAG_PADDING_NOT_NULL = 32;
    private static final int FLAG_PREVENT_DISPATCH_ATTACHED_TO_WINDOW = 4194304;
    private static final int FLAG_RUN_ANIMATION = 8;
    private static final int FLAG_SHOW_CONTEXT_MENU_WITH_COORDS = 536870912;
    private static final int FLAG_SPLIT_MOTION_EVENTS = 2097152;
    private static final int FLAG_START_ACTION_MODE_FOR_CHILD_IS_NOT_TYPED = 268435456;
    private static final int FLAG_START_ACTION_MODE_FOR_CHILD_IS_TYPED = 134217728;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123769647)
    protected static final int FLAG_SUPPORT_STATIC_TRANSFORMATIONS = 2048;
    static final int FLAG_TOUCHSCREEN_BLOCKS_FOCUS = 67108864;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123769377)
    protected static final int FLAG_USE_CHILD_DRAWING_ORDER = 1024;
    public static final int FOCUS_AFTER_DESCENDANTS = 262144;
    public static final int FOCUS_BEFORE_DESCENDANTS = 131072;
    public static final int FOCUS_BLOCK_DESCENDANTS = 393216;
    public static final int LAYOUT_MODE_CLIP_BOUNDS = 0;
    public static final int LAYOUT_MODE_OPTICAL_BOUNDS = 1;
    private static final int LAYOUT_MODE_UNDEFINED = -1;
    @Deprecated
    public static final int PERSISTENT_ALL_CACHES = 3;
    @Deprecated
    public static final int PERSISTENT_ANIMATION_CACHE = 1;
    @Deprecated
    public static final int PERSISTENT_NO_CACHE = 0;
    @Deprecated
    public static final int PERSISTENT_SCROLLING_CACHE = 2;
    private static final String TAG = "ViewGroup";
    private static float[] sDebugLines;
    private Animation.AnimationListener mAnimationListener;
    Paint mCachePaint;
    @ViewDebug.ExportedProperty(category = TtmlUtils.TAG_LAYOUT)
    private int mChildCountWithTransientState;
    private Transformation mChildTransformation;
    int mChildUnhandledKeyListeners;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private View[] mChildren;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private int mChildrenCount;
    private HashSet<View> mChildrenInterestedInDrag;
    private View mCurrentDragChild;
    private DragEvent mCurrentDragStartEvent;
    private View mDefaultFocus;
    @UnsupportedAppUsage
    protected ArrayList<View> mDisappearingChildren;
    private HoverTarget mFirstHoverTarget;
    @UnsupportedAppUsage
    private TouchTarget mFirstTouchTarget;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private View mFocused;
    View mFocusedInCluster;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123769411)
    @ViewDebug.ExportedProperty(flagMapping = {@ViewDebug.FlagToString(equals = 1, mask = 1, name = "CLIP_CHILDREN"), @ViewDebug.FlagToString(equals = 2, mask = 2, name = "CLIP_TO_PADDING"), @ViewDebug.FlagToString(equals = 32, mask = 32, name = "PADDING_NOT_NULL")}, formatToHexString = true)
    protected int mGroupFlags;
    private boolean mHoveredSelf;
    RectF mInvalidateRegion;
    Transformation mInvalidationTransformation;
    private boolean mIsInterestedInDrag;
    @ViewDebug.ExportedProperty(category = "events")
    private int mLastTouchDownIndex;
    @ViewDebug.ExportedProperty(category = "events")
    private long mLastTouchDownTime;
    @ViewDebug.ExportedProperty(category = "events")
    private float mLastTouchDownX;
    @ViewDebug.ExportedProperty(category = "events")
    private float mLastTouchDownY;
    private LayoutAnimationController mLayoutAnimationController;
    private boolean mLayoutCalledWhileSuppressed;
    private int mLayoutMode;
    private LayoutTransition.TransitionListener mLayoutTransitionListener;
    private PointF mLocalPoint;
    private int mNestedScrollAxes;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123768704)
    protected OnHierarchyChangeListener mOnHierarchyChangeListener;
    @UnsupportedAppUsage
    protected int mPersistentDrawingCache;
    private ArrayList<View> mPreSortedChildren;
    boolean mSuppressLayout;
    private float[] mTempPoint;
    private View mTooltipHoverTarget;
    private boolean mTooltipHoveredSelf;
    private List<Integer> mTransientIndices;
    private List<View> mTransientViews;
    private LayoutTransition mTransition;
    private ArrayList<View> mTransitioningViews;
    private ArrayList<View> mVisibilityChangingChildren;
    private static final int[] DESCENDANT_FOCUSABILITY_FLAGS = {131072, 262144, 393216};
    public static int LAYOUT_MODE_DEFAULT = 0;
    private static final ActionMode SENTINEL_ACTION_MODE = new ActionMode() { // from class: android.view.ViewGroup.1
        @Override // android.view.ActionMode
        public void setTitle(CharSequence title) {
        }

        @Override // android.view.ActionMode
        public void setTitle(int resId) {
        }

        @Override // android.view.ActionMode
        public void setSubtitle(CharSequence subtitle) {
        }

        @Override // android.view.ActionMode
        public void setSubtitle(int resId) {
        }

        @Override // android.view.ActionMode
        public void setCustomView(View view) {
        }

        @Override // android.view.ActionMode
        public void invalidate() {
        }

        @Override // android.view.ActionMode
        public void finish() {
        }

        @Override // android.view.ActionMode
        public Menu getMenu() {
            return null;
        }

        @Override // android.view.ActionMode
        public CharSequence getTitle() {
            return null;
        }

        @Override // android.view.ActionMode
        public CharSequence getSubtitle() {
            return null;
        }

        @Override // android.view.ActionMode
        public View getCustomView() {
            return null;
        }

        @Override // android.view.ActionMode
        public MenuInflater getMenuInflater() {
            return null;
        }
    };

    /* loaded from: classes3.dex */
    public interface OnHierarchyChangeListener {
        void onChildViewAdded(View view, View view2);

        void onChildViewRemoved(View view, View view2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public abstract void onLayout(boolean z, int i, int i2, int i3, int i4);

    /* loaded from: classes3.dex */
    public static class LayoutParams {
        @Deprecated
        public static final int FILL_PARENT = -1;
        public static final int MATCH_PARENT = -1;
        public static final int WRAP_CONTENT = -2;
        @ViewDebug.ExportedProperty(category = TtmlUtils.TAG_LAYOUT, mapping = {@ViewDebug.IntToString(from = -1, to = "MATCH_PARENT"), @ViewDebug.IntToString(from = -2, to = "WRAP_CONTENT")})
        public int height;
        public LayoutAnimationController.AnimationParameters layoutAnimationParameters;
        @ViewDebug.ExportedProperty(category = TtmlUtils.TAG_LAYOUT, mapping = {@ViewDebug.IntToString(from = -1, to = "MATCH_PARENT"), @ViewDebug.IntToString(from = -2, to = "WRAP_CONTENT")})
        public int width;

        /* loaded from: classes3.dex */
        public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<LayoutParams> {
            private int mLayout_heightId;
            private int mLayout_widthId;
            private boolean mPropertiesMapped = false;

            @Override // android.view.inspector.InspectionCompanion
            public void mapProperties(PropertyMapper propertyMapper) {
                SparseArray<String> layout_heightEnumMapping = new SparseArray<>();
                layout_heightEnumMapping.put(-2, "wrap_content");
                layout_heightEnumMapping.put(-1, "match_parent");
                Objects.requireNonNull(layout_heightEnumMapping);
                this.mLayout_heightId = propertyMapper.mapIntEnum("layout_height", 16842997, new $$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(layout_heightEnumMapping));
                SparseArray<String> layout_widthEnumMapping = new SparseArray<>();
                layout_widthEnumMapping.put(-2, "wrap_content");
                layout_widthEnumMapping.put(-1, "match_parent");
                Objects.requireNonNull(layout_widthEnumMapping);
                this.mLayout_widthId = propertyMapper.mapIntEnum("layout_width", 16842996, new $$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(layout_widthEnumMapping));
                this.mPropertiesMapped = true;
            }

            @Override // android.view.inspector.InspectionCompanion
            public void readProperties(LayoutParams node, PropertyReader propertyReader) {
                if (!this.mPropertiesMapped) {
                    throw new InspectionCompanion.UninitializedPropertyMapException();
                }
                propertyReader.readIntEnum(this.mLayout_heightId, node.height);
                propertyReader.readIntEnum(this.mLayout_widthId, node.width);
            }
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.ViewGroup_Layout);
            setBaseAttributes(a, 0, 1);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public LayoutParams(LayoutParams source) {
            this.width = source.width;
            this.height = source.height;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @UnsupportedAppUsage
        public LayoutParams() {
        }

        protected void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
            this.width = a.getLayoutDimension(widthAttr, "layout_width");
            this.height = a.getLayoutDimension(heightAttr, "layout_height");
        }

        public void resolveLayoutDirection(int layoutDirection) {
        }

        public String debug(String output) {
            return output + "ViewGroup.LayoutParams={ width=" + sizeToString(this.width) + ", height=" + sizeToString(this.height) + " }";
        }

        public void onDebugDraw(View view, Canvas canvas, Paint paint) {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public static String sizeToString(int size) {
            if (size == -2) {
                return "wrap-content";
            }
            if (size == -1) {
                return "match-parent";
            }
            return String.valueOf(size);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void encode(ViewHierarchyEncoder encoder) {
            encoder.beginObject(this);
            encodeProperties(encoder);
            encoder.endObject();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void encodeProperties(ViewHierarchyEncoder encoder) {
            encoder.addProperty("width", this.width);
            encoder.addProperty("height", this.height);
        }
    }

    /* loaded from: classes3.dex */
    public static class MarginLayoutParams extends LayoutParams {
        public static final int DEFAULT_MARGIN_RELATIVE = Integer.MIN_VALUE;
        private static final int DEFAULT_MARGIN_RESOLVED = 0;
        private static final int LAYOUT_DIRECTION_MASK = 3;
        private static final int LEFT_MARGIN_UNDEFINED_MASK = 4;
        private static final int NEED_RESOLUTION_MASK = 32;
        private static final int RIGHT_MARGIN_UNDEFINED_MASK = 8;
        private static final int RTL_COMPATIBILITY_MODE_MASK = 16;
        private static final int UNDEFINED_MARGIN = Integer.MIN_VALUE;
        @ViewDebug.ExportedProperty(category = TtmlUtils.TAG_LAYOUT)
        public int bottomMargin;
        @UnsupportedAppUsage
        @ViewDebug.ExportedProperty(category = TtmlUtils.TAG_LAYOUT)
        private int endMargin;
        @ViewDebug.ExportedProperty(category = TtmlUtils.TAG_LAYOUT)
        public int leftMargin;
        @ViewDebug.ExportedProperty(category = TtmlUtils.TAG_LAYOUT, flagMapping = {@ViewDebug.FlagToString(equals = 3, mask = 3, name = "LAYOUT_DIRECTION"), @ViewDebug.FlagToString(equals = 4, mask = 4, name = "LEFT_MARGIN_UNDEFINED_MASK"), @ViewDebug.FlagToString(equals = 8, mask = 8, name = "RIGHT_MARGIN_UNDEFINED_MASK"), @ViewDebug.FlagToString(equals = 16, mask = 16, name = "RTL_COMPATIBILITY_MODE_MASK"), @ViewDebug.FlagToString(equals = 32, mask = 32, name = "NEED_RESOLUTION_MASK")}, formatToHexString = true)
        byte mMarginFlags;
        @ViewDebug.ExportedProperty(category = TtmlUtils.TAG_LAYOUT)
        public int rightMargin;
        @UnsupportedAppUsage
        @ViewDebug.ExportedProperty(category = TtmlUtils.TAG_LAYOUT)
        private int startMargin;
        @ViewDebug.ExportedProperty(category = TtmlUtils.TAG_LAYOUT)
        public int topMargin;

        /* loaded from: classes3.dex */
        public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<MarginLayoutParams> {
            private int mLayout_marginBottomId;
            private int mLayout_marginLeftId;
            private int mLayout_marginRightId;
            private int mLayout_marginTopId;
            private boolean mPropertiesMapped = false;

            @Override // android.view.inspector.InspectionCompanion
            public void mapProperties(PropertyMapper propertyMapper) {
                this.mLayout_marginBottomId = propertyMapper.mapInt("layout_marginBottom", 16843002);
                this.mLayout_marginLeftId = propertyMapper.mapInt("layout_marginLeft", 16842999);
                this.mLayout_marginRightId = propertyMapper.mapInt("layout_marginRight", 16843001);
                this.mLayout_marginTopId = propertyMapper.mapInt("layout_marginTop", 16843000);
                this.mPropertiesMapped = true;
            }

            @Override // android.view.inspector.InspectionCompanion
            public void readProperties(MarginLayoutParams node, PropertyReader propertyReader) {
                if (!this.mPropertiesMapped) {
                    throw new InspectionCompanion.UninitializedPropertyMapException();
                }
                propertyReader.readInt(this.mLayout_marginBottomId, node.bottomMargin);
                propertyReader.readInt(this.mLayout_marginLeftId, node.leftMargin);
                propertyReader.readInt(this.mLayout_marginRightId, node.rightMargin);
                propertyReader.readInt(this.mLayout_marginTopId, node.topMargin);
            }
        }

        public MarginLayoutParams(Context c, AttributeSet attrs) {
            this.startMargin = Integer.MIN_VALUE;
            this.endMargin = Integer.MIN_VALUE;
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.ViewGroup_MarginLayout);
            setBaseAttributes(a, 0, 1);
            int margin = a.getDimensionPixelSize(2, -1);
            if (margin < 0) {
                int horizontalMargin = a.getDimensionPixelSize(9, -1);
                int verticalMargin = a.getDimensionPixelSize(10, -1);
                if (horizontalMargin < 0) {
                    this.leftMargin = a.getDimensionPixelSize(3, Integer.MIN_VALUE);
                    if (this.leftMargin == Integer.MIN_VALUE) {
                        this.mMarginFlags = (byte) (this.mMarginFlags | 4);
                        this.leftMargin = 0;
                    }
                    this.rightMargin = a.getDimensionPixelSize(5, Integer.MIN_VALUE);
                    if (this.rightMargin == Integer.MIN_VALUE) {
                        this.mMarginFlags = (byte) (this.mMarginFlags | 8);
                        this.rightMargin = 0;
                    }
                } else {
                    this.leftMargin = horizontalMargin;
                    this.rightMargin = horizontalMargin;
                }
                this.startMargin = a.getDimensionPixelSize(7, Integer.MIN_VALUE);
                this.endMargin = a.getDimensionPixelSize(8, Integer.MIN_VALUE);
                if (verticalMargin >= 0) {
                    this.topMargin = verticalMargin;
                    this.bottomMargin = verticalMargin;
                } else {
                    this.topMargin = a.getDimensionPixelSize(4, 0);
                    this.bottomMargin = a.getDimensionPixelSize(6, 0);
                }
                if (isMarginRelative()) {
                    this.mMarginFlags = (byte) (this.mMarginFlags | 32);
                }
            } else {
                this.leftMargin = margin;
                this.topMargin = margin;
                this.rightMargin = margin;
                this.bottomMargin = margin;
            }
            boolean hasRtlSupport = c.getApplicationInfo().hasRtlSupport();
            int targetSdkVersion = c.getApplicationInfo().targetSdkVersion;
            if (targetSdkVersion < 17 || !hasRtlSupport) {
                this.mMarginFlags = (byte) (this.mMarginFlags | WifiScanner.PnoSettings.PnoNetwork.FLAG_SAME_NETWORK);
            }
            this.mMarginFlags = (byte) (0 | this.mMarginFlags);
            a.recycle();
        }

        public MarginLayoutParams(int width, int height) {
            super(width, height);
            this.startMargin = Integer.MIN_VALUE;
            this.endMargin = Integer.MIN_VALUE;
            this.mMarginFlags = (byte) (this.mMarginFlags | 4);
            this.mMarginFlags = (byte) (this.mMarginFlags | 8);
            this.mMarginFlags = (byte) (this.mMarginFlags & (-33));
            this.mMarginFlags = (byte) (this.mMarginFlags & (-17));
        }

        public MarginLayoutParams(MarginLayoutParams source) {
            this.startMargin = Integer.MIN_VALUE;
            this.endMargin = Integer.MIN_VALUE;
            this.width = source.width;
            this.height = source.height;
            this.leftMargin = source.leftMargin;
            this.topMargin = source.topMargin;
            this.rightMargin = source.rightMargin;
            this.bottomMargin = source.bottomMargin;
            this.startMargin = source.startMargin;
            this.endMargin = source.endMargin;
            this.mMarginFlags = source.mMarginFlags;
        }

        public MarginLayoutParams(LayoutParams source) {
            super(source);
            this.startMargin = Integer.MIN_VALUE;
            this.endMargin = Integer.MIN_VALUE;
            this.mMarginFlags = (byte) (this.mMarginFlags | 4);
            this.mMarginFlags = (byte) (this.mMarginFlags | 8);
            this.mMarginFlags = (byte) (this.mMarginFlags & (-33));
            this.mMarginFlags = (byte) (this.mMarginFlags & (-17));
        }

        public final void copyMarginsFrom(MarginLayoutParams source) {
            this.leftMargin = source.leftMargin;
            this.topMargin = source.topMargin;
            this.rightMargin = source.rightMargin;
            this.bottomMargin = source.bottomMargin;
            this.startMargin = source.startMargin;
            this.endMargin = source.endMargin;
            this.mMarginFlags = source.mMarginFlags;
        }

        public void setMargins(int left, int top, int right, int bottom) {
            this.leftMargin = left;
            this.topMargin = top;
            this.rightMargin = right;
            this.bottomMargin = bottom;
            this.mMarginFlags = (byte) (this.mMarginFlags & (-5));
            this.mMarginFlags = (byte) (this.mMarginFlags & (-9));
            if (isMarginRelative()) {
                this.mMarginFlags = (byte) (this.mMarginFlags | 32);
            } else {
                this.mMarginFlags = (byte) (this.mMarginFlags & (-33));
            }
        }

        @UnsupportedAppUsage
        public void setMarginsRelative(int start, int top, int end, int bottom) {
            this.startMargin = start;
            this.topMargin = top;
            this.endMargin = end;
            this.bottomMargin = bottom;
            this.mMarginFlags = (byte) (this.mMarginFlags | 32);
        }

        public void setMarginStart(int start) {
            this.startMargin = start;
            this.mMarginFlags = (byte) (this.mMarginFlags | 32);
        }

        public int getMarginStart() {
            int i = this.startMargin;
            if (i != Integer.MIN_VALUE) {
                return i;
            }
            if ((this.mMarginFlags & 32) == 32) {
                doResolveMargins();
            }
            if ((this.mMarginFlags & 3) == 1) {
                return this.rightMargin;
            }
            return this.leftMargin;
        }

        public void setMarginEnd(int end) {
            this.endMargin = end;
            this.mMarginFlags = (byte) (this.mMarginFlags | 32);
        }

        public int getMarginEnd() {
            int i = this.endMargin;
            if (i != Integer.MIN_VALUE) {
                return i;
            }
            if ((this.mMarginFlags & 32) == 32) {
                doResolveMargins();
            }
            if ((this.mMarginFlags & 3) == 1) {
                return this.leftMargin;
            }
            return this.rightMargin;
        }

        public boolean isMarginRelative() {
            return (this.startMargin == Integer.MIN_VALUE && this.endMargin == Integer.MIN_VALUE) ? false : true;
        }

        public void setLayoutDirection(int layoutDirection) {
            if (layoutDirection != 0 && layoutDirection != 1) {
                return;
            }
            byte b = this.mMarginFlags;
            if (layoutDirection != (b & 3)) {
                this.mMarginFlags = (byte) (b & (-4));
                this.mMarginFlags = (byte) (this.mMarginFlags | (layoutDirection & 3));
                if (isMarginRelative()) {
                    this.mMarginFlags = (byte) (this.mMarginFlags | 32);
                } else {
                    this.mMarginFlags = (byte) (this.mMarginFlags & (-33));
                }
            }
        }

        public int getLayoutDirection() {
            return this.mMarginFlags & 3;
        }

        @Override // android.view.ViewGroup.LayoutParams
        public void resolveLayoutDirection(int layoutDirection) {
            setLayoutDirection(layoutDirection);
            if (!isMarginRelative() || (this.mMarginFlags & 32) != 32) {
                return;
            }
            doResolveMargins();
        }

        private void doResolveMargins() {
            int i;
            int i2;
            byte b = this.mMarginFlags;
            if ((b & WifiScanner.PnoSettings.PnoNetwork.FLAG_SAME_NETWORK) == 16) {
                if ((b & 4) == 4 && (i2 = this.startMargin) > Integer.MIN_VALUE) {
                    this.leftMargin = i2;
                }
                if ((this.mMarginFlags & 8) == 8 && (i = this.endMargin) > Integer.MIN_VALUE) {
                    this.rightMargin = i;
                }
            } else if ((b & 3) == 1) {
                int i3 = this.endMargin;
                if (i3 <= Integer.MIN_VALUE) {
                    i3 = 0;
                }
                this.leftMargin = i3;
                int i4 = this.startMargin;
                if (i4 <= Integer.MIN_VALUE) {
                    i4 = 0;
                }
                this.rightMargin = i4;
            } else {
                int i5 = this.startMargin;
                if (i5 <= Integer.MIN_VALUE) {
                    i5 = 0;
                }
                this.leftMargin = i5;
                int i6 = this.endMargin;
                if (i6 <= Integer.MIN_VALUE) {
                    i6 = 0;
                }
                this.rightMargin = i6;
            }
            this.mMarginFlags = (byte) (this.mMarginFlags & (-33));
        }

        public boolean isLayoutRtl() {
            return (this.mMarginFlags & 3) == 1;
        }

        @Override // android.view.ViewGroup.LayoutParams
        public void onDebugDraw(View view, Canvas canvas, Paint paint) {
            Insets oi = View.isLayoutModeOptical(view.mParent) ? view.getOpticalInsets() : Insets.NONE;
            ViewGroup.fillDifference(canvas, view.getLeft() + oi.left, view.getTop() + oi.top, view.getRight() - oi.right, view.getBottom() - oi.bottom, this.leftMargin, this.topMargin, this.rightMargin, this.bottomMargin, paint);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.view.ViewGroup.LayoutParams
        public void encodeProperties(ViewHierarchyEncoder encoder) {
            super.encodeProperties(encoder);
            encoder.addProperty("leftMargin", this.leftMargin);
            encoder.addProperty("topMargin", this.topMargin);
            encoder.addProperty("rightMargin", this.rightMargin);
            encoder.addProperty("bottomMargin", this.bottomMargin);
            encoder.addProperty("startMargin", this.startMargin);
            encoder.addProperty("endMargin", this.endMargin);
        }
    }

    /* loaded from: classes3.dex */
    public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<ViewGroup> {
        private int mAddStatesFromChildrenId;
        private int mAlwaysDrawnWithCacheId;
        private int mAnimationCacheId;
        private int mClipChildrenId;
        private int mClipToPaddingId;
        private int mDescendantFocusabilityId;
        private int mLayoutAnimationId;
        private int mLayoutModeId;
        private int mPersistentDrawingCacheId;
        private boolean mPropertiesMapped = false;
        private int mSplitMotionEventsId;
        private int mTouchscreenBlocksFocusId;
        private int mTransitionGroupId;

        @Override // android.view.inspector.InspectionCompanion
        public void mapProperties(PropertyMapper propertyMapper) {
            this.mAddStatesFromChildrenId = propertyMapper.mapBoolean("addStatesFromChildren", 16842992);
            this.mAlwaysDrawnWithCacheId = propertyMapper.mapBoolean("alwaysDrawnWithCache", 16842991);
            this.mAnimationCacheId = propertyMapper.mapBoolean("animationCache", 16842989);
            this.mClipChildrenId = propertyMapper.mapBoolean("clipChildren", 16842986);
            this.mClipToPaddingId = propertyMapper.mapBoolean("clipToPadding", 16842987);
            SparseArray<String> descendantFocusabilityEnumMapping = new SparseArray<>();
            descendantFocusabilityEnumMapping.put(131072, "beforeDescendants");
            descendantFocusabilityEnumMapping.put(262144, "afterDescendants");
            descendantFocusabilityEnumMapping.put(393216, "blocksDescendants");
            Objects.requireNonNull(descendantFocusabilityEnumMapping);
            this.mDescendantFocusabilityId = propertyMapper.mapIntEnum("descendantFocusability", 16842993, new $$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(descendantFocusabilityEnumMapping));
            this.mLayoutAnimationId = propertyMapper.mapObject("layoutAnimation", 16842988);
            SparseArray<String> layoutModeEnumMapping = new SparseArray<>();
            layoutModeEnumMapping.put(0, "clipBounds");
            layoutModeEnumMapping.put(1, "opticalBounds");
            Objects.requireNonNull(layoutModeEnumMapping);
            this.mLayoutModeId = propertyMapper.mapIntEnum("layoutMode", 16843738, new $$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(layoutModeEnumMapping));
            SparseArray<String> persistentDrawingCacheEnumMapping = new SparseArray<>();
            persistentDrawingCacheEnumMapping.put(0, "none");
            persistentDrawingCacheEnumMapping.put(1, "animation");
            persistentDrawingCacheEnumMapping.put(2, "scrolling");
            persistentDrawingCacheEnumMapping.put(3, "all");
            Objects.requireNonNull(persistentDrawingCacheEnumMapping);
            this.mPersistentDrawingCacheId = propertyMapper.mapIntEnum("persistentDrawingCache", 16842990, new $$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(persistentDrawingCacheEnumMapping));
            this.mSplitMotionEventsId = propertyMapper.mapBoolean("splitMotionEvents", 16843503);
            this.mTouchscreenBlocksFocusId = propertyMapper.mapBoolean("touchscreenBlocksFocus", 16843919);
            this.mTransitionGroupId = propertyMapper.mapBoolean("transitionGroup", 16843777);
            this.mPropertiesMapped = true;
        }

        @Override // android.view.inspector.InspectionCompanion
        public void readProperties(ViewGroup node, PropertyReader propertyReader) {
            if (!this.mPropertiesMapped) {
                throw new InspectionCompanion.UninitializedPropertyMapException();
            }
            propertyReader.readBoolean(this.mAddStatesFromChildrenId, node.addStatesFromChildren());
            propertyReader.readBoolean(this.mAlwaysDrawnWithCacheId, node.isAlwaysDrawnWithCacheEnabled());
            propertyReader.readBoolean(this.mAnimationCacheId, node.isAnimationCacheEnabled());
            propertyReader.readBoolean(this.mClipChildrenId, node.getClipChildren());
            propertyReader.readBoolean(this.mClipToPaddingId, node.getClipToPadding());
            propertyReader.readIntEnum(this.mDescendantFocusabilityId, node.getDescendantFocusability());
            propertyReader.readObject(this.mLayoutAnimationId, node.getLayoutAnimation());
            propertyReader.readIntEnum(this.mLayoutModeId, node.getLayoutMode());
            propertyReader.readIntEnum(this.mPersistentDrawingCacheId, node.getPersistentDrawingCache());
            propertyReader.readBoolean(this.mSplitMotionEventsId, node.isMotionEventSplittingEnabled());
            propertyReader.readBoolean(this.mTouchscreenBlocksFocusId, node.getTouchscreenBlocksFocus());
            propertyReader.readBoolean(this.mTransitionGroupId, node.isTransitionGroup());
        }
    }

    public ViewGroup(Context context) {
        this(context, null);
    }

    public ViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mLastTouchDownIndex = -1;
        this.mLayoutMode = -1;
        this.mSuppressLayout = false;
        this.mLayoutCalledWhileSuppressed = false;
        this.mChildCountWithTransientState = 0;
        this.mTransientIndices = null;
        this.mTransientViews = null;
        this.mChildUnhandledKeyListeners = 0;
        this.mLayoutTransitionListener = new LayoutTransition.TransitionListener() { // from class: android.view.ViewGroup.4
            @Override // android.animation.LayoutTransition.TransitionListener
            public void startTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
                if (transitionType == 3) {
                    ViewGroup.this.startViewTransition(view);
                }
            }

            @Override // android.animation.LayoutTransition.TransitionListener
            public void endTransition(LayoutTransition transition, ViewGroup container, View view, int transitionType) {
                if (ViewGroup.this.mLayoutCalledWhileSuppressed && !transition.isChangingLayout()) {
                    ViewGroup.this.requestLayout();
                    ViewGroup.this.mLayoutCalledWhileSuppressed = false;
                }
                if (transitionType == 3 && ViewGroup.this.mTransitioningViews != null) {
                    ViewGroup.this.endViewTransition(view);
                }
            }
        };
        initViewGroup();
        initFromAttributes(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initViewGroup() {
        if (!debugDraw()) {
            setFlags(128, 128);
        }
        this.mGroupFlags |= 1;
        this.mGroupFlags |= 2;
        this.mGroupFlags |= 16;
        this.mGroupFlags |= 64;
        this.mGroupFlags |= 16384;
        if (this.mContext.getApplicationInfo().targetSdkVersion >= 11) {
            this.mGroupFlags |= 2097152;
        }
        setDescendantFocusability(131072);
        this.mChildren = new View[12];
        this.mChildrenCount = 0;
        this.mPersistentDrawingCache = 2;
    }

    private void initFromAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewGroup, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.ViewGroup, attrs, a, defStyleAttr, defStyleRes);
        int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case 0:
                    setClipChildren(a.getBoolean(attr, true));
                    break;
                case 1:
                    setClipToPadding(a.getBoolean(attr, true));
                    break;
                case 2:
                    int id = a.getResourceId(attr, -1);
                    if (id > 0) {
                        setLayoutAnimation(AnimationUtils.loadLayoutAnimation(this.mContext, id));
                        break;
                    } else {
                        break;
                    }
                case 3:
                    setAnimationCacheEnabled(a.getBoolean(attr, true));
                    break;
                case 4:
                    setPersistentDrawingCache(a.getInt(attr, 2));
                    break;
                case 5:
                    setAlwaysDrawnWithCacheEnabled(a.getBoolean(attr, true));
                    break;
                case 6:
                    setAddStatesFromChildren(a.getBoolean(attr, false));
                    break;
                case 7:
                    setDescendantFocusability(DESCENDANT_FOCUSABILITY_FLAGS[a.getInt(attr, 0)]);
                    break;
                case 8:
                    boolean animateLayoutChanges = a.getBoolean(attr, false);
                    setMotionEventSplittingEnabled(animateLayoutChanges);
                    break;
                case 9:
                    boolean animateLayoutChanges2 = a.getBoolean(attr, false);
                    if (animateLayoutChanges2) {
                        setLayoutTransition(new LayoutTransition());
                        break;
                    } else {
                        break;
                    }
                case 10:
                    setLayoutMode(a.getInt(attr, -1));
                    break;
                case 11:
                    setTransitionGroup(a.getBoolean(attr, false));
                    break;
                case 12:
                    setTouchscreenBlocksFocus(a.getBoolean(attr, false));
                    break;
            }
        }
        a.recycle();
    }

    @ViewDebug.ExportedProperty(category = "focus", mapping = {@ViewDebug.IntToString(from = 131072, to = "FOCUS_BEFORE_DESCENDANTS"), @ViewDebug.IntToString(from = 262144, to = "FOCUS_AFTER_DESCENDANTS"), @ViewDebug.IntToString(from = 393216, to = "FOCUS_BLOCK_DESCENDANTS")})
    public int getDescendantFocusability() {
        return this.mGroupFlags & 393216;
    }

    public void setDescendantFocusability(int focusability) {
        if (focusability != 131072 && focusability != 262144 && focusability != 393216) {
            throw new IllegalArgumentException("must be one of FOCUS_BEFORE_DESCENDANTS, FOCUS_AFTER_DESCENDANTS, FOCUS_BLOCK_DESCENDANTS");
        }
        this.mGroupFlags &= -393217;
        this.mGroupFlags |= 393216 & focusability;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.view.View
    public void handleFocusGainInternal(int direction, Rect previouslyFocusedRect) {
        View view = this.mFocused;
        if (view != null) {
            view.unFocus(this);
            this.mFocused = null;
            this.mFocusedInCluster = null;
        }
        super.handleFocusGainInternal(direction, previouslyFocusedRect);
    }

    @Override // android.view.ViewParent
    public void requestChildFocus(View child, View focused) {
        if (getDescendantFocusability() == 393216) {
            return;
        }
        super.unFocus(focused);
        View view = this.mFocused;
        if (view != child) {
            if (view != null) {
                view.unFocus(focused);
            }
            this.mFocused = child;
        }
        if (this.mParent != null) {
            this.mParent.requestChildFocus(this, focused);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setDefaultFocus(View child) {
        View view = this.mDefaultFocus;
        if (view != null && view.isFocusedByDefault()) {
            return;
        }
        this.mDefaultFocus = child;
        if (this.mParent instanceof ViewGroup) {
            ((ViewGroup) this.mParent).setDefaultFocus(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearDefaultFocus(View child) {
        View view = this.mDefaultFocus;
        if (view != child && view != null && view.isFocusedByDefault()) {
            return;
        }
        this.mDefaultFocus = null;
        for (int i = 0; i < this.mChildrenCount; i++) {
            View sibling = this.mChildren[i];
            if (sibling.isFocusedByDefault()) {
                this.mDefaultFocus = sibling;
                return;
            }
            if (this.mDefaultFocus == null && sibling.hasDefaultFocus()) {
                this.mDefaultFocus = sibling;
            }
        }
        if (this.mParent instanceof ViewGroup) {
            ((ViewGroup) this.mParent).clearDefaultFocus(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.view.View
    public boolean hasDefaultFocus() {
        return this.mDefaultFocus != null || super.hasDefaultFocus();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearFocusedInCluster(View child) {
        if (this.mFocusedInCluster != child) {
            return;
        }
        clearFocusedInCluster();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearFocusedInCluster() {
        View top = findKeyboardNavigationCluster();
        ViewParent parent = this;
        do {
            ((ViewGroup) parent).mFocusedInCluster = null;
            if (parent != top) {
                parent = parent.getParent();
            } else {
                return;
            }
        } while (parent instanceof ViewGroup);
    }

    @Override // android.view.ViewParent
    public void focusableViewAvailable(View v) {
        if (this.mParent != null && getDescendantFocusability() != 393216 && (this.mViewFlags & 12) == 0) {
            if (isFocusableInTouchMode() || !shouldBlockFocusForTouchscreen()) {
                if (!isFocused() || getDescendantFocusability() == 262144) {
                    this.mParent.focusableViewAvailable(v);
                }
            }
        }
    }

    @Override // android.view.ViewParent
    public boolean showContextMenuForChild(View originalView) {
        return (isShowingContextMenuWithCoords() || this.mParent == null || !this.mParent.showContextMenuForChild(originalView)) ? false : true;
    }

    public final boolean isShowingContextMenuWithCoords() {
        return (this.mGroupFlags & 536870912) != 0;
    }

    @Override // android.view.ViewParent
    public boolean showContextMenuForChild(View originalView, float x, float y) {
        try {
            this.mGroupFlags |= 536870912;
            if (showContextMenuForChild(originalView)) {
                return true;
            }
            this.mGroupFlags = (-536870913) & this.mGroupFlags;
            return this.mParent != null && this.mParent.showContextMenuForChild(originalView, x, y);
        } finally {
            this.mGroupFlags = (-536870913) & this.mGroupFlags;
        }
    }

    @Override // android.view.ViewParent
    public ActionMode startActionModeForChild(View originalView, ActionMode.Callback callback) {
        int i = this.mGroupFlags;
        if ((134217728 & i) == 0) {
            try {
                this.mGroupFlags = i | 268435456;
                return startActionModeForChild(originalView, callback, 0);
            } finally {
                this.mGroupFlags = (-268435457) & this.mGroupFlags;
            }
        }
        return SENTINEL_ACTION_MODE;
    }

    public ActionMode startActionModeForChild(View originalView, ActionMode.Callback callback, int type) {
        int i = this.mGroupFlags;
        if ((268435456 & i) == 0 && type == 0) {
            try {
                this.mGroupFlags = i | 134217728;
                ActionMode mode = startActionModeForChild(originalView, callback);
                this.mGroupFlags = (-134217729) & this.mGroupFlags;
                if (mode != SENTINEL_ACTION_MODE) {
                    return mode;
                }
            } catch (Throwable th) {
                this.mGroupFlags = (-134217729) & this.mGroupFlags;
                throw th;
            }
        }
        if (this.mParent != null) {
            try {
                return this.mParent.startActionModeForChild(originalView, callback, type);
            } catch (AbstractMethodError e) {
                return this.mParent.startActionModeForChild(originalView, callback);
            }
        }
        return null;
    }

    @Override // android.view.View
    public boolean dispatchActivityResult(String who, int requestCode, int resultCode, Intent data) {
        if (super.dispatchActivityResult(who, requestCode, resultCode, data)) {
            return true;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.dispatchActivityResult(who, requestCode, resultCode, data)) {
                return true;
            }
        }
        return false;
    }

    @Override // android.view.ViewParent
    public View focusSearch(View focused, int direction) {
        if (isRootNamespace()) {
            return FocusFinder.getInstance().findNextFocus(this, focused, direction);
        }
        if (this.mParent != null) {
            return this.mParent.focusSearch(focused, direction);
        }
        return null;
    }

    @Override // android.view.ViewParent
    public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
        return false;
    }

    @Override // android.view.ViewParent
    public boolean requestSendAccessibilityEvent(View child, AccessibilityEvent event) {
        ViewParent parent = this.mParent;
        if (parent == null) {
            return false;
        }
        boolean propagate = onRequestSendAccessibilityEvent(child, event);
        if (!propagate) {
            return false;
        }
        return parent.requestSendAccessibilityEvent(this, event);
    }

    public boolean onRequestSendAccessibilityEvent(View child, AccessibilityEvent event) {
        if (this.mAccessibilityDelegate != null) {
            return this.mAccessibilityDelegate.onRequestSendAccessibilityEvent(this, child, event);
        }
        return onRequestSendAccessibilityEventInternal(child, event);
    }

    public boolean onRequestSendAccessibilityEventInternal(View child, AccessibilityEvent event) {
        return true;
    }

    @Override // android.view.ViewParent
    public void childHasTransientStateChanged(View child, boolean childHasTransientState) {
        boolean oldHasTransientState = hasTransientState();
        if (childHasTransientState) {
            this.mChildCountWithTransientState++;
        } else {
            this.mChildCountWithTransientState--;
        }
        boolean newHasTransientState = hasTransientState();
        if (this.mParent != null && oldHasTransientState != newHasTransientState) {
            try {
                this.mParent.childHasTransientStateChanged(this, newHasTransientState);
            } catch (AbstractMethodError e) {
                Log.e(TAG, this.mParent.getClass().getSimpleName() + " does not fully implement ViewParent", e);
            }
        }
    }

    @Override // android.view.View
    public boolean hasTransientState() {
        return this.mChildCountWithTransientState > 0 || super.hasTransientState();
    }

    @Override // android.view.View
    public boolean dispatchUnhandledMove(View focused, int direction) {
        View view = this.mFocused;
        return view != null && view.dispatchUnhandledMove(focused, direction);
    }

    @Override // android.view.ViewParent
    public void clearChildFocus(View child) {
        this.mFocused = null;
        if (this.mParent != null) {
            this.mParent.clearChildFocus(this);
        }
    }

    @Override // android.view.View
    public void clearFocus() {
        if (this.mFocused == null) {
            super.clearFocus();
            return;
        }
        View focused = this.mFocused;
        this.mFocused = null;
        focused.clearFocus();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.view.View
    public void unFocus(View focused) {
        View view = this.mFocused;
        if (view == null) {
            super.unFocus(focused);
            return;
        }
        view.unFocus(focused);
        this.mFocused = null;
    }

    public View getFocusedChild() {
        return this.mFocused;
    }

    /* JADX WARN: Multi-variable type inference failed */
    View getDeepestFocusedChild() {
        View v = this;
        while (true) {
            View view = null;
            if (v == null) {
                return null;
            }
            if (v.isFocused()) {
                return v;
            }
            if (v instanceof ViewGroup) {
                view = ((ViewGroup) v).getFocusedChild();
            }
            v = view;
        }
    }

    @Override // android.view.View
    public boolean hasFocus() {
        return ((this.mPrivateFlags & 2) == 0 && this.mFocused == null) ? false : true;
    }

    @Override // android.view.View
    public View findFocus() {
        if (isFocused()) {
            return this;
        }
        View view = this.mFocused;
        if (view != null) {
            return view.findFocus();
        }
        return null;
    }

    @Override // android.view.View
    boolean hasFocusable(boolean allowAutoFocus, boolean dispatchExplicit) {
        if ((this.mViewFlags & 12) != 0) {
            return false;
        }
        if ((allowAutoFocus || getFocusable() != 16) && isFocusable()) {
            return true;
        }
        int descendantFocusability = getDescendantFocusability();
        if (descendantFocusability != 393216) {
            return hasFocusableChild(dispatchExplicit);
        }
        return false;
    }

    boolean hasFocusableChild(boolean dispatchExplicit) {
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            View child = children[i];
            if (!dispatchExplicit || !child.hasExplicitFocusable()) {
                if (!dispatchExplicit && child.hasFocusable()) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    @Override // android.view.View
    public void addFocusables(ArrayList<View> views, int direction, int focusableMode) {
        int focusableCount = views.size();
        int descendantFocusability = getDescendantFocusability();
        boolean blockFocusForTouchscreen = shouldBlockFocusForTouchscreen();
        boolean focusSelf = isFocusableInTouchMode() || !blockFocusForTouchscreen;
        if (descendantFocusability == 393216) {
            if (focusSelf) {
                super.addFocusables(views, direction, focusableMode);
                return;
            }
            return;
        }
        if (blockFocusForTouchscreen) {
            focusableMode |= 1;
        }
        if (descendantFocusability == 131072 && focusSelf) {
            super.addFocusables(views, direction, focusableMode);
        }
        int count = 0;
        View[] children = new View[this.mChildrenCount];
        for (int i = 0; i < this.mChildrenCount; i++) {
            View child = this.mChildren[i];
            if ((child.mViewFlags & 12) == 0) {
                children[count] = child;
                count++;
            }
        }
        FocusFinder.sort(children, 0, count, this, isLayoutRtl());
        for (int i2 = 0; i2 < count; i2++) {
            children[i2].addFocusables(views, direction, focusableMode);
        }
        if (descendantFocusability == 262144 && focusSelf && focusableCount == views.size()) {
            super.addFocusables(views, direction, focusableMode);
        }
    }

    @Override // android.view.View
    public void addKeyboardNavigationClusters(Collection<View> views, int direction) {
        int focusableCount = views.size();
        if (isKeyboardNavigationCluster()) {
            boolean blockedFocus = getTouchscreenBlocksFocus();
            try {
                setTouchscreenBlocksFocusNoRefocus(false);
                super.addKeyboardNavigationClusters(views, direction);
            } finally {
                setTouchscreenBlocksFocusNoRefocus(blockedFocus);
            }
        } else {
            super.addKeyboardNavigationClusters(views, direction);
        }
        if (focusableCount != views.size() || getDescendantFocusability() == 393216) {
            return;
        }
        int count = 0;
        View[] visibleChildren = new View[this.mChildrenCount];
        for (int i = 0; i < this.mChildrenCount; i++) {
            View child = this.mChildren[i];
            if ((child.mViewFlags & 12) == 0) {
                visibleChildren[count] = child;
                count++;
            }
        }
        FocusFinder.sort(visibleChildren, 0, count, this, isLayoutRtl());
        for (int i2 = 0; i2 < count; i2++) {
            visibleChildren[i2].addKeyboardNavigationClusters(views, direction);
        }
    }

    public void setTouchscreenBlocksFocus(boolean touchscreenBlocksFocus) {
        View newFocus;
        if (touchscreenBlocksFocus) {
            this.mGroupFlags |= 67108864;
            if (hasFocus() && !isKeyboardNavigationCluster()) {
                View focusedChild = getDeepestFocusedChild();
                if (!focusedChild.isFocusableInTouchMode() && (newFocus = focusSearch(2)) != null) {
                    newFocus.requestFocus();
                    return;
                }
                return;
            }
            return;
        }
        this.mGroupFlags &= -67108865;
    }

    private void setTouchscreenBlocksFocusNoRefocus(boolean touchscreenBlocksFocus) {
        if (touchscreenBlocksFocus) {
            this.mGroupFlags |= 67108864;
        } else {
            this.mGroupFlags &= -67108865;
        }
    }

    @ViewDebug.ExportedProperty(category = "focus")
    public boolean getTouchscreenBlocksFocus() {
        return (this.mGroupFlags & 67108864) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldBlockFocusForTouchscreen() {
        return getTouchscreenBlocksFocus() && this.mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN) && (!isKeyboardNavigationCluster() || (!hasFocus() && findKeyboardNavigationCluster() == this));
    }

    @Override // android.view.View
    public void findViewsWithText(ArrayList<View> outViews, CharSequence text, int flags) {
        super.findViewsWithText(outViews, text, flags);
        int childrenCount = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < childrenCount; i++) {
            View child = children[i];
            if ((child.mViewFlags & 12) == 0 && (child.mPrivateFlags & 8) == 0) {
                child.findViewsWithText(outViews, text, flags);
            }
        }
    }

    @Override // android.view.View
    public View findViewByAccessibilityIdTraversal(int accessibilityId) {
        View foundView = super.findViewByAccessibilityIdTraversal(accessibilityId);
        if (foundView != null) {
            return foundView;
        }
        if (getAccessibilityNodeProvider() != null) {
            return null;
        }
        int childrenCount = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < childrenCount; i++) {
            View child = children[i];
            View foundView2 = child.findViewByAccessibilityIdTraversal(accessibilityId);
            if (foundView2 != null) {
                return foundView2;
            }
        }
        return null;
    }

    @Override // android.view.View
    public View findViewByAutofillIdTraversal(int autofillId) {
        View foundView = super.findViewByAutofillIdTraversal(autofillId);
        if (foundView != null) {
            return foundView;
        }
        int childrenCount = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < childrenCount; i++) {
            View child = children[i];
            View foundView2 = child.findViewByAutofillIdTraversal(autofillId);
            if (foundView2 != null) {
                return foundView2;
            }
        }
        return null;
    }

    @Override // android.view.View
    public void dispatchWindowFocusChanged(boolean hasFocus) {
        super.dispatchWindowFocusChanged(hasFocus);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchWindowFocusChanged(hasFocus);
        }
    }

    @Override // android.view.View
    public void addTouchables(ArrayList<View> views) {
        super.addTouchables(views);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            View child = children[i];
            if ((child.mViewFlags & 12) == 0) {
                child.addTouchables(views);
            }
        }
    }

    @Override // android.view.View
    @UnsupportedAppUsage
    public void makeOptionalFitsSystemWindows() {
        super.makeOptionalFitsSystemWindows();
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].makeOptionalFitsSystemWindows();
        }
    }

    @Override // android.view.View
    public void dispatchDisplayHint(int hint) {
        super.dispatchDisplayHint(hint);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchDisplayHint(hint);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @UnsupportedAppUsage
    public void onChildVisibilityChanged(View child, int oldVisibility, int newVisibility) {
        LayoutTransition layoutTransition = this.mTransition;
        if (layoutTransition != null) {
            if (newVisibility == 0) {
                layoutTransition.showChild(this, child, oldVisibility);
            } else {
                layoutTransition.hideChild(this, child, newVisibility);
                ArrayList<View> arrayList = this.mTransitioningViews;
                if (arrayList != null && arrayList.contains(child)) {
                    if (this.mVisibilityChangingChildren == null) {
                        this.mVisibilityChangingChildren = new ArrayList<>();
                    }
                    this.mVisibilityChangingChildren.add(child);
                    addDisappearingView(child);
                }
            }
        }
        if (newVisibility == 0 && this.mCurrentDragStartEvent != null && !this.mChildrenInterestedInDrag.contains(child)) {
            notifyChildOfDragStart(child);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void dispatchVisibilityChanged(View changedView, int visibility) {
        super.dispatchVisibilityChanged(changedView, visibility);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchVisibilityChanged(changedView, visibility);
        }
    }

    @Override // android.view.View
    public void dispatchWindowVisibilityChanged(int visibility) {
        super.dispatchWindowVisibilityChanged(visibility);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchWindowVisibilityChanged(visibility);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.view.View
    public boolean dispatchVisibilityAggregated(boolean isVisible) {
        boolean isVisible2 = super.dispatchVisibilityAggregated(isVisible);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            if (children[i].getVisibility() == 0) {
                children[i].dispatchVisibilityAggregated(isVisible2);
            }
        }
        return isVisible2;
    }

    @Override // android.view.View
    public void dispatchConfigurationChanged(Configuration newConfig) {
        super.dispatchConfigurationChanged(newConfig);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchConfigurationChanged(newConfig);
        }
    }

    @Override // android.view.ViewParent
    public void recomputeViewAttributes(View child) {
        ViewParent parent;
        if (this.mAttachInfo == null || this.mAttachInfo.mRecomputeGlobalAttributes || (parent = this.mParent) == null) {
            return;
        }
        parent.recomputeViewAttributes(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.view.View
    public void dispatchCollectViewAttributes(View.AttachInfo attachInfo, int visibility) {
        if ((visibility & 12) == 0) {
            super.dispatchCollectViewAttributes(attachInfo, visibility);
            int count = this.mChildrenCount;
            View[] children = this.mChildren;
            for (int i = 0; i < count; i++) {
                View child = children[i];
                child.dispatchCollectViewAttributes(attachInfo, (child.mViewFlags & 12) | visibility);
            }
        }
    }

    @Override // android.view.ViewParent
    public void bringChildToFront(View child) {
        int index = indexOfChild(child);
        if (index >= 0) {
            removeFromArray(index);
            addInArray(child, this.mChildrenCount);
            child.mParent = this;
            requestLayout();
            invalidate();
        }
    }

    private PointF getLocalPoint() {
        if (this.mLocalPoint == null) {
            this.mLocalPoint = new PointF();
        }
        return this.mLocalPoint;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.view.View
    public boolean dispatchDragEnterExitInPreN(DragEvent event) {
        View view;
        if (event.mAction == 6 && (view = this.mCurrentDragChild) != null) {
            view.dispatchDragEnterExitInPreN(event);
            this.mCurrentDragChild = null;
        }
        return this.mIsInterestedInDrag && super.dispatchDragEnterExitInPreN(event);
    }

    @Override // android.view.View
    public boolean dispatchDragEvent(DragEvent event) {
        boolean eventWasConsumed;
        boolean retval = false;
        float tx = event.mX;
        float ty = event.mY;
        ClipData td = event.mClipData;
        PointF localPoint = getLocalPoint();
        int i = event.mAction;
        if (i == 1) {
            this.mCurrentDragChild = null;
            this.mCurrentDragStartEvent = DragEvent.obtain(event);
            HashSet<View> hashSet = this.mChildrenInterestedInDrag;
            if (hashSet == null) {
                this.mChildrenInterestedInDrag = new HashSet<>();
            } else {
                hashSet.clear();
            }
            int count = this.mChildrenCount;
            View[] children = this.mChildren;
            for (int i2 = 0; i2 < count; i2++) {
                View child = children[i2];
                child.mPrivateFlags2 &= -4;
                if (child.getVisibility() == 0 && notifyChildOfDragStart(children[i2])) {
                    retval = true;
                }
            }
            this.mIsInterestedInDrag = super.dispatchDragEvent(event);
            if (this.mIsInterestedInDrag) {
                retval = true;
            }
            if (!retval) {
                this.mCurrentDragStartEvent.recycle();
                this.mCurrentDragStartEvent = null;
                return retval;
            }
            return retval;
        } else if (i != 2 && i != 3) {
            if (i != 4) {
                return false;
            }
            HashSet<View> childrenInterestedInDrag = this.mChildrenInterestedInDrag;
            if (childrenInterestedInDrag != null) {
                Iterator<View> it = childrenInterestedInDrag.iterator();
                while (it.hasNext()) {
                    if (it.next().dispatchDragEvent(event)) {
                        retval = true;
                    }
                }
                childrenInterestedInDrag.clear();
            }
            DragEvent dragEvent = this.mCurrentDragStartEvent;
            if (dragEvent != null) {
                dragEvent.recycle();
                this.mCurrentDragStartEvent = null;
            }
            if (this.mIsInterestedInDrag) {
                if (super.dispatchDragEvent(event)) {
                    retval = true;
                }
                this.mIsInterestedInDrag = false;
                return retval;
            }
            return retval;
        } else {
            View target = findFrontmostDroppableChildAt(event.mX, event.mY, localPoint);
            if (target != this.mCurrentDragChild) {
                if (sCascadedDragDrop) {
                    int action = event.mAction;
                    event.mX = 0.0f;
                    event.mY = 0.0f;
                    event.mClipData = null;
                    View view = this.mCurrentDragChild;
                    if (view != null) {
                        event.mAction = 6;
                        view.dispatchDragEnterExitInPreN(event);
                    }
                    if (target != null) {
                        event.mAction = 5;
                        target.dispatchDragEnterExitInPreN(event);
                    }
                    event.mAction = action;
                    event.mX = tx;
                    event.mY = ty;
                    event.mClipData = td;
                }
                this.mCurrentDragChild = target;
            }
            if (target == null && this.mIsInterestedInDrag) {
                target = this;
            }
            if (target == null) {
                return false;
            }
            if (target != this) {
                event.mX = localPoint.x;
                event.mY = localPoint.y;
                boolean retval2 = target.dispatchDragEvent(event);
                event.mX = tx;
                event.mY = ty;
                if (this.mIsInterestedInDrag) {
                    if (sCascadedDragDrop) {
                        eventWasConsumed = retval2;
                    } else {
                        eventWasConsumed = event.mEventHandlerWasCalled;
                    }
                    if (!eventWasConsumed) {
                        return super.dispatchDragEvent(event);
                    }
                    return retval2;
                }
                return retval2;
            }
            return super.dispatchDragEvent(event);
        }
    }

    View findFrontmostDroppableChildAt(float x, float y, PointF outLocalPoint) {
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = count - 1; i >= 0; i--) {
            View child = children[i];
            if (child.canAcceptDrag() && isTransformedTouchPointInView(x, y, child, outLocalPoint)) {
                return child;
            }
        }
        return null;
    }

    boolean notifyChildOfDragStart(View child) {
        float tx = this.mCurrentDragStartEvent.mX;
        float ty = this.mCurrentDragStartEvent.mY;
        float[] point = getTempPoint();
        point[0] = tx;
        point[1] = ty;
        transformPointToViewLocal(point, child);
        DragEvent dragEvent = this.mCurrentDragStartEvent;
        dragEvent.mX = point[0];
        dragEvent.mY = point[1];
        boolean canAccept = child.dispatchDragEvent(dragEvent);
        DragEvent dragEvent2 = this.mCurrentDragStartEvent;
        dragEvent2.mX = tx;
        dragEvent2.mY = ty;
        dragEvent2.mEventHandlerWasCalled = false;
        if (canAccept) {
            this.mChildrenInterestedInDrag.add(child);
            if (!child.canAcceptDrag()) {
                child.mPrivateFlags2 |= 1;
                child.refreshDrawableState();
            }
        }
        return canAccept;
    }

    @Override // android.view.View
    public void dispatchWindowSystemUiVisiblityChanged(int visible) {
        super.dispatchWindowSystemUiVisiblityChanged(visible);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            View child = children[i];
            child.dispatchWindowSystemUiVisiblityChanged(visible);
        }
    }

    @Override // android.view.View
    public void dispatchSystemUiVisibilityChanged(int visible) {
        super.dispatchSystemUiVisibilityChanged(visible);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            View child = children[i];
            child.dispatchSystemUiVisibilityChanged(visible);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.view.View
    public boolean updateLocalSystemUiVisibility(int localValue, int localChanges) {
        boolean changed = super.updateLocalSystemUiVisibility(localValue, localChanges);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            View child = children[i];
            changed |= child.updateLocalSystemUiVisibility(localValue, localChanges);
        }
        return changed;
    }

    @Override // android.view.View
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if ((this.mPrivateFlags & 18) == 18) {
            return super.dispatchKeyEventPreIme(event);
        }
        View view = this.mFocused;
        if (view != null && (view.mPrivateFlags & 16) == 16) {
            return this.mFocused.dispatchKeyEventPreIme(event);
        }
        return false;
    }

    @Override // android.view.View
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (this.mInputEventConsistencyVerifier != null) {
            this.mInputEventConsistencyVerifier.onKeyEvent(event, 1);
        }
        if ((this.mPrivateFlags & 18) == 18) {
            if (super.dispatchKeyEvent(event)) {
                return true;
            }
        } else {
            View view = this.mFocused;
            if (view != null && (view.mPrivateFlags & 16) == 16 && this.mFocused.dispatchKeyEvent(event)) {
                return true;
            }
        }
        if (this.mInputEventConsistencyVerifier != null) {
            this.mInputEventConsistencyVerifier.onUnhandledEvent(event, 1);
            return false;
        }
        return false;
    }

    @Override // android.view.View
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        if ((this.mPrivateFlags & 18) == 18) {
            return super.dispatchKeyShortcutEvent(event);
        }
        View view = this.mFocused;
        if (view != null && (view.mPrivateFlags & 16) == 16) {
            return this.mFocused.dispatchKeyShortcutEvent(event);
        }
        return false;
    }

    @Override // android.view.View
    public boolean dispatchTrackballEvent(MotionEvent event) {
        if (this.mInputEventConsistencyVerifier != null) {
            this.mInputEventConsistencyVerifier.onTrackballEvent(event, 1);
        }
        if ((this.mPrivateFlags & 18) == 18) {
            if (super.dispatchTrackballEvent(event)) {
                return true;
            }
        } else {
            View view = this.mFocused;
            if (view != null && (view.mPrivateFlags & 16) == 16 && this.mFocused.dispatchTrackballEvent(event)) {
                return true;
            }
        }
        if (this.mInputEventConsistencyVerifier != null) {
            this.mInputEventConsistencyVerifier.onUnhandledEvent(event, 1);
            return false;
        }
        return false;
    }

    @Override // android.view.View
    public boolean dispatchCapturedPointerEvent(MotionEvent event) {
        if ((this.mPrivateFlags & 18) == 18) {
            return super.dispatchCapturedPointerEvent(event);
        }
        View view = this.mFocused;
        return view != null && (view.mPrivateFlags & 16) == 16 && this.mFocused.dispatchCapturedPointerEvent(event);
    }

    @Override // android.view.View
    public void dispatchPointerCaptureChanged(boolean hasCapture) {
        exitHoverTargets();
        super.dispatchPointerCaptureChanged(hasCapture);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchPointerCaptureChanged(hasCapture);
        }
    }

    @Override // android.view.View
    public PointerIcon onResolvePointerIcon(MotionEvent event, int pointerIndex) {
        PointerIcon pointerIcon;
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);
        if (isOnScrollbarThumb(x, y) || isDraggingScrollBar()) {
            return PointerIcon.getSystemIcon(this.mContext, 1000);
        }
        int childrenCount = this.mChildrenCount;
        if (childrenCount != 0) {
            ArrayList<View> preorderedList = buildOrderedChildList();
            boolean customOrder = preorderedList == null && isChildrenDrawingOrderEnabled();
            View[] children = this.mChildren;
            for (int i = childrenCount - 1; i >= 0; i--) {
                int childIndex = getAndVerifyPreorderedIndex(childrenCount, i, customOrder);
                View child = getAndVerifyPreorderedView(preorderedList, children, childIndex);
                if (child.canReceivePointerEvents() && isTransformedTouchPointInView(x, y, child, null) && (pointerIcon = dispatchResolvePointerIcon(event, pointerIndex, child)) != null) {
                    if (preorderedList != null) {
                        preorderedList.clear();
                    }
                    return pointerIcon;
                }
            }
            if (preorderedList != null) {
                preorderedList.clear();
            }
        }
        return super.onResolvePointerIcon(event, pointerIndex);
    }

    private PointerIcon dispatchResolvePointerIcon(MotionEvent event, int pointerIndex, View child) {
        if (!child.hasIdentityMatrix()) {
            MotionEvent transformedEvent = getTransformedMotionEvent(event, child);
            PointerIcon pointerIcon = child.onResolvePointerIcon(transformedEvent, pointerIndex);
            transformedEvent.recycle();
            return pointerIcon;
        }
        float offsetX = this.mScrollX - child.mLeft;
        float offsetY = this.mScrollY - child.mTop;
        event.offsetLocation(offsetX, offsetY);
        PointerIcon pointerIcon2 = child.onResolvePointerIcon(event, pointerIndex);
        event.offsetLocation(-offsetX, -offsetY);
        return pointerIcon2;
    }

    private int getAndVerifyPreorderedIndex(int childrenCount, int i, boolean customOrder) {
        if (customOrder) {
            int childIndex1 = getChildDrawingOrder(childrenCount, i);
            if (childIndex1 >= childrenCount) {
                throw new IndexOutOfBoundsException("getChildDrawingOrder() returned invalid index " + childIndex1 + " (child count is " + childrenCount + ")");
            }
            return childIndex1;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:57:0x011a  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0163  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x016c  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x01c1  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean dispatchHoverEvent(android.view.MotionEvent r26) {
        /*
            Method dump skipped, instructions count: 453
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.ViewGroup.dispatchHoverEvent(android.view.MotionEvent):boolean");
    }

    private void exitHoverTargets() {
        if (this.mHoveredSelf || this.mFirstHoverTarget != null) {
            long now = SystemClock.uptimeMillis();
            MotionEvent event = MotionEvent.obtain(now, now, 10, 0.0f, 0.0f, 0);
            event.setSource(4098);
            dispatchHoverEvent(event);
            event.recycle();
        }
    }

    private void cancelHoverTarget(View view) {
        HoverTarget predecessor = null;
        HoverTarget target = this.mFirstHoverTarget;
        while (target != null) {
            HoverTarget next = target.next;
            if (target.child == view) {
                if (predecessor == null) {
                    this.mFirstHoverTarget = next;
                } else {
                    predecessor.next = next;
                }
                target.recycle();
                long now = SystemClock.uptimeMillis();
                MotionEvent event = MotionEvent.obtain(now, now, 10, 0.0f, 0.0f, 0);
                event.setSource(4098);
                view.dispatchHoverEvent(event);
                event.recycle();
                return;
            }
            predecessor = target;
            target = next;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.view.View
    public boolean dispatchTooltipHoverEvent(MotionEvent event) {
        int action = event.getAction();
        if (action != 7) {
            if (action != 9 && action == 10) {
                View view = this.mTooltipHoverTarget;
                if (view != null) {
                    view.dispatchTooltipHoverEvent(event);
                    this.mTooltipHoverTarget = null;
                } else if (this.mTooltipHoveredSelf) {
                    super.dispatchTooltipHoverEvent(event);
                    this.mTooltipHoveredSelf = false;
                }
            }
            return false;
        }
        View newTarget = null;
        int childrenCount = this.mChildrenCount;
        if (childrenCount != 0) {
            float x = event.getX();
            float y = event.getY();
            ArrayList<View> preorderedList = buildOrderedChildList();
            boolean customOrder = preorderedList == null && isChildrenDrawingOrderEnabled();
            View[] children = this.mChildren;
            int i = childrenCount - 1;
            while (true) {
                if (i < 0) {
                    break;
                }
                int childIndex = getAndVerifyPreorderedIndex(childrenCount, i, customOrder);
                View child = getAndVerifyPreorderedView(preorderedList, children, childIndex);
                if (!child.canReceivePointerEvents() || !isTransformedTouchPointInView(x, y, child, null) || !dispatchTooltipHoverEvent(event, child)) {
                    i--;
                } else {
                    newTarget = child;
                    break;
                }
            }
            if (preorderedList != null) {
                preorderedList.clear();
            }
        }
        View view2 = this.mTooltipHoverTarget;
        if (view2 != newTarget) {
            if (view2 != null) {
                event.setAction(10);
                this.mTooltipHoverTarget.dispatchTooltipHoverEvent(event);
                event.setAction(action);
            }
            this.mTooltipHoverTarget = newTarget;
        }
        if (this.mTooltipHoverTarget == null) {
            this.mTooltipHoveredSelf = super.dispatchTooltipHoverEvent(event);
            return this.mTooltipHoveredSelf;
        } else if (this.mTooltipHoveredSelf) {
            this.mTooltipHoveredSelf = false;
            event.setAction(10);
            super.dispatchTooltipHoverEvent(event);
            event.setAction(action);
            return true;
        } else {
            return true;
        }
    }

    private boolean dispatchTooltipHoverEvent(MotionEvent event, View child) {
        if (!child.hasIdentityMatrix()) {
            MotionEvent transformedEvent = getTransformedMotionEvent(event, child);
            boolean result = child.dispatchTooltipHoverEvent(transformedEvent);
            transformedEvent.recycle();
            return result;
        }
        float offsetX = this.mScrollX - child.mLeft;
        float offsetY = this.mScrollY - child.mTop;
        event.offsetLocation(offsetX, offsetY);
        boolean result2 = child.dispatchTooltipHoverEvent(event);
        event.offsetLocation(-offsetX, -offsetY);
        return result2;
    }

    private void exitTooltipHoverTargets() {
        if (this.mTooltipHoveredSelf || this.mTooltipHoverTarget != null) {
            long now = SystemClock.uptimeMillis();
            MotionEvent event = MotionEvent.obtain(now, now, 10, 0.0f, 0.0f, 0);
            event.setSource(4098);
            dispatchTooltipHoverEvent(event);
            event.recycle();
        }
    }

    @Override // android.view.View
    protected boolean hasHoveredChild() {
        return this.mFirstHoverTarget != null;
    }

    @Override // android.view.View
    protected boolean pointInHoveredChild(MotionEvent event) {
        if (this.mFirstHoverTarget != null) {
            return isTransformedTouchPointInView(event.getX(), event.getY(), this.mFirstHoverTarget.child, null);
        }
        return false;
    }

    @Override // android.view.View
    public void addChildrenForAccessibility(ArrayList<View> outChildren) {
        if (getAccessibilityNodeProvider() != null) {
            return;
        }
        ChildListForAccessibility children = ChildListForAccessibility.obtain(this, true);
        try {
            int childrenCount = children.getChildCount();
            for (int i = 0; i < childrenCount; i++) {
                View child = children.getChildAt(i);
                if ((child.mViewFlags & 12) == 0) {
                    if (child.includeForAccessibility()) {
                        outChildren.add(child);
                    } else {
                        child.addChildrenForAccessibility(outChildren);
                    }
                }
            }
        } finally {
            children.recycle();
        }
    }

    public boolean onInterceptHoverEvent(MotionEvent event) {
        if (event.isFromSource(8194)) {
            int action = event.getAction();
            float x = event.getX();
            float y = event.getY();
            if ((action == 7 || action == 9) && isOnScrollbar(x, y)) {
                return true;
            }
            return false;
        }
        return false;
    }

    private static MotionEvent obtainMotionEventNoHistoryOrSelf(MotionEvent event) {
        if (event.getHistorySize() == 0) {
            return event;
        }
        return MotionEvent.obtainNoHistory(event);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public boolean dispatchGenericPointerEvent(MotionEvent event) {
        int childrenCount = this.mChildrenCount;
        if (childrenCount != 0) {
            float x = event.getX();
            float y = event.getY();
            ArrayList<View> preorderedList = buildOrderedChildList();
            boolean customOrder = preorderedList == null && isChildrenDrawingOrderEnabled();
            View[] children = this.mChildren;
            for (int i = childrenCount - 1; i >= 0; i--) {
                int childIndex = getAndVerifyPreorderedIndex(childrenCount, i, customOrder);
                View child = getAndVerifyPreorderedView(preorderedList, children, childIndex);
                if (child.canReceivePointerEvents() && isTransformedTouchPointInView(x, y, child, null) && dispatchTransformedGenericPointerEvent(event, child)) {
                    if (preorderedList != null) {
                        preorderedList.clear();
                    }
                    return true;
                }
            }
            if (preorderedList != null) {
                preorderedList.clear();
            }
        }
        return super.dispatchGenericPointerEvent(event);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public boolean dispatchGenericFocusedEvent(MotionEvent event) {
        if ((this.mPrivateFlags & 18) == 18) {
            return super.dispatchGenericFocusedEvent(event);
        }
        View view = this.mFocused;
        if (view != null && (view.mPrivateFlags & 16) == 16) {
            return this.mFocused.dispatchGenericMotionEvent(event);
        }
        return false;
    }

    private boolean dispatchTransformedGenericPointerEvent(MotionEvent event, View child) {
        if (!child.hasIdentityMatrix()) {
            MotionEvent transformedEvent = getTransformedMotionEvent(event, child);
            boolean handled = child.dispatchGenericMotionEvent(transformedEvent);
            transformedEvent.recycle();
            return handled;
        }
        float offsetX = this.mScrollX - child.mLeft;
        float offsetY = this.mScrollY - child.mTop;
        event.offsetLocation(offsetX, offsetY);
        boolean handled2 = child.dispatchGenericMotionEvent(event);
        event.offsetLocation(-offsetX, -offsetY);
        return handled2;
    }

    private MotionEvent getTransformedMotionEvent(MotionEvent event, View child) {
        float offsetX = this.mScrollX - child.mLeft;
        float offsetY = this.mScrollY - child.mTop;
        MotionEvent transformedEvent = MotionEvent.obtain(event);
        transformedEvent.offsetLocation(offsetX, offsetY);
        if (!child.hasIdentityMatrix()) {
            transformedEvent.transform(child.getInverseMatrix());
        }
        return transformedEvent;
    }

    /* JADX WARN: Removed duplicated region for block: B:120:0x01ed  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x01f4  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean dispatchTouchEvent(android.view.MotionEvent r30) {
        /*
            Method dump skipped, instructions count: 609
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.ViewGroup.dispatchTouchEvent(android.view.MotionEvent):boolean");
    }

    public ArrayList<View> buildTouchDispatchChildList() {
        return buildOrderedChildList();
    }

    private View findChildWithAccessibilityFocus() {
        View current;
        ViewRootImpl viewRoot = getViewRootImpl();
        if (viewRoot == null || (current = viewRoot.getAccessibilityFocusedHost()) == null) {
            return null;
        }
        ViewParent parent = current.getParent();
        while (parent instanceof View) {
            if (parent == this) {
                return current;
            }
            current = (View) parent;
            parent = current.getParent();
        }
        return null;
    }

    private void resetTouchState() {
        clearTouchTargets();
        resetCancelNextUpFlag(this);
        this.mGroupFlags &= -524289;
        this.mNestedScrollAxes = 0;
    }

    private static boolean resetCancelNextUpFlag(View view) {
        if ((view.mPrivateFlags & 67108864) != 0) {
            view.mPrivateFlags &= -67108865;
            return true;
        }
        return false;
    }

    private void clearTouchTargets() {
        TouchTarget target = this.mFirstTouchTarget;
        if (target != null) {
            do {
                TouchTarget next = target.next;
                target.recycle();
                target = next;
            } while (target != null);
            this.mFirstTouchTarget = null;
        }
    }

    private void cancelAndClearTouchTargets(MotionEvent event) {
        if (this.mFirstTouchTarget != null) {
            boolean syntheticEvent = false;
            if (event == null) {
                long now = SystemClock.uptimeMillis();
                event = MotionEvent.obtain(now, now, 3, 0.0f, 0.0f, 0);
                event.setSource(4098);
                syntheticEvent = true;
            }
            for (TouchTarget target = this.mFirstTouchTarget; target != null; target = target.next) {
                resetCancelNextUpFlag(target.child);
                dispatchTransformedTouchEvent(event, true, target.child, target.pointerIdBits);
            }
            clearTouchTargets();
            if (syntheticEvent) {
                event.recycle();
            }
        }
    }

    private TouchTarget getTouchTarget(View child) {
        for (TouchTarget target = this.mFirstTouchTarget; target != null; target = target.next) {
            if (target.child == child) {
                return target;
            }
        }
        return null;
    }

    private TouchTarget addTouchTarget(View child, int pointerIdBits) {
        TouchTarget target = TouchTarget.obtain(child, pointerIdBits);
        target.next = this.mFirstTouchTarget;
        this.mFirstTouchTarget = target;
        return target;
    }

    private void removePointersFromTouchTargets(int pointerIdBits) {
        TouchTarget predecessor = null;
        TouchTarget target = this.mFirstTouchTarget;
        while (target != null) {
            TouchTarget next = target.next;
            if ((target.pointerIdBits & pointerIdBits) != 0) {
                target.pointerIdBits &= ~pointerIdBits;
                if (target.pointerIdBits == 0) {
                    if (predecessor == null) {
                        this.mFirstTouchTarget = next;
                    } else {
                        predecessor.next = next;
                    }
                    target.recycle();
                    target = next;
                }
            }
            predecessor = target;
            target = next;
        }
    }

    @UnsupportedAppUsage
    private void cancelTouchTarget(View view) {
        TouchTarget predecessor = null;
        TouchTarget target = this.mFirstTouchTarget;
        while (target != null) {
            TouchTarget next = target.next;
            if (target.child == view) {
                if (predecessor == null) {
                    this.mFirstTouchTarget = next;
                } else {
                    predecessor.next = next;
                }
                target.recycle();
                long now = SystemClock.uptimeMillis();
                MotionEvent event = MotionEvent.obtain(now, now, 3, 0.0f, 0.0f, 0);
                event.setSource(4098);
                view.dispatchTouchEvent(event);
                event.recycle();
                return;
            }
            predecessor = target;
            target = next;
        }
    }

    private float[] getTempPoint() {
        if (this.mTempPoint == null) {
            this.mTempPoint = new float[2];
        }
        return this.mTempPoint;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @UnsupportedAppUsage
    public boolean isTransformedTouchPointInView(float x, float y, View child, PointF outLocalPoint) {
        float[] point = getTempPoint();
        point[0] = x;
        point[1] = y;
        transformPointToViewLocal(point, child);
        boolean isInView = child.pointInView(point[0], point[1]);
        if (isInView && outLocalPoint != null) {
            outLocalPoint.set(point[0], point[1]);
        }
        return isInView;
    }

    @UnsupportedAppUsage
    public void transformPointToViewLocal(float[] point, View child) {
        point[0] = point[0] + (this.mScrollX - child.mLeft);
        point[1] = point[1] + (this.mScrollY - child.mTop);
        if (!child.hasIdentityMatrix()) {
            child.getInverseMatrix().mapPoints(point);
        }
    }

    private boolean dispatchTransformedTouchEvent(MotionEvent event, boolean cancel, View child, int desiredPointerIdBits) {
        boolean handled;
        MotionEvent transformedEvent;
        boolean handled2;
        int oldAction = event.getAction();
        if (cancel || oldAction == 3) {
            event.setAction(3);
            if (child == null) {
                handled = super.dispatchTouchEvent(event);
            } else {
                handled = child.dispatchTouchEvent(event);
            }
            event.setAction(oldAction);
            return handled;
        }
        int oldPointerIdBits = event.getPointerIdBits();
        int newPointerIdBits = oldPointerIdBits & desiredPointerIdBits;
        if (newPointerIdBits == 0) {
            return false;
        }
        if (newPointerIdBits == oldPointerIdBits) {
            if (child == null || child.hasIdentityMatrix()) {
                if (child == null) {
                    boolean handled3 = super.dispatchTouchEvent(event);
                    return handled3;
                }
                float offsetX = this.mScrollX - child.mLeft;
                float offsetY = this.mScrollY - child.mTop;
                event.offsetLocation(offsetX, offsetY);
                boolean handled4 = child.dispatchTouchEvent(event);
                event.offsetLocation(-offsetX, -offsetY);
                return handled4;
            }
            transformedEvent = MotionEvent.obtain(event);
        } else {
            transformedEvent = event.split(newPointerIdBits);
        }
        if (child == null) {
            handled2 = super.dispatchTouchEvent(transformedEvent);
        } else {
            transformedEvent.offsetLocation(this.mScrollX - child.mLeft, this.mScrollY - child.mTop);
            if (!child.hasIdentityMatrix()) {
                transformedEvent.transform(child.getInverseMatrix());
            }
            handled2 = child.dispatchTouchEvent(transformedEvent);
        }
        transformedEvent.recycle();
        return handled2;
    }

    public void setMotionEventSplittingEnabled(boolean split) {
        if (split) {
            this.mGroupFlags |= 2097152;
        } else {
            this.mGroupFlags &= -2097153;
        }
    }

    public boolean isMotionEventSplittingEnabled() {
        return (this.mGroupFlags & 2097152) == 2097152;
    }

    public boolean isTransitionGroup() {
        int i = this.mGroupFlags;
        if ((33554432 & i) != 0) {
            return (i & 16777216) != 0;
        }
        ViewOutlineProvider outlineProvider = getOutlineProvider();
        return (getBackground() == null && getTransitionName() == null && (outlineProvider == null || outlineProvider == ViewOutlineProvider.BACKGROUND)) ? false : true;
    }

    public void setTransitionGroup(boolean isTransitionGroup) {
        this.mGroupFlags |= 33554432;
        if (isTransitionGroup) {
            this.mGroupFlags |= 16777216;
        } else {
            this.mGroupFlags &= -16777217;
        }
    }

    @Override // android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (disallowIntercept == ((this.mGroupFlags & 524288) != 0)) {
            return;
        }
        if (disallowIntercept) {
            this.mGroupFlags |= 524288;
        } else {
            this.mGroupFlags &= -524289;
        }
        if (this.mParent != null) {
            this.mParent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.isFromSource(8194) && ev.getAction() == 0 && ev.isButtonPressed(1) && isOnScrollbarThumb(ev.getX(), ev.getY())) {
            return true;
        }
        return false;
    }

    @Override // android.view.View
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        boolean result;
        int descendantFocusability = getDescendantFocusability();
        if (descendantFocusability == 131072) {
            boolean took = super.requestFocus(direction, previouslyFocusedRect);
            result = took ? took : onRequestFocusInDescendants(direction, previouslyFocusedRect);
        } else if (descendantFocusability == 262144) {
            boolean took2 = onRequestFocusInDescendants(direction, previouslyFocusedRect);
            result = took2 ? took2 : super.requestFocus(direction, previouslyFocusedRect);
        } else if (descendantFocusability == 393216) {
            boolean result2 = super.requestFocus(direction, previouslyFocusedRect);
            result = result2;
        } else {
            throw new IllegalStateException("descendant focusability must be one of FOCUS_BEFORE_DESCENDANTS, FOCUS_AFTER_DESCENDANTS, FOCUS_BLOCK_DESCENDANTS but is " + descendantFocusability);
        }
        if (result && !isLayoutValid() && (this.mPrivateFlags & 1) == 0) {
            this.mPrivateFlags |= 1;
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        int index;
        int increment;
        int end;
        int count = this.mChildrenCount;
        if ((direction & 2) != 0) {
            index = 0;
            increment = 1;
            end = count;
        } else {
            index = count - 1;
            increment = -1;
            end = -1;
        }
        View[] children = this.mChildren;
        for (int i = index; i != end; i += increment) {
            View child = children[i];
            if ((child.mViewFlags & 12) == 0 && child.requestFocus(direction, previouslyFocusedRect)) {
                return true;
            }
        }
        return false;
    }

    @Override // android.view.View
    public boolean restoreDefaultFocus() {
        if (this.mDefaultFocus != null && getDescendantFocusability() != 393216 && (this.mDefaultFocus.mViewFlags & 12) == 0 && this.mDefaultFocus.restoreDefaultFocus()) {
            return true;
        }
        return super.restoreDefaultFocus();
    }

    @Override // android.view.View
    public boolean restoreFocusInCluster(int direction) {
        if (isKeyboardNavigationCluster()) {
            boolean blockedFocus = getTouchscreenBlocksFocus();
            try {
                setTouchscreenBlocksFocusNoRefocus(false);
                return restoreFocusInClusterInternal(direction);
            } finally {
                setTouchscreenBlocksFocusNoRefocus(blockedFocus);
            }
        }
        return restoreFocusInClusterInternal(direction);
    }

    private boolean restoreFocusInClusterInternal(int direction) {
        if (this.mFocusedInCluster != null && getDescendantFocusability() != 393216 && (this.mFocusedInCluster.mViewFlags & 12) == 0 && this.mFocusedInCluster.restoreFocusInCluster(direction)) {
            return true;
        }
        return super.restoreFocusInCluster(direction);
    }

    @Override // android.view.View
    public boolean restoreFocusNotInCluster() {
        if (this.mFocusedInCluster != null) {
            return restoreFocusInCluster(130);
        }
        if (isKeyboardNavigationCluster() || (this.mViewFlags & 12) != 0) {
            return false;
        }
        int descendentFocusability = getDescendantFocusability();
        if (descendentFocusability == 393216) {
            return super.requestFocus(130, null);
        }
        if (descendentFocusability == 131072 && super.requestFocus(130, null)) {
            return true;
        }
        for (int i = 0; i < this.mChildrenCount; i++) {
            View child = this.mChildren[i];
            if (!child.isKeyboardNavigationCluster() && child.restoreFocusNotInCluster()) {
                return true;
            }
        }
        if (descendentFocusability != 262144 || hasFocusableChild(false)) {
            return false;
        }
        return super.requestFocus(130, null);
    }

    @Override // android.view.View
    public void dispatchStartTemporaryDetach() {
        super.dispatchStartTemporaryDetach();
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchStartTemporaryDetach();
        }
    }

    @Override // android.view.View
    public void dispatchFinishTemporaryDetach() {
        super.dispatchFinishTemporaryDetach();
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchFinishTemporaryDetach();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.view.View
    @UnsupportedAppUsage
    public void dispatchAttachedToWindow(View.AttachInfo info, int visibility) {
        this.mGroupFlags |= 4194304;
        super.dispatchAttachedToWindow(info, visibility);
        this.mGroupFlags &= -4194305;
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            View child = children[i];
            child.dispatchAttachedToWindow(info, combineVisibility(visibility, child.getVisibility()));
        }
        List<Integer> list = this.mTransientIndices;
        int transientCount = list == null ? 0 : list.size();
        for (int i2 = 0; i2 < transientCount; i2++) {
            View view = this.mTransientViews.get(i2);
            view.dispatchAttachedToWindow(info, combineVisibility(visibility, view.getVisibility()));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.view.View
    public void dispatchScreenStateChanged(int screenState) {
        super.dispatchScreenStateChanged(screenState);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchScreenStateChanged(screenState);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.view.View
    public void dispatchMovedToDisplay(Display display, Configuration config) {
        super.dispatchMovedToDisplay(display, config);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchMovedToDisplay(display, config);
        }
    }

    @Override // android.view.View
    public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        boolean handled;
        if (includeForAccessibility() && (handled = super.dispatchPopulateAccessibilityEventInternal(event))) {
            return handled;
        }
        ChildListForAccessibility children = ChildListForAccessibility.obtain(this, true);
        try {
            int childCount = children.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = children.getChildAt(i);
                if ((child.mViewFlags & 12) == 0) {
                    boolean handled2 = child.dispatchPopulateAccessibilityEvent(event);
                    if (handled2) {
                        return handled2;
                    }
                }
            }
            children.recycle();
            return false;
        } finally {
            children.recycle();
        }
    }

    @Override // android.view.View
    public void dispatchProvideStructure(ViewStructure structure) {
        int childrenCount;
        int childIndex;
        super.dispatchProvideStructure(structure);
        if (isAssistBlocked() || structure.getChildCount() != 0 || (childrenCount = this.mChildrenCount) <= 0) {
            return;
        }
        if (!isLaidOut()) {
            if (Helper.sVerbose) {
                Log.v("View", "dispatchProvideStructure(): not laid out, ignoring " + childrenCount + " children of " + getAccessibilityViewId());
                return;
            }
            return;
        }
        structure.setChildCount(childrenCount);
        ArrayList<View> preorderedList = buildOrderedChildList();
        boolean customOrder = preorderedList == null && isChildrenDrawingOrderEnabled();
        for (int i = 0; i < childrenCount; i++) {
            try {
                childIndex = getAndVerifyPreorderedIndex(childrenCount, i, customOrder);
            } catch (IndexOutOfBoundsException e) {
                childIndex = i;
                if (this.mContext.getApplicationInfo().targetSdkVersion < 23) {
                    Log.w(TAG, "Bad getChildDrawingOrder while collecting assist @ " + i + " of " + childrenCount, e);
                    customOrder = false;
                    if (i > 0) {
                        int[] permutation = new int[childrenCount];
                        SparseBooleanArray usedIndices = new SparseBooleanArray();
                        for (int j = 0; j < i; j++) {
                            permutation[j] = getChildDrawingOrder(childrenCount, j);
                            usedIndices.put(permutation[j], true);
                        }
                        int nextIndex = 0;
                        for (int j2 = i; j2 < childrenCount; j2++) {
                            while (usedIndices.get(nextIndex, false)) {
                                nextIndex++;
                            }
                            permutation[j2] = nextIndex;
                            nextIndex++;
                        }
                        preorderedList = new ArrayList<>(childrenCount);
                        for (int j3 = 0; j3 < childrenCount; j3++) {
                            int index = permutation[j3];
                            View child = this.mChildren[index];
                            preorderedList.add(child);
                        }
                    }
                } else {
                    throw e;
                }
            }
            View child2 = getAndVerifyPreorderedView(preorderedList, this.mChildren, childIndex);
            ViewStructure cstructure = structure.newChild(i);
            child2.dispatchProvideStructure(cstructure);
        }
        if (preorderedList != null) {
            preorderedList.clear();
        }
    }

    @Override // android.view.View
    public void dispatchProvideAutofillStructure(ViewStructure structure, int flags) {
        super.dispatchProvideAutofillStructure(structure, flags);
        if (structure.getChildCount() != 0) {
            return;
        }
        if (!isLaidOut()) {
            if (Helper.sVerbose) {
                Log.v("View", "dispatchProvideAutofillStructure(): not laid out, ignoring " + this.mChildrenCount + " children of " + getAutofillId());
                return;
            }
            return;
        }
        ChildListForAutoFillOrContentCapture children = getChildrenForAutofill(flags);
        int childrenCount = children.size();
        structure.setChildCount(childrenCount);
        for (int i = 0; i < childrenCount; i++) {
            View child = children.get(i);
            ViewStructure cstructure = structure.newChild(i);
            child.dispatchProvideAutofillStructure(cstructure, flags);
        }
        children.recycle();
    }

    @Override // android.view.View
    public void dispatchProvideContentCaptureStructure() {
        super.dispatchProvideContentCaptureStructure();
        if (isLaidOut()) {
            ChildListForAutoFillOrContentCapture children = getChildrenForContentCapture();
            int childrenCount = children.size();
            for (int i = 0; i < childrenCount; i++) {
                View child = children.get(i);
                child.dispatchProvideContentCaptureStructure();
            }
            children.recycle();
        }
    }

    private ChildListForAutoFillOrContentCapture getChildrenForAutofill(int flags) {
        ChildListForAutoFillOrContentCapture children = ChildListForAutoFillOrContentCapture.obtain();
        populateChildrenForAutofill(children, flags);
        return children;
    }

    private void populateChildrenForAutofill(ArrayList<View> list, int flags) {
        int childrenCount = this.mChildrenCount;
        if (childrenCount <= 0) {
            return;
        }
        ArrayList<View> preorderedList = buildOrderedChildList();
        boolean customOrder = preorderedList == null && isChildrenDrawingOrderEnabled();
        for (int i = 0; i < childrenCount; i++) {
            int childIndex = getAndVerifyPreorderedIndex(childrenCount, i, customOrder);
            View child = preorderedList == null ? this.mChildren[childIndex] : preorderedList.get(childIndex);
            if ((flags & 1) != 0 || child.isImportantForAutofill()) {
                list.add(child);
            } else if (child instanceof ViewGroup) {
                ((ViewGroup) child).populateChildrenForAutofill(list, flags);
            }
        }
    }

    private ChildListForAutoFillOrContentCapture getChildrenForContentCapture() {
        ChildListForAutoFillOrContentCapture children = ChildListForAutoFillOrContentCapture.obtain();
        populateChildrenForContentCapture(children);
        return children;
    }

    private void populateChildrenForContentCapture(ArrayList<View> list) {
        int childrenCount = this.mChildrenCount;
        if (childrenCount <= 0) {
            return;
        }
        ArrayList<View> preorderedList = buildOrderedChildList();
        boolean customOrder = preorderedList == null && isChildrenDrawingOrderEnabled();
        for (int i = 0; i < childrenCount; i++) {
            int childIndex = getAndVerifyPreorderedIndex(childrenCount, i, customOrder);
            View child = preorderedList == null ? this.mChildren[childIndex] : preorderedList.get(childIndex);
            if (child.isImportantForContentCapture()) {
                list.add(child);
            } else if (child instanceof ViewGroup) {
                ((ViewGroup) child).populateChildrenForContentCapture(list);
            }
        }
    }

    private static View getAndVerifyPreorderedView(ArrayList<View> preorderedList, View[] children, int childIndex) {
        if (preorderedList != null) {
            View child = preorderedList.get(childIndex);
            if (child == null) {
                throw new RuntimeException("Invalid preorderedList contained null child at index " + childIndex);
            }
            return child;
        }
        return children[childIndex];
    }

    @Override // android.view.View
    @UnsupportedAppUsage
    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        if (getAccessibilityNodeProvider() == null && this.mAttachInfo != null) {
            ArrayList<View> childrenForAccessibility = this.mAttachInfo.mTempArrayList;
            childrenForAccessibility.clear();
            addChildrenForAccessibility(childrenForAccessibility);
            int childrenForAccessibilityCount = childrenForAccessibility.size();
            for (int i = 0; i < childrenForAccessibilityCount; i++) {
                View child = childrenForAccessibility.get(i);
                info.addChildUnchecked(child);
            }
            childrenForAccessibility.clear();
        }
    }

    @Override // android.view.View
    public CharSequence getAccessibilityClassName() {
        return ViewGroup.class.getName();
    }

    @Override // android.view.ViewParent
    public void notifySubtreeAccessibilityStateChanged(View child, View source, int changeType) {
        if (getAccessibilityLiveRegion() != 0) {
            notifyViewAccessibilityStateChangedIfNeeded(1);
        } else if (this.mParent != null) {
            try {
                this.mParent.notifySubtreeAccessibilityStateChanged(this, source, changeType);
            } catch (AbstractMethodError e) {
                Log.e("View", this.mParent.getClass().getSimpleName() + " does not fully implement ViewParent", e);
            }
        }
    }

    @Override // android.view.View
    public void notifySubtreeAccessibilityStateChangedIfNeeded() {
        if (!AccessibilityManager.getInstance(this.mContext).isEnabled() || this.mAttachInfo == null) {
            return;
        }
        if (getImportantForAccessibility() != 4 && !isImportantForAccessibility() && getChildCount() > 0) {
            ViewParent a11yParent = getParentForAccessibility();
            if (a11yParent instanceof View) {
                ((View) a11yParent).notifySubtreeAccessibilityStateChangedIfNeeded();
                return;
            }
        }
        super.notifySubtreeAccessibilityStateChangedIfNeeded();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.view.View
    public void resetSubtreeAccessibilityStateChanged() {
        super.resetSubtreeAccessibilityStateChanged();
        View[] children = this.mChildren;
        int childCount = this.mChildrenCount;
        for (int i = 0; i < childCount; i++) {
            children[i].resetSubtreeAccessibilityStateChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getNumChildrenForAccessibility() {
        int numChildrenForAccessibility = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.includeForAccessibility()) {
                numChildrenForAccessibility++;
            } else if (child instanceof ViewGroup) {
                numChildrenForAccessibility += ((ViewGroup) child).getNumChildrenForAccessibility();
            }
        }
        return numChildrenForAccessibility;
    }

    @Override // android.view.ViewParent
    public boolean onNestedPrePerformAccessibilityAction(View target, int action, Bundle args) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.view.View
    @UnsupportedAppUsage
    public void dispatchDetachedFromWindow() {
        cancelAndClearTouchTargets(null);
        exitHoverTargets();
        exitTooltipHoverTargets();
        this.mLayoutCalledWhileSuppressed = false;
        this.mChildrenInterestedInDrag = null;
        this.mIsInterestedInDrag = false;
        DragEvent dragEvent = this.mCurrentDragStartEvent;
        if (dragEvent != null) {
            dragEvent.recycle();
            this.mCurrentDragStartEvent = null;
        }
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            children[i].dispatchDetachedFromWindow();
        }
        clearDisappearingChildren();
        int transientCount = this.mTransientViews != null ? this.mTransientIndices.size() : 0;
        for (int i2 = 0; i2 < transientCount; i2++) {
            View view = this.mTransientViews.get(i2);
            view.dispatchDetachedFromWindow();
        }
        super.dispatchDetachedFromWindow();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void internalSetPadding(int left, int top, int right, int bottom) {
        super.internalSetPadding(left, top, right, bottom);
        if ((this.mPaddingLeft | this.mPaddingTop | this.mPaddingRight | this.mPaddingBottom) != 0) {
            this.mGroupFlags |= 32;
        } else {
            this.mGroupFlags &= -33;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        super.dispatchSaveInstanceState(container);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            View c = children[i];
            if ((c.mViewFlags & 536870912) != 536870912) {
                c.dispatchSaveInstanceState(container);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dispatchFreezeSelfOnly(SparseArray<Parcelable> container) {
        super.dispatchSaveInstanceState(container);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        super.dispatchRestoreInstanceState(container);
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            View c = children[i];
            if ((c.mViewFlags & 536870912) != 536870912) {
                c.dispatchRestoreInstanceState(container);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dispatchThawSelfOnly(SparseArray<Parcelable> container) {
        super.dispatchRestoreInstanceState(container);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Deprecated
    public void setChildrenDrawingCacheEnabled(boolean enabled) {
        if (enabled || (this.mPersistentDrawingCache & 3) != 3) {
            View[] children = this.mChildren;
            int count = this.mChildrenCount;
            for (int i = 0; i < count; i++) {
                children[i].setDrawingCacheEnabled(enabled);
            }
        }
    }

    @Override // android.view.View
    public Bitmap createSnapshot(ViewDebug.CanvasProvider canvasProvider, boolean skipChildren) {
        int count = this.mChildrenCount;
        int[] visibilities = null;
        if (skipChildren) {
            visibilities = new int[count];
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                visibilities[i] = child.getVisibility();
                if (visibilities[i] == 0) {
                    child.mViewFlags = (child.mViewFlags & (-13)) | 4;
                }
            }
        }
        try {
            return super.createSnapshot(canvasProvider, skipChildren);
        } finally {
            if (skipChildren) {
                for (int i2 = 0; i2 < count; i2++) {
                    View child2 = getChildAt(i2);
                    child2.mViewFlags = (child2.mViewFlags & (-13)) | (visibilities[i2] & 12);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isLayoutModeOptical() {
        return this.mLayoutMode == 1;
    }

    @Override // android.view.View
    Insets computeOpticalInsets() {
        if (isLayoutModeOptical()) {
            int left = 0;
            int top = 0;
            int right = 0;
            int bottom = 0;
            for (int i = 0; i < this.mChildrenCount; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() == 0) {
                    Insets insets = child.getOpticalInsets();
                    left = Math.max(left, insets.left);
                    top = Math.max(top, insets.top);
                    right = Math.max(right, insets.right);
                    bottom = Math.max(bottom, insets.bottom);
                }
            }
            return Insets.of(left, top, right, bottom);
        }
        return Insets.NONE;
    }

    private static void fillRect(Canvas canvas, Paint paint, int x1, int y1, int x2, int y2) {
        if (x1 != x2 && y1 != y2) {
            if (x1 > x2) {
                x1 = x2;
                x2 = x1;
            }
            if (y1 > y2) {
                y1 = y2;
                y2 = y1;
            }
            canvas.drawRect(x1, y1, x2, y2, paint);
        }
    }

    private static int sign(int x) {
        return x >= 0 ? 1 : -1;
    }

    private static void drawCorner(Canvas c, Paint paint, int x1, int y1, int dx, int dy, int lw) {
        fillRect(c, paint, x1, y1, x1 + dx, y1 + (sign(dy) * lw));
        fillRect(c, paint, x1, y1, x1 + (sign(dx) * lw), y1 + dy);
    }

    private static void drawRectCorners(Canvas canvas, int x1, int y1, int x2, int y2, Paint paint, int lineLength, int lineWidth) {
        drawCorner(canvas, paint, x1, y1, lineLength, lineLength, lineWidth);
        drawCorner(canvas, paint, x1, y2, lineLength, -lineLength, lineWidth);
        drawCorner(canvas, paint, x2, y1, -lineLength, lineLength, lineWidth);
        drawCorner(canvas, paint, x2, y2, -lineLength, -lineLength, lineWidth);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void fillDifference(Canvas canvas, int x2, int y2, int x3, int y3, int dx1, int dy1, int dx2, int dy2, Paint paint) {
        int x1 = x2 - dx1;
        int y1 = y2 - dy1;
        int x4 = x3 + dx2;
        int y4 = y3 + dy2;
        fillRect(canvas, paint, x1, y1, x4, y2);
        fillRect(canvas, paint, x1, y2, x2, y3);
        fillRect(canvas, paint, x3, y2, x4, y3);
        fillRect(canvas, paint, x1, y3, x4, y4);
    }

    protected void onDebugDrawMargins(Canvas canvas, Paint paint) {
        for (int i = 0; i < getChildCount(); i++) {
            View c = getChildAt(i);
            c.getLayoutParams().onDebugDraw(c, canvas, paint);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onDebugDraw(Canvas canvas) {
        Paint paint = getDebugPaint();
        paint.setColor(-65536);
        paint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < getChildCount(); i++) {
            View c = getChildAt(i);
            if (c.getVisibility() != 8) {
                Insets insets = c.getOpticalInsets();
                drawRect(canvas, paint, insets.left + c.getLeft(), insets.top + c.getTop(), (c.getRight() - insets.right) - 1, (c.getBottom() - insets.bottom) - 1);
            }
        }
        paint.setColor(Color.argb(63, 255, 0, 255));
        paint.setStyle(Paint.Style.FILL);
        onDebugDrawMargins(canvas, paint);
        paint.setColor(DEBUG_CORNERS_COLOR);
        paint.setStyle(Paint.Style.FILL);
        int lineLength = dipsToPixels(8);
        int lineWidth = dipsToPixels(1);
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            View c2 = getChildAt(i2);
            if (c2.getVisibility() != 8) {
                drawRectCorners(canvas, c2.getLeft(), c2.getTop(), c2.getRight(), c2.getBottom(), paint, lineLength, lineWidth);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void dispatchDraw(Canvas canvas) {
        boolean usingRenderNodeProperties = canvas.isRecordingFor(this.mRenderNode);
        int childrenCount = this.mChildrenCount;
        View[] children = this.mChildren;
        int flags = this.mGroupFlags;
        if ((flags & 8) != 0 && canAnimate()) {
            boolean z = !isHardwareAccelerated();
            for (int i = 0; i < childrenCount; i++) {
                View child = children[i];
                if ((child.mViewFlags & 12) == 0) {
                    LayoutParams params = child.getLayoutParams();
                    attachLayoutAnimationParameters(child, params, i, childrenCount);
                    bindLayoutAnimation(child);
                }
            }
            LayoutAnimationController controller = this.mLayoutAnimationController;
            if (controller.willOverlap()) {
                this.mGroupFlags |= 128;
            }
            controller.start();
            this.mGroupFlags &= -9;
            this.mGroupFlags &= -17;
            Animation.AnimationListener animationListener = this.mAnimationListener;
            if (animationListener != null) {
                animationListener.onAnimationStart(controller.getAnimation());
            }
        }
        int clipSaveCount = 0;
        boolean customOrder = false;
        boolean clipToPadding = (flags & 34) == 34;
        if (clipToPadding) {
            clipSaveCount = canvas.save(2);
            canvas.clipRect(this.mScrollX + this.mPaddingLeft, this.mScrollY + this.mPaddingTop, ((this.mScrollX + this.mRight) - this.mLeft) - this.mPaddingRight, ((this.mScrollY + this.mBottom) - this.mTop) - this.mPaddingBottom);
        }
        this.mPrivateFlags &= -65;
        this.mGroupFlags &= -5;
        long drawingTime = getDrawingTime();
        if (usingRenderNodeProperties) {
            canvas.insertReorderBarrier();
        }
        List<Integer> list = this.mTransientIndices;
        int transientCount = list == null ? 0 : list.size();
        int transientIndex = transientCount != 0 ? 0 : -1;
        ArrayList<View> preorderedList = usingRenderNodeProperties ? null : buildOrderedChildList();
        if (preorderedList == null && isChildrenDrawingOrderEnabled()) {
            customOrder = true;
        }
        boolean more = false;
        int i2 = 0;
        while (i2 < childrenCount) {
            while (transientIndex >= 0 && this.mTransientIndices.get(transientIndex).intValue() == i2) {
                View transientChild = this.mTransientViews.get(transientIndex);
                int flags2 = flags;
                if ((transientChild.mViewFlags & 12) == 0 || transientChild.getAnimation() != null) {
                    more |= drawChild(canvas, transientChild, drawingTime);
                }
                transientIndex++;
                if (transientIndex >= transientCount) {
                    transientIndex = -1;
                }
                flags = flags2;
            }
            int flags3 = flags;
            int childIndex = getAndVerifyPreorderedIndex(childrenCount, i2, customOrder);
            View child2 = getAndVerifyPreorderedView(preorderedList, children, childIndex);
            int childrenCount2 = childrenCount;
            if ((child2.mViewFlags & 12) == 0 || child2.getAnimation() != null) {
                more |= drawChild(canvas, child2, drawingTime);
            }
            i2++;
            flags = flags3;
            childrenCount = childrenCount2;
        }
        while (transientIndex >= 0) {
            View transientChild2 = this.mTransientViews.get(transientIndex);
            if ((transientChild2.mViewFlags & 12) == 0 || transientChild2.getAnimation() != null) {
                more |= drawChild(canvas, transientChild2, drawingTime);
            }
            transientIndex++;
            if (transientIndex >= transientCount) {
                break;
            }
        }
        if (preorderedList != null) {
            preorderedList.clear();
        }
        if (this.mDisappearingChildren != null) {
            ArrayList<View> disappearingChildren = this.mDisappearingChildren;
            int disappearingCount = disappearingChildren.size() - 1;
            for (int i3 = disappearingCount; i3 >= 0; i3--) {
                more |= drawChild(canvas, disappearingChildren.get(i3), drawingTime);
            }
        }
        if (usingRenderNodeProperties) {
            canvas.insertInorderBarrier();
        }
        if (debugDraw()) {
            onDebugDraw(canvas);
        }
        if (clipToPadding) {
            canvas.restoreToCount(clipSaveCount);
        }
        int flags4 = this.mGroupFlags;
        if ((flags4 & 4) == 4) {
            invalidate(true);
        }
        if ((flags4 & 16) == 0 && (flags4 & 512) == 0 && this.mLayoutAnimationController.isDone() && !more) {
            this.mGroupFlags |= 512;
            Runnable end = new Runnable() { // from class: android.view.ViewGroup.2
                @Override // java.lang.Runnable
                public void run() {
                    ViewGroup.this.notifyAnimationListener();
                }
            };
            post(end);
        }
    }

    @Override // android.view.View
    public ViewGroupOverlay getOverlay() {
        if (this.mOverlay == null) {
            this.mOverlay = new ViewGroupOverlay(this.mContext, this);
        }
        return (ViewGroupOverlay) this.mOverlay;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getChildDrawingOrder(int childCount, int drawingPosition) {
        return drawingPosition;
    }

    public final int getChildDrawingOrder(int drawingPosition) {
        return getChildDrawingOrder(getChildCount(), drawingPosition);
    }

    private boolean hasChildWithZ() {
        for (int i = 0; i < this.mChildrenCount; i++) {
            if (this.mChildren[i].getZ() != 0.0f) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList<View> buildOrderedChildList() {
        int childrenCount = this.mChildrenCount;
        if (childrenCount <= 1 || !hasChildWithZ()) {
            return null;
        }
        ArrayList<View> arrayList = this.mPreSortedChildren;
        if (arrayList == null) {
            this.mPreSortedChildren = new ArrayList<>(childrenCount);
        } else {
            arrayList.clear();
            this.mPreSortedChildren.ensureCapacity(childrenCount);
        }
        boolean customOrder = isChildrenDrawingOrderEnabled();
        for (int i = 0; i < childrenCount; i++) {
            int childIndex = getAndVerifyPreorderedIndex(childrenCount, i, customOrder);
            View nextChild = this.mChildren[childIndex];
            float currentZ = nextChild.getZ();
            int insertIndex = i;
            while (insertIndex > 0 && this.mPreSortedChildren.get(insertIndex - 1).getZ() > currentZ) {
                insertIndex--;
            }
            this.mPreSortedChildren.add(insertIndex, nextChild);
        }
        return this.mPreSortedChildren;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyAnimationListener() {
        this.mGroupFlags &= -513;
        this.mGroupFlags |= 16;
        if (this.mAnimationListener != null) {
            Runnable end = new Runnable() { // from class: android.view.ViewGroup.3
                @Override // java.lang.Runnable
                public void run() {
                    ViewGroup.this.mAnimationListener.onAnimationEnd(ViewGroup.this.mLayoutAnimationController.getAnimation());
                }
            };
            post(end);
        }
        invalidate(true);
    }

    @Override // android.view.View
    @UnsupportedAppUsage
    protected void dispatchGetDisplayList() {
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            View child = children[i];
            if ((child.mViewFlags & 12) == 0 || child.getAnimation() != null) {
                recreateChildDisplayList(child);
            }
        }
        int transientCount = this.mTransientViews == null ? 0 : this.mTransientIndices.size();
        for (int i2 = 0; i2 < transientCount; i2++) {
            View child2 = this.mTransientViews.get(i2);
            if ((child2.mViewFlags & 12) == 0 || child2.getAnimation() != null) {
                recreateChildDisplayList(child2);
            }
        }
        if (this.mOverlay != null) {
            View overlayView = this.mOverlay.getOverlayView();
            recreateChildDisplayList(overlayView);
        }
        if (this.mDisappearingChildren != null) {
            ArrayList<View> disappearingChildren = this.mDisappearingChildren;
            int disappearingCount = disappearingChildren.size();
            for (int i3 = 0; i3 < disappearingCount; i3++) {
                recreateChildDisplayList(disappearingChildren.get(i3));
            }
        }
    }

    private void recreateChildDisplayList(View child) {
        child.mRecreateDisplayList = (child.mPrivateFlags & Integer.MIN_VALUE) != 0;
        child.mPrivateFlags &= Integer.MAX_VALUE;
        child.updateDisplayListIfDirty();
        child.mRecreateDisplayList = false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean drawChild(Canvas canvas, View child, long drawingTime) {
        return child.draw(canvas, this, drawingTime);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.view.View
    public void getScrollIndicatorBounds(Rect out) {
        super.getScrollIndicatorBounds(out);
        boolean clipToPadding = (this.mGroupFlags & 34) == 34;
        if (clipToPadding) {
            out.left += this.mPaddingLeft;
            out.right -= this.mPaddingRight;
            out.top += this.mPaddingTop;
            out.bottom -= this.mPaddingBottom;
        }
    }

    @ViewDebug.ExportedProperty(category = "drawing")
    public boolean getClipChildren() {
        return (this.mGroupFlags & 1) != 0;
    }

    public void setClipChildren(boolean clipChildren) {
        boolean previousValue = (this.mGroupFlags & 1) == 1;
        if (clipChildren != previousValue) {
            setBooleanFlag(1, clipChildren);
            for (int i = 0; i < this.mChildrenCount; i++) {
                View child = getChildAt(i);
                if (child.mRenderNode != null) {
                    child.mRenderNode.setClipToBounds(clipChildren);
                }
            }
            invalidate(true);
        }
    }

    public void setClipToPadding(boolean clipToPadding) {
        if (hasBooleanFlag(2) != clipToPadding) {
            setBooleanFlag(2, clipToPadding);
            invalidate(true);
        }
    }

    @ViewDebug.ExportedProperty(category = "drawing")
    public boolean getClipToPadding() {
        return hasBooleanFlag(2);
    }

    @Override // android.view.View
    public void dispatchSetSelected(boolean selected) {
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        for (int i = 0; i < count; i++) {
            children[i].setSelected(selected);
        }
    }

    @Override // android.view.View
    public void dispatchSetActivated(boolean activated) {
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        for (int i = 0; i < count; i++) {
            children[i].setActivated(activated);
        }
    }

    @Override // android.view.View
    protected void dispatchSetPressed(boolean pressed) {
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        for (int i = 0; i < count; i++) {
            View child = children[i];
            if (!pressed || (!child.isClickable() && !child.isLongClickable())) {
                child.setPressed(pressed);
            }
        }
    }

    @Override // android.view.View
    public void dispatchDrawableHotspotChanged(float x, float y) {
        int count = this.mChildrenCount;
        if (count == 0) {
            return;
        }
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            View child = children[i];
            boolean nonActionable = (child.isClickable() || child.isLongClickable()) ? false : true;
            boolean duplicatesState = (child.mViewFlags & 4194304) != 0;
            if (nonActionable || duplicatesState) {
                float[] point = getTempPoint();
                point[0] = x;
                point[1] = y;
                transformPointToViewLocal(point, child);
                child.drawableHotspotChanged(point[0], point[1]);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.view.View
    public void dispatchCancelPendingInputEvents() {
        super.dispatchCancelPendingInputEvents();
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        for (int i = 0; i < count; i++) {
            children[i].dispatchCancelPendingInputEvents();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setStaticTransformationsEnabled(boolean enabled) {
        setBooleanFlag(2048, enabled);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean getChildStaticTransformation(View child, Transformation t) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Transformation getChildTransformation() {
        if (this.mChildTransformation == null) {
            this.mChildTransformation = new Transformation();
        }
        return this.mChildTransformation;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public <T extends View> T findViewTraversal(int id) {
        T t;
        if (id == this.mID) {
            return this;
        }
        View[] where = this.mChildren;
        int len = this.mChildrenCount;
        for (int i = 0; i < len; i++) {
            View v = where[i];
            if ((v.mPrivateFlags & 8) == 0 && (t = (T) v.findViewById(id)) != null) {
                return t;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public <T extends View> T findViewWithTagTraversal(Object tag) {
        T t;
        if (tag != null && tag.equals(this.mTag)) {
            return this;
        }
        View[] where = this.mChildren;
        int len = this.mChildrenCount;
        for (int i = 0; i < len; i++) {
            View v = where[i];
            if ((v.mPrivateFlags & 8) == 0 && (t = (T) v.findViewWithTag(tag)) != null) {
                return t;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public <T extends View> T findViewByPredicateTraversal(Predicate<View> predicate, View childToSkip) {
        T t;
        if (predicate.test(this)) {
            return this;
        }
        View[] where = this.mChildren;
        int len = this.mChildrenCount;
        for (int i = 0; i < len; i++) {
            View v = where[i];
            if (v != childToSkip && (v.mPrivateFlags & 8) == 0 && (t = (T) v.findViewByPredicate(predicate)) != null) {
                return t;
            }
        }
        return null;
    }

    @UnsupportedAppUsage
    public void addTransientView(View view, int index) {
        if (index < 0 || view == null) {
            return;
        }
        if (view.mParent != null) {
            throw new IllegalStateException("The specified view already has a parent " + view.mParent);
        }
        if (this.mTransientIndices == null) {
            this.mTransientIndices = new ArrayList();
            this.mTransientViews = new ArrayList();
        }
        int oldSize = this.mTransientIndices.size();
        if (oldSize > 0) {
            int insertionIndex = 0;
            while (insertionIndex < oldSize && index >= this.mTransientIndices.get(insertionIndex).intValue()) {
                insertionIndex++;
            }
            this.mTransientIndices.add(insertionIndex, Integer.valueOf(index));
            this.mTransientViews.add(insertionIndex, view);
        } else {
            this.mTransientIndices.add(Integer.valueOf(index));
            this.mTransientViews.add(view);
        }
        view.mParent = this;
        if (this.mAttachInfo != null) {
            view.dispatchAttachedToWindow(this.mAttachInfo, this.mViewFlags & 12);
        }
        invalidate(true);
    }

    @UnsupportedAppUsage
    public void removeTransientView(View view) {
        List<View> list = this.mTransientViews;
        if (list == null) {
            return;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (view == this.mTransientViews.get(i)) {
                this.mTransientViews.remove(i);
                this.mTransientIndices.remove(i);
                view.mParent = null;
                if (view.mAttachInfo != null) {
                    view.dispatchDetachedFromWindow();
                }
                invalidate(true);
                return;
            }
        }
    }

    @UnsupportedAppUsage
    public int getTransientViewCount() {
        List<Integer> list = this.mTransientIndices;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public int getTransientViewIndex(int position) {
        List<Integer> list;
        if (position < 0 || (list = this.mTransientIndices) == null || position >= list.size()) {
            return -1;
        }
        return this.mTransientIndices.get(position).intValue();
    }

    @UnsupportedAppUsage
    public View getTransientView(int position) {
        List<View> list = this.mTransientViews;
        if (list == null || position >= list.size()) {
            return null;
        }
        return this.mTransientViews.get(position);
    }

    public void addView(View child) {
        addView(child, -1);
    }

    public void addView(View child, int index) {
        if (child == null) {
            throw new IllegalArgumentException("Cannot add a null child view to a ViewGroup");
        }
        LayoutParams params = child.getLayoutParams();
        if (params == null && (params = generateDefaultLayoutParams()) == null) {
            throw new IllegalArgumentException("generateDefaultLayoutParams() cannot return null");
        }
        addView(child, index, params);
    }

    public void addView(View child, int width, int height) {
        LayoutParams params = generateDefaultLayoutParams();
        params.width = width;
        params.height = height;
        addView(child, -1, params);
    }

    @Override // android.view.ViewManager
    public void addView(View child, LayoutParams params) {
        addView(child, -1, params);
    }

    public void addView(View child, int index, LayoutParams params) {
        if (child == null) {
            throw new IllegalArgumentException("Cannot add a null child view to a ViewGroup");
        }
        requestLayout();
        invalidate(true);
        addViewInner(child, index, params, false);
    }

    @Override // android.view.ViewManager
    public void updateViewLayout(View view, LayoutParams params) {
        if (!checkLayoutParams(params)) {
            throw new IllegalArgumentException("Invalid LayoutParams supplied to " + this);
        } else if (view.mParent != this) {
            throw new IllegalArgumentException("Given view not a child of " + this);
        } else {
            view.setLayoutParams(params);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean checkLayoutParams(LayoutParams p) {
        return p != null;
    }

    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        this.mOnHierarchyChangeListener = listener;
    }

    @UnsupportedAppUsage
    void dispatchViewAdded(View child) {
        onViewAdded(child);
        OnHierarchyChangeListener onHierarchyChangeListener = this.mOnHierarchyChangeListener;
        if (onHierarchyChangeListener != null) {
            onHierarchyChangeListener.onChildViewAdded(this, child);
        }
    }

    public void onViewAdded(View child) {
    }

    @UnsupportedAppUsage
    void dispatchViewRemoved(View child) {
        onViewRemoved(child);
        OnHierarchyChangeListener onHierarchyChangeListener = this.mOnHierarchyChangeListener;
        if (onHierarchyChangeListener != null) {
            onHierarchyChangeListener.onChildViewRemoved(this, child);
        }
    }

    public void onViewRemoved(View child) {
    }

    private void clearCachedLayoutMode() {
        if (!hasBooleanFlag(8388608)) {
            this.mLayoutMode = -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        clearCachedLayoutMode();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearCachedLayoutMode();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void destroyHardwareResources() {
        super.destroyHardwareResources();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).destroyHardwareResources();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean addViewInLayout(View child, int index, LayoutParams params) {
        return addViewInLayout(child, index, params, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean addViewInLayout(View child, int index, LayoutParams params, boolean preventRequestLayout) {
        if (child == null) {
            throw new IllegalArgumentException("Cannot add a null child view to a ViewGroup");
        }
        child.mParent = null;
        addViewInner(child, index, params, preventRequestLayout);
        child.mPrivateFlags = (child.mPrivateFlags & (-2097153)) | 32;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void cleanupLayoutState(View child) {
        child.mPrivateFlags &= -4097;
    }

    private void addViewInner(View child, int index, LayoutParams params, boolean preventRequestLayout) {
        LayoutTransition layoutTransition = this.mTransition;
        if (layoutTransition != null) {
            layoutTransition.cancel(3);
        }
        if (child.getParent() != null) {
            throw new IllegalStateException("The specified child already has a parent. You must call removeView() on the child's parent first.");
        }
        LayoutTransition layoutTransition2 = this.mTransition;
        if (layoutTransition2 != null) {
            layoutTransition2.addChild(this, child);
        }
        if (!checkLayoutParams(params)) {
            params = generateLayoutParams(params);
        }
        if (preventRequestLayout) {
            child.mLayoutParams = params;
        } else {
            child.setLayoutParams(params);
        }
        if (index < 0) {
            index = this.mChildrenCount;
        }
        addInArray(child, index);
        if (preventRequestLayout) {
            child.assignParent(this);
        } else {
            child.mParent = this;
        }
        if (child.hasUnhandledKeyListener()) {
            incrementChildUnhandledKeyListeners();
        }
        boolean childHasFocus = child.hasFocus();
        if (childHasFocus) {
            requestChildFocus(child, child.findFocus());
        }
        View.AttachInfo ai = this.mAttachInfo;
        if (ai != null && (this.mGroupFlags & 4194304) == 0) {
            boolean lastKeepOn = ai.mKeepScreenOn;
            ai.mKeepScreenOn = false;
            child.dispatchAttachedToWindow(this.mAttachInfo, this.mViewFlags & 12);
            if (ai.mKeepScreenOn) {
                needGlobalAttributesUpdate(true);
            }
            ai.mKeepScreenOn = lastKeepOn;
        }
        boolean lastKeepOn2 = child.isLayoutDirectionInherited();
        if (lastKeepOn2) {
            child.resetRtlProperties();
        }
        dispatchViewAdded(child);
        if ((child.mViewFlags & 4194304) == 4194304) {
            this.mGroupFlags |= 65536;
        }
        if (child.hasTransientState()) {
            childHasTransientStateChanged(child, true);
        }
        if (child.getVisibility() != 8) {
            notifySubtreeAccessibilityStateChangedIfNeeded();
        }
        List<Integer> list = this.mTransientIndices;
        if (list != null) {
            int transientCount = list.size();
            for (int i = 0; i < transientCount; i++) {
                int oldIndex = this.mTransientIndices.get(i).intValue();
                if (index <= oldIndex) {
                    this.mTransientIndices.set(i, Integer.valueOf(oldIndex + 1));
                }
            }
        }
        if (this.mCurrentDragStartEvent != null && child.getVisibility() == 0) {
            notifyChildOfDragStart(child);
        }
        if (child.hasDefaultFocus()) {
            setDefaultFocus(child);
        }
        touchAccessibilityNodeProviderIfNeeded(child);
    }

    private void touchAccessibilityNodeProviderIfNeeded(View child) {
        if (this.mContext.isAutofillCompatibilityEnabled()) {
            child.getAccessibilityNodeProvider();
        }
    }

    private void addInArray(View child, int index) {
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        int size = children.length;
        if (index == count) {
            if (size == count) {
                this.mChildren = new View[size + 12];
                System.arraycopy(children, 0, this.mChildren, 0, size);
                children = this.mChildren;
            }
            int i = this.mChildrenCount;
            this.mChildrenCount = i + 1;
            children[i] = child;
        } else if (index < count) {
            if (size == count) {
                this.mChildren = new View[size + 12];
                System.arraycopy(children, 0, this.mChildren, 0, index);
                System.arraycopy(children, index, this.mChildren, index + 1, count - index);
                children = this.mChildren;
            } else {
                System.arraycopy(children, index, children, index + 1, count - index);
            }
            children[index] = child;
            this.mChildrenCount++;
            int i2 = this.mLastTouchDownIndex;
            if (i2 >= index) {
                this.mLastTouchDownIndex = i2 + 1;
            }
        } else {
            throw new IndexOutOfBoundsException("index=" + index + " count=" + count);
        }
    }

    private void removeFromArray(int index) {
        View[] children = this.mChildren;
        ArrayList<View> arrayList = this.mTransitioningViews;
        if (arrayList == null || !arrayList.contains(children[index])) {
            children[index].mParent = null;
        }
        int count = this.mChildrenCount;
        if (index == count - 1) {
            int i = this.mChildrenCount - 1;
            this.mChildrenCount = i;
            children[i] = null;
        } else if (index >= 0 && index < count) {
            System.arraycopy(children, index + 1, children, index, (count - index) - 1);
            int i2 = this.mChildrenCount - 1;
            this.mChildrenCount = i2;
            children[i2] = null;
        } else {
            throw new IndexOutOfBoundsException();
        }
        int i3 = this.mLastTouchDownIndex;
        if (i3 == index) {
            this.mLastTouchDownTime = 0L;
            this.mLastTouchDownIndex = -1;
        } else if (i3 > index) {
            this.mLastTouchDownIndex = i3 - 1;
        }
    }

    private void removeFromArray(int start, int count) {
        View[] children = this.mChildren;
        int childrenCount = this.mChildrenCount;
        int start2 = Math.max(0, start);
        int end = Math.min(childrenCount, start2 + count);
        if (start2 == end) {
            return;
        }
        if (end == childrenCount) {
            for (int i = start2; i < end; i++) {
                children[i].mParent = null;
                children[i] = null;
            }
        } else {
            for (int i2 = start2; i2 < end; i2++) {
                children[i2].mParent = null;
            }
            int i3 = childrenCount - end;
            System.arraycopy(children, end, children, start2, i3);
            for (int i4 = childrenCount - (end - start2); i4 < childrenCount; i4++) {
                children[i4] = null;
            }
        }
        this.mChildrenCount -= end - start2;
    }

    private void bindLayoutAnimation(View child) {
        Animation a = this.mLayoutAnimationController.getAnimationForView(child);
        child.setAnimation(a);
    }

    protected void attachLayoutAnimationParameters(View child, LayoutParams params, int index, int count) {
        LayoutAnimationController.AnimationParameters animationParams = params.layoutAnimationParameters;
        if (animationParams == null) {
            animationParams = new LayoutAnimationController.AnimationParameters();
            params.layoutAnimationParameters = animationParams;
        }
        animationParams.count = count;
        animationParams.index = index;
    }

    @Override // android.view.ViewManager
    public void removeView(View view) {
        if (removeViewInternal(view)) {
            requestLayout();
            invalidate(true);
        }
    }

    public void removeViewInLayout(View view) {
        removeViewInternal(view);
    }

    public void removeViewsInLayout(int start, int count) {
        removeViewsInternal(start, count);
    }

    public void removeViewAt(int index) {
        removeViewInternal(index, getChildAt(index));
        requestLayout();
        invalidate(true);
    }

    public void removeViews(int start, int count) {
        removeViewsInternal(start, count);
        requestLayout();
        invalidate(true);
    }

    private boolean removeViewInternal(View view) {
        int index = indexOfChild(view);
        if (index >= 0) {
            removeViewInternal(index, view);
            return true;
        }
        return false;
    }

    private void removeViewInternal(int index, View view) {
        ArrayList<View> arrayList;
        LayoutTransition layoutTransition = this.mTransition;
        if (layoutTransition != null) {
            layoutTransition.removeChild(this, view);
        }
        boolean clearChildFocus = false;
        if (view == this.mFocused) {
            view.unFocus(null);
            clearChildFocus = true;
        }
        if (view == this.mFocusedInCluster) {
            clearFocusedInCluster(view);
        }
        view.clearAccessibilityFocus();
        cancelTouchTarget(view);
        cancelHoverTarget(view);
        if (view.getAnimation() != null || ((arrayList = this.mTransitioningViews) != null && arrayList.contains(view))) {
            addDisappearingView(view);
        } else if (view.mAttachInfo != null) {
            view.dispatchDetachedFromWindow();
        }
        if (view.hasTransientState()) {
            childHasTransientStateChanged(view, false);
        }
        needGlobalAttributesUpdate(false);
        removeFromArray(index);
        if (view.hasUnhandledKeyListener()) {
            decrementChildUnhandledKeyListeners();
        }
        if (view == this.mDefaultFocus) {
            clearDefaultFocus(view);
        }
        if (clearChildFocus) {
            clearChildFocus(view);
            if (!rootViewRequestFocus()) {
                notifyGlobalFocusCleared(this);
            }
        }
        dispatchViewRemoved(view);
        if (view.getVisibility() != 8) {
            notifySubtreeAccessibilityStateChangedIfNeeded();
        }
        List<Integer> list = this.mTransientIndices;
        int transientCount = list != null ? list.size() : 0;
        for (int i = 0; i < transientCount; i++) {
            int oldIndex = this.mTransientIndices.get(i).intValue();
            if (index < oldIndex) {
                this.mTransientIndices.set(i, Integer.valueOf(oldIndex - 1));
            }
        }
        if (this.mCurrentDragStartEvent != null) {
            this.mChildrenInterestedInDrag.remove(view);
        }
    }

    public void setLayoutTransition(LayoutTransition transition) {
        if (this.mTransition != null) {
            LayoutTransition previousTransition = this.mTransition;
            previousTransition.cancel();
            previousTransition.removeTransitionListener(this.mLayoutTransitionListener);
        }
        this.mTransition = transition;
        LayoutTransition layoutTransition = this.mTransition;
        if (layoutTransition != null) {
            layoutTransition.addTransitionListener(this.mLayoutTransitionListener);
        }
    }

    public LayoutTransition getLayoutTransition() {
        return this.mTransition;
    }

    private void removeViewsInternal(int start, int count) {
        ArrayList<View> arrayList;
        int end = start + count;
        if (start < 0 || count < 0 || end > this.mChildrenCount) {
            throw new IndexOutOfBoundsException();
        }
        View focused = this.mFocused;
        boolean detach = this.mAttachInfo != null;
        boolean clearChildFocus = false;
        View clearDefaultFocus = null;
        View[] children = this.mChildren;
        for (int i = start; i < end; i++) {
            View view = children[i];
            LayoutTransition layoutTransition = this.mTransition;
            if (layoutTransition != null) {
                layoutTransition.removeChild(this, view);
            }
            if (view == focused) {
                view.unFocus(null);
                clearChildFocus = true;
            }
            if (view == this.mDefaultFocus) {
                clearDefaultFocus = view;
            }
            if (view == this.mFocusedInCluster) {
                clearFocusedInCluster(view);
            }
            view.clearAccessibilityFocus();
            cancelTouchTarget(view);
            cancelHoverTarget(view);
            if (view.getAnimation() != null || ((arrayList = this.mTransitioningViews) != null && arrayList.contains(view))) {
                addDisappearingView(view);
            } else if (detach) {
                view.dispatchDetachedFromWindow();
            }
            if (view.hasTransientState()) {
                childHasTransientStateChanged(view, false);
            }
            needGlobalAttributesUpdate(false);
            dispatchViewRemoved(view);
        }
        removeFromArray(start, count);
        if (clearDefaultFocus != null) {
            clearDefaultFocus(clearDefaultFocus);
        }
        if (clearChildFocus) {
            clearChildFocus(focused);
            if (!rootViewRequestFocus()) {
                notifyGlobalFocusCleared(focused);
            }
        }
    }

    public void removeAllViews() {
        removeAllViewsInLayout();
        requestLayout();
        invalidate(true);
    }

    public void removeAllViewsInLayout() {
        ArrayList<View> arrayList;
        int count = this.mChildrenCount;
        if (count <= 0) {
            return;
        }
        View[] children = this.mChildren;
        this.mChildrenCount = 0;
        View focused = this.mFocused;
        boolean detach = this.mAttachInfo != null;
        boolean clearChildFocus = false;
        needGlobalAttributesUpdate(false);
        for (int i = count - 1; i >= 0; i--) {
            View view = children[i];
            LayoutTransition layoutTransition = this.mTransition;
            if (layoutTransition != null) {
                layoutTransition.removeChild(this, view);
            }
            if (view == focused) {
                view.unFocus(null);
                clearChildFocus = true;
            }
            view.clearAccessibilityFocus();
            cancelTouchTarget(view);
            cancelHoverTarget(view);
            if (view.getAnimation() != null || ((arrayList = this.mTransitioningViews) != null && arrayList.contains(view))) {
                addDisappearingView(view);
            } else if (detach) {
                view.dispatchDetachedFromWindow();
            }
            if (view.hasTransientState()) {
                childHasTransientStateChanged(view, false);
            }
            dispatchViewRemoved(view);
            view.mParent = null;
            children[i] = null;
        }
        View view2 = this.mDefaultFocus;
        if (view2 != null) {
            clearDefaultFocus(view2);
        }
        View view3 = this.mFocusedInCluster;
        if (view3 != null) {
            clearFocusedInCluster(view3);
        }
        if (clearChildFocus) {
            clearChildFocus(focused);
            if (!rootViewRequestFocus()) {
                notifyGlobalFocusCleared(focused);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void removeDetachedView(View child, boolean animate) {
        ArrayList<View> arrayList;
        LayoutTransition layoutTransition = this.mTransition;
        if (layoutTransition != null) {
            layoutTransition.removeChild(this, child);
        }
        if (child == this.mFocused) {
            child.clearFocus();
        }
        if (child == this.mDefaultFocus) {
            clearDefaultFocus(child);
        }
        if (child == this.mFocusedInCluster) {
            clearFocusedInCluster(child);
        }
        child.clearAccessibilityFocus();
        cancelTouchTarget(child);
        cancelHoverTarget(child);
        if ((animate && child.getAnimation() != null) || ((arrayList = this.mTransitioningViews) != null && arrayList.contains(child))) {
            addDisappearingView(child);
        } else if (child.mAttachInfo != null) {
            child.dispatchDetachedFromWindow();
        }
        if (child.hasTransientState()) {
            childHasTransientStateChanged(child, false);
        }
        dispatchViewRemoved(child);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void attachViewToParent(View child, int index, LayoutParams params) {
        child.mLayoutParams = params;
        if (index < 0) {
            index = this.mChildrenCount;
        }
        addInArray(child, index);
        child.mParent = this;
        child.mPrivateFlags = (child.mPrivateFlags & (-2097153) & (-32769)) | 32 | Integer.MIN_VALUE;
        this.mPrivateFlags |= Integer.MIN_VALUE;
        if (child.hasFocus()) {
            requestChildFocus(child, child.findFocus());
        }
        dispatchVisibilityAggregated(isAttachedToWindow() && getWindowVisibility() == 0 && isShown());
        notifySubtreeAccessibilityStateChangedIfNeeded();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void detachViewFromParent(View child) {
        removeFromArray(indexOfChild(child));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void detachViewFromParent(int index) {
        removeFromArray(index);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void detachViewsFromParent(int start, int count) {
        removeFromArray(start, count);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void detachAllViewsFromParent() {
        int count = this.mChildrenCount;
        if (count <= 0) {
            return;
        }
        View[] children = this.mChildren;
        this.mChildrenCount = 0;
        for (int i = count - 1; i >= 0; i--) {
            children[i].mParent = null;
            children[i] = null;
        }
    }

    @Override // android.view.ViewParent
    public void onDescendantInvalidated(View child, View target) {
        this.mPrivateFlags |= target.mPrivateFlags & 64;
        if ((target.mPrivateFlags & (-2097153)) != 0) {
            this.mPrivateFlags = (this.mPrivateFlags & (-2097153)) | 2097152;
            this.mPrivateFlags &= -32769;
        }
        if (this.mLayerType == 1) {
            this.mPrivateFlags |= -2145386496;
            target = this;
        }
        if (this.mParent != null) {
            this.mParent.onDescendantInvalidated(this, target);
        }
    }

    @Override // android.view.ViewParent
    @Deprecated
    public final void invalidateChild(View child, Rect dirty) {
        Matrix transformMatrix;
        Matrix childMatrix;
        View.AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null && attachInfo.mHardwareAccelerated) {
            onDescendantInvalidated(child, child);
            return;
        }
        ViewParent parent = this;
        if (attachInfo != null) {
            boolean z = true;
            boolean drawAnimation = (child.mPrivateFlags & 64) != 0;
            Matrix childMatrix2 = child.getMatrix();
            if (child.mLayerType != 0) {
                this.mPrivateFlags |= Integer.MIN_VALUE;
                this.mPrivateFlags &= -32769;
            }
            int[] location = attachInfo.mInvalidateChildLocation;
            location[0] = child.mLeft;
            location[1] = child.mTop;
            if (!childMatrix2.isIdentity() || (this.mGroupFlags & 2048) != 0) {
                RectF boundingRect = attachInfo.mTmpTransformRect;
                boundingRect.set(dirty);
                if ((this.mGroupFlags & 2048) != 0) {
                    Transformation t = attachInfo.mTmpTransformation;
                    boolean transformed = getChildStaticTransformation(child, t);
                    if (transformed) {
                        transformMatrix = attachInfo.mTmpMatrix;
                        transformMatrix.set(t.getMatrix());
                        if (!childMatrix2.isIdentity()) {
                            transformMatrix.preConcat(childMatrix2);
                        }
                    } else {
                        transformMatrix = childMatrix2;
                    }
                } else {
                    transformMatrix = childMatrix2;
                }
                transformMatrix.mapRect(boundingRect);
                dirty.set((int) Math.floor(boundingRect.left), (int) Math.floor(boundingRect.top), (int) Math.ceil(boundingRect.right), (int) Math.ceil(boundingRect.bottom));
            }
            while (true) {
                View view = null;
                if (parent instanceof View) {
                    view = (View) parent;
                }
                if (drawAnimation) {
                    if (view != null) {
                        view.mPrivateFlags |= 64;
                    } else if (parent instanceof ViewRootImpl) {
                        ((ViewRootImpl) parent).mIsAnimating = z;
                    }
                }
                if (view != null && (view.mPrivateFlags & 2097152) != 2097152) {
                    view.mPrivateFlags = (view.mPrivateFlags & (-2097153)) | 2097152;
                }
                parent = parent.invalidateChildInParent(location, dirty);
                if (view == null) {
                    childMatrix = childMatrix2;
                } else {
                    Matrix m = view.getMatrix();
                    if (m.isIdentity()) {
                        childMatrix = childMatrix2;
                    } else {
                        RectF boundingRect2 = attachInfo.mTmpTransformRect;
                        boundingRect2.set(dirty);
                        m.mapRect(boundingRect2);
                        childMatrix = childMatrix2;
                        dirty.set((int) Math.floor(boundingRect2.left), (int) Math.floor(boundingRect2.top), (int) Math.ceil(boundingRect2.right), (int) Math.ceil(boundingRect2.bottom));
                    }
                }
                if (parent == null) {
                    return;
                }
                childMatrix2 = childMatrix;
                z = true;
            }
        }
    }

    @Override // android.view.ViewParent
    @Deprecated
    public ViewParent invalidateChildInParent(int[] location, Rect dirty) {
        if ((this.mPrivateFlags & 32800) != 0) {
            int i = this.mGroupFlags;
            if ((i & 144) != 128) {
                dirty.offset(location[0] - this.mScrollX, location[1] - this.mScrollY);
                if ((this.mGroupFlags & 1) == 0) {
                    dirty.union(0, 0, this.mRight - this.mLeft, this.mBottom - this.mTop);
                }
                int left = this.mLeft;
                int top = this.mTop;
                if ((this.mGroupFlags & 1) == 1 && !dirty.intersect(0, 0, this.mRight - left, this.mBottom - top)) {
                    dirty.setEmpty();
                }
                location[0] = left;
                location[1] = top;
            } else {
                if ((i & 1) == 1) {
                    dirty.set(0, 0, this.mRight - this.mLeft, this.mBottom - this.mTop);
                } else {
                    dirty.union(0, 0, this.mRight - this.mLeft, this.mBottom - this.mTop);
                }
                location[0] = this.mLeft;
                location[1] = this.mTop;
                this.mPrivateFlags &= -33;
            }
            this.mPrivateFlags &= -32769;
            if (this.mLayerType != 0) {
                this.mPrivateFlags |= Integer.MIN_VALUE;
            }
            return this.mParent;
        }
        return null;
    }

    public final void offsetDescendantRectToMyCoords(View descendant, Rect rect) {
        offsetRectBetweenParentAndChild(descendant, rect, true, false);
    }

    public final void offsetRectIntoDescendantCoords(View descendant, Rect rect) {
        offsetRectBetweenParentAndChild(descendant, rect, false, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0063, code lost:
        if (r9 == false) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0065, code lost:
        r8.offset(r7.mLeft - r7.mScrollX, r7.mTop - r7.mScrollY);
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0073, code lost:
        r8.offset(r7.mScrollX - r7.mLeft, r7.mScrollY - r7.mTop);
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0080, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:?, code lost:
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void offsetRectBetweenParentAndChild(android.view.View r7, android.graphics.Rect r8, boolean r9, boolean r10) {
        /*
            r6 = this;
            if (r7 != r6) goto L3
            return
        L3:
            android.view.ViewParent r0 = r7.mParent
        L5:
            if (r0 == 0) goto L61
            boolean r1 = r0 instanceof android.view.View
            if (r1 == 0) goto L61
            if (r0 == r6) goto L61
            r1 = 0
            if (r9 == 0) goto L36
            int r2 = r7.mLeft
            int r3 = r7.mScrollX
            int r2 = r2 - r3
            int r3 = r7.mTop
            int r4 = r7.mScrollY
            int r3 = r3 - r4
            r8.offset(r2, r3)
            if (r10 == 0) goto L5b
            r2 = r0
            android.view.View r2 = (android.view.View) r2
            int r3 = r2.mRight
            int r4 = r2.mLeft
            int r3 = r3 - r4
            int r4 = r2.mBottom
            int r5 = r2.mTop
            int r4 = r4 - r5
            boolean r1 = r8.intersect(r1, r1, r3, r4)
            if (r1 != 0) goto L35
            r8.setEmpty()
        L35:
            goto L5b
        L36:
            if (r10 == 0) goto L4e
            r2 = r0
            android.view.View r2 = (android.view.View) r2
            int r3 = r2.mRight
            int r4 = r2.mLeft
            int r3 = r3 - r4
            int r4 = r2.mBottom
            int r5 = r2.mTop
            int r4 = r4 - r5
            boolean r1 = r8.intersect(r1, r1, r3, r4)
            if (r1 != 0) goto L4e
            r8.setEmpty()
        L4e:
            int r1 = r7.mScrollX
            int r2 = r7.mLeft
            int r1 = r1 - r2
            int r2 = r7.mScrollY
            int r3 = r7.mTop
            int r2 = r2 - r3
            r8.offset(r1, r2)
        L5b:
            r7 = r0
            android.view.View r7 = (android.view.View) r7
            android.view.ViewParent r0 = r7.mParent
            goto L5
        L61:
            if (r0 != r6) goto L81
            if (r9 == 0) goto L73
            int r1 = r7.mLeft
            int r2 = r7.mScrollX
            int r1 = r1 - r2
            int r2 = r7.mTop
            int r3 = r7.mScrollY
            int r2 = r2 - r3
            r8.offset(r1, r2)
            goto L80
        L73:
            int r1 = r7.mScrollX
            int r2 = r7.mLeft
            int r1 = r1 - r2
            int r2 = r7.mScrollY
            int r3 = r7.mTop
            int r2 = r2 - r3
            r8.offset(r1, r2)
        L80:
            return
        L81:
            java.lang.IllegalArgumentException r1 = new java.lang.IllegalArgumentException
            java.lang.String r2 = "parameter must be a descendant of this view"
            r1.<init>(r2)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.ViewGroup.offsetRectBetweenParentAndChild(android.view.View, android.graphics.Rect, boolean, boolean):void");
    }

    @UnsupportedAppUsage
    public void offsetChildrenTopAndBottom(int offset) {
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        boolean invalidate = false;
        for (int i = 0; i < count; i++) {
            View v = children[i];
            v.mTop += offset;
            v.mBottom += offset;
            if (v.mRenderNode != null) {
                invalidate = true;
                v.mRenderNode.offsetTopAndBottom(offset);
            }
        }
        if (invalidate) {
            invalidateViewProperty(false, false);
        }
        notifySubtreeAccessibilityStateChangedIfNeeded();
    }

    @Override // android.view.ViewParent
    public boolean getChildVisibleRect(View child, Rect r, Point offset) {
        return getChildVisibleRect(child, r, offset, false);
    }

    public boolean getChildVisibleRect(View child, Rect r, Point offset, boolean forceParentCheck) {
        RectF rect = this.mAttachInfo != null ? this.mAttachInfo.mTmpTransformRect : new RectF();
        rect.set(r);
        if (!child.hasIdentityMatrix()) {
            child.getMatrix().mapRect(rect);
        }
        int dx = child.mLeft - this.mScrollX;
        int dy = child.mTop - this.mScrollY;
        rect.offset(dx, dy);
        if (offset != null) {
            if (!child.hasIdentityMatrix()) {
                float[] position = this.mAttachInfo != null ? this.mAttachInfo.mTmpTransformLocation : new float[2];
                position[0] = offset.x;
                position[1] = offset.y;
                child.getMatrix().mapPoints(position);
                offset.x = Math.round(position[0]);
                offset.y = Math.round(position[1]);
            }
            offset.x += dx;
            offset.y += dy;
        }
        int width = this.mRight - this.mLeft;
        int height = this.mBottom - this.mTop;
        boolean rectIsVisible = true;
        if (this.mParent == null || ((this.mParent instanceof ViewGroup) && ((ViewGroup) this.mParent).getClipChildren())) {
            rectIsVisible = rect.intersect(0.0f, 0.0f, width, height);
        }
        if ((forceParentCheck || rectIsVisible) && (this.mGroupFlags & 34) == 34) {
            rectIsVisible = rect.intersect(this.mPaddingLeft, this.mPaddingTop, width - this.mPaddingRight, height - this.mPaddingBottom);
        }
        if ((forceParentCheck || rectIsVisible) && this.mClipBounds != null) {
            rectIsVisible = rect.intersect(this.mClipBounds.left, this.mClipBounds.top, this.mClipBounds.right, this.mClipBounds.bottom);
        }
        r.set((int) Math.floor(rect.left), (int) Math.floor(rect.top), (int) Math.ceil(rect.right), (int) Math.ceil(rect.bottom));
        if ((forceParentCheck || rectIsVisible) && this.mParent != null) {
            if (this.mParent instanceof ViewGroup) {
                return ((ViewGroup) this.mParent).getChildVisibleRect(this, r, offset, forceParentCheck);
            }
            return this.mParent.getChildVisibleRect(this, r, offset);
        }
        return rectIsVisible;
    }

    @Override // android.view.View
    public final void layout(int l, int t, int r, int b) {
        LayoutTransition layoutTransition;
        if (!this.mSuppressLayout && ((layoutTransition = this.mTransition) == null || !layoutTransition.isChangingLayout())) {
            LayoutTransition layoutTransition2 = this.mTransition;
            if (layoutTransition2 != null) {
                layoutTransition2.layoutChange(this);
            }
            super.layout(l, t, r, b);
            return;
        }
        this.mLayoutCalledWhileSuppressed = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean canAnimate() {
        return this.mLayoutAnimationController != null;
    }

    public void startLayoutAnimation() {
        if (this.mLayoutAnimationController != null) {
            this.mGroupFlags |= 8;
            requestLayout();
        }
    }

    public void scheduleLayoutAnimation() {
        this.mGroupFlags |= 8;
    }

    public void setLayoutAnimation(LayoutAnimationController controller) {
        this.mLayoutAnimationController = controller;
        if (this.mLayoutAnimationController != null) {
            this.mGroupFlags |= 8;
        }
    }

    public LayoutAnimationController getLayoutAnimation() {
        return this.mLayoutAnimationController;
    }

    @Deprecated
    public boolean isAnimationCacheEnabled() {
        return (this.mGroupFlags & 64) == 64;
    }

    @Deprecated
    public void setAnimationCacheEnabled(boolean enabled) {
        setBooleanFlag(64, enabled);
    }

    @Deprecated
    public boolean isAlwaysDrawnWithCacheEnabled() {
        return (this.mGroupFlags & 16384) == 16384;
    }

    @Deprecated
    public void setAlwaysDrawnWithCacheEnabled(boolean always) {
        setBooleanFlag(16384, always);
    }

    @Deprecated
    protected boolean isChildrenDrawnWithCacheEnabled() {
        return (this.mGroupFlags & 32768) == 32768;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Deprecated
    public void setChildrenDrawnWithCacheEnabled(boolean enabled) {
        setBooleanFlag(32768, enabled);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @ViewDebug.ExportedProperty(category = "drawing")
    public boolean isChildrenDrawingOrderEnabled() {
        return (this.mGroupFlags & 1024) == 1024;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setChildrenDrawingOrderEnabled(boolean enabled) {
        setBooleanFlag(1024, enabled);
    }

    private boolean hasBooleanFlag(int flag) {
        return (this.mGroupFlags & flag) == flag;
    }

    private void setBooleanFlag(int flag, boolean value) {
        if (value) {
            this.mGroupFlags |= flag;
        } else {
            this.mGroupFlags &= ~flag;
        }
    }

    @ViewDebug.ExportedProperty(category = "drawing", mapping = {@ViewDebug.IntToString(from = 0, to = "NONE"), @ViewDebug.IntToString(from = 1, to = "ANIMATION"), @ViewDebug.IntToString(from = 2, to = "SCROLLING"), @ViewDebug.IntToString(from = 3, to = "ALL")})
    @Deprecated
    public int getPersistentDrawingCache() {
        return this.mPersistentDrawingCache;
    }

    @Deprecated
    public void setPersistentDrawingCache(int drawingCacheToKeep) {
        this.mPersistentDrawingCache = drawingCacheToKeep & 3;
    }

    private void setLayoutMode(int layoutMode, boolean explicitly) {
        this.mLayoutMode = layoutMode;
        setBooleanFlag(8388608, explicitly);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.view.View
    public void invalidateInheritedLayoutMode(int layoutModeOfRoot) {
        int i = this.mLayoutMode;
        if (i == -1 || i == layoutModeOfRoot || hasBooleanFlag(8388608)) {
            return;
        }
        setLayoutMode(-1, false);
        int N = getChildCount();
        for (int i2 = 0; i2 < N; i2++) {
            getChildAt(i2).invalidateInheritedLayoutMode(layoutModeOfRoot);
        }
    }

    public int getLayoutMode() {
        if (this.mLayoutMode == -1) {
            int inheritedLayoutMode = this.mParent instanceof ViewGroup ? ((ViewGroup) this.mParent).getLayoutMode() : LAYOUT_MODE_DEFAULT;
            setLayoutMode(inheritedLayoutMode, false);
        }
        int inheritedLayoutMode2 = this.mLayoutMode;
        return inheritedLayoutMode2;
    }

    public void setLayoutMode(int layoutMode) {
        if (this.mLayoutMode != layoutMode) {
            invalidateInheritedLayoutMode(layoutMode);
            setLayoutMode(layoutMode, layoutMode != -1);
            requestLayout();
        }
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return p;
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void debug(int depth) {
        super.debug(depth);
        if (this.mFocused != null) {
            String output = debugIndent(depth);
            Log.d("View", output + "mFocused");
            this.mFocused.debug(depth + 1);
        }
        if (this.mDefaultFocus != null) {
            String output2 = debugIndent(depth);
            Log.d("View", output2 + "mDefaultFocus");
            this.mDefaultFocus.debug(depth + 1);
        }
        if (this.mFocusedInCluster != null) {
            String output3 = debugIndent(depth);
            Log.d("View", output3 + "mFocusedInCluster");
            this.mFocusedInCluster.debug(depth + 1);
        }
        if (this.mChildrenCount != 0) {
            String output4 = debugIndent(depth);
            Log.d("View", output4 + "{");
        }
        int count = this.mChildrenCount;
        for (int i = 0; i < count; i++) {
            View child = this.mChildren[i];
            child.debug(depth + 1);
        }
        int i2 = this.mChildrenCount;
        if (i2 != 0) {
            String output5 = debugIndent(depth);
            Log.d("View", output5 + "}");
        }
    }

    public int indexOfChild(View child) {
        int count = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < count; i++) {
            if (children[i] == child) {
                return i;
            }
        }
        return -1;
    }

    public int getChildCount() {
        return this.mChildrenCount;
    }

    public View getChildAt(int index) {
        if (index < 0 || index >= this.mChildrenCount) {
            return null;
        }
        return this.mChildren[index];
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
        int size = this.mChildrenCount;
        View[] children = this.mChildren;
        for (int i = 0; i < size; i++) {
            View child = children[i];
            if ((child.mViewFlags & 12) != 8) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        LayoutParams lp = child.getLayoutParams();
        int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, this.mPaddingLeft + this.mPaddingRight, lp.width);
        int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec, this.mPaddingTop + this.mPaddingBottom, lp.height);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec, this.mPaddingLeft + this.mPaddingRight + lp.leftMargin + lp.rightMargin + widthUsed, lp.width);
        int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec, this.mPaddingTop + this.mPaddingBottom + lp.topMargin + lp.bottomMargin + heightUsed, lp.height);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    public static int getChildMeasureSpec(int spec, int padding, int childDimension) {
        int specMode = View.MeasureSpec.getMode(spec);
        int specSize = View.MeasureSpec.getSize(spec);
        int size = Math.max(0, specSize - padding);
        int resultSize = 0;
        int resultMode = 0;
        if (specMode != Integer.MIN_VALUE) {
            if (specMode != 0) {
                if (specMode == 1073741824) {
                    if (childDimension >= 0) {
                        resultSize = childDimension;
                        resultMode = 1073741824;
                    } else if (childDimension == -1) {
                        resultSize = size;
                        resultMode = 1073741824;
                    } else if (childDimension == -2) {
                        resultSize = size;
                        resultMode = Integer.MIN_VALUE;
                    }
                }
            } else if (childDimension >= 0) {
                resultSize = childDimension;
                resultMode = 1073741824;
            } else if (childDimension == -1) {
                resultSize = View.sUseZeroUnspecifiedMeasureSpec ? 0 : size;
                resultMode = 0;
            } else if (childDimension == -2) {
                resultSize = View.sUseZeroUnspecifiedMeasureSpec ? 0 : size;
                resultMode = 0;
            }
        } else if (childDimension >= 0) {
            resultSize = childDimension;
            resultMode = 1073741824;
        } else if (childDimension == -1) {
            resultSize = size;
            resultMode = Integer.MIN_VALUE;
        } else if (childDimension == -2) {
            resultSize = size;
            resultMode = Integer.MIN_VALUE;
        }
        return View.MeasureSpec.makeMeasureSpec(resultSize, resultMode);
    }

    public void clearDisappearingChildren() {
        ArrayList<View> disappearingChildren = this.mDisappearingChildren;
        if (disappearingChildren != null) {
            int count = disappearingChildren.size();
            for (int i = 0; i < count; i++) {
                View view = disappearingChildren.get(i);
                if (view.mAttachInfo != null) {
                    view.dispatchDetachedFromWindow();
                }
                view.clearAnimation();
            }
            disappearingChildren.clear();
            invalidate();
        }
    }

    private void addDisappearingView(View v) {
        ArrayList<View> disappearingChildren = this.mDisappearingChildren;
        if (disappearingChildren == null) {
            ArrayList<View> arrayList = new ArrayList<>();
            this.mDisappearingChildren = arrayList;
            disappearingChildren = arrayList;
        }
        disappearingChildren.add(v);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishAnimatingView(View view, Animation animation) {
        ArrayList<View> disappearingChildren = this.mDisappearingChildren;
        if (disappearingChildren != null && disappearingChildren.contains(view)) {
            disappearingChildren.remove(view);
            if (view.mAttachInfo != null) {
                view.dispatchDetachedFromWindow();
            }
            view.clearAnimation();
            this.mGroupFlags |= 4;
        }
        if (animation != null && !animation.getFillAfter()) {
            view.clearAnimation();
        }
        if ((view.mPrivateFlags & 65536) == 65536) {
            view.onAnimationEnd();
            view.mPrivateFlags &= -65537;
            this.mGroupFlags |= 4;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isViewTransitioning(View view) {
        ArrayList<View> arrayList = this.mTransitioningViews;
        return arrayList != null && arrayList.contains(view);
    }

    public void startViewTransition(View view) {
        if (view.mParent == this) {
            if (this.mTransitioningViews == null) {
                this.mTransitioningViews = new ArrayList<>();
            }
            this.mTransitioningViews.add(view);
        }
    }

    public void endViewTransition(View view) {
        ArrayList<View> arrayList = this.mTransitioningViews;
        if (arrayList != null) {
            arrayList.remove(view);
            ArrayList<View> disappearingChildren = this.mDisappearingChildren;
            if (disappearingChildren != null && disappearingChildren.contains(view)) {
                disappearingChildren.remove(view);
                ArrayList<View> arrayList2 = this.mVisibilityChangingChildren;
                if (arrayList2 != null && arrayList2.contains(view)) {
                    this.mVisibilityChangingChildren.remove(view);
                } else {
                    if (view.mAttachInfo != null) {
                        view.dispatchDetachedFromWindow();
                    }
                    if (view.mParent != null) {
                        view.mParent = null;
                    }
                }
                invalidate();
            }
        }
    }

    public void suppressLayout(boolean suppress) {
        this.mSuppressLayout = suppress;
        if (!suppress && this.mLayoutCalledWhileSuppressed) {
            requestLayout();
            this.mLayoutCalledWhileSuppressed = false;
        }
    }

    public boolean isLayoutSuppressed() {
        return this.mSuppressLayout;
    }

    @Override // android.view.View
    public boolean gatherTransparentRegion(Region region) {
        boolean meOpaque = (this.mPrivateFlags & 512) == 0;
        if (meOpaque && region == null) {
            return true;
        }
        super.gatherTransparentRegion(region);
        int childrenCount = this.mChildrenCount;
        boolean noneOfTheChildrenAreTransparent = true;
        if (childrenCount > 0) {
            ArrayList<View> preorderedList = buildOrderedChildList();
            boolean customOrder = preorderedList == null && isChildrenDrawingOrderEnabled();
            View[] children = this.mChildren;
            for (int i = 0; i < childrenCount; i++) {
                int childIndex = getAndVerifyPreorderedIndex(childrenCount, i, customOrder);
                View child = getAndVerifyPreorderedView(preorderedList, children, childIndex);
                if (((child.mViewFlags & 12) == 0 || child.getAnimation() != null) && !child.gatherTransparentRegion(region)) {
                    noneOfTheChildrenAreTransparent = false;
                }
            }
            if (preorderedList != null) {
                preorderedList.clear();
            }
        }
        return meOpaque || noneOfTheChildrenAreTransparent;
    }

    @Override // android.view.ViewParent
    public void requestTransparentRegion(View child) {
        if (child != null) {
            child.mPrivateFlags |= 512;
            if (this.mParent != null) {
                this.mParent.requestTransparentRegion(this);
            }
        }
    }

    @Override // android.view.ViewParent
    public void subtractObscuredTouchableRegion(Region touchableRegion, View view) {
        int childrenCount = this.mChildrenCount;
        ArrayList<View> preorderedList = buildTouchDispatchChildList();
        boolean customOrder = preorderedList == null && isChildrenDrawingOrderEnabled();
        View[] children = this.mChildren;
        for (int i = childrenCount - 1; i >= 0; i--) {
            int childIndex = getAndVerifyPreorderedIndex(childrenCount, i, customOrder);
            View child = getAndVerifyPreorderedView(preorderedList, children, childIndex);
            if (child == view) {
                break;
            }
            if (child.canReceivePointerEvents()) {
                applyOpToRegionByBounds(touchableRegion, child, Region.Op.DIFFERENCE);
            }
        }
        applyOpToRegionByBounds(touchableRegion, this, Region.Op.INTERSECT);
        ViewParent parent = getParent();
        if (parent != null) {
            parent.subtractObscuredTouchableRegion(touchableRegion, this);
        }
    }

    private static void applyOpToRegionByBounds(Region region, View view, Region.Op op) {
        int[] locationInWindow = new int[2];
        view.getLocationInWindow(locationInWindow);
        int x = locationInWindow[0];
        int y = locationInWindow[1];
        region.op(x, y, x + view.getWidth(), y + view.getHeight(), op);
    }

    @Override // android.view.View
    public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
        WindowInsets insets2 = super.dispatchApplyWindowInsets(insets);
        if (View.sBrokenInsetsDispatch) {
            return brokenDispatchApplyWindowInsets(insets2);
        }
        return newDispatchApplyWindowInsets(insets2);
    }

    private WindowInsets brokenDispatchApplyWindowInsets(WindowInsets insets) {
        if (!insets.isConsumed()) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                insets = getChildAt(i).dispatchApplyWindowInsets(insets);
                if (insets.isConsumed()) {
                    break;
                }
            }
        }
        return insets;
    }

    private WindowInsets newDispatchApplyWindowInsets(WindowInsets insets) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).dispatchApplyWindowInsets(insets);
        }
        return insets;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.view.View
    public void dispatchWindowInsetsAnimationStarted(WindowInsetsAnimationListener.InsetsAnimation animation) {
        super.dispatchWindowInsetsAnimationStarted(animation);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).dispatchWindowInsetsAnimationStarted(animation);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.view.View
    public WindowInsets dispatchWindowInsetsAnimationProgress(WindowInsets insets) {
        WindowInsets insets2 = super.dispatchWindowInsetsAnimationProgress(insets);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).dispatchWindowInsetsAnimationProgress(insets2);
        }
        return insets2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.view.View
    public void dispatchWindowInsetsAnimationFinished(WindowInsetsAnimationListener.InsetsAnimation animation) {
        super.dispatchWindowInsetsAnimationFinished(animation);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).dispatchWindowInsetsAnimationFinished(animation);
        }
    }

    public Animation.AnimationListener getLayoutAnimationListener() {
        return this.mAnimationListener;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        int i = this.mGroupFlags;
        if ((65536 & i) != 0) {
            if ((i & 8192) != 0) {
                throw new IllegalStateException("addStateFromChildren cannot be enabled if a child has duplicateParentState set to true");
            }
            View[] children = this.mChildren;
            int count = this.mChildrenCount;
            for (int i2 = 0; i2 < count; i2++) {
                View child = children[i2];
                if ((child.mViewFlags & 4194304) != 0) {
                    child.refreshDrawableState();
                }
            }
        }
    }

    @Override // android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        View[] children = this.mChildren;
        int count = this.mChildrenCount;
        for (int i = 0; i < count; i++) {
            children[i].jumpDrawablesToCurrentState();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public int[] onCreateDrawableState(int extraSpace) {
        if ((this.mGroupFlags & 8192) == 0) {
            return super.onCreateDrawableState(extraSpace);
        }
        int need = 0;
        int n = getChildCount();
        for (int i = 0; i < n; i++) {
            int[] childState = getChildAt(i).getDrawableState();
            if (childState != null) {
                need += childState.length;
            }
        }
        int i2 = extraSpace + need;
        int[] state = super.onCreateDrawableState(i2);
        for (int i3 = 0; i3 < n; i3++) {
            int[] childState2 = getChildAt(i3).getDrawableState();
            if (childState2 != null) {
                state = mergeDrawableStates(state, childState2);
            }
        }
        return state;
    }

    public void setAddStatesFromChildren(boolean addsStates) {
        if (addsStates) {
            this.mGroupFlags |= 8192;
        } else {
            this.mGroupFlags &= -8193;
        }
        refreshDrawableState();
    }

    public boolean addStatesFromChildren() {
        return (this.mGroupFlags & 8192) != 0;
    }

    @Override // android.view.ViewParent
    public void childDrawableStateChanged(View child) {
        if ((this.mGroupFlags & 8192) != 0) {
            refreshDrawableState();
        }
    }

    public void setLayoutAnimationListener(Animation.AnimationListener animationListener) {
        this.mAnimationListener = animationListener;
    }

    public void requestTransitionStart(LayoutTransition transition) {
        ViewRootImpl viewAncestor = getViewRootImpl();
        if (viewAncestor != null) {
            viewAncestor.requestTransitionStart(transition);
        }
    }

    @Override // android.view.View
    public boolean resolveRtlPropertiesIfNeeded() {
        boolean result = super.resolveRtlPropertiesIfNeeded();
        if (result) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.isLayoutDirectionInherited()) {
                    child.resolveRtlPropertiesIfNeeded();
                }
            }
        }
        return result;
    }

    @Override // android.view.View
    public boolean resolveLayoutDirection() {
        boolean result = super.resolveLayoutDirection();
        if (result) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.isLayoutDirectionInherited()) {
                    child.resolveLayoutDirection();
                }
            }
        }
        return result;
    }

    @Override // android.view.View
    public boolean resolveTextDirection() {
        boolean result = super.resolveTextDirection();
        if (result) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.isTextDirectionInherited()) {
                    child.resolveTextDirection();
                }
            }
        }
        return result;
    }

    @Override // android.view.View
    public boolean resolveTextAlignment() {
        boolean result = super.resolveTextAlignment();
        if (result) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (child.isTextAlignmentInherited()) {
                    child.resolveTextAlignment();
                }
            }
        }
        return result;
    }

    @Override // android.view.View
    @UnsupportedAppUsage
    public void resolvePadding() {
        super.resolvePadding();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.isLayoutDirectionInherited() && !child.isPaddingResolved()) {
                child.resolvePadding();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void resolveDrawables() {
        super.resolveDrawables();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.isLayoutDirectionInherited() && !child.areDrawablesResolved()) {
                child.resolveDrawables();
            }
        }
    }

    @Override // android.view.View
    public void resolveLayoutParams() {
        super.resolveLayoutParams();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            child.resolveLayoutParams();
        }
    }

    @Override // android.view.View
    public void resetResolvedLayoutDirection() {
        super.resetResolvedLayoutDirection();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.isLayoutDirectionInherited()) {
                child.resetResolvedLayoutDirection();
            }
        }
    }

    @Override // android.view.View
    public void resetResolvedTextDirection() {
        super.resetResolvedTextDirection();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.isTextDirectionInherited()) {
                child.resetResolvedTextDirection();
            }
        }
    }

    @Override // android.view.View
    public void resetResolvedTextAlignment() {
        super.resetResolvedTextAlignment();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.isTextAlignmentInherited()) {
                child.resetResolvedTextAlignment();
            }
        }
    }

    @Override // android.view.View
    public void resetResolvedPadding() {
        super.resetResolvedPadding();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.isLayoutDirectionInherited()) {
                child.resetResolvedPadding();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void resetResolvedDrawables() {
        super.resetResolvedDrawables();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.isLayoutDirectionInherited()) {
                child.resetResolvedDrawables();
            }
        }
    }

    public boolean shouldDelayChildPressedState() {
        return true;
    }

    @Override // android.view.ViewParent
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return false;
    }

    @Override // android.view.ViewParent
    public void onNestedScrollAccepted(View child, View target, int axes) {
        this.mNestedScrollAxes = axes;
    }

    @Override // android.view.ViewParent
    public void onStopNestedScroll(View child) {
        stopNestedScroll();
        this.mNestedScrollAxes = 0;
    }

    @Override // android.view.ViewParent
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, null);
    }

    @Override // android.view.ViewParent
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        dispatchNestedPreScroll(dx, dy, consumed, null);
    }

    @Override // android.view.ViewParent
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override // android.view.ViewParent
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    public int getNestedScrollAxes() {
        return this.mNestedScrollAxes;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onSetLayoutParams(View child, LayoutParams layoutParams) {
        requestLayout();
    }

    @Override // android.view.View
    public void captureTransitioningViews(List<View> transitioningViews) {
        if (getVisibility() != 0) {
            return;
        }
        if (isTransitionGroup()) {
            transitioningViews.add(this);
            return;
        }
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            child.captureTransitioningViews(transitioningViews);
        }
    }

    @Override // android.view.View
    public void findNamedViews(Map<String, View> namedElements) {
        if (getVisibility() != 0 && this.mGhostView == null) {
            return;
        }
        super.findNamedViews(namedElements);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            child.findNamedViews(namedElements);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.view.View
    public boolean hasUnhandledKeyListener() {
        return this.mChildUnhandledKeyListeners > 0 || super.hasUnhandledKeyListener();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void incrementChildUnhandledKeyListeners() {
        this.mChildUnhandledKeyListeners++;
        if (this.mChildUnhandledKeyListeners == 1 && (this.mParent instanceof ViewGroup)) {
            ((ViewGroup) this.mParent).incrementChildUnhandledKeyListeners();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void decrementChildUnhandledKeyListeners() {
        this.mChildUnhandledKeyListeners--;
        if (this.mChildUnhandledKeyListeners == 0 && (this.mParent instanceof ViewGroup)) {
            ((ViewGroup) this.mParent).decrementChildUnhandledKeyListeners();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.view.View
    public View dispatchUnhandledKeyEvent(KeyEvent evt) {
        if (hasUnhandledKeyListener()) {
            ArrayList<View> orderedViews = buildOrderedChildList();
            if (orderedViews != null) {
                try {
                    for (int i = orderedViews.size() - 1; i >= 0; i--) {
                        View v = orderedViews.get(i);
                        View consumer = v.dispatchUnhandledKeyEvent(evt);
                        if (consumer != null) {
                            return consumer;
                        }
                    }
                } finally {
                    orderedViews.clear();
                }
            } else {
                for (int i2 = getChildCount() - 1; i2 >= 0; i2--) {
                    View v2 = getChildAt(i2);
                    View consumer2 = v2.dispatchUnhandledKeyEvent(evt);
                    if (consumer2 != null) {
                        return consumer2;
                    }
                }
            }
            if (onUnhandledKeyEvent(evt)) {
                return this;
            }
            return null;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class TouchTarget {
        public static final int ALL_POINTER_IDS = -1;
        private static final int MAX_RECYCLED = 32;
        private static TouchTarget sRecycleBin;
        private static final Object sRecycleLock = new Object[0];
        private static int sRecycledCount;
        @UnsupportedAppUsage
        public View child;
        public TouchTarget next;
        public int pointerIdBits;

        @UnsupportedAppUsage
        private TouchTarget() {
        }

        public static TouchTarget obtain(View child, int pointerIdBits) {
            TouchTarget target;
            if (child == null) {
                throw new IllegalArgumentException("child must be non-null");
            }
            synchronized (sRecycleLock) {
                if (sRecycleBin == null) {
                    target = new TouchTarget();
                } else {
                    target = sRecycleBin;
                    sRecycleBin = target.next;
                    sRecycledCount--;
                    target.next = null;
                }
            }
            target.child = child;
            target.pointerIdBits = pointerIdBits;
            return target;
        }

        public void recycle() {
            if (this.child == null) {
                throw new IllegalStateException("already recycled once");
            }
            synchronized (sRecycleLock) {
                if (sRecycledCount < 32) {
                    this.next = sRecycleBin;
                    sRecycleBin = this;
                    sRecycledCount++;
                } else {
                    this.next = null;
                }
                this.child = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class HoverTarget {
        private static final int MAX_RECYCLED = 32;
        private static HoverTarget sRecycleBin;
        private static final Object sRecycleLock = new Object[0];
        private static int sRecycledCount;
        public View child;
        public HoverTarget next;

        private HoverTarget() {
        }

        public static HoverTarget obtain(View child) {
            HoverTarget target;
            if (child == null) {
                throw new IllegalArgumentException("child must be non-null");
            }
            synchronized (sRecycleLock) {
                if (sRecycleBin == null) {
                    target = new HoverTarget();
                } else {
                    target = sRecycleBin;
                    sRecycleBin = target.next;
                    sRecycledCount--;
                    target.next = null;
                }
            }
            target.child = child;
            return target;
        }

        public void recycle() {
            if (this.child == null) {
                throw new IllegalStateException("already recycled once");
            }
            synchronized (sRecycleLock) {
                if (sRecycledCount < 32) {
                    this.next = sRecycleBin;
                    sRecycleBin = this;
                    sRecycledCount++;
                } else {
                    this.next = null;
                }
                this.child = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class ChildListForAutoFillOrContentCapture extends ArrayList<View> {
        private static final int MAX_POOL_SIZE = 32;
        private static final Pools.SimplePool<ChildListForAutoFillOrContentCapture> sPool = new Pools.SimplePool<>(32);

        private ChildListForAutoFillOrContentCapture() {
        }

        public static ChildListForAutoFillOrContentCapture obtain() {
            ChildListForAutoFillOrContentCapture list = sPool.acquire();
            if (list == null) {
                return new ChildListForAutoFillOrContentCapture();
            }
            return list;
        }

        public void recycle() {
            clear();
            sPool.release(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class ChildListForAccessibility {
        private static final int MAX_POOL_SIZE = 32;
        private static final Pools.SynchronizedPool<ChildListForAccessibility> sPool = new Pools.SynchronizedPool<>(32);
        private final ArrayList<View> mChildren = new ArrayList<>();
        private final ArrayList<ViewLocationHolder> mHolders = new ArrayList<>();

        ChildListForAccessibility() {
        }

        public static ChildListForAccessibility obtain(ViewGroup parent, boolean sort) {
            ChildListForAccessibility list = sPool.acquire();
            if (list == null) {
                list = new ChildListForAccessibility();
            }
            list.init(parent, sort);
            return list;
        }

        public void recycle() {
            clear();
            sPool.release(this);
        }

        public int getChildCount() {
            return this.mChildren.size();
        }

        public View getChildAt(int index) {
            return this.mChildren.get(index);
        }

        private void init(ViewGroup parent, boolean sort) {
            ArrayList<View> children = this.mChildren;
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);
                children.add(child);
            }
            if (sort) {
                ArrayList<ViewLocationHolder> holders = this.mHolders;
                for (int i2 = 0; i2 < childCount; i2++) {
                    View child2 = children.get(i2);
                    holders.add(ViewLocationHolder.obtain(parent, child2));
                }
                sort(holders);
                for (int i3 = 0; i3 < childCount; i3++) {
                    ViewLocationHolder holder = holders.get(i3);
                    children.set(i3, holder.mView);
                    holder.recycle();
                }
                holders.clear();
            }
        }

        private void sort(ArrayList<ViewLocationHolder> holders) {
            try {
                ViewLocationHolder.setComparisonStrategy(1);
                Collections.sort(holders);
            } catch (IllegalArgumentException e) {
                ViewLocationHolder.setComparisonStrategy(2);
                Collections.sort(holders);
            }
        }

        private void clear() {
            this.mChildren.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class ViewLocationHolder implements Comparable<ViewLocationHolder> {
        public static final int COMPARISON_STRATEGY_LOCATION = 2;
        public static final int COMPARISON_STRATEGY_STRIPE = 1;
        private static final int MAX_POOL_SIZE = 32;
        private int mLayoutDirection;
        private final Rect mLocation = new Rect();
        private ViewGroup mRoot;
        public View mView;
        private static final Pools.SynchronizedPool<ViewLocationHolder> sPool = new Pools.SynchronizedPool<>(32);
        private static int sComparisonStrategy = 1;

        ViewLocationHolder() {
        }

        public static ViewLocationHolder obtain(ViewGroup root, View view) {
            ViewLocationHolder holder = sPool.acquire();
            if (holder == null) {
                holder = new ViewLocationHolder();
            }
            holder.init(root, view);
            return holder;
        }

        public static void setComparisonStrategy(int strategy) {
            sComparisonStrategy = strategy;
        }

        public void recycle() {
            clear();
            sPool.release(this);
        }

        @Override // java.lang.Comparable
        public int compareTo(ViewLocationHolder another) {
            if (another == null) {
                return 1;
            }
            int boundsResult = compareBoundsOfTree(this, another);
            if (boundsResult != 0) {
                return boundsResult;
            }
            return this.mView.getAccessibilityViewId() - another.mView.getAccessibilityViewId();
        }

        private static int compareBoundsOfTree(ViewLocationHolder holder1, ViewLocationHolder holder2) {
            if (sComparisonStrategy == 1) {
                if (holder1.mLocation.bottom - holder2.mLocation.top <= 0) {
                    return -1;
                }
                if (holder1.mLocation.top - holder2.mLocation.bottom >= 0) {
                    return 1;
                }
            }
            if (holder1.mLayoutDirection == 0) {
                int leftDifference = holder1.mLocation.left - holder2.mLocation.left;
                if (leftDifference != 0) {
                    return leftDifference;
                }
            } else {
                int rightDifference = holder1.mLocation.right - holder2.mLocation.right;
                if (rightDifference != 0) {
                    return -rightDifference;
                }
            }
            int topDifference = holder1.mLocation.top - holder2.mLocation.top;
            if (topDifference != 0) {
                return topDifference;
            }
            int heightDiference = holder1.mLocation.height() - holder2.mLocation.height();
            if (heightDiference != 0) {
                return -heightDiference;
            }
            int widthDifference = holder1.mLocation.width() - holder2.mLocation.width();
            if (widthDifference != 0) {
                return -widthDifference;
            }
            final Rect view1Bounds = new Rect();
            final Rect view2Bounds = new Rect();
            final Rect tempRect = new Rect();
            holder1.mView.getBoundsOnScreen(view1Bounds, true);
            holder2.mView.getBoundsOnScreen(view2Bounds, true);
            View child1 = holder1.mView.findViewByPredicateTraversal(new Predicate() { // from class: android.view.-$$Lambda$ViewGroup$ViewLocationHolder$QbO7cM0ULKe25a7bfXG3VH6DB0c
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return ViewGroup.ViewLocationHolder.lambda$compareBoundsOfTree$0(Rect.this, view1Bounds, (View) obj);
                }
            }, null);
            View child2 = holder2.mView.findViewByPredicateTraversal(new Predicate() { // from class: android.view.-$$Lambda$ViewGroup$ViewLocationHolder$AjKvqdj7SGGIzA5qrlZUuu71jl8
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return ViewGroup.ViewLocationHolder.lambda$compareBoundsOfTree$1(Rect.this, view2Bounds, (View) obj);
                }
            }, null);
            if (child1 != null && child2 != null) {
                ViewLocationHolder childHolder1 = obtain(holder1.mRoot, child1);
                ViewLocationHolder childHolder2 = obtain(holder1.mRoot, child2);
                return compareBoundsOfTree(childHolder1, childHolder2);
            } else if (child1 != null) {
                return 1;
            } else {
                return child2 != null ? -1 : 0;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ boolean lambda$compareBoundsOfTree$0(Rect tempRect, Rect view1Bounds, View view) {
            view.getBoundsOnScreen(tempRect, true);
            return true ^ tempRect.equals(view1Bounds);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static /* synthetic */ boolean lambda$compareBoundsOfTree$1(Rect tempRect, Rect view2Bounds, View view) {
            view.getBoundsOnScreen(tempRect, true);
            return true ^ tempRect.equals(view2Bounds);
        }

        private void init(ViewGroup root, View view) {
            Rect viewLocation = this.mLocation;
            view.getDrawingRect(viewLocation);
            root.offsetDescendantRectToMyCoords(view, viewLocation);
            this.mView = view;
            this.mRoot = root;
            this.mLayoutDirection = root.getLayoutDirection();
        }

        private void clear() {
            this.mView = null;
            this.mRoot = null;
            this.mLocation.set(0, 0, 0, 0);
        }
    }

    private static void drawRect(Canvas canvas, Paint paint, int x1, int y1, int x2, int y2) {
        if (sDebugLines == null) {
            sDebugLines = new float[16];
        }
        float[] fArr = sDebugLines;
        fArr[0] = x1;
        fArr[1] = y1;
        fArr[2] = x2;
        fArr[3] = y1;
        fArr[4] = x2;
        fArr[5] = y1;
        fArr[6] = x2;
        fArr[7] = y2;
        fArr[8] = x2;
        fArr[9] = y2;
        fArr[10] = x1;
        fArr[11] = y2;
        fArr[12] = x1;
        fArr[13] = y2;
        fArr[14] = x1;
        fArr[15] = y1;
        canvas.drawLines(fArr, paint);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    @UnsupportedAppUsage
    public void encodeProperties(ViewHierarchyEncoder encoder) {
        super.encodeProperties(encoder);
        encoder.addProperty("focus:descendantFocusability", getDescendantFocusability());
        encoder.addProperty("drawing:clipChildren", getClipChildren());
        encoder.addProperty("drawing:clipToPadding", getClipToPadding());
        encoder.addProperty("drawing:childrenDrawingOrderEnabled", isChildrenDrawingOrderEnabled());
        encoder.addProperty("drawing:persistentDrawingCache", getPersistentDrawingCache());
        int n = getChildCount();
        encoder.addProperty("meta:__childCount__", (short) n);
        for (int i = 0; i < n; i++) {
            encoder.addPropertyKey("meta:__child__" + i);
            getChildAt(i).encode(encoder);
        }
    }
}
