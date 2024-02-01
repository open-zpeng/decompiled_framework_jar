package android.se.omapi;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.se.omapi.ISecureElementListener;
import android.se.omapi.ISecureElementService;
import android.util.Log;
import java.util.HashMap;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public final class SEService {
    public static final int IO_ERROR = 1;
    public static final int NO_SUCH_ELEMENT_ERROR = 2;
    private static final String TAG = "OMAPI.SEService";
    private ServiceConnection mConnection;
    private final Context mContext;
    private volatile ISecureElementService mSecureElementService;
    private SEListener mSEListener = new SEListener();
    private final Object mLock = new Object();
    private final HashMap<String, Reader> mReaders = new HashMap<>();

    /* loaded from: classes2.dex */
    public interface OnConnectedListener {
        void onConnected();
    }

    /* loaded from: classes2.dex */
    private class SEListener extends ISecureElementListener.Stub {
        public Executor mExecutor;
        public OnConnectedListener mListener;

        private SEListener() {
            this.mListener = null;
            this.mExecutor = null;
        }

        @Override // android.se.omapi.ISecureElementListener.Stub, android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public synchronized void onConnected() {
            if (this.mListener != null && this.mExecutor != null) {
                this.mExecutor.execute(new Runnable() { // from class: android.se.omapi.SEService.SEListener.1
                    @Override // java.lang.Runnable
                    public void run() {
                        SEListener.this.mListener.onConnected();
                    }
                });
            }
        }
    }

    public SEService(Context context, Executor executor, OnConnectedListener listener) {
        if (context == null || listener == null || executor == null) {
            throw new NullPointerException("Arguments must not be null");
        }
        this.mContext = context;
        this.mSEListener.mListener = listener;
        this.mSEListener.mExecutor = executor;
        this.mConnection = new ServiceConnection() { // from class: android.se.omapi.SEService.1
            @Override // android.content.ServiceConnection
            public synchronized void onServiceConnected(ComponentName className, IBinder service) {
                SEService.this.mSecureElementService = ISecureElementService.Stub.asInterface(service);
                if (SEService.this.mSEListener != null) {
                    SEService.this.mSEListener.onConnected();
                }
                Log.i(SEService.TAG, "Service onServiceConnected");
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName className) {
                SEService.this.mSecureElementService = null;
                Log.i(SEService.TAG, "Service onServiceDisconnected");
            }
        };
        Intent intent = new Intent(ISecureElementService.class.getName());
        intent.setClassName("com.android.se", "com.android.se.SecureElementService");
        boolean bindingSuccessful = this.mContext.bindService(intent, this.mConnection, 1);
        if (bindingSuccessful) {
            Log.i(TAG, "bindService successful");
        }
    }

    public boolean isConnected() {
        return this.mSecureElementService != null;
    }

    public Reader[] getReaders() {
        int i;
        if (this.mSecureElementService == null) {
            throw new IllegalStateException("service not connected to system");
        }
        try {
            String[] readerNames = this.mSecureElementService.getReaders();
            Reader[] readers = new Reader[readerNames.length];
            int i2 = 0;
            for (String readerName : readerNames) {
                if (this.mReaders.get(readerName) == null) {
                    try {
                        this.mReaders.put(readerName, new Reader(this, readerName, getReader(readerName)));
                        i = i2 + 1;
                    } catch (Exception e) {
                        e = e;
                    }
                    try {
                        readers[i2] = this.mReaders.get(readerName);
                    } catch (Exception e2) {
                        e = e2;
                        i2 = i;
                        Log.e(TAG, "Error adding Reader: " + readerName, e);
                    }
                } else {
                    i = i2 + 1;
                    readers[i2] = this.mReaders.get(readerName);
                }
                i2 = i;
            }
            return readers;
        } catch (RemoteException e3) {
            throw new RuntimeException(e3);
        }
    }

    public void shutdown() {
        synchronized (this.mLock) {
            if (this.mSecureElementService != null) {
                for (Reader reader : this.mReaders.values()) {
                    try {
                        reader.closeSessions();
                    } catch (Exception e) {
                    }
                }
            }
            try {
                this.mContext.unbindService(this.mConnection);
            } catch (IllegalArgumentException e2) {
            }
            this.mSecureElementService = null;
        }
    }

    public String getVersion() {
        return "3.3";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ISecureElementListener getListener() {
        return this.mSEListener;
    }

    private synchronized ISecureElementReader getReader(String name) {
        try {
            return this.mSecureElementService.getReader(name);
        } catch (RemoteException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
}
