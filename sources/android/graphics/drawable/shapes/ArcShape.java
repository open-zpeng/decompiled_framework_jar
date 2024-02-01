package android.graphics.drawable.shapes;

import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
/* loaded from: classes.dex */
public class ArcShape extends RectShape {
    private final float mStartAngle;
    private final float mSweepAngle;

    public ArcShape(float startAngle, float sweepAngle) {
        this.mStartAngle = startAngle;
        this.mSweepAngle = sweepAngle;
    }

    public final float getStartAngle() {
        return this.mStartAngle;
    }

    public final float getSweepAngle() {
        return this.mSweepAngle;
    }

    @Override // android.graphics.drawable.shapes.RectShape, android.graphics.drawable.shapes.Shape
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawArc(rect(), this.mStartAngle, this.mSweepAngle, true, paint);
    }

    @Override // android.graphics.drawable.shapes.RectShape, android.graphics.drawable.shapes.Shape
    public void getOutline(Outline outline) {
    }

    @Override // android.graphics.drawable.shapes.RectShape, android.graphics.drawable.shapes.Shape
    /* renamed from: clone */
    public ArcShape mo22clone() throws CloneNotSupportedException {
        return (ArcShape) super.mo22clone();
    }
}
