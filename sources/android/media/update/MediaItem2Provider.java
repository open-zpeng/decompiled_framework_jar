package android.media.update;

import android.media.DataSourceDesc;
import android.media.MediaItem2;
import android.media.MediaMetadata2;
import android.os.Bundle;
/* loaded from: classes2.dex */
public interface MediaItem2Provider {

    /* loaded from: classes2.dex */
    public interface BuilderProvider {
        /* JADX INFO: Access modifiers changed from: private */
        synchronized MediaItem2 build_impl();

        /* JADX INFO: Access modifiers changed from: private */
        synchronized MediaItem2.Builder setDataSourceDesc_impl(DataSourceDesc dataSourceDesc);

        /* JADX INFO: Access modifiers changed from: private */
        synchronized MediaItem2.Builder setMediaId_impl(String str);

        /* JADX INFO: Access modifiers changed from: private */
        synchronized MediaItem2.Builder setMetadata_impl(MediaMetadata2 mediaMetadata2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    synchronized boolean equals_impl(Object obj);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized DataSourceDesc getDataSourceDesc_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized int getFlags_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized String getMediaId_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized MediaMetadata2 getMetadata_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized boolean isBrowsable_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized boolean isPlayable_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setMetadata_impl(MediaMetadata2 mediaMetadata2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized Bundle toBundle_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized String toString_impl();
}
