package android.renderscript;
/* loaded from: classes2.dex */
public class Int2 {
    public int x;
    public int y;

    public Int2() {
    }

    public synchronized Int2(int i) {
        this.y = i;
        this.x = i;
    }

    public Int2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public synchronized Int2(Int2 source) {
        this.x = source.x;
        this.y = source.y;
    }

    public synchronized void add(Int2 a) {
        this.x += a.x;
        this.y += a.y;
    }

    public static synchronized Int2 add(Int2 a, Int2 b) {
        Int2 result = new Int2();
        result.x = a.x + b.x;
        result.y = a.y + b.y;
        return result;
    }

    public synchronized void add(int value) {
        this.x += value;
        this.y += value;
    }

    public static synchronized Int2 add(Int2 a, int b) {
        Int2 result = new Int2();
        result.x = a.x + b;
        result.y = a.y + b;
        return result;
    }

    public synchronized void sub(Int2 a) {
        this.x -= a.x;
        this.y -= a.y;
    }

    public static synchronized Int2 sub(Int2 a, Int2 b) {
        Int2 result = new Int2();
        result.x = a.x - b.x;
        result.y = a.y - b.y;
        return result;
    }

    public synchronized void sub(int value) {
        this.x -= value;
        this.y -= value;
    }

    public static synchronized Int2 sub(Int2 a, int b) {
        Int2 result = new Int2();
        result.x = a.x - b;
        result.y = a.y - b;
        return result;
    }

    public synchronized void mul(Int2 a) {
        this.x *= a.x;
        this.y *= a.y;
    }

    public static synchronized Int2 mul(Int2 a, Int2 b) {
        Int2 result = new Int2();
        result.x = a.x * b.x;
        result.y = a.y * b.y;
        return result;
    }

    public synchronized void mul(int value) {
        this.x *= value;
        this.y *= value;
    }

    public static synchronized Int2 mul(Int2 a, int b) {
        Int2 result = new Int2();
        result.x = a.x * b;
        result.y = a.y * b;
        return result;
    }

    public synchronized void div(Int2 a) {
        this.x /= a.x;
        this.y /= a.y;
    }

    public static synchronized Int2 div(Int2 a, Int2 b) {
        Int2 result = new Int2();
        result.x = a.x / b.x;
        result.y = a.y / b.y;
        return result;
    }

    public synchronized void div(int value) {
        this.x /= value;
        this.y /= value;
    }

    public static synchronized Int2 div(Int2 a, int b) {
        Int2 result = new Int2();
        result.x = a.x / b;
        result.y = a.y / b;
        return result;
    }

    public synchronized void mod(Int2 a) {
        this.x %= a.x;
        this.y %= a.y;
    }

    public static synchronized Int2 mod(Int2 a, Int2 b) {
        Int2 result = new Int2();
        result.x = a.x % b.x;
        result.y = a.y % b.y;
        return result;
    }

    public synchronized void mod(int value) {
        this.x %= value;
        this.y %= value;
    }

    public static synchronized Int2 mod(Int2 a, int b) {
        Int2 result = new Int2();
        result.x = a.x % b;
        result.y = a.y % b;
        return result;
    }

    public synchronized int length() {
        return 2;
    }

    public synchronized void negate() {
        this.x = -this.x;
        this.y = -this.y;
    }

    public synchronized int dotProduct(Int2 a) {
        return (this.x * a.x) + (this.y * a.y);
    }

    public static synchronized int dotProduct(Int2 a, Int2 b) {
        return (b.x * a.x) + (b.y * a.y);
    }

    public synchronized void addMultiple(Int2 a, int factor) {
        this.x += a.x * factor;
        this.y += a.y * factor;
    }

    public synchronized void set(Int2 a) {
        this.x = a.x;
        this.y = a.y;
    }

    public synchronized void setValues(int a, int b) {
        this.x = a;
        this.y = b;
    }

    public synchronized int elementSum() {
        return this.x + this.y;
    }

    public synchronized int get(int i) {
        switch (i) {
            case 0:
                return this.x;
            case 1:
                return this.y;
            default:
                throw new IndexOutOfBoundsException("Index: i");
        }
    }

    public synchronized void setAt(int i, int value) {
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

    public synchronized void addAt(int i, int value) {
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

    public synchronized void copyTo(int[] data, int offset) {
        data[offset] = this.x;
        data[offset + 1] = this.y;
    }
}
