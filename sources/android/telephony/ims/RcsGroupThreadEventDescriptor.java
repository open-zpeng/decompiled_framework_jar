package android.telephony.ims;

import android.os.Parcel;

/* loaded from: classes2.dex */
public abstract class RcsGroupThreadEventDescriptor extends RcsEventDescriptor {
    protected final int mOriginatingParticipantId;
    protected final int mRcsGroupThreadId;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RcsGroupThreadEventDescriptor(long timestamp, int rcsGroupThreadId, int originatingParticipantId) {
        super(timestamp);
        this.mRcsGroupThreadId = rcsGroupThreadId;
        this.mOriginatingParticipantId = originatingParticipantId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RcsGroupThreadEventDescriptor(Parcel in) {
        super(in);
        this.mRcsGroupThreadId = in.readInt();
        this.mOriginatingParticipantId = in.readInt();
    }

    @Override // android.telephony.ims.RcsEventDescriptor, android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.mRcsGroupThreadId);
        dest.writeInt(this.mOriginatingParticipantId);
    }
}
