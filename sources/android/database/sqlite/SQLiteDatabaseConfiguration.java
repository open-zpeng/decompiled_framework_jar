package android.database.sqlite;

import android.annotation.UnsupportedAppUsage;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public final class SQLiteDatabaseConfiguration {
    private static final Pattern EMAIL_IN_DB_PATTERN = Pattern.compile("[\\w\\.\\-]+@[\\w\\.\\-]+");
    public static final String MEMORY_DB_PATH = ":memory:";
    public boolean foreignKeyConstraintsEnabled;
    public String journalMode;
    public final String label;
    public Locale locale;
    @UnsupportedAppUsage
    public int maxSqlCacheSize;
    public int openFlags;
    public final String path;
    public String syncMode;
    public final ArrayList<SQLiteCustomFunction> customFunctions = new ArrayList<>();
    public int lookasideSlotSize = -1;
    public int lookasideSlotCount = -1;
    public long idleConnectionTimeoutMs = Long.MAX_VALUE;

    public SQLiteDatabaseConfiguration(String path, int openFlags) {
        if (path == null) {
            throw new IllegalArgumentException("path must not be null.");
        }
        this.path = path;
        this.label = stripPathForLogs(path);
        this.openFlags = openFlags;
        this.maxSqlCacheSize = 25;
        this.locale = Locale.getDefault();
    }

    public SQLiteDatabaseConfiguration(SQLiteDatabaseConfiguration other) {
        if (other == null) {
            throw new IllegalArgumentException("other must not be null.");
        }
        this.path = other.path;
        this.label = other.label;
        updateParametersFrom(other);
    }

    public void updateParametersFrom(SQLiteDatabaseConfiguration other) {
        if (other == null) {
            throw new IllegalArgumentException("other must not be null.");
        }
        if (!this.path.equals(other.path)) {
            throw new IllegalArgumentException("other configuration must refer to the same database.");
        }
        this.openFlags = other.openFlags;
        this.maxSqlCacheSize = other.maxSqlCacheSize;
        this.locale = other.locale;
        this.foreignKeyConstraintsEnabled = other.foreignKeyConstraintsEnabled;
        this.customFunctions.clear();
        this.customFunctions.addAll(other.customFunctions);
        this.lookasideSlotSize = other.lookasideSlotSize;
        this.lookasideSlotCount = other.lookasideSlotCount;
        this.idleConnectionTimeoutMs = other.idleConnectionTimeoutMs;
        this.journalMode = other.journalMode;
        this.syncMode = other.syncMode;
    }

    public boolean isInMemoryDb() {
        return this.path.equalsIgnoreCase(MEMORY_DB_PATH);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isLegacyCompatibilityWalEnabled() {
        return this.journalMode == null && this.syncMode == null && (this.openFlags & Integer.MIN_VALUE) != 0;
    }

    private static String stripPathForLogs(String path) {
        if (path.indexOf(64) == -1) {
            return path;
        }
        return EMAIL_IN_DB_PATTERN.matcher(path).replaceAll("XX@YY");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isLookasideConfigSet() {
        return this.lookasideSlotCount >= 0 && this.lookasideSlotSize >= 0;
    }
}
