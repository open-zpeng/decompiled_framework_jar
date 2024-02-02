package android.text;

import java.lang.reflect.Array;
import java.util.Arrays;
/* loaded from: classes2.dex */
public class SpanSet<E> {
    private final Class<? extends E> classType;
    int numberOfSpans = 0;
    int[] spanEnds;
    int[] spanFlags;
    int[] spanStarts;
    public private protected E[] spans;

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized SpanSet(Class<? extends E> type) {
        this.classType = type;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public synchronized void init(Spanned spanned, int start, int limit) {
        Object[] spans = spanned.getSpans(start, limit, this.classType);
        int length = spans.length;
        if (length > 0 && (this.spans == null || this.spans.length < length)) {
            this.spans = (E[]) ((Object[]) Array.newInstance(this.classType, length));
            this.spanStarts = new int[length];
            this.spanEnds = new int[length];
            this.spanFlags = new int[length];
        }
        int prevNumberOfSpans = this.numberOfSpans;
        this.numberOfSpans = 0;
        for (Object obj : spans) {
            int spanStart = spanned.getSpanStart(obj);
            int spanEnd = spanned.getSpanEnd(obj);
            if (spanStart != spanEnd) {
                int spanFlag = spanned.getSpanFlags(obj);
                this.spans[this.numberOfSpans] = obj;
                this.spanStarts[this.numberOfSpans] = spanStart;
                this.spanEnds[this.numberOfSpans] = spanEnd;
                this.spanFlags[this.numberOfSpans] = spanFlag;
                this.numberOfSpans++;
            }
        }
        int i = this.numberOfSpans;
        if (i < prevNumberOfSpans) {
            Arrays.fill(this.spans, this.numberOfSpans, prevNumberOfSpans, (Object) null);
        }
    }

    public synchronized boolean hasSpansIntersecting(int start, int end) {
        for (int i = 0; i < this.numberOfSpans; i++) {
            if (this.spanStarts[i] < end && this.spanEnds[i] > start) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int getNextTransition(int start, int limit) {
        for (int i = 0; i < this.numberOfSpans; i++) {
            int spanStart = this.spanStarts[i];
            int spanEnd = this.spanEnds[i];
            if (spanStart > start && spanStart < limit) {
                limit = spanStart;
            }
            if (spanEnd > start && spanEnd < limit) {
                limit = spanEnd;
            }
        }
        return limit;
    }

    public synchronized void recycle() {
        if (this.spans != null) {
            Arrays.fill(this.spans, 0, this.numberOfSpans, (Object) null);
        }
    }
}
