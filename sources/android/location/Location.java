package android.location;

import android.annotation.SystemApi;
import android.bluetooth.BluetoothHidDevice;
import android.net.wifi.WifiScanner;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.provider.SettingsStringUtil;
import android.util.Printer;
import android.util.TimeUtils;
import com.xiaopeng.util.FeatureOption;
import java.text.DecimalFormat;
import java.util.StringTokenizer;
/* loaded from: classes.dex */
public class Location implements Parcelable {
    public static final String EXTRA_COARSE_LOCATION = "coarseLocation";
    public static final String EXTRA_NO_GPS_LOCATION = "noGPSLocation";
    public static final int FORMAT_DEGREES = 0;
    public static final int FORMAT_MINUTES = 1;
    public static final int FORMAT_SECONDS = 2;
    private static final int HAS_ALTITUDE_MASK = 1;
    private static final int HAS_BEARING_ACCURACY_MASK = 128;
    private static final int HAS_BEARING_MASK = 4;
    private static final int HAS_HORIZONTAL_ACCURACY_MASK = 8;
    private static final int HAS_MOCK_PROVIDER_MASK = 16;
    private static final int HAS_SPEED_ACCURACY_MASK = 64;
    private static final int HAS_SPEED_MASK = 2;
    private static final int HAS_VERTICAL_ACCURACY_MASK = 32;
    public protected String mProvider;
    private static ThreadLocal<BearingDistanceCache> sBearingDistanceCache = new ThreadLocal<BearingDistanceCache>() { // from class: android.location.Location.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public BearingDistanceCache initialValue() {
            return new BearingDistanceCache();
        }
    };
    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() { // from class: android.location.Location.2
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Location createFromParcel(Parcel in) {
            String provider = in.readString();
            Location l = new Location(provider);
            l.mTime = in.readLong();
            l.mElapsedRealtimeNanos = in.readLong();
            l.mFieldsMask = in.readByte();
            l.mLatitude = in.readDouble();
            l.mLongitude = in.readDouble();
            l.mAltitude = in.readDouble();
            l.mSpeed = in.readFloat();
            l.mBearing = in.readFloat();
            l.mHorizontalAccuracyMeters = in.readFloat();
            l.mVerticalAccuracyMeters = in.readFloat();
            l.mSpeedAccuracyMetersPerSecond = in.readFloat();
            l.mBearingAccuracyDegrees = in.readFloat();
            l.mExtras = Bundle.setDefusable(in.readBundle(), true);
            return l;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
    private long mTime = 0;
    public protected long mElapsedRealtimeNanos = 0;
    private double mLatitude = FeatureOption.FO_BOOT_POLICY_CPU;
    private double mLongitude = FeatureOption.FO_BOOT_POLICY_CPU;
    private double mAltitude = FeatureOption.FO_BOOT_POLICY_CPU;
    private float mSpeed = 0.0f;
    private float mBearing = 0.0f;
    private float mHorizontalAccuracyMeters = 0.0f;
    private float mVerticalAccuracyMeters = 0.0f;
    private float mSpeedAccuracyMetersPerSecond = 0.0f;
    private float mBearingAccuracyDegrees = 0.0f;
    private Bundle mExtras = null;
    private byte mFieldsMask = 0;

    public Location(String provider) {
        this.mProvider = provider;
    }

    public Location(Location l) {
        set(l);
    }

    public void set(Location l) {
        this.mProvider = l.mProvider;
        this.mTime = l.mTime;
        this.mElapsedRealtimeNanos = l.mElapsedRealtimeNanos;
        this.mFieldsMask = l.mFieldsMask;
        this.mLatitude = l.mLatitude;
        this.mLongitude = l.mLongitude;
        this.mAltitude = l.mAltitude;
        this.mSpeed = l.mSpeed;
        this.mBearing = l.mBearing;
        this.mHorizontalAccuracyMeters = l.mHorizontalAccuracyMeters;
        this.mVerticalAccuracyMeters = l.mVerticalAccuracyMeters;
        this.mSpeedAccuracyMetersPerSecond = l.mSpeedAccuracyMetersPerSecond;
        this.mBearingAccuracyDegrees = l.mBearingAccuracyDegrees;
        this.mExtras = l.mExtras == null ? null : new Bundle(l.mExtras);
    }

    public void reset() {
        this.mProvider = null;
        this.mTime = 0L;
        this.mElapsedRealtimeNanos = 0L;
        this.mFieldsMask = (byte) 0;
        this.mLatitude = FeatureOption.FO_BOOT_POLICY_CPU;
        this.mLongitude = FeatureOption.FO_BOOT_POLICY_CPU;
        this.mAltitude = FeatureOption.FO_BOOT_POLICY_CPU;
        this.mSpeed = 0.0f;
        this.mBearing = 0.0f;
        this.mHorizontalAccuracyMeters = 0.0f;
        this.mVerticalAccuracyMeters = 0.0f;
        this.mSpeedAccuracyMetersPerSecond = 0.0f;
        this.mBearingAccuracyDegrees = 0.0f;
        this.mExtras = null;
    }

    public static String convert(double coordinate, int outputType) {
        if (coordinate < -180.0d || coordinate > 180.0d || Double.isNaN(coordinate)) {
            throw new IllegalArgumentException("coordinate=" + coordinate);
        } else if (outputType != 0 && outputType != 1 && outputType != 2) {
            throw new IllegalArgumentException("outputType=" + outputType);
        } else {
            StringBuilder sb = new StringBuilder();
            if (coordinate < FeatureOption.FO_BOOT_POLICY_CPU) {
                sb.append('-');
                coordinate = -coordinate;
            }
            DecimalFormat df = new DecimalFormat("###.#####");
            if (outputType == 1 || outputType == 2) {
                int degrees = (int) Math.floor(coordinate);
                sb.append(degrees);
                sb.append(':');
                coordinate = (coordinate - degrees) * 60.0d;
                if (outputType == 2) {
                    int minutes = (int) Math.floor(coordinate);
                    sb.append(minutes);
                    sb.append(':');
                    coordinate = (coordinate - minutes) * 60.0d;
                }
            }
            sb.append(df.format(coordinate));
            return sb.toString();
        }
    }

    public static double convert(String coordinate) {
        double min;
        String coordinate2 = coordinate;
        if (coordinate2 == null) {
            throw new NullPointerException("coordinate");
        }
        boolean negative = false;
        if (coordinate2.charAt(0) == '-') {
            coordinate2 = coordinate2.substring(1);
            negative = true;
        }
        boolean negative2 = negative;
        String coordinate3 = coordinate2;
        StringTokenizer st = new StringTokenizer(coordinate3, SettingsStringUtil.DELIMITER);
        int tokens = st.countTokens();
        if (tokens < 1) {
            throw new IllegalArgumentException("coordinate=" + coordinate3);
        }
        try {
            String degrees = st.nextToken();
            try {
                if (tokens == 1) {
                    double val = Double.parseDouble(degrees);
                    return negative2 ? -val : val;
                }
                String minutes = st.nextToken();
                int deg = Integer.parseInt(degrees);
                double sec = FeatureOption.FO_BOOT_POLICY_CPU;
                boolean secPresent = false;
                if (st.hasMoreTokens()) {
                    min = Integer.parseInt(minutes);
                    String seconds = st.nextToken();
                    sec = Double.parseDouble(seconds);
                    secPresent = true;
                } else {
                    min = Double.parseDouble(minutes);
                }
                boolean isNegative180 = negative2 && deg == 180 && min == FeatureOption.FO_BOOT_POLICY_CPU && sec == FeatureOption.FO_BOOT_POLICY_CPU;
                try {
                    if (deg < FeatureOption.FO_BOOT_POLICY_CPU || (deg > 179 && !isNegative180)) {
                        throw new IllegalArgumentException("coordinate=" + coordinate3);
                    } else if (min < FeatureOption.FO_BOOT_POLICY_CPU || min >= 60.0d || (secPresent && min > 59.0d)) {
                        throw new IllegalArgumentException("coordinate=" + coordinate3);
                    } else if (sec < FeatureOption.FO_BOOT_POLICY_CPU || sec >= 60.0d) {
                        throw new IllegalArgumentException("coordinate=" + coordinate3);
                    } else {
                        double val2 = (((deg * 3600.0d) + (60.0d * min)) + sec) / 3600.0d;
                        return negative2 ? -val2 : val2;
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("coordinate=" + coordinate3);
                }
            } catch (NumberFormatException e2) {
            }
        } catch (NumberFormatException e3) {
        }
    }

    private static synchronized void computeDistanceAndBearing(double lat1, double lon1, double lat2, double lon2, BearingDistanceCache results) {
        double lat22;
        double lat12 = lat1 * 0.017453292519943295d;
        double lat23 = lat2 * 0.017453292519943295d;
        double lon12 = lon1 * 0.017453292519943295d;
        double lon22 = 0.017453292519943295d * lon2;
        double f = (6378137.0d - 6356752.3142d) / 6378137.0d;
        double aSqMinusBSqOverBSq = ((6378137.0d * 6378137.0d) - (6356752.3142d * 6356752.3142d)) / (6356752.3142d * 6356752.3142d);
        double L = lon22 - lon12;
        double A = FeatureOption.FO_BOOT_POLICY_CPU;
        double a = (1.0d - f) * Math.tan(lat12);
        double cosSigma = Math.atan(a);
        double lon23 = (1.0d - f) * Math.tan(lat23);
        double U2 = Math.atan(lon23);
        double cosU1 = Math.cos(cosSigma);
        double cosU2 = Math.cos(U2);
        double lon13 = Math.sin(cosSigma);
        double sinU2 = Math.sin(U2);
        double cosU1cosU2 = cosU1 * cosU2;
        double sinU1sinU2 = lon13 * sinU2;
        double sigma = FeatureOption.FO_BOOT_POLICY_CPU;
        double deltaSigma = FeatureOption.FO_BOOT_POLICY_CPU;
        double cosSigma2 = FeatureOption.FO_BOOT_POLICY_CPU;
        double sinSigma = FeatureOption.FO_BOOT_POLICY_CPU;
        double cosLambda = FeatureOption.FO_BOOT_POLICY_CPU;
        double sinLambda = FeatureOption.FO_BOOT_POLICY_CPU;
        int iter = 0;
        double lambda = L;
        while (true) {
            int iter2 = iter;
            double U22 = U2;
            if (iter2 < 20) {
                double lambdaOrig = lambda;
                double U1 = cosSigma;
                double U12 = lambda;
                cosLambda = Math.cos(U12);
                sinLambda = Math.sin(U12);
                double t1 = cosU2 * sinLambda;
                double t2 = (cosU1 * sinU2) - ((lon13 * cosU2) * cosLambda);
                double lambda2 = (t1 * t1) + (t2 * t2);
                lat22 = lat23;
                double lat24 = Math.sqrt(lambda2);
                double sinSigma2 = cosU1cosU2 * cosLambda;
                double sinSqSigma = sinU1sinU2 + sinSigma2;
                sigma = Math.atan2(lat24, sinSqSigma);
                double d = FeatureOption.FO_BOOT_POLICY_CPU;
                double sinAlpha = lat24 == FeatureOption.FO_BOOT_POLICY_CPU ? 0.0d : (cosU1cosU2 * sinLambda) / lat24;
                double cosSqAlpha = 1.0d - (sinAlpha * sinAlpha);
                if (cosSqAlpha != FeatureOption.FO_BOOT_POLICY_CPU) {
                    d = sinSqSigma - ((2.0d * sinU1sinU2) / cosSqAlpha);
                }
                double cos2SM = d;
                double uSquared = cosSqAlpha * aSqMinusBSqOverBSq;
                A = 1.0d + ((uSquared / 16384.0d) * (4096.0d + (((-768.0d) + ((320.0d - (175.0d * uSquared)) * uSquared)) * uSquared)));
                double B = (uSquared / 1024.0d) * (256.0d + (((-128.0d) + ((74.0d - (47.0d * uSquared)) * uSquared)) * uSquared));
                double C = (f / 16.0d) * cosSqAlpha * (4.0d + ((4.0d - (3.0d * cosSqAlpha)) * f));
                double cos2SMSq = cos2SM * cos2SM;
                deltaSigma = B * lat24 * (cos2SM + ((B / 4.0d) * ((((-1.0d) + (2.0d * cos2SMSq)) * sinSqSigma) - ((((B / 6.0d) * cos2SM) * ((-3.0d) + ((4.0d * lat24) * lat24))) * ((-3.0d) + (4.0d * cos2SMSq))))));
                double lambda3 = L + ((1.0d - C) * f * sinAlpha * (sigma + (C * lat24 * (cos2SM + (C * sinSqSigma * ((-1.0d) + (2.0d * cos2SM * cos2SM)))))));
                double sinSigma3 = (lambda3 - lambdaOrig) / lambda3;
                if (Math.abs(sinSigma3) < 1.0E-12d) {
                    break;
                }
                iter = iter2 + 1;
                cosSigma2 = sinSqSigma;
                U2 = U22;
                cosSigma = U1;
                lambda = lambda3;
                lat23 = lat22;
                sinSigma = lat24;
            } else {
                lat22 = lat23;
                break;
            }
        }
        float distance = (float) (6356752.3142d * A * (sigma - deltaSigma));
        results.mDistance = distance;
        float initialBearing = (float) Math.atan2(cosU2 * sinLambda, (cosU1 * sinU2) - ((lon13 * cosU2) * cosLambda));
        results.mInitialBearing = (float) (initialBearing * 57.29577951308232d);
        float finalBearing = (float) Math.atan2(cosU1 * sinLambda, ((-lon13) * cosU2) + (cosU1 * sinU2 * cosLambda));
        results.mFinalBearing = (float) (finalBearing * 57.29577951308232d);
        results.mLat1 = lat12;
        results.mLat2 = lat22;
        results.mLon1 = lon12;
        results.mLon2 = lon22;
    }

    public static void distanceBetween(double startLatitude, double startLongitude, double endLatitude, double endLongitude, float[] results) {
        if (results == null || results.length < 1) {
            throw new IllegalArgumentException("results is null or has length < 1");
        }
        BearingDistanceCache cache = sBearingDistanceCache.get();
        computeDistanceAndBearing(startLatitude, startLongitude, endLatitude, endLongitude, cache);
        results[0] = cache.mDistance;
        if (results.length > 1) {
            results[1] = cache.mInitialBearing;
            if (results.length > 2) {
                results[2] = cache.mFinalBearing;
            }
        }
    }

    public float distanceTo(Location dest) {
        BearingDistanceCache cache = sBearingDistanceCache.get();
        if (this.mLatitude != cache.mLat1 || this.mLongitude != cache.mLon1 || dest.mLatitude != cache.mLat2 || dest.mLongitude != cache.mLon2) {
            computeDistanceAndBearing(this.mLatitude, this.mLongitude, dest.mLatitude, dest.mLongitude, cache);
        }
        return cache.mDistance;
    }

    public float bearingTo(Location dest) {
        BearingDistanceCache cache = sBearingDistanceCache.get();
        if (this.mLatitude != cache.mLat1 || this.mLongitude != cache.mLon1 || dest.mLatitude != cache.mLat2 || dest.mLongitude != cache.mLon2) {
            computeDistanceAndBearing(this.mLatitude, this.mLongitude, dest.mLatitude, dest.mLongitude, cache);
        }
        return cache.mInitialBearing;
    }

    public String getProvider() {
        return this.mProvider;
    }

    public void setProvider(String provider) {
        this.mProvider = provider;
    }

    public long getTime() {
        return this.mTime;
    }

    public void setTime(long time) {
        this.mTime = time;
    }

    public long getElapsedRealtimeNanos() {
        return this.mElapsedRealtimeNanos;
    }

    public void setElapsedRealtimeNanos(long time) {
        this.mElapsedRealtimeNanos = time;
    }

    public double getLatitude() {
        return this.mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public double getLongitude() {
        return this.mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    public boolean hasAltitude() {
        return (this.mFieldsMask & 1) != 0;
    }

    public double getAltitude() {
        return this.mAltitude;
    }

    public void setAltitude(double altitude) {
        this.mAltitude = altitude;
        this.mFieldsMask = (byte) (this.mFieldsMask | 1);
    }

    @Deprecated
    public void removeAltitude() {
        this.mAltitude = FeatureOption.FO_BOOT_POLICY_CPU;
        this.mFieldsMask = (byte) (this.mFieldsMask & (-2));
    }

    public boolean hasSpeed() {
        return (this.mFieldsMask & 2) != 0;
    }

    public float getSpeed() {
        return this.mSpeed;
    }

    public void setSpeed(float speed) {
        this.mSpeed = speed;
        this.mFieldsMask = (byte) (this.mFieldsMask | 2);
    }

    @Deprecated
    public void removeSpeed() {
        this.mSpeed = 0.0f;
        this.mFieldsMask = (byte) (this.mFieldsMask & (-3));
    }

    public boolean hasBearing() {
        return (this.mFieldsMask & 4) != 0;
    }

    public float getBearing() {
        return this.mBearing;
    }

    public void setBearing(float bearing) {
        while (bearing < 0.0f) {
            bearing += 360.0f;
        }
        while (bearing >= 360.0f) {
            bearing -= 360.0f;
        }
        this.mBearing = bearing;
        this.mFieldsMask = (byte) (this.mFieldsMask | 4);
    }

    @Deprecated
    public void removeBearing() {
        this.mBearing = 0.0f;
        this.mFieldsMask = (byte) (this.mFieldsMask & (-5));
    }

    public boolean hasAccuracy() {
        return (this.mFieldsMask & 8) != 0;
    }

    public float getAccuracy() {
        return this.mHorizontalAccuracyMeters;
    }

    public void setAccuracy(float horizontalAccuracy) {
        this.mHorizontalAccuracyMeters = horizontalAccuracy;
        this.mFieldsMask = (byte) (this.mFieldsMask | 8);
    }

    @Deprecated
    public void removeAccuracy() {
        this.mHorizontalAccuracyMeters = 0.0f;
        this.mFieldsMask = (byte) (this.mFieldsMask & (-9));
    }

    public boolean hasVerticalAccuracy() {
        return (this.mFieldsMask & 32) != 0;
    }

    public float getVerticalAccuracyMeters() {
        return this.mVerticalAccuracyMeters;
    }

    public void setVerticalAccuracyMeters(float verticalAccuracyMeters) {
        this.mVerticalAccuracyMeters = verticalAccuracyMeters;
        this.mFieldsMask = (byte) (this.mFieldsMask | 32);
    }

    @Deprecated
    private protected void removeVerticalAccuracy() {
        this.mVerticalAccuracyMeters = 0.0f;
        this.mFieldsMask = (byte) (this.mFieldsMask & (-33));
    }

    public boolean hasSpeedAccuracy() {
        return (this.mFieldsMask & BluetoothHidDevice.SUBCLASS1_KEYBOARD) != 0;
    }

    public float getSpeedAccuracyMetersPerSecond() {
        return this.mSpeedAccuracyMetersPerSecond;
    }

    public void setSpeedAccuracyMetersPerSecond(float speedAccuracyMeterPerSecond) {
        this.mSpeedAccuracyMetersPerSecond = speedAccuracyMeterPerSecond;
        this.mFieldsMask = (byte) (this.mFieldsMask | BluetoothHidDevice.SUBCLASS1_KEYBOARD);
    }

    @Deprecated
    private protected void removeSpeedAccuracy() {
        this.mSpeedAccuracyMetersPerSecond = 0.0f;
        this.mFieldsMask = (byte) (this.mFieldsMask & (-65));
    }

    public boolean hasBearingAccuracy() {
        return (this.mFieldsMask & 128) != 0;
    }

    public float getBearingAccuracyDegrees() {
        return this.mBearingAccuracyDegrees;
    }

    public void setBearingAccuracyDegrees(float bearingAccuracyDegrees) {
        this.mBearingAccuracyDegrees = bearingAccuracyDegrees;
        this.mFieldsMask = (byte) (this.mFieldsMask | 128);
    }

    @Deprecated
    private protected void removeBearingAccuracy() {
        this.mBearingAccuracyDegrees = 0.0f;
        this.mFieldsMask = (byte) (this.mFieldsMask & (-129));
    }

    @SystemApi
    public boolean isComplete() {
        return (this.mProvider == null || !hasAccuracy() || this.mTime == 0 || this.mElapsedRealtimeNanos == 0) ? false : true;
    }

    @SystemApi
    public void makeComplete() {
        if (this.mProvider == null) {
            this.mProvider = "?";
        }
        if (!hasAccuracy()) {
            this.mFieldsMask = (byte) (this.mFieldsMask | 8);
            this.mHorizontalAccuracyMeters = 100.0f;
        }
        if (this.mTime == 0) {
            this.mTime = System.currentTimeMillis();
        }
        if (this.mElapsedRealtimeNanos == 0) {
            this.mElapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
        }
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public void setExtras(Bundle extras) {
        this.mExtras = extras == null ? null : new Bundle(extras);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Location[");
        s.append(this.mProvider);
        s.append(String.format(" %.6f,%.6f", Double.valueOf(this.mLatitude), Double.valueOf(this.mLongitude)));
        if (hasAccuracy()) {
            s.append(String.format(" hAcc=%.0f", Float.valueOf(this.mHorizontalAccuracyMeters)));
        } else {
            s.append(" hAcc=???");
        }
        if (this.mTime == 0) {
            s.append(" t=?!?");
        }
        if (this.mElapsedRealtimeNanos == 0) {
            s.append(" et=?!?");
        } else {
            s.append(" et=");
            TimeUtils.formatDuration(this.mElapsedRealtimeNanos / 1000000, s);
        }
        if (hasAltitude()) {
            s.append(" alt=");
            s.append(this.mAltitude);
        }
        if (hasSpeed()) {
            s.append(" vel=");
            s.append(this.mSpeed);
        }
        if (hasBearing()) {
            s.append(" bear=");
            s.append(this.mBearing);
        }
        if (hasVerticalAccuracy()) {
            s.append(String.format(" vAcc=%.0f", Float.valueOf(this.mVerticalAccuracyMeters)));
        } else {
            s.append(" vAcc=???");
        }
        if (hasSpeedAccuracy()) {
            s.append(String.format(" sAcc=%.0f", Float.valueOf(this.mSpeedAccuracyMetersPerSecond)));
        } else {
            s.append(" sAcc=???");
        }
        if (hasBearingAccuracy()) {
            s.append(String.format(" bAcc=%.0f", Float.valueOf(this.mBearingAccuracyDegrees)));
        } else {
            s.append(" bAcc=???");
        }
        if (isFromMockProvider()) {
            s.append(" mock");
        }
        if (this.mExtras != null) {
            s.append(" {");
            s.append(this.mExtras);
            s.append('}');
        }
        s.append(']');
        return s.toString();
    }

    public void dump(Printer pw, String prefix) {
        pw.println(prefix + toString());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.mProvider);
        parcel.writeLong(this.mTime);
        parcel.writeLong(this.mElapsedRealtimeNanos);
        parcel.writeByte(this.mFieldsMask);
        parcel.writeDouble(this.mLatitude);
        parcel.writeDouble(this.mLongitude);
        parcel.writeDouble(this.mAltitude);
        parcel.writeFloat(this.mSpeed);
        parcel.writeFloat(this.mBearing);
        parcel.writeFloat(this.mHorizontalAccuracyMeters);
        parcel.writeFloat(this.mVerticalAccuracyMeters);
        parcel.writeFloat(this.mSpeedAccuracyMetersPerSecond);
        parcel.writeFloat(this.mBearingAccuracyDegrees);
        parcel.writeBundle(this.mExtras);
    }

    public synchronized Location getExtraLocation(String key) {
        if (this.mExtras != null) {
            Parcelable value = this.mExtras.getParcelable(key);
            if (value instanceof Location) {
                return (Location) value;
            }
            return null;
        }
        return null;
    }

    private protected void setExtraLocation(String key, Location value) {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        this.mExtras.putParcelable(key, value);
    }

    public boolean isFromMockProvider() {
        return (this.mFieldsMask & WifiScanner.PnoSettings.PnoNetwork.FLAG_SAME_NETWORK) != 0;
    }

    @SystemApi
    public void setIsFromMockProvider(boolean isFromMockProvider) {
        if (isFromMockProvider) {
            this.mFieldsMask = (byte) (this.mFieldsMask | WifiScanner.PnoSettings.PnoNetwork.FLAG_SAME_NETWORK);
        } else {
            this.mFieldsMask = (byte) (this.mFieldsMask & (-17));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class BearingDistanceCache {
        private float mDistance;
        private float mFinalBearing;
        private float mInitialBearing;
        private double mLat1;
        private double mLat2;
        private double mLon1;
        private double mLon2;

        private synchronized BearingDistanceCache() {
            this.mLat1 = FeatureOption.FO_BOOT_POLICY_CPU;
            this.mLon1 = FeatureOption.FO_BOOT_POLICY_CPU;
            this.mLat2 = FeatureOption.FO_BOOT_POLICY_CPU;
            this.mLon2 = FeatureOption.FO_BOOT_POLICY_CPU;
            this.mDistance = 0.0f;
            this.mInitialBearing = 0.0f;
            this.mFinalBearing = 0.0f;
        }
    }
}
