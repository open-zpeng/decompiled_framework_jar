package android.filterfw.core;
/* loaded from: classes.dex */
public abstract class InputPort extends FilterPort {
    protected OutputPort mSourcePort;

    public abstract synchronized void transfer(FilterContext filterContext);

    public synchronized InputPort(Filter filter, String name) {
        super(filter, name);
    }

    public synchronized void setSourcePort(OutputPort source) {
        if (this.mSourcePort != null) {
            throw new RuntimeException(this + " already connected to " + this.mSourcePort + "!");
        }
        this.mSourcePort = source;
    }

    public synchronized boolean isConnected() {
        return this.mSourcePort != null;
    }

    @Override // android.filterfw.core.FilterPort
    public synchronized void open() {
        super.open();
        if (this.mSourcePort != null && !this.mSourcePort.isOpen()) {
            this.mSourcePort.open();
        }
    }

    @Override // android.filterfw.core.FilterPort
    public synchronized void close() {
        if (this.mSourcePort != null && this.mSourcePort.isOpen()) {
            this.mSourcePort.close();
        }
        super.close();
    }

    public synchronized OutputPort getSourcePort() {
        return this.mSourcePort;
    }

    public synchronized Filter getSourceFilter() {
        if (this.mSourcePort == null) {
            return null;
        }
        return this.mSourcePort.getFilter();
    }

    public synchronized FrameFormat getSourceFormat() {
        return this.mSourcePort != null ? this.mSourcePort.getPortFormat() : getPortFormat();
    }

    public synchronized Object getTarget() {
        return null;
    }

    @Override // android.filterfw.core.FilterPort
    public synchronized boolean filterMustClose() {
        return (isOpen() || !isBlocking() || hasFrame()) ? false : true;
    }

    @Override // android.filterfw.core.FilterPort
    public synchronized boolean isReady() {
        return hasFrame() || !isBlocking();
    }

    public synchronized boolean acceptsFrame() {
        return !hasFrame();
    }
}
