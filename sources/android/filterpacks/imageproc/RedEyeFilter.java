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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
/* loaded from: classes.dex */
public class RedEyeFilter extends Filter {
    public protected static final float DEFAULT_RED_INTENSITY = 1.3f;
    public protected static final float MIN_RADIUS = 10.0f;
    public protected static final float RADIUS_RATIO = 0.06f;
    public protected final Canvas mCanvas;
    @GenerateFieldPort(name = "centers")
    public protected float[] mCenters;
    public protected int mHeight;
    public protected final Paint mPaint;
    public protected Program mProgram;
    public protected float mRadius;
    public protected Bitmap mRedEyeBitmap;
    public protected Frame mRedEyeFrame;
    public protected final String mRedEyeShader;
    public protected int mTarget;
    @GenerateFieldPort(hasDefault = true, name = "tile_size")
    public protected int mTileSize;
    public protected int mWidth;

    private protected synchronized RedEyeFilter(String name) {
        super(name);
        this.mTileSize = 640;
        this.mCanvas = new Canvas();
        this.mPaint = new Paint();
        this.mWidth = 0;
        this.mHeight = 0;
        this.mTarget = 0;
        this.mRedEyeShader = "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform float intensity;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  vec4 mask = texture2D(tex_sampler_1, v_texcoord);\n  if (mask.a > 0.0) {\n    float green_blue = color.g + color.b;\n    float red_intensity = color.r / green_blue;\n    if (red_intensity > intensity) {\n      color.r = 0.5 * green_blue;\n    }\n  }\n  gl_FragColor = color;\n}\n";
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
            ShaderProgram shaderProgram = new ShaderProgram(context, "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform float intensity;\nvarying vec2 v_texcoord;\nvoid main() {\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  vec4 mask = texture2D(tex_sampler_1, v_texcoord);\n  if (mask.a > 0.0) {\n    float green_blue = color.g + color.b;\n    float red_intensity = color.r / green_blue;\n    if (red_intensity > intensity) {\n      color.r = 0.5 * green_blue;\n    }\n  }\n  gl_FragColor = color;\n}\n");
            shaderProgram.setMaximumTileSize(this.mTileSize);
            this.mProgram = shaderProgram;
            this.mProgram.setHostValue("intensity", Float.valueOf(1.3f));
            this.mTarget = target;
            return;
        }
        throw new RuntimeException("Filter RedEye does not support frames of target " + target + "!");
    }

    private protected synchronized void process(FilterContext context) {
        Frame input = pullInput(SliceItem.FORMAT_IMAGE);
        FrameFormat inputFormat = input.getFormat();
        Frame output = context.getFrameManager().newFrame(inputFormat);
        if (this.mProgram == null || inputFormat.getTarget() != this.mTarget) {
            initProgram(context, inputFormat.getTarget());
        }
        if (inputFormat.getWidth() != this.mWidth || inputFormat.getHeight() != this.mHeight) {
            this.mWidth = inputFormat.getWidth();
            this.mHeight = inputFormat.getHeight();
        }
        createRedEyeFrame(context);
        Frame[] inputs = {input, this.mRedEyeFrame};
        this.mProgram.process(inputs, output);
        pushOutput(SliceItem.FORMAT_IMAGE, output);
        output.release();
        this.mRedEyeFrame.release();
        this.mRedEyeFrame = null;
    }

    private protected synchronized void fieldPortValueUpdated(String name, FilterContext context) {
        if (this.mProgram != null) {
            updateProgramParams();
        }
    }

    public protected synchronized void createRedEyeFrame(FilterContext context) {
        int bitmapWidth = this.mWidth / 2;
        int bitmapHeight = this.mHeight / 2;
        Bitmap redEyeBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        this.mCanvas.setBitmap(redEyeBitmap);
        this.mPaint.setColor(-1);
        this.mRadius = Math.max(10.0f, 0.06f * Math.min(bitmapWidth, bitmapHeight));
        for (int i = 0; i < this.mCenters.length; i += 2) {
            this.mCanvas.drawCircle(this.mCenters[i] * bitmapWidth, this.mCenters[i + 1] * bitmapHeight, this.mRadius, this.mPaint);
        }
        FrameFormat format = ImageFormat.create(bitmapWidth, bitmapHeight, 3, 3);
        this.mRedEyeFrame = context.getFrameManager().newFrame(format);
        this.mRedEyeFrame.setBitmap(redEyeBitmap);
        redEyeBitmap.recycle();
    }

    public protected synchronized void updateProgramParams() {
        if (this.mCenters.length % 2 == 1) {
            throw new RuntimeException("The size of center array must be even.");
        }
    }
}
