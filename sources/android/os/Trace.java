package android.os;

import android.annotation.UnsupportedAppUsage;
import dalvik.annotation.optimization.FastNative;

/* loaded from: classes2.dex */
public final class Trace {
    private static final int MAX_SECTION_NAME_LEN = 127;
    private static final String TAG = "Trace";
    public static final long TRACE_TAG_ACTIVITY_MANAGER = 64;
    public static final long TRACE_TAG_ADB = 4194304;
    public static final long TRACE_TAG_AIDL = 16777216;
    public static final long TRACE_TAG_ALWAYS = 1;
    @UnsupportedAppUsage
    public static final long TRACE_TAG_APP = 4096;
    public static final long TRACE_TAG_AUDIO = 256;
    public static final long TRACE_TAG_BIONIC = 65536;
    public static final long TRACE_TAG_CAMERA = 1024;
    public static final long TRACE_TAG_DALVIK = 16384;
    public static final long TRACE_TAG_DATABASE = 1048576;
    public static final long TRACE_TAG_GRAPHICS = 2;
    public static final long TRACE_TAG_HAL = 2048;
    public static final long TRACE_TAG_INPUT = 4;
    public static final long TRACE_TAG_NETWORK = 2097152;
    public static final long TRACE_TAG_NEVER = 0;
    public static final long TRACE_TAG_NNAPI = 33554432;
    private static final long TRACE_TAG_NOT_READY = Long.MIN_VALUE;
    public static final long TRACE_TAG_PACKAGE_MANAGER = 262144;
    public static final long TRACE_TAG_POWER = 131072;
    public static final long TRACE_TAG_RESOURCES = 8192;
    public static final long TRACE_TAG_RRO = 67108864;
    public static final long TRACE_TAG_RS = 32768;
    public static final long TRACE_TAG_SYNC_MANAGER = 128;
    public static final long TRACE_TAG_SYSTEM_SERVER = 524288;
    public static final long TRACE_TAG_VIBRATOR = 8388608;
    public static final long TRACE_TAG_VIDEO = 512;
    @UnsupportedAppUsage
    public static final long TRACE_TAG_VIEW = 8;
    public static final long TRACE_TAG_WEBVIEW = 16;
    public static final long TRACE_TAG_WINDOW_MANAGER = 32;
    @UnsupportedAppUsage
    private static volatile long sEnabledTags = Long.MIN_VALUE;
    private static int sZygoteDebugFlags = 0;

    @FastNative
    private static native void nativeAsyncTraceBegin(long j, String str, int i);

    @FastNative
    private static native void nativeAsyncTraceEnd(long j, String str, int i);

    @UnsupportedAppUsage
    private static native long nativeGetEnabledTags();

    private static native void nativeSetAppTracingAllowed(boolean z);

    private static native void nativeSetTracingEnabled(boolean z);

    @FastNative
    private static native void nativeTraceBegin(long j, String str);

    @FastNative
    private static native void nativeTraceCounter(long j, String str, long j2);

    @FastNative
    private static native void nativeTraceEnd(long j);

    static {
        SystemProperties.addChangeCallback(new Runnable() { // from class: android.os.-$$Lambda$Trace$2zLZ-Lc2kAXsVjw_nLYeNhqmGq0
            @Override // java.lang.Runnable
            public final void run() {
                Trace.lambda$static$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$static$0() {
        cacheEnabledTags();
        if ((sZygoteDebugFlags & 256) != 0) {
            traceCounter(1L, "java_debuggable", 1);
        }
    }

    private Trace() {
    }

    private static long cacheEnabledTags() {
        long tags = nativeGetEnabledTags();
        sEnabledTags = tags;
        return tags;
    }

    @UnsupportedAppUsage
    public static boolean isTagEnabled(long traceTag) {
        long tags = sEnabledTags;
        if (tags == Long.MIN_VALUE) {
            tags = cacheEnabledTags();
        }
        return (tags & traceTag) != 0;
    }

    @UnsupportedAppUsage
    public static void traceCounter(long traceTag, String counterName, int counterValue) {
        if (isTagEnabled(traceTag)) {
            nativeTraceCounter(traceTag, counterName, counterValue);
        }
    }

    @UnsupportedAppUsage
    public static void setAppTracingAllowed(boolean allowed) {
        nativeSetAppTracingAllowed(allowed);
        cacheEnabledTags();
    }

    public static void setTracingEnabled(boolean enabled, int debugFlags) {
        nativeSetTracingEnabled(enabled);
        sZygoteDebugFlags = debugFlags;
        cacheEnabledTags();
    }

    @UnsupportedAppUsage
    public static void traceBegin(long traceTag, String methodName) {
        if (isTagEnabled(traceTag)) {
            nativeTraceBegin(traceTag, methodName);
        }
    }

    @UnsupportedAppUsage
    public static void traceEnd(long traceTag) {
        if (isTagEnabled(traceTag)) {
            nativeTraceEnd(traceTag);
        }
    }

    @UnsupportedAppUsage
    public static void asyncTraceBegin(long traceTag, String methodName, int cookie) {
        if (isTagEnabled(traceTag)) {
            nativeAsyncTraceBegin(traceTag, methodName, cookie);
        }
    }

    @UnsupportedAppUsage
    public static void asyncTraceEnd(long traceTag, String methodName, int cookie) {
        if (isTagEnabled(traceTag)) {
            nativeAsyncTraceEnd(traceTag, methodName, cookie);
        }
    }

    public static boolean isEnabled() {
        return isTagEnabled(4096L);
    }

    public static void beginSection(String sectionName) {
        if (isTagEnabled(4096L)) {
            if (sectionName.length() > 127) {
                throw new IllegalArgumentException("sectionName is too long");
            }
            nativeTraceBegin(4096L, sectionName);
        }
    }

    public static void endSection() {
        if (isTagEnabled(4096L)) {
            nativeTraceEnd(4096L);
        }
    }

    public static void beginAsyncSection(String methodName, int cookie) {
        asyncTraceBegin(4096L, methodName, cookie);
    }

    public static void endAsyncSection(String methodName, int cookie) {
        asyncTraceEnd(4096L, methodName, cookie);
    }

    public static void setCounter(String counterName, long counterValue) {
        if (isTagEnabled(4096L)) {
            nativeTraceCounter(4096L, counterName, counterValue);
        }
    }
}
