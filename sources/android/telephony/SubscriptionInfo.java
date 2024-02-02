package android.telephony;

import android.annotation.SystemApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes2.dex */
public class SubscriptionInfo implements Parcelable {
    public static final Parcelable.Creator<SubscriptionInfo> CREATOR = new Parcelable.Creator<SubscriptionInfo>() { // from class: android.telephony.SubscriptionInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SubscriptionInfo createFromParcel(Parcel source) {
            int id = source.readInt();
            String iccId = source.readString();
            int simSlotIndex = source.readInt();
            CharSequence displayName = source.readCharSequence();
            CharSequence carrierName = source.readCharSequence();
            int nameSource = source.readInt();
            int iconTint = source.readInt();
            String number = source.readString();
            int dataRoaming = source.readInt();
            int mcc = source.readInt();
            int mnc = source.readInt();
            String countryIso = source.readString();
            Bitmap iconBitmap = Bitmap.CREATOR.createFromParcel(source);
            boolean isEmbedded = source.readBoolean();
            UiccAccessRule[] accessRules = (UiccAccessRule[]) source.createTypedArray(UiccAccessRule.CREATOR);
            String cardId = source.readString();
            return new SubscriptionInfo(id, iccId, simSlotIndex, displayName, carrierName, nameSource, iconTint, number, dataRoaming, iconBitmap, mcc, mnc, countryIso, isEmbedded, accessRules, cardId);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public SubscriptionInfo[] newArray(int size) {
            return new SubscriptionInfo[size];
        }
    };
    private static final int TEXT_SIZE = 16;
    private UiccAccessRule[] mAccessRules;
    private String mCardId;
    private CharSequence mCarrierName;
    private String mCountryIso;
    private int mDataRoaming;
    private CharSequence mDisplayName;
    private String mIccId;
    private Bitmap mIconBitmap;
    private int mIconTint;
    private int mId;
    private boolean mIsEmbedded;
    private int mMcc;
    private int mMnc;
    private int mNameSource;
    private String mNumber;
    private int mSimSlotIndex;

    public synchronized SubscriptionInfo(int id, String iccId, int simSlotIndex, CharSequence displayName, CharSequence carrierName, int nameSource, int iconTint, String number, int roaming, Bitmap icon, int mcc, int mnc, String countryIso) {
        this(id, iccId, simSlotIndex, displayName, carrierName, nameSource, iconTint, number, roaming, icon, mcc, mnc, countryIso, false, null, null);
    }

    public synchronized SubscriptionInfo(int id, String iccId, int simSlotIndex, CharSequence displayName, CharSequence carrierName, int nameSource, int iconTint, String number, int roaming, Bitmap icon, int mcc, int mnc, String countryIso, boolean isEmbedded, UiccAccessRule[] accessRules) {
        this(id, iccId, simSlotIndex, displayName, carrierName, nameSource, iconTint, number, roaming, icon, mcc, mnc, countryIso, isEmbedded, accessRules, null);
    }

    public synchronized SubscriptionInfo(int id, String iccId, int simSlotIndex, CharSequence displayName, CharSequence carrierName, int nameSource, int iconTint, String number, int roaming, Bitmap icon, int mcc, int mnc, String countryIso, boolean isEmbedded, UiccAccessRule[] accessRules, String cardId) {
        this.mId = id;
        this.mIccId = iccId;
        this.mSimSlotIndex = simSlotIndex;
        this.mDisplayName = displayName;
        this.mCarrierName = carrierName;
        this.mNameSource = nameSource;
        this.mIconTint = iconTint;
        this.mNumber = number;
        this.mDataRoaming = roaming;
        this.mIconBitmap = icon;
        this.mMcc = mcc;
        this.mMnc = mnc;
        this.mCountryIso = countryIso;
        this.mIsEmbedded = isEmbedded;
        this.mAccessRules = accessRules;
        this.mCardId = cardId;
    }

    public int getSubscriptionId() {
        return this.mId;
    }

    public String getIccId() {
        return this.mIccId;
    }

    public int getSimSlotIndex() {
        return this.mSimSlotIndex;
    }

    public CharSequence getDisplayName() {
        return this.mDisplayName;
    }

    private protected void setDisplayName(CharSequence name) {
        this.mDisplayName = name;
    }

    public CharSequence getCarrierName() {
        return this.mCarrierName;
    }

    public synchronized void setCarrierName(CharSequence name) {
        this.mCarrierName = name;
    }

    private protected int getNameSource() {
        return this.mNameSource;
    }

    public Bitmap createIconBitmap(Context context) {
        int width = this.mIconBitmap.getWidth();
        int height = this.mIconBitmap.getHeight();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        Bitmap workingBitmap = Bitmap.createBitmap(metrics, width, height, this.mIconBitmap.getConfig());
        Canvas canvas = new Canvas(workingBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(this.mIconTint, PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(this.mIconBitmap, 0.0f, 0.0f, paint);
        paint.setColorFilter(null);
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.create("sans-serif", 0));
        paint.setColor(-1);
        paint.setTextSize(16.0f * metrics.density);
        String index = String.format("%d", Integer.valueOf(this.mSimSlotIndex + 1));
        Rect textBound = new Rect();
        paint.getTextBounds(index, 0, 1, textBound);
        float xOffset = (width / 2.0f) - textBound.centerX();
        float yOffset = (height / 2.0f) - textBound.centerY();
        canvas.drawText(index, xOffset, yOffset, paint);
        return workingBitmap;
    }

    public int getIconTint() {
        return this.mIconTint;
    }

    private protected void setIconTint(int iconTint) {
        this.mIconTint = iconTint;
    }

    public String getNumber() {
        return this.mNumber;
    }

    public int getDataRoaming() {
        return this.mDataRoaming;
    }

    public int getMcc() {
        return this.mMcc;
    }

    public int getMnc() {
        return this.mMnc;
    }

    public String getCountryIso() {
        return this.mCountryIso;
    }

    public boolean isEmbedded() {
        return this.mIsEmbedded;
    }

    @Deprecated
    public synchronized boolean canManageSubscription(Context context) {
        return canManageSubscription(context, context.getPackageName());
    }

    @Deprecated
    public synchronized boolean canManageSubscription(Context context, String packageName) {
        UiccAccessRule[] uiccAccessRuleArr;
        if (!isEmbedded()) {
            throw new UnsupportedOperationException("Not an embedded subscription");
        }
        if (this.mAccessRules == null) {
            return false;
        }
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 64);
            for (UiccAccessRule rule : this.mAccessRules) {
                if (rule.getCarrierPrivilegeStatus(packageInfo) == 1) {
                    return true;
                }
            }
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalArgumentException("Unknown package: " + packageName, e);
        }
    }

    @SystemApi
    public List<UiccAccessRule> getAccessRules() {
        if (!isEmbedded()) {
            throw new UnsupportedOperationException("Not an embedded subscription");
        }
        if (this.mAccessRules == null) {
            return null;
        }
        return Arrays.asList(this.mAccessRules);
    }

    public synchronized String getCardId() {
        return this.mCardId;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mIccId);
        dest.writeInt(this.mSimSlotIndex);
        dest.writeCharSequence(this.mDisplayName);
        dest.writeCharSequence(this.mCarrierName);
        dest.writeInt(this.mNameSource);
        dest.writeInt(this.mIconTint);
        dest.writeString(this.mNumber);
        dest.writeInt(this.mDataRoaming);
        dest.writeInt(this.mMcc);
        dest.writeInt(this.mMnc);
        dest.writeString(this.mCountryIso);
        this.mIconBitmap.writeToParcel(dest, flags);
        dest.writeBoolean(this.mIsEmbedded);
        dest.writeTypedArray(this.mAccessRules, flags);
        dest.writeString(this.mCardId);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public static synchronized String givePrintableIccid(String iccId) {
        if (iccId == null) {
            return null;
        }
        if (iccId.length() > 9 && !Build.IS_DEBUGGABLE) {
            String iccIdToPrint = iccId.substring(0, 9) + Rlog.pii(false, (Object) iccId.substring(9));
            return iccIdToPrint;
        }
        return iccId;
    }

    public String toString() {
        String iccIdToPrint = givePrintableIccid(this.mIccId);
        String cardIdToPrint = givePrintableIccid(this.mCardId);
        return "{id=" + this.mId + ", iccId=" + iccIdToPrint + " simSlotIndex=" + this.mSimSlotIndex + " displayName=" + ((Object) this.mDisplayName) + " carrierName=" + ((Object) this.mCarrierName) + " nameSource=" + this.mNameSource + " iconTint=" + this.mIconTint + " dataRoaming=" + this.mDataRoaming + " iconBitmap=" + this.mIconBitmap + " mcc " + this.mMcc + " mnc " + this.mMnc + " isEmbedded " + this.mIsEmbedded + " accessRules " + Arrays.toString(this.mAccessRules) + " cardId=" + cardIdToPrint + "}";
    }
}
