package android.app.job;

import android.content.ClipData;
import android.content.ComponentName;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.util.Log;
import android.util.TimeUtils;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public class JobInfo implements Parcelable {
    public static final int BACKOFF_POLICY_EXPONENTIAL = 1;
    public static final int BACKOFF_POLICY_LINEAR = 0;
    public static final int CONSTRAINT_FLAG_BATTERY_NOT_LOW = 2;
    public static final int CONSTRAINT_FLAG_CHARGING = 1;
    public static final int CONSTRAINT_FLAG_DEVICE_IDLE = 4;
    public static final int CONSTRAINT_FLAG_STORAGE_NOT_LOW = 8;
    public static final int DEFAULT_BACKOFF_POLICY = 1;
    public static final long DEFAULT_INITIAL_BACKOFF_MILLIS = 30000;
    public static final int FLAG_EXEMPT_FROM_APP_STANDBY = 8;
    public static final int FLAG_IMPORTANT_WHILE_FOREGROUND = 2;
    public static final int FLAG_PREFETCH = 4;
    private protected static final int FLAG_WILL_BE_FOREGROUND = 1;
    public static final long MAX_BACKOFF_DELAY_MILLIS = 18000000;
    public static final long MIN_BACKOFF_MILLIS = 10000;
    private static final long MIN_FLEX_MILLIS = 300000;
    private static final long MIN_PERIOD_MILLIS = 900000;
    public static final int NETWORK_BYTES_UNKNOWN = -1;
    public static final int NETWORK_TYPE_ANY = 1;
    public static final int NETWORK_TYPE_CELLULAR = 4;
    @Deprecated
    public static final int NETWORK_TYPE_METERED = 4;
    public static final int NETWORK_TYPE_NONE = 0;
    public static final int NETWORK_TYPE_NOT_ROAMING = 3;
    public static final int NETWORK_TYPE_UNMETERED = 2;
    public static final int PRIORITY_ADJ_ALWAYS_RUNNING = -80;
    public static final int PRIORITY_ADJ_OFTEN_RUNNING = -40;
    public static final int PRIORITY_DEFAULT = 0;
    private protected static final int PRIORITY_FOREGROUND_APP = 30;
    public static final int PRIORITY_SYNC_EXPEDITED = 10;
    public static final int PRIORITY_SYNC_INITIALIZATION = 20;
    public static final int PRIORITY_TOP_APP = 40;
    private final int backoffPolicy;
    private final ClipData clipData;
    private final int clipGrantFlags;
    private final int constraintFlags;
    private final PersistableBundle extras;
    public protected final int flags;
    private final long flexMillis;
    private final boolean hasEarlyConstraint;
    private final boolean hasLateConstraint;
    private final long initialBackoffMillis;
    private final long intervalMillis;
    private final boolean isPeriodic;
    private final boolean isPersisted;
    public protected final int jobId;
    private final long maxExecutionDelayMillis;
    private final long minLatencyMillis;
    private final long networkDownloadBytes;
    private final NetworkRequest networkRequest;
    private final long networkUploadBytes;
    private final int priority;
    public protected final ComponentName service;
    private final Bundle transientExtras;
    private final long triggerContentMaxDelay;
    private final long triggerContentUpdateDelay;
    private final TriggerContentUri[] triggerContentUris;
    private static String TAG = "JobInfo";
    public static final Parcelable.Creator<JobInfo> CREATOR = new Parcelable.Creator<JobInfo>() { // from class: android.app.job.JobInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public JobInfo createFromParcel(Parcel in) {
            return new JobInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public JobInfo[] newArray(int size) {
            return new JobInfo[size];
        }
    };

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface BackoffPolicy {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface NetworkType {
    }

    public static final long getMinPeriodMillis() {
        return 900000L;
    }

    public static final long getMinFlexMillis() {
        return 300000L;
    }

    public static final synchronized long getMinBackoffMillis() {
        return MIN_BACKOFF_MILLIS;
    }

    public int getId() {
        return this.jobId;
    }

    public PersistableBundle getExtras() {
        return this.extras;
    }

    public Bundle getTransientExtras() {
        return this.transientExtras;
    }

    public ClipData getClipData() {
        return this.clipData;
    }

    public int getClipGrantFlags() {
        return this.clipGrantFlags;
    }

    public ComponentName getService() {
        return this.service;
    }

    public synchronized int getPriority() {
        return this.priority;
    }

    public synchronized int getFlags() {
        return this.flags;
    }

    public synchronized boolean isExemptedFromAppStandby() {
        return ((this.flags & 8) == 0 || isPeriodic()) ? false : true;
    }

    public boolean isRequireCharging() {
        return (this.constraintFlags & 1) != 0;
    }

    public boolean isRequireBatteryNotLow() {
        return (this.constraintFlags & 2) != 0;
    }

    public boolean isRequireDeviceIdle() {
        return (this.constraintFlags & 4) != 0;
    }

    public boolean isRequireStorageNotLow() {
        return (this.constraintFlags & 8) != 0;
    }

    public synchronized int getConstraintFlags() {
        return this.constraintFlags;
    }

    public TriggerContentUri[] getTriggerContentUris() {
        return this.triggerContentUris;
    }

    public long getTriggerContentUpdateDelay() {
        return this.triggerContentUpdateDelay;
    }

    public long getTriggerContentMaxDelay() {
        return this.triggerContentMaxDelay;
    }

    @Deprecated
    public int getNetworkType() {
        if (this.networkRequest == null) {
            return 0;
        }
        if (this.networkRequest.networkCapabilities.hasCapability(11)) {
            return 2;
        }
        if (this.networkRequest.networkCapabilities.hasCapability(18)) {
            return 3;
        }
        if (this.networkRequest.networkCapabilities.hasTransport(0)) {
            return 4;
        }
        return 1;
    }

    public NetworkRequest getRequiredNetwork() {
        return this.networkRequest;
    }

    @Deprecated
    private protected long getEstimatedNetworkBytes() {
        if (this.networkDownloadBytes == -1 && this.networkUploadBytes == -1) {
            return -1L;
        }
        if (this.networkDownloadBytes == -1) {
            return this.networkUploadBytes;
        }
        if (this.networkUploadBytes == -1) {
            return this.networkDownloadBytes;
        }
        return this.networkDownloadBytes + this.networkUploadBytes;
    }

    public long getEstimatedNetworkDownloadBytes() {
        return this.networkDownloadBytes;
    }

    public long getEstimatedNetworkUploadBytes() {
        return this.networkUploadBytes;
    }

    public long getMinLatencyMillis() {
        return this.minLatencyMillis;
    }

    public long getMaxExecutionDelayMillis() {
        return this.maxExecutionDelayMillis;
    }

    public boolean isPeriodic() {
        return this.isPeriodic;
    }

    public boolean isPersisted() {
        return this.isPersisted;
    }

    public long getIntervalMillis() {
        return this.intervalMillis;
    }

    public long getFlexMillis() {
        return this.flexMillis;
    }

    public long getInitialBackoffMillis() {
        return this.initialBackoffMillis;
    }

    public int getBackoffPolicy() {
        return this.backoffPolicy;
    }

    public boolean isImportantWhileForeground() {
        return (this.flags & 2) != 0;
    }

    public boolean isPrefetch() {
        return (this.flags & 4) != 0;
    }

    public synchronized boolean hasEarlyConstraint() {
        return this.hasEarlyConstraint;
    }

    public synchronized boolean hasLateConstraint() {
        return this.hasLateConstraint;
    }

    private static synchronized boolean kindofEqualsBundle(BaseBundle a, BaseBundle b) {
        return a == b || (a != null && a.kindofEquals(b));
    }

    public boolean equals(Object o) {
        if (o instanceof JobInfo) {
            JobInfo j = (JobInfo) o;
            return this.jobId == j.jobId && kindofEqualsBundle(this.extras, j.extras) && kindofEqualsBundle(this.transientExtras, j.transientExtras) && this.clipData == j.clipData && this.clipGrantFlags == j.clipGrantFlags && Objects.equals(this.service, j.service) && this.constraintFlags == j.constraintFlags && Arrays.equals(this.triggerContentUris, j.triggerContentUris) && this.triggerContentUpdateDelay == j.triggerContentUpdateDelay && this.triggerContentMaxDelay == j.triggerContentMaxDelay && this.hasEarlyConstraint == j.hasEarlyConstraint && this.hasLateConstraint == j.hasLateConstraint && Objects.equals(this.networkRequest, j.networkRequest) && this.networkDownloadBytes == j.networkDownloadBytes && this.networkUploadBytes == j.networkUploadBytes && this.minLatencyMillis == j.minLatencyMillis && this.maxExecutionDelayMillis == j.maxExecutionDelayMillis && this.isPeriodic == j.isPeriodic && this.isPersisted == j.isPersisted && this.intervalMillis == j.intervalMillis && this.flexMillis == j.flexMillis && this.initialBackoffMillis == j.initialBackoffMillis && this.backoffPolicy == j.backoffPolicy && this.priority == j.priority && this.flags == j.flags;
        }
        return false;
    }

    public int hashCode() {
        int hashCode = this.jobId;
        if (this.extras != null) {
            hashCode = (31 * hashCode) + this.extras.hashCode();
        }
        if (this.transientExtras != null) {
            hashCode = (31 * hashCode) + this.transientExtras.hashCode();
        }
        if (this.clipData != null) {
            hashCode = (31 * hashCode) + this.clipData.hashCode();
        }
        int hashCode2 = (31 * hashCode) + this.clipGrantFlags;
        if (this.service != null) {
            hashCode2 = (31 * hashCode2) + this.service.hashCode();
        }
        int hashCode3 = (31 * hashCode2) + this.constraintFlags;
        if (this.triggerContentUris != null) {
            hashCode3 = (31 * hashCode3) + Arrays.hashCode(this.triggerContentUris);
        }
        int hashCode4 = (31 * ((31 * ((31 * ((31 * hashCode3) + Long.hashCode(this.triggerContentUpdateDelay))) + Long.hashCode(this.triggerContentMaxDelay))) + Boolean.hashCode(this.hasEarlyConstraint))) + Boolean.hashCode(this.hasLateConstraint);
        if (this.networkRequest != null) {
            hashCode4 = (31 * hashCode4) + this.networkRequest.hashCode();
        }
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * hashCode4) + Long.hashCode(this.networkDownloadBytes))) + Long.hashCode(this.networkUploadBytes))) + Long.hashCode(this.minLatencyMillis))) + Long.hashCode(this.maxExecutionDelayMillis))) + Boolean.hashCode(this.isPeriodic))) + Boolean.hashCode(this.isPersisted))) + Long.hashCode(this.intervalMillis))) + Long.hashCode(this.flexMillis))) + Long.hashCode(this.initialBackoffMillis))) + this.backoffPolicy)) + this.priority)) + this.flags;
    }

    private synchronized JobInfo(Parcel in) {
        this.jobId = in.readInt();
        this.extras = in.readPersistableBundle();
        this.transientExtras = in.readBundle();
        if (in.readInt() != 0) {
            this.clipData = ClipData.CREATOR.createFromParcel(in);
            this.clipGrantFlags = in.readInt();
        } else {
            this.clipData = null;
            this.clipGrantFlags = 0;
        }
        this.service = (ComponentName) in.readParcelable(null);
        this.constraintFlags = in.readInt();
        this.triggerContentUris = (TriggerContentUri[]) in.createTypedArray(TriggerContentUri.CREATOR);
        this.triggerContentUpdateDelay = in.readLong();
        this.triggerContentMaxDelay = in.readLong();
        if (in.readInt() != 0) {
            this.networkRequest = NetworkRequest.CREATOR.createFromParcel(in);
        } else {
            this.networkRequest = null;
        }
        this.networkDownloadBytes = in.readLong();
        this.networkUploadBytes = in.readLong();
        this.minLatencyMillis = in.readLong();
        this.maxExecutionDelayMillis = in.readLong();
        this.isPeriodic = in.readInt() == 1;
        this.isPersisted = in.readInt() == 1;
        this.intervalMillis = in.readLong();
        this.flexMillis = in.readLong();
        this.initialBackoffMillis = in.readLong();
        this.backoffPolicy = in.readInt();
        this.hasEarlyConstraint = in.readInt() == 1;
        this.hasLateConstraint = in.readInt() == 1;
        this.priority = in.readInt();
        this.flags = in.readInt();
    }

    private synchronized JobInfo(Builder b) {
        TriggerContentUri[] triggerContentUriArr;
        this.jobId = b.mJobId;
        this.extras = b.mExtras.deepCopy();
        this.transientExtras = b.mTransientExtras.deepCopy();
        this.clipData = b.mClipData;
        this.clipGrantFlags = b.mClipGrantFlags;
        this.service = b.mJobService;
        this.constraintFlags = b.mConstraintFlags;
        if (b.mTriggerContentUris == null) {
            triggerContentUriArr = null;
        } else {
            triggerContentUriArr = (TriggerContentUri[]) b.mTriggerContentUris.toArray(new TriggerContentUri[b.mTriggerContentUris.size()]);
        }
        this.triggerContentUris = triggerContentUriArr;
        this.triggerContentUpdateDelay = b.mTriggerContentUpdateDelay;
        this.triggerContentMaxDelay = b.mTriggerContentMaxDelay;
        this.networkRequest = b.mNetworkRequest;
        this.networkDownloadBytes = b.mNetworkDownloadBytes;
        this.networkUploadBytes = b.mNetworkUploadBytes;
        this.minLatencyMillis = b.mMinLatencyMillis;
        this.maxExecutionDelayMillis = b.mMaxExecutionDelayMillis;
        this.isPeriodic = b.mIsPeriodic;
        this.isPersisted = b.mIsPersisted;
        this.intervalMillis = b.mIntervalMillis;
        this.flexMillis = b.mFlexMillis;
        this.initialBackoffMillis = b.mInitialBackoffMillis;
        this.backoffPolicy = b.mBackoffPolicy;
        this.hasEarlyConstraint = b.mHasEarlyConstraint;
        this.hasLateConstraint = b.mHasLateConstraint;
        this.priority = b.mPriority;
        this.flags = b.mFlags;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.jobId);
        out.writePersistableBundle(this.extras);
        out.writeBundle(this.transientExtras);
        if (this.clipData != null) {
            out.writeInt(1);
            this.clipData.writeToParcel(out, flags);
            out.writeInt(this.clipGrantFlags);
        } else {
            out.writeInt(0);
        }
        out.writeParcelable(this.service, flags);
        out.writeInt(this.constraintFlags);
        out.writeTypedArray(this.triggerContentUris, flags);
        out.writeLong(this.triggerContentUpdateDelay);
        out.writeLong(this.triggerContentMaxDelay);
        if (this.networkRequest != null) {
            out.writeInt(1);
            this.networkRequest.writeToParcel(out, flags);
        } else {
            out.writeInt(0);
        }
        out.writeLong(this.networkDownloadBytes);
        out.writeLong(this.networkUploadBytes);
        out.writeLong(this.minLatencyMillis);
        out.writeLong(this.maxExecutionDelayMillis);
        out.writeInt(this.isPeriodic ? 1 : 0);
        out.writeInt(this.isPersisted ? 1 : 0);
        out.writeLong(this.intervalMillis);
        out.writeLong(this.flexMillis);
        out.writeLong(this.initialBackoffMillis);
        out.writeInt(this.backoffPolicy);
        out.writeInt(this.hasEarlyConstraint ? 1 : 0);
        out.writeInt(this.hasLateConstraint ? 1 : 0);
        out.writeInt(this.priority);
        out.writeInt(this.flags);
    }

    public String toString() {
        return "(job:" + this.jobId + "/" + this.service.flattenToShortString() + ")";
    }

    /* loaded from: classes.dex */
    public static final class TriggerContentUri implements Parcelable {
        public static final Parcelable.Creator<TriggerContentUri> CREATOR = new Parcelable.Creator<TriggerContentUri>() { // from class: android.app.job.JobInfo.TriggerContentUri.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public TriggerContentUri createFromParcel(Parcel in) {
                return new TriggerContentUri(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public TriggerContentUri[] newArray(int size) {
                return new TriggerContentUri[size];
            }
        };
        public static final int FLAG_NOTIFY_FOR_DESCENDANTS = 1;
        private final int mFlags;
        private final Uri mUri;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes.dex */
        public @interface Flags {
        }

        public TriggerContentUri(Uri uri, int flags) {
            this.mUri = uri;
            this.mFlags = flags;
        }

        public Uri getUri() {
            return this.mUri;
        }

        public int getFlags() {
            return this.mFlags;
        }

        public boolean equals(Object o) {
            if (o instanceof TriggerContentUri) {
                TriggerContentUri t = (TriggerContentUri) o;
                return Objects.equals(t.mUri, this.mUri) && t.mFlags == this.mFlags;
            }
            return false;
        }

        public int hashCode() {
            return (this.mUri == null ? 0 : this.mUri.hashCode()) ^ this.mFlags;
        }

        private synchronized TriggerContentUri(Parcel in) {
            this.mUri = Uri.CREATOR.createFromParcel(in);
            this.mFlags = in.readInt();
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            this.mUri.writeToParcel(out, flags);
            out.writeInt(this.mFlags);
        }
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        private ClipData mClipData;
        private int mClipGrantFlags;
        private int mConstraintFlags;
        private int mFlags;
        private long mFlexMillis;
        private boolean mHasEarlyConstraint;
        private boolean mHasLateConstraint;
        private long mIntervalMillis;
        private boolean mIsPeriodic;
        private boolean mIsPersisted;
        private final int mJobId;
        private final ComponentName mJobService;
        private long mMaxExecutionDelayMillis;
        private long mMinLatencyMillis;
        private NetworkRequest mNetworkRequest;
        private ArrayList<TriggerContentUri> mTriggerContentUris;
        private PersistableBundle mExtras = PersistableBundle.EMPTY;
        private Bundle mTransientExtras = Bundle.EMPTY;
        private int mPriority = 0;
        private long mNetworkDownloadBytes = -1;
        private long mNetworkUploadBytes = -1;
        private long mTriggerContentUpdateDelay = -1;
        private long mTriggerContentMaxDelay = -1;
        private long mInitialBackoffMillis = 30000;
        private int mBackoffPolicy = 1;
        private boolean mBackoffPolicySet = false;

        public Builder(int jobId, ComponentName jobService) {
            this.mJobService = jobService;
            this.mJobId = jobId;
        }

        private protected Builder setPriority(int priority) {
            this.mPriority = priority;
            return this;
        }

        private protected Builder setFlags(int flags) {
            this.mFlags = flags;
            return this;
        }

        public Builder setExtras(PersistableBundle extras) {
            this.mExtras = extras;
            return this;
        }

        public Builder setTransientExtras(Bundle extras) {
            this.mTransientExtras = extras;
            return this;
        }

        public Builder setClipData(ClipData clip, int grantFlags) {
            this.mClipData = clip;
            this.mClipGrantFlags = grantFlags;
            return this;
        }

        public Builder setRequiredNetworkType(int networkType) {
            if (networkType == 0) {
                return setRequiredNetwork(null);
            }
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            builder.addCapability(12);
            builder.addCapability(16);
            builder.removeCapability(15);
            if (networkType != 1) {
                if (networkType == 2) {
                    builder.addCapability(11);
                } else if (networkType == 3) {
                    builder.addCapability(18);
                } else if (networkType == 4) {
                    builder.addTransportType(0);
                }
            }
            return setRequiredNetwork(builder.build());
        }

        public Builder setRequiredNetwork(NetworkRequest networkRequest) {
            this.mNetworkRequest = networkRequest;
            return this;
        }

        @Deprecated
        private protected Builder setEstimatedNetworkBytes(long networkBytes) {
            return setEstimatedNetworkBytes(networkBytes, -1L);
        }

        public Builder setEstimatedNetworkBytes(long downloadBytes, long uploadBytes) {
            this.mNetworkDownloadBytes = downloadBytes;
            this.mNetworkUploadBytes = uploadBytes;
            return this;
        }

        public Builder setRequiresCharging(boolean requiresCharging) {
            this.mConstraintFlags = (this.mConstraintFlags & (-2)) | (requiresCharging ? 1 : 0);
            return this;
        }

        public Builder setRequiresBatteryNotLow(boolean batteryNotLow) {
            this.mConstraintFlags = (this.mConstraintFlags & (-3)) | (batteryNotLow ? 2 : 0);
            return this;
        }

        public Builder setRequiresDeviceIdle(boolean requiresDeviceIdle) {
            this.mConstraintFlags = (this.mConstraintFlags & (-5)) | (requiresDeviceIdle ? 4 : 0);
            return this;
        }

        public Builder setRequiresStorageNotLow(boolean storageNotLow) {
            this.mConstraintFlags = (this.mConstraintFlags & (-9)) | (storageNotLow ? 8 : 0);
            return this;
        }

        public Builder addTriggerContentUri(TriggerContentUri uri) {
            if (this.mTriggerContentUris == null) {
                this.mTriggerContentUris = new ArrayList<>();
            }
            this.mTriggerContentUris.add(uri);
            return this;
        }

        public Builder setTriggerContentUpdateDelay(long durationMs) {
            this.mTriggerContentUpdateDelay = durationMs;
            return this;
        }

        public Builder setTriggerContentMaxDelay(long durationMs) {
            this.mTriggerContentMaxDelay = durationMs;
            return this;
        }

        public Builder setPeriodic(long intervalMillis) {
            return setPeriodic(intervalMillis, intervalMillis);
        }

        public Builder setPeriodic(long intervalMillis, long flexMillis) {
            long minPeriod = JobInfo.getMinPeriodMillis();
            if (intervalMillis < minPeriod) {
                String str = JobInfo.TAG;
                Log.w(str, "Requested interval " + TimeUtils.formatDuration(intervalMillis) + " for job " + this.mJobId + " is too small; raising to " + TimeUtils.formatDuration(minPeriod));
                intervalMillis = minPeriod;
            }
            long percentClamp = (5 * intervalMillis) / 100;
            long minFlex = Math.max(percentClamp, JobInfo.getMinFlexMillis());
            if (flexMillis < minFlex) {
                String str2 = JobInfo.TAG;
                Log.w(str2, "Requested flex " + TimeUtils.formatDuration(flexMillis) + " for job " + this.mJobId + " is too small; raising to " + TimeUtils.formatDuration(minFlex));
                flexMillis = minFlex;
            }
            this.mIsPeriodic = true;
            this.mIntervalMillis = intervalMillis;
            this.mFlexMillis = flexMillis;
            this.mHasLateConstraint = true;
            this.mHasEarlyConstraint = true;
            return this;
        }

        public Builder setMinimumLatency(long minLatencyMillis) {
            this.mMinLatencyMillis = minLatencyMillis;
            this.mHasEarlyConstraint = true;
            return this;
        }

        public Builder setOverrideDeadline(long maxExecutionDelayMillis) {
            this.mMaxExecutionDelayMillis = maxExecutionDelayMillis;
            this.mHasLateConstraint = true;
            return this;
        }

        public Builder setBackoffCriteria(long initialBackoffMillis, int backoffPolicy) {
            long minBackoff = JobInfo.getMinBackoffMillis();
            if (initialBackoffMillis < minBackoff) {
                String str = JobInfo.TAG;
                Log.w(str, "Requested backoff " + TimeUtils.formatDuration(initialBackoffMillis) + " for job " + this.mJobId + " is too small; raising to " + TimeUtils.formatDuration(minBackoff));
                initialBackoffMillis = minBackoff;
            }
            this.mBackoffPolicySet = true;
            this.mInitialBackoffMillis = initialBackoffMillis;
            this.mBackoffPolicy = backoffPolicy;
            return this;
        }

        public Builder setImportantWhileForeground(boolean importantWhileForeground) {
            if (importantWhileForeground) {
                this.mFlags |= 2;
            } else {
                this.mFlags &= -3;
            }
            return this;
        }

        @Deprecated
        private protected Builder setIsPrefetch(boolean isPrefetch) {
            return setPrefetch(isPrefetch);
        }

        public Builder setPrefetch(boolean prefetch) {
            if (prefetch) {
                this.mFlags |= 4;
            } else {
                this.mFlags &= -5;
            }
            return this;
        }

        public Builder setPersisted(boolean isPersisted) {
            this.mIsPersisted = isPersisted;
            return this;
        }

        public JobInfo build() {
            if (!this.mHasEarlyConstraint && !this.mHasLateConstraint && this.mConstraintFlags == 0 && this.mNetworkRequest == null && this.mTriggerContentUris == null) {
                throw new IllegalArgumentException("You're trying to build a job with no constraints, this is not allowed.");
            }
            if ((this.mNetworkDownloadBytes > 0 || this.mNetworkUploadBytes > 0) && this.mNetworkRequest == null) {
                throw new IllegalArgumentException("Can't provide estimated network usage without requiring a network");
            }
            if (this.mIsPersisted && this.mNetworkRequest != null && this.mNetworkRequest.networkCapabilities.getNetworkSpecifier() != null) {
                throw new IllegalArgumentException("Network specifiers aren't supported for persistent jobs");
            }
            if (this.mIsPeriodic) {
                if (this.mMaxExecutionDelayMillis != 0) {
                    throw new IllegalArgumentException("Can't call setOverrideDeadline() on a periodic job.");
                }
                if (this.mMinLatencyMillis != 0) {
                    throw new IllegalArgumentException("Can't call setMinimumLatency() on a periodic job");
                }
                if (this.mTriggerContentUris != null) {
                    throw new IllegalArgumentException("Can't call addTriggerContentUri() on a periodic job");
                }
            }
            if (this.mIsPersisted) {
                if (this.mTriggerContentUris != null) {
                    throw new IllegalArgumentException("Can't call addTriggerContentUri() on a persisted job");
                }
                if (!this.mTransientExtras.isEmpty()) {
                    throw new IllegalArgumentException("Can't call setTransientExtras() on a persisted job");
                }
                if (this.mClipData != null) {
                    throw new IllegalArgumentException("Can't call setClipData() on a persisted job");
                }
            }
            if ((this.mFlags & 2) != 0 && this.mHasEarlyConstraint) {
                throw new IllegalArgumentException("An important while foreground job cannot have a time delay");
            }
            if (this.mBackoffPolicySet && (this.mConstraintFlags & 4) != 0) {
                throw new IllegalArgumentException("An idle mode job will not respect any back-off policy, so calling setBackoffCriteria with setRequiresDeviceIdle is an error.");
            }
            return new JobInfo(this);
        }
    }
}
