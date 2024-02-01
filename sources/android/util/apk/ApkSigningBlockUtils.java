package android.util.apk;

import android.util.ArrayMap;
import android.util.Pair;
import android.util.apk.VerityBuilder;
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

    private static byte[][] computeContentDigestsPer1MbChunk(int[] digestAlgorithms, DataSource[] contents) throws DigestException {
        long totalChunkCountLong;
        int[] iArr = digestAlgorithms;
        DataSource[] dataSourceArr = contents;
        int i = 0;
        long totalChunkCountLong2 = 0;
        for (DataSource input : dataSourceArr) {
            totalChunkCountLong2 += getChunkCount(input.size());
        }
        if (totalChunkCountLong2 >= 2097151) {
            throw new DigestException("Too many chunks: " + totalChunkCountLong2);
        }
        int totalChunkCount = (int) totalChunkCountLong2;
        byte[][] digestsOfChunks = new byte[iArr.length];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            int digestAlgorithm = iArr[i2];
            int digestOutputSizeBytes = getContentDigestAlgorithmOutputSizeBytes(digestAlgorithm);
            byte[] concatenationOfChunkCountAndChunkDigests = new byte[(totalChunkCount * digestOutputSizeBytes) + 5];
            concatenationOfChunkCountAndChunkDigests[0] = 90;
            setUnsignedInt32LittleEndian(totalChunkCount, concatenationOfChunkCountAndChunkDigests, 1);
            digestsOfChunks[i2] = concatenationOfChunkCountAndChunkDigests;
        }
        byte[] chunkContentPrefix = new byte[5];
        chunkContentPrefix[0] = -91;
        int chunkIndex = 0;
        MessageDigest[] mds = new MessageDigest[iArr.length];
        for (int i3 = 0; i3 < iArr.length; i3++) {
            String jcaAlgorithmName = getContentDigestAlgorithmJcaDigestAlgorithm(iArr[i3]);
            try {
                mds[i3] = MessageDigest.getInstance(jcaAlgorithmName);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(jcaAlgorithmName + " digest not supported", e);
            }
        }
        DataDigester digester = new MultipleDigestDataDigester(mds);
        int chunkIndex2 = dataSourceArr.length;
        int dataSourceIndex = 0;
        while (i < chunkIndex2) {
            DataSource input2 = dataSourceArr[i];
            int totalChunkCount2 = totalChunkCount;
            int i4 = chunkIndex2;
            long inputOffset = 0;
            int chunkIndex3 = chunkIndex;
            long inputRemaining = input2.size();
            while (inputRemaining > 0) {
                int chunkSize = (int) Math.min(inputRemaining, 1048576L);
                setUnsignedInt32LittleEndian(chunkSize, chunkContentPrefix, 1);
                int i5 = 0;
                while (true) {
                    totalChunkCountLong = totalChunkCountLong2;
                    if (i5 < mds.length) {
                        mds[i5].update(chunkContentPrefix);
                        i5++;
                        totalChunkCountLong2 = totalChunkCountLong;
                    } else {
                        try {
                            break;
                        } catch (IOException e2) {
                            throw new DigestException("Failed to digest chunk #" + chunkIndex3 + " of section #" + dataSourceIndex, e2);
                        }
                    }
                }
                input2.feedIntoDataDigester(digester, inputOffset, chunkSize);
                int i6 = 0;
                while (i6 < digestAlgorithms.length) {
                    int digestAlgorithm2 = digestAlgorithms[i6];
                    byte[] chunkContentPrefix2 = chunkContentPrefix;
                    byte[] chunkContentPrefix3 = digestsOfChunks[i6];
                    DataSource input3 = input2;
                    int expectedDigestSizeBytes = getContentDigestAlgorithmOutputSizeBytes(digestAlgorithm2);
                    MessageDigest md = mds[i6];
                    MessageDigest[] mds2 = mds;
                    int actualDigestSizeBytes = md.digest(chunkContentPrefix3, (chunkIndex3 * expectedDigestSizeBytes) + 5, expectedDigestSizeBytes);
                    if (actualDigestSizeBytes == expectedDigestSizeBytes) {
                        i6++;
                        chunkContentPrefix = chunkContentPrefix2;
                        input2 = input3;
                        mds = mds2;
                    } else {
                        throw new RuntimeException("Unexpected output size of " + md.getAlgorithm() + " digest: " + actualDigestSizeBytes);
                    }
                }
                inputOffset += chunkSize;
                inputRemaining -= chunkSize;
                chunkIndex3++;
                iArr = digestAlgorithms;
                chunkContentPrefix = chunkContentPrefix;
                totalChunkCountLong2 = totalChunkCountLong;
            }
            dataSourceIndex++;
            i++;
            dataSourceArr = contents;
            chunkIndex = chunkIndex3;
            totalChunkCount = totalChunkCount2;
            chunkIndex2 = i4;
            chunkContentPrefix = chunkContentPrefix;
            totalChunkCountLong2 = totalChunkCountLong2;
        }
        int[] iArr2 = iArr;
        byte[][] result = new byte[iArr2.length];
        for (int i7 = 0; i7 < iArr2.length; i7++) {
            int digestAlgorithm3 = iArr2[i7];
            byte[] input4 = digestsOfChunks[i7];
            String jcaAlgorithmName2 = getContentDigestAlgorithmJcaDigestAlgorithm(digestAlgorithm3);
            try {
                byte[] output = MessageDigest.getInstance(jcaAlgorithmName2).digest(input4);
                result[i7] = output;
            } catch (NoSuchAlgorithmException e3) {
                throw new RuntimeException(jcaAlgorithmName2 + " digest not supported", e3);
            }
        }
        return result;
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
            VerityBuilder.VerityResult verity = VerityBuilder.generateApkVerityTree(apk, signatureInfo, new ByteBufferFactory() { // from class: android.util.apk.ApkSigningBlockUtils.1
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
        if (digestAlgorithm1 == 1) {
            if (digestAlgorithm2 != 1) {
                if (digestAlgorithm2 == 2 || digestAlgorithm2 == 3) {
                    return -1;
                }
                throw new IllegalArgumentException("Unknown digestAlgorithm2: " + digestAlgorithm2);
            }
            return 0;
        } else if (digestAlgorithm1 == 2) {
            if (digestAlgorithm2 != 1) {
                if (digestAlgorithm2 == 2) {
                    return 0;
                }
                if (digestAlgorithm2 != 3) {
                    throw new IllegalArgumentException("Unknown digestAlgorithm2: " + digestAlgorithm2);
                }
            }
            return 1;
        } else if (digestAlgorithm1 == 3) {
            if (digestAlgorithm2 != 1) {
                if (digestAlgorithm2 != 2) {
                    if (digestAlgorithm2 == 3) {
                        return 0;
                    }
                    throw new IllegalArgumentException("Unknown digestAlgorithm2: " + digestAlgorithm2);
                }
                return -1;
            }
            return 1;
        } else {
            throw new IllegalArgumentException("Unknown digestAlgorithm1: " + digestAlgorithm1);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getSignatureAlgorithmContentDigestAlgorithm(int sigAlgorithm) {
        if (sigAlgorithm != 513) {
            if (sigAlgorithm != 514) {
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
                                throw new IllegalArgumentException("Unknown signature algorithm: 0x" + Long.toHexString(sigAlgorithm & (-1)));
                        }
                    }
                    return 3;
                }
                return 1;
            }
            return 2;
        }
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getContentDigestAlgorithmJcaDigestAlgorithm(int digestAlgorithm) {
        if (digestAlgorithm != 1) {
            if (digestAlgorithm != 2) {
                if (digestAlgorithm == 3) {
                    return "SHA-256";
                }
                throw new IllegalArgumentException("Unknown content digest algorthm: " + digestAlgorithm);
            }
            return "SHA-512";
        }
        return "SHA-256";
    }

    private static int getContentDigestAlgorithmOutputSizeBytes(int digestAlgorithm) {
        if (digestAlgorithm != 1) {
            if (digestAlgorithm != 2) {
                if (digestAlgorithm == 3) {
                    return 32;
                }
                throw new IllegalArgumentException("Unknown content digest algorthm: " + digestAlgorithm);
            }
            return 64;
        }
        return 32;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getSignatureAlgorithmJcaKeyAlgorithm(int sigAlgorithm) {
        if (sigAlgorithm == 513 || sigAlgorithm == 514) {
            return "EC";
        }
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
                                throw new IllegalArgumentException("Unknown signature algorithm: 0x" + Long.toHexString(sigAlgorithm & (-1)));
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
        if (sigAlgorithm != 513) {
            if (sigAlgorithm == 514) {
                return Pair.create("SHA512withECDSA", null);
            }
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
                                    throw new IllegalArgumentException("Unknown signature algorithm: 0x" + Long.toHexString(sigAlgorithm & (-1)));
                            }
                        }
                    }
                }
                return Pair.create("SHA256withRSA", null);
            }
            return Pair.create("SHA256withDSA", null);
        }
        return Pair.create("SHA256withECDSA", null);
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
