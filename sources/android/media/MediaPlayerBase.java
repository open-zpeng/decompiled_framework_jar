package android.media;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public abstract class MediaPlayerBase implements AutoCloseable {
    public static final int BUFFERING_STATE_BUFFERING_AND_PLAYABLE = 1;
    public static final int BUFFERING_STATE_BUFFERING_AND_STARVED = 2;
    public static final int BUFFERING_STATE_BUFFERING_COMPLETE = 3;
    public static final int BUFFERING_STATE_UNKNOWN = 0;
    public static final int PLAYER_STATE_ERROR = 3;
    public static final int PLAYER_STATE_IDLE = 0;
    public static final int PLAYER_STATE_PAUSED = 1;
    public static final int PLAYER_STATE_PLAYING = 2;
    public static final long UNKNOWN_TIME = -1;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface BuffState {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface PlayerState {
    }

    public abstract synchronized AudioAttributes getAudioAttributes();

    public abstract synchronized int getBufferingState();

    public abstract synchronized DataSourceDesc getCurrentDataSource();

    public abstract synchronized int getPlayerState();

    public abstract synchronized float getPlayerVolume();

    public abstract synchronized void loopCurrent(boolean z);

    public abstract synchronized void pause();

    public abstract synchronized void play();

    public abstract synchronized void prepare();

    public abstract synchronized void registerPlayerEventCallback(Executor executor, PlayerEventCallback playerEventCallback);

    public abstract synchronized void reset();

    public abstract synchronized void seekTo(long j);

    public abstract synchronized void setAudioAttributes(AudioAttributes audioAttributes);

    public abstract synchronized void setDataSource(DataSourceDesc dataSourceDesc);

    public abstract synchronized void setNextDataSource(DataSourceDesc dataSourceDesc);

    public abstract synchronized void setNextDataSources(List<DataSourceDesc> list);

    public abstract synchronized void setPlaybackSpeed(float f);

    public abstract synchronized void setPlayerVolume(float f);

    public abstract synchronized void skipToNext();

    public abstract synchronized void unregisterPlayerEventCallback(PlayerEventCallback playerEventCallback);

    public synchronized long getCurrentPosition() {
        return -1L;
    }

    public synchronized long getDuration() {
        return -1L;
    }

    public synchronized long getBufferedPosition() {
        return -1L;
    }

    public synchronized float getPlaybackSpeed() {
        return 1.0f;
    }

    public synchronized boolean isReversePlaybackSupported() {
        return false;
    }

    public synchronized float getMaxPlayerVolume() {
        return 1.0f;
    }

    /* loaded from: classes.dex */
    public static abstract class PlayerEventCallback {
        public synchronized void onCurrentDataSourceChanged(MediaPlayerBase mpb, DataSourceDesc dsd) {
        }

        public synchronized void onMediaPrepared(MediaPlayerBase mpb, DataSourceDesc dsd) {
        }

        public synchronized void onPlayerStateChanged(MediaPlayerBase mpb, int state) {
        }

        public synchronized void onBufferingStateChanged(MediaPlayerBase mpb, DataSourceDesc dsd, int state) {
        }

        public synchronized void onPlaybackSpeedChanged(MediaPlayerBase mpb, float speed) {
        }

        public synchronized void onSeekCompleted(MediaPlayerBase mpb, long position) {
        }
    }
}
