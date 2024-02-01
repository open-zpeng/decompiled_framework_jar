package com.android.internal.textservice;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.textservice.SpellCheckerInfo;
import android.view.textservice.SpellCheckerSubtype;
import com.android.internal.textservice.ISpellCheckerSessionListener;
import com.android.internal.textservice.ITextServicesSessionListener;
/* loaded from: classes3.dex */
public interface ITextServicesManager extends IInterface {
    synchronized void finishSpellCheckerService(ISpellCheckerSessionListener iSpellCheckerSessionListener) throws RemoteException;

    synchronized SpellCheckerInfo getCurrentSpellChecker(String str) throws RemoteException;

    synchronized SpellCheckerSubtype getCurrentSpellCheckerSubtype(String str, boolean z) throws RemoteException;

    synchronized SpellCheckerInfo[] getEnabledSpellCheckers() throws RemoteException;

    synchronized void getSpellCheckerService(String str, String str2, ITextServicesSessionListener iTextServicesSessionListener, ISpellCheckerSessionListener iSpellCheckerSessionListener, Bundle bundle) throws RemoteException;

    synchronized boolean isSpellCheckerEnabled() throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements ITextServicesManager {
        private static final String DESCRIPTOR = "com.android.internal.textservice.ITextServicesManager";
        static final int TRANSACTION_finishSpellCheckerService = 4;
        static final int TRANSACTION_getCurrentSpellChecker = 1;
        static final int TRANSACTION_getCurrentSpellCheckerSubtype = 2;
        static final int TRANSACTION_getEnabledSpellCheckers = 6;
        static final int TRANSACTION_getSpellCheckerService = 3;
        static final int TRANSACTION_isSpellCheckerEnabled = 5;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized ITextServicesManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ITextServicesManager)) {
                return (ITextServicesManager) iin;
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
                    SpellCheckerInfo _result = getCurrentSpellChecker(_arg0);
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
                    String _arg02 = data.readString();
                    boolean _arg1 = data.readInt() != 0;
                    SpellCheckerSubtype _result2 = getCurrentSpellCheckerSubtype(_arg02, _arg1);
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    String _arg12 = data.readString();
                    ITextServicesSessionListener _arg2 = ITextServicesSessionListener.Stub.asInterface(data.readStrongBinder());
                    ISpellCheckerSessionListener _arg3 = ISpellCheckerSessionListener.Stub.asInterface(data.readStrongBinder());
                    Bundle _arg4 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    getSpellCheckerService(_arg03, _arg12, _arg2, _arg3, _arg4);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    ISpellCheckerSessionListener _arg04 = ISpellCheckerSessionListener.Stub.asInterface(data.readStrongBinder());
                    finishSpellCheckerService(_arg04);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    boolean isSpellCheckerEnabled = isSpellCheckerEnabled();
                    reply.writeNoException();
                    reply.writeInt(isSpellCheckerEnabled ? 1 : 0);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    SpellCheckerInfo[] _result3 = getEnabledSpellCheckers();
                    reply.writeNoException();
                    reply.writeTypedArray(_result3, 1);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes3.dex */
        private static class Proxy implements ITextServicesManager {
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

            @Override // com.android.internal.textservice.ITextServicesManager
            public synchronized SpellCheckerInfo getCurrentSpellChecker(String locale) throws RemoteException {
                SpellCheckerInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(locale);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = SpellCheckerInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.textservice.ITextServicesManager
            public synchronized SpellCheckerSubtype getCurrentSpellCheckerSubtype(String locale, boolean allowImplicitlySelectedSubtype) throws RemoteException {
                SpellCheckerSubtype _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(locale);
                    _data.writeInt(allowImplicitlySelectedSubtype ? 1 : 0);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = SpellCheckerSubtype.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.textservice.ITextServicesManager
            public synchronized void getSpellCheckerService(String sciId, String locale, ITextServicesSessionListener tsListener, ISpellCheckerSessionListener scListener, Bundle bundle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(sciId);
                    _data.writeString(locale);
                    _data.writeStrongBinder(tsListener != null ? tsListener.asBinder() : null);
                    _data.writeStrongBinder(scListener != null ? scListener.asBinder() : null);
                    if (bundle != null) {
                        _data.writeInt(1);
                        bundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.textservice.ITextServicesManager
            public synchronized void finishSpellCheckerService(ISpellCheckerSessionListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.textservice.ITextServicesManager
            public synchronized boolean isSpellCheckerEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.textservice.ITextServicesManager
            public synchronized SpellCheckerInfo[] getEnabledSpellCheckers() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    SpellCheckerInfo[] _result = (SpellCheckerInfo[]) _reply.createTypedArray(SpellCheckerInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
