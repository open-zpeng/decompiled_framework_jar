package android.media;

import android.media.update.ApiLoader;
import android.media.update.MediaItem2Provider;
import android.os.Bundle;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes.dex */
public class MediaItem2 {
    public static final int FLAG_BROWSABLE = 1;
    public static final int FLAG_PLAYABLE = 2;
    private final MediaItem2Provider mProvider;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Flags {
    }

    public synchronized MediaItem2(MediaItem2Provider provider) {
        this.mProvider = provider;
    }

    public synchronized MediaItem2Provider getProvider() {
        return this.mProvider;
    }

    public synchronized Bundle toBundle() {
        return this.mProvider.toBundle_impl();
    }

    public static synchronized MediaItem2 fromBundle(Bundle bundle) {
        return ApiLoader.getProvider().fromBundle_MediaItem2(bundle);
    }

    public String toString() {
        return this.mProvider.toString_impl();
    }

    public synchronized int getFlags() {
        return this.mProvider.getFlags_impl();
    }

    public synchronized boolean isBrowsable() {
        return this.mProvider.isBrowsable_impl();
    }

    public synchronized boolean isPlayable() {
        return this.mProvider.isPlayable_impl();
    }

    public synchronized void setMetadata(MediaMetadata2 metadata) {
        this.mProvider.setMetadata_impl(metadata);
    }

    public synchronized MediaMetadata2 getMetadata() {
        return this.mProvider.getMetadata_impl();
    }

    public synchronized String getMediaId() {
        return this.mProvider.getMediaId_impl();
    }

    public synchronized DataSourceDesc getDataSourceDesc() {
        return this.mProvider.getDataSourceDesc_impl();
    }

    public boolean equals(Object obj) {
        return this.mProvider.equals_impl(obj);
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        private final MediaItem2Provider.BuilderProvider mProvider;

        public synchronized Builder(int flags) {
            this.mProvider = ApiLoader.getProvider().createMediaItem2Builder(this, flags);
        }

        public synchronized Builder setMediaId(String mediaId) {
            return this.mProvider.setMediaId_impl(mediaId);
        }

        public synchronized Builder setMetadata(MediaMetadata2 metadata) {
            return this.mProvider.setMetadata_impl(metadata);
        }

        public synchronized Builder setDataSourceDesc(DataSourceDesc dataSourceDesc) {
            return this.mProvider.setDataSourceDesc_impl(dataSourceDesc);
        }

        public synchronized MediaItem2 build() {
            return this.mProvider.build_impl();
        }
    }
}
