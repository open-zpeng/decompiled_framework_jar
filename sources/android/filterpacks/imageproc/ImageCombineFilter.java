package android.filterpacks.imageproc;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.Program;
import android.filterfw.format.ImageFormat;
import java.lang.reflect.Field;
/* loaded from: classes.dex */
public abstract class ImageCombineFilter extends Filter {
    public private int mCurrentTarget;
    public private String[] mInputNames;
    public private String mOutputName;
    public private String mParameterName;
    public private Program mProgram;

    public private abstract synchronized Program getNativeProgram(FilterContext filterContext);

    public private abstract synchronized Program getShaderProgram(FilterContext filterContext);

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized ImageCombineFilter(String name, String[] inputNames, String outputName, String parameterName) {
        super(name);
        this.mCurrentTarget = 0;
        this.mInputNames = inputNames;
        this.mOutputName = outputName;
        this.mParameterName = parameterName;
    }

    private protected synchronized void setupPorts() {
        String[] strArr;
        if (this.mParameterName != null) {
            try {
                Field programField = ImageCombineFilter.class.getDeclaredField("mProgram");
                addProgramPort(this.mParameterName, this.mParameterName, programField, Float.TYPE, false);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Internal Error: mProgram field not found!");
            }
        }
        for (String inputName : this.mInputNames) {
            addMaskedInputPort(inputName, ImageFormat.create(3));
        }
        addOutputBasedOnInput(this.mOutputName, this.mInputNames[0]);
    }

    private protected synchronized FrameFormat getOutputFormat(String portName, FrameFormat inputFormat) {
        return inputFormat;
    }

    public protected synchronized void assertAllInputTargetsMatch() {
        String[] strArr;
        int target = getInputFormat(this.mInputNames[0]).getTarget();
        for (String inputName : this.mInputNames) {
            if (target != getInputFormat(inputName).getTarget()) {
                throw new RuntimeException("Type mismatch of input formats in filter " + this + ". All input frames must have the same target!");
            }
        }
    }

    private protected synchronized void process(FilterContext context) {
        Frame[] inputs = new Frame[this.mInputNames.length];
        String[] strArr = this.mInputNames;
        int length = strArr.length;
        int i = 0;
        int i2 = 0;
        while (i2 < length) {
            String inputName = strArr[i2];
            inputs[i] = pullInput(inputName);
            i2++;
            i++;
        }
        Frame output = context.getFrameManager().newFrame(inputs[0].getFormat());
        updateProgramWithTarget(inputs[0].getFormat().getTarget(), context);
        this.mProgram.process(inputs, output);
        pushOutput(this.mOutputName, output);
        output.release();
    }

    public private synchronized void updateProgramWithTarget(int target, FilterContext context) {
        if (target != this.mCurrentTarget) {
            switch (target) {
                case 2:
                    this.mProgram = getNativeProgram(context);
                    break;
                case 3:
                    this.mProgram = getShaderProgram(context);
                    break;
                default:
                    this.mProgram = null;
                    break;
            }
            if (this.mProgram == null) {
                throw new RuntimeException("Could not create a program for image filter " + this + "!");
            }
            initProgramInputs(this.mProgram, context);
            this.mCurrentTarget = target;
        }
    }
}
