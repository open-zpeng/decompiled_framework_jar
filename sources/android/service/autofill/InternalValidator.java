package android.service.autofill;

import android.os.Parcelable;

/* loaded from: classes2.dex */
public abstract class InternalValidator implements Validator, Parcelable {
    public abstract boolean isValid(ValueFinder valueFinder);
}
