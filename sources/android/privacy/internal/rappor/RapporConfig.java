package android.privacy.internal.rappor;

import android.privacy.DifferentialPrivacyConfig;
import android.text.TextUtils;
import com.android.internal.util.Preconditions;
import com.xiaopeng.util.FeatureOption;
/* loaded from: classes2.dex */
public class RapporConfig implements DifferentialPrivacyConfig {
    public protected static final String ALGORITHM_NAME = "Rappor";
    public private protected final String mEncoderId;
    public private protected final int mNumBits;
    public private protected final int mNumBloomHashes;
    public private protected final int mNumCohorts;
    public private protected final double mProbabilityF;
    public private protected final double mProbabilityP;
    public private protected final double mProbabilityQ;

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized RapporConfig(String encoderId, int numBits, double probabilityF, double probabilityP, double probabilityQ, int numCohorts, int numBloomHashes) {
        Preconditions.checkArgument(!TextUtils.isEmpty(encoderId), "encoderId cannot be empty");
        this.mEncoderId = encoderId;
        Preconditions.checkArgument(numBits > 0, "numBits needs to be > 0");
        this.mNumBits = numBits;
        Preconditions.checkArgument(probabilityF >= FeatureOption.FO_BOOT_POLICY_CPU && probabilityF <= 1.0d, "probabilityF must be in range [0.0, 1.0]");
        this.mProbabilityF = probabilityF;
        Preconditions.checkArgument(probabilityP >= FeatureOption.FO_BOOT_POLICY_CPU && probabilityP <= 1.0d, "probabilityP must be in range [0.0, 1.0]");
        this.mProbabilityP = probabilityP;
        Preconditions.checkArgument(probabilityQ >= FeatureOption.FO_BOOT_POLICY_CPU && probabilityQ <= 1.0d, "probabilityQ must be in range [0.0, 1.0]");
        this.mProbabilityQ = probabilityQ;
        Preconditions.checkArgument(numCohorts > 0, "numCohorts needs to be > 0");
        this.mNumCohorts = numCohorts;
        Preconditions.checkArgument(numBloomHashes > 0, "numBloomHashes needs to be > 0");
        this.mNumBloomHashes = numBloomHashes;
    }

    private protected synchronized String getAlgorithm() {
        return ALGORITHM_NAME;
    }

    public String toString() {
        return String.format("EncoderId: %s, NumBits: %d, ProbabilityF: %.3f, ProbabilityP: %.3f, ProbabilityQ: %.3f, NumCohorts: %d, NumBloomHashes: %d", this.mEncoderId, Integer.valueOf(this.mNumBits), Double.valueOf(this.mProbabilityF), Double.valueOf(this.mProbabilityP), Double.valueOf(this.mProbabilityQ), Integer.valueOf(this.mNumCohorts), Integer.valueOf(this.mNumBloomHashes));
    }
}
