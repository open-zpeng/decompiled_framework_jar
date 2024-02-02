package android.service.dreams;
/* loaded from: classes2.dex */
public abstract class DreamManagerInternal {
    public abstract synchronized boolean isDreaming();

    public abstract synchronized void startDream(boolean z);

    public abstract synchronized void stopDream(boolean z);
}
