package android.graphics.drawable;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.DrawableContainer;
import android.util.AttributeSet;
import android.util.StateSet;
import com.android.ims.ImsConfig;
import com.android.internal.R;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class StateListDrawable extends DrawableContainer {
    private static final boolean DEBUG = false;
    private static final String TAG = "StateListDrawable";
    private boolean mMutated;
    public protected StateListState mStateListState;

    public StateListDrawable() {
        this(null, null);
    }

    public void addState(int[] stateSet, Drawable drawable) {
        if (drawable != null) {
            this.mStateListState.addStateSet(stateSet, drawable);
            onStateChange(getState());
        }
    }

    @Override // android.graphics.drawable.DrawableContainer, android.graphics.drawable.Drawable
    public boolean isStateful() {
        return true;
    }

    @Override // android.graphics.drawable.DrawableContainer, android.graphics.drawable.Drawable
    public boolean hasFocusStateSpecified() {
        return this.mStateListState.hasFocusStateSpecified();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.drawable.DrawableContainer, android.graphics.drawable.Drawable
    public boolean onStateChange(int[] stateSet) {
        boolean changed = super.onStateChange(stateSet);
        int idx = this.mStateListState.indexOfStateSet(stateSet);
        if (idx < 0) {
            idx = this.mStateListState.indexOfStateSet(StateSet.WILD_CARD);
        }
        return selectDrawable(idx) || changed;
    }

    @Override // android.graphics.drawable.Drawable
    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        TypedArray a = obtainAttributes(r, theme, attrs, R.styleable.StateListDrawable);
        super.inflateWithAttributes(r, parser, a, 1);
        updateStateFromTypedArray(a);
        updateDensity(r);
        a.recycle();
        inflateChildElements(r, parser, attrs, theme);
        onStateChange(getState());
    }

    public protected void updateStateFromTypedArray(TypedArray a) {
        StateListState state = this.mStateListState;
        state.mChangingConfigurations |= a.getChangingConfigurations();
        state.mThemeAttrs = a.extractThemeAttrs();
        state.mVariablePadding = a.getBoolean(2, state.mVariablePadding);
        state.mConstantSize = a.getBoolean(3, state.mConstantSize);
        state.mEnterFadeDuration = a.getInt(4, state.mEnterFadeDuration);
        state.mExitFadeDuration = a.getInt(5, state.mExitFadeDuration);
        state.mDither = a.getBoolean(0, state.mDither);
        state.mAutoMirrored = a.getBoolean(6, state.mAutoMirrored);
    }

    private synchronized void inflateChildElements(Resources r, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        int type;
        StateListState state = this.mStateListState;
        int innerDepth = parser.getDepth() + 1;
        while (true) {
            int type2 = parser.next();
            if (type2 != 1) {
                int depth = parser.getDepth();
                if (depth >= innerDepth || type2 != 3) {
                    if (type2 == 2 && depth <= innerDepth && parser.getName().equals(ImsConfig.EXTRA_CHANGED_ITEM)) {
                        TypedArray a = obtainAttributes(r, theme, attrs, R.styleable.StateListDrawableItem);
                        Drawable dr = a.getDrawable(0);
                        a.recycle();
                        int[] states = extractStateSet(attrs);
                        if (dr == null) {
                            do {
                                type = parser.next();
                            } while (type == 4);
                            if (type != 2) {
                                throw new XmlPullParserException(parser.getPositionDescription() + ": <item> tag requires a 'drawable' attribute or child tag defining a drawable");
                            }
                            dr = Drawable.createFromXmlInner(r, parser, attrs, theme);
                        }
                        state.addStateSet(states, dr);
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    public private protected int[] extractStateSet(AttributeSet attrs) {
        int numAttrs = attrs.getAttributeCount();
        int[] states = new int[numAttrs];
        int j = 0;
        for (int j2 = 0; j2 < numAttrs; j2++) {
            int stateResId = attrs.getAttributeNameResource(j2);
            if (stateResId != 0 && stateResId != 16842960 && stateResId != 16843161) {
                int j3 = j + 1;
                states[j] = attrs.getAttributeBooleanValue(j2, false) ? stateResId : -stateResId;
                j = j3;
            }
        }
        return StateSet.trimStateSet(states, j);
    }

    synchronized StateListState getStateListState() {
        return this.mStateListState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getStateCount() {
        return this.mStateListState.getChildCount();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] getStateSet(int index) {
        return this.mStateListState.mStateSets[index];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Drawable getStateDrawable(int index) {
        return this.mStateListState.getChild(index);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getStateDrawableIndex(int[] stateSet) {
        return this.mStateListState.indexOfStateSet(stateSet);
    }

    @Override // android.graphics.drawable.DrawableContainer, android.graphics.drawable.Drawable
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mStateListState.mutate();
            this.mMutated = true;
        }
        return this;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.graphics.drawable.DrawableContainer
    public synchronized StateListState cloneConstantState() {
        return new StateListState(this.mStateListState, this, null);
    }

    @Override // android.graphics.drawable.DrawableContainer, android.graphics.drawable.Drawable
    public synchronized void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class StateListState extends DrawableContainer.DrawableContainerState {
        int[][] mStateSets;
        int[] mThemeAttrs;

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized StateListState(StateListState orig, StateListDrawable owner, Resources res) {
            super(orig, owner, res);
            if (orig != null) {
                this.mThemeAttrs = orig.mThemeAttrs;
                this.mStateSets = orig.mStateSets;
                return;
            }
            this.mThemeAttrs = null;
            this.mStateSets = new int[getCapacity()];
        }

        synchronized void mutate() {
            this.mThemeAttrs = this.mThemeAttrs != null ? (int[]) this.mThemeAttrs.clone() : null;
            int[][] stateSets = new int[this.mStateSets.length];
            for (int i = this.mStateSets.length - 1; i >= 0; i--) {
                stateSets[i] = this.mStateSets[i] != null ? (int[]) this.mStateSets[i].clone() : null;
            }
            this.mStateSets = stateSets;
        }

        public private protected int addStateSet(int[] stateSet, Drawable drawable) {
            int pos = addChild(drawable);
            this.mStateSets[pos] = stateSet;
            return pos;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized int indexOfStateSet(int[] stateSet) {
            int[][] stateSets = this.mStateSets;
            int N = getChildCount();
            for (int i = 0; i < N; i++) {
                if (StateSet.stateSetMatches(stateSets[i], stateSet)) {
                    return i;
                }
            }
            return -1;
        }

        synchronized boolean hasFocusStateSpecified() {
            return StateSet.containsAttribute(this.mStateSets, android.R.attr.state_focused);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            return new StateListDrawable(this, null);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources res) {
            return new StateListDrawable(this, res);
        }

        @Override // android.graphics.drawable.DrawableContainer.DrawableContainerState, android.graphics.drawable.Drawable.ConstantState
        public boolean canApplyTheme() {
            return this.mThemeAttrs != null || super.canApplyTheme();
        }

        @Override // android.graphics.drawable.DrawableContainer.DrawableContainerState
        public void growArray(int oldSize, int newSize) {
            super.growArray(oldSize, newSize);
            int[][] newStateSets = new int[newSize];
            System.arraycopy(this.mStateSets, 0, newStateSets, 0, oldSize);
            this.mStateSets = newStateSets;
        }
    }

    @Override // android.graphics.drawable.DrawableContainer, android.graphics.drawable.Drawable
    public void applyTheme(Resources.Theme theme) {
        super.applyTheme(theme);
        onStateChange(getState());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.graphics.drawable.DrawableContainer
    public void setConstantState(DrawableContainer.DrawableContainerState state) {
        super.setConstantState(state);
        if (state instanceof StateListState) {
            this.mStateListState = (StateListState) state;
        }
    }

    private synchronized StateListDrawable(StateListState state, Resources res) {
        StateListState newState = new StateListState(state, this, res);
        setConstantState(newState);
        onStateChange(getState());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized StateListDrawable(StateListState state) {
        if (state != null) {
            setConstantState(state);
        }
    }
}
