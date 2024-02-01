package com.android.internal.backup;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
/* loaded from: classes3.dex */
public class LocalTransportService extends Service {
    private static LocalTransport sTransport = null;

    @Override // android.app.Service
    public void onCreate() {
        if (sTransport == null) {
            LocalTransportParameters parameters = new LocalTransportParameters(getMainThreadHandler(), getContentResolver());
            sTransport = new LocalTransport(this, parameters);
        }
        sTransport.getParameters().start();
    }

    @Override // android.app.Service
    public void onDestroy() {
        sTransport.getParameters().stop();
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return sTransport.getBinder();
    }
}
