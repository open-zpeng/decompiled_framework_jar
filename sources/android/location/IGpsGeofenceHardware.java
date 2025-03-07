package android.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface IGpsGeofenceHardware extends IInterface {
    boolean addCircularHardwareGeofence(int i, double d, double d2, double d3, int i2, int i3, int i4, int i5) throws RemoteException;

    boolean isHardwareGeofenceSupported() throws RemoteException;

    boolean pauseHardwareGeofence(int i) throws RemoteException;

    boolean removeHardwareGeofence(int i) throws RemoteException;

    boolean resumeHardwareGeofence(int i, int i2) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IGpsGeofenceHardware {
        @Override // android.location.IGpsGeofenceHardware
        public boolean isHardwareGeofenceSupported() throws RemoteException {
            return false;
        }

        @Override // android.location.IGpsGeofenceHardware
        public boolean addCircularHardwareGeofence(int geofenceId, double latitude, double longitude, double radius, int lastTransition, int monitorTransition, int notificationResponsiveness, int unknownTimer) throws RemoteException {
            return false;
        }

        @Override // android.location.IGpsGeofenceHardware
        public boolean removeHardwareGeofence(int geofenceId) throws RemoteException {
            return false;
        }

        @Override // android.location.IGpsGeofenceHardware
        public boolean pauseHardwareGeofence(int geofenceId) throws RemoteException {
            return false;
        }

        @Override // android.location.IGpsGeofenceHardware
        public boolean resumeHardwareGeofence(int geofenceId, int monitorTransition) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IGpsGeofenceHardware {
        private static final String DESCRIPTOR = "android.location.IGpsGeofenceHardware";
        static final int TRANSACTION_addCircularHardwareGeofence = 2;
        static final int TRANSACTION_isHardwareGeofenceSupported = 1;
        static final int TRANSACTION_pauseHardwareGeofence = 4;
        static final int TRANSACTION_removeHardwareGeofence = 3;
        static final int TRANSACTION_resumeHardwareGeofence = 5;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IGpsGeofenceHardware asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IGpsGeofenceHardware)) {
                return (IGpsGeofenceHardware) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                if (transactionCode != 2) {
                    if (transactionCode != 3) {
                        if (transactionCode != 4) {
                            if (transactionCode == 5) {
                                return "resumeHardwareGeofence";
                            }
                            return null;
                        }
                        return "pauseHardwareGeofence";
                    }
                    return "removeHardwareGeofence";
                }
                return "addCircularHardwareGeofence";
            }
            return "isHardwareGeofenceSupported";
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1) {
                data.enforceInterface(DESCRIPTOR);
                boolean isHardwareGeofenceSupported = isHardwareGeofenceSupported();
                reply.writeNoException();
                reply.writeInt(isHardwareGeofenceSupported ? 1 : 0);
                return true;
            } else if (code == 2) {
                data.enforceInterface(DESCRIPTOR);
                int _arg0 = data.readInt();
                double _arg1 = data.readDouble();
                double _arg2 = data.readDouble();
                double _arg3 = data.readDouble();
                int _arg4 = data.readInt();
                int _arg5 = data.readInt();
                int _arg6 = data.readInt();
                int _arg7 = data.readInt();
                boolean addCircularHardwareGeofence = addCircularHardwareGeofence(_arg0, _arg1, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7);
                reply.writeNoException();
                reply.writeInt(addCircularHardwareGeofence ? 1 : 0);
                return true;
            } else if (code == 3) {
                data.enforceInterface(DESCRIPTOR);
                int _arg02 = data.readInt();
                boolean removeHardwareGeofence = removeHardwareGeofence(_arg02);
                reply.writeNoException();
                reply.writeInt(removeHardwareGeofence ? 1 : 0);
                return true;
            } else if (code == 4) {
                data.enforceInterface(DESCRIPTOR);
                int _arg03 = data.readInt();
                boolean pauseHardwareGeofence = pauseHardwareGeofence(_arg03);
                reply.writeNoException();
                reply.writeInt(pauseHardwareGeofence ? 1 : 0);
                return true;
            } else if (code != 5) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            } else {
                data.enforceInterface(DESCRIPTOR);
                int _arg04 = data.readInt();
                int _arg12 = data.readInt();
                boolean resumeHardwareGeofence = resumeHardwareGeofence(_arg04, _arg12);
                reply.writeNoException();
                reply.writeInt(resumeHardwareGeofence ? 1 : 0);
                return true;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IGpsGeofenceHardware {
            public static IGpsGeofenceHardware sDefaultImpl;
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

            @Override // android.location.IGpsGeofenceHardware
            public boolean isHardwareGeofenceSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isHardwareGeofenceSupported();
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.IGpsGeofenceHardware
            public boolean addCircularHardwareGeofence(int geofenceId, double latitude, double longitude, double radius, int lastTransition, int monitorTransition, int notificationResponsiveness, int unknownTimer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(geofenceId);
                    _data.writeDouble(latitude);
                    _data.writeDouble(longitude);
                    _data.writeDouble(radius);
                    _data.writeInt(lastTransition);
                    _data.writeInt(monitorTransition);
                    _data.writeInt(notificationResponsiveness);
                    _data.writeInt(unknownTimer);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        boolean addCircularHardwareGeofence = Stub.getDefaultImpl().addCircularHardwareGeofence(geofenceId, latitude, longitude, radius, lastTransition, monitorTransition, notificationResponsiveness, unknownTimer);
                        _reply.recycle();
                        _data.recycle();
                        return addCircularHardwareGeofence;
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    _reply.recycle();
                    _data.recycle();
                    return _status2;
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.location.IGpsGeofenceHardware
            public boolean removeHardwareGeofence(int geofenceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(geofenceId);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().removeHardwareGeofence(geofenceId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.IGpsGeofenceHardware
            public boolean pauseHardwareGeofence(int geofenceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(geofenceId);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().pauseHardwareGeofence(geofenceId);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.location.IGpsGeofenceHardware
            public boolean resumeHardwareGeofence(int geofenceId, int monitorTransition) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(geofenceId);
                    _data.writeInt(monitorTransition);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().resumeHardwareGeofence(geofenceId, monitorTransition);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IGpsGeofenceHardware impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IGpsGeofenceHardware getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
