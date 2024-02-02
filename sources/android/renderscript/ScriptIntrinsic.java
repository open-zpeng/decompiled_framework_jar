package android.renderscript;
/* loaded from: classes2.dex */
public abstract class ScriptIntrinsic extends Script {
    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ScriptIntrinsic(long id, RenderScript rs) {
        super(id, rs);
        if (id == 0) {
            throw new RSRuntimeException("Loading of ScriptIntrinsic failed.");
        }
    }
}
