package android.media.audiopolicy;

import android.media.AudioFormat;
import android.media.TtmlUtils;
import android.media.audiopolicy.AudioMix;
import android.media.audiopolicy.AudioMixingRule;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.SettingsStringUtil;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

/* loaded from: classes2.dex */
public class AudioPolicyConfig implements Parcelable {
    public static final Parcelable.Creator<AudioPolicyConfig> CREATOR = new Parcelable.Creator<AudioPolicyConfig>() { // from class: android.media.audiopolicy.AudioPolicyConfig.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AudioPolicyConfig createFromParcel(Parcel p) {
            return new AudioPolicyConfig(p);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public AudioPolicyConfig[] newArray(int size) {
            return new AudioPolicyConfig[size];
        }
    };
    private static final String TAG = "AudioPolicyConfig";
    protected int mDuckingPolicy;
    private int mMixCounter;
    protected final ArrayList<AudioMix> mMixes;
    private String mRegistrationId;

    protected AudioPolicyConfig(AudioPolicyConfig conf) {
        this.mDuckingPolicy = 0;
        this.mRegistrationId = null;
        this.mMixCounter = 0;
        this.mMixes = conf.mMixes;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioPolicyConfig(ArrayList<AudioMix> mixes) {
        this.mDuckingPolicy = 0;
        this.mRegistrationId = null;
        this.mMixCounter = 0;
        this.mMixes = mixes;
    }

    public void addMix(AudioMix mix) throws IllegalArgumentException {
        if (mix == null) {
            throw new IllegalArgumentException("Illegal null AudioMix argument");
        }
        this.mMixes.add(mix);
    }

    public ArrayList<AudioMix> getMixes() {
        return this.mMixes;
    }

    public int hashCode() {
        return Objects.hash(this.mMixes);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mMixes.size());
        Iterator<AudioMix> it = this.mMixes.iterator();
        while (it.hasNext()) {
            AudioMix mix = it.next();
            dest.writeInt(mix.getRouteFlags());
            dest.writeInt(mix.mCallbackFlags);
            dest.writeInt(mix.mDeviceSystemType);
            dest.writeString(mix.mDeviceAddress);
            dest.writeInt(mix.getFormat().getSampleRate());
            dest.writeInt(mix.getFormat().getEncoding());
            dest.writeInt(mix.getFormat().getChannelMask());
            dest.writeBoolean(mix.getRule().allowPrivilegedPlaybackCapture());
            ArrayList<AudioMixingRule.AudioMixMatchCriterion> criteria = mix.getRule().getCriteria();
            dest.writeInt(criteria.size());
            Iterator<AudioMixingRule.AudioMixMatchCriterion> it2 = criteria.iterator();
            while (it2.hasNext()) {
                AudioMixingRule.AudioMixMatchCriterion criterion = it2.next();
                criterion.writeToParcel(dest);
            }
        }
    }

    private AudioPolicyConfig(Parcel in) {
        this.mDuckingPolicy = 0;
        this.mRegistrationId = null;
        this.mMixCounter = 0;
        this.mMixes = new ArrayList<>();
        int nbMixes = in.readInt();
        for (int i = 0; i < nbMixes; i++) {
            AudioMix.Builder mixBuilder = new AudioMix.Builder();
            int routeFlags = in.readInt();
            mixBuilder.setRouteFlags(routeFlags);
            mixBuilder.setCallbackFlags(in.readInt());
            mixBuilder.setDevice(in.readInt(), in.readString());
            int sampleRate = in.readInt();
            int encoding = in.readInt();
            int channelMask = in.readInt();
            AudioFormat format = new AudioFormat.Builder().setSampleRate(sampleRate).setChannelMask(channelMask).setEncoding(encoding).build();
            mixBuilder.setFormat(format);
            AudioMixingRule.Builder ruleBuilder = new AudioMixingRule.Builder();
            ruleBuilder.allowPrivilegedPlaybackCapture(in.readBoolean());
            int nbRules = in.readInt();
            for (int j = 0; j < nbRules; j++) {
                ruleBuilder.addRuleFromParcel(in);
            }
            mixBuilder.setMixingRule(ruleBuilder.build());
            this.mMixes.add(mixBuilder.build());
        }
    }

    public String toLogFriendlyString() {
        String textDump;
        String textDump2 = new String("android.media.audiopolicy.AudioPolicyConfig:\n");
        String textDump3 = textDump2 + this.mMixes.size() + " AudioMix: " + this.mRegistrationId + "\n";
        Iterator<AudioMix> it = this.mMixes.iterator();
        while (it.hasNext()) {
            AudioMix mix = it.next();
            textDump3 = (((((textDump3 + "* route flags=0x" + Integer.toHexString(mix.getRouteFlags()) + "\n") + "  rate=" + mix.getFormat().getSampleRate() + "Hz\n") + "  encoding=" + mix.getFormat().getEncoding() + "\n") + "  channels=0x") + Integer.toHexString(mix.getFormat().getChannelMask()).toUpperCase() + "\n") + "  ignore playback capture opt out=" + mix.getRule().allowPrivilegedPlaybackCapture() + "\n";
            ArrayList<AudioMixingRule.AudioMixMatchCriterion> criteria = mix.getRule().getCriteria();
            Iterator<AudioMixingRule.AudioMixMatchCriterion> it2 = criteria.iterator();
            while (it2.hasNext()) {
                AudioMixingRule.AudioMixMatchCriterion criterion = it2.next();
                int i = criterion.mRule;
                if (i == 1) {
                    textDump = (textDump3 + "  match usage ") + criterion.mAttr.usageToString();
                } else if (i == 2) {
                    textDump = (textDump3 + "  match capture preset ") + criterion.mAttr.getCapturePreset();
                } else if (i == 4) {
                    textDump = (textDump3 + "  match UID ") + criterion.mIntProp;
                } else if (i != 32772) {
                    switch (i) {
                        case 32769:
                            textDump = (textDump3 + "  exclude usage ") + criterion.mAttr.usageToString();
                            break;
                        case 32770:
                            textDump = (textDump3 + "  exclude capture preset ") + criterion.mAttr.getCapturePreset();
                            break;
                        default:
                            textDump = textDump3 + "invalid rule!";
                            break;
                    }
                } else {
                    textDump = (textDump3 + "  exclude UID ") + criterion.mIntProp;
                }
                textDump3 = textDump + "\n";
            }
        }
        return textDump3;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setRegistration(String regId) {
        String str = this.mRegistrationId;
        boolean newRegNull = false;
        boolean currentRegNull = str == null || str.isEmpty();
        if (regId == null || regId.isEmpty()) {
            newRegNull = true;
        }
        if (!currentRegNull && !newRegNull && !this.mRegistrationId.equals(regId)) {
            Log.e(TAG, "Invalid registration transition from " + this.mRegistrationId + " to " + regId);
            return;
        }
        this.mRegistrationId = regId == null ? "" : regId;
        Iterator<AudioMix> it = this.mMixes.iterator();
        while (it.hasNext()) {
            AudioMix mix = it.next();
            setMixRegistration(mix);
        }
    }

    private void setMixRegistration(AudioMix mix) {
        if (!this.mRegistrationId.isEmpty()) {
            if ((mix.getRouteFlags() & 2) == 2) {
                mix.setRegistration(this.mRegistrationId + "mix" + mixTypeId(mix.getMixType()) + SettingsStringUtil.DELIMITER + this.mMixCounter);
            } else if ((mix.getRouteFlags() & 1) == 1) {
                mix.setRegistration(mix.mDeviceAddress);
            }
        } else {
            mix.setRegistration("");
        }
        this.mMixCounter++;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @GuardedBy({"mMixes"})
    public void add(ArrayList<AudioMix> mixes) {
        Iterator<AudioMix> it = mixes.iterator();
        while (it.hasNext()) {
            AudioMix mix = it.next();
            setMixRegistration(mix);
            this.mMixes.add(mix);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @GuardedBy({"mMixes"})
    public void remove(ArrayList<AudioMix> mixes) {
        Iterator<AudioMix> it = mixes.iterator();
        while (it.hasNext()) {
            AudioMix mix = it.next();
            this.mMixes.remove(mix);
        }
    }

    private static String mixTypeId(int type) {
        return type == 0 ? TtmlUtils.TAG_P : type == 1 ? "r" : "i";
    }

    protected String getRegistration() {
        return this.mRegistrationId;
    }
}
