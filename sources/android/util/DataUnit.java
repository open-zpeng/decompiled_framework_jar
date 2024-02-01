package android.util;

import android.net.TrafficStats;
/* loaded from: classes2.dex */
public enum DataUnit {
    KILOBYTES { // from class: android.util.DataUnit.1
        @Override // android.util.DataUnit
        public long toBytes(long v) {
            return 1000 * v;
        }
    },
    MEGABYTES { // from class: android.util.DataUnit.2
        @Override // android.util.DataUnit
        public long toBytes(long v) {
            return 1000000 * v;
        }
    },
    GIGABYTES { // from class: android.util.DataUnit.3
        @Override // android.util.DataUnit
        public long toBytes(long v) {
            return 1000000000 * v;
        }
    },
    KIBIBYTES { // from class: android.util.DataUnit.4
        @Override // android.util.DataUnit
        public long toBytes(long v) {
            return 1024 * v;
        }
    },
    MEBIBYTES { // from class: android.util.DataUnit.5
        @Override // android.util.DataUnit
        public long toBytes(long v) {
            return 1048576 * v;
        }
    },
    GIBIBYTES { // from class: android.util.DataUnit.6
        @Override // android.util.DataUnit
        public long toBytes(long v) {
            return TrafficStats.GB_IN_BYTES * v;
        }
    };

    public synchronized long toBytes(long v) {
        throw new AbstractMethodError();
    }
}
