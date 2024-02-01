package android.app;

import android.app.IXpConfigService;
import android.content.Context;
import android.os.IBinder;
import android.os.ServiceManager;
import android.util.Singleton;

/* loaded from: classes.dex */
public class XpConfigManager {
    private static String TAG = "XpConfigManager";
    private static final Singleton<IXpConfigService> IXpConfigServiceSingleton = new Singleton<IXpConfigService>() { // from class: android.app.XpConfigManager.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.util.Singleton
        public IXpConfigService create() {
            IBinder b = ServiceManager.getService(Context.XP_CONFIG_SERVICE);
            IXpConfigService am = IXpConfigService.Stub.asInterface(b);
            return am;
        }
    };

    public static IXpConfigService getService() {
        return IXpConfigServiceSingleton.get();
    }

    public XpConfigManager(Context ctx, IXpConfigService service) {
    }
}
