package android.location;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public final class Geofence implements Parcelable {
    @UnsupportedAppUsage
    public static final Parcelable.Creator<Geofence> CREATOR = new Parcelable.Creator<Geofence>() { // from class: android.location.Geofence.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Geofence createFromParcel(Parcel in) {
            int type = in.readInt();
            double latitude = in.readDouble();
            double longitude = in.readDouble();
            float radius = in.readFloat();
            Geofence.checkType(type);
            return Geofence.createCircle(latitude, longitude, radius);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Geofence[] newArray(int size) {
            return new Geofence[size];
        }
    };
    public static final int TYPE_HORIZONTAL_CIRCLE = 1;
    private final double mLatitude;
    private final double mLongitude;
    private final float mRadius;
    private final int mType;

    public static Geofence createCircle(double latitude, double longitude, float radius) {
        return new Geofence(latitude, longitude, radius);
    }

    private Geofence(double latitude, double longitude, float radius) {
        checkRadius(radius);
        checkLatLong(latitude, longitude);
        this.mType = 1;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mRadius = radius;
    }

    public int getType() {
        return this.mType;
    }

    public double getLatitude() {
        return this.mLatitude;
    }

    public double getLongitude() {
        return this.mLongitude;
    }

    public float getRadius() {
        return this.mRadius;
    }

    private static void checkRadius(float radius) {
        if (radius <= 0.0f) {
            throw new IllegalArgumentException("invalid radius: " + radius);
        }
    }

    private static void checkLatLong(double latitude, double longitude) {
        if (latitude > 90.0d || latitude < -90.0d) {
            throw new IllegalArgumentException("invalid latitude: " + latitude);
        } else if (longitude > 180.0d || longitude < -180.0d) {
            throw new IllegalArgumentException("invalid longitude: " + longitude);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void checkType(int type) {
        if (type != 1) {
            throw new IllegalArgumentException("invalid type: " + type);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.mType);
        parcel.writeDouble(this.mLatitude);
        parcel.writeDouble(this.mLongitude);
        parcel.writeFloat(this.mRadius);
    }

    private static String typeToString(int type) {
        if (type == 1) {
            return "CIRCLE";
        }
        checkType(type);
        return null;
    }

    public String toString() {
        return String.format("Geofence[%s %.6f, %.6f %.0fm]", typeToString(this.mType), Double.valueOf(this.mLatitude), Double.valueOf(this.mLongitude), Float.valueOf(this.mRadius));
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.mLatitude);
        int result = (1 * 31) + ((int) ((temp >>> 32) ^ temp));
        long temp2 = Double.doubleToLongBits(this.mLongitude);
        return (((((result * 31) + ((int) ((temp2 >>> 32) ^ temp2))) * 31) + Float.floatToIntBits(this.mRadius)) * 31) + this.mType;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Geofence)) {
            return false;
        }
        Geofence other = (Geofence) obj;
        if (this.mRadius == other.mRadius && this.mLatitude == other.mLatitude && this.mLongitude == other.mLongitude && this.mType == other.mType) {
            return true;
        }
        return false;
    }
}
