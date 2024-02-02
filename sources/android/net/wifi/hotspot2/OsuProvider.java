package android.net.wifi.hotspot2;

import android.graphics.drawable.Icon;
import android.net.Uri;
import android.net.wifi.WifiSsid;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
/* loaded from: classes2.dex */
public final class OsuProvider implements Parcelable {
    public static final Parcelable.Creator<OsuProvider> CREATOR = new Parcelable.Creator<OsuProvider>() { // from class: android.net.wifi.hotspot2.OsuProvider.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OsuProvider createFromParcel(Parcel in) {
            WifiSsid osuSsid = (WifiSsid) in.readParcelable(null);
            String friendlyName = in.readString();
            String serviceDescription = in.readString();
            Uri serverUri = (Uri) in.readParcelable(null);
            String nai = in.readString();
            List<Integer> methodList = new ArrayList<>();
            in.readList(methodList, null);
            Icon icon = (Icon) in.readParcelable(null);
            return new OsuProvider(osuSsid, friendlyName, serviceDescription, serverUri, nai, methodList, icon);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public OsuProvider[] newArray(int size) {
            return new OsuProvider[size];
        }
    };
    public static final int METHOD_OMA_DM = 0;
    public static final int METHOD_SOAP_XML_SPP = 1;
    private final String mFriendlyName;
    private final Icon mIcon;
    private final List<Integer> mMethodList;
    private final String mNetworkAccessIdentifier;
    private final WifiSsid mOsuSsid;
    private final Uri mServerUri;
    private final String mServiceDescription;

    public synchronized OsuProvider(WifiSsid osuSsid, String friendlyName, String serviceDescription, Uri serverUri, String nai, List<Integer> methodList, Icon icon) {
        this.mOsuSsid = osuSsid;
        this.mFriendlyName = friendlyName;
        this.mServiceDescription = serviceDescription;
        this.mServerUri = serverUri;
        this.mNetworkAccessIdentifier = nai;
        if (methodList == null) {
            this.mMethodList = new ArrayList();
        } else {
            this.mMethodList = new ArrayList(methodList);
        }
        this.mIcon = icon;
    }

    public synchronized OsuProvider(OsuProvider source) {
        if (source == null) {
            this.mOsuSsid = null;
            this.mFriendlyName = null;
            this.mServiceDescription = null;
            this.mServerUri = null;
            this.mNetworkAccessIdentifier = null;
            this.mMethodList = new ArrayList();
            this.mIcon = null;
            return;
        }
        this.mOsuSsid = source.mOsuSsid;
        this.mFriendlyName = source.mFriendlyName;
        this.mServiceDescription = source.mServiceDescription;
        this.mServerUri = source.mServerUri;
        this.mNetworkAccessIdentifier = source.mNetworkAccessIdentifier;
        if (source.mMethodList == null) {
            this.mMethodList = new ArrayList();
        } else {
            this.mMethodList = new ArrayList(source.mMethodList);
        }
        this.mIcon = source.mIcon;
    }

    public synchronized WifiSsid getOsuSsid() {
        return this.mOsuSsid;
    }

    public synchronized String getFriendlyName() {
        return this.mFriendlyName;
    }

    public synchronized String getServiceDescription() {
        return this.mServiceDescription;
    }

    public synchronized Uri getServerUri() {
        return this.mServerUri;
    }

    public synchronized String getNetworkAccessIdentifier() {
        return this.mNetworkAccessIdentifier;
    }

    public synchronized List<Integer> getMethodList() {
        return Collections.unmodifiableList(this.mMethodList);
    }

    public synchronized Icon getIcon() {
        return this.mIcon;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mOsuSsid, flags);
        dest.writeString(this.mFriendlyName);
        dest.writeString(this.mServiceDescription);
        dest.writeParcelable(this.mServerUri, flags);
        dest.writeString(this.mNetworkAccessIdentifier);
        dest.writeList(this.mMethodList);
        dest.writeParcelable(this.mIcon, flags);
    }

    public boolean equals(Object thatObject) {
        if (this == thatObject) {
            return true;
        }
        if (thatObject instanceof OsuProvider) {
            OsuProvider that = (OsuProvider) thatObject;
            if (this.mOsuSsid != null ? this.mOsuSsid.equals(that.mOsuSsid) : that.mOsuSsid == null) {
                if (TextUtils.equals(this.mFriendlyName, that.mFriendlyName) && TextUtils.equals(this.mServiceDescription, that.mServiceDescription) && (this.mServerUri != null ? this.mServerUri.equals(that.mServerUri) : that.mServerUri == null) && TextUtils.equals(this.mNetworkAccessIdentifier, that.mNetworkAccessIdentifier) && (this.mMethodList != null ? this.mMethodList.equals(that.mMethodList) : that.mMethodList == null)) {
                    if (this.mIcon == null) {
                        if (that.mIcon == null) {
                            return true;
                        }
                    } else if (this.mIcon.sameAs(that.mIcon)) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.mOsuSsid, this.mFriendlyName, this.mServiceDescription, this.mServerUri, this.mNetworkAccessIdentifier, this.mMethodList, this.mIcon);
    }

    public String toString() {
        return "OsuProvider{mOsuSsid=" + this.mOsuSsid + " mFriendlyName=" + this.mFriendlyName + " mServiceDescription=" + this.mServiceDescription + " mServerUri=" + this.mServerUri + " mNetworkAccessIdentifier=" + this.mNetworkAccessIdentifier + " mMethodList=" + this.mMethodList + " mIcon=" + this.mIcon;
    }
}
