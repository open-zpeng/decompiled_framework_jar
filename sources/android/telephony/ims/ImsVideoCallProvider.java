package android.telephony.ims;

import android.annotation.SystemApi;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.telecom.VideoProfile;
import android.view.Surface;
import com.android.ims.internal.IImsVideoCallCallback;
import com.android.ims.internal.IImsVideoCallProvider;
import com.android.internal.os.SomeArgs;
@SystemApi
/* loaded from: classes2.dex */
public abstract class ImsVideoCallProvider {
    private static final int MSG_REQUEST_CALL_DATA_USAGE = 10;
    private static final int MSG_REQUEST_CAMERA_CAPABILITIES = 9;
    private static final int MSG_SEND_SESSION_MODIFY_REQUEST = 7;
    private static final int MSG_SEND_SESSION_MODIFY_RESPONSE = 8;
    private static final int MSG_SET_CALLBACK = 1;
    private static final int MSG_SET_CAMERA = 2;
    private static final int MSG_SET_DEVICE_ORIENTATION = 5;
    private static final int MSG_SET_DISPLAY_SURFACE = 4;
    private static final int MSG_SET_PAUSE_IMAGE = 11;
    private static final int MSG_SET_PREVIEW_SURFACE = 3;
    private static final int MSG_SET_ZOOM = 6;
    private IImsVideoCallCallback mCallback;
    private final Handler mProviderHandler = new Handler(Looper.getMainLooper()) { // from class: android.telephony.ims.ImsVideoCallProvider.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            SomeArgs args;
            switch (msg.what) {
                case 1:
                    ImsVideoCallProvider.this.mCallback = (IImsVideoCallCallback) msg.obj;
                    return;
                case 2:
                    args = (SomeArgs) msg.obj;
                    try {
                        ImsVideoCallProvider.this.onSetCamera((String) args.arg1);
                        ImsVideoCallProvider.this.onSetCamera((String) args.arg1, args.argi1);
                        return;
                    } finally {
                    }
                case 3:
                    ImsVideoCallProvider.this.onSetPreviewSurface((Surface) msg.obj);
                    return;
                case 4:
                    ImsVideoCallProvider.this.onSetDisplaySurface((Surface) msg.obj);
                    return;
                case 5:
                    ImsVideoCallProvider.this.onSetDeviceOrientation(msg.arg1);
                    return;
                case 6:
                    ImsVideoCallProvider.this.onSetZoom(((Float) msg.obj).floatValue());
                    return;
                case 7:
                    args = (SomeArgs) msg.obj;
                    try {
                        VideoProfile fromProfile = (VideoProfile) args.arg1;
                        VideoProfile toProfile = (VideoProfile) args.arg2;
                        ImsVideoCallProvider.this.onSendSessionModifyRequest(fromProfile, toProfile);
                        return;
                    } finally {
                    }
                case 8:
                    ImsVideoCallProvider.this.onSendSessionModifyResponse((VideoProfile) msg.obj);
                    return;
                case 9:
                    ImsVideoCallProvider.this.onRequestCameraCapabilities();
                    return;
                case 10:
                    ImsVideoCallProvider.this.onRequestCallDataUsage();
                    return;
                case 11:
                    ImsVideoCallProvider.this.onSetPauseImage((Uri) msg.obj);
                    return;
                default:
                    return;
            }
        }
    };
    private final ImsVideoCallProviderBinder mBinder = new ImsVideoCallProviderBinder();

    public abstract void onRequestCallDataUsage();

    public abstract void onRequestCameraCapabilities();

    public abstract void onSendSessionModifyRequest(VideoProfile videoProfile, VideoProfile videoProfile2);

    public abstract void onSendSessionModifyResponse(VideoProfile videoProfile);

    public abstract void onSetCamera(String str);

    public abstract void onSetDeviceOrientation(int i);

    public abstract void onSetDisplaySurface(Surface surface);

    public abstract void onSetPauseImage(Uri uri);

    public abstract void onSetPreviewSurface(Surface surface);

    public abstract void onSetZoom(float f);

    /* loaded from: classes2.dex */
    private final class ImsVideoCallProviderBinder extends IImsVideoCallProvider.Stub {
        private ImsVideoCallProviderBinder() {
        }

        public synchronized void setCallback(IImsVideoCallCallback callback) {
            ImsVideoCallProvider.this.mProviderHandler.obtainMessage(1, callback).sendToTarget();
        }

        @Override // com.android.ims.internal.IImsVideoCallProvider
        public synchronized void setCamera(String cameraId, int uid) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = cameraId;
            args.argi1 = uid;
            ImsVideoCallProvider.this.mProviderHandler.obtainMessage(2, args).sendToTarget();
        }

        @Override // com.android.ims.internal.IImsVideoCallProvider
        public synchronized void setPreviewSurface(Surface surface) {
            ImsVideoCallProvider.this.mProviderHandler.obtainMessage(3, surface).sendToTarget();
        }

        @Override // com.android.ims.internal.IImsVideoCallProvider
        public synchronized void setDisplaySurface(Surface surface) {
            ImsVideoCallProvider.this.mProviderHandler.obtainMessage(4, surface).sendToTarget();
        }

        @Override // com.android.ims.internal.IImsVideoCallProvider
        public synchronized void setDeviceOrientation(int rotation) {
            ImsVideoCallProvider.this.mProviderHandler.obtainMessage(5, rotation, 0).sendToTarget();
        }

        @Override // com.android.ims.internal.IImsVideoCallProvider
        public synchronized void setZoom(float value) {
            ImsVideoCallProvider.this.mProviderHandler.obtainMessage(6, Float.valueOf(value)).sendToTarget();
        }

        @Override // com.android.ims.internal.IImsVideoCallProvider
        public synchronized void sendSessionModifyRequest(VideoProfile fromProfile, VideoProfile toProfile) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = fromProfile;
            args.arg2 = toProfile;
            ImsVideoCallProvider.this.mProviderHandler.obtainMessage(7, args).sendToTarget();
        }

        @Override // com.android.ims.internal.IImsVideoCallProvider
        public synchronized void sendSessionModifyResponse(VideoProfile responseProfile) {
            ImsVideoCallProvider.this.mProviderHandler.obtainMessage(8, responseProfile).sendToTarget();
        }

        @Override // com.android.ims.internal.IImsVideoCallProvider
        public synchronized void requestCameraCapabilities() {
            ImsVideoCallProvider.this.mProviderHandler.obtainMessage(9).sendToTarget();
        }

        @Override // com.android.ims.internal.IImsVideoCallProvider
        public synchronized void requestCallDataUsage() {
            ImsVideoCallProvider.this.mProviderHandler.obtainMessage(10).sendToTarget();
        }

        @Override // com.android.ims.internal.IImsVideoCallProvider
        public synchronized void setPauseImage(Uri uri) {
            ImsVideoCallProvider.this.mProviderHandler.obtainMessage(11, uri).sendToTarget();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final IImsVideoCallProvider getInterface() {
        return this.mBinder;
    }

    public void onSetCamera(String cameraId, int uid) {
    }

    public void receiveSessionModifyRequest(VideoProfile VideoProfile) {
        if (this.mCallback != null) {
            try {
                this.mCallback.receiveSessionModifyRequest(VideoProfile);
            } catch (RemoteException e) {
            }
        }
    }

    public void receiveSessionModifyResponse(int status, VideoProfile requestedProfile, VideoProfile responseProfile) {
        if (this.mCallback != null) {
            try {
                this.mCallback.receiveSessionModifyResponse(status, requestedProfile, responseProfile);
            } catch (RemoteException e) {
            }
        }
    }

    public void handleCallSessionEvent(int event) {
        if (this.mCallback != null) {
            try {
                this.mCallback.handleCallSessionEvent(event);
            } catch (RemoteException e) {
            }
        }
    }

    public void changePeerDimensions(int width, int height) {
        if (this.mCallback != null) {
            try {
                this.mCallback.changePeerDimensions(width, height);
            } catch (RemoteException e) {
            }
        }
    }

    public void changeCallDataUsage(long dataUsage) {
        if (this.mCallback != null) {
            try {
                this.mCallback.changeCallDataUsage(dataUsage);
            } catch (RemoteException e) {
            }
        }
    }

    public void changeCameraCapabilities(VideoProfile.CameraCapabilities CameraCapabilities) {
        if (this.mCallback != null) {
            try {
                this.mCallback.changeCameraCapabilities(CameraCapabilities);
            } catch (RemoteException e) {
            }
        }
    }

    public void changeVideoQuality(int videoQuality) {
        if (this.mCallback != null) {
            try {
                this.mCallback.changeVideoQuality(videoQuality);
            } catch (RemoteException e) {
            }
        }
    }
}
