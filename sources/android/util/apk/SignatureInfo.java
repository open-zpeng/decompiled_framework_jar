package android.util.apk;

import java.nio.ByteBuffer;
/* loaded from: classes2.dex */
class SignatureInfo {
    public final long apkSigningBlockOffset;
    public final long centralDirOffset;
    public final ByteBuffer eocd;
    public final long eocdOffset;
    public final ByteBuffer signatureBlock;

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized SignatureInfo(ByteBuffer signatureBlock, long apkSigningBlockOffset, long centralDirOffset, long eocdOffset, ByteBuffer eocd) {
        this.signatureBlock = signatureBlock;
        this.apkSigningBlockOffset = apkSigningBlockOffset;
        this.centralDirOffset = centralDirOffset;
        this.eocdOffset = eocdOffset;
        this.eocd = eocd;
    }
}
