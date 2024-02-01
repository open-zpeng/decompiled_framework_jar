package android.media;

import java.util.Arrays;
import java.util.UUID;

/* loaded from: classes2.dex */
public abstract class DrmInitData {
    public abstract SchemeInitData get(UUID uuid);

    /* loaded from: classes2.dex */
    public static final class SchemeInitData {
        public final byte[] data;
        public final String mimeType;

        public SchemeInitData(String mimeType, byte[] data) {
            this.mimeType = mimeType;
            this.data = data;
        }

        public boolean equals(Object obj) {
            if (obj instanceof SchemeInitData) {
                if (obj == this) {
                    return true;
                }
                SchemeInitData other = (SchemeInitData) obj;
                return this.mimeType.equals(other.mimeType) && Arrays.equals(this.data, other.data);
            }
            return false;
        }

        public int hashCode() {
            return this.mimeType.hashCode() + (Arrays.hashCode(this.data) * 31);
        }
    }
}
