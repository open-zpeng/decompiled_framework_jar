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
            byte[] data;
            int type = in.readInt();
            int status = in.readInt();
            int transId = in.readInt();
            WifiP2pDevice dev = (WifiP2pDevice) in.readParcelable(null);
            int len = in.readInt();
            if (len <= 0) {
                data = null;
            } else {
                byte[] data2 = new byte[len];
                in.readByteArray(data2);
                data = data2;
            }
            if (type == 1) {
                return WifiP2pDnsSdServiceResponse.newInstance(status, transId, dev, data);
            }
            if (type == 2) {
                return WifiP2pUpnpServiceResponse.newInstance(status, transId, dev, data);
            }
            return new WifiP2pServiceResponse(type, status, transId, dev, data);
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

        public static String toString(int status) {
            if (status != 0) {
                if (status != 1) {
                    if (status != 2) {
                        if (status == 3) {
                            return "BAD_REQUEST";
                        }
                        return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
                    }
                    return "REQUESTED_INFORMATION_NOT_AVAILABLE";
                }
                return "SERVICE_PROTOCOL_NOT_AVAILABLE";
            }
            return "SUCCESS";
        }

        private Status() {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public WifiP2pServiceResponse(int serviceType, int status, int transId, WifiP2pDevice device, byte[] data) {
        this.mServiceType = serviceType;
        this.mStatus = status;
        this.mTransId = transId;
        this.mDevice = device;
        this.mData = data;
    }

    public int getServiceType() {
        return this.mServiceType;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public int getTransactionId() {
        return this.mTransId;
    }

    public byte[] getRawData() {
        return this.mData;
    }

    public WifiP2pDevice getSrcDevice() {
        return this.mDevice;
    }

    public void setSrcDevice(WifiP2pDevice dev) {
        if (dev == null) {
            return;
        }
        this.mDevice = dev;
    }

    public static List<WifiP2pServiceResponse> newInstance(String srcAddr, byte[] tlvsBin) {
        WifiP2pServiceResponse resp;
        List<WifiP2pServiceResponse> respList = new ArrayList<>();
        WifiP2pDevice dev = new WifiP2pDevice();
        dev.deviceAddress = srcAddr;
        List<WifiP2pServiceResponse> list = null;
        if (tlvsBin == null) {
            return null;
        }
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(tlvsBin));
        while (dis.available() > 0) {
            try {
                int length = (dis.readUnsignedByte() + (dis.readUnsignedByte() << 8)) - 3;
                int type = dis.readUnsignedByte();
                int transId = dis.readUnsignedByte();
                int status = dis.readUnsignedByte();
                if (length < 0) {
                    return list;
                }
                if (length == 0) {
                    if (status != 0) {
                        list = null;
                    } else {
                        respList.add(new WifiP2pServiceResponse(type, status, transId, dev, null));
                        list = null;
                    }
                } else if (length > MAX_BUF_SIZE) {
                    dis.skip(length);
                    list = null;
                } else {
                    byte[] data = new byte[length];
                    dis.readFully(data);
                    if (type == 1) {
                        resp = WifiP2pDnsSdServiceResponse.newInstance(status, transId, dev, data);
                    } else if (type == 2) {
                        resp = WifiP2pUpnpServiceResponse.newInstance(status, transId, dev, data);
                    } else {
                        resp = new WifiP2pServiceResponse(type, status, transId, dev, data);
                    }
                    if (resp != null && resp.getStatus() == 0) {
                        respList.add(resp);
                    }
                    list = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (respList.size() > 0) {
                    return respList;
                }
                return null;
            }
        }
        return respList;
    }

    private static byte[] hexStr2Bin(String hex) {
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

    private boolean equals(Object a, Object b) {
        if (a == null && b == null) {
            return true;
        }
        if (a != null) {
            return a.equals(b);
        }
        return false;
    }

    public int hashCode() {
        int result = (17 * 31) + this.mServiceType;
        int result2 = ((((((result * 31) + this.mStatus) * 31) + this.mTransId) * 31) + (this.mDevice.deviceAddress == null ? 0 : this.mDevice.deviceAddress.hashCode())) * 31;
        byte[] bArr = this.mData;
        return result2 + (bArr != null ? Arrays.hashCode(bArr) : 0);
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
        byte[] bArr = this.mData;
        if (bArr == null || bArr.length == 0) {
            dest.writeInt(0);
            return;
        }
        dest.writeInt(bArr.length);
        dest.writeByteArray(this.mData);
    }
}
