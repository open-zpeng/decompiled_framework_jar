package android.media;

import android.media.update.ApiLoader;
import android.media.update.MediaPlaylistAgentProvider;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public abstract class MediaPlaylistAgent {
    public static final int REPEAT_MODE_ALL = 2;
    public static final int REPEAT_MODE_GROUP = 3;
    public static final int REPEAT_MODE_NONE = 0;
    public static final int REPEAT_MODE_ONE = 1;
    public static final int SHUFFLE_MODE_ALL = 1;
    public static final int SHUFFLE_MODE_GROUP = 2;
    public static final int SHUFFLE_MODE_NONE = 0;
    private final MediaPlaylistAgentProvider mProvider = ApiLoader.getProvider().createMediaPlaylistAgent(this);

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface RepeatMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface ShuffleMode {
    }

    /* loaded from: classes.dex */
    public static abstract class PlaylistEventCallback {
        public synchronized void onPlaylistChanged(MediaPlaylistAgent playlistAgent, List<MediaItem2> list, MediaMetadata2 metadata) {
        }

        public synchronized void onPlaylistMetadataChanged(MediaPlaylistAgent playlistAgent, MediaMetadata2 metadata) {
        }

        public synchronized void onShuffleModeChanged(MediaPlaylistAgent playlistAgent, int shuffleMode) {
        }

        public synchronized void onRepeatModeChanged(MediaPlaylistAgent playlistAgent, int repeatMode) {
        }
    }

    public final synchronized void registerPlaylistEventCallback(Executor executor, PlaylistEventCallback callback) {
        this.mProvider.registerPlaylistEventCallback_impl(executor, callback);
    }

    public final synchronized void unregisterPlaylistEventCallback(PlaylistEventCallback callback) {
        this.mProvider.unregisterPlaylistEventCallback_impl(callback);
    }

    public final synchronized void notifyPlaylistChanged() {
        this.mProvider.notifyPlaylistChanged_impl();
    }

    public final synchronized void notifyPlaylistMetadataChanged() {
        this.mProvider.notifyPlaylistMetadataChanged_impl();
    }

    public final synchronized void notifyShuffleModeChanged() {
        this.mProvider.notifyShuffleModeChanged_impl();
    }

    public final synchronized void notifyRepeatModeChanged() {
        this.mProvider.notifyRepeatModeChanged_impl();
    }

    public synchronized List<MediaItem2> getPlaylist() {
        return this.mProvider.getPlaylist_impl();
    }

    public synchronized void setPlaylist(List<MediaItem2> list, MediaMetadata2 metadata) {
        this.mProvider.setPlaylist_impl(list, metadata);
    }

    public synchronized MediaMetadata2 getPlaylistMetadata() {
        return this.mProvider.getPlaylistMetadata_impl();
    }

    public synchronized void updatePlaylistMetadata(MediaMetadata2 metadata) {
        this.mProvider.updatePlaylistMetadata_impl(metadata);
    }

    public synchronized void addPlaylistItem(int index, MediaItem2 item) {
        this.mProvider.addPlaylistItem_impl(index, item);
    }

    public synchronized void removePlaylistItem(MediaItem2 item) {
        this.mProvider.removePlaylistItem_impl(item);
    }

    public synchronized void replacePlaylistItem(int index, MediaItem2 item) {
        this.mProvider.replacePlaylistItem_impl(index, item);
    }

    public synchronized void skipToPlaylistItem(MediaItem2 item) {
        this.mProvider.skipToPlaylistItem_impl(item);
    }

    public synchronized void skipToPreviousItem() {
        this.mProvider.skipToPreviousItem_impl();
    }

    public synchronized void skipToNextItem() {
        this.mProvider.skipToNextItem_impl();
    }

    public synchronized int getRepeatMode() {
        return this.mProvider.getRepeatMode_impl();
    }

    public synchronized void setRepeatMode(int repeatMode) {
        this.mProvider.setRepeatMode_impl(repeatMode);
    }

    public synchronized int getShuffleMode() {
        return this.mProvider.getShuffleMode_impl();
    }

    public synchronized void setShuffleMode(int shuffleMode) {
        this.mProvider.setShuffleMode_impl(shuffleMode);
    }

    public synchronized MediaItem2 getMediaItem(DataSourceDesc dsd) {
        return this.mProvider.getMediaItem_impl(dsd);
    }
}
