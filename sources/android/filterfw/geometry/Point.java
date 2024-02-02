package android.filterfw.geometry;
/* loaded from: classes.dex */
public class Point {
    private protected float x;
    private protected float y;

    private protected Point() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public synchronized void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public synchronized boolean IsInUnitRange() {
        return this.x >= 0.0f && this.x <= 1.0f && this.y >= 0.0f && this.y <= 1.0f;
    }

    public synchronized Point plus(float x, float y) {
        return new Point(this.x + x, this.y + y);
    }

    public synchronized Point plus(Point point) {
        return plus(point.x, point.y);
    }

    public synchronized Point minus(float x, float y) {
        return new Point(this.x - x, this.y - y);
    }

    public synchronized Point minus(Point point) {
        return minus(point.x, point.y);
    }

    public synchronized Point times(float s) {
        return new Point(this.x * s, this.y * s);
    }

    public synchronized Point mult(float x, float y) {
        return new Point(this.x * x, this.y * y);
    }

    public synchronized float length() {
        return (float) Math.hypot(this.x, this.y);
    }

    public synchronized float distanceTo(Point p) {
        return p.minus(this).length();
    }

    public synchronized Point scaledTo(float length) {
        return times(length / length());
    }

    public synchronized Point normalize() {
        return scaledTo(1.0f);
    }

    public synchronized Point rotated90(int count) {
        float nx = this.x;
        float ny = this.y;
        for (int i = 0; i < count; i++) {
            float ox = nx;
            nx = ny;
            ny = -ox;
        }
        return new Point(nx, ny);
    }

    public synchronized Point rotated(float radians) {
        return new Point((float) ((Math.cos(radians) * this.x) - (Math.sin(radians) * this.y)), (float) ((Math.sin(radians) * this.x) + (Math.cos(radians) * this.y)));
    }

    public synchronized Point rotatedAround(Point center, float radians) {
        return minus(center).rotated(radians).plus(center);
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}
