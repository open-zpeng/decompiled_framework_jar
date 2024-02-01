package android.media.update;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
/* loaded from: classes2.dex */
public interface ViewGroupProvider {
    /* JADX INFO: Access modifiers changed from: private */
    synchronized boolean checkLayoutParams_impl(ViewGroup.LayoutParams layoutParams);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized boolean dispatchTouchEvent_impl(MotionEvent motionEvent);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized ViewGroup.LayoutParams generateDefaultLayoutParams_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized ViewGroup.LayoutParams generateLayoutParams_impl(AttributeSet attributeSet);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized ViewGroup.LayoutParams generateLayoutParams_impl(ViewGroup.LayoutParams layoutParams);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized CharSequence getAccessibilityClassName_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized int getSuggestedMinimumHeight_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized int getSuggestedMinimumWidth_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void measureChildWithMargins_impl(View view, int i, int i2, int i3, int i4);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void onAttachedToWindow_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void onDetachedFromWindow_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void onFinishInflate_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void onLayout_impl(boolean z, int i, int i2, int i3, int i4);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void onMeasure_impl(int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized boolean onTouchEvent_impl(MotionEvent motionEvent);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized boolean onTrackballEvent_impl(MotionEvent motionEvent);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void onVisibilityAggregated_impl(boolean z);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized void setEnabled_impl(boolean z);

    private protected synchronized void setMeasuredDimension_impl(int i, int i2);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized boolean shouldDelayChildPressedState_impl();
}
