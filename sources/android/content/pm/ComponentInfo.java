package android.content.pm;

import android.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.util.Printer;

/* loaded from: classes.dex */
public class ComponentInfo extends PackageItemInfo {
    public ApplicationInfo applicationInfo;
    public int descriptionRes;
    public boolean directBootAware;
    public boolean enabled;
    @Deprecated
    public boolean encryptionAware;
    public boolean exported;
    public String processName;
    public String splitName;

    public ComponentInfo() {
        this.enabled = true;
        this.exported = false;
        this.directBootAware = false;
        this.encryptionAware = false;
    }

    public ComponentInfo(ComponentInfo orig) {
        super(orig);
        this.enabled = true;
        this.exported = false;
        this.directBootAware = false;
        this.encryptionAware = false;
        this.applicationInfo = orig.applicationInfo;
        this.processName = orig.processName;
        this.splitName = orig.splitName;
        this.descriptionRes = orig.descriptionRes;
        this.enabled = orig.enabled;
        this.exported = orig.exported;
        boolean z = orig.directBootAware;
        this.directBootAware = z;
        this.encryptionAware = z;
    }

    @Override // android.content.pm.PackageItemInfo
    public CharSequence loadUnsafeLabel(PackageManager pm) {
        CharSequence label;
        CharSequence label2;
        if (this.nonLocalizedLabel != null) {
            return this.nonLocalizedLabel;
        }
        ApplicationInfo ai = this.applicationInfo;
        if (this.labelRes != 0 && (label2 = pm.getText(this.packageName, this.labelRes, ai)) != null) {
            return label2;
        }
        CharSequence label3 = ai.nonLocalizedLabel;
        if (label3 != null) {
            return ai.nonLocalizedLabel;
        }
        if (ai.labelRes != 0 && (label = pm.getText(this.packageName, ai.labelRes, ai)) != null) {
            return label;
        }
        CharSequence label4 = this.name;
        return label4;
    }

    public boolean isEnabled() {
        return this.enabled && this.applicationInfo.enabled;
    }

    public final int getIconResource() {
        return this.icon != 0 ? this.icon : this.applicationInfo.icon;
    }

    public final int getLogoResource() {
        return this.logo != 0 ? this.logo : this.applicationInfo.logo;
    }

    public final int getBannerResource() {
        return this.banner != 0 ? this.banner : this.applicationInfo.banner;
    }

    @UnsupportedAppUsage
    public ComponentName getComponentName() {
        return new ComponentName(this.packageName, this.name);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.content.pm.PackageItemInfo
    public void dumpFront(Printer pw, String prefix) {
        super.dumpFront(pw, prefix);
        if (this.processName != null && !this.packageName.equals(this.processName)) {
            pw.println(prefix + "processName=" + this.processName);
        }
        if (this.splitName != null) {
            pw.println(prefix + "splitName=" + this.splitName);
        }
        pw.println(prefix + "enabled=" + this.enabled + " exported=" + this.exported + " directBootAware=" + this.directBootAware);
        if (this.descriptionRes != 0) {
            pw.println(prefix + "description=" + this.descriptionRes);
        }
    }

    @Override // android.content.pm.PackageItemInfo
    protected void dumpBack(Printer pw, String prefix) {
        dumpBack(pw, prefix, 3);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpBack(Printer pw, String prefix, int dumpFlags) {
        if ((dumpFlags & 2) != 0) {
            if (this.applicationInfo != null) {
                pw.println(prefix + "ApplicationInfo:");
                ApplicationInfo applicationInfo = this.applicationInfo;
                applicationInfo.dump(pw, prefix + "  ", dumpFlags);
            } else {
                pw.println(prefix + "ApplicationInfo: null");
            }
        }
        super.dumpBack(pw, prefix);
    }

    @Override // android.content.pm.PackageItemInfo, android.os.Parcelable
    public void writeToParcel(Parcel dest, int parcelableFlags) {
        super.writeToParcel(dest, parcelableFlags);
        if ((parcelableFlags & 2) != 0) {
            dest.writeInt(0);
        } else {
            dest.writeInt(1);
            this.applicationInfo.writeToParcel(dest, parcelableFlags);
        }
        dest.writeString(this.processName);
        dest.writeString(this.splitName);
        dest.writeInt(this.descriptionRes);
        dest.writeInt(this.enabled ? 1 : 0);
        dest.writeInt(this.exported ? 1 : 0);
        dest.writeInt(this.directBootAware ? 1 : 0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ComponentInfo(Parcel source) {
        super(source);
        boolean hasApplicationInfo;
        boolean z;
        boolean z2;
        this.enabled = true;
        this.exported = false;
        this.directBootAware = false;
        this.encryptionAware = false;
        if (source.readInt() != 0) {
            hasApplicationInfo = true;
        } else {
            hasApplicationInfo = false;
        }
        if (hasApplicationInfo) {
            this.applicationInfo = ApplicationInfo.CREATOR.createFromParcel(source);
        }
        this.processName = source.readString();
        this.splitName = source.readString();
        this.descriptionRes = source.readInt();
        if (source.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.enabled = z;
        if (source.readInt() != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.exported = z2;
        boolean z3 = source.readInt() != 0;
        this.directBootAware = z3;
        this.encryptionAware = z3;
    }

    @Override // android.content.pm.PackageItemInfo
    public Drawable loadDefaultIcon(PackageManager pm) {
        return this.applicationInfo.loadIcon(pm);
    }

    @Override // android.content.pm.PackageItemInfo
    protected Drawable loadDefaultBanner(PackageManager pm) {
        return this.applicationInfo.loadBanner(pm);
    }

    @Override // android.content.pm.PackageItemInfo
    protected Drawable loadDefaultLogo(PackageManager pm) {
        return this.applicationInfo.loadLogo(pm);
    }

    @Override // android.content.pm.PackageItemInfo
    protected ApplicationInfo getApplicationInfo() {
        return this.applicationInfo;
    }
}
