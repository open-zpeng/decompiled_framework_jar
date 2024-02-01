package android.hardware.camera2.legacy;

import android.hardware.camera2.impl.CameraMetadataNative;
import android.os.Handler;
import android.util.Log;
import com.android.internal.telephony.IccCardConstants;
/* loaded from: classes.dex */
public class CameraDeviceState {
    public protected static final boolean DEBUG = false;
    private protected static final int NO_CAPTURE_ERROR = -1;
    public protected static final int STATE_CAPTURING = 4;
    public protected static final int STATE_CONFIGURING = 2;
    public protected static final int STATE_ERROR = 0;
    public protected static final int STATE_IDLE = 3;
    public protected static final int STATE_UNCONFIGURED = 1;
    public protected static final String TAG = "CameraDeviceState";
    public protected static final String[] sStateNames = {"ERROR", "UNCONFIGURED", "CONFIGURING", "IDLE", "CAPTURING"};
    public protected int mCurrentState = 1;
    public protected int mCurrentError = -1;
    public protected RequestHolder mCurrentRequest = null;
    public protected Handler mCurrentHandler = null;
    public protected CameraDeviceStateListener mCurrentListener = null;

    /* loaded from: classes.dex */
    public interface CameraDeviceStateListener {
        private protected synchronized void onBusy();

        private protected synchronized void onCaptureResult(CameraMetadataNative cameraMetadataNative, RequestHolder requestHolder);

        private protected synchronized void onCaptureStarted(RequestHolder requestHolder, long j);

        private protected synchronized void onConfiguring();

        private protected synchronized void onError(int i, Object obj, RequestHolder requestHolder);

        private protected synchronized void onIdle();

        private protected synchronized void onRepeatingRequestError(long j, int i);

        private protected synchronized void onRequestQueueEmpty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setError(int error) {
        this.mCurrentError = error;
        doStateTransition(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean setConfiguring() {
        doStateTransition(2);
        return this.mCurrentError == -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean setIdle() {
        doStateTransition(3);
        return this.mCurrentError == -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean setCaptureStart(RequestHolder request, long timestamp, int captureError) {
        this.mCurrentRequest = request;
        doStateTransition(4, timestamp, captureError);
        return this.mCurrentError == -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean setCaptureResult(final RequestHolder request, final CameraMetadataNative result, final int captureError, final Object captureErrorArg) {
        if (this.mCurrentState != 4) {
            Log.e(TAG, "Cannot receive result while in state: " + this.mCurrentState);
            this.mCurrentError = 1;
            doStateTransition(0);
            return this.mCurrentError == -1;
        }
        if (this.mCurrentHandler != null && this.mCurrentListener != null) {
            if (captureError != -1) {
                this.mCurrentHandler.post(new Runnable() { // from class: android.hardware.camera2.legacy.CameraDeviceState.1
                    @Override // java.lang.Runnable
                    public void run() {
                        CameraDeviceState.this.mCurrentListener.onError(captureError, captureErrorArg, request);
                    }
                });
            } else {
                this.mCurrentHandler.post(new Runnable() { // from class: android.hardware.camera2.legacy.CameraDeviceState.2
                    @Override // java.lang.Runnable
                    public void run() {
                        CameraDeviceState.this.mCurrentListener.onCaptureResult(result, request);
                    }
                });
            }
        }
        return this.mCurrentError == -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized boolean setCaptureResult(RequestHolder request, CameraMetadataNative result) {
        return setCaptureResult(request, result, -1, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setRepeatingRequestError(final long lastFrameNumber, final int repeatingRequestId) {
        this.mCurrentHandler.post(new Runnable() { // from class: android.hardware.camera2.legacy.CameraDeviceState.3
            @Override // java.lang.Runnable
            public void run() {
                CameraDeviceState.this.mCurrentListener.onRepeatingRequestError(lastFrameNumber, repeatingRequestId);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setRequestQueueEmpty() {
        this.mCurrentHandler.post(new Runnable() { // from class: android.hardware.camera2.legacy.CameraDeviceState.4
            @Override // java.lang.Runnable
            public void run() {
                CameraDeviceState.this.mCurrentListener.onRequestQueueEmpty();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setCameraDeviceCallbacks(Handler handler, CameraDeviceStateListener listener) {
        this.mCurrentHandler = handler;
        this.mCurrentListener = listener;
    }

    public protected synchronized void doStateTransition(int newState) {
        doStateTransition(newState, 0L, -1);
    }

    public protected synchronized void doStateTransition(int newState, final long timestamp, final int error) {
        if (newState != this.mCurrentState) {
            String stateName = IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
            if (newState >= 0 && newState < sStateNames.length) {
                stateName = sStateNames[newState];
            }
            Log.i(TAG, "Legacy camera service transitioning to state " + stateName);
        }
        if (newState != 0 && newState != 3 && this.mCurrentState != newState && this.mCurrentHandler != null && this.mCurrentListener != null) {
            this.mCurrentHandler.post(new Runnable() { // from class: android.hardware.camera2.legacy.CameraDeviceState.5
                @Override // java.lang.Runnable
                public void run() {
                    CameraDeviceState.this.mCurrentListener.onBusy();
                }
            });
        }
        if (newState == 0) {
            if (this.mCurrentState != 0 && this.mCurrentHandler != null && this.mCurrentListener != null) {
                this.mCurrentHandler.post(new Runnable() { // from class: android.hardware.camera2.legacy.CameraDeviceState.6
                    @Override // java.lang.Runnable
                    public void run() {
                        CameraDeviceState.this.mCurrentListener.onError(CameraDeviceState.this.mCurrentError, null, CameraDeviceState.this.mCurrentRequest);
                    }
                });
            }
            this.mCurrentState = 0;
            return;
        }
        switch (newState) {
            case 2:
                if (this.mCurrentState != 1 && this.mCurrentState != 3) {
                    Log.e(TAG, "Cannot call configure while in state: " + this.mCurrentState);
                    this.mCurrentError = 1;
                    doStateTransition(0);
                    return;
                }
                if (this.mCurrentState != 2 && this.mCurrentHandler != null && this.mCurrentListener != null) {
                    this.mCurrentHandler.post(new Runnable() { // from class: android.hardware.camera2.legacy.CameraDeviceState.7
                        @Override // java.lang.Runnable
                        public void run() {
                            CameraDeviceState.this.mCurrentListener.onConfiguring();
                        }
                    });
                }
                this.mCurrentState = 2;
                return;
            case 3:
                if (this.mCurrentState != 3) {
                    if (this.mCurrentState == 2 || this.mCurrentState == 4) {
                        if (this.mCurrentState != 3 && this.mCurrentHandler != null && this.mCurrentListener != null) {
                            this.mCurrentHandler.post(new Runnable() { // from class: android.hardware.camera2.legacy.CameraDeviceState.8
                                @Override // java.lang.Runnable
                                public void run() {
                                    CameraDeviceState.this.mCurrentListener.onIdle();
                                }
                            });
                        }
                        this.mCurrentState = 3;
                        return;
                    }
                    Log.e(TAG, "Cannot call idle while in state: " + this.mCurrentState);
                    this.mCurrentError = 1;
                    doStateTransition(0);
                    return;
                }
                return;
            case 4:
                if (this.mCurrentState != 3 && this.mCurrentState != 4) {
                    Log.e(TAG, "Cannot call capture while in state: " + this.mCurrentState);
                    this.mCurrentError = 1;
                    doStateTransition(0);
                    return;
                }
                if (this.mCurrentHandler != null && this.mCurrentListener != null) {
                    if (error != -1) {
                        this.mCurrentHandler.post(new Runnable() { // from class: android.hardware.camera2.legacy.CameraDeviceState.9
                            @Override // java.lang.Runnable
                            public void run() {
                                CameraDeviceState.this.mCurrentListener.onError(error, null, CameraDeviceState.this.mCurrentRequest);
                            }
                        });
                    } else {
                        this.mCurrentHandler.post(new Runnable() { // from class: android.hardware.camera2.legacy.CameraDeviceState.10
                            @Override // java.lang.Runnable
                            public void run() {
                                CameraDeviceState.this.mCurrentListener.onCaptureStarted(CameraDeviceState.this.mCurrentRequest, timestamp);
                            }
                        });
                    }
                }
                this.mCurrentState = 4;
                return;
            default:
                throw new IllegalStateException("Transition to unknown state: " + newState);
        }
    }
}
