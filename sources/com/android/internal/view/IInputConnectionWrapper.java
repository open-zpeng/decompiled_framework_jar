package com.android.internal.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionInspector;
import android.view.inputmethod.InputContentInfo;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.SomeArgs;
import com.android.internal.view.IInputContext;
/* loaded from: classes3.dex */
public abstract class IInputConnectionWrapper extends IInputContext.Stub {
    private static final boolean DEBUG = false;
    private static final int DO_BEGIN_BATCH_EDIT = 90;
    private static final int DO_CLEAR_META_KEY_STATES = 130;
    private static final int DO_CLOSE_CONNECTION = 150;
    private static final int DO_COMMIT_COMPLETION = 55;
    private static final int DO_COMMIT_CONTENT = 160;
    private static final int DO_COMMIT_CORRECTION = 56;
    private static final int DO_COMMIT_TEXT = 50;
    private static final int DO_DELETE_SURROUNDING_TEXT = 80;
    private static final int DO_DELETE_SURROUNDING_TEXT_IN_CODE_POINTS = 81;
    private static final int DO_END_BATCH_EDIT = 95;
    private static final int DO_FINISH_COMPOSING_TEXT = 65;
    private static final int DO_GET_CURSOR_CAPS_MODE = 30;
    private static final int DO_GET_EXTRACTED_TEXT = 40;
    private static final int DO_GET_SELECTED_TEXT = 25;
    private static final int DO_GET_TEXT_AFTER_CURSOR = 10;
    private static final int DO_GET_TEXT_BEFORE_CURSOR = 20;
    private static final int DO_PERFORM_CONTEXT_MENU_ACTION = 59;
    private static final int DO_PERFORM_EDITOR_ACTION = 58;
    private static final int DO_PERFORM_PRIVATE_COMMAND = 120;
    private static final int DO_REQUEST_UPDATE_CURSOR_ANCHOR_INFO = 140;
    private static final int DO_SEND_KEY_EVENT = 70;
    private static final int DO_SET_COMPOSING_REGION = 63;
    private static final int DO_SET_COMPOSING_TEXT = 60;
    private static final int DO_SET_SELECTION = 57;
    private static final String TAG = "IInputConnectionWrapper";
    private Handler mH;
    @GuardedBy("mLock")
    public protected InputConnection mInputConnection;
    private Looper mMainLooper;
    public protected Object mLock = new Object();
    @GuardedBy("mLock")
    private boolean mFinished = false;

    protected abstract synchronized boolean isActive();

    protected abstract synchronized void onUserAction();

    /* loaded from: classes3.dex */
    class MyHandler extends Handler {
        MyHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            IInputConnectionWrapper.this.executeMessage(msg);
        }
    }

    public synchronized IInputConnectionWrapper(Looper mainLooper, InputConnection inputConnection) {
        this.mInputConnection = inputConnection;
        this.mMainLooper = mainLooper;
        this.mH = new MyHandler(this.mMainLooper);
    }

    public synchronized InputConnection getInputConnection() {
        InputConnection inputConnection;
        synchronized (this.mLock) {
            inputConnection = this.mInputConnection;
        }
        return inputConnection;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized boolean isFinished() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mFinished;
        }
        return z;
    }

    @Override // com.android.internal.view.IInputContext
    public synchronized void getTextAfterCursor(int length, int flags, int seq, IInputContextCallback callback) {
        dispatchMessage(obtainMessageIISC(10, length, flags, seq, callback));
    }

    @Override // com.android.internal.view.IInputContext
    public synchronized void getTextBeforeCursor(int length, int flags, int seq, IInputContextCallback callback) {
        dispatchMessage(obtainMessageIISC(20, length, flags, seq, callback));
    }

    @Override // com.android.internal.view.IInputContext
    public synchronized void getSelectedText(int flags, int seq, IInputContextCallback callback) {
        dispatchMessage(obtainMessageISC(25, flags, seq, callback));
    }

    @Override // com.android.internal.view.IInputContext
    public synchronized void getCursorCapsMode(int reqModes, int seq, IInputContextCallback callback) {
        dispatchMessage(obtainMessageISC(30, reqModes, seq, callback));
    }

    @Override // com.android.internal.view.IInputContext
    public synchronized void getExtractedText(ExtractedTextRequest request, int flags, int seq, IInputContextCallback callback) {
        dispatchMessage(obtainMessageIOSC(40, flags, request, seq, callback));
    }

    @Override // com.android.internal.view.IInputContext
    public synchronized void commitText(CharSequence text, int newCursorPosition) {
        dispatchMessage(obtainMessageIO(50, newCursorPosition, text));
    }

    @Override // com.android.internal.view.IInputContext
    public synchronized void commitCompletion(CompletionInfo text) {
        dispatchMessage(obtainMessageO(55, text));
    }

    @Override // com.android.internal.view.IInputContext
    public synchronized void commitCorrection(CorrectionInfo info) {
        dispatchMessage(obtainMessageO(56, info));
    }

    @Override // com.android.internal.view.IInputContext
    public synchronized void setSelection(int start, int end) {
        dispatchMessage(obtainMessageII(57, start, end));
    }

    @Override // com.android.internal.view.IInputContext
    public synchronized void performEditorAction(int id) {
        dispatchMessage(obtainMessageII(58, id, 0));
    }

    @Override // com.android.internal.view.IInputContext
    public synchronized void performContextMenuAction(int id) {
        dispatchMessage(obtainMessageII(59, id, 0));
    }

    @Override // com.android.internal.view.IInputContext
    public synchronized void setComposingRegion(int start, int end) {
        dispatchMessage(obtainMessageII(63, start, end));
    }

    @Override // com.android.internal.view.IInputContext
    public synchronized void setComposingText(CharSequence text, int newCursorPosition) {
        dispatchMessage(obtainMessageIO(60, newCursorPosition, text));
    }

    @Override // com.android.internal.view.IInputContext
    public synchronized void finishComposingText() {
        dispatchMessage(obtainMessage(65));
    }

    @Override // com.android.internal.view.IInputContext
    public synchronized void sendKeyEvent(KeyEvent event) {
        dispatchMessage(obtainMessageO(70, event));
    }

    @Override // com.android.internal.view.IInputContext
    public synchronized void clearMetaKeyStates(int states) {
        dispatchMessage(obtainMessageII(130, states, 0));
    }

    @Override // com.android.internal.view.IInputContext
    public synchronized void deleteSurroundingText(int beforeLength, int afterLength) {
        dispatchMessage(obtainMessageII(80, beforeLength, afterLength));
    }

    @Override // com.android.internal.view.IInputContext
    public synchronized void deleteSurroundingTextInCodePoints(int beforeLength, int afterLength) {
        dispatchMessage(obtainMessageII(81, beforeLength, afterLength));
    }

    @Override // com.android.internal.view.IInputContext
    public synchronized void beginBatchEdit() {
        dispatchMessage(obtainMessage(90));
    }

    @Override // com.android.internal.view.IInputContext
    public synchronized void endBatchEdit() {
        dispatchMessage(obtainMessage(95));
    }

    @Override // com.android.internal.view.IInputContext
    public synchronized void performPrivateCommand(String action, Bundle data) {
        dispatchMessage(obtainMessageOO(120, action, data));
    }

    @Override // com.android.internal.view.IInputContext
    public synchronized void requestUpdateCursorAnchorInfo(int cursorUpdateMode, int seq, IInputContextCallback callback) {
        dispatchMessage(obtainMessageISC(140, cursorUpdateMode, seq, callback));
    }

    public synchronized void closeConnection() {
        dispatchMessage(obtainMessage(150));
    }

    @Override // com.android.internal.view.IInputContext
    public synchronized void commitContent(InputContentInfo inputContentInfo, int flags, Bundle opts, int seq, IInputContextCallback callback) {
        dispatchMessage(obtainMessageIOOSC(160, flags, inputContentInfo, opts, seq, callback));
    }

    synchronized void dispatchMessage(Message msg) {
        if (Looper.myLooper() == this.mMainLooper) {
            executeMessage(msg);
            msg.recycle();
            return;
        }
        this.mH.sendMessage(msg);
    }

    synchronized void executeMessage(Message msg) {
        SomeArgs args;
        IInputContextCallback callback;
        int callbackSeq;
        InputConnection ic;
        IInputContextCallback callback2;
        int callbackSeq2;
        InputConnection ic2;
        IInputContextCallback callback3;
        int callbackSeq3;
        InputConnection ic3;
        IInputContextCallback callback4;
        int callbackSeq4;
        InputConnection ic4;
        IInputContextCallback callback5;
        int callbackSeq5;
        InputConnection ic5;
        IInputContextCallback callback6;
        int callbackSeq6;
        InputConnection ic6;
        IInputContextCallback callback7;
        int callbackSeq7;
        InputConnection ic7;
        int i = msg.what;
        switch (i) {
            case 55:
                InputConnection ic8 = getInputConnection();
                if (ic8 == null || !isActive()) {
                    Log.w(TAG, "commitCompletion on inactive InputConnection");
                    return;
                } else {
                    ic8.commitCompletion((CompletionInfo) msg.obj);
                    return;
                }
            case 56:
                InputConnection ic9 = getInputConnection();
                if (ic9 == null || !isActive()) {
                    Log.w(TAG, "commitCorrection on inactive InputConnection");
                    return;
                } else {
                    ic9.commitCorrection((CorrectionInfo) msg.obj);
                    return;
                }
            case 57:
                InputConnection ic10 = getInputConnection();
                if (ic10 == null || !isActive()) {
                    Log.w(TAG, "setSelection on inactive InputConnection");
                    return;
                } else {
                    ic10.setSelection(msg.arg1, msg.arg2);
                    return;
                }
            case 58:
                InputConnection ic11 = getInputConnection();
                if (ic11 == null || !isActive()) {
                    Log.w(TAG, "performEditorAction on inactive InputConnection");
                    return;
                } else {
                    ic11.performEditorAction(msg.arg1);
                    return;
                }
            case 59:
                InputConnection ic12 = getInputConnection();
                if (ic12 == null || !isActive()) {
                    Log.w(TAG, "performContextMenuAction on inactive InputConnection");
                    return;
                } else {
                    ic12.performContextMenuAction(msg.arg1);
                    return;
                }
            case 60:
                InputConnection ic13 = getInputConnection();
                if (ic13 == null || !isActive()) {
                    Log.w(TAG, "setComposingText on inactive InputConnection");
                    return;
                }
                ic13.setComposingText((CharSequence) msg.obj, msg.arg1);
                onUserAction();
                return;
            default:
                switch (i) {
                    case 80:
                        InputConnection ic14 = getInputConnection();
                        if (ic14 == null || !isActive()) {
                            Log.w(TAG, "deleteSurroundingText on inactive InputConnection");
                            return;
                        } else {
                            ic14.deleteSurroundingText(msg.arg1, msg.arg2);
                            return;
                        }
                    case 81:
                        InputConnection ic15 = getInputConnection();
                        if (ic15 == null || !isActive()) {
                            Log.w(TAG, "deleteSurroundingTextInCodePoints on inactive InputConnection");
                            return;
                        } else {
                            ic15.deleteSurroundingTextInCodePoints(msg.arg1, msg.arg2);
                            return;
                        }
                    default:
                        switch (i) {
                            case 10:
                                args = (SomeArgs) msg.obj;
                                try {
                                    try {
                                        callback = (IInputContextCallback) args.arg6;
                                        callbackSeq = args.argi6;
                                        ic = getInputConnection();
                                    } finally {
                                    }
                                } catch (RemoteException e) {
                                    Log.w(TAG, "Got RemoteException calling setTextAfterCursor", e);
                                }
                                if (ic != null && isActive()) {
                                    callback.setTextAfterCursor(ic.getTextAfterCursor(msg.arg1, msg.arg2), callbackSeq);
                                    return;
                                }
                                Log.w(TAG, "getTextAfterCursor on inactive InputConnection");
                                callback.setTextAfterCursor(null, callbackSeq);
                                return;
                            case 20:
                                args = (SomeArgs) msg.obj;
                                try {
                                    try {
                                        callback2 = (IInputContextCallback) args.arg6;
                                        callbackSeq2 = args.argi6;
                                        ic2 = getInputConnection();
                                    } finally {
                                    }
                                } catch (RemoteException e2) {
                                    Log.w(TAG, "Got RemoteException calling setTextBeforeCursor", e2);
                                }
                                if (ic2 != null && isActive()) {
                                    callback2.setTextBeforeCursor(ic2.getTextBeforeCursor(msg.arg1, msg.arg2), callbackSeq2);
                                    return;
                                }
                                Log.w(TAG, "getTextBeforeCursor on inactive InputConnection");
                                callback2.setTextBeforeCursor(null, callbackSeq2);
                                return;
                            case 25:
                                args = (SomeArgs) msg.obj;
                                try {
                                    try {
                                        callback3 = (IInputContextCallback) args.arg6;
                                        callbackSeq3 = args.argi6;
                                        ic3 = getInputConnection();
                                    } finally {
                                    }
                                } catch (RemoteException e3) {
                                    Log.w(TAG, "Got RemoteException calling setSelectedText", e3);
                                }
                                if (ic3 != null && isActive()) {
                                    callback3.setSelectedText(ic3.getSelectedText(msg.arg1), callbackSeq3);
                                    return;
                                }
                                Log.w(TAG, "getSelectedText on inactive InputConnection");
                                callback3.setSelectedText(null, callbackSeq3);
                                return;
                            case 30:
                                args = (SomeArgs) msg.obj;
                                try {
                                    try {
                                        callback4 = (IInputContextCallback) args.arg6;
                                        callbackSeq4 = args.argi6;
                                        ic4 = getInputConnection();
                                    } finally {
                                    }
                                } catch (RemoteException e4) {
                                    Log.w(TAG, "Got RemoteException calling setCursorCapsMode", e4);
                                }
                                if (ic4 != null && isActive()) {
                                    callback4.setCursorCapsMode(ic4.getCursorCapsMode(msg.arg1), callbackSeq4);
                                    return;
                                }
                                Log.w(TAG, "getCursorCapsMode on inactive InputConnection");
                                callback4.setCursorCapsMode(0, callbackSeq4);
                                return;
                            case 40:
                                args = (SomeArgs) msg.obj;
                                try {
                                    try {
                                        callback5 = (IInputContextCallback) args.arg6;
                                        callbackSeq5 = args.argi6;
                                        ic5 = getInputConnection();
                                    } finally {
                                    }
                                } catch (RemoteException e5) {
                                    Log.w(TAG, "Got RemoteException calling setExtractedText", e5);
                                }
                                if (ic5 != null && isActive()) {
                                    callback5.setExtractedText(ic5.getExtractedText((ExtractedTextRequest) args.arg1, msg.arg1), callbackSeq5);
                                    return;
                                }
                                Log.w(TAG, "getExtractedText on inactive InputConnection");
                                callback5.setExtractedText(null, callbackSeq5);
                                return;
                            case 50:
                                InputConnection ic16 = getInputConnection();
                                if (ic16 == null || !isActive()) {
                                    Log.w(TAG, "commitText on inactive InputConnection");
                                    return;
                                }
                                ic16.commitText((CharSequence) msg.obj, msg.arg1);
                                onUserAction();
                                return;
                            case 63:
                                InputConnection ic17 = getInputConnection();
                                if (ic17 == null || !isActive()) {
                                    Log.w(TAG, "setComposingRegion on inactive InputConnection");
                                    return;
                                } else {
                                    ic17.setComposingRegion(msg.arg1, msg.arg2);
                                    return;
                                }
                            case 65:
                                if (isFinished()) {
                                    return;
                                }
                                InputConnection ic18 = getInputConnection();
                                if (ic18 == null) {
                                    Log.w(TAG, "finishComposingText on inactive InputConnection");
                                    return;
                                } else {
                                    ic18.finishComposingText();
                                    return;
                                }
                            case 70:
                                InputConnection ic19 = getInputConnection();
                                if (ic19 == null || !isActive()) {
                                    Log.w(TAG, "sendKeyEvent on inactive InputConnection");
                                    return;
                                }
                                ic19.sendKeyEvent((KeyEvent) msg.obj);
                                onUserAction();
                                return;
                            case 90:
                                InputConnection ic20 = getInputConnection();
                                if (ic20 == null || !isActive()) {
                                    Log.w(TAG, "beginBatchEdit on inactive InputConnection");
                                    return;
                                } else {
                                    ic20.beginBatchEdit();
                                    return;
                                }
                            case 95:
                                InputConnection ic21 = getInputConnection();
                                if (ic21 == null || !isActive()) {
                                    Log.w(TAG, "endBatchEdit on inactive InputConnection");
                                    return;
                                } else {
                                    ic21.endBatchEdit();
                                    return;
                                }
                            case 120:
                                args = (SomeArgs) msg.obj;
                                try {
                                    String action = (String) args.arg1;
                                    Bundle data = (Bundle) args.arg2;
                                    InputConnection ic22 = getInputConnection();
                                    if (ic22 != null && isActive()) {
                                        ic22.performPrivateCommand(action, data);
                                        return;
                                    }
                                    Log.w(TAG, "performPrivateCommand on inactive InputConnection");
                                    return;
                                } finally {
                                }
                            case 130:
                                InputConnection ic23 = getInputConnection();
                                if (ic23 == null || !isActive()) {
                                    Log.w(TAG, "clearMetaKeyStates on inactive InputConnection");
                                    return;
                                } else {
                                    ic23.clearMetaKeyStates(msg.arg1);
                                    return;
                                }
                            case 140:
                                args = (SomeArgs) msg.obj;
                                try {
                                    try {
                                        callback6 = (IInputContextCallback) args.arg6;
                                        callbackSeq6 = args.argi6;
                                        ic6 = getInputConnection();
                                    } finally {
                                    }
                                } catch (RemoteException e6) {
                                    Log.w(TAG, "Got RemoteException calling requestCursorAnchorInfo", e6);
                                }
                                if (ic6 != null && isActive()) {
                                    callback6.setRequestUpdateCursorAnchorInfoResult(ic6.requestCursorUpdates(msg.arg1), callbackSeq6);
                                    return;
                                }
                                Log.w(TAG, "requestCursorAnchorInfo on inactive InputConnection");
                                callback6.setRequestUpdateCursorAnchorInfoResult(false, callbackSeq6);
                                return;
                            case 150:
                                if (isFinished()) {
                                    return;
                                }
                                try {
                                    InputConnection ic24 = getInputConnection();
                                    if (ic24 == null) {
                                        synchronized (this.mLock) {
                                            this.mInputConnection = null;
                                            this.mFinished = true;
                                        }
                                        return;
                                    }
                                    int missingMethods = InputConnectionInspector.getMissingMethodFlags(ic24);
                                    if ((missingMethods & 64) == 0) {
                                        ic24.closeConnection();
                                    }
                                    synchronized (this.mLock) {
                                        this.mInputConnection = null;
                                        this.mFinished = true;
                                    }
                                    return;
                                } catch (Throwable th) {
                                    synchronized (this.mLock) {
                                        this.mInputConnection = null;
                                        this.mFinished = true;
                                        throw th;
                                    }
                                }
                            case 160:
                                int flags = msg.arg1;
                                args = (SomeArgs) msg.obj;
                                try {
                                    try {
                                        callback7 = (IInputContextCallback) args.arg6;
                                        callbackSeq7 = args.argi6;
                                        ic7 = getInputConnection();
                                    } finally {
                                    }
                                } catch (RemoteException e7) {
                                    Log.w(TAG, "Got RemoteException calling commitContent", e7);
                                }
                                if (ic7 != null && isActive()) {
                                    InputContentInfo inputContentInfo = (InputContentInfo) args.arg1;
                                    if (inputContentInfo != null && inputContentInfo.validate()) {
                                        boolean result = ic7.commitContent(inputContentInfo, flags, (Bundle) args.arg2);
                                        callback7.setCommitContentResult(result, callbackSeq7);
                                        return;
                                    }
                                    Log.w(TAG, "commitContent with invalid inputContentInfo=" + inputContentInfo);
                                    callback7.setCommitContentResult(false, callbackSeq7);
                                    return;
                                }
                                Log.w(TAG, "commitContent on inactive InputConnection");
                                callback7.setCommitContentResult(false, callbackSeq7);
                                return;
                            default:
                                Log.w(TAG, "Unhandled message code: " + msg.what);
                                return;
                        }
                }
        }
    }

    synchronized Message obtainMessage(int what) {
        return this.mH.obtainMessage(what);
    }

    synchronized Message obtainMessageII(int what, int arg1, int arg2) {
        return this.mH.obtainMessage(what, arg1, arg2);
    }

    synchronized Message obtainMessageO(int what, Object arg1) {
        return this.mH.obtainMessage(what, 0, 0, arg1);
    }

    synchronized Message obtainMessageISC(int what, int arg1, int callbackSeq, IInputContextCallback callback) {
        SomeArgs args = SomeArgs.obtain();
        args.arg6 = callback;
        args.argi6 = callbackSeq;
        return this.mH.obtainMessage(what, arg1, 0, args);
    }

    synchronized Message obtainMessageIISC(int what, int arg1, int arg2, int callbackSeq, IInputContextCallback callback) {
        SomeArgs args = SomeArgs.obtain();
        args.arg6 = callback;
        args.argi6 = callbackSeq;
        return this.mH.obtainMessage(what, arg1, arg2, args);
    }

    synchronized Message obtainMessageIOOSC(int what, int arg1, Object objArg1, Object objArg2, int callbackSeq, IInputContextCallback callback) {
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = objArg1;
        args.arg2 = objArg2;
        args.arg6 = callback;
        args.argi6 = callbackSeq;
        return this.mH.obtainMessage(what, arg1, 0, args);
    }

    synchronized Message obtainMessageIOSC(int what, int arg1, Object arg2, int callbackSeq, IInputContextCallback callback) {
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = arg2;
        args.arg6 = callback;
        args.argi6 = callbackSeq;
        return this.mH.obtainMessage(what, arg1, 0, args);
    }

    synchronized Message obtainMessageIO(int what, int arg1, Object arg2) {
        return this.mH.obtainMessage(what, arg1, 0, arg2);
    }

    synchronized Message obtainMessageOO(int what, Object arg1, Object arg2) {
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = arg1;
        args.arg2 = arg2;
        return this.mH.obtainMessage(what, 0, 0, args);
    }
}
