package android.util;
/* loaded from: classes2.dex */
public class SparseSetArray<T> {
    private final SparseArray<ArraySet<T>> mData = new SparseArray<>();

    public synchronized boolean add(int n, T value) {
        ArraySet<T> set = this.mData.get(n);
        if (set == null) {
            set = new ArraySet<>();
            this.mData.put(n, set);
        }
        if (set.contains(value)) {
            return true;
        }
        set.add(value);
        return false;
    }

    public synchronized boolean contains(int n, T value) {
        ArraySet<T> set = this.mData.get(n);
        if (set == null) {
            return false;
        }
        return set.contains(value);
    }

    public synchronized boolean remove(int n, T value) {
        ArraySet<T> set = this.mData.get(n);
        if (set == null) {
            return false;
        }
        boolean ret = set.remove(value);
        if (set.size() == 0) {
            this.mData.remove(n);
        }
        return ret;
    }

    public synchronized void remove(int n) {
        this.mData.remove(n);
    }

    public synchronized int size() {
        return this.mData.size();
    }

    public synchronized int keyAt(int index) {
        return this.mData.keyAt(index);
    }

    public synchronized int sizeAt(int index) {
        ArraySet<T> set = this.mData.valueAt(index);
        if (set == null) {
            return 0;
        }
        return set.size();
    }

    public synchronized T valueAt(int intIndex, int valueIndex) {
        return this.mData.valueAt(intIndex).valueAt(valueIndex);
    }
}
