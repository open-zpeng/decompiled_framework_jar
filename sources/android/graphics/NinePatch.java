package android.graphics;
/* loaded from: classes.dex */
public class NinePatch {
    public protected final Bitmap mBitmap;
    private protected long mNativeChunk;
    private Paint mPaint;
    private String mSrcName;

    public static native boolean isNinePatchChunk(byte[] bArr);

    private static native void nativeFinalize(long j);

    private static native long nativeGetTransparentRegion(Bitmap bitmap, long j, Rect rect);

    private static native long validateNinePatchChunk(byte[] bArr);

    /* loaded from: classes.dex */
    public static class InsetStruct {
        public final Rect opticalRect;
        public final float outlineAlpha;
        public final float outlineRadius;
        public final Rect outlineRect;

        public private protected InsetStruct(int opticalLeft, int opticalTop, int opticalRight, int opticalBottom, int outlineLeft, int outlineTop, int outlineRight, int outlineBottom, float outlineRadius, int outlineAlpha, float decodeScale) {
            this.opticalRect = new Rect(opticalLeft, opticalTop, opticalRight, opticalBottom);
            this.opticalRect.scale(decodeScale);
            this.outlineRect = scaleInsets(outlineLeft, outlineTop, outlineRight, outlineBottom, decodeScale);
            this.outlineRadius = outlineRadius * decodeScale;
            this.outlineAlpha = outlineAlpha / 255.0f;
        }

        public static synchronized Rect scaleInsets(int left, int top, int right, int bottom, float scale) {
            if (scale == 1.0f) {
                return new Rect(left, top, right, bottom);
            }
            Rect result = new Rect();
            result.left = (int) Math.ceil(left * scale);
            result.top = (int) Math.ceil(top * scale);
            result.right = (int) Math.ceil(right * scale);
            result.bottom = (int) Math.ceil(bottom * scale);
            return result;
        }
    }

    public NinePatch(Bitmap bitmap, byte[] chunk) {
        this(bitmap, chunk, null);
    }

    public NinePatch(Bitmap bitmap, byte[] chunk, String srcName) {
        this.mBitmap = bitmap;
        this.mSrcName = srcName;
        this.mNativeChunk = validateNinePatchChunk(chunk);
    }

    public synchronized NinePatch(NinePatch patch) {
        this.mBitmap = patch.mBitmap;
        this.mSrcName = patch.mSrcName;
        if (patch.mPaint != null) {
            this.mPaint = new Paint(patch.mPaint);
        }
        this.mNativeChunk = patch.mNativeChunk;
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mNativeChunk != 0) {
                nativeFinalize(this.mNativeChunk);
                this.mNativeChunk = 0L;
            }
        } finally {
            super.finalize();
        }
    }

    public String getName() {
        return this.mSrcName;
    }

    public Paint getPaint() {
        return this.mPaint;
    }

    public void setPaint(Paint p) {
        this.mPaint = p;
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public void draw(Canvas canvas, RectF location) {
        canvas.drawPatch(this, location, this.mPaint);
    }

    public void draw(Canvas canvas, Rect location) {
        canvas.drawPatch(this, location, this.mPaint);
    }

    public void draw(Canvas canvas, Rect location, Paint paint) {
        canvas.drawPatch(this, location, paint);
    }

    public int getDensity() {
        return this.mBitmap.mDensity;
    }

    public int getWidth() {
        return this.mBitmap.getWidth();
    }

    public int getHeight() {
        return this.mBitmap.getHeight();
    }

    public final boolean hasAlpha() {
        return this.mBitmap.hasAlpha();
    }

    public final Region getTransparentRegion(Rect bounds) {
        long r = nativeGetTransparentRegion(this.mBitmap, this.mNativeChunk, bounds);
        if (r != 0) {
            return new Region(r);
        }
        return null;
    }
}
