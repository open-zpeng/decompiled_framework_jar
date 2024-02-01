package android.telephony.ims;

import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.ims.RcsMessageCreationParams;

/* loaded from: classes2.dex */
public final class RcsIncomingMessageCreationParams extends RcsMessageCreationParams implements Parcelable {
    public static final Parcelable.Creator<RcsIncomingMessageCreationParams> CREATOR = new Parcelable.Creator<RcsIncomingMessageCreationParams>() { // from class: android.telephony.ims.RcsIncomingMessageCreationParams.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RcsIncomingMessageCreationParams createFromParcel(Parcel in) {
            return new RcsIncomingMessageCreationParams(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RcsIncomingMessageCreationParams[] newArray(int size) {
            return new RcsIncomingMessageCreationParams[size];
        }
    };
    private final long mArrivalTimestamp;
    private final long mSeenTimestamp;
    private final int mSenderParticipantId;

    /* loaded from: classes2.dex */
    public static class Builder extends RcsMessageCreationParams.Builder {
        private long mArrivalTimestamp;
        private long mSeenTimestamp;
        private RcsParticipant mSenderParticipant;

        public Builder(long originationTimestamp, long arrivalTimestamp, int subscriptionId) {
            super(originationTimestamp, subscriptionId);
            this.mArrivalTimestamp = arrivalTimestamp;
        }

        public Builder setSenderParticipant(RcsParticipant senderParticipant) {
            this.mSenderParticipant = senderParticipant;
            return this;
        }

        public Builder setArrivalTimestamp(long arrivalTimestamp) {
            this.mArrivalTimestamp = arrivalTimestamp;
            return this;
        }

        public Builder setSeenTimestamp(long seenTimestamp) {
            this.mSeenTimestamp = seenTimestamp;
            return this;
        }

        @Override // android.telephony.ims.RcsMessageCreationParams.Builder
        public RcsIncomingMessageCreationParams build() {
            return new RcsIncomingMessageCreationParams(this);
        }
    }

    private RcsIncomingMessageCreationParams(Builder builder) {
        super(builder);
        this.mArrivalTimestamp = builder.mArrivalTimestamp;
        this.mSeenTimestamp = builder.mSeenTimestamp;
        this.mSenderParticipantId = builder.mSenderParticipant.getId();
    }

    private RcsIncomingMessageCreationParams(Parcel in) {
        super(in);
        this.mArrivalTimestamp = in.readLong();
        this.mSeenTimestamp = in.readLong();
        this.mSenderParticipantId = in.readInt();
    }

    public long getArrivalTimestamp() {
        return this.mArrivalTimestamp;
    }

    public long getSeenTimestamp() {
        return this.mSeenTimestamp;
    }

    public int getSenderParticipantId() {
        return this.mSenderParticipantId;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest);
        dest.writeLong(this.mArrivalTimestamp);
        dest.writeLong(this.mSeenTimestamp);
        dest.writeInt(this.mSenderParticipantId);
    }
}
