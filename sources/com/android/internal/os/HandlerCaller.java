package com.android.internal.os;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
/* loaded from: classes3.dex */
public class HandlerCaller {
    final Callback mCallback;
    final Handler mH;
    final Looper mMainLooper;

    /* loaded from: classes3.dex */
    public interface Callback {
        synchronized void executeMessage(Message message);
    }

    /* loaded from: classes3.dex */
    class MyHandler extends Handler {
        MyHandler(Looper looper, boolean async) {
            super(looper, null, async);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            HandlerCaller.this.mCallback.executeMessage(msg);
        }
    }

    public synchronized HandlerCaller(Context context, Looper looper, Callback callback, boolean asyncHandler) {
        this.mMainLooper = looper != null ? looper : context.getMainLooper();
        this.mH = new MyHandler(this.mMainLooper, asyncHandler);
        this.mCallback = callback;
    }

    public synchronized Handler getHandler() {
        return this.mH;
    }

    public synchronized void executeOrSendMessage(Message msg) {
        if (Looper.myLooper() == this.mMainLooper) {
            this.mCallback.executeMessage(msg);
            msg.recycle();
            return;
        }
        this.mH.sendMessage(msg);
    }

    public synchronized void sendMessageDelayed(Message msg, long delayMillis) {
        this.mH.sendMessageDelayed(msg, delayMillis);
    }

    public synchronized boolean hasMessages(int what) {
        return this.mH.hasMessages(what);
    }

    public synchronized void removeMessages(int what) {
        this.mH.removeMessages(what);
    }

    public synchronized void removeMessages(int what, Object obj) {
        this.mH.removeMessages(what, obj);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMessage(Message msg) {
        this.mH.sendMessage(msg);
    }

    public synchronized SomeArgs sendMessageAndWait(Message msg) {
        if (Looper.myLooper() == this.mH.getLooper()) {
            throw new IllegalStateException("Can't wait on same thread as looper");
        }
        SomeArgs args = (SomeArgs) msg.obj;
        args.mWaitState = 1;
        this.mH.sendMessage(msg);
        synchronized (args) {
            while (args.mWaitState == 1) {
                try {
                    args.wait();
                } catch (InterruptedException e) {
                    return null;
                }
            }
        }
        args.mWaitState = 0;
        return args;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Message obtainMessage(int what) {
        return this.mH.obtainMessage(what);
    }

    public synchronized Message obtainMessageBO(int what, boolean arg1, Object arg2) {
        return this.mH.obtainMessage(what, arg1 ? 1 : 0, 0, arg2);
    }

    public synchronized Message obtainMessageBOO(int what, boolean arg1, Object arg2, Object arg3) {
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = arg2;
        args.arg2 = arg3;
        return this.mH.obtainMessage(what, arg1 ? 1 : 0, 0, args);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Message obtainMessageO(int what, Object arg1) {
        return this.mH.obtainMessage(what, 0, 0, arg1);
    }

    public synchronized Message obtainMessageI(int what, int arg1) {
        return this.mH.obtainMessage(what, arg1, 0);
    }

    public synchronized Message obtainMessageII(int what, int arg1, int arg2) {
        return this.mH.obtainMessage(what, arg1, arg2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Message obtainMessageIO(int what, int arg1, Object arg2) {
        return this.mH.obtainMessage(what, arg1, 0, arg2);
    }

    public synchronized Message obtainMessageIIO(int what, int arg1, int arg2, Object arg3) {
        return this.mH.obtainMessage(what, arg1, arg2, arg3);
    }

    public synchronized Message obtainMessageIIOO(int what, int arg1, int arg2, Object arg3, Object arg4) {
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = arg3;
        args.arg2 = arg4;
        return this.mH.obtainMessage(what, arg1, arg2, args);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Message obtainMessageIOO(int what, int arg1, Object arg2, Object arg3) {
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = arg2;
        args.arg2 = arg3;
        return this.mH.obtainMessage(what, arg1, 0, args);
    }

    public synchronized Message obtainMessageIOOO(int what, int arg1, Object arg2, Object arg3, Object arg4) {
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = arg2;
        args.arg2 = arg3;
        args.arg3 = arg4;
        return this.mH.obtainMessage(what, arg1, 0, args);
    }

    public synchronized Message obtainMessageIIOOO(int what, int arg1, int arg2, Object arg3, Object arg4, Object arg5) {
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = arg3;
        args.arg2 = arg4;
        args.arg3 = arg5;
        return this.mH.obtainMessage(what, arg1, arg2, args);
    }

    public synchronized Message obtainMessageIIOOOO(int what, int arg1, int arg2, Object arg3, Object arg4, Object arg5, Object arg6) {
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = arg3;
        args.arg2 = arg4;
        args.arg3 = arg5;
        args.arg4 = arg6;
        return this.mH.obtainMessage(what, arg1, arg2, args);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Message obtainMessageOO(int what, Object arg1, Object arg2) {
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = arg1;
        args.arg2 = arg2;
        return this.mH.obtainMessage(what, 0, 0, args);
    }

    private protected Message obtainMessageOOO(int what, Object arg1, Object arg2, Object arg3) {
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = arg1;
        args.arg2 = arg2;
        args.arg3 = arg3;
        return this.mH.obtainMessage(what, 0, 0, args);
    }

    public synchronized Message obtainMessageOOOO(int what, Object arg1, Object arg2, Object arg3, Object arg4) {
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = arg1;
        args.arg2 = arg2;
        args.arg3 = arg3;
        args.arg4 = arg4;
        return this.mH.obtainMessage(what, 0, 0, args);
    }

    public synchronized Message obtainMessageOOOOO(int what, Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) {
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = arg1;
        args.arg2 = arg2;
        args.arg3 = arg3;
        args.arg4 = arg4;
        args.arg5 = arg5;
        return this.mH.obtainMessage(what, 0, 0, args);
    }

    public synchronized Message obtainMessageOOOOII(int what, Object arg1, Object arg2, Object arg3, Object arg4, int arg5, int arg6) {
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = arg1;
        args.arg2 = arg2;
        args.arg3 = arg3;
        args.arg4 = arg4;
        args.argi5 = arg5;
        args.argi6 = arg6;
        return this.mH.obtainMessage(what, 0, 0, args);
    }

    public synchronized Message obtainMessageIIII(int what, int arg1, int arg2, int arg3, int arg4) {
        SomeArgs args = SomeArgs.obtain();
        args.argi1 = arg1;
        args.argi2 = arg2;
        args.argi3 = arg3;
        args.argi4 = arg4;
        return this.mH.obtainMessage(what, 0, 0, args);
    }

    public synchronized Message obtainMessageIIIIII(int what, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6) {
        SomeArgs args = SomeArgs.obtain();
        args.argi1 = arg1;
        args.argi2 = arg2;
        args.argi3 = arg3;
        args.argi4 = arg4;
        args.argi5 = arg5;
        args.argi6 = arg6;
        return this.mH.obtainMessage(what, 0, 0, args);
    }

    public synchronized Message obtainMessageIIIIO(int what, int arg1, int arg2, int arg3, int arg4, Object arg5) {
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = arg5;
        args.argi1 = arg1;
        args.argi2 = arg2;
        args.argi3 = arg3;
        args.argi4 = arg4;
        return this.mH.obtainMessage(what, 0, 0, args);
    }
}
