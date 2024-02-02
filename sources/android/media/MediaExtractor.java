package android.media;

import android.content.res.AssetFileDescriptor;
import android.media.DrmInitData;
import android.media.MediaCas;
import android.media.MediaCodec;
import android.os.IBinder;
import android.os.IHwBinder;
import android.os.PersistableBundle;
import com.android.internal.util.Preconditions;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/* loaded from: classes.dex */
public final class MediaExtractor {
    public static final int SAMPLE_FLAG_ENCRYPTED = 2;
    public static final int SAMPLE_FLAG_PARTIAL_FRAME = 4;
    public static final int SAMPLE_FLAG_SYNC = 1;
    public static final int SEEK_TO_CLOSEST_SYNC = 2;
    public static final int SEEK_TO_NEXT_SYNC = 1;
    public static final int SEEK_TO_PREVIOUS_SYNC = 0;
    private MediaCas mMediaCas;
    private long mNativeContext;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface SampleFlag {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface SeekMode {
    }

    private native Map<String, Object> getFileFormatNative();

    private native Map<String, Object> getTrackFormatNative(int i);

    private final native void nativeSetDataSource(IBinder iBinder, String str, String[] strArr, String[] strArr2) throws IOException;

    private final native void nativeSetMediaCas(IHwBinder iHwBinder);

    private final native void native_finalize();

    private native PersistableBundle native_getMetrics();

    private static final native void native_init();

    private final native void native_setup();

    public native boolean advance();

    public native long getCachedDuration();

    public native boolean getSampleCryptoInfo(MediaCodec.CryptoInfo cryptoInfo);

    public native int getSampleFlags();

    public native long getSampleSize();

    public native long getSampleTime();

    public native int getSampleTrackIndex();

    public final native int getTrackCount();

    public native boolean hasCacheReachedEndOfStream();

    public native int readSampleData(ByteBuffer byteBuffer, int i);

    public final native void release();

    public native void seekTo(long j, int i);

    public native void selectTrack(int i);

    public final native void setDataSource(MediaDataSource mediaDataSource) throws IOException;

    public final native void setDataSource(FileDescriptor fileDescriptor, long j, long j2) throws IOException;

    public native void unselectTrack(int i);

    public MediaExtractor() {
        native_setup();
    }

    /* JADX WARN: Code restructure failed: missing block: B:26:0x0055, code lost:
        if (0 == 0) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0057, code lost:
        r1.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x005c, code lost:
        if (0 == 0) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x005f, code lost:
        setDataSource(r11.toString(), r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0066, code lost:
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void setDataSource(android.content.Context r10, android.net.Uri r11, java.util.Map<java.lang.String, java.lang.String> r12) throws java.io.IOException {
        /*
            r9 = this;
            java.lang.String r0 = r11.getScheme()
            if (r0 == 0) goto L67
            java.lang.String r1 = "file"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto Lf
            goto L67
        Lf:
            r1 = 0
            android.content.ContentResolver r2 = r10.getContentResolver()     // Catch: java.lang.Throwable -> L4d java.io.IOException -> L54 java.lang.SecurityException -> L5b
            java.lang.String r3 = "r"
            android.content.res.AssetFileDescriptor r3 = r2.openAssetFileDescriptor(r11, r3)     // Catch: java.lang.Throwable -> L4d java.io.IOException -> L54 java.lang.SecurityException -> L5b
            r1 = r3
            if (r1 != 0) goto L24
            if (r1 == 0) goto L23
            r1.close()
        L23:
            return
        L24:
            long r3 = r1.getDeclaredLength()     // Catch: java.lang.Throwable -> L4d java.io.IOException -> L54 java.lang.SecurityException -> L5b
            r5 = 0
            int r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r3 >= 0) goto L36
            java.io.FileDescriptor r3 = r1.getFileDescriptor()     // Catch: java.lang.Throwable -> L4d java.io.IOException -> L54 java.lang.SecurityException -> L5b
            r9.setDataSource(r3)     // Catch: java.lang.Throwable -> L4d java.io.IOException -> L54 java.lang.SecurityException -> L5b
            goto L47
        L36:
            java.io.FileDescriptor r4 = r1.getFileDescriptor()     // Catch: java.lang.Throwable -> L4d java.io.IOException -> L54 java.lang.SecurityException -> L5b
            long r5 = r1.getStartOffset()     // Catch: java.lang.Throwable -> L4d java.io.IOException -> L54 java.lang.SecurityException -> L5b
            long r7 = r1.getDeclaredLength()     // Catch: java.lang.Throwable -> L4d java.io.IOException -> L54 java.lang.SecurityException -> L5b
            r3 = r9
            r3.setDataSource(r4, r5, r7)     // Catch: java.lang.Throwable -> L4d java.io.IOException -> L54 java.lang.SecurityException -> L5b
        L47:
            if (r1 == 0) goto L4c
            r1.close()
        L4c:
            return
        L4d:
            r2 = move-exception
            if (r1 == 0) goto L53
            r1.close()
        L53:
            throw r2
        L54:
            r2 = move-exception
            if (r1 == 0) goto L5f
        L57:
            r1.close()
            goto L5f
        L5b:
            r2 = move-exception
            if (r1 == 0) goto L5f
            goto L57
        L5f:
            java.lang.String r2 = r11.toString()
            r9.setDataSource(r2, r12)
            return
        L67:
            java.lang.String r1 = r11.getPath()
            r9.setDataSource(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaExtractor.setDataSource(android.content.Context, android.net.Uri, java.util.Map):void");
    }

    public final void setDataSource(String path, Map<String, String> headers) throws IOException {
        String[] keys = null;
        String[] values = null;
        if (headers != null) {
            keys = new String[headers.size()];
            values = new String[headers.size()];
            int i = 0;
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                keys[i] = entry.getKey();
                values[i] = entry.getValue();
                i++;
            }
        }
        nativeSetDataSource(MediaHTTPService.createHttpServiceBinderIfNecessary(path), path, keys, values);
    }

    public final void setDataSource(String path) throws IOException {
        nativeSetDataSource(MediaHTTPService.createHttpServiceBinderIfNecessary(path), path, null, null);
    }

    public final void setDataSource(AssetFileDescriptor afd) throws IOException, IllegalArgumentException, IllegalStateException {
        Preconditions.checkNotNull(afd);
        if (afd.getDeclaredLength() < 0) {
            setDataSource(afd.getFileDescriptor());
        } else {
            setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
        }
    }

    public final void setDataSource(FileDescriptor fd) throws IOException {
        setDataSource(fd, 0L, DataSourceDesc.LONG_MAX);
    }

    public final void setMediaCas(MediaCas mediaCas) {
        this.mMediaCas = mediaCas;
        nativeSetMediaCas(mediaCas.getBinder());
    }

    /* loaded from: classes.dex */
    public static final class CasInfo {
        private final MediaCas.Session mSession;
        private final int mSystemId;

        synchronized CasInfo(int systemId, MediaCas.Session session) {
            this.mSystemId = systemId;
            this.mSession = session;
        }

        public int getSystemId() {
            return this.mSystemId;
        }

        public MediaCas.Session getSession() {
            return this.mSession;
        }
    }

    private synchronized ArrayList<Byte> toByteArray(byte[] data) {
        ArrayList<Byte> byteArray = new ArrayList<>(data.length);
        for (int i = 0; i < data.length; i++) {
            byteArray.add(i, Byte.valueOf(data[i]));
        }
        return byteArray;
    }

    public CasInfo getCasInfo(int index) {
        Map<String, Object> formatMap = getTrackFormatNative(index);
        if (formatMap.containsKey(MediaFormat.KEY_CA_SYSTEM_ID)) {
            int systemId = ((Integer) formatMap.get(MediaFormat.KEY_CA_SYSTEM_ID)).intValue();
            MediaCas.Session session = null;
            if (this.mMediaCas != null && formatMap.containsKey(MediaFormat.KEY_CA_SESSION_ID)) {
                ByteBuffer buf = (ByteBuffer) formatMap.get(MediaFormat.KEY_CA_SESSION_ID);
                buf.rewind();
                byte[] sessionId = new byte[buf.remaining()];
                buf.get(sessionId);
                session = this.mMediaCas.createFromSessionId(toByteArray(sessionId));
            }
            return new CasInfo(systemId, session);
        }
        return null;
    }

    protected void finalize() {
        native_finalize();
    }

    public DrmInitData getDrmInitData() {
        Map<String, Object> formatMap = getFileFormatNative();
        if (formatMap == null) {
            return null;
        }
        if (formatMap.containsKey("pssh")) {
            Map<UUID, byte[]> psshMap = getPsshInfo();
            final Map<UUID, DrmInitData.SchemeInitData> initDataMap = new HashMap<>();
            for (Map.Entry<UUID, byte[]> e : psshMap.entrySet()) {
                UUID uuid = e.getKey();
                initDataMap.put(uuid, new DrmInitData.SchemeInitData("cenc", e.getValue()));
            }
            return new DrmInitData() { // from class: android.media.MediaExtractor.1
                @Override // android.media.DrmInitData
                public DrmInitData.SchemeInitData get(UUID schemeUuid) {
                    return (DrmInitData.SchemeInitData) initDataMap.get(schemeUuid);
                }
            };
        }
        int numTracks = getTrackCount();
        for (int i = 0; i < numTracks; i++) {
            Map<String, Object> trackFormatMap = getTrackFormatNative(i);
            if (trackFormatMap.containsKey("crypto-key")) {
                ByteBuffer buf = (ByteBuffer) trackFormatMap.get("crypto-key");
                buf.rewind();
                final byte[] data = new byte[buf.remaining()];
                buf.get(data);
                return new DrmInitData() { // from class: android.media.MediaExtractor.2
                    @Override // android.media.DrmInitData
                    public DrmInitData.SchemeInitData get(UUID schemeUuid) {
                        return new DrmInitData.SchemeInitData("webm", data);
                    }
                };
            }
        }
        return null;
    }

    public List<AudioPresentation> getAudioPresentations(int trackIndex) {
        return new ArrayList();
    }

    public Map<UUID, byte[]> getPsshInfo() {
        Map<UUID, byte[]> psshMap = null;
        Map<String, Object> formatMap = getFileFormatNative();
        if (formatMap != null && formatMap.containsKey("pssh")) {
            ByteBuffer rawpssh = (ByteBuffer) formatMap.get("pssh");
            rawpssh.order(ByteOrder.nativeOrder());
            rawpssh.rewind();
            formatMap.remove("pssh");
            psshMap = new HashMap<>();
            while (rawpssh.remaining() > 0) {
                rawpssh.order(ByteOrder.BIG_ENDIAN);
                long msb = rawpssh.getLong();
                long lsb = rawpssh.getLong();
                UUID uuid = new UUID(msb, lsb);
                rawpssh.order(ByteOrder.nativeOrder());
                int datalen = rawpssh.getInt();
                byte[] psshdata = new byte[datalen];
                rawpssh.get(psshdata);
                psshMap.put(uuid, psshdata);
            }
        }
        return psshMap;
    }

    public MediaFormat getTrackFormat(int index) {
        return new MediaFormat(getTrackFormatNative(index));
    }

    public PersistableBundle getMetrics() {
        PersistableBundle bundle = native_getMetrics();
        return bundle;
    }

    static {
        System.loadLibrary("media_jni");
        native_init();
    }

    /* loaded from: classes.dex */
    public static final class MetricsConstants {
        public static final String FORMAT = "android.media.mediaextractor.fmt";
        public static final String MIME_TYPE = "android.media.mediaextractor.mime";
        public static final String TRACKS = "android.media.mediaextractor.ntrk";

        private synchronized MetricsConstants() {
        }
    }
}
