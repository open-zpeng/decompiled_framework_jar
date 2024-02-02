package android.view;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.Log;
import android.view.IGraphicsStats;
import android.view.IGraphicsStatsCallback;
import android.view.Surface;
import android.view.View;
import android.view.animation.AnimationUtils;
import com.android.internal.R;
import com.android.internal.util.VirtualRefBasePtr;
import java.io.File;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes2.dex */
public final class ThreadedRenderer {
    private static final String CACHE_PATH_SHADERS = "com.android.opengl.shaders_cache";
    private static final String CACHE_PATH_SKIASHADERS = "com.android.skia.shaders_cache";
    public static final String DEBUG_DIRTY_REGIONS_PROPERTY = "debug.hwui.show_dirty_regions";
    public static final String DEBUG_FPS_DIVISOR = "debug.hwui.fps_divisor";
    public static final String DEBUG_OVERDRAW_PROPERTY = "debug.hwui.overdraw";
    public static final String DEBUG_SHOW_LAYERS_UPDATES_PROPERTY = "debug.hwui.show_layers_updates";
    public static final String DEBUG_SHOW_NON_RECTANGULAR_CLIP_PROPERTY = "debug.hwui.show_non_rect_clip";
    private static final int FLAG_DUMP_ALL = 1;
    private static final int FLAG_DUMP_FRAMESTATS = 1;
    private static final int FLAG_DUMP_RESET = 2;
    private static final String LOG_TAG = "ThreadedRenderer";
    public static final String OVERDRAW_PROPERTY_SHOW = "show";
    static final String PRINT_CONFIG_PROPERTY = "debug.hwui.print_config";
    static final String PROFILE_MAXFRAMES_PROPERTY = "debug.hwui.profile.maxframes";
    public static final String PROFILE_PROPERTY = "debug.hwui.profile";
    public static final String PROFILE_PROPERTY_VISUALIZE_BARS = "visual_bars";
    private static final int SYNC_CONTEXT_IS_STOPPED = 4;
    private static final int SYNC_FRAME_DROPPED = 8;
    private static final int SYNC_INVALIDATE_REQUIRED = 1;
    private static final int SYNC_LOST_SURFACE_REWARD_IF_FOUND = 2;
    private static final int SYNC_OK = 0;
    private static final String[] VISUALIZERS;
    public static boolean sRendererDisabled;
    private static Boolean sSupportsOpenGL;
    public static boolean sSystemRendererDisabled;
    public static boolean sTrimForeground;
    private final int mAmbientShadowAlpha;
    private boolean mEnabled;
    private boolean mHasInsets;
    private int mHeight;
    private int mInsetLeft;
    private int mInsetTop;
    private boolean mIsOpaque;
    private final float mLightRadius;
    private final float mLightY;
    private final float mLightZ;
    private long mNativeProxy;
    private RenderNode mRootNode;
    private boolean mRootNodeNeedsUpdate;
    private final int mSpotShadowAlpha;
    private int mSurfaceHeight;
    private int mSurfaceWidth;
    private int mWidth;
    public static int EGL_CONTEXT_PRIORITY_HIGH_IMG = 12545;
    public static int EGL_CONTEXT_PRIORITY_MEDIUM_IMG = 12546;
    public static int EGL_CONTEXT_PRIORITY_LOW_IMG = 12547;
    private boolean mInitialized = false;
    private boolean mRequested = true;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public interface DrawCallbacks {
        synchronized void onPostDraw(DisplayListCanvas displayListCanvas);

        synchronized void onPreDraw(DisplayListCanvas displayListCanvas);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface DumpFlags {
    }

    /* loaded from: classes2.dex */
    public interface FrameCompleteCallback {
        synchronized void onFrameComplete(long j);
    }

    /* loaded from: classes2.dex */
    public interface FrameDrawingCallback {
        synchronized void onFrameDraw(long j);
    }

    public static native void disableVsync();

    private static native long nAddFrameMetricsObserver(long j, FrameMetricsObserver frameMetricsObserver);

    private static native void nAddRenderNode(long j, long j2, boolean z);

    private static native void nAllocateBuffers(long j, Surface surface);

    private static native void nBuildLayer(long j, long j2);

    private static native void nCancelLayerUpdate(long j, long j2);

    private static native boolean nCopyLayerInto(long j, long j2, Bitmap bitmap);

    private static native int nCopySurfaceInto(Surface surface, int i, int i2, int i3, int i4, Bitmap bitmap);

    private static native float nCountOverDraw(long j);

    private static native Bitmap nCreateHardwareBitmap(long j, int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native long nCreateProxy(boolean z, long j);

    private static native long nCreateRootRenderNode();

    private static native long nCreateTextureLayer(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nDeleteProxy(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nDestroy(long j, long j2);

    private static native void nDestroyHardwareResources(long j);

    private static native void nDetachSurfaceTexture(long j, long j2);

    private static native void nDrawRenderNode(long j, long j2);

    private static native void nDumpProfileInfo(long j, FileDescriptor fileDescriptor, int i);

    private static native void nFence(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int nGetRenderThreadTid(long j);

    private static native void nHackySetRTAnimationsEnabled(boolean z);

    private static native void nInitialize(long j, Surface surface);

    private static native void nInvokeFunctor(long j, boolean z);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nLoadSystemProperties(long j);

    private static native void nNotifyFramePending(long j);

    private static native void nOverrideProperty(String str, String str2);

    private static native boolean nPauseSurface(long j, Surface surface);

    private static native void nPushLayerUpdate(long j, long j2);

    private static native void nRegisterAnimatingRenderNode(long j, long j2);

    private static native void nRegisterVectorDrawableAnimator(long j, long j2);

    private static native void nRemoveFrameMetricsObserver(long j, long j2);

    private static native void nRemoveRenderNode(long j, long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nRotateProcessStatsBuffer();

    private static native void nSerializeDisplayListTree(long j);

    private static native void nSetContentDrawBounds(long j, int i, int i2, int i3, int i4);

    private static native void nSetContextPriority(int i);

    private static native void nSetDebuggingEnabled(boolean z);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nSetFrameCallback(long j, FrameDrawingCallback frameDrawingCallback);

    private static native void nSetFrameCompleteCallback(long j, FrameCompleteCallback frameCompleteCallback);

    private static native void nSetHighContrastText(boolean z);

    private static native void nSetIsolatedProcess(boolean z);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nSetLightCenter(long j, float f, float f2, float f3);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nSetName(long j, String str);

    private static native void nSetOpaque(long j, boolean z);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nSetProcessStatsBuffer(int i);

    private static native void nSetStopped(long j, boolean z);

    private static native void nSetWideGamut(long j, boolean z);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nSetup(long j, float f, int i, int i2);

    private static native void nStopDrawing(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int nSyncAndDrawFrame(long j, long[] jArr, int i);

    private static native void nTrimMemory(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nUpdateSurface(long j, Surface surface);

    static native void setupShadersDiskCache(String str, String str2);

    static /* synthetic */ long access$000() {
        return nCreateRootRenderNode();
    }

    static {
        isAvailable();
        sRendererDisabled = false;
        sSystemRendererDisabled = false;
        sTrimForeground = false;
        VISUALIZERS = new String[]{PROFILE_PROPERTY_VISUALIZE_BARS};
    }

    public static synchronized void disable(boolean system) {
        sRendererDisabled = true;
        if (system) {
            sSystemRendererDisabled = true;
        }
    }

    public static synchronized void enableForegroundTrimming() {
        sTrimForeground = true;
    }

    public static synchronized boolean isAvailable() {
        if (sSupportsOpenGL != null) {
            return sSupportsOpenGL.booleanValue();
        }
        if (SystemProperties.getInt("ro.kernel.qemu", 0) == 0) {
            sSupportsOpenGL = true;
            return true;
        }
        int qemu_gles = SystemProperties.getInt("qemu.gles", -1);
        if (qemu_gles == -1) {
            return false;
        }
        sSupportsOpenGL = Boolean.valueOf(qemu_gles > 0);
        return sSupportsOpenGL.booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setupDiskCache(File cacheDir) {
        setupShadersDiskCache(new File(cacheDir, CACHE_PATH_SHADERS).getAbsolutePath(), new File(cacheDir, CACHE_PATH_SKIASHADERS).getAbsolutePath());
    }

    public static synchronized ThreadedRenderer create(Context context, boolean translucent, String name) {
        if (!isAvailable()) {
            return null;
        }
        ThreadedRenderer renderer = new ThreadedRenderer(context, translucent, name);
        return renderer;
    }

    public static synchronized void trimMemory(int level) {
        nTrimMemory(level);
    }

    public static synchronized void overrideProperty(String name, String value) {
        if (name == null || value == null) {
            throw new IllegalArgumentException("name and value must be non-null");
        }
        nOverrideProperty(name, value);
    }

    synchronized ThreadedRenderer(Context context, boolean translucent, String name) {
        this.mIsOpaque = false;
        TypedArray a = context.obtainStyledAttributes(null, R.styleable.Lighting, 0, 0);
        this.mLightY = a.getDimension(3, 0.0f);
        this.mLightZ = a.getDimension(4, 0.0f);
        this.mLightRadius = a.getDimension(2, 0.0f);
        this.mAmbientShadowAlpha = (int) ((a.getFloat(0, 0.0f) * 255.0f) + 0.5f);
        this.mSpotShadowAlpha = (int) ((255.0f * a.getFloat(1, 0.0f)) + 0.5f);
        a.recycle();
        long rootNodePtr = nCreateRootRenderNode();
        this.mRootNode = RenderNode.adopt(rootNodePtr);
        this.mRootNode.setClipToBounds(false);
        this.mIsOpaque = !translucent;
        this.mNativeProxy = nCreateProxy(translucent, rootNodePtr);
        nSetName(this.mNativeProxy, name);
        ProcessInitializer.sInstance.init(context, this.mNativeProxy);
        loadSystemProperties();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void destroy() {
        this.mInitialized = false;
        updateEnabledState(null);
        nDestroy(this.mNativeProxy, this.mRootNode.mNativeRenderNode);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean isEnabled() {
        return this.mEnabled;
    }

    synchronized void setEnabled(boolean enabled) {
        this.mEnabled = enabled;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean isRequested() {
        return this.mRequested;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setRequested(boolean requested) {
        this.mRequested = requested;
    }

    private synchronized void updateEnabledState(Surface surface) {
        if (surface == null || !surface.isValid()) {
            setEnabled(false);
        } else {
            setEnabled(this.mInitialized);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean initialize(Surface surface) throws Surface.OutOfResourcesException {
        boolean status = !this.mInitialized;
        this.mInitialized = true;
        updateEnabledState(surface);
        nInitialize(this.mNativeProxy, surface);
        return status;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean initializeIfNeeded(int width, int height, View.AttachInfo attachInfo, Surface surface, Rect surfaceInsets) throws Surface.OutOfResourcesException {
        if (isRequested() && !isEnabled() && initialize(surface)) {
            setup(width, height, attachInfo, surfaceInsets);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void updateSurface(Surface surface) throws Surface.OutOfResourcesException {
        updateEnabledState(surface);
        nUpdateSurface(this.mNativeProxy, surface);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean pauseSurface(Surface surface) {
        return nPauseSurface(this.mNativeProxy, surface);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setStopped(boolean stopped) {
        nSetStopped(this.mNativeProxy, stopped);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void destroyHardwareResources(View view) {
        destroyResources(view);
        nDestroyHardwareResources(this.mNativeProxy);
    }

    private static synchronized void destroyResources(View view) {
        view.destroyHardwareResources();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void detachSurfaceTexture(long hardwareLayer) {
        nDetachSurfaceTexture(this.mNativeProxy, hardwareLayer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setup(int width, int height, View.AttachInfo attachInfo, Rect surfaceInsets) {
        this.mWidth = width;
        this.mHeight = height;
        if (surfaceInsets != null && (surfaceInsets.left != 0 || surfaceInsets.right != 0 || surfaceInsets.top != 0 || surfaceInsets.bottom != 0)) {
            this.mHasInsets = true;
            this.mInsetLeft = surfaceInsets.left;
            this.mInsetTop = surfaceInsets.top;
            this.mSurfaceWidth = this.mInsetLeft + width + surfaceInsets.right;
            this.mSurfaceHeight = this.mInsetTop + height + surfaceInsets.bottom;
            setOpaque(false);
        } else {
            this.mHasInsets = false;
            this.mInsetLeft = 0;
            this.mInsetTop = 0;
            this.mSurfaceWidth = width;
            this.mSurfaceHeight = height;
        }
        this.mRootNode.setLeftTopRightBottom(-this.mInsetLeft, -this.mInsetTop, this.mSurfaceWidth, this.mSurfaceHeight);
        nSetup(this.mNativeProxy, this.mLightRadius, this.mAmbientShadowAlpha, this.mSpotShadowAlpha);
        setLightCenter(attachInfo);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setLightCenter(View.AttachInfo attachInfo) {
        Point displaySize = attachInfo.mPoint;
        attachInfo.mDisplay.getRealSize(displaySize);
        float lightX = (displaySize.x / 2.0f) - attachInfo.mWindowLeft;
        float lightY = this.mLightY - attachInfo.mWindowTop;
        nSetLightCenter(this.mNativeProxy, lightX, lightY, this.mLightZ);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setOpaque(boolean opaque) {
        this.mIsOpaque = opaque && !this.mHasInsets;
        nSetOpaque(this.mNativeProxy, this.mIsOpaque);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean isOpaque() {
        return this.mIsOpaque;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setWideGamut(boolean wideGamut) {
        nSetWideGamut(this.mNativeProxy, wideGamut);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int getWidth() {
        return this.mWidth;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized int getHeight() {
        return this.mHeight;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void dumpGfxInfo(PrintWriter pw, FileDescriptor fd, String[] args) {
        char c;
        pw.flush();
        int flags = (args == null || args.length == 0) ? 1 : 0;
        int flags2 = flags;
        for (String str : args) {
            int hashCode = str.hashCode();
            if (hashCode == -252053678) {
                if (str.equals("framestats")) {
                    c = 0;
                }
                c = 65535;
            } else if (hashCode != 1492) {
                if (hashCode == 108404047 && str.equals("reset")) {
                    c = 1;
                }
                c = 65535;
            } else {
                if (str.equals("-a")) {
                    c = 2;
                }
                c = 65535;
            }
            switch (c) {
                case 0:
                    flags2 |= 1;
                    break;
                case 1:
                    flags2 |= 2;
                    break;
                case 2:
                    flags2 = 1;
                    break;
            }
        }
        nDumpProfileInfo(this.mNativeProxy, fd, flags2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean loadSystemProperties() {
        boolean changed = nLoadSystemProperties(this.mNativeProxy);
        if (changed) {
            invalidateRoot();
        }
        return changed;
    }

    private synchronized void updateViewTreeDisplayList(View view) {
        view.mPrivateFlags |= 32;
        view.mRecreateDisplayList = (view.mPrivateFlags & Integer.MIN_VALUE) == Integer.MIN_VALUE;
        view.mPrivateFlags &= Integer.MAX_VALUE;
        view.updateDisplayListIfDirty();
        view.mRecreateDisplayList = false;
    }

    private synchronized void updateRootDisplayList(View view, DrawCallbacks callbacks) {
        Trace.traceBegin(8L, "Record View#draw()");
        updateViewTreeDisplayList(view);
        if (this.mRootNodeNeedsUpdate || !this.mRootNode.isValid()) {
            DisplayListCanvas canvas = this.mRootNode.start(this.mSurfaceWidth, this.mSurfaceHeight);
            try {
                int saveCount = canvas.save();
                canvas.translate(this.mInsetLeft, this.mInsetTop);
                callbacks.onPreDraw(canvas);
                canvas.insertReorderBarrier();
                canvas.drawRenderNode(view.updateDisplayListIfDirty());
                canvas.insertInorderBarrier();
                callbacks.onPostDraw(canvas);
                canvas.restoreToCount(saveCount);
                this.mRootNodeNeedsUpdate = false;
            } finally {
                this.mRootNode.end(canvas);
            }
        }
        Trace.traceEnd(8L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addRenderNode(RenderNode node, boolean placeFront) {
        nAddRenderNode(this.mNativeProxy, node.mNativeRenderNode, placeFront);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeRenderNode(RenderNode node) {
        nRemoveRenderNode(this.mNativeProxy, node.mNativeRenderNode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void drawRenderNode(RenderNode node) {
        nDrawRenderNode(this.mNativeProxy, node.mNativeRenderNode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setContentDrawBounds(int left, int top, int right, int bottom) {
        nSetContentDrawBounds(this.mNativeProxy, left, top, right, bottom);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void invalidateRoot() {
        this.mRootNodeNeedsUpdate = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void draw(View view, View.AttachInfo attachInfo, DrawCallbacks callbacks, FrameDrawingCallback frameDrawingCallback) {
        attachInfo.mIgnoreDirtyState = true;
        Choreographer choreographer = attachInfo.mViewRootImpl.mChoreographer;
        choreographer.mFrameInfo.markDrawStart();
        updateRootDisplayList(view, callbacks);
        attachInfo.mIgnoreDirtyState = false;
        if (attachInfo.mPendingAnimatingRenderNodes != null) {
            int count = attachInfo.mPendingAnimatingRenderNodes.size();
            for (int i = 0; i < count; i++) {
                registerAnimatingRenderNode(attachInfo.mPendingAnimatingRenderNodes.get(i));
            }
            attachInfo.mPendingAnimatingRenderNodes.clear();
            attachInfo.mPendingAnimatingRenderNodes = null;
        }
        long[] frameInfo = choreographer.mFrameInfo.mFrameInfo;
        if (frameDrawingCallback != null) {
            nSetFrameCallback(this.mNativeProxy, frameDrawingCallback);
        }
        int syncResult = nSyncAndDrawFrame(this.mNativeProxy, frameInfo, frameInfo.length);
        if ((syncResult & 2) != 0) {
            setEnabled(false);
            attachInfo.mViewRootImpl.mSurface.release();
            attachInfo.mViewRootImpl.invalidate();
        }
        if ((syncResult & 1) != 0) {
            attachInfo.mViewRootImpl.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setFrameCompleteCallback(FrameCompleteCallback callback) {
        nSetFrameCompleteCallback(this.mNativeProxy, callback);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized void invokeFunctor(long functor, boolean waitForCompletion) {
        nInvokeFunctor(functor, waitForCompletion);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized TextureLayer createTextureLayer() {
        long layer = nCreateTextureLayer(this.mNativeProxy);
        return TextureLayer.adoptTextureLayer(this, layer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void buildLayer(RenderNode node) {
        nBuildLayer(this.mNativeProxy, node.getNativeDisplayList());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean copyLayerInto(TextureLayer layer, Bitmap bitmap) {
        return nCopyLayerInto(this.mNativeProxy, layer.getDeferredLayerUpdater(), bitmap);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void pushLayerUpdate(TextureLayer layer) {
        nPushLayerUpdate(this.mNativeProxy, layer.getDeferredLayerUpdater());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void onLayerDestroyed(TextureLayer layer) {
        nCancelLayerUpdate(this.mNativeProxy, layer.getDeferredLayerUpdater());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void fence() {
        nFence(this.mNativeProxy);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void stopDrawing() {
        nStopDrawing(this.mNativeProxy);
    }

    public synchronized void notifyFramePending() {
        nNotifyFramePending(this.mNativeProxy);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void registerAnimatingRenderNode(RenderNode animator) {
        nRegisterAnimatingRenderNode(this.mRootNode.mNativeRenderNode, animator.mNativeRenderNode);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void registerVectorDrawableAnimator(AnimatedVectorDrawable.VectorDrawableAnimatorRT animator) {
        nRegisterVectorDrawableAnimator(this.mRootNode.mNativeRenderNode, animator.getAnimatorNativePtr());
    }

    public synchronized void serializeDisplayListTree() {
        nSerializeDisplayListTree(this.mNativeProxy);
    }

    public static synchronized int copySurfaceInto(Surface surface, Rect srcRect, Bitmap bitmap) {
        if (srcRect == null) {
            return nCopySurfaceInto(surface, 0, 0, 0, 0, bitmap);
        }
        return nCopySurfaceInto(surface, srcRect.left, srcRect.top, srcRect.right, srcRect.bottom, bitmap);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Bitmap createHardwareBitmap(RenderNode node, int width, int height) {
        return nCreateHardwareBitmap(node.getNativeDisplayList(), width, height);
    }

    public static synchronized void setHighContrastText(boolean highContrastText) {
        nSetHighContrastText(highContrastText);
    }

    public static synchronized void setIsolatedProcess(boolean isIsolated) {
        nSetIsolatedProcess(isIsolated);
    }

    public static synchronized void setDebuggingEnabled(boolean enable) {
        nSetDebuggingEnabled(enable);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void allocateBuffers(Surface surface) {
        nAllocateBuffers(this.mNativeProxy, surface);
    }

    protected void finalize() throws Throwable {
        try {
            nDeleteProxy(this.mNativeProxy);
            this.mNativeProxy = 0L;
        } finally {
            super.finalize();
        }
    }

    /* loaded from: classes2.dex */
    public static class SimpleRenderer {
        private final FrameInfo mFrameInfo = new FrameInfo();
        private final float mLightY;
        private final float mLightZ;
        private long mNativeProxy;
        private final RenderNode mRootNode;
        private Surface mSurface;

        public synchronized SimpleRenderer(Context context, String name, Surface surface) {
            TypedArray a = context.obtainStyledAttributes(null, R.styleable.Lighting, 0, 0);
            this.mLightY = a.getDimension(3, 0.0f);
            this.mLightZ = a.getDimension(4, 0.0f);
            float lightRadius = a.getDimension(2, 0.0f);
            int ambientShadowAlpha = (int) ((a.getFloat(0, 0.0f) * 255.0f) + 0.5f);
            int spotShadowAlpha = (int) ((255.0f * a.getFloat(1, 0.0f)) + 0.5f);
            a.recycle();
            long rootNodePtr = ThreadedRenderer.access$000();
            this.mRootNode = RenderNode.adopt(rootNodePtr);
            this.mRootNode.setClipToBounds(false);
            this.mNativeProxy = ThreadedRenderer.nCreateProxy(true, rootNodePtr);
            ThreadedRenderer.nSetName(this.mNativeProxy, name);
            ProcessInitializer.sInstance.init(context, this.mNativeProxy);
            ThreadedRenderer.nLoadSystemProperties(this.mNativeProxy);
            ThreadedRenderer.nSetup(this.mNativeProxy, lightRadius, ambientShadowAlpha, spotShadowAlpha);
            this.mSurface = surface;
            ThreadedRenderer.nUpdateSurface(this.mNativeProxy, surface);
        }

        public synchronized void setLightCenter(Display display, int windowLeft, int windowTop) {
            Point displaySize = new Point();
            display.getRealSize(displaySize);
            float lightX = (displaySize.x / 2.0f) - windowLeft;
            float lightY = this.mLightY - windowTop;
            ThreadedRenderer.nSetLightCenter(this.mNativeProxy, lightX, lightY, this.mLightZ);
        }

        public synchronized RenderNode getRootNode() {
            return this.mRootNode;
        }

        public synchronized void draw(FrameDrawingCallback callback) {
            long vsync = AnimationUtils.currentAnimationTimeMillis() * 1000000;
            this.mFrameInfo.setVsync(vsync, vsync);
            this.mFrameInfo.addFlags(4L);
            if (callback != null) {
                ThreadedRenderer.nSetFrameCallback(this.mNativeProxy, callback);
            }
            ThreadedRenderer.nSyncAndDrawFrame(this.mNativeProxy, this.mFrameInfo.mFrameInfo, this.mFrameInfo.mFrameInfo.length);
        }

        public synchronized void destroy() {
            this.mSurface = null;
            ThreadedRenderer.nDestroy(this.mNativeProxy, this.mRootNode.mNativeRenderNode);
        }

        protected void finalize() throws Throwable {
            try {
                ThreadedRenderer.nDeleteProxy(this.mNativeProxy);
                this.mNativeProxy = 0L;
            } finally {
                super.finalize();
            }
        }
    }

    /* loaded from: classes2.dex */
    private static class ProcessInitializer {
        static ProcessInitializer sInstance = new ProcessInitializer();
        private Context mAppContext;
        private IGraphicsStats mGraphicsStatsService;
        private boolean mInitialized = false;
        private IGraphicsStatsCallback mGraphicsStatsCallback = new IGraphicsStatsCallback.Stub() { // from class: android.view.ThreadedRenderer.ProcessInitializer.1
            @Override // android.view.IGraphicsStatsCallback
            public void onRotateGraphicsStatsBuffer() throws RemoteException {
                ProcessInitializer.this.rotateBuffer();
            }
        };

        private synchronized ProcessInitializer() {
        }

        synchronized void init(Context context, long renderProxy) {
            if (this.mInitialized) {
                return;
            }
            this.mInitialized = true;
            this.mAppContext = context.getApplicationContext();
            initSched(renderProxy);
            if (this.mAppContext != null) {
                initGraphicsStats();
            }
        }

        private synchronized void initSched(long renderProxy) {
            try {
                int tid = ThreadedRenderer.nGetRenderThreadTid(renderProxy);
                ActivityManager.getService().setRenderThread(tid);
            } catch (Throwable t) {
                Log.w(ThreadedRenderer.LOG_TAG, "Failed to set scheduler for RenderThread", t);
            }
        }

        private synchronized void initGraphicsStats() {
            try {
                IBinder binder = ServiceManager.getService("graphicsstats");
                if (binder == null) {
                    return;
                }
                this.mGraphicsStatsService = IGraphicsStats.Stub.asInterface(binder);
                requestBuffer();
            } catch (Throwable t) {
                Log.w(ThreadedRenderer.LOG_TAG, "Could not acquire gfx stats buffer", t);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void rotateBuffer() {
            ThreadedRenderer.nRotateProcessStatsBuffer();
            requestBuffer();
        }

        private synchronized void requestBuffer() {
            try {
                String pkg = this.mAppContext.getApplicationInfo().packageName;
                ParcelFileDescriptor pfd = this.mGraphicsStatsService.requestBufferForProcess(pkg, this.mGraphicsStatsCallback);
                ThreadedRenderer.nSetProcessStatsBuffer(pfd.getFd());
                pfd.close();
            } catch (Throwable t) {
                Log.w(ThreadedRenderer.LOG_TAG, "Could not acquire gfx stats buffer", t);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void addFrameMetricsObserver(FrameMetricsObserver observer) {
        long nativeObserver = nAddFrameMetricsObserver(this.mNativeProxy, observer);
        observer.mNative = new VirtualRefBasePtr(nativeObserver);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void removeFrameMetricsObserver(FrameMetricsObserver observer) {
        nRemoveFrameMetricsObserver(this.mNativeProxy, observer.mNative.get());
        observer.mNative = null;
    }

    public static synchronized void setFPSDivisor(int divisor) {
        nHackySetRTAnimationsEnabled(divisor <= 1);
    }

    public static synchronized void setContextPriority(int priority) {
        nSetContextPriority(priority);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float countOverDraw() {
        return nCountOverDraw(this.mNativeProxy);
    }
}
