package android.media;

import android.app.PendingIntent;
import android.media.MediaLibraryService2;
import android.media.MediaSession2;
import android.media.update.ApiLoader;
import android.media.update.MediaLibraryService2Provider;
import android.media.update.MediaSession2Provider;
import android.media.update.MediaSessionService2Provider;
import android.media.update.ProviderCreator;
import android.os.Bundle;
import java.util.List;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public abstract class MediaLibraryService2 extends MediaSessionService2 {
    public static final String SERVICE_INTERFACE = "android.media.MediaLibraryService2";

    @Override // android.media.MediaSessionService2
    public abstract synchronized MediaLibrarySession onCreateSession(String str);

    /* loaded from: classes.dex */
    public static final class MediaLibrarySession extends MediaSession2 {
        private final MediaLibraryService2Provider.MediaLibrarySessionProvider mProvider;

        /* loaded from: classes.dex */
        public static class MediaLibrarySessionCallback extends MediaSession2.SessionCallback {
            public synchronized LibraryRoot onGetLibraryRoot(MediaLibrarySession session, MediaSession2.ControllerInfo controllerInfo, Bundle rootHints) {
                return null;
            }

            public synchronized MediaItem2 onGetItem(MediaLibrarySession session, MediaSession2.ControllerInfo controllerInfo, String mediaId) {
                return null;
            }

            public synchronized List<MediaItem2> onGetChildren(MediaLibrarySession session, MediaSession2.ControllerInfo controller, String parentId, int page, int pageSize, Bundle extras) {
                return null;
            }

            public synchronized void onSubscribe(MediaLibrarySession session, MediaSession2.ControllerInfo controller, String parentId, Bundle extras) {
            }

            public synchronized void onUnsubscribe(MediaLibrarySession session, MediaSession2.ControllerInfo controller, String parentId) {
            }

            public synchronized void onSearch(MediaLibrarySession session, MediaSession2.ControllerInfo controllerInfo, String query, Bundle extras) {
            }

            public synchronized List<MediaItem2> onGetSearchResult(MediaLibrarySession session, MediaSession2.ControllerInfo controllerInfo, String query, int page, int pageSize, Bundle extras) {
                return null;
            }
        }

        /* loaded from: classes.dex */
        public static final class Builder extends MediaSession2.BuilderBase<MediaLibrarySession, Builder, MediaLibrarySessionCallback> {
            public synchronized Builder(final MediaLibraryService2 service, final Executor callbackExecutor, final MediaLibrarySessionCallback callback) {
                super(new ProviderCreator() { // from class: android.media.-$$Lambda$MediaLibraryService2$MediaLibrarySession$Builder$KbvKQ6JiEvVRMpYadxywG_GUsco
                    public final Object createProvider(Object obj) {
                        MediaSession2Provider.BuilderBaseProvider createMediaLibraryService2Builder;
                        createMediaLibraryService2Builder = ApiLoader.getProvider().createMediaLibraryService2Builder(MediaLibraryService2.this, (MediaLibraryService2.MediaLibrarySession.Builder) ((MediaSession2.BuilderBase) obj), callbackExecutor, callback);
                        return createMediaLibraryService2Builder;
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

            @Override // android.media.MediaSession2.BuilderBase
            public synchronized Builder setSessionCallback(Executor executor, MediaLibrarySessionCallback callback) {
                return (Builder) super.setSessionCallback(executor, (Executor) callback);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.media.MediaSession2.BuilderBase
            public synchronized MediaLibrarySession build() {
                return (MediaLibrarySession) super.build();
            }
        }

        public synchronized MediaLibrarySession(MediaLibraryService2Provider.MediaLibrarySessionProvider provider) {
            super(provider);
            this.mProvider = provider;
        }

        public synchronized void notifyChildrenChanged(MediaSession2.ControllerInfo controller, String parentId, int itemCount, Bundle extras) {
            this.mProvider.notifyChildrenChanged_impl(controller, parentId, itemCount, extras);
        }

        public synchronized void notifyChildrenChanged(String parentId, int itemCount, Bundle extras) {
            this.mProvider.notifyChildrenChanged_impl(parentId, itemCount, extras);
        }

        public synchronized void notifySearchResultChanged(MediaSession2.ControllerInfo controller, String query, int itemCount, Bundle extras) {
            this.mProvider.notifySearchResultChanged_impl(controller, query, itemCount, extras);
        }
    }

    @Override // android.media.MediaSessionService2
    synchronized MediaSessionService2Provider createProvider() {
        return ApiLoader.getProvider().createMediaLibraryService2(this);
    }

    /* loaded from: classes.dex */
    public static final class LibraryRoot {
        public static final String EXTRA_OFFLINE = "android.media.extra.OFFLINE";
        public static final String EXTRA_RECENT = "android.media.extra.RECENT";
        public static final String EXTRA_SUGGESTED = "android.media.extra.SUGGESTED";
        private final MediaLibraryService2Provider.LibraryRootProvider mProvider;

        public synchronized LibraryRoot(String rootId, Bundle extras) {
            this.mProvider = ApiLoader.getProvider().createMediaLibraryService2LibraryRoot(this, rootId, extras);
        }

        public synchronized String getRootId() {
            return this.mProvider.getRootId_impl();
        }

        public synchronized Bundle getExtras() {
            return this.mProvider.getExtras_impl();
        }
    }
}
