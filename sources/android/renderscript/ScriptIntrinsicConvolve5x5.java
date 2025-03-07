package android.renderscript;

import android.renderscript.Script;

/* loaded from: classes2.dex */
public final class ScriptIntrinsicConvolve5x5 extends ScriptIntrinsic {
    private Allocation mInput;
    private final float[] mValues;

    private ScriptIntrinsicConvolve5x5(long id, RenderScript rs) {
        super(id, rs);
        this.mValues = new float[25];
    }

    public static ScriptIntrinsicConvolve5x5 create(RenderScript rs, Element e) {
        if (!e.isCompatible(Element.U8(rs)) && !e.isCompatible(Element.U8_2(rs)) && !e.isCompatible(Element.U8_3(rs)) && !e.isCompatible(Element.U8_4(rs)) && !e.isCompatible(Element.F32(rs)) && !e.isCompatible(Element.F32_2(rs)) && !e.isCompatible(Element.F32_3(rs)) && !e.isCompatible(Element.F32_4(rs))) {
            throw new RSIllegalArgumentException("Unsupported element type.");
        }
        long id = rs.nScriptIntrinsicCreate(4, e.getID(rs));
        return new ScriptIntrinsicConvolve5x5(id, rs);
    }

    public void setInput(Allocation ain) {
        this.mInput = ain;
        setVar(1, ain);
    }

    public void setCoefficients(float[] v) {
        FieldPacker fp = new FieldPacker(100);
        int ct = 0;
        while (true) {
            float[] fArr = this.mValues;
            if (ct < fArr.length) {
                fArr[ct] = v[ct];
                fp.addF32(fArr[ct]);
                ct++;
            } else {
                setVar(0, fp);
                return;
            }
        }
    }

    public void forEach(Allocation aout) {
        forEach(0, (Allocation) null, aout, (FieldPacker) null);
    }

    public void forEach(Allocation aout, Script.LaunchOptions opt) {
        forEach(0, (Allocation) null, aout, (FieldPacker) null, opt);
    }

    public Script.KernelID getKernelID() {
        return createKernelID(0, 2, null, null);
    }

    public Script.FieldID getFieldID_Input() {
        return createFieldID(1, null);
    }
}
