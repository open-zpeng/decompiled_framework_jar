package android.net.wifi.aware;

import android.content.Context;
import android.net.wifi.aware.IWifiAwareDiscoverySessionCallback;
import android.net.wifi.aware.IWifiAwareEventCallback;
import android.net.wifi.aware.TlvBufferUtils;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.nio.BufferOverflowException;
import java.util.List;
import libcore.util.HexEncoding;
/* loaded from: classes2.dex */
public class WifiAwareManager {
    public static final String ACTION_WIFI_AWARE_STATE_CHANGED = "android.net.wifi.aware.action.WIFI_AWARE_STATE_CHANGED";
    private static final boolean DBG = false;
    private static final String TAG = "WifiAwareManager";
    private static final boolean VDBG = false;
    public static final int WIFI_AWARE_DATA_PATH_ROLE_INITIATOR = 0;
    public static final int WIFI_AWARE_DATA_PATH_ROLE_RESPONDER = 1;
    private final Context mContext;
    private final Object mLock = new Object();
    private final IWifiAwareManager mService;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface DataPathRole {
    }

    public synchronized WifiAwareManager(Context context, IWifiAwareManager service) {
        this.mContext = context;
        this.mService = service;
    }

    public boolean isAvailable() {
        try {
            return this.mService.isUsageEnabled();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public Characteristics getCharacteristics() {
        try {
            return this.mService.getCharacteristics();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void attach(AttachCallback attachCallback, Handler handler) {
        attach(handler, null, attachCallback, null);
    }

    public void attach(AttachCallback attachCallback, IdentityChangedListener identityChangedListener, Handler handler) {
        attach(handler, null, attachCallback, identityChangedListener);
    }

    public synchronized void attach(Handler handler, ConfigRequest configRequest, AttachCallback attachCallback, IdentityChangedListener identityChangedListener) {
        if (attachCallback == null) {
            throw new IllegalArgumentException("Null callback provided");
        }
        synchronized (this.mLock) {
            try {
                Looper looper = handler == null ? Looper.getMainLooper() : handler.getLooper();
                try {
                    Binder binder = new Binder();
                    this.mService.connect(binder, this.mContext.getOpPackageName(), new WifiAwareEventCallbackProxy(this, looper, binder, attachCallback, identityChangedListener), configRequest, identityChangedListener != null);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public synchronized void disconnect(int clientId, Binder binder) {
        try {
            this.mService.disconnect(clientId, binder);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void publish(int clientId, Looper looper, PublishConfig publishConfig, DiscoverySessionCallback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Null callback provided");
        }
        try {
            this.mService.publish(this.mContext.getOpPackageName(), clientId, publishConfig, new WifiAwareDiscoverySessionCallbackProxy(this, looper, true, callback, clientId));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void updatePublish(int clientId, int sessionId, PublishConfig publishConfig) {
        try {
            this.mService.updatePublish(clientId, sessionId, publishConfig);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void subscribe(int clientId, Looper looper, SubscribeConfig subscribeConfig, DiscoverySessionCallback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Null callback provided");
        }
        try {
            this.mService.subscribe(this.mContext.getOpPackageName(), clientId, subscribeConfig, new WifiAwareDiscoverySessionCallbackProxy(this, looper, false, callback, clientId));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void updateSubscribe(int clientId, int sessionId, SubscribeConfig subscribeConfig) {
        try {
            this.mService.updateSubscribe(clientId, sessionId, subscribeConfig);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void terminateSession(int clientId, int sessionId) {
        try {
            this.mService.terminateSession(clientId, sessionId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public synchronized void sendMessage(int clientId, int sessionId, PeerHandle peerHandle, byte[] message, int messageId, int retryCount) {
        if (peerHandle == null) {
            throw new IllegalArgumentException("sendMessage: invalid peerHandle - must be non-null");
        }
        try {
            this.mService.sendMessage(clientId, sessionId, peerHandle.peerId, message, messageId, retryCount);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x001d, code lost:
        if (android.net.wifi.aware.WifiAwareUtils.isLegacyVersion(r14.mContext, 28) != false) goto L10;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized android.net.NetworkSpecifier createNetworkSpecifier(int r15, int r16, int r17, android.net.wifi.aware.PeerHandle r18, byte[] r19, java.lang.String r20) {
        /*
            r14 = this;
            r10 = r16
            r11 = r18
            r0 = 1
            if (r10 == 0) goto L12
            if (r10 != r0) goto La
            goto L12
        La:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "createNetworkSpecifier: Invalid 'role' argument when creating a network specifier"
            r0.<init>(r1)
            throw r0
        L12:
            if (r10 == 0) goto L20
            r12 = r14
            android.content.Context r1 = r12.mContext
            r2 = 28
            boolean r1 = android.net.wifi.aware.WifiAwareUtils.isLegacyVersion(r1, r2)
            if (r1 != 0) goto L23
            goto L21
        L20:
            r12 = r14
        L21:
            if (r11 == 0) goto L46
        L23:
            android.net.wifi.aware.WifiAwareNetworkSpecifier r13 = new android.net.wifi.aware.WifiAwareNetworkSpecifier
            r1 = 0
            if (r11 != 0) goto L2a
            r2 = r0
            goto L2c
        L2a:
            r2 = r1
        L2c:
            if (r11 == 0) goto L32
            int r0 = r11.peerId
            r5 = r0
            goto L33
        L32:
            r5 = r1
        L33:
            r6 = 0
            int r9 = android.os.Process.myUid()
            r0 = r13
            r1 = r2
            r2 = r10
            r3 = r15
            r4 = r17
            r7 = r19
            r8 = r20
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9)
            return r13
        L46:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "createNetworkSpecifier: Invalid peer handle - cannot be null"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.aware.WifiAwareManager.createNetworkSpecifier(int, int, int, android.net.wifi.aware.PeerHandle, byte[], java.lang.String):android.net.NetworkSpecifier");
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x001d, code lost:
        if (android.net.wifi.aware.WifiAwareUtils.isLegacyVersion(r14.mContext, 28) != false) goto L11;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized android.net.NetworkSpecifier createNetworkSpecifier(int r15, int r16, byte[] r17, byte[] r18, java.lang.String r19) {
        /*
            r14 = this;
            r10 = r16
            r11 = r17
            if (r10 == 0) goto L12
            r0 = 1
            if (r10 != r0) goto La
            goto L12
        La:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "createNetworkSpecifier: Invalid 'role' argument when creating a network specifier"
            r0.<init>(r1)
            throw r0
        L12:
            if (r10 == 0) goto L20
            r12 = r14
            android.content.Context r0 = r12.mContext
            r1 = 28
            boolean r0 = android.net.wifi.aware.WifiAwareUtils.isLegacyVersion(r0, r1)
            if (r0 != 0) goto L23
            goto L21
        L20:
            r12 = r14
        L21:
            if (r11 == 0) goto L4d
        L23:
            if (r11 == 0) goto L32
            int r0 = r11.length
            r1 = 6
            if (r0 != r1) goto L2a
            goto L32
        L2a:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "createNetworkSpecifier: Invalid peer MAC address"
            r0.<init>(r1)
            throw r0
        L32:
            android.net.wifi.aware.WifiAwareNetworkSpecifier r13 = new android.net.wifi.aware.WifiAwareNetworkSpecifier
            if (r11 != 0) goto L39
            r0 = 3
        L37:
            r1 = r0
            goto L3b
        L39:
            r0 = 2
            goto L37
        L3b:
            r4 = 0
            r5 = 0
            int r9 = android.os.Process.myUid()
            r0 = r13
            r2 = r10
            r3 = r15
            r6 = r11
            r7 = r18
            r8 = r19
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8, r9)
            return r13
        L4d:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "createNetworkSpecifier: Invalid peer MAC - cannot be null"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.aware.WifiAwareManager.createNetworkSpecifier(int, int, byte[], byte[], java.lang.String):android.net.NetworkSpecifier");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class WifiAwareEventCallbackProxy extends IWifiAwareEventCallback.Stub {
        private static final int CALLBACK_CONNECT_FAIL = 1;
        private static final int CALLBACK_CONNECT_SUCCESS = 0;
        private static final int CALLBACK_IDENTITY_CHANGED = 2;
        private final WeakReference<WifiAwareManager> mAwareManager;
        private final Binder mBinder;
        private final Handler mHandler;
        private final Looper mLooper;

        synchronized WifiAwareEventCallbackProxy(WifiAwareManager mgr, Looper looper, Binder binder, final AttachCallback attachCallback, final IdentityChangedListener identityChangedListener) {
            this.mAwareManager = new WeakReference<>(mgr);
            this.mLooper = looper;
            this.mBinder = binder;
            this.mHandler = new Handler(looper) { // from class: android.net.wifi.aware.WifiAwareManager.WifiAwareEventCallbackProxy.1
                @Override // android.os.Handler
                public void handleMessage(Message msg) {
                    WifiAwareManager mgr2 = (WifiAwareManager) WifiAwareEventCallbackProxy.this.mAwareManager.get();
                    if (mgr2 == null) {
                        Log.w(WifiAwareManager.TAG, "WifiAwareEventCallbackProxy: handleMessage post GC");
                        return;
                    }
                    switch (msg.what) {
                        case 0:
                            attachCallback.onAttached(new WifiAwareSession(mgr2, WifiAwareEventCallbackProxy.this.mBinder, msg.arg1));
                            return;
                        case 1:
                            WifiAwareEventCallbackProxy.this.mAwareManager.clear();
                            attachCallback.onAttachFailed();
                            return;
                        case 2:
                            if (identityChangedListener == null) {
                                Log.e(WifiAwareManager.TAG, "CALLBACK_IDENTITY_CHANGED: null listener.");
                                return;
                            } else {
                                identityChangedListener.onIdentityChanged((byte[]) msg.obj);
                                return;
                            }
                        default:
                            return;
                    }
                }
            };
        }

        @Override // android.net.wifi.aware.IWifiAwareEventCallback
        public synchronized void onConnectSuccess(int clientId) {
            Message msg = this.mHandler.obtainMessage(0);
            msg.arg1 = clientId;
            this.mHandler.sendMessage(msg);
        }

        @Override // android.net.wifi.aware.IWifiAwareEventCallback
        public synchronized void onConnectFail(int reason) {
            Message msg = this.mHandler.obtainMessage(1);
            msg.arg1 = reason;
            this.mHandler.sendMessage(msg);
        }

        @Override // android.net.wifi.aware.IWifiAwareEventCallback
        public synchronized void onIdentityChanged(byte[] mac) {
            Message msg = this.mHandler.obtainMessage(2);
            msg.obj = mac;
            this.mHandler.sendMessage(msg);
        }
    }

    /* loaded from: classes2.dex */
    private static class WifiAwareDiscoverySessionCallbackProxy extends IWifiAwareDiscoverySessionCallback.Stub {
        private static final int CALLBACK_MATCH = 4;
        private static final int CALLBACK_MATCH_WITH_DISTANCE = 8;
        private static final int CALLBACK_MESSAGE_RECEIVED = 7;
        private static final int CALLBACK_MESSAGE_SEND_FAIL = 6;
        private static final int CALLBACK_MESSAGE_SEND_SUCCESS = 5;
        private static final int CALLBACK_SESSION_CONFIG_FAIL = 2;
        private static final int CALLBACK_SESSION_CONFIG_SUCCESS = 1;
        private static final int CALLBACK_SESSION_STARTED = 0;
        private static final int CALLBACK_SESSION_TERMINATED = 3;
        private static final String MESSAGE_BUNDLE_KEY_MESSAGE = "message";
        private static final String MESSAGE_BUNDLE_KEY_MESSAGE2 = "message2";
        private final WeakReference<WifiAwareManager> mAwareManager;
        private final int mClientId;
        private final Handler mHandler;
        private final boolean mIsPublish;
        private final DiscoverySessionCallback mOriginalCallback;
        private DiscoverySession mSession;

        synchronized WifiAwareDiscoverySessionCallbackProxy(WifiAwareManager mgr, Looper looper, boolean isPublish, DiscoverySessionCallback originalCallback, int clientId) {
            this.mAwareManager = new WeakReference<>(mgr);
            this.mIsPublish = isPublish;
            this.mOriginalCallback = originalCallback;
            this.mClientId = clientId;
            this.mHandler = new Handler(looper) { // from class: android.net.wifi.aware.WifiAwareManager.WifiAwareDiscoverySessionCallbackProxy.1
                @Override // android.os.Handler
                public void handleMessage(Message msg) {
                    List<byte[]> matchFilter;
                    if (WifiAwareDiscoverySessionCallbackProxy.this.mAwareManager.get() == null) {
                        Log.w(WifiAwareManager.TAG, "WifiAwareDiscoverySessionCallbackProxy: handleMessage post GC");
                        return;
                    }
                    switch (msg.what) {
                        case 0:
                            WifiAwareDiscoverySessionCallbackProxy.this.onProxySessionStarted(msg.arg1);
                            return;
                        case 1:
                            WifiAwareDiscoverySessionCallbackProxy.this.mOriginalCallback.onSessionConfigUpdated();
                            return;
                        case 2:
                            WifiAwareDiscoverySessionCallbackProxy.this.mOriginalCallback.onSessionConfigFailed();
                            if (WifiAwareDiscoverySessionCallbackProxy.this.mSession == null) {
                                WifiAwareDiscoverySessionCallbackProxy.this.mAwareManager.clear();
                                return;
                            }
                            return;
                        case 3:
                            WifiAwareDiscoverySessionCallbackProxy.this.onProxySessionTerminated(msg.arg1);
                            return;
                        case 4:
                        case 8:
                            byte[] arg = msg.getData().getByteArray(WifiAwareDiscoverySessionCallbackProxy.MESSAGE_BUNDLE_KEY_MESSAGE2);
                            try {
                                matchFilter = new TlvBufferUtils.TlvIterable(0, 1, arg).toList();
                            } catch (BufferOverflowException e) {
                                matchFilter = null;
                                Log.e(WifiAwareManager.TAG, "onServiceDiscovered: invalid match filter byte array '" + new String(HexEncoding.encode(arg)) + "' - cannot be parsed: e=" + e);
                            }
                            if (msg.what == 4) {
                                WifiAwareDiscoverySessionCallbackProxy.this.mOriginalCallback.onServiceDiscovered(new PeerHandle(msg.arg1), msg.getData().getByteArray("message"), matchFilter);
                                return;
                            } else {
                                WifiAwareDiscoverySessionCallbackProxy.this.mOriginalCallback.onServiceDiscoveredWithinRange(new PeerHandle(msg.arg1), msg.getData().getByteArray("message"), matchFilter, msg.arg2);
                                return;
                            }
                        case 5:
                            WifiAwareDiscoverySessionCallbackProxy.this.mOriginalCallback.onMessageSendSucceeded(msg.arg1);
                            return;
                        case 6:
                            WifiAwareDiscoverySessionCallbackProxy.this.mOriginalCallback.onMessageSendFailed(msg.arg1);
                            return;
                        case 7:
                            WifiAwareDiscoverySessionCallbackProxy.this.mOriginalCallback.onMessageReceived(new PeerHandle(msg.arg1), (byte[]) msg.obj);
                            return;
                        default:
                            return;
                    }
                }
            };
        }

        @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
        public synchronized void onSessionStarted(int sessionId) {
            Message msg = this.mHandler.obtainMessage(0);
            msg.arg1 = sessionId;
            this.mHandler.sendMessage(msg);
        }

        @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
        public synchronized void onSessionConfigSuccess() {
            Message msg = this.mHandler.obtainMessage(1);
            this.mHandler.sendMessage(msg);
        }

        @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
        public synchronized void onSessionConfigFail(int reason) {
            Message msg = this.mHandler.obtainMessage(2);
            msg.arg1 = reason;
            this.mHandler.sendMessage(msg);
        }

        @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
        public synchronized void onSessionTerminated(int reason) {
            Message msg = this.mHandler.obtainMessage(3);
            msg.arg1 = reason;
            this.mHandler.sendMessage(msg);
        }

        private synchronized void onMatchCommon(int messageType, int peerId, byte[] serviceSpecificInfo, byte[] matchFilter, int distanceMm) {
            Bundle data = new Bundle();
            data.putByteArray("message", serviceSpecificInfo);
            data.putByteArray(MESSAGE_BUNDLE_KEY_MESSAGE2, matchFilter);
            Message msg = this.mHandler.obtainMessage(messageType);
            msg.arg1 = peerId;
            msg.arg2 = distanceMm;
            msg.setData(data);
            this.mHandler.sendMessage(msg);
        }

        @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
        public synchronized void onMatch(int peerId, byte[] serviceSpecificInfo, byte[] matchFilter) {
            onMatchCommon(4, peerId, serviceSpecificInfo, matchFilter, 0);
        }

        @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
        public synchronized void onMatchWithDistance(int peerId, byte[] serviceSpecificInfo, byte[] matchFilter, int distanceMm) {
            onMatchCommon(8, peerId, serviceSpecificInfo, matchFilter, distanceMm);
        }

        @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
        public synchronized void onMessageSendSuccess(int messageId) {
            Message msg = this.mHandler.obtainMessage(5);
            msg.arg1 = messageId;
            this.mHandler.sendMessage(msg);
        }

        @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
        public synchronized void onMessageSendFail(int messageId, int reason) {
            Message msg = this.mHandler.obtainMessage(6);
            msg.arg1 = messageId;
            msg.arg2 = reason;
            this.mHandler.sendMessage(msg);
        }

        @Override // android.net.wifi.aware.IWifiAwareDiscoverySessionCallback
        public synchronized void onMessageReceived(int peerId, byte[] message) {
            Message msg = this.mHandler.obtainMessage(7);
            msg.arg1 = peerId;
            msg.obj = message;
            this.mHandler.sendMessage(msg);
        }

        public synchronized void onProxySessionStarted(int sessionId) {
            if (this.mSession != null) {
                Log.e(WifiAwareManager.TAG, "onSessionStarted: sessionId=" + sessionId + ": session already created!?");
                throw new IllegalStateException("onSessionStarted: sessionId=" + sessionId + ": session already created!?");
            }
            WifiAwareManager mgr = this.mAwareManager.get();
            if (mgr == null) {
                Log.w(WifiAwareManager.TAG, "onProxySessionStarted: mgr GC'd");
            } else if (this.mIsPublish) {
                PublishDiscoverySession session = new PublishDiscoverySession(mgr, this.mClientId, sessionId);
                this.mSession = session;
                this.mOriginalCallback.onPublishStarted(session);
            } else {
                SubscribeDiscoverySession session2 = new SubscribeDiscoverySession(mgr, this.mClientId, sessionId);
                this.mSession = session2;
                this.mOriginalCallback.onSubscribeStarted(session2);
            }
        }

        public synchronized void onProxySessionTerminated(int reason) {
            if (this.mSession != null) {
                this.mSession.setTerminated();
                this.mSession = null;
            } else {
                Log.w(WifiAwareManager.TAG, "Proxy: onSessionTerminated called but mSession is null!?");
            }
            this.mAwareManager.clear();
            this.mOriginalCallback.onSessionTerminated();
        }
    }
}
