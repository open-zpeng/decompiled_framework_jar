package android.view;

import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.view.FocusFinder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes3.dex */
public class FocusFinder {
    private static final ThreadLocal<FocusFinder> tlFocusFinder = new ThreadLocal<FocusFinder>() { // from class: android.view.FocusFinder.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public FocusFinder initialValue() {
            return new FocusFinder();
        }
    };
    final Rect mBestCandidateRect;
    private final FocusSorter mFocusSorter;
    final Rect mFocusedRect;
    final Rect mOtherRect;
    private final ArrayList<View> mTempList;
    private final UserSpecifiedFocusComparator mUserSpecifiedClusterComparator;
    private final UserSpecifiedFocusComparator mUserSpecifiedFocusComparator;

    public static FocusFinder getInstance() {
        return tlFocusFinder.get();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ View lambda$new$0(View r, View v) {
        if (isValidId(v.getNextFocusForwardId())) {
            return v.findUserSetNextFocus(r, 2);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ View lambda$new$1(View r, View v) {
        if (isValidId(v.getNextClusterForwardId())) {
            return v.findUserSetNextKeyboardNavigationCluster(r, 2);
        }
        return null;
    }

    private FocusFinder() {
        this.mFocusedRect = new Rect();
        this.mOtherRect = new Rect();
        this.mBestCandidateRect = new Rect();
        this.mUserSpecifiedFocusComparator = new UserSpecifiedFocusComparator(new UserSpecifiedFocusComparator.NextFocusGetter() { // from class: android.view.-$$Lambda$FocusFinder$Pgx6IETuqCkrhJYdiBes48tolG4
            @Override // android.view.FocusFinder.UserSpecifiedFocusComparator.NextFocusGetter
            public final View get(View view, View view2) {
                return FocusFinder.lambda$new$0(view, view2);
            }
        });
        this.mUserSpecifiedClusterComparator = new UserSpecifiedFocusComparator(new UserSpecifiedFocusComparator.NextFocusGetter() { // from class: android.view.-$$Lambda$FocusFinder$P8rLvOJhymJH5ALAgUjGaM5gxKA
            @Override // android.view.FocusFinder.UserSpecifiedFocusComparator.NextFocusGetter
            public final View get(View view, View view2) {
                return FocusFinder.lambda$new$1(view, view2);
            }
        });
        this.mFocusSorter = new FocusSorter();
        this.mTempList = new ArrayList<>();
    }

    public final View findNextFocus(ViewGroup root, View focused, int direction) {
        return findNextFocus(root, focused, null, direction);
    }

    public View findNextFocusFromRect(ViewGroup root, Rect focusedRect, int direction) {
        this.mFocusedRect.set(focusedRect);
        return findNextFocus(root, null, this.mFocusedRect, direction);
    }

    private View findNextFocus(ViewGroup root, View focused, Rect focusedRect, int direction) {
        View next = null;
        ViewGroup effectiveRoot = getEffectiveRoot(root, focused);
        if (focused != null) {
            next = findNextUserSpecifiedFocus(effectiveRoot, focused, direction);
        }
        if (next != null) {
            return next;
        }
        ArrayList<View> focusables = this.mTempList;
        try {
            focusables.clear();
            effectiveRoot.addFocusables(focusables, direction);
            if (!focusables.isEmpty()) {
                next = findNextFocus(effectiveRoot, focused, focusedRect, direction, focusables);
            }
            return next;
        } finally {
            focusables.clear();
        }
    }

    private ViewGroup getEffectiveRoot(ViewGroup root, View focused) {
        if (focused == null || focused == root) {
            return root;
        }
        ViewGroup effective = null;
        ViewParent nextParent = focused.getParent();
        while (nextParent != root) {
            ViewGroup vg = (ViewGroup) nextParent;
            if (vg.getTouchscreenBlocksFocus() && focused.getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN) && vg.isKeyboardNavigationCluster()) {
                effective = vg;
            }
            nextParent = nextParent.getParent();
            if (!(nextParent instanceof ViewGroup)) {
                return root;
            }
        }
        return effective != null ? effective : root;
    }

    public View findNextKeyboardNavigationCluster(View root, View currentCluster, int direction) {
        View next = null;
        if (currentCluster != null && (next = findNextUserSpecifiedKeyboardNavigationCluster(root, currentCluster, direction)) != null) {
            return next;
        }
        ArrayList<View> clusters = this.mTempList;
        try {
            clusters.clear();
            root.addKeyboardNavigationClusters(clusters, direction);
            if (!clusters.isEmpty()) {
                next = findNextKeyboardNavigationCluster(root, currentCluster, clusters, direction);
            }
            return next;
        } finally {
            clusters.clear();
        }
    }

    private View findNextUserSpecifiedKeyboardNavigationCluster(View root, View currentCluster, int direction) {
        View userSetNextCluster = currentCluster.findUserSetNextKeyboardNavigationCluster(root, direction);
        if (userSetNextCluster != null && userSetNextCluster.hasFocusable()) {
            return userSetNextCluster;
        }
        return null;
    }

    private View findNextUserSpecifiedFocus(ViewGroup root, View focused, int direction) {
        View userSetNextFocus = focused.findUserSetNextFocus(root, direction);
        View cycleCheck = userSetNextFocus;
        boolean cycleStep = true;
        while (userSetNextFocus != null) {
            if (userSetNextFocus.isFocusable() && userSetNextFocus.getVisibility() == 0 && (!userSetNextFocus.isInTouchMode() || userSetNextFocus.isFocusableInTouchMode())) {
                return userSetNextFocus;
            }
            userSetNextFocus = userSetNextFocus.findUserSetNextFocus(root, direction);
            boolean z = !cycleStep;
            cycleStep = z;
            if (z && (cycleCheck = cycleCheck.findUserSetNextFocus(root, direction)) == userSetNextFocus) {
                return null;
            }
        }
        return null;
    }

    private View findNextFocus(ViewGroup root, View focused, Rect focusedRect, int direction, ArrayList<View> focusables) {
        Rect focusedRect2;
        if (focused != null) {
            if (focusedRect != null) {
                focusedRect2 = focusedRect;
            } else {
                focusedRect2 = this.mFocusedRect;
            }
            focused.getFocusedRect(focusedRect2);
            root.offsetDescendantRectToMyCoords(focused, focusedRect2);
        } else if (focusedRect != null) {
            focusedRect2 = focusedRect;
        } else {
            focusedRect2 = this.mFocusedRect;
            if (direction != 1) {
                if (direction != 2) {
                    if (direction == 17 || direction == 33) {
                        setFocusBottomRight(root, focusedRect2);
                    } else if (direction == 66 || direction == 130) {
                        setFocusTopLeft(root, focusedRect2);
                    }
                } else if (root.isLayoutRtl()) {
                    setFocusBottomRight(root, focusedRect2);
                } else {
                    setFocusTopLeft(root, focusedRect2);
                }
            } else if (root.isLayoutRtl()) {
                setFocusTopLeft(root, focusedRect2);
            } else {
                setFocusBottomRight(root, focusedRect2);
            }
        }
        if (direction == 1 || direction == 2) {
            return findNextFocusInRelativeDirection(focusables, root, focused, focusedRect2, direction);
        }
        if (direction == 17 || direction == 33 || direction == 66 || direction == 130) {
            return findNextFocusInAbsoluteDirection(focusables, root, focused, focusedRect2, direction);
        }
        throw new IllegalArgumentException("Unknown direction: " + direction);
    }

    private View findNextKeyboardNavigationCluster(View root, View currentCluster, List<View> clusters, int direction) {
        try {
            this.mUserSpecifiedClusterComparator.setFocusables(clusters, root);
            Collections.sort(clusters, this.mUserSpecifiedClusterComparator);
            this.mUserSpecifiedClusterComparator.recycle();
            int count = clusters.size();
            if (direction != 1) {
                if (direction != 2) {
                    if (direction != 17 && direction != 33) {
                        if (direction != 66 && direction != 130) {
                            throw new IllegalArgumentException("Unknown direction: " + direction);
                        }
                    }
                }
                return getNextKeyboardNavigationCluster(root, currentCluster, clusters, count);
            }
            return getPreviousKeyboardNavigationCluster(root, currentCluster, clusters, count);
        } catch (Throwable th) {
            this.mUserSpecifiedClusterComparator.recycle();
            throw th;
        }
    }

    private View findNextFocusInRelativeDirection(ArrayList<View> focusables, ViewGroup root, View focused, Rect focusedRect, int direction) {
        try {
            this.mUserSpecifiedFocusComparator.setFocusables(focusables, root);
            Collections.sort(focusables, this.mUserSpecifiedFocusComparator);
            this.mUserSpecifiedFocusComparator.recycle();
            int count = focusables.size();
            if (direction != 1) {
                if (direction == 2) {
                    return getNextFocusable(focused, focusables, count);
                }
                return focusables.get(count - 1);
            }
            return getPreviousFocusable(focused, focusables, count);
        } catch (Throwable th) {
            this.mUserSpecifiedFocusComparator.recycle();
            throw th;
        }
    }

    private void setFocusBottomRight(ViewGroup root, Rect focusedRect) {
        int rootBottom = root.getScrollY() + root.getHeight();
        int rootRight = root.getScrollX() + root.getWidth();
        focusedRect.set(rootRight, rootBottom, rootRight, rootBottom);
    }

    private void setFocusTopLeft(ViewGroup root, Rect focusedRect) {
        int rootTop = root.getScrollY();
        int rootLeft = root.getScrollX();
        focusedRect.set(rootLeft, rootTop, rootLeft, rootTop);
    }

    View findNextFocusInAbsoluteDirection(ArrayList<View> focusables, ViewGroup root, View focused, Rect focusedRect, int direction) {
        this.mBestCandidateRect.set(focusedRect);
        if (direction == 17) {
            this.mBestCandidateRect.offset(focusedRect.width() + 1, 0);
        } else if (direction == 33) {
            this.mBestCandidateRect.offset(0, focusedRect.height() + 1);
        } else if (direction == 66) {
            this.mBestCandidateRect.offset(-(focusedRect.width() + 1), 0);
        } else if (direction == 130) {
            this.mBestCandidateRect.offset(0, -(focusedRect.height() + 1));
        }
        View closest = null;
        int numFocusables = focusables.size();
        for (int i = 0; i < numFocusables; i++) {
            View focusable = focusables.get(i);
            if (focusable != focused && focusable != root) {
                focusable.getFocusedRect(this.mOtherRect);
                root.offsetDescendantRectToMyCoords(focusable, this.mOtherRect);
                if (isBetterCandidate(direction, focusedRect, this.mOtherRect, this.mBestCandidateRect)) {
                    this.mBestCandidateRect.set(this.mOtherRect);
                    closest = focusable;
                }
            }
        }
        return closest;
    }

    private static View getNextFocusable(View focused, ArrayList<View> focusables, int count) {
        int position;
        if (focused != null && (position = focusables.lastIndexOf(focused)) >= 0 && position + 1 < count) {
            return focusables.get(position + 1);
        }
        if (!focusables.isEmpty()) {
            return focusables.get(0);
        }
        return null;
    }

    private static View getPreviousFocusable(View focused, ArrayList<View> focusables, int count) {
        int position;
        if (focused != null && (position = focusables.indexOf(focused)) > 0) {
            return focusables.get(position - 1);
        }
        if (!focusables.isEmpty()) {
            return focusables.get(count - 1);
        }
        return null;
    }

    private static View getNextKeyboardNavigationCluster(View root, View currentCluster, List<View> clusters, int count) {
        if (currentCluster == null) {
            return clusters.get(0);
        }
        int position = clusters.lastIndexOf(currentCluster);
        if (position >= 0 && position + 1 < count) {
            return clusters.get(position + 1);
        }
        return root;
    }

    private static View getPreviousKeyboardNavigationCluster(View root, View currentCluster, List<View> clusters, int count) {
        if (currentCluster == null) {
            return clusters.get(count - 1);
        }
        int position = clusters.indexOf(currentCluster);
        if (position > 0) {
            return clusters.get(position - 1);
        }
        return root;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isBetterCandidate(int direction, Rect source, Rect rect1, Rect rect2) {
        if (isCandidate(source, rect1, direction)) {
            if (isCandidate(source, rect2, direction) && !beamBeats(direction, source, rect1, rect2)) {
                return !beamBeats(direction, source, rect2, rect1) && getWeightedDistanceFor((long) majorAxisDistance(direction, source, rect1), (long) minorAxisDistance(direction, source, rect1)) < getWeightedDistanceFor((long) majorAxisDistance(direction, source, rect2), (long) minorAxisDistance(direction, source, rect2));
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean beamBeats(int direction, Rect source, Rect rect1, Rect rect2) {
        boolean rect1InSrcBeam = beamsOverlap(direction, source, rect1);
        boolean rect2InSrcBeam = beamsOverlap(direction, source, rect2);
        if (rect2InSrcBeam || !rect1InSrcBeam) {
            return false;
        }
        return !isToDirectionOf(direction, source, rect2) || direction == 17 || direction == 66 || majorAxisDistance(direction, source, rect1) < majorAxisDistanceToFarEdge(direction, source, rect2);
    }

    long getWeightedDistanceFor(long majorAxisDistance, long minorAxisDistance) {
        return (13 * majorAxisDistance * majorAxisDistance) + (minorAxisDistance * minorAxisDistance);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isCandidate(Rect srcRect, Rect destRect, int direction) {
        if (direction == 17) {
            return (srcRect.right > destRect.right || srcRect.left >= destRect.right) && srcRect.left > destRect.left;
        } else if (direction == 33) {
            return (srcRect.bottom > destRect.bottom || srcRect.top >= destRect.bottom) && srcRect.top > destRect.top;
        } else if (direction == 66) {
            return (srcRect.left < destRect.left || srcRect.right <= destRect.left) && srcRect.right < destRect.right;
        } else if (direction == 130) {
            return (srcRect.top < destRect.top || srcRect.bottom <= destRect.top) && srcRect.bottom < destRect.bottom;
        } else {
            throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean beamsOverlap(int direction, Rect rect1, Rect rect2) {
        if (direction != 17) {
            if (direction != 33) {
                if (direction != 66) {
                    if (direction != 130) {
                        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                    }
                }
            }
            return rect2.right > rect1.left && rect2.left < rect1.right;
        }
        return rect2.bottom > rect1.top && rect2.top < rect1.bottom;
    }

    boolean isToDirectionOf(int direction, Rect src, Rect dest) {
        if (direction == 17) {
            return src.left >= dest.right;
        } else if (direction == 33) {
            return src.top >= dest.bottom;
        } else if (direction == 66) {
            return src.right <= dest.left;
        } else if (direction == 130) {
            return src.bottom <= dest.top;
        } else {
            throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int majorAxisDistance(int direction, Rect source, Rect dest) {
        return Math.max(0, majorAxisDistanceRaw(direction, source, dest));
    }

    static int majorAxisDistanceRaw(int direction, Rect source, Rect dest) {
        if (direction != 17) {
            if (direction != 33) {
                if (direction != 66) {
                    if (direction == 130) {
                        return dest.top - source.bottom;
                    }
                    throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                }
                return dest.left - source.right;
            }
            return source.top - dest.bottom;
        }
        return source.left - dest.right;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int majorAxisDistanceToFarEdge(int direction, Rect source, Rect dest) {
        return Math.max(1, majorAxisDistanceToFarEdgeRaw(direction, source, dest));
    }

    static int majorAxisDistanceToFarEdgeRaw(int direction, Rect source, Rect dest) {
        if (direction != 17) {
            if (direction != 33) {
                if (direction != 66) {
                    if (direction == 130) {
                        return dest.bottom - source.bottom;
                    }
                    throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                }
                return dest.right - source.right;
            }
            return source.top - dest.top;
        }
        return source.left - dest.left;
    }

    static int minorAxisDistance(int direction, Rect source, Rect dest) {
        if (direction != 17) {
            if (direction != 33) {
                if (direction != 66) {
                    if (direction != 130) {
                        throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
                    }
                }
            }
            return Math.abs((source.left + (source.width() / 2)) - (dest.left + (dest.width() / 2)));
        }
        return Math.abs((source.top + (source.height() / 2)) - (dest.top + (dest.height() / 2)));
    }

    public View findNearestTouchable(ViewGroup root, int x, int y, int direction, int[] deltas) {
        FocusFinder focusFinder = this;
        ArrayList<View> touchables = root.getTouchables();
        int minDistance = Integer.MAX_VALUE;
        View closest = null;
        int numTouchables = touchables.size();
        int edgeSlop = ViewConfiguration.get(root.mContext).getScaledEdgeSlop();
        Rect closestBounds = new Rect();
        Rect touchableBounds = focusFinder.mOtherRect;
        int i = 0;
        while (i < numTouchables) {
            View touchable = touchables.get(i);
            touchable.getDrawingRect(touchableBounds);
            root.offsetRectBetweenParentAndChild(touchable, touchableBounds, true, true);
            if (focusFinder.isTouchCandidate(x, y, touchableBounds, direction)) {
                int distance = Integer.MAX_VALUE;
                if (direction == 17) {
                    distance = (x - touchableBounds.right) + 1;
                } else if (direction == 33) {
                    distance = (y - touchableBounds.bottom) + 1;
                } else if (direction == 66) {
                    distance = touchableBounds.left;
                } else if (direction == 130) {
                    distance = touchableBounds.top;
                }
                if (distance < edgeSlop && (closest == null || closestBounds.contains(touchableBounds) || (!touchableBounds.contains(closestBounds) && distance < minDistance))) {
                    minDistance = distance;
                    closest = touchable;
                    closestBounds.set(touchableBounds);
                    if (direction == 17) {
                        deltas[0] = -distance;
                    } else if (direction == 33) {
                        deltas[1] = -distance;
                    } else if (direction == 66) {
                        deltas[0] = distance;
                    } else if (direction == 130) {
                        deltas[1] = distance;
                    }
                }
            }
            i++;
            focusFinder = this;
        }
        return closest;
    }

    private boolean isTouchCandidate(int x, int y, Rect destRect, int direction) {
        if (direction == 17) {
            return destRect.left <= x && destRect.top <= y && y <= destRect.bottom;
        } else if (direction == 33) {
            return destRect.top <= y && destRect.left <= x && x <= destRect.right;
        } else if (direction == 66) {
            return destRect.left >= x && destRect.top <= y && y <= destRect.bottom;
        } else if (direction == 130) {
            return destRect.top >= y && destRect.left <= x && x <= destRect.right;
        } else {
            throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
    }

    private static final boolean isValidId(int id) {
        return (id == 0 || id == -1) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static final class FocusSorter {
        private int mLastPoolRect;
        private int mRtlMult;
        private ArrayList<Rect> mRectPool = new ArrayList<>();
        private HashMap<View, Rect> mRectByView = null;
        private Comparator<View> mTopsComparator = new Comparator() { // from class: android.view.-$$Lambda$FocusFinder$FocusSorter$kW7K1t9q7Y62V38r-7g6xRzqqq8
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return FocusFinder.FocusSorter.this.lambda$new$0$FocusFinder$FocusSorter((View) obj, (View) obj2);
            }
        };
        private Comparator<View> mSidesComparator = new Comparator() { // from class: android.view.-$$Lambda$FocusFinder$FocusSorter$h0f2ZYL6peSaaEeCCkAoYs_YZvU
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return FocusFinder.FocusSorter.this.lambda$new$1$FocusFinder$FocusSorter((View) obj, (View) obj2);
            }
        };

        FocusSorter() {
        }

        public /* synthetic */ int lambda$new$0$FocusFinder$FocusSorter(View first, View second) {
            if (first == second) {
                return 0;
            }
            Rect firstRect = this.mRectByView.get(first);
            Rect secondRect = this.mRectByView.get(second);
            int result = firstRect.top - secondRect.top;
            if (result == 0) {
                return firstRect.bottom - secondRect.bottom;
            }
            return result;
        }

        public /* synthetic */ int lambda$new$1$FocusFinder$FocusSorter(View first, View second) {
            if (first == second) {
                return 0;
            }
            Rect firstRect = this.mRectByView.get(first);
            Rect secondRect = this.mRectByView.get(second);
            int result = firstRect.left - secondRect.left;
            if (result == 0) {
                return firstRect.right - secondRect.right;
            }
            return this.mRtlMult * result;
        }

        public void sort(View[] views, int start, int end, ViewGroup root, boolean isRtl) {
            int count = end - start;
            if (count < 2) {
                return;
            }
            if (this.mRectByView == null) {
                this.mRectByView = new HashMap<>();
            }
            this.mRtlMult = isRtl ? -1 : 1;
            for (int i = this.mRectPool.size(); i < count; i++) {
                this.mRectPool.add(new Rect());
            }
            for (int i2 = start; i2 < end; i2++) {
                ArrayList<Rect> arrayList = this.mRectPool;
                int i3 = this.mLastPoolRect;
                this.mLastPoolRect = i3 + 1;
                Rect next = arrayList.get(i3);
                views[i2].getDrawingRect(next);
                root.offsetDescendantRectToMyCoords(views[i2], next);
                this.mRectByView.put(views[i2], next);
            }
            Arrays.sort(views, start, count, this.mTopsComparator);
            int sweepBottom = this.mRectByView.get(views[start]).bottom;
            int rowStart = start;
            int sweepIdx = start + 1;
            while (sweepIdx < end) {
                Rect currRect = this.mRectByView.get(views[sweepIdx]);
                if (currRect.top >= sweepBottom) {
                    if (sweepIdx - rowStart > 1) {
                        Arrays.sort(views, rowStart, sweepIdx, this.mSidesComparator);
                    }
                    sweepBottom = currRect.bottom;
                    rowStart = sweepIdx;
                } else {
                    sweepBottom = Math.max(sweepBottom, currRect.bottom);
                }
                sweepIdx++;
            }
            if (sweepIdx - rowStart > 1) {
                Arrays.sort(views, rowStart, sweepIdx, this.mSidesComparator);
            }
            this.mLastPoolRect = 0;
            this.mRectByView.clear();
        }
    }

    public static void sort(View[] views, int start, int end, ViewGroup root, boolean isRtl) {
        getInstance().mFocusSorter.sort(views, start, end, root, isRtl);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class UserSpecifiedFocusComparator implements Comparator<View> {
        private final NextFocusGetter mNextFocusGetter;
        private View mRoot;
        private final ArrayMap<View, View> mNextFoci = new ArrayMap<>();
        private final ArraySet<View> mIsConnectedTo = new ArraySet<>();
        private final ArrayMap<View, View> mHeadsOfChains = new ArrayMap<>();
        private final ArrayMap<View, Integer> mOriginalOrdinal = new ArrayMap<>();

        /* loaded from: classes3.dex */
        public interface NextFocusGetter {
            View get(View view, View view2);
        }

        UserSpecifiedFocusComparator(NextFocusGetter nextFocusGetter) {
            this.mNextFocusGetter = nextFocusGetter;
        }

        public void recycle() {
            this.mRoot = null;
            this.mHeadsOfChains.clear();
            this.mIsConnectedTo.clear();
            this.mOriginalOrdinal.clear();
            this.mNextFoci.clear();
        }

        public void setFocusables(List<View> focusables, View root) {
            this.mRoot = root;
            for (int i = 0; i < focusables.size(); i++) {
                this.mOriginalOrdinal.put(focusables.get(i), Integer.valueOf(i));
            }
            int i2 = focusables.size();
            for (int i3 = i2 - 1; i3 >= 0; i3--) {
                View view = focusables.get(i3);
                View next = this.mNextFocusGetter.get(this.mRoot, view);
                if (next != null && this.mOriginalOrdinal.containsKey(next)) {
                    this.mNextFoci.put(view, next);
                    this.mIsConnectedTo.add(next);
                }
            }
            int i4 = focusables.size();
            for (int i5 = i4 - 1; i5 >= 0; i5--) {
                View view2 = focusables.get(i5);
                if (this.mNextFoci.get(view2) != null && !this.mIsConnectedTo.contains(view2)) {
                    setHeadOfChain(view2);
                }
            }
        }

        private void setHeadOfChain(View head) {
            View view = head;
            while (view != null) {
                View otherHead = this.mHeadsOfChains.get(view);
                if (otherHead != null) {
                    if (otherHead == head) {
                        return;
                    }
                    view = head;
                    head = otherHead;
                }
                this.mHeadsOfChains.put(view, head);
                view = this.mNextFoci.get(view);
            }
        }

        @Override // java.util.Comparator
        public int compare(View first, View second) {
            if (first == second) {
                return 0;
            }
            View firstHead = this.mHeadsOfChains.get(first);
            View secondHead = this.mHeadsOfChains.get(second);
            if (firstHead == secondHead && firstHead != null) {
                if (first == firstHead) {
                    return -1;
                }
                return (second == firstHead || this.mNextFoci.get(first) == null) ? 1 : -1;
            }
            boolean involvesChain = false;
            if (firstHead != null) {
                first = firstHead;
                involvesChain = true;
            }
            if (secondHead != null) {
                second = secondHead;
                involvesChain = true;
            }
            if (involvesChain) {
                return this.mOriginalOrdinal.get(first).intValue() < this.mOriginalOrdinal.get(second).intValue() ? -1 : 1;
            }
            return 0;
        }
    }
}
