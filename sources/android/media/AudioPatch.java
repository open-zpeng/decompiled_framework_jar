package android.media;

import android.annotation.UnsupportedAppUsage;

/* loaded from: classes2.dex */
public class AudioPatch {
    @UnsupportedAppUsage
    private final AudioHandle mHandle;
    private final AudioPortConfig[] mSinks;
    private final AudioPortConfig[] mSources;

    @UnsupportedAppUsage
    AudioPatch(AudioHandle patchHandle, AudioPortConfig[] sources, AudioPortConfig[] sinks) {
        this.mHandle = patchHandle;
        this.mSources = sources;
        this.mSinks = sinks;
    }

    @UnsupportedAppUsage
    public AudioPortConfig[] sources() {
        return this.mSources;
    }

    @UnsupportedAppUsage
    public AudioPortConfig[] sinks() {
        return this.mSinks;
    }

    public int id() {
        return this.mHandle.id();
    }

    public String toString() {
        AudioPortConfig[] audioPortConfigArr;
        AudioPortConfig[] audioPortConfigArr2;
        StringBuilder s = new StringBuilder();
        s.append("mHandle: ");
        s.append(this.mHandle.toString());
        s.append(" mSources: {");
        for (AudioPortConfig source : this.mSources) {
            s.append(source.toString());
            s.append(", ");
        }
        s.append("} mSinks: {");
        for (AudioPortConfig sink : this.mSinks) {
            s.append(sink.toString());
            s.append(", ");
        }
        s.append("}");
        return s.toString();
    }
}
