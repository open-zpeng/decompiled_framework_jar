package android.media;
/* loaded from: classes.dex */
public class MediaCasException extends Exception {
    private synchronized MediaCasException(String detailMessage) {
        super(detailMessage);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized void throwExceptionIfNeeded(int error) throws MediaCasException {
        if (error == 0) {
            return;
        }
        if (error == 7) {
            throw new NotProvisionedException(null);
        }
        if (error == 8) {
            throw new ResourceBusyException(null);
        }
        if (error == 11) {
            throw new DeniedByServerException(null);
        }
        MediaCasStateException.throwExceptionIfNeeded(error);
    }

    /* loaded from: classes.dex */
    public static final class UnsupportedCasException extends MediaCasException {
        public synchronized UnsupportedCasException(String detailMessage) {
            super(detailMessage);
        }
    }

    /* loaded from: classes.dex */
    public static final class NotProvisionedException extends MediaCasException {
        public synchronized NotProvisionedException(String detailMessage) {
            super(detailMessage);
        }
    }

    /* loaded from: classes.dex */
    public static final class DeniedByServerException extends MediaCasException {
        public synchronized DeniedByServerException(String detailMessage) {
            super(detailMessage);
        }
    }

    /* loaded from: classes.dex */
    public static final class ResourceBusyException extends MediaCasException {
        public synchronized ResourceBusyException(String detailMessage) {
            super(detailMessage);
        }
    }
}
