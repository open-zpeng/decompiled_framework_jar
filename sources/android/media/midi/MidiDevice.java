package android.media.midi;

import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import dalvik.system.CloseGuard;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.HashSet;
import libcore.io.IoUtils;
/* loaded from: classes.dex */
public final class MidiDevice implements Closeable {
    private static final String TAG = "MidiDevice";
    private static HashSet<MidiDevice> mMirroredDevices;
    private final IBinder mClientToken;
    private final MidiDeviceInfo mDeviceInfo;
    private final IMidiDeviceServer mDeviceServer;
    private final IBinder mDeviceToken;
    private final CloseGuard mGuard = CloseGuard.get();
    private boolean mIsDeviceClosed;
    private final IMidiManager mMidiManager;
    private long mNativeHandle;

    private native long native_mirrorToNative(IBinder iBinder, int i);

    private native void native_removeFromNative(long j);

    static {
        System.loadLibrary("media_jni");
        mMirroredDevices = new HashSet<>();
    }

    /* loaded from: classes.dex */
    public class MidiConnection implements Closeable {
        private final CloseGuard mGuard = CloseGuard.get();
        private final IMidiDeviceServer mInputPortDeviceServer;
        private final IBinder mInputPortToken;
        private boolean mIsClosed;
        private final IBinder mOutputPortToken;

        MidiConnection(IBinder outputPortToken, MidiInputPort inputPort) {
            this.mInputPortDeviceServer = inputPort.getDeviceServer();
            this.mInputPortToken = inputPort.getToken();
            this.mOutputPortToken = outputPortToken;
            this.mGuard.open("close");
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            synchronized (this.mGuard) {
                if (this.mIsClosed) {
                    return;
                }
                this.mGuard.close();
                try {
                    this.mInputPortDeviceServer.closePort(this.mInputPortToken);
                    MidiDevice.this.mDeviceServer.closePort(this.mOutputPortToken);
                } catch (RemoteException e) {
                    Log.e(MidiDevice.TAG, "RemoteException in MidiConnection.close");
                }
                this.mIsClosed = true;
            }
        }

        protected void finalize() throws Throwable {
            try {
                if (this.mGuard != null) {
                    this.mGuard.warnIfOpen();
                }
                close();
            } finally {
                super.finalize();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized MidiDevice(MidiDeviceInfo deviceInfo, IMidiDeviceServer server, IMidiManager midiManager, IBinder clientToken, IBinder deviceToken) {
        this.mDeviceInfo = deviceInfo;
        this.mDeviceServer = server;
        this.mMidiManager = midiManager;
        this.mClientToken = clientToken;
        this.mDeviceToken = deviceToken;
        this.mGuard.open("close");
    }

    public MidiDeviceInfo getInfo() {
        return this.mDeviceInfo;
    }

    public MidiInputPort openInputPort(int portNumber) {
        if (this.mIsDeviceClosed) {
            return null;
        }
        try {
            IBinder token = new Binder();
            FileDescriptor fd = this.mDeviceServer.openInputPort(token, portNumber);
            if (fd == null) {
                return null;
            }
            return new MidiInputPort(this.mDeviceServer, token, fd, portNumber);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in openInputPort");
            return null;
        }
    }

    public MidiOutputPort openOutputPort(int portNumber) {
        if (this.mIsDeviceClosed) {
            return null;
        }
        try {
            IBinder token = new Binder();
            FileDescriptor fd = this.mDeviceServer.openOutputPort(token, portNumber);
            if (fd == null) {
                return null;
            }
            return new MidiOutputPort(this.mDeviceServer, token, fd, portNumber);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in openOutputPort");
            return null;
        }
    }

    public MidiConnection connectPorts(MidiInputPort inputPort, int outputPortNumber) {
        FileDescriptor fd;
        if (outputPortNumber < 0 || outputPortNumber >= this.mDeviceInfo.getOutputPortCount()) {
            throw new IllegalArgumentException("outputPortNumber out of range");
        }
        if (this.mIsDeviceClosed || (fd = inputPort.claimFileDescriptor()) == null) {
            return null;
        }
        try {
            IBinder token = new Binder();
            int calleePid = this.mDeviceServer.connectPorts(token, fd, outputPortNumber);
            if (calleePid != Process.myPid()) {
                IoUtils.closeQuietly(fd);
            }
            return new MidiConnection(token, inputPort);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in connectPorts");
            return null;
        }
    }

    public synchronized long mirrorToNative() throws IOException {
        if (this.mIsDeviceClosed || this.mNativeHandle != 0) {
            return 0L;
        }
        this.mNativeHandle = native_mirrorToNative(this.mDeviceServer.asBinder(), this.mDeviceInfo.getId());
        if (this.mNativeHandle == 0) {
            throw new IOException("Failed mirroring to native");
        }
        synchronized (mMirroredDevices) {
            mMirroredDevices.add(this);
        }
        return this.mNativeHandle;
    }

    public synchronized void removeFromNative() {
        if (this.mNativeHandle == 0) {
            return;
        }
        synchronized (this.mGuard) {
            native_removeFromNative(this.mNativeHandle);
            this.mNativeHandle = 0L;
        }
        synchronized (mMirroredDevices) {
            mMirroredDevices.remove(this);
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        synchronized (this.mGuard) {
            if (!this.mIsDeviceClosed) {
                removeFromNative();
                this.mGuard.close();
                this.mIsDeviceClosed = true;
                try {
                    this.mMidiManager.closeDevice(this.mClientToken, this.mDeviceToken);
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in closeDevice");
                }
            }
        }
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mGuard != null) {
                this.mGuard.warnIfOpen();
            }
            close();
        } finally {
            super.finalize();
        }
    }

    public String toString() {
        return "MidiDevice: " + this.mDeviceInfo.toString();
    }
}
