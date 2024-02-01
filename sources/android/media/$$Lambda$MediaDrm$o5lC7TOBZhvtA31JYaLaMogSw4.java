package android.media;

import android.media.MediaDrm;
import java.util.function.Consumer;
import java.util.function.Function;

/* compiled from: lambda */
/* renamed from: android.media.-$$Lambda$MediaDrm$o5lC7TOBZhvtA31JYaLa-MogSw4  reason: invalid class name */
/* loaded from: classes2.dex */
public final /* synthetic */ class $$Lambda$MediaDrm$o5lC7TOBZhvtA31JYaLaMogSw4 implements Function {
    private final /* synthetic */ MediaDrm f$0;

    public /* synthetic */ $$Lambda$MediaDrm$o5lC7TOBZhvtA31JYaLaMogSw4(MediaDrm mediaDrm) {
        this.f$0 = mediaDrm;
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        Consumer createOnSessionLostStateListener;
        createOnSessionLostStateListener = this.f$0.createOnSessionLostStateListener((MediaDrm.OnSessionLostStateListener) obj);
        return createOnSessionLostStateListener;
    }
}
