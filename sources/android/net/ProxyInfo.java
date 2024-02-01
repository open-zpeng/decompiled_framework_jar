package android.net;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.SmsManager;
import android.text.TextUtils;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class ProxyInfo implements Parcelable {
    public static final Parcelable.Creator<ProxyInfo> CREATOR = new Parcelable.Creator<ProxyInfo>() { // from class: android.net.ProxyInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ProxyInfo createFromParcel(Parcel in) {
            String host = null;
            int port = 0;
            if (in.readByte() != 0) {
                Uri url = Uri.CREATOR.createFromParcel(in);
                int localPort = in.readInt();
                return new ProxyInfo(url, localPort);
            }
            if (in.readByte() != 0) {
                host = in.readString();
                port = in.readInt();
            }
            String exclList = in.readString();
            String[] parsedExclList = in.readStringArray();
            ProxyInfo proxyProperties = new ProxyInfo(host, port, exclList, parsedExclList);
            return proxyProperties;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ProxyInfo[] newArray(int size) {
            return new ProxyInfo[size];
        }
    };
    public static final String LOCAL_EXCL_LIST = "";
    public static final String LOCAL_HOST = "localhost";
    public static final int LOCAL_PORT = -1;
    private final String mExclusionList;
    private final String mHost;
    private final Uri mPacFileUrl;
    private final String[] mParsedExclusionList;
    private final int mPort;

    public static ProxyInfo buildDirectProxy(String host, int port) {
        return new ProxyInfo(host, port, null);
    }

    public static ProxyInfo buildDirectProxy(String host, int port, List<String> exclList) {
        String[] array = (String[]) exclList.toArray(new String[exclList.size()]);
        return new ProxyInfo(host, port, TextUtils.join(SmsManager.REGEX_PREFIX_DELIMITER, array), array);
    }

    public static ProxyInfo buildPacProxy(Uri pacUri) {
        return new ProxyInfo(pacUri);
    }

    @UnsupportedAppUsage
    public ProxyInfo(String host, int port, String exclList) {
        this.mHost = host;
        this.mPort = port;
        this.mExclusionList = exclList;
        this.mParsedExclusionList = parseExclusionList(this.mExclusionList);
        this.mPacFileUrl = Uri.EMPTY;
    }

    public ProxyInfo(Uri pacFileUrl) {
        this.mHost = LOCAL_HOST;
        this.mPort = -1;
        this.mExclusionList = "";
        this.mParsedExclusionList = parseExclusionList(this.mExclusionList);
        if (pacFileUrl == null) {
            throw new NullPointerException();
        }
        this.mPacFileUrl = pacFileUrl;
    }

    public ProxyInfo(String pacFileUrl) {
        this.mHost = LOCAL_HOST;
        this.mPort = -1;
        this.mExclusionList = "";
        this.mParsedExclusionList = parseExclusionList(this.mExclusionList);
        this.mPacFileUrl = Uri.parse(pacFileUrl);
    }

    public ProxyInfo(Uri pacFileUrl, int localProxyPort) {
        this.mHost = LOCAL_HOST;
        this.mPort = localProxyPort;
        this.mExclusionList = "";
        this.mParsedExclusionList = parseExclusionList(this.mExclusionList);
        if (pacFileUrl == null) {
            throw new NullPointerException();
        }
        this.mPacFileUrl = pacFileUrl;
    }

    private static String[] parseExclusionList(String exclusionList) {
        if (exclusionList == null) {
            return new String[0];
        }
        return exclusionList.toLowerCase(Locale.ROOT).split(SmsManager.REGEX_PREFIX_DELIMITER);
    }

    private ProxyInfo(String host, int port, String exclList, String[] parsedExclList) {
        this.mHost = host;
        this.mPort = port;
        this.mExclusionList = exclList;
        this.mParsedExclusionList = parsedExclList;
        this.mPacFileUrl = Uri.EMPTY;
    }

    public ProxyInfo(ProxyInfo source) {
        if (source != null) {
            this.mHost = source.getHost();
            this.mPort = source.getPort();
            this.mPacFileUrl = source.mPacFileUrl;
            this.mExclusionList = source.getExclusionListAsString();
            this.mParsedExclusionList = source.mParsedExclusionList;
            return;
        }
        this.mHost = null;
        this.mPort = 0;
        this.mExclusionList = null;
        this.mParsedExclusionList = null;
        this.mPacFileUrl = Uri.EMPTY;
    }

    public InetSocketAddress getSocketAddress() {
        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(this.mHost, this.mPort);
            return inetSocketAddress;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Uri getPacFileUrl() {
        return this.mPacFileUrl;
    }

    public String getHost() {
        return this.mHost;
    }

    public int getPort() {
        return this.mPort;
    }

    public String[] getExclusionList() {
        return this.mParsedExclusionList;
    }

    public String getExclusionListAsString() {
        return this.mExclusionList;
    }

    public boolean isValid() {
        if (Uri.EMPTY.equals(this.mPacFileUrl)) {
            String str = this.mHost;
            if (str == null) {
                str = "";
            }
            int i = this.mPort;
            String num = i == 0 ? "" : Integer.toString(i);
            String str2 = this.mExclusionList;
            return Proxy.validate(str, num, str2 != null ? str2 : "") == 0;
        }
        return true;
    }

    public java.net.Proxy makeProxy() {
        java.net.Proxy proxy = java.net.Proxy.NO_PROXY;
        String str = this.mHost;
        if (str != null) {
            try {
                InetSocketAddress inetSocketAddress = new InetSocketAddress(str, this.mPort);
                return new java.net.Proxy(Proxy.Type.HTTP, inetSocketAddress);
            } catch (IllegalArgumentException e) {
                return proxy;
            }
        }
        return proxy;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!Uri.EMPTY.equals(this.mPacFileUrl)) {
            sb.append("PAC Script: ");
            sb.append(this.mPacFileUrl);
        }
        if (this.mHost != null) {
            sb.append("[");
            sb.append(this.mHost);
            sb.append("] ");
            sb.append(Integer.toString(this.mPort));
            if (this.mExclusionList != null) {
                sb.append(" xl=");
                sb.append(this.mExclusionList);
            }
        } else {
            sb.append("[ProxyProperties.mHost == null]");
        }
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (o instanceof ProxyInfo) {
            ProxyInfo p = (ProxyInfo) o;
            if (!Uri.EMPTY.equals(this.mPacFileUrl)) {
                return this.mPacFileUrl.equals(p.getPacFileUrl()) && this.mPort == p.mPort;
            } else if (Uri.EMPTY.equals(p.mPacFileUrl)) {
                String str = this.mExclusionList;
                if (str == null || str.equals(p.getExclusionListAsString())) {
                    if (this.mHost == null || p.getHost() == null || this.mHost.equals(p.getHost())) {
                        if (this.mHost == null || p.mHost != null) {
                            return (this.mHost != null || p.mHost == null) && this.mPort == p.mPort;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int hashCode() {
        String str = this.mHost;
        int hashCode = str == null ? 0 : str.hashCode();
        String str2 = this.mExclusionList;
        return hashCode + (str2 != null ? str2.hashCode() : 0) + this.mPort;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        if (!Uri.EMPTY.equals(this.mPacFileUrl)) {
            dest.writeByte((byte) 1);
            this.mPacFileUrl.writeToParcel(dest, 0);
            dest.writeInt(this.mPort);
            return;
        }
        dest.writeByte((byte) 0);
        if (this.mHost != null) {
            dest.writeByte((byte) 1);
            dest.writeString(this.mHost);
            dest.writeInt(this.mPort);
        } else {
            dest.writeByte((byte) 0);
        }
        dest.writeString(this.mExclusionList);
        dest.writeStringArray(this.mParsedExclusionList);
    }
}
