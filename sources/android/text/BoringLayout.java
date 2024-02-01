package android.text;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.Layout;
import android.text.TextUtils;
import android.text.style.ParagraphStyle;
/* loaded from: classes2.dex */
public class BoringLayout extends Layout implements TextUtils.EllipsizeCallback {
    int mBottom;
    private int mBottomPadding;
    int mDesc;
    private String mDirect;
    private int mEllipsizedCount;
    private int mEllipsizedStart;
    private int mEllipsizedWidth;
    private float mMax;
    private Paint mPaint;
    private int mTopPadding;

    public static BoringLayout make(CharSequence source, TextPaint paint, int outerWidth, Layout.Alignment align, float spacingMult, float spacingAdd, Metrics metrics, boolean includePad) {
        return new BoringLayout(source, paint, outerWidth, align, spacingMult, spacingAdd, metrics, includePad);
    }

    public static BoringLayout make(CharSequence source, TextPaint paint, int outerWidth, Layout.Alignment align, float spacingmult, float spacingadd, Metrics metrics, boolean includePad, TextUtils.TruncateAt ellipsize, int ellipsizedWidth) {
        return new BoringLayout(source, paint, outerWidth, align, spacingmult, spacingadd, metrics, includePad, ellipsize, ellipsizedWidth);
    }

    public BoringLayout replaceOrMake(CharSequence source, TextPaint paint, int outerwidth, Layout.Alignment align, float spacingMult, float spacingAdd, Metrics metrics, boolean includePad) {
        replaceWith(source, paint, outerwidth, align, spacingMult, spacingAdd);
        this.mEllipsizedWidth = outerwidth;
        this.mEllipsizedStart = 0;
        this.mEllipsizedCount = 0;
        init(source, paint, align, metrics, includePad, true);
        return this;
    }

    public BoringLayout replaceOrMake(CharSequence source, TextPaint paint, int outerWidth, Layout.Alignment align, float spacingMult, float spacingAdd, Metrics metrics, boolean includePad, TextUtils.TruncateAt ellipsize, int ellipsizedWidth) {
        boolean trust;
        if (ellipsize == null || ellipsize == TextUtils.TruncateAt.MARQUEE) {
            replaceWith(source, paint, outerWidth, align, spacingMult, spacingAdd);
            this.mEllipsizedWidth = outerWidth;
            this.mEllipsizedStart = 0;
            this.mEllipsizedCount = 0;
            trust = true;
        } else {
            replaceWith(TextUtils.ellipsize(source, paint, ellipsizedWidth, ellipsize, true, this), paint, outerWidth, align, spacingMult, spacingAdd);
            this.mEllipsizedWidth = ellipsizedWidth;
            trust = false;
        }
        init(getText(), paint, align, metrics, includePad, trust);
        return this;
    }

    public BoringLayout(CharSequence source, TextPaint paint, int outerwidth, Layout.Alignment align, float spacingMult, float spacingAdd, Metrics metrics, boolean includePad) {
        super(source, paint, outerwidth, align, spacingMult, spacingAdd);
        this.mEllipsizedWidth = outerwidth;
        this.mEllipsizedStart = 0;
        this.mEllipsizedCount = 0;
        init(source, paint, align, metrics, includePad, true);
    }

    public BoringLayout(CharSequence source, TextPaint paint, int outerWidth, Layout.Alignment align, float spacingMult, float spacingAdd, Metrics metrics, boolean includePad, TextUtils.TruncateAt ellipsize, int ellipsizedWidth) {
        super(source, paint, outerWidth, align, spacingMult, spacingAdd);
        boolean trust;
        if (ellipsize == null || ellipsize == TextUtils.TruncateAt.MARQUEE) {
            this.mEllipsizedWidth = outerWidth;
            this.mEllipsizedStart = 0;
            this.mEllipsizedCount = 0;
            trust = true;
        } else {
            replaceWith(TextUtils.ellipsize(source, paint, ellipsizedWidth, ellipsize, true, this), paint, outerWidth, align, spacingMult, spacingAdd);
            this.mEllipsizedWidth = ellipsizedWidth;
            trust = false;
        }
        init(getText(), paint, align, metrics, includePad, trust);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0022  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x002d  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x003b  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0042  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x006f  */
    /* JADX WARN: Removed duplicated region for block: B:21:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    synchronized void init(java.lang.CharSequence r19, android.text.TextPaint r20, android.text.Layout.Alignment r21, android.text.BoringLayout.Metrics r22, boolean r23, boolean r24) {
        /*
            r18 = this;
            r0 = r18
            r1 = r22
            r11 = r19
            boolean r2 = r11 instanceof java.lang.String
            r12 = 0
            if (r2 == 0) goto L18
            android.text.Layout$Alignment r2 = android.text.Layout.Alignment.ALIGN_NORMAL
            r13 = r21
            if (r13 != r2) goto L1a
            java.lang.String r2 = r19.toString()
            r0.mDirect = r2
            goto L1c
        L18:
            r13 = r21
        L1a:
            r0.mDirect = r12
        L1c:
            r14 = r20
            r0.mPaint = r14
            if (r23 == 0) goto L2d
            int r2 = r1.bottom
            int r3 = r1.top
            int r2 = r2 - r3
            int r3 = r1.bottom
            r0.mDesc = r3
        L2b:
            r10 = r2
            goto L37
        L2d:
            int r2 = r1.descent
            int r3 = r1.ascent
            int r2 = r2 - r3
            int r3 = r1.descent
            r0.mDesc = r3
            goto L2b
        L37:
            r0.mBottom = r10
            if (r24 == 0) goto L42
            int r2 = r1.width
            float r2 = (float) r2
            r0.mMax = r2
            r15 = r10
            goto L6d
        L42:
            android.text.TextLine r9 = android.text.TextLine.obtain()
            r5 = 0
            int r6 = r19.length()
            r7 = 1
            android.text.Layout$Directions r8 = android.text.Layout.DIRS_ALL_LEFT_TO_RIGHT
            r15 = 0
            r16 = 0
            r2 = r9
            r3 = r14
            r4 = r11
            r12 = r9
            r9 = r15
            r15 = r10
            r10 = r16
            r2.set(r3, r4, r5, r6, r7, r8, r9, r10)
            r2 = 0
            float r2 = r12.metrics(r2)
            double r2 = (double) r2
            double r2 = java.lang.Math.ceil(r2)
            int r2 = (int) r2
            float r2 = (float) r2
            r0.mMax = r2
            android.text.TextLine.recycle(r12)
        L6d:
            if (r23 == 0) goto L7d
            int r2 = r1.top
            int r3 = r1.ascent
            int r2 = r2 - r3
            r0.mTopPadding = r2
            int r2 = r1.bottom
            int r3 = r1.descent
            int r2 = r2 - r3
            r0.mBottomPadding = r2
        L7d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.BoringLayout.init(java.lang.CharSequence, android.text.TextPaint, android.text.Layout$Alignment, android.text.BoringLayout$Metrics, boolean, boolean):void");
    }

    public static Metrics isBoring(CharSequence text, TextPaint paint) {
        return isBoring(text, paint, TextDirectionHeuristics.FIRSTSTRONG_LTR, null);
    }

    public static Metrics isBoring(CharSequence text, TextPaint paint, Metrics metrics) {
        return isBoring(text, paint, TextDirectionHeuristics.FIRSTSTRONG_LTR, metrics);
    }

    private static synchronized boolean hasAnyInterestingChars(CharSequence text, int textLength) {
        char[] buffer = TextUtils.obtain(500);
        for (int start = 0; start < textLength; start += 500) {
            try {
                int end = Math.min(start + 500, textLength);
                TextUtils.getChars(text, start, end, buffer, 0);
                int len = end - start;
                for (int i = 0; i < len; i++) {
                    char c = buffer[i];
                    if (c == '\n' || c == '\t' || TextUtils.couldAffectRtl(c)) {
                        TextUtils.recycle(buffer);
                        return true;
                    }
                }
            } finally {
                TextUtils.recycle(buffer);
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Metrics isBoring(CharSequence text, TextPaint paint, TextDirectionHeuristic textDir, Metrics metrics) {
        int textLength = text.length();
        if (hasAnyInterestingChars(text, textLength)) {
            return null;
        }
        if (textDir == null || !textDir.isRtl(text, 0, textLength)) {
            if (text instanceof Spanned) {
                Spanned sp = (Spanned) text;
                Object[] styles = sp.getSpans(0, textLength, ParagraphStyle.class);
                if (styles.length > 0) {
                    return null;
                }
            }
            Metrics fm = metrics;
            if (fm == null) {
                fm = new Metrics();
            } else {
                fm.reset();
            }
            Metrics fm2 = fm;
            TextLine line = TextLine.obtain();
            line.set(paint, text, 0, textLength, 1, Layout.DIRS_ALL_LEFT_TO_RIGHT, false, null);
            fm2.width = (int) Math.ceil(line.metrics(fm2));
            TextLine.recycle(line);
            return fm2;
        }
        return null;
    }

    @Override // android.text.Layout
    public int getHeight() {
        return this.mBottom;
    }

    @Override // android.text.Layout
    public int getLineCount() {
        return 1;
    }

    @Override // android.text.Layout
    public int getLineTop(int line) {
        if (line == 0) {
            return 0;
        }
        return this.mBottom;
    }

    @Override // android.text.Layout
    public int getLineDescent(int line) {
        return this.mDesc;
    }

    @Override // android.text.Layout
    public int getLineStart(int line) {
        if (line == 0) {
            return 0;
        }
        return getText().length();
    }

    @Override // android.text.Layout
    public int getParagraphDirection(int line) {
        return 1;
    }

    @Override // android.text.Layout
    public boolean getLineContainsTab(int line) {
        return false;
    }

    @Override // android.text.Layout
    public float getLineMax(int line) {
        return this.mMax;
    }

    @Override // android.text.Layout
    public float getLineWidth(int line) {
        if (line == 0) {
            return this.mMax;
        }
        return 0.0f;
    }

    @Override // android.text.Layout
    public final Layout.Directions getLineDirections(int line) {
        return Layout.DIRS_ALL_LEFT_TO_RIGHT;
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
    public int getEllipsisCount(int line) {
        return this.mEllipsizedCount;
    }

    @Override // android.text.Layout
    public int getEllipsisStart(int line) {
        return this.mEllipsizedStart;
    }

    @Override // android.text.Layout
    public int getEllipsizedWidth() {
        return this.mEllipsizedWidth;
    }

    @Override // android.text.Layout
    public void draw(Canvas c, Path highlight, Paint highlightpaint, int cursorOffset) {
        if (this.mDirect != null && highlight == null) {
            c.drawText(this.mDirect, 0.0f, this.mBottom - this.mDesc, this.mPaint);
        } else {
            super.draw(c, highlight, highlightpaint, cursorOffset);
        }
    }

    @Override // android.text.TextUtils.EllipsizeCallback
    public void ellipsized(int start, int end) {
        this.mEllipsizedStart = start;
        this.mEllipsizedCount = end - start;
    }

    /* loaded from: classes2.dex */
    public static class Metrics extends Paint.FontMetricsInt {
        public int width;

        @Override // android.graphics.Paint.FontMetricsInt
        public String toString() {
            return super.toString() + " width=" + this.width;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void reset() {
            this.top = 0;
            this.bottom = 0;
            this.ascent = 0;
            this.descent = 0;
            this.width = 0;
            this.leading = 0;
        }
    }
}
