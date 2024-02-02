package android.privacy.internal.longitudinalreporting;

import android.privacy.DifferentialPrivacyConfig;
import android.privacy.internal.rappor.RapporConfig;
import android.text.TextUtils;
import com.android.internal.util.Preconditions;
import com.xiaopeng.util.FeatureOption;
/* loaded from: classes2.dex */
public class LongitudinalReportingConfig implements DifferentialPrivacyConfig {
    public protected static final String ALGORITHM_NAME = "LongitudinalReporting";
    public protected final String mEncoderId;
    public protected final RapporConfig mIRRConfig;
    public protected final double mProbabilityF;
    public protected final double mProbabilityP;
    public protected final double mProbabilityQ;

    private protected synchronized LongitudinalReportingConfig(String encoderId, double probabilityF, double probabilityP, double probabilityQ) {
        boolean z = false;
        Preconditions.checkArgument(probabilityF >= FeatureOption.FO_BOOT_POLICY_CPU && probabilityF <= 1.0d, "probabilityF must be in range [0.0, 1.0]");
        this.mProbabilityF = probabilityF;
        Preconditions.checkArgument(probabilityP >= FeatureOption.FO_BOOT_POLICY_CPU && probabilityP <= 1.0d, "probabilityP must be in range [0.0, 1.0]");
        this.mProbabilityP = probabilityP;
        if (probabilityQ >= FeatureOption.FO_BOOT_POLICY_CPU && probabilityQ <= 1.0d) {
            z = true;
        }
        Preconditions.checkArgument(z, "probabilityQ must be in range [0.0, 1.0]");
        this.mProbabilityQ = probabilityQ;
        Preconditions.checkArgument(!TextUtils.isEmpty(encoderId), "encoderId cannot be empty");
        this.mEncoderId = encoderId;
        this.mIRRConfig = new RapporConfig(encoderId, 1, FeatureOption.FO_BOOT_POLICY_CPU, probabilityF, 1.0d - probabilityF, 1, 1);
    }

    private protected synchronized String getAlgorithm() {
        return ALGORITHM_NAME;
    }

    public private protected synchronized RapporConfig getIRRConfig() {
        return this.mIRRConfig;
    }

    public private protected synchronized double getProbabilityP() {
        return this.mProbabilityP;
    }

    public private protected synchronized double getProbabilityQ() {
        return this.mProbabilityQ;
    }

    public private protected synchronized String getEncoderId() {
        return this.mEncoderId;
    }

    public String toString() {
        return String.format("EncoderId: %s, ProbabilityF: %.3f, ProbabilityP: %.3f, ProbabilityQ: %.3f", this.mEncoderId, Double.valueOf(this.mProbabilityF), Double.valueOf(this.mProbabilityP), Double.valueOf(this.mProbabilityQ));
    }
}
