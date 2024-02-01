package android.content.pm;

import android.annotation.UnsupportedAppUsage;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class BaseParceledListSlice<T> implements Parcelable {
    private static final int MAX_IPC_SIZE = 65536;
    private int mInlineCountLimit = Integer.MAX_VALUE;
    private final List<T> mList;
    private static String TAG = "ParceledListSlice";
    private static boolean DEBUG = false;

    protected abstract Parcelable.Creator<?> readParcelableCreator(Parcel parcel, ClassLoader classLoader);

    protected abstract void writeElement(T t, Parcel parcel, int i);

    @UnsupportedAppUsage
    protected abstract void writeParcelableCreator(T t, Parcel parcel);

    public BaseParceledListSlice(List<T> list) {
        this.mList = list;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseParceledListSlice(Parcel p, ClassLoader loader) {
        String str;
        String str2;
        List<T> list;
        int N = p.readInt();
        this.mList = new ArrayList(N);
        if (DEBUG) {
            Log.d(TAG, "Retrieving " + N + " items");
        }
        if (N <= 0) {
            return;
        }
        Parcelable.Creator<?> creator = readParcelableCreator(p, loader);
        int i = 0;
        Class<?> listElementClass = null;
        while (true) {
            str = ": ";
            if (i < N && p.readInt() != 0) {
                T parcelable = readCreator(creator, p, loader);
                if (listElementClass == null) {
                    listElementClass = parcelable.getClass();
                } else {
                    verifySameType(listElementClass, parcelable.getClass());
                }
                this.mList.add(parcelable);
                if (DEBUG) {
                    String str3 = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Read inline #");
                    sb.append(i);
                    sb.append(": ");
                    List<T> list2 = this.mList;
                    sb.append(list2.get(list2.size() - 1));
                    Log.d(str3, sb.toString());
                }
                i++;
            }
        }
        if (i >= N) {
            return;
        }
        IBinder retriever = p.readStrongBinder();
        while (i < N) {
            if (DEBUG) {
                Log.d(TAG, "Reading more @" + i + " of " + N + ": retriever=" + retriever);
            }
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            data.writeInt(i);
            try {
                retriever.transact(1, data, reply, 0);
                while (i < N && reply.readInt() != 0) {
                    T parcelable2 = readCreator(creator, reply, loader);
                    verifySameType(listElementClass, parcelable2.getClass());
                    this.mList.add(parcelable2);
                    if (DEBUG) {
                        String str4 = TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Read extra #");
                        sb2.append(i);
                        sb2.append(str);
                        str2 = str;
                        sb2.append(this.mList.get(list.size() - 1));
                        Log.d(str4, sb2.toString());
                    } else {
                        str2 = str;
                    }
                    i++;
                    str = str2;
                }
                reply.recycle();
                data.recycle();
                str = str;
            } catch (RemoteException e) {
                Log.w(TAG, "Failure retrieving array; only received " + i + " of " + N, e);
                return;
            }
        }
    }

    private T readCreator(Parcelable.Creator<?> creator, Parcel p, ClassLoader loader) {
        if (creator instanceof Parcelable.ClassLoaderCreator) {
            Parcelable.ClassLoaderCreator<?> classLoaderCreator = (Parcelable.ClassLoaderCreator) creator;
            return (T) classLoaderCreator.createFromParcel(p, loader);
        }
        return (T) creator.createFromParcel(p);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void verifySameType(Class<?> expected, Class<?> actual) {
        if (!actual.equals(expected)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Can't unparcel type ");
            sb.append(actual.getName());
            sb.append(" in list of type ");
            sb.append(expected == null ? null : expected.getName());
            throw new IllegalArgumentException(sb.toString());
        }
    }

    @UnsupportedAppUsage
    public List<T> getList() {
        return this.mList;
    }

    public void setInlineCountLimit(int maxCount) {
        this.mInlineCountLimit = maxCount;
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0090, code lost:
        r10.writeInt(0);
        r3 = new android.content.pm.BaseParceledListSlice.AnonymousClass1(r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x009a, code lost:
        if (android.content.pm.BaseParceledListSlice.DEBUG == false) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x009c, code lost:
        r5 = android.content.pm.BaseParceledListSlice.TAG;
        android.util.Log.d(r5, "Breaking @" + r4 + " of " + r0 + ": retriever=" + r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x00c2, code lost:
        r10.writeStrongBinder(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x00c5, code lost:
        return;
     */
    @Override // android.os.Parcelable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void writeToParcel(android.os.Parcel r10, final int r11) {
        /*
            r9 = this;
            java.util.List<T> r0 = r9.mList
            int r0 = r0.size()
            r1 = r11
            r10.writeInt(r0)
            boolean r2 = android.content.pm.BaseParceledListSlice.DEBUG
            if (r2 == 0) goto L29
            java.lang.String r2 = android.content.pm.BaseParceledListSlice.TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Writing "
            r3.append(r4)
            r3.append(r0)
            java.lang.String r4 = " items"
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            android.util.Log.d(r2, r3)
        L29:
            if (r0 <= 0) goto Lc5
            java.util.List<T> r2 = r9.mList
            r3 = 0
            java.lang.Object r2 = r2.get(r3)
            java.lang.Class r2 = r2.getClass()
            java.util.List<T> r4 = r9.mList
            java.lang.Object r4 = r4.get(r3)
            r9.writeParcelableCreator(r4, r10)
            r4 = 0
        L40:
            if (r4 >= r0) goto L8e
            int r5 = r9.mInlineCountLimit
            if (r4 >= r5) goto L8e
            int r5 = r10.dataSize()
            r6 = 65536(0x10000, float:9.1835E-41)
            if (r5 >= r6) goto L8e
            r5 = 1
            r10.writeInt(r5)
            java.util.List<T> r5 = r9.mList
            java.lang.Object r5 = r5.get(r4)
            java.lang.Class r6 = r5.getClass()
            verifySameType(r2, r6)
            r9.writeElement(r5, r10, r1)
            boolean r6 = android.content.pm.BaseParceledListSlice.DEBUG
            if (r6 == 0) goto L8a
            java.lang.String r6 = android.content.pm.BaseParceledListSlice.TAG
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "Wrote inline #"
            r7.append(r8)
            r7.append(r4)
            java.lang.String r8 = ": "
            r7.append(r8)
            java.util.List<T> r8 = r9.mList
            java.lang.Object r8 = r8.get(r4)
            r7.append(r8)
            java.lang.String r7 = r7.toString()
            android.util.Log.d(r6, r7)
        L8a:
            int r4 = r4 + 1
            goto L40
        L8e:
            if (r4 >= r0) goto Lc5
            r10.writeInt(r3)
            android.content.pm.BaseParceledListSlice$1 r3 = new android.content.pm.BaseParceledListSlice$1
            r3.<init>()
            boolean r5 = android.content.pm.BaseParceledListSlice.DEBUG
            if (r5 == 0) goto Lc2
            java.lang.String r5 = android.content.pm.BaseParceledListSlice.TAG
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "Breaking @"
            r6.append(r7)
            r6.append(r4)
            java.lang.String r7 = " of "
            r6.append(r7)
            r6.append(r0)
            java.lang.String r7 = ": retriever="
            r6.append(r7)
            r6.append(r3)
            java.lang.String r6 = r6.toString()
            android.util.Log.d(r5, r6)
        Lc2:
            r10.writeStrongBinder(r3)
        Lc5:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.pm.BaseParceledListSlice.writeToParcel(android.os.Parcel, int):void");
    }
}
