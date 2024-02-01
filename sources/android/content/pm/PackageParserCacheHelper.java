package android.content.pm;

import android.os.Parcel;
import java.util.ArrayList;
import java.util.HashMap;
/* loaded from: classes.dex */
public class PackageParserCacheHelper {
    private static final boolean DEBUG = false;
    private static final String TAG = "PackageParserCacheHelper";

    private synchronized PackageParserCacheHelper() {
    }

    /* loaded from: classes.dex */
    public static class ReadHelper extends Parcel.ReadWriteHelper {
        private final Parcel mParcel;
        private final ArrayList<String> mStrings = new ArrayList<>();

        public synchronized ReadHelper(Parcel p) {
            this.mParcel = p;
        }

        public synchronized void startAndInstall() {
            this.mStrings.clear();
            int poolPosition = this.mParcel.readInt();
            int startPosition = this.mParcel.dataPosition();
            this.mParcel.setDataPosition(poolPosition);
            this.mParcel.readStringList(this.mStrings);
            this.mParcel.setDataPosition(startPosition);
            this.mParcel.setReadWriteHelper(this);
        }

        @Override // android.os.Parcel.ReadWriteHelper
        public synchronized String readString(Parcel p) {
            return this.mStrings.get(p.readInt());
        }
    }

    /* loaded from: classes.dex */
    public static class WriteHelper extends Parcel.ReadWriteHelper {
        private final Parcel mParcel;
        private final int mStartPos;
        private final ArrayList<String> mStrings = new ArrayList<>();
        private final HashMap<String, Integer> mIndexes = new HashMap<>();

        public synchronized WriteHelper(Parcel p) {
            this.mParcel = p;
            this.mStartPos = p.dataPosition();
            this.mParcel.writeInt(0);
            this.mParcel.setReadWriteHelper(this);
        }

        @Override // android.os.Parcel.ReadWriteHelper
        public synchronized void writeString(Parcel p, String s) {
            Integer cur = this.mIndexes.get(s);
            if (cur != null) {
                p.writeInt(cur.intValue());
                return;
            }
            int index = this.mStrings.size();
            this.mIndexes.put(s, Integer.valueOf(index));
            this.mStrings.add(s);
            p.writeInt(index);
        }

        public synchronized void finishAndUninstall() {
            this.mParcel.setReadWriteHelper(null);
            int poolPosition = this.mParcel.dataPosition();
            this.mParcel.writeStringList(this.mStrings);
            this.mParcel.setDataPosition(this.mStartPos);
            this.mParcel.writeInt(poolPosition);
            this.mParcel.setDataPosition(this.mParcel.dataSize());
        }
    }
}
