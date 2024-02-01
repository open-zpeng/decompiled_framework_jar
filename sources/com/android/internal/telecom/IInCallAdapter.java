package com.android.internal.telecom;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telecom.PhoneAccountHandle;
import java.util.List;
/* loaded from: classes3.dex */
public interface IInCallAdapter extends IInterface {
    synchronized void answerCall(String str, int i) throws RemoteException;

    synchronized void conference(String str, String str2) throws RemoteException;

    synchronized void deflectCall(String str, Uri uri) throws RemoteException;

    synchronized void disconnectCall(String str) throws RemoteException;

    synchronized void handoverTo(String str, PhoneAccountHandle phoneAccountHandle, int i, Bundle bundle) throws RemoteException;

    synchronized void holdCall(String str) throws RemoteException;

    synchronized void mergeConference(String str) throws RemoteException;

    synchronized void mute(boolean z) throws RemoteException;

    synchronized void phoneAccountSelected(String str, PhoneAccountHandle phoneAccountHandle, boolean z) throws RemoteException;

    synchronized void playDtmfTone(String str, char c) throws RemoteException;

    synchronized void postDialContinue(String str, boolean z) throws RemoteException;

    synchronized void pullExternalCall(String str) throws RemoteException;

    synchronized void putExtras(String str, Bundle bundle) throws RemoteException;

    synchronized void rejectCall(String str, boolean z, String str2) throws RemoteException;

    synchronized void removeExtras(String str, List<String> list) throws RemoteException;

    synchronized void respondToRttRequest(String str, int i, boolean z) throws RemoteException;

    synchronized void sendCallEvent(String str, String str2, int i, Bundle bundle) throws RemoteException;

    synchronized void sendRttRequest(String str) throws RemoteException;

    synchronized void setAudioRoute(int i, String str) throws RemoteException;

    synchronized void setRttMode(String str, int i) throws RemoteException;

    synchronized void splitFromConference(String str) throws RemoteException;

    synchronized void stopDtmfTone(String str) throws RemoteException;

    synchronized void stopRtt(String str) throws RemoteException;

    synchronized void swapConference(String str) throws RemoteException;

    synchronized void turnOffProximitySensor(boolean z) throws RemoteException;

    synchronized void turnOnProximitySensor() throws RemoteException;

    synchronized void unholdCall(String str) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IInCallAdapter {
        private static final String DESCRIPTOR = "com.android.internal.telecom.IInCallAdapter";
        static final int TRANSACTION_answerCall = 1;
        static final int TRANSACTION_conference = 13;
        static final int TRANSACTION_deflectCall = 2;
        static final int TRANSACTION_disconnectCall = 4;
        static final int TRANSACTION_handoverTo = 27;
        static final int TRANSACTION_holdCall = 5;
        static final int TRANSACTION_mergeConference = 15;
        static final int TRANSACTION_mute = 7;
        static final int TRANSACTION_phoneAccountSelected = 12;
        static final int TRANSACTION_playDtmfTone = 9;
        static final int TRANSACTION_postDialContinue = 11;
        static final int TRANSACTION_pullExternalCall = 19;
        static final int TRANSACTION_putExtras = 21;
        static final int TRANSACTION_rejectCall = 3;
        static final int TRANSACTION_removeExtras = 22;
        static final int TRANSACTION_respondToRttRequest = 24;
        static final int TRANSACTION_sendCallEvent = 20;
        static final int TRANSACTION_sendRttRequest = 23;
        static final int TRANSACTION_setAudioRoute = 8;
        static final int TRANSACTION_setRttMode = 26;
        static final int TRANSACTION_splitFromConference = 14;
        static final int TRANSACTION_stopDtmfTone = 10;
        static final int TRANSACTION_stopRtt = 25;
        static final int TRANSACTION_swapConference = 16;
        static final int TRANSACTION_turnOffProximitySensor = 18;
        static final int TRANSACTION_turnOnProximitySensor = 17;
        static final int TRANSACTION_unholdCall = 6;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IInCallAdapter asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IInCallAdapter)) {
                return (IInCallAdapter) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg2;
            PhoneAccountHandle _arg1;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0 = data.readString();
                    int _arg12 = data.readInt();
                    answerCall(_arg0, _arg12);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    Uri _arg13 = data.readInt() != 0 ? Uri.CREATOR.createFromParcel(data) : null;
                    deflectCall(_arg02, _arg13);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    _arg2 = data.readInt() != 0;
                    rejectCall(_arg03, _arg2, data.readString());
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    disconnectCall(_arg04);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    holdCall(_arg05);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg06 = data.readString();
                    unholdCall(_arg06);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    _arg2 = data.readInt() != 0;
                    mute(_arg2);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg07 = data.readInt();
                    String _arg14 = data.readString();
                    setAudioRoute(_arg07, _arg14);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    char _arg15 = (char) data.readInt();
                    playDtmfTone(_arg08, _arg15);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg09 = data.readString();
                    stopDtmfTone(_arg09);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg010 = data.readString();
                    _arg2 = data.readInt() != 0;
                    postDialContinue(_arg010, _arg2);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg011 = data.readString();
                    PhoneAccountHandle _arg16 = data.readInt() != 0 ? PhoneAccountHandle.CREATOR.createFromParcel(data) : null;
                    _arg2 = data.readInt() != 0;
                    phoneAccountSelected(_arg011, _arg16, _arg2);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg012 = data.readString();
                    String _arg17 = data.readString();
                    conference(_arg012, _arg17);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg013 = data.readString();
                    splitFromConference(_arg013);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg014 = data.readString();
                    mergeConference(_arg014);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg015 = data.readString();
                    swapConference(_arg015);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    turnOnProximitySensor();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    _arg2 = data.readInt() != 0;
                    turnOffProximitySensor(_arg2);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg016 = data.readString();
                    pullExternalCall(_arg016);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg017 = data.readString();
                    String _arg18 = data.readString();
                    int _arg22 = data.readInt();
                    Bundle _arg3 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    sendCallEvent(_arg017, _arg18, _arg22, _arg3);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg018 = data.readString();
                    Bundle _arg19 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    putExtras(_arg018, _arg19);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg019 = data.readString();
                    List<String> _arg110 = data.createStringArrayList();
                    removeExtras(_arg019, _arg110);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg020 = data.readString();
                    sendRttRequest(_arg020);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg021 = data.readString();
                    int _arg111 = data.readInt();
                    _arg2 = data.readInt() != 0;
                    respondToRttRequest(_arg021, _arg111, _arg2);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg022 = data.readString();
                    stopRtt(_arg022);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg023 = data.readString();
                    int _arg112 = data.readInt();
                    setRttMode(_arg023, _arg112);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg024 = data.readString();
                    if (data.readInt() != 0) {
                        _arg1 = PhoneAccountHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    int _arg23 = data.readInt();
                    Bundle _arg32 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    handoverTo(_arg024, _arg1, _arg23, _arg32);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes3.dex */
        private static class Proxy implements IInCallAdapter {
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

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void answerCall(String callId, int videoState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeInt(videoState);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void deflectCall(String callId, Uri address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (address != null) {
                        _data.writeInt(1);
                        address.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void rejectCall(String callId, boolean rejectWithMessage, String textMessage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeInt(rejectWithMessage ? 1 : 0);
                    _data.writeString(textMessage);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void disconnectCall(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void holdCall(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void unholdCall(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void mute(boolean shouldMute) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(shouldMute ? 1 : 0);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void setAudioRoute(int route, String bluetoothAddress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(route);
                    _data.writeString(bluetoothAddress);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void playDtmfTone(String callId, char digit) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeInt(digit);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void stopDtmfTone(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void postDialContinue(String callId, boolean proceed) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeInt(proceed ? 1 : 0);
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void phoneAccountSelected(String callId, PhoneAccountHandle accountHandle, boolean setDefault) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (accountHandle != null) {
                        _data.writeInt(1);
                        accountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(setDefault ? 1 : 0);
                    this.mRemote.transact(12, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void conference(String callId, String otherCallId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeString(otherCallId);
                    this.mRemote.transact(13, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void splitFromConference(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    this.mRemote.transact(14, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void mergeConference(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    this.mRemote.transact(15, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void swapConference(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    this.mRemote.transact(16, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void turnOnProximitySensor() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(17, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void turnOffProximitySensor(boolean screenOnImmediately) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(screenOnImmediately ? 1 : 0);
                    this.mRemote.transact(18, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void pullExternalCall(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    this.mRemote.transact(19, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void sendCallEvent(String callId, String event, int targetSdkVer, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeString(event);
                    _data.writeInt(targetSdkVer);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(20, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void putExtras(String callId, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(21, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void removeExtras(String callId, List<String> keys) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeStringList(keys);
                    this.mRemote.transact(22, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void sendRttRequest(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    this.mRemote.transact(23, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void respondToRttRequest(String callId, int id, boolean accept) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeInt(id);
                    _data.writeInt(accept ? 1 : 0);
                    this.mRemote.transact(24, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void stopRtt(String callId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    this.mRemote.transact(25, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void setRttMode(String callId, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    _data.writeInt(mode);
                    this.mRemote.transact(26, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.IInCallAdapter
            public synchronized void handoverTo(String callId, PhoneAccountHandle destAcct, int videoState, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callId);
                    if (destAcct != null) {
                        _data.writeInt(1);
                        destAcct.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(videoState);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(27, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
