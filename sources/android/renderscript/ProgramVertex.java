package android.renderscript;

import android.renderscript.Program;
/* loaded from: classes2.dex */
public class ProgramVertex extends Program {
    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ProgramVertex(long id, RenderScript rs) {
        super(id, rs);
    }

    public synchronized int getInputCount() {
        if (this.mInputs != null) {
            return this.mInputs.length;
        }
        return 0;
    }

    public synchronized Element getInput(int slot) {
        if (slot < 0 || slot >= this.mInputs.length) {
            throw new IllegalArgumentException("Slot ID out of range.");
        }
        return this.mInputs[slot];
    }

    /* loaded from: classes2.dex */
    public static class Builder extends Program.BaseProgramBuilder {
        private protected Builder(RenderScript rs) {
            super(rs);
        }

        private protected Builder addInput(Element e) throws IllegalStateException {
            if (this.mInputCount >= 8) {
                throw new RSIllegalArgumentException("Max input count exceeded.");
            }
            if (e.isComplex()) {
                throw new RSIllegalArgumentException("Complex elements not allowed.");
            }
            Element[] elementArr = this.mInputs;
            int i = this.mInputCount;
            this.mInputCount = i + 1;
            elementArr[i] = e;
            return this;
        }

        private protected ProgramVertex create() {
            this.mRS.validate();
            long[] tmp = new long[(this.mInputCount + this.mOutputCount + this.mConstantCount + this.mTextureCount) * 2];
            String[] texNames = new String[this.mTextureCount];
            int i = 0;
            int idx = 0;
            for (int idx2 = 0; idx2 < this.mInputCount; idx2++) {
                int idx3 = idx + 1;
                tmp[idx] = Program.ProgramParam.INPUT.mID;
                idx = idx3 + 1;
                tmp[idx3] = this.mInputs[idx2].getID(this.mRS);
            }
            for (int i2 = 0; i2 < this.mOutputCount; i2++) {
                int idx4 = idx + 1;
                tmp[idx] = Program.ProgramParam.OUTPUT.mID;
                idx = idx4 + 1;
                tmp[idx4] = this.mOutputs[i2].getID(this.mRS);
            }
            for (int i3 = 0; i3 < this.mConstantCount; i3++) {
                int idx5 = idx + 1;
                tmp[idx] = Program.ProgramParam.CONSTANT.mID;
                idx = idx5 + 1;
                tmp[idx5] = this.mConstants[i3].getID(this.mRS);
            }
            while (true) {
                int i4 = i;
                int i5 = this.mTextureCount;
                if (i4 < i5) {
                    int idx6 = idx + 1;
                    tmp[idx] = Program.ProgramParam.TEXTURE_TYPE.mID;
                    idx = idx6 + 1;
                    tmp[idx6] = this.mTextureTypes[i4].mID;
                    texNames[i4] = this.mTextureNames[i4];
                    i = i4 + 1;
                } else {
                    long id = this.mRS.nProgramVertexCreate(this.mShader, texNames, tmp);
                    ProgramVertex pv = new ProgramVertex(id, this.mRS);
                    initProgram(pv);
                    return pv;
                }
            }
        }
    }
}
