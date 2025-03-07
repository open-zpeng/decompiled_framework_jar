package android.media;

import android.annotation.UnsupportedAppUsage;
import android.media.MediaCodec;
import dalvik.system.CloseGuard;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.Map;

/* loaded from: classes2.dex */
public final class MediaMuxer {
    private static final int MUXER_STATE_INITIALIZED = 0;
    @UnsupportedAppUsage
    private static final int MUXER_STATE_STARTED = 1;
    @UnsupportedAppUsage
    private static final int MUXER_STATE_STOPPED = 2;
    @UnsupportedAppUsage
    private static final int MUXER_STATE_UNINITIALIZED = -1;
    @UnsupportedAppUsage
    private long mNativeObject;
    @UnsupportedAppUsage
    private int mState = -1;
    @UnsupportedAppUsage
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private int mLastTrackIndex = -1;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface Format {
    }

    private static native int nativeAddTrack(long j, String[] strArr, Object[] objArr);

    @UnsupportedAppUsage
    private static native void nativeRelease(long j);

    private static native void nativeSetLocation(long j, int i, int i2);

    private static native void nativeSetOrientationHint(long j, int i);

    @UnsupportedAppUsage
    private static native long nativeSetup(FileDescriptor fileDescriptor, int i) throws IllegalArgumentException, IOException;

    private static native void nativeStart(long j);

    private static native void nativeStop(long j);

    private static native void nativeWriteSampleData(long j, int i, ByteBuffer byteBuffer, int i2, int i3, long j2, int i4);

    static {
        System.loadLibrary("media_jni");
    }

    /* loaded from: classes2.dex */
    public static final class OutputFormat {
        public static final int MUXER_OUTPUT_3GPP = 2;
        public static final int MUXER_OUTPUT_FIRST = 0;
        public static final int MUXER_OUTPUT_HEIF = 3;
        public static final int MUXER_OUTPUT_LAST = 4;
        public static final int MUXER_OUTPUT_MPEG_4 = 0;
        public static final int MUXER_OUTPUT_OGG = 4;
        public static final int MUXER_OUTPUT_WEBM = 1;

        private OutputFormat() {
        }
    }

    public MediaMuxer(String path, int format) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("path must not be null");
        }
        RandomAccessFile file = null;
        try {
            file = new RandomAccessFile(path, "rws");
            file.setLength(0L);
            FileDescriptor fd = file.getFD();
            setUpMediaMuxer(fd, format);
            file.close();
        } catch (Throwable th) {
            if (file != null) {
                file.close();
            }
            throw th;
        }
    }

    public MediaMuxer(FileDescriptor fd, int format) throws IOException {
        setUpMediaMuxer(fd, format);
    }

    private void setUpMediaMuxer(FileDescriptor fd, int format) throws IOException {
        if (format < 0 || format > 4) {
            throw new IllegalArgumentException("format: " + format + " is invalid");
        }
        this.mNativeObject = nativeSetup(fd, format);
        this.mState = 0;
        this.mCloseGuard.open("release");
    }

    public void setOrientationHint(int degrees) {
        if (degrees != 0 && degrees != 90 && degrees != 180 && degrees != 270) {
            throw new IllegalArgumentException("Unsupported angle: " + degrees);
        } else if (this.mState == 0) {
            nativeSetOrientationHint(this.mNativeObject, degrees);
        } else {
            throw new IllegalStateException("Can't set rotation degrees due to wrong state.");
        }
    }

    public void setLocation(float latitude, float longitude) {
        int latitudex10000 = (int) ((latitude * 10000.0f) + 0.5d);
        int longitudex10000 = (int) ((10000.0f * longitude) + 0.5d);
        if (latitudex10000 > 900000 || latitudex10000 < -900000) {
            String msg = "Latitude: " + latitude + " out of range.";
            throw new IllegalArgumentException(msg);
        } else if (longitudex10000 > 1800000 || longitudex10000 < -1800000) {
            String msg2 = "Longitude: " + longitude + " out of range";
            throw new IllegalArgumentException(msg2);
        } else {
            if (this.mState == 0) {
                long j = this.mNativeObject;
                if (j != 0) {
                    nativeSetLocation(j, latitudex10000, longitudex10000);
                    return;
                }
            }
            throw new IllegalStateException("Can't set location due to wrong state.");
        }
    }

    public void start() {
        long j = this.mNativeObject;
        if (j == 0) {
            throw new IllegalStateException("Muxer has been released!");
        }
        if (this.mState == 0) {
            nativeStart(j);
            this.mState = 1;
            return;
        }
        throw new IllegalStateException("Can't start due to wrong state.");
    }

    public void stop() {
        if (this.mState == 1) {
            nativeStop(this.mNativeObject);
            this.mState = 2;
            return;
        }
        throw new IllegalStateException("Can't stop due to wrong state.");
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mCloseGuard != null) {
                this.mCloseGuard.warnIfOpen();
            }
            if (this.mNativeObject != 0) {
                nativeRelease(this.mNativeObject);
                this.mNativeObject = 0L;
            }
        } finally {
            super.finalize();
        }
    }

    public int addTrack(MediaFormat format) {
        if (format == null) {
            throw new IllegalArgumentException("format must not be null.");
        }
        if (this.mState != 0) {
            throw new IllegalStateException("Muxer is not initialized.");
        }
        if (this.mNativeObject == 0) {
            throw new IllegalStateException("Muxer has been released!");
        }
        Map<String, Object> formatMap = format.getMap();
        int mapSize = formatMap.size();
        if (mapSize > 0) {
            String[] keys = new String[mapSize];
            Object[] values = new Object[mapSize];
            int i = 0;
            for (Map.Entry<String, Object> entry : formatMap.entrySet()) {
                keys[i] = entry.getKey();
                values[i] = entry.getValue();
                i++;
            }
            int trackIndex = nativeAddTrack(this.mNativeObject, keys, values);
            if (this.mLastTrackIndex >= trackIndex) {
                throw new IllegalArgumentException("Invalid format.");
            }
            this.mLastTrackIndex = trackIndex;
            return trackIndex;
        }
        throw new IllegalArgumentException("format must not be empty.");
    }

    public void writeSampleData(int trackIndex, ByteBuffer byteBuf, MediaCodec.BufferInfo bufferInfo) {
        if (trackIndex < 0 || trackIndex > this.mLastTrackIndex) {
            throw new IllegalArgumentException("trackIndex is invalid");
        }
        if (byteBuf == null) {
            throw new IllegalArgumentException("byteBuffer must not be null");
        }
        if (bufferInfo == null) {
            throw new IllegalArgumentException("bufferInfo must not be null");
        }
        if (bufferInfo.size < 0 || bufferInfo.offset < 0 || bufferInfo.offset + bufferInfo.size > byteBuf.capacity() || bufferInfo.presentationTimeUs < 0) {
            throw new IllegalArgumentException("bufferInfo must specify a valid buffer offset, size and presentation time");
        }
        long j = this.mNativeObject;
        if (j == 0) {
            throw new IllegalStateException("Muxer has been released!");
        }
        if (this.mState != 1) {
            throw new IllegalStateException("Can't write, muxer is not started");
        }
        nativeWriteSampleData(j, trackIndex, byteBuf, bufferInfo.offset, bufferInfo.size, bufferInfo.presentationTimeUs, bufferInfo.flags);
    }

    public void release() {
        if (this.mState == 1) {
            stop();
        }
        long j = this.mNativeObject;
        if (j != 0) {
            nativeRelease(j);
            this.mNativeObject = 0L;
            this.mCloseGuard.close();
        }
        this.mState = -1;
    }
}
