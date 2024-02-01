package android.media.update;

import android.content.Context;
import android.media.update.ViewGroupProvider;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
/* loaded from: classes2.dex */
public abstract class ViewGroupHelper<T extends ViewGroupProvider> extends ViewGroup {
    private protected final T mProvider;

    @FunctionalInterface
    /* loaded from: classes2.dex */
    public interface ProviderCreator<T extends ViewGroupProvider> {
        private protected synchronized T createProvider(ViewGroupHelper<T> viewGroupHelper, ViewGroupProvider viewGroupProvider, ViewGroupProvider viewGroupProvider2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized ViewGroupHelper(ProviderCreator<T> creator, Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mProvider = creator.createProvider(this, new SuperProvider(), new PrivateProvider());
    }

    private protected synchronized T getProvider() {
        return this.mProvider;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        this.mProvider.onAttachedToWindow_impl();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        this.mProvider.onDetachedFromWindow_impl();
    }

    @Override // android.view.ViewGroup, android.view.View
    public CharSequence getAccessibilityClassName() {
        return this.mProvider.getAccessibilityClassName_impl();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        return this.mProvider.onTouchEvent_impl(ev);
    }

    @Override // android.view.View
    public boolean onTrackballEvent(MotionEvent ev) {
        return this.mProvider.onTrackballEvent_impl(ev);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        this.mProvider.onFinishInflate_impl();
    }

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        this.mProvider.setEnabled_impl(enabled);
    }

    @Override // android.view.View
    public void onVisibilityAggregated(boolean isVisible) {
        this.mProvider.onVisibilityAggregated_impl(isVisible);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        this.mProvider.onLayout_impl(changed, left, top, right, bottom);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.mProvider.onMeasure_impl(widthMeasureSpec, heightMeasureSpec);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public int getSuggestedMinimumWidth() {
        return this.mProvider.getSuggestedMinimumWidth_impl();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public int getSuggestedMinimumHeight() {
        return this.mProvider.getSuggestedMinimumHeight_impl();
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return this.mProvider.dispatchTouchEvent_impl(ev);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return this.mProvider.checkLayoutParams_impl(p);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return this.mProvider.generateDefaultLayoutParams_impl();
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return this.mProvider.generateLayoutParams_impl(attrs);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return this.mProvider.generateLayoutParams_impl(lp);
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return this.mProvider.shouldDelayChildPressedState_impl();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        this.mProvider.measureChildWithMargins_impl(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }

    /* loaded from: classes2.dex */
    public class SuperProvider implements ViewGroupProvider {
        public SuperProvider() {
        }

        private protected synchronized CharSequence getAccessibilityClassName_impl() {
            return ViewGroupHelper.super.getAccessibilityClassName();
        }

        private protected synchronized boolean onTouchEvent_impl(MotionEvent ev) {
            return ViewGroupHelper.super.onTouchEvent(ev);
        }

        private protected synchronized boolean onTrackballEvent_impl(MotionEvent ev) {
            return ViewGroupHelper.super.onTrackballEvent(ev);
        }

        private protected synchronized void onFinishInflate_impl() {
            ViewGroupHelper.super.onFinishInflate();
        }

        private protected synchronized void setEnabled_impl(boolean enabled) {
            ViewGroupHelper.super.setEnabled(enabled);
        }

        private protected synchronized void onAttachedToWindow_impl() {
            ViewGroupHelper.super.onAttachedToWindow();
        }

        private protected synchronized void onDetachedFromWindow_impl() {
            ViewGroupHelper.super.onDetachedFromWindow();
        }

        private protected synchronized void onVisibilityAggregated_impl(boolean isVisible) {
            ViewGroupHelper.super.onVisibilityAggregated(isVisible);
        }

        private protected synchronized void onLayout_impl(boolean changed, int left, int top, int right, int bottom) {
        }

        private protected synchronized void onMeasure_impl(int widthMeasureSpec, int heightMeasureSpec) {
            ViewGroupHelper.super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        private protected synchronized int getSuggestedMinimumWidth_impl() {
            return ViewGroupHelper.super.getSuggestedMinimumWidth();
        }

        private protected synchronized int getSuggestedMinimumHeight_impl() {
            return ViewGroupHelper.super.getSuggestedMinimumHeight();
        }

        private protected synchronized void setMeasuredDimension_impl(int measuredWidth, int measuredHeight) {
            ViewGroupHelper.super.setMeasuredDimension(measuredWidth, measuredHeight);
        }

        private protected synchronized boolean dispatchTouchEvent_impl(MotionEvent ev) {
            return ViewGroupHelper.super.dispatchTouchEvent(ev);
        }

        private protected synchronized boolean checkLayoutParams_impl(ViewGroup.LayoutParams p) {
            return ViewGroupHelper.super.checkLayoutParams(p);
        }

        private protected synchronized ViewGroup.LayoutParams generateDefaultLayoutParams_impl() {
            return ViewGroupHelper.super.generateDefaultLayoutParams();
        }

        private protected synchronized ViewGroup.LayoutParams generateLayoutParams_impl(AttributeSet attrs) {
            return ViewGroupHelper.super.generateLayoutParams(attrs);
        }

        private protected synchronized ViewGroup.LayoutParams generateLayoutParams_impl(ViewGroup.LayoutParams lp) {
            return ViewGroupHelper.super.generateLayoutParams(lp);
        }

        private protected synchronized boolean shouldDelayChildPressedState_impl() {
            return ViewGroupHelper.super.shouldDelayChildPressedState();
        }

        private protected synchronized void measureChildWithMargins_impl(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
            ViewGroupHelper.super.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
        }
    }

    /* loaded from: classes2.dex */
    public class PrivateProvider implements ViewGroupProvider {
        public PrivateProvider() {
        }

        private protected synchronized CharSequence getAccessibilityClassName_impl() {
            return ViewGroupHelper.this.getAccessibilityClassName();
        }

        private protected synchronized boolean onTouchEvent_impl(MotionEvent ev) {
            return ViewGroupHelper.this.onTouchEvent(ev);
        }

        private protected synchronized boolean onTrackballEvent_impl(MotionEvent ev) {
            return ViewGroupHelper.this.onTrackballEvent(ev);
        }

        private protected synchronized void onFinishInflate_impl() {
            ViewGroupHelper.this.onFinishInflate();
        }

        private protected synchronized void setEnabled_impl(boolean enabled) {
            ViewGroupHelper.this.setEnabled(enabled);
        }

        private protected synchronized void onAttachedToWindow_impl() {
            ViewGroupHelper.this.onAttachedToWindow();
        }

        private protected synchronized void onDetachedFromWindow_impl() {
            ViewGroupHelper.this.onDetachedFromWindow();
        }

        private protected synchronized void onVisibilityAggregated_impl(boolean isVisible) {
            ViewGroupHelper.this.onVisibilityAggregated(isVisible);
        }

        private protected synchronized void onLayout_impl(boolean changed, int left, int top, int right, int bottom) {
            ViewGroupHelper.this.onLayout(changed, left, top, right, bottom);
        }

        private protected synchronized void onMeasure_impl(int widthMeasureSpec, int heightMeasureSpec) {
            ViewGroupHelper.this.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        private protected synchronized int getSuggestedMinimumWidth_impl() {
            return ViewGroupHelper.this.getSuggestedMinimumWidth();
        }

        private protected synchronized int getSuggestedMinimumHeight_impl() {
            return ViewGroupHelper.this.getSuggestedMinimumHeight();
        }

        private protected synchronized void setMeasuredDimension_impl(int measuredWidth, int measuredHeight) {
            ViewGroupHelper.this.setMeasuredDimension(measuredWidth, measuredHeight);
        }

        private protected synchronized boolean dispatchTouchEvent_impl(MotionEvent ev) {
            return ViewGroupHelper.this.dispatchTouchEvent(ev);
        }

        private protected synchronized boolean checkLayoutParams_impl(ViewGroup.LayoutParams p) {
            return ViewGroupHelper.this.checkLayoutParams(p);
        }

        private protected synchronized ViewGroup.LayoutParams generateDefaultLayoutParams_impl() {
            return ViewGroupHelper.this.generateDefaultLayoutParams();
        }

        private protected synchronized ViewGroup.LayoutParams generateLayoutParams_impl(AttributeSet attrs) {
            return ViewGroupHelper.this.generateLayoutParams(attrs);
        }

        private protected synchronized ViewGroup.LayoutParams generateLayoutParams_impl(ViewGroup.LayoutParams lp) {
            return ViewGroupHelper.this.generateLayoutParams(lp);
        }

        private protected synchronized boolean shouldDelayChildPressedState_impl() {
            return ViewGroupHelper.this.shouldDelayChildPressedState();
        }

        private protected synchronized void measureChildWithMargins_impl(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
            ViewGroupHelper.this.measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
        }
    }
}
