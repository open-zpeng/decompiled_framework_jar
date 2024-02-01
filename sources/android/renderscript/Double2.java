package android.renderscript;
/* loaded from: classes2.dex */
public class Double2 {
    public double x;
    public double y;

    public Double2() {
    }

    public synchronized Double2(Double2 data) {
        this.x = data.x;
        this.y = data.y;
    }

    public Double2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static synchronized Double2 add(Double2 a, Double2 b) {
        Double2 res = new Double2();
        res.x = a.x + b.x;
        res.y = a.y + b.y;
        return res;
    }

    public synchronized void add(Double2 value) {
        this.x += value.x;
        this.y += value.y;
    }

    public synchronized void add(double value) {
        this.x += value;
        this.y += value;
    }

    public static synchronized Double2 add(Double2 a, double b) {
        Double2 res = new Double2();
        res.x = a.x + b;
        res.y = a.y + b;
        return res;
    }

    public synchronized void sub(Double2 value) {
        this.x -= value.x;
        this.y -= value.y;
    }

    public static synchronized Double2 sub(Double2 a, Double2 b) {
        Double2 res = new Double2();
        res.x = a.x - b.x;
        res.y = a.y - b.y;
        return res;
    }

    public synchronized void sub(double value) {
        this.x -= value;
        this.y -= value;
    }

    public static synchronized Double2 sub(Double2 a, double b) {
        Double2 res = new Double2();
        res.x = a.x - b;
        res.y = a.y - b;
        return res;
    }

    public synchronized void mul(Double2 value) {
        this.x *= value.x;
        this.y *= value.y;
    }

    public static synchronized Double2 mul(Double2 a, Double2 b) {
        Double2 res = new Double2();
        res.x = a.x * b.x;
        res.y = a.y * b.y;
        return res;
    }

    public synchronized void mul(double value) {
        this.x *= value;
        this.y *= value;
    }

    public static synchronized Double2 mul(Double2 a, double b) {
        Double2 res = new Double2();
        res.x = a.x * b;
        res.y = a.y * b;
        return res;
    }

    public synchronized void div(Double2 value) {
        this.x /= value.x;
        this.y /= value.y;
    }

    public static synchronized Double2 div(Double2 a, Double2 b) {
        Double2 res = new Double2();
        res.x = a.x / b.x;
        res.y = a.y / b.y;
        return res;
    }

    public synchronized void div(double value) {
        this.x /= value;
        this.y /= value;
    }

    public static synchronized Double2 div(Double2 a, double b) {
        Double2 res = new Double2();
        res.x = a.x / b;
        res.y = a.y / b;
        return res;
    }

    public synchronized double dotProduct(Double2 a) {
        return (this.x * a.x) + (this.y * a.y);
    }

    public static synchronized Double dotProduct(Double2 a, Double2 b) {
        return Double.valueOf((b.x * a.x) + (b.y * a.y));
    }

    public synchronized void addMultiple(Double2 a, double factor) {
        this.x += a.x * factor;
        this.y += a.y * factor;
    }

    public synchronized void set(Double2 a) {
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

    public synchronized double elementSum() {
        return this.x + this.y;
    }

    public synchronized double get(int i) {
        switch (i) {
            case 0:
                return this.x;
            case 1:
                return this.y;
            default:
                throw new IndexOutOfBoundsException("Index: i");
        }
    }

    public synchronized void setAt(int i, double value) {
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

    public synchronized void addAt(int i, double value) {
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

    public synchronized void setValues(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public synchronized void copyTo(double[] data, int offset) {
        data[offset] = this.x;
        data[offset + 1] = this.y;
    }
}
