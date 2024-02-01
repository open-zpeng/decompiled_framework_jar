package android.media.audiopolicy;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.media.AudioAttributes;
import android.os.Parcel;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

@SystemApi
/* loaded from: classes2.dex */
public class AudioMixingRule {
    public static final int RULE_EXCLUDE_ATTRIBUTE_CAPTURE_PRESET = 32770;
    public static final int RULE_EXCLUDE_ATTRIBUTE_USAGE = 32769;
    public static final int RULE_EXCLUDE_UID = 32772;
    private static final int RULE_EXCLUSION_MASK = 32768;
    public static final int RULE_MATCH_ATTRIBUTE_CAPTURE_PRESET = 2;
    public static final int RULE_MATCH_ATTRIBUTE_USAGE = 1;
    public static final int RULE_MATCH_UID = 4;
    @UnsupportedAppUsage
    private boolean mAllowPrivilegedPlaybackCapture;
    @UnsupportedAppUsage
    private final ArrayList<AudioMixMatchCriterion> mCriteria;
    private final int mTargetMixType;

    private AudioMixingRule(int mixType, ArrayList<AudioMixMatchCriterion> criteria, boolean allowPrivilegedPlaybackCapture) {
        this.mAllowPrivilegedPlaybackCapture = false;
        this.mCriteria = criteria;
        this.mTargetMixType = mixType;
        this.mAllowPrivilegedPlaybackCapture = allowPrivilegedPlaybackCapture;
    }

    /* loaded from: classes2.dex */
    public static final class AudioMixMatchCriterion {
        @UnsupportedAppUsage
        final AudioAttributes mAttr;
        @UnsupportedAppUsage
        final int mIntProp;
        @UnsupportedAppUsage
        final int mRule;

        AudioMixMatchCriterion(AudioAttributes attributes, int rule) {
            this.mAttr = attributes;
            this.mIntProp = Integer.MIN_VALUE;
            this.mRule = rule;
        }

        AudioMixMatchCriterion(Integer intProp, int rule) {
            this.mAttr = null;
            this.mIntProp = intProp.intValue();
            this.mRule = rule;
        }

        public int hashCode() {
            return Objects.hash(this.mAttr, Integer.valueOf(this.mIntProp), Integer.valueOf(this.mRule));
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void writeToParcel(Parcel dest) {
            dest.writeInt(this.mRule);
            int match_rule = this.mRule & (-32769);
            if (match_rule == 1) {
                dest.writeInt(this.mAttr.getUsage());
            } else if (match_rule == 2) {
                dest.writeInt(this.mAttr.getCapturePreset());
            } else if (match_rule == 4) {
                dest.writeInt(this.mIntProp);
            } else {
                Log.e("AudioMixMatchCriterion", "Unknown match rule" + match_rule + " when writing to Parcel");
                dest.writeInt(-1);
            }
        }

        public AudioAttributes getAudioAttributes() {
            return this.mAttr;
        }

        public int getIntProp() {
            return this.mIntProp;
        }

        public int getRule() {
            return this.mRule;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAffectingUsage(int usage) {
        Iterator<AudioMixMatchCriterion> it = this.mCriteria.iterator();
        while (it.hasNext()) {
            AudioMixMatchCriterion criterion = it.next();
            if ((criterion.mRule & 1) != 0 && criterion.mAttr != null && criterion.mAttr.getUsage() == usage) {
                return true;
            }
        }
        return false;
    }

    private static boolean areCriteriaEquivalent(ArrayList<AudioMixMatchCriterion> cr1, ArrayList<AudioMixMatchCriterion> cr2) {
        if (cr1 == null || cr2 == null) {
            return false;
        }
        if (cr1 == cr2) {
            return true;
        }
        if (cr1.size() != cr2.size() || cr1.hashCode() != cr2.hashCode()) {
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getTargetMixType() {
        return this.mTargetMixType;
    }

    public ArrayList<AudioMixMatchCriterion> getCriteria() {
        return this.mCriteria;
    }

    public boolean allowPrivilegedPlaybackCapture() {
        return this.mAllowPrivilegedPlaybackCapture;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AudioMixingRule that = (AudioMixingRule) o;
        if (this.mTargetMixType == that.mTargetMixType && areCriteriaEquivalent(this.mCriteria, that.mCriteria) && this.mAllowPrivilegedPlaybackCapture == that.mAllowPrivilegedPlaybackCapture) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mTargetMixType), this.mCriteria, Boolean.valueOf(this.mAllowPrivilegedPlaybackCapture));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidSystemApiRule(int rule) {
        if (rule == 1 || rule == 2 || rule == 4) {
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidAttributesSystemApiRule(int rule) {
        if (rule == 1 || rule == 2) {
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidRule(int rule) {
        int match_rule = (-32769) & rule;
        if (match_rule == 1 || match_rule == 2 || match_rule == 4) {
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isPlayerRule(int rule) {
        int match_rule = (-32769) & rule;
        if (match_rule == 1 || match_rule == 4) {
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isAudioAttributeRule(int match_rule) {
        if (match_rule == 1 || match_rule == 2) {
            return true;
        }
        return false;
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        private int mTargetMixType = -1;
        private boolean mAllowPrivilegedPlaybackCapture = false;
        private ArrayList<AudioMixMatchCriterion> mCriteria = new ArrayList<>();

        public Builder addRule(AudioAttributes attrToMatch, int rule) throws IllegalArgumentException {
            if (!AudioMixingRule.isValidAttributesSystemApiRule(rule)) {
                throw new IllegalArgumentException("Illegal rule value " + rule);
            }
            return checkAddRuleObjInternal(rule, attrToMatch);
        }

        public Builder excludeRule(AudioAttributes attrToMatch, int rule) throws IllegalArgumentException {
            if (!AudioMixingRule.isValidAttributesSystemApiRule(rule)) {
                throw new IllegalArgumentException("Illegal rule value " + rule);
            }
            return checkAddRuleObjInternal(32768 | rule, attrToMatch);
        }

        public Builder addMixRule(int rule, Object property) throws IllegalArgumentException {
            if (!AudioMixingRule.isValidSystemApiRule(rule)) {
                throw new IllegalArgumentException("Illegal rule value " + rule);
            }
            return checkAddRuleObjInternal(rule, property);
        }

        public Builder excludeMixRule(int rule, Object property) throws IllegalArgumentException {
            if (!AudioMixingRule.isValidSystemApiRule(rule)) {
                throw new IllegalArgumentException("Illegal rule value " + rule);
            }
            return checkAddRuleObjInternal(32768 | rule, property);
        }

        public Builder allowPrivilegedPlaybackCapture(boolean allow) {
            this.mAllowPrivilegedPlaybackCapture = allow;
            return this;
        }

        private Builder checkAddRuleObjInternal(int rule, Object property) throws IllegalArgumentException {
            if (property != null) {
                if (!AudioMixingRule.isValidRule(rule)) {
                    throw new IllegalArgumentException("Illegal rule value " + rule);
                }
                int match_rule = (-32769) & rule;
                if (AudioMixingRule.isAudioAttributeRule(match_rule)) {
                    if (!(property instanceof AudioAttributes)) {
                        throw new IllegalArgumentException("Invalid AudioAttributes argument");
                    }
                    return addRuleInternal((AudioAttributes) property, null, rule);
                } else if (!(property instanceof Integer)) {
                    throw new IllegalArgumentException("Invalid Integer argument");
                } else {
                    return addRuleInternal(null, (Integer) property, rule);
                }
            }
            throw new IllegalArgumentException("Illegal null argument for mixing rule");
        }

        private Builder addRuleInternal(AudioAttributes attrToMatch, Integer intProp, int rule) throws IllegalArgumentException {
            int i = this.mTargetMixType;
            if (i == -1) {
                if (AudioMixingRule.isPlayerRule(rule)) {
                    this.mTargetMixType = 0;
                } else {
                    this.mTargetMixType = 1;
                }
            } else if ((i == 0 && !AudioMixingRule.isPlayerRule(rule)) || (this.mTargetMixType == 1 && AudioMixingRule.isPlayerRule(rule))) {
                throw new IllegalArgumentException("Incompatible rule for mix");
            }
            synchronized (this.mCriteria) {
                Iterator<AudioMixMatchCriterion> crIterator = this.mCriteria.iterator();
                int match_rule = rule & (-32769);
                while (crIterator.hasNext()) {
                    AudioMixMatchCriterion criterion = crIterator.next();
                    if ((criterion.mRule & (-32769)) == match_rule) {
                        if (match_rule != 1) {
                            if (match_rule == 2) {
                                if (criterion.mAttr.getCapturePreset() == attrToMatch.getCapturePreset()) {
                                    if (criterion.mRule == rule) {
                                        return this;
                                    }
                                    throw new IllegalArgumentException("Contradictory rule exists for " + attrToMatch);
                                }
                            } else if (match_rule == 4 && criterion.mIntProp == intProp.intValue()) {
                                if (criterion.mRule == rule) {
                                    return this;
                                }
                                throw new IllegalArgumentException("Contradictory rule exists for UID " + intProp);
                            }
                        } else if (criterion.mAttr.getUsage() == attrToMatch.getUsage()) {
                            if (criterion.mRule == rule) {
                                return this;
                            }
                            throw new IllegalArgumentException("Contradictory rule exists for " + attrToMatch);
                        }
                    }
                }
                if (match_rule == 1 || match_rule == 2) {
                    this.mCriteria.add(new AudioMixMatchCriterion(attrToMatch, rule));
                } else if (match_rule == 4) {
                    this.mCriteria.add(new AudioMixMatchCriterion(intProp, rule));
                } else {
                    throw new IllegalStateException("Unreachable code in addRuleInternal()");
                }
                return this;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder addRuleFromParcel(Parcel in) throws IllegalArgumentException {
            int rule = in.readInt();
            int match_rule = (-32769) & rule;
            AudioAttributes attr = null;
            Integer intProp = null;
            if (match_rule == 1) {
                int usage = in.readInt();
                attr = new AudioAttributes.Builder().setUsage(usage).build();
            } else if (match_rule == 2) {
                int preset = in.readInt();
                attr = new AudioAttributes.Builder().setInternalCapturePreset(preset).build();
            } else if (match_rule == 4) {
                intProp = new Integer(in.readInt());
            } else {
                in.readInt();
                throw new IllegalArgumentException("Illegal rule value " + rule + " in parcel");
            }
            return addRuleInternal(attr, intProp, rule);
        }

        public AudioMixingRule build() {
            return new AudioMixingRule(this.mTargetMixType, this.mCriteria, this.mAllowPrivilegedPlaybackCapture);
        }
    }
}
