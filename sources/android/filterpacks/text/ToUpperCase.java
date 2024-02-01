package android.filterpacks.text;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.format.ObjectFormat;
import java.util.Locale;
/* loaded from: classes.dex */
public class ToUpperCase extends Filter {
    public protected FrameFormat mOutputFormat;

    private protected synchronized ToUpperCase(String name) {
        super(name);
    }

    private protected synchronized void setupPorts() {
        this.mOutputFormat = ObjectFormat.fromClass(String.class, 1);
        addMaskedInputPort("mixedcase", this.mOutputFormat);
        addOutputPort("uppercase", this.mOutputFormat);
    }

    private protected synchronized void process(FilterContext env) {
        Frame input = pullInput("mixedcase");
        String inputString = (String) input.getObjectValue();
        Frame output = env.getFrameManager().newFrame(this.mOutputFormat);
        output.setObjectValue(inputString.toUpperCase(Locale.getDefault()));
        pushOutput("uppercase", output);
    }
}
