package android.content.pm;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Printer;
import android.util.Slog;
import java.text.Collator;
import java.util.Comparator;

/* loaded from: classes.dex */
public class ResolveInfo implements Parcelable {
    public static final Parcelable.Creator<ResolveInfo> CREATOR = new Parcelable.Creator<ResolveInfo>() { // from class: android.content.pm.ResolveInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ResolveInfo createFromParcel(Parcel source) {
            return new ResolveInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ResolveInfo[] newArray(int size) {
            return new ResolveInfo[size];
        }
    };
    private static final String TAG = "ResolveInfo";
    public ActivityInfo activityInfo;
    public AuxiliaryResolveInfo auxiliaryInfo;
    public IntentFilter filter;
    @SystemApi
    public boolean handleAllWebDataURI;
    public int icon;
    public int iconResourceId;
    @Deprecated
    public boolean instantAppAvailable;
    public boolean isDefault;
    public boolean isInstantAppAvailable;
    public int labelRes;
    public int match;
    public boolean noResourceId;
    public CharSequence nonLocalizedLabel;
    public int preferredOrder;
    public int priority;
    public ProviderInfo providerInfo;
    public String resolvePackageName;
    public ServiceInfo serviceInfo;
    public int specificIndex;
    @UnsupportedAppUsage
    public boolean system;
    @UnsupportedAppUsage
    public int targetUserId;

    @UnsupportedAppUsage
    public ComponentInfo getComponentInfo() {
        ActivityInfo activityInfo = this.activityInfo;
        if (activityInfo != null) {
            return activityInfo;
        }
        ServiceInfo serviceInfo = this.serviceInfo;
        if (serviceInfo != null) {
            return serviceInfo;
        }
        ProviderInfo providerInfo = this.providerInfo;
        if (providerInfo != null) {
            return providerInfo;
        }
        throw new IllegalStateException("Missing ComponentInfo!");
    }

    public CharSequence loadLabel(PackageManager pm) {
        CharSequence label;
        int i;
        CharSequence label2;
        CharSequence charSequence = this.nonLocalizedLabel;
        if (charSequence != null) {
            return charSequence;
        }
        String str = this.resolvePackageName;
        if (str != null && (i = this.labelRes) != 0 && (label2 = pm.getText(str, i, null)) != null) {
            return label2.toString().trim();
        }
        ComponentInfo ci = getComponentInfo();
        ApplicationInfo ai = ci.applicationInfo;
        if (this.labelRes != 0 && (label = pm.getText(ci.packageName, this.labelRes, ai)) != null) {
            return label.toString().trim();
        }
        CharSequence data = ci.loadLabel(pm);
        return data != null ? data.toString().trim() : data;
    }

    public int resolveLabelResId() {
        int i = this.labelRes;
        if (i != 0) {
            return i;
        }
        ComponentInfo componentInfo = getComponentInfo();
        if (componentInfo.labelRes != 0) {
            return componentInfo.labelRes;
        }
        return componentInfo.applicationInfo.labelRes;
    }

    public int resolveIconResId() {
        int i = this.icon;
        if (i != 0) {
            return i;
        }
        ComponentInfo componentInfo = getComponentInfo();
        if (componentInfo.icon != 0) {
            return componentInfo.icon;
        }
        return componentInfo.applicationInfo.icon;
    }

    public Drawable loadIcon(PackageManager pm) {
        int i;
        Drawable dr = null;
        String str = this.resolvePackageName;
        if (str != null && (i = this.iconResourceId) != 0) {
            dr = pm.getDrawable(str, i, null);
        }
        ComponentInfo ci = getComponentInfo();
        if (dr == null && this.iconResourceId != 0) {
            ApplicationInfo ai = ci.applicationInfo;
            dr = pm.getDrawable(ci.packageName, this.iconResourceId, ai);
        }
        if (dr != null) {
            return pm.getUserBadgedIcon(dr, new UserHandle(pm.getUserId()));
        }
        return ci.loadIcon(pm);
    }

    final int getIconResourceInternal() {
        int i = this.iconResourceId;
        if (i != 0) {
            return i;
        }
        ComponentInfo ci = getComponentInfo();
        if (ci != null) {
            return ci.getIconResource();
        }
        return 0;
    }

    public final int getIconResource() {
        if (this.noResourceId) {
            return 0;
        }
        return getIconResourceInternal();
    }

    public void dump(Printer pw, String prefix) {
        dump(pw, prefix, 3);
    }

    public void dump(Printer pw, String prefix, int dumpFlags) {
        if (this.filter != null) {
            pw.println(prefix + "Filter:");
            IntentFilter intentFilter = this.filter;
            intentFilter.dump(pw, prefix + "  ");
        }
        pw.println(prefix + "priority=" + this.priority + " preferredOrder=" + this.preferredOrder + " match=0x" + Integer.toHexString(this.match) + " specificIndex=" + this.specificIndex + " isDefault=" + this.isDefault);
        if (this.resolvePackageName != null) {
            pw.println(prefix + "resolvePackageName=" + this.resolvePackageName);
        }
        if (this.labelRes != 0 || this.nonLocalizedLabel != null || this.icon != 0) {
            pw.println(prefix + "labelRes=0x" + Integer.toHexString(this.labelRes) + " nonLocalizedLabel=" + ((Object) this.nonLocalizedLabel) + " icon=0x" + Integer.toHexString(this.icon));
        }
        if (this.activityInfo != null) {
            pw.println(prefix + "ActivityInfo:");
            ActivityInfo activityInfo = this.activityInfo;
            activityInfo.dump(pw, prefix + "  ", dumpFlags);
        } else if (this.serviceInfo != null) {
            pw.println(prefix + "ServiceInfo:");
            ServiceInfo serviceInfo = this.serviceInfo;
            serviceInfo.dump(pw, prefix + "  ", dumpFlags);
        } else if (this.providerInfo != null) {
            pw.println(prefix + "ProviderInfo:");
            ProviderInfo providerInfo = this.providerInfo;
            providerInfo.dump(pw, prefix + "  ", dumpFlags);
        }
    }

    public ResolveInfo() {
        this.specificIndex = -1;
        this.targetUserId = -2;
    }

    public ResolveInfo(ResolveInfo orig) {
        this.specificIndex = -1;
        this.activityInfo = orig.activityInfo;
        this.serviceInfo = orig.serviceInfo;
        this.providerInfo = orig.providerInfo;
        this.filter = orig.filter;
        this.priority = orig.priority;
        this.preferredOrder = orig.preferredOrder;
        this.match = orig.match;
        this.specificIndex = orig.specificIndex;
        this.labelRes = orig.labelRes;
        this.nonLocalizedLabel = orig.nonLocalizedLabel;
        this.icon = orig.icon;
        this.resolvePackageName = orig.resolvePackageName;
        this.noResourceId = orig.noResourceId;
        this.iconResourceId = orig.iconResourceId;
        this.system = orig.system;
        this.targetUserId = orig.targetUserId;
        this.handleAllWebDataURI = orig.handleAllWebDataURI;
        this.isInstantAppAvailable = orig.isInstantAppAvailable;
        this.instantAppAvailable = this.isInstantAppAvailable;
    }

    public String toString() {
        ComponentInfo ci = getComponentInfo();
        StringBuilder sb = new StringBuilder(128);
        sb.append("ResolveInfo{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(' ');
        ComponentName.appendShortString(sb, ci.packageName, ci.name);
        if (this.priority != 0) {
            sb.append(" p=");
            sb.append(this.priority);
        }
        if (this.preferredOrder != 0) {
            sb.append(" o=");
            sb.append(this.preferredOrder);
        }
        sb.append(" m=0x");
        sb.append(Integer.toHexString(this.match));
        if (this.targetUserId != -2) {
            sb.append(" targetUserId=");
            sb.append(this.targetUserId);
        }
        sb.append('}');
        return sb.toString();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int parcelableFlags) {
        if (this.activityInfo != null) {
            dest.writeInt(1);
            this.activityInfo.writeToParcel(dest, parcelableFlags);
        } else if (this.serviceInfo != null) {
            dest.writeInt(2);
            this.serviceInfo.writeToParcel(dest, parcelableFlags);
        } else if (this.providerInfo != null) {
            dest.writeInt(3);
            this.providerInfo.writeToParcel(dest, parcelableFlags);
        } else {
            dest.writeInt(0);
        }
        if (this.filter != null) {
            dest.writeInt(1);
            this.filter.writeToParcel(dest, parcelableFlags);
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(this.priority);
        dest.writeInt(this.preferredOrder);
        dest.writeInt(this.match);
        dest.writeInt(this.specificIndex);
        dest.writeInt(this.labelRes);
        TextUtils.writeToParcel(this.nonLocalizedLabel, dest, parcelableFlags);
        dest.writeInt(this.icon);
        dest.writeString(this.resolvePackageName);
        dest.writeInt(this.targetUserId);
        dest.writeInt(this.system ? 1 : 0);
        dest.writeInt(this.noResourceId ? 1 : 0);
        dest.writeInt(this.iconResourceId);
        dest.writeInt(this.handleAllWebDataURI ? 1 : 0);
        dest.writeInt(this.isInstantAppAvailable ? 1 : 0);
    }

    private ResolveInfo(Parcel source) {
        this.specificIndex = -1;
        this.activityInfo = null;
        this.serviceInfo = null;
        this.providerInfo = null;
        int readInt = source.readInt();
        if (readInt == 1) {
            this.activityInfo = ActivityInfo.CREATOR.createFromParcel(source);
        } else if (readInt == 2) {
            this.serviceInfo = ServiceInfo.CREATOR.createFromParcel(source);
        } else if (readInt == 3) {
            this.providerInfo = ProviderInfo.CREATOR.createFromParcel(source);
        } else {
            Slog.w(TAG, "Missing ComponentInfo!");
        }
        if (source.readInt() != 0) {
            this.filter = IntentFilter.CREATOR.createFromParcel(source);
        }
        this.priority = source.readInt();
        this.preferredOrder = source.readInt();
        this.match = source.readInt();
        this.specificIndex = source.readInt();
        this.labelRes = source.readInt();
        this.nonLocalizedLabel = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source);
        this.icon = source.readInt();
        this.resolvePackageName = source.readString();
        this.targetUserId = source.readInt();
        this.system = source.readInt() != 0;
        this.noResourceId = source.readInt() != 0;
        this.iconResourceId = source.readInt();
        this.handleAllWebDataURI = source.readInt() != 0;
        boolean z = source.readInt() != 0;
        this.isInstantAppAvailable = z;
        this.instantAppAvailable = z;
    }

    /* loaded from: classes.dex */
    public static class DisplayNameComparator implements Comparator<ResolveInfo> {
        private final Collator mCollator = Collator.getInstance();
        private PackageManager mPM;

        public DisplayNameComparator(PackageManager pm) {
            this.mPM = pm;
            this.mCollator.setStrength(0);
        }

        @Override // java.util.Comparator
        public final int compare(ResolveInfo a, ResolveInfo b) {
            if (a.targetUserId != -2) {
                return 1;
            }
            if (b.targetUserId != -2) {
                return -1;
            }
            CharSequence sa = a.loadLabel(this.mPM);
            if (sa == null) {
                sa = a.activityInfo.name;
            }
            CharSequence sb = b.loadLabel(this.mPM);
            if (sb == null) {
                sb = b.activityInfo.name;
            }
            return this.mCollator.compare(sa.toString(), sb.toString());
        }
    }
}
