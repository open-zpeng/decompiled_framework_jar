package android.filterfw.core;
/* loaded from: classes.dex */
public abstract class GraphRunner {
    public static final int RESULT_BLOCKED = 4;
    public static final int RESULT_ERROR = 6;
    public static final int RESULT_FINISHED = 2;
    public static final int RESULT_RUNNING = 1;
    public static final int RESULT_SLEEPING = 3;
    public static final int RESULT_STOPPED = 5;
    public static final int RESULT_UNKNOWN = 0;
    protected FilterContext mFilterContext;

    /* loaded from: classes.dex */
    public interface OnRunnerDoneListener {
        synchronized void onRunnerDone(int i);
    }

    public abstract synchronized void close();

    private protected abstract Exception getError();

    private protected abstract FilterGraph getGraph();

    public abstract synchronized boolean isRunning();

    /* JADX INFO: Access modifiers changed from: private */
    public abstract void run();

    private protected abstract void setDoneCallback(OnRunnerDoneListener onRunnerDoneListener);

    private protected abstract void stop();

    public synchronized GraphRunner(FilterContext context) {
        this.mFilterContext = null;
        this.mFilterContext = context;
    }

    public synchronized FilterContext getContext() {
        return this.mFilterContext;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized boolean activateGlContext() {
        GLEnvironment glEnv = this.mFilterContext.getGLEnvironment();
        if (glEnv != null && !glEnv.isActive()) {
            glEnv.activate();
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized void deactivateGlContext() {
        GLEnvironment glEnv = this.mFilterContext.getGLEnvironment();
        if (glEnv != null) {
            glEnv.deactivate();
        }
    }
}
