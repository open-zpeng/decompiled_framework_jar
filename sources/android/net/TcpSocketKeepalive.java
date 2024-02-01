package android.net;

import android.net.SocketKeepalive;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import java.io.FileDescriptor;
import java.util.concurrent.Executor;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class TcpSocketKeepalive extends SocketKeepalive {
    /* JADX INFO: Access modifiers changed from: package-private */
    public TcpSocketKeepalive(IConnectivityManager service, Network network, ParcelFileDescriptor pfd, Executor executor, SocketKeepalive.Callback callback) {
        super(service, network, pfd, executor, callback);
    }

    @Override // android.net.SocketKeepalive
    void startImpl(final int intervalSec) {
        this.mExecutor.execute(new Runnable() { // from class: android.net.-$$Lambda$TcpSocketKeepalive$E1MP45uBTM6jPfrxAAqXFllEmAA
            @Override // java.lang.Runnable
            public final void run() {
                TcpSocketKeepalive.this.lambda$startImpl$0$TcpSocketKeepalive(intervalSec);
            }
        });
    }

    public /* synthetic */ void lambda$startImpl$0$TcpSocketKeepalive(int intervalSec) {
        try {
            FileDescriptor fd = this.mPfd.getFileDescriptor();
            this.mService.startTcpKeepalive(this.mNetwork, fd, intervalSec, this.mCallback);
        } catch (RemoteException e) {
            Log.e("SocketKeepalive", "Error starting packet keepalive: ", e);
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.net.SocketKeepalive
    void stopImpl() {
        this.mExecutor.execute(new Runnable() { // from class: android.net.-$$Lambda$TcpSocketKeepalive$XcFd1FxqMQjF6WWgzFIVR4hV2xk
            @Override // java.lang.Runnable
            public final void run() {
                TcpSocketKeepalive.this.lambda$stopImpl$1$TcpSocketKeepalive();
            }
        });
    }

    public /* synthetic */ void lambda$stopImpl$1$TcpSocketKeepalive() {
        try {
            if (this.mSlot != null) {
                this.mService.stopKeepalive(this.mNetwork, this.mSlot.intValue());
            }
        } catch (RemoteException e) {
            Log.e("SocketKeepalive", "Error stopping packet keepalive: ", e);
            throw e.rethrowFromSystemServer();
        }
    }
}
