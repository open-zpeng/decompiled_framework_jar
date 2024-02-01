package android.media;

import android.content.Context;
import android.media.SubtitleController;
/* loaded from: classes.dex */
public class ClosedCaptionRenderer extends SubtitleController.Renderer {
    private Cea608CCWidget mCCWidget;
    private final Context mContext;

    public synchronized ClosedCaptionRenderer(Context context) {
        this.mContext = context;
    }

    @Override // android.media.SubtitleController.Renderer
    public synchronized boolean supports(MediaFormat format) {
        if (format.containsKey(MediaFormat.KEY_MIME)) {
            String mimeType = format.getString(MediaFormat.KEY_MIME);
            return "text/cea-608".equals(mimeType);
        }
        return false;
    }

    @Override // android.media.SubtitleController.Renderer
    public synchronized SubtitleTrack createTrack(MediaFormat format) {
        String mimeType = format.getString(MediaFormat.KEY_MIME);
        if ("text/cea-608".equals(mimeType)) {
            if (this.mCCWidget == null) {
                this.mCCWidget = new Cea608CCWidget(this.mContext);
            }
            return new Cea608CaptionTrack(this.mCCWidget, format);
        }
        throw new RuntimeException("No matching format: " + format.toString());
    }
}
