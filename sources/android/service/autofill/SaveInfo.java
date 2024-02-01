package android.service.autofill;

import android.content.IntentSender;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.DebugUtils;
import android.view.autofill.AutofillId;
import android.view.autofill.Helper;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
/* loaded from: classes2.dex */
public final class SaveInfo implements Parcelable {
    public static final Parcelable.Creator<SaveInfo> CREATOR = new Parcelable.Creator<SaveInfo>() { // from class: android.service.autofill.SaveInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SaveInfo createFromParcel(Parcel parcel) {
            Builder builder;
            int type = parcel.readInt();
            AutofillId[] requiredIds = (AutofillId[]) parcel.readParcelableArray(null, AutofillId.class);
            if (requiredIds != null) {
                builder = new Builder(type, requiredIds);
            } else {
                builder = new Builder(type);
            }
            AutofillId[] optionalIds = (AutofillId[]) parcel.readParcelableArray(null, AutofillId.class);
            if (optionalIds != null) {
                builder.setOptionalIds(optionalIds);
            }
            builder.setNegativeAction(parcel.readInt(), (IntentSender) parcel.readParcelable(null));
            builder.setDescription(parcel.readCharSequence());
            CustomDescription customDescripton = (CustomDescription) parcel.readParcelable(null);
            if (customDescripton != null) {
                builder.setCustomDescription(customDescripton);
            }
            InternalValidator validator = (InternalValidator) parcel.readParcelable(null);
            if (validator != null) {
                builder.setValidator(validator);
            }
            InternalSanitizer[] sanitizers = (InternalSanitizer[]) parcel.readParcelableArray(null, InternalSanitizer.class);
            if (sanitizers != null) {
                for (InternalSanitizer internalSanitizer : sanitizers) {
                    AutofillId[] autofillIds = (AutofillId[]) parcel.readParcelableArray(null, AutofillId.class);
                    builder.addSanitizer(internalSanitizer, autofillIds);
                }
            }
            AutofillId triggerId = (AutofillId) parcel.readParcelable(null);
            if (triggerId != null) {
                builder.setTriggerId(triggerId);
            }
            builder.setFlags(parcel.readInt());
            return builder.build();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SaveInfo[] newArray(int size) {
            return new SaveInfo[size];
        }
    };
    public static final int FLAG_DONT_SAVE_ON_FINISH = 2;
    public static final int FLAG_SAVE_ON_ALL_VIEWS_INVISIBLE = 1;
    public static final int NEGATIVE_BUTTON_STYLE_CANCEL = 0;
    public static final int NEGATIVE_BUTTON_STYLE_REJECT = 1;
    public static final int SAVE_DATA_TYPE_ADDRESS = 2;
    public static final int SAVE_DATA_TYPE_CREDIT_CARD = 4;
    public static final int SAVE_DATA_TYPE_EMAIL_ADDRESS = 16;
    public static final int SAVE_DATA_TYPE_GENERIC = 0;
    public static final int SAVE_DATA_TYPE_PASSWORD = 1;
    public static final int SAVE_DATA_TYPE_USERNAME = 8;
    private final CustomDescription mCustomDescription;
    private final CharSequence mDescription;
    private final int mFlags;
    private final IntentSender mNegativeActionListener;
    private final int mNegativeButtonStyle;
    private final AutofillId[] mOptionalIds;
    private final AutofillId[] mRequiredIds;
    private final InternalSanitizer[] mSanitizerKeys;
    private final AutofillId[][] mSanitizerValues;
    private final AutofillId mTriggerId;
    private final int mType;
    private final InternalValidator mValidator;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    @interface NegativeButtonStyle {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    @interface SaveDataType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    @interface SaveInfoFlags {
    }

    private synchronized SaveInfo(Builder builder) {
        this.mType = builder.mType;
        this.mNegativeButtonStyle = builder.mNegativeButtonStyle;
        this.mNegativeActionListener = builder.mNegativeActionListener;
        this.mRequiredIds = builder.mRequiredIds;
        this.mOptionalIds = builder.mOptionalIds;
        this.mDescription = builder.mDescription;
        this.mFlags = builder.mFlags;
        this.mCustomDescription = builder.mCustomDescription;
        this.mValidator = builder.mValidator;
        if (builder.mSanitizers == null) {
            this.mSanitizerKeys = null;
            this.mSanitizerValues = null;
        } else {
            int size = builder.mSanitizers.size();
            this.mSanitizerKeys = new InternalSanitizer[size];
            this.mSanitizerValues = new AutofillId[size];
            for (int i = 0; i < size; i++) {
                this.mSanitizerKeys[i] = (InternalSanitizer) builder.mSanitizers.keyAt(i);
                this.mSanitizerValues[i] = (AutofillId[]) builder.mSanitizers.valueAt(i);
            }
        }
        this.mTriggerId = builder.mTriggerId;
    }

    public synchronized int getNegativeActionStyle() {
        return this.mNegativeButtonStyle;
    }

    public synchronized IntentSender getNegativeActionListener() {
        return this.mNegativeActionListener;
    }

    public synchronized AutofillId[] getRequiredIds() {
        return this.mRequiredIds;
    }

    public synchronized AutofillId[] getOptionalIds() {
        return this.mOptionalIds;
    }

    public synchronized int getType() {
        return this.mType;
    }

    public synchronized int getFlags() {
        return this.mFlags;
    }

    public synchronized CharSequence getDescription() {
        return this.mDescription;
    }

    public synchronized CustomDescription getCustomDescription() {
        return this.mCustomDescription;
    }

    public synchronized InternalValidator getValidator() {
        return this.mValidator;
    }

    public synchronized InternalSanitizer[] getSanitizerKeys() {
        return this.mSanitizerKeys;
    }

    public synchronized AutofillId[][] getSanitizerValues() {
        return this.mSanitizerValues;
    }

    public synchronized AutofillId getTriggerId() {
        return this.mTriggerId;
    }

    /* loaded from: classes2.dex */
    public static final class Builder {
        private CustomDescription mCustomDescription;
        private CharSequence mDescription;
        private boolean mDestroyed;
        private int mFlags;
        private IntentSender mNegativeActionListener;
        private int mNegativeButtonStyle;
        private AutofillId[] mOptionalIds;
        private final AutofillId[] mRequiredIds;
        private ArraySet<AutofillId> mSanitizerIds;
        private ArrayMap<InternalSanitizer, AutofillId[]> mSanitizers;
        private AutofillId mTriggerId;
        private final int mType;
        private InternalValidator mValidator;

        public Builder(int type, AutofillId[] requiredIds) {
            this.mNegativeButtonStyle = 0;
            this.mType = type;
            this.mRequiredIds = AutofillServiceHelper.assertValid(requiredIds);
        }

        public Builder(int type) {
            this.mNegativeButtonStyle = 0;
            this.mType = type;
            this.mRequiredIds = null;
        }

        public Builder setFlags(int flags) {
            throwIfDestroyed();
            this.mFlags = Preconditions.checkFlagsArgument(flags, 3);
            return this;
        }

        public Builder setOptionalIds(AutofillId[] ids) {
            throwIfDestroyed();
            this.mOptionalIds = AutofillServiceHelper.assertValid(ids);
            return this;
        }

        public Builder setDescription(CharSequence description) {
            throwIfDestroyed();
            Preconditions.checkState(this.mCustomDescription == null, "Can call setDescription() or setCustomDescription(), but not both");
            this.mDescription = description;
            return this;
        }

        public Builder setCustomDescription(CustomDescription customDescription) {
            throwIfDestroyed();
            Preconditions.checkState(this.mDescription == null, "Can call setDescription() or setCustomDescription(), but not both");
            this.mCustomDescription = customDescription;
            return this;
        }

        public Builder setNegativeAction(int style, IntentSender listener) {
            throwIfDestroyed();
            if (style != 0 && style != 1) {
                throw new IllegalArgumentException("Invalid style: " + style);
            }
            this.mNegativeButtonStyle = style;
            this.mNegativeActionListener = listener;
            return this;
        }

        public Builder setValidator(Validator validator) {
            throwIfDestroyed();
            Preconditions.checkArgument(validator instanceof InternalValidator, "not provided by Android System: " + validator);
            this.mValidator = (InternalValidator) validator;
            return this;
        }

        public Builder addSanitizer(Sanitizer sanitizer, AutofillId... ids) {
            throwIfDestroyed();
            Preconditions.checkArgument(!ArrayUtils.isEmpty(ids), "ids cannot be empty or null");
            Preconditions.checkArgument(sanitizer instanceof InternalSanitizer, "not provided by Android System: " + sanitizer);
            if (this.mSanitizers == null) {
                this.mSanitizers = new ArrayMap<>();
                this.mSanitizerIds = new ArraySet<>(ids.length);
            }
            for (AutofillId id : ids) {
                Preconditions.checkArgument(!this.mSanitizerIds.contains(id), "already added %s", id);
                this.mSanitizerIds.add(id);
            }
            this.mSanitizers.put((InternalSanitizer) sanitizer, ids);
            return this;
        }

        public Builder setTriggerId(AutofillId id) {
            throwIfDestroyed();
            this.mTriggerId = (AutofillId) Preconditions.checkNotNull(id);
            return this;
        }

        public SaveInfo build() {
            throwIfDestroyed();
            Preconditions.checkState((ArrayUtils.isEmpty(this.mRequiredIds) && ArrayUtils.isEmpty(this.mOptionalIds)) ? false : true, "must have at least one required or optional id");
            this.mDestroyed = true;
            return new SaveInfo(this);
        }

        private synchronized void throwIfDestroyed() {
            if (this.mDestroyed) {
                throw new IllegalStateException("Already called #build()");
            }
        }
    }

    public String toString() {
        if (Helper.sDebug) {
            StringBuilder sb = new StringBuilder("SaveInfo: [type=");
            sb.append(DebugUtils.flagsToString(SaveInfo.class, "SAVE_DATA_TYPE_", this.mType));
            sb.append(", requiredIds=");
            sb.append(Arrays.toString(this.mRequiredIds));
            sb.append(", style=");
            StringBuilder builder = sb.append(DebugUtils.flagsToString(SaveInfo.class, "NEGATIVE_BUTTON_STYLE_", this.mNegativeButtonStyle));
            if (this.mOptionalIds != null) {
                builder.append(", optionalIds=");
                builder.append(Arrays.toString(this.mOptionalIds));
            }
            if (this.mDescription != null) {
                builder.append(", description=");
                builder.append(this.mDescription);
            }
            if (this.mFlags != 0) {
                builder.append(", flags=");
                builder.append(this.mFlags);
            }
            if (this.mCustomDescription != null) {
                builder.append(", customDescription=");
                builder.append(this.mCustomDescription);
            }
            if (this.mValidator != null) {
                builder.append(", validator=");
                builder.append(this.mValidator);
            }
            if (this.mSanitizerKeys != null) {
                builder.append(", sanitizerKeys=");
                builder.append(this.mSanitizerKeys.length);
            }
            if (this.mSanitizerValues != null) {
                builder.append(", sanitizerValues=");
                builder.append(this.mSanitizerValues.length);
            }
            if (this.mTriggerId != null) {
                builder.append(", triggerId=");
                builder.append(this.mTriggerId);
            }
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
        parcel.writeInt(this.mType);
        parcel.writeParcelableArray(this.mRequiredIds, flags);
        parcel.writeParcelableArray(this.mOptionalIds, flags);
        parcel.writeInt(this.mNegativeButtonStyle);
        parcel.writeParcelable(this.mNegativeActionListener, flags);
        parcel.writeCharSequence(this.mDescription);
        parcel.writeParcelable(this.mCustomDescription, flags);
        parcel.writeParcelable(this.mValidator, flags);
        parcel.writeParcelableArray(this.mSanitizerKeys, flags);
        if (this.mSanitizerKeys != null) {
            for (int i = 0; i < this.mSanitizerValues.length; i++) {
                parcel.writeParcelableArray(this.mSanitizerValues[i], flags);
            }
        }
        parcel.writeParcelable(this.mTriggerId, flags);
        parcel.writeInt(this.mFlags);
    }
}
