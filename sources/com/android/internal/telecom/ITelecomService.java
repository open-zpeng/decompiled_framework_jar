package com.android.internal.telecom;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomAnalytics;
import java.util.List;
/* loaded from: classes3.dex */
public interface ITelecomService extends IInterface {
    synchronized void acceptHandover(Uri uri, int i, PhoneAccountHandle phoneAccountHandle) throws RemoteException;

    synchronized void acceptRingingCall(String str) throws RemoteException;

    synchronized void acceptRingingCallWithVideoState(String str, int i) throws RemoteException;

    synchronized void addNewIncomingCall(PhoneAccountHandle phoneAccountHandle, Bundle bundle) throws RemoteException;

    synchronized void addNewUnknownCall(PhoneAccountHandle phoneAccountHandle, Bundle bundle) throws RemoteException;

    synchronized void cancelMissedCallsNotification(String str) throws RemoteException;

    synchronized void clearAccounts(String str) throws RemoteException;

    synchronized Intent createManageBlockedNumbersIntent() throws RemoteException;

    synchronized TelecomAnalytics dumpCallAnalytics() throws RemoteException;

    synchronized boolean enablePhoneAccount(PhoneAccountHandle phoneAccountHandle, boolean z) throws RemoteException;

    synchronized boolean endCall(String str) throws RemoteException;

    synchronized Uri getAdnUriForPhoneAccount(PhoneAccountHandle phoneAccountHandle, String str) throws RemoteException;

    synchronized List<PhoneAccountHandle> getAllPhoneAccountHandles() throws RemoteException;

    synchronized List<PhoneAccount> getAllPhoneAccounts() throws RemoteException;

    synchronized int getAllPhoneAccountsCount() throws RemoteException;

    synchronized List<PhoneAccountHandle> getCallCapablePhoneAccounts(boolean z, String str) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int getCallState() throws RemoteException;

    synchronized int getCurrentTtyMode(String str) throws RemoteException;

    synchronized String getDefaultDialerPackage() throws RemoteException;

    synchronized PhoneAccountHandle getDefaultOutgoingPhoneAccount(String str, String str2) throws RemoteException;

    synchronized ComponentName getDefaultPhoneApp() throws RemoteException;

    synchronized String getLine1Number(PhoneAccountHandle phoneAccountHandle, String str) throws RemoteException;

    synchronized PhoneAccount getPhoneAccount(PhoneAccountHandle phoneAccountHandle) throws RemoteException;

    synchronized List<PhoneAccountHandle> getPhoneAccountsForPackage(String str) throws RemoteException;

    synchronized List<PhoneAccountHandle> getPhoneAccountsSupportingScheme(String str, String str2) throws RemoteException;

    synchronized List<PhoneAccountHandle> getSelfManagedPhoneAccounts(String str) throws RemoteException;

    synchronized PhoneAccountHandle getSimCallManager() throws RemoteException;

    synchronized PhoneAccountHandle getSimCallManagerForUser(int i) throws RemoteException;

    synchronized String getSystemDialerPackage() throws RemoteException;

    synchronized PhoneAccountHandle getUserSelectedOutgoingPhoneAccount() throws RemoteException;

    synchronized String getVoiceMailNumber(PhoneAccountHandle phoneAccountHandle, String str) throws RemoteException;

    synchronized boolean handlePinMmi(String str, String str2) throws RemoteException;

    synchronized boolean handlePinMmiForPhoneAccount(PhoneAccountHandle phoneAccountHandle, String str, String str2) throws RemoteException;

    synchronized boolean isInCall(String str) throws RemoteException;

    synchronized boolean isInManagedCall(String str) throws RemoteException;

    synchronized boolean isIncomingCallPermitted(PhoneAccountHandle phoneAccountHandle) throws RemoteException;

    synchronized boolean isOutgoingCallPermitted(PhoneAccountHandle phoneAccountHandle) throws RemoteException;

    synchronized boolean isRinging(String str) throws RemoteException;

    synchronized boolean isTtySupported(String str) throws RemoteException;

    synchronized boolean isVoiceMailNumber(PhoneAccountHandle phoneAccountHandle, String str, String str2) throws RemoteException;

    synchronized void placeCall(Uri uri, Bundle bundle, String str) throws RemoteException;

    synchronized void registerPhoneAccount(PhoneAccount phoneAccount) throws RemoteException;

    synchronized boolean setDefaultDialer(String str) throws RemoteException;

    synchronized void setUserSelectedOutgoingPhoneAccount(PhoneAccountHandle phoneAccountHandle) throws RemoteException;

    synchronized void showInCallScreen(boolean z, String str) throws RemoteException;

    synchronized void silenceRinger(String str) throws RemoteException;

    synchronized void unregisterPhoneAccount(PhoneAccountHandle phoneAccountHandle) throws RemoteException;

    synchronized void waitOnHandlers() throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements ITelecomService {
        private static final String DESCRIPTOR = "com.android.internal.telecom.ITelecomService";
        static final int TRANSACTION_acceptHandover = 48;
        static final int TRANSACTION_acceptRingingCall = 31;
        static final int TRANSACTION_acceptRingingCallWithVideoState = 32;
        static final int TRANSACTION_addNewIncomingCall = 39;
        static final int TRANSACTION_addNewUnknownCall = 40;
        static final int TRANSACTION_cancelMissedCallsNotification = 33;
        static final int TRANSACTION_clearAccounts = 17;
        static final int TRANSACTION_createManageBlockedNumbersIntent = 44;
        static final int TRANSACTION_dumpCallAnalytics = 24;
        static final int TRANSACTION_enablePhoneAccount = 42;
        static final int TRANSACTION_endCall = 30;
        static final int TRANSACTION_getAdnUriForPhoneAccount = 36;
        static final int TRANSACTION_getAllPhoneAccountHandles = 12;
        static final int TRANSACTION_getAllPhoneAccounts = 11;
        static final int TRANSACTION_getAllPhoneAccountsCount = 10;
        static final int TRANSACTION_getCallCapablePhoneAccounts = 5;
        static final int TRANSACTION_getCallState = 29;
        static final int TRANSACTION_getCurrentTtyMode = 38;
        static final int TRANSACTION_getDefaultDialerPackage = 22;
        static final int TRANSACTION_getDefaultOutgoingPhoneAccount = 2;
        static final int TRANSACTION_getDefaultPhoneApp = 21;
        static final int TRANSACTION_getLine1Number = 20;
        static final int TRANSACTION_getPhoneAccount = 9;
        static final int TRANSACTION_getPhoneAccountsForPackage = 8;
        static final int TRANSACTION_getPhoneAccountsSupportingScheme = 7;
        static final int TRANSACTION_getSelfManagedPhoneAccounts = 6;
        static final int TRANSACTION_getSimCallManager = 13;
        static final int TRANSACTION_getSimCallManagerForUser = 14;
        static final int TRANSACTION_getSystemDialerPackage = 23;
        static final int TRANSACTION_getUserSelectedOutgoingPhoneAccount = 3;
        static final int TRANSACTION_getVoiceMailNumber = 19;
        static final int TRANSACTION_handlePinMmi = 34;
        static final int TRANSACTION_handlePinMmiForPhoneAccount = 35;
        static final int TRANSACTION_isInCall = 26;
        static final int TRANSACTION_isInManagedCall = 27;
        static final int TRANSACTION_isIncomingCallPermitted = 45;
        static final int TRANSACTION_isOutgoingCallPermitted = 46;
        static final int TRANSACTION_isRinging = 28;
        static final int TRANSACTION_isTtySupported = 37;
        static final int TRANSACTION_isVoiceMailNumber = 18;
        static final int TRANSACTION_placeCall = 41;
        static final int TRANSACTION_registerPhoneAccount = 15;
        static final int TRANSACTION_setDefaultDialer = 43;
        static final int TRANSACTION_setUserSelectedOutgoingPhoneAccount = 4;
        static final int TRANSACTION_showInCallScreen = 1;
        static final int TRANSACTION_silenceRinger = 25;
        static final int TRANSACTION_unregisterPhoneAccount = 16;
        static final int TRANSACTION_waitOnHandlers = 47;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static ITelecomService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ITelecomService)) {
                return (ITelecomService) iin;
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
            PhoneAccountHandle _arg0;
            PhoneAccountHandle _arg02;
            Uri _arg03;
            Uri _arg04;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    showInCallScreen(_arg1, data.readString());
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    PhoneAccountHandle _result = getDefaultOutgoingPhoneAccount(_arg05, data.readString());
                    reply.writeNoException();
                    if (_result != null) {
                        reply.writeInt(1);
                        _result.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    PhoneAccountHandle _result2 = getUserSelectedOutgoingPhoneAccount();
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    PhoneAccountHandle _arg06 = data.readInt() != 0 ? PhoneAccountHandle.CREATOR.createFromParcel(data) : null;
                    setUserSelectedOutgoingPhoneAccount(_arg06);
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    List<PhoneAccountHandle> _result3 = getCallCapablePhoneAccounts(_arg1, data.readString());
                    reply.writeNoException();
                    reply.writeTypedList(_result3);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    List<PhoneAccountHandle> _result4 = getSelfManagedPhoneAccounts(_arg07);
                    reply.writeNoException();
                    reply.writeTypedList(_result4);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    List<PhoneAccountHandle> _result5 = getPhoneAccountsSupportingScheme(_arg08, data.readString());
                    reply.writeNoException();
                    reply.writeTypedList(_result5);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg09 = data.readString();
                    List<PhoneAccountHandle> _result6 = getPhoneAccountsForPackage(_arg09);
                    reply.writeNoException();
                    reply.writeTypedList(_result6);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    PhoneAccountHandle _arg010 = data.readInt() != 0 ? PhoneAccountHandle.CREATOR.createFromParcel(data) : null;
                    PhoneAccount _result7 = getPhoneAccount(_arg010);
                    reply.writeNoException();
                    if (_result7 != null) {
                        reply.writeInt(1);
                        _result7.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    int _result8 = getAllPhoneAccountsCount();
                    reply.writeNoException();
                    reply.writeInt(_result8);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    List<PhoneAccount> _result9 = getAllPhoneAccounts();
                    reply.writeNoException();
                    reply.writeTypedList(_result9);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    List<PhoneAccountHandle> _result10 = getAllPhoneAccountHandles();
                    reply.writeNoException();
                    reply.writeTypedList(_result10);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    PhoneAccountHandle _result11 = getSimCallManager();
                    reply.writeNoException();
                    if (_result11 != null) {
                        reply.writeInt(1);
                        _result11.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg011 = data.readInt();
                    PhoneAccountHandle _result12 = getSimCallManagerForUser(_arg011);
                    reply.writeNoException();
                    if (_result12 != null) {
                        reply.writeInt(1);
                        _result12.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    PhoneAccount _arg012 = data.readInt() != 0 ? PhoneAccount.CREATOR.createFromParcel(data) : null;
                    registerPhoneAccount(_arg012);
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    PhoneAccountHandle _arg013 = data.readInt() != 0 ? PhoneAccountHandle.CREATOR.createFromParcel(data) : null;
                    unregisterPhoneAccount(_arg013);
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg014 = data.readString();
                    clearAccounts(_arg014);
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    PhoneAccountHandle _arg015 = data.readInt() != 0 ? PhoneAccountHandle.CREATOR.createFromParcel(data) : null;
                    PhoneAccountHandle _arg016 = _arg015;
                    String _arg12 = data.readString();
                    String _arg2 = data.readString();
                    boolean isVoiceMailNumber = isVoiceMailNumber(_arg016, _arg12, _arg2);
                    reply.writeNoException();
                    reply.writeInt(isVoiceMailNumber ? 1 : 0);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    PhoneAccountHandle _arg017 = data.readInt() != 0 ? PhoneAccountHandle.CREATOR.createFromParcel(data) : null;
                    String _result13 = getVoiceMailNumber(_arg017, data.readString());
                    reply.writeNoException();
                    reply.writeString(_result13);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    PhoneAccountHandle _arg018 = data.readInt() != 0 ? PhoneAccountHandle.CREATOR.createFromParcel(data) : null;
                    String _result14 = getLine1Number(_arg018, data.readString());
                    reply.writeNoException();
                    reply.writeString(_result14);
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _result15 = getDefaultPhoneApp();
                    reply.writeNoException();
                    if (_result15 != null) {
                        reply.writeInt(1);
                        _result15.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    String _result16 = getDefaultDialerPackage();
                    reply.writeNoException();
                    reply.writeString(_result16);
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    String _result17 = getSystemDialerPackage();
                    reply.writeNoException();
                    reply.writeString(_result17);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    TelecomAnalytics _result18 = dumpCallAnalytics();
                    reply.writeNoException();
                    if (_result18 != null) {
                        reply.writeInt(1);
                        _result18.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg019 = data.readString();
                    silenceRinger(_arg019);
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg020 = data.readString();
                    boolean isInCall = isInCall(_arg020);
                    reply.writeNoException();
                    reply.writeInt(isInCall ? 1 : 0);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg021 = data.readString();
                    boolean isInManagedCall = isInManagedCall(_arg021);
                    reply.writeNoException();
                    reply.writeInt(isInManagedCall ? 1 : 0);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg022 = data.readString();
                    boolean isRinging = isRinging(_arg022);
                    reply.writeNoException();
                    reply.writeInt(isRinging ? 1 : 0);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    int _result19 = getCallState();
                    reply.writeNoException();
                    reply.writeInt(_result19);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg023 = data.readString();
                    boolean endCall = endCall(_arg023);
                    reply.writeNoException();
                    reply.writeInt(endCall ? 1 : 0);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg024 = data.readString();
                    acceptRingingCall(_arg024);
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg025 = data.readString();
                    acceptRingingCallWithVideoState(_arg025, data.readInt());
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg026 = data.readString();
                    cancelMissedCallsNotification(_arg026);
                    reply.writeNoException();
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg027 = data.readString();
                    boolean handlePinMmi = handlePinMmi(_arg027, data.readString());
                    reply.writeNoException();
                    reply.writeInt(handlePinMmi ? 1 : 0);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    PhoneAccountHandle _arg028 = data.readInt() != 0 ? PhoneAccountHandle.CREATOR.createFromParcel(data) : null;
                    PhoneAccountHandle _arg029 = _arg028;
                    String _arg13 = data.readString();
                    String _arg22 = data.readString();
                    boolean handlePinMmiForPhoneAccount = handlePinMmiForPhoneAccount(_arg029, _arg13, _arg22);
                    reply.writeNoException();
                    reply.writeInt(handlePinMmiForPhoneAccount ? 1 : 0);
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    PhoneAccountHandle _arg030 = data.readInt() != 0 ? PhoneAccountHandle.CREATOR.createFromParcel(data) : null;
                    Uri _result20 = getAdnUriForPhoneAccount(_arg030, data.readString());
                    reply.writeNoException();
                    if (_result20 != null) {
                        reply.writeInt(1);
                        _result20.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg031 = data.readString();
                    boolean isTtySupported = isTtySupported(_arg031);
                    reply.writeNoException();
                    reply.writeInt(isTtySupported ? 1 : 0);
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg032 = data.readString();
                    int _result21 = getCurrentTtyMode(_arg032);
                    reply.writeNoException();
                    reply.writeInt(_result21);
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = PhoneAccountHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    Bundle _arg14 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    addNewIncomingCall(_arg0, _arg14);
                    reply.writeNoException();
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = PhoneAccountHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    Bundle _arg15 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    addNewUnknownCall(_arg02, _arg15);
                    reply.writeNoException();
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    Bundle _arg16 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    String _arg23 = data.readString();
                    placeCall(_arg03, _arg16, _arg23);
                    reply.writeNoException();
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    PhoneAccountHandle _arg033 = data.readInt() != 0 ? PhoneAccountHandle.CREATOR.createFromParcel(data) : null;
                    _arg1 = data.readInt() != 0;
                    boolean enablePhoneAccount = enablePhoneAccount(_arg033, _arg1);
                    reply.writeNoException();
                    reply.writeInt(enablePhoneAccount ? 1 : 0);
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg034 = data.readString();
                    boolean defaultDialer = setDefaultDialer(_arg034);
                    reply.writeNoException();
                    reply.writeInt(defaultDialer ? 1 : 0);
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    Intent _result22 = createManageBlockedNumbersIntent();
                    reply.writeNoException();
                    if (_result22 != null) {
                        reply.writeInt(1);
                        _result22.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    PhoneAccountHandle _arg035 = data.readInt() != 0 ? PhoneAccountHandle.CREATOR.createFromParcel(data) : null;
                    boolean isIncomingCallPermitted = isIncomingCallPermitted(_arg035);
                    reply.writeNoException();
                    reply.writeInt(isIncomingCallPermitted ? 1 : 0);
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    PhoneAccountHandle _arg036 = data.readInt() != 0 ? PhoneAccountHandle.CREATOR.createFromParcel(data) : null;
                    boolean isOutgoingCallPermitted = isOutgoingCallPermitted(_arg036);
                    reply.writeNoException();
                    reply.writeInt(isOutgoingCallPermitted ? 1 : 0);
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    waitOnHandlers();
                    reply.writeNoException();
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = Uri.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    int _arg17 = data.readInt();
                    PhoneAccountHandle _arg24 = data.readInt() != 0 ? PhoneAccountHandle.CREATOR.createFromParcel(data) : null;
                    acceptHandover(_arg04, _arg17, _arg24);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements ITelecomService {
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

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized void showInCallScreen(boolean showDialpad, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(showDialpad ? 1 : 0);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized PhoneAccountHandle getDefaultOutgoingPhoneAccount(String uriScheme, String callingPackage) throws RemoteException {
                PhoneAccountHandle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uriScheme);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = PhoneAccountHandle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized PhoneAccountHandle getUserSelectedOutgoingPhoneAccount() throws RemoteException {
                PhoneAccountHandle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = PhoneAccountHandle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized void setUserSelectedOutgoingPhoneAccount(PhoneAccountHandle account) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
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

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized List<PhoneAccountHandle> getCallCapablePhoneAccounts(boolean includeDisabledAccounts, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(includeDisabledAccounts ? 1 : 0);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    List<PhoneAccountHandle> _result = _reply.createTypedArrayList(PhoneAccountHandle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized List<PhoneAccountHandle> getSelfManagedPhoneAccounts(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    List<PhoneAccountHandle> _result = _reply.createTypedArrayList(PhoneAccountHandle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized List<PhoneAccountHandle> getPhoneAccountsSupportingScheme(String uriScheme, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uriScheme);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    List<PhoneAccountHandle> _result = _reply.createTypedArrayList(PhoneAccountHandle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized List<PhoneAccountHandle> getPhoneAccountsForPackage(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                    List<PhoneAccountHandle> _result = _reply.createTypedArrayList(PhoneAccountHandle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized PhoneAccount getPhoneAccount(PhoneAccountHandle account) throws RemoteException {
                PhoneAccount _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = PhoneAccount.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized int getAllPhoneAccountsCount() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized List<PhoneAccount> getAllPhoneAccounts() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    List<PhoneAccount> _result = _reply.createTypedArrayList(PhoneAccount.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized List<PhoneAccountHandle> getAllPhoneAccountHandles() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    List<PhoneAccountHandle> _result = _reply.createTypedArrayList(PhoneAccountHandle.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized PhoneAccountHandle getSimCallManager() throws RemoteException {
                PhoneAccountHandle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = PhoneAccountHandle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized PhoneAccountHandle getSimCallManagerForUser(int userId) throws RemoteException {
                PhoneAccountHandle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = PhoneAccountHandle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized void registerPhoneAccount(PhoneAccount metadata) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (metadata != null) {
                        _data.writeInt(1);
                        metadata.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized void unregisterPhoneAccount(PhoneAccountHandle account) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized void clearAccounts(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized boolean isVoiceMailNumber(PhoneAccountHandle accountHandle, String number, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (accountHandle != null) {
                        _data.writeInt(1);
                        accountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(number);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized String getVoiceMailNumber(PhoneAccountHandle accountHandle, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (accountHandle != null) {
                        _data.writeInt(1);
                        accountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized String getLine1Number(PhoneAccountHandle accountHandle, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (accountHandle != null) {
                        _data.writeInt(1);
                        accountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized ComponentName getDefaultPhoneApp() throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ComponentName.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized String getDefaultDialerPackage() throws RemoteException {
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

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized String getSystemDialerPackage() throws RemoteException {
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

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized TelecomAnalytics dumpCallAnalytics() throws RemoteException {
                TelecomAnalytics _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = TelecomAnalytics.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized void silenceRinger(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized boolean isInCall(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized boolean isInManagedCall(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized boolean isRinging(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int getCallState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized boolean endCall(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized void acceptRingingCall(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized void acceptRingingCallWithVideoState(String callingPackage, int videoState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(videoState);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized void cancelMissedCallsNotification(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized boolean handlePinMmi(String dialString, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(dialString);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized boolean handlePinMmiForPhoneAccount(PhoneAccountHandle accountHandle, String dialString, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (accountHandle != null) {
                        _data.writeInt(1);
                        accountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(dialString);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized Uri getAdnUriForPhoneAccount(PhoneAccountHandle accountHandle, String callingPackage) throws RemoteException {
                Uri _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (accountHandle != null) {
                        _data.writeInt(1);
                        accountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Uri.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized boolean isTtySupported(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized int getCurrentTtyMode(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized void addNewIncomingCall(PhoneAccountHandle phoneAccount, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (phoneAccount != null) {
                        _data.writeInt(1);
                        phoneAccount.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized void addNewUnknownCall(PhoneAccountHandle phoneAccount, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (phoneAccount != null) {
                        _data.writeInt(1);
                        phoneAccount.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized void placeCall(Uri handle, Bundle extras, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (handle != null) {
                        _data.writeInt(1);
                        handle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized boolean enablePhoneAccount(PhoneAccountHandle accountHandle, boolean isEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (accountHandle != null) {
                        _data.writeInt(1);
                        accountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(isEnabled ? 1 : 0);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized boolean setDefaultDialer(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized Intent createManageBlockedNumbersIntent() throws RemoteException {
                Intent _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = Intent.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized boolean isIncomingCallPermitted(PhoneAccountHandle phoneAccountHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (phoneAccountHandle != null) {
                        _data.writeInt(1);
                        phoneAccountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized boolean isOutgoingCallPermitted(PhoneAccountHandle phoneAccountHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (phoneAccountHandle != null) {
                        _data.writeInt(1);
                        phoneAccountHandle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized void waitOnHandlers() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.telecom.ITelecomService
            public synchronized void acceptHandover(Uri srcAddr, int videoState, PhoneAccountHandle destAcct) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (srcAddr != null) {
                        _data.writeInt(1);
                        srcAddr.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(videoState);
                    if (destAcct != null) {
                        _data.writeInt(1);
                        destAcct.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
