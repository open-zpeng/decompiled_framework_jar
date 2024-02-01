package android.net.wifi.rtt;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class RangingResultCallback {
    public static final int STATUS_CODE_FAIL = 1;
    public static final int STATUS_CODE_FAIL_RTT_NOT_AVAILABLE = 2;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface RangingOperationStatus {
    }

    public abstract void onRangingFailure(int i);

    public abstract void onRangingResults(List<RangingResult> list);
}
