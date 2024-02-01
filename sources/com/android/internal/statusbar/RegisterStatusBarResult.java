package com.android.internal.statusbar;

import android.graphics.Rect;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArrayMap;

/* loaded from: classes3.dex */
public final class RegisterStatusBarResult implements Parcelable {
    public static final Parcelable.Creator<RegisterStatusBarResult> CREATOR = new Parcelable.Creator<RegisterStatusBarResult>() { // from class: com.android.internal.statusbar.RegisterStatusBarResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RegisterStatusBarResult createFromParcel(Parcel source) {
            ArrayMap<String, StatusBarIcon> icons = source.createTypedArrayMap(StatusBarIcon.CREATOR);
            int disabledFlags1 = source.readInt();
            int systemUiVisibility = source.readInt();
            boolean menuVisible = source.readBoolean();
            int imeWindowVis = source.readInt();
            int imeBackDisposition = source.readInt();
            boolean showImeSwitcher = source.readBoolean();
            int disabledFlags2 = source.readInt();
            int fullscreenStackSysUiVisibility = source.readInt();
            int dockedStackSysUiVisibility = source.readInt();
            IBinder imeToken = source.readStrongBinder();
            Rect fullscreenStackBounds = (Rect) source.readTypedObject(Rect.CREATOR);
            Rect dockedStackBounds = (Rect) source.readTypedObject(Rect.CREATOR);
            boolean navbarColorManagedByIme = source.readBoolean();
            return new RegisterStatusBarResult(icons, disabledFlags1, systemUiVisibility, menuVisible, imeWindowVis, imeBackDisposition, showImeSwitcher, disabledFlags2, fullscreenStackSysUiVisibility, dockedStackSysUiVisibility, imeToken, fullscreenStackBounds, dockedStackBounds, navbarColorManagedByIme);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public RegisterStatusBarResult[] newArray(int size) {
            return new RegisterStatusBarResult[size];
        }
    };
    public final int mDisabledFlags1;
    public final int mDisabledFlags2;
    public final Rect mDockedStackBounds;
    public final int mDockedStackSysUiVisibility;
    public final Rect mFullscreenStackBounds;
    public final int mFullscreenStackSysUiVisibility;
    public final ArrayMap<String, StatusBarIcon> mIcons;
    public final int mImeBackDisposition;
    public final IBinder mImeToken;
    public final int mImeWindowVis;
    public final boolean mMenuVisible;
    public final boolean mNavbarColorManagedByIme;
    public final boolean mShowImeSwitcher;
    public final int mSystemUiVisibility;

    public RegisterStatusBarResult(ArrayMap<String, StatusBarIcon> icons, int disabledFlags1, int systemUiVisibility, boolean menuVisible, int imeWindowVis, int imeBackDisposition, boolean showImeSwitcher, int disabledFlags2, int fullscreenStackSysUiVisibility, int dockedStackSysUiVisibility, IBinder imeToken, Rect fullscreenStackBounds, Rect dockedStackBounds, boolean navbarColorManagedByIme) {
        this.mIcons = new ArrayMap<>(icons);
        this.mDisabledFlags1 = disabledFlags1;
        this.mSystemUiVisibility = systemUiVisibility;
        this.mMenuVisible = menuVisible;
        this.mImeWindowVis = imeWindowVis;
        this.mImeBackDisposition = imeBackDisposition;
        this.mShowImeSwitcher = showImeSwitcher;
        this.mDisabledFlags2 = disabledFlags2;
        this.mFullscreenStackSysUiVisibility = fullscreenStackSysUiVisibility;
        this.mDockedStackSysUiVisibility = dockedStackSysUiVisibility;
        this.mImeToken = imeToken;
        this.mFullscreenStackBounds = fullscreenStackBounds;
        this.mDockedStackBounds = dockedStackBounds;
        this.mNavbarColorManagedByIme = navbarColorManagedByIme;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArrayMap(this.mIcons, flags);
        dest.writeInt(this.mDisabledFlags1);
        dest.writeInt(this.mSystemUiVisibility);
        dest.writeBoolean(this.mMenuVisible);
        dest.writeInt(this.mImeWindowVis);
        dest.writeInt(this.mImeBackDisposition);
        dest.writeBoolean(this.mShowImeSwitcher);
        dest.writeInt(this.mDisabledFlags2);
        dest.writeInt(this.mFullscreenStackSysUiVisibility);
        dest.writeInt(this.mDockedStackSysUiVisibility);
        dest.writeStrongBinder(this.mImeToken);
        dest.writeTypedObject(this.mFullscreenStackBounds, flags);
        dest.writeTypedObject(this.mDockedStackBounds, flags);
        dest.writeBoolean(this.mNavbarColorManagedByIme);
    }
}
