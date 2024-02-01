package android.renderscript;
/* loaded from: classes2.dex */
public class Short3 {
    public short x;
    public short y;
    public short z;

    public Short3() {
    }

    public synchronized Short3(short i) {
        this.z = i;
        this.y = i;
        this.x = i;
    }

    public Short3(short x, short y, short z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public synchronized Short3(Short3 source) {
        this.x = source.x;
        this.y = source.y;
        this.z = source.z;
    }

    public synchronized void add(Short3 a) {
        this.x = (short) (this.x + a.x);
        this.y = (short) (this.y + a.y);
        this.z = (short) (this.z + a.z);
    }

    public static synchronized Short3 add(Short3 a, Short3 b) {
        Short3 result = new Short3();
        result.x = (short) (a.x + b.x);
        result.y = (short) (a.y + b.y);
        result.z = (short) (a.z + b.z);
        return result;
    }

    public synchronized void add(short value) {
        this.x = (short) (this.x + value);
        this.y = (short) (this.y + value);
        this.z = (short) (this.z + value);
    }

    public static synchronized Short3 add(Short3 a, short b) {
        Short3 result = new Short3();
        result.x = (short) (a.x + b);
        result.y = (short) (a.y + b);
        result.z = (short) (a.z + b);
        return result;
    }

    public synchronized void sub(Short3 a) {
        this.x = (short) (this.x - a.x);
        this.y = (short) (this.y - a.y);
        this.z = (short) (this.z - a.z);
    }

    public static synchronized Short3 sub(Short3 a, Short3 b) {
        Short3 result = new Short3();
        result.x = (short) (a.x - b.x);
        result.y = (short) (a.y - b.y);
        result.z = (short) (a.z - b.z);
        return result;
    }

    public synchronized void sub(short value) {
        this.x = (short) (this.x - value);
        this.y = (short) (this.y - value);
        this.z = (short) (this.z - value);
    }

    public static synchronized Short3 sub(Short3 a, short b) {
        Short3 result = new Short3();
        result.x = (short) (a.x - b);
        result.y = (short) (a.y - b);
        result.z = (short) (a.z - b);
        return result;
    }

    public synchronized void mul(Short3 a) {
        this.x = (short) (this.x * a.x);
        this.y = (short) (this.y * a.y);
        this.z = (short) (this.z * a.z);
    }

    public static synchronized Short3 mul(Short3 a, Short3 b) {
        Short3 result = new Short3();
        result.x = (short) (a.x * b.x);
        result.y = (short) (a.y * b.y);
        result.z = (short) (a.z * b.z);
        return result;
    }

    public synchronized void mul(short value) {
        this.x = (short) (this.x * value);
        this.y = (short) (this.y * value);
        this.z = (short) (this.z * value);
    }

    public static synchronized Short3 mul(Short3 a, short b) {
        Short3 result = new Short3();
        result.x = (short) (a.x * b);
        result.y = (short) (a.y * b);
        result.z = (short) (a.z * b);
        return result;
    }

    public synchronized void div(Short3 a) {
        this.x = (short) (this.x / a.x);
        this.y = (short) (this.y / a.y);
        this.z = (short) (this.z / a.z);
    }

    public static synchronized Short3 div(Short3 a, Short3 b) {
        Short3 result = new Short3();
        result.x = (short) (a.x / b.x);
        result.y = (short) (a.y / b.y);
        result.z = (short) (a.z / b.z);
        return result;
    }

    public synchronized void div(short value) {
        this.x = (short) (this.x / value);
        this.y = (short) (this.y / value);
        this.z = (short) (this.z / value);
    }

    public static synchronized Short3 div(Short3 a, short b) {
        Short3 result = new Short3();
        result.x = (short) (a.x / b);
        result.y = (short) (a.y / b);
        result.z = (short) (a.z / b);
        return result;
    }

    public synchronized void mod(Short3 a) {
        this.x = (short) (this.x % a.x);
        this.y = (short) (this.y % a.y);
        this.z = (short) (this.z % a.z);
    }

    public static synchronized Short3 mod(Short3 a, Short3 b) {
        Short3 result = new Short3();
        result.x = (short) (a.x % b.x);
        result.y = (short) (a.y % b.y);
        result.z = (short) (a.z % b.z);
        return result;
    }

    public synchronized void mod(short value) {
        this.x = (short) (this.x % value);
        this.y = (short) (this.y % value);
        this.z = (short) (this.z % value);
    }

    public static synchronized Short3 mod(Short3 a, short b) {
        Short3 result = new Short3();
        result.x = (short) (a.x % b);
        result.y = (short) (a.y % b);
        result.z = (short) (a.z % b);
        return result;
    }

    public synchronized short length() {
        return (short) 3;
    }

    public synchronized void negate() {
        this.x = (short) (-this.x);
        this.y = (short) (-this.y);
        this.z = (short) (-this.z);
    }

    public synchronized short dotProduct(Short3 a) {
        return (short) ((this.x * a.x) + (this.y * a.y) + (this.z * a.z));
    }

    public static synchronized short dotProduct(Short3 a, Short3 b) {
        return (short) ((b.x * a.x) + (b.y * a.y) + (b.z * a.z));
    }

    public synchronized void addMultiple(Short3 a, short factor) {
        this.x = (short) (this.x + (a.x * factor));
        this.y = (short) (this.y + (a.y * factor));
        this.z = (short) (this.z + (a.z * factor));
    }

    public synchronized void set(Short3 a) {
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;
    }

    public synchronized void setValues(short a, short b, short c) {
        this.x = a;
        this.y = b;
        this.z = c;
    }

    public synchronized short elementSum() {
        return (short) (this.x + this.y + this.z);
    }

    public synchronized short get(int i) {
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

    public synchronized void setAt(int i, short value) {
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

    public synchronized void addAt(int i, short value) {
        switch (i) {
            case 0:
                this.x = (short) (this.x + value);
                return;
            case 1:
                this.y = (short) (this.y + value);
                return;
            case 2:
                this.z = (short) (this.z + value);
                return;
            default:
                throw new IndexOutOfBoundsException("Index: i");
        }
    }

    public synchronized void copyTo(short[] data, int offset) {
        data[offset] = this.x;
        data[offset + 1] = this.y;
        data[offset + 2] = this.z;
    }
}
