package android.view.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.IAccessibilityServiceClient;
import android.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.IWindow;
import android.view.accessibility.IAccessibilityInteractionConnection;
import android.view.accessibility.IAccessibilityManagerClient;
import java.util.List;

/* loaded from: classes3.dex */
public interface IAccessibilityManager extends IInterface {
    int addAccessibilityInteractionConnection(IWindow iWindow, IAccessibilityInteractionConnection iAccessibilityInteractionConnection, String str, int i) throws RemoteException;

    long addClient(IAccessibilityManagerClient iAccessibilityManagerClient, int i) throws RemoteException;

    String getAccessibilityShortcutService() throws RemoteException;

    int getAccessibilityWindowId(IBinder iBinder) throws RemoteException;

    @UnsupportedAppUsage
    List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(int i, int i2) throws RemoteException;

    List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(int i) throws RemoteException;

    long getRecommendedTimeoutMillis() throws RemoteException;

    IBinder getWindowToken(int i, int i2) throws RemoteException;

    void interrupt(int i) throws RemoteException;

    void notifyAccessibilityButtonClicked(int i) throws RemoteException;

    void notifyAccessibilityButtonVisibilityChanged(boolean z) throws RemoteException;

    void performAccessibilityShortcut() throws RemoteException;

    void registerUiTestAutomationService(IBinder iBinder, IAccessibilityServiceClient iAccessibilityServiceClient, AccessibilityServiceInfo accessibilityServiceInfo, int i) throws RemoteException;

    void removeAccessibilityInteractionConnection(IWindow iWindow) throws RemoteException;

    void sendAccessibilityEvent(AccessibilityEvent accessibilityEvent, int i) throws RemoteException;

    boolean sendFingerprintGesture(int i) throws RemoteException;

    void setPictureInPictureActionReplacingConnection(IAccessibilityInteractionConnection iAccessibilityInteractionConnection) throws RemoteException;

    void temporaryEnableAccessibilityStateUntilKeyguardRemoved(ComponentName componentName, boolean z) throws RemoteException;

    void unregisterUiTestAutomationService(IAccessibilityServiceClient iAccessibilityServiceClient) throws RemoteException;

    /* loaded from: classes3.dex */
    public static class Default implements IAccessibilityManager {
        @Override // android.view.accessibility.IAccessibilityManager
        public void interrupt(int userId) throws RemoteException {
        }

        @Override // android.view.accessibility.IAccessibilityManager
        public void sendAccessibilityEvent(AccessibilityEvent uiEvent, int userId) throws RemoteException {
        }

        @Override // android.view.accessibility.IAccessibilityManager
        public long addClient(IAccessibilityManagerClient client, int userId) throws RemoteException {
            return 0L;
        }

        @Override // android.view.accessibility.IAccessibilityManager
        public List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(int userId) throws RemoteException {
            return null;
        }

        @Override // android.view.accessibility.IAccessibilityManager
        public List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(int feedbackType, int userId) throws RemoteException {
            return null;
        }

        @Override // android.view.accessibility.IAccessibilityManager
        public int addAccessibilityInteractionConnection(IWindow windowToken, IAccessibilityInteractionConnection connection, String packageName, int userId) throws RemoteException {
            return 0;
        }

        @Override // android.view.accessibility.IAccessibilityManager
        public void removeAccessibilityInteractionConnection(IWindow windowToken) throws RemoteException {
        }

        @Override // android.view.accessibility.IAccessibilityManager
        public void setPictureInPictureActionReplacingConnection(IAccessibilityInteractionConnection connection) throws RemoteException {
        }

        @Override // android.view.accessibility.IAccessibilityManager
        public void registerUiTestAutomationService(IBinder owner, IAccessibilityServiceClient client, AccessibilityServiceInfo info, int flags) throws RemoteException {
        }

        @Override // android.view.accessibility.IAccessibilityManager
        public void unregisterUiTestAutomationService(IAccessibilityServiceClient client) throws RemoteException {
        }

        @Override // android.view.accessibility.IAccessibilityManager
        public void temporaryEnableAccessibilityStateUntilKeyguardRemoved(ComponentName service, boolean touchExplorationEnabled) throws RemoteException {
        }

        @Override // android.view.accessibility.IAccessibilityManager
        public IBinder getWindowToken(int windowId, int userId) throws RemoteException {
            return null;
        }

        @Override // android.view.accessibility.IAccessibilityManager
        public void notifyAccessibilityButtonClicked(int displayId) throws RemoteException {
        }

        @Override // android.view.accessibility.IAccessibilityManager
        public void notifyAccessibilityButtonVisibilityChanged(boolean available) throws RemoteException {
        }

        @Override // android.view.accessibility.IAccessibilityManager
        public void performAccessibilityShortcut() throws RemoteException {
        }

        @Override // android.view.accessibility.IAccessibilityManager
        public String getAccessibilityShortcutService() throws RemoteException {
            return null;
        }

        @Override // android.view.accessibility.IAccessibilityManager
        public boolean sendFingerprintGesture(int gestureKeyCode) throws RemoteException {
            return false;
        }

        @Override // android.view.accessibility.IAccessibilityManager
        public int getAccessibilityWindowId(IBinder windowToken) throws RemoteException {
            return 0;
        }

        @Override // android.view.accessibility.IAccessibilityManager
        public long getRecommendedTimeoutMillis() throws RemoteException {
            return 0L;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IAccessibilityManager {
        private static final String DESCRIPTOR = "android.view.accessibility.IAccessibilityManager";
        static final int TRANSACTION_addAccessibilityInteractionConnection = 6;
        static final int TRANSACTION_addClient = 3;
        static final int TRANSACTION_getAccessibilityShortcutService = 16;
        static final int TRANSACTION_getAccessibilityWindowId = 18;
        static final int TRANSACTION_getEnabledAccessibilityServiceList = 5;
        static final int TRANSACTION_getInstalledAccessibilityServiceList = 4;
        static final int TRANSACTION_getRecommendedTimeoutMillis = 19;
        static final int TRANSACTION_getWindowToken = 12;
        static final int TRANSACTION_interrupt = 1;
        static final int TRANSACTION_notifyAccessibilityButtonClicked = 13;
        static final int TRANSACTION_notifyAccessibilityButtonVisibilityChanged = 14;
        static final int TRANSACTION_performAccessibilityShortcut = 15;
        static final int TRANSACTION_registerUiTestAutomationService = 9;
        static final int TRANSACTION_removeAccessibilityInteractionConnection = 7;
        static final int TRANSACTION_sendAccessibilityEvent = 2;
        static final int TRANSACTION_sendFingerprintGesture = 17;
        static final int TRANSACTION_setPictureInPictureActionReplacingConnection = 8;
        static final int TRANSACTION_temporaryEnableAccessibilityStateUntilKeyguardRemoved = 11;
        static final int TRANSACTION_unregisterUiTestAutomationService = 10;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAccessibilityManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IAccessibilityManager)) {
                return (IAccessibilityManager) iin;
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
                    return "interrupt";
                case 2:
                    return "sendAccessibilityEvent";
                case 3:
                    return "addClient";
                case 4:
                    return "getInstalledAccessibilityServiceList";
                case 5:
                    return "getEnabledAccessibilityServiceList";
                case 6:
                    return "addAccessibilityInteractionConnection";
                case 7:
                    return "removeAccessibilityInteractionConnection";
                case 8:
                    return "setPictureInPictureActionReplacingConnection";
                case 9:
                    return "registerUiTestAutomationService";
                case 10:
                    return "unregisterUiTestAutomationService";
                case 11:
                    return "temporaryEnableAccessibilityStateUntilKeyguardRemoved";
                case 12:
                    return "getWindowToken";
                case 13:
                    return "notifyAccessibilityButtonClicked";
                case 14:
                    return "notifyAccessibilityButtonVisibilityChanged";
                case 15:
                    return "performAccessibilityShortcut";
                case 16:
                    return "getAccessibilityShortcutService";
                case 17:
                    return "sendFingerprintGesture";
                case 18:
                    return "getAccessibilityWindowId";
                case 19:
                    return "getRecommendedTimeoutMillis";
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
            AccessibilityEvent _arg0;
            AccessibilityServiceInfo _arg2;
            ComponentName _arg02;
            boolean _arg03;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    interrupt(data.readInt());
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = AccessibilityEvent.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    int _arg1 = data.readInt();
                    sendAccessibilityEvent(_arg0, _arg1);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    IAccessibilityManagerClient _arg04 = IAccessibilityManagerClient.Stub.asInterface(data.readStrongBinder());
                    int _arg12 = data.readInt();
                    long _result = addClient(_arg04, _arg12);
                    reply.writeNoException();
                    reply.writeLong(_result);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    List<AccessibilityServiceInfo> _result2 = getInstalledAccessibilityServiceList(data.readInt());
                    reply.writeNoException();
                    reply.writeTypedList(_result2);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg05 = data.readInt();
                    int _arg13 = data.readInt();
                    List<AccessibilityServiceInfo> _result3 = getEnabledAccessibilityServiceList(_arg05, _arg13);
                    reply.writeNoException();
                    reply.writeTypedList(_result3);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    IWindow _arg06 = IWindow.Stub.asInterface(data.readStrongBinder());
                    IAccessibilityInteractionConnection _arg14 = IAccessibilityInteractionConnection.Stub.asInterface(data.readStrongBinder());
                    String _arg22 = data.readString();
                    int _arg3 = data.readInt();
                    int _result4 = addAccessibilityInteractionConnection(_arg06, _arg14, _arg22, _arg3);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    removeAccessibilityInteractionConnection(IWindow.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    setPictureInPictureActionReplacingConnection(IAccessibilityInteractionConnection.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    IBinder _arg07 = data.readStrongBinder();
                    IAccessibilityServiceClient _arg15 = IAccessibilityServiceClient.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg2 = AccessibilityServiceInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    int _arg32 = data.readInt();
                    registerUiTestAutomationService(_arg07, _arg15, _arg2, _arg32);
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterUiTestAutomationService(IAccessibilityServiceClient.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = ComponentName.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    _arg03 = data.readInt() != 0;
                    temporaryEnableAccessibilityStateUntilKeyguardRemoved(_arg02, _arg03);
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg08 = data.readInt();
                    int _arg16 = data.readInt();
                    IBinder _result5 = getWindowToken(_arg08, _arg16);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result5);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    notifyAccessibilityButtonClicked(data.readInt());
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    _arg03 = data.readInt() != 0;
                    notifyAccessibilityButtonVisibilityChanged(_arg03);
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    performAccessibilityShortcut();
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    String _result6 = getAccessibilityShortcutService();
                    reply.writeNoException();
                    reply.writeString(_result6);
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    boolean sendFingerprintGesture = sendFingerprintGesture(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(sendFingerprintGesture ? 1 : 0);
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    int _result7 = getAccessibilityWindowId(data.readStrongBinder());
                    reply.writeNoException();
                    reply.writeInt(_result7);
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    long _result8 = getRecommendedTimeoutMillis();
                    reply.writeNoException();
                    reply.writeLong(_result8);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements IAccessibilityManager {
            public static IAccessibilityManager sDefaultImpl;
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

            @Override // android.view.accessibility.IAccessibilityManager
            public void interrupt(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().interrupt(userId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.accessibility.IAccessibilityManager
            public void sendAccessibilityEvent(AccessibilityEvent uiEvent, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uiEvent != null) {
                        _data.writeInt(1);
                        uiEvent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().sendAccessibilityEvent(uiEvent, userId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.view.accessibility.IAccessibilityManager
            public long addClient(IAccessibilityManagerClient client, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().addClient(client, userId);
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.accessibility.IAccessibilityManager
            public List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getInstalledAccessibilityServiceList(userId);
                    }
                    _reply.readException();
                    List<AccessibilityServiceInfo> _result = _reply.createTypedArrayList(AccessibilityServiceInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.accessibility.IAccessibilityManager
            public List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(int feedbackType, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(feedbackType);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getEnabledAccessibilityServiceList(feedbackType, userId);
                    }
                    _reply.readException();
                    List<AccessibilityServiceInfo> _result = _reply.createTypedArrayList(AccessibilityServiceInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.accessibility.IAccessibilityManager
            public int addAccessibilityInteractionConnection(IWindow windowToken, IAccessibilityInteractionConnection connection, String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(windowToken != null ? windowToken.asBinder() : null);
                    _data.writeStrongBinder(connection != null ? connection.asBinder() : null);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().addAccessibilityInteractionConnection(windowToken, connection, packageName, userId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.accessibility.IAccessibilityManager
            public void removeAccessibilityInteractionConnection(IWindow windowToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(windowToken != null ? windowToken.asBinder() : null);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeAccessibilityInteractionConnection(windowToken);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.accessibility.IAccessibilityManager
            public void setPictureInPictureActionReplacingConnection(IAccessibilityInteractionConnection connection) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection != null ? connection.asBinder() : null);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setPictureInPictureActionReplacingConnection(connection);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.accessibility.IAccessibilityManager
            public void registerUiTestAutomationService(IBinder owner, IAccessibilityServiceClient client, AccessibilityServiceInfo info, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(owner);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerUiTestAutomationService(owner, client, info, flags);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.accessibility.IAccessibilityManager
            public void unregisterUiTestAutomationService(IAccessibilityServiceClient client) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(client != null ? client.asBinder() : null);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterUiTestAutomationService(client);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.accessibility.IAccessibilityManager
            public void temporaryEnableAccessibilityStateUntilKeyguardRemoved(ComponentName service, boolean touchExplorationEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!touchExplorationEnabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().temporaryEnableAccessibilityStateUntilKeyguardRemoved(service, touchExplorationEnabled);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.accessibility.IAccessibilityManager
            public IBinder getWindowToken(int windowId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(windowId);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getWindowToken(windowId, userId);
                    }
                    _reply.readException();
                    IBinder _result = _reply.readStrongBinder();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.accessibility.IAccessibilityManager
            public void notifyAccessibilityButtonClicked(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyAccessibilityButtonClicked(displayId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.accessibility.IAccessibilityManager
            public void notifyAccessibilityButtonVisibilityChanged(boolean available) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(available ? 1 : 0);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().notifyAccessibilityButtonVisibilityChanged(available);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.accessibility.IAccessibilityManager
            public void performAccessibilityShortcut() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().performAccessibilityShortcut();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.accessibility.IAccessibilityManager
            public String getAccessibilityShortcutService() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAccessibilityShortcutService();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.accessibility.IAccessibilityManager
            public boolean sendFingerprintGesture(int gestureKeyCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(gestureKeyCode);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().sendFingerprintGesture(gestureKeyCode);
                    }
                    _reply.readException();
                    boolean _status2 = _reply.readInt() != 0;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.accessibility.IAccessibilityManager
            public int getAccessibilityWindowId(IBinder windowToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(windowToken);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAccessibilityWindowId(windowToken);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.view.accessibility.IAccessibilityManager
            public long getRecommendedTimeoutMillis() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRecommendedTimeoutMillis();
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IAccessibilityManager impl) {
            if (Proxy.sDefaultImpl == null && impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IAccessibilityManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
