package android.os;

import android.os.Parcelable;
import android.util.MathUtils;
/* loaded from: classes2.dex */
public class ParcelableParcel implements Parcelable {
    private protected static final Parcelable.ClassLoaderCreator<ParcelableParcel> CREATOR = new Parcelable.ClassLoaderCreator<ParcelableParcel>() { // from class: android.os.ParcelableParcel.1
        @Override // android.os.Parcelable.Creator
        public ParcelableParcel createFromParcel(Parcel in) {
            return new ParcelableParcel(in, null);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.ClassLoaderCreator
        public ParcelableParcel createFromParcel(Parcel in, ClassLoader loader) {
            return new ParcelableParcel(in, loader);
        }

        @Override // android.os.Parcelable.Creator
        public ParcelableParcel[] newArray(int size) {
            return new ParcelableParcel[size];
        }
    };
    final ClassLoader mClassLoader;
    final Parcel mParcel = Parcel.obtain();

    /* JADX INFO: Access modifiers changed from: private */
    public ParcelableParcel(ClassLoader loader) {
        this.mClassLoader = loader;
    }

    public synchronized ParcelableParcel(Parcel src, ClassLoader loader) {
        this.mClassLoader = loader;
        int size = src.readInt();
        if (size < 0) {
            throw new IllegalArgumentException("Negative size read from parcel");
        }
        int pos = src.dataPosition();
        src.setDataPosition(MathUtils.addOrThrow(pos, size));
        this.mParcel.appendFrom(src, pos, size);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Parcel getParcel() {
        this.mParcel.setDataPosition(0);
        return this.mParcel;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ClassLoader getClassLoader() {
        return this.mClassLoader;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mParcel.dataSize());
        dest.appendFrom(this.mParcel, 0, this.mParcel.dataSize());
    }
}
