package com.android.internal.telephony;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Telephony;

/* loaded from: classes3.dex */
public class OperatorInfo implements Parcelable {
    @UnsupportedAppUsage
    public static final Parcelable.Creator<OperatorInfo> CREATOR = new Parcelable.Creator<OperatorInfo>() { // from class: com.android.internal.telephony.OperatorInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OperatorInfo createFromParcel(Parcel in) {
            OperatorInfo opInfo = new OperatorInfo(in.readString(), in.readString(), in.readString(), (State) in.readSerializable());
            return opInfo;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OperatorInfo[] newArray(int size) {
            return new OperatorInfo[size];
        }
    };
    @UnsupportedAppUsage
    private String mOperatorAlphaLong;
    @UnsupportedAppUsage
    private String mOperatorAlphaShort;
    @UnsupportedAppUsage
    private String mOperatorNumeric;
    @UnsupportedAppUsage
    private State mState;

    /* loaded from: classes3.dex */
    public enum State {
        UNKNOWN,
        AVAILABLE,
        CURRENT,
        FORBIDDEN
    }

    @UnsupportedAppUsage
    public String getOperatorAlphaLong() {
        return this.mOperatorAlphaLong;
    }

    @UnsupportedAppUsage
    public String getOperatorAlphaShort() {
        return this.mOperatorAlphaShort;
    }

    @UnsupportedAppUsage
    public String getOperatorNumeric() {
        return this.mOperatorNumeric;
    }

    @UnsupportedAppUsage
    public State getState() {
        return this.mState;
    }

    @UnsupportedAppUsage
    OperatorInfo(String operatorAlphaLong, String operatorAlphaShort, String operatorNumeric, State state) {
        this.mState = State.UNKNOWN;
        this.mOperatorAlphaLong = operatorAlphaLong;
        this.mOperatorAlphaShort = operatorAlphaShort;
        this.mOperatorNumeric = operatorNumeric;
        this.mState = state;
    }

    @UnsupportedAppUsage
    public OperatorInfo(String operatorAlphaLong, String operatorAlphaShort, String operatorNumeric, String stateString) {
        this(operatorAlphaLong, operatorAlphaShort, operatorNumeric, rilStateToState(stateString));
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public OperatorInfo(String operatorAlphaLong, String operatorAlphaShort, String operatorNumeric) {
        this(operatorAlphaLong, operatorAlphaShort, operatorNumeric, State.UNKNOWN);
    }

    @UnsupportedAppUsage
    private static State rilStateToState(String s) {
        if (s.equals("unknown")) {
            return State.UNKNOWN;
        }
        if (s.equals("available")) {
            return State.AVAILABLE;
        }
        if (s.equals(Telephony.Carriers.CURRENT)) {
            return State.CURRENT;
        }
        if (s.equals("forbidden")) {
            return State.FORBIDDEN;
        }
        throw new RuntimeException("RIL impl error: Invalid network state '" + s + "'");
    }

    public String toString() {
        return "OperatorInfo " + this.mOperatorAlphaLong + "/" + this.mOperatorAlphaShort + "/" + this.mOperatorNumeric + "/" + this.mState;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mOperatorAlphaLong);
        dest.writeString(this.mOperatorAlphaShort);
        dest.writeString(this.mOperatorNumeric);
        dest.writeSerializable(this.mState);
    }
}
