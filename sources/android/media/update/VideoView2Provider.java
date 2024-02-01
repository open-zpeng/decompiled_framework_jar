package android.media.update;

import android.media.AudioAttributes;
import android.media.DataSourceDesc;
import android.media.MediaItem2;
import android.media.MediaMetadata2;
import android.media.SessionToken2;
import android.media.session.MediaController;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.MediaControlView2;
import android.widget.VideoView2;
import com.android.internal.annotations.VisibleForTesting;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public interface VideoView2Provider extends ViewGroupProvider {
    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaControlView2 getMediaControlView2_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaController getMediaController_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaMetadata2 getMediaMetadata_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized SessionToken2 getMediaSessionToken_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized int getViewType_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void initialize(AttributeSet attributeSet, int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized boolean isSubtitleEnabled_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setAudioAttributes_impl(AudioAttributes audioAttributes);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setAudioFocusRequest_impl(int i);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setCustomActions_impl(List<PlaybackState.CustomAction> list, Executor executor, VideoView2.OnCustomActionListener onCustomActionListener);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setDataSource_impl(DataSourceDesc dataSourceDesc);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setFullScreenRequestListener_impl(VideoView2.OnFullScreenRequestListener onFullScreenRequestListener);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setMediaControlView2_impl(MediaControlView2 mediaControlView2, long j);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setMediaItem_impl(MediaItem2 mediaItem2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setMediaMetadata_impl(MediaMetadata2 mediaMetadata2);

    /* JADX INFO: Access modifiers changed from: private */
    @VisibleForTesting
    synchronized void setOnViewTypeChangedListener_impl(VideoView2.OnViewTypeChangedListener onViewTypeChangedListener);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setSpeed_impl(float f);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setSubtitleEnabled_impl(boolean z);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setVideoPath_impl(String str);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setVideoUri_impl(Uri uri);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setVideoUri_impl(Uri uri, Map<String, String> map);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setViewType_impl(int i);
}
