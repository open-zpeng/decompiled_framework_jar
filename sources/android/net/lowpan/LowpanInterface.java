package android.net.lowpan;

import android.content.Context;
import android.net.IpPrefix;
import android.net.LinkAddress;
import android.net.lowpan.ILowpanInterfaceListener;
import android.net.lowpan.LowpanInterface;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ServiceSpecificException;
import android.util.Log;
import java.util.HashMap;
/* loaded from: classes2.dex */
public class LowpanInterface {
    private protected static final String EMPTY_PARTITION_ID = "";
    private protected static final String NETWORK_TYPE_THREAD_V1 = "org.threadgroup.thread.v1";
    private protected static final String ROLE_COORDINATOR = "coordinator";
    private protected static final String ROLE_DETACHED = "detached";
    private protected static final String ROLE_END_DEVICE = "end-device";
    private protected static final String ROLE_LEADER = "leader";
    private protected static final String ROLE_ROUTER = "router";
    private protected static final String ROLE_SLEEPY_END_DEVICE = "sleepy-end-device";
    private protected static final String ROLE_SLEEPY_ROUTER = "sleepy-router";
    private protected static final String STATE_ATTACHED = "attached";
    private protected static final String STATE_ATTACHING = "attaching";
    private protected static final String STATE_COMMISSIONING = "commissioning";
    private protected static final String STATE_FAULT = "fault";
    private protected static final String STATE_OFFLINE = "offline";
    public protected static final String TAG = LowpanInterface.class.getSimpleName();
    public protected final ILowpanInterface mBinder;
    public protected final HashMap<Integer, ILowpanInterfaceListener> mListenerMap = new HashMap<>();
    public protected final Looper mLooper;

    /* loaded from: classes2.dex */
    public static abstract class Callback {
        private protected synchronized Callback() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void onConnectedChanged(boolean value) {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void onEnabledChanged(boolean value) {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void onUpChanged(boolean value) {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void onRoleChanged(String value) {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void onStateChanged(String state) {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void onLowpanIdentityChanged(LowpanIdentity value) {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void onLinkNetworkAdded(IpPrefix prefix) {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void onLinkNetworkRemoved(IpPrefix prefix) {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void onLinkAddressAdded(LinkAddress address) {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void onLinkAddressRemoved(LinkAddress address) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized LowpanInterface(Context context, ILowpanInterface service, Looper looper) {
        this.mBinder = service;
        this.mLooper = looper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized ILowpanInterface getService() {
        return this.mBinder;
    }

    private protected synchronized void form(LowpanProvision provision) throws LowpanException {
        try {
            this.mBinder.form(provision);
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        } catch (ServiceSpecificException x2) {
            throw LowpanException.rethrowFromServiceSpecificException(x2);
        }
    }

    private protected synchronized void join(LowpanProvision provision) throws LowpanException {
        try {
            this.mBinder.join(provision);
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        } catch (ServiceSpecificException x2) {
            throw LowpanException.rethrowFromServiceSpecificException(x2);
        }
    }

    private protected synchronized void attach(LowpanProvision provision) throws LowpanException {
        try {
            this.mBinder.attach(provision);
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        } catch (ServiceSpecificException x2) {
            throw LowpanException.rethrowFromServiceSpecificException(x2);
        }
    }

    private protected synchronized void leave() throws LowpanException {
        try {
            this.mBinder.leave();
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        } catch (ServiceSpecificException x2) {
            throw LowpanException.rethrowFromServiceSpecificException(x2);
        }
    }

    private protected synchronized LowpanCommissioningSession startCommissioningSession(LowpanBeaconInfo beaconInfo) throws LowpanException {
        try {
            this.mBinder.startCommissioningSession(beaconInfo);
            return new LowpanCommissioningSession(this.mBinder, beaconInfo, this.mLooper);
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        } catch (ServiceSpecificException x2) {
            throw LowpanException.rethrowFromServiceSpecificException(x2);
        }
    }

    private protected synchronized void reset() throws LowpanException {
        try {
            this.mBinder.reset();
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        } catch (ServiceSpecificException x2) {
            throw LowpanException.rethrowFromServiceSpecificException(x2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized String getName() {
        try {
            return this.mBinder.getName();
        } catch (DeadObjectException e) {
            return "";
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        }
    }

    private protected synchronized boolean isEnabled() {
        try {
            return this.mBinder.isEnabled();
        } catch (DeadObjectException e) {
            return false;
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        }
    }

    private protected synchronized void setEnabled(boolean enabled) throws LowpanException {
        try {
            this.mBinder.setEnabled(enabled);
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        } catch (ServiceSpecificException x2) {
            throw LowpanException.rethrowFromServiceSpecificException(x2);
        }
    }

    private protected synchronized boolean isUp() {
        try {
            return this.mBinder.isUp();
        } catch (DeadObjectException e) {
            return false;
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        }
    }

    private protected synchronized boolean isConnected() {
        try {
            return this.mBinder.isConnected();
        } catch (DeadObjectException e) {
            return false;
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        }
    }

    private protected synchronized boolean isCommissioned() {
        try {
            return this.mBinder.isCommissioned();
        } catch (DeadObjectException e) {
            return false;
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        }
    }

    private protected synchronized String getState() {
        try {
            return this.mBinder.getState();
        } catch (DeadObjectException e) {
            return STATE_FAULT;
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        }
    }

    private protected synchronized String getPartitionId() {
        try {
            return this.mBinder.getPartitionId();
        } catch (DeadObjectException e) {
            return "";
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        }
    }

    private protected synchronized LowpanIdentity getLowpanIdentity() {
        try {
            return this.mBinder.getLowpanIdentity();
        } catch (DeadObjectException e) {
            return new LowpanIdentity();
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        }
    }

    private protected synchronized String getRole() {
        try {
            return this.mBinder.getRole();
        } catch (DeadObjectException e) {
            return ROLE_DETACHED;
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        }
    }

    private protected synchronized LowpanCredential getLowpanCredential() {
        try {
            return this.mBinder.getLowpanCredential();
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        }
    }

    private protected synchronized String[] getSupportedNetworkTypes() throws LowpanException {
        try {
            return this.mBinder.getSupportedNetworkTypes();
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        } catch (ServiceSpecificException x2) {
            throw LowpanException.rethrowFromServiceSpecificException(x2);
        }
    }

    private protected synchronized LowpanChannelInfo[] getSupportedChannels() throws LowpanException {
        try {
            return this.mBinder.getSupportedChannels();
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        } catch (ServiceSpecificException x2) {
            throw LowpanException.rethrowFromServiceSpecificException(x2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: android.net.lowpan.LowpanInterface$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends ILowpanInterfaceListener.Stub {
        private Handler mHandler;
        final /* synthetic */ Callback val$cb;
        final /* synthetic */ Handler val$handler;

        AnonymousClass1(Handler handler, Callback callback) {
            this.val$handler = handler;
            this.val$cb = callback;
            if (this.val$handler == null) {
                if (LowpanInterface.this.mLooper != null) {
                    this.mHandler = new Handler(LowpanInterface.this.mLooper);
                    return;
                } else {
                    this.mHandler = new Handler();
                    return;
                }
            }
            this.mHandler = this.val$handler;
        }

        public void onEnabledChanged(final boolean value) {
            Handler handler = this.mHandler;
            final Callback callback = this.val$cb;
            handler.post(new Runnable() { // from class: android.net.lowpan.-$$Lambda$LowpanInterface$1$LMuYw1xVwTG7Wbs4COpO6TLHuQ0
                @Override // java.lang.Runnable
                public final void run() {
                    LowpanInterface.Callback.this.onEnabledChanged(value);
                }
            });
        }

        public void onConnectedChanged(final boolean value) {
            Handler handler = this.mHandler;
            final Callback callback = this.val$cb;
            handler.post(new Runnable() { // from class: android.net.lowpan.-$$Lambda$LowpanInterface$1$Nidk8wBLJKibO6BNky-_lJftmGs
                @Override // java.lang.Runnable
                public final void run() {
                    LowpanInterface.Callback.this.onConnectedChanged(value);
                }
            });
        }

        public void onUpChanged(final boolean value) {
            Handler handler = this.mHandler;
            final Callback callback = this.val$cb;
            handler.post(new Runnable() { // from class: android.net.lowpan.-$$Lambda$LowpanInterface$1$a1rvbSIFSC6J5j7aKUf1ekbmIIA
                @Override // java.lang.Runnable
                public final void run() {
                    LowpanInterface.Callback.this.onUpChanged(value);
                }
            });
        }

        public void onRoleChanged(final String value) {
            Handler handler = this.mHandler;
            final Callback callback = this.val$cb;
            handler.post(new Runnable() { // from class: android.net.lowpan.-$$Lambda$LowpanInterface$1$9yiRqHwJmFc-LEKn1vk5rA75W0M
                @Override // java.lang.Runnable
                public final void run() {
                    LowpanInterface.Callback.this.onRoleChanged(value);
                }
            });
        }

        public void onStateChanged(final String value) {
            Handler handler = this.mHandler;
            final Callback callback = this.val$cb;
            handler.post(new Runnable() { // from class: android.net.lowpan.-$$Lambda$LowpanInterface$1$5PUJBkKF3VANgkiEem5Oq8oyB6U
                @Override // java.lang.Runnable
                public final void run() {
                    LowpanInterface.Callback.this.onStateChanged(value);
                }
            });
        }

        public void onLowpanIdentityChanged(final LowpanIdentity value) {
            Handler handler = this.mHandler;
            final Callback callback = this.val$cb;
            handler.post(new Runnable() { // from class: android.net.lowpan.-$$Lambda$LowpanInterface$1$rl_ENeH3C5Kvf22BOtLnz-Ehs5c
                @Override // java.lang.Runnable
                public final void run() {
                    LowpanInterface.Callback.this.onLowpanIdentityChanged(value);
                }
            });
        }

        public void onLinkNetworkAdded(final IpPrefix value) {
            Handler handler = this.mHandler;
            final Callback callback = this.val$cb;
            handler.post(new Runnable() { // from class: android.net.lowpan.-$$Lambda$LowpanInterface$1$oacwoIgJ4pmkBqVtGJfFzk7A35k
                @Override // java.lang.Runnable
                public final void run() {
                    LowpanInterface.Callback.this.onLinkNetworkAdded(value);
                }
            });
        }

        public void onLinkNetworkRemoved(final IpPrefix value) {
            Handler handler = this.mHandler;
            final Callback callback = this.val$cb;
            handler.post(new Runnable() { // from class: android.net.lowpan.-$$Lambda$LowpanInterface$1$cH3X25eT4t6pHlLvzBjlSOMs2vc
                @Override // java.lang.Runnable
                public final void run() {
                    LowpanInterface.Callback.this.onLinkNetworkRemoved(value);
                }
            });
        }

        public void onLinkAddressAdded(String value) {
            try {
                final LinkAddress la = new LinkAddress(value);
                Handler handler = this.mHandler;
                final Callback callback = this.val$cb;
                handler.post(new Runnable() { // from class: android.net.lowpan.-$$Lambda$LowpanInterface$1$i2_6hzE6WEaUSOaaltxLebbf7-E
                    @Override // java.lang.Runnable
                    public final void run() {
                        LowpanInterface.Callback.this.onLinkAddressAdded(la);
                    }
                });
            } catch (IllegalArgumentException x) {
                String str = LowpanInterface.TAG;
                Log.e(str, "onLinkAddressAdded: Bad LinkAddress \"" + value + "\", " + x);
            }
        }

        public void onLinkAddressRemoved(String value) {
            try {
                final LinkAddress la = new LinkAddress(value);
                Handler handler = this.mHandler;
                final Callback callback = this.val$cb;
                handler.post(new Runnable() { // from class: android.net.lowpan.-$$Lambda$LowpanInterface$1$bAiJozbLxVR9_EMESl7KCJxLARA
                    @Override // java.lang.Runnable
                    public final void run() {
                        LowpanInterface.Callback.this.onLinkAddressRemoved(la);
                    }
                });
            } catch (IllegalArgumentException x) {
                String str = LowpanInterface.TAG;
                Log.e(str, "onLinkAddressRemoved: Bad LinkAddress \"" + value + "\", " + x);
            }
        }

        public void onReceiveFromCommissioner(byte[] packet) {
        }
    }

    private protected synchronized void registerCallback(Callback cb, Handler handler) {
        ILowpanInterfaceListener.Stub listenerBinder = new AnonymousClass1(handler, cb);
        try {
            this.mBinder.addListener(listenerBinder);
            synchronized (this.mListenerMap) {
                this.mListenerMap.put(Integer.valueOf(System.identityHashCode(cb)), listenerBinder);
            }
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        }
    }

    private protected synchronized void registerCallback(Callback cb) {
        registerCallback(cb, null);
    }

    private protected synchronized void unregisterCallback(Callback cb) {
        int hashCode = System.identityHashCode(cb);
        synchronized (this.mListenerMap) {
            ILowpanInterfaceListener listenerBinder = this.mListenerMap.get(Integer.valueOf(hashCode));
            if (listenerBinder != null) {
                this.mListenerMap.remove(Integer.valueOf(hashCode));
                try {
                    this.mBinder.removeListener(listenerBinder);
                } catch (DeadObjectException e) {
                } catch (RemoteException x) {
                    throw x.rethrowAsRuntimeException();
                }
            }
        }
    }

    private protected synchronized LowpanScanner createScanner() {
        return new LowpanScanner(this.mBinder);
    }

    private protected synchronized LinkAddress[] getLinkAddresses() throws LowpanException {
        try {
            String[] linkAddressStrings = this.mBinder.getLinkAddresses();
            LinkAddress[] ret = new LinkAddress[linkAddressStrings.length];
            int i = 0;
            int length = linkAddressStrings.length;
            int i2 = 0;
            while (i2 < length) {
                String str = linkAddressStrings[i2];
                int i3 = i + 1;
                ret[i] = new LinkAddress(str);
                i2++;
                i = i3;
            }
            return ret;
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        } catch (ServiceSpecificException x2) {
            throw LowpanException.rethrowFromServiceSpecificException(x2);
        }
    }

    private protected synchronized IpPrefix[] getLinkNetworks() throws LowpanException {
        try {
            return this.mBinder.getLinkNetworks();
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        } catch (ServiceSpecificException x2) {
            throw LowpanException.rethrowFromServiceSpecificException(x2);
        }
    }

    private protected synchronized void addOnMeshPrefix(IpPrefix prefix, int flags) throws LowpanException {
        try {
            this.mBinder.addOnMeshPrefix(prefix, flags);
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        } catch (ServiceSpecificException x2) {
            throw LowpanException.rethrowFromServiceSpecificException(x2);
        }
    }

    private protected synchronized void removeOnMeshPrefix(IpPrefix prefix) {
        try {
            this.mBinder.removeOnMeshPrefix(prefix);
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        } catch (ServiceSpecificException x2) {
            Log.e(TAG, x2.toString());
        }
    }

    private protected synchronized void addExternalRoute(IpPrefix prefix, int flags) throws LowpanException {
        try {
            this.mBinder.addExternalRoute(prefix, flags);
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        } catch (ServiceSpecificException x2) {
            throw LowpanException.rethrowFromServiceSpecificException(x2);
        }
    }

    private protected synchronized void removeExternalRoute(IpPrefix prefix) {
        try {
            this.mBinder.removeExternalRoute(prefix);
        } catch (RemoteException x) {
            throw x.rethrowAsRuntimeException();
        } catch (ServiceSpecificException x2) {
            Log.e(TAG, x2.toString());
        }
    }
}
