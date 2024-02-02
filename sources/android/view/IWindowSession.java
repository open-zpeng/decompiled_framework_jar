package android.view;

import android.content.ClipData;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.MergedConfiguration;
import android.view.DisplayCutout;
import android.view.IWindow;
import android.view.IWindowId;
import android.view.WindowManager;
/* loaded from: classes2.dex */
public interface IWindowSession extends IInterface {
    synchronized int add(IWindow iWindow, int i, WindowManager.LayoutParams layoutParams, int i2, Rect rect, Rect rect2, InputChannel inputChannel) throws RemoteException;

    synchronized int addToDisplay(IWindow iWindow, int i, WindowManager.LayoutParams layoutParams, int i2, int i3, Rect rect, Rect rect2, Rect rect3, Rect rect4, DisplayCutout.ParcelableWrapper parcelableWrapper, InputChannel inputChannel) throws RemoteException;

    synchronized int addToDisplayWithoutInputChannel(IWindow iWindow, int i, WindowManager.LayoutParams layoutParams, int i2, int i3, Rect rect, Rect rect2) throws RemoteException;

    synchronized int addWithoutInputChannel(IWindow iWindow, int i, WindowManager.LayoutParams layoutParams, int i2, Rect rect, Rect rect2) throws RemoteException;

    synchronized void cancelDragAndDrop(IBinder iBinder) throws RemoteException;

    synchronized void dragRecipientEntered(IWindow iWindow) throws RemoteException;

    synchronized void dragRecipientExited(IWindow iWindow) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void finishDrawing(IWindow iWindow) throws RemoteException;

    synchronized void getDisplayFrame(IWindow iWindow, Rect rect) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean getInTouchMode() throws RemoteException;

    synchronized IWindowId getWindowId(IBinder iBinder) throws RemoteException;

    synchronized void onRectangleOnScreenRequested(IBinder iBinder, Rect rect) throws RemoteException;

    synchronized boolean outOfMemory(IWindow iWindow) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    IBinder performDrag(IWindow iWindow, int i, SurfaceControl surfaceControl, int i2, float f, float f2, float f3, float f4, ClipData clipData) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    boolean performHapticFeedback(IWindow iWindow, int i, boolean z) throws RemoteException;

    synchronized void pokeDrawLock(IBinder iBinder) throws RemoteException;

    synchronized void prepareToReplaceWindows(IBinder iBinder, boolean z) throws RemoteException;

    synchronized int relayout(IWindow iWindow, int i, WindowManager.LayoutParams layoutParams, int i2, int i3, int i4, int i5, long j, Rect rect, Rect rect2, Rect rect3, Rect rect4, Rect rect5, Rect rect6, Rect rect7, DisplayCutout.ParcelableWrapper parcelableWrapper, MergedConfiguration mergedConfiguration, Surface surface) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void remove(IWindow iWindow) throws RemoteException;

    synchronized void reportDropResult(IWindow iWindow, boolean z) throws RemoteException;

    synchronized Bundle sendWallpaperCommand(IBinder iBinder, String str, int i, int i2, int i3, Bundle bundle, boolean z) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void setInTouchMode(boolean z) throws RemoteException;

    synchronized void setInsets(IWindow iWindow, int i, Rect rect, Rect rect2, Region region) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void setTransparentRegion(IWindow iWindow, Region region) throws RemoteException;

    synchronized void setWallpaperDisplayOffset(IBinder iBinder, int i, int i2) throws RemoteException;

    synchronized void setWallpaperPosition(IBinder iBinder, float f, float f2, float f3, float f4) throws RemoteException;

    synchronized boolean startMovingTask(IWindow iWindow, float f, float f2) throws RemoteException;

    synchronized void updatePointerIcon(IWindow iWindow) throws RemoteException;

    synchronized void updateTapExcludeRegion(IWindow iWindow, int i, int i2, int i3, int i4, int i5) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void wallpaperCommandComplete(IBinder iBinder, Bundle bundle) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void wallpaperOffsetsComplete(IBinder iBinder) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IWindowSession {
        private static final String DESCRIPTOR = "android.view.IWindowSession";
        static final int TRANSACTION_add = 1;
        static final int TRANSACTION_addToDisplay = 2;
        static final int TRANSACTION_addToDisplayWithoutInputChannel = 4;
        static final int TRANSACTION_addWithoutInputChannel = 3;
        static final int TRANSACTION_cancelDragAndDrop = 18;
        static final int TRANSACTION_dragRecipientEntered = 19;
        static final int TRANSACTION_dragRecipientExited = 20;
        static final int TRANSACTION_finishDrawing = 12;
        static final int TRANSACTION_getDisplayFrame = 11;
        static final int TRANSACTION_getInTouchMode = 14;
        static final int TRANSACTION_getWindowId = 27;
        static final int TRANSACTION_onRectangleOnScreenRequested = 26;
        static final int TRANSACTION_outOfMemory = 8;
        static final int TRANSACTION_performDrag = 16;
        static final int TRANSACTION_performHapticFeedback = 15;
        static final int TRANSACTION_pokeDrawLock = 28;
        static final int TRANSACTION_prepareToReplaceWindows = 7;
        static final int TRANSACTION_relayout = 6;
        static final int TRANSACTION_remove = 5;
        static final int TRANSACTION_reportDropResult = 17;
        static final int TRANSACTION_sendWallpaperCommand = 24;
        static final int TRANSACTION_setInTouchMode = 13;
        static final int TRANSACTION_setInsets = 10;
        static final int TRANSACTION_setTransparentRegion = 9;
        static final int TRANSACTION_setWallpaperDisplayOffset = 23;
        static final int TRANSACTION_setWallpaperPosition = 21;
        static final int TRANSACTION_startMovingTask = 29;
        static final int TRANSACTION_updatePointerIcon = 30;
        static final int TRANSACTION_updateTapExcludeRegion = 31;
        static final int TRANSACTION_wallpaperCommandComplete = 25;
        static final int TRANSACTION_wallpaperOffsetsComplete = 22;

        private protected Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static IWindowSession asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IWindowSession)) {
                return (IWindowSession) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Rect _arg2;
            Rect _arg3;
            SurfaceControl _arg22;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    IWindow _arg0 = IWindow.Stub.asInterface(data.readStrongBinder());
                    int _arg1 = data.readInt();
                    WindowManager.LayoutParams _arg23 = data.readInt() != 0 ? WindowManager.LayoutParams.CREATOR.createFromParcel(data) : null;
                    int _arg32 = data.readInt();
                    Rect _arg4 = new Rect();
                    Rect _arg5 = new Rect();
                    InputChannel _arg6 = new InputChannel();
                    int _result = add(_arg0, _arg1, _arg23, _arg32, _arg4, _arg5, _arg6);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    reply.writeInt(1);
                    _arg4.writeToParcel(reply, 1);
                    reply.writeInt(1);
                    _arg5.writeToParcel(reply, 1);
                    reply.writeInt(1);
                    _arg6.writeToParcel(reply, 1);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IWindow _arg02 = IWindow.Stub.asInterface(data.readStrongBinder());
                    int _arg12 = data.readInt();
                    WindowManager.LayoutParams _arg24 = data.readInt() != 0 ? WindowManager.LayoutParams.CREATOR.createFromParcel(data) : null;
                    int _arg33 = data.readInt();
                    int _arg42 = data.readInt();
                    Rect _arg52 = new Rect();
                    Rect _arg62 = new Rect();
                    Rect _arg7 = new Rect();
                    Rect _arg8 = new Rect();
                    DisplayCutout.ParcelableWrapper _arg9 = new DisplayCutout.ParcelableWrapper();
                    InputChannel _arg10 = new InputChannel();
                    int _result2 = addToDisplay(_arg02, _arg12, _arg24, _arg33, _arg42, _arg52, _arg62, _arg7, _arg8, _arg9, _arg10);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    reply.writeInt(1);
                    _arg52.writeToParcel(reply, 1);
                    reply.writeInt(1);
                    _arg62.writeToParcel(reply, 1);
                    reply.writeInt(1);
                    _arg7.writeToParcel(reply, 1);
                    reply.writeInt(1);
                    _arg8.writeToParcel(reply, 1);
                    reply.writeInt(1);
                    _arg9.writeToParcel(reply, 1);
                    reply.writeInt(1);
                    _arg10.writeToParcel(reply, 1);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IWindow _arg03 = IWindow.Stub.asInterface(data.readStrongBinder());
                    int _arg13 = data.readInt();
                    WindowManager.LayoutParams _arg25 = data.readInt() != 0 ? WindowManager.LayoutParams.CREATOR.createFromParcel(data) : null;
                    int _arg34 = data.readInt();
                    Rect _arg43 = new Rect();
                    Rect _arg53 = new Rect();
                    int _result3 = addWithoutInputChannel(_arg03, _arg13, _arg25, _arg34, _arg43, _arg53);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    reply.writeInt(1);
                    _arg43.writeToParcel(reply, 1);
                    reply.writeInt(1);
                    _arg53.writeToParcel(reply, 1);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    IWindow _arg04 = IWindow.Stub.asInterface(data.readStrongBinder());
                    int _arg14 = data.readInt();
                    WindowManager.LayoutParams _arg26 = data.readInt() != 0 ? WindowManager.LayoutParams.CREATOR.createFromParcel(data) : null;
                    int _arg35 = data.readInt();
                    int _arg44 = data.readInt();
                    Rect _arg54 = new Rect();
                    Rect _arg63 = new Rect();
                    int _result4 = addToDisplayWithoutInputChannel(_arg04, _arg14, _arg26, _arg35, _arg44, _arg54, _arg63);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    reply.writeInt(1);
                    _arg54.writeToParcel(reply, 1);
                    reply.writeInt(1);
                    _arg63.writeToParcel(reply, 1);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    IWindow _arg05 = IWindow.Stub.asInterface(data.readStrongBinder());
                    remove(_arg05);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    IWindow _arg06 = IWindow.Stub.asInterface(data.readStrongBinder());
                    int _arg15 = data.readInt();
                    WindowManager.LayoutParams _arg27 = data.readInt() != 0 ? WindowManager.LayoutParams.CREATOR.createFromParcel(data) : null;
                    int _arg36 = data.readInt();
                    int _arg45 = data.readInt();
                    int _arg55 = data.readInt();
                    int _arg64 = data.readInt();
                    long _arg72 = data.readLong();
                    Rect _arg82 = new Rect();
                    Rect _arg92 = new Rect();
                    Rect _arg102 = new Rect();
                    Rect _arg11 = new Rect();
                    Rect _arg122 = new Rect();
                    Rect _arg132 = new Rect();
                    Rect _arg142 = new Rect();
                    DisplayCutout.ParcelableWrapper _arg152 = new DisplayCutout.ParcelableWrapper();
                    MergedConfiguration _arg16 = new MergedConfiguration();
                    Surface _arg17 = new Surface();
                    int _result5 = relayout(_arg06, _arg15, _arg27, _arg36, _arg45, _arg55, _arg64, _arg72, _arg82, _arg92, _arg102, _arg11, _arg122, _arg132, _arg142, _arg152, _arg16, _arg17);
                    reply.writeNoException();
                    reply.writeInt(_result5);
                    reply.writeInt(1);
                    _arg82.writeToParcel(reply, 1);
                    reply.writeInt(1);
                    _arg92.writeToParcel(reply, 1);
                    reply.writeInt(1);
                    _arg102.writeToParcel(reply, 1);
                    reply.writeInt(1);
                    _arg11.writeToParcel(reply, 1);
                    reply.writeInt(1);
                    _arg122.writeToParcel(reply, 1);
                    reply.writeInt(1);
                    _arg132.writeToParcel(reply, 1);
                    reply.writeInt(1);
                    _arg142.writeToParcel(reply, 1);
                    reply.writeInt(1);
                    _arg152.writeToParcel(reply, 1);
                    reply.writeInt(1);
                    _arg16.writeToParcel(reply, 1);
                    reply.writeInt(1);
                    _arg17.writeToParcel(reply, 1);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg07 = data.readStrongBinder();
                    boolean _arg18 = data.readInt() != 0;
                    prepareToReplaceWindows(_arg07, _arg18);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    IWindow _arg08 = IWindow.Stub.asInterface(data.readStrongBinder());
                    boolean outOfMemory = outOfMemory(_arg08);
                    reply.writeNoException();
                    reply.writeInt(outOfMemory ? 1 : 0);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    IWindow _arg09 = IWindow.Stub.asInterface(data.readStrongBinder());
                    Region _arg19 = data.readInt() != 0 ? Region.CREATOR.createFromParcel(data) : null;
                    setTransparentRegion(_arg09, _arg19);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    IWindow _arg010 = IWindow.Stub.asInterface(data.readStrongBinder());
                    int _arg110 = data.readInt();
                    if (data.readInt() != 0) {
                        Rect _arg28 = Rect.CREATOR.createFromParcel(data);
                        _arg2 = _arg28;
                    } else {
                        _arg2 = null;
                    }
                    if (data.readInt() != 0) {
                        Rect _arg37 = Rect.CREATOR.createFromParcel(data);
                        _arg3 = _arg37;
                    } else {
                        _arg3 = null;
                    }
                    setInsets(_arg010, _arg110, _arg2, _arg3, data.readInt() != 0 ? Region.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    IWindow _arg011 = IWindow.Stub.asInterface(data.readStrongBinder());
                    Rect _arg111 = new Rect();
                    getDisplayFrame(_arg011, _arg111);
                    reply.writeNoException();
                    reply.writeInt(1);
                    _arg111.writeToParcel(reply, 1);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    IWindow _arg012 = IWindow.Stub.asInterface(data.readStrongBinder());
                    finishDrawing(_arg012);
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg013 = data.readInt() != 0;
                    setInTouchMode(_arg013);
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    boolean inTouchMode = getInTouchMode();
                    reply.writeNoException();
                    reply.writeInt(inTouchMode ? 1 : 0);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    IWindow _arg014 = IWindow.Stub.asInterface(data.readStrongBinder());
                    int _arg112 = data.readInt();
                    boolean _arg29 = data.readInt() != 0;
                    boolean performHapticFeedback = performHapticFeedback(_arg014, _arg112, _arg29);
                    reply.writeNoException();
                    reply.writeInt(performHapticFeedback ? 1 : 0);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    IWindow _arg015 = IWindow.Stub.asInterface(data.readStrongBinder());
                    int _arg113 = data.readInt();
                    if (data.readInt() != 0) {
                        SurfaceControl _arg210 = SurfaceControl.CREATOR.createFromParcel(data);
                        _arg22 = _arg210;
                    } else {
                        _arg22 = null;
                    }
                    int _arg38 = data.readInt();
                    IBinder _result6 = performDrag(_arg015, _arg113, _arg22, _arg38, data.readFloat(), data.readFloat(), data.readFloat(), data.readFloat(), data.readInt() != 0 ? ClipData.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result6);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    IWindow _arg016 = IWindow.Stub.asInterface(data.readStrongBinder());
                    boolean _arg114 = data.readInt() != 0;
                    reportDropResult(_arg016, _arg114);
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg017 = data.readStrongBinder();
                    cancelDragAndDrop(_arg017);
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    IWindow _arg018 = IWindow.Stub.asInterface(data.readStrongBinder());
                    dragRecipientEntered(_arg018);
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    IWindow _arg019 = IWindow.Stub.asInterface(data.readStrongBinder());
                    dragRecipientExited(_arg019);
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg020 = data.readStrongBinder();
                    float _arg115 = data.readFloat();
                    float _arg211 = data.readFloat();
                    float _arg39 = data.readFloat();
                    setWallpaperPosition(_arg020, _arg115, _arg211, _arg39, data.readFloat());
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg021 = data.readStrongBinder();
                    wallpaperOffsetsComplete(_arg021);
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg022 = data.readStrongBinder();
                    int _arg116 = data.readInt();
                    int _arg212 = data.readInt();
                    setWallpaperDisplayOffset(_arg022, _arg116, _arg212);
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg023 = data.readStrongBinder();
                    String _arg117 = data.readString();
                    int _arg213 = data.readInt();
                    int _arg310 = data.readInt();
                    Bundle _result7 = sendWallpaperCommand(_arg023, _arg117, _arg213, _arg310, data.readInt(), data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null, data.readInt() != 0);
                    reply.writeNoException();
                    if (_result7 != null) {
                        reply.writeInt(1);
                        _result7.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg024 = data.readStrongBinder();
                    Bundle _arg118 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    wallpaperCommandComplete(_arg024, _arg118);
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg025 = data.readStrongBinder();
                    Rect _arg119 = data.readInt() != 0 ? Rect.CREATOR.createFromParcel(data) : null;
                    onRectangleOnScreenRequested(_arg025, _arg119);
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg026 = data.readStrongBinder();
                    IWindowId _result8 = getWindowId(_arg026);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result8 != null ? _result8.asBinder() : null);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg027 = data.readStrongBinder();
                    pokeDrawLock(_arg027);
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    IWindow _arg028 = IWindow.Stub.asInterface(data.readStrongBinder());
                    float _arg120 = data.readFloat();
                    float _arg214 = data.readFloat();
                    boolean startMovingTask = startMovingTask(_arg028, _arg120, _arg214);
                    reply.writeNoException();
                    reply.writeInt(startMovingTask ? 1 : 0);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    IWindow _arg029 = IWindow.Stub.asInterface(data.readStrongBinder());
                    updatePointerIcon(_arg029);
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    IWindow _arg030 = IWindow.Stub.asInterface(data.readStrongBinder());
                    int _arg121 = data.readInt();
                    int _arg215 = data.readInt();
                    int _arg311 = data.readInt();
                    updateTapExcludeRegion(_arg030, _arg121, _arg215, _arg311, data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IWindowSession {
            private IBinder mRemote;

            synchronized Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.view.IWindowSession
            public synchronized int add(IWindow window, int seq, WindowManager.LayoutParams attrs, int viewVisibility, Rect outContentInsets, Rect outStableInsets, InputChannel outInputChannel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    _data.writeInt(seq);
                    if (attrs != null) {
                        _data.writeInt(1);
                        attrs.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(viewVisibility);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        outContentInsets.readFromParcel(_reply);
                    }
                    if (_reply.readInt() != 0) {
                        outStableInsets.readFromParcel(_reply);
                    }
                    if (_reply.readInt() != 0) {
                        outInputChannel.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IWindowSession
            public synchronized int addToDisplay(IWindow window, int seq, WindowManager.LayoutParams attrs, int viewVisibility, int layerStackId, Rect outFrame, Rect outContentInsets, Rect outStableInsets, Rect outOutsets, DisplayCutout.ParcelableWrapper displayCutout, InputChannel outInputChannel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    try {
                        _data.writeInt(seq);
                        if (attrs != null) {
                            _data.writeInt(1);
                            attrs.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeInt(viewVisibility);
                            try {
                                _data.writeInt(layerStackId);
                                try {
                                    this.mRemote.transact(2, _data, _reply, 0);
                                    _reply.readException();
                                    int _result = _reply.readInt();
                                    if (_reply.readInt() != 0) {
                                        try {
                                            outFrame.readFromParcel(_reply);
                                        } catch (Throwable th) {
                                            th = th;
                                            _reply.recycle();
                                            _data.recycle();
                                            throw th;
                                        }
                                    }
                                    if (_reply.readInt() != 0) {
                                        try {
                                            outContentInsets.readFromParcel(_reply);
                                        } catch (Throwable th2) {
                                            th = th2;
                                            _reply.recycle();
                                            _data.recycle();
                                            throw th;
                                        }
                                    }
                                    if (_reply.readInt() != 0) {
                                        try {
                                            outStableInsets.readFromParcel(_reply);
                                        } catch (Throwable th3) {
                                            th = th3;
                                            _reply.recycle();
                                            _data.recycle();
                                            throw th;
                                        }
                                    }
                                    if (_reply.readInt() != 0) {
                                        try {
                                            outOutsets.readFromParcel(_reply);
                                        } catch (Throwable th4) {
                                            th = th4;
                                            _reply.recycle();
                                            _data.recycle();
                                            throw th;
                                        }
                                    }
                                    if (_reply.readInt() != 0) {
                                        try {
                                            displayCutout.readFromParcel(_reply);
                                        } catch (Throwable th5) {
                                            th = th5;
                                            _reply.recycle();
                                            _data.recycle();
                                            throw th;
                                        }
                                    }
                                    if (_reply.readInt() != 0) {
                                        try {
                                            outInputChannel.readFromParcel(_reply);
                                        } catch (Throwable th6) {
                                            th = th6;
                                            _reply.recycle();
                                            _data.recycle();
                                            throw th;
                                        }
                                    }
                                    _reply.recycle();
                                    _data.recycle();
                                    return _result;
                                } catch (Throwable th7) {
                                    th = th7;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th8) {
                                th = th8;
                            }
                        } catch (Throwable th9) {
                            th = th9;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th10) {
                        th = th10;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th11) {
                    th = th11;
                }
            }

            @Override // android.view.IWindowSession
            public synchronized int addWithoutInputChannel(IWindow window, int seq, WindowManager.LayoutParams attrs, int viewVisibility, Rect outContentInsets, Rect outStableInsets) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    _data.writeInt(seq);
                    if (attrs != null) {
                        _data.writeInt(1);
                        attrs.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(viewVisibility);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        outContentInsets.readFromParcel(_reply);
                    }
                    if (_reply.readInt() != 0) {
                        outStableInsets.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IWindowSession
            public synchronized int addToDisplayWithoutInputChannel(IWindow window, int seq, WindowManager.LayoutParams attrs, int viewVisibility, int layerStackId, Rect outContentInsets, Rect outStableInsets) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    _data.writeInt(seq);
                    if (attrs != null) {
                        _data.writeInt(1);
                        attrs.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(viewVisibility);
                    _data.writeInt(layerStackId);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    if (_reply.readInt() != 0) {
                        outContentInsets.readFromParcel(_reply);
                    }
                    if (_reply.readInt() != 0) {
                        outStableInsets.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void remove(IWindow window) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected int relayout(IWindow window, int seq, WindowManager.LayoutParams attrs, int requestedWidth, int requestedHeight, int viewVisibility, int flags, long frameNumber, Rect outFrame, Rect outOverscanInsets, Rect outContentInsets, Rect outVisibleInsets, Rect outStableInsets, Rect outOutsets, Rect outBackdropFrame, DisplayCutout.ParcelableWrapper displayCutout, MergedConfiguration outMergedConfiguration, Surface outSurface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(seq);
                    if (attrs != null) {
                        _data.writeInt(1);
                        attrs.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeInt(requestedWidth);
                        try {
                            _data.writeInt(requestedHeight);
                            try {
                                _data.writeInt(viewVisibility);
                            } catch (Throwable th2) {
                                th = th2;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(flags);
                        try {
                            _data.writeLong(frameNumber);
                            try {
                                this.mRemote.transact(6, _data, _reply, 0);
                                _reply.readException();
                                int _result = _reply.readInt();
                                if (_reply.readInt() != 0) {
                                    try {
                                        outFrame.readFromParcel(_reply);
                                    } catch (Throwable th5) {
                                        th = th5;
                                        _reply.recycle();
                                        _data.recycle();
                                        throw th;
                                    }
                                }
                                if (_reply.readInt() != 0) {
                                    try {
                                        outOverscanInsets.readFromParcel(_reply);
                                    } catch (Throwable th6) {
                                        th = th6;
                                        _reply.recycle();
                                        _data.recycle();
                                        throw th;
                                    }
                                }
                                if (_reply.readInt() != 0) {
                                    try {
                                        outContentInsets.readFromParcel(_reply);
                                    } catch (Throwable th7) {
                                        th = th7;
                                        _reply.recycle();
                                        _data.recycle();
                                        throw th;
                                    }
                                }
                                if (_reply.readInt() != 0) {
                                    outVisibleInsets.readFromParcel(_reply);
                                }
                                if (_reply.readInt() != 0) {
                                    outStableInsets.readFromParcel(_reply);
                                }
                                if (_reply.readInt() != 0) {
                                    outOutsets.readFromParcel(_reply);
                                }
                                if (_reply.readInt() != 0) {
                                    outBackdropFrame.readFromParcel(_reply);
                                }
                                if (_reply.readInt() != 0) {
                                    displayCutout.readFromParcel(_reply);
                                }
                                if (_reply.readInt() != 0) {
                                    outMergedConfiguration.readFromParcel(_reply);
                                }
                                if (_reply.readInt() != 0) {
                                    try {
                                        outSurface.readFromParcel(_reply);
                                    } catch (Throwable th8) {
                                        th = th8;
                                        _reply.recycle();
                                        _data.recycle();
                                        throw th;
                                    }
                                }
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            } catch (Throwable th9) {
                                th = th9;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th10) {
                            th = th10;
                        }
                    } catch (Throwable th11) {
                        th = th11;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th12) {
                    th = th12;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.view.IWindowSession
            public synchronized void prepareToReplaceWindows(IBinder appToken, boolean childrenOnly) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(appToken);
                    _data.writeInt(childrenOnly ? 1 : 0);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IWindowSession
            public synchronized boolean outOfMemory(IWindow window) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void setTransparentRegion(IWindow window, Region region) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    if (region != null) {
                        _data.writeInt(1);
                        region.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IWindowSession
            public synchronized void setInsets(IWindow window, int touchableInsets, Rect contentInsets, Rect visibleInsets, Region touchableRegion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    _data.writeInt(touchableInsets);
                    if (contentInsets != null) {
                        _data.writeInt(1);
                        contentInsets.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (visibleInsets != null) {
                        _data.writeInt(1);
                        visibleInsets.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (touchableRegion != null) {
                        _data.writeInt(1);
                        touchableRegion.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IWindowSession
            public synchronized void getDisplayFrame(IWindow window, Rect outDisplayFrame) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        outDisplayFrame.readFromParcel(_reply);
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void finishDrawing(IWindow window) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void setInTouchMode(boolean showFocus) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(showFocus ? 1 : 0);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean getInTouchMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized boolean performHapticFeedback(IWindow window, int effectId, boolean always) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    _data.writeInt(effectId);
                    _data.writeInt(always ? 1 : 0);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized IBinder performDrag(IWindow window, int flags, SurfaceControl surface, int touchSource, float touchX, float touchY, float thumbCenterX, float thumbCenterY, ClipData data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    _data.writeInt(flags);
                    if (surface != null) {
                        _data.writeInt(1);
                        surface.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(touchSource);
                    _data.writeFloat(touchX);
                    _data.writeFloat(touchY);
                    _data.writeFloat(thumbCenterX);
                    _data.writeFloat(thumbCenterY);
                    if (data != null) {
                        _data.writeInt(1);
                        data.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    IBinder _result = _reply.readStrongBinder();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IWindowSession
            public synchronized void reportDropResult(IWindow window, boolean consumed) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    _data.writeInt(consumed ? 1 : 0);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IWindowSession
            public synchronized void cancelDragAndDrop(IBinder dragToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(dragToken);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IWindowSession
            public synchronized void dragRecipientEntered(IWindow window) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IWindowSession
            public synchronized void dragRecipientExited(IWindow window) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IWindowSession
            public synchronized void setWallpaperPosition(IBinder windowToken, float x, float y, float xstep, float ystep) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(windowToken);
                    _data.writeFloat(x);
                    _data.writeFloat(y);
                    _data.writeFloat(xstep);
                    _data.writeFloat(ystep);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void wallpaperOffsetsComplete(IBinder window) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IWindowSession
            public synchronized void setWallpaperDisplayOffset(IBinder windowToken, int x, int y) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(windowToken);
                    _data.writeInt(x);
                    _data.writeInt(y);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IWindowSession
            public synchronized Bundle sendWallpaperCommand(IBinder window, String action, int x, int y, int z, Bundle extras, boolean sync) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window);
                    _data.writeString(action);
                    _data.writeInt(x);
                    _data.writeInt(y);
                    _data.writeInt(z);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sync ? 1 : 0);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void wallpaperCommandComplete(IBinder window, Bundle result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IWindowSession
            public synchronized void onRectangleOnScreenRequested(IBinder token, Rect rectangle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (rectangle != null) {
                        _data.writeInt(1);
                        rectangle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IWindowSession
            public synchronized IWindowId getWindowId(IBinder window) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    IWindowId _result = IWindowId.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IWindowSession
            public synchronized void pokeDrawLock(IBinder window) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IWindowSession
            public synchronized boolean startMovingTask(IWindow window, float startX, float startY) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    _data.writeFloat(startX);
                    _data.writeFloat(startY);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IWindowSession
            public synchronized void updatePointerIcon(IWindow window) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.IWindowSession
            public synchronized void updateTapExcludeRegion(IWindow window, int regionId, int left, int top, int width, int height) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(window != null ? window.asBinder() : null);
                    _data.writeInt(regionId);
                    _data.writeInt(left);
                    _data.writeInt(top);
                    _data.writeInt(width);
                    _data.writeInt(height);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
