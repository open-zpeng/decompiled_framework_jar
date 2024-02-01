package com.android.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.ImsiEncryptionInfo;
/* loaded from: classes3.dex */
public interface IPhoneSubInfo extends IInterface {
    synchronized ImsiEncryptionInfo getCarrierInfoForImsiEncryption(int i, int i2, String str) throws RemoteException;

    synchronized String getCompleteVoiceMailNumber() throws RemoteException;

    synchronized String getCompleteVoiceMailNumberForSubscriber(int i) throws RemoteException;

    synchronized String getDeviceId(String str) throws RemoteException;

    synchronized String getDeviceIdForPhone(int i, String str) throws RemoteException;

    synchronized String getDeviceSvn(String str) throws RemoteException;

    synchronized String getDeviceSvnUsingSubId(int i, String str) throws RemoteException;

    synchronized String getGroupIdLevel1ForSubscriber(int i, String str) throws RemoteException;

    private protected String getIccSerialNumber(String str) throws RemoteException;

    synchronized String getIccSerialNumberForSubscriber(int i, String str) throws RemoteException;

    synchronized String getIccSimChallengeResponse(int i, int i2, int i3, String str) throws RemoteException;

    synchronized String getImeiForSubscriber(int i, String str) throws RemoteException;

    synchronized String getIsimDomain(int i) throws RemoteException;

    synchronized String getIsimImpi(int i) throws RemoteException;

    synchronized String[] getIsimImpu(int i) throws RemoteException;

    synchronized String getIsimIst(int i) throws RemoteException;

    synchronized String[] getIsimPcscf(int i) throws RemoteException;

    synchronized String getLine1AlphaTag(String str) throws RemoteException;

    synchronized String getLine1AlphaTagForSubscriber(int i, String str) throws RemoteException;

    synchronized String getLine1Number(String str) throws RemoteException;

    synchronized String getLine1NumberForSubscriber(int i, String str) throws RemoteException;

    synchronized String getMsisdn(String str) throws RemoteException;

    synchronized String getMsisdnForSubscriber(int i, String str) throws RemoteException;

    synchronized String getNaiForSubscriber(int i, String str) throws RemoteException;

    private protected String getSubscriberId(String str) throws RemoteException;

    synchronized String getSubscriberIdForSubscriber(int i, String str) throws RemoteException;

    synchronized String getVoiceMailAlphaTag(String str) throws RemoteException;

    synchronized String getVoiceMailAlphaTagForSubscriber(int i, String str) throws RemoteException;

    synchronized String getVoiceMailNumber(String str) throws RemoteException;

    synchronized String getVoiceMailNumberForSubscriber(int i, String str) throws RemoteException;

    synchronized void resetCarrierKeysForImsiEncryption(int i, String str) throws RemoteException;

    synchronized void setCarrierInfoForImsiEncryption(int i, String str, ImsiEncryptionInfo imsiEncryptionInfo) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IPhoneSubInfo {
        private static final String DESCRIPTOR = "com.android.internal.telephony.IPhoneSubInfo";
        static final int TRANSACTION_getCarrierInfoForImsiEncryption = 22;
        static final int TRANSACTION_getCompleteVoiceMailNumber = 20;
        static final int TRANSACTION_getCompleteVoiceMailNumberForSubscriber = 21;
        public private protected static final int TRANSACTION_getDeviceId = 1;
        static final int TRANSACTION_getDeviceIdForPhone = 3;
        static final int TRANSACTION_getDeviceSvn = 5;
        static final int TRANSACTION_getDeviceSvnUsingSubId = 6;
        static final int TRANSACTION_getGroupIdLevel1ForSubscriber = 9;
        static final int TRANSACTION_getIccSerialNumber = 10;
        static final int TRANSACTION_getIccSerialNumberForSubscriber = 11;
        static final int TRANSACTION_getIccSimChallengeResponse = 32;
        static final int TRANSACTION_getImeiForSubscriber = 4;
        static final int TRANSACTION_getIsimDomain = 28;
        static final int TRANSACTION_getIsimImpi = 27;
        static final int TRANSACTION_getIsimImpu = 29;
        static final int TRANSACTION_getIsimIst = 30;
        static final int TRANSACTION_getIsimPcscf = 31;
        static final int TRANSACTION_getLine1AlphaTag = 14;
        static final int TRANSACTION_getLine1AlphaTagForSubscriber = 15;
        static final int TRANSACTION_getLine1Number = 12;
        static final int TRANSACTION_getLine1NumberForSubscriber = 13;
        static final int TRANSACTION_getMsisdn = 16;
        static final int TRANSACTION_getMsisdnForSubscriber = 17;
        static final int TRANSACTION_getNaiForSubscriber = 2;
        static final int TRANSACTION_getSubscriberId = 7;
        static final int TRANSACTION_getSubscriberIdForSubscriber = 8;
        static final int TRANSACTION_getVoiceMailAlphaTag = 25;
        static final int TRANSACTION_getVoiceMailAlphaTagForSubscriber = 26;
        static final int TRANSACTION_getVoiceMailNumber = 18;
        static final int TRANSACTION_getVoiceMailNumberForSubscriber = 19;
        static final int TRANSACTION_resetCarrierKeysForImsiEncryption = 24;
        static final int TRANSACTION_setCarrierInfoForImsiEncryption = 23;

        private protected Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static IPhoneSubInfo asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IPhoneSubInfo)) {
                return (IPhoneSubInfo) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ImsiEncryptionInfo _arg2;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0 = data.readString();
                    String _result = getDeviceId(_arg0);
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    String _arg1 = data.readString();
                    String _result2 = getNaiForSubscriber(_arg02, _arg1);
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    String _arg12 = data.readString();
                    String _result3 = getDeviceIdForPhone(_arg03, _arg12);
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    String _arg13 = data.readString();
                    String _result4 = getImeiForSubscriber(_arg04, _arg13);
                    reply.writeNoException();
                    reply.writeString(_result4);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    String _result5 = getDeviceSvn(_arg05);
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    String _arg14 = data.readString();
                    String _result6 = getDeviceSvnUsingSubId(_arg06, _arg14);
                    reply.writeNoException();
                    reply.writeString(_result6);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    String _result7 = getSubscriberId(_arg07);
                    reply.writeNoException();
                    reply.writeString(_result7);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    String _arg15 = data.readString();
                    String _result8 = getSubscriberIdForSubscriber(_arg08, _arg15);
                    reply.writeNoException();
                    reply.writeString(_result8);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg09 = data.readInt();
                    String _arg16 = data.readString();
                    String _result9 = getGroupIdLevel1ForSubscriber(_arg09, _arg16);
                    reply.writeNoException();
                    reply.writeString(_result9);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg010 = data.readString();
                    String _result10 = getIccSerialNumber(_arg010);
                    reply.writeNoException();
                    reply.writeString(_result10);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg011 = data.readInt();
                    String _arg17 = data.readString();
                    String _result11 = getIccSerialNumberForSubscriber(_arg011, _arg17);
                    reply.writeNoException();
                    reply.writeString(_result11);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg012 = data.readString();
                    String _result12 = getLine1Number(_arg012);
                    reply.writeNoException();
                    reply.writeString(_result12);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg013 = data.readInt();
                    String _arg18 = data.readString();
                    String _result13 = getLine1NumberForSubscriber(_arg013, _arg18);
                    reply.writeNoException();
                    reply.writeString(_result13);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg014 = data.readString();
                    String _result14 = getLine1AlphaTag(_arg014);
                    reply.writeNoException();
                    reply.writeString(_result14);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg015 = data.readInt();
                    String _arg19 = data.readString();
                    String _result15 = getLine1AlphaTagForSubscriber(_arg015, _arg19);
                    reply.writeNoException();
                    reply.writeString(_result15);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg016 = data.readString();
                    String _result16 = getMsisdn(_arg016);
                    reply.writeNoException();
                    reply.writeString(_result16);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg017 = data.readInt();
                    String _arg110 = data.readString();
                    String _result17 = getMsisdnForSubscriber(_arg017, _arg110);
                    reply.writeNoException();
                    reply.writeString(_result17);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg018 = data.readString();
                    String _result18 = getVoiceMailNumber(_arg018);
                    reply.writeNoException();
                    reply.writeString(_result18);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg019 = data.readInt();
                    String _arg111 = data.readString();
                    String _result19 = getVoiceMailNumberForSubscriber(_arg019, _arg111);
                    reply.writeNoException();
                    reply.writeString(_result19);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    String _result20 = getCompleteVoiceMailNumber();
                    reply.writeNoException();
                    reply.writeString(_result20);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg020 = data.readInt();
                    String _result21 = getCompleteVoiceMailNumberForSubscriber(_arg020);
                    reply.writeNoException();
                    reply.writeString(_result21);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg021 = data.readInt();
                    int _arg112 = data.readInt();
                    String _arg22 = data.readString();
                    ImsiEncryptionInfo _result22 = getCarrierInfoForImsiEncryption(_arg021, _arg112, _arg22);
                    reply.writeNoException();
                    if (_result22 != null) {
                        reply.writeInt(1);
                        _result22.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg022 = data.readInt();
                    String _arg113 = data.readString();
                    if (data.readInt() != 0) {
                        _arg2 = ImsiEncryptionInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    setCarrierInfoForImsiEncryption(_arg022, _arg113, _arg2);
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg023 = data.readInt();
                    String _arg114 = data.readString();
                    resetCarrierKeysForImsiEncryption(_arg023, _arg114);
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg024 = data.readString();
                    String _result23 = getVoiceMailAlphaTag(_arg024);
                    reply.writeNoException();
                    reply.writeString(_result23);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg025 = data.readInt();
                    String _arg115 = data.readString();
                    String _result24 = getVoiceMailAlphaTagForSubscriber(_arg025, _arg115);
                    reply.writeNoException();
                    reply.writeString(_result24);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg026 = data.readInt();
                    String _result25 = getIsimImpi(_arg026);
                    reply.writeNoException();
                    reply.writeString(_result25);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg027 = data.readInt();
                    String _result26 = getIsimDomain(_arg027);
                    reply.writeNoException();
                    reply.writeString(_result26);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg028 = data.readInt();
                    String[] _result27 = getIsimImpu(_arg028);
                    reply.writeNoException();
                    reply.writeStringArray(_result27);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg029 = data.readInt();
                    String _result28 = getIsimIst(_arg029);
                    reply.writeNoException();
                    reply.writeString(_result28);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg030 = data.readInt();
                    String[] _result29 = getIsimPcscf(_arg030);
                    reply.writeNoException();
                    reply.writeStringArray(_result29);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg031 = data.readInt();
                    int _arg116 = data.readInt();
                    int _arg23 = data.readInt();
                    String _arg3 = data.readString();
                    String _result30 = getIccSimChallengeResponse(_arg031, _arg116, _arg23, _arg3);
                    reply.writeNoException();
                    reply.writeString(_result30);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements IPhoneSubInfo {
            private IBinder mRemote;

            public private protected Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            private protected String getDeviceId(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String getNaiForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String getDeviceIdForPhone(int phoneId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String getImeiForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String getDeviceSvn(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String getDeviceSvnUsingSubId(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized String getSubscriberId(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String getSubscriberIdForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String getGroupIdLevel1ForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized String getIccSerialNumber(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String getIccSerialNumberForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String getLine1Number(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String getLine1NumberForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String getLine1AlphaTag(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String getLine1AlphaTagForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String getMsisdn(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String getMsisdnForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String getVoiceMailNumber(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String getVoiceMailNumberForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String getCompleteVoiceMailNumber() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String getCompleteVoiceMailNumberForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized ImsiEncryptionInfo getCarrierInfoForImsiEncryption(int subId, int keyType, String callingPackage) throws RemoteException {
                ImsiEncryptionInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(keyType);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ImsiEncryptionInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized void setCarrierInfoForImsiEncryption(int subId, String callingPackage, ImsiEncryptionInfo imsiEncryptionInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    if (imsiEncryptionInfo != null) {
                        _data.writeInt(1);
                        imsiEncryptionInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized void resetCarrierKeysForImsiEncryption(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String getVoiceMailAlphaTag(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String getVoiceMailAlphaTagForSubscriber(int subId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String getIsimImpi(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String getIsimDomain(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String[] getIsimImpu(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String getIsimIst(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String[] getIsimPcscf(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                    String[] _result = _reply.createStringArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneSubInfo
            public synchronized String getIccSimChallengeResponse(int subId, int appType, int authType, String data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(appType);
                    _data.writeInt(authType);
                    _data.writeString(data);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
