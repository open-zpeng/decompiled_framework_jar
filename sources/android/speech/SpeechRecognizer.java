package android.speech;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.speech.IRecognitionListener;
import android.speech.IRecognitionService;
import android.text.TextUtils;
import android.util.Log;
import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.json.JSONArray;
/* loaded from: classes2.dex */
public class SpeechRecognizer {
    public static final String CONFIDENCE_SCORES = "confidence_scores";
    private static final boolean DBG = true;
    public static final int ERROR_AUDIO = 3;
    public static final int ERROR_CLIENT = 5;
    public static final int ERROR_INSUFFICIENT_PERMISSIONS = 9;
    public static final int ERROR_NETWORK = 2;
    public static final int ERROR_NETWORK_TIMEOUT = 1;
    public static final int ERROR_NO_MATCH = 7;
    public static final int ERROR_RECOGNIZER_BUSY = 8;
    public static final int ERROR_SERVER = 4;
    public static final int ERROR_SPEECH_TIMEOUT = 6;
    private static final int MAX_BYTE_LIMIT_SIZE = 51200;
    private static final int MSG_CANCEL = 3;
    private static final int MSG_CHANGE_LISTENER = 4;
    private static final int MSG_INIT = 5;
    private static final int MSG_PUSH_DATA = 6;
    private static final int MSG_SET_PARAMETERS = 8;
    private static final int MSG_START = 1;
    private static final int MSG_STOP = 2;
    private static final int MSG_UPDATA_VOCAB = 7;
    public static final String RESULTS_RECOGNITION = "results_recognition";
    private static final String SOCKET_NAME = "com.xiaopeng.xpspeechservice";
    private static final String TAG = "SpeechRecognizer";
    private Connection mConnection;
    private final Context mContext;
    private Handler mHandler;
    private IRecognitionService mService;
    private final ComponentName mServiceComponent;
    private LocalSocket mSocket;
    private ConcurrentLinkedQueue<byte[]> mBufferQueue = new ConcurrentLinkedQueue<>();
    private final Queue<Message> mPendingTasks = new ConcurrentLinkedQueue();
    private ByteArrayPool mByteArrayPool = new ByteArrayPool(MAX_BYTE_LIMIT_SIZE);
    private final InternalListener mListener = new InternalListener();

    private synchronized SpeechRecognizer(Context context, ComponentName serviceComponent) {
        this.mContext = context;
        this.mServiceComponent = serviceComponent;
        HandlerThread handlerThread = new HandlerThread("SpeechRecognizerWork");
        handlerThread.start();
        this.mHandler = new Handler(handlerThread.getLooper()) { // from class: android.speech.SpeechRecognizer.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        SpeechRecognizer.this.handleStartListening((Intent) msg.obj);
                        return;
                    case 2:
                        SpeechRecognizer.this.handleStopMessage();
                        return;
                    case 3:
                        SpeechRecognizer.this.handleCancelMessage();
                        return;
                    case 4:
                        SpeechRecognizer.this.handleChangeListener((RecognitionListener) msg.obj);
                        return;
                    case 5:
                        SpeechRecognizer.this.handleInitRecognizer((Bundle) msg.obj);
                        return;
                    case 6:
                        SpeechRecognizer.this.handlePushData();
                        return;
                    case 7:
                        SpeechRecognizer.this.handleUpdateVocab((VocabArgs) msg.obj);
                        return;
                    case 8:
                        SpeechRecognizer.this.handleSetParameters((Bundle) msg.obj);
                        return;
                    default:
                        return;
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class Connection implements ServiceConnection {
        private Connection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            SpeechRecognizer.this.mService = IRecognitionService.Stub.asInterface(service);
            Log.d(SpeechRecognizer.TAG, "onServiceConnected - Success");
            while (!SpeechRecognizer.this.mPendingTasks.isEmpty()) {
                SpeechRecognizer.this.mHandler.sendMessage((Message) SpeechRecognizer.this.mPendingTasks.poll());
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            SpeechRecognizer.this.mService = null;
            SpeechRecognizer.this.mConnection = null;
            SpeechRecognizer.this.mPendingTasks.clear();
            Log.d(SpeechRecognizer.TAG, "onServiceDisconnected - Success");
        }
    }

    public static boolean isRecognitionAvailable(Context context) {
        List<ResolveInfo> list = context.getPackageManager().queryIntentServices(new Intent(RecognitionService.SERVICE_INTERFACE), 0);
        return (list == null || list.size() == 0) ? false : true;
    }

    public static SpeechRecognizer createSpeechRecognizer(Context context) {
        return createSpeechRecognizer(context, null);
    }

    public static SpeechRecognizer createSpeechRecognizer(Context context, ComponentName serviceComponent) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null)");
        }
        checkIsCalledFromMainThread();
        return new SpeechRecognizer(context, serviceComponent);
    }

    public void setRecognitionListener(RecognitionListener listener) {
        checkIsCalledFromMainThread();
        putMessage(Message.obtain(this.mHandler, 4, listener));
    }

    private boolean connectService() {
        if (this.mConnection == null) {
            this.mConnection = new Connection();
            Intent serviceIntent = new Intent(RecognitionService.SERVICE_INTERFACE);
            if (this.mServiceComponent == null) {
                String serviceComponent = new ComponentName(SOCKET_NAME, "com.xiaopeng.xpspeechservice.asr.XpRecognitionService").flattenToShortString();
                if (TextUtils.isEmpty(serviceComponent)) {
                    Log.e(TAG, "no selected voice recognition service");
                    this.mListener.onError(5);
                    return false;
                }
                serviceIntent.setComponent(ComponentName.unflattenFromString(serviceComponent));
            } else {
                serviceIntent.setComponent(this.mServiceComponent);
            }
            if (!this.mContext.bindService(serviceIntent, this.mConnection, 1)) {
                Log.e(TAG, "bind to recognition service failed");
                this.mConnection = null;
                this.mService = null;
                this.mListener.onError(5);
                return false;
            }
        }
        return true;
    }

    public void setParameters(Bundle params) {
        checkIsCalledFromMainThread();
        if (connectService()) {
            putMessage(Message.obtain(this.mHandler, 8, params));
        }
    }

    public void initSpeechRecognizer(Bundle params) {
        checkIsCalledFromMainThread();
        if (connectService()) {
            putMessage(Message.obtain(this.mHandler, 5, params));
        }
    }

    public void startListening(Intent recognizerIntent) {
        if (recognizerIntent == null) {
            throw new IllegalArgumentException("intent must not be null");
        }
        checkIsCalledFromMainThread();
        if (connectService()) {
            putMessage(Message.obtain(this.mHandler, 1, recognizerIntent));
        }
    }

    public void stopListening() {
        checkIsCalledFromMainThread();
        putMessage(Message.obtain(this.mHandler, 2));
    }

    public void cancel() {
        checkIsCalledFromMainThread();
        putMessage(Message.obtain(this.mHandler, 3));
    }

    public void pushSpeechData(byte[] data) {
        checkIsCalledFromMainThread();
        byte[] dest = this.mByteArrayPool.getBuf(data.length);
        System.arraycopy(data, 0, dest, 0, data.length);
        this.mBufferQueue.offer(dest);
        putMessage(Message.obtain(this.mHandler, 6));
    }

    /* loaded from: classes2.dex */
    public static class VocabArgs {
        public final boolean mAddOrDelete;
        public final JSONArray mJsonArray;
        public final int mType;

        public VocabArgs(boolean addOrDelete, int type, JSONArray jsonArray) {
            this.mAddOrDelete = addOrDelete;
            this.mType = type;
            this.mJsonArray = jsonArray;
        }
    }

    public void updateVocab(boolean addOrDelete, int type, JSONArray jsonArray) {
        checkIsCalledFromMainThread();
        putMessage(Message.obtain(this.mHandler, 7, new VocabArgs(addOrDelete, type, jsonArray)));
    }

    private static synchronized void checkIsCalledFromMainThread() {
    }

    private synchronized void putMessage(Message msg) {
        if (this.mService == null) {
            this.mPendingTasks.offer(msg);
        } else {
            this.mHandler.sendMessage(msg);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleInitRecognizer(Bundle params) {
        if (!checkOpenConnection()) {
            return;
        }
        try {
            this.mService.initRecognizer(params, this.mListener);
            Log.d(TAG, "service init recognizer command succeded");
        } catch (RemoteException e) {
            Log.e(TAG, "initRecognizer() failed", e);
            this.mListener.onError(5);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handleStartListening(Intent recognizerIntent) {
        if (!checkOpenConnection()) {
            return;
        }
        try {
            this.mService.startListening(recognizerIntent, this.mListener);
            Log.d(TAG, "service start listening command succeded");
        } catch (RemoteException e) {
            Log.e(TAG, "startListening() failed", e);
            this.mListener.onError(5);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handleStopMessage() {
        if (!checkOpenConnection()) {
            return;
        }
        try {
            this.mService.stopListening(this.mListener);
            this.mBufferQueue.clear();
            Log.d(TAG, "service stop listening command succeded");
        } catch (RemoteException e) {
            Log.e(TAG, "stopListening() failed", e);
            this.mListener.onError(5);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePushData() {
        byte[] data;
        if (this.mSocket == null) {
            this.mSocket = new LocalSocket();
            try {
                this.mSocket.connect(new LocalSocketAddress(SOCKET_NAME));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.mSocket.isConnected() && (data = this.mBufferQueue.poll()) != null) {
            try {
                this.mSocket.getOutputStream().write(data);
                this.mSocket.getOutputStream().flush();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            this.mByteArrayPool.returnBuf(data);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUpdateVocab(VocabArgs args) {
        if (!checkOpenConnection()) {
            return;
        }
        try {
            this.mService.updateVocab(args.mAddOrDelete, args.mType, args.mJsonArray.toString(), this.mListener);
            Log.d(TAG, "update vocab to service succeded");
        } catch (RemoteException e) {
            Log.e(TAG, "update vocab failed", e);
            this.mListener.onError(5);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSetParameters(Bundle params) {
        if (!checkOpenConnection()) {
            return;
        }
        try {
            this.mService.setParameters(params);
            Log.d(TAG, "set parameters command succeded");
        } catch (RemoteException e) {
            Log.e(TAG, "setParameters() failed", e);
            this.mListener.onError(5);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handleCancelMessage() {
        if (!checkOpenConnection()) {
            return;
        }
        try {
            this.mService.cancel(this.mListener);
            Log.d(TAG, "service cancel command succeded");
        } catch (RemoteException e) {
            Log.e(TAG, "cancel() failed", e);
            this.mListener.onError(5);
        }
    }

    private synchronized boolean checkOpenConnection() {
        if (this.mService != null) {
            return true;
        }
        this.mListener.onError(5);
        Log.e(TAG, "not connected to the recognition service");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void handleChangeListener(RecognitionListener listener) {
        Log.d(TAG, "handleChangeListener, listener=" + listener);
        this.mListener.mInternalListener = listener;
    }

    public void destroy() {
        if (this.mService != null) {
            try {
                this.mService.cancel(this.mListener);
            } catch (RemoteException e) {
            }
        }
        if (this.mConnection != null) {
            this.mContext.unbindService(this.mConnection);
        }
        this.mHandler.post(new Runnable() { // from class: android.speech.SpeechRecognizer.2
            @Override // java.lang.Runnable
            public void run() {
                if (SpeechRecognizer.this.mSocket != null) {
                    try {
                        SpeechRecognizer.this.mSocket.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    SpeechRecognizer.this.mSocket = null;
                }
            }
        });
        this.mPendingTasks.clear();
        this.mBufferQueue.clear();
        this.mByteArrayPool.releaseBuf();
        this.mService = null;
        this.mConnection = null;
        this.mListener.mInternalListener = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class InternalListener extends IRecognitionListener.Stub {
        private static final int MSG_BEGINNING_OF_SPEECH = 1;
        private static final int MSG_BUFFER_RECEIVED = 2;
        private static final int MSG_END_OF_SPEECH = 3;
        private static final int MSG_ERROR = 4;
        private static final int MSG_ON_EVENT = 9;
        private static final int MSG_PARTIAL_RESULTS = 7;
        private static final int MSG_READY_FOR_SPEECH = 5;
        private static final int MSG_RESULTS = 6;
        private static final int MSG_RMS_CHANGED = 8;
        private final Handler mInternalHandler;
        private RecognitionListener mInternalListener;

        private synchronized InternalListener() {
            this.mInternalHandler = new Handler() { // from class: android.speech.SpeechRecognizer.InternalListener.1
                @Override // android.os.Handler
                public void handleMessage(Message msg) {
                    if (InternalListener.this.mInternalListener != null) {
                        switch (msg.what) {
                            case 1:
                                InternalListener.this.mInternalListener.onBeginningOfSpeech();
                                return;
                            case 2:
                                InternalListener.this.mInternalListener.onBufferReceived((byte[]) msg.obj);
                                return;
                            case 3:
                                InternalListener.this.mInternalListener.onEndOfSpeech();
                                return;
                            case 4:
                                InternalListener.this.mInternalListener.onError(((Integer) msg.obj).intValue());
                                return;
                            case 5:
                                InternalListener.this.mInternalListener.onReadyForSpeech((Bundle) msg.obj);
                                return;
                            case 6:
                                InternalListener.this.mInternalListener.onResults((Bundle) msg.obj);
                                return;
                            case 7:
                                InternalListener.this.mInternalListener.onPartialResults((Bundle) msg.obj);
                                return;
                            case 8:
                                InternalListener.this.mInternalListener.onRmsChanged(((Float) msg.obj).floatValue());
                                return;
                            case 9:
                                InternalListener.this.mInternalListener.onEvent(msg.arg1, (Bundle) msg.obj);
                                return;
                            default:
                                return;
                        }
                    }
                }
            };
        }

        @Override // android.speech.IRecognitionListener
        public synchronized void onBeginningOfSpeech() {
            Message.obtain(this.mInternalHandler, 1).sendToTarget();
        }

        @Override // android.speech.IRecognitionListener
        public synchronized void onBufferReceived(byte[] buffer) {
            Message.obtain(this.mInternalHandler, 2, buffer).sendToTarget();
        }

        @Override // android.speech.IRecognitionListener
        public synchronized void onEndOfSpeech() {
            Message.obtain(this.mInternalHandler, 3).sendToTarget();
        }

        @Override // android.speech.IRecognitionListener
        public synchronized void onError(int error) {
            Message.obtain(this.mInternalHandler, 4, Integer.valueOf(error)).sendToTarget();
        }

        @Override // android.speech.IRecognitionListener
        public synchronized void onReadyForSpeech(Bundle noiseParams) {
            Message.obtain(this.mInternalHandler, 5, noiseParams).sendToTarget();
        }

        @Override // android.speech.IRecognitionListener
        public synchronized void onResults(Bundle results) {
            Message.obtain(this.mInternalHandler, 6, results).sendToTarget();
        }

        @Override // android.speech.IRecognitionListener
        public synchronized void onPartialResults(Bundle results) {
            Message.obtain(this.mInternalHandler, 7, results).sendToTarget();
        }

        @Override // android.speech.IRecognitionListener
        public synchronized void onRmsChanged(float rmsdB) {
            Message.obtain(this.mInternalHandler, 8, Float.valueOf(rmsdB)).sendToTarget();
        }

        public synchronized void onEvent(int eventType, Bundle params) {
            Message.obtain(this.mInternalHandler, 9, eventType, eventType, params).sendToTarget();
        }
    }
}
