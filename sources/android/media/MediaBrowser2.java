package android.media;

import android.content.Context;
import android.media.MediaController2;
import android.media.update.ApiLoader;
import android.media.update.MediaBrowser2Provider;
import android.os.Bundle;
import java.util.List;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class MediaBrowser2 extends MediaController2 {
    private final MediaBrowser2Provider mProvider;

    /* loaded from: classes.dex */
    public static class BrowserCallback extends MediaController2.ControllerCallback {
        public synchronized void onGetLibraryRootDone(MediaBrowser2 browser, Bundle rootHints, String rootMediaId, Bundle rootExtra) {
        }

        public synchronized void onChildrenChanged(MediaBrowser2 browser, String parentId, int itemCount, Bundle extras) {
        }

        public synchronized void onGetChildrenDone(MediaBrowser2 browser, String parentId, int page, int pageSize, List<MediaItem2> result, Bundle extras) {
        }

        public synchronized void onGetItemDone(MediaBrowser2 browser, String mediaId, MediaItem2 result) {
        }

        public synchronized void onSearchResultChanged(MediaBrowser2 browser, String query, int itemCount, Bundle extras) {
        }

        public synchronized void onGetSearchResultDone(MediaBrowser2 browser, String query, int page, int pageSize, List<MediaItem2> result, Bundle extras) {
        }
    }

    public synchronized MediaBrowser2(Context context, SessionToken2 token, Executor executor, BrowserCallback callback) {
        super(context, token, executor, callback);
        this.mProvider = (MediaBrowser2Provider) getProvider();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.media.MediaController2
    public synchronized MediaBrowser2Provider createProvider(Context context, SessionToken2 token, Executor executor, MediaController2.ControllerCallback callback) {
        return ApiLoader.getProvider().createMediaBrowser2(context, this, token, executor, (BrowserCallback) callback);
    }

    public synchronized void getLibraryRoot(Bundle rootHints) {
        this.mProvider.getLibraryRoot_impl(rootHints);
    }

    public synchronized void subscribe(String parentId, Bundle extras) {
        this.mProvider.subscribe_impl(parentId, extras);
    }

    public synchronized void unsubscribe(String parentId) {
        this.mProvider.unsubscribe_impl(parentId);
    }

    public synchronized void getChildren(String parentId, int page, int pageSize, Bundle extras) {
        this.mProvider.getChildren_impl(parentId, page, pageSize, extras);
    }

    public synchronized void getItem(String mediaId) {
        this.mProvider.getItem_impl(mediaId);
    }

    public synchronized void search(String query, Bundle extras) {
        this.mProvider.search_impl(query, extras);
    }

    public synchronized void getSearchResult(String query, int page, int pageSize, Bundle extras) {
        this.mProvider.getSearchResult_impl(query, page, pageSize, extras);
    }
}
