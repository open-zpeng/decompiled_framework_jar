package android.filterpacks.performance;
/* loaded from: classes.dex */
public class Throughput {
    public protected final int mPeriodFrames;
    public protected final int mPeriodTime;
    public protected final int mPixels;
    public protected final int mTotalFrames;

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized Throughput(int totalFrames, int periodFrames, int periodTime, int pixels) {
        this.mTotalFrames = totalFrames;
        this.mPeriodFrames = periodFrames;
        this.mPeriodTime = periodTime;
        this.mPixels = pixels;
    }

    private protected synchronized int getTotalFrameCount() {
        return this.mTotalFrames;
    }

    private protected synchronized int getPeriodFrameCount() {
        return this.mPeriodFrames;
    }

    private protected synchronized int getPeriodTime() {
        return this.mPeriodTime;
    }

    private protected synchronized float getFramesPerSecond() {
        return this.mPeriodFrames / this.mPeriodTime;
    }

    private protected synchronized float getNanosPerPixel() {
        double frameTimeInNanos = (this.mPeriodTime / this.mPeriodFrames) * 1000000.0d;
        return (float) (frameTimeInNanos / this.mPixels);
    }

    public String toString() {
        return getFramesPerSecond() + " FPS";
    }
}
