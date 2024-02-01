package android.net.wifi.p2p.nsd;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class WifiP2pServiceInfo implements Parcelable {
    @UnsupportedAppUsage
    public static final Parcelable.Creator<WifiP2pServiceInfo> CREATOR = new Parcelable.Creator<WifiP2pServiceInfo>() { // from class: android.net.wifi.p2p.nsd.WifiP2pServiceInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiP2pServiceInfo createFromParcel(Parcel in) {
            List<String> data = new ArrayList<>();
            in.readStringList(data);
            return new WifiP2pServiceInfo(data);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public WifiP2pServiceInfo[] newArray(int size) {
            return new WifiP2pServiceInfo[size];
        }
    };
    public static final int SERVICE_TYPE_ALL = 0;
    public static final int SERVICE_TYPE_BONJOUR = 1;
    public static final int SERVICE_TYPE_UPNP = 2;
    public static final int SERVICE_TYPE_VENDOR_SPECIFIC = 255;
    public static final int SERVICE_TYPE_WS_DISCOVERY = 3;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private List<String> mQueryList;

    /* JADX INFO: Access modifiers changed from: protected */
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public WifiP2pServiceInfo(List<String> queryList) {
        if (queryList == null) {
            throw new IllegalArgumentException("query list cannot be null");
        }
        this.mQueryList = queryList;
    }

    public List<String> getSupplicantQueryList() {
        return this.mQueryList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String bin2HexStr(byte[] data) {
        StringBuffer sb = new StringBuffer();
        for (byte b : data) {
            try {
                String s = Integer.toHexString(b & 255);
                if (s.length() == 1) {
                    sb.append('0');
                }
                sb.append(s);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WifiP2pServiceInfo)) {
            return false;
        }
        WifiP2pServiceInfo servInfo = (WifiP2pServiceInfo) o;
        return this.mQueryList.equals(servInfo.mQueryList);
    }

    public int hashCode() {
        int i = 17 * 31;
        List<String> list = this.mQueryList;
        int result = i + (list == null ? 0 : list.hashCode());
        return result;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.mQueryList);
    }
}
