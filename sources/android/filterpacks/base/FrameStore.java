package android.filterpacks.base;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.GenerateFieldPort;
/* loaded from: classes.dex */
public class FrameStore extends Filter {
    @GenerateFieldPort(name = "key")
    public protected String mKey;

    private protected synchronized FrameStore(String name) {
        super(name);
    }

    private protected synchronized void setupPorts() {
        addInputPort("frame");
    }

    private protected synchronized void process(FilterContext context) {
        Frame input = pullInput("frame");
        context.storeFrame(this.mKey, input);
    }
}
