package com.android.ims.internal;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.ims.ImsCallForwardInfo;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsSsData;
import android.telephony.ims.ImsSsInfo;
import com.android.ims.internal.IImsUt;
/* loaded from: classes3.dex */
public interface IImsUtListener extends IInterface {
    synchronized void onSupplementaryServiceIndication(ImsSsData imsSsData) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void utConfigurationCallBarringQueried(IImsUt iImsUt, int i, ImsSsInfo[] imsSsInfoArr) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void utConfigurationCallForwardQueried(IImsUt iImsUt, int i, ImsCallForwardInfo[] imsCallForwardInfoArr) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void utConfigurationCallWaitingQueried(IImsUt iImsUt, int i, ImsSsInfo[] imsSsInfoArr) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void utConfigurationQueried(IImsUt iImsUt, int i, Bundle bundle) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void utConfigurationQueryFailed(IImsUt iImsUt, int i, ImsReasonInfo imsReasonInfo) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void utConfigurationUpdateFailed(IImsUt iImsUt, int i, ImsReasonInfo imsReasonInfo) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void utConfigurationUpdated(IImsUt iImsUt, int i) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IImsUtListener {
        private static final String DESCRIPTOR = "com.android.ims.internal.IImsUtListener";
        static final int TRANSACTION_onSupplementaryServiceIndication = 8;
        static final int TRANSACTION_utConfigurationCallBarringQueried = 5;
        static final int TRANSACTION_utConfigurationCallForwardQueried = 6;
        static final int TRANSACTION_utConfigurationCallWaitingQueried = 7;
        static final int TRANSACTION_utConfigurationQueried = 3;
        static final int TRANSACTION_utConfigurationQueryFailed = 4;
        static final int TRANSACTION_utConfigurationUpdateFailed = 2;
        static final int TRANSACTION_utConfigurationUpdated = 1;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IImsUtListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IImsUtListener)) {
                return (IImsUtListener) iin;
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
                    IImsUt _arg0 = IImsUt.Stub.asInterface(data.readStrongBinder());
                    int _arg1 = data.readInt();
                    utConfigurationUpdated(_arg0, _arg1);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IImsUt _arg02 = IImsUt.Stub.asInterface(data.readStrongBinder());
                    int _arg12 = data.readInt();
                    ImsReasonInfo _arg2 = data.readInt() != 0 ? ImsReasonInfo.CREATOR.createFromParcel(data) : null;
                    utConfigurationUpdateFailed(_arg02, _arg12, _arg2);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IImsUt _arg03 = IImsUt.Stub.asInterface(data.readStrongBinder());
                    int _arg13 = data.readInt();
                    Bundle _arg22 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    utConfigurationQueried(_arg03, _arg13, _arg22);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    IImsUt _arg04 = IImsUt.Stub.asInterface(data.readStrongBinder());
                    int _arg14 = data.readInt();
                    ImsReasonInfo _arg23 = data.readInt() != 0 ? ImsReasonInfo.CREATOR.createFromParcel(data) : null;
                    utConfigurationQueryFailed(_arg04, _arg14, _arg23);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    IImsUt _arg05 = IImsUt.Stub.asInterface(data.readStrongBinder());
                    int _arg15 = data.readInt();
                    ImsSsInfo[] _arg24 = (ImsSsInfo[]) data.createTypedArray(ImsSsInfo.CREATOR);
                    utConfigurationCallBarringQueried(_arg05, _arg15, _arg24);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    IImsUt _arg06 = IImsUt.Stub.asInterface(data.readStrongBinder());
                    int _arg16 = data.readInt();
                    ImsCallForwardInfo[] _arg25 = (ImsCallForwardInfo[]) data.createTypedArray(ImsCallForwardInfo.CREATOR);
                    utConfigurationCallForwardQueried(_arg06, _arg16, _arg25);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    IImsUt _arg07 = IImsUt.Stub.asInterface(data.readStrongBinder());
                    int _arg17 = data.readInt();
                    ImsSsInfo[] _arg26 = (ImsSsInfo[]) data.createTypedArray(ImsSsInfo.CREATOR);
                    utConfigurationCallWaitingQueried(_arg07, _arg17, _arg26);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    ImsSsData _arg08 = data.readInt() != 0 ? ImsSsData.CREATOR.createFromParcel(data) : null;
                    onSupplementaryServiceIndication(_arg08);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes3.dex */
        private static class Proxy implements IImsUtListener {
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

            public synchronized void utConfigurationUpdated(IImsUt ut, int id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(ut != null ? ut.asBinder() : null);
                    _data.writeInt(id);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void utConfigurationUpdateFailed(IImsUt ut, int id, ImsReasonInfo error) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(ut != null ? ut.asBinder() : null);
                    _data.writeInt(id);
                    if (error != null) {
                        _data.writeInt(1);
                        error.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void utConfigurationQueried(IImsUt ut, int id, Bundle ssInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(ut != null ? ut.asBinder() : null);
                    _data.writeInt(id);
                    if (ssInfo != null) {
                        _data.writeInt(1);
                        ssInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void utConfigurationQueryFailed(IImsUt ut, int id, ImsReasonInfo error) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(ut != null ? ut.asBinder() : null);
                    _data.writeInt(id);
                    if (error != null) {
                        _data.writeInt(1);
                        error.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void utConfigurationCallBarringQueried(IImsUt ut, int id, ImsSsInfo[] cbInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(ut != null ? ut.asBinder() : null);
                    _data.writeInt(id);
                    _data.writeTypedArray(cbInfo, 0);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void utConfigurationCallForwardQueried(IImsUt ut, int id, ImsCallForwardInfo[] cfInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(ut != null ? ut.asBinder() : null);
                    _data.writeInt(id);
                    _data.writeTypedArray(cfInfo, 0);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void utConfigurationCallWaitingQueried(IImsUt ut, int id, ImsSsInfo[] cwInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(ut != null ? ut.asBinder() : null);
                    _data.writeInt(id);
                    _data.writeTypedArray(cwInfo, 0);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.ims.internal.IImsUtListener
            public synchronized void onSupplementaryServiceIndication(ImsSsData ssData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (ssData != null) {
                        _data.writeInt(1);
                        ssData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
