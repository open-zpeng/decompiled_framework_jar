package android.print;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/* loaded from: classes2.dex */
public final class PrintDocumentInfo implements Parcelable {
    public static final int CONTENT_TYPE_DOCUMENT = 0;
    public static final int CONTENT_TYPE_PHOTO = 1;
    public static final int CONTENT_TYPE_UNKNOWN = -1;
    public static final Parcelable.Creator<PrintDocumentInfo> CREATOR = new Parcelable.Creator<PrintDocumentInfo>() { // from class: android.print.PrintDocumentInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PrintDocumentInfo createFromParcel(Parcel parcel) {
            return new PrintDocumentInfo(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PrintDocumentInfo[] newArray(int size) {
            return new PrintDocumentInfo[size];
        }
    };
    public static final int PAGE_COUNT_UNKNOWN = -1;
    private int mContentType;
    private long mDataSize;
    private String mName;
    private int mPageCount;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface ContentType {
    }

    private PrintDocumentInfo() {
    }

    private PrintDocumentInfo(PrintDocumentInfo prototype) {
        this.mName = prototype.mName;
        this.mPageCount = prototype.mPageCount;
        this.mContentType = prototype.mContentType;
        this.mDataSize = prototype.mDataSize;
    }

    private PrintDocumentInfo(Parcel parcel) {
        this.mName = (String) Preconditions.checkStringNotEmpty(parcel.readString());
        this.mPageCount = parcel.readInt();
        int i = this.mPageCount;
        Preconditions.checkArgument(i == -1 || i > 0);
        this.mContentType = parcel.readInt();
        this.mDataSize = Preconditions.checkArgumentNonnegative(parcel.readLong());
    }

    public String getName() {
        return this.mName;
    }

    public int getPageCount() {
        return this.mPageCount;
    }

    public int getContentType() {
        return this.mContentType;
    }

    public long getDataSize() {
        return this.mDataSize;
    }

    public void setDataSize(long dataSize) {
        this.mDataSize = dataSize;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.mName);
        parcel.writeInt(this.mPageCount);
        parcel.writeInt(this.mContentType);
        parcel.writeLong(this.mDataSize);
    }

    public int hashCode() {
        int i = 1 * 31;
        String str = this.mName;
        int result = i + (str != null ? str.hashCode() : 0);
        long j = this.mDataSize;
        return (((((((result * 31) + this.mContentType) * 31) + this.mPageCount) * 31) + ((int) j)) * 31) + ((int) (j >> 32));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PrintDocumentInfo other = (PrintDocumentInfo) obj;
        if (TextUtils.equals(this.mName, other.mName) && this.mContentType == other.mContentType && this.mPageCount == other.mPageCount && this.mDataSize == other.mDataSize) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "PrintDocumentInfo{name=" + this.mName + ", pageCount=" + this.mPageCount + ", contentType=" + contentTypeToString(this.mContentType) + ", dataSize=" + this.mDataSize + "}";
    }

    private String contentTypeToString(int contentType) {
        if (contentType != 0) {
            if (contentType == 1) {
                return "CONTENT_TYPE_PHOTO";
            }
            return "CONTENT_TYPE_UNKNOWN";
        }
        return "CONTENT_TYPE_DOCUMENT";
    }

    /* loaded from: classes2.dex */
    public static final class Builder {
        private final PrintDocumentInfo mPrototype;

        public Builder(String name) {
            if (TextUtils.isEmpty(name)) {
                throw new IllegalArgumentException("name cannot be empty");
            }
            this.mPrototype = new PrintDocumentInfo();
            this.mPrototype.mName = name;
        }

        public Builder setPageCount(int pageCount) {
            if (pageCount >= 0 || pageCount == -1) {
                this.mPrototype.mPageCount = pageCount;
                return this;
            }
            throw new IllegalArgumentException("pageCount must be greater than or equal to zero or DocumentInfo#PAGE_COUNT_UNKNOWN");
        }

        public Builder setContentType(int type) {
            this.mPrototype.mContentType = type;
            return this;
        }

        public PrintDocumentInfo build() {
            if (this.mPrototype.mPageCount == 0) {
                this.mPrototype.mPageCount = -1;
            }
            return new PrintDocumentInfo();
        }
    }
}
