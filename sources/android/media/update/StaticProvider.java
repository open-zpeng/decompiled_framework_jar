package android.media.update;

import android.app.Notification;
import android.content.Context;
import android.media.MediaBrowser2;
import android.media.MediaController2;
import android.media.MediaItem2;
import android.media.MediaLibraryService2;
import android.media.MediaMetadata2;
import android.media.MediaPlaylistAgent;
import android.media.MediaSession2;
import android.media.MediaSessionService2;
import android.media.Rating2;
import android.media.SessionCommand2;
import android.media.SessionCommandGroup2;
import android.media.SessionToken2;
import android.media.VolumeProvider2;
import android.media.update.MediaItem2Provider;
import android.media.update.MediaLibraryService2Provider;
import android.media.update.MediaMetadata2Provider;
import android.media.update.MediaSession2Provider;
import android.media.update.MediaSessionService2Provider;
import android.os.Bundle;
import android.os.IInterface;
import android.util.AttributeSet;
import android.widget.MediaControlView2;
import android.widget.VideoView2;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public interface StaticProvider {
    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaBrowser2Provider createMediaBrowser2(Context context, MediaBrowser2 mediaBrowser2, SessionToken2 sessionToken2, Executor executor, MediaBrowser2.BrowserCallback browserCallback);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaControlView2Provider createMediaControlView2(MediaControlView2 mediaControlView2, ViewGroupProvider viewGroupProvider, ViewGroupProvider viewGroupProvider2, AttributeSet attributeSet, int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaController2Provider createMediaController2(Context context, MediaController2 mediaController2, SessionToken2 sessionToken2, Executor executor, MediaController2.ControllerCallback controllerCallback);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaItem2Provider.BuilderProvider createMediaItem2Builder(MediaItem2.Builder builder, int i);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaSessionService2Provider createMediaLibraryService2(MediaLibraryService2 mediaLibraryService2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaSession2Provider.BuilderBaseProvider<MediaLibraryService2.MediaLibrarySession, MediaLibraryService2.MediaLibrarySession.MediaLibrarySessionCallback> createMediaLibraryService2Builder(MediaLibraryService2 mediaLibraryService2, MediaLibraryService2.MediaLibrarySession.Builder builder, Executor executor, MediaLibraryService2.MediaLibrarySession.MediaLibrarySessionCallback mediaLibrarySessionCallback);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaLibraryService2Provider.LibraryRootProvider createMediaLibraryService2LibraryRoot(MediaLibraryService2.LibraryRoot libraryRoot, String str, Bundle bundle);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaMetadata2Provider.BuilderProvider createMediaMetadata2Builder(MediaMetadata2.Builder builder);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaMetadata2Provider.BuilderProvider createMediaMetadata2Builder(MediaMetadata2.Builder builder, MediaMetadata2 mediaMetadata2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaPlaylistAgentProvider createMediaPlaylistAgent(MediaPlaylistAgent mediaPlaylistAgent);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaSession2Provider.BuilderBaseProvider<MediaSession2, MediaSession2.SessionCallback> createMediaSession2Builder(Context context, MediaSession2.Builder builder);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaSession2Provider.CommandProvider createMediaSession2Command(SessionCommand2 sessionCommand2, int i, String str, Bundle bundle);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaSession2Provider.CommandButtonProvider.BuilderProvider createMediaSession2CommandButtonBuilder(MediaSession2.CommandButton.Builder builder);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaSession2Provider.CommandGroupProvider createMediaSession2CommandGroup(SessionCommandGroup2 sessionCommandGroup2, SessionCommandGroup2 sessionCommandGroup22);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaSession2Provider.ControllerInfoProvider createMediaSession2ControllerInfo(Context context, MediaSession2.ControllerInfo controllerInfo, int i, int i2, String str, IInterface iInterface);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaSessionService2Provider createMediaSessionService2(MediaSessionService2 mediaSessionService2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaSessionService2Provider.MediaNotificationProvider createMediaSessionService2MediaNotification(MediaSessionService2.MediaNotification mediaNotification, int i, Notification notification);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized SessionToken2Provider createSessionToken2(Context context, SessionToken2 sessionToken2, String str, String str2, int i);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized VideoView2Provider createVideoView2(VideoView2 videoView2, ViewGroupProvider viewGroupProvider, ViewGroupProvider viewGroupProvider2, AttributeSet attributeSet, int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized VolumeProvider2Provider createVolumeProvider2(VolumeProvider2 volumeProvider2, int i, int i2, int i3);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaItem2 fromBundle_MediaItem2(Bundle bundle);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaMetadata2 fromBundle_MediaMetadata2(Bundle bundle);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized SessionCommand2 fromBundle_MediaSession2Command(Bundle bundle);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized SessionCommandGroup2 fromBundle_MediaSession2CommandGroup(Bundle bundle);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized Rating2 fromBundle_Rating2(Bundle bundle);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized SessionToken2 fromBundle_SessionToken2(Bundle bundle);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized Rating2 newHeartRating_Rating2(boolean z);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized Rating2 newPercentageRating_Rating2(float f);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized Rating2 newStarRating_Rating2(int i, float f);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized Rating2 newThumbRating_Rating2(boolean z);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized Rating2 newUnratedRating_Rating2(int i);
}
