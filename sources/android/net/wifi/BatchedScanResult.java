package android.net.wifi;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;
@SystemApi
@Deprecated
/* loaded from: classes2.dex */
public class BatchedScanResult implements Parcelable {
    public static final Parcelable.Creator<BatchedScanResult> CREATOR = new Parcelable.Creator<BatchedScanResult>() { // from class: android.net.wifi.BatchedScanResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BatchedScanResult createFromParcel(Parcel in) {
            BatchedScanResult result = new BatchedScanResult();
            result.truncated = in.readInt() == 1;
            int count = in.readInt();
            while (true) {
                int count2 = count - 1;
                if (count > 0) {
                    result.scanResults.add(ScanResult.CREATOR.createFromParcel(in));
                    count = count2;
                } else {
                    return result;
                }
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public BatchedScanResult[] newArray(int size) {
            return new BatchedScanResult[size];
        }
    };
    private static final String TAG = "BatchedScanResult";
    private protected final List<ScanResult> scanResults = new ArrayList();
    private protected boolean truncated;

    private protected BatchedScanResult() {
    }

    private protected BatchedScanResult(BatchedScanResult source) {
        this.truncated = source.truncated;
        for (ScanResult s : source.scanResults) {
            this.scanResults.add(new ScanResult(s));
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("BatchedScanResult: ");
        sb.append("truncated: ");
        sb.append(String.valueOf(this.truncated));
        sb.append("scanResults: [");
        for (ScanResult s : this.scanResults) {
            sb.append(" <");
            sb.append(s.toString());
            sb.append("> ");
        }
        sb.append(" ]");
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.truncated ? 1 : 0);
        dest.writeInt(this.scanResults.size());
        for (ScanResult s : this.scanResults) {
            s.writeToParcel(dest, flags);
        }
    }
}
