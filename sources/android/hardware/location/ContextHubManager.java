package android.hardware.location;

import android.annotation.SuppressLint;
import android.annotation.SystemApi;
import android.content.Context;
import android.hardware.location.ContextHubTransaction;
import android.hardware.location.IContextHubCallback;
import android.hardware.location.IContextHubClientCallback;
import android.hardware.location.IContextHubService;
import android.hardware.location.IContextHubTransactionCallback;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.util.List;
import java.util.concurrent.Executor;
@SystemApi
/* loaded from: classes.dex */
public final class ContextHubManager {
    private static final String TAG = "ContextHubManager";
    private Callback mCallback;
    private Handler mCallbackHandler;
    @Deprecated
    private ICallback mLocalCallback;
    private final Looper mMainLooper;
    private final IContextHubCallback.Stub mClientCallback = new IContextHubCallback.Stub() { // from class: android.hardware.location.ContextHubManager.4
        @Override // android.hardware.location.IContextHubCallback
        public void onMessageReceipt(final int hubId, final int nanoAppId, final ContextHubMessage message) {
            Handler handler;
            if (ContextHubManager.this.mCallback == null) {
                if (ContextHubManager.this.mLocalCallback != null) {
                    synchronized (this) {
                        ContextHubManager.this.mLocalCallback.onMessageReceipt(hubId, nanoAppId, message);
                    }
                    return;
                }
                return;
            }
            synchronized (this) {
                final Callback callback = ContextHubManager.this.mCallback;
                if (ContextHubManager.this.mCallbackHandler != null) {
                    handler = ContextHubManager.this.mCallbackHandler;
                } else {
                    handler = new Handler(ContextHubManager.this.mMainLooper);
                }
                handler.post(new Runnable() { // from class: android.hardware.location.ContextHubManager.4.1
                    @Override // java.lang.Runnable
                    public void run() {
                        callback.onMessageReceipt(hubId, nanoAppId, message);
                    }
                });
            }
        }
    };
    private final IContextHubService mService = IContextHubService.Stub.asInterface(ServiceManager.getServiceOrThrow(Context.CONTEXTHUB_SERVICE));

    @Deprecated
    /* loaded from: classes.dex */
    public interface ICallback {
        synchronized void onMessageReceipt(int i, int i2, ContextHubMessage contextHubMessage);
    }

    @Deprecated
    /* loaded from: classes.dex */
    public static abstract class Callback {
        public abstract void onMessageReceipt(int i, int i2, ContextHubMessage contextHubMessage);

        protected Callback() {
        }
    }

    @Deprecated
    public int[] getContextHubHandles() {
        try {
            return this.mService.getContextHubHandles();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public ContextHubInfo getContextHubInfo(int hubHandle) {
        try {
            return this.mService.getContextHubInfo(hubHandle);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public int loadNanoApp(int hubHandle, NanoApp app) {
        try {
            return this.mService.loadNanoApp(hubHandle, app);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public int unloadNanoApp(int nanoAppHandle) {
        try {
            return this.mService.unloadNanoApp(nanoAppHandle);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public NanoAppInstanceInfo getNanoAppInstanceInfo(int nanoAppHandle) {
        try {
            return this.mService.getNanoAppInstanceInfo(nanoAppHandle);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public int[] findNanoAppOnHub(int hubHandle, NanoAppFilter filter) {
        try {
            return this.mService.findNanoAppOnHub(hubHandle, filter);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public int sendMessage(int hubHandle, int nanoAppHandle, ContextHubMessage message) {
        try {
            return this.mService.sendMessage(hubHandle, nanoAppHandle, message);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<ContextHubInfo> getContextHubs() {
        try {
            return this.mService.getContextHubs();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private synchronized IContextHubTransactionCallback createTransactionCallback(final ContextHubTransaction<Void> transaction) {
        return new IContextHubTransactionCallback.Stub() { // from class: android.hardware.location.ContextHubManager.1
            @Override // android.hardware.location.IContextHubTransactionCallback
            public void onQueryResponse(int result, List<NanoAppState> nanoappList) {
                Log.e(ContextHubManager.TAG, "Received a query callback on a non-query request");
                transaction.setResponse(new ContextHubTransaction.Response(7, null));
            }

            @Override // android.hardware.location.IContextHubTransactionCallback
            public void onTransactionComplete(int result) {
                transaction.setResponse(new ContextHubTransaction.Response(result, null));
            }
        };
    }

    private synchronized IContextHubTransactionCallback createQueryCallback(final ContextHubTransaction<List<NanoAppState>> transaction) {
        return new IContextHubTransactionCallback.Stub() { // from class: android.hardware.location.ContextHubManager.2
            @Override // android.hardware.location.IContextHubTransactionCallback
            public void onQueryResponse(int result, List<NanoAppState> nanoappList) {
                transaction.setResponse(new ContextHubTransaction.Response(result, nanoappList));
            }

            @Override // android.hardware.location.IContextHubTransactionCallback
            public void onTransactionComplete(int result) {
                Log.e(ContextHubManager.TAG, "Received a non-query callback on a query request");
                transaction.setResponse(new ContextHubTransaction.Response(7, null));
            }
        };
    }

    public ContextHubTransaction<Void> loadNanoApp(ContextHubInfo hubInfo, NanoAppBinary appBinary) {
        Preconditions.checkNotNull(hubInfo, "ContextHubInfo cannot be null");
        Preconditions.checkNotNull(appBinary, "NanoAppBinary cannot be null");
        ContextHubTransaction<Void> transaction = new ContextHubTransaction<>(0);
        IContextHubTransactionCallback callback = createTransactionCallback(transaction);
        try {
            this.mService.loadNanoAppOnHub(hubInfo.getId(), callback, appBinary);
            return transaction;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ContextHubTransaction<Void> unloadNanoApp(ContextHubInfo hubInfo, long nanoAppId) {
        Preconditions.checkNotNull(hubInfo, "ContextHubInfo cannot be null");
        ContextHubTransaction<Void> transaction = new ContextHubTransaction<>(1);
        IContextHubTransactionCallback callback = createTransactionCallback(transaction);
        try {
            this.mService.unloadNanoAppFromHub(hubInfo.getId(), callback, nanoAppId);
            return transaction;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ContextHubTransaction<Void> enableNanoApp(ContextHubInfo hubInfo, long nanoAppId) {
        Preconditions.checkNotNull(hubInfo, "ContextHubInfo cannot be null");
        ContextHubTransaction<Void> transaction = new ContextHubTransaction<>(2);
        IContextHubTransactionCallback callback = createTransactionCallback(transaction);
        try {
            this.mService.enableNanoApp(hubInfo.getId(), callback, nanoAppId);
            return transaction;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ContextHubTransaction<Void> disableNanoApp(ContextHubInfo hubInfo, long nanoAppId) {
        Preconditions.checkNotNull(hubInfo, "ContextHubInfo cannot be null");
        ContextHubTransaction<Void> transaction = new ContextHubTransaction<>(3);
        IContextHubTransactionCallback callback = createTransactionCallback(transaction);
        try {
            this.mService.disableNanoApp(hubInfo.getId(), callback, nanoAppId);
            return transaction;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ContextHubTransaction<List<NanoAppState>> queryNanoApps(ContextHubInfo hubInfo) {
        Preconditions.checkNotNull(hubInfo, "ContextHubInfo cannot be null");
        ContextHubTransaction<List<NanoAppState>> transaction = new ContextHubTransaction<>(4);
        IContextHubTransactionCallback callback = createQueryCallback(transaction);
        try {
            this.mService.queryNanoApps(hubInfo.getId(), callback);
            return transaction;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SuppressLint({"Doclava125"})
    @Deprecated
    public int registerCallback(Callback callback) {
        return registerCallback(callback, null);
    }

    @Deprecated
    public synchronized int registerCallback(ICallback callback) {
        if (this.mLocalCallback != null) {
            Log.w(TAG, "Max number of local callbacks reached!");
            return -1;
        }
        this.mLocalCallback = callback;
        return 0;
    }

    @SuppressLint({"Doclava125"})
    @Deprecated
    public int registerCallback(Callback callback, Handler handler) {
        synchronized (this) {
            if (this.mCallback != null) {
                Log.w(TAG, "Max number of callbacks reached!");
                return -1;
            }
            this.mCallback = callback;
            this.mCallbackHandler = handler;
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: android.hardware.location.ContextHubManager$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass3 extends IContextHubClientCallback.Stub {
        final /* synthetic */ ContextHubClientCallback val$callback;
        final /* synthetic */ ContextHubClient val$client;
        final /* synthetic */ Executor val$executor;

        AnonymousClass3(Executor executor, ContextHubClientCallback contextHubClientCallback, ContextHubClient contextHubClient) {
            this.val$executor = executor;
            this.val$callback = contextHubClientCallback;
            this.val$client = contextHubClient;
        }

        @Override // android.hardware.location.IContextHubClientCallback
        public void onMessageFromNanoApp(final NanoAppMessage message) {
            Executor executor = this.val$executor;
            final ContextHubClientCallback contextHubClientCallback = this.val$callback;
            final ContextHubClient contextHubClient = this.val$client;
            executor.execute(new Runnable() { // from class: android.hardware.location.-$$Lambda$ContextHubManager$3$U9x_HK_GdADIEQ3mS5mDWMNWMu8
                @Override // java.lang.Runnable
                public final void run() {
                    ContextHubClientCallback.this.onMessageFromNanoApp(contextHubClient, message);
                }
            });
        }

        @Override // android.hardware.location.IContextHubClientCallback
        public void onHubReset() {
            Executor executor = this.val$executor;
            final ContextHubClientCallback contextHubClientCallback = this.val$callback;
            final ContextHubClient contextHubClient = this.val$client;
            executor.execute(new Runnable() { // from class: android.hardware.location.-$$Lambda$ContextHubManager$3$kLhhBRChCeue1LKohd5lK_lfKTU
                @Override // java.lang.Runnable
                public final void run() {
                    ContextHubClientCallback.this.onHubReset(contextHubClient);
                }
            });
        }

        @Override // android.hardware.location.IContextHubClientCallback
        public void onNanoAppAborted(final long nanoAppId, final int abortCode) {
            Executor executor = this.val$executor;
            final ContextHubClientCallback contextHubClientCallback = this.val$callback;
            final ContextHubClient contextHubClient = this.val$client;
            executor.execute(new Runnable() { // from class: android.hardware.location.-$$Lambda$ContextHubManager$3$hASoxw9hzmd9l2NpC91O5tXLzxU
                @Override // java.lang.Runnable
                public final void run() {
                    ContextHubClientCallback.this.onNanoAppAborted(contextHubClient, nanoAppId, abortCode);
                }
            });
        }

        @Override // android.hardware.location.IContextHubClientCallback
        public void onNanoAppLoaded(final long nanoAppId) {
            Executor executor = this.val$executor;
            final ContextHubClientCallback contextHubClientCallback = this.val$callback;
            final ContextHubClient contextHubClient = this.val$client;
            executor.execute(new Runnable() { // from class: android.hardware.location.-$$Lambda$ContextHubManager$3$5yx25kUuvL9qy3uBcIzI3sQQoL8
                @Override // java.lang.Runnable
                public final void run() {
                    ContextHubClientCallback.this.onNanoAppLoaded(contextHubClient, nanoAppId);
                }
            });
        }

        @Override // android.hardware.location.IContextHubClientCallback
        public void onNanoAppUnloaded(final long nanoAppId) {
            Executor executor = this.val$executor;
            final ContextHubClientCallback contextHubClientCallback = this.val$callback;
            final ContextHubClient contextHubClient = this.val$client;
            executor.execute(new Runnable() { // from class: android.hardware.location.-$$Lambda$ContextHubManager$3$KgVQePwT_QpjU9EQTp2L3LsHE5Y
                @Override // java.lang.Runnable
                public final void run() {
                    ContextHubClientCallback.this.onNanoAppUnloaded(contextHubClient, nanoAppId);
                }
            });
        }

        @Override // android.hardware.location.IContextHubClientCallback
        public void onNanoAppEnabled(final long nanoAppId) {
            Executor executor = this.val$executor;
            final ContextHubClientCallback contextHubClientCallback = this.val$callback;
            final ContextHubClient contextHubClient = this.val$client;
            executor.execute(new Runnable() { // from class: android.hardware.location.-$$Lambda$ContextHubManager$3$8oeFzBAC_VuH1d32Kod8BVn0Os8
                @Override // java.lang.Runnable
                public final void run() {
                    ContextHubClientCallback.this.onNanoAppEnabled(contextHubClient, nanoAppId);
                }
            });
        }

        @Override // android.hardware.location.IContextHubClientCallback
        public void onNanoAppDisabled(final long nanoAppId) {
            Executor executor = this.val$executor;
            final ContextHubClientCallback contextHubClientCallback = this.val$callback;
            final ContextHubClient contextHubClient = this.val$client;
            executor.execute(new Runnable() { // from class: android.hardware.location.-$$Lambda$ContextHubManager$3$On2Q5Obzm4-zLY0UP3Xs4E3P-V0
                @Override // java.lang.Runnable
                public final void run() {
                    ContextHubClientCallback.this.onNanoAppDisabled(contextHubClient, nanoAppId);
                }
            });
        }
    }

    private synchronized IContextHubClientCallback createClientCallback(ContextHubClient client, ContextHubClientCallback callback, Executor executor) {
        return new AnonymousClass3(executor, callback, client);
    }

    public ContextHubClient createClient(ContextHubInfo hubInfo, ContextHubClientCallback callback, Executor executor) {
        Preconditions.checkNotNull(callback, "Callback cannot be null");
        Preconditions.checkNotNull(hubInfo, "ContextHubInfo cannot be null");
        Preconditions.checkNotNull(executor, "Executor cannot be null");
        ContextHubClient client = new ContextHubClient(hubInfo);
        IContextHubClientCallback clientInterface = createClientCallback(client, callback, executor);
        try {
            IContextHubClient clientProxy = this.mService.createClient(clientInterface, hubInfo.getId());
            client.setClientProxy(clientProxy);
            return client;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ContextHubClient createClient(ContextHubInfo hubInfo, ContextHubClientCallback callback) {
        return createClient(hubInfo, callback, new HandlerExecutor(Handler.getMain()));
    }

    @SuppressLint({"Doclava125"})
    @Deprecated
    public int unregisterCallback(Callback callback) {
        synchronized (this) {
            if (callback != this.mCallback) {
                Log.w(TAG, "Cannot recognize callback!");
                return -1;
            }
            this.mCallback = null;
            this.mCallbackHandler = null;
            return 0;
        }
    }

    @Deprecated
    public synchronized int unregisterCallback(ICallback callback) {
        if (callback != this.mLocalCallback) {
            Log.w(TAG, "Cannot recognize local callback!");
            return -1;
        }
        this.mLocalCallback = null;
        return 0;
    }

    public synchronized ContextHubManager(Context context, Looper mainLooper) throws ServiceManager.ServiceNotFoundException {
        this.mMainLooper = mainLooper;
        try {
            this.mService.registerCallback(this.mClientCallback);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
