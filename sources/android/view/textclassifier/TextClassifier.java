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
import android.view.textclassifier.ConversationActions;
import android.view.textclassifier.TextClassification;
import android.view.textclassifier.TextLanguage;
import android.view.textclassifier.TextLinks;
import android.view.textclassifier.TextSelection;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.IndentingPrintWriter;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: classes3.dex */
public interface TextClassifier {
    public static final String DEFAULT_LOG_TAG = "androidtc";
    public static final String EXTRA_FROM_TEXT_CLASSIFIER = "android.view.textclassifier.extra.FROM_TEXT_CLASSIFIER";
    public static final String HINT_TEXT_IS_EDITABLE = "android.text_is_editable";
    public static final String HINT_TEXT_IS_NOT_EDITABLE = "android.text_is_not_editable";
    public static final int LOCAL = 0;
    public static final TextClassifier NO_OP = new TextClassifier() { // from class: android.view.textclassifier.TextClassifier.1
        public String toString() {
            return "TextClassifier.NO_OP";
        }
    };
    public static final int SYSTEM = 1;
    public static final String TYPE_ADDRESS = "address";
    public static final String TYPE_DATE = "date";
    public static final String TYPE_DATE_TIME = "datetime";
    public static final String TYPE_DICTIONARY = "dictionary";
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
    public static final String WIDGET_TYPE_NOTIFICATION = "notification";
    public static final String WIDGET_TYPE_TEXTVIEW = "textview";
    public static final String WIDGET_TYPE_UNKNOWN = "unknown";
    public static final String WIDGET_TYPE_UNSELECTABLE_TEXTVIEW = "nosel-textview";
    public static final String WIDGET_TYPE_WEBVIEW = "webview";

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes3.dex */
    public @interface EntityType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes3.dex */
    public @interface Hints {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes3.dex */
    public @interface TextClassifierType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes3.dex */
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

    default TextClassification classifyText(TextClassification.Request request) {
        Preconditions.checkNotNull(request);
        Utils.checkMainThread();
        return TextClassification.EMPTY;
    }

    default TextClassification classifyText(CharSequence text, int startIndex, int endIndex, LocaleList defaultLocales) {
        TextClassification.Request request = new TextClassification.Request.Builder(text, startIndex, endIndex).setDefaultLocales(defaultLocales).build();
        return classifyText(request);
    }

    default TextLinks generateLinks(TextLinks.Request request) {
        Preconditions.checkNotNull(request);
        Utils.checkMainThread();
        return new TextLinks.Builder(request.getText().toString()).build();
    }

    default int getMaxGenerateLinksTextLength() {
        return Integer.MAX_VALUE;
    }

    default TextLanguage detectLanguage(TextLanguage.Request request) {
        Preconditions.checkNotNull(request);
        Utils.checkMainThread();
        return TextLanguage.EMPTY;
    }

    default ConversationActions suggestConversationActions(ConversationActions.Request request) {
        Preconditions.checkNotNull(request);
        Utils.checkMainThread();
        return new ConversationActions(Collections.emptyList(), (String) null);
    }

    default void onSelectionEvent(SelectionEvent event) {
    }

    default void onTextClassifierEvent(TextClassifierEvent event) {
    }

    default void destroy() {
    }

    default boolean isDestroyed() {
        return false;
    }

    default void dump(IndentingPrintWriter printWriter) {
    }

    /* loaded from: classes3.dex */
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
        private final List<String> mExcludedTypes;
        private final List<String> mHints;
        private final boolean mIncludeTypesFromTextClassifier;
        private final List<String> mIncludedTypes;

        private EntityConfig(List<String> includedEntityTypes, List<String> excludedEntityTypes, List<String> hints, boolean includeTypesFromTextClassifier) {
            this.mIncludedTypes = (List) Preconditions.checkNotNull(includedEntityTypes);
            this.mExcludedTypes = (List) Preconditions.checkNotNull(excludedEntityTypes);
            this.mHints = (List) Preconditions.checkNotNull(hints);
            this.mIncludeTypesFromTextClassifier = includeTypesFromTextClassifier;
        }

        private EntityConfig(Parcel in) {
            this.mIncludedTypes = new ArrayList();
            in.readStringList(this.mIncludedTypes);
            this.mExcludedTypes = new ArrayList();
            in.readStringList(this.mExcludedTypes);
            List<String> tmpHints = new ArrayList<>();
            in.readStringList(tmpHints);
            this.mHints = Collections.unmodifiableList(tmpHints);
            this.mIncludeTypesFromTextClassifier = in.readByte() != 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeStringList(this.mIncludedTypes);
            parcel.writeStringList(this.mExcludedTypes);
            parcel.writeStringList(this.mHints);
            parcel.writeByte(this.mIncludeTypesFromTextClassifier ? (byte) 1 : (byte) 0);
        }

        @Deprecated
        public static EntityConfig createWithHints(Collection<String> hints) {
            return new Builder().includeTypesFromTextClassifier(true).setHints(hints).build();
        }

        @Deprecated
        public static EntityConfig create(Collection<String> hints, Collection<String> includedEntityTypes, Collection<String> excludedEntityTypes) {
            return new Builder().setIncludedTypes(includedEntityTypes).setExcludedTypes(excludedEntityTypes).setHints(hints).includeTypesFromTextClassifier(true).build();
        }

        @Deprecated
        public static EntityConfig createWithExplicitEntityList(Collection<String> entityTypes) {
            return new Builder().setIncludedTypes(entityTypes).includeTypesFromTextClassifier(false).build();
        }

        public Collection<String> resolveEntityListModifications(Collection<String> entityTypes) {
            Set<String> finalSet = new HashSet<>();
            if (this.mIncludeTypesFromTextClassifier) {
                finalSet.addAll(entityTypes);
            }
            finalSet.addAll(this.mIncludedTypes);
            finalSet.removeAll(this.mExcludedTypes);
            return finalSet;
        }

        public Collection<String> getHints() {
            return this.mHints;
        }

        public boolean shouldIncludeTypesFromTextClassifier() {
            return this.mIncludeTypesFromTextClassifier;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        /* loaded from: classes3.dex */
        public static final class Builder {
            private Collection<String> mExcludedTypes;
            private Collection<String> mHints;
            private boolean mIncludeTypesFromTextClassifier = true;
            private Collection<String> mIncludedTypes;

            public Builder setIncludedTypes(Collection<String> includedTypes) {
                this.mIncludedTypes = includedTypes;
                return this;
            }

            public Builder setExcludedTypes(Collection<String> excludedTypes) {
                this.mExcludedTypes = excludedTypes;
                return this;
            }

            public Builder includeTypesFromTextClassifier(boolean includeTypesFromTextClassifier) {
                this.mIncludeTypesFromTextClassifier = includeTypesFromTextClassifier;
                return this;
            }

            public Builder setHints(Collection<String> hints) {
                this.mHints = hints;
                return this;
            }

            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v6, types: [java.util.List] */
            /* JADX WARN: Type inference failed for: r0v7, types: [java.util.List] */
            public EntityConfig build() {
                ArrayList arrayList;
                ArrayList arrayList2;
                List unmodifiableList;
                Collection<String> collection = this.mIncludedTypes;
                if (collection == null) {
                    arrayList = Collections.emptyList();
                } else {
                    arrayList = new ArrayList(collection);
                }
                Collection<String> collection2 = this.mExcludedTypes;
                if (collection2 == null) {
                    arrayList2 = Collections.emptyList();
                } else {
                    arrayList2 = new ArrayList(collection2);
                }
                Collection<String> collection3 = this.mHints;
                if (collection3 == null) {
                    unmodifiableList = Collections.emptyList();
                } else {
                    unmodifiableList = Collections.unmodifiableList(new ArrayList(collection3));
                }
                return new EntityConfig(arrayList, arrayList2, unmodifiableList, this.mIncludeTypesFromTextClassifier);
            }
        }
    }

    /* loaded from: classes3.dex */
    public static final class Utils {
        @GuardedBy({"WORD_ITERATOR"})
        private static final BreakIterator WORD_ITERATOR = BreakIterator.getWordInstance();

        /* JADX INFO: Access modifiers changed from: package-private */
        public static void checkArgument(CharSequence text, int startIndex, int endIndex) {
            Preconditions.checkArgument(text != null);
            Preconditions.checkArgument(startIndex >= 0);
            Preconditions.checkArgument(endIndex <= text.length());
            Preconditions.checkArgument(endIndex > startIndex);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static void checkTextLength(CharSequence text, int maxLength) {
            Preconditions.checkArgumentInRange(text.length(), 0, maxLength, "text.length()");
        }

        public static String getSubString(String text, int start, int end, int minimumLength) {
            String substring;
            Preconditions.checkArgument(start >= 0);
            Preconditions.checkArgument(end <= text.length());
            Preconditions.checkArgument(start <= end);
            if (text.length() < minimumLength) {
                return text;
            }
            int length = end - start;
            if (length >= minimumLength) {
                return text.substring(start, end);
            }
            int offset = (minimumLength - length) / 2;
            int iterStart = Math.max(0, Math.min(start - offset, text.length() - minimumLength));
            int iterEnd = Math.min(text.length(), iterStart + minimumLength);
            synchronized (WORD_ITERATOR) {
                WORD_ITERATOR.setText(text);
                int iterStart2 = WORD_ITERATOR.isBoundary(iterStart) ? iterStart : Math.max(0, WORD_ITERATOR.preceding(iterStart));
                int iterEnd2 = WORD_ITERATOR.isBoundary(iterEnd) ? iterEnd : Math.max(iterEnd, WORD_ITERATOR.following(iterEnd));
                WORD_ITERATOR.setText("");
                substring = text.substring(iterStart2, iterEnd2);
            }
            return substring;
        }

        public static TextLinks generateLegacyLinks(TextLinks.Request request) {
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

        private static void addLinks(TextLinks.Builder links, String string, String entityType) {
            Spannable spannable = new SpannableString(string);
            if (Linkify.addLinks(spannable, linkMask(entityType))) {
                URLSpan[] spans = (URLSpan[]) spannable.getSpans(0, spannable.length(), URLSpan.class);
                for (URLSpan urlSpan : spans) {
                    links.addLink(spannable.getSpanStart(urlSpan), spannable.getSpanEnd(urlSpan), entityScores(entityType), urlSpan);
                }
            }
        }

        private static int linkMask(String entityType) {
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
            if (c != 0) {
                if (c != 1) {
                    return c != 2 ? 0 : 2;
                }
                return 4;
            }
            return 1;
        }

        private static Map<String, Float> entityScores(String entityType) {
            Map<String, Float> scores = new ArrayMap<>();
            scores.put(entityType, Float.valueOf(1.0f));
            return scores;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public static void checkMainThread() {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Log.w(TextClassifier.DEFAULT_LOG_TAG, "TextClassifier called on main thread");
            }
        }
    }
}
