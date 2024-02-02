package android.widget;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.DataSourceDesc;
import android.media.MediaItem2;
import android.media.MediaMetadata2;
import android.media.SessionToken2;
import android.media.session.PlaybackState;
import android.media.update.ApiLoader;
import android.media.update.VideoView2Provider;
import android.media.update.ViewGroupHelper;
import android.media.update.ViewGroupProvider;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
/* loaded from: classes3.dex */
public class VideoView2 extends ViewGroupHelper<VideoView2Provider> {
    public static final int VIEW_TYPE_SURFACEVIEW = 1;
    public static final int VIEW_TYPE_TEXTUREVIEW = 2;

    /* loaded from: classes3.dex */
    public interface OnCustomActionListener {
        synchronized void onCustomAction(String str, Bundle bundle);
    }

    /* loaded from: classes3.dex */
    public interface OnFullScreenRequestListener {
        synchronized void onFullScreenRequest(View view, boolean z);
    }

    @VisibleForTesting
    /* loaded from: classes3.dex */
    public interface OnViewTypeChangedListener {
        private protected void onViewTypeChanged(View view, int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes3.dex */
    public @interface ViewType {
    }

    public synchronized VideoView2(Context context) {
        this(context, null);
    }

    public synchronized VideoView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public synchronized VideoView2(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public synchronized VideoView2(Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(new ViewGroupHelper.ProviderCreator() { // from class: android.widget.-$$Lambda$VideoView2$uEOuYyXshHhDohoRHf3tK3H7V00
            public final ViewGroupProvider createProvider(ViewGroupHelper viewGroupHelper, ViewGroupProvider viewGroupProvider, ViewGroupProvider viewGroupProvider2) {
                VideoView2Provider createVideoView2;
                createVideoView2 = ApiLoader.getProvider().createVideoView2((VideoView2) viewGroupHelper, viewGroupProvider, viewGroupProvider2, AttributeSet.this, defStyleAttr, defStyleRes);
                return createVideoView2;
            }
        }, context, attrs, defStyleAttr, defStyleRes);
        ((VideoView2Provider) this.mProvider).initialize(attrs, defStyleAttr, defStyleRes);
    }

    public synchronized void setMediaControlView2(MediaControlView2 mediaControlView, long intervalMs) {
        ((VideoView2Provider) this.mProvider).setMediaControlView2_impl(mediaControlView, intervalMs);
    }

    public synchronized MediaControlView2 getMediaControlView2() {
        return ((VideoView2Provider) this.mProvider).getMediaControlView2_impl();
    }

    public synchronized void setMediaMetadata(MediaMetadata2 metadata) {
        ((VideoView2Provider) this.mProvider).setMediaMetadata_impl(metadata);
    }

    public synchronized MediaMetadata2 getMediaMetadata() {
        return ((VideoView2Provider) this.mProvider).getMediaMetadata_impl();
    }

    private protected android.media.session.MediaController getMediaController() {
        return ((VideoView2Provider) this.mProvider).getMediaController_impl();
    }

    public synchronized SessionToken2 getMediaSessionToken() {
        return ((VideoView2Provider) this.mProvider).getMediaSessionToken_impl();
    }

    public synchronized void setSubtitleEnabled(boolean enable) {
        ((VideoView2Provider) this.mProvider).setSubtitleEnabled_impl(enable);
    }

    public synchronized boolean isSubtitleEnabled() {
        return ((VideoView2Provider) this.mProvider).isSubtitleEnabled_impl();
    }

    public synchronized void setSpeed(float speed) {
        ((VideoView2Provider) this.mProvider).setSpeed_impl(speed);
    }

    public synchronized void setAudioFocusRequest(int focusGain) {
        ((VideoView2Provider) this.mProvider).setAudioFocusRequest_impl(focusGain);
    }

    public synchronized void setAudioAttributes(AudioAttributes attributes) {
        ((VideoView2Provider) this.mProvider).setAudioAttributes_impl(attributes);
    }

    private protected void setVideoPath(String path) {
        ((VideoView2Provider) this.mProvider).setVideoPath_impl(path);
    }

    public synchronized void setVideoUri(Uri uri) {
        ((VideoView2Provider) this.mProvider).setVideoUri_impl(uri);
    }

    public synchronized void setVideoUri(Uri uri, Map<String, String> headers) {
        ((VideoView2Provider) this.mProvider).setVideoUri_impl(uri, headers);
    }

    public synchronized void setMediaItem(MediaItem2 mediaItem) {
        ((VideoView2Provider) this.mProvider).setMediaItem_impl(mediaItem);
    }

    public synchronized void setDataSource(DataSourceDesc dataSource) {
        ((VideoView2Provider) this.mProvider).setDataSource_impl(dataSource);
    }

    public synchronized void setViewType(int viewType) {
        ((VideoView2Provider) this.mProvider).setViewType_impl(viewType);
    }

    public synchronized int getViewType() {
        return ((VideoView2Provider) this.mProvider).getViewType_impl();
    }

    public synchronized void setCustomActions(List<PlaybackState.CustomAction> actionList, Executor executor, OnCustomActionListener listener) {
        ((VideoView2Provider) this.mProvider).setCustomActions_impl(actionList, executor, listener);
    }

    @VisibleForTesting
    private protected void setOnViewTypeChangedListener(OnViewTypeChangedListener l) {
        ((VideoView2Provider) this.mProvider).setOnViewTypeChangedListener_impl(l);
    }

    public synchronized void setFullScreenRequestListener(OnFullScreenRequestListener l) {
        ((VideoView2Provider) this.mProvider).setFullScreenRequestListener_impl(l);
    }

    @Override // android.media.update.ViewGroupHelper, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        ((VideoView2Provider) this.mProvider).onLayout_impl(changed, l, t, r, b);
    }
}
