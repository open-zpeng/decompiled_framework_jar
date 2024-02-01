package android.filterpacks.imageproc;

import android.app.slice.SliceItem;
import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.Program;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;
import android.filterfw.geometry.Point;
import android.filterfw.geometry.Quad;
/* loaded from: classes.dex */
public class RotateFilter extends Filter {
    @GenerateFieldPort(name = "angle")
    public protected int mAngle;
    public protected int mHeight;
    public protected int mOutputHeight;
    public protected int mOutputWidth;
    public protected Program mProgram;
    public protected int mTarget;
    @GenerateFieldPort(hasDefault = true, name = "tile_size")
    public protected int mTileSize;
    public protected int mWidth;

    private protected synchronized RotateFilter(String name) {
        super(name);
        this.mTileSize = 640;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mTarget = 0;
    }

    private protected synchronized void setupPorts() {
        addMaskedInputPort(SliceItem.FORMAT_IMAGE, ImageFormat.create(3));
        addOutputBasedOnInput(SliceItem.FORMAT_IMAGE, SliceItem.FORMAT_IMAGE);
    }

    private protected synchronized void initProgram(FilterContext context, int target) {
        if (target == 3) {
            ShaderProgram shaderProgram = ShaderProgram.createIdentity(context);
            shaderProgram.setMaximumTileSize(this.mTileSize);
            shaderProgram.setClearsOutput(true);
            this.mProgram = shaderProgram;
            this.mTarget = target;
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
        if (inputFormat.getWidth() != this.mWidth || inputFormat.getHeight() != this.mHeight) {
            this.mWidth = inputFormat.getWidth();
            this.mHeight = inputFormat.getHeight();
            this.mOutputWidth = this.mWidth;
            this.mOutputHeight = this.mHeight;
            updateParameters();
        }
        FrameFormat outputFormat = ImageFormat.create(this.mOutputWidth, this.mOutputHeight, 3, 3);
        Frame output = context.getFrameManager().newFrame(outputFormat);
        this.mProgram.process(input, output);
        pushOutput(SliceItem.FORMAT_IMAGE, output);
        output.release();
    }

    public protected synchronized void updateParameters() {
        float cosTheta;
        if (this.mAngle % 90 == 0) {
            if (this.mAngle % 180 == 0) {
                cosTheta = 0.0f;
                if (this.mAngle % 360 == 0) {
                    sinTheta = 1.0f;
                }
            } else {
                sinTheta = (this.mAngle + 90) % 360 != 0 ? 1.0f : -1.0f;
                this.mOutputWidth = this.mHeight;
                this.mOutputHeight = this.mWidth;
                float f = sinTheta;
                sinTheta = 0.0f;
                cosTheta = f;
            }
            Point x0 = new Point(((-sinTheta) + cosTheta + 1.0f) * 0.5f, (((-cosTheta) - sinTheta) + 1.0f) * 0.5f);
            Point x1 = new Point((sinTheta + cosTheta + 1.0f) * 0.5f, ((cosTheta - sinTheta) + 1.0f) * 0.5f);
            Point x2 = new Point((((-sinTheta) - cosTheta) + 1.0f) * 0.5f, ((-cosTheta) + sinTheta + 1.0f) * 0.5f);
            Point x3 = new Point(((sinTheta - cosTheta) + 1.0f) * 0.5f, 0.5f * (cosTheta + sinTheta + 1.0f));
            Quad quad = new Quad(x0, x1, x2, x3);
            ((ShaderProgram) this.mProgram).setTargetRegion(quad);
            return;
        }
        throw new RuntimeException("degree has to be multiply of 90.");
    }
}
