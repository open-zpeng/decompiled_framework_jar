package android.text.style;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Parcel;
import android.text.Layout;
import android.text.ParcelableSpan;
import android.text.Spanned;
/* loaded from: classes2.dex */
public class BulletSpan implements LeadingMarginSpan, ParcelableSpan {
    private static final int STANDARD_BULLET_RADIUS = 4;
    private static final int STANDARD_COLOR = 0;
    public static final int STANDARD_GAP_WIDTH = 2;
    private Path mBulletPath;
    private final int mBulletRadius;
    public protected final int mColor;
    public protected final int mGapWidth;
    public protected final boolean mWantColor;

    public BulletSpan() {
        this(2, 0, false, 4);
    }

    public BulletSpan(int gapWidth) {
        this(gapWidth, 0, false, 4);
    }

    public BulletSpan(int gapWidth, int color) {
        this(gapWidth, color, true, 4);
    }

    public BulletSpan(int gapWidth, int color, int bulletRadius) {
        this(gapWidth, color, true, bulletRadius);
    }

    private synchronized BulletSpan(int gapWidth, int color, boolean wantColor, int bulletRadius) {
        this.mBulletPath = null;
        this.mGapWidth = gapWidth;
        this.mBulletRadius = bulletRadius;
        this.mColor = color;
        this.mWantColor = wantColor;
    }

    public BulletSpan(Parcel src) {
        this.mBulletPath = null;
        this.mGapWidth = src.readInt();
        this.mWantColor = src.readInt() != 0;
        this.mColor = src.readInt();
        this.mBulletRadius = src.readInt();
    }

    @Override // android.text.ParcelableSpan
    public int getSpanTypeId() {
        return getSpanTypeIdInternal();
    }

    @Override // android.text.ParcelableSpan
    public synchronized int getSpanTypeIdInternal() {
        return 8;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        writeToParcelInternal(dest, flags);
    }

    @Override // android.text.ParcelableSpan
    public synchronized void writeToParcelInternal(Parcel dest, int flags) {
        dest.writeInt(this.mGapWidth);
        dest.writeInt(this.mWantColor ? 1 : 0);
        dest.writeInt(this.mColor);
        dest.writeInt(this.mBulletRadius);
    }

    @Override // android.text.style.LeadingMarginSpan
    public int getLeadingMargin(boolean first) {
        return (2 * this.mBulletRadius) + this.mGapWidth;
    }

    public int getGapWidth() {
        return this.mGapWidth;
    }

    public int getBulletRadius() {
        return this.mBulletRadius;
    }

    public int getColor() {
        return this.mColor;
    }

    @Override // android.text.style.LeadingMarginSpan
    public void drawLeadingMargin(Canvas canvas, Paint paint, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        int line;
        if (((Spanned) text).getSpanStart(this) == start) {
            Paint.Style style = paint.getStyle();
            int oldcolor = 0;
            if (this.mWantColor) {
                oldcolor = paint.getColor();
                paint.setColor(this.mColor);
            }
            paint.setStyle(Paint.Style.FILL);
            if (layout != null) {
                int line2 = layout.getLineForOffset(start);
                line = bottom - layout.getLineExtra(line2);
            } else {
                line = bottom;
            }
            float yPosition = (top + line) / 2.0f;
            float xPosition = x + (this.mBulletRadius * dir);
            if (canvas.isHardwareAccelerated()) {
                if (this.mBulletPath == null) {
                    this.mBulletPath = new Path();
                    this.mBulletPath.addCircle(0.0f, 0.0f, this.mBulletRadius, Path.Direction.CW);
                }
                canvas.save();
                canvas.translate(xPosition, yPosition);
                canvas.drawPath(this.mBulletPath, paint);
                canvas.restore();
            } else {
                canvas.drawCircle(xPosition, yPosition, this.mBulletRadius, paint);
            }
            if (this.mWantColor) {
                paint.setColor(oldcolor);
            }
            paint.setStyle(style);
        }
    }
}
