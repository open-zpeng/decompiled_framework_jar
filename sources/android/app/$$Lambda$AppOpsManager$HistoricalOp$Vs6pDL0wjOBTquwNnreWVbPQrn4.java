package android.app;

import android.app.AppOpsManager;
import android.util.LongSparseLongArray;
import java.util.function.Supplier;

/* compiled from: lambda */
/* renamed from: android.app.-$$Lambda$AppOpsManager$HistoricalOp$Vs6pDL0wjOBTquwNnreWVbPQrn4  reason: invalid class name */
/* loaded from: classes.dex */
public final /* synthetic */ class $$Lambda$AppOpsManager$HistoricalOp$Vs6pDL0wjOBTquwNnreWVbPQrn4 implements Supplier {
    private final /* synthetic */ AppOpsManager.HistoricalOp f$0;

    public /* synthetic */ $$Lambda$AppOpsManager$HistoricalOp$Vs6pDL0wjOBTquwNnreWVbPQrn4(AppOpsManager.HistoricalOp historicalOp) {
        this.f$0 = historicalOp;
    }

    @Override // java.util.function.Supplier
    public final Object get() {
        LongSparseLongArray orCreateAccessDuration;
        orCreateAccessDuration = this.f$0.getOrCreateAccessDuration();
        return orCreateAccessDuration;
    }
}
