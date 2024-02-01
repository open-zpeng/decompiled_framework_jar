package android.renderscript;
/* loaded from: classes2.dex */
public class Float2 {
    public float x;
    public float y;

    public Float2() {
    }

    public synchronized Float2(Float2 data) {
        this.x = data.x;
        this.y = data.y;
    }

    public Float2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static synchronized Float2 add(Float2 a, Float2 b) {
        Float2 res = new Float2();
        res.x = a.x + b.x;
        res.y = a.y + b.y;
        return res;
    }

    public synchronized void add(Float2 value) {
        this.x += value.x;
        this.y += value.y;
    }

    public synchronized void add(float value) {
        this.x += value;
        this.y += value;
    }

    public static synchronized Float2 add(Float2 a, float b) {
        Float2 res = new Float2();
        res.x = a.x + b;
        res.y = a.y + b;
        return res;
    }

    public synchronized void sub(Float2 value) {
        this.x -= value.x;
        this.y -= value.y;
    }

    public static synchronized Float2 sub(Float2 a, Float2 b) {
        Float2 res = new Float2();
        res.x = a.x - b.x;
        res.y = a.y - b.y;
        return res;
    }

    public synchronized void sub(float value) {
        this.x -= value;
        this.y -= value;
    }

    public static synchronized Float2 sub(Float2 a, float b) {
        Float2 res = new Float2();
        res.x = a.x - b;
        res.y = a.y - b;
        return res;
    }

    public synchronized void mul(Float2 value) {
        this.x *= value.x;
        this.y *= value.y;
    }

    public static synchronized Float2 mul(Float2 a, Float2 b) {
        Float2 res = new Float2();
        res.x = a.x * b.x;
        res.y = a.y * b.y;
        return res;
    }

    public synchronized void mul(float value) {
        this.x *= value;
        this.y *= value;
    }

    public static synchronized Float2 mul(Float2 a, float b) {
        Float2 res = new Float2();
        res.x = a.x * b;
        res.y = a.y * b;
        return res;
    }

    public synchronized void div(Float2 value) {
        this.x /= value.x;
        this.y /= value.y;
    }

    public static synchronized Float2 div(Float2 a, Float2 b) {
        Float2 res = new Float2();
        res.x = a.x / b.x;
        res.y = a.y / b.y;
        return res;
    }

    public synchronized void div(float value) {
        this.x /= value;
        this.y /= value;
    }

    public static synchronized Float2 div(Float2 a, float b) {
        Float2 res = new Float2();
        res.x = a.x / b;
        res.y = a.y / b;
        return res;
    }

    public synchronized float dotProduct(Float2 a) {
        return (this.x * a.x) + (this.y * a.y);
    }

    public static synchronized float dotProduct(Float2 a, Float2 b) {
        return (b.x * a.x) + (b.y * a.y);
    }

    public synchronized void addMultiple(Float2 a, float factor) {
        this.x += a.x * factor;
        this.y += a.y * factor;
    }

    public synchronized void set(Float2 a) {
        this.x = a.x;
        this.y = a.y;
    }

    public synchronized void negate() {
        this.x = -this.x;
        this.y = -this.y;
    }

    public synchronized int length() {
        return 2;
    }

    public synchronized float elementSum() {
        return this.x + this.y;
    }

    public synchronized float get(int i) {
        switch (i) {
            case 0:
                return this.x;
            case 1:
                return this.y;
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
            default:
                throw new IndexOutOfBoundsException("Index: i");
        }
    }

    public synchronized void setValues(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public synchronized void copyTo(float[] data, int offset) {
        data[offset] = this.x;
        data[offset + 1] = this.y;
    }
}
