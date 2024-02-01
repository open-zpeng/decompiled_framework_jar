package com.android.internal.policy;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.TextUtils;
import com.android.internal.policy.IKeyguardDismissCallback;
import com.android.internal.policy.IKeyguardDrawnCallback;
import com.android.internal.policy.IKeyguardExitCallback;
import com.android.internal.policy.IKeyguardStateCallback;
/* loaded from: classes3.dex */
public interface IKeyguardService extends IInterface {
    synchronized void addStateMonitorCallback(IKeyguardStateCallback iKeyguardStateCallback) throws RemoteException;

    synchronized void dismiss(IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) throws RemoteException;

    private protected void doKeyguardTimeout(Bundle bundle) throws RemoteException;

    synchronized void onBootCompleted() throws RemoteException;

    synchronized void onDreamingStarted() throws RemoteException;

    synchronized void onDreamingStopped() throws RemoteException;

    synchronized void onFinishedGoingToSleep(int i, boolean z) throws RemoteException;

    synchronized void onFinishedWakingUp() throws RemoteException;

    synchronized void onScreenTurnedOff() throws RemoteException;

    synchronized void onScreenTurnedOn() throws RemoteException;

    synchronized void onScreenTurningOff() throws RemoteException;

    synchronized void onScreenTurningOn(IKeyguardDrawnCallback iKeyguardDrawnCallback) throws RemoteException;

    synchronized void onShortPowerPressedGoHome() throws RemoteException;

    synchronized void onStartedGoingToSleep(int i) throws RemoteException;

    synchronized void onStartedWakingUp() throws RemoteException;

    synchronized void onSystemReady() throws RemoteException;

    synchronized void setCurrentUser(int i) throws RemoteException;

    private protected void setKeyguardEnabled(boolean z) throws RemoteException;

    synchronized void setOccluded(boolean z, boolean z2) throws RemoteException;

    synchronized void setSwitchingUser(boolean z) throws RemoteException;

    synchronized void startKeyguardExitAnimation(long j, long j2) throws RemoteException;

    synchronized void verifyUnlock(IKeyguardExitCallback iKeyguardExitCallback) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IKeyguardService {
        private static final String DESCRIPTOR = "com.android.internal.policy.IKeyguardService";
        static final int TRANSACTION_addStateMonitorCallback = 2;
        static final int TRANSACTION_dismiss = 4;
        static final int TRANSACTION_doKeyguardTimeout = 17;
        static final int TRANSACTION_onBootCompleted = 20;
        static final int TRANSACTION_onDreamingStarted = 5;
        static final int TRANSACTION_onDreamingStopped = 6;
        static final int TRANSACTION_onFinishedGoingToSleep = 8;
        static final int TRANSACTION_onFinishedWakingUp = 10;
        static final int TRANSACTION_onScreenTurnedOff = 14;
        static final int TRANSACTION_onScreenTurnedOn = 12;
        static final int TRANSACTION_onScreenTurningOff = 13;
        static final int TRANSACTION_onScreenTurningOn = 11;
        static final int TRANSACTION_onShortPowerPressedGoHome = 22;
        static final int TRANSACTION_onStartedGoingToSleep = 7;
        static final int TRANSACTION_onStartedWakingUp = 9;
        static final int TRANSACTION_onSystemReady = 16;
        static final int TRANSACTION_setCurrentUser = 19;
        static final int TRANSACTION_setKeyguardEnabled = 15;
        static final int TRANSACTION_setOccluded = 1;
        static final int TRANSACTION_setSwitchingUser = 18;
        static final int TRANSACTION_startKeyguardExitAnimation = 21;
        static final int TRANSACTION_verifyUnlock = 3;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        private protected static IKeyguardService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IKeyguardService)) {
                return (IKeyguardService) iin;
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
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg0 = data.readInt() != 0;
                    _arg1 = data.readInt() != 0;
                    setOccluded(_arg0, _arg1);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    IKeyguardStateCallback _arg02 = IKeyguardStateCallback.Stub.asInterface(data.readStrongBinder());
                    addStateMonitorCallback(_arg02);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IKeyguardExitCallback _arg03 = IKeyguardExitCallback.Stub.asInterface(data.readStrongBinder());
                    verifyUnlock(_arg03);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    IKeyguardDismissCallback _arg04 = IKeyguardDismissCallback.Stub.asInterface(data.readStrongBinder());
                    dismiss(_arg04, data.readInt() != 0 ? TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data) : null);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    onDreamingStarted();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    onDreamingStopped();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    onStartedGoingToSleep(_arg05);
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    onFinishedGoingToSleep(_arg06, _arg1);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    onStartedWakingUp();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    onFinishedWakingUp();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    IKeyguardDrawnCallback _arg07 = IKeyguardDrawnCallback.Stub.asInterface(data.readStrongBinder());
                    onScreenTurningOn(_arg07);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    onScreenTurnedOn();
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    onScreenTurningOff();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    onScreenTurnedOff();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg08 = _arg1;
                    setKeyguardEnabled(_arg08);
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    onSystemReady();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    Bundle _arg09 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    doKeyguardTimeout(_arg09);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg010 = _arg1;
                    setSwitchingUser(_arg010);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg011 = data.readInt();
                    setCurrentUser(_arg011);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    onBootCompleted();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg012 = data.readLong();
                    startKeyguardExitAnimation(_arg012, data.readLong());
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    onShortPowerPressedGoHome();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes3.dex */
        private static class Proxy implements IKeyguardService {
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

            @Override // com.android.internal.policy.IKeyguardService
            public synchronized void setOccluded(boolean isOccluded, boolean animate) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isOccluded ? 1 : 0);
                    _data.writeInt(animate ? 1 : 0);
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.policy.IKeyguardService
            public synchronized void addStateMonitorCallback(IKeyguardStateCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.policy.IKeyguardService
            public synchronized void verifyUnlock(IKeyguardExitCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.policy.IKeyguardService
            public synchronized void dismiss(IKeyguardDismissCallback callback, CharSequence message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (message != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(message, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.policy.IKeyguardService
            public synchronized void onDreamingStarted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.policy.IKeyguardService
            public synchronized void onDreamingStopped() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.policy.IKeyguardService
            public synchronized void onStartedGoingToSleep(int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(reason);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.policy.IKeyguardService
            public synchronized void onFinishedGoingToSleep(int reason, boolean cameraGestureTriggered) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(reason);
                    _data.writeInt(cameraGestureTriggered ? 1 : 0);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.policy.IKeyguardService
            public synchronized void onStartedWakingUp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.policy.IKeyguardService
            public synchronized void onFinishedWakingUp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.policy.IKeyguardService
            public synchronized void onScreenTurningOn(IKeyguardDrawnCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.policy.IKeyguardService
            public synchronized void onScreenTurnedOn() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(12, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.policy.IKeyguardService
            public synchronized void onScreenTurningOff() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.policy.IKeyguardService
            public synchronized void onScreenTurnedOff() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(14, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void setKeyguardEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    this.mRemote.transact(15, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.policy.IKeyguardService
            public synchronized void onSystemReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(16, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public synchronized void doKeyguardTimeout(Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(17, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.policy.IKeyguardService
            public synchronized void setSwitchingUser(boolean switching) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(switching ? 1 : 0);
                    this.mRemote.transact(18, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.policy.IKeyguardService
            public synchronized void setCurrentUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(19, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.policy.IKeyguardService
            public synchronized void onBootCompleted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.policy.IKeyguardService
            public synchronized void startKeyguardExitAnimation(long startTime, long fadeoutDuration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(startTime);
                    _data.writeLong(fadeoutDuration);
                    this.mRemote.transact(21, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.policy.IKeyguardService
            public synchronized void onShortPowerPressedGoHome() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(22, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
