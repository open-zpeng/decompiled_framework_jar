package com.android.ims.internal;

import android.accounts.AccountManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.provider.Telephony;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsStreamMediaProfile;
import android.telephony.ims.aidl.IImsCallSessionListener;
import com.android.ims.internal.IImsVideoCallProvider;

/* loaded from: classes3.dex */
public interface IImsCallSession extends IInterface {
    void accept(int i, ImsStreamMediaProfile imsStreamMediaProfile) throws RemoteException;

    void close() throws RemoteException;

    void deflect(String str) throws RemoteException;

    void extendToConference(String[] strArr) throws RemoteException;

    String getCallId() throws RemoteException;

    ImsCallProfile getCallProfile() throws RemoteException;

    ImsCallProfile getLocalCallProfile() throws RemoteException;

    String getProperty(String str) throws RemoteException;

    ImsCallProfile getRemoteCallProfile() throws RemoteException;

    int getState() throws RemoteException;

    IImsVideoCallProvider getVideoCallProvider() throws RemoteException;

    void hold(ImsStreamMediaProfile imsStreamMediaProfile) throws RemoteException;

    void inviteParticipants(String[] strArr) throws RemoteException;

    boolean isInCall() throws RemoteException;

    boolean isMultiparty() throws RemoteException;

    void merge() throws RemoteException;

    void reject(int i) throws RemoteException;

    void removeParticipants(String[] strArr) throws RemoteException;

    void resume(ImsStreamMediaProfile imsStreamMediaProfile) throws RemoteException;

    void sendDtmf(char c, Message message) throws RemoteException;

    void sendRttMessage(String str) throws RemoteException;

    void sendRttModifyRequest(ImsCallProfile imsCallProfile) throws RemoteException;

    void sendRttModifyResponse(boolean z) throws RemoteException;

    void sendUssd(String str) throws RemoteException;

    void setListener(android.telephony.ims.aidl.IImsCallSessionListener iImsCallSessionListener) throws RemoteException;

    void setMute(boolean z) throws RemoteException;

    void start(String str, ImsCallProfile imsCallProfile) throws RemoteException;

    void startConference(String[] strArr, ImsCallProfile imsCallProfile) throws RemoteException;

    void startDtmf(char c) throws RemoteException;

    void stopDtmf() throws RemoteException;

    void terminate(int i) throws RemoteException;

    void update(int i, ImsStreamMediaProfile imsStreamMediaProfile) throws RemoteException;

    /* loaded from: classes3.dex */
    public static class Default implements IImsCallSession {
        @Override // com.android.ims.internal.IImsCallSession
        public void close() throws RemoteException {
        }

        @Override // com.android.ims.internal.IImsCallSession
        public String getCallId() throws RemoteException {
            return null;
        }

        @Override // com.android.ims.internal.IImsCallSession
        public ImsCallProfile getCallProfile() throws RemoteException {
            return null;
        }

        @Override // com.android.ims.internal.IImsCallSession
        public ImsCallProfile getLocalCallProfile() throws RemoteException {
            return null;
        }

        @Override // com.android.ims.internal.IImsCallSession
        public ImsCallProfile getRemoteCallProfile() throws RemoteException {
            return null;
        }

        @Override // com.android.ims.internal.IImsCallSession
        public String getProperty(String name) throws RemoteException {
            return null;
        }

        @Override // com.android.ims.internal.IImsCallSession
        public int getState() throws RemoteException {
            return 0;
        }

        @Override // com.android.ims.internal.IImsCallSession
        public boolean isInCall() throws RemoteException {
            return false;
        }

        @Override // com.android.ims.internal.IImsCallSession
        public void setListener(android.telephony.ims.aidl.IImsCallSessionListener listener) throws RemoteException {
        }

        @Override // com.android.ims.internal.IImsCallSession
        public void setMute(boolean muted) throws RemoteException {
        }

        @Override // com.android.ims.internal.IImsCallSession
        public void start(String callee, ImsCallProfile profile) throws RemoteException {
        }

        @Override // com.android.ims.internal.IImsCallSession
        public void startConference(String[] participants, ImsCallProfile profile) throws RemoteException {
        }

        @Override // com.android.ims.internal.IImsCallSession
        public void accept(int callType, ImsStreamMediaProfile profile) throws RemoteException {
        }

        @Override // com.android.ims.internal.IImsCallSession
        public void deflect(String deflectNumber) throws RemoteException {
        }

        @Override // com.android.ims.internal.IImsCallSession
        public void reject(int reason) throws RemoteException {
        }

        @Override // com.android.ims.internal.IImsCallSession
        public void terminate(int reason) throws RemoteException {
        }

        @Override // com.android.ims.internal.IImsCallSession
        public void hold(ImsStreamMediaProfile profile) throws RemoteException {
        }

        @Override // com.android.ims.internal.IImsCallSession
        public void resume(ImsStreamMediaProfile profile) throws RemoteException {
        }

        @Override // com.android.ims.internal.IImsCallSession
        public void merge() throws RemoteException {
        }

        @Override // com.android.ims.internal.IImsCallSession
        public void update(int callType, ImsStreamMediaProfile profile) throws RemoteException {
        }

        @Override // com.android.ims.internal.IImsCallSession
        public void extendToConference(String[] participants) throws RemoteException {
        }

        @Override // com.android.ims.internal.IImsCallSession
        public void inviteParticipants(String[] participants) throws RemoteException {
        }

        @Override // com.android.ims.internal.IImsCallSession
        public void removeParticipants(String[] participants) throws RemoteException {
        }

        @Override // com.android.ims.internal.IImsCallSession
        public void sendDtmf(char c, Message result) throws RemoteException {
        }

        @Override // com.android.ims.internal.IImsCallSession
        public void startDtmf(char c) throws RemoteException {
        }

        @Override // com.android.ims.internal.IImsCallSession
        public void stopDtmf() throws RemoteException {
        }

        @Override // com.android.ims.internal.IImsCallSession
        public void sendUssd(String ussdMessage) throws RemoteException {
        }

        @Override // com.android.ims.internal.IImsCallSession
        public IImsVideoCallProvider getVideoCallProvider() throws RemoteException {
            return null;
        }

        @Override // com.android.ims.internal.IImsCallSession
        public boolean isMultiparty() throws RemoteException {
            return false;
        }

        @Override // com.android.ims.internal.IImsCallSession
        public void sendRttModifyRequest(ImsCallProfile toProfile) throws RemoteException {
        }

        @Override // com.android.ims.internal.IImsCallSession
        public void sendRttModifyResponse(boolean status) throws RemoteException {
        }

        @Override // com.android.ims.internal.IImsCallSession
        public void sendRttMessage(String rttMessage) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IImsCallSession {
        private static final String DESCRIPTOR = "com.android.ims.internal.IImsCallSession";
        static final int TRANSACTION_accept = 13;
        static final int TRANSACTION_close = 1;
        static final int TRANSACTION_deflect = 14;
        static final int TRANSACTION_extendToConference = 21;
        static final int TRANSACTION_getCallId = 2;
        static final int TRANSACTION_getCallProfile = 3;
        static final int TRANSACTION_getLocalCallProfile = 4;
        static final int TRANSACTION_getProperty = 6;
        static final int TRANSACTION_getRemoteCallProfile = 5;
        static final int TRANSACTION_getState = 7;
        static final int TRANSACTION_getVideoCallProvider = 28;
        static final int TRANSACTION_hold = 17;
        static final int TRANSACTION_inviteParticipants = 22;
        static final int TRANSACTION_isInCall = 8;
        static final int TRANSACTION_isMultiparty = 29;
        static final int TRANSACTION_merge = 19;
        static final int TRANSACTION_reject = 15;
        static final int TRANSACTION_removeParticipants = 23;
        static final int TRANSACTION_resume = 18;
        static final int TRANSACTION_sendDtmf = 24;
        static final int TRANSACTION_sendRttMessage = 32;
        static final int TRANSACTION_sendRttModifyRequest = 30;
        static final int TRANSACTION_sendRttModifyResponse = 31;
        static final int TRANSACTION_sendUssd = 27;
        static final int TRANSACTION_setListener = 9;
        static final int TRANSACTION_setMute = 10;
        static final int TRANSACTION_start = 11;
        static final int TRANSACTION_startConference = 12;
        static final int TRANSACTION_startDtmf = 25;
        static final int TRANSACTION_stopDtmf = 26;
        static final int TRANSACTION_terminate = 16;
        static final int TRANSACTION_update = 20;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IImsCallSession asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IImsCallSession)) {
                return (IImsCallSession) iin;
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
                    return "close";
                case 2:
                    return "getCallId";
                case 3:
                    return "getCallProfile";
                case 4:
                    return "getLocalCallProfile";
                case 5:
                    return "getRemoteCallProfile";
                case 6:
                    return "getProperty";
                case 7:
                    return "getState";
                case 8:
                    return "isInCall";
                case 9:
                    return "setListener";
                case 10:
                    return "setMute";
                case 11:
                    return Telephony.BaseMmsColumns.START;
                case 12:
                    return "startConference";
                case 13:
                    return "accept";
                case 14:
                    return "deflect";
                case 15:
                    return "reject";
                case 16:
                    return "terminate";
                case 17:
                    return "hold";
                case 18:
                    return "resume";
                case 19:
                    return "merge";
                case 20:
                    return AccountManager.USER_DATA_EXTRA_UPDATE;
                case 21:
                    return "extendToConference";
                case 22:
                    return "inviteParticipants";
                case 23:
                    return "removeParticipants";
                case 24:
                    return "sendDtmf";
                case 25:
                    return "startDtmf";
                case 26:
                    return "stopDtmf";
                case 27:
                    return "sendUssd";
                case 28:
                    return "getVideoCallProvider";
                case 29:
                    return "isMultiparty";
                case 30:
                    return "sendRttModifyRequest";
                case 31:
                    return "sendRttModifyResponse";
                case 32:
                    return "sendRttMessage";
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
            boolean _arg0;
            ImsCallProfile _arg1;
            ImsCallProfile _arg12;
            ImsStreamMediaProfile _arg13;
            ImsStreamMediaProfile _arg02;
            ImsStreamMediaProfile _arg03;
            ImsStreamMediaProfile _arg14;
            Message _arg15;
            ImsCallProfile _arg04;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    close();
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _result = getCallId();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    ImsCallProfile _result2 = getCallProfile();
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
                    ImsCallProfile _result3 = getLocalCallProfile();
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    ImsCallProfile _result4 = getRemoteCallProfile();
                    reply.writeNoException();
                    if (_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _result5 = getProperty(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _result6 = getState();
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isInCall = isInCall();
                    reply.writeNoException();
                    reply.writeInt(isInCall ? 1 : 0);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    setListener(IImsCallSessionListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    setMute(_arg0);
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    if (data.readInt() != 0) {
                        _arg1 = ImsCallProfile.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    start(_arg05, _arg1);
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    String[] _arg06 = data.createStringArray();
                    if (data.readInt() != 0) {
                        _arg12 = ImsCallProfile.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    startConference(_arg06, _arg12);
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg07 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg13 = ImsStreamMediaProfile.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    accept(_arg07, _arg13);
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    deflect(data.readString());
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    reject(data.readInt());
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    terminate(data.readInt());
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = ImsStreamMediaProfile.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    hold(_arg02);
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = ImsStreamMediaProfile.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    resume(_arg03);
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    merge();
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg14 = ImsStreamMediaProfile.CREATOR.createFromParcel(data);
                    } else {
                        _arg14 = null;
                    }
                    update(_arg08, _arg14);
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    extendToConference(data.createStringArray());
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    inviteParticipants(data.createStringArray());
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    removeParticipants(data.createStringArray());
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    char _arg09 = (char) data.readInt();
                    if (data.readInt() != 0) {
                        _arg15 = Message.CREATOR.createFromParcel(data);
                    } else {
                        _arg15 = null;
                    }
                    sendDtmf(_arg09, _arg15);
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    startDtmf((char) data.readInt());
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    stopDtmf();
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    sendUssd(data.readString());
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    IImsVideoCallProvider _result7 = getVideoCallProvider();
                    reply.writeNoException();
                    reply.writeStrongBinder(_result7 != null ? _result7.asBinder() : null);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isMultiparty = isMultiparty();
                    reply.writeNoException();
                    reply.writeInt(isMultiparty ? 1 : 0);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = ImsCallProfile.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    sendRttModifyRequest(_arg04);
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    sendRttModifyResponse(_arg0);
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    sendRttMessage(data.readString());
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements IImsCallSession {
            public static IImsCallSession sDefaultImpl;
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

            @Override // com.android.ims.internal.IImsCallSession
            public void close() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().close();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public String getCallId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCallId();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public ImsCallProfile getCallProfile() throws RemoteException {
                ImsCallProfile _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCallProfile();
                    }
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

            @Override // com.android.ims.internal.IImsCallSession
            public ImsCallProfile getLocalCallProfile() throws RemoteException {
                ImsCallProfile _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLocalCallProfile();
                    }
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

            @Override // com.android.ims.internal.IImsCallSession
            public ImsCallProfile getRemoteCallProfile() throws RemoteException {
                ImsCallProfile _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRemoteCallProfile();
                    }
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

            @Override // com.android.ims.internal.IImsCallSession
            public String getProperty(String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getProperty(name);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public int getState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getState();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public boolean isInCall() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isInCall();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public void setListener(android.telephony.ims.aidl.IImsCallSessionListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setListener(listener);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public void setMute(boolean muted) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(muted ? 1 : 0);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMute(muted);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public void start(String callee, ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callee);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().start(callee, profile);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public void startConference(String[] participants, ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(participants);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startConference(participants, profile);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public void accept(int callType, ImsStreamMediaProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(callType);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().accept(callType, profile);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public void deflect(String deflectNumber) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(deflectNumber);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().deflect(deflectNumber);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public void reject(int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(reason);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reject(reason);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public void terminate(int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(reason);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().terminate(reason);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public void hold(ImsStreamMediaProfile profile) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().hold(profile);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public void resume(ImsStreamMediaProfile profile) throws RemoteException {
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
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().resume(profile);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public void merge() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().merge();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public void update(int callType, ImsStreamMediaProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(callType);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().update(callType, profile);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public void extendToConference(String[] participants) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(participants);
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().extendToConference(participants);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public void inviteParticipants(String[] participants) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(participants);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().inviteParticipants(participants);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public void removeParticipants(String[] participants) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(participants);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeParticipants(participants);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public void sendDtmf(char c, Message result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(c);
                    if (result != null) {
                        _data.writeInt(1);
                        result.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().sendDtmf(c, result);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public void startDtmf(char c) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(c);
                    boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startDtmf(c);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public void stopDtmf() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().stopDtmf();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public void sendUssd(String ussdMessage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(ussdMessage);
                    boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().sendUssd(ussdMessage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public IImsVideoCallProvider getVideoCallProvider() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getVideoCallProvider();
                    }
                    _reply.readException();
                    IImsVideoCallProvider _result = IImsVideoCallProvider.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public boolean isMultiparty() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isMultiparty();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public void sendRttModifyRequest(ImsCallProfile toProfile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (toProfile != null) {
                        _data.writeInt(1);
                        toProfile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(30, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().sendRttModifyRequest(toProfile);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public void sendRttModifyResponse(boolean status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status ? 1 : 0);
                    boolean _status = this.mRemote.transact(31, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().sendRttModifyResponse(status);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsCallSession
            public void sendRttMessage(String rttMessage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(rttMessage);
                    boolean _status = this.mRemote.transact(32, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().sendRttMessage(rttMessage);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IImsCallSession impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IImsCallSession getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
