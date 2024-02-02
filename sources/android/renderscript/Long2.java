package android.renderscript;
/* loaded from: classes2.dex */
public class Long2 {
    public long x;
    public long y;

    public Long2() {
    }

    public synchronized Long2(long i) {
        this.y = i;
        this.x = i;
    }

    public Long2(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public synchronized Long2(Long2 source) {
        this.x = source.x;
        this.y = source.y;
    }

    public synchronized void add(Long2 a) {
        this.x += a.x;
        this.y += a.y;
    }

    public static synchronized Long2 add(Long2 a, Long2 b) {
        Long2 result = new Long2();
        result.x = a.x + b.x;
        result.y = a.y + b.y;
        return result;
    }

    public synchronized void add(long value) {
        this.x += value;
        this.y += value;
    }

    public static synchronized Long2 add(Long2 a, long b) {
        Long2 result = new Long2();
        result.x = a.x + b;
        result.y = a.y + b;
        return result;
    }

    public synchronized void sub(Long2 a) {
        this.x -= a.x;
        this.y -= a.y;
    }

    public static synchronized Long2 sub(Long2 a, Long2 b) {
        Long2 result = new Long2();
        result.x = a.x - b.x;
        result.y = a.y - b.y;
        return result;
    }

    public synchronized void sub(long value) {
        this.x -= value;
        this.y -= value;
    }

    public static synchronized Long2 sub(Long2 a, long b) {
        Long2 result = new Long2();
        result.x = a.x - b;
        result.y = a.y - b;
        return result;
    }

    public synchronized void mul(Long2 a) {
        this.x *= a.x;
        this.y *= a.y;
    }

    public static synchronized Long2 mul(Long2 a, Long2 b) {
        Long2 result = new Long2();
        result.x = a.x * b.x;
        result.y = a.y * b.y;
        return result;
    }

    public synchronized void mul(long value) {
        this.x *= value;
        this.y *= value;
    }

    public static synchronized Long2 mul(Long2 a, long b) {
        Long2 result = new Long2();
        result.x = a.x * b;
        result.y = a.y * b;
        return result;
    }

    public synchronized void div(Long2 a) {
        this.x /= a.x;
        this.y /= a.y;
    }

    public static synchronized Long2 div(Long2 a, Long2 b) {
        Long2 result = new Long2();
        result.x = a.x / b.x;
        result.y = a.y / b.y;
        return result;
    }

    public synchronized void div(long value) {
        this.x /= value;
        this.y /= value;
    }

    public static synchronized Long2 div(Long2 a, long b) {
        Long2 result = new Long2();
        result.x = a.x / b;
        result.y = a.y / b;
        return result;
    }

    public synchronized void mod(Long2 a) {
        this.x %= a.x;
        this.y %= a.y;
    }

    public static synchronized Long2 mod(Long2 a, Long2 b) {
        Long2 result = new Long2();
        result.x = a.x % b.x;
        result.y = a.y % b.y;
        return result;
    }

    public synchronized void mod(long value) {
        this.x %= value;
        this.y %= value;
    }

    public static synchronized Long2 mod(Long2 a, long b) {
        Long2 result = new Long2();
        result.x = a.x % b;
        result.y = a.y % b;
        return result;
    }

    public synchronized long length() {
        return 2L;
    }

    public synchronized void negate() {
        this.x = -this.x;
        this.y = -this.y;
    }

    public synchronized long dotProduct(Long2 a) {
        return (this.x * a.x) + (this.y * a.y);
    }

    public static synchronized long dotProduct(Long2 a, Long2 b) {
        return (b.x * a.x) + (b.y * a.y);
    }

    public synchronized void addMultiple(Long2 a, long factor) {
        this.x += a.x * factor;
        this.y += a.y * factor;
    }

    public synchronized void set(Long2 a) {
        this.x = a.x;
        this.y = a.y;
    }

    public synchronized void setValues(long a, long b) {
        this.x = a;
        this.y = b;
    }

    public synchronized long elementSum() {
        return this.x + this.y;
    }

    public synchronized long get(int i) {
        switch (i) {
            case 0:
                return this.x;
            case 1:
                return this.y;
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
            default:
                throw new IndexOutOfBoundsException("Index: i");
        }
    }

    public synchronized void copyTo(long[] data, int offset) {
        data[offset] = this.x;
        data[offset + 1] = this.y;
    }
}
