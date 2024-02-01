package android.view.textclassifier;

import android.app.PendingIntent;
import android.app.RemoteAction;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.os.LocaleList;
import android.os.ParcelFileDescriptor;
import android.os.UserManager;
import android.provider.Browser;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.service.notification.ZenModeConfig;
import android.view.textclassifier.TextClassification;
import android.view.textclassifier.TextClassifier;
import android.view.textclassifier.TextClassifierImplNative;
import android.view.textclassifier.TextLinks;
import android.view.textclassifier.TextSelection;
import com.android.internal.R;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.util.Preconditions;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sun.misc.Cleaner;
/* loaded from: classes2.dex */
public final class TextClassifierImpl implements TextClassifier {
    private static final String LOG_TAG = "androidtc";
    private static final String MODEL_DIR = "/etc/textclassifier/";
    private static final String MODEL_FILE_REGEX = "textclassifier\\.(.*)\\.model";
    private static final String UPDATED_MODEL_FILE_PATH = "/data/misc/textclassifier/textclassifier.model";
    @GuardedBy("mLock")
    private List<ModelFile> mAllModelFiles;
    private final Context mContext;
    private final TextClassifier mFallback;
    private final GenerateLinksLogger mGenerateLinksLogger;
    private final Object mLock;
    private final Object mLoggerLock;
    @GuardedBy("mLock")
    private ModelFile mModel;
    @GuardedBy("mLock")
    private TextClassifierImplNative mNative;
    @GuardedBy("mLock")
    private Cleaner mNativeCleaner;
    @GuardedBy("mLoggerLock")
    private SelectionSessionLogger mSessionLogger;
    private final TextClassificationConstants mSettings;

    public synchronized TextClassifierImpl(Context context, TextClassificationConstants settings, TextClassifier fallback) {
        this.mLock = new Object();
        this.mLoggerLock = new Object();
        this.mContext = (Context) Preconditions.checkNotNull(context);
        this.mFallback = (TextClassifier) Preconditions.checkNotNull(fallback);
        this.mSettings = (TextClassificationConstants) Preconditions.checkNotNull(settings);
        this.mGenerateLinksLogger = new GenerateLinksLogger(this.mSettings.getGenerateLinksLogSampleRate());
    }

    public synchronized TextClassifierImpl(Context context, TextClassificationConstants settings) {
        this(context, settings, TextClassifier.NO_OP);
    }

    @Override // android.view.textclassifier.TextClassifier
    public TextSelection suggestSelection(TextSelection.Request request) {
        int start;
        int start2;
        Preconditions.checkNotNull(request);
        TextClassifier.Utils.checkMainThread();
        try {
            int rangeLength = request.getEndIndex() - request.getStartIndex();
            String string = request.getText().toString();
            if (string.length() > 0 && rangeLength <= this.mSettings.getSuggestSelectionMaxRangeLength()) {
                String localesString = concatenateLocales(request.getDefaultLocales());
                ZonedDateTime refTime = ZonedDateTime.now();
                TextClassifierImplNative nativeImpl = getNative(request.getDefaultLocales());
                if (this.mSettings.isModelDarkLaunchEnabled() && !request.isDarkLaunchAllowed()) {
                    start = request.getStartIndex();
                    start2 = request.getEndIndex();
                } else {
                    int[] startEnd = nativeImpl.suggestSelection(string, request.getStartIndex(), request.getEndIndex(), new TextClassifierImplNative.SelectionOptions(localesString));
                    int start3 = startEnd[0];
                    int i = startEnd[1];
                    start = start3;
                    start2 = i;
                }
                if (start < start2 && start >= 0 && start2 <= string.length() && start <= request.getStartIndex() && start2 >= request.getEndIndex()) {
                    TextSelection.Builder tsBuilder = new TextSelection.Builder(start, start2);
                    TextClassifierImplNative.ClassificationResult[] results = nativeImpl.classifyText(string, start, start2, new TextClassifierImplNative.ClassificationOptions(refTime.toInstant().toEpochMilli(), refTime.getZone().getId(), localesString));
                    int size = results.length;
                    for (int i2 = 0; i2 < size; i2++) {
                        tsBuilder.setEntityType(results[i2].getCollection(), results[i2].getScore());
                    }
                    return tsBuilder.setId(createId(string, request.getStartIndex(), request.getEndIndex())).build();
                }
                Log.d("androidtc", "Got bad indices for input text. Ignoring result.");
            }
        } catch (Throwable t) {
            Log.e("androidtc", "Error suggesting selection for text. No changes to selection suggested.", t);
        }
        return this.mFallback.suggestSelection(request);
    }

    @Override // android.view.textclassifier.TextClassifier
    public TextClassification classifyText(TextClassification.Request request) {
        Preconditions.checkNotNull(request);
        TextClassifier.Utils.checkMainThread();
        try {
            int rangeLength = request.getEndIndex() - request.getStartIndex();
            String string = request.getText().toString();
            if (string.length() > 0 && rangeLength <= this.mSettings.getClassifyTextMaxRangeLength()) {
                String localesString = concatenateLocales(request.getDefaultLocales());
                ZonedDateTime refTime = request.getReferenceTime() != null ? request.getReferenceTime() : ZonedDateTime.now();
                TextClassifierImplNative.ClassificationResult[] results = getNative(request.getDefaultLocales()).classifyText(string, request.getStartIndex(), request.getEndIndex(), new TextClassifierImplNative.ClassificationOptions(refTime.toInstant().toEpochMilli(), refTime.getZone().getId(), localesString));
                if (results.length > 0) {
                    return createClassificationResult(results, string, request.getStartIndex(), request.getEndIndex(), refTime.toInstant());
                }
            }
        } catch (Throwable t) {
            Log.e("androidtc", "Error getting text classification info.", t);
        }
        return this.mFallback.classifyText(request);
    }

    @Override // android.view.textclassifier.TextClassifier
    public TextLinks generateLinks(TextLinks.Request request) {
        Collection<String> entitiesToIdentify;
        String callingPackageName;
        ZonedDateTime refTime;
        Preconditions.checkNotNull(request);
        TextClassifier.Utils.checkTextLength(request.getText(), getMaxGenerateLinksTextLength());
        TextClassifier.Utils.checkMainThread();
        if (!this.mSettings.isSmartLinkifyEnabled() && request.isLegacyFallback()) {
            return TextClassifier.Utils.generateLegacyLinks(request);
        }
        String textString = request.getText().toString();
        TextLinks.Builder builder = new TextLinks.Builder(textString);
        try {
            long startTimeMs = System.currentTimeMillis();
            ZonedDateTime refTime2 = ZonedDateTime.now();
            if (request.getEntityConfig() != null) {
                entitiesToIdentify = request.getEntityConfig().resolveEntityListModifications(getEntitiesForHints(request.getEntityConfig().getHints()));
            } else {
                entitiesToIdentify = this.mSettings.getEntityListDefault();
            }
            TextClassifierImplNative nativeImpl = getNative(request.getDefaultLocales());
            TextClassifierImplNative.AnnotatedSpan[] annotations = nativeImpl.annotate(textString, new TextClassifierImplNative.AnnotationOptions(refTime2.toInstant().toEpochMilli(), refTime2.getZone().getId(), concatenateLocales(request.getDefaultLocales())));
            int length = annotations.length;
            int i = 0;
            int i2 = 0;
            while (i2 < length) {
                TextClassifierImplNative.AnnotatedSpan span = annotations[i2];
                TextClassifierImplNative.ClassificationResult[] results = span.getClassification();
                if (results.length == 0) {
                    refTime = refTime2;
                } else if (!entitiesToIdentify.contains(results[i].getCollection())) {
                    refTime = refTime2;
                } else {
                    Map<String, Float> entityScores = new HashMap<>();
                    int i3 = i;
                    while (i3 < results.length) {
                        entityScores.put(results[i3].getCollection(), Float.valueOf(results[i3].getScore()));
                        i3++;
                        refTime2 = refTime2;
                    }
                    refTime = refTime2;
                    builder.addLink(span.getStartIndex(), span.getEndIndex(), entityScores);
                }
                i2++;
                refTime2 = refTime;
                i = 0;
            }
            TextLinks links = builder.build();
            long endTimeMs = System.currentTimeMillis();
            if (request.getCallingPackageName() == null) {
                callingPackageName = this.mContext.getPackageName();
            } else {
                callingPackageName = request.getCallingPackageName();
            }
            String callingPackageName2 = callingPackageName;
            this.mGenerateLinksLogger.logGenerateLinks(request.getText(), links, callingPackageName2, endTimeMs - startTimeMs);
            return links;
        } catch (Throwable t) {
            Log.e("androidtc", "Error getting links info.", t);
            return this.mFallback.generateLinks(request);
        }
    }

    @Override // android.view.textclassifier.TextClassifier
    public int getMaxGenerateLinksTextLength() {
        return this.mSettings.getGenerateLinksMaxTextLength();
    }

    private synchronized Collection<String> getEntitiesForHints(Collection<String> hints) {
        boolean editable = hints.contains(TextClassifier.HINT_TEXT_IS_EDITABLE);
        boolean notEditable = hints.contains(TextClassifier.HINT_TEXT_IS_NOT_EDITABLE);
        boolean useDefault = editable == notEditable;
        if (useDefault) {
            return this.mSettings.getEntityListDefault();
        }
        if (editable) {
            return this.mSettings.getEntityListEditable();
        }
        return this.mSettings.getEntityListNotEditable();
    }

    @Override // android.view.textclassifier.TextClassifier
    public void onSelectionEvent(SelectionEvent event) {
        Preconditions.checkNotNull(event);
        synchronized (this.mLoggerLock) {
            if (this.mSessionLogger == null) {
                this.mSessionLogger = new SelectionSessionLogger();
            }
            this.mSessionLogger.writeEvent(event);
        }
    }

    private synchronized TextClassifierImplNative getNative(LocaleList localeList) throws FileNotFoundException {
        LocaleList emptyLocaleList;
        TextClassifierImplNative textClassifierImplNative;
        synchronized (this.mLock) {
            if (localeList != null) {
                emptyLocaleList = localeList;
            } else {
                try {
                    emptyLocaleList = LocaleList.getEmptyLocaleList();
                } catch (Throwable th) {
                    throw th;
                }
            }
            LocaleList localeList2 = emptyLocaleList;
            ModelFile bestModel = findBestModelLocked(localeList2);
            if (bestModel == null) {
                throw new FileNotFoundException("No model for " + localeList2.toLanguageTags());
            }
            if (this.mNative == null || this.mNative.isClosed() || !Objects.equals(this.mModel, bestModel)) {
                Log.d("androidtc", "Loading " + bestModel);
                ParcelFileDescriptor fd = ParcelFileDescriptor.open(new File(bestModel.getPath()), 268435456);
                this.mNative = new TextClassifierImplNative(fd.getFd());
                this.mNativeCleaner = Cleaner.create(this, new NativeCloser(this.mNative));
                closeAndLogError(fd);
                this.mModel = bestModel;
            }
            textClassifierImplNative = this.mNative;
        }
        return textClassifierImplNative;
    }

    private synchronized String createId(String text, int start, int end) {
        String createId;
        synchronized (this.mLock) {
            createId = SelectionSessionLogger.createId(text, start, end, this.mContext, this.mModel.getVersion(), this.mModel.getSupportedLocales());
        }
        return createId;
    }

    private static synchronized String concatenateLocales(LocaleList locales) {
        return locales == null ? "" : locales.toLanguageTags();
    }

    @GuardedBy("mLock")
    private synchronized ModelFile findBestModelLocked(LocaleList localeList) {
        String languages = localeList.isEmpty() ? LocaleList.getDefault().toLanguageTags() : localeList.toLanguageTags() + "," + LocaleList.getDefault().toLanguageTags();
        List<Locale.LanguageRange> languageRangeList = Locale.LanguageRange.parse(languages);
        ModelFile bestModel = null;
        for (ModelFile model : listAllModelsLocked()) {
            if (model.isAnyLanguageSupported(languageRangeList) && model.isPreferredTo(bestModel)) {
                bestModel = model;
            }
        }
        return bestModel;
    }

    @GuardedBy("mLock")
    private synchronized List<ModelFile> listAllModelsLocked() {
        ModelFile model;
        ModelFile updatedModel;
        if (this.mAllModelFiles == null) {
            List<ModelFile> allModels = new ArrayList<>();
            if (new File(UPDATED_MODEL_FILE_PATH).exists() && (updatedModel = ModelFile.fromPath(UPDATED_MODEL_FILE_PATH)) != null) {
                allModels.add(updatedModel);
            }
            File modelsDir = new File(MODEL_DIR);
            if (modelsDir.exists() && modelsDir.isDirectory()) {
                File[] modelFiles = modelsDir.listFiles();
                Pattern modelFilenamePattern = Pattern.compile(MODEL_FILE_REGEX);
                for (File modelFile : modelFiles) {
                    Matcher matcher = modelFilenamePattern.matcher(modelFile.getName());
                    if (matcher.matches() && modelFile.isFile() && (model = ModelFile.fromPath(modelFile.getAbsolutePath())) != null) {
                        allModels.add(model);
                    }
                }
            }
            this.mAllModelFiles = allModels;
        }
        return this.mAllModelFiles;
    }

    private synchronized TextClassification createClassificationResult(TextClassifierImplNative.ClassificationResult[] classifications, String text, int start, int end, Instant referenceTime) {
        String classifiedText = text.substring(start, end);
        TextClassification.Builder builder = new TextClassification.Builder().setText(classifiedText);
        int size = classifications.length;
        TextClassifierImplNative.ClassificationResult highestScoringResult = null;
        float highestScore = Float.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            builder.setEntityType(classifications[i].getCollection(), classifications[i].getScore());
            if (classifications[i].getScore() > highestScore) {
                highestScoringResult = classifications[i];
                highestScore = classifications[i].getScore();
            }
        }
        boolean isPrimaryAction = true;
        for (LabeledIntent labeledIntent : IntentFactory.create(this.mContext, referenceTime, highestScoringResult, classifiedText)) {
            RemoteAction action = labeledIntent.asRemoteAction(this.mContext);
            if (action != null) {
                if (isPrimaryAction) {
                    builder.setIcon(action.getIcon().loadDrawable(this.mContext));
                    builder.setLabel(action.getTitle().toString());
                    builder.setIntent(labeledIntent.getIntent());
                    builder.setOnClickListener(TextClassification.createIntentOnClickListener(TextClassification.createPendingIntent(this.mContext, labeledIntent.getIntent(), labeledIntent.getRequestCode())));
                    isPrimaryAction = false;
                }
                builder.addAction(action);
            }
        }
        return builder.setId(createId(text, start, end)).build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized void closeAndLogError(ParcelFileDescriptor fd) {
        try {
            fd.close();
        } catch (IOException e) {
            Log.e("androidtc", "Error closing file.", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class ModelFile {
        private final boolean mLanguageIndependent;
        private final String mName;
        private final String mPath;
        private final List<Locale> mSupportedLocales;
        private final int mVersion;

        static synchronized ModelFile fromPath(String path) {
            String[] split;
            File file = new File(path);
            try {
                ParcelFileDescriptor modelFd = ParcelFileDescriptor.open(file, 268435456);
                int version = TextClassifierImplNative.getVersion(modelFd.getFd());
                String supportedLocalesStr = TextClassifierImplNative.getLocales(modelFd.getFd());
                if (supportedLocalesStr.isEmpty()) {
                    Log.d("androidtc", "Ignoring " + file.getAbsolutePath());
                    return null;
                }
                boolean languageIndependent = supportedLocalesStr.equals(PhoneConstants.APN_TYPE_ALL);
                List<Locale> supportedLocales = new ArrayList<>();
                for (String langTag : supportedLocalesStr.split(",")) {
                    supportedLocales.add(Locale.forLanguageTag(langTag));
                }
                TextClassifierImpl.closeAndLogError(modelFd);
                return new ModelFile(path, file.getName(), version, supportedLocales, languageIndependent);
            } catch (FileNotFoundException e) {
                Log.e("androidtc", "Failed to peek " + file.getAbsolutePath(), e);
                return null;
            }
        }

        synchronized String getPath() {
            return this.mPath;
        }

        synchronized String getName() {
            return this.mName;
        }

        synchronized int getVersion() {
            return this.mVersion;
        }

        synchronized boolean isAnyLanguageSupported(List<Locale.LanguageRange> languageRanges) {
            return this.mLanguageIndependent || Locale.lookup(languageRanges, this.mSupportedLocales) != null;
        }

        synchronized List<Locale> getSupportedLocales() {
            return Collections.unmodifiableList(this.mSupportedLocales);
        }

        public synchronized boolean isPreferredTo(ModelFile model) {
            if (model == null) {
                return true;
            }
            if ((!this.mLanguageIndependent && model.mLanguageIndependent) || getVersion() > model.getVersion()) {
                return true;
            }
            return false;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || !ModelFile.class.isAssignableFrom(other.getClass())) {
                return false;
            }
            ModelFile otherModel = (ModelFile) other;
            return this.mPath.equals(otherModel.mPath);
        }

        public String toString() {
            StringJoiner localesJoiner = new StringJoiner(",");
            for (Locale locale : this.mSupportedLocales) {
                localesJoiner.add(locale.toLanguageTag());
            }
            return String.format(Locale.US, "ModelFile { path=%s name=%s version=%d locales=%s }", this.mPath, this.mName, Integer.valueOf(this.mVersion), localesJoiner.toString());
        }

        private synchronized ModelFile(String path, String name, int version, List<Locale> supportedLocales, boolean languageIndependent) {
            this.mPath = path;
            this.mName = name;
            this.mVersion = version;
            this.mSupportedLocales = supportedLocales;
            this.mLanguageIndependent = languageIndependent;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class LabeledIntent {
        static final int DEFAULT_REQUEST_CODE = 0;
        private final String mDescription;
        private final Intent mIntent;
        private final int mRequestCode;
        private final String mTitle;

        synchronized LabeledIntent(String title, String description, Intent intent, int requestCode) {
            this.mTitle = title;
            this.mDescription = description;
            this.mIntent = intent;
            this.mRequestCode = requestCode;
        }

        synchronized String getTitle() {
            return this.mTitle;
        }

        synchronized String getDescription() {
            return this.mDescription;
        }

        synchronized Intent getIntent() {
            return this.mIntent;
        }

        synchronized int getRequestCode() {
            return this.mRequestCode;
        }

        synchronized RemoteAction asRemoteAction(Context context) {
            String packageName;
            PackageManager pm = context.getPackageManager();
            ResolveInfo resolveInfo = pm.resolveActivity(this.mIntent, 0);
            if (resolveInfo == null || resolveInfo.activityInfo == null) {
                packageName = null;
            } else {
                packageName = resolveInfo.activityInfo.packageName;
            }
            Icon icon = null;
            boolean shouldShowIcon = false;
            if (packageName != null && !ZenModeConfig.SYSTEM_AUTHORITY.equals(packageName)) {
                this.mIntent.setComponent(new ComponentName(packageName, resolveInfo.activityInfo.name));
                if (resolveInfo.activityInfo.getIconResource() != 0) {
                    icon = Icon.createWithResource(packageName, resolveInfo.activityInfo.getIconResource());
                    shouldShowIcon = true;
                }
            }
            if (icon == null) {
                icon = Icon.createWithResource(ZenModeConfig.SYSTEM_AUTHORITY, (int) R.drawable.ic_more_items);
            }
            PendingIntent pendingIntent = TextClassification.createPendingIntent(context, this.mIntent, this.mRequestCode);
            if (pendingIntent == null) {
                return null;
            }
            RemoteAction action = new RemoteAction(icon, this.mTitle, this.mDescription, pendingIntent);
            action.setShouldShowIcon(shouldShowIcon);
            return action;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static final class IntentFactory {
        private static final long MIN_EVENT_FUTURE_MILLIS = TimeUnit.MINUTES.toMillis(5);
        private static final long DEFAULT_EVENT_DURATION = TimeUnit.HOURS.toMillis(1);

        private synchronized IntentFactory() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        public static synchronized List<LabeledIntent> create(Context context, Instant referenceTime, TextClassifierImplNative.ClassificationResult classification, String text) {
            char c;
            String type = classification.getCollection().trim().toLowerCase(Locale.ENGLISH);
            String text2 = text.trim();
            switch (type.hashCode()) {
                case -1271823248:
                    if (type.equals(TextClassifier.TYPE_FLIGHT_NUMBER)) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case -1147692044:
                    if (type.equals("address")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case 116079:
                    if (type.equals("url")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case 3076014:
                    if (type.equals("date")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case 96619420:
                    if (type.equals("email")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case 106642798:
                    if (type.equals("phone")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case 1793702779:
                    if (type.equals(TextClassifier.TYPE_DATE_TIME)) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                    return createForEmail(context, text2);
                case 1:
                    return createForPhone(context, text2);
                case 2:
                    return createForAddress(context, text2);
                case 3:
                    return createForUrl(context, text2);
                case 4:
                case 5:
                    if (classification.getDatetimeResult() != null) {
                        Instant parsedTime = Instant.ofEpochMilli(classification.getDatetimeResult().getTimeMsUtc());
                        return createForDatetime(context, type, referenceTime, parsedTime);
                    }
                    return new ArrayList();
                case 6:
                    return createForFlight(context, text2);
                default:
                    return new ArrayList();
            }
        }

        private static synchronized List<LabeledIntent> createForEmail(Context context, String text) {
            return Arrays.asList(new LabeledIntent(context.getString(R.string.email), context.getString(R.string.email_desc), new Intent(Intent.ACTION_SENDTO).setData(Uri.parse(String.format("mailto:%s", text))), 0), new LabeledIntent(context.getString(R.string.add_contact), context.getString(R.string.add_contact_desc), new Intent(Intent.ACTION_INSERT_OR_EDIT).setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE).putExtra("email", text), text.hashCode()));
        }

        private static synchronized List<LabeledIntent> createForPhone(Context context, String text) {
            List<LabeledIntent> actions = new ArrayList<>();
            UserManager userManager = (UserManager) context.getSystemService(UserManager.class);
            Bundle userRestrictions = userManager != null ? userManager.getUserRestrictions() : new Bundle();
            if (!userRestrictions.getBoolean(UserManager.DISALLOW_OUTGOING_CALLS, false)) {
                actions.add(new LabeledIntent(context.getString(R.string.dial), context.getString(R.string.dial_desc), new Intent(Intent.ACTION_DIAL).setData(Uri.parse(String.format("tel:%s", text))), 0));
            }
            actions.add(new LabeledIntent(context.getString(R.string.add_contact), context.getString(R.string.add_contact_desc), new Intent(Intent.ACTION_INSERT_OR_EDIT).setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE).putExtra("phone", text), text.hashCode()));
            if (!userRestrictions.getBoolean(UserManager.DISALLOW_SMS, false)) {
                actions.add(new LabeledIntent(context.getString(R.string.sms), context.getString(R.string.sms_desc), new Intent(Intent.ACTION_SENDTO).setData(Uri.parse(String.format("smsto:%s", text))), 0));
            }
            return actions;
        }

        private static synchronized List<LabeledIntent> createForAddress(Context context, String text) {
            List<LabeledIntent> actions = new ArrayList<>();
            try {
                String encText = URLEncoder.encode(text, "UTF-8");
                actions.add(new LabeledIntent(context.getString(17040211), context.getString(R.string.map_desc), new Intent("android.intent.action.VIEW").setData(Uri.parse(String.format("geo:0,0?q=%s", encText))), 0));
            } catch (UnsupportedEncodingException e) {
                Log.e("androidtc", "Could not encode address", e);
            }
            return actions;
        }

        private static synchronized List<LabeledIntent> createForUrl(Context context, String text) {
            if (Uri.parse(text).getScheme() == null) {
                text = "http://" + text;
            }
            return Arrays.asList(new LabeledIntent(context.getString(R.string.browse), context.getString(R.string.browse_desc), new Intent("android.intent.action.VIEW", Uri.parse(text)).putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName()), 0));
        }

        private static synchronized List<LabeledIntent> createForDatetime(Context context, String type, Instant referenceTime, Instant parsedTime) {
            if (referenceTime == null) {
                referenceTime = Instant.now();
            }
            List<LabeledIntent> actions = new ArrayList<>();
            actions.add(createCalendarViewIntent(context, parsedTime));
            long millisUntilEvent = referenceTime.until(parsedTime, ChronoUnit.MILLIS);
            if (millisUntilEvent > MIN_EVENT_FUTURE_MILLIS) {
                actions.add(createCalendarCreateEventIntent(context, parsedTime, type));
            }
            return actions;
        }

        private static synchronized List<LabeledIntent> createForFlight(Context context, String text) {
            return Arrays.asList(new LabeledIntent(context.getString(R.string.view_flight), context.getString(R.string.view_flight_desc), new Intent(Intent.ACTION_WEB_SEARCH).putExtra("query", text), text.hashCode()));
        }

        private static synchronized LabeledIntent createCalendarViewIntent(Context context, Instant parsedTime) {
            Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
            builder.appendPath(DropBoxManager.EXTRA_TIME);
            ContentUris.appendId(builder, parsedTime.toEpochMilli());
            return new LabeledIntent(context.getString(R.string.view_calendar), context.getString(R.string.view_calendar_desc), new Intent("android.intent.action.VIEW").setData(builder.build()), 0);
        }

        private static synchronized LabeledIntent createCalendarCreateEventIntent(Context context, Instant parsedTime, String type) {
            boolean isAllDay = "date".equals(type);
            return new LabeledIntent(context.getString(R.string.add_calendar_event), context.getString(R.string.add_calendar_event_desc), new Intent("android.intent.action.INSERT").setData(CalendarContract.Events.CONTENT_URI).putExtra("allDay", isAllDay).putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, parsedTime.toEpochMilli()).putExtra(CalendarContract.EXTRA_EVENT_END_TIME, parsedTime.toEpochMilli() + DEFAULT_EVENT_DURATION), parsedTime.hashCode());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class NativeCloser implements Runnable {
        private final TextClassifierImplNative mNative;

        NativeCloser(TextClassifierImplNative nativeImpl) {
            this.mNative = (TextClassifierImplNative) Preconditions.checkNotNull(nativeImpl);
        }

        @Override // java.lang.Runnable
        public void run() {
            this.mNative.close();
        }
    }
}
