package com.xiaopeng.app;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes3.dex */
public class xpAppInfo implements Parcelable {
    public static final Parcelable.Creator<xpAppInfo> CREATOR = new Parcelable.Creator<xpAppInfo>() { // from class: com.xiaopeng.app.xpAppInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public xpAppInfo createFromParcel(Parcel source) {
            return new xpAppInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public xpAppInfo[] newArray(int size) {
            return new xpAppInfo[size];
        }
    };
    public int mLaunchMode;
    public String mLaunchParam;
    public Bitmap mXpAppIcon;
    public String mXpAppId;
    public String mXpAppPage;
    public String mXpAppTitle;
    public String pkgName;
    public String resId;
    public int supportScreenId;

    public xpAppInfo() {
    }

    private xpAppInfo(Parcel source) {
        this.pkgName = source.readString();
        this.resId = source.readString();
        this.mXpAppTitle = source.readString();
        this.mXpAppId = source.readString();
        this.mXpAppPage = source.readString();
        this.supportScreenId = source.readInt();
        this.mXpAppIcon = (Bitmap) source.readParcelable(Bitmap.class.getClassLoader());
        this.mLaunchMode = source.readInt();
        this.mLaunchParam = source.readString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int parcelableFlags) {
        dest.writeString(this.pkgName);
        dest.writeString(this.resId);
        dest.writeString(this.mXpAppTitle);
        dest.writeString(this.mXpAppId);
        dest.writeString(this.mXpAppPage);
        dest.writeInt(this.supportScreenId);
        dest.writeParcelable(this.mXpAppIcon, 0);
        dest.writeInt(this.mLaunchMode);
        dest.writeString(this.mLaunchParam);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "xpAppInfo{pkgName=" + this.pkgName + "'} ,mXpAppTitle=" + this.mXpAppTitle + " ,mXpAppId=" + this.mXpAppId + " ,mXpAppPage=" + this.mXpAppPage + " ,supportScreenId=" + this.supportScreenId + " ,mLaunchMode=" + this.mLaunchMode + " ,mLaunchParam=" + this.mLaunchParam;
    }
}
