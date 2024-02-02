package android.media.update;

import android.app.PendingIntent;
import android.media.AudioAttributes;
import android.media.MediaController2;
import android.media.MediaItem2;
import android.media.MediaMetadata2;
import android.media.Rating2;
import android.media.SessionCommand2;
import android.media.SessionToken2;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import java.util.List;
/* loaded from: classes2.dex */
public interface MediaController2Provider extends TransportControlProvider {

    /* loaded from: classes2.dex */
    public interface PlaybackInfoProvider {
        /* JADX INFO: Access modifiers changed from: private */
        synchronized AudioAttributes getAudioAttributes_impl();

        /* JADX INFO: Access modifiers changed from: private */
        synchronized int getControlType_impl();

        /* JADX INFO: Access modifiers changed from: private */
        synchronized int getCurrentVolume_impl();

        /* JADX INFO: Access modifiers changed from: private */
        synchronized int getMaxVolume_impl();

        /* JADX INFO: Access modifiers changed from: private */
        synchronized int getPlaybackType_impl();
    }

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void addPlaylistItem_impl(int i, MediaItem2 mediaItem2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void adjustVolume_impl(int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void close_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void fastForward_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized long getBufferedPosition_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaItem2 getCurrentMediaItem_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized long getCurrentPosition_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaController2.PlaybackInfo getPlaybackInfo_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized float getPlaybackSpeed_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized int getPlayerState_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaMetadata2 getPlaylistMetadata_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized List<MediaItem2> getPlaylist_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized PendingIntent getSessionActivity_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized SessionToken2 getSessionToken_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void initialize();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized boolean isConnected_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void playFromMediaId_impl(String str, Bundle bundle);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void playFromSearch_impl(String str, Bundle bundle);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void playFromUri_impl(Uri uri, Bundle bundle);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void prepareFromMediaId_impl(String str, Bundle bundle);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void prepareFromSearch_impl(String str, Bundle bundle);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void prepareFromUri_impl(Uri uri, Bundle bundle);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void removePlaylistItem_impl(MediaItem2 mediaItem2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void replacePlaylistItem_impl(int i, MediaItem2 mediaItem2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void rewind_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void sendCustomCommand_impl(SessionCommand2 sessionCommand2, Bundle bundle, ResultReceiver resultReceiver);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setPlaylist_impl(List<MediaItem2> list, MediaMetadata2 mediaMetadata2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setRating_impl(String str, Rating2 rating2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setVolumeTo_impl(int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void updatePlaylistMetadata_impl(MediaMetadata2 mediaMetadata2);
}
