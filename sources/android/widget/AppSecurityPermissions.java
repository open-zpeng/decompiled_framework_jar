package android.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.UserHandle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.android.internal.R;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
/* loaded from: classes3.dex */
public class AppSecurityPermissions {
    private static final String TAG = "AppSecurityPermissions";
    public static final int WHICH_ALL = 65535;
    public static final int WHICH_NEW = 4;
    private static final boolean localLOGV = false;
    private final Context mContext;
    private final LayoutInflater mInflater;
    private final CharSequence mNewPermPrefix;
    private String mPackageName;
    private final PermissionInfoComparator mPermComparator;
    private final PermissionGroupInfoComparator mPermGroupComparator;
    private final Map<String, MyPermissionGroupInfo> mPermGroups;
    private final List<MyPermissionGroupInfo> mPermGroupsList;
    private final List<MyPermissionInfo> mPermsList;
    private final PackageManager mPm;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class MyPermissionGroupInfo extends PermissionGroupInfo {
        final ArrayList<MyPermissionInfo> mAllPermissions;
        CharSequence mLabel;
        final ArrayList<MyPermissionInfo> mNewPermissions;

        synchronized MyPermissionGroupInfo(PermissionInfo perm) {
            this.mNewPermissions = new ArrayList<>();
            this.mAllPermissions = new ArrayList<>();
            this.name = perm.packageName;
            this.packageName = perm.packageName;
        }

        synchronized MyPermissionGroupInfo(PermissionGroupInfo info) {
            super(info);
            this.mNewPermissions = new ArrayList<>();
            this.mAllPermissions = new ArrayList<>();
        }

        public synchronized Drawable loadGroupIcon(Context context, PackageManager pm) {
            if (this.icon != 0) {
                return loadUnbadgedIcon(pm);
            }
            return context.getDrawable(R.drawable.ic_perm_device_info);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class MyPermissionInfo extends PermissionInfo {
        int mExistingReqFlags;
        CharSequence mLabel;
        boolean mNew;
        int mNewReqFlags;

        synchronized MyPermissionInfo(PermissionInfo info) {
            super(info);
        }
    }

    /* loaded from: classes3.dex */
    public static class PermissionItemView extends LinearLayout implements View.OnClickListener {
        AlertDialog mDialog;
        MyPermissionGroupInfo mGroup;
        private String mPackageName;
        MyPermissionInfo mPerm;
        private boolean mShowRevokeUI;

        public synchronized PermissionItemView(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.mShowRevokeUI = false;
            setClickable(true);
        }

        public synchronized void setPermission(MyPermissionGroupInfo grp, MyPermissionInfo perm, boolean first, CharSequence newPermPrefix, String packageName, boolean showRevokeUI) {
            this.mGroup = grp;
            this.mPerm = perm;
            this.mShowRevokeUI = showRevokeUI;
            this.mPackageName = packageName;
            ImageView permGrpIcon = (ImageView) findViewById(R.id.perm_icon);
            TextView permNameView = (TextView) findViewById(R.id.perm_name);
            PackageManager pm = getContext().getPackageManager();
            Drawable icon = null;
            if (first) {
                icon = grp.loadGroupIcon(getContext(), pm);
            }
            CharSequence label = perm.mLabel;
            if (perm.mNew && newPermPrefix != null) {
                SpannableStringBuilder builder = new SpannableStringBuilder();
                Parcel parcel = Parcel.obtain();
                TextUtils.writeToParcel(newPermPrefix, parcel, 0);
                parcel.setDataPosition(0);
                CharSequence newStr = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
                parcel.recycle();
                builder.append(newStr);
                builder.append(label);
                label = builder;
            }
            permGrpIcon.setImageDrawable(icon);
            permNameView.setText(label);
            setOnClickListener(this);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View v) {
            CharSequence appName;
            if (this.mGroup != null && this.mPerm != null) {
                if (this.mDialog != null) {
                    this.mDialog.dismiss();
                }
                PackageManager pm = getContext().getPackageManager();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(this.mGroup.mLabel);
                if (this.mPerm.descriptionRes != 0) {
                    builder.setMessage(this.mPerm.loadDescription(pm));
                } else {
                    try {
                        ApplicationInfo app = pm.getApplicationInfo(this.mPerm.packageName, 0);
                        appName = app.loadLabel(pm);
                    } catch (PackageManager.NameNotFoundException e) {
                        appName = this.mPerm.packageName;
                    }
                    StringBuilder sbuilder = new StringBuilder(128);
                    sbuilder.append(getContext().getString(R.string.perms_description_app, appName));
                    sbuilder.append("\n\n");
                    sbuilder.append(this.mPerm.name);
                    builder.setMessage(sbuilder.toString());
                }
                builder.setCancelable(true);
                builder.setIcon(this.mGroup.loadGroupIcon(getContext(), pm));
                addRevokeUIIfNecessary(builder);
                this.mDialog = builder.show();
                this.mDialog.setCanceledOnTouchOutside(true);
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.view.ViewGroup, android.view.View
        public void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            if (this.mDialog != null) {
                this.mDialog.dismiss();
            }
        }

        private synchronized void addRevokeUIIfNecessary(AlertDialog.Builder builder) {
            if (!this.mShowRevokeUI) {
                return;
            }
            boolean isRequired = (this.mPerm.mExistingReqFlags & 1) != 0;
            if (isRequired) {
                return;
            }
            DialogInterface.OnClickListener ocl = new DialogInterface.OnClickListener() { // from class: android.widget.AppSecurityPermissions.PermissionItemView.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    PackageManager pm = PermissionItemView.this.getContext().getPackageManager();
                    pm.revokeRuntimePermission(PermissionItemView.this.mPackageName, PermissionItemView.this.mPerm.name, new UserHandle(PermissionItemView.this.mContext.getUserId()));
                    PermissionItemView.this.setVisibility(8);
                }
            };
            builder.setNegativeButton(R.string.revoke, ocl);
            builder.setPositiveButton(android.R.string.ok, (DialogInterface.OnClickListener) null);
        }
    }

    private synchronized AppSecurityPermissions(Context context) {
        this.mPermGroups = new HashMap();
        this.mPermGroupsList = new ArrayList();
        this.mPermGroupComparator = new PermissionGroupInfoComparator();
        this.mPermComparator = new PermissionInfoComparator();
        this.mPermsList = new ArrayList();
        this.mContext = context;
        this.mInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mPm = this.mContext.getPackageManager();
        this.mNewPermPrefix = this.mContext.getText(R.string.perms_new_perm_prefix);
    }

    private protected AppSecurityPermissions(Context context, String packageName) {
        this(context);
        this.mPackageName = packageName;
        Set<MyPermissionInfo> permSet = new HashSet<>();
        try {
            PackageInfo pkgInfo = this.mPm.getPackageInfo(packageName, 4096);
            if (pkgInfo.applicationInfo != null && pkgInfo.applicationInfo.uid != -1) {
                getAllUsedPermissions(pkgInfo.applicationInfo.uid, permSet);
            }
            this.mPermsList.addAll(permSet);
            setPermissions(this.mPermsList);
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "Couldn't retrieve permissions for package:" + packageName);
        }
    }

    public synchronized AppSecurityPermissions(Context context, PackageInfo info) {
        this(context);
        Set<MyPermissionInfo> permSet = new HashSet<>();
        if (info == null) {
            return;
        }
        this.mPackageName = info.packageName;
        PackageInfo installedPkgInfo = null;
        if (info.requestedPermissions != null) {
            try {
                installedPkgInfo = this.mPm.getPackageInfo(info.packageName, 4096);
            } catch (PackageManager.NameNotFoundException e) {
            }
            extractPerms(info, permSet, installedPkgInfo);
        }
        if (info.sharedUserId != null) {
            try {
                int sharedUid = this.mPm.getUidForSharedUser(info.sharedUserId);
                getAllUsedPermissions(sharedUid, permSet);
            } catch (PackageManager.NameNotFoundException e2) {
                Log.w(TAG, "Couldn't retrieve shared user id for: " + info.packageName);
            }
        }
        this.mPermsList.addAll(permSet);
        setPermissions(this.mPermsList);
    }

    public static synchronized View getPermissionItemView(Context context, CharSequence grpName, CharSequence description, boolean dangerous) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Drawable icon = context.getDrawable(dangerous ? R.drawable.ic_bullet_key_permission : R.drawable.ic_text_dot);
        return getPermissionItemViewOld(context, inflater, grpName, description, dangerous, icon);
    }

    private synchronized void getAllUsedPermissions(int sharedUid, Set<MyPermissionInfo> permSet) {
        String[] sharedPkgList = this.mPm.getPackagesForUid(sharedUid);
        if (sharedPkgList == null || sharedPkgList.length == 0) {
            return;
        }
        for (String sharedPkg : sharedPkgList) {
            getPermissionsForPackage(sharedPkg, permSet);
        }
    }

    private synchronized void getPermissionsForPackage(String packageName, Set<MyPermissionInfo> permSet) {
        try {
            PackageInfo pkgInfo = this.mPm.getPackageInfo(packageName, 4096);
            extractPerms(pkgInfo, permSet, pkgInfo);
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "Couldn't retrieve permissions for package: " + packageName);
        }
    }

    private synchronized void extractPerms(PackageInfo info, Set<MyPermissionInfo> permSet, PackageInfo installedPkgInfo) {
        PermissionInfo tmpPermInfo;
        MyPermissionGroupInfo group;
        String[] strList = info.requestedPermissions;
        int[] flagsList = info.requestedPermissionsFlags;
        if (strList == null || strList.length == 0) {
            return;
        }
        int i = 0;
        int i2 = 0;
        while (true) {
            int i3 = i2;
            int i4 = strList.length;
            if (i3 >= i4) {
                return;
            }
            String permName = strList[i3];
            try {
                tmpPermInfo = this.mPm.getPermissionInfo(permName, i);
            } catch (PackageManager.NameNotFoundException e) {
            }
            if (tmpPermInfo != null) {
                int existingIndex = -1;
                if (installedPkgInfo != null && installedPkgInfo.requestedPermissions != null) {
                    int j = i;
                    while (true) {
                        if (j >= installedPkgInfo.requestedPermissions.length) {
                            break;
                        } else if (!permName.equals(installedPkgInfo.requestedPermissions[j])) {
                            j++;
                        } else {
                            existingIndex = j;
                            break;
                        }
                    }
                }
                int existingFlags = existingIndex >= 0 ? installedPkgInfo.requestedPermissionsFlags[existingIndex] : i;
                if (isDisplayablePermission(tmpPermInfo, flagsList[i3], existingFlags)) {
                    String origGroupName = tmpPermInfo.group;
                    String groupName = origGroupName;
                    if (groupName == null) {
                        groupName = tmpPermInfo.packageName;
                        tmpPermInfo.group = groupName;
                    }
                    MyPermissionGroupInfo group2 = this.mPermGroups.get(groupName);
                    if (group2 == null) {
                        PermissionGroupInfo grp = null;
                        if (origGroupName != null) {
                            grp = this.mPm.getPermissionGroupInfo(origGroupName, i);
                        }
                        if (grp != null) {
                            group = new MyPermissionGroupInfo(grp);
                        } else {
                            tmpPermInfo.group = tmpPermInfo.packageName;
                            MyPermissionGroupInfo group3 = this.mPermGroups.get(tmpPermInfo.group);
                            if (group3 == null) {
                                new MyPermissionGroupInfo(tmpPermInfo);
                            }
                            group = new MyPermissionGroupInfo(tmpPermInfo);
                        }
                        this.mPermGroups.put(tmpPermInfo.group, group);
                    }
                    boolean newPerm = installedPkgInfo != null && (existingFlags & 2) == 0;
                    MyPermissionInfo myPerm = new MyPermissionInfo(tmpPermInfo);
                    myPerm.mNewReqFlags = flagsList[i3];
                    myPerm.mExistingReqFlags = existingFlags;
                    myPerm.mNew = newPerm;
                    try {
                        permSet.add(myPerm);
                    } catch (PackageManager.NameNotFoundException e2) {
                        Log.i(TAG, "Ignoring unknown permission:" + permName);
                        i2 = i3 + 1;
                        i = 0;
                    }
                    i2 = i3 + 1;
                    i = 0;
                }
            }
            i2 = i3 + 1;
            i = 0;
        }
    }

    private protected int getPermissionCount() {
        return getPermissionCount(65535);
    }

    private synchronized List<MyPermissionInfo> getPermissionList(MyPermissionGroupInfo grp, int which) {
        if (which == 4) {
            return grp.mNewPermissions;
        }
        return grp.mAllPermissions;
    }

    public synchronized int getPermissionCount(int which) {
        int N = 0;
        for (int i = 0; i < this.mPermGroupsList.size(); i++) {
            N += getPermissionList(this.mPermGroupsList.get(i), which).size();
        }
        return N;
    }

    private protected View getPermissionsView() {
        return getPermissionsView(65535, false);
    }

    public synchronized View getPermissionsViewWithRevokeButtons() {
        return getPermissionsView(65535, true);
    }

    public synchronized View getPermissionsView(int which) {
        return getPermissionsView(which, false);
    }

    private synchronized View getPermissionsView(int which, boolean showRevokeUI) {
        LinearLayout permsView = (LinearLayout) this.mInflater.inflate(R.layout.app_perms_summary, (ViewGroup) null);
        LinearLayout displayList = (LinearLayout) permsView.findViewById(R.id.perms_list);
        View noPermsView = permsView.findViewById(R.id.no_permissions);
        displayPermissions(this.mPermGroupsList, displayList, which, showRevokeUI);
        if (displayList.getChildCount() <= 0) {
            noPermsView.setVisibility(0);
        }
        return permsView;
    }

    private synchronized void displayPermissions(List<MyPermissionGroupInfo> groups, LinearLayout permListView, int which, boolean showRevokeUI) {
        permListView.removeAllViews();
        int spacing = (int) (8.0f * this.mContext.getResources().getDisplayMetrics().density);
        int i = 0;
        while (true) {
            int i2 = i;
            int i3 = groups.size();
            if (i2 >= i3) {
                return;
            }
            MyPermissionGroupInfo grp = groups.get(i2);
            List<MyPermissionInfo> perms = getPermissionList(grp, which);
            int j = 0;
            while (true) {
                int j2 = j;
                int j3 = perms.size();
                if (j2 < j3) {
                    MyPermissionInfo perm = perms.get(j2);
                    View view = getPermissionItemView(grp, perm, j2 == 0, which != 4 ? this.mNewPermPrefix : null, showRevokeUI);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
                    if (j2 == 0) {
                        lp.topMargin = spacing;
                    }
                    if (j2 == grp.mAllPermissions.size() - 1) {
                        lp.bottomMargin = spacing;
                    }
                    if (permListView.getChildCount() == 0) {
                        lp.topMargin *= 2;
                    }
                    permListView.addView(view, lp);
                    j = j2 + 1;
                }
            }
            i = i2 + 1;
        }
    }

    private synchronized PermissionItemView getPermissionItemView(MyPermissionGroupInfo grp, MyPermissionInfo perm, boolean first, CharSequence newPermPrefix, boolean showRevokeUI) {
        return getPermissionItemView(this.mContext, this.mInflater, grp, perm, first, newPermPrefix, this.mPackageName, showRevokeUI);
    }

    private static synchronized PermissionItemView getPermissionItemView(Context context, LayoutInflater inflater, MyPermissionGroupInfo grp, MyPermissionInfo perm, boolean first, CharSequence newPermPrefix, String packageName, boolean showRevokeUI) {
        PermissionItemView permView = (PermissionItemView) inflater.inflate((perm.flags & 1) != 0 ? R.layout.app_permission_item_money : R.layout.app_permission_item, (ViewGroup) null);
        permView.setPermission(grp, perm, first, newPermPrefix, packageName, showRevokeUI);
        return permView;
    }

    private static synchronized View getPermissionItemViewOld(Context context, LayoutInflater inflater, CharSequence grpName, CharSequence permList, boolean dangerous, Drawable icon) {
        View permView = inflater.inflate(R.layout.app_permission_item_old, (ViewGroup) null);
        TextView permGrpView = (TextView) permView.findViewById(R.id.permission_group);
        TextView permDescView = (TextView) permView.findViewById(R.id.permission_list);
        ImageView imgView = (ImageView) permView.findViewById(R.id.perm_icon);
        imgView.setImageDrawable(icon);
        if (grpName != null) {
            permGrpView.setText(grpName);
            permDescView.setText(permList);
        } else {
            permGrpView.setText(permList);
            permDescView.setVisibility(8);
        }
        return permView;
    }

    private synchronized boolean isDisplayablePermission(PermissionInfo pInfo, int newReqFlags, int existingReqFlags) {
        int base = pInfo.protectionLevel & 15;
        boolean isNormal = base == 0;
        if (isNormal) {
            return false;
        }
        boolean isDangerous = base == 1 || (pInfo.protectionLevel & 128) != 0;
        boolean isRequired = (newReqFlags & 1) != 0;
        boolean isDevelopment = (pInfo.protectionLevel & 32) != 0;
        boolean wasGranted = (existingReqFlags & 2) != 0;
        boolean isGranted = (newReqFlags & 2) != 0;
        if (isDangerous && (isRequired || wasGranted || isGranted)) {
            return true;
        }
        return isDevelopment && wasGranted;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class PermissionGroupInfoComparator implements Comparator<MyPermissionGroupInfo> {
        private final Collator sCollator;

        private synchronized PermissionGroupInfoComparator() {
            this.sCollator = Collator.getInstance();
        }

        @Override // java.util.Comparator
        public final synchronized int compare(MyPermissionGroupInfo a, MyPermissionGroupInfo b) {
            return this.sCollator.compare(a.mLabel, b.mLabel);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class PermissionInfoComparator implements Comparator<MyPermissionInfo> {
        private final Collator sCollator = Collator.getInstance();

        synchronized PermissionInfoComparator() {
        }

        @Override // java.util.Comparator
        public final synchronized int compare(MyPermissionInfo a, MyPermissionInfo b) {
            return this.sCollator.compare(a.mLabel, b.mLabel);
        }
    }

    private synchronized void addPermToList(List<MyPermissionInfo> permList, MyPermissionInfo pInfo) {
        if (pInfo.mLabel == null) {
            pInfo.mLabel = pInfo.loadSafeLabel(this.mPm, 20000.0f, 5);
        }
        int idx = Collections.binarySearch(permList, pInfo, this.mPermComparator);
        if (idx < 0) {
            permList.add((-idx) - 1, pInfo);
        }
    }

    private synchronized void setPermissions(List<MyPermissionInfo> permList) {
        MyPermissionGroupInfo group;
        if (permList != null) {
            for (MyPermissionInfo pInfo : permList) {
                if (isDisplayablePermission(pInfo, pInfo.mNewReqFlags, pInfo.mExistingReqFlags) && (group = this.mPermGroups.get(pInfo.group)) != null) {
                    pInfo.mLabel = pInfo.loadSafeLabel(this.mPm, 20000.0f, 5);
                    addPermToList(group.mAllPermissions, pInfo);
                    if (pInfo.mNew) {
                        addPermToList(group.mNewPermissions, pInfo);
                    }
                }
            }
        }
        for (MyPermissionGroupInfo pgrp : this.mPermGroups.values()) {
            if (pgrp.labelRes != 0 || pgrp.nonLocalizedLabel != null) {
                pgrp.mLabel = pgrp.loadSafeLabel(this.mPm, 20000.0f, 5);
            } else {
                try {
                    ApplicationInfo app = this.mPm.getApplicationInfo(pgrp.packageName, 0);
                    pgrp.mLabel = app.loadSafeLabel(this.mPm, 20000.0f, 5);
                } catch (PackageManager.NameNotFoundException e) {
                    pgrp.mLabel = pgrp.loadSafeLabel(this.mPm, 20000.0f, 5);
                }
            }
            this.mPermGroupsList.add(pgrp);
        }
        Collections.sort(this.mPermGroupsList, this.mPermGroupComparator);
    }
}
