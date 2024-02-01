package android.permission;

import android.annotation.SystemApi;
import android.content.Context;
import android.content.pm.IPackageManager;
import android.content.pm.permission.SplitPermissionInfoParcelable;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.annotations.Immutable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SystemApi
/* loaded from: classes2.dex */
public final class PermissionManager {
    private static final String TAG = PermissionManager.class.getName();
    private final Context mContext;
    private final IPackageManager mPackageManager;
    private List<SplitPermissionInfo> mSplitPermissionInfos;

    public PermissionManager(Context context, IPackageManager packageManager) {
        this.mContext = context;
        this.mPackageManager = packageManager;
    }

    @SystemApi
    public int getRuntimePermissionsVersion() {
        try {
            return this.mPackageManager.getRuntimePermissionsVersion(this.mContext.getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void setRuntimePermissionsVersion(int version) {
        try {
            this.mPackageManager.setRuntimePermissionsVersion(version, this.mContext.getUserId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<SplitPermissionInfo> getSplitPermissions() {
        List<SplitPermissionInfo> list = this.mSplitPermissionInfos;
        if (list != null) {
            return list;
        }
        try {
            List<SplitPermissionInfoParcelable> parcelableList = this.mPackageManager.getSplitPermissions();
            this.mSplitPermissionInfos = splitPermissionInfoListToNonParcelableList(parcelableList);
            return this.mSplitPermissionInfos;
        } catch (RemoteException e) {
            Log.w(TAG, "Error getting split permissions", e);
            return Collections.emptyList();
        }
    }

    private List<SplitPermissionInfo> splitPermissionInfoListToNonParcelableList(List<SplitPermissionInfoParcelable> parcelableList) {
        int size = parcelableList.size();
        List<SplitPermissionInfo> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(new SplitPermissionInfo(parcelableList.get(i)));
        }
        return list;
    }

    public static List<SplitPermissionInfoParcelable> splitPermissionInfoListToParcelableList(List<SplitPermissionInfo> splitPermissionsList) {
        int size = splitPermissionsList.size();
        List<SplitPermissionInfoParcelable> outList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            SplitPermissionInfo info = splitPermissionsList.get(i);
            outList.add(new SplitPermissionInfoParcelable(info.getSplitPermission(), info.getNewPermissions(), info.getTargetSdk()));
        }
        return outList;
    }

    @Immutable
    /* loaded from: classes2.dex */
    public static final class SplitPermissionInfo {
        private final SplitPermissionInfoParcelable mSplitPermissionInfoParcelable;

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            SplitPermissionInfo that = (SplitPermissionInfo) o;
            return this.mSplitPermissionInfoParcelable.equals(that.mSplitPermissionInfoParcelable);
        }

        public int hashCode() {
            return this.mSplitPermissionInfoParcelable.hashCode();
        }

        public String getSplitPermission() {
            return this.mSplitPermissionInfoParcelable.getSplitPermission();
        }

        public List<String> getNewPermissions() {
            return this.mSplitPermissionInfoParcelable.getNewPermissions();
        }

        public int getTargetSdk() {
            return this.mSplitPermissionInfoParcelable.getTargetSdk();
        }

        public SplitPermissionInfo(String splitPerm, List<String> newPerms, int targetSdk) {
            this(new SplitPermissionInfoParcelable(splitPerm, newPerms, targetSdk));
        }

        private SplitPermissionInfo(SplitPermissionInfoParcelable parcelable) {
            this.mSplitPermissionInfoParcelable = parcelable;
        }
    }
}
