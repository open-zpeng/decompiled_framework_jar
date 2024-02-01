package android.media.update;

import android.os.Bundle;
/* loaded from: classes2.dex */
public interface MediaBrowser2Provider extends MediaController2Provider {
    /* JADX INFO: Access modifiers changed from: private */
    synchronized void getChildren_impl(String str, int i, int i2, Bundle bundle);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void getItem_impl(String str);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void getLibraryRoot_impl(Bundle bundle);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void getSearchResult_impl(String str, int i, int i2, Bundle bundle);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void search_impl(String str, Bundle bundle);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void subscribe_impl(String str, Bundle bundle);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void unsubscribe_impl(String str);
}
