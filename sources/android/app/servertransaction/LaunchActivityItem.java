package android.app.servertransaction;

import android.app.ActivityThread;
import android.app.ClientTransactionHandler;
import android.app.ProfilerInfo;
import android.app.ResultInfo;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.os.Trace;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.content.ReferrerIntent;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class LaunchActivityItem extends ClientTransactionItem {
    public static final Parcelable.Creator<LaunchActivityItem> CREATOR = new Parcelable.Creator<LaunchActivityItem>() { // from class: android.app.servertransaction.LaunchActivityItem.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LaunchActivityItem createFromParcel(Parcel in) {
            return new LaunchActivityItem(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public LaunchActivityItem[] newArray(int size) {
            return new LaunchActivityItem[size];
        }
    };
    private CompatibilityInfo mCompatInfo;
    private Configuration mCurConfig;
    private int mIdent;
    public protected ActivityInfo mInfo;
    public protected Intent mIntent;
    private boolean mIsForward;
    private Configuration mOverrideConfig;
    private List<ReferrerIntent> mPendingNewIntents;
    private List<ResultInfo> mPendingResults;
    private PersistableBundle mPersistentState;
    private int mProcState;
    private ProfilerInfo mProfilerInfo;
    private String mReferrer;
    private Bundle mState;
    private IVoiceInteractor mVoiceInteractor;

    @Override // android.app.servertransaction.BaseClientRequest
    public synchronized void preExecute(ClientTransactionHandler client, IBinder token) {
        client.updateProcessState(this.mProcState, false);
        client.updatePendingConfiguration(this.mCurConfig);
    }

    @Override // android.app.servertransaction.BaseClientRequest
    public synchronized void execute(ClientTransactionHandler client, IBinder token, PendingTransactionActions pendingActions) {
        Trace.traceBegin(64L, "activityStart");
        ActivityThread.ActivityClientRecord r = new ActivityThread.ActivityClientRecord(token, this.mIntent, this.mIdent, this.mInfo, this.mOverrideConfig, this.mCompatInfo, this.mReferrer, this.mVoiceInteractor, this.mState, this.mPersistentState, this.mPendingResults, this.mPendingNewIntents, this.mIsForward, this.mProfilerInfo, client);
        client.handleLaunchActivity(r, pendingActions, null);
        Trace.traceEnd(64L);
    }

    private synchronized LaunchActivityItem() {
    }

    public static synchronized LaunchActivityItem obtain(Intent intent, int ident, ActivityInfo info, Configuration curConfig, Configuration overrideConfig, CompatibilityInfo compatInfo, String referrer, IVoiceInteractor voiceInteractor, int procState, Bundle state, PersistableBundle persistentState, List<ResultInfo> pendingResults, List<ReferrerIntent> pendingNewIntents, boolean isForward, ProfilerInfo profilerInfo) {
        LaunchActivityItem instance = (LaunchActivityItem) ObjectPool.obtain(LaunchActivityItem.class);
        if (instance == null) {
            instance = new LaunchActivityItem();
        }
        setValues(instance, intent, ident, info, curConfig, overrideConfig, compatInfo, referrer, voiceInteractor, procState, state, persistentState, pendingResults, pendingNewIntents, isForward, profilerInfo);
        return instance;
    }

    @Override // android.app.servertransaction.ObjectPoolItem
    public synchronized void recycle() {
        setValues(this, null, 0, null, null, null, null, null, null, 0, null, null, null, null, false, null);
        ObjectPool.recycle(this);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedObject(this.mIntent, flags);
        dest.writeInt(this.mIdent);
        dest.writeTypedObject(this.mInfo, flags);
        dest.writeTypedObject(this.mCurConfig, flags);
        dest.writeTypedObject(this.mOverrideConfig, flags);
        dest.writeTypedObject(this.mCompatInfo, flags);
        dest.writeString(this.mReferrer);
        dest.writeStrongInterface(this.mVoiceInteractor);
        dest.writeInt(this.mProcState);
        dest.writeBundle(this.mState);
        dest.writePersistableBundle(this.mPersistentState);
        dest.writeTypedList(this.mPendingResults, flags);
        dest.writeTypedList(this.mPendingNewIntents, flags);
        dest.writeBoolean(this.mIsForward);
        dest.writeTypedObject(this.mProfilerInfo, flags);
    }

    private synchronized LaunchActivityItem(Parcel in) {
        setValues(this, (Intent) in.readTypedObject(Intent.CREATOR), in.readInt(), (ActivityInfo) in.readTypedObject(ActivityInfo.CREATOR), (Configuration) in.readTypedObject(Configuration.CREATOR), (Configuration) in.readTypedObject(Configuration.CREATOR), (CompatibilityInfo) in.readTypedObject(CompatibilityInfo.CREATOR), in.readString(), IVoiceInteractor.Stub.asInterface(in.readStrongBinder()), in.readInt(), in.readBundle(getClass().getClassLoader()), in.readPersistableBundle(getClass().getClassLoader()), in.createTypedArrayList(ResultInfo.CREATOR), in.createTypedArrayList(ReferrerIntent.CREATOR), in.readBoolean(), (ProfilerInfo) in.readTypedObject(ProfilerInfo.CREATOR));
    }

    public boolean equals(Object o) {
        boolean intentsEqual;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LaunchActivityItem other = (LaunchActivityItem) o;
        if ((this.mIntent == null && other.mIntent == null) || (this.mIntent != null && this.mIntent.filterEquals(other.mIntent))) {
            intentsEqual = true;
        } else {
            intentsEqual = false;
        }
        if (intentsEqual && this.mIdent == other.mIdent && activityInfoEqual(other.mInfo) && Objects.equals(this.mCurConfig, other.mCurConfig) && Objects.equals(this.mOverrideConfig, other.mOverrideConfig) && Objects.equals(this.mCompatInfo, other.mCompatInfo) && Objects.equals(this.mReferrer, other.mReferrer) && this.mProcState == other.mProcState && areBundlesEqual(this.mState, other.mState) && areBundlesEqual(this.mPersistentState, other.mPersistentState) && Objects.equals(this.mPendingResults, other.mPendingResults) && Objects.equals(this.mPendingNewIntents, other.mPendingNewIntents) && this.mIsForward == other.mIsForward && Objects.equals(this.mProfilerInfo, other.mProfilerInfo)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result = (31 * 17) + this.mIntent.filterHashCode();
        return (31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * ((31 * result) + this.mIdent)) + Objects.hashCode(this.mCurConfig))) + Objects.hashCode(this.mOverrideConfig))) + Objects.hashCode(this.mCompatInfo))) + Objects.hashCode(this.mReferrer))) + Objects.hashCode(Integer.valueOf(this.mProcState)))) + (this.mState != null ? this.mState.size() : 0))) + (this.mPersistentState != null ? this.mPersistentState.size() : 0))) + Objects.hashCode(this.mPendingResults))) + Objects.hashCode(this.mPendingNewIntents))) + (this.mIsForward ? 1 : 0))) + Objects.hashCode(this.mProfilerInfo);
    }

    private synchronized boolean activityInfoEqual(ActivityInfo other) {
        return this.mInfo == null ? other == null : other != null && this.mInfo.flags == other.flags && this.mInfo.maxAspectRatio == other.maxAspectRatio && Objects.equals(this.mInfo.launchToken, other.launchToken) && Objects.equals(this.mInfo.getComponentName(), other.getComponentName());
    }

    private static synchronized boolean areBundlesEqual(BaseBundle extras, BaseBundle newExtras) {
        if (extras == null || newExtras == null) {
            return extras == newExtras;
        } else if (extras.size() != newExtras.size()) {
            return false;
        } else {
            for (String key : extras.keySet()) {
                if (key != null) {
                    Object value = extras.get(key);
                    Object newValue = newExtras.get(key);
                    if (!Objects.equals(value, newValue)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    public String toString() {
        return "LaunchActivityItem{intent=" + this.mIntent + ",ident=" + this.mIdent + ",info=" + this.mInfo + ",curConfig=" + this.mCurConfig + ",overrideConfig=" + this.mOverrideConfig + ",referrer=" + this.mReferrer + ",procState=" + this.mProcState + ",state=" + this.mState + ",persistentState=" + this.mPersistentState + ",pendingResults=" + this.mPendingResults + ",pendingNewIntents=" + this.mPendingNewIntents + ",profilerInfo=" + this.mProfilerInfo + "}";
    }

    private static synchronized void setValues(LaunchActivityItem instance, Intent intent, int ident, ActivityInfo info, Configuration curConfig, Configuration overrideConfig, CompatibilityInfo compatInfo, String referrer, IVoiceInteractor voiceInteractor, int procState, Bundle state, PersistableBundle persistentState, List<ResultInfo> pendingResults, List<ReferrerIntent> pendingNewIntents, boolean isForward, ProfilerInfo profilerInfo) {
        instance.mIntent = intent;
        instance.mIdent = ident;
        instance.mInfo = info;
        instance.mCurConfig = curConfig;
        instance.mOverrideConfig = overrideConfig;
        instance.mCompatInfo = compatInfo;
        instance.mReferrer = referrer;
        instance.mVoiceInteractor = voiceInteractor;
        instance.mProcState = procState;
        instance.mState = state;
        instance.mPersistentState = persistentState;
        instance.mPendingResults = pendingResults;
        instance.mPendingNewIntents = pendingNewIntents;
        instance.mIsForward = isForward;
        instance.mProfilerInfo = profilerInfo;
    }
}
