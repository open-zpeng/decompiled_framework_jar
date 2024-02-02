package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public class ParceledListSlice<T extends Parcelable> extends BaseParceledListSlice<T> {
    private protected static final Parcelable.ClassLoaderCreator<ParceledListSlice> CREATOR = new Parcelable.ClassLoaderCreator<ParceledListSlice>() { // from class: android.content.pm.ParceledListSlice.1
        @Override // android.os.Parcelable.Creator
        public ParceledListSlice createFromParcel(Parcel in) {
            return new ParceledListSlice(in, null);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.ClassLoaderCreator
        public ParceledListSlice createFromParcel(Parcel in, ClassLoader loader) {
            return new ParceledListSlice(in, loader);
        }

        @Override // android.os.Parcelable.Creator
        public ParceledListSlice[] newArray(int size) {
            return new ParceledListSlice[size];
        }
    };

    public /* bridge */ /* synthetic */ List getList() {
        return super.getList();
    }

    @Override // android.content.pm.BaseParceledListSlice
    public /* bridge */ /* synthetic */ void setInlineCountLimit(int i) {
        super.setInlineCountLimit(i);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.content.pm.BaseParceledListSlice
    protected /* bridge */ /* synthetic */ void writeElement(Object obj, Parcel parcel, int i) {
        writeElement((ParceledListSlice<T>) obj, parcel, i);
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected /* bridge */ /* synthetic */ void writeParcelableCreator(Object obj, Parcel parcel) {
        writeParcelableCreator((ParceledListSlice<T>) obj, parcel);
    }

    @Override // android.content.pm.BaseParceledListSlice, android.os.Parcelable
    public /* bridge */ /* synthetic */ void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ParceledListSlice(List<T> list) {
        super(list);
    }

    private synchronized ParceledListSlice(Parcel in, ClassLoader loader) {
        super(in, loader);
    }

    public static synchronized <T extends Parcelable> ParceledListSlice<T> emptyList() {
        return new ParceledListSlice<>(Collections.emptyList());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        int contents = 0;
        List<T> list = getList();
        for (int i = 0; i < list.size(); i++) {
            contents |= list.get(i).describeContents();
        }
        return contents;
    }

    protected synchronized void writeElement(T parcelable, Parcel dest, int callFlags) {
        parcelable.writeToParcel(dest, callFlags);
    }

    public private void writeParcelableCreator(T parcelable, Parcel dest) {
        dest.writeParcelableCreator(parcelable);
    }

    @Override // android.content.pm.BaseParceledListSlice
    protected synchronized Parcelable.Creator<?> readParcelableCreator(Parcel from, ClassLoader loader) {
        return from.readParcelableCreator(loader);
    }
}
