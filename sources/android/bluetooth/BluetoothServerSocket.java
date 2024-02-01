package android.bluetooth;

import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import java.io.Closeable;
import java.io.IOException;
/* loaded from: classes.dex */
public final class BluetoothServerSocket implements Closeable {
    private static final boolean DBG = false;
    private static final String TAG = "BluetoothServerSocket";
    private int mChannel;
    private Handler mHandler;
    private int mMessage;
    public private protected final BluetoothSocket mSocket;

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized BluetoothServerSocket(int type, boolean auth, boolean encrypt, int port) throws IOException {
        this.mChannel = port;
        this.mSocket = new BluetoothSocket(type, -1, auth, encrypt, null, port, null);
        if (port == -2) {
            this.mSocket.setExcludeSdp(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized BluetoothServerSocket(int type, boolean auth, boolean encrypt, int port, boolean mitm, boolean min16DigitPin) throws IOException {
        this.mChannel = port;
        this.mSocket = new BluetoothSocket(type, -1, auth, encrypt, null, port, null, mitm, min16DigitPin);
        if (port == -2) {
            this.mSocket.setExcludeSdp(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized BluetoothServerSocket(int type, boolean auth, boolean encrypt, ParcelUuid uuid) throws IOException {
        this.mSocket = new BluetoothSocket(type, -1, auth, encrypt, null, -1, uuid);
        this.mChannel = this.mSocket.getPort();
    }

    public BluetoothSocket accept() throws IOException {
        return accept(-1);
    }

    public BluetoothSocket accept(int timeout) throws IOException {
        return this.mSocket.accept(timeout);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        synchronized (this) {
            if (this.mHandler != null) {
                this.mHandler.obtainMessage(this.mMessage).sendToTarget();
            }
        }
        this.mSocket.close();
    }

    synchronized void setCloseHandler(Handler handler, int message) {
        this.mHandler = handler;
        this.mMessage = message;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setServiceName(String serviceName) {
        this.mSocket.setServiceName(serviceName);
    }

    public synchronized int getChannel() {
        return this.mChannel;
    }

    public synchronized int getPsm() {
        return this.mChannel;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setChannel(int newChannel) {
        if (this.mSocket != null && this.mSocket.getPort() != newChannel) {
            Log.w(TAG, "The port set is different that the underlying port. mSocket.getPort(): " + this.mSocket.getPort() + " requested newChannel: " + newChannel);
        }
        this.mChannel = newChannel;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ServerSocket: Type: ");
        switch (this.mSocket.getConnectionType()) {
            case 1:
                sb.append("TYPE_RFCOMM");
                break;
            case 2:
                sb.append("TYPE_SCO");
                break;
            case 3:
                sb.append("TYPE_L2CAP");
                break;
            case 4:
                sb.append("TYPE_L2CAP_LE");
                break;
        }
        sb.append(" Channel: ");
        sb.append(this.mChannel);
        return sb.toString();
    }
}
