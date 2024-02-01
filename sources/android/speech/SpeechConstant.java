package android.speech;

/* loaded from: classes2.dex */
public final class SpeechConstant {

    /* loaded from: classes2.dex */
    public interface Recognition {
        public static final String ENABLE_PUNCTUATION = "enablePunctuation";
        public static final String ENGINE = "engine";
        public static final String ERROR_ID = "errorId";
        public static final String ERROR_INFO = "errorInfo";
        public static final String EVENT_TYPE = "eventType";
        public static final String INIT_ID = "initId";
        public static final String INIT_STATE = "initState";
        public static final String NET_BIN = "netBin";
        public static final String PINYIN = "pinyin";
        public static final String RAW_SEM = "raw_sem";
        public static final String SAVE_AUDIO_PATH = "saveAudioPath";
        public static final String WAKEUP_WORDS = "wakeupWords";

        /* loaded from: classes2.dex */
        public interface Engine {
            public static final String CLOUD = "cloud";
            public static final String LOCAL = "local";
            public static final int MODE_BEGIN = 0;
            public static final int MODE_CLOUD = 2;
            public static final int MODE_END = 3;
            public static final int MODE_LOCAL = 1;
            public static final int MODE_MIX = 0;
            public static final int MODE_VAD = 3;
        }

        /* loaded from: classes2.dex */
        public interface EventType {
            public static final int EVENT_TYPE_ERROR = 2000;
            public static final int EVENT_TYPE_INIT = 1000;
        }

        /* loaded from: classes2.dex */
        public interface InitId {
            public static final int ASR_INIT = 3002;
            public static final int GRAMMAR_INIT = 3001;
        }

        /* loaded from: classes2.dex */
        public interface SpeechDataSource {
            public static final int SOURCE_APP = 1;
            public static final int SOURCE_SERVICE = 0;
        }

        /* loaded from: classes2.dex */
        public interface StateResult {
            public static final int ERROR = 0;
            public static final int OTHER = -1;
            public static final int SUCCESS = 1;
        }

        /* loaded from: classes2.dex */
        public interface VocabType {
            public static final int VOCAB_CONTACT = 1;
        }
    }
}
