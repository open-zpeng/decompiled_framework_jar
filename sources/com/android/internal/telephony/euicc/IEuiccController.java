package com.android.internal.telephony.euicc;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.euicc.DownloadableSubscription;
import android.telephony.euicc.EuiccInfo;
/* loaded from: classes3.dex */
public interface IEuiccController extends IInterface {
    /* JADX INFO: Access modifiers changed from: private */
    synchronized void continueOperation(Intent intent, Bundle bundle) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void deleteSubscription(int i, String str, PendingIntent pendingIntent) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void downloadSubscription(DownloadableSubscription downloadableSubscription, boolean z, String str, PendingIntent pendingIntent) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void eraseSubscriptions(PendingIntent pendingIntent) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void getDefaultDownloadableSubscriptionList(String str, PendingIntent pendingIntent) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void getDownloadableSubscriptionMetadata(DownloadableSubscription downloadableSubscription, String str, PendingIntent pendingIntent) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized String getEid() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized EuiccInfo getEuiccInfo() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized int getOtaStatus() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void retainSubscriptionsForFactoryReset(PendingIntent pendingIntent) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void switchToSubscription(int i, String str, PendingIntent pendingIntent) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void updateSubscriptionNickname(int i, String str, PendingIntent pendingIntent) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IEuiccController {
        public protected static final String DESCRIPTOR = "com.android.internal.telephony.euicc.IEuiccController";
        public private protected static final int TRANSACTION_continueOperation = 1;
        public private protected static final int TRANSACTION_deleteSubscription = 8;
        public private protected static final int TRANSACTION_downloadSubscription = 6;
        public private protected static final int TRANSACTION_eraseSubscriptions = 11;
        public private protected static final int TRANSACTION_getDefaultDownloadableSubscriptionList = 3;
        public private protected static final int TRANSACTION_getDownloadableSubscriptionMetadata = 2;
        public private protected static final int TRANSACTION_getEid = 4;
        public private protected static final int TRANSACTION_getEuiccInfo = 7;
        public private protected static final int TRANSACTION_getOtaStatus = 5;
        public private protected static final int TRANSACTION_retainSubscriptionsForFactoryReset = 12;
        public private protected static final int TRANSACTION_switchToSubscription = 9;
        public private protected static final int TRANSACTION_updateSubscriptionNickname = 10;

        private protected synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized IEuiccController asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IEuiccController)) {
                return (IEuiccController) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Intent _arg0;
            DownloadableSubscription _arg02;
            DownloadableSubscription _arg03;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    Bundle _arg1 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    continueOperation(_arg0, _arg1);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = DownloadableSubscription.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    String _arg12 = data.readString();
                    PendingIntent _arg2 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                    getDownloadableSubscriptionMetadata(_arg02, _arg12, _arg2);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    PendingIntent _arg13 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                    getDefaultDownloadableSubscriptionList(_arg04, _arg13);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    String _result = getEid();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _result2 = getOtaStatus();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = DownloadableSubscription.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    boolean _arg14 = data.readInt() != 0;
                    String _arg22 = data.readString();
                    PendingIntent _arg3 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                    downloadSubscription(_arg03, _arg14, _arg22, _arg3);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    EuiccInfo _result3 = getEuiccInfo();
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    String _arg15 = data.readString();
                    PendingIntent _arg23 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                    deleteSubscription(_arg05, _arg15, _arg23);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    String _arg16 = data.readString();
                    PendingIntent _arg24 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                    switchToSubscription(_arg06, _arg16, _arg24);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg07 = data.readInt();
                    String _arg17 = data.readString();
                    PendingIntent _arg25 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                    updateSubscriptionNickname(_arg07, _arg17, _arg25);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    PendingIntent _arg08 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                    eraseSubscriptions(_arg08);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    PendingIntent _arg09 = data.readInt() != 0 ? PendingIntent.CREATOR.createFromParcel(data) : null;
                    retainSubscriptionsForFactoryReset(_arg09);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes3.dex */
        private static class Proxy implements IEuiccController {
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

            private protected synchronized void continueOperation(Intent resolutionIntent, Bundle resolutionExtras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (resolutionIntent != null) {
                        _data.writeInt(1);
                        resolutionIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (resolutionExtras != null) {
                        _data.writeInt(1);
                        resolutionExtras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void getDownloadableSubscriptionMetadata(DownloadableSubscription subscription, String callingPackage, PendingIntent callbackIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (subscription != null) {
                        _data.writeInt(1);
                        subscription.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    if (callbackIntent != null) {
                        _data.writeInt(1);
                        callbackIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void getDefaultDownloadableSubscriptionList(String callingPackage, PendingIntent callbackIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    if (callbackIntent != null) {
                        _data.writeInt(1);
                        callbackIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized String getEid() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized int getOtaStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void downloadSubscription(DownloadableSubscription subscription, boolean switchAfterDownload, String callingPackage, PendingIntent callbackIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (subscription != null) {
                        _data.writeInt(1);
                        subscription.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(switchAfterDownload ? 1 : 0);
                    _data.writeString(callingPackage);
                    if (callbackIntent != null) {
                        _data.writeInt(1);
                        callbackIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized EuiccInfo getEuiccInfo() throws RemoteException {
                EuiccInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = EuiccInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected synchronized void deleteSubscription(int subscriptionId, String callingPackage, PendingIntent callbackIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subscriptionId);
                    _data.writeString(callingPackage);
                    if (callbackIntent != null) {
                        _data.writeInt(1);
                        callbackIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void switchToSubscription(int subscriptionId, String callingPackage, PendingIntent callbackIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subscriptionId);
                    _data.writeString(callingPackage);
                    if (callbackIntent != null) {
                        _data.writeInt(1);
                        callbackIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void updateSubscriptionNickname(int subscriptionId, String nickname, PendingIntent callbackIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subscriptionId);
                    _data.writeString(nickname);
                    if (callbackIntent != null) {
                        _data.writeInt(1);
                        callbackIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void eraseSubscriptions(PendingIntent callbackIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (callbackIntent != null) {
                        _data.writeInt(1);
                        callbackIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void retainSubscriptionsForFactoryReset(PendingIntent callbackIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (callbackIntent != null) {
                        _data.writeInt(1);
                        callbackIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(12, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
