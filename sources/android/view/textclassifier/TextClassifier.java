package android.view.textclassifier;

import android.os.LocaleList;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.view.textclassifier.TextClassification;
import android.view.textclassifier.TextLinks;
import android.view.textclassifier.TextSelection;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/* loaded from: classes2.dex */
public interface TextClassifier {
    public static final String DEFAULT_LOG_TAG = "androidtc";
    public static final String HINT_TEXT_IS_EDITABLE = "android.text_is_editable";
    public static final String HINT_TEXT_IS_NOT_EDITABLE = "android.text_is_not_editable";
    public static final int LOCAL = 0;
    public static final TextClassifier NO_OP = new TextClassifier() { // from class: android.view.textclassifier.TextClassifier.1
    };
    public static final int SYSTEM = 1;
    public static final String TYPE_ADDRESS = "address";
    public static final String TYPE_DATE = "date";
    public static final String TYPE_DATE_TIME = "datetime";
    public static final String TYPE_EMAIL = "email";
    public static final String TYPE_FLIGHT_NUMBER = "flight";
    public static final String TYPE_OTHER = "other";
    public static final String TYPE_PHONE = "phone";
    public static final String TYPE_UNKNOWN = "";
    public static final String TYPE_URL = "url";
    public static final String WIDGET_TYPE_CUSTOM_EDITTEXT = "customedit";
    public static final String WIDGET_TYPE_CUSTOM_TEXTVIEW = "customview";
    public static final String WIDGET_TYPE_CUSTOM_UNSELECTABLE_TEXTVIEW = "nosel-customview";
    public static final String WIDGET_TYPE_EDITTEXT = "edittext";
    public static final String WIDGET_TYPE_EDIT_WEBVIEW = "edit-webview";
    public static final String WIDGET_TYPE_TEXTVIEW = "textview";
    public static final String WIDGET_TYPE_UNKNOWN = "unknown";
    public static final String WIDGET_TYPE_UNSELECTABLE_TEXTVIEW = "nosel-textview";
    public static final String WIDGET_TYPE_WEBVIEW = "webview";

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface EntityType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface Hints {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface TextClassifierType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface WidgetType {
    }

    default TextSelection suggestSelection(TextSelection.Request request) {
        Preconditions.checkNotNull(request);
        Utils.checkMainThread();
        return new TextSelection.Builder(request.getStartIndex(), request.getEndIndex()).build();
    }

    default TextSelection suggestSelection(CharSequence text, int selectionStartIndex, int selectionEndIndex, LocaleList defaultLocales) {
        TextSelection.Request request = new TextSelection.Request.Builder(text, selectionStartIndex, selectionEndIndex).setDefaultLocales(defaultLocales).build();
        return suggestSelection(request);
    }

    private protected default TextSelection suggestSelection(CharSequence text, int selectionStartIndex, int selectionEndIndex, TextSelection.Options options) {
        if (options == null) {
            return suggestSelection(new TextSelection.Request.Builder(text, selectionStartIndex, selectionEndIndex).build());
        }
        if (options.getRequest() != null) {
            return suggestSelection(options.getRequest());
        }
        return suggestSelection(new TextSelection.Request.Builder(text, selectionStartIndex, selectionEndIndex).setDefaultLocales(options.getDefaultLocales()).build());
    }

    default TextClassification classifyText(TextClassification.Request request) {
        Preconditions.checkNotNull(request);
        Utils.checkMainThread();
        return TextClassification.EMPTY;
    }

    default TextClassification classifyText(CharSequence text, int startIndex, int endIndex, LocaleList defaultLocales) {
        TextClassification.Request request = new TextClassification.Request.Builder(text, startIndex, endIndex).setDefaultLocales(defaultLocales).build();
        return classifyText(request);
    }

    private protected default TextClassification classifyText(CharSequence text, int startIndex, int endIndex, TextClassification.Options options) {
        if (options == null) {
            return classifyText(new TextClassification.Request.Builder(text, startIndex, endIndex).build());
        }
        if (options.getRequest() != null) {
            return classifyText(options.getRequest());
        }
        return classifyText(new TextClassification.Request.Builder(text, startIndex, endIndex).setDefaultLocales(options.getDefaultLocales()).setReferenceTime(options.getReferenceTime()).build());
    }

    default TextLinks generateLinks(TextLinks.Request request) {
        Preconditions.checkNotNull(request);
        Utils.checkMainThread();
        return new TextLinks.Builder(request.getText().toString()).build();
    }

    private protected default TextLinks generateLinks(CharSequence text, TextLinks.Options options) {
        if (options == null) {
            return generateLinks(new TextLinks.Request.Builder(text).build());
        }
        if (options.getRequest() != null) {
            return generateLinks(options.getRequest());
        }
        return generateLinks(new TextLinks.Request.Builder(text).setDefaultLocales(options.getDefaultLocales()).setEntityConfig(options.getEntityConfig()).build());
    }

    default int getMaxGenerateLinksTextLength() {
        return Integer.MAX_VALUE;
    }

    default void onSelectionEvent(SelectionEvent event) {
    }

    default void destroy() {
    }

    default boolean isDestroyed() {
        return false;
    }

    /* loaded from: classes2.dex */
    public static final class EntityConfig implements Parcelable {
        public static final Parcelable.Creator<EntityConfig> CREATOR = new Parcelable.Creator<EntityConfig>() { // from class: android.view.textclassifier.TextClassifier.EntityConfig.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public EntityConfig createFromParcel(Parcel in) {
                return new EntityConfig(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public EntityConfig[] newArray(int size) {
                return new EntityConfig[size];
            }
        };
        private final Collection<String> mExcludedEntityTypes;
        private final Collection<String> mHints;
        private final Collection<String> mIncludedEntityTypes;
        private final boolean mUseHints;

        private synchronized EntityConfig(boolean useHints, Collection<String> hints, Collection<String> includedEntityTypes, Collection<String> excludedEntityTypes) {
            Collection<String> unmodifiableCollection;
            if (hints == null) {
                unmodifiableCollection = Collections.EMPTY_LIST;
            } else {
                unmodifiableCollection = Collections.unmodifiableCollection(new ArraySet(hints));
            }
            this.mHints = unmodifiableCollection;
            this.mExcludedEntityTypes = excludedEntityTypes == null ? Collections.EMPTY_LIST : new ArraySet<>(excludedEntityTypes);
            this.mIncludedEntityTypes = includedEntityTypes == null ? Collections.EMPTY_LIST : new ArraySet<>(includedEntityTypes);
            this.mUseHints = useHints;
        }

        public static EntityConfig createWithHints(Collection<String> hints) {
            return new EntityConfig(true, hints, null, null);
        }

        public static synchronized EntityConfig create(Collection<String> hints) {
            return createWithHints(hints);
        }

        public static EntityConfig create(Collection<String> hints, Collection<String> includedEntityTypes, Collection<String> excludedEntityTypes) {
            return new EntityConfig(true, hints, includedEntityTypes, excludedEntityTypes);
        }

        public static EntityConfig createWithExplicitEntityList(Collection<String> entityTypes) {
            return new EntityConfig(false, null, entityTypes, null);
        }

        public static synchronized EntityConfig createWithEntityList(Collection<String> entityTypes) {
            return createWithExplicitEntityList(entityTypes);
        }

        public Collection<String> resolveEntityListModifications(Collection<String> entities) {
            Set<String> finalSet = new HashSet<>();
            if (this.mUseHints) {
                finalSet.addAll(entities);
            }
            finalSet.addAll(this.mIncludedEntityTypes);
            finalSet.removeAll(this.mExcludedEntityTypes);
            return finalSet;
        }

        public Collection<String> getHints() {
            return this.mHints;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeStringList(new ArrayList(this.mHints));
            dest.writeStringList(new ArrayList(this.mExcludedEntityTypes));
            dest.writeStringList(new ArrayList(this.mIncludedEntityTypes));
            dest.writeInt(this.mUseHints ? 1 : 0);
        }

        private synchronized EntityConfig(Parcel in) {
            this.mHints = new ArraySet(in.createStringArrayList());
            this.mExcludedEntityTypes = new ArraySet(in.createStringArrayList());
            this.mIncludedEntityTypes = new ArraySet(in.createStringArrayList());
            this.mUseHints = in.readInt() == 1;
        }
    }

    /* loaded from: classes2.dex */
    public static final class Utils {
        /* JADX INFO: Access modifiers changed from: package-private */
        public static synchronized void checkArgument(CharSequence text, int startIndex, int endIndex) {
            Preconditions.checkArgument(text != null);
            Preconditions.checkArgument(startIndex >= 0);
            Preconditions.checkArgument(endIndex <= text.length());
            Preconditions.checkArgument(endIndex > startIndex);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static synchronized void checkTextLength(CharSequence text, int maxLength) {
            Preconditions.checkArgumentInRange(text.length(), 0, maxLength, "text.length()");
        }

        public static synchronized TextLinks generateLegacyLinks(TextLinks.Request request) {
            String string = request.getText().toString();
            TextLinks.Builder links = new TextLinks.Builder(string);
            Collection<String> entities = request.getEntityConfig().resolveEntityListModifications(Collections.emptyList());
            if (entities.contains("url")) {
                addLinks(links, string, "url");
            }
            if (entities.contains("phone")) {
                addLinks(links, string, "phone");
            }
            if (entities.contains("email")) {
                addLinks(links, string, "email");
            }
            return links.build();
        }

        private static synchronized void addLinks(TextLinks.Builder links, String string, String entityType) {
            Spannable spannable = new SpannableString(string);
            if (Linkify.addLinks(spannable, linkMask(entityType))) {
                URLSpan[] spans = (URLSpan[]) spannable.getSpans(0, spannable.length(), URLSpan.class);
                for (URLSpan urlSpan : spans) {
                    links.addLink(spannable.getSpanStart(urlSpan), spannable.getSpanEnd(urlSpan), entityScores(entityType), urlSpan);
                }
            }
        }

        private static synchronized int linkMask(String entityType) {
            char c;
            int hashCode = entityType.hashCode();
            if (hashCode == 116079) {
                if (entityType.equals("url")) {
                    c = 0;
                }
                c = 65535;
            } else if (hashCode != 96619420) {
                if (hashCode == 106642798 && entityType.equals("phone")) {
                    c = 1;
                }
                c = 65535;
            } else {
                if (entityType.equals("email")) {
                    c = 2;
                }
                c = 65535;
            }
            switch (c) {
                case 0:
                    return 1;
                case 1:
                    return 4;
                case 2:
                    return 2;
                default:
                    return 0;
            }
        }

        private static synchronized Map<String, Float> entityScores(String entityType) {
            Map<String, Float> scores = new ArrayMap<>();
            scores.put(entityType, Float.valueOf(1.0f));
            return scores;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static synchronized void checkMainThread() {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Log.w(TextClassifier.DEFAULT_LOG_TAG, "TextClassifier called on main thread");
            }
        }
    }
}
