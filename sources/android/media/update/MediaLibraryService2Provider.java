package android.media.update;

import android.media.MediaSession2;
import android.os.Bundle;
/* loaded from: classes2.dex */
public interface MediaLibraryService2Provider extends MediaSessionService2Provider {

    /* loaded from: classes2.dex */
    public interface LibraryRootProvider {
        /* JADX INFO: Access modifiers changed from: private */
        synchronized Bundle getExtras_impl();

        /* JADX INFO: Access modifiers changed from: private */
        synchronized String getRootId_impl();
    }

    /* loaded from: classes2.dex */
    public interface MediaLibrarySessionProvider extends MediaSession2Provider {
        /* JADX INFO: Access modifiers changed from: private */
        synchronized void notifyChildrenChanged_impl(MediaSession2.ControllerInfo controllerInfo, String str, int i, Bundle bundle);

        /* JADX INFO: Access modifiers changed from: private */
        synchronized void notifyChildrenChanged_impl(String str, int i, Bundle bundle);

        /* JADX INFO: Access modifiers changed from: private */
        synchronized void notifySearchResultChanged_impl(MediaSession2.ControllerInfo controllerInfo, String str, int i, Bundle bundle);
    }
}
