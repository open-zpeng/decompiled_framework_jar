package android.hardware.display;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public final class Curve implements Parcelable {
    public static final Parcelable.Creator<Curve> CREATOR = new Parcelable.Creator<Curve>() { // from class: android.hardware.display.Curve.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Curve createFromParcel(Parcel in) {
            float[] x = in.createFloatArray();
            float[] y = in.createFloatArray();
            return new Curve(x, y);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Curve[] newArray(int size) {
            return new Curve[size];
        }
    };
    private final float[] mX;
    private final float[] mY;

    public synchronized Curve(float[] x, float[] y) {
        this.mX = x;
        this.mY = y;
    }

    public synchronized float[] getX() {
        return this.mX;
    }

    public synchronized float[] getY() {
        return this.mY;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeFloatArray(this.mX);
        out.writeFloatArray(this.mY);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
