package android.filterfw.core;
/* loaded from: classes.dex */
public class SimpleScheduler extends Scheduler {
    public synchronized SimpleScheduler(FilterGraph graph) {
        super(graph);
    }

    @Override // android.filterfw.core.Scheduler
    public synchronized void reset() {
    }

    @Override // android.filterfw.core.Scheduler
    public synchronized Filter scheduleNextNode() {
        for (Filter filter : getGraph().getFilters()) {
            if (filter.canProcess()) {
                return filter;
            }
        }
        return null;
    }
}
