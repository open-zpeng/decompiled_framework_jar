package android.view;

import android.graphics.Insets;

/* loaded from: classes3.dex */
public interface WindowInsetsAnimationListener {
    void onFinished(InsetsAnimation insetsAnimation);

    WindowInsets onProgress(WindowInsets windowInsets);

    void onStarted(InsetsAnimation insetsAnimation);

    /* loaded from: classes3.dex */
    public static class InsetsAnimation {
        private final Insets mLowerBound;
        private final int mTypeMask;
        private final Insets mUpperBound;

        /* JADX INFO: Access modifiers changed from: package-private */
        public InsetsAnimation(int typeMask, Insets lowerBound, Insets upperBound) {
            this.mTypeMask = typeMask;
            this.mLowerBound = lowerBound;
            this.mUpperBound = upperBound;
        }

        public int getTypeMask() {
            return this.mTypeMask;
        }

        public Insets getLowerBound() {
            return this.mLowerBound;
        }

        public Insets getUpperBound() {
            return this.mUpperBound;
        }
    }
}
