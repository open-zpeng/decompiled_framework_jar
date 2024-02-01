package android.view.animation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import com.android.internal.R;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class AnimationSet extends Animation {
    private static final int PROPERTY_CHANGE_BOUNDS_MASK = 128;
    private static final int PROPERTY_DURATION_MASK = 32;
    private static final int PROPERTY_FILL_AFTER_MASK = 1;
    private static final int PROPERTY_FILL_BEFORE_MASK = 2;
    private static final int PROPERTY_MORPH_MATRIX_MASK = 64;
    private static final int PROPERTY_REPEAT_MODE_MASK = 4;
    private static final int PROPERTY_SHARE_INTERPOLATOR_MASK = 16;
    private static final int PROPERTY_START_OFFSET_MASK = 8;
    private ArrayList<Animation> mAnimations;
    private boolean mDirty;
    private int mFlags;
    private boolean mHasAlpha;
    private long mLastEnd;
    private long[] mStoredOffsets;
    private Transformation mTempTransformation;

    public AnimationSet(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mFlags = 0;
        this.mAnimations = new ArrayList<>();
        this.mTempTransformation = new Transformation();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AnimationSet);
        setFlag(16, a.getBoolean(1, true));
        init();
        if (context.getApplicationInfo().targetSdkVersion >= 14) {
            if (a.hasValue(0)) {
                this.mFlags |= 32;
            }
            if (a.hasValue(2)) {
                this.mFlags = 2 | this.mFlags;
            }
            if (a.hasValue(3)) {
                this.mFlags |= 1;
            }
            if (a.hasValue(5)) {
                this.mFlags |= 4;
            }
            if (a.hasValue(4)) {
                this.mFlags |= 8;
            }
        }
        a.recycle();
    }

    public AnimationSet(boolean shareInterpolator) {
        this.mFlags = 0;
        this.mAnimations = new ArrayList<>();
        this.mTempTransformation = new Transformation();
        setFlag(16, shareInterpolator);
        init();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.animation.Animation
    /* renamed from: clone */
    public AnimationSet mo62clone() throws CloneNotSupportedException {
        AnimationSet animation = (AnimationSet) super.mo62clone();
        animation.mTempTransformation = new Transformation();
        animation.mAnimations = new ArrayList<>();
        int count = this.mAnimations.size();
        ArrayList<Animation> animations = this.mAnimations;
        for (int i = 0; i < count; i++) {
            animation.mAnimations.add(animations.get(i).mo62clone());
        }
        return animation;
    }

    private synchronized void setFlag(int mask, boolean value) {
        if (value) {
            this.mFlags |= mask;
        } else {
            this.mFlags &= ~mask;
        }
    }

    private synchronized void init() {
        this.mStartTime = 0L;
    }

    @Override // android.view.animation.Animation
    public void setFillAfter(boolean fillAfter) {
        this.mFlags |= 1;
        super.setFillAfter(fillAfter);
    }

    @Override // android.view.animation.Animation
    public void setFillBefore(boolean fillBefore) {
        this.mFlags |= 2;
        super.setFillBefore(fillBefore);
    }

    @Override // android.view.animation.Animation
    public void setRepeatMode(int repeatMode) {
        this.mFlags |= 4;
        super.setRepeatMode(repeatMode);
    }

    @Override // android.view.animation.Animation
    public void setStartOffset(long startOffset) {
        this.mFlags |= 8;
        super.setStartOffset(startOffset);
    }

    @Override // android.view.animation.Animation
    public synchronized boolean hasAlpha() {
        if (this.mDirty) {
            int i = 0;
            this.mHasAlpha = false;
            this.mDirty = false;
            int count = this.mAnimations.size();
            ArrayList<Animation> animations = this.mAnimations;
            while (true) {
                if (i >= count) {
                    break;
                } else if (!animations.get(i).hasAlpha()) {
                    i++;
                } else {
                    this.mHasAlpha = true;
                    break;
                }
            }
        }
        return this.mHasAlpha;
    }

    @Override // android.view.animation.Animation
    public void setDuration(long durationMillis) {
        this.mFlags |= 32;
        super.setDuration(durationMillis);
        this.mLastEnd = this.mStartOffset + this.mDuration;
    }

    public void addAnimation(Animation a) {
        this.mAnimations.add(a);
        boolean noMatrix = (this.mFlags & 64) == 0;
        if (noMatrix && a.willChangeTransformationMatrix()) {
            this.mFlags |= 64;
        }
        boolean changeBounds = (this.mFlags & 128) == 0;
        if (changeBounds && a.willChangeBounds()) {
            this.mFlags |= 128;
        }
        if ((this.mFlags & 32) == 32) {
            this.mLastEnd = this.mStartOffset + this.mDuration;
        } else if (this.mAnimations.size() == 1) {
            this.mDuration = a.getStartOffset() + a.getDuration();
            this.mLastEnd = this.mStartOffset + this.mDuration;
        } else {
            this.mLastEnd = Math.max(this.mLastEnd, this.mStartOffset + a.getStartOffset() + a.getDuration());
            this.mDuration = this.mLastEnd - this.mStartOffset;
        }
        this.mDirty = true;
    }

    @Override // android.view.animation.Animation
    public void setStartTime(long startTimeMillis) {
        super.setStartTime(startTimeMillis);
        int count = this.mAnimations.size();
        ArrayList<Animation> animations = this.mAnimations;
        for (int i = 0; i < count; i++) {
            Animation a = animations.get(i);
            a.setStartTime(startTimeMillis);
        }
    }

    @Override // android.view.animation.Animation
    public long getStartTime() {
        long startTime = Long.MAX_VALUE;
        int count = this.mAnimations.size();
        ArrayList<Animation> animations = this.mAnimations;
        for (int i = 0; i < count; i++) {
            Animation a = animations.get(i);
            startTime = Math.min(startTime, a.getStartTime());
        }
        return startTime;
    }

    @Override // android.view.animation.Animation
    public void restrictDuration(long durationMillis) {
        super.restrictDuration(durationMillis);
        ArrayList<Animation> animations = this.mAnimations;
        int count = animations.size();
        for (int i = 0; i < count; i++) {
            animations.get(i).restrictDuration(durationMillis);
        }
    }

    @Override // android.view.animation.Animation
    public long getDuration() {
        ArrayList<Animation> animations = this.mAnimations;
        int count = animations.size();
        long duration = 0;
        int i = 0;
        boolean durationSet = (this.mFlags & 32) == 32;
        if (durationSet) {
            long duration2 = this.mDuration;
            return duration2;
        }
        while (true) {
            int i2 = i;
            if (i2 < count) {
                duration = Math.max(duration, animations.get(i2).getDuration());
                i = i2 + 1;
            } else {
                return duration;
            }
        }
    }

    @Override // android.view.animation.Animation
    public long computeDurationHint() {
        long duration = 0;
        int count = this.mAnimations.size();
        ArrayList<Animation> animations = this.mAnimations;
        for (int i = count - 1; i >= 0; i--) {
            long d = animations.get(i).computeDurationHint();
            if (d > duration) {
                duration = d;
            }
        }
        return duration;
    }

    public synchronized void initializeInvalidateRegion(int left, int top, int right, int bottom) {
        RectF region = this.mPreviousRegion;
        region.set(left, top, right, bottom);
        region.inset(-1.0f, -1.0f);
        if (this.mFillBefore) {
            int count = this.mAnimations.size();
            ArrayList<Animation> animations = this.mAnimations;
            Transformation temp = this.mTempTransformation;
            Transformation previousTransformation = this.mPreviousTransformation;
            for (int i = count - 1; i >= 0; i--) {
                Animation a = animations.get(i);
                if (!a.isFillEnabled() || a.getFillBefore() || a.getStartOffset() == 0) {
                    temp.clear();
                    Interpolator interpolator = a.mInterpolator;
                    a.applyTransformation(interpolator != null ? interpolator.getInterpolation(0.0f) : 0.0f, temp);
                    previousTransformation.compose(temp);
                }
            }
        }
    }

    @Override // android.view.animation.Animation
    public boolean getTransformation(long currentTime, Transformation t) {
        int count = this.mAnimations.size();
        ArrayList<Animation> animations = this.mAnimations;
        Transformation temp = this.mTempTransformation;
        boolean more = false;
        boolean started = false;
        boolean ended = true;
        t.clear();
        for (int i = count - 1; i >= 0; i--) {
            Animation a = animations.get(i);
            temp.clear();
            boolean z = false;
            more = a.getTransformation(currentTime, temp, getScaleFactor()) || more;
            t.compose(temp);
            started = started || a.hasStarted();
            if (a.hasEnded() && ended) {
                z = true;
            }
            ended = z;
        }
        if (started && !this.mStarted) {
            if (this.mListener != null) {
                this.mListener.onAnimationStart(this);
            }
            this.mStarted = true;
        }
        if (ended != this.mEnded) {
            if (this.mListener != null) {
                this.mListener.onAnimationEnd(this);
            }
            this.mEnded = ended;
        }
        return more;
    }

    @Override // android.view.animation.Animation
    public void scaleCurrentDuration(float scale) {
        ArrayList<Animation> animations = this.mAnimations;
        int count = animations.size();
        for (int i = 0; i < count; i++) {
            animations.get(i).scaleCurrentDuration(scale);
        }
    }

    @Override // android.view.animation.Animation
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        boolean durationSet;
        boolean fillAfterSet;
        super.initialize(width, height, parentWidth, parentHeight);
        boolean durationSet2 = (this.mFlags & 32) == 32;
        boolean fillAfterSet2 = (this.mFlags & 1) == 1;
        boolean fillBeforeSet = (this.mFlags & 2) == 2;
        boolean repeatModeSet = (this.mFlags & 4) == 4;
        boolean shareInterpolator = (this.mFlags & 16) == 16;
        boolean startOffsetSet = (this.mFlags & 8) == 8;
        if (shareInterpolator) {
            ensureInterpolator();
        }
        ArrayList<Animation> children = this.mAnimations;
        int count = children.size();
        long duration = this.mDuration;
        boolean fillAfter = this.mFillAfter;
        boolean fillBefore = this.mFillBefore;
        int repeatMode = this.mRepeatMode;
        Interpolator interpolator = this.mInterpolator;
        boolean startOffsetSet2 = startOffsetSet;
        long startOffset = this.mStartOffset;
        long[] storedOffsets = this.mStoredOffsets;
        if (startOffsetSet2) {
            if (storedOffsets == null || storedOffsets.length != count) {
                long[] jArr = new long[count];
                this.mStoredOffsets = jArr;
                storedOffsets = jArr;
            }
        } else if (storedOffsets != null) {
            this.mStoredOffsets = null;
            storedOffsets = null;
        }
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= count) {
                return;
            }
            Animation a = children.get(i2);
            if (durationSet2) {
                a.setDuration(duration);
            }
            if (fillAfterSet2) {
                a.setFillAfter(fillAfter);
            }
            if (fillBeforeSet) {
                a.setFillBefore(fillBefore);
            }
            if (repeatModeSet) {
                a.setRepeatMode(repeatMode);
            }
            if (shareInterpolator) {
                a.setInterpolator(interpolator);
            }
            if (startOffsetSet2) {
                long offset = a.getStartOffset();
                durationSet = durationSet2;
                fillAfterSet = fillAfterSet2;
                a.setStartOffset(offset + startOffset);
                storedOffsets[i2] = offset;
            } else {
                durationSet = durationSet2;
                fillAfterSet = fillAfterSet2;
            }
            a.initialize(width, height, parentWidth, parentHeight);
            i = i2 + 1;
            durationSet2 = durationSet;
            fillAfterSet2 = fillAfterSet;
            storedOffsets = storedOffsets;
            fillBeforeSet = fillBeforeSet;
        }
    }

    @Override // android.view.animation.Animation
    public void reset() {
        super.reset();
        restoreChildrenStartOffset();
    }

    synchronized void restoreChildrenStartOffset() {
        long[] offsets = this.mStoredOffsets;
        if (offsets == null) {
            return;
        }
        ArrayList<Animation> children = this.mAnimations;
        int count = children.size();
        for (int i = 0; i < count; i++) {
            children.get(i).setStartOffset(offsets[i]);
        }
    }

    public List<Animation> getAnimations() {
        return this.mAnimations;
    }

    @Override // android.view.animation.Animation
    public boolean willChangeTransformationMatrix() {
        return (this.mFlags & 64) == 64;
    }

    @Override // android.view.animation.Animation
    public boolean willChangeBounds() {
        return (this.mFlags & 128) == 128;
    }
}
