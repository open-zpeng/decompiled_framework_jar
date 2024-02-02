package android.net;

import android.content.Context;
import android.os.Binder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceSpecificException;
import android.system.ErrnoException;
import android.system.OsConstants;
import android.util.AndroidException;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import dalvik.system.CloseGuard;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
/* loaded from: classes2.dex */
public final class IpSecManager {
    public static final int DIRECTION_IN = 0;
    public static final int DIRECTION_OUT = 1;
    public static final int INVALID_RESOURCE_ID = -1;
    public static final int INVALID_SECURITY_PARAMETER_INDEX = 0;
    private static final String TAG = "IpSecManager";
    private final Context mContext;
    private final IIpSecService mService;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface PolicyDirection {
    }

    /* loaded from: classes2.dex */
    public interface Status {
        public static final int OK = 0;
        public static final int RESOURCE_UNAVAILABLE = 1;
        public static final int SPI_UNAVAILABLE = 2;
    }

    /* loaded from: classes2.dex */
    public static final class SpiUnavailableException extends AndroidException {
        private final int mSpi;

        synchronized SpiUnavailableException(String msg, int spi) {
            super(msg + " (spi: " + spi + ")");
            this.mSpi = spi;
        }

        public int getSpi() {
            return this.mSpi;
        }
    }

    /* loaded from: classes2.dex */
    public static final class ResourceUnavailableException extends AndroidException {
        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized ResourceUnavailableException(String msg) {
            super(msg);
        }
    }

    /* loaded from: classes2.dex */
    public static final class SecurityParameterIndex implements AutoCloseable {
        private final CloseGuard mCloseGuard;
        private final InetAddress mDestinationAddress;
        private int mResourceId;
        private final IIpSecService mService;
        private int mSpi;

        public int getSpi() {
            return this.mSpi;
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            try {
                try {
                    try {
                        this.mService.releaseSecurityParameterIndex(this.mResourceId);
                    } catch (RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                } catch (Exception e2) {
                    Log.e(IpSecManager.TAG, "Failed to close " + this + ", Exception=" + e2);
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

        private synchronized SecurityParameterIndex(IIpSecService service, InetAddress destinationAddress, int spi) throws ResourceUnavailableException, SpiUnavailableException {
            this.mCloseGuard = CloseGuard.get();
            this.mSpi = 0;
            this.mResourceId = -1;
            this.mService = service;
            this.mDestinationAddress = destinationAddress;
            try {
                IpSecSpiResponse result = this.mService.allocateSecurityParameterIndex(destinationAddress.getHostAddress(), spi, new Binder());
                if (result == null) {
                    throw new NullPointerException("Received null response from IpSecService");
                }
                int status = result.status;
                switch (status) {
                    case 0:
                        this.mSpi = result.spi;
                        this.mResourceId = result.resourceId;
                        if (this.mSpi != 0) {
                            if (this.mResourceId == -1) {
                                throw new RuntimeException("Invalid Resource ID returned by IpSecService: " + status);
                            }
                            this.mCloseGuard.open("open");
                            return;
                        }
                        throw new RuntimeException("Invalid SPI returned by IpSecService: " + status);
                    case 1:
                        throw new ResourceUnavailableException("No more SPIs may be allocated by this requester.");
                    case 2:
                        throw new SpiUnavailableException("Requested SPI is unavailable", spi);
                    default:
                        throw new RuntimeException("Unknown status returned by IpSecService: " + status);
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        @VisibleForTesting
        public synchronized int getResourceId() {
            return this.mResourceId;
        }

        public String toString() {
            return "SecurityParameterIndex{spi=" + this.mSpi + ",resourceId=" + this.mResourceId + "}";
        }
    }

    public SecurityParameterIndex allocateSecurityParameterIndex(InetAddress destinationAddress) throws ResourceUnavailableException {
        try {
            return new SecurityParameterIndex(this.mService, destinationAddress, 0);
        } catch (SpiUnavailableException e) {
            throw new ResourceUnavailableException("No SPIs available");
        } catch (ServiceSpecificException e2) {
            throw rethrowUncheckedExceptionFromServiceSpecificException(e2);
        }
    }

    public SecurityParameterIndex allocateSecurityParameterIndex(InetAddress destinationAddress, int requestedSpi) throws SpiUnavailableException, ResourceUnavailableException {
        if (requestedSpi == 0) {
            throw new IllegalArgumentException("Requested SPI must be a valid (non-zero) SPI");
        }
        try {
            return new SecurityParameterIndex(this.mService, destinationAddress, requestedSpi);
        } catch (ServiceSpecificException e) {
            throw rethrowUncheckedExceptionFromServiceSpecificException(e);
        }
    }

    public void applyTransportModeTransform(Socket socket, int direction, IpSecTransform transform) throws IOException {
        socket.getSoLinger();
        applyTransportModeTransform(socket.getFileDescriptor$(), direction, transform);
    }

    public void applyTransportModeTransform(DatagramSocket socket, int direction, IpSecTransform transform) throws IOException {
        applyTransportModeTransform(socket.getFileDescriptor$(), direction, transform);
    }

    public void applyTransportModeTransform(FileDescriptor socket, int direction, IpSecTransform transform) throws IOException {
        try {
            ParcelFileDescriptor pfd = ParcelFileDescriptor.dup(socket);
            try {
                this.mService.applyTransportModeTransform(pfd, direction, transform.getResourceId());
                if (pfd != null) {
                    $closeResource(null, pfd);
                }
            } catch (Throwable th) {
                try {
                    throw th;
                } catch (Throwable th2) {
                    if (pfd != null) {
                        $closeResource(th, pfd);
                    }
                    throw th2;
                }
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        } catch (ServiceSpecificException e2) {
            throw rethrowCheckedExceptionFromServiceSpecificException(e2);
        }
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 == null) {
            x1.close();
            return;
        }
        try {
            x1.close();
        } catch (Throwable th) {
            x0.addSuppressed(th);
        }
    }

    public void removeTransportModeTransforms(Socket socket) throws IOException {
        socket.getSoLinger();
        removeTransportModeTransforms(socket.getFileDescriptor$());
    }

    public void removeTransportModeTransforms(DatagramSocket socket) throws IOException {
        removeTransportModeTransforms(socket.getFileDescriptor$());
    }

    public void removeTransportModeTransforms(FileDescriptor socket) throws IOException {
        try {
            ParcelFileDescriptor pfd = ParcelFileDescriptor.dup(socket);
            try {
                this.mService.removeTransportModeTransforms(pfd);
                if (pfd != null) {
                    $closeResource(null, pfd);
                }
            } catch (Throwable th) {
                try {
                    throw th;
                } catch (Throwable th2) {
                    if (pfd != null) {
                        $closeResource(th, pfd);
                    }
                    throw th2;
                }
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        } catch (ServiceSpecificException e2) {
            throw rethrowCheckedExceptionFromServiceSpecificException(e2);
        }
    }

    public synchronized void removeTunnelModeTransform(Network net, IpSecTransform transform) {
    }

    /* loaded from: classes2.dex */
    public static final class UdpEncapsulationSocket implements AutoCloseable {
        private final CloseGuard mCloseGuard;
        private final ParcelFileDescriptor mPfd;
        private final int mPort;
        private int mResourceId;
        private final IIpSecService mService;

        private synchronized UdpEncapsulationSocket(IIpSecService service, int port) throws ResourceUnavailableException, IOException {
            this.mResourceId = -1;
            this.mCloseGuard = CloseGuard.get();
            this.mService = service;
            try {
                IpSecUdpEncapResponse result = this.mService.openUdpEncapsulationSocket(port, new Binder());
                switch (result.status) {
                    case 0:
                        this.mResourceId = result.resourceId;
                        this.mPort = result.port;
                        this.mPfd = result.fileDescriptor;
                        this.mCloseGuard.open("constructor");
                        return;
                    case 1:
                        throw new ResourceUnavailableException("No more Sockets may be allocated by this requester.");
                    default:
                        throw new RuntimeException("Unknown status returned by IpSecService: " + result.status);
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public FileDescriptor getFileDescriptor() {
            if (this.mPfd == null) {
                return null;
            }
            return this.mPfd.getFileDescriptor();
        }

        public int getPort() {
            return this.mPort;
        }

        @Override // java.lang.AutoCloseable
        public void close() throws IOException {
            try {
                try {
                    try {
                        this.mService.closeUdpEncapsulationSocket(this.mResourceId);
                        this.mResourceId = -1;
                    } catch (RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                } catch (Exception e2) {
                    Log.e(IpSecManager.TAG, "Failed to close " + this + ", Exception=" + e2);
                }
                try {
                    this.mPfd.close();
                } catch (IOException e3) {
                    Log.e(IpSecManager.TAG, "Failed to close UDP Encapsulation Socket with Port= " + this.mPort);
                    throw e3;
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

        @VisibleForTesting
        public synchronized int getResourceId() {
            return this.mResourceId;
        }

        public String toString() {
            return "UdpEncapsulationSocket{port=" + this.mPort + ",resourceId=" + this.mResourceId + "}";
        }
    }

    public UdpEncapsulationSocket openUdpEncapsulationSocket(int port) throws IOException, ResourceUnavailableException {
        if (port == 0) {
            throw new IllegalArgumentException("Specified port must be a valid port number!");
        }
        try {
            return new UdpEncapsulationSocket(this.mService, port);
        } catch (ServiceSpecificException e) {
            throw rethrowCheckedExceptionFromServiceSpecificException(e);
        }
    }

    public UdpEncapsulationSocket openUdpEncapsulationSocket() throws IOException, ResourceUnavailableException {
        try {
            return new UdpEncapsulationSocket(this.mService, 0);
        } catch (ServiceSpecificException e) {
            throw rethrowCheckedExceptionFromServiceSpecificException(e);
        }
    }

    /* loaded from: classes2.dex */
    public static final class IpSecTunnelInterface implements AutoCloseable {
        private final CloseGuard mCloseGuard;
        private String mInterfaceName;
        private final InetAddress mLocalAddress;
        private final String mOpPackageName;
        private final InetAddress mRemoteAddress;
        private int mResourceId;
        private final IIpSecService mService;
        private final Network mUnderlyingNetwork;

        public synchronized String getInterfaceName() {
            return this.mInterfaceName;
        }

        public synchronized void addAddress(InetAddress address, int prefixLen) throws IOException {
            try {
                this.mService.addAddressToTunnelInterface(this.mResourceId, new LinkAddress(address, prefixLen), this.mOpPackageName);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (ServiceSpecificException e2) {
                throw IpSecManager.rethrowCheckedExceptionFromServiceSpecificException(e2);
            }
        }

        public synchronized void removeAddress(InetAddress address, int prefixLen) throws IOException {
            try {
                this.mService.removeAddressFromTunnelInterface(this.mResourceId, new LinkAddress(address, prefixLen), this.mOpPackageName);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            } catch (ServiceSpecificException e2) {
                throw IpSecManager.rethrowCheckedExceptionFromServiceSpecificException(e2);
            }
        }

        private synchronized IpSecTunnelInterface(Context ctx, IIpSecService service, InetAddress localAddress, InetAddress remoteAddress, Network underlyingNetwork) throws ResourceUnavailableException, IOException {
            this.mCloseGuard = CloseGuard.get();
            this.mResourceId = -1;
            this.mOpPackageName = ctx.getOpPackageName();
            this.mService = service;
            this.mLocalAddress = localAddress;
            this.mRemoteAddress = remoteAddress;
            this.mUnderlyingNetwork = underlyingNetwork;
            try {
                IpSecTunnelInterfaceResponse result = this.mService.createTunnelInterface(localAddress.getHostAddress(), remoteAddress.getHostAddress(), underlyingNetwork, new Binder(), this.mOpPackageName);
                switch (result.status) {
                    case 0:
                        this.mResourceId = result.resourceId;
                        this.mInterfaceName = result.interfaceName;
                        this.mCloseGuard.open("constructor");
                        return;
                    case 1:
                        throw new ResourceUnavailableException("No more tunnel interfaces may be allocated by this requester.");
                    default:
                        throw new RuntimeException("Unknown status returned by IpSecService: " + result.status);
                }
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            try {
                try {
                    try {
                        this.mService.deleteTunnelInterface(this.mResourceId, this.mOpPackageName);
                    } catch (RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                } catch (Exception e2) {
                    Log.e(IpSecManager.TAG, "Failed to close " + this + ", Exception=" + e2);
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

        @VisibleForTesting
        public synchronized int getResourceId() {
            return this.mResourceId;
        }

        public String toString() {
            return "IpSecTunnelInterface{ifname=" + this.mInterfaceName + ",resourceId=" + this.mResourceId + "}";
        }
    }

    public synchronized IpSecTunnelInterface createIpSecTunnelInterface(InetAddress localAddress, InetAddress remoteAddress, Network underlyingNetwork) throws ResourceUnavailableException, IOException {
        try {
            return new IpSecTunnelInterface(this.mContext, this.mService, localAddress, remoteAddress, underlyingNetwork);
        } catch (ServiceSpecificException e) {
            throw rethrowCheckedExceptionFromServiceSpecificException(e);
        }
    }

    public synchronized void applyTunnelModeTransform(IpSecTunnelInterface tunnel, int direction, IpSecTransform transform) throws IOException {
        try {
            this.mService.applyTunnelModeTransform(tunnel.getResourceId(), direction, transform.getResourceId(), this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        } catch (ServiceSpecificException e2) {
            throw rethrowCheckedExceptionFromServiceSpecificException(e2);
        }
    }

    public synchronized IpSecManager(Context ctx, IIpSecService service) {
        this.mContext = ctx;
        this.mService = (IIpSecService) Preconditions.checkNotNull(service, "missing service");
    }

    private static synchronized void maybeHandleServiceSpecificException(ServiceSpecificException sse) {
        if (sse.errorCode == OsConstants.EINVAL) {
            throw new IllegalArgumentException(sse);
        }
        if (sse.errorCode == OsConstants.EAGAIN) {
            throw new IllegalStateException(sse);
        }
        if (sse.errorCode == OsConstants.EOPNOTSUPP) {
            throw new UnsupportedOperationException(sse);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized RuntimeException rethrowUncheckedExceptionFromServiceSpecificException(ServiceSpecificException sse) {
        maybeHandleServiceSpecificException(sse);
        throw new RuntimeException(sse);
    }

    static synchronized IOException rethrowCheckedExceptionFromServiceSpecificException(ServiceSpecificException sse) throws IOException {
        maybeHandleServiceSpecificException(sse);
        throw new ErrnoException("IpSec encountered errno=" + sse.errorCode, sse.errorCode).rethrowAsIOException();
    }
}
