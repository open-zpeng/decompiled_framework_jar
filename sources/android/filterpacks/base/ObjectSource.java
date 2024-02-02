package android.filterpacks.base;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.GenerateFinalPort;
import android.filterfw.format.ObjectFormat;
import android.provider.Telephony;
/* loaded from: classes.dex */
public class ObjectSource extends Filter {
    public protected Frame mFrame;
    @GenerateFieldPort(name = "object")
    public protected Object mObject;
    @GenerateFinalPort(hasDefault = true, name = Telephony.CellBroadcasts.MESSAGE_FORMAT)
    public protected FrameFormat mOutputFormat;
    @GenerateFieldPort(hasDefault = true, name = "repeatFrame")
    public private protected boolean mRepeatFrame;

    private protected synchronized ObjectSource(String name) {
        super(name);
        this.mOutputFormat = FrameFormat.unspecified();
        this.mRepeatFrame = false;
    }

    private protected synchronized void setupPorts() {
        addOutputPort("frame", this.mOutputFormat);
    }

    private protected synchronized void process(FilterContext context) {
        if (this.mFrame == null) {
            if (this.mObject == null) {
                throw new NullPointerException("ObjectSource producing frame with no object set!");
            }
            FrameFormat outputFormat = ObjectFormat.fromObject(this.mObject, 1);
            this.mFrame = context.getFrameManager().newFrame(outputFormat);
            this.mFrame.setObjectValue(this.mObject);
            this.mFrame.setTimestamp(-1L);
        }
        pushOutput("frame", this.mFrame);
        if (!this.mRepeatFrame) {
            closeOutputPort("frame");
        }
    }

    private protected synchronized void tearDown(FilterContext context) {
        this.mFrame.release();
    }

    private protected synchronized void fieldPortValueUpdated(String name, FilterContext context) {
        if (name.equals("object") && this.mFrame != null) {
            this.mFrame.release();
            this.mFrame = null;
        }
    }
}
