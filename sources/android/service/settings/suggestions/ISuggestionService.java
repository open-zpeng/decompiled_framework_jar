package android.service.settings.suggestions;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
/* loaded from: classes2.dex */
public interface ISuggestionService extends IInterface {
    synchronized void dismissSuggestion(Suggestion suggestion) throws RemoteException;

    synchronized List<Suggestion> getSuggestions() throws RemoteException;

    synchronized void launchSuggestion(Suggestion suggestion) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements ISuggestionService {
        private static final String DESCRIPTOR = "android.service.settings.suggestions.ISuggestionService";
        static final int TRANSACTION_dismissSuggestion = 3;
        static final int TRANSACTION_getSuggestions = 2;
        static final int TRANSACTION_launchSuggestion = 4;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static synchronized ISuggestionService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ISuggestionService)) {
                return (ISuggestionService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Suggestion _arg0;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    List<Suggestion> _result = getSuggestions();
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0 ? Suggestion.CREATOR.createFromParcel(data) : null;
                    dismissSuggestion(_arg0);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0 ? Suggestion.CREATOR.createFromParcel(data) : null;
                    launchSuggestion(_arg0);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements ISuggestionService {
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

            @Override // android.service.settings.suggestions.ISuggestionService
            public synchronized List<Suggestion> getSuggestions() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    List<Suggestion> _result = _reply.createTypedArrayList(Suggestion.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.service.settings.suggestions.ISuggestionService
            public synchronized void dismissSuggestion(Suggestion suggestion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (suggestion != null) {
                        _data.writeInt(1);
                        suggestion.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.service.settings.suggestions.ISuggestionService
            public synchronized void launchSuggestion(Suggestion suggestion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (suggestion != null) {
                        _data.writeInt(1);
                        suggestion.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
