package android.media.update;

import android.media.SessionToken2;
import android.media.session.MediaController;
import android.util.AttributeSet;
import android.widget.MediaControlView2;
/* loaded from: classes2.dex */
public interface MediaControlView2Provider extends ViewGroupProvider {
    /* JADX INFO: Access modifiers changed from: private */
    synchronized void initialize(AttributeSet attributeSet, int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void requestPlayButtonFocus_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setButtonVisibility_impl(int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setController_impl(MediaController mediaController);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setMediaSessionToken_impl(SessionToken2 sessionToken2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setOnFullScreenListener_impl(MediaControlView2.OnFullScreenListener onFullScreenListener);
}
