package android.filterpacks.base;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
/* loaded from: classes.dex */
public class NullFilter extends Filter {
    /* JADX INFO: Access modifiers changed from: private */
    public synchronized NullFilter(String name) {
        super(name);
    }

    private protected synchronized void setupPorts() {
        addInputPort("frame");
    }

    private protected synchronized void process(FilterContext context) {
        pullInput("frame");
    }
}
