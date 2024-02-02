package android.view.textclassifier;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.util.Preconditions;
import java.util.Locale;
/* loaded from: classes2.dex */
public final class TextClassificationContext implements Parcelable {
    public static final Parcelable.Creator<TextClassificationContext> CREATOR = new Parcelable.Creator<TextClassificationContext>() { // from class: android.view.textclassifier.TextClassificationContext.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TextClassificationContext createFromParcel(Parcel parcel) {
            return new TextClassificationContext(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TextClassificationContext[] newArray(int size) {
            return new TextClassificationContext[size];
        }
    };
    private final String mPackageName;
    private final String mWidgetType;
    private final String mWidgetVersion;

    private synchronized TextClassificationContext(String packageName, String widgetType, String widgetVersion) {
        this.mPackageName = (String) Preconditions.checkNotNull(packageName);
        this.mWidgetType = (String) Preconditions.checkNotNull(widgetType);
        this.mWidgetVersion = widgetVersion;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public String getWidgetType() {
        return this.mWidgetType;
    }

    public String getWidgetVersion() {
        return this.mWidgetVersion;
    }

    public String toString() {
        return String.format(Locale.US, "TextClassificationContext{packageName=%s, widgetType=%s, widgetVersion=%s}", this.mPackageName, this.mWidgetType, this.mWidgetVersion);
    }

    /* loaded from: classes2.dex */
    public static final class Builder {
        private final String mPackageName;
        private final String mWidgetType;
        private String mWidgetVersion;

        public Builder(String packageName, String widgetType) {
            this.mPackageName = (String) Preconditions.checkNotNull(packageName);
            this.mWidgetType = (String) Preconditions.checkNotNull(widgetType);
        }

        public Builder setWidgetVersion(String widgetVersion) {
            this.mWidgetVersion = widgetVersion;
            return this;
        }

        public TextClassificationContext build() {
            return new TextClassificationContext(this.mPackageName, this.mWidgetType, this.mWidgetVersion);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.mPackageName);
        parcel.writeString(this.mWidgetType);
        parcel.writeString(this.mWidgetVersion);
    }

    private synchronized TextClassificationContext(Parcel in) {
        this.mPackageName = in.readString();
        this.mWidgetType = in.readString();
        this.mWidgetVersion = in.readString();
    }
}
