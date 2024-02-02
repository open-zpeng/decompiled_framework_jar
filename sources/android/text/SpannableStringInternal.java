package android.text;

import android.net.wifi.WifiEnterpriseConfig;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import java.lang.reflect.Array;
import libcore.util.EmptyArray;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public abstract class SpannableStringInternal {
    public protected static final int COLUMNS = 3;
    public private protected static final Object[] EMPTY = new Object[0];
    public protected static final int END = 1;
    public protected static final int FLAGS = 2;
    public protected static final int START = 0;
    public protected int mSpanCount;
    public protected int[] mSpanData;
    public protected Object[] mSpans;
    public protected String mText;

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized SpannableStringInternal(CharSequence source, int start, int end, boolean ignoreNoCopySpan) {
        if (start == 0 && end == source.length()) {
            this.mText = source.toString();
        } else {
            this.mText = source.toString().substring(start, end);
        }
        this.mSpans = EmptyArray.OBJECT;
        this.mSpanData = EmptyArray.INT;
        if (source instanceof Spanned) {
            if (source instanceof SpannableStringInternal) {
                copySpans((SpannableStringInternal) source, start, end, ignoreNoCopySpan);
            } else {
                copySpans((Spanned) source, start, end, ignoreNoCopySpan);
            }
        }
    }

    public private protected SpannableStringInternal(CharSequence source, int start, int end) {
        this(source, start, end, false);
    }

    private synchronized void copySpans(Spanned src, int start, int end, boolean ignoreNoCopySpan) {
        Object[] spans = src.getSpans(start, end, Object.class);
        for (int i = 0; i < spans.length; i++) {
            if (!ignoreNoCopySpan || !(spans[i] instanceof NoCopySpan)) {
                int st = src.getSpanStart(spans[i]);
                int en = src.getSpanEnd(spans[i]);
                int fl = src.getSpanFlags(spans[i]);
                if (st < start) {
                    st = start;
                }
                if (en > end) {
                    en = end;
                }
                setSpan(spans[i], st - start, en - start, fl, false);
            }
        }
    }

    private synchronized void copySpans(SpannableStringInternal src, int start, int end, boolean ignoreNoCopySpan) {
        int[] srcData = src.mSpanData;
        Object[] srcSpans = src.mSpans;
        int limit = src.mSpanCount;
        boolean hasNoCopySpan = false;
        int count = 0;
        for (int count2 = 0; count2 < limit; count2++) {
            if (!isOutOfCopyRange(start, end, srcData[(count2 * 3) + 0], srcData[(count2 * 3) + 1])) {
                if (srcSpans[count2] instanceof NoCopySpan) {
                    hasNoCopySpan = true;
                    if (ignoreNoCopySpan) {
                    }
                }
                count++;
            }
        }
        if (count == 0) {
            return;
        }
        if (!hasNoCopySpan && start == 0 && end == src.length()) {
            this.mSpans = ArrayUtils.newUnpaddedObjectArray(src.mSpans.length);
            this.mSpanData = new int[src.mSpanData.length];
            this.mSpanCount = src.mSpanCount;
            System.arraycopy(src.mSpans, 0, this.mSpans, 0, src.mSpans.length);
            System.arraycopy(src.mSpanData, 0, this.mSpanData, 0, this.mSpanData.length);
            return;
        }
        this.mSpanCount = count;
        this.mSpans = ArrayUtils.newUnpaddedObjectArray(this.mSpanCount);
        this.mSpanData = new int[this.mSpans.length * 3];
        int j = 0;
        for (int i = 0; i < limit; i++) {
            int spanStart = srcData[(i * 3) + 0];
            int spanEnd = srcData[(i * 3) + 1];
            if (!isOutOfCopyRange(start, end, spanStart, spanEnd) && (!ignoreNoCopySpan || !(srcSpans[i] instanceof NoCopySpan))) {
                if (spanStart < start) {
                    spanStart = start;
                }
                if (spanEnd > end) {
                    spanEnd = end;
                }
                this.mSpans[j] = srcSpans[i];
                this.mSpanData[(j * 3) + 0] = spanStart - start;
                this.mSpanData[(j * 3) + 1] = spanEnd - start;
                this.mSpanData[(j * 3) + 2] = srcData[(i * 3) + 2];
                j++;
            }
        }
    }

    public protected final boolean isOutOfCopyRange(int start, int end, int spanStart, int spanEnd) {
        if (spanStart > end || spanEnd < start) {
            return true;
        }
        if (spanStart != spanEnd && start != end) {
            if (spanStart == end || spanEnd == start) {
                return true;
            }
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int length() {
        return this.mText.length();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final char charAt(int i) {
        return this.mText.charAt(i);
    }

    public final String toString() {
        return this.mText;
    }

    private protected final void getChars(int start, int end, char[] dest, int off) {
        this.mText.getChars(start, end, dest, off);
    }

    public private protected void setSpan(Object what, int start, int end, int flags) {
        setSpan(what, start, end, flags, true);
    }

    public protected boolean isIndexFollowsNextLine(int index) {
        return (index == 0 || index == length() || charAt(index + (-1)) == '\n') ? false : true;
    }

    public protected void setSpan(Object what, int start, int end, int flags, boolean enforceParagraph) {
        checkRange("setSpan", start, end);
        if ((flags & 51) == 51) {
            if (isIndexFollowsNextLine(start)) {
                if (enforceParagraph) {
                    throw new RuntimeException("PARAGRAPH span must start at paragraph boundary (" + start + " follows " + charAt(start - 1) + ")");
                }
                return;
            } else if (isIndexFollowsNextLine(end)) {
                if (enforceParagraph) {
                    throw new RuntimeException("PARAGRAPH span must end at paragraph boundary (" + end + " follows " + charAt(end - 1) + ")");
                }
                return;
            }
        }
        int count = this.mSpanCount;
        Object[] spans = this.mSpans;
        int[] data = this.mSpanData;
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= count) {
                if (this.mSpanCount + 1 >= this.mSpans.length) {
                    Object[] newtags = ArrayUtils.newUnpaddedObjectArray(GrowingArrayUtils.growSize(this.mSpanCount));
                    int[] newdata = new int[newtags.length * 3];
                    System.arraycopy(this.mSpans, 0, newtags, 0, this.mSpanCount);
                    System.arraycopy(this.mSpanData, 0, newdata, 0, this.mSpanCount * 3);
                    this.mSpans = newtags;
                    this.mSpanData = newdata;
                }
                this.mSpans[this.mSpanCount] = what;
                this.mSpanData[(this.mSpanCount * 3) + 0] = start;
                this.mSpanData[(this.mSpanCount * 3) + 1] = end;
                this.mSpanData[(this.mSpanCount * 3) + 2] = flags;
                this.mSpanCount++;
                if (this instanceof Spannable) {
                    sendSpanAdded(what, start, end);
                    return;
                }
                return;
            } else if (spans[i2] == what) {
                int ostart = data[(i2 * 3) + 0];
                int oend = data[(i2 * 3) + 1];
                data[(i2 * 3) + 0] = start;
                data[(i2 * 3) + 1] = end;
                data[(i2 * 3) + 2] = flags;
                sendSpanChanged(what, ostart, oend, start, end);
                return;
            } else {
                i = i2 + 1;
            }
        }
    }

    public private protected void removeSpan(Object what) {
        removeSpan(what, 0);
    }

    public synchronized void removeSpan(Object what, int flags) {
        int count = this.mSpanCount;
        Object[] spans = this.mSpans;
        int[] data = this.mSpanData;
        for (int i = count - 1; i >= 0; i--) {
            if (spans[i] == what) {
                int ostart = data[(i * 3) + 0];
                int oend = data[(i * 3) + 1];
                int c = count - (i + 1);
                System.arraycopy(spans, i + 1, spans, i, c);
                System.arraycopy(data, (i + 1) * 3, data, i * 3, c * 3);
                this.mSpanCount--;
                if ((flags & 512) == 0) {
                    sendSpanRemoved(what, ostart, oend);
                    return;
                }
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSpanStart(Object what) {
        int count = this.mSpanCount;
        Object[] spans = this.mSpans;
        int[] data = this.mSpanData;
        for (int i = count - 1; i >= 0; i--) {
            if (spans[i] == what) {
                return data[(i * 3) + 0];
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSpanEnd(Object what) {
        int count = this.mSpanCount;
        Object[] spans = this.mSpans;
        int[] data = this.mSpanData;
        for (int i = count - 1; i >= 0; i--) {
            if (spans[i] == what) {
                return data[(i * 3) + 1];
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSpanFlags(Object what) {
        int count = this.mSpanCount;
        Object[] spans = this.mSpans;
        int[] data = this.mSpanData;
        for (int i = count - 1; i >= 0; i--) {
            if (spans[i] == what) {
                return data[(i * 3) + 2];
            }
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public <T> T[] getSpans(int queryStart, int queryEnd, Class<T> kind) {
        int j;
        int i = queryStart;
        int spanCount = this.mSpanCount;
        Object[] spans = this.mSpans;
        int[] data = this.mSpanData;
        int i2 = 0;
        Object ret1 = null;
        Object ret12 = null;
        Object[] ret = (T[]) ret12;
        int count = 0;
        int count2 = 0;
        while (count2 < spanCount) {
            int spanStart = data[(count2 * 3) + i2];
            int spanEnd = data[(count2 * 3) + 1];
            if (spanStart <= queryEnd && spanEnd >= i && ((spanStart == spanEnd || i == queryEnd || (spanStart != queryEnd && spanEnd != i)) && (kind == null || kind == Object.class || kind.isInstance(spans[count2])))) {
                if (count == 0) {
                    ret1 = spans[count2];
                    count++;
                } else {
                    if (count == 1) {
                        Object[] ret2 = (Object[]) Array.newInstance((Class<?>) kind, (spanCount - count2) + 1);
                        ret = (T[]) ret2;
                        ret[i2] = ret1;
                    }
                    int prio = data[(count2 * 3) + 2] & Spanned.SPAN_PRIORITY;
                    if (prio != 0) {
                        int j2 = i2;
                        while (true) {
                            j = j2;
                            if (j >= count) {
                                break;
                            }
                            int p = getSpanFlags(ret[j]) & Spanned.SPAN_PRIORITY;
                            if (prio > p) {
                                break;
                            }
                            j2 = j + 1;
                        }
                        System.arraycopy(ret, j, ret, j + 1, count - j);
                        ret[j] = spans[count2];
                        count++;
                    } else {
                        ret[count] = spans[count2];
                        count++;
                    }
                }
            }
            count2++;
            i = queryStart;
            i2 = 0;
        }
        if (count == 0) {
            return (T[]) ArrayUtils.emptyArray(kind);
        }
        if (count == 1) {
            T[] tArr = (T[]) ((Object[]) Array.newInstance((Class<?>) kind, 1));
            tArr[0] = ret1;
            return tArr;
        } else if (count == ret.length) {
            return (T[]) ret;
        } else {
            T[] tArr2 = (T[]) ((Object[]) Array.newInstance((Class<?>) kind, count));
            System.arraycopy(ret, 0, tArr2, 0, count);
            return tArr2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int nextSpanTransition(int start, int limit, Class kind) {
        int count = this.mSpanCount;
        Object[] spans = this.mSpans;
        int[] data = this.mSpanData;
        if (kind == null) {
            kind = Object.class;
        }
        int limit2 = limit;
        for (int limit3 = 0; limit3 < count; limit3++) {
            int st = data[(limit3 * 3) + 0];
            int en = data[(limit3 * 3) + 1];
            if (st > start && st < limit2 && kind.isInstance(spans[limit3])) {
                limit2 = st;
            }
            if (en > start && en < limit2 && kind.isInstance(spans[limit3])) {
                limit2 = en;
            }
        }
        return limit2;
    }

    public protected void sendSpanAdded(Object what, int start, int end) {
        SpanWatcher[] recip = (SpanWatcher[]) getSpans(start, end, SpanWatcher.class);
        for (SpanWatcher spanWatcher : recip) {
            spanWatcher.onSpanAdded((Spannable) this, what, start, end);
        }
    }

    public protected void sendSpanRemoved(Object what, int start, int end) {
        SpanWatcher[] recip = (SpanWatcher[]) getSpans(start, end, SpanWatcher.class);
        for (SpanWatcher spanWatcher : recip) {
            spanWatcher.onSpanRemoved((Spannable) this, what, start, end);
        }
    }

    public protected void sendSpanChanged(Object what, int s, int e, int st, int en) {
        SpanWatcher[] recip = (SpanWatcher[]) getSpans(Math.min(s, st), Math.max(e, en), SpanWatcher.class);
        for (SpanWatcher spanWatcher : recip) {
            spanWatcher.onSpanChanged((Spannable) this, what, s, e, st, en);
        }
    }

    public protected static String region(int start, int end) {
        return "(" + start + " ... " + end + ")";
    }

    public protected void checkRange(String operation, int start, int end) {
        if (end < start) {
            throw new IndexOutOfBoundsException(operation + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + region(start, end) + " has end before start");
        }
        int len = length();
        if (start > len || end > len) {
            throw new IndexOutOfBoundsException(operation + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + region(start, end) + " ends beyond length " + len);
        } else if (start < 0 || end < 0) {
            throw new IndexOutOfBoundsException(operation + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + region(start, end) + " starts before 0");
        }
    }

    public boolean equals(Object o) {
        if ((o instanceof Spanned) && toString().equals(o.toString())) {
            Spanned other = (Spanned) o;
            Object[] otherSpans = other.getSpans(0, other.length(), Object.class);
            if (this.mSpanCount == otherSpans.length) {
                for (int i = 0; i < this.mSpanCount; i++) {
                    Object thisSpan = this.mSpans[i];
                    Object otherSpan = otherSpans[i];
                    if (thisSpan == this) {
                        if (other != otherSpan || getSpanStart(thisSpan) != other.getSpanStart(otherSpan) || getSpanEnd(thisSpan) != other.getSpanEnd(otherSpan) || getSpanFlags(thisSpan) != other.getSpanFlags(otherSpan)) {
                            return false;
                        }
                    } else if (!thisSpan.equals(otherSpan) || getSpanStart(thisSpan) != other.getSpanStart(otherSpan) || getSpanEnd(thisSpan) != other.getSpanEnd(otherSpan) || getSpanFlags(thisSpan) != other.getSpanFlags(otherSpan)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int hash = toString().hashCode();
        int hash2 = (hash * 31) + this.mSpanCount;
        for (int i = 0; i < this.mSpanCount; i++) {
            Object span = this.mSpans[i];
            if (span != this) {
                hash2 = (hash2 * 31) + span.hashCode();
            }
            hash2 = (((((hash2 * 31) + getSpanStart(span)) * 31) + getSpanEnd(span)) * 31) + getSpanFlags(span);
        }
        return hash2;
    }

    public protected void copySpans(Spanned src, int start, int end) {
        copySpans(src, start, end, false);
    }

    public protected void copySpans(SpannableStringInternal src, int start, int end) {
        copySpans(src, start, end, false);
    }
}
