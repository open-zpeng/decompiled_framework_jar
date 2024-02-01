package android.media;

import android.content.Context;
import android.media.AudioRouting;
import android.media.BufferingParams;
import android.media.MediaDrm;
import android.media.MediaPlayerBase;
import android.media.SubtitleController;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcel;
import android.os.PersistableBundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public abstract class MediaPlayer2 extends MediaPlayerBase implements SubtitleController.Listener, AudioRouting {
    public static final boolean APPLY_METADATA_FILTER = true;
    public static final boolean BYPASS_METADATA_FILTER = false;
    public static final int CALL_COMPLETED_ATTACH_AUX_EFFECT = 1;
    public static final int CALL_COMPLETED_DESELECT_TRACK = 2;
    public static final int CALL_COMPLETED_LOOP_CURRENT = 3;
    public static final int CALL_COMPLETED_NOTIFY_WHEN_COMMAND_LABEL_REACHED = 1003;
    public static final int CALL_COMPLETED_PAUSE = 4;
    public static final int CALL_COMPLETED_PLAY = 5;
    public static final int CALL_COMPLETED_PREPARE = 6;
    public static final int CALL_COMPLETED_RELEASE_DRM = 12;
    public static final int CALL_COMPLETED_RESTORE_DRM_KEYS = 13;
    public static final int CALL_COMPLETED_SEEK_TO = 14;
    public static final int CALL_COMPLETED_SELECT_TRACK = 15;
    public static final int CALL_COMPLETED_SET_AUDIO_ATTRIBUTES = 16;
    public static final int CALL_COMPLETED_SET_AUDIO_SESSION_ID = 17;
    public static final int CALL_COMPLETED_SET_AUX_EFFECT_SEND_LEVEL = 18;
    public static final int CALL_COMPLETED_SET_BUFFERING_PARAMS = 1001;
    public static final int CALL_COMPLETED_SET_DATA_SOURCE = 19;
    public static final int CALL_COMPLETED_SET_NEXT_DATA_SOURCE = 22;
    public static final int CALL_COMPLETED_SET_NEXT_DATA_SOURCES = 23;
    public static final int CALL_COMPLETED_SET_PLAYBACK_PARAMS = 24;
    public static final int CALL_COMPLETED_SET_PLAYBACK_SPEED = 25;
    public static final int CALL_COMPLETED_SET_PLAYER_VOLUME = 26;
    public static final int CALL_COMPLETED_SET_SURFACE = 27;
    public static final int CALL_COMPLETED_SET_SYNC_PARAMS = 28;
    public static final int CALL_COMPLETED_SET_VIDEO_SCALING_MODE = 1002;
    public static final int CALL_COMPLETED_SKIP_TO_NEXT = 29;
    public static final int CALL_STATUS_BAD_VALUE = 2;
    public static final int CALL_STATUS_ERROR_IO = 4;
    public static final int CALL_STATUS_ERROR_UNKNOWN = Integer.MIN_VALUE;
    public static final int CALL_STATUS_INVALID_OPERATION = 1;
    public static final int CALL_STATUS_NO_DRM_SCHEME = 5;
    public static final int CALL_STATUS_NO_ERROR = 0;
    public static final int CALL_STATUS_PERMISSION_DENIED = 3;
    public static final int MEDIAPLAYER2_STATE_ERROR = 5;
    public static final int MEDIAPLAYER2_STATE_IDLE = 1;
    public static final int MEDIAPLAYER2_STATE_PAUSED = 3;
    public static final int MEDIAPLAYER2_STATE_PLAYING = 4;
    public static final int MEDIAPLAYER2_STATE_PREPARED = 2;
    public static final int MEDIA_ERROR_IO = -1004;
    public static final int MEDIA_ERROR_MALFORMED = -1007;
    public static final int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200;
    public static final int MEDIA_ERROR_SYSTEM = Integer.MIN_VALUE;
    public static final int MEDIA_ERROR_TIMED_OUT = -110;
    public static final int MEDIA_ERROR_UNKNOWN = 1;
    public static final int MEDIA_ERROR_UNSUPPORTED = -1010;
    public static final int MEDIA_INFO_AUDIO_NOT_PLAYING = 804;
    public static final int MEDIA_INFO_AUDIO_RENDERING_START = 4;
    public static final int MEDIA_INFO_BAD_INTERLEAVING = 800;
    public static final int MEDIA_INFO_BUFFERING_END = 702;
    public static final int MEDIA_INFO_BUFFERING_START = 701;
    public static final int MEDIA_INFO_BUFFERING_UPDATE = 704;
    public static final int MEDIA_INFO_EXTERNAL_METADATA_UPDATE = 803;
    public static final int MEDIA_INFO_METADATA_UPDATE = 802;
    public static final int MEDIA_INFO_NETWORK_BANDWIDTH = 703;
    public static final int MEDIA_INFO_NOT_SEEKABLE = 801;
    public static final int MEDIA_INFO_PLAYBACK_COMPLETE = 5;
    public static final int MEDIA_INFO_PLAYLIST_END = 6;
    public static final int MEDIA_INFO_PREPARED = 100;
    public static final int MEDIA_INFO_STARTED_AS_NEXT = 2;
    public static final int MEDIA_INFO_SUBTITLE_TIMED_OUT = 902;
    public static final int MEDIA_INFO_TIMED_TEXT_ERROR = 900;
    public static final int MEDIA_INFO_UNKNOWN = 1;
    public static final int MEDIA_INFO_UNSUPPORTED_SUBTITLE = 901;
    public static final int MEDIA_INFO_VIDEO_NOT_PLAYING = 805;
    public static final int MEDIA_INFO_VIDEO_RENDERING_START = 3;
    public static final int MEDIA_INFO_VIDEO_TRACK_LAGGING = 700;
    public static final String MEDIA_MIMETYPE_TEXT_CEA_608 = "text/cea-608";
    public static final String MEDIA_MIMETYPE_TEXT_CEA_708 = "text/cea-708";
    public static final String MEDIA_MIMETYPE_TEXT_SUBRIP = "application/x-subrip";
    public static final String MEDIA_MIMETYPE_TEXT_VTT = "text/vtt";
    public static final boolean METADATA_ALL = false;
    public static final boolean METADATA_UPDATE_ONLY = true;
    public static final int PLAYBACK_RATE_AUDIO_MODE_DEFAULT = 0;
    public static final int PLAYBACK_RATE_AUDIO_MODE_RESAMPLE = 2;
    public static final int PLAYBACK_RATE_AUDIO_MODE_STRETCH = 1;
    public static final int PREPARE_DRM_STATUS_PREPARATION_ERROR = 3;
    public static final int PREPARE_DRM_STATUS_PROVISIONING_NETWORK_ERROR = 1;
    public static final int PREPARE_DRM_STATUS_PROVISIONING_SERVER_ERROR = 2;
    public static final int PREPARE_DRM_STATUS_SUCCESS = 0;
    public static final int SEEK_CLOSEST = 3;
    public static final int SEEK_CLOSEST_SYNC = 2;
    public static final int SEEK_NEXT_SYNC = 1;
    public static final int SEEK_PREVIOUS_SYNC = 0;
    public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT = 1;
    public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING = 2;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface CallCompleted {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface CallStatus {
    }

    /* loaded from: classes.dex */
    public static abstract class DrmInfo {
        public abstract synchronized Map<UUID, byte[]> getPssh();

        public abstract synchronized List<UUID> getSupportedSchemes();
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface MediaError {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface MediaInfo {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface MediaPlayer2State {
    }

    /* loaded from: classes.dex */
    public interface OnDrmConfigHelper {
        synchronized void onDrmConfig(MediaPlayer2 mediaPlayer2, DataSourceDesc dataSourceDesc);
    }

    /* loaded from: classes.dex */
    public interface OnSubtitleDataListener {
        synchronized void onSubtitleData(MediaPlayer2 mediaPlayer2, SubtitleData subtitleData);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface PlaybackRateAudioMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface PrepareDrmStatusCode {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface SeekMode {
    }

    /* loaded from: classes.dex */
    public static abstract class TrackInfo {
        public static final int MEDIA_TRACK_TYPE_AUDIO = 2;
        public static final int MEDIA_TRACK_TYPE_METADATA = 5;
        public static final int MEDIA_TRACK_TYPE_SUBTITLE = 4;
        public static final int MEDIA_TRACK_TYPE_TIMEDTEXT = 3;
        public static final int MEDIA_TRACK_TYPE_UNKNOWN = 0;
        public static final int MEDIA_TRACK_TYPE_VIDEO = 1;

        public abstract synchronized MediaFormat getFormat();

        private protected abstract String getLanguage();

        private protected abstract int getTrackType();

        public abstract String toString();
    }

    @Override // android.media.AudioRouting
    public abstract void addOnRoutingChangedListener(AudioRouting.OnRoutingChangedListener onRoutingChangedListener, Handler handler);

    public abstract synchronized void addTimedTextSource(FileDescriptor fileDescriptor, long j, long j2, String str);

    public abstract synchronized void attachAuxEffect(int i);

    public abstract synchronized void clearDrmEventCallback();

    public abstract synchronized void clearMediaPlayer2EventCallback();

    public abstract synchronized void clearPendingCommands();

    @Override // java.lang.AutoCloseable
    public abstract void close();

    public abstract synchronized void deselectTrack(int i);

    @Override // android.media.MediaPlayerBase
    public abstract synchronized AudioAttributes getAudioAttributes();

    public abstract synchronized int getAudioSessionId();

    @Override // android.media.MediaPlayerBase
    public abstract synchronized long getBufferedPosition();

    @Override // android.media.MediaPlayerBase
    public abstract synchronized int getBufferingState();

    @Override // android.media.MediaPlayerBase
    public abstract synchronized DataSourceDesc getCurrentDataSource();

    @Override // android.media.MediaPlayerBase
    public abstract synchronized long getCurrentPosition();

    public abstract synchronized DrmInfo getDrmInfo();

    public abstract synchronized MediaDrm.KeyRequest getDrmKeyRequest(byte[] bArr, byte[] bArr2, String str, int i, Map<String, String> map) throws NoDrmSchemeException;

    public abstract synchronized String getDrmPropertyString(String str) throws NoDrmSchemeException;

    @Override // android.media.MediaPlayerBase
    public abstract synchronized long getDuration();

    public abstract synchronized int getMediaPlayer2State();

    public abstract synchronized PersistableBundle getMetrics();

    public abstract synchronized PlaybackParams getPlaybackParams();

    @Override // android.media.MediaPlayerBase
    public abstract synchronized int getPlayerState();

    @Override // android.media.MediaPlayerBase
    public abstract synchronized float getPlayerVolume();

    @Override // android.media.AudioRouting
    public abstract AudioDeviceInfo getPreferredDevice();

    @Override // android.media.AudioRouting
    public abstract AudioDeviceInfo getRoutedDevice();

    public abstract synchronized int getSelectedTrack(int i);

    public abstract synchronized SyncParams getSyncParams();

    public abstract synchronized MediaTimestamp getTimestamp();

    public abstract synchronized List<TrackInfo> getTrackInfo();

    public abstract synchronized int getVideoHeight();

    public abstract synchronized int getVideoWidth();

    public abstract synchronized boolean isPlaying();

    @Override // android.media.MediaPlayerBase
    public abstract synchronized void loopCurrent(boolean z);

    @Override // android.media.MediaPlayerBase
    public abstract synchronized void pause();

    @Override // android.media.MediaPlayerBase
    public abstract synchronized void play();

    @Override // android.media.MediaPlayerBase
    public abstract synchronized void prepare();

    public abstract synchronized void prepareDrm(UUID uuid) throws UnsupportedSchemeException, ResourceBusyException, ProvisioningNetworkErrorException, ProvisioningServerErrorException;

    public abstract synchronized byte[] provideDrmKeyResponse(byte[] bArr, byte[] bArr2) throws NoDrmSchemeException, DeniedByServerException;

    @Override // android.media.MediaPlayerBase
    public abstract synchronized void registerPlayerEventCallback(Executor executor, MediaPlayerBase.PlayerEventCallback playerEventCallback);

    public abstract synchronized void releaseDrm() throws NoDrmSchemeException;

    @Override // android.media.AudioRouting
    public abstract void removeOnRoutingChangedListener(AudioRouting.OnRoutingChangedListener onRoutingChangedListener);

    @Override // android.media.MediaPlayerBase
    public abstract synchronized void reset();

    public abstract synchronized void restoreDrmKeys(byte[] bArr) throws NoDrmSchemeException;

    public abstract synchronized void seekTo(long j, int i);

    public abstract synchronized void selectTrack(int i);

    @Override // android.media.MediaPlayerBase
    public abstract synchronized void setAudioAttributes(AudioAttributes audioAttributes);

    public abstract synchronized void setAudioSessionId(int i);

    public abstract synchronized void setAuxEffectSendLevel(float f);

    @Override // android.media.MediaPlayerBase
    public abstract synchronized void setDataSource(DataSourceDesc dataSourceDesc);

    public abstract synchronized void setDisplay(SurfaceHolder surfaceHolder);

    public abstract synchronized void setDrmEventCallback(Executor executor, DrmEventCallback drmEventCallback);

    public abstract synchronized void setDrmPropertyString(String str, String str2) throws NoDrmSchemeException;

    public abstract synchronized void setMediaPlayer2EventCallback(Executor executor, MediaPlayer2EventCallback mediaPlayer2EventCallback);

    @Override // android.media.MediaPlayerBase
    public abstract synchronized void setNextDataSource(DataSourceDesc dataSourceDesc);

    @Override // android.media.MediaPlayerBase
    public abstract synchronized void setNextDataSources(List<DataSourceDesc> list);

    public abstract synchronized void setOnDrmConfigHelper(OnDrmConfigHelper onDrmConfigHelper);

    public abstract synchronized void setPlaybackParams(PlaybackParams playbackParams);

    @Override // android.media.MediaPlayerBase
    public abstract synchronized void setPlaybackSpeed(float f);

    @Override // android.media.MediaPlayerBase
    public abstract synchronized void setPlayerVolume(float f);

    @Override // android.media.AudioRouting
    public abstract boolean setPreferredDevice(AudioDeviceInfo audioDeviceInfo);

    public abstract synchronized void setScreenOnWhilePlaying(boolean z);

    public abstract synchronized void setSurface(Surface surface);

    public abstract synchronized void setSyncParams(SyncParams syncParams);

    public abstract synchronized void setWakeMode(Context context, int i);

    @Override // android.media.MediaPlayerBase
    public abstract synchronized void skipToNext();

    @Override // android.media.MediaPlayerBase
    public abstract synchronized void unregisterPlayerEventCallback(MediaPlayerBase.PlayerEventCallback playerEventCallback);

    public static final synchronized MediaPlayer2 create() {
        return new MediaPlayer2Impl();
    }

    private static final synchronized String[] decodeMediaPlayer2Uri(String location) {
        Uri uri = Uri.parse(location);
        if ("mediaplayer2".equals(uri.getScheme())) {
            List<String> uris = uri.getQueryParameters("uri");
            if (uris.isEmpty()) {
                return new String[]{location};
            }
            List<String> keys = uri.getQueryParameters("key");
            List<String> values = uri.getQueryParameters("value");
            if (keys.size() != values.size()) {
                return new String[]{uris.get(0)};
            }
            List<String> ls = new ArrayList<>();
            ls.add(uris.get(0));
            for (int i = 0; i < keys.size(); i++) {
                ls.add(keys.get(i));
                ls.add(values.get(i));
            }
            int i2 = ls.size();
            return (String[]) ls.toArray(new String[i2]);
        }
        return new String[]{location};
    }

    private static final synchronized String encodeMediaPlayer2Uri(String uri, String[] keys, String[] values) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("mediaplayer2").path("/").appendQueryParameter("uri", uri);
        if (keys == null || values == null || keys.length != values.length) {
            return builder.build().toString();
        }
        for (int i = 0; i < keys.length; i++) {
            builder.appendQueryParameter("key", keys[i]).appendQueryParameter("value", values[i]);
        }
        return builder.build().toString();
    }

    @Override // android.media.MediaPlayerBase
    public synchronized void seekTo(long msec) {
        seekTo(msec, 0);
    }

    @Override // android.media.MediaPlayerBase
    public synchronized float getPlaybackSpeed() {
        return 1.0f;
    }

    @Override // android.media.MediaPlayerBase
    public synchronized boolean isReversePlaybackSupported() {
        return false;
    }

    @Override // android.media.MediaPlayerBase
    public synchronized float getMaxPlayerVolume() {
        return 1.0f;
    }

    public synchronized Parcel newRequest() {
        return null;
    }

    public synchronized void invoke(Parcel request, Parcel reply) {
    }

    public synchronized void notifyWhenCommandLabelReached(Object label) {
    }

    public synchronized void setVideoScalingMode(int mode) {
    }

    public synchronized void stop() {
    }

    public synchronized BufferingParams getBufferingParams() {
        return new BufferingParams.Builder().build();
    }

    public synchronized void setBufferingParams(BufferingParams params) {
    }

    public synchronized PlaybackParams easyPlaybackParams(float rate, int audioMode) {
        return new PlaybackParams();
    }

    public synchronized Metadata getMetadata(boolean update_only, boolean apply_filter) {
        return null;
    }

    public synchronized int setMetadataFilter(Set<Integer> allow, Set<Integer> block) {
        return 0;
    }

    public synchronized void notifyAt(long mediaTimeUs) {
    }

    public synchronized boolean isLooping() {
        return false;
    }

    public synchronized void setSubtitleAnchor(SubtitleController controller, SubtitleController.Anchor anchor) {
    }

    @Override // android.media.SubtitleController.Listener
    public synchronized void onSubtitleTrackSelected(SubtitleTrack track) {
    }

    public synchronized void addSubtitleSource(InputStream is, MediaFormat format) {
    }

    public synchronized void addTimedTextSource(String path, String mimeType) throws IOException {
    }

    public synchronized void addTimedTextSource(Context context, Uri uri, String mimeType) throws IOException {
    }

    public synchronized void addTimedTextSource(FileDescriptor fd, String mimeType) {
    }

    public synchronized MediaTimeProvider getMediaTimeProvider() {
        return null;
    }

    /* loaded from: classes.dex */
    public static abstract class MediaPlayer2EventCallback {
        public synchronized void onVideoSizeChanged(MediaPlayer2 mp, DataSourceDesc dsd, int width, int height) {
        }

        public synchronized void onTimedText(MediaPlayer2 mp, DataSourceDesc dsd, TimedText text) {
        }

        public synchronized void onTimedMetaDataAvailable(MediaPlayer2 mp, DataSourceDesc dsd, TimedMetaData data) {
        }

        public synchronized void onError(MediaPlayer2 mp, DataSourceDesc dsd, int what, int extra) {
        }

        public synchronized void onInfo(MediaPlayer2 mp, DataSourceDesc dsd, int what, int extra) {
        }

        public synchronized void onCallCompleted(MediaPlayer2 mp, DataSourceDesc dsd, int what, int status) {
        }

        public synchronized void onMediaTimeChanged(MediaPlayer2 mp, DataSourceDesc dsd, MediaTimestamp timestamp) {
        }

        public synchronized void onCommandLabelReached(MediaPlayer2 mp, Object label) {
        }
    }

    public synchronized void setOnSubtitleDataListener(OnSubtitleDataListener listener) {
    }

    /* loaded from: classes.dex */
    public static abstract class DrmEventCallback {
        public synchronized void onDrmInfo(MediaPlayer2 mp, DataSourceDesc dsd, DrmInfo drmInfo) {
        }

        public synchronized void onDrmPrepared(MediaPlayer2 mp, DataSourceDesc dsd, int status) {
        }
    }

    /* loaded from: classes.dex */
    public static abstract class NoDrmSchemeException extends MediaDrmException {
        /* JADX INFO: Access modifiers changed from: protected */
        public synchronized NoDrmSchemeException(String detailMessage) {
            super(detailMessage);
        }
    }

    /* loaded from: classes.dex */
    public static abstract class ProvisioningNetworkErrorException extends MediaDrmException {
        /* JADX INFO: Access modifiers changed from: protected */
        public synchronized ProvisioningNetworkErrorException(String detailMessage) {
            super(detailMessage);
        }
    }

    /* loaded from: classes.dex */
    public static abstract class ProvisioningServerErrorException extends MediaDrmException {
        /* JADX INFO: Access modifiers changed from: protected */
        public synchronized ProvisioningServerErrorException(String detailMessage) {
            super(detailMessage);
        }
    }

    /* loaded from: classes.dex */
    public static final class MetricsConstants {
        public static final String CODEC_AUDIO = "android.media.mediaplayer.audio.codec";
        public static final String CODEC_VIDEO = "android.media.mediaplayer.video.codec";
        public static final String DURATION = "android.media.mediaplayer.durationMs";
        public static final String ERRORS = "android.media.mediaplayer.err";
        public static final String ERROR_CODE = "android.media.mediaplayer.errcode";
        public static final String FRAMES = "android.media.mediaplayer.frames";
        public static final String FRAMES_DROPPED = "android.media.mediaplayer.dropped";
        public static final String HEIGHT = "android.media.mediaplayer.height";
        public static final String MIME_TYPE_AUDIO = "android.media.mediaplayer.audio.mime";
        public static final String MIME_TYPE_VIDEO = "android.media.mediaplayer.video.mime";
        public static final String PLAYING = "android.media.mediaplayer.playingMs";
        public static final String WIDTH = "android.media.mediaplayer.width";

        private synchronized MetricsConstants() {
        }
    }
}
