package com.android.internal.telephony;

import android.annotation.UnsupportedAppUsage;
import android.net.LinkProperties;
import android.net.NetworkCapabilities;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.CallQuality;
import android.telephony.CellInfo;
import android.telephony.PhoneCapability;
import android.telephony.PhysicalChannelConfig;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.ims.ImsReasonInfo;
import com.android.internal.telephony.IOnSubscriptionsChangedListener;
import com.android.internal.telephony.IPhoneStateListener;
import java.util.List;

/* loaded from: classes3.dex */
public interface ITelephonyRegistry extends IInterface {
    void addOnOpportunisticSubscriptionsChangedListener(String str, IOnSubscriptionsChangedListener iOnSubscriptionsChangedListener) throws RemoteException;

    void addOnSubscriptionsChangedListener(String str, IOnSubscriptionsChangedListener iOnSubscriptionsChangedListener) throws RemoteException;

    @UnsupportedAppUsage
    void listen(String str, IPhoneStateListener iPhoneStateListener, int i, boolean z) throws RemoteException;

    void listenForSubscriber(int i, String str, IPhoneStateListener iPhoneStateListener, int i2, boolean z) throws RemoteException;

    void notifyActiveDataSubIdChanged(int i) throws RemoteException;

    void notifyCallForwardingChanged(boolean z) throws RemoteException;

    void notifyCallForwardingChangedForSubscriber(int i, boolean z) throws RemoteException;

    void notifyCallQualityChanged(CallQuality callQuality, int i, int i2, int i3) throws RemoteException;

    @UnsupportedAppUsage
    void notifyCallState(int i, String str) throws RemoteException;

    void notifyCallStateForPhoneId(int i, int i2, int i3, String str) throws RemoteException;

    void notifyCarrierNetworkChange(boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void notifyCellInfo(List<CellInfo> list) throws RemoteException;

    void notifyCellInfoForSubscriber(int i, List<CellInfo> list) throws RemoteException;

    void notifyCellLocation(Bundle bundle) throws RemoteException;

    void notifyCellLocationForSubscriber(int i, Bundle bundle) throws RemoteException;

    void notifyDataActivity(int i) throws RemoteException;

    void notifyDataActivityForSubscriber(int i, int i2) throws RemoteException;

    void notifyDataConnection(int i, boolean z, String str, String str2, LinkProperties linkProperties, NetworkCapabilities networkCapabilities, int i2, boolean z2) throws RemoteException;

    @UnsupportedAppUsage
    void notifyDataConnectionFailed(String str) throws RemoteException;

    void notifyDataConnectionFailedForSubscriber(int i, int i2, String str) throws RemoteException;

    void notifyDataConnectionForSubscriber(int i, int i2, int i3, boolean z, String str, String str2, LinkProperties linkProperties, NetworkCapabilities networkCapabilities, int i4, boolean z2) throws RemoteException;

    void notifyDisconnectCause(int i, int i2, int i3, int i4) throws RemoteException;

    void notifyEmergencyNumberList(int i, int i2) throws RemoteException;

    void notifyImsDisconnectCause(int i, ImsReasonInfo imsReasonInfo) throws RemoteException;

    void notifyMessageWaitingChangedForPhoneId(int i, int i2, boolean z) throws RemoteException;

    void notifyOemHookRawEventForSubscriber(int i, int i2, byte[] bArr) throws RemoteException;

    void notifyOpportunisticSubscriptionInfoChanged() throws RemoteException;

    void notifyOtaspChanged(int i, int i2) throws RemoteException;

    void notifyPhoneCapabilityChanged(PhoneCapability phoneCapability) throws RemoteException;

    void notifyPhysicalChannelConfigurationForSubscriber(int i, int i2, List<PhysicalChannelConfig> list) throws RemoteException;

    void notifyPreciseCallState(int i, int i2, int i3, int i4, int i5) throws RemoteException;

    void notifyPreciseDataConnectionFailed(int i, int i2, String str, String str2, int i3) throws RemoteException;

    void notifyRadioPowerStateChanged(int i, int i2, int i3) throws RemoteException;

    void notifyServiceStateForPhoneId(int i, int i2, ServiceState serviceState) throws RemoteException;

    void notifySignalStrengthForPhoneId(int i, int i2, SignalStrength signalStrength) throws RemoteException;

    void notifySimActivationStateChangedForPhoneId(int i, int i2, int i3, int i4) throws RemoteException;

    void notifySrvccStateChanged(int i, int i2) throws RemoteException;

    void notifySubscriptionInfoChanged() throws RemoteException;

    void notifyUserMobileDataStateChangedForPhoneId(int i, int i2, boolean z) throws RemoteException;

    void removeOnSubscriptionsChangedListener(String str, IOnSubscriptionsChangedListener iOnSubscriptionsChangedListener) throws RemoteException;

    /* loaded from: classes3.dex */
    public static class Default implements ITelephonyRegistry {
        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void addOnSubscriptionsChangedListener(String pkg, IOnSubscriptionsChangedListener callback) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void addOnOpportunisticSubscriptionsChangedListener(String pkg, IOnSubscriptionsChangedListener callback) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void removeOnSubscriptionsChangedListener(String pkg, IOnSubscriptionsChangedListener callback) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void listen(String pkg, IPhoneStateListener callback, int events, boolean notifyNow) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void listenForSubscriber(int subId, String pkg, IPhoneStateListener callback, int events, boolean notifyNow) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyCallState(int state, String incomingNumber) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyCallStateForPhoneId(int phoneId, int subId, int state, String incomingNumber) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyServiceStateForPhoneId(int phoneId, int subId, ServiceState state) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifySignalStrengthForPhoneId(int phoneId, int subId, SignalStrength signalStrength) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyMessageWaitingChangedForPhoneId(int phoneId, int subId, boolean mwi) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyCallForwardingChanged(boolean cfi) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyCallForwardingChangedForSubscriber(int subId, boolean cfi) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyDataActivity(int state) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyDataActivityForSubscriber(int subId, int state) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyDataConnection(int state, boolean isDataConnectivityPossible, String apn, String apnType, LinkProperties linkProperties, NetworkCapabilities networkCapabilities, int networkType, boolean roaming) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyDataConnectionForSubscriber(int phoneId, int subId, int state, boolean isDataConnectivityPossible, String apn, String apnType, LinkProperties linkProperties, NetworkCapabilities networkCapabilities, int networkType, boolean roaming) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyDataConnectionFailed(String apnType) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyDataConnectionFailedForSubscriber(int phoneId, int subId, String apnType) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyCellLocation(Bundle cellLocation) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyCellLocationForSubscriber(int subId, Bundle cellLocation) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyOtaspChanged(int subId, int otaspMode) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyCellInfo(List<CellInfo> cellInfo) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyPhysicalChannelConfigurationForSubscriber(int phoneId, int subId, List<PhysicalChannelConfig> configs) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyPreciseCallState(int phoneId, int subId, int ringingCallState, int foregroundCallState, int backgroundCallState) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyDisconnectCause(int phoneId, int subId, int disconnectCause, int preciseDisconnectCause) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyPreciseDataConnectionFailed(int phoneId, int subId, String apnType, String apn, int failCause) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyCellInfoForSubscriber(int subId, List<CellInfo> cellInfo) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifySrvccStateChanged(int subId, int lteState) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifySimActivationStateChangedForPhoneId(int phoneId, int subId, int activationState, int activationType) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyOemHookRawEventForSubscriber(int phoneId, int subId, byte[] rawData) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifySubscriptionInfoChanged() throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyOpportunisticSubscriptionInfoChanged() throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyCarrierNetworkChange(boolean active) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyUserMobileDataStateChangedForPhoneId(int phoneId, int subId, boolean state) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyPhoneCapabilityChanged(PhoneCapability capability) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyActiveDataSubIdChanged(int activeDataSubId) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyRadioPowerStateChanged(int phoneId, int subId, int state) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyEmergencyNumberList(int phoneId, int subId) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyCallQualityChanged(CallQuality callQuality, int phoneId, int subId, int callNetworkType) throws RemoteException {
        }

        @Override // com.android.internal.telephony.ITelephonyRegistry
        public void notifyImsDisconnectCause(int subId, ImsReasonInfo imsReasonInfo) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements ITelephonyRegistry {
        private static final String DESCRIPTOR = "com.android.internal.telephony.ITelephonyRegistry";
        static final int TRANSACTION_addOnOpportunisticSubscriptionsChangedListener = 2;
        static final int TRANSACTION_addOnSubscriptionsChangedListener = 1;
        static final int TRANSACTION_listen = 4;
        static final int TRANSACTION_listenForSubscriber = 5;
        static final int TRANSACTION_notifyActiveDataSubIdChanged = 36;
        static final int TRANSACTION_notifyCallForwardingChanged = 11;
        static final int TRANSACTION_notifyCallForwardingChangedForSubscriber = 12;
        static final int TRANSACTION_notifyCallQualityChanged = 39;
        static final int TRANSACTION_notifyCallState = 6;
        static final int TRANSACTION_notifyCallStateForPhoneId = 7;
        static final int TRANSACTION_notifyCarrierNetworkChange = 33;
        static final int TRANSACTION_notifyCellInfo = 22;
        static final int TRANSACTION_notifyCellInfoForSubscriber = 27;
        static final int TRANSACTION_notifyCellLocation = 19;
        static final int TRANSACTION_notifyCellLocationForSubscriber = 20;
        static final int TRANSACTION_notifyDataActivity = 13;
        static final int TRANSACTION_notifyDataActivityForSubscriber = 14;
        static final int TRANSACTION_notifyDataConnection = 15;
        static final int TRANSACTION_notifyDataConnectionFailed = 17;
        static final int TRANSACTION_notifyDataConnectionFailedForSubscriber = 18;
        static final int TRANSACTION_notifyDataConnectionForSubscriber = 16;
        static final int TRANSACTION_notifyDisconnectCause = 25;
        static final int TRANSACTION_notifyEmergencyNumberList = 38;
        static final int TRANSACTION_notifyImsDisconnectCause = 40;
        static final int TRANSACTION_notifyMessageWaitingChangedForPhoneId = 10;
        static final int TRANSACTION_notifyOemHookRawEventForSubscriber = 30;
        static final int TRANSACTION_notifyOpportunisticSubscriptionInfoChanged = 32;
        static final int TRANSACTION_notifyOtaspChanged = 21;
        static final int TRANSACTION_notifyPhoneCapabilityChanged = 35;
        static final int TRANSACTION_notifyPhysicalChannelConfigurationForSubscriber = 23;
        static final int TRANSACTION_notifyPreciseCallState = 24;
        static final int TRANSACTION_notifyPreciseDataConnectionFailed = 26;
        static final int TRANSACTION_notifyRadioPowerStateChanged = 37;
        static final int TRANSACTION_notifyServiceStateForPhoneId = 8;
        static final int TRANSACTION_notifySignalStrengthForPhoneId = 9;
        static final int TRANSACTION_notifySimActivationStateChangedForPhoneId = 29;
        static final int TRANSACTION_notifySrvccStateChanged = 28;
        static final int TRANSACTION_notifySubscriptionInfoChanged = 31;
        static final int TRANSACTION_notifyUserMobileDataStateChangedForPhoneId = 34;
        static final int TRANSACTION_removeOnSubscriptionsChangedListener = 3;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITelephonyRegistry asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ITelephonyRegistry)) {
                return (ITelephonyRegistry) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "addOnSubscriptionsChangedListener";
                case 2:
                    return "addOnOpportunisticSubscriptionsChangedListener";
                case 3:
                    return "removeOnSubscriptionsChangedListener";
                case 4:
                    return "listen";
                case 5:
                    return "listenForSubscriber";
                case 6:
                    return "notifyCallState";
                case 7:
                    return "notifyCallStateForPhoneId";
                case 8:
                    return "notifyServiceStateForPhoneId";
                case 9:
                    return "notifySignalStrengthForPhoneId";
                case 10:
                    return "notifyMessageWaitingChangedForPhoneId";
                case 11:
                    return "notifyCallForwardingChanged";
                case 12:
                    return "notifyCallForwardingChangedForSubscriber";
                case 13:
                    return "notifyDataActivity";
                case 14:
                    return "notifyDataActivityForSubscriber";
                case 15:
                    return "notifyDataConnection";
                case 16:
                    return "notifyDataConnectionForSubscriber";
                case 17:
                    return "notifyDataConnectionFailed";
                case 18:
                    return "notifyDataConnectionFailedForSubscriber";
                case 19:
                    return "notifyCellLocation";
                case 20:
                    return "notifyCellLocationForSubscriber";
                case 21:
                    return "notifyOtaspChanged";
                case 22:
                    return "notifyCellInfo";
                case 23:
                    return "notifyPhysicalChannelConfigurationForSubscriber";
                case 24:
                    return "notifyPreciseCallState";
                case 25:
                    return "notifyDisconnectCause";
                case 26:
                    return "notifyPreciseDataConnectionFailed";
                case 27:
                    return "notifyCellInfoForSubscriber";
                case 28:
                    return "notifySrvccStateChanged";
                case 29:
                    return "notifySimActivationStateChangedForPhoneId";
                case 30:
                    return "notifyOemHookRawEventForSubscriber";
                case 31:
                    return "notifySubscriptionInfoChanged";
                case 32:
                    return "notifyOpportunisticSubscriptionInfoChanged";
                case 33:
                    return "notifyCarrierNetworkChange";
                case 34:
                    return "notifyUserMobileDataStateChangedForPhoneId";
                case 35:
                    return "notifyPhoneCapabilityChanged";
                case 36:
                    return "notifyActiveDataSubIdChanged";
                case 37:
                    return "notifyRadioPowerStateChanged";
                case 38:
                    return "notifyEmergencyNumberList";
                case 39:
                    return "notifyCallQualityChanged";
                case 40:
                    return "notifyImsDisconnectCause";
                default:
                    return null;
            }
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg2;
            ServiceState _arg22;
            SignalStrength _arg23;
            LinkProperties _arg4;
            NetworkCapabilities _arg5;
            LinkProperties _arg6;
            NetworkCapabilities _arg7;
            Bundle _arg0;
            Bundle _arg1;
            PhoneCapability _arg02;
            CallQuality _arg03;
            ImsReasonInfo _arg12;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    IOnSubscriptionsChangedListener _arg13 = IOnSubscriptionsChangedListener.Stub.asInterface(data.readStrongBinder());
                    addOnSubscriptionsChangedListener(_arg04, _arg13);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    IOnSubscriptionsChangedListener _arg14 = IOnSubscriptionsChangedListener.Stub.asInterface(data.readStrongBinder());
                    addOnOpportunisticSubscriptionsChangedListener(_arg05, _arg14);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg06 = data.readString();
                    IOnSubscriptionsChangedListener _arg15 = IOnSubscriptionsChangedListener.Stub.asInterface(data.readStrongBinder());
                    removeOnSubscriptionsChangedListener(_arg06, _arg15);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    IPhoneStateListener _arg16 = IPhoneStateListener.Stub.asInterface(data.readStrongBinder());
                    int _arg24 = data.readInt();
                    _arg2 = data.readInt() != 0;
                    listen(_arg07, _arg16, _arg24, _arg2);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    String _arg17 = data.readString();
                    IPhoneStateListener _arg25 = IPhoneStateListener.Stub.asInterface(data.readStrongBinder());
                    int _arg3 = data.readInt();
                    boolean _arg42 = data.readInt() != 0;
                    listenForSubscriber(_arg08, _arg17, _arg25, _arg3, _arg42);
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg09 = data.readInt();
                    String _arg18 = data.readString();
                    notifyCallState(_arg09, _arg18);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg010 = data.readInt();
                    int _arg19 = data.readInt();
                    int _arg26 = data.readInt();
                    String _arg32 = data.readString();
                    notifyCallStateForPhoneId(_arg010, _arg19, _arg26, _arg32);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg011 = data.readInt();
                    int _arg110 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg22 = ServiceState.CREATOR.createFromParcel(data);
                    } else {
                        _arg22 = null;
                    }
                    notifyServiceStateForPhoneId(_arg011, _arg110, _arg22);
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg012 = data.readInt();
                    int _arg111 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg23 = SignalStrength.CREATOR.createFromParcel(data);
                    } else {
                        _arg23 = null;
                    }
                    notifySignalStrengthForPhoneId(_arg012, _arg111, _arg23);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg013 = data.readInt();
                    int _arg112 = data.readInt();
                    _arg2 = data.readInt() != 0;
                    notifyMessageWaitingChangedForPhoneId(_arg013, _arg112, _arg2);
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    _arg2 = data.readInt() != 0;
                    notifyCallForwardingChanged(_arg2);
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg014 = data.readInt();
                    _arg2 = data.readInt() != 0;
                    notifyCallForwardingChangedForSubscriber(_arg014, _arg2);
                    reply.writeNoException();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg015 = data.readInt();
                    notifyDataActivity(_arg015);
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg016 = data.readInt();
                    int _arg113 = data.readInt();
                    notifyDataActivityForSubscriber(_arg016, _arg113);
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg017 = data.readInt();
                    boolean _arg114 = data.readInt() != 0;
                    String _arg27 = data.readString();
                    String _arg33 = data.readString();
                    if (data.readInt() != 0) {
                        _arg4 = LinkProperties.CREATOR.createFromParcel(data);
                    } else {
                        _arg4 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg5 = NetworkCapabilities.CREATOR.createFromParcel(data);
                    } else {
                        _arg5 = null;
                    }
                    int _arg62 = data.readInt();
                    boolean _arg72 = data.readInt() != 0;
                    notifyDataConnection(_arg017, _arg114, _arg27, _arg33, _arg4, _arg5, _arg62, _arg72);
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg018 = data.readInt();
                    int _arg115 = data.readInt();
                    int _arg28 = data.readInt();
                    boolean _arg34 = data.readInt() != 0;
                    String _arg43 = data.readString();
                    String _arg52 = data.readString();
                    if (data.readInt() != 0) {
                        _arg6 = LinkProperties.CREATOR.createFromParcel(data);
                    } else {
                        _arg6 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg7 = NetworkCapabilities.CREATOR.createFromParcel(data);
                    } else {
                        _arg7 = null;
                    }
                    int _arg8 = data.readInt();
                    boolean _arg9 = data.readInt() != 0;
                    notifyDataConnectionForSubscriber(_arg018, _arg115, _arg28, _arg34, _arg43, _arg52, _arg6, _arg7, _arg8, _arg9);
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg019 = data.readString();
                    notifyDataConnectionFailed(_arg019);
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg020 = data.readInt();
                    int _arg116 = data.readInt();
                    notifyDataConnectionFailedForSubscriber(_arg020, _arg116, data.readString());
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    notifyCellLocation(_arg0);
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg021 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    notifyCellLocationForSubscriber(_arg021, _arg1);
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg022 = data.readInt();
                    int _arg117 = data.readInt();
                    notifyOtaspChanged(_arg022, _arg117);
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    List<CellInfo> _arg023 = data.createTypedArrayList(CellInfo.CREATOR);
                    notifyCellInfo(_arg023);
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg024 = data.readInt();
                    int _arg118 = data.readInt();
                    notifyPhysicalChannelConfigurationForSubscriber(_arg024, _arg118, data.createTypedArrayList(PhysicalChannelConfig.CREATOR));
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg025 = data.readInt();
                    int _arg119 = data.readInt();
                    int _arg29 = data.readInt();
                    int _arg35 = data.readInt();
                    int _arg44 = data.readInt();
                    notifyPreciseCallState(_arg025, _arg119, _arg29, _arg35, _arg44);
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg026 = data.readInt();
                    int _arg120 = data.readInt();
                    int _arg210 = data.readInt();
                    int _arg36 = data.readInt();
                    notifyDisconnectCause(_arg026, _arg120, _arg210, _arg36);
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg027 = data.readInt();
                    int _arg121 = data.readInt();
                    String _arg211 = data.readString();
                    String _arg37 = data.readString();
                    int _arg45 = data.readInt();
                    notifyPreciseDataConnectionFailed(_arg027, _arg121, _arg211, _arg37, _arg45);
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg028 = data.readInt();
                    List<CellInfo> _arg122 = data.createTypedArrayList(CellInfo.CREATOR);
                    notifyCellInfoForSubscriber(_arg028, _arg122);
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg029 = data.readInt();
                    int _arg123 = data.readInt();
                    notifySrvccStateChanged(_arg029, _arg123);
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg030 = data.readInt();
                    int _arg124 = data.readInt();
                    int _arg212 = data.readInt();
                    int _arg38 = data.readInt();
                    notifySimActivationStateChangedForPhoneId(_arg030, _arg124, _arg212, _arg38);
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg031 = data.readInt();
                    int _arg125 = data.readInt();
                    notifyOemHookRawEventForSubscriber(_arg031, _arg125, data.createByteArray());
                    reply.writeNoException();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    notifySubscriptionInfoChanged();
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    notifyOpportunisticSubscriptionInfoChanged();
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    _arg2 = data.readInt() != 0;
                    notifyCarrierNetworkChange(_arg2);
                    reply.writeNoException();
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg032 = data.readInt();
                    int _arg126 = data.readInt();
                    _arg2 = data.readInt() != 0;
                    notifyUserMobileDataStateChangedForPhoneId(_arg032, _arg126, _arg2);
                    reply.writeNoException();
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = PhoneCapability.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    notifyPhoneCapabilityChanged(_arg02);
                    reply.writeNoException();
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg033 = data.readInt();
                    notifyActiveDataSubIdChanged(_arg033);
                    reply.writeNoException();
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg034 = data.readInt();
                    int _arg127 = data.readInt();
                    notifyRadioPowerStateChanged(_arg034, _arg127, data.readInt());
                    reply.writeNoException();
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg035 = data.readInt();
                    int _arg128 = data.readInt();
                    notifyEmergencyNumberList(_arg035, _arg128);
                    reply.writeNoException();
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = CallQuality.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    int _arg129 = data.readInt();
                    int _arg213 = data.readInt();
                    int _arg39 = data.readInt();
                    notifyCallQualityChanged(_arg03, _arg129, _arg213, _arg39);
                    reply.writeNoException();
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg036 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg12 = ImsReasonInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    notifyImsDisconnectCause(_arg036, _arg12);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements ITelephonyRegistry {
            public static ITelephonyRegistry sDefaultImpl;
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

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void addOnSubscriptionsChangedListener(String pkg, IOnSubscriptionsChangedListener callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addOnSubscriptionsChangedListener(pkg, callback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void addOnOpportunisticSubscriptionsChangedListener(String pkg, IOnSubscriptionsChangedListener callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addOnOpportunisticSubscriptionsChangedListener(pkg, callback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void removeOnSubscriptionsChangedListener(String pkg, IOnSubscriptionsChangedListener callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeOnSubscriptionsChangedListener(pkg, callback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void listen(String pkg, IPhoneStateListener callback, int events, boolean notifyNow) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(events);
                    _data.writeInt(notifyNow ? 1 : 0);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().listen(pkg, callback, events, notifyNow);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void listenForSubscriber(int subId, String pkg, IPhoneStateListener callback, int events, boolean notifyNow) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeString(pkg);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(events);
                    _data.writeInt(notifyNow ? 1 : 0);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().listenForSubscriber(subId, pkg, callback, events, notifyNow);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyCallState(int state, String incomingNumber) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    _data.writeString(incomingNumber);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyCallState(state, incomingNumber);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyCallStateForPhoneId(int phoneId, int subId, int state, String incomingNumber) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    _data.writeInt(state);
                    _data.writeString(incomingNumber);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyCallStateForPhoneId(phoneId, subId, state, incomingNumber);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyServiceStateForPhoneId(int phoneId, int subId, ServiceState state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    if (state != null) {
                        _data.writeInt(1);
                        state.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyServiceStateForPhoneId(phoneId, subId, state);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifySignalStrengthForPhoneId(int phoneId, int subId, SignalStrength signalStrength) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    if (signalStrength != null) {
                        _data.writeInt(1);
                        signalStrength.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifySignalStrengthForPhoneId(phoneId, subId, signalStrength);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyMessageWaitingChangedForPhoneId(int phoneId, int subId, boolean mwi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    _data.writeInt(mwi ? 1 : 0);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyMessageWaitingChangedForPhoneId(phoneId, subId, mwi);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyCallForwardingChanged(boolean cfi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cfi ? 1 : 0);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyCallForwardingChanged(cfi);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyCallForwardingChangedForSubscriber(int subId, boolean cfi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(cfi ? 1 : 0);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyCallForwardingChangedForSubscriber(subId, cfi);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyDataActivity(int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyDataActivity(state);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyDataActivityForSubscriber(int subId, int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(state);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyDataActivityForSubscriber(subId, state);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyDataConnection(int state, boolean isDataConnectivityPossible, String apn, String apnType, LinkProperties linkProperties, NetworkCapabilities networkCapabilities, int networkType, boolean roaming) throws RemoteException {
                int i;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeInt(state);
                    i = 1;
                    _data.writeInt(isDataConnectivityPossible ? 1 : 0);
                } catch (Throwable th2) {
                    th = th2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
                try {
                    _data.writeString(apn);
                    _data.writeString(apnType);
                    if (linkProperties != null) {
                        _data.writeInt(1);
                        linkProperties.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (networkCapabilities != null) {
                        _data.writeInt(1);
                        networkCapabilities.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(networkType);
                    if (!roaming) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyDataConnection(state, isDataConnectivityPossible, apn, apnType, linkProperties, networkCapabilities, networkType, roaming);
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    _reply.readException();
                    _reply.recycle();
                    _data.recycle();
                } catch (Throwable th3) {
                    th = th3;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyDataConnectionForSubscriber(int phoneId, int subId, int state, boolean isDataConnectivityPossible, String apn, String apnType, LinkProperties linkProperties, NetworkCapabilities networkCapabilities, int networkType, boolean roaming) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    _data.writeInt(state);
                    int i = 1;
                    _data.writeInt(isDataConnectivityPossible ? 1 : 0);
                    _data.writeString(apn);
                    _data.writeString(apnType);
                    if (linkProperties != null) {
                        _data.writeInt(1);
                        linkProperties.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (networkCapabilities != null) {
                        _data.writeInt(1);
                        networkCapabilities.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(networkType);
                    if (!roaming) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyDataConnectionForSubscriber(phoneId, subId, state, isDataConnectivityPossible, apn, apnType, linkProperties, networkCapabilities, networkType, roaming);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyDataConnectionFailed(String apnType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(apnType);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyDataConnectionFailed(apnType);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyDataConnectionFailedForSubscriber(int phoneId, int subId, String apnType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    _data.writeString(apnType);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyDataConnectionFailedForSubscriber(phoneId, subId, apnType);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyCellLocation(Bundle cellLocation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (cellLocation != null) {
                        _data.writeInt(1);
                        cellLocation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyCellLocation(cellLocation);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyCellLocationForSubscriber(int subId, Bundle cellLocation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    if (cellLocation != null) {
                        _data.writeInt(1);
                        cellLocation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyCellLocationForSubscriber(subId, cellLocation);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyOtaspChanged(int subId, int otaspMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(otaspMode);
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyOtaspChanged(subId, otaspMode);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyCellInfo(List<CellInfo> cellInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeTypedList(cellInfo);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyCellInfo(cellInfo);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyPhysicalChannelConfigurationForSubscriber(int phoneId, int subId, List<PhysicalChannelConfig> configs) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    _data.writeTypedList(configs);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyPhysicalChannelConfigurationForSubscriber(phoneId, subId, configs);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyPreciseCallState(int phoneId, int subId, int ringingCallState, int foregroundCallState, int backgroundCallState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    _data.writeInt(ringingCallState);
                    _data.writeInt(foregroundCallState);
                    _data.writeInt(backgroundCallState);
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyPreciseCallState(phoneId, subId, ringingCallState, foregroundCallState, backgroundCallState);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyDisconnectCause(int phoneId, int subId, int disconnectCause, int preciseDisconnectCause) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    _data.writeInt(disconnectCause);
                    _data.writeInt(preciseDisconnectCause);
                    boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyDisconnectCause(phoneId, subId, disconnectCause, preciseDisconnectCause);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyPreciseDataConnectionFailed(int phoneId, int subId, String apnType, String apn, int failCause) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    _data.writeString(apnType);
                    _data.writeString(apn);
                    _data.writeInt(failCause);
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyPreciseDataConnectionFailed(phoneId, subId, apnType, apn, failCause);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyCellInfoForSubscriber(int subId, List<CellInfo> cellInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeTypedList(cellInfo);
                    boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyCellInfoForSubscriber(subId, cellInfo);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifySrvccStateChanged(int subId, int lteState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    _data.writeInt(lteState);
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifySrvccStateChanged(subId, lteState);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifySimActivationStateChangedForPhoneId(int phoneId, int subId, int activationState, int activationType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    _data.writeInt(activationState);
                    _data.writeInt(activationType);
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifySimActivationStateChangedForPhoneId(phoneId, subId, activationState, activationType);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyOemHookRawEventForSubscriber(int phoneId, int subId, byte[] rawData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    _data.writeByteArray(rawData);
                    boolean _status = this.mRemote.transact(30, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyOemHookRawEventForSubscriber(phoneId, subId, rawData);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifySubscriptionInfoChanged() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(31, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifySubscriptionInfoChanged();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyOpportunisticSubscriptionInfoChanged() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(32, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyOpportunisticSubscriptionInfoChanged();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyCarrierNetworkChange(boolean active) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(active ? 1 : 0);
                    boolean _status = this.mRemote.transact(33, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyCarrierNetworkChange(active);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyUserMobileDataStateChangedForPhoneId(int phoneId, int subId, boolean state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    _data.writeInt(state ? 1 : 0);
                    boolean _status = this.mRemote.transact(34, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyUserMobileDataStateChangedForPhoneId(phoneId, subId, state);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyPhoneCapabilityChanged(PhoneCapability capability) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (capability != null) {
                        _data.writeInt(1);
                        capability.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(35, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyPhoneCapabilityChanged(capability);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyActiveDataSubIdChanged(int activeDataSubId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(activeDataSubId);
                    boolean _status = this.mRemote.transact(36, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyActiveDataSubIdChanged(activeDataSubId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyRadioPowerStateChanged(int phoneId, int subId, int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    _data.writeInt(state);
                    boolean _status = this.mRemote.transact(37, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyRadioPowerStateChanged(phoneId, subId, state);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyEmergencyNumberList(int phoneId, int subId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    boolean _status = this.mRemote.transact(38, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyEmergencyNumberList(phoneId, subId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyCallQualityChanged(CallQuality callQuality, int phoneId, int subId, int callNetworkType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (callQuality != null) {
                        _data.writeInt(1);
                        callQuality.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(phoneId);
                    _data.writeInt(subId);
                    _data.writeInt(callNetworkType);
                    boolean _status = this.mRemote.transact(39, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyCallQualityChanged(callQuality, phoneId, subId, callNetworkType);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telephony.ITelephonyRegistry
            public void notifyImsDisconnectCause(int subId, ImsReasonInfo imsReasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(subId);
                    if (imsReasonInfo != null) {
                        _data.writeInt(1);
                        imsReasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(40, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyImsDisconnectCause(subId, imsReasonInfo);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ITelephonyRegistry impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static ITelephonyRegistry getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
