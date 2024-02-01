package android.hardware.camera2.impl;

import android.hardware.camera2.impl.CameraMetadataNative;
/* loaded from: classes.dex */
public interface GetCommand {
    synchronized <T> T getValue(CameraMetadataNative cameraMetadataNative, CameraMetadataNative.Key<T> key);
}
