package android.media;

import android.media.MediaDrm;
import java.util.function.Consumer;
import java.util.function.Function;

/* compiled from: lambda */
/* renamed from: android.media.-$$Lambda$MediaDrm$dloezJ1eKxYxi1Oq-oYrMXoRpPM  reason: invalid class name */
/* loaded from: classes2.dex */
public final /* synthetic */ class $$Lambda$MediaDrm$dloezJ1eKxYxi1OqoYrMXoRpPM implements Function {
    private final /* synthetic */ MediaDrm f$0;

    public /* synthetic */ $$Lambda$MediaDrm$dloezJ1eKxYxi1OqoYrMXoRpPM(MediaDrm mediaDrm) {
        this.f$0 = mediaDrm;
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        Consumer createOnExpirationUpdateListener;
        createOnExpirationUpdateListener = this.f$0.createOnExpirationUpdateListener((MediaDrm.OnExpirationUpdateListener) obj);
        return createOnExpirationUpdateListener;
    }
}
