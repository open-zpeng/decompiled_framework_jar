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
public class StraightenFilter extends Filter {
    public protected static final float DEGREE_TO_RADIAN = 0.017453292f;
    @GenerateFieldPort(hasDefault = true, name = "angle")
    public protected float mAngle;
    public protected int mHeight;
    @GenerateFieldPort(hasDefault = true, name = "maxAngle")
    public protected float mMaxAngle;
    public protected Program mProgram;
    public protected int mTarget;
    @GenerateFieldPort(hasDefault = true, name = "tile_size")
    public protected int mTileSize;
    public protected int mWidth;

    private protected synchronized StraightenFilter(String name) {
        super(name);
        this.mAngle = 0.0f;
        this.mMaxAngle = 45.0f;
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
            updateParameters();
        }
        Frame output = context.getFrameManager().newFrame(inputFormat);
        this.mProgram.process(input, output);
        pushOutput(SliceItem.FORMAT_IMAGE, output);
        output.release();
    }

    public protected synchronized void updateParameters() {
        float cosTheta = (float) Math.cos(this.mAngle * 0.017453292f);
        float sinTheta = (float) Math.sin(this.mAngle * 0.017453292f);
        if (this.mMaxAngle <= 0.0f) {
            throw new RuntimeException("Max angle is out of range (0-180).");
        }
        this.mMaxAngle = this.mMaxAngle <= 90.0f ? this.mMaxAngle : 90.0f;
        Point p0 = new Point(((-cosTheta) * this.mWidth) + (this.mHeight * sinTheta), ((-sinTheta) * this.mWidth) - (this.mHeight * cosTheta));
        Point p1 = new Point((this.mWidth * cosTheta) + (this.mHeight * sinTheta), (this.mWidth * sinTheta) - (this.mHeight * cosTheta));
        Point p2 = new Point(((-cosTheta) * this.mWidth) - (this.mHeight * sinTheta), ((-sinTheta) * this.mWidth) + (this.mHeight * cosTheta));
        Point p3 = new Point((this.mWidth * cosTheta) - (this.mHeight * sinTheta), (this.mWidth * sinTheta) + (this.mHeight * cosTheta));
        float maxWidth = Math.max(Math.abs(p0.x), Math.abs(p1.x));
        float maxHeight = Math.max(Math.abs(p0.y), Math.abs(p1.y));
        float scale = Math.min(this.mWidth / maxWidth, this.mHeight / maxHeight) * 0.5f;
        p0.set(((p0.x * scale) / this.mWidth) + 0.5f, ((p0.y * scale) / this.mHeight) + 0.5f);
        p1.set(((p1.x * scale) / this.mWidth) + 0.5f, ((p1.y * scale) / this.mHeight) + 0.5f);
        p2.set(((p2.x * scale) / this.mWidth) + 0.5f, ((p2.y * scale) / this.mHeight) + 0.5f);
        p3.set(((p3.x * scale) / this.mWidth) + 0.5f, ((p3.y * scale) / this.mHeight) + 0.5f);
        Quad quad = new Quad(p0, p1, p2, p3);
        ((ShaderProgram) this.mProgram).setSourceRegion(quad);
    }
}
