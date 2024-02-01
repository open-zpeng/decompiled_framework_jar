package android.net;

import android.net.SocketKeepalive;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import java.net.InetAddress;
import java.util.concurrent.Executor;

/* loaded from: classes2.dex */
public final class NattSocketKeepalive extends SocketKeepalive {
    public static final int NATT_PORT = 4500;
    private final InetAddress mDestination;
    private final int mResourceId;
    private final InetAddress mSource;

    /* JADX INFO: Access modifiers changed from: package-private */
    public NattSocketKeepalive(IConnectivityManager service, Network network, ParcelFileDescriptor pfd, int resourceId, InetAddress source, InetAddress destination, Executor executor, SocketKeepalive.Callback callback) {
        super(service, network, pfd, executor, callback);
        this.mSource = source;
        this.mDestination = destination;
        this.mResourceId = resourceId;
    }

    @Override // android.net.SocketKeepalive
    void startImpl(final int intervalSec) {
        this.mExecutor.execute(new Runnable() { // from class: android.net.-$$Lambda$NattSocketKeepalive$7nsE-7bVdhw33oN4gmk8WVi-r9U
            @Override // java.lang.Runnable
            public final void run() {
                NattSocketKeepalive.this.lambda$startImpl$0$NattSocketKeepalive(intervalSec);
            }
        });
    }

    public /* synthetic */ void lambda$startImpl$0$NattSocketKeepalive(int intervalSec) {
        try {
            this.mService.startNattKeepaliveWithFd(this.mNetwork, this.mPfd.getFileDescriptor(), this.mResourceId, intervalSec, this.mCallback, this.mSource.getHostAddress(), this.mDestination.getHostAddress());
        } catch (RemoteException e) {
            Log.e("SocketKeepalive", "Error starting socket keepalive: ", e);
            throw e.rethrowFromSystemServer();
        }
    }

    @Override // android.net.SocketKeepalive
    void stopImpl() {
        this.mExecutor.execute(new Runnable() { // from class: android.net.-$$Lambda$NattSocketKeepalive$60CcdfQ34rdXme76td_j4bbtPHU
            @Override // java.lang.Runnable
            public final void run() {
                NattSocketKeepalive.this.lambda$stopImpl$1$NattSocketKeepalive();
            }
        });
    }

    public /* synthetic */ void lambda$stopImpl$1$NattSocketKeepalive() {
        try {
            if (this.mSlot != null) {
                this.mService.stopKeepalive(this.mNetwork, this.mSlot.intValue());
            }
        } catch (RemoteException e) {
            Log.e("SocketKeepalive", "Error stopping socket keepalive: ", e);
            throw e.rethrowFromSystemServer();
        }
    }
}
