package android.view.inputmethod;

import android.os.Parcel;
import android.util.Slog;
import java.util.List;
/* loaded from: classes2.dex */
public class InputMethodSubtypeArray {
    private static final String TAG = "InputMethodSubtypeArray";
    private volatile byte[] mCompressedData;
    private final int mCount;
    private volatile int mDecompressedSize;
    private volatile InputMethodSubtype[] mInstance;
    private final Object mLockObject = new Object();

    /* JADX INFO: Access modifiers changed from: private */
    public InputMethodSubtypeArray(List<InputMethodSubtype> subtypes) {
        if (subtypes == null) {
            this.mCount = 0;
            return;
        }
        this.mCount = subtypes.size();
        this.mInstance = (InputMethodSubtype[]) subtypes.toArray(new InputMethodSubtype[this.mCount]);
    }

    public synchronized InputMethodSubtypeArray(Parcel source) {
        this.mCount = source.readInt();
        if (this.mCount > 0) {
            this.mDecompressedSize = source.readInt();
            this.mCompressedData = source.createByteArray();
        }
    }

    public synchronized void writeToParcel(Parcel dest) {
        if (this.mCount == 0) {
            dest.writeInt(this.mCount);
            return;
        }
        byte[] compressedData = this.mCompressedData;
        int decompressedSize = this.mDecompressedSize;
        if (compressedData == null && decompressedSize == 0) {
            synchronized (this.mLockObject) {
                compressedData = this.mCompressedData;
                decompressedSize = this.mDecompressedSize;
                if (compressedData == null && decompressedSize == 0) {
                    byte[] decompressedData = marshall(this.mInstance);
                    compressedData = compress(decompressedData);
                    if (compressedData == null) {
                        decompressedSize = -1;
                        Slog.i(TAG, "Failed to compress data.");
                    } else {
                        decompressedSize = decompressedData.length;
                    }
                    this.mDecompressedSize = decompressedSize;
                    this.mCompressedData = compressedData;
                }
            }
        }
        if (compressedData != null && decompressedSize > 0) {
            dest.writeInt(this.mCount);
            dest.writeInt(decompressedSize);
            dest.writeByteArray(compressedData);
            return;
        }
        Slog.i(TAG, "Unexpected state. Behaving as an empty array.");
        dest.writeInt(0);
    }

    public synchronized InputMethodSubtype get(int index) {
        if (index < 0 || this.mCount <= index) {
            throw new ArrayIndexOutOfBoundsException();
        }
        InputMethodSubtype[] instance = this.mInstance;
        if (instance == null) {
            synchronized (this.mLockObject) {
                instance = this.mInstance;
                if (instance == null) {
                    byte[] decompressedData = decompress(this.mCompressedData, this.mDecompressedSize);
                    this.mCompressedData = null;
                    this.mDecompressedSize = 0;
                    if (decompressedData != null) {
                        instance = unmarshall(decompressedData);
                    } else {
                        Slog.e(TAG, "Failed to decompress data. Returns null as fallback.");
                        instance = new InputMethodSubtype[this.mCount];
                    }
                    this.mInstance = instance;
                }
            }
        }
        return instance[index];
    }

    public synchronized int getCount() {
        return this.mCount;
    }

    private static synchronized byte[] marshall(InputMethodSubtype[] array) {
        Parcel parcel = null;
        try {
            parcel = Parcel.obtain();
            parcel.writeTypedArray(array, 0);
            return parcel.marshall();
        } finally {
            if (parcel != null) {
                parcel.recycle();
            }
        }
    }

    private static synchronized InputMethodSubtype[] unmarshall(byte[] data) {
        Parcel parcel = null;
        try {
            parcel = Parcel.obtain();
            parcel.unmarshall(data, 0, data.length);
            parcel.setDataPosition(0);
            return (InputMethodSubtype[]) parcel.createTypedArray(InputMethodSubtype.CREATOR);
        } finally {
            if (parcel != null) {
                parcel.recycle();
            }
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Found unreachable blocks
        	at jadx.core.dex.visitors.blocks.DominatorTree.sortBlocks(DominatorTree.java:35)
        	at jadx.core.dex.visitors.blocks.DominatorTree.compute(DominatorTree.java:25)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.computeDominators(BlockProcessor.java:202)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:45)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:39)
        */
    private static synchronized byte[] compress(byte[] r6) {
        /*
            r0 = 0
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch: java.lang.Exception -> L37
            r1.<init>()     // Catch: java.lang.Exception -> L37
            java.util.zip.GZIPOutputStream r2 = new java.util.zip.GZIPOutputStream     // Catch: java.lang.Throwable -> L2d
            r2.<init>(r1)     // Catch: java.lang.Throwable -> L2d
            r2.write(r6)     // Catch: java.lang.Throwable -> L20
            r2.finish()     // Catch: java.lang.Throwable -> L20
            byte[] r3 = r1.toByteArray()     // Catch: java.lang.Throwable -> L20
            $closeResource(r0, r2)     // Catch: java.lang.Throwable -> L2d
            $closeResource(r0, r1)     // Catch: java.lang.Exception -> L37
            return r3
        L1d:
            r3 = move-exception
            r4 = r0
            goto L26
        L20:
            r3 = move-exception
            throw r3     // Catch: java.lang.Throwable -> L22
        L22:
            r4 = move-exception
            r5 = r4
            r4 = r3
            r3 = r5
        L26:
            $closeResource(r4, r2)     // Catch: java.lang.Throwable -> L2d
            throw r3     // Catch: java.lang.Throwable -> L2d
        L2a:
            r2 = move-exception
            r3 = r0
            goto L33
        L2d:
            r2 = move-exception
            throw r2     // Catch: java.lang.Throwable -> L2f
        L2f:
            r3 = move-exception
            r5 = r3
            r3 = r2
            r2 = r5
        L33:
            $closeResource(r3, r1)     // Catch: java.lang.Exception -> L37
            throw r2     // Catch: java.lang.Exception -> L37
        L37:
            r1 = move-exception
            java.lang.String r2 = "InputMethodSubtypeArray"
            java.lang.String r3 = "Failed to compress the data."
            android.util.Slog.e(r2, r3, r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodSubtypeArray.compress(byte[]):byte[]");
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 == null) {
            x1.close();
            return;
        }
        try {
            x1.close();
        } catch (Throwable th) {
            x0.addSuppressed(th);
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Found unreachable blocks
        	at jadx.core.dex.visitors.blocks.DominatorTree.sortBlocks(DominatorTree.java:35)
        	at jadx.core.dex.visitors.blocks.DominatorTree.compute(DominatorTree.java:25)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.computeDominators(BlockProcessor.java:202)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:45)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:39)
        */
    private static synchronized byte[] decompress(byte[] r8, int r9) {
        /*
            r0 = 0
            java.io.ByteArrayInputStream r1 = new java.io.ByteArrayInputStream     // Catch: java.lang.Exception -> L49
            r1.<init>(r8)     // Catch: java.lang.Exception -> L49
            java.util.zip.GZIPInputStream r2 = new java.util.zip.GZIPInputStream     // Catch: java.lang.Throwable -> L3f
            r2.<init>(r1)     // Catch: java.lang.Throwable -> L3f
            byte[] r3 = new byte[r9]     // Catch: java.lang.Throwable -> L32
            r4 = 0
        Lf:
            int r5 = r3.length     // Catch: java.lang.Throwable -> L32
            if (r4 >= r5) goto L1d
            int r5 = r3.length     // Catch: java.lang.Throwable -> L32
            int r5 = r5 - r4
            int r6 = r2.read(r3, r4, r5)     // Catch: java.lang.Throwable -> L32
            if (r6 >= 0) goto L1b
            goto L1d
        L1b:
            int r4 = r4 + r6
            goto Lf
        L1d:
            if (r9 == r4) goto L27
        L20:
            $closeResource(r0, r2)     // Catch: java.lang.Throwable -> L3f
            $closeResource(r0, r1)     // Catch: java.lang.Exception -> L49
            return r0
        L27:
            $closeResource(r0, r2)     // Catch: java.lang.Throwable -> L3f
            $closeResource(r0, r1)     // Catch: java.lang.Exception -> L49
            return r3
        L2f:
            r3 = move-exception
            r4 = r0
            goto L38
        L32:
            r3 = move-exception
            throw r3     // Catch: java.lang.Throwable -> L34
        L34:
            r4 = move-exception
            r7 = r4
            r4 = r3
            r3 = r7
        L38:
            $closeResource(r4, r2)     // Catch: java.lang.Throwable -> L3f
            throw r3     // Catch: java.lang.Throwable -> L3f
        L3c:
            r2 = move-exception
            r3 = r0
            goto L45
        L3f:
            r2 = move-exception
            throw r2     // Catch: java.lang.Throwable -> L41
        L41:
            r3 = move-exception
            r7 = r3
            r3 = r2
            r2 = r7
        L45:
            $closeResource(r3, r1)     // Catch: java.lang.Exception -> L49
            throw r2     // Catch: java.lang.Exception -> L49
        L49:
            r1 = move-exception
            java.lang.String r2 = "InputMethodSubtypeArray"
            java.lang.String r3 = "Failed to decompress the data."
            android.util.Slog.e(r2, r3, r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodSubtypeArray.decompress(byte[], int):byte[]");
    }
}
