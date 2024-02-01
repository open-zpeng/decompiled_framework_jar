package android.hardware.radio;

import android.annotation.SystemApi;
import android.hardware.radio.ProgramList;
import android.hardware.radio.ProgramSelector;
import android.hardware.radio.RadioManager;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@SystemApi
/* loaded from: classes.dex */
public final class ProgramList implements AutoCloseable {
    private OnCloseListener mOnCloseListener;
    private final Object mLock = new Object();
    private final Map<ProgramSelector.Identifier, RadioManager.ProgramInfo> mPrograms = new HashMap();
    private final List<ListCallback> mListCallbacks = new ArrayList();
    private final List<OnCompleteListener> mOnCompleteListeners = new ArrayList();
    private boolean mIsClosed = false;
    private boolean mIsComplete = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface OnCloseListener {
        void onClose();
    }

    /* loaded from: classes.dex */
    public interface OnCompleteListener {
        void onComplete();
    }

    /* loaded from: classes.dex */
    public static abstract class ListCallback {
        public void onItemChanged(ProgramSelector.Identifier id) {
        }

        public void onItemRemoved(ProgramSelector.Identifier id) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: android.hardware.radio.ProgramList$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends ListCallback {
        final /* synthetic */ ListCallback val$callback;
        final /* synthetic */ Executor val$executor;

        AnonymousClass1(Executor executor, ListCallback listCallback) {
            this.val$executor = executor;
            this.val$callback = listCallback;
        }

        @Override // android.hardware.radio.ProgramList.ListCallback
        public void onItemChanged(final ProgramSelector.Identifier id) {
            Executor executor = this.val$executor;
            final ListCallback listCallback = this.val$callback;
            executor.execute(new Runnable() { // from class: android.hardware.radio.-$$Lambda$ProgramList$1$DVvry5MfhR6n8H2EZn67rvuhllI
                @Override // java.lang.Runnable
                public final void run() {
                    ProgramList.ListCallback.this.onItemChanged(id);
                }
            });
        }

        @Override // android.hardware.radio.ProgramList.ListCallback
        public void onItemRemoved(final ProgramSelector.Identifier id) {
            Executor executor = this.val$executor;
            final ListCallback listCallback = this.val$callback;
            executor.execute(new Runnable() { // from class: android.hardware.radio.-$$Lambda$ProgramList$1$a_xWqo5pESOZhcJIWvpiCd2AXmY
                @Override // java.lang.Runnable
                public final void run() {
                    ProgramList.ListCallback.this.onItemRemoved(id);
                }
            });
        }
    }

    public void registerListCallback(Executor executor, ListCallback callback) {
        registerListCallback(new AnonymousClass1(executor, callback));
    }

    public void registerListCallback(ListCallback callback) {
        synchronized (this.mLock) {
            if (this.mIsClosed) {
                return;
            }
            this.mListCallbacks.add((ListCallback) Objects.requireNonNull(callback));
        }
    }

    public void unregisterListCallback(ListCallback callback) {
        synchronized (this.mLock) {
            if (this.mIsClosed) {
                return;
            }
            this.mListCallbacks.remove(Objects.requireNonNull(callback));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$addOnCompleteListener$0(Executor executor, final OnCompleteListener listener) {
        Objects.requireNonNull(listener);
        executor.execute(new Runnable() { // from class: android.hardware.radio.-$$Lambda$1DA3e7WM2G0cVcFyFUhdDG0CYnw
            @Override // java.lang.Runnable
            public final void run() {
                ProgramList.OnCompleteListener.this.onComplete();
            }
        });
    }

    public void addOnCompleteListener(final Executor executor, final OnCompleteListener listener) {
        addOnCompleteListener(new OnCompleteListener() { // from class: android.hardware.radio.-$$Lambda$ProgramList$aDYMynqVdAUqeKXIxfNtN1u67zs
            @Override // android.hardware.radio.ProgramList.OnCompleteListener
            public final void onComplete() {
                ProgramList.lambda$addOnCompleteListener$0(executor, listener);
            }
        });
    }

    public void addOnCompleteListener(OnCompleteListener listener) {
        synchronized (this.mLock) {
            if (this.mIsClosed) {
                return;
            }
            this.mOnCompleteListeners.add((OnCompleteListener) Objects.requireNonNull(listener));
            if (this.mIsComplete) {
                listener.onComplete();
            }
        }
    }

    public void removeOnCompleteListener(OnCompleteListener listener) {
        synchronized (this.mLock) {
            if (this.mIsClosed) {
                return;
            }
            this.mOnCompleteListeners.remove(Objects.requireNonNull(listener));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOnCloseListener(OnCloseListener listener) {
        synchronized (this.mLock) {
            if (this.mOnCloseListener != null) {
                throw new IllegalStateException("Close callback is already set");
            }
            this.mOnCloseListener = listener;
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        synchronized (this.mLock) {
            if (this.mIsClosed) {
                return;
            }
            this.mIsClosed = true;
            this.mPrograms.clear();
            this.mListCallbacks.clear();
            this.mOnCompleteListeners.clear();
            if (this.mOnCloseListener != null) {
                this.mOnCloseListener.onClose();
                this.mOnCloseListener = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void apply(Chunk chunk) {
        synchronized (this.mLock) {
            if (this.mIsClosed) {
                return;
            }
            this.mIsComplete = false;
            if (chunk.isPurge()) {
                new HashSet(this.mPrograms.keySet()).stream().forEach(new Consumer() { // from class: android.hardware.radio.-$$Lambda$ProgramList$F-JpTj3vYguKIUQbnLbTePTuqUE
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ProgramList.this.lambda$apply$1$ProgramList((ProgramSelector.Identifier) obj);
                    }
                });
            }
            chunk.getRemoved().stream().forEach(new Consumer() { // from class: android.hardware.radio.-$$Lambda$ProgramList$pKu0Zp5jwjix619hfB_Imj8Ke_g
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ProgramList.this.lambda$apply$2$ProgramList((ProgramSelector.Identifier) obj);
                }
            });
            chunk.getModified().stream().forEach(new Consumer() { // from class: android.hardware.radio.-$$Lambda$ProgramList$eY050tMTgAcGV9hiWR-UDxhkfhw
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ProgramList.this.lambda$apply$3$ProgramList((RadioManager.ProgramInfo) obj);
                }
            });
            if (chunk.isComplete()) {
                this.mIsComplete = true;
                this.mOnCompleteListeners.forEach(new Consumer() { // from class: android.hardware.radio.-$$Lambda$ProgramList$GfCj9jJ5znxw2TV4c2uykq35dgI
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((ProgramList.OnCompleteListener) obj).onComplete();
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: putLocked */
    public void lambda$apply$3$ProgramList(RadioManager.ProgramInfo value) {
        ProgramSelector.Identifier key = value.getSelector().getPrimaryId();
        this.mPrograms.put((ProgramSelector.Identifier) Objects.requireNonNull(key), value);
        final ProgramSelector.Identifier sel = value.getSelector().getPrimaryId();
        this.mListCallbacks.forEach(new Consumer() { // from class: android.hardware.radio.-$$Lambda$ProgramList$fDnoTVk5UB7qTfD9S7SYPcadYn0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ProgramList.ListCallback) obj).onItemChanged(ProgramSelector.Identifier.this);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: removeLocked */
    public void lambda$apply$2$ProgramList(ProgramSelector.Identifier key) {
        RadioManager.ProgramInfo removed = this.mPrograms.remove(Objects.requireNonNull(key));
        if (removed == null) {
            return;
        }
        final ProgramSelector.Identifier sel = removed.getSelector().getPrimaryId();
        this.mListCallbacks.forEach(new Consumer() { // from class: android.hardware.radio.-$$Lambda$ProgramList$fHYelmhnUsVTYl6dFj75fMqCjGs
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((ProgramList.ListCallback) obj).onItemRemoved(ProgramSelector.Identifier.this);
            }
        });
    }

    public List<RadioManager.ProgramInfo> toList() {
        List<RadioManager.ProgramInfo> list;
        synchronized (this.mLock) {
            list = (List) this.mPrograms.values().stream().collect(Collectors.toList());
        }
        return list;
    }

    public RadioManager.ProgramInfo get(ProgramSelector.Identifier id) {
        RadioManager.ProgramInfo programInfo;
        synchronized (this.mLock) {
            programInfo = this.mPrograms.get(Objects.requireNonNull(id));
        }
        return programInfo;
    }

    /* loaded from: classes.dex */
    public static final class Filter implements Parcelable {
        public static final Parcelable.Creator<Filter> CREATOR = new Parcelable.Creator<Filter>() { // from class: android.hardware.radio.ProgramList.Filter.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Filter createFromParcel(Parcel in) {
                return new Filter(in, null);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Filter[] newArray(int size) {
                return new Filter[size];
            }
        };
        private final boolean mExcludeModifications;
        private final Set<Integer> mIdentifierTypes;
        private final Set<ProgramSelector.Identifier> mIdentifiers;
        private final boolean mIncludeCategories;
        private final Map<String, String> mVendorFilter;

        /* synthetic */ Filter(Parcel x0, AnonymousClass1 x1) {
            this(x0);
        }

        public Filter(Set<Integer> identifierTypes, Set<ProgramSelector.Identifier> identifiers, boolean includeCategories, boolean excludeModifications) {
            this.mIdentifierTypes = (Set) Objects.requireNonNull(identifierTypes);
            this.mIdentifiers = (Set) Objects.requireNonNull(identifiers);
            this.mIncludeCategories = includeCategories;
            this.mExcludeModifications = excludeModifications;
            this.mVendorFilter = null;
        }

        public Filter() {
            this.mIdentifierTypes = Collections.emptySet();
            this.mIdentifiers = Collections.emptySet();
            this.mIncludeCategories = false;
            this.mExcludeModifications = false;
            this.mVendorFilter = null;
        }

        public Filter(Map<String, String> vendorFilter) {
            this.mIdentifierTypes = Collections.emptySet();
            this.mIdentifiers = Collections.emptySet();
            this.mIncludeCategories = false;
            this.mExcludeModifications = false;
            this.mVendorFilter = vendorFilter;
        }

        private Filter(Parcel in) {
            this.mIdentifierTypes = Utils.createIntSet(in);
            this.mIdentifiers = Utils.createSet(in, ProgramSelector.Identifier.CREATOR);
            this.mIncludeCategories = in.readByte() != 0;
            this.mExcludeModifications = in.readByte() != 0;
            this.mVendorFilter = Utils.readStringMap(in);
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            Utils.writeIntSet(dest, this.mIdentifierTypes);
            Utils.writeSet(dest, this.mIdentifiers);
            dest.writeByte(this.mIncludeCategories ? (byte) 1 : (byte) 0);
            dest.writeByte(this.mExcludeModifications ? (byte) 1 : (byte) 0);
            Utils.writeStringMap(dest, this.mVendorFilter);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public Map<String, String> getVendorFilter() {
            return this.mVendorFilter;
        }

        public Set<Integer> getIdentifierTypes() {
            return this.mIdentifierTypes;
        }

        public Set<ProgramSelector.Identifier> getIdentifiers() {
            return this.mIdentifiers;
        }

        public boolean areCategoriesIncluded() {
            return this.mIncludeCategories;
        }

        public boolean areModificationsExcluded() {
            return this.mExcludeModifications;
        }
    }

    /* loaded from: classes.dex */
    public static final class Chunk implements Parcelable {
        public static final Parcelable.Creator<Chunk> CREATOR = new Parcelable.Creator<Chunk>() { // from class: android.hardware.radio.ProgramList.Chunk.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Chunk createFromParcel(Parcel in) {
                return new Chunk(in, null);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public Chunk[] newArray(int size) {
                return new Chunk[size];
            }
        };
        private final boolean mComplete;
        private final Set<RadioManager.ProgramInfo> mModified;
        private final boolean mPurge;
        private final Set<ProgramSelector.Identifier> mRemoved;

        /* synthetic */ Chunk(Parcel x0, AnonymousClass1 x1) {
            this(x0);
        }

        public Chunk(boolean purge, boolean complete, Set<RadioManager.ProgramInfo> modified, Set<ProgramSelector.Identifier> removed) {
            this.mPurge = purge;
            this.mComplete = complete;
            this.mModified = modified != null ? modified : Collections.emptySet();
            this.mRemoved = removed != null ? removed : Collections.emptySet();
        }

        private Chunk(Parcel in) {
            this.mPurge = in.readByte() != 0;
            this.mComplete = in.readByte() != 0;
            this.mModified = Utils.createSet(in, RadioManager.ProgramInfo.CREATOR);
            this.mRemoved = Utils.createSet(in, ProgramSelector.Identifier.CREATOR);
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte(this.mPurge ? (byte) 1 : (byte) 0);
            dest.writeByte(this.mComplete ? (byte) 1 : (byte) 0);
            Utils.writeSet(dest, this.mModified);
            Utils.writeSet(dest, this.mRemoved);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public boolean isPurge() {
            return this.mPurge;
        }

        public boolean isComplete() {
            return this.mComplete;
        }

        public Set<RadioManager.ProgramInfo> getModified() {
            return this.mModified;
        }

        public Set<ProgramSelector.Identifier> getRemoved() {
            return this.mRemoved;
        }
    }
}
