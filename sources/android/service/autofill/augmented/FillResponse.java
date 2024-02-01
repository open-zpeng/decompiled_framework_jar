package android.service.autofill.augmented;

import android.annotation.SystemApi;

@SystemApi
/* loaded from: classes2.dex */
public final class FillResponse {
    private final FillWindow mFillWindow;

    private FillResponse(Builder builder) {
        this.mFillWindow = builder.mFillWindow;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FillWindow getFillWindow() {
        return this.mFillWindow;
    }

    @SystemApi
    /* loaded from: classes2.dex */
    public static final class Builder {
        private FillWindow mFillWindow;

        public Builder setFillWindow(FillWindow fillWindow) {
            this.mFillWindow = fillWindow;
            return this;
        }

        public FillResponse build() {
            return new FillResponse(this);
        }
    }
}
