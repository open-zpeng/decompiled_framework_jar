package android.util.apk;

import android.app.backup.FullBackup;
import android.util.ArrayMap;
import android.util.Pair;
import android.util.apk.ApkVerityBuilder;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.Arrays;
import java.util.Map;
/* loaded from: classes2.dex */
final class ApkSigningBlockUtils {
    private static final long APK_SIG_BLOCK_MAGIC_HI = 3617552046287187010L;
    private static final long APK_SIG_BLOCK_MAGIC_LO = 2334950737559900225L;
    private static final int APK_SIG_BLOCK_MIN_SIZE = 32;
    private static final int CHUNK_SIZE_BYTES = 1048576;
    static final int CONTENT_DIGEST_CHUNKED_SHA256 = 1;
    static final int CONTENT_DIGEST_CHUNKED_SHA512 = 2;
    static final int CONTENT_DIGEST_VERITY_CHUNKED_SHA256 = 3;
    static final int SIGNATURE_DSA_WITH_SHA256 = 769;
    static final int SIGNATURE_ECDSA_WITH_SHA256 = 513;
    static final int SIGNATURE_ECDSA_WITH_SHA512 = 514;
    static final int SIGNATURE_RSA_PKCS1_V1_5_WITH_SHA256 = 259;
    static final int SIGNATURE_RSA_PKCS1_V1_5_WITH_SHA512 = 260;
    static final int SIGNATURE_RSA_PSS_WITH_SHA256 = 257;
    static final int SIGNATURE_RSA_PSS_WITH_SHA512 = 258;
    static final int SIGNATURE_VERITY_DSA_WITH_SHA256 = 1061;
    static final int SIGNATURE_VERITY_ECDSA_WITH_SHA256 = 1059;
    static final int SIGNATURE_VERITY_RSA_PKCS1_V1_5_WITH_SHA256 = 1057;

    private ApkSigningBlockUtils() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static SignatureInfo findSignature(RandomAccessFile apk, int blockId) throws IOException, SignatureNotFoundException {
        Pair<ByteBuffer, Long> eocdAndOffsetInFile = getEocd(apk);
        ByteBuffer eocd = eocdAndOffsetInFile.first;
        long eocdOffset = eocdAndOffsetInFile.second.longValue();
        if (ZipUtils.isZip64EndOfCentralDirectoryLocatorPresent(apk, eocdOffset)) {
            throw new SignatureNotFoundException("ZIP64 APK not supported");
        }
        long centralDirOffset = getCentralDirOffset(eocd, eocdOffset);
        Pair<ByteBuffer, Long> apkSigningBlockAndOffsetInFile = findApkSigningBlock(apk, centralDirOffset);
        ByteBuffer apkSigningBlock = apkSigningBlockAndOffsetInFile.first;
        long apkSigningBlockOffset = apkSigningBlockAndOffsetInFile.second.longValue();
        ByteBuffer apkSignatureSchemeBlock = findApkSignatureSchemeBlock(apkSigningBlock, blockId);
        return new SignatureInfo(apkSignatureSchemeBlock, apkSigningBlockOffset, centralDirOffset, eocdOffset, eocd);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void verifyIntegrity(Map<Integer, byte[]> expectedDigests, RandomAccessFile apk, SignatureInfo signatureInfo) throws SecurityException {
        if (expectedDigests.isEmpty()) {
            throw new SecurityException("No digests provided");
        }
        boolean neverVerified = true;
        Map<Integer, byte[]> expected1MbChunkDigests = new ArrayMap<>();
        if (expectedDigests.containsKey(1)) {
            expected1MbChunkDigests.put(1, expectedDigests.get(1));
        }
        if (expectedDigests.containsKey(2)) {
            expected1MbChunkDigests.put(2, expectedDigests.get(2));
        }
        if (!expected1MbChunkDigests.isEmpty()) {
            try {
                verifyIntegrityFor1MbChunkBasedAlgorithm(expected1MbChunkDigests, apk.getFD(), signatureInfo);
                neverVerified = false;
            } catch (IOException e) {
                throw new SecurityException("Cannot get FD", e);
            }
        }
        if (expectedDigests.containsKey(3)) {
            verifyIntegrityForVerityBasedAlgorithm(expectedDigests.get(3), apk, signatureInfo);
            neverVerified = false;
        }
        if (neverVerified) {
            throw new SecurityException("No known digest exists for integrity check");
        }
    }

    private static void verifyIntegrityFor1MbChunkBasedAlgorithm(Map<Integer, byte[]> expectedDigests, FileDescriptor apkFileDescriptor, SignatureInfo signatureInfo) throws SecurityException {
        DataSource beforeApkSigningBlock = new MemoryMappedFileDataSource(apkFileDescriptor, 0L, signatureInfo.apkSigningBlockOffset);
        DataSource centralDir = new MemoryMappedFileDataSource(apkFileDescriptor, signatureInfo.centralDirOffset, signatureInfo.eocdOffset - signatureInfo.centralDirOffset);
        ByteBuffer eocdBuf = signatureInfo.eocd.duplicate();
        eocdBuf.order(ByteOrder.LITTLE_ENDIAN);
        ZipUtils.setZipEocdCentralDirectoryOffset(eocdBuf, signatureInfo.apkSigningBlockOffset);
        DataSource eocd = new ByteBufferDataSource(eocdBuf);
        int[] digestAlgorithms = new int[expectedDigests.size()];
        int digestAlgorithmCount = 0;
        for (Integer num : expectedDigests.keySet()) {
            digestAlgorithms[digestAlgorithmCount] = num.intValue();
            digestAlgorithmCount++;
        }
        try {
            byte[][] actualDigests = computeContentDigestsPer1MbChunk(digestAlgorithms, new DataSource[]{beforeApkSigningBlock, centralDir, eocd});
            for (int i = 0; i < digestAlgorithms.length; i++) {
                int digestAlgorithm = digestAlgorithms[i];
                byte[] expectedDigest = expectedDigests.get(Integer.valueOf(digestAlgorithm));
                byte[] actualDigest = actualDigests[i];
                if (!MessageDigest.isEqual(expectedDigest, actualDigest)) {
                    throw new SecurityException(getContentDigestAlgorithmJcaDigestAlgorithm(digestAlgorithm) + " digest of contents did not verify");
                }
            }
        } catch (DigestException e) {
            throw new SecurityException("Failed to compute digest(s) of contents", e);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:43:0x016a, code lost:
        r15 = r15 + 1;
        r0 = r0 + 1;
        r6 = r6;
        r2 = r37;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static byte[][] computeContentDigestsPer1MbChunk(int[] r36, android.util.apk.DataSource[] r37) throws java.security.DigestException {
        /*
            Method dump skipped, instructions count: 491
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.apk.ApkSigningBlockUtils.computeContentDigestsPer1MbChunk(int[], android.util.apk.DataSource[]):byte[][]");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] parseVerityDigestAndVerifySourceLength(byte[] data, long fileSize, SignatureInfo signatureInfo) throws SecurityException {
        if (data.length != 32 + 8) {
            throw new SecurityException("Verity digest size is wrong: " + data.length);
        }
        ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        buffer.position(32);
        long expectedSourceLength = buffer.getLong();
        long signingBlockSize = signatureInfo.centralDirOffset - signatureInfo.apkSigningBlockOffset;
        if (expectedSourceLength == fileSize - signingBlockSize) {
            return Arrays.copyOfRange(data, 0, 32);
        }
        throw new SecurityException("APK content size did not verify");
    }

    private static void verifyIntegrityForVerityBasedAlgorithm(byte[] expectedDigest, RandomAccessFile apk, SignatureInfo signatureInfo) throws SecurityException {
        try {
            byte[] expectedRootHash = parseVerityDigestAndVerifySourceLength(expectedDigest, apk.length(), signatureInfo);
            ApkVerityBuilder.ApkVerityResult verity = ApkVerityBuilder.generateApkVerity(apk, signatureInfo, new ByteBufferFactory() { // from class: android.util.apk.ApkSigningBlockUtils.1
                @Override // android.util.apk.ByteBufferFactory
                public ByteBuffer create(int capacity) {
                    return ByteBuffer.allocate(capacity);
                }
            });
            if (!Arrays.equals(expectedRootHash, verity.rootHash)) {
                throw new SecurityException("APK verity digest of contents did not verify");
            }
        } catch (IOException | DigestException | NoSuchAlgorithmException e) {
            throw new SecurityException("Error during verification", e);
        }
    }

    public static byte[] generateApkVerity(String apkPath, ByteBufferFactory bufferFactory, SignatureInfo signatureInfo) throws IOException, SignatureNotFoundException, SecurityException, DigestException, NoSuchAlgorithmException {
        RandomAccessFile apk = new RandomAccessFile(apkPath, FullBackup.ROOT_TREE_TOKEN);
        try {
            ApkVerityBuilder.ApkVerityResult result = ApkVerityBuilder.generateApkVerity(apk, signatureInfo, bufferFactory);
            byte[] bArr = result.rootHash;
            apk.close();
            return bArr;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                if (th != null) {
                    try {
                        apk.close();
                    } catch (Throwable th3) {
                        th.addSuppressed(th3);
                    }
                } else {
                    apk.close();
                }
                throw th2;
            }
        }
    }

    static Pair<ByteBuffer, Long> getEocd(RandomAccessFile apk) throws IOException, SignatureNotFoundException {
        Pair<ByteBuffer, Long> eocdAndOffsetInFile = ZipUtils.findZipEndOfCentralDirectoryRecord(apk);
        if (eocdAndOffsetInFile == null) {
            throw new SignatureNotFoundException("Not an APK file: ZIP End of Central Directory record not found");
        }
        return eocdAndOffsetInFile;
    }

    static long getCentralDirOffset(ByteBuffer eocd, long eocdOffset) throws SignatureNotFoundException {
        long centralDirOffset = ZipUtils.getZipEocdCentralDirectoryOffset(eocd);
        if (centralDirOffset > eocdOffset) {
            throw new SignatureNotFoundException("ZIP Central Directory offset out of range: " + centralDirOffset + ". ZIP End of Central Directory offset: " + eocdOffset);
        }
        long centralDirSize = ZipUtils.getZipEocdCentralDirectorySizeBytes(eocd);
        if (centralDirOffset + centralDirSize != eocdOffset) {
            throw new SignatureNotFoundException("ZIP Central Directory is not immediately followed by End of Central Directory");
        }
        return centralDirOffset;
    }

    private static long getChunkCount(long inputSizeBytes) {
        return ((inputSizeBytes + 1048576) - 1) / 1048576;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int compareSignatureAlgorithm(int sigAlgorithm1, int sigAlgorithm2) {
        int digestAlgorithm1 = getSignatureAlgorithmContentDigestAlgorithm(sigAlgorithm1);
        int digestAlgorithm2 = getSignatureAlgorithmContentDigestAlgorithm(sigAlgorithm2);
        return compareContentDigestAlgorithm(digestAlgorithm1, digestAlgorithm2);
    }

    private static int compareContentDigestAlgorithm(int digestAlgorithm1, int digestAlgorithm2) {
        switch (digestAlgorithm1) {
            case 1:
                switch (digestAlgorithm2) {
                    case 1:
                        return 0;
                    case 2:
                    case 3:
                        return -1;
                    default:
                        throw new IllegalArgumentException("Unknown digestAlgorithm2: " + digestAlgorithm2);
                }
            case 2:
                switch (digestAlgorithm2) {
                    case 1:
                    case 3:
                        return 1;
                    case 2:
                        return 0;
                    default:
                        throw new IllegalArgumentException("Unknown digestAlgorithm2: " + digestAlgorithm2);
                }
            case 3:
                switch (digestAlgorithm2) {
                    case 1:
                        return 1;
                    case 2:
                        return -1;
                    case 3:
                        return 0;
                    default:
                        throw new IllegalArgumentException("Unknown digestAlgorithm2: " + digestAlgorithm2);
                }
            default:
                throw new IllegalArgumentException("Unknown digestAlgorithm1: " + digestAlgorithm1);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getSignatureAlgorithmContentDigestAlgorithm(int sigAlgorithm) {
        if (sigAlgorithm != 769) {
            if (sigAlgorithm != 1057 && sigAlgorithm != SIGNATURE_VERITY_ECDSA_WITH_SHA256 && sigAlgorithm != 1061) {
                switch (sigAlgorithm) {
                    case 257:
                    case 259:
                        return 1;
                    case 258:
                    case 260:
                        return 2;
                    default:
                        switch (sigAlgorithm) {
                            case 513:
                                return 1;
                            case 514:
                                return 2;
                            default:
                                throw new IllegalArgumentException("Unknown signature algorithm: 0x" + Long.toHexString(sigAlgorithm & (-1)));
                        }
                }
            }
            return 3;
        }
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getContentDigestAlgorithmJcaDigestAlgorithm(int digestAlgorithm) {
        switch (digestAlgorithm) {
            case 1:
            case 3:
                return "SHA-256";
            case 2:
                return "SHA-512";
            default:
                throw new IllegalArgumentException("Unknown content digest algorthm: " + digestAlgorithm);
        }
    }

    private static int getContentDigestAlgorithmOutputSizeBytes(int digestAlgorithm) {
        switch (digestAlgorithm) {
            case 1:
            case 3:
                return 32;
            case 2:
                return 64;
            default:
                throw new IllegalArgumentException("Unknown content digest algorthm: " + digestAlgorithm);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getSignatureAlgorithmJcaKeyAlgorithm(int sigAlgorithm) {
        if (sigAlgorithm != 769) {
            if (sigAlgorithm != 1057) {
                if (sigAlgorithm != SIGNATURE_VERITY_ECDSA_WITH_SHA256) {
                    if (sigAlgorithm != 1061) {
                        switch (sigAlgorithm) {
                            case 257:
                            case 258:
                            case 259:
                            case 260:
                                return "RSA";
                            default:
                                switch (sigAlgorithm) {
                                    case 513:
                                    case 514:
                                        return "EC";
                                    default:
                                        throw new IllegalArgumentException("Unknown signature algorithm: 0x" + Long.toHexString(sigAlgorithm & (-1)));
                                }
                        }
                    }
                    return "DSA";
                }
                return "EC";
            }
            return "RSA";
        }
        return "DSA";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Pair<String, ? extends AlgorithmParameterSpec> getSignatureAlgorithmJcaSignatureAlgorithm(int sigAlgorithm) {
        if (sigAlgorithm != 769) {
            if (sigAlgorithm != 1057) {
                if (sigAlgorithm != SIGNATURE_VERITY_ECDSA_WITH_SHA256) {
                    if (sigAlgorithm != 1061) {
                        switch (sigAlgorithm) {
                            case 257:
                                return Pair.create("SHA256withRSA/PSS", new PSSParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, 32, 1));
                            case 258:
                                return Pair.create("SHA512withRSA/PSS", new PSSParameterSpec("SHA-512", "MGF1", MGF1ParameterSpec.SHA512, 64, 1));
                            case 259:
                                break;
                            case 260:
                                return Pair.create("SHA512withRSA", null);
                            default:
                                switch (sigAlgorithm) {
                                    case 513:
                                        break;
                                    case 514:
                                        return Pair.create("SHA512withECDSA", null);
                                    default:
                                        throw new IllegalArgumentException("Unknown signature algorithm: 0x" + Long.toHexString(sigAlgorithm & (-1)));
                                }
                        }
                    }
                }
                return Pair.create("SHA256withECDSA", null);
            }
            return Pair.create("SHA256withRSA", null);
        }
        return Pair.create("SHA256withDSA", null);
    }

    static ByteBuffer sliceFromTo(ByteBuffer source, int start, int end) {
        if (start < 0) {
            throw new IllegalArgumentException("start: " + start);
        } else if (end < start) {
            throw new IllegalArgumentException("end < start: " + end + " < " + start);
        } else {
            int capacity = source.capacity();
            if (end > source.capacity()) {
                throw new IllegalArgumentException("end > capacity: " + end + " > " + capacity);
            }
            int originalLimit = source.limit();
            int originalPosition = source.position();
            try {
                source.position(0);
                source.limit(end);
                source.position(start);
                ByteBuffer result = source.slice();
                result.order(source.order());
                return result;
            } finally {
                source.position(0);
                source.limit(originalLimit);
                source.position(originalPosition);
            }
        }
    }

    static ByteBuffer getByteBuffer(ByteBuffer source, int size) throws BufferUnderflowException {
        if (size < 0) {
            throw new IllegalArgumentException("size: " + size);
        }
        int originalLimit = source.limit();
        int position = source.position();
        int limit = position + size;
        if (limit < position || limit > originalLimit) {
            throw new BufferUnderflowException();
        }
        source.limit(limit);
        try {
            ByteBuffer result = source.slice();
            result.order(source.order());
            source.position(limit);
            return result;
        } finally {
            source.limit(originalLimit);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ByteBuffer getLengthPrefixedSlice(ByteBuffer source) throws IOException {
        if (source.remaining() < 4) {
            throw new IOException("Remaining buffer too short to contain length of length-prefixed field. Remaining: " + source.remaining());
        }
        int len = source.getInt();
        if (len < 0) {
            throw new IllegalArgumentException("Negative length");
        }
        if (len > source.remaining()) {
            throw new IOException("Length-prefixed field longer than remaining buffer. Field length: " + len + ", remaining: " + source.remaining());
        }
        return getByteBuffer(source, len);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] readLengthPrefixedByteArray(ByteBuffer buf) throws IOException {
        int len = buf.getInt();
        if (len < 0) {
            throw new IOException("Negative length");
        }
        if (len > buf.remaining()) {
            throw new IOException("Underflow while reading length-prefixed value. Length: " + len + ", available: " + buf.remaining());
        }
        byte[] result = new byte[len];
        buf.get(result);
        return result;
    }

    static void setUnsignedInt32LittleEndian(int value, byte[] result, int offset) {
        result[offset] = (byte) (value & 255);
        result[offset + 1] = (byte) ((value >>> 8) & 255);
        result[offset + 2] = (byte) ((value >>> 16) & 255);
        result[offset + 3] = (byte) ((value >>> 24) & 255);
    }

    static Pair<ByteBuffer, Long> findApkSigningBlock(RandomAccessFile apk, long centralDirOffset) throws IOException, SignatureNotFoundException {
        if (centralDirOffset < 32) {
            throw new SignatureNotFoundException("APK too small for APK Signing Block. ZIP Central Directory offset: " + centralDirOffset);
        }
        ByteBuffer footer = ByteBuffer.allocate(24);
        footer.order(ByteOrder.LITTLE_ENDIAN);
        apk.seek(centralDirOffset - footer.capacity());
        apk.readFully(footer.array(), footer.arrayOffset(), footer.capacity());
        if (footer.getLong(8) != APK_SIG_BLOCK_MAGIC_LO || footer.getLong(16) != APK_SIG_BLOCK_MAGIC_HI) {
            throw new SignatureNotFoundException("No APK Signing Block before ZIP Central Directory");
        }
        long apkSigBlockSizeInFooter = footer.getLong(0);
        if (apkSigBlockSizeInFooter < footer.capacity() || apkSigBlockSizeInFooter > 2147483639) {
            throw new SignatureNotFoundException("APK Signing Block size out of range: " + apkSigBlockSizeInFooter);
        }
        int totalSize = (int) (8 + apkSigBlockSizeInFooter);
        long apkSigBlockOffset = centralDirOffset - totalSize;
        if (apkSigBlockOffset < 0) {
            throw new SignatureNotFoundException("APK Signing Block offset out of range: " + apkSigBlockOffset);
        }
        ByteBuffer apkSigBlock = ByteBuffer.allocate(totalSize);
        apkSigBlock.order(ByteOrder.LITTLE_ENDIAN);
        apk.seek(apkSigBlockOffset);
        apk.readFully(apkSigBlock.array(), apkSigBlock.arrayOffset(), apkSigBlock.capacity());
        long apkSigBlockSizeInHeader = apkSigBlock.getLong(0);
        if (apkSigBlockSizeInHeader != apkSigBlockSizeInFooter) {
            throw new SignatureNotFoundException("APK Signing Block sizes in header and footer do not match: " + apkSigBlockSizeInHeader + " vs " + apkSigBlockSizeInFooter);
        }
        return Pair.create(apkSigBlock, Long.valueOf(apkSigBlockOffset));
    }

    static ByteBuffer findApkSignatureSchemeBlock(ByteBuffer apkSigningBlock, int blockId) throws SignatureNotFoundException {
        checkByteOrderLittleEndian(apkSigningBlock);
        ByteBuffer pairs = sliceFromTo(apkSigningBlock, 8, apkSigningBlock.capacity() - 24);
        int entryCount = 0;
        while (pairs.hasRemaining()) {
            entryCount++;
            if (pairs.remaining() < 8) {
                throw new SignatureNotFoundException("Insufficient data to read size of APK Signing Block entry #" + entryCount);
            }
            long lenLong = pairs.getLong();
            if (lenLong < 4 || lenLong > 2147483647L) {
                throw new SignatureNotFoundException("APK Signing Block entry #" + entryCount + " size out of range: " + lenLong);
            }
            int len = (int) lenLong;
            int nextEntryPos = pairs.position() + len;
            if (len > pairs.remaining()) {
                throw new SignatureNotFoundException("APK Signing Block entry #" + entryCount + " size out of range: " + len + ", available: " + pairs.remaining());
            }
            int id = pairs.getInt();
            if (id == blockId) {
                return getByteBuffer(pairs, len - 4);
            }
            pairs.position(nextEntryPos);
        }
        throw new SignatureNotFoundException("No block with ID " + blockId + " in APK Signing Block.");
    }

    private static void checkByteOrderLittleEndian(ByteBuffer buffer) {
        if (buffer.order() != ByteOrder.LITTLE_ENDIAN) {
            throw new IllegalArgumentException("ByteBuffer byte order must be little endian");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class MultipleDigestDataDigester implements DataDigester {
        private final MessageDigest[] mMds;

        MultipleDigestDataDigester(MessageDigest[] mds) {
            this.mMds = mds;
        }

        @Override // android.util.apk.DataDigester
        public void consume(ByteBuffer buffer) {
            MessageDigest[] messageDigestArr;
            ByteBuffer buffer2 = buffer.slice();
            for (MessageDigest md : this.mMds) {
                buffer2.position(0);
                md.update(buffer2);
            }
        }
    }
}
