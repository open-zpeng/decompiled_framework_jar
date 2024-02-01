package android.view;

import android.graphics.Point;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.MergedConfiguration;
import android.view.DisplayCutout;
import com.android.internal.os.IResultReceiver;

/* loaded from: classes3.dex */
public interface IWindow extends IInterface {
    void closeSystemDialogs(String str) throws RemoteException;

    void dispatchAppVisibility(boolean z) throws RemoteException;

    void dispatchDragEvent(DragEvent dragEvent) throws RemoteException;

    void dispatchGetNewSurface() throws RemoteException;

    void dispatchPointerCaptureChanged(boolean z) throws RemoteException;

    void dispatchSystemUiVisibilityChanged(int i, int i2, int i3, int i4) throws RemoteException;

    void dispatchWallpaperCommand(String str, int i, int i2, int i3, Bundle bundle, boolean z) throws RemoteException;

    void dispatchWallpaperOffsets(float f, float f2, float f3, float f4, boolean z) throws RemoteException;

    void dispatchWindowShown() throws RemoteException;

    void executeCommand(String str, String str2, ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    void insetsChanged(InsetsState insetsState) throws RemoteException;

    void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) throws RemoteException;

    void locationInParentDisplayChanged(Point point) throws RemoteException;

    void moved(int i, int i2) throws RemoteException;

    void requestAppKeyboardShortcuts(IResultReceiver iResultReceiver, int i) throws RemoteException;

    void resized(Rect rect, Rect rect2, Rect rect3, Rect rect4, Rect rect5, Rect rect6, boolean z, MergedConfiguration mergedConfiguration, Rect rect7, boolean z2, boolean z3, int i, DisplayCutout.ParcelableWrapper parcelableWrapper) throws RemoteException;

    void updatePointerIcon(float f, float f2) throws RemoteException;

    void windowFocusChanged(boolean z, boolean z2) throws RemoteException;

    /* loaded from: classes3.dex */
    public static class Default implements IWindow {
        @Override // android.view.IWindow
        public void executeCommand(String command, String parameters, ParcelFileDescriptor descriptor) throws RemoteException {
        }

        @Override // android.view.IWindow
        public void resized(Rect frame, Rect overscanInsets, Rect contentInsets, Rect visibleInsets, Rect stableInsets, Rect outsets, boolean reportDraw, MergedConfiguration newMergedConfiguration, Rect backDropFrame, boolean forceLayout, boolean alwaysConsumeSystemBars, int displayId, DisplayCutout.ParcelableWrapper displayCutout) throws RemoteException {
        }

        @Override // android.view.IWindow
        public void locationInParentDisplayChanged(Point offset) throws RemoteException {
        }

        @Override // android.view.IWindow
        public void insetsChanged(InsetsState insetsState) throws RemoteException {
        }

        @Override // android.view.IWindow
        public void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] activeControls) throws RemoteException {
        }

        @Override // android.view.IWindow
        public void moved(int newX, int newY) throws RemoteException {
        }

        @Override // android.view.IWindow
        public void dispatchAppVisibility(boolean visible) throws RemoteException {
        }

        @Override // android.view.IWindow
        public void dispatchGetNewSurface() throws RemoteException {
        }

        @Override // android.view.IWindow
        public void windowFocusChanged(boolean hasFocus, boolean inTouchMode) throws RemoteException {
        }

        @Override // android.view.IWindow
        public void closeSystemDialogs(String reason) throws RemoteException {
        }

        @Override // android.view.IWindow
        public void dispatchWallpaperOffsets(float x, float y, float xStep, float yStep, boolean sync) throws RemoteException {
        }

        @Override // android.view.IWindow
        public void dispatchWallpaperCommand(String action, int x, int y, int z, Bundle extras, boolean sync) throws RemoteException {
        }

        @Override // android.view.IWindow
        public void dispatchDragEvent(DragEvent event) throws RemoteException {
        }

        @Override // android.view.IWindow
        public void updatePointerIcon(float x, float y) throws RemoteException {
        }

        @Override // android.view.IWindow
        public void dispatchSystemUiVisibilityChanged(int seq, int globalVisibility, int localValue, int localChanges) throws RemoteException {
        }

        @Override // android.view.IWindow
        public void dispatchWindowShown() throws RemoteException {
        }

        @Override // android.view.IWindow
        public void requestAppKeyboardShortcuts(IResultReceiver receiver, int deviceId) throws RemoteException {
        }

        @Override // android.view.IWindow
        public void dispatchPointerCaptureChanged(boolean hasCapture) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IWindow {
        private static final String DESCRIPTOR = "android.view.IWindow";
        static final int TRANSACTION_closeSystemDialogs = 10;
        static final int TRANSACTION_dispatchAppVisibility = 7;
        static final int TRANSACTION_dispatchDragEvent = 13;
        static final int TRANSACTION_dispatchGetNewSurface = 8;
        static final int TRANSACTION_dispatchPointerCaptureChanged = 18;
        static final int TRANSACTION_dispatchSystemUiVisibilityChanged = 15;
        static final int TRANSACTION_dispatchWallpaperCommand = 12;
        static final int TRANSACTION_dispatchWallpaperOffsets = 11;
        static final int TRANSACTION_dispatchWindowShown = 16;
        static final int TRANSACTION_executeCommand = 1;
        static final int TRANSACTION_insetsChanged = 4;
        static final int TRANSACTION_insetsControlChanged = 5;
        static final int TRANSACTION_locationInParentDisplayChanged = 3;
        static final int TRANSACTION_moved = 6;
        static final int TRANSACTION_requestAppKeyboardShortcuts = 17;
        static final int TRANSACTION_resized = 2;
        static final int TRANSACTION_updatePointerIcon = 14;
        static final int TRANSACTION_windowFocusChanged = 9;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWindow asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IWindow)) {
                return (IWindow) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "executeCommand";
                case 2:
                    return "resized";
                case 3:
                    return "locationInParentDisplayChanged";
                case 4:
                    return "insetsChanged";
                case 5:
                    return "insetsControlChanged";
                case 6:
                    return "moved";
                case 7:
                    return "dispatchAppVisibility";
                case 8:
                    return "dispatchGetNewSurface";
                case 9:
                    return "windowFocusChanged";
                case 10:
                    return "closeSystemDialogs";
                case 11:
                    return "dispatchWallpaperOffsets";
                case 12:
                    return "dispatchWallpaperCommand";
                case 13:
                    return "dispatchDragEvent";
                case 14:
                    return "updatePointerIcon";
                case 15:
                    return "dispatchSystemUiVisibilityChanged";
                case 16:
                    return "dispatchWindowShown";
                case 17:
                    return "requestAppKeyboardShortcuts";
                case 18:
                    return "dispatchPointerCaptureChanged";
                default:
                    return null;
            }
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ParcelFileDescriptor _arg2;
            Rect _arg0;
            Rect _arg1;
            Rect _arg22;
            Rect _arg3;
            Rect _arg4;
            Rect _arg5;
            MergedConfiguration _arg7;
            Rect _arg8;
            DisplayCutout.ParcelableWrapper _arg12;
            Point _arg02;
            InsetsState _arg03;
            InsetsState _arg04;
            boolean _arg05;
            Bundle _arg42;
            DragEvent _arg06;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    String _arg13 = data.readString();
                    if (data.readInt() != 0) {
                        _arg2 = ParcelFileDescriptor.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    executeCommand(_arg07, _arg13, _arg2);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg1 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg22 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg22 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg3 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg4 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg4 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg5 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg5 = null;
                    }
                    boolean _arg6 = data.readInt() != 0;
                    if (data.readInt() != 0) {
                        _arg7 = MergedConfiguration.CREATOR.createFromParcel(data);
                    } else {
                        _arg7 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg8 = Rect.CREATOR.createFromParcel(data);
                    } else {
                        _arg8 = null;
                    }
                    boolean _arg9 = data.readInt() != 0;
                    boolean _arg10 = data.readInt() != 0;
                    int _arg11 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg12 = DisplayCutout.ParcelableWrapper.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    resized(_arg0, _arg1, _arg22, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8, _arg9, _arg10, _arg11, _arg12);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = Point.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    locationInParentDisplayChanged(_arg02);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = InsetsState.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    insetsChanged(_arg03);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = InsetsState.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    InsetsSourceControl[] _arg14 = (InsetsSourceControl[]) data.createTypedArray(InsetsSourceControl.CREATOR);
                    insetsControlChanged(_arg04, _arg14);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    int _arg15 = data.readInt();
                    moved(_arg08, _arg15);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _arg05 = data.readInt() != 0;
                    dispatchAppVisibility(_arg05);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    dispatchGetNewSurface();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg09 = data.readInt() != 0;
                    _arg05 = data.readInt() != 0;
                    windowFocusChanged(_arg09, _arg05);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    closeSystemDialogs(data.readString());
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    float _arg010 = data.readFloat();
                    float _arg16 = data.readFloat();
                    float _arg23 = data.readFloat();
                    float _arg32 = data.readFloat();
                    boolean _arg43 = data.readInt() != 0;
                    dispatchWallpaperOffsets(_arg010, _arg16, _arg23, _arg32, _arg43);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg011 = data.readString();
                    int _arg17 = data.readInt();
                    int _arg24 = data.readInt();
                    int _arg33 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg42 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg42 = null;
                    }
                    boolean _arg52 = data.readInt() != 0;
                    dispatchWallpaperCommand(_arg011, _arg17, _arg24, _arg33, _arg42, _arg52);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg06 = DragEvent.CREATOR.createFromParcel(data);
                    } else {
                        _arg06 = null;
                    }
                    dispatchDragEvent(_arg06);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    float _arg012 = data.readFloat();
                    float _arg18 = data.readFloat();
                    updatePointerIcon(_arg012, _arg18);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg013 = data.readInt();
                    int _arg19 = data.readInt();
                    int _arg25 = data.readInt();
                    int _arg34 = data.readInt();
                    dispatchSystemUiVisibilityChanged(_arg013, _arg19, _arg25, _arg34);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    dispatchWindowShown();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    IResultReceiver _arg014 = IResultReceiver.Stub.asInterface(data.readStrongBinder());
                    int _arg110 = data.readInt();
                    requestAppKeyboardShortcuts(_arg014, _arg110);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    _arg05 = data.readInt() != 0;
                    dispatchPointerCaptureChanged(_arg05);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements IWindow {
            public static IWindow sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.view.IWindow
            public void executeCommand(String command, String parameters, ParcelFileDescriptor descriptor) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(command);
                    _data.writeString(parameters);
                    if (descriptor != null) {
                        _data.writeInt(1);
                        descriptor.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().executeCommand(command, parameters, descriptor);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IWindow
            public void resized(Rect frame, Rect overscanInsets, Rect contentInsets, Rect visibleInsets, Rect stableInsets, Rect outsets, boolean reportDraw, MergedConfiguration newMergedConfiguration, Rect backDropFrame, boolean forceLayout, boolean alwaysConsumeSystemBars, int displayId, DisplayCutout.ParcelableWrapper displayCutout) throws RemoteException {
                Parcel _data;
                Parcel _data2 = Parcel.obtain();
                try {
                    _data2.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (frame != null) {
                        try {
                            _data2.writeInt(1);
                            frame.writeToParcel(_data2, 0);
                        } catch (Throwable th) {
                            th = th;
                            _data = _data2;
                            _data.recycle();
                            throw th;
                        }
                    } else {
                        _data2.writeInt(0);
                    }
                    if (overscanInsets != null) {
                        _data2.writeInt(1);
                        overscanInsets.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(0);
                    }
                    if (contentInsets != null) {
                        _data2.writeInt(1);
                        contentInsets.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(0);
                    }
                    if (visibleInsets != null) {
                        _data2.writeInt(1);
                        visibleInsets.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(0);
                    }
                    if (stableInsets != null) {
                        _data2.writeInt(1);
                        stableInsets.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(0);
                    }
                    if (outsets != null) {
                        _data2.writeInt(1);
                        outsets.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(0);
                    }
                    _data2.writeInt(reportDraw ? 1 : 0);
                    if (newMergedConfiguration != null) {
                        _data2.writeInt(1);
                        newMergedConfiguration.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(0);
                    }
                    if (backDropFrame != null) {
                        _data2.writeInt(1);
                        backDropFrame.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(0);
                    }
                    _data2.writeInt(forceLayout ? 1 : 0);
                    _data2.writeInt(alwaysConsumeSystemBars ? 1 : 0);
                    _data2.writeInt(displayId);
                    if (displayCutout != null) {
                        _data2.writeInt(1);
                        displayCutout.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(2, _data2, null, 1);
                    if (_status || Stub.getDefaultImpl() == null) {
                        _data2.recycle();
                        return;
                    }
                    _data = _data2;
                    try {
                        Stub.getDefaultImpl().resized(frame, overscanInsets, contentInsets, visibleInsets, stableInsets, outsets, reportDraw, newMergedConfiguration, backDropFrame, forceLayout, alwaysConsumeSystemBars, displayId, displayCutout);
                        _data.recycle();
                    } catch (Throwable th2) {
                        th = th2;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    _data = _data2;
                }
            }

            @Override // android.view.IWindow
            public void locationInParentDisplayChanged(Point offset) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (offset != null) {
                        _data.writeInt(1);
                        offset.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().locationInParentDisplayChanged(offset);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IWindow
            public void insetsChanged(InsetsState insetsState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (insetsState != null) {
                        _data.writeInt(1);
                        insetsState.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().insetsChanged(insetsState);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IWindow
            public void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] activeControls) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (insetsState != null) {
                        _data.writeInt(1);
                        insetsState.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeTypedArray(activeControls, 0);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().insetsControlChanged(insetsState, activeControls);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IWindow
            public void moved(int newX, int newY) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(newX);
                    _data.writeInt(newY);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().moved(newX, newY);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IWindow
            public void dispatchAppVisibility(boolean visible) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(visible ? 1 : 0);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dispatchAppVisibility(visible);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IWindow
            public void dispatchGetNewSurface() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dispatchGetNewSurface();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IWindow
            public void windowFocusChanged(boolean hasFocus, boolean inTouchMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(hasFocus ? 1 : 0);
                    _data.writeInt(inTouchMode ? 1 : 0);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().windowFocusChanged(hasFocus, inTouchMode);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IWindow
            public void closeSystemDialogs(String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(reason);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().closeSystemDialogs(reason);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IWindow
            public void dispatchWallpaperOffsets(float x, float y, float xStep, float yStep, boolean sync) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeFloat(x);
                    _data.writeFloat(y);
                    _data.writeFloat(xStep);
                    _data.writeFloat(yStep);
                    _data.writeInt(sync ? 1 : 0);
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dispatchWallpaperOffsets(x, y, xStep, yStep, sync);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IWindow
            public void dispatchWallpaperCommand(String action, int x, int y, int z, Bundle extras, boolean sync) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeString(action);
                } catch (Throwable th2) {
                    th = th2;
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(x);
                } catch (Throwable th3) {
                    th = th3;
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeInt(y);
                    try {
                        _data.writeInt(z);
                        if (extras != null) {
                            _data.writeInt(1);
                            extras.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        _data.writeInt(sync ? 1 : 0);
                    } catch (Throwable th4) {
                        th = th4;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    _data.recycle();
                    throw th;
                }
                try {
                    boolean _status = this.mRemote.transact(12, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dispatchWallpaperCommand(action, x, y, z, extras, sync);
                        _data.recycle();
                        return;
                    }
                    _data.recycle();
                } catch (Throwable th6) {
                    th = th6;
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.view.IWindow
            public void dispatchDragEvent(DragEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(13, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dispatchDragEvent(event);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IWindow
            public void updatePointerIcon(float x, float y) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeFloat(x);
                    _data.writeFloat(y);
                    boolean _status = this.mRemote.transact(14, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updatePointerIcon(x, y);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IWindow
            public void dispatchSystemUiVisibilityChanged(int seq, int globalVisibility, int localValue, int localChanges) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(seq);
                    _data.writeInt(globalVisibility);
                    _data.writeInt(localValue);
                    _data.writeInt(localChanges);
                    boolean _status = this.mRemote.transact(15, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dispatchSystemUiVisibilityChanged(seq, globalVisibility, localValue, localChanges);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IWindow
            public void dispatchWindowShown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(16, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dispatchWindowShown();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IWindow
            public void requestAppKeyboardShortcuts(IResultReceiver receiver, int deviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    _data.writeInt(deviceId);
                    boolean _status = this.mRemote.transact(17, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().requestAppKeyboardShortcuts(receiver, deviceId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.IWindow
            public void dispatchPointerCaptureChanged(boolean hasCapture) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(hasCapture ? 1 : 0);
                    boolean _status = this.mRemote.transact(18, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().dispatchPointerCaptureChanged(hasCapture);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IWindow impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IWindow getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
