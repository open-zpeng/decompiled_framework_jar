package android.filterpacks.base;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.GenerateFinalPort;
import android.provider.Telephony;
/* loaded from: classes.dex */
public class FrameFetch extends Filter {
    @GenerateFinalPort(hasDefault = true, name = Telephony.CellBroadcasts.MESSAGE_FORMAT)
    public protected FrameFormat mFormat;
    @GenerateFieldPort(name = "key")
    public protected String mKey;
    @GenerateFieldPort(hasDefault = true, name = "repeatFrame")
    public protected boolean mRepeatFrame;

    private protected synchronized FrameFetch(String name) {
        super(name);
        this.mRepeatFrame = false;
    }

    private protected synchronized void setupPorts() {
        addOutputPort("frame", this.mFormat == null ? FrameFormat.unspecified() : this.mFormat);
    }

    private protected synchronized void process(FilterContext context) {
        Frame output = context.fetchFrame(this.mKey);
        if (output != null) {
            pushOutput("frame", output);
            if (!this.mRepeatFrame) {
                closeOutputPort("frame");
                return;
            }
            return;
        }
        delayNextProcess(250);
    }
}
