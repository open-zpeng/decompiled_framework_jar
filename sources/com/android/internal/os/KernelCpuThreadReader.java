package com.android.internal.os;

import android.os.Process;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.Preconditions;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

/* loaded from: classes3.dex */
public class KernelCpuThreadReader {
    private static final String CPU_STATISTICS_FILENAME = "time_in_state";
    private static final boolean DEBUG = false;
    private static final String DEFAULT_PROCESS_NAME = "unknown_process";
    private static final String DEFAULT_THREAD_NAME = "unknown_thread";
    private static final int ID_ERROR = -1;
    private static final String PROCESS_DIRECTORY_FILTER = "[0-9]*";
    private static final String PROCESS_NAME_FILENAME = "cmdline";
    private static final String TAG = "KernelCpuThreadReader";
    private static final String THREAD_NAME_FILENAME = "comm";
    private int[] mFrequenciesKhz;
    private FrequencyBucketCreator mFrequencyBucketCreator;
    private final Injector mInjector;
    private final Path mProcPath;
    private final ProcTimeInStateReader mProcTimeInStateReader;
    private Predicate<Integer> mUidPredicate;
    private static final Path DEFAULT_PROC_PATH = Paths.get("/proc", new String[0]);
    private static final Path DEFAULT_INITIAL_TIME_IN_STATE_PATH = DEFAULT_PROC_PATH.resolve("self/time_in_state");

    @VisibleForTesting
    public KernelCpuThreadReader(int numBuckets, Predicate<Integer> uidPredicate, Path procPath, Path initialTimeInStatePath, Injector injector) throws IOException {
        this.mUidPredicate = uidPredicate;
        this.mProcPath = procPath;
        this.mProcTimeInStateReader = new ProcTimeInStateReader(initialTimeInStatePath);
        this.mInjector = injector;
        setNumBuckets(numBuckets);
    }

    public static KernelCpuThreadReader create(int numBuckets, Predicate<Integer> uidPredicate) {
        try {
            return new KernelCpuThreadReader(numBuckets, uidPredicate, DEFAULT_PROC_PATH, DEFAULT_INITIAL_TIME_IN_STATE_PATH, new Injector());
        } catch (IOException e) {
            Slog.e(TAG, "Failed to initialize KernelCpuThreadReader", e);
            return null;
        }
    }

    public ArrayList<ProcessCpuUsage> getProcessCpuUsage() {
        ProcessCpuUsage processCpuUsage;
        ArrayList<ProcessCpuUsage> processCpuUsages = new ArrayList<>();
        try {
            DirectoryStream<Path> processPaths = Files.newDirectoryStream(this.mProcPath, PROCESS_DIRECTORY_FILTER);
            for (Path processPath : processPaths) {
                int processId = getProcessId(processPath);
                int uid = this.mInjector.getUidForPid(processId);
                if (uid != -1 && processId != -1 && this.mUidPredicate.test(Integer.valueOf(uid)) && (processCpuUsage = getProcessCpuUsage(processPath, processId, uid)) != null) {
                    processCpuUsages.add(processCpuUsage);
                }
            }
            $closeResource(null, processPaths);
            if (processCpuUsages.isEmpty()) {
                Slog.w(TAG, "Didn't successfully get any process CPU information for UIDs specified");
                return null;
            }
            return processCpuUsages;
        } catch (IOException e) {
            Slog.w(TAG, "Failed to iterate over process paths", e);
            return null;
        }
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

    public int[] getCpuFrequenciesKhz() {
        return this.mFrequenciesKhz;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setNumBuckets(int numBuckets) {
        if (numBuckets < 1) {
            Slog.w(TAG, "Number of buckets must be at least 1, but was " + numBuckets);
            return;
        }
        int[] iArr = this.mFrequenciesKhz;
        if (iArr != null && iArr.length == numBuckets) {
            return;
        }
        this.mFrequencyBucketCreator = new FrequencyBucketCreator(this.mProcTimeInStateReader.getFrequenciesKhz(), numBuckets);
        this.mFrequenciesKhz = this.mFrequencyBucketCreator.bucketFrequencies(this.mProcTimeInStateReader.getFrequenciesKhz());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setUidPredicate(Predicate<Integer> uidPredicate) {
        this.mUidPredicate = uidPredicate;
    }

    private ProcessCpuUsage getProcessCpuUsage(Path processPath, int processId, int uid) {
        Path allThreadsPath = processPath.resolve("task");
        ArrayList<ThreadCpuUsage> threadCpuUsages = new ArrayList<>();
        try {
            DirectoryStream<Path> threadPaths = Files.newDirectoryStream(allThreadsPath);
            for (Path threadDirectory : threadPaths) {
                ThreadCpuUsage threadCpuUsage = getThreadCpuUsage(threadDirectory);
                if (threadCpuUsage != null) {
                    threadCpuUsages.add(threadCpuUsage);
                }
            }
            $closeResource(null, threadPaths);
            if (threadCpuUsages.isEmpty()) {
                return null;
            }
            return new ProcessCpuUsage(processId, getProcessName(processPath), uid, threadCpuUsages);
        } catch (IOException e) {
            return null;
        }
    }

    private ThreadCpuUsage getThreadCpuUsage(Path threadDirectory) {
        try {
            String directoryName = threadDirectory.getFileName().toString();
            int threadId = Integer.parseInt(directoryName);
            String threadName = getThreadName(threadDirectory);
            Path threadCpuStatPath = threadDirectory.resolve(CPU_STATISTICS_FILENAME);
            long[] cpuUsagesLong = this.mProcTimeInStateReader.getUsageTimesMillis(threadCpuStatPath);
            if (cpuUsagesLong == null) {
                return null;
            }
            int[] cpuUsages = this.mFrequencyBucketCreator.bucketValues(cpuUsagesLong);
            return new ThreadCpuUsage(threadId, threadName, cpuUsages);
        } catch (NumberFormatException e) {
            Slog.w(TAG, "Failed to parse thread ID when iterating over /proc/*/task", e);
            return null;
        }
    }

    private String getProcessName(Path processPath) {
        Path processNamePath = processPath.resolve(PROCESS_NAME_FILENAME);
        String processName = ProcStatsUtil.readSingleLineProcFile(processNamePath.toString());
        if (processName != null) {
            return processName;
        }
        return DEFAULT_PROCESS_NAME;
    }

    private String getThreadName(Path threadPath) {
        Path threadNamePath = threadPath.resolve(THREAD_NAME_FILENAME);
        String threadName = ProcStatsUtil.readNullSeparatedFile(threadNamePath.toString());
        if (threadName == null) {
            return DEFAULT_THREAD_NAME;
        }
        return threadName;
    }

    private int getProcessId(Path processPath) {
        String fileName = processPath.getFileName().toString();
        try {
            return Integer.parseInt(fileName);
        } catch (NumberFormatException e) {
            Slog.w(TAG, "Failed to parse " + fileName + " as process ID", e);
            return -1;
        }
    }

    @VisibleForTesting
    /* loaded from: classes3.dex */
    public static class FrequencyBucketCreator {
        private final int[] mBucketStartIndices;
        private final int mNumBuckets;
        private final int mNumFrequencies;

        @VisibleForTesting
        public FrequencyBucketCreator(long[] frequencies, int targetNumBuckets) {
            this.mNumFrequencies = frequencies.length;
            int[] clusterStartIndices = getClusterStartIndices(frequencies);
            this.mBucketStartIndices = getBucketStartIndices(clusterStartIndices, targetNumBuckets, this.mNumFrequencies);
            this.mNumBuckets = this.mBucketStartIndices.length;
        }

        @VisibleForTesting
        public int[] bucketValues(long[] values) {
            Preconditions.checkArgument(values.length == this.mNumFrequencies);
            int[] buckets = new int[this.mNumBuckets];
            for (int bucketIdx = 0; bucketIdx < this.mNumBuckets; bucketIdx++) {
                int bucketStartIdx = getLowerBound(bucketIdx, this.mBucketStartIndices);
                int bucketEndIdx = getUpperBound(bucketIdx, this.mBucketStartIndices, values.length);
                for (int valuesIdx = bucketStartIdx; valuesIdx < bucketEndIdx; valuesIdx++) {
                    buckets[bucketIdx] = (int) (buckets[bucketIdx] + values[valuesIdx]);
                }
            }
            return buckets;
        }

        @VisibleForTesting
        public int[] bucketFrequencies(long[] frequencies) {
            Preconditions.checkArgument(frequencies.length == this.mNumFrequencies);
            int[] buckets = new int[this.mNumBuckets];
            for (int i = 0; i < buckets.length; i++) {
                buckets[i] = (int) frequencies[this.mBucketStartIndices[i]];
            }
            return buckets;
        }

        private static int[] getClusterStartIndices(long[] frequencies) {
            ArrayList<Integer> indices = new ArrayList<>();
            indices.add(0);
            for (int i = 0; i < frequencies.length - 1; i++) {
                if (frequencies[i] >= frequencies[i + 1]) {
                    indices.add(Integer.valueOf(i + 1));
                }
            }
            return ArrayUtils.convertToIntArray(indices);
        }

        private static int[] getBucketStartIndices(int[] clusterStartIndices, int targetNumBuckets, int numFrequencies) {
            int previousBucketsInCluster;
            int numClusters = clusterStartIndices.length;
            if (numClusters > targetNumBuckets) {
                return Arrays.copyOfRange(clusterStartIndices, 0, targetNumBuckets);
            }
            ArrayList<Integer> bucketStartIndices = new ArrayList<>();
            for (int clusterIdx = 0; clusterIdx < numClusters; clusterIdx++) {
                int clusterStartIdx = getLowerBound(clusterIdx, clusterStartIndices);
                int clusterEndIdx = getUpperBound(clusterIdx, clusterStartIndices, numFrequencies);
                if (clusterIdx != numClusters - 1) {
                    previousBucketsInCluster = targetNumBuckets / numClusters;
                } else {
                    int numBucketsInCluster = targetNumBuckets / numClusters;
                    previousBucketsInCluster = targetNumBuckets - ((numClusters - 1) * numBucketsInCluster);
                }
                int numFrequenciesInCluster = clusterEndIdx - clusterStartIdx;
                int numFrequenciesInBucket = Math.max(1, numFrequenciesInCluster / previousBucketsInCluster);
                for (int bucketIdx = 0; bucketIdx < previousBucketsInCluster; bucketIdx++) {
                    int bucketStartIdx = (bucketIdx * numFrequenciesInBucket) + clusterStartIdx;
                    if (bucketStartIdx >= clusterEndIdx) {
                        break;
                    }
                    bucketStartIndices.add(Integer.valueOf(bucketStartIdx));
                }
            }
            return ArrayUtils.convertToIntArray(bucketStartIndices);
        }

        private static int getLowerBound(int index, int[] startIndices) {
            return startIndices[index];
        }

        private static int getUpperBound(int index, int[] startIndices, int max) {
            if (index != startIndices.length - 1) {
                return startIndices[index + 1];
            }
            return max;
        }
    }

    /* loaded from: classes3.dex */
    public static class ProcessCpuUsage {
        public final int processId;
        public final String processName;
        public ArrayList<ThreadCpuUsage> threadCpuUsages;
        public final int uid;

        @VisibleForTesting
        public ProcessCpuUsage(int processId, String processName, int uid, ArrayList<ThreadCpuUsage> threadCpuUsages) {
            this.processId = processId;
            this.processName = processName;
            this.uid = uid;
            this.threadCpuUsages = threadCpuUsages;
        }
    }

    /* loaded from: classes3.dex */
    public static class ThreadCpuUsage {
        public final int threadId;
        public final String threadName;
        public int[] usageTimesMillis;

        @VisibleForTesting
        public ThreadCpuUsage(int threadId, String threadName, int[] usageTimesMillis) {
            this.threadId = threadId;
            this.threadName = threadName;
            this.usageTimesMillis = usageTimesMillis;
        }
    }

    @VisibleForTesting
    /* loaded from: classes3.dex */
    public static class Injector {
        public int getUidForPid(int pid) {
            return Process.getUidForPid(pid);
        }
    }
}
