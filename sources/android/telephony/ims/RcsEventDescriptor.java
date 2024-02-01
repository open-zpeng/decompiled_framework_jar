package android.telephony.ims;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.annotations.VisibleForTesting;

/* loaded from: classes2.dex */
public abstract class RcsEventDescriptor implements Parcelable {
    protected final long mTimestamp;

    @VisibleForTesting(visibility = VisibleForTesting.Visibility.PROTECTED)
    public abstract RcsEvent createRcsEvent(RcsControllerCall rcsControllerCall);

    /* JADX INFO: Access modifiers changed from: package-private */
    public RcsEventDescriptor(long timestamp) {
        this.mTimestamp = timestamp;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RcsEventDescriptor(Parcel in) {
        this.mTimestamp = in.readLong();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mTimestamp);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
