package android.filterfw.core;
/* loaded from: classes.dex */
public class OutputPort extends FilterPort {
    protected InputPort mBasePort;
    protected InputPort mTargetPort;

    public synchronized OutputPort(Filter filter, String name) {
        super(filter, name);
    }

    public synchronized void connectTo(InputPort target) {
        if (this.mTargetPort != null) {
            throw new RuntimeException(this + " already connected to " + this.mTargetPort + "!");
        }
        this.mTargetPort = target;
        this.mTargetPort.setSourcePort(this);
    }

    public synchronized boolean isConnected() {
        return this.mTargetPort != null;
    }

    @Override // android.filterfw.core.FilterPort
    public synchronized void open() {
        super.open();
        if (this.mTargetPort != null && !this.mTargetPort.isOpen()) {
            this.mTargetPort.open();
        }
    }

    @Override // android.filterfw.core.FilterPort
    public synchronized void close() {
        super.close();
        if (this.mTargetPort != null && this.mTargetPort.isOpen()) {
            this.mTargetPort.close();
        }
    }

    public synchronized InputPort getTargetPort() {
        return this.mTargetPort;
    }

    public synchronized Filter getTargetFilter() {
        if (this.mTargetPort == null) {
            return null;
        }
        return this.mTargetPort.getFilter();
    }

    public synchronized void setBasePort(InputPort basePort) {
        this.mBasePort = basePort;
    }

    public synchronized InputPort getBasePort() {
        return this.mBasePort;
    }

    @Override // android.filterfw.core.FilterPort
    public synchronized boolean filterMustClose() {
        return !isOpen() && isBlocking();
    }

    @Override // android.filterfw.core.FilterPort
    public synchronized boolean isReady() {
        return (isOpen() && this.mTargetPort.acceptsFrame()) || !isBlocking();
    }

    @Override // android.filterfw.core.FilterPort
    public synchronized void clear() {
        if (this.mTargetPort != null) {
            this.mTargetPort.clear();
        }
    }

    @Override // android.filterfw.core.FilterPort
    public synchronized void pushFrame(Frame frame) {
        if (this.mTargetPort == null) {
            throw new RuntimeException("Attempting to push frame on unconnected port: " + this + "!");
        }
        this.mTargetPort.pushFrame(frame);
    }

    @Override // android.filterfw.core.FilterPort
    public synchronized void setFrame(Frame frame) {
        assertPortIsOpen();
        if (this.mTargetPort == null) {
            throw new RuntimeException("Attempting to set frame on unconnected port: " + this + "!");
        }
        this.mTargetPort.setFrame(frame);
    }

    @Override // android.filterfw.core.FilterPort
    public synchronized Frame pullFrame() {
        throw new RuntimeException("Cannot pull frame on " + this + "!");
    }

    @Override // android.filterfw.core.FilterPort
    public synchronized boolean hasFrame() {
        if (this.mTargetPort == null) {
            return false;
        }
        return this.mTargetPort.hasFrame();
    }

    @Override // android.filterfw.core.FilterPort
    public String toString() {
        return "output " + super.toString();
    }
}
