package android.text;

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
    private static final int TAB_INCREMENT = 20;
    public protected static final TextLine[] sCached = new TextLine[3];
    private float mAddedWidth;
    private char[] mChars;
    private boolean mCharsValid;
    private PrecomputedText mComputed;
    private int mDir;
    private Layout.Directions mDirections;
    private boolean mHasTabs;
    private int mLen;
    private TextPaint mPaint;
    public protected Spanned mSpanned;
    private int mStart;
    private Layout.TabStops mTabs;
    public protected CharSequence mText;
    private final TextPaint mWorkPaint = new TextPaint();
    private final TextPaint mActivePaint = new TextPaint();
    public protected final SpanSet<MetricAffectingSpan> mMetricAffectingSpanSpanSet = new SpanSet<>(MetricAffectingSpan.class);
    public protected final SpanSet<CharacterStyle> mCharacterStyleSpanSet = new SpanSet<>(CharacterStyle.class);
    public protected final SpanSet<ReplacementSpan> mReplacementSpanSpanSet = new SpanSet<>(ReplacementSpan.class);
    private final DecorationInfo mDecorationInfo = new DecorationInfo();
    private final ArrayList<DecorationInfo> mDecorations = new ArrayList<>();

    /* JADX INFO: Access modifiers changed from: private */
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
    public static synchronized TextLine recycle(TextLine tl) {
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
    public synchronized void set(TextPaint paint, CharSequence text, int start, int limit, int dir, Layout.Directions directions, boolean hasTabs, Layout.TabStops tabStops) {
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
        this.mCharsValid = hasReplacement || hasTabs || directions != Layout.DIRS_ALL_LEFT_TO_RIGHT;
        if (this.mCharsValid) {
            if (this.mChars == null || this.mChars.length < this.mLen) {
                this.mChars = ArrayUtils.newUnpaddedCharArray(this.mLen);
            }
            TextUtils.getChars(text, start, limit, this.mChars, 0);
            if (hasReplacement) {
                char[] chars = this.mChars;
                int i = start;
                while (i < limit) {
                    int inext = this.mReplacementSpanSpanSet.getNextTransition(i, limit);
                    if (this.mReplacementSpanSpanSet.hasSpansIntersecting(i, inext)) {
                        chars[i - start] = 65532;
                        int e = inext - start;
                        for (int j = (i - start) + 1; j < e; j++) {
                            chars[j] = 65279;
                        }
                    }
                    i = inext;
                }
            }
        }
        this.mTabs = tabStops;
        this.mAddedWidth = 0.0f;
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public synchronized void justify(float justifyWidth) {
        int end = this.mLen;
        while (end > 0 && isLineEndSpace(this.mText.charAt((this.mStart + end) - 1))) {
            end--;
        }
        int spaces = countStretchableSpaces(0, end);
        if (spaces != 0) {
            float width = Math.abs(measure(end, false, null));
            this.mAddedWidth = (justifyWidth - width) / spaces;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void draw(Canvas c, float x, int top, int y, int bottom) {
        int runLimit;
        if (!this.mHasTabs) {
            if (this.mDirections == Layout.DIRS_ALL_LEFT_TO_RIGHT) {
                drawRun(c, 0, this.mLen, false, x, top, y, bottom, false);
                return;
            } else if (this.mDirections == Layout.DIRS_ALL_RIGHT_TO_LEFT) {
                drawRun(c, 0, this.mLen, true, x, top, y, bottom, false);
                return;
            }
        }
        int[] runs = this.mDirections.mDirections;
        int lastRunIndex = runs.length - 2;
        float h = 0.0f;
        int i = 0;
        while (true) {
            int i2 = i;
            int i3 = runs.length;
            if (i2 < i3) {
                int runStart = runs[i2];
                int runLimit2 = (runs[i2 + 1] & 67108863) + runStart;
                if (runLimit2 > this.mLen) {
                    runLimit2 = this.mLen;
                }
                int runLimit3 = runLimit2;
                boolean runIsRtl = (runs[i2 + 1] & 67108864) != 0;
                int j = this.mHasTabs ? runStart : runLimit3;
                int segstart = runStart;
                float h2 = h;
                while (true) {
                    int j2 = j;
                    if (j2 <= runLimit3) {
                        int codept = 0;
                        codept = 0;
                        if (this.mHasTabs && j2 < runLimit3) {
                            char c2 = this.mChars[j2];
                            codept = c2;
                            if (c2 >= 55296) {
                                codept = c2;
                                if (c2 < 56320) {
                                    codept = c2;
                                    if (j2 + 1 < runLimit3) {
                                        int codept2 = Character.codePointAt(this.mChars, j2);
                                        codept = codept2;
                                        if (codept2 > 65535) {
                                            j2++;
                                            runLimit = runLimit3;
                                            j = j2 + 1;
                                            runLimit3 = runLimit;
                                        }
                                    }
                                }
                            }
                        }
                        int codept3 = codept;
                        if (j2 == runLimit3 || codept3 == 9) {
                            runLimit = runLimit3;
                            h2 += drawRun(c, segstart, j2, runIsRtl, x + h2, top, y, bottom, (i2 == lastRunIndex && j2 == this.mLen) ? false : true);
                            if (codept3 == 9) {
                                h2 = this.mDir * nextTab(this.mDir * h2);
                            }
                            segstart = j2 + 1;
                            j2 = j2;
                            j = j2 + 1;
                            runLimit3 = runLimit;
                        }
                        runLimit = runLimit3;
                        j = j2 + 1;
                        runLimit3 = runLimit;
                    }
                }
                i = i2 + 2;
                h = h2;
            } else {
                return;
            }
        }
    }

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    public synchronized float metrics(Paint.FontMetricsInt fmi) {
        return measure(this.mLen, false, fmi);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized float measure(int offset, boolean trailing, Paint.FontMetricsInt fmi) {
        float h;
        boolean runIsRtl;
        int runLimit;
        int j;
        int target = trailing ? offset - 1 : offset;
        if (target < 0) {
            return 0.0f;
        }
        float h2 = 0.0f;
        if (!this.mHasTabs) {
            if (this.mDirections == Layout.DIRS_ALL_LEFT_TO_RIGHT) {
                return measureRun(0, offset, this.mLen, false, fmi);
            }
            if (this.mDirections == Layout.DIRS_ALL_RIGHT_TO_LEFT) {
                return measureRun(0, offset, this.mLen, true, fmi);
            }
        }
        char[] chars = this.mChars;
        int[] runs = this.mDirections.mDirections;
        int i = 0;
        while (true) {
            int i2 = i;
            int i3 = runs.length;
            if (i2 < i3) {
                int runStart = runs[i2];
                int runLimit2 = (runs[i2 + 1] & 67108863) + runStart;
                if (runLimit2 > this.mLen) {
                    runLimit2 = this.mLen;
                }
                int runLimit3 = runLimit2;
                boolean runIsRtl2 = (runs[i2 + 1] & 67108864) != 0;
                int j2 = this.mHasTabs ? runStart : runLimit3;
                h = h2;
                int segstart = runStart;
                while (true) {
                    int j3 = j2;
                    if (j3 <= runLimit3) {
                        int codept = 0;
                        codept = 0;
                        if (this.mHasTabs && j3 < runLimit3) {
                            char c = chars[j3];
                            codept = c;
                            if (c >= 55296) {
                                codept = c;
                                if (c < 56320) {
                                    codept = c;
                                    if (j3 + 1 < runLimit3) {
                                        int codept2 = Character.codePointAt(chars, j3);
                                        codept = codept2;
                                        if (codept2 > 65535) {
                                            j3++;
                                            runIsRtl = runIsRtl2;
                                            runLimit = runLimit3;
                                            j2 = j3 + 1;
                                            runIsRtl2 = runIsRtl;
                                            runLimit3 = runLimit;
                                        }
                                    }
                                }
                            }
                        }
                        int codept3 = codept;
                        if (j3 == runLimit3 || codept3 == 9) {
                            boolean inSegment = target >= segstart && target < j3;
                            boolean advance = (this.mDir == -1) == runIsRtl2;
                            if (inSegment && advance) {
                                return h + measureRun(segstart, offset, j3, runIsRtl2, fmi);
                            }
                            runIsRtl = runIsRtl2;
                            runLimit = runLimit3;
                            float w = measureRun(segstart, j3, j3, runIsRtl, fmi);
                            h += advance ? w : -w;
                            if (!inSegment) {
                                if (codept3 != 9) {
                                    j = j3;
                                } else {
                                    j = j3;
                                    if (offset == j) {
                                        return h;
                                    }
                                    float h3 = this.mDir * nextTab(this.mDir * h);
                                    if (target == j) {
                                        return h3;
                                    }
                                    h = h3;
                                }
                                segstart = j + 1;
                                j3 = j;
                                j2 = j3 + 1;
                                runIsRtl2 = runIsRtl;
                                runLimit3 = runLimit;
                            } else {
                                return h + measureRun(segstart, offset, j3, runIsRtl, null);
                            }
                        }
                        runIsRtl = runIsRtl2;
                        runLimit = runLimit3;
                        j2 = j3 + 1;
                        runIsRtl2 = runIsRtl;
                        runLimit3 = runLimit;
                    }
                }
            } else {
                return h2;
            }
            i = i2 + 2;
            h2 = h;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized float[] measureAllOffsets(boolean[] trailing, Paint.FontMetricsInt fmi) {
        boolean runIsRtl;
        int runLimit;
        int j;
        int j2;
        int j3;
        int offset;
        float w;
        int i = 1;
        float[] measurement = new float[this.mLen + 1];
        int[] target = new int[this.mLen + 1];
        int offset2 = 0;
        for (int offset3 = 0; offset3 < target.length; offset3++) {
            target[offset3] = trailing[offset3] ? offset3 - 1 : offset3;
        }
        if (target[0] < 0) {
            measurement[0] = 0.0f;
        }
        float h = 0.0f;
        if (!this.mHasTabs) {
            if (this.mDirections == Layout.DIRS_ALL_LEFT_TO_RIGHT) {
                while (true) {
                    int offset4 = offset2;
                    if (offset4 <= this.mLen) {
                        measurement[offset4] = measureRun(0, offset4, this.mLen, false, fmi);
                        offset2 = offset4 + 1;
                    } else {
                        return measurement;
                    }
                }
            } else if (this.mDirections == Layout.DIRS_ALL_RIGHT_TO_LEFT) {
                while (true) {
                    int offset5 = offset2;
                    if (offset5 <= this.mLen) {
                        measurement[offset5] = measureRun(0, offset5, this.mLen, true, fmi);
                        offset2 = offset5 + 1;
                    } else {
                        return measurement;
                    }
                }
            }
        }
        char[] chars = this.mChars;
        int[] runs = this.mDirections.mDirections;
        int i2 = 0;
        while (true) {
            int i3 = i2;
            int i4 = runs.length;
            if (i3 >= i4) {
                break;
            }
            int runStart = runs[i3];
            int runLimit2 = (runs[i3 + 1] & 67108863) + runStart;
            if (runLimit2 > this.mLen) {
                runLimit2 = this.mLen;
            }
            int runLimit3 = runLimit2;
            boolean runIsRtl2 = (runs[i3 + 1] & 67108864) != 0 ? i : offset2;
            int j4 = this.mHasTabs ? runStart : runLimit3;
            float h2 = h;
            int segstart = runStart;
            while (true) {
                int j5 = j4;
                if (j5 <= runLimit3) {
                    int codept = 0;
                    codept = 0;
                    if (this.mHasTabs && j5 < runLimit3) {
                        char c = chars[j5];
                        codept = c;
                        if (c >= 55296) {
                            codept = c;
                            if (c < 56320) {
                                codept = c;
                                if (j5 + 1 < runLimit3) {
                                    int codept2 = Character.codePointAt(chars, j5);
                                    codept = codept2;
                                    if (codept2 > 65535) {
                                        j5++;
                                        runIsRtl = runIsRtl2;
                                        runLimit = runLimit3;
                                        j4 = j5 + 1;
                                        i = 1;
                                        runIsRtl2 = runIsRtl;
                                        runLimit3 = runLimit;
                                        offset2 = 0;
                                    }
                                }
                            }
                        }
                    }
                    int codept3 = codept;
                    if (j5 == runLimit3 || codept3 == 9) {
                        float oldh = h2;
                        int i5 = (this.mDir == -1 ? i : offset2) == runIsRtl2 ? i : offset2;
                        int offset6 = j5;
                        runIsRtl = runIsRtl2;
                        runLimit = runLimit3;
                        float w2 = measureRun(segstart, j5, j5, runIsRtl2, fmi);
                        h2 += i5 != 0 ? w2 : -w2;
                        float baseh = i5 != 0 ? oldh : h2;
                        Paint.FontMetricsInt crtfmi = i5 != 0 ? fmi : null;
                        int offset7 = segstart;
                        while (true) {
                            int offset8 = offset7;
                            j = offset6;
                            if (offset8 > j || offset8 > this.mLen) {
                                break;
                            }
                            if (target[offset8] < segstart || target[offset8] >= j) {
                                j3 = j;
                                offset = offset8;
                                w = w2;
                            } else {
                                j3 = j;
                                offset = offset8;
                                w = w2;
                                measurement[offset] = baseh + measureRun(segstart, offset8, j3, runIsRtl, crtfmi);
                            }
                            offset7 = offset + 1;
                            offset6 = j3;
                            w2 = w;
                        }
                        if (codept3 != 9) {
                            j2 = j;
                        } else {
                            j2 = j;
                            if (target[j2] == j2) {
                                measurement[j2] = h2;
                            }
                            float h3 = this.mDir * nextTab(this.mDir * h2);
                            if (target[j2 + 1] == j2) {
                                measurement[j2 + 1] = h3;
                            }
                            h2 = h3;
                        }
                        segstart = j2 + 1;
                        j5 = j2;
                        j4 = j5 + 1;
                        i = 1;
                        runIsRtl2 = runIsRtl;
                        runLimit3 = runLimit;
                        offset2 = 0;
                    }
                    runIsRtl = runIsRtl2;
                    runLimit = runLimit3;
                    j4 = j5 + 1;
                    i = 1;
                    runIsRtl2 = runIsRtl;
                    runLimit3 = runLimit;
                    offset2 = 0;
                }
            }
            i2 = i3 + 2;
            h = h2;
            offset2 = 0;
        }
        if (target[this.mLen] == this.mLen) {
            measurement[this.mLen] = h;
        }
        return measurement;
    }

    private synchronized float drawRun(Canvas c, int start, int limit, boolean runIsRtl, float x, int top, int y, int bottom, boolean needWidth) {
        if ((this.mDir == 1) == runIsRtl) {
            float w = -measureRun(start, limit, limit, runIsRtl, null);
            handleRun(start, limit, limit, runIsRtl, c, x + w, top, y, bottom, null, false);
            return w;
        }
        return handleRun(start, limit, limit, runIsRtl, c, x, top, y, bottom, null, needWidth);
    }

    private synchronized float measureRun(int start, int offset, int limit, boolean runIsRtl, Paint.FontMetricsInt fmi) {
        return handleRun(start, offset, limit, runIsRtl, null, 0.0f, 0, 0, 0, fmi, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x0175, code lost:
        r1 = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x0178, code lost:
        if (r13 != (-1)) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x017a, code lost:
        if (r0 == false) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x017c, code lost:
        r1 = r27.mLen + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x0181, code lost:
        r13 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x0183, code lost:
        if (r13 > r11) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x0185, code lost:
        if (r0 == false) goto L63;
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x0187, code lost:
        r1 = r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x0189, code lost:
        r1 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x018a, code lost:
        r13 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x018b, code lost:
        return r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:135:?, code lost:
        return r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:136:?, code lost:
        return r13;
     */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0175 A[ADDED_TO_REGION, EDGE_INSN: B:120:0x0175->B:109:0x0175 ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:72:0x00fe  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x0100  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0103  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0105  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x010a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized int getOffsetToLeftRightOf(int r28, boolean r29) {
        /*
            Method dump skipped, instructions count: 396
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.TextLine.getOffsetToLeftRightOf(int, boolean):int");
    }

    private synchronized int getOffsetBeforeAfter(int runIndex, int runStart, int runLimit, boolean runIsRtl, int offset, boolean after) {
        int spanLimit;
        int spanStart;
        int spanLimit2;
        if (runIndex >= 0) {
            if (offset != (after ? this.mLen : 0)) {
                TextPaint wp = this.mWorkPaint;
                wp.set(this.mPaint);
                wp.setWordSpacing(this.mAddedWidth);
                int spanStart2 = runStart;
                if (this.mSpanned != null) {
                    int target = after ? offset + 1 : offset;
                    int limit = this.mStart + runLimit;
                    while (true) {
                        spanLimit = this.mSpanned.nextSpanTransition(this.mStart + spanStart2, limit, MetricAffectingSpan.class) - this.mStart;
                        if (spanLimit >= target) {
                            break;
                        }
                        spanStart2 = spanLimit;
                    }
                    MetricAffectingSpan[] spans = (MetricAffectingSpan[]) TextUtils.removeEmptySpans((MetricAffectingSpan[]) this.mSpanned.getSpans(this.mStart + spanStart2, this.mStart + spanLimit, MetricAffectingSpan.class), this.mSpanned, MetricAffectingSpan.class);
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
                } else {
                    spanStart = spanStart2;
                    spanLimit2 = runLimit;
                }
                int cursorOpt = after ? 0 : 2;
                if (this.mCharsValid) {
                    return wp.getTextRunCursor(this.mChars, spanStart, spanLimit2 - spanStart, runIsRtl ? 1 : 0, offset, cursorOpt);
                }
                return wp.getTextRunCursor(this.mText, this.mStart + spanStart, this.mStart + spanLimit2, runIsRtl ? 1 : 0, this.mStart + offset, cursorOpt) - this.mStart;
            }
        }
        if (after) {
            return TextUtils.getOffsetAfter(this.mText, this.mStart + offset) - this.mStart;
        }
        return TextUtils.getOffsetBefore(this.mText, this.mStart + offset) - this.mStart;
    }

    private static synchronized void expandMetricsFromPaint(Paint.FontMetricsInt fmi, TextPaint wp) {
        int previousTop = fmi.top;
        int previousAscent = fmi.ascent;
        int previousDescent = fmi.descent;
        int previousBottom = fmi.bottom;
        int previousLeading = fmi.leading;
        wp.getFontMetricsInt(fmi);
        updateMetrics(fmi, previousTop, previousAscent, previousDescent, previousBottom, previousLeading);
    }

    static synchronized void updateMetrics(Paint.FontMetricsInt fmi, int previousTop, int previousAscent, int previousDescent, int previousBottom, int previousLeading) {
        fmi.top = Math.min(fmi.top, previousTop);
        fmi.ascent = Math.min(fmi.ascent, previousAscent);
        fmi.descent = Math.max(fmi.descent, previousDescent);
        fmi.bottom = Math.max(fmi.bottom, previousBottom);
        fmi.leading = Math.max(fmi.leading, previousLeading);
    }

    private static synchronized void drawStroke(TextPaint wp, Canvas c, int color, float position, float thickness, float xleft, float xright, float baseline) {
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

    private synchronized float getRunAdvance(TextPaint wp, int start, int end, int contextStart, int contextEnd, boolean runIsRtl, int offset) {
        if (this.mCharsValid) {
            return wp.getRunAdvance(this.mChars, start, end, contextStart, contextEnd, runIsRtl, offset);
        }
        int delta = this.mStart;
        if (this.mComputed == null) {
            return wp.getRunAdvance(this.mText, delta + start, delta + end, delta + contextStart, delta + contextEnd, runIsRtl, delta + offset);
        }
        return this.mComputed.getWidth(start + delta, end + delta);
    }

    private synchronized float handleText(TextPaint wp, int start, int end, int contextStart, int contextEnd, boolean runIsRtl, Canvas c, float x, int top, int y, int bottom, Paint.FontMetricsInt fmi, boolean needWidth, int offset, ArrayList<DecorationInfo> decorations) {
        int numDecorations;
        float totalWidth;
        float leftX;
        float rightX;
        float decorationXLeft;
        float f;
        float totalWidth2;
        DecorationInfo info;
        int i;
        int numDecorations2;
        int i2 = start;
        int i3 = y;
        ArrayList<DecorationInfo> arrayList = decorations;
        TextLine textLine = this;
        wp.setWordSpacing(textLine.mAddedWidth);
        if (fmi != null) {
            expandMetricsFromPaint(fmi, wp);
        }
        int i4 = end;
        if (i4 == i2) {
            return 0.0f;
        }
        float totalWidth3 = 0.0f;
        int decorationStart = 0;
        int numDecorations3 = arrayList == null ? 0 : decorations.size();
        if (needWidth || (c != null && (wp.bgColor != 0 || numDecorations3 != 0 || runIsRtl))) {
            numDecorations = numDecorations3;
            totalWidth3 = textLine.getRunAdvance(wp, i2, i4, contextStart, contextEnd, runIsRtl, offset);
        } else {
            numDecorations = numDecorations3;
        }
        if (c != null) {
            if (runIsRtl) {
                leftX = x - totalWidth3;
                rightX = x;
            } else {
                leftX = x;
                rightX = x + totalWidth3;
            }
            float leftX2 = leftX;
            float rightX2 = rightX;
            if (wp.bgColor != 0) {
                int previousColor = wp.getColor();
                Paint.Style previousStyle = wp.getStyle();
                wp.setColor(wp.bgColor);
                wp.setStyle(Paint.Style.FILL);
                c.drawRect(leftX2, top, rightX2, bottom, wp);
                wp.setStyle(previousStyle);
                wp.setColor(previousColor);
            }
            if (numDecorations != 0) {
                while (true) {
                    int i5 = decorationStart;
                    if (i5 >= numDecorations) {
                        break;
                    }
                    DecorationInfo info2 = arrayList.get(i5);
                    int decorationStart2 = Math.max(info2.start, i2);
                    int decorationEnd = Math.min(info2.end, offset);
                    TextLine textLine2 = textLine;
                    int i6 = i2;
                    int i7 = i4;
                    int numDecorations4 = numDecorations;
                    float decorationStartAdvance = textLine2.getRunAdvance(wp, i6, i7, contextStart, contextEnd, runIsRtl, decorationStart2);
                    float decorationEndAdvance = textLine2.getRunAdvance(wp, i6, i7, contextStart, contextEnd, runIsRtl, decorationEnd);
                    if (runIsRtl) {
                        decorationXLeft = rightX2 - decorationEndAdvance;
                        f = rightX2 - decorationStartAdvance;
                    } else {
                        decorationXLeft = leftX2 + decorationStartAdvance;
                        f = leftX2 + decorationEndAdvance;
                    }
                    float decorationXLeft2 = decorationXLeft;
                    float decorationXRight = f;
                    if (info2.underlineColor != 0) {
                        drawStroke(wp, c, info2.underlineColor, wp.getUnderlinePosition(), info2.underlineThickness, decorationXLeft2, decorationXRight, i3);
                    }
                    if (info2.isUnderlineText) {
                        float thickness = Math.max(wp.getUnderlineThickness(), 1.0f);
                        totalWidth2 = totalWidth3;
                        info = info2;
                        numDecorations2 = numDecorations4;
                        i = i3;
                        drawStroke(wp, c, wp.getColor(), wp.getUnderlinePosition(), thickness, decorationXLeft2, decorationXRight, i3);
                    } else {
                        totalWidth2 = totalWidth3;
                        info = info2;
                        i = i3;
                        numDecorations2 = numDecorations4;
                    }
                    if (info.isStrikeThruText) {
                        float thickness2 = Math.max(wp.getStrikeThruThickness(), 1.0f);
                        drawStroke(wp, c, wp.getColor(), wp.getStrikeThruPosition(), thickness2, decorationXLeft2, decorationXRight, i);
                    }
                    decorationStart = i5 + 1;
                    textLine = this;
                    i2 = start;
                    i4 = end;
                    arrayList = decorations;
                    i3 = i;
                    totalWidth3 = totalWidth2;
                    numDecorations = numDecorations2;
                }
            }
            totalWidth = totalWidth3;
            drawTextRun(c, wp, start, end, contextStart, contextEnd, runIsRtl, leftX2, i3 + wp.baselineShift);
        } else {
            totalWidth = totalWidth3;
        }
        return runIsRtl ? -totalWidth : totalWidth;
    }

    private synchronized float handleReplacement(ReplacementSpan replacement, TextPaint wp, int start, int limit, boolean runIsRtl, Canvas c, float x, int top, int y, int bottom, Paint.FontMetricsInt fmi, boolean needWidth) {
        float ret;
        float ret2 = 0.0f;
        int textStart = this.mStart + start;
        int textLimit = this.mStart + limit;
        if (needWidth || (c != null && runIsRtl)) {
            int previousTop = 0;
            int previousAscent = 0;
            int previousDescent = 0;
            int previousBottom = 0;
            int previousLeading = 0;
            boolean needUpdateMetrics = fmi != null;
            if (needUpdateMetrics) {
                previousTop = fmi.top;
                previousAscent = fmi.ascent;
                previousDescent = fmi.descent;
                previousBottom = fmi.bottom;
                previousLeading = fmi.leading;
            }
            int previousTop2 = previousTop;
            int previousAscent2 = previousAscent;
            int previousDescent2 = previousDescent;
            int previousBottom2 = previousBottom;
            int previousLeading2 = previousLeading;
            ret2 = replacement.getSize(wp, this.mText, textStart, textLimit, fmi);
            if (needUpdateMetrics) {
                updateMetrics(fmi, previousTop2, previousAscent2, previousDescent2, previousBottom2, previousLeading2);
            }
        }
        float ret3 = ret2;
        if (c != null) {
            ret = ret3;
            replacement.draw(c, this.mText, textStart, textLimit, runIsRtl ? x - ret3 : x, top, y, bottom, wp);
        } else {
            ret = ret3;
        }
        return runIsRtl ? -ret : ret;
    }

    private synchronized int adjustHyphenEdit(int start, int limit, int hyphenEdit) {
        int result = hyphenEdit;
        if (start > 0) {
            result &= -25;
        }
        if (limit < this.mLen) {
            return result & (-8);
        }
        return result;
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

        private synchronized DecorationInfo() {
            this.start = -1;
            this.end = -1;
        }

        public synchronized boolean hasDecoration() {
            return this.isStrikeThruText || this.isUnderlineText || this.underlineColor != 0;
        }

        public synchronized DecorationInfo copyInfo() {
            DecorationInfo copy = new DecorationInfo();
            copy.isStrikeThruText = this.isStrikeThruText;
            copy.isUnderlineText = this.isUnderlineText;
            copy.underlineColor = this.underlineColor;
            copy.underlineThickness = this.underlineThickness;
            return copy;
        }
    }

    private synchronized void extractDecorationInfo(TextPaint paint, DecorationInfo info) {
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

    /* JADX WARN: Removed duplicated region for block: B:79:0x023b  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x024d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized float handleRun(int r37, int r38, int r39, boolean r40, android.graphics.Canvas r41, float r42, int r43, int r44, int r45, android.graphics.Paint.FontMetricsInt r46, boolean r47) {
        /*
            Method dump skipped, instructions count: 764
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.TextLine.handleRun(int, int, int, boolean, android.graphics.Canvas, float, int, int, int, android.graphics.Paint$FontMetricsInt, boolean):float");
    }

    private synchronized void drawTextRun(Canvas c, TextPaint wp, int start, int end, int contextStart, int contextEnd, boolean runIsRtl, float x, int y) {
        if (this.mCharsValid) {
            int count = end - start;
            int contextCount = contextEnd - contextStart;
            c.drawTextRun(this.mChars, start, count, contextStart, contextCount, x, y, runIsRtl, wp);
            return;
        }
        int delta = this.mStart;
        c.drawTextRun(this.mText, delta + start, delta + end, delta + contextStart, delta + contextEnd, x, y, runIsRtl, wp);
    }

    synchronized float nextTab(float h) {
        if (this.mTabs != null) {
            return this.mTabs.nextTab(h);
        }
        return Layout.TabStops.nextDefaultStop(h, 20);
    }

    private synchronized boolean isStretchableWhitespace(int ch) {
        return ch == 32;
    }

    private synchronized int countStretchableSpaces(int start, int end) {
        int count = 0;
        for (int count2 = start; count2 < end; count2++) {
            char c = this.mCharsValid ? this.mChars[count2] : this.mText.charAt(this.mStart + count2);
            if (isStretchableWhitespace(c)) {
                count++;
            }
        }
        return count;
    }

    public static synchronized boolean isLineEndSpace(char ch) {
        return ch == ' ' || ch == '\t' || ch == 5760 || (8192 <= ch && ch <= 8202 && ch != 8199) || ch == 8287 || ch == 12288;
    }
}
