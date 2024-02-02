package android.view;

import android.graphics.Rect;
/* loaded from: classes2.dex */
public class FocusFinderHelper {
    private FocusFinder mFocusFinder;

    public synchronized FocusFinderHelper(FocusFinder focusFinder) {
        this.mFocusFinder = focusFinder;
    }

    public synchronized boolean isBetterCandidate(int direction, Rect source, Rect rect1, Rect rect2) {
        return this.mFocusFinder.isBetterCandidate(direction, source, rect1, rect2);
    }

    public synchronized boolean beamBeats(int direction, Rect source, Rect rect1, Rect rect2) {
        return this.mFocusFinder.beamBeats(direction, source, rect1, rect2);
    }

    public synchronized boolean isCandidate(Rect srcRect, Rect destRect, int direction) {
        return this.mFocusFinder.isCandidate(srcRect, destRect, direction);
    }

    public synchronized boolean beamsOverlap(int direction, Rect rect1, Rect rect2) {
        return this.mFocusFinder.beamsOverlap(direction, rect1, rect2);
    }

    public static synchronized int majorAxisDistance(int direction, Rect source, Rect dest) {
        return FocusFinder.majorAxisDistance(direction, source, dest);
    }

    public static synchronized int majorAxisDistanceToFarEdge(int direction, Rect source, Rect dest) {
        return FocusFinder.majorAxisDistanceToFarEdge(direction, source, dest);
    }
}
