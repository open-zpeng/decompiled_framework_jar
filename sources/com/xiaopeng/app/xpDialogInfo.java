package com.xiaopeng.app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.CalendarContract;
import android.service.quicksettings.TileService;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.xiaopeng.view.xpWindowManager;

@SuppressLint({"ParcelCreator"})
/* loaded from: classes3.dex */
public class xpDialogInfo implements Parcelable {
    public static final Parcelable.Creator<xpDialogInfo> CREATOR = new Parcelable.Creator<xpDialogInfo>() { // from class: com.xiaopeng.app.xpDialogInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public xpDialogInfo createFromParcel(Parcel source) {
            return new xpDialogInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public xpDialogInfo[] newArray(int size) {
            return new xpDialogInfo[size];
        }
    };
    public static final int STATE_ATTACH = 3;
    public static final int STATE_DEFAULT = 0;
    public static final int STATE_DETACH = 4;
    public static final int STATE_DISMISS = 7;
    public static final int STATE_FOCUS_OFF = 6;
    public static final int STATE_FOCUS_ON = 5;
    public static final int STATE_HIDE = 2;
    public static final int STATE_SHOW = 1;
    public static final int TYPE_APPLICATION = 2;
    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_SYSTEM = 1;
    public boolean cancelable;
    public boolean closeOutside;
    public float dimAmount;
    public boolean fullscreen;
    public boolean hasFocus;
    public int hashCode;
    public String packageName;
    public int pid;
    public boolean shown;
    public int state;
    public int subType;
    public String token;
    public int uid;
    public boolean visible;
    public int windowType;

    private xpDialogInfo() {
    }

    public static xpDialogInfo create(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        try {
            xpDialogInfo info = new xpDialogInfo();
            info.hashCode = 0;
            info.windowType = bundle.getInt("windowType", 9);
            info.subType = bundle.getInt("subType", -1);
            info.pid = bundle.getInt("pid", -1);
            info.uid = bundle.getInt("uid", -1);
            info.dimAmount = bundle.getFloat("dimAmount", 0.0f);
            info.token = bundle.getString(TileService.EXTRA_TOKEN, "");
            info.packageName = bundle.getString("packageName", "");
            info.shown = bundle.getBoolean("shown", true);
            info.visible = bundle.getBoolean(CalendarContract.CalendarColumns.VISIBLE, true);
            info.fullscreen = bundle.getBoolean("fullscreen", true);
            info.cancelable = bundle.getBoolean("cancelable", true);
            info.closeOutside = bundle.getBoolean("closeOutside", true);
            info.hasFocus = bundle.getBoolean("hasFocus", true);
            return info;
        } catch (Exception e) {
            return null;
        }
    }

    public static xpDialogInfo create(Dialog dialog) {
        if (dialog == null) {
            return null;
        }
        xpDialogInfo info = new xpDialogInfo();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window != null ? window.getAttributes() : null;
        if (window == null || lp == null) {
            return null;
        }
        View view = window.getDecorView();
        boolean systemDialog = xpWindowManager.isSystemAlertWindowType(lp);
        boolean fullscreen = xpWindowManager.isFullscreen(lp.systemUiVisibility | lp.subtreeSystemUiVisibility, lp.flags, lp.xpFlags);
        boolean z = true;
        boolean dimEnabled = (lp.flags & 2) == 2;
        info.hashCode = System.identityHashCode(dialog);
        info.windowType = lp.type;
        info.pid = dialog.myPid();
        info.uid = dialog.myUid();
        info.dimAmount = dimEnabled ? lp.dimAmount : 0.0f;
        info.token = lp.winToken;
        info.packageName = dialog.getContext().getBasePackageName();
        info.shown = dialog.isShowing();
        info.visible = view != null && view.getVisibility() == 0 && view.isAttachedToWindow();
        if (!fullscreen && !systemDialog) {
            z = false;
        }
        info.fullscreen = z;
        info.cancelable = dialog.isCancelable();
        info.closeOutside = window.shouldCloseOnTouchOutside();
        info.hasFocus = dialog.hasFocus();
        return info;
    }

    public static xpDialogInfo create(Dialog dialog, int state) {
        xpDialogInfo info = create(dialog);
        if (info != null) {
            info.state = state;
        }
        return info;
    }

    public static xpDialogInfo clone(xpDialogInfo info) {
        if (info != null) {
            xpDialogInfo di = new xpDialogInfo();
            di.hashCode = info.hashCode;
            di.windowType = info.windowType;
            di.pid = info.pid;
            di.uid = info.uid;
            di.state = info.state;
            di.dimAmount = info.dimAmount;
            di.token = info.token;
            di.packageName = info.packageName;
            di.shown = info.shown;
            di.visible = info.visible;
            di.fullscreen = info.fullscreen;
            di.cancelable = info.cancelable;
            di.closeOutside = info.closeOutside;
            di.hasFocus = info.hasFocus;
            di.subType = info.subType;
            return di;
        }
        return null;
    }

    private xpDialogInfo(Parcel source) {
        this.hashCode = source.readInt();
        this.windowType = source.readInt();
        this.pid = source.readInt();
        this.uid = source.readInt();
        this.state = source.readInt();
        this.dimAmount = source.readFloat();
        this.token = source.readString();
        this.packageName = source.readString();
        this.shown = source.readBoolean();
        this.visible = source.readBoolean();
        this.fullscreen = source.readBoolean();
        this.cancelable = source.readBoolean();
        this.closeOutside = source.readBoolean();
        this.hasFocus = source.readBoolean();
        this.subType = source.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int parcelableFlags) {
        dest.writeInt(this.hashCode);
        dest.writeInt(this.windowType);
        dest.writeInt(this.pid);
        dest.writeInt(this.uid);
        dest.writeInt(this.state);
        dest.writeFloat(this.dimAmount);
        dest.writeString(this.token);
        dest.writeString(this.packageName);
        dest.writeBoolean(this.shown);
        dest.writeBoolean(this.visible);
        dest.writeBoolean(this.fullscreen);
        dest.writeBoolean(this.cancelable);
        dest.writeBoolean(this.closeOutside);
        dest.writeBoolean(this.hasFocus);
        dest.writeInt(this.subType);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer("");
        buffer.append("DialogInfo");
        buffer.append(" hashCode=" + this.hashCode);
        buffer.append(" windowType=" + this.windowType);
        buffer.append(" pid=" + this.pid);
        buffer.append(" uid=" + this.uid);
        buffer.append(" state=" + this.state);
        buffer.append(" dimAmount=" + this.dimAmount);
        buffer.append(" token=" + this.token);
        buffer.append(" packageName=" + this.packageName);
        buffer.append(" shown=" + this.shown);
        buffer.append(" visible=" + this.visible);
        buffer.append(" fullscreen=" + this.fullscreen);
        buffer.append(" cancelable=" + this.cancelable);
        buffer.append(" closeOutside=" + this.closeOutside);
        buffer.append(" hasFocus=" + this.hasFocus);
        buffer.append(" subType=" + this.subType);
        return buffer.toString();
    }

    public boolean topVisible() {
        int i = this.state;
        if (i == 2 || i == 4 || i == 7) {
            return false;
        }
        return (this.hasFocus || i == 5) && this.visible && this.shown;
    }

    public boolean needRemove() {
        int i = this.state;
        if (i == 2 || i == 4 || i == 7) {
            return true;
        }
        return false;
    }

    public boolean isSystemDialog() {
        return xpWindowManager.isSystemAlertWindowType(this.windowType);
    }
}
