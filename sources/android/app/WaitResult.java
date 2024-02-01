package android.app;

import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public class WaitResult implements Parcelable {
    public static final Parcelable.Creator<WaitResult> CREATOR = new Parcelable.Creator<WaitResult>() { // from class: android.app.WaitResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WaitResult createFromParcel(Parcel source) {
            return new WaitResult(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WaitResult[] newArray(int size) {
            return new WaitResult[size];
        }
    };
    public static final int INVALID_DELAY = -1;
    public int result;
    public boolean timeout;
    public long totalTime;
    public ComponentName who;

    public synchronized WaitResult() {
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.result);
        dest.writeInt(this.timeout ? 1 : 0);
        ComponentName.writeToParcel(this.who, dest);
        dest.writeLong(this.totalTime);
    }

    private synchronized WaitResult(Parcel source) {
        this.result = source.readInt();
        this.timeout = source.readInt() != 0;
        this.who = ComponentName.readFromParcel(source);
        this.totalTime = source.readLong();
    }

    public synchronized void dump(PrintWriter pw, String prefix) {
        pw.println(prefix + "WaitResult:");
        pw.println(prefix + "  result=" + this.result);
        pw.println(prefix + "  timeout=" + this.timeout);
        pw.println(prefix + "  who=" + this.who);
        pw.println(prefix + "  totalTime=" + this.totalTime);
    }
}
