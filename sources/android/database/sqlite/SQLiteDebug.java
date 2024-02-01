package android.database.sqlite;

import android.os.Build;
import android.os.SystemProperties;
import android.util.Log;
import android.util.Printer;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class SQLiteDebug {
    public static final boolean DEBUG_SQL_LOG = Log.isLoggable("SQLiteLog", 2);
    public static final boolean DEBUG_SQL_STATEMENTS = Log.isLoggable("SQLiteStatements", 2);
    public static final boolean DEBUG_SQL_TIME = Log.isLoggable("SQLiteTime", 2);
    public static final boolean DEBUG_LOG_SLOW_QUERIES = Build.IS_DEBUGGABLE;

    /* loaded from: classes.dex */
    public static class PagerStats {
        public ArrayList<DbStats> dbStats;
        private protected int largestMemAlloc;
        private protected int memoryUsed;
        private protected int pageCacheOverflow;
    }

    private static native void nativeGetPagerStats(PagerStats pagerStats);

    private synchronized SQLiteDebug() {
    }

    public static final synchronized boolean shouldLogSlowQuery(long elapsedTimeMillis) {
        int slowQueryMillis = SystemProperties.getInt("db.log.slow_query_threshold", -1);
        return slowQueryMillis >= 0 && elapsedTimeMillis >= ((long) slowQueryMillis);
    }

    /* loaded from: classes.dex */
    public static class DbStats {
        public String cache;
        public String dbName;
        public long dbSize;
        public int lookaside;
        public long pageSize;

        public synchronized DbStats(String dbName, long pageCount, long pageSize, int lookaside, int hits, int misses, int cachesize) {
            this.dbName = dbName;
            this.pageSize = pageSize / 1024;
            this.dbSize = (pageCount * pageSize) / 1024;
            this.lookaside = lookaside;
            this.cache = hits + "/" + misses + "/" + cachesize;
        }
    }

    public static synchronized PagerStats getDatabaseInfo() {
        PagerStats stats = new PagerStats();
        nativeGetPagerStats(stats);
        stats.dbStats = SQLiteDatabase.getDbStats();
        return stats;
    }

    public static synchronized void dump(Printer printer, String[] args) {
        boolean verbose = false;
        for (String arg : args) {
            if (arg.equals("-v")) {
                verbose = true;
            }
        }
        SQLiteDatabase.dumpAll(printer, verbose);
    }
}
