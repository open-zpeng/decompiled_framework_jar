package android.hardware.soundtrigger;

import android.util.ArraySet;
import java.util.Locale;
/* loaded from: classes.dex */
public class KeyphraseMetadata {
    public final int id;
    public final String keyphrase;
    public final int recognitionModeFlags;
    public final ArraySet<Locale> supportedLocales;

    public synchronized KeyphraseMetadata(int id, String keyphrase, ArraySet<Locale> supportedLocales, int recognitionModeFlags) {
        this.id = id;
        this.keyphrase = keyphrase;
        this.supportedLocales = supportedLocales;
        this.recognitionModeFlags = recognitionModeFlags;
    }

    public String toString() {
        return "id=" + this.id + ", keyphrase=" + this.keyphrase + ", supported-locales=" + this.supportedLocales + ", recognition-modes=" + this.recognitionModeFlags;
    }

    public synchronized boolean supportsPhrase(String phrase) {
        return this.keyphrase.isEmpty() || this.keyphrase.equalsIgnoreCase(phrase);
    }

    public synchronized boolean supportsLocale(Locale locale) {
        return this.supportedLocales.isEmpty() || this.supportedLocales.contains(locale);
    }
}
