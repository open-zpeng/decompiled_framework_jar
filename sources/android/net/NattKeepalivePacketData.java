package android.net;

import android.bluetooth.BluetoothHidDevice;
import android.net.SocketKeepalive;
import android.net.util.IpUtils;
import android.os.Parcel;
import android.os.Parcelable;
import android.system.OsConstants;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* loaded from: classes2.dex */
public final class NattKeepalivePacketData extends KeepalivePacketData implements Parcelable {
    public static final Parcelable.Creator<NattKeepalivePacketData> CREATOR = new Parcelable.Creator<NattKeepalivePacketData>() { // from class: android.net.NattKeepalivePacketData.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NattKeepalivePacketData createFromParcel(Parcel in) {
            InetAddress srcAddress = InetAddresses.parseNumericAddress(in.readString());
            InetAddress dstAddress = InetAddresses.parseNumericAddress(in.readString());
            int srcPort = in.readInt();
            int dstPort = in.readInt();
            try {
                return NattKeepalivePacketData.nattKeepalivePacket(srcAddress, srcPort, dstAddress, dstPort);
            } catch (SocketKeepalive.InvalidPacketException e) {
                throw new IllegalArgumentException("Invalid NAT-T keepalive data: " + e.error);
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NattKeepalivePacketData[] newArray(int size) {
            return new NattKeepalivePacketData[size];
        }
    };

    private NattKeepalivePacketData(InetAddress srcAddress, int srcPort, InetAddress dstAddress, int dstPort, byte[] data) throws SocketKeepalive.InvalidPacketException {
        super(srcAddress, srcPort, dstAddress, dstPort, data);
    }

    public static NattKeepalivePacketData nattKeepalivePacket(InetAddress srcAddress, int srcPort, InetAddress dstAddress, int dstPort) throws SocketKeepalive.InvalidPacketException {
        if (!(srcAddress instanceof Inet4Address) || !(dstAddress instanceof Inet4Address)) {
            throw new SocketKeepalive.InvalidPacketException(-21);
        }
        if (dstPort != 4500) {
            throw new SocketKeepalive.InvalidPacketException(-22);
        }
        ByteBuffer buf = ByteBuffer.allocate(29);
        buf.order(ByteOrder.BIG_ENDIAN);
        buf.putShort((short) 17664);
        buf.putShort((short) 29);
        buf.putInt(0);
        buf.put(BluetoothHidDevice.SUBCLASS1_KEYBOARD);
        buf.put((byte) OsConstants.IPPROTO_UDP);
        int ipChecksumOffset = buf.position();
        buf.putShort((short) 0);
        buf.put(srcAddress.getAddress());
        buf.put(dstAddress.getAddress());
        buf.putShort((short) srcPort);
        buf.putShort((short) dstPort);
        buf.putShort((short) (29 - 20));
        int udpChecksumOffset = buf.position();
        buf.putShort((short) 0);
        buf.put((byte) -1);
        buf.putShort(ipChecksumOffset, IpUtils.ipChecksum(buf, 0));
        buf.putShort(udpChecksumOffset, IpUtils.udpChecksum(buf, 0, 20));
        return new NattKeepalivePacketData(srcAddress, srcPort, dstAddress, dstPort, buf.array());
    }

    @Override // android.net.KeepalivePacketData, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.net.KeepalivePacketData, android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.srcAddress.getHostAddress());
        out.writeString(this.dstAddress.getHostAddress());
        out.writeInt(this.srcPort);
        out.writeInt(this.dstPort);
    }
}
