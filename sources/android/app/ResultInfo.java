package android.app;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;
/* loaded from: classes.dex */
public class ResultInfo implements Parcelable {
    private protected static final Parcelable.Creator<ResultInfo> CREATOR = new Parcelable.Creator<ResultInfo>() { // from class: android.app.ResultInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ResultInfo createFromParcel(Parcel in) {
            return new ResultInfo(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ResultInfo[] newArray(int size) {
            return new ResultInfo[size];
        }
    };
    private protected final Intent mData;
    private protected final int mRequestCode;
    public final int mResultCode;
    private protected final String mResultWho;

    /* JADX INFO: Access modifiers changed from: private */
    public ResultInfo(String resultWho, int requestCode, int resultCode, Intent data) {
        this.mResultWho = resultWho;
        this.mRequestCode = requestCode;
        this.mResultCode = resultCode;
        this.mData = data;
    }

    public String toString() {
        return "ResultInfo{who=" + this.mResultWho + ", request=" + this.mRequestCode + ", result=" + this.mResultCode + ", data=" + this.mData + "}";
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.mResultWho);
        out.writeInt(this.mRequestCode);
        out.writeInt(this.mResultCode);
        if (this.mData != null) {
            out.writeInt(1);
            this.mData.writeToParcel(out, 0);
            return;
        }
        out.writeInt(0);
    }

    public synchronized ResultInfo(Parcel in) {
        this.mResultWho = in.readString();
        this.mRequestCode = in.readInt();
        this.mResultCode = in.readInt();
        if (in.readInt() != 0) {
            this.mData = Intent.CREATOR.createFromParcel(in);
        } else {
            this.mData = null;
        }
    }

    public boolean equals(Object obj) {
        boolean intentsEqual;
        if (obj == null || !(obj instanceof ResultInfo)) {
            return false;
        }
        ResultInfo other = (ResultInfo) obj;
        if (this.mData == null) {
            intentsEqual = other.mData == null;
        } else {
            intentsEqual = this.mData.filterEquals(other.mData);
        }
        return intentsEqual && Objects.equals(this.mResultWho, other.mResultWho) && this.mResultCode == other.mResultCode && this.mRequestCode == other.mRequestCode;
    }

    public int hashCode() {
        int result = (31 * ((31 * ((31 * 17) + this.mRequestCode)) + this.mResultCode)) + Objects.hashCode(this.mResultWho);
        if (this.mData != null) {
            return (31 * result) + this.mData.filterHashCode();
        }
        return result;
    }
}
