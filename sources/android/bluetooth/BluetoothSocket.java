package android.bluetooth;

import android.annotation.UnsupportedAppUsage;
import android.hardware.contexthub.V1_0.HostEndPoint;
import android.net.LocalSocket;
import android.os.ParcelFileDescriptor;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.Log;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

/* loaded from: classes.dex */
public final class BluetoothSocket implements Closeable {
    static final int BTSOCK_FLAG_NO_SDP = 4;
    @UnsupportedAppUsage
    static final int EADDRINUSE = 98;
    static final int EBADFD = 77;
    static final int MAX_L2CAP_PACKAGE_SIZE = 65535;
    public static final int MAX_RFCOMM_CHANNEL = 30;
    private static final int PROXY_CONNECTION_TIMEOUT = 5000;
    static final int SEC_FLAG_AUTH = 2;
    static final int SEC_FLAG_AUTH_16_DIGIT = 16;
    static final int SEC_FLAG_AUTH_MITM = 8;
    static final int SEC_FLAG_ENCRYPT = 1;
    private static final int SOCK_SIGNAL_SIZE = 20;
    public static final int TYPE_L2CAP = 3;
    public static final int TYPE_L2CAP_BREDR = 3;
    public static final int TYPE_L2CAP_LE = 4;
    public static final int TYPE_RFCOMM = 1;
    public static final int TYPE_SCO = 2;
    private String mAddress;
    private final boolean mAuth;
    private boolean mAuthMitm;
    private BluetoothDevice mDevice;
    private final boolean mEncrypt;
    private boolean mExcludeSdp;
    private int mFd;
    private final BluetoothInputStream mInputStream;
    private ByteBuffer mL2capBuffer;
    private int mMaxRxPacketSize;
    private int mMaxTxPacketSize;
    private boolean mMin16DigitPin;
    private final BluetoothOutputStream mOutputStream;
    @UnsupportedAppUsage
    private ParcelFileDescriptor mPfd;
    @UnsupportedAppUsage
    private int mPort;
    private String mServiceName;
    @UnsupportedAppUsage
    private LocalSocket mSocket;
    private InputStream mSocketIS;
    private OutputStream mSocketOS;
    private volatile SocketState mSocketState;
    private final int mType;
    private final ParcelUuid mUuid;
    private static final String TAG = "BluetoothSocket";
    private static final boolean DBG = Log.isLoggable(TAG, 3);
    private static final boolean VDBG = Log.isLoggable(TAG, 2);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum SocketState {
        INIT,
        CONNECTED,
        LISTENING,
        CLOSED
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BluetoothSocket(int type, int fd, boolean auth, boolean encrypt, BluetoothDevice device, int port, ParcelUuid uuid) throws IOException {
        this(type, fd, auth, encrypt, device, port, uuid, false, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BluetoothSocket(int type, int fd, boolean auth, boolean encrypt, BluetoothDevice device, int port, ParcelUuid uuid, boolean mitm, boolean min16DigitPin) throws IOException {
        this.mExcludeSdp = false;
        this.mAuthMitm = false;
        this.mMin16DigitPin = false;
        this.mL2capBuffer = null;
        this.mMaxTxPacketSize = 0;
        this.mMaxRxPacketSize = 0;
        if (VDBG) {
            Log.d(TAG, "Creating new BluetoothSocket of type: " + type);
        }
        if (type == 1 && uuid == null && fd == -1 && port != -2 && (port < 1 || port > 30)) {
            throw new IOException("Invalid RFCOMM channel: " + port);
        }
        if (uuid != null) {
            this.mUuid = uuid;
        } else {
            this.mUuid = new ParcelUuid(new UUID(0L, 0L));
        }
        this.mType = type;
        this.mAuth = auth;
        this.mAuthMitm = mitm;
        this.mMin16DigitPin = min16DigitPin;
        this.mEncrypt = encrypt;
        this.mDevice = device;
        this.mPort = port;
        this.mFd = fd;
        this.mSocketState = SocketState.INIT;
        if (device == null) {
            this.mAddress = BluetoothAdapter.getDefaultAdapter().getAddress();
        } else {
            this.mAddress = device.getAddress();
        }
        this.mInputStream = new BluetoothInputStream(this);
        this.mOutputStream = new BluetoothOutputStream(this);
    }

    private BluetoothSocket(BluetoothSocket s) {
        this.mExcludeSdp = false;
        this.mAuthMitm = false;
        this.mMin16DigitPin = false;
        this.mL2capBuffer = null;
        this.mMaxTxPacketSize = 0;
        this.mMaxRxPacketSize = 0;
        if (VDBG) {
            Log.d(TAG, "Creating new Private BluetoothSocket of type: " + s.mType);
        }
        this.mUuid = s.mUuid;
        this.mType = s.mType;
        this.mAuth = s.mAuth;
        this.mEncrypt = s.mEncrypt;
        this.mPort = s.mPort;
        this.mInputStream = new BluetoothInputStream(this);
        this.mOutputStream = new BluetoothOutputStream(this);
        this.mMaxRxPacketSize = s.mMaxRxPacketSize;
        this.mMaxTxPacketSize = s.mMaxTxPacketSize;
        this.mServiceName = s.mServiceName;
        this.mExcludeSdp = s.mExcludeSdp;
        this.mAuthMitm = s.mAuthMitm;
        this.mMin16DigitPin = s.mMin16DigitPin;
    }

    private BluetoothSocket acceptSocket(String remoteAddr) throws IOException {
        BluetoothSocket as = new BluetoothSocket(this);
        as.mSocketState = SocketState.CONNECTED;
        FileDescriptor[] fds = this.mSocket.getAncillaryFileDescriptors();
        if (DBG) {
            Log.d(TAG, "socket fd passed by stack fds: " + Arrays.toString(fds));
        }
        if (fds == null || fds.length != 1) {
            Log.e(TAG, "socket fd passed from stack failed, fds: " + Arrays.toString(fds));
            as.close();
            throw new IOException("bt socket acept failed");
        }
        as.mPfd = new ParcelFileDescriptor(fds[0]);
        as.mSocket = LocalSocket.createConnectedLocalSocket(fds[0]);
        as.mSocketIS = as.mSocket.getInputStream();
        as.mSocketOS = as.mSocket.getOutputStream();
        as.mAddress = remoteAddr;
        as.mDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(remoteAddr);
        return as;
    }

    private BluetoothSocket(int type, int fd, boolean auth, boolean encrypt, String address, int port) throws IOException {
        this(type, fd, auth, encrypt, new BluetoothDevice(address), port, null, false, false);
    }

    protected void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    private int getSecurityFlags() {
        int flags = 0;
        if (this.mAuth) {
            flags = 0 | 2;
        }
        if (this.mEncrypt) {
            flags |= 1;
        }
        if (this.mExcludeSdp) {
            flags |= 4;
        }
        if (this.mAuthMitm) {
            flags |= 8;
        }
        if (this.mMin16DigitPin) {
            return flags | 16;
        }
        return flags;
    }

    public BluetoothDevice getRemoteDevice() {
        return this.mDevice;
    }

    public InputStream getInputStream() throws IOException {
        return this.mInputStream;
    }

    public OutputStream getOutputStream() throws IOException {
        return this.mOutputStream;
    }

    public boolean isConnected() {
        return this.mSocketState == SocketState.CONNECTED;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setServiceName(String name) {
        this.mServiceName = name;
    }

    public void connect() throws IOException {
        if (this.mDevice == null) {
            throw new IOException("Connect is called on null device");
        }
        try {
            if (this.mSocketState == SocketState.CLOSED) {
                throw new IOException("socket closed");
            }
            IBluetooth bluetoothProxy = BluetoothAdapter.getDefaultAdapter().getBluetoothService(null);
            if (bluetoothProxy == null) {
                throw new IOException("Bluetooth is off");
            }
            this.mPfd = bluetoothProxy.getSocketManager().connectSocket(this.mDevice, this.mType, this.mUuid, this.mPort, getSecurityFlags());
            synchronized (this) {
                if (DBG) {
                    Log.d(TAG, "connect(), SocketState: " + this.mSocketState + ", mPfd: " + this.mPfd);
                }
                if (this.mSocketState == SocketState.CLOSED) {
                    throw new IOException("socket closed");
                }
                if (this.mPfd == null) {
                    throw new IOException("bt socket connect failed");
                }
                FileDescriptor fd = this.mPfd.getFileDescriptor();
                this.mSocket = LocalSocket.createConnectedLocalSocket(fd);
                this.mSocketIS = this.mSocket.getInputStream();
                this.mSocketOS = this.mSocket.getOutputStream();
            }
            int channel = readInt(this.mSocketIS);
            if (channel <= 0) {
                throw new IOException("bt socket connect failed");
            }
            this.mPort = channel;
            waitSocketSignal(this.mSocketIS);
            synchronized (this) {
                if (this.mSocketState == SocketState.CLOSED) {
                    throw new IOException("bt socket closed");
                }
                this.mSocketState = SocketState.CONNECTED;
            }
        } catch (RemoteException e) {
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
            throw new IOException("unable to send RPC: " + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int bindListen() {
        if (this.mSocketState == SocketState.CLOSED) {
            return 77;
        }
        IBluetooth bluetoothProxy = BluetoothAdapter.getDefaultAdapter().getBluetoothService(null);
        if (bluetoothProxy == null) {
            Log.e(TAG, "bindListen fail, reason: bluetooth is off");
            return -1;
        }
        try {
            if (DBG) {
                Log.d(TAG, "bindListen(): mPort=" + this.mPort + ", mType=" + this.mType);
            }
            this.mPfd = bluetoothProxy.getSocketManager().createSocketChannel(this.mType, this.mServiceName, this.mUuid, this.mPort, getSecurityFlags());
            try {
                synchronized (this) {
                    if (DBG) {
                        Log.d(TAG, "bindListen(), SocketState: " + this.mSocketState + ", mPfd: " + this.mPfd);
                    }
                    if (this.mSocketState != SocketState.INIT) {
                        return 77;
                    }
                    if (this.mPfd == null) {
                        return -1;
                    }
                    FileDescriptor fd = this.mPfd.getFileDescriptor();
                    if (fd == null) {
                        Log.e(TAG, "bindListen(), null file descriptor");
                        return -1;
                    }
                    if (DBG) {
                        Log.d(TAG, "bindListen(), Create LocalSocket");
                    }
                    this.mSocket = LocalSocket.createConnectedLocalSocket(fd);
                    if (DBG) {
                        Log.d(TAG, "bindListen(), new LocalSocket.getInputStream()");
                    }
                    this.mSocketIS = this.mSocket.getInputStream();
                    this.mSocketOS = this.mSocket.getOutputStream();
                    if (DBG) {
                        Log.d(TAG, "bindListen(), readInt mSocketIS: " + this.mSocketIS);
                    }
                    int channel = readInt(this.mSocketIS);
                    synchronized (this) {
                        if (this.mSocketState == SocketState.INIT) {
                            this.mSocketState = SocketState.LISTENING;
                        }
                    }
                    if (DBG) {
                        Log.d(TAG, "bindListen(): channel=" + channel + ", mPort=" + this.mPort);
                    }
                    if (this.mPort <= -1) {
                        this.mPort = channel;
                        return 0;
                    }
                    return 0;
                }
            } catch (IOException e) {
                ParcelFileDescriptor parcelFileDescriptor = this.mPfd;
                if (parcelFileDescriptor != null) {
                    try {
                        parcelFileDescriptor.close();
                    } catch (IOException e1) {
                        Log.e(TAG, "bindListen, close mPfd: " + e1);
                    }
                    this.mPfd = null;
                }
                Log.e(TAG, "bindListen, fail to get port number, exception: " + e);
                return -1;
            }
        } catch (RemoteException e2) {
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BluetoothSocket accept(int timeout) throws IOException {
        BluetoothSocket acceptedSocket;
        if (this.mSocketState != SocketState.LISTENING) {
            throw new IOException("bt socket is not in listen state");
        }
        if (timeout > 0) {
            Log.d(TAG, "accept() set timeout (ms):" + timeout);
            this.mSocket.setSoTimeout(timeout);
        }
        String RemoteAddr = waitSocketSignal(this.mSocketIS);
        if (timeout > 0) {
            this.mSocket.setSoTimeout(0);
        }
        synchronized (this) {
            if (this.mSocketState != SocketState.LISTENING) {
                throw new IOException("bt socket is not in listen state");
            }
            acceptedSocket = acceptSocket(RemoteAddr);
        }
        return acceptedSocket;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int available() throws IOException {
        if (VDBG) {
            Log.d(TAG, "available: " + this.mSocketIS);
        }
        return this.mSocketIS.available();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @UnsupportedAppUsage
    public void flush() throws IOException {
        if (this.mSocketOS == null) {
            throw new IOException("flush is called on null OutputStream");
        }
        if (VDBG) {
            Log.d(TAG, "flush: " + this.mSocketOS);
        }
        this.mSocketOS.flush();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int read(byte[] b, int offset, int length) throws IOException {
        int ret;
        if (VDBG) {
            Log.d(TAG, "read in:  " + this.mSocketIS + " len: " + length);
        }
        int i = this.mType;
        if (i == 3 || i == 4) {
            int bytesToRead = length;
            if (VDBG) {
                Log.v(TAG, "l2cap: read(): offset: " + offset + " length:" + length + "mL2capBuffer= " + this.mL2capBuffer);
            }
            if (this.mL2capBuffer == null) {
                createL2capRxBuffer();
            }
            if (this.mL2capBuffer.remaining() == 0) {
                if (VDBG) {
                    Log.v(TAG, "l2cap buffer empty, refilling...");
                }
                if (fillL2capRxBuffer() == -1) {
                    return -1;
                }
            }
            if (bytesToRead > this.mL2capBuffer.remaining()) {
                bytesToRead = this.mL2capBuffer.remaining();
            }
            if (VDBG) {
                Log.v(TAG, "get(): offset: " + offset + " bytesToRead: " + bytesToRead);
            }
            this.mL2capBuffer.get(b, offset, bytesToRead);
            ret = bytesToRead;
        } else {
            if (VDBG) {
                Log.v(TAG, "default: read(): offset: " + offset + " length:" + length);
            }
            ret = this.mSocketIS.read(b, offset, length);
        }
        if (ret < 0) {
            throw new IOException("bt socket closed, read return: " + ret);
        }
        if (VDBG) {
            Log.d(TAG, "read out:  " + this.mSocketIS + " ret: " + ret);
        }
        return ret;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int write(byte[] b, int offset, int length) throws IOException {
        if (VDBG) {
            Log.d(TAG, "write: " + this.mSocketOS + " length: " + length);
        }
        int i = this.mType;
        if (i == 3 || i == 4) {
            if (length <= this.mMaxTxPacketSize) {
                this.mSocketOS.write(b, offset, length);
            } else {
                if (DBG) {
                    Log.w(TAG, "WARNING: Write buffer larger than L2CAP packet size!\nPacket will be divided into SDU packets of size " + this.mMaxTxPacketSize);
                }
                int tmpOffset = offset;
                int bytesToWrite = length;
                while (bytesToWrite > 0) {
                    int tmpLength = this.mMaxTxPacketSize;
                    if (bytesToWrite <= tmpLength) {
                        tmpLength = bytesToWrite;
                    }
                    this.mSocketOS.write(b, tmpOffset, tmpLength);
                    tmpOffset += tmpLength;
                    bytesToWrite -= tmpLength;
                }
            }
        } else {
            this.mSocketOS.write(b, offset, length);
        }
        if (VDBG) {
            Log.d(TAG, "write out: " + this.mSocketOS + " length: " + length);
        }
        return length;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        Log.d(TAG, "close() this: " + this + ", channel: " + this.mPort + ", mSocketIS: " + this.mSocketIS + ", mSocketOS: " + this.mSocketOS + "mSocket: " + this.mSocket + ", mSocketState: " + this.mSocketState);
        if (this.mSocketState == SocketState.CLOSED) {
            return;
        }
        synchronized (this) {
            if (this.mSocketState == SocketState.CLOSED) {
                return;
            }
            this.mSocketState = SocketState.CLOSED;
            if (this.mSocket != null) {
                if (DBG) {
                    Log.d(TAG, "Closing mSocket: " + this.mSocket);
                }
                this.mSocket.shutdownInput();
                this.mSocket.shutdownOutput();
                this.mSocket.close();
                this.mSocket = null;
            }
            if (this.mPfd != null) {
                this.mPfd.close();
                this.mPfd = null;
            }
        }
    }

    void removeChannel() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getPort() {
        return this.mPort;
    }

    public int getMaxTransmitPacketSize() {
        return this.mMaxTxPacketSize;
    }

    public int getMaxReceivePacketSize() {
        return this.mMaxRxPacketSize;
    }

    public int getConnectionType() {
        int i = this.mType;
        if (i == 4) {
            return 3;
        }
        return i;
    }

    public void setExcludeSdp(boolean excludeSdp) {
        this.mExcludeSdp = excludeSdp;
    }

    public void requestMaximumTxDataLength() throws IOException {
        if (this.mDevice == null) {
            throw new IOException("requestMaximumTxDataLength is called on null device");
        }
        try {
            if (this.mSocketState == SocketState.CLOSED) {
                throw new IOException("socket closed");
            }
            IBluetooth bluetoothProxy = BluetoothAdapter.getDefaultAdapter().getBluetoothService(null);
            if (bluetoothProxy == null) {
                throw new IOException("Bluetooth is off");
            }
            if (DBG) {
                Log.d(TAG, "requestMaximumTxDataLength");
            }
            bluetoothProxy.getSocketManager().requestMaximumTxDataLength(this.mDevice);
        } catch (RemoteException e) {
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
            throw new IOException("unable to send RPC: " + e.getMessage());
        }
    }

    private String convertAddr(byte[] addr) {
        return String.format(Locale.US, "%02X:%02X:%02X:%02X:%02X:%02X", Byte.valueOf(addr[0]), Byte.valueOf(addr[1]), Byte.valueOf(addr[2]), Byte.valueOf(addr[3]), Byte.valueOf(addr[4]), Byte.valueOf(addr[5]));
    }

    private String waitSocketSignal(InputStream is) throws IOException {
        byte[] sig = new byte[20];
        int ret = readAll(is, sig);
        if (VDBG) {
            Log.d(TAG, "waitSocketSignal read 20 bytes signal ret: " + ret);
        }
        ByteBuffer bb = ByteBuffer.wrap(sig);
        bb.order(ByteOrder.nativeOrder());
        int size = bb.getShort();
        if (size != 20) {
            throw new IOException("Connection failure, wrong signal size: " + size);
        }
        byte[] addr = new byte[6];
        bb.get(addr);
        int channel = bb.getInt();
        int status = bb.getInt();
        this.mMaxTxPacketSize = bb.getShort() & HostEndPoint.BROADCAST;
        this.mMaxRxPacketSize = bb.getShort() & HostEndPoint.BROADCAST;
        String RemoteAddr = convertAddr(addr);
        if (VDBG) {
            Log.d(TAG, "waitSocketSignal: sig size: " + size + ", remote addr: " + RemoteAddr + ", channel: " + channel + ", status: " + status + " MaxRxPktSize: " + this.mMaxRxPacketSize + " MaxTxPktSize: " + this.mMaxTxPacketSize);
        }
        if (status != 0) {
            throw new IOException("Connection failure, status: " + status);
        }
        return RemoteAddr;
    }

    private void createL2capRxBuffer() {
        int i = this.mType;
        if (i == 3 || i == 4) {
            if (VDBG) {
                Log.v(TAG, "  Creating mL2capBuffer: mMaxPacketSize: " + this.mMaxRxPacketSize);
            }
            this.mL2capBuffer = ByteBuffer.wrap(new byte[this.mMaxRxPacketSize]);
            if (VDBG) {
                Log.v(TAG, "mL2capBuffer.remaining()" + this.mL2capBuffer.remaining());
            }
            this.mL2capBuffer.limit(0);
            if (VDBG) {
                Log.v(TAG, "mL2capBuffer.remaining() after limit(0):" + this.mL2capBuffer.remaining());
            }
        }
    }

    private int readAll(InputStream is, byte[] b) throws IOException {
        int left = b.length;
        while (left > 0) {
            int ret = is.read(b, b.length - left, left);
            if (ret <= 0) {
                throw new IOException("read failed, socket might closed or timeout, read ret: " + ret);
            }
            left -= ret;
            if (left != 0) {
                Log.w(TAG, "readAll() looping, read partial size: " + (b.length - left) + ", expect size: " + b.length);
            }
        }
        return b.length;
    }

    private int readInt(InputStream is) throws IOException {
        byte[] ibytes = new byte[4];
        int ret = readAll(is, ibytes);
        if (VDBG) {
            Log.d(TAG, "inputStream.read ret: " + ret);
        }
        ByteBuffer bb = ByteBuffer.wrap(ibytes);
        bb.order(ByteOrder.nativeOrder());
        return bb.getInt();
    }

    private int fillL2capRxBuffer() throws IOException {
        this.mL2capBuffer.rewind();
        int ret = this.mSocketIS.read(this.mL2capBuffer.array());
        if (ret == -1) {
            this.mL2capBuffer.limit(0);
            return -1;
        }
        this.mL2capBuffer.limit(ret);
        return ret;
    }
}
