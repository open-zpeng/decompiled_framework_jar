package android.database;

import android.annotation.UnsupportedAppUsage;
import android.content.res.Resources;
import android.database.sqlite.SQLiteClosable;
import android.os.Binder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.SparseIntArray;
import com.android.internal.R;
import dalvik.annotation.optimization.FastNative;
import dalvik.system.CloseGuard;

/* loaded from: classes.dex */
public class CursorWindow extends SQLiteClosable implements Parcelable {
    private static final String STATS_TAG = "CursorWindowStats";
    private final CloseGuard mCloseGuard;
    private final String mName;
    private int mStartPos;
    @UnsupportedAppUsage
    public long mWindowPtr;
    @UnsupportedAppUsage
    private static int sCursorWindowSize = -1;
    public static final Parcelable.Creator<CursorWindow> CREATOR = new Parcelable.Creator<CursorWindow>() { // from class: android.database.CursorWindow.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CursorWindow createFromParcel(Parcel source) {
            return new CursorWindow(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CursorWindow[] newArray(int size) {
            return new CursorWindow[size];
        }
    };
    @UnsupportedAppUsage
    private static final LongSparseArray<Integer> sWindowToPidMap = new LongSparseArray<>();

    @FastNative
    private static native boolean nativeAllocRow(long j);

    @FastNative
    private static native void nativeClear(long j);

    private static native void nativeCopyStringToBuffer(long j, int i, int i2, CharArrayBuffer charArrayBuffer);

    private static native long nativeCreate(String str, int i);

    private static native long nativeCreateFromParcel(Parcel parcel);

    private static native void nativeDispose(long j);

    @FastNative
    private static native void nativeFreeLastRow(long j);

    private static native byte[] nativeGetBlob(long j, int i, int i2);

    @FastNative
    private static native double nativeGetDouble(long j, int i, int i2);

    @FastNative
    private static native long nativeGetLong(long j, int i, int i2);

    private static native String nativeGetName(long j);

    @FastNative
    private static native int nativeGetNumRows(long j);

    private static native String nativeGetString(long j, int i, int i2);

    @FastNative
    private static native int nativeGetType(long j, int i, int i2);

    private static native boolean nativePutBlob(long j, byte[] bArr, int i, int i2);

    @FastNative
    private static native boolean nativePutDouble(long j, double d, int i, int i2);

    @FastNative
    private static native boolean nativePutLong(long j, long j2, int i, int i2);

    @FastNative
    private static native boolean nativePutNull(long j, int i, int i2);

    private static native boolean nativePutString(long j, String str, int i, int i2);

    @FastNative
    private static native boolean nativeSetNumColumns(long j, int i);

    private static native void nativeWriteToParcel(long j, Parcel parcel);

    public CursorWindow(String name) {
        this(name, getCursorWindowSize());
    }

    public CursorWindow(String name, long windowSizeBytes) {
        this.mCloseGuard = CloseGuard.get();
        this.mStartPos = 0;
        this.mName = (name == null || name.length() == 0) ? "<unnamed>" : name;
        this.mWindowPtr = nativeCreate(this.mName, (int) windowSizeBytes);
        if (this.mWindowPtr == 0) {
            throw new AssertionError();
        }
        this.mCloseGuard.open("close");
        recordNewWindow(Binder.getCallingPid(), this.mWindowPtr);
    }

    @Deprecated
    public CursorWindow(boolean localWindow) {
        this((String) null);
    }

    private CursorWindow(Parcel source) {
        this.mCloseGuard = CloseGuard.get();
        this.mStartPos = source.readInt();
        this.mWindowPtr = nativeCreateFromParcel(source);
        long j = this.mWindowPtr;
        if (j == 0) {
            throw new AssertionError();
        }
        this.mName = nativeGetName(j);
        this.mCloseGuard.open("close");
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mCloseGuard != null) {
                this.mCloseGuard.warnIfOpen();
            }
            dispose();
        } finally {
            super.finalize();
        }
    }

    private void dispose() {
        CloseGuard closeGuard = this.mCloseGuard;
        if (closeGuard != null) {
            closeGuard.close();
        }
        long j = this.mWindowPtr;
        if (j != 0) {
            recordClosingOfWindow(j);
            nativeDispose(this.mWindowPtr);
            this.mWindowPtr = 0L;
        }
    }

    public String getName() {
        return this.mName;
    }

    public void clear() {
        acquireReference();
        try {
            this.mStartPos = 0;
            nativeClear(this.mWindowPtr);
        } finally {
            releaseReference();
        }
    }

    public int getStartPosition() {
        return this.mStartPos;
    }

    public void setStartPosition(int pos) {
        this.mStartPos = pos;
    }

    public int getNumRows() {
        acquireReference();
        try {
            return nativeGetNumRows(this.mWindowPtr);
        } finally {
            releaseReference();
        }
    }

    public boolean setNumColumns(int columnNum) {
        acquireReference();
        try {
            return nativeSetNumColumns(this.mWindowPtr, columnNum);
        } finally {
            releaseReference();
        }
    }

    public boolean allocRow() {
        acquireReference();
        try {
            return nativeAllocRow(this.mWindowPtr);
        } finally {
            releaseReference();
        }
    }

    public void freeLastRow() {
        acquireReference();
        try {
            nativeFreeLastRow(this.mWindowPtr);
        } finally {
            releaseReference();
        }
    }

    @Deprecated
    public boolean isNull(int row, int column) {
        return getType(row, column) == 0;
    }

    @Deprecated
    public boolean isBlob(int row, int column) {
        int type = getType(row, column);
        return type == 4 || type == 0;
    }

    @Deprecated
    public boolean isLong(int row, int column) {
        return getType(row, column) == 1;
    }

    @Deprecated
    public boolean isFloat(int row, int column) {
        return getType(row, column) == 2;
    }

    @Deprecated
    public boolean isString(int row, int column) {
        int type = getType(row, column);
        return type == 3 || type == 0;
    }

    public int getType(int row, int column) {
        acquireReference();
        try {
            return nativeGetType(this.mWindowPtr, row - this.mStartPos, column);
        } finally {
            releaseReference();
        }
    }

    public byte[] getBlob(int row, int column) {
        acquireReference();
        try {
            return nativeGetBlob(this.mWindowPtr, row - this.mStartPos, column);
        } finally {
            releaseReference();
        }
    }

    public String getString(int row, int column) {
        acquireReference();
        try {
            return nativeGetString(this.mWindowPtr, row - this.mStartPos, column);
        } finally {
            releaseReference();
        }
    }

    public void copyStringToBuffer(int row, int column, CharArrayBuffer buffer) {
        if (buffer == null) {
            throw new IllegalArgumentException("CharArrayBuffer should not be null");
        }
        acquireReference();
        try {
            nativeCopyStringToBuffer(this.mWindowPtr, row - this.mStartPos, column, buffer);
        } finally {
            releaseReference();
        }
    }

    public long getLong(int row, int column) {
        acquireReference();
        try {
            return nativeGetLong(this.mWindowPtr, row - this.mStartPos, column);
        } finally {
            releaseReference();
        }
    }

    public double getDouble(int row, int column) {
        acquireReference();
        try {
            return nativeGetDouble(this.mWindowPtr, row - this.mStartPos, column);
        } finally {
            releaseReference();
        }
    }

    public short getShort(int row, int column) {
        return (short) getLong(row, column);
    }

    public int getInt(int row, int column) {
        return (int) getLong(row, column);
    }

    public float getFloat(int row, int column) {
        return (float) getDouble(row, column);
    }

    public boolean putBlob(byte[] value, int row, int column) {
        acquireReference();
        try {
            return nativePutBlob(this.mWindowPtr, value, row - this.mStartPos, column);
        } finally {
            releaseReference();
        }
    }

    public boolean putString(String value, int row, int column) {
        acquireReference();
        try {
            return nativePutString(this.mWindowPtr, value, row - this.mStartPos, column);
        } finally {
            releaseReference();
        }
    }

    public boolean putLong(long value, int row, int column) {
        acquireReference();
        try {
            return nativePutLong(this.mWindowPtr, value, row - this.mStartPos, column);
        } finally {
            releaseReference();
        }
    }

    public boolean putDouble(double value, int row, int column) {
        acquireReference();
        try {
            return nativePutDouble(this.mWindowPtr, value, row - this.mStartPos, column);
        } finally {
            releaseReference();
        }
    }

    public boolean putNull(int row, int column) {
        acquireReference();
        try {
            return nativePutNull(this.mWindowPtr, row - this.mStartPos, column);
        } finally {
            releaseReference();
        }
    }

    public static CursorWindow newFromParcel(Parcel p) {
        return CREATOR.createFromParcel(p);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        acquireReference();
        try {
            dest.writeInt(this.mStartPos);
            nativeWriteToParcel(this.mWindowPtr, dest);
            releaseReference();
            if ((flags & 1) != 0) {
            }
        } finally {
            releaseReference();
        }
    }

    @Override // android.database.sqlite.SQLiteClosable
    protected void onAllReferencesReleased() {
        dispose();
    }

    private void recordNewWindow(int pid, long window) {
        synchronized (sWindowToPidMap) {
            sWindowToPidMap.put(window, Integer.valueOf(pid));
            if (Log.isLoggable(STATS_TAG, 2)) {
                Log.i(STATS_TAG, "Created a new Cursor. " + printStats());
            }
        }
    }

    private void recordClosingOfWindow(long window) {
        synchronized (sWindowToPidMap) {
            if (sWindowToPidMap.size() == 0) {
                return;
            }
            sWindowToPidMap.delete(window);
        }
    }

    @UnsupportedAppUsage
    private String printStats() {
        StringBuilder buff = new StringBuilder();
        int myPid = Process.myPid();
        int total = 0;
        SparseIntArray pidCounts = new SparseIntArray();
        synchronized (sWindowToPidMap) {
            int size = sWindowToPidMap.size();
            if (size == 0) {
                return "";
            }
            for (int indx = 0; indx < size; indx++) {
                int pid = sWindowToPidMap.valueAt(indx).intValue();
                int value = pidCounts.get(pid);
                pidCounts.put(pid, value + 1);
            }
            int numPids = pidCounts.size();
            for (int i = 0; i < numPids; i++) {
                buff.append(" (# cursors opened by ");
                int pid2 = pidCounts.keyAt(i);
                if (pid2 == myPid) {
                    buff.append("this proc=");
                } else {
                    buff.append("pid " + pid2 + "=");
                }
                int num = pidCounts.get(pid2);
                buff.append(num + ")");
                total += num;
            }
            int i2 = buff.length();
            String s = i2 > 980 ? buff.substring(0, 980) : buff.toString();
            return "# Open Cursors=" + total + s;
        }
    }

    private static int getCursorWindowSize() {
        if (sCursorWindowSize < 0) {
            sCursorWindowSize = Resources.getSystem().getInteger(R.integer.config_cursorWindowSize) * 1024;
        }
        return sCursorWindowSize;
    }

    public String toString() {
        return getName() + " {" + Long.toHexString(this.mWindowPtr) + "}";
    }
}
