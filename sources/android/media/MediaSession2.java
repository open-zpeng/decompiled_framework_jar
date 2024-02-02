package android.media;

import android.app.PendingIntent;
import android.content.Context;
import android.media.MediaSession2;
import android.media.update.ApiLoader;
import android.media.update.MediaSession2Provider;
import android.media.update.ProviderCreator;
import android.net.Uri;
import android.os.Bundle;
import android.os.IInterface;
import android.os.ResultReceiver;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class MediaSession2 implements AutoCloseable {
    public static final int ERROR_CODE_ACTION_ABORTED = 10;
    public static final int ERROR_CODE_APP_ERROR = 1;
    public static final int ERROR_CODE_AUTHENTICATION_EXPIRED = 3;
    public static final int ERROR_CODE_CONCURRENT_STREAM_LIMIT = 5;
    public static final int ERROR_CODE_CONTENT_ALREADY_PLAYING = 8;
    public static final int ERROR_CODE_END_OF_QUEUE = 11;
    public static final int ERROR_CODE_NOT_AVAILABLE_IN_REGION = 7;
    public static final int ERROR_CODE_NOT_SUPPORTED = 2;
    public static final int ERROR_CODE_PARENTAL_CONTROL_RESTRICTED = 6;
    public static final int ERROR_CODE_PREMIUM_ACCOUNT_REQUIRED = 4;
    public static final int ERROR_CODE_SETUP_REQUIRED = 12;
    public static final int ERROR_CODE_SKIP_LIMIT_REACHED = 9;
    public static final int ERROR_CODE_UNKNOWN_ERROR = 0;
    private final MediaSession2Provider mProvider;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface ErrorCode {
    }

    /* loaded from: classes.dex */
    public interface OnDataSourceMissingHelper {
        synchronized DataSourceDesc onDataSourceMissing(MediaSession2 mediaSession2, MediaItem2 mediaItem2);
    }

    /* loaded from: classes.dex */
    public static abstract class SessionCallback {
        public synchronized SessionCommandGroup2 onConnect(MediaSession2 session, ControllerInfo controller) {
            SessionCommandGroup2 commands = new SessionCommandGroup2();
            commands.addAllPredefinedCommands();
            return commands;
        }

        public synchronized void onDisconnected(MediaSession2 session, ControllerInfo controller) {
        }

        public synchronized boolean onCommandRequest(MediaSession2 session, ControllerInfo controller, SessionCommand2 command) {
            return true;
        }

        public synchronized void onSetRating(MediaSession2 session, ControllerInfo controller, String mediaId, Rating2 rating) {
        }

        public synchronized void onCustomCommand(MediaSession2 session, ControllerInfo controller, SessionCommand2 customCommand, Bundle args, ResultReceiver cb) {
        }

        public synchronized void onPlayFromMediaId(MediaSession2 session, ControllerInfo controller, String mediaId, Bundle extras) {
        }

        public synchronized void onPlayFromSearch(MediaSession2 session, ControllerInfo controller, String query, Bundle extras) {
        }

        public synchronized void onPlayFromUri(MediaSession2 session, ControllerInfo controller, Uri uri, Bundle extras) {
        }

        public synchronized void onPrepareFromMediaId(MediaSession2 session, ControllerInfo controller, String mediaId, Bundle extras) {
        }

        public synchronized void onPrepareFromSearch(MediaSession2 session, ControllerInfo controller, String query, Bundle extras) {
        }

        public synchronized void onPrepareFromUri(MediaSession2 session, ControllerInfo controller, Uri uri, Bundle extras) {
        }

        public synchronized void onFastForward(MediaSession2 session) {
        }

        public synchronized void onRewind(MediaSession2 session) {
        }

        public synchronized void onCurrentMediaItemChanged(MediaSession2 session, MediaPlayerBase player, MediaItem2 item) {
        }

        public synchronized void onMediaPrepared(MediaSession2 session, MediaPlayerBase player, MediaItem2 item) {
        }

        public synchronized void onPlayerStateChanged(MediaSession2 session, MediaPlayerBase player, int state) {
        }

        public synchronized void onBufferingStateChanged(MediaSession2 session, MediaPlayerBase player, MediaItem2 item, int state) {
        }

        public synchronized void onPlaybackSpeedChanged(MediaSession2 session, MediaPlayerBase player, float speed) {
        }

        public synchronized void onSeekCompleted(MediaSession2 session, MediaPlayerBase mpb, long position) {
        }

        public synchronized void onPlaylistChanged(MediaSession2 session, MediaPlaylistAgent playlistAgent, List<MediaItem2> list, MediaMetadata2 metadata) {
        }

        public synchronized void onPlaylistMetadataChanged(MediaSession2 session, MediaPlaylistAgent playlistAgent, MediaMetadata2 metadata) {
        }

        public synchronized void onShuffleModeChanged(MediaSession2 session, MediaPlaylistAgent playlistAgent, int shuffleMode) {
        }

        public synchronized void onRepeatModeChanged(MediaSession2 session, MediaPlaylistAgent playlistAgent, int repeatMode) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static abstract class BuilderBase<T extends MediaSession2, U extends BuilderBase<T, U, C>, C extends SessionCallback> {
        private final MediaSession2Provider.BuilderBaseProvider<T, C> mProvider;

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized BuilderBase(ProviderCreator<BuilderBase<T, U, C>, MediaSession2Provider.BuilderBaseProvider<T, C>> creator) {
            this.mProvider = creator.createProvider(this);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized U setPlayer(MediaPlayerBase player) {
            this.mProvider.setPlayer_impl(player);
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized U setPlaylistAgent(MediaPlaylistAgent playlistAgent) {
            this.mProvider.setPlaylistAgent_impl(playlistAgent);
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized U setVolumeProvider(VolumeProvider2 volumeProvider) {
            this.mProvider.setVolumeProvider_impl(volumeProvider);
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized U setSessionActivity(PendingIntent pi) {
            this.mProvider.setSessionActivity_impl(pi);
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized U setId(String id) {
            this.mProvider.setId_impl(id);
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized U setSessionCallback(Executor executor, C callback) {
            this.mProvider.setSessionCallback_impl(executor, callback);
            return this;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized T build() {
            return this.mProvider.build_impl();
        }
    }

    /* loaded from: classes.dex */
    public static final class Builder extends BuilderBase<MediaSession2, Builder, SessionCallback> {
        public synchronized Builder(final Context context) {
            super(new ProviderCreator() { // from class: android.media.-$$Lambda$MediaSession2$Builder$oNoIFxcC0aQZ6LWPAiWUEmVRf6c
                public final Object createProvider(Object obj) {
                    MediaSession2Provider.BuilderBaseProvider createMediaSession2Builder;
                    createMediaSession2Builder = ApiLoader.getProvider().createMediaSession2Builder(Context.this, (MediaSession2.Builder) ((MediaSession2.BuilderBase) obj));
                    return createMediaSession2Builder;
                }
            });
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.media.MediaSession2.BuilderBase
        public synchronized Builder setPlayer(MediaPlayerBase player) {
            return (Builder) super.setPlayer(player);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.media.MediaSession2.BuilderBase
        public synchronized Builder setPlaylistAgent(MediaPlaylistAgent playlistAgent) {
            return (Builder) super.setPlaylistAgent(playlistAgent);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.media.MediaSession2.BuilderBase
        public synchronized Builder setVolumeProvider(VolumeProvider2 volumeProvider) {
            return (Builder) super.setVolumeProvider(volumeProvider);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.media.MediaSession2.BuilderBase
        public synchronized Builder setSessionActivity(PendingIntent pi) {
            return (Builder) super.setSessionActivity(pi);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.media.MediaSession2.BuilderBase
        public synchronized Builder setId(String id) {
            return (Builder) super.setId(id);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.media.MediaSession2.BuilderBase
        public synchronized Builder setSessionCallback(Executor executor, SessionCallback callback) {
            return (Builder) super.setSessionCallback(executor, (Executor) callback);
        }

        @Override // android.media.MediaSession2.BuilderBase
        public synchronized MediaSession2 build() {
            return super.build();
        }
    }

    /* loaded from: classes.dex */
    public static final class ControllerInfo {
        private final MediaSession2Provider.ControllerInfoProvider mProvider;

        public synchronized ControllerInfo(Context context, int uid, int pid, String packageName, IInterface callback) {
            this.mProvider = ApiLoader.getProvider().createMediaSession2ControllerInfo(context, this, uid, pid, packageName, callback);
        }

        public synchronized String getPackageName() {
            return this.mProvider.getPackageName_impl();
        }

        public synchronized int getUid() {
            return this.mProvider.getUid_impl();
        }

        public synchronized boolean isTrusted() {
            return this.mProvider.isTrusted_impl();
        }

        public synchronized MediaSession2Provider.ControllerInfoProvider getProvider() {
            return this.mProvider;
        }

        public int hashCode() {
            return this.mProvider.hashCode_impl();
        }

        public boolean equals(Object obj) {
            return this.mProvider.equals_impl(obj);
        }

        public String toString() {
            return this.mProvider.toString_impl();
        }
    }

    /* loaded from: classes.dex */
    public static final class CommandButton {
        private final MediaSession2Provider.CommandButtonProvider mProvider;

        public synchronized CommandButton(MediaSession2Provider.CommandButtonProvider provider) {
            this.mProvider = provider;
        }

        public synchronized SessionCommand2 getCommand() {
            return this.mProvider.getCommand_impl();
        }

        public synchronized int getIconResId() {
            return this.mProvider.getIconResId_impl();
        }

        public synchronized String getDisplayName() {
            return this.mProvider.getDisplayName_impl();
        }

        public synchronized Bundle getExtras() {
            return this.mProvider.getExtras_impl();
        }

        public synchronized boolean isEnabled() {
            return this.mProvider.isEnabled_impl();
        }

        public synchronized MediaSession2Provider.CommandButtonProvider getProvider() {
            return this.mProvider;
        }

        /* loaded from: classes.dex */
        public static final class Builder {
            private final MediaSession2Provider.CommandButtonProvider.BuilderProvider mProvider = ApiLoader.getProvider().createMediaSession2CommandButtonBuilder(this);

            public synchronized Builder setCommand(SessionCommand2 command) {
                return this.mProvider.setCommand_impl(command);
            }

            public synchronized Builder setIconResId(int resId) {
                return this.mProvider.setIconResId_impl(resId);
            }

            public synchronized Builder setDisplayName(String displayName) {
                return this.mProvider.setDisplayName_impl(displayName);
            }

            public synchronized Builder setEnabled(boolean enabled) {
                return this.mProvider.setEnabled_impl(enabled);
            }

            public synchronized Builder setExtras(Bundle extras) {
                return this.mProvider.setExtras_impl(extras);
            }

            public synchronized CommandButton build() {
                return this.mProvider.build_impl();
            }
        }
    }

    public synchronized MediaSession2(MediaSession2Provider provider) {
        this.mProvider = provider;
    }

    public synchronized MediaSession2Provider getProvider() {
        return this.mProvider;
    }

    public synchronized void updatePlayer(MediaPlayerBase player, MediaPlaylistAgent playlistAgent, VolumeProvider2 volumeProvider) {
        this.mProvider.updatePlayer_impl(player, playlistAgent, volumeProvider);
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        this.mProvider.close_impl();
    }

    public synchronized MediaPlayerBase getPlayer() {
        return this.mProvider.getPlayer_impl();
    }

    public synchronized MediaPlaylistAgent getPlaylistAgent() {
        return this.mProvider.getPlaylistAgent_impl();
    }

    public synchronized VolumeProvider2 getVolumeProvider() {
        return this.mProvider.getVolumeProvider_impl();
    }

    public synchronized SessionToken2 getToken() {
        return this.mProvider.getToken_impl();
    }

    public synchronized List<ControllerInfo> getConnectedControllers() {
        return this.mProvider.getConnectedControllers_impl();
    }

    public synchronized void setAudioFocusRequest(AudioFocusRequest afr) {
    }

    public synchronized void setCustomLayout(ControllerInfo controller, List<CommandButton> layout) {
        this.mProvider.setCustomLayout_impl(controller, layout);
    }

    public synchronized void setAllowedCommands(ControllerInfo controller, SessionCommandGroup2 commands) {
        this.mProvider.setAllowedCommands_impl(controller, commands);
    }

    public synchronized void sendCustomCommand(SessionCommand2 command, Bundle args) {
        this.mProvider.sendCustomCommand_impl(command, args);
    }

    public synchronized void sendCustomCommand(ControllerInfo controller, SessionCommand2 command, Bundle args, ResultReceiver receiver) {
        this.mProvider.sendCustomCommand_impl(controller, command, args, receiver);
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

    public synchronized void seekTo(long pos) {
        this.mProvider.seekTo_impl(pos);
    }

    public synchronized void skipForward() {
    }

    public synchronized void skipBackward() {
    }

    public synchronized void notifyError(int errorCode, Bundle extras) {
        this.mProvider.notifyError_impl(errorCode, extras);
    }

    public synchronized int getPlayerState() {
        return this.mProvider.getPlayerState_impl();
    }

    public synchronized long getCurrentPosition() {
        return this.mProvider.getCurrentPosition_impl();
    }

    public synchronized long getBufferedPosition() {
        return this.mProvider.getBufferedPosition_impl();
    }

    public synchronized int getBufferingState() {
        return 0;
    }

    public synchronized float getPlaybackSpeed() {
        return -1.0f;
    }

    public synchronized void setPlaybackSpeed(float speed) {
    }

    public synchronized void setOnDataSourceMissingHelper(OnDataSourceMissingHelper helper) {
        this.mProvider.setOnDataSourceMissingHelper_impl(helper);
    }

    public synchronized void clearOnDataSourceMissingHelper() {
        this.mProvider.clearOnDataSourceMissingHelper_impl();
    }

    public synchronized List<MediaItem2> getPlaylist() {
        return this.mProvider.getPlaylist_impl();
    }

    public synchronized void setPlaylist(List<MediaItem2> list, MediaMetadata2 metadata) {
        this.mProvider.setPlaylist_impl(list, metadata);
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
        return this.mProvider.getCurrentPlaylistItem_impl();
    }

    public synchronized void updatePlaylistMetadata(MediaMetadata2 metadata) {
        this.mProvider.updatePlaylistMetadata_impl(metadata);
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
