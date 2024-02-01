package android.net.wifi.p2p.nsd;

import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.telephony.IccCardConstants;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes2.dex */
public class WifiP2pServiceResponse implements Parcelable {
    protected byte[] mData;
    protected WifiP2pDevice mDevice;
    protected int mServiceType;
    protected int mStatus;
    protected int mTransId;
    private static int MAX_BUF_SIZE = 1024;
    public static final Parcelable.Creator<WifiP2pServiceResponse> CREATOR = new Parcelable.Creator<WifiP2pServiceResponse>() { // from class: android.net.wifi.p2p.nsd.WifiP2pServiceResponse.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiP2pServiceResponse createFromParcel(Parcel in) {
            int type = in.readInt();
            int status = in.readInt();
            int transId = in.readInt();
            WifiP2pDevice dev = (WifiP2pDevice) in.readParcelable(null);
            int len = in.readInt();
            byte[] data = null;
            if (len > 0) {
                data = new byte[len];
                in.readByteArray(data);
            }
            byte[] data2 = data;
            if (type == 1) {
                return WifiP2pDnsSdServiceResponse.newInstance(status, transId, dev, data2);
            }
            if (type == 2) {
                return WifiP2pUpnpServiceResponse.newInstance(status, transId, dev, data2);
            }
            return new WifiP2pServiceResponse(type, status, transId, dev, data2);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiP2pServiceResponse[] newArray(int size) {
            return new WifiP2pServiceResponse[size];
        }
    };

    /* loaded from: classes2.dex */
    public static class Status {
        public static final int BAD_REQUEST = 3;
        public static final int REQUESTED_INFORMATION_NOT_AVAILABLE = 2;
        public static final int SERVICE_PROTOCOL_NOT_AVAILABLE = 1;
        public static final int SUCCESS = 0;

        public static synchronized String toString(int status) {
            switch (status) {
                case 0:
                    return "SUCCESS";
                case 1:
                    return "SERVICE_PROTOCOL_NOT_AVAILABLE";
                case 2:
                    return "REQUESTED_INFORMATION_NOT_AVAILABLE";
                case 3:
                    return "BAD_REQUEST";
                default:
                    return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
            }
        }

        private synchronized Status() {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized WifiP2pServiceResponse(int serviceType, int status, int transId, WifiP2pDevice device, byte[] data) {
        this.mServiceType = serviceType;
        this.mStatus = status;
        this.mTransId = transId;
        this.mDevice = device;
        this.mData = data;
    }

    public synchronized int getServiceType() {
        return this.mServiceType;
    }

    public synchronized int getStatus() {
        return this.mStatus;
    }

    public synchronized int getTransactionId() {
        return this.mTransId;
    }

    public synchronized byte[] getRawData() {
        return this.mData;
    }

    public synchronized WifiP2pDevice getSrcDevice() {
        return this.mDevice;
    }

    public synchronized void setSrcDevice(WifiP2pDevice dev) {
        if (dev == null) {
            return;
        }
        this.mDevice = dev;
    }

    public static synchronized List<WifiP2pServiceResponse> newInstance(String srcAddr, byte[] tlvsBin) {
        WifiP2pServiceResponse resp;
        List<WifiP2pServiceResponse> respList = new ArrayList<>();
        WifiP2pDevice dev = new WifiP2pDevice();
        dev.deviceAddress = srcAddr;
        List<WifiP2pServiceResponse> list = null;
        if (tlvsBin == null) {
            return null;
        }
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(tlvsBin));
        while (true) {
            DataInputStream dis2 = dis;
            try {
                if (dis2.available() <= 0) {
                    return respList;
                }
                int length = (dis2.readUnsignedByte() + (dis2.readUnsignedByte() << 8)) - 3;
                int type = dis2.readUnsignedByte();
                int transId = dis2.readUnsignedByte();
                int status = dis2.readUnsignedByte();
                if (length < 0) {
                    return list;
                }
                if (length == 0) {
                    if (status == 0) {
                        respList.add(new WifiP2pServiceResponse(type, status, transId, dev, null));
                    }
                } else if (length > MAX_BUF_SIZE) {
                    dis2.skip(length);
                } else {
                    byte[] data = new byte[length];
                    dis2.readFully(data);
                    if (type == 1) {
                        resp = WifiP2pDnsSdServiceResponse.newInstance(status, transId, dev, data);
                    } else if (type == 2) {
                        resp = WifiP2pUpnpServiceResponse.newInstance(status, transId, dev, data);
                    } else {
                        resp = new WifiP2pServiceResponse(type, status, transId, dev, data);
                        if (resp != null && resp.getStatus() == 0) {
                            respList.add(resp);
                        }
                    }
                    if (resp != null) {
                        respList.add(resp);
                    }
                }
                dis = dis2;
                list = null;
            } catch (IOException e) {
                e.printStackTrace();
                if (respList.size() > 0) {
                    return respList;
                }
                return null;
            }
        }
    }

    private static synchronized byte[] hexStr2Bin(String hex) {
        int sz = hex.length() / 2;
        byte[] b = new byte[hex.length() / 2];
        for (int i = 0; i < sz; i++) {
            try {
                b[i] = (byte) Integer.parseInt(hex.substring(i * 2, (i * 2) + 2), 16);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return b;
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("serviceType:");
        sbuf.append(this.mServiceType);
        sbuf.append(" status:");
        sbuf.append(Status.toString(this.mStatus));
        sbuf.append(" srcAddr:");
        sbuf.append(this.mDevice.deviceAddress);
        sbuf.append(" data:");
        sbuf.append(Arrays.toString(this.mData));
        return sbuf.toString();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof WifiP2pServiceResponse) {
            WifiP2pServiceResponse req = (WifiP2pServiceResponse) o;
            return req.mServiceType == this.mServiceType && req.mStatus == this.mStatus && equals(req.mDevice.deviceAddress, this.mDevice.deviceAddress) && Arrays.equals(req.mData, this.mData);
        }
        return false;
    }

    private synchronized boolean equals(Object a, Object b) {
        if (a == null && b == null) {
            return true;
        }
        if (a != null) {
            return a.equals(b);
        }
        return false;
    }

    public int hashCode() {
        int result = (31 * 17) + this.mServiceType;
        return (31 * ((31 * ((31 * ((31 * result) + this.mStatus)) + this.mTransId)) + (this.mDevice.deviceAddress == null ? 0 : this.mDevice.deviceAddress.hashCode()))) + (this.mData != null ? Arrays.hashCode(this.mData) : 0);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mServiceType);
        dest.writeInt(this.mStatus);
        dest.writeInt(this.mTransId);
        dest.writeParcelable(this.mDevice, flags);
        if (this.mData == null || this.mData.length == 0) {
            dest.writeInt(0);
            return;
        }
        dest.writeInt(this.mData.length);
        dest.writeByteArray(this.mData);
    }
}
