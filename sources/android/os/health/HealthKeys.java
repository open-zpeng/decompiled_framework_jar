package android.os.health;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Arrays;
/* loaded from: classes2.dex */
public class HealthKeys {
    public static final int BASE_PACKAGE = 40000;
    public static final int BASE_PID = 20000;
    public static final int BASE_PROCESS = 30000;
    public static final int BASE_SERVICE = 50000;
    public static final int BASE_UID = 10000;
    public static final int TYPE_COUNT = 5;
    public static final int TYPE_MEASUREMENT = 1;
    public static final int TYPE_MEASUREMENTS = 4;
    public static final int TYPE_STATS = 2;
    public static final int TYPE_TIMER = 0;
    public static final int TYPE_TIMERS = 3;
    public static final int UNKNOWN_KEY = 0;

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: classes2.dex */
    public @interface Constant {
        int type();
    }

    /* loaded from: classes2.dex */
    public static class Constants {
        private final String mDataType;
        private final int[][] mKeys = new int[5];

        /* JADX INFO: Access modifiers changed from: private */
        public Constants(Class clazz) {
            this.mDataType = clazz.getSimpleName();
            Field[] fields = clazz.getDeclaredFields();
            int N = fields.length;
            SortedIntArray[] keys = new SortedIntArray[this.mKeys.length];
            for (int i = 0; i < keys.length; i++) {
                keys[i] = new SortedIntArray(N);
            }
            for (Field field : fields) {
                Constant constant = (Constant) field.getAnnotation(Constant.class);
                if (constant != null) {
                    int type = constant.type();
                    if (type >= keys.length) {
                        throw new RuntimeException("Unknown Constant type " + type + " on " + field);
                    }
                    try {
                        keys[type].addValue(field.getInt(null));
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException("Can't read constant value type=" + type + " field=" + field, ex);
                    }
                }
            }
            for (int i2 = 0; i2 < keys.length; i2++) {
                this.mKeys[i2] = keys[i2].getArray();
            }
        }

        public synchronized String getDataType() {
            return this.mDataType;
        }

        public synchronized int getSize(int type) {
            return this.mKeys[type].length;
        }

        public synchronized int getIndex(int type, int key) {
            int index = Arrays.binarySearch(this.mKeys[type], key);
            if (index >= 0) {
                return index;
            }
            throw new RuntimeException("Unknown Constant " + key + " (of type " + type + " )");
        }

        public synchronized int[] getKeys(int type) {
            return this.mKeys[type];
        }
    }

    /* loaded from: classes2.dex */
    private static class SortedIntArray {
        int[] mArray;
        int mCount;

        synchronized SortedIntArray(int maxCount) {
            this.mArray = new int[maxCount];
        }

        synchronized void addValue(int value) {
            int[] iArr = this.mArray;
            int i = this.mCount;
            this.mCount = i + 1;
            iArr[i] = value;
        }

        synchronized int[] getArray() {
            if (this.mCount == this.mArray.length) {
                Arrays.sort(this.mArray);
                return this.mArray;
            }
            int[] result = new int[this.mCount];
            System.arraycopy(this.mArray, 0, result, 0, this.mCount);
            Arrays.sort(result);
            return result;
        }
    }
}
