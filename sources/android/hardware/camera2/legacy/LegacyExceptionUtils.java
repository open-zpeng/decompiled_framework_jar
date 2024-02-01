package android.hardware.camera2.legacy;

import android.os.ServiceSpecificException;
import android.system.OsConstants;
import android.util.AndroidException;

/* loaded from: classes.dex */
public class LegacyExceptionUtils {
    public static final int NO_ERROR = 0;
    private static final String TAG = "LegacyExceptionUtils";
    public static final int PERMISSION_DENIED = -OsConstants.EPERM;
    public static final int ALREADY_EXISTS = -OsConstants.EEXIST;
    public static final int BAD_VALUE = -OsConstants.EINVAL;
    public static final int DEAD_OBJECT = -OsConstants.ENOSYS;
    public static final int INVALID_OPERATION = -OsConstants.EPIPE;
    public static final int TIMED_OUT = -OsConstants.ETIMEDOUT;

    /* loaded from: classes.dex */
    public static class BufferQueueAbandonedException extends AndroidException {
        public BufferQueueAbandonedException() {
        }

        public BufferQueueAbandonedException(String name) {
            super(name);
        }

        public BufferQueueAbandonedException(String name, Throwable cause) {
            super(name, cause);
        }

        public BufferQueueAbandonedException(Exception cause) {
            super(cause);
        }
    }

    public static int throwOnError(int errorFlag) throws BufferQueueAbandonedException {
        if (errorFlag == 0) {
            return 0;
        }
        if (errorFlag == BAD_VALUE) {
            throw new BufferQueueAbandonedException();
        }
        if (errorFlag < 0) {
            throw new UnsupportedOperationException("Unknown error " + errorFlag);
        }
        return errorFlag;
    }

    public static void throwOnServiceError(int errorFlag) {
        int errorCode;
        String errorMsg;
        if (errorFlag >= 0) {
            return;
        }
        if (errorFlag == PERMISSION_DENIED) {
            errorCode = 1;
            errorMsg = "Lacking privileges to access camera service";
        } else if (errorFlag == ALREADY_EXISTS) {
            return;
        } else {
            if (errorFlag == BAD_VALUE) {
                errorCode = 3;
                errorMsg = "Bad argument passed to camera service";
            } else if (errorFlag == DEAD_OBJECT) {
                errorCode = 4;
                errorMsg = "Camera service not available";
            } else if (errorFlag == TIMED_OUT) {
                errorCode = 10;
                errorMsg = "Operation timed out in camera service";
            } else if (errorFlag == (-OsConstants.EACCES)) {
                errorCode = 6;
                errorMsg = "Camera disabled by policy";
            } else if (errorFlag == (-OsConstants.EBUSY)) {
                errorCode = 7;
                errorMsg = "Camera already in use";
            } else if (errorFlag == (-OsConstants.EUSERS)) {
                errorCode = 8;
                errorMsg = "Maximum number of cameras in use";
            } else if (errorFlag == (-OsConstants.ENODEV)) {
                errorCode = 4;
                errorMsg = "Camera device not available";
            } else if (errorFlag == (-OsConstants.EOPNOTSUPP)) {
                errorCode = 9;
                errorMsg = "Deprecated camera HAL does not support this";
            } else if (errorFlag == INVALID_OPERATION) {
                errorCode = 10;
                errorMsg = "Illegal state encountered in camera service.";
            } else {
                errorCode = 10;
                errorMsg = "Unknown camera device error " + errorFlag;
            }
        }
        throw new ServiceSpecificException(errorCode, errorMsg);
    }

    private LegacyExceptionUtils() {
        throw new AssertionError();
    }
}
