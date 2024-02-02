package android.text;

import android.graphics.Rect;
import android.text.style.MetricAffectingSpan;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes2.dex */
public class PrecomputedText implements Spannable {
    private static final char LINE_FEED = '\n';
    private final int mEnd;
    private final ParagraphInfo[] mParagraphInfo;
    private final Params mParams;
    private final int mStart;
    private final SpannableString mText;

    /* loaded from: classes2.dex */
    public static final class Params {
        private final int mBreakStrategy;
        private final int mHyphenationFrequency;
        private final TextPaint mPaint;
        private final TextDirectionHeuristic mTextDir;

        /* loaded from: classes2.dex */
        public static class Builder {
            private final TextPaint mPaint;
            private TextDirectionHeuristic mTextDir = TextDirectionHeuristics.FIRSTSTRONG_LTR;
            private int mBreakStrategy = 1;
            private int mHyphenationFrequency = 1;

            public Builder(TextPaint paint) {
                this.mPaint = paint;
            }

            public Builder setBreakStrategy(int strategy) {
                this.mBreakStrategy = strategy;
                return this;
            }

            public Builder setHyphenationFrequency(int frequency) {
                this.mHyphenationFrequency = frequency;
                return this;
            }

            public Builder setTextDirection(TextDirectionHeuristic textDir) {
                this.mTextDir = textDir;
                return this;
            }

            public Params build() {
                return new Params(this.mPaint, this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
            }
        }

        public synchronized Params(TextPaint paint, TextDirectionHeuristic textDir, int strategy, int frequency) {
            this.mPaint = paint;
            this.mTextDir = textDir;
            this.mBreakStrategy = strategy;
            this.mHyphenationFrequency = frequency;
        }

        public TextPaint getTextPaint() {
            return this.mPaint;
        }

        public TextDirectionHeuristic getTextDirection() {
            return this.mTextDir;
        }

        public int getBreakStrategy() {
            return this.mBreakStrategy;
        }

        public int getHyphenationFrequency() {
            return this.mHyphenationFrequency;
        }

        public synchronized boolean isSameTextMetricsInternal(TextPaint paint, TextDirectionHeuristic textDir, int strategy, int frequency) {
            return this.mTextDir == textDir && this.mBreakStrategy == strategy && this.mHyphenationFrequency == frequency && this.mPaint.equalsForTextMeasurement(paint);
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o == null || !(o instanceof Params)) {
                return false;
            }
            Params param = (Params) o;
            return isSameTextMetricsInternal(param.mPaint, param.mTextDir, param.mBreakStrategy, param.mHyphenationFrequency);
        }

        public int hashCode() {
            return Objects.hash(Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), Float.valueOf(this.mPaint.getLetterSpacing()), Float.valueOf(this.mPaint.getWordSpacing()), Integer.valueOf(this.mPaint.getFlags()), this.mPaint.getTextLocales(), this.mPaint.getTypeface(), this.mPaint.getFontVariationSettings(), Boolean.valueOf(this.mPaint.isElegantTextHeight()), this.mTextDir, Integer.valueOf(this.mBreakStrategy), Integer.valueOf(this.mHyphenationFrequency));
        }

        public String toString() {
            return "{textSize=" + this.mPaint.getTextSize() + ", textScaleX=" + this.mPaint.getTextScaleX() + ", textSkewX=" + this.mPaint.getTextSkewX() + ", letterSpacing=" + this.mPaint.getLetterSpacing() + ", textLocale=" + this.mPaint.getTextLocales() + ", typeface=" + this.mPaint.getTypeface() + ", variationSettings=" + this.mPaint.getFontVariationSettings() + ", elegantTextHeight=" + this.mPaint.isElegantTextHeight() + ", textDir=" + this.mTextDir + ", breakStrategy=" + this.mBreakStrategy + ", hyphenationFrequency=" + this.mHyphenationFrequency + "}";
        }
    }

    /* loaded from: classes2.dex */
    public static class ParagraphInfo {
        public final MeasuredParagraph measured;
        public final int paragraphEnd;

        public synchronized ParagraphInfo(int paraEnd, MeasuredParagraph measured) {
            this.paragraphEnd = paraEnd;
            this.measured = measured;
        }
    }

    public static PrecomputedText create(CharSequence text, Params params) {
        ParagraphInfo[] paraInfo = createMeasuredParagraphs(text, params, 0, text.length(), true);
        return new PrecomputedText(text, 0, text.length(), params, paraInfo);
    }

    public static synchronized ParagraphInfo[] createMeasuredParagraphs(CharSequence text, Params params, int start, int end, boolean computeLayout) {
        int paraEnd;
        ArrayList<ParagraphInfo> result = new ArrayList<>();
        Preconditions.checkNotNull(text);
        Preconditions.checkNotNull(params);
        boolean needHyphenation = (params.getBreakStrategy() == 0 || params.getHyphenationFrequency() == 0) ? false : true;
        int paraStart = start;
        while (paraStart < end) {
            int paraEnd2 = TextUtils.indexOf(text, (char) LINE_FEED, paraStart, end);
            if (paraEnd2 < 0) {
                paraEnd = end;
            } else {
                paraEnd = paraEnd2 + 1;
            }
            int paraEnd3 = paraEnd;
            result.add(new ParagraphInfo(paraEnd3, MeasuredParagraph.buildForStaticLayout(params.getTextPaint(), text, paraStart, paraEnd3, params.getTextDirection(), needHyphenation, computeLayout, null)));
            paraStart = paraEnd3;
        }
        return (ParagraphInfo[]) result.toArray(new ParagraphInfo[result.size()]);
    }

    private synchronized PrecomputedText(CharSequence text, int start, int end, Params params, ParagraphInfo[] paraInfo) {
        this.mText = new SpannableString(text, true);
        this.mStart = start;
        this.mEnd = end;
        this.mParams = params;
        this.mParagraphInfo = paraInfo;
    }

    public synchronized CharSequence getText() {
        return this.mText;
    }

    public synchronized int getStart() {
        return this.mStart;
    }

    public synchronized int getEnd() {
        return this.mEnd;
    }

    public Params getParams() {
        return this.mParams;
    }

    public int getParagraphCount() {
        return this.mParagraphInfo.length;
    }

    public int getParagraphStart(int paraIndex) {
        Preconditions.checkArgumentInRange(paraIndex, 0, getParagraphCount(), "paraIndex");
        return paraIndex == 0 ? this.mStart : getParagraphEnd(paraIndex - 1);
    }

    public int getParagraphEnd(int paraIndex) {
        Preconditions.checkArgumentInRange(paraIndex, 0, getParagraphCount(), "paraIndex");
        return this.mParagraphInfo[paraIndex].paragraphEnd;
    }

    public synchronized MeasuredParagraph getMeasuredParagraph(int paraIndex) {
        return this.mParagraphInfo[paraIndex].measured;
    }

    public synchronized ParagraphInfo[] getParagraphInfo() {
        return this.mParagraphInfo;
    }

    public synchronized boolean canUseMeasuredResult(int start, int end, TextDirectionHeuristic textDir, TextPaint paint, int strategy, int frequency) {
        this.mParams.getTextPaint();
        return this.mStart == start && this.mEnd == end && this.mParams.isSameTextMetricsInternal(paint, textDir, strategy, frequency);
    }

    public synchronized int findParaIndex(int pos) {
        for (int i = 0; i < this.mParagraphInfo.length; i++) {
            if (pos < this.mParagraphInfo[i].paragraphEnd) {
                return i;
            }
        }
        throw new IndexOutOfBoundsException("pos must be less than " + this.mParagraphInfo[this.mParagraphInfo.length - 1].paragraphEnd + ", gave " + pos);
    }

    public float getWidth(int start, int end) {
        Preconditions.checkArgument(start >= 0 && start <= this.mText.length(), "invalid start offset");
        Preconditions.checkArgument(end >= 0 && end <= this.mText.length(), "invalid end offset");
        Preconditions.checkArgument(start <= end, "start offset can not be larger than end offset");
        if (start == end) {
            return 0.0f;
        }
        int paraIndex = findParaIndex(start);
        int paraStart = getParagraphStart(paraIndex);
        int paraEnd = getParagraphEnd(paraIndex);
        if (start < paraStart || paraEnd < end) {
            throw new IllegalArgumentException("Cannot measured across the paragraph:para: (" + paraStart + ", " + paraEnd + "), request: (" + start + ", " + end + ")");
        }
        return getMeasuredParagraph(paraIndex).getWidth(start - paraStart, end - paraStart);
    }

    public void getBounds(int start, int end, Rect bounds) {
        Preconditions.checkArgument(start >= 0 && start <= this.mText.length(), "invalid start offset");
        Preconditions.checkArgument(end >= 0 && end <= this.mText.length(), "invalid end offset");
        Preconditions.checkArgument(start <= end, "start offset can not be larger than end offset");
        Preconditions.checkNotNull(bounds);
        if (start == end) {
            bounds.set(0, 0, 0, 0);
            return;
        }
        int paraIndex = findParaIndex(start);
        int paraStart = getParagraphStart(paraIndex);
        int paraEnd = getParagraphEnd(paraIndex);
        if (start < paraStart || paraEnd < end) {
            throw new IllegalArgumentException("Cannot measured across the paragraph:para: (" + paraStart + ", " + paraEnd + "), request: (" + start + ", " + end + ")");
        }
        getMeasuredParagraph(paraIndex).getBounds(start - paraStart, end - paraStart, bounds);
    }

    public synchronized int getMemoryUsage() {
        int r = 0;
        for (int i = 0; i < getParagraphCount(); i++) {
            r += getMeasuredParagraph(i).getMemoryUsage();
        }
        return r;
    }

    @Override // android.text.Spannable
    public void setSpan(Object what, int start, int end, int flags) {
        if (what instanceof MetricAffectingSpan) {
            throw new IllegalArgumentException("MetricAffectingSpan can not be set to PrecomputedText.");
        }
        this.mText.setSpan(what, start, end, flags);
    }

    @Override // android.text.Spannable
    public void removeSpan(Object what) {
        if (what instanceof MetricAffectingSpan) {
            throw new IllegalArgumentException("MetricAffectingSpan can not be removed from PrecomputedText.");
        }
        this.mText.removeSpan(what);
    }

    @Override // android.text.Spanned
    public <T> T[] getSpans(int start, int end, Class<T> type) {
        return (T[]) this.mText.getSpans(start, end, type);
    }

    @Override // android.text.Spanned
    public int getSpanStart(Object tag) {
        return this.mText.getSpanStart(tag);
    }

    @Override // android.text.Spanned
    public int getSpanEnd(Object tag) {
        return this.mText.getSpanEnd(tag);
    }

    @Override // android.text.Spanned
    public int getSpanFlags(Object tag) {
        return this.mText.getSpanFlags(tag);
    }

    @Override // android.text.Spanned
    public int nextSpanTransition(int start, int limit, Class type) {
        return this.mText.nextSpanTransition(start, limit, type);
    }

    @Override // java.lang.CharSequence
    public int length() {
        return this.mText.length();
    }

    @Override // java.lang.CharSequence
    public char charAt(int index) {
        return this.mText.charAt(index);
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int start, int end) {
        return create(this.mText.subSequence(start, end), this.mParams);
    }

    @Override // java.lang.CharSequence
    public String toString() {
        return this.mText.toString();
    }
}
