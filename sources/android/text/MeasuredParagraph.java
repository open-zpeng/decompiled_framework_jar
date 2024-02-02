package android.text;

import android.graphics.Paint;
import android.graphics.Rect;
import android.text.AutoGrowArray;
import android.text.Layout;
import android.text.style.MetricAffectingSpan;
import android.text.style.ReplacementSpan;
import android.util.Pools;
import dalvik.annotation.optimization.CriticalNative;
import java.util.Arrays;
import libcore.util.NativeAllocationRegistry;
/* loaded from: classes2.dex */
public class MeasuredParagraph {
    private static final char OBJECT_REPLACEMENT_CHARACTER = 65532;
    private Paint.FontMetricsInt mCachedFm;
    private char[] mCopiedBuffer;
    private boolean mLtrWithoutBidi;
    private Runnable mNativeObjectCleaner;
    private int mParaDir;
    private Spanned mSpanned;
    private int mTextLength;
    private int mTextStart;
    private float mWholeWidth;
    private static final NativeAllocationRegistry sRegistry = new NativeAllocationRegistry(MeasuredParagraph.class.getClassLoader(), nGetReleaseFunc(), 1024);
    private static final Pools.SynchronizedPool<MeasuredParagraph> sPool = new Pools.SynchronizedPool<>(1);
    private AutoGrowArray.ByteArray mLevels = new AutoGrowArray.ByteArray();
    private AutoGrowArray.FloatArray mWidths = new AutoGrowArray.FloatArray();
    private AutoGrowArray.IntArray mSpanEndCache = new AutoGrowArray.IntArray(4);
    private AutoGrowArray.IntArray mFontMetrics = new AutoGrowArray.IntArray(16);
    private long mNativePtr = 0;
    private TextPaint mCachedPaint = new TextPaint();

    private static native void nAddReplacementRun(long j, long j2, int i, int i2, float f);

    private static native void nAddStyleRun(long j, long j2, int i, int i2, boolean z);

    private static native long nBuildNativeMeasuredParagraph(long j, char[] cArr, boolean z, boolean z2);

    private static native void nFreeBuilder(long j);

    private static native void nGetBounds(long j, char[] cArr, int i, int i2, Rect rect);

    @CriticalNative
    private static native int nGetMemoryUsage(long j);

    @CriticalNative
    private static native long nGetReleaseFunc();

    @CriticalNative
    private static native float nGetWidth(long j, int i, int i2);

    private static native long nInitBuilder();

    private synchronized MeasuredParagraph() {
    }

    private static synchronized MeasuredParagraph obtain() {
        MeasuredParagraph mt = sPool.acquire();
        return mt != null ? mt : new MeasuredParagraph();
    }

    public synchronized void recycle() {
        release();
        sPool.release(this);
    }

    private synchronized void bindNativeObject(long nativePtr) {
        this.mNativePtr = nativePtr;
        this.mNativeObjectCleaner = sRegistry.registerNativeAllocation(this, nativePtr);
    }

    private synchronized void unbindNativeObject() {
        if (this.mNativePtr != 0) {
            this.mNativeObjectCleaner.run();
            this.mNativePtr = 0L;
        }
    }

    public synchronized void release() {
        reset();
        this.mLevels.clearWithReleasingLargeArray();
        this.mWidths.clearWithReleasingLargeArray();
        this.mFontMetrics.clearWithReleasingLargeArray();
        this.mSpanEndCache.clearWithReleasingLargeArray();
    }

    private synchronized void reset() {
        this.mSpanned = null;
        this.mCopiedBuffer = null;
        this.mWholeWidth = 0.0f;
        this.mLevels.clear();
        this.mWidths.clear();
        this.mFontMetrics.clear();
        this.mSpanEndCache.clear();
        unbindNativeObject();
    }

    public synchronized int getTextLength() {
        return this.mTextLength;
    }

    public synchronized char[] getChars() {
        return this.mCopiedBuffer;
    }

    public synchronized int getParagraphDir() {
        return this.mParaDir;
    }

    public synchronized Layout.Directions getDirections(int start, int end) {
        if (this.mLtrWithoutBidi) {
            return Layout.DIRS_ALL_LEFT_TO_RIGHT;
        }
        int length = end - start;
        return AndroidBidi.directions(this.mParaDir, this.mLevels.getRawArray(), start, this.mCopiedBuffer, start, length);
    }

    public synchronized float getWholeWidth() {
        return this.mWholeWidth;
    }

    public synchronized AutoGrowArray.FloatArray getWidths() {
        return this.mWidths;
    }

    public synchronized AutoGrowArray.IntArray getSpanEndCache() {
        return this.mSpanEndCache;
    }

    public synchronized AutoGrowArray.IntArray getFontMetrics() {
        return this.mFontMetrics;
    }

    public synchronized long getNativePtr() {
        return this.mNativePtr;
    }

    public synchronized float getWidth(int start, int end) {
        if (this.mNativePtr == 0) {
            float[] widths = this.mWidths.getRawArray();
            float r = 0.0f;
            for (int i = start; i < end; i++) {
                r += widths[i];
            }
            return r;
        }
        return nGetWidth(this.mNativePtr, start, end);
    }

    public synchronized void getBounds(int start, int end, Rect bounds) {
        nGetBounds(this.mNativePtr, this.mCopiedBuffer, start, end, bounds);
    }

    public static synchronized MeasuredParagraph buildForBidi(CharSequence text, int start, int end, TextDirectionHeuristic textDir, MeasuredParagraph recycle) {
        MeasuredParagraph mt = recycle == null ? obtain() : recycle;
        mt.resetAndAnalyzeBidi(text, start, end, textDir);
        return mt;
    }

    public static synchronized MeasuredParagraph buildForMeasurement(TextPaint paint, CharSequence text, int start, int end, TextDirectionHeuristic textDir, MeasuredParagraph recycle) {
        MeasuredParagraph mt = recycle == null ? obtain() : recycle;
        mt.resetAndAnalyzeBidi(text, start, end, textDir);
        mt.mWidths.resize(mt.mTextLength);
        if (mt.mTextLength == 0) {
            return mt;
        }
        if (mt.mSpanned == null) {
            mt.applyMetricsAffectingSpan(paint, null, start, end, 0L);
            return mt;
        }
        int spanStart = start;
        while (spanStart < end) {
            int spanEnd = mt.mSpanned.nextSpanTransition(spanStart, end, MetricAffectingSpan.class);
            MetricAffectingSpan[] spans = (MetricAffectingSpan[]) mt.mSpanned.getSpans(spanStart, spanEnd, MetricAffectingSpan.class);
            mt.applyMetricsAffectingSpan(paint, (MetricAffectingSpan[]) TextUtils.removeEmptySpans(spans, mt.mSpanned, MetricAffectingSpan.class), spanStart, spanEnd, 0L);
            spanStart = spanEnd;
            mt = mt;
        }
        return mt;
    }

    /* JADX WARN: Not initialized variable reg: 15, insn: 0x004f: MOVE  (r1 I:??[long, double]) = (r15 I:??[long, double] A[D('nativeBuilderPtr' long)]), block:B:21:0x004f */
    public static synchronized MeasuredParagraph buildForStaticLayout(TextPaint paint, CharSequence text, int start, int end, TextDirectionHeuristic textDir, boolean computeHyphenation, boolean computeLayout, MeasuredParagraph recycle) {
        long nativeBuilderPtr;
        long nativeBuilderPtr2;
        long nativeBuilderPtr3;
        long nativeBuilderPtr4;
        MeasuredParagraph mt = recycle == null ? obtain() : recycle;
        mt.resetAndAnalyzeBidi(text, start, end, textDir);
        if (mt.mTextLength == 0) {
            nativeBuilderPtr2 = nInitBuilder();
            try {
                mt.bindNativeObject(nBuildNativeMeasuredParagraph(nativeBuilderPtr2, mt.mCopiedBuffer, computeHyphenation, computeLayout));
                return mt;
            } finally {
                nFreeBuilder(nativeBuilderPtr2);
            }
        }
        long nativeBuilderPtr5 = nInitBuilder();
        try {
            try {
                if (mt.mSpanned == null) {
                    nativeBuilderPtr4 = nativeBuilderPtr5;
                    mt.applyMetricsAffectingSpan(paint, null, start, end, nativeBuilderPtr5);
                    mt.mSpanEndCache.append(end);
                } else {
                    nativeBuilderPtr4 = nativeBuilderPtr5;
                    int spanStart = start;
                    while (spanStart < end) {
                        int spanEnd = mt.mSpanned.nextSpanTransition(spanStart, end, MetricAffectingSpan.class);
                        MetricAffectingSpan[] spans = (MetricAffectingSpan[]) mt.mSpanned.getSpans(spanStart, spanEnd, MetricAffectingSpan.class);
                        int i = spanStart;
                        spanStart = spanEnd;
                        mt.applyMetricsAffectingSpan(paint, (MetricAffectingSpan[]) TextUtils.removeEmptySpans(spans, mt.mSpanned, MetricAffectingSpan.class), i, spanEnd, nativeBuilderPtr4);
                        mt.mSpanEndCache.append(spanStart);
                    }
                }
                try {
                    nativeBuilderPtr2 = nativeBuilderPtr4;
                    try {
                        mt.bindNativeObject(nBuildNativeMeasuredParagraph(nativeBuilderPtr2, mt.mCopiedBuffer, computeHyphenation, computeLayout));
                        return mt;
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    nativeBuilderPtr = nativeBuilderPtr4;
                }
            } catch (Throwable th3) {
                th = th3;
                nativeBuilderPtr = nativeBuilderPtr3;
            }
        } catch (Throwable th4) {
            th = th4;
            nativeBuilderPtr = nativeBuilderPtr5;
        }
    }

    private synchronized void resetAndAnalyzeBidi(CharSequence text, int start, int end, TextDirectionHeuristic textDir) {
        int bidiRequest;
        reset();
        this.mSpanned = text instanceof Spanned ? (Spanned) text : null;
        this.mTextStart = start;
        this.mTextLength = end - start;
        if (this.mCopiedBuffer == null || this.mCopiedBuffer.length != this.mTextLength) {
            this.mCopiedBuffer = new char[this.mTextLength];
        }
        TextUtils.getChars(text, start, end, this.mCopiedBuffer, 0);
        if (this.mSpanned != null) {
            ReplacementSpan[] spans = (ReplacementSpan[]) this.mSpanned.getSpans(start, end, ReplacementSpan.class);
            for (int i = 0; i < spans.length; i++) {
                int startInPara = this.mSpanned.getSpanStart(spans[i]) - start;
                int endInPara = this.mSpanned.getSpanEnd(spans[i]) - start;
                if (startInPara < 0) {
                    startInPara = 0;
                }
                if (endInPara > this.mTextLength) {
                    endInPara = this.mTextLength;
                }
                Arrays.fill(this.mCopiedBuffer, startInPara, endInPara, (char) OBJECT_REPLACEMENT_CHARACTER);
            }
        }
        if ((textDir == TextDirectionHeuristics.LTR || textDir == TextDirectionHeuristics.FIRSTSTRONG_LTR || textDir == TextDirectionHeuristics.ANYRTL_LTR) && TextUtils.doesNotNeedBidi(this.mCopiedBuffer, 0, this.mTextLength)) {
            this.mLevels.clear();
            this.mParaDir = 1;
            this.mLtrWithoutBidi = true;
            return;
        }
        if (textDir == TextDirectionHeuristics.LTR) {
            bidiRequest = 1;
        } else if (textDir == TextDirectionHeuristics.RTL) {
            bidiRequest = -1;
        } else if (textDir == TextDirectionHeuristics.FIRSTSTRONG_LTR) {
            bidiRequest = 2;
        } else if (textDir == TextDirectionHeuristics.FIRSTSTRONG_RTL) {
            bidiRequest = -2;
        } else {
            boolean isRtl = textDir.isRtl(this.mCopiedBuffer, 0, this.mTextLength);
            bidiRequest = isRtl ? -1 : 1;
        }
        this.mLevels.resize(this.mTextLength);
        this.mParaDir = AndroidBidi.bidi(bidiRequest, this.mCopiedBuffer, this.mLevels.getRawArray());
        this.mLtrWithoutBidi = false;
    }

    private synchronized void applyReplacementRun(ReplacementSpan replacement, int start, int end, long nativeBuilderPtr) {
        float width = replacement.getSize(this.mCachedPaint, this.mSpanned, start + this.mTextStart, end + this.mTextStart, this.mCachedFm);
        if (nativeBuilderPtr == 0) {
            this.mWidths.set(start, width);
            if (end > start + 1) {
                Arrays.fill(this.mWidths.getRawArray(), start + 1, end, 0.0f);
            }
            this.mWholeWidth += width;
            return;
        }
        nAddReplacementRun(nativeBuilderPtr, this.mCachedPaint.getNativeInstance(), start, end, width);
    }

    private synchronized void applyStyleRun(int start, int end, long nativeBuilderPtr) {
        int levelEnd;
        if (this.mLtrWithoutBidi) {
            if (nativeBuilderPtr == 0) {
                this.mWholeWidth += this.mCachedPaint.getTextRunAdvances(this.mCopiedBuffer, start, end - start, start, end - start, false, this.mWidths.getRawArray(), start);
                return;
            } else {
                nAddStyleRun(nativeBuilderPtr, this.mCachedPaint.getNativeInstance(), start, end, false);
                return;
            }
        }
        byte level = this.mLevels.get(start);
        int levelEnd2 = start + 1;
        byte level2 = level;
        int levelStart = start;
        while (true) {
            int levelEnd3 = levelEnd2;
            if (levelEnd3 != end && this.mLevels.get(levelEnd3) == level2) {
                levelEnd = levelEnd3;
            } else {
                boolean isRtl = (level2 & 1) != 0;
                if (nativeBuilderPtr == 0) {
                    int levelLength = levelEnd3 - levelStart;
                    this.mWholeWidth += this.mCachedPaint.getTextRunAdvances(this.mCopiedBuffer, levelStart, levelLength, levelStart, levelLength, isRtl, this.mWidths.getRawArray(), levelStart);
                    levelEnd = levelEnd3;
                } else {
                    levelEnd = levelEnd3;
                    nAddStyleRun(nativeBuilderPtr, this.mCachedPaint.getNativeInstance(), levelStart, levelEnd3, isRtl);
                }
                if (levelEnd != end) {
                    levelStart = levelEnd;
                    byte level3 = this.mLevels.get(levelEnd);
                    level2 = level3;
                } else {
                    return;
                }
            }
            levelEnd2 = levelEnd + 1;
        }
    }

    private synchronized void applyMetricsAffectingSpan(TextPaint paint, MetricAffectingSpan[] spans, int start, int end, long nativeBuilderPtr) {
        this.mCachedPaint.set(paint);
        this.mCachedPaint.baselineShift = 0;
        boolean needFontMetrics = nativeBuilderPtr != 0;
        if (needFontMetrics && this.mCachedFm == null) {
            this.mCachedFm = new Paint.FontMetricsInt();
        }
        ReplacementSpan replacement = null;
        if (spans != null) {
            for (MetricAffectingSpan span : spans) {
                if (span instanceof ReplacementSpan) {
                    replacement = (ReplacementSpan) span;
                } else {
                    span.updateMeasureState(this.mCachedPaint);
                }
            }
        }
        ReplacementSpan replacement2 = replacement;
        int startInCopiedBuffer = start - this.mTextStart;
        int endInCopiedBuffer = end - this.mTextStart;
        if (nativeBuilderPtr != 0) {
            this.mCachedPaint.getFontMetricsInt(this.mCachedFm);
        }
        if (replacement2 == null) {
            applyStyleRun(startInCopiedBuffer, endInCopiedBuffer, nativeBuilderPtr);
        } else {
            applyReplacementRun(replacement2, startInCopiedBuffer, endInCopiedBuffer, nativeBuilderPtr);
        }
        if (needFontMetrics) {
            if (this.mCachedPaint.baselineShift < 0) {
                this.mCachedFm.ascent += this.mCachedPaint.baselineShift;
                this.mCachedFm.top += this.mCachedPaint.baselineShift;
            } else {
                this.mCachedFm.descent += this.mCachedPaint.baselineShift;
                this.mCachedFm.bottom += this.mCachedPaint.baselineShift;
            }
            this.mFontMetrics.append(this.mCachedFm.top);
            this.mFontMetrics.append(this.mCachedFm.bottom);
            this.mFontMetrics.append(this.mCachedFm.ascent);
            this.mFontMetrics.append(this.mCachedFm.descent);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int breakText(int limit, boolean forwards, float width) {
        float[] w = this.mWidths.getRawArray();
        if (forwards) {
            int i = 0;
            while (i < limit) {
                width -= w[i];
                if (width < 0.0f) {
                    break;
                }
                i++;
            }
            while (i > 0 && this.mCopiedBuffer[i - 1] == ' ') {
                i--;
            }
            return i;
        }
        int i2 = limit - 1;
        while (i2 >= 0) {
            width -= w[i2];
            if (width < 0.0f) {
                break;
            }
            i2--;
        }
        while (i2 < limit - 1 && (this.mCopiedBuffer[i2 + 1] == ' ' || w[i2 + 1] == 0.0f)) {
            i2++;
        }
        return (limit - i2) - 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized float measure(int start, int limit) {
        float[] w = this.mWidths.getRawArray();
        float width = 0.0f;
        for (int i = start; i < limit; i++) {
            width += w[i];
        }
        return width;
    }

    public synchronized int getMemoryUsage() {
        return nGetMemoryUsage(this.mNativePtr);
    }
}
