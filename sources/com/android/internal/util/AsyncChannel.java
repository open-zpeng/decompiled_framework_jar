package com.android.internal.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Slog;
import java.util.Objects;
import java.util.Stack;
/* loaded from: classes3.dex */
public class AsyncChannel {
    private static final int BASE = 69632;
    public static final int CMD_CHANNEL_DISCONNECT = 69635;
    public static final int CMD_CHANNEL_DISCONNECTED = 69636;
    public static final int CMD_CHANNEL_FULLY_CONNECTED = 69634;
    private protected static final int CMD_CHANNEL_FULL_CONNECTION = 69633;
    private protected static final int CMD_CHANNEL_HALF_CONNECTED = 69632;
    private static final int CMD_TO_STRING_COUNT = 5;
    private static final boolean DBG = false;
    public static final int STATUS_BINDING_UNSUCCESSFUL = 1;
    public static final int STATUS_FULL_CONNECTION_REFUSED_ALREADY_CONNECTED = 3;
    public static final int STATUS_REMOTE_DISCONNECTION = 4;
    public static final int STATUS_SEND_UNSUCCESSFUL = 2;
    private protected static final int STATUS_SUCCESSFUL = 0;
    private static final String TAG = "AsyncChannel";
    private static String[] sCmdToString = new String[5];
    private AsyncChannelConnection mConnection;
    private DeathMonitor mDeathMonitor;
    private Messenger mDstMessenger;
    private Context mSrcContext;
    private Handler mSrcHandler;
    private Messenger mSrcMessenger;

    static {
        sCmdToString[0] = "CMD_CHANNEL_HALF_CONNECTED";
        sCmdToString[1] = "CMD_CHANNEL_FULL_CONNECTION";
        sCmdToString[2] = "CMD_CHANNEL_FULLY_CONNECTED";
        sCmdToString[3] = "CMD_CHANNEL_DISCONNECT";
        sCmdToString[4] = "CMD_CHANNEL_DISCONNECTED";
    }

    public private static String cmdToString(int cmd) {
        int cmd2 = cmd - Protocol.BASE_SYSTEM_ASYNC_CHANNEL;
        if (cmd2 >= 0 && cmd2 < sCmdToString.length) {
            return sCmdToString[cmd2];
        }
        return null;
    }

    public synchronized int connectSrcHandlerToPackageSync(Context srcContext, Handler srcHandler, String dstPackageName, String dstClassName) {
        this.mConnection = new AsyncChannelConnection();
        this.mSrcContext = srcContext;
        this.mSrcHandler = srcHandler;
        this.mSrcMessenger = new Messenger(srcHandler);
        this.mDstMessenger = null;
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName(dstPackageName, dstClassName);
        boolean result = srcContext.bindService(intent, this.mConnection, 1);
        return !result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int connectSync(Context srcContext, Handler srcHandler, Messenger dstMessenger) {
        connected(srcContext, srcHandler, dstMessenger);
        return 0;
    }

    public synchronized int connectSync(Context srcContext, Handler srcHandler, Handler dstHandler) {
        return connectSync(srcContext, srcHandler, new Messenger(dstHandler));
    }

    public synchronized int fullyConnectSync(Context srcContext, Handler srcHandler, Handler dstHandler) {
        int status = connectSync(srcContext, srcHandler, dstHandler);
        if (status == 0) {
            Message response = sendMessageSynchronously(CMD_CHANNEL_FULL_CONNECTION);
            return response.arg1;
        }
        return status;
    }

    public synchronized void connect(Context srcContext, Handler srcHandler, String dstPackageName, String dstClassName) {
        new Thread(new Runnable(srcContext, srcHandler, dstPackageName, dstClassName) { // from class: com.android.internal.util.AsyncChannel.1ConnectAsync
            String mDstClassName;
            String mDstPackageName;
            Context mSrcCtx;
            Handler mSrcHdlr;

            {
                this.mSrcCtx = srcContext;
                this.mSrcHdlr = srcHandler;
                this.mDstPackageName = dstPackageName;
                this.mDstClassName = dstClassName;
            }

            @Override // java.lang.Runnable
            public void run() {
                int result = AsyncChannel.this.connectSrcHandlerToPackageSync(this.mSrcCtx, this.mSrcHdlr, this.mDstPackageName, this.mDstClassName);
                AsyncChannel.this.replyHalfConnected(result);
            }
        }).start();
    }

    public synchronized void connect(Context srcContext, Handler srcHandler, Class<?> klass) {
        connect(srcContext, srcHandler, klass.getPackage().getName(), klass.getName());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connect(Context srcContext, Handler srcHandler, Messenger dstMessenger) {
        connected(srcContext, srcHandler, dstMessenger);
        replyHalfConnected(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connected(Context srcContext, Handler srcHandler, Messenger dstMessenger) {
        this.mSrcContext = srcContext;
        this.mSrcHandler = srcHandler;
        this.mSrcMessenger = new Messenger(this.mSrcHandler);
        this.mDstMessenger = dstMessenger;
    }

    public synchronized void connect(Context srcContext, Handler srcHandler, Handler dstHandler) {
        connect(srcContext, srcHandler, new Messenger(dstHandler));
    }

    public synchronized void connect(AsyncService srcAsyncService, Messenger dstMessenger) {
        connect(srcAsyncService, srcAsyncService.getHandler(), dstMessenger);
    }

    public synchronized void disconnected() {
        this.mSrcContext = null;
        this.mSrcHandler = null;
        this.mSrcMessenger = null;
        this.mDstMessenger = null;
        this.mDeathMonitor = null;
        this.mConnection = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disconnect() {
        if (this.mConnection != null && this.mSrcContext != null) {
            this.mSrcContext.unbindService(this.mConnection);
            this.mConnection = null;
        }
        try {
            Message msg = Message.obtain();
            msg.what = CMD_CHANNEL_DISCONNECTED;
            msg.replyTo = this.mSrcMessenger;
            this.mDstMessenger.send(msg);
        } catch (Exception e) {
        }
        replyDisconnected(0);
        this.mSrcHandler = null;
        if (this.mConnection == null && this.mDstMessenger != null && this.mDeathMonitor != null) {
            this.mDstMessenger.getBinder().unlinkToDeath(this.mDeathMonitor, 0);
            this.mDeathMonitor = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMessage(Message msg) {
        msg.replyTo = this.mSrcMessenger;
        try {
            this.mDstMessenger.send(msg);
        } catch (RemoteException e) {
            replyDisconnected(2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMessage(int what) {
        Message msg = Message.obtain();
        msg.what = what;
        sendMessage(msg);
    }

    private protected void sendMessage(int what, int arg1) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = arg1;
        sendMessage(msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMessage(int what, int arg1, int arg2) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        sendMessage(msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMessage(int what, int arg1, int arg2, Object obj) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.obj = obj;
        sendMessage(msg);
    }

    public synchronized void sendMessage(int what, Object obj) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        sendMessage(msg);
    }

    private protected void replyToMessage(Message srcMsg, Message dstMsg) {
        try {
            dstMsg.replyTo = this.mSrcMessenger;
            srcMsg.replyTo.send(dstMsg);
        } catch (RemoteException e) {
            log("TODO: handle replyToMessage RemoteException" + e);
            e.printStackTrace();
        }
    }

    private protected void replyToMessage(Message srcMsg, int what) {
        Message msg = Message.obtain();
        msg.what = what;
        replyToMessage(srcMsg, msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void replyToMessage(Message srcMsg, int what, int arg1) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = arg1;
        replyToMessage(srcMsg, msg);
    }

    public synchronized void replyToMessage(Message srcMsg, int what, int arg1, int arg2) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        replyToMessage(srcMsg, msg);
    }

    private protected void replyToMessage(Message srcMsg, int what, int arg1, int arg2, Object obj) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.obj = obj;
        replyToMessage(srcMsg, msg);
    }

    private protected void replyToMessage(Message srcMsg, int what, Object obj) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        replyToMessage(srcMsg, msg);
    }

    private protected Message sendMessageSynchronously(Message msg) {
        Message resultMsg = SyncMessenger.sendMessageSynchronously(this.mDstMessenger, msg);
        return resultMsg;
    }

    public synchronized Message sendMessageSynchronously(int what) {
        Message msg = Message.obtain();
        msg.what = what;
        Message resultMsg = sendMessageSynchronously(msg);
        return resultMsg;
    }

    public synchronized Message sendMessageSynchronously(int what, int arg1) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = arg1;
        Message resultMsg = sendMessageSynchronously(msg);
        return resultMsg;
    }

    private protected Message sendMessageSynchronously(int what, int arg1, int arg2) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        Message resultMsg = sendMessageSynchronously(msg);
        return resultMsg;
    }

    public synchronized Message sendMessageSynchronously(int what, int arg1, int arg2, Object obj) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        msg.obj = obj;
        Message resultMsg = sendMessageSynchronously(msg);
        return resultMsg;
    }

    public synchronized Message sendMessageSynchronously(int what, Object obj) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        Message resultMsg = sendMessageSynchronously(msg);
        return resultMsg;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class SyncMessenger {
        private SyncHandler mHandler;
        private HandlerThread mHandlerThread;
        private Messenger mMessenger;
        private static Stack<SyncMessenger> sStack = new Stack<>();
        private static int sCount = 0;

        private synchronized SyncMessenger() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public class SyncHandler extends Handler {
            private Object mLockObject;
            private Message mResultMsg;

            private SyncHandler(Looper looper) {
                super(looper);
                this.mLockObject = new Object();
            }

            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                Message msgCopy = Message.obtain();
                msgCopy.copyFrom(msg);
                synchronized (this.mLockObject) {
                    this.mResultMsg = msgCopy;
                    this.mLockObject.notify();
                }
            }
        }

        private static synchronized SyncMessenger obtain() {
            SyncMessenger sm;
            synchronized (sStack) {
                if (sStack.isEmpty()) {
                    sm = new SyncMessenger();
                    StringBuilder sb = new StringBuilder();
                    sb.append("SyncHandler-");
                    int i = sCount;
                    sCount = i + 1;
                    sb.append(i);
                    sm.mHandlerThread = new HandlerThread(sb.toString());
                    sm.mHandlerThread.start();
                    Objects.requireNonNull(sm);
                    sm.mHandler = new SyncHandler(sm.mHandlerThread.getLooper());
                    sm.mMessenger = new Messenger(sm.mHandler);
                } else {
                    sm = sStack.pop();
                }
            }
            return sm;
        }

        private synchronized void recycle() {
            synchronized (sStack) {
                sStack.push(this);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized Message sendMessageSynchronously(Messenger dstMessenger, Message msg) {
            SyncMessenger sm = obtain();
            Message resultMsg = null;
            if (dstMessenger != null && msg != null) {
                try {
                    msg.replyTo = sm.mMessenger;
                    synchronized (sm.mHandler.mLockObject) {
                        if (sm.mHandler.mResultMsg != null) {
                            Slog.wtf(AsyncChannel.TAG, "mResultMsg should be null here");
                            sm.mHandler.mResultMsg = null;
                        }
                        dstMessenger.send(msg);
                        sm.mHandler.mLockObject.wait();
                        resultMsg = sm.mHandler.mResultMsg;
                        sm.mHandler.mResultMsg = null;
                    }
                } catch (RemoteException e) {
                    Slog.e(AsyncChannel.TAG, "error in sendMessageSynchronously", e);
                } catch (InterruptedException e2) {
                    Slog.e(AsyncChannel.TAG, "error in sendMessageSynchronously", e2);
                }
            }
            sm.recycle();
            return resultMsg;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void replyHalfConnected(int status) {
        Message msg = this.mSrcHandler.obtainMessage(Protocol.BASE_SYSTEM_ASYNC_CHANNEL);
        msg.arg1 = status;
        msg.obj = this;
        msg.replyTo = this.mDstMessenger;
        if (!linkToDeathMonitor()) {
            msg.arg1 = 1;
        }
        this.mSrcHandler.sendMessage(msg);
    }

    private synchronized boolean linkToDeathMonitor() {
        if (this.mConnection == null && this.mDeathMonitor == null) {
            this.mDeathMonitor = new DeathMonitor();
            try {
                this.mDstMessenger.getBinder().linkToDeath(this.mDeathMonitor, 0);
                return true;
            } catch (RemoteException e) {
                this.mDeathMonitor = null;
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void replyDisconnected(int status) {
        if (this.mSrcHandler == null) {
            return;
        }
        Message msg = this.mSrcHandler.obtainMessage(CMD_CHANNEL_DISCONNECTED);
        msg.arg1 = status;
        msg.obj = this;
        msg.replyTo = this.mDstMessenger;
        this.mSrcHandler.sendMessage(msg);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class AsyncChannelConnection implements ServiceConnection {
        AsyncChannelConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName className, IBinder service) {
            AsyncChannel.this.mDstMessenger = new Messenger(service);
            AsyncChannel.this.replyHalfConnected(0);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName className) {
            AsyncChannel.this.replyDisconnected(0);
        }
    }

    private static synchronized void log(String s) {
        Slog.d(TAG, s);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public final class DeathMonitor implements IBinder.DeathRecipient {
        DeathMonitor() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            AsyncChannel.this.replyDisconnected(4);
        }
    }
}
