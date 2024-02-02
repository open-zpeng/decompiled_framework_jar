package com.android.internal.telephony;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes3.dex */
public class DcParamObject implements Parcelable {
    public static final Parcelable.Creator<DcParamObject> CREATOR = new Parcelable.Creator<DcParamObject>() { // from class: com.android.internal.telephony.DcParamObject.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DcParamObject createFromParcel(Parcel in) {
            return new DcParamObject(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public DcParamObject[] newArray(int size) {
            return new DcParamObject[size];
        }
    };
    private int mSubId;

    public DcParamObject(int subId) {
        this.mSubId = subId;
    }

    public DcParamObject(Parcel in) {
        readFromParcel(in);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mSubId);
    }

    private void readFromParcel(Parcel in) {
        this.mSubId = in.readInt();
    }

    public int getSubId() {
        return this.mSubId;
    }
}
