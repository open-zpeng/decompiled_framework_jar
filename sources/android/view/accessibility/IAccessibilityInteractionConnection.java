package android.view.accessibility;

import android.graphics.Region;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.MagnificationSpec;
import android.view.accessibility.IAccessibilityInteractionConnectionCallback;
/* loaded from: classes2.dex */
public interface IAccessibilityInteractionConnection extends IInterface {
    synchronized void findAccessibilityNodeInfoByAccessibilityId(long j, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec, Bundle bundle) throws RemoteException;

    synchronized void findAccessibilityNodeInfosByText(long j, String str, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec) throws RemoteException;

    synchronized void findAccessibilityNodeInfosByViewId(long j, String str, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec) throws RemoteException;

    synchronized void findFocus(long j, int i, Region region, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2, MagnificationSpec magnificationSpec) throws RemoteException;

    synchronized void focusSearch(long j, int i, Region region, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2, MagnificationSpec magnificationSpec) throws RemoteException;

    synchronized void performAccessibilityAction(long j, int i, Bundle bundle, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IAccessibilityInteractionConnection {
        private static final String DESCRIPTOR = "android.view.accessibility.IAccessibilityInteractionConnection";
        static final int TRANSACTION_findAccessibilityNodeInfoByAccessibilityId = 1;
        static final int TRANSACTION_findAccessibilityNodeInfosByText = 3;
        static final int TRANSACTION_findAccessibilityNodeInfosByViewId = 2;
        static final int TRANSACTION_findFocus = 4;
        static final int TRANSACTION_focusSearch = 5;
        static final int TRANSACTION_performAccessibilityAction = 6;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IAccessibilityInteractionConnection asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IAccessibilityInteractionConnection)) {
                return (IAccessibilityInteractionConnection) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Region _arg1;
            MagnificationSpec _arg7;
            Region _arg2;
            Region _arg22;
            Region _arg23;
            Region _arg24;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg0 = data.readLong();
                    if (data.readInt() != 0) {
                        Region _arg12 = Region.CREATOR.createFromParcel(data);
                        _arg1 = _arg12;
                    } else {
                        _arg1 = null;
                    }
                    int _arg25 = data.readInt();
                    IAccessibilityInteractionConnectionCallback _arg3 = IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder());
                    int _arg4 = data.readInt();
                    int _arg5 = data.readInt();
                    long _arg6 = data.readLong();
                    if (data.readInt() != 0) {
                        MagnificationSpec _arg72 = MagnificationSpec.CREATOR.createFromParcel(data);
                        _arg7 = _arg72;
                    } else {
                        _arg7 = null;
                    }
                    Bundle _arg8 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    findAccessibilityNodeInfoByAccessibilityId(_arg0, _arg1, _arg25, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg02 = data.readLong();
                    String _arg13 = data.readString();
                    if (data.readInt() != 0) {
                        Region _arg26 = Region.CREATOR.createFromParcel(data);
                        _arg2 = _arg26;
                    } else {
                        _arg2 = null;
                    }
                    int _arg32 = data.readInt();
                    IAccessibilityInteractionConnectionCallback _arg42 = IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder());
                    int _arg52 = data.readInt();
                    int _arg62 = data.readInt();
                    long _arg73 = data.readLong();
                    MagnificationSpec _arg82 = data.readInt() != 0 ? MagnificationSpec.CREATOR.createFromParcel(data) : null;
                    findAccessibilityNodeInfosByViewId(_arg02, _arg13, _arg2, _arg32, _arg42, _arg52, _arg62, _arg73, _arg82);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg03 = data.readLong();
                    String _arg14 = data.readString();
                    if (data.readInt() != 0) {
                        Region _arg27 = Region.CREATOR.createFromParcel(data);
                        _arg22 = _arg27;
                    } else {
                        _arg22 = null;
                    }
                    int _arg33 = data.readInt();
                    IAccessibilityInteractionConnectionCallback _arg43 = IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder());
                    int _arg53 = data.readInt();
                    int _arg63 = data.readInt();
                    long _arg74 = data.readLong();
                    MagnificationSpec _arg83 = data.readInt() != 0 ? MagnificationSpec.CREATOR.createFromParcel(data) : null;
                    findAccessibilityNodeInfosByText(_arg03, _arg14, _arg22, _arg33, _arg43, _arg53, _arg63, _arg74, _arg83);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg04 = data.readLong();
                    int _arg15 = data.readInt();
                    if (data.readInt() != 0) {
                        Region _arg28 = Region.CREATOR.createFromParcel(data);
                        _arg23 = _arg28;
                    } else {
                        _arg23 = null;
                    }
                    int _arg34 = data.readInt();
                    IAccessibilityInteractionConnectionCallback _arg44 = IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder());
                    int _arg54 = data.readInt();
                    int _arg64 = data.readInt();
                    long _arg75 = data.readLong();
                    MagnificationSpec _arg84 = data.readInt() != 0 ? MagnificationSpec.CREATOR.createFromParcel(data) : null;
                    findFocus(_arg04, _arg15, _arg23, _arg34, _arg44, _arg54, _arg64, _arg75, _arg84);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg05 = data.readLong();
                    int _arg16 = data.readInt();
                    if (data.readInt() != 0) {
                        Region _arg29 = Region.CREATOR.createFromParcel(data);
                        _arg24 = _arg29;
                    } else {
                        _arg24 = null;
                    }
                    int _arg35 = data.readInt();
                    IAccessibilityInteractionConnectionCallback _arg45 = IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder());
                    int _arg55 = data.readInt();
                    int _arg65 = data.readInt();
                    long _arg76 = data.readLong();
                    MagnificationSpec _arg85 = data.readInt() != 0 ? MagnificationSpec.CREATOR.createFromParcel(data) : null;
                    focusSearch(_arg05, _arg16, _arg24, _arg35, _arg45, _arg55, _arg65, _arg76, _arg85);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg06 = data.readLong();
                    int _arg17 = data.readInt();
                    Bundle _arg210 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    int _arg36 = data.readInt();
                    IAccessibilityInteractionConnectionCallback _arg46 = IAccessibilityInteractionConnectionCallback.Stub.asInterface(data.readStrongBinder());
                    int _arg56 = data.readInt();
                    int _arg66 = data.readInt();
                    long _arg77 = data.readLong();
                    performAccessibilityAction(_arg06, _arg17, _arg210, _arg36, _arg46, _arg56, _arg66, _arg77);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IAccessibilityInteractionConnection {
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

            @Override // android.view.accessibility.IAccessibilityInteractionConnection
            public synchronized void findAccessibilityNodeInfoByAccessibilityId(long accessibilityNodeId, Region bounds, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec, Bundle arguments) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeLong(accessibilityNodeId);
                        if (bounds != null) {
                            _data.writeInt(1);
                            bounds.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeInt(interactionId);
                            _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                        } catch (Throwable th) {
                            th = th;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
                try {
                    _data.writeInt(flags);
                    try {
                        _data.writeInt(interrogatingPid);
                        try {
                            _data.writeLong(interrogatingTid);
                            if (spec != null) {
                                _data.writeInt(1);
                                spec.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (arguments != null) {
                                _data.writeInt(1);
                                arguments.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            try {
                                this.mRemote.transact(1, _data, null, 1);
                                _data.recycle();
                            } catch (Throwable th4) {
                                th = th4;
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.view.accessibility.IAccessibilityInteractionConnection
            public synchronized void findAccessibilityNodeInfosByViewId(long accessibilityNodeId, String viewId, Region bounds, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeLong(accessibilityNodeId);
                        try {
                            _data.writeString(viewId);
                            if (bounds != null) {
                                _data.writeInt(1);
                                bounds.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                        } catch (Throwable th) {
                            th = th;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
                try {
                    _data.writeInt(interactionId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    try {
                        _data.writeInt(flags);
                        try {
                            _data.writeInt(interrogatingPid);
                            try {
                                _data.writeLong(interrogatingTid);
                                if (spec != null) {
                                    _data.writeInt(1);
                                    spec.writeToParcel(_data, 0);
                                } else {
                                    _data.writeInt(0);
                                }
                            } catch (Throwable th4) {
                                th = th4;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    _data.recycle();
                    throw th;
                }
                try {
                    this.mRemote.transact(2, _data, null, 1);
                    _data.recycle();
                } catch (Throwable th8) {
                    th = th8;
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.view.accessibility.IAccessibilityInteractionConnection
            public synchronized void findAccessibilityNodeInfosByText(long accessibilityNodeId, String text, Region bounds, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeLong(accessibilityNodeId);
                        try {
                            _data.writeString(text);
                            if (bounds != null) {
                                _data.writeInt(1);
                                bounds.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                        } catch (Throwable th) {
                            th = th;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
                try {
                    _data.writeInt(interactionId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    try {
                        _data.writeInt(flags);
                        try {
                            _data.writeInt(interrogatingPid);
                            try {
                                _data.writeLong(interrogatingTid);
                                if (spec != null) {
                                    _data.writeInt(1);
                                    spec.writeToParcel(_data, 0);
                                } else {
                                    _data.writeInt(0);
                                }
                            } catch (Throwable th4) {
                                th = th4;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    _data.recycle();
                    throw th;
                }
                try {
                    this.mRemote.transact(3, _data, null, 1);
                    _data.recycle();
                } catch (Throwable th8) {
                    th = th8;
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.view.accessibility.IAccessibilityInteractionConnection
            public synchronized void findFocus(long accessibilityNodeId, int focusType, Region bounds, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeLong(accessibilityNodeId);
                        try {
                            _data.writeInt(focusType);
                            if (bounds != null) {
                                _data.writeInt(1);
                                bounds.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                        } catch (Throwable th) {
                            th = th;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
                try {
                    _data.writeInt(interactionId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    try {
                        _data.writeInt(flags);
                        try {
                            _data.writeInt(interrogatingPid);
                            try {
                                _data.writeLong(interrogatingTid);
                                if (spec != null) {
                                    _data.writeInt(1);
                                    spec.writeToParcel(_data, 0);
                                } else {
                                    _data.writeInt(0);
                                }
                            } catch (Throwable th4) {
                                th = th4;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    _data.recycle();
                    throw th;
                }
                try {
                    this.mRemote.transact(4, _data, null, 1);
                    _data.recycle();
                } catch (Throwable th8) {
                    th = th8;
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.view.accessibility.IAccessibilityInteractionConnection
            public synchronized void focusSearch(long accessibilityNodeId, int direction, Region bounds, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeLong(accessibilityNodeId);
                        try {
                            _data.writeInt(direction);
                            if (bounds != null) {
                                _data.writeInt(1);
                                bounds.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                        } catch (Throwable th) {
                            th = th;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
                try {
                    _data.writeInt(interactionId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    try {
                        _data.writeInt(flags);
                        try {
                            _data.writeInt(interrogatingPid);
                            try {
                                _data.writeLong(interrogatingTid);
                                if (spec != null) {
                                    _data.writeInt(1);
                                    spec.writeToParcel(_data, 0);
                                } else {
                                    _data.writeInt(0);
                                }
                            } catch (Throwable th4) {
                                th = th4;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    _data.recycle();
                    throw th;
                }
                try {
                    this.mRemote.transact(5, _data, null, 1);
                    _data.recycle();
                } catch (Throwable th8) {
                    th = th8;
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.view.accessibility.IAccessibilityInteractionConnection
            public synchronized void performAccessibilityAction(long accessibilityNodeId, int action, Bundle arguments, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(accessibilityNodeId);
                    _data.writeInt(action);
                    if (arguments != null) {
                        _data.writeInt(1);
                        arguments.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(interactionId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(flags);
                    _data.writeInt(interrogatingPid);
                    _data.writeLong(interrogatingTid);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
