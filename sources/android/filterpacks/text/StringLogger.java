package android.filterpacks.text;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.format.ObjectFormat;
import android.util.Log;
/* loaded from: classes.dex */
public class StringLogger extends Filter {
    private protected synchronized StringLogger(String name) {
        super(name);
    }

    private protected synchronized void setupPorts() {
        addMaskedInputPort("string", ObjectFormat.fromClass(Object.class, 1));
    }

    private protected synchronized void process(FilterContext env) {
        Frame input = pullInput("string");
        String inputString = input.getObjectValue().toString();
        Log.i("StringLogger", inputString);
    }
}
