package android.hardware.display;

import android.content.pm.ParceledListSlice;
import android.graphics.Point;
import android.hardware.display.IDisplayManagerCallback;
import android.hardware.display.IVirtualDisplayCallback;
import android.media.projection.IMediaProjection;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.DisplayInfo;
import android.view.Surface;
/* loaded from: classes.dex */
public interface IDisplayManager extends IInterface {
    synchronized void connectWifiDisplay(String str) throws RemoteException;

    synchronized int createVirtualDisplay(IVirtualDisplayCallback iVirtualDisplayCallback, IMediaProjection iMediaProjection, String str, String str2, int i, int i2, int i3, Surface surface, int i4, String str3) throws RemoteException;

    synchronized void disconnectWifiDisplay() throws RemoteException;

    synchronized void forgetWifiDisplay(String str) throws RemoteException;

    synchronized ParceledListSlice getAmbientBrightnessStats() throws RemoteException;

    synchronized BrightnessConfiguration getBrightnessConfigurationForUser(int i) throws RemoteException;

    synchronized ParceledListSlice getBrightnessEvents(String str) throws RemoteException;

    synchronized BrightnessConfiguration getDefaultBrightnessConfiguration() throws RemoteException;

    synchronized int[] getDisplayIds() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    DisplayInfo getDisplayInfo(int i) throws RemoteException;

    synchronized Curve getMinimumBrightnessCurve() throws RemoteException;

    int getSmoothBrightness(int i, int i2, float f) throws RemoteException;

    synchronized Point getStableDisplaySize() throws RemoteException;

    synchronized WifiDisplayStatus getWifiDisplayStatus() throws RemoteException;

    synchronized void pauseWifiDisplay() throws RemoteException;

    synchronized void registerCallback(IDisplayManagerCallback iDisplayManagerCallback) throws RemoteException;

    synchronized void releaseVirtualDisplay(IVirtualDisplayCallback iVirtualDisplayCallback) throws RemoteException;

    synchronized void renameWifiDisplay(String str, String str2) throws RemoteException;

    synchronized void requestColorMode(int i, int i2) throws RemoteException;

    synchronized void resizeVirtualDisplay(IVirtualDisplayCallback iVirtualDisplayCallback, int i, int i2, int i3) throws RemoteException;

    synchronized void resumeWifiDisplay() throws RemoteException;

    synchronized void setBrightnessConfigurationForUser(BrightnessConfiguration brightnessConfiguration, int i, String str) throws RemoteException;

    synchronized void setSaturationLevel(float f) throws RemoteException;

    synchronized void setTemporaryAutoBrightnessAdjustment(float f) throws RemoteException;

    synchronized void setTemporaryBrightness(int i) throws RemoteException;

    synchronized void setVirtualDisplaySurface(IVirtualDisplayCallback iVirtualDisplayCallback, Surface surface) throws RemoteException;

    synchronized void startWifiDisplayScan() throws RemoteException;

    synchronized void stopWifiDisplayScan() throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IDisplayManager {
        private static final String DESCRIPTOR = "android.hardware.display.IDisplayManager";
        static final int TRANSACTION_connectWifiDisplay = 6;
        static final int TRANSACTION_createVirtualDisplay = 15;
        static final int TRANSACTION_disconnectWifiDisplay = 7;
        static final int TRANSACTION_forgetWifiDisplay = 9;
        static final int TRANSACTION_getAmbientBrightnessStats = 21;
        static final int TRANSACTION_getBrightnessConfigurationForUser = 23;
        static final int TRANSACTION_getBrightnessEvents = 20;
        static final int TRANSACTION_getDefaultBrightnessConfiguration = 24;
        static final int TRANSACTION_getDisplayIds = 2;
        static final int TRANSACTION_getDisplayInfo = 1;
        static final int TRANSACTION_getMinimumBrightnessCurve = 27;
        static final int TRANSACTION_getSmoothBrightness = 28;
        static final int TRANSACTION_getStableDisplaySize = 19;
        static final int TRANSACTION_getWifiDisplayStatus = 12;
        static final int TRANSACTION_pauseWifiDisplay = 10;
        static final int TRANSACTION_registerCallback = 3;
        static final int TRANSACTION_releaseVirtualDisplay = 18;
        static final int TRANSACTION_renameWifiDisplay = 8;
        static final int TRANSACTION_requestColorMode = 13;
        static final int TRANSACTION_resizeVirtualDisplay = 16;
        static final int TRANSACTION_resumeWifiDisplay = 11;
        static final int TRANSACTION_setBrightnessConfigurationForUser = 22;
        static final int TRANSACTION_setSaturationLevel = 14;
        static final int TRANSACTION_setTemporaryAutoBrightnessAdjustment = 26;
        static final int TRANSACTION_setTemporaryBrightness = 25;
        static final int TRANSACTION_setVirtualDisplaySurface = 17;
        static final int TRANSACTION_startWifiDisplayScan = 4;
        static final int TRANSACTION_stopWifiDisplayScan = 5;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static IDisplayManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IDisplayManager)) {
                return (IDisplayManager) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code != 1598968902) {
                switch (code) {
                    case 1:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg0 = data.readInt();
                        DisplayInfo _result = getDisplayInfo(_arg0);
                        reply.writeNoException();
                        if (_result != null) {
                            reply.writeInt(1);
                            _result.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        int[] _result2 = getDisplayIds();
                        reply.writeNoException();
                        reply.writeIntArray(_result2);
                        return true;
                    case 3:
                        data.enforceInterface(DESCRIPTOR);
                        IDisplayManagerCallback _arg02 = IDisplayManagerCallback.Stub.asInterface(data.readStrongBinder());
                        registerCallback(_arg02);
                        reply.writeNoException();
                        return true;
                    case 4:
                        data.enforceInterface(DESCRIPTOR);
                        startWifiDisplayScan();
                        reply.writeNoException();
                        return true;
                    case 5:
                        data.enforceInterface(DESCRIPTOR);
                        stopWifiDisplayScan();
                        reply.writeNoException();
                        return true;
                    case 6:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg03 = data.readString();
                        connectWifiDisplay(_arg03);
                        reply.writeNoException();
                        return true;
                    case 7:
                        data.enforceInterface(DESCRIPTOR);
                        disconnectWifiDisplay();
                        reply.writeNoException();
                        return true;
                    case 8:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg04 = data.readString();
                        String _arg1 = data.readString();
                        renameWifiDisplay(_arg04, _arg1);
                        reply.writeNoException();
                        return true;
                    case 9:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg05 = data.readString();
                        forgetWifiDisplay(_arg05);
                        reply.writeNoException();
                        return true;
                    case 10:
                        data.enforceInterface(DESCRIPTOR);
                        pauseWifiDisplay();
                        reply.writeNoException();
                        return true;
                    case 11:
                        data.enforceInterface(DESCRIPTOR);
                        resumeWifiDisplay();
                        reply.writeNoException();
                        return true;
                    case 12:
                        data.enforceInterface(DESCRIPTOR);
                        WifiDisplayStatus _result3 = getWifiDisplayStatus();
                        reply.writeNoException();
                        if (_result3 != null) {
                            reply.writeInt(1);
                            _result3.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 13:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg06 = data.readInt();
                        int _arg12 = data.readInt();
                        requestColorMode(_arg06, _arg12);
                        reply.writeNoException();
                        return true;
                    case 14:
                        data.enforceInterface(DESCRIPTOR);
                        float _arg07 = data.readFloat();
                        setSaturationLevel(_arg07);
                        reply.writeNoException();
                        return true;
                    case 15:
                        data.enforceInterface(DESCRIPTOR);
                        IVirtualDisplayCallback _arg08 = IVirtualDisplayCallback.Stub.asInterface(data.readStrongBinder());
                        IMediaProjection _arg13 = IMediaProjection.Stub.asInterface(data.readStrongBinder());
                        String _arg2 = data.readString();
                        String _arg3 = data.readString();
                        int _arg4 = data.readInt();
                        int _arg5 = data.readInt();
                        int _arg6 = data.readInt();
                        Surface _arg7 = data.readInt() != 0 ? Surface.CREATOR.createFromParcel(data) : null;
                        int _arg8 = data.readInt();
                        String _arg9 = data.readString();
                        int _result4 = createVirtualDisplay(_arg08, _arg13, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8, _arg9);
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 16:
                        data.enforceInterface(DESCRIPTOR);
                        IVirtualDisplayCallback _arg09 = IVirtualDisplayCallback.Stub.asInterface(data.readStrongBinder());
                        int _arg14 = data.readInt();
                        int _arg22 = data.readInt();
                        int _arg32 = data.readInt();
                        resizeVirtualDisplay(_arg09, _arg14, _arg22, _arg32);
                        reply.writeNoException();
                        return true;
                    case 17:
                        data.enforceInterface(DESCRIPTOR);
                        IVirtualDisplayCallback _arg010 = IVirtualDisplayCallback.Stub.asInterface(data.readStrongBinder());
                        Surface _arg15 = data.readInt() != 0 ? Surface.CREATOR.createFromParcel(data) : null;
                        setVirtualDisplaySurface(_arg010, _arg15);
                        reply.writeNoException();
                        return true;
                    case 18:
                        data.enforceInterface(DESCRIPTOR);
                        IVirtualDisplayCallback _arg011 = IVirtualDisplayCallback.Stub.asInterface(data.readStrongBinder());
                        releaseVirtualDisplay(_arg011);
                        reply.writeNoException();
                        return true;
                    case 19:
                        data.enforceInterface(DESCRIPTOR);
                        Point _result5 = getStableDisplaySize();
                        reply.writeNoException();
                        if (_result5 != null) {
                            reply.writeInt(1);
                            _result5.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 20:
                        data.enforceInterface(DESCRIPTOR);
                        String _arg012 = data.readString();
                        ParceledListSlice _result6 = getBrightnessEvents(_arg012);
                        reply.writeNoException();
                        if (_result6 != null) {
                            reply.writeInt(1);
                            _result6.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 21:
                        data.enforceInterface(DESCRIPTOR);
                        ParceledListSlice _result7 = getAmbientBrightnessStats();
                        reply.writeNoException();
                        if (_result7 != null) {
                            reply.writeInt(1);
                            _result7.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 22:
                        data.enforceInterface(DESCRIPTOR);
                        BrightnessConfiguration _arg013 = data.readInt() != 0 ? BrightnessConfiguration.CREATOR.createFromParcel(data) : null;
                        int _arg16 = data.readInt();
                        String _arg23 = data.readString();
                        setBrightnessConfigurationForUser(_arg013, _arg16, _arg23);
                        reply.writeNoException();
                        return true;
                    case 23:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg014 = data.readInt();
                        BrightnessConfiguration _result8 = getBrightnessConfigurationForUser(_arg014);
                        reply.writeNoException();
                        if (_result8 != null) {
                            reply.writeInt(1);
                            _result8.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 24:
                        data.enforceInterface(DESCRIPTOR);
                        BrightnessConfiguration _result9 = getDefaultBrightnessConfiguration();
                        reply.writeNoException();
                        if (_result9 != null) {
                            reply.writeInt(1);
                            _result9.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 25:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg015 = data.readInt();
                        setTemporaryBrightness(_arg015);
                        reply.writeNoException();
                        return true;
                    case 26:
                        data.enforceInterface(DESCRIPTOR);
                        float _arg016 = data.readFloat();
                        setTemporaryAutoBrightnessAdjustment(_arg016);
                        reply.writeNoException();
                        return true;
                    case 27:
                        data.enforceInterface(DESCRIPTOR);
                        Curve _result10 = getMinimumBrightnessCurve();
                        reply.writeNoException();
                        if (_result10 != null) {
                            reply.writeInt(1);
                            _result10.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 28:
                        data.enforceInterface(DESCRIPTOR);
                        int _arg017 = data.readInt();
                        int _arg17 = data.readInt();
                        float _arg24 = data.readFloat();
                        int _result11 = getSmoothBrightness(_arg017, _arg17, _arg24);
                        reply.writeNoException();
                        reply.writeInt(_result11);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(DESCRIPTOR);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IDisplayManager {
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

            public synchronized DisplayInfo getDisplayInfo(int displayId) throws RemoteException {
                DisplayInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = DisplayInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized int[] getDisplayIds() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized void registerCallback(IDisplayManagerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized void startWifiDisplayScan() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized void stopWifiDisplayScan() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized void connectWifiDisplay(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized void disconnectWifiDisplay() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized void renameWifiDisplay(String address, String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeString(alias);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized void forgetWifiDisplay(String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized void pauseWifiDisplay() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized void resumeWifiDisplay() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized WifiDisplayStatus getWifiDisplayStatus() throws RemoteException {
                WifiDisplayStatus _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = WifiDisplayStatus.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized void requestColorMode(int displayId, int colorMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    _data.writeInt(colorMode);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized void setSaturationLevel(float level) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeFloat(level);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized int createVirtualDisplay(IVirtualDisplayCallback callback, IMediaProjection projectionToken, String packageName, String name, int width, int height, int densityDpi, Surface surface, int flags, String uniqueId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeStrongBinder(projectionToken != null ? projectionToken.asBinder() : null);
                    _data.writeString(packageName);
                    _data.writeString(name);
                    _data.writeInt(width);
                    _data.writeInt(height);
                    _data.writeInt(densityDpi);
                    if (surface != null) {
                        _data.writeInt(1);
                        surface.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    _data.writeString(uniqueId);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized void resizeVirtualDisplay(IVirtualDisplayCallback token, int width, int height, int densityDpi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeInt(width);
                    _data.writeInt(height);
                    _data.writeInt(densityDpi);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized void setVirtualDisplaySurface(IVirtualDisplayCallback token, Surface surface) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    if (surface != null) {
                        _data.writeInt(1);
                        surface.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized void releaseVirtualDisplay(IVirtualDisplayCallback token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized Point getStableDisplaySize() throws RemoteException {
                Point _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Point.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized ParceledListSlice getBrightnessEvents(String callingPackage) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized ParceledListSlice getAmbientBrightnessStats() throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized void setBrightnessConfigurationForUser(BrightnessConfiguration c, int userId, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (c != null) {
                        _data.writeInt(1);
                        c.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    _data.writeString(packageName);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized BrightnessConfiguration getBrightnessConfigurationForUser(int userId) throws RemoteException {
                BrightnessConfiguration _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = BrightnessConfiguration.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized BrightnessConfiguration getDefaultBrightnessConfiguration() throws RemoteException {
                BrightnessConfiguration _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = BrightnessConfiguration.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized void setTemporaryBrightness(int brightness) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(brightness);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized void setTemporaryAutoBrightnessAdjustment(float adjustment) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeFloat(adjustment);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public synchronized Curve getMinimumBrightnessCurve() throws RemoteException {
                Curve _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Curve.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.display.IDisplayManager
            public int getSmoothBrightness(int from, int to, float percent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(from);
                    _data.writeInt(to);
                    _data.writeFloat(percent);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
