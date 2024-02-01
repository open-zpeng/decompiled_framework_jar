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
import android.graphics.Color;
/* loaded from: classes.dex */
public class DuotoneFilter extends Filter {
    public protected final String mDuotoneShader;
    @GenerateFieldPort(hasDefault = true, name = "first_color")
    public protected int mFirstColor;
    public protected Program mProgram;
    @GenerateFieldPort(hasDefault = true, name = "second_color")
    public protected int mSecondColor;
    public protected int mTarget;
    @GenerateFieldPort(hasDefault = true, name = "tile_size")
    public protected int mTileSize;

    private protected synchronized DuotoneFilter(String name) {
        super(name);
        this.mFirstColor = -65536;
        this.mSecondColor = -256;
        this.mTileSize = 640;
        this.mTarget = 0;
        this.mDuotoneShader = "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform vec3 first;\nuniform vec3 second;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  float energy = (color.r + color.g + color.b) * 0.3333;\n  vec3 new_color = (1.0 - energy) * first + energy * second;\n  gl_FragColor = vec4(new_color.rgb, color.a);\n}\n";
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
            ShaderProgram shaderProgram = new ShaderProgram(context, "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform vec3 first;\nuniform vec3 second;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  float energy = (color.r + color.g + color.b) * 0.3333;\n  vec3 new_color = (1.0 - energy) * first + energy * second;\n  gl_FragColor = vec4(new_color.rgb, color.a);\n}\n");
            shaderProgram.setMaximumTileSize(this.mTileSize);
            this.mProgram = shaderProgram;
            this.mTarget = target;
            return;
        }
        throw new RuntimeException("Filter Duotone does not support frames of target " + target + "!");
    }

    private protected synchronized void process(FilterContext context) {
        Frame input = pullInput(SliceItem.FORMAT_IMAGE);
        FrameFormat inputFormat = input.getFormat();
        Frame output = context.getFrameManager().newFrame(inputFormat);
        if (this.mProgram == null || inputFormat.getTarget() != this.mTarget) {
            initProgram(context, inputFormat.getTarget());
        }
        updateParameters();
        this.mProgram.process(input, output);
        pushOutput(SliceItem.FORMAT_IMAGE, output);
        output.release();
    }

    public protected synchronized void updateParameters() {
        float[] first = {Color.red(this.mFirstColor) / 255.0f, Color.green(this.mFirstColor) / 255.0f, Color.blue(this.mFirstColor) / 255.0f};
        float[] second = {Color.red(this.mSecondColor) / 255.0f, Color.green(this.mSecondColor) / 255.0f, Color.blue(this.mSecondColor) / 255.0f};
        this.mProgram.setHostValue("first", first);
        this.mProgram.setHostValue("second", second);
    }
}
