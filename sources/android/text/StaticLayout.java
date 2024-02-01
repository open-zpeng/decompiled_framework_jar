package android.text;

import android.annotation.UnsupportedAppUsage;
import android.graphics.Paint;
import android.text.Layout;
import android.text.TextUtils;
import android.text.style.LineHeightSpan;
import android.util.Log;
import android.util.Pools;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import com.xiaopeng.util.FeatureOption;

/* loaded from: classes2.dex */
public class StaticLayout extends Layout {
    private static final char CHAR_NEW_LINE = '\n';
    private static final int COLUMNS_ELLIPSIZE = 7;
    private static final int COLUMNS_NORMAL = 5;
    private static final int DEFAULT_MAX_LINE_HEIGHT = -1;
    private static final int DESCENT = 2;
    private static final int DIR = 0;
    private static final int DIR_SHIFT = 30;
    private static final int ELLIPSIS_COUNT = 6;
    @UnsupportedAppUsage
    private static final int ELLIPSIS_START = 5;
    private static final int END_HYPHEN_MASK = 7;
    private static final int EXTRA = 3;
    private static final double EXTRA_ROUNDING = 0.5d;
    private static final int HYPHEN = 4;
    private static final int HYPHEN_MASK = 255;
    private static final int START = 0;
    private static final int START_HYPHEN_BITS_SHIFT = 3;
    private static final int START_HYPHEN_MASK = 24;
    private static final int START_MASK = 536870911;
    private static final int TAB = 0;
    private static final float TAB_INCREMENT = 20.0f;
    private static final int TAB_MASK = 536870912;
    static final String TAG = "StaticLayout";
    private static final int TOP = 1;
    private int mBottomPadding;
    @UnsupportedAppUsage
    private int mColumns;
    private boolean mEllipsized;
    private int mEllipsizedWidth;
    private int[] mLeftIndents;
    @UnsupportedAppUsage
    private int mLineCount;
    @UnsupportedAppUsage
    private Layout.Directions[] mLineDirections;
    @UnsupportedAppUsage
    private int[] mLines;
    private int mMaxLineHeight;
    @UnsupportedAppUsage
    private int mMaximumVisibleLineCount;
    private int[] mRightIndents;
    private int mTopPadding;

    /* loaded from: classes2.dex */
    public static final class Builder {
        private static final Pools.SynchronizedPool<Builder> sPool = new Pools.SynchronizedPool<>(3);
        private boolean mAddLastLineLineSpacing;
        private Layout.Alignment mAlignment;
        private int mBreakStrategy;
        private TextUtils.TruncateAt mEllipsize;
        private int mEllipsizedWidth;
        private int mEnd;
        private boolean mFallbackLineSpacing;
        private final Paint.FontMetricsInt mFontMetricsInt = new Paint.FontMetricsInt();
        private int mHyphenationFrequency;
        private boolean mIncludePad;
        private int mJustificationMode;
        private int[] mLeftIndents;
        private int mMaxLines;
        private TextPaint mPaint;
        private int[] mRightIndents;
        private float mSpacingAdd;
        private float mSpacingMult;
        private int mStart;
        private CharSequence mText;
        private TextDirectionHeuristic mTextDir;
        private int mWidth;

        private Builder() {
        }

        public static Builder obtain(CharSequence source, int start, int end, TextPaint paint, int width) {
            Builder b = sPool.acquire();
            if (b == null) {
                b = new Builder();
            }
            b.mText = source;
            b.mStart = start;
            b.mEnd = end;
            b.mPaint = paint;
            b.mWidth = width;
            b.mAlignment = Layout.Alignment.ALIGN_NORMAL;
            b.mTextDir = TextDirectionHeuristics.FIRSTSTRONG_LTR;
            b.mSpacingMult = 1.0f;
            b.mSpacingAdd = 0.0f;
            b.mIncludePad = true;
            b.mFallbackLineSpacing = false;
            b.mEllipsizedWidth = width;
            b.mEllipsize = null;
            b.mMaxLines = Integer.MAX_VALUE;
            b.mBreakStrategy = 0;
            b.mHyphenationFrequency = 0;
            b.mJustificationMode = 0;
            return b;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void recycle(Builder b) {
            b.mPaint = null;
            b.mText = null;
            b.mLeftIndents = null;
            b.mRightIndents = null;
            sPool.release(b);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void finish() {
            this.mText = null;
            this.mPaint = null;
            this.mLeftIndents = null;
            this.mRightIndents = null;
        }

        public Builder setText(CharSequence source) {
            return setText(source, 0, source.length());
        }

        public Builder setText(CharSequence source, int start, int end) {
            this.mText = source;
            this.mStart = start;
            this.mEnd = end;
            return this;
        }

        public Builder setPaint(TextPaint paint) {
            this.mPaint = paint;
            return this;
        }

        public Builder setWidth(int width) {
            this.mWidth = width;
            if (this.mEllipsize == null) {
                this.mEllipsizedWidth = width;
            }
            return this;
        }

        public Builder setAlignment(Layout.Alignment alignment) {
            this.mAlignment = alignment;
            return this;
        }

        public Builder setTextDirection(TextDirectionHeuristic textDir) {
            this.mTextDir = textDir;
            return this;
        }

        public Builder setLineSpacing(float spacingAdd, float spacingMult) {
            this.mSpacingAdd = spacingAdd;
            this.mSpacingMult = spacingMult;
            return this;
        }

        public Builder setIncludePad(boolean includePad) {
            this.mIncludePad = includePad;
            return this;
        }

        public Builder setUseLineSpacingFromFallbacks(boolean useLineSpacingFromFallbacks) {
            this.mFallbackLineSpacing = useLineSpacingFromFallbacks;
            return this;
        }

        public Builder setEllipsizedWidth(int ellipsizedWidth) {
            this.mEllipsizedWidth = ellipsizedWidth;
            return this;
        }

        public Builder setEllipsize(TextUtils.TruncateAt ellipsize) {
            this.mEllipsize = ellipsize;
            return this;
        }

        public Builder setMaxLines(int maxLines) {
            this.mMaxLines = maxLines;
            return this;
        }

        public Builder setBreakStrategy(int breakStrategy) {
            this.mBreakStrategy = breakStrategy;
            return this;
        }

        public Builder setHyphenationFrequency(int hyphenationFrequency) {
            this.mHyphenationFrequency = hyphenationFrequency;
            return this;
        }

        public Builder setIndents(int[] leftIndents, int[] rightIndents) {
            this.mLeftIndents = leftIndents;
            this.mRightIndents = rightIndents;
            return this;
        }

        public Builder setJustificationMode(int justificationMode) {
            this.mJustificationMode = justificationMode;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder setAddLastLineLineSpacing(boolean value) {
            this.mAddLastLineLineSpacing = value;
            return this;
        }

        public StaticLayout build() {
            StaticLayout result = new StaticLayout(this);
            recycle(this);
            return result;
        }
    }

    @Deprecated
    public StaticLayout(CharSequence source, TextPaint paint, int width, Layout.Alignment align, float spacingmult, float spacingadd, boolean includepad) {
        this(source, 0, source.length(), paint, width, align, spacingmult, spacingadd, includepad);
    }

    @Deprecated
    public StaticLayout(CharSequence source, int bufstart, int bufend, TextPaint paint, int outerwidth, Layout.Alignment align, float spacingmult, float spacingadd, boolean includepad) {
        this(source, bufstart, bufend, paint, outerwidth, align, spacingmult, spacingadd, includepad, null, 0);
    }

    @Deprecated
    public StaticLayout(CharSequence source, int bufstart, int bufend, TextPaint paint, int outerwidth, Layout.Alignment align, float spacingmult, float spacingadd, boolean includepad, TextUtils.TruncateAt ellipsize, int ellipsizedWidth) {
        this(source, bufstart, bufend, paint, outerwidth, align, TextDirectionHeuristics.FIRSTSTRONG_LTR, spacingmult, spacingadd, includepad, ellipsize, ellipsizedWidth, Integer.MAX_VALUE);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 117521430)
    @java.lang.Deprecated
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public StaticLayout(java.lang.CharSequence r15, int r16, int r17, android.text.TextPaint r18, int r19, android.text.Layout.Alignment r20, android.text.TextDirectionHeuristic r21, float r22, float r23, boolean r24, android.text.TextUtils.TruncateAt r25, int r26, int r27) {
        /*
            r14 = this;
            r8 = r14
            r9 = r15
            r10 = r25
            r11 = r26
            r12 = r27
            if (r10 != 0) goto Lc
            r1 = r9
            goto L1d
        Lc:
            boolean r0 = r9 instanceof android.text.Spanned
            if (r0 == 0) goto L17
            android.text.Layout$SpannedEllipsizer r0 = new android.text.Layout$SpannedEllipsizer
            r0.<init>(r15)
            r1 = r0
            goto L1d
        L17:
            android.text.Layout$Ellipsizer r0 = new android.text.Layout$Ellipsizer
            r0.<init>(r15)
            r1 = r0
        L1d:
            r0 = r14
            r2 = r18
            r3 = r19
            r4 = r20
            r5 = r21
            r6 = r22
            r7 = r23
            r0.<init>(r1, r2, r3, r4, r5, r6, r7)
            r0 = -1
            r8.mMaxLineHeight = r0
            r0 = 2147483647(0x7fffffff, float:NaN)
            r8.mMaximumVisibleLineCount = r0
            android.text.StaticLayout$Builder r0 = android.text.StaticLayout.Builder.obtain(r15, r16, r17, r18, r19)
            r1 = r20
            android.text.StaticLayout$Builder r0 = r0.setAlignment(r1)
            r2 = r21
            android.text.StaticLayout$Builder r0 = r0.setTextDirection(r2)
            r3 = r22
            r4 = r23
            android.text.StaticLayout$Builder r0 = r0.setLineSpacing(r4, r3)
            r5 = r24
            android.text.StaticLayout$Builder r0 = r0.setIncludePad(r5)
            android.text.StaticLayout$Builder r0 = r0.setEllipsizedWidth(r11)
            android.text.StaticLayout$Builder r0 = r0.setEllipsize(r10)
            android.text.StaticLayout$Builder r0 = r0.setMaxLines(r12)
            if (r10 == 0) goto L75
            java.lang.CharSequence r6 = r14.getText()
            android.text.Layout$Ellipsizer r6 = (android.text.Layout.Ellipsizer) r6
            r6.mLayout = r8
            r6.mWidth = r11
            r6.mMethod = r10
            r8.mEllipsizedWidth = r11
            r7 = 7
            r8.mColumns = r7
            r6 = r19
            goto L7c
        L75:
            r6 = 5
            r8.mColumns = r6
            r6 = r19
            r8.mEllipsizedWidth = r6
        L7c:
            java.lang.Class<android.text.Layout$Directions> r7 = android.text.Layout.Directions.class
            r13 = 2
            java.lang.Object[] r7 = com.android.internal.util.ArrayUtils.newUnpaddedArray(r7, r13)
            android.text.Layout$Directions[] r7 = (android.text.Layout.Directions[]) r7
            r8.mLineDirections = r7
            int r7 = r8.mColumns
            int r7 = r7 * r13
            int[] r7 = com.android.internal.util.ArrayUtils.newUnpaddedIntArray(r7)
            r8.mLines = r7
            r8.mMaximumVisibleLineCount = r12
            boolean r7 = android.text.StaticLayout.Builder.access$100(r0)
            boolean r13 = android.text.StaticLayout.Builder.access$100(r0)
            r14.generate(r0, r7, r13)
            android.text.StaticLayout.Builder.access$200(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.StaticLayout.<init>(java.lang.CharSequence, int, int, android.text.TextPaint, int, android.text.Layout$Alignment, android.text.TextDirectionHeuristic, float, float, boolean, android.text.TextUtils$TruncateAt, int, int):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public StaticLayout(CharSequence text) {
        super(text, null, 0, null, 0.0f, 0.0f);
        this.mMaxLineHeight = -1;
        this.mMaximumVisibleLineCount = Integer.MAX_VALUE;
        this.mColumns = 7;
        this.mLineDirections = (Layout.Directions[]) ArrayUtils.newUnpaddedArray(Layout.Directions.class, 2);
        this.mLines = ArrayUtils.newUnpaddedIntArray(this.mColumns * 2);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v23, types: [java.lang.CharSequence] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private StaticLayout(android.text.StaticLayout.Builder r11) {
        /*
            r10 = this;
            android.text.TextUtils$TruncateAt r0 = android.text.StaticLayout.Builder.access$300(r11)
            if (r0 != 0) goto Lc
            java.lang.CharSequence r0 = android.text.StaticLayout.Builder.access$400(r11)
            r3 = r0
            goto L29
        Lc:
            java.lang.CharSequence r0 = android.text.StaticLayout.Builder.access$400(r11)
            boolean r0 = r0 instanceof android.text.Spanned
            if (r0 == 0) goto L1f
            android.text.Layout$SpannedEllipsizer r0 = new android.text.Layout$SpannedEllipsizer
            java.lang.CharSequence r1 = android.text.StaticLayout.Builder.access$400(r11)
            r0.<init>(r1)
            r3 = r0
            goto L29
        L1f:
            android.text.Layout$Ellipsizer r0 = new android.text.Layout$Ellipsizer
            java.lang.CharSequence r1 = android.text.StaticLayout.Builder.access$400(r11)
            r0.<init>(r1)
            r3 = r0
        L29:
            android.text.TextPaint r4 = android.text.StaticLayout.Builder.access$500(r11)
            int r5 = android.text.StaticLayout.Builder.access$600(r11)
            android.text.Layout$Alignment r6 = android.text.StaticLayout.Builder.access$700(r11)
            android.text.TextDirectionHeuristic r7 = android.text.StaticLayout.Builder.access$800(r11)
            float r8 = android.text.StaticLayout.Builder.access$900(r11)
            float r9 = android.text.StaticLayout.Builder.access$1000(r11)
            r2 = r10
            r2.<init>(r3, r4, r5, r6, r7, r8, r9)
            r0 = -1
            r10.mMaxLineHeight = r0
            r0 = 2147483647(0x7fffffff, float:NaN)
            r10.mMaximumVisibleLineCount = r0
            android.text.TextUtils$TruncateAt r0 = android.text.StaticLayout.Builder.access$300(r11)
            if (r0 == 0) goto L71
            java.lang.CharSequence r0 = r10.getText()
            android.text.Layout$Ellipsizer r0 = (android.text.Layout.Ellipsizer) r0
            r0.mLayout = r10
            int r1 = android.text.StaticLayout.Builder.access$1100(r11)
            r0.mWidth = r1
            android.text.TextUtils$TruncateAt r1 = android.text.StaticLayout.Builder.access$300(r11)
            r0.mMethod = r1
            int r1 = android.text.StaticLayout.Builder.access$1100(r11)
            r10.mEllipsizedWidth = r1
            r1 = 7
            r10.mColumns = r1
            goto L7a
        L71:
            r0 = 5
            r10.mColumns = r0
            int r0 = android.text.StaticLayout.Builder.access$600(r11)
            r10.mEllipsizedWidth = r0
        L7a:
            java.lang.Class<android.text.Layout$Directions> r0 = android.text.Layout.Directions.class
            r1 = 2
            java.lang.Object[] r0 = com.android.internal.util.ArrayUtils.newUnpaddedArray(r0, r1)
            android.text.Layout$Directions[] r0 = (android.text.Layout.Directions[]) r0
            r10.mLineDirections = r0
            int r0 = r10.mColumns
            int r0 = r0 * r1
            int[] r0 = com.android.internal.util.ArrayUtils.newUnpaddedIntArray(r0)
            r10.mLines = r0
            int r0 = android.text.StaticLayout.Builder.access$1200(r11)
            r10.mMaximumVisibleLineCount = r0
            int[] r0 = android.text.StaticLayout.Builder.access$1300(r11)
            r10.mLeftIndents = r0
            int[] r0 = android.text.StaticLayout.Builder.access$1400(r11)
            r10.mRightIndents = r0
            int r0 = android.text.StaticLayout.Builder.access$1500(r11)
            r10.setJustificationMode(r0)
            boolean r0 = android.text.StaticLayout.Builder.access$100(r11)
            boolean r1 = android.text.StaticLayout.Builder.access$100(r11)
            r10.generate(r11, r0, r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.StaticLayout.<init>(android.text.StaticLayout$Builder):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:183:0x0650, code lost:
        r10 = r83;
     */
    /* JADX WARN: Code restructure failed: missing block: B:184:0x0652, code lost:
        if (r1 == r10) goto L167;
     */
    /* JADX WARN: Code restructure failed: missing block: B:185:0x0654, code lost:
        r12 = r84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:186:0x065e, code lost:
        if (r12.charAt(r1 - 1) != '\n') goto L159;
     */
    /* JADX WARN: Code restructure failed: missing block: B:189:0x0669, code lost:
        r12 = r84;
     */
    /* JADX WARN: Code restructure failed: missing block: B:191:0x066f, code lost:
        if (r7.mLineCount >= r7.mMaximumVisibleLineCount) goto L165;
     */
    /* JADX WARN: Code restructure failed: missing block: B:192:0x0672, code lost:
        r13 = android.text.MeasuredParagraph.buildForBidi(r12, r1, r1, r80, null);
        r15 = r81;
        r15.getFontMetricsInt(r2);
        out(r12, r1, r1, r2.ascent, r2.descent, r2.top, r2.bottom, r8, r48, r49, null, null, r2, false, 0, r16, r13, r1, r91, r92, r50, null, r10, r68, r77, 0.0f, r15, false);
     */
    /* JADX WARN: Code restructure failed: missing block: B:194:0x06cd, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:220:?, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:221:?, code lost:
        return;
     */
    /* JADX WARN: Removed duplicated region for block: B:100:0x02c9  */
    /* JADX WARN: Removed duplicated region for block: B:103:0x02dc A[LOOP:3: B:102:0x02da->B:103:0x02dc, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:134:0x03aa  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x05f6 A[LOOP:0: B:54:0x0172->B:181:0x05f6, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:196:0x05e4 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:99:0x02a1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void generate(android.text.StaticLayout.Builder r90, boolean r91, boolean r92) {
        /*
            Method dump skipped, instructions count: 1742
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.StaticLayout.generate(android.text.StaticLayout$Builder, boolean, boolean):void");
    }

    private int out(CharSequence text, int start, int end, int above, int below, int top, int bottom, int v, float spacingmult, float spacingadd, LineHeightSpan[] chooseHt, int[] chooseHtv, Paint.FontMetricsInt fm, boolean hasTab, int hyphenEdit, boolean needMultiply, MeasuredParagraph measured, int bufEnd, boolean includePad, boolean trackPad, boolean addLastLineLineSpacing, char[] chs, int widthStart, TextUtils.TruncateAt ellipsize, float ellipsisWidth, float textWidth, TextPaint paint, boolean moreChars) {
        int[] lines;
        char c;
        char c2;
        int j;
        int above2;
        int below2;
        int top2;
        int bottom2;
        int i;
        int i2;
        char c3;
        int i3;
        int i4;
        boolean lastCharIsNewLine;
        int extra;
        int want;
        char c4;
        int j2;
        boolean z;
        int j3 = this.mLineCount;
        int i5 = this.mColumns;
        int off = j3 * i5;
        char c5 = 1;
        int want2 = off + i5 + 1;
        int[] lines2 = this.mLines;
        int dir = measured.getParagraphDir();
        if (want2 < lines2.length) {
            lines = lines2;
        } else {
            int[] grow = ArrayUtils.newUnpaddedIntArray(GrowingArrayUtils.growSize(want2));
            System.arraycopy(lines2, 0, grow, 0, lines2.length);
            this.mLines = grow;
            lines = grow;
        }
        if (j3 >= this.mLineDirections.length) {
            Layout.Directions[] grow2 = (Layout.Directions[]) ArrayUtils.newUnpaddedArray(Layout.Directions.class, GrowingArrayUtils.growSize(j3));
            Layout.Directions[] directionsArr = this.mLineDirections;
            System.arraycopy(directionsArr, 0, grow2, 0, directionsArr.length);
            this.mLineDirections = grow2;
        }
        if (chooseHt == null) {
            c = 0;
            c2 = 1;
            j = j3;
            above2 = above;
            below2 = below;
            top2 = top;
            bottom2 = bottom;
        } else {
            fm.ascent = above;
            fm.descent = below;
            fm.top = top;
            fm.bottom = bottom;
            int i6 = 0;
            while (i6 < chooseHt.length) {
                if (chooseHt[i6] instanceof LineHeightSpan.WithDensity) {
                    z = false;
                    want = want2;
                    c4 = c5;
                    j2 = j3;
                    ((LineHeightSpan.WithDensity) chooseHt[i6]).chooseHeight(text, start, end, chooseHtv[i6], v, fm, paint);
                } else {
                    want = want2;
                    c4 = c5;
                    j2 = j3;
                    z = false;
                    chooseHt[i6].chooseHeight(text, start, end, chooseHtv[i6], v, fm);
                }
                i6++;
                c5 = c4;
                want2 = want;
                j3 = j2;
            }
            c2 = c5;
            j = j3;
            c = 0;
            above2 = fm.ascent;
            below2 = fm.descent;
            top2 = fm.top;
            bottom2 = fm.bottom;
        }
        char c6 = j == 0 ? c2 : c;
        char c7 = j + 1 == this.mMaximumVisibleLineCount ? c2 : c;
        if (ellipsize == null) {
            i = widthStart;
            i2 = bufEnd;
        } else {
            boolean forceEllipsis = (moreChars && this.mLineCount + c2 == this.mMaximumVisibleLineCount) ? c2 : c;
            if (((((!(this.mMaximumVisibleLineCount == c2 && moreChars) && (c6 == 0 || moreChars)) || ellipsize == TextUtils.TruncateAt.MARQUEE) && (c6 != 0 || ((c7 == 0 && moreChars) || ellipsize != TextUtils.TruncateAt.END))) ? c : c2) == 0) {
                i = widthStart;
                i2 = bufEnd;
            } else {
                i = widthStart;
                i2 = bufEnd;
                calculateEllipsis(start, end, measured, widthStart, ellipsisWidth, ellipsize, j, textWidth, paint, forceEllipsis);
            }
        }
        if (this.mEllipsized) {
            lastCharIsNewLine = true;
            i3 = start;
            i4 = 1;
        } else {
            if (i != i2 && i2 > 0) {
                if (text.charAt(i2 - 1) == '\n') {
                    c3 = 1;
                    if (end != i2 && c3 == 0) {
                        lastCharIsNewLine = true;
                        i4 = 1;
                        i3 = start;
                    } else {
                        i3 = start;
                        i4 = 1;
                        if (i3 != i2 && c3 != 0) {
                            lastCharIsNewLine = true;
                        } else {
                            lastCharIsNewLine = false;
                        }
                    }
                }
            }
            c3 = c;
            if (end != i2) {
            }
            i3 = start;
            i4 = 1;
            if (i3 != i2) {
            }
            lastCharIsNewLine = false;
        }
        if (c6 != 0) {
            if (trackPad) {
                this.mTopPadding = top2 - above2;
            }
            if (includePad) {
                above2 = top2;
            }
        }
        if (lastCharIsNewLine) {
            if (trackPad) {
                this.mBottomPadding = bottom2 - below2;
            }
            if (includePad) {
                below2 = bottom2;
            }
        }
        if (needMultiply && (addLastLineLineSpacing || !lastCharIsNewLine)) {
            double ex = ((below2 - above2) * (spacingmult - 1.0f)) + spacingadd;
            if (ex >= FeatureOption.FO_BOOT_POLICY_CPU) {
                extra = (int) (EXTRA_ROUNDING + ex);
            } else {
                extra = -((int) ((-ex) + EXTRA_ROUNDING));
            }
        } else {
            extra = 0;
        }
        lines[off + 0] = i3;
        lines[off + 1] = v;
        lines[off + 2] = below2 + extra;
        lines[off + 3] = extra;
        if (!this.mEllipsized && c7 != 0) {
            int maxLineBelow = includePad ? bottom2 : below2;
            this.mMaxLineHeight = v + (maxLineBelow - above2);
        }
        int maxLineBelow2 = below2 - above2;
        int v2 = v + maxLineBelow2 + extra;
        int i7 = this.mColumns;
        lines[off + i7 + 0] = end;
        lines[off + i7 + i4] = v2;
        int i8 = off + 0;
        int i9 = lines[i8];
        if (hasTab) {
            c = 0;
        }
        lines[i8] = i9 | c;
        lines[off + 4] = hyphenEdit;
        int i10 = off + 0;
        lines[i10] = lines[i10] | (dir << 30);
        this.mLineDirections[j] = measured.getDirections(i3 - i, end - i);
        this.mLineCount += i4;
        return v2;
    }

    private void calculateEllipsis(int lineStart, int lineEnd, MeasuredParagraph measured, int widthStart, float avail, TextUtils.TruncateAt where, int line, float textWidth, TextPaint paint, boolean forceEllipsis) {
        float avail2 = avail - getTotalInsets(line);
        if (textWidth <= avail2 && !forceEllipsis) {
            int[] iArr = this.mLines;
            int i = this.mColumns;
            iArr[(i * line) + 5] = 0;
            iArr[(i * line) + 6] = 0;
            return;
        }
        float ellipsisWidth = paint.measureText(TextUtils.getEllipsisString(where));
        int ellipsisStart = 0;
        int ellipsisCount = 0;
        int len = lineEnd - lineStart;
        if (where == TextUtils.TruncateAt.START) {
            if (this.mMaximumVisibleLineCount == 1) {
                float sum = 0.0f;
                int i2 = len;
                while (true) {
                    if (i2 <= 0) {
                        break;
                    }
                    float w = measured.getCharWidthAt(((i2 - 1) + lineStart) - widthStart);
                    if (w + sum + ellipsisWidth > avail2) {
                        while (i2 < len && measured.getCharWidthAt((i2 + lineStart) - widthStart) == 0.0f) {
                            i2++;
                        }
                    } else {
                        sum += w;
                        i2--;
                    }
                }
                ellipsisStart = 0;
                ellipsisCount = i2;
            } else if (Log.isLoggable(TAG, 5)) {
                Log.w(TAG, "Start Ellipsis only supported with one line");
            }
        } else if (where == TextUtils.TruncateAt.END || where == TextUtils.TruncateAt.MARQUEE || where == TextUtils.TruncateAt.END_SMALL) {
            float sum2 = 0.0f;
            int i3 = 0;
            while (i3 < len) {
                float w2 = measured.getCharWidthAt((i3 + lineStart) - widthStart);
                if (w2 + sum2 + ellipsisWidth > avail2) {
                    break;
                }
                sum2 += w2;
                i3++;
            }
            ellipsisStart = i3;
            ellipsisCount = len - i3;
            if (forceEllipsis && ellipsisCount == 0 && len > 0) {
                ellipsisStart = len - 1;
                ellipsisCount = 1;
            }
        } else if (this.mMaximumVisibleLineCount == 1) {
            float lsum = 0.0f;
            float rsum = 0.0f;
            int right = len;
            float ravail = (avail2 - ellipsisWidth) / 2.0f;
            while (true) {
                if (right <= 0) {
                    break;
                }
                float w3 = measured.getCharWidthAt(((right - 1) + lineStart) - widthStart);
                if (w3 + rsum <= ravail) {
                    rsum += w3;
                    right--;
                } else {
                    while (right < len && measured.getCharWidthAt((right + lineStart) - widthStart) == 0.0f) {
                        right++;
                    }
                }
            }
            float lavail = (avail2 - ellipsisWidth) - rsum;
            int left = 0;
            while (left < right) {
                float w4 = measured.getCharWidthAt((left + lineStart) - widthStart);
                if (w4 + lsum > lavail) {
                    break;
                }
                lsum += w4;
                left++;
            }
            ellipsisStart = left;
            ellipsisCount = right - left;
        } else if (Log.isLoggable(TAG, 5)) {
            Log.w(TAG, "Middle Ellipsis only supported with one line");
        }
        this.mEllipsized = true;
        int[] iArr2 = this.mLines;
        int i4 = this.mColumns;
        iArr2[(i4 * line) + 5] = ellipsisStart;
        iArr2[(i4 * line) + 6] = ellipsisCount;
    }

    private float getTotalInsets(int line) {
        int totalIndent = 0;
        int[] iArr = this.mLeftIndents;
        if (iArr != null) {
            totalIndent = iArr[Math.min(line, iArr.length - 1)];
        }
        int[] iArr2 = this.mRightIndents;
        if (iArr2 != null) {
            totalIndent += iArr2[Math.min(line, iArr2.length - 1)];
        }
        return totalIndent;
    }

    @Override // android.text.Layout
    public int getLineForVertical(int vertical) {
        int high = this.mLineCount;
        int low = -1;
        int[] lines = this.mLines;
        while (high - low > 1) {
            int guess = (high + low) >> 1;
            if (lines[(this.mColumns * guess) + 1] > vertical) {
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

    @Override // android.text.Layout
    public int getLineCount() {
        return this.mLineCount;
    }

    @Override // android.text.Layout
    public int getLineTop(int line) {
        return this.mLines[(this.mColumns * line) + 1];
    }

    @Override // android.text.Layout
    public int getLineExtra(int line) {
        return this.mLines[(this.mColumns * line) + 3];
    }

    @Override // android.text.Layout
    public int getLineDescent(int line) {
        return this.mLines[(this.mColumns * line) + 2];
    }

    @Override // android.text.Layout
    public int getLineStart(int line) {
        return this.mLines[(this.mColumns * line) + 0] & 536870911;
    }

    @Override // android.text.Layout
    public int getParagraphDirection(int line) {
        return this.mLines[(this.mColumns * line) + 0] >> 30;
    }

    @Override // android.text.Layout
    public boolean getLineContainsTab(int line) {
        return (this.mLines[(this.mColumns * line) + 0] & 536870912) != 0;
    }

    @Override // android.text.Layout
    public final Layout.Directions getLineDirections(int line) {
        if (line > getLineCount()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return this.mLineDirections[line];
    }

    @Override // android.text.Layout
    public int getTopPadding() {
        return this.mTopPadding;
    }

    @Override // android.text.Layout
    public int getBottomPadding() {
        return this.mBottomPadding;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int packHyphenEdit(int start, int end) {
        return (start << 3) | end;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int unpackStartHyphenEdit(int packedHyphenEdit) {
        return (packedHyphenEdit & 24) >> 3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int unpackEndHyphenEdit(int packedHyphenEdit) {
        return packedHyphenEdit & 7;
    }

    @Override // android.text.Layout
    public int getStartHyphenEdit(int lineNumber) {
        return unpackStartHyphenEdit(this.mLines[(this.mColumns * lineNumber) + 4] & 255);
    }

    @Override // android.text.Layout
    public int getEndHyphenEdit(int lineNumber) {
        return unpackEndHyphenEdit(this.mLines[(this.mColumns * lineNumber) + 4] & 255);
    }

    @Override // android.text.Layout
    public int getIndentAdjust(int line, Layout.Alignment align) {
        if (align == Layout.Alignment.ALIGN_LEFT) {
            int[] iArr = this.mLeftIndents;
            if (iArr == null) {
                return 0;
            }
            return iArr[Math.min(line, iArr.length - 1)];
        } else if (align == Layout.Alignment.ALIGN_RIGHT) {
            int[] iArr2 = this.mRightIndents;
            if (iArr2 == null) {
                return 0;
            }
            return -iArr2[Math.min(line, iArr2.length - 1)];
        } else if (align == Layout.Alignment.ALIGN_CENTER) {
            int left = 0;
            int[] iArr3 = this.mLeftIndents;
            if (iArr3 != null) {
                left = iArr3[Math.min(line, iArr3.length - 1)];
            }
            int right = 0;
            int[] iArr4 = this.mRightIndents;
            if (iArr4 != null) {
                right = iArr4[Math.min(line, iArr4.length - 1)];
            }
            return (left - right) >> 1;
        } else {
            throw new AssertionError("unhandled alignment " + align);
        }
    }

    @Override // android.text.Layout
    public int getEllipsisCount(int line) {
        int i = this.mColumns;
        if (i < 7) {
            return 0;
        }
        return this.mLines[(i * line) + 6];
    }

    @Override // android.text.Layout
    public int getEllipsisStart(int line) {
        int i = this.mColumns;
        if (i < 7) {
            return 0;
        }
        return this.mLines[(i * line) + 5];
    }

    @Override // android.text.Layout
    public int getEllipsizedWidth() {
        return this.mEllipsizedWidth;
    }

    @Override // android.text.Layout
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public int getHeight(boolean cap) {
        int i;
        if (cap && this.mLineCount > this.mMaximumVisibleLineCount && this.mMaxLineHeight == -1 && Log.isLoggable(TAG, 5)) {
            Log.w(TAG, "maxLineHeight should not be -1.  maxLines:" + this.mMaximumVisibleLineCount + " lineCount:" + this.mLineCount);
        }
        return (!cap || this.mLineCount <= this.mMaximumVisibleLineCount || (i = this.mMaxLineHeight) == -1) ? super.getHeight() : i;
    }

    /* loaded from: classes2.dex */
    static class LineBreaks {
        private static final int INITIAL_SIZE = 16;
        @UnsupportedAppUsage
        public int[] breaks = new int[16];
        @UnsupportedAppUsage
        public float[] widths = new float[16];
        @UnsupportedAppUsage
        public float[] ascents = new float[16];
        @UnsupportedAppUsage
        public float[] descents = new float[16];
        @UnsupportedAppUsage
        public int[] flags = new int[16];

        LineBreaks() {
        }
    }
}
