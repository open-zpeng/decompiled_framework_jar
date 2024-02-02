package android.database.sqlite;
/* loaded from: classes.dex */
public class DatabaseObjectNotClosedException extends RuntimeException {
    private static final String s = "Application did not close the cursor or database object that was opened here";

    /* JADX INFO: Access modifiers changed from: private */
    public DatabaseObjectNotClosedException() {
        super(s);
    }
}
