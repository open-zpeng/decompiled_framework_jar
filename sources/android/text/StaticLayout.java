package android.text;

import android.graphics.Paint;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pools;
import com.android.internal.util.ArrayUtils;
import dalvik.annotation.optimization.CriticalNative;
import dalvik.annotation.optimization.FastNative;
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
    public protected static final int ELLIPSIS_START = 5;
    private static final int EXTRA = 3;
    private static final double EXTRA_ROUNDING = 0.5d;
    private static final int HYPHEN = 4;
    private static final int HYPHEN_MASK = 255;
    private static final int START = 0;
    private static final int START_MASK = 536870911;
    private static final int TAB = 0;
    private static final int TAB_INCREMENT = 20;
    private static final int TAB_MASK = 536870912;
    static final String TAG = "StaticLayout";
    private static final int TOP = 1;
    private int mBottomPadding;
    public protected int mColumns;
    private boolean mEllipsized;
    private int mEllipsizedWidth;
    private int[] mLeftIndents;
    private int[] mLeftPaddings;
    public protected int mLineCount;
    public protected Layout.Directions[] mLineDirections;
    public protected int[] mLines;
    private int mMaxLineHeight;
    public protected int mMaximumVisibleLineCount;
    private int[] mRightIndents;
    private int[] mRightPaddings;
    private int mTopPadding;

    private static native int nComputeLineBreaks(long j, char[] cArr, long j2, int i, float f, int i2, float f2, int[] iArr, int i3, int i4, LineBreaks lineBreaks, int i5, int[] iArr2, float[] fArr, float[] fArr2, float[] fArr3, int[] iArr3, float[] fArr4);

    @CriticalNative
    private static native void nFinish(long j);

    @FastNative
    private static native long nInit(int i, int i2, boolean z, int[] iArr, int[] iArr2, int[] iArr3);

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
        private int[] mLeftPaddings;
        private int mMaxLines;
        private TextPaint mPaint;
        private int[] mRightIndents;
        private int[] mRightPaddings;
        private float mSpacingAdd;
        private float mSpacingMult;
        private int mStart;
        private CharSequence mText;
        private TextDirectionHeuristic mTextDir;
        private int mWidth;

        private synchronized Builder() {
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
        public static synchronized void recycle(Builder b) {
            b.mPaint = null;
            b.mText = null;
            b.mLeftIndents = null;
            b.mRightIndents = null;
            b.mLeftPaddings = null;
            b.mRightPaddings = null;
            sPool.release(b);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized void finish() {
            this.mText = null;
            this.mPaint = null;
            this.mLeftIndents = null;
            this.mRightIndents = null;
            this.mLeftPaddings = null;
            this.mRightPaddings = null;
        }

        public Builder setText(CharSequence source) {
            return setText(source, 0, source.length());
        }

        public synchronized Builder setText(CharSequence source, int start, int end) {
            this.mText = source;
            this.mStart = start;
            this.mEnd = end;
            return this;
        }

        public synchronized Builder setPaint(TextPaint paint) {
            this.mPaint = paint;
            return this;
        }

        public synchronized Builder setWidth(int width) {
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

        public synchronized Builder setAvailablePaddings(int[] leftPaddings, int[] rightPaddings) {
            this.mLeftPaddings = leftPaddings;
            this.mRightPaddings = rightPaddings;
            return this;
        }

        public Builder setJustificationMode(int justificationMode) {
            this.mJustificationMode = justificationMode;
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized Builder setAddLastLineLineSpacing(boolean value) {
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
    @java.lang.Deprecated
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private protected StaticLayout(java.lang.CharSequence r15, int r16, int r17, android.text.TextPaint r18, int r19, android.text.Layout.Alignment r20, android.text.TextDirectionHeuristic r21, float r22, float r23, boolean r24, android.text.TextUtils.TruncateAt r25, int r26, int r27) {
        /*
            r14 = this;
            r8 = r14
            r9 = r15
            r10 = r25
            r11 = r26
            r12 = r27
            if (r10 != 0) goto Ld
        Lb:
            r1 = r9
            goto L1e
        Ld:
            boolean r0 = r9 instanceof android.text.Spanned
            if (r0 == 0) goto L18
            android.text.Layout$SpannedEllipsizer r0 = new android.text.Layout$SpannedEllipsizer
            r0.<init>(r9)
        L16:
            r1 = r0
            goto L1e
        L18:
            android.text.Layout$Ellipsizer r0 = new android.text.Layout$Ellipsizer
            r0.<init>(r9)
            goto L16
        L1e:
            r0 = r8
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
            if (r10 == 0) goto L77
            java.lang.CharSequence r6 = r8.getText()
            android.text.Layout$Ellipsizer r6 = (android.text.Layout.Ellipsizer) r6
            r6.mLayout = r8
            r6.mWidth = r11
            r6.mMethod = r10
            r8.mEllipsizedWidth = r11
            r7 = 7
            r8.mColumns = r7
            r6 = r19
            goto L7e
        L77:
            r6 = 5
            r8.mColumns = r6
            r6 = r19
            r8.mEllipsizedWidth = r6
        L7e:
            java.lang.Class<android.text.Layout$Directions> r7 = android.text.Layout.Directions.class
            r13 = 2
            java.lang.Object[] r7 = com.android.internal.util.ArrayUtils.newUnpaddedArray(r7, r13)
            android.text.Layout$Directions[] r7 = (android.text.Layout.Directions[]) r7
            r8.mLineDirections = r7
            int r7 = r8.mColumns
            int r13 = r13 * r7
            int[] r7 = com.android.internal.util.ArrayUtils.newUnpaddedIntArray(r13)
            r8.mLines = r7
            r8.mMaximumVisibleLineCount = r12
            boolean r7 = android.text.StaticLayout.Builder.access$100(r0)
            boolean r13 = android.text.StaticLayout.Builder.access$100(r0)
            r8.generate(r0, r7, r13)
            android.text.StaticLayout.Builder.access$200(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.StaticLayout.<init>(java.lang.CharSequence, int, int, android.text.TextPaint, int, android.text.Layout$Alignment, android.text.TextDirectionHeuristic, float, float, boolean, android.text.TextUtils$TruncateAt, int, int):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized StaticLayout(CharSequence text) {
        super(text, null, 0, null, 0.0f, 0.0f);
        this.mMaxLineHeight = -1;
        this.mMaximumVisibleLineCount = Integer.MAX_VALUE;
        this.mColumns = 7;
        this.mLineDirections = (Layout.Directions[]) ArrayUtils.newUnpaddedArray(Layout.Directions.class, 2);
        this.mLines = ArrayUtils.newUnpaddedIntArray(2 * this.mColumns);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized StaticLayout(android.text.StaticLayout.Builder r11) {
        /*
            r10 = this;
            android.text.TextUtils$TruncateAt r0 = android.text.StaticLayout.Builder.access$300(r11)
            if (r0 != 0) goto Lc
            java.lang.CharSequence r0 = android.text.StaticLayout.Builder.access$400(r11)
        La:
            r3 = r0
            goto L28
        Lc:
            java.lang.CharSequence r0 = android.text.StaticLayout.Builder.access$400(r11)
            boolean r0 = r0 instanceof android.text.Spanned
            if (r0 == 0) goto L1e
            android.text.Layout$SpannedEllipsizer r0 = new android.text.Layout$SpannedEllipsizer
            java.lang.CharSequence r1 = android.text.StaticLayout.Builder.access$400(r11)
            r0.<init>(r1)
            goto La
        L1e:
            android.text.Layout$Ellipsizer r0 = new android.text.Layout$Ellipsizer
            java.lang.CharSequence r1 = android.text.StaticLayout.Builder.access$400(r11)
            r0.<init>(r1)
            goto La
        L28:
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
            if (r0 == 0) goto L70
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
            goto L79
        L70:
            r0 = 5
            r10.mColumns = r0
            int r0 = android.text.StaticLayout.Builder.access$600(r11)
            r10.mEllipsizedWidth = r0
        L79:
            java.lang.Class<android.text.Layout$Directions> r0 = android.text.Layout.Directions.class
            r1 = 2
            java.lang.Object[] r0 = com.android.internal.util.ArrayUtils.newUnpaddedArray(r0, r1)
            android.text.Layout$Directions[] r0 = (android.text.Layout.Directions[]) r0
            r10.mLineDirections = r0
            int r0 = r10.mColumns
            int r1 = r1 * r0
            int[] r0 = com.android.internal.util.ArrayUtils.newUnpaddedIntArray(r1)
            r10.mLines = r0
            int r0 = android.text.StaticLayout.Builder.access$1200(r11)
            r10.mMaximumVisibleLineCount = r0
            int[] r0 = android.text.StaticLayout.Builder.access$1300(r11)
            r10.mLeftIndents = r0
            int[] r0 = android.text.StaticLayout.Builder.access$1400(r11)
            r10.mRightIndents = r0
            int[] r0 = android.text.StaticLayout.Builder.access$1500(r11)
            r10.mLeftPaddings = r0
            int[] r0 = android.text.StaticLayout.Builder.access$1600(r11)
            r10.mRightPaddings = r0
            int r0 = android.text.StaticLayout.Builder.access$1700(r11)
            r10.setJustificationMode(r0)
            boolean r0 = android.text.StaticLayout.Builder.access$100(r11)
            boolean r1 = android.text.StaticLayout.Builder.access$100(r11)
            r10.generate(r11, r0, r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.StaticLayout.<init>(android.text.StaticLayout$Builder):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:138:0x0384  */
    /* JADX WARN: Removed duplicated region for block: B:142:0x038b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:146:0x0395 A[Catch: all -> 0x0348, TryCatch #6 {all -> 0x0348, blocks: (B:128:0x033e, B:130:0x0343, B:144:0x038f, B:146:0x0395, B:148:0x0399, B:155:0x03b1, B:152:0x03a5, B:154:0x03a9, B:151:0x03a1, B:156:0x03ba), top: B:296:0x033e }] */
    /* JADX WARN: Removed duplicated region for block: B:160:0x03e0  */
    /* JADX WARN: Removed duplicated region for block: B:239:0x06c9 A[LOOP:0: B:53:0x0147->B:239:0x06c9, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:255:0x07c1  */
    /* JADX WARN: Removed duplicated region for block: B:263:0x07dd  */
    /* JADX WARN: Removed duplicated region for block: B:266:0x07e6  */
    /* JADX WARN: Removed duplicated region for block: B:275:0x0843  */
    /* JADX WARN: Removed duplicated region for block: B:294:0x0338 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:333:0x07a1 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:334:0x06c5 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x012f  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x014b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized void generate(android.text.StaticLayout.Builder r99, boolean r100, boolean r101) {
        /*
            Method dump skipped, instructions count: 2163
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.StaticLayout.generate(android.text.StaticLayout$Builder, boolean, boolean):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:50:0x012f  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0147  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x01e3  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x01e6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized int out(java.lang.CharSequence r32, int r33, int r34, int r35, int r36, int r37, int r38, int r39, float r40, float r41, android.text.style.LineHeightSpan[] r42, int[] r43, android.graphics.Paint.FontMetricsInt r44, int r45, boolean r46, android.text.MeasuredParagraph r47, int r48, boolean r49, boolean r50, boolean r51, char[] r52, float[] r53, int r54, android.text.TextUtils.TruncateAt r55, float r56, float r57, android.text.TextPaint r58, boolean r59) {
        /*
            Method dump skipped, instructions count: 560
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.StaticLayout.out(java.lang.CharSequence, int, int, int, int, int, int, int, float, float, android.text.style.LineHeightSpan[], int[], android.graphics.Paint$FontMetricsInt, int, boolean, android.text.MeasuredParagraph, int, boolean, boolean, boolean, char[], float[], int, android.text.TextUtils$TruncateAt, float, float, android.text.TextPaint, boolean):int");
    }

    private synchronized void calculateEllipsis(int lineStart, int lineEnd, float[] widths, int widthStart, float avail, TextUtils.TruncateAt where, int line, float textWidth, TextPaint paint, boolean forceEllipsis) {
        int i;
        int right;
        float avail2 = avail - getTotalInsets(line);
        if (textWidth <= avail2 && !forceEllipsis) {
            this.mLines[(this.mColumns * line) + 5] = 0;
            this.mLines[(this.mColumns * line) + 6] = 0;
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
                    float w = widths[((i2 - 1) + lineStart) - widthStart];
                    if (w + sum + ellipsisWidth > avail2) {
                        while (i2 < len && widths[(i2 + lineStart) - widthStart] == 0.0f) {
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
            while (true) {
                i = i3;
                if (i >= len) {
                    break;
                }
                float w2 = widths[(i + lineStart) - widthStart];
                if (w2 + sum2 + ellipsisWidth > avail2) {
                    break;
                }
                sum2 += w2;
                i3 = i + 1;
            }
            int ellipsisStart2 = i;
            int ellipsisStart3 = len - i;
            if (forceEllipsis && ellipsisStart3 == 0 && len > 0) {
                ellipsisStart2 = len - 1;
                ellipsisCount = 1;
            } else {
                ellipsisCount = ellipsisStart3;
            }
            ellipsisStart = ellipsisStart2;
        } else if (this.mMaximumVisibleLineCount == 1) {
            float rsum = 0.0f;
            int right2 = len;
            float ravail = (avail2 - ellipsisWidth) / 2.0f;
            while (true) {
                if (right2 > 0) {
                    float w3 = widths[((right2 - 1) + lineStart) - widthStart];
                    if (w3 + rsum > ravail) {
                        right = right2;
                        while (right < len && widths[(right + lineStart) - widthStart] == 0.0f) {
                            right++;
                        }
                    } else {
                        rsum += w3;
                        right2--;
                    }
                } else {
                    right = right2;
                    break;
                }
            }
            float lavail = (avail2 - ellipsisWidth) - rsum;
            float lsum = 0.0f;
            int left = 0;
            while (left < right) {
                float w4 = widths[(left + lineStart) - widthStart];
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
        this.mLines[(this.mColumns * line) + 5] = ellipsisStart;
        this.mLines[(this.mColumns * line) + 6] = ellipsisCount;
    }

    private synchronized float getTotalInsets(int line) {
        int totalIndent = 0;
        if (this.mLeftIndents != null) {
            totalIndent = this.mLeftIndents[Math.min(line, this.mLeftIndents.length - 1)];
        }
        if (this.mRightIndents != null) {
            totalIndent += this.mRightIndents[Math.min(line, this.mRightIndents.length - 1)];
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
    public synchronized int getLineExtra(int line) {
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

    @Override // android.text.Layout
    public synchronized int getHyphen(int line) {
        return this.mLines[(this.mColumns * line) + 4] & 255;
    }

    @Override // android.text.Layout
    public synchronized int getIndentAdjust(int line, Layout.Alignment align) {
        if (align == Layout.Alignment.ALIGN_LEFT) {
            if (this.mLeftIndents == null) {
                return 0;
            }
            return this.mLeftIndents[Math.min(line, this.mLeftIndents.length - 1)];
        } else if (align == Layout.Alignment.ALIGN_RIGHT) {
            if (this.mRightIndents == null) {
                return 0;
            }
            return -this.mRightIndents[Math.min(line, this.mRightIndents.length - 1)];
        } else if (align == Layout.Alignment.ALIGN_CENTER) {
            int left = 0;
            if (this.mLeftIndents != null) {
                left = this.mLeftIndents[Math.min(line, this.mLeftIndents.length - 1)];
            }
            int right = 0;
            if (this.mRightIndents != null) {
                right = this.mRightIndents[Math.min(line, this.mRightIndents.length - 1)];
            }
            return (left - right) >> 1;
        } else {
            throw new AssertionError("unhandled alignment " + align);
        }
    }

    @Override // android.text.Layout
    public int getEllipsisCount(int line) {
        if (this.mColumns < 7) {
            return 0;
        }
        return this.mLines[(this.mColumns * line) + 6];
    }

    @Override // android.text.Layout
    public int getEllipsisStart(int line) {
        if (this.mColumns < 7) {
            return 0;
        }
        return this.mLines[(this.mColumns * line) + 5];
    }

    @Override // android.text.Layout
    public int getEllipsizedWidth() {
        return this.mEllipsizedWidth;
    }

    private protected int getHeight(boolean cap) {
        if (cap && this.mLineCount >= this.mMaximumVisibleLineCount && this.mMaxLineHeight == -1 && Log.isLoggable(TAG, 5)) {
            Log.w(TAG, "maxLineHeight should not be -1.  maxLines:" + this.mMaximumVisibleLineCount + " lineCount:" + this.mLineCount);
        }
        return (!cap || this.mLineCount < this.mMaximumVisibleLineCount || this.mMaxLineHeight == -1) ? super.getHeight() : this.mMaxLineHeight;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class LineBreaks {
        private static final int INITIAL_SIZE = 16;
        private protected int[] breaks = new int[16];
        private protected float[] widths = new float[16];
        private protected float[] ascents = new float[16];
        private protected float[] descents = new float[16];
        private protected int[] flags = new int[16];

        synchronized LineBreaks() {
        }
    }
}
