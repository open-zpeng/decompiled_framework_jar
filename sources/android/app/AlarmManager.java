package android.app;

import android.annotation.SystemApi;
import android.app.IAlarmListener;
import android.content.Context;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.WorkSource;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.proto.ProtoOutputStream;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import libcore.util.ZoneInfoDB;
/* loaded from: classes.dex */
public class AlarmManager {
    public static final String ACTION_NEXT_ALARM_CLOCK_CHANGED = "android.app.action.NEXT_ALARM_CLOCK_CHANGED";
    public static final int ELAPSED_REALTIME = 3;
    public static final int ELAPSED_REALTIME_WAKEUP = 2;
    public static final int FLAG_ALLOW_WHILE_IDLE = 4;
    private protected static final int FLAG_ALLOW_WHILE_IDLE_UNRESTRICTED = 8;
    private protected static final int FLAG_IDLE_UNTIL = 16;
    private protected static final int FLAG_STANDALONE = 1;
    private protected static final int FLAG_WAKE_FROM_IDLE = 2;
    public static final long INTERVAL_DAY = 86400000;
    public static final long INTERVAL_FIFTEEN_MINUTES = 900000;
    public static final long INTERVAL_HALF_DAY = 43200000;
    public static final long INTERVAL_HALF_HOUR = 1800000;
    public static final long INTERVAL_HOUR = 3600000;
    public static final int RTC = 1;
    public static final int RTC_WAKEUP = 0;
    private static final String TAG = "AlarmManager";
    private protected static final long WINDOW_EXACT = 0;
    private protected static final long WINDOW_HEURISTIC = -1;
    private static ArrayMap<OnAlarmListener, ListenerWrapper> sWrappers;
    private final boolean mAlwaysExact;
    private final Context mContext;
    private final Handler mMainThreadHandler;
    private final String mPackageName;
    public protected final IAlarmManager mService;
    private final int mTargetSdkVersion;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface AlarmType {
    }

    /* loaded from: classes.dex */
    public interface OnAlarmListener {
        void onAlarm();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class ListenerWrapper extends IAlarmListener.Stub implements Runnable {
        IAlarmCompleteListener mCompletion;
        Handler mHandler;
        final OnAlarmListener mListener;

        public ListenerWrapper(OnAlarmListener listener) {
            this.mListener = listener;
        }

        public synchronized void setHandler(Handler h) {
            this.mHandler = h;
        }

        public synchronized void cancel() {
            try {
                AlarmManager.this.mService.remove(null, this);
                synchronized (AlarmManager.class) {
                    if (AlarmManager.sWrappers != null) {
                        AlarmManager.sWrappers.remove(this.mListener);
                    }
                }
            } catch (RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        }

        @Override // android.app.IAlarmListener
        public synchronized void doAlarm(IAlarmCompleteListener alarmManager) {
            this.mCompletion = alarmManager;
            synchronized (AlarmManager.class) {
                if (AlarmManager.sWrappers != null) {
                    AlarmManager.sWrappers.remove(this.mListener);
                }
            }
            this.mHandler.post(this);
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                this.mListener.onAlarm();
                try {
                    this.mCompletion.alarmComplete(this);
                } catch (Exception e) {
                    Log.e(AlarmManager.TAG, "Unable to report completion to Alarm Manager!", e);
                }
            } catch (Throwable th) {
                try {
                    this.mCompletion.alarmComplete(this);
                } catch (Exception e2) {
                    Log.e(AlarmManager.TAG, "Unable to report completion to Alarm Manager!", e2);
                }
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized AlarmManager(IAlarmManager service, Context ctx) {
        this.mService = service;
        this.mContext = ctx;
        this.mPackageName = ctx.getPackageName();
        this.mTargetSdkVersion = ctx.getApplicationInfo().targetSdkVersion;
        this.mAlwaysExact = this.mTargetSdkVersion < 19;
        this.mMainThreadHandler = new Handler(ctx.getMainLooper());
    }

    private synchronized long legacyExactLength() {
        return this.mAlwaysExact ? 0L : -1L;
    }

    public void set(int type, long triggerAtMillis, PendingIntent operation) {
        setImpl(type, triggerAtMillis, legacyExactLength(), 0L, 0, operation, null, null, null, null, null);
    }

    public void set(int type, long triggerAtMillis, String tag, OnAlarmListener listener, Handler targetHandler) {
        setImpl(type, triggerAtMillis, legacyExactLength(), 0L, 0, null, listener, tag, targetHandler, null, null);
    }

    public void setRepeating(int type, long triggerAtMillis, long intervalMillis, PendingIntent operation) {
        setImpl(type, triggerAtMillis, legacyExactLength(), intervalMillis, 0, operation, null, null, null, null, null);
    }

    public void setWindow(int type, long windowStartMillis, long windowLengthMillis, PendingIntent operation) {
        setImpl(type, windowStartMillis, windowLengthMillis, 0L, 0, operation, null, null, null, null, null);
    }

    public void setWindow(int type, long windowStartMillis, long windowLengthMillis, String tag, OnAlarmListener listener, Handler targetHandler) {
        setImpl(type, windowStartMillis, windowLengthMillis, 0L, 0, null, listener, tag, targetHandler, null, null);
    }

    public void setExact(int type, long triggerAtMillis, PendingIntent operation) {
        setImpl(type, triggerAtMillis, 0L, 0L, 0, operation, null, null, null, null, null);
    }

    public void setExact(int type, long triggerAtMillis, String tag, OnAlarmListener listener, Handler targetHandler) {
        setImpl(type, triggerAtMillis, 0L, 0L, 0, null, listener, tag, targetHandler, null, null);
    }

    public synchronized void setIdleUntil(int type, long triggerAtMillis, String tag, OnAlarmListener listener, Handler targetHandler) {
        setImpl(type, triggerAtMillis, 0L, 0L, 16, null, listener, tag, targetHandler, null, null);
    }

    public void setAlarmClock(AlarmClockInfo info, PendingIntent operation) {
        setImpl(0, info.getTriggerTime(), 0L, 0L, 0, operation, null, null, null, null, info);
    }

    @SystemApi
    public void set(int type, long triggerAtMillis, long windowMillis, long intervalMillis, PendingIntent operation, WorkSource workSource) {
        setImpl(type, triggerAtMillis, windowMillis, intervalMillis, 0, operation, null, null, null, workSource, null);
    }

    private protected void set(int type, long triggerAtMillis, long windowMillis, long intervalMillis, String tag, OnAlarmListener listener, Handler targetHandler, WorkSource workSource) {
        setImpl(type, triggerAtMillis, windowMillis, intervalMillis, 0, null, listener, tag, targetHandler, workSource, null);
    }

    @SystemApi
    public void set(int type, long triggerAtMillis, long windowMillis, long intervalMillis, OnAlarmListener listener, Handler targetHandler, WorkSource workSource) {
        setImpl(type, triggerAtMillis, windowMillis, intervalMillis, 0, null, listener, null, targetHandler, workSource, null);
    }

    private synchronized void setImpl(int type, long triggerAtMillis, long windowMillis, long intervalMillis, int flags, PendingIntent operation, OnAlarmListener listener, String listenerTag, Handler targetHandler, WorkSource workSource, AlarmClockInfo alarmClock) {
        long triggerAtMillis2;
        if (triggerAtMillis < 0) {
            triggerAtMillis2 = 0;
        } else {
            triggerAtMillis2 = triggerAtMillis;
        }
        ListenerWrapper recipientWrapper = null;
        if (listener != null) {
            synchronized (AlarmManager.class) {
                if (sWrappers == null) {
                    sWrappers = new ArrayMap<>();
                }
                recipientWrapper = sWrappers.get(listener);
                if (recipientWrapper == null) {
                    recipientWrapper = new ListenerWrapper(listener);
                    sWrappers.put(listener, recipientWrapper);
                }
            }
            Handler handler = targetHandler != null ? targetHandler : this.mMainThreadHandler;
            recipientWrapper.setHandler(handler);
        }
        try {
            this.mService.set(this.mPackageName, type, triggerAtMillis2, windowMillis, intervalMillis, flags, operation, recipientWrapper, listenerTag, workSource, alarmClock);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public void setInexactRepeating(int type, long triggerAtMillis, long intervalMillis, PendingIntent operation) {
        setImpl(type, triggerAtMillis, -1L, intervalMillis, 0, operation, null, null, null, null, null);
    }

    public void setAndAllowWhileIdle(int type, long triggerAtMillis, PendingIntent operation) {
        setImpl(type, triggerAtMillis, -1L, 0L, 4, operation, null, null, null, null, null);
    }

    public void setExactAndAllowWhileIdle(int type, long triggerAtMillis, PendingIntent operation) {
        setImpl(type, triggerAtMillis, 0L, 0L, 4, operation, null, null, null, null, null);
    }

    public void cancel(PendingIntent operation) {
        if (operation == null) {
            if (this.mTargetSdkVersion >= 24) {
                throw new NullPointerException("cancel() called with a null PendingIntent");
            }
            Log.e(TAG, "cancel() called with a null PendingIntent");
            return;
        }
        try {
            this.mService.remove(operation, null);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public void cancel(OnAlarmListener listener) {
        if (listener == null) {
            throw new NullPointerException("cancel() called with a null OnAlarmListener");
        }
        ListenerWrapper wrapper = null;
        synchronized (AlarmManager.class) {
            if (sWrappers != null) {
                wrapper = sWrappers.get(listener);
            }
        }
        if (wrapper == null) {
            Log.w(TAG, "Unrecognized alarm listener " + listener);
            return;
        }
        wrapper.cancel();
    }

    public void setTime(long millis) {
        try {
            this.mService.setTime(millis);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public void setTimeZone(String timeZone) {
        if (TextUtils.isEmpty(timeZone)) {
            return;
        }
        if (this.mTargetSdkVersion >= 23) {
            boolean hasTimeZone = false;
            try {
                hasTimeZone = ZoneInfoDB.getInstance().hasTimeZone(timeZone);
            } catch (IOException e) {
            }
            if (!hasTimeZone) {
                throw new IllegalArgumentException("Timezone: " + timeZone + " is not an Olson ID");
            }
        }
        try {
            this.mService.setTimeZone(timeZone);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public synchronized long getNextWakeFromIdleTime() {
        try {
            return this.mService.getNextWakeFromIdleTime();
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public AlarmClockInfo getNextAlarmClock() {
        return getNextAlarmClock(this.mContext.getUserId());
    }

    public synchronized AlarmClockInfo getNextAlarmClock(int userId) {
        try {
            return this.mService.getNextAlarmClock(userId);
        } catch (RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    /* loaded from: classes.dex */
    public static final class AlarmClockInfo implements Parcelable {
        public static final Parcelable.Creator<AlarmClockInfo> CREATOR = new Parcelable.Creator<AlarmClockInfo>() { // from class: android.app.AlarmManager.AlarmClockInfo.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public AlarmClockInfo createFromParcel(Parcel in) {
                return new AlarmClockInfo(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public AlarmClockInfo[] newArray(int size) {
                return new AlarmClockInfo[size];
            }
        };
        private final PendingIntent mShowIntent;
        private final long mTriggerTime;

        public AlarmClockInfo(long triggerTime, PendingIntent showIntent) {
            this.mTriggerTime = triggerTime;
            this.mShowIntent = showIntent;
        }

        synchronized AlarmClockInfo(Parcel in) {
            this.mTriggerTime = in.readLong();
            this.mShowIntent = (PendingIntent) in.readParcelable(PendingIntent.class.getClassLoader());
        }

        public long getTriggerTime() {
            return this.mTriggerTime;
        }

        public PendingIntent getShowIntent() {
            return this.mShowIntent;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.mTriggerTime);
            dest.writeParcelable(this.mShowIntent, flags);
        }

        public synchronized void writeToProto(ProtoOutputStream proto, long fieldId) {
            long token = proto.start(fieldId);
            proto.write(1112396529665L, this.mTriggerTime);
            this.mShowIntent.writeToProto(proto, 1146756268034L);
            proto.end(token);
        }
    }
}
