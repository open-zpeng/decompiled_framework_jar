package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.aidl.IImsCapabilityCallback;
import android.telephony.ims.aidl.IImsMmTelListener;
import android.telephony.ims.aidl.IImsSmsListener;
import android.telephony.ims.feature.CapabilityChangeRequest;
import com.android.ims.internal.IImsCallSession;
import com.android.ims.internal.IImsEcbm;
import com.android.ims.internal.IImsMultiEndpoint;
import com.android.ims.internal.IImsUt;
/* loaded from: classes2.dex */
public interface IImsMmTelFeature extends IInterface {
    private protected synchronized void acknowledgeSms(int i, int i2, int i3) throws RemoteException;

    private protected synchronized void acknowledgeSmsReport(int i, int i2, int i3) throws RemoteException;

    private protected synchronized void addCapabilityCallback(IImsCapabilityCallback iImsCapabilityCallback) throws RemoteException;

    private protected synchronized void changeCapabilitiesConfiguration(CapabilityChangeRequest capabilityChangeRequest, IImsCapabilityCallback iImsCapabilityCallback) throws RemoteException;

    private protected synchronized ImsCallProfile createCallProfile(int i, int i2) throws RemoteException;

    private protected synchronized IImsCallSession createCallSession(ImsCallProfile imsCallProfile) throws RemoteException;

    private protected synchronized IImsEcbm getEcbmInterface() throws RemoteException;

    private protected synchronized int getFeatureState() throws RemoteException;

    private protected synchronized IImsMultiEndpoint getMultiEndpointInterface() throws RemoteException;

    private protected synchronized String getSmsFormat() throws RemoteException;

    private protected synchronized IImsUt getUtInterface() throws RemoteException;

    private protected synchronized void onSmsReady() throws RemoteException;

    private protected synchronized void queryCapabilityConfiguration(int i, int i2, IImsCapabilityCallback iImsCapabilityCallback) throws RemoteException;

    private protected synchronized int queryCapabilityStatus() throws RemoteException;

    private protected synchronized void removeCapabilityCallback(IImsCapabilityCallback iImsCapabilityCallback) throws RemoteException;

    private protected synchronized void sendSms(int i, int i2, String str, String str2, boolean z, byte[] bArr) throws RemoteException;

    private protected synchronized void setListener(IImsMmTelListener iImsMmTelListener) throws RemoteException;

    private protected synchronized void setSmsListener(IImsSmsListener iImsSmsListener) throws RemoteException;

    private protected synchronized void setUiTtyMode(int i, Message message) throws RemoteException;

    private protected synchronized int shouldProcessCall(String[] strArr) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IImsMmTelFeature {
        public protected static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsMmTelFeature";
        public private protected static final int TRANSACTION_acknowledgeSms = 17;
        public private protected static final int TRANSACTION_acknowledgeSmsReport = 18;
        public private protected static final int TRANSACTION_addCapabilityCallback = 11;
        public private protected static final int TRANSACTION_changeCapabilitiesConfiguration = 13;
        public private protected static final int TRANSACTION_createCallProfile = 3;
        public private protected static final int TRANSACTION_createCallSession = 4;
        public private protected static final int TRANSACTION_getEcbmInterface = 7;
        public private protected static final int TRANSACTION_getFeatureState = 2;
        public private protected static final int TRANSACTION_getMultiEndpointInterface = 9;
        public private protected static final int TRANSACTION_getSmsFormat = 19;
        public private protected static final int TRANSACTION_getUtInterface = 6;
        public private protected static final int TRANSACTION_onSmsReady = 20;
        public private protected static final int TRANSACTION_queryCapabilityConfiguration = 14;
        public private protected static final int TRANSACTION_queryCapabilityStatus = 10;
        public private protected static final int TRANSACTION_removeCapabilityCallback = 12;
        public private protected static final int TRANSACTION_sendSms = 16;
        public private protected static final int TRANSACTION_setListener = 1;
        public private protected static final int TRANSACTION_setSmsListener = 15;
        public private protected static final int TRANSACTION_setUiTtyMode = 8;
        public private protected static final int TRANSACTION_shouldProcessCall = 5;

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized IImsMmTelFeature asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IImsMmTelFeature)) {
                return (IImsMmTelFeature) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ImsCallProfile _arg0;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    IImsMmTelListener _arg02 = IImsMmTelListener.Stub.asInterface(data.readStrongBinder());
                    setListener(_arg02);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _result = getFeatureState();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    int _arg1 = data.readInt();
                    ImsCallProfile _result2 = createCallProfile(_arg03, _arg1);
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = ImsCallProfile.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    IImsCallSession _result3 = createCallSession(_arg0);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result3 != null ? _result3.asBinder() : null);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg04 = data.createStringArray();
                    int _result4 = shouldProcessCall(_arg04);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    IImsUt _result5 = getUtInterface();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result5 != null ? _result5.asBinder() : null);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    IImsEcbm _result6 = getEcbmInterface();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result6 != null ? _result6.asBinder() : null);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    Message _arg12 = data.readInt() != 0 ? Message.CREATOR.createFromParcel(data) : null;
                    setUiTtyMode(_arg05, _arg12);
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    IImsMultiEndpoint _result7 = getMultiEndpointInterface();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result7 != null ? _result7.asBinder() : null);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    int _result8 = queryCapabilityStatus();
                    reply.writeNoException();
                    reply.writeInt(_result8);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    IImsCapabilityCallback _arg06 = IImsCapabilityCallback.Stub.asInterface(data.readStrongBinder());
                    addCapabilityCallback(_arg06);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    IImsCapabilityCallback _arg07 = IImsCapabilityCallback.Stub.asInterface(data.readStrongBinder());
                    removeCapabilityCallback(_arg07);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    CapabilityChangeRequest _arg08 = data.readInt() != 0 ? CapabilityChangeRequest.CREATOR.createFromParcel(data) : null;
                    CapabilityChangeRequest _arg09 = _arg08;
                    IImsCapabilityCallback _arg13 = IImsCapabilityCallback.Stub.asInterface(data.readStrongBinder());
                    changeCapabilitiesConfiguration(_arg09, _arg13);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg010 = data.readInt();
                    int _arg14 = data.readInt();
                    IImsCapabilityCallback _arg2 = IImsCapabilityCallback.Stub.asInterface(data.readStrongBinder());
                    queryCapabilityConfiguration(_arg010, _arg14, _arg2);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    IImsSmsListener _arg011 = IImsSmsListener.Stub.asInterface(data.readStrongBinder());
                    setSmsListener(_arg011);
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg012 = data.readInt();
                    int _arg15 = data.readInt();
                    String _arg22 = data.readString();
                    String _arg3 = data.readString();
                    boolean _arg4 = data.readInt() != 0;
                    byte[] _arg5 = data.createByteArray();
                    sendSms(_arg012, _arg15, _arg22, _arg3, _arg4, _arg5);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg013 = data.readInt();
                    int _arg16 = data.readInt();
                    int _arg23 = data.readInt();
                    acknowledgeSms(_arg013, _arg16, _arg23);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg014 = data.readInt();
                    int _arg17 = data.readInt();
                    int _arg24 = data.readInt();
                    acknowledgeSmsReport(_arg014, _arg17, _arg24);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    String _result9 = getSmsFormat();
                    reply.writeNoException();
                    reply.writeString(_result9);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    onSmsReady();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IImsMmTelFeature {
            public protected IBinder mRemote;

            public private protected synchronized Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            private protected synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            private protected synchronized void setListener(IImsMmTelListener l) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(l != null ? l.asBinder() : null);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized int getFeatureState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized ImsCallProfile createCallProfile(int callSessionType, int callType) throws RemoteException {
                ImsCallProfile _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(callSessionType);
                    _data.writeInt(callType);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ImsCallProfile.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized IImsCallSession createCallSession(ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    IImsCallSession _result = IImsCallSession.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized int shouldProcessCall(String[] uris) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(uris);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized IImsUt getUtInterface() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    IImsUt _result = IImsUt.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized IImsEcbm getEcbmInterface() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    IImsEcbm _result = IImsEcbm.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void setUiTtyMode(int uiTtyMode, Message onCompleteMessage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uiTtyMode);
                    if (onCompleteMessage != null) {
                        _data.writeInt(1);
                        onCompleteMessage.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized IImsMultiEndpoint getMultiEndpointInterface() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    IImsMultiEndpoint _result = IImsMultiEndpoint.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized int queryCapabilityStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void addCapabilityCallback(IImsCapabilityCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void removeCapabilityCallback(IImsCapabilityCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    this.mRemote.transact(12, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void changeCapabilitiesConfiguration(CapabilityChangeRequest request, IImsCapabilityCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    this.mRemote.transact(13, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void queryCapabilityConfiguration(int capability, int radioTech, IImsCapabilityCallback c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(capability);
                    _data.writeInt(radioTech);
                    _data.writeStrongBinder(c != null ? c.asBinder() : null);
                    this.mRemote.transact(14, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void setSmsListener(IImsSmsListener l) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(l != null ? l.asBinder() : null);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void sendSms(int token, int messageRef, String format, String smsc, boolean retry, byte[] pdu) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(token);
                    _data.writeInt(messageRef);
                    _data.writeString(format);
                    _data.writeString(smsc);
                    _data.writeInt(retry ? 1 : 0);
                    _data.writeByteArray(pdu);
                    this.mRemote.transact(16, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void acknowledgeSms(int token, int messageRef, int result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(token);
                    _data.writeInt(messageRef);
                    _data.writeInt(result);
                    this.mRemote.transact(17, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void acknowledgeSmsReport(int token, int messageRef, int result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(token);
                    _data.writeInt(messageRef);
                    _data.writeInt(result);
                    this.mRemote.transact(18, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized String getSmsFormat() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void onSmsReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
