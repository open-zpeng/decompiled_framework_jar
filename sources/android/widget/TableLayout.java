package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.android.internal.R;
import java.util.regex.Pattern;
/* loaded from: classes3.dex */
public class TableLayout extends LinearLayout {
    private SparseBooleanArray mCollapsedColumns;
    private boolean mInitialized;
    private int[] mMaxWidths;
    private PassThroughHierarchyChangeListener mPassThroughListener;
    private boolean mShrinkAllColumns;
    private SparseBooleanArray mShrinkableColumns;
    private boolean mStretchAllColumns;
    private SparseBooleanArray mStretchableColumns;

    public TableLayout(Context context) {
        super(context);
        initTableLayout();
    }

    public TableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TableLayout);
        String stretchedColumns = a.getString(0);
        if (stretchedColumns != null) {
            if (stretchedColumns.charAt(0) == '*') {
                this.mStretchAllColumns = true;
            } else {
                this.mStretchableColumns = parseColumns(stretchedColumns);
            }
        }
        String shrinkedColumns = a.getString(1);
        if (shrinkedColumns != null) {
            if (shrinkedColumns.charAt(0) == '*') {
                this.mShrinkAllColumns = true;
            } else {
                this.mShrinkableColumns = parseColumns(shrinkedColumns);
            }
        }
        String collapsedColumns = a.getString(2);
        if (collapsedColumns != null) {
            this.mCollapsedColumns = parseColumns(collapsedColumns);
        }
        a.recycle();
        initTableLayout();
    }

    private static synchronized SparseBooleanArray parseColumns(String sequence) {
        SparseBooleanArray columns = new SparseBooleanArray();
        Pattern pattern = Pattern.compile("\\s*,\\s*");
        String[] columnDefs = pattern.split(sequence);
        for (String columnIdentifier : columnDefs) {
            try {
                int columnIndex = Integer.parseInt(columnIdentifier);
                if (columnIndex >= 0) {
                    columns.put(columnIndex, true);
                }
            } catch (NumberFormatException e) {
            }
        }
        return columns;
    }

    private synchronized void initTableLayout() {
        if (this.mCollapsedColumns == null) {
            this.mCollapsedColumns = new SparseBooleanArray();
        }
        if (this.mStretchableColumns == null) {
            this.mStretchableColumns = new SparseBooleanArray();
        }
        if (this.mShrinkableColumns == null) {
            this.mShrinkableColumns = new SparseBooleanArray();
        }
        setOrientation(1);
        this.mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(this.mPassThroughListener);
        this.mInitialized = true;
    }

    @Override // android.view.ViewGroup
    public void setOnHierarchyChangeListener(ViewGroup.OnHierarchyChangeListener listener) {
        this.mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    private synchronized void requestRowsLayout() {
        if (this.mInitialized) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                getChildAt(i).requestLayout();
            }
        }
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestLayout() {
        if (this.mInitialized) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                getChildAt(i).forceLayout();
            }
        }
        super.requestLayout();
    }

    public boolean isShrinkAllColumns() {
        return this.mShrinkAllColumns;
    }

    public void setShrinkAllColumns(boolean shrinkAllColumns) {
        this.mShrinkAllColumns = shrinkAllColumns;
    }

    public boolean isStretchAllColumns() {
        return this.mStretchAllColumns;
    }

    public void setStretchAllColumns(boolean stretchAllColumns) {
        this.mStretchAllColumns = stretchAllColumns;
    }

    public void setColumnCollapsed(int columnIndex, boolean isCollapsed) {
        this.mCollapsedColumns.put(columnIndex, isCollapsed);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view instanceof TableRow) {
                ((TableRow) view).setColumnCollapsed(columnIndex, isCollapsed);
            }
        }
        requestRowsLayout();
    }

    public boolean isColumnCollapsed(int columnIndex) {
        return this.mCollapsedColumns.get(columnIndex);
    }

    public void setColumnStretchable(int columnIndex, boolean isStretchable) {
        this.mStretchableColumns.put(columnIndex, isStretchable);
        requestRowsLayout();
    }

    public boolean isColumnStretchable(int columnIndex) {
        return this.mStretchAllColumns || this.mStretchableColumns.get(columnIndex);
    }

    public void setColumnShrinkable(int columnIndex, boolean isShrinkable) {
        this.mShrinkableColumns.put(columnIndex, isShrinkable);
        requestRowsLayout();
    }

    public boolean isColumnShrinkable(int columnIndex) {
        return this.mShrinkAllColumns || this.mShrinkableColumns.get(columnIndex);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void trackCollapsedColumns(View child) {
        if (child instanceof TableRow) {
            TableRow row = (TableRow) child;
            SparseBooleanArray collapsedColumns = this.mCollapsedColumns;
            int count = collapsedColumns.size();
            for (int i = 0; i < count; i++) {
                int columnIndex = collapsedColumns.keyAt(i);
                boolean isCollapsed = collapsedColumns.valueAt(i);
                if (isCollapsed) {
                    row.setColumnCollapsed(columnIndex, isCollapsed);
                }
            }
        }
    }

    @Override // android.view.ViewGroup
    public void addView(View child) {
        super.addView(child);
        requestRowsLayout();
    }

    @Override // android.view.ViewGroup
    public void addView(View child, int index) {
        super.addView(child, index);
        requestRowsLayout();
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        requestRowsLayout();
    }

    @Override // android.view.ViewGroup
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        requestRowsLayout();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureVertical(widthMeasureSpec, heightMeasureSpec);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutVertical(l, t, r, b);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.widget.LinearLayout
    public synchronized void measureChildBeforeLayout(View child, int childIndex, int widthMeasureSpec, int totalWidth, int heightMeasureSpec, int totalHeight) {
        if (child instanceof TableRow) {
            ((TableRow) child).setColumnsWidthConstraints(this.mMaxWidths);
        }
        super.measureChildBeforeLayout(child, childIndex, widthMeasureSpec, totalWidth, heightMeasureSpec, totalHeight);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.widget.LinearLayout
    public synchronized void measureVertical(int widthMeasureSpec, int heightMeasureSpec) {
        findLargestCells(widthMeasureSpec, heightMeasureSpec);
        shrinkAndStretchColumns(widthMeasureSpec);
        super.measureVertical(widthMeasureSpec, heightMeasureSpec);
    }

    private synchronized void findLargestCells(int widthMeasureSpec, int heightMeasureSpec) {
        int count;
        int count2 = getChildCount();
        int i = 0;
        boolean firstRow = true;
        int i2 = 0;
        while (i2 < count2) {
            View child = getChildAt(i2);
            if (child.getVisibility() != 8) {
                if (child instanceof TableRow) {
                    TableRow row = (TableRow) child;
                    ViewGroup.LayoutParams layoutParams = row.getLayoutParams();
                    layoutParams.height = -2;
                    int[] widths = row.getColumnsWidths(widthMeasureSpec, heightMeasureSpec);
                    int newLength = widths.length;
                    if (firstRow) {
                        if (this.mMaxWidths == null || this.mMaxWidths.length != newLength) {
                            this.mMaxWidths = new int[newLength];
                        }
                        System.arraycopy(widths, i, this.mMaxWidths, i, newLength);
                        firstRow = false;
                    } else {
                        int length = this.mMaxWidths.length;
                        int difference = newLength - length;
                        if (difference > 0) {
                            int[] oldMaxWidths = this.mMaxWidths;
                            this.mMaxWidths = new int[newLength];
                            int[] iArr = this.mMaxWidths;
                            count = count2;
                            int count3 = oldMaxWidths.length;
                            System.arraycopy(oldMaxWidths, i, iArr, i, count3);
                            System.arraycopy(widths, oldMaxWidths.length, this.mMaxWidths, oldMaxWidths.length, difference);
                        } else {
                            count = count2;
                        }
                        int[] maxWidths = this.mMaxWidths;
                        int length2 = Math.min(length, newLength);
                        for (int j = 0; j < length2; j++) {
                            maxWidths[j] = Math.max(maxWidths[j], widths[j]);
                        }
                    }
                } else {
                    count = count2;
                }
                i2++;
                count2 = count;
                i = 0;
            }
            count = count2;
            i2++;
            count2 = count;
            i = 0;
        }
    }

    private synchronized void shrinkAndStretchColumns(int widthMeasureSpec) {
        int[] iArr;
        if (this.mMaxWidths == null) {
            return;
        }
        int totalWidth = 0;
        for (int width : this.mMaxWidths) {
            totalWidth += width;
        }
        int size = (View.MeasureSpec.getSize(widthMeasureSpec) - this.mPaddingLeft) - this.mPaddingRight;
        if (totalWidth > size && (this.mShrinkAllColumns || this.mShrinkableColumns.size() > 0)) {
            mutateColumnsWidth(this.mShrinkableColumns, this.mShrinkAllColumns, size, totalWidth);
        } else if (totalWidth < size) {
            if (this.mStretchAllColumns || this.mStretchableColumns.size() > 0) {
                mutateColumnsWidth(this.mStretchableColumns, this.mStretchAllColumns, size, totalWidth);
            }
        }
    }

    private synchronized void mutateColumnsWidth(SparseBooleanArray columns, boolean allColumns, int size, int totalWidth) {
        int[] maxWidths = this.mMaxWidths;
        int length = maxWidths.length;
        int count = allColumns ? length : columns.size();
        int totalExtraSpace = size - totalWidth;
        int extraSpace = totalExtraSpace / count;
        int nbChildren = getChildCount();
        for (int i = 0; i < nbChildren; i++) {
            View child = getChildAt(i);
            if (child instanceof TableRow) {
                child.forceLayout();
            }
        }
        if (!allColumns) {
            int skipped = 0;
            for (int skipped2 = 0; skipped2 < count; skipped2++) {
                int column = columns.keyAt(skipped2);
                if (columns.valueAt(skipped2)) {
                    if (column < length) {
                        maxWidths[column] = maxWidths[column] + extraSpace;
                    } else {
                        skipped++;
                    }
                }
            }
            if (skipped > 0 && skipped < count) {
                int extraSpace2 = (skipped * extraSpace) / (count - skipped);
                for (int i2 = 0; i2 < count; i2++) {
                    int column2 = columns.keyAt(i2);
                    if (columns.valueAt(i2) && column2 < length) {
                        if (extraSpace2 > maxWidths[column2]) {
                            maxWidths[column2] = 0;
                        } else {
                            maxWidths[column2] = maxWidths[column2] + extraSpace2;
                        }
                    }
                }
                return;
            }
            return;
        }
        for (int i3 = 0; i3 < count; i3++) {
            maxWidths[i3] = maxWidths[i3] + extraSpace;
        }
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.LinearLayout, android.view.ViewGroup
    public LinearLayout.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.LinearLayout, android.view.ViewGroup
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.LinearLayout, android.view.ViewGroup
    public LinearLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public CharSequence getAccessibilityClassName() {
        return TableLayout.class.getName();
    }

    /* loaded from: classes3.dex */
    public static class LayoutParams extends LinearLayout.LayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int w, int h) {
            super(-1, h);
        }

        public LayoutParams(int w, int h, float initWeight) {
            super(-1, h, initWeight);
        }

        public LayoutParams() {
            super(-1, -2);
        }

        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
            this.width = -1;
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
            this.width = -1;
            if (source instanceof LayoutParams) {
                this.weight = ((LayoutParams) source).weight;
            }
        }

        @Override // android.view.ViewGroup.LayoutParams
        protected void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
            this.width = -1;
            if (a.hasValue(heightAttr)) {
                this.height = a.getLayoutDimension(heightAttr, "layout_height");
            } else {
                this.height = -2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class PassThroughHierarchyChangeListener implements ViewGroup.OnHierarchyChangeListener {
        private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;

        private PassThroughHierarchyChangeListener() {
        }

        @Override // android.view.ViewGroup.OnHierarchyChangeListener
        public void onChildViewAdded(View parent, View child) {
            TableLayout.this.trackCollapsedColumns(child);
            if (this.mOnHierarchyChangeListener != null) {
                this.mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        @Override // android.view.ViewGroup.OnHierarchyChangeListener
        public void onChildViewRemoved(View parent, View child) {
            if (this.mOnHierarchyChangeListener != null) {
                this.mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }
}
