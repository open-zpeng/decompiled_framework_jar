package android.filterfw.io;

import android.filterfw.core.Filter;
import android.filterfw.core.FilterFactory;
import android.filterfw.core.FilterGraph;
import android.filterfw.core.KeyValueMap;
import android.filterfw.core.ProtocolException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public class TextGraphReader extends GraphReader {
    public protected KeyValueMap mBoundReferences;
    public protected ArrayList<Command> mCommands = new ArrayList<>();
    public protected Filter mCurrentFilter;
    public protected FilterGraph mCurrentGraph;
    public protected FilterFactory mFactory;
    public protected KeyValueMap mSettings;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface Command {
        private protected synchronized void execute(TextGraphReader textGraphReader) throws GraphIOException;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ImportPackageCommand implements Command {
        public protected String mPackageName;

        public ImportPackageCommand(String packageName) {
            this.mPackageName = packageName;
        }

        private protected synchronized void execute(TextGraphReader reader) throws GraphIOException {
            try {
                reader.mFactory.addPackage(this.mPackageName);
            } catch (IllegalArgumentException e) {
                throw new GraphIOException(e.getMessage());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class AddLibraryCommand implements Command {
        public protected String mLibraryName;

        public AddLibraryCommand(String libraryName) {
            this.mLibraryName = libraryName;
        }

        private protected synchronized void execute(TextGraphReader reader) {
            FilterFactory unused = reader.mFactory;
            FilterFactory.addFilterLibrary(this.mLibraryName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class AllocateFilterCommand implements Command {
        public protected String mClassName;
        public protected String mFilterName;

        public AllocateFilterCommand(String className, String filterName) {
            this.mClassName = className;
            this.mFilterName = filterName;
        }

        private protected synchronized void execute(TextGraphReader reader) throws GraphIOException {
            try {
                Filter filter = reader.mFactory.createFilterByClassName(this.mClassName, this.mFilterName);
                reader.mCurrentFilter = filter;
            } catch (IllegalArgumentException e) {
                throw new GraphIOException(e.getMessage());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class InitFilterCommand implements Command {
        public protected KeyValueMap mParams;

        public InitFilterCommand(KeyValueMap params) {
            this.mParams = params;
        }

        private protected synchronized void execute(TextGraphReader reader) throws GraphIOException {
            Filter filter = reader.mCurrentFilter;
            try {
                filter.initWithValueMap(this.mParams);
                reader.mCurrentGraph.addFilter(TextGraphReader.this.mCurrentFilter);
            } catch (ProtocolException e) {
                throw new GraphIOException(e.getMessage());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class ConnectCommand implements Command {
        public protected String mSourceFilter;
        public protected String mSourcePort;
        public protected String mTargetFilter;
        public protected String mTargetName;

        public ConnectCommand(String sourceFilter, String sourcePort, String targetFilter, String targetName) {
            this.mSourceFilter = sourceFilter;
            this.mSourcePort = sourcePort;
            this.mTargetFilter = targetFilter;
            this.mTargetName = targetName;
        }

        private protected synchronized void execute(TextGraphReader reader) {
            reader.mCurrentGraph.connect(this.mSourceFilter, this.mSourcePort, this.mTargetFilter, this.mTargetName);
        }
    }

    private protected synchronized FilterGraph readGraphString(String graphString) throws GraphIOException {
        FilterGraph result = new FilterGraph();
        reset();
        this.mCurrentGraph = result;
        parseString(graphString);
        applySettings();
        executeCommands();
        reset();
        return result;
    }

    public protected synchronized void reset() {
        this.mCurrentGraph = null;
        this.mCurrentFilter = null;
        this.mCommands.clear();
        this.mBoundReferences = new KeyValueMap();
        this.mSettings = new KeyValueMap();
        this.mFactory = new FilterFactory();
    }

    public protected synchronized void parseString(String graphString) throws GraphIOException {
        Pattern ignorePattern;
        Pattern semicolonPattern;
        String curClassName;
        PatternScanner scanner;
        Pattern packageNamePattern;
        Pattern packageNamePattern2;
        Pattern commandPattern;
        int state;
        Pattern commandPattern2;
        Pattern packageNamePattern3;
        int state2;
        String curClassName2;
        int state3;
        Pattern commandPattern3 = Pattern.compile("@[a-zA-Z]+");
        Pattern curlyClosePattern = Pattern.compile("\\}");
        Pattern curlyOpenPattern = Pattern.compile("\\{");
        Pattern ignorePattern2 = Pattern.compile("(\\s+|//[^\\n]*\\n)+");
        Pattern wordPattern = Pattern.compile("[a-zA-Z\\.]+");
        Pattern libraryNamePattern = Pattern.compile("[a-zA-Z\\./:]+");
        Pattern portPattern = Pattern.compile("\\[[a-zA-Z0-9\\-_]+\\]");
        Pattern rightArrowPattern = Pattern.compile("=>");
        Pattern semicolonPattern2 = Pattern.compile(";");
        Pattern wordPattern2 = Pattern.compile("[a-zA-Z0-9\\-_]+");
        int state4 = 0;
        PatternScanner scanner2 = new PatternScanner(graphString, ignorePattern2);
        String curClassName3 = null;
        String curTargetPortName = null;
        String curTargetFilterName = null;
        String curTargetFilterName2 = null;
        String curSourcePortName = null;
        while (true) {
            int state5 = state4;
            String curTargetPortName2 = curTargetPortName;
            if (scanner2.atEnd()) {
                if (state5 != 16 && state5 != 0) {
                    throw new GraphIOException("Unexpected end of input!");
                }
                return;
            }
            switch (state5) {
                case 0:
                    Pattern commandPattern4 = commandPattern3;
                    ignorePattern = ignorePattern2;
                    semicolonPattern = semicolonPattern2;
                    curClassName = curClassName3;
                    scanner = scanner2;
                    packageNamePattern = wordPattern;
                    packageNamePattern2 = wordPattern2;
                    commandPattern = commandPattern4;
                    String curCommand = scanner.eat(commandPattern, "<command>");
                    if (curCommand.equals("@import")) {
                        state = 1;
                    } else if (curCommand.equals("@library")) {
                        state = 2;
                    } else if (curCommand.equals("@filter")) {
                        state = 3;
                    } else if (curCommand.equals("@connect")) {
                        state = 8;
                    } else if (curCommand.equals("@set")) {
                        state = 13;
                    } else if (curCommand.equals("@external")) {
                        state = 14;
                    } else if (!curCommand.equals("@setting")) {
                        throw new GraphIOException("Unknown command '" + curCommand + "'!");
                    } else {
                        state = 15;
                    }
                    state4 = state;
                    curTargetPortName = curTargetPortName2;
                    break;
                case 1:
                    Pattern commandPattern5 = commandPattern3;
                    ignorePattern = ignorePattern2;
                    Pattern packageNamePattern4 = wordPattern;
                    semicolonPattern = semicolonPattern2;
                    curClassName = curClassName3;
                    scanner = scanner2;
                    packageNamePattern2 = wordPattern2;
                    packageNamePattern = packageNamePattern4;
                    String packageName = scanner.eat(packageNamePattern, "<package-name>");
                    this.mCommands.add(new ImportPackageCommand(packageName));
                    state4 = 16;
                    curTargetPortName = curTargetPortName2;
                    commandPattern = commandPattern5;
                    break;
                case 2:
                    commandPattern2 = commandPattern3;
                    ignorePattern = ignorePattern2;
                    packageNamePattern3 = wordPattern;
                    semicolonPattern = semicolonPattern2;
                    curClassName = curClassName3;
                    scanner = scanner2;
                    packageNamePattern2 = wordPattern2;
                    String libraryName = scanner.eat(libraryNamePattern, "<library-name>");
                    this.mCommands.add(new AddLibraryCommand(libraryName));
                    state2 = 16;
                    state4 = state2;
                    curTargetPortName = curTargetPortName2;
                    commandPattern = commandPattern2;
                    packageNamePattern = packageNamePattern3;
                    break;
                case 3:
                    commandPattern2 = commandPattern3;
                    ignorePattern = ignorePattern2;
                    packageNamePattern3 = wordPattern;
                    semicolonPattern = semicolonPattern2;
                    scanner = scanner2;
                    packageNamePattern2 = wordPattern2;
                    String curClassName4 = scanner.eat(packageNamePattern2, "<class-name>");
                    state2 = 4;
                    curClassName = curClassName4;
                    state4 = state2;
                    curTargetPortName = curTargetPortName2;
                    commandPattern = commandPattern2;
                    packageNamePattern = packageNamePattern3;
                    break;
                case 4:
                    commandPattern2 = commandPattern3;
                    ignorePattern = ignorePattern2;
                    packageNamePattern3 = wordPattern;
                    semicolonPattern = semicolonPattern2;
                    curClassName = curClassName3;
                    scanner = scanner2;
                    packageNamePattern2 = wordPattern2;
                    String curFilterName = scanner.eat(packageNamePattern2, "<filter-name>");
                    this.mCommands.add(new AllocateFilterCommand(curClassName, curFilterName));
                    state2 = 5;
                    state4 = state2;
                    curTargetPortName = curTargetPortName2;
                    commandPattern = commandPattern2;
                    packageNamePattern = packageNamePattern3;
                    break;
                case 5:
                    commandPattern2 = commandPattern3;
                    ignorePattern = ignorePattern2;
                    packageNamePattern3 = wordPattern;
                    semicolonPattern = semicolonPattern2;
                    curClassName = curClassName3;
                    scanner = scanner2;
                    packageNamePattern2 = wordPattern2;
                    scanner.eat(curlyOpenPattern, "{");
                    state4 = 6;
                    curTargetPortName = curTargetPortName2;
                    commandPattern = commandPattern2;
                    packageNamePattern = packageNamePattern3;
                    break;
                case 6:
                    commandPattern2 = commandPattern3;
                    ignorePattern = ignorePattern2;
                    packageNamePattern3 = wordPattern;
                    semicolonPattern = semicolonPattern2;
                    curClassName = curClassName3;
                    scanner = scanner2;
                    packageNamePattern2 = wordPattern2;
                    KeyValueMap params = readKeyValueAssignments(scanner, curlyClosePattern);
                    this.mCommands.add(new InitFilterCommand(params));
                    state2 = 7;
                    state4 = state2;
                    curTargetPortName = curTargetPortName2;
                    commandPattern = commandPattern2;
                    packageNamePattern = packageNamePattern3;
                    break;
                case 7:
                    commandPattern2 = commandPattern3;
                    ignorePattern = ignorePattern2;
                    packageNamePattern3 = wordPattern;
                    semicolonPattern = semicolonPattern2;
                    curClassName = curClassName3;
                    scanner = scanner2;
                    packageNamePattern2 = wordPattern2;
                    scanner.eat(curlyClosePattern, "}");
                    state4 = 0;
                    curTargetPortName = curTargetPortName2;
                    commandPattern = commandPattern2;
                    packageNamePattern = packageNamePattern3;
                    break;
                case 8:
                    commandPattern2 = commandPattern3;
                    ignorePattern = ignorePattern2;
                    packageNamePattern3 = wordPattern;
                    semicolonPattern = semicolonPattern2;
                    curClassName = curClassName3;
                    scanner = scanner2;
                    packageNamePattern2 = wordPattern2;
                    curSourcePortName = scanner.eat(packageNamePattern2, "<source-filter-name>");
                    state4 = 9;
                    curTargetPortName = curTargetPortName2;
                    commandPattern = commandPattern2;
                    packageNamePattern = packageNamePattern3;
                    break;
                case 9:
                    commandPattern2 = commandPattern3;
                    ignorePattern = ignorePattern2;
                    packageNamePattern3 = wordPattern;
                    semicolonPattern = semicolonPattern2;
                    curClassName = curClassName3;
                    scanner = scanner2;
                    packageNamePattern2 = wordPattern2;
                    String portString = scanner.eat(portPattern, "[<source-port-name>]");
                    curTargetFilterName2 = portString.substring(1, portString.length() - 1);
                    state4 = 10;
                    curTargetPortName = curTargetPortName2;
                    commandPattern = commandPattern2;
                    packageNamePattern = packageNamePattern3;
                    break;
                case 10:
                    commandPattern2 = commandPattern3;
                    ignorePattern = ignorePattern2;
                    packageNamePattern3 = wordPattern;
                    semicolonPattern = semicolonPattern2;
                    curClassName = curClassName3;
                    scanner = scanner2;
                    packageNamePattern2 = wordPattern2;
                    scanner.eat(rightArrowPattern, "=>");
                    state4 = 11;
                    curTargetPortName = curTargetPortName2;
                    commandPattern = commandPattern2;
                    packageNamePattern = packageNamePattern3;
                    break;
                case 11:
                    commandPattern2 = commandPattern3;
                    ignorePattern = ignorePattern2;
                    packageNamePattern3 = wordPattern;
                    semicolonPattern = semicolonPattern2;
                    curClassName = curClassName3;
                    scanner = scanner2;
                    packageNamePattern2 = wordPattern2;
                    curTargetFilterName = scanner.eat(packageNamePattern2, "<target-filter-name>");
                    state4 = 12;
                    curTargetPortName = curTargetPortName2;
                    commandPattern = commandPattern2;
                    packageNamePattern = packageNamePattern3;
                    break;
                case 12:
                    String curClassName5 = curClassName3;
                    String portString2 = scanner2.eat(portPattern, "[<target-port-name>]");
                    int state6 = portString2.length() - 1;
                    curTargetPortName = portString2.substring(1, state6);
                    ignorePattern = ignorePattern2;
                    curClassName = curClassName5;
                    semicolonPattern = semicolonPattern2;
                    commandPattern2 = commandPattern3;
                    scanner = scanner2;
                    packageNamePattern3 = wordPattern;
                    packageNamePattern2 = wordPattern2;
                    this.mCommands.add(new ConnectCommand(curSourcePortName, curTargetFilterName2, curTargetFilterName, curTargetPortName));
                    state4 = 16;
                    commandPattern = commandPattern2;
                    packageNamePattern = packageNamePattern3;
                    break;
                case 13:
                    curClassName2 = curClassName3;
                    KeyValueMap assignment = readKeyValueAssignments(scanner2, semicolonPattern2);
                    this.mBoundReferences.putAll(assignment);
                    state3 = 16;
                    state4 = state3;
                    commandPattern = commandPattern3;
                    ignorePattern = ignorePattern2;
                    packageNamePattern = wordPattern;
                    semicolonPattern = semicolonPattern2;
                    curTargetPortName = curTargetPortName2;
                    curClassName = curClassName2;
                    scanner = scanner2;
                    packageNamePattern2 = wordPattern2;
                    break;
                case 14:
                    curClassName2 = curClassName3;
                    String externalName = scanner2.eat(wordPattern2, "<external-identifier>");
                    bindExternal(externalName);
                    state3 = 16;
                    state4 = state3;
                    commandPattern = commandPattern3;
                    ignorePattern = ignorePattern2;
                    packageNamePattern = wordPattern;
                    semicolonPattern = semicolonPattern2;
                    curTargetPortName = curTargetPortName2;
                    curClassName = curClassName2;
                    scanner = scanner2;
                    packageNamePattern2 = wordPattern2;
                    break;
                case 15:
                    KeyValueMap setting = readKeyValueAssignments(scanner2, semicolonPattern2);
                    curClassName2 = curClassName3;
                    this.mSettings.putAll(setting);
                    state3 = 16;
                    state4 = state3;
                    commandPattern = commandPattern3;
                    ignorePattern = ignorePattern2;
                    packageNamePattern = wordPattern;
                    semicolonPattern = semicolonPattern2;
                    curTargetPortName = curTargetPortName2;
                    curClassName = curClassName2;
                    scanner = scanner2;
                    packageNamePattern2 = wordPattern2;
                    break;
                case 16:
                    scanner2.eat(semicolonPattern2, ";");
                    state4 = 0;
                    commandPattern = commandPattern3;
                    ignorePattern = ignorePattern2;
                    semicolonPattern = semicolonPattern2;
                    curTargetPortName = curTargetPortName2;
                    curClassName = curClassName3;
                    scanner = scanner2;
                    packageNamePattern = wordPattern;
                    packageNamePattern2 = wordPattern2;
                    break;
                default:
                    commandPattern = commandPattern3;
                    ignorePattern = ignorePattern2;
                    semicolonPattern = semicolonPattern2;
                    curClassName = curClassName3;
                    scanner = scanner2;
                    packageNamePattern = wordPattern;
                    packageNamePattern2 = wordPattern2;
                    curTargetPortName = curTargetPortName2;
                    state4 = state5;
                    break;
            }
            scanner2 = scanner;
            wordPattern2 = packageNamePattern2;
            semicolonPattern2 = semicolonPattern;
            wordPattern = packageNamePattern;
            commandPattern3 = commandPattern;
            curClassName3 = curClassName;
            ignorePattern2 = ignorePattern;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized KeyValueMap readKeyValueAssignments(String assignments) throws GraphIOException {
        Pattern ignorePattern = Pattern.compile("\\s+");
        PatternScanner scanner = new PatternScanner(assignments, ignorePattern);
        return readKeyValueAssignments(scanner, null);
    }

    public protected synchronized KeyValueMap readKeyValueAssignments(PatternScanner scanner, Pattern endPattern) throws GraphIOException {
        int STATE_VALUE;
        int STATE_POST_VALUE;
        Pattern semicolonPattern;
        int state;
        TextGraphReader textGraphReader = this;
        int STATE_VALUE2 = 2;
        int STATE_POST_VALUE2 = 3;
        Pattern equalsPattern = Pattern.compile("=");
        Pattern semicolonPattern2 = Pattern.compile(";");
        Pattern wordPattern = Pattern.compile("[a-zA-Z]+[a-zA-Z0-9]*");
        Pattern stringPattern = Pattern.compile("'[^']*'|\\\"[^\\\"]*\\\"");
        Pattern intPattern = Pattern.compile("[0-9]+");
        Pattern floatPattern = Pattern.compile("[0-9]*\\.[0-9]+f?");
        Pattern referencePattern = Pattern.compile("\\$[a-zA-Z]+[a-zA-Z0-9]");
        Pattern booleanPattern = Pattern.compile("true|false");
        KeyValueMap newVals = new KeyValueMap();
        int STATE_IDENTIFIER = 0;
        String curKey = null;
        while (!scanner.atEnd() && (endPattern == null || !scanner.peek(endPattern))) {
            switch (STATE_IDENTIFIER) {
                case 0:
                    STATE_VALUE = STATE_VALUE2;
                    STATE_POST_VALUE = STATE_POST_VALUE2;
                    semicolonPattern = semicolonPattern2;
                    String curKey2 = scanner.eat(wordPattern, "<identifier>");
                    STATE_IDENTIFIER = 1;
                    curKey = curKey2;
                    continue;
                    STATE_VALUE2 = STATE_VALUE;
                    STATE_POST_VALUE2 = STATE_POST_VALUE;
                    semicolonPattern2 = semicolonPattern;
                    textGraphReader = this;
                case 1:
                    STATE_VALUE = STATE_VALUE2;
                    STATE_POST_VALUE = STATE_POST_VALUE2;
                    semicolonPattern = semicolonPattern2;
                    scanner.eat(equalsPattern, "=");
                    state = 2;
                    break;
                case 2:
                    STATE_VALUE = STATE_VALUE2;
                    String curValue = scanner.tryEat(stringPattern);
                    STATE_POST_VALUE = STATE_POST_VALUE2;
                    if (curValue == null) {
                        String curValue2 = scanner.tryEat(referencePattern);
                        if (curValue2 == null) {
                            semicolonPattern = semicolonPattern2;
                            String curValue3 = scanner.tryEat(booleanPattern);
                            if (curValue3 != null) {
                                newVals.put(curKey, Boolean.valueOf(Boolean.parseBoolean(curValue3)));
                            } else {
                                String curValue4 = scanner.tryEat(floatPattern);
                                if (curValue4 != null) {
                                    newVals.put(curKey, Float.valueOf(Float.parseFloat(curValue4)));
                                } else {
                                    String curValue5 = scanner.tryEat(intPattern);
                                    if (curValue5 == null) {
                                        throw new GraphIOException(scanner.unexpectedTokenMessage("<value>"));
                                    }
                                    newVals.put(curKey, Integer.valueOf(Integer.parseInt(curValue5)));
                                }
                            }
                            state = 3;
                            break;
                        } else {
                            String refName = curValue2.substring(1, curValue2.length());
                            Object referencedObject = textGraphReader.mBoundReferences != null ? textGraphReader.mBoundReferences.get(refName) : null;
                            if (referencedObject == null) {
                                throw new GraphIOException("Unknown object reference to '" + refName + "'!");
                            }
                            newVals.put(curKey, referencedObject);
                        }
                    } else {
                        newVals.put(curKey, curValue.substring(1, curValue.length() - 1));
                    }
                    semicolonPattern = semicolonPattern2;
                    state = 3;
                case 3:
                    STATE_VALUE = STATE_VALUE2;
                    scanner.eat(semicolonPattern2, ";");
                    STATE_IDENTIFIER = 0;
                    STATE_POST_VALUE = STATE_POST_VALUE2;
                    semicolonPattern = semicolonPattern2;
                    continue;
                    STATE_VALUE2 = STATE_VALUE;
                    STATE_POST_VALUE2 = STATE_POST_VALUE;
                    semicolonPattern2 = semicolonPattern;
                    textGraphReader = this;
                default:
                    STATE_VALUE = STATE_VALUE2;
                    STATE_POST_VALUE = STATE_POST_VALUE2;
                    semicolonPattern = semicolonPattern2;
                    continue;
                    STATE_VALUE2 = STATE_VALUE;
                    STATE_POST_VALUE2 = STATE_POST_VALUE;
                    semicolonPattern2 = semicolonPattern;
                    textGraphReader = this;
            }
            STATE_IDENTIFIER = state;
            STATE_VALUE2 = STATE_VALUE;
            STATE_POST_VALUE2 = STATE_POST_VALUE;
            semicolonPattern2 = semicolonPattern;
            textGraphReader = this;
        }
        if (STATE_IDENTIFIER == 0 || STATE_IDENTIFIER == 3) {
            return newVals;
        }
        throw new GraphIOException("Unexpected end of assignments on line " + scanner.lineNo() + "!");
    }

    public protected synchronized void bindExternal(String name) throws GraphIOException {
        if (this.mReferences.containsKey(name)) {
            Object value = this.mReferences.get(name);
            this.mBoundReferences.put(name, value);
            return;
        }
        throw new GraphIOException("Unknown external variable '" + name + "'! You must add a reference to this external in the host program using addReference(...)!");
    }

    public protected synchronized void checkReferences() throws GraphIOException {
        for (String reference : this.mReferences.keySet()) {
            if (!this.mBoundReferences.containsKey(reference)) {
                throw new GraphIOException("Host program specifies reference to '" + reference + "', which is not declared @external in graph file!");
            }
        }
    }

    public protected synchronized void applySettings() throws GraphIOException {
        for (String setting : this.mSettings.keySet()) {
            Object value = this.mSettings.get(setting);
            if (setting.equals("autoBranch")) {
                expectSettingClass(setting, value, String.class);
                if (value.equals("synced")) {
                    this.mCurrentGraph.setAutoBranchMode(1);
                } else if (value.equals("unsynced")) {
                    this.mCurrentGraph.setAutoBranchMode(2);
                } else if (value.equals("off")) {
                    this.mCurrentGraph.setAutoBranchMode(0);
                } else {
                    throw new GraphIOException("Unknown autobranch setting: " + value + "!");
                }
            } else if (setting.equals("discardUnconnectedOutputs")) {
                expectSettingClass(setting, value, Boolean.class);
                this.mCurrentGraph.setDiscardUnconnectedOutputs(((Boolean) value).booleanValue());
            } else {
                throw new GraphIOException("Unknown @setting '" + setting + "'!");
            }
        }
    }

    public protected synchronized void expectSettingClass(String setting, Object value, Class expectedClass) throws GraphIOException {
        if (value.getClass() != expectedClass) {
            throw new GraphIOException("Setting '" + setting + "' must have a value of type " + expectedClass.getSimpleName() + ", but found a value of type " + value.getClass().getSimpleName() + "!");
        }
    }

    public protected synchronized void executeCommands() throws GraphIOException {
        Iterator<Command> it = this.mCommands.iterator();
        while (it.hasNext()) {
            Command command = it.next();
            command.execute(this);
        }
    }
}
