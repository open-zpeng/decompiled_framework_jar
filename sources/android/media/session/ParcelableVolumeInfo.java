package android.media.session;

import android.media.AudioAttributes;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public class ParcelableVolumeInfo implements Parcelable {
    public static final Parcelable.Creator<ParcelableVolumeInfo> CREATOR = new Parcelable.Creator<ParcelableVolumeInfo>() { // from class: android.media.session.ParcelableVolumeInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ParcelableVolumeInfo createFromParcel(Parcel in) {
            return new ParcelableVolumeInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ParcelableVolumeInfo[] newArray(int size) {
            return new ParcelableVolumeInfo[size];
        }
    };
    public AudioAttributes audioAttrs;
    public int controlType;
    public int currentVolume;
    public int maxVolume;
    public int volumeType;

    public synchronized ParcelableVolumeInfo(int volumeType, AudioAttributes audioAttrs, int controlType, int maxVolume, int currentVolume) {
        this.volumeType = volumeType;
        this.audioAttrs = audioAttrs;
        this.controlType = controlType;
        this.maxVolume = maxVolume;
        this.currentVolume = currentVolume;
    }

    public synchronized ParcelableVolumeInfo(Parcel from) {
        this.volumeType = from.readInt();
        this.controlType = from.readInt();
        this.maxVolume = from.readInt();
        this.currentVolume = from.readInt();
        this.audioAttrs = AudioAttributes.CREATOR.createFromParcel(from);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.volumeType);
        dest.writeInt(this.controlType);
        dest.writeInt(this.maxVolume);
        dest.writeInt(this.currentVolume);
        this.audioAttrs.writeToParcel(dest, flags);
    }
}
