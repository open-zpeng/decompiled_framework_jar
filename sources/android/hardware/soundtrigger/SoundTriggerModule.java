package android.hardware.soundtrigger;

import android.hardware.soundtrigger.SoundTrigger;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public class SoundTriggerModule {
    private static final int EVENT_RECOGNITION = 1;
    private static final int EVENT_SERVICE_DIED = 2;
    private static final int EVENT_SERVICE_STATE_CHANGE = 4;
    private static final int EVENT_SOUNDMODEL = 3;
    private NativeEventHandlerDelegate mEventHandlerDelegate;
    public protected int mId;
    public protected long mNativeContext;

    private native void native_finalize();

    private native void native_setup(Object obj);

    private protected native void detach();

    private protected native int loadSoundModel(SoundTrigger.SoundModel soundModel, int[] iArr);

    private protected native int startRecognition(int i, SoundTrigger.RecognitionConfig recognitionConfig);

    private protected native int stopRecognition(int i);

    private protected native int unloadSoundModel(int i);

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized SoundTriggerModule(int moduleId, SoundTrigger.StatusListener listener, Handler handler) {
        this.mId = moduleId;
        this.mEventHandlerDelegate = new NativeEventHandlerDelegate(listener, handler);
        native_setup(new WeakReference(this));
    }

    protected void finalize() {
        native_finalize();
    }

    /* loaded from: classes.dex */
    private class NativeEventHandlerDelegate {
        private final Handler mHandler;

        NativeEventHandlerDelegate(final SoundTrigger.StatusListener listener, Handler handler) {
            Looper looper;
            if (handler != null) {
                looper = handler.getLooper();
            } else {
                looper = Looper.getMainLooper();
            }
            if (looper != null) {
                this.mHandler = new Handler(looper) { // from class: android.hardware.soundtrigger.SoundTriggerModule.NativeEventHandlerDelegate.1
                    @Override // android.os.Handler
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case 1:
                                if (listener != null) {
                                    listener.onRecognition((SoundTrigger.RecognitionEvent) msg.obj);
                                    return;
                                }
                                return;
                            case 2:
                                if (listener != null) {
                                    listener.onServiceDied();
                                    return;
                                }
                                return;
                            case 3:
                                if (listener != null) {
                                    listener.onSoundModelUpdate((SoundTrigger.SoundModelEvent) msg.obj);
                                    return;
                                }
                                return;
                            case 4:
                                if (listener != null) {
                                    listener.onServiceStateChange(msg.arg1);
                                    return;
                                }
                                return;
                            default:
                                return;
                        }
                    }
                };
            } else {
                this.mHandler = null;
            }
        }

        synchronized Handler handler() {
            return this.mHandler;
        }
    }

    public protected static void postEventFromNative(Object module_ref, int what, int arg1, int arg2, Object obj) {
        NativeEventHandlerDelegate delegate;
        Handler handler;
        SoundTriggerModule module = (SoundTriggerModule) ((WeakReference) module_ref).get();
        if (module != null && (delegate = module.mEventHandlerDelegate) != null && (handler = delegate.handler()) != null) {
            Message m = handler.obtainMessage(what, arg1, arg2, obj);
            handler.sendMessage(m);
        }
    }
}
