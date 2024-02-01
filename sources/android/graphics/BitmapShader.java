package android.graphics;

import android.annotation.UnsupportedAppUsage;
import android.graphics.Shader;

/* loaded from: classes.dex */
public class BitmapShader extends Shader {
    @UnsupportedAppUsage
    public Bitmap mBitmap;
    @UnsupportedAppUsage
    private int mTileX;
    @UnsupportedAppUsage
    private int mTileY;

    private static native long nativeCreate(long j, long j2, int i, int i2);

    public BitmapShader(Bitmap bitmap, Shader.TileMode tileX, Shader.TileMode tileY) {
        this(bitmap, tileX.nativeInt, tileY.nativeInt);
    }

    private BitmapShader(Bitmap bitmap, int tileX, int tileY) {
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
    long createNativeInstance(long nativeMatrix) {
        return nativeCreate(nativeMatrix, this.mBitmap.getNativeInstance(), this.mTileX, this.mTileY);
    }
}
