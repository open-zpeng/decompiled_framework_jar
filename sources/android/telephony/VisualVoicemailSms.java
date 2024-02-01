package android.telephony;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.telecom.PhoneAccountHandle;
/* loaded from: classes2.dex */
public final class VisualVoicemailSms implements Parcelable {
    public static final Parcelable.Creator<VisualVoicemailSms> CREATOR = new Parcelable.Creator<VisualVoicemailSms>() { // from class: android.telephony.VisualVoicemailSms.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VisualVoicemailSms createFromParcel(Parcel in) {
            return new Builder().setPhoneAccountHandle((PhoneAccountHandle) in.readParcelable(null)).setPrefix(in.readString()).setFields(in.readBundle()).setMessageBody(in.readString()).build();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public VisualVoicemailSms[] newArray(int size) {
            return new VisualVoicemailSms[size];
        }
    };
    private final Bundle mFields;
    private final String mMessageBody;
    private final PhoneAccountHandle mPhoneAccountHandle;
    private final String mPrefix;

    synchronized VisualVoicemailSms(Builder builder) {
        this.mPhoneAccountHandle = builder.mPhoneAccountHandle;
        this.mPrefix = builder.mPrefix;
        this.mFields = builder.mFields;
        this.mMessageBody = builder.mMessageBody;
    }

    public PhoneAccountHandle getPhoneAccountHandle() {
        return this.mPhoneAccountHandle;
    }

    public String getPrefix() {
        return this.mPrefix;
    }

    public Bundle getFields() {
        return this.mFields;
    }

    public String getMessageBody() {
        return this.mMessageBody;
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        private Bundle mFields;
        private String mMessageBody;
        private PhoneAccountHandle mPhoneAccountHandle;
        private String mPrefix;

        public synchronized VisualVoicemailSms build() {
            return new VisualVoicemailSms(this);
        }

        public synchronized Builder setPhoneAccountHandle(PhoneAccountHandle phoneAccountHandle) {
            this.mPhoneAccountHandle = phoneAccountHandle;
            return this;
        }

        public synchronized Builder setPrefix(String prefix) {
            this.mPrefix = prefix;
            return this;
        }

        public synchronized Builder setFields(Bundle fields) {
            this.mFields = fields;
            return this;
        }

        public synchronized Builder setMessageBody(String messageBody) {
            this.mMessageBody = messageBody;
            return this;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(getPhoneAccountHandle(), flags);
        dest.writeString(getPrefix());
        dest.writeBundle(getFields());
        dest.writeString(getMessageBody());
    }
}
