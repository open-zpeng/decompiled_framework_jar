package android.filterpacks.base;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.GenerateFinalPort;
/* loaded from: classes.dex */
public class FrameBranch extends Filter {
    @GenerateFinalPort(hasDefault = true, name = "outputs")
    public protected int mNumberOfOutputs;

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized FrameBranch(String name) {
        super(name);
        this.mNumberOfOutputs = 2;
    }

    private protected synchronized void setupPorts() {
        addInputPort("in");
        for (int i = 0; i < this.mNumberOfOutputs; i++) {
            addOutputBasedOnInput("out" + i, "in");
        }
    }

    private protected synchronized FrameFormat getOutputFormat(String portName, FrameFormat inputFormat) {
        return inputFormat;
    }

    private protected synchronized void process(FilterContext context) {
        Frame input = pullInput("in");
        for (int i = 0; i < this.mNumberOfOutputs; i++) {
            pushOutput("out" + i, input);
        }
    }
}
