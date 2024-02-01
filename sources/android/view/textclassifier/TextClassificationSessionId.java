package android.view.textclassifier;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.util.Preconditions;
import java.util.Locale;
import java.util.UUID;
/* loaded from: classes2.dex */
public final class TextClassificationSessionId implements Parcelable {
    public static final Parcelable.Creator<TextClassificationSessionId> CREATOR = new Parcelable.Creator<TextClassificationSessionId>() { // from class: android.view.textclassifier.TextClassificationSessionId.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TextClassificationSessionId createFromParcel(Parcel parcel) {
            return new TextClassificationSessionId((String) Preconditions.checkNotNull(parcel.readString()));
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TextClassificationSessionId[] newArray(int size) {
            return new TextClassificationSessionId[size];
        }
    };
    private final String mValue;

    public synchronized TextClassificationSessionId() {
        this(UUID.randomUUID().toString());
    }

    public synchronized TextClassificationSessionId(String value) {
        this.mValue = value;
    }

    public int hashCode() {
        int result = (31 * 1) + this.mValue.hashCode();
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        TextClassificationSessionId other = (TextClassificationSessionId) obj;
        if (this.mValue.equals(other.mValue)) {
            return true;
        }
        return false;
    }

    public String toString() {
        return String.format(Locale.US, "TextClassificationSessionId {%s}", this.mValue);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.mValue);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public synchronized String flattenToString() {
        return this.mValue;
    }

    public static synchronized TextClassificationSessionId unflattenFromString(String string) {
        return new TextClassificationSessionId(string);
    }
}
