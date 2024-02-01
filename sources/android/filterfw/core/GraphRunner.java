package android.filterfw.core;

import android.annotation.UnsupportedAppUsage;

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
        void onRunnerDone(int i);
    }

    public abstract void close();

    @UnsupportedAppUsage
    public abstract Exception getError();

    @UnsupportedAppUsage
    public abstract FilterGraph getGraph();

    public abstract boolean isRunning();

    @UnsupportedAppUsage
    public abstract void run();

    @UnsupportedAppUsage
    public abstract void setDoneCallback(OnRunnerDoneListener onRunnerDoneListener);

    @UnsupportedAppUsage
    public abstract void stop();

    public GraphRunner(FilterContext context) {
        this.mFilterContext = null;
        this.mFilterContext = context;
    }

    public FilterContext getContext() {
        return this.mFilterContext;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean activateGlContext() {
        GLEnvironment glEnv = this.mFilterContext.getGLEnvironment();
        if (glEnv != null && !glEnv.isActive()) {
            glEnv.activate();
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void deactivateGlContext() {
        GLEnvironment glEnv = this.mFilterContext.getGLEnvironment();
        if (glEnv != null) {
            glEnv.deactivate();
        }
    }
}
