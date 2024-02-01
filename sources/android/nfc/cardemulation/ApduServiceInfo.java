package android.nfc.cardemulation;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.android.internal.telephony.PhoneConstants;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: classes2.dex */
public final class ApduServiceInfo implements Parcelable {
    private protected static final Parcelable.Creator<ApduServiceInfo> CREATOR = new Parcelable.Creator<ApduServiceInfo>() { // from class: android.nfc.cardemulation.ApduServiceInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ApduServiceInfo createFromParcel(Parcel source) {
            ResolveInfo info = ResolveInfo.CREATOR.createFromParcel(source);
            String description = source.readString();
            boolean onHost = source.readInt() != 0;
            ArrayList<AidGroup> staticAidGroups = new ArrayList<>();
            int numStaticGroups = source.readInt();
            if (numStaticGroups > 0) {
                source.readTypedList(staticAidGroups, AidGroup.CREATOR);
            }
            ArrayList<AidGroup> dynamicAidGroups = new ArrayList<>();
            int numDynamicGroups = source.readInt();
            if (numDynamicGroups > 0) {
                source.readTypedList(dynamicAidGroups, AidGroup.CREATOR);
            }
            boolean requiresUnlock = source.readInt() != 0;
            int bannerResource = source.readInt();
            int uid = source.readInt();
            String settingsActivityName = source.readString();
            return new ApduServiceInfo(info, onHost, description, staticAidGroups, dynamicAidGroups, requiresUnlock, bannerResource, uid, settingsActivityName);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ApduServiceInfo[] newArray(int size) {
            return new ApduServiceInfo[size];
        }
    };
    static final String TAG = "ApduServiceInfo";
    final int mBannerResourceId;
    final String mDescription;
    public private protected final HashMap<String, AidGroup> mDynamicAidGroups;
    final boolean mOnHost;
    final boolean mRequiresDeviceUnlock;
    public private protected final ResolveInfo mService;
    final String mSettingsActivityName;
    public private protected final HashMap<String, AidGroup> mStaticAidGroups;
    final int mUid;

    private protected ApduServiceInfo(ResolveInfo info, boolean onHost, String description, ArrayList<AidGroup> staticAidGroups, ArrayList<AidGroup> dynamicAidGroups, boolean requiresUnlock, int bannerResource, int uid, String settingsActivityName) {
        this.mService = info;
        this.mDescription = description;
        this.mStaticAidGroups = new HashMap<>();
        this.mDynamicAidGroups = new HashMap<>();
        this.mOnHost = onHost;
        this.mRequiresDeviceUnlock = requiresUnlock;
        Iterator<AidGroup> it = staticAidGroups.iterator();
        while (it.hasNext()) {
            AidGroup aidGroup = it.next();
            this.mStaticAidGroups.put(aidGroup.category, aidGroup);
        }
        Iterator<AidGroup> it2 = dynamicAidGroups.iterator();
        while (it2.hasNext()) {
            AidGroup aidGroup2 = it2.next();
            this.mDynamicAidGroups.put(aidGroup2.category, aidGroup2);
        }
        this.mBannerResourceId = bannerResource;
        this.mUid = uid;
        this.mSettingsActivityName = settingsActivityName;
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x0062, code lost:
        if ("offhost-apdu-service".equals(r10) == false) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x006c, code lost:
        throw new org.xmlpull.v1.XmlPullParserException("Meta-data does not start with <offhost-apdu-service> tag");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private protected ApduServiceInfo(android.content.pm.PackageManager r22, android.content.pm.ResolveInfo r23, boolean r24) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 719
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: android.nfc.cardemulation.ApduServiceInfo.<init>(android.content.pm.PackageManager, android.content.pm.ResolveInfo, boolean):void");
    }

    public synchronized ComponentName getComponent() {
        return new ComponentName(this.mService.serviceInfo.packageName, this.mService.serviceInfo.name);
    }

    public synchronized List<String> getAids() {
        ArrayList<String> aids = new ArrayList<>();
        Iterator<AidGroup> it = getAidGroups().iterator();
        while (it.hasNext()) {
            AidGroup group = it.next();
            aids.addAll(group.aids);
        }
        return aids;
    }

    public synchronized List<String> getPrefixAids() {
        ArrayList<String> prefixAids = new ArrayList<>();
        Iterator<AidGroup> it = getAidGroups().iterator();
        while (it.hasNext()) {
            AidGroup group = it.next();
            for (String aid : group.aids) {
                if (aid.endsWith(PhoneConstants.APN_TYPE_ALL)) {
                    prefixAids.add(aid);
                }
            }
        }
        return prefixAids;
    }

    public synchronized List<String> getSubsetAids() {
        ArrayList<String> subsetAids = new ArrayList<>();
        Iterator<AidGroup> it = getAidGroups().iterator();
        while (it.hasNext()) {
            AidGroup group = it.next();
            for (String aid : group.aids) {
                if (aid.endsWith("#")) {
                    subsetAids.add(aid);
                }
            }
        }
        return subsetAids;
    }

    public synchronized AidGroup getDynamicAidGroupForCategory(String category) {
        return this.mDynamicAidGroups.get(category);
    }

    public synchronized boolean removeDynamicAidGroupForCategory(String category) {
        return this.mDynamicAidGroups.remove(category) != null;
    }

    public synchronized ArrayList<AidGroup> getAidGroups() {
        ArrayList<AidGroup> groups = new ArrayList<>();
        for (Map.Entry<String, AidGroup> entry : this.mDynamicAidGroups.entrySet()) {
            groups.add(entry.getValue());
        }
        for (Map.Entry<String, AidGroup> entry2 : this.mStaticAidGroups.entrySet()) {
            if (!this.mDynamicAidGroups.containsKey(entry2.getKey())) {
                groups.add(entry2.getValue());
            }
        }
        return groups;
    }

    public synchronized String getCategoryForAid(String aid) {
        ArrayList<AidGroup> groups = getAidGroups();
        Iterator<AidGroup> it = groups.iterator();
        while (it.hasNext()) {
            AidGroup group = it.next();
            if (group.aids.contains(aid.toUpperCase())) {
                return group.category;
            }
        }
        return null;
    }

    public synchronized boolean hasCategory(String category) {
        return this.mStaticAidGroups.containsKey(category) || this.mDynamicAidGroups.containsKey(category);
    }

    private protected boolean isOnHost() {
        return this.mOnHost;
    }

    private protected boolean requiresUnlock() {
        return this.mRequiresDeviceUnlock;
    }

    private protected String getDescription() {
        return this.mDescription;
    }

    private protected int getUid() {
        return this.mUid;
    }

    public synchronized void setOrReplaceDynamicAidGroup(AidGroup aidGroup) {
        this.mDynamicAidGroups.put(aidGroup.getCategory(), aidGroup);
    }

    public synchronized CharSequence loadLabel(PackageManager pm) {
        return this.mService.loadLabel(pm);
    }

    public synchronized CharSequence loadAppLabel(PackageManager pm) {
        try {
            return pm.getApplicationLabel(pm.getApplicationInfo(this.mService.resolvePackageName, 128));
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public synchronized Drawable loadIcon(PackageManager pm) {
        return this.mService.loadIcon(pm);
    }

    private protected Drawable loadBanner(PackageManager pm) {
        try {
            Resources res = pm.getResourcesForApplication(this.mService.serviceInfo.packageName);
            Drawable banner = res.getDrawable(this.mBannerResourceId);
            return banner;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Could not load banner.");
            return null;
        } catch (Resources.NotFoundException e2) {
            Log.e(TAG, "Could not load banner.");
            return null;
        }
    }

    private protected String getSettingsActivityName() {
        return this.mSettingsActivityName;
    }

    public String toString() {
        StringBuilder out = new StringBuilder("ApduService: ");
        out.append(getComponent());
        out.append(", description: " + this.mDescription);
        out.append(", Static AID Groups: ");
        for (AidGroup aidGroup : this.mStaticAidGroups.values()) {
            out.append(aidGroup.toString());
        }
        out.append(", Dynamic AID Groups: ");
        for (AidGroup aidGroup2 : this.mDynamicAidGroups.values()) {
            out.append(aidGroup2.toString());
        }
        return out.toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof ApduServiceInfo) {
            ApduServiceInfo thatService = (ApduServiceInfo) o;
            return thatService.getComponent().equals(getComponent());
        }
        return false;
    }

    public int hashCode() {
        return getComponent().hashCode();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        this.mService.writeToParcel(dest, flags);
        dest.writeString(this.mDescription);
        dest.writeInt(this.mOnHost ? 1 : 0);
        dest.writeInt(this.mStaticAidGroups.size());
        if (this.mStaticAidGroups.size() > 0) {
            dest.writeTypedList(new ArrayList(this.mStaticAidGroups.values()));
        }
        dest.writeInt(this.mDynamicAidGroups.size());
        if (this.mDynamicAidGroups.size() > 0) {
            dest.writeTypedList(new ArrayList(this.mDynamicAidGroups.values()));
        }
        dest.writeInt(this.mRequiresDeviceUnlock ? 1 : 0);
        dest.writeInt(this.mBannerResourceId);
        dest.writeInt(this.mUid);
        dest.writeString(this.mSettingsActivityName);
    }

    public synchronized void dump(FileDescriptor fd, PrintWriter pw, String[] args) {
        pw.println("    " + getComponent() + " (Description: " + getDescription() + ")");
        pw.println("    Static AID groups:");
        for (AidGroup group : this.mStaticAidGroups.values()) {
            pw.println("        Category: " + group.category);
            for (String aid : group.aids) {
                pw.println("            AID: " + aid);
            }
        }
        pw.println("    Dynamic AID groups:");
        for (AidGroup group2 : this.mDynamicAidGroups.values()) {
            pw.println("        Category: " + group2.category);
            for (String aid2 : group2.aids) {
                pw.println("            AID: " + aid2);
            }
        }
        pw.println("    Settings Activity: " + this.mSettingsActivityName);
    }
}
