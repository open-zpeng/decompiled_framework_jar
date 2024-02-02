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
/* loaded from: classes2.dex */
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

    private synchronized FocusFinder() {
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

    private synchronized View findNextFocus(ViewGroup root, View focused, Rect focusedRect, int direction) {
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

    private synchronized ViewGroup getEffectiveRoot(ViewGroup root, View focused) {
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

    private synchronized View findNextUserSpecifiedKeyboardNavigationCluster(View root, View currentCluster, int direction) {
        View userSetNextCluster = currentCluster.findUserSetNextKeyboardNavigationCluster(root, direction);
        if (userSetNextCluster != null && userSetNextCluster.hasFocusable()) {
            return userSetNextCluster;
        }
        return null;
    }

    private synchronized View findNextUserSpecifiedFocus(ViewGroup root, View focused, int direction) {
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

    private synchronized View findNextFocus(ViewGroup root, View focused, Rect focusedRect, int direction, ArrayList<View> focusables) {
        Rect focusedRect2;
        Rect focusedRect3;
        if (focused != null) {
            if (focusedRect == null) {
                focusedRect3 = this.mFocusedRect;
            } else {
                focusedRect3 = focusedRect;
            }
            focused.getFocusedRect(focusedRect3);
            root.offsetDescendantRectToMyCoords(focused, focusedRect3);
        } else if (focusedRect == null) {
            focusedRect3 = this.mFocusedRect;
            if (direction != 17 && direction != 33) {
                if (direction == 66 || direction == 130) {
                    setFocusTopLeft(root, focusedRect3);
                } else {
                    switch (direction) {
                        case 1:
                            if (root.isLayoutRtl()) {
                                setFocusTopLeft(root, focusedRect3);
                                break;
                            } else {
                                setFocusBottomRight(root, focusedRect3);
                                break;
                            }
                        case 2:
                            if (root.isLayoutRtl()) {
                                setFocusBottomRight(root, focusedRect3);
                                break;
                            } else {
                                setFocusTopLeft(root, focusedRect3);
                                break;
                            }
                    }
                }
            } else {
                setFocusBottomRight(root, focusedRect3);
            }
        } else {
            focusedRect2 = focusedRect;
            if (direction == 17 && direction != 33 && direction != 66 && direction != 130) {
                switch (direction) {
                    case 1:
                    case 2:
                        return findNextFocusInRelativeDirection(focusables, root, focused, focusedRect2, direction);
                    default:
                        throw new IllegalArgumentException("Unknown direction: " + direction);
                }
            }
            return findNextFocusInAbsoluteDirection(focusables, root, focused, focusedRect2, direction);
        }
        focusedRect2 = focusedRect3;
        if (direction == 17) {
        }
        return findNextFocusInAbsoluteDirection(focusables, root, focused, focusedRect2, direction);
    }

    private synchronized View findNextKeyboardNavigationCluster(View root, View currentCluster, List<View> clusters, int direction) {
        try {
            this.mUserSpecifiedClusterComparator.setFocusables(clusters, root);
            Collections.sort(clusters, this.mUserSpecifiedClusterComparator);
            this.mUserSpecifiedClusterComparator.recycle();
            int count = clusters.size();
            if (direction != 17 && direction != 33) {
                if (direction != 66 && direction != 130) {
                    switch (direction) {
                        case 1:
                            break;
                        case 2:
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown direction: " + direction);
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

    private synchronized View findNextFocusInRelativeDirection(ArrayList<View> focusables, ViewGroup root, View focused, Rect focusedRect, int direction) {
        try {
            this.mUserSpecifiedFocusComparator.setFocusables(focusables, root);
            Collections.sort(focusables, this.mUserSpecifiedFocusComparator);
            this.mUserSpecifiedFocusComparator.recycle();
            int count = focusables.size();
            switch (direction) {
                case 1:
                    return getPreviousFocusable(focused, focusables, count);
                case 2:
                    return getNextFocusable(focused, focusables, count);
                default:
                    return focusables.get(count - 1);
            }
        } catch (Throwable th) {
            this.mUserSpecifiedFocusComparator.recycle();
            throw th;
        }
    }

    private synchronized void setFocusBottomRight(ViewGroup root, Rect focusedRect) {
        int rootBottom = root.getScrollY() + root.getHeight();
        int rootRight = root.getScrollX() + root.getWidth();
        focusedRect.set(rootRight, rootBottom, rootRight, rootBottom);
    }

    private synchronized void setFocusTopLeft(ViewGroup root, Rect focusedRect) {
        int rootTop = root.getScrollY();
        int rootLeft = root.getScrollX();
        focusedRect.set(rootLeft, rootTop, rootLeft, rootTop);
    }

    synchronized View findNextFocusInAbsoluteDirection(ArrayList<View> focusables, ViewGroup root, View focused, Rect focusedRect, int direction) {
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

    private static synchronized View getNextFocusable(View focused, ArrayList<View> focusables, int count) {
        int position;
        if (focused != null && (position = focusables.lastIndexOf(focused)) >= 0 && position + 1 < count) {
            return focusables.get(position + 1);
        }
        if (!focusables.isEmpty()) {
            return focusables.get(0);
        }
        return null;
    }

    private static synchronized View getPreviousFocusable(View focused, ArrayList<View> focusables, int count) {
        int position;
        if (focused != null && (position = focusables.indexOf(focused)) > 0) {
            return focusables.get(position - 1);
        }
        if (!focusables.isEmpty()) {
            return focusables.get(count - 1);
        }
        return null;
    }

    private static synchronized View getNextKeyboardNavigationCluster(View root, View currentCluster, List<View> clusters, int count) {
        if (currentCluster == null) {
            return clusters.get(0);
        }
        int position = clusters.lastIndexOf(currentCluster);
        if (position >= 0 && position + 1 < count) {
            return clusters.get(position + 1);
        }
        return root;
    }

    private static synchronized View getPreviousKeyboardNavigationCluster(View root, View currentCluster, List<View> clusters, int count) {
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
    public synchronized boolean isBetterCandidate(int direction, Rect source, Rect rect1, Rect rect2) {
        if (isCandidate(source, rect1, direction)) {
            if (isCandidate(source, rect2, direction) && !beamBeats(direction, source, rect1, rect2)) {
                return !beamBeats(direction, source, rect2, rect1) && getWeightedDistanceFor((long) majorAxisDistance(direction, source, rect1), (long) minorAxisDistance(direction, source, rect1)) < getWeightedDistanceFor((long) majorAxisDistance(direction, source, rect2), (long) minorAxisDistance(direction, source, rect2));
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean beamBeats(int direction, Rect source, Rect rect1, Rect rect2) {
        boolean rect1InSrcBeam = beamsOverlap(direction, source, rect1);
        boolean rect2InSrcBeam = beamsOverlap(direction, source, rect2);
        if (rect2InSrcBeam || !rect1InSrcBeam) {
            return false;
        }
        return !isToDirectionOf(direction, source, rect2) || direction == 17 || direction == 66 || majorAxisDistance(direction, source, rect1) < majorAxisDistanceToFarEdge(direction, source, rect2);
    }

    synchronized long getWeightedDistanceFor(long majorAxisDistance, long minorAxisDistance) {
        return (13 * majorAxisDistance * majorAxisDistance) + (minorAxisDistance * minorAxisDistance);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean isCandidate(Rect srcRect, Rect destRect, int direction) {
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
    public synchronized boolean beamsOverlap(int direction, Rect rect1, Rect rect2) {
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

    synchronized boolean isToDirectionOf(int direction, Rect src, Rect dest) {
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
    public static synchronized int majorAxisDistance(int direction, Rect source, Rect dest) {
        return Math.max(0, majorAxisDistanceRaw(direction, source, dest));
    }

    static synchronized int majorAxisDistanceRaw(int direction, Rect source, Rect dest) {
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
    public static synchronized int majorAxisDistanceToFarEdge(int direction, Rect source, Rect dest) {
        return Math.max(1, majorAxisDistanceToFarEdgeRaw(direction, source, dest));
    }

    static synchronized int majorAxisDistanceToFarEdgeRaw(int direction, Rect source, Rect dest) {
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

    static synchronized int minorAxisDistance(int direction, Rect source, Rect dest) {
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

    /* JADX WARN: Removed duplicated region for block: B:28:0x0086  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00a2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.view.View findNearestTouchable(android.view.ViewGroup r20, int r21, int r22, int r23, int[] r24) {
        /*
            r19 = this;
            r0 = r19
            r1 = r20
            r2 = r21
            r3 = r22
            r4 = r23
            java.util.ArrayList r5 = r20.getTouchables()
            r6 = 2147483647(0x7fffffff, float:NaN)
            r7 = 0
            int r8 = r5.size()
            android.content.Context r9 = r1.mContext
            android.view.ViewConfiguration r9 = android.view.ViewConfiguration.get(r9)
            int r9 = r9.getScaledEdgeSlop()
            android.graphics.Rect r10 = new android.graphics.Rect
            r10.<init>()
            android.graphics.Rect r11 = r0.mOtherRect
            r13 = r6
            r6 = 0
        L29:
            if (r6 >= r8) goto Laf
            java.lang.Object r14 = r5.get(r6)
            android.view.View r14 = (android.view.View) r14
            r14.getDrawingRect(r11)
            r12 = 1
            r1.offsetRectBetweenParentAndChild(r14, r11, r12, r12)
            boolean r16 = r0.isTouchCandidate(r2, r3, r11, r4)
            if (r16 != 0) goto L3f
            goto L92
        L3f:
            r16 = 2147483647(0x7fffffff, float:NaN)
            r12 = 33
            r0 = 17
            if (r4 == r0) goto L64
            if (r4 == r12) goto L5b
            r12 = 66
            if (r4 == r12) goto L58
            r12 = 130(0x82, float:1.82E-43)
            if (r4 == r12) goto L55
        L52:
            r12 = r16
            goto L6d
        L55:
            int r12 = r11.top
            goto L6d
        L58:
            int r12 = r11.left
            goto L6d
        L5b:
            int r12 = r11.bottom
            int r12 = r3 - r12
            r17 = 1
            int r16 = r12 + 1
            goto L52
        L64:
            r17 = 1
            int r12 = r11.right
            int r12 = r2 - r12
            int r16 = r12 + 1
            goto L52
        L6d:
            if (r12 >= r9) goto L92
            if (r7 == 0) goto L7f
            boolean r16 = r10.contains(r11)
            if (r16 != 0) goto L7f
            boolean r16 = r11.contains(r10)
            if (r16 != 0) goto L92
            if (r12 >= r13) goto L92
        L7f:
            r13 = r12
            r7 = r14
            r10.set(r11)
            if (r4 == r0) goto La2
            r0 = 33
            if (r4 == r0) goto L9c
            r0 = 66
            if (r4 == r0) goto L98
            r0 = 130(0x82, float:1.82E-43)
            if (r4 == r0) goto L94
        L92:
            r15 = 0
            goto La7
        L94:
            r0 = 1
            r24[r0] = r12
            goto L92
        L98:
            r15 = 0
            r24[r15] = r12
            goto La7
        L9c:
            r0 = 1
            r15 = 0
            int r1 = -r12
            r24[r0] = r1
            goto La7
        La2:
            r15 = 0
            int r0 = -r12
            r24[r15] = r0
        La7:
            int r6 = r6 + 1
            r0 = r19
            r1 = r20
            goto L29
        Laf:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.FocusFinder.findNearestTouchable(android.view.ViewGroup, int, int, int, int[]):android.view.View");
    }

    private synchronized boolean isTouchCandidate(int x, int y, Rect destRect, int direction) {
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

    private static final synchronized boolean isValidId(int id) {
        return (id == 0 || id == -1) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static final class FocusSorter {
        private int mLastPoolRect;
        private int mRtlMult;
        private ArrayList<Rect> mRectPool = new ArrayList<>();
        private HashMap<View, Rect> mRectByView = null;
        private Comparator<View> mTopsComparator = new Comparator() { // from class: android.view.-$$Lambda$FocusFinder$FocusSorter$kW7K1t9q7Y62V38r-7g6xRzqqq8
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return FocusFinder.FocusSorter.lambda$new$0(FocusFinder.FocusSorter.this, (View) obj, (View) obj2);
            }
        };
        private Comparator<View> mSidesComparator = new Comparator() { // from class: android.view.-$$Lambda$FocusFinder$FocusSorter$h0f2ZYL6peSaaEeCCkAoYs_YZvU
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return FocusFinder.FocusSorter.lambda$new$1(FocusFinder.FocusSorter.this, (View) obj, (View) obj2);
            }
        };

        synchronized FocusSorter() {
        }

        public static /* synthetic */ int lambda$new$0(FocusSorter focusSorter, View first, View second) {
            if (first == second) {
                return 0;
            }
            Rect firstRect = focusSorter.mRectByView.get(first);
            Rect secondRect = focusSorter.mRectByView.get(second);
            int result = firstRect.top - secondRect.top;
            if (result == 0) {
                return firstRect.bottom - secondRect.bottom;
            }
            return result;
        }

        public static /* synthetic */ int lambda$new$1(FocusSorter focusSorter, View first, View second) {
            if (first == second) {
                return 0;
            }
            Rect firstRect = focusSorter.mRectByView.get(first);
            Rect secondRect = focusSorter.mRectByView.get(second);
            int result = firstRect.left - secondRect.left;
            if (result == 0) {
                return firstRect.right - secondRect.right;
            }
            return focusSorter.mRtlMult * result;
        }

        public synchronized void sort(View[] views, int start, int end, ViewGroup root, boolean isRtl) {
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
    /* loaded from: classes2.dex */
    public static final class UserSpecifiedFocusComparator implements Comparator<View> {
        private final NextFocusGetter mNextFocusGetter;
        private View mRoot;
        private final ArrayMap<View, View> mNextFoci = new ArrayMap<>();
        private final ArraySet<View> mIsConnectedTo = new ArraySet<>();
        private final ArrayMap<View, View> mHeadsOfChains = new ArrayMap<>();
        private final ArrayMap<View, Integer> mOriginalOrdinal = new ArrayMap<>();

        /* loaded from: classes2.dex */
        public interface NextFocusGetter {
            synchronized View get(View view, View view2);
        }

        synchronized UserSpecifiedFocusComparator(NextFocusGetter nextFocusGetter) {
            this.mNextFocusGetter = nextFocusGetter;
        }

        public synchronized void recycle() {
            this.mRoot = null;
            this.mHeadsOfChains.clear();
            this.mIsConnectedTo.clear();
            this.mOriginalOrdinal.clear();
            this.mNextFoci.clear();
        }

        public synchronized void setFocusables(List<View> focusables, View root) {
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

        private synchronized void setHeadOfChain(View view) {
            View head = view;
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
        public synchronized int compare(View first, View second) {
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
