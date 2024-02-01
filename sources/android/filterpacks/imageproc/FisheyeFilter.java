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
import android.os.BatteryManager;
/* loaded from: classes.dex */
public class FisheyeFilter extends Filter {
    public protected static final String TAG = "FisheyeFilter";
    public protected static final String mFisheyeShader = "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform vec2 scale;\nuniform float alpha;\nuniform float radius2;\nuniform float factor;\nvarying vec2 v_texcoord;\nvoid main() {\n  const float m_pi_2 = 1.570963;\n  const float min_dist = 0.01;\n  vec2 coord = v_texcoord - vec2(0.5, 0.5);\n  float dist = length(coord * scale);\n  dist = max(dist, min_dist);\n  float radian = m_pi_2 - atan(alpha * sqrt(radius2 - dist * dist), dist);\n  float scalar = radian * factor / dist;\n  vec2 new_coord = coord * scalar + vec2(0.5, 0.5);\n  gl_FragColor = texture2D(tex_sampler_0, new_coord);\n}\n";
    public protected int mHeight;
    public protected Program mProgram;
    @GenerateFieldPort(hasDefault = true, name = BatteryManager.EXTRA_SCALE)
    public protected float mScale;
    public protected int mTarget;
    @GenerateFieldPort(hasDefault = true, name = "tile_size")
    public protected int mTileSize;
    public protected int mWidth;

    private protected synchronized FisheyeFilter(String name) {
        super(name);
        this.mScale = 0.0f;
        this.mTileSize = 640;
        this.mWidth = 0;
        this.mHeight = 0;
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
            ShaderProgram shaderProgram = new ShaderProgram(context, mFisheyeShader);
            shaderProgram.setMaximumTileSize(this.mTileSize);
            this.mProgram = shaderProgram;
            this.mTarget = target;
            return;
        }
        throw new RuntimeException("Filter FisheyeFilter does not support frames of target " + target + "!");
    }

    private protected synchronized void process(FilterContext context) {
        Frame input = pullInput(SliceItem.FORMAT_IMAGE);
        FrameFormat inputFormat = input.getFormat();
        Frame output = context.getFrameManager().newFrame(inputFormat);
        if (this.mProgram == null || inputFormat.getTarget() != this.mTarget) {
            initProgram(context, inputFormat.getTarget());
        }
        if (inputFormat.getWidth() != this.mWidth || inputFormat.getHeight() != this.mHeight) {
            updateFrameSize(inputFormat.getWidth(), inputFormat.getHeight());
        }
        this.mProgram.process(input, output);
        pushOutput(SliceItem.FORMAT_IMAGE, output);
        output.release();
    }

    private protected synchronized void fieldPortValueUpdated(String name, FilterContext context) {
        if (this.mProgram != null) {
            updateProgramParams();
        }
    }

    public protected synchronized void updateFrameSize(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        updateProgramParams();
    }

    public protected synchronized void updateProgramParams() {
        float[] scale = new float[2];
        if (this.mWidth > this.mHeight) {
            scale[0] = 1.0f;
            scale[1] = this.mHeight / this.mWidth;
        } else {
            scale[0] = this.mWidth / this.mHeight;
            scale[1] = 1.0f;
        }
        float alpha = (this.mScale * 2.0f) + 0.75f;
        float bound2 = 0.25f * ((scale[0] * scale[0]) + (scale[1] * scale[1]));
        float bound = (float) Math.sqrt(bound2);
        float radius = 1.15f * bound;
        float radius2 = radius * radius;
        float max_radian = 1.5707964f - ((float) Math.atan((alpha / bound) * ((float) Math.sqrt(radius2 - bound2))));
        float factor = bound / max_radian;
        this.mProgram.setHostValue(BatteryManager.EXTRA_SCALE, scale);
        this.mProgram.setHostValue("radius2", Float.valueOf(radius2));
        this.mProgram.setHostValue("factor", Float.valueOf(factor));
        this.mProgram.setHostValue("alpha", Float.valueOf(alpha));
    }
}
