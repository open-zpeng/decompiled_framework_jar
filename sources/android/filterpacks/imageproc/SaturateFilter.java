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
public class SaturateFilter extends Filter {
    public protected Program mBenProgram;
    public protected final String mBenSaturateShader;
    public protected Program mHerfProgram;
    public protected final String mHerfSaturateShader;
    @GenerateFieldPort(hasDefault = true, name = BatteryManager.EXTRA_SCALE)
    public protected float mScale;
    public protected int mTarget;
    @GenerateFieldPort(hasDefault = true, name = "tile_size")
    public protected int mTileSize;

    private protected synchronized SaturateFilter(String name) {
        super(name);
        this.mScale = 0.0f;
        this.mTileSize = 640;
        this.mTarget = 0;
        this.mBenSaturateShader = "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform float scale;\nuniform float shift;\nuniform vec3 weights;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  float kv = dot(color.rgb, weights) + shift;\n  vec3 new_color = scale * color.rgb + (1.0 - scale) * kv;\n  gl_FragColor = vec4(new_color, color.a);\n}\n";
        this.mHerfSaturateShader = "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform vec3 weights;\nuniform vec3 exponents;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  float de = dot(color.rgb, weights);\n  float inv_de = 1.0 / de;\n  vec3 new_color = de * pow(color.rgb * inv_de, exponents);\n  float max_color = max(max(max(new_color.r, new_color.g), new_color.b), 1.0);\n  gl_FragColor = vec4(new_color / max_color, color.a);\n}\n";
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
            ShaderProgram shaderProgram = new ShaderProgram(context, "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform float scale;\nuniform float shift;\nuniform vec3 weights;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  float kv = dot(color.rgb, weights) + shift;\n  vec3 new_color = scale * color.rgb + (1.0 - scale) * kv;\n  gl_FragColor = vec4(new_color, color.a);\n}\n");
            shaderProgram.setMaximumTileSize(this.mTileSize);
            this.mBenProgram = shaderProgram;
            ShaderProgram shaderProgram2 = new ShaderProgram(context, "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform vec3 weights;\nuniform vec3 exponents;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  float de = dot(color.rgb, weights);\n  float inv_de = 1.0 / de;\n  vec3 new_color = de * pow(color.rgb * inv_de, exponents);\n  float max_color = max(max(max(new_color.r, new_color.g), new_color.b), 1.0);\n  gl_FragColor = vec4(new_color / max_color, color.a);\n}\n");
            shaderProgram2.setMaximumTileSize(this.mTileSize);
            this.mHerfProgram = shaderProgram2;
            this.mTarget = target;
            return;
        }
        throw new RuntimeException("Filter Sharpen does not support frames of target " + target + "!");
    }

    private protected synchronized void fieldPortValueUpdated(String name, FilterContext context) {
        if (this.mBenProgram != null && this.mHerfProgram != null) {
            updateParameters();
        }
    }

    private protected synchronized void process(FilterContext context) {
        Frame input = pullInput(SliceItem.FORMAT_IMAGE);
        FrameFormat inputFormat = input.getFormat();
        if (this.mBenProgram == null || inputFormat.getTarget() != this.mTarget) {
            initProgram(context, inputFormat.getTarget());
            initParameters();
        }
        Frame output = context.getFrameManager().newFrame(inputFormat);
        if (this.mScale > 0.0f) {
            this.mHerfProgram.process(input, output);
        } else {
            this.mBenProgram.process(input, output);
        }
        pushOutput(SliceItem.FORMAT_IMAGE, output);
        output.release();
    }

    public protected synchronized void initParameters() {
        float[] weights = {0.25f, 0.625f, 0.125f};
        this.mBenProgram.setHostValue("weights", weights);
        this.mBenProgram.setHostValue("shift", Float.valueOf(0.003921569f));
        this.mHerfProgram.setHostValue("weights", weights);
        updateParameters();
    }

    public protected synchronized void updateParameters() {
        if (this.mScale > 0.0f) {
            float[] exponents = {(0.9f * this.mScale) + 1.0f, (2.1f * this.mScale) + 1.0f, (2.7f * this.mScale) + 1.0f};
            this.mHerfProgram.setHostValue("exponents", exponents);
            return;
        }
        this.mBenProgram.setHostValue(BatteryManager.EXTRA_SCALE, Float.valueOf(1.0f + this.mScale));
    }
}
