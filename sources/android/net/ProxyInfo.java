package android.net;

import android.os.Parcel;
import android.os.Parcelable;
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
    private String mExclusionList;
    private String mHost;
    private Uri mPacFileUrl;
    private String[] mParsedExclusionList;
    private int mPort;

    public static ProxyInfo buildDirectProxy(String host, int port) {
        return new ProxyInfo(host, port, null);
    }

    public static ProxyInfo buildDirectProxy(String host, int port, List<String> exclList) {
        String[] array = (String[]) exclList.toArray(new String[exclList.size()]);
        return new ProxyInfo(host, port, TextUtils.join(",", array), array);
    }

    public static ProxyInfo buildPacProxy(Uri pacUri) {
        return new ProxyInfo(pacUri);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ProxyInfo(String host, int port, String exclList) {
        this.mHost = host;
        this.mPort = port;
        setExclusionList(exclList);
        this.mPacFileUrl = Uri.EMPTY;
    }

    public synchronized ProxyInfo(Uri pacFileUrl) {
        this.mHost = LOCAL_HOST;
        this.mPort = -1;
        setExclusionList("");
        if (pacFileUrl == null) {
            throw new NullPointerException();
        }
        this.mPacFileUrl = pacFileUrl;
    }

    public synchronized ProxyInfo(String pacFileUrl) {
        this.mHost = LOCAL_HOST;
        this.mPort = -1;
        setExclusionList("");
        this.mPacFileUrl = Uri.parse(pacFileUrl);
    }

    public synchronized ProxyInfo(Uri pacFileUrl, int localProxyPort) {
        this.mHost = LOCAL_HOST;
        this.mPort = localProxyPort;
        setExclusionList("");
        if (pacFileUrl == null) {
            throw new NullPointerException();
        }
        this.mPacFileUrl = pacFileUrl;
    }

    private synchronized ProxyInfo(String host, int port, String exclList, String[] parsedExclList) {
        this.mHost = host;
        this.mPort = port;
        this.mExclusionList = exclList;
        this.mParsedExclusionList = parsedExclList;
        this.mPacFileUrl = Uri.EMPTY;
    }

    public synchronized ProxyInfo(ProxyInfo source) {
        if (source != null) {
            this.mHost = source.getHost();
            this.mPort = source.getPort();
            this.mPacFileUrl = source.mPacFileUrl;
            this.mExclusionList = source.getExclusionListAsString();
            this.mParsedExclusionList = source.mParsedExclusionList;
            return;
        }
        this.mPacFileUrl = Uri.EMPTY;
    }

    public synchronized InetSocketAddress getSocketAddress() {
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

    public synchronized String getExclusionListAsString() {
        return this.mExclusionList;
    }

    private synchronized void setExclusionList(String exclusionList) {
        this.mExclusionList = exclusionList;
        if (this.mExclusionList == null) {
            this.mParsedExclusionList = new String[0];
        } else {
            this.mParsedExclusionList = exclusionList.toLowerCase(Locale.ROOT).split(",");
        }
    }

    public synchronized boolean isValid() {
        if (Uri.EMPTY.equals(this.mPacFileUrl)) {
            return Proxy.validate(this.mHost == null ? "" : this.mHost, this.mPort == 0 ? "" : Integer.toString(this.mPort), this.mExclusionList == null ? "" : this.mExclusionList) == 0;
        }
        return true;
    }

    public synchronized java.net.Proxy makeProxy() {
        java.net.Proxy proxy = java.net.Proxy.NO_PROXY;
        if (this.mHost != null) {
            try {
                InetSocketAddress inetSocketAddress = new InetSocketAddress(this.mHost, this.mPort);
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
                if (this.mExclusionList == null || this.mExclusionList.equals(p.getExclusionListAsString())) {
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
        int hashCode;
        if (this.mHost != null) {
            hashCode = this.mHost.hashCode();
        } else {
            hashCode = 0;
        }
        return hashCode + (this.mExclusionList != null ? this.mExclusionList.hashCode() : 0) + this.mPort;
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
