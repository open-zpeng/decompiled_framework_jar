package com.android.internal.telephony;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.CellInfo;
import android.telephony.DataConnectionRealTimeInfo;
import android.telephony.PhysicalChannelConfig;
import android.telephony.PreciseCallState;
import android.telephony.PreciseDataConnectionState;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.VoLteServiceState;
import java.util.List;
/* loaded from: classes3.dex */
public interface IPhoneStateListener extends IInterface {
    private protected void onCallForwardingIndicatorChanged(boolean z) throws RemoteException;

    private protected void onCallStateChanged(int i, String str) throws RemoteException;

    synchronized void onCarrierNetworkChange(boolean z) throws RemoteException;

    synchronized void onCellInfoChanged(List<CellInfo> list) throws RemoteException;

    private protected void onCellLocationChanged(Bundle bundle) throws RemoteException;

    synchronized void onDataActivationStateChanged(int i) throws RemoteException;

    private protected void onDataActivity(int i) throws RemoteException;

    synchronized void onDataConnectionRealTimeInfoChanged(DataConnectionRealTimeInfo dataConnectionRealTimeInfo) throws RemoteException;

    private protected void onDataConnectionStateChanged(int i, int i2) throws RemoteException;

    private protected void onMessageWaitingIndicatorChanged(boolean z) throws RemoteException;

    synchronized void onOemHookRawEvent(byte[] bArr) throws RemoteException;

    synchronized void onOtaspChanged(int i) throws RemoteException;

    synchronized void onPhysicalChannelConfigurationChanged(List<PhysicalChannelConfig> list) throws RemoteException;

    synchronized void onPreciseCallStateChanged(PreciseCallState preciseCallState) throws RemoteException;

    synchronized void onPreciseDataConnectionStateChanged(PreciseDataConnectionState preciseDataConnectionState) throws RemoteException;

    private protected void onServiceStateChanged(ServiceState serviceState) throws RemoteException;

    private protected void onSignalStrengthChanged(int i) throws RemoteException;

    private protected void onSignalStrengthsChanged(SignalStrength signalStrength) throws RemoteException;

    synchronized void onUserMobileDataStateChanged(boolean z) throws RemoteException;

    synchronized void onVoLteServiceStateChanged(VoLteServiceState voLteServiceState) throws RemoteException;

    synchronized void onVoiceActivationStateChanged(int i) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IPhoneStateListener {
        private static final String DESCRIPTOR = "com.android.internal.telephony.IPhoneStateListener";
        static final int TRANSACTION_onCallForwardingIndicatorChanged = 4;
        static final int TRANSACTION_onCallStateChanged = 6;
        static final int TRANSACTION_onCarrierNetworkChange = 20;
        static final int TRANSACTION_onCellInfoChanged = 12;
        static final int TRANSACTION_onCellLocationChanged = 5;
        static final int TRANSACTION_onDataActivationStateChanged = 18;
        static final int TRANSACTION_onDataActivity = 8;
        static final int TRANSACTION_onDataConnectionRealTimeInfoChanged = 15;
        static final int TRANSACTION_onDataConnectionStateChanged = 7;
        static final int TRANSACTION_onMessageWaitingIndicatorChanged = 3;
        static final int TRANSACTION_onOemHookRawEvent = 19;
        static final int TRANSACTION_onOtaspChanged = 11;
        static final int TRANSACTION_onPhysicalChannelConfigurationChanged = 10;
        static final int TRANSACTION_onPreciseCallStateChanged = 13;
        static final int TRANSACTION_onPreciseDataConnectionStateChanged = 14;
        static final int TRANSACTION_onServiceStateChanged = 1;
        static final int TRANSACTION_onSignalStrengthChanged = 2;
        static final int TRANSACTION_onSignalStrengthsChanged = 9;
        static final int TRANSACTION_onUserMobileDataStateChanged = 21;
        static final int TRANSACTION_onVoLteServiceStateChanged = 16;
        static final int TRANSACTION_onVoiceActivationStateChanged = 17;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static IPhoneStateListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IPhoneStateListener)) {
                return (IPhoneStateListener) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg0;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    onServiceStateChanged(data.readInt() != 0 ? ServiceState.CREATOR.createFromParcel(data) : null);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    onSignalStrengthChanged(data.readInt());
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    onMessageWaitingIndicatorChanged(_arg0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    onCallForwardingIndicatorChanged(_arg0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    onCellLocationChanged(data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg02 = data.readInt();
                    String _arg1 = data.readString();
                    onCallStateChanged(_arg02, _arg1);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    int _arg12 = data.readInt();
                    onDataConnectionStateChanged(_arg03, _arg12);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    onDataActivity(data.readInt());
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    onSignalStrengthsChanged(data.readInt() != 0 ? SignalStrength.CREATOR.createFromParcel(data) : null);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    onPhysicalChannelConfigurationChanged(data.createTypedArrayList(PhysicalChannelConfig.CREATOR));
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    onOtaspChanged(data.readInt());
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    onCellInfoChanged(data.createTypedArrayList(CellInfo.CREATOR));
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    onPreciseCallStateChanged(data.readInt() != 0 ? PreciseCallState.CREATOR.createFromParcel(data) : null);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    onPreciseDataConnectionStateChanged(data.readInt() != 0 ? PreciseDataConnectionState.CREATOR.createFromParcel(data) : null);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    onDataConnectionRealTimeInfoChanged(data.readInt() != 0 ? DataConnectionRealTimeInfo.CREATOR.createFromParcel(data) : null);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    onVoLteServiceStateChanged(data.readInt() != 0 ? VoLteServiceState.CREATOR.createFromParcel(data) : null);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    onVoiceActivationStateChanged(data.readInt());
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    onDataActivationStateChanged(data.readInt());
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    onOemHookRawEvent(data.createByteArray());
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    onCarrierNetworkChange(_arg0);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    _arg0 = data.readInt() != 0;
                    onUserMobileDataStateChanged(_arg0);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes3.dex */
        private static class Proxy implements IPhoneStateListener {
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

            public synchronized void onServiceStateChanged(ServiceState serviceState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (serviceState != null) {
                        _data.writeInt(1);
                        serviceState.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void onSignalStrengthChanged(int asu) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(asu);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void onMessageWaitingIndicatorChanged(boolean mwi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mwi ? 1 : 0);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void onCallForwardingIndicatorChanged(boolean cfi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cfi ? 1 : 0);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void onCellLocationChanged(Bundle location) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (location != null) {
                        _data.writeInt(1);
                        location.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void onCallStateChanged(int state, String incomingNumber) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    _data.writeString(incomingNumber);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void onDataConnectionStateChanged(int state, int networkType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    _data.writeInt(networkType);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void onDataActivity(int direction) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(direction);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void onSignalStrengthsChanged(SignalStrength signalStrength) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (signalStrength != null) {
                        _data.writeInt(1);
                        signalStrength.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneStateListener
            public synchronized void onPhysicalChannelConfigurationChanged(List<PhysicalChannelConfig> configs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(configs);
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneStateListener
            public synchronized void onOtaspChanged(int otaspMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(otaspMode);
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneStateListener
            public synchronized void onCellInfoChanged(List<CellInfo> cellInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(cellInfo);
                    this.mRemote.transact(12, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneStateListener
            public synchronized void onPreciseCallStateChanged(PreciseCallState callState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (callState != null) {
                        _data.writeInt(1);
                        callState.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(13, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneStateListener
            public synchronized void onPreciseDataConnectionStateChanged(PreciseDataConnectionState dataConnectionState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (dataConnectionState != null) {
                        _data.writeInt(1);
                        dataConnectionState.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(14, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneStateListener
            public synchronized void onDataConnectionRealTimeInfoChanged(DataConnectionRealTimeInfo dcRtInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (dcRtInfo != null) {
                        _data.writeInt(1);
                        dcRtInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(15, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneStateListener
            public synchronized void onVoLteServiceStateChanged(VoLteServiceState lteState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (lteState != null) {
                        _data.writeInt(1);
                        lteState.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(16, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneStateListener
            public synchronized void onVoiceActivationStateChanged(int activationState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(activationState);
                    this.mRemote.transact(17, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneStateListener
            public synchronized void onDataActivationStateChanged(int activationState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(activationState);
                    this.mRemote.transact(18, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneStateListener
            public synchronized void onOemHookRawEvent(byte[] rawData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(rawData);
                    this.mRemote.transact(19, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneStateListener
            public synchronized void onCarrierNetworkChange(boolean active) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(active ? 1 : 0);
                    this.mRemote.transact(20, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.IPhoneStateListener
            public synchronized void onUserMobileDataStateChanged(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    this.mRemote.transact(21, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
