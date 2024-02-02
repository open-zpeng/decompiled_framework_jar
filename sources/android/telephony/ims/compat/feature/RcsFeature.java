package android.telephony.ims.compat.feature;

import com.android.ims.internal.IImsRcsFeature;
/* loaded from: classes2.dex */
public class RcsFeature extends ImsFeature {
    private final IImsRcsFeature mImsRcsBinder = new IImsRcsFeature.Stub() { // from class: android.telephony.ims.compat.feature.RcsFeature.1
    };

    @Override // android.telephony.ims.compat.feature.ImsFeature
    public synchronized void onFeatureReady() {
    }

    @Override // android.telephony.ims.compat.feature.ImsFeature
    public synchronized void onFeatureRemoved() {
    }

    @Override // android.telephony.ims.compat.feature.ImsFeature
    public final synchronized IImsRcsFeature getBinder() {
        return this.mImsRcsBinder;
    }
}
