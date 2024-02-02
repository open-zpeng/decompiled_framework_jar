package com.android.internal.app.procstats;

import android.util.DebugUtils;
import com.android.internal.app.procstats.SparseMappingTable;
import java.io.PrintWriter;
/* loaded from: classes3.dex */
public class SysMemUsageTable extends SparseMappingTable.Table {
    public SysMemUsageTable(SparseMappingTable tableData) {
        super(tableData);
    }

    public void mergeStats(SysMemUsageTable that) {
        int N = that.getKeyCount();
        for (int i = 0; i < N; i++) {
            int key = that.getKeyAt(i);
            int state = SparseMappingTable.getIdFromKey(key);
            long[] addData = that.getArrayForKey(key);
            int addOff = SparseMappingTable.getIndexFromKey(key);
            mergeStats(state, addData, addOff);
        }
    }

    public void mergeStats(int state, long[] addData, int addOff) {
        int key = getOrAddKey((byte) state, 16);
        long[] dstData = getArrayForKey(key);
        int dstOff = SparseMappingTable.getIndexFromKey(key);
        mergeSysMemUsage(dstData, dstOff, addData, addOff);
    }

    public long[] getTotalMemUsage() {
        long[] total = new long[16];
        int N = getKeyCount();
        for (int i = 0; i < N; i++) {
            int key = getKeyAt(i);
            long[] addData = getArrayForKey(key);
            int addOff = SparseMappingTable.getIndexFromKey(key);
            mergeSysMemUsage(total, 0, addData, addOff);
        }
        return total;
    }

    public static void mergeSysMemUsage(long[] dstData, int dstOff, long[] addData, int addOff) {
        long dstCount = dstData[dstOff + 0];
        long addCount = addData[addOff + 0];
        int i = 16;
        int i2 = 1;
        if (dstCount == 0) {
            dstData[dstOff + 0] = addCount;
            while (true) {
                int i3 = i2;
                if (i3 < 16) {
                    dstData[dstOff + i3] = addData[addOff + i3];
                    i2 = i3 + 1;
                } else {
                    return;
                }
            }
        } else if (addCount > 0) {
            dstData[dstOff + 0] = dstCount + addCount;
            int i4 = 1;
            while (i4 < i) {
                if (dstData[dstOff + i4] > addData[addOff + i4]) {
                    dstData[dstOff + i4] = addData[addOff + i4];
                }
                dstData[dstOff + i4 + i2] = (long) (((dstData[(dstOff + i4) + i2] * dstCount) + (addData[(addOff + i4) + 1] * addCount)) / (dstCount + addCount));
                if (dstData[dstOff + i4 + 2] < addData[addOff + i4 + 2]) {
                    dstData[dstOff + i4 + 2] = addData[addOff + i4 + 2];
                }
                i4 += 3;
                i = 16;
                i2 = 1;
            }
        }
    }

    public void dump(PrintWriter pw, String prefix, int[] screenStates, int[] memStates) {
        int printedScreen;
        int printedScreen2 = -1;
        int printedScreen3 = 0;
        while (true) {
            int is = printedScreen3;
            if (is >= screenStates.length) {
                return;
            }
            int printedScreen4 = printedScreen2;
            int printedMem = -1;
            int printedMem2 = 0;
            while (true) {
                int im = printedMem2;
                if (im < memStates.length) {
                    int iscreen = screenStates[is];
                    int imem = memStates[im];
                    int bucket = (iscreen + imem) * 14;
                    long count = getValueForId((byte) bucket, 0);
                    if (count > 0) {
                        pw.print(prefix);
                        if (screenStates.length > 1) {
                            DumpUtils.printScreenLabel(pw, printedScreen4 != iscreen ? iscreen : -1);
                            printedScreen = iscreen;
                        } else {
                            printedScreen = printedScreen4;
                        }
                        if (memStates.length > 1) {
                            DumpUtils.printMemLabel(pw, printedMem != imem ? imem : -1, (char) 0);
                            printedMem = imem;
                        }
                        pw.print(": ");
                        pw.print(count);
                        pw.println(" samples:");
                        dumpCategory(pw, prefix, "  Cached", bucket, 1);
                        dumpCategory(pw, prefix, "  Free", bucket, 4);
                        dumpCategory(pw, prefix, "  ZRam", bucket, 7);
                        dumpCategory(pw, prefix, "  Kernel", bucket, 10);
                        dumpCategory(pw, prefix, "  Native", bucket, 13);
                        printedScreen4 = printedScreen;
                        printedMem = printedMem;
                    }
                    printedMem2 = im + 1;
                }
            }
            printedScreen3 = is + 1;
            printedScreen2 = printedScreen4;
        }
    }

    private void dumpCategory(PrintWriter pw, String prefix, String label, int bucket, int index) {
        pw.print(prefix);
        pw.print(label);
        pw.print(": ");
        DebugUtils.printSizeValue(pw, getValueForId((byte) bucket, index) * 1024);
        pw.print(" min, ");
        DebugUtils.printSizeValue(pw, getValueForId((byte) bucket, index + 1) * 1024);
        pw.print(" avg, ");
        DebugUtils.printSizeValue(pw, getValueForId((byte) bucket, index + 2) * 1024);
        pw.println(" max");
    }
}
