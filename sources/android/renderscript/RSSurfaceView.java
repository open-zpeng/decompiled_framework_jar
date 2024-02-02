package android.renderscript;

import android.content.Context;
import android.renderscript.RenderScriptGL;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
/* loaded from: classes2.dex */
public class RSSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private RenderScriptGL mRS;
    private SurfaceHolder mSurfaceHolder;

    private protected RSSurfaceView(Context context) {
        super(context);
        init();
    }

    private protected RSSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private synchronized void init() {
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder holder) {
        this.mSurfaceHolder = holder;
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (this) {
            if (this.mRS != null) {
                this.mRS.setSurface(null, 0, 0);
            }
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        synchronized (this) {
            if (this.mRS != null) {
                this.mRS.setSurface(holder, w, h);
            }
        }
    }

    public synchronized void pause() {
        if (this.mRS != null) {
            this.mRS.pause();
        }
    }

    public synchronized void resume() {
        if (this.mRS != null) {
            this.mRS.resume();
        }
    }

    public synchronized RenderScriptGL createRenderScriptGL(RenderScriptGL.SurfaceConfig sc) {
        RenderScriptGL rs = new RenderScriptGL(getContext(), sc);
        setRenderScriptGL(rs);
        return rs;
    }

    public synchronized void destroyRenderScriptGL() {
        this.mRS.destroy();
        this.mRS = null;
    }

    public synchronized void setRenderScriptGL(RenderScriptGL rs) {
        this.mRS = rs;
    }

    public synchronized RenderScriptGL getRenderScriptGL() {
        return this.mRS;
    }
}
