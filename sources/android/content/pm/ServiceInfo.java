package android.content.pm;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Printer;
/* loaded from: classes.dex */
public class ServiceInfo extends ComponentInfo implements Parcelable {
    public static final Parcelable.Creator<ServiceInfo> CREATOR = new Parcelable.Creator<ServiceInfo>() { // from class: android.content.pm.ServiceInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ServiceInfo createFromParcel(Parcel source) {
            return new ServiceInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ServiceInfo[] newArray(int size) {
            return new ServiceInfo[size];
        }
    };
    public static final int FLAG_EXTERNAL_SERVICE = 4;
    public static final int FLAG_ISOLATED_PROCESS = 2;
    public static final int FLAG_SINGLE_USER = 1073741824;
    public static final int FLAG_STOP_WITH_TASK = 1;
    public static final int FLAG_VISIBLE_TO_INSTANT_APP = 1048576;
    public int flags;
    public String permission;

    public ServiceInfo() {
    }

    public ServiceInfo(ServiceInfo orig) {
        super(orig);
        this.permission = orig.permission;
        this.flags = orig.flags;
    }

    public void dump(Printer pw, String prefix) {
        dump(pw, prefix, 3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void dump(Printer pw, String prefix, int dumpFlags) {
        super.dumpFront(pw, prefix);
        pw.println(prefix + "permission=" + this.permission);
        pw.println(prefix + "flags=0x" + Integer.toHexString(this.flags));
        super.dumpBack(pw, prefix, dumpFlags);
    }

    public String toString() {
        return "ServiceInfo{" + Integer.toHexString(System.identityHashCode(this)) + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + this.name + "}";
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.content.pm.ComponentInfo, android.content.pm.PackageItemInfo, android.os.Parcelable
    public void writeToParcel(Parcel dest, int parcelableFlags) {
        super.writeToParcel(dest, parcelableFlags);
        dest.writeString(this.permission);
        dest.writeInt(this.flags);
    }

    private synchronized ServiceInfo(Parcel source) {
        super(source);
        this.permission = source.readString();
        this.flags = source.readInt();
    }
}
