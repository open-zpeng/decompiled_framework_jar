package android.filterpacks.videosink;
/* loaded from: classes.dex */
public class MediaRecorderStopException extends RuntimeException {
    public protected static final String TAG = "MediaRecorderStopException";

    private protected synchronized MediaRecorderStopException(String msg) {
        super(msg);
    }

    private protected synchronized MediaRecorderStopException() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized MediaRecorderStopException(String msg, Throwable t) {
        super(msg, t);
    }

    private protected synchronized MediaRecorderStopException(Throwable t) {
        super(t);
    }
}
