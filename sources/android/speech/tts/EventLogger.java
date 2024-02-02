package android.speech.tts;

import android.text.TextUtils;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class EventLogger extends AbstractEventLogger {
    private final SynthesisRequest mRequest;

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized EventLogger(SynthesisRequest request, int callerUid, int callerPid, String serviceApp) {
        super(callerUid, callerPid, serviceApp);
        this.mRequest = request;
    }

    @Override // android.speech.tts.AbstractEventLogger
    protected synchronized void logFailure(int statusCode) {
    }

    @Override // android.speech.tts.AbstractEventLogger
    protected synchronized void logSuccess(long audioLatency, long engineLatency, long engineTotal) {
    }

    private synchronized int getUtteranceLength() {
        String utterance = this.mRequest.getText();
        if (utterance == null) {
            return 0;
        }
        return utterance.length();
    }

    private synchronized String getLocaleString() {
        StringBuilder sb = new StringBuilder(this.mRequest.getLanguage());
        if (!TextUtils.isEmpty(this.mRequest.getCountry())) {
            sb.append('-');
            sb.append(this.mRequest.getCountry());
            if (!TextUtils.isEmpty(this.mRequest.getVariant())) {
                sb.append('-');
                sb.append(this.mRequest.getVariant());
            }
        }
        return sb.toString();
    }
}
