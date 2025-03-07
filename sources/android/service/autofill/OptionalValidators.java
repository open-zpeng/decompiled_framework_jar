package android.service.autofill;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.autofill.Helper;
import com.android.internal.util.Preconditions;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class OptionalValidators extends InternalValidator {
    public static final Parcelable.Creator<OptionalValidators> CREATOR = new Parcelable.Creator<OptionalValidators>() { // from class: android.service.autofill.OptionalValidators.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OptionalValidators createFromParcel(Parcel parcel) {
            return new OptionalValidators((InternalValidator[]) parcel.readParcelableArray(null, InternalValidator.class));
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OptionalValidators[] newArray(int size) {
            return new OptionalValidators[size];
        }
    };
    private static final String TAG = "OptionalValidators";
    private final InternalValidator[] mValidators;

    /* JADX INFO: Access modifiers changed from: package-private */
    public OptionalValidators(InternalValidator[] validators) {
        this.mValidators = (InternalValidator[]) Preconditions.checkArrayElementsNotNull(validators, "validators");
    }

    @Override // android.service.autofill.InternalValidator
    public boolean isValid(ValueFinder finder) {
        InternalValidator[] internalValidatorArr;
        for (InternalValidator validator : this.mValidators) {
            boolean valid = validator.isValid(finder);
            if (Helper.sDebug) {
                Log.d(TAG, "isValid(" + validator + "): " + valid);
            }
            if (valid) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        if (Helper.sDebug) {
            return "OptionalValidators: [validators=" + this.mValidators + "]";
        }
        return super.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelableArray(this.mValidators, flags);
    }
}
