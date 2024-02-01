package android.inputmethodservice;

import android.R;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.ContentObserver;
import android.graphics.Rect;
import android.graphics.Region;
import android.inputmethodservice.AbstractInputMethodService;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings;
import android.provider.SettingsStringUtil;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.util.Log;
import android.util.PrintWriterPrinter;
import android.util.Printer;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CursorAnchorInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputBinding;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputContentInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes.dex */
public class InputMethodService extends AbstractInputMethodService {
    public static final int BACK_DISPOSITION_ADJUST_NOTHING = 3;
    public static final int BACK_DISPOSITION_DEFAULT = 0;
    private static final int BACK_DISPOSITION_MAX = 3;
    private static final int BACK_DISPOSITION_MIN = 0;
    @Deprecated
    public static final int BACK_DISPOSITION_WILL_DISMISS = 2;
    @Deprecated
    public static final int BACK_DISPOSITION_WILL_NOT_DISMISS = 1;
    static final boolean DEBUG;
    public static final int IME_ACTIVE = 1;
    public static final int IME_VISIBLE = 2;
    static final int MOVEMENT_DOWN = -1;
    static final int MOVEMENT_UP = -2;
    static final String TAG = "InputMethodService";
    int mBackDisposition;
    FrameLayout mCandidatesFrame;
    boolean mCandidatesViewStarted;
    int mCandidatesVisibility;
    CompletionInfo[] mCurCompletions;
    ViewGroup mExtractAccessories;
    View mExtractAction;
    public private protected ExtractEditText mExtractEditText;
    FrameLayout mExtractFrame;
    public private protected View mExtractView;
    boolean mExtractViewHidden;
    ExtractedText mExtractedText;
    int mExtractedToken;
    boolean mFullscreenApplied;
    ViewGroup mFullscreenArea;
    InputMethodManager mImm;
    boolean mInShowWindow;
    LayoutInflater mInflater;
    boolean mInitialized;
    InputBinding mInputBinding;
    InputConnection mInputConnection;
    EditorInfo mInputEditorInfo;
    FrameLayout mInputFrame;
    boolean mInputStarted;
    View mInputView;
    boolean mInputViewStarted;
    boolean mIsFullscreen;
    boolean mIsInputViewShown;
    boolean mLastShowInputRequested;
    public private protected View mRootView;
    public protected SettingsObserver mSettingsObserver;
    boolean mShouldClearInsetOfPreviousIme;
    int mShowInputFlags;
    boolean mShowInputRequested;
    private IBinder mStartInputToken;
    InputConnection mStartedInputConnection;
    int mStatusIcon;
    TypedArray mThemeAttrs;
    IBinder mToken;
    SoftInputWindow mWindow;
    boolean mWindowAdded;
    boolean mWindowCreated;
    boolean mWindowVisible;
    boolean mWindowWasVisible;
    public private protected int mTheme = 0;
    public private protected final Insets mTmpInsets = new Insets();
    final int[] mTmpLocation = new int[2];
    final ViewTreeObserver.OnComputeInternalInsetsListener mInsetsComputer = new ViewTreeObserver.OnComputeInternalInsetsListener() { // from class: android.inputmethodservice.-$$Lambda$InputMethodService$8T9TmAUIN7vW9eU6kTg8309_d4E
        @Override // android.view.ViewTreeObserver.OnComputeInternalInsetsListener
        public final void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
            InputMethodService.lambda$new$0(InputMethodService.this, internalInsetsInfo);
        }
    };
    final View.OnClickListener mActionClickListener = new View.OnClickListener() { // from class: android.inputmethodservice.-$$Lambda$InputMethodService$wp8DeVGx_WDOPw4F6an7QbwVxf0
        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            InputMethodService.lambda$new$1(InputMethodService.this, view);
        }
    };

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface BackDispositionMode {
    }

    /* loaded from: classes.dex */
    public static final class Insets {
        public static final int TOUCHABLE_INSETS_CONTENT = 1;
        public static final int TOUCHABLE_INSETS_FRAME = 0;
        public static final int TOUCHABLE_INSETS_REGION = 3;
        public static final int TOUCHABLE_INSETS_VISIBLE = 2;
        public int contentTopInsets;
        public int touchableInsets;
        public final Region touchableRegion = new Region();
        public int visibleTopInsets;
    }

    static {
        DEBUG = SystemProperties.getInt("persist.debug.input", 0) == 1;
    }

    public static /* synthetic */ void lambda$new$0(InputMethodService inputMethodService, ViewTreeObserver.InternalInsetsInfo info) {
        if (inputMethodService.isExtractViewShown()) {
            View decor = inputMethodService.getWindow().getWindow().getDecorView();
            Rect rect = info.contentInsets;
            Rect rect2 = info.visibleInsets;
            int height = decor.getHeight();
            rect2.top = height;
            rect.top = height;
            info.touchableRegion.setEmpty();
            info.setTouchableInsets(0);
            return;
        }
        inputMethodService.onComputeInsets(inputMethodService.mTmpInsets);
        info.contentInsets.top = inputMethodService.mTmpInsets.contentTopInsets;
        info.visibleInsets.top = inputMethodService.mTmpInsets.visibleTopInsets;
        info.touchableRegion.set(inputMethodService.mTmpInsets.touchableRegion);
        info.setTouchableInsets(inputMethodService.mTmpInsets.touchableInsets);
    }

    public static /* synthetic */ void lambda$new$1(InputMethodService inputMethodService, View v) {
        EditorInfo ei = inputMethodService.getCurrentInputEditorInfo();
        InputConnection ic = inputMethodService.getCurrentInputConnection();
        if (ei != null && ic != null) {
            if (ei.actionId != 0) {
                ic.performEditorAction(ei.actionId);
            } else if ((ei.imeOptions & 255) != 1) {
                ic.performEditorAction(ei.imeOptions & 255);
            }
        }
    }

    /* loaded from: classes.dex */
    public class InputMethodImpl extends AbstractInputMethodService.AbstractInputMethodImpl {
        public InputMethodImpl() {
            super();
        }

        @Override // android.view.inputmethod.InputMethod
        public void attachToken(IBinder token) {
            if (InputMethodService.this.mToken == null) {
                InputMethodService.this.mToken = token;
                InputMethodService.this.mWindow.setToken(token);
            }
        }

        @Override // android.view.inputmethod.InputMethod
        public void bindInput(InputBinding binding) {
            InputMethodService.this.mInputBinding = binding;
            InputMethodService.this.mInputConnection = binding.getConnection();
            if (InputMethodService.DEBUG) {
                Log.v(InputMethodService.TAG, "bindInput(): binding=" + binding + " ic=" + InputMethodService.this.mInputConnection);
            }
            if (InputMethodService.this.mImm != null && InputMethodService.this.mToken != null) {
                InputMethodService.this.mImm.reportFullscreenMode(InputMethodService.this.mToken, InputMethodService.this.mIsFullscreen);
            }
            InputMethodService.this.initialize();
            InputMethodService.this.onBindInput();
        }

        @Override // android.view.inputmethod.InputMethod
        public void unbindInput() {
            if (InputMethodService.DEBUG) {
                Log.v(InputMethodService.TAG, "unbindInput(): binding=" + InputMethodService.this.mInputBinding + " ic=" + InputMethodService.this.mInputConnection);
            }
            InputMethodService.this.onUnbindInput();
            InputMethodService.this.mInputBinding = null;
            InputMethodService.this.mInputConnection = null;
            InputMethodService.this.doHideWindow();
        }

        @Override // android.view.inputmethod.InputMethod
        public void startInput(InputConnection ic, EditorInfo attribute) {
            if (InputMethodService.DEBUG) {
                Log.v(InputMethodService.TAG, "startInput(): editor=" + attribute);
            }
            InputMethodService.this.doStartInput(ic, attribute, false);
        }

        @Override // android.view.inputmethod.InputMethod
        public void restartInput(InputConnection ic, EditorInfo attribute) {
            if (InputMethodService.DEBUG) {
                Log.v(InputMethodService.TAG, "restartInput(): editor=" + attribute);
            }
            InputMethodService.this.doStartInput(ic, attribute, true);
        }

        @Override // android.view.inputmethod.InputMethod
        public synchronized void dispatchStartInputWithToken(InputConnection inputConnection, EditorInfo editorInfo, boolean restarting, IBinder startInputToken) {
            InputMethodService.this.mStartInputToken = startInputToken;
            super.dispatchStartInputWithToken(inputConnection, editorInfo, restarting, startInputToken);
        }

        @Override // android.view.inputmethod.InputMethod
        public void hideSoftInput(int flags, ResultReceiver resultReceiver) {
            if (InputMethodService.DEBUG) {
                Log.v(InputMethodService.TAG, "hideSoftInput()");
            }
            boolean wasVis = InputMethodService.this.isInputViewShown();
            int i = 0;
            InputMethodService.this.mShowInputFlags = 0;
            InputMethodService.this.mShowInputRequested = false;
            InputMethodService.this.doHideWindow();
            InputMethodService.this.clearInsetOfPreviousIme();
            if (resultReceiver != null) {
                if (wasVis != InputMethodService.this.isInputViewShown()) {
                    i = 3;
                } else if (!wasVis) {
                    i = 1;
                }
                resultReceiver.send(i, null);
            }
        }

        @Override // android.view.inputmethod.InputMethod
        public void showSoftInput(int flags, ResultReceiver resultReceiver) {
            if (InputMethodService.DEBUG) {
                Log.v(InputMethodService.TAG, "showSoftInput()");
            }
            boolean wasVis = InputMethodService.this.isInputViewShown();
            int i = 0;
            if (InputMethodService.this.dispatchOnShowInputRequested(flags, false)) {
                try {
                    InputMethodService.this.showWindow(true);
                } catch (WindowManager.BadTokenException e) {
                }
            }
            InputMethodService.this.clearInsetOfPreviousIme();
            InputMethodService.this.mImm.setImeWindowStatus(InputMethodService.this.mToken, InputMethodService.this.mStartInputToken, InputMethodService.mapToImeWindowStatus(InputMethodService.this.isInputViewShown()), InputMethodService.this.mBackDisposition);
            if (resultReceiver != null) {
                if (wasVis != InputMethodService.this.isInputViewShown()) {
                    i = 2;
                } else if (!wasVis) {
                    i = 1;
                }
                resultReceiver.send(i, null);
            }
        }

        @Override // android.view.inputmethod.InputMethod
        public void changeInputMethodSubtype(InputMethodSubtype subtype) {
            InputMethodService.this.onCurrentInputMethodSubtypeChanged(subtype);
        }
    }

    /* loaded from: classes.dex */
    public class InputMethodSessionImpl extends AbstractInputMethodService.AbstractInputMethodSessionImpl {
        public InputMethodSessionImpl() {
            super();
        }

        @Override // android.view.inputmethod.InputMethodSession
        public void finishInput() {
            if (!isEnabled()) {
                return;
            }
            if (InputMethodService.DEBUG) {
                Log.v(InputMethodService.TAG, "finishInput() in " + this);
            }
            InputMethodService.this.doFinishInput();
        }

        @Override // android.view.inputmethod.InputMethodSession
        public void displayCompletions(CompletionInfo[] completions) {
            if (!isEnabled()) {
                return;
            }
            InputMethodService.this.mCurCompletions = completions;
            InputMethodService.this.onDisplayCompletions(completions);
        }

        @Override // android.view.inputmethod.InputMethodSession
        public void updateExtractedText(int token, ExtractedText text) {
            if (!isEnabled()) {
                return;
            }
            InputMethodService.this.onUpdateExtractedText(token, text);
        }

        @Override // android.view.inputmethod.InputMethodSession
        public void updateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd, int candidatesStart, int candidatesEnd) {
            if (!isEnabled()) {
                return;
            }
            InputMethodService.this.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd);
        }

        @Override // android.view.inputmethod.InputMethodSession
        public void viewClicked(boolean focusChanged) {
            if (!isEnabled()) {
                return;
            }
            InputMethodService.this.onViewClicked(focusChanged);
        }

        @Override // android.view.inputmethod.InputMethodSession
        public void updateCursor(Rect newCursor) {
            if (!isEnabled()) {
                return;
            }
            InputMethodService.this.onUpdateCursor(newCursor);
        }

        @Override // android.view.inputmethod.InputMethodSession
        public void appPrivateCommand(String action, Bundle data) {
            if (!isEnabled()) {
                return;
            }
            InputMethodService.this.onAppPrivateCommand(action, data);
        }

        @Override // android.view.inputmethod.InputMethodSession
        public void toggleSoftInput(int showFlags, int hideFlags) {
            InputMethodService.this.onToggleSoftInput(showFlags, hideFlags);
        }

        @Override // android.view.inputmethod.InputMethodSession
        public void updateCursorAnchorInfo(CursorAnchorInfo info) {
            if (!isEnabled()) {
                return;
            }
            InputMethodService.this.onUpdateCursorAnchorInfo(info);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class SettingsObserver extends ContentObserver {
        private final InputMethodService mService;
        private int mShowImeWithHardKeyboard;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes.dex */
        private @interface ShowImeWithHardKeyboardType {
            public static final int FALSE = 1;
            public static final int TRUE = 2;
            public static final int UNKNOWN = 0;
        }

        private synchronized SettingsObserver(InputMethodService service) {
            super(new Handler(service.getMainLooper()));
            this.mShowImeWithHardKeyboard = 0;
            this.mService = service;
        }

        public static synchronized SettingsObserver createAndRegister(InputMethodService service) {
            SettingsObserver observer = new SettingsObserver(service);
            service.getContentResolver().registerContentObserver(Settings.Secure.getUriFor(Settings.Secure.SHOW_IME_WITH_HARD_KEYBOARD), false, observer);
            return observer;
        }

        synchronized void unregister() {
            this.mService.getContentResolver().unregisterContentObserver(this);
        }

        /* JADX INFO: Access modifiers changed from: public */
        public boolean shouldShowImeWithHardKeyboard() {
            if (this.mShowImeWithHardKeyboard == 0) {
                this.mShowImeWithHardKeyboard = Settings.Secure.getInt(this.mService.getContentResolver(), Settings.Secure.SHOW_IME_WITH_HARD_KEYBOARD, 0) != 0 ? 2 : 1;
            }
            switch (this.mShowImeWithHardKeyboard) {
                case 1:
                    return false;
                case 2:
                    return true;
                default:
                    Log.e(InputMethodService.TAG, "Unexpected mShowImeWithHardKeyboard=" + this.mShowImeWithHardKeyboard);
                    return false;
            }
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, Uri uri) {
            Uri showImeWithHardKeyboardUri = Settings.Secure.getUriFor(Settings.Secure.SHOW_IME_WITH_HARD_KEYBOARD);
            if (showImeWithHardKeyboardUri.equals(uri)) {
                this.mShowImeWithHardKeyboard = Settings.Secure.getInt(this.mService.getContentResolver(), Settings.Secure.SHOW_IME_WITH_HARD_KEYBOARD, 0) != 0 ? 2 : 1;
                this.mService.resetStateForNewConfiguration();
            }
        }

        public String toString() {
            return "SettingsObserver{mShowImeWithHardKeyboard=" + this.mShowImeWithHardKeyboard + "}";
        }
    }

    @Override // android.content.ContextWrapper, android.content.Context
    public void setTheme(int theme) {
        if (this.mWindow != null) {
            throw new IllegalStateException("Must be called before onCreate()");
        }
        this.mTheme = theme;
    }

    @Deprecated
    public boolean enableHardwareAcceleration() {
        if (this.mWindow != null) {
            throw new IllegalStateException("Must be called before onCreate()");
        }
        return ActivityManager.isHighEndGfx();
    }

    @Override // android.app.Service
    public void onCreate() {
        this.mTheme = Resources.selectSystemTheme(this.mTheme, getApplicationInfo().targetSdkVersion, 16973908, 16973951, 16974142, 16974142);
        super.setTheme(this.mTheme);
        super.onCreate();
        this.mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        this.mSettingsObserver = SettingsObserver.createAndRegister(this);
        this.mShouldClearInsetOfPreviousIme = this.mImm.getInputMethodWindowVisibleHeight() > 0;
        this.mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mWindow = new SoftInputWindow(this, "InputMethod", this.mTheme, null, null, this.mDispatcherState, WindowManager.LayoutParams.TYPE_INPUT_METHOD, 80, false);
        this.mWindow.getWindow().setFlags(Integer.MIN_VALUE, Integer.MIN_VALUE);
        initViews();
        this.mWindow.getWindow().setLayout(-1, -2);
    }

    public void onInitializeInterface() {
    }

    synchronized void initialize() {
        if (!this.mInitialized) {
            this.mInitialized = true;
            onInitializeInterface();
        }
    }

    synchronized void initViews() {
        this.mInitialized = false;
        this.mWindowCreated = false;
        this.mShowInputRequested = false;
        this.mShowInputFlags = 0;
        this.mThemeAttrs = obtainStyledAttributes(R.styleable.InputMethodService);
        this.mRootView = this.mInflater.inflate(com.android.internal.R.layout.input_method, (ViewGroup) null);
        this.mWindow.setContentView(this.mRootView);
        this.mRootView.getViewTreeObserver().removeOnComputeInternalInsetsListener(this.mInsetsComputer);
        this.mRootView.getViewTreeObserver().addOnComputeInternalInsetsListener(this.mInsetsComputer);
        if (Settings.Global.getInt(getContentResolver(), Settings.Global.FANCY_IME_ANIMATIONS, 0) != 0) {
            this.mWindow.getWindow().setWindowAnimations(com.android.internal.R.style.Animation_InputMethodFancy);
        }
        this.mFullscreenArea = (ViewGroup) this.mRootView.findViewById(com.android.internal.R.id.fullscreenArea);
        this.mExtractViewHidden = false;
        this.mExtractFrame = (FrameLayout) this.mRootView.findViewById(16908316);
        this.mExtractView = null;
        this.mExtractEditText = null;
        this.mExtractAccessories = null;
        this.mExtractAction = null;
        this.mFullscreenApplied = false;
        this.mCandidatesFrame = (FrameLayout) this.mRootView.findViewById(16908317);
        this.mInputFrame = (FrameLayout) this.mRootView.findViewById(16908318);
        this.mInputView = null;
        this.mIsInputViewShown = false;
        this.mExtractFrame.setVisibility(8);
        this.mCandidatesVisibility = getCandidatesHiddenVisibility();
        this.mCandidatesFrame.setVisibility(this.mCandidatesVisibility);
        this.mInputFrame.setVisibility(8);
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        this.mRootView.getViewTreeObserver().removeOnComputeInternalInsetsListener(this.mInsetsComputer);
        doFinishInput();
        if (this.mWindowAdded) {
            this.mWindow.getWindow().setWindowAnimations(0);
            this.mWindow.dismiss();
        }
        if (this.mSettingsObserver != null) {
            this.mSettingsObserver.unregister();
            this.mSettingsObserver = null;
        }
    }

    @Override // android.app.Service, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        resetStateForNewConfiguration();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void resetStateForNewConfiguration() {
        boolean visible = this.mWindowVisible;
        int showFlags = this.mShowInputFlags;
        boolean showingInput = this.mShowInputRequested;
        CompletionInfo[] completions = this.mCurCompletions;
        initViews();
        this.mInputViewStarted = false;
        this.mCandidatesViewStarted = false;
        if (this.mInputStarted) {
            doStartInput(getCurrentInputConnection(), getCurrentInputEditorInfo(), true);
        }
        if (visible) {
            if (showingInput) {
                if (dispatchOnShowInputRequested(showFlags, true)) {
                    showWindow(true);
                    if (completions != null) {
                        this.mCurCompletions = completions;
                        onDisplayCompletions(completions);
                    }
                } else {
                    doHideWindow();
                }
            } else if (this.mCandidatesVisibility == 0) {
                showWindow(false);
            } else {
                doHideWindow();
            }
            boolean showing = onEvaluateInputViewShown();
            this.mImm.setImeWindowStatus(this.mToken, this.mStartInputToken, (showing ? 2 : 0) | 1, this.mBackDisposition);
        }
    }

    @Override // android.inputmethodservice.AbstractInputMethodService
    public AbstractInputMethodService.AbstractInputMethodImpl onCreateInputMethodInterface() {
        return new InputMethodImpl();
    }

    @Override // android.inputmethodservice.AbstractInputMethodService
    public AbstractInputMethodService.AbstractInputMethodSessionImpl onCreateInputMethodSessionInterface() {
        return new InputMethodSessionImpl();
    }

    public LayoutInflater getLayoutInflater() {
        return this.mInflater;
    }

    public Dialog getWindow() {
        return this.mWindow;
    }

    public void setBackDisposition(int disposition) {
        if (disposition == this.mBackDisposition) {
            return;
        }
        if (disposition > 3 || disposition < 0) {
            Log.e(TAG, "Invalid back disposition value (" + disposition + ") specified.");
            return;
        }
        this.mBackDisposition = disposition;
        this.mImm.setImeWindowStatus(this.mToken, this.mStartInputToken, mapToImeWindowStatus(isInputViewShown()), this.mBackDisposition);
    }

    public int getBackDisposition() {
        return this.mBackDisposition;
    }

    public int getMaxWidth() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public InputBinding getCurrentInputBinding() {
        return this.mInputBinding;
    }

    public InputConnection getCurrentInputConnection() {
        InputConnection ic = this.mStartedInputConnection;
        if (ic != null) {
            return ic;
        }
        return this.mInputConnection;
    }

    public final boolean switchToPreviousInputMethod() {
        return this.mImm.switchToPreviousInputMethodInternal(this.mToken);
    }

    public final boolean switchToNextInputMethod(boolean onlyCurrentIme) {
        return this.mImm.switchToNextInputMethodInternal(this.mToken, onlyCurrentIme);
    }

    public final boolean shouldOfferSwitchingToNextInputMethod() {
        return this.mImm.shouldOfferSwitchingToNextInputMethodInternal(this.mToken);
    }

    public boolean getCurrentInputStarted() {
        return this.mInputStarted;
    }

    public EditorInfo getCurrentInputEditorInfo() {
        return this.mInputEditorInfo;
    }

    public void updateFullscreenMode() {
        View v;
        boolean isFullscreen = this.mShowInputRequested && onEvaluateFullscreenMode();
        boolean changed = this.mLastShowInputRequested != this.mShowInputRequested;
        if (this.mIsFullscreen != isFullscreen || !this.mFullscreenApplied) {
            changed = true;
            this.mIsFullscreen = isFullscreen;
            if (this.mImm != null && this.mToken != null) {
                this.mImm.reportFullscreenMode(this.mToken, this.mIsFullscreen);
            }
            this.mFullscreenApplied = true;
            initialize();
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.mFullscreenArea.getLayoutParams();
            if (isFullscreen) {
                this.mFullscreenArea.setBackgroundDrawable(this.mThemeAttrs.getDrawable(0));
                lp.height = 0;
                lp.weight = 1.0f;
            } else {
                this.mFullscreenArea.setBackgroundDrawable(null);
                lp.height = -2;
                lp.weight = 0.0f;
            }
            ((ViewGroup) this.mFullscreenArea.getParent()).updateViewLayout(this.mFullscreenArea, lp);
            if (isFullscreen) {
                if (this.mExtractView == null && (v = onCreateExtractTextView()) != null) {
                    setExtractView(v);
                }
                startExtractingText(false);
            }
            updateExtractFrameVisibility();
        }
        if (changed) {
            onConfigureWindow(this.mWindow.getWindow(), isFullscreen, true ^ this.mShowInputRequested);
            this.mLastShowInputRequested = this.mShowInputRequested;
        }
    }

    public void onConfigureWindow(Window win, boolean isFullscreen, boolean isCandidatesOnly) {
        int currentHeight = this.mWindow.getWindow().getAttributes().height;
        int newHeight = isFullscreen ? -1 : -2;
        if (this.mIsInputViewShown && currentHeight != newHeight && DEBUG) {
            Log.w(TAG, "Window size has been changed. This may cause jankiness of resizing window: " + currentHeight + " -> " + newHeight);
        }
        this.mWindow.getWindow().setLayout(-1, newHeight);
    }

    public boolean isFullscreenMode() {
        return this.mIsFullscreen;
    }

    public boolean onEvaluateFullscreenMode() {
        Configuration config = getResources().getConfiguration();
        if (config.orientation != 2) {
            return false;
        }
        return this.mInputEditorInfo == null || (this.mInputEditorInfo.imeOptions & 33554432) == 0;
    }

    public void setExtractViewShown(boolean shown) {
        if (this.mExtractViewHidden == shown) {
            this.mExtractViewHidden = !shown;
            updateExtractFrameVisibility();
        }
    }

    public boolean isExtractViewShown() {
        return this.mIsFullscreen && !this.mExtractViewHidden;
    }

    synchronized void updateExtractFrameVisibility() {
        int vis;
        if (isFullscreenMode()) {
            vis = this.mExtractViewHidden ? 4 : 0;
            this.mExtractFrame.setVisibility(vis);
        } else {
            vis = 0;
            this.mExtractFrame.setVisibility(8);
        }
        updateCandidatesVisibility(this.mCandidatesVisibility == 0);
        if (this.mWindowWasVisible && this.mFullscreenArea.getVisibility() != vis) {
            int animRes = this.mThemeAttrs.getResourceId(vis != 0 ? 2 : 1, 0);
            if (animRes != 0) {
                this.mFullscreenArea.startAnimation(AnimationUtils.loadAnimation(this, animRes));
            }
        }
        this.mFullscreenArea.setVisibility(vis);
    }

    public void onComputeInsets(Insets outInsets) {
        int[] loc = this.mTmpLocation;
        if (this.mInputFrame.getVisibility() == 0) {
            this.mInputFrame.getLocationInWindow(loc);
        } else {
            View decor = getWindow().getWindow().getDecorView();
            loc[1] = decor.getHeight();
        }
        if (isFullscreenMode()) {
            View decor2 = getWindow().getWindow().getDecorView();
            outInsets.contentTopInsets = decor2.getHeight();
        } else {
            outInsets.contentTopInsets = loc[1];
        }
        if (this.mCandidatesFrame.getVisibility() == 0) {
            this.mCandidatesFrame.getLocationInWindow(loc);
        }
        outInsets.visibleTopInsets = loc[1];
        outInsets.touchableInsets = 2;
        outInsets.touchableRegion.setEmpty();
    }

    public void updateInputViewShown() {
        boolean isShown = this.mShowInputRequested && onEvaluateInputViewShown();
        if (this.mIsInputViewShown != isShown && this.mWindowVisible) {
            this.mIsInputViewShown = isShown;
            this.mInputFrame.setVisibility(isShown ? 0 : 8);
            if (this.mInputView == null) {
                initialize();
                View v = onCreateInputView();
                if (v != null) {
                    setInputView(v);
                }
            }
        }
    }

    public boolean isShowInputRequested() {
        return this.mShowInputRequested;
    }

    public boolean isInputViewShown() {
        return this.mIsInputViewShown && this.mWindowVisible;
    }

    public boolean onEvaluateInputViewShown() {
        if (this.mSettingsObserver != null) {
            if (this.mSettingsObserver.shouldShowImeWithHardKeyboard()) {
                return true;
            }
            Configuration config = getResources().getConfiguration();
            return config.keyboard == 1 || config.hardKeyboardHidden == 2;
        }
        Log.w(TAG, "onEvaluateInputViewShown: mSettingsObserver must not be null here.");
        return false;
    }

    public void setCandidatesViewShown(boolean shown) {
        updateCandidatesVisibility(shown);
        if (!this.mShowInputRequested && this.mWindowVisible != shown) {
            if (shown) {
                showWindow(false);
            } else {
                doHideWindow();
            }
        }
    }

    synchronized void updateCandidatesVisibility(boolean shown) {
        int vis = shown ? 0 : getCandidatesHiddenVisibility();
        if (this.mCandidatesVisibility != vis) {
            this.mCandidatesFrame.setVisibility(vis);
            this.mCandidatesVisibility = vis;
        }
    }

    public int getCandidatesHiddenVisibility() {
        return isExtractViewShown() ? 8 : 4;
    }

    public void showStatusIcon(int iconResId) {
        this.mStatusIcon = iconResId;
        this.mImm.showStatusIconInternal(this.mToken, getPackageName(), iconResId);
    }

    public void hideStatusIcon() {
        this.mStatusIcon = 0;
        this.mImm.hideStatusIconInternal(this.mToken);
    }

    public void switchInputMethod(String id) {
        this.mImm.setInputMethodInternal(this.mToken, id);
    }

    public final void switchInputMethod(String id, InputMethodSubtype subtype) {
        this.mImm.setInputMethodAndSubtypeInternal(this.mToken, id, subtype);
    }

    public void setExtractView(View view) {
        this.mExtractFrame.removeAllViews();
        this.mExtractFrame.addView(view, new FrameLayout.LayoutParams(-1, -1));
        this.mExtractView = view;
        if (view != null) {
            this.mExtractEditText = (ExtractEditText) view.findViewById(16908325);
            this.mExtractEditText.setIME(this);
            this.mExtractAction = view.findViewById(com.android.internal.R.id.inputExtractAction);
            if (this.mExtractAction != null) {
                this.mExtractAccessories = (ViewGroup) view.findViewById(com.android.internal.R.id.inputExtractAccessories);
            }
            startExtractingText(false);
            return;
        }
        this.mExtractEditText = null;
        this.mExtractAccessories = null;
        this.mExtractAction = null;
    }

    public void setCandidatesView(View view) {
        this.mCandidatesFrame.removeAllViews();
        this.mCandidatesFrame.addView(view, new FrameLayout.LayoutParams(-1, -2));
    }

    public void setInputView(View view) {
        this.mInputFrame.removeAllViews();
        this.mInputFrame.addView(view, new FrameLayout.LayoutParams(-1, -2));
        this.mInputView = view;
    }

    public View onCreateExtractTextView() {
        return this.mInflater.inflate(com.android.internal.R.layout.input_method_extract_view, (ViewGroup) null);
    }

    public View onCreateCandidatesView() {
        return null;
    }

    public View onCreateInputView() {
        return null;
    }

    public void onStartInputView(EditorInfo info, boolean restarting) {
    }

    public void onFinishInputView(boolean finishingInput) {
        InputConnection ic;
        if (!finishingInput && (ic = getCurrentInputConnection()) != null) {
            ic.finishComposingText();
        }
    }

    public void onStartCandidatesView(EditorInfo info, boolean restarting) {
    }

    public void onFinishCandidatesView(boolean finishingInput) {
        InputConnection ic;
        if (!finishingInput && (ic = getCurrentInputConnection()) != null) {
            ic.finishComposingText();
        }
    }

    public boolean onShowInputRequested(int flags, boolean configChange) {
        if (onEvaluateInputViewShown()) {
            if ((flags & 1) == 0) {
                if (!configChange && onEvaluateFullscreenMode()) {
                    return false;
                }
                if (!this.mSettingsObserver.shouldShowImeWithHardKeyboard() && getResources().getConfiguration().keyboard != 1) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean dispatchOnShowInputRequested(int flags, boolean configChange) {
        boolean result = onShowInputRequested(flags, configChange);
        if (result) {
            this.mShowInputFlags = flags;
        } else {
            this.mShowInputFlags = 0;
        }
        return result;
    }

    public void showWindow(boolean showInput) {
        if (DEBUG) {
            Log.v(TAG, "Showing window: showInput=" + showInput + " mShowInputRequested=" + this.mShowInputRequested + " mWindowAdded=" + this.mWindowAdded + " mWindowCreated=" + this.mWindowCreated + " mWindowVisible=" + this.mWindowVisible + " mInputStarted=" + this.mInputStarted + " mShowInputFlags=" + this.mShowInputFlags);
        }
        if (this.mInShowWindow) {
            Log.w(TAG, "Re-entrance in to showWindow");
            return;
        }
        try {
            try {
                this.mWindowWasVisible = this.mWindowVisible;
                this.mInShowWindow = true;
                showWindowInner(showInput);
            } catch (WindowManager.BadTokenException e) {
                if (DEBUG) {
                    Log.v(TAG, "BadTokenException: IME is done.");
                }
                this.mWindowVisible = false;
                this.mWindowAdded = false;
                throw e;
            }
        } finally {
            this.mWindowWasVisible = true;
            this.mInShowWindow = false;
        }
    }

    synchronized void showWindowInner(boolean showInput) {
        boolean doShowInput = false;
        int previousImeWindowStatus = (this.mWindowVisible ? 1 : 0) | (isInputViewShown() ? 2 : 0);
        this.mWindowVisible = true;
        if (!this.mShowInputRequested && this.mInputStarted && showInput) {
            doShowInput = true;
            this.mShowInputRequested = true;
        }
        if (DEBUG) {
            Log.v(TAG, "showWindow: updating UI");
        }
        initialize();
        updateFullscreenMode();
        updateInputViewShown();
        if (!this.mWindowAdded || !this.mWindowCreated) {
            this.mWindowAdded = true;
            this.mWindowCreated = true;
            initialize();
            if (DEBUG) {
                Log.v(TAG, "CALL: onCreateCandidatesView");
            }
            View v = onCreateCandidatesView();
            if (DEBUG) {
                Log.v(TAG, "showWindow: candidates=" + v);
            }
            if (v != null) {
                setCandidatesView(v);
            }
        }
        if (this.mShowInputRequested) {
            if (!this.mInputViewStarted) {
                if (DEBUG) {
                    Log.v(TAG, "CALL: onStartInputView");
                }
                this.mInputViewStarted = true;
                onStartInputView(this.mInputEditorInfo, false);
            }
        } else if (!this.mCandidatesViewStarted) {
            if (DEBUG) {
                Log.v(TAG, "CALL: onStartCandidatesView");
            }
            this.mCandidatesViewStarted = true;
            onStartCandidatesView(this.mInputEditorInfo, false);
        }
        if (doShowInput) {
            startExtractingText(false);
        }
        int nextImeWindowStatus = mapToImeWindowStatus(isInputViewShown());
        if (previousImeWindowStatus != nextImeWindowStatus) {
            this.mImm.setImeWindowStatus(this.mToken, this.mStartInputToken, nextImeWindowStatus, this.mBackDisposition);
        }
        if ((previousImeWindowStatus & 1) == 0) {
            if (DEBUG) {
                Log.v(TAG, "showWindow: showing!");
            }
            onWindowShown();
            this.mWindow.show();
            this.mShouldClearInsetOfPreviousIme = false;
        }
    }

    private synchronized void finishViews() {
        if (this.mInputViewStarted) {
            if (DEBUG) {
                Log.v(TAG, "CALL: onFinishInputView");
            }
            onFinishInputView(false);
        } else if (this.mCandidatesViewStarted) {
            if (DEBUG) {
                Log.v(TAG, "CALL: onFinishCandidatesView");
            }
            onFinishCandidatesView(false);
        }
        this.mInputViewStarted = false;
        this.mCandidatesViewStarted = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void doHideWindow() {
        this.mImm.setImeWindowStatus(this.mToken, this.mStartInputToken, 0, this.mBackDisposition);
        hideWindow();
    }

    public void hideWindow() {
        finishViews();
        if (this.mWindowVisible) {
            this.mWindow.hide();
            this.mWindowVisible = false;
            onWindowHidden();
            this.mWindowWasVisible = false;
        }
        updateFullscreenMode();
    }

    public void onWindowShown() {
    }

    public void onWindowHidden() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void clearInsetOfPreviousIme() {
        if (DEBUG) {
            Log.v(TAG, "clearInsetOfPreviousIme()  mShouldClearInsetOfPreviousIme=" + this.mShouldClearInsetOfPreviousIme);
        }
        if (this.mShouldClearInsetOfPreviousIme) {
            this.mImm.clearLastInputMethodWindowForTransition(this.mToken);
            this.mShouldClearInsetOfPreviousIme = false;
        }
    }

    public void onBindInput() {
    }

    public void onUnbindInput() {
    }

    public void onStartInput(EditorInfo attribute, boolean restarting) {
    }

    synchronized void doFinishInput() {
        if (this.mInputViewStarted) {
            if (DEBUG) {
                Log.v(TAG, "CALL: onFinishInputView");
            }
            onFinishInputView(true);
        } else if (this.mCandidatesViewStarted) {
            if (DEBUG) {
                Log.v(TAG, "CALL: onFinishCandidatesView");
            }
            onFinishCandidatesView(true);
        }
        this.mInputViewStarted = false;
        this.mCandidatesViewStarted = false;
        if (this.mInputStarted) {
            if (DEBUG) {
                Log.v(TAG, "CALL: onFinishInput");
            }
            onFinishInput();
        }
        this.mInputStarted = false;
        this.mStartedInputConnection = null;
        this.mCurCompletions = null;
    }

    synchronized void doStartInput(InputConnection ic, EditorInfo attribute, boolean restarting) {
        if (!restarting) {
            doFinishInput();
        }
        this.mInputStarted = true;
        this.mStartedInputConnection = ic;
        this.mInputEditorInfo = attribute;
        initialize();
        if (DEBUG) {
            Log.v(TAG, "CALL: onStartInput");
        }
        onStartInput(attribute, restarting);
        if (this.mWindowVisible) {
            if (this.mShowInputRequested) {
                if (DEBUG) {
                    Log.v(TAG, "CALL: onStartInputView");
                }
                this.mInputViewStarted = true;
                onStartInputView(this.mInputEditorInfo, restarting);
                startExtractingText(true);
            } else if (this.mCandidatesVisibility == 0) {
                if (DEBUG) {
                    Log.v(TAG, "CALL: onStartCandidatesView");
                }
                this.mCandidatesViewStarted = true;
                onStartCandidatesView(this.mInputEditorInfo, restarting);
            }
        }
    }

    public void onFinishInput() {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) {
            ic.finishComposingText();
        }
    }

    public void onDisplayCompletions(CompletionInfo[] completions) {
    }

    public void onUpdateExtractedText(int token, ExtractedText text) {
        if (this.mExtractedToken == token && text != null && this.mExtractEditText != null) {
            this.mExtractedText = text;
            this.mExtractEditText.setExtractedText(text);
        }
    }

    public void onUpdateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd, int candidatesStart, int candidatesEnd) {
        ExtractEditText eet = this.mExtractEditText;
        if (eet != null && isFullscreenMode() && this.mExtractedText != null) {
            int off = this.mExtractedText.startOffset;
            eet.startInternalChanges();
            int newSelStart2 = newSelStart - off;
            int newSelEnd2 = newSelEnd - off;
            int len = eet.getText().length();
            if (newSelStart2 < 0) {
                newSelStart2 = 0;
            } else if (newSelStart2 > len) {
                newSelStart2 = len;
            }
            if (newSelEnd2 < 0) {
                newSelEnd2 = 0;
            } else if (newSelEnd2 > len) {
                newSelEnd2 = len;
            }
            eet.setSelection(newSelStart2, newSelEnd2);
            eet.finishInternalChanges();
        }
    }

    public void onViewClicked(boolean focusChanged) {
    }

    @Deprecated
    public void onUpdateCursor(Rect newCursor) {
    }

    public void onUpdateCursorAnchorInfo(CursorAnchorInfo cursorAnchorInfo) {
    }

    public void requestHideSelf(int flags) {
        this.mImm.hideSoftInputFromInputMethodInternal(this.mToken, flags);
    }

    public final void requestShowSelf(int flags) {
        this.mImm.showSoftInputFromInputMethodInternal(this.mToken, flags);
    }

    private synchronized boolean handleBack(boolean doIt) {
        if (this.mShowInputRequested) {
            if (doIt) {
                requestHideSelf(0);
            }
            return true;
        } else if (this.mWindowVisible) {
            if (this.mCandidatesVisibility == 0) {
                if (doIt) {
                    setCandidatesViewShown(false);
                }
            } else if (doIt) {
                doHideWindow();
            }
            return true;
        } else {
            return false;
        }
    }

    private synchronized ExtractEditText getExtractEditTextIfVisible() {
        if (!isExtractViewShown() || !isInputViewShown()) {
            return null;
        }
        return this.mExtractEditText;
    }

    @Override // android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == 4) {
            ExtractEditText eet = getExtractEditTextIfVisible();
            if (eet != null && eet.handleBackInTextActionModeIfNeeded(event)) {
                return true;
            }
            if (!handleBack(false)) {
                return false;
            }
            event.startTracking();
            return true;
        }
        return doMovementKey(keyCode, event, -1);
    }

    @Override // android.view.KeyEvent.Callback
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }

    @Override // android.view.KeyEvent.Callback
    public boolean onKeyMultiple(int keyCode, int count, KeyEvent event) {
        return doMovementKey(keyCode, event, count);
    }

    @Override // android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == 4) {
            ExtractEditText eet = getExtractEditTextIfVisible();
            if (eet != null && eet.handleBackInTextActionModeIfNeeded(event)) {
                return true;
            }
            if (event.isTracking() && !event.isCanceled()) {
                return handleBack(true);
            }
        }
        return doMovementKey(keyCode, event, -2);
    }

    @Override // android.inputmethodservice.AbstractInputMethodService
    public boolean onTrackballEvent(MotionEvent event) {
        if (DEBUG) {
            Log.v(TAG, "onTrackballEvent: " + event);
            return false;
        }
        return false;
    }

    @Override // android.inputmethodservice.AbstractInputMethodService
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (DEBUG) {
            Log.v(TAG, "onGenericMotionEvent(): event " + event);
            return false;
        }
        return false;
    }

    public void onAppPrivateCommand(String action, Bundle data) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void onToggleSoftInput(int showFlags, int hideFlags) {
        if (DEBUG) {
            Log.v(TAG, "toggleSoftInput()");
        }
        if (isInputViewShown()) {
            requestHideSelf(hideFlags);
        } else {
            requestShowSelf(showFlags);
        }
    }

    synchronized void reportExtractedMovement(int keyCode, int count) {
        int dx = 0;
        int dy = 0;
        switch (keyCode) {
            case 19:
                dy = -count;
                break;
            case 20:
                dy = count;
                break;
            case 21:
                dx = -count;
                break;
            case 22:
                dx = count;
                break;
        }
        onExtractedCursorMovement(dx, dy);
    }

    synchronized boolean doMovementKey(int keyCode, KeyEvent event, int count) {
        ExtractEditText eet = getExtractEditTextIfVisible();
        if (eet != null) {
            MovementMethod movement = eet.getMovementMethod();
            Layout layout = eet.getLayout();
            if (movement != null && layout != null) {
                if (count == -1) {
                    if (movement.onKeyDown(eet, eet.getText(), keyCode, event)) {
                        reportExtractedMovement(keyCode, 1);
                        return true;
                    }
                } else if (count == -2) {
                    if (movement.onKeyUp(eet, eet.getText(), keyCode, event)) {
                        return true;
                    }
                } else if (movement.onKeyOther(eet, eet.getText(), event)) {
                    reportExtractedMovement(keyCode, count);
                } else {
                    KeyEvent down = KeyEvent.changeAction(event, 0);
                    if (movement.onKeyDown(eet, eet.getText(), keyCode, down)) {
                        KeyEvent up = KeyEvent.changeAction(event, 1);
                        movement.onKeyUp(eet, eet.getText(), keyCode, up);
                        while (true) {
                            count--;
                            if (count <= 0) {
                                break;
                            }
                            movement.onKeyDown(eet, eet.getText(), keyCode, down);
                            movement.onKeyUp(eet, eet.getText(), keyCode, up);
                        }
                        reportExtractedMovement(keyCode, count);
                    }
                }
            }
            switch (keyCode) {
                case 19:
                case 20:
                case 21:
                case 22:
                    return true;
            }
        }
        return false;
    }

    public void sendDownUpKeyEvents(int keyEventCode) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) {
            return;
        }
        long eventTime = SystemClock.uptimeMillis();
        ic.sendKeyEvent(new KeyEvent(eventTime, eventTime, 0, keyEventCode, 0, 0, -1, 0, 6));
        ic.sendKeyEvent(new KeyEvent(eventTime, SystemClock.uptimeMillis(), 1, keyEventCode, 0, 0, -1, 0, 6));
    }

    public boolean sendDefaultEditorAction(boolean fromEnterKey) {
        EditorInfo ei = getCurrentInputEditorInfo();
        if (ei != null) {
            if ((!fromEnterKey || (ei.imeOptions & 1073741824) == 0) && (ei.imeOptions & 255) != 1) {
                InputConnection ic = getCurrentInputConnection();
                if (ic != null) {
                    ic.performEditorAction(ei.imeOptions & 255);
                }
                return true;
            }
            return false;
        }
        return false;
    }

    public void sendKeyChar(char charCode) {
        if (charCode == '\n') {
            if (!sendDefaultEditorAction(true)) {
                sendDownUpKeyEvents(66);
            }
        } else if (charCode >= '0' && charCode <= '9') {
            sendDownUpKeyEvents((charCode - '0') + 7);
        } else {
            InputConnection ic = getCurrentInputConnection();
            if (ic != null) {
                ic.commitText(String.valueOf(charCode), 1);
            }
        }
    }

    public void onExtractedSelectionChanged(int start, int end) {
        InputConnection conn = getCurrentInputConnection();
        if (conn != null) {
            conn.setSelection(start, end);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onExtractedDeleteText(int start, int end) {
        InputConnection conn = getCurrentInputConnection();
        if (conn != null) {
            conn.finishComposingText();
            conn.setSelection(start, start);
            conn.deleteSurroundingText(0, end - start);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onExtractedReplaceText(int start, int end, CharSequence text) {
        InputConnection conn = getCurrentInputConnection();
        if (conn != null) {
            conn.setComposingRegion(start, end);
            conn.commitText(text, 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onExtractedSetSpan(Object span, int start, int end, int flags) {
        InputConnection conn = getCurrentInputConnection();
        if (conn == null || !conn.setSelection(start, end)) {
            return;
        }
        CharSequence text = conn.getSelectedText(1);
        if (text instanceof Spannable) {
            ((Spannable) text).setSpan(span, 0, text.length(), flags);
            conn.setComposingRegion(start, end);
            conn.commitText(text, 1);
        }
    }

    public void onExtractedTextClicked() {
        if (this.mExtractEditText != null && this.mExtractEditText.hasVerticalScrollBar()) {
            setCandidatesViewShown(false);
        }
    }

    public void onExtractedCursorMovement(int dx, int dy) {
        if (this.mExtractEditText != null && dy != 0 && this.mExtractEditText.hasVerticalScrollBar()) {
            setCandidatesViewShown(false);
        }
    }

    public boolean onExtractTextContextMenuItem(int id) {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) {
            ic.performContextMenuAction(id);
            return true;
        }
        return true;
    }

    public CharSequence getTextForImeAction(int imeOptions) {
        switch (imeOptions & 255) {
            case 1:
                return null;
            case 2:
                return getText(com.android.internal.R.string.ime_action_go);
            case 3:
                return getText(com.android.internal.R.string.ime_action_search);
            case 4:
                return getText(com.android.internal.R.string.ime_action_send);
            case 5:
                return getText(com.android.internal.R.string.ime_action_next);
            case 6:
                return getText(com.android.internal.R.string.ime_action_done);
            case 7:
                return getText(com.android.internal.R.string.ime_action_previous);
            default:
                return getText(com.android.internal.R.string.ime_action_default);
        }
    }

    private synchronized int getIconForImeAction(int imeOptions) {
        switch (imeOptions & 255) {
            case 2:
                return com.android.internal.R.drawable.ic_input_extract_action_go;
            case 3:
                return com.android.internal.R.drawable.ic_input_extract_action_search;
            case 4:
                return com.android.internal.R.drawable.ic_input_extract_action_send;
            case 5:
                return com.android.internal.R.drawable.ic_input_extract_action_next;
            case 6:
                return com.android.internal.R.drawable.ic_input_extract_action_done;
            case 7:
                return com.android.internal.R.drawable.ic_input_extract_action_previous;
            default:
                return com.android.internal.R.drawable.ic_input_extract_action_return;
        }
    }

    public void onUpdateExtractingVisibility(EditorInfo ei) {
        if (ei.inputType == 0 || (ei.imeOptions & 268435456) != 0) {
            setExtractViewShown(false);
        } else {
            setExtractViewShown(true);
        }
    }

    public void onUpdateExtractingViews(EditorInfo ei) {
        if (!isExtractViewShown() || this.mExtractAccessories == null) {
            return;
        }
        boolean z = true;
        if (ei.actionLabel == null && ((ei.imeOptions & 255) == 1 || (ei.imeOptions & 536870912) != 0 || ei.inputType == 0)) {
            z = false;
        }
        boolean hasAction = z;
        if (hasAction) {
            this.mExtractAccessories.setVisibility(0);
            if (this.mExtractAction != null) {
                if (this.mExtractAction instanceof ImageButton) {
                    ((ImageButton) this.mExtractAction).setImageResource(getIconForImeAction(ei.imeOptions));
                    if (ei.actionLabel != null) {
                        this.mExtractAction.setContentDescription(ei.actionLabel);
                    } else {
                        this.mExtractAction.setContentDescription(getTextForImeAction(ei.imeOptions));
                    }
                } else if (ei.actionLabel != null) {
                    ((TextView) this.mExtractAction).setText(ei.actionLabel);
                } else {
                    ((TextView) this.mExtractAction).setText(getTextForImeAction(ei.imeOptions));
                }
                this.mExtractAction.setOnClickListener(this.mActionClickListener);
                return;
            }
            return;
        }
        this.mExtractAccessories.setVisibility(8);
        if (this.mExtractAction != null) {
            this.mExtractAction.setOnClickListener(null);
        }
    }

    public void onExtractingInputChanged(EditorInfo ei) {
        if (ei.inputType == 0) {
            requestHideSelf(2);
        }
    }

    synchronized void startExtractingText(boolean inputChanged) {
        ExtractEditText eet = this.mExtractEditText;
        if (eet != null && getCurrentInputStarted() && isFullscreenMode()) {
            this.mExtractedToken++;
            ExtractedTextRequest req = new ExtractedTextRequest();
            req.token = this.mExtractedToken;
            req.flags = 1;
            req.hintMaxLines = 10;
            req.hintMaxChars = 10000;
            InputConnection ic = getCurrentInputConnection();
            this.mExtractedText = ic == null ? null : ic.getExtractedText(req, 1);
            if (this.mExtractedText == null || ic == null) {
                Log.e(TAG, "Unexpected null in startExtractingText : mExtractedText = " + this.mExtractedText + ", input connection = " + ic);
            }
            EditorInfo ei = getCurrentInputEditorInfo();
            try {
                eet.startInternalChanges();
                onUpdateExtractingVisibility(ei);
                onUpdateExtractingViews(ei);
                int inputType = ei.inputType;
                if ((inputType & 15) == 1 && (262144 & inputType) != 0) {
                    inputType |= 131072;
                }
                eet.setInputType(inputType);
                eet.setHint(ei.hintText);
                if (this.mExtractedText != null) {
                    eet.setEnabled(true);
                    eet.setExtractedText(this.mExtractedText);
                } else {
                    eet.setEnabled(false);
                    eet.setText("");
                }
                if (inputChanged) {
                    onExtractingInputChanged(ei);
                }
            } finally {
                eet.finishInternalChanges();
            }
        }
    }

    protected void onCurrentInputMethodSubtypeChanged(InputMethodSubtype newSubtype) {
        if (DEBUG) {
            int nameResId = newSubtype.getNameResId();
            String mode = newSubtype.getMode();
            StringBuilder sb = new StringBuilder();
            sb.append("changeInputMethodSubtype:");
            sb.append(nameResId == 0 ? "<none>" : getString(nameResId));
            sb.append(",");
            sb.append(mode);
            sb.append(",");
            sb.append(newSubtype.getLocale());
            sb.append(",");
            sb.append(newSubtype.getExtraValue());
            String output = sb.toString();
            Log.v(TAG, "--- " + output);
        }
    }

    public int getInputMethodWindowRecommendedHeight() {
        return this.mImm.getInputMethodWindowVisibleHeight();
    }

    @Override // android.inputmethodservice.AbstractInputMethodService
    public final synchronized void exposeContent(InputContentInfo inputContentInfo, InputConnection inputConnection) {
        if (inputConnection == null || getCurrentInputConnection() != inputConnection) {
            return;
        }
        this.mImm.exposeContent(this.mToken, inputContentInfo, getCurrentInputEditorInfo());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized int mapToImeWindowStatus(boolean isInputViewShown) {
        return (isInputViewShown ? 2 : 0) | 1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.inputmethodservice.AbstractInputMethodService, android.app.Service
    public void dump(FileDescriptor fd, PrintWriter fout, String[] args) {
        Printer p = new PrintWriterPrinter(fout);
        p.println("Input method service state for " + this + SettingsStringUtil.DELIMITER);
        p.println("  mWindowCreated=" + this.mWindowCreated + " mWindowAdded=" + this.mWindowAdded);
        p.println("  mWindowVisible=" + this.mWindowVisible + " mWindowWasVisible=" + this.mWindowWasVisible + " mInShowWindow=" + this.mInShowWindow);
        StringBuilder sb = new StringBuilder();
        sb.append("  Configuration=");
        sb.append(getResources().getConfiguration());
        p.println(sb.toString());
        p.println("  mToken=" + this.mToken);
        p.println("  mInputBinding=" + this.mInputBinding);
        p.println("  mInputConnection=" + this.mInputConnection);
        p.println("  mStartedInputConnection=" + this.mStartedInputConnection);
        p.println("  mInputStarted=" + this.mInputStarted + " mInputViewStarted=" + this.mInputViewStarted + " mCandidatesViewStarted=" + this.mCandidatesViewStarted);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("  mStartInputToken=");
        sb2.append(this.mStartInputToken);
        p.println(sb2.toString());
        if (this.mInputEditorInfo != null) {
            p.println("  mInputEditorInfo:");
            this.mInputEditorInfo.dump(p, "    ");
        } else {
            p.println("  mInputEditorInfo: null");
        }
        p.println("  mShowInputRequested=" + this.mShowInputRequested + " mLastShowInputRequested=" + this.mLastShowInputRequested + " mShowInputFlags=0x" + Integer.toHexString(this.mShowInputFlags));
        p.println("  mCandidatesVisibility=" + this.mCandidatesVisibility + " mFullscreenApplied=" + this.mFullscreenApplied + " mIsFullscreen=" + this.mIsFullscreen + " mExtractViewHidden=" + this.mExtractViewHidden);
        if (this.mExtractedText != null) {
            p.println("  mExtractedText:");
            p.println("    text=" + this.mExtractedText.text.length() + " chars startOffset=" + this.mExtractedText.startOffset);
            p.println("    selectionStart=" + this.mExtractedText.selectionStart + " selectionEnd=" + this.mExtractedText.selectionEnd + " flags=0x" + Integer.toHexString(this.mExtractedText.flags));
        } else {
            p.println("  mExtractedText: null");
        }
        p.println("  mExtractedToken=" + this.mExtractedToken);
        p.println("  mIsInputViewShown=" + this.mIsInputViewShown + " mStatusIcon=" + this.mStatusIcon);
        p.println("Last computed insets:");
        p.println("  contentTopInsets=" + this.mTmpInsets.contentTopInsets + " visibleTopInsets=" + this.mTmpInsets.visibleTopInsets + " touchableInsets=" + this.mTmpInsets.touchableInsets + " touchableRegion=" + this.mTmpInsets.touchableRegion);
        StringBuilder sb3 = new StringBuilder();
        sb3.append(" mShouldClearInsetOfPreviousIme=");
        sb3.append(this.mShouldClearInsetOfPreviousIme);
        p.println(sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append(" mSettingsObserver=");
        sb4.append(this.mSettingsObserver);
        p.println(sb4.toString());
    }
}
