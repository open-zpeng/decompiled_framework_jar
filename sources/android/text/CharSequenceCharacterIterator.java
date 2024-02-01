package android.text;

import java.text.CharacterIterator;

/* loaded from: classes2.dex */
public class CharSequenceCharacterIterator implements CharacterIterator {
    private final int mBeginIndex;
    private final CharSequence mCharSeq;
    private final int mEndIndex;
    private int mIndex;

    public CharSequenceCharacterIterator(CharSequence text, int start, int end) {
        this.mCharSeq = text;
        this.mIndex = start;
        this.mBeginIndex = start;
        this.mEndIndex = end;
    }

    @Override // java.text.CharacterIterator
    public char first() {
        this.mIndex = this.mBeginIndex;
        return current();
    }

    @Override // java.text.CharacterIterator
    public char last() {
        int i = this.mBeginIndex;
        int i2 = this.mEndIndex;
        if (i == i2) {
            this.mIndex = i2;
            return (char) 65535;
        }
        this.mIndex = i2 - 1;
        return this.mCharSeq.charAt(this.mIndex);
    }

    @Override // java.text.CharacterIterator
    public char current() {
        int i = this.mIndex;
        if (i == this.mEndIndex) {
            return (char) 65535;
        }
        return this.mCharSeq.charAt(i);
    }

    @Override // java.text.CharacterIterator
    public char next() {
        this.mIndex++;
        int i = this.mIndex;
        int i2 = this.mEndIndex;
        if (i >= i2) {
            this.mIndex = i2;
            return (char) 65535;
        }
        return this.mCharSeq.charAt(i);
    }

    @Override // java.text.CharacterIterator
    public char previous() {
        int i = this.mIndex;
        if (i <= this.mBeginIndex) {
            return (char) 65535;
        }
        this.mIndex = i - 1;
        return this.mCharSeq.charAt(this.mIndex);
    }

    @Override // java.text.CharacterIterator
    public char setIndex(int position) {
        if (this.mBeginIndex <= position && position <= this.mEndIndex) {
            this.mIndex = position;
            return current();
        }
        throw new IllegalArgumentException("invalid position");
    }

    @Override // java.text.CharacterIterator
    public int getBeginIndex() {
        return this.mBeginIndex;
    }

    @Override // java.text.CharacterIterator
    public int getEndIndex() {
        return this.mEndIndex;
    }

    @Override // java.text.CharacterIterator
    public int getIndex() {
        return this.mIndex;
    }

    @Override // java.text.CharacterIterator
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
}
