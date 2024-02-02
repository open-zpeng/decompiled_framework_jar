package android.hardware.usb;

import android.util.Log;
import com.android.internal.util.Preconditions;
import dalvik.system.CloseGuard;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class UsbRequest {
    static final int MAX_USBFS_BUFFER_SIZE = 16384;
    private static final String TAG = "UsbRequest";
    public protected ByteBuffer mBuffer;
    private Object mClientData;
    private UsbDeviceConnection mConnection;
    private UsbEndpoint mEndpoint;
    private boolean mIsUsingNewQueue;
    public protected int mLength;
    public protected long mNativeContext;
    private ByteBuffer mTempBuffer;
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private final Object mLock = new Object();

    private native boolean native_cancel();

    private native void native_close();

    private native int native_dequeue_array(byte[] bArr, int i, boolean z);

    private native int native_dequeue_direct();

    private native boolean native_init(UsbDeviceConnection usbDeviceConnection, int i, int i2, int i3, int i4);

    private native boolean native_queue(ByteBuffer byteBuffer, int i, int i2);

    private native boolean native_queue_array(byte[] bArr, int i, boolean z);

    private native boolean native_queue_direct(ByteBuffer byteBuffer, int i, boolean z);

    public boolean initialize(UsbDeviceConnection connection, UsbEndpoint endpoint) {
        this.mEndpoint = endpoint;
        this.mConnection = (UsbDeviceConnection) Preconditions.checkNotNull(connection, "connection");
        boolean wasInitialized = native_init(connection, endpoint.getAddress(), endpoint.getAttributes(), endpoint.getMaxPacketSize(), endpoint.getInterval());
        if (wasInitialized) {
            this.mCloseGuard.open("close");
        }
        return wasInitialized;
    }

    public void close() {
        if (this.mNativeContext != 0) {
            this.mEndpoint = null;
            this.mConnection = null;
            native_close();
            this.mCloseGuard.close();
        }
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mCloseGuard != null) {
                this.mCloseGuard.warnIfOpen();
            }
            close();
        } finally {
            super.finalize();
        }
    }

    public UsbEndpoint getEndpoint() {
        return this.mEndpoint;
    }

    public Object getClientData() {
        return this.mClientData;
    }

    public void setClientData(Object data) {
        this.mClientData = data;
    }

    @Deprecated
    public boolean queue(ByteBuffer buffer, int length) {
        boolean result;
        boolean out = this.mEndpoint.getDirection() == 0;
        if (this.mConnection.getContext().getApplicationInfo().targetSdkVersion < 28 && length > 16384) {
            length = 16384;
        }
        synchronized (this.mLock) {
            this.mBuffer = buffer;
            this.mLength = length;
            if (buffer.isDirect()) {
                result = native_queue_direct(buffer, length, out);
            } else {
                boolean result2 = buffer.hasArray();
                if (result2) {
                    result = native_queue_array(buffer.array(), length, out);
                } else {
                    throw new IllegalArgumentException("buffer is not direct and has no array");
                }
            }
            if (!result) {
                this.mBuffer = null;
                this.mLength = 0;
            }
        }
        return result;
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0069 A[Catch: all -> 0x00a8, TryCatch #0 {, blocks: (B:12:0x002a, B:14:0x002f, B:31:0x009e, B:15:0x0036, B:17:0x0046, B:18:0x0052, B:24:0x005e, B:26:0x0069, B:28:0x0077, B:29:0x008d, B:30:0x0090), top: B:38:0x002a }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean queue(java.nio.ByteBuffer r9) {
        /*
            r8 = this;
            long r0 = r8.mNativeContext
            r2 = 0
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            r1 = 1
            r2 = 0
            if (r0 == 0) goto Lc
            r0 = r1
            goto Ld
        Lc:
            r0 = r2
        Ld:
            java.lang.String r3 = "request is not initialized"
            com.android.internal.util.Preconditions.checkState(r0, r3)
            boolean r0 = r8.mIsUsingNewQueue
            r0 = r0 ^ r1
            java.lang.String r3 = "this request is currently queued"
            com.android.internal.util.Preconditions.checkState(r0, r3)
            android.hardware.usb.UsbEndpoint r0 = r8.mEndpoint
            int r0 = r0.getDirection()
            if (r0 != 0) goto L26
            r0 = r1
            goto L27
        L26:
            r0 = r2
        L27:
            java.lang.Object r3 = r8.mLock
            monitor-enter(r3)
            r8.mBuffer = r9     // Catch: java.lang.Throwable -> La8
            r4 = 0
            if (r9 != 0) goto L36
            r8.mIsUsingNewQueue = r1     // Catch: java.lang.Throwable -> La8
            boolean r1 = r8.native_queue(r4, r2, r2)     // Catch: java.lang.Throwable -> La8
            goto L9e
        L36:
            android.hardware.usb.UsbDeviceConnection r5 = r8.mConnection     // Catch: java.lang.Throwable -> La8
            android.content.Context r5 = r5.getContext()     // Catch: java.lang.Throwable -> La8
            android.content.pm.ApplicationInfo r5 = r5.getApplicationInfo()     // Catch: java.lang.Throwable -> La8
            int r5 = r5.targetSdkVersion     // Catch: java.lang.Throwable -> La8
            r6 = 28
            if (r5 >= r6) goto L52
            int r5 = r9.remaining()     // Catch: java.lang.Throwable -> La8
            r6 = 16384(0x4000, float:2.2959E-41)
            java.lang.String r7 = "number of remaining bytes"
            com.android.internal.util.Preconditions.checkArgumentInRange(r5, r2, r6, r7)     // Catch: java.lang.Throwable -> La8
        L52:
            boolean r5 = r9.isReadOnly()     // Catch: java.lang.Throwable -> La8
            if (r5 == 0) goto L5d
            if (r0 == 0) goto L5b
            goto L5d
        L5b:
            r5 = r2
            goto L5e
        L5d:
            r5 = r1
        L5e:
            java.lang.String r6 = "buffer can not be read-only when receiving data"
            com.android.internal.util.Preconditions.checkArgument(r5, r6)     // Catch: java.lang.Throwable -> La8
            boolean r5 = r9.isDirect()     // Catch: java.lang.Throwable -> La8
            if (r5 != 0) goto L90
            java.nio.ByteBuffer r5 = r8.mBuffer     // Catch: java.lang.Throwable -> La8
            int r5 = r5.remaining()     // Catch: java.lang.Throwable -> La8
            java.nio.ByteBuffer r5 = java.nio.ByteBuffer.allocateDirect(r5)     // Catch: java.lang.Throwable -> La8
            r8.mTempBuffer = r5     // Catch: java.lang.Throwable -> La8
            if (r0 == 0) goto L8d
            java.nio.ByteBuffer r5 = r8.mBuffer     // Catch: java.lang.Throwable -> La8
            r5.mark()     // Catch: java.lang.Throwable -> La8
            java.nio.ByteBuffer r5 = r8.mTempBuffer     // Catch: java.lang.Throwable -> La8
            java.nio.ByteBuffer r6 = r8.mBuffer     // Catch: java.lang.Throwable -> La8
            r5.put(r6)     // Catch: java.lang.Throwable -> La8
            java.nio.ByteBuffer r5 = r8.mTempBuffer     // Catch: java.lang.Throwable -> La8
            r5.flip()     // Catch: java.lang.Throwable -> La8
            java.nio.ByteBuffer r5 = r8.mBuffer     // Catch: java.lang.Throwable -> La8
            r5.reset()     // Catch: java.lang.Throwable -> La8
        L8d:
            java.nio.ByteBuffer r5 = r8.mTempBuffer     // Catch: java.lang.Throwable -> La8
            r9 = r5
        L90:
            r8.mIsUsingNewQueue = r1     // Catch: java.lang.Throwable -> La8
            int r1 = r9.position()     // Catch: java.lang.Throwable -> La8
            int r5 = r9.remaining()     // Catch: java.lang.Throwable -> La8
            boolean r1 = r8.native_queue(r9, r1, r5)     // Catch: java.lang.Throwable -> La8
        L9e:
            monitor-exit(r3)     // Catch: java.lang.Throwable -> La8
            if (r1 != 0) goto La7
            r8.mIsUsingNewQueue = r2
            r8.mTempBuffer = r4
            r8.mBuffer = r4
        La7:
            return r1
        La8:
            r1 = move-exception
            monitor-exit(r3)     // Catch: java.lang.Throwable -> La8
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.usb.UsbRequest.queue(java.nio.ByteBuffer):boolean");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void dequeue(boolean useBufferOverflowInsteadOfIllegalArg) {
        int bytesTransferred;
        boolean isSend = this.mEndpoint.getDirection() == 0;
        synchronized (this.mLock) {
            if (this.mIsUsingNewQueue) {
                bytesTransferred = native_dequeue_direct();
                this.mIsUsingNewQueue = false;
                if (this.mBuffer != null) {
                    if (this.mTempBuffer == null) {
                        this.mBuffer.position(this.mBuffer.position() + bytesTransferred);
                    } else {
                        this.mTempBuffer.limit(bytesTransferred);
                        if (isSend) {
                            this.mBuffer.position(this.mBuffer.position() + bytesTransferred);
                        } else {
                            this.mBuffer.put(this.mTempBuffer);
                        }
                        this.mTempBuffer = null;
                    }
                }
            } else {
                if (this.mBuffer.isDirect()) {
                    bytesTransferred = native_dequeue_direct();
                } else {
                    bytesTransferred = native_dequeue_array(this.mBuffer.array(), this.mLength, isSend);
                }
                if (bytesTransferred >= 0) {
                    int bytesToStore = Math.min(bytesTransferred, this.mLength);
                    try {
                        this.mBuffer.position(bytesToStore);
                    } catch (IllegalArgumentException e) {
                        if (useBufferOverflowInsteadOfIllegalArg) {
                            Log.e(TAG, "Buffer " + this.mBuffer + " does not have enough space to read " + bytesToStore + " bytes", e);
                            throw new BufferOverflowException();
                        }
                        throw e;
                    }
                }
            }
            this.mBuffer = null;
            this.mLength = 0;
        }
    }

    public boolean cancel() {
        return native_cancel();
    }
}
