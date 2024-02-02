package android.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewRootImpl;
import android.view.WindowManager;
import com.android.internal.R;
@Deprecated
/* loaded from: classes3.dex */
public class ZoomButtonsController implements View.OnTouchListener {
    private static final int MSG_DISMISS_ZOOM_CONTROLS = 3;
    private static final int MSG_POST_CONFIGURATION_CHANGED = 2;
    private static final int MSG_POST_SET_VISIBLE = 4;
    private static final String TAG = "ZoomButtonsController";
    private static final int ZOOM_CONTROLS_TIMEOUT = (int) ViewConfiguration.getZoomControlsTimeout();
    private static final int ZOOM_CONTROLS_TOUCH_PADDING = 20;
    private OnZoomListener mCallback;
    private final FrameLayout mContainer;
    private WindowManager.LayoutParams mContainerLayoutParams;
    private final Context mContext;
    private ZoomControls mControls;
    private boolean mIsVisible;
    private final View mOwnerView;
    private Runnable mPostedVisibleInitializer;
    private boolean mReleaseTouchListenerOnUp;
    private int mTouchPaddingScaledSq;
    private View mTouchTargetView;
    private final WindowManager mWindowManager;
    private boolean mAutoDismissControls = true;
    private final int[] mOwnerViewRawLocation = new int[2];
    private final int[] mContainerRawLocation = new int[2];
    private final int[] mTouchTargetWindowLocation = new int[2];
    private final Rect mTempRect = new Rect();
    private final int[] mTempIntArray = new int[2];
    private final IntentFilter mConfigurationChangedFilter = new IntentFilter(Intent.ACTION_CONFIGURATION_CHANGED);
    private final BroadcastReceiver mConfigurationChangedReceiver = new BroadcastReceiver() { // from class: android.widget.ZoomButtonsController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (ZoomButtonsController.this.mIsVisible) {
                ZoomButtonsController.this.mHandler.removeMessages(2);
                ZoomButtonsController.this.mHandler.sendEmptyMessage(2);
            }
        }
    };
    private final Handler mHandler = new Handler() { // from class: android.widget.ZoomButtonsController.2
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    ZoomButtonsController.this.onPostConfigurationChanged();
                    return;
                case 3:
                    ZoomButtonsController.this.setVisible(false);
                    return;
                case 4:
                    if (ZoomButtonsController.this.mOwnerView.getWindowToken() == null) {
                        Log.e(ZoomButtonsController.TAG, "Cannot make the zoom controller visible if the owner view is not attached to a window.");
                        return;
                    } else {
                        ZoomButtonsController.this.setVisible(true);
                        return;
                    }
                default:
                    return;
            }
        }
    };

    /* loaded from: classes3.dex */
    public interface OnZoomListener {
        void onVisibilityChanged(boolean z);

        void onZoom(boolean z);
    }

    public ZoomButtonsController(View ownerView) {
        this.mContext = ownerView.getContext();
        this.mWindowManager = (WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE);
        this.mOwnerView = ownerView;
        this.mTouchPaddingScaledSq = (int) (20.0f * this.mContext.getResources().getDisplayMetrics().density);
        this.mTouchPaddingScaledSq *= this.mTouchPaddingScaledSq;
        this.mContainer = createContainer();
    }

    public void setZoomInEnabled(boolean enabled) {
        this.mControls.setIsZoomInEnabled(enabled);
    }

    public void setZoomOutEnabled(boolean enabled) {
        this.mControls.setIsZoomOutEnabled(enabled);
    }

    public void setZoomSpeed(long speed) {
        this.mControls.setZoomSpeed(speed);
    }

    private synchronized FrameLayout createContainer() {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(-2, -2);
        lp.gravity = 8388659;
        lp.flags = 131608;
        lp.height = -2;
        lp.width = -1;
        lp.type = 1000;
        lp.format = -3;
        lp.windowAnimations = R.style.Animation_ZoomButtons;
        this.mContainerLayoutParams = lp;
        FrameLayout container = new Container(this.mContext);
        container.setLayoutParams(lp);
        container.setMeasureAllChildren(true);
        LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.zoom_container, container);
        this.mControls = (ZoomControls) container.findViewById(16909620);
        this.mControls.setOnZoomInClickListener(new View.OnClickListener() { // from class: android.widget.ZoomButtonsController.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ZoomButtonsController.this.dismissControlsDelayed(ZoomButtonsController.ZOOM_CONTROLS_TIMEOUT);
                if (ZoomButtonsController.this.mCallback != null) {
                    ZoomButtonsController.this.mCallback.onZoom(true);
                }
            }
        });
        this.mControls.setOnZoomOutClickListener(new View.OnClickListener() { // from class: android.widget.ZoomButtonsController.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ZoomButtonsController.this.dismissControlsDelayed(ZoomButtonsController.ZOOM_CONTROLS_TIMEOUT);
                if (ZoomButtonsController.this.mCallback != null) {
                    ZoomButtonsController.this.mCallback.onZoom(false);
                }
            }
        });
        return container;
    }

    public void setOnZoomListener(OnZoomListener listener) {
        this.mCallback = listener;
    }

    public void setFocusable(boolean focusable) {
        int oldFlags = this.mContainerLayoutParams.flags;
        if (focusable) {
            this.mContainerLayoutParams.flags &= -9;
        } else {
            this.mContainerLayoutParams.flags |= 8;
        }
        if (this.mContainerLayoutParams.flags != oldFlags && this.mIsVisible) {
            this.mWindowManager.updateViewLayout(this.mContainer, this.mContainerLayoutParams);
        }
    }

    public boolean isAutoDismissed() {
        return this.mAutoDismissControls;
    }

    public void setAutoDismissed(boolean autoDismiss) {
        if (this.mAutoDismissControls == autoDismiss) {
            return;
        }
        this.mAutoDismissControls = autoDismiss;
    }

    public boolean isVisible() {
        return this.mIsVisible;
    }

    public void setVisible(boolean visible) {
        if (visible) {
            if (this.mOwnerView.getWindowToken() == null) {
                if (!this.mHandler.hasMessages(4)) {
                    this.mHandler.sendEmptyMessage(4);
                    return;
                }
                return;
            }
            dismissControlsDelayed(ZOOM_CONTROLS_TIMEOUT);
        }
        if (this.mIsVisible == visible) {
            return;
        }
        this.mIsVisible = visible;
        if (visible) {
            if (this.mContainerLayoutParams.token == null) {
                this.mContainerLayoutParams.token = this.mOwnerView.getWindowToken();
            }
            this.mWindowManager.addView(this.mContainer, this.mContainerLayoutParams);
            if (this.mPostedVisibleInitializer == null) {
                this.mPostedVisibleInitializer = new Runnable() { // from class: android.widget.ZoomButtonsController.5
                    @Override // java.lang.Runnable
                    public void run() {
                        ZoomButtonsController.this.refreshPositioningVariables();
                        if (ZoomButtonsController.this.mCallback != null) {
                            ZoomButtonsController.this.mCallback.onVisibilityChanged(true);
                        }
                    }
                };
            }
            this.mHandler.post(this.mPostedVisibleInitializer);
            this.mContext.registerReceiver(this.mConfigurationChangedReceiver, this.mConfigurationChangedFilter);
            this.mOwnerView.setOnTouchListener(this);
            this.mReleaseTouchListenerOnUp = false;
            return;
        }
        if (this.mTouchTargetView != null) {
            this.mReleaseTouchListenerOnUp = true;
        } else {
            this.mOwnerView.setOnTouchListener(null);
        }
        this.mContext.unregisterReceiver(this.mConfigurationChangedReceiver);
        this.mWindowManager.removeViewImmediate(this.mContainer);
        this.mHandler.removeCallbacks(this.mPostedVisibleInitializer);
        if (this.mCallback != null) {
            this.mCallback.onVisibilityChanged(false);
        }
    }

    public ViewGroup getContainer() {
        return this.mContainer;
    }

    public View getZoomControls() {
        return this.mControls;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void dismissControlsDelayed(int delay) {
        if (this.mAutoDismissControls) {
            this.mHandler.removeMessages(3);
            this.mHandler.sendEmptyMessageDelayed(3, delay);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void refreshPositioningVariables() {
        if (this.mOwnerView.getWindowToken() == null) {
            return;
        }
        int ownerHeight = this.mOwnerView.getHeight();
        int ownerWidth = this.mOwnerView.getWidth();
        int containerOwnerYOffset = ownerHeight - this.mContainer.getHeight();
        this.mOwnerView.getLocationOnScreen(this.mOwnerViewRawLocation);
        this.mContainerRawLocation[0] = this.mOwnerViewRawLocation[0];
        this.mContainerRawLocation[1] = this.mOwnerViewRawLocation[1] + containerOwnerYOffset;
        int[] ownerViewWindowLoc = this.mTempIntArray;
        this.mOwnerView.getLocationInWindow(ownerViewWindowLoc);
        this.mContainerLayoutParams.x = ownerViewWindowLoc[0];
        this.mContainerLayoutParams.width = ownerWidth;
        this.mContainerLayoutParams.y = ownerViewWindowLoc[1] + containerOwnerYOffset;
        if (this.mIsVisible) {
            this.mWindowManager.updateViewLayout(this.mContainer, this.mContainerLayoutParams);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean onContainerKey(KeyEvent event) {
        KeyEvent.DispatcherState ds;
        int keyCode = event.getKeyCode();
        if (isInterestingKey(keyCode)) {
            if (keyCode == 4) {
                if (event.getAction() == 0 && event.getRepeatCount() == 0) {
                    if (this.mOwnerView != null && (ds = this.mOwnerView.getKeyDispatcherState()) != null) {
                        ds.startTracking(event, this);
                    }
                    return true;
                } else if (event.getAction() == 1 && event.isTracking() && !event.isCanceled()) {
                    setVisible(false);
                    return true;
                }
            } else {
                dismissControlsDelayed(ZOOM_CONTROLS_TIMEOUT);
            }
            return false;
        }
        ViewRootImpl viewRoot = this.mOwnerView.getViewRootImpl();
        if (viewRoot != null) {
            viewRoot.dispatchInputEvent(event);
        }
        return true;
    }

    private synchronized boolean isInterestingKey(int keyCode) {
        if (keyCode == 4 || keyCode == 66) {
            return true;
        }
        switch (keyCode) {
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                return true;
            default:
                return false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0046  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0096 A[RETURN] */
    @Override // android.view.View.OnTouchListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouch(android.view.View r11, android.view.MotionEvent r12) {
        /*
            r10 = this;
            int r0 = r12.getAction()
            int r1 = r12.getPointerCount()
            r2 = 0
            r3 = 1
            if (r1 <= r3) goto Ld
            return r2
        Ld:
            boolean r1 = r10.mReleaseTouchListenerOnUp
            r4 = 3
            r5 = 0
            if (r1 == 0) goto L22
            if (r0 == r3) goto L17
            if (r0 != r4) goto L21
        L17:
            android.view.View r1 = r10.mOwnerView
            r1.setOnTouchListener(r5)
            r10.setTouchTargetView(r5)
            r10.mReleaseTouchListenerOnUp = r2
        L21:
            return r3
        L22:
            int r1 = android.widget.ZoomButtonsController.ZOOM_CONTROLS_TIMEOUT
            r10.dismissControlsDelayed(r1)
            android.view.View r1 = r10.mTouchTargetView
            if (r0 == r4) goto L41
            switch(r0) {
                case 0: goto L2f;
                case 1: goto L41;
                default: goto L2e;
            }
        L2e:
            goto L44
        L2f:
            float r4 = r12.getRawX()
            int r4 = (int) r4
            float r5 = r12.getRawY()
            int r5 = (int) r5
            android.view.View r1 = r10.findViewForTouch(r4, r5)
            r10.setTouchTargetView(r1)
            goto L44
        L41:
            r10.setTouchTargetView(r5)
        L44:
            if (r1 == 0) goto L96
            int[] r4 = r10.mContainerRawLocation
            r4 = r4[r2]
            int[] r5 = r10.mTouchTargetWindowLocation
            r5 = r5[r2]
            int r4 = r4 + r5
            int[] r5 = r10.mContainerRawLocation
            r5 = r5[r3]
            int[] r6 = r10.mTouchTargetWindowLocation
            r6 = r6[r3]
            int r5 = r5 + r6
            android.view.MotionEvent r6 = android.view.MotionEvent.obtain(r12)
            int[] r7 = r10.mOwnerViewRawLocation
            r2 = r7[r2]
            int r2 = r2 - r4
            float r2 = (float) r2
            int[] r7 = r10.mOwnerViewRawLocation
            r3 = r7[r3]
            int r3 = r3 - r5
            float r3 = (float) r3
            r6.offsetLocation(r2, r3)
            float r2 = r6.getX()
            float r3 = r6.getY()
            r7 = 0
            int r8 = (r2 > r7 ? 1 : (r2 == r7 ? 0 : -1))
            r9 = -1046478848(0xffffffffc1a00000, float:-20.0)
            if (r8 >= 0) goto L82
            int r8 = (r2 > r9 ? 1 : (r2 == r9 ? 0 : -1))
            if (r8 <= 0) goto L82
            float r8 = -r2
            r6.offsetLocation(r8, r7)
        L82:
            int r8 = (r3 > r7 ? 1 : (r3 == r7 ? 0 : -1))
            if (r8 >= 0) goto L8e
            int r8 = (r3 > r9 ? 1 : (r3 == r9 ? 0 : -1))
            if (r8 <= 0) goto L8e
            float r8 = -r3
            r6.offsetLocation(r7, r8)
        L8e:
            boolean r7 = r1.dispatchTouchEvent(r6)
            r6.recycle()
            return r7
        L96:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.ZoomButtonsController.onTouch(android.view.View, android.view.MotionEvent):boolean");
    }

    private synchronized void setTouchTargetView(View view) {
        this.mTouchTargetView = view;
        if (view != null) {
            view.getLocationInWindow(this.mTouchTargetWindowLocation);
        }
    }

    private synchronized View findViewForTouch(int rawX, int rawY) {
        int distanceX;
        int distanceY;
        int containerCoordsX = rawX - this.mContainerRawLocation[0];
        int containerCoordsY = rawY - this.mContainerRawLocation[1];
        Rect frame = this.mTempRect;
        View closestChild = null;
        int closestChildDistanceSq = Integer.MAX_VALUE;
        int i = this.mContainer.getChildCount() - 1;
        while (true) {
            int i2 = i;
            if (i2 >= 0) {
                View child = this.mContainer.getChildAt(i2);
                if (child.getVisibility() == 0) {
                    child.getHitRect(frame);
                    if (frame.contains(containerCoordsX, containerCoordsY)) {
                        return child;
                    }
                    if (containerCoordsX >= frame.left && containerCoordsX <= frame.right) {
                        distanceX = 0;
                    } else {
                        int distanceX2 = frame.left;
                        distanceX = Math.min(Math.abs(distanceX2 - containerCoordsX), Math.abs(containerCoordsX - frame.right));
                    }
                    if (containerCoordsY >= frame.top && containerCoordsY <= frame.bottom) {
                        distanceY = 0;
                    } else {
                        int distanceY2 = frame.top;
                        distanceY = Math.min(Math.abs(distanceY2 - containerCoordsY), Math.abs(containerCoordsY - frame.bottom));
                    }
                    int distanceSq = (distanceX * distanceX) + (distanceY * distanceY);
                    if (distanceSq < this.mTouchPaddingScaledSq && distanceSq < closestChildDistanceSq) {
                        closestChild = child;
                        closestChildDistanceSq = distanceSq;
                    }
                }
                i = i2 - 1;
            } else {
                return closestChild;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onPostConfigurationChanged() {
        dismissControlsDelayed(ZOOM_CONTROLS_TIMEOUT);
        refreshPositioningVariables();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class Container extends FrameLayout {
        public Container(Context context) {
            super(context);
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean dispatchKeyEvent(KeyEvent event) {
            if (ZoomButtonsController.this.onContainerKey(event)) {
                return true;
            }
            return super.dispatchKeyEvent(event);
        }
    }
}
