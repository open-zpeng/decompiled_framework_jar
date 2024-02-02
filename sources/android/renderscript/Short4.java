package android.renderscript;
/* loaded from: classes2.dex */
public class Short4 {
    public short w;
    public short x;
    public short y;
    public short z;

    public Short4() {
    }

    public synchronized Short4(short i) {
        this.w = i;
        this.z = i;
        this.y = i;
        this.x = i;
    }

    public Short4(short x, short y, short z, short w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public synchronized Short4(Short4 source) {
        this.x = source.x;
        this.y = source.y;
        this.z = source.z;
        this.w = source.w;
    }

    public synchronized void add(Short4 a) {
        this.x = (short) (this.x + a.x);
        this.y = (short) (this.y + a.y);
        this.z = (short) (this.z + a.z);
        this.w = (short) (this.w + a.w);
    }

    public static synchronized Short4 add(Short4 a, Short4 b) {
        Short4 result = new Short4();
        result.x = (short) (a.x + b.x);
        result.y = (short) (a.y + b.y);
        result.z = (short) (a.z + b.z);
        result.w = (short) (a.w + b.w);
        return result;
    }

    public synchronized void add(short value) {
        this.x = (short) (this.x + value);
        this.y = (short) (this.y + value);
        this.z = (short) (this.z + value);
        this.w = (short) (this.w + value);
    }

    public static synchronized Short4 add(Short4 a, short b) {
        Short4 result = new Short4();
        result.x = (short) (a.x + b);
        result.y = (short) (a.y + b);
        result.z = (short) (a.z + b);
        result.w = (short) (a.w + b);
        return result;
    }

    public synchronized void sub(Short4 a) {
        this.x = (short) (this.x - a.x);
        this.y = (short) (this.y - a.y);
        this.z = (short) (this.z - a.z);
        this.w = (short) (this.w - a.w);
    }

    public static synchronized Short4 sub(Short4 a, Short4 b) {
        Short4 result = new Short4();
        result.x = (short) (a.x - b.x);
        result.y = (short) (a.y - b.y);
        result.z = (short) (a.z - b.z);
        result.w = (short) (a.w - b.w);
        return result;
    }

    public synchronized void sub(short value) {
        this.x = (short) (this.x - value);
        this.y = (short) (this.y - value);
        this.z = (short) (this.z - value);
        this.w = (short) (this.w - value);
    }

    public static synchronized Short4 sub(Short4 a, short b) {
        Short4 result = new Short4();
        result.x = (short) (a.x - b);
        result.y = (short) (a.y - b);
        result.z = (short) (a.z - b);
        result.w = (short) (a.w - b);
        return result;
    }

    public synchronized void mul(Short4 a) {
        this.x = (short) (this.x * a.x);
        this.y = (short) (this.y * a.y);
        this.z = (short) (this.z * a.z);
        this.w = (short) (this.w * a.w);
    }

    public static synchronized Short4 mul(Short4 a, Short4 b) {
        Short4 result = new Short4();
        result.x = (short) (a.x * b.x);
        result.y = (short) (a.y * b.y);
        result.z = (short) (a.z * b.z);
        result.w = (short) (a.w * b.w);
        return result;
    }

    public synchronized void mul(short value) {
        this.x = (short) (this.x * value);
        this.y = (short) (this.y * value);
        this.z = (short) (this.z * value);
        this.w = (short) (this.w * value);
    }

    public static synchronized Short4 mul(Short4 a, short b) {
        Short4 result = new Short4();
        result.x = (short) (a.x * b);
        result.y = (short) (a.y * b);
        result.z = (short) (a.z * b);
        result.w = (short) (a.w * b);
        return result;
    }

    public synchronized void div(Short4 a) {
        this.x = (short) (this.x / a.x);
        this.y = (short) (this.y / a.y);
        this.z = (short) (this.z / a.z);
        this.w = (short) (this.w / a.w);
    }

    public static synchronized Short4 div(Short4 a, Short4 b) {
        Short4 result = new Short4();
        result.x = (short) (a.x / b.x);
        result.y = (short) (a.y / b.y);
        result.z = (short) (a.z / b.z);
        result.w = (short) (a.w / b.w);
        return result;
    }

    public synchronized void div(short value) {
        this.x = (short) (this.x / value);
        this.y = (short) (this.y / value);
        this.z = (short) (this.z / value);
        this.w = (short) (this.w / value);
    }

    public static synchronized Short4 div(Short4 a, short b) {
        Short4 result = new Short4();
        result.x = (short) (a.x / b);
        result.y = (short) (a.y / b);
        result.z = (short) (a.z / b);
        result.w = (short) (a.w / b);
        return result;
    }

    public synchronized void mod(Short4 a) {
        this.x = (short) (this.x % a.x);
        this.y = (short) (this.y % a.y);
        this.z = (short) (this.z % a.z);
        this.w = (short) (this.w % a.w);
    }

    public static synchronized Short4 mod(Short4 a, Short4 b) {
        Short4 result = new Short4();
        result.x = (short) (a.x % b.x);
        result.y = (short) (a.y % b.y);
        result.z = (short) (a.z % b.z);
        result.w = (short) (a.w % b.w);
        return result;
    }

    public synchronized void mod(short value) {
        this.x = (short) (this.x % value);
        this.y = (short) (this.y % value);
        this.z = (short) (this.z % value);
        this.w = (short) (this.w % value);
    }

    public static synchronized Short4 mod(Short4 a, short b) {
        Short4 result = new Short4();
        result.x = (short) (a.x % b);
        result.y = (short) (a.y % b);
        result.z = (short) (a.z % b);
        result.w = (short) (a.w % b);
        return result;
    }

    public synchronized short length() {
        return (short) 4;
    }

    public synchronized void negate() {
        this.x = (short) (-this.x);
        this.y = (short) (-this.y);
        this.z = (short) (-this.z);
        this.w = (short) (-this.w);
    }

    public synchronized short dotProduct(Short4 a) {
        return (short) ((this.x * a.x) + (this.y * a.y) + (this.z * a.z) + (this.w * a.w));
    }

    public static synchronized short dotProduct(Short4 a, Short4 b) {
        return (short) ((b.x * a.x) + (b.y * a.y) + (b.z * a.z) + (b.w * a.w));
    }

    public synchronized void addMultiple(Short4 a, short factor) {
        this.x = (short) (this.x + (a.x * factor));
        this.y = (short) (this.y + (a.y * factor));
        this.z = (short) (this.z + (a.z * factor));
        this.w = (short) (this.w + (a.w * factor));
    }

    public synchronized void set(Short4 a) {
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;
        this.w = a.w;
    }

    public synchronized void setValues(short a, short b, short c, short d) {
        this.x = a;
        this.y = b;
        this.z = c;
        this.w = d;
    }

    public synchronized short elementSum() {
        return (short) (this.x + this.y + this.z + this.w);
    }

    public synchronized short get(int i) {
        switch (i) {
            case 0:
                return this.x;
            case 1:
                return this.y;
            case 2:
                return this.z;
            case 3:
                return this.w;
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
            case 3:
                this.w = value;
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
            case 3:
                this.w = (short) (this.w + value);
                return;
            default:
                throw new IndexOutOfBoundsException("Index: i");
        }
    }

    public synchronized void copyTo(short[] data, int offset) {
        data[offset] = this.x;
        data[offset + 1] = this.y;
        data[offset + 2] = this.z;
        data[offset + 3] = this.w;
    }
}
