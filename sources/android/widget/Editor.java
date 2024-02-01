package android.widget;

import android.animation.ValueAnimator;
import android.annotation.UnsupportedAppUsage;
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
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.RenderNode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ParcelableParcel;
import android.os.SystemClock;
import android.provider.Settings;
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
import android.text.format.DateFormat;
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
import android.view.DragAndDropPermissions;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
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
import java.util.Collections;
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
    @UnsupportedAppUsage
    boolean mCreatedWithASelection;
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
    @UnsupportedAppUsage
    private boolean mInsertionControllerEnabled;
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
    @UnsupportedAppUsage(maxTargetSdk = 28)
    Drawable mSelectHandleCenter;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    Drawable mSelectHandleLeft;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    Drawable mSelectHandleRight;
    private SelectionActionModeHelper mSelectionActionModeHelper;
    @UnsupportedAppUsage
    private boolean mSelectionControllerEnabled;
    SelectionModifierCursorController mSelectionModifierCursorController;
    boolean mSelectionMoved;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private long mShowCursor;
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
    @UnsupportedAppUsage
    boolean mShowSoftInputOnFocus = true;
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
        void hide();

        boolean isActive();

        boolean isCursorBeingModified();

        void onDetached();

        void show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public interface EasyEditDeleteListener {
        void onDeleteClick(EasyEditSpan easyEditSpan);
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
        void updatePosition(int i, int i2, boolean z, boolean z2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class TextRenderNode {
        boolean isDirty = true;
        boolean needsToBeShifted = true;
        RenderNode renderNode;

        public TextRenderNode(String name) {
            this.renderNode = RenderNode.create(name, null);
        }

        boolean needsRecord() {
            return this.isDirty || !this.renderNode.hasDisplayList();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Editor(TextView textView) {
        this.mTextView = textView;
        TextView textView2 = this.mTextView;
        textView2.setFilters(textView2.getFilters());
        this.mProcessTextIntentActionsHandler = new ProcessTextIntentActionsHandler();
        this.mHapticTextHandleEnabled = this.mTextView.getContext().getResources().getBoolean(R.bool.config_enableHapticTextHandle);
        this.mMagnifierAnimator = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ParcelableParcel saveInstanceState() {
        ParcelableParcel state = new ParcelableParcel(getClass().getClassLoader());
        Parcel parcel = state.getParcel();
        this.mUndoManager.saveInstanceState(parcel);
        this.mUndoInputFilter.saveInstanceState(parcel);
        return state;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void restoreInstanceState(ParcelableParcel state) {
        Parcel parcel = state.getParcel();
        this.mUndoManager.restoreInstanceState(parcel, state.getClassLoader());
        this.mUndoInputFilter.restoreInstanceState(parcel);
        this.mUndoOwner = this.mUndoManager.getOwner("Editor", this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void forgetUndoRedo() {
        UndoOwner[] owners = {this.mUndoOwner};
        this.mUndoManager.forgetUndos(owners, -1);
        this.mUndoManager.forgetRedos(owners, -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canUndo() {
        UndoOwner[] owners = {this.mUndoOwner};
        return this.mAllowUndo && this.mUndoManager.countUndos(owners) > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean canRedo() {
        UndoOwner[] owners = {this.mUndoOwner};
        return this.mAllowUndo && this.mUndoManager.countRedos(owners) > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void undo() {
        if (!this.mAllowUndo) {
            return;
        }
        UndoOwner[] owners = {this.mUndoOwner};
        this.mUndoManager.undo(owners, 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void redo() {
        if (!this.mAllowUndo) {
            return;
        }
        UndoOwner[] owners = {this.mUndoOwner};
        this.mUndoManager.redo(owners, 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void replace() {
        if (this.mSuggestionsPopupWindow == null) {
            this.mSuggestionsPopupWindow = new SuggestionsPopupWindow();
        }
        hideCursorAndSpanControllers();
        this.mSuggestionsPopupWindow.show();
        int middle = (this.mTextView.getSelectionStart() + this.mTextView.getSelectionEnd()) / 2;
        Selection.setSelection((Spannable) this.mTextView.getText(), middle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAttachedToWindow() {
        if (this.mShowErrorAfterAttach) {
            showError();
            this.mShowErrorAfterAttach = false;
        }
        ViewTreeObserver observer = this.mTextView.getViewTreeObserver();
        if (observer.isAlive()) {
            InsertionPointCursorController insertionPointCursorController = this.mInsertionPointCursorController;
            if (insertionPointCursorController != null) {
                observer.addOnTouchModeChangeListener(insertionPointCursorController);
            }
            SelectionModifierCursorController selectionModifierCursorController = this.mSelectionModifierCursorController;
            if (selectionModifierCursorController != null) {
                selectionModifierCursorController.resetTouchOffsets();
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
    public void onDetachedFromWindow() {
        getPositionListener().removeSubscriber(this.mCursorAnchorInfoNotifier);
        if (this.mError != null) {
            hideError();
        }
        suspendBlink();
        InsertionPointCursorController insertionPointCursorController = this.mInsertionPointCursorController;
        if (insertionPointCursorController != null) {
            insertionPointCursorController.onDetached();
        }
        SelectionModifierCursorController selectionModifierCursorController = this.mSelectionModifierCursorController;
        if (selectionModifierCursorController != null) {
            selectionModifierCursorController.onDetached();
        }
        Runnable runnable = this.mShowSuggestionRunnable;
        if (runnable != null) {
            this.mTextView.removeCallbacks(runnable);
        }
        Runnable runnable2 = this.mInsertionActionModeRunnable;
        if (runnable2 != null) {
            this.mTextView.removeCallbacks(runnable2);
        }
        this.mTextView.removeCallbacks(this.mShowFloatingToolbar);
        discardTextDisplayLists();
        SpellChecker spellChecker = this.mSpellChecker;
        if (spellChecker != null) {
            spellChecker.closeSession();
            this.mSpellChecker = null;
        }
        hideCursorAndSpanControllers();
        stopTextActionModeWithPreservingSelection();
    }

    private void discardTextDisplayLists() {
        if (this.mTextRenderNodes != null) {
            int i = 0;
            while (true) {
                TextRenderNode[] textRenderNodeArr = this.mTextRenderNodes;
                if (i < textRenderNodeArr.length) {
                    RenderNode displayList = textRenderNodeArr[i] != null ? textRenderNodeArr[i].renderNode : null;
                    if (displayList != null && displayList.hasDisplayList()) {
                        displayList.discardDisplayList();
                    }
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    private void showError() {
        if (this.mTextView.getWindowToken() == null) {
            this.mShowErrorAfterAttach = true;
            return;
        }
        if (this.mErrorPopup == null) {
            LayoutInflater inflater = LayoutInflater.from(this.mTextView.getContext());
            TextView err = (TextView) inflater.inflate(R.layout.textview_hint, (ViewGroup) null);
            float scale = this.mTextView.getResources().getDisplayMetrics().density;
            this.mErrorPopup = new ErrorPopup(err, (int) ((200.0f * scale) + LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS), (int) ((50.0f * scale) + LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS));
            this.mErrorPopup.setFocusable(false);
            this.mErrorPopup.setInputMethodMode(1);
        }
        TextView tv = (TextView) this.mErrorPopup.getContentView();
        chooseSize(this.mErrorPopup, this.mError, tv);
        tv.setText(this.mError);
        this.mErrorPopup.showAsDropDown(this.mTextView, getErrorX(), getErrorY(), 51);
        ErrorPopup errorPopup = this.mErrorPopup;
        errorPopup.fixDirection(errorPopup.isAboveAnchor());
    }

    public void setError(CharSequence error, Drawable icon) {
        this.mError = TextUtils.stringOrSpannedString(error);
        this.mErrorWasChanged = true;
        if (this.mError == null) {
            setErrorIcon(null);
            ErrorPopup errorPopup = this.mErrorPopup;
            if (errorPopup != null) {
                if (errorPopup.isShowing()) {
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

    private void setErrorIcon(Drawable icon) {
        TextView.Drawables dr = this.mTextView.mDrawables;
        if (dr == null) {
            TextView textView = this.mTextView;
            TextView.Drawables drawables = new TextView.Drawables(textView.getContext());
            dr = drawables;
            textView.mDrawables = drawables;
        }
        dr.setErrorDrawable(icon, this.mTextView);
        this.mTextView.resetResolvedDrawables();
        this.mTextView.invalidate();
        this.mTextView.requestLayout();
    }

    private void hideError() {
        ErrorPopup errorPopup = this.mErrorPopup;
        if (errorPopup != null && errorPopup.isShowing()) {
            this.mErrorPopup.dismiss();
        }
        this.mShowErrorAfterAttach = false;
    }

    private int getErrorX() {
        float scale = this.mTextView.getResources().getDisplayMetrics().density;
        TextView.Drawables dr = this.mTextView.mDrawables;
        int layoutDirection = this.mTextView.getLayoutDirection();
        if (layoutDirection != 1) {
            int offset = ((-(dr != null ? dr.mDrawableSizeRight : 0)) / 2) + ((int) ((25.0f * scale) + LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS));
            int errorX = ((this.mTextView.getWidth() - this.mErrorPopup.getWidth()) - this.mTextView.getPaddingRight()) + offset;
            return errorX;
        }
        int offset2 = ((dr != null ? dr.mDrawableSizeLeft : 0) / 2) - ((int) ((25.0f * scale) + LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS));
        int errorX2 = this.mTextView.getPaddingLeft() + offset2;
        return errorX2;
    }

    private int getErrorY() {
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
        return ((icontop + height) - this.mTextView.getHeight()) - ((int) ((2.0f * scale) + LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void createInputContentTypeIfNeeded() {
        if (this.mInputContentType == null) {
            this.mInputContentType = new InputContentType();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void createInputMethodStateIfNeeded() {
        if (this.mInputMethodState == null) {
            this.mInputMethodState = new InputMethodState();
        }
    }

    private boolean isCursorVisible() {
        return this.mCursorVisible && this.mTextView.isTextEditable();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean shouldRenderCursor() {
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
    public void prepareCursorControllers() {
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
            InsertionPointCursorController insertionPointCursorController = this.mInsertionPointCursorController;
            if (insertionPointCursorController != null) {
                insertionPointCursorController.onDetached();
                this.mInsertionPointCursorController = null;
            }
        }
        if (!this.mSelectionControllerEnabled) {
            lambda$startActionModeInternal$0$Editor();
            SelectionModifierCursorController selectionModifierCursorController = this.mSelectionModifierCursorController;
            if (selectionModifierCursorController != null) {
                selectionModifierCursorController.onDetached();
                this.mSelectionModifierCursorController = null;
            }
        }
    }

    void hideInsertionPointCursorController() {
        InsertionPointCursorController insertionPointCursorController = this.mInsertionPointCursorController;
        if (insertionPointCursorController != null) {
            insertionPointCursorController.hide();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void hideCursorAndSpanControllers() {
        hideCursorControllers();
        hideSpanControllers();
    }

    private void hideSpanControllers() {
        SpanController spanController = this.mSpanController;
        if (spanController != null) {
            spanController.hide();
        }
    }

    private void hideCursorControllers() {
        if (this.mSuggestionsPopupWindow != null && (this.mTextView.isInExtractedMode() || !this.mSuggestionsPopupWindow.isShowingUp())) {
            this.mSuggestionsPopupWindow.hide();
        }
        hideInsertionPointCursorController();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSpellCheckSpans(int start, int end, boolean createSpellChecker) {
        this.mTextView.removeAdjacentSuggestionSpans(start);
        this.mTextView.removeAdjacentSuggestionSpans(end);
        if (this.mTextView.isTextEditable() && this.mTextView.isSuggestionsEnabled() && !this.mTextView.isInExtractedMode()) {
            if (this.mSpellChecker == null && createSpellChecker) {
                this.mSpellChecker = new SpellChecker(this.mTextView);
            }
            SpellChecker spellChecker = this.mSpellChecker;
            if (spellChecker != null) {
                spellChecker.spellCheck(start, end);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onScreenStateChanged(int screenState) {
        if (screenState == 0) {
            suspendBlink();
        } else if (screenState == 1) {
            resumeBlink();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void suspendBlink() {
        Blink blink = this.mBlink;
        if (blink != null) {
            blink.cancel();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resumeBlink() {
        Blink blink = this.mBlink;
        if (blink != null) {
            blink.uncancel();
            makeBlink();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void adjustInputType(boolean password, boolean passwordInputType, boolean webPasswordInputType, boolean numberPasswordInputType) {
        int i = this.mInputType;
        if ((i & 15) == 1) {
            if (password || passwordInputType) {
                this.mInputType = (this.mInputType & (-4081)) | 128;
            }
            if (webPasswordInputType) {
                this.mInputType = (this.mInputType & (-4081)) | 224;
            }
        } else if ((i & 15) == 2 && numberPasswordInputType) {
            this.mInputType = (i & (-4081)) | 16;
        }
    }

    private void chooseSize(PopupWindow pop, CharSequence text, TextView tv) {
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
    public void setFrame() {
        ErrorPopup errorPopup = this.mErrorPopup;
        if (errorPopup != null) {
            TextView tv = (TextView) errorPopup.getContentView();
            chooseSize(this.mErrorPopup, this.mError, tv);
            this.mErrorPopup.update(this.mTextView, getErrorX(), getErrorY(), this.mErrorPopup.getWidth(), this.mErrorPopup.getHeight());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getWordStart(int offset) {
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
    public int getWordEnd(int offset) {
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

    private boolean needsToSelectAllToSelectWordOrParagraph() {
        if (this.mTextView.hasPasswordTransformationMethod()) {
            return true;
        }
        int inputType = this.mTextView.getInputType();
        int klass = inputType & 15;
        int variation = inputType & InputType.TYPE_MASK_VARIATION;
        return klass == 2 || klass == 3 || klass == 4 || variation == 16 || variation == 32 || variation == 208 || variation == 176;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean selectCurrentWord() {
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
                selectionEnd = wordIterator.getEnd(maxOffset);
                if (selectionStart == -1 || selectionEnd == -1 || selectionStart == selectionEnd) {
                    long range = getCharClusterRange(minOffset);
                    selectionStart = TextUtils.unpackRangeStartFromLong(range);
                    selectionEnd = TextUtils.unpackRangeEndFromLong(range);
                }
            }
            Selection.setSelection((Spannable) this.mTextView.getText(), selectionStart, selectionEnd);
            return selectionEnd > selectionStart;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean selectCurrentParagraph() {
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
    public long getParagraphsRange(int startOffset, int endOffset) {
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
    public void onLocaleChanged() {
        this.mWordIterator = null;
        this.mWordIteratorWithText = null;
    }

    public WordIterator getWordIterator() {
        if (this.mWordIterator == null) {
            this.mWordIterator = new WordIterator(this.mTextView.getTextServicesLocale());
        }
        return this.mWordIterator;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public WordIterator getWordIteratorWithText() {
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
    public int getNextCursorOffset(int offset, boolean findAfterGivenOffset) {
        Layout layout = this.mTextView.getLayout();
        return layout == null ? offset : findAfterGivenOffset == layout.isRtlCharAt(offset) ? layout.getOffsetToLeftOf(offset) : layout.getOffsetToRightOf(offset);
    }

    private long getCharClusterRange(int offset) {
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

    private boolean touchPositionIsInSelection() {
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
    public PositionListener getPositionListener() {
        if (this.mPositionListener == null) {
            this.mPositionListener = new PositionListener();
        }
        return this.mPositionListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isOffsetVisible(int offset) {
        Layout layout = this.mTextView.getLayout();
        if (layout == null) {
            return false;
        }
        int line = layout.getLineForOffset(offset);
        int lineBottom = layout.getLineBottom(line);
        int primaryHorizontal = (int) layout.getPrimaryHorizontal(offset);
        TextView textView = this.mTextView;
        return textView.isPositionVisible(textView.viewportToContentHorizontalOffset() + primaryHorizontal, this.mTextView.viewportToContentVerticalOffset() + lineBottom);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPositionOnText(float x, float y) {
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
    public void startDragAndDrop() {
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
        lambda$startActionModeInternal$0$Editor();
        if (hasSelectionController()) {
            getSelectionController().resetTouchOffsets();
        }
    }

    public boolean performLongClick(boolean handled) {
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
                lambda$startActionModeInternal$0$Editor();
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
    public float getLastUpPositionX() {
        return this.mLastUpPositionX;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getLastUpPositionY() {
        return this.mLastUpPositionY;
    }

    private long getLastTouchOffsets() {
        SelectionModifierCursorController selectionController = getSelectionController();
        int minOffset = selectionController.getMinTouchOffset();
        int maxOffset = selectionController.getMaxTouchOffset();
        return TextUtils.packRangeInLong(minOffset, maxOffset);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onFocusChanged(boolean focused, int direction) {
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
                    TextView textView = this.mTextView;
                    mMovement.onTakeFocus(textView, (Spannable) textView.getText(), direction);
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
                lambda$startActionModeInternal$0$Editor();
            }
            downgradeEasyCorrectionSpans();
        }
        SelectionModifierCursorController selectionModifierCursorController = this.mSelectionModifierCursorController;
        if (selectionModifierCursorController != null) {
            selectionModifierCursorController.resetTouchOffsets();
        }
        ensureNoSelectionIfNonSelectable();
    }

    private void ensureNoSelectionIfNonSelectable() {
        if (!this.mTextView.textCanBeSelected() && this.mTextView.hasSelection()) {
            Selection.setSelection((Spannable) this.mTextView.getText(), this.mTextView.length(), this.mTextView.length());
        }
    }

    private void downgradeEasyCorrectionSpans() {
        CharSequence text = this.mTextView.getText();
        if (text instanceof Spannable) {
            Spannable spannable = (Spannable) text;
            SuggestionSpan[] suggestionSpans = (SuggestionSpan[]) spannable.getSpans(0, spannable.length(), SuggestionSpan.class);
            for (int i = 0; i < suggestionSpans.length; i++) {
                int flags = suggestionSpans[i].getFlags();
                if ((flags & 1) != 0 && (flags & 2) == 0) {
                    suggestionSpans[i].setFlags(flags & (-2));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void sendOnTextChanged(int start, int before, int after) {
        getSelectionActionModeHelper().onTextChanged(start, start + before);
        updateSpellCheckSpans(start, start + after, false);
        this.mUpdateWordIteratorText = true;
        hideCursorControllers();
        SelectionModifierCursorController selectionModifierCursorController = this.mSelectionModifierCursorController;
        if (selectionModifierCursorController != null) {
            selectionModifierCursorController.resetTouchOffsets();
        }
        lambda$startActionModeInternal$0$Editor();
    }

    private int getLastTapPosition() {
        int lastTapPosition;
        SelectionModifierCursorController selectionModifierCursorController = this.mSelectionModifierCursorController;
        if (selectionModifierCursorController != null && (lastTapPosition = selectionModifierCursorController.getMinTouchOffset()) >= 0) {
            if (lastTapPosition > this.mTextView.getText().length()) {
                return this.mTextView.getText().length();
            }
            return lastTapPosition;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (hasWindowFocus) {
            Blink blink = this.mBlink;
            if (blink != null) {
                blink.uncancel();
                makeBlink();
            }
            if (this.mTextView.hasSelection() && !extractedTextModeWillBeStarted()) {
                refreshTextActionMode();
                return;
            }
            return;
        }
        Blink blink2 = this.mBlink;
        if (blink2 != null) {
            blink2.cancel();
        }
        InputContentType inputContentType = this.mInputContentType;
        if (inputContentType != null) {
            inputContentType.enterDown = false;
        }
        hideCursorAndSpanControllers();
        stopTextActionModeWithPreservingSelection();
        SuggestionsPopupWindow suggestionsPopupWindow = this.mSuggestionsPopupWindow;
        if (suggestionsPopupWindow != null) {
            suggestionsPopupWindow.onParentLostFocus();
        }
        ensureEndedBatchEdit();
        ensureNoSelectionIfNonSelectable();
    }

    private void updateTapState(MotionEvent event) {
        int action = event.getActionMasked();
        if (action == 0) {
            boolean isMouse = event.isFromSource(8194);
            int i = this.mTapState;
            if ((i == 1 || (i == 2 && isMouse)) && SystemClock.uptimeMillis() - this.mLastTouchUpTime <= ViewConfiguration.getDoubleTapTimeout()) {
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

    private boolean shouldFilterOutTouchEvent(MotionEvent event) {
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
    public void onTouchEvent(MotionEvent event) {
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
        Runnable runnable = this.mShowSuggestionRunnable;
        if (runnable != null) {
            this.mTextView.removeCallbacks(runnable);
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
    public void updateFloatingToolbarVisibility(MotionEvent event) {
        if (this.mTextActionMode != null) {
            int actionMasked = event.getActionMasked();
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    hideFloatingToolbar(-1);
                    return;
                } else if (actionMasked != 3) {
                    return;
                }
            }
            showFloatingToolbar();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void hideFloatingToolbar(int duration) {
        if (this.mTextActionMode != null) {
            this.mTextView.removeCallbacks(this.mShowFloatingToolbar);
            this.mTextActionMode.hide(duration);
        }
    }

    private void showFloatingToolbar() {
        if (this.mTextActionMode != null) {
            int delay = ViewConfiguration.getDoubleTapTimeout();
            this.mTextView.postDelayed(this.mShowFloatingToolbar, delay);
            invalidateActionModeAsync();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public InputMethodManager getInputMethodManager() {
        return (InputMethodManager) this.mTextView.getContext().getSystemService(InputMethodManager.class);
    }

    public void beginBatchEdit() {
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

    public void endBatchEdit() {
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

    void ensureEndedBatchEdit() {
        InputMethodState ims = this.mInputMethodState;
        if (ims != null && ims.mBatchEditNesting != 0) {
            ims.mBatchEditNesting = 0;
            finishBatchEdit(ims);
        }
    }

    void finishBatchEdit(InputMethodState ims) {
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
    public boolean extractText(ExtractedTextRequest request, ExtractedText outText) {
        return extractTextInternal(request, -1, -1, -1, outText);
    }

    private boolean extractTextInternal(ExtractedTextRequest request, int partialStartOffset, int partialEndOffset, int delta, ExtractedText outText) {
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
    public boolean reportExtractedText() {
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
        if (req == null || (imm = getInputMethodManager()) == null) {
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
    public void sendUpdateSelection() {
        InputMethodManager imm;
        int candStart;
        int candEnd;
        InputMethodState inputMethodState = this.mInputMethodState;
        if (inputMethodState != null && inputMethodState.mBatchEditNesting <= 0 && (imm = getInputMethodManager()) != null) {
            int selectionStart = this.mTextView.getSelectionStart();
            int selectionEnd = this.mTextView.getSelectionEnd();
            if (!(this.mTextView.getText() instanceof Spannable)) {
                candStart = -1;
                candEnd = -1;
            } else {
                Spannable sp = (Spannable) this.mTextView.getText();
                int candStart2 = EditableInputConnection.getComposingSpanStart(sp);
                int candEnd2 = EditableInputConnection.getComposingSpanEnd(sp);
                candStart = candStart2;
                candEnd = candEnd2;
            }
            imm.updateSelection(this.mTextView, selectionStart, selectionEnd, candStart, candEnd);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDraw(Canvas canvas, Layout layout, Path highlight, Paint highlightPaint, int cursorOffsetVertical) {
        InputMethodManager imm;
        int selectionStart = this.mTextView.getSelectionStart();
        int selectionEnd = this.mTextView.getSelectionEnd();
        InputMethodState ims = this.mInputMethodState;
        if (ims != null && ims.mBatchEditNesting == 0 && (imm = getInputMethodManager()) != null && imm.isActive(this.mTextView) && (ims.mContentChanged || ims.mSelectionModeChanged)) {
            reportExtractedText();
        }
        CorrectionHighlighter correctionHighlighter = this.mCorrectionHighlighter;
        if (correctionHighlighter != null) {
            correctionHighlighter.draw(canvas, cursorOffsetVertical);
        }
        if (highlight != null && selectionStart == selectionEnd && this.mDrawableForCursor != null) {
            drawCursor(canvas, cursorOffsetVertical);
            highlight = null;
        }
        SelectionActionModeHelper selectionActionModeHelper = this.mSelectionActionModeHelper;
        if (selectionActionModeHelper != null) {
            selectionActionModeHelper.onDraw(canvas);
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

    private void drawHardwareAccelerated(Canvas canvas, Layout layout, Path highlight, Paint highlightPaint, int cursorOffsetVertical) {
        int numberOfBlocks;
        int[] blockEndLines;
        DynamicLayout dynamicLayout;
        int lastLine;
        int firstLine;
        ArraySet<Integer> blockSet;
        int lastIndex;
        int lastIndex2;
        int i;
        int lastIndex3;
        boolean z;
        long lineRange;
        int i2;
        int indexFirstChangedBlock;
        Editor editor = this;
        long lineRange2 = layout.getLineRangeForDraw(canvas);
        int firstLine2 = TextUtils.unpackRangeStartFromLong(lineRange2);
        int lastLine2 = TextUtils.unpackRangeEndFromLong(lineRange2);
        if (lastLine2 < 0) {
            return;
        }
        layout.drawBackground(canvas, highlight, highlightPaint, cursorOffsetVertical, firstLine2, lastLine2);
        if (layout instanceof DynamicLayout) {
            if (editor.mTextRenderNodes == null) {
                editor.mTextRenderNodes = (TextRenderNode[]) ArrayUtils.emptyArray(TextRenderNode.class);
            }
            DynamicLayout dynamicLayout2 = (DynamicLayout) layout;
            int[] blockEndLines2 = dynamicLayout2.getBlockEndLines();
            int[] blockIndices = dynamicLayout2.getBlockIndices();
            int numberOfBlocks2 = dynamicLayout2.getNumberOfBlocks();
            int indexFirstChangedBlock2 = dynamicLayout2.getIndexFirstChangedBlock();
            ArraySet<Integer> blockSet2 = dynamicLayout2.getBlocksAlwaysNeedToBeRedrawn();
            int i3 = -1;
            boolean z2 = true;
            if (blockSet2 != null) {
                int i4 = 0;
                while (i4 < blockSet2.size()) {
                    int blockIndex = dynamicLayout2.getBlockIndex(blockSet2.valueAt(i4).intValue());
                    if (blockIndex != i3) {
                        TextRenderNode[] textRenderNodeArr = editor.mTextRenderNodes;
                        if (textRenderNodeArr[blockIndex] != null) {
                            textRenderNodeArr[blockIndex].needsToBeShifted = true;
                        }
                    }
                    i4++;
                    i3 = -1;
                }
            }
            int startBlock = Arrays.binarySearch(blockEndLines2, 0, numberOfBlocks2, firstLine2);
            if (startBlock < 0) {
                startBlock = -(startBlock + 1);
            }
            int startIndexToFindAvailableRenderNode = 0;
            int i5 = Math.min(indexFirstChangedBlock2, startBlock);
            while (true) {
                if (i5 >= numberOfBlocks2) {
                    numberOfBlocks = numberOfBlocks2;
                    blockEndLines = blockEndLines2;
                    dynamicLayout = dynamicLayout2;
                    lastLine = lastLine2;
                    firstLine = firstLine2;
                    blockSet = blockSet2;
                    lastIndex = numberOfBlocks2;
                    break;
                }
                int blockIndex2 = blockIndices[i5];
                if (i5 >= indexFirstChangedBlock2 && blockIndex2 != -1) {
                    TextRenderNode[] textRenderNodeArr2 = editor.mTextRenderNodes;
                    if (textRenderNodeArr2[blockIndex2] != null) {
                        textRenderNodeArr2[blockIndex2].needsToBeShifted = z2;
                    }
                }
                if (blockEndLines2[i5] < firstLine2) {
                    z = z2;
                    i2 = i5;
                    numberOfBlocks = numberOfBlocks2;
                    blockEndLines = blockEndLines2;
                    dynamicLayout = dynamicLayout2;
                    lastLine = lastLine2;
                    firstLine = firstLine2;
                    lineRange = lineRange2;
                    blockSet = blockSet2;
                    indexFirstChangedBlock = indexFirstChangedBlock2;
                } else {
                    z = z2;
                    lineRange = lineRange2;
                    i2 = i5;
                    blockSet = blockSet2;
                    indexFirstChangedBlock = indexFirstChangedBlock2;
                    numberOfBlocks = numberOfBlocks2;
                    blockEndLines = blockEndLines2;
                    dynamicLayout = dynamicLayout2;
                    lastLine = lastLine2;
                    firstLine = firstLine2;
                    startIndexToFindAvailableRenderNode = drawHardwareAcceleratedInner(canvas, layout, highlight, highlightPaint, cursorOffsetVertical, blockEndLines2, blockIndices, i2, numberOfBlocks, startIndexToFindAvailableRenderNode);
                    if (blockEndLines[i2] >= lastLine) {
                        int lastIndex4 = Math.max(indexFirstChangedBlock, i2 + 1);
                        lastIndex = lastIndex4;
                        break;
                    }
                }
                i5 = i2 + 1;
                dynamicLayout2 = dynamicLayout;
                lastLine2 = lastLine;
                indexFirstChangedBlock2 = indexFirstChangedBlock;
                blockSet2 = blockSet;
                z2 = z;
                lineRange2 = lineRange;
                numberOfBlocks2 = numberOfBlocks;
                blockEndLines2 = blockEndLines;
                firstLine2 = firstLine;
            }
            if (blockSet == null) {
                lastIndex2 = lastIndex;
            } else {
                int i6 = 0;
                while (i6 < blockSet.size()) {
                    int block = blockSet.valueAt(i6).intValue();
                    int blockIndex3 = dynamicLayout.getBlockIndex(block);
                    if (blockIndex3 != -1) {
                        TextRenderNode[] textRenderNodeArr3 = editor.mTextRenderNodes;
                        if (textRenderNodeArr3[blockIndex3] != null && !textRenderNodeArr3[blockIndex3].needsToBeShifted) {
                            i = i6;
                            lastIndex3 = lastIndex;
                            i6 = i + 1;
                            lastIndex = lastIndex3;
                            editor = this;
                        }
                    }
                    i = i6;
                    int i7 = numberOfBlocks;
                    lastIndex3 = lastIndex;
                    int lastIndex5 = startIndexToFindAvailableRenderNode;
                    startIndexToFindAvailableRenderNode = drawHardwareAcceleratedInner(canvas, layout, highlight, highlightPaint, cursorOffsetVertical, blockEndLines, blockIndices, block, i7, lastIndex5);
                    i6 = i + 1;
                    lastIndex = lastIndex3;
                    editor = this;
                }
                lastIndex2 = lastIndex;
            }
            dynamicLayout.setIndexFirstChangedBlock(lastIndex2);
            return;
        }
        layout.drawText(canvas, firstLine2, lastLine2);
    }

    private int drawHardwareAcceleratedInner(Canvas canvas, Layout layout, Path highlight, Paint highlightPaint, int cursorOffsetVertical, int[] blockEndLines, int[] blockIndices, int blockInfoIndex, int numberOfBlocks, int startIndexToFindAvailableRenderNode) {
        int startIndexToFindAvailableRenderNode2;
        int blockIndex;
        int i;
        int line;
        boolean z;
        int blockEndLine = blockEndLines[blockInfoIndex];
        int blockIndex2 = blockIndices[blockInfoIndex];
        boolean blockIsInvalid = blockIndex2 == -1;
        if (blockIsInvalid) {
            int blockIndex3 = getAvailableDisplayListIndex(blockIndices, numberOfBlocks, startIndexToFindAvailableRenderNode);
            blockIndices[blockInfoIndex] = blockIndex3;
            TextRenderNode[] textRenderNodeArr = this.mTextRenderNodes;
            if (textRenderNodeArr[blockIndex3] != null) {
                textRenderNodeArr[blockIndex3].isDirty = true;
            }
            startIndexToFindAvailableRenderNode2 = blockIndex3 + 1;
            blockIndex = blockIndex3;
        } else {
            startIndexToFindAvailableRenderNode2 = startIndexToFindAvailableRenderNode;
            blockIndex = blockIndex2;
        }
        TextRenderNode[] textRenderNodeArr2 = this.mTextRenderNodes;
        if (textRenderNodeArr2[blockIndex] == null) {
            textRenderNodeArr2[blockIndex] = new TextRenderNode("Text " + blockIndex);
        }
        boolean blockDisplayListIsInvalid = this.mTextRenderNodes[blockIndex].needsRecord();
        RenderNode blockDisplayList = this.mTextRenderNodes[blockIndex].renderNode;
        if (this.mTextRenderNodes[blockIndex].needsToBeShifted || blockDisplayListIsInvalid) {
            if (blockInfoIndex != 0) {
                i = blockEndLines[blockInfoIndex - 1] + 1;
            } else {
                i = 0;
            }
            int blockBeginLine = i;
            int top = layout.getLineTop(blockBeginLine);
            int bottom = layout.getLineBottom(blockEndLine);
            int right = this.mTextView.getWidth();
            if (!this.mTextView.getHorizontallyScrolling()) {
                line = 0;
            } else {
                float min = Float.MAX_VALUE;
                float max = Float.MIN_VALUE;
                int line2 = blockBeginLine;
                while (line2 <= blockEndLine) {
                    min = Math.min(min, layout.getLineLeft(line2));
                    max = Math.max(max, layout.getLineRight(line2));
                    line2++;
                    blockIsInvalid = blockIsInvalid;
                }
                line = (int) min;
                right = (int) (LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS + max);
            }
            if (!blockDisplayListIsInvalid) {
                z = false;
            } else {
                RecordingCanvas recordingCanvas = blockDisplayList.beginRecording(right - line, bottom - top);
                try {
                    recordingCanvas.translate(-line, -top);
                    layout.drawText(recordingCanvas, blockBeginLine, blockEndLine);
                    this.mTextRenderNodes[blockIndex].isDirty = false;
                    blockDisplayList.endRecording();
                    blockDisplayList.setClipToBounds(false);
                    z = false;
                } catch (Throwable th) {
                    blockDisplayList.endRecording();
                    blockDisplayList.setClipToBounds(false);
                    throw th;
                }
            }
            blockDisplayList.setLeftTopRightBottom(line, top, right, bottom);
            this.mTextRenderNodes[blockIndex].needsToBeShifted = z;
        }
        ((RecordingCanvas) canvas).drawRenderNode(blockDisplayList);
        return startIndexToFindAvailableRenderNode2;
    }

    private int getAvailableDisplayListIndex(int[] blockIndices, int numberOfBlocks, int searchStartIndex) {
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

    private void drawCursor(Canvas canvas, int cursorOffsetVertical) {
        boolean translate = cursorOffsetVertical != 0;
        if (translate) {
            canvas.translate(0.0f, cursorOffsetVertical);
        }
        Drawable drawable = this.mDrawableForCursor;
        if (drawable != null) {
            drawable.draw(canvas);
        }
        if (translate) {
            canvas.translate(0.0f, -cursorOffsetVertical);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void invalidateHandlesAndActionMode() {
        SelectionModifierCursorController selectionModifierCursorController = this.mSelectionModifierCursorController;
        if (selectionModifierCursorController != null) {
            selectionModifierCursorController.invalidateHandles();
        }
        InsertionPointCursorController insertionPointCursorController = this.mInsertionPointCursorController;
        if (insertionPointCursorController != null) {
            insertionPointCursorController.invalidateHandle();
        }
        if (this.mTextActionMode != null) {
            invalidateActionMode();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void invalidateTextDisplayList(Layout layout, int start, int end) {
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
                if (blockEndLines[i] >= lastLine) {
                    return;
                }
                i++;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public void invalidateTextDisplayList() {
        if (this.mTextRenderNodes != null) {
            int i = 0;
            while (true) {
                TextRenderNode[] textRenderNodeArr = this.mTextRenderNodes;
                if (i < textRenderNodeArr.length) {
                    if (textRenderNodeArr[i] != null) {
                        textRenderNodeArr[i].isDirty = true;
                    }
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateCursorPosition() {
        loadCursorDrawable();
        if (this.mDrawableForCursor == null) {
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
    public void refreshTextActionMode() {
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
            lambda$startActionModeInternal$0$Editor();
        } else {
            ActionMode actionMode = this.mTextActionMode;
            if (actionMode != null) {
                actionMode.invalidateContentRect();
            }
        }
        this.mRestartActionModeOnNextRefresh = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startInsertionActionMode() {
        Runnable runnable = this.mInsertionActionModeRunnable;
        if (runnable != null) {
            this.mTextView.removeCallbacks(runnable);
        }
        if (extractedTextModeWillBeStarted()) {
            return;
        }
        lambda$startActionModeInternal$0$Editor();
        ActionMode.Callback actionModeCallback = new TextActionModeCallback(1);
        this.mTextActionMode = this.mTextView.startActionMode(actionModeCallback, 1);
        if (this.mTextActionMode != null && getInsertionController() != null) {
            getInsertionController().show();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TextView getTextView() {
        return this.mTextView;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ActionMode getTextActionMode() {
        return this.mTextActionMode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setRestartActionModeOnNextRefresh(boolean value) {
        this.mRestartActionModeOnNextRefresh = value;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startSelectionActionModeAsync(boolean adjustSelection) {
        getSelectionActionModeHelper().startSelectionActionModeAsync(adjustSelection);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startLinkActionModeAsync(int start, int end) {
        if (!(this.mTextView.getText() instanceof Spannable)) {
            return;
        }
        lambda$startActionModeInternal$0$Editor();
        this.mRequestingLinkActionMode = true;
        getSelectionActionModeHelper().startLinkActionModeAsync(start, end);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void invalidateActionModeAsync() {
        getSelectionActionModeHelper().invalidateActionModeAsync();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidateActionMode() {
        ActionMode actionMode = this.mTextActionMode;
        if (actionMode != null) {
            actionMode.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SelectionActionModeHelper getSelectionActionModeHelper() {
        if (this.mSelectionActionModeHelper == null) {
            this.mSelectionActionModeHelper = new SelectionActionModeHelper(this);
        }
        return this.mSelectionActionModeHelper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean selectCurrentWordAndStartDrag() {
        Runnable runnable = this.mInsertionActionModeRunnable;
        if (runnable != null) {
            this.mTextView.removeCallbacks(runnable);
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

    boolean checkField() {
        if (!this.mTextView.canSelectText() || !this.mTextView.requestFocus()) {
            Log.w("TextView", "TextView does not support text selection. Selection cancelled.");
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean startActionModeInternal(@TextActionMode int actionMode) {
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
            if (actionMode == 2 && !selectableText) {
                ActionMode actionMode2 = this.mTextActionMode;
                if (actionMode2 instanceof FloatingActionMode) {
                    ((FloatingActionMode) actionMode2).setOutsideTouchable(true, new PopupWindow.OnDismissListener() { // from class: android.widget.-$$Lambda$Editor$TdqUlJ6RRep0wXYHaRH51nTa08I
                        @Override // android.widget.PopupWindow.OnDismissListener
                        public final void onDismiss() {
                            Editor.this.lambda$startActionModeInternal$0$Editor();
                        }
                    });
                }
            }
            boolean selectionStarted = this.mTextActionMode != null;
            if (selectionStarted && this.mTextView.isTextEditable() && !this.mTextView.isTextSelectable() && this.mShowSoftInputOnFocus && (imm = getInputMethodManager()) != null) {
                imm.showSoftInput(this.mTextView, 0, null);
            }
            return selectionStarted;
        } else {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean extractedTextModeWillBeStarted() {
        InputMethodManager imm;
        return (this.mTextView.isInExtractedMode() || (imm = getInputMethodManager()) == null || !imm.isFullscreenMode()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldOfferToShowSuggestions() {
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
            int maxSpanEnd = 0;
            int unionOfSpansCoveringSelectionStartStart = this.mTextView.getText().length();
            int unionOfSpansCoveringSelectionStartEnd = 0;
            boolean hasValidSuggestions = false;
            for (int i = 0; i < suggestionSpans.length; i++) {
                int spanStart = spannable.getSpanStart(suggestionSpans[i]);
                int spanEnd = spannable.getSpanEnd(suggestionSpans[i]);
                minSpanStart = Math.min(minSpanStart, spanStart);
                maxSpanEnd = Math.max(maxSpanEnd, spanEnd);
                if (selectionStart >= spanStart && selectionStart <= spanEnd) {
                    boolean hasValidSuggestions2 = hasValidSuggestions || suggestionSpans[i].getSuggestions().length > 0;
                    unionOfSpansCoveringSelectionStartStart = Math.min(unionOfSpansCoveringSelectionStartStart, spanStart);
                    unionOfSpansCoveringSelectionStartEnd = Math.max(unionOfSpansCoveringSelectionStartEnd, spanEnd);
                    hasValidSuggestions = hasValidSuggestions2;
                }
            }
            return hasValidSuggestions && unionOfSpansCoveringSelectionStartStart < unionOfSpansCoveringSelectionStartEnd && minSpanStart >= unionOfSpansCoveringSelectionStartStart && maxSpanEnd <= unionOfSpansCoveringSelectionStartEnd;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCursorInsideEasyCorrectionSpan() {
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
    public void onTouchUpEvent(MotionEvent event) {
        if (getSelectionActionModeHelper().resetSelection(getTextView().getOffsetForPosition(event.getX(), event.getY()))) {
            return;
        }
        boolean selectAllGotFocus = this.mSelectAllOnFocus && this.mTextView.didTouchFocusSelect();
        hideCursorAndSpanControllers();
        lambda$startActionModeInternal$0$Editor();
        CharSequence text = this.mTextView.getText();
        if (!selectAllGotFocus && text.length() > 0) {
            int offset = this.mTextView.getOffsetForPosition(event.getX(), event.getY());
            boolean shouldInsertCursor = true ^ this.mRequestingLinkActionMode;
            if (shouldInsertCursor) {
                Selection.setSelection((Spannable) text, offset);
                SpellChecker spellChecker = this.mSpellChecker;
                if (spellChecker != null) {
                    spellChecker.onSelectionChanged();
                }
            }
            if (!extractedTextModeWillBeStarted()) {
                if (isCursorInsideEasyCorrectionSpan()) {
                    Runnable runnable = this.mInsertionActionModeRunnable;
                    if (runnable != null) {
                        this.mTextView.removeCallbacks(runnable);
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

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void onTextOperationUserChanged() {
        SpellChecker spellChecker = this.mSpellChecker;
        if (spellChecker != null) {
            spellChecker.resetSession();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: stopTextActionMode */
    public void lambda$startActionModeInternal$0$Editor() {
        ActionMode actionMode = this.mTextActionMode;
        if (actionMode != null) {
            actionMode.finish();
        }
    }

    private void stopTextActionModeWithPreservingSelection() {
        if (this.mTextActionMode != null) {
            this.mRestartActionModeOnNextRefresh = true;
        }
        this.mPreserveSelection = true;
        lambda$startActionModeInternal$0$Editor();
        this.mPreserveSelection = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasInsertionController() {
        return this.mInsertionControllerEnabled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasSelectionController() {
        return this.mSelectionControllerEnabled;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public InsertionPointCursorController getInsertionController() {
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
    public SelectionModifierCursorController getSelectionController() {
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
    public Drawable getCursorDrawable() {
        return this.mDrawableForCursor;
    }

    private void updateCursorPosition(int top, int bottom, float horizontal) {
        loadCursorDrawable();
        int left = clampHorizontalPosition(this.mDrawableForCursor, horizontal);
        int width = this.mDrawableForCursor.getIntrinsicWidth();
        this.mDrawableForCursor.setBounds(left, top - this.mTempRect.top, left + width, this.mTempRect.bottom + bottom);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int clampHorizontalPosition(Drawable drawable, float horizontal) {
        float horizontal2 = Math.max((float) LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS, horizontal - LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS);
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

    public void onCommitCorrection(CorrectionInfo info) {
        CorrectionHighlighter correctionHighlighter = this.mCorrectionHighlighter;
        if (correctionHighlighter == null) {
            this.mCorrectionHighlighter = new CorrectionHighlighter();
        } else {
            correctionHighlighter.invalidate(false);
        }
        this.mCorrectionHighlighter.highlight(info);
        this.mUndoInputFilter.freezeLastEdit();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onScrollChanged() {
        PositionListener positionListener = this.mPositionListener;
        if (positionListener != null) {
            positionListener.onScrollChanged();
        }
        ActionMode actionMode = this.mTextActionMode;
        if (actionMode != null) {
            actionMode.invalidateContentRect();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldBlink() {
        int start;
        int end;
        return isCursorVisible() && this.mTextView.isFocused() && (start = this.mTextView.getSelectionStart()) >= 0 && (end = this.mTextView.getSelectionEnd()) >= 0 && start == end;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void makeBlink() {
        if (shouldBlink()) {
            this.mShowCursor = SystemClock.uptimeMillis();
            if (this.mBlink == null) {
                this.mBlink = new Blink();
            }
            this.mTextView.removeCallbacks(this.mBlink);
            this.mTextView.postDelayed(this.mBlink, 500L);
            return;
        }
        Blink blink = this.mBlink;
        if (blink != null) {
            this.mTextView.removeCallbacks(blink);
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

        void cancel() {
            if (!this.mCancelled) {
                Editor.this.mTextView.removeCallbacks(this);
                this.mCancelled = true;
            }
        }

        void uncancel() {
            this.mCancelled = false;
        }
    }

    private View.DragShadowBuilder getTextThumbnailBuilder(int start, int end) {
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

        public DragLocalState(TextView sourceTextView, int start, int end) {
            this.sourceTextView = sourceTextView;
            this.start = start;
            this.end = end;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDrop(DragEvent event) {
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

    public void addSpanWatchers(Spannable text) {
        int textLength = text.length();
        KeyListener keyListener = this.mKeyListener;
        if (keyListener != null) {
            text.setSpan(keyListener, 0, textLength, 18);
        }
        if (this.mSpanController == null) {
            this.mSpanController = new SpanController();
        }
        text.setSpan(this.mSpanController, 0, textLength, 18);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setContextMenuAnchor(float x, float y) {
        this.mContextMenuAnchorX = x;
        this.mContextMenuAnchorY = y;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onCreateContextMenu(ContextMenu menu) {
        int offset;
        if (this.mIsBeingLongClicked || Float.isNaN(this.mContextMenuAnchorX) || Float.isNaN(this.mContextMenuAnchorY) || (offset = this.mTextView.getOffsetForPosition(this.mContextMenuAnchorX, this.mContextMenuAnchorY)) == -1) {
            return;
        }
        stopTextActionModeWithPreservingSelection();
        if (this.mTextView.canSelectText()) {
            boolean isOnSelection = this.mTextView.hasSelection() && offset >= this.mTextView.getSelectionStart() && offset <= this.mTextView.getSelectionEnd();
            if (!isOnSelection) {
                Selection.setSelection((Spannable) this.mTextView.getText(), offset);
                lambda$startActionModeInternal$0$Editor();
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
        menu.add(0, 16908338, 2, R.string.undo).setAlphabeticShortcut(DateFormat.TIME_ZONE).setOnMenuItemClickListener(this.mOnContextMenuItemClickListener).setEnabled(this.mTextView.canUndo());
        menu.add(0, 16908339, 3, R.string.redo).setOnMenuItemClickListener(this.mOnContextMenuItemClickListener).setEnabled(this.mTextView.canRedo());
        menu.add(0, 16908320, 4, 17039363).setAlphabeticShortcut(EpicenterTranslateClipReveal.StateProperty.TARGET_X).setOnMenuItemClickListener(this.mOnContextMenuItemClickListener).setEnabled(this.mTextView.canCut());
        menu.add(0, 16908321, 5, 17039361).setAlphabeticShortcut('c').setOnMenuItemClickListener(this.mOnContextMenuItemClickListener).setEnabled(this.mTextView.canCopy());
        menu.add(0, 16908322, 6, 17039371).setAlphabeticShortcut('v').setEnabled(this.mTextView.canPaste()).setOnMenuItemClickListener(this.mOnContextMenuItemClickListener);
        menu.add(0, 16908337, 11, 17039385).setEnabled(this.mTextView.canPasteAsPlainText()).setOnMenuItemClickListener(this.mOnContextMenuItemClickListener);
        menu.add(0, 16908341, 7, R.string.share).setEnabled(this.mTextView.canShare()).setOnMenuItemClickListener(this.mOnContextMenuItemClickListener);
        menu.add(0, 16908319, 8, 17039373).setAlphabeticShortcut(DateFormat.AM_PM).setEnabled(this.mTextView.canSelectAllText()).setOnMenuItemClickListener(this.mOnContextMenuItemClickListener);
        menu.add(0, 16908355, 10, 17039386).setEnabled(this.mTextView.canRequestAutofill()).setOnMenuItemClickListener(this.mOnContextMenuItemClickListener);
        this.mPreserveSelection = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SuggestionSpan findEquivalentSuggestionSpan(SuggestionSpanInfo suggestionSpanInfo) {
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
    public void replaceWithSuggestion(SuggestionInfo suggestionInfo) {
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
            int i2 = suggestionInfo.mSuggestionStart;
            int suggestionEnd = suggestionInfo.mSuggestionEnd;
            String suggestion = suggestionInfo.mText.subSequence(i2, suggestionEnd).toString();
            this.mTextView.replaceText_internal(spanStart2, spanEnd, suggestion);
            String[] suggestions = targetSuggestionSpan.getSuggestions();
            suggestions[suggestionInfo.mSuggestionIndex] = originalText2;
            int lengthDelta = suggestion.length() - (spanEnd - spanStart2);
            int i3 = 0;
            while (i3 < length2) {
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
                i3++;
                editable = editable2;
                spanStart2 = spanStart;
                originalText2 = originalText;
                length2 = length;
                suggestionSpans2 = suggestionSpans;
            }
            int i4 = spanEnd + lengthDelta;
            this.mTextView.setCursorPosition_internal(i4, i4);
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

        private boolean isNonIntermediateSelectionSpan(Spannable text, Object span) {
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
                Editor.this.mTextView.postDelayed(this.mHidePopup, 3000L);
            }
        }

        @Override // android.text.SpanWatcher
        public void onSpanRemoved(Spannable text, Object span, int start, int end) {
            if (isNonIntermediateSelectionSpan(text, span)) {
                Editor.this.sendUpdateSelection();
                return;
            }
            EasyEditPopupWindow easyEditPopupWindow = this.mPopupWindow;
            if (easyEditPopupWindow != null && span == easyEditPopupWindow.mEasyEditSpan) {
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

        public void hide() {
            EasyEditPopupWindow easyEditPopupWindow = this.mPopupWindow;
            if (easyEditPopupWindow != null) {
                easyEditPopupWindow.hide();
                Editor.this.mTextView.removeCallbacks(this.mHidePopup);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void sendEasySpanNotification(int textChangedType, EasyEditSpan span) {
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
        private static final int POPUP_TEXT_LAYOUT = 17367313;
        private TextView mDeleteTextView;
        private EasyEditSpan mEasyEditSpan;
        private EasyEditDeleteListener mOnDeleteListener;

        private EasyEditPopupWindow() {
            super();
        }

        @Override // android.widget.Editor.PinnedPopupWindow
        protected void createPopupWindow() {
            this.mPopupWindow = new PopupWindow(Editor.this.mTextView.getContext(), (AttributeSet) null, 16843464);
            this.mPopupWindow.setInputMethodMode(2);
            this.mPopupWindow.setClippingEnabled(true);
        }

        @Override // android.widget.Editor.PinnedPopupWindow
        protected void initContentView() {
            LinearLayout linearLayout = new LinearLayout(Editor.this.mTextView.getContext());
            linearLayout.setOrientation(0);
            this.mContentView = linearLayout;
            this.mContentView.setBackgroundResource(R.drawable.text_edit_side_paste_window);
            LayoutInflater inflater = (LayoutInflater) Editor.this.mTextView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewGroup.LayoutParams wrapContent = new ViewGroup.LayoutParams(-2, -2);
            this.mDeleteTextView = (TextView) inflater.inflate(17367313, (ViewGroup) null);
            this.mDeleteTextView.setLayoutParams(wrapContent);
            this.mDeleteTextView.setText(R.string.delete);
            this.mDeleteTextView.setOnClickListener(this);
            this.mContentView.addView(this.mDeleteTextView);
        }

        public void setEasyEditSpan(EasyEditSpan easyEditSpan) {
            this.mEasyEditSpan = easyEditSpan;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setOnDeleteListener(EasyEditDeleteListener listener) {
            this.mOnDeleteListener = listener;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            EasyEditSpan easyEditSpan;
            EasyEditDeleteListener easyEditDeleteListener;
            if (view == this.mDeleteTextView && (easyEditSpan = this.mEasyEditSpan) != null && easyEditSpan.isDeleteEnabled() && (easyEditDeleteListener = this.mOnDeleteListener) != null) {
                easyEditDeleteListener.onDeleteClick(this.mEasyEditSpan);
            }
        }

        @Override // android.widget.Editor.PinnedPopupWindow
        public void hide() {
            EasyEditSpan easyEditSpan = this.mEasyEditSpan;
            if (easyEditSpan != null) {
                easyEditSpan.setDeleteEnabled(false);
            }
            this.mOnDeleteListener = null;
            super.hide();
        }

        @Override // android.widget.Editor.PinnedPopupWindow
        protected int getTextOffset() {
            Editable editable = (Editable) Editor.this.mTextView.getText();
            return editable.getSpanEnd(this.mEasyEditSpan);
        }

        @Override // android.widget.Editor.PinnedPopupWindow
        protected int getVerticalLocalPosition(int line) {
            Layout layout = Editor.this.mTextView.getLayout();
            return layout.getLineBottomWithoutSpacing(line);
        }

        @Override // android.widget.Editor.PinnedPopupWindow
        protected int clipVertically(int positionY) {
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

        public void addSubscriber(TextViewPositionListener positionListener, boolean canMove) {
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

        public void removeSubscriber(TextViewPositionListener positionListener) {
            int i = 0;
            while (true) {
                if (i >= 7) {
                    break;
                }
                TextViewPositionListener[] textViewPositionListenerArr = this.mPositionListeners;
                if (textViewPositionListenerArr[i] != positionListener) {
                    i++;
                } else {
                    textViewPositionListenerArr[i] = null;
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

        public int getPositionX() {
            return this.mPositionX;
        }

        public int getPositionY() {
            return this.mPositionY;
        }

        public int getPositionXOnScreen() {
            return this.mPositionXOnScreen;
        }

        public int getPositionYOnScreen() {
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

        private void updatePosition() {
            Editor.this.mTextView.getLocationInWindow(this.mTempCoords);
            int[] iArr = this.mTempCoords;
            this.mPositionHasChanged = (iArr[0] == this.mPositionX && iArr[1] == this.mPositionY) ? false : true;
            int[] iArr2 = this.mTempCoords;
            this.mPositionX = iArr2[0];
            this.mPositionY = iArr2[1];
            Editor.this.mTextView.getLocationOnScreen(this.mTempCoords);
            int[] iArr3 = this.mTempCoords;
            this.mPositionXOnScreen = iArr3[0];
            this.mPositionYOnScreen = iArr3[1];
        }

        public void onScrollChanged() {
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

        protected abstract int clipVertically(int i);

        protected abstract void createPopupWindow();

        protected abstract int getTextOffset();

        protected abstract int getVerticalLocalPosition(int i);

        protected abstract void initContentView();

        protected void setUp() {
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

        public void show() {
            Editor.this.getPositionListener().addSubscriber(this, false);
            computeLocalPosition();
            PositionListener positionListener = Editor.this.getPositionListener();
            updatePosition(positionListener.getPositionX(), positionListener.getPositionY());
        }

        protected void measureContent() {
            DisplayMetrics displayMetrics = Editor.this.mTextView.getResources().getDisplayMetrics();
            this.mContentView.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, Integer.MIN_VALUE));
        }

        private void computeLocalPosition() {
            measureContent();
            int width = this.mContentView.getMeasuredWidth();
            int offset = getTextOffset();
            this.mPositionX = (int) (Editor.this.mTextView.getLayout().getPrimaryHorizontal(offset) - (width / 2.0f));
            this.mPositionX += Editor.this.mTextView.viewportToContentHorizontalOffset();
            int line = Editor.this.mTextView.getLayout().getLineForOffset(offset);
            this.mPositionY = getVerticalLocalPosition(line);
            this.mPositionY += Editor.this.mTextView.viewportToContentVerticalOffset();
        }

        private void updatePosition(int parentPositionX, int parentPositionY) {
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

        public void hide() {
            if (!isShowing()) {
                return;
            }
            this.mPopupWindow.dismiss();
            Editor.this.getPositionListener().removeSubscriber(this);
        }

        @Override // android.widget.Editor.TextViewPositionListener
        public void updatePosition(int parentPositionX, int parentPositionY, boolean parentPositionChanged, boolean parentScrolled) {
            if (isShowing() && Editor.this.isOffsetVisible(getTextOffset())) {
                if (parentScrolled) {
                    computeLocalPosition();
                }
                updatePosition(parentPositionX, parentPositionY);
                return;
            }
            hide();
        }

        public boolean isShowing() {
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

        private SuggestionInfo() {
            this.mSuggestionSpanInfo = new SuggestionSpanInfo();
            this.mText = new SpannableStringBuilder();
        }

        void clear() {
            this.mSuggestionSpanInfo.clear();
            this.mText.clear();
        }

        void setSpanInfo(SuggestionSpan span, int spanStart, int spanEnd) {
            SuggestionSpanInfo suggestionSpanInfo = this.mSuggestionSpanInfo;
            suggestionSpanInfo.mSuggestionSpan = span;
            suggestionSpanInfo.mSpanStart = spanStart;
            suggestionSpanInfo.mSpanEnd = spanEnd;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class SuggestionSpanInfo {
        int mSpanEnd;
        int mSpanStart;
        SuggestionSpan mSuggestionSpan;

        private SuggestionSpanInfo() {
        }

        void clear() {
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
            public int compare(SuggestionSpan span1, SuggestionSpan span2) {
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

        private SuggestionSpan[] getSortedSuggestionSpans() {
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

        public int getSuggestionInfo(SuggestionInfo[] suggestionInfos, SuggestionSpanInfo misspelledSpanInfo) {
            Spannable spannable;
            SuggestionSpan[] suggestionSpans;
            boolean z;
            SuggestionSpanInfo suggestionSpanInfo = misspelledSpanInfo;
            Spannable spannable2 = (Spannable) Editor.this.mTextView.getText();
            SuggestionSpan[] suggestionSpans2 = getSortedSuggestionSpans();
            int nbSpans = suggestionSpans2.length;
            boolean z2 = false;
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
                int suggestionIndex = 0;
                while (suggestionIndex < nbSuggestions) {
                    String suggestion = suggestions[suggestionIndex];
                    int i = 0;
                    while (true) {
                        if (i < numberOfSuggestions) {
                            SuggestionInfo otherSuggestionInfo = suggestionInfos[i];
                            spannable = spannable2;
                            Spannable spannable3 = otherSuggestionInfo.mText;
                            if (!spannable3.toString().equals(suggestion)) {
                                suggestionSpans = suggestionSpans2;
                            } else {
                                int otherSpanStart = otherSuggestionInfo.mSuggestionSpanInfo.mSpanStart;
                                suggestionSpans = suggestionSpans2;
                                int otherSpanEnd = otherSuggestionInfo.mSuggestionSpanInfo.mSpanEnd;
                                if (spanStart == otherSpanStart && spanEnd == otherSpanEnd) {
                                    z = false;
                                    break;
                                }
                            }
                            i++;
                            spannable2 = spannable;
                            suggestionSpans2 = suggestionSpans;
                        } else {
                            spannable = spannable2;
                            suggestionSpans = suggestionSpans2;
                            SuggestionInfo suggestionInfo = suggestionInfos[numberOfSuggestions];
                            suggestionInfo.setSpanInfo(suggestionSpan, spanStart, spanEnd);
                            suggestionInfo.mSuggestionIndex = suggestionIndex;
                            z = false;
                            suggestionInfo.mSuggestionStart = 0;
                            suggestionInfo.mSuggestionEnd = suggestion.length();
                            suggestionInfo.mText.replace(0, suggestionInfo.mText.length(), (CharSequence) suggestion);
                            numberOfSuggestions++;
                            if (numberOfSuggestions >= suggestionInfos.length) {
                                return numberOfSuggestions;
                            }
                        }
                    }
                    suggestionIndex++;
                    z2 = z;
                    spannable2 = spannable;
                    suggestionSpans2 = suggestionSpans;
                }
                numberOfSuggestions2++;
                suggestionSpanInfo = misspelledSpanInfo;
            }
            return numberOfSuggestions;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public final class SuggestionsPopupWindow extends PinnedPopupWindow implements AdapterView.OnItemClickListener {
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
        protected void setUp() {
            this.mContext = applyDefaultTheme(Editor.this.mTextView.getContext());
            this.mHighlightSpan = new TextAppearanceSpan(this.mContext, Editor.this.mTextView.mTextEditSuggestionHighlightStyle);
        }

        private Context applyDefaultTheme(Context originalContext) {
            TypedArray a = originalContext.obtainStyledAttributes(new int[]{16844176});
            boolean isLightTheme = a.getBoolean(0, true);
            int themeId = isLightTheme ? 16974410 : 16974411;
            a.recycle();
            return new ContextThemeWrapper(originalContext, themeId);
        }

        @Override // android.widget.Editor.PinnedPopupWindow
        protected void createPopupWindow() {
            this.mPopupWindow = new CustomPopupWindow();
            this.mPopupWindow.setInputMethodMode(2);
            this.mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
            this.mPopupWindow.setFocusable(true);
            this.mPopupWindow.setClippingEnabled(false);
        }

        @Override // android.widget.Editor.PinnedPopupWindow
        protected void initContentView() {
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
            int i = 0;
            while (true) {
                SuggestionInfo[] suggestionInfoArr = this.mSuggestionInfos;
                if (i < suggestionInfoArr.length) {
                    suggestionInfoArr[i] = new SuggestionInfo();
                    i++;
                } else {
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
                                Intent intent = new Intent(Settings.ACTION_USER_DICTIONARY_INSERT);
                                intent.putExtra("word", originalText);
                                intent.putExtra("locale", Editor.this.mTextView.getTextServicesLocale().toString());
                                intent.setFlags(intent.getFlags() | 268435456);
                                Editor.this.mTextView.startActivityAsTextOperationUserIfNecessary(intent);
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
                    return;
                }
            }
        }

        public boolean isShowingUp() {
            return this.mIsShowingUp;
        }

        public void onParentLostFocus() {
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

        @Override // android.widget.Editor.PinnedPopupWindow
        public void show() {
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
        protected void measureContent() {
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
        protected int getTextOffset() {
            return (Editor.this.mTextView.getSelectionStart() + Editor.this.mTextView.getSelectionStart()) / 2;
        }

        @Override // android.widget.Editor.PinnedPopupWindow
        protected int getVerticalLocalPosition(int line) {
            Layout layout = Editor.this.mTextView.getLayout();
            return layout.getLineBottomWithoutSpacing(line) - this.mContainerMarginTop;
        }

        @Override // android.widget.Editor.PinnedPopupWindow
        protected int clipVertically(int positionY) {
            int height = this.mContentView.getMeasuredHeight();
            DisplayMetrics displayMetrics = Editor.this.mTextView.getResources().getDisplayMetrics();
            return Math.min(positionY, displayMetrics.heightPixels - height);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void hideWithCleanUp() {
            SuggestionInfo[] suggestionInfoArr;
            for (SuggestionInfo info : this.mSuggestionInfos) {
                info.clear();
            }
            this.mMisspelledSpanInfo.clear();
            hide();
        }

        private boolean updateSuggestions() {
            int underlineColor;
            Spannable spannable = (Spannable) Editor.this.mTextView.getText();
            this.mNumberOfSuggestions = Editor.this.mSuggestionHelper.getSuggestionInfo(this.mSuggestionInfos, this.mMisspelledSpanInfo);
            if (this.mNumberOfSuggestions == 0 && this.mMisspelledSpanInfo.mSuggestionSpan == null) {
                return false;
            }
            int spanUnionStart = Editor.this.mTextView.getText().length();
            int spanUnionEnd = 0;
            for (int i = 0; i < this.mNumberOfSuggestions; i++) {
                SuggestionSpanInfo spanInfo = this.mSuggestionInfos[i].mSuggestionSpanInfo;
                spanUnionStart = Math.min(spanUnionStart, spanInfo.mSpanStart);
                spanUnionEnd = Math.max(spanUnionEnd, spanInfo.mSpanEnd);
            }
            if (this.mMisspelledSpanInfo.mSuggestionSpan != null) {
                spanUnionStart = Math.min(spanUnionStart, this.mMisspelledSpanInfo.mSpanStart);
                spanUnionEnd = Math.max(spanUnionEnd, this.mMisspelledSpanInfo.mSpanEnd);
            }
            for (int i2 = 0; i2 < this.mNumberOfSuggestions; i2++) {
                highlightTextDifferences(this.mSuggestionInfos[i2], spanUnionStart, spanUnionEnd);
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
            spannable.setSpan(Editor.this.mSuggestionRangeSpan, spanUnionStart, spanUnionEnd, 33);
            this.mSuggestionsAdapter.notifyDataSetChanged();
            return true;
        }

        private void highlightTextDifferences(SuggestionInfo suggestionInfo, int unionStart, int unionEnd) {
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
                Editor.this.loadHandleDrawables(false);
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

        private ActionMode.Callback getCustomCallback() {
            if (this.mHasSelection) {
                return Editor.this.mCustomSelectionActionModeCallback;
            }
            return Editor.this.mCustomInsertionActionModeCallback;
        }

        private void populateMenuWithItems(Menu menu) {
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
                menu.add(0, 16908341, 7, R.string.share).setShowAsAction(1);
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

        private void updateSelectAllItem(Menu menu) {
            boolean canSelectAll = Editor.this.mTextView.canSelectAllText();
            boolean selectAllItemExists = menu.findItem(16908319) != null;
            if (canSelectAll && !selectAllItemExists) {
                menu.add(0, 16908319, 8, 17039373).setShowAsAction(1);
            } else if (!canSelectAll && selectAllItemExists) {
                menu.removeItem(16908319);
            }
        }

        private void updateReplaceItem(Menu menu) {
            boolean canReplace = Editor.this.mTextView.isSuggestionsEnabled() && Editor.this.shouldOfferToShowSuggestions();
            boolean replaceItemExists = menu.findItem(16908340) != null;
            if (canReplace && !replaceItemExists) {
                menu.add(0, 16908340, 9, R.string.replace).setShowAsAction(1);
            } else if (!canReplace && replaceItemExists) {
                menu.removeItem(16908340);
            }
        }

        private void updateAssistMenuItems(Menu menu) {
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
                addAssistMenuItem(menu, textClassification.getActions().get(i), 0, (i + 50) - 1, 0);
            }
        }

        private MenuItem addAssistMenuItem(Menu menu, RemoteAction action, int itemId, int order, int showAsAction) {
            MenuItem item = menu.add(16908353, itemId, order, action.getTitle()).setContentDescription(action.getContentDescription());
            if (action.shouldShowIcon()) {
                item.setIcon(action.getIcon().loadDrawable(Editor.this.mTextView.getContext()));
            }
            item.setShowAsAction(showAsAction);
            this.mAssistClickHandlers.put(item, TextClassification.createIntentOnClickListener(action.getActionIntent()));
            return item;
        }

        private void clearAssistMenuItems(Menu menu) {
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

        private boolean hasLegacyAssistItem(TextClassification classification) {
            return ((classification.getIcon() == null && TextUtils.isEmpty(classification.getLabel())) || (classification.getIntent() == null && classification.getOnClickListener() == null)) ? false : true;
        }

        private boolean onAssistMenuItemClicked(MenuItem assistMenuItem) {
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
                Editor.this.lambda$startActionModeInternal$0$Editor();
            }
            return true;
        }

        private int createAssistMenuItemPendingIntentRequestCode() {
            if (Editor.this.mTextView.hasSelection()) {
                return Editor.this.mTextView.getText().subSequence(Editor.this.mTextView.getSelectionStart(), Editor.this.mTextView.getSelectionEnd()).hashCode();
            }
            return 0;
        }

        private boolean shouldEnableAssistMenuItems() {
            return Editor.this.mTextView.isDeviceProvisioned() && TextClassificationManager.getSettings(Editor.this.mTextView.getContext()).isSmartTextShareEnabled();
        }

        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Editor.this.getSelectionActionModeHelper().onSelectionAction(item.getItemId(), item.getTitle().toString());
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
                    Editor editor = Editor.this;
                    float primaryHorizontal = editor.clampHorizontalPosition(null, layout.getPrimaryHorizontal(editor.mTextView.getSelectionStart()));
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
        public void updatePosition(int parentPositionX, int parentPositionY, boolean parentPositionChanged, boolean parentScrolled) {
            InputMethodManager imm;
            Layout layout;
            int insertionMarkerFlags;
            int composingTextStart;
            int temp;
            InputMethodState ims = Editor.this.mInputMethodState;
            if (ims == null || ims.mBatchEditNesting > 0 || (imm = Editor.this.getInputMethodManager()) == null || !imm.isActive(Editor.this.mTextView) || !imm.isCursorAnchorInfoEnabled() || (layout = Editor.this.mTextView.getLayout()) == null) {
                return;
            }
            CursorAnchorInfo.Builder builder = this.mSelectionInfoBuilder;
            builder.reset();
            int selectionStart = Editor.this.mTextView.getSelectionStart();
            builder.setSelectionRange(selectionStart, Editor.this.mTextView.getSelectionEnd());
            this.mViewToScreenMatrix.set(Editor.this.mTextView.getMatrix());
            Editor.this.mTextView.getLocationOnScreen(this.mTmpIntOffset);
            Matrix matrix = this.mViewToScreenMatrix;
            int[] iArr = this.mTmpIntOffset;
            boolean z = false;
            matrix.postTranslate(iArr[0], iArr[1]);
            builder.setMatrix(this.mViewToScreenMatrix);
            float viewportToContentHorizontalOffset = Editor.this.mTextView.viewportToContentHorizontalOffset();
            float viewportToContentVerticalOffset = Editor.this.mTextView.viewportToContentVerticalOffset();
            CharSequence text = Editor.this.mTextView.getText();
            if (text instanceof Spannable) {
                Spannable sp = (Spannable) text;
                int composingTextStart2 = EditableInputConnection.getComposingSpanStart(sp);
                int composingTextEnd = EditableInputConnection.getComposingSpanEnd(sp);
                if (composingTextEnd >= composingTextStart2) {
                    composingTextStart = composingTextStart2;
                    temp = composingTextEnd;
                } else {
                    composingTextStart = composingTextEnd;
                    temp = composingTextStart2;
                }
                if (composingTextStart >= 0 && composingTextStart < temp) {
                    z = true;
                }
                boolean hasComposingText = z;
                if (hasComposingText) {
                    CharSequence composingText = text.subSequence(composingTextStart, temp);
                    builder.setComposingText(composingTextStart, composingText);
                    Editor.this.mTextView.populateCharacterBounds(builder, composingTextStart, temp, viewportToContentHorizontalOffset, viewportToContentVerticalOffset);
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
                int insertionMarkerFlags2 = 0;
                if (isTopVisible || isBottomVisible) {
                    insertionMarkerFlags2 = 0 | 1;
                }
                if (!isTopVisible || !isBottomVisible) {
                    insertionMarkerFlags2 |= 2;
                }
                if (!layout.isRtlCharAt(selectionStart)) {
                    insertionMarkerFlags = insertionMarkerFlags2;
                } else {
                    insertionMarkerFlags = insertionMarkerFlags2 | 4;
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

        private MagnifierMotionAnimator(Magnifier magnifier) {
            this.mMagnifier = magnifier;
            this.mAnimator.setDuration(100L);
            this.mAnimator.setInterpolator(new LinearInterpolator());
            this.mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: android.widget.-$$Lambda$Editor$MagnifierMotionAnimator$E-RaelOMgCHAzvKgSSZE-hDYeIg
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Editor.MagnifierMotionAnimator.this.lambda$new$0$Editor$MagnifierMotionAnimator(valueAnimator);
                }
            });
        }

        public /* synthetic */ void lambda$new$0$Editor$MagnifierMotionAnimator(ValueAnimator animation) {
            float f = this.mAnimationStartX;
            this.mAnimationCurrentX = f + ((this.mLastX - f) * animation.getAnimatedFraction());
            float f2 = this.mAnimationStartY;
            this.mAnimationCurrentY = f2 + ((this.mLastY - f2) * animation.getAnimatedFraction());
            this.mMagnifier.show(this.mAnimationCurrentX, this.mAnimationCurrentY);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void show(float x, float y) {
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
        public void update() {
            this.mMagnifier.update();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dismiss() {
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
        private float mCurrentDragInitialTouchRawX;
        protected Drawable mDrawable;
        protected Drawable mDrawableLtr;
        protected Drawable mDrawableRtl;
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
        private float mTextViewScaleX;
        private float mTextViewScaleY;
        private float mTouchOffsetY;
        private float mTouchToWindowOffsetX;
        private float mTouchToWindowOffsetY;

        public abstract int getCurrentCursorOffset();

        protected abstract int getHorizontalGravity(boolean z);

        protected abstract int getHotspotX(Drawable drawable, boolean z);

        protected abstract int getMagnifierHandleTrigger();

        protected abstract void updatePosition(float f, float f2, boolean z);

        protected abstract void updateSelection(int i);

        private HandleView(Drawable drawableLtr, Drawable drawableRtl, int id) {
            super(Editor.this.mTextView.getContext());
            this.mPreviousOffset = -1;
            this.mPositionHasChanged = true;
            this.mPrevLine = -1;
            this.mPreviousLineTouched = -1;
            this.mCurrentDragInitialTouchRawX = -1.0f;
            this.mPreviousOffsetsTimes = new long[5];
            this.mPreviousOffsets = new int[5];
            this.mPreviousOffsetIndex = 0;
            this.mNumberPreviousOffsets = 0;
            setId(id);
            this.mContainer = new PopupWindow(Editor.this.mTextView.getContext(), (AttributeSet) null, 16843464);
            this.mContainer.setSplitTouchEnabled(true);
            this.mContainer.setClippingEnabled(false);
            this.mContainer.setWindowLayoutType(1002);
            this.mContainer.setWidth(-2);
            this.mContainer.setHeight(-2);
            this.mContainer.setContentView(this);
            setDrawables(drawableLtr, drawableRtl);
            this.mMinSize = Editor.this.mTextView.getContext().getResources().getDimensionPixelSize(R.dimen.text_handle_min_size);
            int handleHeight = getPreferredHeight();
            this.mTouchOffsetY = handleHeight * (-0.3f);
            this.mIdealVerticalOffset = handleHeight * 0.7f;
        }

        public float getIdealVerticalOffset() {
            return this.mIdealVerticalOffset;
        }

        void setDrawables(Drawable drawableLtr, Drawable drawableRtl) {
            this.mDrawableLtr = drawableLtr;
            this.mDrawableRtl = drawableRtl;
            updateDrawable(true);
        }

        protected void updateDrawable(boolean updateDrawableWhenDragging) {
            Layout layout;
            if ((!updateDrawableWhenDragging && this.mIsDragging) || (layout = Editor.this.mTextView.getLayout()) == null) {
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

        private void startTouchUpFilter(int offset) {
            this.mNumberPreviousOffsets = 0;
            addPositionToTouchUpFilter(offset);
        }

        private void addPositionToTouchUpFilter(int offset) {
            this.mPreviousOffsetIndex = (this.mPreviousOffsetIndex + 1) % 5;
            int[] iArr = this.mPreviousOffsets;
            int i = this.mPreviousOffsetIndex;
            iArr[i] = offset;
            this.mPreviousOffsetsTimes[i] = SystemClock.uptimeMillis();
            this.mNumberPreviousOffsets++;
        }

        private void filterOnTouchUp(boolean fromTouchScreen) {
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

        public boolean offsetHasBeenChanged() {
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

        private int getPreferredWidth() {
            return Math.max(this.mDrawable.getIntrinsicWidth(), this.mMinSize);
        }

        private int getPreferredHeight() {
            return Math.max(this.mDrawable.getIntrinsicHeight(), this.mMinSize);
        }

        public void show() {
            if (isShowing()) {
                return;
            }
            Editor.this.getPositionListener().addSubscriber(this, true);
            this.mPreviousOffset = -1;
            positionAtCursorOffset(getCurrentCursorOffset(), false, false);
        }

        protected void dismiss() {
            this.mIsDragging = false;
            this.mContainer.dismiss();
            onDetached();
        }

        public void hide() {
            dismiss();
            Editor.this.getPositionListener().removeSubscriber(this);
        }

        public boolean isShowing() {
            return this.mContainer.isShowing();
        }

        private boolean shouldShow() {
            if (!this.mIsDragging) {
                if (Editor.this.mTextView.isInBatchEditMode()) {
                    return false;
                }
                return Editor.this.mTextView.isPositionVisible(this.mPositionX + this.mHotspotX + getHorizontalOffset(), this.mPositionY);
            }
            return true;
        }

        private void setVisible(boolean visible) {
            this.mContainer.getContentView().setVisibility(visible ? 0 : 4);
        }

        protected boolean isAtRtlRun(Layout layout, int offset) {
            return layout.isRtlCharAt(offset);
        }

        @VisibleForTesting
        public float getHorizontal(Layout layout, int offset) {
            return layout.getPrimaryHorizontal(offset);
        }

        protected int getOffsetAtCoordinate(Layout layout, int line, float x) {
            return Editor.this.mTextView.getOffsetAtCoordinate(line, x);
        }

        protected void positionAtCursorOffset(int offset, boolean forceUpdatePosition, boolean fromTouchScreen) {
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

        int getCursorHorizontalPosition(Layout layout, int offset) {
            return (int) (getHorizontal(layout, offset) - Editor.LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS);
        }

        @Override // android.widget.Editor.TextViewPositionListener
        public void updatePosition(int parentPositionX, int parentPositionY, boolean parentPositionChanged, boolean parentScrolled) {
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
            Drawable drawable = this.mDrawable;
            drawable.setBounds(left, 0, left + drawWidth, drawable.getIntrinsicHeight());
            this.mDrawable.draw(c);
        }

        private int getHorizontalOffset() {
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

        protected int getCursorOffset() {
            return 0;
        }

        private boolean tooLargeTextForMagnifier() {
            float magnifierContentHeight = Math.round(Editor.this.mMagnifierAnimator.mMagnifier.getHeight() / Editor.this.mMagnifierAnimator.mMagnifier.getZoom());
            Paint.FontMetrics fontMetrics = Editor.this.mTextView.getPaint().getFontMetrics();
            float glyphHeight = fontMetrics.descent - fontMetrics.ascent;
            return this.mTextViewScaleY * glyphHeight > magnifierContentHeight;
        }

        private boolean checkForTransforms() {
            if (Editor.this.mMagnifierAnimator.mMagnifierIsShowing) {
                return true;
            }
            if (Editor.this.mTextView.getRotation() == 0.0f && Editor.this.mTextView.getRotationX() == 0.0f && Editor.this.mTextView.getRotationY() == 0.0f) {
                this.mTextViewScaleX = Editor.this.mTextView.getScaleX();
                this.mTextViewScaleY = Editor.this.mTextView.getScaleY();
                for (ViewParent viewParent = Editor.this.mTextView.getParent(); viewParent != null; viewParent = viewParent.getParent()) {
                    if (viewParent instanceof View) {
                        View view = (View) viewParent;
                        if (view.getRotation() != 0.0f || view.getRotationX() != 0.0f || view.getRotationY() != 0.0f) {
                            return false;
                        }
                        this.mTextViewScaleX *= view.getScaleX();
                        this.mTextViewScaleY *= view.getScaleY();
                    }
                }
                return true;
            }
            return false;
        }

        /* JADX WARN: Removed duplicated region for block: B:38:0x00ee  */
        /* JADX WARN: Removed duplicated region for block: B:46:0x0117  */
        /* JADX WARN: Removed duplicated region for block: B:55:0x016f  */
        /* JADX WARN: Removed duplicated region for block: B:60:0x0182  */
        /* JADX WARN: Removed duplicated region for block: B:61:0x0186  */
        /* JADX WARN: Removed duplicated region for block: B:64:0x01e2 A[ADDED_TO_REGION] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private boolean obtainMagnifierShowCoordinates(android.view.MotionEvent r20, android.graphics.PointF r21) {
            /*
                Method dump skipped, instructions count: 486
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: android.widget.Editor.HandleView.obtainMagnifierShowCoordinates(android.view.MotionEvent, android.graphics.PointF):boolean");
        }

        private boolean handleOverlapsMagnifier(HandleView handle, Rect magnifierRect) {
            PopupWindow window = handle.mContainer;
            if (!window.hasDecorView()) {
                return false;
            }
            Rect handleRect = new Rect(window.getDecorViewLayoutParams().x, window.getDecorViewLayoutParams().y, window.getDecorViewLayoutParams().x + window.getContentView().getWidth(), window.getDecorViewLayoutParams().y + window.getContentView().getHeight());
            return Rect.intersects(handleRect, magnifierRect);
        }

        private HandleView getOtherSelectionHandle() {
            SelectionModifierCursorController controller = Editor.this.getSelectionController();
            if (controller == null || !controller.isActive()) {
                return null;
            }
            return controller.mStartHandle != this ? controller.mStartHandle : controller.mEndHandle;
        }

        private void updateHandlesVisibility() {
            Point magnifierTopLeft = Editor.this.mMagnifierAnimator.mMagnifier.getPosition();
            if (magnifierTopLeft == null) {
                return;
            }
            Rect magnifierRect = new Rect(magnifierTopLeft.x, magnifierTopLeft.y, magnifierTopLeft.x + Editor.this.mMagnifierAnimator.mMagnifier.getWidth(), magnifierTopLeft.y + Editor.this.mMagnifierAnimator.mMagnifier.getHeight());
            setVisible(!handleOverlapsMagnifier(this, magnifierRect));
            HandleView otherHandle = getOtherSelectionHandle();
            if (otherHandle != null) {
                otherHandle.setVisible(!handleOverlapsMagnifier(otherHandle, magnifierRect));
            }
        }

        protected final void updateMagnifier(MotionEvent event) {
            if (Editor.this.mMagnifierAnimator == null) {
                return;
            }
            PointF showPosInView = new PointF();
            boolean shouldShow = checkForTransforms() && !tooLargeTextForMagnifier() && obtainMagnifierShowCoordinates(event, showPosInView);
            if (shouldShow) {
                Editor.this.mRenderCursorRegardlessTiming = true;
                Editor.this.mTextView.invalidateCursorPath();
                Editor.this.suspendBlink();
                Editor.this.mMagnifierAnimator.show(showPosInView.x, showPosInView.y);
                updateHandlesVisibility();
                return;
            }
            dismissMagnifier();
        }

        protected final void dismissMagnifier() {
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

        /* JADX WARN: Code restructure failed: missing block: B:9:0x0014, code lost:
            if (r0 != 3) goto L10;
         */
        @Override // android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public boolean onTouchEvent(android.view.MotionEvent r11) {
            /*
                Method dump skipped, instructions count: 214
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: android.widget.Editor.HandleView.onTouchEvent(android.view.MotionEvent):boolean");
        }

        public boolean isDragging() {
            return this.mIsDragging;
        }

        void onHandleMoved() {
        }

        public void onDetached() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.view.View
        public void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            setSystemGestureExclusionRects(Collections.singletonList(new Rect(0, 0, w, h)));
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
        public void show() {
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

        private void hideAfterDelay() {
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

        private void removeHiderCallback() {
            if (this.mHider != null) {
                Editor.this.mTextView.removeCallbacks(this.mHider);
            }
        }

        @Override // android.widget.Editor.HandleView
        protected int getHotspotX(Drawable drawable, boolean isRtlRun) {
            return drawable.getIntrinsicWidth() / 2;
        }

        @Override // android.widget.Editor.HandleView
        protected int getHorizontalGravity(boolean isRtlRun) {
            return 1;
        }

        @Override // android.widget.Editor.HandleView
        protected int getCursorOffset() {
            int offset = super.getCursorOffset();
            if (Editor.this.mDrawableForCursor != null) {
                Editor.this.mDrawableForCursor.getPadding(Editor.this.mTempRect);
                return offset + (((Editor.this.mDrawableForCursor.getIntrinsicWidth() - Editor.this.mTempRect.left) - Editor.this.mTempRect.right) / 2);
            }
            return offset;
        }

        @Override // android.widget.Editor.HandleView
        int getCursorHorizontalPosition(Layout layout, int offset) {
            if (Editor.this.mDrawableForCursor != null) {
                float horizontal = getHorizontal(layout, offset);
                Editor editor = Editor.this;
                return editor.clampHorizontalPosition(editor.mDrawableForCursor, horizontal) + Editor.this.mTempRect.left;
            }
            return super.getCursorHorizontalPosition(layout, offset);
        }

        /* JADX WARN: Code restructure failed: missing block: B:9:0x0011, code lost:
            if (r1 != 3) goto L10;
         */
        @Override // android.widget.Editor.HandleView, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public boolean onTouchEvent(android.view.MotionEvent r8) {
            /*
                r7 = this;
                boolean r0 = super.onTouchEvent(r8)
                int r1 = r8.getActionMasked()
                if (r1 == 0) goto L77
                r2 = 1
                if (r1 == r2) goto L19
                r2 = 2
                if (r1 == r2) goto L15
                r2 = 3
                if (r1 == r2) goto L70
                goto L87
            L15:
                r7.updateMagnifier(r8)
                goto L87
            L19:
                boolean r1 = r7.offsetHasBeenChanged()
                if (r1 != 0) goto L5f
                float r1 = r7.mDownPositionX
                float r2 = r8.getRawX()
                float r1 = r1 - r2
                float r2 = r7.mDownPositionY
                float r3 = r8.getRawY()
                float r2 = r2 - r3
                float r3 = r1 * r1
                float r4 = r2 * r2
                float r3 = r3 + r4
                android.widget.Editor r4 = android.widget.Editor.this
                android.widget.TextView r4 = android.widget.Editor.access$300(r4)
                android.content.Context r4 = r4.getContext()
                android.view.ViewConfiguration r4 = android.view.ViewConfiguration.get(r4)
                int r5 = r4.getScaledTouchSlop()
                int r6 = r5 * r5
                float r6 = (float) r6
                int r6 = (r3 > r6 ? 1 : (r3 == r6 ? 0 : -1))
                if (r6 >= 0) goto L5e
                android.widget.Editor r6 = android.widget.Editor.this
                android.view.ActionMode r6 = android.widget.Editor.access$500(r6)
                if (r6 == 0) goto L59
                android.widget.Editor r6 = android.widget.Editor.this
                r6.lambda$startActionModeInternal$0$Editor()
                goto L5e
            L59:
                android.widget.Editor r6 = android.widget.Editor.this
                r6.startInsertionActionMode()
            L5e:
                goto L70
            L5f:
                android.widget.Editor r1 = android.widget.Editor.this
                android.view.ActionMode r1 = android.widget.Editor.access$500(r1)
                if (r1 == 0) goto L70
                android.widget.Editor r1 = android.widget.Editor.this
                android.view.ActionMode r1 = android.widget.Editor.access$500(r1)
                r1.invalidateContentRect()
            L70:
                r7.hideAfterDelay()
                r7.dismissMagnifier()
                goto L87
            L77:
                float r1 = r8.getRawX()
                r7.mDownPositionX = r1
                float r1 = r8.getRawY()
                r7.mDownPositionY = r1
                r7.updateMagnifier(r8)
            L87:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: android.widget.Editor.InsertionHandleView.onTouchEvent(android.view.MotionEvent):boolean");
        }

        @Override // android.widget.Editor.HandleView
        public int getCurrentCursorOffset() {
            return Editor.this.mTextView.getSelectionStart();
        }

        @Override // android.widget.Editor.HandleView
        public void updateSelection(int offset) {
            Selection.setSelection((Spannable) Editor.this.mTextView.getText(), offset);
        }

        @Override // android.widget.Editor.HandleView
        protected void updatePosition(float x, float y, boolean fromTouchScreen) {
            int offset;
            Layout layout = Editor.this.mTextView.getLayout();
            if (layout != null) {
                if (this.mPreviousLineTouched == -1) {
                    this.mPreviousLineTouched = Editor.this.mTextView.getLineAtCoordinate(y);
                }
                int currLine = Editor.this.getCurrentLineAdjustedForSlop(layout, this.mPreviousLineTouched, y);
                offset = getOffsetAtCoordinate(layout, currLine, x);
                this.mPreviousLineTouched = currLine;
            } else {
                offset = -1;
            }
            positionAtCursorOffset(offset, false, fromTouchScreen);
            if (Editor.this.mTextActionMode != null) {
                Editor.this.invalidateActionMode();
            }
        }

        @Override // android.widget.Editor.HandleView
        void onHandleMoved() {
            super.onHandleMoved();
            removeHiderCallback();
        }

        @Override // android.widget.Editor.HandleView
        public void onDetached() {
            super.onDetached();
            removeHiderCallback();
        }

        @Override // android.widget.Editor.HandleView
        protected int getMagnifierHandleTrigger() {
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

        private boolean isStartHandle() {
            return this.mHandleType == 0;
        }

        @Override // android.widget.Editor.HandleView
        protected int getHotspotX(Drawable drawable, boolean isRtlRun) {
            if (isRtlRun == isStartHandle()) {
                return drawable.getIntrinsicWidth() / 4;
            }
            return (drawable.getIntrinsicWidth() * 3) / 4;
        }

        @Override // android.widget.Editor.HandleView
        protected int getHorizontalGravity(boolean isRtlRun) {
            return isRtlRun == isStartHandle() ? 3 : 5;
        }

        @Override // android.widget.Editor.HandleView
        public int getCurrentCursorOffset() {
            return isStartHandle() ? Editor.this.mTextView.getSelectionStart() : Editor.this.mTextView.getSelectionEnd();
        }

        @Override // android.widget.Editor.HandleView
        protected void updateSelection(int offset) {
            if (isStartHandle()) {
                Selection.setSelection((Spannable) Editor.this.mTextView.getText(), offset, Editor.this.mTextView.getSelectionEnd());
            } else {
                Selection.setSelection((Spannable) Editor.this.mTextView.getText(), Editor.this.mTextView.getSelectionStart(), offset);
            }
            updateDrawable(false);
            if (Editor.this.mTextActionMode != null) {
                Editor.this.invalidateActionMode();
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:130:0x01cd, code lost:
            if (r8 < r23.mPrevLine) goto L115;
         */
        /* JADX WARN: Code restructure failed: missing block: B:77:0x0130, code lost:
            if (r23.this$0.mTextView.canScrollHorizontally(r15 ? -1 : 1) != false) goto L53;
         */
        @Override // android.widget.Editor.HandleView
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        protected void updatePosition(float r24, float r25, boolean r26) {
            /*
                Method dump skipped, instructions count: 710
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: android.widget.Editor.SelectionHandleView.updatePosition(float, float, boolean):void");
        }

        @Override // android.widget.Editor.HandleView
        protected void positionAtCursorOffset(int offset, boolean forceUpdatePosition, boolean fromTouchScreen) {
            super.positionAtCursorOffset(offset, forceUpdatePosition, fromTouchScreen);
            this.mInWord = (offset == -1 || Editor.this.getWordIteratorWithText().isBoundary(offset)) ? false : true;
        }

        /* JADX WARN: Code restructure failed: missing block: B:9:0x0011, code lost:
            if (r1 != 3) goto L9;
         */
        @Override // android.widget.Editor.HandleView, android.view.View
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public boolean onTouchEvent(android.view.MotionEvent r4) {
            /*
                r3 = this;
                boolean r0 = super.onTouchEvent(r4)
                int r1 = r4.getActionMasked()
                if (r1 == 0) goto L1c
                r2 = 1
                if (r1 == r2) goto L18
                r2 = 2
                if (r1 == r2) goto L14
                r2 = 3
                if (r1 == r2) goto L18
                goto L27
            L14:
                r3.updateMagnifier(r4)
                goto L27
            L18:
                r3.dismissMagnifier()
                goto L27
            L1c:
                r1 = 0
                r3.mTouchWordDelta = r1
                r1 = -1082130432(0xffffffffbf800000, float:-1.0)
                r3.mPrevX = r1
                r3.updateMagnifier(r4)
            L27:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: android.widget.Editor.SelectionHandleView.onTouchEvent(android.view.MotionEvent):boolean");
        }

        private void positionAndAdjustForCrossingHandles(int offset, boolean fromTouchScreen) {
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

        private boolean positionNearEdgeOfScrollingView(float x, boolean atRtl) {
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
        protected boolean isAtRtlRun(Layout layout, int offset) {
            int offsetToCheck = isStartHandle() ? offset : Math.max(offset - 1, 0);
            return layout.isRtlCharAt(offsetToCheck);
        }

        @Override // android.widget.Editor.HandleView
        public float getHorizontal(Layout layout, int offset) {
            return getHorizontal(layout, offset, isStartHandle());
        }

        private float getHorizontal(Layout layout, int offset, boolean startHandle) {
            int line = layout.getLineForOffset(offset);
            int offsetToCheck = startHandle ? offset : Math.max(offset - 1, 0);
            boolean isRtlChar = layout.isRtlCharAt(offsetToCheck);
            boolean isRtlParagraph = layout.getParagraphDirection(line) == -1;
            return isRtlChar == isRtlParagraph ? layout.getPrimaryHorizontal(offset) : layout.getSecondaryHorizontal(offset);
        }

        @Override // android.widget.Editor.HandleView
        protected int getOffsetAtCoordinate(Layout layout, int line, float x) {
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
        protected int getMagnifierHandleTrigger() {
            if (isStartHandle()) {
                return 1;
            }
            return 2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCurrentLineAdjustedForSlop(Layout layout, int prevLine, float y) {
        int trueLine = this.mTextView.getLineAtCoordinate(y);
        if (layout == null || prevLine > layout.getLineCount() || layout.getLineCount() <= 0 || prevLine < 0) {
            return trueLine;
        }
        if (Math.abs(trueLine - prevLine) >= 2) {
            return trueLine;
        }
        float verticalOffset = this.mTextView.viewportToContentVerticalOffset();
        int lineCount = layout.getLineCount();
        float slop = this.mTextView.getLineHeight() * LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS;
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

    /* JADX INFO: Access modifiers changed from: package-private */
    public void loadCursorDrawable() {
        if (this.mDrawableForCursor == null) {
            this.mDrawableForCursor = this.mTextView.getTextCursorDrawable();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class InsertionPointCursorController implements CursorController {
        private InsertionHandleView mHandle;

        private InsertionPointCursorController() {
        }

        @Override // android.widget.Editor.CursorController
        public void show() {
            getHandle().show();
            if (Editor.this.mSelectionModifierCursorController != null) {
                Editor.this.mSelectionModifierCursorController.hide();
            }
        }

        @Override // android.widget.Editor.CursorController
        public void hide() {
            InsertionHandleView insertionHandleView = this.mHandle;
            if (insertionHandleView != null) {
                insertionHandleView.hide();
            }
        }

        @Override // android.view.ViewTreeObserver.OnTouchModeChangeListener
        public void onTouchModeChanged(boolean isInTouchMode) {
            if (!isInTouchMode) {
                hide();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public InsertionHandleView getHandle() {
            if (this.mHandle == null) {
                Editor.this.loadHandleDrawables(false);
                Editor editor = Editor.this;
                this.mHandle = new InsertionHandleView(editor.mSelectHandleCenter);
            }
            return this.mHandle;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void reloadHandleDrawable() {
            InsertionHandleView insertionHandleView = this.mHandle;
            if (insertionHandleView == null) {
                return;
            }
            insertionHandleView.setDrawables(Editor.this.mSelectHandleCenter, Editor.this.mSelectHandleCenter);
        }

        @Override // android.widget.Editor.CursorController
        public void onDetached() {
            ViewTreeObserver observer = Editor.this.mTextView.getViewTreeObserver();
            observer.removeOnTouchModeChangeListener(this);
            InsertionHandleView insertionHandleView = this.mHandle;
            if (insertionHandleView != null) {
                insertionHandleView.onDetached();
            }
        }

        @Override // android.widget.Editor.CursorController
        public boolean isCursorBeingModified() {
            InsertionHandleView insertionHandleView = this.mHandle;
            return insertionHandleView != null && insertionHandleView.isDragging();
        }

        @Override // android.widget.Editor.CursorController
        public boolean isActive() {
            InsertionHandleView insertionHandleView = this.mHandle;
            return insertionHandleView != null && insertionHandleView.isShowing();
        }

        public void invalidateHandle() {
            InsertionHandleView insertionHandleView = this.mHandle;
            if (insertionHandleView != null) {
                insertionHandleView.invalidate();
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
        public void show() {
            if (Editor.this.mTextView.isInBatchEditMode()) {
                return;
            }
            Editor.this.loadHandleDrawables(false);
            initHandles();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void initHandles() {
            if (this.mStartHandle == null) {
                Editor editor = Editor.this;
                this.mStartHandle = new SelectionHandleView(editor.mSelectHandleLeft, Editor.this.mSelectHandleRight, R.id.selection_start_handle, 0);
            }
            if (this.mEndHandle == null) {
                Editor editor2 = Editor.this;
                this.mEndHandle = new SelectionHandleView(editor2.mSelectHandleRight, Editor.this.mSelectHandleLeft, R.id.selection_end_handle, 1);
            }
            this.mStartHandle.show();
            this.mEndHandle.show();
            Editor.this.hideInsertionPointCursorController();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void reloadHandleDrawables() {
            SelectionHandleView selectionHandleView = this.mStartHandle;
            if (selectionHandleView == null) {
                return;
            }
            selectionHandleView.setDrawables(Editor.this.mSelectHandleLeft, Editor.this.mSelectHandleRight);
            this.mEndHandle.setDrawables(Editor.this.mSelectHandleRight, Editor.this.mSelectHandleLeft);
        }

        @Override // android.widget.Editor.CursorController
        public void hide() {
            SelectionHandleView selectionHandleView = this.mStartHandle;
            if (selectionHandleView != null) {
                selectionHandleView.hide();
            }
            SelectionHandleView selectionHandleView2 = this.mEndHandle;
            if (selectionHandleView2 != null) {
                selectionHandleView2.hide();
            }
        }

        public void enterDrag(int dragAcceleratorMode) {
            show();
            this.mDragAcceleratorMode = dragAcceleratorMode;
            this.mStartOffset = Editor.this.mTextView.getOffsetForPosition(Editor.this.mLastDownPositionX, Editor.this.mLastDownPositionY);
            this.mLineSelectionIsOn = Editor.this.mTextView.getLineAtCoordinate(Editor.this.mLastDownPositionY);
            hide();
            Editor.this.mTextView.getParent().requestDisallowInterceptTouchEvent(true);
            Editor.this.mTextView.cancelLongPress();
        }

        public void onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            boolean isMouse = event.isFromSource(8194);
            int actionMasked = event.getActionMasked();
            if (actionMasked == 0) {
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
            } else if (actionMasked == 1) {
                if (isDragAcceleratorActive()) {
                    updateSelection(event);
                    Editor.this.mTextView.getParent().requestDisallowInterceptTouchEvent(false);
                    resetDragAcceleratorState();
                    if (Editor.this.mTextView.hasSelection()) {
                        Editor.this.startSelectionActionModeAsync(this.mHaventMovedEnoughToStartDrag);
                    }
                }
            } else if (actionMasked != 2) {
                if ((actionMasked == 5 || actionMasked == 6) && Editor.this.mTextView.getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH_DISTINCT)) {
                    updateMinAndMaxOffsets(event);
                }
            } else {
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
                        Editor.this.lambda$startActionModeInternal$0$Editor();
                        enterDrag(1);
                        Editor.this.mDiscardNextActionUp = true;
                        this.mHaventMovedEnoughToStartDrag = false;
                    }
                }
                SelectionHandleView selectionHandleView = this.mStartHandle;
                if (selectionHandleView == null || !selectionHandleView.isShowing()) {
                    updateSelection(event);
                }
            }
        }

        private void updateSelection(MotionEvent event) {
            if (Editor.this.mTextView.getLayout() != null) {
                int i = this.mDragAcceleratorMode;
                if (i == 1) {
                    updateCharacterBasedSelection(event);
                } else if (i == 2) {
                    updateWordBasedSelection(event);
                } else if (i == 3) {
                    updateParagraphBasedSelection(event);
                }
            }
        }

        private boolean selectCurrentParagraphAndStartDrag() {
            if (Editor.this.mInsertionActionModeRunnable != null) {
                Editor.this.mTextView.removeCallbacks(Editor.this.mInsertionActionModeRunnable);
            }
            Editor.this.lambda$startActionModeInternal$0$Editor();
            if (!Editor.this.selectCurrentParagraph()) {
                return false;
            }
            enterDrag(3);
            return true;
        }

        private void updateCharacterBasedSelection(MotionEvent event) {
            int offset = Editor.this.mTextView.getOffsetForPosition(event.getX(), event.getY());
            updateSelectionInternal(this.mStartOffset, offset, event.isFromSource(4098));
        }

        private void updateWordBasedSelection(MotionEvent event) {
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
                    SelectionHandleView selectionHandleView = this.mStartHandle;
                    if (selectionHandleView != null) {
                        fingerOffset = selectionHandleView.getIdealVerticalOffset();
                    } else {
                        fingerOffset = touchSlop;
                    }
                    y = eventY - fingerOffset;
                }
                Editor editor = Editor.this;
                int currLine2 = editor.getCurrentLineAdjustedForSlop(editor.mTextView.getLayout(), this.mLineSelectionIsOn, y);
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

        private void updateParagraphBasedSelection(MotionEvent event) {
            int offset = Editor.this.mTextView.getOffsetForPosition(event.getX(), event.getY());
            int start = Math.min(offset, this.mStartOffset);
            int end = Math.max(offset, this.mStartOffset);
            long paragraphsRange = Editor.this.getParagraphsRange(start, end);
            int selectionStart = TextUtils.unpackRangeStartFromLong(paragraphsRange);
            int selectionEnd = TextUtils.unpackRangeEndFromLong(paragraphsRange);
            updateSelectionInternal(selectionStart, selectionEnd, event.isFromSource(4098));
        }

        private void updateSelectionInternal(int selectionStart, int selectionEnd, boolean fromTouchScreen) {
            boolean performHapticFeedback = fromTouchScreen && Editor.this.mHapticTextHandleEnabled && !(Editor.this.mTextView.getSelectionStart() == selectionStart && Editor.this.mTextView.getSelectionEnd() == selectionEnd);
            Selection.setSelection((Spannable) Editor.this.mTextView.getText(), selectionStart, selectionEnd);
            if (performHapticFeedback) {
                Editor.this.mTextView.performHapticFeedback(9);
            }
        }

        private void updateMinAndMaxOffsets(MotionEvent event) {
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

        public int getMinTouchOffset() {
            return this.mMinTouchOffset;
        }

        public int getMaxTouchOffset() {
            return this.mMaxTouchOffset;
        }

        public void resetTouchOffsets() {
            this.mMaxTouchOffset = -1;
            this.mMinTouchOffset = -1;
            resetDragAcceleratorState();
        }

        private void resetDragAcceleratorState() {
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

        public boolean isSelectionStartDragged() {
            SelectionHandleView selectionHandleView = this.mStartHandle;
            return selectionHandleView != null && selectionHandleView.isDragging();
        }

        @Override // android.widget.Editor.CursorController
        public boolean isCursorBeingModified() {
            SelectionHandleView selectionHandleView;
            return isDragAcceleratorActive() || isSelectionStartDragged() || ((selectionHandleView = this.mEndHandle) != null && selectionHandleView.isDragging());
        }

        public boolean isDragAcceleratorActive() {
            return this.mDragAcceleratorMode != 0;
        }

        @Override // android.view.ViewTreeObserver.OnTouchModeChangeListener
        public void onTouchModeChanged(boolean isInTouchMode) {
            if (!isInTouchMode) {
                hide();
            }
        }

        @Override // android.widget.Editor.CursorController
        public void onDetached() {
            ViewTreeObserver observer = Editor.this.mTextView.getViewTreeObserver();
            observer.removeOnTouchModeChangeListener(this);
            SelectionHandleView selectionHandleView = this.mStartHandle;
            if (selectionHandleView != null) {
                selectionHandleView.onDetached();
            }
            SelectionHandleView selectionHandleView2 = this.mEndHandle;
            if (selectionHandleView2 != null) {
                selectionHandleView2.onDetached();
            }
        }

        @Override // android.widget.Editor.CursorController
        public boolean isActive() {
            SelectionHandleView selectionHandleView = this.mStartHandle;
            return selectionHandleView != null && selectionHandleView.isShowing();
        }

        public void invalidateHandles() {
            SelectionHandleView selectionHandleView = this.mStartHandle;
            if (selectionHandleView != null) {
                selectionHandleView.invalidate();
            }
            SelectionHandleView selectionHandleView2 = this.mEndHandle;
            if (selectionHandleView2 != null) {
                selectionHandleView2.invalidate();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void loadHandleDrawables(boolean overwrite) {
        if (this.mSelectHandleCenter == null || overwrite) {
            this.mSelectHandleCenter = this.mTextView.getTextSelectHandle();
            if (hasInsertionController()) {
                getInsertionController().reloadHandleDrawable();
            }
        }
        if (this.mSelectHandleLeft == null || this.mSelectHandleRight == null || overwrite) {
            this.mSelectHandleLeft = this.mTextView.getTextSelectHandleLeft();
            this.mSelectHandleRight = this.mTextView.getTextSelectHandleRight();
            if (hasSelectionController()) {
                getSelectionController().reloadHandleDrawables();
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

        public void highlight(CorrectionInfo info) {
            this.mStart = info.getOffset();
            this.mEnd = this.mStart + info.getNewText().length();
            this.mFadingStartTime = SystemClock.uptimeMillis();
            if (this.mStart < 0 || this.mEnd < 0) {
                stopAnimation();
            }
        }

        public void draw(Canvas canvas, int cursorOffsetVertical) {
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

        private boolean updatePaint() {
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

        private boolean updatePath() {
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
        public void invalidate(boolean delayed) {
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

        private void stopAnimation() {
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

        ErrorPopup(TextView v, int width, int height) {
            super(v, width, height);
            this.mAbove = false;
            this.mPopupInlineErrorBackgroundId = 0;
            this.mPopupInlineErrorAboveBackgroundId = 0;
            this.mView = v;
            this.mPopupInlineErrorBackgroundId = getResourceId(this.mPopupInlineErrorBackgroundId, 297);
            this.mView.setBackgroundResource(this.mPopupInlineErrorBackgroundId);
        }

        void fixDirection(boolean above) {
            this.mAbove = above;
            if (above) {
                this.mPopupInlineErrorAboveBackgroundId = getResourceId(this.mPopupInlineErrorAboveBackgroundId, 296);
            } else {
                this.mPopupInlineErrorBackgroundId = getResourceId(this.mPopupInlineErrorBackgroundId, 297);
            }
            this.mView.setBackgroundResource(above ? this.mPopupInlineErrorAboveBackgroundId : this.mPopupInlineErrorBackgroundId);
        }

        private int getResourceId(int currentId, int index) {
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
        @UnsupportedAppUsage
        String privateImeOptions;

        InputContentType() {
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

        InputMethodState() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidRange(CharSequence text, int start, int end) {
        return start >= 0 && start <= end && end <= text.length();
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

        public UndoInputFilter(Editor editor) {
            this.mEditor = editor;
        }

        public void saveInstanceState(Parcel parcel) {
            parcel.writeInt(this.mIsUserEdit ? 1 : 0);
            parcel.writeInt(this.mHasComposition ? 1 : 0);
            parcel.writeInt(this.mExpanding ? 1 : 0);
            parcel.writeInt(this.mPreviousOperationWasInSameBatchEdit ? 1 : 0);
        }

        public void restoreInstanceState(Parcel parcel) {
            this.mIsUserEdit = parcel.readInt() != 0;
            this.mHasComposition = parcel.readInt() != 0;
            this.mExpanding = parcel.readInt() != 0;
            this.mPreviousOperationWasInSameBatchEdit = parcel.readInt() != 0;
        }

        public void beginBatchEdit() {
            this.mIsUserEdit = true;
        }

        public void endBatchEdit() {
            this.mIsUserEdit = false;
            this.mPreviousOperationWasInSameBatchEdit = false;
        }

        @Override // android.text.InputFilter
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            boolean shouldCreateSeparateState;
            if (canUndoEdit(source, start, end, dest, dstart, dend)) {
                boolean hadComposition = this.mHasComposition;
                this.mHasComposition = isComposition(source);
                boolean wasExpanding = this.mExpanding;
                if (end - start != dend - dstart) {
                    this.mExpanding = end - start > dend - dstart;
                    if (hadComposition && this.mExpanding != wasExpanding) {
                        shouldCreateSeparateState = true;
                        handleEdit(source, start, end, dest, dstart, dend, shouldCreateSeparateState);
                        return null;
                    }
                }
                shouldCreateSeparateState = false;
                handleEdit(source, start, end, dest, dstart, dend, shouldCreateSeparateState);
                return null;
            }
            return null;
        }

        void freezeLastEdit() {
            this.mEditor.mUndoManager.beginUpdate("Edit text");
            EditOperation lastEdit = getLastEdit();
            if (lastEdit != null) {
                lastEdit.mFrozen = true;
            }
            this.mEditor.mUndoManager.endUpdate();
        }

        private void handleEdit(CharSequence source, int start, int end, Spanned dest, int dstart, int dend, boolean shouldCreateSeparateState) {
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

        private EditOperation getLastEdit() {
            UndoManager um = this.mEditor.mUndoManager;
            return (EditOperation) um.getLastOperation(EditOperation.class, this.mEditor.mUndoOwner, 1);
        }

        private void recordEdit(EditOperation edit, int mergeMode) {
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

        private boolean canUndoEdit(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (this.mEditor.mAllowUndo && !this.mEditor.mUndoManager.isInUndo() && Editor.isValidRange(source, start, end) && Editor.isValidRange(dest, dstart, dend)) {
                return (start == end && dstart == dend) ? false : true;
            }
            return false;
        }

        private static boolean isComposition(CharSequence source) {
            if (source instanceof Spannable) {
                Spannable text = (Spannable) source;
                int composeBegin = EditableInputConnection.getComposingSpanStart(text);
                int composeEnd = EditableInputConnection.getComposingSpanEnd(text);
                return composeBegin < composeEnd;
            }
            return false;
        }

        private boolean isInTextWatcher() {
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

        public EditOperation(Editor editor, String oldText, int dstart, String newText, boolean isComposition) {
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

        public EditOperation(Parcel src, ClassLoader loader) {
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

        private int getNewTextEnd() {
            return this.mStart + this.mNewText.length();
        }

        private int getOldTextEnd() {
            return this.mStart + this.mOldText.length();
        }

        @Override // android.content.UndoOperation
        public void commit() {
        }

        @Override // android.content.UndoOperation
        public void undo() {
            Editor editor = getOwnerData();
            Editable text = (Editable) editor.mTextView.getText();
            modifyText(text, this.mStart, getNewTextEnd(), this.mOldText, this.mStart, this.mOldCursorPos);
        }

        @Override // android.content.UndoOperation
        public void redo() {
            Editor editor = getOwnerData();
            Editable text = (Editable) editor.mTextView.getText();
            modifyText(text, this.mStart, getOldTextEnd(), this.mNewText, this.mStart, this.mNewCursorPos);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean mergeWith(EditOperation edit) {
            if (this.mFrozen) {
                return false;
            }
            int i = this.mType;
            if (i != 0) {
                if (i != 1) {
                    if (i != 2) {
                        return false;
                    }
                    return mergeReplaceWith(edit);
                }
                return mergeDeleteWith(edit);
            }
            return mergeInsertWith(edit);
        }

        private boolean mergeInsertWith(EditOperation edit) {
            int i = edit.mType;
            if (i == 0) {
                if (getNewTextEnd() != edit.mStart) {
                    return false;
                }
                this.mNewText += edit.mNewText;
                this.mNewCursorPos = edit.mNewCursorPos;
                this.mFrozen = edit.mFrozen;
                this.mIsComposition = edit.mIsComposition;
                return true;
            } else if (!this.mIsComposition || i != 2 || this.mStart > edit.mStart || getNewTextEnd() < edit.getOldTextEnd()) {
                return false;
            } else {
                this.mNewText = this.mNewText.substring(0, edit.mStart - this.mStart) + edit.mNewText + this.mNewText.substring(edit.getOldTextEnd() - this.mStart, this.mNewText.length());
                this.mNewCursorPos = edit.mNewCursorPos;
                this.mIsComposition = edit.mIsComposition;
                return true;
            }
        }

        private boolean mergeDeleteWith(EditOperation edit) {
            if (edit.mType == 1 && this.mStart == edit.getOldTextEnd()) {
                this.mStart = edit.mStart;
                this.mOldText = edit.mOldText + this.mOldText;
                this.mNewCursorPos = edit.mNewCursorPos;
                this.mIsComposition = edit.mIsComposition;
                return true;
            }
            return false;
        }

        private boolean mergeReplaceWith(EditOperation edit) {
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

        public void forceMergeWith(EditOperation edit) {
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

        private static void modifyText(Editable text, int deleteFrom, int deleteTo, CharSequence newText, int newTextInsertAt, int newCursorPos) {
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

        private String getTypeString() {
            int i = this.mType;
            if (i != 0) {
                if (i != 1) {
                    if (i == 2) {
                        return "replace";
                    }
                    return "";
                }
                return "delete";
            }
            return "insert";
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

        private ProcessTextIntentActionsHandler(Editor editor) {
            this.mAccessibilityIntents = new SparseArray<>();
            this.mAccessibilityActions = new SparseArray<>();
            this.mSupportedActivities = new ArrayList();
            this.mEditor = (Editor) Preconditions.checkNotNull(editor);
            this.mTextView = (TextView) Preconditions.checkNotNull(this.mEditor.mTextView);
            this.mContext = (Context) Preconditions.checkNotNull(this.mTextView.getContext());
            this.mPackageManager = (PackageManager) Preconditions.checkNotNull(this.mContext.getPackageManager());
            this.mPackageName = (String) Preconditions.checkNotNull(this.mContext.getPackageName());
        }

        public void onInitializeMenu(Menu menu) {
            loadSupportedActivities();
            int size = this.mSupportedActivities.size();
            for (int i = 0; i < size; i++) {
                ResolveInfo resolveInfo = this.mSupportedActivities.get(i);
                menu.add(0, 0, i + 100, getLabel(resolveInfo)).setIntent(createProcessTextIntentForResolveInfo(resolveInfo)).setShowAsAction(0);
            }
        }

        public boolean performMenuItemAction(MenuItem item) {
            return fireIntent(item.getIntent());
        }

        public void initializeAccessibilityActions() {
            this.mAccessibilityIntents.clear();
            this.mAccessibilityActions.clear();
            int actionId = 0;
            loadSupportedActivities();
            for (ResolveInfo resolveInfo : this.mSupportedActivities) {
                int i = actionId + 1;
                int actionId2 = actionId + 268435712;
                this.mAccessibilityActions.put(actionId2, new AccessibilityNodeInfo.AccessibilityAction(actionId2, getLabel(resolveInfo)));
                this.mAccessibilityIntents.put(actionId2, createProcessTextIntentForResolveInfo(resolveInfo));
                actionId = i;
            }
        }

        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo nodeInfo) {
            for (int i = 0; i < this.mAccessibilityActions.size(); i++) {
                nodeInfo.addAction(this.mAccessibilityActions.valueAt(i));
            }
        }

        public boolean performAccessibilityAction(int actionId) {
            return fireIntent(this.mAccessibilityIntents.get(actionId));
        }

        private boolean fireIntent(Intent intent) {
            if (intent != null && Intent.ACTION_PROCESS_TEXT.equals(intent.getAction())) {
                String selectedText = this.mTextView.getSelectedText();
                intent.putExtra(Intent.EXTRA_PROCESS_TEXT, (String) TextUtils.trimToParcelableSize(selectedText));
                this.mEditor.mPreserveSelection = true;
                this.mTextView.startActivityForResult(intent, 100);
                return true;
            }
            return false;
        }

        private void loadSupportedActivities() {
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

        private boolean isSupportedActivity(ResolveInfo info) {
            return this.mPackageName.equals(info.activityInfo.packageName) || (info.activityInfo.exported && (info.activityInfo.permission == null || this.mContext.checkSelfPermission(info.activityInfo.permission) == 0));
        }

        private Intent createProcessTextIntentForResolveInfo(ResolveInfo info) {
            return createProcessTextIntent().putExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, !this.mTextView.isTextEditable()).setClassName(info.activityInfo.packageName, info.activityInfo.name);
        }

        private Intent createProcessTextIntent() {
            return new Intent().setAction(Intent.ACTION_PROCESS_TEXT).setType(ClipDescription.MIMETYPE_TEXT_PLAIN);
        }

        private CharSequence getLabel(ResolveInfo resolveInfo) {
            return resolveInfo.loadLabel(this.mPackageManager);
        }
    }
}
