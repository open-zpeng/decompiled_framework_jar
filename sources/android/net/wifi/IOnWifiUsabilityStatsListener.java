package android.net.wifi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* loaded from: classes2.dex */
public interface IOnWifiUsabilityStatsListener extends IInterface {
    void onWifiUsabilityStats(int i, boolean z, WifiUsabilityStatsEntry wifiUsabilityStatsEntry) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IOnWifiUsabilityStatsListener {
        @Override // android.net.wifi.IOnWifiUsabilityStatsListener
        public void onWifiUsabilityStats(int seqNum, boolean isSameBssidAndFreq, WifiUsabilityStatsEntry stats) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IOnWifiUsabilityStatsListener {
        private static final String DESCRIPTOR = "android.net.wifi.IOnWifiUsabilityStatsListener";
        static final int TRANSACTION_onWifiUsabilityStats = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IOnWifiUsabilityStatsListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IOnWifiUsabilityStatsListener)) {
                return (IOnWifiUsabilityStatsListener) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onWifiUsabilityStats";
            }
            return null;
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            WifiUsabilityStatsEntry _arg2;
            if (code != 1) {
                if (code == 1598968902) {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(code, data, reply, flags);
            }
            data.enforceInterface(DESCRIPTOR);
            int _arg0 = data.readInt();
            boolean _arg1 = data.readInt() != 0;
            if (data.readInt() != 0) {
                _arg2 = WifiUsabilityStatsEntry.CREATOR.createFromParcel(data);
            } else {
                _arg2 = null;
            }
            onWifiUsabilityStats(_arg0, _arg1, _arg2);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IOnWifiUsabilityStatsListener {
            public static IOnWifiUsabilityStatsListener sDefaultImpl;
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

            @Override // android.net.wifi.IOnWifiUsabilityStatsListener
            public void onWifiUsabilityStats(int seqNum, boolean isSameBssidAndFreq, WifiUsabilityStatsEntry stats) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(seqNum);
                    _data.writeInt(isSameBssidAndFreq ? 1 : 0);
                    if (stats != null) {
                        _data.writeInt(1);
                        stats.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onWifiUsabilityStats(seqNum, isSameBssidAndFreq, stats);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IOnWifiUsabilityStatsListener impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IOnWifiUsabilityStatsListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
