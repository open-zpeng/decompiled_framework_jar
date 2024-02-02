package com.xiaopeng.app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Parcel;
import android.os.Parcelable;
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
    public boolean foreground;
    public boolean fullscreen;
    public boolean hasFocus;
    public int hashCode;
    public String packageName;
    public int pid;
    public boolean shown;
    public int state;
    public int uid;
    public boolean visible;
    public int windowType;

    private xpDialogInfo() {
    }

    public static xpDialogInfo create(Dialog dialog) {
        if (dialog == null) {
            return null;
        }
        xpDialogInfo info = new xpDialogInfo();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window != null ? window.getAttributes() : null;
        View view = window != null ? window.getDecorView() : null;
        if (window != null && lp != null && view != null) {
            boolean systemDialog = xpWindowManager.isSystemAlertWindowType(lp);
            boolean fullscreen = xpWindowManager.isFullscreen(lp.systemUiVisibility | lp.subtreeSystemUiVisibility, lp.flags, lp.xpFlags);
            boolean z = false;
            boolean dimEnabled = (lp.flags & 2) == 2;
            info.hashCode = System.identityHashCode(dialog);
            info.windowType = lp.type;
            info.pid = dialog.myPid();
            info.uid = dialog.myUid();
            info.dimAmount = dimEnabled ? lp.dimAmount : 0.0f;
            info.packageName = dialog.getContext().getBasePackageName();
            info.shown = dialog.isShowing();
            info.visible = view.getVisibility() == 0 && view.isAttachedToWindow();
            if (fullscreen || systemDialog) {
                z = true;
            }
            info.fullscreen = z;
            info.cancelable = dialog.isCancelable();
            info.closeOutside = window.shouldCloseOnTouchOutside();
            info.hasFocus = dialog.hasFocus();
            info.foreground = info.hasFocus ? true : info.foreground;
            return info;
        }
        info.hashCode = System.identityHashCode(dialog);
        return info;
    }

    public static xpDialogInfo create(Dialog dialog, int state) {
        xpDialogInfo info = create(dialog);
        if (info != null) {
            info.state = state;
            info.foreground = state == 5 ? true : info.foreground;
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
            di.packageName = info.packageName;
            di.shown = info.shown;
            di.visible = info.visible;
            di.fullscreen = info.fullscreen;
            di.cancelable = info.cancelable;
            di.closeOutside = info.closeOutside;
            di.hasFocus = info.hasFocus;
            di.foreground = info.foreground;
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
        this.packageName = source.readString();
        this.shown = source.readBoolean();
        this.visible = source.readBoolean();
        this.fullscreen = source.readBoolean();
        this.cancelable = source.readBoolean();
        this.closeOutside = source.readBoolean();
        this.hasFocus = source.readBoolean();
        this.foreground = source.readBoolean();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int parcelableFlags) {
        dest.writeInt(this.hashCode);
        dest.writeInt(this.windowType);
        dest.writeInt(this.pid);
        dest.writeInt(this.uid);
        dest.writeInt(this.state);
        dest.writeFloat(this.dimAmount);
        dest.writeString(this.packageName);
        dest.writeBoolean(this.shown);
        dest.writeBoolean(this.visible);
        dest.writeBoolean(this.fullscreen);
        dest.writeBoolean(this.cancelable);
        dest.writeBoolean(this.closeOutside);
        dest.writeBoolean(this.hasFocus);
        dest.writeBoolean(this.foreground);
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
        buffer.append(" packageName=" + this.packageName);
        buffer.append(" shown=" + this.shown);
        buffer.append(" visible=" + this.visible);
        buffer.append(" fullscreen=" + this.fullscreen);
        buffer.append(" cancelable=" + this.cancelable);
        buffer.append(" closeOutside=" + this.closeOutside);
        buffer.append(" hasFocus=" + this.hasFocus);
        return buffer.toString();
    }

    public boolean topVisible() {
        int i = this.state;
        boolean z = false;
        if (i == 2 || i == 4 || i == 7) {
            return false;
        }
        boolean system = xpWindowManager.isSystemAlertWindowType(this.windowType);
        if (system) {
            if ((this.hasFocus || this.state == 5) && this.shown) {
                z = true;
            }
            boolean topVisible = z;
            return topVisible;
        }
        if ((this.hasFocus || this.state == 5) && this.shown && this.foreground) {
            z = true;
        }
        boolean topVisible2 = z;
        return topVisible2;
    }

    public boolean needRemove() {
        int i = this.state;
        if (i == 2 || i == 4 || i == 7) {
            return true;
        }
        boolean system = xpWindowManager.isSystemAlertWindowType(this.windowType);
        if (system) {
            return false;
        }
        boolean needRemove = true ^ this.foreground;
        return needRemove;
    }
}
