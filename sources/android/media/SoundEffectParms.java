package android.media;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public class SoundEffectParms implements Parcelable {
    public static final Parcelable.Creator<SoundEffectParms> CREATOR = new Parcelable.Creator<SoundEffectParms>() { // from class: android.media.SoundEffectParms.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SoundEffectParms createFromParcel(Parcel source) {
            return new SoundEffectParms(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SoundEffectParms[] newArray(int size) {
            return new SoundEffectParms[size];
        }
    };
    public int innervationValue;
    public int nativeValue;
    public int softValue;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.nativeValue);
        dest.writeInt(this.softValue);
        dest.writeInt(this.innervationValue);
    }

    public SoundEffectParms(int nativeValue, int softValue, int innervationValue) {
        this.nativeValue = nativeValue;
        this.softValue = softValue;
        this.innervationValue = innervationValue;
    }

    private SoundEffectParms(Parcel source) {
        readFromParcel(source);
    }

    public void readFromParcel(Parcel source) {
        this.nativeValue = source.readInt();
        this.softValue = source.readInt();
        this.innervationValue = source.readInt();
    }

    public String toString() {
        return "[" + this.nativeValue + ", " + this.softValue + ", " + this.innervationValue + "]";
    }
}
