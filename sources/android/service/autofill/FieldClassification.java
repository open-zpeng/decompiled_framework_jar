package android.service.autofill;

import android.os.Parcel;
import android.view.autofill.Helper;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/* loaded from: classes2.dex */
public final class FieldClassification {
    private final ArrayList<Match> mMatches;

    public synchronized FieldClassification(ArrayList<Match> matches) {
        this.mMatches = (ArrayList) Preconditions.checkNotNull(matches);
        Collections.sort(this.mMatches, new Comparator<Match>() { // from class: android.service.autofill.FieldClassification.1
            @Override // java.util.Comparator
            public int compare(Match o1, Match o2) {
                if (o1.mScore > o2.mScore) {
                    return -1;
                }
                return o1.mScore < o2.mScore ? 1 : 0;
            }
        });
    }

    public List<Match> getMatches() {
        return this.mMatches;
    }

    public String toString() {
        if (Helper.sDebug) {
            return "FieldClassification: " + this.mMatches;
        }
        return super.toString();
    }

    private synchronized void writeToParcel(Parcel parcel) {
        parcel.writeInt(this.mMatches.size());
        for (int i = 0; i < this.mMatches.size(); i++) {
            this.mMatches.get(i).writeToParcel(parcel);
        }
    }

    private static synchronized FieldClassification readFromParcel(Parcel parcel) {
        int size = parcel.readInt();
        ArrayList<Match> matches = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            matches.add(i, Match.readFromParcel(parcel));
        }
        return new FieldClassification(matches);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized FieldClassification[] readArrayFromParcel(Parcel parcel) {
        int length = parcel.readInt();
        FieldClassification[] fcs = new FieldClassification[length];
        for (int i = 0; i < length; i++) {
            fcs[i] = readFromParcel(parcel);
        }
        return fcs;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized void writeArrayToParcel(Parcel parcel, FieldClassification[] fcs) {
        parcel.writeInt(fcs.length);
        for (FieldClassification fieldClassification : fcs) {
            fieldClassification.writeToParcel(parcel);
        }
    }

    /* loaded from: classes2.dex */
    public static final class Match {
        private final String mCategoryId;
        private final float mScore;

        public synchronized Match(String categoryId, float score) {
            this.mCategoryId = (String) Preconditions.checkNotNull(categoryId);
            this.mScore = score;
        }

        public String getCategoryId() {
            return this.mCategoryId;
        }

        public float getScore() {
            return this.mScore;
        }

        public String toString() {
            if (Helper.sDebug) {
                StringBuilder string = new StringBuilder("Match: categoryId=");
                Helper.appendRedacted(string, this.mCategoryId);
                string.append(", score=");
                string.append(this.mScore);
                return string.toString();
            }
            return super.toString();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void writeToParcel(Parcel parcel) {
            parcel.writeString(this.mCategoryId);
            parcel.writeFloat(this.mScore);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static synchronized Match readFromParcel(Parcel parcel) {
            return new Match(parcel.readString(), parcel.readFloat());
        }
    }
}
