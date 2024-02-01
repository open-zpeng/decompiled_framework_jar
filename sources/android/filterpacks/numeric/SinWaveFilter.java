package android.filterpacks.numeric;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.format.ObjectFormat;
/* loaded from: classes.dex */
public class SinWaveFilter extends Filter {
    public protected FrameFormat mOutputFormat;
    @GenerateFieldPort(hasDefault = true, name = "stepSize")
    public protected float mStepSize;
    public protected float mValue;

    private protected synchronized SinWaveFilter(String name) {
        super(name);
        this.mStepSize = 0.05f;
        this.mValue = 0.0f;
    }

    private protected synchronized void setupPorts() {
        this.mOutputFormat = ObjectFormat.fromClass(Float.class, 1);
        addOutputPort("value", this.mOutputFormat);
    }

    private protected synchronized void open(FilterContext env) {
        this.mValue = 0.0f;
    }

    private protected synchronized void process(FilterContext env) {
        Frame output = env.getFrameManager().newFrame(this.mOutputFormat);
        output.setObjectValue(Float.valueOf((((float) Math.sin(this.mValue)) + 1.0f) / 2.0f));
        pushOutput("value", output);
        this.mValue += this.mStepSize;
        output.release();
    }
}
