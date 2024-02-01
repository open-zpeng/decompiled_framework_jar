package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/* loaded from: classes2.dex */
public final class ConnectionInfo implements Parcelable {
    public static final Parcelable.Creator<ConnectionInfo> CREATOR = new Parcelable.Creator<ConnectionInfo>() { // from class: android.net.ConnectionInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ConnectionInfo createFromParcel(Parcel in) {
            int protocol = in.readInt();
            try {
                InetAddress localAddress = InetAddress.getByAddress(in.createByteArray());
                int localPort = in.readInt();
                try {
                    InetAddress remoteAddress = InetAddress.getByAddress(in.createByteArray());
                    int remotePort = in.readInt();
                    InetSocketAddress local = new InetSocketAddress(localAddress, localPort);
                    InetSocketAddress remote = new InetSocketAddress(remoteAddress, remotePort);
                    return new ConnectionInfo(protocol, local, remote);
                } catch (UnknownHostException e) {
                    throw new IllegalArgumentException("Invalid InetAddress");
                }
            } catch (UnknownHostException e2) {
                throw new IllegalArgumentException("Invalid InetAddress");
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ConnectionInfo[] newArray(int size) {
            return new ConnectionInfo[size];
        }
    };
    public final InetSocketAddress local;
    public final int protocol;
    public final InetSocketAddress remote;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public ConnectionInfo(int protocol, InetSocketAddress local, InetSocketAddress remote) {
        this.protocol = protocol;
        this.local = local;
        this.remote = remote;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.protocol);
        out.writeByteArray(this.local.getAddress().getAddress());
        out.writeInt(this.local.getPort());
        out.writeByteArray(this.remote.getAddress().getAddress());
        out.writeInt(this.remote.getPort());
    }
}
