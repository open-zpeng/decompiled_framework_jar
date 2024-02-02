package android.app.timezone;

import android.os.Parcel;
import android.os.Parcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes.dex */
public final class RulesState implements Parcelable {
    public protected static final byte BYTE_FALSE = 0;
    public protected static final byte BYTE_TRUE = 1;
    private protected static final Parcelable.Creator<RulesState> CREATOR = new Parcelable.Creator<RulesState>() { // from class: android.app.timezone.RulesState.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RulesState createFromParcel(Parcel in) {
            return RulesState.createFromParcel(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RulesState[] newArray(int size) {
            return new RulesState[size];
        }
    };
    private protected static final int DISTRO_STATUS_INSTALLED = 2;
    private protected static final int DISTRO_STATUS_NONE = 1;
    private protected static final int DISTRO_STATUS_UNKNOWN = 0;
    private protected static final int STAGED_OPERATION_INSTALL = 3;
    private protected static final int STAGED_OPERATION_NONE = 1;
    private protected static final int STAGED_OPERATION_UNINSTALL = 2;
    private protected static final int STAGED_OPERATION_UNKNOWN = 0;
    public protected final DistroFormatVersion mDistroFormatVersionSupported;
    public protected final int mDistroStatus;
    public protected final DistroRulesVersion mInstalledDistroRulesVersion;
    public protected final boolean mOperationInProgress;
    public protected final DistroRulesVersion mStagedDistroRulesVersion;
    public protected final int mStagedOperationType;
    public protected final String mSystemRulesVersion;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    private @interface DistroStatus {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    private @interface StagedOperationType {
    }

    private protected synchronized RulesState(String systemRulesVersion, DistroFormatVersion distroFormatVersionSupported, boolean operationInProgress, int stagedOperationType, DistroRulesVersion stagedDistroRulesVersion, int distroStatus, DistroRulesVersion installedDistroRulesVersion) {
        this.mSystemRulesVersion = Utils.validateRulesVersion("systemRulesVersion", systemRulesVersion);
        this.mDistroFormatVersionSupported = (DistroFormatVersion) Utils.validateNotNull("distroFormatVersionSupported", distroFormatVersionSupported);
        this.mOperationInProgress = operationInProgress;
        if (operationInProgress && stagedOperationType != 0) {
            throw new IllegalArgumentException("stagedOperationType != STAGED_OPERATION_UNKNOWN");
        }
        this.mStagedOperationType = validateStagedOperation(stagedOperationType);
        this.mStagedDistroRulesVersion = (DistroRulesVersion) Utils.validateConditionalNull(this.mStagedOperationType == 3, "stagedDistroRulesVersion", stagedDistroRulesVersion);
        this.mDistroStatus = validateDistroStatus(distroStatus);
        this.mInstalledDistroRulesVersion = (DistroRulesVersion) Utils.validateConditionalNull(this.mDistroStatus == 2, "installedDistroRulesVersion", installedDistroRulesVersion);
    }

    private protected synchronized String getSystemRulesVersion() {
        return this.mSystemRulesVersion;
    }

    private protected synchronized boolean isOperationInProgress() {
        return this.mOperationInProgress;
    }

    private protected synchronized int getStagedOperationType() {
        return this.mStagedOperationType;
    }

    private protected synchronized DistroRulesVersion getStagedDistroRulesVersion() {
        return this.mStagedDistroRulesVersion;
    }

    private protected synchronized int getDistroStatus() {
        return this.mDistroStatus;
    }

    private protected synchronized DistroRulesVersion getInstalledDistroRulesVersion() {
        return this.mInstalledDistroRulesVersion;
    }

    private protected synchronized boolean isDistroFormatVersionSupported(DistroFormatVersion distroFormatVersion) {
        return this.mDistroFormatVersionSupported.supports(distroFormatVersion);
    }

    private protected synchronized boolean isSystemVersionNewerThan(DistroRulesVersion distroRulesVersion) {
        return this.mSystemRulesVersion.compareTo(distroRulesVersion.getRulesVersion()) > 0;
    }

    /* JADX INFO: Access modifiers changed from: public */
    public static synchronized RulesState createFromParcel(Parcel in) {
        String systemRulesVersion = in.readString();
        DistroFormatVersion distroFormatVersionSupported = (DistroFormatVersion) in.readParcelable(null);
        boolean operationInProgress = in.readByte() == 1;
        int distroStagedState = in.readByte();
        DistroRulesVersion stagedDistroRulesVersion = (DistroRulesVersion) in.readParcelable(null);
        int installedDistroStatus = in.readByte();
        DistroRulesVersion installedDistroRulesVersion = (DistroRulesVersion) in.readParcelable(null);
        return new RulesState(systemRulesVersion, distroFormatVersionSupported, operationInProgress, distroStagedState, stagedDistroRulesVersion, installedDistroStatus, installedDistroRulesVersion);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.mSystemRulesVersion);
        out.writeParcelable(this.mDistroFormatVersionSupported, 0);
        out.writeByte(this.mOperationInProgress ? (byte) 1 : (byte) 0);
        out.writeByte((byte) this.mStagedOperationType);
        out.writeParcelable(this.mStagedDistroRulesVersion, 0);
        out.writeByte((byte) this.mDistroStatus);
        out.writeParcelable(this.mInstalledDistroRulesVersion, 0);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RulesState that = (RulesState) o;
        if (this.mOperationInProgress != that.mOperationInProgress || this.mStagedOperationType != that.mStagedOperationType || this.mDistroStatus != that.mDistroStatus || !this.mSystemRulesVersion.equals(that.mSystemRulesVersion) || !this.mDistroFormatVersionSupported.equals(that.mDistroFormatVersionSupported)) {
            return false;
        }
        if (this.mStagedDistroRulesVersion == null ? that.mStagedDistroRulesVersion != null : !this.mStagedDistroRulesVersion.equals(that.mStagedDistroRulesVersion)) {
            return false;
        }
        if (this.mInstalledDistroRulesVersion != null) {
            return this.mInstalledDistroRulesVersion.equals(that.mInstalledDistroRulesVersion);
        }
        if (that.mInstalledDistroRulesVersion == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int i;
        int result = this.mSystemRulesVersion.hashCode();
        int result2 = 31 * ((31 * ((31 * ((31 * result) + this.mDistroFormatVersionSupported.hashCode())) + (this.mOperationInProgress ? 1 : 0))) + this.mStagedOperationType);
        if (this.mStagedDistroRulesVersion != null) {
            i = this.mStagedDistroRulesVersion.hashCode();
        } else {
            i = 0;
        }
        return (31 * ((31 * (result2 + i)) + this.mDistroStatus)) + (this.mInstalledDistroRulesVersion != null ? this.mInstalledDistroRulesVersion.hashCode() : 0);
    }

    public String toString() {
        return "RulesState{mSystemRulesVersion='" + this.mSystemRulesVersion + "', mDistroFormatVersionSupported=" + this.mDistroFormatVersionSupported + ", mOperationInProgress=" + this.mOperationInProgress + ", mStagedOperationType=" + this.mStagedOperationType + ", mStagedDistroRulesVersion=" + this.mStagedDistroRulesVersion + ", mDistroStatus=" + this.mDistroStatus + ", mInstalledDistroRulesVersion=" + this.mInstalledDistroRulesVersion + '}';
    }

    public protected static synchronized int validateStagedOperation(int stagedOperationType) {
        if (stagedOperationType < 0 || stagedOperationType > 3) {
            throw new IllegalArgumentException("Unknown operation type=" + stagedOperationType);
        }
        return stagedOperationType;
    }

    public protected static synchronized int validateDistroStatus(int distroStatus) {
        if (distroStatus < 0 || distroStatus > 2) {
            throw new IllegalArgumentException("Unknown distro status=" + distroStatus);
        }
        return distroStatus;
    }
}
