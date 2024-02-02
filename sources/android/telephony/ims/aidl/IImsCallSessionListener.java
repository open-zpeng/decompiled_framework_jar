package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.ims.ImsCallProfile;
import android.telephony.ims.ImsConferenceState;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsStreamMediaProfile;
import android.telephony.ims.ImsSuppServiceNotification;
import com.android.ims.internal.IImsCallSession;
/* loaded from: classes2.dex */
public interface IImsCallSessionListener extends IInterface {
    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionConferenceExtendFailed(ImsReasonInfo imsReasonInfo) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionConferenceExtendReceived(IImsCallSession iImsCallSession, ImsCallProfile imsCallProfile) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionConferenceExtended(IImsCallSession iImsCallSession, ImsCallProfile imsCallProfile) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionConferenceStateUpdated(ImsConferenceState imsConferenceState) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionHandover(int i, int i2, ImsReasonInfo imsReasonInfo) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionHandoverFailed(int i, int i2, ImsReasonInfo imsReasonInfo) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionHeld(ImsCallProfile imsCallProfile) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionHoldFailed(ImsReasonInfo imsReasonInfo) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionHoldReceived(ImsCallProfile imsCallProfile) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionInitiated(ImsCallProfile imsCallProfile) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionInitiatedFailed(ImsReasonInfo imsReasonInfo) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionInviteParticipantsRequestDelivered() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionInviteParticipantsRequestFailed(ImsReasonInfo imsReasonInfo) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionMayHandover(int i, int i2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionMergeComplete(IImsCallSession iImsCallSession) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionMergeFailed(ImsReasonInfo imsReasonInfo) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionMergeStarted(IImsCallSession iImsCallSession, ImsCallProfile imsCallProfile) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionMultipartyStateChanged(boolean z) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionProgressing(ImsStreamMediaProfile imsStreamMediaProfile) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionRemoveParticipantsRequestDelivered() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionRemoveParticipantsRequestFailed(ImsReasonInfo imsReasonInfo) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionResumeFailed(ImsReasonInfo imsReasonInfo) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionResumeReceived(ImsCallProfile imsCallProfile) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionResumed(ImsCallProfile imsCallProfile) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionRttMessageReceived(String str) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionRttModifyRequestReceived(ImsCallProfile imsCallProfile) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionRttModifyResponseReceived(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionSuppServiceReceived(ImsSuppServiceNotification imsSuppServiceNotification) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionTerminated(ImsReasonInfo imsReasonInfo) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionTtyModeReceived(int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionUpdateFailed(ImsReasonInfo imsReasonInfo) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionUpdateReceived(ImsCallProfile imsCallProfile) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionUpdated(ImsCallProfile imsCallProfile) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void callSessionUssdMessageReceived(int i, String str) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IImsCallSessionListener {
        public protected static final String DESCRIPTOR = "android.telephony.ims.aidl.IImsCallSessionListener";
        public private protected static final int TRANSACTION_callSessionConferenceExtendFailed = 18;
        public private protected static final int TRANSACTION_callSessionConferenceExtendReceived = 19;
        public private protected static final int TRANSACTION_callSessionConferenceExtended = 17;
        public private protected static final int TRANSACTION_callSessionConferenceStateUpdated = 24;
        public private protected static final int TRANSACTION_callSessionHandover = 26;
        public private protected static final int TRANSACTION_callSessionHandoverFailed = 27;
        public private protected static final int TRANSACTION_callSessionHeld = 5;
        public private protected static final int TRANSACTION_callSessionHoldFailed = 6;
        public private protected static final int TRANSACTION_callSessionHoldReceived = 7;
        public private protected static final int TRANSACTION_callSessionInitiated = 2;
        public private protected static final int TRANSACTION_callSessionInitiatedFailed = 3;
        public private protected static final int TRANSACTION_callSessionInviteParticipantsRequestDelivered = 20;
        public private protected static final int TRANSACTION_callSessionInviteParticipantsRequestFailed = 21;
        public private protected static final int TRANSACTION_callSessionMayHandover = 28;
        public private protected static final int TRANSACTION_callSessionMergeComplete = 12;
        public private protected static final int TRANSACTION_callSessionMergeFailed = 13;
        public private protected static final int TRANSACTION_callSessionMergeStarted = 11;
        public private protected static final int TRANSACTION_callSessionMultipartyStateChanged = 30;
        public private protected static final int TRANSACTION_callSessionProgressing = 1;
        public private protected static final int TRANSACTION_callSessionRemoveParticipantsRequestDelivered = 22;
        public private protected static final int TRANSACTION_callSessionRemoveParticipantsRequestFailed = 23;
        public private protected static final int TRANSACTION_callSessionResumeFailed = 9;
        public private protected static final int TRANSACTION_callSessionResumeReceived = 10;
        public private protected static final int TRANSACTION_callSessionResumed = 8;
        public private protected static final int TRANSACTION_callSessionRttMessageReceived = 34;
        public private protected static final int TRANSACTION_callSessionRttModifyRequestReceived = 32;
        public private protected static final int TRANSACTION_callSessionRttModifyResponseReceived = 33;
        public private protected static final int TRANSACTION_callSessionSuppServiceReceived = 31;
        public private protected static final int TRANSACTION_callSessionTerminated = 4;
        public private protected static final int TRANSACTION_callSessionTtyModeReceived = 29;
        public private protected static final int TRANSACTION_callSessionUpdateFailed = 15;
        public private protected static final int TRANSACTION_callSessionUpdateReceived = 16;
        public private protected static final int TRANSACTION_callSessionUpdated = 14;
        public private protected static final int TRANSACTION_callSessionUssdMessageReceived = 25;

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized IImsCallSessionListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IImsCallSessionListener)) {
                return (IImsCallSessionListener) iin;
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
                    ImsStreamMediaProfile _arg0 = data.readInt() != 0 ? ImsStreamMediaProfile.CREATOR.createFromParcel(data) : null;
                    callSessionProgressing(_arg0);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    ImsCallProfile _arg02 = data.readInt() != 0 ? ImsCallProfile.CREATOR.createFromParcel(data) : null;
                    callSessionInitiated(_arg02);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    ImsReasonInfo _arg03 = data.readInt() != 0 ? ImsReasonInfo.CREATOR.createFromParcel(data) : null;
                    callSessionInitiatedFailed(_arg03);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    ImsReasonInfo _arg04 = data.readInt() != 0 ? ImsReasonInfo.CREATOR.createFromParcel(data) : null;
                    callSessionTerminated(_arg04);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    ImsCallProfile _arg05 = data.readInt() != 0 ? ImsCallProfile.CREATOR.createFromParcel(data) : null;
                    callSessionHeld(_arg05);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    ImsReasonInfo _arg06 = data.readInt() != 0 ? ImsReasonInfo.CREATOR.createFromParcel(data) : null;
                    callSessionHoldFailed(_arg06);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    ImsCallProfile _arg07 = data.readInt() != 0 ? ImsCallProfile.CREATOR.createFromParcel(data) : null;
                    callSessionHoldReceived(_arg07);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    ImsCallProfile _arg08 = data.readInt() != 0 ? ImsCallProfile.CREATOR.createFromParcel(data) : null;
                    callSessionResumed(_arg08);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    ImsReasonInfo _arg09 = data.readInt() != 0 ? ImsReasonInfo.CREATOR.createFromParcel(data) : null;
                    callSessionResumeFailed(_arg09);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    ImsCallProfile _arg010 = data.readInt() != 0 ? ImsCallProfile.CREATOR.createFromParcel(data) : null;
                    callSessionResumeReceived(_arg010);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    IImsCallSession _arg011 = IImsCallSession.Stub.asInterface(data.readStrongBinder());
                    ImsCallProfile _arg1 = data.readInt() != 0 ? ImsCallProfile.CREATOR.createFromParcel(data) : null;
                    callSessionMergeStarted(_arg011, _arg1);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    IImsCallSession _arg012 = IImsCallSession.Stub.asInterface(data.readStrongBinder());
                    callSessionMergeComplete(_arg012);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    ImsReasonInfo _arg013 = data.readInt() != 0 ? ImsReasonInfo.CREATOR.createFromParcel(data) : null;
                    callSessionMergeFailed(_arg013);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    ImsCallProfile _arg014 = data.readInt() != 0 ? ImsCallProfile.CREATOR.createFromParcel(data) : null;
                    callSessionUpdated(_arg014);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    ImsReasonInfo _arg015 = data.readInt() != 0 ? ImsReasonInfo.CREATOR.createFromParcel(data) : null;
                    callSessionUpdateFailed(_arg015);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    ImsCallProfile _arg016 = data.readInt() != 0 ? ImsCallProfile.CREATOR.createFromParcel(data) : null;
                    callSessionUpdateReceived(_arg016);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    IImsCallSession _arg017 = IImsCallSession.Stub.asInterface(data.readStrongBinder());
                    ImsCallProfile _arg12 = data.readInt() != 0 ? ImsCallProfile.CREATOR.createFromParcel(data) : null;
                    callSessionConferenceExtended(_arg017, _arg12);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    ImsReasonInfo _arg018 = data.readInt() != 0 ? ImsReasonInfo.CREATOR.createFromParcel(data) : null;
                    callSessionConferenceExtendFailed(_arg018);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    IImsCallSession _arg019 = IImsCallSession.Stub.asInterface(data.readStrongBinder());
                    ImsCallProfile _arg13 = data.readInt() != 0 ? ImsCallProfile.CREATOR.createFromParcel(data) : null;
                    callSessionConferenceExtendReceived(_arg019, _arg13);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    callSessionInviteParticipantsRequestDelivered();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    ImsReasonInfo _arg020 = data.readInt() != 0 ? ImsReasonInfo.CREATOR.createFromParcel(data) : null;
                    callSessionInviteParticipantsRequestFailed(_arg020);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    callSessionRemoveParticipantsRequestDelivered();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    ImsReasonInfo _arg021 = data.readInt() != 0 ? ImsReasonInfo.CREATOR.createFromParcel(data) : null;
                    callSessionRemoveParticipantsRequestFailed(_arg021);
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    ImsConferenceState _arg022 = data.readInt() != 0 ? ImsConferenceState.CREATOR.createFromParcel(data) : null;
                    callSessionConferenceStateUpdated(_arg022);
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg023 = data.readInt();
                    String _arg14 = data.readString();
                    callSessionUssdMessageReceived(_arg023, _arg14);
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg024 = data.readInt();
                    int _arg15 = data.readInt();
                    ImsReasonInfo _arg2 = data.readInt() != 0 ? ImsReasonInfo.CREATOR.createFromParcel(data) : null;
                    callSessionHandover(_arg024, _arg15, _arg2);
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg025 = data.readInt();
                    int _arg16 = data.readInt();
                    ImsReasonInfo _arg22 = data.readInt() != 0 ? ImsReasonInfo.CREATOR.createFromParcel(data) : null;
                    callSessionHandoverFailed(_arg025, _arg16, _arg22);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg026 = data.readInt();
                    int _arg17 = data.readInt();
                    callSessionMayHandover(_arg026, _arg17);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg027 = data.readInt();
                    callSessionTtyModeReceived(_arg027);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg028 = data.readInt() != 0;
                    callSessionMultipartyStateChanged(_arg028);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    ImsSuppServiceNotification _arg029 = data.readInt() != 0 ? ImsSuppServiceNotification.CREATOR.createFromParcel(data) : null;
                    callSessionSuppServiceReceived(_arg029);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    ImsCallProfile _arg030 = data.readInt() != 0 ? ImsCallProfile.CREATOR.createFromParcel(data) : null;
                    callSessionRttModifyRequestReceived(_arg030);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg031 = data.readInt();
                    callSessionRttModifyResponseReceived(_arg031);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg032 = data.readString();
                    callSessionRttMessageReceived(_arg032);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes2.dex */
        private static class Proxy implements IImsCallSessionListener {
            public protected IBinder mRemote;

            public private protected synchronized Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            private protected synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            private protected synchronized void callSessionProgressing(ImsStreamMediaProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionInitiated(ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionInitiatedFailed(ImsReasonInfo reasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (reasonInfo != null) {
                        _data.writeInt(1);
                        reasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionTerminated(ImsReasonInfo reasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (reasonInfo != null) {
                        _data.writeInt(1);
                        reasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionHeld(ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionHoldFailed(ImsReasonInfo reasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (reasonInfo != null) {
                        _data.writeInt(1);
                        reasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionHoldReceived(ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionResumed(ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionResumeFailed(ImsReasonInfo reasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (reasonInfo != null) {
                        _data.writeInt(1);
                        reasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionResumeReceived(ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionMergeStarted(IImsCallSession newSession, ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(newSession != null ? newSession.asBinder() : null);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionMergeComplete(IImsCallSession session) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    this.mRemote.transact(12, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionMergeFailed(ImsReasonInfo reasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (reasonInfo != null) {
                        _data.writeInt(1);
                        reasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(13, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionUpdated(ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(14, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionUpdateFailed(ImsReasonInfo reasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (reasonInfo != null) {
                        _data.writeInt(1);
                        reasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(15, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionUpdateReceived(ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(16, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionConferenceExtended(IImsCallSession newSession, ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(newSession != null ? newSession.asBinder() : null);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(17, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionConferenceExtendFailed(ImsReasonInfo reasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (reasonInfo != null) {
                        _data.writeInt(1);
                        reasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(18, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionConferenceExtendReceived(IImsCallSession newSession, ImsCallProfile profile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(newSession != null ? newSession.asBinder() : null);
                    if (profile != null) {
                        _data.writeInt(1);
                        profile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(19, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionInviteParticipantsRequestDelivered() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionInviteParticipantsRequestFailed(ImsReasonInfo reasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (reasonInfo != null) {
                        _data.writeInt(1);
                        reasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(21, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionRemoveParticipantsRequestDelivered() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(22, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionRemoveParticipantsRequestFailed(ImsReasonInfo reasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (reasonInfo != null) {
                        _data.writeInt(1);
                        reasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(23, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionConferenceStateUpdated(ImsConferenceState state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (state != null) {
                        _data.writeInt(1);
                        state.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(24, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionUssdMessageReceived(int mode, String ussdMessage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeString(ussdMessage);
                    this.mRemote.transact(25, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionHandover(int srcAccessTech, int targetAccessTech, ImsReasonInfo reasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(srcAccessTech);
                    _data.writeInt(targetAccessTech);
                    if (reasonInfo != null) {
                        _data.writeInt(1);
                        reasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(26, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionHandoverFailed(int srcAccessTech, int targetAccessTech, ImsReasonInfo reasonInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(srcAccessTech);
                    _data.writeInt(targetAccessTech);
                    if (reasonInfo != null) {
                        _data.writeInt(1);
                        reasonInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(27, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionMayHandover(int srcAccessTech, int targetAccessTech) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(srcAccessTech);
                    _data.writeInt(targetAccessTech);
                    this.mRemote.transact(28, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionTtyModeReceived(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    this.mRemote.transact(29, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionMultipartyStateChanged(boolean isMultiParty) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isMultiParty ? 1 : 0);
                    this.mRemote.transact(30, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionSuppServiceReceived(ImsSuppServiceNotification suppSrvNotification) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (suppSrvNotification != null) {
                        _data.writeInt(1);
                        suppSrvNotification.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(31, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionRttModifyRequestReceived(ImsCallProfile callProfile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (callProfile != null) {
                        _data.writeInt(1);
                        callProfile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(32, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionRttModifyResponseReceived(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    this.mRemote.transact(33, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            private protected synchronized void callSessionRttMessageReceived(String rttMessage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(rttMessage);
                    this.mRemote.transact(34, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
