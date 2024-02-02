package android.filterpacks.base;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.format.ImageFormat;
/* loaded from: classes.dex */
public class GLTextureTarget extends Filter {
    @GenerateFieldPort(name = "texId")
    public protected int mTexId;

    private protected synchronized GLTextureTarget(String name) {
        super(name);
    }

    private protected synchronized void setupPorts() {
        addMaskedInputPort("frame", ImageFormat.create(3));
    }

    private protected synchronized void process(FilterContext context) {
        Frame input = pullInput("frame");
        FrameFormat format = ImageFormat.create(input.getFormat().getWidth(), input.getFormat().getHeight(), 3, 3);
        Frame frame = context.getFrameManager().newBoundFrame(format, 100, this.mTexId);
        frame.setDataFromFrame(input);
        frame.release();
    }
}
