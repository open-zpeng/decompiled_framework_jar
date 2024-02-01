package com.android.internal.telephony.uicc.asn1;

import com.android.internal.telephony.uicc.IccUtils;
/* loaded from: classes3.dex */
public final class Asn1Decoder {
    private final int mEnd;
    private int mPosition;
    private final byte[] mSrc;

    public Asn1Decoder(String hex) {
        this(IccUtils.hexStringToBytes(hex));
    }

    public Asn1Decoder(byte[] src) {
        this(src, 0, src.length);
    }

    public Asn1Decoder(byte[] bytes, int offset, int length) {
        if (offset < 0 || length < 0 || offset + length > bytes.length) {
            throw new IndexOutOfBoundsException("Out of the bounds: bytes=[" + bytes.length + "], offset=" + offset + ", length=" + length);
        }
        this.mSrc = bytes;
        this.mPosition = offset;
        this.mEnd = offset + length;
    }

    public int getPosition() {
        return this.mPosition;
    }

    public boolean hasNextNode() {
        return this.mPosition < this.mEnd;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Asn1Node nextNode() throws InvalidAsn1DataException {
        int dataLen;
        if (this.mPosition >= this.mEnd) {
            throw new IllegalStateException("No bytes to parse.");
        }
        int offset = this.mPosition;
        int offset2 = offset + 1;
        if ((this.mSrc[offset] & 31) == 31) {
            while (true) {
                if (offset2 >= this.mEnd) {
                    break;
                }
                int offset3 = offset2 + 1;
                if ((this.mSrc[offset2] & 128) == 0) {
                    offset2 = offset3;
                    break;
                }
                offset2 = offset3;
            }
        }
        if (offset2 < this.mEnd) {
            try {
                int tag = IccUtils.bytesToInt(this.mSrc, offset, offset2 - offset);
                int offset4 = offset2 + 1;
                int b = this.mSrc[offset2];
                if ((b & 128) == 0) {
                    dataLen = b;
                } else {
                    int dataLen2 = b & 127;
                    if (offset4 + dataLen2 > this.mEnd) {
                        throw new InvalidAsn1DataException(tag, "Cannot parse length at position: " + offset4);
                    }
                    try {
                        int dataLen3 = IccUtils.bytesToInt(this.mSrc, offset4, dataLen2);
                        offset4 += dataLen2;
                        dataLen = dataLen3;
                    } catch (IllegalArgumentException e) {
                        throw new InvalidAsn1DataException(tag, "Cannot parse length at position: " + offset4, e);
                    }
                }
                if (offset4 + dataLen > this.mEnd) {
                    throw new InvalidAsn1DataException(tag, "Incomplete data at position: " + offset4 + ", expected bytes: " + dataLen + ", actual bytes: " + (this.mEnd - offset4));
                }
                Asn1Node root = new Asn1Node(tag, this.mSrc, offset4, dataLen);
                this.mPosition = offset4 + dataLen;
                return root;
            } catch (IllegalArgumentException e2) {
                throw new InvalidAsn1DataException(0, "Cannot parse tag at position: " + offset, e2);
            }
        }
        throw new InvalidAsn1DataException(0, "Invalid length at position: " + offset2);
    }
}
