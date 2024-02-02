package android.filterfw.core;

import android.util.Log;
/* loaded from: classes.dex */
public abstract class FilterPort {
    private static final String TAG = "FilterPort";
    protected Filter mFilter;
    protected String mName;
    protected FrameFormat mPortFormat;
    protected boolean mIsBlocking = true;
    protected boolean mIsOpen = false;
    protected boolean mChecksType = false;
    private boolean mLogVerbose = Log.isLoggable(TAG, 2);

    public abstract synchronized void clear();

    public abstract synchronized boolean filterMustClose();

    public abstract synchronized boolean hasFrame();

    public abstract synchronized boolean isReady();

    public abstract synchronized Frame pullFrame();

    public abstract synchronized void pushFrame(Frame frame);

    public abstract synchronized void setFrame(Frame frame);

    public synchronized FilterPort(Filter filter, String name) {
        this.mName = name;
        this.mFilter = filter;
    }

    public synchronized boolean isAttached() {
        return this.mFilter != null;
    }

    public synchronized FrameFormat getPortFormat() {
        return this.mPortFormat;
    }

    public synchronized void setPortFormat(FrameFormat format) {
        this.mPortFormat = format;
    }

    public synchronized Filter getFilter() {
        return this.mFilter;
    }

    public synchronized String getName() {
        return this.mName;
    }

    public synchronized void setBlocking(boolean blocking) {
        this.mIsBlocking = blocking;
    }

    public synchronized void setChecksType(boolean checksType) {
        this.mChecksType = checksType;
    }

    public synchronized void open() {
        if (!this.mIsOpen && this.mLogVerbose) {
            Log.v(TAG, "Opening " + this);
        }
        this.mIsOpen = true;
    }

    public synchronized void close() {
        if (this.mIsOpen && this.mLogVerbose) {
            Log.v(TAG, "Closing " + this);
        }
        this.mIsOpen = false;
    }

    public synchronized boolean isOpen() {
        return this.mIsOpen;
    }

    public synchronized boolean isBlocking() {
        return this.mIsBlocking;
    }

    public String toString() {
        return "port '" + this.mName + "' of " + this.mFilter;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void assertPortIsOpen() {
        if (!isOpen()) {
            throw new RuntimeException("Illegal operation on closed " + this + "!");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void checkFrameType(Frame frame, boolean forceCheck) {
        if ((this.mChecksType || forceCheck) && this.mPortFormat != null && !frame.getFormat().isCompatibleWith(this.mPortFormat)) {
            throw new RuntimeException("Frame passed to " + this + " is of incorrect type! Expected " + this.mPortFormat + " but got " + frame.getFormat());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void checkFrameManager(Frame frame, FilterContext context) {
        if (frame.getFrameManager() != null && frame.getFrameManager() != context.getFrameManager()) {
            throw new RuntimeException("Frame " + frame + " is managed by foreign FrameManager! ");
        }
    }
}
