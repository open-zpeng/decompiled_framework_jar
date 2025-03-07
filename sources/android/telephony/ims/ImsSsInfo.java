package android.telephony.ims;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SystemApi
/* loaded from: classes2.dex */
public final class ImsSsInfo implements Parcelable {
    public static final int CLIR_OUTGOING_DEFAULT = 0;
    public static final int CLIR_OUTGOING_INVOCATION = 1;
    public static final int CLIR_OUTGOING_SUPPRESSION = 2;
    public static final int CLIR_STATUS_NOT_PROVISIONED = 0;
    public static final int CLIR_STATUS_PROVISIONED_PERMANENT = 1;
    public static final int CLIR_STATUS_TEMPORARILY_ALLOWED = 4;
    public static final int CLIR_STATUS_TEMPORARILY_RESTRICTED = 3;
    public static final int CLIR_STATUS_UNKNOWN = 2;
    public static final Parcelable.Creator<ImsSsInfo> CREATOR = new Parcelable.Creator<ImsSsInfo>() { // from class: android.telephony.ims.ImsSsInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ImsSsInfo createFromParcel(Parcel in) {
            return new ImsSsInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ImsSsInfo[] newArray(int size) {
            return new ImsSsInfo[size];
        }
    };
    public static final int DISABLED = 0;
    public static final int ENABLED = 1;
    public static final int NOT_REGISTERED = -1;
    public static final int SERVICE_NOT_PROVISIONED = 0;
    public static final int SERVICE_PROVISIONED = 1;
    public static final int SERVICE_PROVISIONING_UNKNOWN = -1;
    private int mClirInterrogationStatus;
    private int mClirOutgoingState;
    @UnsupportedAppUsage
    public String mIcbNum;
    public int mProvisionStatus;
    @UnsupportedAppUsage
    public int mStatus;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface ClirInterrogationStatus {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface ClirOutgoingState {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface ServiceProvisionStatus {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface ServiceStatus {
    }

    @UnsupportedAppUsage
    public ImsSsInfo() {
        this.mProvisionStatus = -1;
        this.mClirInterrogationStatus = 2;
        this.mClirOutgoingState = 0;
    }

    /* loaded from: classes2.dex */
    public static final class Builder {
        private final ImsSsInfo mImsSsInfo = new ImsSsInfo();

        public Builder(int status) {
            this.mImsSsInfo.mStatus = status;
        }

        public Builder setIncomingCommunicationBarringNumber(String number) {
            this.mImsSsInfo.mIcbNum = number;
            return this;
        }

        public Builder setProvisionStatus(int provisionStatus) {
            this.mImsSsInfo.mProvisionStatus = provisionStatus;
            return this;
        }

        public Builder setClirInterrogationStatus(int status) {
            this.mImsSsInfo.mClirInterrogationStatus = status;
            return this;
        }

        public Builder setClirOutgoingState(int state) {
            this.mImsSsInfo.mClirOutgoingState = state;
            return this;
        }

        public ImsSsInfo build() {
            return this.mImsSsInfo;
        }
    }

    @Deprecated
    public ImsSsInfo(int status, String icbNum) {
        this.mProvisionStatus = -1;
        this.mClirInterrogationStatus = 2;
        this.mClirOutgoingState = 0;
        this.mStatus = status;
        this.mIcbNum = icbNum;
    }

    private ImsSsInfo(Parcel in) {
        this.mProvisionStatus = -1;
        this.mClirInterrogationStatus = 2;
        this.mClirOutgoingState = 0;
        readFromParcel(in);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mStatus);
        out.writeString(this.mIcbNum);
        out.writeInt(this.mProvisionStatus);
        out.writeInt(this.mClirInterrogationStatus);
        out.writeInt(this.mClirOutgoingState);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(", Status: ");
        sb.append(this.mStatus == 0 ? "disabled" : "enabled");
        sb.append(", ProvisionStatus: ");
        sb.append(provisionStatusToString(this.mProvisionStatus));
        return sb.toString();
    }

    private static String provisionStatusToString(int pStatus) {
        if (pStatus != 0) {
            if (pStatus == 1) {
                return "Service provisioned";
            }
            return "Service provisioning unknown";
        }
        return "Service not provisioned";
    }

    private void readFromParcel(Parcel in) {
        this.mStatus = in.readInt();
        this.mIcbNum = in.readString();
        this.mProvisionStatus = in.readInt();
        this.mClirInterrogationStatus = in.readInt();
        this.mClirOutgoingState = in.readInt();
    }

    public int getStatus() {
        return this.mStatus;
    }

    @Deprecated
    public String getIcbNum() {
        return this.mIcbNum;
    }

    public String getIncomingCommunicationBarringNumber() {
        return this.mIcbNum;
    }

    public int getProvisionStatus() {
        return this.mProvisionStatus;
    }

    public int getClirOutgoingState() {
        return this.mClirOutgoingState;
    }

    public int getClirInterrogationStatus() {
        return this.mClirInterrogationStatus;
    }
}
