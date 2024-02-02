package android.view.inputmethod;

import android.content.ComponentName;
/* loaded from: classes2.dex */
public interface InputMethodManagerInternal {
    synchronized void hideCurrentInputMethod();

    synchronized void setInteractive(boolean z);

    synchronized void startVrInputMethodNoCheck(ComponentName componentName);

    synchronized void switchInputMethod(boolean z);
}
