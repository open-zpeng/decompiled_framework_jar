package android.security.net.config;

import android.util.Pair;
import java.util.Set;
/* loaded from: classes2.dex */
public interface ConfigSource {
    synchronized NetworkSecurityConfig getDefaultConfig();

    synchronized Set<Pair<Domain, NetworkSecurityConfig>> getPerDomainConfigs();
}
