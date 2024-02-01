package android.app.admin;

import android.content.Intent;
import java.util.List;
/* loaded from: classes.dex */
public abstract class DevicePolicyManagerInternal {

    /* loaded from: classes.dex */
    public interface OnCrossProfileWidgetProvidersChangeListener {
        synchronized void onCrossProfileWidgetProvidersChanged(int i, List<String> list);
    }

    public abstract synchronized void addOnCrossProfileWidgetProvidersChangeListener(OnCrossProfileWidgetProvidersChangeListener onCrossProfileWidgetProvidersChangeListener);

    public abstract synchronized boolean canUserHaveUntrustedCredentialReset(int i);

    public abstract synchronized Intent createShowAdminSupportIntent(int i, boolean z);

    public abstract synchronized Intent createUserRestrictionSupportIntent(int i, String str);

    public abstract synchronized List<String> getCrossProfileWidgetProviders(int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract synchronized DevicePolicyCache getDevicePolicyCache();

    public abstract synchronized CharSequence getPrintingDisabledReasonForUser(int i);

    public abstract synchronized boolean isActiveAdminWithPolicy(int i, int i2);

    public abstract synchronized boolean isUserAffiliatedWithDevice(int i);

    public abstract synchronized void reportSeparateProfileChallengeChanged(int i);
}
