package com.android.internal.infra;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.Slog;
import android.util.TimeUtils;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.infra.AbstractRemoteService;
import com.android.internal.util.function.pooled.PooledLambda;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/* loaded from: classes3.dex */
public abstract class AbstractRemoteService<S extends AbstractRemoteService<S, I>, I extends IInterface> implements IBinder.DeathRecipient {
    protected static final int LAST_PRIVATE_MSG = 2;
    private static final int MSG_BIND = 1;
    private static final int MSG_UNBIND = 2;
    public static final long PERMANENT_BOUND_TIMEOUT_MS = 0;
    private boolean mBinding;
    private final int mBindingFlags;
    private boolean mCompleted;
    protected final ComponentName mComponentName;
    private final Context mContext;
    private boolean mDestroyed;
    protected final Handler mHandler;
    private final Intent mIntent;
    private long mNextUnbind;
    protected I mService;
    private boolean mServiceDied;
    private final int mUserId;
    public final boolean mVerbose;
    private final VultureCallback<S> mVultureCallback;
    protected final String mTag = getClass().getSimpleName();
    private final ServiceConnection mServiceConnection = new RemoteServiceConnection();
    private final ArrayList<BasePendingRequest<S, I>> mUnfinishedRequests = new ArrayList<>();

    /* loaded from: classes3.dex */
    public interface AsyncRequest<I extends IInterface> {
        void run(I i) throws RemoteException;
    }

    /* loaded from: classes3.dex */
    public interface VultureCallback<T> {
        void onServiceDied(T t);
    }

    protected abstract I getServiceInterface(IBinder iBinder);

    protected abstract long getTimeoutIdleBindMillis();

    abstract void handleBindFailure();

    protected abstract void handleOnDestroy();

    abstract void handlePendingRequestWhileUnBound(BasePendingRequest<S, I> basePendingRequest);

    abstract void handlePendingRequests();

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractRemoteService(Context context, String serviceInterface, ComponentName componentName, int userId, VultureCallback<S> callback, Handler handler, int bindingFlags, boolean verbose) {
        this.mContext = context;
        this.mVultureCallback = callback;
        this.mVerbose = verbose;
        this.mComponentName = componentName;
        this.mIntent = new Intent(serviceInterface).setComponent(this.mComponentName);
        this.mUserId = userId;
        this.mHandler = new Handler(handler.getLooper());
        this.mBindingFlags = bindingFlags;
    }

    public final void destroy() {
        this.mHandler.sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: com.android.internal.infra.-$$Lambda$AbstractRemoteService$9IBVTCLLZgndvH7fu1P14PW1_1o
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((AbstractRemoteService) obj).handleDestroy();
            }
        }, this));
    }

    public final boolean isDestroyed() {
        return this.mDestroyed;
    }

    public final ComponentName getComponentName() {
        return this.mComponentName;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnConnectedStateChangedInternal(boolean connected) {
        handleOnConnectedStateChanged(connected);
        if (connected) {
            handlePendingRequests();
        }
    }

    protected void handleOnConnectedStateChanged(boolean state) {
    }

    protected long getRemoteRequestMillis() {
        throw new UnsupportedOperationException("not implemented by " + getClass());
    }

    public final I getServiceInterface() {
        return this.mService;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDestroy() {
        if (checkIfDestroyed()) {
            return;
        }
        handleOnDestroy();
        handleEnsureUnbound();
        this.mDestroyed = true;
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        this.mHandler.sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: com.android.internal.infra.-$$Lambda$AbstractRemoteService$ocrHd68Md9x6FfAzVQ6w8MAjFqY
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((AbstractRemoteService) obj).handleBinderDied();
            }
        }, this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBinderDied() {
        if (checkIfDestroyed()) {
            return;
        }
        I i = this.mService;
        if (i != null) {
            i.asBinder().unlinkToDeath(this, 0);
        }
        this.mService = null;
        this.mServiceDied = true;
        cancelScheduledUnbind();
        this.mVultureCallback.onServiceDied(this);
        handleBindFailure();
    }

    public void dump(String prefix, PrintWriter pw) {
        pw.append((CharSequence) prefix).append("service:").println();
        pw.append((CharSequence) prefix).append("  ").append("userId=").append((CharSequence) String.valueOf(this.mUserId)).println();
        pw.append((CharSequence) prefix).append("  ").append("componentName=").append((CharSequence) this.mComponentName.flattenToString()).println();
        pw.append((CharSequence) prefix).append("  ").append("destroyed=").append((CharSequence) String.valueOf(this.mDestroyed)).println();
        pw.append((CharSequence) prefix).append("  ").append("numUnfinishedRequests=").append((CharSequence) String.valueOf(this.mUnfinishedRequests.size())).println();
        boolean bound = handleIsBound();
        pw.append((CharSequence) prefix).append("  ").append("bound=").append((CharSequence) String.valueOf(bound));
        long idleTimeout = getTimeoutIdleBindMillis();
        if (bound) {
            if (idleTimeout > 0) {
                pw.append(" (unbind in : ");
                TimeUtils.formatDuration(this.mNextUnbind - SystemClock.elapsedRealtime(), pw);
                pw.append(")");
            } else {
                pw.append(" (permanently bound)");
            }
        }
        pw.println();
        pw.append((CharSequence) prefix).append("mBindingFlags=").println(this.mBindingFlags);
        pw.append((CharSequence) prefix).append("idleTimeout=").append((CharSequence) Long.toString(idleTimeout / 1000)).append("s\n");
        pw.append((CharSequence) prefix).append("requestTimeout=");
        try {
            pw.append((CharSequence) Long.toString(getRemoteRequestMillis() / 1000)).append("s\n");
        } catch (UnsupportedOperationException e) {
            pw.append("not supported\n");
        }
        pw.println();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void scheduleRequest(BasePendingRequest<S, I> pendingRequest) {
        this.mHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.internal.infra.-$$Lambda$7-CJJfrUZBVuXZyYFEWBNh8Mky8
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((AbstractRemoteService) obj).handlePendingRequest((AbstractRemoteService.BasePendingRequest) obj2);
            }
        }, this, pendingRequest));
    }

    void finishRequest(BasePendingRequest<S, I> finshedRequest) {
        this.mHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.internal.infra.-$$Lambda$AbstractRemoteService$6FcEKfZ-7TXLg6dcCU8EMuMNAy4
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((AbstractRemoteService) obj).handleFinishRequest((AbstractRemoteService.BasePendingRequest) obj2);
            }
        }, this, finshedRequest));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleFinishRequest(BasePendingRequest<S, I> finshedRequest) {
        this.mUnfinishedRequests.remove(finshedRequest);
        if (this.mUnfinishedRequests.isEmpty()) {
            scheduleUnbind();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void scheduleAsyncRequest(AsyncRequest<I> request) {
        MyAsyncPendingRequest<S, I> asyncRequest = new MyAsyncPendingRequest<>(this, request);
        this.mHandler.sendMessage(PooledLambda.obtainMessage(new BiConsumer() { // from class: com.android.internal.infra.-$$Lambda$EbzSql2RHkXox5Myj8A-7kLC4_A
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ((AbstractRemoteService) obj).handlePendingRequest((AbstractRemoteService.MyAsyncPendingRequest) obj2);
            }
        }, this, asyncRequest));
    }

    private void cancelScheduledUnbind() {
        this.mHandler.removeMessages(2);
    }

    protected void scheduleBind() {
        if (this.mHandler.hasMessages(1)) {
            if (this.mVerbose) {
                Slog.v(this.mTag, "scheduleBind(): already scheduled");
                return;
            }
            return;
        }
        this.mHandler.sendMessage(PooledLambda.obtainMessage(new Consumer() { // from class: com.android.internal.infra.-$$Lambda$AbstractRemoteService$YSUzqqi1Pbrg2dlwMGMtKWbGXck
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((AbstractRemoteService) obj).handleEnsureBound();
            }
        }, this).setWhat(1));
    }

    protected void scheduleUnbind() {
        scheduleUnbind(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleUnbind(boolean delay) {
        long unbindDelay = getTimeoutIdleBindMillis();
        if (unbindDelay <= 0) {
            if (this.mVerbose) {
                String str = this.mTag;
                Slog.v(str, "not scheduling unbind when value is " + unbindDelay);
                return;
            }
            return;
        }
        if (!delay) {
            unbindDelay = 0;
        }
        cancelScheduledUnbind();
        this.mNextUnbind = SystemClock.elapsedRealtime() + unbindDelay;
        if (this.mVerbose) {
            String str2 = this.mTag;
            Slog.v(str2, "unbinding in " + unbindDelay + "ms: " + this.mNextUnbind);
        }
        this.mHandler.sendMessageDelayed(PooledLambda.obtainMessage(new Consumer() { // from class: com.android.internal.infra.-$$Lambda$AbstractRemoteService$MDW40b8CzodE5xRowI9wDEyXEnw
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((AbstractRemoteService) obj).handleUnbind();
            }
        }, this).setWhat(2), unbindDelay);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUnbind() {
        if (checkIfDestroyed()) {
            return;
        }
        handleEnsureUnbound();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void handlePendingRequest(BasePendingRequest<S, I> pendingRequest) {
        if (checkIfDestroyed() || this.mCompleted) {
            return;
        }
        if (!handleIsBound()) {
            if (this.mVerbose) {
                String str = this.mTag;
                Slog.v(str, "handlePendingRequest(): queuing " + pendingRequest);
            }
            handlePendingRequestWhileUnBound(pendingRequest);
            handleEnsureBound();
            return;
        }
        if (this.mVerbose) {
            String str2 = this.mTag;
            Slog.v(str2, "handlePendingRequest(): " + pendingRequest);
        }
        this.mUnfinishedRequests.add(pendingRequest);
        cancelScheduledUnbind();
        pendingRequest.run();
        if (pendingRequest.isFinal()) {
            this.mCompleted = true;
        }
    }

    private boolean handleIsBound() {
        return this.mService != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleEnsureBound() {
        if (handleIsBound() || this.mBinding) {
            return;
        }
        if (this.mVerbose) {
            Slog.v(this.mTag, "ensureBound()");
        }
        this.mBinding = true;
        int flags = 67108865 | this.mBindingFlags;
        boolean willBind = this.mContext.bindServiceAsUser(this.mIntent, this.mServiceConnection, flags, this.mHandler, new UserHandle(this.mUserId));
        if (!willBind) {
            String str = this.mTag;
            Slog.w(str, "could not bind to " + this.mIntent + " using flags " + flags);
            this.mBinding = false;
            if (!this.mServiceDied) {
                handleBinderDied();
            }
        }
    }

    private void handleEnsureUnbound() {
        if (handleIsBound() || this.mBinding) {
            if (this.mVerbose) {
                Slog.v(this.mTag, "ensureUnbound()");
            }
            this.mBinding = false;
            if (handleIsBound()) {
                handleOnConnectedStateChangedInternal(false);
                I i = this.mService;
                if (i != null) {
                    i.asBinder().unlinkToDeath(this, 0);
                    this.mService = null;
                }
            }
            this.mNextUnbind = 0L;
            this.mContext.unbindService(this.mServiceConnection);
        }
    }

    /* loaded from: classes3.dex */
    private class RemoteServiceConnection implements ServiceConnection {
        private RemoteServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (AbstractRemoteService.this.mVerbose) {
                Slog.v(AbstractRemoteService.this.mTag, "onServiceConnected()");
            }
            if (!AbstractRemoteService.this.mDestroyed && AbstractRemoteService.this.mBinding) {
                AbstractRemoteService.this.mBinding = false;
                try {
                    service.linkToDeath(AbstractRemoteService.this, 0);
                    AbstractRemoteService abstractRemoteService = AbstractRemoteService.this;
                    abstractRemoteService.mService = (I) abstractRemoteService.getServiceInterface(service);
                    AbstractRemoteService.this.handleOnConnectedStateChangedInternal(true);
                    AbstractRemoteService.this.mServiceDied = false;
                    return;
                } catch (RemoteException e) {
                    AbstractRemoteService.this.handleBinderDied();
                    return;
                }
            }
            Slog.wtf(AbstractRemoteService.this.mTag, "onServiceConnected() was dispatched after unbindService.");
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
            if (AbstractRemoteService.this.mVerbose) {
                Slog.v(AbstractRemoteService.this.mTag, "onServiceDisconnected()");
            }
            AbstractRemoteService.this.mBinding = true;
            AbstractRemoteService.this.mService = null;
        }

        @Override // android.content.ServiceConnection
        public void onBindingDied(ComponentName name) {
            if (AbstractRemoteService.this.mVerbose) {
                Slog.v(AbstractRemoteService.this.mTag, "onBindingDied()");
            }
            AbstractRemoteService.this.scheduleUnbind(false);
        }
    }

    private boolean checkIfDestroyed() {
        if (this.mDestroyed && this.mVerbose) {
            String str = this.mTag;
            Slog.v(str, "Not handling operation as service for " + this.mComponentName + " is already destroyed");
        }
        return this.mDestroyed;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append("[");
        sb.append(this.mComponentName);
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(System.identityHashCode(this));
        sb.append(this.mService != null ? " (bound)" : " (unbound)");
        sb.append(this.mDestroyed ? " (destroyed)" : "");
        sb.append("]");
        return sb.toString();
    }

    /* loaded from: classes3.dex */
    public static abstract class BasePendingRequest<S extends AbstractRemoteService<S, I>, I extends IInterface> implements Runnable {
        @GuardedBy({"mLock"})
        boolean mCancelled;
        @GuardedBy({"mLock"})
        boolean mCompleted;
        final WeakReference<S> mWeakService;
        protected final String mTag = getClass().getSimpleName();
        protected final Object mLock = new Object();

        BasePendingRequest(S service) {
            this.mWeakService = new WeakReference<>(service);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public final S getService() {
            return this.mWeakService.get();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public final boolean finish() {
            synchronized (this.mLock) {
                if (!this.mCompleted && !this.mCancelled) {
                    this.mCompleted = true;
                    S service = this.mWeakService.get();
                    if (service != null) {
                        service.finishRequest(this);
                    }
                    onFinished();
                    return true;
                }
                return false;
            }
        }

        void onFinished() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void onFailed() {
        }

        @GuardedBy({"mLock"})
        protected final boolean isCancelledLocked() {
            return this.mCancelled;
        }

        public boolean cancel() {
            synchronized (this.mLock) {
                if (!this.mCancelled && !this.mCompleted) {
                    this.mCancelled = true;
                    onCancel();
                    return true;
                }
                return false;
            }
        }

        void onCancel() {
        }

        protected boolean isFinal() {
            return false;
        }

        protected boolean isRequestCompleted() {
            boolean z;
            synchronized (this.mLock) {
                z = this.mCompleted;
            }
            return z;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class PendingRequest<S extends AbstractRemoteService<S, I>, I extends IInterface> extends BasePendingRequest<S, I> {
        private final Handler mServiceHandler;
        private final Runnable mTimeoutTrigger;

        protected abstract void onTimeout(S s);

        /* JADX INFO: Access modifiers changed from: protected */
        public PendingRequest(final S service) {
            super(service);
            this.mServiceHandler = service.mHandler;
            this.mTimeoutTrigger = new Runnable() { // from class: com.android.internal.infra.-$$Lambda$AbstractRemoteService$PendingRequest$IBoaBGXZQEXJr69u3aJF-LCJ42Y
                @Override // java.lang.Runnable
                public final void run() {
                    AbstractRemoteService.PendingRequest.this.lambda$new$0$AbstractRemoteService$PendingRequest(service);
                }
            };
            this.mServiceHandler.postAtTime(this.mTimeoutTrigger, SystemClock.uptimeMillis() + service.getRemoteRequestMillis());
        }

        public /* synthetic */ void lambda$new$0$AbstractRemoteService$PendingRequest(AbstractRemoteService service) {
            synchronized (this.mLock) {
                if (this.mCancelled) {
                    return;
                }
                this.mCompleted = true;
                S remoteService = this.mWeakService.get();
                if (remoteService != null) {
                    String str = this.mTag;
                    Slog.w(str, "timed out after " + service.getRemoteRequestMillis() + " ms");
                    remoteService.finishRequest(this);
                    onTimeout(remoteService);
                    return;
                }
                Slog.w(this.mTag, "timed out (no service)");
            }
        }

        @Override // com.android.internal.infra.AbstractRemoteService.BasePendingRequest
        final void onFinished() {
            this.mServiceHandler.removeCallbacks(this.mTimeoutTrigger);
        }

        @Override // com.android.internal.infra.AbstractRemoteService.BasePendingRequest
        final void onCancel() {
            this.mServiceHandler.removeCallbacks(this.mTimeoutTrigger);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class MyAsyncPendingRequest<S extends AbstractRemoteService<S, I>, I extends IInterface> extends BasePendingRequest<S, I> {
        private static final String TAG = MyAsyncPendingRequest.class.getSimpleName();
        private final AsyncRequest<I> mRequest;

        protected MyAsyncPendingRequest(S service, AsyncRequest<I> request) {
            super(service);
            this.mRequest = request;
        }

        @Override // java.lang.Runnable
        public void run() {
            S remoteService = getService();
            try {
                if (remoteService == null) {
                    return;
                }
                try {
                    this.mRequest.run(remoteService.mService);
                } catch (RemoteException e) {
                    String str = TAG;
                    Slog.w(str, "exception handling async request (" + this + "): " + e);
                }
            } finally {
                finish();
            }
        }
    }
}
