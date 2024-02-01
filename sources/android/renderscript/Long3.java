package android.renderscript;
/* loaded from: classes2.dex */
public class Long3 {
    public long x;
    public long y;
    public long z;

    public Long3() {
    }

    public synchronized Long3(long i) {
        this.z = i;
        this.y = i;
        this.x = i;
    }

    public Long3(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public synchronized Long3(Long3 source) {
        this.x = source.x;
        this.y = source.y;
        this.z = source.z;
    }

    public synchronized void add(Long3 a) {
        this.x += a.x;
        this.y += a.y;
        this.z += a.z;
    }

    public static synchronized Long3 add(Long3 a, Long3 b) {
        Long3 result = new Long3();
        result.x = a.x + b.x;
        result.y = a.y + b.y;
        result.z = a.z + b.z;
        return result;
    }

    public synchronized void add(long value) {
        this.x += value;
        this.y += value;
        this.z += value;
    }

    public static synchronized Long3 add(Long3 a, long b) {
        Long3 result = new Long3();
        result.x = a.x + b;
        result.y = a.y + b;
        result.z = a.z + b;
        return result;
    }

    public synchronized void sub(Long3 a) {
        this.x -= a.x;
        this.y -= a.y;
        this.z -= a.z;
    }

    public static synchronized Long3 sub(Long3 a, Long3 b) {
        Long3 result = new Long3();
        result.x = a.x - b.x;
        result.y = a.y - b.y;
        result.z = a.z - b.z;
        return result;
    }

    public synchronized void sub(long value) {
        this.x -= value;
        this.y -= value;
        this.z -= value;
    }

    public static synchronized Long3 sub(Long3 a, long b) {
        Long3 result = new Long3();
        result.x = a.x - b;
        result.y = a.y - b;
        result.z = a.z - b;
        return result;
    }

    public synchronized void mul(Long3 a) {
        this.x *= a.x;
        this.y *= a.y;
        this.z *= a.z;
    }

    public static synchronized Long3 mul(Long3 a, Long3 b) {
        Long3 result = new Long3();
        result.x = a.x * b.x;
        result.y = a.y * b.y;
        result.z = a.z * b.z;
        return result;
    }

    public synchronized void mul(long value) {
        this.x *= value;
        this.y *= value;
        this.z *= value;
    }

    public static synchronized Long3 mul(Long3 a, long b) {
        Long3 result = new Long3();
        result.x = a.x * b;
        result.y = a.y * b;
        result.z = a.z * b;
        return result;
    }

    public synchronized void div(Long3 a) {
        this.x /= a.x;
        this.y /= a.y;
        this.z /= a.z;
    }

    public static synchronized Long3 div(Long3 a, Long3 b) {
        Long3 result = new Long3();
        result.x = a.x / b.x;
        result.y = a.y / b.y;
        result.z = a.z / b.z;
        return result;
    }

    public synchronized void div(long value) {
        this.x /= value;
        this.y /= value;
        this.z /= value;
    }

    public static synchronized Long3 div(Long3 a, long b) {
        Long3 result = new Long3();
        result.x = a.x / b;
        result.y = a.y / b;
        result.z = a.z / b;
        return result;
    }

    public synchronized void mod(Long3 a) {
        this.x %= a.x;
        this.y %= a.y;
        this.z %= a.z;
    }

    public static synchronized Long3 mod(Long3 a, Long3 b) {
        Long3 result = new Long3();
        result.x = a.x % b.x;
        result.y = a.y % b.y;
        result.z = a.z % b.z;
        return result;
    }

    public synchronized void mod(long value) {
        this.x %= value;
        this.y %= value;
        this.z %= value;
    }

    public static synchronized Long3 mod(Long3 a, long b) {
        Long3 result = new Long3();
        result.x = a.x % b;
        result.y = a.y % b;
        result.z = a.z % b;
        return result;
    }

    public synchronized long length() {
        return 3L;
    }

    public synchronized void negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
    }

    public synchronized long dotProduct(Long3 a) {
        return (this.x * a.x) + (this.y * a.y) + (this.z * a.z);
    }

    public static synchronized long dotProduct(Long3 a, Long3 b) {
        return (b.x * a.x) + (b.y * a.y) + (b.z * a.z);
    }

    public synchronized void addMultiple(Long3 a, long factor) {
        this.x += a.x * factor;
        this.y += a.y * factor;
        this.z += a.z * factor;
    }

    public synchronized void set(Long3 a) {
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;
    }

    public synchronized void setValues(long a, long b, long c) {
        this.x = a;
        this.y = b;
        this.z = c;
    }

    public synchronized long elementSum() {
        return this.x + this.y + this.z;
    }

    public synchronized long get(int i) {
        switch (i) {
            case 0:
                return this.x;
            case 1:
                return this.y;
            case 2:
                return this.z;
            default:
                throw new IndexOutOfBoundsException("Index: i");
        }
    }

    public synchronized void setAt(int i, long value) {
        switch (i) {
            case 0:
                this.x = value;
                return;
            case 1:
                this.y = value;
                return;
            case 2:
                this.z = value;
                return;
            default:
                throw new IndexOutOfBoundsException("Index: i");
        }
    }

    public synchronized void addAt(int i, long value) {
        switch (i) {
            case 0:
                this.x += value;
                return;
            case 1:
                this.y += value;
                return;
            case 2:
                this.z += value;
                return;
            default:
                throw new IndexOutOfBoundsException("Index: i");
        }
    }

    public synchronized void copyTo(long[] data, int offset) {
        data[offset] = this.x;
        data[offset + 1] = this.y;
        data[offset + 2] = this.z;
    }
}
