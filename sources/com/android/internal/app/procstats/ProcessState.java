package com.android.internal.app.procstats;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.SettingsStringUtil;
import android.util.ArrayMap;
import android.util.DebugUtils;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.Slog;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import android.util.proto.ProtoUtils;
import com.android.internal.app.procstats.ProcessStats;
import com.android.internal.content.NativeLibraryHelper;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes3.dex */
public final class ProcessState {
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_PARCEL = false;
    private static final String TAG = "ProcessStats";
    private boolean mActive;
    private long mAvgCachedKillPss;
    private ProcessState mCommonProcess;
    private int mCurState;
    private boolean mDead;
    private final DurationsTable mDurations;
    private int mLastPssState;
    private long mLastPssTime;
    private long mMaxCachedKillPss;
    private long mMinCachedKillPss;
    private boolean mMultiPackage;
    private final String mName;
    private int mNumActiveServices;
    private int mNumCachedKill;
    private int mNumExcessiveCpu;
    private int mNumStartedServices;
    private final String mPackage;
    private final PssTable mPssTable;
    private long mStartTime;
    private final ProcessStats mStats;
    private long mTmpTotalTime;
    private final int mUid;
    private final long mVersion;
    public ProcessState tmpFoundSubProc;
    public int tmpNumInUse;
    private static final int[] PROCESS_STATE_TO_STATE = {0, 0, 1, 2, 2, 2, 3, 3, 4, 5, 7, 1, 8, 9, 10, 11, 12, 11, 13};
    public static final Comparator<ProcessState> COMPARATOR = new Comparator<ProcessState>() { // from class: com.android.internal.app.procstats.ProcessState.1
        @Override // java.util.Comparator
        public int compare(ProcessState lhs, ProcessState rhs) {
            if (lhs.mTmpTotalTime >= rhs.mTmpTotalTime) {
                if (lhs.mTmpTotalTime > rhs.mTmpTotalTime) {
                    return 1;
                }
                return 0;
            }
            return -1;
        }
    };

    /* loaded from: classes3.dex */
    static class PssAggr {
        long pss = 0;
        long samples = 0;

        PssAggr() {
        }

        void add(long newPss, long newSamples) {
            this.pss = ((long) ((this.pss * this.samples) + (newPss * newSamples))) / (this.samples + newSamples);
            this.samples += newSamples;
        }
    }

    public ProcessState(ProcessStats processStats, String pkg, int uid, long vers, String name) {
        this.mCurState = -1;
        this.mLastPssState = -1;
        this.mStats = processStats;
        this.mName = name;
        this.mCommonProcess = this;
        this.mPackage = pkg;
        this.mUid = uid;
        this.mVersion = vers;
        this.mDurations = new DurationsTable(processStats.mTableData);
        this.mPssTable = new PssTable(processStats.mTableData);
    }

    public ProcessState(ProcessState commonProcess, String pkg, int uid, long vers, String name, long now) {
        this.mCurState = -1;
        this.mLastPssState = -1;
        this.mStats = commonProcess.mStats;
        this.mName = name;
        this.mCommonProcess = commonProcess;
        this.mPackage = pkg;
        this.mUid = uid;
        this.mVersion = vers;
        this.mCurState = commonProcess.mCurState;
        this.mStartTime = now;
        this.mDurations = new DurationsTable(commonProcess.mStats.mTableData);
        this.mPssTable = new PssTable(commonProcess.mStats.mTableData);
    }

    public ProcessState clone(long now) {
        ProcessState pnew = new ProcessState(this, this.mPackage, this.mUid, this.mVersion, this.mName, now);
        pnew.mDurations.addDurations(this.mDurations);
        pnew.mPssTable.copyFrom(this.mPssTable, 10);
        pnew.mNumExcessiveCpu = this.mNumExcessiveCpu;
        pnew.mNumCachedKill = this.mNumCachedKill;
        pnew.mMinCachedKillPss = this.mMinCachedKillPss;
        pnew.mAvgCachedKillPss = this.mAvgCachedKillPss;
        pnew.mMaxCachedKillPss = this.mMaxCachedKillPss;
        pnew.mActive = this.mActive;
        pnew.mNumActiveServices = this.mNumActiveServices;
        pnew.mNumStartedServices = this.mNumStartedServices;
        return pnew;
    }

    public String getName() {
        return this.mName;
    }

    public ProcessState getCommonProcess() {
        return this.mCommonProcess;
    }

    public void makeStandalone() {
        this.mCommonProcess = this;
    }

    public String getPackage() {
        return this.mPackage;
    }

    public int getUid() {
        return this.mUid;
    }

    public long getVersion() {
        return this.mVersion;
    }

    public boolean isMultiPackage() {
        return this.mMultiPackage;
    }

    public void setMultiPackage(boolean val) {
        this.mMultiPackage = val;
    }

    public int getDurationsBucketCount() {
        return this.mDurations.getKeyCount();
    }

    public void add(ProcessState other) {
        this.mDurations.addDurations(other.mDurations);
        this.mPssTable.mergeStats(other.mPssTable);
        this.mNumExcessiveCpu += other.mNumExcessiveCpu;
        if (other.mNumCachedKill > 0) {
            addCachedKill(other.mNumCachedKill, other.mMinCachedKillPss, other.mAvgCachedKillPss, other.mMaxCachedKillPss);
        }
    }

    public void resetSafely(long now) {
        this.mDurations.resetTable();
        this.mPssTable.resetTable();
        this.mStartTime = now;
        this.mLastPssState = -1;
        this.mLastPssTime = 0L;
        this.mNumExcessiveCpu = 0;
        this.mNumCachedKill = 0;
        this.mMaxCachedKillPss = 0L;
        this.mAvgCachedKillPss = 0L;
        this.mMinCachedKillPss = 0L;
    }

    public void makeDead() {
        this.mDead = true;
    }

    private void ensureNotDead() {
        if (!this.mDead) {
            return;
        }
        Slog.w("ProcessStats", "ProcessState dead: name=" + this.mName + " pkg=" + this.mPackage + " uid=" + this.mUid + " common.name=" + this.mCommonProcess.mName);
    }

    public void writeToParcel(Parcel out, long now) {
        out.writeInt(this.mMultiPackage ? 1 : 0);
        this.mDurations.writeToParcel(out);
        this.mPssTable.writeToParcel(out);
        out.writeInt(0);
        out.writeInt(this.mNumExcessiveCpu);
        out.writeInt(this.mNumCachedKill);
        if (this.mNumCachedKill > 0) {
            out.writeLong(this.mMinCachedKillPss);
            out.writeLong(this.mAvgCachedKillPss);
            out.writeLong(this.mMaxCachedKillPss);
        }
    }

    public boolean readFromParcel(Parcel in, boolean fully) {
        boolean multiPackage = in.readInt() != 0;
        if (fully) {
            this.mMultiPackage = multiPackage;
        }
        if (this.mDurations.readFromParcel(in) && this.mPssTable.readFromParcel(in)) {
            in.readInt();
            this.mNumExcessiveCpu = in.readInt();
            this.mNumCachedKill = in.readInt();
            if (this.mNumCachedKill > 0) {
                this.mMinCachedKillPss = in.readLong();
                this.mAvgCachedKillPss = in.readLong();
                this.mMaxCachedKillPss = in.readLong();
            } else {
                this.mMaxCachedKillPss = 0L;
                this.mAvgCachedKillPss = 0L;
                this.mMinCachedKillPss = 0L;
            }
            return true;
        }
        return false;
    }

    public void makeActive() {
        ensureNotDead();
        this.mActive = true;
    }

    public void makeInactive() {
        this.mActive = false;
    }

    public boolean isInUse() {
        return this.mActive || this.mNumActiveServices > 0 || this.mNumStartedServices > 0 || this.mCurState != -1;
    }

    public boolean isActive() {
        return this.mActive;
    }

    public boolean hasAnyData() {
        return (this.mDurations.getKeyCount() == 0 && this.mCurState == -1 && this.mPssTable.getKeyCount() == 0) ? false : true;
    }

    public void setState(int state, int memFactor, long now, ArrayMap<String, ProcessStats.ProcessStateHolder> pkgList) {
        int state2;
        if (state < 0) {
            state2 = this.mNumStartedServices > 0 ? 6 + (memFactor * 14) : -1;
        } else {
            state2 = PROCESS_STATE_TO_STATE[state] + (memFactor * 14);
        }
        this.mCommonProcess.setState(state2, now);
        if (this.mCommonProcess.mMultiPackage && pkgList != null) {
            for (int ip = pkgList.size() - 1; ip >= 0; ip--) {
                pullFixedProc(pkgList, ip).setState(state2, now);
            }
        }
    }

    public void setState(int state, long now) {
        ensureNotDead();
        if (!this.mDead && this.mCurState != state) {
            commitStateTime(now);
            this.mCurState = state;
        }
    }

    public void commitStateTime(long now) {
        if (this.mCurState != -1) {
            long dur = now - this.mStartTime;
            if (dur > 0) {
                this.mDurations.addDuration(this.mCurState, dur);
            }
        }
        this.mStartTime = now;
    }

    public void incActiveServices(String serviceName) {
        if (this.mCommonProcess != this) {
            this.mCommonProcess.incActiveServices(serviceName);
        }
        this.mNumActiveServices++;
    }

    public void decActiveServices(String serviceName) {
        if (this.mCommonProcess != this) {
            this.mCommonProcess.decActiveServices(serviceName);
        }
        this.mNumActiveServices--;
        if (this.mNumActiveServices < 0) {
            Slog.wtfStack("ProcessStats", "Proc active services underrun: pkg=" + this.mPackage + " uid=" + this.mUid + " proc=" + this.mName + " service=" + serviceName);
            this.mNumActiveServices = 0;
        }
    }

    public void incStartedServices(int memFactor, long now, String serviceName) {
        if (this.mCommonProcess != this) {
            this.mCommonProcess.incStartedServices(memFactor, now, serviceName);
        }
        this.mNumStartedServices++;
        if (this.mNumStartedServices == 1 && this.mCurState == -1) {
            setState(6 + (memFactor * 14), now);
        }
    }

    public void decStartedServices(int memFactor, long now, String serviceName) {
        if (this.mCommonProcess != this) {
            this.mCommonProcess.decStartedServices(memFactor, now, serviceName);
        }
        this.mNumStartedServices--;
        if (this.mNumStartedServices == 0 && this.mCurState % 14 == 6) {
            setState(-1, now);
        } else if (this.mNumStartedServices < 0) {
            Slog.wtfStack("ProcessStats", "Proc started services underrun: pkg=" + this.mPackage + " uid=" + this.mUid + " name=" + this.mName);
            this.mNumStartedServices = 0;
        }
    }

    public void addPss(long pss, long uss, long rss, boolean always, int type, long duration, ArrayMap<String, ProcessStats.ProcessStateHolder> pkgList) {
        ensureNotDead();
        switch (type) {
            case 0:
                this.mStats.mInternalSinglePssCount++;
                this.mStats.mInternalSinglePssTime += duration;
                break;
            case 1:
                this.mStats.mInternalAllMemPssCount++;
                this.mStats.mInternalAllMemPssTime += duration;
                break;
            case 2:
                this.mStats.mInternalAllPollPssCount++;
                this.mStats.mInternalAllPollPssTime += duration;
                break;
            case 3:
                this.mStats.mExternalPssCount++;
                this.mStats.mExternalPssTime += duration;
                break;
            case 4:
                this.mStats.mExternalSlowPssCount++;
                this.mStats.mExternalSlowPssTime += duration;
                break;
        }
        if (!always && this.mLastPssState == this.mCurState && SystemClock.uptimeMillis() < this.mLastPssTime + 30000) {
            return;
        }
        this.mLastPssState = this.mCurState;
        this.mLastPssTime = SystemClock.uptimeMillis();
        if (this.mCurState != -1) {
            this.mCommonProcess.mPssTable.mergeStats(this.mCurState, 1, pss, pss, pss, uss, uss, uss, rss, rss, rss);
            if (this.mCommonProcess.mMultiPackage && pkgList != null) {
                for (int ip = pkgList.size() - 1; ip >= 0; ip--) {
                    pullFixedProc(pkgList, ip).mPssTable.mergeStats(this.mCurState, 1, pss, pss, pss, uss, uss, uss, rss, rss, rss);
                }
            }
        }
    }

    public void reportExcessiveCpu(ArrayMap<String, ProcessStats.ProcessStateHolder> pkgList) {
        ensureNotDead();
        this.mCommonProcess.mNumExcessiveCpu++;
        if (!this.mCommonProcess.mMultiPackage) {
            return;
        }
        for (int ip = pkgList.size() - 1; ip >= 0; ip--) {
            pullFixedProc(pkgList, ip).mNumExcessiveCpu++;
        }
    }

    private void addCachedKill(int num, long minPss, long avgPss, long maxPss) {
        if (this.mNumCachedKill <= 0) {
            this.mNumCachedKill = num;
            this.mMinCachedKillPss = minPss;
            this.mAvgCachedKillPss = avgPss;
            this.mMaxCachedKillPss = maxPss;
            return;
        }
        if (minPss < this.mMinCachedKillPss) {
            this.mMinCachedKillPss = minPss;
        }
        if (maxPss > this.mMaxCachedKillPss) {
            this.mMaxCachedKillPss = maxPss;
        }
        this.mAvgCachedKillPss = (long) (((this.mAvgCachedKillPss * this.mNumCachedKill) + avgPss) / (this.mNumCachedKill + num));
        this.mNumCachedKill += num;
    }

    public void reportCachedKill(ArrayMap<String, ProcessStats.ProcessStateHolder> pkgList, long pss) {
        ensureNotDead();
        this.mCommonProcess.addCachedKill(1, pss, pss, pss);
        if (!this.mCommonProcess.mMultiPackage) {
            return;
        }
        for (int ip = pkgList.size() - 1; ip >= 0; ip--) {
            pullFixedProc(pkgList, ip).addCachedKill(1, pss, pss, pss);
        }
    }

    public ProcessState pullFixedProc(String pkgName) {
        if (this.mMultiPackage) {
            LongSparseArray<ProcessStats.PackageState> vpkg = this.mStats.mPackages.get(pkgName, this.mUid);
            if (vpkg == null) {
                throw new IllegalStateException("Didn't find package " + pkgName + " / " + this.mUid);
            }
            ProcessStats.PackageState pkg = vpkg.get(this.mVersion);
            if (pkg == null) {
                throw new IllegalStateException("Didn't find package " + pkgName + " / " + this.mUid + " vers " + this.mVersion);
            }
            ProcessState proc = pkg.mProcesses.get(this.mName);
            if (proc == null) {
                throw new IllegalStateException("Didn't create per-package process " + this.mName + " in pkg " + pkgName + " / " + this.mUid + " vers " + this.mVersion);
            }
            return proc;
        }
        return this;
    }

    private ProcessState pullFixedProc(ArrayMap<String, ProcessStats.ProcessStateHolder> pkgList, int index) {
        ProcessStats.ProcessStateHolder holder = pkgList.valueAt(index);
        ProcessState proc = holder.state;
        if (this.mDead && proc.mCommonProcess != proc) {
            Log.wtf("ProcessStats", "Pulling dead proc: name=" + this.mName + " pkg=" + this.mPackage + " uid=" + this.mUid + " common.name=" + this.mCommonProcess.mName);
            proc = this.mStats.getProcessStateLocked(proc.mPackage, proc.mUid, proc.mVersion, proc.mName);
        }
        if (proc.mMultiPackage) {
            LongSparseArray<ProcessStats.PackageState> vpkg = this.mStats.mPackages.get(pkgList.keyAt(index), proc.mUid);
            if (vpkg == null) {
                throw new IllegalStateException("No existing package " + pkgList.keyAt(index) + "/" + proc.mUid + " for multi-proc " + proc.mName);
            }
            ProcessStats.PackageState pkg = vpkg.get(proc.mVersion);
            if (pkg == null) {
                throw new IllegalStateException("No existing package " + pkgList.keyAt(index) + "/" + proc.mUid + " for multi-proc " + proc.mName + " version " + proc.mVersion);
            }
            String savedName = proc.mName;
            proc = pkg.mProcesses.get(proc.mName);
            if (proc == null) {
                throw new IllegalStateException("Didn't create per-package process " + savedName + " in pkg " + pkg.mPackageName + "/" + pkg.mUid);
            }
            holder.state = proc;
        }
        return proc;
    }

    public long getDuration(int state, long now) {
        long time = this.mDurations.getValueForId((byte) state);
        if (this.mCurState == state) {
            return time + (now - this.mStartTime);
        }
        return time;
    }

    public long getPssSampleCount(int state) {
        return this.mPssTable.getValueForId((byte) state, 0);
    }

    public long getPssMinimum(int state) {
        return this.mPssTable.getValueForId((byte) state, 1);
    }

    public long getPssAverage(int state) {
        return this.mPssTable.getValueForId((byte) state, 2);
    }

    public long getPssMaximum(int state) {
        return this.mPssTable.getValueForId((byte) state, 3);
    }

    public long getPssUssMinimum(int state) {
        return this.mPssTable.getValueForId((byte) state, 4);
    }

    public long getPssUssAverage(int state) {
        return this.mPssTable.getValueForId((byte) state, 5);
    }

    public long getPssUssMaximum(int state) {
        return this.mPssTable.getValueForId((byte) state, 6);
    }

    public long getPssRssMinimum(int state) {
        return this.mPssTable.getValueForId((byte) state, 7);
    }

    public long getPssRssAverage(int state) {
        return this.mPssTable.getValueForId((byte) state, 8);
    }

    public long getPssRssMaximum(int state) {
        return this.mPssTable.getValueForId((byte) state, 9);
    }

    public void aggregatePss(ProcessStats.TotalMemoryUseCollection data, long now) {
        long time;
        long samples;
        long avg;
        int[] iArr;
        ProcessState processState = this;
        ProcessStats.TotalMemoryUseCollection totalMemoryUseCollection = data;
        PssAggr fgPss = new PssAggr();
        PssAggr bgPss = new PssAggr();
        PssAggr cachedPss = new PssAggr();
        boolean havePss = false;
        for (int i = 0; i < processState.mDurations.getKeyCount(); i++) {
            int type = SparseMappingTable.getIdFromKey(processState.mDurations.getKeyAt(i));
            int procState = type % 14;
            boolean havePss2 = havePss;
            long samples2 = processState.getPssSampleCount(type);
            if (samples2 > 0) {
                long avg2 = processState.getPssAverage(type);
                havePss2 = true;
                if (procState <= 2) {
                    fgPss.add(avg2, samples2);
                } else if (procState <= 7) {
                    bgPss.add(avg2, samples2);
                } else {
                    cachedPss.add(avg2, samples2);
                }
            }
            havePss = havePss2;
        }
        if (!havePss) {
            return;
        }
        boolean fgHasBg = false;
        boolean fgHasCached = false;
        boolean bgHasCached = false;
        if (fgPss.samples < 3 && bgPss.samples > 0) {
            fgHasBg = true;
            fgPss.add(bgPss.pss, bgPss.samples);
        }
        if (fgPss.samples < 3 && cachedPss.samples > 0) {
            fgHasCached = true;
            fgPss.add(cachedPss.pss, cachedPss.samples);
        }
        if (bgPss.samples < 3 && cachedPss.samples > 0) {
            bgHasCached = true;
            bgPss.add(cachedPss.pss, cachedPss.samples);
        }
        if (bgPss.samples < 3 && !fgHasBg && fgPss.samples > 0) {
            bgPss.add(fgPss.pss, fgPss.samples);
        }
        if (cachedPss.samples < 3 && !bgHasCached && bgPss.samples > 0) {
            cachedPss.add(bgPss.pss, bgPss.samples);
        }
        if (cachedPss.samples < 3 && !fgHasCached && fgPss.samples > 0) {
            cachedPss.add(fgPss.pss, fgPss.samples);
        }
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= processState.mDurations.getKeyCount()) {
                return;
            }
            int key = processState.mDurations.getKeyAt(i3);
            int type2 = SparseMappingTable.getIdFromKey(key);
            long time2 = processState.mDurations.getValue(key);
            if (processState.mCurState == type2) {
                time2 += now - processState.mStartTime;
            }
            int procState2 = type2 % 14;
            long[] jArr = totalMemoryUseCollection.processStateTime;
            jArr[procState2] = jArr[procState2] + time2;
            long samples3 = processState.getPssSampleCount(type2);
            if (samples3 > 0) {
                long avg3 = processState.getPssAverage(type2);
                time = time2;
                samples = samples3;
                avg = avg3;
            } else if (procState2 <= 2) {
                time = time2;
                samples = fgPss.samples;
                avg = fgPss.pss;
            } else {
                time = time2;
                if (procState2 <= 7) {
                    long samples4 = bgPss.samples;
                    long avg4 = bgPss.pss;
                    avg = avg4;
                    samples = samples4;
                } else {
                    samples = cachedPss.samples;
                    avg = cachedPss.pss;
                }
            }
            PssAggr fgPss2 = fgPss;
            double newAvg = ((totalMemoryUseCollection.processStatePss[procState2] * totalMemoryUseCollection.processStateSamples[procState2]) + (avg * samples)) / (totalMemoryUseCollection.processStateSamples[procState2] + samples);
            totalMemoryUseCollection.processStatePss[procState2] = (long) newAvg;
            totalMemoryUseCollection.processStateSamples[procState2] = (int) (iArr[procState2] + samples);
            double[] dArr = totalMemoryUseCollection.processStateWeight;
            dArr[procState2] = dArr[procState2] + (avg * time);
            i2 = i3 + 1;
            fgPss = fgPss2;
            bgPss = bgPss;
            cachedPss = cachedPss;
            fgHasBg = fgHasBg;
            fgHasCached = fgHasCached;
            bgHasCached = bgHasCached;
            processState = this;
            totalMemoryUseCollection = data;
        }
    }

    public long computeProcessTimeLocked(int[] screenStates, int[] memStates, int[] procStates, long now) {
        long totalTime = 0;
        for (int i : screenStates) {
            int im = 0;
            while (im < memStates.length) {
                long totalTime2 = totalTime;
                for (int i2 : procStates) {
                    int bucket = ((i + memStates[im]) * 14) + i2;
                    totalTime2 += getDuration(bucket, now);
                }
                im++;
                totalTime = totalTime2;
            }
        }
        this.mTmpTotalTime = totalTime;
        return totalTime;
    }

    public void dumpSummary(PrintWriter pw, String prefix, int[] screenStates, int[] memStates, int[] procStates, long now, long totalTime) {
        pw.print(prefix);
        pw.print("* ");
        pw.print(this.mName);
        pw.print(" / ");
        UserHandle.formatUid(pw, this.mUid);
        pw.print(" / v");
        pw.print(this.mVersion);
        pw.println(SettingsStringUtil.DELIMITER);
        dumpProcessSummaryDetails(pw, prefix, "         TOTAL: ", screenStates, memStates, procStates, now, totalTime, true);
        dumpProcessSummaryDetails(pw, prefix, "    Persistent: ", screenStates, memStates, new int[]{0}, now, totalTime, true);
        dumpProcessSummaryDetails(pw, prefix, "           Top: ", screenStates, memStates, new int[]{1}, now, totalTime, true);
        dumpProcessSummaryDetails(pw, prefix, "        Imp Fg: ", screenStates, memStates, new int[]{2}, now, totalTime, true);
        dumpProcessSummaryDetails(pw, prefix, "        Imp Bg: ", screenStates, memStates, new int[]{3}, now, totalTime, true);
        dumpProcessSummaryDetails(pw, prefix, "        Backup: ", screenStates, memStates, new int[]{4}, now, totalTime, true);
        dumpProcessSummaryDetails(pw, prefix, "     Heavy Wgt: ", screenStates, memStates, new int[]{8}, now, totalTime, true);
        dumpProcessSummaryDetails(pw, prefix, "       Service: ", screenStates, memStates, new int[]{5}, now, totalTime, true);
        dumpProcessSummaryDetails(pw, prefix, "    Service Rs: ", screenStates, memStates, new int[]{6}, now, totalTime, true);
        dumpProcessSummaryDetails(pw, prefix, "      Receiver: ", screenStates, memStates, new int[]{7}, now, totalTime, true);
        dumpProcessSummaryDetails(pw, prefix, "         Heavy: ", screenStates, memStates, new int[]{9}, now, totalTime, true);
        dumpProcessSummaryDetails(pw, prefix, "        (Home): ", screenStates, memStates, new int[]{9}, now, totalTime, true);
        dumpProcessSummaryDetails(pw, prefix, "    (Last Act): ", screenStates, memStates, new int[]{10}, now, totalTime, true);
        dumpProcessSummaryDetails(pw, prefix, "      (Cached): ", screenStates, memStates, new int[]{11, 12, 13}, now, totalTime, true);
    }

    public void dumpProcessState(PrintWriter pw, String prefix, int[] screenStates, int[] memStates, int[] procStates, long now) {
        int i;
        String running;
        ProcessState processState = this;
        int printedScreen = -1;
        long totalTime = 0;
        int is = 0;
        while (is < screenStates.length) {
            int printedMem = -1;
            long totalTime2 = totalTime;
            int im = 0;
            while (im < memStates.length) {
                int ip = 0;
                while (ip < procStates.length) {
                    int iscreen = screenStates[is];
                    int imem = memStates[im];
                    int bucket = ((iscreen + imem) * 14) + procStates[ip];
                    long time = processState.mDurations.getValueForId((byte) bucket);
                    if (processState.mCurState != bucket) {
                        running = "";
                    } else {
                        running = " (running)";
                    }
                    if (time != 0) {
                        pw.print(prefix);
                        if (screenStates.length > 1) {
                            DumpUtils.printScreenLabel(pw, printedScreen != iscreen ? iscreen : -1);
                            printedScreen = iscreen;
                        }
                        if (memStates.length > 1) {
                            DumpUtils.printMemLabel(pw, printedMem != imem ? imem : -1, '/');
                            printedMem = imem;
                        }
                        pw.print(DumpUtils.STATE_NAMES[procStates[ip]]);
                        pw.print(": ");
                        TimeUtils.formatDuration(time, pw);
                        pw.println(running);
                        totalTime2 += time;
                    }
                    ip++;
                    processState = this;
                }
                im++;
                processState = this;
            }
            is++;
            totalTime = totalTime2;
            processState = this;
        }
        if (totalTime != 0) {
            pw.print(prefix);
            if (screenStates.length > 1) {
                i = -1;
                DumpUtils.printScreenLabel(pw, -1);
            } else {
                i = -1;
            }
            if (memStates.length > 1) {
                DumpUtils.printMemLabel(pw, i, '/');
            }
            pw.print("TOTAL  : ");
            TimeUtils.formatDuration(totalTime, pw);
            pw.println();
        }
    }

    public void dumpPss(PrintWriter pw, String prefix, int[] screenStates, int[] memStates, int[] procStates) {
        int[] iArr = screenStates;
        int[] iArr2 = memStates;
        int printedScreen = -1;
        boolean printedHeader = false;
        int is = 0;
        while (is < iArr.length) {
            int printedMem = -1;
            int printedScreen2 = printedScreen;
            boolean printedHeader2 = printedHeader;
            int im = 0;
            while (im < iArr2.length) {
                int printedMem2 = printedMem;
                boolean printedHeader3 = printedHeader2;
                int ip = 0;
                while (ip < procStates.length) {
                    int iscreen = iArr[is];
                    int imem = iArr2[im];
                    int bucket = ((iscreen + imem) * 14) + procStates[ip];
                    long count = getPssSampleCount(bucket);
                    if (count > 0) {
                        if (!printedHeader3) {
                            pw.print(prefix);
                            pw.print("PSS/USS (");
                            pw.print(this.mPssTable.getKeyCount());
                            pw.println(" entries):");
                            printedHeader3 = true;
                        }
                        pw.print(prefix);
                        boolean printedHeader4 = printedHeader3;
                        pw.print("  ");
                        if (iArr.length > 1) {
                            DumpUtils.printScreenLabel(pw, printedScreen2 != iscreen ? iscreen : -1);
                            printedScreen2 = iscreen;
                        }
                        if (iArr2.length > 1) {
                            DumpUtils.printMemLabel(pw, printedMem2 != imem ? imem : -1, '/');
                            printedMem2 = imem;
                        }
                        pw.print(DumpUtils.STATE_NAMES[procStates[ip]]);
                        pw.print(": ");
                        pw.print(count);
                        pw.print(" samples ");
                        DebugUtils.printSizeValue(pw, getPssMinimum(bucket) * 1024);
                        pw.print(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                        DebugUtils.printSizeValue(pw, getPssAverage(bucket) * 1024);
                        pw.print(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                        DebugUtils.printSizeValue(pw, getPssMaximum(bucket) * 1024);
                        pw.print(" / ");
                        DebugUtils.printSizeValue(pw, getPssUssMinimum(bucket) * 1024);
                        pw.print(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                        DebugUtils.printSizeValue(pw, getPssUssAverage(bucket) * 1024);
                        pw.print(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                        DebugUtils.printSizeValue(pw, getPssUssMaximum(bucket) * 1024);
                        pw.print(" / ");
                        DebugUtils.printSizeValue(pw, getPssRssMinimum(bucket) * 1024);
                        pw.print(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                        DebugUtils.printSizeValue(pw, getPssRssAverage(bucket) * 1024);
                        pw.print(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                        DebugUtils.printSizeValue(pw, getPssRssMaximum(bucket) * 1024);
                        pw.println();
                        printedHeader3 = printedHeader4;
                    }
                    ip++;
                    iArr = screenStates;
                    iArr2 = memStates;
                }
                boolean printedHeader5 = printedHeader3;
                im++;
                printedMem = printedMem2;
                printedHeader2 = printedHeader5;
                iArr = screenStates;
                iArr2 = memStates;
            }
            is++;
            printedHeader = printedHeader2;
            printedScreen = printedScreen2;
            iArr = screenStates;
            iArr2 = memStates;
        }
        if (this.mNumExcessiveCpu != 0) {
            pw.print(prefix);
            pw.print("Killed for excessive CPU use: ");
            pw.print(this.mNumExcessiveCpu);
            pw.println(" times");
        }
        if (this.mNumCachedKill != 0) {
            pw.print(prefix);
            pw.print("Killed from cached state: ");
            pw.print(this.mNumCachedKill);
            pw.print(" times from pss ");
            DebugUtils.printSizeValue(pw, this.mMinCachedKillPss * 1024);
            pw.print(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
            DebugUtils.printSizeValue(pw, this.mAvgCachedKillPss * 1024);
            pw.print(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
            DebugUtils.printSizeValue(pw, this.mMaxCachedKillPss * 1024);
            pw.println();
        }
    }

    private void dumpProcessSummaryDetails(PrintWriter pw, String prefix, String label, int[] screenStates, int[] memStates, int[] procStates, long now, long totalTime, boolean full) {
        ProcessStats.ProcessDataCollection totals = new ProcessStats.ProcessDataCollection(screenStates, memStates, procStates);
        computeProcessData(totals, now);
        double percentage = (totals.totalTime / totalTime) * 100.0d;
        if (percentage >= 0.005d || totals.numPss != 0) {
            if (prefix != null) {
                pw.print(prefix);
            }
            if (label != null) {
                pw.print(label);
            }
            totals.print(pw, totalTime, full);
            if (prefix != null) {
                pw.println();
            }
        }
    }

    public void dumpInternalLocked(PrintWriter pw, String prefix, boolean dumpAll) {
        if (dumpAll) {
            pw.print(prefix);
            pw.print("myID=");
            pw.print(Integer.toHexString(System.identityHashCode(this)));
            pw.print(" mCommonProcess=");
            pw.print(Integer.toHexString(System.identityHashCode(this.mCommonProcess)));
            pw.print(" mPackage=");
            pw.println(this.mPackage);
            if (this.mMultiPackage) {
                pw.print(prefix);
                pw.print("mMultiPackage=");
                pw.println(this.mMultiPackage);
            }
            if (this != this.mCommonProcess) {
                pw.print(prefix);
                pw.print("Common Proc: ");
                pw.print(this.mCommonProcess.mName);
                pw.print("/");
                pw.print(this.mCommonProcess.mUid);
                pw.print(" pkg=");
                pw.println(this.mCommonProcess.mPackage);
            }
        }
        if (this.mActive) {
            pw.print(prefix);
            pw.print("mActive=");
            pw.println(this.mActive);
        }
        if (this.mDead) {
            pw.print(prefix);
            pw.print("mDead=");
            pw.println(this.mDead);
        }
        if (this.mNumActiveServices != 0 || this.mNumStartedServices != 0) {
            pw.print(prefix);
            pw.print("mNumActiveServices=");
            pw.print(this.mNumActiveServices);
            pw.print(" mNumStartedServices=");
            pw.println(this.mNumStartedServices);
        }
    }

    public void computeProcessData(ProcessStats.ProcessDataCollection data, long now) {
        long j;
        int is;
        int im;
        int ip;
        long avgPss;
        long maxUss;
        long minRss;
        long j2 = 0;
        data.totalTime = 0L;
        data.maxRss = 0L;
        data.avgRss = 0L;
        data.minRss = 0L;
        data.maxUss = 0L;
        data.avgUss = 0L;
        data.minUss = 0L;
        data.maxPss = 0L;
        data.avgPss = 0L;
        data.minPss = 0L;
        data.numPss = 0L;
        int is2 = 0;
        while (is2 < data.screenStates.length) {
            int im2 = 0;
            while (im2 < data.memStates.length) {
                int ip2 = 0;
                while (ip2 < data.procStates.length) {
                    int bucket = ((data.screenStates[is2] + data.memStates[im2]) * 14) + data.procStates[ip2];
                    data.totalTime += getDuration(bucket, now);
                    long samples = getPssSampleCount(bucket);
                    if (samples <= j2) {
                        j = j2;
                        is = is2;
                        im = im2;
                        ip = ip2;
                    } else {
                        long minPss = getPssMinimum(bucket);
                        is = is2;
                        long avgPss2 = getPssAverage(bucket);
                        long maxPss = getPssMaximum(bucket);
                        long minUss = getPssUssMinimum(bucket);
                        im = im2;
                        ip = ip2;
                        long avgUss = getPssUssAverage(bucket);
                        long samples2 = getPssUssMaximum(bucket);
                        long maxUss2 = getPssRssMinimum(bucket);
                        long minRss2 = getPssRssAverage(bucket);
                        long avgRss = getPssRssMaximum(bucket);
                        j = 0;
                        if (data.numPss == 0) {
                            data.minPss = minPss;
                            data.avgPss = avgPss2;
                            data.maxPss = maxPss;
                            data.minUss = minUss;
                            data.avgUss = avgUss;
                            data.maxUss = samples2;
                            data.minRss = maxUss2;
                            data.avgRss = minRss2;
                            data.maxRss = avgRss;
                            avgPss = samples;
                        } else {
                            long maxRss = data.minPss;
                            if (minPss < maxRss) {
                                data.minPss = minPss;
                            }
                            double d = avgPss2;
                            avgPss = samples;
                            data.avgPss = (long) (((data.avgPss * data.numPss) + (d * avgPss)) / (data.numPss + avgPss));
                            if (maxPss > data.maxPss) {
                                data.maxPss = maxPss;
                            }
                            if (minUss < data.minUss) {
                                data.minUss = minUss;
                            }
                            data.avgUss = (long) (((data.avgUss * data.numPss) + (avgUss * avgPss)) / (data.numPss + avgPss));
                            if (samples2 > data.maxUss) {
                                maxUss = samples2;
                                data.maxUss = maxUss;
                            } else {
                                maxUss = samples2;
                            }
                            if (maxUss2 < data.minRss) {
                                minRss = maxUss2;
                                data.minRss = minRss;
                            } else {
                                minRss = maxUss2;
                            }
                            data.avgRss = (long) (((data.avgRss * data.numPss) + (minRss2 * avgPss)) / (data.numPss + avgPss));
                            if (avgRss > data.maxRss) {
                                data.maxRss = avgRss;
                            }
                        }
                        data.numPss += avgPss;
                    }
                    ip2 = ip + 1;
                    is2 = is;
                    j2 = j;
                    im2 = im;
                }
                im2++;
            }
            is2++;
        }
    }

    public void dumpCsv(PrintWriter pw, boolean sepScreenStates, int[] screenStates, boolean sepMemStates, int[] memStates, boolean sepProcStates, int[] procStates, long now) {
        int NSS;
        int NSS2;
        int iss;
        int[] iArr = screenStates;
        int[] iArr2 = memStates;
        int NSS3 = sepScreenStates ? iArr.length : 1;
        int NMS = sepMemStates ? iArr2.length : 1;
        int NPS = sepProcStates ? procStates.length : 1;
        int iss2 = 0;
        while (iss2 < NSS3) {
            int ims = 0;
            while (ims < NMS) {
                int ips = 0;
                while (ips < NPS) {
                    int vsscreen = sepScreenStates ? iArr[iss2] : 0;
                    int vsmem = sepMemStates ? iArr2[ims] : 0;
                    int vsproc = sepProcStates ? procStates[ips] : 0;
                    int NSA = sepScreenStates ? 1 : iArr.length;
                    int NMA = sepMemStates ? 1 : iArr2.length;
                    if (sepProcStates) {
                        NSS = NSS3;
                        NSS2 = 1;
                    } else {
                        NSS = NSS3;
                        NSS2 = procStates.length;
                    }
                    int NMS2 = NMS;
                    int NPS2 = NPS;
                    long totalTime = 0;
                    int isa = 0;
                    while (true) {
                        int isa2 = isa;
                        iss = iss2;
                        if (isa2 < NSA) {
                            long totalTime2 = totalTime;
                            int ima = 0;
                            while (ima < NMA) {
                                int ipa = 0;
                                while (ipa < NSS2) {
                                    int vascreen = sepScreenStates ? 0 : iArr[isa2];
                                    int vamem = sepMemStates ? 0 : iArr2[ima];
                                    int vaproc = sepProcStates ? 0 : procStates[ipa];
                                    int bucket = ((vsscreen + vascreen + vsmem + vamem) * 14) + vsproc + vaproc;
                                    totalTime2 += getDuration(bucket, now);
                                    ipa++;
                                    iArr = screenStates;
                                    iArr2 = memStates;
                                }
                                ima++;
                                iArr = screenStates;
                                iArr2 = memStates;
                            }
                            totalTime = totalTime2;
                            iss2 = iss;
                            iArr = screenStates;
                            iArr2 = memStates;
                            isa = isa2 + 1;
                        }
                    }
                    pw.print("\t");
                    pw.print(totalTime);
                    ips++;
                    NSS3 = NSS;
                    NMS = NMS2;
                    NPS = NPS2;
                    iss2 = iss;
                    iArr = screenStates;
                    iArr2 = memStates;
                }
                ims++;
                iArr = screenStates;
                iArr2 = memStates;
            }
            iss2++;
            iArr = screenStates;
            iArr2 = memStates;
        }
    }

    public void dumpPackageProcCheckin(PrintWriter pw, String pkgName, int uid, long vers, String itemName, long now) {
        pw.print("pkgproc,");
        pw.print(pkgName);
        pw.print(",");
        pw.print(uid);
        pw.print(",");
        pw.print(vers);
        pw.print(",");
        pw.print(DumpUtils.collapseString(pkgName, itemName));
        dumpAllStateCheckin(pw, now);
        pw.println();
        if (this.mPssTable.getKeyCount() > 0) {
            pw.print("pkgpss,");
            pw.print(pkgName);
            pw.print(",");
            pw.print(uid);
            pw.print(",");
            pw.print(vers);
            pw.print(",");
            pw.print(DumpUtils.collapseString(pkgName, itemName));
            dumpAllPssCheckin(pw);
            pw.println();
        }
        if (this.mNumExcessiveCpu > 0 || this.mNumCachedKill > 0) {
            pw.print("pkgkills,");
            pw.print(pkgName);
            pw.print(",");
            pw.print(uid);
            pw.print(",");
            pw.print(vers);
            pw.print(",");
            pw.print(DumpUtils.collapseString(pkgName, itemName));
            pw.print(",");
            pw.print("0");
            pw.print(",");
            pw.print(this.mNumExcessiveCpu);
            pw.print(",");
            pw.print(this.mNumCachedKill);
            pw.print(",");
            pw.print(this.mMinCachedKillPss);
            pw.print(SettingsStringUtil.DELIMITER);
            pw.print(this.mAvgCachedKillPss);
            pw.print(SettingsStringUtil.DELIMITER);
            pw.print(this.mMaxCachedKillPss);
            pw.println();
        }
    }

    public void dumpProcCheckin(PrintWriter pw, String procName, int uid, long now) {
        if (this.mDurations.getKeyCount() > 0) {
            pw.print("proc,");
            pw.print(procName);
            pw.print(",");
            pw.print(uid);
            dumpAllStateCheckin(pw, now);
            pw.println();
        }
        if (this.mPssTable.getKeyCount() > 0) {
            pw.print("pss,");
            pw.print(procName);
            pw.print(",");
            pw.print(uid);
            dumpAllPssCheckin(pw);
            pw.println();
        }
        if (this.mNumExcessiveCpu > 0 || this.mNumCachedKill > 0) {
            pw.print("kills,");
            pw.print(procName);
            pw.print(",");
            pw.print(uid);
            pw.print(",");
            pw.print("0");
            pw.print(",");
            pw.print(this.mNumExcessiveCpu);
            pw.print(",");
            pw.print(this.mNumCachedKill);
            pw.print(",");
            pw.print(this.mMinCachedKillPss);
            pw.print(SettingsStringUtil.DELIMITER);
            pw.print(this.mAvgCachedKillPss);
            pw.print(SettingsStringUtil.DELIMITER);
            pw.print(this.mMaxCachedKillPss);
            pw.println();
        }
    }

    public void dumpAllStateCheckin(PrintWriter pw, long now) {
        boolean didCurState = false;
        for (int i = 0; i < this.mDurations.getKeyCount(); i++) {
            int key = this.mDurations.getKeyAt(i);
            int type = SparseMappingTable.getIdFromKey(key);
            long time = this.mDurations.getValue(key);
            if (this.mCurState == type) {
                didCurState = true;
                time += now - this.mStartTime;
            }
            DumpUtils.printProcStateTagAndValue(pw, type, time);
        }
        if (!didCurState && this.mCurState != -1) {
            DumpUtils.printProcStateTagAndValue(pw, this.mCurState, now - this.mStartTime);
        }
    }

    public void dumpAllPssCheckin(PrintWriter pw) {
        int N = this.mPssTable.getKeyCount();
        for (int i = 0; i < N; i++) {
            int key = this.mPssTable.getKeyAt(i);
            int type = SparseMappingTable.getIdFromKey(key);
            pw.print(',');
            DumpUtils.printProcStateTag(pw, type);
            pw.print(':');
            pw.print(this.mPssTable.getValue(key, 0));
            pw.print(':');
            pw.print(this.mPssTable.getValue(key, 1));
            pw.print(':');
            pw.print(this.mPssTable.getValue(key, 2));
            pw.print(':');
            pw.print(this.mPssTable.getValue(key, 3));
            pw.print(':');
            pw.print(this.mPssTable.getValue(key, 4));
            pw.print(':');
            pw.print(this.mPssTable.getValue(key, 5));
            pw.print(':');
            pw.print(this.mPssTable.getValue(key, 6));
            pw.print(':');
            pw.print(this.mPssTable.getValue(key, 7));
            pw.print(':');
            pw.print(this.mPssTable.getValue(key, 8));
            pw.print(':');
            pw.print(this.mPssTable.getValue(key, 9));
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("ProcessState{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(this.mName);
        sb.append("/");
        sb.append(this.mUid);
        sb.append(" pkg=");
        sb.append(this.mPackage);
        if (this.mMultiPackage) {
            sb.append(" (multi)");
        }
        if (this.mCommonProcess != this) {
            sb.append(" (sub)");
        }
        sb.append("}");
        return sb.toString();
    }

    public void writeToProto(ProtoOutputStream proto, long fieldId, String procName, int uid, long now) {
        long j;
        int i;
        Map<Integer, Long> durationByState;
        Map<Integer, Long> durationByState2;
        long token = proto.start(fieldId);
        proto.write(1138166333441L, procName);
        proto.write(1120986464258L, uid);
        if (this.mNumExcessiveCpu > 0 || this.mNumCachedKill > 0) {
            long killToken = proto.start(1146756268035L);
            proto.write(1120986464257L, this.mNumExcessiveCpu);
            proto.write(1120986464258L, this.mNumCachedKill);
            long j2 = this.mMinCachedKillPss;
            long killToken2 = this.mAvgCachedKillPss;
            ProtoUtils.toAggStatsProto(proto, 1146756268035L, j2, killToken2, this.mMaxCachedKillPss);
            proto.end(killToken);
        }
        Map<Integer, Long> durationByState3 = new HashMap<>();
        boolean didCurState = false;
        for (int i2 = 0; i2 < this.mDurations.getKeyCount(); i2++) {
            int key = this.mDurations.getKeyAt(i2);
            int type = SparseMappingTable.getIdFromKey(key);
            long time = this.mDurations.getValue(key);
            if (this.mCurState == type) {
                durationByState2 = durationByState3;
                time += now - this.mStartTime;
                didCurState = true;
            } else {
                durationByState2 = durationByState3;
            }
            durationByState3 = durationByState2;
            durationByState3.put(Integer.valueOf(type), Long.valueOf(time));
        }
        if (!didCurState && this.mCurState != -1) {
            durationByState3.put(Integer.valueOf(this.mCurState), Long.valueOf(now - this.mStartTime));
        }
        int i3 = 0;
        while (true) {
            int i4 = i3;
            j = 2246267895813L;
            if (i4 >= this.mPssTable.getKeyCount()) {
                break;
            }
            int key2 = this.mPssTable.getKeyAt(i4);
            int type2 = SparseMappingTable.getIdFromKey(key2);
            if (!durationByState3.containsKey(Integer.valueOf(type2))) {
                i = i4;
                durationByState = durationByState3;
            } else {
                long stateToken = proto.start(2246267895813L);
                i = i4;
                DumpUtils.printProcStateTagProto(proto, 1159641169921L, 1159641169922L, 1159641169923L, type2);
                long duration = durationByState3.get(Integer.valueOf(type2)).longValue();
                durationByState3.remove(Integer.valueOf(type2));
                proto.write(1112396529668L, duration);
                proto.write(1120986464261L, this.mPssTable.getValue(key2, 0));
                durationByState = durationByState3;
                ProtoUtils.toAggStatsProto(proto, 1146756268038L, this.mPssTable.getValue(key2, 1), this.mPssTable.getValue(key2, 2), this.mPssTable.getValue(key2, 3));
                ProtoUtils.toAggStatsProto(proto, 1146756268039L, this.mPssTable.getValue(key2, 4), this.mPssTable.getValue(key2, 5), this.mPssTable.getValue(key2, 6));
                ProtoUtils.toAggStatsProto(proto, 1146756268040L, this.mPssTable.getValue(key2, 7), this.mPssTable.getValue(key2, 8), this.mPssTable.getValue(key2, 9));
                proto.end(stateToken);
            }
            i3 = i + 1;
            durationByState3 = durationByState;
        }
        for (Map.Entry<Integer, Long> entry : durationByState3.entrySet()) {
            long stateToken2 = proto.start(j);
            DumpUtils.printProcStateTagProto(proto, 1159641169921L, 1159641169922L, 1159641169923L, entry.getKey().intValue());
            proto.write(1112396529668L, entry.getValue().longValue());
            proto.end(stateToken2);
            j = j;
        }
        proto.end(token);
    }
}
