package android.filterfw.core;
/* loaded from: classes.dex */
public abstract class Scheduler {
    private FilterGraph mGraph;

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract synchronized void reset();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract synchronized Filter scheduleNextNode();

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Scheduler(FilterGraph graph) {
        this.mGraph = graph;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized FilterGraph getGraph() {
        return this.mGraph;
    }

    synchronized boolean finished() {
        return true;
    }
}
