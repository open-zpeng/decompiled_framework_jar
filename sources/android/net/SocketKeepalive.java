package android.net;

import android.net.ISocketKeepaliveCallback;
import android.net.SocketKeepalive;
import android.os.Binder;
import android.os.ParcelFileDescriptor;
import com.android.internal.util.FunctionalUtils;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.Executor;

/* loaded from: classes2.dex */
public abstract class SocketKeepalive implements AutoCloseable {
    public static final int BINDER_DIED = -10;
    public static final int DATA_RECEIVED = -2;
    public static final int ERROR_HARDWARE_ERROR = -31;
    public static final int ERROR_HARDWARE_UNSUPPORTED = -30;
    public static final int ERROR_INSUFFICIENT_RESOURCES = -32;
    public static final int ERROR_INVALID_INTERVAL = -24;
    public static final int ERROR_INVALID_IP_ADDRESS = -21;
    public static final int ERROR_INVALID_LENGTH = -23;
    public static final int ERROR_INVALID_NETWORK = -20;
    public static final int ERROR_INVALID_PORT = -22;
    public static final int ERROR_INVALID_SOCKET = -25;
    public static final int ERROR_SOCKET_NOT_IDLE = -26;
    public static final int ERROR_UNSUPPORTED = -30;
    public static final int MAX_INTERVAL_SEC = 3600;
    public static final int MIN_INTERVAL_SEC = 10;
    public static final int NO_KEEPALIVE = -1;
    public static final int SUCCESS = 0;
    static final String TAG = "SocketKeepalive";
    final ISocketKeepaliveCallback mCallback;
    final Executor mExecutor;
    final Network mNetwork;
    final ParcelFileDescriptor mPfd;
    final IConnectivityManager mService;
    Integer mSlot;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface ErrorCode {
    }

    abstract void startImpl(int i);

    abstract void stopImpl();

    /* loaded from: classes2.dex */
    public static class ErrorCodeException extends Exception {
        public final int error;

        public ErrorCodeException(int error, Throwable e) {
            super(e);
            this.error = error;
        }

        public ErrorCodeException(int error) {
            this.error = error;
        }
    }

    /* loaded from: classes2.dex */
    public static class InvalidSocketException extends ErrorCodeException {
        public InvalidSocketException(int error, Throwable e) {
            super(error, e);
        }

        public InvalidSocketException(int error) {
            super(error);
        }
    }

    /* loaded from: classes2.dex */
    public static class InvalidPacketException extends ErrorCodeException {
        public InvalidPacketException(int error) {
            super(error);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public SocketKeepalive(IConnectivityManager service, Network network, ParcelFileDescriptor pfd, Executor executor, Callback callback) {
        this.mService = service;
        this.mNetwork = network;
        this.mPfd = pfd;
        this.mExecutor = executor;
        this.mCallback = new AnonymousClass1(callback, executor);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: android.net.SocketKeepalive$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends ISocketKeepaliveCallback.Stub {
        final /* synthetic */ Callback val$callback;
        final /* synthetic */ Executor val$executor;

        AnonymousClass1(Callback callback, Executor executor) {
            this.val$callback = callback;
            this.val$executor = executor;
        }

        @Override // android.net.ISocketKeepaliveCallback
        public void onStarted(final int slot) {
            final Callback callback = this.val$callback;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.net.-$$Lambda$SocketKeepalive$1$m-VPtyb2YaC8aWd5gXQYgFGhVbM
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    SocketKeepalive.AnonymousClass1.this.lambda$onStarted$1$SocketKeepalive$1(slot, callback);
                }
            });
        }

        public /* synthetic */ void lambda$onStarted$1$SocketKeepalive$1(final int slot, final Callback callback) throws Exception {
            SocketKeepalive.this.mExecutor.execute(new Runnable() { // from class: android.net.-$$Lambda$SocketKeepalive$1$nDWCSiqzvu6z8lptsLq-qY42hTk
                @Override // java.lang.Runnable
                public final void run() {
                    SocketKeepalive.AnonymousClass1.this.lambda$onStarted$0$SocketKeepalive$1(slot, callback);
                }
            });
        }

        public /* synthetic */ void lambda$onStarted$0$SocketKeepalive$1(int slot, Callback callback) {
            SocketKeepalive.this.mSlot = Integer.valueOf(slot);
            callback.onStarted();
        }

        @Override // android.net.ISocketKeepaliveCallback
        public void onStopped() {
            final Executor executor = this.val$executor;
            final Callback callback = this.val$callback;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.net.-$$Lambda$SocketKeepalive$1$GQbcC2yhPzv5xknkQV01K3_QTNA
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    SocketKeepalive.AnonymousClass1.this.lambda$onStopped$3$SocketKeepalive$1(executor, callback);
                }
            });
        }

        public /* synthetic */ void lambda$onStopped$3$SocketKeepalive$1(Executor executor, final Callback callback) throws Exception {
            executor.execute(new Runnable() { // from class: android.net.-$$Lambda$SocketKeepalive$1$Ghy-awbQuJd8C-GZAjeZCXMiaUw
                @Override // java.lang.Runnable
                public final void run() {
                    SocketKeepalive.AnonymousClass1.this.lambda$onStopped$2$SocketKeepalive$1(callback);
                }
            });
        }

        public /* synthetic */ void lambda$onStopped$2$SocketKeepalive$1(Callback callback) {
            SocketKeepalive.this.mSlot = null;
            callback.onStopped();
        }

        @Override // android.net.ISocketKeepaliveCallback
        public void onError(final int error) {
            final Executor executor = this.val$executor;
            final Callback callback = this.val$callback;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.net.-$$Lambda$SocketKeepalive$1$0jK7H49vYYFjBANIXTac00ocnSo
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    SocketKeepalive.AnonymousClass1.this.lambda$onError$5$SocketKeepalive$1(executor, callback, error);
                }
            });
        }

        public /* synthetic */ void lambda$onError$5$SocketKeepalive$1(Executor executor, final Callback callback, final int error) throws Exception {
            executor.execute(new Runnable() { // from class: android.net.-$$Lambda$SocketKeepalive$1$xxwNi85oVXVQ_ILhrZNWwo4ppA8
                @Override // java.lang.Runnable
                public final void run() {
                    SocketKeepalive.AnonymousClass1.this.lambda$onError$4$SocketKeepalive$1(callback, error);
                }
            });
        }

        public /* synthetic */ void lambda$onError$4$SocketKeepalive$1(Callback callback, int error) {
            SocketKeepalive.this.mSlot = null;
            callback.onError(error);
        }

        @Override // android.net.ISocketKeepaliveCallback
        public void onDataReceived() {
            final Executor executor = this.val$executor;
            final Callback callback = this.val$callback;
            Binder.withCleanCallingIdentity(new FunctionalUtils.ThrowingRunnable() { // from class: android.net.-$$Lambda$SocketKeepalive$1$nPQMIWzmX3WEJCjp1qnz_O7qaxs
                @Override // com.android.internal.util.FunctionalUtils.ThrowingRunnable
                public final void runOrThrow() {
                    SocketKeepalive.AnonymousClass1.this.lambda$onDataReceived$7$SocketKeepalive$1(executor, callback);
                }
            });
        }

        public /* synthetic */ void lambda$onDataReceived$7$SocketKeepalive$1(Executor executor, final Callback callback) throws Exception {
            executor.execute(new Runnable() { // from class: android.net.-$$Lambda$SocketKeepalive$1$yVvEaumPDc_celEzvlSEH2FU0nc
                @Override // java.lang.Runnable
                public final void run() {
                    SocketKeepalive.AnonymousClass1.this.lambda$onDataReceived$6$SocketKeepalive$1(callback);
                }
            });
        }

        public /* synthetic */ void lambda$onDataReceived$6$SocketKeepalive$1(Callback callback) {
            SocketKeepalive.this.mSlot = null;
            callback.onDataReceived();
        }
    }

    public final void start(int intervalSec) {
        startImpl(intervalSec);
    }

    public final void stop() {
        stopImpl();
    }

    @Override // java.lang.AutoCloseable
    public final void close() {
        stop();
        try {
            this.mPfd.close();
        } catch (IOException e) {
        }
    }

    /* loaded from: classes2.dex */
    public static class Callback {
        public void onStarted() {
        }

        public void onStopped() {
        }

        public void onError(int error) {
        }

        public void onDataReceived() {
        }
    }
}
