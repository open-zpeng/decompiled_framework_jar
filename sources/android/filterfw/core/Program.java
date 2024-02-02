package android.filterfw.core;
/* loaded from: classes.dex */
public abstract class Program {
    public abstract synchronized Object getHostValue(String str);

    /* JADX INFO: Access modifiers changed from: private */
    public abstract void process(Frame[] frameArr, Frame frame);

    /* JADX INFO: Access modifiers changed from: private */
    public abstract void setHostValue(String str, Object obj);

    /* JADX INFO: Access modifiers changed from: private */
    public void process(Frame input, Frame output) {
        Frame[] inputs = {input};
        process(inputs, output);
    }

    public synchronized void reset() {
    }
}
