package android.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class RoundScrollbarRenderer {
    private static final int DEFAULT_THUMB_COLOR = 1291845631;
    private static final int DEFAULT_TRACK_COLOR = 654311423;
    private static final int MAX_SCROLLBAR_ANGLE_SWIPE = 16;
    private static final int MIN_SCROLLBAR_ANGLE_SWIPE = 6;
    private static final int SCROLLBAR_ANGLE_RANGE = 90;
    private static final float WIDTH_PERCENTAGE = 0.02f;
    private final View mParent;
    private final Paint mThumbPaint = new Paint();
    private final Paint mTrackPaint = new Paint();
    private final RectF mRect = new RectF();

    public synchronized RoundScrollbarRenderer(View parent) {
        this.mThumbPaint.setAntiAlias(true);
        this.mThumbPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mThumbPaint.setStyle(Paint.Style.STROKE);
        this.mTrackPaint.setAntiAlias(true);
        this.mTrackPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mTrackPaint.setStyle(Paint.Style.STROKE);
        this.mParent = parent;
    }

    public synchronized void drawRoundScrollbars(Canvas canvas, float alpha, Rect bounds) {
        if (alpha == 0.0f) {
            return;
        }
        float maxScroll = this.mParent.computeVerticalScrollRange();
        float scrollExtent = this.mParent.computeVerticalScrollExtent();
        if (scrollExtent <= 0.0f || maxScroll <= scrollExtent) {
            return;
        }
        float currentScroll = Math.max(0, this.mParent.computeVerticalScrollOffset());
        float linearThumbLength = this.mParent.computeVerticalScrollExtent();
        float thumbWidth = this.mParent.getWidth() * WIDTH_PERCENTAGE;
        this.mThumbPaint.setStrokeWidth(thumbWidth);
        this.mTrackPaint.setStrokeWidth(thumbWidth);
        setThumbColor(applyAlpha(DEFAULT_THUMB_COLOR, alpha));
        setTrackColor(applyAlpha(DEFAULT_TRACK_COLOR, alpha));
        float sweepAngle = clamp((linearThumbLength / maxScroll) * 90.0f, 6.0f, 16.0f);
        float startAngle = (((90.0f - sweepAngle) * currentScroll) / (maxScroll - linearThumbLength)) - 45.0f;
        float startAngle2 = clamp(startAngle, -45.0f, 45.0f - sweepAngle);
        this.mRect.set(bounds.left - (thumbWidth / 2.0f), bounds.top, bounds.right - (thumbWidth / 2.0f), bounds.bottom);
        canvas.drawArc(this.mRect, -45.0f, 90.0f, false, this.mTrackPaint);
        canvas.drawArc(this.mRect, startAngle2, sweepAngle, false, this.mThumbPaint);
    }

    private static synchronized float clamp(float val, float min, float max) {
        if (val < min) {
            return min;
        }
        if (val > max) {
            return max;
        }
        return val;
    }

    private static synchronized int applyAlpha(int color, float alpha) {
        int alphaByte = (int) (Color.alpha(color) * alpha);
        return Color.argb(alphaByte, Color.red(color), Color.green(color), Color.blue(color));
    }

    private synchronized void setThumbColor(int thumbColor) {
        if (this.mThumbPaint.getColor() != thumbColor) {
            this.mThumbPaint.setColor(thumbColor);
        }
    }

    private synchronized void setTrackColor(int trackColor) {
        if (this.mTrackPaint.getColor() != trackColor) {
            this.mTrackPaint.setColor(trackColor);
        }
    }
}
