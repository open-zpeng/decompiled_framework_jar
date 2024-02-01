package android.apex;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface IApexService extends IInterface {
    void abortActiveSession() throws RemoteException;

    void activatePackage(String str) throws RemoteException;

    void deactivatePackage(String str) throws RemoteException;

    ApexInfo getActivePackage(String str) throws RemoteException;

    ApexInfo[] getActivePackages() throws RemoteException;

    ApexInfo[] getAllPackages() throws RemoteException;

    ApexSessionInfo[] getSessions() throws RemoteException;

    ApexSessionInfo getStagedSessionInfo(int i) throws RemoteException;

    boolean markStagedSessionReady(int i) throws RemoteException;

    void markStagedSessionSuccessful(int i) throws RemoteException;

    void postinstallPackages(List<String> list) throws RemoteException;

    void preinstallPackages(List<String> list) throws RemoteException;

    void resumeRollbackIfNeeded() throws RemoteException;

    void rollbackActiveSession() throws RemoteException;

    boolean stagePackage(String str) throws RemoteException;

    boolean stagePackages(List<String> list) throws RemoteException;

    boolean submitStagedSession(int i, int[] iArr, ApexInfoList apexInfoList) throws RemoteException;

    void unstagePackages(List<String> list) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IApexService {
        @Override // android.apex.IApexService
        public boolean submitStagedSession(int session_id, int[] child_session_ids, ApexInfoList packages) throws RemoteException {
            return false;
        }

        @Override // android.apex.IApexService
        public boolean markStagedSessionReady(int session_id) throws RemoteException {
            return false;
        }

        @Override // android.apex.IApexService
        public void markStagedSessionSuccessful(int session_id) throws RemoteException {
        }

        @Override // android.apex.IApexService
        public ApexSessionInfo[] getSessions() throws RemoteException {
            return null;
        }

        @Override // android.apex.IApexService
        public ApexSessionInfo getStagedSessionInfo(int session_id) throws RemoteException {
            return null;
        }

        @Override // android.apex.IApexService
        public ApexInfo[] getActivePackages() throws RemoteException {
            return null;
        }

        @Override // android.apex.IApexService
        public ApexInfo[] getAllPackages() throws RemoteException {
            return null;
        }

        @Override // android.apex.IApexService
        public void abortActiveSession() throws RemoteException {
        }

        @Override // android.apex.IApexService
        public void unstagePackages(List<String> active_package_paths) throws RemoteException {
        }

        @Override // android.apex.IApexService
        public ApexInfo getActivePackage(String package_name) throws RemoteException {
            return null;
        }

        @Override // android.apex.IApexService
        public void activatePackage(String package_path) throws RemoteException {
        }

        @Override // android.apex.IApexService
        public void deactivatePackage(String package_path) throws RemoteException {
        }

        @Override // android.apex.IApexService
        public void preinstallPackages(List<String> package_tmp_paths) throws RemoteException {
        }

        @Override // android.apex.IApexService
        public void postinstallPackages(List<String> package_tmp_paths) throws RemoteException {
        }

        @Override // android.apex.IApexService
        public boolean stagePackage(String package_tmp_path) throws RemoteException {
            return false;
        }

        @Override // android.apex.IApexService
        public boolean stagePackages(List<String> package_tmp_paths) throws RemoteException {
            return false;
        }

        @Override // android.apex.IApexService
        public void rollbackActiveSession() throws RemoteException {
        }

        @Override // android.apex.IApexService
        public void resumeRollbackIfNeeded() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IApexService {
        private static final String DESCRIPTOR = "android.apex.IApexService";
        static final int TRANSACTION_abortActiveSession = 8;
        static final int TRANSACTION_activatePackage = 11;
        static final int TRANSACTION_deactivatePackage = 12;
        static final int TRANSACTION_getActivePackage = 10;
        static final int TRANSACTION_getActivePackages = 6;
        static final int TRANSACTION_getAllPackages = 7;
        static final int TRANSACTION_getSessions = 4;
        static final int TRANSACTION_getStagedSessionInfo = 5;
        static final int TRANSACTION_markStagedSessionReady = 2;
        static final int TRANSACTION_markStagedSessionSuccessful = 3;
        static final int TRANSACTION_postinstallPackages = 14;
        static final int TRANSACTION_preinstallPackages = 13;
        static final int TRANSACTION_resumeRollbackIfNeeded = 18;
        static final int TRANSACTION_rollbackActiveSession = 17;
        static final int TRANSACTION_stagePackage = 15;
        static final int TRANSACTION_stagePackages = 16;
        static final int TRANSACTION_submitStagedSession = 1;
        static final int TRANSACTION_unstagePackages = 9;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IApexService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IApexService)) {
                return (IApexService) iin;
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
                    int _arg0 = data.readInt();
                    int[] _arg1 = data.createIntArray();
                    ApexInfoList _arg2 = new ApexInfoList();
                    boolean submitStagedSession = submitStagedSession(_arg0, _arg1, _arg2);
                    reply.writeNoException();
                    reply.writeInt(submitStagedSession ? 1 : 0);
                    reply.writeInt(1);
                    _arg2.writeToParcel(reply, 1);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    boolean markStagedSessionReady = markStagedSessionReady(_arg02);
                    reply.writeNoException();
                    reply.writeInt(markStagedSessionReady ? 1 : 0);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    markStagedSessionSuccessful(_arg03);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    ApexSessionInfo[] _result = getSessions();
                    reply.writeNoException();
                    reply.writeTypedArray(_result, 1);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg04 = data.readInt();
                    ApexSessionInfo _result2 = getStagedSessionInfo(_arg04);
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    ApexInfo[] _result3 = getActivePackages();
                    reply.writeNoException();
                    reply.writeTypedArray(_result3, 1);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    ApexInfo[] _result4 = getAllPackages();
                    reply.writeNoException();
                    reply.writeTypedArray(_result4, 1);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    abortActiveSession();
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _arg05 = data.createStringArrayList();
                    unstagePackages(_arg05);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg06 = data.readString();
                    ApexInfo _result5 = getActivePackage(_arg06);
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    activatePackage(_arg07);
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    deactivatePackage(_arg08);
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _arg09 = data.createStringArrayList();
                    preinstallPackages(_arg09);
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _arg010 = data.createStringArrayList();
                    postinstallPackages(_arg010);
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg011 = data.readString();
                    boolean stagePackage = stagePackage(_arg011);
                    reply.writeNoException();
                    reply.writeInt(stagePackage ? 1 : 0);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _arg012 = data.createStringArrayList();
                    boolean stagePackages = stagePackages(_arg012);
                    reply.writeNoException();
                    reply.writeInt(stagePackages ? 1 : 0);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    rollbackActiveSession();
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    resumeRollbackIfNeeded();
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IApexService {
            public static IApexService sDefaultImpl;
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

            @Override // android.apex.IApexService
            public boolean submitStagedSession(int session_id, int[] child_session_ids, ApexInfoList packages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(session_id);
                    _data.writeIntArray(child_session_ids);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().submitStagedSession(session_id, child_session_ids, packages);
                    }
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    if (_reply.readInt() != 0) {
                        packages.readFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.apex.IApexService
            public boolean markStagedSessionReady(int session_id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(session_id);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().markStagedSessionReady(session_id);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.apex.IApexService
            public void markStagedSessionSuccessful(int session_id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(session_id);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().markStagedSessionSuccessful(session_id);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.apex.IApexService
            public ApexSessionInfo[] getSessions() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSessions();
                    }
                    _reply.readException();
                    ApexSessionInfo[] _result = (ApexSessionInfo[]) _reply.createTypedArray(ApexSessionInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.apex.IApexService
            public ApexSessionInfo getStagedSessionInfo(int session_id) throws RemoteException {
                ApexSessionInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(session_id);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getStagedSessionInfo(session_id);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ApexSessionInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.apex.IApexService
            public ApexInfo[] getActivePackages() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getActivePackages();
                    }
                    _reply.readException();
                    ApexInfo[] _result = (ApexInfo[]) _reply.createTypedArray(ApexInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.apex.IApexService
            public ApexInfo[] getAllPackages() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAllPackages();
                    }
                    _reply.readException();
                    ApexInfo[] _result = (ApexInfo[]) _reply.createTypedArray(ApexInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.apex.IApexService
            public void abortActiveSession() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().abortActiveSession();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.apex.IApexService
            public void unstagePackages(List<String> active_package_paths) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(active_package_paths);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unstagePackages(active_package_paths);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.apex.IApexService
            public ApexInfo getActivePackage(String package_name) throws RemoteException {
                ApexInfo _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(package_name);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getActivePackage(package_name);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ApexInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.apex.IApexService
            public void activatePackage(String package_path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(package_path);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().activatePackage(package_path);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.apex.IApexService
            public void deactivatePackage(String package_path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(package_path);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().deactivatePackage(package_path);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.apex.IApexService
            public void preinstallPackages(List<String> package_tmp_paths) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(package_tmp_paths);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().preinstallPackages(package_tmp_paths);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.apex.IApexService
            public void postinstallPackages(List<String> package_tmp_paths) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(package_tmp_paths);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().postinstallPackages(package_tmp_paths);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.apex.IApexService
            public boolean stagePackage(String package_tmp_path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(package_tmp_path);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stagePackage(package_tmp_path);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.apex.IApexService
            public boolean stagePackages(List<String> package_tmp_paths) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(package_tmp_paths);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stagePackages(package_tmp_paths);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.apex.IApexService
            public void rollbackActiveSession() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().rollbackActiveSession();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.apex.IApexService
            public void resumeRollbackIfNeeded() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().resumeRollbackIfNeeded();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IApexService impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IApexService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
