package android.view.textclassifier;

import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArrayMap;
import android.view.textclassifier.TextClassifier;
import com.android.internal.util.Preconditions;
import java.util.Locale;
import java.util.Map;
/* loaded from: classes2.dex */
public final class TextSelection implements Parcelable {
    public static final Parcelable.Creator<TextSelection> CREATOR = new Parcelable.Creator<TextSelection>() { // from class: android.view.textclassifier.TextSelection.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TextSelection createFromParcel(Parcel in) {
            return new TextSelection(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TextSelection[] newArray(int size) {
            return new TextSelection[size];
        }
    };
    private final int mEndIndex;
    private final EntityConfidence mEntityConfidence;
    private final String mId;
    private final int mStartIndex;

    private synchronized TextSelection(int startIndex, int endIndex, Map<String, Float> entityConfidence, String id) {
        this.mStartIndex = startIndex;
        this.mEndIndex = endIndex;
        this.mEntityConfidence = new EntityConfidence(entityConfidence);
        this.mId = id;
    }

    public int getSelectionStartIndex() {
        return this.mStartIndex;
    }

    public int getSelectionEndIndex() {
        return this.mEndIndex;
    }

    public int getEntityCount() {
        return this.mEntityConfidence.getEntities().size();
    }

    public String getEntity(int index) {
        return this.mEntityConfidence.getEntities().get(index);
    }

    public float getConfidenceScore(String entity) {
        return this.mEntityConfidence.getConfidenceScore(entity);
    }

    public String getId() {
        return this.mId;
    }

    public String toString() {
        return String.format(Locale.US, "TextSelection {id=%s, startIndex=%d, endIndex=%d, entities=%s}", this.mId, Integer.valueOf(this.mStartIndex), Integer.valueOf(this.mEndIndex), this.mEntityConfidence);
    }

    /* loaded from: classes2.dex */
    public static final class Builder {
        private final int mEndIndex;
        private final Map<String, Float> mEntityConfidence = new ArrayMap();
        private String mId;
        private final int mStartIndex;

        public Builder(int startIndex, int endIndex) {
            Preconditions.checkArgument(startIndex >= 0);
            Preconditions.checkArgument(endIndex > startIndex);
            this.mStartIndex = startIndex;
            this.mEndIndex = endIndex;
        }

        public Builder setEntityType(String type, float confidenceScore) {
            Preconditions.checkNotNull(type);
            this.mEntityConfidence.put(type, Float.valueOf(confidenceScore));
            return this;
        }

        public Builder setId(String id) {
            this.mId = id;
            return this;
        }

        public TextSelection build() {
            return new TextSelection(this.mStartIndex, this.mEndIndex, this.mEntityConfidence, this.mId);
        }
    }

    /* loaded from: classes2.dex */
    public static final class Request implements Parcelable {
        public static final Parcelable.Creator<Request> CREATOR = new Parcelable.Creator<Request>() { // from class: android.view.textclassifier.TextSelection.Request.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Request createFromParcel(Parcel in) {
                return new Request(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Request[] newArray(int size) {
                return new Request[size];
            }
        };
        private final boolean mDarkLaunchAllowed;
        private final LocaleList mDefaultLocales;
        private final int mEndIndex;
        private final int mStartIndex;
        private final CharSequence mText;

        private synchronized Request(CharSequence text, int startIndex, int endIndex, LocaleList defaultLocales, boolean darkLaunchAllowed) {
            this.mText = text;
            this.mStartIndex = startIndex;
            this.mEndIndex = endIndex;
            this.mDefaultLocales = defaultLocales;
            this.mDarkLaunchAllowed = darkLaunchAllowed;
        }

        public CharSequence getText() {
            return this.mText;
        }

        public int getStartIndex() {
            return this.mStartIndex;
        }

        public int getEndIndex() {
            return this.mEndIndex;
        }

        public synchronized boolean isDarkLaunchAllowed() {
            return this.mDarkLaunchAllowed;
        }

        public LocaleList getDefaultLocales() {
            return this.mDefaultLocales;
        }

        /* loaded from: classes2.dex */
        public static final class Builder {
            private boolean mDarkLaunchAllowed;
            private LocaleList mDefaultLocales;
            private final int mEndIndex;
            private final int mStartIndex;
            private final CharSequence mText;

            public Builder(CharSequence text, int startIndex, int endIndex) {
                TextClassifier.Utils.checkArgument(text, startIndex, endIndex);
                this.mText = text;
                this.mStartIndex = startIndex;
                this.mEndIndex = endIndex;
            }

            public Builder setDefaultLocales(LocaleList defaultLocales) {
                this.mDefaultLocales = defaultLocales;
                return this;
            }

            public synchronized Builder setDarkLaunchAllowed(boolean allowed) {
                this.mDarkLaunchAllowed = allowed;
                return this;
            }

            public Request build() {
                return new Request(this.mText, this.mStartIndex, this.mEndIndex, this.mDefaultLocales, this.mDarkLaunchAllowed);
            }
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mText.toString());
            dest.writeInt(this.mStartIndex);
            dest.writeInt(this.mEndIndex);
            dest.writeInt(this.mDefaultLocales != null ? 1 : 0);
            if (this.mDefaultLocales != null) {
                this.mDefaultLocales.writeToParcel(dest, flags);
            }
        }

        private synchronized Request(Parcel in) {
            this.mText = in.readString();
            this.mStartIndex = in.readInt();
            this.mEndIndex = in.readInt();
            this.mDefaultLocales = in.readInt() == 0 ? null : LocaleList.CREATOR.createFromParcel(in);
            this.mDarkLaunchAllowed = false;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mStartIndex);
        dest.writeInt(this.mEndIndex);
        this.mEntityConfidence.writeToParcel(dest, flags);
        dest.writeString(this.mId);
    }

    private synchronized TextSelection(Parcel in) {
        this.mStartIndex = in.readInt();
        this.mEndIndex = in.readInt();
        this.mEntityConfidence = EntityConfidence.CREATOR.createFromParcel(in);
        this.mId = in.readString();
    }

    /* loaded from: classes2.dex */
    public static final class Options {
        private boolean mDarkLaunchAllowed;
        private LocaleList mDefaultLocales;
        private final Request mRequest;
        private final TextClassificationSessionId mSessionId;

        public synchronized Options() {
            this(null, null);
        }

        private synchronized Options(TextClassificationSessionId sessionId, Request request) {
            this.mSessionId = sessionId;
            this.mRequest = request;
        }

        public static synchronized Options from(TextClassificationSessionId sessionId, Request request) {
            Options options = new Options(sessionId, request);
            options.setDefaultLocales(request.getDefaultLocales());
            return options;
        }

        public synchronized Options setDefaultLocales(LocaleList defaultLocales) {
            this.mDefaultLocales = defaultLocales;
            return this;
        }

        public synchronized LocaleList getDefaultLocales() {
            return this.mDefaultLocales;
        }

        public synchronized Request getRequest() {
            return this.mRequest;
        }

        public synchronized TextClassificationSessionId getSessionId() {
            return this.mSessionId;
        }
    }
}
