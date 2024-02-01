package android.renderscript;
/* loaded from: classes2.dex */
public class Int3 {
    public int x;
    public int y;
    public int z;

    public Int3() {
    }

    public synchronized Int3(int i) {
        this.z = i;
        this.y = i;
        this.x = i;
    }

    public Int3(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public synchronized Int3(Int3 source) {
        this.x = source.x;
        this.y = source.y;
        this.z = source.z;
    }

    public synchronized void add(Int3 a) {
        this.x += a.x;
        this.y += a.y;
        this.z += a.z;
    }

    public static synchronized Int3 add(Int3 a, Int3 b) {
        Int3 result = new Int3();
        result.x = a.x + b.x;
        result.y = a.y + b.y;
        result.z = a.z + b.z;
        return result;
    }

    public synchronized void add(int value) {
        this.x += value;
        this.y += value;
        this.z += value;
    }

    public static synchronized Int3 add(Int3 a, int b) {
        Int3 result = new Int3();
        result.x = a.x + b;
        result.y = a.y + b;
        result.z = a.z + b;
        return result;
    }

    public synchronized void sub(Int3 a) {
        this.x -= a.x;
        this.y -= a.y;
        this.z -= a.z;
    }

    public static synchronized Int3 sub(Int3 a, Int3 b) {
        Int3 result = new Int3();
        result.x = a.x - b.x;
        result.y = a.y - b.y;
        result.z = a.z - b.z;
        return result;
    }

    public synchronized void sub(int value) {
        this.x -= value;
        this.y -= value;
        this.z -= value;
    }

    public static synchronized Int3 sub(Int3 a, int b) {
        Int3 result = new Int3();
        result.x = a.x - b;
        result.y = a.y - b;
        result.z = a.z - b;
        return result;
    }

    public synchronized void mul(Int3 a) {
        this.x *= a.x;
        this.y *= a.y;
        this.z *= a.z;
    }

    public static synchronized Int3 mul(Int3 a, Int3 b) {
        Int3 result = new Int3();
        result.x = a.x * b.x;
        result.y = a.y * b.y;
        result.z = a.z * b.z;
        return result;
    }

    public synchronized void mul(int value) {
        this.x *= value;
        this.y *= value;
        this.z *= value;
    }

    public static synchronized Int3 mul(Int3 a, int b) {
        Int3 result = new Int3();
        result.x = a.x * b;
        result.y = a.y * b;
        result.z = a.z * b;
        return result;
    }

    public synchronized void div(Int3 a) {
        this.x /= a.x;
        this.y /= a.y;
        this.z /= a.z;
    }

    public static synchronized Int3 div(Int3 a, Int3 b) {
        Int3 result = new Int3();
        result.x = a.x / b.x;
        result.y = a.y / b.y;
        result.z = a.z / b.z;
        return result;
    }

    public synchronized void div(int value) {
        this.x /= value;
        this.y /= value;
        this.z /= value;
    }

    public static synchronized Int3 div(Int3 a, int b) {
        Int3 result = new Int3();
        result.x = a.x / b;
        result.y = a.y / b;
        result.z = a.z / b;
        return result;
    }

    public synchronized void mod(Int3 a) {
        this.x %= a.x;
        this.y %= a.y;
        this.z %= a.z;
    }

    public static synchronized Int3 mod(Int3 a, Int3 b) {
        Int3 result = new Int3();
        result.x = a.x % b.x;
        result.y = a.y % b.y;
        result.z = a.z % b.z;
        return result;
    }

    public synchronized void mod(int value) {
        this.x %= value;
        this.y %= value;
        this.z %= value;
    }

    public static synchronized Int3 mod(Int3 a, int b) {
        Int3 result = new Int3();
        result.x = a.x % b;
        result.y = a.y % b;
        result.z = a.z % b;
        return result;
    }

    public synchronized int length() {
        return 3;
    }

    public synchronized void negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
    }

    public synchronized int dotProduct(Int3 a) {
        return (this.x * a.x) + (this.y * a.y) + (this.z * a.z);
    }

    public static synchronized int dotProduct(Int3 a, Int3 b) {
        return (b.x * a.x) + (b.y * a.y) + (b.z * a.z);
    }

    public synchronized void addMultiple(Int3 a, int factor) {
        this.x += a.x * factor;
        this.y += a.y * factor;
        this.z += a.z * factor;
    }

    public synchronized void set(Int3 a) {
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;
    }

    public synchronized void setValues(int a, int b, int c) {
        this.x = a;
        this.y = b;
        this.z = c;
    }

    public synchronized int elementSum() {
        return this.x + this.y + this.z;
    }

    public synchronized int get(int i) {
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

    public synchronized void setAt(int i, int value) {
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

    public synchronized void addAt(int i, int value) {
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

    public synchronized void copyTo(int[] data, int offset) {
        data[offset] = this.x;
        data[offset + 1] = this.y;
        data[offset + 2] = this.z;
    }
}
