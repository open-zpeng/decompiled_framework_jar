package android.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;
import com.android.internal.R;
import com.android.internal.widget.CachingIconView;
import java.util.ArrayList;
@RemoteViews.RemoteView
/* loaded from: classes2.dex */
public class NotificationHeaderView extends ViewGroup {
    public static final int NO_COLOR = 1;
    private boolean mAcceptAllTouches;
    private View mAppName;
    private View mAppOps;
    private View.OnClickListener mAppOpsListener;
    private Drawable mBackground;
    private View mCameraIcon;
    private final int mChildMinWidth;
    private final int mContentEndMargin;
    private boolean mEntireHeaderClickable;
    private ImageView mExpandButton;
    private View.OnClickListener mExpandClickListener;
    private boolean mExpandOnlyOnButton;
    private boolean mExpanded;
    private final int mGravity;
    private View mHeaderText;
    private CachingIconView mIcon;
    private int mIconColor;
    private View mMicIcon;
    private int mOriginalNotificationColor;
    private View mOverlayIcon;
    private View mProfileBadge;
    ViewOutlineProvider mProvider;
    private View mSecondaryHeaderText;
    private boolean mShowExpandButtonAtEnd;
    private boolean mShowWorkBadgeAtEnd;
    private int mTotalWidth;
    private HeaderTouchListener mTouchListener;

    public synchronized NotificationHeaderView(Context context) {
        this(context, null);
    }

    private protected NotificationHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public synchronized NotificationHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public synchronized NotificationHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mTouchListener = new HeaderTouchListener();
        this.mProvider = new ViewOutlineProvider() { // from class: android.view.NotificationHeaderView.1
            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                if (NotificationHeaderView.this.mBackground != null) {
                    outline.setRect(0, 0, NotificationHeaderView.this.getWidth(), NotificationHeaderView.this.getHeight());
                    outline.setAlpha(1.0f);
                }
            }
        };
        Resources res = getResources();
        this.mChildMinWidth = res.getDimensionPixelSize(R.dimen.notification_header_shrink_min_width);
        this.mContentEndMargin = res.getDimensionPixelSize(R.dimen.notification_content_margin_end);
        this.mEntireHeaderClickable = res.getBoolean(R.bool.config_notificationHeaderClickableForExpand);
        int[] attrIds = {16842927};
        TypedArray ta = context.obtainStyledAttributes(attrs, attrIds, defStyleAttr, defStyleRes);
        this.mGravity = ta.getInt(0, 0);
        ta.recycle();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mAppName = findViewById(R.id.app_name_text);
        this.mHeaderText = findViewById(R.id.header_text);
        this.mSecondaryHeaderText = findViewById(R.id.header_text_secondary);
        this.mExpandButton = (ImageView) findViewById(R.id.expand_button);
        this.mIcon = (CachingIconView) findViewById(android.R.id.icon);
        this.mProfileBadge = findViewById(R.id.profile_badge);
        this.mCameraIcon = findViewById(16908872);
        this.mMicIcon = findViewById(R.id.mic);
        this.mOverlayIcon = findViewById(16909248);
        this.mAppOps = findViewById(R.id.app_ops);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int givenWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        int givenHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        int wrapContentWidthSpec = View.MeasureSpec.makeMeasureSpec(givenWidth, Integer.MIN_VALUE);
        int wrapContentHeightSpec = View.MeasureSpec.makeMeasureSpec(givenHeight, Integer.MIN_VALUE);
        int totalWidth = getPaddingStart() + getPaddingEnd();
        int totalWidth2 = totalWidth;
        for (int totalWidth3 = 0; totalWidth3 < getChildCount(); totalWidth3++) {
            View child = getChildAt(totalWidth3);
            if (child.getVisibility() != 8) {
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                int childWidthSpec = getChildMeasureSpec(wrapContentWidthSpec, lp.leftMargin + lp.rightMargin, lp.width);
                int childHeightSpec = getChildMeasureSpec(wrapContentHeightSpec, lp.topMargin + lp.bottomMargin, lp.height);
                child.measure(childWidthSpec, childHeightSpec);
                totalWidth2 += lp.leftMargin + lp.rightMargin + child.getMeasuredWidth();
            }
        }
        if (totalWidth2 > givenWidth) {
            int overFlow = totalWidth2 - givenWidth;
            shrinkViewForOverflow(wrapContentHeightSpec, shrinkViewForOverflow(wrapContentHeightSpec, shrinkViewForOverflow(wrapContentHeightSpec, overFlow, this.mAppName, this.mChildMinWidth), this.mHeaderText, 0), this.mSecondaryHeaderText, 0);
        }
        int overFlow2 = Math.min(totalWidth2, givenWidth);
        this.mTotalWidth = overFlow2;
        setMeasuredDimension(givenWidth, givenHeight);
    }

    private synchronized int shrinkViewForOverflow(int heightSpec, int overFlow, View targetView, int minimumWidth) {
        int oldWidth = targetView.getMeasuredWidth();
        if (overFlow > 0 && targetView.getVisibility() != 8 && oldWidth > minimumWidth) {
            int newSize = Math.max(minimumWidth, oldWidth - overFlow);
            int childWidthSpec = View.MeasureSpec.makeMeasureSpec(newSize, Integer.MIN_VALUE);
            targetView.measure(childWidthSpec, heightSpec);
            return overFlow - (oldWidth - newSize);
        }
        return overFlow;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = getPaddingStart();
        int end = getMeasuredWidth();
        boolean centerAligned = (this.mGravity & 1) != 0;
        if (centerAligned) {
            left += (getMeasuredWidth() / 2) - (this.mTotalWidth / 2);
        }
        int childCount = getChildCount();
        int ownHeight = (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                int childHeight = child.getMeasuredHeight();
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
                int left2 = left + params.getMarginStart();
                int right = child.getMeasuredWidth() + left2;
                int top = (int) (getPaddingTop() + ((ownHeight - childHeight) / 2.0f));
                int bottom = top + childHeight;
                int layoutLeft = left2;
                int layoutRight = right;
                if (child == this.mExpandButton && this.mShowExpandButtonAtEnd) {
                    layoutRight = end - this.mContentEndMargin;
                    int measuredWidth = layoutRight - child.getMeasuredWidth();
                    layoutLeft = measuredWidth;
                    end = measuredWidth;
                }
                if (child == this.mProfileBadge) {
                    int paddingEnd = getPaddingEnd();
                    if (this.mShowWorkBadgeAtEnd) {
                        paddingEnd = this.mContentEndMargin;
                    }
                    layoutRight = end - paddingEnd;
                    int measuredWidth2 = layoutRight - child.getMeasuredWidth();
                    layoutLeft = measuredWidth2;
                    end = measuredWidth2;
                }
                if (child == this.mAppOps) {
                    int paddingEnd2 = this.mContentEndMargin;
                    layoutRight = end - paddingEnd2;
                    int measuredWidth3 = layoutRight - child.getMeasuredWidth();
                    layoutLeft = measuredWidth3;
                    end = measuredWidth3;
                }
                int paddingEnd3 = getLayoutDirection();
                if (paddingEnd3 == 1) {
                    int ltrLeft = layoutLeft;
                    layoutLeft = getWidth() - layoutRight;
                    layoutRight = getWidth() - ltrLeft;
                }
                child.layout(layoutLeft, top, layoutRight, bottom);
                left = right + params.getMarginEnd();
            }
        }
        updateTouchListener();
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new ViewGroup.MarginLayoutParams(getContext(), attrs);
    }

    public synchronized void setHeaderBackgroundDrawable(Drawable drawable) {
        if (drawable != null) {
            setWillNotDraw(false);
            this.mBackground = drawable;
            this.mBackground.setCallback(this);
            setOutlineProvider(this.mProvider);
        } else {
            setWillNotDraw(true);
            this.mBackground = null;
            setOutlineProvider(null);
        }
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        if (this.mBackground != null) {
            this.mBackground.setBounds(0, 0, getWidth(), getHeight());
            this.mBackground.draw(canvas);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.mBackground;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void drawableStateChanged() {
        if (this.mBackground != null && this.mBackground.isStateful()) {
            this.mBackground.setState(getDrawableState());
        }
    }

    private synchronized void updateTouchListener() {
        if (this.mExpandClickListener == null && this.mAppOpsListener == null) {
            setOnTouchListener(null);
            return;
        }
        setOnTouchListener(this.mTouchListener);
        this.mTouchListener.bindTouchRects();
    }

    public synchronized void setAppOpsOnClickListener(View.OnClickListener l) {
        this.mAppOpsListener = l;
        this.mAppOps.setOnClickListener(this.mAppOpsListener);
        this.mCameraIcon.setOnClickListener(this.mAppOpsListener);
        this.mMicIcon.setOnClickListener(this.mAppOpsListener);
        this.mOverlayIcon.setOnClickListener(this.mAppOpsListener);
        updateTouchListener();
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener l) {
        this.mExpandClickListener = l;
        this.mExpandButton.setOnClickListener(this.mExpandClickListener);
        updateTouchListener();
    }

    @RemotableViewMethod
    public synchronized void setOriginalIconColor(int color) {
        this.mIconColor = color;
    }

    public synchronized int getOriginalIconColor() {
        return this.mIconColor;
    }

    @RemotableViewMethod
    public synchronized void setOriginalNotificationColor(int color) {
        this.mOriginalNotificationColor = color;
    }

    public synchronized int getOriginalNotificationColor() {
        return this.mOriginalNotificationColor;
    }

    @RemotableViewMethod
    public synchronized void setExpanded(boolean expanded) {
        this.mExpanded = expanded;
        updateExpandButton();
    }

    public synchronized void showAppOpsIcons(ArraySet<Integer> appOps) {
        if (this.mOverlayIcon == null || this.mCameraIcon == null || this.mMicIcon == null || appOps == null) {
            return;
        }
        this.mOverlayIcon.setVisibility(appOps.contains(24) ? 0 : 8);
        this.mCameraIcon.setVisibility(appOps.contains(26) ? 0 : 8);
        this.mMicIcon.setVisibility(appOps.contains(27) ? 0 : 8);
    }

    private synchronized void updateExpandButton() {
        int drawableId;
        int contentDescriptionId;
        if (this.mExpanded) {
            drawableId = R.drawable.ic_collapse_notification;
            contentDescriptionId = R.string.expand_button_content_description_expanded;
        } else {
            drawableId = R.drawable.ic_expand_notification;
            contentDescriptionId = R.string.expand_button_content_description_collapsed;
        }
        this.mExpandButton.setImageDrawable(getContext().getDrawable(drawableId));
        this.mExpandButton.setColorFilter(this.mOriginalNotificationColor);
        this.mExpandButton.setContentDescription(this.mContext.getText(contentDescriptionId));
    }

    public synchronized void setShowWorkBadgeAtEnd(boolean showWorkBadgeAtEnd) {
        if (showWorkBadgeAtEnd != this.mShowWorkBadgeAtEnd) {
            setClipToPadding(!showWorkBadgeAtEnd);
            this.mShowWorkBadgeAtEnd = showWorkBadgeAtEnd;
        }
    }

    public synchronized void setShowExpandButtonAtEnd(boolean showExpandButtonAtEnd) {
        if (showExpandButtonAtEnd != this.mShowExpandButtonAtEnd) {
            setClipToPadding(!showExpandButtonAtEnd);
            this.mShowExpandButtonAtEnd = showExpandButtonAtEnd;
        }
    }

    public synchronized View getWorkProfileIcon() {
        return this.mProfileBadge;
    }

    public synchronized CachingIconView getIcon() {
        return this.mIcon;
    }

    /* loaded from: classes2.dex */
    public class HeaderTouchListener implements View.OnTouchListener {
        private Rect mAppOpsRect;
        private float mDownX;
        private float mDownY;
        private Rect mExpandButtonRect;
        private final ArrayList<Rect> mTouchRects = new ArrayList<>();
        private int mTouchSlop;
        private boolean mTrackGesture;

        public HeaderTouchListener() {
        }

        public synchronized void bindTouchRects() {
            this.mTouchRects.clear();
            addRectAroundView(NotificationHeaderView.this.mIcon);
            this.mExpandButtonRect = addRectAroundView(NotificationHeaderView.this.mExpandButton);
            this.mAppOpsRect = addRectAroundView(NotificationHeaderView.this.mAppOps);
            addWidthRect();
            this.mTouchSlop = ViewConfiguration.get(NotificationHeaderView.this.getContext()).getScaledTouchSlop();
        }

        private synchronized void addWidthRect() {
            Rect r = new Rect();
            r.top = 0;
            r.bottom = (int) (32.0f * NotificationHeaderView.this.getResources().getDisplayMetrics().density);
            r.left = 0;
            r.right = NotificationHeaderView.this.getWidth();
            this.mTouchRects.add(r);
        }

        private synchronized Rect addRectAroundView(View view) {
            Rect r = getRectAroundView(view);
            this.mTouchRects.add(r);
            return r;
        }

        private synchronized Rect getRectAroundView(View view) {
            float size = 48.0f * NotificationHeaderView.this.getResources().getDisplayMetrics().density;
            float width = Math.max(size, view.getWidth());
            float height = Math.max(size, view.getHeight());
            Rect r = new Rect();
            if (view.getVisibility() == 8) {
                view = NotificationHeaderView.this.getFirstChildNotGone();
                r.left = (int) (view.getLeft() - (width / 2.0f));
            } else {
                r.left = (int) (((view.getLeft() + view.getRight()) / 2.0f) - (width / 2.0f));
            }
            r.top = (int) (((view.getTop() + view.getBottom()) / 2.0f) - (height / 2.0f));
            r.bottom = (int) (r.top + height);
            r.right = (int) (r.left + width);
            return r;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View v, MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getActionMasked() & 255) {
                case 0:
                    this.mTrackGesture = false;
                    if (isInside(x, y)) {
                        this.mDownX = x;
                        this.mDownY = y;
                        this.mTrackGesture = true;
                        return true;
                    }
                    break;
                case 1:
                    if (this.mTrackGesture) {
                        if (!NotificationHeaderView.this.mAppOps.isVisibleToUser() || (!this.mAppOpsRect.contains((int) x, (int) y) && !this.mAppOpsRect.contains((int) this.mDownX, (int) this.mDownY))) {
                            NotificationHeaderView.this.mExpandButton.performClick();
                            break;
                        } else {
                            NotificationHeaderView.this.mAppOps.performClick();
                            return true;
                        }
                    }
                    break;
                case 2:
                    if (this.mTrackGesture && (Math.abs(this.mDownX - x) > this.mTouchSlop || Math.abs(this.mDownY - y) > this.mTouchSlop)) {
                        this.mTrackGesture = false;
                        break;
                    }
                    break;
            }
            return this.mTrackGesture;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized boolean isInside(float x, float y) {
            if (NotificationHeaderView.this.mAcceptAllTouches) {
                return true;
            }
            if (NotificationHeaderView.this.mExpandOnlyOnButton) {
                return this.mExpandButtonRect.contains((int) x, (int) y);
            }
            for (int i = 0; i < this.mTouchRects.size(); i++) {
                Rect r = this.mTouchRects.get(i);
                if (r.contains((int) x, (int) y)) {
                    return true;
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized View getFirstChildNotGone() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                return child;
            }
        }
        return this;
    }

    public synchronized ImageView getExpandButton() {
        return this.mExpandButton;
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public synchronized boolean isInTouchRect(float x, float y) {
        if (this.mExpandClickListener != null) {
            return this.mTouchListener.isInside(x, y);
        }
        return false;
    }

    @RemotableViewMethod
    public synchronized void setAcceptAllTouches(boolean acceptAllTouches) {
        this.mAcceptAllTouches = this.mEntireHeaderClickable || acceptAllTouches;
    }

    @RemotableViewMethod
    public synchronized void setExpandOnlyOnButton(boolean expandOnlyOnButton) {
        this.mExpandOnlyOnButton = expandOnlyOnButton;
    }
}
