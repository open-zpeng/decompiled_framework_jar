package android.media.midi;

import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.midi.MidiDispatcher;
import dalvik.system.CloseGuard;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import libcore.io.IoUtils;

/* loaded from: classes2.dex */
public final class MidiOutputPort extends MidiSender implements Closeable {
    private static final String TAG = "MidiOutputPort";
    private IMidiDeviceServer mDeviceServer;
    private final MidiDispatcher mDispatcher;
    private final CloseGuard mGuard;
    private final FileInputStream mInputStream;
    private boolean mIsClosed;
    private final int mPortNumber;
    private final Thread mThread;
    private final IBinder mToken;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MidiOutputPort(IMidiDeviceServer server, IBinder token, FileDescriptor fd, int portNumber) {
        this.mDispatcher = new MidiDispatcher();
        this.mGuard = CloseGuard.get();
        this.mThread = new Thread() { // from class: android.media.midi.MidiOutputPort.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                byte[] buffer = new byte[1024];
                while (true) {
                    try {
                        int count = MidiOutputPort.this.mInputStream.read(buffer);
                        if (count < 0) {
                            break;
                        }
                        int packetType = MidiPortImpl.getPacketType(buffer, count);
                        if (packetType == 1) {
                            int offset = MidiPortImpl.getDataOffset(buffer, count);
                            int size = MidiPortImpl.getDataSize(buffer, count);
                            long timestamp = MidiPortImpl.getPacketTimestamp(buffer, count);
                            MidiOutputPort.this.mDispatcher.send(buffer, offset, size, timestamp);
                        } else if (packetType == 2) {
                            MidiOutputPort.this.mDispatcher.flush();
                        } else {
                            Log.e(MidiOutputPort.TAG, "Unknown packet type " + packetType);
                        }
                    } catch (IOException e) {
                    } catch (Throwable th) {
                        IoUtils.closeQuietly(MidiOutputPort.this.mInputStream);
                        throw th;
                    }
                }
                IoUtils.closeQuietly(MidiOutputPort.this.mInputStream);
            }
        };
        this.mDeviceServer = server;
        this.mToken = token;
        this.mPortNumber = portNumber;
        this.mInputStream = new ParcelFileDescriptor.AutoCloseInputStream(new ParcelFileDescriptor(fd));
        this.mThread.start();
        this.mGuard.open("close");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MidiOutputPort(FileDescriptor fd, int portNumber) {
        this(null, null, fd, portNumber);
    }

    public final int getPortNumber() {
        return this.mPortNumber;
    }

    @Override // android.media.midi.MidiSender
    public void onConnect(MidiReceiver receiver) {
        this.mDispatcher.getSender().connect(receiver);
    }

    @Override // android.media.midi.MidiSender
    public void onDisconnect(MidiReceiver receiver) {
        this.mDispatcher.getSender().disconnect(receiver);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        synchronized (this.mGuard) {
            if (this.mIsClosed) {
                return;
            }
            this.mGuard.close();
            this.mInputStream.close();
            if (this.mDeviceServer != null) {
                try {
                    this.mDeviceServer.closePort(this.mToken);
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in MidiOutputPort.close()");
                }
            }
            this.mIsClosed = true;
        }
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mGuard != null) {
                this.mGuard.warnIfOpen();
            }
            this.mDeviceServer = null;
            close();
        } finally {
            super.finalize();
        }
    }
}
