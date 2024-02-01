package android.hardware.camera2.impl;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.ICameraDeviceUser;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.utils.SubmitInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.Surface;
/* loaded from: classes.dex */
public class ICameraDeviceUserWrapper {
    private final ICameraDeviceUser mRemoteDevice;

    public synchronized ICameraDeviceUserWrapper(ICameraDeviceUser remoteDevice) {
        if (remoteDevice == null) {
            throw new NullPointerException("Remote device may not be null");
        }
        this.mRemoteDevice = remoteDevice;
    }

    public synchronized void unlinkToDeath(IBinder.DeathRecipient recipient, int flags) {
        if (this.mRemoteDevice.asBinder() != null) {
            this.mRemoteDevice.asBinder().unlinkToDeath(recipient, flags);
        }
    }

    public synchronized void disconnect() {
        try {
            this.mRemoteDevice.disconnect();
        } catch (RemoteException e) {
        }
    }

    public synchronized SubmitInfo submitRequest(CaptureRequest request, boolean streaming) throws CameraAccessException {
        try {
            return this.mRemoteDevice.submitRequest(request, streaming);
        } catch (Throwable t) {
            CameraManager.throwAsPublicException(t);
            throw new UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public synchronized SubmitInfo submitRequestList(CaptureRequest[] requestList, boolean streaming) throws CameraAccessException {
        try {
            return this.mRemoteDevice.submitRequestList(requestList, streaming);
        } catch (Throwable t) {
            CameraManager.throwAsPublicException(t);
            throw new UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public synchronized long cancelRequest(int requestId) throws CameraAccessException {
        try {
            return this.mRemoteDevice.cancelRequest(requestId);
        } catch (Throwable t) {
            CameraManager.throwAsPublicException(t);
            throw new UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public synchronized void beginConfigure() throws CameraAccessException {
        try {
            this.mRemoteDevice.beginConfigure();
        } catch (Throwable t) {
            CameraManager.throwAsPublicException(t);
            throw new UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public synchronized void endConfigure(int operatingMode, CameraMetadataNative sessionParams) throws CameraAccessException {
        CameraMetadataNative cameraMetadataNative;
        try {
            ICameraDeviceUser iCameraDeviceUser = this.mRemoteDevice;
            if (sessionParams != null) {
                cameraMetadataNative = sessionParams;
            } else {
                cameraMetadataNative = new CameraMetadataNative();
            }
            iCameraDeviceUser.endConfigure(operatingMode, cameraMetadataNative);
        } catch (Throwable t) {
            CameraManager.throwAsPublicException(t);
            throw new UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public synchronized void deleteStream(int streamId) throws CameraAccessException {
        try {
            this.mRemoteDevice.deleteStream(streamId);
        } catch (Throwable t) {
            CameraManager.throwAsPublicException(t);
            throw new UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public synchronized int createStream(OutputConfiguration outputConfiguration) throws CameraAccessException {
        try {
            return this.mRemoteDevice.createStream(outputConfiguration);
        } catch (Throwable t) {
            CameraManager.throwAsPublicException(t);
            throw new UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public synchronized int createInputStream(int width, int height, int format) throws CameraAccessException {
        try {
            return this.mRemoteDevice.createInputStream(width, height, format);
        } catch (Throwable t) {
            CameraManager.throwAsPublicException(t);
            throw new UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public synchronized Surface getInputSurface() throws CameraAccessException {
        try {
            return this.mRemoteDevice.getInputSurface();
        } catch (Throwable t) {
            CameraManager.throwAsPublicException(t);
            throw new UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public synchronized CameraMetadataNative createDefaultRequest(int templateId) throws CameraAccessException {
        try {
            return this.mRemoteDevice.createDefaultRequest(templateId);
        } catch (Throwable t) {
            CameraManager.throwAsPublicException(t);
            throw new UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public synchronized CameraMetadataNative getCameraInfo() throws CameraAccessException {
        try {
            return this.mRemoteDevice.getCameraInfo();
        } catch (Throwable t) {
            CameraManager.throwAsPublicException(t);
            throw new UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public synchronized void waitUntilIdle() throws CameraAccessException {
        try {
            this.mRemoteDevice.waitUntilIdle();
        } catch (Throwable t) {
            CameraManager.throwAsPublicException(t);
            throw new UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public synchronized long flush() throws CameraAccessException {
        try {
            return this.mRemoteDevice.flush();
        } catch (Throwable t) {
            CameraManager.throwAsPublicException(t);
            throw new UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public synchronized void prepare(int streamId) throws CameraAccessException {
        try {
            this.mRemoteDevice.prepare(streamId);
        } catch (Throwable t) {
            CameraManager.throwAsPublicException(t);
            throw new UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public synchronized void tearDown(int streamId) throws CameraAccessException {
        try {
            this.mRemoteDevice.tearDown(streamId);
        } catch (Throwable t) {
            CameraManager.throwAsPublicException(t);
            throw new UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public synchronized void prepare2(int maxCount, int streamId) throws CameraAccessException {
        try {
            this.mRemoteDevice.prepare2(maxCount, streamId);
        } catch (Throwable t) {
            CameraManager.throwAsPublicException(t);
            throw new UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public synchronized void updateOutputConfiguration(int streamId, OutputConfiguration config) throws CameraAccessException {
        try {
            this.mRemoteDevice.updateOutputConfiguration(streamId, config);
        } catch (Throwable t) {
            CameraManager.throwAsPublicException(t);
            throw new UnsupportedOperationException("Unexpected exception", t);
        }
    }

    public synchronized void finalizeOutputConfigurations(int streamId, OutputConfiguration deferredConfig) throws CameraAccessException {
        try {
            this.mRemoteDevice.finalizeOutputConfigurations(streamId, deferredConfig);
        } catch (Throwable t) {
            CameraManager.throwAsPublicException(t);
            throw new UnsupportedOperationException("Unexpected exception", t);
        }
    }
}
