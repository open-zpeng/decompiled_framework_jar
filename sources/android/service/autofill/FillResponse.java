package android.service.autofill;

import android.content.IntentSender;
import android.content.pm.ParceledListSlice;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Downloads;
import android.view.autofill.AutofillId;
import android.view.autofill.Helper;
import android.widget.RemoteViews;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public final class FillResponse implements Parcelable {
    public static final Parcelable.Creator<FillResponse> CREATOR = new Parcelable.Creator<FillResponse>() { // from class: android.service.autofill.FillResponse.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FillResponse createFromParcel(Parcel parcel) {
            Builder builder = new Builder();
            ParceledListSlice<Dataset> datasetSlice = (ParceledListSlice) parcel.readParcelable(null);
            List<Dataset> datasets = datasetSlice != null ? datasetSlice.getList() : null;
            int datasetCount = datasets != null ? datasets.size() : 0;
            for (int i = 0; i < datasetCount; i++) {
                builder.addDataset(datasets.get(i));
            }
            builder.setSaveInfo((SaveInfo) parcel.readParcelable(null));
            builder.setClientState((Bundle) parcel.readParcelable(null));
            AutofillId[] authenticationIds = (AutofillId[]) parcel.readParcelableArray(null, AutofillId.class);
            IntentSender authentication = (IntentSender) parcel.readParcelable(null);
            RemoteViews presentation = (RemoteViews) parcel.readParcelable(null);
            if (authenticationIds != null) {
                builder.setAuthentication(authenticationIds, authentication, presentation);
            }
            RemoteViews header = (RemoteViews) parcel.readParcelable(null);
            if (header != null) {
                builder.setHeader(header);
            }
            RemoteViews footer = (RemoteViews) parcel.readParcelable(null);
            if (footer != null) {
                builder.setFooter(footer);
            }
            UserData userData = (UserData) parcel.readParcelable(null);
            if (userData != null) {
                builder.setUserData(userData);
            }
            builder.setIgnoredIds((AutofillId[]) parcel.readParcelableArray(null, AutofillId.class));
            long disableDuration = parcel.readLong();
            if (disableDuration > 0) {
                builder.disableAutofill(disableDuration);
            }
            AutofillId[] fieldClassifactionIds = (AutofillId[]) parcel.readParcelableArray(null, AutofillId.class);
            if (fieldClassifactionIds != null) {
                builder.setFieldClassificationIds(fieldClassifactionIds);
            }
            builder.setFlags(parcel.readInt());
            FillResponse response = builder.build();
            response.setRequestId(parcel.readInt());
            return response;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public FillResponse[] newArray(int size) {
            return new FillResponse[size];
        }
    };
    public static final int FLAG_DISABLE_ACTIVITY_ONLY = 2;
    public static final int FLAG_TRACK_CONTEXT_COMMITED = 1;
    private final IntentSender mAuthentication;
    private final AutofillId[] mAuthenticationIds;
    private final Bundle mClientState;
    private final ParceledListSlice<Dataset> mDatasets;
    private final long mDisableDuration;
    private final AutofillId[] mFieldClassificationIds;
    private final int mFlags;
    private final RemoteViews mFooter;
    private final RemoteViews mHeader;
    private final AutofillId[] mIgnoredIds;
    private final RemoteViews mPresentation;
    private int mRequestId;
    private final SaveInfo mSaveInfo;
    private final UserData mUserData;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    @interface FillResponseFlags {
    }

    private FillResponse(Builder builder) {
        this.mDatasets = builder.mDatasets != null ? new ParceledListSlice<>(builder.mDatasets) : null;
        this.mSaveInfo = builder.mSaveInfo;
        this.mClientState = builder.mClientState;
        this.mPresentation = builder.mPresentation;
        this.mHeader = builder.mHeader;
        this.mFooter = builder.mFooter;
        this.mAuthentication = builder.mAuthentication;
        this.mAuthenticationIds = builder.mAuthenticationIds;
        this.mIgnoredIds = builder.mIgnoredIds;
        this.mDisableDuration = builder.mDisableDuration;
        this.mFieldClassificationIds = builder.mFieldClassificationIds;
        this.mFlags = builder.mFlags;
        this.mRequestId = Integer.MIN_VALUE;
        this.mUserData = builder.mUserData;
    }

    public Bundle getClientState() {
        return this.mClientState;
    }

    public List<Dataset> getDatasets() {
        ParceledListSlice<Dataset> parceledListSlice = this.mDatasets;
        if (parceledListSlice != null) {
            return parceledListSlice.getList();
        }
        return null;
    }

    public SaveInfo getSaveInfo() {
        return this.mSaveInfo;
    }

    public RemoteViews getPresentation() {
        return this.mPresentation;
    }

    public RemoteViews getHeader() {
        return this.mHeader;
    }

    public RemoteViews getFooter() {
        return this.mFooter;
    }

    public IntentSender getAuthentication() {
        return this.mAuthentication;
    }

    public AutofillId[] getAuthenticationIds() {
        return this.mAuthenticationIds;
    }

    public AutofillId[] getIgnoredIds() {
        return this.mIgnoredIds;
    }

    public long getDisableDuration() {
        return this.mDisableDuration;
    }

    public AutofillId[] getFieldClassificationIds() {
        return this.mFieldClassificationIds;
    }

    public UserData getUserData() {
        return this.mUserData;
    }

    public int getFlags() {
        return this.mFlags;
    }

    public void setRequestId(int requestId) {
        this.mRequestId = requestId;
    }

    public int getRequestId() {
        return this.mRequestId;
    }

    /* loaded from: classes2.dex */
    public static final class Builder {
        private IntentSender mAuthentication;
        private AutofillId[] mAuthenticationIds;
        private Bundle mClientState;
        private ArrayList<Dataset> mDatasets;
        private boolean mDestroyed;
        private long mDisableDuration;
        private AutofillId[] mFieldClassificationIds;
        private int mFlags;
        private RemoteViews mFooter;
        private RemoteViews mHeader;
        private AutofillId[] mIgnoredIds;
        private RemoteViews mPresentation;
        private SaveInfo mSaveInfo;
        private UserData mUserData;

        public Builder setAuthentication(AutofillId[] ids, IntentSender authentication, RemoteViews presentation) {
            throwIfDestroyed();
            throwIfDisableAutofillCalled();
            if (this.mHeader != null || this.mFooter != null) {
                throw new IllegalStateException("Already called #setHeader() or #setFooter()");
            }
            if ((presentation == null) ^ (authentication == null)) {
                throw new IllegalArgumentException("authentication and presentation must be both non-null or null");
            }
            this.mAuthentication = authentication;
            this.mPresentation = presentation;
            this.mAuthenticationIds = AutofillServiceHelper.assertValid(ids);
            return this;
        }

        public Builder setIgnoredIds(AutofillId... ids) {
            throwIfDestroyed();
            this.mIgnoredIds = ids;
            return this;
        }

        public Builder addDataset(Dataset dataset) {
            throwIfDestroyed();
            throwIfDisableAutofillCalled();
            if (dataset == null) {
                return this;
            }
            if (this.mDatasets == null) {
                this.mDatasets = new ArrayList<>();
            }
            if (!this.mDatasets.add(dataset)) {
                return this;
            }
            return this;
        }

        public Builder setSaveInfo(SaveInfo saveInfo) {
            throwIfDestroyed();
            throwIfDisableAutofillCalled();
            this.mSaveInfo = saveInfo;
            return this;
        }

        public Builder setClientState(Bundle clientState) {
            throwIfDestroyed();
            throwIfDisableAutofillCalled();
            this.mClientState = clientState;
            return this;
        }

        public Builder setFieldClassificationIds(AutofillId... ids) {
            throwIfDestroyed();
            throwIfDisableAutofillCalled();
            Preconditions.checkArrayElementsNotNull(ids, Downloads.EXTRA_IDS);
            Preconditions.checkArgumentInRange(ids.length, 1, UserData.getMaxFieldClassificationIdsSize(), "ids length");
            this.mFieldClassificationIds = ids;
            this.mFlags |= 1;
            return this;
        }

        public Builder setFlags(int flags) {
            throwIfDestroyed();
            this.mFlags = Preconditions.checkFlagsArgument(flags, 3);
            return this;
        }

        public Builder disableAutofill(long duration) {
            throwIfDestroyed();
            if (duration <= 0) {
                throw new IllegalArgumentException("duration must be greater than 0");
            }
            if (this.mAuthentication != null || this.mDatasets != null || this.mSaveInfo != null || this.mFieldClassificationIds != null || this.mClientState != null) {
                throw new IllegalStateException("disableAutofill() must be the only method called");
            }
            this.mDisableDuration = duration;
            return this;
        }

        public Builder setHeader(RemoteViews header) {
            throwIfDestroyed();
            throwIfAuthenticationCalled();
            this.mHeader = (RemoteViews) Preconditions.checkNotNull(header);
            return this;
        }

        public Builder setFooter(RemoteViews footer) {
            throwIfDestroyed();
            throwIfAuthenticationCalled();
            this.mFooter = (RemoteViews) Preconditions.checkNotNull(footer);
            return this;
        }

        public Builder setUserData(UserData userData) {
            throwIfDestroyed();
            throwIfAuthenticationCalled();
            this.mUserData = (UserData) Preconditions.checkNotNull(userData);
            return this;
        }

        public FillResponse build() {
            throwIfDestroyed();
            if (this.mAuthentication == null && this.mDatasets == null && this.mSaveInfo == null && this.mDisableDuration == 0 && this.mFieldClassificationIds == null && this.mClientState == null) {
                throw new IllegalStateException("need to provide: at least one DataSet, or a SaveInfo, or an authentication with a presentation, or a FieldsDetection, or a client state, or disable autofill");
            }
            if (this.mDatasets == null && (this.mHeader != null || this.mFooter != null)) {
                throw new IllegalStateException("must add at least 1 dataset when using header or footer");
            }
            this.mDestroyed = true;
            return new FillResponse(this);
        }

        private void throwIfDestroyed() {
            if (this.mDestroyed) {
                throw new IllegalStateException("Already called #build()");
            }
        }

        private void throwIfDisableAutofillCalled() {
            if (this.mDisableDuration > 0) {
                throw new IllegalStateException("Already called #disableAutofill()");
            }
        }

        private void throwIfAuthenticationCalled() {
            if (this.mAuthentication != null) {
                throw new IllegalStateException("Already called #setAuthentication()");
            }
        }
    }

    public String toString() {
        if (Helper.sDebug) {
            StringBuilder builder = new StringBuilder("FillResponse : [mRequestId=" + this.mRequestId);
            if (this.mDatasets != null) {
                builder.append(", datasets=");
                builder.append(this.mDatasets.getList());
            }
            if (this.mSaveInfo != null) {
                builder.append(", saveInfo=");
                builder.append(this.mSaveInfo);
            }
            if (this.mClientState != null) {
                builder.append(", hasClientState");
            }
            if (this.mPresentation != null) {
                builder.append(", hasPresentation");
            }
            if (this.mHeader != null) {
                builder.append(", hasHeader");
            }
            if (this.mFooter != null) {
                builder.append(", hasFooter");
            }
            if (this.mAuthentication != null) {
                builder.append(", hasAuthentication");
            }
            if (this.mAuthenticationIds != null) {
                builder.append(", authenticationIds=");
                builder.append(Arrays.toString(this.mAuthenticationIds));
            }
            builder.append(", disableDuration=");
            builder.append(this.mDisableDuration);
            if (this.mFlags != 0) {
                builder.append(", flags=");
                builder.append(this.mFlags);
            }
            AutofillId[] autofillIdArr = this.mFieldClassificationIds;
            if (autofillIdArr != null) {
                builder.append(Arrays.toString(autofillIdArr));
            }
            if (this.mUserData != null) {
                builder.append(", userData=");
                builder.append(this.mUserData);
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
        parcel.writeParcelable(this.mDatasets, flags);
        parcel.writeParcelable(this.mSaveInfo, flags);
        parcel.writeParcelable(this.mClientState, flags);
        parcel.writeParcelableArray(this.mAuthenticationIds, flags);
        parcel.writeParcelable(this.mAuthentication, flags);
        parcel.writeParcelable(this.mPresentation, flags);
        parcel.writeParcelable(this.mHeader, flags);
        parcel.writeParcelable(this.mFooter, flags);
        parcel.writeParcelable(this.mUserData, flags);
        parcel.writeParcelableArray(this.mIgnoredIds, flags);
        parcel.writeLong(this.mDisableDuration);
        parcel.writeParcelableArray(this.mFieldClassificationIds, flags);
        parcel.writeInt(this.mFlags);
        parcel.writeInt(this.mRequestId);
    }
}
