package com.android.internal.database;

import android.database.AbstractCursor;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.util.Log;
import java.lang.reflect.Array;
/* loaded from: classes3.dex */
public class SortCursor extends AbstractCursor {
    private static final String TAG = "SortCursor";
    private int[][] mCurRowNumCache;
    private Cursor mCursor;
    private Cursor[] mCursors;
    private int[] mSortColumns;
    private final int ROWCACHESIZE = 64;
    private int[] mRowNumCache = new int[64];
    private int[] mCursorCache = new int[64];
    private int mLastCacheHit = -1;
    private DataSetObserver mObserver = new DataSetObserver() { // from class: com.android.internal.database.SortCursor.1
        @Override // android.database.DataSetObserver
        public void onChanged() {
            SortCursor.this.mPos = -1;
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            SortCursor.this.mPos = -1;
        }
    };

    public SortCursor(Cursor[] cursors, String sortcolumn) {
        this.mCursors = cursors;
        int length = this.mCursors.length;
        this.mSortColumns = new int[length];
        for (int i = 0; i < length; i++) {
            if (this.mCursors[i] != null) {
                this.mCursors[i].registerDataSetObserver(this.mObserver);
                this.mCursors[i].moveToFirst();
                this.mSortColumns[i] = this.mCursors[i].getColumnIndexOrThrow(sortcolumn);
            }
        }
        this.mCursor = null;
        String smallest = "";
        for (int j = 0; j < length; j++) {
            if (this.mCursors[j] != null && !this.mCursors[j].isAfterLast()) {
                String current = this.mCursors[j].getString(this.mSortColumns[j]);
                if (this.mCursor == null || current.compareToIgnoreCase(smallest) < 0) {
                    smallest = current;
                    this.mCursor = this.mCursors[j];
                }
            }
        }
        for (int i2 = this.mRowNumCache.length - 1; i2 >= 0; i2--) {
            this.mRowNumCache[i2] = -2;
        }
        this.mCurRowNumCache = (int[][]) Array.newInstance(int.class, 64, length);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public int getCount() {
        int count = 0;
        int length = this.mCursors.length;
        for (int i = 0; i < length; i++) {
            if (this.mCursors[i] != null) {
                count += this.mCursors[i].getCount();
            }
        }
        return count;
    }

    @Override // android.database.AbstractCursor, android.database.CrossProcessCursor
    public boolean onMove(int oldPosition, int newPosition) {
        if (oldPosition == newPosition) {
            return true;
        }
        int cache_entry = newPosition % 64;
        if (this.mRowNumCache[cache_entry] == newPosition) {
            int which = this.mCursorCache[cache_entry];
            this.mCursor = this.mCursors[which];
            if (this.mCursor == null) {
                Log.w(TAG, "onMove: cache results in a null cursor.");
                return false;
            }
            this.mCursor.moveToPosition(this.mCurRowNumCache[cache_entry][which]);
            this.mLastCacheHit = cache_entry;
            return true;
        }
        this.mCursor = null;
        int length = this.mCursors.length;
        if (this.mLastCacheHit >= 0) {
            for (int i = 0; i < length; i++) {
                if (this.mCursors[i] != null) {
                    this.mCursors[i].moveToPosition(this.mCurRowNumCache[this.mLastCacheHit][i]);
                }
            }
        }
        if (newPosition < oldPosition || oldPosition == -1) {
            for (int i2 = 0; i2 < length; i2++) {
                if (this.mCursors[i2] != null) {
                    this.mCursors[i2].moveToFirst();
                }
            }
            oldPosition = 0;
        }
        if (oldPosition < 0) {
            oldPosition = 0;
        }
        int smallestIdx = -1;
        int smallestIdx2 = oldPosition;
        while (true) {
            if (smallestIdx2 > newPosition) {
                break;
            }
            String smallest = "";
            int smallestIdx3 = -1;
            for (int smallestIdx4 = 0; smallestIdx4 < length; smallestIdx4++) {
                if (this.mCursors[smallestIdx4] != null && !this.mCursors[smallestIdx4].isAfterLast()) {
                    String current = this.mCursors[smallestIdx4].getString(this.mSortColumns[smallestIdx4]);
                    if (smallestIdx3 < 0 || current.compareToIgnoreCase(smallest) < 0) {
                        smallest = current;
                        smallestIdx3 = smallestIdx4;
                    }
                }
            }
            if (smallestIdx2 != newPosition) {
                if (this.mCursors[smallestIdx3] != null) {
                    this.mCursors[smallestIdx3].moveToNext();
                }
                smallestIdx2++;
                smallestIdx = smallestIdx3;
            } else {
                smallestIdx = smallestIdx3;
                break;
            }
        }
        this.mCursor = this.mCursors[smallestIdx];
        this.mRowNumCache[cache_entry] = newPosition;
        this.mCursorCache[cache_entry] = smallestIdx;
        for (int i3 = 0; i3 < length; i3++) {
            if (this.mCursors[i3] != null) {
                this.mCurRowNumCache[cache_entry][i3] = this.mCursors[i3].getPosition();
            }
        }
        this.mLastCacheHit = -1;
        return true;
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public String getString(int column) {
        return this.mCursor.getString(column);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public short getShort(int column) {
        return this.mCursor.getShort(column);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public int getInt(int column) {
        return this.mCursor.getInt(column);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public long getLong(int column) {
        return this.mCursor.getLong(column);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public float getFloat(int column) {
        return this.mCursor.getFloat(column);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public double getDouble(int column) {
        return this.mCursor.getDouble(column);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public int getType(int column) {
        return this.mCursor.getType(column);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public boolean isNull(int column) {
        return this.mCursor.isNull(column);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public byte[] getBlob(int column) {
        return this.mCursor.getBlob(column);
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public String[] getColumnNames() {
        if (this.mCursor != null) {
            return this.mCursor.getColumnNames();
        }
        int length = this.mCursors.length;
        for (int i = 0; i < length; i++) {
            if (this.mCursors[i] != null) {
                return this.mCursors[i].getColumnNames();
            }
        }
        throw new IllegalStateException("No cursor that can return names");
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public void deactivate() {
        int length = this.mCursors.length;
        for (int i = 0; i < length; i++) {
            if (this.mCursors[i] != null) {
                this.mCursors[i].deactivate();
            }
        }
    }

    @Override // android.database.AbstractCursor, android.database.Cursor, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        int length = this.mCursors.length;
        for (int i = 0; i < length; i++) {
            if (this.mCursors[i] != null) {
                this.mCursors[i].close();
            }
        }
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public void registerDataSetObserver(DataSetObserver observer) {
        int length = this.mCursors.length;
        for (int i = 0; i < length; i++) {
            if (this.mCursors[i] != null) {
                this.mCursors[i].registerDataSetObserver(observer);
            }
        }
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public void unregisterDataSetObserver(DataSetObserver observer) {
        int length = this.mCursors.length;
        for (int i = 0; i < length; i++) {
            if (this.mCursors[i] != null) {
                this.mCursors[i].unregisterDataSetObserver(observer);
            }
        }
    }

    @Override // android.database.AbstractCursor, android.database.Cursor
    public boolean requery() {
        int length = this.mCursors.length;
        for (int i = 0; i < length; i++) {
            if (this.mCursors[i] != null && !this.mCursors[i].requery()) {
                return false;
            }
        }
        return true;
    }
}
