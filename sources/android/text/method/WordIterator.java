package android.text.method;

import android.icu.lang.UCharacter;
import android.icu.text.BreakIterator;
import android.mtp.MtpConstants;
import android.text.CharSequenceCharacterIterator;
import android.text.Selection;
import java.util.Locale;
/* loaded from: classes2.dex */
public class WordIterator implements Selection.PositionIterator {
    private static final int WINDOW_WIDTH = 50;
    private CharSequence mCharSeq;
    private int mEnd;
    private final BreakIterator mIterator;
    private int mStart;

    public synchronized WordIterator() {
        this(Locale.getDefault());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public WordIterator(Locale locale) {
        this.mIterator = BreakIterator.getWordInstance(locale);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCharSequence(CharSequence charSequence, int start, int end) {
        if (start >= 0 && end <= charSequence.length()) {
            this.mCharSeq = charSequence;
            this.mStart = Math.max(0, start - 50);
            this.mEnd = Math.min(charSequence.length(), end + 50);
            this.mIterator.setText(new CharSequenceCharacterIterator(charSequence, this.mStart, this.mEnd));
            return;
        }
        throw new IndexOutOfBoundsException("input indexes are outside the CharSequence");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int preceding(int offset) {
        checkOffsetIsValid(offset);
        do {
            offset = this.mIterator.preceding(offset);
            if (offset == -1) {
                break;
            }
        } while (!isOnLetterOrDigit(offset));
        return offset;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int following(int offset) {
        checkOffsetIsValid(offset);
        do {
            offset = this.mIterator.following(offset);
            if (offset == -1) {
                break;
            }
        } while (!isAfterLetterOrDigit(offset));
        return offset;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isBoundary(int offset) {
        checkOffsetIsValid(offset);
        return this.mIterator.isBoundary(offset);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int nextBoundary(int offset) {
        checkOffsetIsValid(offset);
        return this.mIterator.following(offset);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int prevBoundary(int offset) {
        checkOffsetIsValid(offset);
        return this.mIterator.preceding(offset);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getBeginning(int offset) {
        return getBeginning(offset, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getEnd(int offset) {
        return getEnd(offset, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getPrevWordBeginningOnTwoWordsBoundary(int offset) {
        return getBeginning(offset, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getNextWordEndOnTwoWordBoundary(int offset) {
        return getEnd(offset, true);
    }

    private synchronized int getBeginning(int offset, boolean getPrevWordBeginningOnTwoWordsBoundary) {
        checkOffsetIsValid(offset);
        if (isOnLetterOrDigit(offset)) {
            if (this.mIterator.isBoundary(offset) && (!isAfterLetterOrDigit(offset) || !getPrevWordBeginningOnTwoWordsBoundary)) {
                return offset;
            }
            return this.mIterator.preceding(offset);
        } else if (isAfterLetterOrDigit(offset)) {
            return this.mIterator.preceding(offset);
        } else {
            return -1;
        }
    }

    private synchronized int getEnd(int offset, boolean getNextWordEndOnTwoWordBoundary) {
        checkOffsetIsValid(offset);
        if (isAfterLetterOrDigit(offset)) {
            if (this.mIterator.isBoundary(offset) && (!isOnLetterOrDigit(offset) || !getNextWordEndOnTwoWordBoundary)) {
                return offset;
            }
            return this.mIterator.following(offset);
        } else if (isOnLetterOrDigit(offset)) {
            return this.mIterator.following(offset);
        } else {
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getPunctuationBeginning(int offset) {
        checkOffsetIsValid(offset);
        while (offset != -1 && !isPunctuationStartBoundary(offset)) {
            offset = prevBoundary(offset);
        }
        return offset;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getPunctuationEnd(int offset) {
        checkOffsetIsValid(offset);
        while (offset != -1 && !isPunctuationEndBoundary(offset)) {
            offset = nextBoundary(offset);
        }
        return offset;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAfterPunctuation(int offset) {
        if (this.mStart < offset && offset <= this.mEnd) {
            int codePoint = Character.codePointBefore(this.mCharSeq, offset);
            return isPunctuation(codePoint);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isOnPunctuation(int offset) {
        if (this.mStart <= offset && offset < this.mEnd) {
            int codePoint = Character.codePointAt(this.mCharSeq, offset);
            return isPunctuation(codePoint);
        }
        return false;
    }

    public static synchronized boolean isMidWordPunctuation(Locale locale, int codePoint) {
        int wb = UCharacter.getIntPropertyValue(codePoint, MtpConstants.OPERATION_GET_DEVICE_PROP_DESC);
        return wb == 4 || wb == 11 || wb == 15;
    }

    private synchronized boolean isPunctuationStartBoundary(int offset) {
        return isOnPunctuation(offset) && !isAfterPunctuation(offset);
    }

    private synchronized boolean isPunctuationEndBoundary(int offset) {
        return !isOnPunctuation(offset) && isAfterPunctuation(offset);
    }

    private static synchronized boolean isPunctuation(int cp) {
        int type = Character.getType(cp);
        return type == 23 || type == 20 || type == 22 || type == 30 || type == 29 || type == 24 || type == 21;
    }

    private synchronized boolean isAfterLetterOrDigit(int offset) {
        if (this.mStart < offset && offset <= this.mEnd) {
            int codePoint = Character.codePointBefore(this.mCharSeq, offset);
            return Character.isLetterOrDigit(codePoint);
        }
        return false;
    }

    private synchronized boolean isOnLetterOrDigit(int offset) {
        if (this.mStart <= offset && offset < this.mEnd) {
            int codePoint = Character.codePointAt(this.mCharSeq, offset);
            return Character.isLetterOrDigit(codePoint);
        }
        return false;
    }

    private synchronized void checkOffsetIsValid(int offset) {
        if (this.mStart > offset || offset > this.mEnd) {
            throw new IllegalArgumentException("Invalid offset: " + offset + ". Valid range is [" + this.mStart + ", " + this.mEnd + "]");
        }
    }
}
