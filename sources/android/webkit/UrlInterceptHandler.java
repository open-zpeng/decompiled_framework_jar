package android.webkit;

import android.annotation.UnsupportedAppUsage;
import android.webkit.CacheManager;
import java.util.Map;

@Deprecated
/* loaded from: classes3.dex */
public interface UrlInterceptHandler {
    @UnsupportedAppUsage
    @Deprecated
    PluginData getPluginData(String str, Map<String, String> map);

    @UnsupportedAppUsage
    @Deprecated
    CacheManager.CacheResult service(String str, Map<String, String> map);
}
