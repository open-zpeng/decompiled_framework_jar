package android.database.sqlite;

import android.annotation.UnsupportedAppUsage;

/* loaded from: classes.dex */
public class DatabaseObjectNotClosedException extends RuntimeException {
    private static final String s = "Application did not close the cursor or database object that was opened here";

    @UnsupportedAppUsage
    public DatabaseObjectNotClosedException() {
        super(s);
    }
}
