package android.widget;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.IBinder;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionListenerAdapter;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import com.xiaopeng.view.xpWindowManager;
import java.lang.ref.WeakReference;
import java.util.List;
/* loaded from: classes3.dex */
public class PopupWindow {
    private static final int[] ABOVE_ANCHOR_STATE_SET = {R.attr.state_above_anchor};
    private static final int ANIMATION_STYLE_DEFAULT = -1;
    private static final int DEFAULT_ANCHORED_GRAVITY = 8388659;
    public static final int INPUT_METHOD_FROM_FOCUSABLE = 0;
    public static final int INPUT_METHOD_NEEDED = 1;
    public static final int INPUT_METHOD_NOT_NEEDED = 2;
    public protected boolean mAboveAnchor;
    public protected Drawable mAboveAnchorBackgroundDrawable;
    private boolean mAllowScrollingAnchorParent;
    public protected WeakReference<View> mAnchor;
    private WeakReference<View> mAnchorRoot;
    private int mAnchorXoff;
    private int mAnchorYoff;
    private int mAnchoredGravity;
    public protected int mAnimationStyle;
    private boolean mAttachedInDecor;
    private boolean mAttachedInDecorSet;
    private Drawable mBackground;
    public protected View mBackgroundView;
    public protected Drawable mBelowAnchorBackgroundDrawable;
    private boolean mClipToScreen;
    private boolean mClippingEnabled;
    public protected View mContentView;
    public protected Context mContext;
    public protected PopupDecorView mDecorView;
    private float mElevation;
    private Transition mEnterTransition;
    private Rect mEpicenterBounds;
    private Transition mExitTransition;
    private boolean mFocusable;
    private int mGravity;
    private int mHeight;
    public protected int mHeightMode;
    private boolean mIgnoreCheekPress;
    private int mInputMethodMode;
    private boolean mIsAnchorRootAttached;
    public protected boolean mIsDropdown;
    public protected boolean mIsShowing;
    private boolean mIsTransitioningToDismiss;
    public protected int mLastHeight;
    public protected int mLastWidth;
    public protected boolean mLayoutInScreen;
    private boolean mLayoutInsetDecor;
    public protected boolean mNotTouchModal;
    private final View.OnAttachStateChangeListener mOnAnchorDetachedListener;
    private final View.OnAttachStateChangeListener mOnAnchorRootDetachedListener;
    public protected OnDismissListener mOnDismissListener;
    private final View.OnLayoutChangeListener mOnLayoutChangeListener;
    public protected final ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener;
    private boolean mOutsideTouchable;
    public protected boolean mOverlapAnchor;
    private WeakReference<View> mParentRootView;
    private boolean mPopupViewInitialLayoutDirectionInherited;
    private int mSoftInputMode;
    private int mSplitTouchEnabled;
    private final Rect mTempRect;
    private final int[] mTmpAppLocation;
    private final int[] mTmpDrawingLocation;
    private final int[] mTmpScreenLocation;
    public protected View.OnTouchListener mTouchInterceptor;
    private boolean mTouchable;
    private int mWidth;
    public protected int mWidthMode;
    public protected int mWindowLayoutType;
    public protected WindowManager mWindowManager;

    /* loaded from: classes3.dex */
    public interface OnDismissListener {
        void onDismiss();
    }

    public PopupWindow(Context context) {
        this(context, (AttributeSet) null);
    }

    public PopupWindow(Context context, AttributeSet attrs) {
        this(context, attrs, (int) R.attr.popupWindowStyle);
    }

    public PopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public PopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        Transition exitTransition;
        this.mTmpDrawingLocation = new int[2];
        this.mTmpScreenLocation = new int[2];
        this.mTmpAppLocation = new int[2];
        this.mTempRect = new Rect();
        this.mInputMethodMode = 0;
        this.mSoftInputMode = 1;
        this.mTouchable = true;
        this.mOutsideTouchable = false;
        this.mClippingEnabled = true;
        this.mSplitTouchEnabled = -1;
        this.mAllowScrollingAnchorParent = true;
        this.mLayoutInsetDecor = false;
        this.mAttachedInDecor = true;
        this.mAttachedInDecorSet = false;
        this.mWidth = -2;
        this.mHeight = -2;
        this.mWindowLayoutType = 1000;
        this.mIgnoreCheekPress = false;
        this.mAnimationStyle = -1;
        this.mGravity = 0;
        this.mOnAnchorDetachedListener = new View.OnAttachStateChangeListener() { // from class: android.widget.PopupWindow.1
            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(View v) {
                PopupWindow.this.alignToAnchor();
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(View v) {
            }
        };
        this.mOnAnchorRootDetachedListener = new View.OnAttachStateChangeListener() { // from class: android.widget.PopupWindow.2
            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(View v) {
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(View v) {
                PopupWindow.this.mIsAnchorRootAttached = false;
            }
        };
        this.mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() { // from class: android.widget.-$$Lambda$PopupWindow$nV1HS3Nc6Ck5JRIbIHe3mkyHWzc
            @Override // android.view.ViewTreeObserver.OnScrollChangedListener
            public final void onScrollChanged() {
                PopupWindow.this.alignToAnchor();
            }
        };
        this.mOnLayoutChangeListener = new View.OnLayoutChangeListener() { // from class: android.widget.-$$Lambda$PopupWindow$8Gc2stI5cSJZbuKX7X4Qr_vU2nI
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                PopupWindow.this.alignToAnchor();
            }
        };
        this.mContext = context;
        this.mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.PopupWindow, defStyleAttr, defStyleRes);
        Drawable bg = a.getDrawable(0);
        this.mElevation = a.getDimension(3, 0.0f);
        this.mOverlapAnchor = a.getBoolean(2, false);
        if (a.hasValueOrEmpty(1)) {
            int animStyle = a.getResourceId(1, 0);
            if (animStyle == 16974588) {
                this.mAnimationStyle = -1;
            } else {
                this.mAnimationStyle = animStyle;
            }
        } else {
            this.mAnimationStyle = -1;
        }
        Transition enterTransition = getTransition(a.getResourceId(4, 0));
        if (a.hasValueOrEmpty(5)) {
            exitTransition = getTransition(a.getResourceId(5, 0));
        } else {
            exitTransition = enterTransition == null ? null : enterTransition.mo52clone();
        }
        a.recycle();
        setEnterTransition(enterTransition);
        setExitTransition(exitTransition);
        setBackgroundDrawable(bg);
    }

    public PopupWindow() {
        this((View) null, 0, 0);
    }

    public PopupWindow(View contentView) {
        this(contentView, 0, 0);
    }

    public PopupWindow(int width, int height) {
        this((View) null, width, height);
    }

    public PopupWindow(View contentView, int width, int height) {
        this(contentView, width, height, false);
    }

    public PopupWindow(View contentView, int width, int height, boolean focusable) {
        this.mTmpDrawingLocation = new int[2];
        this.mTmpScreenLocation = new int[2];
        this.mTmpAppLocation = new int[2];
        this.mTempRect = new Rect();
        this.mInputMethodMode = 0;
        this.mSoftInputMode = 1;
        this.mTouchable = true;
        this.mOutsideTouchable = false;
        this.mClippingEnabled = true;
        this.mSplitTouchEnabled = -1;
        this.mAllowScrollingAnchorParent = true;
        this.mLayoutInsetDecor = false;
        this.mAttachedInDecor = true;
        this.mAttachedInDecorSet = false;
        this.mWidth = -2;
        this.mHeight = -2;
        this.mWindowLayoutType = 1000;
        this.mIgnoreCheekPress = false;
        this.mAnimationStyle = -1;
        this.mGravity = 0;
        this.mOnAnchorDetachedListener = new View.OnAttachStateChangeListener() { // from class: android.widget.PopupWindow.1
            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(View v) {
                PopupWindow.this.alignToAnchor();
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(View v) {
            }
        };
        this.mOnAnchorRootDetachedListener = new View.OnAttachStateChangeListener() { // from class: android.widget.PopupWindow.2
            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(View v) {
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(View v) {
                PopupWindow.this.mIsAnchorRootAttached = false;
            }
        };
        this.mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() { // from class: android.widget.-$$Lambda$PopupWindow$nV1HS3Nc6Ck5JRIbIHe3mkyHWzc
            @Override // android.view.ViewTreeObserver.OnScrollChangedListener
            public final void onScrollChanged() {
                PopupWindow.this.alignToAnchor();
            }
        };
        this.mOnLayoutChangeListener = new View.OnLayoutChangeListener() { // from class: android.widget.-$$Lambda$PopupWindow$8Gc2stI5cSJZbuKX7X4Qr_vU2nI
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                PopupWindow.this.alignToAnchor();
            }
        };
        if (contentView != null) {
            this.mContext = contentView.getContext();
            this.mWindowManager = (WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE);
        }
        setContentView(contentView);
        setWidth(width);
        setHeight(height);
        setFocusable(focusable);
    }

    public void setEnterTransition(Transition enterTransition) {
        this.mEnterTransition = enterTransition;
    }

    public Transition getEnterTransition() {
        return this.mEnterTransition;
    }

    public void setExitTransition(Transition exitTransition) {
        this.mExitTransition = exitTransition;
    }

    public Transition getExitTransition() {
        return this.mExitTransition;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setEpicenterBounds(Rect bounds) {
        this.mEpicenterBounds = bounds;
    }

    private synchronized Transition getTransition(int resId) {
        if (resId != 0 && resId != 17760256) {
            TransitionInflater inflater = TransitionInflater.from(this.mContext);
            Transition transition = inflater.inflateTransition(resId);
            if (transition != null) {
                boolean isEmpty = (transition instanceof TransitionSet) && ((TransitionSet) transition).getTransitionCount() == 0;
                if (!isEmpty) {
                    return transition;
                }
                return null;
            }
            return null;
        }
        return null;
    }

    public Drawable getBackground() {
        return this.mBackground;
    }

    public void setBackgroundDrawable(Drawable background) {
        this.mBackground = background;
        if (this.mBackground instanceof StateListDrawable) {
            StateListDrawable stateList = (StateListDrawable) this.mBackground;
            int aboveAnchorStateIndex = stateList.getStateDrawableIndex(ABOVE_ANCHOR_STATE_SET);
            int count = stateList.getStateCount();
            int belowAnchorStateIndex = -1;
            int i = 0;
            while (true) {
                if (i >= count) {
                    break;
                } else if (i == aboveAnchorStateIndex) {
                    i++;
                } else {
                    belowAnchorStateIndex = i;
                    break;
                }
            }
            if (aboveAnchorStateIndex != -1 && belowAnchorStateIndex != -1) {
                this.mAboveAnchorBackgroundDrawable = stateList.getStateDrawable(aboveAnchorStateIndex);
                this.mBelowAnchorBackgroundDrawable = stateList.getStateDrawable(belowAnchorStateIndex);
                return;
            }
            this.mBelowAnchorBackgroundDrawable = null;
            this.mAboveAnchorBackgroundDrawable = null;
        }
    }

    public float getElevation() {
        return this.mElevation;
    }

    public void setElevation(float elevation) {
        this.mElevation = elevation;
    }

    public int getAnimationStyle() {
        return this.mAnimationStyle;
    }

    public void setIgnoreCheekPress() {
        this.mIgnoreCheekPress = true;
    }

    public void setAnimationStyle(int animationStyle) {
        this.mAnimationStyle = animationStyle;
    }

    public View getContentView() {
        return this.mContentView;
    }

    public void setContentView(View contentView) {
        if (isShowing()) {
            return;
        }
        this.mContentView = contentView;
        if (this.mContext == null && this.mContentView != null) {
            this.mContext = this.mContentView.getContext();
        }
        if (this.mWindowManager == null && this.mContentView != null) {
            this.mWindowManager = (WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE);
        }
        if (this.mContext != null && !this.mAttachedInDecorSet) {
            setAttachedInDecor(this.mContext.getApplicationInfo().targetSdkVersion >= 22);
        }
    }

    public void setTouchInterceptor(View.OnTouchListener l) {
        this.mTouchInterceptor = l;
    }

    public boolean isFocusable() {
        return this.mFocusable;
    }

    public void setFocusable(boolean focusable) {
        this.mFocusable = focusable;
    }

    public int getInputMethodMode() {
        return this.mInputMethodMode;
    }

    public void setInputMethodMode(int mode) {
        this.mInputMethodMode = mode;
    }

    public void setSoftInputMode(int mode) {
        this.mSoftInputMode = mode;
    }

    public int getSoftInputMode() {
        return this.mSoftInputMode;
    }

    public boolean isTouchable() {
        return this.mTouchable;
    }

    public void setTouchable(boolean touchable) {
        this.mTouchable = touchable;
    }

    public boolean isOutsideTouchable() {
        return this.mOutsideTouchable;
    }

    public void setOutsideTouchable(boolean touchable) {
        this.mOutsideTouchable = touchable;
    }

    public boolean isClippingEnabled() {
        return this.mClippingEnabled;
    }

    public void setClippingEnabled(boolean enabled) {
        this.mClippingEnabled = enabled;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setClipToScreenEnabled(boolean enabled) {
        this.mClipToScreen = enabled;
    }

    public private protected void setAllowScrollingAnchorParent(boolean enabled) {
        this.mAllowScrollingAnchorParent = enabled;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final synchronized boolean getAllowScrollingAnchorParent() {
        return this.mAllowScrollingAnchorParent;
    }

    public boolean isSplitTouchEnabled() {
        return (this.mSplitTouchEnabled >= 0 || this.mContext == null) ? this.mSplitTouchEnabled == 1 : this.mContext.getApplicationInfo().targetSdkVersion >= 11;
    }

    public void setSplitTouchEnabled(boolean enabled) {
        this.mSplitTouchEnabled = enabled ? 1 : 0;
    }

    public synchronized boolean isLayoutInScreenEnabled() {
        return this.mLayoutInScreen;
    }

    private protected void setLayoutInScreenEnabled(boolean enabled) {
        this.mLayoutInScreen = enabled;
    }

    public boolean isAttachedInDecor() {
        return this.mAttachedInDecor;
    }

    public void setAttachedInDecor(boolean enabled) {
        this.mAttachedInDecor = enabled;
        this.mAttachedInDecorSet = true;
    }

    private protected void setLayoutInsetDecor(boolean enabled) {
        this.mLayoutInsetDecor = enabled;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final synchronized boolean isLayoutInsetDecor() {
        return this.mLayoutInsetDecor;
    }

    public void setWindowLayoutType(int layoutType) {
        this.mWindowLayoutType = layoutType;
    }

    public int getWindowLayoutType() {
        return this.mWindowLayoutType;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTouchModal(boolean touchModal) {
        this.mNotTouchModal = !touchModal;
    }

    @Deprecated
    public void setWindowLayoutMode(int widthSpec, int heightSpec) {
        this.mWidthMode = widthSpec;
        this.mHeightMode = heightSpec;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public void setOverlapAnchor(boolean overlapAnchor) {
        this.mOverlapAnchor = overlapAnchor;
    }

    public boolean getOverlapAnchor() {
        return this.mOverlapAnchor;
    }

    public boolean isShowing() {
        return this.mIsShowing;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final synchronized void setShowing(boolean isShowing) {
        this.mIsShowing = isShowing;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final synchronized void setDropDown(boolean isDropDown) {
        this.mIsDropdown = isDropDown;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final synchronized void setTransitioningToDismiss(boolean transitioningToDismiss) {
        this.mIsTransitioningToDismiss = transitioningToDismiss;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final synchronized boolean isTransitioningToDismiss() {
        return this.mIsTransitioningToDismiss;
    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        this.mParentRootView = new WeakReference<>(parent.getRootView());
        showAtLocation(parent.getWindowToken(), gravity, x, y);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAtLocation(IBinder token, int gravity, int x, int y) {
        if (isShowing() || this.mContentView == null) {
            return;
        }
        TransitionManager.endTransitions(this.mDecorView);
        detachFromAnchor();
        this.mIsShowing = true;
        this.mIsDropdown = false;
        this.mGravity = gravity;
        WindowManager.LayoutParams p = createPopupLayoutParams(token);
        preparePopup(p);
        p.x = x;
        p.y = y;
        invokePopup(p);
    }

    public void showAsDropDown(View anchor) {
        showAsDropDown(anchor, 0, 0);
    }

    public void showAsDropDown(View anchor, int xoff, int yoff) {
        showAsDropDown(anchor, xoff, yoff, DEFAULT_ANCHORED_GRAVITY);
    }

    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (isShowing() || !hasContentView()) {
            return;
        }
        TransitionManager.endTransitions(this.mDecorView);
        attachToAnchor(anchor, xoff, yoff, gravity);
        this.mIsShowing = true;
        this.mIsDropdown = true;
        WindowManager.LayoutParams p = createPopupLayoutParams(anchor.getApplicationWindowToken());
        preparePopup(p);
        boolean aboveAnchor = findDropDownPosition(anchor, p, xoff, yoff, p.width, p.height, gravity, this.mAllowScrollingAnchorParent);
        updateAboveAnchor(aboveAnchor);
        p.accessibilityIdOfAnchor = anchor != null ? anchor.getAccessibilityViewId() : -1L;
        invokePopup(p);
    }

    public private final void updateAboveAnchor(boolean aboveAnchor) {
        if (aboveAnchor != this.mAboveAnchor) {
            this.mAboveAnchor = aboveAnchor;
            if (this.mBackground != null && this.mBackgroundView != null) {
                if (this.mAboveAnchorBackgroundDrawable != null) {
                    if (this.mAboveAnchor) {
                        this.mBackgroundView.setBackground(this.mAboveAnchorBackgroundDrawable);
                        return;
                    } else {
                        this.mBackgroundView.setBackground(this.mBelowAnchorBackgroundDrawable);
                        return;
                    }
                }
                this.mBackgroundView.refreshDrawableState();
            }
        }
    }

    public boolean isAboveAnchor() {
        return this.mAboveAnchor;
    }

    public protected void preparePopup(WindowManager.LayoutParams p) {
        if (this.mContentView == null || this.mContext == null || this.mWindowManager == null) {
            throw new IllegalStateException("You must specify a valid content view by calling setContentView() before attempting to show the popup.");
        }
        if (p.accessibilityTitle == null) {
            p.accessibilityTitle = this.mContext.getString(com.android.internal.R.string.popup_window_default_title);
        }
        if (this.mDecorView != null) {
            this.mDecorView.cancelTransitions();
        }
        if (this.mBackground != null) {
            this.mBackgroundView = createBackgroundView(this.mContentView);
            this.mBackgroundView.setBackground(this.mBackground);
        } else {
            this.mBackgroundView = this.mContentView;
        }
        this.mDecorView = createDecorView(this.mBackgroundView);
        this.mDecorView.setIsRootNamespace(true);
        this.mBackgroundView.setElevation(this.mElevation);
        p.setSurfaceInsets(this.mBackgroundView, true, true);
        this.mPopupViewInitialLayoutDirectionInherited = this.mContentView.getRawLayoutDirection() == 2;
    }

    private synchronized PopupBackgroundView createBackgroundView(View contentView) {
        int height;
        ViewGroup.LayoutParams layoutParams = this.mContentView.getLayoutParams();
        if (layoutParams != null && layoutParams.height == -2) {
            height = -2;
        } else {
            height = -1;
        }
        PopupBackgroundView backgroundView = new PopupBackgroundView(this.mContext);
        FrameLayout.LayoutParams listParams = new FrameLayout.LayoutParams(-1, height);
        backgroundView.addView(contentView, listParams);
        return backgroundView;
    }

    private synchronized PopupDecorView createDecorView(View contentView) {
        int height;
        ViewGroup.LayoutParams layoutParams = this.mContentView.getLayoutParams();
        if (layoutParams != null && layoutParams.height == -2) {
            height = -2;
        } else {
            height = -1;
        }
        PopupDecorView decorView = new PopupDecorView(this.mContext);
        decorView.addView(contentView, -1, height);
        decorView.setClipChildren(false);
        decorView.setClipToPadding(false);
        return decorView;
    }

    public protected void invokePopup(WindowManager.LayoutParams p) {
        if (this.mContext != null) {
            p.packageName = this.mContext.getPackageName();
        }
        PopupDecorView decorView = this.mDecorView;
        decorView.setFitsSystemWindows(this.mLayoutInsetDecor);
        setLayoutDirectionFromAnchor();
        this.mWindowManager.addView(decorView, p);
        if (this.mEnterTransition != null) {
            decorView.requestEnterTransition(this.mEnterTransition);
        }
    }

    private synchronized void setLayoutDirectionFromAnchor() {
        View anchor;
        if (this.mAnchor != null && (anchor = this.mAnchor.get()) != null && this.mPopupViewInitialLayoutDirectionInherited) {
            this.mDecorView.setLayoutDirection(anchor.getLayoutDirection());
        }
    }

    private synchronized int computeGravity() {
        int gravity = this.mGravity == 0 ? DEFAULT_ANCHORED_GRAVITY : this.mGravity;
        if (this.mIsDropdown) {
            if (this.mClipToScreen || this.mClippingEnabled) {
                return gravity | 268435456;
            }
            return gravity;
        }
        return gravity;
    }

    public private final WindowManager.LayoutParams createPopupLayoutParams(IBinder token) {
        WindowManager.LayoutParams p = new WindowManager.LayoutParams();
        p.gravity = computeGravity();
        p.flags = computeFlags(p.flags);
        p.type = this.mWindowLayoutType;
        p.token = token;
        p.softInputMode = this.mSoftInputMode;
        p.windowAnimations = computeAnimationResource();
        if (this.mBackground != null) {
            p.format = this.mBackground.getOpacity();
        } else {
            p.format = -3;
        }
        if (this.mHeightMode < 0) {
            int i = this.mHeightMode;
            this.mLastHeight = i;
            p.height = i;
        } else {
            int i2 = this.mHeight;
            this.mLastHeight = i2;
            p.height = i2;
        }
        if (this.mWidthMode < 0) {
            int i3 = this.mWidthMode;
            this.mLastWidth = i3;
            p.width = i3;
        } else {
            int i4 = this.mWidth;
            this.mLastWidth = i4;
            p.width = i4;
        }
        p.privateFlags = 98304;
        p.setTitle("PopupWindow:" + Integer.toHexString(hashCode()));
        return xpWindowManager.getOverrideLayoutParams(p, this);
    }

    private synchronized int computeFlags(int curFlags) {
        int curFlags2 = curFlags & (-8815129);
        if (this.mIgnoreCheekPress) {
            curFlags2 |= 32768;
        }
        if (!this.mFocusable) {
            curFlags2 |= 8;
            if (this.mInputMethodMode == 1) {
                curFlags2 |= 131072;
            }
        } else if (this.mInputMethodMode == 2) {
            curFlags2 |= 131072;
        }
        if (!this.mTouchable) {
            curFlags2 |= 16;
        }
        if (this.mOutsideTouchable) {
            curFlags2 |= 262144;
        }
        if (!this.mClippingEnabled || this.mClipToScreen) {
            curFlags2 |= 512;
        }
        if (isSplitTouchEnabled()) {
            curFlags2 |= 8388608;
        }
        if (this.mLayoutInScreen) {
            curFlags2 |= 256;
        }
        if (this.mLayoutInsetDecor) {
            curFlags2 |= 65536;
        }
        if (this.mNotTouchModal) {
            curFlags2 |= 32;
        }
        if (this.mAttachedInDecor) {
            return curFlags2 | 1073741824;
        }
        return curFlags2;
    }

    public protected int computeAnimationResource() {
        if (this.mAnimationStyle == -1) {
            if (this.mIsDropdown) {
                if (this.mAboveAnchor) {
                    return 16974576;
                }
                return 16974575;
            }
            return 0;
        }
        return this.mAnimationStyle;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r32v0 */
    /* JADX WARN: Type inference failed for: r32v1, types: [boolean] */
    /* JADX WARN: Type inference failed for: r32v3 */
    public synchronized boolean findDropDownPosition(View anchor, WindowManager.LayoutParams outParams, int xOffset, int yOffset, int width, int height, int gravity, boolean allowScroll) {
        int hgrav;
        char c;
        ?? r32;
        int anchorHeight = anchor.getHeight();
        int anchorWidth = anchor.getWidth();
        int yOffset2 = this.mOverlapAnchor ? yOffset - anchorHeight : yOffset;
        int[] appScreenLocation = this.mTmpAppLocation;
        View appRootView = getAppRootView(anchor);
        appRootView.getLocationOnScreen(appScreenLocation);
        int[] screenLocation = this.mTmpScreenLocation;
        anchor.getLocationOnScreen(screenLocation);
        int[] drawingLocation = this.mTmpDrawingLocation;
        drawingLocation[0] = screenLocation[0] - appScreenLocation[0];
        drawingLocation[1] = screenLocation[1] - appScreenLocation[1];
        outParams.x = drawingLocation[0] + xOffset;
        outParams.y = drawingLocation[1] + anchorHeight + yOffset2;
        Rect displayFrame = new Rect();
        appRootView.getWindowVisibleDisplayFrame(displayFrame);
        int i = width;
        if (i == -1) {
            i = displayFrame.right - displayFrame.left;
        }
        int width2 = i;
        int height2 = height == -1 ? displayFrame.bottom - displayFrame.top : height;
        outParams.gravity = computeGravity();
        outParams.width = width2;
        outParams.height = height2;
        int hgrav2 = Gravity.getAbsoluteGravity(gravity, anchor.getLayoutDirection()) & 7;
        if (hgrav2 == 5) {
            outParams.x -= width2 - anchorWidth;
        }
        int height3 = height2;
        boolean fitsVertical = tryFitVertical(outParams, yOffset2, height3, anchorHeight, drawingLocation[1], screenLocation[1], displayFrame.top, displayFrame.bottom, false);
        boolean fitsHorizontal = tryFitHorizontal(outParams, xOffset, width2, anchorWidth, drawingLocation[0], screenLocation[0], displayFrame.left, displayFrame.right, false);
        if (fitsVertical && fitsHorizontal) {
            r32 = 1;
        } else {
            int scrollX = anchor.getScrollX();
            int scrollY = anchor.getScrollY();
            Rect r = new Rect(scrollX, scrollY, scrollX + width2 + xOffset, scrollY + height3 + anchorHeight + yOffset2);
            if (allowScroll) {
                c = 1;
                if (anchor.requestRectangleOnScreen(r, true)) {
                    anchor.getLocationOnScreen(screenLocation);
                    drawingLocation[0] = screenLocation[0] - appScreenLocation[0];
                    drawingLocation[1] = screenLocation[1] - appScreenLocation[1];
                    outParams.x = drawingLocation[0] + xOffset;
                    outParams.y = drawingLocation[1] + anchorHeight + yOffset2;
                    hgrav = hgrav2;
                    if (hgrav == 5) {
                        outParams.x -= width2 - anchorWidth;
                    }
                } else {
                    hgrav = hgrav2;
                }
            } else {
                hgrav = hgrav2;
                c = 1;
            }
            r32 = c;
            tryFitVertical(outParams, yOffset2, height3, anchorHeight, drawingLocation[c], screenLocation[c], displayFrame.top, displayFrame.bottom, this.mClipToScreen);
            tryFitHorizontal(outParams, xOffset, width2, anchorWidth, drawingLocation[0], screenLocation[0], displayFrame.left, displayFrame.right, this.mClipToScreen);
        }
        if (outParams.y < drawingLocation[r32]) {
            return r32;
        }
        return false;
    }

    private synchronized boolean tryFitVertical(WindowManager.LayoutParams outParams, int yOffset, int height, int anchorHeight, int drawingLocationY, int screenLocationY, int displayFrameTop, int displayFrameBottom, boolean allowResize) {
        int yOffset2;
        int winOffsetY = screenLocationY - drawingLocationY;
        int anchorTopInScreen = outParams.y + winOffsetY;
        int spaceBelow = displayFrameBottom - anchorTopInScreen;
        if (anchorTopInScreen >= 0 && height <= spaceBelow) {
            return true;
        }
        int spaceAbove = (anchorTopInScreen - anchorHeight) - displayFrameTop;
        if (height <= spaceAbove) {
            if (this.mOverlapAnchor) {
                yOffset2 = yOffset + anchorHeight;
            } else {
                yOffset2 = yOffset;
            }
            outParams.y = (drawingLocationY - height) + yOffset2;
            return true;
        } else if (positionInDisplayVertical(outParams, height, drawingLocationY, screenLocationY, displayFrameTop, displayFrameBottom, allowResize)) {
            return true;
        } else {
            return false;
        }
    }

    private synchronized boolean positionInDisplayVertical(WindowManager.LayoutParams outParams, int height, int drawingLocationY, int screenLocationY, int displayFrameTop, int displayFrameBottom, boolean canResize) {
        boolean fitsInDisplay = true;
        int winOffsetY = screenLocationY - drawingLocationY;
        outParams.y += winOffsetY;
        outParams.height = height;
        int bottom = outParams.y + height;
        if (bottom > displayFrameBottom) {
            outParams.y -= bottom - displayFrameBottom;
        }
        if (outParams.y < displayFrameTop) {
            outParams.y = displayFrameTop;
            int displayFrameHeight = displayFrameBottom - displayFrameTop;
            if (canResize && height > displayFrameHeight) {
                outParams.height = displayFrameHeight;
            } else {
                fitsInDisplay = false;
            }
        }
        outParams.y -= winOffsetY;
        return fitsInDisplay;
    }

    private synchronized boolean tryFitHorizontal(WindowManager.LayoutParams outParams, int xOffset, int width, int anchorWidth, int drawingLocationX, int screenLocationX, int displayFrameLeft, int displayFrameRight, boolean allowResize) {
        int i;
        int winOffsetX = screenLocationX - drawingLocationX;
        int anchorLeftInScreen = outParams.x + winOffsetX;
        int spaceRight = displayFrameRight - anchorLeftInScreen;
        if (anchorLeftInScreen >= 0) {
            i = width;
            if (i <= spaceRight) {
                return true;
            }
        } else {
            i = width;
        }
        if (positionInDisplayHorizontal(outParams, i, drawingLocationX, screenLocationX, displayFrameLeft, displayFrameRight, allowResize)) {
            return true;
        }
        return false;
    }

    private synchronized boolean positionInDisplayHorizontal(WindowManager.LayoutParams outParams, int width, int drawingLocationX, int screenLocationX, int displayFrameLeft, int displayFrameRight, boolean canResize) {
        boolean fitsInDisplay = true;
        int winOffsetX = screenLocationX - drawingLocationX;
        outParams.x += winOffsetX;
        int right = outParams.x + width;
        if (right > displayFrameRight) {
            outParams.x -= right - displayFrameRight;
        }
        if (outParams.x < displayFrameLeft) {
            outParams.x = displayFrameLeft;
            int displayFrameWidth = displayFrameRight - displayFrameLeft;
            if (canResize && width > displayFrameWidth) {
                outParams.width = displayFrameWidth;
            } else {
                fitsInDisplay = false;
            }
        }
        outParams.x -= winOffsetX;
        return fitsInDisplay;
    }

    public int getMaxAvailableHeight(View anchor) {
        return getMaxAvailableHeight(anchor, 0);
    }

    public int getMaxAvailableHeight(View anchor, int yOffset) {
        return getMaxAvailableHeight(anchor, yOffset, false);
    }

    public int getMaxAvailableHeight(View anchor, int yOffset, boolean ignoreBottomDecorations) {
        Rect displayFrame;
        int distanceToBottom;
        Rect visibleDisplayFrame = new Rect();
        View appView = getAppRootView(anchor);
        appView.getWindowVisibleDisplayFrame(visibleDisplayFrame);
        if (ignoreBottomDecorations) {
            displayFrame = new Rect();
            anchor.getWindowDisplayFrame(displayFrame);
            displayFrame.top = visibleDisplayFrame.top;
            displayFrame.right = visibleDisplayFrame.right;
            displayFrame.left = visibleDisplayFrame.left;
        } else {
            displayFrame = visibleDisplayFrame;
        }
        int[] anchorPos = this.mTmpDrawingLocation;
        anchor.getLocationOnScreen(anchorPos);
        int bottomEdge = displayFrame.bottom;
        if (this.mOverlapAnchor) {
            distanceToBottom = (bottomEdge - anchorPos[1]) - yOffset;
        } else {
            int distanceToBottom2 = anchorPos[1];
            distanceToBottom = (bottomEdge - (distanceToBottom2 + anchor.getHeight())) - yOffset;
        }
        int distanceToTop = (anchorPos[1] - displayFrame.top) + yOffset;
        int returnedHeight = Math.max(distanceToBottom, distanceToTop);
        if (this.mBackground != null) {
            this.mBackground.getPadding(this.mTempRect);
            return returnedHeight - (this.mTempRect.top + this.mTempRect.bottom);
        }
        return returnedHeight;
    }

    public void dismiss() {
        final ViewGroup contentHolder;
        if (!isShowing() || isTransitioningToDismiss()) {
            return;
        }
        final PopupDecorView decorView = this.mDecorView;
        final View contentView = this.mContentView;
        ViewParent contentParent = contentView.getParent();
        if (contentParent instanceof ViewGroup) {
            contentHolder = (ViewGroup) contentParent;
        } else {
            contentHolder = null;
        }
        decorView.cancelTransitions();
        this.mIsShowing = false;
        this.mIsTransitioningToDismiss = true;
        Transition exitTransition = this.mExitTransition;
        if (exitTransition != null && decorView.isLaidOut() && (this.mIsAnchorRootAttached || this.mAnchorRoot == null)) {
            WindowManager.LayoutParams p = (WindowManager.LayoutParams) decorView.getLayoutParams();
            p.flags |= 16;
            p.flags |= 8;
            p.flags &= -131073;
            this.mWindowManager.updateViewLayout(decorView, p);
            View anchorRoot = this.mAnchorRoot != null ? this.mAnchorRoot.get() : null;
            Rect epicenter = getTransitionEpicenter();
            decorView.startExitTransition(exitTransition, anchorRoot, epicenter, new TransitionListenerAdapter() { // from class: android.widget.PopupWindow.3
                @Override // android.transition.TransitionListenerAdapter, android.transition.Transition.TransitionListener
                public void onTransitionEnd(Transition transition) {
                    PopupWindow.this.dismissImmediate(decorView, contentHolder, contentView);
                }
            });
        } else {
            dismissImmediate(decorView, contentHolder, contentView);
        }
        detachFromAnchor();
        if (this.mOnDismissListener != null) {
            this.mOnDismissListener.onDismiss();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final synchronized Rect getTransitionEpicenter() {
        View anchor = this.mAnchor != null ? this.mAnchor.get() : null;
        View decor = this.mDecorView;
        if (anchor == null || decor == null) {
            return null;
        }
        int[] anchorLocation = anchor.getLocationOnScreen();
        int[] popupLocation = this.mDecorView.getLocationOnScreen();
        Rect bounds = new Rect(0, 0, anchor.getWidth(), anchor.getHeight());
        bounds.offset(anchorLocation[0] - popupLocation[0], anchorLocation[1] - popupLocation[1]);
        if (this.mEpicenterBounds != null) {
            int offsetX = bounds.left;
            int offsetY = bounds.top;
            bounds.set(this.mEpicenterBounds);
            bounds.offset(offsetX, offsetY);
        }
        return bounds;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dismissImmediate(View decorView, ViewGroup contentHolder, View contentView) {
        if (decorView.getParent() != null) {
            this.mWindowManager.removeViewImmediate(decorView);
        }
        if (contentHolder != null) {
            contentHolder.removeView(contentView);
        }
        this.mDecorView = null;
        this.mBackgroundView = null;
        this.mIsTransitioningToDismiss = false;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final synchronized OnDismissListener getOnDismissListener() {
        return this.mOnDismissListener;
    }

    public void update() {
        if (!isShowing() || !hasContentView()) {
            return;
        }
        WindowManager.LayoutParams p = getDecorViewLayoutParams();
        boolean update = false;
        int newAnim = computeAnimationResource();
        if (newAnim != p.windowAnimations) {
            p.windowAnimations = newAnim;
            update = true;
        }
        int newFlags = computeFlags(p.flags);
        if (newFlags != p.flags) {
            p.flags = newFlags;
            update = true;
        }
        int newGravity = computeGravity();
        if (newGravity != p.gravity) {
            p.gravity = newGravity;
            update = true;
        }
        if (update) {
            update(this.mAnchor != null ? this.mAnchor.get() : null, p);
        }
    }

    protected synchronized void update(View anchor, WindowManager.LayoutParams params) {
        setLayoutDirectionFromAnchor();
        this.mWindowManager.updateViewLayout(this.mDecorView, params);
    }

    public void update(int width, int height) {
        WindowManager.LayoutParams p = getDecorViewLayoutParams();
        update(p.x, p.y, width, height, false);
    }

    public void update(int x, int y, int width, int height) {
        update(x, y, width, height, false);
    }

    public void update(int x, int y, int width, int height, boolean force) {
        if (width >= 0) {
            this.mLastWidth = width;
            setWidth(width);
        }
        if (height >= 0) {
            this.mLastHeight = height;
            setHeight(height);
        }
        if (!isShowing() || !hasContentView()) {
            return;
        }
        WindowManager.LayoutParams p = getDecorViewLayoutParams();
        boolean update = force;
        int finalWidth = this.mWidthMode < 0 ? this.mWidthMode : this.mLastWidth;
        if (width != -1 && p.width != finalWidth) {
            this.mLastWidth = finalWidth;
            p.width = finalWidth;
            update = true;
        }
        int finalHeight = this.mHeightMode < 0 ? this.mHeightMode : this.mLastHeight;
        if (height != -1 && p.height != finalHeight) {
            this.mLastHeight = finalHeight;
            p.height = finalHeight;
            update = true;
        }
        if (p.x != x) {
            p.x = x;
            update = true;
        }
        if (p.y != y) {
            p.y = y;
            update = true;
        }
        int newAnim = computeAnimationResource();
        if (newAnim != p.windowAnimations) {
            p.windowAnimations = newAnim;
            update = true;
        }
        int newFlags = computeFlags(p.flags);
        if (newFlags != p.flags) {
            p.flags = newFlags;
            update = true;
        }
        int newGravity = computeGravity();
        if (newGravity != p.gravity) {
            p.gravity = newGravity;
            update = true;
        }
        View anchor = null;
        int newAccessibilityIdOfAnchor = -1;
        if (this.mAnchor != null && this.mAnchor.get() != null) {
            anchor = this.mAnchor.get();
            newAccessibilityIdOfAnchor = anchor.getAccessibilityViewId();
        }
        if (newAccessibilityIdOfAnchor != p.accessibilityIdOfAnchor) {
            p.accessibilityIdOfAnchor = newAccessibilityIdOfAnchor;
            update = true;
        }
        if (update) {
            update(anchor, p);
        }
    }

    protected synchronized boolean hasContentView() {
        return this.mContentView != null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized boolean hasDecorView() {
        return this.mDecorView != null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized WindowManager.LayoutParams getDecorViewLayoutParams() {
        return (WindowManager.LayoutParams) this.mDecorView.getLayoutParams();
    }

    public void update(View anchor, int width, int height) {
        update(anchor, false, 0, 0, width, height);
    }

    public void update(View anchor, int xoff, int yoff, int width, int height) {
        update(anchor, true, xoff, yoff, width, height);
    }

    private synchronized void update(View anchor, boolean updateLocation, int xoff, int yoff, int width, int height) {
        if (!isShowing() || !hasContentView()) {
            return;
        }
        WeakReference<View> oldAnchor = this.mAnchor;
        int gravity = this.mAnchoredGravity;
        boolean needsUpdate = updateLocation && !(this.mAnchorXoff == xoff && this.mAnchorYoff == yoff);
        if (oldAnchor == null || oldAnchor.get() != anchor || (needsUpdate && !this.mIsDropdown)) {
            attachToAnchor(anchor, xoff, yoff, gravity);
        } else if (needsUpdate) {
            this.mAnchorXoff = xoff;
            this.mAnchorYoff = yoff;
        }
        WindowManager.LayoutParams p = getDecorViewLayoutParams();
        int oldGravity = p.gravity;
        int oldWidth = p.width;
        int oldHeight = p.height;
        int oldX = p.x;
        int oldY = p.y;
        int width2 = width < 0 ? this.mWidth : width;
        int height2 = height < 0 ? this.mHeight : height;
        int oldHeight2 = width2;
        int oldWidth2 = height2;
        boolean aboveAnchor = findDropDownPosition(anchor, p, this.mAnchorXoff, this.mAnchorYoff, oldHeight2, oldWidth2, gravity, this.mAllowScrollingAnchorParent);
        updateAboveAnchor(aboveAnchor);
        boolean paramsChanged = (oldGravity == p.gravity && oldX == p.x && oldY == p.y && oldWidth == p.width && oldHeight == p.height) ? false : true;
        int newWidth = width2 < 0 ? width2 : p.width;
        int newHeight = height2 < 0 ? height2 : p.height;
        update(p.x, p.y, newWidth, newHeight, paramsChanged);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void detachFromAnchor() {
        View anchor = getAnchor();
        if (anchor != null) {
            ViewTreeObserver vto = anchor.getViewTreeObserver();
            vto.removeOnScrollChangedListener(this.mOnScrollChangedListener);
            anchor.removeOnAttachStateChangeListener(this.mOnAnchorDetachedListener);
        }
        View anchorRoot = this.mAnchorRoot != null ? this.mAnchorRoot.get() : null;
        if (anchorRoot != null) {
            anchorRoot.removeOnAttachStateChangeListener(this.mOnAnchorRootDetachedListener);
            anchorRoot.removeOnLayoutChangeListener(this.mOnLayoutChangeListener);
        }
        this.mAnchor = null;
        this.mAnchorRoot = null;
        this.mIsAnchorRootAttached = false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void attachToAnchor(View anchor, int xoff, int yoff, int gravity) {
        detachFromAnchor();
        ViewTreeObserver vto = anchor.getViewTreeObserver();
        if (vto != null) {
            vto.addOnScrollChangedListener(this.mOnScrollChangedListener);
        }
        anchor.addOnAttachStateChangeListener(this.mOnAnchorDetachedListener);
        View anchorRoot = anchor.getRootView();
        anchorRoot.addOnAttachStateChangeListener(this.mOnAnchorRootDetachedListener);
        anchorRoot.addOnLayoutChangeListener(this.mOnLayoutChangeListener);
        this.mAnchor = new WeakReference<>(anchor);
        this.mAnchorRoot = new WeakReference<>(anchorRoot);
        this.mIsAnchorRootAttached = anchorRoot.isAttachedToWindow();
        this.mParentRootView = this.mAnchorRoot;
        this.mAnchorXoff = xoff;
        this.mAnchorYoff = yoff;
        this.mAnchoredGravity = gravity;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized View getAnchor() {
        if (this.mAnchor != null) {
            return this.mAnchor.get();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void alignToAnchor() {
        View anchor = this.mAnchor != null ? this.mAnchor.get() : null;
        if (anchor != null && anchor.isAttachedToWindow() && hasDecorView()) {
            WindowManager.LayoutParams p = getDecorViewLayoutParams();
            updateAboveAnchor(findDropDownPosition(anchor, p, this.mAnchorXoff, this.mAnchorYoff, p.width, p.height, this.mAnchoredGravity, false));
            update(p.x, p.y, -1, -1, true);
        }
    }

    private synchronized View getAppRootView(View anchor) {
        View appWindowView = WindowManagerGlobal.getInstance().getWindowView(anchor.getApplicationWindowToken());
        if (appWindowView != null) {
            return appWindowView;
        }
        return anchor.getRootView();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class PopupDecorView extends FrameLayout {
        private Runnable mCleanupAfterExit;
        private final View.OnAttachStateChangeListener mOnAnchorRootDetachedListener;

        public PopupDecorView(Context context) {
            super(context);
            this.mOnAnchorRootDetachedListener = new View.OnAttachStateChangeListener() { // from class: android.widget.PopupWindow.PopupDecorView.4
                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewAttachedToWindow(View v) {
                }

                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewDetachedFromWindow(View v) {
                    v.removeOnAttachStateChangeListener(this);
                    if (PopupDecorView.this.isAttachedToWindow()) {
                        TransitionManager.endTransitions(PopupDecorView.this);
                    }
                }
            };
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchKeyEvent(KeyEvent event) {
            KeyEvent.DispatcherState state;
            if (event.getKeyCode() == 4) {
                if (getKeyDispatcherState() == null) {
                    return super.dispatchKeyEvent(event);
                }
                if (event.getAction() == 0 && event.getRepeatCount() == 0) {
                    KeyEvent.DispatcherState state2 = getKeyDispatcherState();
                    if (state2 != null) {
                        state2.startTracking(event, this);
                    }
                    return true;
                } else if (event.getAction() == 1 && (state = getKeyDispatcherState()) != null && state.isTracking(event) && !event.isCanceled()) {
                    PopupWindow.this.dismiss();
                    return true;
                } else {
                    return super.dispatchKeyEvent(event);
                }
            }
            return super.dispatchKeyEvent(event);
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchTouchEvent(MotionEvent ev) {
            if (PopupWindow.this.mTouchInterceptor != null && PopupWindow.this.mTouchInterceptor.onTouch(this, ev)) {
                return true;
            }
            return super.dispatchTouchEvent(ev);
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (event.getAction() == 0 && (x < 0 || x >= getWidth() || y < 0 || y >= getHeight())) {
                PopupWindow.this.dismiss();
                return true;
            } else if (event.getAction() == 4) {
                PopupWindow.this.dismiss();
                return true;
            } else {
                return super.onTouchEvent(event);
            }
        }

        public synchronized void requestEnterTransition(Transition transition) {
            ViewTreeObserver observer = getViewTreeObserver();
            if (observer != null && transition != null) {
                final Transition enterTransition = transition.mo52clone();
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: android.widget.PopupWindow.PopupDecorView.1
                    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                    public void onGlobalLayout() {
                        ViewTreeObserver observer2 = PopupDecorView.this.getViewTreeObserver();
                        if (observer2 != null) {
                            observer2.removeOnGlobalLayoutListener(this);
                        }
                        final Rect epicenter = PopupWindow.this.getTransitionEpicenter();
                        enterTransition.setEpicenterCallback(new Transition.EpicenterCallback() { // from class: android.widget.PopupWindow.PopupDecorView.1.1
                            @Override // android.transition.Transition.EpicenterCallback
                            public Rect onGetEpicenter(Transition transition2) {
                                return epicenter;
                            }
                        });
                        PopupDecorView.this.startEnterTransition(enterTransition);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void startEnterTransition(Transition enterTransition) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                enterTransition.addTarget(child);
                child.setTransitionVisibility(4);
            }
            TransitionManager.beginDelayedTransition(this, enterTransition);
            for (int i2 = 0; i2 < count; i2++) {
                getChildAt(i2).setTransitionVisibility(0);
            }
        }

        public synchronized void startExitTransition(final Transition transition, final View anchorRoot, final Rect epicenter, final Transition.TransitionListener listener) {
            if (transition == null) {
                return;
            }
            if (anchorRoot != null) {
                anchorRoot.addOnAttachStateChangeListener(this.mOnAnchorRootDetachedListener);
            }
            this.mCleanupAfterExit = new Runnable() { // from class: android.widget.-$$Lambda$PopupWindow$PopupDecorView$T99WKEnQefOCXbbKvW95WY38p_I
                @Override // java.lang.Runnable
                public final void run() {
                    PopupWindow.PopupDecorView.lambda$startExitTransition$0(PopupWindow.PopupDecorView.this, listener, transition, anchorRoot);
                }
            };
            Transition exitTransition = transition.mo52clone();
            exitTransition.addListener(new TransitionListenerAdapter() { // from class: android.widget.PopupWindow.PopupDecorView.2
                @Override // android.transition.TransitionListenerAdapter, android.transition.Transition.TransitionListener
                public void onTransitionEnd(Transition t) {
                    t.removeListener(this);
                    if (PopupDecorView.this.mCleanupAfterExit != null) {
                        PopupDecorView.this.mCleanupAfterExit.run();
                    }
                }
            });
            exitTransition.setEpicenterCallback(new Transition.EpicenterCallback() { // from class: android.widget.PopupWindow.PopupDecorView.3
                @Override // android.transition.Transition.EpicenterCallback
                public Rect onGetEpicenter(Transition transition2) {
                    return epicenter;
                }
            });
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                exitTransition.addTarget(child);
            }
            TransitionManager.beginDelayedTransition(this, exitTransition);
            for (int i2 = 0; i2 < count; i2++) {
                View child2 = getChildAt(i2);
                child2.setVisibility(4);
            }
        }

        public static /* synthetic */ void lambda$startExitTransition$0(PopupDecorView popupDecorView, Transition.TransitionListener listener, Transition transition, View anchorRoot) {
            listener.onTransitionEnd(transition);
            if (anchorRoot != null) {
                anchorRoot.removeOnAttachStateChangeListener(popupDecorView.mOnAnchorRootDetachedListener);
            }
            popupDecorView.mCleanupAfterExit = null;
        }

        public synchronized void cancelTransitions() {
            TransitionManager.endTransitions(this);
            if (this.mCleanupAfterExit != null) {
                this.mCleanupAfterExit.run();
            }
        }

        @Override // android.view.View
        public synchronized void requestKeyboardShortcuts(List<KeyboardShortcutGroup> list, int deviceId) {
            View parentRoot;
            if (PopupWindow.this.mParentRootView != null && (parentRoot = (View) PopupWindow.this.mParentRootView.get()) != null) {
                parentRoot.requestKeyboardShortcuts(list, deviceId);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class PopupBackgroundView extends FrameLayout {
        public PopupBackgroundView(Context context) {
            super(context);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.view.ViewGroup, android.view.View
        public int[] onCreateDrawableState(int extraSpace) {
            if (PopupWindow.this.mAboveAnchor) {
                int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
                View.mergeDrawableStates(drawableState, PopupWindow.ABOVE_ANCHOR_STATE_SET);
                return drawableState;
            }
            return super.onCreateDrawableState(extraSpace);
        }
    }
}
