package android.media;

import android.content.Context;
import android.media.SubtitleTrack;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.CaptioningManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Vector;
/* compiled from: TtmlRenderer.java */
/* loaded from: classes.dex */
class TtmlRenderingWidget extends LinearLayout implements SubtitleTrack.RenderingWidget {
    private SubtitleTrack.RenderingWidget.OnChangedListener mListener;
    private final TextView mTextView;

    public synchronized TtmlRenderingWidget(Context context) {
        this(context, null);
    }

    public synchronized TtmlRenderingWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public synchronized TtmlRenderingWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public synchronized TtmlRenderingWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setLayerType(1, null);
        CaptioningManager captionManager = (CaptioningManager) context.getSystemService(Context.CAPTIONING_SERVICE);
        this.mTextView = new TextView(context);
        this.mTextView.setTextColor(captionManager.getUserStyle().foregroundColor);
        addView(this.mTextView, -1, -1);
        this.mTextView.setGravity(81);
    }

    public synchronized void setOnChangedListener(SubtitleTrack.RenderingWidget.OnChangedListener listener) {
        this.mListener = listener;
    }

    public synchronized void setSize(int width, int height) {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(width, 1073741824);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(height, 1073741824);
        measure(widthSpec, heightSpec);
        layout(0, 0, width, height);
    }

    @Override // android.media.SubtitleTrack.RenderingWidget
    public synchronized void setVisible(boolean visible) {
        if (visible) {
            setVisibility(0);
        } else {
            setVisibility(8);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public synchronized void setActiveCues(Vector<SubtitleTrack.Cue> activeCues) {
        int count = activeCues.size();
        String subtitleText = "";
        for (int i = 0; i < count; i++) {
            TtmlCue cue = (TtmlCue) activeCues.get(i);
            subtitleText = subtitleText + cue.mText + "\n";
        }
        this.mTextView.setText(subtitleText);
        if (this.mListener != null) {
            this.mListener.onChanged(this);
        }
    }
}
