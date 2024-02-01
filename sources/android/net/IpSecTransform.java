package android.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.IIpSecService;
import android.net.IpSecManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceSpecificException;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import dalvik.system.CloseGuard;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.InetAddress;
/* loaded from: classes2.dex */
public final class IpSecTransform implements AutoCloseable {
    public static final int ENCAP_ESPINUDP = 2;
    public static final int ENCAP_ESPINUDP_NON_IKE = 1;
    public static final int ENCAP_NONE = 0;
    public static final int MODE_TRANSPORT = 0;
    public static final int MODE_TUNNEL = 1;
    private static final String TAG = "IpSecTransform";
    private Handler mCallbackHandler;
    private final IpSecConfig mConfig;
    private final Context mContext;
    private ConnectivityManager.PacketKeepalive mKeepalive;
    private NattKeepaliveCallback mUserKeepaliveCallback;
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private final ConnectivityManager.PacketKeepaliveCallback mKeepaliveCallback = new AnonymousClass1();
    private int mResourceId = -1;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface EncapType {
    }

    @VisibleForTesting
    public synchronized IpSecTransform(Context context, IpSecConfig config) {
        this.mContext = context;
        this.mConfig = new IpSecConfig(config);
    }

    private synchronized IIpSecService getIpSecService() {
        IBinder b = ServiceManager.getService("ipsec");
        if (b == null) {
            throw new RemoteException("Failed to connect to IpSecService").rethrowAsRuntimeException();
        }
        return IIpSecService.Stub.asInterface(b);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private synchronized void checkResultStatus(int status) throws IOException, IpSecManager.ResourceUnavailableException, IpSecManager.SpiUnavailableException {
        switch (status) {
            case 0:
                return;
            case 1:
                throw new IpSecManager.ResourceUnavailableException("Failed to allocate a new IpSecTransform");
            case 2:
                Log.wtf(TAG, "Attempting to use an SPI that was somehow not reserved");
                break;
        }
        throw new IllegalStateException("Failed to Create a Transform with status code " + status);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized IpSecTransform activate() throws IOException, IpSecManager.ResourceUnavailableException, IpSecManager.SpiUnavailableException {
        try {
            try {
                try {
                    IIpSecService svc = getIpSecService();
                    IpSecTransformResponse result = svc.createTransform(this.mConfig, new Binder(), this.mContext.getOpPackageName());
                    int status = result.status;
                    checkResultStatus(status);
                    this.mResourceId = result.resourceId;
                    Log.d(TAG, "Added Transform with Id " + this.mResourceId);
                    this.mCloseGuard.open("build");
                } catch (RemoteException e) {
                    throw e.rethrowAsRuntimeException();
                }
            } catch (ServiceSpecificException e2) {
                throw IpSecManager.rethrowUncheckedExceptionFromServiceSpecificException(e2);
            }
        } catch (Throwable th) {
            throw th;
        }
        return this;
    }

    @VisibleForTesting
    public static synchronized boolean equals(IpSecTransform lhs, IpSecTransform rhs) {
        return (lhs == null || rhs == null) ? lhs == rhs : IpSecConfig.equals(lhs.getConfig(), rhs.getConfig()) && lhs.mResourceId == rhs.mResourceId;
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        Log.d(TAG, "Removing Transform with Id " + this.mResourceId);
        try {
            if (this.mResourceId == -1) {
                this.mCloseGuard.close();
                return;
            }
            try {
                IIpSecService svc = getIpSecService();
                svc.deleteTransform(this.mResourceId);
                stopNattKeepalive();
            } catch (RemoteException e) {
                throw e.rethrowAsRuntimeException();
            } catch (Exception e2) {
                Log.e(TAG, "Failed to close " + this + ", Exception=" + e2);
            }
        } finally {
            this.mResourceId = -1;
            this.mCloseGuard.close();
        }
    }

    protected void finalize() throws Throwable {
        if (this.mCloseGuard != null) {
            this.mCloseGuard.warnIfOpen();
        }
        close();
    }

    synchronized IpSecConfig getConfig() {
        return this.mConfig;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: android.net.IpSecTransform$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends ConnectivityManager.PacketKeepaliveCallback {
        AnonymousClass1() {
        }

        public void onStarted() {
            synchronized (this) {
                IpSecTransform.this.mCallbackHandler.post(new Runnable() { // from class: android.net.-$$Lambda$IpSecTransform$1$zl9bpxiE2uj_QuCOkuJ091wPuwo
                    @Override // java.lang.Runnable
                    public final void run() {
                        IpSecTransform.this.mUserKeepaliveCallback.onStarted();
                    }
                });
            }
        }

        public void onStopped() {
            synchronized (this) {
                IpSecTransform.this.mKeepalive = null;
                IpSecTransform.this.mCallbackHandler.post(new Runnable() { // from class: android.net.-$$Lambda$IpSecTransform$1$Rc3lbWP51o1kJRHwkpVUEV1G_d8
                    @Override // java.lang.Runnable
                    public final void run() {
                        IpSecTransform.this.mUserKeepaliveCallback.onStopped();
                    }
                });
            }
        }

        public void onError(final int error) {
            synchronized (this) {
                IpSecTransform.this.mKeepalive = null;
                IpSecTransform.this.mCallbackHandler.post(new Runnable() { // from class: android.net.-$$Lambda$IpSecTransform$1$_ae2VrMToKvertNlEIezU0bdvXE
                    @Override // java.lang.Runnable
                    public final void run() {
                        IpSecTransform.this.mUserKeepaliveCallback.onError(error);
                    }
                });
            }
        }
    }

    @VisibleForTesting
    public synchronized int getResourceId() {
        return this.mResourceId;
    }

    /* loaded from: classes2.dex */
    public static class NattKeepaliveCallback {
        public static final int ERROR_HARDWARE_ERROR = 3;
        public static final int ERROR_HARDWARE_UNSUPPORTED = 2;
        public static final int ERROR_INVALID_NETWORK = 1;

        public synchronized void onStarted() {
        }

        public synchronized void onStopped() {
        }

        public synchronized void onError(int error) {
        }
    }

    public synchronized void startNattKeepalive(NattKeepaliveCallback userCallback, int intervalSeconds, Handler handler) throws IOException {
        Preconditions.checkNotNull(userCallback);
        if (intervalSeconds < 20 || intervalSeconds > 3600) {
            throw new IllegalArgumentException("Invalid NAT-T keepalive interval");
        }
        Preconditions.checkNotNull(handler);
        if (this.mResourceId == -1) {
            throw new IllegalStateException("Packet keepalive cannot be started for an inactive transform");
        }
        synchronized (this.mKeepaliveCallback) {
            if (this.mKeepaliveCallback != null) {
                throw new IllegalStateException("Keepalive already active");
            }
            this.mUserKeepaliveCallback = userCallback;
            ConnectivityManager cm = (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            this.mKeepalive = cm.startNattKeepalive(this.mConfig.getNetwork(), intervalSeconds, this.mKeepaliveCallback, NetworkUtils.numericToInetAddress(this.mConfig.getSourceAddress()), ConnectivityManager.PacketKeepalive.NATT_PORT, NetworkUtils.numericToInetAddress(this.mConfig.getDestinationAddress()));
            this.mCallbackHandler = handler;
        }
    }

    public synchronized void stopNattKeepalive() {
        synchronized (this.mKeepaliveCallback) {
            if (this.mKeepalive == null) {
                Log.e(TAG, "No active keepalive to stop");
            } else {
                this.mKeepalive.stop();
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        private IpSecConfig mConfig;
        private Context mContext;

        public Builder setEncryption(IpSecAlgorithm algo) {
            Preconditions.checkNotNull(algo);
            this.mConfig.setEncryption(algo);
            return this;
        }

        public Builder setAuthentication(IpSecAlgorithm algo) {
            Preconditions.checkNotNull(algo);
            this.mConfig.setAuthentication(algo);
            return this;
        }

        public Builder setAuthenticatedEncryption(IpSecAlgorithm algo) {
            Preconditions.checkNotNull(algo);
            this.mConfig.setAuthenticatedEncryption(algo);
            return this;
        }

        public Builder setIpv4Encapsulation(IpSecManager.UdpEncapsulationSocket localSocket, int remotePort) {
            Preconditions.checkNotNull(localSocket);
            this.mConfig.setEncapType(2);
            if (localSocket.getResourceId() == -1) {
                throw new IllegalArgumentException("Invalid UdpEncapsulationSocket");
            }
            this.mConfig.setEncapSocketResourceId(localSocket.getResourceId());
            this.mConfig.setEncapRemotePort(remotePort);
            return this;
        }

        public IpSecTransform buildTransportModeTransform(InetAddress sourceAddress, IpSecManager.SecurityParameterIndex spi) throws IpSecManager.ResourceUnavailableException, IpSecManager.SpiUnavailableException, IOException {
            Preconditions.checkNotNull(sourceAddress);
            Preconditions.checkNotNull(spi);
            if (spi.getResourceId() == -1) {
                throw new IllegalArgumentException("Invalid SecurityParameterIndex");
            }
            this.mConfig.setMode(0);
            this.mConfig.setSourceAddress(sourceAddress.getHostAddress());
            this.mConfig.setSpiResourceId(spi.getResourceId());
            return new IpSecTransform(this.mContext, this.mConfig).activate();
        }

        public synchronized IpSecTransform buildTunnelModeTransform(InetAddress sourceAddress, IpSecManager.SecurityParameterIndex spi) throws IpSecManager.ResourceUnavailableException, IpSecManager.SpiUnavailableException, IOException {
            Preconditions.checkNotNull(sourceAddress);
            Preconditions.checkNotNull(spi);
            if (spi.getResourceId() == -1) {
                throw new IllegalArgumentException("Invalid SecurityParameterIndex");
            }
            this.mConfig.setMode(1);
            this.mConfig.setSourceAddress(sourceAddress.getHostAddress());
            this.mConfig.setSpiResourceId(spi.getResourceId());
            return new IpSecTransform(this.mContext, this.mConfig).activate();
        }

        public Builder(Context context) {
            Preconditions.checkNotNull(context);
            this.mContext = context;
            this.mConfig = new IpSecConfig();
        }
    }

    public String toString() {
        return "IpSecTransform{resourceId=" + this.mResourceId + "}";
    }
}
