package android.filterpacks.imageproc;

import android.filterfw.core.FilterContext;
import android.filterfw.core.NativeProgram;
import android.filterfw.core.Program;
import android.filterfw.core.ShaderProgram;
/* loaded from: classes.dex */
public class BrightnessFilter extends SimpleImageFilter {
    public protected static final String mBrightnessShader = "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform float brightness;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  gl_FragColor = brightness * color;\n}\n";

    private protected synchronized BrightnessFilter(String name) {
        super(name, "brightness");
    }

    public private synchronized Program getNativeProgram(FilterContext context) {
        return new NativeProgram("filterpack_imageproc", "brightness");
    }

    public private synchronized Program getShaderProgram(FilterContext context) {
        return new ShaderProgram(context, mBrightnessShader);
    }
}
