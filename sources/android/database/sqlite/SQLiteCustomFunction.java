package android.database.sqlite;

import android.annotation.UnsupportedAppUsage;
import android.database.sqlite.SQLiteDatabase;

/* loaded from: classes.dex */
public final class SQLiteCustomFunction {
    public final SQLiteDatabase.CustomFunction callback;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public final String name;
    @UnsupportedAppUsage
    public final int numArgs;

    public SQLiteCustomFunction(String name, int numArgs, SQLiteDatabase.CustomFunction callback) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null.");
        }
        this.name = name;
        this.numArgs = numArgs;
        this.callback = callback;
    }

    @UnsupportedAppUsage
    private void dispatchCallback(String[] args) {
        this.callback.callback(args);
    }
}
