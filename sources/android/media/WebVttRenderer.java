package android.media;

import android.content.Context;
import android.media.SubtitleController;
/* loaded from: classes.dex */
public class WebVttRenderer extends SubtitleController.Renderer {
    private final Context mContext;
    private WebVttRenderingWidget mRenderingWidget;

    /* JADX INFO: Access modifiers changed from: private */
    public WebVttRenderer(Context context) {
        this.mContext = context;
    }

    @Override // android.media.SubtitleController.Renderer
    public synchronized boolean supports(MediaFormat format) {
        if (format.containsKey(MediaFormat.KEY_MIME)) {
            return format.getString(MediaFormat.KEY_MIME).equals("text/vtt");
        }
        return false;
    }

    @Override // android.media.SubtitleController.Renderer
    public synchronized SubtitleTrack createTrack(MediaFormat format) {
        if (this.mRenderingWidget == null) {
            this.mRenderingWidget = new WebVttRenderingWidget(this.mContext);
        }
        return new WebVttTrack(this.mRenderingWidget, format);
    }
}
