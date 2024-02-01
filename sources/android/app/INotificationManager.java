package android.app;

import android.app.ITransientNotification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.pm.ParceledListSlice;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.notification.Adjustment;
import android.service.notification.Condition;
import android.service.notification.IConditionProvider;
import android.service.notification.INotificationListener;
import android.service.notification.StatusBarNotification;
import android.service.notification.ZenModeConfig;
import java.util.List;
/* loaded from: classes.dex */
public interface INotificationManager extends IInterface {
    synchronized String addAutomaticZenRule(AutomaticZenRule automaticZenRule) throws RemoteException;

    synchronized void applyAdjustmentFromAssistant(INotificationListener iNotificationListener, Adjustment adjustment) throws RemoteException;

    synchronized void applyAdjustmentsFromAssistant(INotificationListener iNotificationListener, List<Adjustment> list) throws RemoteException;

    synchronized void applyEnqueuedAdjustmentFromAssistant(INotificationListener iNotificationListener, Adjustment adjustment) throws RemoteException;

    synchronized void applyRestore(byte[] bArr, int i) throws RemoteException;

    synchronized boolean areChannelsBypassingDnd() throws RemoteException;

    synchronized boolean areNotificationsEnabled(String str) throws RemoteException;

    private protected boolean areNotificationsEnabledForPackage(String str, int i) throws RemoteException;

    synchronized boolean canShowBadge(String str, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void cancelAllNotifications(String str, int i) throws RemoteException;

    synchronized void cancelNotificationFromListener(INotificationListener iNotificationListener, String str, String str2, int i) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void cancelNotificationWithTag(String str, String str2, int i, int i2) throws RemoteException;

    synchronized void cancelNotificationsFromListener(INotificationListener iNotificationListener, String[] strArr) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void cancelToast(String str, ITransientNotification iTransientNotification) throws RemoteException;

    synchronized void clearData(String str, int i, boolean z) throws RemoteException;

    synchronized void createNotificationChannelGroups(String str, ParceledListSlice parceledListSlice) throws RemoteException;

    synchronized void createNotificationChannels(String str, ParceledListSlice parceledListSlice) throws RemoteException;

    synchronized void createNotificationChannelsForPackage(String str, int i, ParceledListSlice parceledListSlice) throws RemoteException;

    synchronized void deleteNotificationChannel(String str, String str2) throws RemoteException;

    synchronized void deleteNotificationChannelGroup(String str, String str2) throws RemoteException;

    synchronized void enqueueNotificationWithTag(String str, String str2, String str3, int i, Notification notification, int i2) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    void enqueueToast(String str, ITransientNotification iTransientNotification, int i) throws RemoteException;

    synchronized void finishToken(String str, ITransientNotification iTransientNotification) throws RemoteException;

    private protected StatusBarNotification[] getActiveNotifications(String str) throws RemoteException;

    synchronized ParceledListSlice getActiveNotificationsFromListener(INotificationListener iNotificationListener, String[] strArr, int i) throws RemoteException;

    synchronized ParceledListSlice getAppActiveNotifications(String str, int i) throws RemoteException;

    synchronized AutomaticZenRule getAutomaticZenRule(String str) throws RemoteException;

    synchronized byte[] getBackupPayload(int i) throws RemoteException;

    synchronized int getBlockedAppCount(int i) throws RemoteException;

    synchronized int getBlockedChannelCount(String str, int i) throws RemoteException;

    synchronized int getDeletedChannelCount(String str, int i) throws RemoteException;

    synchronized ComponentName getEffectsSuppressor() throws RemoteException;

    synchronized List<String> getEnabledNotificationListenerPackages() throws RemoteException;

    synchronized List<ComponentName> getEnabledNotificationListeners(int i) throws RemoteException;

    synchronized int getHintsFromListener(INotificationListener iNotificationListener) throws RemoteException;

    private protected StatusBarNotification[] getHistoricalNotifications(String str, int i) throws RemoteException;

    synchronized int getInterruptionFilterFromListener(INotificationListener iNotificationListener) throws RemoteException;

    synchronized NotificationChannel getNotificationChannel(String str, String str2) throws RemoteException;

    synchronized NotificationChannel getNotificationChannelForPackage(String str, int i, String str2, boolean z) throws RemoteException;

    synchronized NotificationChannelGroup getNotificationChannelGroup(String str, String str2) throws RemoteException;

    synchronized NotificationChannelGroup getNotificationChannelGroupForPackage(String str, String str2, int i) throws RemoteException;

    synchronized ParceledListSlice getNotificationChannelGroups(String str) throws RemoteException;

    synchronized ParceledListSlice getNotificationChannelGroupsForPackage(String str, int i, boolean z) throws RemoteException;

    synchronized ParceledListSlice getNotificationChannelGroupsFromPrivilegedListener(INotificationListener iNotificationListener, String str, UserHandle userHandle) throws RemoteException;

    synchronized ParceledListSlice getNotificationChannels(String str) throws RemoteException;

    synchronized ParceledListSlice getNotificationChannelsForPackage(String str, int i, boolean z) throws RemoteException;

    synchronized ParceledListSlice getNotificationChannelsFromPrivilegedListener(INotificationListener iNotificationListener, String str, UserHandle userHandle) throws RemoteException;

    List<StatusBarNotification> getNotificationList(int i) throws RemoteException;

    synchronized NotificationManager.Policy getNotificationPolicy(String str) throws RemoteException;

    synchronized int getNumNotificationChannelsForPackage(String str, int i, boolean z) throws RemoteException;

    synchronized int getPackageImportance(String str) throws RemoteException;

    synchronized NotificationChannelGroup getPopulatedNotificationChannelGroupForPackage(String str, int i, String str2, boolean z) throws RemoteException;

    synchronized ParceledListSlice getRecentNotifyingAppsForUser(int i) throws RemoteException;

    synchronized int getRuleInstanceCount(ComponentName componentName) throws RemoteException;

    synchronized ParceledListSlice getSnoozedNotificationsFromListener(INotificationListener iNotificationListener, int i) throws RemoteException;

    int getUnreadCount(String str) throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    int getZenMode() throws RemoteException;

    /* JADX INFO: Access modifiers changed from: private */
    ZenModeConfig getZenModeConfig() throws RemoteException;

    synchronized List<ZenModeConfig.ZenRule> getZenRules() throws RemoteException;

    synchronized boolean isNotificationAssistantAccessGranted(ComponentName componentName) throws RemoteException;

    synchronized boolean isNotificationListenerAccessGranted(ComponentName componentName) throws RemoteException;

    synchronized boolean isNotificationListenerAccessGrantedForUser(ComponentName componentName, int i) throws RemoteException;

    synchronized boolean isNotificationPolicyAccessGranted(String str) throws RemoteException;

    synchronized boolean isNotificationPolicyAccessGrantedForPackage(String str) throws RemoteException;

    synchronized boolean isSystemConditionProviderEnabled(String str) throws RemoteException;

    synchronized boolean matchesCallFilter(Bundle bundle) throws RemoteException;

    synchronized void notifyConditions(String str, IConditionProvider iConditionProvider, Condition[] conditionArr) throws RemoteException;

    synchronized boolean onlyHasDefaultChannel(String str, int i) throws RemoteException;

    synchronized void registerListener(INotificationListener iNotificationListener, ComponentName componentName, int i) throws RemoteException;

    synchronized boolean removeAutomaticZenRule(String str) throws RemoteException;

    synchronized boolean removeAutomaticZenRules(String str) throws RemoteException;

    synchronized void requestBindListener(ComponentName componentName) throws RemoteException;

    synchronized void requestBindProvider(ComponentName componentName) throws RemoteException;

    synchronized void requestHintsFromListener(INotificationListener iNotificationListener, int i) throws RemoteException;

    synchronized void requestInterruptionFilterFromListener(INotificationListener iNotificationListener, int i) throws RemoteException;

    synchronized void requestUnbindListener(INotificationListener iNotificationListener) throws RemoteException;

    synchronized void requestUnbindProvider(IConditionProvider iConditionProvider) throws RemoteException;

    synchronized void setInterruptionFilter(String str, int i) throws RemoteException;

    synchronized void setNotificationAssistantAccessGranted(ComponentName componentName, boolean z) throws RemoteException;

    synchronized void setNotificationAssistantAccessGrantedForUser(ComponentName componentName, int i, boolean z) throws RemoteException;

    synchronized void setNotificationListenerAccessGranted(ComponentName componentName, boolean z) throws RemoteException;

    void setNotificationListenerAccessGrantedForAll(ComponentName componentName, boolean z) throws RemoteException;

    synchronized void setNotificationListenerAccessGrantedForUser(ComponentName componentName, int i, boolean z) throws RemoteException;

    void setNotificationNumber(String str, int i) throws RemoteException;

    synchronized void setNotificationPolicy(String str, NotificationManager.Policy policy) throws RemoteException;

    synchronized void setNotificationPolicyAccessGranted(String str, boolean z) throws RemoteException;

    synchronized void setNotificationPolicyAccessGrantedForUser(String str, int i, boolean z) throws RemoteException;

    synchronized void setNotificationsEnabledForPackage(String str, int i, boolean z) throws RemoteException;

    synchronized void setNotificationsEnabledWithImportanceLockForPackage(String str, int i, boolean z) throws RemoteException;

    synchronized void setNotificationsShownFromListener(INotificationListener iNotificationListener, String[] strArr) throws RemoteException;

    synchronized void setOnNotificationPostedTrimFromListener(INotificationListener iNotificationListener, int i) throws RemoteException;

    void setOsdEnabled(boolean z) throws RemoteException;

    synchronized void setShowBadge(String str, int i, boolean z) throws RemoteException;

    void setToastEnabled(boolean z) throws RemoteException;

    synchronized void setZenMode(int i, Uri uri, String str) throws RemoteException;

    synchronized void snoozeNotificationUntilContextFromListener(INotificationListener iNotificationListener, String str, String str2) throws RemoteException;

    synchronized void snoozeNotificationUntilFromListener(INotificationListener iNotificationListener, String str, long j) throws RemoteException;

    synchronized void unregisterListener(INotificationListener iNotificationListener, int i) throws RemoteException;

    synchronized void unsnoozeNotificationFromAssistant(INotificationListener iNotificationListener, String str) throws RemoteException;

    synchronized boolean updateAutomaticZenRule(String str, AutomaticZenRule automaticZenRule) throws RemoteException;

    synchronized void updateNotificationChannelForPackage(String str, int i, NotificationChannel notificationChannel) throws RemoteException;

    synchronized void updateNotificationChannelFromPrivilegedListener(INotificationListener iNotificationListener, String str, UserHandle userHandle, NotificationChannel notificationChannel) throws RemoteException;

    synchronized void updateNotificationChannelGroupForPackage(String str, int i, NotificationChannelGroup notificationChannelGroup) throws RemoteException;

    synchronized void updateNotificationChannelGroupFromPrivilegedListener(INotificationListener iNotificationListener, String str, UserHandle userHandle, NotificationChannelGroup notificationChannelGroup) throws RemoteException;

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements INotificationManager {
        private static final String DESCRIPTOR = "android.app.INotificationManager";
        static final int TRANSACTION_addAutomaticZenRule = 91;
        static final int TRANSACTION_applyAdjustmentFromAssistant = 64;
        static final int TRANSACTION_applyAdjustmentsFromAssistant = 65;
        static final int TRANSACTION_applyEnqueuedAdjustmentFromAssistant = 63;
        static final int TRANSACTION_applyRestore = 97;
        static final int TRANSACTION_areChannelsBypassingDnd = 37;
        static final int TRANSACTION_areNotificationsEnabled = 13;
        static final int TRANSACTION_areNotificationsEnabledForPackage = 12;
        static final int TRANSACTION_canShowBadge = 9;
        static final int TRANSACTION_cancelAllNotifications = 1;
        static final int TRANSACTION_cancelNotificationFromListener = 42;
        static final int TRANSACTION_cancelNotificationWithTag = 7;
        static final int TRANSACTION_cancelNotificationsFromListener = 43;
        static final int TRANSACTION_cancelToast = 4;
        static final int TRANSACTION_clearData = 2;
        static final int TRANSACTION_createNotificationChannelGroups = 15;
        static final int TRANSACTION_createNotificationChannels = 16;
        static final int TRANSACTION_createNotificationChannelsForPackage = 17;
        static final int TRANSACTION_deleteNotificationChannel = 25;
        static final int TRANSACTION_deleteNotificationChannelGroup = 31;
        public private protected static final int TRANSACTION_enqueueNotificationWithTag = 6;
        static final int TRANSACTION_enqueueToast = 3;
        static final int TRANSACTION_finishToken = 5;
        static final int TRANSACTION_getActiveNotifications = 38;
        static final int TRANSACTION_getActiveNotificationsFromListener = 51;
        static final int TRANSACTION_getAppActiveNotifications = 98;
        static final int TRANSACTION_getAutomaticZenRule = 89;
        static final int TRANSACTION_getBackupPayload = 96;
        static final int TRANSACTION_getBlockedAppCount = 36;
        static final int TRANSACTION_getBlockedChannelCount = 30;
        static final int TRANSACTION_getDeletedChannelCount = 29;
        static final int TRANSACTION_getEffectsSuppressor = 67;
        static final int TRANSACTION_getEnabledNotificationListenerPackages = 77;
        static final int TRANSACTION_getEnabledNotificationListeners = 78;
        static final int TRANSACTION_getHintsFromListener = 54;
        static final int TRANSACTION_getHistoricalNotifications = 39;
        static final int TRANSACTION_getInterruptionFilterFromListener = 56;
        static final int TRANSACTION_getNotificationChannel = 23;
        static final int TRANSACTION_getNotificationChannelForPackage = 24;
        static final int TRANSACTION_getNotificationChannelGroup = 32;
        static final int TRANSACTION_getNotificationChannelGroupForPackage = 19;
        static final int TRANSACTION_getNotificationChannelGroups = 33;
        static final int TRANSACTION_getNotificationChannelGroupsForPackage = 18;
        static final int TRANSACTION_getNotificationChannelGroupsFromPrivilegedListener = 62;
        static final int TRANSACTION_getNotificationChannels = 26;
        static final int TRANSACTION_getNotificationChannelsForPackage = 27;
        static final int TRANSACTION_getNotificationChannelsFromPrivilegedListener = 61;
        static final int TRANSACTION_getNotificationList = 100;
        static final int TRANSACTION_getNotificationPolicy = 84;
        static final int TRANSACTION_getNumNotificationChannelsForPackage = 28;
        static final int TRANSACTION_getPackageImportance = 14;
        static final int TRANSACTION_getPopulatedNotificationChannelGroupForPackage = 20;
        static final int TRANSACTION_getRecentNotifyingAppsForUser = 35;
        static final int TRANSACTION_getRuleInstanceCount = 95;
        static final int TRANSACTION_getSnoozedNotificationsFromListener = 52;
        static final int TRANSACTION_getUnreadCount = 99;
        static final int TRANSACTION_getZenMode = 79;
        static final int TRANSACTION_getZenModeConfig = 80;
        static final int TRANSACTION_getZenRules = 90;
        static final int TRANSACTION_isNotificationAssistantAccessGranted = 72;
        static final int TRANSACTION_isNotificationListenerAccessGranted = 70;
        static final int TRANSACTION_isNotificationListenerAccessGrantedForUser = 71;
        static final int TRANSACTION_isNotificationPolicyAccessGranted = 83;
        static final int TRANSACTION_isNotificationPolicyAccessGrantedForPackage = 86;
        static final int TRANSACTION_isSystemConditionProviderEnabled = 69;
        static final int TRANSACTION_matchesCallFilter = 68;
        static final int TRANSACTION_notifyConditions = 82;
        static final int TRANSACTION_onlyHasDefaultChannel = 34;
        static final int TRANSACTION_registerListener = 40;
        static final int TRANSACTION_removeAutomaticZenRule = 93;
        static final int TRANSACTION_removeAutomaticZenRules = 94;
        static final int TRANSACTION_requestBindListener = 46;
        static final int TRANSACTION_requestBindProvider = 48;
        static final int TRANSACTION_requestHintsFromListener = 53;
        static final int TRANSACTION_requestInterruptionFilterFromListener = 55;
        static final int TRANSACTION_requestUnbindListener = 47;
        static final int TRANSACTION_requestUnbindProvider = 49;
        static final int TRANSACTION_setInterruptionFilter = 58;
        static final int TRANSACTION_setNotificationAssistantAccessGranted = 74;
        static final int TRANSACTION_setNotificationAssistantAccessGrantedForUser = 76;
        static final int TRANSACTION_setNotificationListenerAccessGranted = 73;
        static final int TRANSACTION_setNotificationListenerAccessGrantedForAll = 102;
        static final int TRANSACTION_setNotificationListenerAccessGrantedForUser = 75;
        static final int TRANSACTION_setNotificationNumber = 101;
        static final int TRANSACTION_setNotificationPolicy = 85;
        static final int TRANSACTION_setNotificationPolicyAccessGranted = 87;
        static final int TRANSACTION_setNotificationPolicyAccessGrantedForUser = 88;
        static final int TRANSACTION_setNotificationsEnabledForPackage = 10;
        static final int TRANSACTION_setNotificationsEnabledWithImportanceLockForPackage = 11;
        static final int TRANSACTION_setNotificationsShownFromListener = 50;
        static final int TRANSACTION_setOnNotificationPostedTrimFromListener = 57;
        static final int TRANSACTION_setOsdEnabled = 104;
        static final int TRANSACTION_setShowBadge = 8;
        static final int TRANSACTION_setToastEnabled = 103;
        static final int TRANSACTION_setZenMode = 81;
        static final int TRANSACTION_snoozeNotificationUntilContextFromListener = 44;
        static final int TRANSACTION_snoozeNotificationUntilFromListener = 45;
        static final int TRANSACTION_unregisterListener = 41;
        static final int TRANSACTION_unsnoozeNotificationFromAssistant = 66;
        static final int TRANSACTION_updateAutomaticZenRule = 92;
        static final int TRANSACTION_updateNotificationChannelForPackage = 22;
        static final int TRANSACTION_updateNotificationChannelFromPrivilegedListener = 60;
        static final int TRANSACTION_updateNotificationChannelGroupForPackage = 21;
        static final int TRANSACTION_updateNotificationChannelGroupFromPrivilegedListener = 59;

        private protected Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static INotificationManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof INotificationManager)) {
                return (INotificationManager) iin;
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
            UserHandle _arg2;
            UserHandle _arg22;
            if (code == 1598968902) {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg0 = data.readString();
                    cancelAllNotifications(_arg0, data.readInt());
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    int _arg12 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    clearData(_arg02, _arg12, _arg1);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    ITransientNotification _arg13 = ITransientNotification.Stub.asInterface(data.readStrongBinder());
                    int _arg23 = data.readInt();
                    enqueueToast(_arg03, _arg13, _arg23);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    cancelToast(_arg04, ITransientNotification.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    finishToken(_arg05, ITransientNotification.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg06 = data.readString();
                    String _arg14 = data.readString();
                    String _arg24 = data.readString();
                    int _arg3 = data.readInt();
                    Notification _arg4 = data.readInt() != 0 ? Notification.CREATOR.createFromParcel(data) : null;
                    int _arg5 = data.readInt();
                    enqueueNotificationWithTag(_arg06, _arg14, _arg24, _arg3, _arg4, _arg5);
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    String _arg15 = data.readString();
                    int _arg25 = data.readInt();
                    int _arg32 = data.readInt();
                    cancelNotificationWithTag(_arg07, _arg15, _arg25, _arg32);
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    int _arg16 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    setShowBadge(_arg08, _arg16, _arg1);
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg09 = data.readString();
                    boolean canShowBadge = canShowBadge(_arg09, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(canShowBadge ? 1 : 0);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg010 = data.readString();
                    int _arg17 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    setNotificationsEnabledForPackage(_arg010, _arg17, _arg1);
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg011 = data.readString();
                    int _arg18 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    setNotificationsEnabledWithImportanceLockForPackage(_arg011, _arg18, _arg1);
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg012 = data.readString();
                    boolean areNotificationsEnabledForPackage = areNotificationsEnabledForPackage(_arg012, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(areNotificationsEnabledForPackage ? 1 : 0);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg013 = data.readString();
                    boolean areNotificationsEnabled = areNotificationsEnabled(_arg013);
                    reply.writeNoException();
                    reply.writeInt(areNotificationsEnabled ? 1 : 0);
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg014 = data.readString();
                    int _result = getPackageImportance(_arg014);
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg015 = data.readString();
                    createNotificationChannelGroups(_arg015, data.readInt() != 0 ? ParceledListSlice.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg016 = data.readString();
                    createNotificationChannels(_arg016, data.readInt() != 0 ? ParceledListSlice.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg017 = data.readString();
                    int _arg19 = data.readInt();
                    ParceledListSlice _arg26 = data.readInt() != 0 ? ParceledListSlice.CREATOR.createFromParcel(data) : null;
                    createNotificationChannelsForPackage(_arg017, _arg19, _arg26);
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg018 = data.readString();
                    int _arg110 = data.readInt();
                    boolean _arg27 = data.readInt() != 0;
                    ParceledListSlice _result2 = getNotificationChannelGroupsForPackage(_arg018, _arg110, _arg27);
                    reply.writeNoException();
                    if (_result2 != null) {
                        reply.writeInt(1);
                        _result2.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg019 = data.readString();
                    String _arg111 = data.readString();
                    int _arg28 = data.readInt();
                    NotificationChannelGroup _result3 = getNotificationChannelGroupForPackage(_arg019, _arg111, _arg28);
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg020 = data.readString();
                    int _arg112 = data.readInt();
                    String _arg29 = data.readString();
                    boolean _arg33 = data.readInt() != 0;
                    NotificationChannelGroup _result4 = getPopulatedNotificationChannelGroupForPackage(_arg020, _arg112, _arg29, _arg33);
                    reply.writeNoException();
                    if (_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg021 = data.readString();
                    int _arg113 = data.readInt();
                    NotificationChannelGroup _arg210 = data.readInt() != 0 ? NotificationChannelGroup.CREATOR.createFromParcel(data) : null;
                    updateNotificationChannelGroupForPackage(_arg021, _arg113, _arg210);
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg022 = data.readString();
                    int _arg114 = data.readInt();
                    NotificationChannel _arg211 = data.readInt() != 0 ? NotificationChannel.CREATOR.createFromParcel(data) : null;
                    updateNotificationChannelForPackage(_arg022, _arg114, _arg211);
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg023 = data.readString();
                    NotificationChannel _result5 = getNotificationChannel(_arg023, data.readString());
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg024 = data.readString();
                    int _arg115 = data.readInt();
                    String _arg212 = data.readString();
                    boolean _arg34 = data.readInt() != 0;
                    NotificationChannel _result6 = getNotificationChannelForPackage(_arg024, _arg115, _arg212, _arg34);
                    reply.writeNoException();
                    if (_result6 != null) {
                        reply.writeInt(1);
                        _result6.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg025 = data.readString();
                    deleteNotificationChannel(_arg025, data.readString());
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg026 = data.readString();
                    ParceledListSlice _result7 = getNotificationChannels(_arg026);
                    reply.writeNoException();
                    if (_result7 != null) {
                        reply.writeInt(1);
                        _result7.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg027 = data.readString();
                    int _arg116 = data.readInt();
                    boolean _arg213 = data.readInt() != 0;
                    ParceledListSlice _result8 = getNotificationChannelsForPackage(_arg027, _arg116, _arg213);
                    reply.writeNoException();
                    if (_result8 != null) {
                        reply.writeInt(1);
                        _result8.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg028 = data.readString();
                    int _arg117 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    int _result9 = getNumNotificationChannelsForPackage(_arg028, _arg117, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result9);
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg029 = data.readString();
                    int _result10 = getDeletedChannelCount(_arg029, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result10);
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg030 = data.readString();
                    int _result11 = getBlockedChannelCount(_arg030, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result11);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg031 = data.readString();
                    deleteNotificationChannelGroup(_arg031, data.readString());
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg032 = data.readString();
                    NotificationChannelGroup _result12 = getNotificationChannelGroup(_arg032, data.readString());
                    reply.writeNoException();
                    if (_result12 != null) {
                        reply.writeInt(1);
                        _result12.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg033 = data.readString();
                    ParceledListSlice _result13 = getNotificationChannelGroups(_arg033);
                    reply.writeNoException();
                    if (_result13 != null) {
                        reply.writeInt(1);
                        _result13.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg034 = data.readString();
                    boolean onlyHasDefaultChannel = onlyHasDefaultChannel(_arg034, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(onlyHasDefaultChannel ? 1 : 0);
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg035 = data.readInt();
                    ParceledListSlice _result14 = getRecentNotifyingAppsForUser(_arg035);
                    reply.writeNoException();
                    if (_result14 != null) {
                        reply.writeInt(1);
                        _result14.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg036 = data.readInt();
                    int _result15 = getBlockedAppCount(_arg036);
                    reply.writeNoException();
                    reply.writeInt(_result15);
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    boolean areChannelsBypassingDnd = areChannelsBypassingDnd();
                    reply.writeNoException();
                    reply.writeInt(areChannelsBypassingDnd ? 1 : 0);
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg037 = data.readString();
                    StatusBarNotification[] _result16 = getActiveNotifications(_arg037);
                    reply.writeNoException();
                    reply.writeTypedArray(_result16, 1);
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg038 = data.readString();
                    StatusBarNotification[] _result17 = getHistoricalNotifications(_arg038, data.readInt());
                    reply.writeNoException();
                    reply.writeTypedArray(_result17, 1);
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    INotificationListener _arg039 = INotificationListener.Stub.asInterface(data.readStrongBinder());
                    ComponentName _arg118 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg214 = data.readInt();
                    registerListener(_arg039, _arg118, _arg214);
                    reply.writeNoException();
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    INotificationListener _arg040 = INotificationListener.Stub.asInterface(data.readStrongBinder());
                    unregisterListener(_arg040, data.readInt());
                    reply.writeNoException();
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    INotificationListener _arg041 = INotificationListener.Stub.asInterface(data.readStrongBinder());
                    String _arg119 = data.readString();
                    String _arg215 = data.readString();
                    int _arg35 = data.readInt();
                    cancelNotificationFromListener(_arg041, _arg119, _arg215, _arg35);
                    reply.writeNoException();
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    INotificationListener _arg042 = INotificationListener.Stub.asInterface(data.readStrongBinder());
                    cancelNotificationsFromListener(_arg042, data.createStringArray());
                    reply.writeNoException();
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    INotificationListener _arg043 = INotificationListener.Stub.asInterface(data.readStrongBinder());
                    String _arg120 = data.readString();
                    String _arg216 = data.readString();
                    snoozeNotificationUntilContextFromListener(_arg043, _arg120, _arg216);
                    reply.writeNoException();
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    INotificationListener _arg044 = INotificationListener.Stub.asInterface(data.readStrongBinder());
                    String _arg121 = data.readString();
                    long _arg217 = data.readLong();
                    snoozeNotificationUntilFromListener(_arg044, _arg121, _arg217);
                    reply.writeNoException();
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg045 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    requestBindListener(_arg045);
                    reply.writeNoException();
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    INotificationListener _arg046 = INotificationListener.Stub.asInterface(data.readStrongBinder());
                    requestUnbindListener(_arg046);
                    reply.writeNoException();
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg047 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    requestBindProvider(_arg047);
                    reply.writeNoException();
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    IConditionProvider _arg048 = IConditionProvider.Stub.asInterface(data.readStrongBinder());
                    requestUnbindProvider(_arg048);
                    reply.writeNoException();
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    INotificationListener _arg049 = INotificationListener.Stub.asInterface(data.readStrongBinder());
                    setNotificationsShownFromListener(_arg049, data.createStringArray());
                    reply.writeNoException();
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    INotificationListener _arg050 = INotificationListener.Stub.asInterface(data.readStrongBinder());
                    String[] _arg122 = data.createStringArray();
                    int _arg218 = data.readInt();
                    ParceledListSlice _result18 = getActiveNotificationsFromListener(_arg050, _arg122, _arg218);
                    reply.writeNoException();
                    if (_result18 != null) {
                        reply.writeInt(1);
                        _result18.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    INotificationListener _arg051 = INotificationListener.Stub.asInterface(data.readStrongBinder());
                    ParceledListSlice _result19 = getSnoozedNotificationsFromListener(_arg051, data.readInt());
                    reply.writeNoException();
                    if (_result19 != null) {
                        reply.writeInt(1);
                        _result19.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    INotificationListener _arg052 = INotificationListener.Stub.asInterface(data.readStrongBinder());
                    requestHintsFromListener(_arg052, data.readInt());
                    reply.writeNoException();
                    return true;
                case 54:
                    data.enforceInterface(DESCRIPTOR);
                    INotificationListener _arg053 = INotificationListener.Stub.asInterface(data.readStrongBinder());
                    int _result20 = getHintsFromListener(_arg053);
                    reply.writeNoException();
                    reply.writeInt(_result20);
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    INotificationListener _arg054 = INotificationListener.Stub.asInterface(data.readStrongBinder());
                    requestInterruptionFilterFromListener(_arg054, data.readInt());
                    reply.writeNoException();
                    return true;
                case 56:
                    data.enforceInterface(DESCRIPTOR);
                    INotificationListener _arg055 = INotificationListener.Stub.asInterface(data.readStrongBinder());
                    int _result21 = getInterruptionFilterFromListener(_arg055);
                    reply.writeNoException();
                    reply.writeInt(_result21);
                    return true;
                case 57:
                    data.enforceInterface(DESCRIPTOR);
                    INotificationListener _arg056 = INotificationListener.Stub.asInterface(data.readStrongBinder());
                    setOnNotificationPostedTrimFromListener(_arg056, data.readInt());
                    reply.writeNoException();
                    return true;
                case 58:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg057 = data.readString();
                    setInterruptionFilter(_arg057, data.readInt());
                    reply.writeNoException();
                    return true;
                case 59:
                    data.enforceInterface(DESCRIPTOR);
                    INotificationListener _arg058 = INotificationListener.Stub.asInterface(data.readStrongBinder());
                    String _arg123 = data.readString();
                    if (data.readInt() != 0) {
                        _arg2 = UserHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    NotificationChannelGroup _arg36 = data.readInt() != 0 ? NotificationChannelGroup.CREATOR.createFromParcel(data) : null;
                    updateNotificationChannelGroupFromPrivilegedListener(_arg058, _arg123, _arg2, _arg36);
                    reply.writeNoException();
                    return true;
                case 60:
                    data.enforceInterface(DESCRIPTOR);
                    INotificationListener _arg059 = INotificationListener.Stub.asInterface(data.readStrongBinder());
                    String _arg124 = data.readString();
                    if (data.readInt() != 0) {
                        _arg22 = UserHandle.CREATOR.createFromParcel(data);
                    } else {
                        _arg22 = null;
                    }
                    NotificationChannel _arg37 = data.readInt() != 0 ? NotificationChannel.CREATOR.createFromParcel(data) : null;
                    updateNotificationChannelFromPrivilegedListener(_arg059, _arg124, _arg22, _arg37);
                    reply.writeNoException();
                    return true;
                case 61:
                    data.enforceInterface(DESCRIPTOR);
                    INotificationListener _arg060 = INotificationListener.Stub.asInterface(data.readStrongBinder());
                    String _arg125 = data.readString();
                    UserHandle _arg219 = data.readInt() != 0 ? UserHandle.CREATOR.createFromParcel(data) : null;
                    ParceledListSlice _result22 = getNotificationChannelsFromPrivilegedListener(_arg060, _arg125, _arg219);
                    reply.writeNoException();
                    if (_result22 != null) {
                        reply.writeInt(1);
                        _result22.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 62:
                    data.enforceInterface(DESCRIPTOR);
                    INotificationListener _arg061 = INotificationListener.Stub.asInterface(data.readStrongBinder());
                    String _arg126 = data.readString();
                    UserHandle _arg220 = data.readInt() != 0 ? UserHandle.CREATOR.createFromParcel(data) : null;
                    ParceledListSlice _result23 = getNotificationChannelGroupsFromPrivilegedListener(_arg061, _arg126, _arg220);
                    reply.writeNoException();
                    if (_result23 != null) {
                        reply.writeInt(1);
                        _result23.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 63:
                    data.enforceInterface(DESCRIPTOR);
                    INotificationListener _arg062 = INotificationListener.Stub.asInterface(data.readStrongBinder());
                    applyEnqueuedAdjustmentFromAssistant(_arg062, data.readInt() != 0 ? Adjustment.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case 64:
                    data.enforceInterface(DESCRIPTOR);
                    INotificationListener _arg063 = INotificationListener.Stub.asInterface(data.readStrongBinder());
                    applyAdjustmentFromAssistant(_arg063, data.readInt() != 0 ? Adjustment.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case 65:
                    data.enforceInterface(DESCRIPTOR);
                    INotificationListener _arg064 = INotificationListener.Stub.asInterface(data.readStrongBinder());
                    applyAdjustmentsFromAssistant(_arg064, data.createTypedArrayList(Adjustment.CREATOR));
                    reply.writeNoException();
                    return true;
                case 66:
                    data.enforceInterface(DESCRIPTOR);
                    INotificationListener _arg065 = INotificationListener.Stub.asInterface(data.readStrongBinder());
                    unsnoozeNotificationFromAssistant(_arg065, data.readString());
                    reply.writeNoException();
                    return true;
                case 67:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _result24 = getEffectsSuppressor();
                    reply.writeNoException();
                    if (_result24 != null) {
                        reply.writeInt(1);
                        _result24.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 68:
                    data.enforceInterface(DESCRIPTOR);
                    Bundle _arg066 = data.readInt() != 0 ? Bundle.CREATOR.createFromParcel(data) : null;
                    boolean matchesCallFilter = matchesCallFilter(_arg066);
                    reply.writeNoException();
                    reply.writeInt(matchesCallFilter ? 1 : 0);
                    return true;
                case 69:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg067 = data.readString();
                    boolean isSystemConditionProviderEnabled = isSystemConditionProviderEnabled(_arg067);
                    reply.writeNoException();
                    reply.writeInt(isSystemConditionProviderEnabled ? 1 : 0);
                    return true;
                case 70:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg068 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    boolean isNotificationListenerAccessGranted = isNotificationListenerAccessGranted(_arg068);
                    reply.writeNoException();
                    reply.writeInt(isNotificationListenerAccessGranted ? 1 : 0);
                    return true;
                case 71:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg069 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    boolean isNotificationListenerAccessGrantedForUser = isNotificationListenerAccessGrantedForUser(_arg069, data.readInt());
                    reply.writeNoException();
                    reply.writeInt(isNotificationListenerAccessGrantedForUser ? 1 : 0);
                    return true;
                case 72:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg070 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    boolean isNotificationAssistantAccessGranted = isNotificationAssistantAccessGranted(_arg070);
                    reply.writeNoException();
                    reply.writeInt(isNotificationAssistantAccessGranted ? 1 : 0);
                    return true;
                case 73:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg071 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    _arg1 = data.readInt() != 0;
                    setNotificationListenerAccessGranted(_arg071, _arg1);
                    reply.writeNoException();
                    return true;
                case 74:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg072 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    _arg1 = data.readInt() != 0;
                    setNotificationAssistantAccessGranted(_arg072, _arg1);
                    reply.writeNoException();
                    return true;
                case 75:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg073 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg127 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    setNotificationListenerAccessGrantedForUser(_arg073, _arg127, _arg1);
                    reply.writeNoException();
                    return true;
                case 76:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg074 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _arg128 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    setNotificationAssistantAccessGrantedForUser(_arg074, _arg128, _arg1);
                    reply.writeNoException();
                    return true;
                case 77:
                    data.enforceInterface(DESCRIPTOR);
                    List<String> _result25 = getEnabledNotificationListenerPackages();
                    reply.writeNoException();
                    reply.writeStringList(_result25);
                    return true;
                case 78:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg075 = data.readInt();
                    List<ComponentName> _result26 = getEnabledNotificationListeners(_arg075);
                    reply.writeNoException();
                    reply.writeTypedList(_result26);
                    return true;
                case 79:
                    data.enforceInterface(DESCRIPTOR);
                    int _result27 = getZenMode();
                    reply.writeNoException();
                    reply.writeInt(_result27);
                    return true;
                case 80:
                    data.enforceInterface(DESCRIPTOR);
                    ZenModeConfig _result28 = getZenModeConfig();
                    reply.writeNoException();
                    if (_result28 != null) {
                        reply.writeInt(1);
                        _result28.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 81:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg076 = data.readInt();
                    Uri _arg129 = data.readInt() != 0 ? Uri.CREATOR.createFromParcel(data) : null;
                    String _arg221 = data.readString();
                    setZenMode(_arg076, _arg129, _arg221);
                    return true;
                case 82:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg077 = data.readString();
                    IConditionProvider _arg130 = IConditionProvider.Stub.asInterface(data.readStrongBinder());
                    Condition[] _arg222 = (Condition[]) data.createTypedArray(Condition.CREATOR);
                    notifyConditions(_arg077, _arg130, _arg222);
                    return true;
                case 83:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg078 = data.readString();
                    boolean isNotificationPolicyAccessGranted = isNotificationPolicyAccessGranted(_arg078);
                    reply.writeNoException();
                    reply.writeInt(isNotificationPolicyAccessGranted ? 1 : 0);
                    return true;
                case 84:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg079 = data.readString();
                    NotificationManager.Policy _result29 = getNotificationPolicy(_arg079);
                    reply.writeNoException();
                    if (_result29 != null) {
                        reply.writeInt(1);
                        _result29.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 85:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg080 = data.readString();
                    setNotificationPolicy(_arg080, data.readInt() != 0 ? NotificationManager.Policy.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    return true;
                case 86:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg081 = data.readString();
                    boolean isNotificationPolicyAccessGrantedForPackage = isNotificationPolicyAccessGrantedForPackage(_arg081);
                    reply.writeNoException();
                    reply.writeInt(isNotificationPolicyAccessGrantedForPackage ? 1 : 0);
                    return true;
                case 87:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg082 = data.readString();
                    _arg1 = data.readInt() != 0;
                    setNotificationPolicyAccessGranted(_arg082, _arg1);
                    reply.writeNoException();
                    return true;
                case 88:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg083 = data.readString();
                    int _arg131 = data.readInt();
                    _arg1 = data.readInt() != 0;
                    setNotificationPolicyAccessGrantedForUser(_arg083, _arg131, _arg1);
                    reply.writeNoException();
                    return true;
                case 89:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg084 = data.readString();
                    AutomaticZenRule _result30 = getAutomaticZenRule(_arg084);
                    reply.writeNoException();
                    if (_result30 != null) {
                        reply.writeInt(1);
                        _result30.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 90:
                    data.enforceInterface(DESCRIPTOR);
                    List<ZenModeConfig.ZenRule> _result31 = getZenRules();
                    reply.writeNoException();
                    reply.writeTypedList(_result31);
                    return true;
                case 91:
                    data.enforceInterface(DESCRIPTOR);
                    AutomaticZenRule _arg085 = data.readInt() != 0 ? AutomaticZenRule.CREATOR.createFromParcel(data) : null;
                    String _result32 = addAutomaticZenRule(_arg085);
                    reply.writeNoException();
                    reply.writeString(_result32);
                    return true;
                case 92:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg086 = data.readString();
                    boolean updateAutomaticZenRule = updateAutomaticZenRule(_arg086, data.readInt() != 0 ? AutomaticZenRule.CREATOR.createFromParcel(data) : null);
                    reply.writeNoException();
                    reply.writeInt(updateAutomaticZenRule ? 1 : 0);
                    return true;
                case 93:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg087 = data.readString();
                    boolean removeAutomaticZenRule = removeAutomaticZenRule(_arg087);
                    reply.writeNoException();
                    reply.writeInt(removeAutomaticZenRule ? 1 : 0);
                    return true;
                case 94:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg088 = data.readString();
                    boolean removeAutomaticZenRules = removeAutomaticZenRules(_arg088);
                    reply.writeNoException();
                    reply.writeInt(removeAutomaticZenRules ? 1 : 0);
                    return true;
                case 95:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg089 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    int _result33 = getRuleInstanceCount(_arg089);
                    reply.writeNoException();
                    reply.writeInt(_result33);
                    return true;
                case 96:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg090 = data.readInt();
                    byte[] _result34 = getBackupPayload(_arg090);
                    reply.writeNoException();
                    reply.writeByteArray(_result34);
                    return true;
                case 97:
                    data.enforceInterface(DESCRIPTOR);
                    byte[] _arg091 = data.createByteArray();
                    applyRestore(_arg091, data.readInt());
                    reply.writeNoException();
                    return true;
                case 98:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg092 = data.readString();
                    ParceledListSlice _result35 = getAppActiveNotifications(_arg092, data.readInt());
                    reply.writeNoException();
                    if (_result35 != null) {
                        reply.writeInt(1);
                        _result35.writeToParcel(reply, 1);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                case 99:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg093 = data.readString();
                    int _result36 = getUnreadCount(_arg093);
                    reply.writeNoException();
                    reply.writeInt(_result36);
                    return true;
                case 100:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg094 = data.readInt();
                    List<StatusBarNotification> _result37 = getNotificationList(_arg094);
                    reply.writeNoException();
                    reply.writeTypedList(_result37);
                    return true;
                case 101:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg095 = data.readString();
                    setNotificationNumber(_arg095, data.readInt());
                    reply.writeNoException();
                    return true;
                case 102:
                    data.enforceInterface(DESCRIPTOR);
                    ComponentName _arg096 = data.readInt() != 0 ? ComponentName.CREATOR.createFromParcel(data) : null;
                    _arg1 = data.readInt() != 0;
                    setNotificationListenerAccessGrantedForAll(_arg096, _arg1);
                    reply.writeNoException();
                    return true;
                case 103:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg097 = _arg1;
                    setToastEnabled(_arg097);
                    reply.writeNoException();
                    return true;
                case 104:
                    data.enforceInterface(DESCRIPTOR);
                    _arg1 = data.readInt() != 0;
                    boolean _arg098 = _arg1;
                    setOsdEnabled(_arg098);
                    reply.writeNoException();
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements INotificationManager {
            private IBinder mRemote;

            public private protected Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public synchronized String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public synchronized void cancelAllNotifications(String pkg, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(userId);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void clearData(String pkg, int uid, boolean fromApp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeInt(fromApp ? 1 : 0);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void enqueueToast(String pkg, ITransientNotification callback, int duration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeInt(duration);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void cancelToast(String pkg, ITransientNotification callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void finishToken(String pkg, ITransientNotification callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void enqueueNotificationWithTag(String pkg, String opPkg, String tag, int id, Notification notification, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeString(opPkg);
                    _data.writeString(tag);
                    _data.writeInt(id);
                    if (notification != null) {
                        _data.writeInt(1);
                        notification.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized void cancelNotificationWithTag(String pkg, String tag, int id, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeString(tag);
                    _data.writeInt(id);
                    _data.writeInt(userId);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void setShowBadge(String pkg, int uid, boolean showBadge) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeInt(showBadge ? 1 : 0);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized boolean canShowBadge(String pkg, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void setNotificationsEnabledForPackage(String pkg, int uid, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeInt(enabled ? 1 : 0);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void setNotificationsEnabledWithImportanceLockForPackage(String pkg, int uid, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeInt(enabled ? 1 : 0);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            private protected boolean areNotificationsEnabledForPackage(String pkg, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized boolean areNotificationsEnabled(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized int getPackageImportance(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void createNotificationChannelGroups(String pkg, ParceledListSlice channelGroupList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    if (channelGroupList != null) {
                        _data.writeInt(1);
                        channelGroupList.writeToParcel(_data, 0);
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

            @Override // android.app.INotificationManager
            public synchronized void createNotificationChannels(String pkg, ParceledListSlice channelsList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    if (channelsList != null) {
                        _data.writeInt(1);
                        channelsList.writeToParcel(_data, 0);
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

            @Override // android.app.INotificationManager
            public synchronized void createNotificationChannelsForPackage(String pkg, int uid, ParceledListSlice channelsList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    if (channelsList != null) {
                        _data.writeInt(1);
                        channelsList.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized ParceledListSlice getNotificationChannelGroupsForPackage(String pkg, int uid, boolean includeDeleted) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeInt(includeDeleted ? 1 : 0);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized NotificationChannelGroup getNotificationChannelGroupForPackage(String groupId, String pkg, int uid) throws RemoteException {
                NotificationChannelGroup _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(groupId);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = NotificationChannelGroup.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized NotificationChannelGroup getPopulatedNotificationChannelGroupForPackage(String pkg, int uid, String groupId, boolean includeDeleted) throws RemoteException {
                NotificationChannelGroup _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeString(groupId);
                    _data.writeInt(includeDeleted ? 1 : 0);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = NotificationChannelGroup.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void updateNotificationChannelGroupForPackage(String pkg, int uid, NotificationChannelGroup group) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    if (group != null) {
                        _data.writeInt(1);
                        group.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void updateNotificationChannelForPackage(String pkg, int uid, NotificationChannel channel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    if (channel != null) {
                        _data.writeInt(1);
                        channel.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized NotificationChannel getNotificationChannel(String pkg, String channelId) throws RemoteException {
                NotificationChannel _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeString(channelId);
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = NotificationChannel.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized NotificationChannel getNotificationChannelForPackage(String pkg, int uid, String channelId, boolean includeDeleted) throws RemoteException {
                NotificationChannel _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeString(channelId);
                    _data.writeInt(includeDeleted ? 1 : 0);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = NotificationChannel.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void deleteNotificationChannel(String pkg, String channelId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeString(channelId);
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized ParceledListSlice getNotificationChannels(String pkg) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized ParceledListSlice getNotificationChannelsForPackage(String pkg, int uid, boolean includeDeleted) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeInt(includeDeleted ? 1 : 0);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized int getNumNotificationChannelsForPackage(String pkg, int uid, boolean includeDeleted) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    _data.writeInt(includeDeleted ? 1 : 0);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized int getDeletedChannelCount(String pkg, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized int getBlockedChannelCount(String pkg, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void deleteNotificationChannelGroup(String pkg, String channelGroupId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeString(channelGroupId);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized NotificationChannelGroup getNotificationChannelGroup(String pkg, String channelGroupId) throws RemoteException {
                NotificationChannelGroup _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeString(channelGroupId);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = NotificationChannelGroup.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized ParceledListSlice getNotificationChannelGroups(String pkg) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized boolean onlyHasDefaultChannel(String pkg, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(uid);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized ParceledListSlice getRecentNotifyingAppsForUser(int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized int getBlockedAppCount(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized boolean areChannelsBypassingDnd() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized StatusBarNotification[] getActiveNotifications(String callingPkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                    StatusBarNotification[] _result = (StatusBarNotification[]) _reply.createTypedArray(StatusBarNotification.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized StatusBarNotification[] getHistoricalNotifications(String callingPkg, int count) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeInt(count);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    StatusBarNotification[] _result = (StatusBarNotification[]) _reply.createTypedArray(StatusBarNotification.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void registerListener(INotificationListener listener, ComponentName component, int userid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userid);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void unregisterListener(INotificationListener listener, int userid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    _data.writeInt(userid);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void cancelNotificationFromListener(INotificationListener token, String pkg, String tag, int id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeString(pkg);
                    _data.writeString(tag);
                    _data.writeInt(id);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void cancelNotificationsFromListener(INotificationListener token, String[] keys) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeStringArray(keys);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void snoozeNotificationUntilContextFromListener(INotificationListener token, String key, String snoozeCriterionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeString(key);
                    _data.writeString(snoozeCriterionId);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void snoozeNotificationUntilFromListener(INotificationListener token, String key, long until) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeString(key);
                    _data.writeLong(until);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void requestBindListener(ComponentName component) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void requestUnbindListener(INotificationListener token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void requestBindProvider(ComponentName component) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
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

            @Override // android.app.INotificationManager
            public synchronized void requestUnbindProvider(IConditionProvider token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void setNotificationsShownFromListener(INotificationListener token, String[] keys) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeStringArray(keys);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized ParceledListSlice getActiveNotificationsFromListener(INotificationListener token, String[] keys, int trim) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ParceledListSlice _result = null;
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeStringArray(keys);
                    _data.writeInt(trim);
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized ParceledListSlice getSnoozedNotificationsFromListener(INotificationListener token, int trim) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ParceledListSlice _result = null;
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeInt(trim);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void requestHintsFromListener(INotificationListener token, int hints) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeInt(hints);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized int getHintsFromListener(INotificationListener token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    this.mRemote.transact(54, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void requestInterruptionFilterFromListener(INotificationListener token, int interruptionFilter) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeInt(interruptionFilter);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized int getInterruptionFilterFromListener(INotificationListener token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    this.mRemote.transact(56, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void setOnNotificationPostedTrimFromListener(INotificationListener token, int trim) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeInt(trim);
                    this.mRemote.transact(57, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void setInterruptionFilter(String pkg, int interruptionFilter) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(interruptionFilter);
                    this.mRemote.transact(58, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void updateNotificationChannelGroupFromPrivilegedListener(INotificationListener token, String pkg, UserHandle user, NotificationChannelGroup group) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeString(pkg);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (group != null) {
                        _data.writeInt(1);
                        group.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(59, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void updateNotificationChannelFromPrivilegedListener(INotificationListener token, String pkg, UserHandle user, NotificationChannel channel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeString(pkg);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (channel != null) {
                        _data.writeInt(1);
                        channel.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(60, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized ParceledListSlice getNotificationChannelsFromPrivilegedListener(INotificationListener token, String pkg, UserHandle user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ParceledListSlice _result = null;
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeString(pkg);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(61, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized ParceledListSlice getNotificationChannelGroupsFromPrivilegedListener(INotificationListener token, String pkg, UserHandle user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ParceledListSlice _result = null;
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeString(pkg);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(62, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void applyEnqueuedAdjustmentFromAssistant(INotificationListener token, Adjustment adjustment) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    if (adjustment != null) {
                        _data.writeInt(1);
                        adjustment.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(63, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void applyAdjustmentFromAssistant(INotificationListener token, Adjustment adjustment) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    if (adjustment != null) {
                        _data.writeInt(1);
                        adjustment.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(64, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void applyAdjustmentsFromAssistant(INotificationListener token, List<Adjustment> adjustments) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeTypedList(adjustments);
                    this.mRemote.transact(65, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void unsnoozeNotificationFromAssistant(INotificationListener token, String key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeString(key);
                    this.mRemote.transact(66, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized ComponentName getEffectsSuppressor() throws RemoteException {
                ComponentName _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(67, _data, _reply, 0);
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

            @Override // android.app.INotificationManager
            public synchronized boolean matchesCallFilter(Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(68, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized boolean isSystemConditionProviderEnabled(String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    this.mRemote.transact(69, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized boolean isNotificationListenerAccessGranted(ComponentName listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (listener != null) {
                        _data.writeInt(1);
                        listener.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(70, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized boolean isNotificationListenerAccessGrantedForUser(ComponentName listener, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (listener != null) {
                        _data.writeInt(1);
                        listener.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    this.mRemote.transact(71, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized boolean isNotificationAssistantAccessGranted(ComponentName assistant) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (assistant != null) {
                        _data.writeInt(1);
                        assistant.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(72, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void setNotificationListenerAccessGranted(ComponentName listener, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (listener != null) {
                        _data.writeInt(1);
                        listener.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(enabled ? 1 : 0);
                    this.mRemote.transact(73, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void setNotificationAssistantAccessGranted(ComponentName assistant, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (assistant != null) {
                        _data.writeInt(1);
                        assistant.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(enabled ? 1 : 0);
                    this.mRemote.transact(74, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void setNotificationListenerAccessGrantedForUser(ComponentName listener, int userId, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (listener != null) {
                        _data.writeInt(1);
                        listener.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    _data.writeInt(enabled ? 1 : 0);
                    this.mRemote.transact(75, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void setNotificationAssistantAccessGrantedForUser(ComponentName assistant, int userId, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (assistant != null) {
                        _data.writeInt(1);
                        assistant.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    _data.writeInt(enabled ? 1 : 0);
                    this.mRemote.transact(76, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized List<String> getEnabledNotificationListenerPackages() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(77, _data, _reply, 0);
                    _reply.readException();
                    List<String> _result = _reply.createStringArrayList();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized List<ComponentName> getEnabledNotificationListeners(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    this.mRemote.transact(78, _data, _reply, 0);
                    _reply.readException();
                    List<ComponentName> _result = _reply.createTypedArrayList(ComponentName.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized int getZenMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(79, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public synchronized ZenModeConfig getZenModeConfig() throws RemoteException {
                ZenModeConfig _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(80, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ZenModeConfig.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void setZenMode(int mode, Uri conditionId, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    if (conditionId != null) {
                        _data.writeInt(1);
                        conditionId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(reason);
                    this.mRemote.transact(81, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void notifyConditions(String pkg, IConditionProvider provider, Condition[] conditions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeStrongBinder(provider != null ? provider.asBinder() : null);
                    _data.writeTypedArray(conditions, 0);
                    this.mRemote.transact(82, _data, null, 1);
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized boolean isNotificationPolicyAccessGranted(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    this.mRemote.transact(83, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized NotificationManager.Policy getNotificationPolicy(String pkg) throws RemoteException {
                NotificationManager.Policy _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    this.mRemote.transact(84, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = NotificationManager.Policy.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void setNotificationPolicy(String pkg, NotificationManager.Policy policy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    if (policy != null) {
                        _data.writeInt(1);
                        policy.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(85, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized boolean isNotificationPolicyAccessGrantedForPackage(String pkg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    this.mRemote.transact(86, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void setNotificationPolicyAccessGranted(String pkg, boolean granted) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(granted ? 1 : 0);
                    this.mRemote.transact(87, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void setNotificationPolicyAccessGrantedForUser(String pkg, int userId, boolean granted) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(userId);
                    _data.writeInt(granted ? 1 : 0);
                    this.mRemote.transact(88, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized AutomaticZenRule getAutomaticZenRule(String id) throws RemoteException {
                AutomaticZenRule _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(id);
                    this.mRemote.transact(89, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = AutomaticZenRule.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized List<ZenModeConfig.ZenRule> getZenRules() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(90, _data, _reply, 0);
                    _reply.readException();
                    List<ZenModeConfig.ZenRule> _result = _reply.createTypedArrayList(ZenModeConfig.ZenRule.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized String addAutomaticZenRule(AutomaticZenRule automaticZenRule) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (automaticZenRule != null) {
                        _data.writeInt(1);
                        automaticZenRule.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(91, _data, _reply, 0);
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized boolean updateAutomaticZenRule(String id, AutomaticZenRule automaticZenRule) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(id);
                    if (automaticZenRule != null) {
                        _data.writeInt(1);
                        automaticZenRule.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(92, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized boolean removeAutomaticZenRule(String id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(id);
                    this.mRemote.transact(93, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized boolean removeAutomaticZenRules(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(94, _data, _reply, 0);
                    _reply.readException();
                    boolean _result = _reply.readInt() != 0;
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized int getRuleInstanceCount(ComponentName owner) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (owner != null) {
                        _data.writeInt(1);
                        owner.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(95, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized byte[] getBackupPayload(int user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(user);
                    this.mRemote.transact(96, _data, _reply, 0);
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized void applyRestore(byte[] payload, int user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(payload);
                    _data.writeInt(user);
                    this.mRemote.transact(97, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public synchronized ParceledListSlice getAppActiveNotifications(String callingPkg, int userId) throws RemoteException {
                ParceledListSlice _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeInt(userId);
                    this.mRemote.transact(98, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public int getUnreadCount(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(99, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public List<StatusBarNotification> getNotificationList(int flag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flag);
                    this.mRemote.transact(100, _data, _reply, 0);
                    _reply.readException();
                    List<StatusBarNotification> _result = _reply.createTypedArrayList(StatusBarNotification.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public void setNotificationNumber(String key, int number) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeInt(number);
                    this.mRemote.transact(101, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public void setNotificationListenerAccessGrantedForAll(ComponentName listener, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (listener != null) {
                        _data.writeInt(1);
                        listener.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(enabled ? 1 : 0);
                    this.mRemote.transact(102, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public void setToastEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    this.mRemote.transact(103, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.app.INotificationManager
            public void setOsdEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    this.mRemote.transact(104, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
