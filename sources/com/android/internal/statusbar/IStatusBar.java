package com.android.internal.statusbar;

import android.content.ComponentName;
import android.graphics.Rect;
import android.hardware.biometrics.IBiometricPromptReceiver;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes3.dex */
public interface IStatusBar extends IInterface {
    synchronized void addQsTile(ComponentName componentName) throws RemoteException;

    synchronized void animateCollapsePanels() throws RemoteException;

    synchronized void animateExpandNotificationsPanel() throws RemoteException;

    synchronized void animateExpandSettingsPanel(String str) throws RemoteException;

    synchronized void appTransitionCancelled() throws RemoteException;

    synchronized void appTransitionFinished() throws RemoteException;

    synchronized void appTransitionPending() throws RemoteException;

    synchronized void appTransitionStarting(long j, long j2) throws RemoteException;

    synchronized void cancelPreloadRecentApps() throws RemoteException;

    synchronized void clickQsTile(ComponentName componentName) throws RemoteException;

    synchronized void disable(int i, int i2) throws RemoteException;

    synchronized void dismissKeyboardShortcutsMenu() throws RemoteException;

    synchronized void handleSystemKey(int i) throws RemoteException;

    synchronized void hideFingerprintDialog() throws RemoteException;

    synchronized void hideRecentApps(boolean z, boolean z2) throws RemoteException;

    synchronized void onCameraLaunchGestureDetected(int i) throws RemoteException;

    synchronized void onFingerprintAuthenticated() throws RemoteException;

    synchronized void onFingerprintError(String str) throws RemoteException;

    synchronized void onFingerprintHelp(String str) throws RemoteException;

    synchronized void onProposedRotationChanged(int i, boolean z) throws RemoteException;

    synchronized void preloadRecentApps() throws RemoteException;

    synchronized void remQsTile(ComponentName componentName) throws RemoteException;

    synchronized void removeIcon(String str) throws RemoteException;

    synchronized void setIcon(String str, StatusBarIcon statusBarIcon) throws RemoteException;

    synchronized void setImeWindowStatus(IBinder iBinder, int i, int i2, boolean z) throws RemoteException;

    synchronized void setSystemUiVisibility(int i, int i2, int i3, int i4, Rect rect, Rect rect2) throws RemoteException;

    synchronized void setTopAppHidesStatusBar(boolean z) throws RemoteException;

    synchronized void setWindowState(int i, int i2) throws RemoteException;

    synchronized void showAssistDisclosure() throws RemoteException;

    synchronized void showFingerprintDialog(Bundle bundle, IBiometricPromptReceiver iBiometricPromptReceiver) throws RemoteException;

    synchronized void showGlobalActionsMenu() throws RemoteException;

    synchronized void showPictureInPictureMenu() throws RemoteException;

    synchronized void showPinningEnterExitToast(boolean z) throws RemoteException;

    synchronized void showPinningEscapeToast() throws RemoteException;

    synchronized void showRecentApps(boolean z) throws RemoteException;

    synchronized void showScreenPinningRequest(int i) throws RemoteException;

    synchronized void showShutdownUi(boolean z, String str) throws RemoteException;

    synchronized void showWirelessChargingAnimation(int i) throws RemoteException;

    synchronized void startAssist(Bundle bundle) throws RemoteException;

    synchronized void toggleKeyboardShortcutsMenu(int i) throws RemoteException;

    synchronized void togglePanel() throws RemoteException;

    synchronized void toggleRecentApps() throws RemoteException;

    synchronized void toggleSplitScreen() throws RemoteException;

    synchronized void topAppWindowChanged(boolean z) throws RemoteException;

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IStatusBar {
        private static final String DESCRIPTOR = "com.android.internal.statusbar.IStatusBar";
        static final int TRANSACTION_addQsTile = 33;
        static final int TRANSACTION_animateCollapsePanels = 6;
        static final int TRANSACTION_animateExpandNotificationsPanel = 4;
        static final int TRANSACTION_animateExpandSettingsPanel = 5;
        static final int TRANSACTION_appTransitionCancelled = 23;
        static final int TRANSACTION_appTransitionFinished = 25;
        static final int TRANSACTION_appTransitionPending = 22;
        static final int TRANSACTION_appTransitionStarting = 24;
        static final int TRANSACTION_cancelPreloadRecentApps = 18;
        static final int TRANSACTION_clickQsTile = 35;
        static final int TRANSACTION_disable = 3;
        static final int TRANSACTION_dismissKeyboardShortcutsMenu = 20;
        static final int TRANSACTION_handleSystemKey = 36;
        static final int TRANSACTION_hideFingerprintDialog = 44;
        static final int TRANSACTION_hideRecentApps = 14;
        static final int TRANSACTION_onCameraLaunchGestureDetected = 28;
        static final int TRANSACTION_onFingerprintAuthenticated = 41;
        static final int TRANSACTION_onFingerprintError = 43;
        static final int TRANSACTION_onFingerprintHelp = 42;
        static final int TRANSACTION_onProposedRotationChanged = 31;
        static final int TRANSACTION_preloadRecentApps = 17;
        static final int TRANSACTION_remQsTile = 34;
        static final int TRANSACTION_removeIcon = 2;
        static final int TRANSACTION_setIcon = 1;
        static final int TRANSACTION_setImeWindowStatus = 11;
        static final int TRANSACTION_setSystemUiVisibility = 9;
        static final int TRANSACTION_setTopAppHidesStatusBar = 32;
        static final int TRANSACTION_setWindowState = 12;
        static final int TRANSACTION_showAssistDisclosure = 26;
        static final int TRANSACTION_showFingerprintDialog = 40;
        static final int TRANSACTION_showGlobalActionsMenu = 30;
        static final int TRANSACTION_showPictureInPictureMenu = 29;
        static final int TRANSACTION_showPinningEnterExitToast = 37;
        static final int TRANSACTION_showPinningEscapeToast = 38;
        static final int TRANSACTION_showRecentApps = 13;
        static final int TRANSACTION_showScreenPinningRequest = 19;
        static final int TRANSACTION_showShutdownUi = 39;
        static final int TRANSACTION_showWirelessChargingAnimation = 8;
        static final int TRANSACTION_startAssist = 27;
        static final int TRANSACTION_toggleKeyboardShortcutsMenu = 21;
        static final int TRANSACTION_togglePanel = 7;
        static final int TRANSACTION_toggleRecentApps = 15;
        static final int TRANSACTION_toggleSplitScreen = 16;
        static final int TRANSACTION_topAppWindowChanged = 10;

        public synchronized Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static IStatusBar asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IStatusBar)) {
                return (IStatusBar) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Rect _arg4;
            boolean _arg1;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0 = data.readString();
                    setIcon(_arg0, data.readInt() != 0 ? StatusBarIcon.CREATOR.createFromParcel(data) : null);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    removeIcon(_arg02);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    disable(_arg03, data.readInt());
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    animateExpandNotificationsPanel();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    animateExpandSettingsPanel(_arg04);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    animateCollapsePanels();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    togglePanel();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    showWirelessChargingAnimation(_arg05);
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    int _arg12 = data.readInt();
                    int _arg2 = data.readInt();
                    int _arg3 = data.readInt();
                    if (data.readInt() != 0) {
                        Rect _arg42 = Rect.CREATOR.createFromParcel(data);
                        _arg4 = _arg42;
                    } else {
                        _arg4 = null;
                    }
                    Rect _arg5 = data.readInt() != 0 ? Rect.CREATOR.createFromParcel(data) : null;
                    setSystemUiVisibility(_arg06, _arg12, _arg2, _arg3, _arg4, _arg5);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg07 = _arg1;
                    topAppWindowChanged(_arg07);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg08 = data.readStrongBinder();
                    int _arg13 = data.readInt();
                    int _arg22 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    setImeWindowStatus(_arg08, _arg13, _arg22, _arg1);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg09 = data.readInt();
                    setWindowState(_arg09, data.readInt());
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg010 = _arg1;
                    showRecentApps(_arg010);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _arg011 = data.readInt() != 0;
                    _arg1 = data.readInt() != 0;
                    hideRecentApps(_arg011, _arg1);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    toggleRecentApps();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    toggleSplitScreen();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    preloadRecentApps();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    cancelPreloadRecentApps();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg012 = data.readInt();
                    showScreenPinningRequest(_arg012);
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    dismissKeyboardShortcutsMenu();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg013 = data.readInt();
                    toggleKeyboardShortcutsMenu(_arg013);
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    appTransitionPending();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    appTransitionCancelled();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    long _arg014 = data.readLong();
                    appTransitionStarting(_arg014, data.readLong());
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    appTransitionFinished();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    showAssistDisclosure();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    Bundle _arg015 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    startAssist(_arg015);
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg016 = data.readInt();
                    onCameraLaunchGestureDetected(_arg016);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    showPictureInPictureMenu();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    showGlobalActionsMenu();
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg017 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    onProposedRotationChanged(_arg017, _arg1);
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg018 = _arg1;
                    setTopAppHidesStatusBar(_arg018);
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg019 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    addQsTile(_arg019);
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg020 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    remQsTile(_arg020);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg021 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    clickQsTile(_arg021);
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg022 = data.readInt();
                    handleSystemKey(_arg022);
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg023 = _arg1;
                    showPinningEnterExitToast(_arg023);
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    showPinningEscapeToast();
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg024 = _arg1;
                    showShutdownUi(_arg024, data.readString());
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    Bundle _arg025 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    showFingerprintDialog(_arg025, IBiometricPromptReceiver.Stub.asInterface(data.readStrongBinder()));
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    onFingerprintAuthenticated();
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg026 = data.readString();
                    onFingerprintHelp(_arg026);
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg027 = data.readString();
                    onFingerprintError(_arg027);
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    hideFingerprintDialog();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* loaded from: classes3.dex */
        private static class Proxy implements IStatusBar {
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

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void setIcon(String slot, StatusBarIcon icon) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(slot);
                    if (icon != null) {
                        _data.writeInt(1);
                        icon.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void removeIcon(String slot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(slot);
                    this.mRemote.transact(2, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void disable(int state1, int state2) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(state1);
                    _data.writeInt(state2);
                    this.mRemote.transact(3, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void animateExpandNotificationsPanel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(4, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void animateExpandSettingsPanel(String subPanel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(subPanel);
                    this.mRemote.transact(5, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void animateCollapsePanels() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void togglePanel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void showWirelessChargingAnimation(int batteryLevel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(batteryLevel);
                    this.mRemote.transact(8, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void setSystemUiVisibility(int vis, int fullscreenStackVis, int dockedStackVis, int mask, Rect fullscreenBounds, Rect dockedBounds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(vis);
                    _data.writeInt(fullscreenStackVis);
                    _data.writeInt(dockedStackVis);
                    _data.writeInt(mask);
                    if (fullscreenBounds != null) {
                        _data.writeInt(1);
                        fullscreenBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (dockedBounds != null) {
                        _data.writeInt(1);
                        dockedBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(9, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void topAppWindowChanged(boolean menuVisible) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(menuVisible ? 1 : 0);
                    this.mRemote.transact(10, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void setImeWindowStatus(IBinder token, int vis, int backDisposition, boolean showImeSwitcher) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(vis);
                    _data.writeInt(backDisposition);
                    _data.writeInt(showImeSwitcher ? 1 : 0);
                    this.mRemote.transact(11, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void setWindowState(int window, int state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(window);
                    _data.writeInt(state);
                    this.mRemote.transact(12, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void showRecentApps(boolean triggeredFromAltTab) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(triggeredFromAltTab ? 1 : 0);
                    this.mRemote.transact(13, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void hideRecentApps(boolean triggeredFromAltTab, boolean triggeredFromHomeKey) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(triggeredFromAltTab ? 1 : 0);
                    _data.writeInt(triggeredFromHomeKey ? 1 : 0);
                    this.mRemote.transact(14, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void toggleRecentApps() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(15, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void toggleSplitScreen() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(16, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void preloadRecentApps() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(17, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void cancelPreloadRecentApps() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(18, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void showScreenPinningRequest(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    this.mRemote.transact(19, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void dismissKeyboardShortcutsMenu() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(20, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void toggleKeyboardShortcutsMenu(int deviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(deviceId);
                    this.mRemote.transact(21, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void appTransitionPending() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(22, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void appTransitionCancelled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(23, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void appTransitionStarting(long statusBarAnimationsStartTime, long statusBarAnimationsDuration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(statusBarAnimationsStartTime);
                    _data.writeLong(statusBarAnimationsDuration);
                    this.mRemote.transact(24, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void appTransitionFinished() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(25, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void showAssistDisclosure() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(26, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void startAssist(Bundle args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(27, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void onCameraLaunchGestureDetected(int source) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(source);
                    this.mRemote.transact(28, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void showPictureInPictureMenu() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(29, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void showGlobalActionsMenu() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(30, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void onProposedRotationChanged(int rotation, boolean isValid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(rotation);
                    _data.writeInt(isValid ? 1 : 0);
                    this.mRemote.transact(31, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void setTopAppHidesStatusBar(boolean hidesStatusBar) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(hidesStatusBar ? 1 : 0);
                    this.mRemote.transact(32, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void addQsTile(ComponentName tile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (tile != null) {
                        _data.writeInt(1);
                        tile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(33, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void remQsTile(ComponentName tile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (tile != null) {
                        _data.writeInt(1);
                        tile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(34, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void clickQsTile(ComponentName tile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (tile != null) {
                        _data.writeInt(1);
                        tile.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(35, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void handleSystemKey(int key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(key);
                    this.mRemote.transact(36, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void showPinningEnterExitToast(boolean entering) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(entering ? 1 : 0);
                    this.mRemote.transact(37, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void showPinningEscapeToast() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(38, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void showShutdownUi(boolean isReboot, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isReboot ? 1 : 0);
                    _data.writeString(reason);
                    this.mRemote.transact(39, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void showFingerprintDialog(Bundle bundle, IBiometricPromptReceiver receiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (bundle != null) {
                        _data.writeInt(1);
                        bundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    this.mRemote.transact(40, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void onFingerprintAuthenticated() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(41, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void onFingerprintHelp(String message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(message);
                    this.mRemote.transact(42, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void onFingerprintError(String error) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(error);
                    this.mRemote.transact(43, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.statusbar.IStatusBar
            public synchronized void hideFingerprintDialog() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(44, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
