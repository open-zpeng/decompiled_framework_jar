package android.renderscript;
/* loaded from: classes2.dex */
public class Float4 {
    public float w;
    public float x;
    public float y;
    public float z;

    public Float4() {
    }

    public synchronized Float4(Float4 data) {
        this.x = data.x;
        this.y = data.y;
        this.z = data.z;
        this.w = data.w;
    }

    public Float4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public static synchronized Float4 add(Float4 a, Float4 b) {
        Float4 res = new Float4();
        res.x = a.x + b.x;
        res.y = a.y + b.y;
        res.z = a.z + b.z;
        res.w = a.w + b.w;
        return res;
    }

    public synchronized void add(Float4 value) {
        this.x += value.x;
        this.y += value.y;
        this.z += value.z;
        this.w += value.w;
    }

    public synchronized void add(float value) {
        this.x += value;
        this.y += value;
        this.z += value;
        this.w += value;
    }

    public static synchronized Float4 add(Float4 a, float b) {
        Float4 res = new Float4();
        res.x = a.x + b;
        res.y = a.y + b;
        res.z = a.z + b;
        res.w = a.w + b;
        return res;
    }

    public synchronized void sub(Float4 value) {
        this.x -= value.x;
        this.y -= value.y;
        this.z -= value.z;
        this.w -= value.w;
    }

    public synchronized void sub(float value) {
        this.x -= value;
        this.y -= value;
        this.z -= value;
        this.w -= value;
    }

    public static synchronized Float4 sub(Float4 a, float b) {
        Float4 res = new Float4();
        res.x = a.x - b;
        res.y = a.y - b;
        res.z = a.z - b;
        res.w = a.w - b;
        return res;
    }

    public static synchronized Float4 sub(Float4 a, Float4 b) {
        Float4 res = new Float4();
        res.x = a.x - b.x;
        res.y = a.y - b.y;
        res.z = a.z - b.z;
        res.w = a.w - b.w;
        return res;
    }

    public synchronized void mul(Float4 value) {
        this.x *= value.x;
        this.y *= value.y;
        this.z *= value.z;
        this.w *= value.w;
    }

    public synchronized void mul(float value) {
        this.x *= value;
        this.y *= value;
        this.z *= value;
        this.w *= value;
    }

    public static synchronized Float4 mul(Float4 a, Float4 b) {
        Float4 res = new Float4();
        res.x = a.x * b.x;
        res.y = a.y * b.y;
        res.z = a.z * b.z;
        res.w = a.w * b.w;
        return res;
    }

    public static synchronized Float4 mul(Float4 a, float b) {
        Float4 res = new Float4();
        res.x = a.x * b;
        res.y = a.y * b;
        res.z = a.z * b;
        res.w = a.w * b;
        return res;
    }

    public synchronized void div(Float4 value) {
        this.x /= value.x;
        this.y /= value.y;
        this.z /= value.z;
        this.w /= value.w;
    }

    public synchronized void div(float value) {
        this.x /= value;
        this.y /= value;
        this.z /= value;
        this.w /= value;
    }

    public static synchronized Float4 div(Float4 a, float b) {
        Float4 res = new Float4();
        res.x = a.x / b;
        res.y = a.y / b;
        res.z = a.z / b;
        res.w = a.w / b;
        return res;
    }

    public static synchronized Float4 div(Float4 a, Float4 b) {
        Float4 res = new Float4();
        res.x = a.x / b.x;
        res.y = a.y / b.y;
        res.z = a.z / b.z;
        res.w = a.w / b.w;
        return res;
    }

    public synchronized float dotProduct(Float4 a) {
        return (this.x * a.x) + (this.y * a.y) + (this.z * a.z) + (this.w * a.w);
    }

    public static synchronized float dotProduct(Float4 a, Float4 b) {
        return (b.x * a.x) + (b.y * a.y) + (b.z * a.z) + (b.w * a.w);
    }

    public synchronized void addMultiple(Float4 a, float factor) {
        this.x += a.x * factor;
        this.y += a.y * factor;
        this.z += a.z * factor;
        this.w += a.w * factor;
    }

    public synchronized void set(Float4 a) {
        this.x = a.x;
        this.y = a.y;
        this.z = a.z;
        this.w = a.w;
    }

    public synchronized void negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        this.w = -this.w;
    }

    public synchronized int length() {
        return 4;
    }

    public synchronized float elementSum() {
        return this.x + this.y + this.z + this.w;
    }

    public synchronized float get(int i) {
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

    public synchronized void setAt(int i, float value) {
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

    public synchronized void addAt(int i, float value) {
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
            case 3:
                this.w += value;
                return;
            default:
                throw new IndexOutOfBoundsException("Index: i");
        }
    }

    public synchronized void setValues(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public synchronized void copyTo(float[] data, int offset) {
        data[offset] = this.x;
        data[offset + 1] = this.y;
        data[offset + 2] = this.z;
        data[offset + 3] = this.w;
    }
}
