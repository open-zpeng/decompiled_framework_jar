package android.os;

import android.annotation.SystemApi;
import android.content.Context;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.proto.ProtoOutputStream;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
/* loaded from: classes2.dex */
public class WorkSource implements Parcelable {
    static final boolean DEBUG = false;
    static final String TAG = "WorkSource";
    public private protected static WorkSource sGoneWork;
    public private protected static WorkSource sNewbWork;
    private ArrayList<WorkChain> mChains;
    public private protected String[] mNames;
    public private protected int mNum;
    public private protected int[] mUids;
    public private protected static final WorkSource sTmpWorkSource = new WorkSource(0);
    public static final Parcelable.Creator<WorkSource> CREATOR = new Parcelable.Creator<WorkSource>() { // from class: android.os.WorkSource.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkSource createFromParcel(Parcel in) {
            return new WorkSource(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WorkSource[] newArray(int size) {
            return new WorkSource[size];
        }
    };

    public WorkSource() {
        this.mNum = 0;
        this.mChains = null;
    }

    public WorkSource(WorkSource orig) {
        if (orig == null) {
            this.mNum = 0;
            this.mChains = null;
            return;
        }
        this.mNum = orig.mNum;
        if (orig.mUids != null) {
            this.mUids = (int[]) orig.mUids.clone();
            this.mNames = orig.mNames != null ? (String[]) orig.mNames.clone() : null;
        } else {
            this.mUids = null;
            this.mNames = null;
        }
        if (orig.mChains != null) {
            this.mChains = new ArrayList<>(orig.mChains.size());
            Iterator<WorkChain> it = orig.mChains.iterator();
            while (it.hasNext()) {
                WorkChain chain = it.next();
                this.mChains.add(new WorkChain(chain));
            }
            return;
        }
        this.mChains = null;
    }

    private protected WorkSource(int uid) {
        this.mNum = 1;
        this.mUids = new int[]{uid, 0};
        this.mNames = null;
        this.mChains = null;
    }

    public synchronized WorkSource(int uid, String name) {
        if (name == null) {
            throw new NullPointerException("Name can't be null");
        }
        this.mNum = 1;
        this.mUids = new int[]{uid, 0};
        this.mNames = new String[]{name, null};
        this.mChains = null;
    }

    public private protected WorkSource(Parcel in) {
        this.mNum = in.readInt();
        this.mUids = in.createIntArray();
        this.mNames = in.createStringArray();
        int numChains = in.readInt();
        if (numChains > 0) {
            this.mChains = new ArrayList<>(numChains);
            in.readParcelableList(this.mChains, WorkChain.class.getClassLoader());
            return;
        }
        this.mChains = null;
    }

    public static synchronized boolean isChainedBatteryAttributionEnabled(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), Settings.Global.CHAINED_BATTERY_ATTRIBUTION_ENABLED, 0) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int size() {
        return this.mNum;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int get(int index) {
        return this.mUids[index];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getName(int index) {
        if (this.mNames != null) {
            return this.mNames[index];
        }
        return null;
    }

    public synchronized void clearNames() {
        if (this.mNames != null) {
            this.mNames = null;
            int destIndex = 1;
            int newNum = this.mNum;
            for (int sourceIndex = 1; sourceIndex < this.mNum; sourceIndex++) {
                if (this.mUids[sourceIndex] == this.mUids[sourceIndex - 1]) {
                    newNum--;
                } else {
                    this.mUids[destIndex] = this.mUids[sourceIndex];
                    destIndex++;
                }
            }
            this.mNum = newNum;
        }
    }

    public void clear() {
        this.mNum = 0;
        if (this.mChains != null) {
            this.mChains.clear();
        }
    }

    public boolean equals(Object o) {
        if (o instanceof WorkSource) {
            WorkSource other = (WorkSource) o;
            if (diff(other)) {
                return false;
            }
            if (this.mChains == null || this.mChains.isEmpty()) {
                return other.mChains == null || other.mChains.isEmpty();
            }
            return this.mChains.equals(other.mChains);
        }
        return false;
    }

    public int hashCode() {
        int i = 0;
        int result = 0;
        for (int result2 = 0; result2 < this.mNum; result2++) {
            result = ((result << 4) | (result >>> 28)) ^ this.mUids[result2];
        }
        if (this.mNames != null) {
            while (true) {
                int i2 = i;
                int i3 = this.mNum;
                if (i2 >= i3) {
                    break;
                }
                result = ((result << 4) | (result >>> 28)) ^ this.mNames[i2].hashCode();
                i = i2 + 1;
            }
        }
        if (this.mChains != null) {
            return ((result << 4) | (result >>> 28)) ^ this.mChains.hashCode();
        }
        return result;
    }

    public boolean diff(WorkSource other) {
        int N = this.mNum;
        if (N != other.mNum) {
            return true;
        }
        int[] uids1 = this.mUids;
        int[] uids2 = other.mUids;
        String[] names1 = this.mNames;
        String[] names2 = other.mNames;
        for (int i = 0; i < N; i++) {
            if (uids1[i] != uids2[i]) {
                return true;
            }
            if (names1 != null && names2 != null && !names1[i].equals(names2[i])) {
                return true;
            }
        }
        return false;
    }

    public void set(WorkSource other) {
        if (other == null) {
            this.mNum = 0;
            if (this.mChains != null) {
                this.mChains.clear();
                return;
            }
            return;
        }
        this.mNum = other.mNum;
        if (other.mUids != null) {
            if (this.mUids != null && this.mUids.length >= this.mNum) {
                System.arraycopy(other.mUids, 0, this.mUids, 0, this.mNum);
            } else {
                this.mUids = (int[]) other.mUids.clone();
            }
            if (other.mNames != null) {
                if (this.mNames != null && this.mNames.length >= this.mNum) {
                    System.arraycopy(other.mNames, 0, this.mNames, 0, this.mNum);
                } else {
                    this.mNames = (String[]) other.mNames.clone();
                }
            } else {
                this.mNames = null;
            }
        } else {
            this.mUids = null;
            this.mNames = null;
        }
        if (other.mChains != null) {
            if (this.mChains != null) {
                this.mChains.clear();
            } else {
                this.mChains = new ArrayList<>(other.mChains.size());
            }
            Iterator<WorkChain> it = other.mChains.iterator();
            while (it.hasNext()) {
                WorkChain chain = it.next();
                this.mChains.add(new WorkChain(chain));
            }
        }
    }

    public synchronized void set(int uid) {
        this.mNum = 1;
        if (this.mUids == null) {
            this.mUids = new int[2];
        }
        this.mUids[0] = uid;
        this.mNames = null;
        if (this.mChains != null) {
            this.mChains.clear();
        }
    }

    public synchronized void set(int uid, String name) {
        if (name == null) {
            throw new NullPointerException("Name can't be null");
        }
        this.mNum = 1;
        if (this.mUids == null) {
            this.mUids = new int[2];
            this.mNames = new String[2];
        }
        this.mUids[0] = uid;
        this.mNames[0] = name;
        if (this.mChains != null) {
            this.mChains.clear();
        }
    }

    @Deprecated
    private protected WorkSource[] setReturningDiffs(WorkSource other) {
        synchronized (sTmpWorkSource) {
            sNewbWork = null;
            sGoneWork = null;
            updateLocked(other, true, true);
            if (sNewbWork == null && sGoneWork == null) {
                return null;
            }
            WorkSource[] diffs = {sNewbWork, sGoneWork};
            return diffs;
        }
    }

    public boolean add(WorkSource other) {
        boolean z;
        synchronized (sTmpWorkSource) {
            z = false;
            boolean uidAdded = updateLocked(other, false, false);
            if (other.mChains != null) {
                if (this.mChains == null) {
                    this.mChains = new ArrayList<>(other.mChains.size());
                }
                Iterator<WorkChain> it = other.mChains.iterator();
                while (it.hasNext()) {
                    WorkChain wc = it.next();
                    if (!this.mChains.contains(wc)) {
                        this.mChains.add(new WorkChain(wc));
                    }
                }
            }
            if (!uidAdded && 0 == 0) {
            }
            z = true;
        }
        return z;
    }

    @Deprecated
    private protected WorkSource addReturningNewbs(WorkSource other) {
        WorkSource workSource;
        synchronized (sTmpWorkSource) {
            sNewbWork = null;
            updateLocked(other, false, true);
            workSource = sNewbWork;
        }
        return workSource;
    }

    private protected boolean add(int uid) {
        if (this.mNum <= 0) {
            this.mNames = null;
            insert(0, uid);
            return true;
        } else if (this.mNames != null) {
            throw new IllegalArgumentException("Adding without name to named " + this);
        } else {
            int i = Arrays.binarySearch(this.mUids, 0, this.mNum, uid);
            if (i >= 0) {
                return false;
            }
            insert((-i) - 1, uid);
            return true;
        }
    }

    private protected boolean add(int uid, String name) {
        if (this.mNum <= 0) {
            insert(0, uid, name);
            return true;
        } else if (this.mNames == null) {
            throw new IllegalArgumentException("Adding name to unnamed " + this);
        } else {
            int i = 0;
            while (i < this.mNum && this.mUids[i] <= uid) {
                if (this.mUids[i] == uid) {
                    int diff = this.mNames[i].compareTo(name);
                    if (diff > 0) {
                        break;
                    } else if (diff == 0) {
                        return false;
                    }
                }
                i++;
            }
            insert(i, uid, name);
            return true;
        }
    }

    public boolean remove(WorkSource other) {
        boolean uidRemoved;
        if (isEmpty() || other.isEmpty()) {
            return false;
        }
        if (this.mNames == null && other.mNames == null) {
            uidRemoved = removeUids(other);
        } else if (this.mNames == null) {
            throw new IllegalArgumentException("Other " + other + " has names, but target " + this + " does not");
        } else if (other.mNames == null) {
            throw new IllegalArgumentException("Target " + this + " has names, but other " + other + " does not");
        } else {
            uidRemoved = removeUidsAndNames(other);
        }
        boolean chainRemoved = false;
        if (other.mChains != null && this.mChains != null) {
            chainRemoved = this.mChains.removeAll(other.mChains);
        }
        return uidRemoved || chainRemoved;
    }

    @SystemApi
    public WorkChain createWorkChain() {
        if (this.mChains == null) {
            this.mChains = new ArrayList<>(4);
        }
        WorkChain wc = new WorkChain();
        this.mChains.add(wc);
        return wc;
    }

    public synchronized boolean isEmpty() {
        return this.mNum == 0 && (this.mChains == null || this.mChains.isEmpty());
    }

    public synchronized ArrayList<WorkChain> getWorkChains() {
        return this.mChains;
    }

    public synchronized void transferWorkChains(WorkSource other) {
        if (this.mChains != null) {
            this.mChains.clear();
        }
        if (other.mChains == null || other.mChains.isEmpty()) {
            return;
        }
        if (this.mChains == null) {
            this.mChains = new ArrayList<>(4);
        }
        this.mChains.addAll(other.mChains);
        other.mChains.clear();
    }

    private synchronized boolean removeUids(WorkSource other) {
        int N1 = this.mNum;
        int[] uids1 = this.mUids;
        int N2 = other.mNum;
        int[] uids2 = other.mUids;
        boolean changed = false;
        int i1 = 0;
        int i2 = 0;
        while (i1 < N1 && i2 < N2) {
            if (uids2[i2] == uids1[i1]) {
                N1--;
                changed = true;
                if (i1 < N1) {
                    System.arraycopy(uids1, i1 + 1, uids1, i1, N1 - i1);
                }
                i2++;
            } else if (uids2[i2] > uids1[i1]) {
                i1++;
            } else {
                i2++;
            }
        }
        this.mNum = N1;
        return changed;
    }

    private synchronized boolean removeUidsAndNames(WorkSource other) {
        int N1 = this.mNum;
        int[] uids1 = this.mUids;
        String[] names1 = this.mNames;
        int N2 = other.mNum;
        int[] uids2 = other.mUids;
        String[] names2 = other.mNames;
        boolean changed = false;
        int i1 = 0;
        int i2 = 0;
        while (i1 < N1 && i2 < N2) {
            if (uids2[i2] == uids1[i1] && names2[i2].equals(names1[i1])) {
                N1--;
                changed = true;
                if (i1 < N1) {
                    System.arraycopy(uids1, i1 + 1, uids1, i1, N1 - i1);
                    System.arraycopy(names1, i1 + 1, names1, i1, N1 - i1);
                }
                i2++;
            } else if (uids2[i2] > uids1[i1] || (uids2[i2] == uids1[i1] && names2[i2].compareTo(names1[i1]) > 0)) {
                i1++;
            } else {
                i2++;
            }
        }
        this.mNum = N1;
        return changed;
    }

    public protected boolean updateLocked(WorkSource other, boolean set, boolean returnNewbs) {
        if (this.mNames == null && other.mNames == null) {
            return updateUidsLocked(other, set, returnNewbs);
        }
        if (this.mNum > 0 && this.mNames == null) {
            throw new IllegalArgumentException("Other " + other + " has names, but target " + this + " does not");
        } else if (other.mNum > 0 && other.mNames == null) {
            throw new IllegalArgumentException("Target " + this + " has names, but other " + other + " does not");
        } else {
            return updateUidsAndNamesLocked(other, set, returnNewbs);
        }
    }

    private static synchronized WorkSource addWork(WorkSource cur, int newUid) {
        if (cur == null) {
            return new WorkSource(newUid);
        }
        cur.insert(cur.mNum, newUid);
        return cur;
    }

    private synchronized boolean updateUidsLocked(WorkSource other, boolean set, boolean returnNewbs) {
        int N1 = this.mNum;
        int[] uids1 = this.mUids;
        int N2 = other.mNum;
        int[] uids2 = other.mUids;
        int i1 = 0;
        boolean changed = false;
        int[] uids12 = uids1;
        int N12 = N1;
        int i2 = 0;
        while (true) {
            if (i1 < N12 || i2 < N2) {
                if (i1 >= N12 || (i2 < N2 && uids2[i2] < uids12[i1])) {
                    changed = true;
                    if (uids12 == null) {
                        uids12 = new int[4];
                        uids12[0] = uids2[i2];
                    } else if (N12 >= uids12.length) {
                        int[] newuids = new int[(uids12.length * 3) / 2];
                        if (i1 > 0) {
                            System.arraycopy(uids12, 0, newuids, 0, i1);
                        }
                        if (i1 < N12) {
                            System.arraycopy(uids12, i1, newuids, i1 + 1, N12 - i1);
                        }
                        uids12 = newuids;
                        uids12[i1] = uids2[i2];
                    } else {
                        if (i1 < N12) {
                            System.arraycopy(uids12, i1, uids12, i1 + 1, N12 - i1);
                        }
                        uids12[i1] = uids2[i2];
                    }
                    if (returnNewbs) {
                        sNewbWork = addWork(sNewbWork, uids2[i2]);
                    }
                    N12++;
                    i1++;
                    i2++;
                } else if (!set) {
                    if (i2 < N2 && uids2[i2] == uids12[i1]) {
                        i2++;
                    }
                    i1++;
                } else {
                    int i12 = i1;
                    while (i12 < N12 && (i2 >= N2 || uids2[i2] > uids12[i12])) {
                        sGoneWork = addWork(sGoneWork, uids12[i12]);
                        i12++;
                    }
                    if (i1 < i12) {
                        System.arraycopy(uids12, i12, uids12, i1, N12 - i12);
                        N12 -= i12 - i1;
                        i12 = i1;
                    }
                    if (i12 < N12 && i2 < N2 && uids2[i2] == uids12[i12]) {
                        i12++;
                        i2++;
                    }
                    i1 = i12;
                }
            } else {
                this.mNum = N12;
                this.mUids = uids12;
                return changed;
            }
        }
    }

    private synchronized int compare(WorkSource other, int i1, int i2) {
        int diff = this.mUids[i1] - other.mUids[i2];
        if (diff != 0) {
            return diff;
        }
        return this.mNames[i1].compareTo(other.mNames[i2]);
    }

    private static synchronized WorkSource addWork(WorkSource cur, int newUid, String newName) {
        if (cur == null) {
            return new WorkSource(newUid, newName);
        }
        cur.insert(cur.mNum, newUid, newName);
        return cur;
    }

    private synchronized boolean updateUidsAndNamesLocked(WorkSource other, boolean set, boolean returnNewbs) {
        int N2 = other.mNum;
        int[] uids2 = other.mUids;
        String[] names2 = other.mNames;
        boolean changed = false;
        int i1 = 0;
        int i2 = 0;
        while (true) {
            if (i1 < this.mNum || i2 < N2) {
                int diff = -1;
                if (i1 < this.mNum) {
                    if (i2 < N2) {
                        int compare = compare(other, i1, i2);
                        diff = compare;
                        if (compare > 0) {
                        }
                    }
                    if (!set) {
                        if (i2 < N2 && diff == 0) {
                            i2++;
                        }
                        i1++;
                    } else {
                        int i12 = i1;
                        while (diff < 0) {
                            sGoneWork = addWork(sGoneWork, this.mUids[i12], this.mNames[i12]);
                            i12++;
                            if (i12 >= this.mNum) {
                                break;
                            }
                            diff = i2 < N2 ? compare(other, i12, i2) : -1;
                        }
                        if (i1 < i12) {
                            System.arraycopy(this.mUids, i12, this.mUids, i1, this.mNum - i12);
                            System.arraycopy(this.mNames, i12, this.mNames, i1, this.mNum - i12);
                            this.mNum -= i12 - i1;
                            i12 = i1;
                        }
                        if (i12 < this.mNum && diff == 0) {
                            i12++;
                            i2++;
                        }
                        i1 = i12;
                    }
                }
                changed = true;
                insert(i1, uids2[i2], names2[i2]);
                if (returnNewbs) {
                    sNewbWork = addWork(sNewbWork, uids2[i2], names2[i2]);
                }
                i1++;
                i2++;
            } else {
                return changed;
            }
        }
    }

    private synchronized void insert(int index, int uid) {
        if (this.mUids == null) {
            this.mUids = new int[4];
            this.mUids[0] = uid;
            this.mNum = 1;
        } else if (this.mNum >= this.mUids.length) {
            int[] newuids = new int[(this.mNum * 3) / 2];
            if (index > 0) {
                System.arraycopy(this.mUids, 0, newuids, 0, index);
            }
            if (index < this.mNum) {
                System.arraycopy(this.mUids, index, newuids, index + 1, this.mNum - index);
            }
            this.mUids = newuids;
            this.mUids[index] = uid;
            this.mNum++;
        } else {
            if (index < this.mNum) {
                System.arraycopy(this.mUids, index, this.mUids, index + 1, this.mNum - index);
            }
            this.mUids[index] = uid;
            this.mNum++;
        }
    }

    private synchronized void insert(int index, int uid, String name) {
        if (this.mUids == null) {
            this.mUids = new int[4];
            this.mUids[0] = uid;
            this.mNames = new String[4];
            this.mNames[0] = name;
            this.mNum = 1;
        } else if (this.mNum >= this.mUids.length) {
            int[] newuids = new int[(this.mNum * 3) / 2];
            String[] newnames = new String[(this.mNum * 3) / 2];
            if (index > 0) {
                System.arraycopy(this.mUids, 0, newuids, 0, index);
                System.arraycopy(this.mNames, 0, newnames, 0, index);
            }
            if (index < this.mNum) {
                System.arraycopy(this.mUids, index, newuids, index + 1, this.mNum - index);
                System.arraycopy(this.mNames, index, newnames, index + 1, this.mNum - index);
            }
            this.mUids = newuids;
            this.mNames = newnames;
            this.mUids[index] = uid;
            this.mNames[index] = name;
            this.mNum++;
        } else {
            if (index < this.mNum) {
                System.arraycopy(this.mUids, index, this.mUids, index + 1, this.mNum - index);
                System.arraycopy(this.mNames, index, this.mNames, index + 1, this.mNum - index);
            }
            this.mUids[index] = uid;
            this.mNames[index] = name;
            this.mNum++;
        }
    }

    @SystemApi
    /* loaded from: classes2.dex */
    public static final class WorkChain implements Parcelable {
        public static final Parcelable.Creator<WorkChain> CREATOR = new Parcelable.Creator<WorkChain>() { // from class: android.os.WorkSource.WorkChain.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public WorkChain createFromParcel(Parcel in) {
                return new WorkChain(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public WorkChain[] newArray(int size) {
                return new WorkChain[size];
            }
        };
        private int mSize;
        private String[] mTags;
        private int[] mUids;

        public WorkChain() {
            this.mSize = 0;
            this.mUids = new int[4];
            this.mTags = new String[4];
        }

        @VisibleForTesting
        public synchronized WorkChain(WorkChain other) {
            this.mSize = other.mSize;
            this.mUids = (int[]) other.mUids.clone();
            this.mTags = (String[]) other.mTags.clone();
        }

        private synchronized WorkChain(Parcel in) {
            this.mSize = in.readInt();
            this.mUids = in.createIntArray();
            this.mTags = in.createStringArray();
        }

        public WorkChain addNode(int uid, String tag) {
            if (this.mSize == this.mUids.length) {
                resizeArrays();
            }
            this.mUids[this.mSize] = uid;
            this.mTags[this.mSize] = tag;
            this.mSize++;
            return this;
        }

        public int getAttributionUid() {
            return this.mUids[0];
        }

        public String getAttributionTag() {
            return this.mTags[0];
        }

        @VisibleForTesting
        public synchronized int[] getUids() {
            int[] uids = new int[this.mSize];
            System.arraycopy(this.mUids, 0, uids, 0, this.mSize);
            return uids;
        }

        @VisibleForTesting
        public synchronized String[] getTags() {
            String[] tags = new String[this.mSize];
            System.arraycopy(this.mTags, 0, tags, 0, this.mSize);
            return tags;
        }

        @VisibleForTesting
        public synchronized int getSize() {
            return this.mSize;
        }

        private synchronized void resizeArrays() {
            int newSize = this.mSize * 2;
            int[] uids = new int[newSize];
            String[] tags = new String[newSize];
            System.arraycopy(this.mUids, 0, uids, 0, this.mSize);
            System.arraycopy(this.mTags, 0, tags, 0, this.mSize);
            this.mUids = uids;
            this.mTags = tags;
        }

        public String toString() {
            StringBuilder result = new StringBuilder("WorkChain{");
            for (int i = 0; i < this.mSize; i++) {
                if (i != 0) {
                    result.append(", ");
                }
                result.append("(");
                result.append(this.mUids[i]);
                if (this.mTags[i] != null) {
                    result.append(", ");
                    result.append(this.mTags[i]);
                }
                result.append(")");
            }
            result.append("}");
            return result.toString();
        }

        public int hashCode() {
            return ((this.mSize + (Arrays.hashCode(this.mUids) * 31)) * 31) + Arrays.hashCode(this.mTags);
        }

        public boolean equals(Object o) {
            if (o instanceof WorkChain) {
                WorkChain other = (WorkChain) o;
                return this.mSize == other.mSize && Arrays.equals(this.mUids, other.mUids) && Arrays.equals(this.mTags, other.mTags);
            }
            return false;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mSize);
            dest.writeIntArray(this.mUids);
            dest.writeStringArray(this.mTags);
        }
    }

    public static synchronized ArrayList<WorkChain>[] diffChains(WorkSource oldWs, WorkSource newWs) {
        ArrayList<WorkChain> newChains = null;
        ArrayList<WorkChain> goneChains = null;
        if (oldWs.mChains != null) {
            ArrayList<WorkChain> goneChains2 = null;
            for (int i = 0; i < oldWs.mChains.size(); i++) {
                WorkChain wc = oldWs.mChains.get(i);
                if (newWs.mChains == null || !newWs.mChains.contains(wc)) {
                    if (goneChains2 == null) {
                        goneChains2 = new ArrayList<>(oldWs.mChains.size());
                    }
                    goneChains2.add(wc);
                }
            }
            goneChains = goneChains2;
        }
        ArrayList<WorkChain> goneChains3 = newWs.mChains;
        if (goneChains3 != null) {
            ArrayList<WorkChain> newChains2 = null;
            for (int i2 = 0; i2 < newWs.mChains.size(); i2++) {
                WorkChain wc2 = newWs.mChains.get(i2);
                if (oldWs.mChains == null || !oldWs.mChains.contains(wc2)) {
                    if (newChains2 == null) {
                        newChains2 = new ArrayList<>(newWs.mChains.size());
                    }
                    newChains2.add(wc2);
                }
            }
            newChains = newChains2;
        }
        if (newChains == null && goneChains == null) {
            return null;
        }
        return new ArrayList[]{newChains, goneChains};
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mNum);
        dest.writeIntArray(this.mUids);
        dest.writeStringArray(this.mNames);
        if (this.mChains == null) {
            dest.writeInt(-1);
            return;
        }
        dest.writeInt(this.mChains.size());
        dest.writeParcelableList(this.mChains, flags);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("WorkSource{");
        for (int i = 0; i < this.mNum; i++) {
            if (i != 0) {
                result.append(", ");
            }
            result.append(this.mUids[i]);
            if (this.mNames != null) {
                result.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                result.append(this.mNames[i]);
            }
        }
        if (this.mChains != null) {
            result.append(" chains=");
            for (int i2 = 0; i2 < this.mChains.size(); i2++) {
                if (i2 != 0) {
                    result.append(", ");
                }
                result.append(this.mChains.get(i2));
            }
        }
        result.append("}");
        return result.toString();
    }

    public synchronized void writeToProto(ProtoOutputStream proto, long fieldId) {
        long j;
        long workSourceToken = proto.start(fieldId);
        int i = 0;
        while (true) {
            j = 2246267895809L;
            if (i >= this.mNum) {
                break;
            }
            long contentProto = proto.start(2246267895809L);
            proto.write(1120986464257L, this.mUids[i]);
            if (this.mNames != null) {
                proto.write(1138166333442L, this.mNames[i]);
            }
            proto.end(contentProto);
            i++;
        }
        if (this.mChains != null) {
            int i2 = 0;
            while (i2 < this.mChains.size()) {
                WorkChain wc = this.mChains.get(i2);
                long workChain = proto.start(2246267895810L);
                String[] tags = wc.getTags();
                int[] uids = wc.getUids();
                int j2 = 0;
                while (true) {
                    int j3 = j2;
                    if (j3 < tags.length) {
                        long contentProto2 = proto.start(j);
                        proto.write(1120986464257L, uids[j3]);
                        proto.write(1138166333442L, tags[j3]);
                        proto.end(contentProto2);
                        j2 = j3 + 1;
                        j = 2246267895809L;
                    }
                }
                proto.end(workChain);
                i2++;
                j = 2246267895809L;
            }
        }
        proto.end(workSourceToken);
    }
}
