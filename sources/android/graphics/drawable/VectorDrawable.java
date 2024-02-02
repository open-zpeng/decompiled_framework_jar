package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.ComplexColor;
import android.content.res.GradientColor;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Insets;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Trace;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.FloatProperty;
import android.util.IntProperty;
import android.util.Log;
import android.util.PathParser;
import android.util.Property;
import android.util.Xml;
import com.android.internal.R;
import com.android.internal.util.VirtualRefBasePtr;
import dalvik.annotation.optimization.FastNative;
import dalvik.system.VMRuntime;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class VectorDrawable extends Drawable {
    private static final String LOGTAG = VectorDrawable.class.getSimpleName();
    private static final String SHAPE_CLIP_PATH = "clip-path";
    private static final String SHAPE_GROUP = "group";
    private static final String SHAPE_PATH = "path";
    private static final String SHAPE_VECTOR = "vector";
    private ColorFilter mColorFilter;
    private boolean mDpiScaledDirty;
    private int mDpiScaledHeight;
    private Insets mDpiScaledInsets;
    private int mDpiScaledWidth;
    private boolean mMutated;
    private int mTargetDensity;
    public protected PorterDuffColorFilter mTintFilter;
    private final Rect mTmpBounds;
    private VectorDrawableState mVectorState;

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native void nAddChild(long j, long j2);

    @FastNative
    private static native long nCreateClipPath();

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native long nCreateClipPath(long j);

    @FastNative
    private static native long nCreateFullPath();

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native long nCreateFullPath(long j);

    @FastNative
    private static native long nCreateGroup();

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native long nCreateGroup(long j);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native long nCreateTree(long j);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native long nCreateTreeFromCopy(long j, long j2);

    private static native int nDraw(long j, long j2, long j3, Rect rect, boolean z, boolean z2);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native float nGetFillAlpha(long j);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native int nGetFillColor(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nGetFullPathProperties(long j, byte[] bArr, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native boolean nGetGroupProperties(long j, float[] fArr, int i);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native float nGetPivotX(long j);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native float nGetPivotY(long j);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native float nGetRootAlpha(long j);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native float nGetRotation(long j);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native float nGetScaleX(long j);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native float nGetScaleY(long j);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native float nGetStrokeAlpha(long j);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native int nGetStrokeColor(long j);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native float nGetStrokeWidth(long j);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native float nGetTranslateX(long j);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native float nGetTranslateY(long j);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native float nGetTrimPathEnd(long j);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native float nGetTrimPathOffset(long j);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native float nGetTrimPathStart(long j);

    @FastNative
    private static native void nSetAllowCaching(long j, boolean z);

    @FastNative
    private static native void nSetAntiAlias(long j, boolean z);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native void nSetFillAlpha(long j, float f);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native void nSetFillColor(long j, int i);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nSetName(long j, String str);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native void nSetPathData(long j, long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void nSetPathString(long j, String str, int i);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native void nSetPivotX(long j, float f);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native void nSetPivotY(long j, float f);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native void nSetRendererViewportSize(long j, float f, float f2);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native boolean nSetRootAlpha(long j, float f);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native void nSetRotation(long j, float f);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native void nSetScaleX(long j, float f);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native void nSetScaleY(long j, float f);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native void nSetStrokeAlpha(long j, float f);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native void nSetStrokeColor(long j, int i);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native void nSetStrokeWidth(long j, float f);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native void nSetTranslateX(long j, float f);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native void nSetTranslateY(long j, float f);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native void nSetTrimPathEnd(long j, float f);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native void nSetTrimPathOffset(long j, float f);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native void nSetTrimPathStart(long j, float f);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native void nUpdateFullPathFillGradient(long j, long j2);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native void nUpdateFullPathProperties(long j, float f, int i, float f2, int i2, float f3, float f4, float f5, float f6, float f7, int i3, int i4, int i5);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native void nUpdateFullPathStrokeGradient(long j, long j2);

    /* JADX INFO: Access modifiers changed from: private */
    @FastNative
    public static native void nUpdateGroupProperties(long j, float f, float f2, float f3, float f4, float f5, float f6, float f7);

    static /* synthetic */ long access$1700() {
        return nCreateGroup();
    }

    static /* synthetic */ long access$3700() {
        return nCreateClipPath();
    }

    static /* synthetic */ long access$4800() {
        return nCreateFullPath();
    }

    public VectorDrawable() {
        this(new VectorDrawableState(null), null);
    }

    private synchronized VectorDrawable(VectorDrawableState state, Resources res) {
        this.mDpiScaledWidth = 0;
        this.mDpiScaledHeight = 0;
        this.mDpiScaledInsets = Insets.NONE;
        this.mDpiScaledDirty = true;
        this.mTmpBounds = new Rect();
        this.mVectorState = state;
        updateLocalState(res);
    }

    private synchronized void updateLocalState(Resources res) {
        int density = Drawable.resolveDensity(res, this.mVectorState.mDensity);
        if (this.mTargetDensity != density) {
            this.mTargetDensity = density;
            this.mDpiScaledDirty = true;
        }
        this.mTintFilter = updateTintFilter(this.mTintFilter, this.mVectorState.mTint, this.mVectorState.mTintMode);
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mVectorState = new VectorDrawableState(this.mVectorState);
            this.mMutated = true;
        }
        return this;
    }

    @Override // android.graphics.drawable.Drawable
    public synchronized void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }

    public private protected Object getTargetByName(String name) {
        return this.mVectorState.mVGTargetsMap.get(name);
    }

    @Override // android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        this.mVectorState.mChangingConfigurations = getChangingConfigurations();
        return this.mVectorState;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        int deltaInBytes;
        copyBounds(this.mTmpBounds);
        if (this.mTmpBounds.width() <= 0 || this.mTmpBounds.height() <= 0) {
            return;
        }
        ColorFilter colorFilter = this.mColorFilter == null ? this.mTintFilter : this.mColorFilter;
        long colorFilterNativeInstance = colorFilter == null ? 0L : colorFilter.getNativeInstance();
        boolean canReuseCache = this.mVectorState.canReuseCache();
        int pixelCount = nDraw(this.mVectorState.getNativeRenderer(), canvas.getNativeCanvasWrapper(), colorFilterNativeInstance, this.mTmpBounds, needMirroring(), canReuseCache);
        if (pixelCount == 0) {
            return;
        }
        if (canvas.isHardwareAccelerated()) {
            deltaInBytes = (pixelCount - this.mVectorState.mLastHWCachePixelCount) * 4;
            this.mVectorState.mLastHWCachePixelCount = pixelCount;
        } else {
            deltaInBytes = (pixelCount - this.mVectorState.mLastSWCachePixelCount) * 4;
            this.mVectorState.mLastSWCachePixelCount = pixelCount;
        }
        if (deltaInBytes > 0) {
            VMRuntime.getRuntime().registerNativeAllocation(deltaInBytes);
        } else if (deltaInBytes < 0) {
            VMRuntime.getRuntime().registerNativeFree(-deltaInBytes);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return (int) (this.mVectorState.getAlpha() * 255.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int alpha) {
        if (this.mVectorState.setAlpha(alpha / 255.0f)) {
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        this.mColorFilter = colorFilter;
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public ColorFilter getColorFilter() {
        return this.mColorFilter;
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintList(ColorStateList tint) {
        VectorDrawableState state = this.mVectorState;
        if (state.mTint != tint) {
            state.mTint = tint;
            this.mTintFilter = updateTintFilter(this.mTintFilter, tint, state.mTintMode);
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setTintMode(PorterDuff.Mode tintMode) {
        VectorDrawableState state = this.mVectorState;
        if (state.mTintMode != tintMode) {
            state.mTintMode = tintMode;
            this.mTintFilter = updateTintFilter(this.mTintFilter, state.mTint, tintMode);
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        return super.isStateful() || (this.mVectorState != null && this.mVectorState.isStateful());
    }

    @Override // android.graphics.drawable.Drawable
    public boolean hasFocusStateSpecified() {
        return this.mVectorState != null && this.mVectorState.hasFocusStateSpecified();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.drawable.Drawable
    public boolean onStateChange(int[] stateSet) {
        boolean changed = false;
        if (isStateful()) {
            mutate();
        }
        VectorDrawableState state = this.mVectorState;
        if (state.onStateChange(stateSet)) {
            changed = true;
            state.mCacheDirty = true;
        }
        if (state.mTint != null && state.mTintMode != null) {
            this.mTintFilter = updateTintFilter(this.mTintFilter, state.mTint, state.mTintMode);
            return true;
        }
        return changed;
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return getAlpha() == 0 ? -2 : -3;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        if (this.mDpiScaledDirty) {
            computeVectorSize();
        }
        return this.mDpiScaledWidth;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        if (this.mDpiScaledDirty) {
            computeVectorSize();
        }
        return this.mDpiScaledHeight;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Insets getOpticalInsets() {
        if (this.mDpiScaledDirty) {
            computeVectorSize();
        }
        return this.mDpiScaledInsets;
    }

    synchronized void computeVectorSize() {
        Insets opticalInsets = this.mVectorState.mOpticalInsets;
        int sourceDensity = this.mVectorState.mDensity;
        int targetDensity = this.mTargetDensity;
        if (targetDensity != sourceDensity) {
            this.mDpiScaledWidth = Drawable.scaleFromDensity(this.mVectorState.mBaseWidth, sourceDensity, targetDensity, true);
            this.mDpiScaledHeight = Drawable.scaleFromDensity(this.mVectorState.mBaseHeight, sourceDensity, targetDensity, true);
            int left = Drawable.scaleFromDensity(opticalInsets.left, sourceDensity, targetDensity, false);
            int right = Drawable.scaleFromDensity(opticalInsets.right, sourceDensity, targetDensity, false);
            int top = Drawable.scaleFromDensity(opticalInsets.top, sourceDensity, targetDensity, false);
            int bottom = Drawable.scaleFromDensity(opticalInsets.bottom, sourceDensity, targetDensity, false);
            this.mDpiScaledInsets = Insets.of(left, top, right, bottom);
        } else {
            this.mDpiScaledWidth = this.mVectorState.mBaseWidth;
            this.mDpiScaledHeight = this.mVectorState.mBaseHeight;
            this.mDpiScaledInsets = opticalInsets;
        }
        this.mDpiScaledDirty = false;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean canApplyTheme() {
        return (this.mVectorState != null && this.mVectorState.canApplyTheme()) || super.canApplyTheme();
    }

    @Override // android.graphics.drawable.Drawable
    public void applyTheme(Resources.Theme t) {
        super.applyTheme(t);
        VectorDrawableState state = this.mVectorState;
        if (state == null) {
            return;
        }
        boolean changedDensity = this.mVectorState.setDensity(Drawable.resolveDensity(t.getResources(), 0));
        this.mDpiScaledDirty |= changedDensity;
        if (state.mThemeAttrs != null) {
            TypedArray a = t.resolveAttributes(state.mThemeAttrs, R.styleable.VectorDrawable);
            try {
                try {
                    state.mCacheDirty = true;
                    updateStateFromTypedArray(a);
                    a.recycle();
                    this.mDpiScaledDirty = true;
                } catch (XmlPullParserException e) {
                    throw new RuntimeException(e);
                }
            } catch (Throwable th) {
                a.recycle();
                throw th;
            }
        }
        if (state.mTint != null && state.mTint.canApplyTheme()) {
            state.mTint = state.mTint.mo19obtainForTheme(t);
        }
        if (this.mVectorState != null && this.mVectorState.canApplyTheme()) {
            this.mVectorState.applyTheme(t);
        }
        updateLocalState(t.getResources());
    }

    public synchronized float getPixelSize() {
        if (this.mVectorState == null || this.mVectorState.mBaseWidth == 0 || this.mVectorState.mBaseHeight == 0 || this.mVectorState.mViewportHeight == 0.0f || this.mVectorState.mViewportWidth == 0.0f) {
            return 1.0f;
        }
        float intrinsicWidth = this.mVectorState.mBaseWidth;
        float intrinsicHeight = this.mVectorState.mBaseHeight;
        float viewportWidth = this.mVectorState.mViewportWidth;
        float viewportHeight = this.mVectorState.mViewportHeight;
        float scaleX = viewportWidth / intrinsicWidth;
        float scaleY = viewportHeight / intrinsicHeight;
        return Math.min(scaleX, scaleY);
    }

    public static synchronized VectorDrawable create(Resources resources, int rid) {
        int type;
        try {
            XmlPullParser parser = resources.getXml(rid);
            AttributeSet attrs = Xml.asAttributeSet(parser);
            while (true) {
                type = parser.next();
                if (type == 2 || type == 1) {
                    break;
                }
            }
            if (type != 2) {
                throw new XmlPullParserException("No start tag found");
            }
            VectorDrawable drawable = new VectorDrawable();
            drawable.inflate(resources, parser, attrs);
            return drawable;
        } catch (IOException e) {
            Log.e(LOGTAG, "parser error", e);
            return null;
        } catch (XmlPullParserException e2) {
            Log.e(LOGTAG, "parser error", e2);
            return null;
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        try {
            Trace.traceBegin(8192L, "VectorDrawable#inflate");
            if (this.mVectorState.mRootGroup != null || this.mVectorState.mNativeTree != null) {
                if (this.mVectorState.mRootGroup != null) {
                    VMRuntime.getRuntime().registerNativeFree(this.mVectorState.mRootGroup.getNativeSize());
                    this.mVectorState.mRootGroup.setTree(null);
                }
                this.mVectorState.mRootGroup = new VGroup();
                if (this.mVectorState.mNativeTree != null) {
                    VMRuntime.getRuntime().registerNativeFree(316);
                    this.mVectorState.mNativeTree.release();
                }
                this.mVectorState.createNativeTree(this.mVectorState.mRootGroup);
            }
            VectorDrawableState state = this.mVectorState;
            state.setDensity(Drawable.resolveDensity(r, 0));
            TypedArray a = obtainAttributes(r, theme, attrs, R.styleable.VectorDrawable);
            updateStateFromTypedArray(a);
            a.recycle();
            this.mDpiScaledDirty = true;
            state.mCacheDirty = true;
            inflateChildElements(r, parser, attrs, theme);
            state.onTreeConstructionFinished();
            updateLocalState(r);
        } finally {
            Trace.traceEnd(8192L);
        }
    }

    private synchronized void updateStateFromTypedArray(TypedArray a) throws XmlPullParserException {
        VectorDrawableState state = this.mVectorState;
        state.mChangingConfigurations |= a.getChangingConfigurations();
        state.mThemeAttrs = a.extractThemeAttrs();
        int tintMode = a.getInt(6, -1);
        if (tintMode != -1) {
            state.mTintMode = Drawable.parseTintMode(tintMode, PorterDuff.Mode.SRC_IN);
        }
        ColorStateList tint = a.getColorStateList(1);
        if (tint != null) {
            state.mTint = tint;
        }
        state.mAutoMirrored = a.getBoolean(5, state.mAutoMirrored);
        float viewportWidth = a.getFloat(7, state.mViewportWidth);
        float viewportHeight = a.getFloat(8, state.mViewportHeight);
        state.setViewportSize(viewportWidth, viewportHeight);
        if (state.mViewportWidth <= 0.0f) {
            throw new XmlPullParserException(a.getPositionDescription() + "<vector> tag requires viewportWidth > 0");
        } else if (state.mViewportHeight <= 0.0f) {
            throw new XmlPullParserException(a.getPositionDescription() + "<vector> tag requires viewportHeight > 0");
        } else {
            state.mBaseWidth = a.getDimensionPixelSize(3, state.mBaseWidth);
            state.mBaseHeight = a.getDimensionPixelSize(2, state.mBaseHeight);
            if (state.mBaseWidth <= 0) {
                throw new XmlPullParserException(a.getPositionDescription() + "<vector> tag requires width > 0");
            } else if (state.mBaseHeight <= 0) {
                throw new XmlPullParserException(a.getPositionDescription() + "<vector> tag requires height > 0");
            } else {
                int insetLeft = a.getDimensionPixelOffset(10, state.mOpticalInsets.left);
                int insetTop = a.getDimensionPixelOffset(12, state.mOpticalInsets.top);
                int insetRight = a.getDimensionPixelOffset(11, state.mOpticalInsets.right);
                int insetBottom = a.getDimensionPixelOffset(9, state.mOpticalInsets.bottom);
                state.mOpticalInsets = Insets.of(insetLeft, insetTop, insetRight, insetBottom);
                float alphaInFloat = a.getFloat(4, state.getAlpha());
                state.setAlpha(alphaInFloat);
                String name = a.getString(0);
                if (name != null) {
                    state.mRootName = name;
                    state.mVGTargetsMap.put(name, state);
                }
            }
        }
    }

    private synchronized void inflateChildElements(Resources res, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        VectorDrawableState state = this.mVectorState;
        boolean noPathTag = true;
        Stack<VGroup> groupStack = new Stack<>();
        groupStack.push(state.mRootGroup);
        int eventType = parser.getEventType();
        int innerDepth = parser.getDepth() + 1;
        while (eventType != 1 && (parser.getDepth() >= innerDepth || eventType != 3)) {
            if (eventType == 2) {
                String tagName = parser.getName();
                VGroup currentGroup = groupStack.peek();
                if (SHAPE_PATH.equals(tagName)) {
                    VFullPath path = new VFullPath();
                    path.inflate(res, attrs, theme);
                    currentGroup.addChild(path);
                    if (path.getPathName() != null) {
                        state.mVGTargetsMap.put(path.getPathName(), path);
                    }
                    noPathTag = false;
                    state.mChangingConfigurations |= path.mChangingConfigurations;
                } else if (SHAPE_CLIP_PATH.equals(tagName)) {
                    VClipPath path2 = new VClipPath();
                    path2.inflate(res, attrs, theme);
                    currentGroup.addChild(path2);
                    if (path2.getPathName() != null) {
                        state.mVGTargetsMap.put(path2.getPathName(), path2);
                    }
                    state.mChangingConfigurations |= path2.mChangingConfigurations;
                } else if ("group".equals(tagName)) {
                    VGroup newChildGroup = new VGroup();
                    newChildGroup.inflate(res, attrs, theme);
                    currentGroup.addChild(newChildGroup);
                    groupStack.push(newChildGroup);
                    if (newChildGroup.getGroupName() != null) {
                        state.mVGTargetsMap.put(newChildGroup.getGroupName(), newChildGroup);
                    }
                    state.mChangingConfigurations |= newChildGroup.mChangingConfigurations;
                }
            } else if (eventType == 3 && "group".equals(parser.getName())) {
                groupStack.pop();
            }
            eventType = parser.next();
        }
        if (noPathTag) {
            StringBuffer tag = new StringBuffer();
            if (tag.length() > 0) {
                tag.append(" or ");
            }
            tag.append(SHAPE_PATH);
            throw new XmlPullParserException("no " + ((Object) tag) + " defined");
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mVectorState.getChangingConfigurations();
    }

    public private protected void setAllowCaching(boolean allowCaching) {
        nSetAllowCaching(this.mVectorState.getNativeRenderer(), allowCaching);
    }

    private synchronized boolean needMirroring() {
        return isAutoMirrored() && getLayoutDirection() == 1;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAutoMirrored(boolean mirrored) {
        if (this.mVectorState.mAutoMirrored != mirrored) {
            this.mVectorState.mAutoMirrored = mirrored;
            invalidateSelf();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isAutoMirrored() {
        return this.mVectorState.mAutoMirrored;
    }

    public synchronized long getNativeTree() {
        return this.mVectorState.getNativeRenderer();
    }

    public synchronized void setAntiAlias(boolean aa) {
        nSetAntiAlias(this.mVectorState.mNativeTree.get(), aa);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class VectorDrawableState extends Drawable.ConstantState {
        static final Property<VectorDrawableState, Float> ALPHA = new FloatProperty<VectorDrawableState>("alpha") { // from class: android.graphics.drawable.VectorDrawable.VectorDrawableState.1
            @Override // android.util.FloatProperty
            public void setValue(VectorDrawableState state, float value) {
                state.setAlpha(value);
            }

            @Override // android.util.Property
            public Float get(VectorDrawableState state) {
                return Float.valueOf(state.getAlpha());
            }
        };
        private static final int NATIVE_ALLOCATION_SIZE = 316;
        boolean mAutoMirrored;
        int mBaseHeight;
        int mBaseWidth;
        boolean mCacheDirty;
        boolean mCachedAutoMirrored;
        int[] mCachedThemeAttrs;
        ColorStateList mCachedTint;
        PorterDuff.Mode mCachedTintMode;
        int mChangingConfigurations;
        int mDensity;
        Insets mOpticalInsets;
        VGroup mRootGroup;
        String mRootName;
        int[] mThemeAttrs;
        ColorStateList mTint;
        PorterDuff.Mode mTintMode;
        float mViewportWidth = 0.0f;
        float mViewportHeight = 0.0f;
        VirtualRefBasePtr mNativeTree = null;
        final ArrayMap<String, Object> mVGTargetsMap = new ArrayMap<>();
        int mLastSWCachePixelCount = 0;
        int mLastHWCachePixelCount = 0;
        private int mAllocationOfAllNodes = 0;

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized Property getProperty(String propertyName) {
            if (ALPHA.getName().equals(propertyName)) {
                return ALPHA;
            }
            return null;
        }

        public synchronized VectorDrawableState(VectorDrawableState copy) {
            this.mTint = null;
            this.mTintMode = Drawable.DEFAULT_TINT_MODE;
            this.mBaseWidth = 0;
            this.mBaseHeight = 0;
            this.mOpticalInsets = Insets.NONE;
            this.mRootName = null;
            this.mDensity = 160;
            if (copy != null) {
                this.mThemeAttrs = copy.mThemeAttrs;
                this.mChangingConfigurations = copy.mChangingConfigurations;
                this.mTint = copy.mTint;
                this.mTintMode = copy.mTintMode;
                this.mAutoMirrored = copy.mAutoMirrored;
                this.mRootGroup = new VGroup(copy.mRootGroup, this.mVGTargetsMap);
                createNativeTreeFromCopy(copy, this.mRootGroup);
                this.mBaseWidth = copy.mBaseWidth;
                this.mBaseHeight = copy.mBaseHeight;
                setViewportSize(copy.mViewportWidth, copy.mViewportHeight);
                this.mOpticalInsets = copy.mOpticalInsets;
                this.mRootName = copy.mRootName;
                this.mDensity = copy.mDensity;
                if (copy.mRootName != null) {
                    this.mVGTargetsMap.put(copy.mRootName, this);
                }
            } else {
                this.mRootGroup = new VGroup();
                createNativeTree(this.mRootGroup);
            }
            onTreeConstructionFinished();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void createNativeTree(VGroup rootGroup) {
            this.mNativeTree = new VirtualRefBasePtr(VectorDrawable.nCreateTree(rootGroup.mNativePtr));
            VMRuntime.getRuntime().registerNativeAllocation(316);
        }

        private synchronized void createNativeTreeFromCopy(VectorDrawableState copy, VGroup rootGroup) {
            this.mNativeTree = new VirtualRefBasePtr(VectorDrawable.nCreateTreeFromCopy(copy.mNativeTree.get(), rootGroup.mNativePtr));
            VMRuntime.getRuntime().registerNativeAllocation(316);
        }

        synchronized void onTreeConstructionFinished() {
            this.mRootGroup.setTree(this.mNativeTree);
            this.mAllocationOfAllNodes = this.mRootGroup.getNativeSize();
            VMRuntime.getRuntime().registerNativeAllocation(this.mAllocationOfAllNodes);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized long getNativeRenderer() {
            if (this.mNativeTree == null) {
                return 0L;
            }
            return this.mNativeTree.get();
        }

        public synchronized boolean canReuseCache() {
            if (!this.mCacheDirty && this.mCachedThemeAttrs == this.mThemeAttrs && this.mCachedTint == this.mTint && this.mCachedTintMode == this.mTintMode && this.mCachedAutoMirrored == this.mAutoMirrored) {
                return true;
            }
            updateCacheStates();
            return false;
        }

        public synchronized void updateCacheStates() {
            this.mCachedThemeAttrs = this.mThemeAttrs;
            this.mCachedTint = this.mTint;
            this.mCachedTintMode = this.mTintMode;
            this.mCachedAutoMirrored = this.mAutoMirrored;
            this.mCacheDirty = false;
        }

        public synchronized void applyTheme(Resources.Theme t) {
            this.mRootGroup.applyTheme(t);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public boolean canApplyTheme() {
            return this.mThemeAttrs != null || (this.mRootGroup != null && this.mRootGroup.canApplyTheme()) || ((this.mTint != null && this.mTint.canApplyTheme()) || super.canApplyTheme());
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            return new VectorDrawable(this, null);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources res) {
            return new VectorDrawable(this, res);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return this.mChangingConfigurations | (this.mTint != null ? this.mTint.getChangingConfigurations() : 0);
        }

        public synchronized boolean isStateful() {
            return (this.mTint != null && this.mTint.isStateful()) || (this.mRootGroup != null && this.mRootGroup.isStateful());
        }

        public synchronized boolean hasFocusStateSpecified() {
            return (this.mTint != null && this.mTint.hasFocusStateSpecified()) || (this.mRootGroup != null && this.mRootGroup.hasFocusStateSpecified());
        }

        synchronized void setViewportSize(float viewportWidth, float viewportHeight) {
            this.mViewportWidth = viewportWidth;
            this.mViewportHeight = viewportHeight;
            VectorDrawable.nSetRendererViewportSize(getNativeRenderer(), viewportWidth, viewportHeight);
        }

        public final synchronized boolean setDensity(int targetDensity) {
            if (this.mDensity != targetDensity) {
                int sourceDensity = this.mDensity;
                this.mDensity = targetDensity;
                applyDensityScaling(sourceDensity, targetDensity);
                return true;
            }
            return false;
        }

        private synchronized void applyDensityScaling(int sourceDensity, int targetDensity) {
            this.mBaseWidth = Drawable.scaleFromDensity(this.mBaseWidth, sourceDensity, targetDensity, true);
            this.mBaseHeight = Drawable.scaleFromDensity(this.mBaseHeight, sourceDensity, targetDensity, true);
            int insetLeft = Drawable.scaleFromDensity(this.mOpticalInsets.left, sourceDensity, targetDensity, false);
            int insetTop = Drawable.scaleFromDensity(this.mOpticalInsets.top, sourceDensity, targetDensity, false);
            int insetRight = Drawable.scaleFromDensity(this.mOpticalInsets.right, sourceDensity, targetDensity, false);
            int insetBottom = Drawable.scaleFromDensity(this.mOpticalInsets.bottom, sourceDensity, targetDensity, false);
            this.mOpticalInsets = Insets.of(insetLeft, insetTop, insetRight, insetBottom);
        }

        public synchronized boolean onStateChange(int[] stateSet) {
            return this.mRootGroup.onStateChange(stateSet);
        }

        public void finalize() throws Throwable {
            super.finalize();
            int bitmapCacheSize = (this.mLastHWCachePixelCount * 4) + (this.mLastSWCachePixelCount * 4);
            VMRuntime.getRuntime().registerNativeFree(316 + this.mAllocationOfAllNodes + bitmapCacheSize);
        }

        public synchronized boolean setAlpha(float alpha) {
            return VectorDrawable.nSetRootAlpha(this.mNativeTree.get(), alpha);
        }

        public synchronized float getAlpha() {
            return VectorDrawable.nGetRootAlpha(this.mNativeTree.get());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class VGroup extends VObject {
        private static final int NATIVE_ALLOCATION_SIZE = 100;
        private static final int PIVOT_X_INDEX = 1;
        private static final int PIVOT_Y_INDEX = 2;
        private static final int ROTATION_INDEX = 0;
        private static final int SCALE_X_INDEX = 3;
        private static final int SCALE_Y_INDEX = 4;
        private static final int TRANSFORM_PROPERTY_COUNT = 7;
        private static final int TRANSLATE_X_INDEX = 5;
        private static final int TRANSLATE_Y_INDEX = 6;
        private int mChangingConfigurations;
        private final ArrayList<VObject> mChildren;
        private String mGroupName;
        private boolean mIsStateful;
        private final long mNativePtr;
        private int[] mThemeAttrs;
        private float[] mTransform;
        private static final HashMap<String, Integer> sPropertyIndexMap = new HashMap<String, Integer>() { // from class: android.graphics.drawable.VectorDrawable.VGroup.1
            {
                put("translateX", 5);
                put("translateY", 6);
                put("scaleX", 3);
                put("scaleY", 4);
                put("pivotX", 1);
                put("pivotY", 2);
                put("rotation", 0);
            }
        };
        private static final Property<VGroup, Float> TRANSLATE_X = new FloatProperty<VGroup>("translateX") { // from class: android.graphics.drawable.VectorDrawable.VGroup.2
            @Override // android.util.FloatProperty
            public void setValue(VGroup object, float value) {
                object.setTranslateX(value);
            }

            @Override // android.util.Property
            public Float get(VGroup object) {
                return Float.valueOf(object.getTranslateX());
            }
        };
        private static final Property<VGroup, Float> TRANSLATE_Y = new FloatProperty<VGroup>("translateY") { // from class: android.graphics.drawable.VectorDrawable.VGroup.3
            @Override // android.util.FloatProperty
            public void setValue(VGroup object, float value) {
                object.setTranslateY(value);
            }

            @Override // android.util.Property
            public Float get(VGroup object) {
                return Float.valueOf(object.getTranslateY());
            }
        };
        private static final Property<VGroup, Float> SCALE_X = new FloatProperty<VGroup>("scaleX") { // from class: android.graphics.drawable.VectorDrawable.VGroup.4
            @Override // android.util.FloatProperty
            public void setValue(VGroup object, float value) {
                object.setScaleX(value);
            }

            @Override // android.util.Property
            public Float get(VGroup object) {
                return Float.valueOf(object.getScaleX());
            }
        };
        private static final Property<VGroup, Float> SCALE_Y = new FloatProperty<VGroup>("scaleY") { // from class: android.graphics.drawable.VectorDrawable.VGroup.5
            @Override // android.util.FloatProperty
            public void setValue(VGroup object, float value) {
                object.setScaleY(value);
            }

            @Override // android.util.Property
            public Float get(VGroup object) {
                return Float.valueOf(object.getScaleY());
            }
        };
        private static final Property<VGroup, Float> PIVOT_X = new FloatProperty<VGroup>("pivotX") { // from class: android.graphics.drawable.VectorDrawable.VGroup.6
            @Override // android.util.FloatProperty
            public void setValue(VGroup object, float value) {
                object.setPivotX(value);
            }

            @Override // android.util.Property
            public Float get(VGroup object) {
                return Float.valueOf(object.getPivotX());
            }
        };
        private static final Property<VGroup, Float> PIVOT_Y = new FloatProperty<VGroup>("pivotY") { // from class: android.graphics.drawable.VectorDrawable.VGroup.7
            @Override // android.util.FloatProperty
            public void setValue(VGroup object, float value) {
                object.setPivotY(value);
            }

            @Override // android.util.Property
            public Float get(VGroup object) {
                return Float.valueOf(object.getPivotY());
            }
        };
        private static final Property<VGroup, Float> ROTATION = new FloatProperty<VGroup>("rotation") { // from class: android.graphics.drawable.VectorDrawable.VGroup.8
            @Override // android.util.FloatProperty
            public void setValue(VGroup object, float value) {
                object.setRotation(value);
            }

            @Override // android.util.Property
            public Float get(VGroup object) {
                return Float.valueOf(object.getRotation());
            }
        };
        private static final HashMap<String, Property> sPropertyMap = new HashMap<String, Property>() { // from class: android.graphics.drawable.VectorDrawable.VGroup.9
            {
                put("translateX", VGroup.TRANSLATE_X);
                put("translateY", VGroup.TRANSLATE_Y);
                put("scaleX", VGroup.SCALE_X);
                put("scaleY", VGroup.SCALE_Y);
                put("pivotX", VGroup.PIVOT_X);
                put("pivotY", VGroup.PIVOT_Y);
                put("rotation", VGroup.ROTATION);
            }
        };

        /* JADX INFO: Access modifiers changed from: package-private */
        public static synchronized int getPropertyIndex(String propertyName) {
            if (sPropertyIndexMap.containsKey(propertyName)) {
                return sPropertyIndexMap.get(propertyName).intValue();
            }
            return -1;
        }

        public synchronized VGroup(VGroup copy, ArrayMap<String, Object> targetsMap) {
            VPath newPath;
            this.mChildren = new ArrayList<>();
            this.mGroupName = null;
            this.mIsStateful = copy.mIsStateful;
            this.mThemeAttrs = copy.mThemeAttrs;
            this.mGroupName = copy.mGroupName;
            this.mChangingConfigurations = copy.mChangingConfigurations;
            if (this.mGroupName != null) {
                targetsMap.put(this.mGroupName, this);
            }
            this.mNativePtr = VectorDrawable.nCreateGroup(copy.mNativePtr);
            ArrayList<VObject> children = copy.mChildren;
            for (int i = 0; i < children.size(); i++) {
                VObject copyChild = children.get(i);
                if (copyChild instanceof VGroup) {
                    VGroup copyGroup = (VGroup) copyChild;
                    addChild(new VGroup(copyGroup, targetsMap));
                } else {
                    if (copyChild instanceof VFullPath) {
                        newPath = new VFullPath((VFullPath) copyChild);
                    } else if (copyChild instanceof VClipPath) {
                        newPath = new VClipPath((VClipPath) copyChild);
                    } else {
                        throw new IllegalStateException("Unknown object in the tree!");
                    }
                    addChild(newPath);
                    if (newPath.mPathName != null) {
                        targetsMap.put(newPath.mPathName, newPath);
                    }
                }
            }
        }

        public synchronized VGroup() {
            this.mChildren = new ArrayList<>();
            this.mGroupName = null;
            this.mNativePtr = VectorDrawable.access$1700();
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        synchronized Property getProperty(String propertyName) {
            if (sPropertyMap.containsKey(propertyName)) {
                return sPropertyMap.get(propertyName);
            }
            return null;
        }

        public synchronized String getGroupName() {
            return this.mGroupName;
        }

        public synchronized void addChild(VObject child) {
            VectorDrawable.nAddChild(this.mNativePtr, child.getNativePtr());
            this.mChildren.add(child);
            this.mIsStateful |= child.isStateful();
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        public synchronized void setTree(VirtualRefBasePtr treeRoot) {
            super.setTree(treeRoot);
            for (int i = 0; i < this.mChildren.size(); i++) {
                this.mChildren.get(i).setTree(treeRoot);
            }
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        public synchronized long getNativePtr() {
            return this.mNativePtr;
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        public synchronized void inflate(Resources res, AttributeSet attrs, Resources.Theme theme) {
            TypedArray a = Drawable.obtainAttributes(res, theme, attrs, R.styleable.VectorDrawableGroup);
            updateStateFromTypedArray(a);
            a.recycle();
        }

        synchronized void updateStateFromTypedArray(TypedArray a) {
            this.mChangingConfigurations |= a.getChangingConfigurations();
            this.mThemeAttrs = a.extractThemeAttrs();
            if (this.mTransform == null) {
                this.mTransform = new float[7];
            }
            boolean success = VectorDrawable.nGetGroupProperties(this.mNativePtr, this.mTransform, 7);
            if (success) {
                float rotate = a.getFloat(5, this.mTransform[0]);
                float pivotX = a.getFloat(1, this.mTransform[1]);
                float pivotY = a.getFloat(2, this.mTransform[2]);
                float scaleX = a.getFloat(3, this.mTransform[3]);
                float scaleY = a.getFloat(4, this.mTransform[4]);
                float translateX = a.getFloat(6, this.mTransform[5]);
                float translateY = a.getFloat(7, this.mTransform[6]);
                String groupName = a.getString(0);
                if (groupName != null) {
                    this.mGroupName = groupName;
                    VectorDrawable.nSetName(this.mNativePtr, this.mGroupName);
                }
                VectorDrawable.nUpdateGroupProperties(this.mNativePtr, rotate, pivotX, pivotY, scaleX, scaleY, translateX, translateY);
                return;
            }
            throw new RuntimeException("Error: inconsistent property count");
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        public synchronized boolean onStateChange(int[] stateSet) {
            boolean changed = false;
            ArrayList<VObject> children = this.mChildren;
            int count = children.size();
            for (int i = 0; i < count; i++) {
                VObject child = children.get(i);
                if (child.isStateful()) {
                    changed |= child.onStateChange(stateSet);
                }
            }
            return changed;
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        public synchronized boolean isStateful() {
            return this.mIsStateful;
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        public synchronized boolean hasFocusStateSpecified() {
            boolean result = false;
            ArrayList<VObject> children = this.mChildren;
            int count = children.size();
            for (int i = 0; i < count; i++) {
                VObject child = children.get(i);
                if (child.isStateful()) {
                    result |= child.hasFocusStateSpecified();
                }
            }
            return result;
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        synchronized int getNativeSize() {
            int size = 100;
            for (int i = 0; i < this.mChildren.size(); i++) {
                size += this.mChildren.get(i).getNativeSize();
            }
            return size;
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        public synchronized boolean canApplyTheme() {
            if (this.mThemeAttrs != null) {
                return true;
            }
            ArrayList<VObject> children = this.mChildren;
            int count = children.size();
            for (int i = 0; i < count; i++) {
                VObject child = children.get(i);
                if (child.canApplyTheme()) {
                    return true;
                }
            }
            return false;
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        public synchronized void applyTheme(Resources.Theme t) {
            if (this.mThemeAttrs != null) {
                TypedArray a = t.resolveAttributes(this.mThemeAttrs, R.styleable.VectorDrawableGroup);
                updateStateFromTypedArray(a);
                a.recycle();
            }
            ArrayList<VObject> children = this.mChildren;
            int count = children.size();
            for (int i = 0; i < count; i++) {
                VObject child = children.get(i);
                if (child.canApplyTheme()) {
                    child.applyTheme(t);
                    this.mIsStateful |= child.isStateful();
                }
            }
        }

        public synchronized float getRotation() {
            if (isTreeValid()) {
                return VectorDrawable.nGetRotation(this.mNativePtr);
            }
            return 0.0f;
        }

        private protected void setRotation(float rotation) {
            if (isTreeValid()) {
                VectorDrawable.nSetRotation(this.mNativePtr, rotation);
            }
        }

        public synchronized float getPivotX() {
            if (isTreeValid()) {
                return VectorDrawable.nGetPivotX(this.mNativePtr);
            }
            return 0.0f;
        }

        private protected void setPivotX(float pivotX) {
            if (isTreeValid()) {
                VectorDrawable.nSetPivotX(this.mNativePtr, pivotX);
            }
        }

        public synchronized float getPivotY() {
            if (isTreeValid()) {
                return VectorDrawable.nGetPivotY(this.mNativePtr);
            }
            return 0.0f;
        }

        private protected void setPivotY(float pivotY) {
            if (isTreeValid()) {
                VectorDrawable.nSetPivotY(this.mNativePtr, pivotY);
            }
        }

        public synchronized float getScaleX() {
            if (isTreeValid()) {
                return VectorDrawable.nGetScaleX(this.mNativePtr);
            }
            return 0.0f;
        }

        public synchronized void setScaleX(float scaleX) {
            if (isTreeValid()) {
                VectorDrawable.nSetScaleX(this.mNativePtr, scaleX);
            }
        }

        public synchronized float getScaleY() {
            if (isTreeValid()) {
                return VectorDrawable.nGetScaleY(this.mNativePtr);
            }
            return 0.0f;
        }

        public synchronized void setScaleY(float scaleY) {
            if (isTreeValid()) {
                VectorDrawable.nSetScaleY(this.mNativePtr, scaleY);
            }
        }

        public synchronized float getTranslateX() {
            if (isTreeValid()) {
                return VectorDrawable.nGetTranslateX(this.mNativePtr);
            }
            return 0.0f;
        }

        private protected void setTranslateX(float translateX) {
            if (isTreeValid()) {
                VectorDrawable.nSetTranslateX(this.mNativePtr, translateX);
            }
        }

        public synchronized float getTranslateY() {
            if (isTreeValid()) {
                return VectorDrawable.nGetTranslateY(this.mNativePtr);
            }
            return 0.0f;
        }

        private protected void setTranslateY(float translateY) {
            if (isTreeValid()) {
                VectorDrawable.nSetTranslateY(this.mNativePtr, translateY);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static abstract class VPath extends VObject {
        private static final Property<VPath, PathParser.PathData> PATH_DATA = new Property<VPath, PathParser.PathData>(PathParser.PathData.class, "pathData") { // from class: android.graphics.drawable.VectorDrawable.VPath.1
            @Override // android.util.Property
            public void set(VPath object, PathParser.PathData data) {
                object.setPathData(data);
            }

            @Override // android.util.Property
            public PathParser.PathData get(VPath object) {
                return object.getPathData();
            }
        };
        int mChangingConfigurations;
        protected PathParser.PathData mPathData;
        String mPathName;

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // android.graphics.drawable.VectorDrawable.VObject
        public synchronized Property getProperty(String propertyName) {
            if (PATH_DATA.getName().equals(propertyName)) {
                return PATH_DATA;
            }
            return null;
        }

        public synchronized VPath() {
            this.mPathData = null;
        }

        public synchronized VPath(VPath copy) {
            this.mPathData = null;
            this.mPathName = copy.mPathName;
            this.mChangingConfigurations = copy.mChangingConfigurations;
            this.mPathData = copy.mPathData != null ? new PathParser.PathData(copy.mPathData) : null;
        }

        public synchronized String getPathName() {
            return this.mPathName;
        }

        public synchronized PathParser.PathData getPathData() {
            return this.mPathData;
        }

        public synchronized void setPathData(PathParser.PathData pathData) {
            this.mPathData.setPathData(pathData);
            if (isTreeValid()) {
                VectorDrawable.nSetPathData(getNativePtr(), this.mPathData.getNativePtr());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class VClipPath extends VPath {
        private static final int NATIVE_ALLOCATION_SIZE = 120;
        private final long mNativePtr;

        public synchronized VClipPath() {
            this.mNativePtr = VectorDrawable.access$3700();
        }

        public synchronized VClipPath(VClipPath copy) {
            super(copy);
            this.mNativePtr = VectorDrawable.nCreateClipPath(copy.mNativePtr);
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        public synchronized long getNativePtr() {
            return this.mNativePtr;
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        public synchronized void inflate(Resources r, AttributeSet attrs, Resources.Theme theme) {
            TypedArray a = Drawable.obtainAttributes(r, theme, attrs, R.styleable.VectorDrawableClipPath);
            updateStateFromTypedArray(a);
            a.recycle();
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        public synchronized boolean canApplyTheme() {
            return false;
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        public synchronized void applyTheme(Resources.Theme theme) {
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        public synchronized boolean onStateChange(int[] stateSet) {
            return false;
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        public synchronized boolean isStateful() {
            return false;
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        public synchronized boolean hasFocusStateSpecified() {
            return false;
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        synchronized int getNativeSize() {
            return 120;
        }

        private synchronized void updateStateFromTypedArray(TypedArray a) {
            this.mChangingConfigurations |= a.getChangingConfigurations();
            String pathName = a.getString(0);
            if (pathName != null) {
                this.mPathName = pathName;
                VectorDrawable.nSetName(this.mNativePtr, this.mPathName);
            }
            String pathDataString = a.getString(1);
            if (pathDataString != null) {
                this.mPathData = new PathParser.PathData(pathDataString);
                VectorDrawable.nSetPathString(this.mNativePtr, pathDataString, pathDataString.length());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class VFullPath extends VPath {
        private static final int FILL_ALPHA_INDEX = 4;
        private static final int FILL_COLOR_INDEX = 3;
        private static final int FILL_TYPE_INDEX = 11;
        private static final int NATIVE_ALLOCATION_SIZE = 264;
        private static final int STROKE_ALPHA_INDEX = 2;
        private static final int STROKE_COLOR_INDEX = 1;
        private static final int STROKE_LINE_CAP_INDEX = 8;
        private static final int STROKE_LINE_JOIN_INDEX = 9;
        private static final int STROKE_MITER_LIMIT_INDEX = 10;
        private static final int STROKE_WIDTH_INDEX = 0;
        private static final int TOTAL_PROPERTY_COUNT = 12;
        private static final int TRIM_PATH_END_INDEX = 6;
        private static final int TRIM_PATH_OFFSET_INDEX = 7;
        private static final int TRIM_PATH_START_INDEX = 5;
        ComplexColor mFillColors;
        private final long mNativePtr;
        private byte[] mPropertyData;
        ComplexColor mStrokeColors;
        private int[] mThemeAttrs;
        private static final HashMap<String, Integer> sPropertyIndexMap = new HashMap<String, Integer>() { // from class: android.graphics.drawable.VectorDrawable.VFullPath.1
            {
                put("strokeWidth", 0);
                put("strokeColor", 1);
                put("strokeAlpha", 2);
                put("fillColor", 3);
                put("fillAlpha", 4);
                put("trimPathStart", 5);
                put("trimPathEnd", 6);
                put("trimPathOffset", 7);
            }
        };
        private static final Property<VFullPath, Float> STROKE_WIDTH = new FloatProperty<VFullPath>("strokeWidth") { // from class: android.graphics.drawable.VectorDrawable.VFullPath.2
            @Override // android.util.FloatProperty
            public void setValue(VFullPath object, float value) {
                object.setStrokeWidth(value);
            }

            @Override // android.util.Property
            public Float get(VFullPath object) {
                return Float.valueOf(object.getStrokeWidth());
            }
        };
        private static final Property<VFullPath, Integer> STROKE_COLOR = new IntProperty<VFullPath>("strokeColor") { // from class: android.graphics.drawable.VectorDrawable.VFullPath.3
            @Override // android.util.IntProperty
            public void setValue(VFullPath object, int value) {
                object.setStrokeColor(value);
            }

            @Override // android.util.Property
            public Integer get(VFullPath object) {
                return Integer.valueOf(object.getStrokeColor());
            }
        };
        private static final Property<VFullPath, Float> STROKE_ALPHA = new FloatProperty<VFullPath>("strokeAlpha") { // from class: android.graphics.drawable.VectorDrawable.VFullPath.4
            @Override // android.util.FloatProperty
            public void setValue(VFullPath object, float value) {
                object.setStrokeAlpha(value);
            }

            @Override // android.util.Property
            public Float get(VFullPath object) {
                return Float.valueOf(object.getStrokeAlpha());
            }
        };
        private static final Property<VFullPath, Integer> FILL_COLOR = new IntProperty<VFullPath>("fillColor") { // from class: android.graphics.drawable.VectorDrawable.VFullPath.5
            @Override // android.util.IntProperty
            public void setValue(VFullPath object, int value) {
                object.setFillColor(value);
            }

            @Override // android.util.Property
            public Integer get(VFullPath object) {
                return Integer.valueOf(object.getFillColor());
            }
        };
        private static final Property<VFullPath, Float> FILL_ALPHA = new FloatProperty<VFullPath>("fillAlpha") { // from class: android.graphics.drawable.VectorDrawable.VFullPath.6
            @Override // android.util.FloatProperty
            public void setValue(VFullPath object, float value) {
                object.setFillAlpha(value);
            }

            @Override // android.util.Property
            public Float get(VFullPath object) {
                return Float.valueOf(object.getFillAlpha());
            }
        };
        private static final Property<VFullPath, Float> TRIM_PATH_START = new FloatProperty<VFullPath>("trimPathStart") { // from class: android.graphics.drawable.VectorDrawable.VFullPath.7
            @Override // android.util.FloatProperty
            public void setValue(VFullPath object, float value) {
                object.setTrimPathStart(value);
            }

            @Override // android.util.Property
            public Float get(VFullPath object) {
                return Float.valueOf(object.getTrimPathStart());
            }
        };
        private static final Property<VFullPath, Float> TRIM_PATH_END = new FloatProperty<VFullPath>("trimPathEnd") { // from class: android.graphics.drawable.VectorDrawable.VFullPath.8
            @Override // android.util.FloatProperty
            public void setValue(VFullPath object, float value) {
                object.setTrimPathEnd(value);
            }

            @Override // android.util.Property
            public Float get(VFullPath object) {
                return Float.valueOf(object.getTrimPathEnd());
            }
        };
        private static final Property<VFullPath, Float> TRIM_PATH_OFFSET = new FloatProperty<VFullPath>("trimPathOffset") { // from class: android.graphics.drawable.VectorDrawable.VFullPath.9
            @Override // android.util.FloatProperty
            public void setValue(VFullPath object, float value) {
                object.setTrimPathOffset(value);
            }

            @Override // android.util.Property
            public Float get(VFullPath object) {
                return Float.valueOf(object.getTrimPathOffset());
            }
        };
        private static final HashMap<String, Property> sPropertyMap = new HashMap<String, Property>() { // from class: android.graphics.drawable.VectorDrawable.VFullPath.10
            {
                put("strokeWidth", VFullPath.STROKE_WIDTH);
                put("strokeColor", VFullPath.STROKE_COLOR);
                put("strokeAlpha", VFullPath.STROKE_ALPHA);
                put("fillColor", VFullPath.FILL_COLOR);
                put("fillAlpha", VFullPath.FILL_ALPHA);
                put("trimPathStart", VFullPath.TRIM_PATH_START);
                put("trimPathEnd", VFullPath.TRIM_PATH_END);
                put("trimPathOffset", VFullPath.TRIM_PATH_OFFSET);
            }
        };

        public synchronized VFullPath() {
            this.mStrokeColors = null;
            this.mFillColors = null;
            this.mNativePtr = VectorDrawable.access$4800();
        }

        public synchronized VFullPath(VFullPath copy) {
            super(copy);
            this.mStrokeColors = null;
            this.mFillColors = null;
            this.mNativePtr = VectorDrawable.nCreateFullPath(copy.mNativePtr);
            this.mThemeAttrs = copy.mThemeAttrs;
            this.mStrokeColors = copy.mStrokeColors;
            this.mFillColors = copy.mFillColors;
        }

        @Override // android.graphics.drawable.VectorDrawable.VPath, android.graphics.drawable.VectorDrawable.VObject
        synchronized Property getProperty(String propertyName) {
            Property p = super.getProperty(propertyName);
            if (p != null) {
                return p;
            }
            if (sPropertyMap.containsKey(propertyName)) {
                return sPropertyMap.get(propertyName);
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized int getPropertyIndex(String propertyName) {
            if (!sPropertyIndexMap.containsKey(propertyName)) {
                return -1;
            }
            return sPropertyIndexMap.get(propertyName).intValue();
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        public synchronized boolean onStateChange(int[] stateSet) {
            boolean changed = false;
            if (this.mStrokeColors != null && (this.mStrokeColors instanceof ColorStateList)) {
                int oldStrokeColor = getStrokeColor();
                int newStrokeColor = ((ColorStateList) this.mStrokeColors).getColorForState(stateSet, oldStrokeColor);
                changed = false | (oldStrokeColor != newStrokeColor);
                if (oldStrokeColor != newStrokeColor) {
                    VectorDrawable.nSetStrokeColor(this.mNativePtr, newStrokeColor);
                }
            }
            if (this.mFillColors != null && (this.mFillColors instanceof ColorStateList)) {
                int oldFillColor = getFillColor();
                int newFillColor = ((ColorStateList) this.mFillColors).getColorForState(stateSet, oldFillColor);
                changed |= oldFillColor != newFillColor;
                if (oldFillColor != newFillColor) {
                    VectorDrawable.nSetFillColor(this.mNativePtr, newFillColor);
                }
            }
            return changed;
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        public synchronized boolean isStateful() {
            return (this.mStrokeColors == null && this.mFillColors == null) ? false : true;
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        public synchronized boolean hasFocusStateSpecified() {
            return this.mStrokeColors != null && (this.mStrokeColors instanceof ColorStateList) && ((ColorStateList) this.mStrokeColors).hasFocusStateSpecified() && this.mFillColors != null && (this.mFillColors instanceof ColorStateList) && ((ColorStateList) this.mFillColors).hasFocusStateSpecified();
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        synchronized int getNativeSize() {
            return 264;
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        public synchronized long getNativePtr() {
            return this.mNativePtr;
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        public synchronized void inflate(Resources r, AttributeSet attrs, Resources.Theme theme) {
            TypedArray a = Drawable.obtainAttributes(r, theme, attrs, R.styleable.VectorDrawablePath);
            updateStateFromTypedArray(a);
            a.recycle();
        }

        private synchronized void updateStateFromTypedArray(TypedArray a) {
            int strokeColor;
            float trimPathOffset;
            Shader strokeGradient;
            if (this.mPropertyData == null) {
                this.mPropertyData = new byte[48];
            }
            boolean success = VectorDrawable.nGetFullPathProperties(this.mNativePtr, this.mPropertyData, 48);
            if (!success) {
                throw new RuntimeException("Error: inconsistent property count");
            }
            ByteBuffer properties = ByteBuffer.wrap(this.mPropertyData);
            properties.order(ByteOrder.nativeOrder());
            float strokeWidth = properties.getFloat(0);
            int strokeColor2 = properties.getInt(4);
            float strokeAlpha = properties.getFloat(8);
            int fillColor = properties.getInt(12);
            float fillAlpha = properties.getFloat(16);
            float trimPathStart = properties.getFloat(20);
            float trimPathEnd = properties.getFloat(24);
            float trimPathOffset2 = properties.getFloat(28);
            int strokeLineCap = properties.getInt(32);
            int strokeLineJoin = properties.getInt(36);
            float strokeMiterLimit = properties.getFloat(40);
            int fillType = properties.getInt(44);
            Shader fillGradient = null;
            Shader strokeGradient2 = null;
            this.mChangingConfigurations |= a.getChangingConfigurations();
            this.mThemeAttrs = a.extractThemeAttrs();
            String pathName = a.getString(0);
            if (pathName != null) {
                this.mPathName = pathName;
                strokeColor = strokeColor2;
                VectorDrawable.nSetName(this.mNativePtr, this.mPathName);
            } else {
                strokeColor = strokeColor2;
            }
            String pathString = a.getString(2);
            if (pathString != null) {
                this.mPathData = new PathParser.PathData(pathString);
                trimPathOffset = trimPathOffset2;
                VectorDrawable.nSetPathString(this.mNativePtr, pathString, pathString.length());
            } else {
                trimPathOffset = trimPathOffset2;
            }
            ComplexColor fillColors = a.getComplexColor(1);
            if (fillColors != null) {
                if (fillColors instanceof GradientColor) {
                    this.mFillColors = fillColors;
                    fillGradient = ((GradientColor) fillColors).getShader();
                } else if (fillColors.isStateful()) {
                    this.mFillColors = fillColors;
                } else {
                    this.mFillColors = null;
                }
                fillColor = fillColors.getDefaultColor();
            }
            Shader fillGradient2 = fillGradient;
            ComplexColor strokeColors = a.getComplexColor(3);
            if (strokeColors == null) {
                strokeGradient = null;
            } else {
                if (strokeColors instanceof GradientColor) {
                    this.mStrokeColors = strokeColors;
                    strokeGradient2 = ((GradientColor) strokeColors).getShader();
                } else if (strokeColors.isStateful()) {
                    this.mStrokeColors = strokeColors;
                } else {
                    this.mStrokeColors = null;
                }
                strokeColor = strokeColors.getDefaultColor();
                strokeGradient = strokeGradient2;
            }
            VectorDrawable.nUpdateFullPathFillGradient(this.mNativePtr, fillGradient2 != null ? fillGradient2.getNativeInstance() : 0L);
            VectorDrawable.nUpdateFullPathStrokeGradient(this.mNativePtr, strokeGradient != null ? strokeGradient.getNativeInstance() : 0L);
            float fillAlpha2 = a.getFloat(12, fillAlpha);
            int strokeLineCap2 = a.getInt(8, strokeLineCap);
            int strokeLineJoin2 = a.getInt(9, strokeLineJoin);
            float strokeMiterLimit2 = a.getFloat(10, strokeMiterLimit);
            VectorDrawable.nUpdateFullPathProperties(this.mNativePtr, a.getFloat(4, strokeWidth), strokeColor, a.getFloat(11, strokeAlpha), fillColor, fillAlpha2, a.getFloat(5, trimPathStart), a.getFloat(6, trimPathEnd), a.getFloat(7, trimPathOffset), strokeMiterLimit2, strokeLineCap2, strokeLineJoin2, a.getInt(13, fillType));
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        public synchronized boolean canApplyTheme() {
            if (this.mThemeAttrs != null) {
                return true;
            }
            boolean fillCanApplyTheme = canComplexColorApplyTheme(this.mFillColors);
            boolean strokeCanApplyTheme = canComplexColorApplyTheme(this.mStrokeColors);
            return fillCanApplyTheme || strokeCanApplyTheme;
        }

        @Override // android.graphics.drawable.VectorDrawable.VObject
        public synchronized void applyTheme(Resources.Theme t) {
            if (this.mThemeAttrs != null) {
                TypedArray a = t.resolveAttributes(this.mThemeAttrs, R.styleable.VectorDrawablePath);
                updateStateFromTypedArray(a);
                a.recycle();
            }
            boolean fillCanApplyTheme = canComplexColorApplyTheme(this.mFillColors);
            boolean strokeCanApplyTheme = canComplexColorApplyTheme(this.mStrokeColors);
            if (fillCanApplyTheme) {
                this.mFillColors = this.mFillColors.mo19obtainForTheme(t);
                if (this.mFillColors instanceof GradientColor) {
                    VectorDrawable.nUpdateFullPathFillGradient(this.mNativePtr, ((GradientColor) this.mFillColors).getShader().getNativeInstance());
                } else if (this.mFillColors instanceof ColorStateList) {
                    VectorDrawable.nSetFillColor(this.mNativePtr, this.mFillColors.getDefaultColor());
                }
            }
            if (strokeCanApplyTheme) {
                this.mStrokeColors = this.mStrokeColors.mo19obtainForTheme(t);
                if (this.mStrokeColors instanceof GradientColor) {
                    VectorDrawable.nUpdateFullPathStrokeGradient(this.mNativePtr, ((GradientColor) this.mStrokeColors).getShader().getNativeInstance());
                } else if (this.mStrokeColors instanceof ColorStateList) {
                    VectorDrawable.nSetStrokeColor(this.mNativePtr, this.mStrokeColors.getDefaultColor());
                }
            }
        }

        private synchronized boolean canComplexColorApplyTheme(ComplexColor complexColor) {
            return complexColor != null && complexColor.canApplyTheme();
        }

        synchronized int getStrokeColor() {
            if (isTreeValid()) {
                return VectorDrawable.nGetStrokeColor(this.mNativePtr);
            }
            return 0;
        }

        synchronized void setStrokeColor(int strokeColor) {
            this.mStrokeColors = null;
            if (isTreeValid()) {
                VectorDrawable.nSetStrokeColor(this.mNativePtr, strokeColor);
            }
        }

        synchronized float getStrokeWidth() {
            if (isTreeValid()) {
                return VectorDrawable.nGetStrokeWidth(this.mNativePtr);
            }
            return 0.0f;
        }

        synchronized void setStrokeWidth(float strokeWidth) {
            if (isTreeValid()) {
                VectorDrawable.nSetStrokeWidth(this.mNativePtr, strokeWidth);
            }
        }

        synchronized float getStrokeAlpha() {
            if (isTreeValid()) {
                return VectorDrawable.nGetStrokeAlpha(this.mNativePtr);
            }
            return 0.0f;
        }

        synchronized void setStrokeAlpha(float strokeAlpha) {
            if (isTreeValid()) {
                VectorDrawable.nSetStrokeAlpha(this.mNativePtr, strokeAlpha);
            }
        }

        synchronized int getFillColor() {
            if (isTreeValid()) {
                return VectorDrawable.nGetFillColor(this.mNativePtr);
            }
            return 0;
        }

        synchronized void setFillColor(int fillColor) {
            this.mFillColors = null;
            if (isTreeValid()) {
                VectorDrawable.nSetFillColor(this.mNativePtr, fillColor);
            }
        }

        synchronized float getFillAlpha() {
            if (isTreeValid()) {
                return VectorDrawable.nGetFillAlpha(this.mNativePtr);
            }
            return 0.0f;
        }

        synchronized void setFillAlpha(float fillAlpha) {
            if (isTreeValid()) {
                VectorDrawable.nSetFillAlpha(this.mNativePtr, fillAlpha);
            }
        }

        synchronized float getTrimPathStart() {
            if (isTreeValid()) {
                return VectorDrawable.nGetTrimPathStart(this.mNativePtr);
            }
            return 0.0f;
        }

        synchronized void setTrimPathStart(float trimPathStart) {
            if (isTreeValid()) {
                VectorDrawable.nSetTrimPathStart(this.mNativePtr, trimPathStart);
            }
        }

        synchronized float getTrimPathEnd() {
            if (isTreeValid()) {
                return VectorDrawable.nGetTrimPathEnd(this.mNativePtr);
            }
            return 0.0f;
        }

        synchronized void setTrimPathEnd(float trimPathEnd) {
            if (isTreeValid()) {
                VectorDrawable.nSetTrimPathEnd(this.mNativePtr, trimPathEnd);
            }
        }

        synchronized float getTrimPathOffset() {
            if (isTreeValid()) {
                return VectorDrawable.nGetTrimPathOffset(this.mNativePtr);
            }
            return 0.0f;
        }

        synchronized void setTrimPathOffset(float trimPathOffset) {
            if (isTreeValid()) {
                VectorDrawable.nSetTrimPathOffset(this.mNativePtr, trimPathOffset);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static abstract class VObject {
        VirtualRefBasePtr mTreePtr = null;

        abstract synchronized void applyTheme(Resources.Theme theme);

        abstract synchronized boolean canApplyTheme();

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract synchronized long getNativePtr();

        abstract synchronized int getNativeSize();

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract synchronized Property getProperty(String str);

        abstract synchronized boolean hasFocusStateSpecified();

        abstract synchronized void inflate(Resources resources, AttributeSet attributeSet, Resources.Theme theme);

        abstract synchronized boolean isStateful();

        abstract synchronized boolean onStateChange(int[] iArr);

        synchronized VObject() {
        }

        synchronized boolean isTreeValid() {
            return (this.mTreePtr == null || this.mTreePtr.get() == 0) ? false : true;
        }

        synchronized void setTree(VirtualRefBasePtr ptr) {
            this.mTreePtr = ptr;
        }
    }
}
