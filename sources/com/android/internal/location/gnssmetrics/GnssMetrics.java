package com.android.internal.location.gnssmetrics;

import android.location.GnssStatus;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.connectivity.GpsBatteryStats;
import android.util.Base64;
import android.util.Log;
import android.util.StatsLog;
import android.util.TimeUtils;
import com.android.internal.app.IBatteryStats;
import com.android.internal.location.nano.GnssLogsProto;
import com.xiaopeng.util.FeatureOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/* loaded from: classes3.dex */
public class GnssMetrics {
    private static final int DEFAULT_TIME_BETWEEN_FIXES_MILLISECS = 1000;
    public static final int GPS_SIGNAL_QUALITY_GOOD = 1;
    public static final int GPS_SIGNAL_QUALITY_POOR = 0;
    public static final int GPS_SIGNAL_QUALITY_UNKNOWN = -1;
    private static final double HZ_PER_MHZ = 1000000.0d;
    private static final double L5_CARRIER_FREQ_RANGE_HIGH_HZ = 1.189E9d;
    private static final double L5_CARRIER_FREQ_RANGE_LOW_HZ = 1.164E9d;
    public static final int NUM_GPS_SIGNAL_QUALITY_LEVELS = 2;
    private static final String TAG = GnssMetrics.class.getSimpleName();
    private String logStartInElapsedRealTime;
    private boolean[] mConstellationTypes;
    private GnssPowerMetrics mGnssPowerMetrics;
    private Statistics locationFailureStatistics = new Statistics();
    private Statistics timeToFirstFixSecStatistics = new Statistics();
    private Statistics positionAccuracyMeterStatistics = new Statistics();
    private Statistics topFourAverageCn0Statistics = new Statistics();
    private Statistics mTopFourAverageCn0StatisticsL5 = new Statistics();
    private int mNumSvStatus = 0;
    private int mNumL5SvStatus = 0;
    private int mNumSvStatusUsedInFix = 0;
    private int mNumL5SvStatusUsedInFix = 0;

    public GnssMetrics(IBatteryStats stats) {
        this.mGnssPowerMetrics = new GnssPowerMetrics(stats);
        reset();
    }

    public void logReceivedLocationStatus(boolean isSuccessful) {
        if (!isSuccessful) {
            this.locationFailureStatistics.addItem(1.0d);
        } else {
            this.locationFailureStatistics.addItem(FeatureOption.FO_BOOT_POLICY_CPU);
        }
    }

    public void logMissedReports(int desiredTimeBetweenFixesMilliSeconds, int actualTimeBetweenFixesMilliSeconds) {
        int numReportMissed = (actualTimeBetweenFixesMilliSeconds / Math.max(1000, desiredTimeBetweenFixesMilliSeconds)) - 1;
        if (numReportMissed > 0) {
            for (int i = 0; i < numReportMissed; i++) {
                this.locationFailureStatistics.addItem(1.0d);
            }
        }
    }

    public void logTimeToFirstFixMilliSecs(int timeToFirstFixMilliSeconds) {
        this.timeToFirstFixSecStatistics.addItem(timeToFirstFixMilliSeconds / 1000);
    }

    public void logPositionAccuracyMeters(float positionAccuracyMeters) {
        this.positionAccuracyMeterStatistics.addItem(positionAccuracyMeters);
    }

    public void logCn0(float[] cn0s, int numSv, float[] svCarrierFreqs) {
        logCn0L5(numSv, cn0s, svCarrierFreqs);
        if (numSv == 0 || cn0s == null || cn0s.length == 0 || cn0s.length < numSv) {
            if (numSv == 0) {
                this.mGnssPowerMetrics.reportSignalQuality(null, 0);
                return;
            }
            return;
        }
        float[] cn0Array = Arrays.copyOf(cn0s, numSv);
        Arrays.sort(cn0Array);
        this.mGnssPowerMetrics.reportSignalQuality(cn0Array, numSv);
        if (numSv >= 4 && cn0Array[numSv - 4] > FeatureOption.FO_BOOT_POLICY_CPU) {
            double top4AvgCn0 = FeatureOption.FO_BOOT_POLICY_CPU;
            for (int i = numSv - 4; i < numSv; i++) {
                top4AvgCn0 += cn0Array[i];
            }
            this.topFourAverageCn0Statistics.addItem(top4AvgCn0 / 4.0d);
        }
    }

    private static boolean isL5Sv(float carrierFreq) {
        return ((double) carrierFreq) >= L5_CARRIER_FREQ_RANGE_LOW_HZ && ((double) carrierFreq) <= L5_CARRIER_FREQ_RANGE_HIGH_HZ;
    }

    public void logSvStatus(int svCount, int[] svidWithFlags, float[] svCarrierFreqs) {
        for (int i = 0; i < svCount; i++) {
            if ((svidWithFlags[i] & 8) != 0) {
                this.mNumSvStatus++;
                boolean isL5 = isL5Sv(svCarrierFreqs[i]);
                if (isL5) {
                    this.mNumL5SvStatus++;
                }
                if ((svidWithFlags[i] & 4) != 0) {
                    this.mNumSvStatusUsedInFix++;
                    if (isL5) {
                        this.mNumL5SvStatusUsedInFix++;
                    }
                }
            }
        }
    }

    private void logCn0L5(int svCount, float[] cn0s, float[] svCarrierFreqs) {
        if (svCount == 0 || cn0s == null || cn0s.length == 0 || cn0s.length < svCount || svCarrierFreqs == null || svCarrierFreqs.length == 0 || svCarrierFreqs.length < svCount) {
            return;
        }
        ArrayList<Float> CnoL5Array = new ArrayList<>();
        for (int i = 0; i < svCount; i++) {
            if (isL5Sv(svCarrierFreqs[i])) {
                CnoL5Array.add(Float.valueOf(cn0s[i]));
            }
        }
        int i2 = CnoL5Array.size();
        if (i2 == 0 || CnoL5Array.size() < 4) {
            return;
        }
        int numSvL5 = CnoL5Array.size();
        Collections.sort(CnoL5Array);
        if (CnoL5Array.get(numSvL5 - 4).floatValue() > FeatureOption.FO_BOOT_POLICY_CPU) {
            double top4AvgCn0 = FeatureOption.FO_BOOT_POLICY_CPU;
            for (int i3 = numSvL5 - 4; i3 < numSvL5; i3++) {
                top4AvgCn0 += CnoL5Array.get(i3).floatValue();
            }
            this.mTopFourAverageCn0StatisticsL5.addItem(top4AvgCn0 / 4.0d);
        }
    }

    public void logConstellationType(int constellationType) {
        boolean[] zArr = this.mConstellationTypes;
        if (constellationType >= zArr.length) {
            String str = TAG;
            Log.e(str, "Constellation type " + constellationType + " is not valid.");
            return;
        }
        zArr[constellationType] = true;
    }

    public String dumpGnssMetricsAsProtoString() {
        GnssLogsProto.GnssLog msg = new GnssLogsProto.GnssLog();
        if (this.locationFailureStatistics.getCount() > 0) {
            msg.numLocationReportProcessed = this.locationFailureStatistics.getCount();
            msg.percentageLocationFailure = (int) (this.locationFailureStatistics.getMean() * 100.0d);
        }
        if (this.timeToFirstFixSecStatistics.getCount() > 0) {
            msg.numTimeToFirstFixProcessed = this.timeToFirstFixSecStatistics.getCount();
            msg.meanTimeToFirstFixSecs = (int) this.timeToFirstFixSecStatistics.getMean();
            msg.standardDeviationTimeToFirstFixSecs = (int) this.timeToFirstFixSecStatistics.getStandardDeviation();
        }
        if (this.positionAccuracyMeterStatistics.getCount() > 0) {
            msg.numPositionAccuracyProcessed = this.positionAccuracyMeterStatistics.getCount();
            msg.meanPositionAccuracyMeters = (int) this.positionAccuracyMeterStatistics.getMean();
            msg.standardDeviationPositionAccuracyMeters = (int) this.positionAccuracyMeterStatistics.getStandardDeviation();
        }
        if (this.topFourAverageCn0Statistics.getCount() > 0) {
            msg.numTopFourAverageCn0Processed = this.topFourAverageCn0Statistics.getCount();
            msg.meanTopFourAverageCn0DbHz = this.topFourAverageCn0Statistics.getMean();
            msg.standardDeviationTopFourAverageCn0DbHz = this.topFourAverageCn0Statistics.getStandardDeviation();
        }
        int i = this.mNumSvStatus;
        if (i > 0) {
            msg.numSvStatusProcessed = i;
        }
        int i2 = this.mNumL5SvStatus;
        if (i2 > 0) {
            msg.numL5SvStatusProcessed = i2;
        }
        int i3 = this.mNumSvStatusUsedInFix;
        if (i3 > 0) {
            msg.numSvStatusUsedInFix = i3;
        }
        int i4 = this.mNumL5SvStatusUsedInFix;
        if (i4 > 0) {
            msg.numL5SvStatusUsedInFix = i4;
        }
        if (this.mTopFourAverageCn0StatisticsL5.getCount() > 0) {
            msg.numL5TopFourAverageCn0Processed = this.mTopFourAverageCn0StatisticsL5.getCount();
            msg.meanL5TopFourAverageCn0DbHz = this.mTopFourAverageCn0StatisticsL5.getMean();
            msg.standardDeviationL5TopFourAverageCn0DbHz = this.mTopFourAverageCn0StatisticsL5.getStandardDeviation();
        }
        msg.powerMetrics = this.mGnssPowerMetrics.buildProto();
        msg.hardwareRevision = SystemProperties.get("ro.boot.revision", "");
        String s = Base64.encodeToString(GnssLogsProto.GnssLog.toByteArray(msg), 0);
        reset();
        return s;
    }

    public String dumpGnssMetricsAsText() {
        StringBuilder s = new StringBuilder();
        s.append("GNSS_KPI_START");
        s.append('\n');
        s.append("  KPI logging start time: ");
        s.append(this.logStartInElapsedRealTime);
        s.append("\n");
        s.append("  KPI logging end time: ");
        TimeUtils.formatDuration(SystemClock.elapsedRealtimeNanos() / TimeUtils.NANOS_PER_MS, s);
        s.append("\n");
        s.append("  Number of location reports: ");
        s.append(this.locationFailureStatistics.getCount());
        s.append("\n");
        if (this.locationFailureStatistics.getCount() > 0) {
            s.append("  Percentage location failure: ");
            s.append(this.locationFailureStatistics.getMean() * 100.0d);
            s.append("\n");
        }
        s.append("  Number of TTFF reports: ");
        s.append(this.timeToFirstFixSecStatistics.getCount());
        s.append("\n");
        if (this.timeToFirstFixSecStatistics.getCount() > 0) {
            s.append("  TTFF mean (sec): ");
            s.append(this.timeToFirstFixSecStatistics.getMean());
            s.append("\n");
            s.append("  TTFF standard deviation (sec): ");
            s.append(this.timeToFirstFixSecStatistics.getStandardDeviation());
            s.append("\n");
        }
        s.append("  Number of position accuracy reports: ");
        s.append(this.positionAccuracyMeterStatistics.getCount());
        s.append("\n");
        if (this.positionAccuracyMeterStatistics.getCount() > 0) {
            s.append("  Position accuracy mean (m): ");
            s.append(this.positionAccuracyMeterStatistics.getMean());
            s.append("\n");
            s.append("  Position accuracy standard deviation (m): ");
            s.append(this.positionAccuracyMeterStatistics.getStandardDeviation());
            s.append("\n");
        }
        s.append("  Number of CN0 reports: ");
        s.append(this.topFourAverageCn0Statistics.getCount());
        s.append("\n");
        if (this.topFourAverageCn0Statistics.getCount() > 0) {
            s.append("  Top 4 Avg CN0 mean (dB-Hz): ");
            s.append(this.topFourAverageCn0Statistics.getMean());
            s.append("\n");
            s.append("  Top 4 Avg CN0 standard deviation (dB-Hz): ");
            s.append(this.topFourAverageCn0Statistics.getStandardDeviation());
            s.append("\n");
        }
        s.append("  Total number of sv status messages processed: ");
        s.append(this.mNumSvStatus);
        s.append("\n");
        s.append("  Total number of L5 sv status messages processed: ");
        s.append(this.mNumL5SvStatus);
        s.append("\n");
        s.append("  Total number of sv status messages processed, where sv is used in fix: ");
        s.append(this.mNumSvStatusUsedInFix);
        s.append("\n");
        s.append("  Total number of L5 sv status messages processed, where sv is used in fix: ");
        s.append(this.mNumL5SvStatusUsedInFix);
        s.append("\n");
        s.append("  Number of L5 CN0 reports: ");
        s.append(this.mTopFourAverageCn0StatisticsL5.getCount());
        s.append("\n");
        if (this.mTopFourAverageCn0StatisticsL5.getCount() > 0) {
            s.append("  L5 Top 4 Avg CN0 mean (dB-Hz): ");
            s.append(this.mTopFourAverageCn0StatisticsL5.getMean());
            s.append("\n");
            s.append("  L5 Top 4 Avg CN0 standard deviation (dB-Hz): ");
            s.append(this.mTopFourAverageCn0StatisticsL5.getStandardDeviation());
            s.append("\n");
        }
        s.append("  Used-in-fix constellation types: ");
        int i = 0;
        while (true) {
            boolean[] zArr = this.mConstellationTypes;
            if (i >= zArr.length) {
                break;
            }
            if (zArr[i]) {
                s.append(GnssStatus.constellationTypeToString(i));
                s.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            }
            i++;
        }
        s.append("\n");
        s.append("GNSS_KPI_END");
        s.append("\n");
        GpsBatteryStats stats = this.mGnssPowerMetrics.getGpsBatteryStats();
        if (stats != null) {
            s.append("Power Metrics");
            s.append("\n");
            s.append("  Time on battery (min): " + (stats.getLoggingDurationMs() / 60000.0d));
            s.append("\n");
            long[] t = stats.getTimeInGpsSignalQualityLevel();
            if (t != null && t.length == 2) {
                s.append("  Amount of time (while on battery) Top 4 Avg CN0 > " + Double.toString(20.0d) + " dB-Hz (min): ");
                s.append(((double) t[1]) / 60000.0d);
                s.append("\n");
                s.append("  Amount of time (while on battery) Top 4 Avg CN0 <= " + Double.toString(20.0d) + " dB-Hz (min): ");
                s.append(((double) t[0]) / 60000.0d);
                s.append("\n");
            }
            s.append("  Energy consumed while on battery (mAh): ");
            s.append(stats.getEnergyConsumedMaMs() / 3600000.0d);
            s.append("\n");
        }
        s.append("Hardware Version: " + SystemProperties.get("ro.boot.revision", ""));
        s.append("\n");
        return s.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class Statistics {
        private int count;
        private double sum;
        private double sumSquare;

        private Statistics() {
        }

        public void reset() {
            this.count = 0;
            this.sum = FeatureOption.FO_BOOT_POLICY_CPU;
            this.sumSquare = FeatureOption.FO_BOOT_POLICY_CPU;
        }

        public void addItem(double item) {
            this.count++;
            this.sum += item;
            this.sumSquare += item * item;
        }

        public int getCount() {
            return this.count;
        }

        public double getMean() {
            return this.sum / this.count;
        }

        public double getStandardDeviation() {
            double d = this.sum;
            int i = this.count;
            double m = d / i;
            double m2 = m * m;
            double v = this.sumSquare / i;
            if (v > m2) {
                return Math.sqrt(v - m2);
            }
            return FeatureOption.FO_BOOT_POLICY_CPU;
        }
    }

    private void reset() {
        StringBuilder s = new StringBuilder();
        TimeUtils.formatDuration(SystemClock.elapsedRealtimeNanos() / TimeUtils.NANOS_PER_MS, s);
        this.logStartInElapsedRealTime = s.toString();
        this.locationFailureStatistics.reset();
        this.timeToFirstFixSecStatistics.reset();
        this.positionAccuracyMeterStatistics.reset();
        this.topFourAverageCn0Statistics.reset();
        this.mTopFourAverageCn0StatisticsL5.reset();
        this.mNumSvStatus = 0;
        this.mNumL5SvStatus = 0;
        this.mNumSvStatusUsedInFix = 0;
        this.mNumL5SvStatusUsedInFix = 0;
        resetConstellationTypes();
    }

    public void resetConstellationTypes() {
        this.mConstellationTypes = new boolean[8];
    }

    /* loaded from: classes3.dex */
    private class GnssPowerMetrics {
        public static final double POOR_TOP_FOUR_AVG_CN0_THRESHOLD_DB_HZ = 20.0d;
        private static final double REPORTING_THRESHOLD_DB_HZ = 1.0d;
        private final IBatteryStats mBatteryStats;
        private double mLastAverageCn0 = -100.0d;
        private int mLastSignalLevel = -1;

        public GnssPowerMetrics(IBatteryStats stats) {
            this.mBatteryStats = stats;
        }

        public GnssLogsProto.PowerMetrics buildProto() {
            GnssLogsProto.PowerMetrics p = new GnssLogsProto.PowerMetrics();
            GpsBatteryStats stats = GnssMetrics.this.mGnssPowerMetrics.getGpsBatteryStats();
            if (stats != null) {
                p.loggingDurationMs = stats.getLoggingDurationMs();
                p.energyConsumedMah = stats.getEnergyConsumedMaMs() / 3600000.0d;
                long[] t = stats.getTimeInGpsSignalQualityLevel();
                p.timeInSignalQualityLevelMs = new long[t.length];
                for (int i = 0; i < t.length; i++) {
                    p.timeInSignalQualityLevelMs[i] = t[i];
                }
            }
            return p;
        }

        public GpsBatteryStats getGpsBatteryStats() {
            try {
                return this.mBatteryStats.getGpsBatteryStats();
            } catch (Exception e) {
                Log.w(GnssMetrics.TAG, "Exception", e);
                return null;
            }
        }

        public void reportSignalQuality(float[] ascendingCN0Array, int numSv) {
            double avgCn0 = FeatureOption.FO_BOOT_POLICY_CPU;
            if (numSv > 0) {
                for (int i = Math.max(0, numSv - 4); i < numSv; i++) {
                    avgCn0 += ascendingCN0Array[i];
                }
                avgCn0 /= Math.min(numSv, 4);
            }
            if (Math.abs(avgCn0 - this.mLastAverageCn0) < REPORTING_THRESHOLD_DB_HZ) {
                return;
            }
            int signalLevel = getSignalLevel(avgCn0);
            if (signalLevel != this.mLastSignalLevel) {
                StatsLog.write(69, signalLevel);
                this.mLastSignalLevel = signalLevel;
            }
            try {
                this.mBatteryStats.noteGpsSignalQuality(signalLevel);
                this.mLastAverageCn0 = avgCn0;
            } catch (Exception e) {
                Log.w(GnssMetrics.TAG, "Exception", e);
            }
        }

        private int getSignalLevel(double cn0) {
            if (cn0 > 20.0d) {
                return 1;
            }
            return 0;
        }
    }
}
