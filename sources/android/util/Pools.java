package android.util;
/* loaded from: classes2.dex */
public final class Pools {

    /* loaded from: classes2.dex */
    public interface Pool<T> {
        /* JADX INFO: Access modifiers changed from: private */
        T acquire();

        /* JADX INFO: Access modifiers changed from: private */
        boolean release(T t);
    }

    private synchronized Pools() {
    }

    /* loaded from: classes2.dex */
    public static class SimplePool<T> implements Pool<T> {
        public protected final Object[] mPool;
        private int mPoolSize;

        /* JADX INFO: Access modifiers changed from: private */
        public SimplePool(int maxPoolSize) {
            if (maxPoolSize <= 0) {
                throw new IllegalArgumentException("The max pool size must be > 0");
            }
            this.mPool = new Object[maxPoolSize];
        }

        /* JADX INFO: Access modifiers changed from: private */
        public T acquire() {
            if (this.mPoolSize > 0) {
                int lastPooledIndex = this.mPoolSize - 1;
                T instance = (T) this.mPool[lastPooledIndex];
                this.mPool[lastPooledIndex] = null;
                this.mPoolSize--;
                return instance;
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean release(T instance) {
            if (isInPool(instance)) {
                throw new IllegalStateException("Already in the pool!");
            }
            if (this.mPoolSize < this.mPool.length) {
                this.mPool[this.mPoolSize] = instance;
                this.mPoolSize++;
                return true;
            }
            return false;
        }

        private synchronized boolean isInPool(T instance) {
            for (int i = 0; i < this.mPoolSize; i++) {
                if (this.mPool[i] == instance) {
                    return true;
                }
            }
            return false;
        }
    }

    /* loaded from: classes2.dex */
    public static class SynchronizedPool<T> extends SimplePool<T> {
        private final Object mLock;

        public synchronized SynchronizedPool(int maxPoolSize, Object lock) {
            super(maxPoolSize);
            this.mLock = lock;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public SynchronizedPool(int maxPoolSize) {
            this(maxPoolSize, new Object());
        }

        /* JADX INFO: Access modifiers changed from: private */
        public T acquire() {
            T t;
            synchronized (this.mLock) {
                t = (T) super.acquire();
            }
            return t;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean release(T element) {
            boolean release;
            synchronized (this.mLock) {
                release = super.release(element);
            }
            return release;
        }
    }
}
