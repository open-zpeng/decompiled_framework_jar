package android.webkit;

import android.annotation.UnsupportedAppUsage;

/* loaded from: classes3.dex */
public class ConsoleMessage {
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private MessageLevel mLevel;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private int mLineNumber;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private String mMessage;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private String mSourceId;

    /* loaded from: classes3.dex */
    public enum MessageLevel {
        TIP,
        LOG,
        WARNING,
        ERROR,
        DEBUG
    }

    public ConsoleMessage(String message, String sourceId, int lineNumber, MessageLevel msgLevel) {
        this.mMessage = message;
        this.mSourceId = sourceId;
        this.mLineNumber = lineNumber;
        this.mLevel = msgLevel;
    }

    public MessageLevel messageLevel() {
        return this.mLevel;
    }

    public String message() {
        return this.mMessage;
    }

    public String sourceId() {
        return this.mSourceId;
    }

    public int lineNumber() {
        return this.mLineNumber;
    }
}
