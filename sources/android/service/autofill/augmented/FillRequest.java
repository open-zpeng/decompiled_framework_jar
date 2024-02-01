package android.service.autofill.augmented;

import android.annotation.SystemApi;
import android.content.ComponentName;
import android.service.autofill.augmented.AugmentedAutofillService;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillValue;

@SystemApi
/* loaded from: classes2.dex */
public final class FillRequest {
    final AugmentedAutofillService.AutofillProxy mProxy;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FillRequest(AugmentedAutofillService.AutofillProxy proxy) {
        this.mProxy = proxy;
    }

    public int getTaskId() {
        return this.mProxy.taskId;
    }

    public ComponentName getActivityComponent() {
        return this.mProxy.componentName;
    }

    public AutofillId getFocusedId() {
        return this.mProxy.getFocusedId();
    }

    public AutofillValue getFocusedValue() {
        return this.mProxy.getFocusedValue();
    }

    public PresentationParams getPresentationParams() {
        return this.mProxy.getSmartSuggestionParams();
    }

    public String toString() {
        return "FillRequest[act=" + getActivityComponent().flattenToShortString() + ", id=" + this.mProxy.getFocusedId() + "]";
    }
}
