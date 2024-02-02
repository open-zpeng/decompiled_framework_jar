package android.renderscript;
/* loaded from: classes2.dex */
public class ProgramRaster extends BaseObj {
    CullMode mCullMode;
    boolean mPointSprite;

    /* loaded from: classes2.dex */
    public enum CullMode {
        BACK(0),
        FRONT(1),
        NONE(2);
        
        int mID;

        CullMode(int id) {
            this.mID = id;
        }
    }

    synchronized ProgramRaster(long id, RenderScript rs) {
        super(id, rs);
        this.mPointSprite = false;
        this.mCullMode = CullMode.BACK;
    }

    public synchronized boolean isPointSpriteEnabled() {
        return this.mPointSprite;
    }

    public synchronized CullMode getCullMode() {
        return this.mCullMode;
    }

    public static synchronized ProgramRaster CULL_BACK(RenderScript rs) {
        if (rs.mProgramRaster_CULL_BACK == null) {
            Builder builder = new Builder(rs);
            builder.setCullMode(CullMode.BACK);
            rs.mProgramRaster_CULL_BACK = builder.create();
        }
        return rs.mProgramRaster_CULL_BACK;
    }

    public static synchronized ProgramRaster CULL_FRONT(RenderScript rs) {
        if (rs.mProgramRaster_CULL_FRONT == null) {
            Builder builder = new Builder(rs);
            builder.setCullMode(CullMode.FRONT);
            rs.mProgramRaster_CULL_FRONT = builder.create();
        }
        return rs.mProgramRaster_CULL_FRONT;
    }

    public static synchronized ProgramRaster CULL_NONE(RenderScript rs) {
        if (rs.mProgramRaster_CULL_NONE == null) {
            Builder builder = new Builder(rs);
            builder.setCullMode(CullMode.NONE);
            rs.mProgramRaster_CULL_NONE = builder.create();
        }
        return rs.mProgramRaster_CULL_NONE;
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        RenderScript mRS;
        boolean mPointSprite = false;
        CullMode mCullMode = CullMode.BACK;

        private protected Builder(RenderScript rs) {
            this.mRS = rs;
        }

        private protected Builder setPointSpriteEnabled(boolean enable) {
            this.mPointSprite = enable;
            return this;
        }

        public synchronized Builder setCullMode(CullMode m) {
            this.mCullMode = m;
            return this;
        }

        private protected ProgramRaster create() {
            this.mRS.validate();
            long id = this.mRS.nProgramRasterCreate(this.mPointSprite, this.mCullMode.mID);
            ProgramRaster programRaster = new ProgramRaster(id, this.mRS);
            programRaster.mPointSprite = this.mPointSprite;
            programRaster.mCullMode = this.mCullMode;
            return programRaster;
        }
    }
}
