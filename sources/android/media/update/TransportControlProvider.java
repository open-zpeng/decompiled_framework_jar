package android.media.update;

import android.media.MediaItem2;
/* loaded from: classes2.dex */
public interface TransportControlProvider {
    /* JADX INFO: Access modifiers changed from: private */
    synchronized int getRepeatMode_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized int getShuffleMode_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void pause_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void play_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void prepare_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void seekTo_impl(long j);

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
    synchronized void stop_impl();
}
