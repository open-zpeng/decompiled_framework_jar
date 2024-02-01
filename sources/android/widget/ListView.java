package android.widget;

import android.app.slice.Slice;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Trace;
import android.util.AttributeSet;
import android.util.Log;
import android.util.MathUtils;
import android.util.SparseBooleanArray;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.RemotableViewMethod;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.ViewHierarchyEncoder;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.AbsListView;
import android.widget.RemoteViews;
import com.android.internal.R;
import com.google.android.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
@RemoteViews.RemoteView
/* loaded from: classes3.dex */
public class ListView extends AbsListView {
    private static final float MAX_SCROLL_FACTOR = 0.33f;
    private static final int MIN_SCROLL_PREVIEW_PIXELS = 2;
    static final int NO_POSITION = -1;
    static final String TAG = "ListView";
    public protected boolean mAreAllItemsSelectable;
    private final ArrowScrollFocusResult mArrowScrollFocusResult;
    public private protected Drawable mDivider;
    public private protected int mDividerHeight;
    private boolean mDividerIsOpaque;
    private Paint mDividerPaint;
    private FocusSelector mFocusSelector;
    private boolean mFooterDividersEnabled;
    public private protected ArrayList<FixedViewInfo> mFooterViewInfos;
    private boolean mHeaderDividersEnabled;
    public private protected ArrayList<FixedViewInfo> mHeaderViewInfos;
    private boolean mIsCacheColorOpaque;
    private boolean mItemsCanFocus;
    Drawable mOverScrollFooter;
    Drawable mOverScrollHeader;
    private final Rect mTempRect;

    /* loaded from: classes3.dex */
    public class FixedViewInfo {
        public Object data;
        public boolean isSelectable;
        public View view;

        public FixedViewInfo() {
        }
    }

    public ListView(Context context) {
        this(context, null);
    }

    public ListView(Context context, AttributeSet attrs) {
        this(context, attrs, 16842868);
    }

    public ListView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        int dividerHeight;
        this.mHeaderViewInfos = Lists.newArrayList();
        this.mFooterViewInfos = Lists.newArrayList();
        this.mAreAllItemsSelectable = true;
        this.mItemsCanFocus = false;
        this.mTempRect = new Rect();
        this.mArrowScrollFocusResult = new ArrowScrollFocusResult();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ListView, defStyleAttr, defStyleRes);
        CharSequence[] entries = a.getTextArray(0);
        if (entries != null) {
            setAdapter((ListAdapter) new ArrayAdapter(context, 17367043, entries));
        }
        Drawable d = a.getDrawable(1);
        if (d != null) {
            setDivider(d);
        }
        Drawable osHeader = a.getDrawable(5);
        if (osHeader != null) {
            setOverscrollHeader(osHeader);
        }
        Drawable osFooter = a.getDrawable(6);
        if (osFooter != null) {
            setOverscrollFooter(osFooter);
        }
        if (a.hasValueOrEmpty(2) && (dividerHeight = a.getDimensionPixelSize(2, 0)) != 0) {
            setDividerHeight(dividerHeight);
        }
        this.mHeaderDividersEnabled = a.getBoolean(3, true);
        this.mFooterDividersEnabled = a.getBoolean(4, true);
        a.recycle();
    }

    public int getMaxScrollAmount() {
        return (int) (MAX_SCROLL_FACTOR * (this.mBottom - this.mTop));
    }

    private synchronized void adjustViewsUpOrDown() {
        int delta;
        int childCount = getChildCount();
        if (childCount > 0) {
            if (!this.mStackFromBottom) {
                View child = getChildAt(0);
                delta = child.getTop() - this.mListPadding.top;
                if (this.mFirstPosition != 0) {
                    delta -= this.mDividerHeight;
                }
                if (delta < 0) {
                    delta = 0;
                }
            } else {
                View child2 = getChildAt(childCount - 1);
                delta = child2.getBottom() - (getHeight() - this.mListPadding.bottom);
                if (this.mFirstPosition + childCount < this.mItemCount) {
                    delta += this.mDividerHeight;
                }
                if (delta > 0) {
                    delta = 0;
                }
            }
            if (delta != 0) {
                offsetChildrenTopAndBottom(-delta);
            }
        }
    }

    public void addHeaderView(View v, Object data, boolean isSelectable) {
        if (v.getParent() != null && v.getParent() != this && Log.isLoggable(TAG, 5)) {
            Log.w(TAG, "The specified child already has a parent. You must call removeView() on the child's parent first.");
        }
        FixedViewInfo info = new FixedViewInfo();
        info.view = v;
        info.data = data;
        info.isSelectable = isSelectable;
        this.mHeaderViewInfos.add(info);
        this.mAreAllItemsSelectable &= isSelectable;
        if (this.mAdapter != null) {
            if (!(this.mAdapter instanceof HeaderViewListAdapter)) {
                wrapHeaderListAdapterInternal();
            }
            if (this.mDataSetObserver != null) {
                this.mDataSetObserver.onChanged();
            }
        }
    }

    public void addHeaderView(View v) {
        addHeaderView(v, null, true);
    }

    @Override // android.widget.AbsListView
    public int getHeaderViewsCount() {
        return this.mHeaderViewInfos.size();
    }

    public boolean removeHeaderView(View v) {
        if (this.mHeaderViewInfos.size() > 0) {
            boolean result = false;
            if (this.mAdapter != null && ((HeaderViewListAdapter) this.mAdapter).removeHeader(v)) {
                if (this.mDataSetObserver != null) {
                    this.mDataSetObserver.onChanged();
                }
                result = true;
            }
            removeFixedViewInfo(v, this.mHeaderViewInfos);
            return result;
        }
        return false;
    }

    private synchronized void removeFixedViewInfo(View v, ArrayList<FixedViewInfo> where) {
        int len = where.size();
        for (int i = 0; i < len; i++) {
            FixedViewInfo info = where.get(i);
            if (info.view == v) {
                where.remove(i);
                return;
            }
        }
    }

    public void addFooterView(View v, Object data, boolean isSelectable) {
        if (v.getParent() != null && v.getParent() != this && Log.isLoggable(TAG, 5)) {
            Log.w(TAG, "The specified child already has a parent. You must call removeView() on the child's parent first.");
        }
        FixedViewInfo info = new FixedViewInfo();
        info.view = v;
        info.data = data;
        info.isSelectable = isSelectable;
        this.mFooterViewInfos.add(info);
        this.mAreAllItemsSelectable &= isSelectable;
        if (this.mAdapter != null) {
            if (!(this.mAdapter instanceof HeaderViewListAdapter)) {
                wrapHeaderListAdapterInternal();
            }
            if (this.mDataSetObserver != null) {
                this.mDataSetObserver.onChanged();
            }
        }
    }

    public void addFooterView(View v) {
        addFooterView(v, null, true);
    }

    @Override // android.widget.AbsListView
    public int getFooterViewsCount() {
        return this.mFooterViewInfos.size();
    }

    public boolean removeFooterView(View v) {
        if (this.mFooterViewInfos.size() > 0) {
            boolean result = false;
            if (this.mAdapter != null && ((HeaderViewListAdapter) this.mAdapter).removeFooter(v)) {
                if (this.mDataSetObserver != null) {
                    this.mDataSetObserver.onChanged();
                }
                result = true;
            }
            removeFixedViewInfo(v, this.mFooterViewInfos);
            return result;
        }
        return false;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.widget.AdapterView
    public ListAdapter getAdapter() {
        return this.mAdapter;
    }

    @Override // android.widget.AbsListView
    @RemotableViewMethod(asyncImpl = "setRemoteViewsAdapterAsync")
    public void setRemoteViewsAdapter(Intent intent) {
        super.setRemoteViewsAdapter(intent);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.widget.AbsListView, android.widget.AdapterView
    public void setAdapter(ListAdapter adapter) {
        int position;
        if (this.mAdapter != null && this.mDataSetObserver != null) {
            this.mAdapter.unregisterDataSetObserver(this.mDataSetObserver);
        }
        resetList();
        this.mRecycler.clear();
        if (this.mHeaderViewInfos.size() > 0 || this.mFooterViewInfos.size() > 0) {
            this.mAdapter = wrapHeaderListAdapterInternal(this.mHeaderViewInfos, this.mFooterViewInfos, adapter);
        } else {
            this.mAdapter = adapter;
        }
        this.mOldSelectedPosition = -1;
        this.mOldSelectedRowId = Long.MIN_VALUE;
        super.setAdapter(adapter);
        if (this.mAdapter != null) {
            this.mAreAllItemsSelectable = this.mAdapter.areAllItemsEnabled();
            this.mOldItemCount = this.mItemCount;
            this.mItemCount = this.mAdapter.getCount();
            checkFocus();
            this.mDataSetObserver = new AbsListView.AdapterDataSetObserver();
            this.mAdapter.registerDataSetObserver(this.mDataSetObserver);
            this.mRecycler.setViewTypeCount(this.mAdapter.getViewTypeCount());
            if (this.mStackFromBottom) {
                position = lookForSelectablePosition(this.mItemCount - 1, false);
            } else {
                position = lookForSelectablePosition(0, true);
            }
            setSelectedPositionInt(position);
            setNextSelectedPositionInt(position);
            if (this.mItemCount == 0) {
                checkSelectionChanged();
            }
        } else {
            this.mAreAllItemsSelectable = true;
            checkFocus();
            checkSelectionChanged();
        }
        requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.widget.AbsListView
    public synchronized void resetList() {
        clearRecycledState(this.mHeaderViewInfos);
        clearRecycledState(this.mFooterViewInfos);
        super.resetList();
        this.mLayoutMode = 0;
    }

    private synchronized void clearRecycledState(ArrayList<FixedViewInfo> infos) {
        if (infos != null) {
            int count = infos.size();
            for (int i = 0; i < count; i++) {
                View child = infos.get(i).view;
                ViewGroup.LayoutParams params = child.getLayoutParams();
                if (checkLayoutParams(params)) {
                    ((AbsListView.LayoutParams) params).recycledHeaderFooter = false;
                }
            }
        }
    }

    private synchronized boolean showingTopFadingEdge() {
        int listTop = this.mScrollY + this.mListPadding.top;
        return this.mFirstPosition > 0 || getChildAt(0).getTop() > listTop;
    }

    private synchronized boolean showingBottomFadingEdge() {
        int childCount = getChildCount();
        int bottomOfBottomChild = getChildAt(childCount - 1).getBottom();
        int lastVisiblePosition = (this.mFirstPosition + childCount) - 1;
        int listBottom = (this.mScrollY + getHeight()) - this.mListPadding.bottom;
        return lastVisiblePosition < this.mItemCount - 1 || bottomOfBottomChild < listBottom;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
        int rectTopWithinChild = rect.top;
        rect.offset(child.getLeft(), child.getTop());
        rect.offset(-child.getScrollX(), -child.getScrollY());
        int height = getHeight();
        int listUnfadedTop = getScrollY();
        int listUnfadedBottom = listUnfadedTop + height;
        int fadingEdge = getVerticalFadingEdgeLength();
        if (showingTopFadingEdge() && (this.mSelectedPosition > 0 || rectTopWithinChild > fadingEdge)) {
            listUnfadedTop += fadingEdge;
        }
        int childCount = getChildCount();
        int bottomOfBottomChild = getChildAt(childCount - 1).getBottom();
        if (showingBottomFadingEdge() && (this.mSelectedPosition < this.mItemCount - 1 || rect.bottom < bottomOfBottomChild - fadingEdge)) {
            listUnfadedBottom -= fadingEdge;
        }
        int scrollYDelta = 0;
        if (rect.bottom > listUnfadedBottom && rect.top > listUnfadedTop) {
            int distanceToBottom = bottomOfBottomChild - listUnfadedBottom;
            scrollYDelta = Math.min(rect.height() > height ? 0 + (rect.top - listUnfadedTop) : 0 + (rect.bottom - listUnfadedBottom), distanceToBottom);
        } else if (rect.top < listUnfadedTop && rect.bottom < listUnfadedBottom) {
            int scrollYDelta2 = rect.height() > height ? 0 - (listUnfadedBottom - rect.bottom) : 0 - (listUnfadedTop - rect.top);
            int top = getChildAt(0).getTop();
            int deltaToTop = top - listUnfadedTop;
            scrollYDelta = Math.max(scrollYDelta2, deltaToTop);
        }
        boolean scroll = scrollYDelta != 0;
        if (scroll) {
            scrollListItemsBy(-scrollYDelta);
            positionSelector(-1, child);
            this.mSelectedTop = child.getTop();
            invalidate();
        }
        return scroll;
    }

    @Override // android.widget.AbsListView
    synchronized void fillGap(boolean down) {
        int count = getChildCount();
        if (down) {
            int paddingTop = 0;
            if ((this.mGroupFlags & 34) == 34) {
                paddingTop = getListPaddingTop();
            }
            int startOffset = count > 0 ? getChildAt(count - 1).getBottom() + this.mDividerHeight : paddingTop;
            fillDown(this.mFirstPosition + count, startOffset);
            correctTooHigh(getChildCount());
            return;
        }
        int paddingBottom = 0;
        if ((this.mGroupFlags & 34) == 34) {
            paddingBottom = getListPaddingBottom();
        }
        int startOffset2 = count > 0 ? getChildAt(0).getTop() - this.mDividerHeight : getHeight() - paddingBottom;
        fillUp(this.mFirstPosition - 1, startOffset2);
        correctTooLow(getChildCount());
    }

    public protected View fillDown(int pos, int nextTop) {
        View selectedView = null;
        int end = this.mBottom - this.mTop;
        if ((this.mGroupFlags & 34) == 34) {
            end -= this.mListPadding.bottom;
        }
        while (true) {
            if (nextTop >= end || pos >= this.mItemCount) {
                break;
            }
            boolean selected = pos == this.mSelectedPosition;
            View child = makeAndAddView(pos, nextTop, true, this.mListPadding.left, selected);
            nextTop = child.getBottom() + this.mDividerHeight;
            if (selected) {
                selectedView = child;
            }
            pos++;
        }
        setVisibleRangeHint(this.mFirstPosition, (this.mFirstPosition + getChildCount()) - 1);
        return selectedView;
    }

    public protected View fillUp(int pos, int nextBottom) {
        View selectedView = null;
        int end = 0;
        if ((this.mGroupFlags & 34) == 34) {
            end = this.mListPadding.top;
        }
        while (true) {
            if (nextBottom <= end || pos < 0) {
                break;
            }
            boolean selected = pos == this.mSelectedPosition;
            View child = makeAndAddView(pos, nextBottom, false, this.mListPadding.left, selected);
            nextBottom = child.getTop() - this.mDividerHeight;
            if (selected) {
                selectedView = child;
            }
            pos--;
        }
        this.mFirstPosition = pos + 1;
        setVisibleRangeHint(this.mFirstPosition, (this.mFirstPosition + getChildCount()) - 1);
        return selectedView;
    }

    private synchronized View fillFromTop(int nextTop) {
        this.mFirstPosition = Math.min(this.mFirstPosition, this.mSelectedPosition);
        this.mFirstPosition = Math.min(this.mFirstPosition, this.mItemCount - 1);
        if (this.mFirstPosition < 0) {
            this.mFirstPosition = 0;
        }
        return fillDown(this.mFirstPosition, nextTop);
    }

    private synchronized View fillFromMiddle(int childrenTop, int childrenBottom) {
        int height = childrenBottom - childrenTop;
        int position = reconcileSelectedPosition();
        View sel = makeAndAddView(position, childrenTop, true, this.mListPadding.left, true);
        this.mFirstPosition = position;
        int selHeight = sel.getMeasuredHeight();
        if (selHeight <= height) {
            sel.offsetTopAndBottom((height - selHeight) / 2);
        }
        fillAboveAndBelow(sel, position);
        if (!this.mStackFromBottom) {
            correctTooHigh(getChildCount());
        } else {
            correctTooLow(getChildCount());
        }
        return sel;
    }

    private synchronized void fillAboveAndBelow(View sel, int position) {
        int dividerHeight = this.mDividerHeight;
        if (!this.mStackFromBottom) {
            fillUp(position - 1, sel.getTop() - dividerHeight);
            adjustViewsUpOrDown();
            fillDown(position + 1, sel.getBottom() + dividerHeight);
            return;
        }
        fillDown(position + 1, sel.getBottom() + dividerHeight);
        adjustViewsUpOrDown();
        fillUp(position - 1, sel.getTop() - dividerHeight);
    }

    private synchronized View fillFromSelection(int selectedTop, int childrenTop, int childrenBottom) {
        int fadingEdgeLength = getVerticalFadingEdgeLength();
        int selectedPosition = this.mSelectedPosition;
        int topSelectionPixel = getTopSelectionPixel(childrenTop, fadingEdgeLength, selectedPosition);
        int bottomSelectionPixel = getBottomSelectionPixel(childrenBottom, fadingEdgeLength, selectedPosition);
        View sel = makeAndAddView(selectedPosition, selectedTop, true, this.mListPadding.left, true);
        if (sel.getBottom() > bottomSelectionPixel) {
            int spaceAbove = sel.getTop() - topSelectionPixel;
            int spaceBelow = sel.getBottom() - bottomSelectionPixel;
            int offset = Math.min(spaceAbove, spaceBelow);
            sel.offsetTopAndBottom(-offset);
        } else if (sel.getTop() < topSelectionPixel) {
            int spaceAbove2 = topSelectionPixel - sel.getTop();
            int spaceBelow2 = bottomSelectionPixel - sel.getBottom();
            int offset2 = Math.min(spaceAbove2, spaceBelow2);
            sel.offsetTopAndBottom(offset2);
        }
        fillAboveAndBelow(sel, selectedPosition);
        if (!this.mStackFromBottom) {
            correctTooHigh(getChildCount());
        } else {
            correctTooLow(getChildCount());
        }
        return sel;
    }

    private synchronized int getBottomSelectionPixel(int childrenBottom, int fadingEdgeLength, int selectedPosition) {
        if (selectedPosition == this.mItemCount - 1) {
            return childrenBottom;
        }
        int bottomSelectionPixel = childrenBottom - fadingEdgeLength;
        return bottomSelectionPixel;
    }

    private synchronized int getTopSelectionPixel(int childrenTop, int fadingEdgeLength, int selectedPosition) {
        if (selectedPosition <= 0) {
            return childrenTop;
        }
        int topSelectionPixel = childrenTop + fadingEdgeLength;
        return topSelectionPixel;
    }

    @Override // android.widget.AbsListView
    @RemotableViewMethod
    public void smoothScrollToPosition(int position) {
        super.smoothScrollToPosition(position);
    }

    @Override // android.widget.AbsListView
    @RemotableViewMethod
    public void smoothScrollByOffset(int offset) {
        super.smoothScrollByOffset(offset);
    }

    private synchronized View moveSelection(View oldSel, View newSel, int delta, int childrenTop, int childrenBottom) {
        View sel;
        int fadingEdgeLength = getVerticalFadingEdgeLength();
        int selectedPosition = this.mSelectedPosition;
        int topSelectionPixel = getTopSelectionPixel(childrenTop, fadingEdgeLength, selectedPosition);
        int bottomSelectionPixel = getBottomSelectionPixel(childrenTop, fadingEdgeLength, selectedPosition);
        if (delta > 0) {
            View oldSel2 = makeAndAddView(selectedPosition - 1, oldSel.getTop(), true, this.mListPadding.left, false);
            int dividerHeight = this.mDividerHeight;
            sel = makeAndAddView(selectedPosition, oldSel2.getBottom() + dividerHeight, true, this.mListPadding.left, true);
            if (sel.getBottom() > bottomSelectionPixel) {
                int spaceAbove = sel.getTop() - topSelectionPixel;
                int spaceBelow = sel.getBottom() - bottomSelectionPixel;
                int halfVerticalSpace = (childrenBottom - childrenTop) / 2;
                int offset = Math.min(spaceAbove, spaceBelow);
                int offset2 = Math.min(offset, halfVerticalSpace);
                oldSel2.offsetTopAndBottom(-offset2);
                sel.offsetTopAndBottom(-offset2);
            }
            if (!this.mStackFromBottom) {
                fillUp(this.mSelectedPosition - 2, sel.getTop() - dividerHeight);
                adjustViewsUpOrDown();
                fillDown(this.mSelectedPosition + 1, sel.getBottom() + dividerHeight);
            } else {
                fillDown(this.mSelectedPosition + 1, sel.getBottom() + dividerHeight);
                adjustViewsUpOrDown();
                fillUp(this.mSelectedPosition - 2, sel.getTop() - dividerHeight);
            }
        } else if (delta < 0) {
            if (newSel != null) {
                sel = makeAndAddView(selectedPosition, newSel.getTop(), true, this.mListPadding.left, true);
            } else {
                sel = makeAndAddView(selectedPosition, oldSel.getTop(), false, this.mListPadding.left, true);
            }
            if (sel.getTop() < topSelectionPixel) {
                int spaceAbove2 = topSelectionPixel - sel.getTop();
                int spaceBelow2 = bottomSelectionPixel - sel.getBottom();
                int halfVerticalSpace2 = (childrenBottom - childrenTop) / 2;
                int offset3 = Math.min(spaceAbove2, spaceBelow2);
                sel.offsetTopAndBottom(Math.min(offset3, halfVerticalSpace2));
            }
            fillAboveAndBelow(sel, selectedPosition);
        } else {
            int oldTop = oldSel.getTop();
            sel = makeAndAddView(selectedPosition, oldTop, true, this.mListPadding.left, true);
            if (oldTop < childrenTop) {
                int newBottom = sel.getBottom();
                if (newBottom < childrenTop + 20) {
                    sel.offsetTopAndBottom(childrenTop - sel.getTop());
                }
            }
            fillAboveAndBelow(sel, selectedPosition);
        }
        return sel;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class FocusSelector implements Runnable {
        private static final int STATE_REQUEST_FOCUS = 3;
        private static final int STATE_SET_SELECTION = 1;
        private static final int STATE_WAIT_FOR_LAYOUT = 2;
        private int mAction;
        private int mPosition;
        private int mPositionTop;

        private FocusSelector() {
        }

        synchronized FocusSelector setupForSetSelection(int position, int top) {
            this.mPosition = position;
            this.mPositionTop = top;
            this.mAction = 1;
            return this;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.mAction == 1) {
                ListView.this.setSelectionFromTop(this.mPosition, this.mPositionTop);
                this.mAction = 2;
            } else if (this.mAction == 3) {
                int childIndex = this.mPosition - ListView.this.mFirstPosition;
                View child = ListView.this.getChildAt(childIndex);
                if (child != null) {
                    child.requestFocus();
                }
                this.mAction = -1;
            }
        }

        synchronized Runnable setupFocusIfValid(int position) {
            if (this.mAction != 2 || position != this.mPosition) {
                return null;
            }
            this.mAction = 3;
            return this;
        }

        synchronized void onLayoutComplete() {
            if (this.mAction == 2) {
                this.mAction = -1;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.AbsListView, android.widget.AdapterView, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        if (this.mFocusSelector != null) {
            removeCallbacks(this.mFocusSelector);
            this.mFocusSelector = null;
        }
        super.onDetachedFromWindow();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.AbsListView, android.view.View
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        View focusedChild;
        if (getChildCount() > 0 && (focusedChild = getFocusedChild()) != null) {
            int childPosition = this.mFirstPosition + indexOfChild(focusedChild);
            int childBottom = focusedChild.getBottom();
            int offset = Math.max(0, childBottom - (h - this.mPaddingTop));
            int top = focusedChild.getTop() - offset;
            if (this.mFocusSelector == null) {
                this.mFocusSelector = new FocusSelector();
            }
            post(this.mFocusSelector.setupForSetSelection(childPosition, top));
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.AbsListView, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int widthSize2 = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int childWidth = 0;
        int childHeight = 0;
        int childState = 0;
        this.mItemCount = this.mAdapter == null ? 0 : this.mAdapter.getCount();
        if (this.mItemCount > 0 && (widthMode == 0 || heightMode == 0)) {
            View child = obtainView(0, this.mIsScrap);
            measureScrapChild(child, 0, widthMeasureSpec, heightSize);
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();
            childState = combineMeasuredStates(0, child.getMeasuredState());
            if (recycleOnMeasure() && this.mRecycler.shouldRecycleViewType(((AbsListView.LayoutParams) child.getLayoutParams()).viewType)) {
                this.mRecycler.addScrapView(child, 0);
            }
        }
        int childWidth2 = childWidth;
        int childHeight2 = childHeight;
        int childState2 = childState;
        if (widthMode == 0) {
            widthSize = this.mListPadding.left + this.mListPadding.right + childWidth2 + getVerticalScrollbarWidth();
        } else {
            widthSize = ((-16777216) & childState2) | widthSize2;
        }
        int widthSize3 = widthSize;
        int heightSize2 = heightMode == 0 ? this.mListPadding.top + this.mListPadding.bottom + childHeight2 + (getVerticalFadingEdgeLength() * 2) : heightSize;
        if (heightMode == Integer.MIN_VALUE) {
            heightSize2 = measureHeightOfChildren(widthMeasureSpec, 0, -1, heightSize2, -1);
        }
        setMeasuredDimension(widthSize3, heightSize2);
        this.mWidthMeasureSpec = widthMeasureSpec;
    }

    private synchronized void measureScrapChild(View child, int position, int widthMeasureSpec, int heightHint) {
        int childHeightSpec;
        AbsListView.LayoutParams p = (AbsListView.LayoutParams) child.getLayoutParams();
        if (p == null) {
            p = (AbsListView.LayoutParams) generateDefaultLayoutParams();
            child.setLayoutParams(p);
        }
        p.viewType = this.mAdapter.getItemViewType(position);
        p.isEnabled = this.mAdapter.isEnabled(position);
        p.forceAdd = true;
        int childWidthSpec = ViewGroup.getChildMeasureSpec(widthMeasureSpec, this.mListPadding.left + this.mListPadding.right, p.width);
        int lpHeight = p.height;
        if (lpHeight > 0) {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, 1073741824);
        } else {
            childHeightSpec = View.MeasureSpec.makeSafeMeasureSpec(heightHint, 0);
        }
        child.measure(childWidthSpec, childHeightSpec);
        child.forceLayout();
    }

    @ViewDebug.ExportedProperty(category = Slice.HINT_LIST)
    protected synchronized boolean recycleOnMeasure() {
        return true;
    }

    public private protected final int measureHeightOfChildren(int widthMeasureSpec, int startPosition, int endPosition, int maxHeight, int disallowPartialChildPosition) {
        ListAdapter adapter = this.mAdapter;
        if (adapter == null) {
            return this.mListPadding.top + this.mListPadding.bottom;
        }
        int returnedHeight = this.mListPadding.top + this.mListPadding.bottom;
        int dividerHeight = this.mDividerHeight;
        int endPosition2 = endPosition == -1 ? adapter.getCount() - 1 : endPosition;
        AbsListView.RecycleBin recycleBin = this.mRecycler;
        boolean recyle = recycleOnMeasure();
        boolean[] isScrap = this.mIsScrap;
        int prevHeightWithoutPartialChild = 0;
        int returnedHeight2 = returnedHeight;
        int returnedHeight3 = startPosition;
        while (returnedHeight3 <= endPosition2) {
            View child = obtainView(returnedHeight3, isScrap);
            measureScrapChild(child, returnedHeight3, widthMeasureSpec, maxHeight);
            if (returnedHeight3 > 0) {
                returnedHeight2 += dividerHeight;
            }
            if (recyle && recycleBin.shouldRecycleViewType(((AbsListView.LayoutParams) child.getLayoutParams()).viewType)) {
                recycleBin.addScrapView(child, -1);
            }
            returnedHeight2 += child.getMeasuredHeight();
            if (returnedHeight2 >= maxHeight) {
                return (disallowPartialChildPosition < 0 || returnedHeight3 <= disallowPartialChildPosition || prevHeightWithoutPartialChild <= 0 || returnedHeight2 == maxHeight) ? maxHeight : prevHeightWithoutPartialChild;
            }
            if (disallowPartialChildPosition >= 0 && returnedHeight3 >= disallowPartialChildPosition) {
                prevHeightWithoutPartialChild = returnedHeight2;
            }
            returnedHeight3++;
        }
        return returnedHeight2;
    }

    synchronized int findMotionRow(int y) {
        int childCount = getChildCount();
        if (childCount > 0) {
            if (!this.mStackFromBottom) {
                for (int i = 0; i < childCount; i++) {
                    View v = getChildAt(i);
                    if (y <= v.getBottom()) {
                        return this.mFirstPosition + i;
                    }
                }
                return -1;
            }
            for (int i2 = childCount - 1; i2 >= 0; i2--) {
                View v2 = getChildAt(i2);
                if (y >= v2.getTop()) {
                    return this.mFirstPosition + i2;
                }
            }
            return -1;
        }
        return -1;
    }

    public protected View fillSpecific(int position, int top) {
        View below;
        View above;
        boolean tempIsSelected = position == this.mSelectedPosition;
        View temp = makeAndAddView(position, top, true, this.mListPadding.left, tempIsSelected);
        this.mFirstPosition = position;
        int dividerHeight = this.mDividerHeight;
        if (!this.mStackFromBottom) {
            above = fillUp(position - 1, temp.getTop() - dividerHeight);
            adjustViewsUpOrDown();
            below = fillDown(position + 1, temp.getBottom() + dividerHeight);
            int childCount = getChildCount();
            if (childCount > 0) {
                correctTooHigh(childCount);
            }
        } else {
            below = fillDown(position + 1, temp.getBottom() + dividerHeight);
            adjustViewsUpOrDown();
            above = fillUp(position - 1, temp.getTop() - dividerHeight);
            int childCount2 = getChildCount();
            if (childCount2 > 0) {
                correctTooLow(childCount2);
            }
        }
        if (tempIsSelected) {
            return temp;
        }
        if (above != null) {
            return above;
        }
        return below;
    }

    public protected void correctTooHigh(int childCount) {
        int lastPosition = (this.mFirstPosition + childCount) - 1;
        if (lastPosition == this.mItemCount - 1 && childCount > 0) {
            View lastChild = getChildAt(childCount - 1);
            int lastBottom = lastChild.getBottom();
            int end = (this.mBottom - this.mTop) - this.mListPadding.bottom;
            int bottomOffset = end - lastBottom;
            View firstChild = getChildAt(0);
            int firstTop = firstChild.getTop();
            if (bottomOffset > 0) {
                if (this.mFirstPosition > 0 || firstTop < this.mListPadding.top) {
                    if (this.mFirstPosition == 0) {
                        bottomOffset = Math.min(bottomOffset, this.mListPadding.top - firstTop);
                    }
                    offsetChildrenTopAndBottom(bottomOffset);
                    if (this.mFirstPosition > 0) {
                        fillUp(this.mFirstPosition - 1, firstChild.getTop() - this.mDividerHeight);
                        adjustViewsUpOrDown();
                    }
                }
            }
        }
    }

    public protected void correctTooLow(int childCount) {
        if (this.mFirstPosition == 0 && childCount > 0) {
            View firstChild = getChildAt(0);
            int firstTop = firstChild.getTop();
            int start = this.mListPadding.top;
            int end = (this.mBottom - this.mTop) - this.mListPadding.bottom;
            int topOffset = firstTop - start;
            View lastChild = getChildAt(childCount - 1);
            int lastBottom = lastChild.getBottom();
            int lastPosition = (this.mFirstPosition + childCount) - 1;
            if (topOffset > 0) {
                if (lastPosition < this.mItemCount - 1 || lastBottom > end) {
                    if (lastPosition == this.mItemCount - 1) {
                        topOffset = Math.min(topOffset, lastBottom - end);
                    }
                    offsetChildrenTopAndBottom(-topOffset);
                    if (lastPosition < this.mItemCount - 1) {
                        fillDown(lastPosition + 1, lastChild.getBottom() + this.mDividerHeight);
                        adjustViewsUpOrDown();
                    }
                } else if (lastPosition == this.mItemCount - 1) {
                    adjustViewsUpOrDown();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:175:0x0301 A[Catch: all -> 0x042e, TryCatch #2 {all -> 0x042e, blocks: (B:103:0x0172, B:123:0x0240, B:142:0x029b, B:144:0x02ab, B:146:0x02af, B:148:0x02b5, B:152:0x02bf, B:159:0x02d0, B:161:0x02d6, B:162:0x02d9, B:166:0x02ea, B:192:0x033e, B:195:0x0346, B:197:0x034f, B:200:0x0358, B:212:0x03b4, B:214:0x03ba, B:215:0x03bd, B:217:0x03c6, B:218:0x03ce, B:220:0x03dd, B:221:0x03e0, B:201:0x036f, B:207:0x038f, B:209:0x03a4, B:163:0x02dd, B:154:0x02c5, B:165:0x02e7, B:167:0x02f1, B:169:0x02f6, B:175:0x0301, B:177:0x030c, B:186:0x032f, B:189:0x0337, B:179:0x0312, B:181:0x0316, B:183:0x0321, B:185:0x0327, B:124:0x0250, B:125:0x0266, B:127:0x026a, B:129:0x0270, B:133:0x027a, B:132:0x0276, B:134:0x027f, B:136:0x0285, B:140:0x028f, B:139:0x028b, B:141:0x0294, B:104:0x0176, B:105:0x0196, B:106:0x01b1, B:108:0x01ce, B:110:0x01d2, B:112:0x01da, B:115:0x01e1, B:118:0x0212, B:120:0x021c, B:121:0x0222, B:229:0x03f4, B:230:0x042d), top: B:244:0x00b6 }] */
    /* JADX WARN: Removed duplicated region for block: B:179:0x0312 A[Catch: all -> 0x042e, TryCatch #2 {all -> 0x042e, blocks: (B:103:0x0172, B:123:0x0240, B:142:0x029b, B:144:0x02ab, B:146:0x02af, B:148:0x02b5, B:152:0x02bf, B:159:0x02d0, B:161:0x02d6, B:162:0x02d9, B:166:0x02ea, B:192:0x033e, B:195:0x0346, B:197:0x034f, B:200:0x0358, B:212:0x03b4, B:214:0x03ba, B:215:0x03bd, B:217:0x03c6, B:218:0x03ce, B:220:0x03dd, B:221:0x03e0, B:201:0x036f, B:207:0x038f, B:209:0x03a4, B:163:0x02dd, B:154:0x02c5, B:165:0x02e7, B:167:0x02f1, B:169:0x02f6, B:175:0x0301, B:177:0x030c, B:186:0x032f, B:189:0x0337, B:179:0x0312, B:181:0x0316, B:183:0x0321, B:185:0x0327, B:124:0x0250, B:125:0x0266, B:127:0x026a, B:129:0x0270, B:133:0x027a, B:132:0x0276, B:134:0x027f, B:136:0x0285, B:140:0x028f, B:139:0x028b, B:141:0x0294, B:104:0x0176, B:105:0x0196, B:106:0x01b1, B:108:0x01ce, B:110:0x01d2, B:112:0x01da, B:115:0x01e1, B:118:0x0212, B:120:0x021c, B:121:0x0222, B:229:0x03f4, B:230:0x042d), top: B:244:0x00b6 }] */
    /* JADX WARN: Removed duplicated region for block: B:212:0x03b4 A[Catch: all -> 0x042e, TryCatch #2 {all -> 0x042e, blocks: (B:103:0x0172, B:123:0x0240, B:142:0x029b, B:144:0x02ab, B:146:0x02af, B:148:0x02b5, B:152:0x02bf, B:159:0x02d0, B:161:0x02d6, B:162:0x02d9, B:166:0x02ea, B:192:0x033e, B:195:0x0346, B:197:0x034f, B:200:0x0358, B:212:0x03b4, B:214:0x03ba, B:215:0x03bd, B:217:0x03c6, B:218:0x03ce, B:220:0x03dd, B:221:0x03e0, B:201:0x036f, B:207:0x038f, B:209:0x03a4, B:163:0x02dd, B:154:0x02c5, B:165:0x02e7, B:167:0x02f1, B:169:0x02f6, B:175:0x0301, B:177:0x030c, B:186:0x032f, B:189:0x0337, B:179:0x0312, B:181:0x0316, B:183:0x0321, B:185:0x0327, B:124:0x0250, B:125:0x0266, B:127:0x026a, B:129:0x0270, B:133:0x027a, B:132:0x0276, B:134:0x027f, B:136:0x0285, B:140:0x028f, B:139:0x028b, B:141:0x0294, B:104:0x0176, B:105:0x0196, B:106:0x01b1, B:108:0x01ce, B:110:0x01d2, B:112:0x01da, B:115:0x01e1, B:118:0x0212, B:120:0x021c, B:121:0x0222, B:229:0x03f4, B:230:0x042d), top: B:244:0x00b6 }] */
    /* JADX WARN: Removed duplicated region for block: B:217:0x03c6 A[Catch: all -> 0x042e, TryCatch #2 {all -> 0x042e, blocks: (B:103:0x0172, B:123:0x0240, B:142:0x029b, B:144:0x02ab, B:146:0x02af, B:148:0x02b5, B:152:0x02bf, B:159:0x02d0, B:161:0x02d6, B:162:0x02d9, B:166:0x02ea, B:192:0x033e, B:195:0x0346, B:197:0x034f, B:200:0x0358, B:212:0x03b4, B:214:0x03ba, B:215:0x03bd, B:217:0x03c6, B:218:0x03ce, B:220:0x03dd, B:221:0x03e0, B:201:0x036f, B:207:0x038f, B:209:0x03a4, B:163:0x02dd, B:154:0x02c5, B:165:0x02e7, B:167:0x02f1, B:169:0x02f6, B:175:0x0301, B:177:0x030c, B:186:0x032f, B:189:0x0337, B:179:0x0312, B:181:0x0316, B:183:0x0321, B:185:0x0327, B:124:0x0250, B:125:0x0266, B:127:0x026a, B:129:0x0270, B:133:0x027a, B:132:0x0276, B:134:0x027f, B:136:0x0285, B:140:0x028f, B:139:0x028b, B:141:0x0294, B:104:0x0176, B:105:0x0196, B:106:0x01b1, B:108:0x01ce, B:110:0x01d2, B:112:0x01da, B:115:0x01e1, B:118:0x0212, B:120:0x021c, B:121:0x0222, B:229:0x03f4, B:230:0x042d), top: B:244:0x00b6 }] */
    /* JADX WARN: Removed duplicated region for block: B:220:0x03dd A[Catch: all -> 0x042e, TryCatch #2 {all -> 0x042e, blocks: (B:103:0x0172, B:123:0x0240, B:142:0x029b, B:144:0x02ab, B:146:0x02af, B:148:0x02b5, B:152:0x02bf, B:159:0x02d0, B:161:0x02d6, B:162:0x02d9, B:166:0x02ea, B:192:0x033e, B:195:0x0346, B:197:0x034f, B:200:0x0358, B:212:0x03b4, B:214:0x03ba, B:215:0x03bd, B:217:0x03c6, B:218:0x03ce, B:220:0x03dd, B:221:0x03e0, B:201:0x036f, B:207:0x038f, B:209:0x03a4, B:163:0x02dd, B:154:0x02c5, B:165:0x02e7, B:167:0x02f1, B:169:0x02f6, B:175:0x0301, B:177:0x030c, B:186:0x032f, B:189:0x0337, B:179:0x0312, B:181:0x0316, B:183:0x0321, B:185:0x0327, B:124:0x0250, B:125:0x0266, B:127:0x026a, B:129:0x0270, B:133:0x027a, B:132:0x0276, B:134:0x027f, B:136:0x0285, B:140:0x028f, B:139:0x028b, B:141:0x0294, B:104:0x0176, B:105:0x0196, B:106:0x01b1, B:108:0x01ce, B:110:0x01d2, B:112:0x01da, B:115:0x01e1, B:118:0x0212, B:120:0x021c, B:121:0x0222, B:229:0x03f4, B:230:0x042d), top: B:244:0x00b6 }] */
    /* JADX WARN: Removed duplicated region for block: B:224:0x03e7  */
    /* JADX WARN: Removed duplicated region for block: B:226:0x03ee  */
    /* JADX WARN: Removed duplicated region for block: B:248:? A[RETURN, SYNTHETIC] */
    @Override // android.widget.AbsListView
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void layoutChildren() {
        /*
            Method dump skipped, instructions count: 1120
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.ListView.layoutChildren():void");
    }

    public private protected boolean trackMotionScroll(int deltaY, int incrementalDeltaY) {
        boolean result = super.trackMotionScroll(deltaY, incrementalDeltaY);
        removeUnusedFixedViews(this.mHeaderViewInfos);
        removeUnusedFixedViews(this.mFooterViewInfos);
        return result;
    }

    private synchronized void removeUnusedFixedViews(List<FixedViewInfo> infoList) {
        if (infoList == null) {
            return;
        }
        for (int i = infoList.size() - 1; i >= 0; i--) {
            FixedViewInfo fixedViewInfo = infoList.get(i);
            View view = fixedViewInfo.view;
            AbsListView.LayoutParams lp = (AbsListView.LayoutParams) view.getLayoutParams();
            if (view.getParent() == null && lp != null && lp.recycledHeaderFooter) {
                removeDetachedView(view, false);
                lp.recycledHeaderFooter = false;
            }
        }
    }

    public protected boolean isDirectChildHeaderOrFooter(View child) {
        ArrayList<FixedViewInfo> headers = this.mHeaderViewInfos;
        int numHeaders = headers.size();
        for (int i = 0; i < numHeaders; i++) {
            if (child == headers.get(i).view) {
                return true;
            }
        }
        ArrayList<FixedViewInfo> footers = this.mFooterViewInfos;
        int numFooters = footers.size();
        for (int i2 = 0; i2 < numFooters; i2++) {
            if (child == footers.get(i2).view) {
                return true;
            }
        }
        return false;
    }

    public protected View makeAndAddView(int position, int y, boolean flow, int childrenLeft, boolean selected) {
        View activeView;
        if (!this.mDataChanged && (activeView = this.mRecycler.getActiveView(position)) != null) {
            setupChild(activeView, position, y, flow, childrenLeft, selected, true);
            return activeView;
        }
        View child = obtainView(position, this.mIsScrap);
        setupChild(child, position, y, flow, childrenLeft, selected, this.mIsScrap[0]);
        return child;
    }

    private synchronized void setupChild(View child, int position, int y, boolean flowDown, int childrenLeft, boolean selected, boolean isAttachedToWindow) {
        boolean z;
        int childHeightSpec;
        Trace.traceBegin(8L, "setupListItem");
        boolean isSelected = selected && shouldShowSelector();
        boolean updateChildSelected = isSelected != child.isSelected();
        int mode = this.mTouchMode;
        boolean isPressed = mode > 0 && mode < 3 && this.mMotionPosition == position;
        boolean updateChildPressed = isPressed != child.isPressed();
        boolean needToMeasure = !isAttachedToWindow || updateChildSelected || child.isLayoutRequested();
        AbsListView.LayoutParams p = (AbsListView.LayoutParams) child.getLayoutParams();
        if (p == null) {
            p = (AbsListView.LayoutParams) generateDefaultLayoutParams();
        }
        AbsListView.LayoutParams p2 = p;
        p2.viewType = this.mAdapter.getItemViewType(position);
        p2.isEnabled = this.mAdapter.isEnabled(position);
        if (updateChildSelected) {
            child.setSelected(isSelected);
        }
        if (updateChildPressed) {
            child.setPressed(isPressed);
        }
        if (this.mChoiceMode != 0 && this.mCheckStates != null) {
            if (child instanceof Checkable) {
                ((Checkable) child).setChecked(this.mCheckStates.get(position));
            } else if (getContext().getApplicationInfo().targetSdkVersion >= 11) {
                child.setActivated(this.mCheckStates.get(position));
            }
        }
        if ((isAttachedToWindow && !p2.forceAdd) || (p2.recycledHeaderFooter && p2.viewType == -2)) {
            attachViewToParent(child, flowDown ? -1 : 0, p2);
            if (isAttachedToWindow && ((AbsListView.LayoutParams) child.getLayoutParams()).scrappedFromPosition != position) {
                child.jumpDrawablesToCurrentState();
            }
        } else {
            p2.forceAdd = false;
            if (p2.viewType == -2) {
                z = true;
                p2.recycledHeaderFooter = true;
            } else {
                z = true;
            }
            addViewInLayout(child, flowDown ? -1 : 0, p2, z);
            child.resolveRtlPropertiesIfNeeded();
        }
        if (needToMeasure) {
            int childWidthSpec = ViewGroup.getChildMeasureSpec(this.mWidthMeasureSpec, this.mListPadding.left + this.mListPadding.right, p2.width);
            int lpHeight = p2.height;
            if (lpHeight > 0) {
                childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, 1073741824);
            } else {
                int childHeightSpec2 = getMeasuredHeight();
                childHeightSpec = View.MeasureSpec.makeSafeMeasureSpec(childHeightSpec2, 0);
            }
            child.measure(childWidthSpec, childHeightSpec);
        } else {
            cleanupLayoutState(child);
        }
        int w = child.getMeasuredWidth();
        int h = child.getMeasuredHeight();
        int childTop = flowDown ? y : y - h;
        if (needToMeasure) {
            int childRight = childrenLeft + w;
            int w2 = childTop + h;
            child.layout(childrenLeft, childTop, childRight, w2);
        } else {
            int w3 = child.getLeft();
            child.offsetLeftAndRight(childrenLeft - w3);
            child.offsetTopAndBottom(childTop - child.getTop());
        }
        if (this.mCachingStarted && !child.isDrawingCacheEnabled()) {
            child.setDrawingCacheEnabled(true);
        }
        Trace.traceEnd(8L);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.AdapterView, android.view.ViewGroup
    public boolean canAnimate() {
        return super.canAnimate() && this.mItemCount > 0;
    }

    @Override // android.widget.AdapterView
    public void setSelection(int position) {
        setSelectionFromTop(position, 0);
    }

    public private protected void setSelectionInt(int position) {
        setNextSelectedPositionInt(position);
        boolean awakeScrollbars = false;
        int selectedPosition = this.mSelectedPosition;
        if (selectedPosition >= 0) {
            if (position == selectedPosition - 1) {
                awakeScrollbars = true;
            } else if (position == selectedPosition + 1) {
                awakeScrollbars = true;
            }
        }
        if (this.mPositionScroller != null) {
            this.mPositionScroller.stop();
        }
        layoutChildren();
        if (awakeScrollbars) {
            awakenScrollBars();
        }
    }

    public private protected int lookForSelectablePosition(int position, boolean lookDown) {
        ListAdapter adapter = this.mAdapter;
        if (adapter == null || isInTouchMode()) {
            return -1;
        }
        int count = adapter.getCount();
        if (!this.mAreAllItemsSelectable) {
            if (lookDown) {
                position = Math.max(0, position);
                while (position < count && !adapter.isEnabled(position)) {
                    position++;
                }
            } else {
                position = Math.min(position, count - 1);
                while (position >= 0 && !adapter.isEnabled(position)) {
                    position--;
                }
            }
        }
        if (position < 0 || position >= count) {
            return -1;
        }
        return position;
    }

    synchronized int lookForSelectablePositionAfter(int current, int position, boolean lookDown) {
        int position2;
        ListAdapter adapter = this.mAdapter;
        if (adapter == null || isInTouchMode()) {
            return -1;
        }
        int after = lookForSelectablePosition(position, lookDown);
        if (after != -1) {
            return after;
        }
        int count = adapter.getCount();
        int current2 = MathUtils.constrain(current, -1, count - 1);
        if (lookDown) {
            position2 = Math.min(position - 1, count - 1);
            while (position2 > current2 && !adapter.isEnabled(position2)) {
                position2--;
            }
            if (position2 <= current2) {
                return -1;
            }
        } else {
            position2 = Math.max(0, position + 1);
            while (position2 < current2 && !adapter.isEnabled(position2)) {
                position2++;
            }
            if (position2 >= current2) {
                return -1;
            }
        }
        return position2;
    }

    public void setSelectionAfterHeaderView() {
        int count = getHeaderViewsCount();
        if (count > 0) {
            this.mNextSelectedPosition = 0;
        } else if (this.mAdapter != null) {
            setSelection(count);
        } else {
            this.mNextSelectedPosition = count;
            this.mLayoutMode = 2;
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean handled = super.dispatchKeyEvent(event);
        if (!handled) {
            View focused = getFocusedChild();
            if (focused != null && event.getAction() == 0) {
                return onKeyDown(event.getKeyCode(), event);
            }
            return handled;
        }
        return handled;
    }

    @Override // android.widget.AbsListView, android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return commonKey(keyCode, 1, event);
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return commonKey(keyCode, repeatCount, event);
    }

    @Override // android.widget.AbsListView, android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return commonKey(keyCode, 1, event);
    }

    private synchronized boolean commonKey(int keyCode, int count, KeyEvent event) {
        int count2;
        if (this.mAdapter == null || !isAttachedToWindow()) {
            return false;
        }
        if (this.mDataChanged) {
            layoutChildren();
        }
        boolean handled = false;
        int action = event.getAction();
        if (KeyEvent.isConfirmKey(keyCode) && event.hasNoModifiers() && action != 1 && !(handled = resurrectSelectionIfNeeded()) && event.getRepeatCount() == 0 && getChildCount() > 0) {
            keyPressed();
            handled = true;
        }
        if (!handled && action != 1) {
            switch (keyCode) {
                case 19:
                    if (event.hasNoModifiers()) {
                        handled = resurrectSelectionIfNeeded();
                        if (!handled) {
                            while (true) {
                                count2 = count - 1;
                                if (count > 0 && arrowScroll(33)) {
                                    handled = true;
                                    count = count2;
                                }
                            }
                            count = count2;
                            break;
                        }
                    } else if (event.hasModifiers(2)) {
                        handled = resurrectSelectionIfNeeded() || fullScroll(33);
                        break;
                    }
                    break;
                case 20:
                    if (event.hasNoModifiers()) {
                        handled = resurrectSelectionIfNeeded();
                        if (!handled) {
                            while (true) {
                                count2 = count - 1;
                                if (count > 0 && arrowScroll(130)) {
                                    handled = true;
                                    count = count2;
                                }
                            }
                            count = count2;
                            break;
                        }
                    } else if (event.hasModifiers(2)) {
                        handled = resurrectSelectionIfNeeded() || fullScroll(130);
                        break;
                    }
                    break;
                case 21:
                    if (event.hasNoModifiers()) {
                        handled = handleHorizontalFocusWithinListItem(17);
                        break;
                    }
                    break;
                case 22:
                    if (event.hasNoModifiers()) {
                        handled = handleHorizontalFocusWithinListItem(66);
                        break;
                    }
                    break;
                case 61:
                    if (event.hasNoModifiers()) {
                        handled = resurrectSelectionIfNeeded() || arrowScroll(130);
                        break;
                    } else if (event.hasModifiers(1)) {
                        handled = resurrectSelectionIfNeeded() || arrowScroll(33);
                        break;
                    }
                    break;
                case 92:
                    if (event.hasNoModifiers()) {
                        handled = resurrectSelectionIfNeeded() || pageScroll(33);
                        break;
                    } else if (event.hasModifiers(2)) {
                        handled = resurrectSelectionIfNeeded() || fullScroll(33);
                        break;
                    }
                    break;
                case 93:
                    if (event.hasNoModifiers()) {
                        handled = resurrectSelectionIfNeeded() || pageScroll(130);
                        break;
                    } else if (event.hasModifiers(2)) {
                        handled = resurrectSelectionIfNeeded() || fullScroll(130);
                        break;
                    }
                    break;
                case 122:
                    if (event.hasNoModifiers()) {
                        handled = resurrectSelectionIfNeeded() || fullScroll(33);
                        break;
                    }
                    break;
                case 123:
                    if (event.hasNoModifiers()) {
                        handled = resurrectSelectionIfNeeded() || fullScroll(130);
                        break;
                    }
                    break;
            }
        }
        if (handled || sendToTextFilter(keyCode, count, event)) {
            return true;
        }
        switch (action) {
            case 0:
                return super.onKeyDown(keyCode, event);
            case 1:
                return super.onKeyUp(keyCode, event);
            case 2:
                return super.onKeyMultiple(keyCode, count, event);
            default:
                return false;
        }
    }

    synchronized boolean pageScroll(int direction) {
        int nextPage;
        boolean down;
        int position;
        if (direction == 33) {
            nextPage = Math.max(0, (this.mSelectedPosition - getChildCount()) - 1);
            down = false;
        } else if (direction != 130) {
            return false;
        } else {
            nextPage = Math.min(this.mItemCount - 1, (this.mSelectedPosition + getChildCount()) - 1);
            down = true;
        }
        if (nextPage < 0 || (position = lookForSelectablePositionAfter(this.mSelectedPosition, nextPage, down)) < 0) {
            return false;
        }
        this.mLayoutMode = 4;
        this.mSpecificTop = this.mPaddingTop + getVerticalFadingEdgeLength();
        if (down && position > this.mItemCount - getChildCount()) {
            this.mLayoutMode = 3;
        }
        if (!down && position < getChildCount()) {
            this.mLayoutMode = 1;
        }
        setSelectionInt(position);
        invokeOnItemScrollListener();
        if (!awakenScrollBars()) {
            invalidate();
        }
        return true;
    }

    synchronized boolean fullScroll(int direction) {
        int lastItem;
        boolean moved = false;
        if (direction == 33) {
            if (this.mSelectedPosition != 0) {
                int position = lookForSelectablePositionAfter(this.mSelectedPosition, 0, true);
                if (position >= 0) {
                    this.mLayoutMode = 1;
                    setSelectionInt(position);
                    invokeOnItemScrollListener();
                }
                moved = true;
            }
        } else if (direction == 130 && this.mSelectedPosition < (lastItem = this.mItemCount - 1)) {
            int position2 = lookForSelectablePositionAfter(this.mSelectedPosition, lastItem, false);
            if (position2 >= 0) {
                this.mLayoutMode = 3;
                setSelectionInt(position2);
                invokeOnItemScrollListener();
            }
            moved = true;
        }
        if (moved && !awakenScrollBars()) {
            awakenScrollBars();
            invalidate();
        }
        return moved;
    }

    private synchronized boolean handleHorizontalFocusWithinListItem(int direction) {
        View selectedView;
        if (direction != 17 && direction != 66) {
            throw new IllegalArgumentException("direction must be one of {View.FOCUS_LEFT, View.FOCUS_RIGHT}");
        }
        int numChildren = getChildCount();
        if (this.mItemsCanFocus && numChildren > 0 && this.mSelectedPosition != -1 && (selectedView = getSelectedView()) != null && selectedView.hasFocus() && (selectedView instanceof ViewGroup)) {
            View currentFocus = selectedView.findFocus();
            View nextFocus = FocusFinder.getInstance().findNextFocus((ViewGroup) selectedView, currentFocus, direction);
            if (nextFocus != null) {
                Rect focusedRect = this.mTempRect;
                if (currentFocus != null) {
                    currentFocus.getFocusedRect(focusedRect);
                    offsetDescendantRectToMyCoords(currentFocus, focusedRect);
                    offsetRectIntoDescendantCoords(nextFocus, focusedRect);
                } else {
                    focusedRect = null;
                }
                if (nextFocus.requestFocus(direction, focusedRect)) {
                    return true;
                }
            }
            View globalNextFocus = FocusFinder.getInstance().findNextFocus((ViewGroup) getRootView(), currentFocus, direction);
            if (globalNextFocus != null) {
                return isViewAncestorOf(globalNextFocus, this);
            }
            return false;
        }
        return false;
    }

    public private protected boolean arrowScroll(int direction) {
        try {
            this.mInLayout = true;
            boolean handled = arrowScrollImpl(direction);
            if (handled) {
                playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
            }
            return handled;
        } finally {
            this.mInLayout = false;
        }
    }

    private final synchronized int nextSelectedPositionForDirection(View selectedView, int selectedPos, int direction) {
        int i;
        int nextSelected;
        if (direction == 130) {
            int listBottom = getHeight() - this.mListPadding.bottom;
            if (selectedView == null || selectedView.getBottom() > listBottom) {
                return -1;
            }
            if (selectedPos != -1 && selectedPos >= this.mFirstPosition) {
                nextSelected = selectedPos + 1;
            } else {
                nextSelected = this.mFirstPosition;
            }
        } else {
            int listTop = this.mListPadding.top;
            if (selectedView == null || selectedView.getTop() < listTop) {
                return -1;
            }
            int lastPos = (this.mFirstPosition + getChildCount()) - 1;
            if (selectedPos != -1 && selectedPos <= lastPos) {
                i = selectedPos - 1;
            } else {
                i = lastPos;
            }
            nextSelected = i;
        }
        int listTop2 = nextSelected;
        if (listTop2 < 0 || listTop2 >= this.mAdapter.getCount()) {
            return -1;
        }
        return lookForSelectablePosition(listTop2, direction == 130);
    }

    private synchronized boolean arrowScrollImpl(int direction) {
        View focused;
        View focused2;
        if (getChildCount() <= 0) {
            return false;
        }
        View selectedView = getSelectedView();
        int selectedPos = this.mSelectedPosition;
        int nextSelectedPosition = nextSelectedPositionForDirection(selectedView, selectedPos, direction);
        int amountToScroll = amountToScroll(direction, nextSelectedPosition);
        ArrowScrollFocusResult focusResult = this.mItemsCanFocus ? arrowScrollFocused(direction) : null;
        if (focusResult != null) {
            nextSelectedPosition = focusResult.getSelectedPosition();
            amountToScroll = focusResult.getAmountToScroll();
        }
        boolean needToRedraw = focusResult != null;
        if (nextSelectedPosition != -1) {
            handleNewSelectionChange(selectedView, direction, nextSelectedPosition, focusResult != null);
            setSelectedPositionInt(nextSelectedPosition);
            setNextSelectedPositionInt(nextSelectedPosition);
            selectedView = getSelectedView();
            selectedPos = nextSelectedPosition;
            if (this.mItemsCanFocus && focusResult == null && (focused2 = getFocusedChild()) != null) {
                focused2.clearFocus();
            }
            needToRedraw = true;
            checkSelectionChanged();
        }
        if (amountToScroll > 0) {
            scrollListItemsBy(direction == 33 ? amountToScroll : -amountToScroll);
            needToRedraw = true;
        }
        if (this.mItemsCanFocus && focusResult == null && selectedView != null && selectedView.hasFocus() && (focused = selectedView.findFocus()) != null && (!isViewAncestorOf(focused, this) || distanceToView(focused) > 0)) {
            focused.clearFocus();
        }
        if (nextSelectedPosition == -1 && selectedView != null && !isViewAncestorOf(selectedView, this)) {
            selectedView = null;
            hideSelector();
            this.mResurrectToPosition = -1;
        }
        if (needToRedraw) {
            if (selectedView != null) {
                positionSelectorLikeFocus(selectedPos, selectedView);
                this.mSelectedTop = selectedView.getTop();
            }
            if (!awakenScrollBars()) {
                invalidate();
            }
            invokeOnItemScrollListener();
            return true;
        }
        return false;
    }

    private synchronized void handleNewSelectionChange(View selectedView, int direction, int newSelectedPosition, boolean newFocusAssigned) {
        int topViewIndex;
        int bottomViewIndex;
        View topView;
        View bottomView;
        if (newSelectedPosition == -1) {
            throw new IllegalArgumentException("newSelectedPosition needs to be valid");
        }
        boolean topSelected = false;
        int selectedIndex = this.mSelectedPosition - this.mFirstPosition;
        int nextSelectedIndex = newSelectedPosition - this.mFirstPosition;
        if (direction == 33) {
            topViewIndex = nextSelectedIndex;
            bottomViewIndex = selectedIndex;
            topView = getChildAt(topViewIndex);
            bottomView = selectedView;
            topSelected = true;
        } else {
            topViewIndex = selectedIndex;
            bottomViewIndex = nextSelectedIndex;
            topView = selectedView;
            bottomView = getChildAt(bottomViewIndex);
        }
        int numChildren = getChildCount();
        boolean z = true;
        if (topView != null) {
            topView.setSelected(!newFocusAssigned && topSelected);
            measureAndAdjustDown(topView, topViewIndex, numChildren);
        }
        if (bottomView != null) {
            if (newFocusAssigned || topSelected) {
                z = false;
            }
            bottomView.setSelected(z);
            measureAndAdjustDown(bottomView, bottomViewIndex, numChildren);
        }
    }

    private synchronized void measureAndAdjustDown(View child, int childIndex, int numChildren) {
        int oldHeight = child.getHeight();
        measureItem(child);
        if (child.getMeasuredHeight() != oldHeight) {
            relayoutMeasuredItem(child);
            int heightDelta = child.getMeasuredHeight() - oldHeight;
            for (int i = childIndex + 1; i < numChildren; i++) {
                getChildAt(i).offsetTopAndBottom(heightDelta);
            }
        }
    }

    private synchronized void measureItem(View child) {
        int childHeightSpec;
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(-1, -2);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(this.mWidthMeasureSpec, this.mListPadding.left + this.mListPadding.right, p.width);
        int lpHeight = p.height;
        if (lpHeight > 0) {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, 1073741824);
        } else {
            int childHeightSpec2 = getMeasuredHeight();
            childHeightSpec = View.MeasureSpec.makeSafeMeasureSpec(childHeightSpec2, 0);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    private synchronized void relayoutMeasuredItem(View child) {
        int w = child.getMeasuredWidth();
        int h = child.getMeasuredHeight();
        int childLeft = this.mListPadding.left;
        int childRight = childLeft + w;
        int childTop = child.getTop();
        int childBottom = childTop + h;
        child.layout(childLeft, childTop, childRight, childBottom);
    }

    private synchronized int getArrowScrollPreviewLength() {
        return Math.max(2, getVerticalFadingEdgeLength());
    }

    private synchronized int amountToScroll(int direction, int nextSelectedPosition) {
        int listBottom = getHeight() - this.mListPadding.bottom;
        int listTop = this.mListPadding.top;
        int numChildren = getChildCount();
        if (direction == 130) {
            int indexToMakeVisible = numChildren - 1;
            if (nextSelectedPosition != -1) {
                indexToMakeVisible = nextSelectedPosition - this.mFirstPosition;
            }
            while (numChildren <= indexToMakeVisible) {
                addViewBelow(getChildAt(numChildren - 1), (this.mFirstPosition + numChildren) - 1);
                numChildren++;
            }
            int positionToMakeVisible = this.mFirstPosition + indexToMakeVisible;
            View viewToMakeVisible = getChildAt(indexToMakeVisible);
            int goalBottom = listBottom;
            if (positionToMakeVisible < this.mItemCount - 1) {
                goalBottom -= getArrowScrollPreviewLength();
            }
            if (viewToMakeVisible.getBottom() <= goalBottom) {
                return 0;
            }
            if (nextSelectedPosition == -1 || goalBottom - viewToMakeVisible.getTop() < getMaxScrollAmount()) {
                int amountToScroll = viewToMakeVisible.getBottom() - goalBottom;
                if (this.mFirstPosition + numChildren == this.mItemCount) {
                    int max = getChildAt(numChildren - 1).getBottom() - listBottom;
                    amountToScroll = Math.min(amountToScroll, max);
                }
                int max2 = getMaxScrollAmount();
                return Math.min(amountToScroll, max2);
            }
            return 0;
        }
        int indexToMakeVisible2 = 0;
        if (nextSelectedPosition != -1) {
            indexToMakeVisible2 = nextSelectedPosition - this.mFirstPosition;
        }
        while (indexToMakeVisible2 < 0) {
            addViewAbove(getChildAt(0), this.mFirstPosition);
            this.mFirstPosition--;
            indexToMakeVisible2 = nextSelectedPosition - this.mFirstPosition;
        }
        int positionToMakeVisible2 = this.mFirstPosition + indexToMakeVisible2;
        View viewToMakeVisible2 = getChildAt(indexToMakeVisible2);
        int goalTop = listTop;
        if (positionToMakeVisible2 > 0) {
            goalTop += getArrowScrollPreviewLength();
        }
        if (viewToMakeVisible2.getTop() >= goalTop) {
            return 0;
        }
        if (nextSelectedPosition == -1 || viewToMakeVisible2.getBottom() - goalTop < getMaxScrollAmount()) {
            int amountToScroll2 = goalTop - viewToMakeVisible2.getTop();
            if (this.mFirstPosition == 0) {
                int max3 = listTop - getChildAt(0).getTop();
                amountToScroll2 = Math.min(amountToScroll2, max3);
            }
            int max4 = getMaxScrollAmount();
            return Math.min(amountToScroll2, max4);
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class ArrowScrollFocusResult {
        private int mAmountToScroll;
        private int mSelectedPosition;

        private synchronized ArrowScrollFocusResult() {
        }

        synchronized void populate(int selectedPosition, int amountToScroll) {
            this.mSelectedPosition = selectedPosition;
            this.mAmountToScroll = amountToScroll;
        }

        public synchronized int getSelectedPosition() {
            return this.mSelectedPosition;
        }

        public synchronized int getAmountToScroll() {
            return this.mAmountToScroll;
        }
    }

    private synchronized int lookForSelectablePositionOnScreen(int direction) {
        int startPos;
        int firstPosition = this.mFirstPosition;
        if (direction == 130) {
            if (this.mSelectedPosition != -1) {
                startPos = this.mSelectedPosition + 1;
            } else {
                startPos = firstPosition;
            }
            if (startPos >= this.mAdapter.getCount()) {
                return -1;
            }
            if (startPos < firstPosition) {
                startPos = firstPosition;
            }
            int lastVisiblePos = getLastVisiblePosition();
            ListAdapter adapter = getAdapter();
            for (int pos = startPos; pos <= lastVisiblePos; pos++) {
                if (adapter.isEnabled(pos) && getChildAt(pos - firstPosition).getVisibility() == 0) {
                    return pos;
                }
            }
        } else {
            int last = (getChildCount() + firstPosition) - 1;
            int startPos2 = this.mSelectedPosition != -1 ? this.mSelectedPosition - 1 : (getChildCount() + firstPosition) - 1;
            if (startPos2 < 0 || startPos2 >= this.mAdapter.getCount()) {
                return -1;
            }
            if (startPos2 > last) {
                startPos2 = last;
            }
            ListAdapter adapter2 = getAdapter();
            for (int pos2 = startPos2; pos2 >= firstPosition; pos2--) {
                if (adapter2.isEnabled(pos2) && getChildAt(pos2 - firstPosition).getVisibility() == 0) {
                    return pos2;
                }
            }
        }
        return -1;
    }

    private synchronized ArrowScrollFocusResult arrowScrollFocused(int direction) {
        boolean topFadingEdgeShowing;
        int ySearchPoint;
        View oldFocus;
        int ySearchPoint2;
        int selectablePosition;
        View selectedView = getSelectedView();
        if (selectedView == null || !selectedView.hasFocus()) {
            if (direction != 130) {
                topFadingEdgeShowing = (this.mFirstPosition + getChildCount()) - 1 < this.mItemCount;
                int listBottom = (getHeight() - this.mListPadding.bottom) - (topFadingEdgeShowing ? getArrowScrollPreviewLength() : 0);
                if (selectedView != null && selectedView.getBottom() < listBottom) {
                    ySearchPoint = selectedView.getBottom();
                } else {
                    ySearchPoint = listBottom;
                }
                this.mTempRect.set(0, ySearchPoint, 0, ySearchPoint);
            } else {
                topFadingEdgeShowing = this.mFirstPosition > 0;
                int listTop = this.mListPadding.top + (topFadingEdgeShowing ? getArrowScrollPreviewLength() : 0);
                if (selectedView != null && selectedView.getTop() > listTop) {
                    ySearchPoint2 = selectedView.getTop();
                } else {
                    ySearchPoint2 = listTop;
                }
                this.mTempRect.set(0, ySearchPoint2, 0, ySearchPoint2);
            }
            oldFocus = FocusFinder.getInstance().findNextFocusFromRect(this, this.mTempRect, direction);
        } else {
            View oldFocus2 = selectedView.findFocus();
            oldFocus = FocusFinder.getInstance().findNextFocus(this, oldFocus2, direction);
        }
        if (oldFocus != null) {
            int positionOfNewFocus = positionOfNewFocus(oldFocus);
            if (this.mSelectedPosition != -1 && positionOfNewFocus != this.mSelectedPosition && (selectablePosition = lookForSelectablePositionOnScreen(direction)) != -1 && ((direction == 130 && selectablePosition < positionOfNewFocus) || (direction == 33 && selectablePosition > positionOfNewFocus))) {
                return null;
            }
            int focusScroll = amountToScrollToNewFocus(direction, oldFocus, positionOfNewFocus);
            int maxScrollAmount = getMaxScrollAmount();
            if (focusScroll < maxScrollAmount) {
                oldFocus.requestFocus(direction);
                this.mArrowScrollFocusResult.populate(positionOfNewFocus, focusScroll);
                return this.mArrowScrollFocusResult;
            } else if (distanceToView(oldFocus) < maxScrollAmount) {
                oldFocus.requestFocus(direction);
                this.mArrowScrollFocusResult.populate(positionOfNewFocus, maxScrollAmount);
                return this.mArrowScrollFocusResult;
            }
        }
        return null;
    }

    private synchronized int positionOfNewFocus(View newFocus) {
        int numChildren = getChildCount();
        for (int i = 0; i < numChildren; i++) {
            View child = getChildAt(i);
            if (isViewAncestorOf(newFocus, child)) {
                return this.mFirstPosition + i;
            }
        }
        throw new IllegalArgumentException("newFocus is not a child of any of the children of the list!");
    }

    private synchronized boolean isViewAncestorOf(View child, View parent) {
        if (child == parent) {
            return true;
        }
        ViewParent theParent = child.getParent();
        return (theParent instanceof ViewGroup) && isViewAncestorOf((View) theParent, parent);
    }

    private synchronized int amountToScrollToNewFocus(int direction, View newFocus, int positionOfNewFocus) {
        newFocus.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(newFocus, this.mTempRect);
        if (direction == 33) {
            if (this.mTempRect.top >= this.mListPadding.top) {
                return 0;
            }
            int amountToScroll = this.mListPadding.top - this.mTempRect.top;
            if (positionOfNewFocus > 0) {
                return amountToScroll + getArrowScrollPreviewLength();
            }
            return amountToScroll;
        }
        int listBottom = getHeight() - this.mListPadding.bottom;
        if (this.mTempRect.bottom <= listBottom) {
            return 0;
        }
        int amountToScroll2 = this.mTempRect.bottom - listBottom;
        if (positionOfNewFocus < this.mItemCount - 1) {
            return amountToScroll2 + getArrowScrollPreviewLength();
        }
        return amountToScroll2;
    }

    private synchronized int distanceToView(View descendant) {
        descendant.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(descendant, this.mTempRect);
        int listBottom = (this.mBottom - this.mTop) - this.mListPadding.bottom;
        if (this.mTempRect.bottom < this.mListPadding.top) {
            int distance = this.mListPadding.top - this.mTempRect.bottom;
            return distance;
        } else if (this.mTempRect.top <= listBottom) {
            return 0;
        } else {
            int distance2 = this.mTempRect.top - listBottom;
            return distance2;
        }
    }

    public protected void scrollListItemsBy(int amount) {
        int lastVisiblePosition;
        offsetChildrenTopAndBottom(amount);
        int listBottom = getHeight() - this.mListPadding.bottom;
        int listTop = this.mListPadding.top;
        AbsListView.RecycleBin recycleBin = this.mRecycler;
        if (amount < 0) {
            int numChildren = getChildCount();
            View last = getChildAt(numChildren - 1);
            while (last.getBottom() < listBottom && (this.mFirstPosition + numChildren) - 1 < this.mItemCount - 1) {
                last = addViewBelow(last, lastVisiblePosition);
                numChildren++;
            }
            if (last.getBottom() < listBottom) {
                offsetChildrenTopAndBottom(listBottom - last.getBottom());
            }
            View first = getChildAt(0);
            while (first.getBottom() < listTop) {
                AbsListView.LayoutParams layoutParams = (AbsListView.LayoutParams) first.getLayoutParams();
                if (recycleBin.shouldRecycleViewType(layoutParams.viewType)) {
                    recycleBin.addScrapView(first, this.mFirstPosition);
                }
                detachViewFromParent(first);
                first = getChildAt(0);
                this.mFirstPosition++;
            }
        } else {
            View first2 = getChildAt(0);
            while (first2.getTop() > listTop && this.mFirstPosition > 0) {
                first2 = addViewAbove(first2, this.mFirstPosition);
                this.mFirstPosition--;
            }
            if (first2.getTop() > listTop) {
                offsetChildrenTopAndBottom(listTop - first2.getTop());
            }
            int lastIndex = getChildCount() - 1;
            View last2 = getChildAt(lastIndex);
            while (last2.getTop() > listBottom) {
                AbsListView.LayoutParams layoutParams2 = (AbsListView.LayoutParams) last2.getLayoutParams();
                if (recycleBin.shouldRecycleViewType(layoutParams2.viewType)) {
                    recycleBin.addScrapView(last2, this.mFirstPosition + lastIndex);
                }
                detachViewFromParent(last2);
                lastIndex--;
                last2 = getChildAt(lastIndex);
            }
        }
        recycleBin.fullyDetachScrapViews();
        removeUnusedFixedViews(this.mHeaderViewInfos);
        removeUnusedFixedViews(this.mFooterViewInfos);
    }

    private synchronized View addViewAbove(View theView, int position) {
        int abovePosition = position - 1;
        View view = obtainView(abovePosition, this.mIsScrap);
        int edgeOfNewChild = theView.getTop() - this.mDividerHeight;
        setupChild(view, abovePosition, edgeOfNewChild, false, this.mListPadding.left, false, this.mIsScrap[0]);
        return view;
    }

    private synchronized View addViewBelow(View theView, int position) {
        int belowPosition = position + 1;
        View view = obtainView(belowPosition, this.mIsScrap);
        int edgeOfNewChild = theView.getBottom() + this.mDividerHeight;
        setupChild(view, belowPosition, edgeOfNewChild, true, this.mListPadding.left, false, this.mIsScrap[0]);
        return view;
    }

    public void setItemsCanFocus(boolean itemsCanFocus) {
        this.mItemsCanFocus = itemsCanFocus;
        if (!itemsCanFocus) {
            setDescendantFocusability(393216);
        }
    }

    public boolean getItemsCanFocus() {
        return this.mItemsCanFocus;
    }

    @Override // android.view.View
    public boolean isOpaque() {
        boolean retValue = (this.mCachingActive && this.mIsCacheColorOpaque && this.mDividerIsOpaque && hasOpaqueScrollbars()) || super.isOpaque();
        if (retValue) {
            int listTop = this.mListPadding != null ? this.mListPadding.top : this.mPaddingTop;
            View first = getChildAt(0);
            if (first == null || first.getTop() > listTop) {
                return false;
            }
            int listBottom = getHeight() - (this.mListPadding != null ? this.mListPadding.bottom : this.mPaddingBottom);
            View last = getChildAt(getChildCount() - 1);
            if (last == null || last.getBottom() < listBottom) {
                return false;
            }
        }
        return retValue;
    }

    @Override // android.widget.AbsListView
    public void setCacheColorHint(int color) {
        boolean opaque = (color >>> 24) == 255;
        this.mIsCacheColorOpaque = opaque;
        if (opaque) {
            if (this.mDividerPaint == null) {
                this.mDividerPaint = new Paint();
            }
            this.mDividerPaint.setColor(color);
        }
        super.setCacheColorHint(color);
    }

    synchronized void drawOverscrollHeader(Canvas canvas, Drawable drawable, Rect bounds) {
        int height = drawable.getMinimumHeight();
        canvas.save();
        canvas.clipRect(bounds);
        int span = bounds.bottom - bounds.top;
        if (span < height) {
            bounds.top = bounds.bottom - height;
        }
        drawable.setBounds(bounds);
        drawable.draw(canvas);
        canvas.restore();
    }

    synchronized void drawOverscrollFooter(Canvas canvas, Drawable drawable, Rect bounds) {
        int height = drawable.getMinimumHeight();
        canvas.save();
        canvas.clipRect(bounds);
        int span = bounds.bottom - bounds.top;
        if (span < height) {
            bounds.bottom = bounds.top + height;
        }
        drawable.setBounds(bounds);
        drawable.draw(canvas);
        canvas.restore();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.AbsListView, android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        ListAdapter adapter;
        int itemCount;
        int effectivePaddingBottom;
        int effectivePaddingTop;
        Drawable overscrollHeader;
        int i;
        int first;
        Drawable overscrollFooter;
        boolean footerDividers;
        int effectivePaddingTop2;
        int start;
        int bottom;
        Drawable overscrollFooter2;
        boolean drawOverscrollHeader;
        boolean drawDividers;
        int listBottom;
        ListAdapter adapter2;
        Paint paint;
        if (this.mCachingStarted) {
            this.mCachingActive = true;
        }
        int dividerHeight = this.mDividerHeight;
        Drawable overscrollHeader2 = this.mOverScrollHeader;
        Drawable overscrollFooter3 = this.mOverScrollFooter;
        boolean drawOverscrollHeader2 = overscrollHeader2 != null;
        boolean drawOverscrollFooter = overscrollFooter3 != null;
        boolean drawDividers2 = dividerHeight > 0 && this.mDivider != null;
        if (drawDividers2 || drawOverscrollHeader2 || drawOverscrollFooter) {
            Rect bounds = this.mTempRect;
            bounds.left = this.mPaddingLeft;
            bounds.right = (this.mRight - this.mLeft) - this.mPaddingRight;
            int count = getChildCount();
            int headerCount = getHeaderViewsCount();
            int itemCount2 = this.mItemCount;
            int footerLimit = itemCount2 - this.mFooterViewInfos.size();
            boolean headerDividers = this.mHeaderDividersEnabled;
            boolean footerDividers2 = this.mFooterDividersEnabled;
            int first2 = this.mFirstPosition;
            boolean z = this.mAreAllItemsSelectable;
            ListAdapter adapter3 = this.mAdapter;
            boolean fillForMissingDividers = isOpaque() && !super.isOpaque();
            if (fillForMissingDividers) {
                itemCount = itemCount2;
                if (this.mDividerPaint == null && this.mIsCacheColorOpaque) {
                    this.mDividerPaint = new Paint();
                    adapter = adapter3;
                    this.mDividerPaint.setColor(getCacheColorHint());
                } else {
                    adapter = adapter3;
                }
            } else {
                adapter = adapter3;
                itemCount = itemCount2;
            }
            Paint paint2 = this.mDividerPaint;
            int effectivePaddingTop3 = this.mGroupFlags;
            Paint paint3 = paint2;
            if ((effectivePaddingTop3 & 34) == 34) {
                effectivePaddingTop = this.mListPadding.top;
                effectivePaddingBottom = this.mListPadding.bottom;
            } else {
                effectivePaddingBottom = 0;
                effectivePaddingTop = 0;
            }
            int effectivePaddingTop4 = effectivePaddingTop;
            int effectivePaddingTop5 = this.mBottom;
            boolean drawOverscrollFooter2 = drawOverscrollFooter;
            int listBottom2 = ((effectivePaddingTop5 - this.mTop) - effectivePaddingBottom) + this.mScrollY;
            if (this.mStackFromBottom) {
                boolean drawOverscrollHeader3 = drawOverscrollHeader2;
                boolean drawDividers3 = drawDividers2;
                Drawable overscrollFooter4 = overscrollFooter3;
                ListAdapter adapter4 = adapter;
                int scrollY = this.mScrollY;
                if (count <= 0 || !drawOverscrollHeader3) {
                    overscrollHeader = overscrollHeader2;
                    i = 0;
                } else {
                    bounds.top = scrollY;
                    i = 0;
                    bounds.bottom = getChildAt(0).getTop();
                    overscrollHeader = overscrollHeader2;
                    drawOverscrollHeader(canvas, overscrollHeader, bounds);
                }
                int i2 = drawOverscrollHeader3 ? 1 : i;
                int start2 = i2;
                while (true) {
                    int i3 = i2;
                    if (i3 >= count) {
                        break;
                    }
                    Drawable overscrollHeader3 = overscrollHeader;
                    int itemIndex = first2 + i3;
                    boolean isHeader = itemIndex < headerCount;
                    boolean isFooter = itemIndex >= footerLimit;
                    if ((!headerDividers && isHeader) || (!footerDividers2 && isFooter)) {
                        footerDividers = footerDividers2;
                        first = first2;
                        overscrollFooter = overscrollFooter4;
                        effectivePaddingTop2 = effectivePaddingTop4;
                        start = start2;
                    } else {
                        first = first2;
                        View child = getChildAt(i3);
                        overscrollFooter = overscrollFooter4;
                        int top = child.getTop();
                        if (drawDividers3) {
                            int effectivePaddingTop6 = effectivePaddingTop4;
                            if (top <= effectivePaddingTop6) {
                                footerDividers = footerDividers2;
                                effectivePaddingTop2 = effectivePaddingTop6;
                                start = start2;
                            } else {
                                effectivePaddingTop2 = effectivePaddingTop6;
                                int effectivePaddingTop7 = start2;
                                boolean isFirstItem = i3 == effectivePaddingTop7;
                                start = effectivePaddingTop7;
                                int start3 = itemIndex - 1;
                                if (adapter4.isEnabled(itemIndex)) {
                                    if ((headerDividers || (!isHeader && start3 >= headerCount)) && (isFirstItem || (adapter4.isEnabled(start3) && (footerDividers2 || (!isFooter && start3 < footerLimit))))) {
                                        footerDividers = footerDividers2;
                                        bounds.top = top - dividerHeight;
                                        bounds.bottom = top;
                                        drawDivider(canvas, bounds, i3 - 1);
                                    } else {
                                        footerDividers = footerDividers2;
                                    }
                                } else {
                                    footerDividers = footerDividers2;
                                }
                                if (fillForMissingDividers) {
                                    bounds.top = top - dividerHeight;
                                    bounds.bottom = top;
                                    canvas.drawRect(bounds, paint3);
                                }
                            }
                        } else {
                            footerDividers = footerDividers2;
                            effectivePaddingTop2 = effectivePaddingTop4;
                            start = start2;
                        }
                    }
                    i2 = i3 + 1;
                    overscrollHeader = overscrollHeader3;
                    first2 = first;
                    overscrollFooter4 = overscrollFooter;
                    effectivePaddingTop4 = effectivePaddingTop2;
                    start2 = start;
                    footerDividers2 = footerDividers;
                }
                Drawable overscrollFooter5 = overscrollFooter4;
                if (count > 0 && scrollY > 0) {
                    if (drawOverscrollFooter2) {
                        int absListBottom = this.mBottom;
                        bounds.top = absListBottom;
                        bounds.bottom = absListBottom + scrollY;
                        drawOverscrollFooter(canvas, overscrollFooter5, bounds);
                    } else if (drawDividers3) {
                        bounds.top = listBottom2;
                        bounds.bottom = listBottom2 + dividerHeight;
                        drawDivider(canvas, bounds, -1);
                    }
                }
            } else {
                int scrollY2 = this.mScrollY;
                if (count > 0 && scrollY2 < 0) {
                    if (drawOverscrollHeader2) {
                        bottom = 0;
                        bounds.bottom = 0;
                        bounds.top = scrollY2;
                        drawOverscrollHeader(canvas, overscrollHeader2, bounds);
                    } else {
                        bottom = 0;
                        if (drawDividers2) {
                            bounds.bottom = 0;
                            bounds.top = -dividerHeight;
                            drawDivider(canvas, bounds, -1);
                        }
                    }
                } else {
                    bottom = 0;
                }
                int bottom2 = bottom;
                int i4 = 0;
                while (i4 < count) {
                    Drawable overscrollHeader4 = overscrollHeader2;
                    int itemIndex2 = first2 + i4;
                    boolean isHeader2 = itemIndex2 < headerCount;
                    boolean isFooter2 = itemIndex2 >= footerLimit;
                    if ((!headerDividers && isHeader2) || (!footerDividers2 && isFooter2)) {
                        drawOverscrollHeader = drawOverscrollHeader2;
                    } else {
                        drawOverscrollHeader = drawOverscrollHeader2;
                        View child2 = getChildAt(i4);
                        bottom2 = child2.getBottom();
                        boolean isLastItem = i4 == count + (-1);
                        if (!drawDividers2 || bottom2 >= listBottom2) {
                            drawDividers = drawDividers2;
                            listBottom = listBottom2;
                            adapter2 = adapter;
                            paint = paint3;
                        } else if (!drawOverscrollFooter2 || !isLastItem) {
                            listBottom = listBottom2;
                            int listBottom3 = itemIndex2 + 1;
                            drawDividers = drawDividers2;
                            adapter2 = adapter;
                            if (adapter2.isEnabled(itemIndex2)) {
                                if ((headerDividers || (!isHeader2 && listBottom3 >= headerCount)) && (isLastItem || (adapter2.isEnabled(listBottom3) && (footerDividers2 || (!isFooter2 && listBottom3 < footerLimit))))) {
                                    bounds.top = bottom2;
                                    bounds.bottom = bottom2 + dividerHeight;
                                    drawDivider(canvas, bounds, i4);
                                    paint = paint3;
                                }
                            }
                            if (!fillForMissingDividers) {
                                paint = paint3;
                            } else {
                                bounds.top = bottom2;
                                bounds.bottom = bottom2 + dividerHeight;
                                paint = paint3;
                                canvas.drawRect(bounds, paint);
                            }
                        }
                        i4++;
                        paint3 = paint;
                        adapter = adapter2;
                        overscrollHeader2 = overscrollHeader4;
                        drawOverscrollHeader2 = drawOverscrollHeader;
                        listBottom2 = listBottom;
                        drawDividers2 = drawDividers;
                    }
                    drawDividers = drawDividers2;
                    listBottom = listBottom2;
                    adapter2 = adapter;
                    paint = paint3;
                    i4++;
                    paint3 = paint;
                    adapter = adapter2;
                    overscrollHeader2 = overscrollHeader4;
                    drawOverscrollHeader2 = drawOverscrollHeader;
                    listBottom2 = listBottom;
                    drawDividers2 = drawDividers;
                }
                int overFooterBottom = this.mBottom + this.mScrollY;
                if (!drawOverscrollFooter2) {
                    overscrollFooter2 = overscrollFooter3;
                } else if (first2 + count == itemCount && overFooterBottom > bottom2) {
                    bounds.top = bottom2;
                    bounds.bottom = overFooterBottom;
                    overscrollFooter2 = overscrollFooter3;
                    drawOverscrollFooter(canvas, overscrollFooter2, bounds);
                } else {
                    overscrollFooter2 = overscrollFooter3;
                }
            }
        }
        super.dispatchDraw(canvas);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean more = super.drawChild(canvas, child, drawingTime);
        if (this.mCachingActive && child.mCachingFailed) {
            this.mCachingActive = false;
        }
        return more;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void drawDivider(Canvas canvas, Rect bounds, int childIndex) {
        Drawable divider = this.mDivider;
        divider.setBounds(bounds);
        divider.draw(canvas);
    }

    public Drawable getDivider() {
        return this.mDivider;
    }

    public void setDivider(Drawable divider) {
        boolean z = false;
        if (divider != null) {
            this.mDividerHeight = divider.getIntrinsicHeight();
        } else {
            this.mDividerHeight = 0;
        }
        this.mDivider = divider;
        this.mDividerIsOpaque = (divider == null || divider.getOpacity() == -1) ? true : true;
        requestLayout();
        invalidate();
    }

    public int getDividerHeight() {
        return this.mDividerHeight;
    }

    public void setDividerHeight(int height) {
        this.mDividerHeight = height;
        requestLayout();
        invalidate();
    }

    public void setHeaderDividersEnabled(boolean headerDividersEnabled) {
        this.mHeaderDividersEnabled = headerDividersEnabled;
        invalidate();
    }

    public boolean areHeaderDividersEnabled() {
        return this.mHeaderDividersEnabled;
    }

    public void setFooterDividersEnabled(boolean footerDividersEnabled) {
        this.mFooterDividersEnabled = footerDividersEnabled;
        invalidate();
    }

    public boolean areFooterDividersEnabled() {
        return this.mFooterDividersEnabled;
    }

    public void setOverscrollHeader(Drawable header) {
        this.mOverScrollHeader = header;
        if (this.mScrollY < 0) {
            invalidate();
        }
    }

    public Drawable getOverscrollHeader() {
        return this.mOverScrollHeader;
    }

    public void setOverscrollFooter(Drawable footer) {
        this.mOverScrollFooter = footer;
        invalidate();
    }

    public Drawable getOverscrollFooter() {
        return this.mOverScrollFooter;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.AbsListView, android.view.View
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        ListAdapter adapter = this.mAdapter;
        int closetChildIndex = -1;
        int closestChildTop = 0;
        if (adapter != null && gainFocus && previouslyFocusedRect != null) {
            previouslyFocusedRect.offset(this.mScrollX, this.mScrollY);
            if (adapter.getCount() < getChildCount() + this.mFirstPosition) {
                this.mLayoutMode = 0;
                layoutChildren();
            }
            Rect otherRect = this.mTempRect;
            int minDistance = Integer.MAX_VALUE;
            int childCount = getChildCount();
            int firstPosition = this.mFirstPosition;
            for (int i = 0; i < childCount; i++) {
                if (adapter.isEnabled(firstPosition + i)) {
                    View other = getChildAt(i);
                    other.getDrawingRect(otherRect);
                    offsetDescendantRectToMyCoords(other, otherRect);
                    int distance = getDistance(previouslyFocusedRect, otherRect, direction);
                    if (distance < minDistance) {
                        minDistance = distance;
                        closetChildIndex = i;
                        closestChildTop = other.getTop();
                    }
                }
            }
        }
        if (closetChildIndex >= 0) {
            setSelectionFromTop(this.mFirstPosition + closetChildIndex, closestChildTop);
        } else {
            requestLayout();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        int count = getChildCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                addHeaderView(getChildAt(i));
            }
            removeAllViews();
        }
    }

    public private <T extends View> T findViewTraversal(int id) {
        T t = (T) super.findViewTraversal(id);
        if (t == null) {
            View v = findViewInHeadersOrFooters(this.mHeaderViewInfos, id);
            T t2 = (T) v;
            if (t2 != null) {
                return t2;
            }
            View v2 = findViewInHeadersOrFooters(this.mFooterViewInfos, id);
            t = (T) v2;
            if (t != null) {
                return t;
            }
        }
        return t;
    }

    synchronized View findViewInHeadersOrFooters(ArrayList<FixedViewInfo> where, int id) {
        View v;
        if (where != null) {
            int len = where.size();
            for (int i = 0; i < len; i++) {
                View v2 = where.get(i).view;
                if (!v2.isRootNamespace() && (v = v2.findViewById(id)) != null) {
                    return v;
                }
            }
            return null;
        }
        return null;
    }

    public private <T extends View> T findViewWithTagTraversal(Object tag) {
        T t = (T) super.findViewWithTagTraversal(tag);
        if (t == null) {
            View v = findViewWithTagInHeadersOrFooters(this.mHeaderViewInfos, tag);
            T t2 = (T) v;
            if (t2 != null) {
                return t2;
            }
            View v2 = findViewWithTagInHeadersOrFooters(this.mFooterViewInfos, tag);
            t = (T) v2;
            if (t != null) {
                return t;
            }
        }
        return t;
    }

    synchronized View findViewWithTagInHeadersOrFooters(ArrayList<FixedViewInfo> where, Object tag) {
        View v;
        if (where != null) {
            int len = where.size();
            for (int i = 0; i < len; i++) {
                View v2 = where.get(i).view;
                if (!v2.isRootNamespace() && (v = v2.findViewWithTag(tag)) != null) {
                    return v;
                }
            }
            return null;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public synchronized <T extends View> T findViewByPredicateTraversal(Predicate<View> predicate, View childToSkip) {
        T t = (T) super.findViewByPredicateTraversal(predicate, childToSkip);
        if (t == null) {
            View v = findViewByPredicateInHeadersOrFooters(this.mHeaderViewInfos, predicate, childToSkip);
            T t2 = (T) v;
            if (t2 != null) {
                return t2;
            }
            View v2 = findViewByPredicateInHeadersOrFooters(this.mFooterViewInfos, predicate, childToSkip);
            t = (T) v2;
            if (t != null) {
                return t;
            }
        }
        return t;
    }

    synchronized View findViewByPredicateInHeadersOrFooters(ArrayList<FixedViewInfo> where, Predicate<View> predicate, View childToSkip) {
        View v;
        if (where != null) {
            int len = where.size();
            for (int i = 0; i < len; i++) {
                View v2 = where.get(i).view;
                if (v2 != childToSkip && !v2.isRootNamespace() && (v = v2.findViewByPredicate(predicate)) != null) {
                    return v;
                }
            }
            return null;
        }
        return null;
    }

    @Deprecated
    public long[] getCheckItemIds() {
        if (this.mAdapter != null && this.mAdapter.hasStableIds()) {
            return getCheckedItemIds();
        }
        if (this.mChoiceMode != 0 && this.mCheckStates != null && this.mAdapter != null) {
            SparseBooleanArray states = this.mCheckStates;
            int count = states.size();
            long[] ids = new long[count];
            ListAdapter adapter = this.mAdapter;
            int checkedCount = 0;
            for (int checkedCount2 = 0; checkedCount2 < count; checkedCount2++) {
                if (states.valueAt(checkedCount2)) {
                    ids[checkedCount] = adapter.getItemId(states.keyAt(checkedCount2));
                    checkedCount++;
                }
            }
            if (checkedCount == count) {
                return ids;
            }
            long[] result = new long[checkedCount];
            System.arraycopy(ids, 0, result, 0, checkedCount);
            return result;
        }
        return new long[0];
    }

    public private protected int getHeightForPosition(int position) {
        int height = super.getHeightForPosition(position);
        if (shouldAdjustHeightForDivider(position)) {
            return this.mDividerHeight + height;
        }
        return height;
    }

    private synchronized boolean shouldAdjustHeightForDivider(int itemIndex) {
        int dividerHeight = this.mDividerHeight;
        Drawable overscrollHeader = this.mOverScrollHeader;
        Drawable overscrollFooter = this.mOverScrollFooter;
        boolean drawOverscrollHeader = overscrollHeader != null;
        boolean drawOverscrollFooter = overscrollFooter != null;
        boolean drawDividers = dividerHeight > 0 && this.mDivider != null;
        if (drawDividers) {
            boolean fillForMissingDividers = isOpaque() && !super.isOpaque();
            int itemCount = this.mItemCount;
            int headerCount = getHeaderViewsCount();
            int footerLimit = itemCount - this.mFooterViewInfos.size();
            boolean isHeader = itemIndex < headerCount;
            boolean isFooter = itemIndex >= footerLimit;
            boolean headerDividers = this.mHeaderDividersEnabled;
            boolean footerDividers = this.mFooterDividersEnabled;
            if ((headerDividers || !isHeader) && (footerDividers || !isFooter)) {
                ListAdapter adapter = this.mAdapter;
                if (!this.mStackFromBottom) {
                    boolean isLastItem = itemIndex == itemCount + (-1);
                    if (drawOverscrollFooter && isLastItem) {
                        return false;
                    }
                    int nextIndex = itemIndex + 1;
                    if (adapter.isEnabled(itemIndex) && (headerDividers || (!isHeader && nextIndex >= headerCount))) {
                        if (!isLastItem) {
                            if (adapter.isEnabled(nextIndex)) {
                                if (footerDividers) {
                                    return true;
                                }
                                if (!isFooter && nextIndex < footerLimit) {
                                    return true;
                                }
                            }
                        } else {
                            return true;
                        }
                    }
                    return fillForMissingDividers;
                }
                boolean isFirstItem = itemIndex == (drawOverscrollHeader ? 1 : 0);
                if (isFirstItem) {
                    return false;
                }
                int start = itemIndex - 1;
                if (adapter.isEnabled(itemIndex) && (headerDividers || (!isHeader && start >= headerCount))) {
                    if (!isFirstItem) {
                        if (adapter.isEnabled(start)) {
                            if (footerDividers) {
                                return true;
                            }
                            if (!isFooter && start < footerLimit) {
                                return true;
                            }
                        }
                    } else {
                        return true;
                    }
                }
                return fillForMissingDividers;
            }
            return false;
        }
        return false;
    }

    @Override // android.widget.AbsListView, android.widget.AdapterView, android.view.ViewGroup, android.view.View
    public CharSequence getAccessibilityClassName() {
        return ListView.class.getName();
    }

    @Override // android.widget.AbsListView, android.widget.AdapterView, android.view.View
    public synchronized void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        int rowsCount = getCount();
        int selectionMode = getSelectionModeForAccessibility();
        AccessibilityNodeInfo.CollectionInfo collectionInfo = AccessibilityNodeInfo.CollectionInfo.obtain(rowsCount, 1, false, selectionMode);
        info.setCollectionInfo(collectionInfo);
        if (rowsCount > 0) {
            info.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_TO_POSITION);
        }
    }

    @Override // android.widget.AbsListView
    public synchronized boolean performAccessibilityActionInternal(int action, Bundle arguments) {
        if (super.performAccessibilityActionInternal(action, arguments)) {
            return true;
        }
        if (action == 16908343) {
            int row = arguments.getInt(AccessibilityNodeInfo.ACTION_ARGUMENT_ROW_INT, -1);
            int position = Math.min(row, getCount() - 1);
            if (row >= 0) {
                smoothScrollToPosition(position);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override // android.widget.AbsListView
    public void onInitializeAccessibilityNodeInfoForItem(View view, int position, AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoForItem(view, position, info);
        AbsListView.LayoutParams lp = (AbsListView.LayoutParams) view.getLayoutParams();
        boolean isHeading = lp != null && lp.viewType == -2;
        boolean isSelected = isItemChecked(position);
        AccessibilityNodeInfo.CollectionItemInfo itemInfo = AccessibilityNodeInfo.CollectionItemInfo.obtain(position, 1, 0, 1, isHeading, isSelected);
        info.setCollectionItemInfo(itemInfo);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.AbsListView, android.widget.AdapterView, android.view.View
    public synchronized void encodeProperties(ViewHierarchyEncoder encoder) {
        super.encodeProperties(encoder);
        encoder.addProperty("recycleOnMeasure", recycleOnMeasure());
    }

    protected synchronized HeaderViewListAdapter wrapHeaderListAdapterInternal(ArrayList<FixedViewInfo> headerViewInfos, ArrayList<FixedViewInfo> footerViewInfos, ListAdapter adapter) {
        return new HeaderViewListAdapter(headerViewInfos, footerViewInfos, adapter);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void wrapHeaderListAdapterInternal() {
        this.mAdapter = wrapHeaderListAdapterInternal(this.mHeaderViewInfos, this.mFooterViewInfos, this.mAdapter);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void dispatchDataSetObserverOnChangedInternal() {
        if (this.mDataSetObserver != null) {
            this.mDataSetObserver.onChanged();
        }
    }
}
