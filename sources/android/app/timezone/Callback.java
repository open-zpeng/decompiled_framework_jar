package android.app.timezone;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes.dex */
public abstract class Callback {
    private protected static final int ERROR_INSTALL_BAD_DISTRO_FORMAT_VERSION = 3;
    private protected static final int ERROR_INSTALL_BAD_DISTRO_STRUCTURE = 2;
    private protected static final int ERROR_INSTALL_RULES_TOO_OLD = 4;
    private protected static final int ERROR_INSTALL_VALIDATION_ERROR = 5;
    private protected static final int ERROR_UNKNOWN_FAILURE = 1;
    private protected static final int SUCCESS = 0;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface AsyncResultCode {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public abstract synchronized void onFinished(int i);

    private protected synchronized Callback() {
    }
}
