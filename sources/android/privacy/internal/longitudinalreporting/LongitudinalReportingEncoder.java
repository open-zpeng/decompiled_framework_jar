package android.privacy.internal.longitudinalreporting;

import android.privacy.DifferentialPrivacyEncoder;
import android.privacy.internal.rappor.RapporConfig;
import android.privacy.internal.rappor.RapporEncoder;
import com.android.internal.annotations.VisibleForTesting;
import com.xiaopeng.util.FeatureOption;
/* loaded from: classes2.dex */
public class LongitudinalReportingEncoder implements DifferentialPrivacyEncoder {
    public protected static final boolean DEBUG = false;
    public protected static final String PRR1_ENCODER_ID = "prr1_encoder_id";
    public protected static final String PRR2_ENCODER_ID = "prr2_encoder_id";
    public protected static final String TAG = "LongitudinalEncoder";
    public protected final LongitudinalReportingConfig mConfig;
    public protected final Boolean mFakeValue;
    public protected final RapporEncoder mIRREncoder;
    public protected final boolean mIsSecure;

    private protected static synchronized LongitudinalReportingEncoder createEncoder(LongitudinalReportingConfig config, byte[] userSecret) {
        return new LongitudinalReportingEncoder(config, true, userSecret);
    }

    @VisibleForTesting
    private protected static synchronized LongitudinalReportingEncoder createInsecureEncoderForTest(LongitudinalReportingConfig config) {
        return new LongitudinalReportingEncoder(config, false, null);
    }

    public protected synchronized LongitudinalReportingEncoder(LongitudinalReportingConfig config, boolean secureEncoder, byte[] userSecret) {
        RapporEncoder createInsecureEncoderForTest;
        this.mConfig = config;
        this.mIsSecure = secureEncoder;
        double probabilityP = config.getProbabilityP();
        boolean ignoreOriginalInput = getLongTermRandomizedResult(probabilityP, secureEncoder, userSecret, config.getEncoderId() + PRR1_ENCODER_ID);
        if (ignoreOriginalInput) {
            double probabilityQ = config.getProbabilityQ();
            this.mFakeValue = Boolean.valueOf(getLongTermRandomizedResult(probabilityQ, secureEncoder, userSecret, config.getEncoderId() + PRR2_ENCODER_ID));
        } else {
            this.mFakeValue = null;
        }
        RapporConfig irrConfig = config.getIRRConfig();
        if (secureEncoder) {
            createInsecureEncoderForTest = RapporEncoder.createEncoder(irrConfig, userSecret);
        } else {
            createInsecureEncoderForTest = RapporEncoder.createInsecureEncoderForTest(irrConfig);
        }
        this.mIRREncoder = createInsecureEncoderForTest;
    }

    private protected synchronized byte[] encodeString(String original) {
        throw new UnsupportedOperationException();
    }

    private protected synchronized byte[] encodeBoolean(boolean original) {
        if (this.mFakeValue != null) {
            original = this.mFakeValue.booleanValue();
        }
        byte[] result = this.mIRREncoder.encodeBoolean(original);
        return result;
    }

    private protected synchronized byte[] encodeBits(byte[] bits) {
        throw new UnsupportedOperationException();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized LongitudinalReportingConfig getConfig() {
        return this.mConfig;
    }

    private protected synchronized boolean isInsecureEncoderForTest() {
        return !this.mIsSecure;
    }

    @VisibleForTesting
    private protected static synchronized boolean getLongTermRandomizedResult(double p, boolean secureEncoder, byte[] userSecret, String encoderId) {
        double effectiveF = p < 0.5d ? p * 2.0d : (1.0d - p) * 2.0d;
        boolean prrInput = p >= 0.5d;
        RapporConfig prrConfig = new RapporConfig(encoderId, 1, effectiveF, FeatureOption.FO_BOOT_POLICY_CPU, 1.0d, 1, 1);
        RapporEncoder encoder = secureEncoder ? RapporEncoder.createEncoder(prrConfig, userSecret) : RapporEncoder.createInsecureEncoderForTest(prrConfig);
        return encoder.encodeBoolean(prrInput)[0] > 0;
    }
}
