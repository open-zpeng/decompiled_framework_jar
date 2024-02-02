package com.xiaopeng.aftersales;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.xiaopeng.aftersales.IAlertListener;
import com.xiaopeng.aftersales.IAuthModeListener;
import com.xiaopeng.aftersales.IEncryptShListener;
import com.xiaopeng.aftersales.ILogicActionListener;
import com.xiaopeng.aftersales.ILogicTreeUpgrader;
import com.xiaopeng.aftersales.IRepairModeListener;
import com.xiaopeng.aftersales.IShellCmdListener;
/* loaded from: classes3.dex */
public interface IAfterSalesManager extends IInterface {
    void disableAuthMode() throws RemoteException;

    void disableRepairMode() throws RemoteException;

    void enableAuthMode(String str, long j) throws RemoteException;

    void enableRepairMode() throws RemoteException;

    void enableRepairModeWithKey(String str) throws RemoteException;

    void enableRepairModeWithKeyId(String str) throws RemoteException;

    void executeEncryptSh(String str, boolean z) throws RemoteException;

    void executeShellCmd(int i, String str, boolean z) throws RemoteException;

    void executeShellCmdWithLimitLine(int i, String str, int i2, String str2, boolean z) throws RemoteException;

    long getAuthEndTime() throws RemoteException;

    boolean getAuthMode() throws RemoteException;

    String getAuthPass() throws RemoteException;

    boolean getRepairMode() throws RemoteException;

    String getRepairModeDisableTime() throws RemoteException;

    String getRepairModeEnableTime() throws RemoteException;

    String getRepairModeKeyId() throws RemoteException;

    String getSpeedLimitDisableTime() throws RemoteException;

    String getSpeedLimitEnableTime() throws RemoteException;

    boolean getSpeedLimitMode() throws RemoteException;

    void recordDiagnosisError(int i, int i2, long j, String str, boolean z) throws RemoteException;

    void recordLogicAction(String str, String str2, String str3, String str4, String str5, String str6, String str7) throws RemoteException;

    void recordRepairmodeAction(String str, String str2) throws RemoteException;

    void recordSpeedLimitOff() throws RemoteException;

    void recordSpeedLimitOn() throws RemoteException;

    void registerAlertListener(IAlertListener iAlertListener) throws RemoteException;

    void registerAuthModeListener(IAuthModeListener iAuthModeListener) throws RemoteException;

    void registerEncryptShListener(IEncryptShListener iEncryptShListener) throws RemoteException;

    void registerLogicActionListener(ILogicActionListener iLogicActionListener) throws RemoteException;

    void registerLogicTreeUpgrader(ILogicTreeUpgrader iLogicTreeUpgrader) throws RemoteException;

    void registerRepairModeListener(IRepairModeListener iRepairModeListener) throws RemoteException;

    void registerShellCmdListener(IShellCmdListener iShellCmdListener) throws RemoteException;

    void requestUpgradeLogicTree(String str) throws RemoteException;

    void requestUploadLogicAction() throws RemoteException;

    void unregisterAlertListener(IAlertListener iAlertListener) throws RemoteException;

    void unregisterAuthModeListener(IAuthModeListener iAuthModeListener) throws RemoteException;

    void unregisterEncryptShListener(IEncryptShListener iEncryptShListener) throws RemoteException;

    void unregisterLogicActionListener(ILogicActionListener iLogicActionListener) throws RemoteException;

    void unregisterLogicTreeUpgrader(ILogicTreeUpgrader iLogicTreeUpgrader) throws RemoteException;

    void unregisterRepairModeListener(IRepairModeListener iRepairModeListener) throws RemoteException;

    void unregisterShellCmdListener(IShellCmdListener iShellCmdListener) throws RemoteException;

    void updateDiagnosisUploadStatus(int i, boolean z, int i2, long j, String str) throws RemoteException;

    void updateLogicActionUploadStatus(boolean z, String str, String str2, String str3, String str4, String str5, String str6, String str7) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IAfterSalesManager {
        private static final String DESCRIPTOR = "com.xiaopeng.aftersales.IAfterSalesManager";
        static final int TRANSACTION_disableAuthMode = 37;
        static final int TRANSACTION_disableRepairMode = 16;
        static final int TRANSACTION_enableAuthMode = 36;
        static final int TRANSACTION_enableRepairMode = 13;
        static final int TRANSACTION_enableRepairModeWithKey = 14;
        static final int TRANSACTION_enableRepairModeWithKeyId = 15;
        static final int TRANSACTION_executeEncryptSh = 33;
        static final int TRANSACTION_executeShellCmd = 29;
        static final int TRANSACTION_executeShellCmdWithLimitLine = 30;
        static final int TRANSACTION_getAuthEndTime = 40;
        static final int TRANSACTION_getAuthMode = 38;
        static final int TRANSACTION_getAuthPass = 39;
        static final int TRANSACTION_getRepairMode = 17;
        static final int TRANSACTION_getRepairModeDisableTime = 19;
        static final int TRANSACTION_getRepairModeEnableTime = 18;
        static final int TRANSACTION_getRepairModeKeyId = 23;
        static final int TRANSACTION_getSpeedLimitDisableTime = 22;
        static final int TRANSACTION_getSpeedLimitEnableTime = 21;
        static final int TRANSACTION_getSpeedLimitMode = 20;
        static final int TRANSACTION_recordDiagnosisError = 1;
        static final int TRANSACTION_recordLogicAction = 5;
        static final int TRANSACTION_recordRepairmodeAction = 28;
        static final int TRANSACTION_recordSpeedLimitOff = 25;
        static final int TRANSACTION_recordSpeedLimitOn = 24;
        static final int TRANSACTION_registerAlertListener = 3;
        static final int TRANSACTION_registerAuthModeListener = 41;
        static final int TRANSACTION_registerEncryptShListener = 34;
        static final int TRANSACTION_registerLogicActionListener = 9;
        static final int TRANSACTION_registerLogicTreeUpgrader = 11;
        static final int TRANSACTION_registerRepairModeListener = 26;
        static final int TRANSACTION_registerShellCmdListener = 31;
        static final int TRANSACTION_requestUpgradeLogicTree = 8;
        static final int TRANSACTION_requestUploadLogicAction = 7;
        static final int TRANSACTION_unregisterAlertListener = 4;
        static final int TRANSACTION_unregisterAuthModeListener = 42;
        static final int TRANSACTION_unregisterEncryptShListener = 35;
        static final int TRANSACTION_unregisterLogicActionListener = 10;
        static final int TRANSACTION_unregisterLogicTreeUpgrader = 12;
        static final int TRANSACTION_unregisterRepairModeListener = 27;
        static final int TRANSACTION_unregisterShellCmdListener = 32;
        static final int TRANSACTION_updateDiagnosisUploadStatus = 2;
        static final int TRANSACTION_updateLogicActionUploadStatus = 6;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAfterSalesManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IAfterSalesManager)) {
                return (IAfterSalesManager) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg1;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0 = data.readInt();
                    int _arg12 = data.readInt();
                    long _arg2 = data.readLong();
                    String _arg3 = data.readString();
                    boolean _arg4 = data.readInt() != 0;
                    recordDiagnosisError(_arg0, _arg12, _arg2, _arg3, _arg4);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    boolean _arg13 = data.readInt() != 0;
                    int _arg22 = data.readInt();
                    long _arg32 = data.readLong();
                    String _arg42 = data.readString();
                    updateDiagnosisUploadStatus(_arg02, _arg13, _arg22, _arg32, _arg42);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IAlertListener _arg03 = IAlertListener.Stub.asInterface(data.readStrongBinder());
                    registerAlertListener(_arg03);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    IAlertListener _arg04 = IAlertListener.Stub.asInterface(data.readStrongBinder());
                    unregisterAlertListener(_arg04);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    String _arg14 = data.readString();
                    String _arg23 = data.readString();
                    String _arg33 = data.readString();
                    String _arg43 = data.readString();
                    String _arg5 = data.readString();
                    String _arg6 = data.readString();
                    recordLogicAction(_arg05, _arg14, _arg23, _arg33, _arg43, _arg5, _arg6);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg06 = data.readInt() != 0;
                    String _arg15 = data.readString();
                    String _arg24 = data.readString();
                    String _arg34 = data.readString();
                    String _arg44 = data.readString();
                    String _arg52 = data.readString();
                    String _arg62 = data.readString();
                    String _arg7 = data.readString();
                    updateLogicActionUploadStatus(_arg06, _arg15, _arg24, _arg34, _arg44, _arg52, _arg62, _arg7);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    requestUploadLogicAction();
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    requestUpgradeLogicTree(_arg07);
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    ILogicActionListener _arg08 = ILogicActionListener.Stub.asInterface(data.readStrongBinder());
                    registerLogicActionListener(_arg08);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    ILogicActionListener _arg09 = ILogicActionListener.Stub.asInterface(data.readStrongBinder());
                    unregisterLogicActionListener(_arg09);
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    ILogicTreeUpgrader _arg010 = ILogicTreeUpgrader.Stub.asInterface(data.readStrongBinder());
                    registerLogicTreeUpgrader(_arg010);
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    ILogicTreeUpgrader _arg011 = ILogicTreeUpgrader.Stub.asInterface(data.readStrongBinder());
                    unregisterLogicTreeUpgrader(_arg011);
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    enableRepairMode();
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg012 = data.readString();
                    enableRepairModeWithKey(_arg012);
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg013 = data.readString();
                    enableRepairModeWithKeyId(_arg013);
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    disableRepairMode();
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    boolean repairMode = getRepairMode();
                    reply.writeNoException();
                    reply.writeInt(repairMode ? 1 : 0);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    String _result = getRepairModeEnableTime();
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    String _result2 = getRepairModeDisableTime();
                    reply.writeNoException();
                    reply.writeString(_result2);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    boolean speedLimitMode = getSpeedLimitMode();
                    reply.writeNoException();
                    reply.writeInt(speedLimitMode ? 1 : 0);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    String _result3 = getSpeedLimitEnableTime();
                    reply.writeNoException();
                    reply.writeString(_result3);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    String _result4 = getSpeedLimitDisableTime();
                    reply.writeNoException();
                    reply.writeString(_result4);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    String _result5 = getRepairModeKeyId();
                    reply.writeNoException();
                    reply.writeString(_result5);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    recordSpeedLimitOn();
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    recordSpeedLimitOff();
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    IRepairModeListener _arg014 = IRepairModeListener.Stub.asInterface(data.readStrongBinder());
                    registerRepairModeListener(_arg014);
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    IRepairModeListener _arg015 = IRepairModeListener.Stub.asInterface(data.readStrongBinder());
                    unregisterRepairModeListener(_arg015);
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg016 = data.readString();
                    recordRepairmodeAction(_arg016, data.readString());
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg017 = data.readInt();
                    String _arg16 = data.readString();
                    _arg1 = data.readInt() != 0;
                    executeShellCmd(_arg017, _arg16, _arg1);
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg018 = data.readInt();
                    String _arg17 = data.readString();
                    int _arg25 = data.readInt();
                    String _arg35 = data.readString();
                    boolean _arg45 = data.readInt() != 0;
                    executeShellCmdWithLimitLine(_arg018, _arg17, _arg25, _arg35, _arg45);
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    IShellCmdListener _arg019 = IShellCmdListener.Stub.asInterface(data.readStrongBinder());
                    registerShellCmdListener(_arg019);
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    IShellCmdListener _arg020 = IShellCmdListener.Stub.asInterface(data.readStrongBinder());
                    unregisterShellCmdListener(_arg020);
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg021 = data.readString();
                    _arg1 = data.readInt() != 0;
                    executeEncryptSh(_arg021, _arg1);
                    reply.writeNoException();
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    IEncryptShListener _arg022 = IEncryptShListener.Stub.asInterface(data.readStrongBinder());
                    registerEncryptShListener(_arg022);
                    reply.writeNoException();
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    IEncryptShListener _arg023 = IEncryptShListener.Stub.asInterface(data.readStrongBinder());
                    unregisterEncryptShListener(_arg023);
                    reply.writeNoException();
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg024 = data.readString();
                    enableAuthMode(_arg024, data.readLong());
                    reply.writeNoException();
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    disableAuthMode();
                    reply.writeNoException();
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    boolean authMode = getAuthMode();
                    reply.writeNoException();
                    reply.writeInt(authMode ? 1 : 0);
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    String _result6 = getAuthPass();
                    reply.writeNoException();
                    reply.writeString(_result6);
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    long _result7 = getAuthEndTime();
                    reply.writeNoException();
                    reply.writeLong(_result7);
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    IAuthModeListener _arg025 = IAuthModeListener.Stub.asInterface(data.readStrongBinder());
                    registerAuthModeListener(_arg025);
                    reply.writeNoException();
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    IAuthModeListener _arg026 = IAuthModeListener.Stub.asInterface(data.readStrongBinder());
                    unregisterAuthModeListener(_arg026);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes3.dex */
        private static class Proxy implements IAfterSalesManager {
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

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void recordDiagnosisError(int module, int errorCode, long millis, String errorMsg, boolean alert) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(module);
                    _data.writeInt(errorCode);
                    _data.writeLong(millis);
                    _data.writeString(errorMsg);
                    _data.writeInt(alert ? 1 : 0);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void updateDiagnosisUploadStatus(int module, boolean status, int errorCode, long millis, String errorMsg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(module);
                    _data.writeInt(status ? 1 : 0);
                    _data.writeInt(errorCode);
                    _data.writeLong(millis);
                    _data.writeString(errorMsg);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void registerAlertListener(IAlertListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void unregisterAlertListener(IAlertListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void recordLogicAction(String issueName, String conclusion, String startTime, String endTime, String logicactionTime, String logicactionEntry, String logictreeVer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(issueName);
                    _data.writeString(conclusion);
                    _data.writeString(startTime);
                    _data.writeString(endTime);
                    _data.writeString(logicactionTime);
                    _data.writeString(logicactionEntry);
                    _data.writeString(logictreeVer);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void updateLogicActionUploadStatus(boolean status, String issueName, String conclusion, String startTime, String endTime, String logicactionTime, String logicactionEntry, String logictreeVer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status ? 1 : 0);
                    _data.writeString(issueName);
                    _data.writeString(conclusion);
                    _data.writeString(startTime);
                    _data.writeString(endTime);
                    _data.writeString(logicactionTime);
                    _data.writeString(logicactionEntry);
                    _data.writeString(logictreeVer);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void requestUploadLogicAction() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void requestUpgradeLogicTree(String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void registerLogicActionListener(ILogicActionListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void unregisterLogicActionListener(ILogicActionListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void registerLogicTreeUpgrader(ILogicTreeUpgrader listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void unregisterLogicTreeUpgrader(ILogicTreeUpgrader listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void enableRepairMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void enableRepairModeWithKey(String keyPath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(keyPath);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void enableRepairModeWithKeyId(String keyId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(keyId);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void disableRepairMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public boolean getRepairMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public String getRepairModeEnableTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public String getRepairModeDisableTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public boolean getSpeedLimitMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public String getSpeedLimitEnableTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public String getSpeedLimitDisableTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public String getRepairModeKeyId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void recordSpeedLimitOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void recordSpeedLimitOff() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void registerRepairModeListener(IRepairModeListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void unregisterRepairModeListener(IRepairModeListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void recordRepairmodeAction(String action, String result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(action);
                    _data.writeString(result);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void executeShellCmd(int cmdtype, String param, boolean isCloudCmd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cmdtype);
                    _data.writeString(param);
                    _data.writeInt(isCloudCmd ? 1 : 0);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void executeShellCmdWithLimitLine(int cmdtype, String param, int limitLine, String quitcmd, boolean isCloudCmd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cmdtype);
                    _data.writeString(param);
                    _data.writeInt(limitLine);
                    _data.writeString(quitcmd);
                    _data.writeInt(isCloudCmd ? 1 : 0);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void registerShellCmdListener(IShellCmdListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void unregisterShellCmdListener(IShellCmdListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void executeEncryptSh(String path, boolean isCloudCmd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    _data.writeInt(isCloudCmd ? 1 : 0);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void registerEncryptShListener(IEncryptShListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void unregisterEncryptShListener(IEncryptShListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void enableAuthMode(String value, long time) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(value);
                    _data.writeLong(time);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void disableAuthMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public boolean getAuthMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public String getAuthPass() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public long getAuthEndTime() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void registerAuthModeListener(IAuthModeListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.xiaopeng.aftersales.IAfterSalesManager
            public void unregisterAuthModeListener(IAuthModeListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
