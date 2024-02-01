package android.filterpacks.base;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.GenerateFinalPort;
import android.provider.Telephony;
/* loaded from: classes.dex */
public class FrameSource extends Filter {
    @GenerateFinalPort(name = Telephony.CellBroadcasts.MESSAGE_FORMAT)
    public protected FrameFormat mFormat;
    @GenerateFieldPort(hasDefault = true, name = "frame")
    public protected Frame mFrame;
    @GenerateFieldPort(hasDefault = true, name = "repeatFrame")
    public protected boolean mRepeatFrame;

    private protected synchronized FrameSource(String name) {
        super(name);
        this.mFrame = null;
        this.mRepeatFrame = false;
    }

    private protected synchronized void setupPorts() {
        addOutputPort("frame", this.mFormat);
    }

    private protected synchronized void process(FilterContext context) {
        if (this.mFrame != null) {
            pushOutput("frame", this.mFrame);
        }
        if (!this.mRepeatFrame) {
            closeOutputPort("frame");
        }
    }
}
