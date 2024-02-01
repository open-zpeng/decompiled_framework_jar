package android.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public interface IGeocodeProvider extends IInterface {
    private protected String getFromLocation(double d, double d2, int i, GeocoderParams geocoderParams, List<Address> list) throws RemoteException;

    private protected String getFromLocationName(String str, double d, double d2, double d3, double d4, int i, GeocoderParams geocoderParams, List<Address> list) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IGeocodeProvider {
        private static final String DESCRIPTOR = "android.location.IGeocodeProvider";
        static final int TRANSACTION_getFromLocation = 1;
        static final int TRANSACTION_getFromLocationName = 2;

        private protected Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        private protected static IGeocodeProvider asInterface(IBinder obj) {
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

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    double _arg0 = data.readDouble();
                    double _arg1 = data.readDouble();
                    int _arg2 = data.readInt();
                    GeocoderParams _arg3 = data.readInt() != 0 ? GeocoderParams.CREATOR.createFromParcel(data) : null;
                    ArrayList arrayList = new ArrayList();
                    String _result = getFromLocation(_arg0, _arg1, _arg2, _arg3, arrayList);
                    reply.writeNoException();
                    reply.writeString(_result);
                    reply.writeTypedList(arrayList);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    double _arg12 = data.readDouble();
                    double _arg22 = data.readDouble();
                    double _arg32 = data.readDouble();
                    double _arg4 = data.readDouble();
                    int _arg5 = data.readInt();
                    GeocoderParams _arg6 = data.readInt() != 0 ? GeocoderParams.CREATOR.createFromParcel(data) : null;
                    ArrayList arrayList2 = new ArrayList();
                    String _result2 = getFromLocationName(_arg02, _arg12, _arg22, _arg32, _arg4, _arg5, _arg6, arrayList2);
                    reply.writeNoException();
                    reply.writeString(_result2);
                    reply.writeTypedList(arrayList2);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes.dex */
        private static class Proxy implements IGeocodeProvider {
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

            public synchronized String getFromLocation(double latitude, double longitude, int maxResults, GeocoderParams params, List<Address> addrs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeDouble(latitude);
                    _data.writeDouble(longitude);
                    _data.writeInt(maxResults);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    _reply.readTypedList(addrs, Address.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized String getFromLocationName(String locationName, double lowerLeftLatitude, double lowerLeftLongitude, double upperRightLatitude, double upperRightLongitude, int maxResults, GeocoderParams params, List<Address> addrs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(locationName);
                    } catch (Throwable th) {
                        th = th;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
                try {
                    _data.writeDouble(lowerLeftLatitude);
                    try {
                        _data.writeDouble(lowerLeftLongitude);
                        try {
                            _data.writeDouble(upperRightLatitude);
                            try {
                                _data.writeDouble(upperRightLongitude);
                                try {
                                    _data.writeInt(maxResults);
                                    if (params != null) {
                                        _data.writeInt(1);
                                        params.writeToParcel(_data, 0);
                                    } else {
                                        _data.writeInt(0);
                                    }
                                    try {
                                        this.mRemote.transact(2, _data, _reply, 0);
                                        _reply.readException();
                                        String _result = _reply.readString();
                                        try {
                                            _reply.readTypedList(addrs, Address.CREATOR);
                                            _reply.recycle();
                                            _data.recycle();
                                            return _result;
                                        } catch (Throwable th3) {
                                            th = th3;
                                            _reply.recycle();
                                            _data.recycle();
                                            throw th;
                                        }
                                    } catch (Throwable th4) {
                                        th = th4;
                                        _reply.recycle();
                                        _data.recycle();
                                        throw th;
                                    }
                                } catch (Throwable th5) {
                                    th = th5;
                                }
                            } catch (Throwable th6) {
                                th = th6;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th7) {
                            th = th7;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th8) {
                        th = th8;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th9) {
                    th = th9;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }
        }
    }
}
