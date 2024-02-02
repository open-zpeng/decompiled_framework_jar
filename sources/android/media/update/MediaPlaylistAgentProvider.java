package android.media.update;

import android.media.DataSourceDesc;
import android.media.MediaItem2;
import android.media.MediaMetadata2;
import android.media.MediaPlaylistAgent;
import java.util.List;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public interface MediaPlaylistAgentProvider {
    /* JADX INFO: Access modifiers changed from: private */
    synchronized void addPlaylistItem_impl(int i, MediaItem2 mediaItem2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaItem2 getMediaItem_impl(DataSourceDesc dataSourceDesc);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaMetadata2 getPlaylistMetadata_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized List<MediaItem2> getPlaylist_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized int getRepeatMode_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized int getShuffleMode_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void notifyPlaylistChanged_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void notifyPlaylistMetadataChanged_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void notifyRepeatModeChanged_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void notifyShuffleModeChanged_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void registerPlaylistEventCallback_impl(Executor executor, MediaPlaylistAgent.PlaylistEventCallback playlistEventCallback);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void removePlaylistItem_impl(MediaItem2 mediaItem2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void replacePlaylistItem_impl(int i, MediaItem2 mediaItem2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setPlaylist_impl(List<MediaItem2> list, MediaMetadata2 mediaMetadata2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setRepeatMode_impl(int i);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setShuffleMode_impl(int i);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void skipToNextItem_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void skipToPlaylistItem_impl(MediaItem2 mediaItem2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void skipToPreviousItem_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void unregisterPlaylistEventCallback_impl(MediaPlaylistAgent.PlaylistEventCallback playlistEventCallback);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void updatePlaylistMetadata_impl(MediaMetadata2 mediaMetadata2);
}
