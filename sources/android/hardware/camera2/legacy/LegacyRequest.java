package android.hardware.camera2.legacy;

import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest;
import android.util.Size;
import com.android.internal.util.Preconditions;
/* loaded from: classes.dex */
public class LegacyRequest {
    private protected final CaptureRequest captureRequest;
    private protected final CameraCharacteristics characteristics;
    private protected final Camera.Parameters parameters;
    private protected final Size previewSize;

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized LegacyRequest(CameraCharacteristics characteristics, CaptureRequest captureRequest, Size previewSize, Camera.Parameters parameters) {
        this.characteristics = (CameraCharacteristics) Preconditions.checkNotNull(characteristics, "characteristics must not be null");
        this.captureRequest = (CaptureRequest) Preconditions.checkNotNull(captureRequest, "captureRequest must not be null");
        this.previewSize = (Size) Preconditions.checkNotNull(previewSize, "previewSize must not be null");
        Preconditions.checkNotNull(parameters, "parameters must not be null");
        this.parameters = Camera.getParametersCopy(parameters);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setParameters(Camera.Parameters parameters) {
        Preconditions.checkNotNull(parameters, "parameters must not be null");
        this.parameters.copyFrom(parameters);
    }
}
