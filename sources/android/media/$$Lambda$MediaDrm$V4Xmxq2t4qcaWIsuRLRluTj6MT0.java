package android.media;

import android.media.MediaDrm;
import java.util.function.Consumer;
import java.util.function.Function;

/* compiled from: lambda */
/* renamed from: android.media.-$$Lambda$MediaDrm$V4Xmxq2t4qcaWIsuRLRluTj6MT0  reason: invalid class name */
/* loaded from: classes2.dex */
public final /* synthetic */ class $$Lambda$MediaDrm$V4Xmxq2t4qcaWIsuRLRluTj6MT0 implements Function {
    private final /* synthetic */ MediaDrm f$0;

    public /* synthetic */ $$Lambda$MediaDrm$V4Xmxq2t4qcaWIsuRLRluTj6MT0(MediaDrm mediaDrm) {
        this.f$0 = mediaDrm;
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        Consumer createOnKeyStatusChangeListener;
        createOnKeyStatusChangeListener = this.f$0.createOnKeyStatusChangeListener((MediaDrm.OnKeyStatusChangeListener) obj);
        return createOnKeyStatusChangeListener;
    }
}
