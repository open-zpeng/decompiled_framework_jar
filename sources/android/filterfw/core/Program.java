package android.filterfw.core;

import android.annotation.UnsupportedAppUsage;

/* loaded from: classes.dex */
public abstract class Program {
    public abstract Object getHostValue(String str);

    @UnsupportedAppUsage
    public abstract void process(Frame[] frameArr, Frame frame);

    @UnsupportedAppUsage
    public abstract void setHostValue(String str, Object obj);

    @UnsupportedAppUsage
    public void process(Frame input, Frame output) {
        Frame[] inputs = {input};
        process(inputs, output);
    }

    public void reset() {
    }
}
