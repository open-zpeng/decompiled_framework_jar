package android.widget;

import android.content.Context;
import android.media.SessionToken2;
import android.media.update.ApiLoader;
import android.media.update.MediaControlView2Provider;
import android.media.update.ViewGroupHelper;
import android.media.update.ViewGroupProvider;
import android.util.AttributeSet;
import android.view.View;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes3.dex */
public class MediaControlView2 extends ViewGroupHelper<MediaControlView2Provider> {
    public static final int BUTTON_ASPECT_RATIO = 10;
    public static final int BUTTON_FFWD = 2;
    public static final int BUTTON_FULL_SCREEN = 7;
    public static final int BUTTON_MUTE = 9;
    public static final int BUTTON_NEXT = 4;
    public static final int BUTTON_OVERFLOW = 8;
    public static final int BUTTON_PLAY_PAUSE = 1;
    public static final int BUTTON_PREV = 5;
    public static final int BUTTON_REW = 3;
    public static final int BUTTON_SETTINGS = 11;
    public static final int BUTTON_SUBTITLE = 6;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes3.dex */
    public @interface Button {
    }

    /* loaded from: classes3.dex */
    public interface OnFullScreenListener {
        synchronized void onFullScreen(View view, boolean z);
    }

    public synchronized MediaControlView2(Context context) {
        this(context, null);
    }

    public synchronized MediaControlView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public synchronized MediaControlView2(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public synchronized MediaControlView2(Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes) {
        super(new ViewGroupHelper.ProviderCreator() { // from class: android.widget.-$$Lambda$MediaControlView2$RI38ILmx2NwSJumbm0C4a0I-utM
            public final ViewGroupProvider createProvider(ViewGroupHelper viewGroupHelper, ViewGroupProvider viewGroupProvider, ViewGroupProvider viewGroupProvider2) {
                MediaControlView2Provider createMediaControlView2;
                createMediaControlView2 = ApiLoader.getProvider().createMediaControlView2((MediaControlView2) viewGroupHelper, viewGroupProvider, viewGroupProvider2, AttributeSet.this, defStyleAttr, defStyleRes);
                return createMediaControlView2;
            }
        }, context, attrs, defStyleAttr, defStyleRes);
        ((MediaControlView2Provider) this.mProvider).initialize(attrs, defStyleAttr, defStyleRes);
    }

    public synchronized void setMediaSessionToken(SessionToken2 token) {
        ((MediaControlView2Provider) this.mProvider).setMediaSessionToken_impl(token);
    }

    public synchronized void setOnFullScreenListener(OnFullScreenListener l) {
        ((MediaControlView2Provider) this.mProvider).setOnFullScreenListener_impl(l);
    }

    public synchronized void setController(android.media.session.MediaController controller) {
        ((MediaControlView2Provider) this.mProvider).setController_impl(controller);
    }

    public synchronized void setButtonVisibility(int button, int visibility) {
        ((MediaControlView2Provider) this.mProvider).setButtonVisibility_impl(button, visibility);
    }

    public synchronized void requestPlayButtonFocus() {
        ((MediaControlView2Provider) this.mProvider).requestPlayButtonFocus_impl();
    }

    @Override // android.media.update.ViewGroupHelper, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        ((MediaControlView2Provider) this.mProvider).onLayout_impl(changed, l, t, r, b);
    }
}
