package com.android.internal.widget;

import android.util.ArraySet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
/* loaded from: classes3.dex */
public class ViewClippingUtil {
    private static final int CLIP_CHILDREN_TAG = 16908892;
    private static final int CLIP_CLIPPING_SET = 16908891;
    private static final int CLIP_TO_PADDING = 16908894;

    public static void setClippingDeactivated(View transformedView, boolean deactivated, ClippingParameters clippingParameters) {
        if ((!deactivated && !clippingParameters.isClippingEnablingAllowed(transformedView)) || !(transformedView.getParent() instanceof ViewGroup)) {
            return;
        }
        ViewGroup parent = (ViewGroup) transformedView.getParent();
        while (true) {
            if (!deactivated && !clippingParameters.isClippingEnablingAllowed(transformedView)) {
                return;
            }
            ArraySet<View> clipSet = (ArraySet) parent.getTag(CLIP_CLIPPING_SET);
            if (clipSet == null) {
                clipSet = new ArraySet<>();
                parent.setTagInternal(CLIP_CLIPPING_SET, clipSet);
            }
            Boolean clipChildren = (Boolean) parent.getTag(CLIP_CHILDREN_TAG);
            if (clipChildren == null) {
                clipChildren = Boolean.valueOf(parent.getClipChildren());
                parent.setTagInternal(CLIP_CHILDREN_TAG, clipChildren);
            }
            Boolean clipToPadding = (Boolean) parent.getTag(CLIP_TO_PADDING);
            if (clipToPadding == null) {
                clipToPadding = Boolean.valueOf(parent.getClipToPadding());
                parent.setTagInternal(CLIP_TO_PADDING, clipToPadding);
            }
            if (!deactivated) {
                clipSet.remove(transformedView);
                if (clipSet.isEmpty()) {
                    parent.setClipChildren(clipChildren.booleanValue());
                    parent.setClipToPadding(clipToPadding.booleanValue());
                    parent.setTagInternal(CLIP_CLIPPING_SET, null);
                    clippingParameters.onClippingStateChanged(parent, true);
                }
            } else {
                clipSet.add(transformedView);
                parent.setClipChildren(false);
                parent.setClipToPadding(false);
                clippingParameters.onClippingStateChanged(parent, false);
            }
            if (clippingParameters.shouldFinish(parent)) {
                return;
            }
            ViewParent viewParent = parent.getParent();
            if (viewParent instanceof ViewGroup) {
                parent = (ViewGroup) viewParent;
            } else {
                return;
            }
        }
    }

    /* loaded from: classes3.dex */
    public interface ClippingParameters {
        boolean shouldFinish(View view);

        default boolean isClippingEnablingAllowed(View view) {
            return !MessagingPropertyAnimator.isAnimatingTranslation(view);
        }

        default void onClippingStateChanged(View view, boolean isClipping) {
        }
    }
}
