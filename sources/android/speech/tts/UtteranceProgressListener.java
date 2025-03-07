package android.speech.tts;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;

/* loaded from: classes2.dex */
public abstract class UtteranceProgressListener {
    public abstract void onDone(String str);

    @Deprecated
    public abstract void onError(String str);

    public abstract void onStart(String str);

    public void onStart(Bundle params) {
        onStart(params.getString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID));
    }

    public void onDone(Bundle params) {
        onDone(params.getString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID));
    }

    public void onError(String utteranceId, int errorCode) {
        onError(utteranceId);
    }

    public void onError(Bundle params) {
        onError(params.getString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID), params.getInt("errorCode"));
    }

    public void onStop(String utteranceId, boolean interrupted) {
    }

    public void onStop(Bundle params) {
        onStop(params.getString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID), params.getBoolean("interrupted"));
    }

    public void onBeginSynthesis(String utteranceId, int sampleRateInHz, int audioFormat, int channelCount) {
    }

    public void onAudioAvailable(String utteranceId, byte[] audio) {
    }

    public void onRangeStart(String utteranceId, int start, int end, int frame) {
        onUtteranceRangeStart(utteranceId, start, end);
    }

    @Deprecated
    public void onUtteranceRangeStart(String utteranceId, int start, int end) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static UtteranceProgressListener from(final TextToSpeech.OnUtteranceCompletedListener listener) {
        return new UtteranceProgressListener() { // from class: android.speech.tts.UtteranceProgressListener.1
            @Override // android.speech.tts.UtteranceProgressListener
            public synchronized void onDone(String utteranceId) {
                TextToSpeech.OnUtteranceCompletedListener.this.onUtteranceCompleted(utteranceId);
            }

            @Override // android.speech.tts.UtteranceProgressListener
            public void onError(String utteranceId) {
                TextToSpeech.OnUtteranceCompletedListener.this.onUtteranceCompleted(utteranceId);
            }

            @Override // android.speech.tts.UtteranceProgressListener
            public void onStart(String utteranceId) {
            }

            @Override // android.speech.tts.UtteranceProgressListener
            public void onStop(String utteranceId, boolean interrupted) {
                TextToSpeech.OnUtteranceCompletedListener.this.onUtteranceCompleted(utteranceId);
            }
        };
    }
}
