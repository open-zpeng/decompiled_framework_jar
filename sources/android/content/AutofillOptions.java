package android.content;

import android.app.ActivityThread;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArraySet;
import android.util.Log;
import android.view.autofill.AutofillManager;
import java.io.PrintWriter;

/* loaded from: classes.dex */
public final class AutofillOptions implements Parcelable {
    public boolean augmentedAutofillEnabled;
    public final boolean compatModeEnabled;
    public final int loggingLevel;
    public ArraySet<ComponentName> whitelistedActivitiesForAugmentedAutofill;
    private static final String TAG = AutofillOptions.class.getSimpleName();
    public static final Parcelable.Creator<AutofillOptions> CREATOR = new Parcelable.Creator<AutofillOptions>() { // from class: android.content.AutofillOptions.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AutofillOptions createFromParcel(Parcel parcel) {
            int loggingLevel = parcel.readInt();
            boolean compatMode = parcel.readBoolean();
            AutofillOptions options = new AutofillOptions(loggingLevel, compatMode);
            options.augmentedAutofillEnabled = parcel.readBoolean();
            options.whitelistedActivitiesForAugmentedAutofill = parcel.readArraySet(null);
            return options;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AutofillOptions[] newArray(int size) {
            return new AutofillOptions[size];
        }
    };

    public AutofillOptions(int loggingLevel, boolean compatModeEnabled) {
        this.loggingLevel = loggingLevel;
        this.compatModeEnabled = compatModeEnabled;
    }

    public boolean isAugmentedAutofillEnabled(Context context) {
        AutofillManager.AutofillClient autofillClient;
        if (this.augmentedAutofillEnabled && (autofillClient = context.getAutofillClient()) != null) {
            ComponentName component = autofillClient.autofillClientGetComponentName();
            ArraySet<ComponentName> arraySet = this.whitelistedActivitiesForAugmentedAutofill;
            return arraySet == null || arraySet.contains(component);
        }
        return false;
    }

    public static AutofillOptions forWhitelistingItself() {
        ActivityThread at = ActivityThread.currentActivityThread();
        if (at == null) {
            throw new IllegalStateException("No ActivityThread");
        }
        String packageName = at.getApplication().getPackageName();
        if (!"android.autofillservice.cts".equals(packageName)) {
            String str = TAG;
            Log.e(str, "forWhitelistingItself(): called by " + packageName);
            throw new SecurityException("Thou shall not pass!");
        }
        AutofillOptions options = new AutofillOptions(4, true);
        options.augmentedAutofillEnabled = true;
        String str2 = TAG;
        Log.i(str2, "forWhitelistingItself(" + packageName + "): " + options);
        return options;
    }

    public String toString() {
        return "AutofillOptions [loggingLevel=" + this.loggingLevel + ", compatMode=" + this.compatModeEnabled + ", augmentedAutofillEnabled=" + this.augmentedAutofillEnabled + "]";
    }

    public void dumpShort(PrintWriter pw) {
        pw.print("logLvl=");
        pw.print(this.loggingLevel);
        pw.print(", compatMode=");
        pw.print(this.compatModeEnabled);
        pw.print(", augmented=");
        pw.print(this.augmentedAutofillEnabled);
        if (this.whitelistedActivitiesForAugmentedAutofill != null) {
            pw.print(", whitelistedActivitiesForAugmentedAutofill=");
            pw.print(this.whitelistedActivitiesForAugmentedAutofill);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.loggingLevel);
        parcel.writeBoolean(this.compatModeEnabled);
        parcel.writeBoolean(this.augmentedAutofillEnabled);
        parcel.writeArraySet(this.whitelistedActivitiesForAugmentedAutofill);
    }
}
