package android.renderscript;

/* loaded from: classes2.dex */
public class Long2 {
    public long x;
    public long y;

    public Long2() {
    }

    public Long2(long i) {
        this.y = i;
        this.x = i;
    }

    public Long2(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public Long2(Long2 source) {
        this.x = source.x;
        this.y = source.y;
    }

    public void add(Long2 a) {
        this.x += a.x;
        this.y += a.y;
    }

    public static Long2 add(Long2 a, Long2 b) {
        Long2 result = new Long2();
        result.x = a.x + b.x;
        result.y = a.y + b.y;
        return result;
    }

    public void add(long value) {
        this.x += value;
        this.y += value;
    }

    public static Long2 add(Long2 a, long b) {
        Long2 result = new Long2();
        result.x = a.x + b;
        result.y = a.y + b;
        return result;
    }

    public void sub(Long2 a) {
        this.x -= a.x;
        this.y -= a.y;
    }

    public static Long2 sub(Long2 a, Long2 b) {
        Long2 result = new Long2();
        result.x = a.x - b.x;
        result.y = a.y - b.y;
        return result;
    }

    public void sub(long value) {
        this.x -= value;
        this.y -= value;
    }

    public static Long2 sub(Long2 a, long b) {
        Long2 result = new Long2();
        result.x = a.x - b;
        result.y = a.y - b;
        return result;
    }

    public void mul(Long2 a) {
        this.x *= a.x;
        this.y *= a.y;
    }

    public static Long2 mul(Long2 a, Long2 b) {
        Long2 result = new Long2();
        result.x = a.x * b.x;
        result.y = a.y * b.y;
        return result;
    }

    public void mul(long value) {
        this.x *= value;
        this.y *= value;
    }

    public static Long2 mul(Long2 a, long b) {
        Long2 result = new Long2();
        result.x = a.x * b;
        result.y = a.y * b;
        return result;
    }

    public void div(Long2 a) {
        this.x /= a.x;
        this.y /= a.y;
    }

    public static Long2 div(Long2 a, Long2 b) {
        Long2 result = new Long2();
        result.x = a.x / b.x;
        result.y = a.y / b.y;
        return result;
    }

    public void div(long value) {
        this.x /= value;
        this.y /= value;
    }

    public static Long2 div(Long2 a, long b) {
        Long2 result = new Long2();
        result.x = a.x / b;
        result.y = a.y / b;
        return result;
    }

    public void mod(Long2 a) {
        this.x %= a.x;
        this.y %= a.y;
    }

    public static Long2 mod(Long2 a, Long2 b) {
        Long2 result = new Long2();
        result.x = a.x % b.x;
        result.y = a.y % b.y;
        return result;
    }

    public void mod(long value) {
        this.x %= value;
        this.y %= value;
    }

    public static Long2 mod(Long2 a, long b) {
        Long2 result = new Long2();
        result.x = a.x % b;
        result.y = a.y % b;
        return result;
    }

    public long length() {
        return 2L;
    }

    public void negate() {
        this.x = -this.x;
        this.y = -this.y;
    }

    public long dotProduct(Long2 a) {
        return (this.x * a.x) + (this.y * a.y);
    }

    public static long dotProduct(Long2 a, Long2 b) {
        return (b.x * a.x) + (b.y * a.y);
    }

    public void addMultiple(Long2 a, long factor) {
        this.x += a.x * factor;
        this.y += a.y * factor;
    }

    public void set(Long2 a) {
        this.x = a.x;
        this.y = a.y;
    }

    public void setValues(long a, long b) {
        this.x = a;
        this.y = b;
    }

    public long elementSum() {
        return this.x + this.y;
    }

    public long get(int i) {
        if (i != 0) {
            if (i == 1) {
                return this.y;
            }
            throw new IndexOutOfBoundsException("Index: i");
        }
        return this.x;
    }

    public void setAt(int i, long value) {
        if (i == 0) {
            this.x = value;
        } else if (i == 1) {
            this.y = value;
        } else {
            throw new IndexOutOfBoundsException("Index: i");
        }
    }

    public void addAt(int i, long value) {
        if (i == 0) {
            this.x += value;
        } else if (i == 1) {
            this.y += value;
        } else {
            throw new IndexOutOfBoundsException("Index: i");
        }
    }

    public void copyTo(long[] data, int offset) {
        data[offset] = this.x;
        data[offset + 1] = this.y;
    }
}
