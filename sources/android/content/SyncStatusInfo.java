package android.content;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
/* loaded from: classes.dex */
public class SyncStatusInfo implements Parcelable {
    private protected static final Parcelable.Creator<SyncStatusInfo> CREATOR = new Parcelable.Creator<SyncStatusInfo>() { // from class: android.content.SyncStatusInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SyncStatusInfo createFromParcel(Parcel in) {
            return new SyncStatusInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SyncStatusInfo[] newArray(int size) {
            return new SyncStatusInfo[size];
        }
    };
    private static final int MAX_EVENT_COUNT = 10;
    private static final int SOURCE_COUNT = 6;
    private static final String TAG = "Sync";
    static final int VERSION = 6;
    private protected final int authorityId;
    private protected long initialFailureTime;
    private protected boolean initialize;
    private protected String lastFailureMesg;
    private protected int lastFailureSource;
    private protected long lastFailureTime;
    private protected int lastSuccessSource;
    private protected long lastSuccessTime;
    public long lastTodayResetTime;
    private final ArrayList<Long> mLastEventTimes;
    private final ArrayList<String> mLastEvents;
    private protected boolean pending;
    public final long[] perSourceLastFailureTimes;
    public final long[] perSourceLastSuccessTimes;
    public protected ArrayList<Long> periodicSyncTimes;
    public final Stats todayStats;
    public final Stats totalStats;
    public final Stats yesterdayStats;

    /* loaded from: classes.dex */
    public static class Stats {
        public int numCancels;
        public int numFailures;
        public int numSourceFeed;
        public int numSourceLocal;
        public int numSourceOther;
        public int numSourcePeriodic;
        public int numSourcePoll;
        public int numSourceUser;
        public int numSyncs;
        public long totalElapsedTime;

        public synchronized void copyTo(Stats to) {
            to.totalElapsedTime = this.totalElapsedTime;
            to.numSyncs = this.numSyncs;
            to.numSourcePoll = this.numSourcePoll;
            to.numSourceOther = this.numSourceOther;
            to.numSourceLocal = this.numSourceLocal;
            to.numSourceUser = this.numSourceUser;
            to.numSourcePeriodic = this.numSourcePeriodic;
            to.numSourceFeed = this.numSourceFeed;
            to.numFailures = this.numFailures;
            to.numCancels = this.numCancels;
        }

        public synchronized void clear() {
            this.totalElapsedTime = 0L;
            this.numSyncs = 0;
            this.numSourcePoll = 0;
            this.numSourceOther = 0;
            this.numSourceLocal = 0;
            this.numSourceUser = 0;
            this.numSourcePeriodic = 0;
            this.numSourceFeed = 0;
            this.numFailures = 0;
            this.numCancels = 0;
        }

        public synchronized void writeToParcel(Parcel parcel) {
            parcel.writeLong(this.totalElapsedTime);
            parcel.writeInt(this.numSyncs);
            parcel.writeInt(this.numSourcePoll);
            parcel.writeInt(this.numSourceOther);
            parcel.writeInt(this.numSourceLocal);
            parcel.writeInt(this.numSourceUser);
            parcel.writeInt(this.numSourcePeriodic);
            parcel.writeInt(this.numSourceFeed);
            parcel.writeInt(this.numFailures);
            parcel.writeInt(this.numCancels);
        }

        public synchronized void readFromParcel(Parcel parcel) {
            this.totalElapsedTime = parcel.readLong();
            this.numSyncs = parcel.readInt();
            this.numSourcePoll = parcel.readInt();
            this.numSourceOther = parcel.readInt();
            this.numSourceLocal = parcel.readInt();
            this.numSourceUser = parcel.readInt();
            this.numSourcePeriodic = parcel.readInt();
            this.numSourceFeed = parcel.readInt();
            this.numFailures = parcel.readInt();
            this.numCancels = parcel.readInt();
        }
    }

    private protected SyncStatusInfo(int authorityId) {
        this.totalStats = new Stats();
        this.todayStats = new Stats();
        this.yesterdayStats = new Stats();
        this.perSourceLastSuccessTimes = new long[6];
        this.perSourceLastFailureTimes = new long[6];
        this.mLastEventTimes = new ArrayList<>();
        this.mLastEvents = new ArrayList<>();
        this.authorityId = authorityId;
    }

    private protected int getLastFailureMesgAsInt(int def) {
        int i = ContentResolver.syncErrorStringToInt(this.lastFailureMesg);
        if (i > 0) {
            return i;
        }
        Log.d(TAG, "Unknown lastFailureMesg:" + this.lastFailureMesg);
        return def;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(6);
        parcel.writeInt(this.authorityId);
        parcel.writeLong(this.totalStats.totalElapsedTime);
        parcel.writeInt(this.totalStats.numSyncs);
        parcel.writeInt(this.totalStats.numSourcePoll);
        parcel.writeInt(this.totalStats.numSourceOther);
        parcel.writeInt(this.totalStats.numSourceLocal);
        parcel.writeInt(this.totalStats.numSourceUser);
        parcel.writeLong(this.lastSuccessTime);
        parcel.writeInt(this.lastSuccessSource);
        parcel.writeLong(this.lastFailureTime);
        parcel.writeInt(this.lastFailureSource);
        parcel.writeString(this.lastFailureMesg);
        parcel.writeLong(this.initialFailureTime);
        parcel.writeInt(this.pending ? 1 : 0);
        parcel.writeInt(this.initialize ? 1 : 0);
        if (this.periodicSyncTimes != null) {
            parcel.writeInt(this.periodicSyncTimes.size());
            Iterator<Long> it = this.periodicSyncTimes.iterator();
            while (it.hasNext()) {
                long periodicSyncTime = it.next().longValue();
                parcel.writeLong(periodicSyncTime);
            }
        } else {
            parcel.writeInt(-1);
        }
        parcel.writeInt(this.mLastEventTimes.size());
        for (int i = 0; i < this.mLastEventTimes.size(); i++) {
            parcel.writeLong(this.mLastEventTimes.get(i).longValue());
            parcel.writeString(this.mLastEvents.get(i));
        }
        parcel.writeInt(this.totalStats.numSourcePeriodic);
        parcel.writeInt(this.totalStats.numSourceFeed);
        parcel.writeInt(this.totalStats.numFailures);
        parcel.writeInt(this.totalStats.numCancels);
        parcel.writeLong(this.lastTodayResetTime);
        this.todayStats.writeToParcel(parcel);
        this.yesterdayStats.writeToParcel(parcel);
        parcel.writeLongArray(this.perSourceLastSuccessTimes);
        parcel.writeLongArray(this.perSourceLastFailureTimes);
    }

    private protected SyncStatusInfo(Parcel parcel) {
        this.totalStats = new Stats();
        this.todayStats = new Stats();
        this.yesterdayStats = new Stats();
        this.perSourceLastSuccessTimes = new long[6];
        this.perSourceLastFailureTimes = new long[6];
        this.mLastEventTimes = new ArrayList<>();
        this.mLastEvents = new ArrayList<>();
        int version = parcel.readInt();
        if (version != 6 && version != 1) {
            Log.w("SyncStatusInfo", "Unknown version: " + version);
        }
        this.authorityId = parcel.readInt();
        this.totalStats.totalElapsedTime = parcel.readLong();
        this.totalStats.numSyncs = parcel.readInt();
        this.totalStats.numSourcePoll = parcel.readInt();
        this.totalStats.numSourceOther = parcel.readInt();
        this.totalStats.numSourceLocal = parcel.readInt();
        this.totalStats.numSourceUser = parcel.readInt();
        this.lastSuccessTime = parcel.readLong();
        this.lastSuccessSource = parcel.readInt();
        this.lastFailureTime = parcel.readLong();
        this.lastFailureSource = parcel.readInt();
        this.lastFailureMesg = parcel.readString();
        this.initialFailureTime = parcel.readLong();
        this.pending = parcel.readInt() != 0;
        this.initialize = parcel.readInt() != 0;
        if (version == 1) {
            this.periodicSyncTimes = null;
        } else {
            int count = parcel.readInt();
            if (count < 0) {
                this.periodicSyncTimes = null;
            } else {
                this.periodicSyncTimes = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    this.periodicSyncTimes.add(Long.valueOf(parcel.readLong()));
                }
            }
            if (version >= 3) {
                this.mLastEventTimes.clear();
                this.mLastEvents.clear();
                int nEvents = parcel.readInt();
                for (int i2 = 0; i2 < nEvents; i2++) {
                    this.mLastEventTimes.add(Long.valueOf(parcel.readLong()));
                    this.mLastEvents.add(parcel.readString());
                }
            }
        }
        if (version < 4) {
            this.totalStats.numSourcePeriodic = (((this.totalStats.numSyncs - this.totalStats.numSourceLocal) - this.totalStats.numSourcePoll) - this.totalStats.numSourceOther) - this.totalStats.numSourceUser;
            if (this.totalStats.numSourcePeriodic < 0) {
                this.totalStats.numSourcePeriodic = 0;
            }
        } else {
            this.totalStats.numSourcePeriodic = parcel.readInt();
        }
        if (version >= 5) {
            this.totalStats.numSourceFeed = parcel.readInt();
            this.totalStats.numFailures = parcel.readInt();
            this.totalStats.numCancels = parcel.readInt();
            this.lastTodayResetTime = parcel.readLong();
            this.todayStats.readFromParcel(parcel);
            this.yesterdayStats.readFromParcel(parcel);
        }
        if (version >= 6) {
            parcel.readLongArray(this.perSourceLastSuccessTimes);
            parcel.readLongArray(this.perSourceLastFailureTimes);
        }
    }

    public synchronized SyncStatusInfo(SyncStatusInfo other) {
        this.totalStats = new Stats();
        this.todayStats = new Stats();
        this.yesterdayStats = new Stats();
        this.perSourceLastSuccessTimes = new long[6];
        this.perSourceLastFailureTimes = new long[6];
        this.mLastEventTimes = new ArrayList<>();
        this.mLastEvents = new ArrayList<>();
        this.authorityId = other.authorityId;
        other.totalStats.copyTo(this.totalStats);
        other.todayStats.copyTo(this.todayStats);
        other.yesterdayStats.copyTo(this.yesterdayStats);
        this.lastTodayResetTime = other.lastTodayResetTime;
        this.lastSuccessTime = other.lastSuccessTime;
        this.lastSuccessSource = other.lastSuccessSource;
        this.lastFailureTime = other.lastFailureTime;
        this.lastFailureSource = other.lastFailureSource;
        this.lastFailureMesg = other.lastFailureMesg;
        this.initialFailureTime = other.initialFailureTime;
        this.pending = other.pending;
        this.initialize = other.initialize;
        if (other.periodicSyncTimes != null) {
            this.periodicSyncTimes = new ArrayList<>(other.periodicSyncTimes);
        }
        this.mLastEventTimes.addAll(other.mLastEventTimes);
        this.mLastEvents.addAll(other.mLastEvents);
        copy(this.perSourceLastSuccessTimes, other.perSourceLastSuccessTimes);
        copy(this.perSourceLastFailureTimes, other.perSourceLastFailureTimes);
    }

    private static synchronized void copy(long[] to, long[] from) {
        System.arraycopy(from, 0, to, 0, to.length);
    }

    private protected void setPeriodicSyncTime(int index, long when) {
        ensurePeriodicSyncTimeSize(index);
        this.periodicSyncTimes.set(index, Long.valueOf(when));
    }

    private protected long getPeriodicSyncTime(int index) {
        if (this.periodicSyncTimes != null && index < this.periodicSyncTimes.size()) {
            return this.periodicSyncTimes.get(index).longValue();
        }
        return 0L;
    }

    private protected void removePeriodicSyncTime(int index) {
        if (this.periodicSyncTimes != null && index < this.periodicSyncTimes.size()) {
            this.periodicSyncTimes.remove(index);
        }
    }

    public synchronized void addEvent(String message) {
        if (this.mLastEventTimes.size() >= 10) {
            this.mLastEventTimes.remove(9);
            this.mLastEvents.remove(9);
        }
        this.mLastEventTimes.add(0, Long.valueOf(System.currentTimeMillis()));
        this.mLastEvents.add(0, message);
    }

    public synchronized int getEventCount() {
        return this.mLastEventTimes.size();
    }

    public synchronized long getEventTime(int i) {
        return this.mLastEventTimes.get(i).longValue();
    }

    public synchronized String getEvent(int i) {
        return this.mLastEvents.get(i);
    }

    public synchronized void setLastSuccess(int source, long lastSyncTime) {
        this.lastSuccessTime = lastSyncTime;
        this.lastSuccessSource = source;
        this.lastFailureTime = 0L;
        this.lastFailureSource = -1;
        this.lastFailureMesg = null;
        this.initialFailureTime = 0L;
        if (source >= 0 && source < this.perSourceLastSuccessTimes.length) {
            this.perSourceLastSuccessTimes[source] = lastSyncTime;
        }
    }

    public synchronized void setLastFailure(int source, long lastSyncTime, String failureMessage) {
        this.lastFailureTime = lastSyncTime;
        this.lastFailureSource = source;
        this.lastFailureMesg = failureMessage;
        if (this.initialFailureTime == 0) {
            this.initialFailureTime = lastSyncTime;
        }
        if (source >= 0 && source < this.perSourceLastFailureTimes.length) {
            this.perSourceLastFailureTimes[source] = lastSyncTime;
        }
    }

    public protected void ensurePeriodicSyncTimeSize(int index) {
        if (this.periodicSyncTimes == null) {
            this.periodicSyncTimes = new ArrayList<>(0);
        }
        int requiredSize = index + 1;
        if (this.periodicSyncTimes.size() < requiredSize) {
            for (int i = this.periodicSyncTimes.size(); i < requiredSize; i++) {
                this.periodicSyncTimes.add(0L);
            }
        }
    }

    public synchronized void maybeResetTodayStats(boolean clockValid, boolean force) {
        long now = System.currentTimeMillis();
        if (!force) {
            if (areSameDates(now, this.lastTodayResetTime)) {
                return;
            }
            if (now < this.lastTodayResetTime && !clockValid) {
                return;
            }
        }
        this.lastTodayResetTime = now;
        this.todayStats.copyTo(this.yesterdayStats);
        this.todayStats.clear();
    }

    private static synchronized boolean areSameDates(long time1, long time2) {
        Calendar c1 = new GregorianCalendar();
        Calendar c2 = new GregorianCalendar();
        c1.setTimeInMillis(time1);
        c2.setTimeInMillis(time2);
        return c1.get(1) == c2.get(1) && c1.get(6) == c2.get(6);
    }
}
