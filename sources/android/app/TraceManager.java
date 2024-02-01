package android.app;

import android.app.ITraceService;
import android.content.Context;
import android.os.IBinder;
import android.os.ServiceManager;
import android.util.Singleton;
/* loaded from: classes.dex */
public class TraceManager {
    private static String TAG = "TraceManager";
    private static final Singleton<ITraceService> ITraceServiceSingleton = new Singleton<ITraceService>() { // from class: android.app.TraceManager.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.util.Singleton
        public ITraceService create() {
            IBinder b = ServiceManager.getService(Context.TRACE_SERVICE);
            ITraceService am = ITraceService.Stub.asInterface(b);
            return am;
        }
    };

    public static ITraceService getService() {
        return ITraceServiceSingleton.get();
    }

    public TraceManager(Context ctx, ITraceService service) {
    }
}
