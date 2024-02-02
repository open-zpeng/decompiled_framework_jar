package com.android.internal.os;

import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Slog;
import android.util.SparseLongArray;
import com.android.internal.content.NativeLibraryHelper;
import com.android.internal.os.KernelUidCpuTimeReaderBase;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/* loaded from: classes3.dex */
public class KernelUidCpuTimeReader extends KernelUidCpuTimeReaderBase<Callback> {
    private static final String TAG = KernelUidCpuTimeReader.class.getSimpleName();
    private static final String sProcFile = "/proc/uid_cputime/show_uid_stat";
    private static final String sRemoveUidProcFile = "/proc/uid_cputime/remove_uid_range";
    private SparseLongArray mLastUserTimeUs = new SparseLongArray();
    private SparseLongArray mLastSystemTimeUs = new SparseLongArray();
    private long mLastTimeReadUs = 0;

    /* loaded from: classes3.dex */
    public interface Callback extends KernelUidCpuTimeReaderBase.Callback {
        synchronized void onUidCpuTime(int i, long j, long j2);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Found unreachable blocks
        	at jadx.core.dex.visitors.blocks.DominatorTree.sortBlocks(DominatorTree.java:35)
        	at jadx.core.dex.visitors.blocks.DominatorTree.compute(DominatorTree.java:25)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.computeDominators(BlockProcessor.java:202)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:45)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:39)
        */
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.internal.os.KernelUidCpuTimeReaderBase
    public synchronized void readDeltaImpl(com.android.internal.os.KernelUidCpuTimeReader.Callback r36) {
        /*
            Method dump skipped, instructions count: 437
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.KernelUidCpuTimeReader.readDeltaImpl(com.android.internal.os.KernelUidCpuTimeReader$Callback):void");
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

    public synchronized void readAbsolute(Callback callback) {
        BufferedReader reader;
        int oldMask = StrictMode.allowThreadDiskReadsMask();
        try {
            try {
                reader = new BufferedReader(new FileReader(sProcFile));
            } catch (IOException e) {
                String str = TAG;
                Slog.e(str, "Failed to read uid_cputime: " + e.getMessage());
            }
            try {
                TextUtils.SimpleStringSplitter splitter = new TextUtils.SimpleStringSplitter(' ');
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    splitter.setString(line);
                    String uidStr = splitter.next();
                    int uid = Integer.parseInt(uidStr.substring(0, uidStr.length() - 1), 10);
                    long userTimeUs = Long.parseLong(splitter.next(), 10);
                    long systemTimeUs = Long.parseLong(splitter.next(), 10);
                    callback.onUidCpuTime(uid, userTimeUs, systemTimeUs);
                }
                $closeResource(null, reader);
            } finally {
            }
        } finally {
            StrictMode.setThreadPolicyMask(oldMask);
        }
    }

    public synchronized void removeUid(int uid) {
        int index = this.mLastSystemTimeUs.indexOfKey(uid);
        if (index >= 0) {
            this.mLastSystemTimeUs.removeAt(index);
            this.mLastUserTimeUs.removeAt(index);
        }
        removeUidsFromKernelModule(uid, uid);
    }

    public synchronized void removeUidsInRange(int startUid, int endUid) {
        if (endUid < startUid) {
            return;
        }
        this.mLastSystemTimeUs.put(startUid, 0L);
        this.mLastUserTimeUs.put(startUid, 0L);
        this.mLastSystemTimeUs.put(endUid, 0L);
        this.mLastUserTimeUs.put(endUid, 0L);
        int startIndex = this.mLastSystemTimeUs.indexOfKey(startUid);
        int endIndex = this.mLastSystemTimeUs.indexOfKey(endUid);
        this.mLastSystemTimeUs.removeAtRange(startIndex, (endIndex - startIndex) + 1);
        this.mLastUserTimeUs.removeAtRange(startIndex, (endIndex - startIndex) + 1);
        removeUidsFromKernelModule(startUid, endUid);
    }

    private synchronized void removeUidsFromKernelModule(int startUid, int endUid) {
        String str = TAG;
        Slog.d(str, "Removing uids " + startUid + NativeLibraryHelper.CLEAR_ABI_OVERRIDE + endUid);
        int oldMask = StrictMode.allowThreadDiskWritesMask();
        try {
            try {
                FileWriter writer = new FileWriter(sRemoveUidProcFile);
                try {
                    writer.write(startUid + NativeLibraryHelper.CLEAR_ABI_OVERRIDE + endUid);
                    writer.flush();
                    $closeResource(null, writer);
                } finally {
                }
            } finally {
                StrictMode.setThreadPolicyMask(oldMask);
            }
        } catch (IOException e) {
            String str2 = TAG;
            Slog.e(str2, "failed to remove uids " + startUid + " - " + endUid + " from uid_cputime module", e);
        }
    }
}
