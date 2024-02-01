package android.content.pm;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Objects;

/* loaded from: classes.dex */
public final class ModuleInfo implements Parcelable {
    public static final Parcelable.Creator<ModuleInfo> CREATOR = new Parcelable.Creator<ModuleInfo>() { // from class: android.content.pm.ModuleInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ModuleInfo createFromParcel(Parcel source) {
            return new ModuleInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ModuleInfo[] newArray(int size) {
            return new ModuleInfo[size];
        }
    };
    private boolean mHidden;
    private CharSequence mName;
    private String mPackageName;

    public ModuleInfo() {
    }

    public ModuleInfo(ModuleInfo orig) {
        this.mName = orig.mName;
        this.mPackageName = orig.mPackageName;
        this.mHidden = orig.mHidden;
    }

    public ModuleInfo setName(CharSequence name) {
        this.mName = name;
        return this;
    }

    public CharSequence getName() {
        return this.mName;
    }

    public ModuleInfo setPackageName(String packageName) {
        this.mPackageName = packageName;
        return this;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public ModuleInfo setHidden(boolean hidden) {
        this.mHidden = hidden;
        return this;
    }

    public boolean isHidden() {
        return this.mHidden;
    }

    public String toString() {
        return "ModuleInfo{" + Integer.toHexString(System.identityHashCode(this)) + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + ((Object) this.mName) + "}";
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int hashCode() {
        int hashCode = (0 * 31) + Objects.hashCode(this.mName);
        return (((hashCode * 31) + Objects.hashCode(this.mPackageName)) * 31) + Boolean.hashCode(this.mHidden);
    }

    public boolean equals(Object obj) {
        if (obj instanceof ModuleInfo) {
            ModuleInfo other = (ModuleInfo) obj;
            return Objects.equals(this.mName, other.mName) && Objects.equals(this.mPackageName, other.mPackageName) && this.mHidden == other.mHidden;
        }
        return false;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int parcelableFlags) {
        dest.writeCharSequence(this.mName);
        dest.writeString(this.mPackageName);
        dest.writeBoolean(this.mHidden);
    }

    private ModuleInfo(Parcel source) {
        this.mName = source.readCharSequence();
        this.mPackageName = source.readString();
        this.mHidden = source.readBoolean();
    }
}
