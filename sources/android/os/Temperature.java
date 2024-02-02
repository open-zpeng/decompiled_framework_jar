package android.os;

import android.os.Parcelable;
/* loaded from: classes2.dex */
public class Temperature implements Parcelable {
    public static final Parcelable.Creator<Temperature> CREATOR = new Parcelable.Creator<Temperature>() { // from class: android.os.Temperature.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Temperature createFromParcel(Parcel p) {
            return new Temperature(p);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Temperature[] newArray(int size) {
            return new Temperature[size];
        }
    };
    private int mType;
    private float mValue;

    public synchronized Temperature() {
        this(-3.4028235E38f, Integer.MIN_VALUE);
    }

    public synchronized Temperature(float value, int type) {
        this.mValue = value;
        this.mType = type;
    }

    public synchronized float getValue() {
        return this.mValue;
    }

    public synchronized int getType() {
        return this.mType;
    }

    private synchronized Temperature(Parcel p) {
        readFromParcel(p);
    }

    public synchronized void readFromParcel(Parcel p) {
        this.mValue = p.readFloat();
        this.mType = p.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel p, int flags) {
        p.writeFloat(this.mValue);
        p.writeInt(this.mType);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
