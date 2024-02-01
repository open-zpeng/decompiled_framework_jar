package android.text;

import com.android.internal.util.ArrayUtils;
import libcore.util.EmptyArray;
/* loaded from: classes2.dex */
public final class AutoGrowArray {
    private static final int MAX_CAPACITY_TO_BE_KEPT = 10000;
    private static final int MIN_CAPACITY_INCREMENT = 12;

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized int computeNewCapacity(int currentSize, int requested) {
        int targetCapacity = (currentSize < 6 ? 12 : currentSize >> 1) + currentSize;
        return targetCapacity > requested ? targetCapacity : requested;
    }

    /* loaded from: classes2.dex */
    public static class ByteArray {
        private int mSize;
        private byte[] mValues;

        public synchronized ByteArray() {
            this(10);
        }

        public synchronized ByteArray(int initialCapacity) {
            if (initialCapacity == 0) {
                this.mValues = EmptyArray.BYTE;
            } else {
                this.mValues = ArrayUtils.newUnpaddedByteArray(initialCapacity);
            }
            this.mSize = 0;
        }

        public synchronized void resize(int newSize) {
            if (newSize > this.mValues.length) {
                ensureCapacity(newSize - this.mSize);
            }
            this.mSize = newSize;
        }

        public synchronized void append(byte value) {
            ensureCapacity(1);
            byte[] bArr = this.mValues;
            int i = this.mSize;
            this.mSize = i + 1;
            bArr[i] = value;
        }

        private synchronized void ensureCapacity(int count) {
            int requestedSize = this.mSize + count;
            if (requestedSize >= this.mValues.length) {
                int newCapacity = AutoGrowArray.computeNewCapacity(this.mSize, requestedSize);
                byte[] newValues = ArrayUtils.newUnpaddedByteArray(newCapacity);
                System.arraycopy(this.mValues, 0, newValues, 0, this.mSize);
                this.mValues = newValues;
            }
        }

        public synchronized void clear() {
            this.mSize = 0;
        }

        public synchronized void clearWithReleasingLargeArray() {
            clear();
            if (this.mValues.length > 10000) {
                this.mValues = EmptyArray.BYTE;
            }
        }

        public synchronized byte get(int index) {
            return this.mValues[index];
        }

        public synchronized void set(int index, byte value) {
            this.mValues[index] = value;
        }

        public synchronized int size() {
            return this.mSize;
        }

        public synchronized byte[] getRawArray() {
            return this.mValues;
        }
    }

    /* loaded from: classes2.dex */
    public static class IntArray {
        private int mSize;
        private int[] mValues;

        public synchronized IntArray() {
            this(10);
        }

        public synchronized IntArray(int initialCapacity) {
            if (initialCapacity == 0) {
                this.mValues = EmptyArray.INT;
            } else {
                this.mValues = ArrayUtils.newUnpaddedIntArray(initialCapacity);
            }
            this.mSize = 0;
        }

        public synchronized void resize(int newSize) {
            if (newSize > this.mValues.length) {
                ensureCapacity(newSize - this.mSize);
            }
            this.mSize = newSize;
        }

        public synchronized void append(int value) {
            ensureCapacity(1);
            int[] iArr = this.mValues;
            int i = this.mSize;
            this.mSize = i + 1;
            iArr[i] = value;
        }

        private synchronized void ensureCapacity(int count) {
            int requestedSize = this.mSize + count;
            if (requestedSize >= this.mValues.length) {
                int newCapacity = AutoGrowArray.computeNewCapacity(this.mSize, requestedSize);
                int[] newValues = ArrayUtils.newUnpaddedIntArray(newCapacity);
                System.arraycopy(this.mValues, 0, newValues, 0, this.mSize);
                this.mValues = newValues;
            }
        }

        public synchronized void clear() {
            this.mSize = 0;
        }

        public synchronized void clearWithReleasingLargeArray() {
            clear();
            if (this.mValues.length > 10000) {
                this.mValues = EmptyArray.INT;
            }
        }

        public synchronized int get(int index) {
            return this.mValues[index];
        }

        public synchronized void set(int index, int value) {
            this.mValues[index] = value;
        }

        public synchronized int size() {
            return this.mSize;
        }

        public synchronized int[] getRawArray() {
            return this.mValues;
        }
    }

    /* loaded from: classes2.dex */
    public static class FloatArray {
        private int mSize;
        private float[] mValues;

        public synchronized FloatArray() {
            this(10);
        }

        public synchronized FloatArray(int initialCapacity) {
            if (initialCapacity == 0) {
                this.mValues = EmptyArray.FLOAT;
            } else {
                this.mValues = ArrayUtils.newUnpaddedFloatArray(initialCapacity);
            }
            this.mSize = 0;
        }

        public synchronized void resize(int newSize) {
            if (newSize > this.mValues.length) {
                ensureCapacity(newSize - this.mSize);
            }
            this.mSize = newSize;
        }

        public synchronized void append(float value) {
            ensureCapacity(1);
            float[] fArr = this.mValues;
            int i = this.mSize;
            this.mSize = i + 1;
            fArr[i] = value;
        }

        private synchronized void ensureCapacity(int count) {
            int requestedSize = this.mSize + count;
            if (requestedSize >= this.mValues.length) {
                int newCapacity = AutoGrowArray.computeNewCapacity(this.mSize, requestedSize);
                float[] newValues = ArrayUtils.newUnpaddedFloatArray(newCapacity);
                System.arraycopy(this.mValues, 0, newValues, 0, this.mSize);
                this.mValues = newValues;
            }
        }

        public synchronized void clear() {
            this.mSize = 0;
        }

        public synchronized void clearWithReleasingLargeArray() {
            clear();
            if (this.mValues.length > 10000) {
                this.mValues = EmptyArray.FLOAT;
            }
        }

        public synchronized float get(int index) {
            return this.mValues[index];
        }

        public synchronized void set(int index, float value) {
            this.mValues[index] = value;
        }

        public synchronized int size() {
            return this.mSize;
        }

        public synchronized float[] getRawArray() {
            return this.mValues;
        }
    }
}
