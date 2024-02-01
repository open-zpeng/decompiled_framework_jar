package android.renderscript;

import android.content.res.AssetManager;
import android.content.res.Resources;
import java.io.File;
import java.io.InputStream;
/* loaded from: classes2.dex */
public class FileA3D extends BaseObj {
    IndexEntry[] mFileEntries;
    InputStream mInputStream;

    /* loaded from: classes2.dex */
    public enum EntryType {
        UNKNOWN(0),
        MESH(1);
        
        int mID;

        EntryType(int id) {
            this.mID = id;
        }

        static synchronized EntryType toEntryType(int intID) {
            return values()[intID];
        }
    }

    /* loaded from: classes2.dex */
    public static class IndexEntry {
        EntryType mEntryType;
        long mID;
        int mIndex;
        BaseObj mLoadedObj = null;
        String mName;
        RenderScript mRS;

        public synchronized String getName() {
            return this.mName;
        }

        private protected EntryType getEntryType() {
            return this.mEntryType;
        }

        private protected BaseObj getObject() {
            this.mRS.validate();
            BaseObj obj = internalCreate(this.mRS, this);
            return obj;
        }

        public synchronized Mesh getMesh() {
            return (Mesh) getObject();
        }

        static synchronized BaseObj internalCreate(RenderScript rs, IndexEntry entry) {
            synchronized (IndexEntry.class) {
                if (entry.mLoadedObj != null) {
                    return entry.mLoadedObj;
                } else if (entry.mEntryType == EntryType.UNKNOWN) {
                    return null;
                } else {
                    long objectID = rs.nFileA3DGetEntryByIndex(entry.mID, entry.mIndex);
                    if (objectID == 0) {
                        return null;
                    }
                    if (AnonymousClass1.$SwitchMap$android$renderscript$FileA3D$EntryType[entry.mEntryType.ordinal()] == 1) {
                        entry.mLoadedObj = new Mesh(objectID, rs);
                        entry.mLoadedObj.updateFromNative();
                        return entry.mLoadedObj;
                    }
                    throw new RSRuntimeException("Unrecognized object type in file.");
                }
            }
        }

        synchronized IndexEntry(RenderScript rs, int index, long id, String name, EntryType type) {
            this.mRS = rs;
            this.mIndex = index;
            this.mID = id;
            this.mName = name;
            this.mEntryType = type;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: android.renderscript.FileA3D$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$renderscript$FileA3D$EntryType = new int[EntryType.values().length];

        static {
            try {
                $SwitchMap$android$renderscript$FileA3D$EntryType[EntryType.MESH.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
        }
    }

    synchronized FileA3D(long id, RenderScript rs, InputStream stream) {
        super(id, rs);
        this.mInputStream = stream;
        this.guard.open("destroy");
    }

    private synchronized void initEntries() {
        int numFileEntries = this.mRS.nFileA3DGetNumIndexEntries(getID(this.mRS));
        if (numFileEntries <= 0) {
            return;
        }
        this.mFileEntries = new IndexEntry[numFileEntries];
        int[] ids = new int[numFileEntries];
        String[] names = new String[numFileEntries];
        this.mRS.nFileA3DGetIndexEntries(getID(this.mRS), numFileEntries, ids, names);
        for (int i = 0; i < numFileEntries; i++) {
            this.mFileEntries[i] = new IndexEntry(this.mRS, i, getID(this.mRS), names[i], EntryType.toEntryType(ids[i]));
        }
    }

    public synchronized int getIndexEntryCount() {
        if (this.mFileEntries == null) {
            return 0;
        }
        return this.mFileEntries.length;
    }

    private protected IndexEntry getIndexEntry(int index) {
        if (getIndexEntryCount() == 0 || index < 0 || index >= this.mFileEntries.length) {
            return null;
        }
        return this.mFileEntries[index];
    }

    public static synchronized FileA3D createFromAsset(RenderScript rs, AssetManager mgr, String path) {
        rs.validate();
        long fileId = rs.nFileA3DCreateFromAsset(mgr, path);
        if (fileId == 0) {
            throw new RSRuntimeException("Unable to create a3d file from asset " + path);
        }
        FileA3D fa3d = new FileA3D(fileId, rs, null);
        fa3d.initEntries();
        return fa3d;
    }

    public static synchronized FileA3D createFromFile(RenderScript rs, String path) {
        long fileId = rs.nFileA3DCreateFromFile(path);
        if (fileId == 0) {
            throw new RSRuntimeException("Unable to create a3d file from " + path);
        }
        FileA3D fa3d = new FileA3D(fileId, rs, null);
        fa3d.initEntries();
        return fa3d;
    }

    public static synchronized FileA3D createFromFile(RenderScript rs, File path) {
        return createFromFile(rs, path.getAbsolutePath());
    }

    private protected static FileA3D createFromResource(RenderScript rs, Resources res, int id) {
        rs.validate();
        try {
            InputStream is = res.openRawResource(id);
            if (is instanceof AssetManager.AssetInputStream) {
                long asset = ((AssetManager.AssetInputStream) is).getNativeAsset();
                long fileId = rs.nFileA3DCreateFromAssetStream(asset);
                if (fileId == 0) {
                    throw new RSRuntimeException("Unable to create a3d file from resource " + id);
                }
                FileA3D fa3d = new FileA3D(fileId, rs, is);
                fa3d.initEntries();
                return fa3d;
            }
            throw new RSRuntimeException("Unsupported asset stream");
        } catch (Exception e) {
            throw new RSRuntimeException("Unable to open resource " + id);
        }
    }
}
