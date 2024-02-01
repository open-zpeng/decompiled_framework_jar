package android.service.autofill;

import android.os.Parcelable;
import android.view.autofill.AutofillValue;
/* loaded from: classes2.dex */
public abstract class InternalSanitizer implements Sanitizer, Parcelable {
    public abstract synchronized AutofillValue sanitize(AutofillValue autofillValue);
}
