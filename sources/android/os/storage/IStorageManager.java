package android.os.storage;

import android.content.pm.IPackageMoveObserver;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IVoldTaskListener;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.storage.IObbActionListener;
import android.os.storage.IStorageEventListener;
import android.os.storage.IStorageShutdownObserver;
import com.android.internal.os.AppFuseMount;
/* loaded from: classes2.dex */
public interface IStorageManager extends IInterface {
    synchronized void abortIdleMaintenance() throws RemoteException;

    synchronized void addUserKeyAuth(int i, int i2, byte[] bArr, byte[] bArr2) throws RemoteException;

    synchronized void allocateBytes(String str, long j, int i, String str2) throws RemoteException;

    synchronized void benchmark(String str, IVoldTaskListener iVoldTaskListener) throws RemoteException;

    synchronized int changeEncryptionPassword(int i, String str) throws RemoteException;

    synchronized void clearPassword() throws RemoteException;

    synchronized void createUserKey(int i, int i2, boolean z) throws RemoteException;

    synchronized int decryptStorage(String str) throws RemoteException;

    synchronized void destroyUserKey(int i) throws RemoteException;

    synchronized void destroyUserStorage(String str, int i, int i2) throws RemoteException;

    synchronized int encryptStorage(int i, String str) throws RemoteException;

    synchronized void fixateNewestUserKeyAuth(int i) throws RemoteException;

    synchronized void forgetAllVolumes() throws RemoteException;

    synchronized void forgetVolume(String str) throws RemoteException;

    synchronized void format(String str) throws RemoteException;

    synchronized void fstrim(int i, IVoldTaskListener iVoldTaskListener) throws RemoteException;

    synchronized long getAllocatableBytes(String str, int i, String str2) throws RemoteException;

    synchronized long getCacheQuotaBytes(String str, int i) throws RemoteException;

    synchronized long getCacheSizeBytes(String str, int i) throws RemoteException;

    synchronized DiskInfo[] getDisks() throws RemoteException;

    synchronized int getEncryptionState() throws RemoteException;

    synchronized String getField(String str) throws RemoteException;

    synchronized String getMountedObbPath(String str) throws RemoteException;

    synchronized String getPassword() throws RemoteException;

    synchronized int getPasswordType() throws RemoteException;

    synchronized String getPrimaryStorageUuid() throws RemoteException;

    synchronized StorageVolume[] getVolumeList(int i, String str, int i2) throws RemoteException;

    synchronized VolumeRecord[] getVolumeRecords(int i) throws RemoteException;

    synchronized VolumeInfo[] getVolumes(int i) throws RemoteException;

    synchronized boolean isConvertibleToFBE() throws RemoteException;

    synchronized boolean isObbMounted(String str) throws RemoteException;

    synchronized boolean isUserKeyUnlocked(int i) throws RemoteException;

    synchronized long lastMaintenance() throws RemoteException;

    synchronized void lockUserKey(int i) throws RemoteException;

    synchronized void mkdirs(String str, String str2) throws RemoteException;

    synchronized void mount(String str) throws RemoteException;

    synchronized void mountObb(String str, String str2, String str3, IObbActionListener iObbActionListener, int i) throws RemoteException;

    synchronized AppFuseMount mountProxyFileDescriptorBridge() throws RemoteException;

    synchronized ParcelFileDescriptor openProxyFileDescriptor(int i, int i2, int i3) throws RemoteException;

    synchronized void partitionMixed(String str, int i) throws RemoteException;

    synchronized void partitionPrivate(String str) throws RemoteException;

    synchronized void partitionPublic(String str) throws RemoteException;

    synchronized void prepareUserStorage(String str, int i, int i2, int i3) throws RemoteException;

    synchronized void registerListener(IStorageEventListener iStorageEventListener) throws RemoteException;

    synchronized void runIdleMaintenance() throws RemoteException;

    synchronized void runMaintenance() throws RemoteException;

    synchronized void setDebugFlags(int i, int i2) throws RemoteException;

    synchronized void setField(String str, String str2) throws RemoteException;

    synchronized void setPrimaryStorageUuid(String str, IPackageMoveObserver iPackageMoveObserver) throws RemoteException;

    synchronized void setVolumeNickname(String str, String str2) throws RemoteException;

    synchronized void setVolumeUserFlags(String str, int i, int i2) throws RemoteException;

    synchronized void shutdown(IStorageShutdownObserver iStorageShutdownObserver) throws RemoteException;

    synchronized void unlockUserKey(int i, int i2, byte[] bArr, byte[] bArr2) throws RemoteException;

    synchronized void unmount(String str) throws RemoteException;

    synchronized void unmountObb(String str, boolean z, IObbActionListener iObbActionListener, int i) throws RemoteException;

    synchronized void unregisterListener(IStorageEventListener iStorageEventListener) throws RemoteException;

    synchronized int verifyEncryptionPassword(String str) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IStorageManager {
        private static final String DESCRIPTOR = "android.os.storage.IStorageManager";
        static final int TRANSACTION_abortIdleMaintenance = 81;
        static final int TRANSACTION_addUserKeyAuth = 71;
        static final int TRANSACTION_allocateBytes = 79;
        static final int TRANSACTION_benchmark = 60;
        static final int TRANSACTION_changeEncryptionPassword = 29;
        static final int TRANSACTION_clearPassword = 38;
        static final int TRANSACTION_createUserKey = 62;
        static final int TRANSACTION_decryptStorage = 27;
        static final int TRANSACTION_destroyUserKey = 63;
        static final int TRANSACTION_destroyUserStorage = 68;
        static final int TRANSACTION_encryptStorage = 28;
        static final int TRANSACTION_fixateNewestUserKeyAuth = 72;
        static final int TRANSACTION_forgetAllVolumes = 57;
        static final int TRANSACTION_forgetVolume = 56;
        static final int TRANSACTION_format = 50;
        static final int TRANSACTION_fstrim = 73;
        static final int TRANSACTION_getAllocatableBytes = 78;
        static final int TRANSACTION_getCacheQuotaBytes = 76;
        static final int TRANSACTION_getCacheSizeBytes = 77;
        static final int TRANSACTION_getDisks = 45;
        static final int TRANSACTION_getEncryptionState = 32;
        static final int TRANSACTION_getField = 40;
        static final int TRANSACTION_getMountedObbPath = 25;
        static final int TRANSACTION_getPassword = 37;
        static final int TRANSACTION_getPasswordType = 36;
        static final int TRANSACTION_getPrimaryStorageUuid = 58;
        static final int TRANSACTION_getVolumeList = 30;
        static final int TRANSACTION_getVolumeRecords = 47;
        static final int TRANSACTION_getVolumes = 46;
        static final int TRANSACTION_isConvertibleToFBE = 69;
        static final int TRANSACTION_isObbMounted = 24;
        static final int TRANSACTION_isUserKeyUnlocked = 66;
        static final int TRANSACTION_lastMaintenance = 42;
        static final int TRANSACTION_lockUserKey = 65;
        static final int TRANSACTION_mkdirs = 35;
        static final int TRANSACTION_mount = 48;
        static final int TRANSACTION_mountObb = 22;
        static final int TRANSACTION_mountProxyFileDescriptorBridge = 74;
        static final int TRANSACTION_openProxyFileDescriptor = 75;
        static final int TRANSACTION_partitionMixed = 53;
        static final int TRANSACTION_partitionPrivate = 52;
        static final int TRANSACTION_partitionPublic = 51;
        static final int TRANSACTION_prepareUserStorage = 67;
        static final int TRANSACTION_registerListener = 1;
        static final int TRANSACTION_runIdleMaintenance = 80;
        static final int TRANSACTION_runMaintenance = 43;
        static final int TRANSACTION_setDebugFlags = 61;
        static final int TRANSACTION_setField = 39;
        static final int TRANSACTION_setPrimaryStorageUuid = 59;
        static final int TRANSACTION_setVolumeNickname = 54;
        static final int TRANSACTION_setVolumeUserFlags = 55;
        static final int TRANSACTION_shutdown = 20;
        static final int TRANSACTION_unlockUserKey = 64;
        static final int TRANSACTION_unmount = 49;
        static final int TRANSACTION_unmountObb = 23;
        static final int TRANSACTION_unregisterListener = 2;
        static final int TRANSACTION_verifyEncryptionPassword = 33;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static IStorageManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IStorageManager)) {
                return (IStorageManager) iin;
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
            if (code == 20) {
                data.enforceInterface(DESCRIPTOR);
                IStorageShutdownObserver _arg0 = IStorageShutdownObserver.Stub.asInterface(data.readStrongBinder());
                shutdown(_arg0);
                reply.writeNoException();
                return true;
            } else if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            } else {
                switch (code) {
                    case 1:
                        data.enforceInterface(DESCRIPTOR);
                        IStorageEventListener _arg02 = IStorageEventListener.Stub.asInterface(data.readStrongBinder());
                        registerListener(_arg02);
                        reply.writeNoException();
                        return true;
                    case 2:
                        data.enforceInterface(DESCRIPTOR);
                        IStorageEventListener _arg03 = IStorageEventListener.Stub.asInterface(data.readStrongBinder());
                        unregisterListener(_arg03);
                        reply.writeNoException();
                        return true;
                    default:
                        switch (code) {
                            case 22:
                                data.enforceInterface(DESCRIPTOR);
                                String _arg04 = data.readString();
                                String _arg1 = data.readString();
                                String _arg22 = data.readString();
                                IObbActionListener _arg3 = IObbActionListener.Stub.asInterface(data.readStrongBinder());
                                int _arg4 = data.readInt();
                                mountObb(_arg04, _arg1, _arg22, _arg3, _arg4);
                                reply.writeNoException();
                                return true;
                            case 23:
                                data.enforceInterface(DESCRIPTOR);
                                String _arg05 = data.readString();
                                _arg2 = data.readInt() != 0;
                                IObbActionListener _arg23 = IObbActionListener.Stub.asInterface(data.readStrongBinder());
                                int _arg32 = data.readInt();
                                unmountObb(_arg05, _arg2, _arg23, _arg32);
                                reply.writeNoException();
                                return true;
                            case 24:
                                data.enforceInterface(DESCRIPTOR);
                                String _arg06 = data.readString();
                                boolean isObbMounted = isObbMounted(_arg06);
                                reply.writeNoException();
                                reply.writeInt(isObbMounted ? 1 : 0);
                                return true;
                            case 25:
                                data.enforceInterface(DESCRIPTOR);
                                String _arg07 = data.readString();
                                String _result = getMountedObbPath(_arg07);
                                reply.writeNoException();
                                reply.writeString(_result);
                                return true;
                            default:
                                switch (code) {
                                    case 27:
                                        data.enforceInterface(DESCRIPTOR);
                                        String _arg08 = data.readString();
                                        int _result2 = decryptStorage(_arg08);
                                        reply.writeNoException();
                                        reply.writeInt(_result2);
                                        return true;
                                    case 28:
                                        data.enforceInterface(DESCRIPTOR);
                                        int _arg09 = data.readInt();
                                        String _arg12 = data.readString();
                                        int _result3 = encryptStorage(_arg09, _arg12);
                                        reply.writeNoException();
                                        reply.writeInt(_result3);
                                        return true;
                                    case 29:
                                        data.enforceInterface(DESCRIPTOR);
                                        int _arg010 = data.readInt();
                                        String _arg13 = data.readString();
                                        int _result4 = changeEncryptionPassword(_arg010, _arg13);
                                        reply.writeNoException();
                                        reply.writeInt(_result4);
                                        return true;
                                    case 30:
                                        data.enforceInterface(DESCRIPTOR);
                                        int _arg011 = data.readInt();
                                        String _arg14 = data.readString();
                                        StorageVolume[] _result5 = getVolumeList(_arg011, _arg14, data.readInt());
                                        reply.writeNoException();
                                        reply.writeTypedArray(_result5, 1);
                                        return true;
                                    default:
                                        switch (code) {
                                            case 32:
                                                data.enforceInterface(DESCRIPTOR);
                                                int _result6 = getEncryptionState();
                                                reply.writeNoException();
                                                reply.writeInt(_result6);
                                                return true;
                                            case 33:
                                                data.enforceInterface(DESCRIPTOR);
                                                String _arg012 = data.readString();
                                                int _result7 = verifyEncryptionPassword(_arg012);
                                                reply.writeNoException();
                                                reply.writeInt(_result7);
                                                return true;
                                            default:
                                                switch (code) {
                                                    case 35:
                                                        data.enforceInterface(DESCRIPTOR);
                                                        String _arg013 = data.readString();
                                                        String _arg15 = data.readString();
                                                        mkdirs(_arg013, _arg15);
                                                        reply.writeNoException();
                                                        return true;
                                                    case 36:
                                                        data.enforceInterface(DESCRIPTOR);
                                                        int _result8 = getPasswordType();
                                                        reply.writeNoException();
                                                        reply.writeInt(_result8);
                                                        return true;
                                                    case 37:
                                                        data.enforceInterface(DESCRIPTOR);
                                                        String _result9 = getPassword();
                                                        reply.writeNoException();
                                                        reply.writeString(_result9);
                                                        return true;
                                                    case 38:
                                                        data.enforceInterface(DESCRIPTOR);
                                                        clearPassword();
                                                        return true;
                                                    case 39:
                                                        data.enforceInterface(DESCRIPTOR);
                                                        String _arg014 = data.readString();
                                                        String _arg16 = data.readString();
                                                        setField(_arg014, _arg16);
                                                        return true;
                                                    case 40:
                                                        data.enforceInterface(DESCRIPTOR);
                                                        String _arg015 = data.readString();
                                                        String _result10 = getField(_arg015);
                                                        reply.writeNoException();
                                                        reply.writeString(_result10);
                                                        return true;
                                                    default:
                                                        switch (code) {
                                                            case 42:
                                                                data.enforceInterface(DESCRIPTOR);
                                                                long _result11 = lastMaintenance();
                                                                reply.writeNoException();
                                                                reply.writeLong(_result11);
                                                                return true;
                                                            case 43:
                                                                data.enforceInterface(DESCRIPTOR);
                                                                runMaintenance();
                                                                reply.writeNoException();
                                                                return true;
                                                            default:
                                                                switch (code) {
                                                                    case 45:
                                                                        data.enforceInterface(DESCRIPTOR);
                                                                        DiskInfo[] _result12 = getDisks();
                                                                        reply.writeNoException();
                                                                        reply.writeTypedArray(_result12, 1);
                                                                        return true;
                                                                    case 46:
                                                                        data.enforceInterface(DESCRIPTOR);
                                                                        int _arg016 = data.readInt();
                                                                        VolumeInfo[] _result13 = getVolumes(_arg016);
                                                                        reply.writeNoException();
                                                                        reply.writeTypedArray(_result13, 1);
                                                                        return true;
                                                                    case 47:
                                                                        data.enforceInterface(DESCRIPTOR);
                                                                        int _arg017 = data.readInt();
                                                                        VolumeRecord[] _result14 = getVolumeRecords(_arg017);
                                                                        reply.writeNoException();
                                                                        reply.writeTypedArray(_result14, 1);
                                                                        return true;
                                                                    case 48:
                                                                        data.enforceInterface(DESCRIPTOR);
                                                                        String _arg018 = data.readString();
                                                                        mount(_arg018);
                                                                        reply.writeNoException();
                                                                        return true;
                                                                    case 49:
                                                                        data.enforceInterface(DESCRIPTOR);
                                                                        String _arg019 = data.readString();
                                                                        unmount(_arg019);
                                                                        reply.writeNoException();
                                                                        return true;
                                                                    case 50:
                                                                        data.enforceInterface(DESCRIPTOR);
                                                                        String _arg020 = data.readString();
                                                                        format(_arg020);
                                                                        reply.writeNoException();
                                                                        return true;
                                                                    case 51:
                                                                        data.enforceInterface(DESCRIPTOR);
                                                                        String _arg021 = data.readString();
                                                                        partitionPublic(_arg021);
                                                                        reply.writeNoException();
                                                                        return true;
                                                                    case 52:
                                                                        data.enforceInterface(DESCRIPTOR);
                                                                        String _arg022 = data.readString();
                                                                        partitionPrivate(_arg022);
                                                                        reply.writeNoException();
                                                                        return true;
                                                                    case 53:
                                                                        data.enforceInterface(DESCRIPTOR);
                                                                        String _arg023 = data.readString();
                                                                        int _arg17 = data.readInt();
                                                                        partitionMixed(_arg023, _arg17);
                                                                        reply.writeNoException();
                                                                        return true;
                                                                    case 54:
                                                                        data.enforceInterface(DESCRIPTOR);
                                                                        String _arg024 = data.readString();
                                                                        String _arg18 = data.readString();
                                                                        setVolumeNickname(_arg024, _arg18);
                                                                        reply.writeNoException();
                                                                        return true;
                                                                    case 55:
                                                                        data.enforceInterface(DESCRIPTOR);
                                                                        String _arg025 = data.readString();
                                                                        int _arg19 = data.readInt();
                                                                        setVolumeUserFlags(_arg025, _arg19, data.readInt());
                                                                        reply.writeNoException();
                                                                        return true;
                                                                    case 56:
                                                                        data.enforceInterface(DESCRIPTOR);
                                                                        String _arg026 = data.readString();
                                                                        forgetVolume(_arg026);
                                                                        reply.writeNoException();
                                                                        return true;
                                                                    case 57:
                                                                        data.enforceInterface(DESCRIPTOR);
                                                                        forgetAllVolumes();
                                                                        reply.writeNoException();
                                                                        return true;
                                                                    case 58:
                                                                        data.enforceInterface(DESCRIPTOR);
                                                                        String _result15 = getPrimaryStorageUuid();
                                                                        reply.writeNoException();
                                                                        reply.writeString(_result15);
                                                                        return true;
                                                                    case 59:
                                                                        data.enforceInterface(DESCRIPTOR);
                                                                        String _arg027 = data.readString();
                                                                        IPackageMoveObserver _arg110 = IPackageMoveObserver.Stub.asInterface(data.readStrongBinder());
                                                                        setPrimaryStorageUuid(_arg027, _arg110);
                                                                        reply.writeNoException();
                                                                        return true;
                                                                    case 60:
                                                                        data.enforceInterface(DESCRIPTOR);
                                                                        String _arg028 = data.readString();
                                                                        IVoldTaskListener _arg111 = IVoldTaskListener.Stub.asInterface(data.readStrongBinder());
                                                                        benchmark(_arg028, _arg111);
                                                                        reply.writeNoException();
                                                                        return true;
                                                                    case 61:
                                                                        data.enforceInterface(DESCRIPTOR);
                                                                        int _arg029 = data.readInt();
                                                                        int _arg112 = data.readInt();
                                                                        setDebugFlags(_arg029, _arg112);
                                                                        reply.writeNoException();
                                                                        return true;
                                                                    case 62:
                                                                        data.enforceInterface(DESCRIPTOR);
                                                                        int _arg030 = data.readInt();
                                                                        int _arg113 = data.readInt();
                                                                        _arg2 = data.readInt() != 0;
                                                                        createUserKey(_arg030, _arg113, _arg2);
                                                                        reply.writeNoException();
                                                                        return true;
                                                                    case 63:
                                                                        data.enforceInterface(DESCRIPTOR);
                                                                        int _arg031 = data.readInt();
                                                                        destroyUserKey(_arg031);
                                                                        reply.writeNoException();
                                                                        return true;
                                                                    case 64:
                                                                        data.enforceInterface(DESCRIPTOR);
                                                                        int _arg032 = data.readInt();
                                                                        int _arg114 = data.readInt();
                                                                        byte[] _arg24 = data.createByteArray();
                                                                        byte[] _arg33 = data.createByteArray();
                                                                        unlockUserKey(_arg032, _arg114, _arg24, _arg33);
                                                                        reply.writeNoException();
                                                                        return true;
                                                                    case 65:
                                                                        data.enforceInterface(DESCRIPTOR);
                                                                        int _arg033 = data.readInt();
                                                                        lockUserKey(_arg033);
                                                                        reply.writeNoException();
                                                                        return true;
                                                                    case 66:
                                                                        data.enforceInterface(DESCRIPTOR);
                                                                        int _arg034 = data.readInt();
                                                                        boolean isUserKeyUnlocked = isUserKeyUnlocked(_arg034);
                                                                        reply.writeNoException();
                                                                        reply.writeInt(isUserKeyUnlocked ? 1 : 0);
                                                                        return true;
                                                                    case 67:
                                                                        data.enforceInterface(DESCRIPTOR);
                                                                        String _arg035 = data.readString();
                                                                        int _arg115 = data.readInt();
                                                                        int _arg25 = data.readInt();
                                                                        int _arg34 = data.readInt();
                                                                        prepareUserStorage(_arg035, _arg115, _arg25, _arg34);
                                                                        reply.writeNoException();
                                                                        return true;
                                                                    case 68:
                                                                        data.enforceInterface(DESCRIPTOR);
                                                                        String _arg036 = data.readString();
                                                                        int _arg116 = data.readInt();
                                                                        destroyUserStorage(_arg036, _arg116, data.readInt());
                                                                        reply.writeNoException();
                                                                        return true;
                                                                    case 69:
                                                                        data.enforceInterface(DESCRIPTOR);
                                                                        boolean isConvertibleToFBE = isConvertibleToFBE();
                                                                        reply.writeNoException();
                                                                        reply.writeInt(isConvertibleToFBE ? 1 : 0);
                                                                        return true;
                                                                    default:
                                                                        switch (code) {
                                                                            case 71:
                                                                                data.enforceInterface(DESCRIPTOR);
                                                                                int _arg037 = data.readInt();
                                                                                int _arg117 = data.readInt();
                                                                                byte[] _arg26 = data.createByteArray();
                                                                                byte[] _arg35 = data.createByteArray();
                                                                                addUserKeyAuth(_arg037, _arg117, _arg26, _arg35);
                                                                                reply.writeNoException();
                                                                                return true;
                                                                            case 72:
                                                                                data.enforceInterface(DESCRIPTOR);
                                                                                int _arg038 = data.readInt();
                                                                                fixateNewestUserKeyAuth(_arg038);
                                                                                reply.writeNoException();
                                                                                return true;
                                                                            case 73:
                                                                                data.enforceInterface(DESCRIPTOR);
                                                                                int _arg039 = data.readInt();
                                                                                IVoldTaskListener _arg118 = IVoldTaskListener.Stub.asInterface(data.readStrongBinder());
                                                                                fstrim(_arg039, _arg118);
                                                                                reply.writeNoException();
                                                                                return true;
                                                                            case 74:
                                                                                data.enforceInterface(DESCRIPTOR);
                                                                                AppFuseMount _result16 = mountProxyFileDescriptorBridge();
                                                                                reply.writeNoException();
                                                                                if (_result16 != null) {
                                                                                    reply.writeInt(1);
                                                                                    _result16.writeToParcel(reply, 1);
                                                                                } else {
                                                                                    reply.writeInt(0);
                                                                                }
                                                                                return true;
                                                                            case 75:
                                                                                data.enforceInterface(DESCRIPTOR);
                                                                                int _arg040 = data.readInt();
                                                                                int _arg119 = data.readInt();
                                                                                ParcelFileDescriptor _result17 = openProxyFileDescriptor(_arg040, _arg119, data.readInt());
                                                                                reply.writeNoException();
                                                                                if (_result17 != null) {
                                                                                    reply.writeInt(1);
                                                                                    _result17.writeToParcel(reply, 1);
                                                                                } else {
                                                                                    reply.writeInt(0);
                                                                                }
                                                                                return true;
                                                                            case 76:
                                                                                data.enforceInterface(DESCRIPTOR);
                                                                                String _arg041 = data.readString();
                                                                                int _arg120 = data.readInt();
                                                                                long _result18 = getCacheQuotaBytes(_arg041, _arg120);
                                                                                reply.writeNoException();
                                                                                reply.writeLong(_result18);
                                                                                return true;
                                                                            case 77:
                                                                                data.enforceInterface(DESCRIPTOR);
                                                                                String _arg042 = data.readString();
                                                                                int _arg121 = data.readInt();
                                                                                long _result19 = getCacheSizeBytes(_arg042, _arg121);
                                                                                reply.writeNoException();
                                                                                reply.writeLong(_result19);
                                                                                return true;
                                                                            case 78:
                                                                                data.enforceInterface(DESCRIPTOR);
                                                                                String _arg043 = data.readString();
                                                                                int _arg122 = data.readInt();
                                                                                long _result20 = getAllocatableBytes(_arg043, _arg122, data.readString());
                                                                                reply.writeNoException();
                                                                                reply.writeLong(_result20);
                                                                                return true;
                                                                            case 79:
                                                                                data.enforceInterface(DESCRIPTOR);
                                                                                String _arg044 = data.readString();
                                                                                long _arg123 = data.readLong();
                                                                                int _arg27 = data.readInt();
                                                                                String _arg36 = data.readString();
                                                                                allocateBytes(_arg044, _arg123, _arg27, _arg36);
                                                                                reply.writeNoException();
                                                                                return true;
                                                                            case 80:
                                                                                data.enforceInterface(DESCRIPTOR);
                                                                                runIdleMaintenance();
                                                                                reply.writeNoException();
                                                                                return true;
                                                                            case 81:
                                                                                data.enforceInterface(DESCRIPTOR);
                                                                                abortIdleMaintenance();
                                                                                reply.writeNoException();
                                                                                return true;
                                                                            default:
                                                                                return super.onTransact(code, data, reply, flags);
                                                                        }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IStorageManager {
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

            @Override // android.os.storage.IStorageManager
            public synchronized void registerListener(IStorageEventListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void unregisterListener(IStorageEventListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void shutdown(IStorageShutdownObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void mountObb(String rawPath, String canonicalPath, String key, IObbActionListener token, int nonce) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(rawPath);
                    _data.writeString(canonicalPath);
                    _data.writeString(key);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeInt(nonce);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void unmountObb(String rawPath, boolean force, IObbActionListener token, int nonce) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(rawPath);
                    _data.writeInt(force ? 1 : 0);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeInt(nonce);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized boolean isObbMounted(String rawPath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(rawPath);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized String getMountedObbPath(String rawPath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(rawPath);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized int decryptStorage(String password) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(password);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized int encryptStorage(int type, String password) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(password);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized int changeEncryptionPassword(int type, String password) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(password);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized StorageVolume[] getVolumeList(int uid, String packageName, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    StorageVolume[] _result = (StorageVolume[]) _reply.createTypedArray(StorageVolume.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized int getEncryptionState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized int verifyEncryptionPassword(String password) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(password);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void mkdirs(String callingPkg, String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeString(path);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized int getPasswordType() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized String getPassword() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void clearPassword() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(38, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void setField(String field, String contents) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(field);
                    _data.writeString(contents);
                    this.mRemote.transact(39, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized String getField(String field) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(field);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized long lastMaintenance() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void runMaintenance() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized DiskInfo[] getDisks() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                    DiskInfo[] _result = (DiskInfo[]) _reply.createTypedArray(DiskInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized VolumeInfo[] getVolumes(int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                    VolumeInfo[] _result = (VolumeInfo[]) _reply.createTypedArray(VolumeInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized VolumeRecord[] getVolumeRecords(int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                    VolumeRecord[] _result = (VolumeRecord[]) _reply.createTypedArray(VolumeRecord.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void mount(String volId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void unmount(String volId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void format(String volId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void partitionPublic(String diskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(diskId);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void partitionPrivate(String diskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(diskId);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void partitionMixed(String diskId, int ratio) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(diskId);
                    _data.writeInt(ratio);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void setVolumeNickname(String fsUuid, String nickname) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(fsUuid);
                    _data.writeString(nickname);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void setVolumeUserFlags(String fsUuid, int flags, int mask) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(fsUuid);
                    _data.writeInt(flags);
                    _data.writeInt(mask);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void forgetVolume(String fsUuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(fsUuid);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void forgetAllVolumes() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized String getPrimaryStorageUuid() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(58, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void setPrimaryStorageUuid(String volumeUuid, IPackageMoveObserver callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(59, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void benchmark(String volId, IVoldTaskListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void setDebugFlags(int flags, int mask) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    _data.writeInt(mask);
                    this.mRemote.transact(61, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void createUserKey(int userId, int serialNumber, boolean ephemeral) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(serialNumber);
                    _data.writeInt(ephemeral ? 1 : 0);
                    this.mRemote.transact(62, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void destroyUserKey(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(63, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void unlockUserKey(int userId, int serialNumber, byte[] token, byte[] secret) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(serialNumber);
                    _data.writeByteArray(token);
                    _data.writeByteArray(secret);
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void lockUserKey(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(65, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized boolean isUserKeyUnlocked(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(66, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void prepareUserStorage(String volumeUuid, int userId, int serialNumber, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeInt(userId);
                    _data.writeInt(serialNumber);
                    _data.writeInt(flags);
                    this.mRemote.transact(67, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void destroyUserStorage(String volumeUuid, int userId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    this.mRemote.transact(68, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized boolean isConvertibleToFBE() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(69, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void addUserKeyAuth(int userId, int serialNumber, byte[] token, byte[] secret) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(serialNumber);
                    _data.writeByteArray(token);
                    _data.writeByteArray(secret);
                    this.mRemote.transact(71, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void fixateNewestUserKeyAuth(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(72, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void fstrim(int flags, IVoldTaskListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(73, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized AppFuseMount mountProxyFileDescriptorBridge() throws RemoteException {
                AppFuseMount _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(74, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = AppFuseMount.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized ParcelFileDescriptor openProxyFileDescriptor(int mountPointId, int fileId, int mode) throws RemoteException {
                ParcelFileDescriptor _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mountPointId);
                    _data.writeInt(fileId);
                    _data.writeInt(mode);
                    this.mRemote.transact(75, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized long getCacheQuotaBytes(String volumeUuid, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeInt(uid);
                    this.mRemote.transact(76, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized long getCacheSizeBytes(String volumeUuid, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeInt(uid);
                    this.mRemote.transact(77, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized long getAllocatableBytes(String volumeUuid, int flags, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeInt(flags);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(78, _data, _reply, 0);
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void allocateBytes(String volumeUuid, long bytes, int flags, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeLong(bytes);
                    _data.writeInt(flags);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(79, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void runIdleMaintenance() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(80, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.storage.IStorageManager
            public synchronized void abortIdleMaintenance() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(81, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
