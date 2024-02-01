package android.filterfw.core;
/* loaded from: classes.dex */
public abstract class FrameManager {
    private FilterContext mContext;

    /* JADX INFO: Access modifiers changed from: private */
    public abstract Frame newBoundFrame(FrameFormat frameFormat, int i, long j);

    /* JADX INFO: Access modifiers changed from: private */
    public abstract Frame newFrame(FrameFormat frameFormat);

    public abstract synchronized Frame releaseFrame(Frame frame);

    public abstract synchronized Frame retainFrame(Frame frame);

    /* JADX INFO: Access modifiers changed from: private */
    public Frame duplicateFrame(Frame frame) {
        Frame result = newFrame(frame.getFormat());
        result.setDataFromFrame(frame);
        return result;
    }

    public synchronized Frame duplicateFrameToTarget(Frame frame, int newTarget) {
        MutableFrameFormat newFormat = frame.getFormat().mutableCopy();
        newFormat.setTarget(newTarget);
        Frame result = newFrame(newFormat);
        result.setDataFromFrame(frame);
        return result;
    }

    public synchronized FilterContext getContext() {
        return this.mContext;
    }

    public synchronized GLEnvironment getGLEnvironment() {
        if (this.mContext != null) {
            return this.mContext.getGLEnvironment();
        }
        return null;
    }

    public synchronized void tearDown() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setContext(FilterContext context) {
        this.mContext = context;
    }
}
