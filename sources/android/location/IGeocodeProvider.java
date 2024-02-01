package android.location;

import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public interface IGeocodeProvider extends IInterface {
    @UnsupportedAppUsage
    String getFromLocation(double d, double d2, int i, GeocoderParams geocoderParams, List<Address> list) throws RemoteException;

    @UnsupportedAppUsage
    String getFromLocationName(String str, double d, double d2, double d3, double d4, int i, GeocoderParams geocoderParams, List<Address> list) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IGeocodeProvider {
        @Override // android.location.IGeocodeProvider
        public String getFromLocation(double latitude, double longitude, int maxResults, GeocoderParams params, List<Address> addrs) throws RemoteException {
            return null;
        }

        @Override // android.location.IGeocodeProvider
        public String getFromLocationName(String locationName, double lowerLeftLatitude, double lowerLeftLongitude, double upperRightLatitude, double upperRightLongitude, int maxResults, GeocoderParams params, List<Address> addrs) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IGeocodeProvider {
        private static final String DESCRIPTOR = "android.location.IGeocodeProvider";
        static final int TRANSACTION_getFromLocation = 1;
        static final int TRANSACTION_getFromLocationName = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IGeocodeProvider asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IGeocodeProvider)) {
                return (IGeocodeProvider) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                if (transactionCode == 2) {
                    return "getFromLocationName";
                }
                return null;
            }
            return "getFromLocation";
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            GeocoderParams _arg3;
            GeocoderParams _arg6;
            if (code != 1) {
                if (code != 2) {
                    if (code == 1598968902) {
                        reply.writeString(DESCRIPTOR);
                        return true;
                    }
                    return super.onTransact(code, data, reply, flags);
                }
                data.enforceInterface(DESCRIPTOR);
                String _arg0 = data.readString();
                double _arg1 = data.readDouble();
                double _arg2 = data.readDouble();
                double _arg32 = data.readDouble();
                double _arg4 = data.readDouble();
                int _arg5 = data.readInt();
                if (data.readInt() != 0) {
                    _arg6 = GeocoderParams.CREATOR.createFromParcel(data);
                } else {
                    _arg6 = null;
                }
                ArrayList arrayList = new ArrayList();
                String _result = getFromLocationName(_arg0, _arg1, _arg2, _arg32, _arg4, _arg5, _arg6, arrayList);
                reply.writeNoException();
                reply.writeString(_result);
                reply.writeTypedList(arrayList);
                return true;
            }
            data.enforceInterface(DESCRIPTOR);
            double _arg02 = data.readDouble();
            double _arg12 = data.readDouble();
            int _arg22 = data.readInt();
            if (data.readInt() != 0) {
                _arg3 = GeocoderParams.CREATOR.createFromParcel(data);
            } else {
                _arg3 = null;
            }
            ArrayList arrayList2 = new ArrayList();
            String _result2 = getFromLocation(_arg02, _arg12, _arg22, _arg3, arrayList2);
            reply.writeNoException();
            reply.writeString(_result2);
            reply.writeTypedList(arrayList2);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IGeocodeProvider {
            public static IGeocodeProvider sDefaultImpl;
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

            @Override // android.location.IGeocodeProvider
            public String getFromLocation(double latitude, double longitude, int maxResults, GeocoderParams params, List<Address> addrs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeDouble(latitude);
                    } catch (Throwable th) {
                        th = th;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeDouble(longitude);
                        _data.writeInt(maxResults);
                        if (params != null) {
                            _data.writeInt(1);
                            params.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                        if (!_status && Stub.getDefaultImpl() != null) {
                            String fromLocation = Stub.getDefaultImpl().getFromLocation(latitude, longitude, maxResults, params, addrs);
                            _reply.recycle();
                            _data.recycle();
                            return fromLocation;
                        }
                        _reply.readException();
                        String _result = _reply.readString();
                        try {
                            _reply.readTypedList(addrs, Address.CREATOR);
                            _reply.recycle();
                            _data.recycle();
                            return _result;
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
                }
            }

            @Override // android.location.IGeocodeProvider
            public String getFromLocationName(String locationName, double lowerLeftLatitude, double lowerLeftLongitude, double upperRightLatitude, double upperRightLongitude, int maxResults, GeocoderParams params, List<Address> addrs) throws RemoteException {
                Parcel _reply;
                String _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply2 = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(locationName);
                    _data.writeDouble(lowerLeftLatitude);
                    _data.writeDouble(lowerLeftLongitude);
                    _data.writeDouble(upperRightLatitude);
                    _data.writeDouble(upperRightLongitude);
                    _data.writeInt(maxResults);
                    if (params != null) {
                        try {
                            _data.writeInt(1);
                            params.writeToParcel(_data, 0);
                        } catch (Throwable th) {
                            th = th;
                            _reply = _reply2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(2, _data, _reply2, 0);
                    if (!_status) {
                        try {
                            if (Stub.getDefaultImpl() != null) {
                                try {
                                    String fromLocationName = Stub.getDefaultImpl().getFromLocationName(locationName, lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude, maxResults, params, addrs);
                                    _reply2.recycle();
                                    _data.recycle();
                                    return fromLocationName;
                                } catch (Throwable th2) {
                                    th = th2;
                                    _reply = _reply2;
                                    _reply.recycle();
                                    _data.recycle();
                                    throw th;
                                }
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            _reply = _reply2;
                        }
                    }
                    try {
                        _reply2.readException();
                        _result = _reply2.readString();
                        _reply = _reply2;
                    } catch (Throwable th4) {
                        th = th4;
                        _reply = _reply2;
                    }
                    try {
                        _reply.readTypedList(addrs, Address.CREATOR);
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    _reply = _reply2;
                }
            }
        }

        public static boolean setDefaultImpl(IGeocodeProvider impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IGeocodeProvider getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
