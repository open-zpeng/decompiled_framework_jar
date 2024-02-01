package android.security.keystore;
/* loaded from: classes2.dex */
public interface UserAuthArgs {
    synchronized long getBoundToSpecificSecureUserId();

    synchronized int getUserAuthenticationValidityDurationSeconds();

    synchronized boolean isInvalidatedByBiometricEnrollment();

    synchronized boolean isUnlockedDeviceRequired();

    synchronized boolean isUserAuthenticationRequired();

    synchronized boolean isUserAuthenticationValidWhileOnBody();

    synchronized boolean isUserConfirmationRequired();

    synchronized boolean isUserPresenceRequired();
}
