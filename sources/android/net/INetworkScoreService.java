package android.net;

import android.net.INetworkScoreCache;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
/* loaded from: classes2.dex */
public interface INetworkScoreService extends IInterface {
    synchronized boolean clearScores() throws RemoteException;

    synchronized void disableScoring() throws RemoteException;

    synchronized NetworkScorerAppData getActiveScorer() throws RemoteException;

    synchronized String getActiveScorerPackage() throws RemoteException;

    synchronized List<NetworkScorerAppData> getAllValidScorers() throws RemoteException;

    synchronized boolean isCallerActiveScorer(int i) throws RemoteException;

    synchronized void registerNetworkScoreCache(int i, INetworkScoreCache iNetworkScoreCache, int i2) throws RemoteException;

    synchronized boolean requestScores(NetworkKey[] networkKeyArr) throws RemoteException;

    synchronized boolean setActiveScorer(String str) throws RemoteException;

    synchronized void unregisterNetworkScoreCache(int i, INetworkScoreCache iNetworkScoreCache) throws RemoteException;

    synchronized boolean updateScores(ScoredNetwork[] scoredNetworkArr) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements INetworkScoreService {
        private static final String DESCRIPTOR = "android.net.INetworkScoreService";
        static final int TRANSACTION_clearScores = 2;
        static final int TRANSACTION_disableScoring = 4;
        static final int TRANSACTION_getActiveScorer = 10;
        static final int TRANSACTION_getActiveScorerPackage = 9;
        static final int TRANSACTION_getAllValidScorers = 11;
        static final int TRANSACTION_isCallerActiveScorer = 8;
        static final int TRANSACTION_registerNetworkScoreCache = 5;
        static final int TRANSACTION_requestScores = 7;
        static final int TRANSACTION_setActiveScorer = 3;
        static final int TRANSACTION_unregisterNetworkScoreCache = 6;
        static final int TRANSACTION_updateScores = 1;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static INetworkScoreService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof INetworkScoreService)) {
                return (INetworkScoreService) iin;
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
                    ScoredNetwork[] _arg0 = (ScoredNetwork[]) data.createTypedArray(ScoredNetwork.CREATOR);
                    boolean updateScores = updateScores(_arg0);
                    reply.writeNoException();
                    reply.writeInt(updateScores ? 1 : 0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    boolean clearScores = clearScores();
                    reply.writeNoException();
                    reply.writeInt(clearScores ? 1 : 0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    boolean activeScorer = setActiveScorer(_arg02);
                    reply.writeNoException();
                    reply.writeInt(activeScorer ? 1 : 0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    disableScoring();
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    INetworkScoreCache _arg1 = INetworkScoreCache.Stub.asInterface(data.readStrongBinder());
                    int _arg2 = data.readInt();
                    registerNetworkScoreCache(_arg03, _arg1, _arg2);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    INetworkScoreCache _arg12 = INetworkScoreCache.Stub.asInterface(data.readStrongBinder());
                    unregisterNetworkScoreCache(_arg04, _arg12);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    NetworkKey[] _arg05 = (NetworkKey[]) data.createTypedArray(NetworkKey.CREATOR);
                    boolean requestScores = requestScores(_arg05);
                    reply.writeNoException();
                    reply.writeInt(requestScores ? 1 : 0);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    boolean isCallerActiveScorer = isCallerActiveScorer(_arg06);
                    reply.writeNoException();
                    reply.writeInt(isCallerActiveScorer ? 1 : 0);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _result = getActiveScorerPackage();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    NetworkScorerAppData _result2 = getActiveScorer();
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    List<NetworkScorerAppData> _result3 = getAllValidScorers();
                    reply.writeNoException();
                    reply.writeTypedList(_result3);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements INetworkScoreService {
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

            @Override // android.net.INetworkScoreService
            public synchronized boolean updateScores(ScoredNetwork[] networks) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(networks, 0);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkScoreService
            public synchronized boolean clearScores() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkScoreService
            public synchronized boolean setActiveScorer(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkScoreService
            public synchronized void disableScoring() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkScoreService
            public synchronized void registerNetworkScoreCache(int networkType, INetworkScoreCache scoreCache, int filterType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    _data.writeStrongBinder(scoreCache != null ? scoreCache.asBinder() : null);
                    _data.writeInt(filterType);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkScoreService
            public synchronized void unregisterNetworkScoreCache(int networkType, INetworkScoreCache scoreCache) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(networkType);
                    _data.writeStrongBinder(scoreCache != null ? scoreCache.asBinder() : null);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkScoreService
            public synchronized boolean requestScores(NetworkKey[] networks) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedArray(networks, 0);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkScoreService
            public synchronized boolean isCallerActiveScorer(int callingUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(callingUid);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkScoreService
            public synchronized String getActiveScorerPackage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkScoreService
            public synchronized NetworkScorerAppData getActiveScorer() throws RemoteException {
                NetworkScorerAppData _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = NetworkScorerAppData.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.net.INetworkScoreService
            public synchronized List<NetworkScorerAppData> getAllValidScorers() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    List<NetworkScorerAppData> _result = _reply.createTypedArrayList(NetworkScorerAppData.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
