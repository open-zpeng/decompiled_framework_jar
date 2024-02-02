package com.xiaopeng.app;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
@SuppressLint({"ParcelCreator"})
/* loaded from: classes3.dex */
public class xpActivityInfo implements Parcelable {
    public static final Parcelable.Creator<xpActivityInfo> CREATOR = new Parcelable.Creator<xpActivityInfo>() { // from class: com.xiaopeng.app.xpActivityInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public xpActivityInfo createFromParcel(Parcel source) {
            return new xpActivityInfo(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public xpActivityInfo[] newArray(int size) {
            return new xpActivityInfo[size];
        }
    };
    public String className;
    public Intent intent;
    public String packageName;

    private xpActivityInfo(Intent intent, String packageName, String className) {
        this.intent = intent;
        this.className = className;
        this.packageName = packageName;
    }

    public static xpActivityInfo create(Intent intent) {
        return new xpActivityInfo(intent, null, null);
    }

    public static xpActivityInfo create(ComponentName component) {
        if (component == null) {
            return null;
        }
        return new xpActivityInfo(null, component.getPackageName(), component.getClassName());
    }

    public static xpActivityInfo create(String packageName, String className) {
        return new xpActivityInfo(null, packageName, className);
    }

    public static xpActivityInfo create(Intent intent, String packageName, String className) {
        return new xpActivityInfo(intent, packageName, className);
    }

    private xpActivityInfo(Parcel source) {
        this.intent = (Intent) source.readParcelable(null);
        this.packageName = source.readString();
        this.className = source.readString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int parcelableFlags) {
        dest.writeParcelable(this.intent, 0);
        dest.writeString(this.packageName);
        dest.writeString(this.className);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "xpActivityInfo packageName=" + this.packageName + " className=" + this.className + " intent=" + this.intent;
    }
}
