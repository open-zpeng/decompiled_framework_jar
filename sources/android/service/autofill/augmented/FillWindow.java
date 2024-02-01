package android.service.autofill.augmented;

import android.annotation.SystemApi;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.service.autofill.augmented.AugmentedAutofillService;
import android.service.autofill.augmented.PresentationParams;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.autofill.IAutofillWindowPresenter;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import com.android.internal.util.function.pooled.PooledLambda;
import dalvik.system.CloseGuard;
import java.io.PrintWriter;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@SystemApi
/* loaded from: classes2.dex */
public final class FillWindow implements AutoCloseable {
    private static final String TAG = FillWindow.class.getSimpleName();
    @GuardedBy({"mLock"})
    private Rect mBounds;
    @GuardedBy({"mLock"})
    private boolean mDestroyed;
    @GuardedBy({"mLock"})
    private View mFillView;
    private AugmentedAutofillService.AutofillProxy mProxy;
    @GuardedBy({"mLock"})
    private boolean mShowing;
    @GuardedBy({"mLock"})
    private boolean mUpdateCalled;
    @GuardedBy({"mLock"})
    private WindowManager mWm;
    private final Object mLock = new Object();
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private final Handler mUiThreadHandler = new Handler(Looper.getMainLooper());
    private final FillWindowPresenter mFillWindowPresenter = new FillWindowPresenter();

    public boolean update(PresentationParams.Area area, View rootView, long flags) {
        if (AugmentedAutofillService.sDebug) {
            String str = TAG;
            Log.d(str, "Updating " + area + " + with " + rootView);
        }
        Preconditions.checkNotNull(area);
        Preconditions.checkNotNull(area.proxy);
        Preconditions.checkNotNull(rootView);
        PresentationParams smartSuggestion = area.proxy.getSmartSuggestionParams();
        if (smartSuggestion == null) {
            Log.w(TAG, "No SmartSuggestionParams");
            return false;
        }
        Rect rect = area.getBounds();
        if (rect == null) {
            Log.wtf(TAG, "No Rect on SmartSuggestionParams");
            return false;
        }
        synchronized (this.mLock) {
            checkNotDestroyedLocked();
            this.mProxy = area.proxy;
            this.mWm = (WindowManager) rootView.getContext().getSystemService(WindowManager.class);
            this.mFillView = rootView;
            this.mFillView.setOnTouchListener(new View.OnTouchListener() { // from class: android.service.autofill.augmented.-$$Lambda$FillWindow$j5yljDJ4VfFL37B-iWRonGGOKN4
                @Override // android.view.View.OnTouchListener
                public final boolean onTouch(View view, MotionEvent motionEvent) {
                    return FillWindow.this.lambda$update$0$FillWindow(view, motionEvent);
                }
            });
            this.mShowing = false;
            this.mBounds = new Rect(area.getBounds());
            if (AugmentedAutofillService.sDebug) {
                String str2 = TAG;
                Log.d(str2, "Created FillWindow: params= " + smartSuggestion + " view=" + rootView);
            }
            this.mUpdateCalled = true;
            this.mDestroyed = false;
            this.mProxy.setFillWindow(this);
        }
        return true;
    }

    public /* synthetic */ boolean lambda$update$0$FillWindow(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 4) {
            if (AugmentedAutofillService.sVerbose) {
                Log.v(TAG, "Outside touch detected, hiding the window");
            }
            hide();
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void show() {
        if (AugmentedAutofillService.sDebug) {
            Log.d(TAG, "show()");
        }
        synchronized (this.mLock) {
            checkNotDestroyedLocked();
            if (this.mWm == null || this.mFillView == null) {
                throw new IllegalStateException("update() not called yet, or already destroyed()");
            }
            if (this.mProxy != null) {
                try {
                    this.mProxy.requestShowFillUi(this.mBounds.right - this.mBounds.left, this.mBounds.bottom - this.mBounds.top, null, this.mFillWindowPresenter);
                } catch (RemoteException e) {
                    Log.w(TAG, "Error requesting to show fill window", e);
                }
                this.mProxy.report(2);
            }
        }
    }

    private void hide() {
        if (AugmentedAutofillService.sDebug) {
            Log.d(TAG, "hide()");
        }
        synchronized (this.mLock) {
            checkNotDestroyedLocked();
            if (this.mWm == null || this.mFillView == null) {
                throw new IllegalStateException("update() not called yet, or already destroyed()");
            }
            if (this.mProxy != null && this.mShowing) {
                try {
                    this.mProxy.requestHideFillUi();
                } catch (RemoteException e) {
                    Log.w(TAG, "Error requesting to hide fill window", e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleShow(WindowManager.LayoutParams p) {
        if (AugmentedAutofillService.sDebug) {
            Log.d(TAG, "handleShow()");
        }
        synchronized (this.mLock) {
            if (this.mWm != null && this.mFillView != null) {
                p.flags |= 262144;
                if (!this.mShowing) {
                    this.mWm.addView(this.mFillView, p);
                    this.mShowing = true;
                } else {
                    this.mWm.updateViewLayout(this.mFillView, p);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleHide() {
        if (AugmentedAutofillService.sDebug) {
            Log.d(TAG, "handleHide()");
        }
        synchronized (this.mLock) {
            if (this.mWm != null && this.mFillView != null && this.mShowing) {
                this.mWm.removeView(this.mFillView);
                this.mShowing = false;
            }
        }
    }

    public void destroy() {
        if (AugmentedAutofillService.sDebug) {
            String str = TAG;
            Log.d(str, "destroy(): mDestroyed=" + this.mDestroyed + " mShowing=" + this.mShowing + " mFillView=" + this.mFillView);
        }
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                return;
            }
            if (this.mUpdateCalled) {
                this.mFillView.setOnClickListener(null);
                hide();
                this.mProxy.report(3);
            }
            this.mDestroyed = true;
            this.mCloseGuard.close();
        }
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mCloseGuard != null) {
                this.mCloseGuard.warnIfOpen();
            }
            destroy();
        } finally {
            super.finalize();
        }
    }

    private void checkNotDestroyedLocked() {
        if (this.mDestroyed) {
            throw new IllegalStateException("already destroyed()");
        }
    }

    public void dump(String prefix, PrintWriter pw) {
        synchronized (this) {
            pw.print(prefix);
            pw.print("destroyed: ");
            pw.println(this.mDestroyed);
            pw.print(prefix);
            pw.print("updateCalled: ");
            pw.println(this.mUpdateCalled);
            if (this.mFillView != null) {
                pw.print(prefix);
                pw.print("fill window: ");
                pw.println(this.mShowing ? "shown" : "hidden");
                pw.print(prefix);
                pw.print("fill view: ");
                pw.println(this.mFillView);
                pw.print(prefix);
                pw.print("mBounds: ");
                pw.println(this.mBounds);
                pw.print(prefix);
                pw.print("mWm: ");
                pw.println(this.mWm);
            }
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() throws Exception {
        destroy();
    }

    /* loaded from: classes2.dex */
    private final class FillWindowPresenter extends IAutofillWindowPresenter.Stub {
        private FillWindowPresenter() {
        }

        @Override // android.view.autofill.IAutofillWindowPresenter
        public void show(WindowManager.LayoutParams p, Rect transitionEpicenter, boolean fitsSystemWindows, int layoutDirection) {
            if (AugmentedAutofillService.sDebug) {
                Log.d(FillWindow.TAG, "FillWindowPresenter.show()");
            }
            FillWindow.this.mUiThreadHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: android.service.autofill.augmented.-$$Lambda$FillWindow$FillWindowPresenter$hdkNZGuYdsvcArvQ2SoMspO1EO8
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    ((FillWindow) obj).handleShow((WindowManager.LayoutParams) obj2);
                }
            }, FillWindow.this, p));
        }

        @Override // android.view.autofill.IAutofillWindowPresenter
        public void hide(Rect transitionEpicenter) {
            if (AugmentedAutofillService.sDebug) {
                Log.d(FillWindow.TAG, "FillWindowPresenter.hide()");
            }
            FillWindow.this.mUiThreadHandler.sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: android.service.autofill.augmented.-$$Lambda$FillWindow$FillWindowPresenter$EnBAJTZRgK05SBPnOQ9Edaq3VXs
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((FillWindow) obj).handleHide();
                }
            }, FillWindow.this));
        }
    }
}
