package android.filterpacks.text;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.format.ObjectFormat;
/* loaded from: classes.dex */
public class StringSource extends Filter {
    public protected FrameFormat mOutputFormat;
    @GenerateFieldPort(name = "stringValue")
    public protected String mString;

    private protected synchronized StringSource(String name) {
        super(name);
    }

    private protected synchronized void setupPorts() {
        this.mOutputFormat = ObjectFormat.fromClass(String.class, 1);
        addOutputPort("string", this.mOutputFormat);
    }

    private protected synchronized void process(FilterContext env) {
        Frame output = env.getFrameManager().newFrame(this.mOutputFormat);
        output.setObjectValue(this.mString);
        output.setTimestamp(-1L);
        pushOutput("string", output);
        closeOutputPort("string");
    }
}
