package com.android.internal.app.procstats;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.SystemClock;
import android.provider.SettingsStringUtil;
import android.util.Slog;
import android.util.TimeUtils;
import java.io.PrintWriter;
/* loaded from: classes3.dex */
public final class ServiceState {
    private static final boolean DEBUG = false;
    public static final int SERVICE_BOUND = 2;
    public static final int SERVICE_COUNT = 4;
    public static final int SERVICE_EXEC = 3;
    public static final int SERVICE_RUN = 0;
    public static final int SERVICE_STARTED = 1;
    private static final String TAG = "ProcessStats";
    private int mBoundCount;
    private long mBoundStartTime;
    private final DurationsTable mDurations;
    private int mExecCount;
    private long mExecStartTime;
    private final String mName;
    private Object mOwner;
    private final String mPackage;
    private ProcessState mProc;
    private final String mProcessName;
    private boolean mRestarting;
    private int mRunCount;
    private long mRunStartTime;
    private boolean mStarted;
    private int mStartedCount;
    private long mStartedStartTime;
    private int mRunState = -1;
    private int mStartedState = -1;
    private int mBoundState = -1;
    private int mExecState = -1;

    public ServiceState(ProcessStats processStats, String pkg, String name, String processName, ProcessState proc) {
        this.mPackage = pkg;
        this.mName = name;
        this.mProcessName = processName;
        this.mProc = proc;
        this.mDurations = new DurationsTable(processStats.mTableData);
    }

    public String getPackage() {
        return this.mPackage;
    }

    public String getProcessName() {
        return this.mProcessName;
    }

    public String getName() {
        return this.mName;
    }

    public ProcessState getProcess() {
        return this.mProc;
    }

    public void setProcess(ProcessState proc) {
        this.mProc = proc;
    }

    public void setMemFactor(int memFactor, long now) {
        if (isRestarting()) {
            setRestarting(true, memFactor, now);
        } else if (isInUse()) {
            if (this.mStartedState != -1) {
                setStarted(true, memFactor, now);
            }
            if (this.mBoundState != -1) {
                setBound(true, memFactor, now);
            }
            if (this.mExecState != -1) {
                setExecuting(true, memFactor, now);
            }
        }
    }

    public void applyNewOwner(Object newOwner) {
        if (this.mOwner != newOwner) {
            if (this.mOwner == null) {
                this.mOwner = newOwner;
                this.mProc.incActiveServices(this.mName);
                return;
            }
            this.mOwner = newOwner;
            if (this.mStarted || this.mBoundState != -1 || this.mExecState != -1) {
                long now = SystemClock.uptimeMillis();
                if (this.mStarted) {
                    setStarted(false, 0, now);
                }
                if (this.mBoundState != -1) {
                    setBound(false, 0, now);
                }
                if (this.mExecState != -1) {
                    setExecuting(false, 0, now);
                }
            }
        }
    }

    public void clearCurrentOwner(Object owner, boolean silently) {
        if (this.mOwner == owner) {
            this.mProc.decActiveServices(this.mName);
            if (this.mStarted || this.mBoundState != -1 || this.mExecState != -1) {
                long now = SystemClock.uptimeMillis();
                if (this.mStarted) {
                    if (!silently) {
                        Slog.wtfStack("ProcessStats", "Service owner " + owner + " cleared while started: pkg=" + this.mPackage + " service=" + this.mName + " proc=" + this.mProc);
                    }
                    setStarted(false, 0, now);
                }
                if (this.mBoundState != -1) {
                    if (!silently) {
                        Slog.wtfStack("ProcessStats", "Service owner " + owner + " cleared while bound: pkg=" + this.mPackage + " service=" + this.mName + " proc=" + this.mProc);
                    }
                    setBound(false, 0, now);
                }
                if (this.mExecState != -1) {
                    if (!silently) {
                        Slog.wtfStack("ProcessStats", "Service owner " + owner + " cleared while exec: pkg=" + this.mPackage + " service=" + this.mName + " proc=" + this.mProc);
                    }
                    setExecuting(false, 0, now);
                }
            }
            this.mOwner = null;
        }
    }

    public boolean isInUse() {
        return this.mOwner != null || this.mRestarting;
    }

    public boolean isRestarting() {
        return this.mRestarting;
    }

    public void add(ServiceState other) {
        this.mDurations.addDurations(other.mDurations);
        this.mRunCount += other.mRunCount;
        this.mStartedCount += other.mStartedCount;
        this.mBoundCount += other.mBoundCount;
        this.mExecCount += other.mExecCount;
    }

    public void resetSafely(long now) {
        this.mDurations.resetTable();
        this.mRunCount = this.mRunState != -1 ? 1 : 0;
        this.mStartedCount = this.mStartedState != -1 ? 1 : 0;
        this.mBoundCount = this.mBoundState != -1 ? 1 : 0;
        this.mExecCount = this.mExecState != -1 ? 1 : 0;
        this.mExecStartTime = now;
        this.mBoundStartTime = now;
        this.mStartedStartTime = now;
        this.mRunStartTime = now;
    }

    public void writeToParcel(Parcel out, long now) {
        this.mDurations.writeToParcel(out);
        out.writeInt(this.mRunCount);
        out.writeInt(this.mStartedCount);
        out.writeInt(this.mBoundCount);
        out.writeInt(this.mExecCount);
    }

    public boolean readFromParcel(Parcel in) {
        if (!this.mDurations.readFromParcel(in)) {
            return false;
        }
        this.mRunCount = in.readInt();
        this.mStartedCount = in.readInt();
        this.mBoundCount = in.readInt();
        this.mExecCount = in.readInt();
        return true;
    }

    public void commitStateTime(long now) {
        if (this.mRunState != -1) {
            this.mDurations.addDuration(0 + (this.mRunState * 4), now - this.mRunStartTime);
            this.mRunStartTime = now;
        }
        if (this.mStartedState != -1) {
            this.mDurations.addDuration(1 + (this.mStartedState * 4), now - this.mStartedStartTime);
            this.mStartedStartTime = now;
        }
        if (this.mBoundState != -1) {
            this.mDurations.addDuration(2 + (this.mBoundState * 4), now - this.mBoundStartTime);
            this.mBoundStartTime = now;
        }
        if (this.mExecState != -1) {
            this.mDurations.addDuration(3 + (this.mExecState * 4), now - this.mExecStartTime);
            this.mExecStartTime = now;
        }
    }

    private void updateRunning(int memFactor, long now) {
        int state = (this.mStartedState == -1 && this.mBoundState == -1 && this.mExecState == -1) ? -1 : memFactor;
        if (this.mRunState != state) {
            if (this.mRunState != -1) {
                this.mDurations.addDuration(0 + (this.mRunState * 4), now - this.mRunStartTime);
            } else if (state != -1) {
                this.mRunCount++;
            }
            this.mRunState = state;
            this.mRunStartTime = now;
        }
    }

    public void setStarted(boolean started, int memFactor, long now) {
        if (this.mOwner == null) {
            Slog.wtf("ProcessStats", "Starting service " + this + " without owner");
        }
        this.mStarted = started;
        updateStartedState(memFactor, now);
    }

    public void setRestarting(boolean restarting, int memFactor, long now) {
        this.mRestarting = restarting;
        updateStartedState(memFactor, now);
    }

    public void updateStartedState(int memFactor, long now) {
        boolean started = false;
        boolean wasStarted = this.mStartedState != -1;
        if (this.mStarted || this.mRestarting) {
            started = true;
        }
        int state = started ? memFactor : -1;
        if (this.mStartedState != state) {
            if (this.mStartedState != -1) {
                this.mDurations.addDuration(1 + (this.mStartedState * 4), now - this.mStartedStartTime);
            } else if (started) {
                this.mStartedCount++;
            }
            this.mStartedState = state;
            this.mStartedStartTime = now;
            this.mProc = this.mProc.pullFixedProc(this.mPackage);
            if (wasStarted != started) {
                if (started) {
                    this.mProc.incStartedServices(memFactor, now, this.mName);
                } else {
                    this.mProc.decStartedServices(memFactor, now, this.mName);
                }
            }
            updateRunning(memFactor, now);
        }
    }

    public void setBound(boolean bound, int memFactor, long now) {
        if (this.mOwner == null) {
            Slog.wtf("ProcessStats", "Binding service " + this + " without owner");
        }
        int state = bound ? memFactor : -1;
        if (this.mBoundState != state) {
            if (this.mBoundState != -1) {
                this.mDurations.addDuration(2 + (this.mBoundState * 4), now - this.mBoundStartTime);
            } else if (bound) {
                this.mBoundCount++;
            }
            this.mBoundState = state;
            this.mBoundStartTime = now;
            updateRunning(memFactor, now);
        }
    }

    public void setExecuting(boolean executing, int memFactor, long now) {
        if (this.mOwner == null) {
            Slog.wtf("ProcessStats", "Executing service " + this + " without owner");
        }
        int state = executing ? memFactor : -1;
        if (this.mExecState != state) {
            if (this.mExecState != -1) {
                this.mDurations.addDuration(3 + (this.mExecState * 4), now - this.mExecStartTime);
            } else if (executing) {
                this.mExecCount++;
            }
            this.mExecState = state;
            this.mExecStartTime = now;
            updateRunning(memFactor, now);
        }
    }

    public long getDuration(int opType, int curState, long startTime, int memFactor, long now) {
        int state = (memFactor * 4) + opType;
        long time = this.mDurations.getValueForId((byte) state);
        if (curState == memFactor) {
            return time + (now - startTime);
        }
        return time;
    }

    public void dumpStats(PrintWriter pw, String prefix, String prefixInner, String headerPrefix, long now, long totalTime, boolean dumpSummary, boolean dumpAll) {
        PrintWriter printWriter;
        boolean z = false;
        dumpStats(pw, prefix, prefixInner, headerPrefix, "Running", this.mRunCount, 0, this.mRunState, this.mRunStartTime, now, totalTime, !dumpSummary || dumpAll);
        dumpStats(pw, prefix, prefixInner, headerPrefix, "Started", this.mStartedCount, 1, this.mStartedState, this.mStartedStartTime, now, totalTime, !dumpSummary || dumpAll);
        dumpStats(pw, prefix, prefixInner, headerPrefix, "Bound", this.mBoundCount, 2, this.mBoundState, this.mBoundStartTime, now, totalTime, !dumpSummary || dumpAll);
        int i = this.mExecCount;
        int i2 = this.mExecState;
        long j = this.mExecStartTime;
        if (!dumpSummary || dumpAll) {
            z = true;
        }
        dumpStats(pw, prefix, prefixInner, headerPrefix, "Executing", i, 3, i2, j, now, totalTime, z);
        if (dumpAll) {
            if (this.mOwner != null) {
                printWriter = pw;
                printWriter.print("        mOwner=");
                printWriter.println(this.mOwner);
            } else {
                printWriter = pw;
            }
            if (this.mStarted || this.mRestarting) {
                printWriter.print("        mStarted=");
                printWriter.print(this.mStarted);
                printWriter.print(" mRestarting=");
                printWriter.println(this.mRestarting);
            }
        }
    }

    private void dumpStats(PrintWriter pw, String prefix, String prefixInner, String headerPrefix, String header, int count, int serviceType, int state, long startTime, long now, long totalTime, boolean dumpAll) {
        if (count != 0) {
            if (dumpAll) {
                pw.print(prefix);
                pw.print(header);
                pw.print(" op count ");
                pw.print(count);
                pw.println(SettingsStringUtil.DELIMITER);
                dumpTime(pw, prefixInner, serviceType, state, startTime, now);
            } else {
                long myTime = dumpTime(null, null, serviceType, state, startTime, now);
                pw.print(prefix);
                pw.print(headerPrefix);
                pw.print(header);
                pw.print(" count ");
                pw.print(count);
                pw.print(" / time ");
                DumpUtils.printPercent(pw, myTime / totalTime);
                pw.println();
            }
        }
    }

    public long dumpTime(PrintWriter pw, String prefix, int serviceType, int curState, long curStartTime, long now) {
        int printedScreen = -1;
        long totalTime = 0;
        int iscreen = 0;
        while (iscreen < 8) {
            int printedMem = -1;
            long totalTime2 = totalTime;
            int imem = 0;
            while (imem < 4) {
                int state = imem + iscreen;
                long time = getDuration(serviceType, curState, curStartTime, state, now);
                String running = "";
                if (curState == state && pw != null) {
                    running = " (running)";
                }
                if (time != 0) {
                    if (pw != null) {
                        pw.print(prefix);
                        DumpUtils.printScreenLabel(pw, printedScreen != iscreen ? iscreen : -1);
                        printedScreen = iscreen;
                        DumpUtils.printMemLabel(pw, printedMem != imem ? imem : -1, (char) 0);
                        printedMem = imem;
                        pw.print(": ");
                        TimeUtils.formatDuration(time, pw);
                        pw.println(running);
                    }
                    totalTime2 += time;
                }
                imem++;
            }
            iscreen += 4;
            totalTime = totalTime2;
        }
        if (totalTime != 0 && pw != null) {
            pw.print(prefix);
            pw.print("    TOTAL: ");
            TimeUtils.formatDuration(totalTime, pw);
            pw.println();
        }
        return totalTime;
    }

    public void dumpTimesCheckin(PrintWriter pw, String pkgName, int uid, long vers, String serviceName, long now) {
        dumpTimeCheckin(pw, "pkgsvc-run", pkgName, uid, vers, serviceName, 0, this.mRunCount, this.mRunState, this.mRunStartTime, now);
        dumpTimeCheckin(pw, "pkgsvc-start", pkgName, uid, vers, serviceName, 1, this.mStartedCount, this.mStartedState, this.mStartedStartTime, now);
        dumpTimeCheckin(pw, "pkgsvc-bound", pkgName, uid, vers, serviceName, 2, this.mBoundCount, this.mBoundState, this.mBoundStartTime, now);
        dumpTimeCheckin(pw, "pkgsvc-exec", pkgName, uid, vers, serviceName, 3, this.mExecCount, this.mExecState, this.mExecStartTime, now);
    }

    private void dumpTimeCheckin(PrintWriter pw, String label, String packageName, int uid, long vers, String serviceName, int serviceType, int opCount, int curState, long curStartTime, long now) {
        ServiceState serviceState = this;
        if (opCount <= 0) {
            return;
        }
        pw.print(label);
        pw.print(",");
        pw.print(packageName);
        pw.print(",");
        pw.print(uid);
        pw.print(",");
        pw.print(vers);
        pw.print(",");
        pw.print(serviceName);
        pw.print(",");
        pw.print(opCount);
        boolean didCurState = false;
        int N = serviceState.mDurations.getKeyCount();
        int i = 0;
        while (i < N) {
            int key = serviceState.mDurations.getKeyAt(i);
            long time = serviceState.mDurations.getValue(key);
            int type = SparseMappingTable.getIdFromKey(key);
            int memFactor = type / 4;
            if (type % 4 == serviceType) {
                if (curState == memFactor) {
                    didCurState = true;
                    time += now - curStartTime;
                }
                DumpUtils.printAdjTagAndValue(pw, memFactor, time);
            }
            i++;
            serviceState = this;
        }
        if (!didCurState && curState != -1) {
            DumpUtils.printAdjTagAndValue(pw, curState, now - curStartTime);
        }
        pw.println();
    }

    public String toString() {
        return "ServiceState{" + Integer.toHexString(System.identityHashCode(this)) + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + this.mName + " pkg=" + this.mPackage + " proc=" + Integer.toHexString(System.identityHashCode(this)) + "}";
    }
}
