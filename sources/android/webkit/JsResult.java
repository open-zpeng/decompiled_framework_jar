package android.webkit;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;

/* loaded from: classes3.dex */
public class JsResult {
    @UnsupportedAppUsage
    private final ResultReceiver mReceiver;
    private boolean mResult;

    @SystemApi
    /* loaded from: classes3.dex */
    public interface ResultReceiver {
        void onJsResultComplete(JsResult jsResult);
    }

    public final void cancel() {
        this.mResult = false;
        wakeUp();
    }

    public final void confirm() {
        this.mResult = true;
        wakeUp();
    }

    @SystemApi
    public JsResult(ResultReceiver receiver) {
        this.mReceiver = receiver;
    }

    @SystemApi
    public final boolean getResult() {
        return this.mResult;
    }

    private final void wakeUp() {
        this.mReceiver.onJsResultComplete(this);
    }
}
