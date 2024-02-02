package android.media;

import java.util.Arrays;
import java.util.UUID;
/* loaded from: classes.dex */
public abstract class DrmInitData {
    public abstract SchemeInitData get(UUID uuid);

    /* loaded from: classes.dex */
    public static final class SchemeInitData {
        public final byte[] data;
        public final String mimeType;

        public synchronized SchemeInitData(String mimeType, byte[] data) {
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
            return this.mimeType.hashCode() + (31 * Arrays.hashCode(this.data));
        }
    }
}
