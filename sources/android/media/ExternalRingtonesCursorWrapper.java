package android.media;

import android.content.ContentProvider;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;
/* loaded from: classes.dex */
public class ExternalRingtonesCursorWrapper extends CursorWrapper {
    private int mUserId;

    public synchronized ExternalRingtonesCursorWrapper(Cursor cursor, int userId) {
        super(cursor);
        this.mUserId = userId;
    }

    @Override // android.database.CursorWrapper, android.database.Cursor
    public String getString(int index) {
        String result = super.getString(index);
        if (index == 2) {
            return ContentProvider.maybeAddUserId(Uri.parse(result), this.mUserId).toString();
        }
        return result;
    }
}
