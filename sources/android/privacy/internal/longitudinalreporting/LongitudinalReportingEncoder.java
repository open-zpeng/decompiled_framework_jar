package android.privacy.internal.longitudinalreporting;

import android.privacy.DifferentialPrivacyEncoder;
import android.privacy.internal.rappor.RapporConfig;
import android.privacy.internal.rappor.RapporEncoder;
import com.android.internal.annotations.VisibleForTesting;
import com.xiaopeng.util.FeatureOption;

/* loaded from: classes2.dex */
public class LongitudinalReportingEncoder implements DifferentialPrivacyEncoder {
    private static final boolean DEBUG = false;
    private static final String PRR1_ENCODER_ID = "prr1_encoder_id";
    private static final String PRR2_ENCODER_ID = "prr2_encoder_id";
    private static final String TAG = "LongitudinalEncoder";
    private final LongitudinalReportingConfig mConfig;
    private final Boolean mFakeValue;
    private final RapporEncoder mIRREncoder;
    private final boolean mIsSecure;

    public static LongitudinalReportingEncoder createEncoder(LongitudinalReportingConfig config, byte[] userSecret) {
        return new LongitudinalReportingEncoder(config, true, userSecret);
    }

    @VisibleForTesting
    public static LongitudinalReportingEncoder createInsecureEncoderForTest(LongitudinalReportingConfig config) {
        return new LongitudinalReportingEncoder(config, false, null);
    }

    private LongitudinalReportingEncoder(LongitudinalReportingConfig config, boolean secureEncoder, byte[] userSecret) {
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

    @Override // android.privacy.DifferentialPrivacyEncoder
    public byte[] encodeString(String original) {
        throw new UnsupportedOperationException();
    }

    @Override // android.privacy.DifferentialPrivacyEncoder
    public byte[] encodeBoolean(boolean original) {
        Boolean bool = this.mFakeValue;
        if (bool != null) {
            original = bool.booleanValue();
        }
        byte[] result = this.mIRREncoder.encodeBoolean(original);
        return result;
    }

    @Override // android.privacy.DifferentialPrivacyEncoder
    public byte[] encodeBits(byte[] bits) {
        throw new UnsupportedOperationException();
    }

    @Override // android.privacy.DifferentialPrivacyEncoder
    public LongitudinalReportingConfig getConfig() {
        return this.mConfig;
    }

    @Override // android.privacy.DifferentialPrivacyEncoder
    public boolean isInsecureEncoderForTest() {
        return !this.mIsSecure;
    }

    @VisibleForTesting
    public static boolean getLongTermRandomizedResult(double p, boolean secureEncoder, byte[] userSecret, String encoderId) {
        double effectiveF = p < 0.5d ? p * 2.0d : (1.0d - p) * 2.0d;
        boolean prrInput = p >= 0.5d;
        RapporConfig prrConfig = new RapporConfig(encoderId, 1, effectiveF, FeatureOption.FO_BOOT_POLICY_CPU, 1.0d, 1, 1);
        RapporEncoder encoder = secureEncoder ? RapporEncoder.createEncoder(prrConfig, userSecret) : RapporEncoder.createInsecureEncoderForTest(prrConfig);
        return encoder.encodeBoolean(prrInput)[0] > 0;
    }
}
