package android.widget;

import android.app.RemoteAction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.LocaleList;
import android.provider.Telephony;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.EventLog;
import android.util.Log;
import android.view.ActionMode;
import android.view.textclassifier.ExtrasUtils;
import android.view.textclassifier.SelectionEvent;
import android.view.textclassifier.SelectionSessionLogger;
import android.view.textclassifier.TextClassification;
import android.view.textclassifier.TextClassificationConstants;
import android.view.textclassifier.TextClassificationContext;
import android.view.textclassifier.TextClassificationManager;
import android.view.textclassifier.TextClassifier;
import android.view.textclassifier.TextClassifierEvent;
import android.view.textclassifier.TextSelection;
import android.widget.Editor;
import android.widget.SelectionActionModeHelper;
import android.widget.SmartSelectSprite;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

@VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
/* loaded from: classes3.dex */
public final class SelectionActionModeHelper {
    private static final String LOG_TAG = "SelectActionModeHelper";
    private final Editor mEditor;
    private final SelectionTracker mSelectionTracker;
    private final SmartSelectSprite mSmartSelectSprite;
    private TextClassification mTextClassification;
    private AsyncTask mTextClassificationAsyncTask;
    private final TextClassificationHelper mTextClassificationHelper;
    private final TextView mTextView;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SelectionActionModeHelper(Editor editor) {
        this.mEditor = (Editor) Preconditions.checkNotNull(editor);
        this.mTextView = this.mEditor.getTextView();
        Context context = this.mTextView.getContext();
        TextView textView = this.mTextView;
        Objects.requireNonNull(textView);
        this.mTextClassificationHelper = new TextClassificationHelper(context, new $$Lambda$yIdmBO6ZxaY03PGN08RySVVQXuE(textView), getText(this.mTextView), 0, 1, this.mTextView.getTextLocales());
        this.mSelectionTracker = new SelectionTracker(this.mTextView);
        if (getTextClassificationSettings().isSmartSelectionAnimationEnabled()) {
            Context context2 = this.mTextView.getContext();
            int i = editor.getTextView().mHighlightColor;
            final TextView textView2 = this.mTextView;
            Objects.requireNonNull(textView2);
            this.mSmartSelectSprite = new SmartSelectSprite(context2, i, new Runnable() { // from class: android.widget.-$$Lambda$IfzAW5fP9thoftErKAjo9SLZufw
                @Override // java.lang.Runnable
                public final void run() {
                    TextView.this.invalidate();
                }
            });
            return;
        }
        this.mSmartSelectSprite = null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v3, types: [android.widget.-$$Lambda$E-XesXLNXm7BCuVAnjZcIGfnQJQ] */
    public void startSelectionActionModeAsync(boolean adjustSelection) {
        $$Lambda$aOGBsMC_jnvTDjezYLRtz35nAPI __lambda_aogbsmc_jnvtdjezylrtz35napi;
        Consumer consumer;
        boolean adjustSelection2 = adjustSelection & getTextClassificationSettings().isSmartSelectionEnabled();
        this.mSelectionTracker.onOriginalSelection(getText(this.mTextView), this.mTextView.getSelectionStart(), this.mTextView.getSelectionEnd(), false);
        cancelAsyncTask();
        if (skipTextClassification()) {
            startSelectionActionMode(null);
            return;
        }
        resetTextClassificationHelper();
        TextView textView = this.mTextView;
        int timeoutDuration = this.mTextClassificationHelper.getTimeoutDuration();
        if (adjustSelection2) {
            final TextClassificationHelper textClassificationHelper = this.mTextClassificationHelper;
            Objects.requireNonNull(textClassificationHelper);
            __lambda_aogbsmc_jnvtdjezylrtz35napi = new Supplier() { // from class: android.widget.-$$Lambda$E-XesXLNXm7BCuVAnjZcIGfnQJQ
                @Override // java.util.function.Supplier
                public final Object get() {
                    return SelectionActionModeHelper.TextClassificationHelper.this.suggestSelection();
                }
            };
        } else {
            TextClassificationHelper textClassificationHelper2 = this.mTextClassificationHelper;
            Objects.requireNonNull(textClassificationHelper2);
            __lambda_aogbsmc_jnvtdjezylrtz35napi = new $$Lambda$aOGBsMC_jnvTDjezYLRtz35nAPI(textClassificationHelper2);
        }
        if (this.mSmartSelectSprite != null) {
            consumer = new Consumer() { // from class: android.widget.-$$Lambda$SelectionActionModeHelper$l1f1_V5lw6noQxI_3u11qF753Iw
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    SelectionActionModeHelper.this.startSelectionActionModeWithSmartSelectAnimation((SelectionActionModeHelper.SelectionResult) obj);
                }
            };
        } else {
            consumer = new Consumer() { // from class: android.widget.-$$Lambda$SelectionActionModeHelper$CcJ0IF8nDFsmkuaqvOxFqYGazzY
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    SelectionActionModeHelper.this.startSelectionActionMode((SelectionActionModeHelper.SelectionResult) obj);
                }
            };
        }
        TextClassificationHelper textClassificationHelper3 = this.mTextClassificationHelper;
        Objects.requireNonNull(textClassificationHelper3);
        this.mTextClassificationAsyncTask = new TextClassificationAsyncTask(textView, timeoutDuration, __lambda_aogbsmc_jnvtdjezylrtz35napi, consumer, new $$Lambda$etfJkiCJnT2dqM2O4M2TCm9i_oA(textClassificationHelper3)).execute(new Void[0]);
    }

    public void startLinkActionModeAsync(int start, int end) {
        this.mSelectionTracker.onOriginalSelection(getText(this.mTextView), start, end, true);
        cancelAsyncTask();
        if (skipTextClassification()) {
            startLinkActionMode(null);
            return;
        }
        resetTextClassificationHelper(start, end);
        TextView textView = this.mTextView;
        int timeoutDuration = this.mTextClassificationHelper.getTimeoutDuration();
        TextClassificationHelper textClassificationHelper = this.mTextClassificationHelper;
        Objects.requireNonNull(textClassificationHelper);
        $$Lambda$aOGBsMC_jnvTDjezYLRtz35nAPI __lambda_aogbsmc_jnvtdjezylrtz35napi = new $$Lambda$aOGBsMC_jnvTDjezYLRtz35nAPI(textClassificationHelper);
        Consumer consumer = new Consumer() { // from class: android.widget.-$$Lambda$SelectionActionModeHelper$WnFw1_gP20c3ltvTN6OPqQ5XUns
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                SelectionActionModeHelper.this.startLinkActionMode((SelectionActionModeHelper.SelectionResult) obj);
            }
        };
        TextClassificationHelper textClassificationHelper2 = this.mTextClassificationHelper;
        Objects.requireNonNull(textClassificationHelper2);
        this.mTextClassificationAsyncTask = new TextClassificationAsyncTask(textView, timeoutDuration, __lambda_aogbsmc_jnvtdjezylrtz35napi, consumer, new $$Lambda$etfJkiCJnT2dqM2O4M2TCm9i_oA(textClassificationHelper2)).execute(new Void[0]);
    }

    public void invalidateActionModeAsync() {
        cancelAsyncTask();
        if (skipTextClassification()) {
            invalidateActionMode(null);
            return;
        }
        resetTextClassificationHelper();
        TextView textView = this.mTextView;
        int timeoutDuration = this.mTextClassificationHelper.getTimeoutDuration();
        TextClassificationHelper textClassificationHelper = this.mTextClassificationHelper;
        Objects.requireNonNull(textClassificationHelper);
        $$Lambda$aOGBsMC_jnvTDjezYLRtz35nAPI __lambda_aogbsmc_jnvtdjezylrtz35napi = new $$Lambda$aOGBsMC_jnvTDjezYLRtz35nAPI(textClassificationHelper);
        Consumer consumer = new Consumer() { // from class: android.widget.-$$Lambda$SelectionActionModeHelper$Lwzg10CkEpNBaAXBpjnWEpIlTzQ
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                SelectionActionModeHelper.this.invalidateActionMode((SelectionActionModeHelper.SelectionResult) obj);
            }
        };
        TextClassificationHelper textClassificationHelper2 = this.mTextClassificationHelper;
        Objects.requireNonNull(textClassificationHelper2);
        this.mTextClassificationAsyncTask = new TextClassificationAsyncTask(textView, timeoutDuration, __lambda_aogbsmc_jnvtdjezylrtz35napi, consumer, new $$Lambda$etfJkiCJnT2dqM2O4M2TCm9i_oA(textClassificationHelper2)).execute(new Void[0]);
    }

    public void onSelectionAction(int menuItemId, String actionLabel) {
        this.mSelectionTracker.onSelectionAction(this.mTextView.getSelectionStart(), this.mTextView.getSelectionEnd(), getActionType(menuItemId), actionLabel, this.mTextClassification);
    }

    public void onSelectionDrag() {
        this.mSelectionTracker.onSelectionAction(this.mTextView.getSelectionStart(), this.mTextView.getSelectionEnd(), 106, null, this.mTextClassification);
    }

    public void onTextChanged(int start, int end) {
        this.mSelectionTracker.onTextChanged(start, end, this.mTextClassification);
    }

    public boolean resetSelection(int textIndex) {
        if (this.mSelectionTracker.resetSelection(textIndex, this.mEditor)) {
            invalidateActionModeAsync();
            return true;
        }
        return false;
    }

    public TextClassification getTextClassification() {
        return this.mTextClassification;
    }

    public void onDestroyActionMode() {
        cancelSmartSelectAnimation();
        this.mSelectionTracker.onSelectionDestroyed();
        cancelAsyncTask();
    }

    public void onDraw(Canvas canvas) {
        SmartSelectSprite smartSelectSprite;
        if (isDrawingHighlight() && (smartSelectSprite = this.mSmartSelectSprite) != null) {
            smartSelectSprite.draw(canvas);
        }
    }

    public boolean isDrawingHighlight() {
        SmartSelectSprite smartSelectSprite = this.mSmartSelectSprite;
        return smartSelectSprite != null && smartSelectSprite.isAnimationActive();
    }

    private TextClassificationConstants getTextClassificationSettings() {
        return TextClassificationManager.getSettings(this.mTextView.getContext());
    }

    private void cancelAsyncTask() {
        AsyncTask asyncTask = this.mTextClassificationAsyncTask;
        if (asyncTask != null) {
            asyncTask.cancel(true);
            this.mTextClassificationAsyncTask = null;
        }
        this.mTextClassification = null;
    }

    private boolean skipTextClassification() {
        boolean noOpTextClassifier = this.mTextView.usesNoOpTextClassifier();
        boolean noSelection = this.mTextView.getSelectionEnd() == this.mTextView.getSelectionStart();
        boolean password = this.mTextView.hasPasswordTransformationMethod() || TextView.isPasswordInputType(this.mTextView.getInputType());
        return noOpTextClassifier || noSelection || password;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startLinkActionMode(SelectionResult result) {
        startActionMode(2, result);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startSelectionActionMode(SelectionResult result) {
        startActionMode(0, result);
    }

    private void startActionMode(@Editor.TextActionMode int actionMode, SelectionResult result) {
        CharSequence text = getText(this.mTextView);
        if (result != null && (text instanceof Spannable) && (this.mTextView.isTextSelectable() || this.mTextView.isTextEditable())) {
            if (!getTextClassificationSettings().isModelDarkLaunchEnabled()) {
                Selection.setSelection((Spannable) text, result.mStart, result.mEnd);
                this.mTextView.invalidate();
            }
            this.mTextClassification = result.mClassification;
        } else if (result == null || actionMode != 2) {
            this.mTextClassification = null;
        } else {
            this.mTextClassification = result.mClassification;
        }
        if (this.mEditor.startActionModeInternal(actionMode)) {
            Editor.SelectionModifierCursorController controller = this.mEditor.getSelectionController();
            if (controller != null && (this.mTextView.isTextSelectable() || this.mTextView.isTextEditable())) {
                controller.show();
            }
            if (result != null) {
                if (actionMode != 0) {
                    if (actionMode == 2) {
                        this.mSelectionTracker.onLinkSelected(result);
                    }
                } else {
                    this.mSelectionTracker.onSmartSelection(result);
                }
            }
        }
        this.mEditor.setRestartActionModeOnNextRefresh(false);
        this.mTextClassificationAsyncTask = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startSelectionActionModeWithSmartSelectAnimation(final SelectionResult result) {
        Layout layout = this.mTextView.getLayout();
        Runnable onAnimationEndCallback = new Runnable() { // from class: android.widget.-$$Lambda$SelectionActionModeHelper$xdBRwQcbRdz8duQr0RBo4YKAnOA
            @Override // java.lang.Runnable
            public final void run() {
                SelectionActionModeHelper.this.lambda$startSelectionActionModeWithSmartSelectAnimation$0$SelectionActionModeHelper(result);
            }
        };
        boolean didSelectionChange = (result == null || (this.mTextView.getSelectionStart() == result.mStart && this.mTextView.getSelectionEnd() == result.mEnd)) ? false : true;
        if (!didSelectionChange) {
            onAnimationEndCallback.run();
            return;
        }
        List<SmartSelectSprite.RectangleWithTextSelectionLayout> selectionRectangles = convertSelectionToRectangles(layout, result.mStart, result.mEnd);
        PointF touchPoint = new PointF(this.mEditor.getLastUpPositionX(), this.mEditor.getLastUpPositionY());
        PointF animationStartPoint = movePointInsideNearestRectangle(touchPoint, selectionRectangles, $$Lambda$ChL7kntlZCrPaPVdRfaSzGdk1JU.INSTANCE);
        this.mSmartSelectSprite.startAnimation(animationStartPoint, selectionRectangles, onAnimationEndCallback);
    }

    public /* synthetic */ void lambda$startSelectionActionModeWithSmartSelectAnimation$0$SelectionActionModeHelper(SelectionResult result) {
        SelectionResult startSelectionResult;
        if (result != null && result.mStart >= 0 && result.mEnd <= getText(this.mTextView).length() && result.mStart <= result.mEnd) {
            startSelectionResult = result;
        } else {
            startSelectionResult = null;
        }
        startSelectionActionMode(startSelectionResult);
    }

    private List<SmartSelectSprite.RectangleWithTextSelectionLayout> convertSelectionToRectangles(Layout layout, int start, int end) {
        final List<SmartSelectSprite.RectangleWithTextSelectionLayout> result = new ArrayList<>();
        Layout.SelectionRectangleConsumer consumer = new Layout.SelectionRectangleConsumer() { // from class: android.widget.-$$Lambda$SelectionActionModeHelper$cMbIRcH-yFkksR3CQmROa0_hmgM
            @Override // android.text.Layout.SelectionRectangleConsumer
            public final void accept(float f, float f2, float f3, float f4, int i) {
                SelectionActionModeHelper.mergeRectangleIntoList(result, new RectF(f, f2, f3, f4), $$Lambda$ChL7kntlZCrPaPVdRfaSzGdk1JU.INSTANCE, new Function() { // from class: android.widget.-$$Lambda$SelectionActionModeHelper$mSUWA79GbPno-4-1PEW8ZDcf0L0
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return SelectionActionModeHelper.lambda$convertSelectionToRectangles$1(i, (RectF) obj);
                    }
                });
            }
        };
        layout.getSelection(start, end, consumer);
        result.sort(Comparator.comparing($$Lambda$ChL7kntlZCrPaPVdRfaSzGdk1JU.INSTANCE, SmartSelectSprite.RECTANGLE_COMPARATOR));
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ SmartSelectSprite.RectangleWithTextSelectionLayout lambda$convertSelectionToRectangles$1(int textSelectionLayout, RectF r) {
        return new SmartSelectSprite.RectangleWithTextSelectionLayout(r, textSelectionLayout);
    }

    @VisibleForTesting
    public static <T> void mergeRectangleIntoList(List<T> list, RectF candidate, Function<T, RectF> extractor, Function<RectF, T> packer) {
        if (candidate.isEmpty()) {
            return;
        }
        int elementCount = list.size();
        for (int index = 0; index < elementCount; index++) {
            RectF existingRectangle = extractor.apply(list.get(index));
            if (existingRectangle.contains(candidate)) {
                return;
            }
            if (candidate.contains(existingRectangle)) {
                existingRectangle.setEmpty();
            } else {
                boolean canMerge = false;
                boolean rectanglesContinueEachOther = candidate.left == existingRectangle.right || candidate.right == existingRectangle.left;
                if (candidate.top == existingRectangle.top && candidate.bottom == existingRectangle.bottom && (RectF.intersects(candidate, existingRectangle) || rectanglesContinueEachOther)) {
                    canMerge = true;
                }
                if (canMerge) {
                    candidate.union(existingRectangle);
                    existingRectangle.setEmpty();
                }
            }
        }
        for (int index2 = elementCount - 1; index2 >= 0; index2--) {
            RectF rectangle = extractor.apply(list.get(index2));
            if (rectangle.isEmpty()) {
                list.remove(index2);
            }
        }
        list.add(packer.apply(candidate));
    }

    @VisibleForTesting
    public static <T> PointF movePointInsideNearestRectangle(PointF point, List<T> list, Function<T, RectF> extractor) {
        float candidateX;
        float bestX = -1.0f;
        float bestY = -1.0f;
        double bestDistance = Double.MAX_VALUE;
        int elementCount = list.size();
        for (int index = 0; index < elementCount; index++) {
            RectF rectangle = extractor.apply(list.get(index));
            float candidateY = rectangle.centerY();
            if (point.x > rectangle.right) {
                candidateX = rectangle.right;
            } else {
                float candidateX2 = point.x;
                if (candidateX2 < rectangle.left) {
                    candidateX = rectangle.left;
                } else {
                    candidateX = point.x;
                }
            }
            double candidateDistance = Math.pow(point.x - candidateX, 2.0d) + Math.pow(point.y - candidateY, 2.0d);
            if (candidateDistance < bestDistance) {
                bestX = candidateX;
                bestY = candidateY;
                bestDistance = candidateDistance;
            }
        }
        return new PointF(bestX, bestY);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void invalidateActionMode(SelectionResult result) {
        TextClassification textClassification;
        cancelSmartSelectAnimation();
        if (result == null) {
            textClassification = null;
        } else {
            textClassification = result.mClassification;
        }
        this.mTextClassification = textClassification;
        ActionMode actionMode = this.mEditor.getTextActionMode();
        if (actionMode != null) {
            actionMode.invalidate();
        }
        this.mSelectionTracker.onSelectionUpdated(this.mTextView.getSelectionStart(), this.mTextView.getSelectionEnd(), this.mTextClassification);
        this.mTextClassificationAsyncTask = null;
    }

    private void resetTextClassificationHelper(int selectionStart, int selectionEnd) {
        if (selectionStart < 0 || selectionEnd < 0) {
            selectionStart = this.mTextView.getSelectionStart();
            selectionEnd = this.mTextView.getSelectionEnd();
        }
        TextClassificationHelper textClassificationHelper = this.mTextClassificationHelper;
        TextView textView = this.mTextView;
        Objects.requireNonNull(textView);
        textClassificationHelper.init(new $$Lambda$yIdmBO6ZxaY03PGN08RySVVQXuE(textView), getText(this.mTextView), selectionStart, selectionEnd, this.mTextView.getTextLocales());
    }

    private void resetTextClassificationHelper() {
        resetTextClassificationHelper(-1, -1);
    }

    private void cancelSmartSelectAnimation() {
        SmartSelectSprite smartSelectSprite = this.mSmartSelectSprite;
        if (smartSelectSprite != null) {
            smartSelectSprite.cancelAnimation();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class SelectionTracker {
        private boolean mAllowReset;
        private final LogAbandonRunnable mDelayedLogAbandon = new LogAbandonRunnable();
        private SelectionMetricsLogger mLogger;
        private int mOriginalEnd;
        private int mOriginalStart;
        private int mSelectionEnd;
        private int mSelectionStart;
        private final TextView mTextView;

        SelectionTracker(TextView textView) {
            this.mTextView = (TextView) Preconditions.checkNotNull(textView);
            this.mLogger = new SelectionMetricsLogger(textView);
        }

        public void onOriginalSelection(CharSequence text, int selectionStart, int selectionEnd, boolean isLink) {
            this.mDelayedLogAbandon.flush();
            this.mSelectionStart = selectionStart;
            this.mOriginalStart = selectionStart;
            this.mSelectionEnd = selectionEnd;
            this.mOriginalEnd = selectionEnd;
            this.mAllowReset = false;
            maybeInvalidateLogger();
            this.mLogger.logSelectionStarted(this.mTextView.getTextClassificationSession(), this.mTextView.getTextClassificationContext(), text, selectionStart, isLink ? 2 : 1);
        }

        public void onSmartSelection(SelectionResult result) {
            onClassifiedSelection(result);
            this.mLogger.logSelectionModified(result.mStart, result.mEnd, result.mClassification, result.mSelection);
        }

        public void onLinkSelected(SelectionResult result) {
            onClassifiedSelection(result);
        }

        private void onClassifiedSelection(SelectionResult result) {
            if (!isSelectionStarted()) {
                return;
            }
            this.mSelectionStart = result.mStart;
            this.mSelectionEnd = result.mEnd;
            this.mAllowReset = (this.mSelectionStart == this.mOriginalStart && this.mSelectionEnd == this.mOriginalEnd) ? false : true;
        }

        public void onSelectionUpdated(int selectionStart, int selectionEnd, TextClassification classification) {
            if (isSelectionStarted()) {
                this.mSelectionStart = selectionStart;
                this.mSelectionEnd = selectionEnd;
                this.mAllowReset = false;
                this.mLogger.logSelectionModified(selectionStart, selectionEnd, classification, null);
            }
        }

        public void onSelectionDestroyed() {
            this.mAllowReset = false;
            this.mDelayedLogAbandon.schedule(100);
        }

        public void onSelectionAction(int selectionStart, int selectionEnd, int action, String actionLabel, TextClassification classification) {
            if (isSelectionStarted()) {
                this.mAllowReset = false;
                this.mLogger.logSelectionAction(selectionStart, selectionEnd, action, actionLabel, classification);
            }
        }

        public boolean resetSelection(int textIndex, Editor editor) {
            TextView textView = editor.getTextView();
            if (isSelectionStarted() && this.mAllowReset && textIndex >= this.mSelectionStart && textIndex <= this.mSelectionEnd && (SelectionActionModeHelper.getText(textView) instanceof Spannable)) {
                this.mAllowReset = false;
                boolean selected = editor.selectCurrentWord();
                if (selected) {
                    this.mSelectionStart = editor.getTextView().getSelectionStart();
                    this.mSelectionEnd = editor.getTextView().getSelectionEnd();
                    this.mLogger.logSelectionAction(textView.getSelectionStart(), textView.getSelectionEnd(), 201, null, null);
                }
                return selected;
            }
            return false;
        }

        public void onTextChanged(int start, int end, TextClassification classification) {
            if (isSelectionStarted() && start == this.mSelectionStart && end == this.mSelectionEnd) {
                onSelectionAction(start, end, 100, null, classification);
            }
        }

        private void maybeInvalidateLogger() {
            if (this.mLogger.isEditTextLogger() != this.mTextView.isTextEditable()) {
                this.mLogger = new SelectionMetricsLogger(this.mTextView);
            }
        }

        private boolean isSelectionStarted() {
            int i;
            int i2 = this.mSelectionStart;
            return i2 >= 0 && (i = this.mSelectionEnd) >= 0 && i2 != i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public final class LogAbandonRunnable implements Runnable {
            private boolean mIsPending;

            private LogAbandonRunnable() {
            }

            void schedule(int delayMillis) {
                if (this.mIsPending) {
                    Log.e(SelectionActionModeHelper.LOG_TAG, "Force flushing abandon due to new scheduling request");
                    flush();
                }
                this.mIsPending = true;
                SelectionTracker.this.mTextView.postDelayed(this, delayMillis);
            }

            void flush() {
                SelectionTracker.this.mTextView.removeCallbacks(this);
                run();
            }

            @Override // java.lang.Runnable
            public void run() {
                if (this.mIsPending) {
                    SelectionTracker.this.mLogger.logSelectionAction(SelectionTracker.this.mSelectionStart, SelectionTracker.this.mSelectionEnd, 107, null, null);
                    SelectionTracker selectionTracker = SelectionTracker.this;
                    selectionTracker.mSelectionStart = selectionTracker.mSelectionEnd = -1;
                    SelectionTracker.this.mLogger.endTextClassificationSession();
                    this.mIsPending = false;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class SelectionMetricsLogger {
        private static final String LOG_TAG = "SelectionMetricsLogger";
        private static final Pattern PATTERN_WHITESPACE = Pattern.compile("\\s+");
        private TextClassificationContext mClassificationContext;
        private TextClassifier mClassificationSession;
        private final boolean mEditTextLogger;
        private int mStartIndex;
        private String mText;
        private final BreakIterator mTokenIterator;
        private TextClassifierEvent mTranslateClickEvent;
        private TextClassifierEvent mTranslateViewEvent;

        SelectionMetricsLogger(TextView textView) {
            Preconditions.checkNotNull(textView);
            this.mEditTextLogger = textView.isTextEditable();
            this.mTokenIterator = SelectionSessionLogger.getTokenIterator(textView.getTextLocale());
        }

        public void logSelectionStarted(TextClassifier classificationSession, TextClassificationContext classificationContext, CharSequence text, int index, int invocationMethod) {
            try {
                Preconditions.checkNotNull(text);
                Preconditions.checkArgumentInRange(index, 0, text.length(), "index");
                if (this.mText == null || !this.mText.contentEquals(text)) {
                    this.mText = text.toString();
                }
                this.mTokenIterator.setText(this.mText);
                this.mStartIndex = index;
                this.mClassificationSession = classificationSession;
                this.mClassificationContext = classificationContext;
                if (hasActiveClassificationSession()) {
                    this.mClassificationSession.onSelectionEvent(SelectionEvent.createSelectionStartedEvent(invocationMethod, 0));
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "" + e.getMessage(), e);
            }
        }

        public void logSelectionModified(int start, int end, TextClassification classification, TextSelection selection) {
            try {
                if (hasActiveClassificationSession()) {
                    Preconditions.checkArgumentInRange(start, 0, this.mText.length(), Telephony.BaseMmsColumns.START);
                    Preconditions.checkArgumentInRange(end, start, this.mText.length(), "end");
                    int[] wordIndices = getWordDelta(start, end);
                    if (selection != null) {
                        this.mClassificationSession.onSelectionEvent(SelectionEvent.createSelectionModifiedEvent(wordIndices[0], wordIndices[1], selection));
                    } else if (classification != null) {
                        this.mClassificationSession.onSelectionEvent(SelectionEvent.createSelectionModifiedEvent(wordIndices[0], wordIndices[1], classification));
                    } else {
                        this.mClassificationSession.onSelectionEvent(SelectionEvent.createSelectionModifiedEvent(wordIndices[0], wordIndices[1]));
                    }
                    maybeGenerateTranslateViewEvent(classification);
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "" + e.getMessage(), e);
            }
        }

        public void logSelectionAction(int start, int end, int action, String actionLabel, TextClassification classification) {
            try {
                if (hasActiveClassificationSession()) {
                    Preconditions.checkArgumentInRange(start, 0, this.mText.length(), Telephony.BaseMmsColumns.START);
                    Preconditions.checkArgumentInRange(end, start, this.mText.length(), "end");
                    int[] wordIndices = getWordDelta(start, end);
                    if (classification != null) {
                        this.mClassificationSession.onSelectionEvent(SelectionEvent.createSelectionActionEvent(wordIndices[0], wordIndices[1], action, classification));
                    } else {
                        this.mClassificationSession.onSelectionEvent(SelectionEvent.createSelectionActionEvent(wordIndices[0], wordIndices[1], action));
                    }
                    maybeGenerateTranslateClickEvent(classification, actionLabel);
                    if (SelectionEvent.isTerminal(action)) {
                        endTextClassificationSession();
                    }
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "" + e.getMessage(), e);
            }
        }

        public boolean isEditTextLogger() {
            return this.mEditTextLogger;
        }

        public void endTextClassificationSession() {
            if (hasActiveClassificationSession()) {
                maybeReportTranslateEvents();
                this.mClassificationSession.destroy();
            }
        }

        private boolean hasActiveClassificationSession() {
            TextClassifier textClassifier = this.mClassificationSession;
            return (textClassifier == null || textClassifier.isDestroyed()) ? false : true;
        }

        private int[] getWordDelta(int start, int end) {
            int[] wordIndices = new int[2];
            int i = this.mStartIndex;
            if (start == i) {
                wordIndices[0] = 0;
            } else if (start < i) {
                wordIndices[0] = -countWordsForward(start);
            } else {
                wordIndices[0] = countWordsBackward(start);
                if (!this.mTokenIterator.isBoundary(start) && !isWhitespace(this.mTokenIterator.preceding(start), this.mTokenIterator.following(start))) {
                    wordIndices[0] = wordIndices[0] - 1;
                }
            }
            int i2 = this.mStartIndex;
            if (end == i2) {
                wordIndices[1] = 0;
            } else if (end < i2) {
                wordIndices[1] = -countWordsForward(end);
            } else {
                wordIndices[1] = countWordsBackward(end);
            }
            return wordIndices;
        }

        private int countWordsBackward(int from) {
            Preconditions.checkArgument(from >= this.mStartIndex);
            int wordCount = 0;
            int offset = from;
            while (offset > this.mStartIndex) {
                int start = this.mTokenIterator.preceding(offset);
                if (!isWhitespace(start, offset)) {
                    wordCount++;
                }
                offset = start;
            }
            return wordCount;
        }

        private int countWordsForward(int from) {
            Preconditions.checkArgument(from <= this.mStartIndex);
            int wordCount = 0;
            int offset = from;
            while (offset < this.mStartIndex) {
                int end = this.mTokenIterator.following(offset);
                if (!isWhitespace(offset, end)) {
                    wordCount++;
                }
                offset = end;
            }
            return wordCount;
        }

        private boolean isWhitespace(int start, int end) {
            return PATTERN_WHITESPACE.matcher(this.mText.substring(start, end)).matches();
        }

        private void maybeGenerateTranslateViewEvent(TextClassification classification) {
            if (classification != null) {
                TextClassifierEvent event = generateTranslateEvent(6, classification, this.mClassificationContext, null);
                this.mTranslateViewEvent = event != null ? event : this.mTranslateViewEvent;
            }
        }

        private void maybeGenerateTranslateClickEvent(TextClassification classification, String actionLabel) {
            if (classification != null) {
                this.mTranslateClickEvent = generateTranslateEvent(13, classification, this.mClassificationContext, actionLabel);
            }
        }

        private void maybeReportTranslateEvents() {
            TextClassifierEvent textClassifierEvent = this.mTranslateViewEvent;
            if (textClassifierEvent != null) {
                this.mClassificationSession.onTextClassifierEvent(textClassifierEvent);
                this.mTranslateViewEvent = null;
            }
            TextClassifierEvent textClassifierEvent2 = this.mTranslateClickEvent;
            if (textClassifierEvent2 != null) {
                this.mClassificationSession.onTextClassifierEvent(textClassifierEvent2);
                this.mTranslateClickEvent = null;
            }
        }

        private static TextClassifierEvent generateTranslateEvent(int eventType, TextClassification classification, TextClassificationContext classificationContext, String actionLabel) {
            RemoteAction translateAction = ExtrasUtils.findTranslateAction(classification);
            if (translateAction == null) {
                return null;
            }
            if (eventType == 13 && !translateAction.getTitle().toString().equals(actionLabel)) {
                return null;
            }
            Bundle foreignLanguageExtra = ExtrasUtils.getForeignLanguageExtra(classification);
            String language = ExtrasUtils.getEntityType(foreignLanguageExtra);
            float score = ExtrasUtils.getScore(foreignLanguageExtra);
            String model = ExtrasUtils.getModelName(foreignLanguageExtra);
            return new TextClassifierEvent.LanguageDetectionEvent.Builder(eventType).setEventContext(classificationContext).setResultId(classification.getId()).setEntityTypes(language).setScores(score).setActionIndices(classification.getActions().indexOf(translateAction)).setModelName(model).build();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class TextClassificationAsyncTask extends AsyncTask<Void, Void, SelectionResult> {
        private final String mOriginalText;
        private final Consumer<SelectionResult> mSelectionResultCallback;
        private final Supplier<SelectionResult> mSelectionResultSupplier;
        private final TextView mTextView;
        private final int mTimeOutDuration;
        private final Supplier<SelectionResult> mTimeOutResultSupplier;

        TextClassificationAsyncTask(TextView textView, int timeOut, Supplier<SelectionResult> selectionResultSupplier, Consumer<SelectionResult> selectionResultCallback, Supplier<SelectionResult> timeOutResultSupplier) {
            super(textView != null ? textView.getHandler() : null);
            this.mTextView = (TextView) Preconditions.checkNotNull(textView);
            this.mTimeOutDuration = timeOut;
            this.mSelectionResultSupplier = (Supplier) Preconditions.checkNotNull(selectionResultSupplier);
            this.mSelectionResultCallback = (Consumer) Preconditions.checkNotNull(selectionResultCallback);
            this.mTimeOutResultSupplier = (Supplier) Preconditions.checkNotNull(timeOutResultSupplier);
            this.mOriginalText = SelectionActionModeHelper.getText(this.mTextView).toString();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public SelectionResult doInBackground(Void... params) {
            Runnable onTimeOut = new Runnable() { // from class: android.widget.-$$Lambda$SelectionActionModeHelper$TextClassificationAsyncTask$D5tkmK-caFBtl9ux2L0aUfUee4E
                @Override // java.lang.Runnable
                public final void run() {
                    SelectionActionModeHelper.TextClassificationAsyncTask.this.onTimeOut();
                }
            };
            this.mTextView.postDelayed(onTimeOut, this.mTimeOutDuration);
            SelectionResult result = this.mSelectionResultSupplier.get();
            this.mTextView.removeCallbacks(onTimeOut);
            return result;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(SelectionResult result) {
            this.mSelectionResultCallback.accept(TextUtils.equals(this.mOriginalText, SelectionActionModeHelper.getText(this.mTextView)) ? result : null);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onTimeOut() {
            Log.d(SelectionActionModeHelper.LOG_TAG, "Timeout in TextClassificationAsyncTask");
            if (getStatus() == AsyncTask.Status.RUNNING) {
                onPostExecute(this.mTimeOutResultSupplier.get());
            }
            cancel(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class TextClassificationHelper {
        private static final int TRIM_DELTA = 120;
        private final Context mContext;
        private LocaleList mDefaultLocales;
        private boolean mHot;
        private LocaleList mLastClassificationLocales;
        private SelectionResult mLastClassificationResult;
        private int mLastClassificationSelectionEnd;
        private int mLastClassificationSelectionStart;
        private CharSequence mLastClassificationText;
        private int mRelativeEnd;
        private int mRelativeStart;
        private int mSelectionEnd;
        private int mSelectionStart;
        private String mText;
        private Supplier<TextClassifier> mTextClassifier;
        private int mTrimStart;
        private CharSequence mTrimmedText;

        TextClassificationHelper(Context context, Supplier<TextClassifier> textClassifier, CharSequence text, int selectionStart, int selectionEnd, LocaleList locales) {
            init(textClassifier, text, selectionStart, selectionEnd, locales);
            this.mContext = (Context) Preconditions.checkNotNull(context);
        }

        public void init(Supplier<TextClassifier> textClassifier, CharSequence text, int selectionStart, int selectionEnd, LocaleList locales) {
            this.mTextClassifier = (Supplier) Preconditions.checkNotNull(textClassifier);
            this.mText = ((CharSequence) Preconditions.checkNotNull(text)).toString();
            this.mLastClassificationText = null;
            Preconditions.checkArgument(selectionEnd > selectionStart);
            this.mSelectionStart = selectionStart;
            this.mSelectionEnd = selectionEnd;
            this.mDefaultLocales = locales;
        }

        public SelectionResult classifyText() {
            this.mHot = true;
            return performClassification(null);
        }

        public SelectionResult suggestSelection() {
            TextSelection selection;
            this.mHot = true;
            trimText();
            if (this.mContext.getApplicationInfo().targetSdkVersion >= 28) {
                TextSelection.Request request = new TextSelection.Request.Builder(this.mTrimmedText, this.mRelativeStart, this.mRelativeEnd).setDefaultLocales(this.mDefaultLocales).setDarkLaunchAllowed(true).build();
                selection = this.mTextClassifier.get().suggestSelection(request);
            } else {
                selection = this.mTextClassifier.get().suggestSelection(this.mTrimmedText, this.mRelativeStart, this.mRelativeEnd, this.mDefaultLocales);
            }
            if (!isDarkLaunchEnabled()) {
                this.mSelectionStart = Math.max(0, selection.getSelectionStartIndex() + this.mTrimStart);
                this.mSelectionEnd = Math.min(this.mText.length(), selection.getSelectionEndIndex() + this.mTrimStart);
            }
            return performClassification(selection);
        }

        public SelectionResult getOriginalSelection() {
            return new SelectionResult(this.mSelectionStart, this.mSelectionEnd, null, null);
        }

        public int getTimeoutDuration() {
            if (this.mHot) {
                return 200;
            }
            return 500;
        }

        private boolean isDarkLaunchEnabled() {
            return TextClassificationManager.getSettings(this.mContext).isModelDarkLaunchEnabled();
        }

        private SelectionResult performClassification(TextSelection selection) {
            TextClassification classification;
            if (!Objects.equals(this.mText, this.mLastClassificationText) || this.mSelectionStart != this.mLastClassificationSelectionStart || this.mSelectionEnd != this.mLastClassificationSelectionEnd || !Objects.equals(this.mDefaultLocales, this.mLastClassificationLocales)) {
                this.mLastClassificationText = this.mText;
                this.mLastClassificationSelectionStart = this.mSelectionStart;
                this.mLastClassificationSelectionEnd = this.mSelectionEnd;
                this.mLastClassificationLocales = this.mDefaultLocales;
                trimText();
                if (Linkify.containsUnsupportedCharacters(this.mText)) {
                    EventLog.writeEvent(1397638484, "116321860", -1, "");
                    classification = TextClassification.EMPTY;
                } else if (this.mContext.getApplicationInfo().targetSdkVersion >= 28) {
                    TextClassification.Request request = new TextClassification.Request.Builder(this.mTrimmedText, this.mRelativeStart, this.mRelativeEnd).setDefaultLocales(this.mDefaultLocales).build();
                    classification = this.mTextClassifier.get().classifyText(request);
                } else {
                    classification = this.mTextClassifier.get().classifyText(this.mTrimmedText, this.mRelativeStart, this.mRelativeEnd, this.mDefaultLocales);
                }
                this.mLastClassificationResult = new SelectionResult(this.mSelectionStart, this.mSelectionEnd, classification, selection);
            }
            return this.mLastClassificationResult;
        }

        private void trimText() {
            this.mTrimStart = Math.max(0, this.mSelectionStart + PackageManager.INSTALL_FAILED_MULTIPACKAGE_INCONSISTENCY);
            int referenceEnd = Math.min(this.mText.length(), this.mSelectionEnd + 120);
            this.mTrimmedText = this.mText.subSequence(this.mTrimStart, referenceEnd);
            int i = this.mSelectionStart;
            int i2 = this.mTrimStart;
            this.mRelativeStart = i - i2;
            this.mRelativeEnd = this.mSelectionEnd - i2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class SelectionResult {
        private final TextClassification mClassification;
        private final int mEnd;
        private final TextSelection mSelection;
        private final int mStart;

        SelectionResult(int start, int end, TextClassification classification, TextSelection selection) {
            this.mStart = start;
            this.mEnd = end;
            this.mClassification = classification;
            this.mSelection = selection;
        }
    }

    private static int getActionType(int menuItemId) {
        if (menuItemId != 16908337) {
            if (menuItemId != 16908341) {
                if (menuItemId != 16908353) {
                    switch (menuItemId) {
                        case 16908319:
                            return 200;
                        case 16908320:
                            return 103;
                        case 16908321:
                            return 101;
                        case 16908322:
                            return 102;
                        default:
                            return 108;
                    }
                }
                return 105;
            }
            return 104;
        }
        return 102;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static CharSequence getText(TextView textView) {
        CharSequence text = textView.getText();
        if (text != null) {
            return text;
        }
        return "";
    }
}
