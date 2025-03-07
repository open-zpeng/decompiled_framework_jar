package android.filterfw;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.filterfw.core.AsyncRunner;
import android.filterfw.core.FilterContext;
import android.filterfw.core.FilterGraph;
import android.filterfw.core.FrameManager;
import android.filterfw.core.GraphRunner;
import android.filterfw.core.RoundRobinScheduler;
import android.filterfw.core.SyncRunner;
import android.filterfw.io.GraphIOException;
import android.filterfw.io.GraphReader;
import android.filterfw.io.TextGraphReader;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class GraphEnvironment extends MffEnvironment {
    public static final int MODE_ASYNCHRONOUS = 1;
    public static final int MODE_SYNCHRONOUS = 2;
    private GraphReader mGraphReader;
    private ArrayList<GraphHandle> mGraphs;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class GraphHandle {
        private AsyncRunner mAsyncRunner;
        private FilterGraph mGraph;
        private SyncRunner mSyncRunner;

        public GraphHandle(FilterGraph graph) {
            this.mGraph = graph;
        }

        public FilterGraph getGraph() {
            return this.mGraph;
        }

        public AsyncRunner getAsyncRunner(FilterContext environment) {
            if (this.mAsyncRunner == null) {
                this.mAsyncRunner = new AsyncRunner(environment, RoundRobinScheduler.class);
                this.mAsyncRunner.setGraph(this.mGraph);
            }
            return this.mAsyncRunner;
        }

        public GraphRunner getSyncRunner(FilterContext environment) {
            if (this.mSyncRunner == null) {
                this.mSyncRunner = new SyncRunner(environment, this.mGraph, RoundRobinScheduler.class);
            }
            return this.mSyncRunner;
        }
    }

    @UnsupportedAppUsage
    public GraphEnvironment() {
        super(null);
        this.mGraphs = new ArrayList<>();
    }

    public GraphEnvironment(FrameManager frameManager, GraphReader reader) {
        super(frameManager);
        this.mGraphs = new ArrayList<>();
        this.mGraphReader = reader;
    }

    public GraphReader getGraphReader() {
        if (this.mGraphReader == null) {
            this.mGraphReader = new TextGraphReader();
        }
        return this.mGraphReader;
    }

    @UnsupportedAppUsage
    public void addReferences(Object... references) {
        getGraphReader().addReferencesByKeysAndValues(references);
    }

    @UnsupportedAppUsage
    public int loadGraph(Context context, int resourceId) {
        try {
            FilterGraph graph = getGraphReader().readGraphResource(context, resourceId);
            return addGraph(graph);
        } catch (GraphIOException e) {
            throw new RuntimeException("Could not read graph: " + e.getMessage());
        }
    }

    public int addGraph(FilterGraph graph) {
        GraphHandle graphHandle = new GraphHandle(graph);
        this.mGraphs.add(graphHandle);
        return this.mGraphs.size() - 1;
    }

    public FilterGraph getGraph(int graphId) {
        if (graphId < 0 || graphId >= this.mGraphs.size()) {
            throw new IllegalArgumentException("Invalid graph ID " + graphId + " specified in runGraph()!");
        }
        return this.mGraphs.get(graphId).getGraph();
    }

    @UnsupportedAppUsage
    public GraphRunner getRunner(int graphId, int executionMode) {
        if (executionMode != 1) {
            if (executionMode == 2) {
                return this.mGraphs.get(graphId).getSyncRunner(getContext());
            }
            throw new RuntimeException("Invalid execution mode " + executionMode + " specified in getRunner()!");
        }
        return this.mGraphs.get(graphId).getAsyncRunner(getContext());
    }
}
