package android.text;

import android.graphics.BaseCanvas;
import android.graphics.Paint;
import android.net.wifi.WifiEnterpriseConfig;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import java.lang.reflect.Array;
import java.util.IdentityHashMap;
import libcore.util.EmptyArray;
/* loaded from: classes2.dex */
public class SpannableStringBuilder implements CharSequence, GetChars, Spannable, Editable, Appendable, GraphicsOperations {
    private static final int END_MASK = 15;
    private static final int MARK = 1;
    private static final int PARAGRAPH = 3;
    private static final int POINT = 2;
    private static final int SPAN_ADDED = 2048;
    private static final int SPAN_END_AT_END = 32768;
    private static final int SPAN_END_AT_START = 16384;
    private static final int SPAN_START_AT_END = 8192;
    private static final int SPAN_START_AT_START = 4096;
    private static final int SPAN_START_END_MASK = 61440;
    private static final int START_MASK = 240;
    private static final int START_SHIFT = 4;
    private static final String TAG = "SpannableStringBuilder";
    private InputFilter[] mFilters;
    public protected int mGapLength;
    public protected int mGapStart;
    private IdentityHashMap<Object, Integer> mIndexOfSpan;
    private int mLowWaterMark;
    public protected int mSpanCount;
    public protected int[] mSpanEnds;
    public protected int[] mSpanFlags;
    private int mSpanInsertCount;
    private int[] mSpanMax;
    private int[] mSpanOrder;
    public protected int[] mSpanStarts;
    public protected Object[] mSpans;
    public protected char[] mText;
    private int mTextWatcherDepth;
    private static final InputFilter[] NO_FILTERS = new InputFilter[0];
    @GuardedBy("sCachedIntBuffer")
    private static final int[][] sCachedIntBuffer = (int[][]) Array.newInstance(int.class, 6, 0);

    public SpannableStringBuilder() {
        this("");
    }

    public SpannableStringBuilder(CharSequence text) {
        this(text, 0, text.length());
    }

    public SpannableStringBuilder(CharSequence text, int start, int end) {
        this.mFilters = NO_FILTERS;
        int srclen = end - start;
        if (srclen < 0) {
            throw new StringIndexOutOfBoundsException();
        }
        this.mText = ArrayUtils.newUnpaddedCharArray(GrowingArrayUtils.growSize(srclen));
        this.mGapStart = srclen;
        this.mGapLength = this.mText.length - srclen;
        int i = 0;
        TextUtils.getChars(text, start, end, this.mText, 0);
        this.mSpanCount = 0;
        this.mSpanInsertCount = 0;
        this.mSpans = EmptyArray.OBJECT;
        this.mSpanStarts = EmptyArray.INT;
        this.mSpanEnds = EmptyArray.INT;
        this.mSpanFlags = EmptyArray.INT;
        this.mSpanMax = EmptyArray.INT;
        this.mSpanOrder = EmptyArray.INT;
        if (text instanceof Spanned) {
            Spanned sp = (Spanned) text;
            Object[] spans = sp.getSpans(start, end, Object.class);
            while (true) {
                int i2 = i;
                if (i2 < spans.length) {
                    if (!(spans[i2] instanceof NoCopySpan)) {
                        int st = sp.getSpanStart(spans[i2]) - start;
                        int en = sp.getSpanEnd(spans[i2]) - start;
                        int fl = sp.getSpanFlags(spans[i2]);
                        st = st < 0 ? 0 : st;
                        int st2 = st > end - start ? end - start : st;
                        en = en < 0 ? 0 : en;
                        setSpan(false, spans[i2], st2, en > end - start ? end - start : en, fl, false);
                    }
                    i = i2 + 1;
                } else {
                    restoreInvariants();
                    return;
                }
            }
        }
    }

    public static SpannableStringBuilder valueOf(CharSequence source) {
        if (source instanceof SpannableStringBuilder) {
            return (SpannableStringBuilder) source;
        }
        return new SpannableStringBuilder(source);
    }

    @Override // java.lang.CharSequence
    public char charAt(int where) {
        int len = length();
        if (where < 0) {
            throw new IndexOutOfBoundsException("charAt: " + where + " < 0");
        } else if (where >= len) {
            throw new IndexOutOfBoundsException("charAt: " + where + " >= length " + len);
        } else if (where >= this.mGapStart) {
            return this.mText[this.mGapLength + where];
        } else {
            return this.mText[where];
        }
    }

    @Override // java.lang.CharSequence
    public int length() {
        return this.mText.length - this.mGapLength;
    }

    private synchronized void resizeFor(int size) {
        int oldLength = this.mText.length;
        if (size + 1 <= oldLength) {
            return;
        }
        char[] newText = ArrayUtils.newUnpaddedCharArray(GrowingArrayUtils.growSize(size));
        System.arraycopy(this.mText, 0, newText, 0, this.mGapStart);
        int newLength = newText.length;
        int delta = newLength - oldLength;
        int after = oldLength - (this.mGapStart + this.mGapLength);
        System.arraycopy(this.mText, oldLength - after, newText, newLength - after, after);
        this.mText = newText;
        this.mGapLength += delta;
        if (this.mGapLength < 1) {
            new Exception("mGapLength < 1").printStackTrace();
        }
        if (this.mSpanCount != 0) {
            for (int i = 0; i < this.mSpanCount; i++) {
                if (this.mSpanStarts[i] > this.mGapStart) {
                    int[] iArr = this.mSpanStarts;
                    iArr[i] = iArr[i] + delta;
                }
                if (this.mSpanEnds[i] > this.mGapStart) {
                    int[] iArr2 = this.mSpanEnds;
                    iArr2[i] = iArr2[i] + delta;
                }
            }
            int i2 = treeRoot();
            calcMax(i2);
        }
    }

    private synchronized void moveGapTo(int where) {
        int flag;
        int flag2;
        if (where == this.mGapStart) {
            return;
        }
        boolean atEnd = where == length();
        if (where < this.mGapStart) {
            int overlap = this.mGapStart - where;
            System.arraycopy(this.mText, where, this.mText, (this.mGapStart + this.mGapLength) - overlap, overlap);
        } else {
            int overlap2 = where - this.mGapStart;
            System.arraycopy(this.mText, (this.mGapLength + where) - overlap2, this.mText, this.mGapStart, overlap2);
        }
        if (this.mSpanCount != 0) {
            for (int i = 0; i < this.mSpanCount; i++) {
                int start = this.mSpanStarts[i];
                int end = this.mSpanEnds[i];
                if (start > this.mGapStart) {
                    start -= this.mGapLength;
                }
                if (start > where) {
                    start += this.mGapLength;
                } else if (start == where && ((flag = (this.mSpanFlags[i] & 240) >> 4) == 2 || (atEnd && flag == 3))) {
                    start += this.mGapLength;
                }
                if (end > this.mGapStart) {
                    end -= this.mGapLength;
                }
                if (end > where) {
                    end += this.mGapLength;
                } else if (end == where && ((flag2 = this.mSpanFlags[i] & 15) == 2 || (atEnd && flag2 == 3))) {
                    end += this.mGapLength;
                }
                this.mSpanStarts[i] = start;
                this.mSpanEnds[i] = end;
            }
            int i2 = treeRoot();
            calcMax(i2);
        }
        this.mGapStart = where;
    }

    @Override // android.text.Editable
    public SpannableStringBuilder insert(int where, CharSequence tb, int start, int end) {
        return replace(where, where, tb, start, end);
    }

    @Override // android.text.Editable
    public SpannableStringBuilder insert(int where, CharSequence tb) {
        return replace(where, where, tb, 0, tb.length());
    }

    @Override // android.text.Editable
    public SpannableStringBuilder delete(int start, int end) {
        SpannableStringBuilder ret = replace(start, end, "", 0, 0);
        if (this.mGapLength > 2 * length()) {
            resizeFor(length());
        }
        return ret;
    }

    @Override // android.text.Editable
    public void clear() {
        replace(0, length(), "", 0, 0);
        this.mSpanInsertCount = 0;
    }

    @Override // android.text.Editable
    public void clearSpans() {
        for (int i = this.mSpanCount - 1; i >= 0; i--) {
            Object what = this.mSpans[i];
            int ostart = this.mSpanStarts[i];
            int oend = this.mSpanEnds[i];
            if (ostart > this.mGapStart) {
                ostart -= this.mGapLength;
            }
            if (oend > this.mGapStart) {
                oend -= this.mGapLength;
            }
            this.mSpanCount = i;
            this.mSpans[i] = null;
            sendSpanRemoved(what, ostart, oend);
        }
        if (this.mIndexOfSpan != null) {
            this.mIndexOfSpan.clear();
        }
        this.mSpanInsertCount = 0;
    }

    @Override // android.text.Editable, java.lang.Appendable
    public SpannableStringBuilder append(CharSequence text) {
        int length = length();
        return replace(length, length, text, 0, text.length());
    }

    public SpannableStringBuilder append(CharSequence text, Object what, int flags) {
        int start = length();
        append(text);
        setSpan(what, start, length(), flags);
        return this;
    }

    @Override // android.text.Editable, java.lang.Appendable
    public SpannableStringBuilder append(CharSequence text, int start, int end) {
        int length = length();
        return replace(length, length, text, start, end);
    }

    @Override // android.text.Editable, java.lang.Appendable
    public SpannableStringBuilder append(char text) {
        return append((CharSequence) String.valueOf(text));
    }

    private synchronized boolean removeSpansForChange(int start, int end, boolean textIsRemoved, int i) {
        if ((i & 1) == 0 || resolveGap(this.mSpanMax[i]) < start || !removeSpansForChange(start, end, textIsRemoved, leftChild(i))) {
            if (i < this.mSpanCount) {
                if ((this.mSpanFlags[i] & 33) != 33 || this.mSpanStarts[i] < start || this.mSpanStarts[i] >= this.mGapStart + this.mGapLength || this.mSpanEnds[i] < start || this.mSpanEnds[i] >= this.mGapStart + this.mGapLength || (!textIsRemoved && this.mSpanStarts[i] <= start && this.mSpanEnds[i] >= this.mGapStart)) {
                    return resolveGap(this.mSpanStarts[i]) <= end && (i & 1) != 0 && removeSpansForChange(start, end, textIsRemoved, rightChild(i));
                }
                this.mIndexOfSpan.remove(this.mSpans[i]);
                removeSpan(i, 0);
                return true;
            }
            return false;
        }
        return true;
    }

    private synchronized void change(int start, int end, CharSequence cs, int csStart, int csEnd) {
        int i;
        int i2;
        CharSequence charSequence;
        int spanEnd;
        CharSequence charSequence2 = cs;
        int i3 = csStart;
        int i4 = csEnd;
        int replacedLength = end - start;
        int replacementLength = i4 - i3;
        int nbNewChars = replacementLength - replacedLength;
        int spanEnd2 = this.mSpanCount - 1;
        boolean changed = false;
        while (true) {
            int i5 = spanEnd2;
            if (i5 < 0) {
                break;
            }
            int spanStart = this.mSpanStarts[i5];
            if (spanStart > this.mGapStart) {
                spanStart -= this.mGapLength;
            }
            int spanEnd3 = this.mSpanEnds[i5];
            if (spanEnd3 > this.mGapStart) {
                spanEnd3 -= this.mGapLength;
            }
            if ((this.mSpanFlags[i5] & 51) == 51) {
                int ost = spanStart;
                int oen = spanEnd3;
                int clen = length();
                if (spanStart > start && spanStart <= end) {
                    spanStart = end;
                    while (spanStart < clen && (spanStart <= end || charAt(spanStart - 1) != '\n')) {
                        spanStart++;
                    }
                }
                int spanStart2 = spanStart;
                if (spanEnd3 <= start || spanEnd3 > end) {
                    spanEnd = spanEnd3;
                } else {
                    int spanEnd4 = end;
                    while (spanEnd4 < clen && (spanEnd4 <= end || charAt(spanEnd4 - 1) != '\n')) {
                        spanEnd4++;
                    }
                    spanEnd = spanEnd4;
                }
                if (spanStart2 != ost || spanEnd != oen) {
                    Object obj = this.mSpans[i5];
                    int ost2 = this.mSpanFlags[i5];
                    int spanEnd5 = spanEnd;
                    setSpan(false, obj, spanStart2, spanEnd5, ost2, true);
                    changed = true;
                    spanStart = spanStart2;
                    spanEnd3 = spanEnd5;
                } else {
                    spanEnd3 = spanEnd;
                    spanStart = spanStart2;
                }
            }
            int flags = 0;
            if (spanStart == start) {
                flags = 0 | 4096;
            } else if (spanStart == end + nbNewChars) {
                flags = 0 | 8192;
            }
            if (spanEnd3 == start) {
                flags |= 16384;
            } else if (spanEnd3 == end + nbNewChars) {
                flags |= 32768;
            }
            int[] iArr = this.mSpanFlags;
            iArr[i5] = iArr[i5] | flags;
            spanEnd2 = i5 - 1;
        }
        if (changed) {
            restoreInvariants();
        }
        moveGapTo(end);
        if (nbNewChars >= this.mGapLength) {
            resizeFor((this.mText.length + nbNewChars) - this.mGapLength);
        }
        int copySpanEnd = 0;
        boolean textIsRemoved = replacementLength == 0;
        if (replacedLength > 0) {
            while (this.mSpanCount > 0 && removeSpansForChange(start, end, textIsRemoved, treeRoot())) {
            }
        }
        this.mGapStart += nbNewChars;
        this.mGapLength -= nbNewChars;
        if (this.mGapLength < 1) {
            new Exception("mGapLength < 1").printStackTrace();
        }
        TextUtils.getChars(charSequence2, i3, i4, this.mText, start);
        if (replacedLength > 0) {
            boolean atEnd = this.mGapStart + this.mGapLength == this.mText.length;
            int endFlag = 0;
            while (true) {
                int i6 = endFlag;
                if (i6 >= this.mSpanCount) {
                    break;
                }
                int startFlag = (this.mSpanFlags[i6] & 240) >> 4;
                this.mSpanStarts[i6] = updatedIntervalBound(this.mSpanStarts[i6], start, nbNewChars, startFlag, atEnd, textIsRemoved);
                int endFlag2 = this.mSpanFlags[i6] & 15;
                boolean textIsRemoved2 = textIsRemoved;
                this.mSpanEnds[i6] = updatedIntervalBound(this.mSpanEnds[i6], start, nbNewChars, endFlag2, atEnd, textIsRemoved2);
                endFlag = i6 + 1;
                i3 = i3;
                i4 = i4;
                charSequence2 = charSequence2;
                textIsRemoved = textIsRemoved2;
            }
            i = i4;
            i2 = i3;
            charSequence = charSequence2;
            restoreInvariants();
        } else {
            i = i4;
            i2 = i3;
            charSequence = charSequence2;
        }
        if (charSequence instanceof Spanned) {
            Spanned sp = (Spanned) charSequence;
            Object[] spans = sp.getSpans(i2, i, Object.class);
            while (true) {
                int i7 = copySpanEnd;
                if (i7 < spans.length) {
                    int st = sp.getSpanStart(spans[i7]);
                    int en = sp.getSpanEnd(spans[i7]);
                    if (st < i2) {
                        st = i2;
                    }
                    int st2 = st;
                    if (en > i) {
                        en = i;
                    }
                    int en2 = en;
                    if (getSpanStart(spans[i7]) < 0) {
                        int copySpanStart = (st2 - i2) + start;
                        int copySpanEnd2 = (en2 - i2) + start;
                        int copySpanFlags = sp.getSpanFlags(spans[i7]) | 2048;
                        setSpan(false, spans[i7], copySpanStart, copySpanEnd2, copySpanFlags, false);
                    }
                    copySpanEnd = i7 + 1;
                    i2 = csStart;
                    i = csEnd;
                } else {
                    restoreInvariants();
                    return;
                }
            }
        }
    }

    private synchronized int updatedIntervalBound(int offset, int start, int nbNewChars, int flag, boolean atEnd, boolean textIsRemoved) {
        if (offset >= start && offset < this.mGapStart + this.mGapLength) {
            if (flag == 2) {
                if (textIsRemoved || offset > start) {
                    return this.mGapStart + this.mGapLength;
                }
            } else if (flag == 3) {
                if (atEnd) {
                    return this.mGapStart + this.mGapLength;
                }
            } else if (textIsRemoved || offset < this.mGapStart - nbNewChars) {
                return start;
            } else {
                return this.mGapStart;
            }
        }
        return offset;
    }

    private synchronized void removeSpan(int i, int flags) {
        Object object = this.mSpans[i];
        int start = this.mSpanStarts[i];
        int end = this.mSpanEnds[i];
        if (start > this.mGapStart) {
            start -= this.mGapLength;
        }
        if (end > this.mGapStart) {
            end -= this.mGapLength;
        }
        int count = this.mSpanCount - (i + 1);
        System.arraycopy(this.mSpans, i + 1, this.mSpans, i, count);
        System.arraycopy(this.mSpanStarts, i + 1, this.mSpanStarts, i, count);
        System.arraycopy(this.mSpanEnds, i + 1, this.mSpanEnds, i, count);
        System.arraycopy(this.mSpanFlags, i + 1, this.mSpanFlags, i, count);
        System.arraycopy(this.mSpanOrder, i + 1, this.mSpanOrder, i, count);
        this.mSpanCount--;
        invalidateIndex(i);
        this.mSpans[this.mSpanCount] = null;
        restoreInvariants();
        if ((flags & 512) == 0) {
            sendSpanRemoved(object, start, end);
        }
    }

    @Override // android.text.Editable
    public SpannableStringBuilder replace(int start, int end, CharSequence tb) {
        return replace(start, end, tb, 0, tb.length());
    }

    @Override // android.text.Editable
    public SpannableStringBuilder replace(int start, int end, CharSequence tb, int tbstart, int tbend) {
        int newLen;
        int selectionEnd;
        checkRange("replace", start, end);
        int filtercount = this.mFilters.length;
        boolean adjustSelection = false;
        CharSequence tb2 = tb;
        int tbstart2 = tbstart;
        int tbend2 = tbend;
        int tbend3 = 0;
        while (true) {
            int i = tbend3;
            if (i >= filtercount) {
                break;
            }
            CharSequence repl = this.mFilters[i].filter(tb2, tbstart2, tbend2, this, start, end);
            if (repl != null) {
                int tbend4 = repl.length();
                tbend2 = tbend4;
                tb2 = repl;
                tbstart2 = 0;
            }
            tbend3 = i + 1;
        }
        int origLen = end - start;
        int newLen2 = tbend2 - tbstart2;
        if (origLen == 0 && newLen2 == 0 && !hasNonExclusiveExclusiveSpanAt(tb2, tbstart2)) {
            return this;
        }
        TextWatcher[] textWatchers = (TextWatcher[]) getSpans(start, start + origLen, TextWatcher.class);
        sendBeforeTextChanged(textWatchers, start, origLen, newLen2);
        if (origLen != 0 && newLen2 != 0) {
            adjustSelection = true;
        }
        int selectionStart = 0;
        int selectionEnd2 = 0;
        if (adjustSelection) {
            selectionStart = Selection.getSelectionStart(this);
            selectionEnd2 = Selection.getSelectionEnd(this);
        }
        int selectionStart2 = selectionStart;
        int selectionEnd3 = selectionEnd2;
        int selectionStart3 = tbstart2;
        change(start, end, tb2, selectionStart3, tbend2);
        if (adjustSelection) {
            boolean changed = false;
            if (selectionStart2 <= start || selectionStart2 >= end) {
                newLen = newLen2;
            } else {
                long diff = selectionStart2 - start;
                int offset = Math.toIntExact((newLen2 * diff) / origLen);
                int selectionStart4 = start + offset;
                newLen = newLen2;
                setSpan(false, Selection.SELECTION_START, selectionStart4, selectionStart4, 34, true);
                changed = true;
            }
            if (selectionEnd3 <= start || selectionEnd3 >= end) {
                selectionEnd = selectionEnd3;
            } else {
                long diff2 = selectionEnd3 - start;
                int offset2 = Math.toIntExact((newLen * diff2) / origLen);
                int selectionEnd4 = start + offset2;
                selectionEnd = selectionEnd4;
                setSpan(false, Selection.SELECTION_END, selectionEnd, selectionEnd4, 34, true);
                changed = true;
            }
            if (changed) {
                restoreInvariants();
            }
        } else {
            newLen = newLen2;
        }
        sendTextChanged(textWatchers, start, origLen, newLen);
        sendAfterTextChanged(textWatchers);
        sendToSpanWatchers(start, end, newLen - origLen);
        return this;
    }

    private static synchronized boolean hasNonExclusiveExclusiveSpanAt(CharSequence text, int offset) {
        if (text instanceof Spanned) {
            Spanned spanned = (Spanned) text;
            Object[] spans = spanned.getSpans(offset, offset, Object.class);
            for (Object span : spans) {
                int flags = spanned.getSpanFlags(span);
                if (flags != 33) {
                    return true;
                }
            }
        }
        return false;
    }

    public protected void sendToSpanWatchers(int replaceStart, int replaceEnd, int nbNewChars) {
        int i = 0;
        int i2 = 0;
        while (true) {
            int i3 = i2;
            int i4 = this.mSpanCount;
            if (i3 >= i4) {
                break;
            }
            int spanFlags = this.mSpanFlags[i3];
            if ((spanFlags & 2048) == 0) {
                int spanStart = this.mSpanStarts[i3];
                int spanEnd = this.mSpanEnds[i3];
                if (spanStart > this.mGapStart) {
                    spanStart -= this.mGapLength;
                }
                int spanStart2 = spanStart;
                if (spanEnd > this.mGapStart) {
                    spanEnd -= this.mGapLength;
                }
                int spanEnd2 = spanEnd;
                int newReplaceEnd = replaceEnd + nbNewChars;
                boolean spanChanged = false;
                int previousSpanStart = spanStart2;
                if (spanStart2 > newReplaceEnd) {
                    if (nbNewChars != 0) {
                        previousSpanStart -= nbNewChars;
                        spanChanged = true;
                    }
                } else if (spanStart2 >= replaceStart && ((spanStart2 != replaceStart || (spanFlags & 4096) != 4096) && (spanStart2 != newReplaceEnd || (spanFlags & 8192) != 8192))) {
                    spanChanged = true;
                }
                int previousSpanStart2 = previousSpanStart;
                int previousSpanEnd = spanEnd2;
                if (spanEnd2 > newReplaceEnd) {
                    if (nbNewChars != 0) {
                        previousSpanEnd -= nbNewChars;
                        spanChanged = true;
                    }
                } else if (spanEnd2 >= replaceStart && ((spanEnd2 != replaceStart || (spanFlags & 16384) != 16384) && (spanEnd2 != newReplaceEnd || (spanFlags & 32768) != 32768))) {
                    spanChanged = true;
                }
                int previousSpanEnd2 = previousSpanEnd;
                if (spanChanged) {
                    sendSpanChanged(this.mSpans[i3], previousSpanStart2, previousSpanEnd2, spanStart2, spanEnd2);
                }
                int[] iArr = this.mSpanFlags;
                iArr[i3] = iArr[i3] & (-61441);
            }
            i2 = i3 + 1;
        }
        while (true) {
            int i5 = i;
            if (i5 < this.mSpanCount) {
                if ((this.mSpanFlags[i5] & 2048) != 0) {
                    int[] iArr2 = this.mSpanFlags;
                    iArr2[i5] = iArr2[i5] & (-2049);
                    int spanStart3 = this.mSpanStarts[i5];
                    int spanEnd3 = this.mSpanEnds[i5];
                    if (spanStart3 > this.mGapStart) {
                        spanStart3 -= this.mGapLength;
                    }
                    if (spanEnd3 > this.mGapStart) {
                        spanEnd3 -= this.mGapLength;
                    }
                    sendSpanAdded(this.mSpans[i5], spanStart3, spanEnd3);
                }
                i = i5 + 1;
            } else {
                return;
            }
        }
    }

    @Override // android.text.Spannable
    public void setSpan(Object what, int start, int end, int flags) {
        setSpan(true, what, start, end, flags, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:61:0x0168  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x016e  */
    /* JADX WARN: Removed duplicated region for block: B:67:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized void setSpan(boolean r21, java.lang.Object r22, int r23, int r24, int r25, boolean r26) {
        /*
            Method dump skipped, instructions count: 373
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.SpannableStringBuilder.setSpan(boolean, java.lang.Object, int, int, int, boolean):void");
    }

    private synchronized boolean isInvalidParagraph(int index, int flag) {
        return (flag != 3 || index == 0 || index == length() || charAt(index + (-1)) == '\n') ? false : true;
    }

    @Override // android.text.Spannable
    public void removeSpan(Object what) {
        removeSpan(what, 0);
    }

    @Override // android.text.Spannable
    public synchronized void removeSpan(Object what, int flags) {
        Integer i;
        if (this.mIndexOfSpan != null && (i = this.mIndexOfSpan.remove(what)) != null) {
            removeSpan(i.intValue(), flags);
        }
    }

    private synchronized int resolveGap(int i) {
        return i > this.mGapStart ? i - this.mGapLength : i;
    }

    @Override // android.text.Spanned
    public int getSpanStart(Object what) {
        Integer i;
        if (this.mIndexOfSpan == null || (i = this.mIndexOfSpan.get(what)) == null) {
            return -1;
        }
        return resolveGap(this.mSpanStarts[i.intValue()]);
    }

    @Override // android.text.Spanned
    public int getSpanEnd(Object what) {
        Integer i;
        if (this.mIndexOfSpan == null || (i = this.mIndexOfSpan.get(what)) == null) {
            return -1;
        }
        return resolveGap(this.mSpanEnds[i.intValue()]);
    }

    @Override // android.text.Spanned
    public int getSpanFlags(Object what) {
        Integer i;
        if (this.mIndexOfSpan == null || (i = this.mIndexOfSpan.get(what)) == null) {
            return 0;
        }
        return this.mSpanFlags[i.intValue()];
    }

    @Override // android.text.Spanned
    public <T> T[] getSpans(int queryStart, int queryEnd, Class<T> kind) {
        return (T[]) getSpans(queryStart, queryEnd, kind, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public <T> T[] getSpans(int queryStart, int queryEnd, Class<T> kind, boolean sortByInsertionOrder) {
        if (kind == null) {
            return (T[]) ArrayUtils.emptyArray(Object.class);
        }
        if (this.mSpanCount == 0) {
            return (T[]) ArrayUtils.emptyArray(kind);
        }
        int count = countSpans(queryStart, queryEnd, kind, treeRoot());
        if (count == 0) {
            return (T[]) ArrayUtils.emptyArray(kind);
        }
        T[] ret = (T[]) ((Object[]) Array.newInstance((Class<?>) kind, count));
        int[] prioSortBuffer = sortByInsertionOrder ? obtain(count) : EmptyArray.INT;
        int[] orderSortBuffer = sortByInsertionOrder ? obtain(count) : EmptyArray.INT;
        getSpansRec(queryStart, queryEnd, kind, treeRoot(), ret, prioSortBuffer, orderSortBuffer, 0, sortByInsertionOrder);
        if (sortByInsertionOrder) {
            sort(ret, prioSortBuffer, orderSortBuffer);
            recycle(prioSortBuffer);
            recycle(orderSortBuffer);
        }
        return ret;
    }

    private synchronized int countSpans(int queryStart, int queryEnd, Class kind, int i) {
        int count = 0;
        if ((i & 1) != 0) {
            int left = leftChild(i);
            int spanMax = this.mSpanMax[left];
            if (spanMax > this.mGapStart) {
                spanMax -= this.mGapLength;
            }
            if (spanMax >= queryStart) {
                count = countSpans(queryStart, queryEnd, kind, left);
            }
        }
        if (i < this.mSpanCount) {
            int spanStart = this.mSpanStarts[i];
            if (spanStart > this.mGapStart) {
                spanStart -= this.mGapLength;
            }
            if (spanStart <= queryEnd) {
                int spanEnd = this.mSpanEnds[i];
                if (spanEnd > this.mGapStart) {
                    spanEnd -= this.mGapLength;
                }
                if (spanEnd >= queryStart && ((spanStart == spanEnd || queryStart == queryEnd || (spanStart != queryEnd && spanEnd != queryStart)) && (Object.class == kind || kind.isInstance(this.mSpans[i])))) {
                    count++;
                }
                if ((i & 1) != 0) {
                    return count + countSpans(queryStart, queryEnd, kind, rightChild(i));
                }
                return count;
            }
            return count;
        }
        return count;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:13:0x003e A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:14:0x003f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private synchronized <T> int getSpansRec(int r22, int r23, java.lang.Class<T> r24, int r25, T[] r26, int[] r27, int[] r28, int r29, boolean r30) {
        /*
            Method dump skipped, instructions count: 212
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.text.SpannableStringBuilder.getSpansRec(int, int, java.lang.Class, int, java.lang.Object[], int[], int[], int, boolean):int");
    }

    private static synchronized int[] obtain(int elementCount) {
        int[] result = null;
        synchronized (sCachedIntBuffer) {
            int candidateIndex = -1;
            int i = sCachedIntBuffer.length - 1;
            while (true) {
                if (i < 0) {
                    break;
                }
                if (sCachedIntBuffer[i] != null) {
                    if (sCachedIntBuffer[i].length >= elementCount) {
                        candidateIndex = i;
                        break;
                    } else if (candidateIndex == -1) {
                        candidateIndex = i;
                    }
                }
                i--;
            }
            if (candidateIndex != -1) {
                result = sCachedIntBuffer[candidateIndex];
                sCachedIntBuffer[candidateIndex] = null;
            }
        }
        return checkSortBuffer(result, elementCount);
    }

    private static synchronized void recycle(int[] buffer) {
        synchronized (sCachedIntBuffer) {
            for (int i = 0; i < sCachedIntBuffer.length; i++) {
                if (sCachedIntBuffer[i] != null && buffer.length <= sCachedIntBuffer[i].length) {
                }
                sCachedIntBuffer[i] = buffer;
            }
        }
    }

    private static synchronized int[] checkSortBuffer(int[] buffer, int size) {
        if (buffer == null || size > buffer.length) {
            return ArrayUtils.newUnpaddedIntArray(GrowingArrayUtils.growSize(size));
        }
        return buffer;
    }

    private final synchronized <T> void sort(T[] array, int[] priority, int[] insertionOrder) {
        int size = array.length;
        int i = (size / 2) - 1;
        while (true) {
            int i2 = i;
            if (i2 < 0) {
                break;
            }
            siftDown(i2, array, size, priority, insertionOrder);
            i = i2 - 1;
        }
        int i3 = size - 1;
        while (true) {
            int i4 = i3;
            if (i4 > 0) {
                T tmpSpan = array[0];
                array[0] = array[i4];
                array[i4] = tmpSpan;
                int tmpPriority = priority[0];
                priority[0] = priority[i4];
                priority[i4] = tmpPriority;
                int tmpOrder = insertionOrder[0];
                insertionOrder[0] = insertionOrder[i4];
                insertionOrder[i4] = tmpOrder;
                siftDown(0, array, i4, priority, insertionOrder);
                i3 = i4 - 1;
            } else {
                return;
            }
        }
    }

    private final synchronized <T> void siftDown(int index, T[] array, int size, int[] priority, int[] insertionOrder) {
        int left = (2 * index) + 1;
        while (left < size) {
            if (left < size - 1 && compareSpans(left, left + 1, priority, insertionOrder) < 0) {
                left++;
            }
            if (compareSpans(index, left, priority, insertionOrder) < 0) {
                T tmpSpan = array[index];
                array[index] = array[left];
                array[left] = tmpSpan;
                int tmpPriority = priority[index];
                priority[index] = priority[left];
                priority[left] = tmpPriority;
                int tmpOrder = insertionOrder[index];
                insertionOrder[index] = insertionOrder[left];
                insertionOrder[left] = tmpOrder;
                index = left;
                left = (2 * index) + 1;
            } else {
                return;
            }
        }
    }

    private final synchronized int compareSpans(int left, int right, int[] priority, int[] insertionOrder) {
        int priority1 = priority[left];
        int priority2 = priority[right];
        if (priority1 == priority2) {
            return Integer.compare(insertionOrder[left], insertionOrder[right]);
        }
        return Integer.compare(priority2, priority1);
    }

    @Override // android.text.Spanned
    public int nextSpanTransition(int start, int limit, Class kind) {
        if (this.mSpanCount == 0) {
            return limit;
        }
        if (kind == null) {
            kind = Object.class;
        }
        return nextSpanTransitionRec(start, limit, kind, treeRoot());
    }

    private synchronized int nextSpanTransitionRec(int start, int limit, Class kind, int i) {
        if ((i & 1) != 0) {
            int left = leftChild(i);
            if (resolveGap(this.mSpanMax[left]) > start) {
                limit = nextSpanTransitionRec(start, limit, kind, left);
            }
        }
        if (i < this.mSpanCount) {
            int st = resolveGap(this.mSpanStarts[i]);
            int en = resolveGap(this.mSpanEnds[i]);
            if (st > start && st < limit && kind.isInstance(this.mSpans[i])) {
                limit = st;
            }
            if (en > start && en < limit && kind.isInstance(this.mSpans[i])) {
                limit = en;
            }
            if (st < limit && (i & 1) != 0) {
                return nextSpanTransitionRec(start, limit, kind, rightChild(i));
            }
            return limit;
        }
        return limit;
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int start, int end) {
        return new SpannableStringBuilder(this, start, end);
    }

    @Override // android.text.GetChars
    public void getChars(int start, int end, char[] dest, int destoff) {
        checkRange("getChars", start, end);
        if (end <= this.mGapStart) {
            System.arraycopy(this.mText, start, dest, destoff, end - start);
        } else if (start >= this.mGapStart) {
            System.arraycopy(this.mText, this.mGapLength + start, dest, destoff, end - start);
        } else {
            System.arraycopy(this.mText, start, dest, destoff, this.mGapStart - start);
            System.arraycopy(this.mText, this.mGapStart + this.mGapLength, dest, (this.mGapStart - start) + destoff, end - this.mGapStart);
        }
    }

    @Override // java.lang.CharSequence
    public String toString() {
        int len = length();
        char[] buf = new char[len];
        getChars(0, len, buf, 0);
        return new String(buf);
    }

    private protected String substring(int start, int end) {
        char[] buf = new char[end - start];
        getChars(start, end, buf, 0);
        return new String(buf);
    }

    public int getTextWatcherDepth() {
        return this.mTextWatcherDepth;
    }

    private synchronized void sendBeforeTextChanged(TextWatcher[] watchers, int start, int before, int after) {
        this.mTextWatcherDepth++;
        for (TextWatcher textWatcher : watchers) {
            textWatcher.beforeTextChanged(this, start, before, after);
        }
        int i = this.mTextWatcherDepth;
        this.mTextWatcherDepth = i - 1;
    }

    private synchronized void sendTextChanged(TextWatcher[] watchers, int start, int before, int after) {
        this.mTextWatcherDepth++;
        for (TextWatcher textWatcher : watchers) {
            textWatcher.onTextChanged(this, start, before, after);
        }
        int i = this.mTextWatcherDepth;
        this.mTextWatcherDepth = i - 1;
    }

    private synchronized void sendAfterTextChanged(TextWatcher[] watchers) {
        this.mTextWatcherDepth++;
        for (TextWatcher textWatcher : watchers) {
            textWatcher.afterTextChanged(this);
        }
        int i = this.mTextWatcherDepth;
        this.mTextWatcherDepth = i - 1;
    }

    private synchronized void sendSpanAdded(Object what, int start, int end) {
        SpanWatcher[] recip = (SpanWatcher[]) getSpans(start, end, SpanWatcher.class);
        for (SpanWatcher spanWatcher : recip) {
            spanWatcher.onSpanAdded(this, what, start, end);
        }
    }

    private synchronized void sendSpanRemoved(Object what, int start, int end) {
        SpanWatcher[] recip = (SpanWatcher[]) getSpans(start, end, SpanWatcher.class);
        for (SpanWatcher spanWatcher : recip) {
            spanWatcher.onSpanRemoved(this, what, start, end);
        }
    }

    private synchronized void sendSpanChanged(Object what, int oldStart, int oldEnd, int start, int end) {
        SpanWatcher[] spanWatchers = (SpanWatcher[]) getSpans(Math.min(oldStart, start), Math.min(Math.max(oldEnd, end), length()), SpanWatcher.class);
        for (SpanWatcher spanWatcher : spanWatchers) {
            spanWatcher.onSpanChanged(this, what, oldStart, oldEnd, start, end);
        }
    }

    private static synchronized String region(int start, int end) {
        return "(" + start + " ... " + end + ")";
    }

    private synchronized void checkRange(String operation, int start, int end) {
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

    @Override // android.text.GraphicsOperations
    public synchronized void drawText(BaseCanvas c, int start, int end, float x, float y, Paint p) {
        checkRange("drawText", start, end);
        if (end <= this.mGapStart) {
            c.drawText(this.mText, start, end - start, x, y, p);
        } else if (start >= this.mGapStart) {
            c.drawText(this.mText, start + this.mGapLength, end - start, x, y, p);
        } else {
            char[] buf = TextUtils.obtain(end - start);
            getChars(start, end, buf, 0);
            c.drawText(buf, 0, end - start, x, y, p);
            TextUtils.recycle(buf);
        }
    }

    @Override // android.text.GraphicsOperations
    public synchronized void drawTextRun(BaseCanvas c, int start, int end, int contextStart, int contextEnd, float x, float y, boolean isRtl, Paint p) {
        checkRange("drawTextRun", start, end);
        int contextLen = contextEnd - contextStart;
        int len = end - start;
        if (contextEnd <= this.mGapStart) {
            c.drawTextRun(this.mText, start, len, contextStart, contextLen, x, y, isRtl, p);
        } else if (contextStart >= this.mGapStart) {
            c.drawTextRun(this.mText, start + this.mGapLength, len, contextStart + this.mGapLength, contextLen, x, y, isRtl, p);
        } else {
            char[] buf = TextUtils.obtain(contextLen);
            getChars(contextStart, contextEnd, buf, 0);
            c.drawTextRun(buf, start - contextStart, len, 0, contextLen, x, y, isRtl, p);
            TextUtils.recycle(buf);
        }
    }

    @Override // android.text.GraphicsOperations
    public synchronized float measureText(int start, int end, Paint p) {
        checkRange("measureText", start, end);
        if (end <= this.mGapStart) {
            float ret = p.measureText(this.mText, start, end - start);
            return ret;
        } else if (start >= this.mGapStart) {
            float ret2 = p.measureText(this.mText, this.mGapLength + start, end - start);
            return ret2;
        } else {
            char[] buf = TextUtils.obtain(end - start);
            getChars(start, end, buf, 0);
            float ret3 = p.measureText(buf, 0, end - start);
            TextUtils.recycle(buf);
            return ret3;
        }
    }

    @Override // android.text.GraphicsOperations
    public synchronized int getTextWidths(int start, int end, float[] widths, Paint p) {
        checkRange("getTextWidths", start, end);
        if (end <= this.mGapStart) {
            int ret = p.getTextWidths(this.mText, start, end - start, widths);
            return ret;
        }
        int ret2 = this.mGapStart;
        if (start >= ret2) {
            int ret3 = p.getTextWidths(this.mText, this.mGapLength + start, end - start, widths);
            return ret3;
        }
        char[] buf = TextUtils.obtain(end - start);
        getChars(start, end, buf, 0);
        int ret4 = p.getTextWidths(buf, 0, end - start, widths);
        TextUtils.recycle(buf);
        return ret4;
    }

    @Override // android.text.GraphicsOperations
    public synchronized float getTextRunAdvances(int start, int end, int contextStart, int contextEnd, boolean isRtl, float[] advances, int advancesPos, Paint p) {
        int contextLen = contextEnd - contextStart;
        int len = end - start;
        if (end <= this.mGapStart) {
            float ret = p.getTextRunAdvances(this.mText, start, len, contextStart, contextLen, isRtl, advances, advancesPos);
            return ret;
        } else if (start >= this.mGapStart) {
            float ret2 = p.getTextRunAdvances(this.mText, start + this.mGapLength, len, contextStart + this.mGapLength, contextLen, isRtl, advances, advancesPos);
            return ret2;
        } else {
            char[] buf = TextUtils.obtain(contextLen);
            getChars(contextStart, contextEnd, buf, 0);
            float ret3 = p.getTextRunAdvances(buf, start - contextStart, len, 0, contextLen, isRtl, advances, advancesPos);
            TextUtils.recycle(buf);
            return ret3;
        }
    }

    @Override // android.text.GraphicsOperations
    @Deprecated
    public int getTextRunCursor(int contextStart, int contextEnd, int dir, int offset, int cursorOpt, Paint p) {
        int contextLen = contextEnd - contextStart;
        if (contextEnd <= this.mGapStart) {
            int ret = p.getTextRunCursor(this.mText, contextStart, contextLen, dir, offset, cursorOpt);
            return ret;
        }
        int ret2 = this.mGapStart;
        if (contextStart >= ret2) {
            int ret3 = p.getTextRunCursor(this.mText, contextStart + this.mGapLength, contextLen, dir, offset + this.mGapLength, cursorOpt) - this.mGapLength;
            return ret3;
        }
        char[] buf = TextUtils.obtain(contextLen);
        getChars(contextStart, contextEnd, buf, 0);
        int ret4 = p.getTextRunCursor(buf, 0, contextLen, dir, offset - contextStart, cursorOpt) + contextStart;
        TextUtils.recycle(buf);
        return ret4;
    }

    @Override // android.text.Editable
    public void setFilters(InputFilter[] filters) {
        if (filters == null) {
            throw new IllegalArgumentException();
        }
        this.mFilters = filters;
    }

    @Override // android.text.Editable
    public InputFilter[] getFilters() {
        return this.mFilters;
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

    private synchronized int treeRoot() {
        return Integer.highestOneBit(this.mSpanCount) - 1;
    }

    private static synchronized int leftChild(int i) {
        return i - (((i + 1) & (~i)) >> 1);
    }

    private static synchronized int rightChild(int i) {
        return (((i + 1) & (~i)) >> 1) + i;
    }

    private synchronized int calcMax(int i) {
        int max = 0;
        if ((i & 1) != 0) {
            max = calcMax(leftChild(i));
        }
        if (i < this.mSpanCount) {
            max = Math.max(max, this.mSpanEnds[i]);
            if ((i & 1) != 0) {
                max = Math.max(max, calcMax(rightChild(i)));
            }
        }
        this.mSpanMax[i] = max;
        return max;
    }

    private synchronized void restoreInvariants() {
        if (this.mSpanCount == 0) {
            return;
        }
        for (int i = 1; i < this.mSpanCount; i++) {
            if (this.mSpanStarts[i] < this.mSpanStarts[i - 1]) {
                Object span = this.mSpans[i];
                int start = this.mSpanStarts[i];
                int end = this.mSpanEnds[i];
                int flags = this.mSpanFlags[i];
                int insertionOrder = this.mSpanOrder[i];
                int j = i;
                do {
                    this.mSpans[j] = this.mSpans[j - 1];
                    this.mSpanStarts[j] = this.mSpanStarts[j - 1];
                    this.mSpanEnds[j] = this.mSpanEnds[j - 1];
                    this.mSpanFlags[j] = this.mSpanFlags[j - 1];
                    this.mSpanOrder[j] = this.mSpanOrder[j - 1];
                    j--;
                    if (j <= 0) {
                        break;
                    }
                } while (start < this.mSpanStarts[j - 1]);
                this.mSpans[j] = span;
                this.mSpanStarts[j] = start;
                this.mSpanEnds[j] = end;
                this.mSpanFlags[j] = flags;
                this.mSpanOrder[j] = insertionOrder;
                invalidateIndex(j);
            }
        }
        int i2 = treeRoot();
        calcMax(i2);
        if (this.mIndexOfSpan == null) {
            this.mIndexOfSpan = new IdentityHashMap<>();
        }
        for (int i3 = this.mLowWaterMark; i3 < this.mSpanCount; i3++) {
            Integer existing = this.mIndexOfSpan.get(this.mSpans[i3]);
            if (existing == null || existing.intValue() != i3) {
                this.mIndexOfSpan.put(this.mSpans[i3], Integer.valueOf(i3));
            }
        }
        this.mLowWaterMark = Integer.MAX_VALUE;
    }

    private synchronized void invalidateIndex(int i) {
        this.mLowWaterMark = Math.min(i, this.mLowWaterMark);
    }
}
