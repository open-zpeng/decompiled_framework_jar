package android.location;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.WorkSource;
import android.util.TimeUtils;
@SystemApi
/* loaded from: classes.dex */
public final class LocationRequest implements Parcelable {
    public static final int ACCURACY_BLOCK = 102;
    public static final int ACCURACY_CITY = 104;
    public static final int ACCURACY_FINE = 100;
    public static final Parcelable.Creator<LocationRequest> CREATOR = new Parcelable.Creator<LocationRequest>() { // from class: android.location.LocationRequest.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LocationRequest createFromParcel(Parcel in) {
            LocationRequest request = new LocationRequest();
            request.setQuality(in.readInt());
            request.setFastestInterval(in.readLong());
            request.setInterval(in.readLong());
            request.setExpireAt(in.readLong());
            request.setNumUpdates(in.readInt());
            request.setSmallestDisplacement(in.readFloat());
            request.setHideFromAppOps(in.readInt() != 0);
            request.setLowPowerMode(in.readInt() != 0);
            String provider = in.readString();
            if (provider != null) {
                request.setProvider(provider);
            }
            WorkSource workSource = (WorkSource) in.readParcelable(null);
            if (workSource != null) {
                request.setWorkSource(workSource);
            }
            return request;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LocationRequest[] newArray(int size) {
            return new LocationRequest[size];
        }
    };
    private static final double FASTEST_INTERVAL_FACTOR = 6.0d;
    public static final int POWER_HIGH = 203;
    public static final int POWER_LOW = 201;
    public static final int POWER_NONE = 200;
    public protected long mExpireAt;
    public protected boolean mExplicitFastestInterval;
    public protected long mFastestInterval;
    public protected boolean mHideFromAppOps;
    public protected long mInterval;
    private boolean mLowPowerMode;
    public protected int mNumUpdates;
    public protected String mProvider;
    public protected int mQuality;
    public protected float mSmallestDisplacement;
    public protected WorkSource mWorkSource;

    public static LocationRequest create() {
        LocationRequest request = new LocationRequest();
        return request;
    }

    @SystemApi
    public static LocationRequest createFromDeprecatedProvider(String provider, long minTime, float minDistance, boolean singleShot) {
        int quality;
        if (minTime < 0) {
            minTime = 0;
        }
        if (minDistance < 0.0f) {
            minDistance = 0.0f;
        }
        if (LocationManager.PASSIVE_PROVIDER.equals(provider)) {
            quality = 200;
        } else if (LocationManager.GPS_PROVIDER.equals(provider)) {
            quality = 100;
        } else {
            quality = 201;
        }
        LocationRequest request = new LocationRequest().setProvider(provider).setQuality(quality).setInterval(minTime).setFastestInterval(minTime).setSmallestDisplacement(minDistance);
        if (singleShot) {
            request.setNumUpdates(1);
        }
        return request;
    }

    @SystemApi
    public static LocationRequest createFromDeprecatedCriteria(Criteria criteria, long minTime, float minDistance, boolean singleShot) {
        int quality;
        if (minTime < 0) {
            minTime = 0;
        }
        if (minDistance < 0.0f) {
            minDistance = 0.0f;
        }
        switch (criteria.getAccuracy()) {
            case 1:
                quality = 100;
                break;
            case 2:
                quality = 102;
                break;
            default:
                if (criteria.getPowerRequirement() == 3) {
                    quality = 203;
                    break;
                } else {
                    quality = 201;
                    break;
                }
        }
        LocationRequest request = new LocationRequest().setQuality(quality).setInterval(minTime).setFastestInterval(minTime).setSmallestDisplacement(minDistance);
        if (singleShot) {
            request.setNumUpdates(1);
        }
        return request;
    }

    public synchronized LocationRequest() {
        this.mQuality = 201;
        this.mInterval = 3600000L;
        this.mFastestInterval = (long) (this.mInterval / FASTEST_INTERVAL_FACTOR);
        this.mExplicitFastestInterval = false;
        this.mExpireAt = Long.MAX_VALUE;
        this.mNumUpdates = Integer.MAX_VALUE;
        this.mSmallestDisplacement = 0.0f;
        this.mWorkSource = null;
        this.mHideFromAppOps = false;
        this.mProvider = LocationManager.FUSED_PROVIDER;
        this.mLowPowerMode = false;
    }

    public synchronized LocationRequest(LocationRequest src) {
        this.mQuality = 201;
        this.mInterval = 3600000L;
        this.mFastestInterval = (long) (this.mInterval / FASTEST_INTERVAL_FACTOR);
        this.mExplicitFastestInterval = false;
        this.mExpireAt = Long.MAX_VALUE;
        this.mNumUpdates = Integer.MAX_VALUE;
        this.mSmallestDisplacement = 0.0f;
        this.mWorkSource = null;
        this.mHideFromAppOps = false;
        this.mProvider = LocationManager.FUSED_PROVIDER;
        this.mLowPowerMode = false;
        this.mQuality = src.mQuality;
        this.mInterval = src.mInterval;
        this.mFastestInterval = src.mFastestInterval;
        this.mExplicitFastestInterval = src.mExplicitFastestInterval;
        this.mExpireAt = src.mExpireAt;
        this.mNumUpdates = src.mNumUpdates;
        this.mSmallestDisplacement = src.mSmallestDisplacement;
        this.mProvider = src.mProvider;
        this.mWorkSource = src.mWorkSource;
        this.mHideFromAppOps = src.mHideFromAppOps;
        this.mLowPowerMode = src.mLowPowerMode;
    }

    public LocationRequest setQuality(int quality) {
        checkQuality(quality);
        this.mQuality = quality;
        return this;
    }

    public int getQuality() {
        return this.mQuality;
    }

    public LocationRequest setInterval(long millis) {
        checkInterval(millis);
        this.mInterval = millis;
        if (!this.mExplicitFastestInterval) {
            this.mFastestInterval = (long) (this.mInterval / FASTEST_INTERVAL_FACTOR);
        }
        return this;
    }

    public long getInterval() {
        return this.mInterval;
    }

    @SystemApi
    public LocationRequest setLowPowerMode(boolean enabled) {
        this.mLowPowerMode = enabled;
        return this;
    }

    @SystemApi
    public boolean isLowPowerMode() {
        return this.mLowPowerMode;
    }

    public LocationRequest setFastestInterval(long millis) {
        checkInterval(millis);
        this.mExplicitFastestInterval = true;
        this.mFastestInterval = millis;
        return this;
    }

    public long getFastestInterval() {
        return this.mFastestInterval;
    }

    public LocationRequest setExpireIn(long millis) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (millis > Long.MAX_VALUE - elapsedRealtime) {
            this.mExpireAt = Long.MAX_VALUE;
        } else {
            this.mExpireAt = millis + elapsedRealtime;
        }
        if (this.mExpireAt < 0) {
            this.mExpireAt = 0L;
        }
        return this;
    }

    public LocationRequest setExpireAt(long millis) {
        this.mExpireAt = millis;
        if (this.mExpireAt < 0) {
            this.mExpireAt = 0L;
        }
        return this;
    }

    public long getExpireAt() {
        return this.mExpireAt;
    }

    public LocationRequest setNumUpdates(int numUpdates) {
        if (numUpdates <= 0) {
            throw new IllegalArgumentException("invalid numUpdates: " + numUpdates);
        }
        this.mNumUpdates = numUpdates;
        return this;
    }

    public int getNumUpdates() {
        return this.mNumUpdates;
    }

    public synchronized void decrementNumUpdates() {
        if (this.mNumUpdates != Integer.MAX_VALUE) {
            this.mNumUpdates--;
        }
        if (this.mNumUpdates < 0) {
            this.mNumUpdates = 0;
        }
    }

    @SystemApi
    public LocationRequest setProvider(String provider) {
        checkProvider(provider);
        this.mProvider = provider;
        return this;
    }

    @SystemApi
    public String getProvider() {
        return this.mProvider;
    }

    @SystemApi
    public LocationRequest setSmallestDisplacement(float meters) {
        checkDisplacement(meters);
        this.mSmallestDisplacement = meters;
        return this;
    }

    @SystemApi
    public float getSmallestDisplacement() {
        return this.mSmallestDisplacement;
    }

    @SystemApi
    public void setWorkSource(WorkSource workSource) {
        this.mWorkSource = workSource;
    }

    @SystemApi
    public WorkSource getWorkSource() {
        return this.mWorkSource;
    }

    @SystemApi
    public void setHideFromAppOps(boolean hideFromAppOps) {
        this.mHideFromAppOps = hideFromAppOps;
    }

    @SystemApi
    public boolean getHideFromAppOps() {
        return this.mHideFromAppOps;
    }

    public protected static void checkInterval(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("invalid interval: " + millis);
        }
    }

    public protected static void checkQuality(int quality) {
        if (quality == 100 || quality == 102 || quality == 104 || quality == 203) {
            return;
        }
        switch (quality) {
            case 200:
            case 201:
                return;
            default:
                throw new IllegalArgumentException("invalid quality: " + quality);
        }
    }

    public protected static void checkDisplacement(float meters) {
        if (meters < 0.0f) {
            throw new IllegalArgumentException("invalid displacement: " + meters);
        }
    }

    public protected static void checkProvider(String name) {
        if (name == null) {
            throw new IllegalArgumentException("invalid provider: " + name);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.mQuality);
        parcel.writeLong(this.mFastestInterval);
        parcel.writeLong(this.mInterval);
        parcel.writeLong(this.mExpireAt);
        parcel.writeInt(this.mNumUpdates);
        parcel.writeFloat(this.mSmallestDisplacement);
        parcel.writeInt(this.mHideFromAppOps ? 1 : 0);
        parcel.writeInt(this.mLowPowerMode ? 1 : 0);
        parcel.writeString(this.mProvider);
        parcel.writeParcelable(this.mWorkSource, 0);
    }

    public static synchronized String qualityToString(int quality) {
        if (quality != 100) {
            if (quality != 102) {
                if (quality != 104) {
                    if (quality != 203) {
                        switch (quality) {
                            case 200:
                                return "POWER_NONE";
                            case 201:
                                return "POWER_LOW";
                            default:
                                return "???";
                        }
                    }
                    return "POWER_HIGH";
                }
                return "ACCURACY_CITY";
            }
            return "ACCURACY_BLOCK";
        }
        return "ACCURACY_FINE";
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Request[");
        s.append(qualityToString(this.mQuality));
        if (this.mProvider != null) {
            s.append(' ');
            s.append(this.mProvider);
        }
        if (this.mQuality != 200) {
            s.append(" requested=");
            TimeUtils.formatDuration(this.mInterval, s);
        }
        s.append(" fastest=");
        TimeUtils.formatDuration(this.mFastestInterval, s);
        if (this.mExpireAt != Long.MAX_VALUE) {
            long expireIn = this.mExpireAt - SystemClock.elapsedRealtime();
            s.append(" expireIn=");
            TimeUtils.formatDuration(expireIn, s);
        }
        if (this.mNumUpdates != Integer.MAX_VALUE) {
            s.append(" num=");
            s.append(this.mNumUpdates);
        }
        s.append(" lowPowerMode=");
        s.append(this.mLowPowerMode);
        s.append(']');
        return s.toString();
    }
}
