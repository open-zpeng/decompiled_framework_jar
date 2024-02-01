package android.speech;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.PermissionChecker;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.speech.IRecognitionService;
import android.speech.SpeechRecognizer;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.Objects;
import org.json.JSONArray;
import org.json.JSONException;

/* loaded from: classes2.dex */
public abstract class RecognitionService extends Service {
    private static final boolean DBG = true;
    private static final int MSG_CANCEL = 3;
    private static final int MSG_INIT_RECOGNIZER = 5;
    private static final int MSG_PUSH_DATA = 6;
    private static final int MSG_RESET = 4;
    private static final int MSG_SET_PARAMETERS = 8;
    private static final int MSG_START_LISTENING = 1;
    private static final int MSG_STOP_LISTENING = 2;
    private static final int MSG_UPDATE_VOCAB = 7;
    public static final String SERVICE_INTERFACE = "android.speech.RecognitionService";
    public static final String SERVICE_META_DATA = "android.speech";
    private static final String TAG = "RecognitionService";
    private RecognitionServiceBinder mBinder = new RecognitionServiceBinder(this);
    private Callback mCurrentCallback = null;
    private boolean isRecognitionInit = false;
    private final Handler mHandler = new Handler() { // from class: android.speech.RecognitionService.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    StartListeningArgs args = (StartListeningArgs) msg.obj;
                    RecognitionService.this.dispatchStartListening(args.mIntent, args.mListener, args.mCallingUid);
                    return;
                case 2:
                    RecognitionService.this.dispatchStopListening((IRecognitionListener) msg.obj);
                    return;
                case 3:
                    RecognitionService.this.dispatchCancel((IRecognitionListener) msg.obj);
                    return;
                case 4:
                    RecognitionService.this.dispatchClearCallback();
                    return;
                case 5:
                    InitRecognizerArgs initArgs = (InitRecognizerArgs) msg.obj;
                    RecognitionService.this.dispatchInitRecognizer(initArgs.mParams, initArgs.mListener, initArgs.mCallingUid);
                    return;
                case 6:
                default:
                    return;
                case 7:
                    VocabArgs vocabArgs = (VocabArgs) msg.obj;
                    RecognitionService.this.dispatchUpdateVocab(vocabArgs.mArgs, vocabArgs.mListener);
                    return;
                case 8:
                    RecognitionService.this.dispatchSetParameters((Bundle) msg.obj);
                    return;
            }
        }
    };

    protected abstract void onCancel(Callback callback);

    protected abstract void onStartListening(Intent intent, Callback callback);

    protected abstract void onStopListening(Callback callback);

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchInitRecognizer(Bundle params, final IRecognitionListener listener, int callingUid) {
        Callback callback = this.mCurrentCallback;
        if (callback == null || callback.mListener.asBinder() == listener.asBinder()) {
            Log.d(TAG, "created new mCurrentCallback, listener = " + listener.asBinder());
            if (this.mCurrentCallback == null) {
                try {
                    listener.asBinder().linkToDeath(new IBinder.DeathRecipient() { // from class: android.speech.RecognitionService.2
                        @Override // android.os.IBinder.DeathRecipient
                        public void binderDied() {
                            RecognitionService.this.mHandler.sendMessage(RecognitionService.this.mHandler.obtainMessage(3, listener));
                        }
                    }, 0);
                    this.mCurrentCallback = new Callback(listener, callingUid);
                } catch (RemoteException e) {
                    Log.e(TAG, "dead listener on initRecognizer");
                    return;
                }
            }
            this.isRecognitionInit = true;
            onInitRecognizer(params, this.mCurrentCallback);
            return;
        }
        try {
            listener.onError(8);
        } catch (RemoteException e2) {
            Log.d(TAG, "onError call from initRecognizer failed");
        }
        Log.i(TAG, "concurrent initRecognizer received - ignoring this call");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchStartListening(Intent intent, final IRecognitionListener listener, int callingUid) {
        Callback callback = this.mCurrentCallback;
        if (callback == null || callback.mListener.asBinder() == listener.asBinder()) {
            Log.d(TAG, "created new mCurrentCallback, listener = " + listener.asBinder());
            if (this.mCurrentCallback == null) {
                try {
                    listener.asBinder().linkToDeath(new IBinder.DeathRecipient() { // from class: android.speech.RecognitionService.3
                        @Override // android.os.IBinder.DeathRecipient
                        public void binderDied() {
                            RecognitionService.this.mHandler.sendMessage(RecognitionService.this.mHandler.obtainMessage(3, listener));
                        }
                    }, 0);
                    this.mCurrentCallback = new Callback(listener, callingUid);
                } catch (RemoteException e) {
                    Log.e(TAG, "dead listener on startListening");
                    return;
                }
            }
            onStartListening(intent, this.mCurrentCallback);
            return;
        }
        try {
            listener.onError(8);
        } catch (RemoteException e2) {
            Log.d(TAG, "onError call from startListening failed");
        }
        Log.i(TAG, "concurrent startListening received - ignoring this call");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchStopListening(IRecognitionListener listener) {
        try {
            if (this.mCurrentCallback == null) {
                listener.onError(5);
                Log.w(TAG, "stopListening called with no preceding startListening - ignoring");
            } else if (this.mCurrentCallback.mListener.asBinder() != listener.asBinder()) {
                listener.onError(8);
                Log.w(TAG, "stopListening called by other caller than startListening - ignoring");
            } else {
                onStopListening(this.mCurrentCallback);
            }
        } catch (RemoteException e) {
            Log.d(TAG, "onError call from stopListening failed");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchCancel(IRecognitionListener listener) {
        Callback callback = this.mCurrentCallback;
        if (callback == null) {
            Log.d(TAG, "cancel called with no preceding startListening - ignoring");
        } else if (callback.mListener.asBinder() != listener.asBinder()) {
            Log.w(TAG, "cancel called by client who did not call startListening - ignoring");
        } else {
            onCancel(this.mCurrentCallback);
            this.mCurrentCallback = null;
            Log.d(TAG, "canceling - setting mCurrentCallback to null");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchClearCallback() {
        this.mCurrentCallback = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchUpdateVocab(SpeechRecognizer.VocabArgs args, IRecognitionListener listener) {
        try {
            if (this.mCurrentCallback == null) {
                listener.onError(5);
                Log.w(TAG, "updateVocab called with no preceding startListening - ignoring");
            } else if (this.mCurrentCallback.mListener.asBinder() != listener.asBinder()) {
                listener.onError(8);
                Log.w(TAG, "updateVocab called by other caller than startListening - ignoring");
            } else {
                onUpdateVocab(args, this.mCurrentCallback);
            }
        } catch (RemoteException e) {
            Log.d(TAG, "onError call from updateVocab failed");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchSetParameters(Bundle params) {
        onSetParameters(params);
    }

    /* loaded from: classes2.dex */
    private class StartListeningArgs {
        public final int mCallingUid;
        public final Intent mIntent;
        public final IRecognitionListener mListener;

        public StartListeningArgs(Intent intent, IRecognitionListener listener, int callingUid) {
            this.mIntent = intent;
            this.mListener = listener;
            this.mCallingUid = callingUid;
        }
    }

    /* loaded from: classes2.dex */
    private class InitRecognizerArgs {
        public final int mCallingUid;
        public final IRecognitionListener mListener;
        public final Bundle mParams;

        public InitRecognizerArgs(Bundle params, IRecognitionListener listener, int callingUid) {
            this.mParams = params;
            this.mListener = listener;
            this.mCallingUid = callingUid;
        }
    }

    /* loaded from: classes2.dex */
    private class VocabArgs {
        public final SpeechRecognizer.VocabArgs mArgs;
        public final IRecognitionListener mListener;

        public VocabArgs(SpeechRecognizer.VocabArgs args, IRecognitionListener listener) {
            this.mArgs = args;
            this.mListener = listener;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkPermissions(IRecognitionListener listener, boolean forDataDelivery) {
        Log.d(TAG, "checkPermissions");
        if (forDataDelivery) {
            if (PermissionChecker.checkCallingOrSelfPermissionForDataDelivery(this, Manifest.permission.RECORD_AUDIO) == 0) {
                return true;
            }
        } else if (PermissionChecker.checkCallingOrSelfPermissionForPreflight(this, Manifest.permission.RECORD_AUDIO) == 0) {
            return true;
        }
        try {
            Log.e(TAG, "call for recognition service without RECORD_AUDIO permissions");
            listener.onError(9);
            return false;
        } catch (RemoteException re) {
            Log.e(TAG, "sending ERROR_INSUFFICIENT_PERMISSIONS message failed", re);
            return false;
        }
    }

    protected void onInitRecognizer(Bundle params, Callback listener) {
    }

    protected void onUpdateVocab(SpeechRecognizer.VocabArgs args, Callback listener) {
    }

    protected void onSetParameters(Bundle params) {
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind, intent=" + intent);
        return this.mBinder;
    }

    @Override // android.app.Service
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        this.mCurrentCallback = null;
        this.mBinder.clearReference();
        super.onDestroy();
    }

    /* loaded from: classes2.dex */
    public class Callback {
        private final int mCallingUid;
        private final IRecognitionListener mListener;

        private Callback(IRecognitionListener listener, int callingUid) {
            this.mListener = listener;
            this.mCallingUid = callingUid;
        }

        public void beginningOfSpeech() throws RemoteException {
            Log.d(RecognitionService.TAG, "beginningOfSpeech");
            this.mListener.onBeginningOfSpeech();
        }

        public void bufferReceived(byte[] buffer) throws RemoteException {
            this.mListener.onBufferReceived(buffer);
        }

        public void endOfSpeech() throws RemoteException {
            this.mListener.onEndOfSpeech();
        }

        public void error(int error) throws RemoteException {
            Message.obtain(RecognitionService.this.mHandler, 4).sendToTarget();
            this.mListener.onError(error);
        }

        public void partialResults(Bundle partialResults) throws RemoteException {
            this.mListener.onPartialResults(partialResults);
        }

        public void readyForSpeech(Bundle params) throws RemoteException {
            this.mListener.onReadyForSpeech(params);
        }

        public void results(Bundle results) throws RemoteException {
            Message.obtain(RecognitionService.this.mHandler, 4).sendToTarget();
            this.mListener.onResults(results);
        }

        public void rmsChanged(float rmsdB) throws RemoteException {
            this.mListener.onRmsChanged(rmsdB);
        }

        public void onEvent(int eventType, Bundle params) throws RemoteException {
            if (RecognitionService.this.isRecognitionInit && eventType == 1000) {
                Message.obtain(RecognitionService.this.mHandler, 4).sendToTarget();
                RecognitionService.this.isRecognitionInit = false;
            }
            this.mListener.onEvent(eventType, params);
        }

        public int getCallingUid() {
            return this.mCallingUid;
        }
    }

    /* loaded from: classes2.dex */
    private static final class RecognitionServiceBinder extends IRecognitionService.Stub {
        private final WeakReference<RecognitionService> mServiceRef;

        public RecognitionServiceBinder(RecognitionService service) {
            this.mServiceRef = new WeakReference<>(service);
        }

        @Override // android.speech.IRecognitionService
        public void initRecognizer(Bundle params, IRecognitionListener listener) {
            Log.d(RecognitionService.TAG, "initRecognizer called by:" + listener.asBinder());
            RecognitionService service = this.mServiceRef.get();
            if (service != null && service.checkPermissions(listener, false)) {
                Handler handler = service.mHandler;
                Handler handler2 = service.mHandler;
                Objects.requireNonNull(service);
                handler.sendMessage(Message.obtain(handler2, 5, new InitRecognizerArgs(params, listener, Binder.getCallingUid())));
            }
        }

        @Override // android.speech.IRecognitionService
        public void startListening(Intent recognizerIntent, IRecognitionListener listener) {
            Log.d(RecognitionService.TAG, "startListening called by:" + listener.asBinder());
            RecognitionService service = this.mServiceRef.get();
            if (service != null && service.checkPermissions(listener, true)) {
                Handler handler = service.mHandler;
                Handler handler2 = service.mHandler;
                Objects.requireNonNull(service);
                handler.sendMessage(Message.obtain(handler2, 1, new StartListeningArgs(recognizerIntent, listener, Binder.getCallingUid())));
            }
        }

        @Override // android.speech.IRecognitionService
        public void stopListening(IRecognitionListener listener) {
            Log.d(RecognitionService.TAG, "stopListening called by:" + listener.asBinder());
            RecognitionService service = this.mServiceRef.get();
            if (service != null && service.checkPermissions(listener, false)) {
                service.mHandler.sendMessage(Message.obtain(service.mHandler, 2, listener));
            }
        }

        @Override // android.speech.IRecognitionService
        public void cancel(IRecognitionListener listener) {
            Log.d(RecognitionService.TAG, "cancel called by:" + listener.asBinder());
            RecognitionService service = this.mServiceRef.get();
            if (service != null && service.checkPermissions(listener, false)) {
                service.mHandler.sendMessage(Message.obtain(service.mHandler, 3, listener));
            }
        }

        @Override // android.speech.IRecognitionService
        public void updateVocab(boolean addOrDelete, int type, String vocab, IRecognitionListener listener) {
            Log.d(RecognitionService.TAG, "updateVocab called by:" + listener.asBinder());
            RecognitionService service = this.mServiceRef.get();
            if (service != null && service.checkPermissions(listener, false)) {
                try {
                    Handler handler = service.mHandler;
                    Handler handler2 = service.mHandler;
                    Objects.requireNonNull(service);
                    handler.sendMessage(Message.obtain(handler2, 7, new VocabArgs(new SpeechRecognizer.VocabArgs(addOrDelete, type, new JSONArray(vocab)), listener)));
                } catch (JSONException e) {
                    Log.e(RecognitionService.TAG, "error convert string to jason array");
                }
            }
        }

        @Override // android.speech.IRecognitionService
        public void setParameters(Bundle params) {
            Log.d(RecognitionService.TAG, "setParameters called");
            RecognitionService service = this.mServiceRef.get();
            if (service != null) {
                service.mHandler.sendMessage(Message.obtain(service.mHandler, 8, params));
            }
        }

        public void clearReference() {
            this.mServiceRef.clear();
        }
    }
}
