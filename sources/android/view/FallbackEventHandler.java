package android.view;
/* loaded from: classes2.dex */
public interface FallbackEventHandler {
    synchronized boolean dispatchKeyEvent(KeyEvent keyEvent);

    synchronized void preDispatchKeyEvent(KeyEvent keyEvent);

    synchronized void setView(View view);
}
