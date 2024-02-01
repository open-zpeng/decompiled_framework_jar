package android.widget;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.media.TtmlUtils;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Pools;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.ViewHierarchyEncoder;
import android.view.accessibility.AccessibilityEvent;
import android.widget.RemoteViews;
import com.android.internal.R;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
@RemoteViews.RemoteView
/* loaded from: classes3.dex */
public class RelativeLayout extends ViewGroup {
    public static final int ABOVE = 2;
    public static final int ALIGN_BASELINE = 4;
    public static final int ALIGN_BOTTOM = 8;
    public static final int ALIGN_END = 19;
    public static final int ALIGN_LEFT = 5;
    public static final int ALIGN_PARENT_BOTTOM = 12;
    public static final int ALIGN_PARENT_END = 21;
    public static final int ALIGN_PARENT_LEFT = 9;
    public static final int ALIGN_PARENT_RIGHT = 11;
    public static final int ALIGN_PARENT_START = 20;
    public static final int ALIGN_PARENT_TOP = 10;
    public static final int ALIGN_RIGHT = 7;
    public static final int ALIGN_START = 18;
    public static final int ALIGN_TOP = 6;
    public static final int BELOW = 3;
    public static final int CENTER_HORIZONTAL = 14;
    public static final int CENTER_IN_PARENT = 13;
    public static final int CENTER_VERTICAL = 15;
    private static final int DEFAULT_WIDTH = 65536;
    public static final int END_OF = 17;
    public static final int LEFT_OF = 0;
    public static final int RIGHT_OF = 1;
    public static final int START_OF = 16;
    public static final int TRUE = -1;
    private static final int VALUE_NOT_SET = Integer.MIN_VALUE;
    private static final int VERB_COUNT = 22;
    private boolean mAllowBrokenMeasureSpecs;
    private View mBaselineView;
    private final Rect mContentBounds;
    private boolean mDirtyHierarchy;
    private final DependencyGraph mGraph;
    public protected int mGravity;
    private int mIgnoreGravity;
    private boolean mMeasureVerticalWithPaddingMargin;
    private final Rect mSelfBounds;
    private View[] mSortedHorizontalChildren;
    private View[] mSortedVerticalChildren;
    private SortedSet<View> mTopToBottomLeftToRightSet;
    private static final int[] RULES_VERTICAL = {2, 3, 4, 6, 8};
    private static final int[] RULES_HORIZONTAL = {0, 1, 5, 7, 16, 17, 18, 19};

    public RelativeLayout(Context context) {
        this(context, null);
    }

    public RelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mBaselineView = null;
        this.mGravity = 8388659;
        this.mContentBounds = new Rect();
        this.mSelfBounds = new Rect();
        this.mTopToBottomLeftToRightSet = null;
        this.mGraph = new DependencyGraph();
        this.mAllowBrokenMeasureSpecs = false;
        this.mMeasureVerticalWithPaddingMargin = false;
        initFromAttributes(context, attrs, defStyleAttr, defStyleRes);
        queryCompatibilityModes(context);
    }

    private synchronized void initFromAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RelativeLayout, defStyleAttr, defStyleRes);
        this.mIgnoreGravity = a.getResourceId(1, -1);
        this.mGravity = a.getInt(0, this.mGravity);
        a.recycle();
    }

    private synchronized void queryCompatibilityModes(Context context) {
        int version = context.getApplicationInfo().targetSdkVersion;
        this.mAllowBrokenMeasureSpecs = version <= 17;
        this.mMeasureVerticalWithPaddingMargin = version >= 18;
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @RemotableViewMethod
    public void setIgnoreGravity(int viewId) {
        this.mIgnoreGravity = viewId;
    }

    public int getGravity() {
        return this.mGravity;
    }

    @RemotableViewMethod
    public void setGravity(int gravity) {
        if (this.mGravity != gravity) {
            if ((8388615 & gravity) == 0) {
                gravity |= Gravity.START;
            }
            if ((gravity & 112) == 0) {
                gravity |= 48;
            }
            this.mGravity = gravity;
            requestLayout();
        }
    }

    @RemotableViewMethod
    public void setHorizontalGravity(int horizontalGravity) {
        int gravity = horizontalGravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        if ((8388615 & this.mGravity) != gravity) {
            this.mGravity = (this.mGravity & (-8388616)) | gravity;
            requestLayout();
        }
    }

    @RemotableViewMethod
    public void setVerticalGravity(int verticalGravity) {
        int gravity = verticalGravity & 112;
        if ((this.mGravity & 112) != gravity) {
            this.mGravity = (this.mGravity & PackageManager.INSTALL_FAILED_NO_MATCHING_ABIS) | gravity;
            requestLayout();
        }
    }

    @Override // android.view.View
    public int getBaseline() {
        return this.mBaselineView != null ? this.mBaselineView.getBaseline() : super.getBaseline();
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        super.requestLayout();
        this.mDirtyHierarchy = true;
    }

    private synchronized void sortChildren() {
        int count = getChildCount();
        if (this.mSortedVerticalChildren == null || this.mSortedVerticalChildren.length != count) {
            this.mSortedVerticalChildren = new View[count];
        }
        if (this.mSortedHorizontalChildren == null || this.mSortedHorizontalChildren.length != count) {
            this.mSortedHorizontalChildren = new View[count];
        }
        DependencyGraph graph = this.mGraph;
        graph.clear();
        for (int i = 0; i < count; i++) {
            graph.add(getChildAt(i));
        }
        graph.getSortedViews(this.mSortedVerticalChildren, RULES_VERTICAL);
        graph.getSortedViews(this.mSortedHorizontalChildren, RULES_HORIZONTAL);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:172:0x03a9  */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void onMeasure(int r58, int r59) {
        /*
            Method dump skipped, instructions count: 974
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.RelativeLayout.onMeasure(int, int):void");
    }

    private synchronized int compareLayoutPosition(LayoutParams p1, LayoutParams p2) {
        int topDiff = p1.mTop - p2.mTop;
        if (topDiff != 0) {
            return topDiff;
        }
        return p1.mLeft - p2.mLeft;
    }

    private synchronized void measureChild(View child, LayoutParams params, int myWidth, int myHeight) {
        int childWidthMeasureSpec = getChildMeasureSpec(params.mLeft, params.mRight, params.width, params.leftMargin, params.rightMargin, this.mPaddingLeft, this.mPaddingRight, myWidth);
        int childHeightMeasureSpec = getChildMeasureSpec(params.mTop, params.mBottom, params.height, params.topMargin, params.bottomMargin, this.mPaddingTop, this.mPaddingBottom, myHeight);
        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    private synchronized void measureChildHorizontal(View child, LayoutParams params, int myWidth, int myHeight) {
        int maxHeight;
        int heightMode;
        int maxHeight2;
        int childWidthMeasureSpec = getChildMeasureSpec(params.mLeft, params.mRight, params.width, params.leftMargin, params.rightMargin, this.mPaddingLeft, this.mPaddingRight, myWidth);
        if (myHeight < 0 && !this.mAllowBrokenMeasureSpecs) {
            if (params.height >= 0) {
                maxHeight2 = View.MeasureSpec.makeMeasureSpec(params.height, 1073741824);
            } else {
                maxHeight2 = View.MeasureSpec.makeMeasureSpec(0, 0);
            }
        } else {
            if (this.mMeasureVerticalWithPaddingMargin) {
                maxHeight = Math.max(0, (((myHeight - this.mPaddingTop) - this.mPaddingBottom) - params.topMargin) - params.bottomMargin);
            } else {
                maxHeight = Math.max(0, myHeight);
            }
            if (params.height == -1) {
                heightMode = 1073741824;
            } else {
                heightMode = Integer.MIN_VALUE;
            }
            maxHeight2 = View.MeasureSpec.makeMeasureSpec(maxHeight, heightMode);
        }
        child.measure(childWidthMeasureSpec, maxHeight2);
    }

    private synchronized int getChildMeasureSpec(int childStart, int childEnd, int childSize, int startMargin, int endMargin, int startPadding, int endPadding, int mySize) {
        int childSpecSize;
        int childSpecMode;
        int childSpecMode2 = 0;
        int childSpecSize2 = 0;
        boolean isUnspecified = mySize < 0;
        if (isUnspecified && !this.mAllowBrokenMeasureSpecs) {
            if (childStart != Integer.MIN_VALUE && childEnd != Integer.MIN_VALUE) {
                childSpecSize = Math.max(0, childEnd - childStart);
                childSpecMode = 1073741824;
            } else if (childSize >= 0) {
                childSpecSize = childSize;
                childSpecMode = 1073741824;
            } else {
                childSpecSize = 0;
                childSpecMode = 0;
            }
            return View.MeasureSpec.makeMeasureSpec(childSpecSize, childSpecMode);
        }
        int tempStart = childStart;
        int tempEnd = childEnd;
        if (tempStart == Integer.MIN_VALUE) {
            tempStart = startPadding + startMargin;
        }
        if (tempEnd == Integer.MIN_VALUE) {
            tempEnd = (mySize - endPadding) - endMargin;
        }
        int maxAvailable = tempEnd - tempStart;
        int i = 1073741824;
        if (childStart != Integer.MIN_VALUE && childEnd != Integer.MIN_VALUE) {
            if (isUnspecified) {
                i = 0;
            }
            childSpecMode2 = i;
            childSpecSize2 = Math.max(0, maxAvailable);
        } else if (childSize >= 0) {
            childSpecMode2 = 1073741824;
            if (maxAvailable >= 0) {
                childSpecSize2 = Math.min(maxAvailable, childSize);
            } else {
                childSpecSize2 = childSize;
            }
        } else if (childSize == -1) {
            if (isUnspecified) {
                i = 0;
            }
            childSpecMode2 = i;
            childSpecSize2 = Math.max(0, maxAvailable);
        } else if (childSize == -2) {
            if (maxAvailable >= 0) {
                childSpecMode2 = Integer.MIN_VALUE;
                childSpecSize2 = maxAvailable;
            } else {
                childSpecMode2 = 0;
                childSpecSize2 = 0;
            }
        }
        return View.MeasureSpec.makeMeasureSpec(childSpecSize2, childSpecMode2);
    }

    private synchronized boolean positionChildHorizontal(View child, LayoutParams params, int myWidth, boolean wrapContent) {
        int layoutDirection = getLayoutDirection();
        int[] rules = params.getRules(layoutDirection);
        if (params.mLeft != Integer.MIN_VALUE || params.mRight == Integer.MIN_VALUE) {
            if (params.mLeft == Integer.MIN_VALUE || params.mRight != Integer.MIN_VALUE) {
                if (params.mLeft == Integer.MIN_VALUE && params.mRight == Integer.MIN_VALUE) {
                    if (rules[13] != 0 || rules[14] != 0) {
                        if (!wrapContent) {
                            centerHorizontal(child, params, myWidth);
                        } else {
                            positionAtEdge(child, params, myWidth);
                        }
                        return true;
                    }
                    positionAtEdge(child, params, myWidth);
                }
            } else {
                params.mRight = params.mLeft + child.getMeasuredWidth();
            }
        } else {
            params.mLeft = params.mRight - child.getMeasuredWidth();
        }
        return rules[21] != 0;
    }

    private synchronized void positionAtEdge(View child, LayoutParams params, int myWidth) {
        if (isLayoutRtl()) {
            params.mRight = (myWidth - this.mPaddingRight) - params.rightMargin;
            params.mLeft = params.mRight - child.getMeasuredWidth();
            return;
        }
        params.mLeft = this.mPaddingLeft + params.leftMargin;
        params.mRight = params.mLeft + child.getMeasuredWidth();
    }

    private synchronized boolean positionChildVertical(View child, LayoutParams params, int myHeight, boolean wrapContent) {
        int[] rules = params.getRules();
        if (params.mTop != Integer.MIN_VALUE || params.mBottom == Integer.MIN_VALUE) {
            if (params.mTop == Integer.MIN_VALUE || params.mBottom != Integer.MIN_VALUE) {
                if (params.mTop == Integer.MIN_VALUE && params.mBottom == Integer.MIN_VALUE) {
                    if (rules[13] != 0 || rules[15] != 0) {
                        if (!wrapContent) {
                            centerVertical(child, params, myHeight);
                        } else {
                            params.mTop = this.mPaddingTop + params.topMargin;
                            params.mBottom = params.mTop + child.getMeasuredHeight();
                        }
                        return true;
                    }
                    params.mTop = this.mPaddingTop + params.topMargin;
                    params.mBottom = params.mTop + child.getMeasuredHeight();
                }
            } else {
                params.mBottom = params.mTop + child.getMeasuredHeight();
            }
        } else {
            params.mTop = params.mBottom - child.getMeasuredHeight();
        }
        return rules[12] != 0;
    }

    private synchronized void applyHorizontalSizeRules(LayoutParams childParams, int myWidth, int[] rules) {
        childParams.mLeft = Integer.MIN_VALUE;
        childParams.mRight = Integer.MIN_VALUE;
        LayoutParams anchorParams = getRelatedViewParams(rules, 0);
        if (anchorParams == null) {
            if (childParams.alignWithParent && rules[0] != 0 && myWidth >= 0) {
                childParams.mRight = (myWidth - this.mPaddingRight) - childParams.rightMargin;
            }
        } else {
            childParams.mRight = anchorParams.mLeft - (anchorParams.leftMargin + childParams.rightMargin);
        }
        LayoutParams anchorParams2 = getRelatedViewParams(rules, 1);
        if (anchorParams2 == null) {
            if (childParams.alignWithParent && rules[1] != 0) {
                childParams.mLeft = this.mPaddingLeft + childParams.leftMargin;
            }
        } else {
            childParams.mLeft = anchorParams2.mRight + anchorParams2.rightMargin + childParams.leftMargin;
        }
        LayoutParams anchorParams3 = getRelatedViewParams(rules, 5);
        if (anchorParams3 == null) {
            if (childParams.alignWithParent && rules[5] != 0) {
                childParams.mLeft = this.mPaddingLeft + childParams.leftMargin;
            }
        } else {
            childParams.mLeft = anchorParams3.mLeft + childParams.leftMargin;
        }
        LayoutParams anchorParams4 = getRelatedViewParams(rules, 7);
        if (anchorParams4 == null) {
            if (childParams.alignWithParent && rules[7] != 0 && myWidth >= 0) {
                childParams.mRight = (myWidth - this.mPaddingRight) - childParams.rightMargin;
            }
        } else {
            childParams.mRight = anchorParams4.mRight - childParams.rightMargin;
        }
        if (rules[9] != 0) {
            childParams.mLeft = this.mPaddingLeft + childParams.leftMargin;
        }
        if (rules[11] == 0 || myWidth < 0) {
            return;
        }
        childParams.mRight = (myWidth - this.mPaddingRight) - childParams.rightMargin;
    }

    private synchronized void applyVerticalSizeRules(LayoutParams childParams, int myHeight, int myBaseline) {
        int[] rules = childParams.getRules();
        int baselineOffset = getRelatedViewBaselineOffset(rules);
        if (baselineOffset != -1) {
            if (myBaseline != -1) {
                baselineOffset -= myBaseline;
            }
            childParams.mTop = baselineOffset;
            childParams.mBottom = Integer.MIN_VALUE;
            return;
        }
        childParams.mTop = Integer.MIN_VALUE;
        childParams.mBottom = Integer.MIN_VALUE;
        LayoutParams anchorParams = getRelatedViewParams(rules, 2);
        if (anchorParams == null) {
            if (childParams.alignWithParent && rules[2] != 0 && myHeight >= 0) {
                childParams.mBottom = (myHeight - this.mPaddingBottom) - childParams.bottomMargin;
            }
        } else {
            childParams.mBottom = anchorParams.mTop - (anchorParams.topMargin + childParams.bottomMargin);
        }
        LayoutParams anchorParams2 = getRelatedViewParams(rules, 3);
        if (anchorParams2 == null) {
            if (childParams.alignWithParent && rules[3] != 0) {
                childParams.mTop = this.mPaddingTop + childParams.topMargin;
            }
        } else {
            childParams.mTop = anchorParams2.mBottom + anchorParams2.bottomMargin + childParams.topMargin;
        }
        LayoutParams anchorParams3 = getRelatedViewParams(rules, 6);
        if (anchorParams3 == null) {
            if (childParams.alignWithParent && rules[6] != 0) {
                childParams.mTop = this.mPaddingTop + childParams.topMargin;
            }
        } else {
            childParams.mTop = anchorParams3.mTop + childParams.topMargin;
        }
        LayoutParams anchorParams4 = getRelatedViewParams(rules, 8);
        if (anchorParams4 == null) {
            if (childParams.alignWithParent && rules[8] != 0 && myHeight >= 0) {
                childParams.mBottom = (myHeight - this.mPaddingBottom) - childParams.bottomMargin;
            }
        } else {
            childParams.mBottom = anchorParams4.mBottom - childParams.bottomMargin;
        }
        if (rules[10] != 0) {
            childParams.mTop = this.mPaddingTop + childParams.topMargin;
        }
        if (rules[12] == 0 || myHeight < 0) {
            return;
        }
        childParams.mBottom = (myHeight - this.mPaddingBottom) - childParams.bottomMargin;
    }

    private synchronized View getRelatedView(int[] rules, int relation) {
        DependencyGraph.Node node;
        int id = rules[relation];
        if (id == 0 || (node = (DependencyGraph.Node) this.mGraph.mKeyNodes.get(id)) == null) {
            return null;
        }
        View v = node.view;
        while (v.getVisibility() == 8) {
            int[] rules2 = ((LayoutParams) v.getLayoutParams()).getRules(v.getLayoutDirection());
            DependencyGraph.Node node2 = (DependencyGraph.Node) this.mGraph.mKeyNodes.get(rules2[relation]);
            if (node2 == null || v == node2.view) {
                return null;
            }
            v = node2.view;
        }
        return v;
    }

    private synchronized LayoutParams getRelatedViewParams(int[] rules, int relation) {
        View v = getRelatedView(rules, relation);
        if (v != null) {
            ViewGroup.LayoutParams params = v.getLayoutParams();
            if (params instanceof LayoutParams) {
                return (LayoutParams) v.getLayoutParams();
            }
            return null;
        }
        return null;
    }

    private synchronized int getRelatedViewBaselineOffset(int[] rules) {
        int baseline;
        View v = getRelatedView(rules, 4);
        if (v != null && (baseline = v.getBaseline()) != -1) {
            ViewGroup.LayoutParams params = v.getLayoutParams();
            if (params instanceof LayoutParams) {
                LayoutParams anchorParams = (LayoutParams) v.getLayoutParams();
                return anchorParams.mTop + baseline;
            }
        }
        return -1;
    }

    private static synchronized void centerHorizontal(View child, LayoutParams params, int myWidth) {
        int childWidth = child.getMeasuredWidth();
        int left = (myWidth - childWidth) / 2;
        params.mLeft = left;
        params.mRight = left + childWidth;
    }

    private static synchronized void centerVertical(View child, LayoutParams params, int myHeight) {
        int childHeight = child.getMeasuredHeight();
        int top = (myHeight - childHeight) / 2;
        params.mTop = top;
        params.mBottom = top + childHeight;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams st = (LayoutParams) child.getLayoutParams();
                child.layout(st.mLeft, st.mTop, st.mRight, st.mBottom);
            }
        }
    }

    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (sPreserveMarginParamsInLayoutParamConversion) {
            if (lp instanceof LayoutParams) {
                return new LayoutParams((LayoutParams) lp);
            }
            if (lp instanceof ViewGroup.MarginLayoutParams) {
                return new LayoutParams((ViewGroup.MarginLayoutParams) lp);
            }
        }
        return new LayoutParams(lp);
    }

    @Override // android.view.ViewGroup, android.view.View
    public synchronized boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        if (this.mTopToBottomLeftToRightSet == null) {
            this.mTopToBottomLeftToRightSet = new TreeSet(new TopToBottomLeftToRightComparator());
        }
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            this.mTopToBottomLeftToRightSet.add(getChildAt(i));
        }
        for (View view : this.mTopToBottomLeftToRightSet) {
            if (view.getVisibility() == 0 && view.dispatchPopulateAccessibilityEvent(event)) {
                this.mTopToBottomLeftToRightSet.clear();
                return true;
            }
        }
        this.mTopToBottomLeftToRightSet.clear();
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public CharSequence getAccessibilityClassName() {
        return RelativeLayout.class.getName();
    }

    /* loaded from: classes3.dex */
    private class TopToBottomLeftToRightComparator implements Comparator<View> {
        private TopToBottomLeftToRightComparator() {
        }

        @Override // java.util.Comparator
        public synchronized int compare(View first, View second) {
            int topDifference = first.getTop() - second.getTop();
            if (topDifference != 0) {
                return topDifference;
            }
            int leftDifference = first.getLeft() - second.getLeft();
            if (leftDifference != 0) {
                return leftDifference;
            }
            int heightDiference = first.getHeight() - second.getHeight();
            if (heightDiference != 0) {
                return heightDiference;
            }
            int widthDiference = first.getWidth() - second.getWidth();
            if (widthDiference != 0) {
                return widthDiference;
            }
            return 0;
        }
    }

    /* loaded from: classes3.dex */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        @ViewDebug.ExportedProperty(category = TtmlUtils.TAG_LAYOUT)
        public boolean alignWithParent;
        public protected int mBottom;
        private int[] mInitialRules;
        private boolean mIsRtlCompatibilityMode;
        public protected int mLeft;
        private boolean mNeedsLayoutResolution;
        public protected int mRight;
        @ViewDebug.ExportedProperty(category = TtmlUtils.TAG_LAYOUT, indexMapping = {@ViewDebug.IntToString(from = 2, to = "above"), @ViewDebug.IntToString(from = 4, to = "alignBaseline"), @ViewDebug.IntToString(from = 8, to = "alignBottom"), @ViewDebug.IntToString(from = 5, to = "alignLeft"), @ViewDebug.IntToString(from = 12, to = "alignParentBottom"), @ViewDebug.IntToString(from = 9, to = "alignParentLeft"), @ViewDebug.IntToString(from = 11, to = "alignParentRight"), @ViewDebug.IntToString(from = 10, to = "alignParentTop"), @ViewDebug.IntToString(from = 7, to = "alignRight"), @ViewDebug.IntToString(from = 6, to = "alignTop"), @ViewDebug.IntToString(from = 3, to = "below"), @ViewDebug.IntToString(from = 14, to = "centerHorizontal"), @ViewDebug.IntToString(from = 13, to = "center"), @ViewDebug.IntToString(from = 15, to = "centerVertical"), @ViewDebug.IntToString(from = 0, to = "leftOf"), @ViewDebug.IntToString(from = 1, to = "rightOf"), @ViewDebug.IntToString(from = 18, to = "alignStart"), @ViewDebug.IntToString(from = 19, to = "alignEnd"), @ViewDebug.IntToString(from = 20, to = "alignParentStart"), @ViewDebug.IntToString(from = 21, to = "alignParentEnd"), @ViewDebug.IntToString(from = 16, to = "startOf"), @ViewDebug.IntToString(from = 17, to = "endOf")}, mapping = {@ViewDebug.IntToString(from = -1, to = "true"), @ViewDebug.IntToString(from = 0, to = "false/NO_ID")}, resolveId = true)
        private int[] mRules;
        private boolean mRulesChanged;
        public protected int mTop;

        static /* synthetic */ int access$112(LayoutParams x0, int x1) {
            int i = x0.mLeft + x1;
            x0.mLeft = i;
            return i;
        }

        static /* synthetic */ int access$120(LayoutParams x0, int x1) {
            int i = x0.mLeft - x1;
            x0.mLeft = i;
            return i;
        }

        static /* synthetic */ int access$212(LayoutParams x0, int x1) {
            int i = x0.mRight + x1;
            x0.mRight = i;
            return i;
        }

        static /* synthetic */ int access$220(LayoutParams x0, int x1) {
            int i = x0.mRight - x1;
            x0.mRight = i;
            return i;
        }

        static /* synthetic */ int access$312(LayoutParams x0, int x1) {
            int i = x0.mBottom + x1;
            x0.mBottom = i;
            return i;
        }

        static /* synthetic */ int access$412(LayoutParams x0, int x1) {
            int i = x0.mTop + x1;
            x0.mTop = i;
            return i;
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.mRules = new int[22];
            this.mInitialRules = new int[22];
            this.mRulesChanged = false;
            this.mIsRtlCompatibilityMode = false;
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.RelativeLayout_Layout);
            int targetSdkVersion = c.getApplicationInfo().targetSdkVersion;
            this.mIsRtlCompatibilityMode = targetSdkVersion < 17 || !c.getApplicationInfo().hasRtlSupport();
            int[] rules = this.mRules;
            int[] initialRules = this.mInitialRules;
            int N = a.getIndexCount();
            for (int i = 0; i < N; i++) {
                int attr = a.getIndex(i);
                switch (attr) {
                    case 0:
                        rules[0] = a.getResourceId(attr, 0);
                        break;
                    case 1:
                        rules[1] = a.getResourceId(attr, 0);
                        break;
                    case 2:
                        rules[2] = a.getResourceId(attr, 0);
                        break;
                    case 3:
                        rules[3] = a.getResourceId(attr, 0);
                        break;
                    case 4:
                        rules[4] = a.getResourceId(attr, 0);
                        break;
                    case 5:
                        rules[5] = a.getResourceId(attr, 0);
                        break;
                    case 6:
                        rules[6] = a.getResourceId(attr, 0);
                        break;
                    case 7:
                        rules[7] = a.getResourceId(attr, 0);
                        break;
                    case 8:
                        rules[8] = a.getResourceId(attr, 0);
                        break;
                    case 9:
                        rules[9] = a.getBoolean(attr, false) ? -1 : 0;
                        break;
                    case 10:
                        rules[10] = a.getBoolean(attr, false) ? -1 : 0;
                        break;
                    case 11:
                        rules[11] = a.getBoolean(attr, false) ? -1 : 0;
                        break;
                    case 12:
                        rules[12] = a.getBoolean(attr, false) ? -1 : 0;
                        break;
                    case 13:
                        rules[13] = a.getBoolean(attr, false) ? -1 : 0;
                        break;
                    case 14:
                        rules[14] = a.getBoolean(attr, false) ? -1 : 0;
                        break;
                    case 15:
                        rules[15] = a.getBoolean(attr, false) ? -1 : 0;
                        break;
                    case 16:
                        this.alignWithParent = a.getBoolean(attr, false);
                        break;
                    case 17:
                        rules[16] = a.getResourceId(attr, 0);
                        break;
                    case 18:
                        rules[17] = a.getResourceId(attr, 0);
                        break;
                    case 19:
                        rules[18] = a.getResourceId(attr, 0);
                        break;
                    case 20:
                        rules[19] = a.getResourceId(attr, 0);
                        break;
                    case 21:
                        rules[20] = a.getBoolean(attr, false) ? -1 : 0;
                        break;
                    case 22:
                        rules[21] = a.getBoolean(attr, false) ? -1 : 0;
                        break;
                }
            }
            this.mRulesChanged = true;
            System.arraycopy(rules, 0, initialRules, 0, 22);
            a.recycle();
        }

        public LayoutParams(int w, int h) {
            super(w, h);
            this.mRules = new int[22];
            this.mInitialRules = new int[22];
            this.mRulesChanged = false;
            this.mIsRtlCompatibilityMode = false;
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
            this.mRules = new int[22];
            this.mInitialRules = new int[22];
            this.mRulesChanged = false;
            this.mIsRtlCompatibilityMode = false;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
            this.mRules = new int[22];
            this.mInitialRules = new int[22];
            this.mRulesChanged = false;
            this.mIsRtlCompatibilityMode = false;
        }

        public LayoutParams(LayoutParams source) {
            super((ViewGroup.MarginLayoutParams) source);
            this.mRules = new int[22];
            this.mInitialRules = new int[22];
            this.mRulesChanged = false;
            this.mIsRtlCompatibilityMode = false;
            this.mIsRtlCompatibilityMode = source.mIsRtlCompatibilityMode;
            this.mRulesChanged = source.mRulesChanged;
            this.alignWithParent = source.alignWithParent;
            System.arraycopy(source.mRules, 0, this.mRules, 0, 22);
            System.arraycopy(source.mInitialRules, 0, this.mInitialRules, 0, 22);
        }

        @Override // android.view.ViewGroup.LayoutParams
        public String debug(String output) {
            return output + "ViewGroup.LayoutParams={ width=" + sizeToString(this.width) + ", height=" + sizeToString(this.height) + " }";
        }

        public void addRule(int verb) {
            addRule(verb, -1);
        }

        public void addRule(int verb, int subject) {
            if (!this.mNeedsLayoutResolution && isRelativeRule(verb) && this.mInitialRules[verb] != 0 && subject == 0) {
                this.mNeedsLayoutResolution = true;
            }
            this.mRules[verb] = subject;
            this.mInitialRules[verb] = subject;
            this.mRulesChanged = true;
        }

        public void removeRule(int verb) {
            addRule(verb, 0);
        }

        public int getRule(int verb) {
            return this.mRules[verb];
        }

        private synchronized boolean hasRelativeRules() {
            return (this.mInitialRules[16] == 0 && this.mInitialRules[17] == 0 && this.mInitialRules[18] == 0 && this.mInitialRules[19] == 0 && this.mInitialRules[20] == 0 && this.mInitialRules[21] == 0) ? false : true;
        }

        private synchronized boolean isRelativeRule(int rule) {
            return rule == 16 || rule == 17 || rule == 18 || rule == 19 || rule == 20 || rule == 21;
        }

        /* JADX WARN: Code restructure failed: missing block: B:98:0x0175, code lost:
            if (r17.mRules[11] != 0) goto L102;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private synchronized void resolveRules(int r18) {
            /*
                Method dump skipped, instructions count: 436
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: android.widget.RelativeLayout.LayoutParams.resolveRules(int):void");
        }

        public synchronized int[] getRules(int layoutDirection) {
            resolveLayoutDirection(layoutDirection);
            return this.mRules;
        }

        public int[] getRules() {
            return this.mRules;
        }

        @Override // android.view.ViewGroup.MarginLayoutParams, android.view.ViewGroup.LayoutParams
        public void resolveLayoutDirection(int layoutDirection) {
            if (shouldResolveLayoutDirection(layoutDirection)) {
                resolveRules(layoutDirection);
            }
            super.resolveLayoutDirection(layoutDirection);
        }

        private synchronized boolean shouldResolveLayoutDirection(int layoutDirection) {
            return (this.mNeedsLayoutResolution || hasRelativeRules()) && (this.mRulesChanged || layoutDirection != getLayoutDirection());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.view.ViewGroup.MarginLayoutParams, android.view.ViewGroup.LayoutParams
        public synchronized void encodeProperties(ViewHierarchyEncoder encoder) {
            super.encodeProperties(encoder);
            encoder.addProperty("layout:alignWithParent", this.alignWithParent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class DependencyGraph {
        private SparseArray<Node> mKeyNodes;
        private ArrayList<Node> mNodes;
        private ArrayDeque<Node> mRoots;

        private synchronized DependencyGraph() {
            this.mNodes = new ArrayList<>();
            this.mKeyNodes = new SparseArray<>();
            this.mRoots = new ArrayDeque<>();
        }

        synchronized void clear() {
            ArrayList<Node> nodes = this.mNodes;
            int count = nodes.size();
            for (int i = 0; i < count; i++) {
                nodes.get(i).release();
            }
            nodes.clear();
            this.mKeyNodes.clear();
            this.mRoots.clear();
        }

        synchronized void add(View view) {
            int id = view.getId();
            Node node = Node.acquire(view);
            if (id != -1) {
                this.mKeyNodes.put(id, node);
            }
            this.mNodes.add(node);
        }

        void getSortedViews(View[] sorted, int... rules) {
            ArrayDeque<Node> roots = findRoots(rules);
            int index = 0;
            while (true) {
                Node node = roots.pollLast();
                if (node == null) {
                    break;
                }
                View view = node.view;
                int key = view.getId();
                int index2 = index + 1;
                sorted[index] = view;
                ArrayMap<Node, DependencyGraph> dependents = node.dependents;
                int count = dependents.size();
                for (int i = 0; i < count; i++) {
                    Node dependent = dependents.keyAt(i);
                    SparseArray<Node> dependencies = dependent.dependencies;
                    dependencies.remove(key);
                    if (dependencies.size() == 0) {
                        roots.add(dependent);
                    }
                }
                index = index2;
            }
            if (index < sorted.length) {
                throw new IllegalStateException("Circular dependencies cannot exist in RelativeLayout");
            }
        }

        private synchronized ArrayDeque<Node> findRoots(int[] rulesFilter) {
            Node dependency;
            SparseArray<Node> keyNodes = this.mKeyNodes;
            ArrayList<Node> nodes = this.mNodes;
            int count = nodes.size();
            for (int i = 0; i < count; i++) {
                Node node = nodes.get(i);
                node.dependents.clear();
                node.dependencies.clear();
            }
            for (int i2 = 0; i2 < count; i2++) {
                Node node2 = nodes.get(i2);
                LayoutParams layoutParams = (LayoutParams) node2.view.getLayoutParams();
                int[] rules = layoutParams.mRules;
                for (int i3 : rulesFilter) {
                    int rule = rules[i3];
                    if (rule > 0 && (dependency = keyNodes.get(rule)) != null && dependency != node2) {
                        dependency.dependents.put(node2, this);
                        node2.dependencies.put(rule, dependency);
                    }
                }
            }
            ArrayDeque<Node> roots = this.mRoots;
            roots.clear();
            for (int i4 = 0; i4 < count; i4++) {
                Node node3 = nodes.get(i4);
                if (node3.dependencies.size() == 0) {
                    roots.addLast(node3);
                }
            }
            return roots;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes3.dex */
        public static class Node {
            private static final int POOL_LIMIT = 100;
            private static final Pools.SynchronizedPool<Node> sPool = new Pools.SynchronizedPool<>(100);
            View view;
            final ArrayMap<Node, DependencyGraph> dependents = new ArrayMap<>();
            final SparseArray<Node> dependencies = new SparseArray<>();

            static synchronized Node acquire(View view) {
                Node node = sPool.acquire();
                if (node == null) {
                    node = new Node();
                }
                node.view = view;
                return node;
            }

            synchronized void release() {
                this.view = null;
                this.dependents.clear();
                this.dependencies.clear();
                sPool.release(this);
            }
        }
    }
}
