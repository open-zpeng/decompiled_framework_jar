package com.android.internal.telephony.uicc.asn1;

import com.android.internal.telephony.uicc.IccUtils;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: classes3.dex */
public final class Asn1Node {
    private static final int INT_BYTES = 4;
    private final List<Asn1Node> mChildren;
    private final boolean mConstructed;
    private byte[] mDataBytes;
    private int mDataLength;
    private int mDataOffset;
    private int mEncodedLength;
    private final int mTag;
    private static final List<Asn1Node> EMPTY_NODE_LIST = Collections.emptyList();
    private static final byte[] TRUE_BYTES = {-1};
    private static final byte[] FALSE_BYTES = {0};

    /* loaded from: classes3.dex */
    public static final class Builder {
        private final List<Asn1Node> mChildren;
        private final int mTag;

        private Builder(int tag) {
            if (!Asn1Node.isConstructedTag(tag)) {
                throw new IllegalArgumentException("Builder should be created for a constructed tag: " + tag);
            }
            this.mTag = tag;
            this.mChildren = new ArrayList();
        }

        public Builder addChild(Asn1Node child) {
            this.mChildren.add(child);
            return this;
        }

        public Builder addChild(Builder child) {
            this.mChildren.add(child.build());
            return this;
        }

        public Builder addChildren(byte[] encodedBytes) throws InvalidAsn1DataException {
            Asn1Decoder subDecoder = new Asn1Decoder(encodedBytes, 0, encodedBytes.length);
            while (subDecoder.hasNextNode()) {
                this.mChildren.add(subDecoder.nextNode());
            }
            return this;
        }

        public Builder addChildAsInteger(int tag, int value) {
            if (Asn1Node.isConstructedTag(tag)) {
                throw new IllegalStateException("Cannot set value of a constructed tag: " + tag);
            }
            byte[] dataBytes = IccUtils.signedIntToBytes(value);
            addChild(new Asn1Node(tag, dataBytes, 0, dataBytes.length));
            return this;
        }

        public Builder addChildAsString(int tag, String value) {
            if (Asn1Node.isConstructedTag(tag)) {
                throw new IllegalStateException("Cannot set value of a constructed tag: " + tag);
            }
            byte[] dataBytes = value.getBytes(StandardCharsets.UTF_8);
            addChild(new Asn1Node(tag, dataBytes, 0, dataBytes.length));
            return this;
        }

        public Builder addChildAsBytes(int tag, byte[] value) {
            if (Asn1Node.isConstructedTag(tag)) {
                throw new IllegalStateException("Cannot set value of a constructed tag: " + tag);
            }
            addChild(new Asn1Node(tag, value, 0, value.length));
            return this;
        }

        public Builder addChildAsBytesFromHex(int tag, String hex) {
            return addChildAsBytes(tag, IccUtils.hexStringToBytes(hex));
        }

        public Builder addChildAsBits(int tag, int value) {
            if (Asn1Node.isConstructedTag(tag)) {
                throw new IllegalStateException("Cannot set value of a constructed tag: " + tag);
            }
            byte[] dataBytes = new byte[5];
            int value2 = Integer.reverse(value);
            int dataLength = 0;
            for (int dataLength2 = 1; dataLength2 < dataBytes.length; dataLength2++) {
                dataBytes[dataLength2] = (byte) (value2 >> ((4 - dataLength2) * 8));
                if (dataBytes[dataLength2] != 0) {
                    dataLength = dataLength2;
                }
            }
            int dataLength3 = dataLength + 1;
            dataBytes[0] = IccUtils.countTrailingZeros(dataBytes[dataLength3 - 1]);
            addChild(new Asn1Node(tag, dataBytes, 0, dataLength3));
            return this;
        }

        public Builder addChildAsBoolean(int tag, boolean value) {
            if (Asn1Node.isConstructedTag(tag)) {
                throw new IllegalStateException("Cannot set value of a constructed tag: " + tag);
            }
            addChild(new Asn1Node(tag, value ? Asn1Node.TRUE_BYTES : Asn1Node.FALSE_BYTES, 0, 1));
            return this;
        }

        public Asn1Node build() {
            return new Asn1Node(this.mTag, this.mChildren);
        }
    }

    public static Builder newBuilder(int tag) {
        return new Builder(tag);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isConstructedTag(int tag) {
        byte[] tagBytes = IccUtils.unsignedIntToBytes(tag);
        return (tagBytes[0] & 32) != 0;
    }

    private static int calculateEncodedBytesNumForLength(int length) {
        if (length > 127) {
            int len = 1 + IccUtils.byteNumForUnsignedInt(length);
            return len;
        }
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Asn1Node(int tag, byte[] src, int offset, int length) {
        this.mTag = tag;
        this.mConstructed = isConstructedTag(tag);
        this.mDataBytes = src;
        this.mDataOffset = offset;
        this.mDataLength = length;
        this.mChildren = this.mConstructed ? new ArrayList<>() : EMPTY_NODE_LIST;
        this.mEncodedLength = IccUtils.byteNumForUnsignedInt(this.mTag) + calculateEncodedBytesNumForLength(this.mDataLength) + this.mDataLength;
    }

    private Asn1Node(int tag, List<Asn1Node> children) {
        this.mTag = tag;
        this.mConstructed = true;
        this.mChildren = children;
        this.mDataLength = 0;
        int size = children.size();
        for (int i = 0; i < size; i++) {
            this.mDataLength += children.get(i).mEncodedLength;
        }
        int i2 = this.mTag;
        this.mEncodedLength = IccUtils.byteNumForUnsignedInt(i2) + calculateEncodedBytesNumForLength(this.mDataLength) + this.mDataLength;
    }

    public int getTag() {
        return this.mTag;
    }

    public boolean isConstructed() {
        return this.mConstructed;
    }

    public boolean hasChild(int tag, int... tags) throws InvalidAsn1DataException {
        try {
            getChild(tag, tags);
            return true;
        } catch (TagNotFoundException e) {
            return false;
        }
    }

    public Asn1Node getChild(int tag, int... tags) throws TagNotFoundException, InvalidAsn1DataException {
        if (!this.mConstructed) {
            throw new TagNotFoundException(tag);
        }
        int index = 0;
        int tag2 = tag;
        Asn1Node node = this;
        while (node != null) {
            List<Asn1Node> children = node.getChildren();
            int size = children.size();
            Asn1Node foundChild = null;
            int i = 0;
            while (true) {
                if (i >= size) {
                    break;
                }
                Asn1Node child = children.get(i);
                if (child.getTag() != tag2) {
                    i++;
                } else {
                    foundChild = child;
                    break;
                }
            }
            node = foundChild;
            if (index >= tags.length) {
                break;
            }
            tag2 = tags[index];
            index++;
        }
        if (node == null) {
            throw new TagNotFoundException(tag2);
        }
        return node;
    }

    public List<Asn1Node> getChildren(int tag) throws TagNotFoundException, InvalidAsn1DataException {
        if (!this.mConstructed) {
            return EMPTY_NODE_LIST;
        }
        List<Asn1Node> children = getChildren();
        if (children.isEmpty()) {
            return EMPTY_NODE_LIST;
        }
        List<Asn1Node> output = new ArrayList<>();
        int size = children.size();
        for (int i = 0; i < size; i++) {
            Asn1Node child = children.get(i);
            if (child.getTag() == tag) {
                output.add(child);
            }
        }
        return output.isEmpty() ? EMPTY_NODE_LIST : output;
    }

    public List<Asn1Node> getChildren() throws InvalidAsn1DataException {
        if (!this.mConstructed) {
            return EMPTY_NODE_LIST;
        }
        if (this.mDataBytes != null) {
            Asn1Decoder subDecoder = new Asn1Decoder(this.mDataBytes, this.mDataOffset, this.mDataLength);
            while (subDecoder.hasNextNode()) {
                this.mChildren.add(subDecoder.nextNode());
            }
            this.mDataBytes = null;
            this.mDataOffset = 0;
        }
        return this.mChildren;
    }

    public boolean hasValue() {
        return (this.mConstructed || this.mDataBytes == null) ? false : true;
    }

    public int asInteger() throws InvalidAsn1DataException {
        if (this.mConstructed) {
            throw new IllegalStateException("Cannot get value of a constructed node.");
        }
        if (this.mDataBytes == null) {
            throw new InvalidAsn1DataException(this.mTag, "Data bytes cannot be null.");
        }
        try {
            return IccUtils.bytesToInt(this.mDataBytes, this.mDataOffset, this.mDataLength);
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            throw new InvalidAsn1DataException(this.mTag, "Cannot parse data bytes.", e);
        }
    }

    public long asRawLong() throws InvalidAsn1DataException {
        if (this.mConstructed) {
            throw new IllegalStateException("Cannot get value of a constructed node.");
        }
        if (this.mDataBytes == null) {
            throw new InvalidAsn1DataException(this.mTag, "Data bytes cannot be null.");
        }
        try {
            return IccUtils.bytesToRawLong(this.mDataBytes, this.mDataOffset, this.mDataLength);
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            throw new InvalidAsn1DataException(this.mTag, "Cannot parse data bytes.", e);
        }
    }

    public String asString() throws InvalidAsn1DataException {
        if (this.mConstructed) {
            throw new IllegalStateException("Cannot get value of a constructed node.");
        }
        if (this.mDataBytes == null) {
            throw new InvalidAsn1DataException(this.mTag, "Data bytes cannot be null.");
        }
        try {
            return new String(this.mDataBytes, this.mDataOffset, this.mDataLength, StandardCharsets.UTF_8);
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidAsn1DataException(this.mTag, "Cannot parse data bytes.", e);
        }
    }

    public byte[] asBytes() throws InvalidAsn1DataException {
        if (this.mConstructed) {
            throw new IllegalStateException("Cannot get value of a constructed node.");
        }
        if (this.mDataBytes == null) {
            throw new InvalidAsn1DataException(this.mTag, "Data bytes cannot be null.");
        }
        byte[] output = new byte[this.mDataLength];
        try {
            System.arraycopy(this.mDataBytes, this.mDataOffset, output, 0, this.mDataLength);
            return output;
        } catch (IndexOutOfBoundsException e) {
            throw new InvalidAsn1DataException(this.mTag, "Cannot parse data bytes.", e);
        }
    }

    public int asBits() throws InvalidAsn1DataException {
        if (this.mConstructed) {
            throw new IllegalStateException("Cannot get value of a constructed node.");
        }
        if (this.mDataBytes == null) {
            throw new InvalidAsn1DataException(this.mTag, "Data bytes cannot be null.");
        }
        try {
            int bits = IccUtils.bytesToInt(this.mDataBytes, this.mDataOffset + 1, this.mDataLength - 1);
            for (int i = this.mDataLength - 1; i < 4; i++) {
                bits <<= 8;
            }
            int i2 = Integer.reverse(bits);
            return i2;
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            throw new InvalidAsn1DataException(this.mTag, "Cannot parse data bytes.", e);
        }
    }

    public boolean asBoolean() throws InvalidAsn1DataException {
        if (this.mConstructed) {
            throw new IllegalStateException("Cannot get value of a constructed node.");
        }
        if (this.mDataBytes == null) {
            throw new InvalidAsn1DataException(this.mTag, "Data bytes cannot be null.");
        }
        if (this.mDataLength != 1) {
            int i = this.mTag;
            throw new InvalidAsn1DataException(i, "Cannot parse data bytes as boolean: length=" + this.mDataLength);
        } else if (this.mDataOffset < 0 || this.mDataOffset >= this.mDataBytes.length) {
            throw new InvalidAsn1DataException(this.mTag, "Cannot parse data bytes.", new ArrayIndexOutOfBoundsException(this.mDataOffset));
        } else {
            if (this.mDataBytes[this.mDataOffset] == -1) {
                return Boolean.TRUE.booleanValue();
            }
            if (this.mDataBytes[this.mDataOffset] == 0) {
                return Boolean.FALSE.booleanValue();
            }
            int i2 = this.mTag;
            throw new InvalidAsn1DataException(i2, "Cannot parse data bytes as boolean: " + ((int) this.mDataBytes[this.mDataOffset]));
        }
    }

    public int getEncodedLength() {
        return this.mEncodedLength;
    }

    public int getDataLength() {
        return this.mDataLength;
    }

    public void writeToBytes(byte[] dest, int offset) {
        if (offset < 0 || this.mEncodedLength + offset > dest.length) {
            throw new IndexOutOfBoundsException("Not enough space to write. Required bytes: " + this.mEncodedLength);
        }
        write(dest, offset);
    }

    public byte[] toBytes() {
        byte[] dest = new byte[this.mEncodedLength];
        write(dest, 0);
        return dest;
    }

    public String toHex() {
        return IccUtils.bytesToHexString(toBytes());
    }

    public String getHeadAsHex() {
        String headHex = IccUtils.bytesToHexString(IccUtils.unsignedIntToBytes(this.mTag));
        if (this.mDataLength <= 127) {
            return headHex + IccUtils.byteToHex((byte) this.mDataLength);
        }
        byte[] lenBytes = IccUtils.unsignedIntToBytes(this.mDataLength);
        return (headHex + IccUtils.byteToHex((byte) (lenBytes.length | 128))) + IccUtils.bytesToHexString(lenBytes);
    }

    private int write(byte[] dest, int offset) {
        int lenLen;
        int offset2 = offset + IccUtils.unsignedIntToBytes(this.mTag, dest, offset);
        if (this.mDataLength <= 127) {
            lenLen = offset2 + 1;
            dest[offset2] = (byte) this.mDataLength;
        } else {
            int offset3 = offset2 + 1;
            int lenLen2 = IccUtils.unsignedIntToBytes(this.mDataLength, dest, offset3);
            dest[offset3 - 1] = (byte) (lenLen2 | 128);
            lenLen = lenLen2 + offset3;
        }
        if (!this.mConstructed || this.mDataBytes != null) {
            if (this.mDataBytes != null) {
                System.arraycopy(this.mDataBytes, this.mDataOffset, dest, lenLen, this.mDataLength);
                return lenLen + this.mDataLength;
            }
            return lenLen;
        }
        int size = this.mChildren.size();
        for (int i = 0; i < size; i++) {
            Asn1Node child = this.mChildren.get(i);
            lenLen = child.write(dest, lenLen);
        }
        return lenLen;
    }
}
