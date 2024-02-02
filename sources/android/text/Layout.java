package android.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.TextUtils;
import android.text.method.TextKeyListener;
import android.text.style.AlignmentSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineBackgroundSpan;
import android.text.style.ParagraphStyle;
import android.text.style.ReplacementSpan;
import android.text.style.TabStopSpan;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
/* loaded from: classes2.dex */
public abstract class Layout {
    public static final int BREAK_STRATEGY_BALANCED = 2;
    public static final int BREAK_STRATEGY_HIGH_QUALITY = 1;
    public static final int BREAK_STRATEGY_SIMPLE = 0;
    public static final float DEFAULT_LINESPACING_ADDITION = 0.0f;
    public static final float DEFAULT_LINESPACING_MULTIPLIER = 1.0f;
    public static final int DIR_LEFT_TO_RIGHT = 1;
    public private protected static final int DIR_REQUEST_DEFAULT_LTR = 2;
    static final int DIR_REQUEST_DEFAULT_RTL = -2;
    static final int DIR_REQUEST_LTR = 1;
    static final int DIR_REQUEST_RTL = -1;
    public static final int DIR_RIGHT_TO_LEFT = -1;
    public static final int HYPHENATION_FREQUENCY_FULL = 2;
    public static final int HYPHENATION_FREQUENCY_NONE = 0;
    public static final int HYPHENATION_FREQUENCY_NORMAL = 1;
    public static final int JUSTIFICATION_MODE_INTER_WORD = 1;
    public static final int JUSTIFICATION_MODE_NONE = 0;
    static final int RUN_LEVEL_MASK = 63;
    static final int RUN_LEVEL_SHIFT = 26;
    static final int RUN_RTL_FLAG = 67108864;
    private static final int TAB_INCREMENT = 20;
    public static final int TEXT_SELECTION_LAYOUT_LEFT_TO_RIGHT = 1;
    public static final int TEXT_SELECTION_LAYOUT_RIGHT_TO_LEFT = 0;
    private Alignment mAlignment;
    private int mJustificationMode;
    private SpanSet<LineBackgroundSpan> mLineBackgroundSpans;
    public protected TextPaint mPaint;
    private float mSpacingAdd;
    private float mSpacingMult;
    private boolean mSpannedText;
    private CharSequence mText;
    private TextDirectionHeuristic mTextDir;
    private int mWidth;
    private TextPaint mWorkPaint;
    private static final ParagraphStyle[] NO_PARA_SPANS = (ParagraphStyle[]) ArrayUtils.emptyArray(ParagraphStyle.class);
    private static final Rect sTempRect = new Rect();
    static final int RUN_LENGTH_MASK = 67108863;
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    private protected static final Directions DIRS_ALL_LEFT_TO_RIGHT = new Directions(new int[]{0, RUN_LENGTH_MASK});
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    private protected static final Directions DIRS_ALL_RIGHT_TO_LEFT = new Directions(new int[]{0, 134217727});

    /* loaded from: classes2.dex */
    public enum Alignment {
        ALIGN_NORMAL,
        ALIGN_OPPOSITE,
        ALIGN_CENTER,
        ALIGN_LEFT,
        ALIGN_RIGHT
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface BreakStrategy {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface Direction {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface HyphenationFrequency {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface JustificationMode {
    }

    @FunctionalInterface
    /* loaded from: classes2.dex */
    public interface SelectionRectangleConsumer {
        synchronized void accept(float f, float f2, float f3, float f4, int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface TextSelectionLayout {
    }

    public abstract int getBottomPadding();

    public abstract int getEllipsisCount(int i);

    public abstract int getEllipsisStart(int i);

    public abstract boolean getLineContainsTab(int i);

    public abstract int getLineCount();

    public abstract int getLineDescent(int i);

    public abstract Directions getLineDirections(int i);

    public abstract int getLineStart(int i);

    public abstract int getLineTop(int i);

    public abstract int getParagraphDirection(int i);

    public abstract int getTopPadding();

    public static float getDesiredWidth(CharSequence source, TextPaint paint) {
        return getDesiredWidth(source, 0, source.length(), paint);
    }

    public static float getDesiredWidth(CharSequence source, int start, int end, TextPaint paint) {
        return getDesiredWidth(source, start, end, paint, TextDirectionHeuristics.FIRSTSTRONG_LTR);
    }

    public static synchronized float getDesiredWidth(CharSequence source, int start, int end, TextPaint paint, TextDirectionHeuristic textDir) {
        return getDesiredWidthWithLimit(source, start, end, paint, textDir, Float.MAX_VALUE);
    }

    public static synchronized float getDesiredWidthWithLimit(CharSequence source, int start, int end, TextPaint paint, TextDirectionHeuristic textDir, float upperLimit) {
        float need = 0.0f;
        int i = start;
        while (i <= end) {
            int next = TextUtils.indexOf(source, '\n', i, end);
            if (next < 0) {
                next = end;
            }
            float w = measurePara(paint, source, i, next, textDir);
            if (w > upperLimit) {
                return upperLimit;
            }
            if (w > need) {
                need = w;
            }
            i = next + 1;
        }
        return need;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Layout(CharSequence text, TextPaint paint, int width, Alignment align, float spacingMult, float spacingAdd) {
        this(text, paint, width, align, TextDirectionHeuristics.FIRSTSTRONG_LTR, spacingMult, spacingAdd);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized Layout(CharSequence text, TextPaint paint, int width, Alignment align, TextDirectionHeuristic textDir, float spacingMult, float spacingAdd) {
        this.mWorkPaint = new TextPaint();
        this.mAlignment = Alignment.ALIGN_NORMAL;
        if (width < 0) {
            throw new IllegalArgumentException("Layout: " + width + " < 0");
        }
        if (paint != null) {
            paint.bgColor = 0;
            paint.baselineShift = 0;
        }
        this.mText = text;
        this.mPaint = paint;
        this.mWidth = width;
        this.mAlignment = align;
        this.mSpacingMult = spacingMult;
        this.mSpacingAdd = spacingAdd;
        this.mSpannedText = text instanceof Spanned;
        this.mTextDir = textDir;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void setJustificationMode(int justificationMode) {
        this.mJustificationMode = justificationMode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void replaceWith(CharSequence text, TextPaint paint, int width, Alignment align, float spacingmult, float spacingadd) {
        if (width < 0) {
            throw new IllegalArgumentException("Layout: " + width + " < 0");
        }
        this.mText = text;
        this.mPaint = paint;
        this.mWidth = width;
        this.mAlignment = align;
        this.mSpacingMult = spacingmult;
        this.mSpacingAdd = spacingadd;
        this.mSpannedText = text instanceof Spanned;
    }

    public void draw(Canvas c) {
        draw(c, null, null, 0);
    }

    public void draw(Canvas canvas, Path highlight, Paint highlightPaint, int cursorOffsetVertical) {
        long lineRange = getLineRangeForDraw(canvas);
        int firstLine = TextUtils.unpackRangeStartFromLong(lineRange);
        int lastLine = TextUtils.unpackRangeEndFromLong(lineRange);
        if (lastLine < 0) {
            return;
        }
        drawBackground(canvas, highlight, highlightPaint, cursorOffsetVertical, firstLine, lastLine);
        drawText(canvas, firstLine, lastLine);
    }

    private synchronized boolean isJustificationRequired(int lineNum) {
        int lineEnd;
        return (this.mJustificationMode == 0 || (lineEnd = getLineEnd(lineNum)) >= this.mText.length() || this.mText.charAt(lineEnd + (-1)) == '\n') ? false : true;
    }

    private synchronized float getJustifyWidth(int lineNum) {
        Alignment align;
        int indentWidth;
        Alignment paraAlign = this.mAlignment;
        int left = 0;
        int right = this.mWidth;
        int dir = getParagraphDirection(lineNum);
        ParagraphStyle[] spans = NO_PARA_SPANS;
        if (this.mSpannedText) {
            Spanned sp = (Spanned) this.mText;
            int start = getLineStart(lineNum);
            boolean isFirstParaLine = start == 0 || this.mText.charAt(start + (-1)) == '\n';
            if (isFirstParaLine) {
                spans = (ParagraphStyle[]) getParagraphSpans(sp, start, sp.nextSpanTransition(start, this.mText.length(), ParagraphStyle.class), ParagraphStyle.class);
                int n = spans.length - 1;
                while (true) {
                    if (n < 0) {
                        break;
                    } else if (!(spans[n] instanceof AlignmentSpan)) {
                        n--;
                    } else {
                        paraAlign = ((AlignmentSpan) spans[n]).getAlignment();
                        break;
                    }
                }
            }
            int spanEnd = spans.length;
            boolean useFirstLineMargin = isFirstParaLine;
            int n2 = 0;
            while (true) {
                if (n2 >= spanEnd) {
                    break;
                }
                if (spans[n2] instanceof LeadingMarginSpan.LeadingMarginSpan2) {
                    int count = ((LeadingMarginSpan.LeadingMarginSpan2) spans[n2]).getLeadingMarginLineCount();
                    int startLine = getLineForOffset(sp.getSpanStart(spans[n2]));
                    if (lineNum < startLine + count) {
                        useFirstLineMargin = true;
                        break;
                    }
                }
                n2++;
            }
            int n3 = 0;
            while (true) {
                int n4 = n3;
                if (n4 >= spanEnd) {
                    break;
                }
                if (spans[n4] instanceof LeadingMarginSpan) {
                    LeadingMarginSpan margin = (LeadingMarginSpan) spans[n4];
                    if (dir == -1) {
                        right -= margin.getLeadingMargin(useFirstLineMargin);
                    } else {
                        left += margin.getLeadingMargin(useFirstLineMargin);
                    }
                }
                n3 = n4 + 1;
            }
        }
        if (paraAlign == Alignment.ALIGN_LEFT) {
            align = dir == 1 ? Alignment.ALIGN_NORMAL : Alignment.ALIGN_OPPOSITE;
        } else {
            Alignment align2 = Alignment.ALIGN_RIGHT;
            if (paraAlign == align2) {
                align = dir == 1 ? Alignment.ALIGN_OPPOSITE : Alignment.ALIGN_NORMAL;
            } else {
                align = paraAlign;
            }
        }
        if (align == Alignment.ALIGN_NORMAL) {
            indentWidth = dir == 1 ? getIndentAdjust(lineNum, Alignment.ALIGN_LEFT) : -getIndentAdjust(lineNum, Alignment.ALIGN_RIGHT);
        } else if (align != Alignment.ALIGN_OPPOSITE) {
            indentWidth = getIndentAdjust(lineNum, Alignment.ALIGN_CENTER);
        } else {
            indentWidth = dir == 1 ? -getIndentAdjust(lineNum, Alignment.ALIGN_RIGHT) : getIndentAdjust(lineNum, Alignment.ALIGN_LEFT);
        }
        return (right - left) - indentWidth;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0108 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:111:0x01ed A[EDGE_INSN: B:111:0x01ed->B:52:0x01ed ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0087  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00c9  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00d9  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0116  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void drawText(android.graphics.Canvas r47, int r48, int r49) {
        /*
            Method dump skipped, instructions count: 839
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.Layout.drawText(android.graphics.Canvas, int, int):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void drawBackground(Canvas canvas, Path highlight, Paint highlightPaint, int cursorOffsetVertical, int firstLine, int lastLine) {
        int i;
        ParagraphStyle[] spans;
        int spanEnd;
        int spansLength;
        if (this.mSpannedText) {
            if (this.mLineBackgroundSpans == null) {
                this.mLineBackgroundSpans = new SpanSet<>(LineBackgroundSpan.class);
            }
            Spanned buffer = (Spanned) this.mText;
            int textLength = buffer.length();
            this.mLineBackgroundSpans.init(buffer, 0, textLength);
            if (this.mLineBackgroundSpans.numberOfSpans > 0) {
                int previousLineBottom = getLineTop(firstLine);
                int previousLineEnd = getLineStart(firstLine);
                ParagraphStyle[] spans2 = NO_PARA_SPANS;
                TextPaint paint = this.mPaint;
                int width = this.mWidth;
                int spanEnd2 = 0;
                int spanEnd3 = 0;
                int spansLength2 = previousLineEnd;
                int previousLineEnd2 = previousLineBottom;
                int i2 = firstLine;
                while (i2 <= lastLine) {
                    int end = getLineStart(i2 + 1);
                    int ltop = previousLineEnd2;
                    int lbottom = getLineTop(i2 + 1);
                    int previousLineBottom2 = getLineDescent(i2);
                    int lbaseline = lbottom - previousLineBottom2;
                    int start = spansLength2;
                    if (start >= spanEnd2) {
                        int spanEnd4 = this.mLineBackgroundSpans.getNextTransition(start, textLength);
                        int spansLength3 = 0;
                        if (start != end || start == 0) {
                            ParagraphStyle[] spans3 = spans2;
                            int j = 0;
                            while (true) {
                                i = i2;
                                int i3 = this.mLineBackgroundSpans.numberOfSpans;
                                if (j >= i3) {
                                    break;
                                }
                                if (this.mLineBackgroundSpans.spanStarts[j] < end && this.mLineBackgroundSpans.spanEnds[j] > start) {
                                    ParagraphStyle[] spans4 = (ParagraphStyle[]) GrowingArrayUtils.append((LineBackgroundSpan[]) spans3, spansLength3, this.mLineBackgroundSpans.spans[j]);
                                    spansLength3++;
                                    spans3 = spans4;
                                }
                                j++;
                                i2 = i;
                            }
                            spanEnd = spanEnd4;
                            spans = spans3;
                        } else {
                            i = i2;
                            spanEnd = spanEnd4;
                            spans = spans2;
                        }
                        spansLength = spansLength3;
                    } else {
                        i = i2;
                        spans = spans2;
                        spanEnd = spanEnd2;
                        spansLength = spanEnd3;
                    }
                    int n = 0;
                    while (true) {
                        int n2 = n;
                        if (n2 < spansLength) {
                            LineBackgroundSpan lineBackgroundSpan = (LineBackgroundSpan) spans[n2];
                            int start2 = start;
                            int n3 = width;
                            int end2 = end;
                            lineBackgroundSpan.drawBackground(canvas, paint, 0, n3, ltop, lbaseline, lbottom, buffer, start2, end2, i);
                            n = n2 + 1;
                            end = end2;
                            start = start2;
                            spansLength = spansLength;
                            width = width;
                            paint = paint;
                            textLength = textLength;
                            buffer = buffer;
                        }
                    }
                    int spansLength4 = spansLength;
                    i2 = i + 1;
                    spans2 = spans;
                    spansLength2 = end;
                    previousLineEnd2 = lbottom;
                    spanEnd2 = spanEnd;
                    spanEnd3 = spansLength4;
                }
            }
            this.mLineBackgroundSpans.recycle();
        }
        if (highlight != null) {
            if (cursorOffsetVertical != 0) {
                canvas.translate(0.0f, cursorOffsetVertical);
            }
            canvas.drawPath(highlight, highlightPaint);
            if (cursorOffsetVertical != 0) {
                canvas.translate(0.0f, -cursorOffsetVertical);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getLineRangeForDraw(Canvas canvas) {
        synchronized (sTempRect) {
            if (!canvas.getClipBounds(sTempRect)) {
                return TextUtils.packRangeInLong(0, -1);
            }
            int dtop = sTempRect.top;
            int dbottom = sTempRect.bottom;
            int top = Math.max(dtop, 0);
            int bottom = Math.min(getLineTop(getLineCount()), dbottom);
            return top >= bottom ? TextUtils.packRangeInLong(0, -1) : TextUtils.packRangeInLong(getLineForVertical(top), getLineForVertical(bottom));
        }
    }

    private synchronized int getLineStartPos(int line, int left, int right) {
        int x;
        Alignment align = getParagraphAlignment(line);
        int dir = getParagraphDirection(line);
        if (align == Alignment.ALIGN_LEFT) {
            align = dir == 1 ? Alignment.ALIGN_NORMAL : Alignment.ALIGN_OPPOSITE;
        } else if (align == Alignment.ALIGN_RIGHT) {
            align = dir == 1 ? Alignment.ALIGN_OPPOSITE : Alignment.ALIGN_NORMAL;
        }
        if (align == Alignment.ALIGN_NORMAL) {
            if (dir == 1) {
                int x2 = getIndentAdjust(line, Alignment.ALIGN_LEFT) + left;
                return x2;
            }
            int x3 = getIndentAdjust(line, Alignment.ALIGN_RIGHT) + right;
            return x3;
        }
        TabStops tabStops = null;
        if (this.mSpannedText && getLineContainsTab(line)) {
            Spanned spanned = (Spanned) this.mText;
            int start = getLineStart(line);
            int spanEnd = spanned.nextSpanTransition(start, spanned.length(), TabStopSpan.class);
            TabStopSpan[] tabSpans = (TabStopSpan[]) getParagraphSpans(spanned, start, spanEnd, TabStopSpan.class);
            if (tabSpans.length > 0) {
                tabStops = new TabStops(20, tabSpans);
            }
        }
        int max = (int) getLineExtent(line, tabStops, false);
        if (align == Alignment.ALIGN_OPPOSITE) {
            if (dir == 1) {
                x = (right - max) + getIndentAdjust(line, Alignment.ALIGN_RIGHT);
            } else {
                int x4 = left - max;
                x = x4 + getIndentAdjust(line, Alignment.ALIGN_LEFT);
            }
            return x;
        }
        int x5 = ((left + right) - (max & (-2))) >> (1 + getIndentAdjust(line, Alignment.ALIGN_CENTER));
        return x5;
    }

    public final CharSequence getText() {
        return this.mText;
    }

    public final TextPaint getPaint() {
        return this.mPaint;
    }

    public final int getWidth() {
        return this.mWidth;
    }

    public int getEllipsizedWidth() {
        return this.mWidth;
    }

    public final void increaseWidthTo(int wid) {
        if (wid < this.mWidth) {
            throw new RuntimeException("attempted to reduce Layout width");
        }
        this.mWidth = wid;
    }

    public int getHeight() {
        return getLineTop(getLineCount());
    }

    public synchronized int getHeight(boolean cap) {
        return getHeight();
    }

    public final Alignment getAlignment() {
        return this.mAlignment;
    }

    public final float getSpacingMultiplier() {
        return this.mSpacingMult;
    }

    public final float getSpacingAdd() {
        return this.mSpacingAdd;
    }

    public final synchronized TextDirectionHeuristic getTextDirectionHeuristic() {
        return this.mTextDir;
    }

    public int getLineBounds(int line, Rect bounds) {
        if (bounds != null) {
            bounds.left = 0;
            bounds.top = getLineTop(line);
            bounds.right = this.mWidth;
            bounds.bottom = getLineTop(line + 1);
        }
        return getLineBaseline(line);
    }

    public synchronized int getHyphen(int line) {
        return 0;
    }

    public synchronized int getIndentAdjust(int line, Alignment alignment) {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isLevelBoundary(int offset) {
        int line = getLineForOffset(offset);
        Directions dirs = getLineDirections(line);
        if (dirs == DIRS_ALL_LEFT_TO_RIGHT || dirs == DIRS_ALL_RIGHT_TO_LEFT) {
            return false;
        }
        int[] runs = dirs.mDirections;
        int lineStart = getLineStart(line);
        int lineEnd = getLineEnd(line);
        if (offset == lineStart || offset == lineEnd) {
            int paraLevel = getParagraphDirection(line) == 1 ? 0 : 1;
            int runIndex = offset == lineStart ? 0 : runs.length - 2;
            return ((runs[runIndex + 1] >>> 26) & 63) != paraLevel;
        }
        int offset2 = offset - lineStart;
        for (int i = 0; i < runs.length; i += 2) {
            if (offset2 == runs[i]) {
                return true;
            }
        }
        return false;
    }

    public boolean isRtlCharAt(int offset) {
        int line = getLineForOffset(offset);
        Directions dirs = getLineDirections(line);
        if (dirs == DIRS_ALL_LEFT_TO_RIGHT) {
            return false;
        }
        if (dirs == DIRS_ALL_RIGHT_TO_LEFT) {
            return true;
        }
        int[] runs = dirs.mDirections;
        int lineStart = getLineStart(line);
        for (int i = 0; i < runs.length; i += 2) {
            int start = runs[i] + lineStart;
            int limit = (runs[i + 1] & RUN_LENGTH_MASK) + start;
            if (offset >= start && offset < limit) {
                int level = (runs[i + 1] >>> 26) & 63;
                return (level & 1) != 0;
            }
        }
        return false;
    }

    public synchronized long getRunRange(int offset) {
        int line = getLineForOffset(offset);
        Directions dirs = getLineDirections(line);
        if (dirs == DIRS_ALL_LEFT_TO_RIGHT || dirs == DIRS_ALL_RIGHT_TO_LEFT) {
            return TextUtils.packRangeInLong(0, getLineEnd(line));
        }
        int[] runs = dirs.mDirections;
        int lineStart = getLineStart(line);
        for (int i = 0; i < runs.length; i += 2) {
            int start = runs[i] + lineStart;
            int limit = (runs[i + 1] & RUN_LENGTH_MASK) + start;
            if (offset >= start && offset < limit) {
                return TextUtils.packRangeInLong(start, limit);
            }
        }
        int i2 = getLineEnd(line);
        return TextUtils.packRangeInLong(0, i2);
    }

    private synchronized boolean primaryIsTrailingPrevious(int offset) {
        int line = getLineForOffset(offset);
        int lineStart = getLineStart(line);
        int lineEnd = getLineEnd(line);
        int[] runs = getLineDirections(line).mDirections;
        int levelAt = -1;
        int i = 0;
        while (true) {
            if (i >= runs.length) {
                break;
            }
            int start = runs[i] + lineStart;
            int limit = (runs[i + 1] & RUN_LENGTH_MASK) + start;
            if (limit > lineEnd) {
                limit = lineEnd;
            }
            if (offset < start || offset >= limit) {
                i += 2;
            } else if (offset > start) {
                return false;
            } else {
                levelAt = (runs[i + 1] >>> 26) & 63;
            }
        }
        if (levelAt == -1) {
            levelAt = getParagraphDirection(line) == 1 ? 0 : 1;
        }
        int levelBefore = -1;
        if (offset == lineStart) {
            levelBefore = getParagraphDirection(line) == 1 ? 0 : 1;
        } else {
            int offset2 = offset - 1;
            int i2 = 0;
            while (true) {
                if (i2 < runs.length) {
                    int start2 = runs[i2] + lineStart;
                    int limit2 = (runs[i2 + 1] & RUN_LENGTH_MASK) + start2;
                    if (limit2 > lineEnd) {
                        limit2 = lineEnd;
                    }
                    if (offset2 < start2 || offset2 >= limit2) {
                        i2 += 2;
                    } else {
                        levelBefore = (runs[i2 + 1] >>> 26) & 63;
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        return levelBefore < levelAt;
    }

    private synchronized boolean[] primaryIsTrailingPreviousAllLineOffsets(int line) {
        byte b;
        int lineStart = getLineStart(line);
        int lineEnd = getLineEnd(line);
        int[] runs = getLineDirections(line).mDirections;
        boolean[] trailing = new boolean[(lineEnd - lineStart) + 1];
        byte[] level = new byte[(lineEnd - lineStart) + 1];
        for (int i = 0; i < runs.length; i += 2) {
            int limit = (runs[i + 1] & RUN_LENGTH_MASK) + runs[i] + lineStart;
            if (limit > lineEnd) {
                limit = lineEnd;
            }
            level[(limit - lineStart) - 1] = (byte) ((runs[i + 1] >>> 26) & 63);
        }
        for (int i2 = 0; i2 < runs.length; i2 += 2) {
            int start = runs[i2] + lineStart;
            byte currentLevel = (byte) ((runs[i2 + 1] >>> 26) & 63);
            int i3 = start - lineStart;
            if (start == lineStart) {
                b = getParagraphDirection(line) == 1 ? (byte) 0 : (byte) 1;
            } else {
                b = level[(start - lineStart) - 1];
            }
            trailing[i3] = currentLevel > b;
        }
        return trailing;
    }

    public float getPrimaryHorizontal(int offset) {
        return getPrimaryHorizontal(offset, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float getPrimaryHorizontal(int offset, boolean clamped) {
        boolean trailing = primaryIsTrailingPrevious(offset);
        return getHorizontal(offset, trailing, clamped);
    }

    public float getSecondaryHorizontal(int offset) {
        return getSecondaryHorizontal(offset, false);
    }

    private protected float getSecondaryHorizontal(int offset, boolean clamped) {
        boolean trailing = primaryIsTrailingPrevious(offset);
        return getHorizontal(offset, !trailing, clamped);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized float getHorizontal(int offset, boolean primary) {
        return primary ? getPrimaryHorizontal(offset) : getSecondaryHorizontal(offset);
    }

    private synchronized float getHorizontal(int offset, boolean trailing, boolean clamped) {
        int line = getLineForOffset(offset);
        return getHorizontal(offset, trailing, line, clamped);
    }

    private synchronized float getHorizontal(int offset, boolean trailing, int line, boolean clamped) {
        int start = getLineStart(line);
        int end = getLineEnd(line);
        int dir = getParagraphDirection(line);
        boolean hasTab = getLineContainsTab(line);
        Directions directions = getLineDirections(line);
        TabStops tabStops = null;
        if (hasTab && (this.mText instanceof Spanned)) {
            TabStopSpan[] tabs = (TabStopSpan[]) getParagraphSpans((Spanned) this.mText, start, end, TabStopSpan.class);
            if (tabs.length > 0) {
                tabStops = new TabStops(20, tabs);
            }
        }
        TextLine tl = TextLine.obtain();
        tl.set(this.mPaint, this.mText, start, end, dir, directions, hasTab, tabStops);
        float wid = tl.measure(offset - start, trailing, null);
        TextLine.recycle(tl);
        if (clamped && wid > this.mWidth) {
            wid = this.mWidth;
        }
        int left = getParagraphLeft(line);
        int right = getParagraphRight(line);
        return getLineStartPos(line, left, right) + wid;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized float[] getLineHorizontals(int line, boolean clamped, boolean primary) {
        int start = getLineStart(line);
        int end = getLineEnd(line);
        int dir = getParagraphDirection(line);
        boolean hasTab = getLineContainsTab(line);
        Directions directions = getLineDirections(line);
        TabStops tabStops = null;
        if (hasTab && (this.mText instanceof Spanned)) {
            TabStopSpan[] tabs = (TabStopSpan[]) getParagraphSpans((Spanned) this.mText, start, end, TabStopSpan.class);
            if (tabs.length > 0) {
                tabStops = new TabStops(20, tabs);
            }
        }
        TextLine tl = TextLine.obtain();
        tl.set(this.mPaint, this.mText, start, end, dir, directions, hasTab, tabStops);
        boolean[] trailings = primaryIsTrailingPreviousAllLineOffsets(line);
        if (!primary) {
            for (int offset = 0; offset < trailings.length; offset++) {
                trailings[offset] = !trailings[offset];
            }
        }
        float[] wid = tl.measureAllOffsets(trailings, null);
        TextLine.recycle(tl);
        if (clamped) {
            for (int offset2 = 0; offset2 <= wid.length; offset2++) {
                if (wid[offset2] > this.mWidth) {
                    wid[offset2] = this.mWidth;
                }
            }
        }
        int left = getParagraphLeft(line);
        int right = getParagraphRight(line);
        int lineStartPos = getLineStartPos(line, left, right);
        float[] horizontal = new float[(end - start) + 1];
        int offset3 = 0;
        while (true) {
            int offset4 = offset3;
            boolean[] trailings2 = trailings;
            if (offset4 < horizontal.length) {
                horizontal[offset4] = lineStartPos + wid[offset4];
                offset3 = offset4 + 1;
                trailings = trailings2;
            } else {
                return horizontal;
            }
        }
    }

    public float getLineLeft(int line) {
        int dir = getParagraphDirection(line);
        Alignment align = getParagraphAlignment(line);
        if (align == Alignment.ALIGN_LEFT) {
            return 0.0f;
        }
        if (align == Alignment.ALIGN_NORMAL) {
            if (dir == -1) {
                return getParagraphRight(line) - getLineMax(line);
            }
            return 0.0f;
        } else if (align == Alignment.ALIGN_RIGHT) {
            return this.mWidth - getLineMax(line);
        } else {
            if (align == Alignment.ALIGN_OPPOSITE) {
                if (dir == -1) {
                    return 0.0f;
                }
                return this.mWidth - getLineMax(line);
            }
            int left = getParagraphLeft(line);
            int right = getParagraphRight(line);
            int max = ((int) getLineMax(line)) & (-2);
            return (((right - left) - max) / 2) + left;
        }
    }

    public float getLineRight(int line) {
        int dir = getParagraphDirection(line);
        Alignment align = getParagraphAlignment(line);
        if (align == Alignment.ALIGN_LEFT) {
            return getParagraphLeft(line) + getLineMax(line);
        }
        if (align == Alignment.ALIGN_NORMAL) {
            if (dir == -1) {
                return this.mWidth;
            }
            return getParagraphLeft(line) + getLineMax(line);
        } else if (align == Alignment.ALIGN_RIGHT) {
            return this.mWidth;
        } else {
            if (align == Alignment.ALIGN_OPPOSITE) {
                if (dir == -1) {
                    return getLineMax(line);
                }
                return this.mWidth;
            }
            int left = getParagraphLeft(line);
            int right = getParagraphRight(line);
            int max = ((int) getLineMax(line)) & (-2);
            return right - (((right - left) - max) / 2);
        }
    }

    public float getLineMax(int line) {
        float margin = getParagraphLeadingMargin(line);
        float signedExtent = getLineExtent(line, false);
        return (signedExtent >= 0.0f ? signedExtent : -signedExtent) + margin;
    }

    public float getLineWidth(int line) {
        float margin = getParagraphLeadingMargin(line);
        float signedExtent = getLineExtent(line, true);
        return (signedExtent >= 0.0f ? signedExtent : -signedExtent) + margin;
    }

    private synchronized float getLineExtent(int line, boolean full) {
        int start = getLineStart(line);
        int end = full ? getLineEnd(line) : getLineVisibleEnd(line);
        boolean hasTabs = getLineContainsTab(line);
        TabStops tabStops = null;
        if (hasTabs && (this.mText instanceof Spanned)) {
            TabStopSpan[] tabs = (TabStopSpan[]) getParagraphSpans((Spanned) this.mText, start, end, TabStopSpan.class);
            if (tabs.length > 0) {
                tabStops = new TabStops(20, tabs);
            }
        }
        TabStops tabStops2 = tabStops;
        Directions directions = getLineDirections(line);
        if (directions == null) {
            return 0.0f;
        }
        int dir = getParagraphDirection(line);
        TextLine tl = TextLine.obtain();
        TextPaint paint = this.mWorkPaint;
        paint.set(this.mPaint);
        paint.setHyphenEdit(getHyphen(line));
        tl.set(paint, this.mText, start, end, dir, directions, hasTabs, tabStops2);
        if (isJustificationRequired(line)) {
            tl.justify(getJustifyWidth(line));
        }
        float width = tl.metrics(null);
        TextLine.recycle(tl);
        return width;
    }

    private synchronized float getLineExtent(int line, TabStops tabStops, boolean full) {
        int start = getLineStart(line);
        int end = full ? getLineEnd(line) : getLineVisibleEnd(line);
        boolean hasTabs = getLineContainsTab(line);
        Directions directions = getLineDirections(line);
        int dir = getParagraphDirection(line);
        TextLine tl = TextLine.obtain();
        TextPaint paint = this.mWorkPaint;
        paint.set(this.mPaint);
        paint.setHyphenEdit(getHyphen(line));
        tl.set(paint, this.mText, start, end, dir, directions, hasTabs, tabStops);
        if (isJustificationRequired(line)) {
            tl.justify(getJustifyWidth(line));
        }
        float width = tl.metrics(null);
        TextLine.recycle(tl);
        return width;
    }

    public int getLineForVertical(int vertical) {
        int high = getLineCount();
        int low = -1;
        while (high - low > 1) {
            int guess = (high + low) / 2;
            if (getLineTop(guess) > vertical) {
                high = guess;
            } else {
                low = guess;
            }
        }
        if (low < 0) {
            return 0;
        }
        return low;
    }

    public int getLineForOffset(int offset) {
        int high = getLineCount();
        int low = -1;
        while (high - low > 1) {
            int guess = (high + low) / 2;
            if (getLineStart(guess) > offset) {
                high = guess;
            } else {
                low = guess;
            }
        }
        if (low < 0) {
            return 0;
        }
        return low;
    }

    public int getOffsetForHorizontal(int line, float horiz) {
        return getOffsetForHorizontal(line, horiz, true);
    }

    public synchronized int getOffsetForHorizontal(int line, float horiz, boolean primary) {
        int max;
        int best;
        int best2;
        int low;
        Layout layout = this;
        int lineEndOffset = getLineEnd(line);
        int lineStartOffset = getLineStart(line);
        Directions dirs = getLineDirections(line);
        TextLine tl = TextLine.obtain();
        tl.set(layout.mPaint, layout.mText, lineStartOffset, lineEndOffset, getParagraphDirection(line), dirs, false, null);
        HorizontalMeasurementProvider horizontal = new HorizontalMeasurementProvider(line, primary);
        if (line == getLineCount() - 1) {
            max = lineEndOffset;
        } else {
            int max2 = lineEndOffset - lineStartOffset;
            max = tl.getOffsetToLeftRightOf(max2, !layout.isRtlCharAt(lineEndOffset - 1)) + lineStartOffset;
        }
        float bestdist = Math.abs(horizontal.get(lineStartOffset) - horiz);
        int guess = lineStartOffset;
        int best3 = 0;
        while (best3 < dirs.mDirections.length) {
            int here = dirs.mDirections[best3] + lineStartOffset;
            int there = (dirs.mDirections[best3 + 1] & RUN_LENGTH_MASK) + here;
            boolean isRtl = (dirs.mDirections[best3 + 1] & 67108864) != 0;
            int swap = isRtl ? -1 : 1;
            if (there > max) {
                there = max;
            }
            int i = 1;
            int high = (there - 1) + 1;
            int low2 = (here + 1) - 1;
            while (true) {
                best2 = guess;
                low = low2;
                int best4 = high - low;
                if (best4 <= i) {
                    break;
                }
                int guess2 = (high + low) / 2;
                int adguess = layout.getOffsetAtStartOf(guess2);
                int swap2 = swap;
                if (horizontal.get(adguess) * swap2 >= swap2 * horiz) {
                    high = guess2;
                    low2 = low;
                } else {
                    low2 = guess2;
                }
                swap = swap2;
                guess = best2;
                layout = this;
                i = 1;
            }
            if (low < here + 1) {
                int low3 = here + 1;
                low = low3;
            }
            if (low < there) {
                int aft = tl.getOffsetToLeftRightOf(low - lineStartOffset, isRtl) + lineStartOffset;
                int swap3 = tl.getOffsetToLeftRightOf(aft - lineStartOffset, !isRtl);
                int low4 = swap3 + lineStartOffset;
                if (low4 >= here && low4 < there) {
                    float dist = Math.abs(horizontal.get(low4) - horiz);
                    if (aft < there) {
                        float other = Math.abs(horizontal.get(aft) - horiz);
                        if (other < dist) {
                            dist = other;
                            low4 = aft;
                        }
                    }
                    if (dist < bestdist) {
                        bestdist = dist;
                        int best5 = low4;
                        best2 = best5;
                    }
                }
            }
            float dist2 = Math.abs(horizontal.get(here) - horiz);
            if (dist2 < bestdist) {
                guess = here;
                bestdist = dist2;
            } else {
                guess = best2;
            }
            best3 += 2;
            layout = this;
        }
        int best6 = guess;
        if (Math.abs(horizontal.get(max) - horiz) <= bestdist) {
            best = max;
        } else {
            best = best6;
        }
        TextLine.recycle(tl);
        return best;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class HorizontalMeasurementProvider {
        private float[] mHorizontals;
        private final int mLine;
        private int mLineStartOffset;
        private final boolean mPrimary;

        HorizontalMeasurementProvider(int line, boolean primary) {
            this.mLine = line;
            this.mPrimary = primary;
            init();
        }

        private synchronized void init() {
            Directions dirs = Layout.this.getLineDirections(this.mLine);
            if (dirs != Layout.DIRS_ALL_LEFT_TO_RIGHT) {
                this.mHorizontals = Layout.this.getLineHorizontals(this.mLine, false, this.mPrimary);
                this.mLineStartOffset = Layout.this.getLineStart(this.mLine);
            }
        }

        synchronized float get(int offset) {
            if (this.mHorizontals == null || offset < this.mLineStartOffset || offset >= this.mLineStartOffset + this.mHorizontals.length) {
                return Layout.this.getHorizontal(offset, this.mPrimary);
            }
            return this.mHorizontals[offset - this.mLineStartOffset];
        }
    }

    public final int getLineEnd(int line) {
        return getLineStart(line + 1);
    }

    public int getLineVisibleEnd(int line) {
        return getLineVisibleEnd(line, getLineStart(line), getLineStart(line + 1));
    }

    private synchronized int getLineVisibleEnd(int line, int start, int end) {
        CharSequence text = this.mText;
        if (line == getLineCount() - 1) {
            return end;
        }
        while (end > start) {
            char ch = text.charAt(end - 1);
            if (ch == '\n') {
                return end - 1;
            }
            if (!TextLine.isLineEndSpace(ch)) {
                break;
            }
            end--;
        }
        return end;
    }

    public final int getLineBottom(int line) {
        return getLineTop(line + 1);
    }

    public final synchronized int getLineBottomWithoutSpacing(int line) {
        return getLineTop(line + 1) - getLineExtra(line);
    }

    public final int getLineBaseline(int line) {
        return getLineTop(line + 1) - getLineDescent(line);
    }

    public final int getLineAscent(int line) {
        return getLineTop(line) - (getLineTop(line + 1) - getLineDescent(line));
    }

    public synchronized int getLineExtra(int line) {
        return 0;
    }

    public int getOffsetToLeftOf(int offset) {
        return getOffsetToLeftRightOf(offset, true);
    }

    public int getOffsetToRightOf(int offset) {
        return getOffsetToLeftRightOf(offset, false);
    }

    private synchronized int getOffsetToLeftRightOf(int caret, boolean toLeft) {
        boolean toLeft2 = toLeft;
        int line = getLineForOffset(caret);
        int lineStart = getLineStart(line);
        int lineEnd = getLineEnd(line);
        int lineDir = getParagraphDirection(line);
        boolean lineChanged = false;
        boolean advance = toLeft2 == (lineDir == -1);
        if (advance) {
            if (caret == lineEnd) {
                if (line >= getLineCount() - 1) {
                    return caret;
                }
                lineChanged = true;
                line++;
            }
        } else if (caret == lineStart) {
            if (line <= 0) {
                return caret;
            }
            lineChanged = true;
            line--;
        }
        if (lineChanged) {
            lineStart = getLineStart(line);
            lineEnd = getLineEnd(line);
            int newDir = getParagraphDirection(line);
            if (newDir != lineDir) {
                toLeft2 = !toLeft2;
                lineDir = newDir;
            }
        }
        Directions directions = getLineDirections(line);
        TextLine tl = TextLine.obtain();
        tl.set(this.mPaint, this.mText, lineStart, lineEnd, lineDir, directions, false, null);
        int caret2 = tl.getOffsetToLeftRightOf(caret - lineStart, toLeft2) + lineStart;
        TextLine.recycle(tl);
        return caret2;
    }

    private synchronized int getOffsetAtStartOf(int offset) {
        char c1;
        if (offset == 0) {
            return 0;
        }
        CharSequence text = this.mText;
        char c = text.charAt(offset);
        if (c >= 56320 && c <= 57343 && (c1 = text.charAt(offset - 1)) >= 55296 && c1 <= 56319) {
            offset--;
        }
        if (this.mSpannedText) {
            ReplacementSpan[] spans = (ReplacementSpan[]) ((Spanned) text).getSpans(offset, offset, ReplacementSpan.class);
            for (int i = 0; i < spans.length; i++) {
                int start = ((Spanned) text).getSpanStart(spans[i]);
                int end = ((Spanned) text).getSpanEnd(spans[i]);
                if (start < offset && end > offset) {
                    offset = start;
                }
            }
        }
        return offset;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldClampCursor(int line) {
        switch (getParagraphAlignment(line)) {
            case ALIGN_LEFT:
                return true;
            case ALIGN_NORMAL:
                return getParagraphDirection(line) > 0;
            default:
                return false;
        }
    }

    public void getCursorPath(int point, Path dest, CharSequence editingBuffer) {
        dest.reset();
        int line = getLineForOffset(point);
        int top = getLineTop(line);
        int bottom = getLineBottomWithoutSpacing(line);
        boolean clamped = shouldClampCursor(line);
        float h1 = getPrimaryHorizontal(point, clamped) - 0.5f;
        float h2 = isLevelBoundary(point) ? getSecondaryHorizontal(point, clamped) - 0.5f : h1;
        int caps = TextKeyListener.getMetaState(editingBuffer, 1) | TextKeyListener.getMetaState(editingBuffer, 2048);
        int fn = TextKeyListener.getMetaState(editingBuffer, 2);
        int dist = 0;
        if (caps != 0 || fn != 0) {
            dist = (bottom - top) >> 2;
            if (fn != 0) {
                top += dist;
            }
            if (caps != 0) {
                bottom -= dist;
            }
        }
        if (h1 < 0.5f) {
            h1 = 0.5f;
        }
        if (h2 < 0.5f) {
            h2 = 0.5f;
        }
        if (Float.compare(h1, h2) == 0) {
            dest.moveTo(h1, top);
            dest.lineTo(h1, bottom);
        } else {
            dest.moveTo(h1, top);
            dest.lineTo(h1, (top + bottom) >> 1);
            dest.moveTo(h2, (top + bottom) >> 1);
            dest.lineTo(h2, bottom);
        }
        if (caps == 2) {
            dest.moveTo(h2, bottom);
            dest.lineTo(h2 - dist, bottom + dist);
            dest.lineTo(h2, bottom);
            dest.lineTo(dist + h2, bottom + dist);
        } else if (caps == 1) {
            dest.moveTo(h2, bottom);
            dest.lineTo(h2 - dist, bottom + dist);
            dest.moveTo(h2 - dist, (bottom + dist) - 0.5f);
            dest.lineTo(dist + h2, (bottom + dist) - 0.5f);
            dest.moveTo(dist + h2, bottom + dist);
            dest.lineTo(h2, bottom);
        }
        if (fn == 2) {
            dest.moveTo(h1, top);
            dest.lineTo(h1 - dist, top - dist);
            dest.lineTo(h1, top);
            dest.lineTo(dist + h1, top - dist);
        } else if (fn == 1) {
            dest.moveTo(h1, top);
            dest.lineTo(h1 - dist, top - dist);
            dest.moveTo(h1 - dist, (top - dist) + 0.5f);
            dest.lineTo(dist + h1, (top - dist) + 0.5f);
            dest.moveTo(dist + h1, top - dist);
            dest.lineTo(h1, top);
        }
    }

    private synchronized void addSelection(int line, int start, int end, int top, int bottom, SelectionRectangleConsumer consumer) {
        int st;
        int en;
        int layout;
        Layout layout2 = this;
        int i = line;
        int linestart = getLineStart(line);
        int lineend = getLineEnd(line);
        Directions dirs = getLineDirections(line);
        if (lineend > linestart && layout2.mText.charAt(lineend - 1) == '\n') {
            lineend--;
        }
        boolean z = false;
        int i2 = 0;
        while (i2 < dirs.mDirections.length) {
            int here = dirs.mDirections[i2] + linestart;
            int there = (dirs.mDirections[i2 + 1] & RUN_LENGTH_MASK) + here;
            if (there > lineend) {
                there = lineend;
            }
            if (start <= there && end >= here && (st = Math.max(start, here)) != (en = Math.min(end, there))) {
                float h1 = layout2.getHorizontal(st, z, i, z);
                float h2 = layout2.getHorizontal(en, true, i, z);
                float left = Math.min(h1, h2);
                float right = Math.max(h1, h2);
                if ((dirs.mDirections[i2 + 1] & 67108864) == 0) {
                    layout = 1;
                } else {
                    layout = 0;
                }
                consumer.accept(left, top, right, bottom, layout);
            }
            i2 += 2;
            layout2 = this;
            i = line;
            z = false;
        }
    }

    public void getSelectionPath(int start, int end, final Path dest) {
        dest.reset();
        getSelection(start, end, new SelectionRectangleConsumer() { // from class: android.text.-$$Lambda$Layout$MzjK2UE2G8VG0asK8_KWY3gHAmY
            @Override // android.text.Layout.SelectionRectangleConsumer
            public final void accept(float f, float f2, float f3, float f4, int i) {
                Path.this.addRect(f, f2, f3, f4, Path.Direction.CW);
            }
        });
    }

    public final synchronized void getSelection(int start, int end, SelectionRectangleConsumer consumer) {
        int i = start;
        int end2 = end;
        if (i == end2) {
            return;
        }
        if (end2 < i) {
            end2 = i;
            i = end2;
        }
        int start2 = i;
        int end3 = end2;
        int startline = getLineForOffset(start2);
        int endline = getLineForOffset(end3);
        int top = getLineTop(startline);
        int bottom = getLineBottomWithoutSpacing(endline);
        if (startline == endline) {
            addSelection(startline, start2, end3, top, bottom, consumer);
            return;
        }
        float width = this.mWidth;
        addSelection(startline, start2, getLineEnd(startline), top, getLineBottom(startline), consumer);
        if (getParagraphDirection(startline) == -1) {
            consumer.accept(getLineLeft(startline), top, 0.0f, getLineBottom(startline), 0);
        } else {
            consumer.accept(getLineRight(startline), top, width, getLineBottom(startline), 1);
        }
        for (int i2 = startline + 1; i2 < endline; i2++) {
            int top2 = getLineTop(i2);
            int bottom2 = getLineBottom(i2);
            if (getParagraphDirection(i2) == -1) {
                consumer.accept(0.0f, top2, width, bottom2, 0);
            } else {
                consumer.accept(0.0f, top2, width, bottom2, 1);
            }
        }
        int top3 = getLineTop(endline);
        int bottom3 = getLineBottomWithoutSpacing(endline);
        addSelection(endline, getLineStart(endline), end3, top3, bottom3, consumer);
        if (getParagraphDirection(endline) == -1) {
            consumer.accept(width, top3, getLineRight(endline), bottom3, 0);
        } else {
            consumer.accept(0.0f, top3, getLineLeft(endline), bottom3, 1);
        }
    }

    public final Alignment getParagraphAlignment(int line) {
        Alignment align = this.mAlignment;
        if (this.mSpannedText) {
            Spanned sp = (Spanned) this.mText;
            AlignmentSpan[] spans = (AlignmentSpan[]) getParagraphSpans(sp, getLineStart(line), getLineEnd(line), AlignmentSpan.class);
            int spanLength = spans.length;
            if (spanLength > 0) {
                return spans[spanLength - 1].getAlignment();
            }
            return align;
        }
        return align;
    }

    public final int getParagraphLeft(int line) {
        int dir = getParagraphDirection(line);
        if (dir == -1 || !this.mSpannedText) {
            return 0;
        }
        return getParagraphLeadingMargin(line);
    }

    public final int getParagraphRight(int line) {
        int right = this.mWidth;
        int dir = getParagraphDirection(line);
        if (dir == 1 || !this.mSpannedText) {
            return right;
        }
        return right - getParagraphLeadingMargin(line);
    }

    private synchronized int getParagraphLeadingMargin(int line) {
        if (this.mSpannedText) {
            Spanned spanned = (Spanned) this.mText;
            int lineStart = getLineStart(line);
            int lineEnd = getLineEnd(line);
            int spanEnd = spanned.nextSpanTransition(lineStart, lineEnd, LeadingMarginSpan.class);
            LeadingMarginSpan[] spans = (LeadingMarginSpan[]) getParagraphSpans(spanned, lineStart, spanEnd, LeadingMarginSpan.class);
            if (spans.length == 0) {
                return 0;
            }
            int margin = 0;
            boolean useFirstLineMargin = lineStart == 0 || spanned.charAt(lineStart + (-1)) == '\n';
            boolean useFirstLineMargin2 = useFirstLineMargin;
            for (int i = 0; i < spans.length; i++) {
                if (spans[i] instanceof LeadingMarginSpan.LeadingMarginSpan2) {
                    int spStart = spanned.getSpanStart(spans[i]);
                    int spanLine = getLineForOffset(spStart);
                    int count = ((LeadingMarginSpan.LeadingMarginSpan2) spans[i]).getLeadingMarginLineCount();
                    useFirstLineMargin2 |= line < spanLine + count;
                }
            }
            for (LeadingMarginSpan span : spans) {
                margin += span.getLeadingMargin(useFirstLineMargin2);
            }
            return margin;
        }
        return 0;
    }

    private static synchronized float measurePara(TextPaint paint, CharSequence text, int start, int end, TextDirectionHeuristic textDir) {
        MeasuredParagraph mt;
        boolean hasTabs;
        int margin;
        TabStops tabStops;
        boolean hasTabs2;
        TextLine tl = TextLine.obtain();
        try {
            mt = MeasuredParagraph.buildForBidi(text, start, end, textDir, null);
            try {
                char[] chars = mt.getChars();
                int len = chars.length;
                Directions directions = mt.getDirections(0, len);
                int dir = mt.getParagraphDir();
                boolean hasTabs3 = false;
                TabStops tabStops2 = null;
                if (!(text instanceof Spanned)) {
                    hasTabs = false;
                    margin = 0;
                } else {
                    LeadingMarginSpan[] spans = (LeadingMarginSpan[]) getParagraphSpans((Spanned) text, start, end, LeadingMarginSpan.class);
                    int length = spans.length;
                    int margin2 = 0;
                    int margin3 = 0;
                    while (margin3 < length) {
                        LeadingMarginSpan lms = spans[margin3];
                        margin2 += lms.getLeadingMargin(true);
                        margin3++;
                        length = length;
                        hasTabs3 = hasTabs3;
                    }
                    hasTabs = hasTabs3;
                    margin = margin2;
                }
                int i = 0;
                while (true) {
                    int i2 = i;
                    if (i2 >= len) {
                        tabStops = null;
                        hasTabs2 = hasTabs;
                        break;
                    } else if (chars[i2] != '\t') {
                        i = i2 + 1;
                    } else if (!(text instanceof Spanned)) {
                        hasTabs2 = true;
                        tabStops = null;
                    } else {
                        Spanned spanned = (Spanned) text;
                        int spanEnd = spanned.nextSpanTransition(start, end, TabStopSpan.class);
                        TabStopSpan[] spans2 = (TabStopSpan[]) getParagraphSpans(spanned, start, spanEnd, TabStopSpan.class);
                        hasTabs2 = true;
                        if (spans2.length > 0) {
                            tabStops2 = new TabStops(20, spans2);
                        }
                        tabStops = tabStops2;
                    }
                }
                int margin4 = margin;
                tl.set(paint, text, start, end, dir, directions, hasTabs2, tabStops);
                float abs = margin4 + Math.abs(tl.metrics(null));
                TextLine.recycle(tl);
                if (mt != null) {
                    mt.recycle();
                }
                return abs;
            } catch (Throwable th) {
                th = th;
                TextLine.recycle(tl);
                if (mt != null) {
                    mt.recycle();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            mt = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class TabStops {
        private int mIncrement;
        private int mNumStops;
        private int[] mStops;

        synchronized TabStops(int increment, Object[] spans) {
            reset(increment, spans);
        }

        synchronized void reset(int increment, Object[] spans) {
            this.mIncrement = increment;
            int ns = 0;
            if (spans != null) {
                int[] stops = this.mStops;
                int[] stops2 = stops;
                int ns2 = 0;
                for (Object o : spans) {
                    if (o instanceof TabStopSpan) {
                        if (stops2 == null) {
                            stops2 = new int[10];
                        } else if (ns2 == stops2.length) {
                            int[] nstops = new int[ns2 * 2];
                            for (int i = 0; i < ns2; i++) {
                                nstops[i] = stops2[i];
                            }
                            stops2 = nstops;
                        }
                        stops2[ns2] = ((TabStopSpan) o).getTabStop();
                        ns2++;
                    }
                }
                if (ns2 > 1) {
                    Arrays.sort(stops2, 0, ns2);
                }
                if (stops2 != this.mStops) {
                    this.mStops = stops2;
                }
                ns = ns2;
            }
            this.mNumStops = ns;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized float nextTab(float h) {
            int ns = this.mNumStops;
            if (ns > 0) {
                int[] stops = this.mStops;
                for (int i = 0; i < ns; i++) {
                    int stop = stops[i];
                    if (stop > h) {
                        return stop;
                    }
                }
            }
            return nextDefaultStop(h, this.mIncrement);
        }

        public static synchronized float nextDefaultStop(float h, int inc) {
            return ((int) ((inc + h) / inc)) * inc;
        }
    }

    static synchronized float nextTab(CharSequence text, int start, int end, float h, Object[] tabs) {
        float nh = Float.MAX_VALUE;
        boolean alltabs = false;
        if (text instanceof Spanned) {
            if (tabs == null) {
                tabs = getParagraphSpans((Spanned) text, start, end, TabStopSpan.class);
                alltabs = true;
            }
            for (int i = 0; i < tabs.length; i++) {
                if (alltabs || (tabs[i] instanceof TabStopSpan)) {
                    int where = ((TabStopSpan) tabs[i]).getTabStop();
                    if (where < nh && where > h) {
                        nh = where;
                    }
                }
            }
            if (nh != Float.MAX_VALUE) {
                return nh;
            }
        }
        return ((int) ((h + 20.0f) / 20.0f)) * 20;
    }

    protected final boolean isSpanned() {
        return this.mSpannedText;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized <T> T[] getParagraphSpans(Spanned text, int start, int end, Class<T> type) {
        if (start == end && start > 0) {
            return (T[]) ArrayUtils.emptyArray(type);
        }
        if (text instanceof SpannableStringBuilder) {
            return (T[]) ((SpannableStringBuilder) text).getSpans(start, end, type, false);
        }
        return (T[]) text.getSpans(start, end, type);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void ellipsize(int start, int end, int line, char[] dest, int destoff, TextUtils.TruncateAt method) {
        char c;
        int ellipsisCount = getEllipsisCount(line);
        if (ellipsisCount == 0) {
            return;
        }
        int ellipsisStart = getEllipsisStart(line);
        int lineStart = getLineStart(line);
        String ellipsisString = TextUtils.getEllipsisString(method);
        int ellipsisStringLen = ellipsisString.length();
        boolean useEllipsisString = ellipsisCount >= ellipsisStringLen;
        for (int i = 0; i < ellipsisCount; i++) {
            if (useEllipsisString && i < ellipsisStringLen) {
                c = ellipsisString.charAt(i);
            } else {
                c = 65279;
            }
            int a = i + ellipsisStart + lineStart;
            if (start <= a && a < end) {
                dest[(destoff + a) - start] = c;
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class Directions {
        @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
        public int[] mDirections;

        @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
        public synchronized Directions(int[] dirs) {
            this.mDirections = dirs;
        }
    }

    /* loaded from: classes2.dex */
    static class Ellipsizer implements CharSequence, GetChars {
        Layout mLayout;
        TextUtils.TruncateAt mMethod;
        CharSequence mText;
        int mWidth;

        public synchronized Ellipsizer(CharSequence s) {
            this.mText = s;
        }

        @Override // java.lang.CharSequence
        public char charAt(int off) {
            char[] buf = TextUtils.obtain(1);
            getChars(off, off + 1, buf, 0);
            char ret = buf[0];
            TextUtils.recycle(buf);
            return ret;
        }

        @Override // android.text.GetChars
        public void getChars(int start, int end, char[] dest, int destoff) {
            int line1 = this.mLayout.getLineForOffset(start);
            int line2 = this.mLayout.getLineForOffset(end);
            TextUtils.getChars(this.mText, start, end, dest, destoff);
            for (int i = line1; i <= line2; i++) {
                this.mLayout.ellipsize(start, end, i, dest, destoff, this.mMethod);
            }
        }

        @Override // java.lang.CharSequence
        public int length() {
            return this.mText.length();
        }

        @Override // java.lang.CharSequence
        public CharSequence subSequence(int start, int end) {
            char[] s = new char[end - start];
            getChars(start, end, s, 0);
            return new String(s);
        }

        @Override // java.lang.CharSequence
        public String toString() {
            char[] s = new char[length()];
            getChars(0, length(), s, 0);
            return new String(s);
        }
    }

    /* loaded from: classes2.dex */
    static class SpannedEllipsizer extends Ellipsizer implements Spanned {
        private Spanned mSpanned;

        public synchronized SpannedEllipsizer(CharSequence display) {
            super(display);
            this.mSpanned = (Spanned) display;
        }

        @Override // android.text.Spanned
        public <T> T[] getSpans(int start, int end, Class<T> type) {
            return (T[]) this.mSpanned.getSpans(start, end, type);
        }

        @Override // android.text.Spanned
        public int getSpanStart(Object tag) {
            return this.mSpanned.getSpanStart(tag);
        }

        @Override // android.text.Spanned
        public int getSpanEnd(Object tag) {
            return this.mSpanned.getSpanEnd(tag);
        }

        @Override // android.text.Spanned
        public int getSpanFlags(Object tag) {
            return this.mSpanned.getSpanFlags(tag);
        }

        @Override // android.text.Spanned
        public int nextSpanTransition(int start, int limit, Class type) {
            return this.mSpanned.nextSpanTransition(start, limit, type);
        }

        @Override // android.text.Layout.Ellipsizer, java.lang.CharSequence
        public CharSequence subSequence(int start, int end) {
            char[] s = new char[end - start];
            getChars(start, end, s, 0);
            SpannableString ss = new SpannableString(new String(s));
            TextUtils.copySpansFrom(this.mSpanned, start, end, Object.class, ss, 0);
            return ss;
        }
    }
}
