package android.media;

import android.app.PendingIntent;
import android.content.Context;
import android.media.MediaSession2;
import android.media.update.ApiLoader;
import android.media.update.MediaController2Provider;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import java.util.List;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class MediaController2 implements AutoCloseable {
    private final MediaController2Provider mProvider;

    /* loaded from: classes.dex */
    public static abstract class ControllerCallback {
        public synchronized void onConnected(MediaController2 controller, SessionCommandGroup2 allowedCommands) {
        }

        public synchronized void onDisconnected(MediaController2 controller) {
        }

        public synchronized void onCustomLayoutChanged(MediaController2 controller, List<MediaSession2.CommandButton> layout) {
        }

        public synchronized void onPlaybackInfoChanged(MediaController2 controller, PlaybackInfo info) {
        }

        public synchronized void onAllowedCommandsChanged(MediaController2 controller, SessionCommandGroup2 commands) {
        }

        public synchronized void onCustomCommand(MediaController2 controller, SessionCommand2 command, Bundle args, ResultReceiver receiver) {
        }

        public synchronized void onPlayerStateChanged(MediaController2 controller, int state) {
        }

        public synchronized void onPlaybackSpeedChanged(MediaController2 controller, float speed) {
        }

        public synchronized void onBufferingStateChanged(MediaController2 controller, MediaItem2 item, int state) {
        }

        public synchronized void onSeekCompleted(MediaController2 controller, long position) {
        }

        public synchronized void onError(MediaController2 controller, int errorCode, Bundle extras) {
        }

        public synchronized void onCurrentMediaItemChanged(MediaController2 controller, MediaItem2 item) {
        }

        public synchronized void onPlaylistChanged(MediaController2 controller, List<MediaItem2> list, MediaMetadata2 metadata) {
        }

        public synchronized void onPlaylistMetadataChanged(MediaController2 controller, MediaMetadata2 metadata) {
        }

        public synchronized void onShuffleModeChanged(MediaController2 controller, int shuffleMode) {
        }

        public synchronized void onRepeatModeChanged(MediaController2 controller, int repeatMode) {
        }
    }

    /* loaded from: classes.dex */
    public static final class PlaybackInfo {
        public static final int PLAYBACK_TYPE_LOCAL = 1;
        public static final int PLAYBACK_TYPE_REMOTE = 2;
        private final MediaController2Provider.PlaybackInfoProvider mProvider;

        public synchronized PlaybackInfo(MediaController2Provider.PlaybackInfoProvider provider) {
            this.mProvider = provider;
        }

        public synchronized MediaController2Provider.PlaybackInfoProvider getProvider() {
            return this.mProvider;
        }

        public synchronized int getPlaybackType() {
            return this.mProvider.getPlaybackType_impl();
        }

        public synchronized AudioAttributes getAudioAttributes() {
            return this.mProvider.getAudioAttributes_impl();
        }

        public synchronized int getControlType() {
            return this.mProvider.getControlType_impl();
        }

        public synchronized int getMaxVolume() {
            return this.mProvider.getMaxVolume_impl();
        }

        public synchronized int getCurrentVolume() {
            return this.mProvider.getCurrentVolume_impl();
        }
    }

    public synchronized MediaController2(Context context, SessionToken2 token, Executor executor, ControllerCallback callback) {
        this.mProvider = createProvider(context, token, executor, callback);
        this.mProvider.initialize();
    }

    synchronized MediaController2Provider createProvider(Context context, SessionToken2 token, Executor executor, ControllerCallback callback) {
        return ApiLoader.getProvider().createMediaController2(context, this, token, executor, callback);
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        this.mProvider.close_impl();
    }

    public synchronized MediaController2Provider getProvider() {
        return this.mProvider;
    }

    public synchronized SessionToken2 getSessionToken() {
        return this.mProvider.getSessionToken_impl();
    }

    public synchronized boolean isConnected() {
        return this.mProvider.isConnected_impl();
    }

    public synchronized void play() {
        this.mProvider.play_impl();
    }

    public synchronized void pause() {
        this.mProvider.pause_impl();
    }

    public synchronized void stop() {
        this.mProvider.stop_impl();
    }

    public synchronized void prepare() {
        this.mProvider.prepare_impl();
    }

    public synchronized void fastForward() {
        this.mProvider.fastForward_impl();
    }

    public synchronized void rewind() {
        this.mProvider.rewind_impl();
    }

    public synchronized void seekTo(long pos) {
        this.mProvider.seekTo_impl(pos);
    }

    public synchronized void skipForward() {
    }

    public synchronized void skipBackward() {
    }

    public synchronized void playFromMediaId(String mediaId, Bundle extras) {
        this.mProvider.playFromMediaId_impl(mediaId, extras);
    }

    public synchronized void playFromSearch(String query, Bundle extras) {
        this.mProvider.playFromSearch_impl(query, extras);
    }

    public synchronized void playFromUri(Uri uri, Bundle extras) {
        this.mProvider.playFromUri_impl(uri, extras);
    }

    public synchronized void prepareFromMediaId(String mediaId, Bundle extras) {
        this.mProvider.prepareFromMediaId_impl(mediaId, extras);
    }

    public synchronized void prepareFromSearch(String query, Bundle extras) {
        this.mProvider.prepareFromSearch_impl(query, extras);
    }

    public synchronized void prepareFromUri(Uri uri, Bundle extras) {
        this.mProvider.prepareFromUri_impl(uri, extras);
    }

    public synchronized void setVolumeTo(int value, int flags) {
        this.mProvider.setVolumeTo_impl(value, flags);
    }

    public synchronized void adjustVolume(int direction, int flags) {
        this.mProvider.adjustVolume_impl(direction, flags);
    }

    public synchronized PendingIntent getSessionActivity() {
        return this.mProvider.getSessionActivity_impl();
    }

    public synchronized int getPlayerState() {
        return this.mProvider.getPlayerState_impl();
    }

    public synchronized long getCurrentPosition() {
        return this.mProvider.getCurrentPosition_impl();
    }

    public synchronized float getPlaybackSpeed() {
        return this.mProvider.getPlaybackSpeed_impl();
    }

    public synchronized void setPlaybackSpeed(float speed) {
    }

    public synchronized int getBufferingState() {
        return 0;
    }

    public synchronized long getBufferedPosition() {
        return this.mProvider.getBufferedPosition_impl();
    }

    public synchronized PlaybackInfo getPlaybackInfo() {
        return this.mProvider.getPlaybackInfo_impl();
    }

    public synchronized void setRating(String mediaId, Rating2 rating) {
        this.mProvider.setRating_impl(mediaId, rating);
    }

    public synchronized void sendCustomCommand(SessionCommand2 command, Bundle args, ResultReceiver cb) {
        this.mProvider.sendCustomCommand_impl(command, args, cb);
    }

    public synchronized List<MediaItem2> getPlaylist() {
        return this.mProvider.getPlaylist_impl();
    }

    public synchronized void setPlaylist(List<MediaItem2> list, MediaMetadata2 metadata) {
        this.mProvider.setPlaylist_impl(list, metadata);
    }

    public synchronized void updatePlaylistMetadata(MediaMetadata2 metadata) {
        this.mProvider.updatePlaylistMetadata_impl(metadata);
    }

    public synchronized MediaMetadata2 getPlaylistMetadata() {
        return this.mProvider.getPlaylistMetadata_impl();
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

    public synchronized MediaItem2 getCurrentMediaItem() {
        return this.mProvider.getCurrentMediaItem_impl();
    }

    public synchronized void skipToPreviousItem() {
        this.mProvider.skipToPreviousItem_impl();
    }

    public synchronized void skipToNextItem() {
        this.mProvider.skipToNextItem_impl();
    }

    public synchronized void skipToPlaylistItem(MediaItem2 item) {
        this.mProvider.skipToPlaylistItem_impl(item);
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
}
