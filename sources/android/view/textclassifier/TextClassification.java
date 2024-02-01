package android.view.textclassifier;

import android.app.PendingIntent;
import android.app.RemoteAction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.LocaleList;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArrayMap;
import android.view.View;
import android.view.textclassifier.TextClassifier;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
/* loaded from: classes2.dex */
public final class TextClassification implements Parcelable {
    private static final String LOG_TAG = "TextClassification";
    private static final int MAX_LEGACY_ICON_SIZE = 192;
    private final List<RemoteAction> mActions;
    private final EntityConfidence mEntityConfidence;
    private final String mId;
    private final Drawable mLegacyIcon;
    private final Intent mLegacyIntent;
    private final String mLegacyLabel;
    private final View.OnClickListener mLegacyOnClickListener;
    private final String mText;
    public static final TextClassification EMPTY = new Builder().build();
    public static final Parcelable.Creator<TextClassification> CREATOR = new Parcelable.Creator<TextClassification>() { // from class: android.view.textclassifier.TextClassification.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TextClassification createFromParcel(Parcel in) {
            return new TextClassification(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public TextClassification[] newArray(int size) {
            return new TextClassification[size];
        }
    };

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    private @interface IntentType {
        public static final int ACTIVITY = 0;
        public static final int SERVICE = 1;
        public static final int UNSUPPORTED = -1;
    }

    private synchronized TextClassification(String text, Drawable legacyIcon, String legacyLabel, Intent legacyIntent, View.OnClickListener legacyOnClickListener, List<RemoteAction> actions, Map<String, Float> entityConfidence, String id) {
        this.mText = text;
        this.mLegacyIcon = legacyIcon;
        this.mLegacyLabel = legacyLabel;
        this.mLegacyIntent = legacyIntent;
        this.mLegacyOnClickListener = legacyOnClickListener;
        this.mActions = Collections.unmodifiableList(actions);
        this.mEntityConfidence = new EntityConfidence(entityConfidence);
        this.mId = id;
    }

    public String getText() {
        return this.mText;
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

    public List<RemoteAction> getActions() {
        return this.mActions;
    }

    @Deprecated
    public Drawable getIcon() {
        return this.mLegacyIcon;
    }

    @Deprecated
    public CharSequence getLabel() {
        return this.mLegacyLabel;
    }

    @Deprecated
    public Intent getIntent() {
        return this.mLegacyIntent;
    }

    public View.OnClickListener getOnClickListener() {
        return this.mLegacyOnClickListener;
    }

    public String getId() {
        return this.mId;
    }

    public String toString() {
        return String.format(Locale.US, "TextClassification {text=%s, entities=%s, actions=%s, id=%s}", this.mText, this.mEntityConfidence, this.mActions, this.mId);
    }

    public static synchronized View.OnClickListener createIntentOnClickListener(final PendingIntent intent) {
        Preconditions.checkNotNull(intent);
        return new View.OnClickListener() { // from class: android.view.textclassifier.-$$Lambda$TextClassification$ysasaE5ZkXkkzjVWIJ06GTV92-g
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TextClassification.lambda$createIntentOnClickListener$0(PendingIntent.this, view);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$createIntentOnClickListener$0(PendingIntent intent, View v) {
        try {
            intent.send();
        } catch (PendingIntent.CanceledException e) {
            Log.e(LOG_TAG, "Error sending PendingIntent", e);
        }
    }

    public static synchronized PendingIntent createPendingIntent(Context context, Intent intent, int requestCode) {
        switch (getIntentType(intent, context)) {
            case 0:
                return PendingIntent.getActivity(context, requestCode, intent, 134217728);
            case 1:
                return PendingIntent.getService(context, requestCode, intent, 134217728);
            default:
                return null;
        }
    }

    private static synchronized int getIntentType(Intent intent, Context context) {
        Preconditions.checkArgument(context != null);
        Preconditions.checkArgument(intent != null);
        ResolveInfo activityRI = context.getPackageManager().resolveActivity(intent, 0);
        if (activityRI != null) {
            if (context.getPackageName().equals(activityRI.activityInfo.packageName)) {
                return 0;
            }
            boolean exported = activityRI.activityInfo.exported;
            if (exported && hasPermission(context, activityRI.activityInfo.permission)) {
                return 0;
            }
        }
        ResolveInfo serviceRI = context.getPackageManager().resolveService(intent, 0);
        if (serviceRI != null) {
            if (context.getPackageName().equals(serviceRI.serviceInfo.packageName)) {
                return 1;
            }
            boolean exported2 = serviceRI.serviceInfo.exported;
            if (exported2 && hasPermission(context, serviceRI.serviceInfo.permission)) {
                return 1;
            }
            return -1;
        }
        return -1;
    }

    private static synchronized boolean hasPermission(Context context, String permission) {
        return permission == null || context.checkSelfPermission(permission) == 0;
    }

    /* loaded from: classes2.dex */
    public static final class Builder {
        private List<RemoteAction> mActions = new ArrayList();
        private final Map<String, Float> mEntityConfidence = new ArrayMap();
        private String mId;
        private Drawable mLegacyIcon;
        private Intent mLegacyIntent;
        private String mLegacyLabel;
        private View.OnClickListener mLegacyOnClickListener;
        private String mText;

        public Builder setText(String text) {
            this.mText = text;
            return this;
        }

        public Builder setEntityType(String type, float confidenceScore) {
            this.mEntityConfidence.put(type, Float.valueOf(confidenceScore));
            return this;
        }

        public Builder addAction(RemoteAction action) {
            Preconditions.checkArgument(action != null);
            this.mActions.add(action);
            return this;
        }

        @Deprecated
        public Builder setIcon(Drawable icon) {
            this.mLegacyIcon = icon;
            return this;
        }

        @Deprecated
        public Builder setLabel(String label) {
            this.mLegacyLabel = label;
            return this;
        }

        @Deprecated
        public Builder setIntent(Intent intent) {
            this.mLegacyIntent = intent;
            return this;
        }

        @Deprecated
        public Builder setOnClickListener(View.OnClickListener onClickListener) {
            this.mLegacyOnClickListener = onClickListener;
            return this;
        }

        public Builder setId(String id) {
            this.mId = id;
            return this;
        }

        public TextClassification build() {
            return new TextClassification(this.mText, this.mLegacyIcon, this.mLegacyLabel, this.mLegacyIntent, this.mLegacyOnClickListener, this.mActions, this.mEntityConfidence, this.mId);
        }
    }

    /* loaded from: classes2.dex */
    public static final class Request implements Parcelable {
        public static final Parcelable.Creator<Request> CREATOR = new Parcelable.Creator<Request>() { // from class: android.view.textclassifier.TextClassification.Request.1
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
        private final LocaleList mDefaultLocales;
        private final int mEndIndex;
        private final ZonedDateTime mReferenceTime;
        private final int mStartIndex;
        private final CharSequence mText;

        private synchronized Request(CharSequence text, int startIndex, int endIndex, LocaleList defaultLocales, ZonedDateTime referenceTime) {
            this.mText = text;
            this.mStartIndex = startIndex;
            this.mEndIndex = endIndex;
            this.mDefaultLocales = defaultLocales;
            this.mReferenceTime = referenceTime;
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

        public LocaleList getDefaultLocales() {
            return this.mDefaultLocales;
        }

        public ZonedDateTime getReferenceTime() {
            return this.mReferenceTime;
        }

        /* loaded from: classes2.dex */
        public static final class Builder {
            private LocaleList mDefaultLocales;
            private final int mEndIndex;
            private ZonedDateTime mReferenceTime;
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

            public Builder setReferenceTime(ZonedDateTime referenceTime) {
                this.mReferenceTime = referenceTime;
                return this;
            }

            public Request build() {
                return new Request(this.mText, this.mStartIndex, this.mEndIndex, this.mDefaultLocales, this.mReferenceTime);
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
            dest.writeInt(this.mReferenceTime != null ? 1 : 0);
            if (this.mReferenceTime != null) {
                dest.writeString(this.mReferenceTime.toString());
            }
        }

        private synchronized Request(Parcel in) {
            this.mText = in.readString();
            this.mStartIndex = in.readInt();
            this.mEndIndex = in.readInt();
            this.mDefaultLocales = in.readInt() == 0 ? null : LocaleList.CREATOR.createFromParcel(in);
            this.mReferenceTime = in.readInt() != 0 ? ZonedDateTime.parse(in.readString()) : null;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mText);
        dest.writeTypedList(this.mActions);
        this.mEntityConfidence.writeToParcel(dest, flags);
        dest.writeString(this.mId);
    }

    private synchronized TextClassification(Parcel in) {
        this.mText = in.readString();
        this.mActions = in.createTypedArrayList(RemoteAction.CREATOR);
        if (!this.mActions.isEmpty()) {
            RemoteAction action = this.mActions.get(0);
            this.mLegacyIcon = maybeLoadDrawable(action.getIcon());
            this.mLegacyLabel = action.getTitle().toString();
            this.mLegacyOnClickListener = createIntentOnClickListener(this.mActions.get(0).getActionIntent());
        } else {
            this.mLegacyIcon = null;
            this.mLegacyLabel = null;
            this.mLegacyOnClickListener = null;
        }
        this.mLegacyIntent = null;
        this.mEntityConfidence = EntityConfidence.CREATOR.createFromParcel(in);
        this.mId = in.readString();
    }

    private static synchronized Drawable maybeLoadDrawable(Icon icon) {
        if (icon == null) {
            return null;
        }
        int type = icon.getType();
        if (type != 1) {
            if (type != 3) {
                if (type != 5) {
                    return null;
                }
                return new AdaptiveIconDrawable((Drawable) null, new BitmapDrawable(Resources.getSystem(), icon.getBitmap()));
            }
            return new BitmapDrawable(Resources.getSystem(), BitmapFactory.decodeByteArray(icon.getDataBytes(), icon.getDataOffset(), icon.getDataLength()));
        }
        return new BitmapDrawable(Resources.getSystem(), icon.getBitmap());
    }

    /* loaded from: classes2.dex */
    public static final class Options {
        private LocaleList mDefaultLocales;
        private ZonedDateTime mReferenceTime;
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
            options.setReferenceTime(request.getReferenceTime());
            return options;
        }

        public synchronized Options setDefaultLocales(LocaleList defaultLocales) {
            this.mDefaultLocales = defaultLocales;
            return this;
        }

        public synchronized Options setReferenceTime(ZonedDateTime referenceTime) {
            this.mReferenceTime = referenceTime;
            return this;
        }

        public synchronized LocaleList getDefaultLocales() {
            return this.mDefaultLocales;
        }

        public synchronized ZonedDateTime getReferenceTime() {
            return this.mReferenceTime;
        }

        public synchronized Request getRequest() {
            return this.mRequest;
        }

        public synchronized TextClassificationSessionId getSessionId() {
            return this.mSessionId;
        }
    }
}
