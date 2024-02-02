package android.filterpacks.imageproc;

import android.app.slice.Slice;
import android.app.slice.SliceItem;
import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.Program;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;
/* loaded from: classes.dex */
public class FlipFilter extends Filter {
    @GenerateFieldPort(hasDefault = true, name = Slice.HINT_HORIZONTAL)
    public protected boolean mHorizontal;
    public protected Program mProgram;
    public protected int mTarget;
    @GenerateFieldPort(hasDefault = true, name = "tile_size")
    public protected int mTileSize;
    @GenerateFieldPort(hasDefault = true, name = "vertical")
    public protected boolean mVertical;

    private protected synchronized FlipFilter(String name) {
        super(name);
        this.mVertical = false;
        this.mHorizontal = false;
        this.mTileSize = 640;
        this.mTarget = 0;
    }

    private protected synchronized void setupPorts() {
        addMaskedInputPort(SliceItem.FORMAT_IMAGE, ImageFormat.create(3));
        addOutputBasedOnInput(SliceItem.FORMAT_IMAGE, SliceItem.FORMAT_IMAGE);
    }

    private protected synchronized FrameFormat getOutputFormat(String portName, FrameFormat inputFormat) {
        return inputFormat;
    }

    private protected synchronized void initProgram(FilterContext context, int target) {
        if (target == 3) {
            ShaderProgram shaderProgram = ShaderProgram.createIdentity(context);
            shaderProgram.setMaximumTileSize(this.mTileSize);
            this.mProgram = shaderProgram;
            this.mTarget = target;
            updateParameters();
            return;
        }
        throw new RuntimeException("Filter Sharpen does not support frames of target " + target + "!");
    }

    private protected synchronized void fieldPortValueUpdated(String name, FilterContext context) {
        if (this.mProgram != null) {
            updateParameters();
        }
    }

    private protected synchronized void process(FilterContext context) {
        Frame input = pullInput(SliceItem.FORMAT_IMAGE);
        FrameFormat inputFormat = input.getFormat();
        if (this.mProgram == null || inputFormat.getTarget() != this.mTarget) {
            initProgram(context, inputFormat.getTarget());
        }
        Frame output = context.getFrameManager().newFrame(inputFormat);
        this.mProgram.process(input, output);
        pushOutput(SliceItem.FORMAT_IMAGE, output);
        output.release();
    }

    public protected synchronized void updateParameters() {
        float x_origin = this.mHorizontal ? 1.0f : 0.0f;
        float y_origin = this.mVertical ? 1.0f : 0.0f;
        float width = this.mHorizontal ? -1.0f : 1.0f;
        float height = this.mVertical ? -1.0f : 1.0f;
        ((ShaderProgram) this.mProgram).setSourceRect(x_origin, y_origin, width, height);
    }
}
