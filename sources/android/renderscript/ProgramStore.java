package android.renderscript;
/* loaded from: classes2.dex */
public class ProgramStore extends BaseObj {
    BlendDstFunc mBlendDst;
    BlendSrcFunc mBlendSrc;
    boolean mColorMaskA;
    boolean mColorMaskB;
    boolean mColorMaskG;
    boolean mColorMaskR;
    DepthFunc mDepthFunc;
    boolean mDepthMask;
    boolean mDither;

    /* loaded from: classes2.dex */
    public enum DepthFunc {
        ALWAYS(0),
        LESS(1),
        LESS_OR_EQUAL(2),
        GREATER(3),
        GREATER_OR_EQUAL(4),
        EQUAL(5),
        NOT_EQUAL(6);
        
        int mID;

        DepthFunc(int id) {
            this.mID = id;
        }
    }

    /* loaded from: classes2.dex */
    public enum BlendSrcFunc {
        ZERO(0),
        ONE(1),
        DST_COLOR(2),
        ONE_MINUS_DST_COLOR(3),
        SRC_ALPHA(4),
        ONE_MINUS_SRC_ALPHA(5),
        DST_ALPHA(6),
        ONE_MINUS_DST_ALPHA(7),
        SRC_ALPHA_SATURATE(8);
        
        int mID;

        BlendSrcFunc(int id) {
            this.mID = id;
        }
    }

    /* loaded from: classes2.dex */
    public enum BlendDstFunc {
        ZERO(0),
        ONE(1),
        SRC_COLOR(2),
        ONE_MINUS_SRC_COLOR(3),
        SRC_ALPHA(4),
        ONE_MINUS_SRC_ALPHA(5),
        DST_ALPHA(6),
        ONE_MINUS_DST_ALPHA(7);
        
        int mID;

        BlendDstFunc(int id) {
            this.mID = id;
        }
    }

    synchronized ProgramStore(long id, RenderScript rs) {
        super(id, rs);
    }

    public synchronized DepthFunc getDepthFunc() {
        return this.mDepthFunc;
    }

    public synchronized boolean isDepthMaskEnabled() {
        return this.mDepthMask;
    }

    public synchronized boolean isColorMaskRedEnabled() {
        return this.mColorMaskR;
    }

    public synchronized boolean isColorMaskGreenEnabled() {
        return this.mColorMaskG;
    }

    public synchronized boolean isColorMaskBlueEnabled() {
        return this.mColorMaskB;
    }

    public synchronized boolean isColorMaskAlphaEnabled() {
        return this.mColorMaskA;
    }

    public synchronized BlendSrcFunc getBlendSrcFunc() {
        return this.mBlendSrc;
    }

    public synchronized BlendDstFunc getBlendDstFunc() {
        return this.mBlendDst;
    }

    public synchronized boolean isDitherEnabled() {
        return this.mDither;
    }

    public static synchronized ProgramStore BLEND_NONE_DEPTH_TEST(RenderScript rs) {
        if (rs.mProgramStore_BLEND_NONE_DEPTH_TEST == null) {
            Builder builder = new Builder(rs);
            builder.setDepthFunc(DepthFunc.LESS);
            builder.setBlendFunc(BlendSrcFunc.ONE, BlendDstFunc.ZERO);
            builder.setDitherEnabled(false);
            builder.setDepthMaskEnabled(true);
            rs.mProgramStore_BLEND_NONE_DEPTH_TEST = builder.create();
        }
        return rs.mProgramStore_BLEND_NONE_DEPTH_TEST;
    }

    public static synchronized ProgramStore BLEND_NONE_DEPTH_NONE(RenderScript rs) {
        if (rs.mProgramStore_BLEND_NONE_DEPTH_NO_DEPTH == null) {
            Builder builder = new Builder(rs);
            builder.setDepthFunc(DepthFunc.ALWAYS);
            builder.setBlendFunc(BlendSrcFunc.ONE, BlendDstFunc.ZERO);
            builder.setDitherEnabled(false);
            builder.setDepthMaskEnabled(false);
            rs.mProgramStore_BLEND_NONE_DEPTH_NO_DEPTH = builder.create();
        }
        return rs.mProgramStore_BLEND_NONE_DEPTH_NO_DEPTH;
    }

    public static synchronized ProgramStore BLEND_ALPHA_DEPTH_TEST(RenderScript rs) {
        if (rs.mProgramStore_BLEND_ALPHA_DEPTH_TEST == null) {
            Builder builder = new Builder(rs);
            builder.setDepthFunc(DepthFunc.LESS);
            builder.setBlendFunc(BlendSrcFunc.SRC_ALPHA, BlendDstFunc.ONE_MINUS_SRC_ALPHA);
            builder.setDitherEnabled(false);
            builder.setDepthMaskEnabled(true);
            rs.mProgramStore_BLEND_ALPHA_DEPTH_TEST = builder.create();
        }
        return rs.mProgramStore_BLEND_ALPHA_DEPTH_TEST;
    }

    private protected static ProgramStore BLEND_ALPHA_DEPTH_NONE(RenderScript rs) {
        if (rs.mProgramStore_BLEND_ALPHA_DEPTH_NO_DEPTH == null) {
            Builder builder = new Builder(rs);
            builder.setDepthFunc(DepthFunc.ALWAYS);
            builder.setBlendFunc(BlendSrcFunc.SRC_ALPHA, BlendDstFunc.ONE_MINUS_SRC_ALPHA);
            builder.setDitherEnabled(false);
            builder.setDepthMaskEnabled(false);
            rs.mProgramStore_BLEND_ALPHA_DEPTH_NO_DEPTH = builder.create();
        }
        return rs.mProgramStore_BLEND_ALPHA_DEPTH_NO_DEPTH;
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        boolean mDither;
        RenderScript mRS;
        DepthFunc mDepthFunc = DepthFunc.ALWAYS;
        boolean mDepthMask = false;
        boolean mColorMaskR = true;
        boolean mColorMaskG = true;
        boolean mColorMaskB = true;
        boolean mColorMaskA = true;
        BlendSrcFunc mBlendSrc = BlendSrcFunc.ONE;
        BlendDstFunc mBlendDst = BlendDstFunc.ZERO;

        private protected Builder(RenderScript rs) {
            this.mRS = rs;
        }

        private protected Builder setDepthFunc(DepthFunc func) {
            this.mDepthFunc = func;
            return this;
        }

        private protected Builder setDepthMaskEnabled(boolean enable) {
            this.mDepthMask = enable;
            return this;
        }

        public synchronized Builder setColorMaskEnabled(boolean r, boolean g, boolean b, boolean a) {
            this.mColorMaskR = r;
            this.mColorMaskG = g;
            this.mColorMaskB = b;
            this.mColorMaskA = a;
            return this;
        }

        private protected Builder setBlendFunc(BlendSrcFunc src, BlendDstFunc dst) {
            this.mBlendSrc = src;
            this.mBlendDst = dst;
            return this;
        }

        private protected Builder setDitherEnabled(boolean enable) {
            this.mDither = enable;
            return this;
        }

        private protected ProgramStore create() {
            this.mRS.validate();
            long id = this.mRS.nProgramStoreCreate(this.mColorMaskR, this.mColorMaskG, this.mColorMaskB, this.mColorMaskA, this.mDepthMask, this.mDither, this.mBlendSrc.mID, this.mBlendDst.mID, this.mDepthFunc.mID);
            ProgramStore programStore = new ProgramStore(id, this.mRS);
            programStore.mDepthFunc = this.mDepthFunc;
            programStore.mDepthMask = this.mDepthMask;
            programStore.mColorMaskR = this.mColorMaskR;
            programStore.mColorMaskG = this.mColorMaskG;
            programStore.mColorMaskB = this.mColorMaskB;
            programStore.mColorMaskA = this.mColorMaskA;
            programStore.mBlendSrc = this.mBlendSrc;
            programStore.mBlendDst = this.mBlendDst;
            programStore.mDither = this.mDither;
            return programStore;
        }
    }
}
