package com.xiaopeng.screen;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import com.xiaopeng.util.xpTextUtils;
import org.json.JSONObject;
/* loaded from: classes3.dex */
public class ScreenItemConfiguration implements Parcelable {
    public static final Parcelable.Creator<ScreenItemConfiguration> CREATOR = new Parcelable.Creator<ScreenItemConfiguration>() { // from class: com.xiaopeng.screen.ScreenItemConfiguration.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ScreenItemConfiguration createFromParcel(Parcel source) {
            return new ScreenItemConfiguration(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ScreenItemConfiguration[] newArray(int size) {
            return new ScreenItemConfiguration[size];
        }
    };
    public int displayId;
    public int displayType;
    public Rect logicalBounds;
    public Rect physicalBounds;
    public boolean screenEnabled;
    public int screenId;
    public String screenName;
    public int screenType;

    public static ScreenItemConfiguration fromText(String text) {
        ScreenItemConfiguration configuration = new ScreenItemConfiguration();
        try {
            JSONObject jObject = new JSONObject(text);
            configuration.screenId = ((Integer) xpTextUtils.getValue("screenId", jObject, -1)).intValue();
            configuration.screenType = ((Integer) xpTextUtils.getValue("screenType", jObject, -1)).intValue();
            configuration.screenName = (String) xpTextUtils.getValue("screenName", jObject, "");
            boolean z = true;
            if (((Integer) xpTextUtils.getValue("screenEnabled", jObject, 1)).intValue() != 1) {
                z = false;
            }
            configuration.screenEnabled = z;
            configuration.displayId = ((Integer) xpTextUtils.getValue("displayId", jObject, -1)).intValue();
            configuration.displayType = ((Integer) xpTextUtils.getValue("displayType", jObject, -1)).intValue();
            String logicalText = (String) xpTextUtils.getValue("logicalBounds", jObject, "");
            String physicalText = (String) xpTextUtils.getValue("physicalBounds", jObject, "");
            configuration.logicalBounds = Rect.unflattenFromString(logicalText);
            configuration.physicalBounds = Rect.unflattenFromString(physicalText);
        } catch (Exception e) {
        }
        return configuration;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" screenId=" + this.screenId);
        buffer.append(" screenType=" + this.screenType);
        buffer.append(" screenName=" + this.screenName);
        buffer.append(" screenEnabled=" + this.screenEnabled);
        buffer.append(" displayId=" + this.displayId);
        buffer.append(" displayType=" + this.displayType);
        buffer.append(" logicalBounds=" + this.logicalBounds);
        buffer.append(" physicalBounds=" + this.physicalBounds);
        return buffer.toString();
    }

    public ScreenItemConfiguration() {
    }

    private ScreenItemConfiguration(Parcel source) {
        this.screenId = source.readInt();
        this.screenType = source.readInt();
        this.screenName = source.readString();
        this.screenEnabled = source.readBoolean();
        this.displayId = source.readInt();
        this.displayType = source.readInt();
        if (source.readInt() != 0) {
            this.logicalBounds = Rect.CREATOR.createFromParcel(source);
        }
        if (source.readInt() != 0) {
            this.physicalBounds = Rect.CREATOR.createFromParcel(source);
        }
    }

    public static ScreenItemConfiguration clone(ScreenItemConfiguration source) {
        if (source == null) {
            return null;
        }
        ScreenItemConfiguration configuration = new ScreenItemConfiguration();
        configuration.screenId = source.screenId;
        configuration.screenType = source.screenType;
        configuration.screenName = source.screenName;
        configuration.screenEnabled = source.screenEnabled;
        configuration.displayId = source.displayId;
        configuration.displayType = source.displayType;
        configuration.logicalBounds = new Rect(source.logicalBounds);
        configuration.physicalBounds = new Rect(source.physicalBounds);
        return configuration;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int parcelableFlags) {
        dest.writeInt(this.screenId);
        dest.writeInt(this.screenType);
        dest.writeString(this.screenName);
        dest.writeBoolean(this.screenEnabled);
        dest.writeInt(this.displayId);
        dest.writeInt(this.displayType);
        if (this.logicalBounds != null) {
            dest.writeInt(1);
            this.logicalBounds.writeToParcel(dest, 0);
        } else {
            dest.writeInt(0);
        }
        if (this.physicalBounds != null) {
            dest.writeInt(1);
            this.physicalBounds.writeToParcel(dest, 0);
            return;
        }
        dest.writeInt(0);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }
}
