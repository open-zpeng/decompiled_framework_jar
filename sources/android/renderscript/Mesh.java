package android.renderscript;

import android.provider.BrowserContract;
import android.renderscript.Element;
import android.renderscript.Type;
import java.util.Vector;
/* loaded from: classes2.dex */
public class Mesh extends BaseObj {
    Allocation[] mIndexBuffers;
    Primitive[] mPrimitives;
    Allocation[] mVertexBuffers;

    /* loaded from: classes2.dex */
    public enum Primitive {
        POINT(0),
        LINE(1),
        LINE_STRIP(2),
        TRIANGLE(3),
        TRIANGLE_STRIP(4),
        TRIANGLE_FAN(5);
        
        int mID;

        Primitive(int id) {
            this.mID = id;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized Mesh(long id, RenderScript rs) {
        super(id, rs);
        this.guard.open("destroy");
    }

    public synchronized int getVertexAllocationCount() {
        if (this.mVertexBuffers == null) {
            return 0;
        }
        return this.mVertexBuffers.length;
    }

    private protected Allocation getVertexAllocation(int slot) {
        return this.mVertexBuffers[slot];
    }

    public synchronized int getPrimitiveCount() {
        if (this.mIndexBuffers == null) {
            return 0;
        }
        return this.mIndexBuffers.length;
    }

    public synchronized Allocation getIndexSetAllocation(int slot) {
        return this.mIndexBuffers[slot];
    }

    public synchronized Primitive getPrimitive(int slot) {
        return this.mPrimitives[slot];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // android.renderscript.BaseObj
    public synchronized void updateFromNative() {
        super.updateFromNative();
        int vtxCount = this.mRS.nMeshGetVertexBufferCount(getID(this.mRS));
        int idxCount = this.mRS.nMeshGetIndexCount(getID(this.mRS));
        long[] vtxIDs = new long[vtxCount];
        long[] idxIDs = new long[idxCount];
        int[] primitives = new int[idxCount];
        this.mRS.nMeshGetVertices(getID(this.mRS), vtxIDs, vtxCount);
        this.mRS.nMeshGetIndices(getID(this.mRS), idxIDs, primitives, idxCount);
        this.mVertexBuffers = new Allocation[vtxCount];
        this.mIndexBuffers = new Allocation[idxCount];
        this.mPrimitives = new Primitive[idxCount];
        for (int i = 0; i < vtxCount; i++) {
            if (vtxIDs[i] != 0) {
                this.mVertexBuffers[i] = new Allocation(vtxIDs[i], this.mRS, null, 1);
                this.mVertexBuffers[i].updateFromNative();
            }
        }
        for (int i2 = 0; i2 < idxCount; i2++) {
            if (idxIDs[i2] != 0) {
                this.mIndexBuffers[i2] = new Allocation(idxIDs[i2], this.mRS, null, 1);
                this.mIndexBuffers[i2].updateFromNative();
            }
            this.mPrimitives[i2] = Primitive.values()[primitives[i2]];
        }
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        RenderScript mRS;
        int mUsage;
        int mVertexTypeCount = 0;
        Entry[] mVertexTypes = new Entry[16];
        Vector mIndexTypes = new Vector();

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes2.dex */
        public class Entry {
            Element e;
            Primitive prim;
            int size;
            Type t;
            int usage;

            Entry() {
            }
        }

        public synchronized Builder(RenderScript rs, int usage) {
            this.mRS = rs;
            this.mUsage = usage;
        }

        public synchronized int getCurrentVertexTypeIndex() {
            return this.mVertexTypeCount - 1;
        }

        public synchronized int getCurrentIndexSetIndex() {
            return this.mIndexTypes.size() - 1;
        }

        public synchronized Builder addVertexType(Type t) throws IllegalStateException {
            if (this.mVertexTypeCount >= this.mVertexTypes.length) {
                throw new IllegalStateException("Max vertex types exceeded.");
            }
            this.mVertexTypes[this.mVertexTypeCount] = new Entry();
            this.mVertexTypes[this.mVertexTypeCount].t = t;
            this.mVertexTypes[this.mVertexTypeCount].e = null;
            this.mVertexTypeCount++;
            return this;
        }

        public synchronized Builder addVertexType(Element e, int size) throws IllegalStateException {
            if (this.mVertexTypeCount >= this.mVertexTypes.length) {
                throw new IllegalStateException("Max vertex types exceeded.");
            }
            this.mVertexTypes[this.mVertexTypeCount] = new Entry();
            this.mVertexTypes[this.mVertexTypeCount].t = null;
            this.mVertexTypes[this.mVertexTypeCount].e = e;
            this.mVertexTypes[this.mVertexTypeCount].size = size;
            this.mVertexTypeCount++;
            return this;
        }

        public synchronized Builder addIndexSetType(Type t, Primitive p) {
            Entry indexType = new Entry();
            indexType.t = t;
            indexType.e = null;
            indexType.size = 0;
            indexType.prim = p;
            this.mIndexTypes.addElement(indexType);
            return this;
        }

        public synchronized Builder addIndexSetType(Primitive p) {
            Entry indexType = new Entry();
            indexType.t = null;
            indexType.e = null;
            indexType.size = 0;
            indexType.prim = p;
            this.mIndexTypes.addElement(indexType);
            return this;
        }

        public synchronized Builder addIndexSetType(Element e, int size, Primitive p) {
            Entry indexType = new Entry();
            indexType.t = null;
            indexType.e = e;
            indexType.size = size;
            indexType.prim = p;
            this.mIndexTypes.addElement(indexType);
            return this;
        }

        synchronized Type newType(Element e, int size) {
            Type.Builder tb = new Type.Builder(this.mRS, e);
            tb.setX(size);
            return tb.create();
        }

        public synchronized Mesh create() {
            Allocation alloc;
            Allocation alloc2;
            this.mRS.validate();
            long[] vtx = new long[this.mVertexTypeCount];
            long[] idx = new long[this.mIndexTypes.size()];
            int[] prim = new int[this.mIndexTypes.size()];
            Allocation[] vertexBuffers = new Allocation[this.mVertexTypeCount];
            Allocation[] indexBuffers = new Allocation[this.mIndexTypes.size()];
            Primitive[] primitives = new Primitive[this.mIndexTypes.size()];
            for (int ct = 0; ct < this.mVertexTypeCount; ct++) {
                Entry entry = this.mVertexTypes[ct];
                if (entry.t != null) {
                    alloc2 = Allocation.createTyped(this.mRS, entry.t, this.mUsage);
                } else if (entry.e != null) {
                    alloc2 = Allocation.createSized(this.mRS, entry.e, entry.size, this.mUsage);
                } else {
                    throw new IllegalStateException("Builder corrupt, no valid element in entry.");
                }
                vertexBuffers[ct] = alloc2;
                vtx[ct] = alloc2.getID(this.mRS);
            }
            for (int ct2 = 0; ct2 < this.mIndexTypes.size(); ct2++) {
                Entry entry2 = (Entry) this.mIndexTypes.elementAt(ct2);
                if (entry2.t != null) {
                    alloc = Allocation.createTyped(this.mRS, entry2.t, this.mUsage);
                } else if (entry2.e != null) {
                    alloc = Allocation.createSized(this.mRS, entry2.e, entry2.size, this.mUsage);
                } else {
                    throw new IllegalStateException("Builder corrupt, no valid element in entry.");
                }
                long allocID = alloc == null ? 0L : alloc.getID(this.mRS);
                indexBuffers[ct2] = alloc;
                primitives[ct2] = entry2.prim;
                idx[ct2] = allocID;
                prim[ct2] = entry2.prim.mID;
            }
            long id = this.mRS.nMeshCreate(vtx, idx, prim);
            Mesh newMesh = new Mesh(id, this.mRS);
            newMesh.mVertexBuffers = vertexBuffers;
            newMesh.mIndexBuffers = indexBuffers;
            newMesh.mPrimitives = primitives;
            return newMesh;
        }
    }

    /* loaded from: classes2.dex */
    public static class AllocationBuilder {
        RenderScript mRS;
        int mVertexTypeCount = 0;
        Entry[] mVertexTypes = new Entry[16];
        Vector mIndexTypes = new Vector();

        /* loaded from: classes2.dex */
        class Entry {
            Allocation a;
            Primitive prim;

            Entry() {
            }
        }

        private protected AllocationBuilder(RenderScript rs) {
            this.mRS = rs;
        }

        public synchronized int getCurrentVertexTypeIndex() {
            return this.mVertexTypeCount - 1;
        }

        public synchronized int getCurrentIndexSetIndex() {
            return this.mIndexTypes.size() - 1;
        }

        private protected AllocationBuilder addVertexAllocation(Allocation a) throws IllegalStateException {
            if (this.mVertexTypeCount >= this.mVertexTypes.length) {
                throw new IllegalStateException("Max vertex types exceeded.");
            }
            this.mVertexTypes[this.mVertexTypeCount] = new Entry();
            this.mVertexTypes[this.mVertexTypeCount].a = a;
            this.mVertexTypeCount++;
            return this;
        }

        private protected AllocationBuilder addIndexSetAllocation(Allocation a, Primitive p) {
            Entry indexType = new Entry();
            indexType.a = a;
            indexType.prim = p;
            this.mIndexTypes.addElement(indexType);
            return this;
        }

        private protected AllocationBuilder addIndexSetType(Primitive p) {
            Entry indexType = new Entry();
            indexType.a = null;
            indexType.prim = p;
            this.mIndexTypes.addElement(indexType);
            return this;
        }

        private protected Mesh create() {
            this.mRS.validate();
            long[] vtx = new long[this.mVertexTypeCount];
            long[] idx = new long[this.mIndexTypes.size()];
            int[] prim = new int[this.mIndexTypes.size()];
            Allocation[] indexBuffers = new Allocation[this.mIndexTypes.size()];
            Primitive[] primitives = new Primitive[this.mIndexTypes.size()];
            Allocation[] vertexBuffers = new Allocation[this.mVertexTypeCount];
            for (int ct = 0; ct < this.mVertexTypeCount; ct++) {
                Entry entry = this.mVertexTypes[ct];
                vertexBuffers[ct] = entry.a;
                vtx[ct] = entry.a.getID(this.mRS);
            }
            for (int ct2 = 0; ct2 < this.mIndexTypes.size(); ct2++) {
                Entry entry2 = (Entry) this.mIndexTypes.elementAt(ct2);
                long allocID = entry2.a == null ? 0L : entry2.a.getID(this.mRS);
                indexBuffers[ct2] = entry2.a;
                primitives[ct2] = entry2.prim;
                idx[ct2] = allocID;
                prim[ct2] = entry2.prim.mID;
            }
            long id = this.mRS.nMeshCreate(vtx, idx, prim);
            Mesh newMesh = new Mesh(id, this.mRS);
            newMesh.mVertexBuffers = vertexBuffers;
            newMesh.mIndexBuffers = indexBuffers;
            newMesh.mPrimitives = primitives;
            return newMesh;
        }
    }

    /* loaded from: classes2.dex */
    public static class TriangleMeshBuilder {
        public static final int COLOR = 1;
        public static final int NORMAL = 2;
        public static final int TEXTURE_0 = 256;
        Element mElement;
        int mFlags;
        RenderScript mRS;
        int mVtxSize;
        float mNX = 0.0f;
        float mNY = 0.0f;
        float mNZ = -1.0f;
        float mS0 = 0.0f;
        float mT0 = 0.0f;
        float mR = 1.0f;
        float mG = 1.0f;
        float mB = 1.0f;
        float mA = 1.0f;
        int mVtxCount = 0;
        int mMaxIndex = 0;
        int mIndexCount = 0;
        float[] mVtxData = new float[128];
        short[] mIndexData = new short[128];

        private protected TriangleMeshBuilder(RenderScript rs, int vtxSize, int flags) {
            this.mRS = rs;
            this.mVtxSize = vtxSize;
            this.mFlags = flags;
            if (vtxSize < 2 || vtxSize > 3) {
                throw new IllegalArgumentException("Vertex size out of range.");
            }
        }

        private synchronized void makeSpace(int count) {
            if (this.mVtxCount + count >= this.mVtxData.length) {
                float[] t = new float[this.mVtxData.length * 2];
                System.arraycopy(this.mVtxData, 0, t, 0, this.mVtxData.length);
                this.mVtxData = t;
            }
        }

        private synchronized void latch() {
            if ((this.mFlags & 1) != 0) {
                makeSpace(4);
                float[] fArr = this.mVtxData;
                int i = this.mVtxCount;
                this.mVtxCount = i + 1;
                fArr[i] = this.mR;
                float[] fArr2 = this.mVtxData;
                int i2 = this.mVtxCount;
                this.mVtxCount = i2 + 1;
                fArr2[i2] = this.mG;
                float[] fArr3 = this.mVtxData;
                int i3 = this.mVtxCount;
                this.mVtxCount = i3 + 1;
                fArr3[i3] = this.mB;
                float[] fArr4 = this.mVtxData;
                int i4 = this.mVtxCount;
                this.mVtxCount = i4 + 1;
                fArr4[i4] = this.mA;
            }
            if ((this.mFlags & 256) != 0) {
                makeSpace(2);
                float[] fArr5 = this.mVtxData;
                int i5 = this.mVtxCount;
                this.mVtxCount = i5 + 1;
                fArr5[i5] = this.mS0;
                float[] fArr6 = this.mVtxData;
                int i6 = this.mVtxCount;
                this.mVtxCount = i6 + 1;
                fArr6[i6] = this.mT0;
            }
            if ((this.mFlags & 2) != 0) {
                makeSpace(4);
                float[] fArr7 = this.mVtxData;
                int i7 = this.mVtxCount;
                this.mVtxCount = i7 + 1;
                fArr7[i7] = this.mNX;
                float[] fArr8 = this.mVtxData;
                int i8 = this.mVtxCount;
                this.mVtxCount = i8 + 1;
                fArr8[i8] = this.mNY;
                float[] fArr9 = this.mVtxData;
                int i9 = this.mVtxCount;
                this.mVtxCount = i9 + 1;
                fArr9[i9] = this.mNZ;
                float[] fArr10 = this.mVtxData;
                int i10 = this.mVtxCount;
                this.mVtxCount = i10 + 1;
                fArr10[i10] = 0.0f;
            }
            this.mMaxIndex++;
        }

        private protected TriangleMeshBuilder addVertex(float x, float y) {
            if (this.mVtxSize != 2) {
                throw new IllegalStateException("add mistmatch with declared components.");
            }
            makeSpace(2);
            float[] fArr = this.mVtxData;
            int i = this.mVtxCount;
            this.mVtxCount = i + 1;
            fArr[i] = x;
            float[] fArr2 = this.mVtxData;
            int i2 = this.mVtxCount;
            this.mVtxCount = i2 + 1;
            fArr2[i2] = y;
            latch();
            return this;
        }

        public synchronized TriangleMeshBuilder addVertex(float x, float y, float z) {
            if (this.mVtxSize != 3) {
                throw new IllegalStateException("add mistmatch with declared components.");
            }
            makeSpace(4);
            float[] fArr = this.mVtxData;
            int i = this.mVtxCount;
            this.mVtxCount = i + 1;
            fArr[i] = x;
            float[] fArr2 = this.mVtxData;
            int i2 = this.mVtxCount;
            this.mVtxCount = i2 + 1;
            fArr2[i2] = y;
            float[] fArr3 = this.mVtxData;
            int i3 = this.mVtxCount;
            this.mVtxCount = i3 + 1;
            fArr3[i3] = z;
            float[] fArr4 = this.mVtxData;
            int i4 = this.mVtxCount;
            this.mVtxCount = i4 + 1;
            fArr4[i4] = 1.0f;
            latch();
            return this;
        }

        public synchronized TriangleMeshBuilder setTexture(float s, float t) {
            if ((this.mFlags & 256) == 0) {
                throw new IllegalStateException("add mistmatch with declared components.");
            }
            this.mS0 = s;
            this.mT0 = t;
            return this;
        }

        public synchronized TriangleMeshBuilder setNormal(float x, float y, float z) {
            if ((this.mFlags & 2) == 0) {
                throw new IllegalStateException("add mistmatch with declared components.");
            }
            this.mNX = x;
            this.mNY = y;
            this.mNZ = z;
            return this;
        }

        public synchronized TriangleMeshBuilder setColor(float r, float g, float b, float a) {
            if ((this.mFlags & 1) == 0) {
                throw new IllegalStateException("add mistmatch with declared components.");
            }
            this.mR = r;
            this.mG = g;
            this.mB = b;
            this.mA = a;
            return this;
        }

        private protected TriangleMeshBuilder addTriangle(int idx1, int idx2, int idx3) {
            if (idx1 >= this.mMaxIndex || idx1 < 0 || idx2 >= this.mMaxIndex || idx2 < 0 || idx3 >= this.mMaxIndex || idx3 < 0) {
                throw new IllegalStateException("Index provided greater than vertex count.");
            }
            if (this.mIndexCount + 3 >= this.mIndexData.length) {
                short[] t = new short[this.mIndexData.length * 2];
                System.arraycopy(this.mIndexData, 0, t, 0, this.mIndexData.length);
                this.mIndexData = t;
            }
            short[] t2 = this.mIndexData;
            int i = this.mIndexCount;
            this.mIndexCount = i + 1;
            t2[i] = (short) idx1;
            short[] sArr = this.mIndexData;
            int i2 = this.mIndexCount;
            this.mIndexCount = i2 + 1;
            sArr[i2] = (short) idx2;
            short[] sArr2 = this.mIndexData;
            int i3 = this.mIndexCount;
            this.mIndexCount = i3 + 1;
            sArr2[i3] = (short) idx3;
            return this;
        }

        private protected Mesh create(boolean uploadToBufferObject) {
            Element.Builder b = new Element.Builder(this.mRS);
            b.add(Element.createVector(this.mRS, Element.DataType.FLOAT_32, this.mVtxSize), BrowserContract.Bookmarks.POSITION);
            if ((this.mFlags & 1) != 0) {
                b.add(Element.F32_4(this.mRS), "color");
            }
            if ((this.mFlags & 256) != 0) {
                b.add(Element.F32_2(this.mRS), "texture0");
            }
            if ((this.mFlags & 2) != 0) {
                b.add(Element.F32_3(this.mRS), "normal");
            }
            this.mElement = b.create();
            int usage = 1;
            if (uploadToBufferObject) {
                usage = 1 | 4;
            }
            Builder smb = new Builder(this.mRS, usage);
            smb.addVertexType(this.mElement, this.mMaxIndex);
            smb.addIndexSetType(Element.U16(this.mRS), this.mIndexCount, Primitive.TRIANGLE);
            Mesh sm = smb.create();
            sm.getVertexAllocation(0).copy1DRangeFromUnchecked(0, this.mMaxIndex, this.mVtxData);
            if (uploadToBufferObject) {
                sm.getVertexAllocation(0).syncAll(1);
            }
            sm.getIndexSetAllocation(0).copy1DRangeFromUnchecked(0, this.mIndexCount, this.mIndexData);
            if (uploadToBufferObject) {
                sm.getIndexSetAllocation(0).syncAll(1);
            }
            return sm;
        }
    }
}
