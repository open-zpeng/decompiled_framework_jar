package android.debug;

import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

/* loaded from: classes.dex */
public class AdbManager {
    private static final String TAG = "AdbManager";
    private final IBinder mBinder = new Binder();
    private final Context mContext;
    private final IAdbManager mService;

    public AdbManager(Context context, IAdbManager service) {
        this.mContext = context;
        this.mService = service;
    }

    public boolean isDebugEnabled() {
        try {
            return this.mService.isDebugEnabled();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setDebugStatus(boolean status) {
        try {
            this.mService.setDebugStatus(status);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void registerXpUsbMode() {
        try {
            this.mService.registerXpUsbMode(this.mBinder);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void unregisterXpUsbMode() {
        try {
            this.mService.unregisterXpUsbMode(this.mBinder);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void enableXpUsbDevice(boolean enable) {
        try {
            this.mService.enableXpUsbDevice(enable);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isXpUsbDevice() {
        try {
            return this.mService.isXpUsbDevice();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
