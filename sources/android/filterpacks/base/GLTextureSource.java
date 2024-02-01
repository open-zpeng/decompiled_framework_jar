package android.filterpacks.base;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.format.ImageFormat;
/* loaded from: classes.dex */
public class GLTextureSource extends Filter {
    public protected Frame mFrame;
    @GenerateFieldPort(name = "height")
    public protected int mHeight;
    @GenerateFieldPort(hasDefault = true, name = "repeatFrame")
    public protected boolean mRepeatFrame;
    @GenerateFieldPort(name = "texId")
    public protected int mTexId;
    @GenerateFieldPort(hasDefault = true, name = "timestamp")
    public protected long mTimestamp;
    @GenerateFieldPort(name = "width")
    public protected int mWidth;

    private protected synchronized GLTextureSource(String name) {
        super(name);
        this.mRepeatFrame = false;
        this.mTimestamp = -1L;
    }

    private protected synchronized void setupPorts() {
        addOutputPort("frame", ImageFormat.create(3, 3));
    }

    private protected synchronized void fieldPortValueUpdated(String name, FilterContext context) {
        if (this.mFrame != null) {
            this.mFrame.release();
            this.mFrame = null;
        }
    }

    private protected synchronized void process(FilterContext context) {
        if (this.mFrame == null) {
            FrameFormat outputFormat = ImageFormat.create(this.mWidth, this.mHeight, 3, 3);
            this.mFrame = context.getFrameManager().newBoundFrame(outputFormat, 100, this.mTexId);
            this.mFrame.setTimestamp(this.mTimestamp);
        }
        pushOutput("frame", this.mFrame);
        if (!this.mRepeatFrame) {
            closeOutputPort("frame");
        }
    }

    private protected synchronized void tearDown(FilterContext context) {
        if (this.mFrame != null) {
            this.mFrame.release();
        }
    }
}
