package android.net;

import android.content.Context;
import android.net.IEthernetServiceListener;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes2.dex */
public class EthernetManager {
    private static final int MSG_AVAILABILITY_CHANGED = 1000;
    private static final String TAG = "EthernetManager";
    private final Context mContext;
    private final IEthernetManager mService;
    private final Handler mHandler = new Handler() { // from class: android.net.EthernetManager.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            if (msg.what == 1000) {
                boolean isAvailable = msg.arg1 == 1;
                Iterator it = EthernetManager.this.mListeners.iterator();
                while (it.hasNext()) {
                    Listener listener = (Listener) it.next();
                    listener.onAvailabilityChanged((String) msg.obj, isAvailable);
                }
            }
        }
    };
    private final ArrayList<Listener> mListeners = new ArrayList<>();
    private final IEthernetServiceListener.Stub mServiceListener = new IEthernetServiceListener.Stub() { // from class: android.net.EthernetManager.2
        @Override // android.net.IEthernetServiceListener
        public void onAvailabilityChanged(String iface, boolean isAvailable) {
            EthernetManager.this.mHandler.obtainMessage(1000, isAvailable ? 1 : 0, 0, iface).sendToTarget();
        }
    };

    /* loaded from: classes2.dex */
    public interface Listener {
        private protected void onAvailabilityChanged(String str, boolean z);
    }

    public synchronized EthernetManager(Context context, IEthernetManager service) {
        this.mContext = context;
        this.mService = service;
    }

    private protected IpConfiguration getConfiguration(String iface) {
        try {
            return this.mService.getConfiguration(iface);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private protected void setConfiguration(String iface, IpConfiguration config) {
        try {
            this.mService.setConfiguration(iface, config);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private protected boolean isAvailable() {
        return getAvailableInterfaces().length > 0;
    }

    private protected boolean isAvailable(String iface) {
        try {
            return this.mService.isAvailable(iface);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private protected void addListener(Listener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }
        this.mListeners.add(listener);
        if (this.mListeners.size() == 1) {
            try {
                this.mService.addListener(this.mServiceListener);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    private protected String[] getAvailableInterfaces() {
        try {
            return this.mService.getAvailableInterfaces();
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    private protected void removeListener(Listener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }
        this.mListeners.remove(listener);
        if (this.mListeners.isEmpty()) {
            try {
                this.mService.removeListener(this.mServiceListener);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }
}
