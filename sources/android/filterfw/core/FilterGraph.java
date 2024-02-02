package android.filterfw.core;

import android.filterpacks.base.FrameBranch;
import android.filterpacks.base.NullFilter;
import android.util.Log;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
/* loaded from: classes.dex */
public class FilterGraph {
    public static final int AUTOBRANCH_OFF = 0;
    public static final int AUTOBRANCH_SYNCED = 1;
    public static final int AUTOBRANCH_UNSYNCED = 2;
    public static final int TYPECHECK_DYNAMIC = 1;
    public static final int TYPECHECK_OFF = 0;
    public static final int TYPECHECK_STRICT = 2;
    private HashSet<Filter> mFilters = new HashSet<>();
    private HashMap<String, Filter> mNameMap = new HashMap<>();
    private HashMap<OutputPort, LinkedList<InputPort>> mPreconnections = new HashMap<>();
    private boolean mIsReady = false;
    private int mAutoBranchMode = 0;
    private int mTypeCheckMode = 2;
    private boolean mDiscardUnconnectedOutputs = false;
    private String TAG = "FilterGraph";
    private boolean mLogVerbose = Log.isLoggable(this.TAG, 2);

    public synchronized boolean addFilter(Filter filter) {
        if (!containsFilter(filter)) {
            this.mFilters.add(filter);
            this.mNameMap.put(filter.getName(), filter);
            return true;
        }
        return false;
    }

    public synchronized boolean containsFilter(Filter filter) {
        return this.mFilters.contains(filter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Filter getFilter(String name) {
        return this.mNameMap.get(name);
    }

    public synchronized void connect(Filter source, String outputName, Filter target, String inputName) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Passing null Filter in connect()!");
        }
        if (!containsFilter(source) || !containsFilter(target)) {
            throw new RuntimeException("Attempting to connect filter not in graph!");
        }
        OutputPort outPort = source.getOutputPort(outputName);
        InputPort inPort = target.getInputPort(inputName);
        if (outPort == null) {
            throw new RuntimeException("Unknown output port '" + outputName + "' on Filter " + source + "!");
        } else if (inPort == null) {
            throw new RuntimeException("Unknown input port '" + inputName + "' on Filter " + target + "!");
        } else {
            preconnect(outPort, inPort);
        }
    }

    public synchronized void connect(String sourceName, String outputName, String targetName, String inputName) {
        Filter source = getFilter(sourceName);
        Filter target = getFilter(targetName);
        if (source == null) {
            throw new RuntimeException("Attempting to connect unknown source filter '" + sourceName + "'!");
        } else if (target == null) {
            throw new RuntimeException("Attempting to connect unknown target filter '" + targetName + "'!");
        } else {
            connect(source, outputName, target, inputName);
        }
    }

    public synchronized Set<Filter> getFilters() {
        return this.mFilters;
    }

    public synchronized void beginProcessing() {
        if (this.mLogVerbose) {
            Log.v(this.TAG, "Opening all filter connections...");
        }
        Iterator<Filter> it = this.mFilters.iterator();
        while (it.hasNext()) {
            Filter filter = it.next();
            filter.openOutputs();
        }
        this.mIsReady = true;
    }

    public synchronized void flushFrames() {
        Iterator<Filter> it = this.mFilters.iterator();
        while (it.hasNext()) {
            Filter filter = it.next();
            filter.clearOutputs();
        }
    }

    public synchronized void closeFilters(FilterContext context) {
        if (this.mLogVerbose) {
            Log.v(this.TAG, "Closing all filters...");
        }
        Iterator<Filter> it = this.mFilters.iterator();
        while (it.hasNext()) {
            Filter filter = it.next();
            filter.performClose(context);
        }
        this.mIsReady = false;
    }

    public synchronized boolean isReady() {
        return this.mIsReady;
    }

    public synchronized void setAutoBranchMode(int autoBranchMode) {
        this.mAutoBranchMode = autoBranchMode;
    }

    public synchronized void setDiscardUnconnectedOutputs(boolean discard) {
        this.mDiscardUnconnectedOutputs = discard;
    }

    public synchronized void setTypeCheckMode(int typeCheckMode) {
        this.mTypeCheckMode = typeCheckMode;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void tearDown(FilterContext context) {
        if (!this.mFilters.isEmpty()) {
            flushFrames();
            Iterator<Filter> it = this.mFilters.iterator();
            while (it.hasNext()) {
                Filter filter = it.next();
                filter.performTearDown(context);
            }
            this.mFilters.clear();
            this.mNameMap.clear();
            this.mIsReady = false;
        }
    }

    private synchronized boolean readyForProcessing(Filter filter, Set<Filter> processed) {
        if (processed.contains(filter)) {
            return false;
        }
        for (InputPort port : filter.getInputPorts()) {
            Filter dependency = port.getSourceFilter();
            if (dependency != null && !processed.contains(dependency)) {
                return false;
            }
        }
        return true;
    }

    private synchronized void runTypeCheck() {
        Stack<Filter> filterStack = new Stack<>();
        Set<Filter> processedFilters = new HashSet<>();
        filterStack.addAll(getSourceFilters());
        while (!filterStack.empty()) {
            Filter filter = filterStack.pop();
            processedFilters.add(filter);
            updateOutputs(filter);
            if (this.mLogVerbose) {
                String str = this.TAG;
                Log.v(str, "Running type check on " + filter + "...");
            }
            runTypeCheckOn(filter);
            for (OutputPort port : filter.getOutputPorts()) {
                Filter target = port.getTargetFilter();
                if (target != null && readyForProcessing(target, processedFilters)) {
                    filterStack.push(target);
                }
            }
        }
        if (processedFilters.size() != getFilters().size()) {
            throw new RuntimeException("Could not schedule all filters! Is your graph malformed?");
        }
    }

    private synchronized void updateOutputs(Filter filter) {
        for (OutputPort outputPort : filter.getOutputPorts()) {
            InputPort inputPort = outputPort.getBasePort();
            if (inputPort != null) {
                FrameFormat inputFormat = inputPort.getSourceFormat();
                FrameFormat outputFormat = filter.getOutputFormat(outputPort.getName(), inputFormat);
                if (outputFormat == null) {
                    throw new RuntimeException("Filter did not return an output format for " + outputPort + "!");
                }
                outputPort.setPortFormat(outputFormat);
            }
        }
    }

    private synchronized void runTypeCheckOn(Filter filter) {
        for (InputPort inputPort : filter.getInputPorts()) {
            if (this.mLogVerbose) {
                String str = this.TAG;
                Log.v(str, "Type checking port " + inputPort);
            }
            FrameFormat sourceFormat = inputPort.getSourceFormat();
            FrameFormat targetFormat = inputPort.getPortFormat();
            if (sourceFormat != null && targetFormat != null) {
                if (this.mLogVerbose) {
                    String str2 = this.TAG;
                    Log.v(str2, "Checking " + sourceFormat + " against " + targetFormat + ".");
                }
                boolean compatible = true;
                switch (this.mTypeCheckMode) {
                    case 0:
                        inputPort.setChecksType(false);
                        break;
                    case 1:
                        compatible = sourceFormat.mayBeCompatibleWith(targetFormat);
                        inputPort.setChecksType(true);
                        break;
                    case 2:
                        compatible = sourceFormat.isCompatibleWith(targetFormat);
                        inputPort.setChecksType(false);
                        break;
                }
                if (!compatible) {
                    throw new RuntimeException("Type mismatch: Filter " + filter + " expects a format of type " + targetFormat + " but got a format of type " + sourceFormat + "!");
                }
            }
        }
    }

    private synchronized void checkConnections() {
    }

    private synchronized void discardUnconnectedOutputs() {
        LinkedList<Filter> addedFilters = new LinkedList<>();
        Iterator<Filter> it = this.mFilters.iterator();
        while (it.hasNext()) {
            Filter filter = it.next();
            int id = 0;
            for (OutputPort port : filter.getOutputPorts()) {
                if (!port.isConnected()) {
                    if (this.mLogVerbose) {
                        String str = this.TAG;
                        Log.v(str, "Autoconnecting unconnected " + port + " to Null filter.");
                    }
                    NullFilter nullFilter = new NullFilter(filter.getName() + "ToNull" + id);
                    nullFilter.init();
                    addedFilters.add(nullFilter);
                    port.connectTo(nullFilter.getInputPort("frame"));
                    id++;
                }
            }
        }
        Iterator<Filter> it2 = addedFilters.iterator();
        while (it2.hasNext()) {
            addFilter(it2.next());
        }
    }

    private synchronized void removeFilter(Filter filter) {
        this.mFilters.remove(filter);
        this.mNameMap.remove(filter.getName());
    }

    private synchronized void preconnect(OutputPort outPort, InputPort inPort) {
        LinkedList<InputPort> targets = this.mPreconnections.get(outPort);
        if (targets == null) {
            targets = new LinkedList<>();
            this.mPreconnections.put(outPort, targets);
        }
        targets.add(inPort);
    }

    private synchronized void connectPorts() {
        int branchId = 1;
        for (Map.Entry<OutputPort, LinkedList<InputPort>> connection : this.mPreconnections.entrySet()) {
            OutputPort outputPort = connection.getKey();
            LinkedList<InputPort> inputPorts = connection.getValue();
            if (inputPorts.size() == 1) {
                outputPort.connectTo(inputPorts.get(0));
            } else if (this.mAutoBranchMode == 0) {
                throw new RuntimeException("Attempting to connect " + outputPort + " to multiple filter ports! Enable auto-branching to allow this.");
            } else {
                if (this.mLogVerbose) {
                    String str = this.TAG;
                    Log.v(str, "Creating branch for " + outputPort + "!");
                }
                if (this.mAutoBranchMode == 1) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("branch");
                    int branchId2 = branchId + 1;
                    sb.append(branchId);
                    FrameBranch branch = new FrameBranch(sb.toString());
                    new KeyValueMap();
                    branch.initWithAssignmentList("outputs", Integer.valueOf(inputPorts.size()));
                    addFilter(branch);
                    outputPort.connectTo(branch.getInputPort("in"));
                    Iterator<InputPort> inputPortIter = inputPorts.iterator();
                    for (OutputPort branchOutPort : branch.getOutputPorts()) {
                        branchOutPort.connectTo(inputPortIter.next());
                    }
                    branchId = branchId2;
                } else {
                    throw new RuntimeException("TODO: Unsynced branches not implemented yet!");
                }
            }
        }
        this.mPreconnections.clear();
    }

    private synchronized HashSet<Filter> getSourceFilters() {
        HashSet<Filter> sourceFilters = new HashSet<>();
        for (Filter filter : getFilters()) {
            if (filter.getNumberOfConnectedInputs() == 0) {
                if (this.mLogVerbose) {
                    String str = this.TAG;
                    Log.v(str, "Found source filter: " + filter);
                }
                sourceFilters.add(filter);
            }
        }
        return sourceFilters;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setupFilters() {
        if (this.mDiscardUnconnectedOutputs) {
            discardUnconnectedOutputs();
        }
        connectPorts();
        checkConnections();
        runTypeCheck();
    }
}
