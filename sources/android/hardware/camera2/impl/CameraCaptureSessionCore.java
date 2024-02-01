package android.hardware.camera2.impl;

import android.hardware.camera2.impl.CameraDeviceImpl;
/* loaded from: classes.dex */
public interface CameraCaptureSessionCore {
    synchronized CameraDeviceImpl.StateCallbackKK getDeviceStateCallback();

    synchronized boolean isAborting();

    synchronized void replaceSessionClose();
}
