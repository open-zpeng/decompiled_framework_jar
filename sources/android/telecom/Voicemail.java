package android.telecom;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes2.dex */
public class Voicemail implements Parcelable {
    public static final Parcelable.Creator<Voicemail> CREATOR = new Parcelable.Creator<Voicemail>() { // from class: android.telecom.Voicemail.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Voicemail createFromParcel(Parcel in) {
            return new Voicemail(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Voicemail[] newArray(int size) {
            return new Voicemail[size];
        }
    };
    private final Long mDuration;
    private final Boolean mHasContent;
    private final Long mId;
    private final Boolean mIsRead;
    private final String mNumber;
    private final PhoneAccountHandle mPhoneAccount;
    private final String mProviderData;
    private final String mSource;
    private final Long mTimestamp;
    private final String mTranscription;
    private final Uri mUri;

    private synchronized Voicemail(Long timestamp, String number, PhoneAccountHandle phoneAccountHandle, Long id, Long duration, String source, String providerData, Uri uri, Boolean isRead, Boolean hasContent, String transcription) {
        this.mTimestamp = timestamp;
        this.mNumber = number;
        this.mPhoneAccount = phoneAccountHandle;
        this.mId = id;
        this.mDuration = duration;
        this.mSource = source;
        this.mProviderData = providerData;
        this.mUri = uri;
        this.mIsRead = isRead;
        this.mHasContent = hasContent;
        this.mTranscription = transcription;
    }

    public static synchronized Builder createForInsertion(long timestamp, String number) {
        return new Builder().setNumber(number).setTimestamp(timestamp);
    }

    public static synchronized Builder createForUpdate(long id, String sourceData) {
        return new Builder().setId(id).setSourceData(sourceData);
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        private Long mBuilderDuration;
        private boolean mBuilderHasContent;
        private Long mBuilderId;
        private Boolean mBuilderIsRead;
        private String mBuilderNumber;
        private PhoneAccountHandle mBuilderPhoneAccount;
        private String mBuilderSourceData;
        private String mBuilderSourcePackage;
        private Long mBuilderTimestamp;
        private String mBuilderTranscription;
        private Uri mBuilderUri;

        private synchronized Builder() {
        }

        public synchronized Builder setNumber(String number) {
            this.mBuilderNumber = number;
            return this;
        }

        public synchronized Builder setTimestamp(long timestamp) {
            this.mBuilderTimestamp = Long.valueOf(timestamp);
            return this;
        }

        public synchronized Builder setPhoneAccount(PhoneAccountHandle phoneAccount) {
            this.mBuilderPhoneAccount = phoneAccount;
            return this;
        }

        public synchronized Builder setId(long id) {
            this.mBuilderId = Long.valueOf(id);
            return this;
        }

        public synchronized Builder setDuration(long duration) {
            this.mBuilderDuration = Long.valueOf(duration);
            return this;
        }

        public synchronized Builder setSourcePackage(String sourcePackage) {
            this.mBuilderSourcePackage = sourcePackage;
            return this;
        }

        public synchronized Builder setSourceData(String sourceData) {
            this.mBuilderSourceData = sourceData;
            return this;
        }

        public synchronized Builder setUri(Uri uri) {
            this.mBuilderUri = uri;
            return this;
        }

        public synchronized Builder setIsRead(boolean isRead) {
            this.mBuilderIsRead = Boolean.valueOf(isRead);
            return this;
        }

        public synchronized Builder setHasContent(boolean hasContent) {
            this.mBuilderHasContent = hasContent;
            return this;
        }

        public synchronized Builder setTranscription(String transcription) {
            this.mBuilderTranscription = transcription;
            return this;
        }

        public synchronized Voicemail build() {
            this.mBuilderId = Long.valueOf(this.mBuilderId == null ? -1L : this.mBuilderId.longValue());
            this.mBuilderTimestamp = Long.valueOf(this.mBuilderTimestamp == null ? 0L : this.mBuilderTimestamp.longValue());
            this.mBuilderDuration = Long.valueOf(this.mBuilderDuration != null ? this.mBuilderDuration.longValue() : 0L);
            this.mBuilderIsRead = Boolean.valueOf(this.mBuilderIsRead == null ? false : this.mBuilderIsRead.booleanValue());
            return new Voicemail(this.mBuilderTimestamp, this.mBuilderNumber, this.mBuilderPhoneAccount, this.mBuilderId, this.mBuilderDuration, this.mBuilderSourcePackage, this.mBuilderSourceData, this.mBuilderUri, this.mBuilderIsRead, Boolean.valueOf(this.mBuilderHasContent), this.mBuilderTranscription);
        }
    }

    public synchronized long getId() {
        return this.mId.longValue();
    }

    public synchronized String getNumber() {
        return this.mNumber;
    }

    public synchronized PhoneAccountHandle getPhoneAccount() {
        return this.mPhoneAccount;
    }

    public synchronized long getTimestampMillis() {
        return this.mTimestamp.longValue();
    }

    public synchronized long getDuration() {
        return this.mDuration.longValue();
    }

    public synchronized String getSourcePackage() {
        return this.mSource;
    }

    public synchronized String getSourceData() {
        return this.mProviderData;
    }

    public synchronized Uri getUri() {
        return this.mUri;
    }

    public synchronized boolean isRead() {
        return this.mIsRead.booleanValue();
    }

    public synchronized boolean hasContent() {
        return this.mHasContent.booleanValue();
    }

    public synchronized String getTranscription() {
        return this.mTranscription;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.mTimestamp.longValue());
        dest.writeCharSequence(this.mNumber);
        if (this.mPhoneAccount == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(1);
            this.mPhoneAccount.writeToParcel(dest, flags);
        }
        dest.writeLong(this.mId.longValue());
        dest.writeLong(this.mDuration.longValue());
        dest.writeCharSequence(this.mSource);
        dest.writeCharSequence(this.mProviderData);
        if (this.mUri == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(1);
            this.mUri.writeToParcel(dest, flags);
        }
        if (this.mIsRead.booleanValue()) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
        }
        if (this.mHasContent.booleanValue()) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
        }
        dest.writeCharSequence(this.mTranscription);
    }

    private synchronized Voicemail(Parcel in) {
        this.mTimestamp = Long.valueOf(in.readLong());
        this.mNumber = (String) in.readCharSequence();
        if (in.readInt() > 0) {
            this.mPhoneAccount = PhoneAccountHandle.CREATOR.createFromParcel(in);
        } else {
            this.mPhoneAccount = null;
        }
        this.mId = Long.valueOf(in.readLong());
        this.mDuration = Long.valueOf(in.readLong());
        this.mSource = (String) in.readCharSequence();
        this.mProviderData = (String) in.readCharSequence();
        if (in.readInt() > 0) {
            this.mUri = Uri.CREATOR.createFromParcel(in);
        } else {
            this.mUri = null;
        }
        this.mIsRead = Boolean.valueOf(in.readInt() > 0);
        this.mHasContent = Boolean.valueOf(in.readInt() > 0);
        this.mTranscription = (String) in.readCharSequence();
    }
}
