package android.service.autofill.augmented;

import android.annotation.SystemApi;
import android.service.autofill.augmented.AugmentedAutofillService;
import android.util.Log;

@SystemApi
/* loaded from: classes2.dex */
public final class FillCallback {
    private static final String TAG = FillCallback.class.getSimpleName();
    private final AugmentedAutofillService.AutofillProxy mProxy;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FillCallback(AugmentedAutofillService.AutofillProxy proxy) {
        this.mProxy = proxy;
    }

    public void onSuccess(FillResponse response) {
        if (AugmentedAutofillService.sDebug) {
            String str = TAG;
            Log.d(str, "onSuccess(): " + response);
        }
        if (response == null) {
            this.mProxy.report(1);
            return;
        }
        FillWindow fillWindow = response.getFillWindow();
        if (fillWindow != null) {
            fillWindow.show();
        }
    }
}
