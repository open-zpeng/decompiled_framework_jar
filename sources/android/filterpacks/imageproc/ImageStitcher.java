package android.filterpacks.imageproc;

import android.app.slice.SliceItem;
import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.MutableFrameFormat;
import android.filterfw.core.Program;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;
/* loaded from: classes.dex */
public class ImageStitcher extends Filter {
    public protected int mImageHeight;
    public protected int mImageWidth;
    public protected int mInputHeight;
    public protected int mInputWidth;
    public protected Frame mOutputFrame;
    @GenerateFieldPort(name = "padSize")
    public protected int mPadSize;
    public protected Program mProgram;
    public protected int mSliceHeight;
    public protected int mSliceIndex;
    public protected int mSliceWidth;
    @GenerateFieldPort(name = "xSlices")
    public protected int mXSlices;
    @GenerateFieldPort(name = "ySlices")
    public protected int mYSlices;

    private protected synchronized ImageStitcher(String name) {
        super(name);
        this.mSliceIndex = 0;
    }

    private protected synchronized void setupPorts() {
        addMaskedInputPort(SliceItem.FORMAT_IMAGE, ImageFormat.create(3, 3));
        addOutputBasedOnInput(SliceItem.FORMAT_IMAGE, SliceItem.FORMAT_IMAGE);
    }

    private protected synchronized FrameFormat getOutputFormat(String portName, FrameFormat inputFormat) {
        return inputFormat;
    }

    public protected synchronized FrameFormat calcOutputFormatForInput(FrameFormat format) {
        MutableFrameFormat outputFormat = format.mutableCopy();
        this.mInputWidth = format.getWidth();
        this.mInputHeight = format.getHeight();
        this.mSliceWidth = this.mInputWidth - (this.mPadSize * 2);
        this.mSliceHeight = this.mInputHeight - (2 * this.mPadSize);
        this.mImageWidth = this.mSliceWidth * this.mXSlices;
        this.mImageHeight = this.mSliceHeight * this.mYSlices;
        outputFormat.setDimensions(this.mImageWidth, this.mImageHeight);
        return outputFormat;
    }

    private protected synchronized void process(FilterContext context) {
        Frame input = pullInput(SliceItem.FORMAT_IMAGE);
        FrameFormat format = input.getFormat();
        if (this.mSliceIndex == 0) {
            this.mOutputFrame = context.getFrameManager().newFrame(calcOutputFormatForInput(format));
        } else if (format.getWidth() != this.mInputWidth || format.getHeight() != this.mInputHeight) {
            throw new RuntimeException("Image size should not change.");
        }
        if (this.mProgram == null) {
            this.mProgram = ShaderProgram.createIdentity(context);
        }
        float x0 = this.mPadSize / this.mInputWidth;
        float y0 = this.mPadSize / this.mInputHeight;
        int outputOffsetX = (this.mSliceIndex % this.mXSlices) * this.mSliceWidth;
        int outputOffsetY = (this.mSliceIndex / this.mXSlices) * this.mSliceHeight;
        float outputWidth = Math.min(this.mSliceWidth, this.mImageWidth - outputOffsetX);
        float outputHeight = Math.min(this.mSliceHeight, this.mImageHeight - outputOffsetY);
        ((ShaderProgram) this.mProgram).setSourceRect(x0, y0, outputWidth / this.mInputWidth, outputHeight / this.mInputHeight);
        ((ShaderProgram) this.mProgram).setTargetRect(outputOffsetX / this.mImageWidth, outputOffsetY / this.mImageHeight, outputWidth / this.mImageWidth, outputHeight / this.mImageHeight);
        this.mProgram.process(input, this.mOutputFrame);
        this.mSliceIndex++;
        if (this.mSliceIndex == this.mXSlices * this.mYSlices) {
            pushOutput(SliceItem.FORMAT_IMAGE, this.mOutputFrame);
            this.mOutputFrame.release();
            this.mSliceIndex = 0;
        }
    }
}
