package android.media.update;

import android.app.PendingIntent;
import android.media.AudioFocusRequest;
import android.media.MediaItem2;
import android.media.MediaMetadata2;
import android.media.MediaPlayerBase;
import android.media.MediaPlaylistAgent;
import android.media.MediaSession2;
import android.media.SessionCommand2;
import android.media.SessionCommandGroup2;
import android.media.SessionToken2;
import android.media.VolumeProvider2;
import android.os.Bundle;
import android.os.ResultReceiver;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public interface MediaSession2Provider extends TransportControlProvider {

    /* loaded from: classes2.dex */
    public interface BuilderBaseProvider<T extends MediaSession2, C extends MediaSession2.SessionCallback> {
        /* JADX INFO: Access modifiers changed from: private */
        synchronized T build_impl();

        /* JADX INFO: Access modifiers changed from: private */
        synchronized void setId_impl(String str);

        /* JADX INFO: Access modifiers changed from: private */
        synchronized void setPlayer_impl(MediaPlayerBase mediaPlayerBase);

        /* JADX INFO: Access modifiers changed from: private */
        synchronized void setPlaylistAgent_impl(MediaPlaylistAgent mediaPlaylistAgent);

        /* JADX INFO: Access modifiers changed from: private */
        synchronized void setSessionActivity_impl(PendingIntent pendingIntent);

        /* JADX INFO: Access modifiers changed from: private */
        synchronized void setSessionCallback_impl(Executor executor, C c);

        /* JADX INFO: Access modifiers changed from: private */
        synchronized void setVolumeProvider_impl(VolumeProvider2 volumeProvider2);
    }

    /* loaded from: classes2.dex */
    public interface CommandButtonProvider {

        /* loaded from: classes2.dex */
        public interface BuilderProvider {
            /* JADX INFO: Access modifiers changed from: private */
            synchronized MediaSession2.CommandButton build_impl();

            /* JADX INFO: Access modifiers changed from: private */
            synchronized MediaSession2.CommandButton.Builder setCommand_impl(SessionCommand2 sessionCommand2);

            /* JADX INFO: Access modifiers changed from: private */
            synchronized MediaSession2.CommandButton.Builder setDisplayName_impl(String str);

            /* JADX INFO: Access modifiers changed from: private */
            synchronized MediaSession2.CommandButton.Builder setEnabled_impl(boolean z);

            /* JADX INFO: Access modifiers changed from: private */
            synchronized MediaSession2.CommandButton.Builder setExtras_impl(Bundle bundle);

            /* JADX INFO: Access modifiers changed from: private */
            synchronized MediaSession2.CommandButton.Builder setIconResId_impl(int i);
        }

        /* JADX INFO: Access modifiers changed from: private */
        synchronized SessionCommand2 getCommand_impl();

        /* JADX INFO: Access modifiers changed from: private */
        synchronized String getDisplayName_impl();

        /* JADX INFO: Access modifiers changed from: private */
        synchronized Bundle getExtras_impl();

        /* JADX INFO: Access modifiers changed from: private */
        synchronized int getIconResId_impl();

        /* JADX INFO: Access modifiers changed from: private */
        synchronized boolean isEnabled_impl();
    }

    /* loaded from: classes2.dex */
    public interface CommandGroupProvider {
        /* JADX INFO: Access modifiers changed from: private */
        synchronized void addAllPredefinedCommands_impl();

        /* JADX INFO: Access modifiers changed from: private */
        synchronized void addCommand_impl(SessionCommand2 sessionCommand2);

        /* JADX INFO: Access modifiers changed from: private */
        synchronized Set<SessionCommand2> getCommands_impl();

        /* JADX INFO: Access modifiers changed from: private */
        synchronized boolean hasCommand_impl(int i);

        /* JADX INFO: Access modifiers changed from: private */
        synchronized boolean hasCommand_impl(SessionCommand2 sessionCommand2);

        /* JADX INFO: Access modifiers changed from: private */
        synchronized void removeCommand_impl(SessionCommand2 sessionCommand2);

        /* JADX INFO: Access modifiers changed from: private */
        synchronized Bundle toBundle_impl();
    }

    /* loaded from: classes2.dex */
    public interface CommandProvider {
        /* JADX INFO: Access modifiers changed from: private */
        synchronized boolean equals_impl(Object obj);

        /* JADX INFO: Access modifiers changed from: private */
        synchronized int getCommandCode_impl();

        /* JADX INFO: Access modifiers changed from: private */
        synchronized String getCustomCommand_impl();

        /* JADX INFO: Access modifiers changed from: private */
        synchronized Bundle getExtras_impl();

        /* JADX INFO: Access modifiers changed from: private */
        synchronized int hashCode_impl();

        /* JADX INFO: Access modifiers changed from: private */
        synchronized Bundle toBundle_impl();
    }

    /* loaded from: classes2.dex */
    public interface ControllerInfoProvider {
        /* JADX INFO: Access modifiers changed from: private */
        synchronized boolean equals_impl(Object obj);

        /* JADX INFO: Access modifiers changed from: private */
        synchronized String getPackageName_impl();

        /* JADX INFO: Access modifiers changed from: private */
        synchronized int getUid_impl();

        /* JADX INFO: Access modifiers changed from: private */
        synchronized int hashCode_impl();

        /* JADX INFO: Access modifiers changed from: private */
        synchronized boolean isTrusted_impl();

        /* JADX INFO: Access modifiers changed from: private */
        synchronized String toString_impl();
    }

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void addPlaylistItem_impl(int i, MediaItem2 mediaItem2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void clearOnDataSourceMissingHelper_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void close_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized long getBufferedPosition_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized List<MediaSession2.ControllerInfo> getConnectedControllers_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaItem2 getCurrentPlaylistItem_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized long getCurrentPosition_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized int getPlayerState_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaPlayerBase getPlayer_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaPlaylistAgent getPlaylistAgent_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaMetadata2 getPlaylistMetadata_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized List<MediaItem2> getPlaylist_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized SessionToken2 getToken_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized VolumeProvider2 getVolumeProvider_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void notifyError_impl(int i, Bundle bundle);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void removePlaylistItem_impl(MediaItem2 mediaItem2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void replacePlaylistItem_impl(int i, MediaItem2 mediaItem2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void sendCustomCommand_impl(MediaSession2.ControllerInfo controllerInfo, SessionCommand2 sessionCommand2, Bundle bundle, ResultReceiver resultReceiver);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void sendCustomCommand_impl(SessionCommand2 sessionCommand2, Bundle bundle);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setAllowedCommands_impl(MediaSession2.ControllerInfo controllerInfo, SessionCommandGroup2 sessionCommandGroup2);

    private protected synchronized void setAudioFocusRequest_impl(AudioFocusRequest audioFocusRequest);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setCustomLayout_impl(MediaSession2.ControllerInfo controllerInfo, List<MediaSession2.CommandButton> list);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setOnDataSourceMissingHelper_impl(MediaSession2.OnDataSourceMissingHelper onDataSourceMissingHelper);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setPlaylist_impl(List<MediaItem2> list, MediaMetadata2 mediaMetadata2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void updatePlayer_impl(MediaPlayerBase mediaPlayerBase, MediaPlaylistAgent mediaPlaylistAgent, VolumeProvider2 volumeProvider2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void updatePlaylistMetadata_impl(MediaMetadata2 mediaMetadata2);
}
