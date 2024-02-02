package android.hardware.camera2.impl;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public class CaptureResultExtras implements Parcelable {
    public static final Parcelable.Creator<CaptureResultExtras> CREATOR = new Parcelable.Creator<CaptureResultExtras>() { // from class: android.hardware.camera2.impl.CaptureResultExtras.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CaptureResultExtras createFromParcel(Parcel in) {
            return new CaptureResultExtras(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CaptureResultExtras[] newArray(int size) {
            return new CaptureResultExtras[size];
        }
    };
    private int afTriggerId;
    private int errorStreamId;
    private long frameNumber;
    private int partialResultCount;
    private int precaptureTriggerId;
    private int requestId;
    private int subsequenceId;

    private synchronized CaptureResultExtras(Parcel in) {
        readFromParcel(in);
    }

    public synchronized CaptureResultExtras(int requestId, int subsequenceId, int afTriggerId, int precaptureTriggerId, long frameNumber, int partialResultCount, int errorStreamId) {
        this.requestId = requestId;
        this.subsequenceId = subsequenceId;
        this.afTriggerId = afTriggerId;
        this.precaptureTriggerId = precaptureTriggerId;
        this.frameNumber = frameNumber;
        this.partialResultCount = partialResultCount;
        this.errorStreamId = errorStreamId;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.requestId);
        dest.writeInt(this.subsequenceId);
        dest.writeInt(this.afTriggerId);
        dest.writeInt(this.precaptureTriggerId);
        dest.writeLong(this.frameNumber);
        dest.writeInt(this.partialResultCount);
        dest.writeInt(this.errorStreamId);
    }

    public synchronized void readFromParcel(Parcel in) {
        this.requestId = in.readInt();
        this.subsequenceId = in.readInt();
        this.afTriggerId = in.readInt();
        this.precaptureTriggerId = in.readInt();
        this.frameNumber = in.readLong();
        this.partialResultCount = in.readInt();
        this.errorStreamId = in.readInt();
    }

    public synchronized int getRequestId() {
        return this.requestId;
    }

    public synchronized int getSubsequenceId() {
        return this.subsequenceId;
    }

    public synchronized int getAfTriggerId() {
        return this.afTriggerId;
    }

    public synchronized int getPrecaptureTriggerId() {
        return this.precaptureTriggerId;
    }

    public synchronized long getFrameNumber() {
        return this.frameNumber;
    }

    public synchronized int getPartialResultCount() {
        return this.partialResultCount;
    }

    public synchronized int getErrorStreamId() {
        return this.errorStreamId;
    }
}
