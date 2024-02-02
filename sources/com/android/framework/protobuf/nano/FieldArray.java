package com.android.framework.protobuf.nano;
/* loaded from: classes3.dex */
public final class FieldArray implements Cloneable {
    private static final FieldData DELETED = new FieldData();
    private FieldData[] mData;
    private int[] mFieldNumbers;
    private boolean mGarbage;
    private int mSize;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FieldArray() {
        this(10);
    }

    FieldArray(int initialCapacity) {
        this.mGarbage = false;
        int initialCapacity2 = idealIntArraySize(initialCapacity);
        this.mFieldNumbers = new int[initialCapacity2];
        this.mData = new FieldData[initialCapacity2];
        this.mSize = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FieldData get(int fieldNumber) {
        int i = binarySearch(fieldNumber);
        if (i < 0 || this.mData[i] == DELETED) {
            return null;
        }
        return this.mData[i];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void remove(int fieldNumber) {
        int i = binarySearch(fieldNumber);
        if (i >= 0 && this.mData[i] != DELETED) {
            this.mData[i] = DELETED;
            this.mGarbage = true;
        }
    }

    private void gc() {
        int n = this.mSize;
        int[] keys = this.mFieldNumbers;
        FieldData[] values = this.mData;
        int o = 0;
        for (int o2 = 0; o2 < n; o2++) {
            FieldData val = values[o2];
            if (val != DELETED) {
                if (o2 != o) {
                    keys[o] = keys[o2];
                    values[o] = val;
                    values[o2] = null;
                }
                o++;
            }
        }
        this.mGarbage = false;
        this.mSize = o;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void put(int fieldNumber, FieldData data) {
        int i = binarySearch(fieldNumber);
        if (i >= 0) {
            this.mData[i] = data;
            return;
        }
        int i2 = ~i;
        if (i2 < this.mSize && this.mData[i2] == DELETED) {
            this.mFieldNumbers[i2] = fieldNumber;
            this.mData[i2] = data;
            return;
        }
        if (this.mGarbage && this.mSize >= this.mFieldNumbers.length) {
            gc();
            i2 = ~binarySearch(fieldNumber);
        }
        if (this.mSize >= this.mFieldNumbers.length) {
            int n = idealIntArraySize(this.mSize + 1);
            int[] nkeys = new int[n];
            FieldData[] nvalues = new FieldData[n];
            System.arraycopy(this.mFieldNumbers, 0, nkeys, 0, this.mFieldNumbers.length);
            System.arraycopy(this.mData, 0, nvalues, 0, this.mData.length);
            this.mFieldNumbers = nkeys;
            this.mData = nvalues;
        }
        if (this.mSize - i2 != 0) {
            System.arraycopy(this.mFieldNumbers, i2, this.mFieldNumbers, i2 + 1, this.mSize - i2);
            System.arraycopy(this.mData, i2, this.mData, i2 + 1, this.mSize - i2);
        }
        this.mFieldNumbers[i2] = fieldNumber;
        this.mData[i2] = data;
        this.mSize++;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int size() {
        if (this.mGarbage) {
            gc();
        }
        return this.mSize;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FieldData dataAt(int index) {
        if (this.mGarbage) {
            gc();
        }
        return this.mData[index];
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof FieldArray) {
            FieldArray other = (FieldArray) o;
            if (size() != other.size()) {
                return false;
            }
            return arrayEquals(this.mFieldNumbers, other.mFieldNumbers, this.mSize) && arrayEquals(this.mData, other.mData, this.mSize);
        }
        return false;
    }

    public int hashCode() {
        if (this.mGarbage) {
            gc();
        }
        int result = 17;
        for (int i = 0; i < this.mSize; i++) {
            int result2 = (31 * result) + this.mFieldNumbers[i];
            result = this.mData[i].hashCode() + (31 * result2);
        }
        return result;
    }

    private int idealIntArraySize(int need) {
        return idealByteArraySize(need * 4) / 4;
    }

    private int idealByteArraySize(int need) {
        for (int i = 4; i < 32; i++) {
            if (need <= (1 << i) - 12) {
                return (1 << i) - 12;
            }
        }
        return need;
    }

    private int binarySearch(int value) {
        int lo = 0;
        int hi = this.mSize - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int midVal = this.mFieldNumbers[mid];
            if (midVal < value) {
                lo = mid + 1;
            } else if (midVal > value) {
                hi = mid - 1;
            } else {
                return mid;
            }
        }
        return ~lo;
    }

    private boolean arrayEquals(int[] a, int[] b, int size) {
        for (int i = 0; i < size; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    private boolean arrayEquals(FieldData[] a, FieldData[] b, int size) {
        for (int i = 0; i < size; i++) {
            if (!a[i].equals(b[i])) {
                return false;
            }
        }
        return true;
    }

    /* renamed from: clone */
    public final FieldArray m67clone() {
        int size = size();
        FieldArray clone = new FieldArray(size);
        int i = 0;
        System.arraycopy(this.mFieldNumbers, 0, clone.mFieldNumbers, 0, size);
        while (true) {
            int i2 = i;
            if (i2 < size) {
                if (this.mData[i2] != null) {
                    clone.mData[i2] = this.mData[i2].m68clone();
                }
                i = i2 + 1;
            } else {
                clone.mSize = size;
                return clone;
            }
        }
    }
}
