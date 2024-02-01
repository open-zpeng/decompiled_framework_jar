package android.media;

import android.annotation.UnsupportedAppUsage;

/* loaded from: classes2.dex */
class AudioHandle {
    @UnsupportedAppUsage
    private final int mId;

    @UnsupportedAppUsage
    AudioHandle(int id) {
        this.mId = id;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int id() {
        return this.mId;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof AudioHandle)) {
            return false;
        }
        AudioHandle ah = (AudioHandle) o;
        return this.mId == ah.id();
    }

    public int hashCode() {
        return this.mId;
    }

    public String toString() {
        return Integer.toString(this.mId);
    }
}
