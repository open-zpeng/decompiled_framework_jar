package android.security.keystore;

import android.content.pm.PackageManager;
import android.hardware.face.FaceManager;
import android.hardware.fingerprint.FingerprintManager;
import android.security.GateKeeper;
import android.security.KeyStore;
import android.security.keymaster.KeymasterArguments;
import android.security.keymaster.KeymasterDefs;
import android.security.keystore.KeyProperties;
import com.android.internal.logging.nano.MetricsProto;
import java.security.ProviderException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class KeymasterUtils {
    private KeymasterUtils() {
    }

    public static int getDigestOutputSizeBits(int keymasterDigest) {
        switch (keymasterDigest) {
            case 0:
                return -1;
            case 1:
                return 128;
            case 2:
                return 160;
            case 3:
                return 224;
            case 4:
                return 256;
            case 5:
                return MetricsProto.MetricsEvent.ACTION_SHOW_SETTINGS_SUGGESTION;
            case 6:
                return 512;
            default:
                throw new IllegalArgumentException("Unknown digest: " + keymasterDigest);
        }
    }

    public static boolean isKeymasterBlockModeIndCpaCompatibleWithSymmetricCrypto(int keymasterBlockMode) {
        if (keymasterBlockMode != 1) {
            if (keymasterBlockMode == 2 || keymasterBlockMode == 3 || keymasterBlockMode == 32) {
                return true;
            }
            throw new IllegalArgumentException("Unsupported block mode: " + keymasterBlockMode);
        }
        return false;
    }

    public static boolean isKeymasterPaddingSchemeIndCpaCompatibleWithAsymmetricCrypto(int keymasterPadding) {
        if (keymasterPadding != 1) {
            if (keymasterPadding == 2 || keymasterPadding == 4) {
                return true;
            }
            throw new IllegalArgumentException("Unsupported asymmetric encryption padding scheme: " + keymasterPadding);
        }
        return false;
    }

    public static void addUserAuthArgs(KeymasterArguments args, UserAuthArgs spec) {
        long sid;
        if (spec.isUserConfirmationRequired()) {
            args.addBoolean(KeymasterDefs.KM_TAG_TRUSTED_CONFIRMATION_REQUIRED);
        }
        if (spec.isUserPresenceRequired()) {
            args.addBoolean(KeymasterDefs.KM_TAG_TRUSTED_USER_PRESENCE_REQUIRED);
        }
        if (spec.isUnlockedDeviceRequired()) {
            args.addBoolean(KeymasterDefs.KM_TAG_UNLOCKED_DEVICE_REQUIRED);
        }
        if (!spec.isUserAuthenticationRequired()) {
            args.addBoolean(KeymasterDefs.KM_TAG_NO_AUTH_REQUIRED);
        } else if (spec.getUserAuthenticationValidityDurationSeconds() == -1) {
            PackageManager pm = KeyStore.getApplicationContext().getPackageManager();
            FingerprintManager fingerprintManager = null;
            FaceManager faceManager = null;
            if (pm.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
                fingerprintManager = (FingerprintManager) KeyStore.getApplicationContext().getSystemService(FingerprintManager.class);
            }
            if (pm.hasSystemFeature(PackageManager.FEATURE_FACE)) {
                faceManager = (FaceManager) KeyStore.getApplicationContext().getSystemService(FaceManager.class);
            }
            long fingerprintOnlySid = fingerprintManager != null ? fingerprintManager.getAuthenticatorId() : 0L;
            long faceOnlySid = faceManager != null ? faceManager.getAuthenticatorId() : 0L;
            if (fingerprintOnlySid == 0 && faceOnlySid == 0) {
                throw new IllegalStateException("At least one biometric must be enrolled to create keys requiring user authentication for every use");
            }
            List<Long> sids = new ArrayList<>();
            if (spec.getBoundToSpecificSecureUserId() != 0) {
                sids.add(Long.valueOf(spec.getBoundToSpecificSecureUserId()));
            } else if (spec.isInvalidatedByBiometricEnrollment()) {
                sids.add(Long.valueOf(fingerprintOnlySid));
                sids.add(Long.valueOf(faceOnlySid));
            } else {
                sids.add(Long.valueOf(getRootSid()));
            }
            for (int i = 0; i < sids.size(); i++) {
                args.addUnsignedLong(KeymasterDefs.KM_TAG_USER_SECURE_ID, KeymasterArguments.toUint64(sids.get(i).longValue()));
            }
            args.addEnum(KeymasterDefs.KM_TAG_USER_AUTH_TYPE, 2);
            if (spec.isUserAuthenticationValidWhileOnBody()) {
                throw new ProviderException("Key validity extension while device is on-body is not supported for keys requiring fingerprint authentication");
            }
        } else {
            if (spec.getBoundToSpecificSecureUserId() != 0) {
                sid = spec.getBoundToSpecificSecureUserId();
            } else {
                sid = getRootSid();
            }
            args.addUnsignedLong(KeymasterDefs.KM_TAG_USER_SECURE_ID, KeymasterArguments.toUint64(sid));
            args.addEnum(KeymasterDefs.KM_TAG_USER_AUTH_TYPE, 3);
            args.addUnsignedInt(KeymasterDefs.KM_TAG_AUTH_TIMEOUT, spec.getUserAuthenticationValidityDurationSeconds());
            if (spec.isUserAuthenticationValidWhileOnBody()) {
                args.addBoolean(KeymasterDefs.KM_TAG_ALLOW_WHILE_ON_BODY);
            }
        }
    }

    public static void addMinMacLengthAuthorizationIfNecessary(KeymasterArguments args, int keymasterAlgorithm, int[] keymasterBlockModes, int[] keymasterDigests) {
        if (keymasterAlgorithm == 32) {
            if (com.android.internal.util.ArrayUtils.contains(keymasterBlockModes, 32)) {
                args.addUnsignedInt(KeymasterDefs.KM_TAG_MIN_MAC_LENGTH, 96L);
            }
        } else if (keymasterAlgorithm == 128) {
            if (keymasterDigests.length != 1) {
                throw new ProviderException("Unsupported number of authorized digests for HMAC key: " + keymasterDigests.length + ". Exactly one digest must be authorized");
            }
            int keymasterDigest = keymasterDigests[0];
            int digestOutputSizeBits = getDigestOutputSizeBits(keymasterDigest);
            if (digestOutputSizeBits != -1) {
                args.addUnsignedInt(KeymasterDefs.KM_TAG_MIN_MAC_LENGTH, digestOutputSizeBits);
                return;
            }
            throw new ProviderException("HMAC key authorized for unsupported digest: " + KeyProperties.Digest.fromKeymaster(keymasterDigest));
        }
    }

    private static long getRootSid() {
        long rootSid = GateKeeper.getSecureUserId();
        if (rootSid == 0) {
            throw new IllegalStateException("Secure lock screen must be enabled to create keys requiring user authentication");
        }
        return rootSid;
    }
}
