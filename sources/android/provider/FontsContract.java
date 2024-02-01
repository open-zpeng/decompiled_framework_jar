package android.provider;

import android.app.job.JobInfo;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.graphics.fonts.FontStyle;
import android.graphics.fonts.FontVariationAxis;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.FontsContract;
import android.util.Log;
import android.util.LruCache;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: classes2.dex */
public class FontsContract {
    private static final long SYNC_FONT_FETCH_TIMEOUT_MS = 500;
    private static final String TAG = "FontsContract";
    private static final int THREAD_RENEWAL_THRESHOLD_MS = 10000;
    private static volatile Context sContext;
    @GuardedBy({"sLock"})
    private static Handler sHandler;
    @GuardedBy({"sLock"})
    private static Set<String> sInQueueSet;
    @GuardedBy({"sLock"})
    private static HandlerThread sThread;
    private static final Object sLock = new Object();
    private static final LruCache<String, Typeface> sTypefaceCache = new LruCache<>(16);
    private static final Runnable sReplaceDispatcherThreadRunnable = new Runnable() { // from class: android.provider.FontsContract.1
        @Override // java.lang.Runnable
        public void run() {
            synchronized (FontsContract.sLock) {
                if (FontsContract.sThread != null) {
                    FontsContract.sThread.quitSafely();
                    HandlerThread unused = FontsContract.sThread = null;
                    Handler unused2 = FontsContract.sHandler = null;
                }
            }
        }
    };
    private static final Comparator<byte[]> sByteArrayComparator = new Comparator() { // from class: android.provider.-$$Lambda$FontsContract$3FDNQd-WsglsyDhif-aHVbzkfrA
        @Override // java.util.Comparator
        public final int compare(Object obj, Object obj2) {
            return FontsContract.lambda$static$13((byte[]) obj, (byte[]) obj2);
        }
    };

    /* loaded from: classes2.dex */
    public static final class Columns implements BaseColumns {
        public static final String FILE_ID = "file_id";
        public static final String ITALIC = "font_italic";
        public static final String RESULT_CODE = "result_code";
        public static final int RESULT_CODE_FONT_NOT_FOUND = 1;
        public static final int RESULT_CODE_FONT_UNAVAILABLE = 2;
        public static final int RESULT_CODE_MALFORMED_QUERY = 3;
        public static final int RESULT_CODE_OK = 0;
        public static final String TTC_INDEX = "font_ttc_index";
        public static final String VARIATION_SETTINGS = "font_variation_settings";
        public static final String WEIGHT = "font_weight";

        private Columns() {
        }
    }

    private FontsContract() {
    }

    public static void setApplicationContextForResources(Context context) {
        sContext = context.getApplicationContext();
    }

    /* loaded from: classes2.dex */
    public static class FontInfo {
        private final FontVariationAxis[] mAxes;
        private final boolean mItalic;
        private final int mResultCode;
        private final int mTtcIndex;
        private final Uri mUri;
        private final int mWeight;

        public FontInfo(Uri uri, int ttcIndex, FontVariationAxis[] axes, int weight, boolean italic, int resultCode) {
            this.mUri = (Uri) Preconditions.checkNotNull(uri);
            this.mTtcIndex = ttcIndex;
            this.mAxes = axes;
            this.mWeight = weight;
            this.mItalic = italic;
            this.mResultCode = resultCode;
        }

        public Uri getUri() {
            return this.mUri;
        }

        public int getTtcIndex() {
            return this.mTtcIndex;
        }

        public FontVariationAxis[] getAxes() {
            return this.mAxes;
        }

        public int getWeight() {
            return this.mWeight;
        }

        public boolean isItalic() {
            return this.mItalic;
        }

        public int getResultCode() {
            return this.mResultCode;
        }
    }

    /* loaded from: classes2.dex */
    public static class FontFamilyResult {
        public static final int STATUS_OK = 0;
        public static final int STATUS_REJECTED = 3;
        public static final int STATUS_UNEXPECTED_DATA_PROVIDED = 2;
        public static final int STATUS_WRONG_CERTIFICATES = 1;
        private final FontInfo[] mFonts;
        private final int mStatusCode;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes2.dex */
        @interface FontResultStatus {
        }

        public FontFamilyResult(int statusCode, FontInfo[] fonts) {
            this.mStatusCode = statusCode;
            this.mFonts = fonts;
        }

        public int getStatusCode() {
            return this.mStatusCode;
        }

        public FontInfo[] getFonts() {
            return this.mFonts;
        }
    }

    public static Typeface getFontSync(final FontRequest request) {
        final String id = request.getIdentifier();
        Typeface cachedTypeface = sTypefaceCache.get(id);
        if (cachedTypeface != null) {
            return cachedTypeface;
        }
        synchronized (sLock) {
            if (sHandler == null) {
                sThread = new HandlerThread("fonts", 10);
                sThread.start();
                sHandler = new Handler(sThread.getLooper());
            }
            final Lock lock = new ReentrantLock();
            final Condition cond = lock.newCondition();
            final AtomicReference<Typeface> holder = new AtomicReference<>();
            final AtomicBoolean waiting = new AtomicBoolean(true);
            final AtomicBoolean timeout = new AtomicBoolean(false);
            sHandler.post(new Runnable() { // from class: android.provider.-$$Lambda$FontsContract$rqfIZKvP1frnI9vP1hVA8jQN_RE
                @Override // java.lang.Runnable
                public final void run() {
                    FontsContract.lambda$getFontSync$0(FontRequest.this, id, holder, lock, timeout, waiting, cond);
                }
            });
            sHandler.removeCallbacks(sReplaceDispatcherThreadRunnable);
            sHandler.postDelayed(sReplaceDispatcherThreadRunnable, JobInfo.MIN_BACKOFF_MILLIS);
            long remaining = TimeUnit.MILLISECONDS.toNanos(500L);
            lock.lock();
            if (!waiting.get()) {
                Typeface typeface = holder.get();
                lock.unlock();
                return typeface;
            }
            do {
                try {
                    remaining = cond.awaitNanos(remaining);
                } catch (InterruptedException e) {
                }
                if (!waiting.get()) {
                    Typeface typeface2 = holder.get();
                    lock.unlock();
                    return typeface2;
                }
            } while (remaining > 0);
            timeout.set(true);
            Log.w(TAG, "Remote font fetch timed out: " + request.getProviderAuthority() + "/" + request.getQuery());
            lock.unlock();
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$getFontSync$0(FontRequest request, String id, AtomicReference holder, Lock lock, AtomicBoolean timeout, AtomicBoolean waiting, Condition cond) {
        try {
            FontFamilyResult result = fetchFonts(sContext, null, request);
            if (result.getStatusCode() == 0) {
                Typeface typeface = buildTypeface(sContext, null, result.getFonts());
                if (typeface != null) {
                    sTypefaceCache.put(id, typeface);
                }
                holder.set(typeface);
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        lock.lock();
        try {
            if (!timeout.get()) {
                waiting.set(false);
                cond.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    /* loaded from: classes2.dex */
    public static class FontRequestCallback {
        public static final int FAIL_REASON_FONT_LOAD_ERROR = -3;
        public static final int FAIL_REASON_FONT_NOT_FOUND = 1;
        public static final int FAIL_REASON_FONT_UNAVAILABLE = 2;
        public static final int FAIL_REASON_MALFORMED_QUERY = 3;
        public static final int FAIL_REASON_PROVIDER_NOT_FOUND = -1;
        public static final int FAIL_REASON_WRONG_CERTIFICATES = -2;

        @Retention(RetentionPolicy.SOURCE)
        /* loaded from: classes2.dex */
        @interface FontRequestFailReason {
        }

        public void onTypefaceRetrieved(Typeface typeface) {
        }

        public void onTypefaceRequestFailed(int reason) {
        }
    }

    public static void requestFonts(final Context context, final FontRequest request, Handler handler, final CancellationSignal cancellationSignal, final FontRequestCallback callback) {
        final Handler callerThreadHandler = new Handler();
        final Typeface cachedTypeface = sTypefaceCache.get(request.getIdentifier());
        if (cachedTypeface != null) {
            callerThreadHandler.post(new Runnable() { // from class: android.provider.-$$Lambda$FontsContract$p_tsXYYYpEH0-EJSp2uPrJ33dkU
                @Override // java.lang.Runnable
                public final void run() {
                    FontsContract.FontRequestCallback.this.onTypefaceRetrieved(cachedTypeface);
                }
            });
        } else {
            handler.post(new Runnable() { // from class: android.provider.-$$Lambda$FontsContract$dFs2m4XF5xdir4W3T-ncUQAVX8k
                @Override // java.lang.Runnable
                public final void run() {
                    FontsContract.lambda$requestFonts$12(Context.this, cancellationSignal, request, callerThreadHandler, callback);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$requestFonts$12(Context context, CancellationSignal cancellationSignal, FontRequest request, Handler callerThreadHandler, final FontRequestCallback callback) {
        try {
            FontFamilyResult result = fetchFonts(context, cancellationSignal, request);
            final Typeface anotherCachedTypeface = sTypefaceCache.get(request.getIdentifier());
            if (anotherCachedTypeface != null) {
                callerThreadHandler.post(new Runnable() { // from class: android.provider.-$$Lambda$FontsContract$xDMhIK5JxjXFDIXBeQbZ_hdXTBc
                    @Override // java.lang.Runnable
                    public final void run() {
                        FontsContract.FontRequestCallback.this.onTypefaceRetrieved(anotherCachedTypeface);
                    }
                });
            } else if (result.getStatusCode() != 0) {
                int statusCode = result.getStatusCode();
                if (statusCode == 1) {
                    callerThreadHandler.post(new Runnable() { // from class: android.provider.-$$Lambda$FontsContract$YhiTIVckhFBdgNR2V1bGY3Q1Nqg
                        @Override // java.lang.Runnable
                        public final void run() {
                            FontsContract.FontRequestCallback.this.onTypefaceRequestFailed(-2);
                        }
                    });
                } else if (statusCode == 2) {
                    callerThreadHandler.post(new Runnable() { // from class: android.provider.-$$Lambda$FontsContract$FCawscMFN_8Qxcb2EdA5gdE-O2k
                        @Override // java.lang.Runnable
                        public final void run() {
                            FontsContract.FontRequestCallback.this.onTypefaceRequestFailed(-3);
                        }
                    });
                } else {
                    callerThreadHandler.post(new Runnable() { // from class: android.provider.-$$Lambda$FontsContract$DV4gvjPxJzdQvcfoIJqGrzFtTQs
                        @Override // java.lang.Runnable
                        public final void run() {
                            FontsContract.FontRequestCallback.this.onTypefaceRequestFailed(-3);
                        }
                    });
                }
            } else {
                FontInfo[] fonts = result.getFonts();
                if (fonts == null || fonts.length == 0) {
                    callerThreadHandler.post(new Runnable() { // from class: android.provider.-$$Lambda$FontsContract$LJ3jfZobcxq5xTMmb88GlM1r9Jk
                        @Override // java.lang.Runnable
                        public final void run() {
                            FontsContract.FontRequestCallback.this.onTypefaceRequestFailed(1);
                        }
                    });
                    return;
                }
                for (FontInfo font : fonts) {
                    if (font.getResultCode() != 0) {
                        final int resultCode = font.getResultCode();
                        if (resultCode < 0) {
                            callerThreadHandler.post(new Runnable() { // from class: android.provider.-$$Lambda$FontsContract$Qvl9aVA7txTF3tFcFbbKD_nWpuM
                                @Override // java.lang.Runnable
                                public final void run() {
                                    FontsContract.FontRequestCallback.this.onTypefaceRequestFailed(-3);
                                }
                            });
                            return;
                        } else {
                            callerThreadHandler.post(new Runnable() { // from class: android.provider.-$$Lambda$FontsContract$rvEOORTXb3mMYTLkoH9nlHQr9Iw
                                @Override // java.lang.Runnable
                                public final void run() {
                                    FontsContract.FontRequestCallback.this.onTypefaceRequestFailed(resultCode);
                                }
                            });
                            return;
                        }
                    }
                }
                final Typeface typeface = buildTypeface(context, cancellationSignal, fonts);
                if (typeface == null) {
                    callerThreadHandler.post(new Runnable() { // from class: android.provider.-$$Lambda$FontsContract$rqmVfWYeZ5NL5MtBx5LOdhNAOP4
                        @Override // java.lang.Runnable
                        public final void run() {
                            FontsContract.FontRequestCallback.this.onTypefaceRequestFailed(-3);
                        }
                    });
                    return;
                }
                sTypefaceCache.put(request.getIdentifier(), typeface);
                callerThreadHandler.post(new Runnable() { // from class: android.provider.-$$Lambda$FontsContract$gJeQYFM3pOm-NcWmWnWDAEk3vlM
                    @Override // java.lang.Runnable
                    public final void run() {
                        FontsContract.FontRequestCallback.this.onTypefaceRetrieved(typeface);
                    }
                });
            }
        } catch (PackageManager.NameNotFoundException e) {
            callerThreadHandler.post(new Runnable() { // from class: android.provider.-$$Lambda$FontsContract$bLFahJqnd9gkPbDqB-OCiChzm_E
                @Override // java.lang.Runnable
                public final void run() {
                    FontsContract.FontRequestCallback.this.onTypefaceRequestFailed(-1);
                }
            });
        }
    }

    public static FontFamilyResult fetchFonts(Context context, CancellationSignal cancellationSignal, FontRequest request) throws PackageManager.NameNotFoundException {
        if (context.isRestricted()) {
            return new FontFamilyResult(3, null);
        }
        ProviderInfo providerInfo = getProvider(context.getPackageManager(), request);
        if (providerInfo == null) {
            return new FontFamilyResult(1, null);
        }
        try {
            FontInfo[] fonts = getFontFromProvider(context, request, providerInfo.authority, cancellationSignal);
            return new FontFamilyResult(0, fonts);
        } catch (IllegalArgumentException e) {
            return new FontFamilyResult(2, null);
        }
    }

    public static Typeface buildTypeface(Context context, CancellationSignal cancellationSignal, FontInfo[] fonts) {
        if (context.isRestricted()) {
            return null;
        }
        Map<Uri, ByteBuffer> uriBuffer = prepareFontData(context, fonts, cancellationSignal);
        if (uriBuffer.isEmpty()) {
            return null;
        }
        FontFamily.Builder familyBuilder = null;
        for (FontInfo fontInfo : fonts) {
            ByteBuffer buffer = uriBuffer.get(fontInfo.getUri());
            if (buffer != null) {
                try {
                    Font font = new Font.Builder(buffer).setWeight(fontInfo.getWeight()).setSlant(fontInfo.isItalic() ? 1 : 0).setTtcIndex(fontInfo.getTtcIndex()).setFontVariationSettings(fontInfo.getAxes()).build();
                    if (familyBuilder == null) {
                        familyBuilder = new FontFamily.Builder(font);
                    } else {
                        familyBuilder.addFont(font);
                    }
                } catch (IOException e) {
                } catch (IllegalArgumentException e2) {
                    return null;
                }
            }
        }
        if (familyBuilder == null) {
            return null;
        }
        FontFamily family = familyBuilder.build();
        FontStyle normal = new FontStyle(400, 0);
        Font bestFont = family.getFont(0);
        int bestScore = normal.getMatchScore(bestFont.getStyle());
        for (int i = 1; i < family.getSize(); i++) {
            Font candidate = family.getFont(i);
            int score = normal.getMatchScore(candidate.getStyle());
            if (score < bestScore) {
                bestFont = candidate;
                bestScore = score;
            }
        }
        return new Typeface.CustomFallbackBuilder(family).setStyle(bestFont.getStyle()).build();
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:44:0x0078
        	at jadx.core.dex.visitors.blocks.BlockProcessor.checkForUnreachableBlocks(BlockProcessor.java:81)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:47)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:39)
        */
    private static java.util.Map<android.net.Uri, java.nio.ByteBuffer> prepareFontData(android.content.Context r19, android.provider.FontsContract.FontInfo[] r20, android.os.CancellationSignal r21) {
        /*
            r1 = r20
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            r2 = r0
            android.content.ContentResolver r3 = r19.getContentResolver()
            int r4 = r1.length
            r0 = 0
            r5 = r0
        Lf:
            if (r5 >= r4) goto L81
            r6 = r1[r5]
            int r0 = r6.getResultCode()
            if (r0 == 0) goto L1d
            r9 = r21
            goto L7e
        L1d:
            android.net.Uri r7 = r6.getUri()
            boolean r0 = r2.containsKey(r7)
            if (r0 == 0) goto L2a
            r9 = r21
            goto L7e
        L2a:
            r8 = 0
            java.lang.String r0 = "r"
            r9 = r21
            android.os.ParcelFileDescriptor r0 = r3.openFileDescriptor(r7, r0, r9)     // Catch: java.io.IOException -> L76
            r10 = r0
            r11 = 0
            if (r10 == 0) goto L70
            java.io.FileInputStream r0 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L62 java.io.IOException -> L6f
            java.io.FileDescriptor r12 = r10.getFileDescriptor()     // Catch: java.lang.Throwable -> L62 java.io.IOException -> L6f
            r0.<init>(r12)     // Catch: java.lang.Throwable -> L62 java.io.IOException -> L6f
            r12 = r0
            java.nio.channels.FileChannel r13 = r12.getChannel()     // Catch: java.lang.Throwable -> L59
            long r17 = r13.size()     // Catch: java.lang.Throwable -> L59
            java.nio.channels.FileChannel$MapMode r14 = java.nio.channels.FileChannel.MapMode.READ_ONLY     // Catch: java.lang.Throwable -> L59
            r15 = 0
            java.nio.MappedByteBuffer r0 = r13.map(r14, r15, r17)     // Catch: java.lang.Throwable -> L59
            r8 = r0
            $closeResource(r11, r12)     // Catch: java.lang.Throwable -> L62 java.io.IOException -> L6f
            goto L70
        L59:
            r0 = move-exception
            r13 = r0
            throw r13     // Catch: java.lang.Throwable -> L5c
        L5c:
            r0 = move-exception
            r14 = r0
            $closeResource(r13, r12)     // Catch: java.lang.Throwable -> L62 java.io.IOException -> L6f
            throw r14     // Catch: java.lang.Throwable -> L62 java.io.IOException -> L6f
        L62:
            r0 = move-exception
            r11 = r8
            r8 = r0
            throw r8     // Catch: java.lang.Throwable -> L66
        L66:
            r0 = move-exception
            r12 = r0
            $closeResource(r8, r10)     // Catch: java.io.IOException -> L6c
            throw r12     // Catch: java.io.IOException -> L6c
        L6c:
            r0 = move-exception
            r8 = r11
            goto L7b
        L6f:
            r0 = move-exception
        L70:
            if (r10 == 0) goto L75
            $closeResource(r11, r10)     // Catch: java.io.IOException -> L76
        L75:
            goto L7b
        L76:
            r0 = move-exception
            goto L7b
        L78:
            r0 = move-exception
            r9 = r21
        L7b:
            r2.put(r7, r8)
        L7e:
            int r5 = r5 + 1
            goto Lf
        L81:
            r9 = r21
            java.util.Map r0 = java.util.Collections.unmodifiableMap(r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.provider.FontsContract.prepareFontData(android.content.Context, android.provider.FontsContract$FontInfo[], android.os.CancellationSignal):java.util.Map");
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 == null) {
            x1.close();
            return;
        }
        try {
            x1.close();
        } catch (Throwable th) {
            x0.addSuppressed(th);
        }
    }

    @VisibleForTesting
    public static ProviderInfo getProvider(PackageManager packageManager, FontRequest request) throws PackageManager.NameNotFoundException {
        String providerAuthority = request.getProviderAuthority();
        ProviderInfo info = packageManager.resolveContentProvider(providerAuthority, 0);
        if (info == null) {
            throw new PackageManager.NameNotFoundException("No package found for authority: " + providerAuthority);
        } else if (!info.packageName.equals(request.getProviderPackage())) {
            throw new PackageManager.NameNotFoundException("Found content provider " + providerAuthority + ", but package was not " + request.getProviderPackage());
        } else if (info.applicationInfo.isSystemApp()) {
            return info;
        } else {
            PackageInfo packageInfo = packageManager.getPackageInfo(info.packageName, 64);
            List<byte[]> signatures = convertToByteArrayList(packageInfo.signatures);
            Collections.sort(signatures, sByteArrayComparator);
            List<List<byte[]>> requestCertificatesList = request.getCertificates();
            for (int i = 0; i < requestCertificatesList.size(); i++) {
                List<byte[]> requestSignatures = new ArrayList<>(requestCertificatesList.get(i));
                Collections.sort(requestSignatures, sByteArrayComparator);
                if (equalsByteArrayList(signatures, requestSignatures)) {
                    return info;
                }
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$static$13(byte[] l, byte[] r) {
        if (l.length != r.length) {
            return l.length - r.length;
        }
        for (int i = 0; i < l.length; i++) {
            if (l[i] != r[i]) {
                return l[i] - r[i];
            }
        }
        return 0;
    }

    private static boolean equalsByteArrayList(List<byte[]> signatures, List<byte[]> requestSignatures) {
        if (signatures.size() != requestSignatures.size()) {
            return false;
        }
        for (int i = 0; i < signatures.size(); i++) {
            if (!Arrays.equals(signatures.get(i), requestSignatures.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static List<byte[]> convertToByteArrayList(Signature[] signatures) {
        List<byte[]> shas = new ArrayList<>();
        for (Signature signature : signatures) {
            shas.add(signature.toByteArray());
        }
        return shas;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @VisibleForTesting
    public static FontInfo[] getFontFromProvider(Context context, FontRequest request, String authority, CancellationSignal cancellationSignal) {
        int resultCodeColumnIndex;
        Uri fileUri;
        int weight;
        int i;
        ArrayList<FontInfo> result = new ArrayList<>();
        Uri uri = new Uri.Builder().scheme("content").authority(authority).build();
        Uri fileBaseUri = new Uri.Builder().scheme("content").authority(authority).appendPath(ContentResolver.SCHEME_FILE).build();
        int i2 = 1;
        Cursor cursor = context.getContentResolver().query(uri, new String[]{"_id", Columns.FILE_ID, Columns.TTC_INDEX, Columns.VARIATION_SETTINGS, Columns.WEIGHT, Columns.ITALIC, Columns.RESULT_CODE}, "query = ?", new String[]{request.getQuery()}, null, cancellationSignal);
        if (cursor != null) {
            try {
                if (cursor.getCount() > 0) {
                    int weight2 = cursor.getColumnIndex(Columns.RESULT_CODE);
                    result = new ArrayList<>();
                    int idColumnIndex = cursor.getColumnIndexOrThrow("_id");
                    int fileIdColumnIndex = cursor.getColumnIndex(Columns.FILE_ID);
                    int ttcIndexColumnIndex = cursor.getColumnIndex(Columns.TTC_INDEX);
                    int vsColumnIndex = cursor.getColumnIndex(Columns.VARIATION_SETTINGS);
                    int weightColumnIndex = cursor.getColumnIndex(Columns.WEIGHT);
                    int italicColumnIndex = cursor.getColumnIndex(Columns.ITALIC);
                    while (cursor.moveToNext()) {
                        int resultCode = weight2 != -1 ? cursor.getInt(weight2) : 0;
                        int ttcIndex = ttcIndexColumnIndex != -1 ? cursor.getInt(ttcIndexColumnIndex) : 0;
                        String variationSettings = vsColumnIndex != -1 ? cursor.getString(vsColumnIndex) : null;
                        if (fileIdColumnIndex == -1) {
                            long id = cursor.getLong(idColumnIndex);
                            resultCodeColumnIndex = weight2;
                            fileUri = ContentUris.withAppendedId(uri, id);
                        } else {
                            resultCodeColumnIndex = weight2;
                            long id2 = cursor.getLong(fileIdColumnIndex);
                            fileUri = ContentUris.withAppendedId(fileBaseUri, id2);
                        }
                        if (weightColumnIndex != -1 && italicColumnIndex != -1) {
                            weight = cursor.getInt(weightColumnIndex);
                            i = cursor.getInt(italicColumnIndex) == i2 ? i2 : 0;
                        } else {
                            weight = 400;
                            i = 0;
                        }
                        FontVariationAxis[] axes = FontVariationAxis.fromFontVariationSettings(variationSettings);
                        result.add(new FontInfo(fileUri, ttcIndex, axes, weight, i, resultCode));
                        weight2 = resultCodeColumnIndex;
                        i2 = 1;
                    }
                }
            } finally {
            }
        }
        if (cursor != null) {
            $closeResource(null, cursor);
        }
        return (FontInfo[]) result.toArray(new FontInfo[0]);
    }
}
