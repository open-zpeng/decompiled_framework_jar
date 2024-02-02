package android.view.inputmethod;

import android.content.Context;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.Trace;
import android.provider.SettingsStringUtil;
import android.text.style.SuggestionSpan;
import android.util.Log;
import android.util.Pools;
import android.util.PrintWriterPrinter;
import android.util.Printer;
import android.util.SparseArray;
import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputEventSender;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewRootImpl;
import android.view.autofill.AutofillManager;
import com.android.internal.inputmethod.IInputContentUriToken;
import com.android.internal.os.SomeArgs;
import com.android.internal.view.IInputConnectionWrapper;
import com.android.internal.view.IInputContext;
import com.android.internal.view.IInputMethodClient;
import com.android.internal.view.IInputMethodManager;
import com.android.internal.view.IInputMethodSession;
import com.android.internal.view.InputBindResult;
import com.android.internal.view.InputMethodClient;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
/* loaded from: classes2.dex */
public final class InputMethodManager {
    public static final int CONTROL_START_INITIAL = 256;
    public static final int CONTROL_WINDOW_FIRST = 4;
    public static final int CONTROL_WINDOW_IS_TEXT_EDITOR = 2;
    public static final int CONTROL_WINDOW_VIEW_HAS_FOCUS = 1;
    static final boolean DEBUG;
    public static final int DISPATCH_HANDLED = 1;
    public static final int DISPATCH_IN_PROGRESS = -1;
    public static final int DISPATCH_NOT_HANDLED = 0;
    public static final int HIDE_IMPLICIT_ONLY = 1;
    public static final int HIDE_NOT_ALWAYS = 2;
    static final long INPUT_METHOD_NOT_RESPONDING_TIMEOUT = 2500;
    static final int MSG_BIND = 2;
    static final int MSG_DUMP = 1;
    static final int MSG_FLUSH_INPUT_EVENT = 7;
    static final int MSG_REPORT_FULLSCREEN_MODE = 10;
    static final int MSG_SEND_INPUT_EVENT = 5;
    static final int MSG_SET_ACTIVE = 4;
    static final int MSG_SET_USER_ACTION_NOTIFICATION_SEQUENCE_NUMBER = 9;
    static final int MSG_TIMEOUT_INPUT_EVENT = 6;
    static final int MSG_UNBIND = 3;
    private static final int NOT_AN_ACTION_NOTIFICATION_SEQUENCE_NUMBER = -1;
    static final String PENDING_EVENT_COUNTER = "aq:imm";
    private static final int REQUEST_UPDATE_CURSOR_ANCHOR_INFO_NONE = 0;
    public static final int RESULT_HIDDEN = 3;
    public static final int RESULT_SHOWN = 2;
    public static final int RESULT_UNCHANGED_HIDDEN = 1;
    public static final int RESULT_UNCHANGED_SHOWN = 0;
    public static final int SHOW_FORCED = 2;
    public static final int SHOW_IMPLICIT = 1;
    public static final int SHOW_IM_PICKER_MODE_AUTO = 0;
    public static final int SHOW_IM_PICKER_MODE_EXCLUDE_AUXILIARY_SUBTYPES = 2;
    public static final int SHOW_IM_PICKER_MODE_INCLUDE_AUXILIARY_SUBTYPES = 1;
    static final String TAG = "InputMethodManager";
    public private protected static InputMethodManager sInstance;
    boolean mActive;
    int mBindSequence;
    final IInputMethodClient.Stub mClient;
    CompletionInfo[] mCompletions;
    InputChannel mCurChannel;
    public private protected String mCurId;
    public private protected IInputMethodSession mCurMethod;
    public private protected View mCurRootView;
    ImeInputEventSender mCurSender;
    EditorInfo mCurrentTextBoxAttribute;
    private CursorAnchorInfo mCursorAnchorInfo;
    int mCursorCandEnd;
    int mCursorCandStart;
    public private protected Rect mCursorRect;
    int mCursorSelEnd;
    int mCursorSelStart;
    final InputConnection mDummyInputConnection;
    boolean mFullscreenMode;
    public private protected final H mH;
    final IInputContext mIInputContext;
    private int mLastSentUserActionNotificationSequenceNumber;
    final Looper mMainLooper;
    public private protected View mNextServedView;
    private int mNextUserActionNotificationSequenceNumber;
    final Pools.Pool<PendingEvent> mPendingEventPool;
    final SparseArray<PendingEvent> mPendingEvents;
    private int mRequestUpdateCursorAnchorInfoMonitorMode;
    boolean mRestartOnNextWindowFocus;
    boolean mServedConnecting;
    public private protected ControlledInputConnectionWrapper mServedInputConnectionWrapper;
    public private protected View mServedView;
    public private protected final IInputMethodManager mService;
    public private protected Rect mTmpCursorRect;

    /* loaded from: classes2.dex */
    public interface FinishedInputEventCallback {
        synchronized void onFinishedInputEvent(Object obj, boolean z);
    }

    static {
        DEBUG = SystemProperties.getInt("persist.debug.input", 0) == 1;
    }

    private static synchronized boolean isAutofillUIShowing(View servedView) {
        AutofillManager afm = (AutofillManager) servedView.getContext().getSystemService(AutofillManager.class);
        return afm != null && afm.isAutofillUiShowing();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized boolean canStartInput(View servedView) {
        return servedView.hasWindowFocus() || isAutofillUIShowing(servedView);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class H extends Handler {
        H(Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    SomeArgs args = (SomeArgs) msg.obj;
                    try {
                        InputMethodManager.this.doDump((FileDescriptor) args.arg1, (PrintWriter) args.arg2, (String[]) args.arg3);
                    } catch (RuntimeException e) {
                        ((PrintWriter) args.arg2).println("Exception: " + e);
                    }
                    synchronized (args.arg4) {
                        ((CountDownLatch) args.arg4).countDown();
                    }
                    args.recycle();
                    return;
                case 2:
                    InputBindResult res = (InputBindResult) msg.obj;
                    if (InputMethodManager.DEBUG) {
                        Log.i(InputMethodManager.TAG, "handleMessage: MSG_BIND " + res.sequence + "," + res.id);
                    }
                    synchronized (InputMethodManager.this.mH) {
                        if (InputMethodManager.this.mBindSequence >= 0 && InputMethodManager.this.mBindSequence == res.sequence) {
                            InputMethodManager.this.mRequestUpdateCursorAnchorInfoMonitorMode = 0;
                            InputMethodManager.this.setInputChannelLocked(res.channel);
                            InputMethodManager.this.mCurMethod = res.method;
                            InputMethodManager.this.mCurId = res.id;
                            InputMethodManager.this.mBindSequence = res.sequence;
                            InputMethodManager.this.startInputInner(5, null, 0, 0, 0);
                            return;
                        }
                        Log.w(InputMethodManager.TAG, "Ignoring onBind: cur seq=" + InputMethodManager.this.mBindSequence + ", given seq=" + res.sequence);
                        if (res.channel != null && res.channel != InputMethodManager.this.mCurChannel) {
                            res.channel.dispose();
                        }
                        return;
                    }
                case 3:
                    int sequence = msg.arg1;
                    int reason = msg.arg2;
                    if (InputMethodManager.DEBUG) {
                        Log.i(InputMethodManager.TAG, "handleMessage: MSG_UNBIND " + sequence + " reason=" + InputMethodClient.getUnbindReason(reason));
                    }
                    synchronized (InputMethodManager.this.mH) {
                        if (InputMethodManager.this.mBindSequence != sequence) {
                            return;
                        }
                        InputMethodManager.this.clearBindingLocked();
                        if (InputMethodManager.this.mServedView != null && InputMethodManager.this.mServedView.isFocused()) {
                            InputMethodManager.this.mServedConnecting = true;
                        }
                        boolean startInput = InputMethodManager.this.mActive;
                        if (startInput) {
                            InputMethodManager.this.startInputInner(6, null, 0, 0, 0);
                            return;
                        }
                        return;
                    }
                case 4:
                    boolean active = msg.arg1 != 0;
                    boolean fullscreen = msg.arg2 != 0;
                    if (InputMethodManager.DEBUG) {
                        Log.i(InputMethodManager.TAG, "handleMessage: MSG_SET_ACTIVE " + active + ", was " + InputMethodManager.this.mActive);
                    }
                    synchronized (InputMethodManager.this.mH) {
                        InputMethodManager.this.mActive = active;
                        InputMethodManager.this.mFullscreenMode = fullscreen;
                        if (!active) {
                            InputMethodManager.this.mRestartOnNextWindowFocus = true;
                            try {
                                InputMethodManager.this.mIInputContext.finishComposingText();
                            } catch (RemoteException e2) {
                            }
                        }
                        if (InputMethodManager.this.mServedView != null && InputMethodManager.canStartInput(InputMethodManager.this.mServedView) && InputMethodManager.this.checkFocusNoStartInput(InputMethodManager.this.mRestartOnNextWindowFocus)) {
                            int reason2 = active ? 7 : 8;
                            InputMethodManager.this.startInputInner(reason2, null, 0, 0, 0);
                        }
                    }
                    return;
                case 5:
                    InputMethodManager.this.sendInputEventAndReportResultOnMainLooper((PendingEvent) msg.obj);
                    return;
                case 6:
                    InputMethodManager.this.finishedInputEvent(msg.arg1, false, true);
                    return;
                case 7:
                    InputMethodManager.this.finishedInputEvent(msg.arg1, false, false);
                    return;
                case 8:
                default:
                    return;
                case 9:
                    synchronized (InputMethodManager.this.mH) {
                        InputMethodManager.this.mNextUserActionNotificationSequenceNumber = msg.arg1;
                    }
                    return;
                case 10:
                    boolean fullscreen2 = msg.arg1 != 0;
                    InputConnection ic = null;
                    synchronized (InputMethodManager.this.mH) {
                        InputMethodManager.this.mFullscreenMode = fullscreen2;
                        if (InputMethodManager.this.mServedInputConnectionWrapper != null) {
                            ic = InputMethodManager.this.mServedInputConnectionWrapper.getInputConnection();
                        }
                    }
                    if (ic != null) {
                        ic.reportFullscreenMode(fullscreen2);
                        return;
                    }
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class ControlledInputConnectionWrapper extends IInputConnectionWrapper {
        private final InputMethodManager mParentInputMethodManager;

        public synchronized ControlledInputConnectionWrapper(Looper mainLooper, InputConnection conn, InputMethodManager inputMethodManager) {
            super(mainLooper, conn);
            this.mParentInputMethodManager = inputMethodManager;
        }

        @Override // com.android.internal.view.IInputConnectionWrapper
        public synchronized boolean isActive() {
            return this.mParentInputMethodManager.mActive && !isFinished();
        }

        synchronized void deactivate() {
            if (isFinished()) {
                return;
            }
            closeConnection();
        }

        @Override // com.android.internal.view.IInputConnectionWrapper
        protected synchronized void onUserAction() {
            this.mParentInputMethodManager.notifyUserAction();
        }

        public String toString() {
            return "ControlledInputConnectionWrapper{connection=" + getInputConnection() + " finished=" + isFinished() + " mParentInputMethodManager.mActive=" + this.mParentInputMethodManager.mActive + "}";
        }
    }

    synchronized InputMethodManager(Looper looper) throws ServiceManager.ServiceNotFoundException {
        this(IInputMethodManager.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.INPUT_METHOD_SERVICE)), looper);
    }

    synchronized InputMethodManager(IInputMethodManager service, Looper looper) {
        this.mActive = false;
        this.mRestartOnNextWindowFocus = true;
        this.mTmpCursorRect = new Rect();
        this.mCursorRect = new Rect();
        this.mNextUserActionNotificationSequenceNumber = -1;
        this.mLastSentUserActionNotificationSequenceNumber = -1;
        this.mCursorAnchorInfo = null;
        this.mBindSequence = -1;
        this.mRequestUpdateCursorAnchorInfoMonitorMode = 0;
        this.mPendingEventPool = new Pools.SimplePool(20);
        this.mPendingEvents = new SparseArray<>(20);
        this.mClient = new IInputMethodClient.Stub() { // from class: android.view.inputmethod.InputMethodManager.1
            @Override // android.os.Binder
            protected void dump(FileDescriptor fd, PrintWriter fout, String[] args) {
                CountDownLatch latch = new CountDownLatch(1);
                SomeArgs sargs = SomeArgs.obtain();
                sargs.arg1 = fd;
                sargs.arg2 = fout;
                sargs.arg3 = args;
                sargs.arg4 = latch;
                InputMethodManager.this.mH.sendMessage(InputMethodManager.this.mH.obtainMessage(1, sargs));
                try {
                    if (!latch.await(5L, TimeUnit.SECONDS)) {
                        fout.println("Timeout waiting for dump");
                    }
                } catch (InterruptedException e) {
                    fout.println("Interrupted waiting for dump");
                }
            }

            public void setUsingInputMethod(boolean state) {
            }

            public void onBindMethod(InputBindResult res) {
                InputMethodManager.this.mH.obtainMessage(2, res).sendToTarget();
            }

            @Override // com.android.internal.view.IInputMethodClient
            public void onUnbindMethod(int sequence, int unbindReason) {
                InputMethodManager.this.mH.obtainMessage(3, sequence, unbindReason).sendToTarget();
            }

            @Override // com.android.internal.view.IInputMethodClient
            public void setActive(boolean active, boolean fullscreen) {
                InputMethodManager.this.mH.obtainMessage(4, active ? 1 : 0, fullscreen ? 1 : 0).sendToTarget();
            }

            @Override // com.android.internal.view.IInputMethodClient
            public void setUserActionNotificationSequenceNumber(int sequenceNumber) {
                InputMethodManager.this.mH.obtainMessage(9, sequenceNumber, 0).sendToTarget();
            }

            @Override // com.android.internal.view.IInputMethodClient
            public void reportFullscreenMode(boolean fullscreen) {
                InputMethodManager.this.mH.obtainMessage(10, fullscreen ? 1 : 0, 0).sendToTarget();
            }
        };
        this.mDummyInputConnection = new BaseInputConnection(this, false);
        this.mService = service;
        this.mMainLooper = looper;
        this.mH = new H(looper);
        this.mIInputContext = new ControlledInputConnectionWrapper(looper, this.mDummyInputConnection, this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static InputMethodManager getInstance() {
        InputMethodManager inputMethodManager;
        synchronized (InputMethodManager.class) {
            if (sInstance == null) {
                try {
                    sInstance = new InputMethodManager(Looper.getMainLooper());
                } catch (ServiceManager.ServiceNotFoundException e) {
                    throw new IllegalStateException(e);
                }
            }
            inputMethodManager = sInstance;
        }
        return inputMethodManager;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static InputMethodManager peekInstance() {
        return sInstance;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IInputMethodClient getClient() {
        return this.mClient;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IInputContext getInputContext() {
        return this.mIInputContext;
    }

    public List<InputMethodInfo> getInputMethodList() {
        try {
            return this.mService.getInputMethodList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean getInputShown() {
        try {
            return this.mService.getInputShown();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void forceHideSoftInputMethod() {
        try {
            this.mService.forceHideSoftInputMethod();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized List<InputMethodInfo> getVrInputMethodList() {
        try {
            return this.mService.getVrInputMethodList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<InputMethodInfo> getEnabledInputMethodList() {
        try {
            return this.mService.getEnabledInputMethodList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<InputMethodSubtype> getEnabledInputMethodSubtypeList(InputMethodInfo imi, boolean allowsImplicitlySelectedSubtypes) {
        try {
            return this.mService.getEnabledInputMethodSubtypeList(imi == null ? null : imi.getId(), allowsImplicitlySelectedSubtypes);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void showStatusIcon(IBinder imeToken, String packageName, int iconId) {
        showStatusIconInternal(imeToken, packageName, iconId);
    }

    public synchronized void showStatusIconInternal(IBinder imeToken, String packageName, int iconId) {
        try {
            this.mService.updateStatusIcon(imeToken, packageName, iconId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void hideStatusIcon(IBinder imeToken) {
        hideStatusIconInternal(imeToken);
    }

    public synchronized void hideStatusIconInternal(IBinder imeToken) {
        try {
            this.mService.updateStatusIcon(imeToken, null, 0);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void setImeWindowStatus(IBinder imeToken, IBinder startInputToken, int vis, int backDisposition) {
        try {
            this.mService.setImeWindowStatus(imeToken, startInputToken, vis, backDisposition);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerSuggestionSpansForNotification(SuggestionSpan[] spans) {
        try {
            this.mService.registerSuggestionSpansForNotification(spans);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifySuggestionPicked(SuggestionSpan span, String originalString, int index) {
        try {
            this.mService.notifySuggestionPicked(span, originalString, index);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isFullscreenMode() {
        boolean z;
        synchronized (this.mH) {
            z = this.mFullscreenMode;
        }
        return z;
    }

    public synchronized void reportFullscreenMode(IBinder token, boolean fullscreen) {
        try {
            this.mService.reportFullscreenMode(token, fullscreen);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isActive(View view) {
        boolean z;
        checkFocus();
        synchronized (this.mH) {
            z = (this.mServedView == view || (this.mServedView != null && this.mServedView.checkInputConnectionProxy(view))) && this.mCurrentTextBoxAttribute != null;
        }
        return z;
    }

    public boolean isActive() {
        boolean z;
        checkFocus();
        synchronized (this.mH) {
            z = (this.mServedView == null || this.mCurrentTextBoxAttribute == null) ? false : true;
        }
        return z;
    }

    public boolean isAcceptingText() {
        checkFocus();
        return (this.mServedInputConnectionWrapper == null || this.mServedInputConnectionWrapper.getInputConnection() == null) ? false : true;
    }

    synchronized void clearBindingLocked() {
        if (DEBUG) {
            Log.v(TAG, "Clearing binding!");
        }
        clearConnectionLocked();
        setInputChannelLocked(null);
        this.mBindSequence = -1;
        this.mCurId = null;
        this.mCurMethod = null;
    }

    synchronized void setInputChannelLocked(InputChannel channel) {
        if (this.mCurChannel != channel) {
            if (this.mCurSender != null) {
                flushPendingEventsLocked();
                this.mCurSender.dispose();
                this.mCurSender = null;
            }
            if (this.mCurChannel != null) {
                this.mCurChannel.dispose();
            }
            this.mCurChannel = channel;
        }
    }

    synchronized void clearConnectionLocked() {
        this.mCurrentTextBoxAttribute = null;
        if (this.mServedInputConnectionWrapper != null) {
            this.mServedInputConnectionWrapper.deactivate();
            this.mServedInputConnectionWrapper = null;
        }
    }

    public private protected void finishInputLocked() {
        this.mNextServedView = null;
        if (this.mServedView != null) {
            if (DEBUG) {
                Log.v(TAG, "FINISH INPUT: mServedView=" + dumpViewInfo(this.mServedView));
            }
            if (this.mCurrentTextBoxAttribute != null) {
                try {
                    this.mService.finishInput(this.mClient);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            this.mServedView = null;
            this.mCompletions = null;
            this.mServedConnecting = false;
            clearConnectionLocked();
        }
    }

    public void displayCompletions(View view, CompletionInfo[] completions) {
        checkFocus();
        synchronized (this.mH) {
            if (this.mServedView == view || (this.mServedView != null && this.mServedView.checkInputConnectionProxy(view))) {
                this.mCompletions = completions;
                if (this.mCurMethod != null) {
                    try {
                        this.mCurMethod.displayCompletions(this.mCompletions);
                    } catch (RemoteException e) {
                    }
                }
            }
        }
    }

    public void updateExtractedText(View view, int token, ExtractedText text) {
        checkFocus();
        synchronized (this.mH) {
            if (this.mServedView == view || (this.mServedView != null && this.mServedView.checkInputConnectionProxy(view))) {
                if (this.mCurMethod != null) {
                    try {
                        this.mCurMethod.updateExtractedText(token, text);
                    } catch (RemoteException e) {
                    }
                }
            }
        }
    }

    public boolean showSoftInput(View view, int flags) {
        return showSoftInput(view, flags, null);
    }

    public boolean showSoftInput(View view, int flags, ResultReceiver resultReceiver) {
        checkFocus();
        synchronized (this.mH) {
            Log.d(TAG, "showSoftInput mServedView:" + this.mServedView + " view:" + view + "flags:" + flags);
            if (this.mServedView != view && (this.mServedView == null || !this.mServedView.checkInputConnectionProxy(view))) {
                return false;
            }
            try {
                return this.mService.showSoftInput(this.mClient, flags, resultReceiver);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    @Deprecated
    private protected void showSoftInputUnchecked(int flags, ResultReceiver resultReceiver) {
        try {
            Log.w(TAG, "showSoftInputUnchecked() is a hidden method, which will be removed soon. If you are using android.support.v7.widget.SearchView, please update to version 26.0 or newer version.");
            this.mService.showSoftInput(this.mClient, flags, resultReceiver);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean hideSoftInputFromWindow(IBinder windowToken, int flags) {
        return hideSoftInputFromWindow(windowToken, flags, null);
    }

    public boolean hideSoftInputFromWindow(IBinder windowToken, int flags, ResultReceiver resultReceiver) {
        checkFocus();
        synchronized (this.mH) {
            if (this.mServedView == null || this.mServedView.getWindowToken() != windowToken) {
                return false;
            }
            try {
                return this.mService.hideSoftInput(this.mClient, flags, resultReceiver);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void toggleSoftInputFromWindow(IBinder windowToken, int showFlags, int hideFlags) {
        synchronized (this.mH) {
            if (this.mServedView != null && this.mServedView.getWindowToken() == windowToken) {
                if (this.mCurMethod != null) {
                    try {
                        this.mCurMethod.toggleSoftInput(showFlags, hideFlags);
                    } catch (RemoteException e) {
                    }
                }
            }
        }
    }

    public void toggleSoftInput(int showFlags, int hideFlags) {
        if (this.mCurMethod != null) {
            try {
                this.mCurMethod.toggleSoftInput(showFlags, hideFlags);
            } catch (RemoteException e) {
            }
        }
    }

    public void restartInput(View view) {
        checkFocus();
        synchronized (this.mH) {
            if (this.mServedView == view || (this.mServedView != null && this.mServedView.checkInputConnectionProxy(view))) {
                this.mServedConnecting = true;
                startInputInner(3, null, 0, 0, 0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean startInputInner(final int startInputReason, IBinder windowGainingFocus, int controlFlags, int softInputMode, int windowFlags) {
        H h;
        int i;
        ControlledInputConnectionWrapper servedContext;
        int missingMethodFlags;
        boolean z;
        H h2;
        int controlFlags2;
        InputBindResult res;
        synchronized (this.mH) {
            try {
                View view = this.mServedView;
                if (DEBUG) {
                    Log.v(TAG, "Starting input: view=" + dumpViewInfo(view) + " reason=" + InputMethodClient.getStartInputReason(startInputReason));
                }
                if (view == null) {
                    if (DEBUG) {
                        Log.v(TAG, "ABORT input: no served view!");
                    }
                    return false;
                }
                Handler vh = view.getHandler();
                if (vh == null) {
                    if (DEBUG) {
                        Log.v(TAG, "ABORT input: no handler for view! Close current input.");
                    }
                    closeCurrentInput();
                    return false;
                } else if (vh.getLooper() != Looper.myLooper()) {
                    if (DEBUG) {
                        Log.v(TAG, "Starting input: reschedule to view thread");
                    }
                    vh.post(new Runnable() { // from class: android.view.inputmethod.-$$Lambda$InputMethodManager$jNoqB3BbMToNjx3pS-WwvtHoFfg
                        @Override // java.lang.Runnable
                        public final void run() {
                            InputMethodManager.this.startInputInner(startInputReason, null, 0, 0, 0);
                        }
                    });
                    return false;
                } else {
                    EditorInfo tba = new EditorInfo();
                    tba.packageName = view.getContext().getOpPackageName();
                    tba.fieldId = view.getId();
                    InputConnection ic = view.onCreateInputConnection(tba);
                    if (DEBUG) {
                        Log.v(TAG, "Starting input: tba=" + tba + " ic=" + ic);
                    }
                    H h3 = this.mH;
                    synchronized (h3) {
                        try {
                            try {
                                if (this.mServedView != view) {
                                    i = controlFlags;
                                    h = h3;
                                } else if (this.mServedConnecting) {
                                    int controlFlags3 = this.mCurrentTextBoxAttribute == null ? controlFlags | 256 : controlFlags;
                                    try {
                                        this.mCurrentTextBoxAttribute = tba;
                                        this.mServedConnecting = false;
                                        if (this.mServedInputConnectionWrapper != null) {
                                            try {
                                                this.mServedInputConnectionWrapper.deactivate();
                                                this.mServedInputConnectionWrapper = null;
                                            } catch (Throwable th) {
                                                th = th;
                                                h = h3;
                                                throw th;
                                            }
                                        }
                                        if (ic != null) {
                                            this.mCursorSelStart = tba.initialSelStart;
                                            this.mCursorSelEnd = tba.initialSelEnd;
                                            this.mCursorCandStart = -1;
                                            this.mCursorCandEnd = -1;
                                            this.mCursorRect.setEmpty();
                                            this.mCursorAnchorInfo = null;
                                            int missingMethodFlags2 = InputConnectionInspector.getMissingMethodFlags(ic);
                                            Handler icHandler = (missingMethodFlags2 & 32) != 0 ? null : ic.getHandler();
                                            ControlledInputConnectionWrapper servedContext2 = new ControlledInputConnectionWrapper(icHandler != null ? icHandler.getLooper() : vh.getLooper(), ic, this);
                                            missingMethodFlags = missingMethodFlags2;
                                            servedContext = servedContext2;
                                        } else {
                                            servedContext = null;
                                            missingMethodFlags = 0;
                                        }
                                        this.mServedInputConnectionWrapper = servedContext;
                                        try {
                                            if (DEBUG) {
                                                try {
                                                    Log.v(TAG, "START INPUT: view=" + dumpViewInfo(view) + " ic=" + ic + " tba=" + tba + " controlFlags=#" + Integer.toHexString(controlFlags3));
                                                } catch (RemoteException e) {
                                                    e = e;
                                                    z = true;
                                                    h2 = h3;
                                                    Log.w(TAG, "IME died: " + this.mCurId, e);
                                                    return z;
                                                }
                                            }
                                            controlFlags2 = controlFlags3;
                                            h2 = h3;
                                            try {
                                                res = this.mService.startInputOrWindowGainedFocus(startInputReason, this.mClient, windowGainingFocus, controlFlags3, softInputMode, windowFlags, tba, servedContext, missingMethodFlags, view.getContext().getApplicationInfo().targetSdkVersion);
                                                if (DEBUG) {
                                                    Log.v(TAG, "Starting input: Bind result=" + res);
                                                }
                                            } catch (RemoteException e2) {
                                                e = e2;
                                                z = true;
                                            }
                                        } catch (RemoteException e3) {
                                            e = e3;
                                            z = true;
                                            h2 = h3;
                                        }
                                        if (res == null) {
                                            Log.wtf(TAG, "startInputOrWindowGainedFocus must not return null. startInputReason=" + InputMethodClient.getStartInputReason(startInputReason) + " editorInfo=" + tba + " controlFlags=#" + Integer.toHexString(controlFlags2));
                                            return false;
                                        }
                                        if (res.id != null) {
                                            setInputChannelLocked(res.channel);
                                            this.mBindSequence = res.sequence;
                                            this.mCurMethod = res.method;
                                            this.mCurId = res.id;
                                            this.mNextUserActionNotificationSequenceNumber = res.userActionNotificationSequenceNumber;
                                        } else if (res.channel != null && res.channel != this.mCurChannel) {
                                            res.channel.dispose();
                                        }
                                        if (res.result != 11) {
                                            z = true;
                                        } else {
                                            z = true;
                                            try {
                                                this.mRestartOnNextWindowFocus = true;
                                            } catch (RemoteException e4) {
                                                e = e4;
                                                Log.w(TAG, "IME died: " + this.mCurId, e);
                                                return z;
                                            }
                                        }
                                        if (this.mCurMethod != null) {
                                            if (this.mCompletions != null) {
                                                try {
                                                    this.mCurMethod.displayCompletions(this.mCompletions);
                                                } catch (RemoteException e5) {
                                                }
                                            }
                                        }
                                        return z;
                                    } catch (Throwable th2) {
                                        th = th2;
                                        h = h3;
                                    }
                                } else {
                                    i = controlFlags;
                                    h = h3;
                                }
                            } catch (Throwable th3) {
                                th = th3;
                            }
                            try {
                                if (DEBUG) {
                                    Log.v(TAG, "Starting input: finished by someone else. view=" + dumpViewInfo(view) + " mServedView=" + dumpViewInfo(this.mServedView) + " mServedConnecting=" + this.mServedConnecting);
                                }
                                return false;
                            } catch (Throwable th4) {
                                th = th4;
                                throw th;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            h = h3;
                        }
                    }
                }
            } catch (Throwable th6) {
                th = th6;
                while (true) {
                    try {
                        break;
                    } catch (Throwable th7) {
                        th = th7;
                    }
                }
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void windowDismissed(IBinder appWindowToken) {
        checkFocus();
        synchronized (this.mH) {
            if (this.mServedView != null && this.mServedView.getWindowToken() == appWindowToken) {
                finishInputLocked();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void focusIn(View view) {
        synchronized (this.mH) {
            focusInLocked(view);
        }
    }

    synchronized void focusInLocked(View view) {
        if (DEBUG) {
            Log.v(TAG, "focusIn: " + dumpViewInfo(view));
        }
        if (view != null && view.isTemporarilyDetached()) {
            if (DEBUG) {
                Log.v(TAG, "Temporarily detached view, ignoring");
            }
        } else if (this.mCurRootView != view.getRootView()) {
            if (DEBUG) {
                Log.v(TAG, "Not IME target window, ignoring");
            }
        } else {
            this.mNextServedView = view;
            scheduleCheckFocusLocked(view);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void focusOut(View view) {
        synchronized (this.mH) {
            if (DEBUG) {
                Log.v(TAG, "focusOut: view=" + dumpViewInfo(view) + " mServedView=" + dumpViewInfo(this.mServedView));
            }
            View view2 = this.mServedView;
        }
    }

    public synchronized void onViewDetachedFromWindow(View view) {
        synchronized (this.mH) {
            if (DEBUG) {
                Log.v(TAG, "onViewDetachedFromWindow: view=" + dumpViewInfo(view) + " mServedView=" + dumpViewInfo(this.mServedView));
            }
            if (this.mServedView == view) {
                this.mNextServedView = null;
                scheduleCheckFocusLocked(view);
            }
        }
    }

    static synchronized void scheduleCheckFocusLocked(View view) {
        ViewRootImpl viewRootImpl = view.getViewRootImpl();
        if (viewRootImpl != null) {
            viewRootImpl.dispatchCheckFocus();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkFocus() {
        if (checkFocusNoStartInput(false)) {
            startInputInner(4, null, 0, 0, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean checkFocusNoStartInput(boolean forceNewFocus) {
        if (this.mServedView != this.mNextServedView || forceNewFocus) {
            synchronized (this.mH) {
                if (this.mServedView != this.mNextServedView || forceNewFocus) {
                    if (DEBUG) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("checkFocus: view=");
                        sb.append(this.mServedView);
                        sb.append(" next=");
                        sb.append(this.mNextServedView);
                        sb.append(" forceNewFocus=");
                        sb.append(forceNewFocus);
                        sb.append(" package=");
                        sb.append(this.mServedView != null ? this.mServedView.getContext().getPackageName() : "<none>");
                        Log.v(TAG, sb.toString());
                    }
                    if (this.mNextServedView == null) {
                        finishInputLocked();
                        closeCurrentInput();
                        return false;
                    }
                    ControlledInputConnectionWrapper ic = this.mServedInputConnectionWrapper;
                    this.mServedView = this.mNextServedView;
                    this.mCurrentTextBoxAttribute = null;
                    this.mCompletions = null;
                    this.mServedConnecting = true;
                    if (ic != null) {
                        ic.finishComposingText();
                    }
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public private protected void closeCurrentInput() {
        try {
            this.mService.hideSoftInput(this.mClient, 2, null);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:49:0x00d3 -> B:50:0x00d4). Please submit an issue!!! */
    public synchronized void onPostWindowFocus(View rootView, View focusedView, int softInputMode, boolean first, int windowFlags) {
        boolean forceNewFocus;
        synchronized (this.mH) {
            try {
                if (DEBUG) {
                    Log.v(TAG, "onWindowFocus: " + focusedView + " softInputMode=" + InputMethodClient.softInputModeToString(softInputMode) + " first=" + first + " flags=#" + Integer.toHexString(windowFlags));
                }
                if (this.mRestartOnNextWindowFocus) {
                    if (DEBUG) {
                        Log.v(TAG, "Restarting due to mRestartOnNextWindowFocus");
                    }
                    this.mRestartOnNextWindowFocus = false;
                    forceNewFocus = true;
                } else {
                    forceNewFocus = false;
                }
                try {
                    focusInLocked(focusedView != null ? focusedView : rootView);
                    int controlFlags = 0;
                    if (focusedView != null) {
                        controlFlags = 0 | 1;
                        if (focusedView.onCheckIsTextEditor()) {
                            controlFlags |= 2;
                        }
                    }
                    if (first) {
                        controlFlags |= 4;
                    }
                    int controlFlags2 = controlFlags;
                    if (checkFocusNoStartInput(forceNewFocus) && startInputInner(1, rootView.getWindowToken(), controlFlags2, softInputMode, windowFlags)) {
                        return;
                    }
                    synchronized (this.mH) {
                        try {
                            try {
                                if (DEBUG) {
                                    Log.v(TAG, "Reporting focus gain, without startInput");
                                }
                                this.mService.startInputOrWindowGainedFocus(2, this.mClient, rootView.getWindowToken(), controlFlags2, softInputMode, windowFlags, null, null, 0, rootView.getContext().getApplicationInfo().targetSdkVersion);
                            } catch (RemoteException e) {
                                throw e.rethrowFromSystemServer();
                            }
                        } catch (Throwable th) {
                            throw th;
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPreWindowFocus(View rootView, boolean hasWindowFocus) {
        synchronized (this.mH) {
            if (rootView == null) {
                try {
                    this.mCurRootView = null;
                } catch (Throwable th) {
                    throw th;
                }
            }
            if (hasWindowFocus) {
                this.mCurRootView = rootView;
            } else if (rootView == this.mCurRootView) {
                this.mCurRootView = null;
            } else if (DEBUG) {
                Log.v(TAG, "Ignoring onPreWindowFocus(). mCurRootView=" + this.mCurRootView + " rootView=" + rootView);
            }
        }
    }

    public void updateSelection(View view, int selStart, int selEnd, int candidatesStart, int candidatesEnd) {
        checkFocus();
        synchronized (this.mH) {
            if ((this.mServedView == view || (this.mServedView != null && this.mServedView.checkInputConnectionProxy(view))) && this.mCurrentTextBoxAttribute != null && this.mCurMethod != null) {
                if (this.mCursorSelStart != selStart || this.mCursorSelEnd != selEnd || this.mCursorCandStart != candidatesStart || this.mCursorCandEnd != candidatesEnd) {
                    if (DEBUG) {
                        Log.d(TAG, "updateSelection");
                    }
                    try {
                        if (DEBUG) {
                            Log.v(TAG, "SELECTION CHANGE: " + this.mCurMethod);
                        }
                        int oldSelStart = this.mCursorSelStart;
                        int oldSelEnd = this.mCursorSelEnd;
                        this.mCursorSelStart = selStart;
                        this.mCursorSelEnd = selEnd;
                        this.mCursorCandStart = candidatesStart;
                        this.mCursorCandEnd = candidatesEnd;
                        this.mCurMethod.updateSelection(oldSelStart, oldSelEnd, selStart, selEnd, candidatesStart, candidatesEnd);
                    } catch (RemoteException e) {
                        Log.w(TAG, "IME died: " + this.mCurId, e);
                    }
                }
            }
        }
    }

    public void viewClicked(View view) {
        boolean focusChanged = this.mServedView != this.mNextServedView;
        checkFocus();
        synchronized (this.mH) {
            if ((this.mServedView != view && (this.mServedView == null || !this.mServedView.checkInputConnectionProxy(view))) || this.mCurrentTextBoxAttribute == null || this.mCurMethod == null) {
                return;
            }
            try {
                if (DEBUG) {
                    Log.v(TAG, "onViewClicked: " + focusChanged);
                }
                this.mCurMethod.viewClicked(focusChanged);
            } catch (RemoteException e) {
                Log.w(TAG, "IME died: " + this.mCurId, e);
            }
        }
    }

    @Deprecated
    public boolean isWatchingCursor(View view) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCursorAnchorInfoEnabled() {
        boolean z;
        synchronized (this.mH) {
            z = true;
            boolean isImmediate = (this.mRequestUpdateCursorAnchorInfoMonitorMode & 1) != 0;
            boolean isMonitoring = (this.mRequestUpdateCursorAnchorInfoMonitorMode & 2) != 0;
            if (!isImmediate && !isMonitoring) {
                z = false;
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setUpdateCursorAnchorInfoMode(int flags) {
        synchronized (this.mH) {
            this.mRequestUpdateCursorAnchorInfoMonitorMode = flags;
        }
    }

    @Deprecated
    public void updateCursor(View view, int left, int top, int right, int bottom) {
        checkFocus();
        synchronized (this.mH) {
            if ((this.mServedView == view || (this.mServedView != null && this.mServedView.checkInputConnectionProxy(view))) && this.mCurrentTextBoxAttribute != null && this.mCurMethod != null) {
                this.mTmpCursorRect.set(left, top, right, bottom);
                if (!this.mCursorRect.equals(this.mTmpCursorRect)) {
                    if (DEBUG) {
                        Log.d(TAG, "updateCursor");
                    }
                    try {
                        if (DEBUG) {
                            Log.v(TAG, "CURSOR CHANGE: " + this.mCurMethod);
                        }
                        this.mCurMethod.updateCursor(this.mTmpCursorRect);
                        this.mCursorRect.set(this.mTmpCursorRect);
                    } catch (RemoteException e) {
                        Log.w(TAG, "IME died: " + this.mCurId, e);
                    }
                }
            }
        }
    }

    public void updateCursorAnchorInfo(View view, CursorAnchorInfo cursorAnchorInfo) {
        if (view == null || cursorAnchorInfo == null) {
            return;
        }
        checkFocus();
        synchronized (this.mH) {
            if ((this.mServedView == view || (this.mServedView != null && this.mServedView.checkInputConnectionProxy(view))) && this.mCurrentTextBoxAttribute != null && this.mCurMethod != null) {
                boolean z = true;
                if ((this.mRequestUpdateCursorAnchorInfoMonitorMode & 1) == 0) {
                    z = false;
                }
                boolean isImmediate = z;
                if (!isImmediate && Objects.equals(this.mCursorAnchorInfo, cursorAnchorInfo)) {
                    if (DEBUG) {
                        Log.w(TAG, "Ignoring redundant updateCursorAnchorInfo: info=" + cursorAnchorInfo);
                    }
                    return;
                }
                if (DEBUG) {
                    Log.v(TAG, "updateCursorAnchorInfo: " + cursorAnchorInfo);
                }
                try {
                    this.mCurMethod.updateCursorAnchorInfo(cursorAnchorInfo);
                    this.mCursorAnchorInfo = cursorAnchorInfo;
                    this.mRequestUpdateCursorAnchorInfoMonitorMode &= -2;
                } catch (RemoteException e) {
                    Log.w(TAG, "IME died: " + this.mCurId, e);
                }
            }
        }
    }

    public void sendAppPrivateCommand(View view, String action, Bundle data) {
        checkFocus();
        synchronized (this.mH) {
            if ((this.mServedView != view && (this.mServedView == null || !this.mServedView.checkInputConnectionProxy(view))) || this.mCurrentTextBoxAttribute == null || this.mCurMethod == null) {
                return;
            }
            try {
                if (DEBUG) {
                    Log.v(TAG, "APP PRIVATE COMMAND " + action + ": " + data);
                }
                this.mCurMethod.appPrivateCommand(action, data);
            } catch (RemoteException e) {
                Log.w(TAG, "IME died: " + this.mCurId, e);
            }
        }
    }

    @Deprecated
    public void setInputMethod(IBinder token, String id) {
        setInputMethodInternal(token, id);
    }

    public synchronized void setInputMethodInternal(IBinder token, String id) {
        try {
            this.mService.setInputMethod(token, id);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void setInputMethodAndSubtype(IBinder token, String id, InputMethodSubtype subtype) {
        setInputMethodAndSubtypeInternal(token, id, subtype);
    }

    public synchronized void setInputMethodAndSubtypeInternal(IBinder token, String id, InputMethodSubtype subtype) {
        try {
            this.mService.setInputMethodAndSubtype(token, id, subtype);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void hideSoftInputFromInputMethod(IBinder token, int flags) {
        hideSoftInputFromInputMethodInternal(token, flags);
    }

    public synchronized void hideSoftInputFromInputMethodInternal(IBinder token, int flags) {
        try {
            this.mService.hideMySoftInput(token, flags);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public void showSoftInputFromInputMethod(IBinder token, int flags) {
        showSoftInputFromInputMethodInternal(token, flags);
    }

    public synchronized void showSoftInputFromInputMethodInternal(IBinder token, int flags) {
        try {
            this.mService.showMySoftInput(token, flags);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized int dispatchInputEvent(InputEvent event, Object token, FinishedInputEventCallback callback, Handler handler) {
        synchronized (this.mH) {
            if (this.mCurMethod != null) {
                if (event instanceof KeyEvent) {
                    KeyEvent keyEvent = (KeyEvent) event;
                    if (keyEvent.getAction() == 0 && keyEvent.getKeyCode() == 63 && keyEvent.getRepeatCount() == 0) {
                        showInputMethodPickerLocked();
                        return 1;
                    }
                }
                if (DEBUG) {
                    Log.v(TAG, "DISPATCH INPUT EVENT: " + this.mCurMethod);
                }
                PendingEvent p = obtainPendingEventLocked(event, token, this.mCurId, callback, handler);
                if (this.mMainLooper.isCurrentThread()) {
                    return sendInputEventOnMainLooperLocked(p);
                }
                Message msg = this.mH.obtainMessage(5, p);
                msg.setAsynchronous(true);
                this.mH.sendMessage(msg);
                return -1;
            }
            return 0;
        }
    }

    public void dispatchKeyEventFromInputMethod(View targetView, KeyEvent event) {
        ViewRootImpl viewRootImpl;
        synchronized (this.mH) {
            if (targetView == null) {
                viewRootImpl = null;
            } else {
                try {
                    viewRootImpl = targetView.getViewRootImpl();
                } catch (Throwable th) {
                    throw th;
                }
            }
            if (viewRootImpl == null && this.mServedView != null) {
                viewRootImpl = this.mServedView.getViewRootImpl();
            }
            if (viewRootImpl != null) {
                viewRootImpl.dispatchKeyFromIme(event);
            }
        }
    }

    synchronized void sendInputEventAndReportResultOnMainLooper(PendingEvent p) {
        synchronized (this.mH) {
            int result = sendInputEventOnMainLooperLocked(p);
            if (result == -1) {
                return;
            }
            boolean z = true;
            if (result != 1) {
                z = false;
            }
            boolean handled = z;
            invokeFinishedInputEventCallback(p, handled);
        }
    }

    synchronized int sendInputEventOnMainLooperLocked(PendingEvent p) {
        if (this.mCurChannel != null) {
            if (this.mCurSender == null) {
                this.mCurSender = new ImeInputEventSender(this.mCurChannel, this.mH.getLooper());
            }
            InputEvent event = p.mEvent;
            int seq = event.getSequenceNumber();
            if (this.mCurSender.sendInputEvent(seq, event)) {
                this.mPendingEvents.put(seq, p);
                Trace.traceCounter(4L, PENDING_EVENT_COUNTER, this.mPendingEvents.size());
                Message msg = this.mH.obtainMessage(6, seq, 0, p);
                msg.setAsynchronous(true);
                this.mH.sendMessageDelayed(msg, 2500L);
                return -1;
            }
            Log.w(TAG, "Unable to send input event to IME: " + this.mCurId + " dropping: " + event);
        }
        return 0;
    }

    synchronized void finishedInputEvent(int seq, boolean handled, boolean timeout) {
        synchronized (this.mH) {
            int index = this.mPendingEvents.indexOfKey(seq);
            if (index < 0) {
                return;
            }
            PendingEvent p = this.mPendingEvents.valueAt(index);
            this.mPendingEvents.removeAt(index);
            Trace.traceCounter(4L, PENDING_EVENT_COUNTER, this.mPendingEvents.size());
            if (timeout) {
                Log.w(TAG, "Timeout waiting for IME to handle input event after 2500 ms: " + p.mInputMethodId);
            } else {
                this.mH.removeMessages(6, p);
            }
            invokeFinishedInputEventCallback(p, handled);
        }
    }

    synchronized void invokeFinishedInputEventCallback(PendingEvent p, boolean handled) {
        p.mHandled = handled;
        if (p.mHandler.getLooper().isCurrentThread()) {
            p.run();
            return;
        }
        Message msg = Message.obtain(p.mHandler, p);
        msg.setAsynchronous(true);
        msg.sendToTarget();
    }

    private synchronized void flushPendingEventsLocked() {
        this.mH.removeMessages(7);
        int count = this.mPendingEvents.size();
        for (int i = 0; i < count; i++) {
            int seq = this.mPendingEvents.keyAt(i);
            Message msg = this.mH.obtainMessage(7, seq, 0);
            msg.setAsynchronous(true);
            msg.sendToTarget();
        }
    }

    private synchronized PendingEvent obtainPendingEventLocked(InputEvent event, Object token, String inputMethodId, FinishedInputEventCallback callback, Handler handler) {
        PendingEvent p = this.mPendingEventPool.acquire();
        if (p == null) {
            p = new PendingEvent();
        }
        p.mEvent = event;
        p.mToken = token;
        p.mInputMethodId = inputMethodId;
        p.mCallback = callback;
        p.mHandler = handler;
        return p;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void recyclePendingEventLocked(PendingEvent p) {
        p.recycle();
        this.mPendingEventPool.release(p);
    }

    public void showInputMethodPicker() {
        synchronized (this.mH) {
            showInputMethodPickerLocked();
        }
    }

    public synchronized void showInputMethodPicker(boolean showAuxiliarySubtypes) {
        int mode;
        synchronized (this.mH) {
            if (showAuxiliarySubtypes) {
                mode = 1;
            } else {
                mode = 2;
            }
            try {
                try {
                    this.mService.showInputMethodPickerFromClient(this.mClient, mode);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private synchronized void showInputMethodPickerLocked() {
        try {
            this.mService.showInputMethodPickerFromClient(this.mClient, 0);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isInputMethodPickerShown() {
        try {
            return this.mService.isInputMethodPickerShownForTest();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void showInputMethodAndSubtypeEnabler(String imiId) {
        synchronized (this.mH) {
            try {
                try {
                    this.mService.showInputMethodAndSubtypeEnablerFromClient(this.mClient, imiId);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public InputMethodSubtype getCurrentInputMethodSubtype() {
        try {
            return this.mService.getCurrentInputMethodSubtype();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean setCurrentInputMethodSubtype(InputMethodSubtype subtype) {
        boolean currentInputMethodSubtype;
        synchronized (this.mH) {
            try {
                try {
                    currentInputMethodSubtype = this.mService.setCurrentInputMethodSubtype(subtype);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return currentInputMethodSubtype;
    }

    private protected void notifyUserAction() {
        synchronized (this.mH) {
            if (this.mLastSentUserActionNotificationSequenceNumber == this.mNextUserActionNotificationSequenceNumber) {
                if (DEBUG) {
                    Log.w(TAG, "Ignoring notifyUserAction as it has already been sent. mLastSentUserActionNotificationSequenceNumber: " + this.mLastSentUserActionNotificationSequenceNumber + " mNextUserActionNotificationSequenceNumber: " + this.mNextUserActionNotificationSequenceNumber);
                }
                return;
            }
            try {
                if (DEBUG) {
                    Log.w(TAG, "notifyUserAction:  mLastSentUserActionNotificationSequenceNumber: " + this.mLastSentUserActionNotificationSequenceNumber + " mNextUserActionNotificationSequenceNumber: " + this.mNextUserActionNotificationSequenceNumber);
                }
                this.mService.notifyUserAction(this.mNextUserActionNotificationSequenceNumber);
                this.mLastSentUserActionNotificationSequenceNumber = this.mNextUserActionNotificationSequenceNumber;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x002c, code lost:
        android.util.Log.e(android.view.inputmethod.InputMethodManager.TAG, "IMI list already contains the same InputMethod.");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.util.Map<android.view.inputmethod.InputMethodInfo, java.util.List<android.view.inputmethod.InputMethodSubtype>> getShortcutInputMethodsAndSubtypes() {
        /*
            r9 = this;
            android.view.inputmethod.InputMethodManager$H r0 = r9.mH
            monitor-enter(r0)
            java.util.HashMap r1 = new java.util.HashMap     // Catch: java.lang.Throwable -> L59
            r1.<init>()     // Catch: java.lang.Throwable -> L59
            com.android.internal.view.IInputMethodManager r2 = r9.mService     // Catch: android.os.RemoteException -> L53 java.lang.Throwable -> L59
            java.util.List r2 = r2.getShortcutInputMethodsAndSubtypes()     // Catch: android.os.RemoteException -> L53 java.lang.Throwable -> L59
            r3 = 0
            if (r2 == 0) goto L50
            boolean r4 = r2.isEmpty()     // Catch: android.os.RemoteException -> L53 java.lang.Throwable -> L59
            if (r4 != 0) goto L50
            int r4 = r2.size()     // Catch: android.os.RemoteException -> L53 java.lang.Throwable -> L59
            r5 = 0
        L1c:
            if (r5 >= r4) goto L50
            java.lang.Object r6 = r2.get(r5)     // Catch: android.os.RemoteException -> L53 java.lang.Throwable -> L59
            boolean r7 = r6 instanceof android.view.inputmethod.InputMethodInfo     // Catch: android.os.RemoteException -> L53 java.lang.Throwable -> L59
            if (r7 == 0) goto L41
            boolean r7 = r1.containsKey(r6)     // Catch: android.os.RemoteException -> L53 java.lang.Throwable -> L59
            if (r7 == 0) goto L34
            java.lang.String r7 = "InputMethodManager"
            java.lang.String r8 = "IMI list already contains the same InputMethod."
            android.util.Log.e(r7, r8)     // Catch: android.os.RemoteException -> L53 java.lang.Throwable -> L59
            goto L50
        L34:
            java.util.ArrayList r7 = new java.util.ArrayList     // Catch: android.os.RemoteException -> L53 java.lang.Throwable -> L59
            r7.<init>()     // Catch: android.os.RemoteException -> L53 java.lang.Throwable -> L59
            r3 = r7
            r7 = r6
            android.view.inputmethod.InputMethodInfo r7 = (android.view.inputmethod.InputMethodInfo) r7     // Catch: android.os.RemoteException -> L53 java.lang.Throwable -> L59
            r1.put(r7, r3)     // Catch: android.os.RemoteException -> L53 java.lang.Throwable -> L59
            goto L4d
        L41:
            if (r3 == 0) goto L4d
            boolean r7 = r6 instanceof android.view.inputmethod.InputMethodSubtype     // Catch: android.os.RemoteException -> L53 java.lang.Throwable -> L59
            if (r7 == 0) goto L4d
            r7 = r6
            android.view.inputmethod.InputMethodSubtype r7 = (android.view.inputmethod.InputMethodSubtype) r7     // Catch: android.os.RemoteException -> L53 java.lang.Throwable -> L59
            r3.add(r7)     // Catch: android.os.RemoteException -> L53 java.lang.Throwable -> L59
        L4d:
            int r5 = r5 + 1
            goto L1c
        L50:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L59
            return r1
        L53:
            r2 = move-exception
            java.lang.RuntimeException r3 = r2.rethrowFromSystemServer()     // Catch: java.lang.Throwable -> L59
            throw r3     // Catch: java.lang.Throwable -> L59
        L59:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L59
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodManager.getShortcutInputMethodsAndSubtypes():java.util.Map");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getInputMethodWindowVisibleHeight() {
        int inputMethodWindowVisibleHeight;
        synchronized (this.mH) {
            try {
                try {
                    inputMethodWindowVisibleHeight = this.mService.getInputMethodWindowVisibleHeight();
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return inputMethodWindowVisibleHeight;
    }

    public synchronized void clearLastInputMethodWindowForTransition(IBinder token) {
        synchronized (this.mH) {
            try {
                try {
                    this.mService.clearLastInputMethodWindowForTransition(token);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Deprecated
    public boolean switchToLastInputMethod(IBinder imeToken) {
        return switchToPreviousInputMethodInternal(imeToken);
    }

    public synchronized boolean switchToPreviousInputMethodInternal(IBinder imeToken) {
        boolean switchToPreviousInputMethod;
        synchronized (this.mH) {
            try {
                try {
                    switchToPreviousInputMethod = this.mService.switchToPreviousInputMethod(imeToken);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return switchToPreviousInputMethod;
    }

    @Deprecated
    public boolean switchToNextInputMethod(IBinder imeToken, boolean onlyCurrentIme) {
        return switchToNextInputMethodInternal(imeToken, onlyCurrentIme);
    }

    public synchronized boolean switchToNextInputMethodInternal(IBinder imeToken, boolean onlyCurrentIme) {
        boolean switchToNextInputMethod;
        synchronized (this.mH) {
            try {
                try {
                    switchToNextInputMethod = this.mService.switchToNextInputMethod(imeToken, onlyCurrentIme);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return switchToNextInputMethod;
    }

    @Deprecated
    public boolean shouldOfferSwitchingToNextInputMethod(IBinder imeToken) {
        return shouldOfferSwitchingToNextInputMethodInternal(imeToken);
    }

    public synchronized boolean shouldOfferSwitchingToNextInputMethodInternal(IBinder imeToken) {
        boolean shouldOfferSwitchingToNextInputMethod;
        synchronized (this.mH) {
            try {
                try {
                    shouldOfferSwitchingToNextInputMethod = this.mService.shouldOfferSwitchingToNextInputMethod(imeToken);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return shouldOfferSwitchingToNextInputMethod;
    }

    public void setAdditionalInputMethodSubtypes(String imiId, InputMethodSubtype[] subtypes) {
        synchronized (this.mH) {
            try {
                try {
                    this.mService.setAdditionalInputMethodSubtypes(imiId, subtypes);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public InputMethodSubtype getLastInputMethodSubtype() {
        InputMethodSubtype lastInputMethodSubtype;
        synchronized (this.mH) {
            try {
                try {
                    lastInputMethodSubtype = this.mService.getLastInputMethodSubtype();
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return lastInputMethodSubtype;
    }

    public synchronized void exposeContent(IBinder token, InputContentInfo inputContentInfo, EditorInfo editorInfo) {
        Uri contentUri = inputContentInfo.getContentUri();
        try {
            IInputContentUriToken uriToken = this.mService.createInputContentUriToken(token, contentUri, editorInfo.packageName);
            if (uriToken == null) {
                return;
            }
            inputContentInfo.setUriToken(uriToken);
        } catch (RemoteException e) {
            Log.e(TAG, "createInputContentAccessToken failed. contentUri=" + contentUri.toString() + " packageName=" + editorInfo.packageName, e);
        }
    }

    synchronized void doDump(FileDescriptor fd, PrintWriter fout, String[] args) {
        Printer p = new PrintWriterPrinter(fout);
        p.println("Input method client state for " + this + SettingsStringUtil.DELIMITER);
        StringBuilder sb = new StringBuilder();
        sb.append("  mService=");
        sb.append(this.mService);
        p.println(sb.toString());
        p.println("  mMainLooper=" + this.mMainLooper);
        p.println("  mIInputContext=" + this.mIInputContext);
        p.println("  mActive=" + this.mActive + " mRestartOnNextWindowFocus=" + this.mRestartOnNextWindowFocus + " mBindSequence=" + this.mBindSequence + " mCurId=" + this.mCurId);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("  mFullscreenMode=");
        sb2.append(this.mFullscreenMode);
        p.println(sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append("  mCurMethod=");
        sb3.append(this.mCurMethod);
        p.println(sb3.toString());
        p.println("  mCurRootView=" + this.mCurRootView);
        p.println("  mServedView=" + this.mServedView);
        p.println("  mNextServedView=" + this.mNextServedView);
        p.println("  mServedConnecting=" + this.mServedConnecting);
        if (this.mCurrentTextBoxAttribute != null) {
            p.println("  mCurrentTextBoxAttribute:");
            this.mCurrentTextBoxAttribute.dump(p, "    ");
        } else {
            p.println("  mCurrentTextBoxAttribute: null");
        }
        p.println("  mServedInputConnectionWrapper=" + this.mServedInputConnectionWrapper);
        p.println("  mCompletions=" + Arrays.toString(this.mCompletions));
        p.println("  mCursorRect=" + this.mCursorRect);
        p.println("  mCursorSelStart=" + this.mCursorSelStart + " mCursorSelEnd=" + this.mCursorSelEnd + " mCursorCandStart=" + this.mCursorCandStart + " mCursorCandEnd=" + this.mCursorCandEnd);
        StringBuilder sb4 = new StringBuilder();
        sb4.append("  mNextUserActionNotificationSequenceNumber=");
        sb4.append(this.mNextUserActionNotificationSequenceNumber);
        sb4.append(" mLastSentUserActionNotificationSequenceNumber=");
        sb4.append(this.mLastSentUserActionNotificationSequenceNumber);
        p.println(sb4.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public final class ImeInputEventSender extends InputEventSender {
        public ImeInputEventSender(InputChannel inputChannel, Looper looper) {
            super(inputChannel, looper);
        }

        @Override // android.view.InputEventSender
        public synchronized void onInputEventFinished(int seq, boolean handled) {
            InputMethodManager.this.finishedInputEvent(seq, handled, false);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public final class PendingEvent implements Runnable {
        public FinishedInputEventCallback mCallback;
        public InputEvent mEvent;
        public boolean mHandled;
        public Handler mHandler;
        public String mInputMethodId;
        public Object mToken;

        private PendingEvent() {
        }

        public synchronized void recycle() {
            this.mEvent = null;
            this.mToken = null;
            this.mInputMethodId = null;
            this.mCallback = null;
            this.mHandler = null;
            this.mHandled = false;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.mCallback.onFinishedInputEvent(this.mToken, this.mHandled);
            synchronized (InputMethodManager.this.mH) {
                InputMethodManager.this.recyclePendingEventLocked(this);
            }
        }
    }

    private static synchronized String dumpViewInfo(View view) {
        if (view == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(view);
        sb.append(",focus=" + view.hasFocus());
        sb.append(",windowFocus=" + view.hasWindowFocus());
        sb.append(",autofillUiShowing=" + isAutofillUIShowing(view));
        sb.append(",window=" + view.getWindowToken());
        sb.append(",temporaryDetach=" + view.isTemporarilyDetached());
        return sb.toString();
    }
}
