package android.service.autofill;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.view.autofill.AutofillValue;
import java.util.List;
/* loaded from: classes2.dex */
public interface IAutofillFieldClassificationService extends IInterface {
    synchronized void getScores(RemoteCallback remoteCallback, String str, Bundle bundle, List<AutofillValue> list, String[] strArr) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IAutofillFieldClassificationService {
        private static final String DESCRIPTOR = "android.service.autofill.IAutofillFieldClassificationService";
        static final int TRANSACTION_getScores = 1;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized IAutofillFieldClassificationService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IAutofillFieldClassificationService)) {
                return (IAutofillFieldClassificationService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            RemoteCallback _arg0;
            if (code != 1) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            }
            data.enforceInterface(DESCRIPTOR);
            if (data.readInt() != 0) {
                RemoteCallback _arg02 = RemoteCallback.CREATOR.createFromParcel(data);
                _arg0 = _arg02;
            } else {
                _arg0 = null;
            }
            String _arg1 = data.readString();
            Bundle _arg2 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
            List<AutofillValue> _arg3 = data.createTypedArrayList(AutofillValue.CREATOR);
            String[] _arg4 = data.createStringArray();
            getScores(_arg0, _arg1, _arg2, _arg3, _arg4);
            return true;
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IAutofillFieldClassificationService {
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

            @Override // android.service.autofill.IAutofillFieldClassificationService
            public synchronized void getScores(RemoteCallback callback, String algorithmName, Bundle algorithmArgs, List<AutofillValue> actualValues, String[] userDataValues) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (callback != null) {
                        _data.writeInt(1);
                        callback.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(algorithmName);
                    if (algorithmArgs != null) {
                        _data.writeInt(1);
                        algorithmArgs.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeTypedList(actualValues);
                    _data.writeStringArray(userDataValues);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
