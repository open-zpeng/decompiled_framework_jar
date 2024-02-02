package android.view.textclassifier;

import android.metrics.LogMaker;
import android.util.ArrayMap;
import android.view.textclassifier.TextLinks;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.Preconditions;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
@VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
/* loaded from: classes2.dex */
public final class GenerateLinksLogger {
    private static final boolean DEBUG_LOG_ENABLED = false;
    private static final String LOG_TAG = "GenerateLinksLogger";
    private static final String ZERO = "0";
    private final MetricsLogger mMetricsLogger;
    private final Random mRng;
    private final int mSampleRate;

    public synchronized GenerateLinksLogger(int sampleRate) {
        this.mSampleRate = sampleRate;
        this.mRng = new Random(System.nanoTime());
        this.mMetricsLogger = new MetricsLogger();
    }

    @VisibleForTesting
    public synchronized GenerateLinksLogger(int sampleRate, MetricsLogger metricsLogger) {
        this.mSampleRate = sampleRate;
        this.mRng = new Random(System.nanoTime());
        this.mMetricsLogger = metricsLogger;
    }

    public synchronized void logGenerateLinks(CharSequence text, TextLinks links, String callingPackageName, long latencyMs) {
        String entityType;
        Preconditions.checkNotNull(text);
        Preconditions.checkNotNull(links);
        Preconditions.checkNotNull(callingPackageName);
        if (!shouldLog()) {
            return;
        }
        LinkifyStats totalStats = new LinkifyStats();
        Map<String, LinkifyStats> perEntityTypeStats = new ArrayMap<>();
        for (TextLinks.TextLink link : links.getLinks()) {
            if (link.getEntityCount() != 0 && (entityType = link.getEntity(0)) != null && !"other".equals(entityType) && !"".equals(entityType)) {
                totalStats.countLink(link);
                perEntityTypeStats.computeIfAbsent(entityType, new Function() { // from class: android.view.textclassifier.-$$Lambda$GenerateLinksLogger$vmbT_h7MLlbrIm0lJJwA-eHQhXk
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return GenerateLinksLogger.lambda$logGenerateLinks$0((String) obj);
                    }
                }).countLink(link);
            }
        }
        String callId = UUID.randomUUID().toString();
        writeStats(callId, callingPackageName, null, totalStats, text, latencyMs);
        for (Map.Entry<String, LinkifyStats> entry : perEntityTypeStats.entrySet()) {
            writeStats(callId, callingPackageName, entry.getKey(), entry.getValue(), text, latencyMs);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ LinkifyStats lambda$logGenerateLinks$0(String k) {
        return new LinkifyStats();
    }

    private synchronized boolean shouldLog() {
        return this.mSampleRate <= 1 || this.mRng.nextInt(this.mSampleRate) == 0;
    }

    private synchronized void writeStats(String callId, String callingPackageName, String entityType, LinkifyStats stats, CharSequence text, long latencyMs) {
        LogMaker log = new LogMaker(1313).setPackageName(callingPackageName).addTaggedData(1319, callId).addTaggedData(1316, Integer.valueOf(stats.mNumLinks)).addTaggedData(1317, Integer.valueOf(stats.mNumLinksTextLength)).addTaggedData(1315, Integer.valueOf(text.length())).addTaggedData(1314, Long.valueOf(latencyMs));
        if (entityType != null) {
            log.addTaggedData(1318, entityType);
        }
        this.mMetricsLogger.write(log);
        debugLog(log);
    }

    private static synchronized void debugLog(LogMaker log) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class LinkifyStats {
        int mNumLinks;
        int mNumLinksTextLength;

        private synchronized LinkifyStats() {
        }

        synchronized void countLink(TextLinks.TextLink link) {
            this.mNumLinks++;
            this.mNumLinksTextLength += link.getEnd() - link.getStart();
        }
    }
}
