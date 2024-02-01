package android.filterpacks.imageproc;

import android.app.slice.SliceItem;
import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.GLFrame;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;
import android.filterfw.format.ObjectFormat;
import android.filterfw.geometry.Quad;
import android.opengl.GLES20;
/* loaded from: classes.dex */
public class DrawRectFilter extends Filter {
    @GenerateFieldPort(hasDefault = true, name = "colorBlue")
    public protected float mColorBlue;
    @GenerateFieldPort(hasDefault = true, name = "colorGreen")
    public protected float mColorGreen;
    @GenerateFieldPort(hasDefault = true, name = "colorRed")
    public protected float mColorRed;
    public protected final String mFixedColorFragmentShader;
    public protected ShaderProgram mProgram;
    public protected final String mVertexShader;

    private protected synchronized DrawRectFilter(String name) {
        super(name);
        this.mColorRed = 0.8f;
        this.mColorGreen = 0.8f;
        this.mColorBlue = 0.0f;
        this.mVertexShader = "attribute vec4 aPosition;\nvoid main() {\n  gl_Position = aPosition;\n}\n";
        this.mFixedColorFragmentShader = "precision mediump float;\nuniform vec4 color;\nvoid main() {\n  gl_FragColor = color;\n}\n";
    }

    private protected synchronized void setupPorts() {
        addMaskedInputPort(SliceItem.FORMAT_IMAGE, ImageFormat.create(3, 3));
        addMaskedInputPort("box", ObjectFormat.fromClass(Quad.class, 1));
        addOutputBasedOnInput(SliceItem.FORMAT_IMAGE, SliceItem.FORMAT_IMAGE);
    }

    private protected synchronized FrameFormat getOutputFormat(String portName, FrameFormat inputFormat) {
        return inputFormat;
    }

    private protected synchronized void prepare(FilterContext context) {
        this.mProgram = new ShaderProgram(context, "attribute vec4 aPosition;\nvoid main() {\n  gl_Position = aPosition;\n}\n", "precision mediump float;\nuniform vec4 color;\nvoid main() {\n  gl_FragColor = color;\n}\n");
    }

    private protected synchronized void process(FilterContext env) {
        Frame imageFrame = pullInput(SliceItem.FORMAT_IMAGE);
        Frame boxFrame = pullInput("box");
        Quad box = (Quad) boxFrame.getObjectValue();
        Quad box2 = box.scaled(2.0f).translated(-1.0f, -1.0f);
        GLFrame output = (GLFrame) env.getFrameManager().duplicateFrame(imageFrame);
        output.focus();
        renderBox(box2);
        pushOutput(SliceItem.FORMAT_IMAGE, output);
        output.release();
    }

    public protected synchronized void renderBox(Quad box) {
        float[] color = {this.mColorRed, this.mColorGreen, this.mColorBlue, 1.0f};
        float[] vertexValues = {box.p0.x, box.p0.y, box.p1.x, box.p1.y, box.p3.x, box.p3.y, box.p2.x, box.p2.y};
        this.mProgram.setHostValue("color", color);
        this.mProgram.setAttributeValues("aPosition", vertexValues, 2);
        this.mProgram.setVertexCount(4);
        this.mProgram.beginDrawing();
        GLES20.glLineWidth(1.0f);
        GLES20.glDrawArrays(2, 0, 4);
    }
}
