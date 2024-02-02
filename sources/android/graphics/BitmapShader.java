package android.graphics;

import android.graphics.Shader;
/* loaded from: classes.dex */
public class BitmapShader extends Shader {
    private protected Bitmap mBitmap;
    public protected int mTileX;
    public protected int mTileY;

    private static native long nativeCreate(long j, Bitmap bitmap, int i, int i2);

    public BitmapShader(Bitmap bitmap, Shader.TileMode tileX, Shader.TileMode tileY) {
        this(bitmap, tileX.nativeInt, tileY.nativeInt);
    }

    private synchronized BitmapShader(Bitmap bitmap, int tileX, int tileY) {
        if (bitmap == null) {
            throw new IllegalArgumentException("Bitmap must be non-null");
        }
        if (bitmap == this.mBitmap && tileX == this.mTileX && tileY == this.mTileY) {
            return;
        }
        this.mBitmap = bitmap;
        this.mTileX = tileX;
        this.mTileY = tileY;
    }

    @Override // android.graphics.Shader
    synchronized long createNativeInstance(long nativeMatrix) {
        return nativeCreate(nativeMatrix, this.mBitmap, this.mTileX, this.mTileY);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.Shader
    public synchronized Shader copy() {
        BitmapShader copy = new BitmapShader(this.mBitmap, this.mTileX, this.mTileY);
        copyLocalMatrix(copy);
        return copy;
    }
}
