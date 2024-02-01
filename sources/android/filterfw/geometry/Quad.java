package android.filterfw.geometry;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public class Quad {
    private protected Point p0;
    private protected Point p1;
    private protected Point p2;
    private protected Point p3;

    /* JADX INFO: Access modifiers changed from: private */
    public Quad() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Quad(Point p0, Point p1, Point p2, Point p3) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public synchronized boolean IsInUnitRange() {
        return this.p0.IsInUnitRange() && this.p1.IsInUnitRange() && this.p2.IsInUnitRange() && this.p3.IsInUnitRange();
    }

    public synchronized Quad translated(Point t) {
        return new Quad(this.p0.plus(t), this.p1.plus(t), this.p2.plus(t), this.p3.plus(t));
    }

    public synchronized Quad translated(float x, float y) {
        return new Quad(this.p0.plus(x, y), this.p1.plus(x, y), this.p2.plus(x, y), this.p3.plus(x, y));
    }

    public synchronized Quad scaled(float s) {
        return new Quad(this.p0.times(s), this.p1.times(s), this.p2.times(s), this.p3.times(s));
    }

    public synchronized Quad scaled(float x, float y) {
        return new Quad(this.p0.mult(x, y), this.p1.mult(x, y), this.p2.mult(x, y), this.p3.mult(x, y));
    }

    public synchronized Rectangle boundingBox() {
        List<Float> xs = Arrays.asList(Float.valueOf(this.p0.x), Float.valueOf(this.p1.x), Float.valueOf(this.p2.x), Float.valueOf(this.p3.x));
        List<Float> ys = Arrays.asList(Float.valueOf(this.p0.y), Float.valueOf(this.p1.y), Float.valueOf(this.p2.y), Float.valueOf(this.p3.y));
        float x0 = ((Float) Collections.min(xs)).floatValue();
        float y0 = ((Float) Collections.min(ys)).floatValue();
        float x1 = ((Float) Collections.max(xs)).floatValue();
        float y1 = ((Float) Collections.max(ys)).floatValue();
        return new Rectangle(x0, y0, x1 - x0, y1 - y0);
    }

    public synchronized float getBoundingWidth() {
        List<Float> xs = Arrays.asList(Float.valueOf(this.p0.x), Float.valueOf(this.p1.x), Float.valueOf(this.p2.x), Float.valueOf(this.p3.x));
        return ((Float) Collections.max(xs)).floatValue() - ((Float) Collections.min(xs)).floatValue();
    }

    public synchronized float getBoundingHeight() {
        List<Float> ys = Arrays.asList(Float.valueOf(this.p0.y), Float.valueOf(this.p1.y), Float.valueOf(this.p2.y), Float.valueOf(this.p3.y));
        return ((Float) Collections.max(ys)).floatValue() - ((Float) Collections.min(ys)).floatValue();
    }

    public String toString() {
        return "{" + this.p0 + ", " + this.p1 + ", " + this.p2 + ", " + this.p3 + "}";
    }
}
