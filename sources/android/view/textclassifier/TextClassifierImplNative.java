package android.view.textclassifier;

import android.content.res.AssetFileDescriptor;
/* loaded from: classes2.dex */
final class TextClassifierImplNative {
    private final Object mCloseLock = new Object();
    private long mModelPtr;

    private static native AnnotatedSpan[] nativeAnnotate(long j, String str, AnnotationOptions annotationOptions);

    private static native ClassificationResult[] nativeClassifyText(long j, String str, int i, int i2, ClassificationOptions classificationOptions);

    private static native void nativeClose(long j);

    private static native String nativeGetLocales(int i);

    private static native int nativeGetVersion(int i);

    private static native long nativeNew(int i);

    private static native long nativeNewFromAssetFileDescriptor(AssetFileDescriptor assetFileDescriptor, long j, long j2);

    private static native long nativeNewFromPath(String str);

    private static native int[] nativeSuggestSelection(long j, String str, int i, int i2, SelectionOptions selectionOptions);

    static {
        System.loadLibrary("textclassifier");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized TextClassifierImplNative(int fd) {
        this.mModelPtr = nativeNew(fd);
        if (this.mModelPtr == 0) {
            throw new IllegalArgumentException("Couldn't initialize TC from file descriptor.");
        }
    }

    synchronized TextClassifierImplNative(String path) {
        this.mModelPtr = nativeNewFromPath(path);
        if (this.mModelPtr == 0) {
            throw new IllegalArgumentException("Couldn't initialize TC from given file.");
        }
    }

    synchronized TextClassifierImplNative(AssetFileDescriptor afd) {
        this.mModelPtr = nativeNewFromAssetFileDescriptor(afd, afd.getStartOffset(), afd.getLength());
        if (this.mModelPtr == 0) {
            throw new IllegalArgumentException("Couldn't initialize TC from given AssetFileDescriptor");
        }
    }

    public synchronized int[] suggestSelection(String context, int selectionBegin, int selectionEnd, SelectionOptions options) {
        return nativeSuggestSelection(this.mModelPtr, context, selectionBegin, selectionEnd, options);
    }

    public synchronized ClassificationResult[] classifyText(String context, int selectionBegin, int selectionEnd, ClassificationOptions options) {
        return nativeClassifyText(this.mModelPtr, context, selectionBegin, selectionEnd, options);
    }

    public synchronized AnnotatedSpan[] annotate(String text, AnnotationOptions options) {
        return nativeAnnotate(this.mModelPtr, text, options);
    }

    public synchronized void close() {
        synchronized (this.mCloseLock) {
            if (!isClosed()) {
                nativeClose(this.mModelPtr);
                this.mModelPtr = 0L;
            }
        }
    }

    public boolean isClosed() {
        return this.mModelPtr == 0;
    }

    public static synchronized String getLocales(int fd) {
        return nativeGetLocales(fd);
    }

    public static synchronized int getVersion(int fd) {
        return nativeGetVersion(fd);
    }

    /* loaded from: classes2.dex */
    public static final class DatetimeResult {
        static final int GRANULARITY_DAY = 3;
        static final int GRANULARITY_HOUR = 4;
        static final int GRANULARITY_MINUTE = 5;
        static final int GRANULARITY_MONTH = 1;
        static final int GRANULARITY_SECOND = 6;
        static final int GRANULARITY_WEEK = 2;
        static final int GRANULARITY_YEAR = 0;
        private final int mGranularity;
        private final long mTimeMsUtc;

        synchronized DatetimeResult(long timeMsUtc, int granularity) {
            this.mGranularity = granularity;
            this.mTimeMsUtc = timeMsUtc;
        }

        public synchronized long getTimeMsUtc() {
            return this.mTimeMsUtc;
        }

        public synchronized int getGranularity() {
            return this.mGranularity;
        }
    }

    /* loaded from: classes2.dex */
    public static final class ClassificationResult {
        private final String mCollection;
        private final DatetimeResult mDatetimeResult;
        private final float mScore;

        synchronized ClassificationResult(String collection, float score, DatetimeResult datetimeResult) {
            this.mCollection = collection;
            this.mScore = score;
            this.mDatetimeResult = datetimeResult;
        }

        public synchronized String getCollection() {
            if (this.mCollection.equals("date") && this.mDatetimeResult != null) {
                switch (this.mDatetimeResult.getGranularity()) {
                    case 4:
                    case 5:
                    case 6:
                        return TextClassifier.TYPE_DATE_TIME;
                    default:
                        return "date";
                }
            }
            return this.mCollection;
        }

        public synchronized float getScore() {
            return this.mScore;
        }

        public synchronized DatetimeResult getDatetimeResult() {
            return this.mDatetimeResult;
        }
    }

    /* loaded from: classes2.dex */
    public static final class AnnotatedSpan {
        private final ClassificationResult[] mClassification;
        private final int mEndIndex;
        private final int mStartIndex;

        synchronized AnnotatedSpan(int startIndex, int endIndex, ClassificationResult[] classification) {
            this.mStartIndex = startIndex;
            this.mEndIndex = endIndex;
            this.mClassification = classification;
        }

        public synchronized int getStartIndex() {
            return this.mStartIndex;
        }

        public synchronized int getEndIndex() {
            return this.mEndIndex;
        }

        public synchronized ClassificationResult[] getClassification() {
            return this.mClassification;
        }
    }

    /* loaded from: classes2.dex */
    public static final class SelectionOptions {
        private final String mLocales;

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized SelectionOptions(String locales) {
            this.mLocales = locales;
        }

        public synchronized String getLocales() {
            return this.mLocales;
        }
    }

    /* loaded from: classes2.dex */
    public static final class ClassificationOptions {
        private final String mLocales;
        private final long mReferenceTimeMsUtc;
        private final String mReferenceTimezone;

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized ClassificationOptions(long referenceTimeMsUtc, String referenceTimezone, String locale) {
            this.mReferenceTimeMsUtc = referenceTimeMsUtc;
            this.mReferenceTimezone = referenceTimezone;
            this.mLocales = locale;
        }

        public synchronized long getReferenceTimeMsUtc() {
            return this.mReferenceTimeMsUtc;
        }

        public synchronized String getReferenceTimezone() {
            return this.mReferenceTimezone;
        }

        public synchronized String getLocale() {
            return this.mLocales;
        }
    }

    /* loaded from: classes2.dex */
    public static final class AnnotationOptions {
        private final String mLocales;
        private final long mReferenceTimeMsUtc;
        private final String mReferenceTimezone;

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized AnnotationOptions(long referenceTimeMsUtc, String referenceTimezone, String locale) {
            this.mReferenceTimeMsUtc = referenceTimeMsUtc;
            this.mReferenceTimezone = referenceTimezone;
            this.mLocales = locale;
        }

        public synchronized long getReferenceTimeMsUtc() {
            return this.mReferenceTimeMsUtc;
        }

        public synchronized String getReferenceTimezone() {
            return this.mReferenceTimezone;
        }

        public synchronized String getLocale() {
            return this.mLocales;
        }
    }
}
