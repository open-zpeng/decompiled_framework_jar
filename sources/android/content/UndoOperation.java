package android.content;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public abstract class UndoOperation<DATA> implements Parcelable {
    UndoOwner mOwner;

    public abstract synchronized void commit();

    public abstract synchronized void redo();

    public abstract synchronized void undo();

    /* JADX INFO: Access modifiers changed from: private */
    public UndoOperation(UndoOwner owner) {
        this.mOwner = owner;
    }

    public private UndoOperation(Parcel src, ClassLoader loader) {
    }

    public synchronized UndoOwner getOwner() {
        return this.mOwner;
    }

    public synchronized DATA getOwnerData() {
        return (DATA) this.mOwner.getData();
    }

    public synchronized boolean matchOwner(UndoOwner owner) {
        return owner == getOwner();
    }

    public synchronized boolean hasData() {
        return true;
    }

    public synchronized boolean allowMerge() {
        return true;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
