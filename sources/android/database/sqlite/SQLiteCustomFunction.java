package android.database.sqlite;

import android.database.sqlite.SQLiteDatabase;
/* loaded from: classes.dex */
public final class SQLiteCustomFunction {
    public final SQLiteDatabase.CustomFunction callback;
    private protected final String name;
    private protected final int numArgs;

    public synchronized SQLiteCustomFunction(String name, int numArgs, SQLiteDatabase.CustomFunction callback) {
        if (name == null) {
            throw new IllegalArgumentException("name must not be null.");
        }
        this.name = name;
        this.numArgs = numArgs;
        this.callback = callback;
    }

    public protected void dispatchCallback(String[] args) {
        this.callback.callback(args);
    }
}
