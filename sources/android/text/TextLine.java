package android.text;

import android.annotation.UnsupportedAppUsage;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.style.CharacterStyle;
import android.text.style.MetricAffectingSpan;
import android.text.style.ReplacementSpan;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import java.util.ArrayList;

@VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
/* loaded from: classes2.dex */
public class TextLine {
    private static final boolean DEBUG = false;
    private static final char TAB_CHAR = '\t';
    private static final int TAB_INCREMENT = 20;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private static final TextLine[] sCached = new TextLine[3];
    private float mAddedWidthForJustify;
    private char[] mChars;
    private boolean mCharsValid;
    private PrecomputedText mComputed;
    private int mDir;
    private Layout.Directions mDirections;
    private int mEllipsisEnd;
    private int mEllipsisStart;
    private boolean mHasTabs;
    private boolean mIsJustifying;
    private int mLen;
    private TextPaint mPaint;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private Spanned mSpanned;
    private int mStart;
    private Layout.TabStops mTabs;
    @UnsupportedAppUsage
    private CharSequence mText;
    private final TextPaint mWorkPaint = new TextPaint();
    private final TextPaint mActivePaint = new TextPaint();
    @UnsupportedAppUsage
    private final SpanSet<MetricAffectingSpan> mMetricAffectingSpanSpanSet = new SpanSet<>(MetricAffectingSpan.class);
    @UnsupportedAppUsage
    private final SpanSet<CharacterStyle> mCharacterStyleSpanSet = new SpanSet<>(CharacterStyle.class);
    @UnsupportedAppUsage
    private final SpanSet<ReplacementSpan> mReplacementSpanSpanSet = new SpanSet<>(ReplacementSpan.class);
    private final DecorationInfo mDecorationInfo = new DecorationInfo();
    private final ArrayList<DecorationInfo> mDecorations = new ArrayList<>();

    @UnsupportedAppUsage
    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public static TextLine obtain() {
        synchronized (sCached) {
            int i = sCached.length;
            do {
                i--;
                if (i < 0) {
                    TextLine tl = new TextLine();
                    return tl;
                }
            } while (sCached[i] == null);
            TextLine tl2 = sCached[i];
            sCached[i] = null;
            return tl2;
        }
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public static TextLine recycle(TextLine tl) {
        tl.mText = null;
        tl.mPaint = null;
        tl.mDirections = null;
        tl.mSpanned = null;
        tl.mTabs = null;
        tl.mChars = null;
        tl.mComputed = null;
        tl.mMetricAffectingSpanSpanSet.recycle();
        tl.mCharacterStyleSpanSet.recycle();
        tl.mReplacementSpanSpanSet.recycle();
        synchronized (sCached) {
            int i = 0;
            while (true) {
                if (i >= sCached.length) {
                    break;
                } else if (sCached[i] != null) {
                    i++;
                } else {
                    sCached[i] = tl;
                    break;
                }
            }
        }
        return null;
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void set(TextPaint paint, CharSequence text, int start, int limit, int dir, Layout.Directions directions, boolean hasTabs, Layout.TabStops tabStops, int ellipsisStart, int ellipsisEnd) {
        this.mPaint = paint;
        this.mText = text;
        this.mStart = start;
        this.mLen = limit - start;
        this.mDir = dir;
        this.mDirections = directions;
        if (this.mDirections == null) {
            throw new IllegalArgumentException("Directions cannot be null");
        }
        this.mHasTabs = hasTabs;
        this.mSpanned = null;
        boolean hasReplacement = false;
        int i = 1;
        if (text instanceof Spanned) {
            this.mSpanned = (Spanned) text;
            this.mReplacementSpanSpanSet.init(this.mSpanned, start, limit);
            hasReplacement = this.mReplacementSpanSpanSet.numberOfSpans > 0;
        }
        this.mComputed = null;
        if (text instanceof PrecomputedText) {
            this.mComputed = (PrecomputedText) text;
            if (!this.mComputed.getParams().getTextPaint().equalsForTextMeasurement(paint)) {
                this.mComputed = null;
            }
        }
        this.mCharsValid = hasReplacement;
        if (this.mCharsValid) {
            char[] cArr = this.mChars;
            if (cArr == null || cArr.length < this.mLen) {
                this.mChars = ArrayUtils.newUnpaddedCharArray(this.mLen);
            }
            TextUtils.getChars(text, start, limit, this.mChars, 0);
            if (hasReplacement) {
                char[] chars = this.mChars;
                int i2 = start;
                while (i2 < limit) {
                    int inext = this.mReplacementSpanSpanSet.getNextTransition(i2, limit);
                    if (this.mReplacementSpanSpanSet.hasSpansIntersecting(i2, inext) && (i2 - start >= ellipsisEnd || inext - start <= ellipsisStart)) {
                        chars[i2 - start] = 65532;
                        int e = inext - start;
                        for (int j = (i2 - start) + i; j < e; j++) {
                            chars[j] = 65279;
                        }
                    }
                    i2 = inext;
                    i = 1;
                }
            }
        }
        this.mTabs = tabStops;
        this.mAddedWidthForJustify = 0.0f;
        this.mIsJustifying = false;
        this.mEllipsisStart = ellipsisStart != ellipsisEnd ? ellipsisStart : 0;
        this.mEllipsisEnd = ellipsisStart != ellipsisEnd ? ellipsisEnd : 0;
    }

    private char charAt(int i) {
        return this.mCharsValid ? this.mChars[i] : this.mText.charAt(this.mStart + i);
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public void justify(float justifyWidth) {
        int end = this.mLen;
        while (end > 0 && isLineEndSpace(this.mText.charAt((this.mStart + end) - 1))) {
            end--;
        }
        int spaces = countStretchableSpaces(0, end);
        if (spaces != 0) {
            float width = Math.abs(measure(end, false, null));
            this.mAddedWidthForJustify = (justifyWidth - width) / spaces;
            this.mIsJustifying = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void draw(Canvas c, float x, int top, int y, int bottom) {
        int runCount;
        int runCount2;
        float h = 0.0f;
        int j = this.mDirections.getRunCount();
        int runIndex = 0;
        while (runIndex < j) {
            int runStart = this.mDirections.getRunStart(runIndex);
            if (runStart <= this.mLen) {
                int runLimit = Math.min(this.mDirections.getRunLength(runIndex) + runStart, this.mLen);
                boolean runIsRtl = this.mDirections.isRunRtl(runIndex);
                float h2 = h;
                int segStart = runStart;
                int j2 = this.mHasTabs ? runStart : runLimit;
                while (j2 <= runLimit) {
                    if (j2 == runLimit || charAt(j2) == '\t') {
                        float f = x + h2;
                        boolean z = (runIndex == j + (-1) && j2 == this.mLen) ? false : true;
                        runCount = j;
                        runCount2 = j2;
                        h2 += drawRun(c, segStart, j2, runIsRtl, f, top, y, bottom, z);
                        if (runCount2 != runLimit) {
                            int i = this.mDir;
                            h2 = i * nextTab(i * h2);
                        }
                        segStart = runCount2 + 1;
                    } else {
                        runCount = j;
                        runCount2 = j2;
                    }
                    j2 = runCount2 + 1;
                    j = runCount;
                }
                int runCount3 = j;
                runIndex++;
                h = h2;
                j = runCount3;
            } else {
                return;
            }
        }
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public float metrics(Paint.FontMetricsInt fmi) {
        return measure(this.mLen, false, fmi);
    }

    public float measure(int offset, boolean trailing, Paint.FontMetricsInt fmi) {
        int runStart;
        if (offset > this.mLen) {
            throw new IndexOutOfBoundsException("offset(" + offset + ") should be less than line limit(" + this.mLen + ")");
        }
        int target = trailing ? offset - 1 : offset;
        if (target < 0) {
            return 0.0f;
        }
        float h = 0.0f;
        int runIndex = 0;
        while (runIndex < this.mDirections.getRunCount() && (runStart = this.mDirections.getRunStart(runIndex)) <= this.mLen) {
            int runLimit = Math.min(this.mDirections.getRunLength(runIndex) + runStart, this.mLen);
            boolean runIsRtl = this.mDirections.isRunRtl(runIndex);
            float h2 = h;
            int segStart = runStart;
            int j = this.mHasTabs ? runStart : runLimit;
            while (j <= runLimit) {
                if (j == runLimit || charAt(j) == '\t') {
                    boolean targetIsInThisSegment = target >= segStart && target < j;
                    boolean sameDirection = (this.mDir == -1) == runIsRtl;
                    if (targetIsInThisSegment && sameDirection) {
                        return measureRun(segStart, offset, j, runIsRtl, fmi) + h2;
                    }
                    float segmentWidth = measureRun(segStart, j, j, runIsRtl, fmi);
                    h2 += sameDirection ? segmentWidth : -segmentWidth;
                    if (targetIsInThisSegment) {
                        return measureRun(segStart, offset, j, runIsRtl, null) + h2;
                    }
                    if (j != runLimit) {
                        if (offset == j) {
                            return h2;
                        }
                        int i = this.mDir;
                        float h3 = i * nextTab(i * h2);
                        if (target != j) {
                            h2 = h3;
                        } else {
                            return h3;
                        }
                    }
                    segStart = j + 1;
                }
                j++;
            }
            runIndex++;
            h = h2;
        }
        return h;
    }

    @VisibleForTesting
    public float[] measureAllOffsets(boolean[] trailing, Paint.FontMetricsInt fmi) {
        int runStart;
        boolean z;
        boolean z2;
        int offset;
        float w;
        int i = this.mLen;
        float[] measurement = new float[i + 1];
        int[] target = new int[i + 1];
        for (int offset2 = 0; offset2 < target.length; offset2++) {
            target[offset2] = trailing[offset2] ? offset2 - 1 : offset2;
        }
        if (target[0] < 0) {
            measurement[0] = 0.0f;
        }
        float h = 0.0f;
        int runIndex = 0;
        while (runIndex < this.mDirections.getRunCount() && (runStart = this.mDirections.getRunStart(runIndex)) <= this.mLen) {
            int runLimit = Math.min(this.mDirections.getRunLength(runIndex) + runStart, this.mLen);
            boolean runIsRtl = this.mDirections.isRunRtl(runIndex);
            float h2 = h;
            int segStart = runStart;
            for (int j = this.mHasTabs ? runStart : runLimit; j <= runLimit; j++) {
                if (j == runLimit || charAt(j) == '\t') {
                    float oldh = h2;
                    if (this.mDir == -1) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (z == runIsRtl) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    boolean advance = z2;
                    int segStart2 = segStart;
                    float w2 = measureRun(segStart, j, j, runIsRtl, fmi);
                    h2 += advance ? w2 : -w2;
                    float baseh = advance ? oldh : h2;
                    Paint.FontMetricsInt crtfmi = advance ? fmi : null;
                    int offset3 = segStart2;
                    while (offset3 <= j && offset3 <= this.mLen) {
                        int segStart3 = segStart2;
                        if (target[offset3] < segStart3 || target[offset3] >= j) {
                            segStart2 = segStart3;
                            offset = offset3;
                            w = w2;
                        } else {
                            segStart2 = segStart3;
                            int segStart4 = offset3;
                            offset = offset3;
                            int offset4 = j;
                            w = w2;
                            measurement[offset] = baseh + measureRun(segStart3, segStart4, offset4, runIsRtl, crtfmi);
                        }
                        offset3 = offset + 1;
                        w2 = w;
                    }
                    if (j != runLimit) {
                        if (target[j] == j) {
                            measurement[j] = h2;
                        }
                        int i2 = this.mDir;
                        float h3 = i2 * nextTab(i2 * h2);
                        if (target[j + 1] == j) {
                            measurement[j + 1] = h3;
                        }
                        h2 = h3;
                    }
                    segStart = j + 1;
                }
            }
            runIndex++;
            h = h2;
        }
        int i3 = this.mLen;
        if (target[i3] == i3) {
            measurement[i3] = h;
        }
        return measurement;
    }

    private float drawRun(Canvas c, int start, int limit, boolean runIsRtl, float x, int top, int y, int bottom, boolean needWidth) {
        if ((this.mDir == 1) == runIsRtl) {
            float w = -measureRun(start, limit, limit, runIsRtl, null);
            handleRun(start, limit, limit, runIsRtl, c, x + w, top, y, bottom, null, false);
            return w;
        }
        return handleRun(start, limit, limit, runIsRtl, c, x, top, y, bottom, null, needWidth);
    }

    private float measureRun(int start, int offset, int limit, boolean runIsRtl, Paint.FontMetricsInt fmi) {
        return handleRun(start, offset, limit, runIsRtl, null, 0.0f, 0, 0, 0, fmi, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x0186, code lost:
        r1 = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x0189, code lost:
        if (r13 != (-1)) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x018b, code lost:
        if (r0 == false) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x018d, code lost:
        r1 = r28.mLen + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x0193, code lost:
        if (r13 > r11) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x0195, code lost:
        if (r0 == false) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x0197, code lost:
        r1 = r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x0199, code lost:
        r1 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x019b, code lost:
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:?, code lost:
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:138:?, code lost:
        return r13;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int getOffsetToLeftRightOf(int r29, boolean r30) {
        /*
            Method dump skipped, instructions count: 412
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.TextLine.getOffsetToLeftRightOf(int, boolean):int");
    }

    private int getOffsetBeforeAfter(int runIndex, int runStart, int runLimit, boolean runIsRtl, int offset, boolean after) {
        int i;
        int spanLimit;
        int spanStart;
        int spanLimit2;
        if (runIndex >= 0) {
            if (offset != (after ? this.mLen : 0)) {
                TextPaint wp = this.mWorkPaint;
                wp.set(this.mPaint);
                if (this.mIsJustifying) {
                    wp.setWordSpacing(this.mAddedWidthForJustify);
                }
                int spanStart2 = runStart;
                if (this.mSpanned == null) {
                    spanStart = spanStart2;
                    spanLimit2 = runLimit;
                } else {
                    int target = after ? offset + 1 : offset;
                    int limit = this.mStart + runLimit;
                    while (true) {
                        int nextSpanTransition = this.mSpanned.nextSpanTransition(this.mStart + spanStart2, limit, MetricAffectingSpan.class);
                        i = this.mStart;
                        spanLimit = nextSpanTransition - i;
                        if (spanLimit >= target) {
                            break;
                        }
                        spanStart2 = spanLimit;
                    }
                    MetricAffectingSpan[] spans = (MetricAffectingSpan[]) TextUtils.removeEmptySpans((MetricAffectingSpan[]) this.mSpanned.getSpans(i + spanStart2, i + spanLimit, MetricAffectingSpan.class), this.mSpanned, MetricAffectingSpan.class);
                    if (spans.length > 0) {
                        ReplacementSpan replacement = null;
                        for (MetricAffectingSpan span : spans) {
                            if (span instanceof ReplacementSpan) {
                                replacement = (ReplacementSpan) span;
                            } else {
                                span.updateMeasureState(wp);
                            }
                        }
                        if (replacement != null) {
                            return after ? spanLimit : spanStart2;
                        }
                    }
                    spanStart = spanStart2;
                    spanLimit2 = spanLimit;
                }
                int cursorOpt = after ? 0 : 2;
                if (this.mCharsValid) {
                    return wp.getTextRunCursor(this.mChars, spanStart, spanLimit2 - spanStart, runIsRtl, offset, cursorOpt);
                }
                CharSequence charSequence = this.mText;
                int i2 = this.mStart;
                return wp.getTextRunCursor(charSequence, i2 + spanStart, i2 + spanLimit2, runIsRtl, i2 + offset, cursorOpt) - this.mStart;
            }
        }
        return after ? TextUtils.getOffsetAfter(this.mText, this.mStart + offset) - this.mStart : TextUtils.getOffsetBefore(this.mText, this.mStart + offset) - this.mStart;
    }

    private static void expandMetricsFromPaint(Paint.FontMetricsInt fmi, TextPaint wp) {
        int previousTop = fmi.top;
        int previousAscent = fmi.ascent;
        int previousDescent = fmi.descent;
        int previousBottom = fmi.bottom;
        int previousLeading = fmi.leading;
        wp.getFontMetricsInt(fmi);
        updateMetrics(fmi, previousTop, previousAscent, previousDescent, previousBottom, previousLeading);
    }

    static void updateMetrics(Paint.FontMetricsInt fmi, int previousTop, int previousAscent, int previousDescent, int previousBottom, int previousLeading) {
        fmi.top = Math.min(fmi.top, previousTop);
        fmi.ascent = Math.min(fmi.ascent, previousAscent);
        fmi.descent = Math.max(fmi.descent, previousDescent);
        fmi.bottom = Math.max(fmi.bottom, previousBottom);
        fmi.leading = Math.max(fmi.leading, previousLeading);
    }

    private static void drawStroke(TextPaint wp, Canvas c, int color, float position, float thickness, float xleft, float xright, float baseline) {
        float strokeTop = baseline + wp.baselineShift + position;
        int previousColor = wp.getColor();
        Paint.Style previousStyle = wp.getStyle();
        boolean previousAntiAlias = wp.isAntiAlias();
        wp.setStyle(Paint.Style.FILL);
        wp.setAntiAlias(true);
        wp.setColor(color);
        c.drawRect(xleft, strokeTop, xright, strokeTop + thickness, wp);
        wp.setStyle(previousStyle);
        wp.setColor(previousColor);
        wp.setAntiAlias(previousAntiAlias);
    }

    private float getRunAdvance(TextPaint wp, int start, int end, int contextStart, int contextEnd, boolean runIsRtl, int offset) {
        if (this.mCharsValid) {
            return wp.getRunAdvance(this.mChars, start, end, contextStart, contextEnd, runIsRtl, offset);
        }
        int delta = this.mStart;
        PrecomputedText precomputedText = this.mComputed;
        if (precomputedText == null) {
            return wp.getRunAdvance(this.mText, delta + start, delta + end, delta + contextStart, delta + contextEnd, runIsRtl, delta + offset);
        }
        return precomputedText.getWidth(start + delta, end + delta);
    }

    private float handleText(TextPaint wp, int start, int end, int contextStart, int contextEnd, boolean runIsRtl, Canvas c, float x, int top, int y, int bottom, Paint.FontMetricsInt fmi, boolean needWidth, int offset, ArrayList<DecorationInfo> decorations) {
        int numDecorations;
        float totalWidth;
        float leftX;
        float rightX;
        float decorationXLeft;
        float decorationXRight;
        int numDecorations2;
        int numDecorations3;
        if (this.mIsJustifying) {
            wp.setWordSpacing(this.mAddedWidthForJustify);
        }
        if (fmi != null) {
            expandMetricsFromPaint(fmi, wp);
        }
        if (end == start) {
            return 0.0f;
        }
        float totalWidth2 = 0.0f;
        int numDecorations4 = decorations == null ? 0 : decorations.size();
        if (needWidth || (c != null && (wp.bgColor != 0 || numDecorations4 != 0 || runIsRtl))) {
            numDecorations = numDecorations4;
            totalWidth2 = getRunAdvance(wp, start, end, contextStart, contextEnd, runIsRtl, offset);
        } else {
            numDecorations = numDecorations4;
        }
        if (c == null) {
            totalWidth = totalWidth2;
        } else {
            if (runIsRtl) {
                float leftX2 = x - totalWidth2;
                leftX = leftX2;
                rightX = x;
            } else {
                leftX = x;
                rightX = x + totalWidth2;
            }
            if (wp.bgColor != 0) {
                int previousColor = wp.getColor();
                Paint.Style previousStyle = wp.getStyle();
                wp.setColor(wp.bgColor);
                wp.setStyle(Paint.Style.FILL);
                c.drawRect(leftX, top, rightX, bottom, wp);
                wp.setStyle(previousStyle);
                wp.setColor(previousColor);
            }
            totalWidth = totalWidth2;
            float totalWidth3 = leftX;
            drawTextRun(c, wp, start, end, contextStart, contextEnd, runIsRtl, totalWidth3, y + wp.baselineShift);
            if (numDecorations != 0) {
                int i = 0;
                while (i < numDecorations) {
                    DecorationInfo info = decorations.get(i);
                    int decorationStart = Math.max(info.start, start);
                    int decorationEnd = Math.min(info.end, offset);
                    float decorationStartAdvance = getRunAdvance(wp, start, end, contextStart, contextEnd, runIsRtl, decorationStart);
                    float decorationEndAdvance = getRunAdvance(wp, start, end, contextStart, contextEnd, runIsRtl, decorationEnd);
                    if (runIsRtl) {
                        float decorationXLeft2 = rightX - decorationEndAdvance;
                        decorationXLeft = decorationXLeft2;
                        decorationXRight = rightX - decorationStartAdvance;
                    } else {
                        float decorationXLeft3 = leftX + decorationStartAdvance;
                        decorationXLeft = decorationXLeft3;
                        decorationXRight = leftX + decorationEndAdvance;
                    }
                    if (info.underlineColor != 0) {
                        drawStroke(wp, c, info.underlineColor, wp.getUnderlinePosition(), info.underlineThickness, decorationXLeft, decorationXRight, y);
                    }
                    if (!info.isUnderlineText) {
                        numDecorations2 = numDecorations;
                        numDecorations3 = 1065353216;
                    } else {
                        float thickness = Math.max(wp.getUnderlineThickness(), 1.0f);
                        numDecorations2 = numDecorations;
                        numDecorations3 = 1065353216;
                        drawStroke(wp, c, wp.getColor(), wp.getUnderlinePosition(), thickness, decorationXLeft, decorationXRight, y);
                    }
                    if (info.isStrikeThruText) {
                        float thickness2 = Math.max(wp.getStrikeThruThickness(), (float) numDecorations3);
                        drawStroke(wp, c, wp.getColor(), wp.getStrikeThruPosition(), thickness2, decorationXLeft, decorationXRight, y);
                    }
                    i++;
                    numDecorations = numDecorations2;
                }
            }
        }
        return runIsRtl ? -totalWidth : totalWidth;
    }

    private float handleReplacement(ReplacementSpan replacement, TextPaint wp, int start, int limit, boolean runIsRtl, Canvas c, float x, int top, int y, int bottom, Paint.FontMetricsInt fmi, boolean needWidth) {
        int previousTop;
        int previousAscent;
        int previousDescent;
        int previousBottom;
        int previousLeading;
        float x2;
        float ret = 0.0f;
        int i = this.mStart;
        int textStart = i + start;
        int textLimit = i + limit;
        if (needWidth || (c != null && runIsRtl)) {
            boolean needUpdateMetrics = fmi != null;
            if (!needUpdateMetrics) {
                previousTop = 0;
                previousAscent = 0;
                previousDescent = 0;
                previousBottom = 0;
                previousLeading = 0;
            } else {
                int previousTop2 = fmi.top;
                int previousAscent2 = fmi.ascent;
                int previousDescent2 = fmi.descent;
                int previousBottom2 = fmi.bottom;
                int previousLeading2 = fmi.leading;
                previousTop = previousTop2;
                previousAscent = previousAscent2;
                previousDescent = previousDescent2;
                previousBottom = previousBottom2;
                previousLeading = previousLeading2;
            }
            ret = replacement.getSize(wp, this.mText, textStart, textLimit, fmi);
            if (needUpdateMetrics) {
                updateMetrics(fmi, previousTop, previousAscent, previousDescent, previousBottom, previousLeading);
            }
        }
        float ret2 = ret;
        if (c != null) {
            if (!runIsRtl) {
                x2 = x;
            } else {
                x2 = x - ret2;
            }
            replacement.draw(c, this.mText, textStart, textLimit, x2, top, y, bottom, wp);
        }
        return runIsRtl ? -ret2 : ret2;
    }

    private int adjustStartHyphenEdit(int start, int startHyphenEdit) {
        if (start > 0) {
            return 0;
        }
        return startHyphenEdit;
    }

    private int adjustEndHyphenEdit(int limit, int endHyphenEdit) {
        if (limit < this.mLen) {
            return 0;
        }
        return endHyphenEdit;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class DecorationInfo {
        public int end;
        public boolean isStrikeThruText;
        public boolean isUnderlineText;
        public int start;
        public int underlineColor;
        public float underlineThickness;

        private DecorationInfo() {
            this.start = -1;
            this.end = -1;
        }

        public boolean hasDecoration() {
            return this.isStrikeThruText || this.isUnderlineText || this.underlineColor != 0;
        }

        public DecorationInfo copyInfo() {
            DecorationInfo copy = new DecorationInfo();
            copy.isStrikeThruText = this.isStrikeThruText;
            copy.isUnderlineText = this.isUnderlineText;
            copy.underlineColor = this.underlineColor;
            copy.underlineThickness = this.underlineThickness;
            return copy;
        }
    }

    private void extractDecorationInfo(TextPaint paint, DecorationInfo info) {
        info.isStrikeThruText = paint.isStrikeThruText();
        if (info.isStrikeThruText) {
            paint.setStrikeThruText(false);
        }
        info.isUnderlineText = paint.isUnderlineText();
        if (info.isUnderlineText) {
            paint.setUnderlineText(false);
        }
        info.underlineColor = paint.underlineColor;
        info.underlineThickness = paint.underlineThickness;
        paint.setUnderlineText(0, 0.0f);
    }

    /* JADX WARN: Removed duplicated region for block: B:88:0x026f  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0281  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private float handleRun(int r36, int r37, int r38, boolean r39, android.graphics.Canvas r40, float r41, int r42, int r43, int r44, android.graphics.Paint.FontMetricsInt r45, boolean r46) {
        /*
            Method dump skipped, instructions count: 837
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.TextLine.handleRun(int, int, int, boolean, android.graphics.Canvas, float, int, int, int, android.graphics.Paint$FontMetricsInt, boolean):float");
    }

    private void drawTextRun(Canvas c, TextPaint wp, int start, int end, int contextStart, int contextEnd, boolean runIsRtl, float x, int y) {
        if (this.mCharsValid) {
            int count = end - start;
            int contextCount = contextEnd - contextStart;
            c.drawTextRun(this.mChars, start, count, contextStart, contextCount, x, y, runIsRtl, wp);
            return;
        }
        int delta = this.mStart;
        c.drawTextRun(this.mText, delta + start, delta + end, delta + contextStart, delta + contextEnd, x, y, runIsRtl, wp);
    }

    float nextTab(float h) {
        Layout.TabStops tabStops = this.mTabs;
        if (tabStops != null) {
            return tabStops.nextTab(h);
        }
        return Layout.TabStops.nextDefaultStop(h, 20.0f);
    }

    private boolean isStretchableWhitespace(int ch) {
        return ch == 32;
    }

    private int countStretchableSpaces(int start, int end) {
        int count = 0;
        for (int i = start; i < end; i++) {
            char c = this.mCharsValid ? this.mChars[i] : this.mText.charAt(this.mStart + i);
            if (isStretchableWhitespace(c)) {
                count++;
            }
        }
        return count;
    }

    public static boolean isLineEndSpace(char ch) {
        return ch == ' ' || ch == '\t' || ch == 5760 || (8192 <= ch && ch <= 8202 && ch != 8199) || ch == 8287 || ch == 12288;
    }

    private static boolean equalAttributes(TextPaint lp, TextPaint rp) {
        return lp.getColorFilter() == rp.getColorFilter() && lp.getMaskFilter() == rp.getMaskFilter() && lp.getShader() == rp.getShader() && lp.getTypeface() == rp.getTypeface() && lp.getXfermode() == rp.getXfermode() && lp.getTextLocales().equals(rp.getTextLocales()) && TextUtils.equals(lp.getFontFeatureSettings(), rp.getFontFeatureSettings()) && TextUtils.equals(lp.getFontVariationSettings(), rp.getFontVariationSettings()) && lp.getShadowLayerRadius() == rp.getShadowLayerRadius() && lp.getShadowLayerDx() == rp.getShadowLayerDx() && lp.getShadowLayerDy() == rp.getShadowLayerDy() && lp.getShadowLayerColor() == rp.getShadowLayerColor() && lp.getFlags() == rp.getFlags() && lp.getHinting() == rp.getHinting() && lp.getStyle() == rp.getStyle() && lp.getColor() == rp.getColor() && lp.getStrokeWidth() == rp.getStrokeWidth() && lp.getStrokeMiter() == rp.getStrokeMiter() && lp.getStrokeCap() == rp.getStrokeCap() && lp.getStrokeJoin() == rp.getStrokeJoin() && lp.getTextAlign() == rp.getTextAlign() && lp.isElegantTextHeight() == rp.isElegantTextHeight() && lp.getTextSize() == rp.getTextSize() && lp.getTextScaleX() == rp.getTextScaleX() && lp.getTextSkewX() == rp.getTextSkewX() && lp.getLetterSpacing() == rp.getLetterSpacing() && lp.getWordSpacing() == rp.getWordSpacing() && lp.getStartHyphenEdit() == rp.getStartHyphenEdit() && lp.getEndHyphenEdit() == rp.getEndHyphenEdit() && lp.bgColor == rp.bgColor && lp.baselineShift == rp.baselineShift && lp.linkColor == rp.linkColor && lp.drawableState == rp.drawableState && lp.density == rp.density && lp.underlineColor == rp.underlineColor && lp.underlineThickness == rp.underlineThickness;
    }
}
