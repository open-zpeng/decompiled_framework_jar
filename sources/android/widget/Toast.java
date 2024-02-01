package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.app.INotificationManager;
import android.app.ITransientNotification;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.R;
import com.xiaopeng.view.xpWindowManager;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes3.dex */
public class Toast {
    public static final int LENGTH_LONG = 1;
    public static final int LENGTH_LONGER = 2;
    public static final int LENGTH_SHORT = 0;
    static final String TAG = "Toast";
    static final boolean localLOGV = false;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private static INotificationManager sService;
    final Context mContext;
    @UnsupportedAppUsage
    int mDuration;
    View mNextView;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    final TN mTN;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes3.dex */
    public @interface Duration {
    }

    static /* synthetic */ INotificationManager access$100() {
        return getService();
    }

    public Toast(Context context) {
        this(context, null);
    }

    public Toast(Context context, Looper looper) {
        this.mContext = context;
        this.mTN = new TN(context.getPackageName(), looper);
        this.mTN.mY = context.getResources().getDimensionPixelSize(R.dimen.toast_y_offset);
        this.mTN.mGravity = context.getResources().getInteger(R.integer.config_toastDefaultGravity);
    }

    public void show() {
        if (this.mNextView == null) {
            throw new RuntimeException("setView must have been called");
        }
        INotificationManager service = getService();
        String pkg = this.mContext.getOpPackageName();
        TN tn = this.mTN;
        tn.mNextView = this.mNextView;
        int displayId = this.mContext.getDisplayId();
        try {
            service.enqueueToast(pkg, tn, this.mDuration, displayId, tn.mSharedId);
        } catch (RemoteException e) {
        }
    }

    public void cancel() {
        this.mTN.cancel();
    }

    public void setView(View view) {
        this.mNextView = view;
    }

    public View getView() {
        return this.mNextView;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
        this.mTN.mDuration = duration;
    }

    public int getDuration() {
        return this.mDuration;
    }

    public void setMargin(float horizontalMargin, float verticalMargin) {
        TN tn = this.mTN;
        tn.mHorizontalMargin = horizontalMargin;
        tn.mVerticalMargin = verticalMargin;
    }

    public float getHorizontalMargin() {
        return this.mTN.mHorizontalMargin;
    }

    public float getVerticalMargin() {
        return this.mTN.mVerticalMargin;
    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        TN tn = this.mTN;
        tn.mGravity = gravity;
        tn.mX = xOffset;
        tn.mY = yOffset;
    }

    public int getGravity() {
        return this.mTN.mGravity;
    }

    public int getXOffset() {
        return this.mTN.mX;
    }

    public int getYOffset() {
        return this.mTN.mY;
    }

    @UnsupportedAppUsage
    public WindowManager.LayoutParams getWindowParams() {
        return this.mTN.mParams;
    }

    public static Toast makeText(Context context, CharSequence text, int duration) {
        return makeText(context, null, text, duration);
    }

    public static Toast makeText(Context context, Looper looper, CharSequence text, int duration) {
        Toast result = new Toast(context, looper);
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.transient_notification, (ViewGroup) null);
        TextView tv = (TextView) v.findViewById(16908299);
        tv.setText(text);
        result.mNextView = v;
        result.mDuration = duration;
        return result;
    }

    public static Toast makeText(Context context, int resId, int duration) throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }

    public void setText(int resId) {
        setText(this.mContext.getText(resId));
    }

    public void setText(CharSequence s) {
        View view = this.mNextView;
        if (view == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        TextView tv = (TextView) view.findViewById(16908299);
        if (tv == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        tv.setText(s);
    }

    public void setSharedId(int sharedId) {
        TN tn = this.mTN;
        if (tn != null) {
            tn.mSharedId = sharedId;
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    private static INotificationManager getService() {
        INotificationManager iNotificationManager = sService;
        if (iNotificationManager != null) {
            return iNotificationManager;
        }
        sService = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
        return sService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class TN extends ITransientNotification.Stub {
        private static final int CANCEL = 2;
        private static final int HIDE = 1;
        static final long LONG_DURATION_TIMEOUT = 4000;
        static final long SHORT_DURATION_TIMEOUT = 2500;
        private static final int SHOW = 0;
        int mDuration;
        @UnsupportedAppUsage(maxTargetSdk = 28)
        int mGravity;
        final Handler mHandler;
        float mHorizontalMargin;
        @UnsupportedAppUsage(maxTargetSdk = 28)
        View mNextView;
        String mPackageName;
        @UnsupportedAppUsage(maxTargetSdk = 28)
        private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
        int mSharedId = -1;
        float mVerticalMargin;
        @UnsupportedAppUsage(maxTargetSdk = 28)
        View mView;
        WindowManager mWM;
        int mX;
        @UnsupportedAppUsage(maxTargetSdk = 28)
        int mY;

        TN(String packageName, Looper looper) {
            WindowManager.LayoutParams params = this.mParams;
            params.height = -2;
            params.width = -2;
            params.format = -3;
            params.windowAnimations = 16973828;
            params.type = 2005;
            params.setTitle(Toast.TAG);
            params.flags = 152;
            this.mPackageName = packageName;
            if (looper == null && (looper = Looper.myLooper()) == null) {
                throw new RuntimeException("Can't toast on a thread that has not called Looper.prepare()");
            }
            this.mHandler = new Handler(looper, null) { // from class: android.widget.Toast.TN.1
                @Override // android.os.Handler
                public void handleMessage(Message msg) {
                    int i = msg.what;
                    if (i == 0) {
                        IBinder token = (IBinder) msg.obj;
                        TN.this.handleShow(token);
                    } else if (i == 1) {
                        TN.this.handleHide();
                        TN.this.mNextView = null;
                    } else if (i == 2) {
                        TN.this.handleHide();
                        TN.this.mNextView = null;
                        try {
                            Toast.access$100().cancelToast(TN.this.mPackageName, TN.this);
                        } catch (RemoteException e) {
                        }
                    }
                }
            };
        }

        @Override // android.app.ITransientNotification
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
        public void show(IBinder windowToken) {
            this.mHandler.obtainMessage(0, windowToken).sendToTarget();
        }

        @Override // android.app.ITransientNotification
        public void hide() {
            this.mHandler.obtainMessage(1).sendToTarget();
        }

        public void cancel() {
            this.mHandler.obtainMessage(2).sendToTarget();
        }

        public void handleShow(IBinder windowToken) {
            if (!this.mHandler.hasMessages(2) && !this.mHandler.hasMessages(1) && this.mView != this.mNextView) {
                handleHide();
                this.mView = this.mNextView;
                Context context = this.mView.getContext().getApplicationContext();
                String packageName = this.mView.getContext().getOpPackageName();
                if (context == null) {
                    context = this.mView.getContext();
                }
                this.mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                Configuration config = this.mView.getContext().getResources().getConfiguration();
                int gravity = Gravity.getAbsoluteGravity(this.mGravity, config.getLayoutDirection());
                WindowManager.LayoutParams layoutParams = this.mParams;
                layoutParams.gravity = gravity;
                if ((gravity & 7) == 7) {
                    layoutParams.horizontalWeight = 1.0f;
                }
                if ((gravity & 112) == 112) {
                    this.mParams.verticalWeight = 1.0f;
                }
                WindowManager.LayoutParams layoutParams2 = this.mParams;
                layoutParams2.x = this.mX;
                layoutParams2.y = this.mY;
                layoutParams2.verticalMargin = this.mVerticalMargin;
                layoutParams2.horizontalMargin = this.mHorizontalMargin;
                layoutParams2.packageName = packageName;
                layoutParams2.hideTimeoutMilliseconds = xpWindowManager.getToastDurationMillis(this.mDuration);
                int i = this.mSharedId;
                if (i == 0) {
                    this.mParams.xpFlags |= 16;
                } else if (i == 1) {
                    this.mParams.xpFlags |= 32;
                }
                this.mParams.token = windowToken;
                if (this.mView.getParent() != null) {
                    this.mWM.removeView(this.mView);
                }
                try {
                    this.mWM.addView(this.mView, this.mParams);
                    trySendAccessibilityEvent();
                } catch (WindowManager.BadTokenException e) {
                } catch (IllegalStateException ie) {
                    Log.e(Toast.TAG, "showToast fail view=" + this.mView + " error=" + ie);
                }
            }
        }

        private void trySendAccessibilityEvent() {
            AccessibilityManager accessibilityManager = AccessibilityManager.getInstance(this.mView.getContext());
            if (!accessibilityManager.isEnabled()) {
                return;
            }
            AccessibilityEvent event = AccessibilityEvent.obtain(64);
            event.setClassName(getClass().getName());
            event.setPackageName(this.mView.getContext().getPackageName());
            this.mView.dispatchPopulateAccessibilityEvent(event);
            accessibilityManager.sendAccessibilityEvent(event);
        }

        @UnsupportedAppUsage
        public void handleHide() {
            View view = this.mView;
            if (view != null) {
                if (view.getParent() != null) {
                    this.mWM.removeViewImmediate(this.mView);
                }
                try {
                    Toast.access$100().finishToken(this.mPackageName, this);
                } catch (RemoteException e) {
                }
                this.mView = null;
            }
        }
    }
}
