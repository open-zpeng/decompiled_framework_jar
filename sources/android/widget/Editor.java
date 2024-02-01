package android.widget;

import android.animation.ValueAnimator;
import android.app.PendingIntent;
import android.app.RemoteAction;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.UndoManager;
import android.content.UndoOperation;
import android.content.UndoOwner;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ParcelableParcel;
import android.os.SystemClock;
import android.telecom.Logging.EventManager;
import android.text.DynamicLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Layout;
import android.text.ParcelableSpan;
import android.text.Selection;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.text.method.MetaKeyKeyListener;
import android.text.method.MovementMethod;
import android.text.method.WordIterator;
import android.text.style.EasyEditSpan;
import android.text.style.SuggestionRangeSpan;
import android.text.style.SuggestionSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.URLSpan;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.DisplayListCanvas;
import android.view.DragAndDropPermissions;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.RenderNode;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.CursorAnchorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputMethodManager;
import android.view.textclassifier.TextClassification;
import android.view.textclassifier.TextClassificationManager;
import android.widget.AdapterView;
import android.widget.Editor;
import android.widget.Magnifier;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.android.internal.R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.transition.EpicenterTranslateClipReveal;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import com.android.internal.util.Preconditions;
import com.android.internal.view.FloatingActionMode;
import com.android.internal.widget.EditableInputConnection;
import com.xiaopeng.view.xpWindowManager;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/* loaded from: classes3.dex */
public class Editor {
    static final int BLINK = 500;
    private static final boolean DEBUG_UNDO = false;
    private static final int DRAG_SHADOW_MAX_TEXT_LENGTH = 20;
    static final int EXTRACT_NOTHING = -2;
    static final int EXTRACT_UNKNOWN = -1;
    private static final boolean FLAG_USE_MAGNIFIER = false;
    public static final int HANDLE_TYPE_SELECTION_END = 1;
    public static final int HANDLE_TYPE_SELECTION_START = 0;
    private static final float LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS = 0.5f;
    private static final int MENU_ITEM_ORDER_ASSIST = 0;
    private static final int MENU_ITEM_ORDER_AUTOFILL = 10;
    private static final int MENU_ITEM_ORDER_COPY = 5;
    private static final int MENU_ITEM_ORDER_CUT = 4;
    private static final int MENU_ITEM_ORDER_PASTE = 6;
    private static final int MENU_ITEM_ORDER_PASTE_AS_PLAIN_TEXT = 11;
    private static final int MENU_ITEM_ORDER_PROCESS_TEXT_INTENT_ACTIONS_START = 100;
    private static final int MENU_ITEM_ORDER_REDO = 3;
    private static final int MENU_ITEM_ORDER_REPLACE = 9;
    private static final int MENU_ITEM_ORDER_SECONDARY_ASSIST_ACTIONS_START = 50;
    private static final int MENU_ITEM_ORDER_SELECT_ALL = 8;
    private static final int MENU_ITEM_ORDER_SHARE = 7;
    private static final int MENU_ITEM_ORDER_UNDO = 2;
    private static final String TAG = "Editor";
    private static final int TAP_STATE_DOUBLE_TAP = 2;
    private static final int TAP_STATE_FIRST_TAP = 1;
    private static final int TAP_STATE_INITIAL = 0;
    private static final int TAP_STATE_TRIPLE_CLICK = 3;
    private static final String UNDO_OWNER_TAG = "Editor";
    private static final int UNSET_LINE = -1;
    private static final int UNSET_X_VALUE = -1;
    private Blink mBlink;
    private float mContextMenuAnchorX;
    private float mContextMenuAnchorY;
    private CorrectionHighlighter mCorrectionHighlighter;
    public private protected boolean mCreatedWithASelection;
    ActionMode.Callback mCustomInsertionActionModeCallback;
    ActionMode.Callback mCustomSelectionActionModeCallback;
    boolean mDiscardNextActionUp;
    CharSequence mError;
    private ErrorPopup mErrorPopup;
    boolean mErrorWasChanged;
    boolean mFrozenWithFocus;
    private final boolean mHapticTextHandleEnabled;
    boolean mIgnoreActionUpEvent;
    boolean mInBatchEditControllers;
    InputContentType mInputContentType;
    InputMethodState mInputMethodState;
    private Runnable mInsertionActionModeRunnable;
    public protected boolean mInsertionControllerEnabled;
    private InsertionPointCursorController mInsertionPointCursorController;
    boolean mIsBeingLongClicked;
    KeyListener mKeyListener;
    private int mLastButtonState;
    private float mLastDownPositionX;
    private float mLastDownPositionY;
    private float mLastUpPositionX;
    private float mLastUpPositionY;
    private final MagnifierMotionAnimator mMagnifierAnimator;
    private PositionListener mPositionListener;
    private boolean mPreserveSelection;
    final ProcessTextIntentActionsHandler mProcessTextIntentActionsHandler;
    private boolean mRenderCursorRegardlessTiming;
    private boolean mRequestingLinkActionMode;
    private boolean mRestartActionModeOnNextRefresh;
    boolean mSelectAllOnFocus;
    public protected Drawable mSelectHandleCenter;
    public protected Drawable mSelectHandleLeft;
    public protected Drawable mSelectHandleRight;
    private SelectionActionModeHelper mSelectionActionModeHelper;
    public protected boolean mSelectionControllerEnabled;
    SelectionModifierCursorController mSelectionModifierCursorController;
    boolean mSelectionMoved;
    public protected long mShowCursor;
    private boolean mShowErrorAfterAttach;
    private Runnable mShowSuggestionRunnable;
    private SpanController mSpanController;
    SpellChecker mSpellChecker;
    SuggestionRangeSpan mSuggestionRangeSpan;
    private SuggestionsPopupWindow mSuggestionsPopupWindow;
    private Rect mTempRect;
    private ActionMode mTextActionMode;
    boolean mTextIsSelectable;
    private TextRenderNode[] mTextRenderNodes;
    private final TextView mTextView;
    boolean mTouchFocusSelected;
    private boolean mUpdateWordIteratorText;
    private WordIterator mWordIterator;
    private WordIterator mWordIteratorWithText;
    private final UndoManager mUndoManager = new UndoManager();
    private UndoOwner mUndoOwner = this.mUndoManager.getOwner("Editor", this);
    final UndoInputFilter mUndoInputFilter = new UndoInputFilter(this);
    boolean mAllowUndo = true;
    private final MetricsLogger mMetricsLogger = new MetricsLogger();
    private final Runnable mUpdateMagnifierRunnable = new Runnable() { // from class: android.widget.Editor.1
        @Override // java.lang.Runnable
        public void run() {
            Editor.this.mMagnifierAnimator.update();
        }
    };
    private final ViewTreeObserver.OnDrawListener mMagnifierOnDrawListener = new ViewTreeObserver.OnDrawListener() { // from class: android.widget.Editor.2
        @Override // android.view.ViewTreeObserver.OnDrawListener
        public void onDraw() {
            if (Editor.this.mMagnifierAnimator != null) {
                Editor.this.mTextView.post(Editor.this.mUpdateMagnifierRunnable);
            }
        }
    };
    int mInputType = 0;
    boolean mCursorVisible = true;
    public private protected boolean mShowSoftInputOnFocus = true;
    Drawable mDrawableForCursor = null;
    private int mTapState = 0;
    private long mLastTouchUpTime = 0;
    private final CursorAnchorInfoNotifier mCursorAnchorInfoNotifier = new CursorAnchorInfoNotifier();
    private final Runnable mShowFloatingToolbar = new Runnable() { // from class: android.widget.Editor.3
        @Override // java.lang.Runnable
        public void run() {
            if (Editor.this.mTextActionMode != null) {
                Editor.this.mTextActionMode.hide(0L);
            }
        }
    };
    boolean mIsInsertionActionModeStartPending = false;
    private final SuggestionHelper mSuggestionHelper = new SuggestionHelper();
    private final MenuItem.OnMenuItemClickListener mOnContextMenuItemClickListener = new MenuItem.OnMenuItemClickListener() { // from class: android.widget.Editor.5
        @Override // android.view.MenuItem.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem item) {
            if (!Editor.this.mProcessTextIntentActionsHandler.performMenuItemAction(item)) {
                return Editor.this.mTextView.onTextContextMenuItem(item.getItemId());
            }
            return true;
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public interface CursorController extends ViewTreeObserver.OnTouchModeChangeListener {
        synchronized void hide();

        synchronized boolean isActive();

        synchronized boolean isCursorBeingModified();

        synchronized void onDetached();

        synchronized void show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public interface EasyEditDeleteListener {
        synchronized void onDeleteClick(EasyEditSpan easyEditSpan);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes3.dex */
    public @interface HandleType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes3.dex */
    private @interface MagnifierHandleTrigger {
        public static final int INSERTION = 0;
        public static final int SELECTION_END = 2;
        public static final int SELECTION_START = 1;
    }

    /* loaded from: classes3.dex */
    @interface TextActionMode {
        public static final int INSERTION = 1;
        public static final int SELECTION = 0;
        public static final int TEXT_LINK = 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public interface TextViewPositionListener {
        synchronized void updatePosition(int i, int i2, boolean z, boolean z2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class TextRenderNode {
        boolean isDirty = true;
        boolean needsToBeShifted = true;
        RenderNode renderNode;

        public synchronized TextRenderNode(String name) {
            this.renderNode = RenderNode.create(name, null);
        }

        synchronized boolean needsRecord() {
            return this.isDirty || !this.renderNode.isValid();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Editor(TextView textView) {
        this.mTextView = textView;
        this.mTextView.setFilters(this.mTextView.getFilters());
        this.mProcessTextIntentActionsHandler = new ProcessTextIntentActionsHandler();
        this.mHapticTextHandleEnabled = this.mTextView.getContext().getResources().getBoolean(R.bool.config_enableHapticTextHandle);
        this.mMagnifierAnimator = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ParcelableParcel saveInstanceState() {
        ParcelableParcel state = new ParcelableParcel(getClass().getClassLoader());
        Parcel parcel = state.getParcel();
        this.mUndoManager.saveInstanceState(parcel);
        this.mUndoInputFilter.saveInstanceState(parcel);
        return state;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void restoreInstanceState(ParcelableParcel state) {
        Parcel parcel = state.getParcel();
        this.mUndoManager.restoreInstanceState(parcel, state.getClassLoader());
        this.mUndoInputFilter.restoreInstanceState(parcel);
        this.mUndoOwner = this.mUndoManager.getOwner("Editor", this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void forgetUndoRedo() {
        UndoOwner[] owners = {this.mUndoOwner};
        this.mUndoManager.forgetUndos(owners, -1);
        this.mUndoManager.forgetRedos(owners, -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean canUndo() {
        UndoOwner[] owners = {this.mUndoOwner};
        return this.mAllowUndo && this.mUndoManager.countUndos(owners) > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean canRedo() {
        UndoOwner[] owners = {this.mUndoOwner};
        return this.mAllowUndo && this.mUndoManager.countRedos(owners) > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void undo() {
        if (!this.mAllowUndo) {
            return;
        }
        UndoOwner[] owners = {this.mUndoOwner};
        this.mUndoManager.undo(owners, 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void redo() {
        if (!this.mAllowUndo) {
            return;
        }
        UndoOwner[] owners = {this.mUndoOwner};
        this.mUndoManager.redo(owners, 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void replace() {
        if (this.mSuggestionsPopupWindow == null) {
            this.mSuggestionsPopupWindow = new SuggestionsPopupWindow();
        }
        hideCursorAndSpanControllers();
        this.mSuggestionsPopupWindow.show();
        int middle = (this.mTextView.getSelectionStart() + this.mTextView.getSelectionEnd()) / 2;
        Selection.setSelection((Spannable) this.mTextView.getText(), middle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onAttachedToWindow() {
        if (this.mShowErrorAfterAttach) {
            showError();
            this.mShowErrorAfterAttach = false;
        }
        ViewTreeObserver observer = this.mTextView.getViewTreeObserver();
        if (observer.isAlive()) {
            if (this.mInsertionPointCursorController != null) {
                observer.addOnTouchModeChangeListener(this.mInsertionPointCursorController);
            }
            if (this.mSelectionModifierCursorController != null) {
                this.mSelectionModifierCursorController.resetTouchOffsets();
                observer.addOnTouchModeChangeListener(this.mSelectionModifierCursorController);
            }
        }
        updateSpellCheckSpans(0, this.mTextView.getText().length(), true);
        if (this.mTextView.hasSelection()) {
            refreshTextActionMode();
        }
        getPositionListener().addSubscriber(this.mCursorAnchorInfoNotifier, true);
        resumeBlink();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onDetachedFromWindow() {
        getPositionListener().removeSubscriber(this.mCursorAnchorInfoNotifier);
        if (this.mError != null) {
            hideError();
        }
        suspendBlink();
        if (this.mInsertionPointCursorController != null) {
            this.mInsertionPointCursorController.onDetached();
        }
        if (this.mSelectionModifierCursorController != null) {
            this.mSelectionModifierCursorController.onDetached();
        }
        if (this.mShowSuggestionRunnable != null) {
            this.mTextView.removeCallbacks(this.mShowSuggestionRunnable);
        }
        if (this.mInsertionActionModeRunnable != null) {
            this.mTextView.removeCallbacks(this.mInsertionActionModeRunnable);
        }
        this.mTextView.removeCallbacks(this.mShowFloatingToolbar);
        discardTextDisplayLists();
        if (this.mSpellChecker != null) {
            this.mSpellChecker.closeSession();
            this.mSpellChecker = null;
        }
        hideCursorAndSpanControllers();
        stopTextActionModeWithPreservingSelection();
    }

    private synchronized void discardTextDisplayLists() {
        if (this.mTextRenderNodes != null) {
            for (int i = 0; i < this.mTextRenderNodes.length; i++) {
                RenderNode displayList = this.mTextRenderNodes[i] != null ? this.mTextRenderNodes[i].renderNode : null;
                if (displayList != null && displayList.isValid()) {
                    displayList.discardDisplayList();
                }
            }
        }
    }

    private synchronized void showError() {
        if (this.mTextView.getWindowToken() == null) {
            this.mShowErrorAfterAttach = true;
            return;
        }
        if (this.mErrorPopup == null) {
            LayoutInflater inflater = LayoutInflater.from(this.mTextView.getContext());
            TextView err = (TextView) inflater.inflate(R.layout.textview_hint, (ViewGroup) null);
            float scale = this.mTextView.getResources().getDisplayMetrics().density;
            this.mErrorPopup = new ErrorPopup(err, (int) ((200.0f * scale) + 0.5f), (int) ((50.0f * scale) + 0.5f));
            this.mErrorPopup.setFocusable(false);
            this.mErrorPopup.setInputMethodMode(1);
        }
        TextView tv = (TextView) this.mErrorPopup.getContentView();
        chooseSize(this.mErrorPopup, this.mError, tv);
        tv.setText(this.mError);
        this.mErrorPopup.showAsDropDown(this.mTextView, getErrorX(), getErrorY(), 51);
        this.mErrorPopup.fixDirection(this.mErrorPopup.isAboveAnchor());
    }

    public synchronized void setError(CharSequence error, Drawable icon) {
        this.mError = TextUtils.stringOrSpannedString(error);
        this.mErrorWasChanged = true;
        if (this.mError == null) {
            setErrorIcon(null);
            if (this.mErrorPopup != null) {
                if (this.mErrorPopup.isShowing()) {
                    this.mErrorPopup.dismiss();
                }
                this.mErrorPopup = null;
            }
            this.mShowErrorAfterAttach = false;
            return;
        }
        setErrorIcon(icon);
        if (this.mTextView.isFocused()) {
            showError();
        }
    }

    private synchronized void setErrorIcon(Drawable icon) {
        TextView.Drawables dr = this.mTextView.mDrawables;
        if (dr == null) {
            TextView textView = this.mTextView;
            TextView.Drawables drawables = new TextView.Drawables(this.mTextView.getContext());
            dr = drawables;
            textView.mDrawables = drawables;
        }
        dr.setErrorDrawable(icon, this.mTextView);
        this.mTextView.resetResolvedDrawables();
        this.mTextView.invalidate();
        this.mTextView.requestLayout();
    }

    private synchronized void hideError() {
        if (this.mErrorPopup != null && this.mErrorPopup.isShowing()) {
            this.mErrorPopup.dismiss();
        }
        this.mShowErrorAfterAttach = false;
    }

    private synchronized int getErrorX() {
        float scale = this.mTextView.getResources().getDisplayMetrics().density;
        TextView.Drawables dr = this.mTextView.mDrawables;
        int layoutDirection = this.mTextView.getLayoutDirection();
        if (layoutDirection != 1) {
            int offset = ((-(dr != null ? dr.mDrawableSizeRight : 0)) / 2) + ((int) ((25.0f * scale) + 0.5f));
            int errorX = ((this.mTextView.getWidth() - this.mErrorPopup.getWidth()) - this.mTextView.getPaddingRight()) + offset;
            return errorX;
        }
        int offset2 = ((dr != null ? dr.mDrawableSizeLeft : 0) / 2) - ((int) ((25.0f * scale) + 0.5f));
        int errorX2 = this.mTextView.getPaddingLeft() + offset2;
        return errorX2;
    }

    private synchronized int getErrorY() {
        int compoundPaddingTop = this.mTextView.getCompoundPaddingTop();
        int vspace = ((this.mTextView.getBottom() - this.mTextView.getTop()) - this.mTextView.getCompoundPaddingBottom()) - compoundPaddingTop;
        TextView.Drawables dr = this.mTextView.mDrawables;
        int layoutDirection = this.mTextView.getLayoutDirection();
        int height = 0;
        if (layoutDirection != 1) {
            if (dr != null) {
                height = dr.mDrawableHeightRight;
            }
        } else if (dr != null) {
            height = dr.mDrawableHeightLeft;
        }
        int icontop = ((vspace - height) / 2) + compoundPaddingTop;
        float scale = this.mTextView.getResources().getDisplayMetrics().density;
        return ((icontop + height) - this.mTextView.getHeight()) - ((int) ((2.0f * scale) + 0.5f));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void createInputContentTypeIfNeeded() {
        if (this.mInputContentType == null) {
            this.mInputContentType = new InputContentType();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void createInputMethodStateIfNeeded() {
        if (this.mInputMethodState == null) {
            this.mInputMethodState = new InputMethodState();
        }
    }

    private synchronized boolean isCursorVisible() {
        return this.mCursorVisible && this.mTextView.isTextEditable();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean shouldRenderCursor() {
        if (isCursorVisible()) {
            if (this.mRenderCursorRegardlessTiming) {
                return true;
            }
            long showCursorDelta = SystemClock.uptimeMillis() - this.mShowCursor;
            return showCursorDelta % 1000 < 500;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void prepareCursorControllers() {
        boolean windowSupportsHandles = false;
        ViewGroup.LayoutParams params = this.mTextView.getRootView().getLayoutParams();
        boolean z = true;
        if (params instanceof WindowManager.LayoutParams) {
            WindowManager.LayoutParams windowParams = (WindowManager.LayoutParams) params;
            windowSupportsHandles = windowParams.type < 1000 || windowParams.type > 1999;
        }
        boolean enabled = windowSupportsHandles && this.mTextView.getLayout() != null;
        this.mInsertionControllerEnabled = enabled && isCursorVisible();
        if (!enabled || !this.mTextView.textCanBeSelected()) {
            z = false;
        }
        this.mSelectionControllerEnabled = z;
        if (!this.mInsertionControllerEnabled) {
            hideInsertionPointCursorController();
            if (this.mInsertionPointCursorController != null) {
                this.mInsertionPointCursorController.onDetached();
                this.mInsertionPointCursorController = null;
            }
        }
        if (!this.mSelectionControllerEnabled) {
            stopTextActionMode();
            if (this.mSelectionModifierCursorController != null) {
                this.mSelectionModifierCursorController.onDetached();
                this.mSelectionModifierCursorController = null;
            }
        }
    }

    synchronized void hideInsertionPointCursorController() {
        if (this.mInsertionPointCursorController != null) {
            this.mInsertionPointCursorController.hide();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void hideCursorAndSpanControllers() {
        hideCursorControllers();
        hideSpanControllers();
    }

    private synchronized void hideSpanControllers() {
        if (this.mSpanController != null) {
            this.mSpanController.hide();
        }
    }

    private synchronized void hideCursorControllers() {
        if (this.mSuggestionsPopupWindow != null && (this.mTextView.isInExtractedMode() || !this.mSuggestionsPopupWindow.isShowingUp())) {
            this.mSuggestionsPopupWindow.hide();
        }
        hideInsertionPointCursorController();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateSpellCheckSpans(int start, int end, boolean createSpellChecker) {
        this.mTextView.removeAdjacentSuggestionSpans(start);
        this.mTextView.removeAdjacentSuggestionSpans(end);
        if (this.mTextView.isTextEditable() && this.mTextView.isSuggestionsEnabled() && !this.mTextView.isInExtractedMode()) {
            if (this.mSpellChecker == null && createSpellChecker) {
                this.mSpellChecker = new SpellChecker(this.mTextView);
            }
            if (this.mSpellChecker != null) {
                this.mSpellChecker.spellCheck(start, end);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onScreenStateChanged(int screenState) {
        switch (screenState) {
            case 0:
                suspendBlink();
                return;
            case 1:
                resumeBlink();
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void suspendBlink() {
        if (this.mBlink != null) {
            this.mBlink.cancel();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void resumeBlink() {
        if (this.mBlink != null) {
            this.mBlink.uncancel();
            makeBlink();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void adjustInputType(boolean password, boolean passwordInputType, boolean webPasswordInputType, boolean numberPasswordInputType) {
        if ((this.mInputType & 15) == 1) {
            if (password || passwordInputType) {
                this.mInputType = (this.mInputType & (-4081)) | 128;
            }
            if (webPasswordInputType) {
                this.mInputType = (this.mInputType & (-4081)) | 224;
            }
        } else if ((this.mInputType & 15) == 2 && numberPasswordInputType) {
            this.mInputType = (this.mInputType & (-4081)) | 16;
        }
    }

    private synchronized void chooseSize(PopupWindow pop, CharSequence text, TextView tv) {
        int wid = tv.getPaddingLeft() + tv.getPaddingRight();
        int ht = tv.getPaddingTop() + tv.getPaddingBottom();
        int defaultWidthInPixels = this.mTextView.getResources().getDimensionPixelSize(R.dimen.textview_error_popup_default_width);
        StaticLayout l = StaticLayout.Builder.obtain(text, 0, text.length(), tv.getPaint(), defaultWidthInPixels).setUseLineSpacingFromFallbacks(tv.mUseFallbackLineSpacing).build();
        float max = 0.0f;
        for (int i = 0; i < l.getLineCount(); i++) {
            max = Math.max(max, l.getLineWidth(i));
        }
        pop.setWidth(((int) Math.ceil(max)) + wid);
        pop.setHeight(l.getHeight() + ht);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setFrame() {
        if (this.mErrorPopup != null) {
            TextView tv = (TextView) this.mErrorPopup.getContentView();
            chooseSize(this.mErrorPopup, this.mError, tv);
            this.mErrorPopup.update(this.mTextView, getErrorX(), getErrorY(), this.mErrorPopup.getWidth(), this.mErrorPopup.getHeight());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized int getWordStart(int offset) {
        int retOffset;
        int retOffset2 = getWordIteratorWithText().prevBoundary(offset);
        if (getWordIteratorWithText().isOnPunctuation(retOffset2)) {
            retOffset = getWordIteratorWithText().getPunctuationBeginning(offset);
        } else {
            retOffset = getWordIteratorWithText().getPrevWordBeginningOnTwoWordsBoundary(offset);
        }
        if (retOffset == -1) {
            return offset;
        }
        return retOffset;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized int getWordEnd(int offset) {
        int retOffset;
        int retOffset2 = getWordIteratorWithText().nextBoundary(offset);
        if (getWordIteratorWithText().isAfterPunctuation(retOffset2)) {
            retOffset = getWordIteratorWithText().getPunctuationEnd(offset);
        } else {
            retOffset = getWordIteratorWithText().getNextWordEndOnTwoWordBoundary(offset);
        }
        if (retOffset == -1) {
            return offset;
        }
        return retOffset;
    }

    private synchronized boolean needsToSelectAllToSelectWordOrParagraph() {
        if (this.mTextView.hasPasswordTransformationMethod()) {
            return true;
        }
        int inputType = this.mTextView.getInputType();
        int klass = inputType & 15;
        int variation = inputType & InputType.TYPE_MASK_VARIATION;
        return klass == 2 || klass == 3 || klass == 4 || variation == 16 || variation == 32 || variation == 208 || variation == 176;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean selectCurrentWord() {
        int selectionStart;
        int selectionEnd;
        if (this.mTextView.canSelectText()) {
            if (needsToSelectAllToSelectWordOrParagraph()) {
                return this.mTextView.selectAllText();
            }
            long lastTouchOffsets = getLastTouchOffsets();
            int minOffset = TextUtils.unpackRangeStartFromLong(lastTouchOffsets);
            int maxOffset = TextUtils.unpackRangeEndFromLong(lastTouchOffsets);
            if (minOffset < 0 || minOffset > this.mTextView.getText().length() || maxOffset < 0 || maxOffset > this.mTextView.getText().length()) {
                return false;
            }
            URLSpan[] urlSpans = (URLSpan[]) ((Spanned) this.mTextView.getText()).getSpans(minOffset, maxOffset, URLSpan.class);
            if (urlSpans.length >= 1) {
                URLSpan urlSpan = urlSpans[0];
                selectionStart = ((Spanned) this.mTextView.getText()).getSpanStart(urlSpan);
                selectionEnd = ((Spanned) this.mTextView.getText()).getSpanEnd(urlSpan);
            } else {
                WordIterator wordIterator = getWordIterator();
                wordIterator.setCharSequence(this.mTextView.getText(), minOffset, maxOffset);
                selectionStart = wordIterator.getBeginning(minOffset);
                int selectionEnd2 = wordIterator.getEnd(maxOffset);
                if (selectionStart == -1 || selectionEnd2 == -1 || selectionStart == selectionEnd2) {
                    long range = getCharClusterRange(minOffset);
                    selectionStart = TextUtils.unpackRangeStartFromLong(range);
                    selectionEnd = TextUtils.unpackRangeEndFromLong(range);
                } else {
                    selectionEnd = selectionEnd2;
                }
            }
            Selection.setSelection((Spannable) this.mTextView.getText(), selectionStart, selectionEnd);
            return selectionEnd > selectionStart;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean selectCurrentParagraph() {
        if (this.mTextView.canSelectText()) {
            if (needsToSelectAllToSelectWordOrParagraph()) {
                return this.mTextView.selectAllText();
            }
            long lastTouchOffsets = getLastTouchOffsets();
            int minLastTouchOffset = TextUtils.unpackRangeStartFromLong(lastTouchOffsets);
            int maxLastTouchOffset = TextUtils.unpackRangeEndFromLong(lastTouchOffsets);
            long paragraphsRange = getParagraphsRange(minLastTouchOffset, maxLastTouchOffset);
            int start = TextUtils.unpackRangeStartFromLong(paragraphsRange);
            int end = TextUtils.unpackRangeEndFromLong(paragraphsRange);
            if (start < end) {
                Selection.setSelection((Spannable) this.mTextView.getText(), start, end);
                return true;
            }
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized long getParagraphsRange(int startOffset, int endOffset) {
        Layout layout = this.mTextView.getLayout();
        if (layout == null) {
            return TextUtils.packRangeInLong(-1, -1);
        }
        CharSequence text = this.mTextView.getText();
        int minLine = layout.getLineForOffset(startOffset);
        while (minLine > 0) {
            int prevLineEndOffset = layout.getLineEnd(minLine - 1);
            if (text.charAt(prevLineEndOffset - 1) == '\n') {
                break;
            }
            minLine--;
        }
        int maxLine = layout.getLineForOffset(endOffset);
        while (maxLine < layout.getLineCount() - 1) {
            int lineEndOffset = layout.getLineEnd(maxLine);
            if (text.charAt(lineEndOffset - 1) == '\n') {
                break;
            }
            maxLine++;
        }
        return TextUtils.packRangeInLong(layout.getLineStart(minLine), layout.getLineEnd(maxLine));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onLocaleChanged() {
        this.mWordIterator = null;
        this.mWordIteratorWithText = null;
    }

    public synchronized WordIterator getWordIterator() {
        if (this.mWordIterator == null) {
            this.mWordIterator = new WordIterator(this.mTextView.getTextServicesLocale());
        }
        return this.mWordIterator;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized WordIterator getWordIteratorWithText() {
        if (this.mWordIteratorWithText == null) {
            this.mWordIteratorWithText = new WordIterator(this.mTextView.getTextServicesLocale());
            this.mUpdateWordIteratorText = true;
        }
        if (this.mUpdateWordIteratorText) {
            CharSequence text = this.mTextView.getText();
            this.mWordIteratorWithText.setCharSequence(text, 0, text.length());
            this.mUpdateWordIteratorText = false;
        }
        return this.mWordIteratorWithText;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized int getNextCursorOffset(int offset, boolean findAfterGivenOffset) {
        Layout layout = this.mTextView.getLayout();
        return layout == null ? offset : findAfterGivenOffset == layout.isRtlCharAt(offset) ? layout.getOffsetToLeftOf(offset) : layout.getOffsetToRightOf(offset);
    }

    private synchronized long getCharClusterRange(int offset) {
        int textLength = this.mTextView.getText().length();
        if (offset < textLength) {
            int clusterEndOffset = getNextCursorOffset(offset, true);
            return TextUtils.packRangeInLong(getNextCursorOffset(clusterEndOffset, false), clusterEndOffset);
        } else if (offset - 1 >= 0) {
            int clusterStartOffset = getNextCursorOffset(offset, false);
            return TextUtils.packRangeInLong(clusterStartOffset, getNextCursorOffset(clusterStartOffset, true));
        } else {
            return TextUtils.packRangeInLong(offset, offset);
        }
    }

    private synchronized boolean touchPositionIsInSelection() {
        int selectionStart = this.mTextView.getSelectionStart();
        int selectionEnd = this.mTextView.getSelectionEnd();
        if (selectionStart == selectionEnd) {
            return false;
        }
        if (selectionStart > selectionEnd) {
            selectionStart = selectionEnd;
            selectionEnd = selectionStart;
            Selection.setSelection((Spannable) this.mTextView.getText(), selectionStart, selectionEnd);
        }
        SelectionModifierCursorController selectionController = getSelectionController();
        int minOffset = selectionController.getMinTouchOffset();
        int maxOffset = selectionController.getMaxTouchOffset();
        return minOffset >= selectionStart && maxOffset < selectionEnd;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized PositionListener getPositionListener() {
        if (this.mPositionListener == null) {
            this.mPositionListener = new PositionListener();
        }
        return this.mPositionListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean isOffsetVisible(int offset) {
        Layout layout = this.mTextView.getLayout();
        if (layout == null) {
            return false;
        }
        int line = layout.getLineForOffset(offset);
        int lineBottom = layout.getLineBottom(line);
        int primaryHorizontal = (int) layout.getPrimaryHorizontal(offset);
        return this.mTextView.isPositionVisible(this.mTextView.viewportToContentHorizontalOffset() + primaryHorizontal, this.mTextView.viewportToContentVerticalOffset() + lineBottom);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean isPositionOnText(float x, float y) {
        Layout layout = this.mTextView.getLayout();
        if (layout == null) {
            return false;
        }
        int line = this.mTextView.getLineAtCoordinate(y);
        float x2 = this.mTextView.convertToLocalHorizontalCoordinate(x);
        if (x2 < layout.getLineLeft(line) || x2 > layout.getLineRight(line)) {
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void startDragAndDrop() {
        getSelectionActionModeHelper().onSelectionDrag();
        if (this.mTextView.isInExtractedMode()) {
            return;
        }
        int start = this.mTextView.getSelectionStart();
        int end = this.mTextView.getSelectionEnd();
        CharSequence selectedText = this.mTextView.getTransformedText(start, end);
        ClipData data = ClipData.newPlainText(null, selectedText);
        DragLocalState localState = new DragLocalState(this.mTextView, start, end);
        this.mTextView.startDragAndDrop(data, getTextThumbnailBuilder(start, end), localState, 256);
        stopTextActionMode();
        if (hasSelectionController()) {
            getSelectionController().resetTouchOffsets();
        }
    }

    public synchronized boolean performLongClick(boolean handled) {
        if (!handled && !isPositionOnText(this.mLastDownPositionX, this.mLastDownPositionY) && this.mInsertionControllerEnabled) {
            int offset = this.mTextView.getOffsetForPosition(this.mLastDownPositionX, this.mLastDownPositionY);
            Selection.setSelection((Spannable) this.mTextView.getText(), offset);
            getInsertionController().show();
            this.mIsInsertionActionModeStartPending = true;
            handled = true;
            MetricsLogger.action(this.mTextView.getContext(), (int) MetricsProto.MetricsEvent.TEXT_LONGPRESS, 0);
        }
        if (!handled && this.mTextActionMode != null) {
            if (touchPositionIsInSelection()) {
                startDragAndDrop();
                MetricsLogger.action(this.mTextView.getContext(), (int) MetricsProto.MetricsEvent.TEXT_LONGPRESS, 2);
            } else {
                stopTextActionMode();
                selectCurrentWordAndStartDrag();
                MetricsLogger.action(this.mTextView.getContext(), (int) MetricsProto.MetricsEvent.TEXT_LONGPRESS, 1);
            }
            handled = true;
        }
        if (!handled && (handled = selectCurrentWordAndStartDrag())) {
            MetricsLogger.action(this.mTextView.getContext(), (int) MetricsProto.MetricsEvent.TEXT_LONGPRESS, 1);
        }
        return handled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized float getLastUpPositionX() {
        return this.mLastUpPositionX;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized float getLastUpPositionY() {
        return this.mLastUpPositionY;
    }

    private synchronized long getLastTouchOffsets() {
        SelectionModifierCursorController selectionController = getSelectionController();
        int minOffset = selectionController.getMinTouchOffset();
        int maxOffset = selectionController.getMaxTouchOffset();
        return TextUtils.packRangeInLong(minOffset, maxOffset);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onFocusChanged(boolean focused, int direction) {
        this.mShowCursor = SystemClock.uptimeMillis();
        ensureEndedBatchEdit();
        if (focused) {
            int selStart = this.mTextView.getSelectionStart();
            int selEnd = this.mTextView.getSelectionEnd();
            boolean isFocusHighlighted = this.mSelectAllOnFocus && selStart == 0 && selEnd == this.mTextView.getText().length();
            this.mCreatedWithASelection = this.mFrozenWithFocus && this.mTextView.hasSelection() && !isFocusHighlighted;
            if (!this.mFrozenWithFocus || selStart < 0 || selEnd < 0) {
                int lastTapPosition = getLastTapPosition();
                if (lastTapPosition >= 0) {
                    Selection.setSelection((Spannable) this.mTextView.getText(), lastTapPosition);
                }
                MovementMethod mMovement = this.mTextView.getMovementMethod();
                if (mMovement != null) {
                    mMovement.onTakeFocus(this.mTextView, (Spannable) this.mTextView.getText(), direction);
                }
                if ((this.mTextView.isInExtractedMode() || this.mSelectionMoved) && selStart >= 0 && selEnd >= 0) {
                    Selection.setSelection((Spannable) this.mTextView.getText(), selStart, selEnd);
                }
                if (this.mSelectAllOnFocus) {
                    this.mTextView.selectAllText();
                }
                this.mTouchFocusSelected = true;
            }
            this.mFrozenWithFocus = false;
            this.mSelectionMoved = false;
            if (this.mError != null) {
                showError();
            }
            makeBlink();
            return;
        }
        if (this.mError != null) {
            hideError();
        }
        this.mTextView.onEndBatchEdit();
        if (this.mTextView.isInExtractedMode()) {
            hideCursorAndSpanControllers();
            stopTextActionModeWithPreservingSelection();
        } else {
            hideCursorAndSpanControllers();
            if (this.mTextView.isTemporarilyDetached()) {
                stopTextActionModeWithPreservingSelection();
            } else {
                stopTextActionMode();
            }
            downgradeEasyCorrectionSpans();
        }
        if (this.mSelectionModifierCursorController != null) {
            this.mSelectionModifierCursorController.resetTouchOffsets();
        }
        ensureNoSelectionIfNonSelectable();
    }

    private synchronized void ensureNoSelectionIfNonSelectable() {
        if (!this.mTextView.textCanBeSelected() && this.mTextView.hasSelection()) {
            Selection.setSelection((Spannable) this.mTextView.getText(), this.mTextView.length(), this.mTextView.length());
        }
    }

    private synchronized void downgradeEasyCorrectionSpans() {
        CharSequence text = this.mTextView.getText();
        if (text instanceof Spannable) {
            Spannable spannable = (Spannable) text;
            int flags = 0;
            SuggestionSpan[] suggestionSpans = (SuggestionSpan[]) spannable.getSpans(0, spannable.length(), SuggestionSpan.class);
            while (true) {
                int i = flags;
                if (i < suggestionSpans.length) {
                    int flags2 = suggestionSpans[i].getFlags();
                    if ((flags2 & 1) != 0 && (flags2 & 2) == 0) {
                        suggestionSpans[i].setFlags(flags2 & (-2));
                    }
                    flags = i + 1;
                } else {
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void sendOnTextChanged(int start, int before, int after) {
        getSelectionActionModeHelper().onTextChanged(start, start + before);
        updateSpellCheckSpans(start, start + after, false);
        this.mUpdateWordIteratorText = true;
        hideCursorControllers();
        if (this.mSelectionModifierCursorController != null) {
            this.mSelectionModifierCursorController.resetTouchOffsets();
        }
        stopTextActionMode();
    }

    private synchronized int getLastTapPosition() {
        int lastTapPosition;
        if (this.mSelectionModifierCursorController != null && (lastTapPosition = this.mSelectionModifierCursorController.getMinTouchOffset()) >= 0) {
            if (lastTapPosition > this.mTextView.getText().length()) {
                return this.mTextView.getText().length();
            }
            return lastTapPosition;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onWindowFocusChanged(boolean hasWindowFocus) {
        if (hasWindowFocus) {
            if (this.mBlink != null) {
                this.mBlink.uncancel();
                makeBlink();
            }
            if (this.mTextView.hasSelection() && !extractedTextModeWillBeStarted()) {
                refreshTextActionMode();
                return;
            }
            return;
        }
        if (this.mBlink != null) {
            this.mBlink.cancel();
        }
        if (this.mInputContentType != null) {
            this.mInputContentType.enterDown = false;
        }
        hideCursorAndSpanControllers();
        stopTextActionModeWithPreservingSelection();
        if (this.mSuggestionsPopupWindow != null) {
            this.mSuggestionsPopupWindow.onParentLostFocus();
        }
        ensureEndedBatchEdit();
        ensureNoSelectionIfNonSelectable();
    }

    private synchronized void updateTapState(MotionEvent event) {
        int action = event.getActionMasked();
        if (action == 0) {
            boolean isMouse = event.isFromSource(8194);
            if ((this.mTapState == 1 || (this.mTapState == 2 && isMouse)) && SystemClock.uptimeMillis() - this.mLastTouchUpTime <= ViewConfiguration.getDoubleTapTimeout()) {
                if (this.mTapState == 1) {
                    this.mTapState = 2;
                } else {
                    this.mTapState = 3;
                }
            } else {
                this.mTapState = 1;
            }
        }
        if (action == 1) {
            this.mLastTouchUpTime = SystemClock.uptimeMillis();
        }
    }

    private synchronized boolean shouldFilterOutTouchEvent(MotionEvent event) {
        if (event.isFromSource(8194)) {
            boolean primaryButtonStateChanged = ((this.mLastButtonState ^ event.getButtonState()) & 1) != 0;
            int action = event.getActionMasked();
            if ((action == 0 || action == 1) && !primaryButtonStateChanged) {
                return true;
            }
            return action == 2 && !event.isButtonPressed(1);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onTouchEvent(MotionEvent event) {
        boolean filterOutEvent = shouldFilterOutTouchEvent(event);
        this.mLastButtonState = event.getButtonState();
        if (filterOutEvent) {
            if (event.getActionMasked() == 1) {
                this.mDiscardNextActionUp = true;
                return;
            }
            return;
        }
        updateTapState(event);
        updateFloatingToolbarVisibility(event);
        if (hasSelectionController()) {
            getSelectionController().onTouchEvent(event);
        }
        if (this.mShowSuggestionRunnable != null) {
            this.mTextView.removeCallbacks(this.mShowSuggestionRunnable);
            this.mShowSuggestionRunnable = null;
        }
        if (event.getActionMasked() == 1) {
            this.mLastUpPositionX = event.getX();
            this.mLastUpPositionY = event.getY();
        }
        if (event.getActionMasked() == 0) {
            this.mLastDownPositionX = event.getX();
            this.mLastDownPositionY = event.getY();
            this.mTouchFocusSelected = false;
            this.mIgnoreActionUpEvent = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void updateFloatingToolbarVisibility(MotionEvent event) {
        if (this.mTextActionMode != null) {
            switch (event.getActionMasked()) {
                case 1:
                case 3:
                    showFloatingToolbar();
                    return;
                case 2:
                    hideFloatingToolbar(-1);
                    return;
                default:
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void hideFloatingToolbar(int duration) {
        if (this.mTextActionMode != null) {
            this.mTextView.removeCallbacks(this.mShowFloatingToolbar);
            this.mTextActionMode.hide(duration);
        }
    }

    private synchronized void showFloatingToolbar() {
        if (this.mTextActionMode != null) {
            int delay = ViewConfiguration.getDoubleTapTimeout();
            this.mTextView.postDelayed(this.mShowFloatingToolbar, delay);
            invalidateActionModeAsync();
        }
    }

    public synchronized void beginBatchEdit() {
        this.mInBatchEditControllers = true;
        InputMethodState ims = this.mInputMethodState;
        if (ims != null) {
            int nesting = ims.mBatchEditNesting + 1;
            ims.mBatchEditNesting = nesting;
            if (nesting == 1) {
                ims.mCursorChanged = false;
                ims.mChangedDelta = 0;
                if (ims.mContentChanged) {
                    ims.mChangedStart = 0;
                    ims.mChangedEnd = this.mTextView.getText().length();
                } else {
                    ims.mChangedStart = -1;
                    ims.mChangedEnd = -1;
                    ims.mContentChanged = false;
                }
                this.mUndoInputFilter.beginBatchEdit();
                this.mTextView.onBeginBatchEdit();
            }
        }
    }

    public synchronized void endBatchEdit() {
        this.mInBatchEditControllers = false;
        InputMethodState ims = this.mInputMethodState;
        if (ims != null) {
            int nesting = ims.mBatchEditNesting - 1;
            ims.mBatchEditNesting = nesting;
            if (nesting == 0) {
                finishBatchEdit(ims);
            }
        }
    }

    synchronized void ensureEndedBatchEdit() {
        InputMethodState ims = this.mInputMethodState;
        if (ims != null && ims.mBatchEditNesting != 0) {
            ims.mBatchEditNesting = 0;
            finishBatchEdit(ims);
        }
    }

    synchronized void finishBatchEdit(InputMethodState ims) {
        this.mTextView.onEndBatchEdit();
        this.mUndoInputFilter.endBatchEdit();
        if (ims.mContentChanged || ims.mSelectionModeChanged) {
            this.mTextView.updateAfterEdit();
            reportExtractedText();
        } else if (ims.mCursorChanged) {
            this.mTextView.invalidateCursor();
        }
        sendUpdateSelection();
        if (this.mTextActionMode != null) {
            CursorController cursorController = this.mTextView.hasSelection() ? getSelectionController() : getInsertionController();
            if (cursorController != null && !cursorController.isActive() && !cursorController.isCursorBeingModified()) {
                cursorController.show();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean extractText(ExtractedTextRequest request, ExtractedText outText) {
        return extractTextInternal(request, -1, -1, -1, outText);
    }

    private synchronized boolean extractTextInternal(ExtractedTextRequest request, int partialStartOffset, int partialEndOffset, int delta, ExtractedText outText) {
        CharSequence content;
        int partialEndOffset2;
        if (request == null || outText == null || (content = this.mTextView.getText()) == null) {
            return false;
        }
        if (partialStartOffset != -2) {
            int N = content.length();
            if (partialStartOffset < 0) {
                outText.partialEndOffset = -1;
                outText.partialStartOffset = -1;
                partialStartOffset = 0;
                partialEndOffset2 = N;
            } else {
                partialEndOffset2 = partialEndOffset + delta;
                if (content instanceof Spanned) {
                    Spanned spanned = (Spanned) content;
                    Object[] spans = spanned.getSpans(partialStartOffset, partialEndOffset2, ParcelableSpan.class);
                    int i = spans.length;
                    while (i > 0) {
                        i--;
                        int j = spanned.getSpanStart(spans[i]);
                        if (j < partialStartOffset) {
                            partialStartOffset = j;
                        }
                        int j2 = spanned.getSpanEnd(spans[i]);
                        if (j2 > partialEndOffset2) {
                            partialEndOffset2 = j2;
                        }
                    }
                }
                outText.partialStartOffset = partialStartOffset;
                outText.partialEndOffset = partialEndOffset2 - delta;
                if (partialStartOffset > N) {
                    partialStartOffset = N;
                } else if (partialStartOffset < 0) {
                    partialStartOffset = 0;
                }
                if (partialEndOffset2 > N) {
                    partialEndOffset2 = N;
                } else if (partialEndOffset2 < 0) {
                    partialEndOffset2 = 0;
                }
            }
            if ((request.flags & 1) != 0) {
                outText.text = content.subSequence(partialStartOffset, partialEndOffset2);
            } else {
                outText.text = TextUtils.substring(content, partialStartOffset, partialEndOffset2);
            }
        } else {
            outText.partialStartOffset = 0;
            outText.partialEndOffset = 0;
            outText.text = "";
        }
        outText.flags = 0;
        if (MetaKeyKeyListener.getMetaState(content, 2048) != 0) {
            outText.flags |= 2;
        }
        if (this.mTextView.isSingleLine()) {
            outText.flags |= 1;
        }
        outText.startOffset = 0;
        outText.selectionStart = this.mTextView.getSelectionStart();
        outText.selectionEnd = this.mTextView.getSelectionEnd();
        outText.hint = this.mTextView.getHint();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean reportExtractedText() {
        InputMethodManager imm;
        InputMethodState ims = this.mInputMethodState;
        if (ims == null) {
            return false;
        }
        boolean wasContentChanged = ims.mContentChanged;
        if (!wasContentChanged && !ims.mSelectionModeChanged) {
            return false;
        }
        ims.mContentChanged = false;
        ims.mSelectionModeChanged = false;
        ExtractedTextRequest req = ims.mExtractedTextRequest;
        if (req == null || (imm = InputMethodManager.peekInstance()) == null) {
            return false;
        }
        if (ims.mChangedStart < 0 && !wasContentChanged) {
            ims.mChangedStart = -2;
        }
        if (!extractTextInternal(req, ims.mChangedStart, ims.mChangedEnd, ims.mChangedDelta, ims.mExtractedText)) {
            return false;
        }
        imm.updateExtractedText(this.mTextView, req.token, ims.mExtractedText);
        ims.mChangedStart = -1;
        ims.mChangedEnd = -1;
        ims.mChangedDelta = 0;
        ims.mContentChanged = false;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void sendUpdateSelection() {
        InputMethodManager imm;
        if (this.mInputMethodState != null && this.mInputMethodState.mBatchEditNesting <= 0 && (imm = InputMethodManager.peekInstance()) != null) {
            int selectionStart = this.mTextView.getSelectionStart();
            int selectionEnd = this.mTextView.getSelectionEnd();
            int candStart = -1;
            int candEnd = -1;
            if (this.mTextView.getText() instanceof Spannable) {
                Spannable sp = (Spannable) this.mTextView.getText();
                candStart = EditableInputConnection.getComposingSpanStart(sp);
                candEnd = EditableInputConnection.getComposingSpanEnd(sp);
            }
            imm.updateSelection(this.mTextView, selectionStart, selectionEnd, candStart, candEnd);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onDraw(Canvas canvas, Layout layout, Path highlight, Paint highlightPaint, int cursorOffsetVertical) {
        InputMethodManager imm;
        int selectionStart = this.mTextView.getSelectionStart();
        int selectionEnd = this.mTextView.getSelectionEnd();
        InputMethodState ims = this.mInputMethodState;
        if (ims != null && ims.mBatchEditNesting == 0 && (imm = InputMethodManager.peekInstance()) != null && imm.isActive(this.mTextView) && (ims.mContentChanged || ims.mSelectionModeChanged)) {
            reportExtractedText();
        }
        if (this.mCorrectionHighlighter != null) {
            this.mCorrectionHighlighter.draw(canvas, cursorOffsetVertical);
        }
        if (highlight != null && selectionStart == selectionEnd && this.mDrawableForCursor != null) {
            drawCursor(canvas, cursorOffsetVertical);
            highlight = null;
        }
        if (this.mSelectionActionModeHelper != null) {
            this.mSelectionActionModeHelper.onDraw(canvas);
            if (this.mSelectionActionModeHelper.isDrawingHighlight()) {
                highlight = null;
            }
        }
        if (this.mTextView.canHaveDisplayList() && canvas.isHardwareAccelerated()) {
            drawHardwareAccelerated(canvas, layout, highlight, highlightPaint, cursorOffsetVertical);
        } else {
            layout.draw(canvas, highlight, highlightPaint, cursorOffsetVertical);
        }
    }

    private synchronized void drawHardwareAccelerated(Canvas canvas, Layout layout, Path highlight, Paint highlightPaint, int cursorOffsetVertical) {
        int i;
        int numberOfBlocks;
        int[] blockEndLines;
        DynamicLayout dynamicLayout;
        int lastLine;
        int firstLine;
        ArraySet<Integer> blockSet;
        int indexFirstChangedBlock;
        int firstLine2;
        DynamicLayout dynamicLayout2;
        int indexFirstChangedBlock2;
        int indexFirstChangedBlock3;
        int i2;
        ArraySet<Integer> blockSet2;
        int lastIndex;
        boolean z;
        boolean z2;
        long lineRange;
        int i3;
        Editor editor = this;
        Canvas canvas2 = canvas;
        Layout layout2 = layout;
        long lineRange2 = layout2.getLineRangeForDraw(canvas2);
        int firstLine3 = TextUtils.unpackRangeStartFromLong(lineRange2);
        int lastLine2 = TextUtils.unpackRangeEndFromLong(lineRange2);
        if (lastLine2 < 0) {
            return;
        }
        layout2.drawBackground(canvas2, highlight, highlightPaint, cursorOffsetVertical, firstLine3, lastLine2);
        if (layout2 instanceof DynamicLayout) {
            if (editor.mTextRenderNodes == null) {
                editor.mTextRenderNodes = (TextRenderNode[]) ArrayUtils.emptyArray(TextRenderNode.class);
            }
            DynamicLayout dynamicLayout3 = (DynamicLayout) layout2;
            int[] blockEndLines2 = dynamicLayout3.getBlockEndLines();
            int[] blockIndices = dynamicLayout3.getBlockIndices();
            int numberOfBlocks2 = dynamicLayout3.getNumberOfBlocks();
            int indexFirstChangedBlock4 = dynamicLayout3.getIndexFirstChangedBlock();
            ArraySet<Integer> blockSet3 = dynamicLayout3.getBlocksAlwaysNeedToBeRedrawn();
            int i4 = -1;
            boolean z3 = true;
            if (blockSet3 != null) {
                int i5 = 0;
                while (i5 < blockSet3.size()) {
                    int blockIndex = dynamicLayout3.getBlockIndex(blockSet3.valueAt(i5).intValue());
                    if (blockIndex != i4 && editor.mTextRenderNodes[blockIndex] != null) {
                        editor.mTextRenderNodes[blockIndex].needsToBeShifted = true;
                    }
                    i5++;
                    i4 = -1;
                }
            }
            int i6 = 0;
            int startBlock = Arrays.binarySearch(blockEndLines2, 0, numberOfBlocks2, firstLine3);
            if (startBlock < 0) {
                startBlock = -(startBlock + 1);
            }
            int startIndexToFindAvailableRenderNode = 0;
            int startIndexToFindAvailableRenderNode2 = Math.min(indexFirstChangedBlock4, startBlock);
            while (true) {
                int i7 = startIndexToFindAvailableRenderNode2;
                if (i7 < numberOfBlocks2) {
                    int blockIndex2 = blockIndices[i7];
                    if (i7 < indexFirstChangedBlock4) {
                        z = z3;
                    } else if (blockIndex2 != -1 && editor.mTextRenderNodes[blockIndex2] != null) {
                        z = true;
                        editor.mTextRenderNodes[blockIndex2].needsToBeShifted = true;
                    } else {
                        z = true;
                    }
                    if (blockEndLines2[i7] < firstLine3) {
                        z2 = z;
                        i3 = i7;
                        numberOfBlocks = numberOfBlocks2;
                        blockEndLines = blockEndLines2;
                        dynamicLayout = dynamicLayout3;
                        lastLine = lastLine2;
                        firstLine = firstLine3;
                        lineRange = lineRange2;
                        i = 0;
                        blockSet = blockSet3;
                        indexFirstChangedBlock = indexFirstChangedBlock4;
                    } else {
                        z2 = z;
                        i = 0;
                        lineRange = lineRange2;
                        i3 = i7;
                        blockSet = blockSet3;
                        indexFirstChangedBlock = indexFirstChangedBlock4;
                        numberOfBlocks = numberOfBlocks2;
                        blockEndLines = blockEndLines2;
                        dynamicLayout = dynamicLayout3;
                        lastLine = lastLine2;
                        firstLine = firstLine3;
                        startIndexToFindAvailableRenderNode = editor.drawHardwareAcceleratedInner(canvas2, layout2, highlight, highlightPaint, cursorOffsetVertical, blockEndLines2, blockIndices, i3, numberOfBlocks, startIndexToFindAvailableRenderNode);
                        if (blockEndLines[i3] >= lastLine) {
                            int lastIndex2 = Math.max(indexFirstChangedBlock, i3 + 1);
                            firstLine2 = lastIndex2;
                            break;
                        }
                    }
                    startIndexToFindAvailableRenderNode2 = i3 + 1;
                    lastLine2 = lastLine;
                    dynamicLayout3 = dynamicLayout;
                    indexFirstChangedBlock4 = indexFirstChangedBlock;
                    blockSet3 = blockSet;
                    z3 = z2;
                    i6 = i;
                    lineRange2 = lineRange;
                    numberOfBlocks2 = numberOfBlocks;
                    blockEndLines2 = blockEndLines;
                    firstLine3 = firstLine;
                    canvas2 = canvas;
                    layout2 = layout;
                } else {
                    i = i6;
                    numberOfBlocks = numberOfBlocks2;
                    blockEndLines = blockEndLines2;
                    dynamicLayout = dynamicLayout3;
                    lastLine = lastLine2;
                    firstLine = firstLine3;
                    blockSet = blockSet3;
                    indexFirstChangedBlock = indexFirstChangedBlock4;
                    firstLine2 = numberOfBlocks2;
                    break;
                }
            }
            if (blockSet != null) {
                while (true) {
                    int i8 = i;
                    if (i8 >= blockSet.size()) {
                        break;
                    }
                    int block = blockSet.valueAt(i8).intValue();
                    int blockIndex3 = dynamicLayout.getBlockIndex(block);
                    if (blockIndex3 == -1 || editor.mTextRenderNodes[blockIndex3] == null || editor.mTextRenderNodes[blockIndex3].needsToBeShifted) {
                        Editor editor2 = editor;
                        dynamicLayout2 = dynamicLayout;
                        indexFirstChangedBlock2 = indexFirstChangedBlock;
                        indexFirstChangedBlock3 = lastLine;
                        i2 = i8;
                        blockSet2 = blockSet;
                        lastIndex = firstLine2;
                        int lastIndex3 = startIndexToFindAvailableRenderNode;
                        startIndexToFindAvailableRenderNode = editor2.drawHardwareAcceleratedInner(canvas, layout, highlight, highlightPaint, cursorOffsetVertical, blockEndLines, blockIndices, block, numberOfBlocks, lastIndex3);
                    } else {
                        i2 = i8;
                        dynamicLayout2 = dynamicLayout;
                        indexFirstChangedBlock2 = indexFirstChangedBlock;
                        blockSet2 = blockSet;
                        lastIndex = firstLine2;
                        indexFirstChangedBlock3 = lastLine;
                    }
                    i = i2 + 1;
                    dynamicLayout = dynamicLayout2;
                    lastLine = indexFirstChangedBlock3;
                    firstLine2 = lastIndex;
                    indexFirstChangedBlock = indexFirstChangedBlock2;
                    blockSet = blockSet2;
                    editor = this;
                }
            }
            int lastIndex4 = firstLine2;
            dynamicLayout.setIndexFirstChangedBlock(lastIndex4);
            return;
        }
        layout2.drawText(canvas2, firstLine3, lastLine2);
    }

    private synchronized int drawHardwareAcceleratedInner(Canvas canvas, Layout layout, Path highlight, Paint highlightPaint, int cursorOffsetVertical, int[] blockEndLines, int[] blockIndices, int blockInfoIndex, int numberOfBlocks, int startIndexToFindAvailableRenderNode) {
        int blockIndex;
        int i;
        int left;
        int right;
        boolean blockIsInvalid;
        int blockEndLine = blockEndLines[blockInfoIndex];
        int blockIndex2 = blockIndices[blockInfoIndex];
        boolean blockIsInvalid2 = blockIndex2 == -1;
        if (blockIsInvalid2) {
            blockIndex2 = getAvailableDisplayListIndex(blockIndices, numberOfBlocks, startIndexToFindAvailableRenderNode);
            blockIndices[blockInfoIndex] = blockIndex2;
            if (this.mTextRenderNodes[blockIndex2] != null) {
                this.mTextRenderNodes[blockIndex2].isDirty = true;
            }
            blockIndex = blockIndex2 + 1;
        } else {
            blockIndex = startIndexToFindAvailableRenderNode;
        }
        int startIndexToFindAvailableRenderNode2 = blockIndex;
        int blockIndex3 = blockIndex2;
        if (this.mTextRenderNodes[blockIndex3] == null) {
            this.mTextRenderNodes[blockIndex3] = new TextRenderNode("Text " + blockIndex3);
        }
        boolean blockDisplayListIsInvalid = this.mTextRenderNodes[blockIndex3].needsRecord();
        RenderNode blockDisplayList = this.mTextRenderNodes[blockIndex3].renderNode;
        if (this.mTextRenderNodes[blockIndex3].needsToBeShifted || blockDisplayListIsInvalid) {
            if (blockInfoIndex != 0) {
                i = blockEndLines[blockInfoIndex - 1] + 1;
            } else {
                i = 0;
            }
            int blockBeginLine = i;
            int top = layout.getLineTop(blockBeginLine);
            int bottom = layout.getLineBottom(blockEndLine);
            int right2 = this.mTextView.getWidth();
            if (!this.mTextView.getHorizontallyScrolling()) {
                left = 0;
                right = right2;
            } else {
                float min = Float.MAX_VALUE;
                float max = Float.MIN_VALUE;
                for (int line = blockBeginLine; line <= blockEndLine; line++) {
                    min = Math.min(min, layout.getLineLeft(line));
                    max = Math.max(max, layout.getLineRight(line));
                }
                int line2 = (int) min;
                right = (int) (0.5f + max);
                left = line2;
            }
            if (blockDisplayListIsInvalid) {
                DisplayListCanvas displayListCanvas = blockDisplayList.start(right - left, bottom - top);
                try {
                    displayListCanvas.translate(-left, -top);
                    layout.drawText(displayListCanvas, blockBeginLine, blockEndLine);
                    this.mTextRenderNodes[blockIndex3].isDirty = false;
                    blockDisplayList.end(displayListCanvas);
                    blockDisplayList.setClipToBounds(false);
                    blockIsInvalid = false;
                } catch (Throwable th) {
                    blockDisplayList.end(displayListCanvas);
                    blockDisplayList.setClipToBounds(false);
                    throw th;
                }
            } else {
                blockIsInvalid = false;
            }
            blockDisplayList.setLeftTopRightBottom(left, top, right, bottom);
            this.mTextRenderNodes[blockIndex3].needsToBeShifted = blockIsInvalid;
        }
        ((DisplayListCanvas) canvas).drawRenderNode(blockDisplayList);
        return startIndexToFindAvailableRenderNode2;
    }

    private synchronized int getAvailableDisplayListIndex(int[] blockIndices, int numberOfBlocks, int searchStartIndex) {
        int length = this.mTextRenderNodes.length;
        for (int i = searchStartIndex; i < length; i++) {
            boolean blockIndexFound = false;
            int j = 0;
            while (true) {
                if (j < numberOfBlocks) {
                    if (blockIndices[j] != i) {
                        j++;
                    } else {
                        blockIndexFound = true;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (!blockIndexFound) {
                return i;
            }
        }
        this.mTextRenderNodes = (TextRenderNode[]) GrowingArrayUtils.append(this.mTextRenderNodes, length, (Object) null);
        return length;
    }

    private synchronized void drawCursor(Canvas canvas, int cursorOffsetVertical) {
        boolean translate = cursorOffsetVertical != 0;
        if (translate) {
            canvas.translate(0.0f, cursorOffsetVertical);
        }
        if (this.mDrawableForCursor != null) {
            this.mDrawableForCursor.draw(canvas);
        }
        if (translate) {
            canvas.translate(0.0f, -cursorOffsetVertical);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void invalidateHandlesAndActionMode() {
        if (this.mSelectionModifierCursorController != null) {
            this.mSelectionModifierCursorController.invalidateHandles();
        }
        if (this.mInsertionPointCursorController != null) {
            this.mInsertionPointCursorController.invalidateHandle();
        }
        if (this.mTextActionMode != null) {
            invalidateActionMode();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void invalidateTextDisplayList(Layout layout, int start, int end) {
        if (this.mTextRenderNodes != null && (layout instanceof DynamicLayout)) {
            int firstLine = layout.getLineForOffset(start);
            int lastLine = layout.getLineForOffset(end);
            DynamicLayout dynamicLayout = (DynamicLayout) layout;
            int[] blockEndLines = dynamicLayout.getBlockEndLines();
            int[] blockIndices = dynamicLayout.getBlockIndices();
            int numberOfBlocks = dynamicLayout.getNumberOfBlocks();
            int i = 0;
            while (i < numberOfBlocks && blockEndLines[i] < firstLine) {
                i++;
            }
            while (i < numberOfBlocks) {
                int blockIndex = blockIndices[i];
                if (blockIndex != -1) {
                    this.mTextRenderNodes[blockIndex].isDirty = true;
                }
                if (blockEndLines[i] < lastLine) {
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public private protected void invalidateTextDisplayList() {
        if (this.mTextRenderNodes != null) {
            for (int i = 0; i < this.mTextRenderNodes.length; i++) {
                if (this.mTextRenderNodes[i] != null) {
                    this.mTextRenderNodes[i].isDirty = true;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void updateCursorPosition() {
        if (this.mTextView.mCursorDrawableRes == 0) {
            this.mDrawableForCursor = null;
            return;
        }
        Layout layout = this.mTextView.getLayout();
        int offset = this.mTextView.getSelectionStart();
        int line = layout.getLineForOffset(offset);
        int top = layout.getLineTop(line);
        int bottom = layout.getLineBottomWithoutSpacing(line);
        boolean clamped = layout.shouldClampCursor(line);
        updateCursorPosition(top, bottom, layout.getPrimaryHorizontal(offset, clamped));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void refreshTextActionMode() {
        if (extractedTextModeWillBeStarted()) {
            this.mRestartActionModeOnNextRefresh = false;
            return;
        }
        boolean hasSelection = this.mTextView.hasSelection();
        SelectionModifierCursorController selectionController = getSelectionController();
        InsertionPointCursorController insertionController = getInsertionController();
        if ((selectionController != null && selectionController.isCursorBeingModified()) || (insertionController != null && insertionController.isCursorBeingModified())) {
            this.mRestartActionModeOnNextRefresh = false;
            return;
        }
        if (hasSelection) {
            hideInsertionPointCursorController();
            if (this.mTextActionMode == null) {
                if (this.mRestartActionModeOnNextRefresh) {
                    startSelectionActionModeAsync(false);
                }
            } else if (selectionController == null || !selectionController.isActive()) {
                stopTextActionModeWithPreservingSelection();
                startSelectionActionModeAsync(false);
            } else {
                this.mTextActionMode.invalidateContentRect();
            }
        } else if (insertionController == null || !insertionController.isActive()) {
            stopTextActionMode();
        } else if (this.mTextActionMode != null) {
            this.mTextActionMode.invalidateContentRect();
        }
        this.mRestartActionModeOnNextRefresh = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void startInsertionActionMode() {
        if (this.mInsertionActionModeRunnable != null) {
            this.mTextView.removeCallbacks(this.mInsertionActionModeRunnable);
        }
        if (extractedTextModeWillBeStarted()) {
            return;
        }
        stopTextActionMode();
        ActionMode.Callback actionModeCallback = new TextActionModeCallback(1);
        this.mTextActionMode = this.mTextView.startActionMode(actionModeCallback, 1);
        if (this.mTextActionMode != null && getInsertionController() != null) {
            getInsertionController().show();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized TextView getTextView() {
        return this.mTextView;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ActionMode getTextActionMode() {
        return this.mTextActionMode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setRestartActionModeOnNextRefresh(boolean value) {
        this.mRestartActionModeOnNextRefresh = value;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void startSelectionActionModeAsync(boolean adjustSelection) {
        getSelectionActionModeHelper().startSelectionActionModeAsync(adjustSelection);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void startLinkActionModeAsync(int start, int end) {
        if (!(this.mTextView.getText() instanceof Spannable)) {
            return;
        }
        stopTextActionMode();
        this.mRequestingLinkActionMode = true;
        getSelectionActionModeHelper().startLinkActionModeAsync(start, end);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void invalidateActionModeAsync() {
        getSelectionActionModeHelper().invalidateActionModeAsync();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void invalidateActionMode() {
        if (this.mTextActionMode != null) {
            this.mTextActionMode.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized SelectionActionModeHelper getSelectionActionModeHelper() {
        if (this.mSelectionActionModeHelper == null) {
            this.mSelectionActionModeHelper = new SelectionActionModeHelper(this);
        }
        return this.mSelectionActionModeHelper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean selectCurrentWordAndStartDrag() {
        if (this.mInsertionActionModeRunnable != null) {
            this.mTextView.removeCallbacks(this.mInsertionActionModeRunnable);
        }
        if (!extractedTextModeWillBeStarted() && checkField()) {
            if (this.mTextView.hasSelection() || selectCurrentWord()) {
                stopTextActionModeWithPreservingSelection();
                getSelectionController().enterDrag(2);
                return true;
            }
            return false;
        }
        return false;
    }

    synchronized boolean checkField() {
        if (!this.mTextView.canSelectText() || !this.mTextView.requestFocus()) {
            Log.w("TextView", "TextView does not support text selection. Selection cancelled.");
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean startActionModeInternal(@TextActionMode int actionMode) {
        InputMethodManager imm;
        if (extractedTextModeWillBeStarted()) {
            return false;
        }
        if (this.mTextActionMode != null) {
            invalidateActionMode();
            return false;
        } else if (actionMode == 2 || (checkField() && this.mTextView.hasSelection())) {
            ActionMode.Callback actionModeCallback = new TextActionModeCallback(actionMode);
            this.mTextActionMode = this.mTextView.startActionMode(actionModeCallback, 1);
            boolean selectableText = this.mTextView.isTextEditable() || this.mTextView.isTextSelectable();
            if (actionMode == 2 && !selectableText && (this.mTextActionMode instanceof FloatingActionMode)) {
                ((FloatingActionMode) this.mTextActionMode).setOutsideTouchable(true, new PopupWindow.OnDismissListener() { // from class: android.widget.-$$Lambda$Editor$TdqUlJ6RRep0wXYHaRH51nTa08I
                    @Override // android.widget.PopupWindow.OnDismissListener
                    public final void onDismiss() {
                        Editor.this.stopTextActionMode();
                    }
                });
            }
            boolean selectionStarted = this.mTextActionMode != null;
            if (selectionStarted && this.mTextView.isTextEditable() && !this.mTextView.isTextSelectable() && this.mShowSoftInputOnFocus && (imm = InputMethodManager.peekInstance()) != null) {
                imm.showSoftInput(this.mTextView, 0, null);
            }
            return selectionStarted;
        } else {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean extractedTextModeWillBeStarted() {
        InputMethodManager imm;
        return (this.mTextView.isInExtractedMode() || (imm = InputMethodManager.peekInstance()) == null || !imm.isFullscreenMode()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean shouldOfferToShowSuggestions() {
        CharSequence text = this.mTextView.getText();
        if (text instanceof Spannable) {
            Spannable spannable = (Spannable) text;
            int selectionStart = this.mTextView.getSelectionStart();
            int selectionEnd = this.mTextView.getSelectionEnd();
            SuggestionSpan[] suggestionSpans = (SuggestionSpan[]) spannable.getSpans(selectionStart, selectionEnd, SuggestionSpan.class);
            if (suggestionSpans.length == 0) {
                return false;
            }
            if (selectionStart == selectionEnd) {
                for (SuggestionSpan suggestionSpan : suggestionSpans) {
                    if (suggestionSpan.getSuggestions().length > 0) {
                        return true;
                    }
                }
                return false;
            }
            int minSpanStart = this.mTextView.getText().length();
            int unionOfSpansCoveringSelectionStartStart = this.mTextView.getText().length();
            boolean unionOfSpansCoveringSelectionStartEnd = false;
            int unionOfSpansCoveringSelectionStartEnd2 = 0;
            int unionOfSpansCoveringSelectionStartStart2 = unionOfSpansCoveringSelectionStartStart;
            int maxSpanEnd = 0;
            int minSpanStart2 = minSpanStart;
            for (int minSpanStart3 = 0; minSpanStart3 < suggestionSpans.length; minSpanStart3++) {
                int spanStart = spannable.getSpanStart(suggestionSpans[minSpanStart3]);
                int spanEnd = spannable.getSpanEnd(suggestionSpans[minSpanStart3]);
                minSpanStart2 = Math.min(minSpanStart2, spanStart);
                maxSpanEnd = Math.max(maxSpanEnd, spanEnd);
                if (selectionStart >= spanStart && selectionStart <= spanEnd) {
                    boolean hasValidSuggestions = unionOfSpansCoveringSelectionStartEnd || suggestionSpans[minSpanStart3].getSuggestions().length > 0;
                    unionOfSpansCoveringSelectionStartStart2 = Math.min(unionOfSpansCoveringSelectionStartStart2, spanStart);
                    unionOfSpansCoveringSelectionStartEnd2 = Math.max(unionOfSpansCoveringSelectionStartEnd2, spanEnd);
                    unionOfSpansCoveringSelectionStartEnd = hasValidSuggestions;
                }
            }
            return unionOfSpansCoveringSelectionStartEnd && unionOfSpansCoveringSelectionStartStart2 < unionOfSpansCoveringSelectionStartEnd2 && minSpanStart2 >= unionOfSpansCoveringSelectionStartStart2 && maxSpanEnd <= unionOfSpansCoveringSelectionStartEnd2;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean isCursorInsideEasyCorrectionSpan() {
        Spannable spannable = (Spannable) this.mTextView.getText();
        SuggestionSpan[] suggestionSpans = (SuggestionSpan[]) spannable.getSpans(this.mTextView.getSelectionStart(), this.mTextView.getSelectionEnd(), SuggestionSpan.class);
        for (SuggestionSpan suggestionSpan : suggestionSpans) {
            if ((suggestionSpan.getFlags() & 1) != 0) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onTouchUpEvent(MotionEvent event) {
        if (getSelectionActionModeHelper().resetSelection(getTextView().getOffsetForPosition(event.getX(), event.getY()))) {
            return;
        }
        boolean selectAllGotFocus = this.mSelectAllOnFocus && this.mTextView.didTouchFocusSelect();
        hideCursorAndSpanControllers();
        stopTextActionMode();
        CharSequence text = this.mTextView.getText();
        if (!selectAllGotFocus && text.length() > 0) {
            int offset = this.mTextView.getOffsetForPosition(event.getX(), event.getY());
            boolean shouldInsertCursor = true ^ this.mRequestingLinkActionMode;
            if (shouldInsertCursor) {
                Selection.setSelection((Spannable) text, offset);
                if (this.mSpellChecker != null) {
                    this.mSpellChecker.onSelectionChanged();
                }
            }
            if (!extractedTextModeWillBeStarted()) {
                if (isCursorInsideEasyCorrectionSpan()) {
                    if (this.mInsertionActionModeRunnable != null) {
                        this.mTextView.removeCallbacks(this.mInsertionActionModeRunnable);
                    }
                    this.mShowSuggestionRunnable = new Runnable() { // from class: android.widget.-$$Lambda$DZXn7FbDDFyBvNjI-iG9_hfa7kw
                        @Override // java.lang.Runnable
                        public final void run() {
                            Editor.this.replace();
                        }
                    };
                    this.mTextView.postDelayed(this.mShowSuggestionRunnable, ViewConfiguration.getDoubleTapTimeout());
                } else if (hasInsertionController()) {
                    if (shouldInsertCursor) {
                        getInsertionController().show();
                    } else {
                        getInsertionController().hide();
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void stopTextActionMode() {
        if (this.mTextActionMode != null) {
            this.mTextActionMode.finish();
        }
    }

    private synchronized void stopTextActionModeWithPreservingSelection() {
        if (this.mTextActionMode != null) {
            this.mRestartActionModeOnNextRefresh = true;
        }
        this.mPreserveSelection = true;
        stopTextActionMode();
        this.mPreserveSelection = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean hasInsertionController() {
        return this.mInsertionControllerEnabled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean hasSelectionController() {
        return this.mSelectionControllerEnabled;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized InsertionPointCursorController getInsertionController() {
        if (this.mInsertionControllerEnabled) {
            if (this.mInsertionPointCursorController == null) {
                this.mInsertionPointCursorController = new InsertionPointCursorController();
                ViewTreeObserver observer = this.mTextView.getViewTreeObserver();
                observer.addOnTouchModeChangeListener(this.mInsertionPointCursorController);
            }
            return this.mInsertionPointCursorController;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized SelectionModifierCursorController getSelectionController() {
        if (!this.mSelectionControllerEnabled) {
            return null;
        }
        if (this.mSelectionModifierCursorController == null) {
            this.mSelectionModifierCursorController = new SelectionModifierCursorController();
            ViewTreeObserver observer = this.mTextView.getViewTreeObserver();
            observer.addOnTouchModeChangeListener(this.mSelectionModifierCursorController);
        }
        return this.mSelectionModifierCursorController;
    }

    @VisibleForTesting
    public synchronized Drawable getCursorDrawable() {
        return this.mDrawableForCursor;
    }

    private synchronized void updateCursorPosition(int top, int bottom, float horizontal) {
        if (this.mDrawableForCursor == null) {
            this.mDrawableForCursor = this.mTextView.getContext().getDrawable(this.mTextView.mCursorDrawableRes);
        }
        int left = clampHorizontalPosition(this.mDrawableForCursor, horizontal);
        int width = this.mDrawableForCursor.getIntrinsicWidth();
        this.mDrawableForCursor.setBounds(left, top - this.mTempRect.top, left + width, this.mTempRect.bottom + bottom);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized int clampHorizontalPosition(Drawable drawable, float horizontal) {
        float horizontal2 = Math.max(0.5f, horizontal - 0.5f);
        if (this.mTempRect == null) {
            this.mTempRect = new Rect();
        }
        int drawableWidth = 0;
        if (drawable != null) {
            drawable.getPadding(this.mTempRect);
            drawableWidth = drawable.getIntrinsicWidth();
        } else {
            this.mTempRect.setEmpty();
        }
        int scrollX = this.mTextView.getScrollX();
        float horizontalDiff = horizontal2 - scrollX;
        int viewClippedWidth = (this.mTextView.getWidth() - this.mTextView.getCompoundPaddingLeft()) - this.mTextView.getCompoundPaddingRight();
        if (horizontalDiff >= viewClippedWidth - 1.0f) {
            int left = (viewClippedWidth + scrollX) - (drawableWidth - this.mTempRect.right);
            return left;
        } else if (Math.abs(horizontalDiff) <= 1.0f || (TextUtils.isEmpty(this.mTextView.getText()) && 1048576 - scrollX <= viewClippedWidth + 1.0f && horizontal2 <= 1.0f)) {
            int left2 = scrollX - this.mTempRect.left;
            return left2;
        } else {
            int left3 = ((int) horizontal2) - this.mTempRect.left;
            return left3;
        }
    }

    public synchronized void onCommitCorrection(CorrectionInfo info) {
        if (this.mCorrectionHighlighter == null) {
            this.mCorrectionHighlighter = new CorrectionHighlighter();
        } else {
            this.mCorrectionHighlighter.invalidate(false);
        }
        this.mCorrectionHighlighter.highlight(info);
        this.mUndoInputFilter.freezeLastEdit();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onScrollChanged() {
        if (this.mPositionListener != null) {
            this.mPositionListener.onScrollChanged();
        }
        if (this.mTextActionMode != null) {
            this.mTextActionMode.invalidateContentRect();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean shouldBlink() {
        int start;
        int end;
        return isCursorVisible() && this.mTextView.isFocused() && (start = this.mTextView.getSelectionStart()) >= 0 && (end = this.mTextView.getSelectionEnd()) >= 0 && start == end;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void makeBlink() {
        if (shouldBlink()) {
            this.mShowCursor = SystemClock.uptimeMillis();
            if (this.mBlink == null) {
                this.mBlink = new Blink();
            }
            this.mTextView.removeCallbacks(this.mBlink);
            this.mTextView.postDelayed(this.mBlink, 500L);
        } else if (this.mBlink != null) {
            this.mTextView.removeCallbacks(this.mBlink);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class Blink implements Runnable {
        private boolean mCancelled;

        private Blink() {
        }

        @Override // java.lang.Runnable
        public void run() {
            if (!this.mCancelled) {
                Editor.this.mTextView.removeCallbacks(this);
                if (Editor.this.shouldBlink()) {
                    if (Editor.this.mTextView.getLayout() != null) {
                        Editor.this.mTextView.invalidateCursorPath();
                    }
                    Editor.this.mTextView.postDelayed(this, 500L);
                }
            }
        }

        synchronized void cancel() {
            if (!this.mCancelled) {
                Editor.this.mTextView.removeCallbacks(this);
                this.mCancelled = true;
            }
        }

        synchronized void uncancel() {
            this.mCancelled = false;
        }
    }

    private synchronized View.DragShadowBuilder getTextThumbnailBuilder(int start, int end) {
        TextView shadowView = (TextView) View.inflate(this.mTextView.getContext(), R.layout.text_drag_thumbnail, null);
        if (shadowView == null) {
            throw new IllegalArgumentException("Unable to inflate text drag thumbnail");
        }
        if (end - start > 20) {
            long range = getCharClusterRange(start + 20);
            end = TextUtils.unpackRangeEndFromLong(range);
        }
        CharSequence text = this.mTextView.getTransformedText(start, end);
        shadowView.setText(text);
        shadowView.setTextColor(this.mTextView.getTextColors());
        shadowView.setTextAppearance(16);
        shadowView.setGravity(17);
        shadowView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
        int size = View.MeasureSpec.makeMeasureSpec(0, 0);
        shadowView.measure(size, size);
        shadowView.layout(0, 0, shadowView.getMeasuredWidth(), shadowView.getMeasuredHeight());
        shadowView.invalidate();
        return new View.DragShadowBuilder(shadowView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class DragLocalState {
        public int end;
        public TextView sourceTextView;
        public int start;

        public synchronized DragLocalState(TextView sourceTextView, int start, int end) {
            this.sourceTextView = sourceTextView;
            this.start = start;
            this.end = end;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onDrop(DragEvent event) {
        SpannableStringBuilder content = new SpannableStringBuilder();
        DragAndDropPermissions permissions = DragAndDropPermissions.obtain(event);
        if (permissions != null) {
            permissions.takeTransient();
        }
        try {
            ClipData clipData = event.getClipData();
            int itemCount = clipData.getItemCount();
            for (int i = 0; i < itemCount; i++) {
                ClipData.Item item = clipData.getItemAt(i);
                content.append(item.coerceToStyledText(this.mTextView.getContext()));
            }
            this.mTextView.beginBatchEdit();
            this.mUndoInputFilter.freezeLastEdit();
            try {
                int offset = this.mTextView.getOffsetForPosition(event.getX(), event.getY());
                Object localState = event.getLocalState();
                DragLocalState dragLocalState = null;
                if (localState instanceof DragLocalState) {
                    dragLocalState = (DragLocalState) localState;
                }
                boolean dragDropIntoItself = dragLocalState != null && dragLocalState.sourceTextView == this.mTextView;
                if (dragDropIntoItself && offset >= dragLocalState.start && offset < dragLocalState.end) {
                    return;
                }
                int originalLength = this.mTextView.getText().length();
                Selection.setSelection((Spannable) this.mTextView.getText(), offset);
                this.mTextView.replaceText_internal(offset, offset, content);
                if (dragDropIntoItself) {
                    int dragSourceStart = dragLocalState.start;
                    int dragSourceEnd = dragLocalState.end;
                    if (offset <= dragSourceStart) {
                        int shift = this.mTextView.getText().length() - originalLength;
                        dragSourceStart += shift;
                        dragSourceEnd += shift;
                    }
                    this.mTextView.deleteText_internal(dragSourceStart, dragSourceEnd);
                    int prevCharIdx = Math.max(0, dragSourceStart - 1);
                    int nextCharIdx = Math.min(this.mTextView.getText().length(), dragSourceStart + 1);
                    if (nextCharIdx > prevCharIdx + 1) {
                        CharSequence t = this.mTextView.getTransformedText(prevCharIdx, nextCharIdx);
                        if (Character.isSpaceChar(t.charAt(0)) && Character.isSpaceChar(t.charAt(1))) {
                            this.mTextView.deleteText_internal(prevCharIdx, prevCharIdx + 1);
                        }
                    }
                }
            } finally {
                this.mTextView.endBatchEdit();
                this.mUndoInputFilter.freezeLastEdit();
            }
        } finally {
            if (permissions != null) {
                permissions.release();
            }
        }
    }

    public synchronized void addSpanWatchers(Spannable text) {
        int textLength = text.length();
        if (this.mKeyListener != null) {
            text.setSpan(this.mKeyListener, 0, textLength, 18);
        }
        if (this.mSpanController == null) {
            this.mSpanController = new SpanController();
        }
        text.setSpan(this.mSpanController, 0, textLength, 18);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setContextMenuAnchor(float x, float y) {
        this.mContextMenuAnchorX = x;
        this.mContextMenuAnchorY = y;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onCreateContextMenu(ContextMenu menu) {
        int offset;
        if (this.mIsBeingLongClicked || Float.isNaN(this.mContextMenuAnchorX) || Float.isNaN(this.mContextMenuAnchorY) || (offset = this.mTextView.getOffsetForPosition(this.mContextMenuAnchorX, this.mContextMenuAnchorY)) == -1) {
            return;
        }
        stopTextActionModeWithPreservingSelection();
        if (this.mTextView.canSelectText()) {
            boolean isOnSelection = this.mTextView.hasSelection() && offset >= this.mTextView.getSelectionStart() && offset <= this.mTextView.getSelectionEnd();
            if (!isOnSelection) {
                Selection.setSelection((Spannable) this.mTextView.getText(), offset);
                stopTextActionMode();
            }
        }
        boolean isOnSelection2 = shouldOfferToShowSuggestions();
        if (isOnSelection2) {
            SuggestionInfo[] suggestionInfoArray = new SuggestionInfo[5];
            for (int i = 0; i < suggestionInfoArray.length; i++) {
                suggestionInfoArray[i] = new SuggestionInfo();
            }
            SubMenu subMenu = menu.addSubMenu(0, 0, 9, R.string.replace);
            int numItems = this.mSuggestionHelper.getSuggestionInfo(suggestionInfoArray, null);
            for (int i2 = 0; i2 < numItems; i2++) {
                final SuggestionInfo info = suggestionInfoArray[i2];
                subMenu.add(0, 0, i2, info.mText).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() { // from class: android.widget.Editor.4
                    @Override // android.view.MenuItem.OnMenuItemClickListener
                    public boolean onMenuItemClick(MenuItem item) {
                        Editor.this.replaceWithSuggestion(info);
                        return true;
                    }
                });
            }
        }
        menu.add(0, 16908338, 2, R.string.undo).setAlphabeticShortcut('z').setOnMenuItemClickListener(this.mOnContextMenuItemClickListener).setEnabled(this.mTextView.canUndo());
        menu.add(0, android.R.id.redo, 3, 17040762).setOnMenuItemClickListener(this.mOnContextMenuItemClickListener).setEnabled(this.mTextView.canRedo());
        menu.add(0, 16908320, 4, 17039363).setAlphabeticShortcut(EpicenterTranslateClipReveal.StateProperty.TARGET_X).setOnMenuItemClickListener(this.mOnContextMenuItemClickListener).setEnabled(this.mTextView.canCut());
        menu.add(0, 16908321, 5, 17039361).setAlphabeticShortcut('c').setOnMenuItemClickListener(this.mOnContextMenuItemClickListener).setEnabled(this.mTextView.canCopy());
        menu.add(0, 16908322, 6, 17039371).setAlphabeticShortcut('v').setEnabled(this.mTextView.canPaste()).setOnMenuItemClickListener(this.mOnContextMenuItemClickListener);
        menu.add(0, 16908337, 11, 17039385).setEnabled(this.mTextView.canPasteAsPlainText()).setOnMenuItemClickListener(this.mOnContextMenuItemClickListener);
        menu.add(0, 16908341, 7, 17040865).setEnabled(this.mTextView.canShare()).setOnMenuItemClickListener(this.mOnContextMenuItemClickListener);
        menu.add(0, 16908319, 8, 17039373).setAlphabeticShortcut('a').setEnabled(this.mTextView.canSelectAllText()).setOnMenuItemClickListener(this.mOnContextMenuItemClickListener);
        menu.add(0, 16908355, 10, 17039386).setEnabled(this.mTextView.canRequestAutofill()).setOnMenuItemClickListener(this.mOnContextMenuItemClickListener);
        this.mPreserveSelection = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized SuggestionSpan findEquivalentSuggestionSpan(SuggestionSpanInfo suggestionSpanInfo) {
        Editable editable = (Editable) this.mTextView.getText();
        if (editable.getSpanStart(suggestionSpanInfo.mSuggestionSpan) >= 0) {
            return suggestionSpanInfo.mSuggestionSpan;
        }
        SuggestionSpan[] suggestionSpans = (SuggestionSpan[]) editable.getSpans(suggestionSpanInfo.mSpanStart, suggestionSpanInfo.mSpanEnd, SuggestionSpan.class);
        for (SuggestionSpan suggestionSpan : suggestionSpans) {
            int start = editable.getSpanStart(suggestionSpan);
            if (start == suggestionSpanInfo.mSpanStart) {
                int end = editable.getSpanEnd(suggestionSpan);
                if (end == suggestionSpanInfo.mSpanEnd && suggestionSpan.equals(suggestionSpanInfo.mSuggestionSpan)) {
                    return suggestionSpan;
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void replaceWithSuggestion(SuggestionInfo suggestionInfo) {
        int spanStart;
        String originalText;
        SuggestionSpan[] suggestionSpans;
        int length;
        SuggestionSpan targetSuggestionSpan = findEquivalentSuggestionSpan(suggestionInfo.mSuggestionSpanInfo);
        if (targetSuggestionSpan == null) {
            return;
        }
        Editable editable = (Editable) this.mTextView.getText();
        int spanStart2 = editable.getSpanStart(targetSuggestionSpan);
        int spanEnd = editable.getSpanEnd(targetSuggestionSpan);
        if (spanStart2 >= 0 && spanEnd > spanStart2) {
            String originalText2 = TextUtils.substring(editable, spanStart2, spanEnd);
            SuggestionSpan[] suggestionSpans2 = (SuggestionSpan[]) editable.getSpans(spanStart2, spanEnd, SuggestionSpan.class);
            int length2 = suggestionSpans2.length;
            int[] suggestionSpansStarts = new int[length2];
            int[] suggestionSpansEnds = new int[length2];
            int[] suggestionSpansFlags = new int[length2];
            for (int i = 0; i < length2; i++) {
                SuggestionSpan suggestionSpan = suggestionSpans2[i];
                suggestionSpansStarts[i] = editable.getSpanStart(suggestionSpan);
                suggestionSpansEnds[i] = editable.getSpanEnd(suggestionSpan);
                suggestionSpansFlags[i] = editable.getSpanFlags(suggestionSpan);
                int suggestionSpanFlags = suggestionSpan.getFlags();
                if ((suggestionSpanFlags & 2) != 0) {
                    suggestionSpan.setFlags(suggestionSpanFlags & (-3) & (-2));
                }
            }
            targetSuggestionSpan.notifySelection(this.mTextView.getContext(), originalText2, suggestionInfo.mSuggestionIndex);
            int suggestionStart = suggestionInfo.mSuggestionStart;
            int suggestionEnd = suggestionInfo.mSuggestionEnd;
            String suggestion = suggestionInfo.mText.subSequence(suggestionStart, suggestionEnd).toString();
            this.mTextView.replaceText_internal(spanStart2, spanEnd, suggestion);
            String[] suggestions = targetSuggestionSpan.getSuggestions();
            suggestions[suggestionInfo.mSuggestionIndex] = originalText2;
            int lengthDelta = suggestion.length() - (spanEnd - spanStart2);
            int i2 = 0;
            while (true) {
                int i3 = i2;
                if (i3 < length2) {
                    Editable editable2 = editable;
                    if (suggestionSpansStarts[i3] > spanStart2 || suggestionSpansEnds[i3] < spanEnd) {
                        spanStart = spanStart2;
                        originalText = originalText2;
                        suggestionSpans = suggestionSpans2;
                        length = length2;
                    } else {
                        spanStart = spanStart2;
                        originalText = originalText2;
                        suggestionSpans = suggestionSpans2;
                        length = length2;
                        this.mTextView.setSpan_internal(suggestionSpans2[i3], suggestionSpansStarts[i3], suggestionSpansEnds[i3] + lengthDelta, suggestionSpansFlags[i3]);
                    }
                    i2 = i3 + 1;
                    editable = editable2;
                    spanStart2 = spanStart;
                    originalText2 = originalText;
                    suggestionSpans2 = suggestionSpans;
                    length2 = length;
                } else {
                    int newCursorPosition = spanEnd + lengthDelta;
                    this.mTextView.setCursorPosition_internal(newCursorPosition, newCursorPosition);
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class SpanController implements SpanWatcher {
        private static final int DISPLAY_TIMEOUT_MS = 3000;
        private Runnable mHidePopup;
        private EasyEditPopupWindow mPopupWindow;

        private SpanController() {
        }

        private synchronized boolean isNonIntermediateSelectionSpan(Spannable text, Object span) {
            return (Selection.SELECTION_START == span || Selection.SELECTION_END == span) && (text.getSpanFlags(span) & 512) == 0;
        }

        @Override // android.text.SpanWatcher
        public void onSpanAdded(Spannable text, Object span, int start, int end) {
            if (isNonIntermediateSelectionSpan(text, span)) {
                Editor.this.sendUpdateSelection();
            } else if (span instanceof EasyEditSpan) {
                if (this.mPopupWindow == null) {
                    this.mPopupWindow = new EasyEditPopupWindow();
                    this.mHidePopup = new Runnable() { // from class: android.widget.Editor.SpanController.1
                        @Override // java.lang.Runnable
                        public void run() {
                            SpanController.this.hide();
                        }
                    };
                }
                if (this.mPopupWindow.mEasyEditSpan != null) {
                    this.mPopupWindow.mEasyEditSpan.setDeleteEnabled(false);
                }
                this.mPopupWindow.setEasyEditSpan((EasyEditSpan) span);
                this.mPopupWindow.setOnDeleteListener(new EasyEditDeleteListener() { // from class: android.widget.Editor.SpanController.2
                    @Override // android.widget.Editor.EasyEditDeleteListener
                    public void onDeleteClick(EasyEditSpan span2) {
                        Editable editable = (Editable) Editor.this.mTextView.getText();
                        int start2 = editable.getSpanStart(span2);
                        int end2 = editable.getSpanEnd(span2);
                        if (start2 >= 0 && end2 >= 0) {
                            SpanController.this.sendEasySpanNotification(1, span2);
                            Editor.this.mTextView.deleteText_internal(start2, end2);
                        }
                        editable.removeSpan(span2);
                    }
                });
                if (Editor.this.mTextView.getWindowVisibility() != 0 || Editor.this.mTextView.getLayout() == null || Editor.this.extractedTextModeWillBeStarted()) {
                    return;
                }
                this.mPopupWindow.show();
                Editor.this.mTextView.removeCallbacks(this.mHidePopup);
                Editor.this.mTextView.postDelayed(this.mHidePopup, EventManager.TimedEventPair.DEFAULT_TIMEOUT);
            }
        }

        @Override // android.text.SpanWatcher
        public void onSpanRemoved(Spannable text, Object span, int start, int end) {
            if (isNonIntermediateSelectionSpan(text, span)) {
                Editor.this.sendUpdateSelection();
            } else if (this.mPopupWindow != null && span == this.mPopupWindow.mEasyEditSpan) {
                hide();
            }
        }

        @Override // android.text.SpanWatcher
        public void onSpanChanged(Spannable text, Object span, int previousStart, int previousEnd, int newStart, int newEnd) {
            if (isNonIntermediateSelectionSpan(text, span)) {
                Editor.this.sendUpdateSelection();
            } else if (this.mPopupWindow != null && (span instanceof EasyEditSpan)) {
                EasyEditSpan easyEditSpan = (EasyEditSpan) span;
                sendEasySpanNotification(2, easyEditSpan);
                text.removeSpan(easyEditSpan);
            }
        }

        public synchronized void hide() {
            if (this.mPopupWindow != null) {
                this.mPopupWindow.hide();
                Editor.this.mTextView.removeCallbacks(this.mHidePopup);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void sendEasySpanNotification(int textChangedType, EasyEditSpan span) {
            try {
                PendingIntent pendingIntent = span.getPendingIntent();
                if (pendingIntent != null) {
                    Intent intent = new Intent();
                    intent.putExtra(EasyEditSpan.EXTRA_TEXT_CHANGED_TYPE, textChangedType);
                    pendingIntent.send(Editor.this.mTextView.getContext(), 0, intent);
                }
            } catch (PendingIntent.CanceledException e) {
                Log.w("Editor", "PendingIntent for notification cannot be sent", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class EasyEditPopupWindow extends PinnedPopupWindow implements View.OnClickListener {
        private static final int POPUP_TEXT_LAYOUT = 17367300;
        private TextView mDeleteTextView;
        private EasyEditSpan mEasyEditSpan;
        private EasyEditDeleteListener mOnDeleteListener;

        private EasyEditPopupWindow() {
            super();
        }

        @Override // android.widget.Editor.PinnedPopupWindow
        protected synchronized void createPopupWindow() {
            this.mPopupWindow = new PopupWindow(Editor.this.mTextView.getContext(), (AttributeSet) null, 16843464);
            this.mPopupWindow.setInputMethodMode(2);
            this.mPopupWindow.setClippingEnabled(true);
        }

        @Override // android.widget.Editor.PinnedPopupWindow
        protected synchronized void initContentView() {
            LinearLayout linearLayout = new LinearLayout(Editor.this.mTextView.getContext());
            linearLayout.setOrientation(0);
            this.mContentView = linearLayout;
            this.mContentView.setBackgroundResource(R.drawable.text_edit_side_paste_window);
            LayoutInflater inflater = (LayoutInflater) Editor.this.mTextView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewGroup.LayoutParams wrapContent = new ViewGroup.LayoutParams(-2, -2);
            this.mDeleteTextView = (TextView) inflater.inflate(17367300, (ViewGroup) null);
            this.mDeleteTextView.setLayoutParams(wrapContent);
            this.mDeleteTextView.setText(R.string.delete);
            this.mDeleteTextView.setOnClickListener(this);
            this.mContentView.addView(this.mDeleteTextView);
        }

        public synchronized void setEasyEditSpan(EasyEditSpan easyEditSpan) {
            this.mEasyEditSpan = easyEditSpan;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void setOnDeleteListener(EasyEditDeleteListener listener) {
            this.mOnDeleteListener = listener;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view == this.mDeleteTextView && this.mEasyEditSpan != null && this.mEasyEditSpan.isDeleteEnabled() && this.mOnDeleteListener != null) {
                this.mOnDeleteListener.onDeleteClick(this.mEasyEditSpan);
            }
        }

        @Override // android.widget.Editor.PinnedPopupWindow
        public synchronized void hide() {
            if (this.mEasyEditSpan != null) {
                this.mEasyEditSpan.setDeleteEnabled(false);
            }
            this.mOnDeleteListener = null;
            super.hide();
        }

        @Override // android.widget.Editor.PinnedPopupWindow
        protected synchronized int getTextOffset() {
            Editable editable = (Editable) Editor.this.mTextView.getText();
            return editable.getSpanEnd(this.mEasyEditSpan);
        }

        @Override // android.widget.Editor.PinnedPopupWindow
        protected synchronized int getVerticalLocalPosition(int line) {
            Layout layout = Editor.this.mTextView.getLayout();
            return layout.getLineBottomWithoutSpacing(line);
        }

        @Override // android.widget.Editor.PinnedPopupWindow
        protected synchronized int clipVertically(int positionY) {
            return positionY;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class PositionListener implements ViewTreeObserver.OnPreDrawListener {
        private static final int MAXIMUM_NUMBER_OF_LISTENERS = 7;
        private boolean[] mCanMove;
        private int mNumberOfListeners;
        private boolean mPositionHasChanged;
        private TextViewPositionListener[] mPositionListeners;
        private int mPositionX;
        private int mPositionXOnScreen;
        private int mPositionY;
        private int mPositionYOnScreen;
        private boolean mScrollHasChanged;
        final int[] mTempCoords;

        private PositionListener() {
            this.mPositionListeners = new TextViewPositionListener[7];
            this.mCanMove = new boolean[7];
            this.mPositionHasChanged = true;
            this.mTempCoords = new int[2];
        }

        public synchronized void addSubscriber(TextViewPositionListener positionListener, boolean canMove) {
            if (this.mNumberOfListeners == 0) {
                updatePosition();
                ViewTreeObserver vto = Editor.this.mTextView.getViewTreeObserver();
                vto.addOnPreDrawListener(this);
            }
            int emptySlotIndex = -1;
            for (int i = 0; i < 7; i++) {
                TextViewPositionListener listener = this.mPositionListeners[i];
                if (listener == positionListener) {
                    return;
                }
                if (emptySlotIndex < 0 && listener == null) {
                    emptySlotIndex = i;
                }
            }
            this.mPositionListeners[emptySlotIndex] = positionListener;
            this.mCanMove[emptySlotIndex] = canMove;
            this.mNumberOfListeners++;
        }

        public synchronized void removeSubscriber(TextViewPositionListener positionListener) {
            int i = 0;
            while (true) {
                if (i >= 7) {
                    break;
                } else if (this.mPositionListeners[i] != positionListener) {
                    i++;
                } else {
                    this.mPositionListeners[i] = null;
                    this.mNumberOfListeners--;
                    break;
                }
            }
            int i2 = this.mNumberOfListeners;
            if (i2 == 0) {
                ViewTreeObserver vto = Editor.this.mTextView.getViewTreeObserver();
                vto.removeOnPreDrawListener(this);
            }
        }

        public synchronized int getPositionX() {
            return this.mPositionX;
        }

        public synchronized int getPositionY() {
            return this.mPositionY;
        }

        public synchronized int getPositionXOnScreen() {
            return this.mPositionXOnScreen;
        }

        public synchronized int getPositionYOnScreen() {
            return this.mPositionYOnScreen;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            TextViewPositionListener positionListener;
            updatePosition();
            for (int i = 0; i < 7; i++) {
                if ((this.mPositionHasChanged || this.mScrollHasChanged || this.mCanMove[i]) && (positionListener = this.mPositionListeners[i]) != null) {
                    positionListener.updatePosition(this.mPositionX, this.mPositionY, this.mPositionHasChanged, this.mScrollHasChanged);
                }
            }
            this.mScrollHasChanged = false;
            return true;
        }

        private synchronized void updatePosition() {
            Editor.this.mTextView.getLocationInWindow(this.mTempCoords);
            this.mPositionHasChanged = (this.mTempCoords[0] == this.mPositionX && this.mTempCoords[1] == this.mPositionY) ? false : true;
            this.mPositionX = this.mTempCoords[0];
            this.mPositionY = this.mTempCoords[1];
            Editor.this.mTextView.getLocationOnScreen(this.mTempCoords);
            this.mPositionXOnScreen = this.mTempCoords[0];
            this.mPositionYOnScreen = this.mTempCoords[1];
        }

        public synchronized void onScrollChanged() {
            this.mScrollHasChanged = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public abstract class PinnedPopupWindow implements TextViewPositionListener {
        int mClippingLimitLeft;
        int mClippingLimitRight;
        protected ViewGroup mContentView;
        protected PopupWindow mPopupWindow;
        int mPositionX;
        int mPositionY;

        protected abstract synchronized int clipVertically(int i);

        protected abstract synchronized void createPopupWindow();

        protected abstract synchronized int getTextOffset();

        protected abstract synchronized int getVerticalLocalPosition(int i);

        protected abstract synchronized void initContentView();

        protected synchronized void setUp() {
        }

        public PinnedPopupWindow() {
            setUp();
            createPopupWindow();
            this.mPopupWindow.setWindowLayoutType(1005);
            this.mPopupWindow.setWidth(-2);
            this.mPopupWindow.setHeight(-2);
            initContentView();
            ViewGroup.LayoutParams wrapContent = new ViewGroup.LayoutParams(-2, -2);
            this.mContentView.setLayoutParams(wrapContent);
            this.mPopupWindow.setContentView(this.mContentView);
        }

        public synchronized void show() {
            Editor.this.getPositionListener().addSubscriber(this, false);
            computeLocalPosition();
            PositionListener positionListener = Editor.this.getPositionListener();
            updatePosition(positionListener.getPositionX(), positionListener.getPositionY());
        }

        protected synchronized void measureContent() {
            DisplayMetrics displayMetrics = Editor.this.mTextView.getResources().getDisplayMetrics();
            this.mContentView.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, Integer.MIN_VALUE));
        }

        private synchronized void computeLocalPosition() {
            measureContent();
            int width = this.mContentView.getMeasuredWidth();
            int offset = getTextOffset();
            this.mPositionX = (int) (Editor.this.mTextView.getLayout().getPrimaryHorizontal(offset) - (width / 2.0f));
            this.mPositionX += Editor.this.mTextView.viewportToContentHorizontalOffset();
            int line = Editor.this.mTextView.getLayout().getLineForOffset(offset);
            this.mPositionY = getVerticalLocalPosition(line);
            this.mPositionY += Editor.this.mTextView.viewportToContentVerticalOffset();
        }

        private synchronized void updatePosition(int parentPositionX, int parentPositionY) {
            int positionX = this.mPositionX + parentPositionX;
            int positionY = clipVertically(this.mPositionY + parentPositionY);
            DisplayMetrics displayMetrics = Editor.this.mTextView.getResources().getDisplayMetrics();
            int width = this.mContentView.getMeasuredWidth();
            int positionX2 = Math.max(-this.mClippingLimitLeft, Math.min((displayMetrics.widthPixels - width) + this.mClippingLimitRight, positionX));
            if (!isShowing()) {
                this.mPopupWindow.showAtLocation(Editor.this.mTextView, 0, positionX2, positionY);
            } else {
                this.mPopupWindow.update(positionX2, positionY, -1, -1);
            }
        }

        public synchronized void hide() {
            if (!isShowing()) {
                return;
            }
            this.mPopupWindow.dismiss();
            Editor.this.getPositionListener().removeSubscriber(this);
        }

        @Override // android.widget.Editor.TextViewPositionListener
        public synchronized void updatePosition(int parentPositionX, int parentPositionY, boolean parentPositionChanged, boolean parentScrolled) {
            if (isShowing() && Editor.this.isOffsetVisible(getTextOffset())) {
                if (parentScrolled) {
                    computeLocalPosition();
                }
                updatePosition(parentPositionX, parentPositionY);
                return;
            }
            hide();
        }

        public synchronized boolean isShowing() {
            return this.mPopupWindow.isShowing();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class SuggestionInfo {
        int mSuggestionEnd;
        int mSuggestionIndex;
        final SuggestionSpanInfo mSuggestionSpanInfo;
        int mSuggestionStart;
        final SpannableStringBuilder mText;

        private synchronized SuggestionInfo() {
            this.mSuggestionSpanInfo = new SuggestionSpanInfo();
            this.mText = new SpannableStringBuilder();
        }

        synchronized void clear() {
            this.mSuggestionSpanInfo.clear();
            this.mText.clear();
        }

        synchronized void setSpanInfo(SuggestionSpan span, int spanStart, int spanEnd) {
            this.mSuggestionSpanInfo.mSuggestionSpan = span;
            this.mSuggestionSpanInfo.mSpanStart = spanStart;
            this.mSuggestionSpanInfo.mSpanEnd = spanEnd;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class SuggestionSpanInfo {
        int mSpanEnd;
        int mSpanStart;
        SuggestionSpan mSuggestionSpan;

        private synchronized SuggestionSpanInfo() {
        }

        synchronized void clear() {
            this.mSuggestionSpan = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class SuggestionHelper {
        private final HashMap<SuggestionSpan, Integer> mSpansLengths;
        private final Comparator<SuggestionSpan> mSuggestionSpanComparator;

        private SuggestionHelper() {
            this.mSuggestionSpanComparator = new SuggestionSpanComparator();
            this.mSpansLengths = new HashMap<>();
        }

        /* loaded from: classes3.dex */
        private class SuggestionSpanComparator implements Comparator<SuggestionSpan> {
            private SuggestionSpanComparator() {
            }

            @Override // java.util.Comparator
            public synchronized int compare(SuggestionSpan span1, SuggestionSpan span2) {
                int flag1 = span1.getFlags();
                int flag2 = span2.getFlags();
                if (flag1 != flag2) {
                    boolean easy1 = (flag1 & 1) != 0;
                    boolean easy2 = (flag2 & 1) != 0;
                    boolean misspelled1 = (flag1 & 2) != 0;
                    boolean misspelled2 = (flag2 & 2) != 0;
                    if (easy1 && !misspelled1) {
                        return -1;
                    }
                    if (easy2 && !misspelled2) {
                        return 1;
                    }
                    if (misspelled1) {
                        return -1;
                    }
                    if (misspelled2) {
                        return 1;
                    }
                }
                return ((Integer) SuggestionHelper.this.mSpansLengths.get(span1)).intValue() - ((Integer) SuggestionHelper.this.mSpansLengths.get(span2)).intValue();
            }
        }

        private synchronized SuggestionSpan[] getSortedSuggestionSpans() {
            int pos = Editor.this.mTextView.getSelectionStart();
            Spannable spannable = (Spannable) Editor.this.mTextView.getText();
            SuggestionSpan[] suggestionSpans = (SuggestionSpan[]) spannable.getSpans(pos, pos, SuggestionSpan.class);
            this.mSpansLengths.clear();
            for (SuggestionSpan suggestionSpan : suggestionSpans) {
                int start = spannable.getSpanStart(suggestionSpan);
                int end = spannable.getSpanEnd(suggestionSpan);
                this.mSpansLengths.put(suggestionSpan, Integer.valueOf(end - start));
            }
            Arrays.sort(suggestionSpans, this.mSuggestionSpanComparator);
            this.mSpansLengths.clear();
            return suggestionSpans;
        }

        public synchronized int getSuggestionInfo(SuggestionInfo[] suggestionInfos, SuggestionSpanInfo misspelledSpanInfo) {
            Spannable spannable;
            SuggestionSpan[] suggestionSpans;
            int i;
            SuggestionSpanInfo suggestionSpanInfo = misspelledSpanInfo;
            Spannable spannable2 = (Spannable) Editor.this.mTextView.getText();
            SuggestionSpan[] suggestionSpans2 = getSortedSuggestionSpans();
            int nbSpans = suggestionSpans2.length;
            int i2 = 0;
            if (nbSpans == 0) {
                return 0;
            }
            int length = suggestionSpans2.length;
            int numberOfSuggestions = 0;
            int numberOfSuggestions2 = 0;
            while (numberOfSuggestions2 < length) {
                SuggestionSpan suggestionSpan = suggestionSpans2[numberOfSuggestions2];
                int spanStart = spannable2.getSpanStart(suggestionSpan);
                int spanEnd = spannable2.getSpanEnd(suggestionSpan);
                if (suggestionSpanInfo != null && (suggestionSpan.getFlags() & 2) != 0) {
                    suggestionSpanInfo.mSuggestionSpan = suggestionSpan;
                    suggestionSpanInfo.mSpanStart = spanStart;
                    suggestionSpanInfo.mSpanEnd = spanEnd;
                }
                String[] suggestions = suggestionSpan.getSuggestions();
                int nbSuggestions = suggestions.length;
                int numberOfSuggestions3 = numberOfSuggestions;
                int numberOfSuggestions4 = i2;
                while (numberOfSuggestions4 < nbSuggestions) {
                    String suggestion = suggestions[numberOfSuggestions4];
                    int i3 = 0;
                    while (true) {
                        int i4 = i3;
                        if (i4 < numberOfSuggestions3) {
                            SuggestionInfo otherSuggestionInfo = suggestionInfos[i4];
                            spannable = spannable2;
                            Spannable spannable3 = otherSuggestionInfo.mText;
                            if (!spannable3.toString().equals(suggestion)) {
                                suggestionSpans = suggestionSpans2;
                            } else {
                                int otherSpanStart = otherSuggestionInfo.mSuggestionSpanInfo.mSpanStart;
                                suggestionSpans = suggestionSpans2;
                                int otherSpanEnd = otherSuggestionInfo.mSuggestionSpanInfo.mSpanEnd;
                                if (spanStart == otherSpanStart && spanEnd == otherSpanEnd) {
                                    i = 0;
                                    break;
                                }
                            }
                            i3 = i4 + 1;
                            spannable2 = spannable;
                            suggestionSpans2 = suggestionSpans;
                        } else {
                            spannable = spannable2;
                            suggestionSpans = suggestionSpans2;
                            SuggestionInfo suggestionInfo = suggestionInfos[numberOfSuggestions3];
                            suggestionInfo.setSpanInfo(suggestionSpan, spanStart, spanEnd);
                            suggestionInfo.mSuggestionIndex = numberOfSuggestions4;
                            i = 0;
                            suggestionInfo.mSuggestionStart = 0;
                            suggestionInfo.mSuggestionEnd = suggestion.length();
                            suggestionInfo.mText.replace(0, suggestionInfo.mText.length(), (CharSequence) suggestion);
                            numberOfSuggestions3++;
                            if (numberOfSuggestions3 >= suggestionInfos.length) {
                                return numberOfSuggestions3;
                            }
                        }
                    }
                    numberOfSuggestions4++;
                    i2 = i;
                    spannable2 = spannable;
                    suggestionSpans2 = suggestionSpans;
                }
                numberOfSuggestions2++;
                numberOfSuggestions = numberOfSuggestions3;
                suggestionSpanInfo = misspelledSpanInfo;
            }
            return numberOfSuggestions;
        }
    }

    @VisibleForTesting
    /* loaded from: classes3.dex */
    public class SuggestionsPopupWindow extends PinnedPopupWindow implements AdapterView.OnItemClickListener {
        private static final int MAX_NUMBER_SUGGESTIONS = 5;
        private static final String USER_DICTIONARY_EXTRA_LOCALE = "locale";
        private static final String USER_DICTIONARY_EXTRA_WORD = "word";
        private TextView mAddToDictionaryButton;
        private int mContainerMarginTop;
        private int mContainerMarginWidth;
        private LinearLayout mContainerView;
        private Context mContext;
        private boolean mCursorWasVisibleBeforeSuggestions;
        private TextView mDeleteButton;
        private TextAppearanceSpan mHighlightSpan;
        private boolean mIsShowingUp;
        private final SuggestionSpanInfo mMisspelledSpanInfo;
        private int mNumberOfSuggestions;
        private SuggestionInfo[] mSuggestionInfos;
        private ListView mSuggestionListView;
        private SuggestionAdapter mSuggestionsAdapter;

        @Override // android.widget.Editor.PinnedPopupWindow
        public /* bridge */ /* synthetic */ void hide() {
            super.hide();
        }

        @Override // android.widget.Editor.PinnedPopupWindow
        public /* bridge */ /* synthetic */ boolean isShowing() {
            return super.isShowing();
        }

        @Override // android.widget.Editor.PinnedPopupWindow, android.widget.Editor.TextViewPositionListener
        public /* bridge */ /* synthetic */ void updatePosition(int i, int i2, boolean z, boolean z2) {
            super.updatePosition(i, i2, z, z2);
        }

        /* loaded from: classes3.dex */
        private class CustomPopupWindow extends PopupWindow {
            private CustomPopupWindow() {
            }

            @Override // android.widget.PopupWindow
            public void dismiss() {
                if (!isShowing()) {
                    return;
                }
                super.dismiss();
                Editor.this.getPositionListener().removeSubscriber(SuggestionsPopupWindow.this);
                ((Spannable) Editor.this.mTextView.getText()).removeSpan(Editor.this.mSuggestionRangeSpan);
                Editor.this.mTextView.setCursorVisible(SuggestionsPopupWindow.this.mCursorWasVisibleBeforeSuggestions);
                if (Editor.this.hasInsertionController() && !Editor.this.extractedTextModeWillBeStarted()) {
                    Editor.this.getInsertionController().show();
                }
            }
        }

        public SuggestionsPopupWindow() {
            super();
            this.mIsShowingUp = false;
            this.mMisspelledSpanInfo = new SuggestionSpanInfo();
            this.mCursorWasVisibleBeforeSuggestions = Editor.this.mCursorVisible;
        }

        @Override // android.widget.Editor.PinnedPopupWindow
        protected synchronized void setUp() {
            this.mContext = applyDefaultTheme(Editor.this.mTextView.getContext());
            this.mHighlightSpan = new TextAppearanceSpan(this.mContext, Editor.this.mTextView.mTextEditSuggestionHighlightStyle);
        }

        private synchronized Context applyDefaultTheme(Context originalContext) {
            TypedArray a = originalContext.obtainStyledAttributes(new int[]{R.attr.isLightTheme});
            boolean isLightTheme = a.getBoolean(0, true);
            int themeId = isLightTheme ? 16974410 : 16974411;
            a.recycle();
            return new ContextThemeWrapper(originalContext, themeId);
        }

        @Override // android.widget.Editor.PinnedPopupWindow
        protected synchronized void createPopupWindow() {
            this.mPopupWindow = new CustomPopupWindow();
            this.mPopupWindow.setInputMethodMode(2);
            this.mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
            this.mPopupWindow.setFocusable(true);
            this.mPopupWindow.setClippingEnabled(false);
        }

        @Override // android.widget.Editor.PinnedPopupWindow
        protected synchronized void initContentView() {
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.mContentView = (ViewGroup) inflater.inflate(Editor.this.mTextView.mTextEditSuggestionContainerLayout, (ViewGroup) null);
            this.mContainerView = (LinearLayout) this.mContentView.findViewById(R.id.suggestionWindowContainer);
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) this.mContainerView.getLayoutParams();
            this.mContainerMarginWidth = lp.leftMargin + lp.rightMargin;
            this.mContainerMarginTop = lp.topMargin;
            this.mClippingLimitLeft = lp.leftMargin;
            this.mClippingLimitRight = lp.rightMargin;
            this.mSuggestionListView = (ListView) this.mContentView.findViewById(R.id.suggestionContainer);
            this.mSuggestionsAdapter = new SuggestionAdapter();
            this.mSuggestionListView.setAdapter((ListAdapter) this.mSuggestionsAdapter);
            this.mSuggestionListView.setOnItemClickListener(this);
            this.mSuggestionInfos = new SuggestionInfo[5];
            for (int i = 0; i < this.mSuggestionInfos.length; i++) {
                this.mSuggestionInfos[i] = new SuggestionInfo();
            }
            this.mAddToDictionaryButton = (TextView) this.mContentView.findViewById(R.id.addToDictionaryButton);
            this.mAddToDictionaryButton.setOnClickListener(new View.OnClickListener() { // from class: android.widget.Editor.SuggestionsPopupWindow.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    SuggestionSpan misspelledSpan = Editor.this.findEquivalentSuggestionSpan(SuggestionsPopupWindow.this.mMisspelledSpanInfo);
                    if (misspelledSpan != null) {
                        Editable editable = (Editable) Editor.this.mTextView.getText();
                        int spanStart = editable.getSpanStart(misspelledSpan);
                        int spanEnd = editable.getSpanEnd(misspelledSpan);
                        if (spanStart < 0 || spanEnd <= spanStart) {
                            return;
                        }
                        String originalText = TextUtils.substring(editable, spanStart, spanEnd);
                        Intent intent = new Intent("com.android.settings.USER_DICTIONARY_INSERT");
                        intent.putExtra("word", originalText);
                        intent.putExtra("locale", Editor.this.mTextView.getTextServicesLocale().toString());
                        intent.setFlags(intent.getFlags() | 268435456);
                        Editor.this.mTextView.getContext().startActivity(intent);
                        editable.removeSpan(SuggestionsPopupWindow.this.mMisspelledSpanInfo.mSuggestionSpan);
                        Selection.setSelection(editable, spanEnd);
                        Editor.this.updateSpellCheckSpans(spanStart, spanEnd, false);
                        SuggestionsPopupWindow.this.hideWithCleanUp();
                    }
                }
            });
            this.mDeleteButton = (TextView) this.mContentView.findViewById(R.id.deleteButton);
            this.mDeleteButton.setOnClickListener(new View.OnClickListener() { // from class: android.widget.Editor.SuggestionsPopupWindow.2
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    Editable editable = (Editable) Editor.this.mTextView.getText();
                    int spanUnionStart = editable.getSpanStart(Editor.this.mSuggestionRangeSpan);
                    int spanUnionEnd = editable.getSpanEnd(Editor.this.mSuggestionRangeSpan);
                    if (spanUnionStart >= 0 && spanUnionEnd > spanUnionStart) {
                        if (spanUnionEnd < editable.length() && Character.isSpaceChar(editable.charAt(spanUnionEnd)) && (spanUnionStart == 0 || Character.isSpaceChar(editable.charAt(spanUnionStart - 1)))) {
                            spanUnionEnd++;
                        }
                        Editor.this.mTextView.deleteText_internal(spanUnionStart, spanUnionEnd);
                    }
                    SuggestionsPopupWindow.this.hideWithCleanUp();
                }
            });
        }

        public synchronized boolean isShowingUp() {
            return this.mIsShowingUp;
        }

        public synchronized void onParentLostFocus() {
            this.mIsShowingUp = false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public class SuggestionAdapter extends BaseAdapter {
            private LayoutInflater mInflater;

            private SuggestionAdapter() {
                this.mInflater = (LayoutInflater) SuggestionsPopupWindow.this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }

            @Override // android.widget.Adapter
            public int getCount() {
                return SuggestionsPopupWindow.this.mNumberOfSuggestions;
            }

            @Override // android.widget.Adapter
            public Object getItem(int position) {
                return SuggestionsPopupWindow.this.mSuggestionInfos[position];
            }

            @Override // android.widget.Adapter
            public long getItemId(int position) {
                return position;
            }

            @Override // android.widget.Adapter
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) convertView;
                if (textView == null) {
                    textView = (TextView) this.mInflater.inflate(Editor.this.mTextView.mTextEditSuggestionItemLayout, parent, false);
                }
                SuggestionInfo suggestionInfo = SuggestionsPopupWindow.this.mSuggestionInfos[position];
                textView.setText(suggestionInfo.mText);
                return textView;
            }
        }

        @VisibleForTesting
        public synchronized ViewGroup getContentViewForTesting() {
            return this.mContentView;
        }

        @Override // android.widget.Editor.PinnedPopupWindow
        public synchronized void show() {
            if (!(Editor.this.mTextView.getText() instanceof Editable) || Editor.this.extractedTextModeWillBeStarted()) {
                return;
            }
            if (updateSuggestions()) {
                this.mCursorWasVisibleBeforeSuggestions = Editor.this.mCursorVisible;
                Editor.this.mTextView.setCursorVisible(false);
                this.mIsShowingUp = true;
                super.show();
            }
            this.mSuggestionListView.setVisibility(this.mNumberOfSuggestions == 0 ? 8 : 0);
        }

        @Override // android.widget.Editor.PinnedPopupWindow
        protected synchronized void measureContent() {
            DisplayMetrics displayMetrics = Editor.this.mTextView.getResources().getDisplayMetrics();
            int horizontalMeasure = View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, Integer.MIN_VALUE);
            int verticalMeasure = View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, Integer.MIN_VALUE);
            int width = 0;
            View view = null;
            for (int i = 0; i < this.mNumberOfSuggestions; i++) {
                view = this.mSuggestionsAdapter.getView(i, view, this.mContentView);
                view.getLayoutParams().width = -2;
                view.measure(horizontalMeasure, verticalMeasure);
                width = Math.max(width, view.getMeasuredWidth());
            }
            if (this.mAddToDictionaryButton.getVisibility() != 8) {
                this.mAddToDictionaryButton.measure(horizontalMeasure, verticalMeasure);
                width = Math.max(width, this.mAddToDictionaryButton.getMeasuredWidth());
            }
            this.mDeleteButton.measure(horizontalMeasure, verticalMeasure);
            int width2 = Math.max(width, this.mDeleteButton.getMeasuredWidth()) + this.mContainerView.getPaddingLeft() + this.mContainerView.getPaddingRight() + this.mContainerMarginWidth;
            this.mContentView.measure(View.MeasureSpec.makeMeasureSpec(width2, 1073741824), verticalMeasure);
            Drawable popupBackground = this.mPopupWindow.getBackground();
            if (popupBackground != null) {
                if (Editor.this.mTempRect == null) {
                    Editor.this.mTempRect = new Rect();
                }
                popupBackground.getPadding(Editor.this.mTempRect);
                width2 += Editor.this.mTempRect.left + Editor.this.mTempRect.right;
            }
            this.mPopupWindow.setWidth(width2);
        }

        @Override // android.widget.Editor.PinnedPopupWindow
        protected synchronized int getTextOffset() {
            return (Editor.this.mTextView.getSelectionStart() + Editor.this.mTextView.getSelectionStart()) / 2;
        }

        @Override // android.widget.Editor.PinnedPopupWindow
        protected synchronized int getVerticalLocalPosition(int line) {
            Layout layout = Editor.this.mTextView.getLayout();
            return layout.getLineBottomWithoutSpacing(line) - this.mContainerMarginTop;
        }

        @Override // android.widget.Editor.PinnedPopupWindow
        protected synchronized int clipVertically(int positionY) {
            int height = this.mContentView.getMeasuredHeight();
            DisplayMetrics displayMetrics = Editor.this.mTextView.getResources().getDisplayMetrics();
            return Math.min(positionY, displayMetrics.heightPixels - height);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void hideWithCleanUp() {
            SuggestionInfo[] suggestionInfoArr;
            for (SuggestionInfo info : this.mSuggestionInfos) {
                info.clear();
            }
            this.mMisspelledSpanInfo.clear();
            hide();
        }

        private synchronized boolean updateSuggestions() {
            int underlineColor;
            Spannable spannable = (Spannable) Editor.this.mTextView.getText();
            this.mNumberOfSuggestions = Editor.this.mSuggestionHelper.getSuggestionInfo(this.mSuggestionInfos, this.mMisspelledSpanInfo);
            if (this.mNumberOfSuggestions == 0 && this.mMisspelledSpanInfo.mSuggestionSpan == null) {
                return false;
            }
            int spanUnionStart = Editor.this.mTextView.getText().length();
            int spanUnionEnd = 0;
            int spanUnionStart2 = spanUnionStart;
            for (int spanUnionStart3 = 0; spanUnionStart3 < this.mNumberOfSuggestions; spanUnionStart3++) {
                SuggestionSpanInfo spanInfo = this.mSuggestionInfos[spanUnionStart3].mSuggestionSpanInfo;
                spanUnionStart2 = Math.min(spanUnionStart2, spanInfo.mSpanStart);
                spanUnionEnd = Math.max(spanUnionEnd, spanInfo.mSpanEnd);
            }
            if (this.mMisspelledSpanInfo.mSuggestionSpan != null) {
                spanUnionStart2 = Math.min(spanUnionStart2, this.mMisspelledSpanInfo.mSpanStart);
                spanUnionEnd = Math.max(spanUnionEnd, this.mMisspelledSpanInfo.mSpanEnd);
            }
            for (int i = 0; i < this.mNumberOfSuggestions; i++) {
                highlightTextDifferences(this.mSuggestionInfos[i], spanUnionStart2, spanUnionEnd);
            }
            int addToDictionaryButtonVisibility = 8;
            if (this.mMisspelledSpanInfo.mSuggestionSpan != null && this.mMisspelledSpanInfo.mSpanStart >= 0 && this.mMisspelledSpanInfo.mSpanEnd > this.mMisspelledSpanInfo.mSpanStart) {
                addToDictionaryButtonVisibility = 0;
            }
            this.mAddToDictionaryButton.setVisibility(addToDictionaryButtonVisibility);
            if (Editor.this.mSuggestionRangeSpan == null) {
                Editor.this.mSuggestionRangeSpan = new SuggestionRangeSpan();
            }
            if (this.mNumberOfSuggestions != 0) {
                underlineColor = this.mSuggestionInfos[0].mSuggestionSpanInfo.mSuggestionSpan.getUnderlineColor();
            } else {
                underlineColor = this.mMisspelledSpanInfo.mSuggestionSpan.getUnderlineColor();
            }
            if (underlineColor == 0) {
                Editor.this.mSuggestionRangeSpan.setBackgroundColor(Editor.this.mTextView.mHighlightColor);
            } else {
                int newAlpha = (int) (Color.alpha(underlineColor) * 0.4f);
                Editor.this.mSuggestionRangeSpan.setBackgroundColor((16777215 & underlineColor) + (newAlpha << 24));
            }
            spannable.setSpan(Editor.this.mSuggestionRangeSpan, spanUnionStart2, spanUnionEnd, 33);
            this.mSuggestionsAdapter.notifyDataSetChanged();
            return true;
        }

        private synchronized void highlightTextDifferences(SuggestionInfo suggestionInfo, int unionStart, int unionEnd) {
            Spannable text = (Spannable) Editor.this.mTextView.getText();
            int spanStart = suggestionInfo.mSuggestionSpanInfo.mSpanStart;
            int spanEnd = suggestionInfo.mSuggestionSpanInfo.mSpanEnd;
            suggestionInfo.mSuggestionStart = spanStart - unionStart;
            suggestionInfo.mSuggestionEnd = suggestionInfo.mSuggestionStart + suggestionInfo.mText.length();
            suggestionInfo.mText.setSpan(this.mHighlightSpan, 0, suggestionInfo.mText.length(), 33);
            String textAsString = text.toString();
            suggestionInfo.mText.insert(0, (CharSequence) textAsString.substring(unionStart, spanStart));
            suggestionInfo.mText.append((CharSequence) textAsString.substring(spanEnd, unionEnd));
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SuggestionInfo suggestionInfo = this.mSuggestionInfos[position];
            Editor.this.replaceWithSuggestion(suggestionInfo);
            hideWithCleanUp();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class TextActionModeCallback extends ActionMode.Callback2 {
        private final int mHandleHeight;
        private final boolean mHasSelection;
        private final Path mSelectionPath = new Path();
        private final RectF mSelectionBounds = new RectF();
        private final Map<MenuItem, View.OnClickListener> mAssistClickHandlers = new HashMap();

        TextActionModeCallback(@TextActionMode int mode) {
            this.mHasSelection = mode == 0 || (Editor.this.mTextIsSelectable && mode == 2);
            if (!this.mHasSelection) {
                InsertionPointCursorController insertionController = Editor.this.getInsertionController();
                if (insertionController == null) {
                    this.mHandleHeight = 0;
                    return;
                }
                insertionController.getHandle();
                this.mHandleHeight = Editor.this.mSelectHandleCenter.getMinimumHeight();
                return;
            }
            SelectionModifierCursorController selectionController = Editor.this.getSelectionController();
            if (selectionController.mStartHandle == null) {
                selectionController.initDrawables();
                selectionController.initHandles();
                selectionController.hide();
            }
            this.mHandleHeight = Math.max(Editor.this.mSelectHandleLeft.getMinimumHeight(), Editor.this.mSelectHandleRight.getMinimumHeight());
        }

        @Override // android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            this.mAssistClickHandlers.clear();
            mode.setTitle((CharSequence) null);
            mode.setSubtitle((CharSequence) null);
            mode.setTitleOptionalHint(true);
            populateMenuWithItems(menu);
            ActionMode.Callback customCallback = getCustomCallback();
            if (customCallback == null || customCallback.onCreateActionMode(mode, menu)) {
                if (Editor.this.mTextView.canProcessText()) {
                    Editor.this.mProcessTextIntentActionsHandler.onInitializeMenu(menu);
                }
                if (this.mHasSelection && !Editor.this.mTextView.hasTransientState()) {
                    Editor.this.mTextView.setHasTransientState(true);
                }
                return true;
            }
            Selection.setSelection((Spannable) Editor.this.mTextView.getText(), Editor.this.mTextView.getSelectionEnd());
            return false;
        }

        private synchronized ActionMode.Callback getCustomCallback() {
            if (this.mHasSelection) {
                return Editor.this.mCustomSelectionActionModeCallback;
            }
            return Editor.this.mCustomInsertionActionModeCallback;
        }

        private synchronized void populateMenuWithItems(Menu menu) {
            String selected;
            if (Editor.this.mTextView.canCut()) {
                menu.add(0, 16908320, 4, 17039363).setAlphabeticShortcut(EpicenterTranslateClipReveal.StateProperty.TARGET_X).setShowAsAction(2);
            }
            if (Editor.this.mTextView.canCopy()) {
                menu.add(0, 16908321, 5, 17039361).setAlphabeticShortcut('c').setShowAsAction(2);
            }
            if (Editor.this.mTextView.canPaste()) {
                menu.add(0, 16908322, 6, 17039371).setAlphabeticShortcut('v').setShowAsAction(2);
            }
            if (Editor.this.mTextView.canShare()) {
                menu.add(0, 16908341, 7, 17040865).setShowAsAction(1);
            }
            if (Editor.this.mTextView.canRequestAutofill() && ((selected = Editor.this.mTextView.getSelectedText()) == null || selected.isEmpty())) {
                menu.add(0, 16908355, 10, 17039386).setShowAsAction(0);
            }
            if (Editor.this.mTextView.canPasteAsPlainText()) {
                menu.add(0, 16908337, 11, 17039385).setShowAsAction(1);
            }
            updateSelectAllItem(menu);
            updateReplaceItem(menu);
            updateAssistMenuItems(menu);
        }

        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            updateSelectAllItem(menu);
            updateReplaceItem(menu);
            updateAssistMenuItems(menu);
            ActionMode.Callback customCallback = getCustomCallback();
            if (customCallback != null) {
                return customCallback.onPrepareActionMode(mode, menu);
            }
            return true;
        }

        private synchronized void updateSelectAllItem(Menu menu) {
            boolean canSelectAll = Editor.this.mTextView.canSelectAllText();
            boolean selectAllItemExists = menu.findItem(16908319) != null;
            if (canSelectAll && !selectAllItemExists) {
                menu.add(0, 16908319, 8, 17039373).setShowAsAction(1);
            } else if (!canSelectAll && selectAllItemExists) {
                menu.removeItem(16908319);
            }
        }

        private synchronized void updateReplaceItem(Menu menu) {
            boolean canReplace = Editor.this.mTextView.isSuggestionsEnabled() && Editor.this.shouldOfferToShowSuggestions();
            boolean replaceItemExists = menu.findItem(16908340) != null;
            if (canReplace && !replaceItemExists) {
                menu.add(0, 16908340, 9, R.string.replace).setShowAsAction(1);
            } else if (!canReplace && replaceItemExists) {
                menu.removeItem(16908340);
            }
        }

        private synchronized void updateAssistMenuItems(Menu menu) {
            TextClassification textClassification;
            clearAssistMenuItems(menu);
            if (!shouldEnableAssistMenuItems() || (textClassification = Editor.this.getSelectionActionModeHelper().getTextClassification()) == null) {
                return;
            }
            if (!textClassification.getActions().isEmpty()) {
                addAssistMenuItem(menu, textClassification.getActions().get(0), 16908353, 0, 2).setIntent(textClassification.getIntent());
            } else if (hasLegacyAssistItem(textClassification)) {
                MenuItem item = menu.add(16908353, 16908353, 0, textClassification.getLabel()).setIcon(textClassification.getIcon()).setIntent(textClassification.getIntent());
                item.setShowAsAction(2);
                this.mAssistClickHandlers.put(item, TextClassification.createIntentOnClickListener(TextClassification.createPendingIntent(Editor.this.mTextView.getContext(), textClassification.getIntent(), createAssistMenuItemPendingIntentRequestCode())));
            }
            int count = textClassification.getActions().size();
            for (int i = 1; i < count; i++) {
                addAssistMenuItem(menu, textClassification.getActions().get(i), 0, (50 + i) - 1, 0);
            }
        }

        private synchronized MenuItem addAssistMenuItem(Menu menu, RemoteAction action, int intemId, int order, int showAsAction) {
            MenuItem item = menu.add(16908353, intemId, order, action.getTitle()).setContentDescription(action.getContentDescription());
            if (action.shouldShowIcon()) {
                item.setIcon(action.getIcon().loadDrawable(Editor.this.mTextView.getContext()));
            }
            item.setShowAsAction(showAsAction);
            this.mAssistClickHandlers.put(item, TextClassification.createIntentOnClickListener(action.getActionIntent()));
            return item;
        }

        private synchronized void clearAssistMenuItems(Menu menu) {
            int i = 0;
            while (i < menu.size()) {
                MenuItem menuItem = menu.getItem(i);
                if (menuItem.getGroupId() == 16908353) {
                    menu.removeItem(menuItem.getItemId());
                } else {
                    i++;
                }
            }
        }

        private synchronized boolean hasLegacyAssistItem(TextClassification classification) {
            return ((classification.getIcon() == null && TextUtils.isEmpty(classification.getLabel())) || (classification.getIntent() == null && classification.getOnClickListener() == null)) ? false : true;
        }

        private synchronized boolean onAssistMenuItemClicked(MenuItem assistMenuItem) {
            Intent intent;
            Preconditions.checkArgument(assistMenuItem.getGroupId() == 16908353);
            TextClassification textClassification = Editor.this.getSelectionActionModeHelper().getTextClassification();
            if (!shouldEnableAssistMenuItems() || textClassification == null) {
                return true;
            }
            View.OnClickListener onClickListener = this.mAssistClickHandlers.get(assistMenuItem);
            if (onClickListener == null && (intent = assistMenuItem.getIntent()) != null) {
                onClickListener = TextClassification.createIntentOnClickListener(TextClassification.createPendingIntent(Editor.this.mTextView.getContext(), intent, createAssistMenuItemPendingIntentRequestCode()));
            }
            if (onClickListener != null) {
                onClickListener.onClick(Editor.this.mTextView);
                Editor.this.stopTextActionMode();
            }
            return true;
        }

        private synchronized int createAssistMenuItemPendingIntentRequestCode() {
            if (Editor.this.mTextView.hasSelection()) {
                return Editor.this.mTextView.getText().subSequence(Editor.this.mTextView.getSelectionStart(), Editor.this.mTextView.getSelectionEnd()).hashCode();
            }
            return 0;
        }

        private synchronized boolean shouldEnableAssistMenuItems() {
            return Editor.this.mTextView.isDeviceProvisioned() && TextClassificationManager.getSettings(Editor.this.mTextView.getContext()).isSmartTextShareEnabled();
        }

        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Editor.this.getSelectionActionModeHelper().onSelectionAction(item.getItemId());
            if (Editor.this.mProcessTextIntentActionsHandler.performMenuItemAction(item)) {
                return true;
            }
            ActionMode.Callback customCallback = getCustomCallback();
            if (customCallback == null || !customCallback.onActionItemClicked(mode, item)) {
                if (item.getGroupId() == 16908353 && onAssistMenuItemClicked(item)) {
                    return true;
                }
                return Editor.this.mTextView.onTextContextMenuItem(item.getItemId());
            }
            return true;
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode mode) {
            Editor.this.getSelectionActionModeHelper().onDestroyActionMode();
            Editor.this.mTextActionMode = null;
            ActionMode.Callback customCallback = getCustomCallback();
            if (customCallback != null) {
                customCallback.onDestroyActionMode(mode);
            }
            if (!Editor.this.mPreserveSelection) {
                Selection.setSelection((Spannable) Editor.this.mTextView.getText(), Editor.this.mTextView.getSelectionEnd());
            }
            if (Editor.this.mSelectionModifierCursorController != null) {
                Editor.this.mSelectionModifierCursorController.hide();
            }
            this.mAssistClickHandlers.clear();
            Editor.this.mRequestingLinkActionMode = false;
        }

        @Override // android.view.ActionMode.Callback2
        public void onGetContentRect(ActionMode mode, View view, Rect outRect) {
            if (view.equals(Editor.this.mTextView) && Editor.this.mTextView.getLayout() != null) {
                if (Editor.this.mTextView.getSelectionStart() == Editor.this.mTextView.getSelectionEnd()) {
                    Layout layout = Editor.this.mTextView.getLayout();
                    int line = layout.getLineForOffset(Editor.this.mTextView.getSelectionStart());
                    float primaryHorizontal = Editor.this.clampHorizontalPosition(null, layout.getPrimaryHorizontal(Editor.this.mTextView.getSelectionStart()));
                    this.mSelectionBounds.set(primaryHorizontal, layout.getLineTop(line), primaryHorizontal, layout.getLineBottom(line) + this.mHandleHeight);
                } else {
                    this.mSelectionPath.reset();
                    Editor.this.mTextView.getLayout().getSelectionPath(Editor.this.mTextView.getSelectionStart(), Editor.this.mTextView.getSelectionEnd(), this.mSelectionPath);
                    this.mSelectionPath.computeBounds(this.mSelectionBounds, true);
                    this.mSelectionBounds.bottom += this.mHandleHeight;
                }
                int textHorizontalOffset = Editor.this.mTextView.viewportToContentHorizontalOffset();
                int textVerticalOffset = Editor.this.mTextView.viewportToContentVerticalOffset();
                outRect.set((int) Math.floor(this.mSelectionBounds.left + textHorizontalOffset), (int) Math.floor(this.mSelectionBounds.top + textVerticalOffset), (int) Math.ceil(this.mSelectionBounds.right + textHorizontalOffset), (int) Math.ceil(this.mSelectionBounds.bottom + textVerticalOffset));
                return;
            }
            super.onGetContentRect(mode, view, outRect);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public final class CursorAnchorInfoNotifier implements TextViewPositionListener {
        final CursorAnchorInfo.Builder mSelectionInfoBuilder;
        final int[] mTmpIntOffset;
        final Matrix mViewToScreenMatrix;

        private CursorAnchorInfoNotifier() {
            this.mSelectionInfoBuilder = new CursorAnchorInfo.Builder();
            this.mTmpIntOffset = new int[2];
            this.mViewToScreenMatrix = new Matrix();
        }

        @Override // android.widget.Editor.TextViewPositionListener
        public synchronized void updatePosition(int parentPositionX, int parentPositionY, boolean parentPositionChanged, boolean parentScrolled) {
            InputMethodManager imm;
            Layout layout;
            InputMethodState ims = Editor.this.mInputMethodState;
            if (ims == null || ims.mBatchEditNesting > 0 || (imm = InputMethodManager.peekInstance()) == null || !imm.isActive(Editor.this.mTextView) || !imm.isCursorAnchorInfoEnabled() || (layout = Editor.this.mTextView.getLayout()) == null) {
                return;
            }
            CursorAnchorInfo.Builder builder = this.mSelectionInfoBuilder;
            builder.reset();
            int selectionStart = Editor.this.mTextView.getSelectionStart();
            builder.setSelectionRange(selectionStart, Editor.this.mTextView.getSelectionEnd());
            this.mViewToScreenMatrix.set(Editor.this.mTextView.getMatrix());
            Editor.this.mTextView.getLocationOnScreen(this.mTmpIntOffset);
            boolean z = false;
            this.mViewToScreenMatrix.postTranslate(this.mTmpIntOffset[0], this.mTmpIntOffset[1]);
            builder.setMatrix(this.mViewToScreenMatrix);
            float viewportToContentHorizontalOffset = Editor.this.mTextView.viewportToContentHorizontalOffset();
            float viewportToContentVerticalOffset = Editor.this.mTextView.viewportToContentVerticalOffset();
            CharSequence text = Editor.this.mTextView.getText();
            if (text instanceof Spannable) {
                Spannable sp = (Spannable) text;
                int composingTextStart = EditableInputConnection.getComposingSpanStart(sp);
                int composingTextEnd = EditableInputConnection.getComposingSpanEnd(sp);
                if (composingTextEnd < composingTextStart) {
                    composingTextEnd = composingTextStart;
                    composingTextStart = composingTextEnd;
                }
                int composingTextStart2 = composingTextStart;
                int composingTextEnd2 = composingTextEnd;
                if (composingTextStart2 >= 0 && composingTextStart2 < composingTextEnd2) {
                    z = true;
                }
                boolean hasComposingText = z;
                if (hasComposingText) {
                    CharSequence composingText = text.subSequence(composingTextStart2, composingTextEnd2);
                    builder.setComposingText(composingTextStart2, composingText);
                    Editor.this.mTextView.populateCharacterBounds(builder, composingTextStart2, composingTextEnd2, viewportToContentHorizontalOffset, viewportToContentVerticalOffset);
                }
            }
            if (selectionStart >= 0) {
                int line = layout.getLineForOffset(selectionStart);
                float insertionMarkerX = layout.getPrimaryHorizontal(selectionStart) + viewportToContentHorizontalOffset;
                float insertionMarkerTop = layout.getLineTop(line) + viewportToContentVerticalOffset;
                float insertionMarkerBaseline = layout.getLineBaseline(line) + viewportToContentVerticalOffset;
                float insertionMarkerBottom = layout.getLineBottomWithoutSpacing(line) + viewportToContentVerticalOffset;
                boolean isTopVisible = Editor.this.mTextView.isPositionVisible(insertionMarkerX, insertionMarkerTop);
                boolean isBottomVisible = Editor.this.mTextView.isPositionVisible(insertionMarkerX, insertionMarkerBottom);
                int insertionMarkerFlags = 0;
                if (isTopVisible || isBottomVisible) {
                    insertionMarkerFlags = 0 | 1;
                }
                if (!isTopVisible || !isBottomVisible) {
                    insertionMarkerFlags |= 2;
                }
                if (layout.isRtlCharAt(selectionStart)) {
                    insertionMarkerFlags |= 4;
                }
                builder.setInsertionMarkerLocation(insertionMarkerX, insertionMarkerTop, insertionMarkerBaseline, insertionMarkerBottom, insertionMarkerFlags);
            }
            imm.updateCursorAnchorInfo(Editor.this.mTextView, builder.build());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class MagnifierMotionAnimator {
        private static final long DURATION = 100;
        private float mAnimationCurrentX;
        private float mAnimationCurrentY;
        private float mAnimationStartX;
        private float mAnimationStartY;
        private final ValueAnimator mAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        private float mLastX;
        private float mLastY;
        private final Magnifier mMagnifier;
        private boolean mMagnifierIsShowing;

        private synchronized MagnifierMotionAnimator(Magnifier magnifier) {
            this.mMagnifier = magnifier;
            this.mAnimator.setDuration(100L);
            this.mAnimator.setInterpolator(new LinearInterpolator());
            this.mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: android.widget.-$$Lambda$Editor$MagnifierMotionAnimator$E-RaelOMgCHAzvKgSSZE-hDYeIg
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Editor.MagnifierMotionAnimator.lambda$new$0(Editor.MagnifierMotionAnimator.this, valueAnimator);
                }
            });
        }

        public static /* synthetic */ void lambda$new$0(MagnifierMotionAnimator magnifierMotionAnimator, ValueAnimator animation) {
            magnifierMotionAnimator.mAnimationCurrentX = magnifierMotionAnimator.mAnimationStartX + ((magnifierMotionAnimator.mLastX - magnifierMotionAnimator.mAnimationStartX) * animation.getAnimatedFraction());
            magnifierMotionAnimator.mAnimationCurrentY = magnifierMotionAnimator.mAnimationStartY + ((magnifierMotionAnimator.mLastY - magnifierMotionAnimator.mAnimationStartY) * animation.getAnimatedFraction());
            magnifierMotionAnimator.mMagnifier.show(magnifierMotionAnimator.mAnimationCurrentX, magnifierMotionAnimator.mAnimationCurrentY);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void show(float x, float y) {
            boolean startNewAnimation = this.mMagnifierIsShowing && y != this.mLastY;
            if (startNewAnimation) {
                if (this.mAnimator.isRunning()) {
                    this.mAnimator.cancel();
                    this.mAnimationStartX = this.mAnimationCurrentX;
                    this.mAnimationStartY = this.mAnimationCurrentY;
                } else {
                    this.mAnimationStartX = this.mLastX;
                    this.mAnimationStartY = this.mLastY;
                }
                this.mAnimator.start();
            } else if (!this.mAnimator.isRunning()) {
                this.mMagnifier.show(x, y);
            }
            this.mLastX = x;
            this.mLastY = y;
            this.mMagnifierIsShowing = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void update() {
            this.mMagnifier.update();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void dismiss() {
            this.mMagnifier.dismiss();
            this.mAnimator.cancel();
            this.mMagnifierIsShowing = false;
        }
    }

    @VisibleForTesting
    /* loaded from: classes3.dex */
    public abstract class HandleView extends View implements TextViewPositionListener {
        private static final int HISTORY_SIZE = 5;
        private static final int TOUCH_UP_FILTER_DELAY_AFTER = 150;
        private static final int TOUCH_UP_FILTER_DELAY_BEFORE = 350;
        private final PopupWindow mContainer;
        protected Drawable mDrawable;
        protected Drawable mDrawableLtr;
        protected Drawable mDrawableRtl;
        private final Magnifier.Callback mHandlesVisibilityCallback;
        protected int mHorizontalGravity;
        protected int mHotspotX;
        private float mIdealVerticalOffset;
        private boolean mIsDragging;
        private int mLastParentX;
        private int mLastParentXOnScreen;
        private int mLastParentY;
        private int mLastParentYOnScreen;
        private int mMinSize;
        private int mNumberPreviousOffsets;
        private boolean mPositionHasChanged;
        private int mPositionX;
        private int mPositionY;
        protected int mPrevLine;
        protected int mPreviousLineTouched;
        protected int mPreviousOffset;
        private int mPreviousOffsetIndex;
        private final int[] mPreviousOffsets;
        private final long[] mPreviousOffsetsTimes;
        private float mTouchOffsetY;
        private float mTouchToWindowOffsetX;
        private float mTouchToWindowOffsetY;

        public abstract synchronized int getCurrentCursorOffset();

        protected abstract synchronized int getHorizontalGravity(boolean z);

        protected abstract synchronized int getHotspotX(Drawable drawable, boolean z);

        protected abstract synchronized int getMagnifierHandleTrigger();

        protected abstract synchronized void updatePosition(float f, float f2, boolean z);

        protected abstract synchronized void updateSelection(int i);

        private HandleView(Drawable drawableLtr, Drawable drawableRtl, int id) {
            super(Editor.this.mTextView.getContext());
            this.mPreviousOffset = -1;
            this.mPositionHasChanged = true;
            this.mPrevLine = -1;
            this.mPreviousLineTouched = -1;
            this.mPreviousOffsetsTimes = new long[5];
            this.mPreviousOffsets = new int[5];
            this.mPreviousOffsetIndex = 0;
            this.mNumberPreviousOffsets = 0;
            this.mHandlesVisibilityCallback = new Magnifier.Callback() { // from class: android.widget.Editor.HandleView.1
                @Override // android.widget.Magnifier.Callback
                public void onOperationComplete() {
                    Point magnifierTopLeft = Editor.this.mMagnifierAnimator.mMagnifier.getWindowCoords();
                    if (magnifierTopLeft == null) {
                        return;
                    }
                    Rect magnifierRect = new Rect(magnifierTopLeft.x, magnifierTopLeft.y, magnifierTopLeft.x + Editor.this.mMagnifierAnimator.mMagnifier.getWidth(), magnifierTopLeft.y + Editor.this.mMagnifierAnimator.mMagnifier.getHeight());
                    HandleView.this.setVisible(!HandleView.this.handleOverlapsMagnifier(HandleView.this, magnifierRect));
                    HandleView otherHandle = HandleView.this.getOtherSelectionHandle();
                    if (otherHandle != null) {
                        otherHandle.setVisible(!HandleView.this.handleOverlapsMagnifier(otherHandle, magnifierRect));
                    }
                }
            };
            setId(id);
            this.mContainer = new PopupWindow(Editor.this.mTextView.getContext(), (AttributeSet) null, 16843464);
            this.mContainer.setSplitTouchEnabled(true);
            this.mContainer.setClippingEnabled(false);
            this.mContainer.setWindowLayoutType(1002);
            this.mContainer.setWidth(-2);
            this.mContainer.setHeight(-2);
            this.mContainer.setContentView(this);
            this.mDrawableLtr = drawableLtr;
            this.mDrawableRtl = drawableRtl;
            this.mMinSize = Editor.this.mTextView.getContext().getResources().getDimensionPixelSize(R.dimen.text_handle_min_size);
            updateDrawable();
            int handleHeight = getPreferredHeight();
            this.mTouchOffsetY = (-0.3f) * handleHeight;
            this.mIdealVerticalOffset = 0.7f * handleHeight;
        }

        public synchronized float getIdealVerticalOffset() {
            return this.mIdealVerticalOffset;
        }

        protected synchronized void updateDrawable() {
            Layout layout;
            if (this.mIsDragging || (layout = Editor.this.mTextView.getLayout()) == null) {
                return;
            }
            int offset = getCurrentCursorOffset();
            boolean isRtlCharAtOffset = isAtRtlRun(layout, offset);
            Drawable oldDrawable = this.mDrawable;
            this.mDrawable = isRtlCharAtOffset ? this.mDrawableRtl : this.mDrawableLtr;
            this.mHotspotX = getHotspotX(this.mDrawable, isRtlCharAtOffset);
            this.mHorizontalGravity = getHorizontalGravity(isRtlCharAtOffset);
            if (oldDrawable != this.mDrawable && isShowing()) {
                this.mPositionX = ((getCursorHorizontalPosition(layout, offset) - this.mHotspotX) - getHorizontalOffset()) + getCursorOffset();
                this.mPositionX += Editor.this.mTextView.viewportToContentHorizontalOffset();
                this.mPositionHasChanged = true;
                updatePosition(this.mLastParentX, this.mLastParentY, false, false);
                postInvalidate();
            }
        }

        private synchronized void startTouchUpFilter(int offset) {
            this.mNumberPreviousOffsets = 0;
            addPositionToTouchUpFilter(offset);
        }

        private synchronized void addPositionToTouchUpFilter(int offset) {
            this.mPreviousOffsetIndex = (this.mPreviousOffsetIndex + 1) % 5;
            this.mPreviousOffsets[this.mPreviousOffsetIndex] = offset;
            this.mPreviousOffsetsTimes[this.mPreviousOffsetIndex] = SystemClock.uptimeMillis();
            this.mNumberPreviousOffsets++;
        }

        private synchronized void filterOnTouchUp(boolean fromTouchScreen) {
            long now = SystemClock.uptimeMillis();
            int i = 0;
            int index = this.mPreviousOffsetIndex;
            int iMax = Math.min(this.mNumberPreviousOffsets, 5);
            while (i < iMax && now - this.mPreviousOffsetsTimes[index] < 150) {
                i++;
                index = ((this.mPreviousOffsetIndex - i) + 5) % 5;
            }
            if (i > 0 && i < iMax && now - this.mPreviousOffsetsTimes[index] > 350) {
                positionAtCursorOffset(this.mPreviousOffsets[index], false, fromTouchScreen);
            }
        }

        public synchronized boolean offsetHasBeenChanged() {
            return this.mNumberPreviousOffsets > 1;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.view.View
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(getPreferredWidth(), getPreferredHeight());
        }

        @Override // android.view.View
        public void invalidate() {
            super.invalidate();
            if (isShowing()) {
                positionAtCursorOffset(getCurrentCursorOffset(), true, false);
            }
        }

        private synchronized int getPreferredWidth() {
            return Math.max(this.mDrawable.getIntrinsicWidth(), this.mMinSize);
        }

        private synchronized int getPreferredHeight() {
            return Math.max(this.mDrawable.getIntrinsicHeight(), this.mMinSize);
        }

        public synchronized void show() {
            if (isShowing()) {
                return;
            }
            Editor.this.getPositionListener().addSubscriber(this, true);
            this.mPreviousOffset = -1;
            positionAtCursorOffset(getCurrentCursorOffset(), false, false);
        }

        protected synchronized void dismiss() {
            this.mIsDragging = false;
            this.mContainer.dismiss();
            onDetached();
        }

        public synchronized void hide() {
            dismiss();
            Editor.this.getPositionListener().removeSubscriber(this);
        }

        public synchronized boolean isShowing() {
            return this.mContainer.isShowing();
        }

        private synchronized boolean shouldShow() {
            if (!this.mIsDragging) {
                if (Editor.this.mTextView.isInBatchEditMode()) {
                    return false;
                }
                return Editor.this.mTextView.isPositionVisible(this.mPositionX + this.mHotspotX + getHorizontalOffset(), this.mPositionY);
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void setVisible(boolean visible) {
            this.mContainer.getContentView().setVisibility(visible ? 0 : 4);
        }

        protected synchronized boolean isAtRtlRun(Layout layout, int offset) {
            return layout.isRtlCharAt(offset);
        }

        @VisibleForTesting
        public synchronized float getHorizontal(Layout layout, int offset) {
            return layout.getPrimaryHorizontal(offset);
        }

        protected synchronized int getOffsetAtCoordinate(Layout layout, int line, float x) {
            return Editor.this.mTextView.getOffsetAtCoordinate(line, x);
        }

        protected synchronized void positionAtCursorOffset(int offset, boolean forceUpdatePosition, boolean fromTouchScreen) {
            if (Editor.this.mTextView.getLayout() != null) {
                Layout layout = Editor.this.mTextView.getLayout();
                boolean offsetChanged = offset != this.mPreviousOffset;
                if (offsetChanged || forceUpdatePosition) {
                    if (offsetChanged) {
                        updateSelection(offset);
                        if (fromTouchScreen && Editor.this.mHapticTextHandleEnabled) {
                            Editor.this.mTextView.performHapticFeedback(9);
                        }
                        addPositionToTouchUpFilter(offset);
                    }
                    int line = layout.getLineForOffset(offset);
                    this.mPrevLine = line;
                    this.mPositionX = ((getCursorHorizontalPosition(layout, offset) - this.mHotspotX) - getHorizontalOffset()) + getCursorOffset();
                    this.mPositionY = layout.getLineBottomWithoutSpacing(line);
                    this.mPositionX += Editor.this.mTextView.viewportToContentHorizontalOffset();
                    this.mPositionY += Editor.this.mTextView.viewportToContentVerticalOffset();
                    this.mPreviousOffset = offset;
                    this.mPositionHasChanged = true;
                    return;
                }
                return;
            }
            Editor.this.prepareCursorControllers();
        }

        synchronized int getCursorHorizontalPosition(Layout layout, int offset) {
            return (int) (getHorizontal(layout, offset) - 0.5f);
        }

        @Override // android.widget.Editor.TextViewPositionListener
        public synchronized void updatePosition(int parentPositionX, int parentPositionY, boolean parentPositionChanged, boolean parentScrolled) {
            positionAtCursorOffset(getCurrentCursorOffset(), parentScrolled, false);
            if (parentPositionChanged || this.mPositionHasChanged) {
                if (this.mIsDragging) {
                    if (parentPositionX != this.mLastParentX || parentPositionY != this.mLastParentY) {
                        this.mTouchToWindowOffsetX += parentPositionX - this.mLastParentX;
                        this.mTouchToWindowOffsetY += parentPositionY - this.mLastParentY;
                        this.mLastParentX = parentPositionX;
                        this.mLastParentY = parentPositionY;
                    }
                    onHandleMoved();
                }
                if (shouldShow()) {
                    int[] pts = {this.mPositionX + this.mHotspotX + getHorizontalOffset(), this.mPositionY};
                    Editor.this.mTextView.transformFromViewToWindowSpace(pts);
                    pts[0] = pts[0] - (this.mHotspotX + getHorizontalOffset());
                    if (!isShowing()) {
                        this.mContainer.showAtLocation(Editor.this.mTextView, 0, pts[0], pts[1]);
                    } else {
                        this.mContainer.update(pts[0], pts[1], -1, -1);
                    }
                } else if (isShowing()) {
                    dismiss();
                }
                this.mPositionHasChanged = false;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.view.View
        public void onDraw(Canvas c) {
            int drawWidth = this.mDrawable.getIntrinsicWidth();
            int left = getHorizontalOffset();
            this.mDrawable.setBounds(left, 0, left + drawWidth, this.mDrawable.getIntrinsicHeight());
            this.mDrawable.draw(c);
        }

        private synchronized int getHorizontalOffset() {
            int width = getPreferredWidth();
            int drawWidth = this.mDrawable.getIntrinsicWidth();
            int i = this.mHorizontalGravity;
            if (i != 3) {
                if (i != 5) {
                    int left = (width - drawWidth) / 2;
                    return left;
                }
                int left2 = width - drawWidth;
                return left2;
            }
            return 0;
        }

        protected synchronized int getCursorOffset() {
            return 0;
        }

        private synchronized boolean tooLargeTextForMagnifier() {
            float magnifierContentHeight = Math.round(Editor.this.mMagnifierAnimator.mMagnifier.getHeight() / Editor.this.mMagnifierAnimator.mMagnifier.getZoom());
            Paint.FontMetrics fontMetrics = Editor.this.mTextView.getPaint().getFontMetrics();
            float glyphHeight = fontMetrics.descent - fontMetrics.ascent;
            return glyphHeight > magnifierContentHeight;
        }

        /* JADX WARN: Removed duplicated region for block: B:30:0x00d5  */
        /* JADX WARN: Removed duplicated region for block: B:38:0x00fe  */
        /* JADX WARN: Removed duplicated region for block: B:47:0x0152  */
        /* JADX WARN: Removed duplicated region for block: B:52:0x01a2 A[ADDED_TO_REGION, ORIG_RETURN, RETURN] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private synchronized boolean obtainMagnifierShowCoordinates(android.view.MotionEvent r19, android.graphics.PointF r20) {
            /*
                Method dump skipped, instructions count: 430
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: android.widget.Editor.HandleView.obtainMagnifierShowCoordinates(android.view.MotionEvent, android.graphics.PointF):boolean");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized boolean handleOverlapsMagnifier(HandleView handle, Rect magnifierRect) {
            PopupWindow window = handle.mContainer;
            if (!window.hasDecorView()) {
                return false;
            }
            Rect handleRect = new Rect(window.getDecorViewLayoutParams().x, window.getDecorViewLayoutParams().y, window.getDecorViewLayoutParams().x + window.getContentView().getWidth(), window.getDecorViewLayoutParams().y + window.getContentView().getHeight());
            return Rect.intersects(handleRect, magnifierRect);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized HandleView getOtherSelectionHandle() {
            SelectionModifierCursorController controller = Editor.this.getSelectionController();
            if (controller == null || !controller.isActive()) {
                return null;
            }
            return controller.mStartHandle != this ? controller.mStartHandle : controller.mEndHandle;
        }

        protected final synchronized void updateMagnifier(MotionEvent event) {
            if (Editor.this.mMagnifierAnimator == null) {
                return;
            }
            PointF showPosInView = new PointF();
            boolean shouldShow = !tooLargeTextForMagnifier() && obtainMagnifierShowCoordinates(event, showPosInView);
            if (shouldShow) {
                Editor.this.mRenderCursorRegardlessTiming = true;
                Editor.this.mTextView.invalidateCursorPath();
                Editor.this.suspendBlink();
                Editor.this.mMagnifierAnimator.mMagnifier.setOnOperationCompleteCallback(this.mHandlesVisibilityCallback);
                Editor.this.mMagnifierAnimator.show(showPosInView.x, showPosInView.y);
                return;
            }
            dismissMagnifier();
        }

        protected final synchronized void dismissMagnifier() {
            if (Editor.this.mMagnifierAnimator != null) {
                Editor.this.mMagnifierAnimator.dismiss();
                Editor.this.mRenderCursorRegardlessTiming = false;
                Editor.this.resumeBlink();
                setVisible(true);
                HandleView otherHandle = getOtherSelectionHandle();
                if (otherHandle != null) {
                    otherHandle.setVisible(true);
                }
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent ev) {
            float newVerticalOffset;
            Editor.this.updateFloatingToolbarVisibility(ev);
            switch (ev.getActionMasked()) {
                case 0:
                    startTouchUpFilter(getCurrentCursorOffset());
                    PositionListener positionListener = Editor.this.getPositionListener();
                    this.mLastParentX = positionListener.getPositionX();
                    this.mLastParentY = positionListener.getPositionY();
                    this.mLastParentXOnScreen = positionListener.getPositionXOnScreen();
                    this.mLastParentYOnScreen = positionListener.getPositionYOnScreen();
                    float xInWindow = (ev.getRawX() - this.mLastParentXOnScreen) + this.mLastParentX;
                    float yInWindow = (ev.getRawY() - this.mLastParentYOnScreen) + this.mLastParentY;
                    this.mTouchToWindowOffsetX = xInWindow - this.mPositionX;
                    this.mTouchToWindowOffsetY = yInWindow - this.mPositionY;
                    this.mIsDragging = true;
                    this.mPreviousLineTouched = -1;
                    break;
                case 1:
                    filterOnTouchUp(ev.isFromSource(4098));
                    this.mIsDragging = false;
                    updateDrawable();
                    break;
                case 2:
                    float xInWindow2 = (ev.getRawX() - this.mLastParentXOnScreen) + this.mLastParentX;
                    float yInWindow2 = (ev.getRawY() - this.mLastParentYOnScreen) + this.mLastParentY;
                    float previousVerticalOffset = this.mTouchToWindowOffsetY - this.mLastParentY;
                    float currentVerticalOffset = (yInWindow2 - this.mPositionY) - this.mLastParentY;
                    if (previousVerticalOffset < this.mIdealVerticalOffset) {
                        float newVerticalOffset2 = Math.min(currentVerticalOffset, this.mIdealVerticalOffset);
                        newVerticalOffset = Math.max(newVerticalOffset2, previousVerticalOffset);
                    } else {
                        float newVerticalOffset3 = this.mIdealVerticalOffset;
                        newVerticalOffset = Math.min(Math.max(currentVerticalOffset, newVerticalOffset3), previousVerticalOffset);
                    }
                    this.mTouchToWindowOffsetY = this.mLastParentY + newVerticalOffset;
                    float newPosX = (xInWindow2 - this.mTouchToWindowOffsetX) + this.mHotspotX + getHorizontalOffset();
                    float newPosY = (yInWindow2 - this.mTouchToWindowOffsetY) + this.mTouchOffsetY;
                    updatePosition(newPosX, newPosY, ev.isFromSource(4098));
                    break;
                case 3:
                    this.mIsDragging = false;
                    updateDrawable();
                    break;
            }
            return true;
        }

        public synchronized boolean isDragging() {
            return this.mIsDragging;
        }

        synchronized void onHandleMoved() {
        }

        public synchronized void onDetached() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class InsertionHandleView extends HandleView {
        private static final int DELAY_BEFORE_HANDLE_FADES_OUT = 4000;
        private static final int RECENT_CUT_COPY_DURATION = 15000;
        private float mDownPositionX;
        private float mDownPositionY;
        private Runnable mHider;

        public InsertionHandleView(Drawable drawable) {
            super(drawable, drawable, R.id.insertion_handle);
        }

        @Override // android.widget.Editor.HandleView
        public synchronized void show() {
            super.show();
            long durationSinceCutOrCopy = SystemClock.uptimeMillis() - TextView.sLastCutCopyOrTextChangedTime;
            if (Editor.this.mInsertionActionModeRunnable != null && (Editor.this.mTapState == 2 || Editor.this.mTapState == 3 || Editor.this.isCursorInsideEasyCorrectionSpan())) {
                Editor.this.mTextView.removeCallbacks(Editor.this.mInsertionActionModeRunnable);
            }
            if (Editor.this.mTapState != 2 && Editor.this.mTapState != 3 && !Editor.this.isCursorInsideEasyCorrectionSpan() && durationSinceCutOrCopy < 15000 && Editor.this.mTextActionMode == null) {
                if (Editor.this.mInsertionActionModeRunnable == null) {
                    Editor.this.mInsertionActionModeRunnable = new Runnable() { // from class: android.widget.Editor.InsertionHandleView.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Editor.this.startInsertionActionMode();
                        }
                    };
                }
                Editor.this.mTextView.postDelayed(Editor.this.mInsertionActionModeRunnable, ViewConfiguration.getDoubleTapTimeout() + 1);
            }
            hideAfterDelay();
        }

        private synchronized void hideAfterDelay() {
            if (this.mHider == null) {
                this.mHider = new Runnable() { // from class: android.widget.Editor.InsertionHandleView.2
                    @Override // java.lang.Runnable
                    public void run() {
                        InsertionHandleView.this.hide();
                    }
                };
            } else {
                removeHiderCallback();
            }
            Editor.this.mTextView.postDelayed(this.mHider, xpWindowManager.TOAST_DURATION_LONG);
        }

        private synchronized void removeHiderCallback() {
            if (this.mHider != null) {
                Editor.this.mTextView.removeCallbacks(this.mHider);
            }
        }

        @Override // android.widget.Editor.HandleView
        protected synchronized int getHotspotX(Drawable drawable, boolean isRtlRun) {
            return drawable.getIntrinsicWidth() / 2;
        }

        @Override // android.widget.Editor.HandleView
        protected synchronized int getHorizontalGravity(boolean isRtlRun) {
            return 1;
        }

        @Override // android.widget.Editor.HandleView
        protected synchronized int getCursorOffset() {
            int offset = super.getCursorOffset();
            if (Editor.this.mDrawableForCursor != null) {
                Editor.this.mDrawableForCursor.getPadding(Editor.this.mTempRect);
                return offset + (((Editor.this.mDrawableForCursor.getIntrinsicWidth() - Editor.this.mTempRect.left) - Editor.this.mTempRect.right) / 2);
            }
            return offset;
        }

        @Override // android.widget.Editor.HandleView
        synchronized int getCursorHorizontalPosition(Layout layout, int offset) {
            if (Editor.this.mDrawableForCursor != null) {
                float horizontal = getHorizontal(layout, offset);
                return Editor.this.clampHorizontalPosition(Editor.this.mDrawableForCursor, horizontal) + Editor.this.mTempRect.left;
            }
            return super.getCursorHorizontalPosition(layout, offset);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // android.widget.Editor.HandleView, android.view.View
        public boolean onTouchEvent(MotionEvent ev) {
            boolean result = super.onTouchEvent(ev);
            switch (ev.getActionMasked()) {
                case 0:
                    this.mDownPositionX = ev.getRawX();
                    this.mDownPositionY = ev.getRawY();
                    updateMagnifier(ev);
                    break;
                case 1:
                    if (offsetHasBeenChanged()) {
                        if (Editor.this.mTextActionMode != null) {
                            Editor.this.mTextActionMode.invalidateContentRect();
                        }
                    } else {
                        float deltaX = this.mDownPositionX - ev.getRawX();
                        float deltaY = this.mDownPositionY - ev.getRawY();
                        float distanceSquared = (deltaX * deltaX) + (deltaY * deltaY);
                        ViewConfiguration viewConfiguration = ViewConfiguration.get(Editor.this.mTextView.getContext());
                        int touchSlop = viewConfiguration.getScaledTouchSlop();
                        if (distanceSquared < touchSlop * touchSlop) {
                            if (Editor.this.mTextActionMode != null) {
                                Editor.this.stopTextActionMode();
                            } else {
                                Editor.this.startInsertionActionMode();
                            }
                        }
                    }
                    hideAfterDelay();
                    dismissMagnifier();
                    break;
                case 2:
                    updateMagnifier(ev);
                    break;
                case 3:
                    hideAfterDelay();
                    dismissMagnifier();
                    break;
            }
            return result;
        }

        @Override // android.widget.Editor.HandleView
        public synchronized int getCurrentCursorOffset() {
            return Editor.this.mTextView.getSelectionStart();
        }

        @Override // android.widget.Editor.HandleView
        public synchronized void updateSelection(int offset) {
            Selection.setSelection((Spannable) Editor.this.mTextView.getText(), offset);
        }

        @Override // android.widget.Editor.HandleView
        protected synchronized void updatePosition(float x, float y, boolean fromTouchScreen) {
            Layout layout = Editor.this.mTextView.getLayout();
            int offset = -1;
            if (layout != null) {
                if (this.mPreviousLineTouched == -1) {
                    this.mPreviousLineTouched = Editor.this.mTextView.getLineAtCoordinate(y);
                }
                int currLine = Editor.this.getCurrentLineAdjustedForSlop(layout, this.mPreviousLineTouched, y);
                int offset2 = getOffsetAtCoordinate(layout, currLine, x);
                this.mPreviousLineTouched = currLine;
                offset = offset2;
            }
            positionAtCursorOffset(offset, false, fromTouchScreen);
            if (Editor.this.mTextActionMode != null) {
                Editor.this.invalidateActionMode();
            }
        }

        @Override // android.widget.Editor.HandleView
        synchronized void onHandleMoved() {
            super.onHandleMoved();
            removeHiderCallback();
        }

        @Override // android.widget.Editor.HandleView
        public synchronized void onDetached() {
            super.onDetached();
            removeHiderCallback();
        }

        @Override // android.widget.Editor.HandleView
        protected synchronized int getMagnifierHandleTrigger() {
            return 0;
        }
    }

    @VisibleForTesting
    /* loaded from: classes3.dex */
    public final class SelectionHandleView extends HandleView {
        private final int mHandleType;
        private boolean mInWord;
        private boolean mLanguageDirectionChanged;
        private float mPrevX;
        private final float mTextViewEdgeSlop;
        private final int[] mTextViewLocation;
        private float mTouchWordDelta;

        public SelectionHandleView(Drawable drawableLtr, Drawable drawableRtl, int id, int handleType) {
            super(drawableLtr, drawableRtl, id);
            this.mInWord = false;
            this.mLanguageDirectionChanged = false;
            this.mTextViewLocation = new int[2];
            this.mHandleType = handleType;
            ViewConfiguration viewConfiguration = ViewConfiguration.get(Editor.this.mTextView.getContext());
            this.mTextViewEdgeSlop = viewConfiguration.getScaledTouchSlop() * 4;
        }

        private synchronized boolean isStartHandle() {
            return this.mHandleType == 0;
        }

        @Override // android.widget.Editor.HandleView
        protected synchronized int getHotspotX(Drawable drawable, boolean isRtlRun) {
            if (isRtlRun == isStartHandle()) {
                return drawable.getIntrinsicWidth() / 4;
            }
            return (drawable.getIntrinsicWidth() * 3) / 4;
        }

        @Override // android.widget.Editor.HandleView
        protected synchronized int getHorizontalGravity(boolean isRtlRun) {
            return isRtlRun == isStartHandle() ? 3 : 5;
        }

        @Override // android.widget.Editor.HandleView
        public synchronized int getCurrentCursorOffset() {
            return isStartHandle() ? Editor.this.mTextView.getSelectionStart() : Editor.this.mTextView.getSelectionEnd();
        }

        @Override // android.widget.Editor.HandleView
        protected synchronized void updateSelection(int offset) {
            if (isStartHandle()) {
                Selection.setSelection((Spannable) Editor.this.mTextView.getText(), offset, Editor.this.mTextView.getSelectionEnd());
            } else {
                Selection.setSelection((Spannable) Editor.this.mTextView.getText(), Editor.this.mTextView.getSelectionStart(), offset);
            }
            updateDrawable();
            if (Editor.this.mTextActionMode != null) {
                Editor.this.invalidateActionMode();
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:128:0x01c7, code lost:
            if (r8 < r26.mPrevLine) goto L116;
         */
        /* JADX WARN: Code restructure failed: missing block: B:77:0x0132, code lost:
            if (r26.this$0.mTextView.canScrollHorizontally(r15 ? -1 : 1) != false) goto L53;
         */
        /* JADX WARN: Removed duplicated region for block: B:151:0x020f  */
        /* JADX WARN: Removed duplicated region for block: B:198:0x02a9  */
        /* JADX WARN: Removed duplicated region for block: B:94:0x0166  */
        @Override // android.widget.Editor.HandleView
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        protected synchronized void updatePosition(float r27, float r28, boolean r29) {
            /*
                Method dump skipped, instructions count: 701
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: android.widget.Editor.SelectionHandleView.updatePosition(float, float, boolean):void");
        }

        @Override // android.widget.Editor.HandleView
        protected synchronized void positionAtCursorOffset(int offset, boolean forceUpdatePosition, boolean fromTouchScreen) {
            super.positionAtCursorOffset(offset, forceUpdatePosition, fromTouchScreen);
            this.mInWord = (offset == -1 || Editor.this.getWordIteratorWithText().isBoundary(offset)) ? false : true;
        }

        @Override // android.widget.Editor.HandleView, android.view.View
        public boolean onTouchEvent(MotionEvent event) {
            boolean superResult = super.onTouchEvent(event);
            switch (event.getActionMasked()) {
                case 0:
                    this.mTouchWordDelta = 0.0f;
                    this.mPrevX = -1.0f;
                    updateMagnifier(event);
                    break;
                case 1:
                case 3:
                    dismissMagnifier();
                    break;
                case 2:
                    updateMagnifier(event);
                    break;
            }
            return superResult;
        }

        private synchronized void positionAndAdjustForCrossingHandles(int offset, boolean fromTouchScreen) {
            int offset2;
            int anotherHandleOffset = isStartHandle() ? Editor.this.mTextView.getSelectionEnd() : Editor.this.mTextView.getSelectionStart();
            if ((isStartHandle() && offset >= anotherHandleOffset) || (!isStartHandle() && offset <= anotherHandleOffset)) {
                this.mTouchWordDelta = 0.0f;
                Layout layout = Editor.this.mTextView.getLayout();
                if (layout != null && offset != anotherHandleOffset) {
                    float horiz = getHorizontal(layout, offset);
                    float anotherHandleHoriz = getHorizontal(layout, anotherHandleOffset, !isStartHandle());
                    float currentHoriz = getHorizontal(layout, this.mPreviousOffset);
                    if ((currentHoriz < anotherHandleHoriz && horiz < anotherHandleHoriz) || (currentHoriz > anotherHandleHoriz && horiz > anotherHandleHoriz)) {
                        int currentOffset = getCurrentCursorOffset();
                        int offsetToGetRunRange = isStartHandle() ? currentOffset : Math.max(currentOffset - 1, 0);
                        long range = layout.getRunRange(offsetToGetRunRange);
                        if (isStartHandle()) {
                            offset2 = TextUtils.unpackRangeStartFromLong(range);
                        } else {
                            offset2 = TextUtils.unpackRangeEndFromLong(range);
                        }
                        positionAtCursorOffset(offset2, false, fromTouchScreen);
                        return;
                    }
                }
                offset = Editor.this.getNextCursorOffset(anotherHandleOffset, !isStartHandle());
            }
            positionAtCursorOffset(offset, false, fromTouchScreen);
        }

        private synchronized boolean positionNearEdgeOfScrollingView(float x, boolean atRtl) {
            Editor.this.mTextView.getLocationOnScreen(this.mTextViewLocation);
            if (atRtl == isStartHandle()) {
                int rightEdge = (this.mTextViewLocation[0] + Editor.this.mTextView.getWidth()) - Editor.this.mTextView.getPaddingRight();
                boolean nearEdge = x > ((float) rightEdge) - this.mTextViewEdgeSlop;
                return nearEdge;
            }
            int leftEdge = this.mTextViewLocation[0] + Editor.this.mTextView.getPaddingLeft();
            boolean nearEdge2 = x < ((float) leftEdge) + this.mTextViewEdgeSlop;
            return nearEdge2;
        }

        @Override // android.widget.Editor.HandleView
        protected synchronized boolean isAtRtlRun(Layout layout, int offset) {
            int offsetToCheck = isStartHandle() ? offset : Math.max(offset - 1, 0);
            return layout.isRtlCharAt(offsetToCheck);
        }

        @Override // android.widget.Editor.HandleView
        public synchronized float getHorizontal(Layout layout, int offset) {
            return getHorizontal(layout, offset, isStartHandle());
        }

        private synchronized float getHorizontal(Layout layout, int offset, boolean startHandle) {
            int line = layout.getLineForOffset(offset);
            int offsetToCheck = startHandle ? offset : Math.max(offset - 1, 0);
            boolean isRtlChar = layout.isRtlCharAt(offsetToCheck);
            boolean isRtlParagraph = layout.getParagraphDirection(line) == -1;
            return isRtlChar == isRtlParagraph ? layout.getPrimaryHorizontal(offset) : layout.getSecondaryHorizontal(offset);
        }

        @Override // android.widget.Editor.HandleView
        protected synchronized int getOffsetAtCoordinate(Layout layout, int line, float x) {
            int offsetToCheck;
            float localX = Editor.this.mTextView.convertToLocalHorizontalCoordinate(x);
            boolean isRtlParagraph = true;
            int primaryOffset = layout.getOffsetForHorizontal(line, localX, true);
            if (!layout.isLevelBoundary(primaryOffset)) {
                return primaryOffset;
            }
            int secondaryOffset = layout.getOffsetForHorizontal(line, localX, false);
            int currentOffset = getCurrentCursorOffset();
            int primaryDiff = Math.abs(primaryOffset - currentOffset);
            int secondaryDiff = Math.abs(secondaryOffset - currentOffset);
            if (primaryDiff < secondaryDiff) {
                return primaryOffset;
            }
            if (primaryDiff > secondaryDiff) {
                return secondaryOffset;
            }
            if (!isStartHandle()) {
                offsetToCheck = Math.max(currentOffset - 1, 0);
            } else {
                offsetToCheck = currentOffset;
            }
            boolean isRtlChar = layout.isRtlCharAt(offsetToCheck);
            if (layout.getParagraphDirection(line) != -1) {
                isRtlParagraph = false;
            }
            return isRtlChar == isRtlParagraph ? primaryOffset : secondaryOffset;
        }

        @Override // android.widget.Editor.HandleView
        protected synchronized int getMagnifierHandleTrigger() {
            if (isStartHandle()) {
                return 1;
            }
            return 2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized int getCurrentLineAdjustedForSlop(Layout layout, int prevLine, float y) {
        int trueLine = this.mTextView.getLineAtCoordinate(y);
        if (layout == null || prevLine > layout.getLineCount() || layout.getLineCount() <= 0 || prevLine < 0) {
            return trueLine;
        }
        if (Math.abs(trueLine - prevLine) >= 2) {
            return trueLine;
        }
        float verticalOffset = this.mTextView.viewportToContentVerticalOffset();
        int lineCount = layout.getLineCount();
        float slop = this.mTextView.getLineHeight() * 0.5f;
        float firstLineTop = layout.getLineTop(0) + verticalOffset;
        float prevLineTop = layout.getLineTop(prevLine) + verticalOffset;
        float yTopBound = Math.max(prevLineTop - slop, firstLineTop + slop);
        float lastLineBottom = layout.getLineBottom(lineCount - 1) + verticalOffset;
        float prevLineBottom = layout.getLineBottom(prevLine) + verticalOffset;
        float yBottomBound = Math.min(prevLineBottom + slop, lastLineBottom - slop);
        if (y <= yTopBound) {
            int currLine = Math.max(prevLine - 1, 0);
            return currLine;
        }
        int currLine2 = (y > yBottomBound ? 1 : (y == yBottomBound ? 0 : -1));
        if (currLine2 >= 0) {
            int currLine3 = Math.min(prevLine + 1, lineCount - 1);
            return currLine3;
        }
        return prevLine;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class InsertionPointCursorController implements CursorController {
        private InsertionHandleView mHandle;

        private InsertionPointCursorController() {
        }

        @Override // android.widget.Editor.CursorController
        public synchronized void show() {
            getHandle().show();
            if (Editor.this.mSelectionModifierCursorController != null) {
                Editor.this.mSelectionModifierCursorController.hide();
            }
        }

        @Override // android.widget.Editor.CursorController
        public synchronized void hide() {
            if (this.mHandle != null) {
                this.mHandle.hide();
            }
        }

        @Override // android.view.ViewTreeObserver.OnTouchModeChangeListener
        public void onTouchModeChanged(boolean isInTouchMode) {
            if (!isInTouchMode) {
                hide();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized InsertionHandleView getHandle() {
            if (Editor.this.mSelectHandleCenter == null) {
                Editor.this.mSelectHandleCenter = Editor.this.mTextView.getContext().getDrawable(Editor.this.mTextView.mTextSelectHandleRes);
            }
            if (this.mHandle == null) {
                this.mHandle = new InsertionHandleView(Editor.this.mSelectHandleCenter);
            }
            return this.mHandle;
        }

        @Override // android.widget.Editor.CursorController
        public synchronized void onDetached() {
            ViewTreeObserver observer = Editor.this.mTextView.getViewTreeObserver();
            observer.removeOnTouchModeChangeListener(this);
            if (this.mHandle != null) {
                this.mHandle.onDetached();
            }
        }

        @Override // android.widget.Editor.CursorController
        public synchronized boolean isCursorBeingModified() {
            return this.mHandle != null && this.mHandle.isDragging();
        }

        @Override // android.widget.Editor.CursorController
        public synchronized boolean isActive() {
            return this.mHandle != null && this.mHandle.isShowing();
        }

        public synchronized void invalidateHandle() {
            if (this.mHandle != null) {
                this.mHandle.invalidate();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class SelectionModifierCursorController implements CursorController {
        private static final int DRAG_ACCELERATOR_MODE_CHARACTER = 1;
        private static final int DRAG_ACCELERATOR_MODE_INACTIVE = 0;
        private static final int DRAG_ACCELERATOR_MODE_PARAGRAPH = 3;
        private static final int DRAG_ACCELERATOR_MODE_WORD = 2;
        private float mDownPositionX;
        private float mDownPositionY;
        private SelectionHandleView mEndHandle;
        private boolean mGestureStayedInTapRegion;
        private boolean mHaventMovedEnoughToStartDrag;
        private int mMaxTouchOffset;
        private int mMinTouchOffset;
        private SelectionHandleView mStartHandle;
        private int mStartOffset = -1;
        private int mLineSelectionIsOn = -1;
        private boolean mSwitchedLines = false;
        private int mDragAcceleratorMode = 0;

        SelectionModifierCursorController() {
            resetTouchOffsets();
        }

        @Override // android.widget.Editor.CursorController
        public synchronized void show() {
            if (Editor.this.mTextView.isInBatchEditMode()) {
                return;
            }
            initDrawables();
            initHandles();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void initDrawables() {
            if (Editor.this.mSelectHandleLeft == null) {
                Editor.this.mSelectHandleLeft = Editor.this.mTextView.getContext().getDrawable(Editor.this.mTextView.mTextSelectHandleLeftRes);
            }
            if (Editor.this.mSelectHandleRight == null) {
                Editor.this.mSelectHandleRight = Editor.this.mTextView.getContext().getDrawable(Editor.this.mTextView.mTextSelectHandleRightRes);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void initHandles() {
            if (this.mStartHandle == null) {
                this.mStartHandle = new SelectionHandleView(Editor.this.mSelectHandleLeft, Editor.this.mSelectHandleRight, R.id.selection_start_handle, 0);
            }
            if (this.mEndHandle == null) {
                this.mEndHandle = new SelectionHandleView(Editor.this.mSelectHandleRight, Editor.this.mSelectHandleLeft, R.id.selection_end_handle, 1);
            }
            this.mStartHandle.show();
            this.mEndHandle.show();
            Editor.this.hideInsertionPointCursorController();
        }

        @Override // android.widget.Editor.CursorController
        public synchronized void hide() {
            if (this.mStartHandle != null) {
                this.mStartHandle.hide();
            }
            if (this.mEndHandle != null) {
                this.mEndHandle.hide();
            }
        }

        public synchronized void enterDrag(int dragAcceleratorMode) {
            show();
            this.mDragAcceleratorMode = dragAcceleratorMode;
            this.mStartOffset = Editor.this.mTextView.getOffsetForPosition(Editor.this.mLastDownPositionX, Editor.this.mLastDownPositionY);
            this.mLineSelectionIsOn = Editor.this.mTextView.getLineAtCoordinate(Editor.this.mLastDownPositionY);
            hide();
            Editor.this.mTextView.getParent().requestDisallowInterceptTouchEvent(true);
            Editor.this.mTextView.cancelLongPress();
        }

        public synchronized void onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            boolean isMouse = event.isFromSource(8194);
            switch (event.getActionMasked()) {
                case 0:
                    if (!Editor.this.extractedTextModeWillBeStarted()) {
                        int offsetForPosition = Editor.this.mTextView.getOffsetForPosition(eventX, eventY);
                        this.mMaxTouchOffset = offsetForPosition;
                        this.mMinTouchOffset = offsetForPosition;
                        if (this.mGestureStayedInTapRegion && (Editor.this.mTapState == 2 || Editor.this.mTapState == 3)) {
                            float deltaX = eventX - this.mDownPositionX;
                            float deltaY = eventY - this.mDownPositionY;
                            float distanceSquared = (deltaX * deltaX) + (deltaY * deltaY);
                            ViewConfiguration viewConfiguration = ViewConfiguration.get(Editor.this.mTextView.getContext());
                            int doubleTapSlop = viewConfiguration.getScaledDoubleTapSlop();
                            boolean stayedInArea = distanceSquared < ((float) (doubleTapSlop * doubleTapSlop));
                            if (stayedInArea && (isMouse || Editor.this.isPositionOnText(eventX, eventY))) {
                                if (Editor.this.mTapState == 2) {
                                    Editor.this.selectCurrentWordAndStartDrag();
                                } else if (Editor.this.mTapState == 3) {
                                    selectCurrentParagraphAndStartDrag();
                                }
                                Editor.this.mDiscardNextActionUp = true;
                            }
                        }
                        this.mDownPositionX = eventX;
                        this.mDownPositionY = eventY;
                        this.mGestureStayedInTapRegion = true;
                        this.mHaventMovedEnoughToStartDrag = true;
                        return;
                    }
                    hide();
                    return;
                case 1:
                    if (isDragAcceleratorActive()) {
                        updateSelection(event);
                        Editor.this.mTextView.getParent().requestDisallowInterceptTouchEvent(false);
                        resetDragAcceleratorState();
                        if (Editor.this.mTextView.hasSelection()) {
                            Editor.this.startSelectionActionModeAsync(this.mHaventMovedEnoughToStartDrag);
                            return;
                        }
                        return;
                    }
                    return;
                case 2:
                    ViewConfiguration viewConfig = ViewConfiguration.get(Editor.this.mTextView.getContext());
                    int touchSlop = viewConfig.getScaledTouchSlop();
                    if (this.mGestureStayedInTapRegion || this.mHaventMovedEnoughToStartDrag) {
                        float deltaX2 = eventX - this.mDownPositionX;
                        float deltaY2 = eventY - this.mDownPositionY;
                        float distanceSquared2 = (deltaX2 * deltaX2) + (deltaY2 * deltaY2);
                        if (this.mGestureStayedInTapRegion) {
                            int doubleTapTouchSlop = viewConfig.getScaledDoubleTapTouchSlop();
                            this.mGestureStayedInTapRegion = distanceSquared2 <= ((float) (doubleTapTouchSlop * doubleTapTouchSlop));
                        }
                        if (this.mHaventMovedEnoughToStartDrag) {
                            this.mHaventMovedEnoughToStartDrag = distanceSquared2 <= ((float) (touchSlop * touchSlop));
                        }
                    }
                    if (isMouse && !isDragAcceleratorActive()) {
                        int offset = Editor.this.mTextView.getOffsetForPosition(eventX, eventY);
                        if (Editor.this.mTextView.hasSelection() && ((!this.mHaventMovedEnoughToStartDrag || this.mStartOffset != offset) && offset >= Editor.this.mTextView.getSelectionStart() && offset <= Editor.this.mTextView.getSelectionEnd())) {
                            Editor.this.startDragAndDrop();
                            return;
                        } else if (this.mStartOffset != offset) {
                            Editor.this.stopTextActionMode();
                            enterDrag(1);
                            Editor.this.mDiscardNextActionUp = true;
                            this.mHaventMovedEnoughToStartDrag = false;
                        }
                    }
                    if (this.mStartHandle == null || !this.mStartHandle.isShowing()) {
                        updateSelection(event);
                        return;
                    }
                    return;
                case 3:
                case 4:
                default:
                    return;
                case 5:
                case 6:
                    if (Editor.this.mTextView.getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH_DISTINCT)) {
                        updateMinAndMaxOffsets(event);
                        return;
                    }
                    return;
            }
        }

        private synchronized void updateSelection(MotionEvent event) {
            if (Editor.this.mTextView.getLayout() != null) {
                switch (this.mDragAcceleratorMode) {
                    case 1:
                        updateCharacterBasedSelection(event);
                        return;
                    case 2:
                        updateWordBasedSelection(event);
                        return;
                    case 3:
                        updateParagraphBasedSelection(event);
                        return;
                    default:
                        return;
                }
            }
        }

        private synchronized boolean selectCurrentParagraphAndStartDrag() {
            if (Editor.this.mInsertionActionModeRunnable != null) {
                Editor.this.mTextView.removeCallbacks(Editor.this.mInsertionActionModeRunnable);
            }
            Editor.this.stopTextActionMode();
            if (!Editor.this.selectCurrentParagraph()) {
                return false;
            }
            enterDrag(3);
            return true;
        }

        private synchronized void updateCharacterBasedSelection(MotionEvent event) {
            int offset = Editor.this.mTextView.getOffsetForPosition(event.getX(), event.getY());
            updateSelectionInternal(this.mStartOffset, offset, event.isFromSource(4098));
        }

        private synchronized void updateWordBasedSelection(MotionEvent event) {
            int currLine;
            float fingerOffset;
            int offset;
            int startOffset;
            if (this.mHaventMovedEnoughToStartDrag) {
                return;
            }
            boolean isMouse = event.isFromSource(8194);
            ViewConfiguration viewConfig = ViewConfiguration.get(Editor.this.mTextView.getContext());
            float eventX = event.getX();
            float eventY = event.getY();
            if (isMouse) {
                currLine = Editor.this.mTextView.getLineAtCoordinate(eventY);
            } else {
                float y = eventY;
                if (this.mSwitchedLines) {
                    int touchSlop = viewConfig.getScaledTouchSlop();
                    if (this.mStartHandle != null) {
                        fingerOffset = this.mStartHandle.getIdealVerticalOffset();
                    } else {
                        fingerOffset = touchSlop;
                    }
                    y = eventY - fingerOffset;
                }
                int currLine2 = Editor.this.getCurrentLineAdjustedForSlop(Editor.this.mTextView.getLayout(), this.mLineSelectionIsOn, y);
                if (!this.mSwitchedLines && currLine2 != this.mLineSelectionIsOn) {
                    this.mSwitchedLines = true;
                    return;
                }
                currLine = currLine2;
            }
            int offset2 = Editor.this.mTextView.getOffsetAtCoordinate(currLine, eventX);
            if (this.mStartOffset < offset2) {
                offset = Editor.this.getWordEnd(offset2);
                startOffset = Editor.this.getWordStart(this.mStartOffset);
            } else {
                offset = Editor.this.getWordStart(offset2);
                startOffset = Editor.this.getWordEnd(this.mStartOffset);
                if (startOffset == offset) {
                    offset = Editor.this.getNextCursorOffset(offset, false);
                }
            }
            this.mLineSelectionIsOn = currLine;
            updateSelectionInternal(startOffset, offset, event.isFromSource(4098));
        }

        private synchronized void updateParagraphBasedSelection(MotionEvent event) {
            int offset = Editor.this.mTextView.getOffsetForPosition(event.getX(), event.getY());
            int start = Math.min(offset, this.mStartOffset);
            int end = Math.max(offset, this.mStartOffset);
            long paragraphsRange = Editor.this.getParagraphsRange(start, end);
            int selectionStart = TextUtils.unpackRangeStartFromLong(paragraphsRange);
            int selectionEnd = TextUtils.unpackRangeEndFromLong(paragraphsRange);
            updateSelectionInternal(selectionStart, selectionEnd, event.isFromSource(4098));
        }

        private synchronized void updateSelectionInternal(int selectionStart, int selectionEnd, boolean fromTouchScreen) {
            boolean performHapticFeedback = fromTouchScreen && Editor.this.mHapticTextHandleEnabled && !(Editor.this.mTextView.getSelectionStart() == selectionStart && Editor.this.mTextView.getSelectionEnd() == selectionEnd);
            Selection.setSelection((Spannable) Editor.this.mTextView.getText(), selectionStart, selectionEnd);
            if (performHapticFeedback) {
                Editor.this.mTextView.performHapticFeedback(9);
            }
        }

        private synchronized void updateMinAndMaxOffsets(MotionEvent event) {
            int pointerCount = event.getPointerCount();
            for (int index = 0; index < pointerCount; index++) {
                int offset = Editor.this.mTextView.getOffsetForPosition(event.getX(index), event.getY(index));
                if (offset < this.mMinTouchOffset) {
                    this.mMinTouchOffset = offset;
                }
                if (offset > this.mMaxTouchOffset) {
                    this.mMaxTouchOffset = offset;
                }
            }
        }

        public synchronized int getMinTouchOffset() {
            return this.mMinTouchOffset;
        }

        public synchronized int getMaxTouchOffset() {
            return this.mMaxTouchOffset;
        }

        public synchronized void resetTouchOffsets() {
            this.mMaxTouchOffset = -1;
            this.mMinTouchOffset = -1;
            resetDragAcceleratorState();
        }

        private synchronized void resetDragAcceleratorState() {
            this.mStartOffset = -1;
            this.mDragAcceleratorMode = 0;
            this.mSwitchedLines = false;
            int selectionStart = Editor.this.mTextView.getSelectionStart();
            int selectionEnd = Editor.this.mTextView.getSelectionEnd();
            if (selectionStart < 0 || selectionEnd < 0) {
                Selection.removeSelection((Spannable) Editor.this.mTextView.getText());
            } else if (selectionStart > selectionEnd) {
                Selection.setSelection((Spannable) Editor.this.mTextView.getText(), selectionEnd, selectionStart);
            }
        }

        public synchronized boolean isSelectionStartDragged() {
            return this.mStartHandle != null && this.mStartHandle.isDragging();
        }

        @Override // android.widget.Editor.CursorController
        public synchronized boolean isCursorBeingModified() {
            return isDragAcceleratorActive() || isSelectionStartDragged() || (this.mEndHandle != null && this.mEndHandle.isDragging());
        }

        public synchronized boolean isDragAcceleratorActive() {
            return this.mDragAcceleratorMode != 0;
        }

        @Override // android.view.ViewTreeObserver.OnTouchModeChangeListener
        public void onTouchModeChanged(boolean isInTouchMode) {
            if (!isInTouchMode) {
                hide();
            }
        }

        @Override // android.widget.Editor.CursorController
        public synchronized void onDetached() {
            ViewTreeObserver observer = Editor.this.mTextView.getViewTreeObserver();
            observer.removeOnTouchModeChangeListener(this);
            if (this.mStartHandle != null) {
                this.mStartHandle.onDetached();
            }
            if (this.mEndHandle != null) {
                this.mEndHandle.onDetached();
            }
        }

        @Override // android.widget.Editor.CursorController
        public synchronized boolean isActive() {
            return this.mStartHandle != null && this.mStartHandle.isShowing();
        }

        public synchronized void invalidateHandles() {
            if (this.mStartHandle != null) {
                this.mStartHandle.invalidate();
            }
            if (this.mEndHandle != null) {
                this.mEndHandle.invalidate();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class CorrectionHighlighter {
        private static final int FADE_OUT_DURATION = 400;
        private int mEnd;
        private long mFadingStartTime;
        private int mStart;
        private RectF mTempRectF;
        private final Path mPath = new Path();
        private final Paint mPaint = new Paint(1);

        public CorrectionHighlighter() {
            this.mPaint.setCompatibilityScaling(Editor.this.mTextView.getResources().getCompatibilityInfo().applicationScale);
            this.mPaint.setStyle(Paint.Style.FILL);
        }

        public synchronized void highlight(CorrectionInfo info) {
            this.mStart = info.getOffset();
            this.mEnd = this.mStart + info.getNewText().length();
            this.mFadingStartTime = SystemClock.uptimeMillis();
            if (this.mStart < 0 || this.mEnd < 0) {
                stopAnimation();
            }
        }

        public synchronized void draw(Canvas canvas, int cursorOffsetVertical) {
            if (updatePath() && updatePaint()) {
                if (cursorOffsetVertical != 0) {
                    canvas.translate(0.0f, cursorOffsetVertical);
                }
                canvas.drawPath(this.mPath, this.mPaint);
                if (cursorOffsetVertical != 0) {
                    canvas.translate(0.0f, -cursorOffsetVertical);
                }
                invalidate(true);
                return;
            }
            stopAnimation();
            invalidate(false);
        }

        private synchronized boolean updatePaint() {
            long duration = SystemClock.uptimeMillis() - this.mFadingStartTime;
            if (duration > 400) {
                return false;
            }
            float coef = 1.0f - (((float) duration) / 400.0f);
            int highlightColorAlpha = Color.alpha(Editor.this.mTextView.mHighlightColor);
            int color = (Editor.this.mTextView.mHighlightColor & 16777215) + (((int) (highlightColorAlpha * coef)) << 24);
            this.mPaint.setColor(color);
            return true;
        }

        private synchronized boolean updatePath() {
            Layout layout = Editor.this.mTextView.getLayout();
            if (layout == null) {
                return false;
            }
            int length = Editor.this.mTextView.getText().length();
            int start = Math.min(length, this.mStart);
            int end = Math.min(length, this.mEnd);
            this.mPath.reset();
            layout.getSelectionPath(start, end, this.mPath);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void invalidate(boolean delayed) {
            if (Editor.this.mTextView.getLayout() == null) {
                return;
            }
            if (this.mTempRectF == null) {
                this.mTempRectF = new RectF();
            }
            this.mPath.computeBounds(this.mTempRectF, false);
            int left = Editor.this.mTextView.getCompoundPaddingLeft();
            int top = Editor.this.mTextView.getExtendedPaddingTop() + Editor.this.mTextView.getVerticalOffset(true);
            if (delayed) {
                Editor.this.mTextView.postInvalidateOnAnimation(((int) this.mTempRectF.left) + left, ((int) this.mTempRectF.top) + top, ((int) this.mTempRectF.right) + left, ((int) this.mTempRectF.bottom) + top);
            } else {
                Editor.this.mTextView.postInvalidate((int) this.mTempRectF.left, (int) this.mTempRectF.top, (int) this.mTempRectF.right, (int) this.mTempRectF.bottom);
            }
        }

        private synchronized void stopAnimation() {
            Editor.this.mCorrectionHighlighter = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class ErrorPopup extends PopupWindow {
        private boolean mAbove;
        private int mPopupInlineErrorAboveBackgroundId;
        private int mPopupInlineErrorBackgroundId;
        private final TextView mView;

        synchronized ErrorPopup(TextView v, int width, int height) {
            super(v, width, height);
            this.mAbove = false;
            this.mPopupInlineErrorBackgroundId = 0;
            this.mPopupInlineErrorAboveBackgroundId = 0;
            this.mView = v;
            this.mPopupInlineErrorBackgroundId = getResourceId(this.mPopupInlineErrorBackgroundId, 293);
            this.mView.setBackgroundResource(this.mPopupInlineErrorBackgroundId);
        }

        synchronized void fixDirection(boolean above) {
            this.mAbove = above;
            if (above) {
                this.mPopupInlineErrorAboveBackgroundId = getResourceId(this.mPopupInlineErrorAboveBackgroundId, 292);
            } else {
                this.mPopupInlineErrorBackgroundId = getResourceId(this.mPopupInlineErrorBackgroundId, 293);
            }
            this.mView.setBackgroundResource(above ? this.mPopupInlineErrorAboveBackgroundId : this.mPopupInlineErrorBackgroundId);
        }

        private synchronized int getResourceId(int currentId, int index) {
            if (currentId == 0) {
                TypedArray styledAttributes = this.mView.getContext().obtainStyledAttributes(android.R.styleable.Theme);
                int currentId2 = styledAttributes.getResourceId(index, 0);
                styledAttributes.recycle();
                return currentId2;
            }
            return currentId;
        }

        @Override // android.widget.PopupWindow
        public void update(int x, int y, int w, int h, boolean force) {
            super.update(x, y, w, h, force);
            boolean above = isAboveAnchor();
            if (above != this.mAbove) {
                fixDirection(above);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class InputContentType {
        boolean enterDown;
        Bundle extras;
        int imeActionId;
        CharSequence imeActionLabel;
        LocaleList imeHintLocales;
        int imeOptions = 0;
        TextView.OnEditorActionListener onEditorActionListener;
        public private protected String privateImeOptions;

        synchronized InputContentType() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class InputMethodState {
        int mBatchEditNesting;
        int mChangedDelta;
        int mChangedEnd;
        int mChangedStart;
        boolean mContentChanged;
        boolean mCursorChanged;
        final ExtractedText mExtractedText = new ExtractedText();
        ExtractedTextRequest mExtractedTextRequest;
        boolean mSelectionModeChanged;

        synchronized InputMethodState() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized boolean isValidRange(CharSequence text, int start, int end) {
        return start >= 0 && start <= end && end <= text.length();
    }

    @VisibleForTesting
    public synchronized SuggestionsPopupWindow getSuggestionsPopupWindowForTesting() {
        return this.mSuggestionsPopupWindow;
    }

    /* loaded from: classes3.dex */
    public static class UndoInputFilter implements InputFilter {
        private static final int MERGE_EDIT_MODE_FORCE_MERGE = 0;
        private static final int MERGE_EDIT_MODE_NEVER_MERGE = 1;
        private static final int MERGE_EDIT_MODE_NORMAL = 2;
        private final Editor mEditor;
        private boolean mExpanding;
        private boolean mHasComposition;
        private boolean mIsUserEdit;
        private boolean mPreviousOperationWasInSameBatchEdit;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes3.dex */
        private @interface MergeMode {
        }

        public synchronized UndoInputFilter(Editor editor) {
            this.mEditor = editor;
        }

        public synchronized void saveInstanceState(Parcel parcel) {
            parcel.writeInt(this.mIsUserEdit ? 1 : 0);
            parcel.writeInt(this.mHasComposition ? 1 : 0);
            parcel.writeInt(this.mExpanding ? 1 : 0);
            parcel.writeInt(this.mPreviousOperationWasInSameBatchEdit ? 1 : 0);
        }

        public synchronized void restoreInstanceState(Parcel parcel) {
            this.mIsUserEdit = parcel.readInt() != 0;
            this.mHasComposition = parcel.readInt() != 0;
            this.mExpanding = parcel.readInt() != 0;
            this.mPreviousOperationWasInSameBatchEdit = parcel.readInt() != 0;
        }

        public synchronized void beginBatchEdit() {
            this.mIsUserEdit = true;
        }

        public synchronized void endBatchEdit() {
            this.mIsUserEdit = false;
            this.mPreviousOperationWasInSameBatchEdit = false;
        }

        @Override // android.text.InputFilter
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (canUndoEdit(source, start, end, dest, dstart, dend)) {
                boolean hadComposition = this.mHasComposition;
                this.mHasComposition = isComposition(source);
                boolean wasExpanding = this.mExpanding;
                boolean shouldCreateSeparateState = false;
                if (end - start != dend - dstart) {
                    this.mExpanding = end - start > dend - dstart;
                    if (hadComposition && this.mExpanding != wasExpanding) {
                        shouldCreateSeparateState = true;
                    }
                }
                handleEdit(source, start, end, dest, dstart, dend, shouldCreateSeparateState);
                return null;
            }
            return null;
        }

        synchronized void freezeLastEdit() {
            this.mEditor.mUndoManager.beginUpdate("Edit text");
            EditOperation lastEdit = getLastEdit();
            if (lastEdit != null) {
                lastEdit.mFrozen = true;
            }
            this.mEditor.mUndoManager.endUpdate();
        }

        private synchronized void handleEdit(CharSequence source, int start, int end, Spanned dest, int dstart, int dend, boolean shouldCreateSeparateState) {
            int mergeMode;
            if (isInTextWatcher() || this.mPreviousOperationWasInSameBatchEdit) {
                mergeMode = 0;
            } else if (shouldCreateSeparateState) {
                mergeMode = 1;
            } else {
                mergeMode = 2;
            }
            String newText = TextUtils.substring(source, start, end);
            String oldText = TextUtils.substring(dest, dstart, dend);
            EditOperation edit = new EditOperation(this.mEditor, oldText, dstart, newText, this.mHasComposition);
            if (this.mHasComposition && TextUtils.equals(edit.mNewText, edit.mOldText)) {
                return;
            }
            recordEdit(edit, mergeMode);
        }

        private synchronized EditOperation getLastEdit() {
            UndoManager um = this.mEditor.mUndoManager;
            return (EditOperation) um.getLastOperation(EditOperation.class, this.mEditor.mUndoOwner, 1);
        }

        private synchronized void recordEdit(EditOperation edit, int mergeMode) {
            UndoManager um = this.mEditor.mUndoManager;
            um.beginUpdate("Edit text");
            EditOperation lastEdit = getLastEdit();
            if (lastEdit == null) {
                um.addOperation(edit, 0);
            } else if (mergeMode == 0) {
                lastEdit.forceMergeWith(edit);
            } else if (!this.mIsUserEdit) {
                um.commitState(this.mEditor.mUndoOwner);
                um.addOperation(edit, 0);
            } else if (mergeMode != 2 || !lastEdit.mergeWith(edit)) {
                um.commitState(this.mEditor.mUndoOwner);
                um.addOperation(edit, 0);
            }
            this.mPreviousOperationWasInSameBatchEdit = this.mIsUserEdit;
            um.endUpdate();
        }

        private synchronized boolean canUndoEdit(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (this.mEditor.mAllowUndo && !this.mEditor.mUndoManager.isInUndo() && Editor.isValidRange(source, start, end) && Editor.isValidRange(dest, dstart, dend)) {
                return (start == end && dstart == dend) ? false : true;
            }
            return false;
        }

        private static synchronized boolean isComposition(CharSequence source) {
            if (source instanceof Spannable) {
                Spannable text = (Spannable) source;
                int composeBegin = EditableInputConnection.getComposingSpanStart(text);
                int composeEnd = EditableInputConnection.getComposingSpanEnd(text);
                return composeBegin < composeEnd;
            }
            return false;
        }

        private synchronized boolean isInTextWatcher() {
            CharSequence text = this.mEditor.mTextView.getText();
            return (text instanceof SpannableStringBuilder) && ((SpannableStringBuilder) text).getTextWatcherDepth() > 0;
        }
    }

    /* loaded from: classes3.dex */
    public static class EditOperation extends UndoOperation<Editor> {
        public static final Parcelable.ClassLoaderCreator<EditOperation> CREATOR = new Parcelable.ClassLoaderCreator<EditOperation>() { // from class: android.widget.Editor.EditOperation.1
            @Override // android.os.Parcelable.Creator
            public EditOperation createFromParcel(Parcel in) {
                return new EditOperation(in, null);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.ClassLoaderCreator
            public EditOperation createFromParcel(Parcel in, ClassLoader loader) {
                return new EditOperation(in, loader);
            }

            @Override // android.os.Parcelable.Creator
            public EditOperation[] newArray(int size) {
                return new EditOperation[size];
            }
        };
        private static final int TYPE_DELETE = 1;
        private static final int TYPE_INSERT = 0;
        private static final int TYPE_REPLACE = 2;
        private boolean mFrozen;
        private boolean mIsComposition;
        private int mNewCursorPos;
        private String mNewText;
        private int mOldCursorPos;
        private String mOldText;
        private int mStart;
        private int mType;

        public synchronized EditOperation(Editor editor, String oldText, int dstart, String newText, boolean isComposition) {
            super(editor.mUndoOwner);
            this.mOldText = oldText;
            this.mNewText = newText;
            if (this.mNewText.length() > 0 && this.mOldText.length() == 0) {
                this.mType = 0;
            } else if (this.mNewText.length() == 0 && this.mOldText.length() > 0) {
                this.mType = 1;
            } else {
                this.mType = 2;
            }
            this.mStart = dstart;
            this.mOldCursorPos = editor.mTextView.getSelectionStart();
            this.mNewCursorPos = this.mNewText.length() + dstart;
            this.mIsComposition = isComposition;
        }

        public synchronized EditOperation(Parcel src, ClassLoader loader) {
            super(src, loader);
            this.mType = src.readInt();
            this.mOldText = src.readString();
            this.mNewText = src.readString();
            this.mStart = src.readInt();
            this.mOldCursorPos = src.readInt();
            this.mNewCursorPos = src.readInt();
            this.mFrozen = src.readInt() == 1;
            this.mIsComposition = src.readInt() == 1;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mType);
            dest.writeString(this.mOldText);
            dest.writeString(this.mNewText);
            dest.writeInt(this.mStart);
            dest.writeInt(this.mOldCursorPos);
            dest.writeInt(this.mNewCursorPos);
            dest.writeInt(this.mFrozen ? 1 : 0);
            dest.writeInt(this.mIsComposition ? 1 : 0);
        }

        private synchronized int getNewTextEnd() {
            return this.mStart + this.mNewText.length();
        }

        private synchronized int getOldTextEnd() {
            return this.mStart + this.mOldText.length();
        }

        @Override // android.content.UndoOperation
        public synchronized void commit() {
        }

        @Override // android.content.UndoOperation
        public synchronized void undo() {
            Editor editor = getOwnerData();
            Editable text = (Editable) editor.mTextView.getText();
            modifyText(text, this.mStart, getNewTextEnd(), this.mOldText, this.mStart, this.mOldCursorPos);
        }

        @Override // android.content.UndoOperation
        public synchronized void redo() {
            Editor editor = getOwnerData();
            Editable text = (Editable) editor.mTextView.getText();
            modifyText(text, this.mStart, getOldTextEnd(), this.mNewText, this.mStart, this.mNewCursorPos);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized boolean mergeWith(EditOperation edit) {
            if (this.mFrozen) {
                return false;
            }
            switch (this.mType) {
                case 0:
                    return mergeInsertWith(edit);
                case 1:
                    return mergeDeleteWith(edit);
                case 2:
                    return mergeReplaceWith(edit);
                default:
                    return false;
            }
        }

        private synchronized boolean mergeInsertWith(EditOperation edit) {
            if (edit.mType == 0) {
                if (getNewTextEnd() != edit.mStart) {
                    return false;
                }
                this.mNewText += edit.mNewText;
                this.mNewCursorPos = edit.mNewCursorPos;
                this.mFrozen = edit.mFrozen;
                this.mIsComposition = edit.mIsComposition;
                return true;
            } else if (!this.mIsComposition || edit.mType != 2 || this.mStart > edit.mStart || getNewTextEnd() < edit.getOldTextEnd()) {
                return false;
            } else {
                this.mNewText = this.mNewText.substring(0, edit.mStart - this.mStart) + edit.mNewText + this.mNewText.substring(edit.getOldTextEnd() - this.mStart, this.mNewText.length());
                this.mNewCursorPos = edit.mNewCursorPos;
                this.mIsComposition = edit.mIsComposition;
                return true;
            }
        }

        private synchronized boolean mergeDeleteWith(EditOperation edit) {
            if (edit.mType == 1 && this.mStart == edit.getOldTextEnd()) {
                this.mStart = edit.mStart;
                this.mOldText = edit.mOldText + this.mOldText;
                this.mNewCursorPos = edit.mNewCursorPos;
                this.mIsComposition = edit.mIsComposition;
                return true;
            }
            return false;
        }

        private synchronized boolean mergeReplaceWith(EditOperation edit) {
            if (edit.mType == 0 && getNewTextEnd() == edit.mStart) {
                this.mNewText += edit.mNewText;
                this.mNewCursorPos = edit.mNewCursorPos;
                return true;
            } else if (this.mIsComposition) {
                if (edit.mType == 1 && this.mStart <= edit.mStart && getNewTextEnd() >= edit.getOldTextEnd()) {
                    this.mNewText = this.mNewText.substring(0, edit.mStart - this.mStart) + this.mNewText.substring(edit.getOldTextEnd() - this.mStart, this.mNewText.length());
                    if (this.mNewText.isEmpty()) {
                        this.mType = 1;
                    }
                    this.mNewCursorPos = edit.mNewCursorPos;
                    this.mIsComposition = edit.mIsComposition;
                    return true;
                } else if (edit.mType == 2 && this.mStart == edit.mStart && TextUtils.equals(this.mNewText, edit.mOldText)) {
                    this.mNewText = edit.mNewText;
                    this.mNewCursorPos = edit.mNewCursorPos;
                    this.mIsComposition = edit.mIsComposition;
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        public synchronized void forceMergeWith(EditOperation edit) {
            if (mergeWith(edit)) {
                return;
            }
            Editor editor = getOwnerData();
            Editable editable = (Editable) editor.mTextView.getText();
            Editable originalText = new SpannableStringBuilder(editable.toString());
            modifyText(originalText, this.mStart, getNewTextEnd(), this.mOldText, this.mStart, this.mOldCursorPos);
            Editable finalText = new SpannableStringBuilder(editable.toString());
            modifyText(finalText, edit.mStart, edit.getOldTextEnd(), edit.mNewText, edit.mStart, edit.mNewCursorPos);
            this.mType = 2;
            this.mNewText = finalText.toString();
            this.mOldText = originalText.toString();
            this.mStart = 0;
            this.mNewCursorPos = edit.mNewCursorPos;
            this.mIsComposition = edit.mIsComposition;
        }

        private static synchronized void modifyText(Editable text, int deleteFrom, int deleteTo, CharSequence newText, int newTextInsertAt, int newCursorPos) {
            if (Editor.isValidRange(text, deleteFrom, deleteTo) && newTextInsertAt <= text.length() - (deleteTo - deleteFrom)) {
                if (deleteFrom != deleteTo) {
                    text.delete(deleteFrom, deleteTo);
                }
                if (newText.length() != 0) {
                    text.insert(newTextInsertAt, newText);
                }
            }
            if (newCursorPos >= 0 && newCursorPos <= text.length()) {
                Selection.setSelection(text, newCursorPos);
            }
        }

        private synchronized String getTypeString() {
            switch (this.mType) {
                case 0:
                    return "insert";
                case 1:
                    return "delete";
                case 2:
                    return "replace";
                default:
                    return "";
            }
        }

        public String toString() {
            return "[mType=" + getTypeString() + ", mOldText=" + this.mOldText + ", mNewText=" + this.mNewText + ", mStart=" + this.mStart + ", mOldCursorPos=" + this.mOldCursorPos + ", mNewCursorPos=" + this.mNewCursorPos + ", mFrozen=" + this.mFrozen + ", mIsComposition=" + this.mIsComposition + "]";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static final class ProcessTextIntentActionsHandler {
        private final SparseArray<AccessibilityNodeInfo.AccessibilityAction> mAccessibilityActions;
        private final SparseArray<Intent> mAccessibilityIntents;
        private final Context mContext;
        private final Editor mEditor;
        private final PackageManager mPackageManager;
        private final String mPackageName;
        private final List<ResolveInfo> mSupportedActivities;
        private final TextView mTextView;

        private synchronized ProcessTextIntentActionsHandler(Editor editor) {
            this.mAccessibilityIntents = new SparseArray<>();
            this.mAccessibilityActions = new SparseArray<>();
            this.mSupportedActivities = new ArrayList();
            this.mEditor = (Editor) Preconditions.checkNotNull(editor);
            this.mTextView = (TextView) Preconditions.checkNotNull(this.mEditor.mTextView);
            this.mContext = (Context) Preconditions.checkNotNull(this.mTextView.getContext());
            this.mPackageManager = (PackageManager) Preconditions.checkNotNull(this.mContext.getPackageManager());
            this.mPackageName = (String) Preconditions.checkNotNull(this.mContext.getPackageName());
        }

        public synchronized void onInitializeMenu(Menu menu) {
            loadSupportedActivities();
            int size = this.mSupportedActivities.size();
            for (int i = 0; i < size; i++) {
                ResolveInfo resolveInfo = this.mSupportedActivities.get(i);
                menu.add(0, 0, 100 + i, getLabel(resolveInfo)).setIntent(createProcessTextIntentForResolveInfo(resolveInfo)).setShowAsAction(0);
            }
        }

        public synchronized boolean performMenuItemAction(MenuItem item) {
            return fireIntent(item.getIntent());
        }

        public synchronized void initializeAccessibilityActions() {
            this.mAccessibilityIntents.clear();
            this.mAccessibilityActions.clear();
            int i = 0;
            loadSupportedActivities();
            for (ResolveInfo resolveInfo : this.mSupportedActivities) {
                int actionId = 268435712 + i;
                this.mAccessibilityActions.put(actionId, new AccessibilityNodeInfo.AccessibilityAction(actionId, getLabel(resolveInfo)));
                this.mAccessibilityIntents.put(actionId, createProcessTextIntentForResolveInfo(resolveInfo));
                i++;
            }
        }

        public synchronized void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo nodeInfo) {
            for (int i = 0; i < this.mAccessibilityActions.size(); i++) {
                nodeInfo.addAction(this.mAccessibilityActions.valueAt(i));
            }
        }

        public synchronized boolean performAccessibilityAction(int actionId) {
            return fireIntent(this.mAccessibilityIntents.get(actionId));
        }

        private synchronized boolean fireIntent(Intent intent) {
            if (intent != null && Intent.ACTION_PROCESS_TEXT.equals(intent.getAction())) {
                String selectedText = this.mTextView.getSelectedText();
                intent.putExtra(Intent.EXTRA_PROCESS_TEXT, (String) TextUtils.trimToParcelableSize(selectedText));
                this.mEditor.mPreserveSelection = true;
                this.mTextView.startActivityForResult(intent, 100);
                return true;
            }
            return false;
        }

        private synchronized void loadSupportedActivities() {
            this.mSupportedActivities.clear();
            if (!this.mContext.canStartActivityForResult()) {
                return;
            }
            PackageManager packageManager = this.mTextView.getContext().getPackageManager();
            List<ResolveInfo> unfiltered = packageManager.queryIntentActivities(createProcessTextIntent(), 0);
            for (ResolveInfo info : unfiltered) {
                if (isSupportedActivity(info)) {
                    this.mSupportedActivities.add(info);
                }
            }
        }

        private synchronized boolean isSupportedActivity(ResolveInfo info) {
            return this.mPackageName.equals(info.activityInfo.packageName) || (info.activityInfo.exported && (info.activityInfo.permission == null || this.mContext.checkSelfPermission(info.activityInfo.permission) == 0));
        }

        private synchronized Intent createProcessTextIntentForResolveInfo(ResolveInfo info) {
            return createProcessTextIntent().putExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, !this.mTextView.isTextEditable()).setClassName(info.activityInfo.packageName, info.activityInfo.name);
        }

        private synchronized Intent createProcessTextIntent() {
            return new Intent().setAction(Intent.ACTION_PROCESS_TEXT).setType(ClipDescription.MIMETYPE_TEXT_PLAIN);
        }

        private synchronized CharSequence getLabel(ResolveInfo resolveInfo) {
            return resolveInfo.loadLabel(this.mPackageManager);
        }
    }
}
