package android.net.lowpan;
/* loaded from: classes2.dex */
public class LowpanEnergyScanResult {
    private protected static final int UNKNOWN = Integer.MAX_VALUE;
    public protected int mChannel = Integer.MAX_VALUE;
    public protected int mMaxRssi = Integer.MAX_VALUE;

    private protected synchronized int getChannel() {
        return this.mChannel;
    }

    private protected synchronized int getMaxRssi() {
        return this.mMaxRssi;
    }

    public private protected synchronized void setChannel(int x) {
        this.mChannel = x;
    }

    public private protected synchronized void setMaxRssi(int x) {
        this.mMaxRssi = x;
    }

    public String toString() {
        return "LowpanEnergyScanResult(channel: " + this.mChannel + ", maxRssi:" + this.mMaxRssi + ")";
    }
}
