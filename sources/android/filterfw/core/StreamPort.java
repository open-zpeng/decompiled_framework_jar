package android.filterfw.core;

/* loaded from: classes.dex */
public class StreamPort extends InputPort {
    private Frame mFrame;
    private boolean mPersistent;

    public StreamPort(Filter filter, String name) {
        super(filter, name);
    }

    @Override // android.filterfw.core.FilterPort
    public void clear() {
        Frame frame = this.mFrame;
        if (frame != null) {
            frame.release();
            this.mFrame = null;
        }
    }

    @Override // android.filterfw.core.FilterPort
    public void setFrame(Frame frame) {
        assignFrame(frame, true);
    }

    @Override // android.filterfw.core.FilterPort
    public void pushFrame(Frame frame) {
        assignFrame(frame, false);
    }

    protected synchronized void assignFrame(Frame frame, boolean persistent) {
        assertPortIsOpen();
        checkFrameType(frame, persistent);
        if (persistent) {
            if (this.mFrame != null) {
                this.mFrame.release();
            }
        } else if (this.mFrame != null) {
            throw new RuntimeException("Attempting to push more than one frame on port: " + this + "!");
        }
        this.mFrame = frame.retain();
        this.mFrame.markReadOnly();
        this.mPersistent = persistent;
    }

    @Override // android.filterfw.core.FilterPort
    public synchronized Frame pullFrame() {
        Frame result;
        if (this.mFrame == null) {
            throw new RuntimeException("No frame available to pull on port: " + this + "!");
        }
        result = this.mFrame;
        if (this.mPersistent) {
            this.mFrame.retain();
        } else {
            this.mFrame = null;
        }
        return result;
    }

    @Override // android.filterfw.core.FilterPort
    public synchronized boolean hasFrame() {
        return this.mFrame != null;
    }

    @Override // android.filterfw.core.FilterPort
    public String toString() {
        return "input " + super.toString();
    }

    @Override // android.filterfw.core.InputPort
    public synchronized void transfer(FilterContext context) {
        if (this.mFrame != null) {
            checkFrameManager(this.mFrame, context);
        }
    }
}
