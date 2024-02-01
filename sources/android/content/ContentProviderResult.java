package android.content;

import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.util.Preconditions;

/* loaded from: classes.dex */
public class ContentProviderResult implements Parcelable {
    public static final Parcelable.Creator<ContentProviderResult> CREATOR = new Parcelable.Creator<ContentProviderResult>() { // from class: android.content.ContentProviderResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ContentProviderResult createFromParcel(Parcel source) {
            return new ContentProviderResult(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ContentProviderResult[] newArray(int size) {
            return new ContentProviderResult[size];
        }
    };
    public final Integer count;
    public final String failure;
    public final Uri uri;

    public ContentProviderResult(Uri uri) {
        this((Uri) Preconditions.checkNotNull(uri), null, null);
    }

    public ContentProviderResult(int count) {
        this(null, Integer.valueOf(count), null);
    }

    public ContentProviderResult(String failure) {
        this(null, null, failure);
    }

    public ContentProviderResult(Uri uri, Integer count, String failure) {
        this.uri = uri;
        this.count = count;
        this.failure = failure;
    }

    public ContentProviderResult(Parcel source) {
        if (source.readInt() != 0) {
            this.uri = Uri.CREATOR.createFromParcel(source);
        } else {
            this.uri = null;
        }
        if (source.readInt() != 0) {
            this.count = Integer.valueOf(source.readInt());
        } else {
            this.count = null;
        }
        if (source.readInt() != 0) {
            this.failure = source.readString();
        } else {
            this.failure = null;
        }
    }

    public ContentProviderResult(ContentProviderResult cpr, int userId) {
        this.uri = ContentProvider.maybeAddUserId(cpr.uri, userId);
        this.count = cpr.count;
        this.failure = cpr.failure;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        if (this.uri != null) {
            dest.writeInt(1);
            this.uri.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
        if (this.count != null) {
            dest.writeInt(1);
            dest.writeInt(this.count.intValue());
        } else {
            dest.writeInt(0);
        }
        if (this.failure != null) {
            dest.writeInt(1);
            dest.writeString(this.failure);
            return;
        }
        dest.writeInt(0);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ContentProviderResult(");
        if (this.uri != null) {
            sb.append("uri=" + this.uri + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        }
        if (this.count != null) {
            sb.append("count=" + this.count + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        }
        if (this.uri != null) {
            sb.append("failure=" + this.failure + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }
}
