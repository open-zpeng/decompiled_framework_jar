package android.widget;

import android.R;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.UndoManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.BaseCanvas;
import android.graphics.Canvas;
import android.graphics.Insets;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.icu.text.DecimalFormatSymbols;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ParcelableParcel;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.BoringLayout;
import android.text.DynamicLayout;
import android.text.Editable;
import android.text.GetChars;
import android.text.GraphicsOperations;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Layout;
import android.text.ParcelableSpan;
import android.text.PrecomputedText;
import android.text.Selection;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.StaticLayout;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.AllCapsTransformationMethod;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.DateKeyListener;
import android.text.method.DateTimeKeyListener;
import android.text.method.DialerKeyListener;
import android.text.method.DigitsKeyListener;
import android.text.method.KeyListener;
import android.text.method.LinkMovementMethod;
import android.text.method.MetaKeyKeyListener;
import android.text.method.MovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.text.method.TextKeyListener;
import android.text.method.TimeKeyListener;
import android.text.method.TransformationMethod;
import android.text.method.TransformationMethod2;
import android.text.method.WordIterator;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ParagraphStyle;
import android.text.style.SpellCheckSpan;
import android.text.style.SuggestionSpan;
import android.text.style.URLSpan;
import android.text.style.UpdateAppearance;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.IntArray;
import android.util.Log;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.AccessibilityIterators;
import android.view.ActionMode;
import android.view.Choreographer;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewDebug;
import android.view.ViewHierarchyEncoder;
import android.view.ViewParent;
import android.view.ViewRootImpl;
import android.view.ViewStructure;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.AnimationUtils;
import android.view.autofill.AutofillManager;
import android.view.autofill.AutofillValue;
import android.view.autofill.Helper;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.CursorAnchorInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.view.textclassifier.TextClassification;
import android.view.textclassifier.TextClassificationContext;
import android.view.textclassifier.TextClassificationManager;
import android.view.textclassifier.TextClassifier;
import android.view.textclassifier.TextLinks;
import android.view.textservice.SpellCheckerSubtype;
import android.view.textservice.TextServicesManager;
import android.widget.AccessibilityIterators;
import android.widget.Editor;
import android.widget.RemoteViews;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.util.FastMath;
import com.android.internal.util.Preconditions;
import com.android.internal.widget.EditableInputConnection;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;
import libcore.util.EmptyArray;
import org.xmlpull.v1.XmlPullParserException;
@RemoteViews.RemoteView
/* loaded from: classes3.dex */
public class TextView extends View implements ViewTreeObserver.OnPreDrawListener {
    static final int ACCESSIBILITY_ACTION_PROCESS_TEXT_START_ID = 268435712;
    private static final int ACCESSIBILITY_ACTION_SHARE = 268435456;
    private static final int ANIMATED_SCROLL_GAP = 250;
    public static final int AUTO_SIZE_TEXT_TYPE_NONE = 0;
    public static final int AUTO_SIZE_TEXT_TYPE_UNIFORM = 1;
    private static final int CHANGE_WATCHER_PRIORITY = 100;
    static final boolean DEBUG_EXTRACT = false;
    private static final int DECIMAL = 4;
    private static final int DEFAULT_AUTO_SIZE_GRANULARITY_IN_PX = 1;
    private static final int DEFAULT_AUTO_SIZE_MAX_TEXT_SIZE_IN_SP = 112;
    private static final int DEFAULT_AUTO_SIZE_MIN_TEXT_SIZE_IN_SP = 12;
    private static final int DEFAULT_TYPEFACE = -1;
    private static final int DEVICE_PROVISIONED_NO = 1;
    private static final int DEVICE_PROVISIONED_UNKNOWN = 0;
    private static final int DEVICE_PROVISIONED_YES = 2;
    private static final int ELLIPSIZE_END = 3;
    private static final int ELLIPSIZE_MARQUEE = 4;
    private static final int ELLIPSIZE_MIDDLE = 2;
    private static final int ELLIPSIZE_NONE = 0;
    private static final int ELLIPSIZE_NOT_SET = -1;
    private static final int ELLIPSIZE_START = 1;
    private static final int EMS = 1;
    private static final int FLOATING_TOOLBAR_SELECT_ALL_REFRESH_DELAY = 500;
    static final int ID_ASSIST = 16908353;
    static final int ID_AUTOFILL = 16908355;
    static final int ID_COPY = 16908321;
    static final int ID_CUT = 16908320;
    static final int ID_PASTE = 16908322;
    static final int ID_PASTE_AS_PLAIN_TEXT = 16908337;
    static final int ID_REDO = 16908339;
    static final int ID_REPLACE = 16908340;
    static final int ID_SELECT_ALL = 16908319;
    static final int ID_SHARE = 16908341;
    static final int ID_UNDO = 16908338;
    private static final int KEY_DOWN_HANDLED_BY_KEY_LISTENER = 1;
    private static final int KEY_DOWN_HANDLED_BY_MOVEMENT_METHOD = 2;
    private static final int KEY_EVENT_HANDLED = -1;
    private static final int KEY_EVENT_NOT_HANDLED = 0;
    public protected static final int LINES = 1;
    static final String LOG_TAG = "TextView";
    private static final int MARQUEE_FADE_NORMAL = 0;
    private static final int MARQUEE_FADE_SWITCH_SHOW_ELLIPSIS = 1;
    private static final int MARQUEE_FADE_SWITCH_SHOW_FADE = 2;
    private static final int MONOSPACE = 3;
    private static final int PIXELS = 2;
    static final int PROCESS_TEXT_REQUEST_CODE = 100;
    private static final int SANS = 1;
    private static final int SERIF = 2;
    private static final int SIGNED = 2;
    @VisibleForTesting
    public static final BoringLayout.Metrics UNKNOWN_BORING;
    private static final float UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE = -1.0f;
    static final int VERY_WIDE = 1048576;
    static long sLastCutCopyOrTextChangedTime;
    public protected boolean mAllowTransformationLengthChange;
    private int mAutoLinkMask;
    private float mAutoSizeMaxTextSizeInPx;
    private float mAutoSizeMinTextSizeInPx;
    private float mAutoSizeStepGranularityInPx;
    private int[] mAutoSizeTextSizesInPx;
    private int mAutoSizeTextType;
    public protected BoringLayout.Metrics mBoring;
    private int mBreakStrategy;
    public protected BufferType mBufferType;
    public protected ChangeWatcher mChangeWatcher;
    public protected CharWrapper mCharWrapper;
    public protected int mCurHintTextColor;
    @ViewDebug.ExportedProperty(category = "text")
    public protected int mCurTextColor;
    private volatile Locale mCurrentSpellCheckerLocaleCache;
    public private protected int mCursorDrawableRes;
    private int mDeferScroll;
    public protected int mDesiredHeightAtMeasure;
    private int mDeviceProvisionedState;
    public private protected Drawables mDrawables;
    public protected Editable.Factory mEditableFactory;
    public protected Editor mEditor;
    private TextUtils.TruncateAt mEllipsize;
    private InputFilter[] mFilters;
    private boolean mFreezesText;
    @ViewDebug.ExportedProperty(category = "text")
    public protected int mGravity;
    private boolean mHasPresetAutoSizeValues;
    public private protected int mHighlightColor;
    public protected final Paint mHighlightPaint;
    private Path mHighlightPath;
    public protected boolean mHighlightPathBogus;
    private CharSequence mHint;
    public protected BoringLayout.Metrics mHintBoring;
    public protected Layout mHintLayout;
    private ColorStateList mHintTextColor;
    public protected boolean mHorizontallyScrolling;
    private int mHyphenationFrequency;
    public protected boolean mIncludePad;
    private int mJustificationMode;
    private int mLastLayoutDirection;
    private long mLastScroll;
    private CharSequence mLastValueSentToAutofillManager;
    public protected Layout mLayout;
    private ColorStateList mLinkTextColor;
    private boolean mLinksClickable;
    private boolean mListenerChanged;
    public protected ArrayList<TextWatcher> mListeners;
    private boolean mLocalesChanged;
    public protected Marquee mMarquee;
    public protected int mMarqueeFadeMode;
    private int mMarqueeRepeatLimit;
    public protected int mMaxMode;
    public protected int mMaxWidth;
    public protected int mMaxWidthMode;
    public protected int mMaximum;
    public protected int mMinMode;
    public protected int mMinWidth;
    public protected int mMinWidthMode;
    public protected int mMinimum;
    private MovementMethod mMovement;
    private boolean mNeedsAutoSizeText;
    public protected int mOldMaxMode;
    public protected int mOldMaximum;
    private boolean mPreDrawListenerDetached;
    private boolean mPreDrawRegistered;
    private PrecomputedText mPrecomputed;
    private boolean mPreventDefaultMovement;
    public protected boolean mRestartMarquee;
    public protected BoringLayout mSavedHintLayout;
    public protected BoringLayout mSavedLayout;
    public protected Layout mSavedMarqueeModeLayout;
    private Scroller mScroller;
    private int mShadowColor;
    public protected float mShadowDx;
    public protected float mShadowDy;
    public protected float mShadowRadius;
    public protected boolean mSingleLine;
    public protected float mSpacingAdd;
    public protected float mSpacingMult;
    private Spannable mSpannable;
    public protected Spannable.Factory mSpannableFactory;
    private Rect mTempRect;
    private TextPaint mTempTextPaint;
    @ViewDebug.ExportedProperty(category = "text")
    public protected CharSequence mText;
    private TextClassifier mTextClassificationSession;
    private TextClassifier mTextClassifier;
    private ColorStateList mTextColor;
    public protected TextDirectionHeuristic mTextDir;
    int mTextEditSuggestionContainerLayout;
    int mTextEditSuggestionHighlightStyle;
    int mTextEditSuggestionItemLayout;
    private int mTextId;
    public protected final TextPaint mTextPaint;
    public private protected int mTextSelectHandleLeftRes;
    public private protected int mTextSelectHandleRes;
    public private protected int mTextSelectHandleRightRes;
    private boolean mTextSetFromXmlOrResourceId;
    private TransformationMethod mTransformation;
    public protected CharSequence mTransformed;
    boolean mUseFallbackLineSpacing;
    private final boolean mUseInternationalizedInput;
    public protected boolean mUserSetTextScaleX;
    private static final float[] TEMP_POSITION = new float[2];
    private static final RectF TEMP_RECTF = new RectF();
    private static final InputFilter[] NO_FILTERS = new InputFilter[0];
    private static final Spanned EMPTY_SPANNED = new SpannedString("");
    private static final int[] MULTILINE_STATE_SET = {16843597};
    private static final SparseIntArray sAppearanceValues = new SparseIntArray();

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes3.dex */
    public @interface AutoSizeTextType {
    }

    /* loaded from: classes3.dex */
    public enum BufferType {
        NORMAL,
        SPANNABLE,
        EDITABLE
    }

    /* loaded from: classes3.dex */
    public interface OnEditorActionListener {
        boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes3.dex */
    public @interface XMLTypefaceAttr {
    }

    static {
        sAppearanceValues.put(6, 4);
        sAppearanceValues.put(5, 3);
        sAppearanceValues.put(7, 5);
        sAppearanceValues.put(8, 6);
        sAppearanceValues.put(2, 0);
        sAppearanceValues.put(3, 1);
        sAppearanceValues.put(75, 12);
        sAppearanceValues.put(4, 2);
        sAppearanceValues.put(94, 17);
        sAppearanceValues.put(72, 11);
        sAppearanceValues.put(36, 7);
        sAppearanceValues.put(37, 8);
        sAppearanceValues.put(38, 9);
        sAppearanceValues.put(39, 10);
        sAppearanceValues.put(76, 13);
        sAppearanceValues.put(90, 16);
        sAppearanceValues.put(77, 14);
        sAppearanceValues.put(78, 15);
        UNKNOWN_BORING = new BoringLayout.Metrics();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class Drawables {
        static final int BOTTOM = 3;
        static final int DRAWABLE_LEFT = 1;
        static final int DRAWABLE_NONE = -1;
        static final int DRAWABLE_RIGHT = 0;
        static final int LEFT = 0;
        static final int RIGHT = 2;
        static final int TOP = 1;
        Drawable mDrawableEnd;
        Drawable mDrawableError;
        int mDrawableHeightEnd;
        int mDrawableHeightError;
        int mDrawableHeightLeft;
        int mDrawableHeightRight;
        int mDrawableHeightStart;
        int mDrawableHeightTemp;
        Drawable mDrawableLeftInitial;
        int mDrawablePadding;
        Drawable mDrawableRightInitial;
        int mDrawableSizeBottom;
        int mDrawableSizeEnd;
        int mDrawableSizeError;
        int mDrawableSizeLeft;
        int mDrawableSizeRight;
        int mDrawableSizeStart;
        int mDrawableSizeTemp;
        int mDrawableSizeTop;
        Drawable mDrawableStart;
        Drawable mDrawableTemp;
        int mDrawableWidthBottom;
        int mDrawableWidthTop;
        boolean mHasTint;
        boolean mHasTintMode;
        boolean mIsRtlCompatibilityMode;
        boolean mOverride;
        ColorStateList mTintList;
        PorterDuff.Mode mTintMode;
        final Rect mCompoundRect = new Rect();
        final Drawable[] mShowing = new Drawable[4];
        int mDrawableSaved = -1;

        public synchronized Drawables(Context context) {
            int targetSdkVersion = context.getApplicationInfo().targetSdkVersion;
            this.mIsRtlCompatibilityMode = targetSdkVersion < 17 || !context.getApplicationInfo().hasRtlSupport();
            this.mOverride = false;
        }

        public synchronized boolean hasMetadata() {
            return this.mDrawablePadding != 0 || this.mHasTintMode || this.mHasTint;
        }

        public synchronized boolean resolveWithLayoutDirection(int layoutDirection) {
            Drawable previousLeft = this.mShowing[0];
            Drawable previousRight = this.mShowing[2];
            this.mShowing[0] = this.mDrawableLeftInitial;
            this.mShowing[2] = this.mDrawableRightInitial;
            if (this.mIsRtlCompatibilityMode) {
                if (this.mDrawableStart != null && this.mShowing[0] == null) {
                    this.mShowing[0] = this.mDrawableStart;
                    this.mDrawableSizeLeft = this.mDrawableSizeStart;
                    this.mDrawableHeightLeft = this.mDrawableHeightStart;
                }
                if (this.mDrawableEnd != null && this.mShowing[2] == null) {
                    this.mShowing[2] = this.mDrawableEnd;
                    this.mDrawableSizeRight = this.mDrawableSizeEnd;
                    this.mDrawableHeightRight = this.mDrawableHeightEnd;
                }
            } else if (layoutDirection == 1) {
                if (this.mOverride) {
                    this.mShowing[2] = this.mDrawableStart;
                    this.mDrawableSizeRight = this.mDrawableSizeStart;
                    this.mDrawableHeightRight = this.mDrawableHeightStart;
                    this.mShowing[0] = this.mDrawableEnd;
                    this.mDrawableSizeLeft = this.mDrawableSizeEnd;
                    this.mDrawableHeightLeft = this.mDrawableHeightEnd;
                }
            } else if (this.mOverride) {
                this.mShowing[0] = this.mDrawableStart;
                this.mDrawableSizeLeft = this.mDrawableSizeStart;
                this.mDrawableHeightLeft = this.mDrawableHeightStart;
                this.mShowing[2] = this.mDrawableEnd;
                this.mDrawableSizeRight = this.mDrawableSizeEnd;
                this.mDrawableHeightRight = this.mDrawableHeightEnd;
            }
            applyErrorDrawableIfNeeded(layoutDirection);
            return (this.mShowing[0] == previousLeft && this.mShowing[2] == previousRight) ? false : true;
        }

        public synchronized void setErrorDrawable(Drawable dr, TextView tv) {
            if (this.mDrawableError != dr && this.mDrawableError != null) {
                this.mDrawableError.setCallback(null);
            }
            this.mDrawableError = dr;
            if (this.mDrawableError != null) {
                Rect compoundRect = this.mCompoundRect;
                int[] state = tv.getDrawableState();
                this.mDrawableError.setState(state);
                this.mDrawableError.copyBounds(compoundRect);
                this.mDrawableError.setCallback(tv);
                this.mDrawableSizeError = compoundRect.width();
                this.mDrawableHeightError = compoundRect.height();
                return;
            }
            this.mDrawableHeightError = 0;
            this.mDrawableSizeError = 0;
        }

        private synchronized void applyErrorDrawableIfNeeded(int layoutDirection) {
            switch (this.mDrawableSaved) {
                case 0:
                    this.mShowing[2] = this.mDrawableTemp;
                    this.mDrawableSizeRight = this.mDrawableSizeTemp;
                    this.mDrawableHeightRight = this.mDrawableHeightTemp;
                    break;
                case 1:
                    this.mShowing[0] = this.mDrawableTemp;
                    this.mDrawableSizeLeft = this.mDrawableSizeTemp;
                    this.mDrawableHeightLeft = this.mDrawableHeightTemp;
                    break;
            }
            if (this.mDrawableError != null) {
                if (layoutDirection == 1) {
                    this.mDrawableSaved = 1;
                    this.mDrawableTemp = this.mShowing[0];
                    this.mDrawableSizeTemp = this.mDrawableSizeLeft;
                    this.mDrawableHeightTemp = this.mDrawableHeightLeft;
                    this.mShowing[0] = this.mDrawableError;
                    this.mDrawableSizeLeft = this.mDrawableSizeError;
                    this.mDrawableHeightLeft = this.mDrawableHeightError;
                    return;
                }
                this.mDrawableSaved = 0;
                this.mDrawableTemp = this.mShowing[2];
                this.mDrawableSizeTemp = this.mDrawableSizeRight;
                this.mDrawableHeightTemp = this.mDrawableHeightRight;
                this.mShowing[2] = this.mDrawableError;
                this.mDrawableSizeRight = this.mDrawableSizeError;
                this.mDrawableHeightRight = this.mDrawableHeightError;
            }
        }
    }

    public static synchronized void preloadFontCache() {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setTypeface(Typeface.DEFAULT);
        p.measureText("H");
    }

    public TextView(Context context) {
        this(context, null);
    }

    public TextView(Context context, AttributeSet attrs) {
        this(context, attrs, 16842884);
    }

    public TextView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /* JADX WARN: Code restructure failed: missing block: B:296:0x0dc5, code lost:
        if ((r127.mEditor.mInputType & android.os.FileObserver.ALL_EVENTS) == 129) goto L306;
     */
    /* JADX WARN: Removed duplicated region for block: B:247:0x0cd0  */
    /* JADX WARN: Removed duplicated region for block: B:248:0x0ce0  */
    /* JADX WARN: Removed duplicated region for block: B:250:0x0cec  */
    /* JADX WARN: Removed duplicated region for block: B:253:0x0cfd  */
    /* JADX WARN: Removed duplicated region for block: B:256:0x0d03 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:261:0x0d0d  */
    /* JADX WARN: Removed duplicated region for block: B:263:0x0d16  */
    /* JADX WARN: Removed duplicated region for block: B:264:0x0d22  */
    /* JADX WARN: Removed duplicated region for block: B:266:0x0d27  */
    /* JADX WARN: Removed duplicated region for block: B:269:0x0d58  */
    /* JADX WARN: Removed duplicated region for block: B:277:0x0d76  */
    /* JADX WARN: Removed duplicated region for block: B:282:0x0d95  */
    /* JADX WARN: Removed duplicated region for block: B:283:0x0d9b  */
    /* JADX WARN: Removed duplicated region for block: B:284:0x0da1  */
    /* JADX WARN: Removed duplicated region for block: B:286:0x0da9 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:293:0x0db5  */
    /* JADX WARN: Removed duplicated region for block: B:300:0x0dcc  */
    /* JADX WARN: Removed duplicated region for block: B:303:0x0dd3  */
    /* JADX WARN: Removed duplicated region for block: B:304:0x0ddb  */
    /* JADX WARN: Removed duplicated region for block: B:307:0x0de4  */
    /* JADX WARN: Removed duplicated region for block: B:310:0x0def  */
    /* JADX WARN: Removed duplicated region for block: B:311:0x0e03  */
    /* JADX WARN: Removed duplicated region for block: B:314:0x0e13  */
    /* JADX WARN: Removed duplicated region for block: B:315:0x0e19  */
    /* JADX WARN: Removed duplicated region for block: B:318:0x0e1f  */
    /* JADX WARN: Removed duplicated region for block: B:321:0x0e3a  */
    /* JADX WARN: Removed duplicated region for block: B:327:0x0e46  */
    /* JADX WARN: Removed duplicated region for block: B:333:0x0e54  */
    /* JADX WARN: Removed duplicated region for block: B:341:0x0e78  */
    /* JADX WARN: Removed duplicated region for block: B:422:0x0ec9 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public TextView(android.content.Context r128, android.util.AttributeSet r129, int r130, int r131) {
        /*
            Method dump skipped, instructions count: 4142
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.TextView.<init>(android.content.Context, android.util.AttributeSet, int, int):void");
    }

    private synchronized void setTextInternal(CharSequence text) {
        this.mText = text;
        this.mSpannable = text instanceof Spannable ? (Spannable) text : null;
        this.mPrecomputed = text instanceof PrecomputedText ? (PrecomputedText) text : null;
    }

    public void setAutoSizeTextTypeWithDefaults(int autoSizeTextType) {
        if (supportsAutoSizeText()) {
            switch (autoSizeTextType) {
                case 0:
                    clearAutoSizeConfiguration();
                    return;
                case 1:
                    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                    float autoSizeMinTextSizeInPx = TypedValue.applyDimension(2, 12.0f, displayMetrics);
                    float autoSizeMaxTextSizeInPx = TypedValue.applyDimension(2, 112.0f, displayMetrics);
                    validateAndSetAutoSizeTextTypeUniformConfiguration(autoSizeMinTextSizeInPx, autoSizeMaxTextSizeInPx, 1.0f);
                    if (setupAutoSizeText()) {
                        autoSizeText();
                        invalidate();
                        return;
                    }
                    return;
                default:
                    throw new IllegalArgumentException("Unknown auto-size text type: " + autoSizeTextType);
            }
        }
    }

    public void setAutoSizeTextTypeUniformWithConfiguration(int autoSizeMinTextSize, int autoSizeMaxTextSize, int autoSizeStepGranularity, int unit) {
        if (supportsAutoSizeText()) {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            float autoSizeMinTextSizeInPx = TypedValue.applyDimension(unit, autoSizeMinTextSize, displayMetrics);
            float autoSizeMaxTextSizeInPx = TypedValue.applyDimension(unit, autoSizeMaxTextSize, displayMetrics);
            float autoSizeStepGranularityInPx = TypedValue.applyDimension(unit, autoSizeStepGranularity, displayMetrics);
            validateAndSetAutoSizeTextTypeUniformConfiguration(autoSizeMinTextSizeInPx, autoSizeMaxTextSizeInPx, autoSizeStepGranularityInPx);
            if (setupAutoSizeText()) {
                autoSizeText();
                invalidate();
            }
        }
    }

    public void setAutoSizeTextTypeUniformWithPresetSizes(int[] presetSizes, int unit) {
        if (supportsAutoSizeText()) {
            int presetSizesLength = presetSizes.length;
            if (presetSizesLength > 0) {
                int[] presetSizesInPx = new int[presetSizesLength];
                if (unit == 0) {
                    presetSizesInPx = Arrays.copyOf(presetSizes, presetSizesLength);
                } else {
                    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                    for (int i = 0; i < presetSizesLength; i++) {
                        presetSizesInPx[i] = Math.round(TypedValue.applyDimension(unit, presetSizes[i], displayMetrics));
                    }
                }
                this.mAutoSizeTextSizesInPx = cleanupAutoSizePresetSizes(presetSizesInPx);
                if (!setupAutoSizeUniformPresetSizesConfiguration()) {
                    throw new IllegalArgumentException("None of the preset sizes is valid: " + Arrays.toString(presetSizes));
                }
            } else {
                this.mHasPresetAutoSizeValues = false;
            }
            if (setupAutoSizeText()) {
                autoSizeText();
                invalidate();
            }
        }
    }

    public int getAutoSizeTextType() {
        return this.mAutoSizeTextType;
    }

    public int getAutoSizeStepGranularity() {
        return Math.round(this.mAutoSizeStepGranularityInPx);
    }

    public int getAutoSizeMinTextSize() {
        return Math.round(this.mAutoSizeMinTextSizeInPx);
    }

    public int getAutoSizeMaxTextSize() {
        return Math.round(this.mAutoSizeMaxTextSizeInPx);
    }

    public int[] getAutoSizeTextAvailableSizes() {
        return this.mAutoSizeTextSizesInPx;
    }

    private synchronized void setupAutoSizeUniformPresetSizes(TypedArray textSizes) {
        int textSizesLength = textSizes.length();
        int[] parsedSizes = new int[textSizesLength];
        if (textSizesLength > 0) {
            for (int i = 0; i < textSizesLength; i++) {
                parsedSizes[i] = textSizes.getDimensionPixelSize(i, -1);
            }
            this.mAutoSizeTextSizesInPx = cleanupAutoSizePresetSizes(parsedSizes);
            setupAutoSizeUniformPresetSizesConfiguration();
        }
    }

    private synchronized boolean setupAutoSizeUniformPresetSizesConfiguration() {
        int sizesLength = this.mAutoSizeTextSizesInPx.length;
        this.mHasPresetAutoSizeValues = sizesLength > 0;
        if (this.mHasPresetAutoSizeValues) {
            this.mAutoSizeTextType = 1;
            this.mAutoSizeMinTextSizeInPx = this.mAutoSizeTextSizesInPx[0];
            this.mAutoSizeMaxTextSizeInPx = this.mAutoSizeTextSizesInPx[sizesLength - 1];
            this.mAutoSizeStepGranularityInPx = -1.0f;
        }
        return this.mHasPresetAutoSizeValues;
    }

    private synchronized void validateAndSetAutoSizeTextTypeUniformConfiguration(float autoSizeMinTextSizeInPx, float autoSizeMaxTextSizeInPx, float autoSizeStepGranularityInPx) {
        if (autoSizeMinTextSizeInPx <= 0.0f) {
            throw new IllegalArgumentException("Minimum auto-size text size (" + autoSizeMinTextSizeInPx + "px) is less or equal to (0px)");
        } else if (autoSizeMaxTextSizeInPx <= autoSizeMinTextSizeInPx) {
            throw new IllegalArgumentException("Maximum auto-size text size (" + autoSizeMaxTextSizeInPx + "px) is less or equal to minimum auto-size text size (" + autoSizeMinTextSizeInPx + "px)");
        } else if (autoSizeStepGranularityInPx <= 0.0f) {
            throw new IllegalArgumentException("The auto-size step granularity (" + autoSizeStepGranularityInPx + "px) is less or equal to (0px)");
        } else {
            this.mAutoSizeTextType = 1;
            this.mAutoSizeMinTextSizeInPx = autoSizeMinTextSizeInPx;
            this.mAutoSizeMaxTextSizeInPx = autoSizeMaxTextSizeInPx;
            this.mAutoSizeStepGranularityInPx = autoSizeStepGranularityInPx;
            this.mHasPresetAutoSizeValues = false;
        }
    }

    private synchronized void clearAutoSizeConfiguration() {
        this.mAutoSizeTextType = 0;
        this.mAutoSizeMinTextSizeInPx = -1.0f;
        this.mAutoSizeMaxTextSizeInPx = -1.0f;
        this.mAutoSizeStepGranularityInPx = -1.0f;
        this.mAutoSizeTextSizesInPx = EmptyArray.INT;
        this.mNeedsAutoSizeText = false;
    }

    private synchronized int[] cleanupAutoSizePresetSizes(int[] presetValues) {
        int presetValuesLength = presetValues.length;
        if (presetValuesLength == 0) {
            return presetValues;
        }
        Arrays.sort(presetValues);
        IntArray uniqueValidSizes = new IntArray();
        for (int currentPresetValue : presetValues) {
            if (currentPresetValue > 0 && uniqueValidSizes.binarySearch(currentPresetValue) < 0) {
                uniqueValidSizes.add(currentPresetValue);
            }
        }
        int i = uniqueValidSizes.size();
        if (presetValuesLength == i) {
            return presetValues;
        }
        return uniqueValidSizes.toArray();
    }

    private synchronized boolean setupAutoSizeText() {
        if (supportsAutoSizeText() && this.mAutoSizeTextType == 1) {
            if (!this.mHasPresetAutoSizeValues || this.mAutoSizeTextSizesInPx.length == 0) {
                int autoSizeValuesLength = ((int) Math.floor((this.mAutoSizeMaxTextSizeInPx - this.mAutoSizeMinTextSizeInPx) / this.mAutoSizeStepGranularityInPx)) + 1;
                int[] autoSizeTextSizesInPx = new int[autoSizeValuesLength];
                for (int i = 0; i < autoSizeValuesLength; i++) {
                    autoSizeTextSizesInPx[i] = Math.round(this.mAutoSizeMinTextSizeInPx + (i * this.mAutoSizeStepGranularityInPx));
                }
                this.mAutoSizeTextSizesInPx = cleanupAutoSizePresetSizes(autoSizeTextSizesInPx);
            }
            this.mNeedsAutoSizeText = true;
        } else {
            this.mNeedsAutoSizeText = false;
        }
        return this.mNeedsAutoSizeText;
    }

    private synchronized int[] parseDimensionArray(TypedArray dimens) {
        if (dimens == null) {
            return null;
        }
        int[] result = new int[dimens.length()];
        for (int i = 0; i < result.length; i++) {
            result[i] = dimens.getDimensionPixelSize(i, 0);
        }
        return result;
    }

    @Override // android.view.View
    public synchronized void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode != -1 || data == null) {
                if (this.mSpannable != null) {
                    Selection.setSelection(this.mSpannable, getSelectionEnd());
                    return;
                }
                return;
            }
            CharSequence result = data.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
            if (result != null) {
                if (isTextEditable()) {
                    replaceSelectionWithText(result);
                    if (this.mEditor != null) {
                        this.mEditor.refreshTextActionMode();
                    }
                } else if (result.length() > 0) {
                    Toast.makeText(getContext(), String.valueOf(result), 1).show();
                }
            }
        }
    }

    private synchronized void setTypefaceFromAttrs(Typeface typeface, String familyName, int typefaceIndex, int style, int weight) {
        if (typeface == null && familyName != null) {
            Typeface normalTypeface = Typeface.create(familyName, 0);
            resolveStyleAndSetTypeface(normalTypeface, style, weight);
        } else if (typeface != null) {
            resolveStyleAndSetTypeface(typeface, style, weight);
        } else {
            switch (typefaceIndex) {
                case 1:
                    resolveStyleAndSetTypeface(Typeface.SANS_SERIF, style, weight);
                    return;
                case 2:
                    resolveStyleAndSetTypeface(Typeface.SERIF, style, weight);
                    return;
                case 3:
                    resolveStyleAndSetTypeface(Typeface.MONOSPACE, style, weight);
                    return;
                default:
                    resolveStyleAndSetTypeface(null, style, weight);
                    return;
            }
        }
    }

    private synchronized void resolveStyleAndSetTypeface(Typeface typeface, int style, int weight) {
        if (weight >= 0) {
            int weight2 = Math.min(1000, weight);
            boolean italic = (style & 2) != 0;
            setTypeface(Typeface.create(typeface, weight2, italic));
            return;
        }
        setTypeface(typeface, style);
    }

    private synchronized void setRelativeDrawablesIfNeeded(Drawable start, Drawable end) {
        boolean hasRelativeDrawables = (start == null && end == null) ? false : true;
        if (hasRelativeDrawables) {
            Drawables dr = this.mDrawables;
            if (dr == null) {
                Drawables drawables = new Drawables(getContext());
                dr = drawables;
                this.mDrawables = drawables;
            }
            this.mDrawables.mOverride = true;
            Rect compoundRect = dr.mCompoundRect;
            int[] state = getDrawableState();
            if (start != null) {
                start.setBounds(0, 0, start.getIntrinsicWidth(), start.getIntrinsicHeight());
                start.setState(state);
                start.copyBounds(compoundRect);
                start.setCallback(this);
                dr.mDrawableStart = start;
                dr.mDrawableSizeStart = compoundRect.width();
                dr.mDrawableHeightStart = compoundRect.height();
            } else {
                dr.mDrawableHeightStart = 0;
                dr.mDrawableSizeStart = 0;
            }
            if (end != null) {
                end.setBounds(0, 0, end.getIntrinsicWidth(), end.getIntrinsicHeight());
                end.setState(state);
                end.copyBounds(compoundRect);
                end.setCallback(this);
                dr.mDrawableEnd = end;
                dr.mDrawableSizeEnd = compoundRect.width();
                dr.mDrawableHeightEnd = compoundRect.height();
            } else {
                dr.mDrawableHeightEnd = 0;
                dr.mDrawableSizeEnd = 0;
            }
            resetResolvedDrawables();
            resolveDrawables();
            applyCompoundDrawableTint();
        }
    }

    @Override // android.view.View
    @RemotableViewMethod
    public void setEnabled(boolean enabled) {
        InputMethodManager imm;
        InputMethodManager imm2;
        if (enabled == isEnabled()) {
            return;
        }
        if (!enabled && (imm2 = InputMethodManager.peekInstance()) != null && imm2.isActive(this)) {
            imm2.hideSoftInputFromWindow(getWindowToken(), 0);
        }
        super.setEnabled(enabled);
        if (enabled && (imm = InputMethodManager.peekInstance()) != null) {
            imm.restartInput(this);
        }
        if (this.mEditor != null) {
            this.mEditor.invalidateTextDisplayList();
            this.mEditor.prepareCursorControllers();
            this.mEditor.makeBlink();
        }
    }

    public void setTypeface(Typeface tf, int style) {
        Typeface tf2;
        if (style > 0) {
            if (tf == null) {
                tf2 = Typeface.defaultFromStyle(style);
            } else {
                tf2 = Typeface.create(tf, style);
            }
            setTypeface(tf2);
            int typefaceStyle = tf2 != null ? tf2.getStyle() : 0;
            int need = (~typefaceStyle) & style;
            this.mTextPaint.setFakeBoldText((need & 1) != 0);
            this.mTextPaint.setTextSkewX((need & 2) != 0 ? -0.25f : 0.0f);
            return;
        }
        this.mTextPaint.setFakeBoldText(false);
        this.mTextPaint.setTextSkewX(0.0f);
        setTypeface(tf);
    }

    protected boolean getDefaultEditable() {
        return false;
    }

    protected MovementMethod getDefaultMovementMethod() {
        return null;
    }

    @ViewDebug.CapturedViewProperty
    public CharSequence getText() {
        return this.mText;
    }

    public int length() {
        return this.mText.length();
    }

    public Editable getEditableText() {
        if (this.mText instanceof Editable) {
            return (Editable) this.mText;
        }
        return null;
    }

    public int getLineHeight() {
        return FastMath.round((this.mTextPaint.getFontMetricsInt(null) * this.mSpacingMult) + this.mSpacingAdd);
    }

    public final Layout getLayout() {
        return this.mLayout;
    }

    public private protected final Layout getHintLayout() {
        return this.mHintLayout;
    }

    public final synchronized UndoManager getUndoManager() {
        throw new UnsupportedOperationException("not implemented");
    }

    @VisibleForTesting
    public final synchronized Editor getEditorForTesting() {
        return this.mEditor;
    }

    public final synchronized void setUndoManager(UndoManager undoManager, String tag) {
        throw new UnsupportedOperationException("not implemented");
    }

    public final KeyListener getKeyListener() {
        if (this.mEditor == null) {
            return null;
        }
        return this.mEditor.mKeyListener;
    }

    public void setKeyListener(KeyListener input) {
        this.mListenerChanged = true;
        setKeyListenerOnly(input);
        fixFocusableAndClickableSettings();
        if (input != null) {
            createEditorIfNeeded();
            setInputTypeFromEditor();
        } else if (this.mEditor != null) {
            this.mEditor.mInputType = 0;
        }
        InputMethodManager imm = InputMethodManager.peekInstance();
        if (imm != null) {
            imm.restartInput(this);
        }
    }

    private synchronized void setInputTypeFromEditor() {
        try {
            this.mEditor.mInputType = this.mEditor.mKeyListener.getInputType();
        } catch (IncompatibleClassChangeError e) {
            this.mEditor.mInputType = 1;
        }
        setInputTypeSingleLine(this.mSingleLine);
    }

    private synchronized void setKeyListenerOnly(KeyListener input) {
        if (this.mEditor == null && input == null) {
            return;
        }
        createEditorIfNeeded();
        if (this.mEditor.mKeyListener != input) {
            this.mEditor.mKeyListener = input;
            if (input != null && !(this.mText instanceof Editable)) {
                setText(this.mText);
            }
            setFilters((Editable) this.mText, this.mFilters);
        }
    }

    public final MovementMethod getMovementMethod() {
        return this.mMovement;
    }

    public final void setMovementMethod(MovementMethod movement) {
        if (this.mMovement != movement) {
            this.mMovement = movement;
            if (movement != null && this.mSpannable == null) {
                setText(this.mText);
            }
            fixFocusableAndClickableSettings();
            if (this.mEditor != null) {
                this.mEditor.prepareCursorControllers();
            }
        }
    }

    private synchronized void fixFocusableAndClickableSettings() {
        if (this.mMovement != null || (this.mEditor != null && this.mEditor.mKeyListener != null)) {
            setFocusable(1);
            setClickable(true);
            setLongClickable(true);
            return;
        }
        setFocusable(16);
        setClickable(false);
        setLongClickable(false);
    }

    public final TransformationMethod getTransformationMethod() {
        return this.mTransformation;
    }

    public final void setTransformationMethod(TransformationMethod method) {
        if (method == this.mTransformation) {
            return;
        }
        if (this.mTransformation != null && this.mSpannable != null) {
            this.mSpannable.removeSpan(this.mTransformation);
        }
        this.mTransformation = method;
        if (method instanceof TransformationMethod2) {
            TransformationMethod2 method2 = (TransformationMethod2) method;
            this.mAllowTransformationLengthChange = (isTextSelectable() || (this.mText instanceof Editable)) ? false : true;
            method2.setLengthChangesAllowed(this.mAllowTransformationLengthChange);
        } else {
            this.mAllowTransformationLengthChange = false;
        }
        setText(this.mText);
        if (hasPasswordTransformationMethod()) {
            notifyViewAccessibilityStateChangedIfNeeded(0);
        }
        this.mTextDir = getTextDirectionHeuristic();
    }

    public int getCompoundPaddingTop() {
        Drawables dr = this.mDrawables;
        if (dr == null || dr.mShowing[1] == null) {
            return this.mPaddingTop;
        }
        return this.mPaddingTop + dr.mDrawablePadding + dr.mDrawableSizeTop;
    }

    public int getCompoundPaddingBottom() {
        Drawables dr = this.mDrawables;
        if (dr == null || dr.mShowing[3] == null) {
            return this.mPaddingBottom;
        }
        return this.mPaddingBottom + dr.mDrawablePadding + dr.mDrawableSizeBottom;
    }

    public int getCompoundPaddingLeft() {
        Drawables dr = this.mDrawables;
        if (dr == null || dr.mShowing[0] == null) {
            return this.mPaddingLeft;
        }
        return this.mPaddingLeft + dr.mDrawablePadding + dr.mDrawableSizeLeft;
    }

    public int getCompoundPaddingRight() {
        Drawables dr = this.mDrawables;
        if (dr == null || dr.mShowing[2] == null) {
            return this.mPaddingRight;
        }
        return this.mPaddingRight + dr.mDrawablePadding + dr.mDrawableSizeRight;
    }

    public int getCompoundPaddingStart() {
        resolveDrawables();
        if (getLayoutDirection() != 1) {
            return getCompoundPaddingLeft();
        }
        return getCompoundPaddingRight();
    }

    public int getCompoundPaddingEnd() {
        resolveDrawables();
        if (getLayoutDirection() != 1) {
            return getCompoundPaddingRight();
        }
        return getCompoundPaddingLeft();
    }

    public int getExtendedPaddingTop() {
        if (this.mMaxMode != 1) {
            return getCompoundPaddingTop();
        }
        if (this.mLayout == null) {
            assumeLayout();
        }
        if (this.mLayout.getLineCount() <= this.mMaximum) {
            return getCompoundPaddingTop();
        }
        int top = getCompoundPaddingTop();
        int bottom = getCompoundPaddingBottom();
        int viewht = (getHeight() - top) - bottom;
        int layoutht = this.mLayout.getLineTop(this.mMaximum);
        if (layoutht >= viewht) {
            return top;
        }
        int gravity = this.mGravity & 112;
        if (gravity == 48) {
            return top;
        }
        if (gravity == 80) {
            return (top + viewht) - layoutht;
        }
        return ((viewht - layoutht) / 2) + top;
    }

    public int getExtendedPaddingBottom() {
        if (this.mMaxMode != 1) {
            return getCompoundPaddingBottom();
        }
        if (this.mLayout == null) {
            assumeLayout();
        }
        if (this.mLayout.getLineCount() <= this.mMaximum) {
            return getCompoundPaddingBottom();
        }
        int top = getCompoundPaddingTop();
        int bottom = getCompoundPaddingBottom();
        int viewht = (getHeight() - top) - bottom;
        int layoutht = this.mLayout.getLineTop(this.mMaximum);
        if (layoutht >= viewht) {
            return bottom;
        }
        int gravity = this.mGravity & 112;
        if (gravity == 48) {
            return (bottom + viewht) - layoutht;
        }
        if (gravity == 80) {
            return bottom;
        }
        return ((viewht - layoutht) / 2) + bottom;
    }

    public int getTotalPaddingLeft() {
        return getCompoundPaddingLeft();
    }

    public int getTotalPaddingRight() {
        return getCompoundPaddingRight();
    }

    public int getTotalPaddingStart() {
        return getCompoundPaddingStart();
    }

    public int getTotalPaddingEnd() {
        return getCompoundPaddingEnd();
    }

    public int getTotalPaddingTop() {
        return getExtendedPaddingTop() + getVerticalOffset(true);
    }

    public int getTotalPaddingBottom() {
        return getExtendedPaddingBottom() + getBottomVerticalOffset(true);
    }

    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        Drawables dr = this.mDrawables;
        if (dr != null) {
            if (dr.mDrawableStart != null) {
                dr.mDrawableStart.setCallback(null);
            }
            dr.mDrawableStart = null;
            if (dr.mDrawableEnd != null) {
                dr.mDrawableEnd.setCallback(null);
            }
            dr.mDrawableEnd = null;
            dr.mDrawableHeightStart = 0;
            dr.mDrawableSizeStart = 0;
            dr.mDrawableHeightEnd = 0;
            dr.mDrawableSizeEnd = 0;
        }
        boolean drawables = (left == null && top == null && right == null && bottom == null) ? false : true;
        if (!drawables) {
            if (dr != null) {
                if (dr.hasMetadata()) {
                    int i = dr.mShowing.length - 1;
                    while (true) {
                        int i2 = i;
                        if (i2 < 0) {
                            break;
                        }
                        if (dr.mShowing[i2] != null) {
                            dr.mShowing[i2].setCallback(null);
                        }
                        dr.mShowing[i2] = null;
                        i = i2 - 1;
                    }
                    dr.mDrawableHeightLeft = 0;
                    dr.mDrawableSizeLeft = 0;
                    dr.mDrawableHeightRight = 0;
                    dr.mDrawableSizeRight = 0;
                    dr.mDrawableWidthTop = 0;
                    dr.mDrawableSizeTop = 0;
                    dr.mDrawableWidthBottom = 0;
                    dr.mDrawableSizeBottom = 0;
                } else {
                    this.mDrawables = null;
                }
            }
        } else {
            if (dr == null) {
                Drawables drawables2 = new Drawables(getContext());
                dr = drawables2;
                this.mDrawables = drawables2;
            }
            this.mDrawables.mOverride = false;
            if (dr.mShowing[0] != left && dr.mShowing[0] != null) {
                dr.mShowing[0].setCallback(null);
            }
            dr.mShowing[0] = left;
            if (dr.mShowing[1] != top && dr.mShowing[1] != null) {
                dr.mShowing[1].setCallback(null);
            }
            dr.mShowing[1] = top;
            if (dr.mShowing[2] != right && dr.mShowing[2] != null) {
                dr.mShowing[2].setCallback(null);
            }
            dr.mShowing[2] = right;
            if (dr.mShowing[3] != bottom && dr.mShowing[3] != null) {
                dr.mShowing[3].setCallback(null);
            }
            dr.mShowing[3] = bottom;
            Rect compoundRect = dr.mCompoundRect;
            int[] state = getDrawableState();
            if (left != null) {
                left.setState(state);
                left.copyBounds(compoundRect);
                left.setCallback(this);
                dr.mDrawableSizeLeft = compoundRect.width();
                dr.mDrawableHeightLeft = compoundRect.height();
            } else {
                dr.mDrawableHeightLeft = 0;
                dr.mDrawableSizeLeft = 0;
            }
            if (right != null) {
                right.setState(state);
                right.copyBounds(compoundRect);
                right.setCallback(this);
                dr.mDrawableSizeRight = compoundRect.width();
                dr.mDrawableHeightRight = compoundRect.height();
            } else {
                dr.mDrawableHeightRight = 0;
                dr.mDrawableSizeRight = 0;
            }
            if (top != null) {
                top.setState(state);
                top.copyBounds(compoundRect);
                top.setCallback(this);
                dr.mDrawableSizeTop = compoundRect.height();
                dr.mDrawableWidthTop = compoundRect.width();
            } else {
                dr.mDrawableWidthTop = 0;
                dr.mDrawableSizeTop = 0;
            }
            if (bottom != null) {
                bottom.setState(state);
                bottom.copyBounds(compoundRect);
                bottom.setCallback(this);
                dr.mDrawableSizeBottom = compoundRect.height();
                dr.mDrawableWidthBottom = compoundRect.width();
            } else {
                dr.mDrawableWidthBottom = 0;
                dr.mDrawableSizeBottom = 0;
            }
        }
        if (dr != null) {
            dr.mDrawableLeftInitial = left;
            dr.mDrawableRightInitial = right;
        }
        resetResolvedDrawables();
        resolveDrawables();
        applyCompoundDrawableTint();
        invalidate();
        requestLayout();
    }

    @RemotableViewMethod
    public void setCompoundDrawablesWithIntrinsicBounds(int left, int top, int right, int bottom) {
        Context context = getContext();
        setCompoundDrawablesWithIntrinsicBounds(left != 0 ? context.getDrawable(left) : null, top != 0 ? context.getDrawable(top) : null, right != 0 ? context.getDrawable(right) : null, bottom != 0 ? context.getDrawable(bottom) : null);
    }

    @RemotableViewMethod
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        if (left != null) {
            left.setBounds(0, 0, left.getIntrinsicWidth(), left.getIntrinsicHeight());
        }
        if (right != null) {
            right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
        }
        if (top != null) {
            top.setBounds(0, 0, top.getIntrinsicWidth(), top.getIntrinsicHeight());
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, bottom.getIntrinsicWidth(), bottom.getIntrinsicHeight());
        }
        setCompoundDrawables(left, top, right, bottom);
    }

    @RemotableViewMethod
    public void setCompoundDrawablesRelative(Drawable start, Drawable top, Drawable end, Drawable bottom) {
        boolean drawables;
        Drawables dr = this.mDrawables;
        if (dr != null) {
            if (dr.mShowing[0] != null) {
                dr.mShowing[0].setCallback(null);
            }
            Drawable[] drawableArr = dr.mShowing;
            dr.mDrawableLeftInitial = null;
            drawableArr[0] = null;
            if (dr.mShowing[2] != null) {
                dr.mShowing[2].setCallback(null);
            }
            Drawable[] drawableArr2 = dr.mShowing;
            dr.mDrawableRightInitial = null;
            drawableArr2[2] = null;
            dr.mDrawableHeightLeft = 0;
            dr.mDrawableSizeLeft = 0;
            dr.mDrawableHeightRight = 0;
            dr.mDrawableSizeRight = 0;
        }
        if (start == null && top == null && end == null && bottom == null) {
            drawables = false;
        } else {
            drawables = true;
        }
        if (!drawables) {
            if (dr != null) {
                if (!dr.hasMetadata()) {
                    this.mDrawables = null;
                } else {
                    if (dr.mDrawableStart != null) {
                        dr.mDrawableStart.setCallback(null);
                    }
                    dr.mDrawableStart = null;
                    if (dr.mShowing[1] != null) {
                        dr.mShowing[1].setCallback(null);
                    }
                    dr.mShowing[1] = null;
                    if (dr.mDrawableEnd != null) {
                        dr.mDrawableEnd.setCallback(null);
                    }
                    dr.mDrawableEnd = null;
                    if (dr.mShowing[3] != null) {
                        dr.mShowing[3].setCallback(null);
                    }
                    dr.mShowing[3] = null;
                    dr.mDrawableHeightStart = 0;
                    dr.mDrawableSizeStart = 0;
                    dr.mDrawableHeightEnd = 0;
                    dr.mDrawableSizeEnd = 0;
                    dr.mDrawableWidthTop = 0;
                    dr.mDrawableSizeTop = 0;
                    dr.mDrawableWidthBottom = 0;
                    dr.mDrawableSizeBottom = 0;
                }
            }
        } else {
            if (dr == null) {
                Drawables drawables2 = new Drawables(getContext());
                dr = drawables2;
                this.mDrawables = drawables2;
            }
            this.mDrawables.mOverride = true;
            if (dr.mDrawableStart != start && dr.mDrawableStart != null) {
                dr.mDrawableStart.setCallback(null);
            }
            dr.mDrawableStart = start;
            if (dr.mShowing[1] != top && dr.mShowing[1] != null) {
                dr.mShowing[1].setCallback(null);
            }
            dr.mShowing[1] = top;
            if (dr.mDrawableEnd != end && dr.mDrawableEnd != null) {
                dr.mDrawableEnd.setCallback(null);
            }
            dr.mDrawableEnd = end;
            if (dr.mShowing[3] != bottom && dr.mShowing[3] != null) {
                dr.mShowing[3].setCallback(null);
            }
            dr.mShowing[3] = bottom;
            Rect compoundRect = dr.mCompoundRect;
            int[] state = getDrawableState();
            if (start != null) {
                start.setState(state);
                start.copyBounds(compoundRect);
                start.setCallback(this);
                dr.mDrawableSizeStart = compoundRect.width();
                dr.mDrawableHeightStart = compoundRect.height();
            } else {
                dr.mDrawableHeightStart = 0;
                dr.mDrawableSizeStart = 0;
            }
            if (end != null) {
                end.setState(state);
                end.copyBounds(compoundRect);
                end.setCallback(this);
                dr.mDrawableSizeEnd = compoundRect.width();
                dr.mDrawableHeightEnd = compoundRect.height();
            } else {
                dr.mDrawableHeightEnd = 0;
                dr.mDrawableSizeEnd = 0;
            }
            if (top != null) {
                top.setState(state);
                top.copyBounds(compoundRect);
                top.setCallback(this);
                dr.mDrawableSizeTop = compoundRect.height();
                dr.mDrawableWidthTop = compoundRect.width();
            } else {
                dr.mDrawableWidthTop = 0;
                dr.mDrawableSizeTop = 0;
            }
            if (bottom != null) {
                bottom.setState(state);
                bottom.copyBounds(compoundRect);
                bottom.setCallback(this);
                dr.mDrawableSizeBottom = compoundRect.height();
                dr.mDrawableWidthBottom = compoundRect.width();
            } else {
                dr.mDrawableWidthBottom = 0;
                dr.mDrawableSizeBottom = 0;
            }
        }
        resetResolvedDrawables();
        resolveDrawables();
        invalidate();
        requestLayout();
    }

    @RemotableViewMethod
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(int start, int top, int end, int bottom) {
        Context context = getContext();
        setCompoundDrawablesRelativeWithIntrinsicBounds(start != 0 ? context.getDrawable(start) : null, top != 0 ? context.getDrawable(top) : null, end != 0 ? context.getDrawable(end) : null, bottom != 0 ? context.getDrawable(bottom) : null);
    }

    @RemotableViewMethod
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(Drawable start, Drawable top, Drawable end, Drawable bottom) {
        if (start != null) {
            start.setBounds(0, 0, start.getIntrinsicWidth(), start.getIntrinsicHeight());
        }
        if (end != null) {
            end.setBounds(0, 0, end.getIntrinsicWidth(), end.getIntrinsicHeight());
        }
        if (top != null) {
            top.setBounds(0, 0, top.getIntrinsicWidth(), top.getIntrinsicHeight());
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, bottom.getIntrinsicWidth(), bottom.getIntrinsicHeight());
        }
        setCompoundDrawablesRelative(start, top, end, bottom);
    }

    public Drawable[] getCompoundDrawables() {
        Drawables dr = this.mDrawables;
        return dr != null ? (Drawable[]) dr.mShowing.clone() : new Drawable[]{null, null, null, null};
    }

    public Drawable[] getCompoundDrawablesRelative() {
        Drawables dr = this.mDrawables;
        return dr != null ? new Drawable[]{dr.mDrawableStart, dr.mShowing[1], dr.mDrawableEnd, dr.mShowing[3]} : new Drawable[]{null, null, null, null};
    }

    @RemotableViewMethod
    public void setCompoundDrawablePadding(int pad) {
        Drawables dr = this.mDrawables;
        if (pad == 0) {
            if (dr != null) {
                dr.mDrawablePadding = pad;
            }
        } else {
            if (dr == null) {
                Drawables drawables = new Drawables(getContext());
                dr = drawables;
                this.mDrawables = drawables;
            }
            dr.mDrawablePadding = pad;
        }
        invalidate();
        requestLayout();
    }

    public int getCompoundDrawablePadding() {
        Drawables dr = this.mDrawables;
        if (dr != null) {
            return dr.mDrawablePadding;
        }
        return 0;
    }

    public void setCompoundDrawableTintList(ColorStateList tint) {
        if (this.mDrawables == null) {
            this.mDrawables = new Drawables(getContext());
        }
        this.mDrawables.mTintList = tint;
        this.mDrawables.mHasTint = true;
        applyCompoundDrawableTint();
    }

    public ColorStateList getCompoundDrawableTintList() {
        if (this.mDrawables != null) {
            return this.mDrawables.mTintList;
        }
        return null;
    }

    public void setCompoundDrawableTintMode(PorterDuff.Mode tintMode) {
        if (this.mDrawables == null) {
            this.mDrawables = new Drawables(getContext());
        }
        this.mDrawables.mTintMode = tintMode;
        this.mDrawables.mHasTintMode = true;
        applyCompoundDrawableTint();
    }

    public PorterDuff.Mode getCompoundDrawableTintMode() {
        if (this.mDrawables != null) {
            return this.mDrawables.mTintMode;
        }
        return null;
    }

    private synchronized void applyCompoundDrawableTint() {
        Drawable[] drawableArr;
        if (this.mDrawables == null) {
            return;
        }
        if (this.mDrawables.mHasTint || this.mDrawables.mHasTintMode) {
            ColorStateList tintList = this.mDrawables.mTintList;
            PorterDuff.Mode tintMode = this.mDrawables.mTintMode;
            boolean hasTint = this.mDrawables.mHasTint;
            boolean hasTintMode = this.mDrawables.mHasTintMode;
            int[] state = getDrawableState();
            for (Drawable dr : this.mDrawables.mShowing) {
                if (dr != null && dr != this.mDrawables.mDrawableError) {
                    dr.mutate();
                    if (hasTint) {
                        dr.setTintList(tintList);
                    }
                    if (hasTintMode) {
                        dr.setTintMode(tintMode);
                    }
                    if (dr.isStateful()) {
                        dr.setState(state);
                    }
                }
            }
        }
    }

    @Override // android.view.View
    public void setPadding(int left, int top, int right, int bottom) {
        if (left != this.mPaddingLeft || right != this.mPaddingRight || top != this.mPaddingTop || bottom != this.mPaddingBottom) {
            nullLayouts();
        }
        super.setPadding(left, top, right, bottom);
        invalidate();
    }

    @Override // android.view.View
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        if (start != getPaddingStart() || end != getPaddingEnd() || top != this.mPaddingTop || bottom != this.mPaddingBottom) {
            nullLayouts();
        }
        super.setPaddingRelative(start, top, end, bottom);
        invalidate();
    }

    public void setFirstBaselineToTopHeight(int firstBaselineToTopHeight) {
        int fontMetricsTop;
        Preconditions.checkArgumentNonnegative(firstBaselineToTopHeight);
        Paint.FontMetricsInt fontMetrics = getPaint().getFontMetricsInt();
        if (getIncludeFontPadding()) {
            fontMetricsTop = fontMetrics.top;
        } else {
            fontMetricsTop = fontMetrics.ascent;
        }
        if (firstBaselineToTopHeight > Math.abs(fontMetricsTop)) {
            int paddingTop = firstBaselineToTopHeight - (-fontMetricsTop);
            setPadding(getPaddingLeft(), paddingTop, getPaddingRight(), getPaddingBottom());
        }
    }

    public void setLastBaselineToBottomHeight(int lastBaselineToBottomHeight) {
        int fontMetricsBottom;
        Preconditions.checkArgumentNonnegative(lastBaselineToBottomHeight);
        Paint.FontMetricsInt fontMetrics = getPaint().getFontMetricsInt();
        if (getIncludeFontPadding()) {
            fontMetricsBottom = fontMetrics.bottom;
        } else {
            fontMetricsBottom = fontMetrics.descent;
        }
        if (lastBaselineToBottomHeight > Math.abs(fontMetricsBottom)) {
            int paddingBottom = lastBaselineToBottomHeight - fontMetricsBottom;
            setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), paddingBottom);
        }
    }

    public int getFirstBaselineToTopHeight() {
        return getPaddingTop() - getPaint().getFontMetricsInt().top;
    }

    public int getLastBaselineToBottomHeight() {
        return getPaddingBottom() + getPaint().getFontMetricsInt().bottom;
    }

    public final int getAutoLinkMask() {
        return this.mAutoLinkMask;
    }

    public void setTextAppearance(int resId) {
        setTextAppearance(this.mContext, resId);
    }

    @Deprecated
    public void setTextAppearance(Context context, int resId) {
        TypedArray ta = context.obtainStyledAttributes(resId, R.styleable.TextAppearance);
        TextAppearanceAttributes attributes = new TextAppearanceAttributes();
        readTextAppearance(context, ta, attributes, false);
        ta.recycle();
        applyTextAppearance(attributes);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class TextAppearanceAttributes {
        boolean mAllCaps;
        boolean mElegant;
        boolean mFallbackLineSpacing;
        String mFontFamily;
        boolean mFontFamilyExplicit;
        String mFontFeatureSettings;
        Typeface mFontTypeface;
        int mFontWeight;
        boolean mHasElegant;
        boolean mHasFallbackLineSpacing;
        boolean mHasLetterSpacing;
        float mLetterSpacing;
        int mShadowColor;
        float mShadowDx;
        float mShadowDy;
        float mShadowRadius;
        int mStyleIndex;
        ColorStateList mTextColor;
        int mTextColorHighlight;
        ColorStateList mTextColorHint;
        ColorStateList mTextColorLink;
        int mTextSize;
        int mTypefaceIndex;

        private synchronized TextAppearanceAttributes() {
            this.mTextColorHighlight = 0;
            this.mTextColor = null;
            this.mTextColorHint = null;
            this.mTextColorLink = null;
            this.mTextSize = 0;
            this.mFontFamily = null;
            this.mFontTypeface = null;
            this.mFontFamilyExplicit = false;
            this.mTypefaceIndex = -1;
            this.mStyleIndex = -1;
            this.mFontWeight = -1;
            this.mAllCaps = false;
            this.mShadowColor = 0;
            this.mShadowDx = 0.0f;
            this.mShadowDy = 0.0f;
            this.mShadowRadius = 0.0f;
            this.mHasElegant = false;
            this.mElegant = false;
            this.mHasFallbackLineSpacing = false;
            this.mFallbackLineSpacing = false;
            this.mHasLetterSpacing = false;
            this.mLetterSpacing = 0.0f;
            this.mFontFeatureSettings = null;
        }

        public String toString() {
            return "TextAppearanceAttributes {\n    mTextColorHighlight:" + this.mTextColorHighlight + "\n    mTextColor:" + this.mTextColor + "\n    mTextColorHint:" + this.mTextColorHint + "\n    mTextColorLink:" + this.mTextColorLink + "\n    mTextSize:" + this.mTextSize + "\n    mFontFamily:" + this.mFontFamily + "\n    mFontTypeface:" + this.mFontTypeface + "\n    mFontFamilyExplicit:" + this.mFontFamilyExplicit + "\n    mTypefaceIndex:" + this.mTypefaceIndex + "\n    mStyleIndex:" + this.mStyleIndex + "\n    mFontWeight:" + this.mFontWeight + "\n    mAllCaps:" + this.mAllCaps + "\n    mShadowColor:" + this.mShadowColor + "\n    mShadowDx:" + this.mShadowDx + "\n    mShadowDy:" + this.mShadowDy + "\n    mShadowRadius:" + this.mShadowRadius + "\n    mHasElegant:" + this.mHasElegant + "\n    mElegant:" + this.mElegant + "\n    mHasFallbackLineSpacing:" + this.mHasFallbackLineSpacing + "\n    mFallbackLineSpacing:" + this.mFallbackLineSpacing + "\n    mHasLetterSpacing:" + this.mHasLetterSpacing + "\n    mLetterSpacing:" + this.mLetterSpacing + "\n    mFontFeatureSettings:" + this.mFontFeatureSettings + "\n}";
        }
    }

    private synchronized void readTextAppearance(Context context, TypedArray appearance, TextAppearanceAttributes attributes, boolean styleArray) {
        int n = appearance.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = appearance.getIndex(i);
            int index = attr;
            if (!styleArray || (index = sAppearanceValues.get(attr, -1)) != -1) {
                switch (index) {
                    case 0:
                        attributes.mTextSize = appearance.getDimensionPixelSize(attr, attributes.mTextSize);
                        continue;
                    case 1:
                        attributes.mTypefaceIndex = appearance.getInt(attr, attributes.mTypefaceIndex);
                        if (attributes.mTypefaceIndex != -1) {
                            if (attributes.mFontFamilyExplicit) {
                                break;
                            } else {
                                attributes.mFontFamily = null;
                                break;
                            }
                        } else {
                            continue;
                        }
                    case 2:
                        attributes.mStyleIndex = appearance.getInt(attr, attributes.mStyleIndex);
                        continue;
                    case 3:
                        attributes.mTextColor = appearance.getColorStateList(attr);
                        continue;
                    case 4:
                        attributes.mTextColorHighlight = appearance.getColor(attr, attributes.mTextColorHighlight);
                        continue;
                    case 5:
                        attributes.mTextColorHint = appearance.getColorStateList(attr);
                        continue;
                    case 6:
                        attributes.mTextColorLink = appearance.getColorStateList(attr);
                        continue;
                    case 7:
                        attributes.mShadowColor = appearance.getInt(attr, attributes.mShadowColor);
                        continue;
                    case 8:
                        attributes.mShadowDx = appearance.getFloat(attr, attributes.mShadowDx);
                        continue;
                    case 9:
                        attributes.mShadowDy = appearance.getFloat(attr, attributes.mShadowDy);
                        continue;
                    case 10:
                        attributes.mShadowRadius = appearance.getFloat(attr, attributes.mShadowRadius);
                        continue;
                    case 11:
                        attributes.mAllCaps = appearance.getBoolean(attr, attributes.mAllCaps);
                        continue;
                    case 12:
                        if (!context.isRestricted() && context.canLoadUnsafeResources()) {
                            try {
                                attributes.mFontTypeface = appearance.getFont(attr);
                            } catch (Resources.NotFoundException | UnsupportedOperationException e) {
                            }
                        }
                        if (attributes.mFontTypeface == null) {
                            attributes.mFontFamily = appearance.getString(attr);
                        }
                        attributes.mFontFamilyExplicit = true;
                        continue;
                    case 13:
                        attributes.mHasElegant = true;
                        attributes.mElegant = appearance.getBoolean(attr, attributes.mElegant);
                        continue;
                    case 14:
                        attributes.mHasLetterSpacing = true;
                        attributes.mLetterSpacing = appearance.getFloat(attr, attributes.mLetterSpacing);
                        continue;
                    case 15:
                        attributes.mFontFeatureSettings = appearance.getString(attr);
                        continue;
                    case 16:
                        attributes.mHasFallbackLineSpacing = true;
                        attributes.mFallbackLineSpacing = appearance.getBoolean(attr, attributes.mFallbackLineSpacing);
                        continue;
                    case 17:
                        attributes.mFontWeight = appearance.getInt(attr, attributes.mFontWeight);
                        continue;
                }
            }
        }
    }

    private synchronized void applyTextAppearance(TextAppearanceAttributes attributes) {
        if (attributes.mTextColor != null) {
            setTextColor(attributes.mTextColor);
        }
        if (attributes.mTextColorHint != null) {
            setHintTextColor(attributes.mTextColorHint);
        }
        if (attributes.mTextColorLink != null) {
            setLinkTextColor(attributes.mTextColorLink);
        }
        if (attributes.mTextColorHighlight != 0) {
            setHighlightColor(attributes.mTextColorHighlight);
        }
        if (attributes.mTextSize != 0) {
            setRawTextSize(attributes.mTextSize, true);
        }
        if (attributes.mTypefaceIndex != -1 && !attributes.mFontFamilyExplicit) {
            attributes.mFontFamily = null;
        }
        setTypefaceFromAttrs(attributes.mFontTypeface, attributes.mFontFamily, attributes.mTypefaceIndex, attributes.mStyleIndex, attributes.mFontWeight);
        if (attributes.mShadowColor != 0) {
            setShadowLayer(attributes.mShadowRadius, attributes.mShadowDx, attributes.mShadowDy, attributes.mShadowColor);
        }
        if (attributes.mAllCaps) {
            setTransformationMethod(new AllCapsTransformationMethod(getContext()));
        }
        if (attributes.mHasElegant) {
            setElegantTextHeight(attributes.mElegant);
        }
        if (attributes.mHasFallbackLineSpacing) {
            setFallbackLineSpacing(attributes.mFallbackLineSpacing);
        }
        if (attributes.mHasLetterSpacing) {
            setLetterSpacing(attributes.mLetterSpacing);
        }
        if (attributes.mFontFeatureSettings != null) {
            setFontFeatureSettings(attributes.mFontFeatureSettings);
        }
    }

    public Locale getTextLocale() {
        return this.mTextPaint.getTextLocale();
    }

    public LocaleList getTextLocales() {
        return this.mTextPaint.getTextLocales();
    }

    private synchronized void changeListenerLocaleTo(Locale locale) {
        KeyListener listener;
        if (!this.mListenerChanged && this.mEditor != null) {
            KeyListener listener2 = this.mEditor.mKeyListener;
            if (listener2 instanceof DigitsKeyListener) {
                listener = DigitsKeyListener.getInstance(locale, (DigitsKeyListener) listener2);
            } else if (listener2 instanceof DateKeyListener) {
                listener = DateKeyListener.getInstance(locale);
            } else if (listener2 instanceof TimeKeyListener) {
                listener = TimeKeyListener.getInstance(locale);
            } else if (listener2 instanceof DateTimeKeyListener) {
                listener = DateTimeKeyListener.getInstance(locale);
            } else {
                return;
            }
            boolean wasPasswordType = isPasswordInputType(this.mEditor.mInputType);
            setKeyListenerOnly(listener);
            setInputTypeFromEditor();
            if (wasPasswordType) {
                int newInputClass = this.mEditor.mInputType & 15;
                if (newInputClass == 1) {
                    this.mEditor.mInputType |= 128;
                } else if (newInputClass == 2) {
                    this.mEditor.mInputType |= 16;
                }
            }
        }
    }

    public void setTextLocale(Locale locale) {
        this.mLocalesChanged = true;
        this.mTextPaint.setTextLocale(locale);
        if (this.mLayout != null) {
            nullLayouts();
            requestLayout();
            invalidate();
        }
    }

    public void setTextLocales(LocaleList locales) {
        this.mLocalesChanged = true;
        this.mTextPaint.setTextLocales(locales);
        if (this.mLayout != null) {
            nullLayouts();
            requestLayout();
            invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (!this.mLocalesChanged) {
            this.mTextPaint.setTextLocales(LocaleList.getDefault());
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    @ViewDebug.ExportedProperty(category = "text")
    public float getTextSize() {
        return this.mTextPaint.getTextSize();
    }

    @ViewDebug.ExportedProperty(category = "text")
    public synchronized float getScaledTextSize() {
        return this.mTextPaint.getTextSize() / this.mTextPaint.density;
    }

    @ViewDebug.ExportedProperty(category = "text", mapping = {@ViewDebug.IntToString(from = 0, to = "NORMAL"), @ViewDebug.IntToString(from = 1, to = "BOLD"), @ViewDebug.IntToString(from = 2, to = "ITALIC"), @ViewDebug.IntToString(from = 3, to = "BOLD_ITALIC")})
    public synchronized int getTypefaceStyle() {
        Typeface typeface = this.mTextPaint.getTypeface();
        if (typeface != null) {
            return typeface.getStyle();
        }
        return 0;
    }

    @RemotableViewMethod
    public void setTextSize(float size) {
        setTextSize(2, size);
    }

    public void setTextSize(int unit, float size) {
        if (!isAutoSizeEnabled()) {
            setTextSizeInternal(unit, size, true);
        }
    }

    private synchronized void setTextSizeInternal(int unit, float size, boolean shouldRequestLayout) {
        Resources r;
        Context c = getContext();
        if (c == null) {
            r = Resources.getSystem();
        } else {
            r = c.getResources();
        }
        setRawTextSize(TypedValue.applyDimension(unit, size, r.getDisplayMetrics()), shouldRequestLayout);
    }

    public protected void setRawTextSize(float size, boolean shouldRequestLayout) {
        if (size != this.mTextPaint.getTextSize()) {
            this.mTextPaint.setTextSize(size);
            if (shouldRequestLayout && this.mLayout != null) {
                this.mNeedsAutoSizeText = false;
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public float getTextScaleX() {
        return this.mTextPaint.getTextScaleX();
    }

    @RemotableViewMethod
    public void setTextScaleX(float size) {
        if (size != this.mTextPaint.getTextScaleX()) {
            this.mUserSetTextScaleX = true;
            this.mTextPaint.setTextScaleX(size);
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public void setTypeface(Typeface tf) {
        if (this.mTextPaint.getTypeface() != tf) {
            this.mTextPaint.setTypeface(tf);
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public Typeface getTypeface() {
        return this.mTextPaint.getTypeface();
    }

    public void setElegantTextHeight(boolean elegant) {
        if (elegant != this.mTextPaint.isElegantTextHeight()) {
            this.mTextPaint.setElegantTextHeight(elegant);
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public void setFallbackLineSpacing(boolean enabled) {
        if (this.mUseFallbackLineSpacing != enabled) {
            this.mUseFallbackLineSpacing = enabled;
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public boolean isFallbackLineSpacing() {
        return this.mUseFallbackLineSpacing;
    }

    public boolean isElegantTextHeight() {
        return this.mTextPaint.isElegantTextHeight();
    }

    public float getLetterSpacing() {
        return this.mTextPaint.getLetterSpacing();
    }

    @RemotableViewMethod
    public void setLetterSpacing(float letterSpacing) {
        if (letterSpacing != this.mTextPaint.getLetterSpacing()) {
            this.mTextPaint.setLetterSpacing(letterSpacing);
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public String getFontFeatureSettings() {
        return this.mTextPaint.getFontFeatureSettings();
    }

    public String getFontVariationSettings() {
        return this.mTextPaint.getFontVariationSettings();
    }

    public void setBreakStrategy(int breakStrategy) {
        this.mBreakStrategy = breakStrategy;
        if (this.mLayout != null) {
            nullLayouts();
            requestLayout();
            invalidate();
        }
    }

    public int getBreakStrategy() {
        return this.mBreakStrategy;
    }

    public void setHyphenationFrequency(int hyphenationFrequency) {
        this.mHyphenationFrequency = hyphenationFrequency;
        if (this.mLayout != null) {
            nullLayouts();
            requestLayout();
            invalidate();
        }
    }

    public int getHyphenationFrequency() {
        return this.mHyphenationFrequency;
    }

    public PrecomputedText.Params getTextMetricsParams() {
        return new PrecomputedText.Params(new TextPaint(this.mTextPaint), getTextDirectionHeuristic(), this.mBreakStrategy, this.mHyphenationFrequency);
    }

    public void setTextMetricsParams(PrecomputedText.Params params) {
        this.mTextPaint.set(params.getTextPaint());
        this.mUserSetTextScaleX = true;
        this.mTextDir = params.getTextDirection();
        this.mBreakStrategy = params.getBreakStrategy();
        this.mHyphenationFrequency = params.getHyphenationFrequency();
        if (this.mLayout != null) {
            nullLayouts();
            requestLayout();
            invalidate();
        }
    }

    public void setJustificationMode(int justificationMode) {
        this.mJustificationMode = justificationMode;
        if (this.mLayout != null) {
            nullLayouts();
            requestLayout();
            invalidate();
        }
    }

    public int getJustificationMode() {
        return this.mJustificationMode;
    }

    @RemotableViewMethod
    public void setFontFeatureSettings(String fontFeatureSettings) {
        if (fontFeatureSettings != this.mTextPaint.getFontFeatureSettings()) {
            this.mTextPaint.setFontFeatureSettings(fontFeatureSettings);
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public boolean setFontVariationSettings(String fontVariationSettings) {
        String existingSettings = this.mTextPaint.getFontVariationSettings();
        if (fontVariationSettings != existingSettings) {
            if (fontVariationSettings != null && fontVariationSettings.equals(existingSettings)) {
                return true;
            }
            boolean effective = this.mTextPaint.setFontVariationSettings(fontVariationSettings);
            if (effective && this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
            return effective;
        }
        return true;
    }

    @RemotableViewMethod
    public void setTextColor(int color) {
        this.mTextColor = ColorStateList.valueOf(color);
        updateTextColors();
    }

    @RemotableViewMethod
    public void setTextColor(ColorStateList colors) {
        if (colors == null) {
            throw new NullPointerException();
        }
        this.mTextColor = colors;
        updateTextColors();
    }

    public final ColorStateList getTextColors() {
        return this.mTextColor;
    }

    public final int getCurrentTextColor() {
        return this.mCurTextColor;
    }

    @RemotableViewMethod
    public void setHighlightColor(int color) {
        if (this.mHighlightColor != color) {
            this.mHighlightColor = color;
            invalidate();
        }
    }

    public int getHighlightColor() {
        return this.mHighlightColor;
    }

    @RemotableViewMethod
    public final void setShowSoftInputOnFocus(boolean show) {
        createEditorIfNeeded();
        this.mEditor.mShowSoftInputOnFocus = show;
    }

    public final boolean getShowSoftInputOnFocus() {
        return this.mEditor == null || this.mEditor.mShowSoftInputOnFocus;
    }

    public void setShadowLayer(float radius, float dx, float dy, int color) {
        this.mTextPaint.setShadowLayer(radius, dx, dy, color);
        this.mShadowRadius = radius;
        this.mShadowDx = dx;
        this.mShadowDy = dy;
        this.mShadowColor = color;
        if (this.mEditor != null) {
            this.mEditor.invalidateTextDisplayList();
            this.mEditor.invalidateHandlesAndActionMode();
        }
        invalidate();
    }

    public float getShadowRadius() {
        return this.mShadowRadius;
    }

    public float getShadowDx() {
        return this.mShadowDx;
    }

    public float getShadowDy() {
        return this.mShadowDy;
    }

    public int getShadowColor() {
        return this.mShadowColor;
    }

    public TextPaint getPaint() {
        return this.mTextPaint;
    }

    @RemotableViewMethod
    public final void setAutoLinkMask(int mask) {
        this.mAutoLinkMask = mask;
    }

    @RemotableViewMethod
    public final void setLinksClickable(boolean whether) {
        this.mLinksClickable = whether;
    }

    public final boolean getLinksClickable() {
        return this.mLinksClickable;
    }

    public URLSpan[] getUrls() {
        if (this.mText instanceof Spanned) {
            return (URLSpan[]) ((Spanned) this.mText).getSpans(0, this.mText.length(), URLSpan.class);
        }
        return new URLSpan[0];
    }

    @RemotableViewMethod
    public final void setHintTextColor(int color) {
        this.mHintTextColor = ColorStateList.valueOf(color);
        updateTextColors();
    }

    public final void setHintTextColor(ColorStateList colors) {
        this.mHintTextColor = colors;
        updateTextColors();
    }

    public final ColorStateList getHintTextColors() {
        return this.mHintTextColor;
    }

    public final int getCurrentHintTextColor() {
        return this.mHintTextColor != null ? this.mCurHintTextColor : this.mCurTextColor;
    }

    @RemotableViewMethod
    public final void setLinkTextColor(int color) {
        this.mLinkTextColor = ColorStateList.valueOf(color);
        updateTextColors();
    }

    public final void setLinkTextColor(ColorStateList colors) {
        this.mLinkTextColor = colors;
        updateTextColors();
    }

    public final ColorStateList getLinkTextColors() {
        return this.mLinkTextColor;
    }

    public void setGravity(int gravity) {
        if ((gravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) == 0) {
            gravity |= Gravity.START;
        }
        if ((gravity & 112) == 0) {
            gravity |= 48;
        }
        boolean newLayout = false;
        if ((gravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) != (8388615 & this.mGravity)) {
            newLayout = true;
        }
        if (gravity != this.mGravity) {
            invalidate();
        }
        this.mGravity = gravity;
        if (this.mLayout != null && newLayout) {
            int want = this.mLayout.getWidth();
            int hintWant = this.mHintLayout == null ? 0 : this.mHintLayout.getWidth();
            makeNewLayout(want, hintWant, UNKNOWN_BORING, UNKNOWN_BORING, ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight(), true);
        }
    }

    public int getGravity() {
        return this.mGravity;
    }

    public int getPaintFlags() {
        return this.mTextPaint.getFlags();
    }

    @RemotableViewMethod
    public void setPaintFlags(int flags) {
        if (this.mTextPaint.getFlags() != flags) {
            this.mTextPaint.setFlags(flags);
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public void setHorizontallyScrolling(boolean whether) {
        if (this.mHorizontallyScrolling != whether) {
            this.mHorizontallyScrolling = whether;
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getHorizontallyScrolling() {
        return this.mHorizontallyScrolling;
    }

    @RemotableViewMethod
    public void setMinLines(int minLines) {
        this.mMinimum = minLines;
        this.mMinMode = 1;
        requestLayout();
        invalidate();
    }

    public int getMinLines() {
        if (this.mMinMode == 1) {
            return this.mMinimum;
        }
        return -1;
    }

    @RemotableViewMethod
    public void setMinHeight(int minPixels) {
        this.mMinimum = minPixels;
        this.mMinMode = 2;
        requestLayout();
        invalidate();
    }

    public int getMinHeight() {
        if (this.mMinMode == 2) {
            return this.mMinimum;
        }
        return -1;
    }

    @RemotableViewMethod
    public void setMaxLines(int maxLines) {
        this.mMaximum = maxLines;
        this.mMaxMode = 1;
        requestLayout();
        invalidate();
    }

    public int getMaxLines() {
        if (this.mMaxMode == 1) {
            return this.mMaximum;
        }
        return -1;
    }

    @RemotableViewMethod
    public void setMaxHeight(int maxPixels) {
        this.mMaximum = maxPixels;
        this.mMaxMode = 2;
        requestLayout();
        invalidate();
    }

    public int getMaxHeight() {
        if (this.mMaxMode == 2) {
            return this.mMaximum;
        }
        return -1;
    }

    @RemotableViewMethod
    public void setLines(int lines) {
        this.mMinimum = lines;
        this.mMaximum = lines;
        this.mMinMode = 1;
        this.mMaxMode = 1;
        requestLayout();
        invalidate();
    }

    @RemotableViewMethod
    public void setHeight(int pixels) {
        this.mMinimum = pixels;
        this.mMaximum = pixels;
        this.mMinMode = 2;
        this.mMaxMode = 2;
        requestLayout();
        invalidate();
    }

    @RemotableViewMethod
    public void setMinEms(int minEms) {
        this.mMinWidth = minEms;
        this.mMinWidthMode = 1;
        requestLayout();
        invalidate();
    }

    public int getMinEms() {
        if (this.mMinWidthMode == 1) {
            return this.mMinWidth;
        }
        return -1;
    }

    @RemotableViewMethod
    public void setMinWidth(int minPixels) {
        this.mMinWidth = minPixels;
        this.mMinWidthMode = 2;
        requestLayout();
        invalidate();
    }

    public int getMinWidth() {
        if (this.mMinWidthMode == 2) {
            return this.mMinWidth;
        }
        return -1;
    }

    @RemotableViewMethod
    public void setMaxEms(int maxEms) {
        this.mMaxWidth = maxEms;
        this.mMaxWidthMode = 1;
        requestLayout();
        invalidate();
    }

    public int getMaxEms() {
        if (this.mMaxWidthMode == 1) {
            return this.mMaxWidth;
        }
        return -1;
    }

    @RemotableViewMethod
    public void setMaxWidth(int maxPixels) {
        this.mMaxWidth = maxPixels;
        this.mMaxWidthMode = 2;
        requestLayout();
        invalidate();
    }

    public int getMaxWidth() {
        if (this.mMaxWidthMode == 2) {
            return this.mMaxWidth;
        }
        return -1;
    }

    @RemotableViewMethod
    public void setEms(int ems) {
        this.mMinWidth = ems;
        this.mMaxWidth = ems;
        this.mMinWidthMode = 1;
        this.mMaxWidthMode = 1;
        requestLayout();
        invalidate();
    }

    @RemotableViewMethod
    public void setWidth(int pixels) {
        this.mMinWidth = pixels;
        this.mMaxWidth = pixels;
        this.mMinWidthMode = 2;
        this.mMaxWidthMode = 2;
        requestLayout();
        invalidate();
    }

    public void setLineSpacing(float add, float mult) {
        if (this.mSpacingAdd != add || this.mSpacingMult != mult) {
            this.mSpacingAdd = add;
            this.mSpacingMult = mult;
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public float getLineSpacingMultiplier() {
        return this.mSpacingMult;
    }

    public float getLineSpacingExtra() {
        return this.mSpacingAdd;
    }

    public void setLineHeight(int lineHeight) {
        Preconditions.checkArgumentNonnegative(lineHeight);
        int fontHeight = getPaint().getFontMetricsInt(null);
        if (lineHeight != fontHeight) {
            setLineSpacing(lineHeight - fontHeight, 1.0f);
        }
    }

    public final void append(CharSequence text) {
        append(text, 0, text.length());
    }

    public void append(CharSequence text, int start, int end) {
        if (!(this.mText instanceof Editable)) {
            setText(this.mText, BufferType.EDITABLE);
        }
        ((Editable) this.mText).append(text, start, end);
        if (this.mAutoLinkMask != 0) {
            boolean linksWereAdded = Linkify.addLinks(this.mSpannable, this.mAutoLinkMask);
            if (linksWereAdded && this.mLinksClickable && !textCanBeSelected()) {
                setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    private synchronized void updateTextColors() {
        int color;
        int color2;
        boolean inval = false;
        int[] drawableState = getDrawableState();
        int color3 = this.mTextColor.getColorForState(drawableState, 0);
        if (color3 != this.mCurTextColor) {
            this.mCurTextColor = color3;
            inval = true;
        }
        if (this.mLinkTextColor != null && (color2 = this.mLinkTextColor.getColorForState(drawableState, 0)) != this.mTextPaint.linkColor) {
            this.mTextPaint.linkColor = color2;
            inval = true;
        }
        if (this.mHintTextColor != null && (color = this.mHintTextColor.getColorForState(drawableState, 0)) != this.mCurHintTextColor) {
            this.mCurHintTextColor = color;
            if (this.mText.length() == 0) {
                inval = true;
            }
        }
        if (inval) {
            if (this.mEditor != null) {
                this.mEditor.invalidateTextDisplayList();
            }
            invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void drawableStateChanged() {
        Drawable[] drawableArr;
        super.drawableStateChanged();
        if ((this.mTextColor != null && this.mTextColor.isStateful()) || ((this.mHintTextColor != null && this.mHintTextColor.isStateful()) || (this.mLinkTextColor != null && this.mLinkTextColor.isStateful()))) {
            updateTextColors();
        }
        if (this.mDrawables != null) {
            int[] state = getDrawableState();
            for (Drawable dr : this.mDrawables.mShowing) {
                if (dr != null && dr.isStateful() && dr.setState(state)) {
                    invalidateDrawable(dr);
                }
            }
        }
    }

    @Override // android.view.View
    public void drawableHotspotChanged(float x, float y) {
        Drawable[] drawableArr;
        super.drawableHotspotChanged(x, y);
        if (this.mDrawables != null) {
            for (Drawable dr : this.mDrawables.mShowing) {
                if (dr != null) {
                    dr.setHotspot(x, y);
                }
            }
        }
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        boolean freezesText = getFreezesText();
        boolean hasSelection = false;
        int start = -1;
        int end = -1;
        if (this.mText != null) {
            start = getSelectionStart();
            end = getSelectionEnd();
            if (start >= 0 || end >= 0) {
                hasSelection = true;
            }
        }
        if (freezesText || hasSelection) {
            SavedState ss = new SavedState(superState);
            if (freezesText) {
                if (this.mText instanceof Spanned) {
                    Spannable sp = new SpannableStringBuilder(this.mText);
                    if (this.mEditor != null) {
                        removeMisspelledSpans(sp);
                        sp.removeSpan(this.mEditor.mSuggestionRangeSpan);
                    }
                    ss.text = sp;
                } else {
                    ss.text = this.mText.toString();
                }
            }
            if (hasSelection) {
                ss.selStart = start;
                ss.selEnd = end;
            }
            if (isFocused() && start >= 0 && end >= 0) {
                ss.frozenWithFocus = true;
            }
            ss.error = getError();
            if (this.mEditor != null) {
                ss.editorState = this.mEditor.saveInstanceState();
            }
            return ss;
        }
        return superState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void removeMisspelledSpans(Spannable spannable) {
        int flags = 0;
        SuggestionSpan[] suggestionSpans = (SuggestionSpan[]) spannable.getSpans(0, spannable.length(), SuggestionSpan.class);
        while (true) {
            int i = flags;
            if (i < suggestionSpans.length) {
                int flags2 = suggestionSpans[i].getFlags();
                if ((flags2 & 1) != 0 && (flags2 & 2) != 0) {
                    spannable.removeSpan(suggestionSpans[i]);
                }
                flags = i + 1;
            } else {
                return;
            }
        }
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        if (ss.text != null) {
            setText(ss.text);
        }
        if (ss.selStart >= 0 && ss.selEnd >= 0 && this.mSpannable != null) {
            int len = this.mText.length();
            if (ss.selStart > len || ss.selEnd > len) {
                String restored = "";
                if (ss.text != null) {
                    restored = "(restored) ";
                }
                Log.e(LOG_TAG, "Saved cursor position " + ss.selStart + "/" + ss.selEnd + " out of range for " + restored + "text " + ((Object) this.mText));
            } else {
                Selection.setSelection(this.mSpannable, ss.selStart, ss.selEnd);
                if (ss.frozenWithFocus) {
                    createEditorIfNeeded();
                    this.mEditor.mFrozenWithFocus = true;
                }
            }
        }
        if (ss.error != null) {
            final CharSequence error = ss.error;
            post(new Runnable() { // from class: android.widget.TextView.1
                @Override // java.lang.Runnable
                public void run() {
                    if (TextView.this.mEditor == null || !TextView.this.mEditor.mErrorWasChanged) {
                        TextView.this.setError(error);
                    }
                }
            });
        }
        if (ss.editorState != null) {
            createEditorIfNeeded();
            this.mEditor.restoreInstanceState(ss.editorState);
        }
    }

    @RemotableViewMethod
    public void setFreezesText(boolean freezesText) {
        this.mFreezesText = freezesText;
    }

    public boolean getFreezesText() {
        return this.mFreezesText;
    }

    public final void setEditableFactory(Editable.Factory factory) {
        this.mEditableFactory = factory;
        setText(this.mText);
    }

    public final void setSpannableFactory(Spannable.Factory factory) {
        this.mSpannableFactory = factory;
        setText(this.mText);
    }

    @RemotableViewMethod
    public final void setText(CharSequence text) {
        setText(text, this.mBufferType);
    }

    @RemotableViewMethod
    public final void setTextKeepState(CharSequence text) {
        setTextKeepState(text, this.mBufferType);
    }

    public void setText(CharSequence text, BufferType type) {
        setText(text, type, true, 0);
        if (this.mCharWrapper == null) {
            return;
        }
        this.mCharWrapper.mChars = null;
    }

    public protected void setText(CharSequence text, BufferType type, boolean notifyBefore, int oldlen) {
        Spannable s2;
        this.mTextSetFromXmlOrResourceId = false;
        if (text == null) {
            text = "";
        }
        if (!isSuggestionsEnabled()) {
            text = removeSuggestionSpans(text);
        }
        if (!this.mUserSetTextScaleX) {
            this.mTextPaint.setTextScaleX(1.0f);
        }
        if ((text instanceof Spanned) && ((Spanned) text).getSpanStart(TextUtils.TruncateAt.MARQUEE) >= 0) {
            if (ViewConfiguration.get(this.mContext).isFadingMarqueeEnabled()) {
                setHorizontalFadingEdgeEnabled(true);
                this.mMarqueeFadeMode = 0;
            } else {
                setHorizontalFadingEdgeEnabled(false);
                this.mMarqueeFadeMode = 1;
            }
            setEllipsize(TextUtils.TruncateAt.MARQUEE);
        }
        int n = this.mFilters.length;
        CharSequence text2 = text;
        for (int i = 0; i < n; i++) {
            CharSequence out = this.mFilters[i].filter(text2, 0, text2.length(), EMPTY_SPANNED, 0, 0);
            if (out != null) {
                text2 = out;
            }
        }
        if (notifyBefore) {
            if (this.mText == null) {
                sendBeforeTextChanged("", 0, 0, text2.length());
            } else {
                oldlen = this.mText.length();
                sendBeforeTextChanged(this.mText, 0, oldlen, text2.length());
            }
        }
        boolean needEditableForNotification = false;
        if (this.mListeners != null && this.mListeners.size() != 0) {
            needEditableForNotification = true;
        }
        PrecomputedText precomputed = text2 instanceof PrecomputedText ? (PrecomputedText) text2 : null;
        if (type == BufferType.EDITABLE || getKeyListener() != null || needEditableForNotification) {
            createEditorIfNeeded();
            this.mEditor.forgetUndoRedo();
            Editable t = this.mEditableFactory.newEditable(text2);
            text2 = t;
            setFilters(t, this.mFilters);
            InputMethodManager imm = InputMethodManager.peekInstance();
            if (imm != null) {
                imm.restartInput(this);
            }
        } else if (precomputed != null) {
            if (this.mTextDir == null) {
                this.mTextDir = getTextDirectionHeuristic();
            }
            if (!precomputed.getParams().isSameTextMetricsInternal(getPaint(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency)) {
                throw new IllegalArgumentException("PrecomputedText's Parameters don't match the parameters of this TextView.Consider using setTextMetricsParams(precomputedText.getParams()) to override the settings of this TextView: PrecomputedText: " + precomputed.getParams() + "TextView: " + getTextMetricsParams());
            }
        } else if (type == BufferType.SPANNABLE || this.mMovement != null) {
            text2 = this.mSpannableFactory.newSpannable(text2);
        } else if (!(text2 instanceof CharWrapper)) {
            text2 = TextUtils.stringOrSpannedString(text2);
        }
        if (this.mAutoLinkMask != 0) {
            if (type == BufferType.EDITABLE || (text2 instanceof Spannable)) {
                s2 = (Spannable) text2;
            } else {
                s2 = this.mSpannableFactory.newSpannable(text2);
            }
            if (Linkify.addLinks(s2, this.mAutoLinkMask)) {
                text2 = s2;
                type = type == BufferType.EDITABLE ? BufferType.EDITABLE : BufferType.SPANNABLE;
                setTextInternal(text2);
                if (this.mLinksClickable && !textCanBeSelected()) {
                    setMovementMethod(LinkMovementMethod.getInstance());
                }
            }
        }
        this.mBufferType = type;
        setTextInternal(text2);
        if (this.mTransformation == null) {
            this.mTransformed = text2;
        } else {
            this.mTransformed = this.mTransformation.getTransformation(text2, this);
        }
        int textLength = text2.length();
        if ((text2 instanceof Spannable) && !this.mAllowTransformationLengthChange) {
            Spannable sp = (Spannable) text2;
            ChangeWatcher[] watchers = (ChangeWatcher[]) sp.getSpans(0, sp.length(), ChangeWatcher.class);
            for (ChangeWatcher changeWatcher : watchers) {
                sp.removeSpan(changeWatcher);
            }
            if (this.mChangeWatcher == null) {
                this.mChangeWatcher = new ChangeWatcher();
            }
            sp.setSpan(this.mChangeWatcher, 0, textLength, 6553618);
            if (this.mEditor != null) {
                this.mEditor.addSpanWatchers(sp);
            }
            if (this.mTransformation != null) {
                sp.setSpan(this.mTransformation, 0, textLength, 18);
            }
            if (this.mMovement != null) {
                this.mMovement.initialize(this, (Spannable) text2);
                if (this.mEditor != null) {
                    this.mEditor.mSelectionMoved = false;
                }
            }
        }
        if (this.mLayout != null) {
            checkForRelayout();
        }
        sendOnTextChanged(text2, 0, oldlen, textLength);
        onTextChanged(text2, 0, oldlen, textLength);
        notifyViewAccessibilityStateChangedIfNeeded(2);
        if (needEditableForNotification) {
            sendAfterTextChanged((Editable) text2);
        } else {
            notifyAutoFillManagerAfterTextChangedIfNeeded();
        }
        if (this.mEditor != null) {
            this.mEditor.prepareCursorControllers();
        }
    }

    public final void setText(char[] text, int start, int len) {
        int oldlen = 0;
        if (start < 0 || len < 0 || start + len > text.length) {
            throw new IndexOutOfBoundsException(start + ", " + len);
        }
        if (this.mText != null) {
            oldlen = this.mText.length();
            sendBeforeTextChanged(this.mText, 0, oldlen, len);
        } else {
            sendBeforeTextChanged("", 0, 0, len);
        }
        if (this.mCharWrapper == null) {
            this.mCharWrapper = new CharWrapper(text, start, len);
        } else {
            this.mCharWrapper.set(text, start, len);
        }
        setText(this.mCharWrapper, this.mBufferType, false, oldlen);
    }

    public final void setTextKeepState(CharSequence text, BufferType type) {
        int start = getSelectionStart();
        int end = getSelectionEnd();
        int len = text.length();
        setText(text, type);
        if ((start >= 0 || end >= 0) && this.mSpannable != null) {
            Selection.setSelection(this.mSpannable, Math.max(0, Math.min(start, len)), Math.max(0, Math.min(end, len)));
        }
    }

    @RemotableViewMethod
    public final void setText(int resid) {
        setText(getContext().getResources().getText(resid));
        this.mTextSetFromXmlOrResourceId = true;
        this.mTextId = resid;
    }

    public final void setText(int resid, BufferType type) {
        setText(getContext().getResources().getText(resid), type);
        this.mTextSetFromXmlOrResourceId = true;
        this.mTextId = resid;
    }

    @RemotableViewMethod
    public final void setHint(CharSequence hint) {
        setHintInternal(hint);
        if (this.mEditor != null && isInputMethodTarget()) {
            this.mEditor.reportExtractedText();
        }
    }

    private synchronized void setHintInternal(CharSequence hint) {
        this.mHint = TextUtils.stringOrSpannedString(hint);
        if (this.mLayout != null) {
            checkForRelayout();
        }
        if (this.mText.length() == 0) {
            invalidate();
        }
        if (this.mEditor != null && this.mText.length() == 0 && this.mHint != null) {
            this.mEditor.invalidateTextDisplayList();
        }
    }

    @RemotableViewMethod
    public final void setHint(int resid) {
        setHint(getContext().getResources().getText(resid));
    }

    @ViewDebug.CapturedViewProperty
    public CharSequence getHint() {
        return this.mHint;
    }

    public private protected boolean isSingleLine() {
        return this.mSingleLine;
    }

    private static synchronized boolean isMultilineInputType(int type) {
        return (131087 & type) == 131073;
    }

    synchronized CharSequence removeSuggestionSpans(CharSequence text) {
        Spannable spannable;
        if (text instanceof Spanned) {
            if (text instanceof Spannable) {
                spannable = (Spannable) text;
            } else {
                spannable = this.mSpannableFactory.newSpannable(text);
            }
            int i = 0;
            SuggestionSpan[] spans = (SuggestionSpan[]) spannable.getSpans(0, text.length(), SuggestionSpan.class);
            if (spans.length == 0) {
                return text;
            }
            text = spannable;
            while (true) {
                int i2 = i;
                int i3 = spans.length;
                if (i2 >= i3) {
                    break;
                }
                spannable.removeSpan(spans[i2]);
                i = i2 + 1;
            }
        }
        return text;
    }

    public void setInputType(int type) {
        boolean wasPassword = isPasswordInputType(getInputType());
        boolean wasVisiblePassword = isVisiblePasswordInputType(getInputType());
        setInputType(type, false);
        boolean isPassword = isPasswordInputType(type);
        boolean isVisiblePassword = isVisiblePasswordInputType(type);
        boolean forceUpdate = false;
        if (isPassword) {
            setTransformationMethod(PasswordTransformationMethod.getInstance());
            setTypefaceFromAttrs(null, null, 3, 0, -1);
        } else if (isVisiblePassword) {
            if (this.mTransformation == PasswordTransformationMethod.getInstance()) {
                forceUpdate = true;
            }
            setTypefaceFromAttrs(null, null, 3, 0, -1);
        } else if (wasPassword || wasVisiblePassword) {
            setTypefaceFromAttrs(null, null, -1, 0, -1);
            if (this.mTransformation == PasswordTransformationMethod.getInstance()) {
                forceUpdate = true;
            }
        }
        boolean singleLine = !isMultilineInputType(type);
        if (this.mSingleLine != singleLine || forceUpdate) {
            applySingleLine(singleLine, !isPassword, true);
        }
        if (!isSuggestionsEnabled()) {
            setTextInternal(removeSuggestionSpans(this.mText));
        }
        InputMethodManager imm = InputMethodManager.peekInstance();
        if (imm != null) {
            imm.restartInput(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean hasPasswordTransformationMethod() {
        return this.mTransformation instanceof PasswordTransformationMethod;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized boolean isPasswordInputType(int inputType) {
        int variation = inputType & FileObserver.ALL_EVENTS;
        return variation == 129 || variation == 225 || variation == 18;
    }

    private static synchronized boolean isVisiblePasswordInputType(int inputType) {
        int variation = inputType & FileObserver.ALL_EVENTS;
        return variation == 145;
    }

    public void setRawInputType(int type) {
        if (type == 0 && this.mEditor == null) {
            return;
        }
        createEditorIfNeeded();
        this.mEditor.mInputType = type;
    }

    private synchronized Locale getCustomLocaleForKeyListenerOrNull() {
        LocaleList locales;
        if (this.mUseInternationalizedInput && (locales = getImeHintLocales()) != null) {
            return locales.get(0);
        }
        return null;
    }

    public protected void setInputType(int type, boolean direct) {
        KeyListener input;
        KeyListener input2;
        boolean autotext;
        TextKeyListener.Capitalize cap;
        int cls = type & 15;
        if (cls == 1) {
            autotext = (32768 & type) != 0;
            if ((type & 4096) != 0) {
                cap = TextKeyListener.Capitalize.CHARACTERS;
            } else if ((type & 8192) != 0) {
                cap = TextKeyListener.Capitalize.WORDS;
            } else if ((type & 16384) != 0) {
                cap = TextKeyListener.Capitalize.SENTENCES;
            } else {
                cap = TextKeyListener.Capitalize.NONE;
            }
            input = TextKeyListener.getInstance(autotext, cap);
        } else if (cls == 2) {
            Locale locale = getCustomLocaleForKeyListenerOrNull();
            boolean z = (type & 4096) != 0;
            autotext = (type & 8192) != 0;
            input = DigitsKeyListener.getInstance(locale, z, autotext);
            if (locale != null) {
                int newType = input.getInputType();
                int newClass = newType & 15;
                if (newClass != 2) {
                    if ((type & 16) != 0) {
                        newType |= 128;
                    }
                    type = newType;
                }
            }
        } else if (cls == 4) {
            Locale locale2 = getCustomLocaleForKeyListenerOrNull();
            int i = type & InputType.TYPE_MASK_VARIATION;
            if (i == 16) {
                input2 = DateKeyListener.getInstance(locale2);
            } else if (i == 32) {
                input2 = TimeKeyListener.getInstance(locale2);
            } else {
                input2 = DateTimeKeyListener.getInstance(locale2);
            }
            if (this.mUseInternationalizedInput) {
                type = input2.getInputType();
            }
            input = input2;
        } else {
            input = cls == 3 ? DialerKeyListener.getInstance() : TextKeyListener.getInstance();
        }
        setRawInputType(type);
        this.mListenerChanged = false;
        if (direct) {
            createEditorIfNeeded();
            this.mEditor.mKeyListener = input;
            return;
        }
        setKeyListenerOnly(input);
    }

    public int getInputType() {
        if (this.mEditor == null) {
            return 0;
        }
        return this.mEditor.mInputType;
    }

    public void setImeOptions(int imeOptions) {
        createEditorIfNeeded();
        this.mEditor.createInputContentTypeIfNeeded();
        this.mEditor.mInputContentType.imeOptions = imeOptions;
    }

    public int getImeOptions() {
        if (this.mEditor == null || this.mEditor.mInputContentType == null) {
            return 0;
        }
        return this.mEditor.mInputContentType.imeOptions;
    }

    public void setImeActionLabel(CharSequence label, int actionId) {
        createEditorIfNeeded();
        this.mEditor.createInputContentTypeIfNeeded();
        this.mEditor.mInputContentType.imeActionLabel = label;
        this.mEditor.mInputContentType.imeActionId = actionId;
    }

    public CharSequence getImeActionLabel() {
        if (this.mEditor == null || this.mEditor.mInputContentType == null) {
            return null;
        }
        return this.mEditor.mInputContentType.imeActionLabel;
    }

    public int getImeActionId() {
        if (this.mEditor == null || this.mEditor.mInputContentType == null) {
            return 0;
        }
        return this.mEditor.mInputContentType.imeActionId;
    }

    public void setOnEditorActionListener(OnEditorActionListener l) {
        createEditorIfNeeded();
        this.mEditor.createInputContentTypeIfNeeded();
        this.mEditor.mInputContentType.onEditorActionListener = l;
    }

    public void onEditorAction(int actionCode) {
        Editor.InputContentType ict = this.mEditor == null ? null : this.mEditor.mInputContentType;
        if (ict != null) {
            if (ict.onEditorActionListener == null || !ict.onEditorActionListener.onEditorAction(this, actionCode, null)) {
                if (actionCode != 5) {
                    if (actionCode != 7) {
                        if (actionCode == 6) {
                            InputMethodManager imm = InputMethodManager.peekInstance();
                            if (imm != null && imm.isActive(this)) {
                                imm.hideSoftInputFromWindow(getWindowToken(), 0);
                                return;
                            }
                            return;
                        }
                    } else {
                        View v = focusSearch(1);
                        if (v != null && !v.requestFocus(1)) {
                            throw new IllegalStateException("focus search returned a view that wasn't able to take focus!");
                        }
                        return;
                    }
                } else {
                    View v2 = focusSearch(2);
                    if (v2 != null && !v2.requestFocus(2)) {
                        throw new IllegalStateException("focus search returned a view that wasn't able to take focus!");
                    }
                    return;
                }
            } else {
                return;
            }
        }
        ViewRootImpl viewRootImpl = getViewRootImpl();
        if (viewRootImpl != null) {
            long eventTime = SystemClock.uptimeMillis();
            viewRootImpl.dispatchKeyFromIme(new KeyEvent(eventTime, eventTime, 0, 66, 0, 0, -1, 0, 22));
            viewRootImpl.dispatchKeyFromIme(new KeyEvent(SystemClock.uptimeMillis(), eventTime, 1, 66, 0, 0, -1, 0, 22));
        }
    }

    public void setPrivateImeOptions(String type) {
        createEditorIfNeeded();
        this.mEditor.createInputContentTypeIfNeeded();
        this.mEditor.mInputContentType.privateImeOptions = type;
    }

    public String getPrivateImeOptions() {
        if (this.mEditor == null || this.mEditor.mInputContentType == null) {
            return null;
        }
        return this.mEditor.mInputContentType.privateImeOptions;
    }

    public void setInputExtras(int xmlResId) throws XmlPullParserException, IOException {
        createEditorIfNeeded();
        XmlResourceParser parser = getResources().getXml(xmlResId);
        this.mEditor.createInputContentTypeIfNeeded();
        this.mEditor.mInputContentType.extras = new Bundle();
        getResources().parseBundleExtras(parser, this.mEditor.mInputContentType.extras);
    }

    public Bundle getInputExtras(boolean create) {
        if (this.mEditor != null || create) {
            createEditorIfNeeded();
            if (this.mEditor.mInputContentType == null) {
                if (!create) {
                    return null;
                }
                this.mEditor.createInputContentTypeIfNeeded();
            }
            if (this.mEditor.mInputContentType.extras == null) {
                if (!create) {
                    return null;
                }
                this.mEditor.mInputContentType.extras = new Bundle();
            }
            return this.mEditor.mInputContentType.extras;
        }
        return null;
    }

    public void setImeHintLocales(LocaleList hintLocales) {
        createEditorIfNeeded();
        this.mEditor.createInputContentTypeIfNeeded();
        this.mEditor.mInputContentType.imeHintLocales = hintLocales;
        if (this.mUseInternationalizedInput) {
            changeListenerLocaleTo(hintLocales == null ? null : hintLocales.get(0));
        }
    }

    public LocaleList getImeHintLocales() {
        if (this.mEditor == null || this.mEditor.mInputContentType == null) {
            return null;
        }
        return this.mEditor.mInputContentType.imeHintLocales;
    }

    public CharSequence getError() {
        if (this.mEditor == null) {
            return null;
        }
        return this.mEditor.mError;
    }

    @RemotableViewMethod
    public void setError(CharSequence error) {
        if (error == null) {
            setError(null, null);
            return;
        }
        Drawable dr = getContext().getDrawable(com.android.internal.R.drawable.indicator_input_error);
        dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
        setError(error, dr);
    }

    public void setError(CharSequence error, Drawable icon) {
        createEditorIfNeeded();
        this.mEditor.setError(error, icon);
        notifyViewAccessibilityStateChangedIfNeeded(0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean setFrame(int l, int t, int r, int b) {
        boolean result = super.setFrame(l, t, r, b);
        if (this.mEditor != null) {
            this.mEditor.setFrame();
        }
        restartMarqueeIfNeeded();
        return result;
    }

    private synchronized void restartMarqueeIfNeeded() {
        if (this.mRestartMarquee && this.mEllipsize == TextUtils.TruncateAt.MARQUEE) {
            this.mRestartMarquee = false;
            startMarquee();
        }
    }

    public void setFilters(InputFilter[] filters) {
        if (filters == null) {
            throw new IllegalArgumentException();
        }
        this.mFilters = filters;
        if (this.mText instanceof Editable) {
            setFilters((Editable) this.mText, filters);
        }
    }

    private synchronized void setFilters(Editable e, InputFilter[] filters) {
        if (this.mEditor != null) {
            boolean undoFilter = this.mEditor.mUndoInputFilter != null;
            boolean keyFilter = this.mEditor.mKeyListener instanceof InputFilter;
            int num = undoFilter ? 0 + 1 : 0;
            if (keyFilter) {
                num++;
            }
            if (num > 0) {
                InputFilter[] nf = new InputFilter[filters.length + num];
                System.arraycopy(filters, 0, nf, 0, filters.length);
                int num2 = 0;
                if (undoFilter) {
                    nf[filters.length] = this.mEditor.mUndoInputFilter;
                    num2 = 0 + 1;
                }
                if (keyFilter) {
                    nf[filters.length + num2] = (InputFilter) this.mEditor.mKeyListener;
                }
                e.setFilters(nf);
                return;
            }
        }
        e.setFilters(filters);
    }

    public InputFilter[] getFilters() {
        return this.mFilters;
    }

    private synchronized int getBoxHeight(Layout l) {
        int padding;
        Insets opticalInsets = isLayoutModeOptical(this.mParent) ? getOpticalInsets() : Insets.NONE;
        if (l == this.mHintLayout) {
            padding = getCompoundPaddingTop() + getCompoundPaddingBottom();
        } else {
            padding = getExtendedPaddingTop() + getExtendedPaddingBottom();
        }
        return (getMeasuredHeight() - padding) + opticalInsets.top + opticalInsets.bottom;
    }

    public private protected int getVerticalOffset(boolean forceNormal) {
        int boxht;
        int textht;
        int gravity = this.mGravity & 112;
        Layout l = this.mLayout;
        if (!forceNormal && this.mText.length() == 0 && this.mHintLayout != null) {
            l = this.mHintLayout;
        }
        if (gravity == 48 || (textht = l.getHeight()) >= (boxht = getBoxHeight(l))) {
            return 0;
        }
        if (gravity == 80) {
            int voffset = boxht - textht;
            return voffset;
        }
        int voffset2 = (boxht - textht) >> 1;
        return voffset2;
    }

    private synchronized int getBottomVerticalOffset(boolean forceNormal) {
        int boxht;
        int textht;
        int gravity = this.mGravity & 112;
        Layout l = this.mLayout;
        if (!forceNormal && this.mText.length() == 0 && this.mHintLayout != null) {
            l = this.mHintLayout;
        }
        if (gravity == 80 || (textht = l.getHeight()) >= (boxht = getBoxHeight(l))) {
            return 0;
        }
        if (gravity == 48) {
            int voffset = boxht - textht;
            return voffset;
        }
        int voffset2 = (boxht - textht) >> 1;
        return voffset2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void invalidateCursorPath() {
        if (this.mHighlightPathBogus) {
            invalidateCursor();
            return;
        }
        int horizontalPadding = getCompoundPaddingLeft();
        int verticalPadding = getExtendedPaddingTop() + getVerticalOffset(true);
        if (this.mEditor.mDrawableForCursor == null) {
            synchronized (TEMP_RECTF) {
                float thick = (float) Math.ceil(this.mTextPaint.getStrokeWidth());
                if (thick < 1.0f) {
                    thick = 1.0f;
                }
                float thick2 = thick / 2.0f;
                this.mHighlightPath.computeBounds(TEMP_RECTF, false);
                invalidate((int) Math.floor((horizontalPadding + TEMP_RECTF.left) - thick2), (int) Math.floor((verticalPadding + TEMP_RECTF.top) - thick2), (int) Math.ceil(horizontalPadding + TEMP_RECTF.right + thick2), (int) Math.ceil(verticalPadding + TEMP_RECTF.bottom + thick2));
            }
            return;
        }
        Rect bounds = this.mEditor.mDrawableForCursor.getBounds();
        invalidate(bounds.left + horizontalPadding, bounds.top + verticalPadding, bounds.right + horizontalPadding, bounds.bottom + verticalPadding);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void invalidateCursor() {
        int where = getSelectionEnd();
        invalidateCursor(where, where, where);
    }

    private synchronized void invalidateCursor(int a, int b, int c) {
        if (a >= 0 || b >= 0 || c >= 0) {
            int start = Math.min(Math.min(a, b), c);
            int end = Math.max(Math.max(a, b), c);
            invalidateRegion(start, end, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void invalidateRegion(int start, int end, boolean invalidateCursor) {
        int lineEnd;
        int left;
        int right;
        if (this.mLayout == null) {
            invalidate();
            return;
        }
        int lineStart = this.mLayout.getLineForOffset(start);
        int top = this.mLayout.getLineTop(lineStart);
        if (lineStart > 0) {
            top -= this.mLayout.getLineDescent(lineStart - 1);
        }
        if (start == end) {
            lineEnd = lineStart;
        } else {
            lineEnd = this.mLayout.getLineForOffset(end);
        }
        int bottom = this.mLayout.getLineBottom(lineEnd);
        if (invalidateCursor && this.mEditor != null && this.mEditor.mDrawableForCursor != null) {
            Rect bounds = this.mEditor.mDrawableForCursor.getBounds();
            top = Math.min(top, bounds.top);
            bottom = Math.max(bottom, bounds.bottom);
        }
        int compoundPaddingLeft = getCompoundPaddingLeft();
        int verticalPadding = getExtendedPaddingTop() + getVerticalOffset(true);
        if (lineStart == lineEnd && !invalidateCursor) {
            int left2 = (int) this.mLayout.getPrimaryHorizontal(start);
            int right2 = (int) (this.mLayout.getPrimaryHorizontal(end) + 1.0d);
            left = left2 + compoundPaddingLeft;
            right = right2 + compoundPaddingLeft;
        } else {
            left = compoundPaddingLeft;
            right = getWidth() - getCompoundPaddingRight();
        }
        invalidate(this.mScrollX + left, verticalPadding + top, this.mScrollX + right, verticalPadding + bottom);
    }

    private synchronized void registerForPreDraw() {
        if (!this.mPreDrawRegistered) {
            getViewTreeObserver().addOnPreDrawListener(this);
            this.mPreDrawRegistered = true;
        }
    }

    private synchronized void unregisterForPreDraw() {
        getViewTreeObserver().removeOnPreDrawListener(this);
        this.mPreDrawRegistered = false;
        this.mPreDrawListenerDetached = false;
    }

    @Override // android.view.ViewTreeObserver.OnPreDrawListener
    public boolean onPreDraw() {
        if (this.mLayout == null) {
            assumeLayout();
        }
        if (this.mMovement != null) {
            int curs = getSelectionEnd();
            if (this.mEditor != null && this.mEditor.mSelectionModifierCursorController != null && this.mEditor.mSelectionModifierCursorController.isSelectionStartDragged()) {
                curs = getSelectionStart();
            }
            if (curs < 0 && (this.mGravity & 112) == 80) {
                curs = this.mText.length();
            }
            if (curs >= 0) {
                bringPointIntoView(curs);
            }
        } else {
            bringTextIntoView();
        }
        if (this.mEditor != null && this.mEditor.mCreatedWithASelection) {
            this.mEditor.refreshTextActionMode();
            this.mEditor.mCreatedWithASelection = false;
        }
        unregisterForPreDraw();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mEditor != null) {
            this.mEditor.onAttachedToWindow();
        }
        if (this.mPreDrawListenerDetached) {
            getViewTreeObserver().addOnPreDrawListener(this);
            this.mPreDrawListenerDetached = false;
        }
    }

    protected synchronized void onDetachedFromWindowInternal() {
        if (this.mPreDrawRegistered) {
            getViewTreeObserver().removeOnPreDrawListener(this);
            this.mPreDrawListenerDetached = true;
        }
        resetResolvedDrawables();
        if (this.mEditor != null) {
            this.mEditor.onDetachedFromWindow();
        }
        super.onDetachedFromWindowInternal();
    }

    @Override // android.view.View
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
        if (this.mEditor != null) {
            this.mEditor.onScreenStateChanged(screenState);
        }
    }

    @Override // android.view.View
    protected boolean isPaddingOffsetRequired() {
        return (this.mShadowRadius == 0.0f && this.mDrawables == null) ? false : true;
    }

    @Override // android.view.View
    protected int getLeftPaddingOffset() {
        return (getCompoundPaddingLeft() - this.mPaddingLeft) + ((int) Math.min(0.0f, this.mShadowDx - this.mShadowRadius));
    }

    @Override // android.view.View
    protected int getTopPaddingOffset() {
        return (int) Math.min(0.0f, this.mShadowDy - this.mShadowRadius);
    }

    @Override // android.view.View
    protected int getBottomPaddingOffset() {
        return (int) Math.max(0.0f, this.mShadowDy + this.mShadowRadius);
    }

    @Override // android.view.View
    protected int getRightPaddingOffset() {
        return (-(getCompoundPaddingRight() - this.mPaddingRight)) + ((int) Math.max(0.0f, this.mShadowDx + this.mShadowRadius));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public boolean verifyDrawable(Drawable who) {
        Drawable[] drawableArr;
        boolean verified = super.verifyDrawable(who);
        if (!verified && this.mDrawables != null) {
            for (Drawable dr : this.mDrawables.mShowing) {
                if (who == dr) {
                    return true;
                }
            }
        }
        return verified;
    }

    @Override // android.view.View
    public void jumpDrawablesToCurrentState() {
        Drawable[] drawableArr;
        super.jumpDrawablesToCurrentState();
        if (this.mDrawables != null) {
            for (Drawable dr : this.mDrawables.mShowing) {
                if (dr != null) {
                    dr.jumpToCurrentState();
                }
            }
        }
    }

    @Override // android.view.View, android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        boolean handled = false;
        if (verifyDrawable(drawable)) {
            Rect dirty = drawable.getBounds();
            int scrollX = this.mScrollX;
            int scrollY = this.mScrollY;
            Drawables drawables = this.mDrawables;
            if (drawables != null) {
                if (drawable == drawables.mShowing[0]) {
                    int compoundPaddingTop = getCompoundPaddingTop();
                    int compoundPaddingBottom = getCompoundPaddingBottom();
                    int vspace = ((this.mBottom - this.mTop) - compoundPaddingBottom) - compoundPaddingTop;
                    scrollX += this.mPaddingLeft;
                    scrollY += ((vspace - drawables.mDrawableHeightLeft) / 2) + compoundPaddingTop;
                    handled = true;
                } else if (drawable == drawables.mShowing[2]) {
                    int compoundPaddingTop2 = getCompoundPaddingTop();
                    int compoundPaddingBottom2 = getCompoundPaddingBottom();
                    int vspace2 = ((this.mBottom - this.mTop) - compoundPaddingBottom2) - compoundPaddingTop2;
                    scrollX += ((this.mRight - this.mLeft) - this.mPaddingRight) - drawables.mDrawableSizeRight;
                    scrollY += ((vspace2 - drawables.mDrawableHeightRight) / 2) + compoundPaddingTop2;
                    handled = true;
                } else if (drawable == drawables.mShowing[1]) {
                    int compoundPaddingLeft = getCompoundPaddingLeft();
                    int compoundPaddingRight = getCompoundPaddingRight();
                    int hspace = ((this.mRight - this.mLeft) - compoundPaddingRight) - compoundPaddingLeft;
                    scrollX += ((hspace - drawables.mDrawableWidthTop) / 2) + compoundPaddingLeft;
                    scrollY += this.mPaddingTop;
                    handled = true;
                } else if (drawable == drawables.mShowing[3]) {
                    int compoundPaddingLeft2 = getCompoundPaddingLeft();
                    int compoundPaddingRight2 = getCompoundPaddingRight();
                    int hspace2 = ((this.mRight - this.mLeft) - compoundPaddingRight2) - compoundPaddingLeft2;
                    scrollX += ((hspace2 - drawables.mDrawableWidthBottom) / 2) + compoundPaddingLeft2;
                    scrollY += ((this.mBottom - this.mTop) - this.mPaddingBottom) - drawables.mDrawableSizeBottom;
                    handled = true;
                }
            }
            if (handled) {
                invalidate(dirty.left + scrollX, dirty.top + scrollY, dirty.right + scrollX, dirty.bottom + scrollY);
            }
        }
        if (!handled) {
            super.invalidateDrawable(drawable);
        }
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return !(getBackground() == null || getBackground().getCurrent() == null) || this.mSpannable != null || hasSelection() || isHorizontalFadingEdgeEnabled();
    }

    public boolean isTextSelectable() {
        if (this.mEditor == null) {
            return false;
        }
        return this.mEditor.mTextIsSelectable;
    }

    public void setTextIsSelectable(boolean selectable) {
        if (selectable || this.mEditor != null) {
            createEditorIfNeeded();
            if (this.mEditor.mTextIsSelectable == selectable) {
                return;
            }
            this.mEditor.mTextIsSelectable = selectable;
            setFocusableInTouchMode(selectable);
            setFocusable(16);
            setClickable(selectable);
            setLongClickable(selectable);
            setMovementMethod(selectable ? ArrowKeyMovementMethod.getInstance() : null);
            setText(this.mText, selectable ? BufferType.SPANNABLE : BufferType.NORMAL);
            this.mEditor.prepareCursorControllers();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState;
        if (this.mSingleLine) {
            drawableState = super.onCreateDrawableState(extraSpace);
        } else {
            drawableState = super.onCreateDrawableState(extraSpace + 1);
            mergeDrawableStates(drawableState, MULTILINE_STATE_SET);
        }
        if (isTextSelectable()) {
            int length = drawableState.length;
            for (int i = 0; i < length; i++) {
                if (drawableState[i] == 16842919) {
                    int[] nonPressedState = new int[length - 1];
                    System.arraycopy(drawableState, 0, nonPressedState, 0, i);
                    System.arraycopy(drawableState, i + 1, nonPressedState, i, (length - i) - 1);
                    return nonPressedState;
                }
            }
        }
        return drawableState;
    }

    public protected Path getUpdatedHighlightPath() {
        Paint highlightPaint = this.mHighlightPaint;
        int selStart = getSelectionStart();
        int selEnd = getSelectionEnd();
        if (this.mMovement != null) {
            if ((isFocused() || isPressed()) && selStart >= 0) {
                if (selStart == selEnd) {
                    if (this.mEditor == null || !this.mEditor.shouldRenderCursor()) {
                        return null;
                    }
                    if (this.mHighlightPathBogus) {
                        if (this.mHighlightPath == null) {
                            this.mHighlightPath = new Path();
                        }
                        this.mHighlightPath.reset();
                        this.mLayout.getCursorPath(selStart, this.mHighlightPath, this.mText);
                        this.mEditor.updateCursorPosition();
                        this.mHighlightPathBogus = false;
                    }
                    highlightPaint.setColor(this.mCurTextColor);
                    highlightPaint.setStyle(Paint.Style.STROKE);
                    Path highlight = this.mHighlightPath;
                    return highlight;
                }
                if (this.mHighlightPathBogus) {
                    if (this.mHighlightPath == null) {
                        this.mHighlightPath = new Path();
                    }
                    this.mHighlightPath.reset();
                    this.mLayout.getSelectionPath(selStart, selEnd, this.mHighlightPath);
                    this.mHighlightPathBogus = false;
                }
                highlightPaint.setColor(this.mHighlightColor);
                highlightPaint.setStyle(Paint.Style.FILL);
                Path highlight2 = this.mHighlightPath;
                return highlight2;
            }
            return null;
        }
        return null;
    }

    public synchronized int getHorizontalOffsetForDrawables() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        float clipBottom;
        float clipRight;
        float clipTop;
        float clipTop2;
        int voffsetCursor;
        int layoutDirection;
        float clipBottom2;
        int layoutDirection2;
        int compoundPaddingLeft;
        Layout layout;
        int cursorOffsetVertical;
        restartMarqueeIfNeeded();
        super.onDraw(canvas);
        int compoundPaddingLeft2 = getCompoundPaddingLeft();
        int compoundPaddingTop = getCompoundPaddingTop();
        int compoundPaddingRight = getCompoundPaddingRight();
        int compoundPaddingBottom = getCompoundPaddingBottom();
        int scrollX = this.mScrollX;
        int scrollY = this.mScrollY;
        int right = this.mRight;
        int left = this.mLeft;
        int bottom = this.mBottom;
        int top = this.mTop;
        boolean isLayoutRtl = isLayoutRtl();
        int offset = getHorizontalOffsetForDrawables();
        int leftOffset = isLayoutRtl ? 0 : offset;
        int rightOffset = isLayoutRtl ? offset : 0;
        Drawables dr = this.mDrawables;
        if (dr != null) {
            int vspace = ((bottom - top) - compoundPaddingBottom) - compoundPaddingTop;
            int hspace = ((right - left) - compoundPaddingRight) - compoundPaddingLeft2;
            if (dr.mShowing[0] != null) {
                canvas.save();
                canvas.translate(this.mPaddingLeft + scrollX + leftOffset, scrollY + compoundPaddingTop + ((vspace - dr.mDrawableHeightLeft) / 2));
                dr.mShowing[0].draw(canvas);
                canvas.restore();
            }
            if (dr.mShowing[2] != null) {
                canvas.save();
                canvas.translate(((((scrollX + right) - left) - this.mPaddingRight) - dr.mDrawableSizeRight) - rightOffset, scrollY + compoundPaddingTop + ((vspace - dr.mDrawableHeightRight) / 2));
                dr.mShowing[2].draw(canvas);
                canvas.restore();
            }
            if (dr.mShowing[1] != null) {
                canvas.save();
                canvas.translate(scrollX + compoundPaddingLeft2 + ((hspace - dr.mDrawableWidthTop) / 2), this.mPaddingTop + scrollY);
                dr.mShowing[1].draw(canvas);
                canvas.restore();
            }
            if (dr.mShowing[3] != null) {
                canvas.save();
                canvas.translate(scrollX + compoundPaddingLeft2 + ((hspace - dr.mDrawableWidthBottom) / 2), (((scrollY + bottom) - top) - this.mPaddingBottom) - dr.mDrawableSizeBottom);
                dr.mShowing[3].draw(canvas);
                canvas.restore();
            }
        }
        int color = this.mCurTextColor;
        if (this.mLayout == null) {
            assumeLayout();
        }
        Layout layout2 = this.mLayout;
        if (this.mHint != null && this.mText.length() == 0) {
            if (this.mHintTextColor != null) {
                color = this.mCurHintTextColor;
            }
            layout2 = this.mHintLayout;
        }
        this.mTextPaint.setColor(color);
        this.mTextPaint.drawableState = getDrawableState();
        canvas.save();
        int extendedPaddingTop = getExtendedPaddingTop();
        int extendedPaddingBottom = getExtendedPaddingBottom();
        int maxScrollY = this.mLayout.getHeight() - (((this.mBottom - this.mTop) - compoundPaddingBottom) - compoundPaddingTop);
        float clipLeft = compoundPaddingLeft2 + scrollX;
        float clipTop3 = scrollY == 0 ? 0.0f : extendedPaddingTop + scrollY;
        float clipRight2 = ((right - left) - getCompoundPaddingRight()) + scrollX;
        int maxScrollY2 = ((bottom - top) + scrollY) - (scrollY == maxScrollY ? 0 : extendedPaddingBottom);
        float clipBottom3 = maxScrollY2;
        if (this.mShadowRadius != 0.0f) {
            float clipLeft2 = clipLeft + Math.min(0.0f, this.mShadowDx - this.mShadowRadius);
            float clipRight3 = clipRight2 + Math.max(0.0f, this.mShadowDx + this.mShadowRadius);
            float clipTop4 = clipTop3 + Math.min(0.0f, this.mShadowDy - this.mShadowRadius);
            clipBottom = clipBottom3 + Math.max(0.0f, this.mShadowDy + this.mShadowRadius);
            clipRight = clipRight3;
            clipTop = clipTop4;
            clipTop2 = clipLeft2;
        } else {
            clipBottom = clipBottom3;
            clipRight = clipRight2;
            clipTop = clipTop3;
            clipTop2 = clipLeft;
        }
        canvas.clipRect(clipTop2, clipTop, clipRight, clipBottom);
        int voffsetText = 0;
        if ((this.mGravity & 112) == 48) {
            voffsetCursor = 0;
        } else {
            int voffsetText2 = getVerticalOffset(false);
            int voffsetCursor2 = getVerticalOffset(true);
            voffsetCursor = voffsetCursor2;
            voffsetText = voffsetText2;
        }
        canvas.translate(compoundPaddingLeft2, extendedPaddingTop + voffsetText);
        int layoutDirection3 = getLayoutDirection();
        int absoluteGravity = Gravity.getAbsoluteGravity(this.mGravity, layoutDirection3);
        if (isMarqueeFadeEnabled()) {
            if (!this.mSingleLine && getLineCount() == 1 && canMarquee() && (absoluteGravity & 7) != 3) {
                int width = this.mRight - this.mLeft;
                int padding = getCompoundPaddingLeft() + getCompoundPaddingRight();
                layoutDirection = layoutDirection3;
                clipBottom2 = clipBottom;
                float dx = this.mLayout.getLineRight(0) - (width - padding);
                int width2 = layout2.getParagraphDirection(0);
                canvas.translate(width2 * dx, 0.0f);
            } else {
                layoutDirection = layoutDirection3;
                clipBottom2 = clipBottom;
            }
            if (this.mMarquee != null && this.mMarquee.isRunning()) {
                float dx2 = -this.mMarquee.getScroll();
                layoutDirection2 = 0;
                canvas.translate(layout2.getParagraphDirection(0) * dx2, 0.0f);
            } else {
                layoutDirection2 = 0;
            }
        } else {
            layoutDirection = layoutDirection3;
            clipBottom2 = clipBottom;
            layoutDirection2 = 0;
        }
        int cursorOffsetVertical2 = voffsetCursor - voffsetText;
        Path highlight = getUpdatedHighlightPath();
        if (this.mEditor != null) {
            Layout layout3 = layout2;
            compoundPaddingLeft = layoutDirection2;
            this.mEditor.onDraw(canvas, layout3, highlight, this.mHighlightPaint, cursorOffsetVertical2);
            layout = layout3;
            highlight = highlight;
            cursorOffsetVertical = cursorOffsetVertical2;
        } else {
            compoundPaddingLeft = layoutDirection2;
            layout = layout2;
            cursorOffsetVertical = cursorOffsetVertical2;
            layout.draw(canvas, highlight, this.mHighlightPaint, cursorOffsetVertical);
        }
        if (this.mMarquee != null && this.mMarquee.shouldDrawGhost()) {
            float dx3 = this.mMarquee.getGhostOffset();
            canvas.translate(layout.getParagraphDirection(compoundPaddingLeft) * dx3, 0.0f);
            layout.draw(canvas, highlight, this.mHighlightPaint, cursorOffsetVertical);
        }
        canvas.restore();
    }

    @Override // android.view.View
    public void getFocusedRect(Rect r) {
        if (this.mLayout == null) {
            super.getFocusedRect(r);
            return;
        }
        int selEnd = getSelectionEnd();
        if (selEnd < 0) {
            super.getFocusedRect(r);
            return;
        }
        int selStart = getSelectionStart();
        if (selStart < 0 || selStart >= selEnd) {
            int line = this.mLayout.getLineForOffset(selEnd);
            r.top = this.mLayout.getLineTop(line);
            r.bottom = this.mLayout.getLineBottom(line);
            r.left = ((int) this.mLayout.getPrimaryHorizontal(selEnd)) - 2;
            r.right = r.left + 4;
        } else {
            int lineStart = this.mLayout.getLineForOffset(selStart);
            int lineEnd = this.mLayout.getLineForOffset(selEnd);
            r.top = this.mLayout.getLineTop(lineStart);
            r.bottom = this.mLayout.getLineBottom(lineEnd);
            if (lineStart == lineEnd) {
                r.left = (int) this.mLayout.getPrimaryHorizontal(selStart);
                r.right = (int) this.mLayout.getPrimaryHorizontal(selEnd);
            } else {
                if (this.mHighlightPathBogus) {
                    if (this.mHighlightPath == null) {
                        this.mHighlightPath = new Path();
                    }
                    this.mHighlightPath.reset();
                    this.mLayout.getSelectionPath(selStart, selEnd, this.mHighlightPath);
                    this.mHighlightPathBogus = false;
                }
                synchronized (TEMP_RECTF) {
                    this.mHighlightPath.computeBounds(TEMP_RECTF, true);
                    r.left = ((int) TEMP_RECTF.left) - 1;
                    r.right = ((int) TEMP_RECTF.right) + 1;
                }
            }
        }
        int paddingLeft = getCompoundPaddingLeft();
        int paddingTop = getExtendedPaddingTop();
        if ((this.mGravity & 112) != 48) {
            paddingTop += getVerticalOffset(false);
        }
        r.offset(paddingLeft, paddingTop);
        int paddingBottom = getExtendedPaddingBottom();
        r.bottom += paddingBottom;
    }

    public int getLineCount() {
        if (this.mLayout != null) {
            return this.mLayout.getLineCount();
        }
        return 0;
    }

    public int getLineBounds(int line, Rect bounds) {
        if (this.mLayout == null) {
            if (bounds != null) {
                bounds.set(0, 0, 0, 0);
            }
            return 0;
        }
        int baseline = this.mLayout.getLineBounds(line, bounds);
        int voffset = getExtendedPaddingTop();
        if ((this.mGravity & 112) != 48) {
            voffset += getVerticalOffset(true);
        }
        if (bounds != null) {
            bounds.offset(getCompoundPaddingLeft(), voffset);
        }
        return baseline + voffset;
    }

    @Override // android.view.View
    public int getBaseline() {
        if (this.mLayout == null) {
            return super.getBaseline();
        }
        return getBaselineOffset() + this.mLayout.getLineBaseline(0);
    }

    synchronized int getBaselineOffset() {
        int voffset = 0;
        if ((this.mGravity & 112) != 48) {
            voffset = getVerticalOffset(true);
        }
        if (isLayoutModeOptical(this.mParent)) {
            voffset -= getOpticalInsets().top;
        }
        return getExtendedPaddingTop() + voffset;
    }

    @Override // android.view.View
    protected synchronized int getFadeTop(boolean offsetRequired) {
        if (this.mLayout == null) {
            return 0;
        }
        int voffset = 0;
        if ((this.mGravity & 112) != 48) {
            voffset = getVerticalOffset(true);
        }
        if (offsetRequired) {
            voffset += getTopPaddingOffset();
        }
        return getExtendedPaddingTop() + voffset;
    }

    @Override // android.view.View
    protected synchronized int getFadeHeight(boolean offsetRequired) {
        if (this.mLayout != null) {
            return this.mLayout.getHeight();
        }
        return 0;
    }

    @Override // android.view.View
    public PointerIcon onResolvePointerIcon(MotionEvent event, int pointerIndex) {
        if (this.mSpannable != null && this.mLinksClickable) {
            float x = event.getX(pointerIndex);
            float y = event.getY(pointerIndex);
            int offset = getOffsetForPosition(x, y);
            ClickableSpan[] clickables = (ClickableSpan[]) this.mSpannable.getSpans(offset, offset, ClickableSpan.class);
            if (clickables.length > 0) {
                return PointerIcon.getSystemIcon(this.mContext, 1002);
            }
        }
        if (isTextSelectable() || isTextEditable()) {
            return PointerIcon.getSystemIcon(this.mContext, 1008);
        }
        return super.onResolvePointerIcon(event, pointerIndex);
    }

    @Override // android.view.View
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == 4 && handleBackInTextActionModeIfNeeded(event)) {
            return true;
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public synchronized boolean handleBackInTextActionModeIfNeeded(KeyEvent event) {
        if (this.mEditor == null || this.mEditor.getTextActionMode() == null) {
            return false;
        }
        if (event.getAction() == 0 && event.getRepeatCount() == 0) {
            KeyEvent.DispatcherState state = getKeyDispatcherState();
            if (state != null) {
                state.startTracking(event, this);
            }
            return true;
        }
        if (event.getAction() == 1) {
            KeyEvent.DispatcherState state2 = getKeyDispatcherState();
            if (state2 != null) {
                state2.handleUpEvent(event);
            }
            if (event.isTracking() && !event.isCanceled()) {
                stopTextActionMode();
                return true;
            }
        }
        return false;
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int which = doKeyDown(keyCode, event, null);
        if (which == 0) {
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        KeyEvent down = KeyEvent.changeAction(event, 0);
        int which = doKeyDown(keyCode, down, event);
        if (which == 0) {
            return super.onKeyMultiple(keyCode, repeatCount, event);
        }
        if (which == -1) {
            return true;
        }
        int repeatCount2 = repeatCount - 1;
        KeyEvent up = KeyEvent.changeAction(event, 1);
        if (which == 1) {
            this.mEditor.mKeyListener.onKeyUp(this, (Editable) this.mText, keyCode, up);
            while (true) {
                repeatCount2--;
                if (repeatCount2 <= 0) {
                    break;
                }
                this.mEditor.mKeyListener.onKeyDown(this, (Editable) this.mText, keyCode, down);
                this.mEditor.mKeyListener.onKeyUp(this, (Editable) this.mText, keyCode, up);
            }
            hideErrorIfUnchanged();
        } else if (which == 2) {
            this.mMovement.onKeyUp(this, this.mSpannable, keyCode, up);
            while (true) {
                repeatCount2--;
                if (repeatCount2 <= 0) {
                    break;
                }
                this.mMovement.onKeyDown(this, this.mSpannable, keyCode, down);
                this.mMovement.onKeyUp(this, this.mSpannable, keyCode, up);
            }
        }
        return true;
    }

    private synchronized boolean shouldAdvanceFocusOnEnter() {
        int variation;
        if (getKeyListener() == null) {
            return false;
        }
        if (this.mSingleLine) {
            return true;
        }
        return this.mEditor != null && (this.mEditor.mInputType & 15) == 1 && ((variation = this.mEditor.mInputType & InputType.TYPE_MASK_VARIATION) == 32 || variation == 48);
    }

    private synchronized boolean shouldAdvanceFocusOnTab() {
        int variation;
        return getKeyListener() == null || this.mSingleLine || this.mEditor == null || (this.mEditor.mInputType & 15) != 1 || !((variation = this.mEditor.mInputType & InputType.TYPE_MASK_VARIATION) == 262144 || variation == 131072);
    }

    private synchronized boolean isDirectionalNavigationKey(int keyCode) {
        switch (keyCode) {
            case 19:
            case 20:
            case 21:
            case 22:
                return true;
            default:
                return false;
        }
    }

    private synchronized int doKeyDown(int keyCode, KeyEvent event, KeyEvent otherEvent) {
        if (isEnabled()) {
            if (event.getRepeatCount() == 0 && !KeyEvent.isModifierKey(keyCode)) {
                this.mPreventDefaultMovement = false;
            }
            if (keyCode != 4) {
                if (keyCode != 23) {
                    if (keyCode != 61) {
                        if (keyCode == 66) {
                            if (event.hasNoModifiers()) {
                                if (this.mEditor != null && this.mEditor.mInputContentType != null && this.mEditor.mInputContentType.onEditorActionListener != null && this.mEditor.mInputContentType.onEditorActionListener.onEditorAction(this, 0, event)) {
                                    this.mEditor.mInputContentType.enterDown = true;
                                    return -1;
                                } else if ((event.getFlags() & 16) != 0 || shouldAdvanceFocusOnEnter()) {
                                    return hasOnClickListeners() ? 0 : -1;
                                }
                            }
                        } else {
                            switch (keyCode) {
                                case 277:
                                    if (event.hasNoModifiers() && canCut() && onTextContextMenuItem(16908320)) {
                                        return -1;
                                    }
                                    break;
                                case 278:
                                    if (event.hasNoModifiers() && canCopy() && onTextContextMenuItem(16908321)) {
                                        return -1;
                                    }
                                    break;
                                case 279:
                                    if (event.hasNoModifiers() && canPaste() && onTextContextMenuItem(16908322)) {
                                        return -1;
                                    }
                                    break;
                            }
                        }
                    } else if ((event.hasNoModifiers() || event.hasModifiers(1)) && shouldAdvanceFocusOnTab()) {
                        return 0;
                    }
                } else if (event.hasNoModifiers() && shouldAdvanceFocusOnEnter()) {
                    return 0;
                }
            } else if (this.mEditor != null && this.mEditor.getTextActionMode() != null) {
                stopTextActionMode();
                return -1;
            }
            if (this.mEditor != null && this.mEditor.mKeyListener != null) {
                boolean doDown = true;
                if (otherEvent != null) {
                    try {
                        beginBatchEdit();
                        boolean handled = this.mEditor.mKeyListener.onKeyOther(this, (Editable) this.mText, otherEvent);
                        hideErrorIfUnchanged();
                        doDown = false;
                        if (handled) {
                            endBatchEdit();
                            return -1;
                        }
                    } catch (AbstractMethodError e) {
                    } catch (Throwable th) {
                        endBatchEdit();
                        throw th;
                    }
                    endBatchEdit();
                }
                if (doDown) {
                    beginBatchEdit();
                    boolean handled2 = this.mEditor.mKeyListener.onKeyDown(this, (Editable) this.mText, keyCode, event);
                    endBatchEdit();
                    hideErrorIfUnchanged();
                    if (handled2) {
                        return 1;
                    }
                }
            }
            if (this.mMovement != null && this.mLayout != null) {
                boolean doDown2 = true;
                if (otherEvent != null) {
                    try {
                        boolean handled3 = this.mMovement.onKeyOther(this, this.mSpannable, otherEvent);
                        doDown2 = false;
                        if (handled3) {
                            return -1;
                        }
                    } catch (AbstractMethodError e2) {
                    }
                }
                if (doDown2 && this.mMovement.onKeyDown(this, this.mSpannable, keyCode, event)) {
                    if (event.getRepeatCount() == 0 && !KeyEvent.isModifierKey(keyCode)) {
                        this.mPreventDefaultMovement = true;
                        return 2;
                    }
                    return 2;
                } else if (event.getSource() == 257 && isDirectionalNavigationKey(keyCode)) {
                    return -1;
                }
            }
            return (!this.mPreventDefaultMovement || KeyEvent.isModifierKey(keyCode)) ? 0 : -1;
        }
        return 0;
    }

    public synchronized void resetErrorChangedFlag() {
        if (this.mEditor != null) {
            this.mEditor.mErrorWasChanged = false;
        }
    }

    public synchronized void hideErrorIfUnchanged() {
        if (this.mEditor != null && this.mEditor.mError != null && !this.mEditor.mErrorWasChanged) {
            setError(null, null);
        }
    }

    @Override // android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        InputMethodManager imm;
        if (!isEnabled()) {
            return super.onKeyUp(keyCode, event);
        }
        if (!KeyEvent.isModifierKey(keyCode)) {
            this.mPreventDefaultMovement = false;
        }
        if (keyCode == 23) {
            if (event.hasNoModifiers() && !hasOnClickListeners() && this.mMovement != null && (this.mText instanceof Editable) && this.mLayout != null && onCheckIsTextEditor()) {
                InputMethodManager imm2 = InputMethodManager.peekInstance();
                viewClicked(imm2);
                if (imm2 != null && getShowSoftInputOnFocus()) {
                    imm2.showSoftInput(this, 0);
                }
            }
            return super.onKeyUp(keyCode, event);
        } else if (keyCode == 66 && event.hasNoModifiers()) {
            if (this.mEditor != null && this.mEditor.mInputContentType != null && this.mEditor.mInputContentType.onEditorActionListener != null && this.mEditor.mInputContentType.enterDown) {
                this.mEditor.mInputContentType.enterDown = false;
                if (this.mEditor.mInputContentType.onEditorActionListener.onEditorAction(this, 0, event)) {
                    return true;
                }
            }
            if (((event.getFlags() & 16) != 0 || shouldAdvanceFocusOnEnter()) && !hasOnClickListeners()) {
                View v = focusSearch(130);
                if (v != null) {
                    if (!v.requestFocus(130)) {
                        throw new IllegalStateException("focus search returned a view that wasn't able to take focus!");
                    }
                    super.onKeyUp(keyCode, event);
                    return true;
                } else if ((event.getFlags() & 16) != 0 && (imm = InputMethodManager.peekInstance()) != null && imm.isActive(this)) {
                    imm.hideSoftInputFromWindow(getWindowToken(), 0);
                }
            }
            return super.onKeyUp(keyCode, event);
        } else if (this.mEditor == null || this.mEditor.mKeyListener == null || !this.mEditor.mKeyListener.onKeyUp(this, (Editable) this.mText, keyCode, event)) {
            if (this.mMovement == null || this.mLayout == null || !this.mMovement.onKeyUp(this, this.mSpannable, keyCode, event)) {
                return super.onKeyUp(keyCode, event);
            }
            return true;
        } else {
            return true;
        }
    }

    @Override // android.view.View
    public boolean onCheckIsTextEditor() {
        return (this.mEditor == null || this.mEditor.mInputType == 0) ? false : true;
    }

    @Override // android.view.View
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        if (onCheckIsTextEditor() && isEnabled()) {
            this.mEditor.createInputMethodStateIfNeeded();
            outAttrs.inputType = getInputType();
            if (this.mEditor.mInputContentType != null) {
                outAttrs.imeOptions = this.mEditor.mInputContentType.imeOptions;
                outAttrs.privateImeOptions = this.mEditor.mInputContentType.privateImeOptions;
                outAttrs.actionLabel = this.mEditor.mInputContentType.imeActionLabel;
                outAttrs.actionId = this.mEditor.mInputContentType.imeActionId;
                outAttrs.extras = this.mEditor.mInputContentType.extras;
                outAttrs.hintLocales = this.mEditor.mInputContentType.imeHintLocales;
            } else {
                outAttrs.imeOptions = 0;
                outAttrs.hintLocales = null;
            }
            if (focusSearch(130) != null) {
                outAttrs.imeOptions |= 134217728;
            }
            if (focusSearch(33) != null) {
                outAttrs.imeOptions |= 67108864;
            }
            if ((outAttrs.imeOptions & 255) == 0) {
                if ((outAttrs.imeOptions & 134217728) != 0) {
                    outAttrs.imeOptions |= 5;
                } else {
                    outAttrs.imeOptions |= 6;
                }
                if (!shouldAdvanceFocusOnEnter()) {
                    outAttrs.imeOptions |= 1073741824;
                }
            }
            if (isMultilineInputType(outAttrs.inputType)) {
                outAttrs.imeOptions |= 1073741824;
            }
            outAttrs.hintText = this.mHint;
            if (this.mText instanceof Editable) {
                InputConnection ic = new EditableInputConnection(this);
                outAttrs.initialSelStart = getSelectionStart();
                outAttrs.initialSelEnd = getSelectionEnd();
                outAttrs.initialCapsMode = ic.getCursorCapsMode(getInputType());
                return ic;
            }
        }
        return null;
    }

    public boolean extractText(ExtractedTextRequest request, ExtractedText outText) {
        createEditorIfNeeded();
        return this.mEditor.extractText(request, outText);
    }

    static synchronized void removeParcelableSpans(Spannable spannable, int start, int end) {
        Object[] spans = spannable.getSpans(start, end, ParcelableSpan.class);
        int i = spans.length;
        while (i > 0) {
            i--;
            spannable.removeSpan(spans[i]);
        }
    }

    public void setExtractedText(ExtractedText text) {
        Editable content = getEditableText();
        if (text.text != null) {
            if (content == null) {
                setText(text.text, BufferType.EDITABLE);
            } else {
                int start = 0;
                int end = content.length();
                if (text.partialStartOffset >= 0) {
                    int N = content.length();
                    start = text.partialStartOffset;
                    if (start > N) {
                        start = N;
                    }
                    end = text.partialEndOffset;
                    if (end > N) {
                        end = N;
                    }
                }
                int start2 = start;
                int end2 = end;
                removeParcelableSpans(content, start2, end2);
                if (TextUtils.equals(content.subSequence(start2, end2), text.text)) {
                    if (text.text instanceof Spanned) {
                        TextUtils.copySpansFrom((Spanned) text.text, 0, end2 - start2, Object.class, content, start2);
                    }
                } else {
                    content.replace(start2, end2, text.text);
                }
            }
        }
        Spannable sp = (Spannable) getText();
        int N2 = sp.length();
        int start3 = text.selectionStart;
        if (start3 < 0) {
            start3 = 0;
        } else if (start3 > N2) {
            start3 = N2;
        }
        int end3 = text.selectionEnd;
        if (end3 < 0) {
            end3 = 0;
        } else if (end3 > N2) {
            end3 = N2;
        }
        Selection.setSelection(sp, start3, end3);
        if ((text.flags & 2) != 0) {
            MetaKeyKeyListener.startSelecting(this, sp);
        } else {
            MetaKeyKeyListener.stopSelecting(this, sp);
        }
        setHintInternal(text.hint);
    }

    public synchronized void setExtracting(ExtractedTextRequest req) {
        if (this.mEditor.mInputMethodState != null) {
            this.mEditor.mInputMethodState.mExtractedTextRequest = req;
        }
        this.mEditor.hideCursorAndSpanControllers();
        stopTextActionMode();
        if (this.mEditor.mSelectionModifierCursorController != null) {
            this.mEditor.mSelectionModifierCursorController.resetTouchOffsets();
        }
    }

    public void onCommitCompletion(CompletionInfo text) {
    }

    public void onCommitCorrection(CorrectionInfo info) {
        if (this.mEditor != null) {
            this.mEditor.onCommitCorrection(info);
        }
    }

    public void beginBatchEdit() {
        if (this.mEditor != null) {
            this.mEditor.beginBatchEdit();
        }
    }

    public void endBatchEdit() {
        if (this.mEditor != null) {
            this.mEditor.endBatchEdit();
        }
    }

    public void onBeginBatchEdit() {
    }

    public void onEndBatchEdit() {
    }

    public boolean onPrivateIMECommand(String action, Bundle data) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    @VisibleForTesting
    public void nullLayouts() {
        if ((this.mLayout instanceof BoringLayout) && this.mSavedLayout == null) {
            this.mSavedLayout = (BoringLayout) this.mLayout;
        }
        if ((this.mHintLayout instanceof BoringLayout) && this.mSavedHintLayout == null) {
            this.mSavedHintLayout = (BoringLayout) this.mHintLayout;
        }
        this.mHintLayout = null;
        this.mLayout = null;
        this.mSavedMarqueeModeLayout = null;
        this.mHintBoring = null;
        this.mBoring = null;
        if (this.mEditor != null) {
            this.mEditor.prepareCursorControllers();
        }
    }

    public protected void assumeLayout() {
        int width = ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight();
        if (width < 1) {
            width = 0;
        }
        int physicalWidth = width;
        if (this.mHorizontallyScrolling) {
            width = 1048576;
        }
        makeNewLayout(width, physicalWidth, UNKNOWN_BORING, UNKNOWN_BORING, physicalWidth, false);
    }

    public protected Layout.Alignment getLayoutAlignment() {
        switch (getTextAlignment()) {
            case 1:
                int i = this.mGravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;
                if (i == 1) {
                    Layout.Alignment alignment = Layout.Alignment.ALIGN_CENTER;
                    return alignment;
                } else if (i == 3) {
                    Layout.Alignment alignment2 = Layout.Alignment.ALIGN_LEFT;
                    return alignment2;
                } else if (i == 5) {
                    Layout.Alignment alignment3 = Layout.Alignment.ALIGN_RIGHT;
                    return alignment3;
                } else if (i == 8388611) {
                    Layout.Alignment alignment4 = Layout.Alignment.ALIGN_NORMAL;
                    return alignment4;
                } else if (i == 8388613) {
                    Layout.Alignment alignment5 = Layout.Alignment.ALIGN_OPPOSITE;
                    return alignment5;
                } else {
                    Layout.Alignment alignment6 = Layout.Alignment.ALIGN_NORMAL;
                    return alignment6;
                }
            case 2:
                Layout.Alignment alignment7 = Layout.Alignment.ALIGN_NORMAL;
                return alignment7;
            case 3:
                Layout.Alignment alignment8 = Layout.Alignment.ALIGN_OPPOSITE;
                return alignment8;
            case 4:
                Layout.Alignment alignment9 = Layout.Alignment.ALIGN_CENTER;
                return alignment9;
            case 5:
                if (getLayoutDirection() == 1) {
                    Layout.Alignment alignment10 = Layout.Alignment.ALIGN_RIGHT;
                    return alignment10;
                }
                Layout.Alignment alignment11 = Layout.Alignment.ALIGN_LEFT;
                return alignment11;
            case 6:
                if (getLayoutDirection() == 1) {
                    Layout.Alignment alignment12 = Layout.Alignment.ALIGN_LEFT;
                    return alignment12;
                }
                Layout.Alignment alignment13 = Layout.Alignment.ALIGN_RIGHT;
                return alignment13;
            default:
                Layout.Alignment alignment14 = Layout.Alignment.ALIGN_NORMAL;
                return alignment14;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:113:0x0221, code lost:
        if (r21 != r24.mLayout.getParagraphDirection(r3)) goto L104;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:130:0x024d  */
    /* JADX WARN: Removed duplicated region for block: B:132:? A[RETURN, SYNTHETIC] */
    @com.android.internal.annotations.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private protected void makeNewLayout(int r25, int r26, android.text.BoringLayout.Metrics r27, android.text.BoringLayout.Metrics r28, int r29, boolean r30) {
        /*
            Method dump skipped, instructions count: 595
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.TextView.makeNewLayout(int, int, android.text.BoringLayout$Metrics, android.text.BoringLayout$Metrics, int, boolean):void");
    }

    @VisibleForTesting
    public synchronized boolean useDynamicLayout() {
        return isTextSelectable() || (this.mSpannable != null && this.mPrecomputed == null);
    }

    protected synchronized Layout makeSingleLayout(int wantWidth, BoringLayout.Metrics boring, int ellipsisWidth, Layout.Alignment alignment, boolean shouldEllipsize, TextUtils.TruncateAt effectiveEllipsize, boolean useSaved) {
        BoringLayout.Metrics boring2;
        Layout result;
        Layout result2 = null;
        if (useDynamicLayout()) {
            result2 = DynamicLayout.Builder.obtain(this.mText, this.mTextPaint, wantWidth).setDisplayText(this.mTransformed).setAlignment(alignment).setTextDirection(this.mTextDir).setLineSpacing(this.mSpacingAdd, this.mSpacingMult).setIncludePad(this.mIncludePad).setUseLineSpacingFromFallbacks(this.mUseFallbackLineSpacing).setBreakStrategy(this.mBreakStrategy).setHyphenationFrequency(this.mHyphenationFrequency).setJustificationMode(this.mJustificationMode).setEllipsize(getKeyListener() == null ? effectiveEllipsize : null).setEllipsizedWidth(ellipsisWidth).build();
        } else {
            if (boring == UNKNOWN_BORING) {
                BoringLayout.Metrics boring3 = BoringLayout.isBoring(this.mTransformed, this.mTextPaint, this.mTextDir, this.mBoring);
                if (boring3 != null) {
                    this.mBoring = boring3;
                }
                boring2 = boring3;
            } else {
                boring2 = boring;
            }
            if (boring2 != null) {
                if (boring2.width <= wantWidth && (effectiveEllipsize == null || boring2.width <= ellipsisWidth)) {
                    if (useSaved && this.mSavedLayout != null) {
                        result = this.mSavedLayout.replaceOrMake(this.mTransformed, this.mTextPaint, wantWidth, alignment, this.mSpacingMult, this.mSpacingAdd, boring2, this.mIncludePad);
                    } else {
                        result = BoringLayout.make(this.mTransformed, this.mTextPaint, wantWidth, alignment, this.mSpacingMult, this.mSpacingAdd, boring2, this.mIncludePad);
                    }
                    result2 = result;
                    if (useSaved) {
                        this.mSavedLayout = (BoringLayout) result2;
                    }
                } else if (shouldEllipsize && boring2.width <= wantWidth) {
                    if (useSaved && this.mSavedLayout != null) {
                        result2 = this.mSavedLayout.replaceOrMake(this.mTransformed, this.mTextPaint, wantWidth, alignment, this.mSpacingMult, this.mSpacingAdd, boring2, this.mIncludePad, effectiveEllipsize, ellipsisWidth);
                    } else {
                        result2 = BoringLayout.make(this.mTransformed, this.mTextPaint, wantWidth, alignment, this.mSpacingMult, this.mSpacingAdd, boring2, this.mIncludePad, effectiveEllipsize, ellipsisWidth);
                    }
                }
            }
        }
        if (result2 == null) {
            StaticLayout.Builder builder = StaticLayout.Builder.obtain(this.mTransformed, 0, this.mTransformed.length(), this.mTextPaint, wantWidth).setAlignment(alignment).setTextDirection(this.mTextDir).setLineSpacing(this.mSpacingAdd, this.mSpacingMult).setIncludePad(this.mIncludePad).setUseLineSpacingFromFallbacks(this.mUseFallbackLineSpacing).setBreakStrategy(this.mBreakStrategy).setHyphenationFrequency(this.mHyphenationFrequency).setJustificationMode(this.mJustificationMode).setMaxLines(this.mMaxMode == 1 ? this.mMaximum : Integer.MAX_VALUE);
            if (shouldEllipsize) {
                builder.setEllipsize(effectiveEllipsize).setEllipsizedWidth(ellipsisWidth);
            }
            Layout result3 = builder.build();
            return result3;
        }
        return result2;
    }

    public protected boolean compressText(float width) {
        if (!isHardwareAccelerated() && width > 0.0f && this.mLayout != null && getLineCount() == 1 && !this.mUserSetTextScaleX && this.mTextPaint.getTextScaleX() == 1.0f) {
            float textWidth = this.mLayout.getLineWidth(0);
            float overflow = ((textWidth + 1.0f) - width) / width;
            if (overflow > 0.0f && overflow <= 0.07f) {
                this.mTextPaint.setTextScaleX((1.0f - overflow) - 0.005f);
                post(new Runnable() { // from class: android.widget.TextView.2
                    @Override // java.lang.Runnable
                    public void run() {
                        TextView.this.requestLayout();
                    }
                });
                return true;
            }
        }
        return false;
    }

    private static synchronized int desired(Layout layout) {
        int n = layout.getLineCount();
        CharSequence text = layout.getText();
        float max = 0.0f;
        for (int i = 0; i < n - 1; i++) {
            if (text.charAt(layout.getLineEnd(i) - 1) != '\n') {
                return -1;
            }
        }
        for (int i2 = 0; i2 < n; i2++) {
            max = Math.max(max, layout.getLineWidth(i2));
        }
        return (int) Math.ceil(max);
    }

    public void setIncludeFontPadding(boolean includepad) {
        if (this.mIncludePad != includepad) {
            this.mIncludePad = includepad;
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public boolean getIncludeFontPadding() {
        return this.mIncludePad;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int width2;
        int width3;
        BoringLayout.Metrics boring;
        BoringLayout.Metrics hintBoring;
        int des;
        boolean fromexisting;
        int width4;
        int hintWidth;
        int unpaddedWidth;
        int unpaddedWidth2;
        int hintWant;
        boolean z;
        boolean layoutChanged;
        boolean maximumChanged;
        int height;
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        BoringLayout.Metrics boring2 = UNKNOWN_BORING;
        BoringLayout.Metrics hintBoring2 = UNKNOWN_BORING;
        if (this.mTextDir == null) {
            this.mTextDir = getTextDirectionHeuristic();
        }
        int des2 = -1;
        boolean fromexisting2 = false;
        float widthLimit = widthMode == Integer.MIN_VALUE ? widthSize : Float.MAX_VALUE;
        if (widthMode != 1073741824) {
            if (this.mLayout != null && this.mEllipsize == null) {
                des2 = desired(this.mLayout);
            }
            if (des2 < 0) {
                boring2 = BoringLayout.isBoring(this.mTransformed, this.mTextPaint, this.mTextDir, this.mBoring);
                if (boring2 != null) {
                    this.mBoring = boring2;
                }
            } else {
                fromexisting2 = true;
            }
            if (boring2 == null || boring2 == UNKNOWN_BORING) {
                if (des2 < 0) {
                    des2 = (int) Math.ceil(Layout.getDesiredWidthWithLimit(this.mTransformed, 0, this.mTransformed.length(), this.mTextPaint, this.mTextDir, widthLimit));
                }
                width = des2;
            } else {
                width = boring2.width;
            }
            Drawables dr = this.mDrawables;
            if (dr != null) {
                width = Math.max(Math.max(width, dr.mDrawableWidthTop), dr.mDrawableWidthBottom);
            }
            if (this.mHint != null) {
                int hintDes = -1;
                if (this.mHintLayout != null && this.mEllipsize == null) {
                    hintDes = desired(this.mHintLayout);
                }
                if (hintDes < 0 && (hintBoring2 = BoringLayout.isBoring(this.mHint, this.mTextPaint, this.mTextDir, this.mHintBoring)) != null) {
                    this.mHintBoring = hintBoring2;
                }
                if (hintBoring2 == null || hintBoring2 == UNKNOWN_BORING) {
                    if (hintDes < 0) {
                        hintDes = (int) Math.ceil(Layout.getDesiredWidthWithLimit(this.mHint, 0, this.mHint.length(), this.mTextPaint, this.mTextDir, widthLimit));
                    }
                    hintWidth = hintDes;
                } else {
                    hintWidth = hintBoring2.width;
                }
                if (hintWidth > width) {
                    width = hintWidth;
                }
            }
            int hintWidth2 = getCompoundPaddingLeft();
            int width5 = width + hintWidth2 + getCompoundPaddingRight();
            if (this.mMaxWidthMode == 1) {
                width2 = Math.min(width5, this.mMaxWidth * getLineHeight());
            } else {
                width2 = Math.min(width5, this.mMaxWidth);
            }
            if (this.mMinWidthMode == 1) {
                width3 = Math.max(width2, this.mMinWidth * getLineHeight());
            } else {
                width3 = Math.max(width2, this.mMinWidth);
            }
            int width6 = Math.max(width3, getSuggestedMinimumWidth());
            if (widthMode == Integer.MIN_VALUE) {
                width6 = Math.min(widthSize, width6);
            }
            boring = boring2;
            hintBoring = hintBoring2;
            des = des2;
            fromexisting = fromexisting2;
            width4 = width6;
        } else {
            boring = boring2;
            hintBoring = hintBoring2;
            des = -1;
            fromexisting = false;
            width4 = widthSize;
        }
        int want = (width4 - getCompoundPaddingLeft()) - getCompoundPaddingRight();
        if (this.mHorizontallyScrolling) {
            want = 1048576;
        }
        int want2 = want;
        int hintWidth3 = this.mHintLayout == null ? want2 : this.mHintLayout.getWidth();
        if (this.mLayout == null) {
            unpaddedWidth = want;
            unpaddedWidth2 = 1073741824;
            makeNewLayout(want2, want2, boring, hintBoring, (width4 - getCompoundPaddingLeft()) - getCompoundPaddingRight(), false);
        } else {
            unpaddedWidth = want;
            unpaddedWidth2 = 1073741824;
            int widthSize2 = des;
            if (this.mLayout.getWidth() == want2) {
                hintWant = want2;
                if (hintWidth3 == hintWant && this.mLayout.getEllipsizedWidth() == (width4 - getCompoundPaddingLeft()) - getCompoundPaddingRight()) {
                    z = false;
                    layoutChanged = z;
                    boolean widthChanged = this.mHint != null && this.mEllipsize == null && want2 > this.mLayout.getWidth() && ((this.mLayout instanceof BoringLayout) || (fromexisting && widthSize2 >= 0 && widthSize2 <= want2));
                    maximumChanged = this.mMaxMode == this.mOldMaxMode || this.mMaximum != this.mOldMaximum;
                    if (!layoutChanged || maximumChanged) {
                        if (maximumChanged && widthChanged) {
                            this.mLayout.increaseWidthTo(want2);
                        } else {
                            makeNewLayout(want2, hintWant, boring, hintBoring, (width4 - getCompoundPaddingLeft()) - getCompoundPaddingRight(), false);
                        }
                    }
                }
            } else {
                hintWant = want2;
            }
            z = true;
            layoutChanged = z;
            boolean widthChanged2 = this.mHint != null && this.mEllipsize == null && want2 > this.mLayout.getWidth() && ((this.mLayout instanceof BoringLayout) || (fromexisting && widthSize2 >= 0 && widthSize2 <= want2));
            maximumChanged = this.mMaxMode == this.mOldMaxMode || this.mMaximum != this.mOldMaximum;
            if (!layoutChanged) {
            }
            if (maximumChanged) {
            }
            makeNewLayout(want2, hintWant, boring, hintBoring, (width4 - getCompoundPaddingLeft()) - getCompoundPaddingRight(), false);
        }
        if (heightMode == unpaddedWidth2) {
            height = heightSize;
            this.mDesiredHeightAtMeasure = -1;
        } else {
            int desired = getDesiredHeight();
            this.mDesiredHeightAtMeasure = desired;
            if (heightMode == Integer.MIN_VALUE) {
                height = Math.min(desired, heightSize);
            } else {
                height = desired;
            }
        }
        int height2 = getCompoundPaddingTop();
        int unpaddedHeight = (height - height2) - getCompoundPaddingBottom();
        if (this.mMaxMode == 1 && this.mLayout.getLineCount() > this.mMaximum) {
            unpaddedHeight = Math.min(unpaddedHeight, this.mLayout.getLineTop(this.mMaximum));
        }
        if (this.mMovement == null && this.mLayout.getWidth() <= unpaddedWidth && this.mLayout.getHeight() <= unpaddedHeight) {
            scrollTo(0, 0);
            setMeasuredDimension(width4, height);
        }
        registerForPreDraw();
        setMeasuredDimension(width4, height);
    }

    private synchronized void autoSizeText() {
        int availableWidth;
        if (!isAutoSizeEnabled()) {
            return;
        }
        if (this.mNeedsAutoSizeText) {
            if (getMeasuredWidth() <= 0 || getMeasuredHeight() <= 0) {
                return;
            }
            if (this.mHorizontallyScrolling) {
                availableWidth = 1048576;
            } else {
                availableWidth = (getMeasuredWidth() - getTotalPaddingLeft()) - getTotalPaddingRight();
            }
            int availableHeight = (getMeasuredHeight() - getExtendedPaddingBottom()) - getExtendedPaddingTop();
            if (availableWidth <= 0 || availableHeight <= 0) {
                return;
            }
            synchronized (TEMP_RECTF) {
                TEMP_RECTF.setEmpty();
                TEMP_RECTF.right = availableWidth;
                TEMP_RECTF.bottom = availableHeight;
                float optimalTextSize = findLargestTextSizeWhichFits(TEMP_RECTF);
                if (optimalTextSize != getTextSize()) {
                    setTextSizeInternal(0, optimalTextSize, false);
                    makeNewLayout(availableWidth, 0, UNKNOWN_BORING, UNKNOWN_BORING, ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight(), false);
                }
            }
        }
        this.mNeedsAutoSizeText = true;
    }

    private synchronized int findLargestTextSizeWhichFits(RectF availableSpace) {
        int sizesCount = this.mAutoSizeTextSizesInPx.length;
        if (sizesCount == 0) {
            throw new IllegalStateException("No available text sizes to choose from.");
        }
        int bestSizeIndex = 0;
        int lowIndex = 0 + 1;
        int highIndex = sizesCount - 1;
        while (lowIndex <= highIndex) {
            int sizeToTryIndex = (lowIndex + highIndex) / 2;
            if (suggestedSizeFitsInSpace(this.mAutoSizeTextSizesInPx[sizeToTryIndex], availableSpace)) {
                bestSizeIndex = lowIndex;
                lowIndex = sizeToTryIndex + 1;
            } else {
                highIndex = sizeToTryIndex - 1;
                bestSizeIndex = highIndex;
            }
        }
        return this.mAutoSizeTextSizesInPx[bestSizeIndex];
    }

    private synchronized boolean suggestedSizeFitsInSpace(int suggestedSizeInPx, RectF availableSpace) {
        CharSequence text;
        if (this.mTransformed != null) {
            text = this.mTransformed;
        } else {
            text = getText();
        }
        int maxLines = getMaxLines();
        if (this.mTempTextPaint == null) {
            this.mTempTextPaint = new TextPaint();
        } else {
            this.mTempTextPaint.reset();
        }
        this.mTempTextPaint.set(getPaint());
        this.mTempTextPaint.setTextSize(suggestedSizeInPx);
        StaticLayout.Builder layoutBuilder = StaticLayout.Builder.obtain(text, 0, text.length(), this.mTempTextPaint, Math.round(availableSpace.right));
        layoutBuilder.setAlignment(getLayoutAlignment()).setLineSpacing(getLineSpacingExtra(), getLineSpacingMultiplier()).setIncludePad(getIncludeFontPadding()).setUseLineSpacingFromFallbacks(this.mUseFallbackLineSpacing).setBreakStrategy(getBreakStrategy()).setHyphenationFrequency(getHyphenationFrequency()).setJustificationMode(getJustificationMode()).setMaxLines(this.mMaxMode == 1 ? this.mMaximum : Integer.MAX_VALUE).setTextDirection(getTextDirectionHeuristic());
        StaticLayout layout = layoutBuilder.build();
        return (maxLines == -1 || layout.getLineCount() <= maxLines) && ((float) layout.getHeight()) <= availableSpace.bottom;
    }

    private synchronized int getDesiredHeight() {
        return Math.max(getDesiredHeight(this.mLayout, true), getDesiredHeight(this.mHintLayout, this.mEllipsize != null));
    }

    private synchronized int getDesiredHeight(Layout layout, boolean cap) {
        if (layout == null) {
            return 0;
        }
        int desired = layout.getHeight(cap);
        Drawables dr = this.mDrawables;
        if (dr != null) {
            desired = Math.max(Math.max(desired, dr.mDrawableHeightLeft), dr.mDrawableHeightRight);
        }
        int linecount = layout.getLineCount();
        int padding = getCompoundPaddingTop() + getCompoundPaddingBottom();
        int desired2 = desired + padding;
        if (this.mMaxMode != 1) {
            desired2 = Math.min(desired2, this.mMaximum);
        } else if (cap && linecount > this.mMaximum && ((layout instanceof DynamicLayout) || (layout instanceof BoringLayout))) {
            int desired3 = layout.getLineTop(this.mMaximum);
            if (dr != null) {
                desired3 = Math.max(Math.max(desired3, dr.mDrawableHeightLeft), dr.mDrawableHeightRight);
            }
            desired2 = desired3 + padding;
            linecount = this.mMaximum;
        }
        if (this.mMinMode == 1) {
            if (linecount < this.mMinimum) {
                desired2 += getLineHeight() * (this.mMinimum - linecount);
            }
        } else {
            desired2 = Math.max(desired2, this.mMinimum);
        }
        return Math.max(desired2, getSuggestedMinimumHeight());
    }

    private synchronized void checkForResize() {
        boolean sizeChanged = false;
        if (this.mLayout != null) {
            if (this.mLayoutParams.width == -2) {
                sizeChanged = true;
                invalidate();
            }
            if (this.mLayoutParams.height == -2) {
                int desiredHeight = getDesiredHeight();
                if (desiredHeight != getHeight()) {
                    sizeChanged = true;
                }
            } else if (this.mLayoutParams.height == -1 && this.mDesiredHeightAtMeasure >= 0) {
                int desiredHeight2 = getDesiredHeight();
                if (desiredHeight2 != this.mDesiredHeightAtMeasure) {
                    sizeChanged = true;
                }
            }
        }
        if (sizeChanged) {
            requestLayout();
        }
    }

    public protected void checkForRelayout() {
        if ((this.mLayoutParams.width != -2 || (this.mMaxWidthMode == this.mMinWidthMode && this.mMaxWidth == this.mMinWidth)) && ((this.mHint == null || this.mHintLayout != null) && ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight() > 0)) {
            int oldht = this.mLayout.getHeight();
            int want = this.mLayout.getWidth();
            int hintWant = this.mHintLayout == null ? 0 : this.mHintLayout.getWidth();
            makeNewLayout(want, hintWant, UNKNOWN_BORING, UNKNOWN_BORING, ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight(), false);
            if (this.mEllipsize != TextUtils.TruncateAt.MARQUEE) {
                if (this.mLayoutParams.height != -2 && this.mLayoutParams.height != -1) {
                    autoSizeText();
                    invalidate();
                    return;
                } else if (this.mLayout.getHeight() == oldht && (this.mHintLayout == null || this.mHintLayout.getHeight() == oldht)) {
                    autoSizeText();
                    invalidate();
                    return;
                }
            }
            requestLayout();
            invalidate();
            return;
        }
        nullLayouts();
        requestLayout();
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mDeferScroll >= 0) {
            int curs = this.mDeferScroll;
            this.mDeferScroll = -1;
            bringPointIntoView(Math.min(curs, this.mText.length()));
        }
        autoSizeText();
    }

    private synchronized boolean isShowingHint() {
        return TextUtils.isEmpty(this.mText) && !TextUtils.isEmpty(this.mHint);
    }

    public protected boolean bringTextIntoView() {
        int left;
        int scrolly;
        int scrollx;
        Layout layout = isShowingHint() ? this.mHintLayout : this.mLayout;
        int line = 0;
        if ((this.mGravity & 112) == 80) {
            line = layout.getLineCount() - 1;
        }
        Layout.Alignment a = layout.getParagraphAlignment(line);
        int dir = layout.getParagraphDirection(line);
        int hspace = ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight();
        int vspace = ((this.mBottom - this.mTop) - getExtendedPaddingTop()) - getExtendedPaddingBottom();
        int ht = layout.getHeight();
        if (a == Layout.Alignment.ALIGN_NORMAL) {
            a = dir == 1 ? Layout.Alignment.ALIGN_LEFT : Layout.Alignment.ALIGN_RIGHT;
        } else if (a == Layout.Alignment.ALIGN_OPPOSITE) {
            a = dir == 1 ? Layout.Alignment.ALIGN_RIGHT : Layout.Alignment.ALIGN_LEFT;
        }
        if (a == Layout.Alignment.ALIGN_CENTER) {
            int left2 = (int) Math.floor(layout.getLineLeft(line));
            int right = (int) Math.ceil(layout.getLineRight(line));
            if (right - left2 < hspace) {
                scrollx = ((right + left2) / 2) - (hspace / 2);
            } else {
                scrollx = dir < 0 ? right - hspace : left2;
            }
            left = scrollx;
        } else if (a == Layout.Alignment.ALIGN_RIGHT) {
            left = ((int) Math.ceil(layout.getLineRight(line))) - hspace;
        } else {
            left = (int) Math.floor(layout.getLineLeft(line));
        }
        if (ht < vspace) {
            scrolly = 0;
        } else if ((this.mGravity & 112) == 80) {
            scrolly = ht - vspace;
        } else {
            scrolly = 0;
        }
        if (left == this.mScrollX && scrolly == this.mScrollY) {
            return false;
        }
        scrollTo(left, scrolly);
        return true;
    }

    public boolean bringPointIntoView(int offset) {
        int grav;
        int vs;
        boolean changed;
        if (isLayoutRequested()) {
            this.mDeferScroll = offset;
            return false;
        }
        Layout layout = isShowingHint() ? this.mHintLayout : this.mLayout;
        if (layout == null) {
            return false;
        }
        int line = layout.getLineForOffset(offset);
        switch (layout.getParagraphAlignment(line)) {
            case ALIGN_LEFT:
                grav = 1;
                break;
            case ALIGN_RIGHT:
                grav = -1;
                break;
            case ALIGN_NORMAL:
                grav = layout.getParagraphDirection(line);
                break;
            case ALIGN_OPPOSITE:
                grav = -layout.getParagraphDirection(line);
                break;
            default:
                grav = 0;
                break;
        }
        boolean clamped = grav > 0;
        int x = (int) layout.getPrimaryHorizontal(offset, clamped);
        int top = layout.getLineTop(line);
        int bottom = layout.getLineTop(line + 1);
        int left = (int) Math.floor(layout.getLineLeft(line));
        int right = (int) Math.ceil(layout.getLineRight(line));
        int ht = layout.getHeight();
        int hspace = ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight();
        int vspace = ((this.mBottom - this.mTop) - getExtendedPaddingTop()) - getExtendedPaddingBottom();
        if (!this.mHorizontallyScrolling && right - left > hspace && right > x) {
            right = Math.max(x, left + hspace);
        }
        int hslack = (bottom - top) / 2;
        int vslack = hslack;
        if (vslack > vspace / 4) {
            vslack = vspace / 4;
        }
        if (hslack > hspace / 4) {
            hslack = hspace / 4;
        }
        int hs = this.mScrollX;
        int vs2 = this.mScrollY;
        if (top - vs2 < vslack) {
            vs2 = top - vslack;
        }
        int vs3 = vs2;
        if (bottom - vs2 > vspace - vslack) {
            vs = bottom - (vspace - vslack);
        } else {
            vs = vs3;
        }
        if (ht - vs < vspace) {
            vs = ht - vspace;
        }
        if (0 - vs > 0) {
            vs = 0;
        }
        if (grav != 0) {
            if (x - hs < hslack) {
                hs = x - hslack;
            }
            int hs2 = hs;
            if (x - hs > hspace - hslack) {
                hs = x - (hspace - hslack);
            } else {
                hs = hs2;
            }
        }
        if (grav < 0) {
            if (left - hs > 0) {
                hs = left;
            }
            if (right - hs < hspace) {
                hs = right - hspace;
            }
        } else if (grav > 0) {
            if (right - hs < hspace) {
                hs = right - hspace;
            }
            if (left - hs > 0) {
                hs = left;
            }
        } else if (right - left <= hspace) {
            hs = left - ((hspace - (right - left)) / 2);
        } else if (x > right - hslack) {
            hs = right - hspace;
        } else if (x < left + hslack) {
            hs = left;
        } else if (left > hs) {
            hs = left;
        } else if (right < hs + hspace) {
            hs = right - hspace;
        } else {
            if (x - hs < hslack) {
                hs = x - hslack;
            }
            int hs3 = hs;
            if (x - hs > hspace - hslack) {
                hs = x - (hspace - hslack);
            } else {
                hs = hs3;
            }
        }
        if (hs == this.mScrollX && vs == this.mScrollY) {
            changed = false;
        } else {
            if (this.mScroller == null) {
                scrollTo(hs, vs);
            } else {
                long duration = AnimationUtils.currentAnimationTimeMillis() - this.mLastScroll;
                int dx = hs - this.mScrollX;
                int dy = vs - this.mScrollY;
                if (duration > 250) {
                    this.mScroller.startScroll(this.mScrollX, this.mScrollY, dx, dy);
                    awakenScrollBars(this.mScroller.getDuration());
                    invalidate();
                } else {
                    if (!this.mScroller.isFinished()) {
                        this.mScroller.abortAnimation();
                    }
                    scrollBy(dx, dy);
                }
                this.mLastScroll = AnimationUtils.currentAnimationTimeMillis();
            }
            changed = true;
        }
        if (isFocused()) {
            if (this.mTempRect == null) {
                this.mTempRect = new Rect();
            }
            this.mTempRect.set(x - 2, top, x + 2, bottom);
            getInterestingRect(this.mTempRect, line);
            this.mTempRect.offset(this.mScrollX, this.mScrollY);
            if (requestRectangleOnScreen(this.mTempRect)) {
                return true;
            }
            return changed;
        }
        return changed;
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00a2  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00ab  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean moveCursorToVisibleOffset() {
        /*
            r18 = this;
            r0 = r18
            java.lang.CharSequence r1 = r0.mText
            boolean r1 = r1 instanceof android.text.Spannable
            r2 = 0
            if (r1 != 0) goto La
            return r2
        La:
            int r1 = r18.getSelectionStart()
            int r3 = r18.getSelectionEnd()
            if (r1 == r3) goto L15
            return r2
        L15:
            android.text.Layout r4 = r0.mLayout
            int r4 = r4.getLineForOffset(r1)
            android.text.Layout r5 = r0.mLayout
            int r5 = r5.getLineTop(r4)
            android.text.Layout r6 = r0.mLayout
            int r7 = r4 + 1
            int r6 = r6.getLineTop(r7)
            int r7 = r0.mBottom
            int r8 = r0.mTop
            int r7 = r7 - r8
            int r8 = r18.getExtendedPaddingTop()
            int r7 = r7 - r8
            int r8 = r18.getExtendedPaddingBottom()
            int r7 = r7 - r8
            int r8 = r6 - r5
            int r8 = r8 / 2
            int r9 = r7 / 4
            if (r8 <= r9) goto L42
            int r8 = r7 / 4
        L42:
            int r9 = r0.mScrollY
            int r10 = r9 + r8
            if (r5 >= r10) goto L54
            android.text.Layout r10 = r0.mLayout
            int r11 = r9 + r8
            int r12 = r6 - r5
            int r11 = r11 + r12
            int r4 = r10.getLineForVertical(r11)
            goto L65
        L54:
            int r10 = r7 + r9
            int r10 = r10 - r8
            if (r6 <= r10) goto L65
            android.text.Layout r10 = r0.mLayout
            int r11 = r7 + r9
            int r11 = r11 - r8
            int r12 = r6 - r5
            int r11 = r11 - r12
            int r4 = r10.getLineForVertical(r11)
        L65:
            int r10 = r0.mRight
            int r11 = r0.mLeft
            int r10 = r10 - r11
            int r11 = r18.getCompoundPaddingLeft()
            int r10 = r10 - r11
            int r11 = r18.getCompoundPaddingRight()
            int r10 = r10 - r11
            int r11 = r0.mScrollX
            android.text.Layout r12 = r0.mLayout
            float r13 = (float) r11
            int r12 = r12.getOffsetForHorizontal(r4, r13)
            android.text.Layout r13 = r0.mLayout
            int r14 = r10 + r11
            float r14 = (float) r14
            int r13 = r13.getOffsetForHorizontal(r4, r14)
            if (r12 >= r13) goto L8a
            r14 = r12
            goto L8b
        L8a:
            r14 = r13
        L8b:
            if (r12 <= r13) goto L8f
            r15 = r12
            goto L90
        L8f:
            r15 = r13
        L90:
            r16 = r1
            r2 = r16
            if (r2 >= r14) goto L9b
            r16 = r14
        L98:
            r2 = r16
            goto La0
        L9b:
            if (r2 <= r15) goto La0
            r16 = r15
            goto L98
        La0:
            if (r2 == r1) goto Lab
            r17 = r1
            android.text.Spannable r1 = r0.mSpannable
            android.text.Selection.setSelection(r1, r2)
            r1 = 1
            return r1
        Lab:
            r17 = r1
            r1 = 0
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.TextView.moveCursorToVisibleOffset():boolean");
    }

    @Override // android.view.View
    public void computeScroll() {
        if (this.mScroller != null && this.mScroller.computeScrollOffset()) {
            this.mScrollX = this.mScroller.getCurrX();
            this.mScrollY = this.mScroller.getCurrY();
            invalidateParentCaches();
            postInvalidate();
        }
    }

    private synchronized void getInterestingRect(Rect r, int line) {
        convertFromViewportToContentCoordinates(r);
        if (line == 0) {
            r.top -= getExtendedPaddingTop();
        }
        if (line == this.mLayout.getLineCount() - 1) {
            r.bottom += getExtendedPaddingBottom();
        }
    }

    private synchronized void convertFromViewportToContentCoordinates(Rect r) {
        int horizontalOffset = viewportToContentHorizontalOffset();
        r.left += horizontalOffset;
        r.right += horizontalOffset;
        int verticalOffset = viewportToContentVerticalOffset();
        r.top += verticalOffset;
        r.bottom += verticalOffset;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int viewportToContentHorizontalOffset() {
        return getCompoundPaddingLeft() - this.mScrollX;
    }

    public private protected int viewportToContentVerticalOffset() {
        int offset = getExtendedPaddingTop() - this.mScrollY;
        if ((this.mGravity & 112) != 48) {
            return offset + getVerticalOffset(false);
        }
        return offset;
    }

    public void debug(int depth) {
        String output;
        super.debug(depth);
        String output2 = debugIndent(depth) + "frame={" + this.mLeft + ", " + this.mTop + ", " + this.mRight + ", " + this.mBottom + "} scroll={" + this.mScrollX + ", " + this.mScrollY + "} ";
        if (this.mText != null) {
            output = output2 + "mText=\"" + ((Object) this.mText) + "\" ";
            if (this.mLayout != null) {
                output = output + "mLayout width=" + this.mLayout.getWidth() + " height=" + this.mLayout.getHeight();
            }
        } else {
            output = output2 + "mText=NULL";
        }
        Log.d("View", output);
    }

    @ViewDebug.ExportedProperty(category = "text")
    public int getSelectionStart() {
        return Selection.getSelectionStart(getText());
    }

    @ViewDebug.ExportedProperty(category = "text")
    public int getSelectionEnd() {
        return Selection.getSelectionEnd(getText());
    }

    public boolean hasSelection() {
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        return selectionStart >= 0 && selectionEnd > 0 && selectionStart != selectionEnd;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized String getSelectedText() {
        if (!hasSelection()) {
            return null;
        }
        int start = getSelectionStart();
        int end = getSelectionEnd();
        return String.valueOf(start > end ? this.mText.subSequence(end, start) : this.mText.subSequence(start, end));
    }

    public void setSingleLine() {
        setSingleLine(true);
    }

    public void setAllCaps(boolean allCaps) {
        if (allCaps) {
            setTransformationMethod(new AllCapsTransformationMethod(getContext()));
        } else {
            setTransformationMethod(null);
        }
    }

    public boolean isAllCaps() {
        TransformationMethod method = getTransformationMethod();
        return method != null && (method instanceof AllCapsTransformationMethod);
    }

    @RemotableViewMethod
    public void setSingleLine(boolean singleLine) {
        setInputTypeSingleLine(singleLine);
        applySingleLine(singleLine, true, true);
    }

    private synchronized void setInputTypeSingleLine(boolean singleLine) {
        if (this.mEditor != null && (this.mEditor.mInputType & 15) == 1) {
            if (singleLine) {
                this.mEditor.mInputType &= -131073;
                return;
            }
            this.mEditor.mInputType |= 131072;
        }
    }

    private synchronized void applySingleLine(boolean singleLine, boolean applyTransformation, boolean changeMaxLines) {
        this.mSingleLine = singleLine;
        if (singleLine) {
            setLines(1);
            setHorizontallyScrolling(true);
            if (applyTransformation) {
                setTransformationMethod(SingleLineTransformationMethod.getInstance());
                return;
            }
            return;
        }
        if (changeMaxLines) {
            setMaxLines(Integer.MAX_VALUE);
        }
        setHorizontallyScrolling(false);
        if (applyTransformation) {
            setTransformationMethod(null);
        }
    }

    public void setEllipsize(TextUtils.TruncateAt where) {
        if (this.mEllipsize != where) {
            this.mEllipsize = where;
            if (this.mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    public void setMarqueeRepeatLimit(int marqueeLimit) {
        this.mMarqueeRepeatLimit = marqueeLimit;
    }

    public int getMarqueeRepeatLimit() {
        return this.mMarqueeRepeatLimit;
    }

    @ViewDebug.ExportedProperty
    public TextUtils.TruncateAt getEllipsize() {
        return this.mEllipsize;
    }

    @RemotableViewMethod
    public void setSelectAllOnFocus(boolean selectAllOnFocus) {
        createEditorIfNeeded();
        this.mEditor.mSelectAllOnFocus = selectAllOnFocus;
        if (selectAllOnFocus && !(this.mText instanceof Spannable)) {
            setText(this.mText, BufferType.SPANNABLE);
        }
    }

    @RemotableViewMethod
    public void setCursorVisible(boolean visible) {
        if (visible && this.mEditor == null) {
            return;
        }
        createEditorIfNeeded();
        if (this.mEditor.mCursorVisible != visible) {
            this.mEditor.mCursorVisible = visible;
            invalidate();
            this.mEditor.makeBlink();
            this.mEditor.prepareCursorControllers();
        }
    }

    public boolean isCursorVisible() {
        if (this.mEditor == null) {
            return true;
        }
        return this.mEditor.mCursorVisible;
    }

    private synchronized boolean canMarquee() {
        int width = ((this.mRight - this.mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight();
        if (width > 0) {
            return this.mLayout.getLineWidth(0) > ((float) width) || !(this.mMarqueeFadeMode == 0 || this.mSavedMarqueeModeLayout == null || this.mSavedMarqueeModeLayout.getLineWidth(0) <= ((float) width));
        }
        return false;
    }

    public protected void startMarquee() {
        if (getKeyListener() != null || compressText((getWidth() - getCompoundPaddingLeft()) - getCompoundPaddingRight())) {
            return;
        }
        if (this.mMarquee == null || this.mMarquee.isStopped()) {
            if ((isFocused() || isSelected()) && getLineCount() == 1 && canMarquee()) {
                if (this.mMarqueeFadeMode == 1) {
                    this.mMarqueeFadeMode = 2;
                    Layout tmp = this.mLayout;
                    this.mLayout = this.mSavedMarqueeModeLayout;
                    this.mSavedMarqueeModeLayout = tmp;
                    setHorizontalFadingEdgeEnabled(true);
                    requestLayout();
                    invalidate();
                }
                if (this.mMarquee == null) {
                    this.mMarquee = new Marquee(this);
                }
                this.mMarquee.start(this.mMarqueeRepeatLimit);
            }
        }
    }

    private synchronized void stopMarquee() {
        if (this.mMarquee != null && !this.mMarquee.isStopped()) {
            this.mMarquee.stop();
        }
        if (this.mMarqueeFadeMode == 2) {
            this.mMarqueeFadeMode = 1;
            Layout tmp = this.mSavedMarqueeModeLayout;
            this.mSavedMarqueeModeLayout = this.mLayout;
            this.mLayout = tmp;
            setHorizontalFadingEdgeEnabled(false);
            requestLayout();
            invalidate();
        }
    }

    public protected void startStopMarquee(boolean start) {
        if (this.mEllipsize == TextUtils.TruncateAt.MARQUEE) {
            if (start) {
                startMarquee();
            } else {
                stopMarquee();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
    }

    protected void onSelectionChanged(int selStart, int selEnd) {
        sendAccessibilityEvent(8192);
    }

    public void addTextChangedListener(TextWatcher watcher) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList<>();
        }
        this.mListeners.add(watcher);
    }

    public void removeTextChangedListener(TextWatcher watcher) {
        int i;
        if (this.mListeners != null && (i = this.mListeners.indexOf(watcher)) >= 0) {
            this.mListeners.remove(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void sendBeforeTextChanged(CharSequence text, int start, int before, int after) {
        if (this.mListeners != null) {
            ArrayList<TextWatcher> list = this.mListeners;
            int count = list.size();
            for (int i = 0; i < count; i++) {
                list.get(i).beforeTextChanged(text, start, before, after);
            }
        }
        removeIntersectingNonAdjacentSpans(start, start + before, SpellCheckSpan.class);
        removeIntersectingNonAdjacentSpans(start, start + before, SuggestionSpan.class);
    }

    private synchronized <T> void removeIntersectingNonAdjacentSpans(int start, int end, Class<T> type) {
        if (this.mText instanceof Editable) {
            Editable text = (Editable) this.mText;
            Object[] spans = text.getSpans(start, end, type);
            int length = spans.length;
            for (int i = 0; i < length; i++) {
                int spanStart = text.getSpanStart(spans[i]);
                int spanEnd = text.getSpanEnd(spans[i]);
                if (spanEnd != start && spanStart != end) {
                    text.removeSpan(spans[i]);
                } else {
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void removeAdjacentSuggestionSpans(int pos) {
        if (this.mText instanceof Editable) {
            Editable text = (Editable) this.mText;
            SuggestionSpan[] spans = (SuggestionSpan[]) text.getSpans(pos, pos, SuggestionSpan.class);
            int length = spans.length;
            for (int i = 0; i < length; i++) {
                int spanStart = text.getSpanStart(spans[i]);
                int spanEnd = text.getSpanEnd(spans[i]);
                if ((spanEnd == pos || spanStart == pos) && SpellChecker.haveWordBoundariesChanged(text, pos, pos, spanStart, spanEnd)) {
                    text.removeSpan(spans[i]);
                }
            }
        }
    }

    synchronized void sendOnTextChanged(CharSequence text, int start, int before, int after) {
        if (this.mListeners != null) {
            ArrayList<TextWatcher> list = this.mListeners;
            int count = list.size();
            for (int i = 0; i < count; i++) {
                list.get(i).onTextChanged(text, start, before, after);
            }
        }
        if (this.mEditor != null) {
            this.mEditor.sendOnTextChanged(start, before, after);
        }
    }

    synchronized void sendAfterTextChanged(Editable text) {
        if (this.mListeners != null) {
            ArrayList<TextWatcher> list = this.mListeners;
            int count = list.size();
            for (int i = 0; i < count; i++) {
                list.get(i).afterTextChanged(text);
            }
        }
        notifyAutoFillManagerAfterTextChangedIfNeeded();
        hideErrorIfUnchanged();
    }

    private synchronized void notifyAutoFillManagerAfterTextChangedIfNeeded() {
        AutofillManager afm;
        if (!isAutofillable() || (afm = (AutofillManager) this.mContext.getSystemService(AutofillManager.class)) == null) {
            return;
        }
        if (this.mLastValueSentToAutofillManager == null || !this.mLastValueSentToAutofillManager.equals(this.mText)) {
            if (Helper.sVerbose) {
                Log.v(LOG_TAG, "notifying AFM after text changed");
            }
            afm.notifyValueChanged(this);
            this.mLastValueSentToAutofillManager = this.mText;
        } else if (Helper.sVerbose) {
            Log.v(LOG_TAG, "not notifying AFM on unchanged text");
        }
    }

    private synchronized boolean isAutofillable() {
        return getAutofillType() != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void updateAfterEdit() {
        invalidate();
        int curs = getSelectionStart();
        if (curs >= 0 || (this.mGravity & 112) == 80) {
            registerForPreDraw();
        }
        checkForResize();
        if (curs >= 0) {
            this.mHighlightPathBogus = true;
            if (this.mEditor != null) {
                this.mEditor.makeBlink();
            }
            bringPointIntoView(curs);
        }
    }

    synchronized void handleTextChanged(CharSequence buffer, int start, int before, int after) {
        sLastCutCopyOrTextChangedTime = 0L;
        Editor.InputMethodState ims = this.mEditor == null ? null : this.mEditor.mInputMethodState;
        if (ims == null || ims.mBatchEditNesting == 0) {
            updateAfterEdit();
        }
        if (ims != null) {
            ims.mContentChanged = true;
            if (ims.mChangedStart < 0) {
                ims.mChangedStart = start;
                ims.mChangedEnd = start + before;
            } else {
                ims.mChangedStart = Math.min(ims.mChangedStart, start);
                ims.mChangedEnd = Math.max(ims.mChangedEnd, (start + before) - ims.mChangedDelta);
            }
            ims.mChangedDelta += after - before;
        }
        resetErrorChangedFlag();
        sendOnTextChanged(buffer, start, before, after);
        onTextChanged(buffer, start, before, after);
    }

    synchronized void spanChange(Spanned buf, Object what, int oldStart, int newStart, int oldEnd, int newEnd) {
        boolean selChanged = false;
        int newSelStart = -1;
        int newSelEnd = -1;
        Editor.InputMethodState ims = this.mEditor == null ? null : this.mEditor.mInputMethodState;
        if (what == Selection.SELECTION_END) {
            selChanged = true;
            newSelEnd = newStart;
            if (oldStart >= 0 || newStart >= 0) {
                invalidateCursor(Selection.getSelectionStart(buf), oldStart, newStart);
                checkForResize();
                registerForPreDraw();
                if (this.mEditor != null) {
                    this.mEditor.makeBlink();
                }
            }
        }
        if (what == Selection.SELECTION_START) {
            selChanged = true;
            newSelStart = newStart;
            if (oldStart >= 0 || newStart >= 0) {
                int end = Selection.getSelectionEnd(buf);
                invalidateCursor(end, oldStart, newStart);
            }
        }
        if (selChanged) {
            this.mHighlightPathBogus = true;
            if (this.mEditor != null && !isFocused()) {
                this.mEditor.mSelectionMoved = true;
            }
            if ((buf.getSpanFlags(what) & 512) == 0) {
                if (newSelStart < 0) {
                    newSelStart = Selection.getSelectionStart(buf);
                }
                if (newSelEnd < 0) {
                    newSelEnd = Selection.getSelectionEnd(buf);
                }
                if (this.mEditor != null) {
                    this.mEditor.refreshTextActionMode();
                    if (!hasSelection() && this.mEditor.getTextActionMode() == null && hasTransientState()) {
                        setHasTransientState(false);
                    }
                }
                onSelectionChanged(newSelStart, newSelEnd);
            }
        }
        if ((what instanceof UpdateAppearance) || (what instanceof ParagraphStyle) || (what instanceof CharacterStyle)) {
            if (ims == null || ims.mBatchEditNesting == 0) {
                invalidate();
                this.mHighlightPathBogus = true;
                checkForResize();
            } else {
                ims.mContentChanged = true;
            }
            if (this.mEditor != null) {
                if (oldStart >= 0) {
                    this.mEditor.invalidateTextDisplayList(this.mLayout, oldStart, oldEnd);
                }
                if (newStart >= 0) {
                    this.mEditor.invalidateTextDisplayList(this.mLayout, newStart, newEnd);
                }
                this.mEditor.invalidateHandlesAndActionMode();
            }
        }
        if (MetaKeyKeyListener.isMetaTracker(buf, what)) {
            this.mHighlightPathBogus = true;
            if (ims != null && MetaKeyKeyListener.isSelectingMetaTracker(buf, what)) {
                ims.mSelectionModeChanged = true;
            }
            if (Selection.getSelectionStart(buf) >= 0) {
                if (ims == null || ims.mBatchEditNesting == 0) {
                    invalidateCursor();
                } else {
                    ims.mCursorChanged = true;
                }
            }
        }
        if ((what instanceof ParcelableSpan) && ims != null && ims.mExtractedTextRequest != null) {
            if (ims.mBatchEditNesting != 0) {
                if (oldStart >= 0) {
                    if (ims.mChangedStart > oldStart) {
                        ims.mChangedStart = oldStart;
                    }
                    if (ims.mChangedStart > oldEnd) {
                        ims.mChangedStart = oldEnd;
                    }
                }
                if (newStart >= 0) {
                    if (ims.mChangedStart > newStart) {
                        ims.mChangedStart = newStart;
                    }
                    if (ims.mChangedStart > newEnd) {
                        ims.mChangedStart = newEnd;
                    }
                }
            } else {
                ims.mContentChanged = true;
            }
        }
        if (this.mEditor != null && this.mEditor.mSpellChecker != null && newStart < 0 && (what instanceof SpellCheckSpan)) {
            this.mEditor.mSpellChecker.onSpellCheckSpanRemoved((SpellCheckSpan) what);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (isTemporarilyDetached()) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
            return;
        }
        if (this.mEditor != null) {
            this.mEditor.onFocusChanged(focused, direction);
        }
        if (focused && this.mSpannable != null) {
            MetaKeyKeyListener.resetMetaState(this.mSpannable);
        }
        startStopMarquee(focused);
        if (this.mTransformation != null) {
            this.mTransformation.onFocusChanged(this, this.mText, focused, direction, previouslyFocusedRect);
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override // android.view.View
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (this.mEditor != null) {
            this.mEditor.onWindowFocusChanged(hasWindowFocus);
        }
        startStopMarquee(hasWindowFocus);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (this.mEditor != null && visibility != 0) {
            this.mEditor.hideCursorAndSpanControllers();
            stopTextActionMode();
        }
    }

    public void clearComposingText() {
        if (this.mText instanceof Spannable) {
            BaseInputConnection.removeComposingSpans(this.mSpannable);
        }
    }

    @Override // android.view.View
    public void setSelected(boolean selected) {
        boolean wasSelected = isSelected();
        super.setSelected(selected);
        if (selected != wasSelected && this.mEllipsize == TextUtils.TruncateAt.MARQUEE) {
            if (selected) {
                startMarquee();
            } else {
                stopMarquee();
            }
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        if (this.mEditor != null) {
            this.mEditor.onTouchEvent(event);
            if (this.mEditor.mSelectionModifierCursorController != null && this.mEditor.mSelectionModifierCursorController.isDragAcceleratorActive()) {
                return true;
            }
        }
        boolean superResult = super.onTouchEvent(event);
        if (this.mEditor != null && this.mEditor.mDiscardNextActionUp && action == 1) {
            this.mEditor.mDiscardNextActionUp = false;
            if (this.mEditor.mIsInsertionActionModeStartPending) {
                this.mEditor.startInsertionActionMode();
                this.mEditor.mIsInsertionActionModeStartPending = false;
            }
            return superResult;
        }
        boolean touchIsFinished = action == 1 && (this.mEditor == null || !this.mEditor.mIgnoreActionUpEvent) && isFocused();
        if ((this.mMovement != null || onCheckIsTextEditor()) && isEnabled() && (this.mText instanceof Spannable) && this.mLayout != null) {
            boolean handled = this.mMovement != null ? false | this.mMovement.onTouchEvent(this, this.mSpannable, event) : false;
            boolean textIsSelectable = isTextSelectable();
            if (touchIsFinished && this.mLinksClickable && this.mAutoLinkMask != 0 && textIsSelectable) {
                ClickableSpan[] links = (ClickableSpan[]) this.mSpannable.getSpans(getSelectionStart(), getSelectionEnd(), ClickableSpan.class);
                if (links.length > 0) {
                    links[0].onClick(this);
                    handled = true;
                }
            }
            if (touchIsFinished && (isTextEditable() || textIsSelectable)) {
                InputMethodManager imm = InputMethodManager.peekInstance();
                viewClicked(imm);
                if (isTextEditable() && this.mEditor.mShowSoftInputOnFocus && imm != null) {
                    imm.showSoftInput(this, 0);
                }
                this.mEditor.onTouchUpEvent(event);
                handled = true;
            }
            if (handled) {
                return true;
            }
        }
        return superResult;
    }

    @Override // android.view.View
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (this.mMovement != null && (this.mText instanceof Spannable) && this.mLayout != null) {
            try {
                if (this.mMovement.onGenericMotionEvent(this, this.mSpannable, event)) {
                    return true;
                }
            } catch (AbstractMethodError e) {
            }
        }
        return super.onGenericMotionEvent(event);
    }

    @Override // android.view.View
    protected void onCreateContextMenu(ContextMenu menu) {
        if (this.mEditor != null) {
            this.mEditor.onCreateContextMenu(menu);
        }
    }

    @Override // android.view.View
    public boolean showContextMenu() {
        if (this.mEditor != null) {
            this.mEditor.setContextMenuAnchor(Float.NaN, Float.NaN);
        }
        return super.showContextMenu();
    }

    @Override // android.view.View
    public boolean showContextMenu(float x, float y) {
        if (this.mEditor != null) {
            this.mEditor.setContextMenuAnchor(x, y);
        }
        return super.showContextMenu(x, y);
    }

    public private protected boolean isTextEditable() {
        return (this.mText instanceof Editable) && onCheckIsTextEditor() && isEnabled();
    }

    public boolean didTouchFocusSelect() {
        return this.mEditor != null && this.mEditor.mTouchFocusSelected;
    }

    @Override // android.view.View
    public void cancelLongPress() {
        super.cancelLongPress();
        if (this.mEditor != null) {
            this.mEditor.mIgnoreActionUpEvent = true;
        }
    }

    @Override // android.view.View
    public boolean onTrackballEvent(MotionEvent event) {
        if (this.mMovement != null && this.mSpannable != null && this.mLayout != null && this.mMovement.onTrackballEvent(this, this.mSpannable, event)) {
            return true;
        }
        return super.onTrackballEvent(event);
    }

    public void setScroller(Scroller s) {
        this.mScroller = s;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public float getLeftFadingEdgeStrength() {
        if (isMarqueeFadeEnabled() && this.mMarquee != null && !this.mMarquee.isStopped()) {
            Marquee marquee = this.mMarquee;
            if (marquee.shouldDrawLeftFade()) {
                return getHorizontalFadingEdgeStrength(marquee.getScroll(), 0.0f);
            }
            return 0.0f;
        } else if (getLineCount() == 1) {
            float lineLeft = getLayout().getLineLeft(0);
            if (lineLeft > this.mScrollX) {
                return 0.0f;
            }
            return getHorizontalFadingEdgeStrength(this.mScrollX, lineLeft);
        } else {
            return super.getLeftFadingEdgeStrength();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public float getRightFadingEdgeStrength() {
        if (isMarqueeFadeEnabled() && this.mMarquee != null && !this.mMarquee.isStopped()) {
            Marquee marquee = this.mMarquee;
            return getHorizontalFadingEdgeStrength(marquee.getMaxFadeScroll(), marquee.getScroll());
        } else if (getLineCount() == 1) {
            float rightEdge = this.mScrollX + ((getWidth() - getCompoundPaddingLeft()) - getCompoundPaddingRight());
            float lineRight = getLayout().getLineRight(0);
            if (lineRight < rightEdge) {
                return 0.0f;
            }
            return getHorizontalFadingEdgeStrength(rightEdge, lineRight);
        } else {
            return super.getRightFadingEdgeStrength();
        }
    }

    private synchronized float getHorizontalFadingEdgeStrength(float position1, float position2) {
        int horizontalFadingEdgeLength = getHorizontalFadingEdgeLength();
        if (horizontalFadingEdgeLength == 0) {
            return 0.0f;
        }
        float diff = Math.abs(position1 - position2);
        if (diff > horizontalFadingEdgeLength) {
            return 1.0f;
        }
        return diff / horizontalFadingEdgeLength;
    }

    private synchronized boolean isMarqueeFadeEnabled() {
        return this.mEllipsize == TextUtils.TruncateAt.MARQUEE && this.mMarqueeFadeMode != 1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public int computeHorizontalScrollRange() {
        if (this.mLayout != null) {
            return (this.mSingleLine && (this.mGravity & 7) == 3) ? (int) this.mLayout.getLineWidth(0) : this.mLayout.getWidth();
        }
        return super.computeHorizontalScrollRange();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public int computeVerticalScrollRange() {
        if (this.mLayout != null) {
            return this.mLayout.getHeight();
        }
        return super.computeVerticalScrollRange();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public int computeVerticalScrollExtent() {
        return (getHeight() - getCompoundPaddingTop()) - getCompoundPaddingBottom();
    }

    @Override // android.view.View
    public void findViewsWithText(ArrayList<View> outViews, CharSequence searched, int flags) {
        super.findViewsWithText(outViews, searched, flags);
        if (!outViews.contains(this) && (flags & 1) != 0 && !TextUtils.isEmpty(searched) && !TextUtils.isEmpty(this.mText)) {
            String searchedLowerCase = searched.toString().toLowerCase();
            String textLowerCase = this.mText.toString().toLowerCase();
            if (textLowerCase.contains(searchedLowerCase)) {
                outViews.add(this);
            }
        }
    }

    private protected static ColorStateList getTextColors(Context context, TypedArray attrs) {
        int ap;
        if (attrs == null) {
            throw new NullPointerException();
        }
        TypedArray a = context.obtainStyledAttributes(R.styleable.TextView);
        ColorStateList colors = a.getColorStateList(5);
        if (colors == null && (ap = a.getResourceId(1, 0)) != 0) {
            TypedArray appearance = context.obtainStyledAttributes(ap, R.styleable.TextAppearance);
            colors = appearance.getColorStateList(3);
            appearance.recycle();
        }
        a.recycle();
        return colors;
    }

    private protected static int getTextColor(Context context, TypedArray attrs, int def) {
        ColorStateList colors = getTextColors(context, attrs);
        if (colors == null) {
            return def;
        }
        return colors.getDefaultColor();
    }

    @Override // android.view.View
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        if (event.hasModifiers(4096)) {
            if (keyCode != 29) {
                if (keyCode != 31) {
                    if (keyCode != 50) {
                        if (keyCode != 52) {
                            if (keyCode == 54 && canUndo()) {
                                return onTextContextMenuItem(16908338);
                            }
                        } else if (canCut()) {
                            return onTextContextMenuItem(16908320);
                        }
                    } else if (canPaste()) {
                        return onTextContextMenuItem(16908322);
                    }
                } else if (canCopy()) {
                    return onTextContextMenuItem(16908321);
                }
            } else if (canSelectText()) {
                return onTextContextMenuItem(16908319);
            }
        } else if (event.hasModifiers(4097)) {
            if (keyCode != 50) {
                if (keyCode == 54 && canRedo()) {
                    return onTextContextMenuItem(16908339);
                }
            } else if (canPaste()) {
                return onTextContextMenuItem(16908337);
            }
        }
        return super.onKeyShortcut(keyCode, event);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean canSelectText() {
        return (this.mText.length() == 0 || this.mEditor == null || !this.mEditor.hasSelectionController()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean textCanBeSelected() {
        if (this.mMovement == null || !this.mMovement.canSelectArbitrarily()) {
            return false;
        }
        return isTextEditable() || (isTextSelectable() && (this.mText instanceof Spannable) && isEnabled());
    }

    public protected Locale getTextServicesLocale(boolean allowNullLocale) {
        updateTextServicesLocaleAsync();
        return (this.mCurrentSpellCheckerLocaleCache != null || allowNullLocale) ? this.mCurrentSpellCheckerLocaleCache : Locale.getDefault();
    }

    public synchronized Locale getTextServicesLocale() {
        return getTextServicesLocale(false);
    }

    public synchronized boolean isInExtractedMode() {
        return false;
    }

    private synchronized boolean isAutoSizeEnabled() {
        return supportsAutoSizeText() && this.mAutoSizeTextType != 0;
    }

    protected synchronized boolean supportsAutoSizeText() {
        return true;
    }

    public synchronized Locale getSpellCheckerLocale() {
        return getTextServicesLocale(true);
    }

    private synchronized void updateTextServicesLocaleAsync() {
        AsyncTask.execute(new Runnable() { // from class: android.widget.TextView.3
            @Override // java.lang.Runnable
            public void run() {
                TextView.this.updateTextServicesLocaleLocked();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: public */
    public void updateTextServicesLocaleLocked() {
        Locale locale;
        TextServicesManager textServicesManager = (TextServicesManager) this.mContext.getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE);
        SpellCheckerSubtype subtype = textServicesManager.getCurrentSpellCheckerSubtype(true);
        if (subtype != null) {
            locale = subtype.getLocaleObject();
        } else {
            locale = null;
        }
        this.mCurrentSpellCheckerLocaleCache = locale;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onLocaleChanged() {
        this.mEditor.onLocaleChanged();
    }

    public synchronized WordIterator getWordIterator() {
        if (this.mEditor != null) {
            return this.mEditor.getWordIterator();
        }
        return null;
    }

    @Override // android.view.View
    public synchronized void onPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        super.onPopulateAccessibilityEventInternal(event);
        CharSequence text = getTextForAccessibility();
        if (!TextUtils.isEmpty(text)) {
            event.getText().add(text);
        }
    }

    @Override // android.view.View
    public CharSequence getAccessibilityClassName() {
        return TextView.class.getName();
    }

    @Override // android.view.View
    public void onProvideStructure(ViewStructure structure) {
        super.onProvideStructure(structure);
        onProvideAutoStructureForAssistOrAutofill(structure, false);
    }

    @Override // android.view.View
    public void onProvideAutofillStructure(ViewStructure structure, int flags) {
        super.onProvideAutofillStructure(structure, flags);
        onProvideAutoStructureForAssistOrAutofill(structure, true);
    }

    /* JADX WARN: Code restructure failed: missing block: B:56:0x0109, code lost:
        if (r3 >= r7.length()) goto L83;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized void onProvideAutoStructureForAssistOrAutofill(android.view.ViewStructure r26, boolean r27) {
        /*
            Method dump skipped, instructions count: 455
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.TextView.onProvideAutoStructureForAssistOrAutofill(android.view.ViewStructure, boolean):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean canRequestAutofill() {
        AutofillManager afm;
        if (isAutofillable() && (afm = (AutofillManager) this.mContext.getSystemService(AutofillManager.class)) != null) {
            return afm.isEnabled();
        }
        return false;
    }

    private synchronized void requestAutofill() {
        AutofillManager afm = (AutofillManager) this.mContext.getSystemService(AutofillManager.class);
        if (afm != null) {
            afm.requestAutofill(this);
        }
    }

    @Override // android.view.View
    public void autofill(AutofillValue value) {
        if (!value.isText() || !isTextEditable()) {
            Log.w(LOG_TAG, value + " could not be autofilled into " + this);
            return;
        }
        CharSequence autofilledValue = value.getTextValue();
        setText(autofilledValue, this.mBufferType, true, 0);
        CharSequence text = getText();
        if (text instanceof Spannable) {
            Selection.setSelection((Spannable) text, text.length());
        }
    }

    @Override // android.view.View
    public int getAutofillType() {
        return isTextEditable() ? 1 : 0;
    }

    @Override // android.view.View
    public AutofillValue getAutofillValue() {
        if (isTextEditable()) {
            CharSequence text = TextUtils.trimToParcelableSize(getText());
            return AutofillValue.forText(text);
        }
        return null;
    }

    public synchronized void onInitializeAccessibilityEventInternal(AccessibilityEvent event) {
        super.onInitializeAccessibilityEventInternal(event);
        boolean isPassword = hasPasswordTransformationMethod();
        event.setPassword(isPassword);
        if (event.getEventType() == 8192) {
            event.setFromIndex(Selection.getSelectionStart(this.mText));
            event.setToIndex(Selection.getSelectionEnd(this.mText));
            event.setItemCount(this.mText.length());
        }
    }

    @Override // android.view.View
    public synchronized void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        boolean isPassword = hasPasswordTransformationMethod();
        info.setPassword(isPassword);
        info.setText(getTextForAccessibility());
        info.setHintText(this.mHint);
        info.setShowingHintText(isShowingHint());
        if (this.mBufferType == BufferType.EDITABLE) {
            info.setEditable(true);
            if (isEnabled()) {
                info.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_TEXT);
            }
        }
        if (this.mEditor != null) {
            info.setInputType(this.mEditor.mInputType);
            if (this.mEditor.mError != null) {
                info.setContentInvalid(true);
                info.setError(this.mEditor.mError);
            }
        }
        if (!TextUtils.isEmpty(this.mText)) {
            info.addAction(256);
            info.addAction(512);
            info.setMovementGranularities(31);
            info.addAction(131072);
            info.setAvailableExtraData(Arrays.asList(AccessibilityNodeInfo.EXTRA_DATA_TEXT_CHARACTER_LOCATION_KEY));
        }
        if (isFocused()) {
            if (canCopy()) {
                info.addAction(16384);
            }
            if (canPaste()) {
                info.addAction(32768);
            }
            if (canCut()) {
                info.addAction(65536);
            }
            if (canShare()) {
                info.addAction(new AccessibilityNodeInfo.AccessibilityAction(268435456, getResources().getString(17040865)));
            }
            if (canProcessText()) {
                this.mEditor.mProcessTextIntentActionsHandler.onInitializeAccessibilityNodeInfo(info);
            }
        }
        int numFilters = this.mFilters.length;
        for (int i = 0; i < numFilters; i++) {
            InputFilter filter = this.mFilters[i];
            if (filter instanceof InputFilter.LengthFilter) {
                info.setMaxTextLength(((InputFilter.LengthFilter) filter).getMax());
            }
        }
        if (!isSingleLine()) {
            info.setMultiLine(true);
        }
    }

    @Override // android.view.View
    public void addExtraDataToAccessibilityNodeInfo(AccessibilityNodeInfo info, String extraDataKey, Bundle arguments) {
        RectF bounds;
        if (arguments != null && extraDataKey.equals(AccessibilityNodeInfo.EXTRA_DATA_TEXT_CHARACTER_LOCATION_KEY)) {
            int positionInfoStartIndex = arguments.getInt(AccessibilityNodeInfo.EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_START_INDEX, -1);
            int positionInfoLength = arguments.getInt(AccessibilityNodeInfo.EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_LENGTH, -1);
            if (positionInfoLength <= 0 || positionInfoStartIndex < 0 || positionInfoStartIndex >= this.mText.length()) {
                Log.e(LOG_TAG, "Invalid arguments for accessibility character locations");
                return;
            }
            RectF[] boundingRects = new RectF[positionInfoLength];
            CursorAnchorInfo.Builder builder = new CursorAnchorInfo.Builder();
            populateCharacterBounds(builder, positionInfoStartIndex, positionInfoStartIndex + positionInfoLength, viewportToContentHorizontalOffset(), viewportToContentVerticalOffset());
            CursorAnchorInfo cursorAnchorInfo = builder.setMatrix(null).build();
            for (int i = 0; i < positionInfoLength; i++) {
                int flags = cursorAnchorInfo.getCharacterBoundsFlags(positionInfoStartIndex + i);
                if ((flags & 1) == 1 && (bounds = cursorAnchorInfo.getCharacterBounds(positionInfoStartIndex + i)) != null) {
                    mapRectFromViewToScreenCoords(bounds, true);
                    boundingRects[i] = bounds;
                }
            }
            info.getExtras().putParcelableArray(extraDataKey, boundingRects);
        }
    }

    public synchronized void populateCharacterBounds(CursorAnchorInfo.Builder builder, int startIndex, int endIndex, float viewportToContentHorizontalOffset, float viewportToContentVerticalOffset) {
        float left;
        float right;
        int i = startIndex;
        int i2 = endIndex;
        int minLine = this.mLayout.getLineForOffset(i);
        int maxLine = this.mLayout.getLineForOffset(i2 - 1);
        int line = minLine;
        while (line <= maxLine) {
            int lineStart = this.mLayout.getLineStart(line);
            int lineEnd = this.mLayout.getLineEnd(line);
            int offsetStart = Math.max(lineStart, i);
            int offsetEnd = Math.min(lineEnd, i2);
            boolean ltrLine = this.mLayout.getParagraphDirection(line) == 1;
            float[] widths = new float[offsetEnd - offsetStart];
            this.mLayout.getPaint().getTextWidths(this.mTransformed, offsetStart, offsetEnd, widths);
            float top = this.mLayout.getLineTop(line);
            float bottom = this.mLayout.getLineBottom(line);
            int offset = offsetStart;
            while (true) {
                int offset2 = offset;
                if (offset2 < offsetEnd) {
                    float charWidth = widths[offset2 - offsetStart];
                    boolean isRtl = this.mLayout.isRtlCharAt(offset2);
                    int minLine2 = minLine;
                    float primary = this.mLayout.getPrimaryHorizontal(offset2);
                    int maxLine2 = maxLine;
                    float secondary = this.mLayout.getSecondaryHorizontal(offset2);
                    if (ltrLine) {
                        if (isRtl) {
                            left = secondary - charWidth;
                            right = secondary;
                        } else {
                            left = primary;
                            right = primary + charWidth;
                        }
                    } else if (!isRtl) {
                        left = secondary;
                        right = secondary + charWidth;
                    } else {
                        left = primary - charWidth;
                        right = primary;
                    }
                    float primary2 = left + viewportToContentHorizontalOffset;
                    float secondary2 = right + viewportToContentHorizontalOffset;
                    float localTop = top + viewportToContentVerticalOffset;
                    int lineStart2 = lineStart;
                    float localBottom = bottom + viewportToContentVerticalOffset;
                    boolean isTopLeftVisible = isPositionVisible(primary2, localTop);
                    boolean isBottomRightVisible = isPositionVisible(secondary2, localBottom);
                    int characterBoundsFlags = 0;
                    if (isTopLeftVisible || isBottomRightVisible) {
                        characterBoundsFlags = 0 | 1;
                    }
                    if (!isTopLeftVisible || !isBottomRightVisible) {
                        characterBoundsFlags |= 2;
                    }
                    if (isRtl) {
                        characterBoundsFlags |= 4;
                    }
                    builder.addCharacterBounds(offset2, primary2, localTop, secondary2, localBottom, characterBoundsFlags);
                    offset = offset2 + 1;
                    minLine = minLine2;
                    maxLine = maxLine2;
                    lineStart = lineStart2;
                }
            }
            line++;
            i = startIndex;
            i2 = endIndex;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public synchronized boolean isPositionVisible(float positionX, float positionY) {
        synchronized (TEMP_POSITION) {
            float[] position = TEMP_POSITION;
            position[0] = positionX;
            position[1] = positionY;
            View view = this;
            while (view != null) {
                if (view != this) {
                    position[0] = position[0] - view.getScrollX();
                    position[1] = position[1] - view.getScrollY();
                }
                if (position[0] >= 0.0f && position[1] >= 0.0f && position[0] <= view.getWidth() && position[1] <= view.getHeight()) {
                    if (!view.getMatrix().isIdentity()) {
                        view.getMatrix().mapPoints(position);
                    }
                    position[0] = position[0] + view.getLeft();
                    position[1] = position[1] + view.getTop();
                    ViewParent parent = view.getParent();
                    if (parent instanceof View) {
                        view = (View) parent;
                    } else {
                        view = null;
                    }
                }
                return false;
            }
            return true;
        }
    }

    public synchronized boolean performAccessibilityActionInternal(int action, Bundle arguments) {
        int start;
        int end;
        int updatedTextLength;
        if (this.mEditor == null || !this.mEditor.mProcessTextIntentActionsHandler.performAccessibilityAction(action)) {
            if (action != 16) {
                if (action == 256 || action == 512) {
                    ensureIterableTextForAccessibilitySelectable();
                    return super.performAccessibilityActionInternal(action, arguments);
                } else if (action == 16384) {
                    return isFocused() && canCopy() && onTextContextMenuItem(16908321);
                } else if (action == 32768) {
                    return isFocused() && canPaste() && onTextContextMenuItem(16908322);
                } else if (action == 65536) {
                    return isFocused() && canCut() && onTextContextMenuItem(16908320);
                } else if (action != 131072) {
                    if (action != 2097152) {
                        if (action != 268435456) {
                            return super.performAccessibilityActionInternal(action, arguments);
                        }
                        return isFocused() && canShare() && onTextContextMenuItem(16908341);
                    } else if (isEnabled() && this.mBufferType == BufferType.EDITABLE) {
                        setText(arguments != null ? arguments.getCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE) : null);
                        if (this.mText != null && (updatedTextLength = this.mText.length()) > 0) {
                            Selection.setSelection(this.mSpannable, updatedTextLength);
                        }
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    ensureIterableTextForAccessibilitySelectable();
                    CharSequence text = getIterableTextForAccessibility();
                    if (text == null) {
                        return false;
                    }
                    if (arguments != null) {
                        start = arguments.getInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_START_INT, -1);
                    } else {
                        start = -1;
                    }
                    if (arguments != null) {
                        end = arguments.getInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_END_INT, -1);
                    } else {
                        end = -1;
                    }
                    if (getSelectionStart() != start || getSelectionEnd() != end) {
                        if (start == end && end == -1) {
                            Selection.removeSelection((Spannable) text);
                            return true;
                        } else if (start >= 0 && start <= end && end <= text.length()) {
                            Selection.setSelection((Spannable) text, start, end);
                            if (this.mEditor != null) {
                                this.mEditor.startSelectionActionModeAsync(false);
                            }
                            return true;
                        }
                    }
                    return false;
                }
            }
            return performAccessibilityActionClick(arguments);
        }
        return true;
    }

    private synchronized boolean performAccessibilityActionClick(Bundle arguments) {
        boolean handled = false;
        if (isEnabled()) {
            if (isClickable() || isLongClickable()) {
                if (isFocusable() && !isFocused()) {
                    requestFocus();
                }
                performClick();
                handled = true;
            }
            if ((this.mMovement != null || onCheckIsTextEditor()) && hasSpannableText() && this.mLayout != null) {
                if ((isTextEditable() || isTextSelectable()) && isFocused()) {
                    InputMethodManager imm = InputMethodManager.peekInstance();
                    viewClicked(imm);
                    if (!isTextSelectable() && this.mEditor.mShowSoftInputOnFocus && imm != null) {
                        return handled | imm.showSoftInput(this, 0);
                    }
                    return handled;
                }
                return handled;
            }
            return handled;
        }
        return false;
    }

    private synchronized boolean hasSpannableText() {
        return this.mText != null && (this.mText instanceof Spannable);
    }

    @Override // android.view.View
    public synchronized void sendAccessibilityEventInternal(int eventType) {
        if (eventType == 32768 && this.mEditor != null) {
            this.mEditor.mProcessTextIntentActionsHandler.initializeAccessibilityActions();
        }
        super.sendAccessibilityEventInternal(eventType);
    }

    @Override // android.view.View, android.view.accessibility.AccessibilityEventSource
    public void sendAccessibilityEventUnchecked(AccessibilityEvent event) {
        if (event.getEventType() == 4096) {
            return;
        }
        super.sendAccessibilityEventUnchecked(event);
    }

    public protected CharSequence getTextForAccessibility() {
        if (TextUtils.isEmpty(this.mText)) {
            return this.mHint;
        }
        return TextUtils.trimToParcelableSize(this.mTransformed);
    }

    synchronized void sendAccessibilityEventTypeViewTextChanged(CharSequence beforeText, int fromIndex, int removedCount, int addedCount) {
        AccessibilityEvent event = AccessibilityEvent.obtain(16);
        event.setFromIndex(fromIndex);
        event.setRemovedCount(removedCount);
        event.setAddedCount(addedCount);
        event.setBeforeText(beforeText);
        sendAccessibilityEventUnchecked(event);
    }

    public boolean isInputMethodTarget() {
        InputMethodManager imm = InputMethodManager.peekInstance();
        return imm != null && imm.isActive(this);
    }

    public boolean onTextContextMenuItem(int id) {
        int min = 0;
        int max = this.mText.length();
        if (isFocused()) {
            int selStart = getSelectionStart();
            int selEnd = getSelectionEnd();
            min = Math.max(0, Math.min(selStart, selEnd));
            max = Math.max(0, Math.max(selStart, selEnd));
        }
        if (id != 16908355) {
            switch (id) {
                case 16908319:
                    boolean hadSelection = hasSelection();
                    selectAllText();
                    if (this.mEditor != null && hadSelection) {
                        this.mEditor.invalidateActionModeAsync();
                    }
                    return true;
                case 16908320:
                    ClipData cutData = ClipData.newPlainText(null, getTransformedText(min, max));
                    if (setPrimaryClip(cutData)) {
                        deleteText_internal(min, max);
                    } else {
                        Toast.makeText(getContext(), (int) com.android.internal.R.string.failed_to_copy_to_clipboard, 0).show();
                    }
                    return true;
                case 16908321:
                    int selStart2 = getSelectionStart();
                    int selEnd2 = getSelectionEnd();
                    int min2 = Math.max(0, Math.min(selStart2, selEnd2));
                    int max2 = Math.max(0, Math.max(selStart2, selEnd2));
                    ClipData copyData = ClipData.newPlainText(null, getTransformedText(min2, max2));
                    if (setPrimaryClip(copyData)) {
                        stopTextActionMode();
                    } else {
                        Toast.makeText(getContext(), (int) com.android.internal.R.string.failed_to_copy_to_clipboard, 0).show();
                    }
                    return true;
                case 16908322:
                    paste(min, max, true);
                    return true;
                default:
                    switch (id) {
                        case 16908337:
                            paste(min, max, false);
                            return true;
                        case 16908338:
                            if (this.mEditor != null) {
                                this.mEditor.undo();
                            }
                            return true;
                        case 16908339:
                            if (this.mEditor != null) {
                                this.mEditor.redo();
                            }
                            return true;
                        case 16908340:
                            if (this.mEditor != null) {
                                this.mEditor.replace();
                            }
                            return true;
                        case 16908341:
                            shareSelectedText();
                            return true;
                        default:
                            return false;
                    }
            }
        }
        requestAutofill();
        stopTextActionMode();
        return true;
    }

    public private protected CharSequence getTransformedText(int start, int end) {
        return removeSuggestionSpans(this.mTransformed.subSequence(start, end));
    }

    @Override // android.view.View
    public boolean performLongClick() {
        boolean handled = false;
        boolean performedHapticFeedback = false;
        if (this.mEditor != null) {
            this.mEditor.mIsBeingLongClicked = true;
        }
        if (super.performLongClick()) {
            handled = true;
            performedHapticFeedback = true;
        }
        if (this.mEditor != null) {
            handled |= this.mEditor.performLongClick(handled);
            this.mEditor.mIsBeingLongClicked = false;
        }
        if (handled) {
            if (!performedHapticFeedback) {
                performHapticFeedback(0);
            }
            if (this.mEditor != null) {
                this.mEditor.mDiscardNextActionUp = true;
            }
        } else {
            MetricsLogger.action(this.mContext, (int) MetricsProto.MetricsEvent.TEXT_LONGPRESS, 0);
        }
        return handled;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onScrollChanged(int horiz, int vert, int oldHoriz, int oldVert) {
        super.onScrollChanged(horiz, vert, oldHoriz, oldVert);
        if (this.mEditor != null) {
            this.mEditor.onScrollChanged();
        }
    }

    public boolean isSuggestionsEnabled() {
        if (this.mEditor != null && (this.mEditor.mInputType & 15) == 1 && (this.mEditor.mInputType & 524288) <= 0) {
            int variation = this.mEditor.mInputType & InputType.TYPE_MASK_VARIATION;
            return variation == 0 || variation == 48 || variation == 80 || variation == 64 || variation == 160;
        }
        return false;
    }

    public void setCustomSelectionActionModeCallback(ActionMode.Callback actionModeCallback) {
        createEditorIfNeeded();
        this.mEditor.mCustomSelectionActionModeCallback = actionModeCallback;
    }

    public ActionMode.Callback getCustomSelectionActionModeCallback() {
        if (this.mEditor == null) {
            return null;
        }
        return this.mEditor.mCustomSelectionActionModeCallback;
    }

    public void setCustomInsertionActionModeCallback(ActionMode.Callback actionModeCallback) {
        createEditorIfNeeded();
        this.mEditor.mCustomInsertionActionModeCallback = actionModeCallback;
    }

    public ActionMode.Callback getCustomInsertionActionModeCallback() {
        if (this.mEditor == null) {
            return null;
        }
        return this.mEditor.mCustomInsertionActionModeCallback;
    }

    public void setTextClassifier(TextClassifier textClassifier) {
        this.mTextClassifier = textClassifier;
    }

    public TextClassifier getTextClassifier() {
        if (this.mTextClassifier == null) {
            TextClassificationManager tcm = (TextClassificationManager) this.mContext.getSystemService(TextClassificationManager.class);
            if (tcm != null) {
                return tcm.getTextClassifier();
            }
            return TextClassifier.NO_OP;
        }
        return this.mTextClassifier;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized TextClassifier getTextClassificationSession() {
        String widgetType;
        if (this.mTextClassificationSession == null || this.mTextClassificationSession.isDestroyed()) {
            TextClassificationManager tcm = (TextClassificationManager) this.mContext.getSystemService(TextClassificationManager.class);
            if (tcm != null) {
                if (isTextEditable()) {
                    widgetType = TextClassifier.WIDGET_TYPE_EDITTEXT;
                } else if (isTextSelectable()) {
                    widgetType = TextClassifier.WIDGET_TYPE_TEXTVIEW;
                } else {
                    widgetType = TextClassifier.WIDGET_TYPE_UNSELECTABLE_TEXTVIEW;
                }
                TextClassificationContext textClassificationContext = new TextClassificationContext.Builder(this.mContext.getPackageName(), widgetType).build();
                if (this.mTextClassifier != null) {
                    this.mTextClassificationSession = tcm.createTextClassificationSession(textClassificationContext, this.mTextClassifier);
                } else {
                    this.mTextClassificationSession = tcm.createTextClassificationSession(textClassificationContext);
                }
            } else {
                this.mTextClassificationSession = TextClassifier.NO_OP;
            }
        }
        return this.mTextClassificationSession;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean usesNoOpTextClassifier() {
        return getTextClassifier() == TextClassifier.NO_OP;
    }

    public synchronized boolean requestActionMode(TextLinks.TextLinkSpan clickedSpan) {
        Preconditions.checkNotNull(clickedSpan);
        if (this.mText instanceof Spanned) {
            int start = ((Spanned) this.mText).getSpanStart(clickedSpan);
            int end = ((Spanned) this.mText).getSpanEnd(clickedSpan);
            if (start < 0 || end > this.mText.length() || start >= end) {
                return false;
            }
            createEditorIfNeeded();
            this.mEditor.startLinkActionModeAsync(start, end);
            return true;
        }
        return false;
    }

    public synchronized boolean handleClick(TextLinks.TextLinkSpan clickedSpan) {
        Preconditions.checkNotNull(clickedSpan);
        if (this.mText instanceof Spanned) {
            Spanned spanned = (Spanned) this.mText;
            int start = spanned.getSpanStart(clickedSpan);
            int end = spanned.getSpanEnd(clickedSpan);
            if (start >= 0 && end <= this.mText.length() && start < end) {
                final TextClassification.Request request = new TextClassification.Request.Builder(this.mText, start, end).setDefaultLocales(getTextLocales()).build();
                Supplier<TextClassification> supplier = new Supplier() { // from class: android.widget.-$$Lambda$TextView$DJlzb7VS7J_1890Kto7GAApQDN0
                    @Override // java.util.function.Supplier
                    public final Object get() {
                        TextClassification classifyText;
                        classifyText = TextView.this.getTextClassifier().classifyText(request);
                        return classifyText;
                    }
                };
                CompletableFuture.supplyAsync(supplier).completeOnTimeout(null, 1L, TimeUnit.SECONDS).thenAccept((Consumer) new Consumer() { // from class: android.widget.-$$Lambda$TextView$jQz3_DIfGrNeNdu_95_wi6UkW4E
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        TextView.lambda$handleClick$1((TextClassification) obj);
                    }
                });
                return true;
            }
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$handleClick$1(TextClassification classification) {
        if (classification != null) {
            if (!classification.getActions().isEmpty()) {
                try {
                    classification.getActions().get(0).getActionIntent().send();
                    return;
                } catch (PendingIntent.CanceledException e) {
                    Log.e(LOG_TAG, "Error sending PendingIntent", e);
                    return;
                }
            }
            Log.d(LOG_TAG, "No link action to perform");
            return;
        }
        Log.d(LOG_TAG, "Timeout while classifying text");
    }

    public private void stopTextActionMode() {
        if (this.mEditor != null) {
            this.mEditor.stopTextActionMode();
        }
    }

    public synchronized void hideFloatingToolbar(int durationMs) {
        if (this.mEditor != null) {
            this.mEditor.hideFloatingToolbar(durationMs);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean canUndo() {
        return this.mEditor != null && this.mEditor.canUndo();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean canRedo() {
        return this.mEditor != null && this.mEditor.canRedo();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean canCut() {
        return !hasPasswordTransformationMethod() && this.mText.length() > 0 && hasSelection() && (this.mText instanceof Editable) && this.mEditor != null && this.mEditor.mKeyListener != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean canCopy() {
        return !hasPasswordTransformationMethod() && this.mText.length() > 0 && hasSelection() && this.mEditor != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean canShare() {
        if (!getContext().canStartActivityForResult() || !isDeviceProvisioned()) {
            return false;
        }
        return canCopy();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean isDeviceProvisioned() {
        if (this.mDeviceProvisionedState == 0) {
            this.mDeviceProvisionedState = Settings.Global.getInt(this.mContext.getContentResolver(), "device_provisioned", 0) != 0 ? 2 : 1;
        }
        return this.mDeviceProvisionedState == 2;
    }

    public private protected boolean canPaste() {
        return (this.mText instanceof Editable) && this.mEditor != null && this.mEditor.mKeyListener != null && getSelectionStart() >= 0 && getSelectionEnd() >= 0 && ((ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE)).hasPrimaryClip();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean canPasteAsPlainText() {
        if (canPaste()) {
            ClipData clipData = ((ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE)).getPrimaryClip();
            ClipDescription description = clipData.getDescription();
            boolean isPlainType = description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);
            CharSequence text = clipData.getItemAt(0).getText();
            if (isPlainType && (text instanceof Spanned)) {
                Spanned spanned = (Spanned) text;
                if (TextUtils.hasStyleSpan(spanned)) {
                    return true;
                }
            }
            return description.hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean canProcessText() {
        if (getId() == -1) {
            return false;
        }
        return canShare();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean canSelectAllText() {
        return (!canSelectText() || hasPasswordTransformationMethod() || (getSelectionStart() == 0 && getSelectionEnd() == this.mText.length())) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean selectAllText() {
        if (this.mEditor != null) {
            hideFloatingToolbar(500);
        }
        int length = this.mText.length();
        Selection.setSelection(this.mSpannable, 0, length);
        return length > 0;
    }

    synchronized void replaceSelectionWithText(CharSequence text) {
        ((Editable) this.mText).replace(getSelectionStart(), getSelectionEnd(), text);
    }

    private synchronized void paste(int min, int max, boolean withFormatting) {
        CharSequence paste;
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null) {
            boolean didFirst = false;
            for (int i = 0; i < clip.getItemCount(); i++) {
                if (withFormatting) {
                    paste = clip.getItemAt(i).coerceToStyledText(getContext());
                } else {
                    CharSequence text = clip.getItemAt(i).coerceToText(getContext());
                    paste = text instanceof Spanned ? text.toString() : text;
                }
                if (paste != null) {
                    if (!didFirst) {
                        Selection.setSelection(this.mSpannable, max);
                        ((Editable) this.mText).replace(min, max, paste);
                        didFirst = true;
                    } else {
                        ((Editable) this.mText).insert(getSelectionEnd(), "\n");
                        ((Editable) this.mText).insert(getSelectionEnd(), paste);
                    }
                }
            }
            sLastCutCopyOrTextChangedTime = 0L;
        }
    }

    private synchronized void shareSelectedText() {
        String selectedText = getSelectedText();
        if (selectedText != null && !selectedText.isEmpty()) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType(ClipDescription.MIMETYPE_TEXT_PLAIN);
            sharingIntent.removeExtra(Intent.EXTRA_TEXT);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, (String) TextUtils.trimToParcelableSize(selectedText));
            getContext().startActivity(Intent.createChooser(sharingIntent, null));
            Selection.setSelection(this.mSpannable, getSelectionEnd());
        }
    }

    private synchronized boolean setPrimaryClip(ClipData clip) {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        try {
            clipboard.setPrimaryClip(clip);
            sLastCutCopyOrTextChangedTime = SystemClock.uptimeMillis();
            return true;
        } catch (Throwable th) {
            return false;
        }
    }

    public int getOffsetForPosition(float x, float y) {
        if (getLayout() == null) {
            return -1;
        }
        int line = getLineAtCoordinate(y);
        int offset = getOffsetAtCoordinate(line, x);
        return offset;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized float convertToLocalHorizontalCoordinate(float x) {
        return Math.min((getWidth() - getTotalPaddingRight()) - 1, Math.max(0.0f, x - getTotalPaddingLeft())) + getScrollX();
    }

    public private protected int getLineAtCoordinate(float y) {
        return getLayout().getLineForVertical((int) (Math.min((getHeight() - getTotalPaddingBottom()) - 1, Math.max(0.0f, y - getTotalPaddingTop())) + getScrollY()));
    }

    synchronized int getLineAtCoordinateUnclamped(float y) {
        return getLayout().getLineForVertical((int) ((y - getTotalPaddingTop()) + getScrollY()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int getOffsetAtCoordinate(int line, float x) {
        return getLayout().getOffsetForHorizontal(line, convertToLocalHorizontalCoordinate(x));
    }

    @Override // android.view.View
    public boolean onDragEvent(DragEvent event) {
        int action = event.getAction();
        if (action == 5) {
            requestFocus();
            return true;
        }
        switch (action) {
            case 1:
                return this.mEditor != null && this.mEditor.hasInsertionController();
            case 2:
                if (this.mText instanceof Spannable) {
                    int offset = getOffsetForPosition(event.getX(), event.getY());
                    Selection.setSelection(this.mSpannable, offset);
                }
                return true;
            case 3:
                if (this.mEditor != null) {
                    this.mEditor.onDrop(event);
                }
                return true;
            default:
                return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean isInBatchEditMode() {
        if (this.mEditor == null) {
            return false;
        }
        Editor.InputMethodState ims = this.mEditor.mInputMethodState;
        if (ims != null) {
            return ims.mBatchEditNesting > 0;
        }
        return this.mEditor.mInBatchEditControllers;
    }

    @Override // android.view.View
    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        TextDirectionHeuristic newTextDir = getTextDirectionHeuristic();
        if (this.mTextDir != newTextDir) {
            this.mTextDir = newTextDir;
            if (this.mLayout != null) {
                checkForRelayout();
            }
        }
    }

    public private TextDirectionHeuristic getTextDirectionHeuristic() {
        if (hasPasswordTransformationMethod()) {
            return TextDirectionHeuristics.LTR;
        }
        if (this.mEditor != null && (this.mEditor.mInputType & 15) == 3) {
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(getTextLocale());
            String zero = symbols.getDigitStrings()[0];
            int firstCodepoint = zero.codePointAt(0);
            byte digitDirection = Character.getDirectionality(firstCodepoint);
            if (digitDirection == 1 || digitDirection == 2) {
                return TextDirectionHeuristics.RTL;
            }
            return TextDirectionHeuristics.LTR;
        }
        boolean defaultIsRtl = getLayoutDirection() == 1;
        switch (getTextDirection()) {
            case 2:
                return TextDirectionHeuristics.ANYRTL_LTR;
            case 3:
                return TextDirectionHeuristics.LTR;
            case 4:
                return TextDirectionHeuristics.RTL;
            case 5:
                return TextDirectionHeuristics.LOCALE;
            case 6:
                return TextDirectionHeuristics.FIRSTSTRONG_LTR;
            case 7:
                return TextDirectionHeuristics.FIRSTSTRONG_RTL;
            default:
                return defaultIsRtl ? TextDirectionHeuristics.FIRSTSTRONG_RTL : TextDirectionHeuristics.FIRSTSTRONG_LTR;
        }
    }

    @Override // android.view.View
    public synchronized void onResolveDrawables(int layoutDirection) {
        if (this.mLastLayoutDirection == layoutDirection) {
            return;
        }
        this.mLastLayoutDirection = layoutDirection;
        if (this.mDrawables != null && this.mDrawables.resolveWithLayoutDirection(layoutDirection)) {
            prepareDrawableForDisplay(this.mDrawables.mShowing[0]);
            prepareDrawableForDisplay(this.mDrawables.mShowing[2]);
            applyCompoundDrawableTint();
        }
    }

    private synchronized void prepareDrawableForDisplay(Drawable dr) {
        if (dr == null) {
            return;
        }
        dr.setLayoutDirection(getLayoutDirection());
        if (dr.isStateful()) {
            dr.setState(getDrawableState());
            dr.jumpToCurrentState();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void resetResolvedDrawables() {
        super.resetResolvedDrawables();
        this.mLastLayoutDirection = -1;
    }

    protected synchronized void viewClicked(InputMethodManager imm) {
        if (imm != null) {
            imm.viewClicked(this);
        }
    }

    public private void deleteText_internal(int start, int end) {
        ((Editable) this.mText).delete(start, end);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void replaceText_internal(int start, int end, CharSequence text) {
        ((Editable) this.mText).replace(start, end, text);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void setSpan_internal(Object span, int start, int end, int flags) {
        ((Editable) this.mText).setSpan(span, start, end, flags);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void setCursorPosition_internal(int start, int end) {
        Selection.setSelection((Editable) this.mText, start, end);
    }

    public protected void createEditorIfNeeded() {
        if (this.mEditor == null) {
            this.mEditor = new Editor(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CharSequence getIterableTextForAccessibility() {
        return this.mText;
    }

    private synchronized void ensureIterableTextForAccessibilitySelectable() {
        if (!(this.mText instanceof Spannable)) {
            setText(this.mText, BufferType.SPANNABLE);
        }
    }

    public synchronized AccessibilityIterators.TextSegmentIterator getIteratorForGranularity(int granularity) {
        if (granularity == 4) {
            Spannable text = (Spannable) getIterableTextForAccessibility();
            if (!TextUtils.isEmpty(text) && getLayout() != null) {
                AccessibilityIterators.LineTextSegmentIterator iterator = AccessibilityIterators.LineTextSegmentIterator.getInstance();
                iterator.initialize(text, getLayout());
                return iterator;
            }
        } else if (granularity == 16 && !TextUtils.isEmpty((Spannable) getIterableTextForAccessibility()) && getLayout() != null) {
            AccessibilityIterators.PageTextSegmentIterator iterator2 = AccessibilityIterators.PageTextSegmentIterator.getInstance();
            iterator2.initialize(this);
            return iterator2;
        }
        return super.getIteratorForGranularity(granularity);
    }

    @Override // android.view.View
    public synchronized int getAccessibilitySelectionStart() {
        return getSelectionStart();
    }

    @Override // android.view.View
    public synchronized boolean isAccessibilitySelectionExtendable() {
        return true;
    }

    @Override // android.view.View
    public synchronized int getAccessibilitySelectionEnd() {
        return getSelectionEnd();
    }

    @Override // android.view.View
    public synchronized void setAccessibilitySelection(int start, int end) {
        if (getAccessibilitySelectionStart() == start && getAccessibilitySelectionEnd() == end) {
            return;
        }
        CharSequence text = getIterableTextForAccessibility();
        if (Math.min(start, end) >= 0 && Math.max(start, end) <= text.length()) {
            Selection.setSelection((Spannable) text, start, end);
        } else {
            Selection.removeSelection((Spannable) text);
        }
        if (this.mEditor != null) {
            this.mEditor.hideCursorAndSpanControllers();
            this.mEditor.stopTextActionMode();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public synchronized void encodeProperties(ViewHierarchyEncoder stream) {
        super.encodeProperties(stream);
        TextUtils.TruncateAt ellipsize = getEllipsize();
        stream.addProperty("text:ellipsize", ellipsize == null ? null : ellipsize.name());
        stream.addProperty("text:textSize", getTextSize());
        stream.addProperty("text:scaledTextSize", getScaledTextSize());
        stream.addProperty("text:typefaceStyle", getTypefaceStyle());
        stream.addProperty("text:selectionStart", getSelectionStart());
        stream.addProperty("text:selectionEnd", getSelectionEnd());
        stream.addProperty("text:curTextColor", this.mCurTextColor);
        stream.addProperty("text:text", this.mText != null ? this.mText.toString() : null);
        stream.addProperty("text:gravity", this.mGravity);
    }

    /* loaded from: classes3.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: android.widget.TextView.SavedState.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        ParcelableParcel editorState;
        CharSequence error;
        boolean frozenWithFocus;
        int selEnd;
        int selStart;
        public private protected CharSequence text;

        synchronized SavedState(Parcelable superState) {
            super(superState);
            this.selStart = -1;
            this.selEnd = -1;
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.selStart);
            out.writeInt(this.selEnd);
            out.writeInt(this.frozenWithFocus ? 1 : 0);
            TextUtils.writeToParcel(this.text, out, flags);
            if (this.error == null) {
                out.writeInt(0);
            } else {
                out.writeInt(1);
                TextUtils.writeToParcel(this.error, out, flags);
            }
            if (this.editorState == null) {
                out.writeInt(0);
                return;
            }
            out.writeInt(1);
            this.editorState.writeToParcel(out, flags);
        }

        public String toString() {
            String str = "TextView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " start=" + this.selStart + " end=" + this.selEnd;
            if (this.text != null) {
                str = str + " text=" + ((Object) this.text);
            }
            return str + "}";
        }

        private synchronized SavedState(Parcel in) {
            super(in);
            this.selStart = -1;
            this.selEnd = -1;
            this.selStart = in.readInt();
            this.selEnd = in.readInt();
            this.frozenWithFocus = in.readInt() != 0;
            this.text = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            if (in.readInt() != 0) {
                this.error = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            }
            if (in.readInt() != 0) {
                this.editorState = ParcelableParcel.CREATOR.createFromParcel(in);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class CharWrapper implements CharSequence, GetChars, GraphicsOperations {
        private char[] mChars;
        private int mLength;
        private int mStart;

        public synchronized CharWrapper(char[] chars, int start, int len) {
            this.mChars = chars;
            this.mStart = start;
            this.mLength = len;
        }

        synchronized void set(char[] chars, int start, int len) {
            this.mChars = chars;
            this.mStart = start;
            this.mLength = len;
        }

        @Override // java.lang.CharSequence
        public int length() {
            return this.mLength;
        }

        @Override // java.lang.CharSequence
        public char charAt(int off) {
            return this.mChars[this.mStart + off];
        }

        @Override // java.lang.CharSequence
        public String toString() {
            return new String(this.mChars, this.mStart, this.mLength);
        }

        @Override // java.lang.CharSequence
        public CharSequence subSequence(int start, int end) {
            if (start < 0 || end < 0 || start > this.mLength || end > this.mLength) {
                throw new IndexOutOfBoundsException(start + ", " + end);
            }
            return new String(this.mChars, this.mStart + start, end - start);
        }

        @Override // android.text.GetChars
        public void getChars(int start, int end, char[] buf, int off) {
            if (start < 0 || end < 0 || start > this.mLength || end > this.mLength) {
                throw new IndexOutOfBoundsException(start + ", " + end);
            }
            System.arraycopy(this.mChars, this.mStart + start, buf, off, end - start);
        }

        @Override // android.text.GraphicsOperations
        public synchronized void drawText(BaseCanvas c, int start, int end, float x, float y, Paint p) {
            c.drawText(this.mChars, start + this.mStart, end - start, x, y, p);
        }

        @Override // android.text.GraphicsOperations
        public synchronized void drawTextRun(BaseCanvas c, int start, int end, int contextStart, int contextEnd, float x, float y, boolean isRtl, Paint p) {
            int count = end - start;
            int contextCount = contextEnd - contextStart;
            c.drawTextRun(this.mChars, start + this.mStart, count, contextStart + this.mStart, contextCount, x, y, isRtl, p);
        }

        @Override // android.text.GraphicsOperations
        public synchronized float measureText(int start, int end, Paint p) {
            return p.measureText(this.mChars, this.mStart + start, end - start);
        }

        @Override // android.text.GraphicsOperations
        public synchronized int getTextWidths(int start, int end, float[] widths, Paint p) {
            return p.getTextWidths(this.mChars, this.mStart + start, end - start, widths);
        }

        @Override // android.text.GraphicsOperations
        public synchronized float getTextRunAdvances(int start, int end, int contextStart, int contextEnd, boolean isRtl, float[] advances, int advancesIndex, Paint p) {
            int count = end - start;
            int contextCount = contextEnd - contextStart;
            return p.getTextRunAdvances(this.mChars, start + this.mStart, count, contextStart + this.mStart, contextCount, isRtl, advances, advancesIndex);
        }

        @Override // android.text.GraphicsOperations
        public synchronized int getTextRunCursor(int contextStart, int contextEnd, int dir, int offset, int cursorOpt, Paint p) {
            int contextCount = contextEnd - contextStart;
            return p.getTextRunCursor(this.mChars, contextStart + this.mStart, contextCount, dir, offset + this.mStart, cursorOpt);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class Marquee {
        private static final int MARQUEE_DELAY = 1200;
        private static final float MARQUEE_DELTA_MAX = 0.07f;
        private static final int MARQUEE_DP_PER_SECOND = 30;
        private static final byte MARQUEE_RUNNING = 2;
        private static final byte MARQUEE_STARTING = 1;
        private static final byte MARQUEE_STOPPED = 0;
        private final Choreographer mChoreographer;
        private float mFadeStop;
        private float mGhostOffset;
        private float mGhostStart;
        private long mLastAnimationMs;
        private float mMaxFadeScroll;
        private float mMaxScroll;
        private final float mPixelsPerMs;
        private int mRepeatLimit;
        private float mScroll;
        private final WeakReference<TextView> mView;
        private byte mStatus = 0;
        private Choreographer.FrameCallback mTickCallback = new Choreographer.FrameCallback() { // from class: android.widget.TextView.Marquee.1
            @Override // android.view.Choreographer.FrameCallback
            public void doFrame(long frameTimeNanos) {
                Marquee.this.tick();
            }
        };
        private Choreographer.FrameCallback mStartCallback = new Choreographer.FrameCallback() { // from class: android.widget.TextView.Marquee.2
            @Override // android.view.Choreographer.FrameCallback
            public void doFrame(long frameTimeNanos) {
                Marquee.this.mStatus = (byte) 2;
                Marquee.this.mLastAnimationMs = Marquee.this.mChoreographer.getFrameTime();
                Marquee.this.tick();
            }
        };
        private Choreographer.FrameCallback mRestartCallback = new Choreographer.FrameCallback() { // from class: android.widget.TextView.Marquee.3
            @Override // android.view.Choreographer.FrameCallback
            public void doFrame(long frameTimeNanos) {
                if (Marquee.this.mStatus == 2) {
                    if (Marquee.this.mRepeatLimit >= 0) {
                        Marquee.access$910(Marquee.this);
                    }
                    Marquee.this.start(Marquee.this.mRepeatLimit);
                }
            }
        };

        static /* synthetic */ int access$910(Marquee x0) {
            int i = x0.mRepeatLimit;
            x0.mRepeatLimit = i - 1;
            return i;
        }

        synchronized Marquee(TextView v) {
            float density = v.getContext().getResources().getDisplayMetrics().density;
            this.mPixelsPerMs = (30.0f * density) / 1000.0f;
            this.mView = new WeakReference<>(v);
            this.mChoreographer = Choreographer.getInstance();
        }

        synchronized void tick() {
            if (this.mStatus != 2) {
                return;
            }
            this.mChoreographer.removeFrameCallback(this.mTickCallback);
            TextView textView = this.mView.get();
            if (textView != null) {
                if (textView.isFocused() || textView.isSelected()) {
                    long currentMs = this.mChoreographer.getFrameTime();
                    long deltaMs = currentMs - this.mLastAnimationMs;
                    this.mLastAnimationMs = currentMs;
                    float deltaPx = ((float) deltaMs) * this.mPixelsPerMs;
                    this.mScroll += deltaPx;
                    if (this.mScroll > this.mMaxScroll) {
                        this.mScroll = this.mMaxScroll;
                        this.mChoreographer.postFrameCallbackDelayed(this.mRestartCallback, 1200L);
                    } else {
                        this.mChoreographer.postFrameCallback(this.mTickCallback);
                    }
                    textView.invalidate();
                }
            }
        }

        synchronized void stop() {
            this.mStatus = (byte) 0;
            this.mChoreographer.removeFrameCallback(this.mStartCallback);
            this.mChoreographer.removeFrameCallback(this.mRestartCallback);
            this.mChoreographer.removeFrameCallback(this.mTickCallback);
            resetScroll();
        }

        private synchronized void resetScroll() {
            this.mScroll = 0.0f;
            TextView textView = this.mView.get();
            if (textView != null) {
                textView.invalidate();
            }
        }

        synchronized void start(int repeatLimit) {
            if (repeatLimit == 0) {
                stop();
                return;
            }
            this.mRepeatLimit = repeatLimit;
            TextView textView = this.mView.get();
            if (textView != null && textView.mLayout != null) {
                this.mStatus = (byte) 1;
                this.mScroll = 0.0f;
                int textWidth = (textView.getWidth() - textView.getCompoundPaddingLeft()) - textView.getCompoundPaddingRight();
                float lineWidth = textView.mLayout.getLineWidth(0);
                float gap = textWidth / 3.0f;
                this.mGhostStart = (lineWidth - textWidth) + gap;
                this.mMaxScroll = this.mGhostStart + textWidth;
                this.mGhostOffset = lineWidth + gap;
                this.mFadeStop = (textWidth / 6.0f) + lineWidth;
                this.mMaxFadeScroll = this.mGhostStart + lineWidth + lineWidth;
                textView.invalidate();
                this.mChoreographer.postFrameCallback(this.mStartCallback);
            }
        }

        synchronized float getGhostOffset() {
            return this.mGhostOffset;
        }

        synchronized float getScroll() {
            return this.mScroll;
        }

        synchronized float getMaxFadeScroll() {
            return this.mMaxFadeScroll;
        }

        synchronized boolean shouldDrawLeftFade() {
            return this.mScroll <= this.mFadeStop;
        }

        synchronized boolean shouldDrawGhost() {
            return this.mStatus == 2 && this.mScroll > this.mGhostStart;
        }

        synchronized boolean isRunning() {
            return this.mStatus == 2;
        }

        synchronized boolean isStopped() {
            return this.mStatus == 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class ChangeWatcher implements TextWatcher, SpanWatcher {
        private CharSequence mBeforeText;

        private ChangeWatcher() {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence buffer, int start, int before, int after) {
            if (AccessibilityManager.getInstance(TextView.this.mContext).isEnabled() && TextView.this.mTransformed != null) {
                this.mBeforeText = TextView.this.mTransformed.toString();
            }
            TextView.this.sendBeforeTextChanged(buffer, start, before, after);
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence buffer, int start, int before, int after) {
            TextView.this.handleTextChanged(buffer, start, before, after);
            if (AccessibilityManager.getInstance(TextView.this.mContext).isEnabled()) {
                if (TextView.this.isFocused() || (TextView.this.isSelected() && TextView.this.isShown())) {
                    TextView.this.sendAccessibilityEventTypeViewTextChanged(this.mBeforeText, start, before, after);
                    this.mBeforeText = null;
                }
            }
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable buffer) {
            TextView.this.sendAfterTextChanged(buffer);
            if (MetaKeyKeyListener.getMetaState(buffer, 2048) != 0) {
                MetaKeyKeyListener.stopSelecting(TextView.this, buffer);
            }
        }

        @Override // android.text.SpanWatcher
        public void onSpanChanged(Spannable buf, Object what, int s, int e, int st, int en) {
            TextView.this.spanChange(buf, what, s, st, e, en);
        }

        @Override // android.text.SpanWatcher
        public void onSpanAdded(Spannable buf, Object what, int s, int e) {
            TextView.this.spanChange(buf, what, -1, s, -1, e);
        }

        @Override // android.text.SpanWatcher
        public void onSpanRemoved(Spannable buf, Object what, int s, int e) {
            TextView.this.spanChange(buf, what, s, -1, e, -1);
        }
    }
}
