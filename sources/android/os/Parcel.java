package android.os;

import android.hardware.camera2.marshal.impl.MarshalQueryableParcelable;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.ExceptionUtils;
import android.util.Log;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import dalvik.annotation.optimization.CriticalNative;
import dalvik.annotation.optimization.FastNative;
import dalvik.system.VMRuntime;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import libcore.util.SneakyThrow;
/* loaded from: classes2.dex */
public final class Parcel {
    private static final boolean DEBUG_ARRAY_MAP = false;
    private static final boolean DEBUG_RECYCLE = false;
    private static final int EX_BAD_PARCELABLE = -2;
    private static final int EX_HAS_REPLY_HEADER = -128;
    private static final int EX_ILLEGAL_ARGUMENT = -3;
    private static final int EX_ILLEGAL_STATE = -5;
    private static final int EX_NETWORK_MAIN_THREAD = -6;
    private static final int EX_NULL_POINTER = -4;
    private static final int EX_PARCELABLE = -9;
    private static final int EX_SECURITY = -1;
    private static final int EX_SERVICE_SPECIFIC = -8;
    private static final int EX_TRANSACTION_FAILED = -129;
    private static final int EX_UNSUPPORTED_OPERATION = -7;
    private static final int POOL_SIZE = 6;
    private static final String TAG = "Parcel";
    private static final int VAL_BOOLEAN = 9;
    private static final int VAL_BOOLEANARRAY = 23;
    private static final int VAL_BUNDLE = 3;
    private static final int VAL_BYTE = 20;
    private static final int VAL_BYTEARRAY = 13;
    private static final int VAL_CHARSEQUENCE = 10;
    private static final int VAL_CHARSEQUENCEARRAY = 24;
    private static final int VAL_DOUBLE = 8;
    private static final int VAL_DOUBLEARRAY = 28;
    private static final int VAL_FLOAT = 7;
    private static final int VAL_IBINDER = 15;
    private static final int VAL_INTARRAY = 18;
    private static final int VAL_INTEGER = 1;
    private static final int VAL_LIST = 11;
    private static final int VAL_LONG = 6;
    private static final int VAL_LONGARRAY = 19;
    private static final int VAL_MAP = 2;
    private static final int VAL_NULL = -1;
    private static final int VAL_OBJECTARRAY = 17;
    private static final int VAL_PARCELABLE = 4;
    private static final int VAL_PARCELABLEARRAY = 16;
    private static final int VAL_PERSISTABLEBUNDLE = 25;
    private static final int VAL_SERIALIZABLE = 21;
    private static final int VAL_SHORT = 5;
    private static final int VAL_SIZE = 26;
    private static final int VAL_SIZEF = 27;
    private static final int VAL_SPARSEARRAY = 12;
    private static final int VAL_SPARSEBOOLEANARRAY = 22;
    private static final int VAL_STRING = 0;
    private static final int VAL_STRINGARRAY = 14;
    private static final int WRITE_EXCEPTION_STACK_TRACE_THRESHOLD_MS = 1000;
    private static volatile long sLastWriteExceptionStackTrace;
    private static boolean sParcelExceptionStackTrace;
    private ArrayMap<Class, Object> mClassCookies;
    public protected long mNativePtr;
    private long mNativeSize;
    private boolean mOwnsNativeParcelObject;
    private ReadWriteHelper mReadWriteHelper = ReadWriteHelper.DEFAULT;
    private RuntimeException mStack;
    private static final Parcel[] sOwnedPool = new Parcel[6];
    private static final Parcel[] sHolderPool = new Parcel[6];
    public static final Parcelable.Creator<String> STRING_CREATOR = new Parcelable.Creator<String>() { // from class: android.os.Parcel.1
        @Override // android.os.Parcelable.Creator
        public String createFromParcel(Parcel source) {
            return source.readString();
        }

        @Override // android.os.Parcelable.Creator
        public String[] newArray(int size) {
            return new String[size];
        }
    };
    public protected static final HashMap<ClassLoader, HashMap<String, Parcelable.Creator<?>>> mCreators = new HashMap<>();

    @Deprecated
    static native void closeFileDescriptor(FileDescriptor fileDescriptor) throws IOException;

    @Deprecated
    static native FileDescriptor dupFileDescriptor(FileDescriptor fileDescriptor) throws IOException;

    /* JADX INFO: Access modifiers changed from: private */
    public static native long getGlobalAllocCount();

    /* JADX INFO: Access modifiers changed from: private */
    public static native long getGlobalAllocSize();

    private static native long nativeAppendFrom(long j, long j2, int i, int i2);

    private static native int nativeCompareData(long j, long j2);

    private static native long nativeCreate();

    private static native byte[] nativeCreateByteArray(long j);

    @CriticalNative
    private static native int nativeDataAvail(long j);

    @CriticalNative
    private static native int nativeDataCapacity(long j);

    @CriticalNative
    private static native int nativeDataPosition(long j);

    @CriticalNative
    private static native int nativeDataSize(long j);

    private static native void nativeDestroy(long j);

    private static native void nativeEnforceInterface(long j, String str);

    private static native long nativeFreeBuffer(long j);

    @CriticalNative
    private static native long nativeGetBlobAshmemSize(long j);

    @CriticalNative
    private static native boolean nativeHasFileDescriptors(long j);

    private static native byte[] nativeMarshall(long j);

    @CriticalNative
    private static native boolean nativePushAllowFds(long j, boolean z);

    private static native byte[] nativeReadBlob(long j);

    private static native boolean nativeReadByteArray(long j, byte[] bArr, int i);

    @CriticalNative
    private static native double nativeReadDouble(long j);

    private static native FileDescriptor nativeReadFileDescriptor(long j);

    @CriticalNative
    private static native float nativeReadFloat(long j);

    @CriticalNative
    private static native int nativeReadInt(long j);

    @CriticalNative
    private static native long nativeReadLong(long j);

    static native String nativeReadString(long j);

    private static native IBinder nativeReadStrongBinder(long j);

    @CriticalNative
    private static native void nativeRestoreAllowFds(long j, boolean z);

    @FastNative
    private static native void nativeSetDataCapacity(long j, int i);

    @CriticalNative
    private static native void nativeSetDataPosition(long j, int i);

    @FastNative
    private static native long nativeSetDataSize(long j, int i);

    private static native long nativeUnmarshall(long j, byte[] bArr, int i, int i2);

    private static native void nativeWriteBlob(long j, byte[] bArr, int i, int i2);

    private static native void nativeWriteByteArray(long j, byte[] bArr, int i, int i2);

    @FastNative
    private static native void nativeWriteDouble(long j, double d);

    private static native long nativeWriteFileDescriptor(long j, FileDescriptor fileDescriptor);

    @FastNative
    private static native void nativeWriteFloat(long j, float f);

    @FastNative
    private static native void nativeWriteInt(long j, int i);

    private static native void nativeWriteInterfaceToken(long j, String str);

    @FastNative
    private static native void nativeWriteLong(long j, long j2);

    static native void nativeWriteString(long j, String str);

    private static native void nativeWriteStrongBinder(long j, IBinder iBinder);

    @Deprecated
    static native FileDescriptor openFileDescriptor(String str, int i) throws FileNotFoundException;

    /* loaded from: classes2.dex */
    public static class ReadWriteHelper {
        public static final ReadWriteHelper DEFAULT = new ReadWriteHelper();

        public synchronized void writeString(Parcel p, String s) {
            Parcel.nativeWriteString(p.mNativePtr, s);
        }

        public synchronized String readString(Parcel p) {
            return Parcel.nativeReadString(p.mNativePtr);
        }
    }

    public static Parcel obtain() {
        Parcel[] pool = sOwnedPool;
        synchronized (pool) {
            for (int i = 0; i < 6; i++) {
                try {
                    Parcel p = pool[i];
                    if (p != null) {
                        pool[i] = null;
                        p.mReadWriteHelper = ReadWriteHelper.DEFAULT;
                        return p;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            return new Parcel(0L);
        }
    }

    public final void recycle() {
        Parcel[] pool;
        freeBuffer();
        if (this.mOwnsNativeParcelObject) {
            pool = sOwnedPool;
        } else {
            this.mNativePtr = 0L;
            pool = sHolderPool;
        }
        synchronized (pool) {
            for (int i = 0; i < 6; i++) {
                try {
                    if (pool[i] == null) {
                        pool[i] = this;
                        return;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public synchronized void setReadWriteHelper(ReadWriteHelper helper) {
        this.mReadWriteHelper = helper != null ? helper : ReadWriteHelper.DEFAULT;
    }

    public synchronized boolean hasReadWriteHelper() {
        return (this.mReadWriteHelper == null || this.mReadWriteHelper == ReadWriteHelper.DEFAULT) ? false : true;
    }

    public final int dataSize() {
        return nativeDataSize(this.mNativePtr);
    }

    public final int dataAvail() {
        return nativeDataAvail(this.mNativePtr);
    }

    public final int dataPosition() {
        return nativeDataPosition(this.mNativePtr);
    }

    public final int dataCapacity() {
        return nativeDataCapacity(this.mNativePtr);
    }

    public final void setDataSize(int size) {
        updateNativeSize(nativeSetDataSize(this.mNativePtr, size));
    }

    public final void setDataPosition(int pos) {
        nativeSetDataPosition(this.mNativePtr, pos);
    }

    public final void setDataCapacity(int size) {
        nativeSetDataCapacity(this.mNativePtr, size);
    }

    public final synchronized boolean pushAllowFds(boolean allowFds) {
        return nativePushAllowFds(this.mNativePtr, allowFds);
    }

    public final synchronized void restoreAllowFds(boolean lastValue) {
        nativeRestoreAllowFds(this.mNativePtr, lastValue);
    }

    public final byte[] marshall() {
        return nativeMarshall(this.mNativePtr);
    }

    public final void unmarshall(byte[] data, int offset, int length) {
        updateNativeSize(nativeUnmarshall(this.mNativePtr, data, offset, length));
    }

    public final void appendFrom(Parcel parcel, int offset, int length) {
        updateNativeSize(nativeAppendFrom(this.mNativePtr, parcel.mNativePtr, offset, length));
    }

    public final synchronized int compareData(Parcel other) {
        return nativeCompareData(this.mNativePtr, other.mNativePtr);
    }

    public final synchronized void setClassCookie(Class clz, Object cookie) {
        if (this.mClassCookies == null) {
            this.mClassCookies = new ArrayMap<>();
        }
        this.mClassCookies.put(clz, cookie);
    }

    public final synchronized Object getClassCookie(Class clz) {
        if (this.mClassCookies != null) {
            return this.mClassCookies.get(clz);
        }
        return null;
    }

    public final synchronized void adoptClassCookies(Parcel from) {
        this.mClassCookies = from.mClassCookies;
    }

    public synchronized Map<Class, Object> copyClassCookies() {
        return new ArrayMap(this.mClassCookies);
    }

    public synchronized void putClassCookies(Map<Class, Object> cookies) {
        if (cookies == null) {
            return;
        }
        if (this.mClassCookies == null) {
            this.mClassCookies = new ArrayMap<>();
        }
        this.mClassCookies.putAll(cookies);
    }

    public final boolean hasFileDescriptors() {
        return nativeHasFileDescriptors(this.mNativePtr);
    }

    public final void writeInterfaceToken(String interfaceName) {
        nativeWriteInterfaceToken(this.mNativePtr, interfaceName);
    }

    public final void enforceInterface(String interfaceName) {
        nativeEnforceInterface(this.mNativePtr, interfaceName);
    }

    public final void writeByteArray(byte[] b) {
        writeByteArray(b, 0, b != null ? b.length : 0);
    }

    public final void writeByteArray(byte[] b, int offset, int len) {
        if (b == null) {
            writeInt(-1);
            return;
        }
        Arrays.checkOffsetAndCount(b.length, offset, len);
        nativeWriteByteArray(this.mNativePtr, b, offset, len);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void writeBlob(byte[] b) {
        writeBlob(b, 0, b != null ? b.length : 0);
    }

    public final synchronized void writeBlob(byte[] b, int offset, int len) {
        if (b == null) {
            writeInt(-1);
            return;
        }
        Arrays.checkOffsetAndCount(b.length, offset, len);
        nativeWriteBlob(this.mNativePtr, b, offset, len);
    }

    public final void writeInt(int val) {
        nativeWriteInt(this.mNativePtr, val);
    }

    public final void writeLong(long val) {
        nativeWriteLong(this.mNativePtr, val);
    }

    public final void writeFloat(float val) {
        nativeWriteFloat(this.mNativePtr, val);
    }

    public final void writeDouble(double val) {
        nativeWriteDouble(this.mNativePtr, val);
    }

    public final void writeString(String val) {
        this.mReadWriteHelper.writeString(this, val);
    }

    public synchronized void writeStringNoHelper(String val) {
        nativeWriteString(this.mNativePtr, val);
    }

    public final synchronized void writeBoolean(boolean val) {
        writeInt(val ? 1 : 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void writeCharSequence(CharSequence val) {
        TextUtils.writeToParcel(val, this, 0);
    }

    public final void writeStrongBinder(IBinder val) {
        nativeWriteStrongBinder(this.mNativePtr, val);
    }

    public final void writeStrongInterface(IInterface val) {
        writeStrongBinder(val == null ? null : val.asBinder());
    }

    public final void writeFileDescriptor(FileDescriptor val) {
        updateNativeSize(nativeWriteFileDescriptor(this.mNativePtr, val));
    }

    private synchronized void updateNativeSize(long newNativeSize) {
        if (this.mOwnsNativeParcelObject) {
            if (newNativeSize > 2147483647L) {
                newNativeSize = 2147483647L;
            }
            if (newNativeSize != this.mNativeSize) {
                int delta = (int) (newNativeSize - this.mNativeSize);
                if (delta > 0) {
                    VMRuntime.getRuntime().registerNativeAllocation(delta);
                } else {
                    VMRuntime.getRuntime().registerNativeFree(-delta);
                }
                this.mNativeSize = newNativeSize;
            }
        }
    }

    public final synchronized void writeRawFileDescriptor(FileDescriptor val) {
        nativeWriteFileDescriptor(this.mNativePtr, val);
    }

    public final synchronized void writeRawFileDescriptorArray(FileDescriptor[] value) {
        if (value != null) {
            int N = value.length;
            writeInt(N);
            for (FileDescriptor fileDescriptor : value) {
                writeRawFileDescriptor(fileDescriptor);
            }
            return;
        }
        writeInt(-1);
    }

    public final void writeByte(byte val) {
        writeInt(val);
    }

    public final void writeMap(Map val) {
        writeMapInternal(val);
    }

    synchronized void writeMapInternal(Map<String, Object> val) {
        if (val == null) {
            writeInt(-1);
            return;
        }
        Set<Map.Entry<String, Object>> entries = val.entrySet();
        int size = entries.size();
        writeInt(size);
        for (Map.Entry<String, Object> e : entries) {
            writeValue(e.getKey());
            writeValue(e.getValue());
            size--;
        }
        if (size != 0) {
            throw new BadParcelableException("Map size does not match number of entries!");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void writeArrayMapInternal(ArrayMap<String, Object> val) {
        if (val == null) {
            writeInt(-1);
            return;
        }
        int N = val.size();
        writeInt(N);
        for (int i = 0; i < N; i++) {
            writeString(val.keyAt(i));
            writeValue(val.valueAt(i));
        }
    }

    private protected void writeArrayMap(ArrayMap<String, Object> val) {
        writeArrayMapInternal(val);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeArraySet(ArraySet<? extends Object> val) {
        int size = val != null ? val.size() : -1;
        writeInt(size);
        for (int i = 0; i < size; i++) {
            writeValue(val.valueAt(i));
        }
    }

    public final void writeBundle(Bundle val) {
        if (val == null) {
            writeInt(-1);
        } else {
            val.writeToParcel(this, 0);
        }
    }

    public final void writePersistableBundle(PersistableBundle val) {
        if (val == null) {
            writeInt(-1);
        } else {
            val.writeToParcel(this, 0);
        }
    }

    public final void writeSize(Size val) {
        writeInt(val.getWidth());
        writeInt(val.getHeight());
    }

    public final void writeSizeF(SizeF val) {
        writeFloat(val.getWidth());
        writeFloat(val.getHeight());
    }

    public final void writeList(List val) {
        if (val == null) {
            writeInt(-1);
            return;
        }
        int N = val.size();
        writeInt(N);
        for (int i = 0; i < N; i++) {
            writeValue(val.get(i));
        }
    }

    public final void writeArray(Object[] val) {
        if (val == null) {
            writeInt(-1);
            return;
        }
        int N = val.length;
        writeInt(N);
        for (Object obj : val) {
            writeValue(obj);
        }
    }

    public final void writeSparseArray(SparseArray<Object> val) {
        if (val == null) {
            writeInt(-1);
            return;
        }
        int N = val.size();
        writeInt(N);
        for (int i = 0; i < N; i++) {
            writeInt(val.keyAt(i));
            writeValue(val.valueAt(i));
        }
    }

    public final void writeSparseBooleanArray(SparseBooleanArray val) {
        if (val == null) {
            writeInt(-1);
            return;
        }
        int N = val.size();
        writeInt(N);
        for (int i = 0; i < N; i++) {
            writeInt(val.keyAt(i));
            writeByte(val.valueAt(i) ? (byte) 1 : (byte) 0);
        }
    }

    public final synchronized void writeSparseIntArray(SparseIntArray val) {
        if (val == null) {
            writeInt(-1);
            return;
        }
        int N = val.size();
        writeInt(N);
        for (int i = 0; i < N; i++) {
            writeInt(val.keyAt(i));
            writeInt(val.valueAt(i));
        }
    }

    public final void writeBooleanArray(boolean[] val) {
        if (val != null) {
            int N = val.length;
            writeInt(N);
            for (boolean z : val) {
                writeInt(z ? 1 : 0);
            }
            return;
        }
        writeInt(-1);
    }

    public final boolean[] createBooleanArray() {
        int N = readInt();
        if (N >= 0 && N <= (dataAvail() >> 2)) {
            boolean[] val = new boolean[N];
            for (int i = 0; i < N; i++) {
                val[i] = readInt() != 0;
            }
            return val;
        }
        return null;
    }

    public final void readBooleanArray(boolean[] val) {
        int N = readInt();
        if (N == val.length) {
            for (int i = 0; i < N; i++) {
                val[i] = readInt() != 0;
            }
            return;
        }
        throw new RuntimeException("bad array lengths");
    }

    public final void writeCharArray(char[] val) {
        if (val != null) {
            int N = val.length;
            writeInt(N);
            for (char c : val) {
                writeInt(c);
            }
            return;
        }
        writeInt(-1);
    }

    public final char[] createCharArray() {
        int N = readInt();
        if (N >= 0 && N <= (dataAvail() >> 2)) {
            char[] val = new char[N];
            for (int i = 0; i < N; i++) {
                val[i] = (char) readInt();
            }
            return val;
        }
        return null;
    }

    public final void readCharArray(char[] val) {
        int N = readInt();
        if (N == val.length) {
            for (int i = 0; i < N; i++) {
                val[i] = (char) readInt();
            }
            return;
        }
        throw new RuntimeException("bad array lengths");
    }

    public final void writeIntArray(int[] val) {
        if (val != null) {
            int N = val.length;
            writeInt(N);
            for (int i : val) {
                writeInt(i);
            }
            return;
        }
        writeInt(-1);
    }

    public final int[] createIntArray() {
        int N = readInt();
        if (N >= 0 && N <= (dataAvail() >> 2)) {
            int[] val = new int[N];
            for (int i = 0; i < N; i++) {
                val[i] = readInt();
            }
            return val;
        }
        return null;
    }

    public final void readIntArray(int[] val) {
        int N = readInt();
        if (N == val.length) {
            for (int i = 0; i < N; i++) {
                val[i] = readInt();
            }
            return;
        }
        throw new RuntimeException("bad array lengths");
    }

    public final void writeLongArray(long[] val) {
        if (val != null) {
            int N = val.length;
            writeInt(N);
            for (long j : val) {
                writeLong(j);
            }
            return;
        }
        writeInt(-1);
    }

    public final long[] createLongArray() {
        int N = readInt();
        if (N >= 0 && N <= (dataAvail() >> 3)) {
            long[] val = new long[N];
            for (int i = 0; i < N; i++) {
                val[i] = readLong();
            }
            return val;
        }
        return null;
    }

    public final void readLongArray(long[] val) {
        int N = readInt();
        if (N == val.length) {
            for (int i = 0; i < N; i++) {
                val[i] = readLong();
            }
            return;
        }
        throw new RuntimeException("bad array lengths");
    }

    public final void writeFloatArray(float[] val) {
        if (val != null) {
            int N = val.length;
            writeInt(N);
            for (float f : val) {
                writeFloat(f);
            }
            return;
        }
        writeInt(-1);
    }

    public final float[] createFloatArray() {
        int N = readInt();
        if (N >= 0 && N <= (dataAvail() >> 2)) {
            float[] val = new float[N];
            for (int i = 0; i < N; i++) {
                val[i] = readFloat();
            }
            return val;
        }
        return null;
    }

    public final void readFloatArray(float[] val) {
        int N = readInt();
        if (N == val.length) {
            for (int i = 0; i < N; i++) {
                val[i] = readFloat();
            }
            return;
        }
        throw new RuntimeException("bad array lengths");
    }

    public final void writeDoubleArray(double[] val) {
        if (val != null) {
            int N = val.length;
            writeInt(N);
            for (double d : val) {
                writeDouble(d);
            }
            return;
        }
        writeInt(-1);
    }

    public final double[] createDoubleArray() {
        int N = readInt();
        if (N >= 0 && N <= (dataAvail() >> 3)) {
            double[] val = new double[N];
            for (int i = 0; i < N; i++) {
                val[i] = readDouble();
            }
            return val;
        }
        return null;
    }

    public final void readDoubleArray(double[] val) {
        int N = readInt();
        if (N == val.length) {
            for (int i = 0; i < N; i++) {
                val[i] = readDouble();
            }
            return;
        }
        throw new RuntimeException("bad array lengths");
    }

    public final void writeStringArray(String[] val) {
        if (val != null) {
            int N = val.length;
            writeInt(N);
            for (String str : val) {
                writeString(str);
            }
            return;
        }
        writeInt(-1);
    }

    public final String[] createStringArray() {
        int N = readInt();
        if (N >= 0) {
            String[] val = new String[N];
            for (int i = 0; i < N; i++) {
                val[i] = readString();
            }
            return val;
        }
        return null;
    }

    public final void readStringArray(String[] val) {
        int N = readInt();
        if (N == val.length) {
            for (int i = 0; i < N; i++) {
                val[i] = readString();
            }
            return;
        }
        throw new RuntimeException("bad array lengths");
    }

    public final void writeBinderArray(IBinder[] val) {
        if (val != null) {
            int N = val.length;
            writeInt(N);
            for (IBinder iBinder : val) {
                writeStrongBinder(iBinder);
            }
            return;
        }
        writeInt(-1);
    }

    public final synchronized void writeCharSequenceArray(CharSequence[] val) {
        if (val != null) {
            int N = val.length;
            writeInt(N);
            for (CharSequence charSequence : val) {
                writeCharSequence(charSequence);
            }
            return;
        }
        writeInt(-1);
    }

    public final synchronized void writeCharSequenceList(ArrayList<CharSequence> val) {
        if (val != null) {
            int N = val.size();
            writeInt(N);
            for (int i = 0; i < N; i++) {
                writeCharSequence(val.get(i));
            }
            return;
        }
        writeInt(-1);
    }

    public final IBinder[] createBinderArray() {
        int N = readInt();
        if (N >= 0) {
            IBinder[] val = new IBinder[N];
            for (int i = 0; i < N; i++) {
                val[i] = readStrongBinder();
            }
            return val;
        }
        return null;
    }

    public final void readBinderArray(IBinder[] val) {
        int N = readInt();
        if (N == val.length) {
            for (int i = 0; i < N; i++) {
                val[i] = readStrongBinder();
            }
            return;
        }
        throw new RuntimeException("bad array lengths");
    }

    public final <T extends Parcelable> void writeTypedList(List<T> val) {
        writeTypedList(val, 0);
    }

    public synchronized <T extends Parcelable> void writeTypedList(List<T> val, int parcelableFlags) {
        if (val == null) {
            writeInt(-1);
            return;
        }
        int N = val.size();
        writeInt(N);
        for (int i = 0; i < N; i++) {
            writeTypedObject(val.get(i), parcelableFlags);
        }
    }

    public final void writeStringList(List<String> val) {
        if (val == null) {
            writeInt(-1);
            return;
        }
        int N = val.size();
        writeInt(N);
        for (int i = 0; i < N; i++) {
            writeString(val.get(i));
        }
    }

    public final void writeBinderList(List<IBinder> val) {
        if (val == null) {
            writeInt(-1);
            return;
        }
        int N = val.size();
        writeInt(N);
        for (int i = 0; i < N; i++) {
            writeStrongBinder(val.get(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final <T extends Parcelable> void writeParcelableList(List<T> val, int flags) {
        if (val == null) {
            writeInt(-1);
            return;
        }
        int N = val.size();
        writeInt(N);
        for (int i = 0; i < N; i++) {
            writeParcelable(val.get(i), flags);
        }
    }

    public final <T extends Parcelable> void writeTypedArray(T[] val, int parcelableFlags) {
        if (val != null) {
            int N = val.length;
            writeInt(N);
            for (T t : val) {
                writeTypedObject(t, parcelableFlags);
            }
            return;
        }
        writeInt(-1);
    }

    public final <T extends Parcelable> void writeTypedObject(T val, int parcelableFlags) {
        if (val != null) {
            writeInt(1);
            val.writeToParcel(this, parcelableFlags);
            return;
        }
        writeInt(0);
    }

    public final void writeValue(Object v) {
        if (v == null) {
            writeInt(-1);
        } else if (v instanceof String) {
            writeInt(0);
            writeString((String) v);
        } else if (v instanceof Integer) {
            writeInt(1);
            writeInt(((Integer) v).intValue());
        } else if (v instanceof Map) {
            writeInt(2);
            writeMap((Map) v);
        } else if (v instanceof Bundle) {
            writeInt(3);
            writeBundle((Bundle) v);
        } else if (v instanceof PersistableBundle) {
            writeInt(25);
            writePersistableBundle((PersistableBundle) v);
        } else if (v instanceof Parcelable) {
            writeInt(4);
            writeParcelable((Parcelable) v, 0);
        } else if (v instanceof Short) {
            writeInt(5);
            writeInt(((Short) v).intValue());
        } else if (v instanceof Long) {
            writeInt(6);
            writeLong(((Long) v).longValue());
        } else if (v instanceof Float) {
            writeInt(7);
            writeFloat(((Float) v).floatValue());
        } else if (v instanceof Double) {
            writeInt(8);
            writeDouble(((Double) v).doubleValue());
        } else if (v instanceof Boolean) {
            writeInt(9);
            writeInt(((Boolean) v).booleanValue() ? 1 : 0);
        } else if (v instanceof CharSequence) {
            writeInt(10);
            writeCharSequence((CharSequence) v);
        } else if (v instanceof List) {
            writeInt(11);
            writeList((List) v);
        } else if (v instanceof SparseArray) {
            writeInt(12);
            writeSparseArray((SparseArray) v);
        } else if (v instanceof boolean[]) {
            writeInt(23);
            writeBooleanArray((boolean[]) v);
        } else if (v instanceof byte[]) {
            writeInt(13);
            writeByteArray((byte[]) v);
        } else if (v instanceof String[]) {
            writeInt(14);
            writeStringArray((String[]) v);
        } else if (v instanceof CharSequence[]) {
            writeInt(24);
            writeCharSequenceArray((CharSequence[]) v);
        } else if (v instanceof IBinder) {
            writeInt(15);
            writeStrongBinder((IBinder) v);
        } else if (v instanceof Parcelable[]) {
            writeInt(16);
            writeParcelableArray((Parcelable[]) v, 0);
        } else if (v instanceof int[]) {
            writeInt(18);
            writeIntArray((int[]) v);
        } else if (v instanceof long[]) {
            writeInt(19);
            writeLongArray((long[]) v);
        } else if (v instanceof Byte) {
            writeInt(20);
            writeInt(((Byte) v).byteValue());
        } else if (v instanceof Size) {
            writeInt(26);
            writeSize((Size) v);
        } else if (v instanceof SizeF) {
            writeInt(27);
            writeSizeF((SizeF) v);
        } else if (v instanceof double[]) {
            writeInt(28);
            writeDoubleArray((double[]) v);
        } else {
            Class<?> clazz = v.getClass();
            if (clazz.isArray() && clazz.getComponentType() == Object.class) {
                writeInt(17);
                writeArray((Object[]) v);
            } else if (v instanceof Serializable) {
                writeInt(21);
                writeSerializable((Serializable) v);
            } else {
                throw new RuntimeException("Parcel: unable to marshal value " + v);
            }
        }
    }

    public final void writeParcelable(Parcelable p, int parcelableFlags) {
        if (p == null) {
            writeString(null);
            return;
        }
        writeParcelableCreator(p);
        p.writeToParcel(this, parcelableFlags);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void writeParcelableCreator(Parcelable p) {
        String name = p.getClass().getName();
        writeString(name);
    }

    public final void writeSerializable(Serializable s) {
        if (s == null) {
            writeString(null);
            return;
        }
        String name = s.getClass().getName();
        writeString(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(s);
            oos.close();
            writeByteArray(baos.toByteArray());
        } catch (IOException ioe) {
            throw new RuntimeException("Parcelable encountered IOException writing serializable object (name = " + name + ")", ioe);
        }
    }

    public static synchronized void setStackTraceParceling(boolean enabled) {
        sParcelExceptionStackTrace = enabled;
    }

    public final void writeException(Exception e) {
        int code = 0;
        if ((e instanceof Parcelable) && e.getClass().getClassLoader() == Parcelable.class.getClassLoader()) {
            code = -9;
        } else if (e instanceof SecurityException) {
            code = -1;
        } else if (e instanceof BadParcelableException) {
            code = -2;
        } else if (e instanceof IllegalArgumentException) {
            code = -3;
        } else if (e instanceof NullPointerException) {
            code = -4;
        } else if (e instanceof IllegalStateException) {
            code = -5;
        } else if (e instanceof NetworkOnMainThreadException) {
            code = -6;
        } else if (e instanceof UnsupportedOperationException) {
            code = -7;
        } else if (e instanceof ServiceSpecificException) {
            code = -8;
        }
        writeInt(code);
        StrictMode.clearGatheredViolations();
        if (code == 0) {
            if (e instanceof RuntimeException) {
                throw ((RuntimeException) e);
            }
            throw new RuntimeException(e);
        }
        writeString(e.getMessage());
        long timeNow = sParcelExceptionStackTrace ? SystemClock.elapsedRealtime() : 0L;
        if (sParcelExceptionStackTrace && timeNow - sLastWriteExceptionStackTrace > 1000) {
            sLastWriteExceptionStackTrace = timeNow;
            int sizePosition = dataPosition();
            writeInt(0);
            StackTraceElement[] stackTrace = e.getStackTrace();
            int truncatedSize = Math.min(stackTrace.length, 5);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < truncatedSize; i++) {
                sb.append("\tat ");
                sb.append(stackTrace[i]);
                sb.append('\n');
            }
            writeString(sb.toString());
            int payloadPosition = dataPosition();
            setDataPosition(sizePosition);
            writeInt(payloadPosition - sizePosition);
            setDataPosition(payloadPosition);
        } else {
            writeInt(0);
        }
        switch (code) {
            case -9:
                int sizePosition2 = dataPosition();
                writeInt(0);
                writeParcelable((Parcelable) e, 1);
                int payloadPosition2 = dataPosition();
                setDataPosition(sizePosition2);
                writeInt(payloadPosition2 - sizePosition2);
                setDataPosition(payloadPosition2);
                return;
            case -8:
                writeInt(((ServiceSpecificException) e).errorCode);
                return;
            default:
                return;
        }
    }

    public final void writeNoException() {
        if (StrictMode.hasGatheredViolations()) {
            writeInt(-128);
            int sizePosition = dataPosition();
            writeInt(0);
            StrictMode.writeGatheredViolationsToParcel(this);
            int payloadPosition = dataPosition();
            setDataPosition(sizePosition);
            writeInt(payloadPosition - sizePosition);
            setDataPosition(payloadPosition);
            return;
        }
        writeInt(0);
    }

    public final void readException() {
        int code = readExceptionCode();
        if (code != 0) {
            String msg = readString();
            readException(code, msg);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int readExceptionCode() {
        int code = readInt();
        if (code == -128) {
            int headerSize = readInt();
            if (headerSize == 0) {
                Log.e(TAG, "Unexpected zero-sized Parcel reply header.");
                return 0;
            }
            StrictMode.readAndHandleBinderCallViolations(this);
            return 0;
        }
        return code;
    }

    public final void readException(int code, String msg) {
        String remoteStackTrace = null;
        int remoteStackPayloadSize = readInt();
        if (remoteStackPayloadSize > 0) {
            remoteStackTrace = readString();
        }
        Exception e = createException(code, msg);
        if (remoteStackTrace != null) {
            RemoteException cause = new RemoteException("Remote stack trace:\n" + remoteStackTrace, null, false, false);
            try {
                Throwable rootCause = ExceptionUtils.getRootCause(e);
                if (rootCause != null) {
                    rootCause.initCause(cause);
                }
            } catch (RuntimeException ex) {
                Log.e(TAG, "Cannot set cause " + cause + " for " + e, ex);
            }
        }
        SneakyThrow.sneakyThrow(e);
    }

    private synchronized Exception createException(int code, String msg) {
        switch (code) {
            case -9:
                if (readInt() > 0) {
                    return (Exception) readParcelable(Parcelable.class.getClassLoader());
                }
                return new RuntimeException(msg + " [missing Parcelable]");
            case -8:
                return new ServiceSpecificException(readInt(), msg);
            case -7:
                return new UnsupportedOperationException(msg);
            case -6:
                return new NetworkOnMainThreadException();
            case -5:
                return new IllegalStateException(msg);
            case -4:
                return new NullPointerException(msg);
            case -3:
                return new IllegalArgumentException(msg);
            case -2:
                return new BadParcelableException(msg);
            case -1:
                return new SecurityException(msg);
            default:
                return new RuntimeException("Unknown exception code: " + code + " msg " + msg);
        }
    }

    public final int readInt() {
        return nativeReadInt(this.mNativePtr);
    }

    public final long readLong() {
        return nativeReadLong(this.mNativePtr);
    }

    public final float readFloat() {
        return nativeReadFloat(this.mNativePtr);
    }

    public final double readDouble() {
        return nativeReadDouble(this.mNativePtr);
    }

    public final String readString() {
        return this.mReadWriteHelper.readString(this);
    }

    public synchronized String readStringNoHelper() {
        return nativeReadString(this.mNativePtr);
    }

    public final synchronized boolean readBoolean() {
        return readInt() != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final CharSequence readCharSequence() {
        return TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(this);
    }

    public final IBinder readStrongBinder() {
        return nativeReadStrongBinder(this.mNativePtr);
    }

    public final ParcelFileDescriptor readFileDescriptor() {
        FileDescriptor fd = nativeReadFileDescriptor(this.mNativePtr);
        if (fd != null) {
            return new ParcelFileDescriptor(fd);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final FileDescriptor readRawFileDescriptor() {
        return nativeReadFileDescriptor(this.mNativePtr);
    }

    public final synchronized FileDescriptor[] createRawFileDescriptorArray() {
        int N = readInt();
        if (N < 0) {
            return null;
        }
        FileDescriptor[] f = new FileDescriptor[N];
        for (int i = 0; i < N; i++) {
            f[i] = readRawFileDescriptor();
        }
        return f;
    }

    public final synchronized void readRawFileDescriptorArray(FileDescriptor[] val) {
        int N = readInt();
        if (N == val.length) {
            for (int i = 0; i < N; i++) {
                val[i] = readRawFileDescriptor();
            }
            return;
        }
        throw new RuntimeException("bad array lengths");
    }

    public final byte readByte() {
        return (byte) (readInt() & 255);
    }

    public final void readMap(Map outVal, ClassLoader loader) {
        int N = readInt();
        readMapInternal(outVal, N, loader);
    }

    public final void readList(List outVal, ClassLoader loader) {
        int N = readInt();
        readListInternal(outVal, N, loader);
    }

    public final HashMap readHashMap(ClassLoader loader) {
        int N = readInt();
        if (N < 0) {
            return null;
        }
        HashMap m = new HashMap(N);
        readMapInternal(m, N, loader);
        return m;
    }

    public final Bundle readBundle() {
        return readBundle(null);
    }

    public final Bundle readBundle(ClassLoader loader) {
        int length = readInt();
        if (length < 0) {
            return null;
        }
        Bundle bundle = new Bundle(this, length);
        if (loader != null) {
            bundle.setClassLoader(loader);
        }
        return bundle;
    }

    public final PersistableBundle readPersistableBundle() {
        return readPersistableBundle(null);
    }

    public final PersistableBundle readPersistableBundle(ClassLoader loader) {
        int length = readInt();
        if (length < 0) {
            return null;
        }
        PersistableBundle bundle = new PersistableBundle(this, length);
        if (loader != null) {
            bundle.setClassLoader(loader);
        }
        return bundle;
    }

    public final Size readSize() {
        int width = readInt();
        int height = readInt();
        return new Size(width, height);
    }

    public final SizeF readSizeF() {
        float width = readFloat();
        float height = readFloat();
        return new SizeF(width, height);
    }

    public final byte[] createByteArray() {
        return nativeCreateByteArray(this.mNativePtr);
    }

    public final void readByteArray(byte[] val) {
        boolean valid = nativeReadByteArray(this.mNativePtr, val, val != null ? val.length : 0);
        if (!valid) {
            throw new RuntimeException("bad array lengths");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final byte[] readBlob() {
        return nativeReadBlob(this.mNativePtr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final String[] readStringArray() {
        String[] array = null;
        int length = readInt();
        if (length >= 0) {
            array = new String[length];
            for (int i = 0; i < length; i++) {
                array[i] = readString();
            }
        }
        return array;
    }

    public final synchronized CharSequence[] readCharSequenceArray() {
        CharSequence[] array = null;
        int length = readInt();
        if (length >= 0) {
            array = new CharSequence[length];
            for (int i = 0; i < length; i++) {
                array[i] = readCharSequence();
            }
        }
        return array;
    }

    public final synchronized ArrayList<CharSequence> readCharSequenceList() {
        ArrayList<CharSequence> array = null;
        int length = readInt();
        if (length >= 0) {
            array = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                array.add(readCharSequence());
            }
        }
        return array;
    }

    public final ArrayList readArrayList(ClassLoader loader) {
        int N = readInt();
        if (N < 0) {
            return null;
        }
        ArrayList l = new ArrayList(N);
        readListInternal(l, N, loader);
        return l;
    }

    public final Object[] readArray(ClassLoader loader) {
        int N = readInt();
        if (N < 0) {
            return null;
        }
        Object[] l = new Object[N];
        readArrayInternal(l, N, loader);
        return l;
    }

    public final SparseArray readSparseArray(ClassLoader loader) {
        int N = readInt();
        if (N < 0) {
            return null;
        }
        SparseArray sa = new SparseArray(N);
        readSparseArrayInternal(sa, N, loader);
        return sa;
    }

    public final SparseBooleanArray readSparseBooleanArray() {
        int N = readInt();
        if (N < 0) {
            return null;
        }
        SparseBooleanArray sa = new SparseBooleanArray(N);
        readSparseBooleanArrayInternal(sa, N);
        return sa;
    }

    public final synchronized SparseIntArray readSparseIntArray() {
        int N = readInt();
        if (N < 0) {
            return null;
        }
        SparseIntArray sa = new SparseIntArray(N);
        readSparseIntArrayInternal(sa, N);
        return sa;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <T> ArrayList<T> createTypedArrayList(Parcelable.Creator<T> c) {
        int N = readInt();
        if (N < 0) {
            return null;
        }
        ArrayList<T> l = (ArrayList<T>) new ArrayList(N);
        while (N > 0) {
            l.add(readTypedObject(c));
            N--;
        }
        return l;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <T> void readTypedList(List<T> list, Parcelable.Creator<T> c) {
        int M = list.size();
        int N = readInt();
        int i = 0;
        while (i < M && i < N) {
            list.set(i, readTypedObject(c));
            i++;
        }
        while (i < N) {
            list.add(readTypedObject(c));
            i++;
        }
        while (i < M) {
            list.remove(N);
            i++;
        }
    }

    public final ArrayList<String> createStringArrayList() {
        int N = readInt();
        if (N < 0) {
            return null;
        }
        ArrayList<String> l = new ArrayList<>(N);
        while (N > 0) {
            l.add(readString());
            N--;
        }
        return l;
    }

    public final ArrayList<IBinder> createBinderArrayList() {
        int N = readInt();
        if (N < 0) {
            return null;
        }
        ArrayList<IBinder> l = new ArrayList<>(N);
        while (N > 0) {
            l.add(readStrongBinder());
            N--;
        }
        return l;
    }

    public final void readStringList(List<String> list) {
        int M = list.size();
        int N = readInt();
        int i = 0;
        while (i < M && i < N) {
            list.set(i, readString());
            i++;
        }
        while (i < N) {
            list.add(readString());
            i++;
        }
        while (i < M) {
            list.remove(N);
            i++;
        }
    }

    public final void readBinderList(List<IBinder> list) {
        int M = list.size();
        int N = readInt();
        int i = 0;
        while (i < M && i < N) {
            list.set(i, readStrongBinder());
            i++;
        }
        while (i < N) {
            list.add(readStrongBinder());
            i++;
        }
        while (i < M) {
            list.remove(N);
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public final <T extends Parcelable> List<T> readParcelableList(List<T> list, ClassLoader cl) {
        int N = readInt();
        if (N == -1) {
            list.clear();
            return list;
        }
        int M = list.size();
        int i = 0;
        while (i < M && i < N) {
            list.set(i, readParcelable(cl));
            i++;
        }
        while (i < N) {
            list.add(readParcelable(cl));
            i++;
        }
        while (i < M) {
            list.remove(N);
            i++;
        }
        return list;
    }

    public final <T> T[] createTypedArray(Parcelable.Creator<T> c) {
        int N = readInt();
        if (N < 0) {
            return null;
        }
        T[] l = c.newArray(N);
        for (int i = 0; i < N; i++) {
            l[i] = readTypedObject(c);
        }
        return l;
    }

    public final <T> void readTypedArray(T[] val, Parcelable.Creator<T> c) {
        int N = readInt();
        if (N == val.length) {
            for (int i = 0; i < N; i++) {
                val[i] = readTypedObject(c);
            }
            return;
        }
        throw new RuntimeException("bad array lengths");
    }

    @Deprecated
    public final synchronized <T> T[] readTypedArray(Parcelable.Creator<T> c) {
        return (T[]) createTypedArray(c);
    }

    public final <T> T readTypedObject(Parcelable.Creator<T> c) {
        if (readInt() != 0) {
            return c.createFromParcel(this);
        }
        return null;
    }

    public final <T extends Parcelable> void writeParcelableArray(T[] value, int parcelableFlags) {
        if (value != null) {
            int N = value.length;
            writeInt(N);
            for (T t : value) {
                writeParcelable(t, parcelableFlags);
            }
            return;
        }
        writeInt(-1);
    }

    public final Object readValue(ClassLoader loader) {
        int type = readInt();
        switch (type) {
            case -1:
                return null;
            case 0:
                return readString();
            case 1:
                return Integer.valueOf(readInt());
            case 2:
                return readHashMap(loader);
            case 3:
                return readBundle(loader);
            case 4:
                return readParcelable(loader);
            case 5:
                return Short.valueOf((short) readInt());
            case 6:
                return Long.valueOf(readLong());
            case 7:
                return Float.valueOf(readFloat());
            case 8:
                return Double.valueOf(readDouble());
            case 9:
                return Boolean.valueOf(readInt() == 1);
            case 10:
                return readCharSequence();
            case 11:
                return readArrayList(loader);
            case 12:
                return readSparseArray(loader);
            case 13:
                return createByteArray();
            case 14:
                return readStringArray();
            case 15:
                return readStrongBinder();
            case 16:
                return readParcelableArray(loader);
            case 17:
                return readArray(loader);
            case 18:
                return createIntArray();
            case 19:
                return createLongArray();
            case 20:
                return Byte.valueOf(readByte());
            case 21:
                return readSerializable(loader);
            case 22:
                return readSparseBooleanArray();
            case 23:
                return createBooleanArray();
            case 24:
                return readCharSequenceArray();
            case 25:
                return readPersistableBundle(loader);
            case 26:
                return readSize();
            case 27:
                return readSizeF();
            case 28:
                return createDoubleArray();
            default:
                int off = dataPosition() - 4;
                throw new RuntimeException("Parcel " + this + ": Unmarshalling unknown type code " + type + " at offset " + off);
        }
    }

    public final <T extends Parcelable> T readParcelable(ClassLoader loader) {
        Parcelable.Creator<?> creator = readParcelableCreator(loader);
        if (creator == null) {
            return null;
        }
        if (creator instanceof Parcelable.ClassLoaderCreator) {
            Parcelable.ClassLoaderCreator<?> classLoaderCreator = (Parcelable.ClassLoaderCreator) creator;
            return (T) classLoaderCreator.createFromParcel(this, loader);
        }
        return (T) creator.createFromParcel(this);
    }

    private protected final <T extends Parcelable> T readCreator(Parcelable.Creator<?> creator, ClassLoader loader) {
        if (creator instanceof Parcelable.ClassLoaderCreator) {
            Parcelable.ClassLoaderCreator<?> classLoaderCreator = (Parcelable.ClassLoaderCreator) creator;
            return (T) classLoaderCreator.createFromParcel(this, loader);
        }
        return (T) creator.createFromParcel(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Parcelable.Creator<?> readParcelableCreator(ClassLoader loader) {
        Parcelable.Creator<?> creator;
        ClassLoader parcelableClassLoader;
        String name = readString();
        if (name == null) {
            return null;
        }
        synchronized (mCreators) {
            HashMap<String, Parcelable.Creator<?>> map = mCreators.get(loader);
            if (map == null) {
                map = new HashMap<>();
                mCreators.put(loader, map);
            }
            creator = map.get(name);
            if (creator == null) {
                if (loader != null) {
                    parcelableClassLoader = loader;
                } else {
                    try {
                        try {
                            parcelableClassLoader = getClass().getClassLoader();
                        } catch (NoSuchFieldException e) {
                            throw new BadParcelableException("Parcelable protocol requires a Parcelable.Creator object called CREATOR on class " + name);
                        }
                    } catch (ClassNotFoundException e2) {
                        Log.e(TAG, "Class not found when unmarshalling: " + name, e2);
                        throw new BadParcelableException("ClassNotFoundException when unmarshalling: " + name);
                    } catch (IllegalAccessException e3) {
                        Log.e(TAG, "Illegal access when unmarshalling: " + name, e3);
                        throw new BadParcelableException("IllegalAccessException when unmarshalling: " + name);
                    }
                }
                Class<?> parcelableClass = Class.forName(name, false, parcelableClassLoader);
                if (!Parcelable.class.isAssignableFrom(parcelableClass)) {
                    throw new BadParcelableException("Parcelable protocol requires subclassing from Parcelable on class " + name);
                }
                Field f = parcelableClass.getField(MarshalQueryableParcelable.FIELD_CREATOR);
                if ((f.getModifiers() & 8) == 0) {
                    throw new BadParcelableException("Parcelable protocol requires the CREATOR object to be static on class " + name);
                }
                Class<?> creatorType = f.getType();
                if (!Parcelable.Creator.class.isAssignableFrom(creatorType)) {
                    throw new BadParcelableException("Parcelable protocol requires a Parcelable.Creator object called CREATOR on class " + name);
                }
                creator = (Parcelable.Creator) f.get(null);
                if (creator == null) {
                    throw new BadParcelableException("Parcelable protocol requires a non-null Parcelable.Creator object called CREATOR on class " + name);
                }
                map.put(name, creator);
            }
        }
        return creator;
    }

    public final Parcelable[] readParcelableArray(ClassLoader loader) {
        int N = readInt();
        if (N < 0) {
            return null;
        }
        Parcelable[] p = new Parcelable[N];
        for (int i = 0; i < N; i++) {
            p[i] = readParcelable(loader);
        }
        return p;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final synchronized <T extends Parcelable> T[] readParcelableArray(ClassLoader loader, Class<T> clazz) {
        int N = readInt();
        if (N < 0) {
            return null;
        }
        T[] p = (T[]) ((Parcelable[]) Array.newInstance((Class<?>) clazz, N));
        for (int i = 0; i < N; i++) {
            p[i] = readParcelable(loader);
        }
        return p;
    }

    public final Serializable readSerializable() {
        return readSerializable(null);
    }

    private final synchronized Serializable readSerializable(final ClassLoader loader) {
        String name = readString();
        if (name == null) {
            return null;
        }
        byte[] serializedData = createByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
        try {
            ObjectInputStream ois = new ObjectInputStream(bais) { // from class: android.os.Parcel.2
                @Override // java.io.ObjectInputStream
                protected Class<?> resolveClass(ObjectStreamClass osClass) throws IOException, ClassNotFoundException {
                    Class<?> c;
                    if (loader != null && (c = Class.forName(osClass.getName(), false, loader)) != null) {
                        return c;
                    }
                    return super.resolveClass(osClass);
                }
            };
            return (Serializable) ois.readObject();
        } catch (IOException ioe) {
            throw new RuntimeException("Parcelable encountered IOException reading a Serializable object (name = " + name + ")", ioe);
        } catch (ClassNotFoundException cnfe) {
            throw new RuntimeException("Parcelable encountered ClassNotFoundException reading a Serializable object (name = " + name + ")", cnfe);
        }
    }

    protected static final synchronized Parcel obtain(int obj) {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static final synchronized Parcel obtain(long obj) {
        Parcel[] pool = sHolderPool;
        synchronized (pool) {
            for (int i = 0; i < 6; i++) {
                try {
                    Parcel p = pool[i];
                    if (p != null) {
                        pool[i] = null;
                        p.init(obj);
                        return p;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            return new Parcel(obj);
        }
    }

    private synchronized Parcel(long nativePtr) {
        init(nativePtr);
    }

    private synchronized void init(long nativePtr) {
        if (nativePtr != 0) {
            this.mNativePtr = nativePtr;
            this.mOwnsNativeParcelObject = false;
            return;
        }
        this.mNativePtr = nativeCreate();
        this.mOwnsNativeParcelObject = true;
    }

    private synchronized void freeBuffer() {
        if (this.mOwnsNativeParcelObject) {
            updateNativeSize(nativeFreeBuffer(this.mNativePtr));
        }
        this.mReadWriteHelper = ReadWriteHelper.DEFAULT;
    }

    private synchronized void destroy() {
        if (this.mNativePtr != 0) {
            if (this.mOwnsNativeParcelObject) {
                nativeDestroy(this.mNativePtr);
                updateNativeSize(0L);
            }
            this.mNativePtr = 0L;
        }
        this.mReadWriteHelper = null;
    }

    protected void finalize() throws Throwable {
        destroy();
    }

    synchronized void readMapInternal(Map outVal, int N, ClassLoader loader) {
        while (N > 0) {
            Object key = readValue(loader);
            Object value = readValue(loader);
            outVal.put(key, value);
            N--;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void readArrayMapInternal(ArrayMap outVal, int N, ClassLoader loader) {
        while (N > 0) {
            String key = readString();
            Object value = readValue(loader);
            outVal.append(key, value);
            N--;
        }
        outVal.validate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void readArrayMapSafelyInternal(ArrayMap outVal, int N, ClassLoader loader) {
        while (N > 0) {
            String key = readString();
            Object value = readValue(loader);
            outVal.put(key, value);
            N--;
        }
    }

    private protected void readArrayMap(ArrayMap outVal, ClassLoader loader) {
        int N = readInt();
        if (N < 0) {
            return;
        }
        readArrayMapInternal(outVal, N, loader);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArraySet<? extends Object> readArraySet(ClassLoader loader) {
        int size = readInt();
        if (size < 0) {
            return null;
        }
        ArraySet<Object> result = new ArraySet<>(size);
        for (int i = 0; i < size; i++) {
            Object value = readValue(loader);
            result.append(value);
        }
        return result;
    }

    private synchronized void readListInternal(List outVal, int N, ClassLoader loader) {
        while (N > 0) {
            Object value = readValue(loader);
            outVal.add(value);
            N--;
        }
    }

    private synchronized void readArrayInternal(Object[] outVal, int N, ClassLoader loader) {
        for (int i = 0; i < N; i++) {
            Object value = readValue(loader);
            outVal[i] = value;
        }
    }

    private synchronized void readSparseArrayInternal(SparseArray outVal, int N, ClassLoader loader) {
        while (N > 0) {
            int key = readInt();
            Object value = readValue(loader);
            outVal.append(key, value);
            N--;
        }
    }

    private synchronized void readSparseBooleanArrayInternal(SparseBooleanArray outVal, int N) {
        while (N > 0) {
            int key = readInt();
            boolean z = true;
            if (readByte() != 1) {
                z = false;
            }
            boolean value = z;
            outVal.append(key, value);
            N--;
        }
    }

    private synchronized void readSparseIntArrayInternal(SparseIntArray outVal, int N) {
        while (N > 0) {
            int key = readInt();
            int value = readInt();
            outVal.append(key, value);
            N--;
        }
    }

    public synchronized long getBlobAshmemSize() {
        return nativeGetBlobAshmemSize(this.mNativePtr);
    }
}
