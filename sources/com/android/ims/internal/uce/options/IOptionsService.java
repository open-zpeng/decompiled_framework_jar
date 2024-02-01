package com.android.ims.internal.uce.options;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.ims.internal.uce.common.CapInfo;
import com.android.ims.internal.uce.common.StatusCode;
import com.android.ims.internal.uce.common.UceLong;
import com.android.ims.internal.uce.options.IOptionsListener;

/* loaded from: classes3.dex */
public interface IOptionsService extends IInterface {
    @UnsupportedAppUsage
    StatusCode addListener(int i, IOptionsListener iOptionsListener, UceLong uceLong) throws RemoteException;

    @UnsupportedAppUsage
    StatusCode getContactCap(int i, String str, int i2) throws RemoteException;

    @UnsupportedAppUsage
    StatusCode getContactListCap(int i, String[] strArr, int i2) throws RemoteException;

    @UnsupportedAppUsage
    StatusCode getMyInfo(int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    StatusCode getVersion(int i) throws RemoteException;

    @UnsupportedAppUsage
    StatusCode removeListener(int i, UceLong uceLong) throws RemoteException;

    @UnsupportedAppUsage
    StatusCode responseIncomingOptions(int i, int i2, int i3, String str, OptionsCapInfo optionsCapInfo, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    StatusCode setMyInfo(int i, CapInfo capInfo, int i2) throws RemoteException;

    /* loaded from: classes3.dex */
    public static class Default implements IOptionsService {
        @Override // com.android.ims.internal.uce.options.IOptionsService
        public StatusCode getVersion(int optionsServiceHandle) throws RemoteException {
            return null;
        }

        @Override // com.android.ims.internal.uce.options.IOptionsService
        public StatusCode addListener(int optionsServiceHandle, IOptionsListener optionsListener, UceLong optionsServiceListenerHdl) throws RemoteException {
            return null;
        }

        @Override // com.android.ims.internal.uce.options.IOptionsService
        public StatusCode removeListener(int optionsServiceHandle, UceLong optionsServiceListenerHdl) throws RemoteException {
            return null;
        }

        @Override // com.android.ims.internal.uce.options.IOptionsService
        public StatusCode setMyInfo(int optionsServiceHandle, CapInfo capInfo, int reqUserData) throws RemoteException {
            return null;
        }

        @Override // com.android.ims.internal.uce.options.IOptionsService
        public StatusCode getMyInfo(int optionsServiceHandle, int reqUserdata) throws RemoteException {
            return null;
        }

        @Override // com.android.ims.internal.uce.options.IOptionsService
        public StatusCode getContactCap(int optionsServiceHandle, String remoteURI, int reqUserData) throws RemoteException {
            return null;
        }

        @Override // com.android.ims.internal.uce.options.IOptionsService
        public StatusCode getContactListCap(int optionsServiceHandle, String[] remoteURIList, int reqUserData) throws RemoteException {
            return null;
        }

        @Override // com.android.ims.internal.uce.options.IOptionsService
        public StatusCode responseIncomingOptions(int optionsServiceHandle, int tId, int sipResponseCode, String reasonPhrase, OptionsCapInfo capInfo, boolean bContactInBL) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IOptionsService {
        private static final String DESCRIPTOR = "com.android.ims.internal.uce.options.IOptionsService";
        static final int TRANSACTION_addListener = 2;
        static final int TRANSACTION_getContactCap = 6;
        static final int TRANSACTION_getContactListCap = 7;
        static final int TRANSACTION_getMyInfo = 5;
        static final int TRANSACTION_getVersion = 1;
        static final int TRANSACTION_removeListener = 3;
        static final int TRANSACTION_responseIncomingOptions = 8;
        static final int TRANSACTION_setMyInfo = 4;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IOptionsService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IOptionsService)) {
                return (IOptionsService) iin;
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
                    return "getVersion";
                case 2:
                    return "addListener";
                case 3:
                    return "removeListener";
                case 4:
                    return "setMyInfo";
                case 5:
                    return "getMyInfo";
                case 6:
                    return "getContactCap";
                case 7:
                    return "getContactListCap";
                case 8:
                    return "responseIncomingOptions";
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
            UceLong _arg2;
            UceLong _arg1;
            CapInfo _arg12;
            OptionsCapInfo _arg4;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0 = data.readInt();
                    StatusCode _result = getVersion(_arg0);
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
                    int _arg02 = data.readInt();
                    IOptionsListener _arg13 = IOptionsListener.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg2 = UceLong.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    StatusCode _result2 = addListener(_arg02, _arg13, _arg2);
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    if (_arg2 != null) {
                        reply.writeInt(1);
                        _arg2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = UceLong.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    StatusCode _result3 = removeListener(_arg03, _arg1);
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg12 = CapInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    int _arg22 = data.readInt();
                    StatusCode _result4 = setMyInfo(_arg04, _arg12, _arg22);
                    reply.writeNoException();
                    if (_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    int _arg14 = data.readInt();
                    StatusCode _result5 = getMyInfo(_arg05, _arg14);
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    String _arg15 = data.readString();
                    int _arg23 = data.readInt();
                    StatusCode _result6 = getContactCap(_arg06, _arg15, _arg23);
                    reply.writeNoException();
                    if (_result6 != null) {
                        reply.writeInt(1);
                        _result6.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg07 = data.readInt();
                    String[] _arg16 = data.createStringArray();
                    int _arg24 = data.readInt();
                    StatusCode _result7 = getContactListCap(_arg07, _arg16, _arg24);
                    reply.writeNoException();
                    if (_result7 != null) {
                        reply.writeInt(1);
                        _result7.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    int _arg17 = data.readInt();
                    int _arg25 = data.readInt();
                    String _arg3 = data.readString();
                    if (data.readInt() != 0) {
                        _arg4 = OptionsCapInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg4 = null;
                    }
                    boolean _arg5 = data.readInt() != 0;
                    StatusCode _result8 = responseIncomingOptions(_arg08, _arg17, _arg25, _arg3, _arg4, _arg5);
                    reply.writeNoException();
                    if (_result8 != null) {
                        reply.writeInt(1);
                        _result8.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements IOptionsService {
            public static IOptionsService sDefaultImpl;
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

            @Override // com.android.ims.internal.uce.options.IOptionsService
            public StatusCode getVersion(int optionsServiceHandle) throws RemoteException {
                StatusCode _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(optionsServiceHandle);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVersion(optionsServiceHandle);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = StatusCode.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.uce.options.IOptionsService
            public StatusCode addListener(int optionsServiceHandle, IOptionsListener optionsListener, UceLong optionsServiceListenerHdl) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(optionsServiceHandle);
                    StatusCode _result = null;
                    _data.writeStrongBinder(optionsListener != null ? optionsListener.asBinder() : null);
                    if (optionsServiceListenerHdl != null) {
                        _data.writeInt(1);
                        optionsServiceListenerHdl.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().addListener(optionsServiceHandle, optionsListener, optionsServiceListenerHdl);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = StatusCode.CREATOR.createFromParcel(_reply);
                    }
                    if (_reply.readInt() != 0) {
                        optionsServiceListenerHdl.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.uce.options.IOptionsService
            public StatusCode removeListener(int optionsServiceHandle, UceLong optionsServiceListenerHdl) throws RemoteException {
                StatusCode _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(optionsServiceHandle);
                    if (optionsServiceListenerHdl != null) {
                        _data.writeInt(1);
                        optionsServiceListenerHdl.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().removeListener(optionsServiceHandle, optionsServiceListenerHdl);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = StatusCode.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.uce.options.IOptionsService
            public StatusCode setMyInfo(int optionsServiceHandle, CapInfo capInfo, int reqUserData) throws RemoteException {
                StatusCode _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(optionsServiceHandle);
                    if (capInfo != null) {
                        _data.writeInt(1);
                        capInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(reqUserData);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setMyInfo(optionsServiceHandle, capInfo, reqUserData);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = StatusCode.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.uce.options.IOptionsService
            public StatusCode getMyInfo(int optionsServiceHandle, int reqUserdata) throws RemoteException {
                StatusCode _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(optionsServiceHandle);
                    _data.writeInt(reqUserdata);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getMyInfo(optionsServiceHandle, reqUserdata);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = StatusCode.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.uce.options.IOptionsService
            public StatusCode getContactCap(int optionsServiceHandle, String remoteURI, int reqUserData) throws RemoteException {
                StatusCode _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(optionsServiceHandle);
                    _data.writeString(remoteURI);
                    _data.writeInt(reqUserData);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getContactCap(optionsServiceHandle, remoteURI, reqUserData);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = StatusCode.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.uce.options.IOptionsService
            public StatusCode getContactListCap(int optionsServiceHandle, String[] remoteURIList, int reqUserData) throws RemoteException {
                StatusCode _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(optionsServiceHandle);
                    _data.writeStringArray(remoteURIList);
                    _data.writeInt(reqUserData);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getContactListCap(optionsServiceHandle, remoteURIList, reqUserData);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = StatusCode.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.uce.options.IOptionsService
            public StatusCode responseIncomingOptions(int optionsServiceHandle, int tId, int sipResponseCode, String reasonPhrase, OptionsCapInfo capInfo, boolean bContactInBL) throws RemoteException {
                StatusCode _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(optionsServiceHandle);
                    } catch (Throwable th) {
                        th = th;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(tId);
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(sipResponseCode);
                        try {
                            _data.writeString(reasonPhrase);
                            int i = 1;
                            if (capInfo != null) {
                                _data.writeInt(1);
                                capInfo.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (!bContactInBL) {
                                i = 0;
                            }
                            _data.writeInt(i);
                        } catch (Throwable th3) {
                            th = th3;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                }
                try {
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        StatusCode responseIncomingOptions = Stub.getDefaultImpl().responseIncomingOptions(optionsServiceHandle, tId, sipResponseCode, reasonPhrase, capInfo, bContactInBL);
                        _reply.recycle();
                        _data.recycle();
                        return responseIncomingOptions;
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = StatusCode.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } catch (Throwable th6) {
                    th = th6;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }
        }

        public static boolean setDefaultImpl(IOptionsService impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IOptionsService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
