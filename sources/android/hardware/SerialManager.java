package android.hardware;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import java.io.IOException;

/* loaded from: classes.dex */
public class SerialManager {
    private static final String TAG = "SerialManager";
    private final Context mContext;
    private final ISerialManager mService;

    public SerialManager(Context context, ISerialManager service) {
        this.mContext = context;
        this.mService = service;
    }

    @UnsupportedAppUsage
    public String[] getSerialPorts() {
        try {
            return this.mService.getSerialPorts();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public SerialPort openSerialPort(String name, int speed) throws IOException {
        try {
            ParcelFileDescriptor pfd = this.mService.openSerialPort(name);
            if (pfd != null) {
                SerialPort port = new SerialPort(name);
                port.open(pfd, speed);
                return port;
            }
            throw new IOException("Could not open serial port " + name);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
