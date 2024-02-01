package android.widget;

import android.database.Cursor;
import android.widget.Filter;
/* loaded from: classes3.dex */
class CursorFilter extends Filter {
    CursorFilterClient mClient;

    /* loaded from: classes3.dex */
    interface CursorFilterClient {
        synchronized void changeCursor(Cursor cursor);

        synchronized CharSequence convertToString(Cursor cursor);

        synchronized Cursor getCursor();

        synchronized Cursor runQueryOnBackgroundThread(CharSequence charSequence);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized CursorFilter(CursorFilterClient client) {
        this.mClient = client;
    }

    @Override // android.widget.Filter
    public CharSequence convertResultToString(Object resultValue) {
        return this.mClient.convertToString((Cursor) resultValue);
    }

    @Override // android.widget.Filter
    protected Filter.FilterResults performFiltering(CharSequence constraint) {
        Cursor cursor = this.mClient.runQueryOnBackgroundThread(constraint);
        Filter.FilterResults results = new Filter.FilterResults();
        if (cursor != null) {
            results.count = cursor.getCount();
            results.values = cursor;
        } else {
            results.count = 0;
            results.values = null;
        }
        return results;
    }

    @Override // android.widget.Filter
    protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
        Cursor oldCursor = this.mClient.getCursor();
        if (results.values != null && results.values != oldCursor) {
            this.mClient.changeCursor((Cursor) results.values);
        }
    }
}
