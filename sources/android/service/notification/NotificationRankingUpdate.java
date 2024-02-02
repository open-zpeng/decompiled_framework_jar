package android.service.notification;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes2.dex */
public class NotificationRankingUpdate implements Parcelable {
    public static final Parcelable.Creator<NotificationRankingUpdate> CREATOR = new Parcelable.Creator<NotificationRankingUpdate>() { // from class: android.service.notification.NotificationRankingUpdate.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NotificationRankingUpdate createFromParcel(Parcel parcel) {
            return new NotificationRankingUpdate(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public NotificationRankingUpdate[] newArray(int size) {
            return new NotificationRankingUpdate[size];
        }
    };
    private final Bundle mChannels;
    private final Bundle mHidden;
    private final int[] mImportance;
    private final Bundle mImportanceExplanation;
    private final String[] mInterceptedKeys;
    private final String[] mKeys;
    private final Bundle mOverrideGroupKeys;
    private final Bundle mOverridePeople;
    private final Bundle mShowBadge;
    private final Bundle mSnoozeCriteria;
    private final Bundle mSuppressedVisualEffects;
    private final Bundle mUserSentiment;
    private final Bundle mVisibilityOverrides;

    public synchronized NotificationRankingUpdate(String[] keys, String[] interceptedKeys, Bundle visibilityOverrides, Bundle suppressedVisualEffects, int[] importance, Bundle explanation, Bundle overrideGroupKeys, Bundle channels, Bundle overridePeople, Bundle snoozeCriteria, Bundle showBadge, Bundle userSentiment, Bundle hidden) {
        this.mKeys = keys;
        this.mInterceptedKeys = interceptedKeys;
        this.mVisibilityOverrides = visibilityOverrides;
        this.mSuppressedVisualEffects = suppressedVisualEffects;
        this.mImportance = importance;
        this.mImportanceExplanation = explanation;
        this.mOverrideGroupKeys = overrideGroupKeys;
        this.mChannels = channels;
        this.mOverridePeople = overridePeople;
        this.mSnoozeCriteria = snoozeCriteria;
        this.mShowBadge = showBadge;
        this.mUserSentiment = userSentiment;
        this.mHidden = hidden;
    }

    public synchronized NotificationRankingUpdate(Parcel in) {
        this.mKeys = in.readStringArray();
        this.mInterceptedKeys = in.readStringArray();
        this.mVisibilityOverrides = in.readBundle();
        this.mSuppressedVisualEffects = in.readBundle();
        this.mImportance = new int[this.mKeys.length];
        in.readIntArray(this.mImportance);
        this.mImportanceExplanation = in.readBundle();
        this.mOverrideGroupKeys = in.readBundle();
        this.mChannels = in.readBundle();
        this.mOverridePeople = in.readBundle();
        this.mSnoozeCriteria = in.readBundle();
        this.mShowBadge = in.readBundle();
        this.mUserSentiment = in.readBundle();
        this.mHidden = in.readBundle();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeStringArray(this.mKeys);
        out.writeStringArray(this.mInterceptedKeys);
        out.writeBundle(this.mVisibilityOverrides);
        out.writeBundle(this.mSuppressedVisualEffects);
        out.writeIntArray(this.mImportance);
        out.writeBundle(this.mImportanceExplanation);
        out.writeBundle(this.mOverrideGroupKeys);
        out.writeBundle(this.mChannels);
        out.writeBundle(this.mOverridePeople);
        out.writeBundle(this.mSnoozeCriteria);
        out.writeBundle(this.mShowBadge);
        out.writeBundle(this.mUserSentiment);
        out.writeBundle(this.mHidden);
    }

    public synchronized String[] getOrderedKeys() {
        return this.mKeys;
    }

    public synchronized String[] getInterceptedKeys() {
        return this.mInterceptedKeys;
    }

    public synchronized Bundle getVisibilityOverrides() {
        return this.mVisibilityOverrides;
    }

    public synchronized Bundle getSuppressedVisualEffects() {
        return this.mSuppressedVisualEffects;
    }

    public synchronized int[] getImportance() {
        return this.mImportance;
    }

    public synchronized Bundle getImportanceExplanation() {
        return this.mImportanceExplanation;
    }

    public synchronized Bundle getOverrideGroupKeys() {
        return this.mOverrideGroupKeys;
    }

    public synchronized Bundle getChannels() {
        return this.mChannels;
    }

    public synchronized Bundle getOverridePeople() {
        return this.mOverridePeople;
    }

    public synchronized Bundle getSnoozeCriteria() {
        return this.mSnoozeCriteria;
    }

    public synchronized Bundle getShowBadge() {
        return this.mShowBadge;
    }

    public synchronized Bundle getUserSentiment() {
        return this.mUserSentiment;
    }

    public synchronized Bundle getHidden() {
        return this.mHidden;
    }
}
