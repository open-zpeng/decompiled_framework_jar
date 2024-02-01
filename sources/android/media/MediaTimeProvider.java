package android.media;
/* loaded from: classes.dex */
public interface MediaTimeProvider {
    public static final long NO_TIME = -1;

    /* loaded from: classes.dex */
    public interface OnMediaTimeListener {
        synchronized void onSeek(long j);

        synchronized void onStop();

        synchronized void onTimedEvent(long j);
    }

    synchronized void cancelNotifications(OnMediaTimeListener onMediaTimeListener);

    synchronized long getCurrentTimeUs(boolean z, boolean z2) throws IllegalStateException;

    synchronized void notifyAt(long j, OnMediaTimeListener onMediaTimeListener);

    synchronized void scheduleUpdate(OnMediaTimeListener onMediaTimeListener);
}
