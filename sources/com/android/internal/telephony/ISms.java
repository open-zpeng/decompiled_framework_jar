package com.android.internal.telephony;

import android.app.PendingIntent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
/* loaded from: classes3.dex */
public interface ISms extends IInterface {
    synchronized boolean copyMessageToIccEfForSubscriber(int i, String str, int i2, byte[] bArr, byte[] bArr2) throws RemoteException;

    synchronized String createAppSpecificSmsToken(int i, String str, PendingIntent pendingIntent) throws RemoteException;

    synchronized boolean disableCellBroadcastForSubscriber(int i, int i2, int i3) throws RemoteException;

    synchronized boolean disableCellBroadcastRangeForSubscriber(int i, int i2, int i3, int i4) throws RemoteException;

    synchronized boolean enableCellBroadcastForSubscriber(int i, int i2, int i3) throws RemoteException;

    synchronized boolean enableCellBroadcastRangeForSubscriber(int i, int i2, int i3, int i4) throws RemoteException;

    synchronized List<SmsRawData> getAllMessagesFromIccEfForSubscriber(int i, String str) throws RemoteException;

    synchronized String getImsSmsFormatForSubscriber(int i) throws RemoteException;

    synchronized int getPreferredSmsSubscription() throws RemoteException;

    synchronized int getPremiumSmsPermission(String str) throws RemoteException;

    synchronized int getPremiumSmsPermissionForSubscriber(int i, String str) throws RemoteException;

    synchronized void injectSmsPduForSubscriber(int i, byte[] bArr, String str, PendingIntent pendingIntent) throws RemoteException;

    synchronized boolean isImsSmsSupportedForSubscriber(int i) throws RemoteException;

    synchronized boolean isSMSPromptEnabled() throws RemoteException;

    synchronized boolean isSmsSimPickActivityNeeded(int i) throws RemoteException;

    synchronized void sendDataForSubscriber(int i, String str, String str2, String str3, int i2, byte[] bArr, PendingIntent pendingIntent, PendingIntent pendingIntent2) throws RemoteException;

    synchronized void sendDataForSubscriberWithSelfPermissions(int i, String str, String str2, String str3, int i2, byte[] bArr, PendingIntent pendingIntent, PendingIntent pendingIntent2) throws RemoteException;

    synchronized void sendMultipartTextForSubscriber(int i, String str, String str2, String str3, List<String> list, List<PendingIntent> list2, List<PendingIntent> list3, boolean z) throws RemoteException;

    synchronized void sendMultipartTextForSubscriberWithOptions(int i, String str, String str2, String str3, List<String> list, List<PendingIntent> list2, List<PendingIntent> list3, boolean z, int i2, boolean z2, int i3) throws RemoteException;

    synchronized void sendStoredMultipartText(int i, String str, Uri uri, String str2, List<PendingIntent> list, List<PendingIntent> list2) throws RemoteException;

    synchronized void sendStoredText(int i, String str, Uri uri, String str2, PendingIntent pendingIntent, PendingIntent pendingIntent2) throws RemoteException;

    synchronized void sendTextForSubscriber(int i, String str, String str2, String str3, String str4, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z) throws RemoteException;

    synchronized void sendTextForSubscriberWithOptions(int i, String str, String str2, String str3, String str4, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z, int i2, boolean z2, int i3) throws RemoteException;

    synchronized void sendTextForSubscriberWithSelfPermissions(int i, String str, String str2, String str3, String str4, PendingIntent pendingIntent, PendingIntent pendingIntent2, boolean z) throws RemoteException;

    synchronized void setPremiumSmsPermission(String str, int i) throws RemoteException;

    synchronized void setPremiumSmsPermissionForSubscriber(int i, String str, int i2) throws RemoteException;

    synchronized boolean updateMessageOnIccEfForSubscriber(int i, String str, int i2, int i3, byte[] bArr) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements ISms {
        private static final String DESCRIPTOR = "com.android.internal.telephony.ISms";
        static final int TRANSACTION_copyMessageToIccEfForSubscriber = 3;
        static final int TRANSACTION_createAppSpecificSmsToken = 27;
        static final int TRANSACTION_disableCellBroadcastForSubscriber = 13;
        static final int TRANSACTION_disableCellBroadcastRangeForSubscriber = 15;
        static final int TRANSACTION_enableCellBroadcastForSubscriber = 12;
        static final int TRANSACTION_enableCellBroadcastRangeForSubscriber = 14;
        static final int TRANSACTION_getAllMessagesFromIccEfForSubscriber = 1;
        static final int TRANSACTION_getImsSmsFormatForSubscriber = 23;
        static final int TRANSACTION_getPreferredSmsSubscription = 22;
        static final int TRANSACTION_getPremiumSmsPermission = 16;
        static final int TRANSACTION_getPremiumSmsPermissionForSubscriber = 17;
        static final int TRANSACTION_injectSmsPduForSubscriber = 9;
        static final int TRANSACTION_isImsSmsSupportedForSubscriber = 20;
        static final int TRANSACTION_isSMSPromptEnabled = 24;
        static final int TRANSACTION_isSmsSimPickActivityNeeded = 21;
        static final int TRANSACTION_sendDataForSubscriber = 4;
        static final int TRANSACTION_sendDataForSubscriberWithSelfPermissions = 5;
        static final int TRANSACTION_sendMultipartTextForSubscriber = 10;
        static final int TRANSACTION_sendMultipartTextForSubscriberWithOptions = 11;
        static final int TRANSACTION_sendStoredMultipartText = 26;
        static final int TRANSACTION_sendStoredText = 25;
        static final int TRANSACTION_sendTextForSubscriber = 6;
        static final int TRANSACTION_sendTextForSubscriberWithOptions = 8;
        static final int TRANSACTION_sendTextForSubscriberWithSelfPermissions = 7;
        static final int TRANSACTION_setPremiumSmsPermission = 18;
        static final int TRANSACTION_setPremiumSmsPermissionForSubscriber = 19;
        static final int TRANSACTION_updateMessageOnIccEfForSubscriber = 2;

        private protected Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static ISms asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ISms)) {
                return (ISms) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            PendingIntent _arg6;
            PendingIntent _arg7;
            PendingIntent _arg62;
            PendingIntent _arg72;
            PendingIntent _arg5;
            PendingIntent _arg2;
            PendingIntent _arg52;
            PendingIntent _arg53;
            PendingIntent _arg4;
            PendingIntent _arg54;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0 = data.readInt();
                    String _arg1 = data.readString();
                    List<SmsRawData> _result = getAllMessagesFromIccEfForSubscriber(_arg0, _arg1);
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    String _arg12 = data.readString();
                    int _arg22 = data.readInt();
                    int _arg3 = data.readInt();
                    byte[] _arg42 = data.createByteArray();
                    boolean updateMessageOnIccEfForSubscriber = updateMessageOnIccEfForSubscriber(_arg02, _arg12, _arg22, _arg3, _arg42);
                    reply.writeNoException();
                    reply.writeInt(updateMessageOnIccEfForSubscriber ? 1 : 0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    String _arg13 = data.readString();
                    int _arg23 = data.readInt();
                    byte[] _arg32 = data.createByteArray();
                    byte[] _arg43 = data.createByteArray();
                    boolean copyMessageToIccEfForSubscriber = copyMessageToIccEfForSubscriber(_arg03, _arg13, _arg23, _arg32, _arg43);
                    reply.writeNoException();
                    reply.writeInt(copyMessageToIccEfForSubscriber ? 1 : 0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    String _arg14 = data.readString();
                    String _arg24 = data.readString();
                    String _arg33 = data.readString();
                    int _arg44 = data.readInt();
                    byte[] _arg55 = data.createByteArray();
                    if (data.readInt() != 0) {
                        PendingIntent _arg63 = PendingIntent.CREATOR.createFromParcel(data);
                        _arg6 = _arg63;
                    } else {
                        _arg6 = null;
                    }
                    if (data.readInt() != 0) {
                        PendingIntent _arg73 = PendingIntent.CREATOR.createFromParcel(data);
                        _arg7 = _arg73;
                    } else {
                        _arg7 = null;
                    }
                    sendDataForSubscriber(_arg04, _arg14, _arg24, _arg33, _arg44, _arg55, _arg6, _arg7);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    String _arg15 = data.readString();
                    String _arg25 = data.readString();
                    String _arg34 = data.readString();
                    int _arg45 = data.readInt();
                    byte[] _arg56 = data.createByteArray();
                    if (data.readInt() != 0) {
                        PendingIntent _arg64 = PendingIntent.CREATOR.createFromParcel(data);
                        _arg62 = _arg64;
                    } else {
                        _arg62 = null;
                    }
                    if (data.readInt() != 0) {
                        PendingIntent _arg74 = PendingIntent.CREATOR.createFromParcel(data);
                        _arg72 = _arg74;
                    } else {
                        _arg72 = null;
                    }
                    sendDataForSubscriberWithSelfPermissions(_arg05, _arg15, _arg25, _arg34, _arg45, _arg56, _arg62, _arg72);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    String _arg16 = data.readString();
                    String _arg26 = data.readString();
                    String _arg35 = data.readString();
                    String _arg46 = data.readString();
                    if (data.readInt() != 0) {
                        PendingIntent _arg57 = PendingIntent.CREATOR.createFromParcel(data);
                        _arg5 = _arg57;
                    } else {
                        _arg5 = null;
                    }
                    _arg2 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                    PendingIntent _arg65 = _arg2;
                    boolean _arg75 = data.readInt() != 0;
                    sendTextForSubscriber(_arg06, _arg16, _arg26, _arg35, _arg46, _arg5, _arg65, _arg75);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg07 = data.readInt();
                    String _arg17 = data.readString();
                    String _arg27 = data.readString();
                    String _arg36 = data.readString();
                    String _arg47 = data.readString();
                    if (data.readInt() != 0) {
                        PendingIntent _arg58 = PendingIntent.CREATOR.createFromParcel(data);
                        _arg52 = _arg58;
                    } else {
                        _arg52 = null;
                    }
                    _arg2 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                    PendingIntent _arg66 = _arg2;
                    boolean _arg76 = data.readInt() != 0;
                    sendTextForSubscriberWithSelfPermissions(_arg07, _arg17, _arg27, _arg36, _arg47, _arg52, _arg66, _arg76);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    String _arg18 = data.readString();
                    String _arg28 = data.readString();
                    String _arg37 = data.readString();
                    String _arg48 = data.readString();
                    if (data.readInt() != 0) {
                        PendingIntent _arg59 = PendingIntent.CREATOR.createFromParcel(data);
                        _arg53 = _arg59;
                    } else {
                        _arg53 = null;
                    }
                    _arg2 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                    PendingIntent _arg67 = _arg2;
                    boolean _arg77 = data.readInt() != 0;
                    int _arg8 = data.readInt();
                    boolean _arg9 = data.readInt() != 0;
                    int _arg10 = data.readInt();
                    sendTextForSubscriberWithOptions(_arg08, _arg18, _arg28, _arg37, _arg48, _arg53, _arg67, _arg77, _arg8, _arg9, _arg10);
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg09 = data.readInt();
                    byte[] _arg19 = data.createByteArray();
                    String _arg29 = data.readString();
                    _arg2 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                    injectSmsPduForSubscriber(_arg09, _arg19, _arg29, _arg2);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg010 = data.readInt();
                    String _arg110 = data.readString();
                    String _arg210 = data.readString();
                    String _arg38 = data.readString();
                    List<String> _arg49 = data.createStringArrayList();
                    List<PendingIntent> _arg510 = data.createTypedArrayList(PendingIntent.CREATOR);
                    List<PendingIntent> _arg68 = data.createTypedArrayList(PendingIntent.CREATOR);
                    boolean _arg78 = data.readInt() != 0;
                    sendMultipartTextForSubscriber(_arg010, _arg110, _arg210, _arg38, _arg49, _arg510, _arg68, _arg78);
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg011 = data.readInt();
                    String _arg111 = data.readString();
                    String _arg211 = data.readString();
                    String _arg39 = data.readString();
                    List<String> _arg410 = data.createStringArrayList();
                    List<PendingIntent> _arg511 = data.createTypedArrayList(PendingIntent.CREATOR);
                    List<PendingIntent> _arg69 = data.createTypedArrayList(PendingIntent.CREATOR);
                    boolean _arg79 = data.readInt() != 0;
                    int _arg82 = data.readInt();
                    boolean _arg92 = data.readInt() != 0;
                    int _arg102 = data.readInt();
                    sendMultipartTextForSubscriberWithOptions(_arg011, _arg111, _arg211, _arg39, _arg410, _arg511, _arg69, _arg79, _arg82, _arg92, _arg102);
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg012 = data.readInt();
                    int _arg112 = data.readInt();
                    boolean enableCellBroadcastForSubscriber = enableCellBroadcastForSubscriber(_arg012, _arg112, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(enableCellBroadcastForSubscriber ? 1 : 0);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg013 = data.readInt();
                    int _arg113 = data.readInt();
                    boolean disableCellBroadcastForSubscriber = disableCellBroadcastForSubscriber(_arg013, _arg113, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(disableCellBroadcastForSubscriber ? 1 : 0);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg014 = data.readInt();
                    int _arg114 = data.readInt();
                    int _arg212 = data.readInt();
                    int _arg310 = data.readInt();
                    boolean enableCellBroadcastRangeForSubscriber = enableCellBroadcastRangeForSubscriber(_arg014, _arg114, _arg212, _arg310);
                    reply.writeNoException();
                    reply.writeInt(enableCellBroadcastRangeForSubscriber ? 1 : 0);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg015 = data.readInt();
                    int _arg115 = data.readInt();
                    int _arg213 = data.readInt();
                    int _arg311 = data.readInt();
                    boolean disableCellBroadcastRangeForSubscriber = disableCellBroadcastRangeForSubscriber(_arg015, _arg115, _arg213, _arg311);
                    reply.writeNoException();
                    reply.writeInt(disableCellBroadcastRangeForSubscriber ? 1 : 0);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg016 = data.readString();
                    int _result2 = getPremiumSmsPermission(_arg016);
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg017 = data.readInt();
                    String _arg116 = data.readString();
                    int _result3 = getPremiumSmsPermissionForSubscriber(_arg017, _arg116);
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg018 = data.readString();
                    int _arg117 = data.readInt();
                    setPremiumSmsPermission(_arg018, _arg117);
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg019 = data.readInt();
                    String _arg118 = data.readString();
                    setPremiumSmsPermissionForSubscriber(_arg019, _arg118, data.readInt());
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg020 = data.readInt();
                    boolean isImsSmsSupportedForSubscriber = isImsSmsSupportedForSubscriber(_arg020);
                    reply.writeNoException();
                    reply.writeInt(isImsSmsSupportedForSubscriber ? 1 : 0);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg021 = data.readInt();
                    boolean isSmsSimPickActivityNeeded = isSmsSimPickActivityNeeded(_arg021);
                    reply.writeNoException();
                    reply.writeInt(isSmsSimPickActivityNeeded ? 1 : 0);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    int _result4 = getPreferredSmsSubscription();
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg022 = data.readInt();
                    String _result5 = getImsSmsFormatForSubscriber(_arg022);
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isSMSPromptEnabled = isSMSPromptEnabled();
                    reply.writeNoException();
                    reply.writeInt(isSMSPromptEnabled ? 1 : 0);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg023 = data.readInt();
                    String _arg119 = data.readString();
                    Uri _arg214 = data.readInt() != 0 ? Uri.CREATOR.createFromParcel(data) : null;
                    String _arg312 = data.readString();
                    if (data.readInt() != 0) {
                        PendingIntent _arg411 = PendingIntent.CREATOR.createFromParcel(data);
                        _arg4 = _arg411;
                    } else {
                        _arg4 = null;
                    }
                    if (data.readInt() != 0) {
                        PendingIntent _arg512 = PendingIntent.CREATOR.createFromParcel(data);
                        _arg54 = _arg512;
                    } else {
                        _arg54 = null;
                    }
                    sendStoredText(_arg023, _arg119, _arg214, _arg312, _arg4, _arg54);
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg024 = data.readInt();
                    String _arg120 = data.readString();
                    Uri _arg215 = data.readInt() != 0 ? Uri.CREATOR.createFromParcel(data) : null;
                    String _arg313 = data.readString();
                    List<PendingIntent> _arg412 = data.createTypedArrayList(PendingIntent.CREATOR);
                    List<PendingIntent> _arg513 = data.createTypedArrayList(PendingIntent.CREATOR);
                    sendStoredMultipartText(_arg024, _arg120, _arg215, _arg313, _arg412, _arg513);
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg025 = data.readInt();
                    String _arg121 = data.readString();
                    _arg2 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                    String _result6 = createAppSpecificSmsToken(_arg025, _arg121, _arg2);
                    reply.writeNoException();
                    reply.writeString(_result6);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements ISms {
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

            @Override // com.android.internal.telephony.ISms
            public synchronized List<SmsRawData> getAllMessagesFromIccEfForSubscriber(int subId, String callingPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    List<SmsRawData> _result = _reply.createTypedArrayList(SmsRawData.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ISms
            public synchronized boolean updateMessageOnIccEfForSubscriber(int subId, String callingPkg, int messageIndex, int newStatus, byte[] pdu) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeInt(messageIndex);
                    _data.writeInt(newStatus);
                    _data.writeByteArray(pdu);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ISms
            public synchronized boolean copyMessageToIccEfForSubscriber(int subId, String callingPkg, int status, byte[] pdu, byte[] smsc) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeInt(status);
                    _data.writeByteArray(pdu);
                    _data.writeByteArray(smsc);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ISms
            public synchronized void sendDataForSubscriber(int subId, String callingPkg, String destAddr, String scAddr, int destPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeInt(destPort);
                    _data.writeByteArray(data);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deliveryIntent != null) {
                        _data.writeInt(1);
                        deliveryIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ISms
            public synchronized void sendDataForSubscriberWithSelfPermissions(int subId, String callingPkg, String destAddr, String scAddr, int destPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeInt(destPort);
                    _data.writeByteArray(data);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deliveryIntent != null) {
                        _data.writeInt(1);
                        deliveryIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ISms
            public synchronized void sendTextForSubscriber(int subId, String callingPkg, String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessageForNonDefaultSmsApp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeString(text);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deliveryIntent != null) {
                        _data.writeInt(1);
                        deliveryIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(persistMessageForNonDefaultSmsApp ? 1 : 0);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ISms
            public synchronized void sendTextForSubscriberWithSelfPermissions(int subId, String callingPkg, String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(destAddr);
                    _data.writeString(scAddr);
                    _data.writeString(text);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deliveryIntent != null) {
                        _data.writeInt(1);
                        deliveryIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(persistMessage ? 1 : 0);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ISms
            public synchronized void sendTextForSubscriberWithOptions(int subId, String callingPkg, String destAddr, String scAddr, String text, PendingIntent sentIntent, PendingIntent deliveryIntent, boolean persistMessageForNonDefaultSmsApp, int priority, boolean expectMore, int validityPeriod) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(subId);
                    try {
                        _data.writeString(callingPkg);
                        try {
                            _data.writeString(destAddr);
                            try {
                                _data.writeString(scAddr);
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
                        _data.writeString(text);
                        if (sentIntent != null) {
                            _data.writeInt(1);
                            sentIntent.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (deliveryIntent != null) {
                            _data.writeInt(1);
                            deliveryIntent.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeInt(persistMessageForNonDefaultSmsApp ? 1 : 0);
                            try {
                                _data.writeInt(priority);
                                try {
                                    _data.writeInt(expectMore ? 1 : 0);
                                    try {
                                        _data.writeInt(validityPeriod);
                                        try {
                                            this.mRemote.transact(8, _data, _reply, 0);
                                            _reply.readException();
                                            _reply.recycle();
                                            _data.recycle();
                                        } catch (Throwable th5) {
                                            th = th5;
                                            _reply.recycle();
                                            _data.recycle();
                                            throw th;
                                        }
                                    } catch (Throwable th6) {
                                        th = th6;
                                    }
                                } catch (Throwable th7) {
                                    th = th7;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            } catch (Throwable th8) {
                                th = th8;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
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
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // com.android.internal.telephony.ISms
            public synchronized void injectSmsPduForSubscriber(int subId, byte[] pdu, String format, PendingIntent receivedIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeByteArray(pdu);
                    _data.writeString(format);
                    if (receivedIntent != null) {
                        _data.writeInt(1);
                        receivedIntent.writeToParcel(_data, 0);
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

            @Override // com.android.internal.telephony.ISms
            public synchronized void sendMultipartTextForSubscriber(int subId, String callingPkg, String destinationAddress, String scAddress, List<String> parts, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, boolean persistMessageForNonDefaultSmsApp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    _data.writeString(destinationAddress);
                    _data.writeString(scAddress);
                    _data.writeStringList(parts);
                    _data.writeTypedList(sentIntents);
                    _data.writeTypedList(deliveryIntents);
                    _data.writeInt(persistMessageForNonDefaultSmsApp ? 1 : 0);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ISms
            public synchronized void sendMultipartTextForSubscriberWithOptions(int subId, String callingPkg, String destinationAddress, String scAddress, List<String> parts, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents, boolean persistMessageForNonDefaultSmsApp, int priority, boolean expectMore, int validityPeriod) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    try {
                        _data.writeString(callingPkg);
                    } catch (Throwable th) {
                        th = th;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
                try {
                    _data.writeString(destinationAddress);
                    try {
                        _data.writeString(scAddress);
                        try {
                            _data.writeStringList(parts);
                            try {
                                _data.writeTypedList(sentIntents);
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
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeTypedList(deliveryIntents);
                        try {
                            _data.writeInt(persistMessageForNonDefaultSmsApp ? 1 : 0);
                            try {
                                _data.writeInt(priority);
                                try {
                                    _data.writeInt(expectMore ? 1 : 0);
                                    try {
                                        _data.writeInt(validityPeriod);
                                        try {
                                            this.mRemote.transact(11, _data, _reply, 0);
                                            _reply.readException();
                                            _reply.recycle();
                                            _data.recycle();
                                        } catch (Throwable th6) {
                                            th = th6;
                                            _reply.recycle();
                                            _data.recycle();
                                            throw th;
                                        }
                                    } catch (Throwable th7) {
                                        th = th7;
                                    }
                                } catch (Throwable th8) {
                                    th = th8;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
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

            @Override // com.android.internal.telephony.ISms
            public synchronized boolean enableCellBroadcastForSubscriber(int subId, int messageIdentifier, int ranType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(messageIdentifier);
                    _data.writeInt(ranType);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ISms
            public synchronized boolean disableCellBroadcastForSubscriber(int subId, int messageIdentifier, int ranType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(messageIdentifier);
                    _data.writeInt(ranType);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ISms
            public synchronized boolean enableCellBroadcastRangeForSubscriber(int subId, int startMessageId, int endMessageId, int ranType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(startMessageId);
                    _data.writeInt(endMessageId);
                    _data.writeInt(ranType);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ISms
            public synchronized boolean disableCellBroadcastRangeForSubscriber(int subId, int startMessageId, int endMessageId, int ranType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(startMessageId);
                    _data.writeInt(endMessageId);
                    _data.writeInt(ranType);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ISms
            public synchronized int getPremiumSmsPermission(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ISms
            public synchronized int getPremiumSmsPermissionForSubscriber(int subId, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(packageName);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ISms
            public synchronized void setPremiumSmsPermission(String packageName, int permission) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(permission);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ISms
            public synchronized void setPremiumSmsPermissionForSubscriber(int subId, String packageName, int permission) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(packageName);
                    _data.writeInt(permission);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ISms
            public synchronized boolean isImsSmsSupportedForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ISms
            public synchronized boolean isSmsSimPickActivityNeeded(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ISms
            public synchronized int getPreferredSmsSubscription() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ISms
            public synchronized String getImsSmsFormatForSubscriber(int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ISms
            public synchronized boolean isSMSPromptEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ISms
            public synchronized void sendStoredText(int subId, String callingPkg, Uri messageUri, String scAddress, PendingIntent sentIntent, PendingIntent deliveryIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    if (messageUri != null) {
                        _data.writeInt(1);
                        messageUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(scAddress);
                    if (sentIntent != null) {
                        _data.writeInt(1);
                        sentIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (deliveryIntent != null) {
                        _data.writeInt(1);
                        deliveryIntent.writeToParcel(_data, 0);
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

            @Override // com.android.internal.telephony.ISms
            public synchronized void sendStoredMultipartText(int subId, String callingPkg, Uri messageUri, String scAddress, List<PendingIntent> sentIntents, List<PendingIntent> deliveryIntents) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    if (messageUri != null) {
                        _data.writeInt(1);
                        messageUri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(scAddress);
                    _data.writeTypedList(sentIntents);
                    _data.writeTypedList(deliveryIntents);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ISms
            public synchronized String createAppSpecificSmsToken(int subId, String callingPkg, PendingIntent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(callingPkg);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(27, _data, _reply, 0);
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
