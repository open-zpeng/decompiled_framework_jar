package android.service.autofill;

import android.app.ActivityThread;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import android.view.autofill.Helper;
import com.android.internal.util.Preconditions;
import java.io.PrintWriter;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public final class UserData implements Parcelable {
    public static final Parcelable.Creator<UserData> CREATOR = new Parcelable.Creator<UserData>() { // from class: android.service.autofill.UserData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserData createFromParcel(Parcel parcel) {
            String id = parcel.readString();
            String[] categoryIds = parcel.readStringArray();
            String[] values = parcel.readStringArray();
            Builder builder = new Builder(id, values[0], categoryIds[0]).setFieldClassificationAlgorithm(parcel.readString(), parcel.readBundle());
            for (int i = 1; i < categoryIds.length; i++) {
                builder.add(values[i], categoryIds[i]);
            }
            return builder.build();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };
    private static final int DEFAULT_MAX_CATEGORY_COUNT = 10;
    private static final int DEFAULT_MAX_FIELD_CLASSIFICATION_IDS_SIZE = 10;
    private static final int DEFAULT_MAX_USER_DATA_SIZE = 50;
    private static final int DEFAULT_MAX_VALUE_LENGTH = 100;
    private static final int DEFAULT_MIN_VALUE_LENGTH = 3;
    private static final String TAG = "UserData";
    private final String mAlgorithm;
    private final Bundle mAlgorithmArgs;
    private final String[] mCategoryIds;
    private final String mId;
    private final String[] mValues;

    private synchronized UserData(Builder builder) {
        this.mId = builder.mId;
        this.mAlgorithm = builder.mAlgorithm;
        this.mAlgorithmArgs = builder.mAlgorithmArgs;
        this.mCategoryIds = new String[builder.mCategoryIds.size()];
        builder.mCategoryIds.toArray(this.mCategoryIds);
        this.mValues = new String[builder.mValues.size()];
        builder.mValues.toArray(this.mValues);
    }

    public String getFieldClassificationAlgorithm() {
        return this.mAlgorithm;
    }

    public String getId() {
        return this.mId;
    }

    public synchronized Bundle getAlgorithmArgs() {
        return this.mAlgorithmArgs;
    }

    public synchronized String[] getCategoryIds() {
        return this.mCategoryIds;
    }

    public synchronized String[] getValues() {
        return this.mValues;
    }

    public synchronized void dump(String prefix, PrintWriter pw) {
        pw.print(prefix);
        pw.print("id: ");
        pw.print(this.mId);
        pw.print(prefix);
        pw.print("Algorithm: ");
        pw.print(this.mAlgorithm);
        pw.print(" Args: ");
        pw.println(this.mAlgorithmArgs);
        pw.print(prefix);
        pw.print("Field ids size: ");
        pw.println(this.mCategoryIds.length);
        for (int i = 0; i < this.mCategoryIds.length; i++) {
            pw.print(prefix);
            pw.print(prefix);
            pw.print(i);
            pw.print(": ");
            pw.println(Helper.getRedacted(this.mCategoryIds[i]));
        }
        pw.print(prefix);
        pw.print("Values size: ");
        pw.println(this.mValues.length);
        for (int i2 = 0; i2 < this.mValues.length; i2++) {
            pw.print(prefix);
            pw.print(prefix);
            pw.print(i2);
            pw.print(": ");
            pw.println(Helper.getRedacted(this.mValues[i2]));
        }
    }

    public static synchronized void dumpConstraints(String prefix, PrintWriter pw) {
        pw.print(prefix);
        pw.print("maxUserDataSize: ");
        pw.println(getMaxUserDataSize());
        pw.print(prefix);
        pw.print("maxFieldClassificationIdsSize: ");
        pw.println(getMaxFieldClassificationIdsSize());
        pw.print(prefix);
        pw.print("maxCategoryCount: ");
        pw.println(getMaxCategoryCount());
        pw.print(prefix);
        pw.print("minValueLength: ");
        pw.println(getMinValueLength());
        pw.print(prefix);
        pw.print("maxValueLength: ");
        pw.println(getMaxValueLength());
    }

    /* loaded from: classes2.dex */
    public static final class Builder {
        private String mAlgorithm;
        private Bundle mAlgorithmArgs;
        private final ArrayList<String> mCategoryIds;
        private boolean mDestroyed;
        private final String mId;
        private final ArraySet<String> mUniqueCategoryIds;
        private final ArrayList<String> mValues;

        public Builder(String id, String value, String categoryId) {
            this.mId = checkNotEmpty(Instrumentation.REPORT_KEY_IDENTIFIER, id);
            checkNotEmpty("categoryId", categoryId);
            checkValidValue(value);
            int maxUserDataSize = UserData.getMaxUserDataSize();
            this.mCategoryIds = new ArrayList<>(maxUserDataSize);
            this.mValues = new ArrayList<>(maxUserDataSize);
            this.mUniqueCategoryIds = new ArraySet<>(UserData.getMaxCategoryCount());
            addMapping(value, categoryId);
        }

        public Builder setFieldClassificationAlgorithm(String name, Bundle args) {
            throwIfDestroyed();
            this.mAlgorithm = name;
            this.mAlgorithmArgs = args;
            return this;
        }

        public Builder add(String value, String categoryId) {
            throwIfDestroyed();
            checkNotEmpty("categoryId", categoryId);
            checkValidValue(value);
            if (!this.mUniqueCategoryIds.contains(categoryId)) {
                boolean z = this.mUniqueCategoryIds.size() < UserData.getMaxCategoryCount();
                Preconditions.checkState(z, "already added " + this.mUniqueCategoryIds.size() + " unique category ids");
            }
            Preconditions.checkState(!this.mValues.contains(value), "already has entry with same value");
            boolean z2 = this.mValues.size() < UserData.getMaxUserDataSize();
            Preconditions.checkState(z2, "already added " + this.mValues.size() + " elements");
            addMapping(value, categoryId);
            return this;
        }

        private synchronized void addMapping(String value, String categoryId) {
            this.mCategoryIds.add(categoryId);
            this.mValues.add(value);
            this.mUniqueCategoryIds.add(categoryId);
        }

        private synchronized String checkNotEmpty(String name, String value) {
            Preconditions.checkNotNull(value);
            Preconditions.checkArgument(!TextUtils.isEmpty(value), "%s cannot be empty", name);
            return value;
        }

        private synchronized void checkValidValue(String value) {
            Preconditions.checkNotNull(value);
            int length = value.length();
            int minValueLength = UserData.getMinValueLength();
            int maxValueLength = UserData.getMaxValueLength();
            Preconditions.checkArgumentInRange(length, minValueLength, maxValueLength, "value length (" + length + ")");
        }

        public UserData build() {
            throwIfDestroyed();
            this.mDestroyed = true;
            return new UserData(this);
        }

        private synchronized void throwIfDestroyed() {
            if (this.mDestroyed) {
                throw new IllegalStateException("Already called #build()");
            }
        }
    }

    public String toString() {
        if (Helper.sDebug) {
            StringBuilder sb = new StringBuilder("UserData: [id=");
            sb.append(this.mId);
            sb.append(", algorithm=");
            StringBuilder builder = sb.append(this.mAlgorithm);
            builder.append(", categoryIds=");
            Helper.appendRedacted(builder, this.mCategoryIds);
            builder.append(", values=");
            Helper.appendRedacted(builder, this.mValues);
            builder.append("]");
            return builder.toString();
        }
        return super.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.mId);
        parcel.writeStringArray(this.mCategoryIds);
        parcel.writeStringArray(this.mValues);
        parcel.writeString(this.mAlgorithm);
        parcel.writeBundle(this.mAlgorithmArgs);
    }

    public static int getMaxUserDataSize() {
        return getInt(Settings.Secure.AUTOFILL_USER_DATA_MAX_USER_DATA_SIZE, 50);
    }

    public static int getMaxFieldClassificationIdsSize() {
        return getInt(Settings.Secure.AUTOFILL_USER_DATA_MAX_FIELD_CLASSIFICATION_IDS_SIZE, 10);
    }

    public static int getMaxCategoryCount() {
        return getInt(Settings.Secure.AUTOFILL_USER_DATA_MAX_CATEGORY_COUNT, 10);
    }

    public static int getMinValueLength() {
        return getInt(Settings.Secure.AUTOFILL_USER_DATA_MIN_VALUE_LENGTH, 3);
    }

    public static int getMaxValueLength() {
        return getInt(Settings.Secure.AUTOFILL_USER_DATA_MAX_VALUE_LENGTH, 100);
    }

    private static synchronized int getInt(String settings, int defaultValue) {
        ContentResolver cr = null;
        ActivityThread at = ActivityThread.currentActivityThread();
        if (at != null) {
            cr = at.getApplication().getContentResolver();
        }
        if (cr == null) {
            Log.w(TAG, "Could not read from " + settings + "; hardcoding " + defaultValue);
            return defaultValue;
        }
        return Settings.Secure.getInt(cr, settings, defaultValue);
    }
}
