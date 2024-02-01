package android.privacy.internal.rappor;

import android.privacy.DifferentialPrivacyEncoder;
import com.android.internal.midi.MidiConstants;
import com.google.android.rappor.Encoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
/* loaded from: classes2.dex */
public class RapporEncoder implements DifferentialPrivacyEncoder {
    public protected static final byte[] INSECURE_SECRET = {-41, 104, -103, -109, -108, 19, 83, 84, -2, MidiConstants.STATUS_CHANNEL_PRESSURE, 126, 84, -2, MidiConstants.STATUS_CHANNEL_PRESSURE, 126, 84, -41, 104, -103, -109, -108, 19, 83, 84, -2, MidiConstants.STATUS_CHANNEL_PRESSURE, 126, 84, -2, MidiConstants.STATUS_CHANNEL_PRESSURE, 126, 84, -41, 104, -103, -109, -108, 19, 83, 84, -2, MidiConstants.STATUS_CHANNEL_PRESSURE, 126, 84, -2, MidiConstants.STATUS_CHANNEL_PRESSURE, 126, 84};
    public protected static final SecureRandom sSecureRandom = new SecureRandom();
    public protected final RapporConfig mConfig;
    public protected final Encoder mEncoder;
    public protected final boolean mIsSecure;

    public protected synchronized RapporEncoder(RapporConfig config, boolean secureEncoder, byte[] userSecret) {
        Random random;
        byte[] userSecret2;
        this.mConfig = config;
        this.mIsSecure = secureEncoder;
        if (secureEncoder) {
            random = sSecureRandom;
            userSecret2 = userSecret;
        } else {
            random = new Random(getInsecureSeed(config.mEncoderId));
            userSecret2 = INSECURE_SECRET;
        }
        this.mEncoder = new Encoder(random, (MessageDigest) null, (MessageDigest) null, userSecret2, config.mEncoderId, config.mNumBits, config.mProbabilityF, config.mProbabilityP, config.mProbabilityQ, config.mNumCohorts, config.mNumBloomHashes);
    }

    public protected synchronized long getInsecureSeed(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return ByteBuffer.wrap(bytes).getLong();
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError("Unable generate insecure seed");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized RapporEncoder createEncoder(RapporConfig config, byte[] userSecret) {
        return new RapporEncoder(config, true, userSecret);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized RapporEncoder createInsecureEncoderForTest(RapporConfig config) {
        return new RapporEncoder(config, false, null);
    }

    private protected synchronized byte[] encodeString(String original) {
        return this.mEncoder.encodeString(original);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized byte[] encodeBoolean(boolean original) {
        return this.mEncoder.encodeBoolean(original);
    }

    private protected synchronized byte[] encodeBits(byte[] bits) {
        return this.mEncoder.encodeBits(bits);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized RapporConfig getConfig() {
        return this.mConfig;
    }

    private protected synchronized boolean isInsecureEncoderForTest() {
        return !this.mIsSecure;
    }
}
