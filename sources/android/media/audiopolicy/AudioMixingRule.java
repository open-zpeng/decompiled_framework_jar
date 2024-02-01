package android.media.audiopolicy;

import android.annotation.SystemApi;
import android.media.AudioAttributes;
import android.os.Parcel;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
@SystemApi
/* loaded from: classes.dex */
public class AudioMixingRule {
    public static final int RULE_EXCLUDE_ATTRIBUTE_CAPTURE_PRESET = 32770;
    public static final int RULE_EXCLUDE_ATTRIBUTE_USAGE = 32769;
    public static final int RULE_EXCLUDE_UID = 32772;
    private static final int RULE_EXCLUSION_MASK = 32768;
    @SystemApi
    public static final int RULE_MATCH_ATTRIBUTE_CAPTURE_PRESET = 2;
    @SystemApi
    public static final int RULE_MATCH_ATTRIBUTE_USAGE = 1;
    @SystemApi
    public static final int RULE_MATCH_UID = 4;
    public protected final ArrayList<AudioMixMatchCriterion> mCriteria;
    private final int mTargetMixType;

    private synchronized AudioMixingRule(int mixType, ArrayList<AudioMixMatchCriterion> criteria) {
        this.mCriteria = criteria;
        this.mTargetMixType = mixType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class AudioMixMatchCriterion {
        public private protected final AudioAttributes mAttr;
        public private protected final int mIntProp;
        public private protected final int mRule;

        synchronized AudioMixMatchCriterion(AudioAttributes attributes, int rule) {
            this.mAttr = attributes;
            this.mIntProp = Integer.MIN_VALUE;
            this.mRule = rule;
        }

        synchronized AudioMixMatchCriterion(Integer intProp, int rule) {
            this.mAttr = null;
            this.mIntProp = intProp.intValue();
            this.mRule = rule;
        }

        public int hashCode() {
            return Objects.hash(this.mAttr, Integer.valueOf(this.mIntProp), Integer.valueOf(this.mRule));
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized void writeToParcel(Parcel dest) {
            dest.writeInt(this.mRule);
            int match_rule = this.mRule & (-32769);
            if (match_rule != 4) {
                switch (match_rule) {
                    case 1:
                        dest.writeInt(this.mAttr.getUsage());
                        return;
                    case 2:
                        dest.writeInt(this.mAttr.getCapturePreset());
                        return;
                    default:
                        Log.e("AudioMixMatchCriterion", "Unknown match rule" + match_rule + " when writing to Parcel");
                        dest.writeInt(-1);
                        return;
                }
            }
            dest.writeInt(this.mIntProp);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized boolean isAffectingUsage(int usage) {
        Iterator<AudioMixMatchCriterion> it = this.mCriteria.iterator();
        while (it.hasNext()) {
            AudioMixMatchCriterion criterion = it.next();
            if ((criterion.mRule & 1) != 0 && criterion.mAttr != null && criterion.mAttr.getUsage() == usage) {
                return true;
            }
        }
        return false;
    }

    private static synchronized boolean areCriteriaEquivalent(ArrayList<AudioMixMatchCriterion> cr1, ArrayList<AudioMixMatchCriterion> cr2) {
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
    public synchronized int getTargetMixType() {
        return this.mTargetMixType;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ArrayList<AudioMixMatchCriterion> getCriteria() {
        return this.mCriteria;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AudioMixingRule that = (AudioMixingRule) o;
        if (this.mTargetMixType == that.mTargetMixType && areCriteriaEquivalent(this.mCriteria, that.mCriteria)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Integer.valueOf(this.mTargetMixType), this.mCriteria);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized boolean isValidSystemApiRule(int rule) {
        if (rule != 4) {
            switch (rule) {
                case 1:
                case 2:
                    return true;
                default:
                    return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized boolean isValidAttributesSystemApiRule(int rule) {
        switch (rule) {
            case 1:
            case 2:
                return true;
            default:
                return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized boolean isValidRule(int rule) {
        int match_rule = (-32769) & rule;
        if (match_rule != 4) {
            switch (match_rule) {
                case 1:
                case 2:
                    return true;
                default:
                    return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized boolean isPlayerRule(int rule) {
        int match_rule = (-32769) & rule;
        if (match_rule == 1 || match_rule == 4) {
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized boolean isAudioAttributeRule(int match_rule) {
        switch (match_rule) {
            case 1:
            case 2:
                return true;
            default:
                return false;
        }
    }

    @SystemApi
    /* loaded from: classes.dex */
    public static class Builder {
        private int mTargetMixType = -1;
        private ArrayList<AudioMixMatchCriterion> mCriteria = new ArrayList<>();

        @SystemApi
        public Builder addRule(AudioAttributes attrToMatch, int rule) throws IllegalArgumentException {
            if (!AudioMixingRule.isValidAttributesSystemApiRule(rule)) {
                throw new IllegalArgumentException("Illegal rule value " + rule);
            }
            return checkAddRuleObjInternal(rule, attrToMatch);
        }

        @SystemApi
        public Builder excludeRule(AudioAttributes attrToMatch, int rule) throws IllegalArgumentException {
            if (!AudioMixingRule.isValidAttributesSystemApiRule(rule)) {
                throw new IllegalArgumentException("Illegal rule value " + rule);
            }
            return checkAddRuleObjInternal(32768 | rule, attrToMatch);
        }

        @SystemApi
        public Builder addMixRule(int rule, Object property) throws IllegalArgumentException {
            if (!AudioMixingRule.isValidSystemApiRule(rule)) {
                throw new IllegalArgumentException("Illegal rule value " + rule);
            }
            return checkAddRuleObjInternal(rule, property);
        }

        @SystemApi
        public Builder excludeMixRule(int rule, Object property) throws IllegalArgumentException {
            if (!AudioMixingRule.isValidSystemApiRule(rule)) {
                throw new IllegalArgumentException("Illegal rule value " + rule);
            }
            return checkAddRuleObjInternal(32768 | rule, property);
        }

        private synchronized Builder checkAddRuleObjInternal(int rule, Object property) throws IllegalArgumentException {
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

        private synchronized Builder addRuleInternal(AudioAttributes attrToMatch, Integer intProp, int rule) throws IllegalArgumentException {
            if (this.mTargetMixType == -1) {
                if (AudioMixingRule.isPlayerRule(rule)) {
                    this.mTargetMixType = 0;
                } else {
                    this.mTargetMixType = 1;
                }
            } else if ((this.mTargetMixType == 0 && !AudioMixingRule.isPlayerRule(rule)) || (this.mTargetMixType == 1 && AudioMixingRule.isPlayerRule(rule))) {
                throw new IllegalArgumentException("Incompatible rule for mix");
            }
            synchronized (this.mCriteria) {
                Iterator<AudioMixMatchCriterion> crIterator = this.mCriteria.iterator();
                int match_rule = (-32769) & rule;
                while (crIterator.hasNext()) {
                    AudioMixMatchCriterion criterion = crIterator.next();
                    if (match_rule != 4) {
                        switch (match_rule) {
                            case 1:
                                if (criterion.mAttr.getUsage() == attrToMatch.getUsage()) {
                                    if (criterion.mRule == rule) {
                                        return this;
                                    }
                                    throw new IllegalArgumentException("Contradictory rule exists for " + attrToMatch);
                                }
                                continue;
                            case 2:
                                if (criterion.mAttr.getCapturePreset() == attrToMatch.getCapturePreset()) {
                                    if (criterion.mRule == rule) {
                                        return this;
                                    }
                                    throw new IllegalArgumentException("Contradictory rule exists for " + attrToMatch);
                                }
                                continue;
                            default:
                                continue;
                        }
                    } else if (criterion.mIntProp == intProp.intValue()) {
                        if (criterion.mRule == rule) {
                            return this;
                        }
                        throw new IllegalArgumentException("Contradictory rule exists for UID " + intProp);
                    }
                }
                if (match_rule != 4) {
                    switch (match_rule) {
                        case 1:
                        case 2:
                            this.mCriteria.add(new AudioMixMatchCriterion(attrToMatch, rule));
                            break;
                        default:
                            throw new IllegalStateException("Unreachable code in addRuleInternal()");
                    }
                } else {
                    this.mCriteria.add(new AudioMixMatchCriterion(intProp, rule));
                }
                return this;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public synchronized Builder addRuleFromParcel(Parcel in) throws IllegalArgumentException {
            int rule = in.readInt();
            int match_rule = (-32769) & rule;
            AudioAttributes attr = null;
            Integer intProp = null;
            if (match_rule != 4) {
                switch (match_rule) {
                    case 1:
                        int usage = in.readInt();
                        attr = new AudioAttributes.Builder().setUsage(usage).build();
                        break;
                    case 2:
                        int preset = in.readInt();
                        attr = new AudioAttributes.Builder().setInternalCapturePreset(preset).build();
                        break;
                    default:
                        in.readInt();
                        throw new IllegalArgumentException("Illegal rule value " + rule + " in parcel");
                }
            } else {
                intProp = new Integer(in.readInt());
            }
            return addRuleInternal(attr, intProp, rule);
        }

        public AudioMixingRule build() {
            return new AudioMixingRule(this.mTargetMixType, this.mCriteria);
        }
    }
}
