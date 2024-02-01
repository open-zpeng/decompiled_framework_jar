package android.media;

import android.hardware.cas.V1_0.HidlCasPluginDescriptor;
import android.hardware.cas.V1_0.ICas;
import android.hardware.cas.V1_0.ICasListener;
import android.hardware.cas.V1_0.IMediaCasService;
import android.media.MediaCasException;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IHwBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.util.Singleton;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class MediaCas implements AutoCloseable {
    private static final String TAG = "MediaCas";
    private static final Singleton<IMediaCasService> gDefault = new Singleton<IMediaCasService>() { // from class: android.media.MediaCas.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.util.Singleton
        public IMediaCasService create() {
            try {
                return IMediaCasService.getService();
            } catch (RemoteException e) {
                return null;
            }
        }
    };
    private final ICasListener.Stub mBinder = new ICasListener.Stub() { // from class: android.media.MediaCas.2
        @Override // android.hardware.cas.V1_0.ICasListener
        public void onEvent(int event, int arg, ArrayList<Byte> data) throws RemoteException {
            MediaCas.this.mEventHandler.sendMessage(MediaCas.this.mEventHandler.obtainMessage(0, event, arg, data));
        }
    };
    private EventHandler mEventHandler;
    private HandlerThread mHandlerThread;
    private ICas mICas;
    private EventListener mListener;

    /* loaded from: classes.dex */
    public interface EventListener {
        void onEvent(MediaCas mediaCas, int i, int i2, byte[] bArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized IMediaCasService getService() {
        return gDefault.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void validateInternalStates() {
        if (this.mICas == null) {
            throw new IllegalStateException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void cleanupAndRethrowIllegalState() {
        this.mICas = null;
        throw new IllegalStateException();
    }

    /* loaded from: classes.dex */
    private class EventHandler extends Handler {
        private static final int MSG_CAS_EVENT = 0;

        public EventHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                MediaCas.this.mListener.onEvent(MediaCas.this, msg.arg1, msg.arg2, MediaCas.this.toBytes((ArrayList) msg.obj));
            }
        }
    }

    /* loaded from: classes.dex */
    public static class PluginDescriptor {
        private final int mCASystemId;
        private final String mName;

        private synchronized PluginDescriptor() {
            this.mCASystemId = 65535;
            this.mName = null;
        }

        synchronized PluginDescriptor(HidlCasPluginDescriptor descriptor) {
            this.mCASystemId = descriptor.caSystemId;
            this.mName = descriptor.name;
        }

        public int getSystemId() {
            return this.mCASystemId;
        }

        public String getName() {
            return this.mName;
        }

        public String toString() {
            return "PluginDescriptor {" + this.mCASystemId + ", " + this.mName + "}";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized ArrayList<Byte> toByteArray(byte[] data, int offset, int length) {
        ArrayList<Byte> byteArray = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            byteArray.add(Byte.valueOf(data[offset + i]));
        }
        return byteArray;
    }

    private synchronized ArrayList<Byte> toByteArray(byte[] data) {
        if (data == null) {
            return new ArrayList<>();
        }
        return toByteArray(data, 0, data.length);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized byte[] toBytes(ArrayList<Byte> byteArray) {
        byte[] data = null;
        if (byteArray != null) {
            data = new byte[byteArray.size()];
            for (int i = 0; i < data.length; i++) {
                data[i] = byteArray.get(i).byteValue();
            }
        }
        return data;
    }

    /* loaded from: classes.dex */
    public final class Session implements AutoCloseable {
        final ArrayList<Byte> mSessionId;

        Session(ArrayList<Byte> sessionId) {
            this.mSessionId = sessionId;
        }

        public void setPrivateData(byte[] data) throws MediaCasException {
            MediaCas.this.validateInternalStates();
            try {
                MediaCasException.throwExceptionIfNeeded(MediaCas.this.mICas.setSessionPrivateData(this.mSessionId, MediaCas.this.toByteArray(data, 0, data.length)));
            } catch (RemoteException e) {
                MediaCas.this.cleanupAndRethrowIllegalState();
            }
        }

        public void processEcm(byte[] data, int offset, int length) throws MediaCasException {
            MediaCas.this.validateInternalStates();
            try {
                MediaCasException.throwExceptionIfNeeded(MediaCas.this.mICas.processEcm(this.mSessionId, MediaCas.this.toByteArray(data, offset, length)));
            } catch (RemoteException e) {
                MediaCas.this.cleanupAndRethrowIllegalState();
            }
        }

        public void processEcm(byte[] data) throws MediaCasException {
            processEcm(data, 0, data.length);
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            MediaCas.this.validateInternalStates();
            try {
                MediaCasStateException.throwExceptionIfNeeded(MediaCas.this.mICas.closeSession(this.mSessionId));
            } catch (RemoteException e) {
                MediaCas.this.cleanupAndRethrowIllegalState();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Session createFromSessionId(ArrayList<Byte> sessionId) {
        if (sessionId == null || sessionId.size() == 0) {
            return null;
        }
        return new Session(sessionId);
    }

    public static boolean isSystemIdSupported(int CA_system_id) {
        IMediaCasService service = getService();
        if (service != null) {
            try {
                return service.isSystemIdSupported(CA_system_id);
            } catch (RemoteException e) {
                return false;
            }
        }
        return false;
    }

    public static PluginDescriptor[] enumeratePlugins() {
        IMediaCasService service = getService();
        if (service != null) {
            try {
                ArrayList<HidlCasPluginDescriptor> descriptors = service.enumeratePlugins();
                if (descriptors.size() == 0) {
                    return null;
                }
                PluginDescriptor[] results = new PluginDescriptor[descriptors.size()];
                for (int i = 0; i < results.length; i++) {
                    results[i] = new PluginDescriptor(descriptors.get(i));
                }
                return results;
            } catch (RemoteException e) {
            }
        }
        return null;
    }

    public MediaCas(int CA_system_id) throws MediaCasException.UnsupportedCasException {
        try {
            try {
                this.mICas = getService().createPlugin(CA_system_id, this.mBinder);
                if (this.mICas != null) {
                    return;
                }
                throw new MediaCasException.UnsupportedCasException("Unsupported CA_system_id " + CA_system_id);
            } catch (Exception e) {
                Log.e(TAG, "Failed to create plugin: " + e);
                this.mICas = null;
                if (this.mICas != null) {
                    return;
                }
                throw new MediaCasException.UnsupportedCasException("Unsupported CA_system_id " + CA_system_id);
            }
        } catch (Throwable th) {
            if (this.mICas != null) {
                throw th;
            }
            throw new MediaCasException.UnsupportedCasException("Unsupported CA_system_id " + CA_system_id);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized IHwBinder getBinder() {
        validateInternalStates();
        return this.mICas.asBinder();
    }

    public void setEventListener(EventListener listener, Handler handler) {
        this.mListener = listener;
        if (this.mListener == null) {
            this.mEventHandler = null;
            return;
        }
        Looper looper = handler != null ? handler.getLooper() : null;
        if (looper == null) {
            Looper myLooper = Looper.myLooper();
            looper = myLooper;
            if (myLooper == null) {
                Looper mainLooper = Looper.getMainLooper();
                looper = mainLooper;
                if (mainLooper == null) {
                    if (this.mHandlerThread == null || !this.mHandlerThread.isAlive()) {
                        this.mHandlerThread = new HandlerThread("MediaCasEventThread", -2);
                        this.mHandlerThread.start();
                    }
                    looper = this.mHandlerThread.getLooper();
                }
            }
        }
        this.mEventHandler = new EventHandler(looper);
    }

    public void setPrivateData(byte[] data) throws MediaCasException {
        validateInternalStates();
        try {
            MediaCasException.throwExceptionIfNeeded(this.mICas.setPrivateData(toByteArray(data, 0, data.length)));
        } catch (RemoteException e) {
            cleanupAndRethrowIllegalState();
        }
    }

    /* loaded from: classes.dex */
    private class OpenSessionCallback implements ICas.openSessionCallback {
        public Session mSession;
        public int mStatus;

        private OpenSessionCallback() {
        }

        @Override // android.hardware.cas.V1_0.ICas.openSessionCallback
        public void onValues(int status, ArrayList<Byte> sessionId) {
            this.mStatus = status;
            this.mSession = MediaCas.this.createFromSessionId(sessionId);
        }
    }

    public Session openSession() throws MediaCasException {
        validateInternalStates();
        try {
            OpenSessionCallback cb = new OpenSessionCallback();
            this.mICas.openSession(cb);
            MediaCasException.throwExceptionIfNeeded(cb.mStatus);
            return cb.mSession;
        } catch (RemoteException e) {
            cleanupAndRethrowIllegalState();
            return null;
        }
    }

    public void processEmm(byte[] data, int offset, int length) throws MediaCasException {
        validateInternalStates();
        try {
            MediaCasException.throwExceptionIfNeeded(this.mICas.processEmm(toByteArray(data, offset, length)));
        } catch (RemoteException e) {
            cleanupAndRethrowIllegalState();
        }
    }

    public void processEmm(byte[] data) throws MediaCasException {
        processEmm(data, 0, data.length);
    }

    public void sendEvent(int event, int arg, byte[] data) throws MediaCasException {
        validateInternalStates();
        try {
            MediaCasException.throwExceptionIfNeeded(this.mICas.sendEvent(event, arg, toByteArray(data)));
        } catch (RemoteException e) {
            cleanupAndRethrowIllegalState();
        }
    }

    public void provision(String provisionString) throws MediaCasException {
        validateInternalStates();
        try {
            MediaCasException.throwExceptionIfNeeded(this.mICas.provision(provisionString));
        } catch (RemoteException e) {
            cleanupAndRethrowIllegalState();
        }
    }

    public void refreshEntitlements(int refreshType, byte[] refreshData) throws MediaCasException {
        validateInternalStates();
        try {
            MediaCasException.throwExceptionIfNeeded(this.mICas.refreshEntitlements(refreshType, toByteArray(refreshData)));
        } catch (RemoteException e) {
            cleanupAndRethrowIllegalState();
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        if (this.mICas != null) {
            try {
                this.mICas.release();
            } catch (RemoteException e) {
            } catch (Throwable th) {
                this.mICas = null;
                throw th;
            }
            this.mICas = null;
        }
    }

    protected void finalize() {
        close();
    }
}
