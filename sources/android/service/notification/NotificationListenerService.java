package android.service.notification;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.app.INotificationManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.Person;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ParceledListSlice;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.service.notification.INotificationListener;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.RemoteViews;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.SomeArgs;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* loaded from: classes2.dex */
public abstract class NotificationListenerService extends Service {
    public static final int HINT_HOST_DISABLE_CALL_EFFECTS = 4;
    public static final int HINT_HOST_DISABLE_EFFECTS = 1;
    public static final int HINT_HOST_DISABLE_NOTIFICATION_EFFECTS = 2;
    public static final int INTERRUPTION_FILTER_ALARMS = 4;
    public static final int INTERRUPTION_FILTER_ALL = 1;
    public static final int INTERRUPTION_FILTER_NONE = 3;
    public static final int INTERRUPTION_FILTER_PRIORITY = 2;
    public static final int INTERRUPTION_FILTER_UNKNOWN = 0;
    public static final int NOTIFICATION_CHANNEL_OR_GROUP_ADDED = 1;
    public static final int NOTIFICATION_CHANNEL_OR_GROUP_DELETED = 3;
    public static final int NOTIFICATION_CHANNEL_OR_GROUP_UPDATED = 2;
    public static final int REASON_APP_CANCEL = 8;
    public static final int REASON_APP_CANCEL_ALL = 9;
    public static final int REASON_CANCEL = 2;
    public static final int REASON_CANCEL_ALL = 3;
    public static final int REASON_CHANNEL_BANNED = 17;
    public static final int REASON_CLICK = 1;
    public static final int REASON_ERROR = 4;
    public static final int REASON_GROUP_OPTIMIZATION = 13;
    public static final int REASON_GROUP_SUMMARY_CANCELED = 12;
    public static final int REASON_LISTENER_CANCEL = 10;
    public static final int REASON_LISTENER_CANCEL_ALL = 11;
    public static final int REASON_PACKAGE_BANNED = 7;
    public static final int REASON_PACKAGE_CHANGED = 5;
    public static final int REASON_PACKAGE_SUSPENDED = 14;
    public static final int REASON_PROFILE_TURNED_OFF = 15;
    public static final int REASON_SNOOZED = 18;
    public static final int REASON_TIMEOUT = 19;
    public static final int REASON_UNAUTOBUNDLED = 16;
    public static final int REASON_USER_STOPPED = 6;
    public static final String SERVICE_INTERFACE = "android.service.notification.NotificationListenerService";
    @Deprecated
    public static final int SUPPRESSED_EFFECT_SCREEN_OFF = 1;
    @Deprecated
    public static final int SUPPRESSED_EFFECT_SCREEN_ON = 2;
    @SystemApi
    public static final int TRIM_FULL = 0;
    @SystemApi
    public static final int TRIM_LIGHT = 1;
    protected int mCurrentUser;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private Handler mHandler;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    protected INotificationManager mNoMan;
    @GuardedBy({"mLock"})
    private RankingMap mRankingMap;
    protected Context mSystemContext;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private final String TAG = getClass().getSimpleName();
    private final Object mLock = new Object();
    @UnsupportedAppUsage
    protected NotificationListenerWrapper mWrapper = null;
    private boolean isConnected = false;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface ChannelOrGroupModificationTypes {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.content.ContextWrapper
    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        this.mHandler = new MyHandler(getMainLooper());
    }

    public void onNotificationPosted(StatusBarNotification sbn) {
    }

    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
        onNotificationPosted(sbn);
    }

    public void onNotificationRemoved(StatusBarNotification sbn) {
    }

    public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap) {
        try {
            if (checkNotificationNotNull(sbn)) {
                onNotificationRemoved(sbn);
            }
        } catch (Exception e) {
        }
    }

    private boolean checkNotificationNotNull(StatusBarNotification sbn) {
        if (sbn != null && sbn.getNotification() != null) {
            return true;
        }
        return false;
    }

    public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap, int reason) {
        onNotificationRemoved(sbn, rankingMap);
    }

    @SystemApi
    public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap, NotificationStats stats, int reason) {
        onNotificationRemoved(sbn, rankingMap, reason);
    }

    public void onListenerConnected() {
    }

    public void onListenerDisconnected() {
    }

    public void onNotificationRankingUpdate(RankingMap rankingMap) {
    }

    public void onListenerHintsChanged(int hints) {
    }

    public void onSilentStatusBarIconsVisibilityChanged(boolean hideSilentStatusIcons) {
    }

    public void onNotificationChannelModified(String pkg, UserHandle user, NotificationChannel channel, int modificationType) {
    }

    public void onNotificationChannelGroupModified(String pkg, UserHandle user, NotificationChannelGroup group, int modificationType) {
    }

    public void onInterruptionFilterChanged(int interruptionFilter) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public final INotificationManager getNotificationInterface() {
        if (this.mNoMan == null) {
            this.mNoMan = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
        }
        return this.mNoMan;
    }

    @Deprecated
    public final void cancelNotification(String pkg, String tag, int id) {
        if (isBound()) {
            try {
                getNotificationInterface().cancelNotificationFromListener(this.mWrapper, pkg, tag, id);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public final void cancelNotification(String key) {
        if (isBound()) {
            try {
                getNotificationInterface().cancelNotificationsFromListener(this.mWrapper, new String[]{key});
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public final void cancelAllNotifications() {
        cancelNotifications(null);
    }

    public final void cancelNotifications(String[] keys) {
        if (isBound()) {
            try {
                getNotificationInterface().cancelNotificationsFromListener(this.mWrapper, keys);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    @SystemApi
    public final void snoozeNotification(String key, String snoozeCriterionId) {
        if (isBound()) {
            try {
                getNotificationInterface().snoozeNotificationUntilContextFromListener(this.mWrapper, key, snoozeCriterionId);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public final void snoozeNotification(String key, long durationMs) {
        if (isBound()) {
            try {
                getNotificationInterface().snoozeNotificationUntilFromListener(this.mWrapper, key, durationMs);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public final void setNotificationsShown(String[] keys) {
        if (isBound()) {
            try {
                getNotificationInterface().setNotificationsShownFromListener(this.mWrapper, keys);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public final void updateNotificationChannel(String pkg, UserHandle user, NotificationChannel channel) {
        if (isBound()) {
            try {
                getNotificationInterface().updateNotificationChannelFromPrivilegedListener(this.mWrapper, pkg, user, channel);
            } catch (RemoteException e) {
                Log.v(this.TAG, "Unable to contact notification manager", e);
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public final List<NotificationChannel> getNotificationChannels(String pkg, UserHandle user) {
        if (isBound()) {
            try {
                return getNotificationInterface().getNotificationChannelsFromPrivilegedListener(this.mWrapper, pkg, user).getList();
            } catch (RemoteException e) {
                Log.v(this.TAG, "Unable to contact notification manager", e);
                throw e.rethrowFromSystemServer();
            }
        }
        return null;
    }

    public final List<NotificationChannelGroup> getNotificationChannelGroups(String pkg, UserHandle user) {
        if (isBound()) {
            try {
                return getNotificationInterface().getNotificationChannelGroupsFromPrivilegedListener(this.mWrapper, pkg, user).getList();
            } catch (RemoteException e) {
                Log.v(this.TAG, "Unable to contact notification manager", e);
                throw e.rethrowFromSystemServer();
            }
        }
        return null;
    }

    @SystemApi
    public final void setOnNotificationPostedTrim(int trim) {
        if (isBound()) {
            try {
                getNotificationInterface().setOnNotificationPostedTrimFromListener(this.mWrapper, trim);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public StatusBarNotification[] getActiveNotifications() {
        StatusBarNotification[] activeNotifications = getActiveNotifications(null, 0);
        return activeNotifications != null ? activeNotifications : new StatusBarNotification[0];
    }

    public final StatusBarNotification[] getSnoozedNotifications() {
        try {
            ParceledListSlice<StatusBarNotification> parceledList = getNotificationInterface().getSnoozedNotificationsFromListener(this.mWrapper, 0);
            return cleanUpNotificationList(parceledList);
        } catch (RemoteException ex) {
            Log.v(this.TAG, "Unable to contact notification manager", ex);
            return null;
        }
    }

    @SystemApi
    public StatusBarNotification[] getActiveNotifications(int trim) {
        StatusBarNotification[] activeNotifications = getActiveNotifications(null, trim);
        return activeNotifications != null ? activeNotifications : new StatusBarNotification[0];
    }

    public StatusBarNotification[] getActiveNotifications(String[] keys) {
        StatusBarNotification[] activeNotifications = getActiveNotifications(keys, 0);
        return activeNotifications != null ? activeNotifications : new StatusBarNotification[0];
    }

    @SystemApi
    public StatusBarNotification[] getActiveNotifications(String[] keys, int trim) {
        if (isBound()) {
            try {
                ParceledListSlice<StatusBarNotification> parceledList = getNotificationInterface().getActiveNotificationsFromListener(this.mWrapper, keys, trim);
                return cleanUpNotificationList(parceledList);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
                return null;
            }
        }
        return null;
    }

    private StatusBarNotification[] cleanUpNotificationList(ParceledListSlice<StatusBarNotification> parceledList) {
        if (parceledList == null || parceledList.getList() == null) {
            return new StatusBarNotification[0];
        }
        List<StatusBarNotification> list = parceledList.getList();
        ArrayList<StatusBarNotification> corruptNotifications = null;
        int N = list.size();
        for (int i = 0; i < N; i++) {
            StatusBarNotification sbn = list.get(i);
            Notification notification = sbn.getNotification();
            try {
                createLegacyIconExtras(notification);
                maybePopulateRemoteViews(notification);
                maybePopulatePeople(notification);
            } catch (IllegalArgumentException e) {
                if (corruptNotifications == null) {
                    corruptNotifications = new ArrayList<>(N);
                }
                corruptNotifications.add(sbn);
                String str = this.TAG;
                Log.w(str, "get(Active/Snoozed)Notifications: can't rebuild notification from " + sbn.getPackageName());
            }
        }
        if (corruptNotifications != null) {
            list.removeAll(corruptNotifications);
        }
        return (StatusBarNotification[]) list.toArray(new StatusBarNotification[list.size()]);
    }

    public final int getCurrentListenerHints() {
        if (isBound()) {
            try {
                return getNotificationInterface().getHintsFromListener(this.mWrapper);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
                return 0;
            }
        }
        return 0;
    }

    public final int getCurrentInterruptionFilter() {
        if (isBound()) {
            try {
                return getNotificationInterface().getInterruptionFilterFromListener(this.mWrapper);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
                return 0;
            }
        }
        return 0;
    }

    public final void clearRequestedListenerHints() {
        if (isBound()) {
            try {
                getNotificationInterface().clearRequestedListenerHints(this.mWrapper);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public final void requestListenerHints(int hints) {
        if (isBound()) {
            try {
                getNotificationInterface().requestHintsFromListener(this.mWrapper, hints);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public final void requestInterruptionFilter(int interruptionFilter) {
        if (isBound()) {
            try {
                getNotificationInterface().requestInterruptionFilterFromListener(this.mWrapper, interruptionFilter);
            } catch (RemoteException ex) {
                Log.v(this.TAG, "Unable to contact notification manager", ex);
            }
        }
    }

    public RankingMap getCurrentRanking() {
        RankingMap rankingMap;
        synchronized (this.mLock) {
            rankingMap = this.mRankingMap;
        }
        return rankingMap;
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        if (this.mWrapper == null) {
            this.mWrapper = new NotificationListenerWrapper();
        }
        return this.mWrapper;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @UnsupportedAppUsage
    public boolean isBound() {
        if (this.mWrapper == null) {
            Log.w(this.TAG, "Notification listener service not yet bound.");
            return false;
        }
        return true;
    }

    @Override // android.app.Service
    public void onDestroy() {
        onListenerDisconnected();
        super.onDestroy();
    }

    @SystemApi
    public void registerAsSystemService(Context context, ComponentName componentName, int currentUser) throws RemoteException {
        if (this.mWrapper == null) {
            this.mWrapper = new NotificationListenerWrapper();
        }
        this.mSystemContext = context;
        INotificationManager noMan = getNotificationInterface();
        this.mHandler = new MyHandler(context.getMainLooper());
        this.mCurrentUser = currentUser;
        noMan.registerListener(this.mWrapper, componentName, currentUser);
    }

    @SystemApi
    public void unregisterAsSystemService() throws RemoteException {
        if (this.mWrapper != null) {
            INotificationManager noMan = getNotificationInterface();
            noMan.unregisterListener(this.mWrapper, this.mCurrentUser);
        }
    }

    public static void requestRebind(ComponentName componentName) {
        INotificationManager noMan = INotificationManager.Stub.asInterface(ServiceManager.getService("notification"));
        try {
            noMan.requestBindListener(componentName);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public final void requestUnbind() {
        if (this.mWrapper != null) {
            INotificationManager noMan = getNotificationInterface();
            try {
                noMan.requestUnbindListener(this.mWrapper);
                this.isConnected = false;
            } catch (RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        }
    }

    public final void createLegacyIconExtras(Notification n) {
        Drawable d;
        if (getContext().getApplicationInfo().targetSdkVersion < 23) {
            Icon smallIcon = n.getSmallIcon();
            Icon largeIcon = n.getLargeIcon();
            if (smallIcon != null && smallIcon.getType() == 2) {
                n.extras.putInt(Notification.EXTRA_SMALL_ICON, smallIcon.getResId());
                n.icon = smallIcon.getResId();
            }
            if (largeIcon != null && (d = largeIcon.loadDrawable(getContext())) != null && (d instanceof BitmapDrawable)) {
                Bitmap largeIconBits = ((BitmapDrawable) d).getBitmap();
                n.extras.putParcelable(Notification.EXTRA_LARGE_ICON, largeIconBits);
                n.largeIcon = largeIconBits;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybePopulateRemoteViews(Notification notification) {
        if (getContext().getApplicationInfo().targetSdkVersion < 24) {
            Notification.Builder builder = Notification.Builder.recoverBuilder(getContext(), notification);
            RemoteViews content = builder.createContentView();
            RemoteViews big = builder.createBigContentView();
            RemoteViews headsUp = builder.createHeadsUpContentView();
            notification.contentView = content;
            notification.bigContentView = big;
            notification.headsUpContentView = headsUp;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybePopulatePeople(Notification notification) {
        ArrayList<Person> people;
        if (getContext().getApplicationInfo().targetSdkVersion < 28 && (people = notification.extras.getParcelableArrayList(Notification.EXTRA_PEOPLE_LIST)) != null && people.isEmpty()) {
            int size = people.size();
            String[] peopleArray = new String[size];
            for (int i = 0; i < size; i++) {
                Person person = people.get(i);
                peopleArray[i] = person.resolveToLegacyUri();
            }
            notification.extras.putStringArray(Notification.EXTRA_PEOPLE, peopleArray);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: classes2.dex */
    public class NotificationListenerWrapper extends INotificationListener.Stub {
        /* JADX INFO: Access modifiers changed from: protected */
        public NotificationListenerWrapper() {
        }

        @Override // android.service.notification.INotificationListener
        public void onNotificationPosted(IStatusBarNotificationHolder sbnHolder, NotificationRankingUpdate update) {
            try {
                StatusBarNotification sbn = sbnHolder.get();
                try {
                    NotificationListenerService.this.createLegacyIconExtras(sbn.getNotification());
                    NotificationListenerService.this.maybePopulateRemoteViews(sbn.getNotification());
                    NotificationListenerService.this.maybePopulatePeople(sbn.getNotification());
                } catch (IllegalArgumentException e) {
                    String str = NotificationListenerService.this.TAG;
                    Log.w(str, "onNotificationPosted: can't rebuild notification from " + sbn.getPackageName());
                    sbn = null;
                }
                synchronized (NotificationListenerService.this.mLock) {
                    NotificationListenerService.this.applyUpdateLocked(update);
                    if (sbn != null) {
                        SomeArgs args = SomeArgs.obtain();
                        args.arg1 = sbn;
                        args.arg2 = NotificationListenerService.this.mRankingMap;
                        NotificationListenerService.this.mHandler.obtainMessage(1, args).sendToTarget();
                    } else {
                        NotificationListenerService.this.mHandler.obtainMessage(4, NotificationListenerService.this.mRankingMap).sendToTarget();
                    }
                }
            } catch (RemoteException e2) {
                Log.w(NotificationListenerService.this.TAG, "onNotificationPosted: Error receiving StatusBarNotification", e2);
            }
        }

        @Override // android.service.notification.INotificationListener
        public void onNotificationRemoved(IStatusBarNotificationHolder sbnHolder, NotificationRankingUpdate update, NotificationStats stats, int reason) {
            try {
                StatusBarNotification sbn = sbnHolder.get();
                synchronized (NotificationListenerService.this.mLock) {
                    NotificationListenerService.this.applyUpdateLocked(update);
                    SomeArgs args = SomeArgs.obtain();
                    args.arg1 = sbn;
                    args.arg2 = NotificationListenerService.this.mRankingMap;
                    args.arg3 = Integer.valueOf(reason);
                    args.arg4 = stats;
                    NotificationListenerService.this.mHandler.obtainMessage(2, args).sendToTarget();
                }
            } catch (RemoteException e) {
                Log.w(NotificationListenerService.this.TAG, "onNotificationRemoved: Error receiving StatusBarNotification", e);
            }
        }

        @Override // android.service.notification.INotificationListener
        public void onListenerConnected(NotificationRankingUpdate update) {
            synchronized (NotificationListenerService.this.mLock) {
                NotificationListenerService.this.applyUpdateLocked(update);
            }
            NotificationListenerService.this.isConnected = true;
            NotificationListenerService.this.mHandler.obtainMessage(3).sendToTarget();
        }

        @Override // android.service.notification.INotificationListener
        public void onNotificationRankingUpdate(NotificationRankingUpdate update) throws RemoteException {
            synchronized (NotificationListenerService.this.mLock) {
                NotificationListenerService.this.applyUpdateLocked(update);
                NotificationListenerService.this.mHandler.obtainMessage(4, NotificationListenerService.this.mRankingMap).sendToTarget();
            }
        }

        @Override // android.service.notification.INotificationListener
        public void onListenerHintsChanged(int hints) throws RemoteException {
            NotificationListenerService.this.mHandler.obtainMessage(5, hints, 0).sendToTarget();
        }

        @Override // android.service.notification.INotificationListener
        public void onInterruptionFilterChanged(int interruptionFilter) throws RemoteException {
            NotificationListenerService.this.mHandler.obtainMessage(6, interruptionFilter, 0).sendToTarget();
        }

        public void onNotificationEnqueuedWithChannel(IStatusBarNotificationHolder notificationHolder, NotificationChannel channel) throws RemoteException {
        }

        public void onNotificationsSeen(List<String> keys) throws RemoteException {
        }

        public void onNotificationSnoozedUntilContext(IStatusBarNotificationHolder notificationHolder, String snoozeCriterionId) throws RemoteException {
        }

        public void onNotificationExpansionChanged(String key, boolean isUserAction, boolean isExpanded) {
        }

        public void onNotificationDirectReply(String key) {
        }

        public void onSuggestedReplySent(String key, CharSequence reply, int source) {
        }

        public void onActionClicked(String key, Notification.Action action, int source) {
        }

        public void onAllowedAdjustmentsChanged() {
        }

        @Override // android.service.notification.INotificationListener
        public void onNotificationChannelModification(String pkgName, UserHandle user, NotificationChannel channel, int modificationType) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = pkgName;
            args.arg2 = user;
            args.arg3 = channel;
            args.arg4 = Integer.valueOf(modificationType);
            NotificationListenerService.this.mHandler.obtainMessage(7, args).sendToTarget();
        }

        @Override // android.service.notification.INotificationListener
        public void onNotificationChannelGroupModification(String pkgName, UserHandle user, NotificationChannelGroup group, int modificationType) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = pkgName;
            args.arg2 = user;
            args.arg3 = group;
            args.arg4 = Integer.valueOf(modificationType);
            NotificationListenerService.this.mHandler.obtainMessage(8, args).sendToTarget();
        }

        @Override // android.service.notification.INotificationListener
        public void onStatusBarIconsBehaviorChanged(boolean hideSilentStatusIcons) {
            NotificationListenerService.this.mHandler.obtainMessage(9, Boolean.valueOf(hideSilentStatusIcons)).sendToTarget();
        }
    }

    @GuardedBy({"mLock"})
    public final void applyUpdateLocked(NotificationRankingUpdate update) {
        this.mRankingMap = update.getRankingMap();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Context getContext() {
        Context context = this.mSystemContext;
        if (context != null) {
            return context;
        }
        return this;
    }

    /* loaded from: classes2.dex */
    public static class Ranking {
        private static final int PARCEL_VERSION = 2;
        public static final int USER_SENTIMENT_NEGATIVE = -1;
        public static final int USER_SENTIMENT_NEUTRAL = 0;
        public static final int USER_SENTIMENT_POSITIVE = 1;
        public static final int VISIBILITY_NO_OVERRIDE = -1000;
        private boolean mCanBubble;
        private NotificationChannel mChannel;
        private boolean mHidden;
        private int mImportance;
        private CharSequence mImportanceExplanation;
        private boolean mIsAmbient;
        private String mKey;
        private long mLastAudiblyAlertedMs;
        private boolean mMatchesInterruptionFilter;
        private boolean mNoisy;
        private String mOverrideGroupKey;
        private ArrayList<String> mOverridePeople;
        private int mRank;
        private boolean mShowBadge;
        private ArrayList<Notification.Action> mSmartActions;
        private ArrayList<CharSequence> mSmartReplies;
        private ArrayList<SnoozeCriterion> mSnoozeCriteria;
        private int mSuppressedVisualEffects;
        private int mUserSentiment;
        private int mVisibilityOverride;
        private boolean mVisuallyInterruptive;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes2.dex */
        public @interface UserSentiment {
        }

        public Ranking() {
            this.mRank = -1;
            this.mUserSentiment = 0;
        }

        @VisibleForTesting
        public void writeToParcel(Parcel out, int flags) {
            out.dataPosition();
            out.writeInt(2);
            out.writeString(this.mKey);
            out.writeInt(this.mRank);
            out.writeBoolean(this.mIsAmbient);
            out.writeBoolean(this.mMatchesInterruptionFilter);
            out.writeInt(this.mVisibilityOverride);
            out.writeInt(this.mSuppressedVisualEffects);
            out.writeInt(this.mImportance);
            out.writeCharSequence(this.mImportanceExplanation);
            out.writeString(this.mOverrideGroupKey);
            out.writeParcelable(this.mChannel, flags);
            out.writeStringList(this.mOverridePeople);
            out.writeTypedList(this.mSnoozeCriteria, flags);
            out.writeBoolean(this.mShowBadge);
            out.writeInt(this.mUserSentiment);
            out.writeBoolean(this.mHidden);
            out.writeLong(this.mLastAudiblyAlertedMs);
            out.writeBoolean(this.mNoisy);
            out.writeTypedList(this.mSmartActions, flags);
            out.writeCharSequenceList(this.mSmartReplies);
            out.writeBoolean(this.mCanBubble);
            out.writeBoolean(this.mVisuallyInterruptive);
        }

        @VisibleForTesting
        public Ranking(Parcel in) {
            this.mRank = -1;
            this.mUserSentiment = 0;
            ClassLoader cl = getClass().getClassLoader();
            int version = in.readInt();
            if (version != 2) {
                throw new IllegalArgumentException("malformed Ranking parcel: " + in + " version " + version + ", expected 2");
            }
            this.mKey = in.readString();
            this.mRank = in.readInt();
            this.mIsAmbient = in.readBoolean();
            this.mMatchesInterruptionFilter = in.readBoolean();
            this.mVisibilityOverride = in.readInt();
            this.mSuppressedVisualEffects = in.readInt();
            this.mImportance = in.readInt();
            this.mImportanceExplanation = in.readCharSequence();
            this.mOverrideGroupKey = in.readString();
            this.mChannel = (NotificationChannel) in.readParcelable(cl);
            this.mOverridePeople = in.createStringArrayList();
            this.mSnoozeCriteria = in.createTypedArrayList(SnoozeCriterion.CREATOR);
            this.mShowBadge = in.readBoolean();
            this.mUserSentiment = in.readInt();
            this.mHidden = in.readBoolean();
            this.mLastAudiblyAlertedMs = in.readLong();
            this.mNoisy = in.readBoolean();
            this.mSmartActions = in.createTypedArrayList(Notification.Action.CREATOR);
            this.mSmartReplies = in.readCharSequenceList();
            this.mCanBubble = in.readBoolean();
            this.mVisuallyInterruptive = in.readBoolean();
        }

        public String getKey() {
            return this.mKey;
        }

        public int getRank() {
            return this.mRank;
        }

        public boolean isAmbient() {
            return this.mIsAmbient;
        }

        @UnsupportedAppUsage
        public int getVisibilityOverride() {
            return this.mVisibilityOverride;
        }

        public int getSuppressedVisualEffects() {
            return this.mSuppressedVisualEffects;
        }

        public boolean matchesInterruptionFilter() {
            return this.mMatchesInterruptionFilter;
        }

        public int getImportance() {
            return this.mImportance;
        }

        public CharSequence getImportanceExplanation() {
            return this.mImportanceExplanation;
        }

        public String getOverrideGroupKey() {
            return this.mOverrideGroupKey;
        }

        public NotificationChannel getChannel() {
            return this.mChannel;
        }

        public int getUserSentiment() {
            return this.mUserSentiment;
        }

        @SystemApi
        public List<String> getAdditionalPeople() {
            return this.mOverridePeople;
        }

        @SystemApi
        public List<SnoozeCriterion> getSnoozeCriteria() {
            return this.mSnoozeCriteria;
        }

        public List<Notification.Action> getSmartActions() {
            return this.mSmartActions;
        }

        public List<CharSequence> getSmartReplies() {
            return this.mSmartReplies;
        }

        public boolean canShowBadge() {
            return this.mShowBadge;
        }

        public boolean isSuspended() {
            return this.mHidden;
        }

        public long getLastAudiblyAlertedMillis() {
            return this.mLastAudiblyAlertedMs;
        }

        public boolean canBubble() {
            return this.mCanBubble;
        }

        public boolean visuallyInterruptive() {
            return this.mVisuallyInterruptive;
        }

        public boolean isNoisy() {
            return this.mNoisy;
        }

        @VisibleForTesting
        public void populate(String key, int rank, boolean matchesInterruptionFilter, int visibilityOverride, int suppressedVisualEffects, int importance, CharSequence explanation, String overrideGroupKey, NotificationChannel channel, ArrayList<String> overridePeople, ArrayList<SnoozeCriterion> snoozeCriteria, boolean showBadge, int userSentiment, boolean hidden, long lastAudiblyAlertedMs, boolean noisy, ArrayList<Notification.Action> smartActions, ArrayList<CharSequence> smartReplies, boolean canBubble, boolean visuallyInterruptive) {
            this.mKey = key;
            this.mRank = rank;
            this.mIsAmbient = importance < 2;
            this.mMatchesInterruptionFilter = matchesInterruptionFilter;
            this.mVisibilityOverride = visibilityOverride;
            this.mSuppressedVisualEffects = suppressedVisualEffects;
            this.mImportance = importance;
            this.mImportanceExplanation = explanation;
            this.mOverrideGroupKey = overrideGroupKey;
            this.mChannel = channel;
            this.mOverridePeople = overridePeople;
            this.mSnoozeCriteria = snoozeCriteria;
            this.mShowBadge = showBadge;
            this.mUserSentiment = userSentiment;
            this.mHidden = hidden;
            this.mLastAudiblyAlertedMs = lastAudiblyAlertedMs;
            this.mNoisy = noisy;
            this.mSmartActions = smartActions;
            this.mSmartReplies = smartReplies;
            this.mCanBubble = canBubble;
            this.mVisuallyInterruptive = visuallyInterruptive;
        }

        public void populate(Ranking other) {
            populate(other.mKey, other.mRank, other.mMatchesInterruptionFilter, other.mVisibilityOverride, other.mSuppressedVisualEffects, other.mImportance, other.mImportanceExplanation, other.mOverrideGroupKey, other.mChannel, other.mOverridePeople, other.mSnoozeCriteria, other.mShowBadge, other.mUserSentiment, other.mHidden, other.mLastAudiblyAlertedMs, other.mNoisy, other.mSmartActions, other.mSmartReplies, other.mCanBubble, other.mVisuallyInterruptive);
        }

        public static String importanceToString(int importance) {
            if (importance != -1000) {
                if (importance != 0) {
                    if (importance != 1) {
                        if (importance != 2) {
                            if (importance != 3) {
                                if (importance == 4 || importance == 5) {
                                    return "HIGH";
                                }
                                return "UNKNOWN(" + String.valueOf(importance) + ")";
                            }
                            return "DEFAULT";
                        }
                        return "LOW";
                    }
                    return "MIN";
                }
                return "NONE";
            }
            return "UNSPECIFIED";
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Ranking other = (Ranking) o;
            if (Objects.equals(this.mKey, other.mKey) && Objects.equals(Integer.valueOf(this.mRank), Integer.valueOf(other.mRank)) && Objects.equals(Boolean.valueOf(this.mMatchesInterruptionFilter), Boolean.valueOf(other.mMatchesInterruptionFilter)) && Objects.equals(Integer.valueOf(this.mVisibilityOverride), Integer.valueOf(other.mVisibilityOverride)) && Objects.equals(Integer.valueOf(this.mSuppressedVisualEffects), Integer.valueOf(other.mSuppressedVisualEffects)) && Objects.equals(Integer.valueOf(this.mImportance), Integer.valueOf(other.mImportance)) && Objects.equals(this.mImportanceExplanation, other.mImportanceExplanation) && Objects.equals(this.mOverrideGroupKey, other.mOverrideGroupKey) && Objects.equals(this.mChannel, other.mChannel) && Objects.equals(this.mOverridePeople, other.mOverridePeople) && Objects.equals(this.mSnoozeCriteria, other.mSnoozeCriteria) && Objects.equals(Boolean.valueOf(this.mShowBadge), Boolean.valueOf(other.mShowBadge)) && Objects.equals(Integer.valueOf(this.mUserSentiment), Integer.valueOf(other.mUserSentiment)) && Objects.equals(Boolean.valueOf(this.mHidden), Boolean.valueOf(other.mHidden)) && Objects.equals(Long.valueOf(this.mLastAudiblyAlertedMs), Long.valueOf(other.mLastAudiblyAlertedMs)) && Objects.equals(Boolean.valueOf(this.mNoisy), Boolean.valueOf(other.mNoisy))) {
                ArrayList<Notification.Action> arrayList = this.mSmartActions;
                int size = arrayList == null ? 0 : arrayList.size();
                ArrayList<Notification.Action> arrayList2 = other.mSmartActions;
                if (size == (arrayList2 == null ? 0 : arrayList2.size()) && Objects.equals(this.mSmartReplies, other.mSmartReplies) && Objects.equals(Boolean.valueOf(this.mCanBubble), Boolean.valueOf(other.mCanBubble)) && Objects.equals(Boolean.valueOf(this.mVisuallyInterruptive), Boolean.valueOf(other.mVisuallyInterruptive))) {
                    return true;
                }
            }
            return false;
        }
    }

    /* loaded from: classes2.dex */
    public static class RankingMap implements Parcelable {
        public static final Parcelable.Creator<RankingMap> CREATOR = new Parcelable.Creator<RankingMap>() { // from class: android.service.notification.NotificationListenerService.RankingMap.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public RankingMap createFromParcel(Parcel source) {
                return new RankingMap(source);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public RankingMap[] newArray(int size) {
                return new RankingMap[size];
            }
        };
        private ArrayList<String> mOrderedKeys;
        private ArrayMap<String, Ranking> mRankings;

        public RankingMap(Ranking[] rankings) {
            this.mOrderedKeys = new ArrayList<>();
            this.mRankings = new ArrayMap<>();
            for (int i = 0; i < rankings.length; i++) {
                String key = rankings[i].getKey();
                this.mOrderedKeys.add(key);
                this.mRankings.put(key, rankings[i]);
            }
        }

        private RankingMap(Parcel in) {
            this.mOrderedKeys = new ArrayList<>();
            this.mRankings = new ArrayMap<>();
            getClass().getClassLoader();
            int count = in.readInt();
            this.mOrderedKeys.ensureCapacity(count);
            this.mRankings.ensureCapacity(count);
            for (int i = 0; i < count; i++) {
                Ranking r = new Ranking(in);
                String key = r.getKey();
                this.mOrderedKeys.add(key);
                this.mRankings.put(key, r);
            }
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            RankingMap other = (RankingMap) o;
            if (this.mOrderedKeys.equals(other.mOrderedKeys) && this.mRankings.equals(other.mRankings)) {
                return true;
            }
            return false;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            int count = this.mOrderedKeys.size();
            out.writeInt(count);
            for (int i = 0; i < count; i++) {
                this.mRankings.get(this.mOrderedKeys.get(i)).writeToParcel(out, flags);
            }
        }

        public String[] getOrderedKeys() {
            return (String[]) this.mOrderedKeys.toArray(new String[0]);
        }

        public boolean getRanking(String key, Ranking outRanking) {
            if (this.mRankings.containsKey(key)) {
                outRanking.populate(this.mRankings.get(key));
                return true;
            }
            return false;
        }

        @VisibleForTesting
        public Ranking getRawRankingObject(String key) {
            return this.mRankings.get(key);
        }
    }

    /* loaded from: classes2.dex */
    private final class MyHandler extends Handler {
        public static final int MSG_ON_INTERRUPTION_FILTER_CHANGED = 6;
        public static final int MSG_ON_LISTENER_CONNECTED = 3;
        public static final int MSG_ON_LISTENER_HINTS_CHANGED = 5;
        public static final int MSG_ON_NOTIFICATION_CHANNEL_GROUP_MODIFIED = 8;
        public static final int MSG_ON_NOTIFICATION_CHANNEL_MODIFIED = 7;
        public static final int MSG_ON_NOTIFICATION_POSTED = 1;
        public static final int MSG_ON_NOTIFICATION_RANKING_UPDATE = 4;
        public static final int MSG_ON_NOTIFICATION_REMOVED = 2;
        public static final int MSG_ON_STATUS_BAR_ICON_BEHAVIOR_CHANGED = 9;

        public MyHandler(Looper looper) {
            super(looper, null, false);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            if (!NotificationListenerService.this.isConnected) {
                return;
            }
            switch (msg.what) {
                case 1:
                    SomeArgs args = (SomeArgs) msg.obj;
                    StatusBarNotification sbn = (StatusBarNotification) args.arg1;
                    RankingMap rankingMap = (RankingMap) args.arg2;
                    args.recycle();
                    NotificationListenerService.this.onNotificationPosted(sbn, rankingMap);
                    return;
                case 2:
                    SomeArgs args2 = (SomeArgs) msg.obj;
                    StatusBarNotification sbn2 = (StatusBarNotification) args2.arg1;
                    RankingMap rankingMap2 = (RankingMap) args2.arg2;
                    int reason = ((Integer) args2.arg3).intValue();
                    NotificationStats stats = (NotificationStats) args2.arg4;
                    args2.recycle();
                    NotificationListenerService.this.onNotificationRemoved(sbn2, rankingMap2, stats, reason);
                    return;
                case 3:
                    NotificationListenerService.this.onListenerConnected();
                    return;
                case 4:
                    RankingMap rankingMap3 = (RankingMap) msg.obj;
                    NotificationListenerService.this.onNotificationRankingUpdate(rankingMap3);
                    return;
                case 5:
                    int hints = msg.arg1;
                    NotificationListenerService.this.onListenerHintsChanged(hints);
                    return;
                case 6:
                    int interruptionFilter = msg.arg1;
                    NotificationListenerService.this.onInterruptionFilterChanged(interruptionFilter);
                    return;
                case 7:
                    SomeArgs args3 = (SomeArgs) msg.obj;
                    String pkgName = (String) args3.arg1;
                    UserHandle user = (UserHandle) args3.arg2;
                    NotificationChannel channel = (NotificationChannel) args3.arg3;
                    int modificationType = ((Integer) args3.arg4).intValue();
                    NotificationListenerService.this.onNotificationChannelModified(pkgName, user, channel, modificationType);
                    return;
                case 8:
                    SomeArgs args4 = (SomeArgs) msg.obj;
                    String pkgName2 = (String) args4.arg1;
                    UserHandle user2 = (UserHandle) args4.arg2;
                    NotificationChannelGroup group = (NotificationChannelGroup) args4.arg3;
                    int modificationType2 = ((Integer) args4.arg4).intValue();
                    NotificationListenerService.this.onNotificationChannelGroupModified(pkgName2, user2, group, modificationType2);
                    return;
                case 9:
                    NotificationListenerService.this.onSilentStatusBarIconsVisibilityChanged(((Boolean) msg.obj).booleanValue());
                    return;
                default:
                    return;
            }
        }
    }
}
