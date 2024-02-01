package android.app;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.GraphicBuffer;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.transition.TransitionUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public abstract class SharedElementCallback {
    private static final String BUNDLE_SNAPSHOT_BITMAP = "sharedElement:snapshot:bitmap";
    private static final String BUNDLE_SNAPSHOT_GRAPHIC_BUFFER = "sharedElement:snapshot:graphicBuffer";
    private static final String BUNDLE_SNAPSHOT_IMAGE_MATRIX = "sharedElement:snapshot:imageMatrix";
    private static final String BUNDLE_SNAPSHOT_IMAGE_SCALETYPE = "sharedElement:snapshot:imageScaleType";
    static final SharedElementCallback NULL_CALLBACK = new SharedElementCallback() { // from class: android.app.SharedElementCallback.1
    };
    private Matrix mTempMatrix;

    /* loaded from: classes.dex */
    public interface OnSharedElementsReadyListener {
        void onSharedElementsReady();
    }

    public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
    }

    public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
    }

    public void onRejectSharedElements(List<View> rejectedSharedElements) {
    }

    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
    }

    public Parcelable onCaptureSharedElementSnapshot(View sharedElement, Matrix viewToGlobalMatrix, RectF screenBounds) {
        Bitmap bitmap;
        if (sharedElement instanceof ImageView) {
            ImageView imageView = (ImageView) sharedElement;
            Drawable d = imageView.getDrawable();
            Drawable bg = imageView.getBackground();
            if (d != null && ((bg == null || bg.getAlpha() == 0) && (bitmap = TransitionUtils.createDrawableBitmap(d, imageView)) != null)) {
                Bundle bundle = new Bundle();
                if (bitmap.getConfig() != Bitmap.Config.HARDWARE) {
                    bundle.putParcelable(BUNDLE_SNAPSHOT_BITMAP, bitmap);
                } else {
                    GraphicBuffer graphicBuffer = bitmap.createGraphicBufferHandle();
                    bundle.putParcelable(BUNDLE_SNAPSHOT_GRAPHIC_BUFFER, graphicBuffer);
                }
                bundle.putString(BUNDLE_SNAPSHOT_IMAGE_SCALETYPE, imageView.getScaleType().toString());
                if (imageView.getScaleType() == ImageView.ScaleType.MATRIX) {
                    Matrix matrix = imageView.getImageMatrix();
                    float[] values = new float[9];
                    matrix.getValues(values);
                    bundle.putFloatArray(BUNDLE_SNAPSHOT_IMAGE_MATRIX, values);
                }
                return bundle;
            }
        }
        if (this.mTempMatrix == null) {
            this.mTempMatrix = new Matrix(viewToGlobalMatrix);
        } else {
            this.mTempMatrix.set(viewToGlobalMatrix);
        }
        ViewGroup parent = (ViewGroup) sharedElement.getParent();
        return TransitionUtils.createViewBitmap(sharedElement, this.mTempMatrix, screenBounds, parent);
    }

    public View onCreateSnapshotView(Context context, Parcelable snapshot) {
        if (!(snapshot instanceof Bundle)) {
            if (!(snapshot instanceof Bitmap)) {
                return null;
            }
            View view = new View(context);
            Resources resources = context.getResources();
            view.setBackground(new BitmapDrawable(resources, (Bitmap) snapshot));
            return view;
        }
        Bundle bundle = (Bundle) snapshot;
        GraphicBuffer buffer = (GraphicBuffer) bundle.getParcelable(BUNDLE_SNAPSHOT_GRAPHIC_BUFFER);
        Bitmap bitmap = (Bitmap) bundle.getParcelable(BUNDLE_SNAPSHOT_BITMAP);
        if (buffer == null && bitmap == null) {
            return null;
        }
        if (bitmap == null) {
            bitmap = Bitmap.createHardwareBitmap(buffer);
        }
        ImageView imageView = new ImageView(context);
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.valueOf(bundle.getString(BUNDLE_SNAPSHOT_IMAGE_SCALETYPE)));
        if (imageView.getScaleType() != ImageView.ScaleType.MATRIX) {
            return imageView;
        }
        float[] values = bundle.getFloatArray(BUNDLE_SNAPSHOT_IMAGE_MATRIX);
        Matrix matrix = new Matrix();
        matrix.setValues(values);
        imageView.setImageMatrix(matrix);
        return imageView;
    }

    public void onSharedElementsArrived(List<String> sharedElementNames, List<View> sharedElements, OnSharedElementsReadyListener listener) {
        listener.onSharedElementsReady();
    }
}
