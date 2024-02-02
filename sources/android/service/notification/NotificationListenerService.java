package android.service.notification;

import android.annotation.SystemApi;
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
import android.os.Bundle;
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
import android.util.ArraySet;
import android.util.Log;
import android.widget.RemoteViews;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.SomeArgs;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    private protected static final int TRIM_FULL = 0;
    @SystemApi
    private protected static final int TRIM_LIGHT = 1;
    protected int mCurrentUser;
    public protected Handler mHandler;
    public private INotificationManager mNoMan;
    @GuardedBy("mLock")
    private RankingMap mRankingMap;
    protected Context mSystemContext;
    public protected final String TAG = getClass().getSimpleName();
    private final Object mLock = new Object();
    public private NotificationListenerWrapper mWrapper = null;
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

    public void onNotificationChannelModified(String pkg, UserHandle user, NotificationChannel channel, int modificationType) {
    }

    public void onNotificationChannelGroupModified(String pkg, UserHandle user, NotificationChannelGroup group, int modificationType) {
    }

    public void onInterruptionFilterChanged(int interruptionFilter) {
    }

    public private final INotificationManager getNotificationInterface() {
        if (this.mNoMan == null) {
            this.mNoMan = INotificationManager.Stub.asInterface(ServiceManager.getService(Context.NOTIFICATION_SERVICE));
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
    private protected final void snoozeNotification(String key, String snoozeCriterionId) {
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
    private protected final void setOnNotificationPostedTrim(int trim) {
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
    private protected StatusBarNotification[] getActiveNotifications(int trim) {
        StatusBarNotification[] activeNotifications = getActiveNotifications(null, trim);
        return activeNotifications != null ? activeNotifications : new StatusBarNotification[0];
    }

    public StatusBarNotification[] getActiveNotifications(String[] keys) {
        StatusBarNotification[] activeNotifications = getActiveNotifications(keys, 0);
        return activeNotifications != null ? activeNotifications : new StatusBarNotification[0];
    }

    @SystemApi
    private protected StatusBarNotification[] getActiveNotifications(String[] keys, int trim) {
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

    private synchronized StatusBarNotification[] cleanUpNotificationList(ParceledListSlice<StatusBarNotification> parceledList) {
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

    public private boolean isBound() {
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
    private protected void registerAsSystemService(Context context, ComponentName componentName, int currentUser) throws RemoteException {
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
    private protected void unregisterAsSystemService() throws RemoteException {
        if (this.mWrapper != null) {
            INotificationManager noMan = getNotificationInterface();
            noMan.unregisterListener(this.mWrapper, this.mCurrentUser);
        }
    }

    public static void requestRebind(ComponentName componentName) {
        INotificationManager noMan = INotificationManager.Stub.asInterface(ServiceManager.getService(Context.NOTIFICATION_SERVICE));
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

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void createLegacyIconExtras(Notification n) {
        Drawable d;
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

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void maybePopulateRemoteViews(Notification notification) {
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
    public synchronized void maybePopulatePeople(Notification notification) {
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
        public synchronized void onNotificationPosted(IStatusBarNotificationHolder sbnHolder, NotificationRankingUpdate update) {
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
        public synchronized void onNotificationRemoved(IStatusBarNotificationHolder sbnHolder, NotificationRankingUpdate update, NotificationStats stats, int reason) {
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
        public synchronized void onListenerConnected(NotificationRankingUpdate update) {
            synchronized (NotificationListenerService.this.mLock) {
                NotificationListenerService.this.applyUpdateLocked(update);
            }
            NotificationListenerService.this.isConnected = true;
            NotificationListenerService.this.mHandler.obtainMessage(3).sendToTarget();
        }

        @Override // android.service.notification.INotificationListener
        public synchronized void onNotificationRankingUpdate(NotificationRankingUpdate update) throws RemoteException {
            synchronized (NotificationListenerService.this.mLock) {
                NotificationListenerService.this.applyUpdateLocked(update);
                NotificationListenerService.this.mHandler.obtainMessage(4, NotificationListenerService.this.mRankingMap).sendToTarget();
            }
        }

        @Override // android.service.notification.INotificationListener
        public synchronized void onListenerHintsChanged(int hints) throws RemoteException {
            NotificationListenerService.this.mHandler.obtainMessage(5, hints, 0).sendToTarget();
        }

        @Override // android.service.notification.INotificationListener
        public synchronized void onInterruptionFilterChanged(int interruptionFilter) throws RemoteException {
            NotificationListenerService.this.mHandler.obtainMessage(6, interruptionFilter, 0).sendToTarget();
        }

        public synchronized void onNotificationEnqueued(IStatusBarNotificationHolder notificationHolder) throws RemoteException {
        }

        public synchronized void onNotificationSnoozedUntilContext(IStatusBarNotificationHolder notificationHolder, String snoozeCriterionId) throws RemoteException {
        }

        @Override // android.service.notification.INotificationListener
        public synchronized void onNotificationChannelModification(String pkgName, UserHandle user, NotificationChannel channel, int modificationType) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = pkgName;
            args.arg2 = user;
            args.arg3 = channel;
            args.arg4 = Integer.valueOf(modificationType);
            NotificationListenerService.this.mHandler.obtainMessage(7, args).sendToTarget();
        }

        @Override // android.service.notification.INotificationListener
        public synchronized void onNotificationChannelGroupModification(String pkgName, UserHandle user, NotificationChannelGroup group, int modificationType) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = pkgName;
            args.arg2 = user;
            args.arg3 = group;
            args.arg4 = Integer.valueOf(modificationType);
            NotificationListenerService.this.mHandler.obtainMessage(8, args).sendToTarget();
        }
    }

    @GuardedBy("mLock")
    public final synchronized void applyUpdateLocked(NotificationRankingUpdate update) {
        this.mRankingMap = new RankingMap(update);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized Context getContext() {
        if (this.mSystemContext != null) {
            return this.mSystemContext;
        }
        return this;
    }

    /* loaded from: classes2.dex */
    public static class Ranking {
        public static final int USER_SENTIMENT_NEGATIVE = -1;
        public static final int USER_SENTIMENT_NEUTRAL = 0;
        public static final int USER_SENTIMENT_POSITIVE = 1;
        public static final int VISIBILITY_NO_OVERRIDE = -1000;
        private NotificationChannel mChannel;
        private boolean mHidden;
        private int mImportance;
        private CharSequence mImportanceExplanation;
        private boolean mIsAmbient;
        private String mKey;
        private boolean mMatchesInterruptionFilter;
        private String mOverrideGroupKey;
        private ArrayList<String> mOverridePeople;
        private boolean mShowBadge;
        private ArrayList<SnoozeCriterion> mSnoozeCriteria;
        private int mSuppressedVisualEffects;
        private int mVisibilityOverride;
        private int mRank = -1;
        private int mUserSentiment = 0;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes2.dex */
        public @interface UserSentiment {
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

        private protected int getVisibilityOverride() {
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
        private protected List<String> getAdditionalPeople() {
            return this.mOverridePeople;
        }

        @SystemApi
        private protected List<SnoozeCriterion> getSnoozeCriteria() {
            return this.mSnoozeCriteria;
        }

        public boolean canShowBadge() {
            return this.mShowBadge;
        }

        public boolean isSuspended() {
            return this.mHidden;
        }

        @VisibleForTesting
        public synchronized void populate(String key, int rank, boolean matchesInterruptionFilter, int visibilityOverride, int suppressedVisualEffects, int importance, CharSequence explanation, String overrideGroupKey, NotificationChannel channel, ArrayList<String> overridePeople, ArrayList<SnoozeCriterion> snoozeCriteria, boolean showBadge, int userSentiment, boolean hidden) {
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
        }

        public static synchronized String importanceToString(int importance) {
            if (importance == -1000) {
                return "UNSPECIFIED";
            }
            switch (importance) {
                case 0:
                    return "NONE";
                case 1:
                    return "MIN";
                case 2:
                    return "LOW";
                case 3:
                    return "DEFAULT";
                case 4:
                case 5:
                    return "HIGH";
                default:
                    return "UNKNOWN(" + String.valueOf(importance) + ")";
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class RankingMap implements Parcelable {
        public static final Parcelable.Creator<RankingMap> CREATOR = new Parcelable.Creator<RankingMap>() { // from class: android.service.notification.NotificationListenerService.RankingMap.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public RankingMap createFromParcel(Parcel source) {
                NotificationRankingUpdate rankingUpdate = (NotificationRankingUpdate) source.readParcelable(null);
                return new RankingMap(rankingUpdate);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public RankingMap[] newArray(int size) {
                return new RankingMap[size];
            }
        };
        private ArrayMap<String, NotificationChannel> mChannels;
        private ArrayMap<String, Boolean> mHidden;
        private ArrayMap<String, Integer> mImportance;
        private ArrayMap<String, String> mImportanceExplanation;
        private ArraySet<Object> mIntercepted;
        private ArrayMap<String, String> mOverrideGroupKeys;
        private ArrayMap<String, ArrayList<String>> mOverridePeople;
        private final NotificationRankingUpdate mRankingUpdate;
        private ArrayMap<String, Integer> mRanks;
        private ArrayMap<String, Boolean> mShowBadge;
        private ArrayMap<String, ArrayList<SnoozeCriterion>> mSnoozeCriteria;
        private ArrayMap<String, Integer> mSuppressedVisualEffects;
        private ArrayMap<String, Integer> mUserSentiment;
        private ArrayMap<String, Integer> mVisibilityOverrides;

        private synchronized RankingMap(NotificationRankingUpdate rankingUpdate) {
            this.mRankingUpdate = rankingUpdate;
        }

        public String[] getOrderedKeys() {
            return this.mRankingUpdate.getOrderedKeys();
        }

        public boolean getRanking(String key, Ranking outRanking) {
            int rank = getRank(key);
            outRanking.populate(key, rank, !isIntercepted(key), getVisibilityOverride(key), getSuppressedVisualEffects(key), getImportance(key), getImportanceExplanation(key), getOverrideGroupKey(key), getChannel(key), getOverridePeople(key), getSnoozeCriteria(key), getShowBadge(key), getUserSentiment(key), getHidden(key));
            return rank >= 0;
        }

        private synchronized int getRank(String key) {
            if (this.mRanks == null) {
                buildRanksLocked();
            }
            Integer rank = this.mRanks.get(key);
            if (rank != null) {
                return rank.intValue();
            }
            return -1;
        }

        private synchronized boolean isIntercepted(String key) {
            if (this.mIntercepted == null) {
                buildInterceptedSetLocked();
            }
            return this.mIntercepted.contains(key);
        }

        private synchronized int getVisibilityOverride(String key) {
            if (this.mVisibilityOverrides == null) {
                buildVisibilityOverridesLocked();
            }
            Integer override = this.mVisibilityOverrides.get(key);
            if (override == null) {
                return -1000;
            }
            return override.intValue();
        }

        private synchronized int getSuppressedVisualEffects(String key) {
            if (this.mSuppressedVisualEffects == null) {
                buildSuppressedVisualEffectsLocked();
            }
            Integer suppressed = this.mSuppressedVisualEffects.get(key);
            if (suppressed == null) {
                return 0;
            }
            return suppressed.intValue();
        }

        private synchronized int getImportance(String key) {
            if (this.mImportance == null) {
                buildImportanceLocked();
            }
            Integer importance = this.mImportance.get(key);
            if (importance == null) {
                return 3;
            }
            return importance.intValue();
        }

        private synchronized String getImportanceExplanation(String key) {
            if (this.mImportanceExplanation == null) {
                buildImportanceExplanationLocked();
            }
            return this.mImportanceExplanation.get(key);
        }

        private synchronized String getOverrideGroupKey(String key) {
            if (this.mOverrideGroupKeys == null) {
                buildOverrideGroupKeys();
            }
            return this.mOverrideGroupKeys.get(key);
        }

        private synchronized NotificationChannel getChannel(String key) {
            if (this.mChannels == null) {
                buildChannelsLocked();
            }
            return this.mChannels.get(key);
        }

        private synchronized ArrayList<String> getOverridePeople(String key) {
            if (this.mOverridePeople == null) {
                buildOverridePeopleLocked();
            }
            return this.mOverridePeople.get(key);
        }

        private synchronized ArrayList<SnoozeCriterion> getSnoozeCriteria(String key) {
            if (this.mSnoozeCriteria == null) {
                buildSnoozeCriteriaLocked();
            }
            return this.mSnoozeCriteria.get(key);
        }

        private synchronized boolean getShowBadge(String key) {
            if (this.mShowBadge == null) {
                buildShowBadgeLocked();
            }
            Boolean showBadge = this.mShowBadge.get(key);
            if (showBadge == null) {
                return false;
            }
            return showBadge.booleanValue();
        }

        private synchronized int getUserSentiment(String key) {
            if (this.mUserSentiment == null) {
                buildUserSentimentLocked();
            }
            Integer userSentiment = this.mUserSentiment.get(key);
            if (userSentiment == null) {
                return 0;
            }
            return userSentiment.intValue();
        }

        private synchronized boolean getHidden(String key) {
            if (this.mHidden == null) {
                buildHiddenLocked();
            }
            Boolean hidden = this.mHidden.get(key);
            if (hidden == null) {
                return false;
            }
            return hidden.booleanValue();
        }

        private synchronized void buildRanksLocked() {
            String[] orderedKeys = this.mRankingUpdate.getOrderedKeys();
            this.mRanks = new ArrayMap<>(orderedKeys.length);
            for (int i = 0; i < orderedKeys.length; i++) {
                String key = orderedKeys[i];
                this.mRanks.put(key, Integer.valueOf(i));
            }
        }

        private synchronized void buildInterceptedSetLocked() {
            String[] dndInterceptedKeys = this.mRankingUpdate.getInterceptedKeys();
            this.mIntercepted = new ArraySet<>(dndInterceptedKeys.length);
            Collections.addAll(this.mIntercepted, dndInterceptedKeys);
        }

        private synchronized void buildVisibilityOverridesLocked() {
            Bundle visibilityBundle = this.mRankingUpdate.getVisibilityOverrides();
            this.mVisibilityOverrides = new ArrayMap<>(visibilityBundle.size());
            for (String key : visibilityBundle.keySet()) {
                this.mVisibilityOverrides.put(key, Integer.valueOf(visibilityBundle.getInt(key)));
            }
        }

        private synchronized void buildSuppressedVisualEffectsLocked() {
            Bundle suppressedBundle = this.mRankingUpdate.getSuppressedVisualEffects();
            this.mSuppressedVisualEffects = new ArrayMap<>(suppressedBundle.size());
            for (String key : suppressedBundle.keySet()) {
                this.mSuppressedVisualEffects.put(key, Integer.valueOf(suppressedBundle.getInt(key)));
            }
        }

        private synchronized void buildImportanceLocked() {
            String[] orderedKeys = this.mRankingUpdate.getOrderedKeys();
            int[] importance = this.mRankingUpdate.getImportance();
            this.mImportance = new ArrayMap<>(orderedKeys.length);
            for (int i = 0; i < orderedKeys.length; i++) {
                String key = orderedKeys[i];
                this.mImportance.put(key, Integer.valueOf(importance[i]));
            }
        }

        private synchronized void buildImportanceExplanationLocked() {
            Bundle explanationBundle = this.mRankingUpdate.getImportanceExplanation();
            this.mImportanceExplanation = new ArrayMap<>(explanationBundle.size());
            for (String key : explanationBundle.keySet()) {
                this.mImportanceExplanation.put(key, explanationBundle.getString(key));
            }
        }

        private synchronized void buildOverrideGroupKeys() {
            Bundle overrideGroupKeys = this.mRankingUpdate.getOverrideGroupKeys();
            this.mOverrideGroupKeys = new ArrayMap<>(overrideGroupKeys.size());
            for (String key : overrideGroupKeys.keySet()) {
                this.mOverrideGroupKeys.put(key, overrideGroupKeys.getString(key));
            }
        }

        private synchronized void buildChannelsLocked() {
            Bundle channels = this.mRankingUpdate.getChannels();
            this.mChannels = new ArrayMap<>(channels.size());
            for (String key : channels.keySet()) {
                this.mChannels.put(key, (NotificationChannel) channels.getParcelable(key));
            }
        }

        private synchronized void buildOverridePeopleLocked() {
            Bundle overridePeople = this.mRankingUpdate.getOverridePeople();
            this.mOverridePeople = new ArrayMap<>(overridePeople.size());
            for (String key : overridePeople.keySet()) {
                this.mOverridePeople.put(key, overridePeople.getStringArrayList(key));
            }
        }

        private synchronized void buildSnoozeCriteriaLocked() {
            Bundle snoozeCriteria = this.mRankingUpdate.getSnoozeCriteria();
            this.mSnoozeCriteria = new ArrayMap<>(snoozeCriteria.size());
            for (String key : snoozeCriteria.keySet()) {
                this.mSnoozeCriteria.put(key, snoozeCriteria.getParcelableArrayList(key));
            }
        }

        private synchronized void buildShowBadgeLocked() {
            Bundle showBadge = this.mRankingUpdate.getShowBadge();
            this.mShowBadge = new ArrayMap<>(showBadge.size());
            for (String key : showBadge.keySet()) {
                this.mShowBadge.put(key, Boolean.valueOf(showBadge.getBoolean(key)));
            }
        }

        private synchronized void buildUserSentimentLocked() {
            Bundle userSentiment = this.mRankingUpdate.getUserSentiment();
            this.mUserSentiment = new ArrayMap<>(userSentiment.size());
            for (String key : userSentiment.keySet()) {
                this.mUserSentiment.put(key, Integer.valueOf(userSentiment.getInt(key)));
            }
        }

        private synchronized void buildHiddenLocked() {
            Bundle hidden = this.mRankingUpdate.getHidden();
            this.mHidden = new ArrayMap<>(hidden.size());
            for (String key : hidden.keySet()) {
                this.mHidden.put(key, Boolean.valueOf(hidden.getBoolean(key)));
            }
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.mRankingUpdate, flags);
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
                default:
                    return;
            }
        }
    }
}
