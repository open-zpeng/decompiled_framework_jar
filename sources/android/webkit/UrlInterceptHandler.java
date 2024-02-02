package android.webkit;

import android.webkit.CacheManager;
import java.util.Map;
@Deprecated
/* loaded from: classes2.dex */
public interface UrlInterceptHandler {
    /* JADX INFO: Access modifiers changed from: private */
    @Deprecated
    PluginData getPluginData(String str, Map<String, String> map);

    /* JADX INFO: Access modifiers changed from: private */
    @Deprecated
    CacheManager.CacheResult service(String str, Map<String, String> map);
}
