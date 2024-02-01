package android.os;

import android.os.IBinder;
import android.util.Log;
import android.util.SparseIntArray;
import com.android.internal.os.BinderInternal;
import java.io.FileDescriptor;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import libcore.util.NativeAllocationRegistry;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Binder.java */
/* loaded from: classes2.dex */
public final class BinderProxy implements IBinder {
    private static final int NATIVE_ALLOCATION_SIZE = 1000;
    private static ProxyMap sProxyMap = new ProxyMap();
    private final long mNativeData;
    volatile boolean mWarnOnBlocking = Binder.sWarnOnBlocking;

    private static native long getNativeFinalizer();

    @Override // android.os.IBinder
    public native String getInterfaceDescriptor() throws RemoteException;

    @Override // android.os.IBinder
    public native boolean isBinderAlive();

    @Override // android.os.IBinder
    public native void linkToDeath(IBinder.DeathRecipient deathRecipient, int i) throws RemoteException;

    @Override // android.os.IBinder
    public native boolean pingBinder();

    public native boolean transactNative(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException;

    @Override // android.os.IBinder
    public native boolean unlinkToDeath(IBinder.DeathRecipient deathRecipient, int i);

    static /* synthetic */ long access$200() {
        return getNativeFinalizer();
    }

    /* compiled from: Binder.java */
    /* loaded from: classes2.dex */
    private static final class ProxyMap {
        private static final int CRASH_AT_SIZE = 20000;
        private static final int LOG_MAIN_INDEX_SIZE = 8;
        private static final int MAIN_INDEX_MASK = 255;
        private static final int MAIN_INDEX_SIZE = 256;
        private static final int WARN_INCREMENT = 10;
        private final Long[][] mMainIndexKeys;
        private final ArrayList<WeakReference<BinderProxy>>[] mMainIndexValues;
        private int mRandom;
        private int mWarnBucketSize;

        private ProxyMap() {
            this.mWarnBucketSize = 20;
            this.mMainIndexKeys = new Long[256];
            this.mMainIndexValues = new ArrayList[256];
        }

        private static int hash(long arg) {
            return ((int) ((arg >> 2) ^ (arg >> 10))) & 255;
        }

        private int size() {
            ArrayList<WeakReference<BinderProxy>>[] arrayListArr;
            int size = 0;
            for (ArrayList<WeakReference<BinderProxy>> a : this.mMainIndexValues) {
                if (a != null) {
                    size += a.size();
                }
            }
            return size;
        }

        private int unclearedSize() {
            ArrayList<WeakReference<BinderProxy>>[] arrayListArr;
            int size = 0;
            for (ArrayList<WeakReference<BinderProxy>> a : this.mMainIndexValues) {
                if (a != null) {
                    Iterator<WeakReference<BinderProxy>> it = a.iterator();
                    while (it.hasNext()) {
                        WeakReference<BinderProxy> ref = it.next();
                        if (ref.get() != null) {
                            size++;
                        }
                    }
                }
            }
            return size;
        }

        private void remove(int hash, int index) {
            Long[] keyArray = this.mMainIndexKeys[hash];
            ArrayList<WeakReference<BinderProxy>> valueArray = this.mMainIndexValues[hash];
            int size = valueArray.size();
            if (index != size - 1) {
                keyArray[index] = keyArray[size - 1];
                valueArray.set(index, valueArray.get(size - 1));
            }
            valueArray.remove(size - 1);
        }

        BinderProxy get(long key) {
            int myHash = hash(key);
            Long[] keyArray = this.mMainIndexKeys[myHash];
            if (keyArray == null) {
                return null;
            }
            ArrayList<WeakReference<BinderProxy>> valueArray = this.mMainIndexValues[myHash];
            int bucketSize = valueArray.size();
            for (int i = 0; i < bucketSize; i++) {
                long foundKey = keyArray[i].longValue();
                if (key == foundKey) {
                    WeakReference<BinderProxy> wr = valueArray.get(i);
                    BinderProxy bp = wr.get();
                    if (bp != null) {
                        return bp;
                    }
                    remove(myHash, i);
                    return null;
                }
            }
            return null;
        }

        void set(long key, BinderProxy value) {
            int myHash = hash(key);
            ArrayList<WeakReference<BinderProxy>> valueArray = this.mMainIndexValues[myHash];
            if (valueArray == null) {
                ArrayList<WeakReference<BinderProxy>>[] arrayListArr = this.mMainIndexValues;
                ArrayList<WeakReference<BinderProxy>> arrayList = new ArrayList<>();
                arrayListArr[myHash] = arrayList;
                valueArray = arrayList;
                this.mMainIndexKeys[myHash] = new Long[1];
            }
            int size = valueArray.size();
            WeakReference<BinderProxy> newWr = new WeakReference<>(value);
            for (int i = 0; i < size; i++) {
                if (valueArray.get(i).get() == null) {
                    valueArray.set(i, newWr);
                    this.mMainIndexKeys[myHash][i] = Long.valueOf(key);
                    if (i < size - 1) {
                        int i2 = this.mRandom + 1;
                        this.mRandom = i2;
                        int rnd = Math.floorMod(i2, size - (i + 1));
                        if (valueArray.get(i + 1 + rnd).get() == null) {
                            remove(myHash, i + 1 + rnd);
                            return;
                        }
                        return;
                    }
                    return;
                }
            }
            valueArray.add(size, newWr);
            Long[] keyArray = this.mMainIndexKeys[myHash];
            if (keyArray.length == size) {
                Long[] newArray = new Long[(size / 2) + size + 2];
                System.arraycopy(keyArray, 0, newArray, 0, size);
                newArray[size] = Long.valueOf(key);
                this.mMainIndexKeys[myHash] = newArray;
            } else {
                keyArray[size] = Long.valueOf(key);
            }
            if (size >= this.mWarnBucketSize) {
                int totalSize = size();
                Log.v("Binder", "BinderProxy map growth! bucket size = " + size + " total = " + totalSize);
                this.mWarnBucketSize = this.mWarnBucketSize + 10;
                if (Build.IS_DEBUGGABLE && totalSize >= 20000) {
                    int totalUnclearedSize = unclearedSize();
                    if (totalUnclearedSize >= 20000) {
                        dumpProxyInterfaceCounts();
                        dumpPerUidProxyCounts();
                        Runtime.getRuntime().gc();
                        throw new AssertionError("Binder ProxyMap has too many entries: " + totalSize + " (total), " + totalUnclearedSize + " (uncleared), " + unclearedSize() + " (uncleared after GC). BinderProxy leak?");
                    } else if (totalSize > (3 * totalUnclearedSize) / 2) {
                        Log.v("Binder", "BinderProxy map has many cleared entries: " + (totalSize - totalUnclearedSize) + " of " + totalSize + " are cleared");
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dumpProxyInterfaceCounts() {
            ArrayList<WeakReference<BinderProxy>>[] arrayListArr;
            String key;
            Map<String, Integer> counts = new HashMap<>();
            for (ArrayList<WeakReference<BinderProxy>> a : this.mMainIndexValues) {
                if (a != null) {
                    Iterator<WeakReference<BinderProxy>> it = a.iterator();
                    while (it.hasNext()) {
                        WeakReference<BinderProxy> weakRef = it.next();
                        BinderProxy bp = weakRef.get();
                        if (bp == null) {
                            key = "<cleared weak-ref>";
                        } else {
                            try {
                                key = bp.getInterfaceDescriptor();
                            } catch (Throwable th) {
                                key = "<exception during getDescriptor>";
                            }
                        }
                        Integer i = counts.get(key);
                        if (i != null) {
                            counts.put(key, Integer.valueOf(i.intValue() + 1));
                        } else {
                            counts.put(key, 1);
                        }
                    }
                }
            }
            Map.Entry<String, Integer>[] sorted = (Map.Entry[]) counts.entrySet().toArray(new Map.Entry[counts.size()]);
            Arrays.sort(sorted, new Comparator() { // from class: android.os.-$$Lambda$BinderProxy$ProxyMap$huB_NMtOmTDIIYkL7mXm-Otlfnw
                @Override // java.util.Comparator
                public final int compare(Object obj, Object obj2) {
                    int compareTo;
                    compareTo = ((Integer) ((Map.Entry) obj2).getValue()).compareTo((Integer) ((Map.Entry) obj).getValue());
                    return compareTo;
                }
            });
            Log.v("Binder", "BinderProxy descriptor histogram (top ten):");
            int printLength = Math.min(10, sorted.length);
            for (int i2 = 0; i2 < printLength; i2++) {
                Log.v("Binder", " #" + (i2 + 1) + ": " + sorted[i2].getKey() + " x" + sorted[i2].getValue());
            }
        }

        private void dumpPerUidProxyCounts() {
            SparseIntArray counts = BinderInternal.nGetBinderProxyPerUidCounts();
            if (counts.size() == 0) {
                return;
            }
            Log.d("Binder", "Per Uid Binder Proxy Counts:");
            for (int i = 0; i < counts.size(); i++) {
                int uid = counts.keyAt(i);
                int binderCount = counts.valueAt(i);
                Log.d("Binder", "UID : " + uid + "  count = " + binderCount);
            }
        }
    }

    private static void dumpProxyDebugInfo() {
        if (Build.IS_DEBUGGABLE) {
            sProxyMap.dumpProxyInterfaceCounts();
        }
    }

    private static BinderProxy getInstance(long nativeData, long iBinder) {
        try {
            BinderProxy result = sProxyMap.get(iBinder);
            if (result != null) {
                return result;
            }
            BinderProxy result2 = new BinderProxy(nativeData);
            NoImagePreloadHolder.sRegistry.registerNativeAllocation(result2, nativeData);
            sProxyMap.set(iBinder, result2);
            return result2;
        } catch (Throwable e) {
            NativeAllocationRegistry.applyFreeFunction(NoImagePreloadHolder.sNativeFinalizer, nativeData);
            throw e;
        }
    }

    private BinderProxy(long nativeData) {
        this.mNativeData = nativeData;
    }

    /* compiled from: Binder.java */
    /* loaded from: classes2.dex */
    private static class NoImagePreloadHolder {
        public static final long sNativeFinalizer = BinderProxy.access$200();
        public static final NativeAllocationRegistry sRegistry = new NativeAllocationRegistry(BinderProxy.class.getClassLoader(), sNativeFinalizer, 1000);

        private NoImagePreloadHolder() {
        }
    }

    @Override // android.os.IBinder
    public IInterface queryLocalInterface(String descriptor) {
        return null;
    }

    @Override // android.os.IBinder
    public boolean transact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        Binder.checkParcel(this, code, data, "Unreasonably large binder buffer");
        if (this.mWarnOnBlocking && (flags & 1) == 0) {
            this.mWarnOnBlocking = false;
            Log.w("Binder", "Outgoing transactions from this process must be FLAG_ONEWAY", new Throwable());
        }
        boolean tracingEnabled = Binder.isTracingEnabled();
        if (tracingEnabled) {
            Throwable tr = new Throwable();
            Binder.getTransactionTracker().addTrace(tr);
            StackTraceElement stackTraceElement = tr.getStackTrace()[1];
            Trace.traceBegin(1L, stackTraceElement.getClassName() + "." + stackTraceElement.getMethodName());
        }
        try {
            return transactNative(code, data, reply, flags);
        } finally {
            if (tracingEnabled) {
                Trace.traceEnd(1L);
            }
        }
    }

    @Override // android.os.IBinder
    public void dump(FileDescriptor fd, String[] args) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeFileDescriptor(fd);
        data.writeStringArray(args);
        try {
            transact(IBinder.DUMP_TRANSACTION, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @Override // android.os.IBinder
    public void dumpAsync(FileDescriptor fd, String[] args) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeFileDescriptor(fd);
        data.writeStringArray(args);
        try {
            transact(IBinder.DUMP_TRANSACTION, data, reply, 1);
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    @Override // android.os.IBinder
    public void shellCommand(FileDescriptor in, FileDescriptor out, FileDescriptor err, String[] args, ShellCallback callback, ResultReceiver resultReceiver) throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeFileDescriptor(in);
        data.writeFileDescriptor(out);
        data.writeFileDescriptor(err);
        data.writeStringArray(args);
        ShellCallback.writeToParcel(callback, data);
        resultReceiver.writeToParcel(data, 0);
        try {
            transact(IBinder.SHELL_COMMAND_TRANSACTION, data, reply, 0);
            reply.readException();
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    private static final void sendDeathNotice(IBinder.DeathRecipient recipient) {
        try {
            recipient.binderDied();
        } catch (RuntimeException exc) {
            Log.w("BinderNative", "Uncaught exception from death notification", exc);
        }
    }
}
