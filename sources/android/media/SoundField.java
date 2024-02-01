package android.media;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public class SoundField implements Parcelable {
    public static final Parcelable.Creator<SoundField> CREATOR = new Parcelable.Creator<SoundField>() { // from class: android.media.SoundField.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SoundField createFromParcel(Parcel source) {
            return new SoundField(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SoundField[] newArray(int size) {
            return new SoundField[size];
        }
    };
    public int x;
    public int y;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.x);
        dest.writeInt(this.y);
    }

    public SoundField(int xSound, int ySound) {
        this.x = xSound;
        this.y = ySound;
    }

    private SoundField(Parcel source) {
        readFromParcel(source);
    }

    public void readFromParcel(Parcel source) {
        this.x = source.readInt();
        this.y = source.readInt();
    }

    public String toString() {
        return "[" + this.x + ", " + this.y + "]";
    }
}
