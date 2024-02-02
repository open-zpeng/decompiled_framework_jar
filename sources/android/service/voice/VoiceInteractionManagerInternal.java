package android.service.voice;

import android.os.Bundle;
import android.os.IBinder;
/* loaded from: classes2.dex */
public abstract class VoiceInteractionManagerInternal {
    public abstract synchronized void startLocalVoiceInteraction(IBinder iBinder, Bundle bundle);

    public abstract synchronized void stopLocalVoiceInteraction(IBinder iBinder);

    public abstract synchronized boolean supportsLocalVoiceInteraction();
}
