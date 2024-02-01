package android.filterpacks.base;

import android.app.Instrumentation;
import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.GenerateFieldPort;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class OutputStreamTarget extends Filter {
    @GenerateFieldPort(name = Instrumentation.REPORT_KEY_STREAMRESULT)
    public protected OutputStream mOutputStream;

    private protected synchronized OutputStreamTarget(String name) {
        super(name);
    }

    private protected synchronized void setupPorts() {
        addInputPort("data");
    }

    private protected synchronized void process(FilterContext context) {
        ByteBuffer data;
        Frame input = pullInput("data");
        if (input.getFormat().getObjectClass() == String.class) {
            String stringVal = (String) input.getObjectValue();
            data = ByteBuffer.wrap(stringVal.getBytes());
        } else {
            data = input.getData();
        }
        try {
            this.mOutputStream.write(data.array(), 0, data.limit());
            this.mOutputStream.flush();
        } catch (IOException exception) {
            throw new RuntimeException("OutputStreamTarget: Could not write to stream: " + exception.getMessage() + "!");
        }
    }
}
