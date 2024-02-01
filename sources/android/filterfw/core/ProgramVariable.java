package android.filterfw.core;
/* loaded from: classes.dex */
public class ProgramVariable {
    private Program mProgram;
    private String mVarName;

    public synchronized ProgramVariable(Program program, String varName) {
        this.mProgram = program;
        this.mVarName = varName;
    }

    public synchronized Program getProgram() {
        return this.mProgram;
    }

    public synchronized String getVariableName() {
        return this.mVarName;
    }

    public synchronized void setValue(Object value) {
        if (this.mProgram == null) {
            throw new RuntimeException("Attempting to set program variable '" + this.mVarName + "' but the program is null!");
        }
        this.mProgram.setHostValue(this.mVarName, value);
    }

    public synchronized Object getValue() {
        if (this.mProgram == null) {
            throw new RuntimeException("Attempting to get program variable '" + this.mVarName + "' but the program is null!");
        }
        return this.mProgram.getHostValue(this.mVarName);
    }
}
