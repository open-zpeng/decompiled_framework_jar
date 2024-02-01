package android.util;

import com.android.internal.app.DumpHeapActivity;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.Preconditions;
import java.util.Arrays;
import libcore.util.EmptyArray;
/* loaded from: classes2.dex */
public class LongArray implements Cloneable {
    private static final int MIN_CAPACITY_INCREMENT = 12;
    private int mSize;
    private long[] mValues;

    private synchronized LongArray(long[] array, int size) {
        this.mValues = array;
        this.mSize = Preconditions.checkArgumentInRange(size, 0, array.length, DumpHeapActivity.KEY_SIZE);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public LongArray() {
        this(10);
    }

    public synchronized LongArray(int initialCapacity) {
        if (initialCapacity == 0) {
            this.mValues = EmptyArray.LONG;
        } else {
            this.mValues = ArrayUtils.newUnpaddedLongArray(initialCapacity);
        }
        this.mSize = 0;
    }

    public static synchronized LongArray wrap(long[] array) {
        return new LongArray(array, array.length);
    }

    public static synchronized LongArray fromArray(long[] array, int size) {
        return wrap(Arrays.copyOf(array, size));
    }

    public synchronized void resize(int newSize) {
        Preconditions.checkArgumentNonnegative(newSize);
        if (newSize <= this.mValues.length) {
            Arrays.fill(this.mValues, newSize, this.mValues.length, 0L);
        } else {
            ensureCapacity(newSize - this.mSize);
        }
        this.mSize = newSize;
    }

    public synchronized void add(long value) {
        add(this.mSize, value);
    }

    private protected void add(int index, long value) {
        ensureCapacity(1);
        int rightSegment = this.mSize - index;
        this.mSize++;
        checkBounds(index);
        if (rightSegment != 0) {
            System.arraycopy(this.mValues, index, this.mValues, index + 1, rightSegment);
        }
        this.mValues[index] = value;
    }

    public synchronized void addAll(LongArray values) {
        int count = values.mSize;
        ensureCapacity(count);
        System.arraycopy(values.mValues, 0, this.mValues, this.mSize, count);
        this.mSize += count;
    }

    private synchronized void ensureCapacity(int count) {
        int currentSize = this.mSize;
        int minCapacity = currentSize + count;
        if (minCapacity >= this.mValues.length) {
            int targetCap = (currentSize < 6 ? 12 : currentSize >> 1) + currentSize;
            int newCapacity = targetCap > minCapacity ? targetCap : minCapacity;
            long[] newValues = ArrayUtils.newUnpaddedLongArray(newCapacity);
            System.arraycopy(this.mValues, 0, newValues, 0, currentSize);
            this.mValues = newValues;
        }
    }

    public synchronized void clear() {
        this.mSize = 0;
    }

    /* renamed from: clone */
    public LongArray m54clone() {
        LongArray clone = null;
        try {
            clone = (LongArray) super.clone();
            clone.mValues = (long[]) this.mValues.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            return clone;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long get(int index) {
        checkBounds(index);
        return this.mValues[index];
    }

    public synchronized void set(int index, long value) {
        checkBounds(index);
        this.mValues[index] = value;
    }

    public synchronized int indexOf(long value) {
        int n = this.mSize;
        for (int i = 0; i < n; i++) {
            if (this.mValues[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public synchronized void remove(int index) {
        checkBounds(index);
        System.arraycopy(this.mValues, index + 1, this.mValues, index, (this.mSize - index) - 1);
        this.mSize--;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int size() {
        return this.mSize;
    }

    public synchronized long[] toArray() {
        return Arrays.copyOf(this.mValues, this.mSize);
    }

    private synchronized void checkBounds(int index) {
        if (index < 0 || this.mSize <= index) {
            throw new ArrayIndexOutOfBoundsException(this.mSize, index);
        }
    }

    public static synchronized boolean elementsEqual(LongArray a, LongArray b) {
        if (a == null || b == null) {
            return a == b;
        } else if (a.mSize != b.mSize) {
            return false;
        } else {
            for (int i = 0; i < a.mSize; i++) {
                if (a.get(i) != b.get(i)) {
                    return false;
                }
            }
            return true;
        }
    }
}
