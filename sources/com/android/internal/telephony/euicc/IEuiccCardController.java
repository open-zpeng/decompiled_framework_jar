package com.android.internal.telephony.euicc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.internal.telephony.euicc.IAuthenticateServerCallback;
import com.android.internal.telephony.euicc.ICancelSessionCallback;
import com.android.internal.telephony.euicc.IDeleteProfileCallback;
import com.android.internal.telephony.euicc.IDisableProfileCallback;
import com.android.internal.telephony.euicc.IGetAllProfilesCallback;
import com.android.internal.telephony.euicc.IGetDefaultSmdpAddressCallback;
import com.android.internal.telephony.euicc.IGetEuiccChallengeCallback;
import com.android.internal.telephony.euicc.IGetEuiccInfo1Callback;
import com.android.internal.telephony.euicc.IGetEuiccInfo2Callback;
import com.android.internal.telephony.euicc.IGetProfileCallback;
import com.android.internal.telephony.euicc.IGetRulesAuthTableCallback;
import com.android.internal.telephony.euicc.IGetSmdsAddressCallback;
import com.android.internal.telephony.euicc.IListNotificationsCallback;
import com.android.internal.telephony.euicc.ILoadBoundProfilePackageCallback;
import com.android.internal.telephony.euicc.IPrepareDownloadCallback;
import com.android.internal.telephony.euicc.IRemoveNotificationFromListCallback;
import com.android.internal.telephony.euicc.IResetMemoryCallback;
import com.android.internal.telephony.euicc.IRetrieveNotificationCallback;
import com.android.internal.telephony.euicc.IRetrieveNotificationListCallback;
import com.android.internal.telephony.euicc.ISetDefaultSmdpAddressCallback;
import com.android.internal.telephony.euicc.ISetNicknameCallback;
import com.android.internal.telephony.euicc.ISwitchToProfileCallback;
/* loaded from: classes3.dex */
public interface IEuiccCardController extends IInterface {
    /* JADX INFO: Access modifiers changed from: private */
    synchronized void authenticateServer(String str, String str2, String str3, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, IAuthenticateServerCallback iAuthenticateServerCallback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void cancelSession(String str, String str2, byte[] bArr, int i, ICancelSessionCallback iCancelSessionCallback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void deleteProfile(String str, String str2, String str3, IDeleteProfileCallback iDeleteProfileCallback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void disableProfile(String str, String str2, String str3, boolean z, IDisableProfileCallback iDisableProfileCallback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void getAllProfiles(String str, String str2, IGetAllProfilesCallback iGetAllProfilesCallback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void getDefaultSmdpAddress(String str, String str2, IGetDefaultSmdpAddressCallback iGetDefaultSmdpAddressCallback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void getEuiccChallenge(String str, String str2, IGetEuiccChallengeCallback iGetEuiccChallengeCallback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void getEuiccInfo1(String str, String str2, IGetEuiccInfo1Callback iGetEuiccInfo1Callback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void getEuiccInfo2(String str, String str2, IGetEuiccInfo2Callback iGetEuiccInfo2Callback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void getProfile(String str, String str2, String str3, IGetProfileCallback iGetProfileCallback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void getRulesAuthTable(String str, String str2, IGetRulesAuthTableCallback iGetRulesAuthTableCallback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void getSmdsAddress(String str, String str2, IGetSmdsAddressCallback iGetSmdsAddressCallback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void listNotifications(String str, String str2, int i, IListNotificationsCallback iListNotificationsCallback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void loadBoundProfilePackage(String str, String str2, byte[] bArr, ILoadBoundProfilePackageCallback iLoadBoundProfilePackageCallback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void prepareDownload(String str, String str2, byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, IPrepareDownloadCallback iPrepareDownloadCallback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void removeNotificationFromList(String str, String str2, int i, IRemoveNotificationFromListCallback iRemoveNotificationFromListCallback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void resetMemory(String str, String str2, int i, IResetMemoryCallback iResetMemoryCallback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void retrieveNotification(String str, String str2, int i, IRetrieveNotificationCallback iRetrieveNotificationCallback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void retrieveNotificationList(String str, String str2, int i, IRetrieveNotificationListCallback iRetrieveNotificationListCallback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setDefaultSmdpAddress(String str, String str2, String str3, ISetDefaultSmdpAddressCallback iSetDefaultSmdpAddressCallback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setNickname(String str, String str2, String str3, String str4, ISetNicknameCallback iSetNicknameCallback) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void switchToProfile(String str, String str2, String str3, boolean z, ISwitchToProfileCallback iSwitchToProfileCallback) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IEuiccCardController {
        public protected static final String DESCRIPTOR = "com.android.internal.telephony.euicc.IEuiccCardController";
        public private protected static final int TRANSACTION_authenticateServer = 15;
        public private protected static final int TRANSACTION_cancelSession = 18;
        public private protected static final int TRANSACTION_deleteProfile = 6;
        public private protected static final int TRANSACTION_disableProfile = 3;
        public private protected static final int TRANSACTION_getAllProfiles = 1;
        public private protected static final int TRANSACTION_getDefaultSmdpAddress = 8;
        public private protected static final int TRANSACTION_getEuiccChallenge = 12;
        public private protected static final int TRANSACTION_getEuiccInfo1 = 13;
        public private protected static final int TRANSACTION_getEuiccInfo2 = 14;
        public private protected static final int TRANSACTION_getProfile = 2;
        public private protected static final int TRANSACTION_getRulesAuthTable = 11;
        public private protected static final int TRANSACTION_getSmdsAddress = 9;
        public private protected static final int TRANSACTION_listNotifications = 19;
        public private protected static final int TRANSACTION_loadBoundProfilePackage = 17;
        public private protected static final int TRANSACTION_prepareDownload = 16;
        public private protected static final int TRANSACTION_removeNotificationFromList = 22;
        public private protected static final int TRANSACTION_resetMemory = 7;
        public private protected static final int TRANSACTION_retrieveNotification = 21;
        public private protected static final int TRANSACTION_retrieveNotificationList = 20;
        public private protected static final int TRANSACTION_setDefaultSmdpAddress = 10;
        public private protected static final int TRANSACTION_setNickname = 5;
        public private protected static final int TRANSACTION_switchToProfile = 4;

        private protected synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized IEuiccCardController asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IEuiccCardController)) {
                return (IEuiccCardController) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0 = data.readString();
                    String _arg1 = data.readString();
                    IGetAllProfilesCallback _arg2 = IGetAllProfilesCallback.Stub.asInterface(data.readStrongBinder());
                    getAllProfiles(_arg0, _arg1, _arg2);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    String _arg12 = data.readString();
                    String _arg22 = data.readString();
                    IGetProfileCallback _arg3 = IGetProfileCallback.Stub.asInterface(data.readStrongBinder());
                    getProfile(_arg02, _arg12, _arg22, _arg3);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    String _arg13 = data.readString();
                    String _arg23 = data.readString();
                    boolean _arg32 = data.readInt() != 0;
                    IDisableProfileCallback _arg4 = IDisableProfileCallback.Stub.asInterface(data.readStrongBinder());
                    disableProfile(_arg03, _arg13, _arg23, _arg32, _arg4);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    String _arg14 = data.readString();
                    String _arg24 = data.readString();
                    boolean _arg33 = data.readInt() != 0;
                    ISwitchToProfileCallback _arg42 = ISwitchToProfileCallback.Stub.asInterface(data.readStrongBinder());
                    switchToProfile(_arg04, _arg14, _arg24, _arg33, _arg42);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    String _arg15 = data.readString();
                    String _arg25 = data.readString();
                    String _arg34 = data.readString();
                    ISetNicknameCallback _arg43 = ISetNicknameCallback.Stub.asInterface(data.readStrongBinder());
                    setNickname(_arg05, _arg15, _arg25, _arg34, _arg43);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg06 = data.readString();
                    String _arg16 = data.readString();
                    String _arg26 = data.readString();
                    IDeleteProfileCallback _arg35 = IDeleteProfileCallback.Stub.asInterface(data.readStrongBinder());
                    deleteProfile(_arg06, _arg16, _arg26, _arg35);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    String _arg17 = data.readString();
                    int _arg27 = data.readInt();
                    IResetMemoryCallback _arg36 = IResetMemoryCallback.Stub.asInterface(data.readStrongBinder());
                    resetMemory(_arg07, _arg17, _arg27, _arg36);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    String _arg18 = data.readString();
                    IGetDefaultSmdpAddressCallback _arg28 = IGetDefaultSmdpAddressCallback.Stub.asInterface(data.readStrongBinder());
                    getDefaultSmdpAddress(_arg08, _arg18, _arg28);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg09 = data.readString();
                    String _arg19 = data.readString();
                    IGetSmdsAddressCallback _arg29 = IGetSmdsAddressCallback.Stub.asInterface(data.readStrongBinder());
                    getSmdsAddress(_arg09, _arg19, _arg29);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg010 = data.readString();
                    String _arg110 = data.readString();
                    String _arg210 = data.readString();
                    ISetDefaultSmdpAddressCallback _arg37 = ISetDefaultSmdpAddressCallback.Stub.asInterface(data.readStrongBinder());
                    setDefaultSmdpAddress(_arg010, _arg110, _arg210, _arg37);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg011 = data.readString();
                    String _arg111 = data.readString();
                    IGetRulesAuthTableCallback _arg211 = IGetRulesAuthTableCallback.Stub.asInterface(data.readStrongBinder());
                    getRulesAuthTable(_arg011, _arg111, _arg211);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg012 = data.readString();
                    String _arg112 = data.readString();
                    IGetEuiccChallengeCallback _arg212 = IGetEuiccChallengeCallback.Stub.asInterface(data.readStrongBinder());
                    getEuiccChallenge(_arg012, _arg112, _arg212);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg013 = data.readString();
                    String _arg113 = data.readString();
                    IGetEuiccInfo1Callback _arg213 = IGetEuiccInfo1Callback.Stub.asInterface(data.readStrongBinder());
                    getEuiccInfo1(_arg013, _arg113, _arg213);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg014 = data.readString();
                    String _arg114 = data.readString();
                    IGetEuiccInfo2Callback _arg214 = IGetEuiccInfo2Callback.Stub.asInterface(data.readStrongBinder());
                    getEuiccInfo2(_arg014, _arg114, _arg214);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg015 = data.readString();
                    String _arg115 = data.readString();
                    String _arg215 = data.readString();
                    byte[] _arg38 = data.createByteArray();
                    byte[] _arg44 = data.createByteArray();
                    byte[] _arg5 = data.createByteArray();
                    byte[] _arg6 = data.createByteArray();
                    IAuthenticateServerCallback _arg7 = IAuthenticateServerCallback.Stub.asInterface(data.readStrongBinder());
                    authenticateServer(_arg015, _arg115, _arg215, _arg38, _arg44, _arg5, _arg6, _arg7);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg016 = data.readString();
                    String _arg116 = data.readString();
                    byte[] _arg216 = data.createByteArray();
                    byte[] _arg39 = data.createByteArray();
                    byte[] _arg45 = data.createByteArray();
                    byte[] _arg52 = data.createByteArray();
                    IPrepareDownloadCallback _arg62 = IPrepareDownloadCallback.Stub.asInterface(data.readStrongBinder());
                    prepareDownload(_arg016, _arg116, _arg216, _arg39, _arg45, _arg52, _arg62);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg017 = data.readString();
                    String _arg117 = data.readString();
                    byte[] _arg217 = data.createByteArray();
                    ILoadBoundProfilePackageCallback _arg310 = ILoadBoundProfilePackageCallback.Stub.asInterface(data.readStrongBinder());
                    loadBoundProfilePackage(_arg017, _arg117, _arg217, _arg310);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg018 = data.readString();
                    String _arg118 = data.readString();
                    byte[] _arg218 = data.createByteArray();
                    int _arg311 = data.readInt();
                    ICancelSessionCallback _arg46 = ICancelSessionCallback.Stub.asInterface(data.readStrongBinder());
                    cancelSession(_arg018, _arg118, _arg218, _arg311, _arg46);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg019 = data.readString();
                    String _arg119 = data.readString();
                    int _arg219 = data.readInt();
                    IListNotificationsCallback _arg312 = IListNotificationsCallback.Stub.asInterface(data.readStrongBinder());
                    listNotifications(_arg019, _arg119, _arg219, _arg312);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg020 = data.readString();
                    String _arg120 = data.readString();
                    int _arg220 = data.readInt();
                    IRetrieveNotificationListCallback _arg313 = IRetrieveNotificationListCallback.Stub.asInterface(data.readStrongBinder());
                    retrieveNotificationList(_arg020, _arg120, _arg220, _arg313);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg021 = data.readString();
                    String _arg121 = data.readString();
                    int _arg221 = data.readInt();
                    IRetrieveNotificationCallback _arg314 = IRetrieveNotificationCallback.Stub.asInterface(data.readStrongBinder());
                    retrieveNotification(_arg021, _arg121, _arg221, _arg314);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg022 = data.readString();
                    String _arg122 = data.readString();
                    int _arg222 = data.readInt();
                    IRemoveNotificationFromListCallback _arg315 = IRemoveNotificationFromListCallback.Stub.asInterface(data.readStrongBinder());
                    removeNotificationFromList(_arg022, _arg122, _arg222, _arg315);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes3.dex */
        private static class Proxy implements IEuiccCardController {
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

            private protected synchronized void getAllProfiles(String callingPackage, String cardId, IGetAllProfilesCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void getProfile(String callingPackage, String cardId, String iccid, IGetProfileCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeString(iccid);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void disableProfile(String callingPackage, String cardId, String iccid, boolean refresh, IDisableProfileCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeString(iccid);
                    _data.writeInt(refresh ? 1 : 0);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void switchToProfile(String callingPackage, String cardId, String iccid, boolean refresh, ISwitchToProfileCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeString(iccid);
                    _data.writeInt(refresh ? 1 : 0);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void setNickname(String callingPackage, String cardId, String iccid, String nickname, ISetNicknameCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeString(iccid);
                    _data.writeString(nickname);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void deleteProfile(String callingPackage, String cardId, String iccid, IDeleteProfileCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeString(iccid);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void resetMemory(String callingPackage, String cardId, int options, IResetMemoryCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeInt(options);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void getDefaultSmdpAddress(String callingPackage, String cardId, IGetDefaultSmdpAddressCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void getSmdsAddress(String callingPackage, String cardId, IGetSmdsAddressCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void setDefaultSmdpAddress(String callingPackage, String cardId, String address, ISetDefaultSmdpAddressCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeString(address);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void getRulesAuthTable(String callingPackage, String cardId, IGetRulesAuthTableCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void getEuiccChallenge(String callingPackage, String cardId, IGetEuiccChallengeCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(12, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void getEuiccInfo1(String callingPackage, String cardId, IGetEuiccInfo1Callback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(13, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void getEuiccInfo2(String callingPackage, String cardId, IGetEuiccInfo2Callback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(14, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void authenticateServer(String callingPackage, String cardId, String matchingId, byte[] serverSigned1, byte[] serverSignature1, byte[] euiccCiPkIdToBeUsed, byte[] serverCertificatein, IAuthenticateServerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeString(matchingId);
                    _data.writeByteArray(serverSigned1);
                    _data.writeByteArray(serverSignature1);
                    _data.writeByteArray(euiccCiPkIdToBeUsed);
                    _data.writeByteArray(serverCertificatein);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(15, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void prepareDownload(String callingPackage, String cardId, byte[] hashCc, byte[] smdpSigned2, byte[] smdpSignature2, byte[] smdpCertificate, IPrepareDownloadCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeByteArray(hashCc);
                    _data.writeByteArray(smdpSigned2);
                    _data.writeByteArray(smdpSignature2);
                    _data.writeByteArray(smdpCertificate);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(16, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void loadBoundProfilePackage(String callingPackage, String cardId, byte[] boundProfilePackage, ILoadBoundProfilePackageCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeByteArray(boundProfilePackage);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(17, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void cancelSession(String callingPackage, String cardId, byte[] transactionId, int reason, ICancelSessionCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeByteArray(transactionId);
                    _data.writeInt(reason);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(18, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void listNotifications(String callingPackage, String cardId, int events, IListNotificationsCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeInt(events);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(19, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void retrieveNotificationList(String callingPackage, String cardId, int events, IRetrieveNotificationListCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeInt(events);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(20, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void retrieveNotification(String callingPackage, String cardId, int seqNumber, IRetrieveNotificationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeInt(seqNumber);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(21, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void removeNotificationFromList(String callingPackage, String cardId, int seqNumber, IRemoveNotificationFromListCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(cardId);
                    _data.writeInt(seqNumber);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(22, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
