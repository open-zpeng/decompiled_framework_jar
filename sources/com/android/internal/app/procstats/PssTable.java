package com.android.internal.app.procstats;

import com.android.internal.app.procstats.SparseMappingTable;
/* loaded from: classes3.dex */
public class PssTable extends SparseMappingTable.Table {
    public PssTable(SparseMappingTable tableData) {
        super(tableData);
    }

    public void mergeStats(PssTable that) {
        int N = that.getKeyCount();
        for (int i = 0; i < N; i++) {
            int key = that.getKeyAt(i);
            int state = SparseMappingTable.getIdFromKey(key);
            mergeStats(state, (int) that.getValue(key, 0), that.getValue(key, 1), that.getValue(key, 2), that.getValue(key, 3), that.getValue(key, 4), that.getValue(key, 5), that.getValue(key, 6), that.getValue(key, 7), that.getValue(key, 8), that.getValue(key, 9));
        }
    }

    public void mergeStats(int state, int inCount, long minPss, long avgPss, long maxPss, long minUss, long avgUss, long maxUss, long minRss, long avgRss, long maxRss) {
        int key = getOrAddKey((byte) state, 10);
        long count = getValue(key, 0);
        if (count == 0) {
            setValue(key, 0, inCount);
            setValue(key, 1, minPss);
            setValue(key, 2, avgPss);
            setValue(key, 3, maxPss);
            setValue(key, 4, minUss);
            setValue(key, 5, avgUss);
            setValue(key, 6, maxUss);
            setValue(key, 7, minRss);
            setValue(key, 8, avgRss);
            setValue(key, 9, maxRss);
            return;
        }
        setValue(key, 0, inCount + count);
        long val = getValue(key, 1);
        if (val > minPss) {
            setValue(key, 1, minPss);
        }
        long val2 = getValue(key, 2);
        setValue(key, 2, (long) (((val2 * count) + (avgPss * inCount)) / (inCount + count)));
        long val3 = getValue(key, 3);
        if (val3 < maxPss) {
            setValue(key, 3, maxPss);
        }
        long val4 = getValue(key, 4);
        if (val4 > minUss) {
            setValue(key, 4, minUss);
        }
        long val5 = getValue(key, 5);
        setValue(key, 5, (long) (((val5 * count) + (avgUss * inCount)) / (inCount + count)));
        long val6 = getValue(key, 6);
        if (val6 < maxUss) {
            setValue(key, 6, maxUss);
        }
        long val7 = getValue(key, 7);
        if (val7 > minUss) {
            setValue(key, 7, minUss);
        }
        long val8 = getValue(key, 8);
        setValue(key, 8, (long) (((val8 * count) + (avgUss * inCount)) / (inCount + count)));
        long val9 = getValue(key, 9);
        if (val9 < maxUss) {
            setValue(key, 9, maxUss);
        }
    }
}
